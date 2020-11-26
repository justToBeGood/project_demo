package org.wengwx.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @ClassName JavaProjectMain
 * @Author wengweixin
 * @Date 2020/11/23 23:16
 **/
@ServletComponentScan(basePackages = {"org.example.filter.filterAnnotation"})
@SpringBootApplication
public class JavaProjectMain {
    public static void main(String[] args) {
        SpringApplication.run(JavaProjectMain.class,args);
    }
}
