package com.iels.manage_course.dao;

import com.iels.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanRepository extends JpaRepository<Teachplan, String> {

    // 根据 课程ID 和 Parentid查询得到 课程计划
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);

    Long deleteAllByParentid(String parentid);
}
