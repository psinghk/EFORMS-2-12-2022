/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.ssologin;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author NIC
 */
public class LoginParichay extends ActionSupport implements SessionAware {

    Map<String, Object> session;

    @Override
    public String execute() throws IOException, ServletException {

        Map ss = (Map) ActionContext.getContext().get("session");

        System.out.println("Inside execute");
        session.put("mykey", "sso");
        HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);//
        res.sendRedirect(res.encodeRedirectURL("https://parichay.staging.nic.in:8080/Accounts/Services?service=efromsCO"));
//res.sendRedirect(res.encodeRedirectURL("https://parichay.pp.nic.in/Accounts/Services?service=eforms"));
//res.sendRedirect("https://parichay.nic.in/Accounts/Services?service=eforms"); 
        return SUCCESS;
        
        }

@Override
    public void setSession(Map<String, Object> sess) {
        this.session=(SessionMap<String, Object>) sess;
        
    }
    
}
