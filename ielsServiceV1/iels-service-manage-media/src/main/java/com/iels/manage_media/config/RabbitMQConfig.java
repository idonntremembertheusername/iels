package com.iels.manage_media.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: RabbitMQ消息发送方
 * @Author: snypxk
 * @Date: 2019/12/12 15
 **/
@Configuration
public class RabbitMQConfig {

    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";

    //视频处理路由[路由key]
    @Value("${iels-service-manage-media.mq.routingkey-media-video}")
    public String routingkey_media_video;

    //消费者并发数量
    public static final int DEFAULT_CONCURRENT = 10;

    /*
     * 交换机配置 - 路由模式的交换机directExchange
     * @return the exchange
     */
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
    }
}
