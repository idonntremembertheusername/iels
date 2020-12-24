package com.iels.learning.mq;

import com.alibaba.fastjson.JSON;
import com.iels.framework.domain.task.IelsTask;
import com.iels.framework.model.response.ResponseResult;
import com.iels.learning.config.RabbitMQConfig;
import com.iels.learning.service.LearningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Description: 监听 "添加选课消息"队列
 * @Author: snypxk
 * @Date: 2019/12/18 13
 * @Other:
 **/
@Slf4j
@Component
public class ChooseCourseTask {

    @Autowired
    private LearningService learningService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
     * @description: 接收定时任务发送过来的 "选课消息"并执行处理
     * @author: snypxk
     * @param xcTask
     * @return: void
     **/
    @RabbitListener(queues = RabbitMQConfig.IELS_LEARNING_ADDCHOOSECOURSE)
    public void receiveChoosecourseTask(IelsTask ielsTask) throws IOException {
        log.info("receive choose course task,taskId:{}", ielsTask.getId());
        //接收到的消息id
        String id = ielsTask.getId();
        //添加选课
        try {
            //xcTask的requestBody属性中包含了消息的内容[其是一个JSON字符串]
            String requestBody = ielsTask.getRequestBody();
            //将requestBody其转成一个map
            Map map = JSON.parseObject(requestBody, Map.class);
            String userId = (String) map.get("userId");
            String courseId = (String) map.get("courseId");
            String valid = (String) map.get("valid");
            Date startTime = null;
            Date endTime = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY‐MM‐dd HH:mm:ss");
            if (map.get("startTime") != null) {
                startTime = dateFormat.parse((String) map.get("startTime"));
            }
            if (map.get("endTime") != null) {
                endTime = dateFormat.parse((String) map.get("endTime"));
            }
            //添加选课
            ResponseResult addcourse = learningService.addcourse(userId, courseId, valid, startTime, endTime, ielsTask);
            //选课成功发送响应消息
            if (addcourse.isSuccess()) {
                //发送响应消息
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE,
                        RabbitMQConfig.IELS_LEARNING_FINISHADDCHOOSECOURSE_KEY, ielsTask);
                log.info("send finish choose course taskId:{}", id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("send finish choose course taskId:{}", id);
        }
    }
}
