package com.iels.manage_course.dao;

import com.iels.framework.domain.course.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/25 13
 * @Other:
 **/
public interface CategoryRepository extends JpaRepository<Category, String> {
}
