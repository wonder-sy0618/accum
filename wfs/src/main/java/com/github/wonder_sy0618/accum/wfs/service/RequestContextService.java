package com.github.wonder_sy0618.accum.wfs.service;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shiying on 2018/2/4.
 */
@Service
public class RequestContextService {

    private static final ThreadLocal<RequestContext> threadLocal = new ThreadLocal<>();

    public void setRequestContext(RequestContext context) {
        threadLocal.set(context);
    }

    public RequestContext getRequestContext() {
        return threadLocal.get();
    }

    /**
     * 请求上下文
     */
    public static class RequestContext implements Serializable {

        /**
         * 请求资源
         */
        private String resources;

        /**
         * 请求ID
         */
        private String requestId;

        /**
         * 请求时间
         */
        private Date requestTime;

        public String getRequestId() {
            return requestId;
        }
        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public Date getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(Date requestTime) {
            this.requestTime = requestTime;
        }

        public String getResources() {
            return resources;
        }

        public void setResources(String resources) {
            this.resources = resources;
        }
    }


}
