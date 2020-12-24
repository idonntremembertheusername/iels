package com.iels.learning.dao;

import com.github.pagehelper.Page;
import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.course.ext.CourseInfo;
import com.iels.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 课程基本信息查询 - MYBATIS
 * @author: snypxk
 **/
@Mapper
public interface CourseMapper {

    CourseBase findCourseBaseById(String id);


}
