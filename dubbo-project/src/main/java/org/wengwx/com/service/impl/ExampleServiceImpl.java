package org.wengwx.com.service.impl;


import org.springframework.stereotype.Service;
import org.wengwx.com.service.ExampleService;

/**
 * @ClassName ExampleServiceImpl
 * @Author wengweixin
 * @Date 2020/11/9 16:04
 **/
@Service("exampleService")
public class ExampleServiceImpl implements ExampleService {
    @Override
    public void helloWorld(String name) {
        System.out.println("I am "+name+",hello world");
    }
}
