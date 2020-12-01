package org.wengwx.com.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wengwx.com.entiry.User;
import org.wengwx.com.util.ZkUtils;

import javax.servlet.ServletOutputStream;
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
@Api(tags = "测试相关接口",description = "这是个拿来测试的控制器")
@RestController()
@RequestMapping("/test")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);
    @GetMapping("/test1")
    public void test1(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        User user = new User();
        user.setPhone("12312311231");
        user.setAge(1231);
        user.setName("啦啦啦");

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpServletResponse.getOutputStream());
            objectOutputStream.writeObject(user);
            byte[] strByte=new String("123").getBytes();
            objectOutputStream.write(strByte);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/test2")
    public void test2(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        try{
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write("测试".getBytes());
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/swaggerTest")
    @ApiOperation(value="swagger测试", notes="这是用来测试swagger的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "num", value = "数量", required = true, dataType = "Integer")
    })
    public String swaggerTest(String id,int num){
        return "hahahhahhaha";
    }


    @GetMapping("/logtest")
    public void testlog(){
        logger.debug("这是测试日志");
        logger.info("这是通知日志");
        logger.warn("这是警告日志");
        logger.error("这是错误日志");

    }

}
