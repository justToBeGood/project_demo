package org.wengwx.com.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * @ClassName ConfigFilter
 * @Author wengweixin
 * @Date 2020/11/5 11:18
 **/
@Configuration
public class ConfigFilter {

    @Resource
    Filter exampleFilter1;

    @Bean
    public FilterRegistrationBean exampleFilter1Registe(){
        FilterRegistrationBean registrationBean=new FilterRegistrationBean();
        registrationBean.setFilter(exampleFilter1);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("exampleFilter1");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
