package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 课程计划扩展类 - 课程结点
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
public class TeachplanNode extends Teachplan {

    List<TeachplanNode> children;

    //媒资文件的ID
    String mediaId;

    //媒资文件的原始名称
    String mediaFileoriginalname;
}
