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

public class LoginInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;
    public String logintype;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        System.out.println("inside login interceptor");
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        //String ip = "10.26.112.140";
        final ActionContext context = invocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) context.get(HTTP_REQUEST);
        String str = request.getParameter("string");
        System.out.println("str=" + str);
        Map<String, Object> session = invocation.getInvocationContext().getSession();

        //HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get;
        //ActionContext ctx = ActionContext.getContext();
        //HttpServletRequest request1 = (HttpServletRequest) ctx.get(ServletActionContext.HTTP_REQUEST);
        //HttpSession session1 = request1.getSession();
        String path = ((HttpServletRequest) request).getRequestURI();
        System.out.println("PATH ::::" + path);

        if (path.contains("ssologin")) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " path contains ssologin ");

            return invocation.invoke();
        } else if (session.get("path") != null && session.get("path").toString().contains("ssologin")) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " session path contains ssologin ");
            if (session.get("uservalues") != null) {

                session.put("status", "ok");
                Database db = new Database();

                UserData userdata = (UserData) session.get("uservalues");
                String userName = userdata.getEmail();

                String localTokenId = session.get("localTokenId").toString();
                String browserId = session.get("browserId").toString();
                String sessionId = session.get("sessionId").toString();

                //Map<String, Object> personDetails = db.fetchFromParichayDataStorage(userName);
//                if(!(localTokenId.equalsIgnoreCase((String)personDetails.get("localTokenId")) && browserId.equalsIgnoreCase((String)personDetails.get("browserId")) && sessionId.equalsIgnoreCase((String)personDetails.get("sessionId")))){
//                    return "failedredirect";
//                }
                //String url = "http://10.122.34.117:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=efromsCO&browserId=" + browserId + "&sessionId=" + sessionId;
                //String url = "https://parichay.pp.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=eforms&browserId=" + browserId + "&sessionId=" + sessionId;              
                String url = "https://parichay.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=eforms&browserId=" + browserId + "&sessionId=" + sessionId;
                System.out.println(url);
                String output = HTTP_URL_Response(url);

                Gson g = new Gson();
                Person person = g.fromJson(output, Person.class);
                String status = person.getStatus();
                String tokenValid = person.getTokenValid();
                System.out.println("checking status -->>-->> " + ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                if ((status.equalsIgnoreCase("failure")) && (tokenValid.equalsIgnoreCase("false"))) {
                    session.clear();
                    HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
                    System.out.println("status is -->>-->> failure redirected to url " + ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
                    //res.sendRedirect("https://parichay.staging.nic.in:8080/Accounts/Services?service=efromsCO");
                    //res.sendRedirect("https://parichay.nic.in/Accounts/Services?service=eforms");//fullycommented
                    return "failedredirect";
                }
            }

            return invocation.invoke();

        }

        /*
        else if (path != null && path.toString().contains("registration")) {
=======
        /*else if (path != null && path.toString().contains("registration")) {
>>>>>>> Stashed changes
            System.out.println("path = " + path);
            if (path.toString().toLowerCase().contains("daonboarding")) {
                System.out.println("daonboarding " );
                session.put("da_onboarding", "true");
                session.put("da_onboarding_2", "true");
            } else {
                System.out.println("not daonboarding " );
                session.remove("da_onboarding");
                session.remove("da_onboarding_2");
            }
        }
         */
//         else if(session.isEmpty()){
//            
//             return "ssosessionout";
////         String timeoutUrl = "https://parichay.staging.nic.in:8080/Accounts/ClientManagement?sessionTimeOut=true&service=efromsCO";
////         String timeoutUrl = "https://parichay.nic.in/Accounts/ClientManagement?sessionTimeOut=true&service=eforms"; 
////         HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
////         System.out.println("Redirecting :----");
////         res.sendRedirect(res.encodeRedirectURL(timeoutUrl));
//         }
        Map<String, Object> sessionAttributes = invocation.getInvocationContext().getSession();
        //System.out.println("=====================================" + sessionAttributes);
        if (sessionAttributes == null || sessionAttributes.isEmpty()) {
            System.out.println("SESSION EXPIRED");
            return "logout";
        } else {
            /*it will uncomment when update-mobile will go live*/

            System.out.println("SESSION NOT EXPIRED");
            UserData userdata = (UserData) sessionAttributes.get("uservalues");
            Database db = new Database();
            String loggedinEmail = "";
            if (sessionAttributes.get("login") != null) {
                loggedinEmail = sessionAttributes.get("login").toString();
            }
            //System.out.println("Logged in EMail " + loggedinEmail + "session Attribute Login = " + sessionAttributes.get("login"));

            if (loggedinEmail.isEmpty() || sessionAttributes.get("login") == null) {
                if (loggedinEmail.isEmpty()) {
                    return invocation.invoke();
                } else {
                    // System.out.println("if user is null in login interceptor");
                    //System.out.println("Session not available ");

                    String logout_status = "logout by eforms as login key in session is null";
                    db.updateAuditTableForLogout(userdata.getEmail(), logout_status);
                    return "logout";
                }
            } else {
//                Ldap ldap = new Ldap();
//                if (ldap.emailValidate(loggedinEmail)) {
//                    if (sessionAttributes.get("update_without_oldmobile") != null) {
//                        if (sessionAttributes.get("update_without_oldmobile").toString().equalsIgnoreCase("yes")) {
//                            userdata.setRole("");
//                            List<String> roles = new ArrayList<>();
//                            userdata.setRoles(roles);
//                            sessionAttributes.remove("uservalues");
//                            sessionAttributes.put("uservalues", userdata);
//
//                            String uriString = ServletActionContext.getRequest().getRequestURI();
//                            URI uri = new URI(uriString);
//                            String path = uri.getPath();
//                            String actionName = path.substring(path.lastIndexOf('/') + 1);
//                            System.out.println("Action Name = " + actionName);
//
//                            if (actionName.equals("Mobile_registration") || actionName.equals("Mobile_registration.action") || actionName.equals("mobile_tab1") || actionName.equals("mobile_tab2") || actionName.equals("mobile_tab1.action") || actionName.equals("mobile_tab2.action")) {
//                                if (sessionAttributes.get("profile_present").toString().equalsIgnoreCase("true")) {
//                                    Map userValues = new HashMap();
//                                    if (userdata.isIsNewUser()) {
//                                        userValues.put("error", "User Not valid");
//                                    }
//                                    sessionAttributes.put("profile-values", userValues);
//                                    return invocation.invoke();
//                                } else {
//                                    return "showprofile";
//                                }
//                            } else {
//                                return "logout";
//                            }
//                        }
//                    }
//                }
                return invocation.invoke();
            }
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
