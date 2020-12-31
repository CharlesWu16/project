package com.atguigu.springboot;

import com.atguigu.springboot.bean.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02RabbitmqApplicationTests {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AmqpAdmin amqpAdmin;

    @Test
    public void createExchange(){
        //创建交换器Exchange
       // amqpAdmin.declareExchange(new DirectExchange("rabbit.exchange"));
        System.out.println("创建Exchange成功");

        //创建队列Queue
        amqpAdmin.declareQueue(new Queue("rabbitmq.queue",true));
        //创建绑定规则
        //(String destination, DestinationType destinationType, String exchange, String routingKey,
        //			Map<String, Object> arguments)
        amqpAdmin.declareBinding(new Binding("rabbitmq.queue",Binding.DestinationType.QUEUE,"rabbit.exchange","rabbit.mq",null));

    }

    /**
     * 测试direct点对点单播模式
     */
    @Test
    public void contextLoads() {
        //Message需要自己构造一个;定义消息体内容和消息头
        //rabbitTemplate.send(exchange,routingKey,message);

        //object默认当成消息体,只需要传入要发送的对象，自动序列化发送给rabbitmq
        //rabbitTemplate.convertAndSend(exchange,routingKey,message);
        Map<String,Object> map=new HashMap<>();
        map.put("msg","这是第一个消息");
        map.put("data", Arrays.asList("helloworld",123,true));
        //对象被默认序列化(也就是变成二进制数据)以后发送出去
        //rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",map);
        rabbitTemplate.convertAndSend("exchange.direct","atguigu.news",new Book("西游记","吴承恩"));
    }

    //接收数据，通过将MessageConverter交给spring管理来实现将数据自动的转为json发送出去
    @Test
    public void receive(){
        Object result = rabbitTemplate.receiveAndConvert("atguigu.news");
        System.out.println(result.getClass());
        System.out.println(result);
    }

    /**
     * 测试fanout广播模式
     */
    @Test
    public void sendMessage(){
        rabbitTemplate.convertAndSend("exchange.fanout","",new Book("西游记","罗承恩"));
    }
}
