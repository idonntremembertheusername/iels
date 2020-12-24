package com.iels.manage_course.dao;

import com.iels.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: 课程市场营销信息查询
 * @Author: snypxk
 **/
public interface CourseMarketRepository extends JpaRepository<CourseMarket, String> {
}
