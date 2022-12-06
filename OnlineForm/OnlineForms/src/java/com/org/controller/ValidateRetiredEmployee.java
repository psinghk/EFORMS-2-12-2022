/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import admin.AdminAction;
import admin.ForwardAction;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.EmailService;
import com.org.service.SignUpService;
import com.org.ssologin.SSODecodeString;
import com.org.ssologin.SsoAuth;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.FormBean;
import model.Person;
import static org.apache.poi.hssf.usermodel.HeaderFooter.date;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author rahul
 */
public class ValidateRetiredEmployee extends ActionSupport implements SessionAware, ServletRequestAware {

    String dateOfRetirement = "";
    private Ldap ldap = null;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    private SessionMap<String, Object> session;
    String localTokenId;
    String uid;
    String browserId;
    String userName;
    String sessionId;
    String mobileNo;
    String name;
    HttpServletRequest request = ServletActionContext.getRequest();
    String userJSONDec;
    String captcha;
    String CSRFRandom;
    Map error = new HashMap();
    EmailService emailservice = new EmailService();

    HttpServletResponse res = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
    String response_string;
    Person person;
    //private String api_key = "tzcdo2Gm337yOPL1rJnEE1qvDFFpMWTX";
    //private String api_key="n4LmmzNH9q3C+Ny8F1WHs3yqhbfJfTkN";
    //private String api_key = "I1JryyZnbozv9ukR787GT/Li0DX6nWIa";
    private String api_key = "pSm2yyZnbozv9ukR787GT/ILqwab761M";

    private Database db = null;
    String path;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;
    }

    public Map getError() {
        return error;
    }

    public String getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(String dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public Ldap getLdap() {
        return ldap;
    }

    public String getIp() {
        return ip;
    }

    public String getLocalTokenId() {
        return localTokenId;
    }

    public String getUid() {
        return uid;
    }

    public String getBrowserId() {
        return browserId;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getUserJSONDec() {
        return userJSONDec;
    }

    public String getResponse_string() {
        return response_string;
    }

    public String getApi_key() {
        return api_key;
    }

    public Database getDb() {
        return db;
    }

    public String getPath() {
        return path;
    }

    public ValidateRetiredEmployee() {
        ldap = new Ldap();
        db = new Database();
    }

    public String validateRetiredBo() throws ParseException {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                + "inside validateRetired etired officer----");
        if (request != null) {
            path = request.getRequestURI();
        }
        System.out.println("Path in validateRetired ====" + path);
        response_string = request.getParameter("string");

        System.out.println("string=" + response_string);

        if (response_string == null || response_string.isEmpty()) {
            System.out.println("inside response string in retired...");
            return "fetchresponseStringretired";
        }

        try {
            try {
                userJSONDec = SSODecodeString.decyText(response_string, api_key);
                if (userJSONDec != null && userJSONDec.isEmpty()) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "failure parichay as userJSONDec is blank ..redirected");
                    return "temperedretired";
                }
                System.out.println(userJSONDec);
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "failure parichay as userJSONDec is blank TEMPERED ..redirected");
                return "temperedretired";
            }
        } catch (Exception ex) {
            Logger.getLogger(SsoAuth.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (userJSONDec != null && !userJSONDec.isEmpty()) {
            Gson g = new Gson();
            person = g.fromJson(userJSONDec, Person.class);
            System.out.println(person.getLocalTokenId());
            System.out.println("Request inside sso====" + request.getParameter("string"));

            localTokenId = person.getLocalTokenId();

            session.put("retired_string", userJSONDec);
            uid = person.getEmail();
            browserId = person.getBrowserId();
            userName = person.getUserName();
            sessionId = person.getSessionId();
            mobileNo = person.getMobileNo();
            name = person.getFullName();
            person.setReason("Retired officer for email acount validation extension");
           // activaleEmail(uid);   // for activate email id
            //Testcase check status
            String url1 = "https://parichay.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + localTokenId + "&userName=" + userName + "&service=eformsretired&browserId=" + browserId + "&sessionId=" + sessionId;
            System.out.println(url1);
            String output = HTTP_URL_Response(url1);
            System.out.println("interceptor response from 10.122.34.117:8081" + output);
            Gson gs = new Gson();
            Person person = gs.fromJson(output, Person.class);
            String status = person.getStatus();
            String tokenValid = person.getTokenValid();

            if ((status.equalsIgnoreCase("failure")) && (tokenValid.equalsIgnoreCase("false"))) {
                session.clear();
                return "failedredirectretired";
            }

            boolean sessionout = false;
            Map<String, Object> personDetails = db.fetchFromParichayDataStorage(uid);
            if (!personDetails.isEmpty()) {
                String oldLocaltokenId = personDetails.get("localTokenId").toString();
                String oldBrowserId = personDetails.get("browserId").toString();
                String oldSessionId = personDetails.get("sessionId").toString();
                String url = "https://parichay.nic.in:8081/Accounts/openam/login/isTokenValid?localTokenId=" + oldLocaltokenId + "&userName=" + userName + "&service=eformsretired&browserId=" + oldBrowserId + "&sessionId=" + oldSessionId;
                System.out.println(url);
                String oldOutput = HTTP_URL_Response(url);
                Person oldPerson = g.fromJson(oldOutput, Person.class);
                String oldStatus = oldPerson.getStatus();
                String oldTokenValid = oldPerson.getTokenValid();

                if ((oldStatus.equalsIgnoreCase("success")) && (oldTokenValid.equalsIgnoreCase("true"))) {
                    try {
                        sessionout = db.fetchLoginDetailsSsoCallback(uid);
                        if (sessionout) {
                            System.out.println("Retired Sessionout parichay");
                            db.updateLogoutDetailsForcefully(uid, "logged out forcefully as sessionout from Retired module");
                            return "ssosessionoutretired";
                        }
                    } catch (ParseException ex) {
                        System.out.println("Retired Sessionout parichay ----" + ex.getMessage());
                        Logger.getLogger(SsoAuth.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //Insert into parichay_data_storage
            int parichayInsert = db.insertParichayDataStorage(person);
            if (parichayInsert > 0) {
                System.out.println("Inserted into Retired parichay table(login_details) " + person.getEmail());
            }
        } else {
            return "error3";
        }

        db.insertIntoAuditTable(uid, "user_retired", "Authentication Sucessfull");

        session.put("email", uid);
        session.put("browserid", browserId);
        session.put("sessionid", sessionId);
        session.put("localtokenid", localTokenId);
        session.put("name", name);
        session.put("mobile", mobileNo);
        Ldap ldap = new Ldap();
        validation valid = new validation();

        String email = uid;
        Set<String> bos = ldap.fetchBosForRetiredEmployees(email);
        System.out.println("com.org.controller.Email_registration.dor_ext_retired_tab1() Bos:" + bos.toString());
        session.put("bos", bos);

        boolean isBoAllowed = false;
        // bos.add("retiredoficers");  // temray added by rahul 
        for (String bo : bos) {
            if (bo.equalsIgnoreCase("retiredoficers")) {
                isBoAllowed = true;
                break;
            }
        }
        System.out.println("isBoAllowed :" + isBoAllowed);
        if (!isBoAllowed) {
            return "error";
        }
        Email_registration emaiRegistration = new Email_registration();
        String formatedDate = "";
        String date = emaiRegistration.fetchDateOfAccountExpiryFromLdap(email);
        if (date.contains("Z")) {
            date = date.replace(date.substring(date.length() - 7), "");
            Date date1 = new SimpleDateFormat("yyyyMMdd").parse(date);
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            formatedDate = newFormat.format(date1);
            System.out.println("finallllllllll::::" + formatedDate);
            dateOfRetirement = formatedDate;
        } else {
            dateOfRetirement = date;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date nyear = cal.getTime();

        LocalDateTime oneYearAfter = nyear.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String afterOneYearDate = oneYearAfter.format(format);

        LocalDate convertedDate = LocalDate.parse(afterOneYearDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        convertedDate = convertedDate.withDayOfMonth(
                convertedDate.getMonth().length(convertedDate.isLeapYear()));

        String newDor = convertedDate.format(format);  // add 1 year
        String preDor = dateOfRetirement;
        // String preDor = "20-09-2022";

        String msg = valid.dorValidationCustomRetired(newDor, preDor);
        // msg = "";
        if (!msg.isEmpty()) {
            if (msg.contains("expired")) {
                return "error2";
            } else if (msg.contains("revisit")) {
                return "error1";
            } else {
                return "error3";
            }
        } else {
            session.put("name", name);
            session.put("mobile", mobileNo);
            session.put("dor", preDor);
            session.put("newdor", newDor);
            return "success";
        }
    }

    String HTTP_URL_Response(String http_url) {
        String line = "";

        try {

            System.out.println("SOUT " + http_url);
            HttpURLConnection huc = (HttpURLConnection) new URL(http_url).openConnection();
            //HttpURLConnection.setFollowRedirects(false);
            huc.setConnectTimeout(2 * 1000);
            huc.connect();

            try (InputStream response2 = huc.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response2));
                for (String tmp; (tmp = reader.readLine()) != null;) {
                    line = tmp;
                    System.out.println("HTTP Response " + line);
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

    public String dor_ext_retired_tab1() {
        try {
            String capcode = (String) session.get("captcha");
            System.out.println("CSRFRandom " + CSRFRandom);
            System.out.println("captcha " + captcha);
            if (!captcha.equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
            }

            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            if (error.isEmpty()) {
                String email = session.get("email").toString();
                String mobile = session.get("mobile").toString();
                String name = session.get("name").toString();
                String dor = session.get("dor").toString();
                String newdor = session.get("newdor").toString();
                String ref_num = emailservice.dor_ext_retired_tab2_new(email, name, mobile, dor, newdor);

                if (ref_num != null && !ref_num.isEmpty()) {
                    session.put("ref_num", ref_num);
                } else {
                    CSRFRandom = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for  controller tab 1: " + e.getMessage());
            CSRFRandom = "";
        }
        return SUCCESS;
    }

//    @Override
//    public void setSession(Map<String, Object> map) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    @Override
//    public void setServletRequest(HttpServletRequest hsr) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = (SessionMap<String, Object>) session;
    }

//    public void activaleEmail(String email) {    // for activate email id
//
//        System.out.println("statrt process of activating email:");
//        ForwardAction frdAction = new ForwardAction();
//        AdminAction aa = new AdminAction();
//        String dn = frdAction.fetchLDAPDN(email, "dor_ext_retired");
//
//        HashMap<String, String> zim_values = new HashMap<String, String>();
//        zim_values.put("mailuserstatus", "active");
//        zim_values.put("inetuserstatus", "active");
//
//        //aa.modifyActivateAttribute(dn, zim_values, "daonboarding");
//        aa.modifyActivateAttribute_retired(dn, zim_values);
//    }
}
