package com.iels.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.iels.framework.domain.media.MediaFile;
import com.iels.framework.domain.media.response.CheckChunkResult;
import com.iels.framework.domain.media.response.MediaCode;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.ResponseResult;
import com.iels.manage_media.config.RabbitMQConfig;
import com.iels.manage_media.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/11 23
 * @Other:
 **/
@Service
@Slf4j
public class MediaUploadService {

    @Autowired
    private MediaFileRepository mediaFileRepository;

    //文件上传到服务器的路径位置: D:/IDEA_workspace/iels/video/
    @Value("${iels-service-manage-media.upload-location}")
    String uploadPath;

    //向MQ发消息
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //视频处理路由[路由key]
    @Value("${iels-service-manage-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    /*
     * @description: 获取 文件所属目录的路径
     * @author: snypxk
     * @param fileMd5
     * @return: java.lang.String
     *  文件目录路径定义规则: uploadPath/fileMd5第一个字符/fileMd5第二个字符/fileMd5/
     *  eg: fileMd5 = f29939a25efabaef3b87e2cbfe641315
     *  则路径是: getFileFolderPath()
     *              = D:/IDEA_workspace/iels/vedio/f/2/f29939a25efabaef3b87e2cbfe641315/
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     **/
    private String getFileFolderPath(String fileMd5) {
        return uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
    }

    /*
     * @description: 获取 文件的路径
     * @author: snypxk
     * @param fileMd5
     * @param fileExt - 文件扩展名
     * @return: java.lang.String - D:/IDEA_workspace/iels/vedio/f/2/f29939a25efabaef3b87e2cbfe641315/f29939a25efabaef3b87e2cbfe641315.avi
     * 根据文件md5得到文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：md5+文件扩展名
     **/
    private String getFilePath(String fileMd5, String fileExt) {
        return uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5
                + "/" + fileMd5 + "." + fileExt;
    }

    /*
     * @description: 文件上传前 - 先检查文件是否已经存在,不存在就注册[本质是创建对应的目录文件夹]
     * @author: snypxk
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return: com.xuecheng.framework.model.response.ResponseResult
     *      检查文件是否上传, 已上传则直接返回.
     *      检查文件上传路径是否存在, 不存在则创建.
     **/
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //1.检查文件在服务器的存储器[磁盘]上是否已经存在
        //1.1.获取 文件所属目录的路径
        String fileFolderPath = this.getFileFolderPath(fileMd5);
        //1.2.获取 文件的路径
        String filePath = this.getFilePath(fileMd5, fileExt);
        File file = new File(filePath);
        //文件是否在在服务器的存储器[磁盘]中已经存在
        boolean exists = file.exists();
        //1.3.检查文件信息在mongodb是否存在
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        if (exists && optional.isPresent()) {
            //文件已经存在: 抛异常返回对应信息
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件不存在: 此时应该先检查文件所在目录是否已经存在,若不存在则创建
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            boolean mkdirs = fileFolder.mkdirs();
            if (!mkdirs) {
                ExceptionCast.cast(MediaCode.CREATE_FOLDER_FAILURE);
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 得到块文件所在目录
     * @author: snypxk
     * @param fileMd5
     * @return: java.lang.String - D:/IDEA_workspace/iels/vedio/f/2/f29939a25efabaef3b87e2cbfe641315/
     **/
    private String getChunkFileFolderPath(String fileMd5) {
        //return this.getFileFolderPath(fileMd5) + "/" + "chunks" + "/";
        return this.getFileFolderPath(fileMd5) + "chunks" + "/";
    }

    /*
     * @description: 检查块文件
     * @author: snypxk
     * @param fileMd5
     * @param chunk - 块的下标[实际也是块的名称]
     * @param chunkSize
     * @return: com.xuecheng.framework.domain.media.response.CheckChunkResult
     **/
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //检查分块文件是否上传, 已上传则返回true.
        //先得到块文件所在目录路径
        String chunkfileFolderPath = this.getChunkFileFolderPath(fileMd5);
        //块文件的文件名称以1,2,3..序号命名，没有扩展名
        File chunkFile = new File(chunkfileFolderPath + chunk);
        if (chunkFile.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    /*
     * @description: 创建块文件目录
     * @author: snypxk
     * @param fileMd5
     * @return: boolean
     **/
    private boolean createChunkFileFolder(String fileMd5) {
        //创建上传文件目录
        String chunkFileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            //创建目录文件夹
            return chunkFileFolder.mkdirs();
        }
        return true;
    }

    /*
     * @description: 块文件上传
     * @author: snypxk
     * @param file
     * @param fileMd5
     * @param chunk
     * @return: com.xuecheng.framework.model.response.ResponseResult
     **/
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        if (file == null) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_ISNULL);
        }
        //创建块文件目录: 先判断是否已经存在,不存在就创建
        boolean fileFold = this.createChunkFileFolder(fileMd5);
        //块文件[分块文件的路径]
        File chunkfile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        //上传的块文件输入流
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkfile);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("upload chunk file fail:{}", e.getMessage());
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 校验文件的md5值
     * @author: snypxk
     * @param mergeFile - 合并后的文件的MD5值
     * @param md5 - 前端传过来的MD5值
     * @return: boolean
     **/
    private boolean checkFileMd5(File mergeFile, String md5) {
        if (mergeFile == null || StringUtils.isEmpty(md5)) {
            return false;
        }
        //进行md5校验
        FileInputStream mergeFileInputstream = null;
        try {
            mergeFileInputstream = new FileInputStream(mergeFile);
            //得到文件的md5
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileInputstream);
            //忽略大小写去比较md5
            if (md5.equalsIgnoreCase(mergeFileMd5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("checkFileMd5 error,file is:{},md5 is:{}", mergeFile.getAbsoluteFile(), md5);
        } finally {
            try {
                mergeFileInputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
     * @description: 获取所有块文件[得到一个块文件列表]
     * @author: snypxk
     * @param chunkfileFolder
     * @return: java.util.List<java.io.File>
     **/
    private List<File> getChunkFiles(File chunkfileFolder) {
        //获取路径下的所有块文件
        File[] chunkFiles = chunkfileFolder.listFiles();
        //将文件数组转成list，并排序
        List<File> chunkFileList = new ArrayList<File>();
        chunkFileList.addAll(Arrays.asList(chunkFiles));
        //排序
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        return chunkFileList;
    }

    /*
     * @description: 合并文件 - 执行合并
     * @author: snypxk
     * @param mergeFile - 合并的初始化空白文件
     * @param chunkFiles - 待合并的块文件列表
     * @return: java.io.File - 合并后得到的文件
     **/
    private File mergeFile(File mergeFile, List<File> chunkFiles) {
        try {
            //创建写文件对象
            RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
            //遍历分块文件开始合并
            //读取文件缓冲区
            byte[] b = new byte[1024];
            for (File chunkFile : chunkFiles) {
                RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
                int len = -1;
                //读取分块文件
                while ((len = raf_read.read(b)) != -1) {
                    //向合并文件中写数据
                    raf_write.write(b, 0, len);
                }
                raf_read.close();
            }
            raf_write.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("merge file error:{}", e.getMessage());
            return null;
        }
        return mergeFile;
    }

    /*
     * @description: 向MQ发送视频处理消息
     * @author: snypxk
     * @param mediaId - 路由key[也是上传合并成功后得到的文件的ID]
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    public ResponseResult sendProcessVideoMsg(String mediaId) {
        //先查询数据库判断mediaId是否正确
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //构建消息内容
        Map<String, String> map = new HashMap<>();
        map.put("mediaId", mediaId);
        String messageJsonString = JSON.toJSONString(map);
        try {
            //向MQ发送消息: 先指定交换机 + 路由 + 消息内容
            rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, messageJsonString);
        } catch (AmqpException e) {
            e.printStackTrace();
            //发送消息失败
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 合并块文件
     * @author: snypxk
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return: com.iels.framework.model.response.ResponseResult
     *      1).将块文件合并
     *      2).校验文件md5是否正确
     *      3.1).如果检验正确,向Mongodb的数据库[iels_media]的集合[]写入文件信息
     *      3.2).向MQ发消息处理视频,将其处理成m3u8格式
     **/
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype,
                                      String fileExt, String companyId, String userId) {
        //获取块文件的路径
        String chunkfileFolderPath = this.getChunkFileFolderPath(fileMd5);
        File chunkfileFolder = new File(chunkfileFolderPath);
        if (!chunkfileFolder.exists()) {
            chunkfileFolder.mkdirs();
        }
        //合并文件的路径
        File mergeFile = new File(this.getFilePath(fileMd5, fileExt));
        //创建合并文件
        //先判断合并文件是否存在: 若存在,则先删除再创建
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        boolean newFile = false;
        try {
            //创建[空白的]合并文件
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("mergechunks..create mergeFile fail:{}", e.getMessage());
        }
        if (!newFile) {
            //如果合并文件创建失败,就抛出异常
            ExceptionCast.cast(MediaCode.MERGE_FILE_CREATEFAIL);
        }
        //获取块文件，此列表是已经排好序的列表
        List<File> chunkFiles = this.getChunkFiles(chunkfileFolder);
        //==== 执行合并: 合并文件
        mergeFile = this.mergeFile(mergeFile, chunkFiles);
        if (mergeFile == null) {
            //合并失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        //==== 校验文件
        boolean checkResult = this.checkFileMd5(mergeFile, fileMd5);
        if (!checkResult) {
            //校验文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        String fileRelativePath = fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
        mediaFile.setFilePath(fileRelativePath);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        mediaFile.setUserId(userId);
        mediaFile.setCompanyId(companyId);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);
        //向MQ发送视频处理消息:
        sendProcessVideoMsg(mediaFile.getFileId());
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
