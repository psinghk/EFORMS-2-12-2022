package com.org.controller;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.org.bean.UserData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.struts2.ServletActionContext;

public class CheckIpInterceptor extends AbstractInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        System.out.println("inside check IP interceptor");
        Map<String, Object> session = invocation.getInvocationContext().getSession();

        String ip = ServletActionContext.getRequest().getRemoteAddr();
        boolean flag = false;
        if (session.get("refresh") == null) {
            session.put("refresh", false);
        }
        UserData udata = (UserData) session.get("uservalues");
        if (udata == null) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " Userdata is null.... no session for user ");
            return invocation.invoke();
        }
        if(session.get("path") != null && session.get("path").toString().contains("ssologin") && session.get("login_ip") == null) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " path contains ssologin ");
            return invocation.invoke();
        }
        
        Set<String> aliases = udata.getAliases();
        if (session.get("login") != null && udata != null) {
//            List<String> currentRoles = udata.getRoles();
//            if (!currentRoles.contains("email_co") && !currentRoles.contains("admin") && !currentRoles.contains("sup")) {
//                System.out.println("USER DOES NOT HAVE ADMIN SUP OR COORD ROLES " + currentRoles.toString());
//                return invocation.invoke();
//            }

            if (!ip.equalsIgnoreCase(session.get("login_ip").toString())) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + " IP mismatches");

                if (session.get("changed_ip") == null || !ip.equalsIgnoreCase(session.get("changed_ip").toString())) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                            + " previous IP not matching wirth the IP through which user is login in ");
                    System.out.println("Login ip ==========    " + session.get("login_ip"));
                    System.out.println("changed_ip ==========    " + session.get("changed_ip"));
                    LoginAction loginAction = new LoginAction();
                    Map<String, Object> map = loginAction.fetchRoles(aliases);
                    List<String> rolesListService = new ArrayList<>();
                    rolesListService = (List<String>) map.get("rolesServiceInList");
                    udata.setRolesService(rolesListService);
                    udata.setRole((String) map.get("rolesInString"));
                    udata.setRoles((List<String>) map.get("rolesInList"));
                    session.remove("uservalues");
                    session.put("uservalues", udata);
                    session.put("changed_ip", ip);
                    session.put("refresh", true);
                    session.remove("regNumberAllowed");
                    flag = true;
                    invocation.getInvocationContext().setSession(session);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                            + "ROLES ::::::::::: " + udata.getRoles().toString());
//                    if (udata.getRoles().contains("admin")) {
//                        return "showlinkdataforadmin";
//                    } else if (udata.getRoles().contains("co")) {
//                        return "showlinkdataforco";
//                    } else if (udata.getRoles().contains("sup")) {
//                        return "showlinkdataforsup";
//                    } else if (udata.getRoles().contains("ca")) {
//                        return "showlinkdataforca";
//                    } else if (udata.getRoles().contains("user")) {
//                        return "showuserdata";
//                    }
                }
            } else if ((ip.equalsIgnoreCase(session.get("login_ip").toString())) && (session.get("changed_ip") != null && !ip.equalsIgnoreCase(session.get("changed_ip").toString()))) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + " IP same as login IP ");
                udata.setRole(udata.getPreviousRoleAtLogin());
                udata.setRoles(udata.getPreviousRolesAtLogin());
                udata.setRolesService(udata.getPreviousRolesServiceAtLogin());
                System.out.println("Roles PreviousRoles" + udata.getRoles().toString());
                session.remove("uservalues");
                session.put("uservalues", udata);
                session.put("changed_ip", ip);
                session.put("refresh", true);
                session.remove("regNumberAllowed");
                flag = true;
                invocation.getInvocationContext().setSession(session);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + "ROLES :: " + udata.getRoles().toString());

//                if (udata.getRoles().contains("admin")) {
//                    return "showlinkdataforadmin";
//                } else if (udata.getRoles().contains("co")) {
//                    return "showlinkdataforco";
//                } else if (udata.getRoles().contains("sup")) {
//                    return "showlinkdataforsup";
//                } else if (udata.getRoles().contains("ca")) {
//                    return "showlinkdataforca";
//                } else if (udata.getRoles().contains("user")) {
//                    return "showuserdata";
//                }
            } else {
                session.put("inelse", true);
                invocation.getInvocationContext().setSession(session);
            }
        } else {
            System.out.println("INSIDE ALLOWED REG NUMBER INTERCEPTOR : Calling logout as session does not exist");
            if (session.containsKey("loginType")) {
                if (session.get("loginType").toString().equalsIgnoreCase("sso")) {
                    return "logout_parichay";
                }
            }
            return "logout";
        }

        if (!flag) {
            if (((boolean) session.get("refresh"))) {
                session.put("refresh", false);
                invocation.getInvocationContext().setSession(session);
            }
        }
        return invocation.invoke();
//        udata.setRole("user");
//        udata.setRoles(Arrays.asList("user"));
//        invocation.getInvocationContext().setSession(session);
//        return "";
    }

}
