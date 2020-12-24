package com.iels.learning.dao;

import com.iels.framework.domain.learning.IelsLearningCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description: 学生选课Dao
 * @Author: snypxk
 * @Date: 2019/12/18 12
 * @Other: 学习服务接收MQ发送添加选课消息，执行添加 选 课操作。
 * 添加选课成功向学生选课表插入记录、向历史任务表插入记录、并向MQ发送“完成选课”消息。
 **/
public interface IelsLearningCourseRepository extends JpaRepository<IelsLearningCourse, String> {

    //根据用户和课程查询选课记录，用于判断是否添加选课
    IelsLearningCourse findByUserIdAndCourseId(String userId, String courseId);

    IelsLearningCourse findByUserId(String userId);

    List<IelsLearningCourse> findAllByUserId(String userId);




}
