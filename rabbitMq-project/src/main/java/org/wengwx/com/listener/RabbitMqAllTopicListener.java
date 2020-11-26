package org.wengwx.com.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName RabbitMqAllTopicListener
 * @Author wengweixin
 * @Date 2020/11/18 11:54
 **/
@Component
@RabbitListener(queues = "topic.woman")
public class RabbitMqAllTopicListener {
    @RabbitHandler
    public void process(Map testMessage){
        System.out.println("TopicTotalReceiver消费者收到的消息："+testMessage.toString());
    }
}
