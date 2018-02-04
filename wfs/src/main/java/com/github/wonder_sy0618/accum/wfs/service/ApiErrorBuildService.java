package com.github.wonder_sy0618.accum.wfs.service;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by shiying on 2018/2/4.
 */
@Service
public class ApiErrorBuildService {

    @Resource
    private RequestContextService requestContextService;

    public ApiErrorMsgDto build(String errcode, String errmsg) {
        ApiErrorMsgDto error = new ApiErrorMsgDto();
        error.setResources(requestContextService.getRequestContext().getResources());
        error.setErrcode(errcode);
        error.setErrmsg(errmsg);
        error.setRequestid(requestContextService.getRequestContext().getRequestId());
        return error;
    }

    public Document buildAsXml(String errcode, String errmsg) {
        //
        ApiErrorMsgDto dto = build(errcode, errmsg);
        //
        Document document = DocumentHelper.createDocument();
        Element error = document.addElement("Error");
        error.addElement("Code").setText(dto.getErrcode());
        error.addElement("Message").setText(dto.getErrmsg());
        error.addElement("Resources").setText(dto.getResources());
        error.addElement("RequestId").setText(dto.getRequestid());
        return document;
    }

    public static final class ApiErrorMsgDto implements Serializable {
        private String errcode;
        private String errmsg;
        private String resources;
        private String requestid;

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public String getResources() {
            return resources;
        }

        public void setResources(String resources) {
            this.resources = resources;
        }

        public String getRequestid() {
            return requestid;
        }

        public void setRequestid(String requestid) {
            this.requestid = requestid;
        }

    }


}
