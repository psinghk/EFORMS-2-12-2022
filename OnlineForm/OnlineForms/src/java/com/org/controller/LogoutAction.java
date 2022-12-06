package com.org.controller;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.service.SignUpService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;

public class LogoutAction extends ActionSupport implements SessionAware {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    private static final long serialVersionUID = -6765991741441442190L;
    private SessionMap<String, Object> sessionMap;
    private SignUpService signup = new SignUpService();
    private Database db = null;

    private final String protocolurl = ServletActionContext.getServletContext().getInitParameter("protocolurl");

    public LogoutAction() {
        db = new Database();
    }

    @Override
    public String execute() throws Exception {
        System.out.println("user loggedout");

        try {

            if (sessionMap.isEmpty()) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "session empty");
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "session not empty");

                UserData userData = (UserData) sessionMap.get("uservalues");
                if (sessionMap.containsKey("loginType")) {
                    if (sessionMap.get("loginType").toString().equalsIgnoreCase("sso")) {
                        return "logout_parichay";
                    }
                }
                String logout_status = "logout by eforms";
                String userEmail = "";
                if (userData != null) {
                    System.out.println("userData.getEmail()" + userData.getEmail());
                    logout_status = "logout by eforms";
                    userEmail = userData.getEmail();
                } else {
                    logout_status = "logout due to some code changes directly.";
                    userEmail = "eforms@nic.in";
                }

                db.updateAuditTableForLogout(userEmail, logout_status);

                if (sessionMap.containsKey("uservalues")) {
                    sessionMap.put("uservalues", null);
                    sessionMap.remove("uservalues");
                }
                if (sessionMap.containsKey("profile-values")) {
                    sessionMap.put("profile-values", null);
                    sessionMap.remove("profile-values");
                }

                if (sessionMap.containsKey("email")) {
                    sessionMap.put("email", "");
                    sessionMap.remove("email");
                }
                if (sessionMap.containsKey("login")) {
                    sessionMap.put("login", "");
                    sessionMap.remove("login");
                }

                ((SessionMap) this.sessionMap).invalidate();

            }
            System.out.println("session map logged out |" + sessionMap);

        } catch (Exception e) // try catch added by pr on 25thapr19
        {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + "EXCEPTION in logoutaction " + e.getMessage());
        }
        return SUCCESS;
    }

    public void parichayLogoutBackup() {

        if (sessionMap.get("path") != null && sessionMap.get("path").toString().contains("ssologin")) {
            System.out.println("Path====" + sessionMap.get("path").toString());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " session path logout sso ");

            String userName = sessionMap.get("userName").toString();
            String sessionId = sessionMap.get("sessionId").toString();
            String browserId = sessionMap.get("browserId").toString();
            db.updateAuditTableForLogout(userName, "logout by parichay");
            // String url = "http://10.122.34.117:8081/Accounts/openam/login/logoutAll?userName=" + userName + "&service=efromsCO&sessionId=" + sessionId + "&browserId=" + browserId + "";
            //String url="https://parichay.staging.nic.in:8080/Accounts/openam/login/logoutAll?userName="+userName+"&service=efromsCO&sessionId="+sessionId+"";
            //String url = "https://parichay.pp.nic.in/Accounts/openam/login/logoutAll?userName=" + userName + "&service=eforms&sessionId=" + sessionId + "";
            String url = "https://parichay.nic.in/Accounts/openam/login/logoutAll?userName=" + userName + "&service=eforms&sessionId=" + sessionId + "";
            System.out.println("url====>>>>" + url);
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            sessionMap.put("str", "");
            sessionMap.put("path", "");

            sessionMap.remove("str");
            sessionMap.remove("path");
            ((SessionMap) this.sessionMap).invalidate();

            try {
                res.sendRedirect(url);
                // return "redirected";
            } catch (IOException ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + " Logout parichay err ");
                Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String parichayLogout() throws NoSuchAlgorithmException, IOException, KeyManagementException {

        String userName = sessionMap.get("userName").toString();
        String sessionId = sessionMap.get("sessionId").toString();
        String browserId = sessionMap.get("browserId").toString();

        if (sessionMap.get("path") != null && sessionMap.get("path").toString().contains("ssologin")) {
            System.out.println("Path====" + sessionMap.get("path").toString());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " session path logout sso ");

            SSLContext sc = SSLContext.getInstance("SSL");

            TrustManager[] dummyTrustManager = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            sc.init(null, dummyTrustManager, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            URL ur = null;
            try {
                ur = new URL("https://parichay.nic.in/Accounts/openam/login/logoutAll?userName=" + userName + "&service=eforms&sessionId=" + sessionId + "");
                //ur = new URL("https://servicedesk.nic.in/");
            } catch (MalformedURLException ex) {
                Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
            }

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;

                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            System.out.println("url = " + ur);
            HttpURLConnection urlConnection = (HttpURLConnection) ur.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            if (urlConnection.getResponseCode() == 200) {
                db.updateAuditTableForLogout(userName, "logout by parichay");
                HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
                sessionMap.put("str", "");
                sessionMap.put("path", "");

                sessionMap.remove("str");
                sessionMap.remove("path");

                if (sessionMap.containsKey("uservalues")) {
                    sessionMap.put("uservalues", null);
                    sessionMap.remove("uservalues");
                }
                if (sessionMap.containsKey("profile-values")) {
                    sessionMap.put("profile-values", null);
                    sessionMap.remove("profile-values");
                }

                if (sessionMap.containsKey("email")) {
                    sessionMap.put("email", "");
                    sessionMap.remove("email");
                }
                if (sessionMap.containsKey("login")) {
                    sessionMap.put("login", "");
                    sessionMap.remove("login");
                }
                ((SessionMap) this.sessionMap).invalidate();
            }
            in.close();
        }
        return SUCCESS;
    }

    public String parichayLogoutForRetiredOfficials() throws NoSuchAlgorithmException, IOException, KeyManagementException {

        String userName = sessionMap.get("email").toString();
        String sessionId = sessionMap.get("sessionid").toString();

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                + " retired session path logout sso ");

        SSLContext sc = SSLContext.getInstance("SSL");

        TrustManager[] dummyTrustManager = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        sc.init(null, dummyTrustManager, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        URL ur = null;
        try {
            ur = new URL("https://parichay.nic.in/Accounts/openam/login/logoutAll?userName=" + userName + "&service=eformsretired&sessionId=" + sessionId + "");
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;

            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        System.out.println("url = " + ur);
        HttpURLConnection urlConnection = (HttpURLConnection) ur.openConnection();

        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        if (urlConnection.getResponseCode() == 200) {
            db.updateAuditTableForLogout(userName, "logout by parichay for retired officials");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            sessionMap.put("str", "");
            sessionMap.put("path", "");

            sessionMap.remove("str");
            sessionMap.remove("path");

            if (sessionMap.containsKey("email")) {
                sessionMap.put("email", "");
                sessionMap.remove("email");
            }
            ((SessionMap) this.sessionMap).invalidate();
        }
        in.close();
        return SUCCESS;
    }
    
    public String parichayLogoutForRetiredOfficials1() throws NoSuchAlgorithmException, IOException, KeyManagementException {
        if (sessionMap.containsKey("email")) {
            sessionMap.put("email", "");
            sessionMap.remove("email");
        }
        ((SessionMap) this.sessionMap).invalidate();

        return SUCCESS;
    }

    public void coordinator_parichay() {
        try {
            System.out.println("redirect to get coordinator");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            res.sendRedirect(res.encodeRedirectURL(protocolurl + "/showLinkData?adminValue=co"));
            //res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eforms"));
        } catch (IOException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " parichayFetchString parichay err ");
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void support_parichay() {

        try {
            System.out.println("redirect to get coordinator");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            res.sendRedirect(res.encodeRedirectURL(protocolurl + "/showLinkData?adminValue=sup"));
            //res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eforms"));
        } catch (IOException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " parichayFetchString parichay err ");
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void admin_parichay() {
        try {
            System.out.println("redirect to get coordinator");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            res.sendRedirect(res.encodeRedirectURL(protocolurl + "/showLinkData?adminValue=admin"));
            //res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eforms"));
        } catch (IOException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " parichayFetchString parichay err ");
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void ca_parichay() {
        try {
            System.out.println("redirect to get coordinator");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            res.sendRedirect(res.encodeRedirectURL(protocolurl + "/showLinkData?adminValue=ca"));
            //res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eforms"));
        } catch (IOException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " parichayFetchString parichay err ");
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void ssoSessionoutPari() {
        String timeoutUrl = "https://parichay.nic.in/Accounts/ClientManagement?sessionTimeOut=true&service=eforms";
        System.out.println("timeouturl called");
        HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try {
            res.sendRedirect(res.encodeRedirectURL(timeoutUrl));

        } catch (IOException ex) {
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ssoSessionoutPariRetired() {
        String timeoutUrl = "https://parichay.nic.in/Accounts/ClientManagement?sessionTimeOut=true&service=eformsretired";
        System.out.println("retired timeouturl called");
        HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try {
            res.sendRedirect(res.encodeRedirectURL(timeoutUrl));
        } catch (IOException ex) {
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void parichayFetchString() {
        try {
            System.out.println("redirect to get String ");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eforms"));
        } catch (IOException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " parichayFetchString parichay err ");
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void parichayFetchStringRetired() {
        try {
            System.out.println("retired redirect to get String ");
            HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            res.sendRedirect(res.encodeRedirectURL("https://parichay.nic.in/Accounts/Services?service=eformsretired"));
        } catch (IOException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " retired parichayFetchString parichay err ");
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void failedredirect() {
        HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        System.out.println("status is -->>-->> failed redirected to url " + ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        try {
            res.sendRedirect("https://parichay.nic.in/Accounts/Services?service=eforms");
        } catch (IOException ex) {
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void failedredirectRetired() {
        HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        System.out.println("status is -->>-->> retired failed redirected to url " + ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        try {
            res.sendRedirect("https://parichay.nic.in/Accounts/Services?service=eformsretired");
        } catch (IOException ex) {
            Logger.getLogger(LogoutAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void setSession(Map<String, Object> map) {
        sessionMap = (SessionMap<String, Object>) map;
    }

}
