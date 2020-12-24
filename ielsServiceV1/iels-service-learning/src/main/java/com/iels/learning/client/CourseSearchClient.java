package com.iels.learning.client;

import com.iels.framework.client.IelsServiceList;
import com.iels.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description: TODO
 * @Author: snypxk
 * @Date: 2019/12/13 02
 * @Other:
 **/
@FeignClient(value = IelsServiceList.IELS_SERVICE_SEARCH)
public interface CourseSearchClient {

    /*
     * @description: 根据课程计划查询对应的媒资视频资源
     * @author: snypxk
     * @param teachplanId
     * @return: com.iels.framework.domain.course.TeachplanMediaPub
     **/
    @GetMapping("/search/course/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
