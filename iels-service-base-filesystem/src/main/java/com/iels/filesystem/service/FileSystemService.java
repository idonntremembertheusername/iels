package com.iels.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.iels.filesystem.dao.FileSystemRepository;
import com.iels.framework.domain.filesystem.FileSystem;
import com.iels.framework.domain.filesystem.response.FileSystemCode;
import com.iels.framework.domain.filesystem.response.UploadFileResult;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @ClassName: FileSystemService
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/3 20
 * @Other:
 **/
@Service
public class FileSystemService {

    @Autowired
    private FileSystemRepository fileSystemRepository;

    //初始化上传到FastDFS的环境参数
    @Value("${iels.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${iels.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${iels.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${iels.fastdfs.charset}")
    String charset;

    /*
     * @description: 上传文件
     * @author: snypxk
     * @date: 2019/12/3
     * @param multipartFile - 文件
     * @param filetag       - 文件标签
     * @param businesskey   - 业务key
     * @param metadata      - 元信息,json格式
     * @return: com.xuecheng.framework.domain.filesystem.response.UploadFileResult
     * @note:
     **/
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        if (multipartFile == null) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOAD_FILE_ISNULL);
        }
        //第一步:将文件上传到FastDFS中,得到一个文件ID
        String fileId = this.fdfs_upload(multipartFile);
        if (StringUtils.isEmpty(fileId)) {
            ExceptionCast.cast(FileSystemCode.FS_UPLOAD_FILE_SERVER_FAIL);
        }
        //第二步:将文件ID及其他文件信息存储到mongodb中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if (StringUtils.isNoneEmpty(metadata)) {
            Map map = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(map);
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

    /*
     * @description: 将文件上传到FastDFS中
     * @author: snypxk
     * @param multipartFile - 文件
     * @return: java.lang.String
     **/
    private String fdfs_upload(MultipartFile multipartFile) {
        //初始化fastDFS
        this.initFastDFSConfig();
        //创建 TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            //得到Storage服务
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建StorageClient1来上传文件
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storeStorage);
            //上传文件
            //得到文件字节
            byte[] bytes = multipartFile.getBytes();
            //得到文件的原始名称
            String originalFilename = multipartFile.getOriginalFilename();
            //得到文件扩展名
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = null;
            try {
                fileId = storageClient1.upload_file1(bytes, ext, null);
            } catch (MyException e) {
                e.printStackTrace();
            }
            return fileId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @description: 初始化FastDFS的环境
     * @author: snypxk
     **/
    private void initFastDFSConfig() {
        try {
            //初始化tracker服务地址(多个tracker中间以逗号分隔)
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出异常
            ExceptionCast.cast(FileSystemCode.INIT_FDFS_ERROR);
        }
    }
}
