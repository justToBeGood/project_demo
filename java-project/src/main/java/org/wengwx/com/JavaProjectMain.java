package org.wengwx.com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.wengwx.com.util.ZkUtils;

/**
 * @ClassName JavaProjectMain
 * @Author wengweixin
 * @Date 2020/11/23 23:16
 **/
@ServletComponentScan(basePackages = {"org.example.filter.filterAnnotation"})
@SpringBootApplication
public class JavaProjectMain {
    private static Logger logger = LoggerFactory.getLogger(JavaProjectMain.class);

    public static void main(String[] args) {
        SpringApplication.run(JavaProjectMain.class,args);
        logger.info("应用启动");
    }
}
