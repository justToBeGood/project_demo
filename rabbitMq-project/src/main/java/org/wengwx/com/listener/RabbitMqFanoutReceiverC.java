package org.wengwx.com.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName RabbitMqFanoutReceiverC
 * @Author wengweixin
 * @Date 2020/11/18 14:24
 **/
@Component
@RabbitListener(queues = "fanout.C")
public class RabbitMqFanoutReceiverC {
    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("FanoutReceiverC消费者收到消息  : " +testMessage.toString());
    }

}
