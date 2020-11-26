package org.wengwx.com.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName RabbitMqTopicListener
 * @Author wengweixin
 * @Date 2020/11/18 11:48
 **/
@Component
@RabbitListener(queues = "topic.man")
public class RabbitMqManTopicListener {
    @RabbitHandler
    public void process(Map testMessage){
        System.out.println("TopicManReceiver消费者收到的消息:"+testMessage.toString());
    }
}
