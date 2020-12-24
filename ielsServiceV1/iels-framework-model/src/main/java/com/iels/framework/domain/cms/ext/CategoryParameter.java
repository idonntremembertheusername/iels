package com.iels.framework.domain.cms.ext;

import com.iels.framework.domain.course.Category;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Description: 分类参数实体
 * @Author: snypxk
 * @Date: 2019/12/4 00
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