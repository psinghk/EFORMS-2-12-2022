package com.org.controller;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.SignUpService;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Person;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.StrutsStatics.HTTP_REQUEST;

public class RetiredLoginInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;
    public String logintype;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("inside retired login interceptor");
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        final ActionContext context = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        String path = ((HttpServletRequest) request).getRequestURI();
        System.out.println("PATH ::::" + path);

        if (session.get("email") != null) {
            return invocation.invoke();
        } else {
            return "logout";
        }
    }

    String HTTP_URL_Response(String http_url) {
        String line = "";

        try {

            //System.out.println("SOUT " + http_url);
            HttpURLConnection huc = (HttpURLConnection) new URL(http_url).openConnection();
            //HttpURLConnection.setFollowRedirects(false);
            huc.setConnectTimeout(2 * 1000);
            huc.connect();

            try (InputStream response2 = huc.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response2));
                for (String tmp; (tmp = reader.readLine()) != null;) {
                    line = tmp;

                }

                reader.close();
            } catch (Exception e) {
                System.out.println("Error In HTTTP URL");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Error In HTTTP URL");
            e.printStackTrace();
        }

        return line;
    }

    /**
     * @return the logintype
     */
    public String getLogintype() {
        return logintype;
    }

    /**
     * @param logintype the logintype to set
     */
    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }
}
