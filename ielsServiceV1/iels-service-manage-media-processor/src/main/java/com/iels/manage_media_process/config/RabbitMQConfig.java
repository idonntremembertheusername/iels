package com.iels.manage_media_process.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: RabbitMQ消息接收方
 * @Author: snypxk
 * @Date: 2019/12/12 15
 **/
@Configuration
public class RabbitMQConfig {

    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";

    //视频处理队列[队列名称]
    @Value("${iels-service-manage-media.mq.queue-media-video-processor}")
    public String queue_media_video_processtask;

    //视频处理路由[路由key]
    @Value("${iels-service-manage-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;

    /*
     * @description: 设置并发
     * @author: snypxk
     * @param configurer
     * @param connectionFactory
     * @return: org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
     **/
    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    /*
     * @description: 交换机配置 - 路由模式的交换机directExchange
     * @author: snypxk
     * @param
     * @return: org.springframework.amqp.core.Exchange
     **/
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
    }

    /*
     * @description: 声明队列
     * @author: snypxk
     * @param
     * @return: org.springframework.amqp.core.Queue
     **/
    @Bean("queue_media_video_processtask")
    public Queue QUEUE_PROCESSTASK() {
        Queue queue = new Queue(queue_media_video_processtask, true, false, true);
        return queue;
    }

    /*
     * 绑定队列到交换机
     * @param queue  -  the queue
     * @param exchange - the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_media_processtask(@Qualifier("queue_media_video_processtask") Queue queue,
                                                   @Qualifier(EX_MEDIA_PROCESSTASK) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey_media_video).noargs();
    }
}