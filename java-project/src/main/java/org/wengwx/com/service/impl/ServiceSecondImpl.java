package org.wengwx.com.service.impl;

import org.wengwx.com.service.ServiceSecond;

/**
 * @ClassName ServiceSecondImpl
 * @Author wengweixin
 * @Date 2020/11/25 11:03
 **/
public class ServiceSecondImpl implements ServiceSecond {
    @Override
    public void testSecond() {
        System.out.println("ServiceSecondImpl-testSecond");
    }

    @Override
    public void testFirst() {
        System.out.println("ServiceSecondImpl-testFirst");
    }
}
