package com.iels.manage_course.dao;

import com.iels.framework.domain.course.CoursePub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: 课程发布信息DAO
 * @Author: snypxk
 **/
public interface CoursePubRepository extends JpaRepository<CoursePub, String> {
}
