package com.iels.manage_course.controller;

import com.iels.api.course.CourseControllerApi;
import com.iels.framework.domain.course.*;
import com.iels.framework.domain.course.ext.CategoryNode;
import com.iels.framework.domain.course.ext.CourseInfo;
import com.iels.framework.domain.course.ext.CourseView;
import com.iels.framework.domain.course.ext.TeachplanNode;
import com.iels.framework.domain.course.request.CourseListRequest;
import com.iels.framework.domain.course.request.CourseMarketEx;
import com.iels.framework.domain.course.response.CoursePublishResult;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.utils.IelsOauth2Util;
import com.iels.framework.web.BaseController;
import com.iels.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: CourseController
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/11/28 22
 * @Other:
 **/
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    /*
     * @description: 根据课程Id获取课程计划列表数据
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.ext.TeachplanNode
     **/
    @Override
    @PreAuthorize("hasAuthority('course_teachplan_list')") //当用户拥有course_teachplan_list权限时才可访问此方法
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.selectList(courseId);
    }

    /*
     * @description: 添加课程计划 - 要对前台传过来的数据进行处理
     * @author: snypxk
     * @param teachplan
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PreAuthorize("hasAuthority('course_teachplan_add')") //当用户拥有course_teachplan_add权限时才可访问此方法
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    /*
     * @description: 分页获取课程列表数据
     * @author: snypxk
     * @param page
     * @param size
     * @param courseListRequest
     * @return: com.iels.framework.model.response.QueryResponseResult<com.iels.framework.domain.course.ext.CourseInfo>
     **/
    @Override
    @PreAuthorize("hasAuthority('coursebase_list')")
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,
                                                          @PathVariable("size") int size,
                                                          CourseListRequest courseListRequest) {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String companyId = userJwt.getCompanyId();
        String userId = userJwt.getId();
        return courseService.findCourseList(companyId, userId, page, size, courseListRequest);
    }

    /*
     * @description: 添加课程图片
     * @author: snypxk
     * @param courseId
     * @param pic
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PreAuthorize("hasAuthority('coursepic_add')")
    @PostMapping("/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.addCoursePic(courseId, pic);
    }

    /*
     * @description: 获取课程图片
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.CoursePic
     **/
    @Override
    @PreAuthorize("hasAuthority('course_pic_list')") //当用户拥有course_pic_list权限时才可访问此方法
    @GetMapping("/coursepic/list/{courseId}")
    public CoursePic findCoursePic(@PathVariable("courseId") String courseId) {
        return courseService.findCoursepic(courseId);
    }

    /*
     * @description: 删除课程图片
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PreAuthorize("hasAuthority('course_pic_delete')")
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    /*
     * @description: TODO
     * @author: snypxk
     * @param id
     * @return: com.iels.framework.domain.course.ext.CourseView
     **/
    @Override
    @PreAuthorize("hasAuthority('courseview')")
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    /*
     * @description: 课程预览
     * @author: snypxk
     * @param id
     * @return: com.iels.framework.domain.course.response.CoursePublishResult
     **/
    @Override
    @PreAuthorize("hasAuthority('preview')")
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    /*
     * @description: 课程发布
     * @author: snypxk
     * @param id
     * @return: com.iels.framework.domain.course.response.CoursePublishResult
     **/
    @Override
    @PreAuthorize("hasAuthority('publish')")
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable String id) {
        return courseService.publish(id);
    }

    /*
     * @description: 保存媒资信息
     * @author: snypxk
     * @param teachplanMedia
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PreAuthorize("hasAuthority('save_media')")
    @PostMapping("/savemedia")
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
        return courseService.savemedia(teachplanMedia);
    }

    /*
     * @description: 获取所有课程分类信息
     * @author: snypxk
     * @param
     * @return: com.iels.framework.domain.course.ext.CategoryNode
     **/
    @Override
    @GetMapping("/category/list")
    public CategoryNode getAllCategory() {
        return courseService.getAllCategory();
    }

    /*
     * @description: 根据课程id查询课程基本信息
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.CourseBase
     **/
    @Override
    @GetMapping("/coursebase/{courseId}")
    public CourseBase getCourseBaseInfo(@PathVariable("courseId") String courseId) {
        return courseService.getCourseBaseInfo(courseId);
    }

    /*
     * @description: 根据课程id更新课程基础信息
     * @author: snypxk
     * @param courseBase
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PostMapping("/coursebase/update")
    public ResponseResult updateCourseBaseInfo(CourseBase courseBase) {
        return courseService.updateCourseBaseInfo(courseBase);
    }

    /*
     * @description: 添加新课程-为课程添加基本信息
     * @author: snypxk
     * @param courseBase
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PostMapping("/coursebase/add")
    public ResponseResult addCourseBase(CourseBase courseBase) {
        //Oauth2Util工具类中，从header中取出JWT令牌，并解析JWT令牌的内容
        //调用工具类取出用户信息
        IelsOauth2Util ielsOauth2Util = new IelsOauth2Util();
        IelsOauth2Util.UserJwt userJwt = ielsOauth2Util.getUserJwtFromHeader(request);
        if (userJwt == null) {
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String companyId = userJwt.getCompanyId();
        String userId = userJwt.getId();
        courseBase.setCompanyId(companyId);
        courseBase.setUserId(userId);
        // 初始添加课程,默认其课程状态是[制作中]
        courseBase.setStatus("202001"); //课程状态: 制作中[202001]  已发布[202002]  已下线[202003]
        return courseService.addCourseBase(courseBase);
    }

    /*
     * @description: 根据课程id查询课程市场信息
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.CourseMarket
     **/
    @Override
    @GetMapping("/market/{courseId}")
    public CourseMarket getCourseMarketInfo(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketInfo(courseId);
    }

    /*
     * @description: 更新课程市场营销信息
     * @author: snypxk
     * @param courseMarketex
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PostMapping("/market/update")
    public ResponseResult updateCourseMarketInfo(CourseMarketEx courseMarketex) {
        return courseService.updateCourseMarketInfo(courseMarketex);
    }

    /*
     * @description: 获取上级课程章节目录信息
     * @author: snypxk
     * @param courseId
     * @return: com.iels.framework.domain.course.ext.TeachplanNode
     **/
    @Override
    @GetMapping("/teachplan/parent/{courseId}")
    public TeachplanNode getParentTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.getParentTeachplanList(courseId);
    }

    /*
     * @description: 后端不作任何处理,直接保存或更新前台传过来的Teachplan
     * @author: snypxk
     * @param teachplan - 完整的Teachplan信息[接收后对其不作任何处理]
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @PostMapping("/teachplan/addteachplan")
    public ResponseResult addTeachPlan(Teachplan teachplan) {
        return courseService.addTeachPlan(teachplan);
    }

    /*
     * @description: 删除课程计划
     * @author: snypxk
     * @param teachplanId
     * @param grade
     * @return: com.iels.framework.model.response.ResponseResult
     **/
    @Override
    @DeleteMapping("/teachplan/delete/{teachplanId}/{grade}")
    public ResponseResult addTeachPlan(@PathVariable("teachplanId") String teachplanId,
                                       @PathVariable("grade") String grade) {
        return courseService.deleteTeachPlan(teachplanId, grade);
    }

    @GetMapping("/allcourse")
    public QueryResponseResult findAllCourse(CourseListRequest courseListRequest){
        return courseService.findAllCourse(courseListRequest);
    }
}
