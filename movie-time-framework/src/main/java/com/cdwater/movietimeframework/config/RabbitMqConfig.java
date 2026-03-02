package com.cdwater.movietimeframework.config;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimecommon.constants.TextConstant;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 */
@Configuration
public class RabbitMqConfig {

    /**
     * JSON消息转换器
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
        jjmc.setCreateMessageIds(true);
        return jjmc;
    }

    /**
     * 配置RabbitTemplate使用JSON消息转换器
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 创建业务交换机
     */
    @Bean
    public DirectExchange businessExchange() {
        return ExchangeBuilder.directExchange(RabbitMqConstant.BUSINESS_EXCHANGE)
                .durable(true)//持久化
                .build();
    }

    /**
     * 创建死信交换机
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(RabbitMqConstant.DLX_EXCHANGE)
                .durable(true)
                .build();
    }

    //===================用户模块====================
    @Bean
    public Queue userCodeQueue() {
        return QueueBuilder.durable(RabbitMqConstant.USER_CODE_QUEUE).build();
    }

    @Bean
    public Binding userCodeBinding() {
        return BindingBuilder
                .bind(userCodeQueue())
                .to(businessExchange())
                .with(RabbitMqConstant.USER_CODE_ROUTING_KEY);
    }

    @Bean
    public Queue userFollowQueue() {
        return QueueBuilder.durable(RabbitMqConstant.USER_FOLLOW_QUEUE).build();
    }

    @Bean
    public Binding userFollowBinding() {
        return BindingBuilder
                .bind(userFollowQueue())
                .to(businessExchange())
                .with(RabbitMqConstant.USER_FOLLOW_ROUTING_KEY);
    }

    //===================电影模块====================
    @Bean
    public Queue movieReviewLikeQueue() {
        return QueueBuilder.durable(RabbitMqConstant.MOVIE_REVIEWLIKE_QUEUE).build();
    }

    @Bean
    public Binding movieReviewLikeBinding() {
        return BindingBuilder
                .bind(movieReviewLikeQueue())
                .to(businessExchange())
                .with(RabbitMqConstant.MOVIE_REVIEWLIKE_ROUTING_KEY);
    }

    //===================订单模块====================
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder.durable(RabbitMqConstant.ORDER_TTL_QUEUE)
                .deadLetterExchange(RabbitMqConstant.DLX_EXCHANGE)//配置死信交换机
                .deadLetterRoutingKey(RabbitMqConstant.ORDER_RELEASE_ROUTING_KEY)//配置死信路由键
                .ttl(TextConstant.ORDER_TTL * 60 * 1000)//配置过期时间
                .build();
    }

    @Bean
    public Binding orderTtlBinding() {
        return BindingBuilder
                .bind(orderTtlQueue())
                .to(businessExchange())
                .with(RabbitMqConstant.ORDER_TTL_ROUTING_KEY);
    }

    @Bean
    public Queue orderReleaseDlq() {
        return QueueBuilder.durable(RabbitMqConstant.ORDER_RELEASE_DLQ)
                .build();
    }

    @Bean
    public Binding orderReleaseDlqBinding() {
        return BindingBuilder
                .bind(orderReleaseDlq())
                .to(deadLetterExchange())
                .with(RabbitMqConstant.ORDER_RELEASE_ROUTING_KEY);
    }
}
