package org.wengwx.com.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName RabbitMqFanoutReceiverB
 * @Author wengweixin
 * @Date 2020/11/18 14:24
 **/
@Component
@RabbitListener(queues = "fanout.B")
public class RabbitMqFanoutReceiverB {
    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("FanoutReceiverB消费者收到消息  : " +testMessage.toString());
    }

}
