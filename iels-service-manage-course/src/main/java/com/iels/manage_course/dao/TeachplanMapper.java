package com.iels.manage_course.dao;

import com.iels.framework.domain.course.Teachplan;
import com.iels.framework.domain.course.ext.TeachplanNode;
import com.iels.framework.model.response.ResponseResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachplanMapper {

    //课程计划查询
    TeachplanNode selectList(String courseId);

    //添加课程计划
    ResponseResult addTeachplan(Teachplan teachplan);

    //查询上级课程计划(一级或二级)
    TeachplanNode selectParentList(String courseId);
}
