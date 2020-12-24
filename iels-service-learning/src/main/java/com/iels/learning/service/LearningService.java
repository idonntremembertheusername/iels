package com.iels.learning.service;

import com.iels.framework.domain.course.CourseBase;
import com.iels.framework.domain.course.TeachplanMediaPub;
import com.iels.framework.domain.course.ext.CourseInfo;
import com.iels.framework.domain.learning.IelsLearningCourse;
import com.iels.framework.domain.learning.response.GetMediaResult;
import com.iels.framework.domain.learning.response.LearningCode;
import com.iels.framework.domain.task.IelsTask;
import com.iels.framework.domain.task.IelsTaskHis;
import com.iels.framework.exception.ExceptionCast;
import com.iels.framework.model.response.CommonCode;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.QueryResult;
import com.iels.framework.model.response.ResponseResult;
import com.iels.framework.utils.UUIDUtil;
import com.iels.learning.client.CourseSearchClient;
import com.iels.learning.dao.CourseBaseRepository;
import com.iels.learning.dao.CourseMapper;
import com.iels.learning.dao.IelsLearningCourseRepository;
import com.iels.learning.dao.IelsTaskHisRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 02
 * @Other:
 **/
@Service
public class LearningService {

    @Autowired
    private CourseSearchClient courseSearchClient;

    @Autowired
    private IelsTaskHisRepository ielsTaskHisRepository;

    @Autowired
    private IelsLearningCourseRepository ielsLearningCourseRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;

    public GetMediaResult getMedia(String courseId, String teachplanId) {
        //校验学生的学习权限...是否资费等
        //调用搜索服务查询
        TeachplanMediaPub teachplanMediaPub = courseSearchClient.getmedia(teachplanId);
        if (teachplanMediaPub == null || StringUtils.isEmpty(teachplanMediaPub.getMediaUrl())) {
            //获取视频播放地址出错
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        return new GetMediaResult(CommonCode.SUCCESS, teachplanMediaPub.getMediaUrl());
    }

    /*
     * @description: 完成选课
     * @author: snypxk
     * @param userId    - 用户id
     * @param courseId  - 课程id
     * @param valid     - 有效性
     * @param startTime - 课程开始时间
     * @param endTime   - 课程结束时间
     * @param ielsTask    - 消息体对象
     * @return: com.xuecheng.framework.model.response.ResponseResult
     *      向xc_learning_course添加记录，为保证不重复添加选课，先查询历史任务表，
     *      如果从历史任务表查询不到任务说明此任务还没有处理，此时则添加选课并添加历史任务
     **/
    @Transactional
    public ResponseResult addcourse(String userId, String courseId, String valid, Date startTime, Date endTime, IelsTask ielsTask) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        if (StringUtils.isEmpty(userId)) {
            ExceptionCast.cast(LearningCode.CHOOSE_COURSE_TASK_ISNULL);
        }
        if (ielsTask == null || StringUtils.isEmpty(ielsTask.getId())) {
            ExceptionCast.cast(LearningCode.CHOOSE_COURSE_TASK_ISNULL);
        }
        //查询历史任务: 如果已经存在,则不再添加,返回ResponseResult(CommonCode.SUCCESS)
        Optional<IelsTaskHis> optional = ielsTaskHisRepository.findById(ielsTask.getId());
        if (optional.isPresent()) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        IelsLearningCourse ielsLearningCourse =
                ielsLearningCourseRepository.findByUserIdAndCourseId(userId, courseId);
        if (ielsLearningCourse == null) {//没有选课记录则添加
            ielsLearningCourse = new IelsLearningCourse();
            ielsLearningCourse.setUserId(userId);
            ielsLearningCourse.setCourseId(courseId);
            ielsLearningCourse.setValid(valid);
            ielsLearningCourse.setStartTime(startTime);
            ielsLearningCourse.setEndTime(endTime);
            ielsLearningCourse.setStatus("501001");
            ielsLearningCourseRepository.save(ielsLearningCourse);
        } else {//有选课记录则更新日期
            ielsLearningCourse.setValid(valid);
            ielsLearningCourse.setStartTime(startTime);
            ielsLearningCourse.setEndTime(endTime);
            ielsLearningCourse.setStatus("501001");
            ielsLearningCourseRepository.save(ielsLearningCourse);
        }
        //向历史任务表播入记录
        Optional<IelsTaskHis> optional2 = ielsTaskHisRepository.findById(ielsTask.getId());
        if (!optional2.isPresent()) {
            //添加历史任务
            IelsTaskHis xcTaskHis = new IelsTaskHis();
            BeanUtils.copyProperties(ielsTask, xcTaskHis);
            ielsTaskHisRepository.save(xcTaskHis);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /*
     * @description: 根据用户id查询课程信息
     * @author: thesky
     * @param userId    - 用户id
     * @return: com.iels.framework.model.response.QueryResponseResult
     *
     **/
    public QueryResponseResult findCourseById(String userId) {
        List<IelsLearningCourse> list = ielsLearningCourseRepository.findAllByUserId(userId);
        QueryResult<CourseBase> courseInfoQueryResult = new QueryResult<CourseBase>();
        List<CourseBase> courseBaseList = new ArrayList<>();
//        courseInfoQueryResult.setList(courseBaseList);
        for (int i = 0; i < list.size(); i++) {
            IelsLearningCourse ielsLearningCourse = list.get(i);
            String courseId = ielsLearningCourse.getCourseId();
            Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(courseId);
            if(optionalCourseBase.isPresent()){
                courseBaseList.add(optionalCourseBase.get());
            }
        }
        courseInfoQueryResult.setList(courseBaseList);
        courseInfoQueryResult.setTotal(courseBaseList.size());
        return new QueryResponseResult(CommonCode.SUCCESS, courseInfoQueryResult);
    }

    @Transactional
    public ResponseResult addCourse(String courseId, String userId) {
        if (courseId != null || userId != null) {
            IelsLearningCourse ielsLearningCourse = new IelsLearningCourse();
            ielsLearningCourse.setId(UUIDUtil.get16UUID());
            ielsLearningCourse.setUserId(courseId);
            ielsLearningCourse.setCourseId(userId);
            ielsLearningCourseRepository.save(ielsLearningCourse);
            return new ResponseResult(CommonCode.SUCCESS);
        }

        return new ResponseResult(CommonCode.FAIL);
    }


}
