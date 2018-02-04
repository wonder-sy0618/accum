package com.github.wonder_sy0618.accum.wfs.configure.filter;

import com.github.wonder_sy0618.accum.wfs.service.RequestContextService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 为每个请求生成独立的上下文，便于日志与跟踪
 *
 * Created by shiying on 2018/2/4.
 */
@Configuration
public class RequestContextFilterConfigure implements Filter {

    @Resource
    private RequestContextService requestContextService;

    @Bean
    public FilterRegistrationBean requestContextFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(this);
        registration.addUrlPatterns("/*");
        registration.setOrder(10);
        return registration;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestContextService.RequestContext context = new RequestContextService.RequestContext();
        context.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
        context.setRequestTime(new Date());
        context.setResources(servletRequest instanceof HttpServletRequest ? ((HttpServletRequest) servletRequest).getServletPath() : "");
        requestContextService.setRequestContext(context);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
