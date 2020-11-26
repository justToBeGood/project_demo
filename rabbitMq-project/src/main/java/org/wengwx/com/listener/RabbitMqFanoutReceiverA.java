package org.wengwx.com.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName RabbitMqFanoutReceiverA
 * @Author wengweixin
 * @Date 2020/11/18 14:23
 **/
@Component
@RabbitListener(queues = "fanout.A")
public class RabbitMqFanoutReceiverA {
    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    }

}
