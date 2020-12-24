package com.iels.filesystem.controller;

import com.iels.api.filesystem.FileSystemControllerApi;
import com.iels.filesystem.service.FileSystemService;
import com.iels.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 14
 * @Other:
 **/
@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    private FileSystemService fileSystemService;

    /*
     * @description: 图片文件上传
     * @author: snypxk
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return: com.iels.framework.domain.filesystem.response.UploadFileResult
     **/
    @Override
    @PostMapping("/upload")
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        return fileSystemService.upload(multipartFile, filetag, businesskey, metadata);
    }
}
