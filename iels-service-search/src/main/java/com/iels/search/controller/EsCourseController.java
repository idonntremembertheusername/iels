package com.iels.search.controller;

import com.iels.api.search.EsCourseControllerApi;
import com.iels.framework.domain.course.CoursePub;
import com.iels.framework.domain.course.TeachplanMediaPub;
import com.iels.framework.domain.search.CourseSearchParam;
import com.iels.framework.model.response.QueryResponseResult;
import com.iels.framework.model.response.QueryResult;
import com.iels.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/10 22
 * @Other:
 **/
@RestController
@RequestMapping("/search/course")
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,
                                               @PathVariable("size") int size,
                                               CourseSearchParam courseSearchParam) throws IOException {
        return esCourseService.list(page, size, courseSearchParam);
    }

    /*
     * @description: 使用ES的高级客户端 向ES请求查询索引信息 - 查询课程所有信息返回给前端
     * @author: snypxk
     * @param id - 课程id
     * @return: java.util.Map<java.lang.String,com.xuecheng.framework.domain.course.CoursePub>
     **/
    @Override
    @GetMapping("/getall/{id}")
    public Map<String, CoursePub> getall(@PathVariable("id") String id) {
        return esCourseService.getall(id);
    }

    /*
     * @description: 根据课程计划ID查询媒资信息 - 向ES索引库: xc_course_meida 查询
     * @author: snypxk
     * @param teachplanId - 课程计划ID
     * @return: com.xuecheng.framework.domain.course.TeachplanMediaPub
     **/
    @Override
    @GetMapping(value = "/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        //将课程计划id放在数组中，为调用service作准备
        String[] teachplanIds = new String[]{teachplanId};
        //通过service查询ES获取课程媒资信息
        QueryResponseResult<TeachplanMediaPub> mediaPubQueryResponseResult = esCourseService.getmedia(teachplanIds);
        QueryResult<TeachplanMediaPub> queryResult = mediaPubQueryResponseResult.getQueryResult();
        if (queryResult != null
                && queryResult.getList() != null
                && queryResult.getList().size() > 0) {
            //返回课程计划对应课程媒资
            return queryResult.getList().get(0);
        }
        //若找不到则返回空对象
        return new TeachplanMediaPub();
    }

    @Override
    @GetMapping("/wechatList")
    public QueryResponseResult<CoursePub> wechatList(CourseSearchParam courseSearchParam) throws IOException {
        return esCourseService.wechatList(courseSearchParam);
    }

}
