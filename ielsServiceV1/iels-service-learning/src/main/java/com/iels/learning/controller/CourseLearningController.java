package com.iels.learning.controller;

import com.iels.api.learning.CourseLearningControllerApi;
import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.learning.response.GetMediaResult;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.ResponseResult;
import com.iels.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 02
 * @Other:
 **/
@RestController
@RequestMapping("/learning/course")
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    private LearningService learningService;

    /*
     * @description: 获取课程学习地址
     * @author: snypxk
     * @param courseId - 课程ID
     * @param teachplanId -课程计划ID
     * @return: com.iels.framework.domain.learning.response.GetMediaResult
     **/
    @Override
    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    public GetMediaResult getmedia(@PathVariable("courseId") String courseId, @PathVariable("teachplanId") String teachplanId) {
        return learningService.getMedia(courseId, teachplanId);
    }

    @Override
    @GetMapping("/getcourse/{userId}")
    public QueryResponseResult findCourseById(@PathVariable("userId")String userId) {
        return learningService.findCourseById(userId);
    }

    @Override
    @PostMapping("/addCourse")
    public ResponseResult addCourse(@RequestParam("courseId") String courseId,@RequestParam("userId") String userId) {
        return learningService.addCourse(courseId,userId);
    }
}
