package org.wengwx.com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wengwx.com.entiry.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TestController
 * @Author wengweixin
 * @Date 2020/11/24 11:18
 **/
@RestController()
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public void test(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        User user = new User();
        user.setPhone("12312311231");
        user.setAge(1231);
        user.setName("啦啦啦");

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpServletResponse.getOutputStream());
            objectOutputStream.writeObject(user);
            byte[] strByte=new String("123").getBytes();
            objectOutputStream.write(strByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
