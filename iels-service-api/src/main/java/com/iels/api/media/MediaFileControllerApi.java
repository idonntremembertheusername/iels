package com.iels.api.media;

import com.iels.framework.domain.media.request.QueryMediaFileRequest;
import com.iels.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/12 15
 * @Other:
 **/
@Api(value = "媒体文件管理", tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {

    @ApiOperation("查询媒资文件列表")
    public QueryResponseResult findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

    @ApiOperation("根据教师用户ID查询媒资文件列表")
    public QueryResponseResult findListByUserId(int page, int size, QueryMediaFileRequest queryMediaFileRequest);

    @ApiOperation("根据教师用户所在的公司的id查询媒资文件列表,如果教师用户没有对应的公司ID,则根据教师用户ID查询")
    public QueryResponseResult findListByCompanyIdOrByUserId(int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
