package org.wengwx.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @ClassName DubboProjectMain
 * @Author wengweixin
 * @Date 2020/11/23 23:33
 **/
@SpringBootApplication
@ImportResource("classpath:context-root.xml")
public class DubboProjectMain {
    public static void main(String[] args) {
        SpringApplication.run(DubboProjectMain.class,args);
    }
}
