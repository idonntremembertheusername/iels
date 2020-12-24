package com.iels.framework.domain.media;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@Data
@ToString
@Document(collection = "media_file")
public class MediaFile {

    @Id
    //文件id
    private String fileId;

    //文件名称
    private String fileName;
    //文件原始名称
    private String fileOriginalName;
    //文件路径
    private String filePath;
    //文件url
    private String fileUrl;
    //文件类型
    private String fileType;
    //mimetype
    private String mimeType;
    //文件大小
    private Long fileSize;
    //文件状态: 未上传、上传完成、上传失败
    private String fileStatus;
    //上传时间
    private Date uploadTime;
    //处理状态
    private String processStatus;
    //hls处理
    private MediaFileProcess_m3u8 mediaFileProcess_m3u8;
    //tag标签用于查询
    private String tag;
    //由哪位用户上传的
    private String userId;
    //该用户是哪个公司或机构的
    private String companyId;
}
