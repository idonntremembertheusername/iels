package com.iels.learning.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //添加选课任务交换机
    public static final String EX_LEARNING_ADDCHOOSECOURSE = "ex_learning_addchoosecourse";

    //添加选课消息队列[由服务[xc_learning]监听该队列]
    public static final String IELS_LEARNING_ADDCHOOSECOURSE = "iels_learning_addchoosecourse";

    //完成添加选课消息队列[由定时任务监听该队列]
    public static final String IELS_LEARNING_FINISHADDCHOOSECOURSE = "iels_learning_finishaddchoosecourse";

    //定时任务 向MQ发送"添加选课消息"的路由key
    public static final String IELS_LEARNING_ADDCHOOSECOURSE_KEY = "addchoosecourse";

    //服务[xc_learning]完成添加选课后 向MQ发送"完成添加选课消息"的路由key
    public static final String IELS_LEARNING_FINISHADDCHOOSECOURSE_KEY = "finishaddchoosecourse";

    /*
     * @description: 交换机配置
     * @author: snypxk
     * @param
     * @return: org.springframework.amqp.core.Exchange
     **/
    @Bean(EX_LEARNING_ADDCHOOSECOURSE)
    public Exchange EX_DECLARE() {
        return ExchangeBuilder.directExchange(EX_LEARNING_ADDCHOOSECOURSE).durable(true).build();
    }

    //声明队列: 添加选课消息队列
    @Bean(IELS_LEARNING_ADDCHOOSECOURSE)
    public Queue QUEUE_DECLARE2() {
        return new Queue(IELS_LEARNING_ADDCHOOSECOURSE, true, false, true);
    }

    //声明队列: 完成添加选课消息队列
    @Bean(IELS_LEARNING_FINISHADDCHOOSECOURSE)
    public Queue QUEUE_DECLARE() {
        return new Queue(IELS_LEARNING_FINISHADDCHOOSECOURSE, true, false, true);
    }

    /*
     * @description: 绑定 "添加选课消息队列" 到 交换机
     * @author: snypxk
     * @param queue     - IELS_LEARNING_ADDCHOOSECOURSE
     * @param exchange  - EX_LEARNING_ADDCHOOSECOURSE
     * @return: org.springframework.amqp.core.Binding
     **/
    @Bean
    public Binding binding_queue_addchoose_processtask2(@Qualifier("iels_learning_addchoosecourse") Queue queue,
                                                        @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(IELS_LEARNING_ADDCHOOSECOURSE_KEY).noargs();
    }


    /*
     * @description: 绑定 "完成添加选课消息队列" 到 交换机
     * @author: snypxk
     * @param queue     - IELS_LEARNING_FINISHADDCHOOSECOURSE
     * @param exchange  - EX_LEARNING_ADDCHOOSECOURSE
     * @return: org.springframework.amqp.core.Binding
     **/
    @Bean
    public Binding binding_queue_finishaddchoose_processtask(@Qualifier("iels_learning_finishaddchoosecourse") Queue queue,
                                                             @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(IELS_LEARNING_FINISHADDCHOOSECOURSE_KEY).noargs();
    }
}
