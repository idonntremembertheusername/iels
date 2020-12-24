package com.iels.manage_course.dao;

import com.iels.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/12 23
 * @Other:
 **/
public interface TeachplanMediaPubRepository extends JpaRepository<TeachplanMediaPub, String> {

    //根据课程id删除课程媒资发布信息
    long deleteByCourseId(String courseId);
}
