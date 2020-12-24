package com.iels.learning.dao;

import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.course.ext.CourseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @description: 课程基本信息查询
 * @author: snypxk
 **/
public interface CourseBaseRepository extends JpaRepository<CourseBase,String> {

    List<CourseBase> findAllById(String id);
}
