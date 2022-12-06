package com.org.controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.StrutsStatics;

public class CachingHeadersInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        final ActionContext context = invocation.getInvocationContext();
        final HttpServletResponse response = (HttpServletResponse) context.get(StrutsStatics.HTTP_RESPONSE);
        if (response!= null) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");

            response.setHeader("Expires", "0");   
            //response.setHeader("X-XSS-Protection: 1", "mode=block");
            response.setHeader("Content-Security-Policy", "'style-src self' 'unsafe-inline'" ); 
            //response.setHeader("X-Content-Type-Options","nosnif"); 
            
            response.setHeader("X-Frame-Options", "DENY");
            response.addHeader("X-Content-Type-Options", "nosniff");
            response.addHeader("X-XSS-Protection", "1; mode=block");
            response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
            response.setHeader("Strict-Transport-Security","max-age=31536000; includeSubDomains; preload");
            response.setHeader("Set-Cookie", "key=value: HttpOnly; SameSite=strict");
          }
            //response.setHeader("Content-Security-Policy", "style-src http://localhost:8084/OnlineForms_audit/ 'unsafe-inline'" ); 

        return invocation.invoke();
    }
}
