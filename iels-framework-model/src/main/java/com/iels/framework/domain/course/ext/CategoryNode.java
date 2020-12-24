package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 分类实体的扩展类
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
public class CategoryNode extends Category {
    List<CategoryNode> children;
}
