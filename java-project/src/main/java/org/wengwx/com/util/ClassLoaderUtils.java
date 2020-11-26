package org.wengwx.com.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Properties;

/**
 * @ClassName ClassLoadUtils
 * @Author wengweixin
 * @Date 2020/11/26 9:47
 **/
public class ClassLoaderUtils {

    public static void loadJar(String jarFilePath) throws FileNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalformedURLException {
        File file=new File(jarFilePath);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        URL url=file.toURI().toURL();
        //得到系统类加载器
        URLClassLoader urlClassLoader=(URLClassLoader)ClassLoader.getSystemClassLoader();
        //addURL的权限为protected，所以通过反射的方式来进行调用
        Method addURL=URLClassLoader.class.getDeclaredMethod("addURL",new Class[]{URL.class});
        addURL.setAccessible(true);
        addURL.invoke(urlClassLoader,new Object[]{url});

    }

    public static String getPropertyByAbsolutelyPath(String propertiesFilePath,String key) throws IOException {
        Class clazz=new Object(){
            public Class getClassName(){
                return this.getClass().getEnclosingClass();
            }
        }.getClassName();
        InputStream inputStream=clazz.getResourceAsStream(propertiesFilePath);
        return getPropertiesValue(inputStream,key);
    }

    public static String getPropertyByRelativePath(String propertiesFilePath,String key) throws IOException {
        Class clazz=new Object(){
            public Class getClassName(){
                return this.getClass().getEnclosingClass();
            }
        }.getClassName();
        InputStream inputStream=clazz.getClassLoader().getResourceAsStream(propertiesFilePath);
        return getPropertiesValue(inputStream,key);
    }

    private static String getPropertiesValue(InputStream inputStream,String key) throws IOException {
        Properties properties=new Properties();
        properties.load(inputStream);
        return properties.getProperty(key);
    }
}
