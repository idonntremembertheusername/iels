package com.iels.api.filesystem;

import com.iels.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 14
 * @Other:
 **/
@Api(value = "文件管理接口", tags = "文件管理接口,提供页面的图片文件CRUD")
public interface FileSystemControllerApi {
    //上传文件
    @ApiOperation("上传文件接口")
    /*
     * @description: TODO
     * @author: snypxk
     * @date: 2019/12/3
     * @param multipartFile - 文件
     * @param filetag - 文件标签
     * @param businesskey - 业务key
     * @param metadata - 元信息,json格式
     * @return: com.xuecheng.framework.domain.filesystem.response.UploadFileResult
     * @note:
     **/
    public UploadFileResult upload(MultipartFile multipartFile,
                                   String filetag,
                                   String businesskey,
                                   String metadata);
}
