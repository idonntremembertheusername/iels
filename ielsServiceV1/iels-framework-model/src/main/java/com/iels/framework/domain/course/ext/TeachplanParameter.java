package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.Teachplan;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 课程计划参数扩展类
 * @Author: snypxk
 * @Date: 2019/12/13 12
 * @Other:
 **/
@Data
@ToString
public class TeachplanParameter extends Teachplan {
    //二级分类ids
    List<String> bIds;
    //三级分类ids
    List<String> cIds;
}
