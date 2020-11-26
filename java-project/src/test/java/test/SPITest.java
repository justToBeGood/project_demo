package test;

import org.junit.Test;
import org.wengwx.com.service.ServiceFirst;
import org.wengwx.com.service.ServiceSecond;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @ClassName SPITest
 * @Author wengweixin
 * @Date 2020/11/26 10:55
 **/
public class SPITest {
    @Test
    public void test1(){
        ServiceLoader<ServiceSecond> serviceLoader1=ServiceLoader.load(ServiceSecond.class);
        Iterator<ServiceSecond> serviceSeconds=serviceLoader1.iterator();
        while (serviceSeconds.hasNext()){
            ServiceSecond serviceSecond=serviceSeconds.next();
            serviceSecond.testSecond();
        }

        ServiceLoader<ServiceFirst> serviceLoader2=ServiceLoader.load(ServiceFirst.class);
        Iterator<ServiceFirst> serviceFirsts=serviceLoader2.iterator();
        while (serviceFirsts.hasNext()){
            ServiceFirst serviceFirst=serviceFirsts.next();
            serviceFirst.testFirst();
        }

    }
}
