package com.iels.framework.domain.cms.ext;

import com.iels.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 课程分类扩展类
 * @Author: snypxk
 * @Date: 2019/12/4 00
 * @Other:
 **/
@Data
@ToString
public class CategoryNode extends Category {

    List<CategoryNode> children;

}
