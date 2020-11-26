package test;



import com.google.gson.Gson;
import org.wengwx.com.exception.MyselfException;
import org.wengwx.com.service.ServiceFirst;
import org.wengwx.com.util.ClassLoaderUtils;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Test
 * @Author wengweixin
 * @Date 2020/11/24 11:31
 **/
public class Test {
    @org.junit.Test
    public void test1() {
        Map<String,Object> testData=new HashMap<>();
        List<Map> whiteList=new ArrayList<>();
        Map<String,String> exceptionWhiteItem1=new HashMap<>();
        exceptionWhiteItem1.put("type","java.lang.ClassCastException,java.sql.SQLException");
        exceptionWhiteItem1.put("msg","");
        whiteList.add(exceptionWhiteItem1);
        Map<String,String> exceptionWhiteItem2=new HashMap<>();
        exceptionWhiteItem2.put("type","java.lang.NullPointerException");
        exceptionWhiteItem2.put("msg","白名单");
        whiteList.add(exceptionWhiteItem2);
        testData.put("*",whiteList);
        System.out.println(new Gson().toJson(testData));
    }



}
