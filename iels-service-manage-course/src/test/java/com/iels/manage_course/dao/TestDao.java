package com.iels.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.course.ext.CategoryNode;
import com.iels.framework.domain.course.ext.TeachplanNode;
import com.iels.manage_course.dao.CategoryMapper;
import com.iels.manage_course.dao.CourseBaseRepository;
import com.iels.manage_course.dao.CourseMapper;
import com.iels.manage_course.dao.TeachplanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void testCourseBaseRepository() {
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }
    }

    @Test
    public void testCourseMapper() {
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);
    }

    @Test
    public void findCourseBaseById() {
        TeachplanNode list = teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(list);
    }

    @Test
    public void testPageHelper() {
        PageHelper.startPage(1, 10);
        Page<CourseBase> courseList = courseMapper.findCourseList();
        //得到页数数据列表
        List<CourseBase> result = courseList.getResult();
        //得到查询的总记录数
        courseList.getTotal();
        System.out.println(result);

    }

    @Test
    public void testGetAllCategory() {
        CategoryNode allCategory = categoryMapper.getAllCategory();
        List<CategoryNode> children = allCategory.getChildren();
        for (CategoryNode second :
                children) {
            System.out.println("============================================================");
            List<CategoryNode> children1 = second.getChildren();
            for (CategoryNode third :
                    children1) {
                System.out.println("id: " + third.getId() + "   name: " + third.getName());
            }
            System.out.println("---------------------------------------");
        }
        System.out.println();
    }
}
