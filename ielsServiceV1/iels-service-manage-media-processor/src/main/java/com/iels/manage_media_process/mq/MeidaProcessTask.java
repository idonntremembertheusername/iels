package com.iels.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.iels.framework.domain.media.MediaFile;
import com.iels.framework.domain.media.MediaFileProcess_m3u8;
import com.iels.framework.utils.HlsVideoUtil;
import com.iels.framework.utils.Mp4VideoUtil;
import com.iels.manage_media_process.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description: 监听队列 - 队列名称[queue_media_video_processor] - 消息消费方
 * @Author: snypxk
 * @Date: 2019/12/12 11
 * @Other: 发送消息方[MediaUploadService.mergechunks()]: 视频文件由web前端分块上传并在服务端合并成功后发送
 * 接收视频处理消息 - 把视频转成mp4 - 把 mp4分块 - 把最终结果保存到mongodb
 **/
/*
 * 1.确定消息格式:
 *      MQ消息统一采用json格式, 视频处理生产方会向MQ发送如下消息,
 *      视频处理消费方接收此消息后进行视频处理: ｛“mediaId”:XXX｝
 * 2.处理流程:
 *       1) 接收视频处理消息
 *       2) 判断媒体文件是否需要处理（本视频处理程序目前只接收avi视频的处理)
 *          当前只有avi文件需要处理，其它文件需要更新处理状态为“无需处理”。
 *       3) 处理前初始化处理状态为“未处理”
 *       4) 处理失败需要在数据库记录处理日志，及处理状态为“处理失败”
 *       5) 处理成功记录处理状态为“处理成功”
 * 3.在MediaFile类中添加mediaFileProcess_m3u8属性记录ts文件列表
 **/
@Slf4j
@Component
public class MeidaProcessTask {

    // ffmpeg.exe的绝对路径:
    // D:/IDEA_workspace/xuecheng/tools/ffmpeg-20180227-fa0c9d6-win64-static/bin/ffmpeg.exe
    @Value("${iels-service-manage-media.ffmpeg-path}")
    String ffmpeg_path;

    // 待处理[编码,转换]的源视频的目录所在位置:
    // D:/IDEA_workspace/iels/video/
    @Value("${iels-service-manage-media.video-location}")
    String serverPath;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    /*
     * @description: 接收视频处理消息,进行视频处理
     * @author: snypxk
     * @param msg - 视频处理消息
     * @return: void
     *      containerFactory - 设置并发数量
     **/
    @RabbitListener(queues = "${iels-service-manage-media.mq.queue-media-video-processor}", containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg) {
        //1) 解析接收的视频处理消息,得到mediaId
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String) map.get("mediaId");

        //2) 根据mediaId从数据库[media_file]查询文件信息,判断媒体文件是否需要处理
        Optional<MediaFile> optional = mediaFileRepository.findById(mediaId);
        if (!optional.isPresent()) {
            //如果Mongodb中对应的数据库集合中查找不到数据,则返回不再往下处理
            return;
        }
        MediaFile mediaFile = optional.get();

        //3) 如果需要处理,则使用工具类将avi生成mp4 [[使用工具类: Mp4VideoUtil]
        //得到文件的类型[后缀格式名]
        String fileType = mediaFile.getFileType();
        if (fileType == null || !fileType.equalsIgnoreCase("avi")) {
            //如果后缀格式不是avi,则不需要处理:把视频处理状态更新为"无需处理"
            //"无需处理"的数据字典[sys_dictionary]编号sd_status是:303004
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            //返回,不再往下处理
            return;
        } else {
            //需要处理: 把视频处理状态更新为"处理中"
            //"处理中"的数据字典[sys_dictionary]编号sd_status是:303001
            mediaFile.setProcessStatus("303001");
            mediaFileRepository.save(mediaFile);
        }
        //得到源文件[avi]的绝对路径
        String video_path = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
        //设定: 转成后的MP4格式的文件名[带后缀.mp4]
        String mp4_name = mediaFile.getFileId() + ".mp4";
        //设定: 转成后的MP4格式的文件的保存路径位置[不带文件名和后缀] [和源文件avi在同级目录]
        String mp4folder_path = serverPath + mediaFile.getFilePath();
        //创建工具类对象
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_path, mp4_name, mp4folder_path);
        //使用工具类对象执行转化: avi --> mp4
        String result = mp4VideoUtil.generateMp4();
        if (result == null || !result.equals("success")) {
            //如果返回的结果为空 或处理结果不是 "success", 说明"处理失败"
            //"处理失败"的数据字典[sys_dictionary]编号sd_status是:303003
            mediaFile.setProcessStatus("303003");
            //定义 MediaFileProcess_m3u8 记录失败原因
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //4) 把生成的mp4文件再转化生成 m3u8 和 ts文件 [使用工具类: HlsVideoUtil]
        //获取: 转化后得到的MP4文件的绝对路径[带后缀]
        String mp4_video_path = serverPath + mediaFile.getFilePath() + mp4_name;
        //设定: m3u8文件的名称[不包含路径,带后缀]
        String m3u8_name = mediaFile.getFileId() + ".m3u8";
        //设定: m3u8文件所在的目录[注意: mediaFile.getFilePath()最后添加了"/"]
        String m3u8Folder_path = serverPath + mediaFile.getFilePath() + "hls/";
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path, mp4_video_path, m3u8_name, m3u8Folder_path);
        //生成m3u8和ts文件的结果
        String generateM3u8Result = hlsVideoUtil.generateM3u8();
        if (generateM3u8Result == null || !generateM3u8Result.equals("success")) {
            //如果返回的结果为空 或处理结果不是 "success", 说明"处理失败"
            //"处理失败"的数据字典[sys_dictionary]编号sd_status是:303003
            mediaFile.setProcessStatus("303003");
            //定义 MediaFileProcess_m3u8 记录失败原因
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(result);
            mediaFileRepository.save(mediaFile);
            return;
        }
        //处理成功: 生成m3u8 和 ts
        //获取生成的ts文件列表:
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        //"处理成功"的数据字典[sys_dictionary]编号sd_status是:303002
        mediaFile.setProcessStatus("303002");
        //保存 fileUrl : 此URL就是视频播放的相对路径, serverPath + fileUrl 便是视频[.m3u8]的完成URL
        String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8_name;
        mediaFile.setFileUrl(fileUrl);
        mediaFileRepository.save(mediaFile);
    }
}