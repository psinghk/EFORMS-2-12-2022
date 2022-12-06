package com.org.controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.StrutsStatics;

public class UrlInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final ActionContext context = invocation.getInvocationContext();
        final HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);

        if (response != null) {
            //  System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Caching header called");
            response.setHeader("Cache-control", "no-cache");
            response.setHeader("Cache-control", "no-store");
            response.setHeader("Cache-control", "pre-check=0");
            response.setHeader("Cache-control", "private");
            //response.setHeader("Cache-control","post-check=0");
            response.setHeader("Cache-control", "must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "-1");
            response.setHeader("X-XSS-Protection: 1", "mode=block");
            response.setHeader("Content-Security-Policy", "style-src http://localhost:8084/OnlineForms_audit/ 'unsafe-inline'");

        }
        return invocation.invoke();
    }
}
