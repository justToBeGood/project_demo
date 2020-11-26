package org.wengwx.com.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName ExampleFilter
 * @Author wengweixin
 * @Date 2020/11/5 11:13
 **/
@Component
public class ExampleFilter1 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //进行springboot整合过滤器的过程中可能会遇到过滤器执行两次的问题,针对这个问题可能出现的一种原因就是因为在请求执行完之后浏览器会再发一次请求.ico的请求
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if (!request.getRequestURL().toString().matches(".+.ico$")) {
            System.out.println("this is ExampleFilter1-before");
            filterChain.doFilter(servletRequest,servletResponse);
            System.out.println("this is ExampleFilter1-after");
        }

    }

    @Override
    public void destroy() {

    }
}
