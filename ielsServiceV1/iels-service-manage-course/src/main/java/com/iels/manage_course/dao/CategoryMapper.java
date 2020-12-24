package com.iels.manage_course.dao;

import com.iels.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 查询课程分类信息
 * @author: snypxk
 **/
@Mapper
public interface CategoryMapper {
    CategoryNode getAllCategory();
}
