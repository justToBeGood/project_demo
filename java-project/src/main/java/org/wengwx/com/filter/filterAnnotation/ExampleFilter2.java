package org.wengwx.com.filter.filterAnnotation;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @ClassName ExampleFilter2
 * @Author wengweixin
 * @Date 2020/11/5 11:27
 **/
@WebFilter(filterName = "exampleFilter2",urlPatterns = "/*")
@Order(2)
public class ExampleFilter2 implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        if (!request.getRequestURL().toString().matches(".+.ico$")) {
            System.out.println("this is ExampleFilter2-before");
            filterChain.doFilter(servletRequest,servletResponse);
            System.out.println("this is ExampleFilter2-after");
        }

    }

    @Override
    public void destroy() {

    }
}
