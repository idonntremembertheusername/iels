package com.iels.manage_course.dao;

import com.iels.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePicRepository extends JpaRepository<CoursePic, String> {

    //当返回值大于0,说明删除成功: 其表示删除成功的记录数
    Long deleteByCourseid(String courseid);
}
