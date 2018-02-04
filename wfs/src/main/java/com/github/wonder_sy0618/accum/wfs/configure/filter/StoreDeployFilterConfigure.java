package com.github.wonder_sy0618.accum.wfs.configure.filter;

import com.github.wonder_sy0618.accum.wfs.service.ApiErrorBuildService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 前端资源发布入口filter
 *
 * Created by shiying on 2018/2/4.
 */
@Configuration
public class StoreDeployFilterConfigure implements Filter {

    @Value("${wfs.store.path}")
    private String wfsStorePath;

    @Value("${wfs.store.deploy:store/}")
    private String wfsStoreDeploy;

    @Resource
    private ApiErrorBuildService apiErrorBuildService;

    @Bean
    public FilterRegistrationBean storeDeployFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(this);
        registration.addUrlPatterns("/*");
        registration.setOrder(11);
        return registration;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest)) {
            // 非HTTP请求，忽略
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //
        if (!wfsStoreDeploy.startsWith("/")){
            // 容错处理，发布路径以/开头
            wfsStoreDeploy = "/" + wfsStoreDeploy;
        }
        if (!request.getServletPath().startsWith(wfsStoreDeploy)) {
            // 不以发布路径起始，忽略
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String relativelyUrl = request.getServletPath().substring(wfsStoreDeploy.length(), request.getServletPath().length());
        File resFile = new File(wfsStorePath, File.separator.equals("/") ? relativelyUrl : relativelyUrl.replaceAll("/", "\\\\"));
        if (!resFile.exists() || !resFile.isFile()) {
            String xml = apiErrorBuildService.buildAsXml("NoSuchResources", "The resources id does not exist.").asXML();
            PrintWriter out = response.getWriter();
            out.append(xml);
            out.flush();
            out.close();
            return;
        }
        InputStream inputStream = new FileInputStream(resFile);
        try {
            IOUtils.copy(inputStream, response.getOutputStream());
            return;
        } catch (Exception e) {
            String xml = apiErrorBuildService.buildAsXml("ResourcesTreatError", e.getMessage()).asXML();
            PrintWriter out = response.getWriter();
            out.append(xml);
            out.flush();
            out.close();
            return;
        } finally {
            inputStream.close();
        }
    }

    @Override
    public void destroy() {

    }
}
