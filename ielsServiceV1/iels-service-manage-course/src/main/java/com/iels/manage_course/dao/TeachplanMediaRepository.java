package com.iels.manage_course.dao;

import com.iels.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/12 20
 * @Other:
 **/
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {

    //根据课程id查询媒资列表
    List<TeachplanMedia> findByCourseId(String courseId);


}
