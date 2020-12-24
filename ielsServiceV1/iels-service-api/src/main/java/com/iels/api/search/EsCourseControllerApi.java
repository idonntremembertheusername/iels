package com.iels.api.search;

import com.iels.framework.domain.course.CoursePub;
import com.iels.framework.domain.course.TeachplanMediaPub;
import com.iels.framework.domain.search.CourseSearchParam;
import com.iels.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/10 22
 * @Other:
 **/
@Api(value = "搜索课程信息", tags = {"课程信息搜索接口"})
public interface EsCourseControllerApi {

    @ApiOperation("课程搜索")
    public QueryResponseResult<CoursePub> list(
            int page, int size, CourseSearchParam courseSearchParam) throws IOException;

    /*
     * 目前课程计划信息在课程管理数据库和ES索引库中存在, 考虑性能要求, 课程发布后对课程的查询统一律从ES索引库中查询。
     * 前端通过请求搜索服务获取课程信息, 需要单独在搜索服务中定义课程信息查询接口。
     * 本接口接收课程id, 查询课程所有信息返回给前端
     **/
    @ApiOperation("根据id查询课程信息")
    public Map<String, CoursePub> getall(String id);

    @ApiOperation("根据课程计划ID查询媒资信息")
    public TeachplanMediaPub getmedia(String teachplanId);

    @ApiOperation("微信端课程搜索")
    public QueryResponseResult<CoursePub> wechatList(CourseSearchParam courseSearchParam) throws IOException;
}
