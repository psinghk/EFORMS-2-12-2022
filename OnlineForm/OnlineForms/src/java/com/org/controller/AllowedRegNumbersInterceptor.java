package com.org.controller;

import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsStatics;
import static org.apache.struts2.StrutsStatics.HTTP_REQUEST;

public class AllowedRegNumbersInterceptor extends AbstractInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("inside allowed reg Number interceptor");
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        System.out.println("inside allowed reg Number interceptor ip::::" + ip);
        Map<String, Object> session = invocation.getInvocationContext().getSession();
        UserData udata = (UserData) session.get("uservalues");
        if (udata == null) {
            return "logout";
        }
        if ((boolean) session.get("refresh")) {
            Map<String, String> details = new HashMap();
            details.put("error", "It seems your IP has been changed. Kindly refresh and try again!!!");
            String json = new Gson().toJson(details);
            ServletActionContext.getResponse().setContentType("application/json");
            ServletActionContext.getResponse().getWriter().write(json);
            return "unauthorized";
        }

        List<String> roles = udata.getRoles();
        Set<String> aliases = udata.getAliases();
        String commaSeparatedAliases = udata.getAliasesInString();
        ArrayList<String> formsAllowed = (ArrayList<String>) udata.getFormsAllowed();
        final ActionContext context = invocation.getInvocationContext();
        Database db = new Database();
        String loggedinEmail = "";
        ArrayList<String> allowedRegNumbers = new ArrayList<>();
        if (session.get("login") != null && udata != null) {
            loggedinEmail = session.get("login").toString();
            HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
            String rno = "";
            if (request.getParameter("data") != null) {
                rno = request.getParameter("data");
            } else if (request.getParameter("regNo") != null) {
                rno = request.getParameter("regNo");
            } else if (request.getParameter("reg_no") != null) {
                rno = request.getParameter("reg_no");
            } else if (request.getParameter("ref_no") != null) {
                rno = request.getParameter("ref_no");
            }
            System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : REGISTRATION NUMBER : " + rno);
            if (!rno.isEmpty()) {
                    if (rno.contains("~")) {
                        String regno[] = rno.split("~");
                        rno = regno[0];
                    }
                    allowedRegNumbers.clear();
                    if (session.get("regNumberAllowed") == null) {
                        System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : inside first if regNumber is not in session : " + rno);
                        allowedRegNumbers = db.setAllowedRegistrationNumbers(commaSeparatedAliases, roles, formsAllowed, aliases);
                        session.put("regNumberAllowed", allowedRegNumbers);
                    } else {
                        System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : session.get(\"regNumberAllowed\") not null");
                        allowedRegNumbers = (ArrayList<String>) session.get("regNumberAllowed");
                    }
                    if (!allowedRegNumbers.contains(rno)) {
//                    if (allowedRegNumbers != null) {
//                        allowedRegNumbers.clear();
//                    }
                        System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : allowedRegNumber does not contain " + rno);
                        if(db.isRegistrationNumberAllowed(commaSeparatedAliases, roles, formsAllowed, aliases, rno)){
                            allowedRegNumbers.add(rno);
                            session.put("regNumberAllowed", allowedRegNumbers);
                        }
                        //allowedRegNumbers = db.setAllowedRegistrationNumbers(commaSeparatedAliases, roles, formsAllowed, aliases);
                    }

//                    if (!allowedRegNumbers.contains(rno)) {
//                        ArrayList<String> allowedRegNumbers1 = new ArrayList<>();
//                        allowedRegNumbers1 = db.setAllowedRegistrationNumbersFromStatusTable(commaSeparatedAliases, roles, formsAllowed, aliases);
//                        allowedRegNumbers.addAll(allowedRegNumbers1);
//                        session.put("regNumberAllowed", allowedRegNumbers);
//                    }

                    if (!allowedRegNumbers.contains(rno)) {
                        System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : Calling logout | allowedRegNumber does not contain " + rno);
                        Map<String, String> details = new HashMap();
                        details.put("error", rno + " does not belong to you!!! For any queries, raise a ticekt at https://servicedesk.nic.in");
                        String json = new Gson().toJson(details);
                        ServletActionContext.getResponse().setContentType("application/json");
                        ServletActionContext.getResponse().getWriter().write(json);
                        return "unauthorized";
                    }
                } else {
                    return invocation.invoke();
                }
            } else {
                System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : Calling logout as session does not exist");
                return "logout";
            }
            return invocation.invoke();
        }
    }
