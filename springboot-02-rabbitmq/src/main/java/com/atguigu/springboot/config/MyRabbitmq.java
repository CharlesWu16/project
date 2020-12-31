package com.atguigu.springboot.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将MessageConverter和它的实现类Jackson2JsonMessageConverter交给spring管理，
 * 这样rabbitmq的web页面传送的消息才是json数据，而不是二进制数据
 */
@Configuration
public class MyRabbitmq {
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
