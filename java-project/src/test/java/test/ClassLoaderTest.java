package test;

import org.junit.Test;
import org.wengwx.com.exception.MyselfException;
import org.wengwx.com.util.ClassLoaderUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * @ClassName ClassLoaderTest
 * @Author wengweixin
 * @Date 2020/11/26 9:33
 **/
public class ClassLoaderTest {
    @Test
    public void test1(){
        //查看Bootstrap ClassLoader加载包
        System.out.println("==============查看Bootstrap ClassLoader加载包==================");
        System.out.println(System.getProperty("sun.boot.class.path"));
        //查看Extension ClassLoader加载包
        System.out.println("==============查看Extension ClassLoader加载包==================");
        System.out.println(System.getProperty("java.ext.dirs"));
        //查看App ClassLoader加载包
        System.out.println("==============查看App ClassLoader加载包==================");
        System.out.println(System.getProperty("java.class.path"));
    }

    @Test
    public void test2(){
        String jarFilePath="D:\\repository\\com\\gnete\\common\\gnete-common-context\\1.0.3-RELEASE\\gnete-common-context-1.0.3-RELEASE.jar";
        try {
            ClassLoaderUtils.loadJar(jarFilePath);
            Class c=Class.forName("com.gnete.common.constant.GlobalConstants");
            System.out.println(c.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    @org.junit.Test
    public void test3() {
        try {
            String value1= ClassLoaderUtils.getPropertyByAbsolutelyPath("/conf/myself.properties","mytest");
            System.out.println(value1);
            String value2=ClassLoaderUtils.getPropertyByRelativePath("conf/myself.properties","mytest");
            System.out.println(value2);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
