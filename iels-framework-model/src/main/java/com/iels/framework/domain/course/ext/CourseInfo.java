package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.CourseBase;
import lombok.Data;
import lombok.ToString;

/**
 * @Description: 课程基本信息扩展类
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
public class CourseInfo extends CourseBase {
    //课程图片
    private String pic;
}
