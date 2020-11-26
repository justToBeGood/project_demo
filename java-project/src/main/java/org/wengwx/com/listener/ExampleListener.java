package org.wengwx.com.listener;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * @ClassName ExampleListener
 * @Author wengweixin
 * @Date 2020/11/5 14:57
 **/
public class ExampleListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        System.out.println("你好，虚拟世界！");
    }
}
