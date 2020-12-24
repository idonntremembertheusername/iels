package com.iels.framework.domain.course.ext;

import com.iels.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 分类参数扩展灰
 * @Author: snypxk
 * @Date: 2019/12/13 11
 * @Other:
 **/
@Data
@ToString
public class CategoryParameter extends Category {
    //二级分类ids
    List<String> bIds;
    //三级分类ids
    List<String> cIds;
}
