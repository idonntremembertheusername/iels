package com.iels.api.course;

import com.iels.framework.domain.course.*;
import com.iels.framework.domain.course.ext.CategoryNode;
import com.iels.framework.domain.course.ext.CourseInfo;
import com.iels.framework.domain.course.ext.CourseView;
import com.iels.framework.domain.course.ext.TeachplanNode;
import com.iels.framework.domain.course.request.CourseListRequest;
import com.iels.framework.domain.course.request.CourseMarketEx;
import com.iels.framework.domain.course.response.CoursePublishResult;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: 课程管理接口
 * @Author: snypxk
 * @Date: 2019/12/4 08
 * @Other:
 **/
@Api(value = "课程管理接口", tags = "课程管理接口,提供课程的CRUD")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    /*
     * @description: 细粒度授权也叫数据范围授权，即不同的用户所拥有的操作权限相同，但是能够操作的数据范围是不一样的
     * @author: snypxk
     * @param page
     * @param size
     * @param courseListRequest
     * @return: QueryResponseResult<com.xuecheng.framework.domain.course.ext.CourseInfo>
     *      细粒度授权过程: 获取当前登录的用户Id -> 得到用户所属教育机构的Id(company_id)-> 查询该教学机构下的课程信息
     **/
    @ApiOperation("查询我的课程列表")
    QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程图片")
    ResponseResult addCoursePic(String courseId, String pic);

    @ApiOperation("获取课程基础信息")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
        //String id: 课程id
    CourseView courseview(String id);

    @ApiOperation("预览课程")
    CoursePublishResult preview(String id);

    @ApiOperation("发布课程")
    CoursePublishResult publish(String id);

    @ApiOperation("保存媒资信息")
    ResponseResult savemedia(TeachplanMedia teachplanMedia);

    @ApiOperation("获取课程分类列表")
    CategoryNode getAllCategory();

    @ApiOperation("根据课程id查询课程基本信息")
    CourseBase getCourseBaseInfo(String courseId);

    @ApiOperation("根据课程id更新课程基本信息")
    ResponseResult updateCourseBaseInfo(CourseBase courseBase);

    @ApiOperation("添加新课程-为课程添加基本信息")
    public ResponseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("根据课程id查询课程市场信息")
    CourseMarket getCourseMarketInfo(String courseId);

    @ApiOperation("更新课程市场信息")
    ResponseResult updateCourseMarketInfo(CourseMarketEx courseMarketex);

    @ApiOperation("获取上级课程章节目录信息")
    public TeachplanNode getParentTeachplanList(@PathVariable("courseId") String courseId);

    @ApiOperation("后端不作任何处理,直接保存前台传过来的Teachplan")
    public ResponseResult addTeachPlan(Teachplan teachplan);

    @ApiOperation("根据课程计划id以及课程等级来删除课程计划")
    public ResponseResult addTeachPlan(@PathVariable("teachplanId") String teachplanId,
                                       @PathVariable("grade") String grade);

    @ApiOperation("显示首页课程信息")
    public QueryResponseResult findAllCourse(CourseListRequest courseListRequest);
}
