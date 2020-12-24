package com.iels.api.media;

import com.iels.framework.domain.media.response.CheckChunkResult;
import com.iels.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/11 23
 * @Other:
 **/
@Api(value = "媒资管理接口", tags = {"提供视频等文件的上传分块,合并处理等接口"})
public interface MediaUploadControllerApi {

    //文件上传前的准备工作: 校验文件是否已经存在
    @ApiOperation("文件上传前-注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMd5", value = "文件MD5值"),
            @ApiImplicitParam(name = "fileName", value = "文件名称[带后缀]"),
            @ApiImplicitParam(name = "fileSize", value = "文件大小"),
            @ApiImplicitParam(name = "mimetype", value = "文件MIME类型"),
            @ApiImplicitParam(name = "fileExt", value = "文件扩展名"),
    })
    ResponseResult register(String fileMd5,
                            String fileName,
                            Long fileSize,
                            String mimetype,
                            String fileExt);

    @ApiOperation("分块文件检查-校验分块文件是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileMd5", value = "分块文件MD5值"),
            @ApiImplicitParam(name = "chunk", value = "分块文件序号[下标]"),
            @ApiImplicitParam(name = "chunkSize", value = "分块文件大小")
    })
    CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);

    @ApiOperation("上传分块文件")
    ResponseResult uploadchunk(MultipartFile file, Integer chunk, String fileMd5);

    @ApiOperation("合并分块文件")
    ResponseResult mergechunks(String fileMd5,
                               String fileName,
                               Long fileSize,
                               String mimetype,
                               String fileExt);


}
