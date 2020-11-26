package org.wengwx.com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wengwx.com.service.ExampleService;

import java.io.File;

/**
 * @ClassName ExampleController
 * @Author wengweixin
 * @Date 2020/11/5 11:04
 **/
@RequestMapping("/example")
@RestController
public class ExampleController {
    @Autowired
    ExampleService exampleService;

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public String test1(){

        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        File file=new File("conf/cert/XXx.p12");
        File file1=new File("/");
        File file2=new File("./conf");
        File file3=new File("../conf");
        if(!file.exists()){
            System.out.println("文件不存在");
        }

        String msg="this is a test";
        System.out.println(msg);
        return msg;
    }

    @RequestMapping(value = "/test1",method = RequestMethod.POST)
        public String test2(){
            return "hahah";
        }






}
