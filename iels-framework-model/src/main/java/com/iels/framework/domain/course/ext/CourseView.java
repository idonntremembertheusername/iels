package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.course.CourseMarket;
import com.iels.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description: 课程视频扩展类 - 四个表的集合
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {
    //基础信息
    private CourseBase courseBase;
    //课程营销
    private CoursePic coursePic;
    //课程图片
    private CourseMarket courseMarket;
    //教学计划
    private TeachplanNode teachplanNode;
}
