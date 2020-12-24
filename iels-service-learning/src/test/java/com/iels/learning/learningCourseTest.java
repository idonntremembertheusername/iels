package com.iels.learning;

import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.learning.IelsLearningCourse;
import com.iels.learning.dao.CourseBaseRepository;
import com.iels.learning.dao.IelsLearningCourseRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @program: ielsServiceV1
 * @description:
 * @author: thesky
 * @create: 2020-05-19 16:57
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class learningCourseTest {
    @Autowired
    IelsLearningCourseRepository ielsLearningCourseRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;


    
    
    @Test
    public void testLearningCourse(){
        String userId="54";
        List<IelsLearningCourse> list = ielsLearningCourseRepository.findAllByUserId(userId);
        for (int i=0;i<list.size();i++
             ) {
            IelsLearningCourse ielsLearningCourse = list.get(i);
            String courseId = ielsLearningCourse.getCourseId();

            List<CourseBase> all = courseBaseRepository.findAllById(courseId);




        }






    }
    
}
