package com.iels.api.learning;

import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.learning.response.GetMediaResult;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 02
 * @Other:
 **/
@Api(value = "录播课程学习管理", tags = {"录播课程学习管理"})
public interface CourseLearningControllerApi {

    @ApiOperation("获取课程学习地址URL")
    public GetMediaResult getmedia(String courseId, String teachplanId);

    @ApiOperation("根据用户id获取课程信息")
    public QueryResponseResult findCourseById(String userId);

    @ApiOperation("添加用户课程")
    public ResponseResult addCourse(String courseId,String userId);
}
