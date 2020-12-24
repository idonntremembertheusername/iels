package com.iels.manage_media.controller;

import com.iels.api.media.MediaUploadControllerApi;
import com.iels.framework.domain.media.response.CheckChunkResult;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.utils.IelsOauth2Util;
import com.iels.framework.web.BaseController;
import com.iels.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/11 23
 * @Other:
 **/
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController extends BaseController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    //文件上传前 - 注册
    @Override
    @PostMapping("/register")
    public ResponseResult register(@RequestParam("fileMd5") String fileMd5,
                                   @RequestParam("fileName") String fileName,
                                   @RequestParam("fileSize") Long fileSize,
                                   @RequestParam("mimetype") String mimetype,
                                   @RequestParam("fileExt") String fileExt) {
        return mediaUploadService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
    }

    @Override
    @PostMapping("/checkchunk")
    public CheckChunkResult checkchunk(@RequestParam("fileMd5") String fileMd5,
                                       @RequestParam("chunk") Integer chunk,
                                       @RequestParam("chunkSize") Integer chunkSize) {
        return mediaUploadService.checkchunk(fileMd5, chunk, chunkSize);
    }

    @Override
    @PostMapping("/uploadchunk")
    public ResponseResult uploadchunk(@RequestParam("file") MultipartFile file,
                                      @RequestParam("chunk") Integer chunk,
                                      @RequestParam("fileMd5") String fileMd5) {
        return mediaUploadService.uploadchunk(file, fileMd5, chunk);
    }

    @Override
    @PostMapping("/mergechunks")
    public ResponseResult mergechunks(@RequestParam("fileMd5") String fileMd5,
                                      @RequestParam("fileName") String fileName,
                                      @RequestParam("fileSize") Long fileSize,
                                      @RequestParam("mimetype") String mimetype,
                                      @RequestParam("fileExt") String fileExt) {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String companyId = userJwt.getCompanyId();
        String userId = userJwt.getId();
        return mediaUploadService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt, companyId, userId);
    }
}
