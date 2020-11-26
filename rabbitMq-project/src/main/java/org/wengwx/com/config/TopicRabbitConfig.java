package org.wengwx.com.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName TopicRabbitConfig
 * @Author wengweixin
 * @Date 2020/11/18 9:21
 **/
@Configuration
public class TopicRabbitConfig {

    //绑定键
    public final static String man = "topic.man";
    public final static String woman = "topic.woman";

    @Bean
    public Queue manQueue() {
        return new Queue(TopicRabbitConfig.man);
    }

    @Bean
    public Queue allQueue() {
        return new Queue(TopicRabbitConfig.woman);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    //将firstQueue和topicExchange绑定,而且绑定的键值为topic.man
//这样只要是消息携带的路由键是topic.man,才会分发到该队列
    @Bean
    Binding bindingExchangeMessage() {
        return BindingBuilder.bind(manQueue()).to(exchange()).with(man);
    }

    //将womanQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
// 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
    @Bean
    Binding bindingExchangeMessage2() {
        return BindingBuilder.bind(allQueue()).to(exchange()).with("topic.#");
    }


}
