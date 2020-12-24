package com.iels.framework.domain.filesystem;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * @Description: FileSystem实体类 - Mongodb中对应的集合是 filesystem
 * @Author: snypxk
 * @Date: 2019/12/13 10
 * @Other:
 **/
@Data
@ToString
@Document(collection = "filesystem")
public class FileSystem {

    @Id
    private String fileId;

    //文件请求路径
    private String filePath;
    //文件大小
    private long fileSize;
    //文件名称
    private String fileName;
    //文件类型
    private String fileType;
    //图片宽度
    private int fileWidth;
    //图片高度
    private int fileHeight;
    //用户id，用于授权
    private String userId;
    //业务key
    private String businesskey;
    //业务标签
    private String filetag;
    //文件元信息
    private Map metadata;
}
