package com.org.controller;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.utility.Random;
import com.org.validation.Validation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.interceptor.SessionAware;
import com.org.service.SignUpService;
import com.org.utility.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import org.apache.struts2.ServletActionContext;
import rabbitmq.NotifyThrouhRabbitMQ;
import utility.Inform;

public class LoginAction extends ActionSupport implements SessionAware {

    private static final String protocolurl = ServletActionContext.getServletContext().getInitParameter("protocolurl");

    private static final long serialVersionUID = -6765991741441442190L;
    private boolean refreshFlag = false;
    private String switchUrl = "";
    private UserData userValues;
    private Validation validate;
    private Otp otp;
    private SessionMap<String, Object> sessionMap;
    private Ldap ldap = null;
    private Database db = null;
    private Inform inform = null;
    private Map<String, Object> userValuesFromLdap;
    private Map<String, String> userValuesFromDb;
    private Map<String, Object> updateMobileMap;
    private SignUpService signUpService = null;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    //String ip = "10.26.112.140";
    Map profile_values = null;
    String returnString;

    //start, code added by pr on 6thsep19
    Map DAOGotMap = null;

    Map DAOGotFFMap = null;

    String sessionid = ServletActionContext.getRequest().getSession().getId();
    private Map<String, Object> session;

    public String getSwitchUrl() {
        return switchUrl;
    }

    public void setSwitchUrl(String switchUrl) {
        this.switchUrl = switchUrl;
    }

    public boolean isRefreshFlag() {
        return refreshFlag;
    }

    public void setRefreshFlag(boolean refreshFlag) {
        this.refreshFlag = refreshFlag;
    }

    //end, code added by pr on 6thsep19
    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    public Map<String, Object> getUpdateMobileMap() {
        return updateMobileMap;
    }

    public void setUpdateMobileMap(Map<String, Object> updateMobileMap) {
        this.updateMobileMap = updateMobileMap;
    }

    public LoginAction() {
        userValues = new UserData();
        validate = new Validation();
        ldap = new Ldap();
        db = new Database();
        otp = new Otp();
        inform = new Inform();
        signUpService = new SignUpService();
        profile_values = new HashMap();
        if (userValues == null) {
            userValues = new UserData();
        }
        if (validate == null) {
            validate = new Validation();
        }
        if (ldap == null) {
            ldap = new Ldap();
        }
        if (db == null) {
            db = new Database();
        }
        if (otp == null) {
            otp = new Otp();
        }
        if (signUpService == null) {
            signUpService = new SignUpService();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
    }

    @Override
    public void validate() {

        System.out.println("Validating email address" + userValues.getEmail());
        if (userValues.getEmail() != null) {
            if (userValues.getEmail().isEmpty()) {
                addFieldError("email", "empty");
            } else if (!validate.checkFormat("EMAIL", userValues.getEmail())) {
                addFieldError("email", "incorrect format");
            }
        }
    }

    @Override
    public String execute() throws Exception {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "EMAIL in LoginAction : " + userValues.getEmail());

        userValues.setIsEmailValidated(ldap.emailValidate(userValues.getEmail()));
        if (!userValues.isIsEmailValidated()) {
            Set<String> aliases = new HashSet<String>();
            aliases.add(userValues.getEmail());
            if (db.isUserBlockedDueToWrongPasswordAttempts(aliases, "")) {
                sessionMap.remove("captcha");
                sessionMap.remove("loginTemp");
                sessionMap.put("authFailed", "true");
                sessionMap.put("Block_user", "true");
                userValues.setBlocked(true);
                return SUCCESS;
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "If user does not belong to LDAP");

            userValues.setIsNewUser(db.isNewUser(userValues.getEmail()));
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "isIsNewUser::::" + userValues.isIsNewUser());

            if (!userValues.isIsNewUser()) {

                userValues.setMobile(db.getMobile(userValues.getEmail()));
                // userValues.setIsMobileInLdap(ldap.isMobileInLdap(userValues.getMobile()));
                Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(userValues.getMobile());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP:  size" + emailsAgainstMobile.size());
                userValues.setEmailCountAgainstMobile(emailsAgainstMobile.size());
                if (emailsAgainstMobile.size() > 0) {
                    String emailAgainstMobileInString = emailsAgainstMobile.toString();
                    userValues.setEmailAgainstMobileNumber(emailAgainstMobileInString);
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "isIsMobileInLdap::::" + userValues.isIsMobileInLdap());

            }
        } else {

            boolean adminflag = false;
            List<String> supportEmails = Arrays.asList("support@nic.in", "support@gov.in", "support@nkn.in", "vpnsupport@nic.in", "smssupport@nic.in", "vpnsupport@gov.in", "smssupport@gov.in");

            if (supportEmails.contains(userValues.getEmail().toLowerCase())) {
                adminflag = true;
            }

            System.out.println("admin flag" + adminflag);
            if (adminflag) {
                System.out.println("inside admin flag");
                userValues.setSsoAllowed("disallowed");

            } else {
                System.out.println("inside not admin flag");
                //code for session out
                boolean sessionout = db.fetchLoginDetailsSso(userValues.getEmail());
                // sessionout=true;
                if (sessionout) {
                    System.out.println("redirected.........****");
                    userValues.setPariSessionout("parichayout");
                } else {
                    userValues.setPariSessionout("parichaycontinue");
                }
                //EO code for session out
                userValues.setSsoAllowed("ssologin");
            }

            if (protocolurl.contains(
                    "10.1.162.243:8080")) {
                userValues.setSsoAllowed("skipped");
                userValues.setPariSessionout("skipped");
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "If user does not belong to LDAP::::");
            Set<String> aliases = ldap.fetchAliases(userValues.getEmail());

            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());  ////  why this line is here an object can be assigned directly into another

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "aliases::::" + aliases);
            if (aliases.size()
                    > 10) {
                aliases.add(userValues.getEmail());
                userValues.setAliases(aliases);

            } else {
                userValues.setAliases(aliases);
            }

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1 && sizeOfAliases
                    <= 10) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases
                    == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (!db.isNewUser(email)) {
                        userValues.setIsNewUser(false);
                        break;
                    }
                }
            }

        }

        sessionMap.put("isGov", userValues.isIsEmailValidated());
        sessionMap.put("email", userValues.getEmail());

        //sessionMap.put("update_mobile", "update_mobile");
        if (sessionMap.get("loginTemp") != null) {
            sessionMap.remove("loginTemp");
        }
        if (sessionMap.get("authFailed") != null) {
            sessionMap.remove("authFailed");
        }

        return SUCCESS;
    }

    public String checkEmail() throws Exception {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "EMAIL in LoginAction : " + userValues.getEmail());
        userValues.setIsEmailValidated(ldap.emailValidate(userValues.getEmail()));
        if (!userValues.isIsEmailValidated()) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "If user does not belong to LDAP");

            userValues.setIsNewUser(db.isNewUser(userValues.getEmail()));
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "isIsNewUser::::" + userValues.isIsNewUser());

            if (!userValues.isIsNewUser()) {

                userValues.setMobile(db.getMobile(userValues.getEmail()));
                // userValues.setIsMobileInLdap(ldap.isMobileInLdap(userValues.getMobile()));
                Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(userValues.getMobile());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP:  size" + emailsAgainstMobile.size());
                userValues.setEmailCountAgainstMobile(emailsAgainstMobile.size());
                if (emailsAgainstMobile.size() > 0) {
                    String emailAgainstMobileInString = emailsAgainstMobile.toString();
                    userValues.setEmailAgainstMobileNumber(emailAgainstMobileInString);
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "isIsMobileInLdap::::" + userValues.isIsMobileInLdap());

            }
        } else {

            // boolean adminflag = false;
//            if (userValues.getEmail().contains("support")) {
//                adminflag = true;
//            }
            //System.out.println("admin flag" + adminflag);
//            if (adminflag) {
//                System.out.println("inside admin flag");
//                userValues.setSsoAllowed("disallowed");
//
//            } else {
//                System.out.println("inside not admin flag");
//                //code for session out
//                boolean sessionout = db.fetchLoginDetailsSso(userValues.getEmail());
//                // sessionout=true;
//                if (sessionout) {
//                    System.out.println("redirected.........****");
//                    userValues.setPariSessionout("parichayout");
//                } else {
//                    userValues.setPariSessionout("parichaycontinue");
//                }
//                //EO code for session out
//                userValues.setSsoAllowed("ssologin");
//            }
            if (protocolurl.contains(
                    "10.1.162.243:8080")) {
                userValues.setSsoAllowed("skipped");
                userValues.setPariSessionout("skipped");
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "If user does not belong to LDAP::::");
            Set<String> aliases = ldap.fetchAliases(userValues.getEmail());

            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());  ////  why this line is here an object can be assigned directly into another

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "aliases::::" + aliases);
            if (aliases.size()
                    > 10) {
                aliases.add(userValues.getEmail());
                userValues.setAliases(aliases);

            } else {
                userValues.setAliases(aliases);
            }

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1 && sizeOfAliases
                    <= 10) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases
                    == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (!db.isNewUser(email)) {
                        userValues.setIsNewUser(false);
                        break;
                    }
                }
            }

            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (db.MobileRequestAlreadyExist(email)) {
                        userValues.setExistMobileRequest("true");
                        break;
                    }
                }
            }

            //Set response for blocked users
            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (db.fetchTrailMobileUpdateBlockOneHour(email, ip).equals("blocked")) {

                        userValues.setBlocked(true);

                        break;
                    }
                }
            }
            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (db.MobileRequestAlreadyPending(email)) {
                        userValues.setExistMobileRequestByFlow("true");
                        sessionMap.put("existMobileRequestByFlow", "true");
                        break;
                    }
                }

            }

            for (String email : aliases) {
                if (!email.isEmpty()) {
                    if (db.checkEmailForUpdateMobile(email).equals("You_Are_NOT_Eligible")) {
                        userValues.setUserAuthenticatedForUpdateMobile("You_Are_NOT_Eligible");

                        if (sessionMap.get("You_Are_NOT_Eligible") == null) {
                            sessionMap.put("You_Are_NOT_Eligible", "true");
                            String from = "eforms@nic.in";
                            String sub = "Important eForms : Someone used your email to Update Mobile";
                            StringBuilder sb = new StringBuilder();
                            // sb.append("Dear Team, Someone is trying to reset the password.Please find the details. Password for this email <b>" + email + "</b> and IP <b>" + ip + "</b>.can not be reset using update mobile portal as it is a high priority account");
                            sb.append("Dear Sir/Madam,\nSomeone is trying to reset the mobile number of '" + email + "' IP '" + ip + "' which is a high priority account. This is just for your information. This mail does not mean that someone knows your credentials.");
                            sb.append("<p>Regards,</p>");
                            sb.append("eForms Team NIC");
                            // inform.sendToRabbit(from, "meenaxi.nhq@nic.in", sb.toString(), sub, "", "", null, null, null);
                            inform.sendToRabbit(from, userValues.getEmail(), sb.toString(), sub, "", "", null, null, null);
                        }

                        break;

                    }
                }
            }

        }

        sessionMap.put("isGov", userValues.isIsEmailValidated());
        sessionMap.put("email", userValues.getEmail());

        if (!protocolurl.contains(
                "10.1.162.243:8080")) {
            sessionMap.put("update_mobile", "update_mobile");
        }
        //sessionMap.put("update_mobile", "update_mobile");
        if (sessionMap.get("loginTemp") != null) {
            sessionMap.remove("loginTemp");
        }
        if (sessionMap.get("authFailed") != null) {
            sessionMap.remove("authFailed");
        }
        if (sessionMap.get("Block_user") != null) {
            sessionMap.remove("Block_user");
        }
        if (sessionMap.get("You_Are_NOT_Eligible") != null) {
            sessionMap.remove("You_Are_NOT_Eligible");
        }
        if (sessionMap.get("existMobile") != null) {
            sessionMap.remove("existMobile");
        }
        if (sessionMap.get("user_validated") != null) {
            sessionMap.remove("user_validated");
        }
        if (sessionMap.get("exist_in_kavach") != null) {
            sessionMap.remove("exist_in_kavach");
        }

        if (sessionMap.get("existMobileRequestByFlow") != null) {
            sessionMap.remove("existMobileRequestByFlow");
        }

        if (sessionMap.get("comingFromUpdateMobile") != null) {
            sessionMap.remove("comingFromUpdateMobile");
        }

        return SUCCESS;
    }

    public String processNonGovEmail() {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "User does not belong to LDAP in processNonGovEmail::::");

        userValues.setIsNewUser(db.isNewUser(userValues.getEmail()));

        System.out.println("isNewUser1 = " + userValues.isIsNewUser());
        // If old user
        if (!userValues.isIsNewUser()) {
            userValues.setMobile(db.getMobile(userValues.getEmail()));
            userValues.setIsMobileInLdap(ldap.isMobileInLdap(userValues.getMobile()));
            Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(userValues.getMobile());
            userValues.setEmailCountAgainstMobile(emailsAgainstMobile.size());
            System.out.println("emailsAgainstMobile.size()" + emailsAgainstMobile.size());
            if (emailsAgainstMobile.size() > 0) {
                String emailAgainstMobileInString = emailsAgainstMobile.toString();
                userValues.setEmailAgainstMobileNumber(emailAgainstMobileInString);
            }
            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " emailsAgainstMobile" + emailsAgainstMobile+"!userValues.isIsMobileInLdap()"+!userValues.isIsMobileInLdap());

            System.out.println("userValues.isIsMobileInLdap()" + userValues.isIsMobileInLdap());
            if (emailsAgainstMobile.size() < 1) {
                System.out.println("inside false and generate otp");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "if MobileInLdap does not exist");

                generateOtp();
            }
        }

        return SUCCESS;
    }

    public String isAuthenticUser() {
        String capcode = (String) sessionMap.get("captcha");
        if (capcode.equals(userValues.getLoginCaptcha())) {
            userValues.setLoginCaptchaAuthenticated(true);

            String user_mobile = ldap.fetchMobileFromLdap(userValues.getEmail());
            String email = userValues.getEmail();
            Set<String> aliases = ldap.fetchAliases(email);
            if (userValues.getMobile() != null && !userValues.getMobile().startsWith("+")) {
                userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
            } else {
                userValues.setMobile(user_mobile);
            }
            if (db.isUserBlockedDueToWrongPasswordAttempts(aliases, userValues.getMobile())) {
                sessionMap.remove("captcha");
                sessionMap.remove("loginTemp");
                sessionMap.put("authFailed", "true");
                sessionMap.put("Block_user", "true");
                userValues.setBlocked(true);
                return SUCCESS;
            }

            if (ldap.authenticate(userValues.getEmail(), userValues.getPassword())) {
                userValues.setIsEmailValidated(true);
                userValues.setMobile(ldap.fetchMobileFromLdap(userValues.getEmail()));
                userValues.setAuthenticated(true);
                String loginTemp = entities.Random.csrf_random();
                userValues.setToken(loginTemp);
                sessionMap.put("loginTemp", loginTemp);
                sessionMap.remove("authFailed");
                if (!(email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("support@nkn.in") || email.equals("smssupport@nic.in") || email.equals("vpnsupport@nic.in"))) {
                    if (protocolurl.contains(
                            "10.1.162.243:8080")) {
                        generateOtp();
                    }
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "USER AUTHENTICATED::::");
            } else {
                if ((userValues.getEmail().equalsIgnoreCase("support@gov.in") || userValues.getEmail().equalsIgnoreCase("support@nic.in") || userValues.getEmail().equalsIgnoreCase("support@nkn.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@nic.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@gov.in") || userValues.getEmail().equalsIgnoreCase("smssupport@nic.in") || userValues.getEmail().equalsIgnoreCase("smssupport@gov.in"))) {
                    db.insertIntoAuditTable(userValues.getEmail(), "", "Authentication fail due to wrong password with mobile no:" + userValues.getMobile());
                } else {
                    db.insertIntoAuditTable(userValues.getEmail(), "", "Authentication fail due to wrong password");
                }
                if (db.fetchCountOfWrongPasswordAttempts(aliases, userValues.getMobile()) > 6) {
                    if ((userValues.getEmail().equalsIgnoreCase("support@gov.in") || userValues.getEmail().equalsIgnoreCase("support@nic.in") || userValues.getEmail().equalsIgnoreCase("support@nkn.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@nic.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@gov.in") || userValues.getEmail().equalsIgnoreCase("smssupport@nic.in") || userValues.getEmail().equalsIgnoreCase("smssupport@gov.in"))) {
                        db.updateBlockUserTable(userValues.getEmail(), "Blocked due to wrong consecutive 6 passwords with mobile no:" + userValues.getMobile());
                    } else {
                        db.updateBlockUserTable(userValues.getEmail(), "Blocked due to wrong consecutive 6 passwords");
                    }
                }
                sessionMap.remove("loginTemp");
                sessionMap.put("authFailed", "true");
            }
        }

        sessionMap.remove("captcha");

        return SUCCESS;
    }

    public String isAuthenticUserForUpdateMobile() {
        boolean auth = ldap.authenticate(userValues.getEmail(), userValues.getPassword());
        if (auth) {
            userValues.setMobile(ldap.fetchMobileFromLdap(userValues.getEmail()));
        } else {
            userValues.setMobile("");

        }
        Set<String> aliases = ldap.fetchAliases(userValues.getEmail());
        sessionMap.put("comingFromUpdateMobile", "updateMobile");
        for (String email : aliases) {
            if (!email.isEmpty()) {
                if (db.checkEmailForUpdateMobile(email).equals("You_Are_NOT_Eligible")) {
                    sessionMap.put("You_Are_NOT_Eligible", "true");
                    break;
                }
            }
        }

        for (String email : aliases) {
            if (!email.isEmpty()) {
                if (db.MobileRequestAlreadyExist(email)) {
                    sessionMap.put("existMobile", "true");
                    break;
                }
            }
        }

        if (sessionMap.get("existMobile") == null && sessionMap.get("You_Are_NOT_Eligible") == null) {
            try {

                int count = 1;
                //boolean auth = ldap.authenticate(userValues.getEmail(), userValues.getPassword());
                String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");

                if (sessionMap.get("comingFromUpdateMobile") != null && sessionMap.get("comingFromUpdateMobile").equals("updateMobile")) {
                    for (String email : aliases) {
                        if (!email.isEmpty()) {
                            if (db.fetchTrailMobileUpdateBlockOneHour(email, ip).equals("blocked")) {
                                userValues.setMsg("blocked");
                                userValues.setBlocked(true);

                                if (sessionMap.get("Block_user") == null) {
                                    System.out.println("BLOCKUSERRRRRRRRRRR");

                                    //Map<String, Object> datasent = db.fetchTrailMobileUpdate(email);
                                    //String status = datasent.get("status").toString();
                                    //if (status.equalsIgnoreCase("failed")) {
                                    sessionMap.put("Block_user", email);
                                    String from = "eforms@nic.in";
                                    String emailTo = email;
                                    String sub = "eForms : Multiple Login Attempts for updating Mobile";
                                    //String smsMessage="Dear user,  Your account has been tried to logged in from '"+ip+"' .If not you, Please contact NIC Service desk";

                                    StringBuilder sb = new StringBuilder();

                                    sb.append("<p>Dear user,</p><p>Your email address was being used from " + ip + " to update mobile number using incorrect password. This is for your information.</p>");
                                    sb.append("<p>Regards,</p>");
                                    sb.append("eForms Team NIC");
                                    //inform.sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);// send sms is given below
                                    inform.sendToRabbit(from, emailTo, sb.toString(), sub, "", "", null, null, null);
                                    System.out.println("BLOCKUSER:::::" + sb.toString());
                                    HashMap<String, Object> map_sms = new HashMap<>();
                                    map_sms.put("smsbody", "Dear user,  Your email address was being used from " + ip + " to update mobile number.If its not you, Please contact <b>incident[at]nic-cert[dot]nic[dot]in</b>. NICSI");
                                    if (userValues.getMobile() == null || userValues.getMobile().isEmpty()) {
                                        map_sms.put("mobile", ldap.fetchMobileFromLdap(userValues.getEmail()));
                                    } else {
                                        map_sms.put("mobile", userValues.getMobile());
                                    }
                                    map_sms.put("templateId", "1107161647440491832");
                                    NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                    object.sendSmsOtpRabbitMq(map_sms);
                                    //}
                                }
                                return SUCCESS;
                            }
                        }

                    }

                    //}
                }
                System.out.println("message=" + userValues.getMsg());

                if (sessionMap.get("count") != null) {
                    int countfinal = Integer.parseInt(sessionMap.get("count").toString());
                    countfinal++;
                    sessionMap.put("count", countfinal);
                } else {
                    sessionMap.put("count", 1);
                }

                //line added for update mobile count
                String capcode = (String) sessionMap.get("captcha");
                String userentrycaptcha = userValues.getLoginCaptcha();
                userValues.setAuthenticated(auth);
                System.out.println("capcode=" + capcode);
                if (!capcode.equals(userentrycaptcha)) {
                    userValues.setLoginCaptchaAuthenticated(false);
                } else {
                    userValues.setLoginCaptchaAuthenticated(true);
                }
                if (capcode.equals(userentrycaptcha)) {
                    if (!auth) {
                        System.out.println("all false");
                        db.insertMobileTrail(userValues.getEmail(), ip, sessionMap.get("count").toString(), "failed", userAgent);
                        sessionMap.remove("loginTemp");
                        sessionMap.put("authFailed", "true");
//                        updateMobileMap.put("designation", "");
//                        updateMobileMap.put("displayName", "");
//                        updateMobileMap.put("nicDateOfBirth", "");
//                        updateMobileMap.put("nicDateOfRetirement", "");
//                        updateMobileMap.put("uid", "");
                        userValues.setMobile("");
                    } else {
                        db.insertMobileTrail(userValues.getEmail(), ip, sessionMap.get("count").toString(), "success", userAgent);
                        userValues.setIsEmailValidated(true);

                        String loginTemp = entities.Random.csrf_random();
                        userValues.setToken(loginTemp);
                        sessionMap.put("loginTemp", loginTemp);
                        sessionMap.remove("authFailed");
                        System.out.println("successfully login::::::::");
                        if (sessionMap.get("comingFromUpdateMobile") != null && sessionMap.get("comingFromUpdateMobile").equals("updateMobile")) {
                            if (sessionMap.get("user_validated") == null) {
                                String from = "eforms@nic.in";
                                //String emailTo = "meenaxi.nhq@nic.in";
                                String emailTo = userValues.getEmail();
                                String sub = "eForms : Successfull authentication for updating Mobile";
                                //String smsMessage="Dear user,  Your account has been tried to logged in from '"+ip+"' .If not you, Please contact NIC Service desk";
                                StringBuilder sb = new StringBuilder();
                                sb.append("<p>Dear user,</p>");
                                sb.append("<p> Someone has initiated the process of updating your mobile number with your correct password on nic email platform for the email address  <b>" + userValues.getEmail() + "</b> and IP  <b>" + ip + "</b>.</p>");
                                sb.append("<p> IP from where this activity was done:- </p>" + ip);
                                sb.append("<p> If this is not you -- Please call on +91-12290-2400 or drop a mail to <b>incident[at]nic-cert[dot]nic[dot]in</b> NICSI</p>");
                                sb.append("<p>Regards,</p>");
                                sb.append("eForms Team NIC");
                                inform.sendToRabbit(from, emailTo, sb.toString(), sub, "", "", null, null, null);
                                System.out.println("successfully login::::::::" + sb.toString());
                                HashMap<String, Object> map_sms = new HashMap<>();
                                map_sms.put("smsbody", "Someone has initiated the process of updating your mobile number with your correct password on nic email platform for the email address  " + userValues.getEmail() + " and IP  " + ip + ". If this is not you -- Please call on +91-12290-2400  or drop a mail to incident[at]nic-cert[dot]nic[dot]in NICSI");
//                                    map_sms.put("smsbody", "Someone has initiated the process of updating your mobile number with your correct password on nic email platform for the email address " + userValues.getEmail() +". If this is not you -- Please call on +91-12290-2400 or drop a mail to incident%5bat%5dnic-cert%5bdot%5dnic%5bdot%5din NICSI");
                                //                                  map_sms.put("smsbody", "Someone+has+initiated+the+process+of+updating+your+mobile+number+with+your+correct+password+on+nic+email+platform+for+the+email+address+" + userValues.getEmail() +".+If+this+is+not+you+--+Please+call+on++91-12290-2400+or+drop+a+mail+to+incident%5bat%5dnic-cert%5bdot%5dnic%5bdot%5din+NICSI");                                    
                                map_sms.put("mobile", userValues.getMobile());
                                map_sms.put("templateId", "1107162237762367630");
                                NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                object.sendSmsOtpRabbitMq(map_sms);
                                sessionMap.put("user_validated", "true");
                                db.insertIntoAuditTable(userValues.getEmail(), "", "Authentication successfull using update mobile");
                            }
                            for (String email : aliases) {
                                updateMobileMap = ldap.fetchAttrUpdateMobile(email);
                                if (updateMobileMap != null) {
                                    System.err.println("attribute found for update_mobile map :::::::::" + email);
                                    break;

                                } else {
                                    System.err.println("attribute not-found for update_mobile map :::::::::" + email);
                                }

                            }
                        }
                    }
                }

                //Eo Line added for update mobile count
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String authCheck() {
        String val = sessionMap.get("loginTemp").toString();
        if (val == null || val.equals("")) {
            return ERROR;
        }
        if (!val.equals(userValues.getToken())) {
            userValues.setAuthenticated(false);
            userValues.setToken(NONE);
            return ERROR;
        } else {
            userValues.setReturnString(val);
            userValues.setAuthenticated(true);
            sessionMap.put("authCheck", true);
        }
        return SUCCESS;
    }

    public void assignRoles(String Email, boolean isIsEmailValidated, boolean isIsNewUser) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles::::");

        Map<String, String> map = new HashMap<>();
        Set<String> roles = new HashSet<>();
        List<String> rolesList = new ArrayList<>();
        //  List<String> rolesInString=new ArrayList<>();
        List<String> rolesListService = new ArrayList<>(); // 22ndoct19
        Map<String, Object> hashMap = new HashMap<>();
        String rolesInString = "";
        Set<String> aliases = new HashSet<>();
        ArrayList<String> employment_data = new ArrayList<>();
        userValues.setEmail(Email);
        userValues.setIsEmailValidated(isIsEmailValidated);
        userValues.setIsNewUser(isIsNewUser);
        System.out.println("userValues.getEmail()" + userValues.getEmail());
        if (userValues.isIsEmailValidated()) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles user is authenticated from ldap::::");
            map = ldap.isUserNICEmployee(userValues.getEmail());
            userValuesFromLdap = ldap.fetchLdapValues(userValues.getEmail());
            aliases = new HashSet<>();
            if (userValues.isIsEmailValidated()) {
                aliases = (Set<String>) userValuesFromLdap.get("aliases");
            }
            aliases.add(userValues.getEmail());   // added to verify for both mail and mail aliases
            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());
            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles aliases are::::" + aliases);
            if (aliases.size() > 10) {
                aliases = new HashSet<>();
                aliases.add(userValues.getEmail());
                userValues.setAliases(aliases);
            } else {
                userValues.setAliases(aliases);
            }
            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1 && sizeOfAliases <= 10) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases == 1 && sizeOfAliases <= 10) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

            userValues.setUserBo(map.get("bo"));
            if (map.get("isNICEmployee").equals("yes")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles if user is nic employee::::");
                userValues.setIsNICEmployee(true);
                aliases = userValues.getAliases();
                for (String email : aliases) {
                    if (!email.isEmpty()) {
                        if (!db.isNewUser(email)) {
                            userValues.setIsNewUser(false);
                            break;
                        }
                    }
                }

                if (userValues.isIsNewUser()) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles NEW USER::::");
                    userValues.setUid(map.get("uid"));
                    map.clear();
                    map = db.fetchUserValuesFromOAD(userValues.getUid()); //Fetching user  values from RO table

                    if (map.size() > 0) {
                        userValues.setIsInRoTable(true);
                        initializeUserFromOAD(map);
                    } else {
                        System.out.println("if doesn not exist in OAD");
                        userValues.setIsInRoTable(false);
                        initializeUserFromLDAP();
                    }
                } else {
                    userValues.setUid(map.get("uid"));
                    String mob = userValues.getMobile();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles OLD USER::::");
                    userValues.setIsNewUser(false);
                    map.clear();

                    for (String email : aliases) {
                        map = db.fetchUserValues(email, userValues.getMobile());
                        if (map.size() > 0) {
                            profile_values = (HashMap) signUpService.getUserValues(email, userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                            profile_values.put("mobile", mob);
                            initializeUserByProfile(map);
                            if (isIsEmailValidated) {
                                userValues.setMobile(mob);
                            }
                            employment_data = db.empCoordData((HashMap) map);
                            break;
                        }
                    }

                    if (!userValues.getMobile().contains("+")) {
                        userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
                    }
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles rolesInString::::" + rolesInString);
                }

                if (signUpService.isHog(userValues.getEmail())) {
                    userValues.setIsHOG(true);
                } else {
                    userValues.setIsHOG(false);
                }

                if (signUpService.isHod(userValues.getEmail())) {
                    userValues.setIsHOD(true);
                } else {
                    userValues.setIsHOD(false);
                }
                userValues.setHodEmail(db.fetchRo(userValues.getAliases()));
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles if user is not nic employee::::");
                userValues.setUid(map.get("uid"));
                boolean existEmail = true;
                userValues.setIsNICEmployee(false);
                if (aliases.size() > 10) {
                    aliases.add(userValues.getEmail());
                }
                for (String email : aliases) {
                    existEmail = db.isNewUser(email);
                    if (!existEmail) {
                        break;
                    }
                }

                if (existEmail) {
                    initializeUserFromLDAP();
                } else {
                    String mob = userValues.getMobile();
                    userValues.setIsNewUser(false);
                    for (String email : aliases) {
                        map = db.fetchUserValues(email, userValues.getMobile());

                        if (map.size() > 0) {
                            profile_values = (HashMap) signUpService.getUserValues(email, userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                            profile_values.put("mobile", mob);
                            initializeUserByProfile(map);
                            if (isIsEmailValidated) {
                                userValues.setMobile(mob);
                            }
                            employment_data = db.empCoordData((HashMap) map);
                            break;
                        }
                    }

                    if (!userValues.getMobile().contains("+")) {
                        userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
                    }
                }
            }
        } else {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles user is not ldap employee::::");

            userValues.setIsNICEmployee(false);
            userValues.setIsEmailValidated(false);

            aliases = new HashSet<>();
            aliases.add(userValues.getEmail());   // added to verify for both mail and mail aliases

            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }

            userValues.setAliases(aliases);

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles user is not ldap employee and aliases::::" + commaSeparatedAliases);

            if (db.isNewUser(userValues.getEmail())) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles user is not ldap employee and new user");

                for (String email : aliases) {
                    map = db.fetchUserValues(userValues.getEmail(), userValues.getMobile());
                    profile_values = (HashMap) signUpService.getUserValues(email, userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                }
                if (map.size() > 0) {
                    initializeUserByProfile(map);
                } else {
                    map.put("email", userValues.getEmail());
                    map.put("mobile", userValues.getMobile());
                    System.out.println("db.getStates()" + db.getStates());
                    userValues.getImpData().setStates(db.getStates());
                }
                employment_data = db.empCoordData((HashMap) map);
                userValues.setIsNewUser(true);
                if (!userValues.getMobile().contains("+")) {
                    userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside assign roles user is not ldap employee and old user");

                userValues.setIsNewUser(false);
                map = db.fetchUserValues(userValues.getEmail(), userValues.getMobile());
                employment_data = db.empCoordData((HashMap) map);
                if (map.size() > 0) {
                    initializeUserByProfile(map);
                    profile_values = (HashMap) signUpService.getUserValues(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                }

                if (!userValues.getMobile().contains("+")) {
                    userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
                }
            }
        }

        hashMap = fetchRoles(userValues.getAliases());
        rolesList = (List<String>) hashMap.get("rolesInList");
        rolesInString = (String) hashMap.get("rolesInString");
        System.out.println("Roles : " + rolesInString);
        userValues.setRole(rolesInString);
        userValues.setRoles(rolesList);
        userValues.setRolesAlreadyAssigned(true);
        userValues.setFormsAllowed(db.fetchAllowedFormsForAdmins(userValues.getAliases()));
        userValues.setPreviousRoleAtLogin(userValues.getRole());
        userValues.setPreviousRolesAtLogin(userValues.getRoles());
        userValues.setPreviousRolesServiceAtLogin(userValues.getRolesService());
        rolesInString = userValues.getRole();
        if (rolesInString.contains("sup") || rolesInString.contains("admin")) {
            userValues.setIndividualEmail(db.fetchIndividualEmailForSupporOrAdmin(userValues.getCountryCode() + userValues.getMobile()));
        }
        String comingFromUpdateMobile = "";
        if (sessionMap.get("comingFromUpdateMobile") == null) {
            String login_status = "";
            if ((userValues.getEmail().equalsIgnoreCase("support@gov.in") || userValues.getEmail().equalsIgnoreCase("support@nic.in") || userValues.getEmail().equalsIgnoreCase("support@nkn.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@nic.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@gov.in") || userValues.getEmail().equalsIgnoreCase("smssupport@nic.in") || userValues.getEmail().equalsIgnoreCase("smssupport@gov.in"))) {
                login_status = "Authentication Sucessfull with mobile no:" + userValues.getMobile();
            } else {
                login_status = "Authentication Sucessfull";
            }
            db.insertIntoAuditTable(userValues.getEmail(), userValues.getRole(), login_status);

        } else {
            comingFromUpdateMobile = "updateMobile";

        }
        String exist_in_kavach = "";
        if (sessionMap.get("exist_in_kavach") != null && sessionMap.get("exist_in_kavach").equals("true")) {
            exist_in_kavach = "true";
        }

        sessionMap.put("email", "");
        sessionMap.clear();
        sessionMap.invalidate();
        this.session = ActionContext.getContext().getSession();
        sessionMap.put("isGov", userValues.isIsEmailValidated());
        sessionMap.put("roleAssigned", userValues.isRolesAlreadyAssigned());
        sessionMap.put("uservalues", userValues);
        sessionMap.put("login", userValues.getEmail());
        sessionMap.put("whichModule", "online");
        sessionMap.put("update_without_oldmobile", "no");

        if (comingFromUpdateMobile.equals("updateMobile")) {
            sessionMap.put("comingFromUpdateMobile", comingFromUpdateMobile);
        }
        if (exist_in_kavach.equals("true")) {
            sessionMap.put("exist_in_kavach", exist_in_kavach);
        }

        System.out.println("inside login action profile_values" + profile_values);
        sessionMap.put("profile-values", profile_values);
        sessionMap.put("login_ip", ip);
        String co_data = "";
        Map co = new HashMap();
        int co_counter = 0;
        System.out.println("Counter:::: " + co_counter);
        for (String e : employment_data) {
            if (co_counter < 4) {
                co.clear();
                co = signUpService.getLdapValues(e);
                if (e.equals("support@nic.in") || e.equals("support@gov.in") || e.equals("smssupport@gov.in") || e.equals("support@nkn.in") || e.equals("smssupport@nic.in") || e.equals("vpnsupport@nic.in")) {
                    if (co_data.equals("")) {
                        co_data = "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
                    } else {
                        co_data = " or " + "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
                    }
                } else if (userValues.getUserOfficialData().getDepartment() != null && userValues.getUserOfficialData().getDepartment().toLowerCase().contains("nic")) {
                    co_data = "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
                } else if (co_data.equals("")) {
                    co_data = "your Coordinator/DA:&ensp;" + co.get("cn").toString() + " (" + e + " - " + co.get("ophone").toString() + ")";
                } else {
                    if (co_counter == 1 && co_counter < 2) {
                        co_data += ", ";
                    }
                    co_data += " or " + co.get("cn").toString() + " (" + e + " - " + co.get("ophone").toString() + ")";
                }
            } else {
                break;
            }
            co_counter++;
        }
        System.out.println("com.org.controller.LoginAction.assignRoles()::::::::::: " + co_data);
        if (co_data.equals("")) {
            co_data = "to Service Desk:&ensp;" + " (servicedesk.nic.in - <b>1800-111-555</b>)";
        }
        sessionMap.put("employment_data", co_data);
        System.out.println("UID###############" + userValues.getUid());
    }

    public String checkSupportEmail() {
        try {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside check support email::::");
            if (sessionMap.get("isGov") != null && (boolean) sessionMap.get("isGov") && sessionMap.get("loginTemp") != null && sessionMap.get("authFailed") == null) {
                System.out.println("inside check support email");
                if (!userValues.getMobile().contains("+")) {
                    userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
                }
                userValues.setExistInsupport(false);
                userValues.setExistInadmin(false);

                String returnString = db.checkSupportEmail(userValues.getMobile());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside check support email returnString::::" + returnString);

                if (!returnString.equals("invalid")) {

                    sessionMap.put("individual_loggedin_email", returnString);
                    userValues.setExistInsupport(true);
                } else {
                    sessionMap.put("individual_loggedin_email", userValues.getEmail());
                    userValues.setExistInsupport(false);
                }
                String returnString1 = db.checkAdminEmail(userValues.getMobile());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside check support email if user exist in admin and return string1::::" + returnString1);

                if (!returnString1.equals("invalid")) {
                    sessionMap.put("individual_loggedin_email", returnString);
                    userValues.setExistInadmin(true);
                } else {
                    userValues.setExistInadmin(false);
                    sessionMap.put("individual_loggedin_email", userValues.getEmail());
                }
            } else {
                userValues.setAuthenticated(false);
                userValues.setExistInsupport(false);
                userValues.setExistInadmin(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String verifyOtp() {
        String mobile = "";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside verify otp");

        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }
        Set<String> aliases = new HashSet<>();
        String s = "";
        if (userValues.isIsEmailValidated()) {
            if (sessionMap.get("isGov") != null && (boolean) sessionMap.get("isGov") && sessionMap.get("loginTemp") != null && sessionMap.get("authFailed") == null) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside verify otp`user is authenticated from ldap");

                aliases = ldap.fetchAliases(userValues.getEmail());
                Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());
                for (String aliase : aliasesNew) {
                    if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                        aliases.remove(aliase);
                    }
                }
                userValues.setAliases(aliases);
                int sizeOfAliases = aliases.size();
                String commaSeparatedAliases = "";
                if (sizeOfAliases > 1) {
                    for (String email : aliases) {
                        commaSeparatedAliases += "'" + email + "',";
                    }
                    commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                    userValues.setAliasesInString(commaSeparatedAliases);
                } else if (sizeOfAliases == 1) {
                    userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
                }
                if ((userValues.getEmail().equalsIgnoreCase("support@gov.in") || userValues.getEmail().equalsIgnoreCase("support@nic.in") || userValues.getEmail().equalsIgnoreCase("support@nkn.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@nic.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@gov.in") || userValues.getEmail().equalsIgnoreCase("smssupport@nic.in") || userValues.getEmail().equalsIgnoreCase("smssupport@gov.in"))) {
                    mobile = userValues.getMobile();
                }
                if (db.isUserBlockedDueToWrongOtpAttempts(aliases, mobile)) {
                    sessionMap.remove("captcha");
                    sessionMap.remove("loginTemp");
                    sessionMap.put("authFailed", "true");
                    sessionMap.put("Block_user", "true");
                    userValues.setBlocked(true);
                    return SUCCESS;
                }
                for (String email : aliases) {
                    s = db.verifyOTP(email, userValues.getMobile(), "", userValues.getMobileOtp(), false, userValues.isMobileOtpActive());
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "VErified OTP" + s);
                    if (!s.isEmpty()) {
                        userValues.setVerifiedOtp(s);
                        break;
                    }
                }
            }
        } else {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside verify otp`user is not authenticated from ldap");

            aliases.add(userValues.getEmail());
            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }
            userValues.setAliases(aliases);

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

            System.out.println("userValues.getEmailOtp" + userValues.getEmailOtp());
            System.out.println("userValues.getMobileOtp().isEmpty" + userValues.getMobileOtp().isEmpty());
            if (db.isUserBlockDuetoWrongEmailOtpOrMobileOtp(userValues.getAliases())) {
                sessionMap.remove("captcha");
                sessionMap.remove("loginTemp");
                sessionMap.put("authFailed", "true");
                sessionMap.put("Block_user", "true");
                userValues.setBlocked(true);
                return SUCCESS;
            }
            if (userValues.isIsNewUser()) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "VErified OTP if user is new");

                if ((userValues.getEmailOtp() != null || !userValues.getEmailOtp().isEmpty() || !userValues.getEmailOtp().equals("0")) || (userValues.getMobileOtp() == null || userValues.getMobileOtp().isEmpty() || userValues.getMobileOtp().equals("0"))) {
                    s = db.verifyOTP(userValues.getEmail(), userValues.getMobile(), userValues.getEmailOtp(), userValues.getMobileOtp(), userValues.isEmailOtpActive(), userValues.isMobileOtpActive());
                    userValues.setVerifiedOtp(s);
                } else if ((userValues.getEmailOtp() == null || userValues.getEmailOtp().isEmpty() || userValues.getEmailOtp().equals("0")) || (userValues.getMobileOtp() != null || !userValues.getMobileOtp().isEmpty() || !userValues.getMobileOtp().equals("0"))) {
                    s = db.verifyOTP(userValues.getEmail(), userValues.getMobile(), userValues.getEmailOtp(), userValues.getMobileOtp(), userValues.isEmailOtpActive(), userValues.isMobileOtpActive());
                    userValues.setVerifiedOtp(s);
                } else {
                    userValues.setVerifiedOtp("notmatch");
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "VErified OTP if user is new and verified otp response is::" + userValues.getVerifiedOtp());

            } else {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "VErified OTP if user is old and verified otp response is::" + userValues.getVerifiedOtp());

                if (userValues.getEmailOtp() == null || userValues.getEmailOtp().isEmpty() || userValues.getEmailOtp().equals("0") || userValues.getMobileOtp() == null || userValues.getMobileOtp().isEmpty() || userValues.getMobileOtp().equals("0")) {
                    userValues.setVerifiedOtp("notmatch");
                } else {
                    s = db.verifyOTP(userValues.getEmail(), userValues.getMobile(), userValues.getEmailOtp(), userValues.getMobileOtp(), userValues.isEmailOtpActive(), userValues.isMobileOtpActive());
                    userValues.setVerifiedOtp(s);
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "VErified OTP if user is old and verified otp response is::" + userValues.getVerifiedOtp());

            }

        }

        if (s.equals("") || s.equalsIgnoreCase("block")) {
            userValues.setVerifiedOtp("notmatch");
            if ((userValues.getEmail().equalsIgnoreCase("support@gov.in") || userValues.getEmail().equalsIgnoreCase("support@nic.in") || userValues.getEmail().equalsIgnoreCase("support@nkn.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@nic.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@gov.in") || userValues.getEmail().equalsIgnoreCase("smssupport@nic.in") || userValues.getEmail().equalsIgnoreCase("smssupport@gov.in"))) {
                db.insertIntoAuditTable(userValues.getEmail(), "", "Authentication fail due to incorrect otp on mobile:" + userValues.getMobile());
            } else {
                db.insertIntoAuditTable(userValues.getEmail(), "", "Authentication fail due to incorrect otp");
            }
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "verified otp response is::" + userValues.getVerifiedOtp());
        UserData user = null;
        boolean isRoleAssigned = false;
        if (sessionMap.get("uservalues") != null) {
            user = (UserData) sessionMap.get("uservalues");
            isRoleAssigned = (boolean) sessionMap.get("roleAssigned");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "USER From SESSION ::" + user);
        }

        if (userValues.isIsEmailValidated()) {
            if (sessionMap.get("isGov") != null && (boolean) sessionMap.get("isGov") && sessionMap.get("loginTemp") != null && sessionMap.get("authFailed") == null) {
                if (userValues.getVerifiedOtp().equals("mobileOnly")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "here is email validate true inside mobile only::");
                    userValues.setAuthenticated(true);
                    this.sessionMap = (SessionMap<String, Object>) ActionContext.getContext().getSession();
                    if (user != null) {
                        if (!isRoleAssigned) {
                            assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
                        }
                    } else {
                        assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
                    }

                } else if (userValues.getVerifiedOtp().equals("block")) {
                    userValues.setAuthenticated(false);
                } else {
                    userValues.setAuthenticated(false);
                }
            } else {
                userValues.setAuthenticated(false);
            }
        } else if (userValues.isIsNewUser()) {
            if (userValues.getVerifiedOtp().equals("mobileOnly") || userValues.getVerifiedOtp().equals("emailOnly") || userValues.getVerifiedOtp().equals("both")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "here is email validate false  for new user and verify otp is::" + userValues.getVerifiedOtp());

                userValues.setAuthenticated(true);
                this.sessionMap = (SessionMap<String, Object>) ActionContext.getContext().getSession();
                if (user != null) {

                    if (!isRoleAssigned) {

                        assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
                    }
                } else {

                    assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
                }
            } else {
                userValues.setAuthenticated(false);
            }
        } else {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "here is email validate false for old user and verify otp is equals to both::" + userValues.getVerifiedOtp());

            if (userValues.getVerifiedOtp().equals("both")) {
                userValues.setAuthenticated(true);
                this.sessionMap = (SessionMap<String, Object>) ActionContext.getContext().getSession();
                if (user != null) {

                    if (!isRoleAssigned) {

                        assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
                    } else {

                    }
                } else {

                    assignRoles(userValues.getEmail(), userValues.isIsEmailValidated(), userValues.isIsNewUser());
                }
                UserData u = (UserData) sessionMap.get("uservalues");
                System.out.println(u.getRoles());
            } else {
                userValues.setAuthenticated(false);
            }
        }

        // start, code added by pr on 6thsep19
        if (userValues != null && userValues.getRoles() != null) {
            ArrayList roless = (ArrayList) userValues.getRoles();
            LinkedList<String> roleLL = new LinkedList<String>();

            if (roless.contains("admin")) {
                roleLL.add("admin");
            }
            if (roless.contains("co")) {
                roleLL.add("co");
            }
            if (roless.contains("sup")) {
                roleLL.add("sup");
            }
            if (roless.contains("ca")) {
                roleLL.add("ca");
            }
            String topRole = "";
            if (roleLL != null && roleLL.size() > 0) {
                int i = 1;
                for (Object rol : roleLL) {
                    // fetch the counts for each role and save in session variables
                    if (i == 1) {
                        topRole = rol.toString();
                        sessionMap.put("toprole", topRole);
                        // start, code added by pr on 5thsep19
                        // call the counts function for the top role in the main thread
                        UserData userdata = (UserData) sessionMap.get("uservalues");
                        Set aliasSet = (Set) userdata.getAliases();
                        ArrayList<String> newAr = new ArrayList<>();
                        newAr.addAll(aliasSet);
                        ArrayList email = newAr;
                        HashMap finalMap = null;
                        ArrayList filterForms = null;
                        // set the session values based on role
                        //settingSessionCount(role, email, sessionMap);
                        finalMap = (HashMap) signUpService.settingSessionCount(topRole, email);
                        if (finalMap != null) {
                            if (finalMap.get("counts") != null) {
                                DAOGotMap = (HashMap) finalMap.get("counts");
                            }
                            if (finalMap.get("forms") != null) {
                                DAOGotFFMap = (HashMap) finalMap.get("forms");
                            }
                        }
                        sessionMap.putAll(DAOGotMap);
                        sessionMap.putAll(DAOGotFFMap);
                        sessionMap.put("co_list_show", coListData());
                        sessionMap.put("showDnsTrackerLink", findValidUser());
                        System.out.println("co_list_show:::: " + coListData());
                        System.out.println(" Main thread inside run for role  " + topRole + " DAOGotMap values are " + DAOGotMap + " DAOGotFFMap got from dao is " + DAOGotFFMap + " sessionMap values are " + sessionMap);
                        // end, code added by pr on 5thsep19
                    } else {
                        System.out.println(" roless size is greater than 0 rol value is " + rol + " nto dashboard ");
                        setRoleCountSession(rol.toString());

                    }
                    //}

                    i++;
                }
            }
        }
        System.out.println(" inside verifyotp function at the end, session values are " + sessionMap);

        // end, code added by pr on 6thsep19
        return SUCCESS;
    }

// below function added by pr on 6thsep19
    public void setRoleCountSession(String role) {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " ==  inside setRoleCountSession function ");

        //ArrayList email = null;
        if (DAOGotMap != null) {
            DAOGotMap.clear();
        }

        if (DAOGotFFMap != null) {
            DAOGotFFMap.clear();
        }

        UserData userdata = (UserData) sessionMap.get("uservalues");

        Set s = (Set) userdata.getAliases();
        System.out.println(" inside getEmailEquivalent get alias not null intialize set value is  " + s);
        ArrayList<String> newAr = new ArrayList<>();
        newAr.addAll(s);
        ArrayList email = newAr;

        System.out.println(" sessionmap values before calling run method " + sessionMap);

        try {

            int delay = 1; //msecs
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                public void run() {

                    HashMap finalMap = null;

                    ArrayList filterForms = null;

                    // set the session values based on role
                    //settingSessionCount(role, email, sessionMap);
                    System.out.println(" role is " + role + " email values are " + email);

                    //DAOGotMap = signUpService.settingSessionCount(role, email);
                    finalMap = (HashMap) signUpService.settingSessionCount(role, email);

                    System.out.println(" finalMap got for role " + role + " is " + finalMap);

                    if (finalMap != null) {
                        if (finalMap.get("counts") != null) {
                            DAOGotMap = (HashMap) finalMap.get("counts");
                        }

                        if (finalMap.get("forms") != null) {
                            //filterForms = (ArrayList)  finalMap.get("forms");

                            DAOGotFFMap = (HashMap) finalMap.get("forms");

                        }
                    }

                    System.out.println(" inside run for role  " + role + " DAOGotMap values are " + DAOGotMap + " DAOGotFFMap value got from dao is " + DAOGotFFMap);

                    sessionMap.putAll(DAOGotMap);

                    sessionMap.putAll(DAOGotFFMap);

                    System.out.println(" inside run for role  " + role + " DAOGotMap values are " + DAOGotMap + " sessionMap values are " + sessionMap);

                }

            }, delay);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle catch method exception is " + e.getMessage());

            e.printStackTrace();
        }

        System.out.println(" after TRY after settingadminvalue  session values are below : sessionMap values are " + sessionMap);

        for (String key : sessionMap.keySet()) {
            System.out.println(" 2 key is " + key + " value is " + sessionMap.get(key));
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside setRoleCountSession function ");

    }

    public String generateOtp() {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside generate otp");

        if (!userValues.isIsEmailValidated()) {
            Set<String> aliases = new HashSet<String>();
            aliases.add(userValues.getEmail());
            if (db.isUserBlockedDueToWrongPasswordAttempts(aliases, "")) {
                sessionMap.remove("captcha");
                sessionMap.remove("loginTemp");
                sessionMap.put("authFailed", "true");
                sessionMap.put("Block_user", "true");
                userValues.setBlocked(true);
                return SUCCESS;
            }
        }

        Set<String> aliases = new HashSet<>();
        if (userValues.isIsEmailValidated()) {
            aliases = ldap.fetchAliases(userValues.getEmail());
            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }
            userValues.setAliases(aliases);

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

        } else {
            aliases.add(userValues.getEmail());
            Set<String> aliasesNew = aliases.stream().collect(Collectors.toSet());

            for (String aliase : aliasesNew) {
                if (aliase.contains("+") || !aliase.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                    aliases.remove(aliase);
                }
            }
            userValues.setAliases(aliases);

            int sizeOfAliases = aliases.size();
            String commaSeparatedAliases = "";
            if (sizeOfAliases > 1) {
                for (String email : aliases) {
                    commaSeparatedAliases += "'" + email + "',";
                }
                commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
                userValues.setAliasesInString(commaSeparatedAliases);
            } else if (sizeOfAliases == 1) {
                userValues.setAliasesInString("'" + aliases.iterator().next() + "'");
            }

        }
        int i = 0;

        userValues.setEmailOtpActive(false);
        for (String email : aliases) {
            if (otp.isEmailOtpActive(email)) {
                userValues.setEmailOtpActive(true);
                break;
            }
        }

        userValues.setMobileOtpActive(otp.isMobileOtpActive(userValues.getMobile(), userValues.getCountryCode()));
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside generate otp mobile active" + userValues.isMobileOtpActive());

        if (!userValues.isEmailOtpActive() || !userValues.isMobileOtpActive()) {
            int random1 = 0;
            int random = 0;

            //Send SMS and Email notification (Both different)
            if (!userValues.getMobile().contains("+")) {
                userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
            }

            if (userValues.isIsEmailValidated() && !userValues.isMobileOtpActive()) {
                //Insert into DB Mobile value
                random = Random.random();
                if (db.insertNewOtp(random, random1, userValues.getMobile(), userValues.getEmail())) {
                    if (!userValues.isMobileOtpActive()) {

                        otp.sendOtpToMobile(random, userValues.getMobile());
                    }
                    System.out.println("OTP Succesfully Inserted");
                } else {
                    System.out.println("OTP could not be inserted");
                }
            } else if (!userValues.isIsEmailValidated()) {
                random = Random.random();
                random1 = Random.random();
                if (db.insertNewOtp(random, random1, userValues.getMobile(), userValues.getEmail())) {
                    if (!userValues.isMobileOtpActive()) {

                        otp.sendOtpToMobile(random, userValues.getMobile());
                    }

                    if (!userValues.isIsEmailValidated() && !userValues.isEmailOtpActive()) {

                        otp.sendOtpToEmail(random1, userValues.getEmail());
                    }
                    System.out.println("OTP Succesfully Inserted");
                } else {
                    System.out.println("OTP could not be inserted");
                }
            }
        }
        System.out.println("userValues.isMobileOtpActive()" + userValues.isMobileOtpActive());
        return SUCCESS;
    }

    public String resendMobile() {

        try {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside resend mobile otp::");

            System.out.println("inside resend mobile otp" + userValues.getMobile());
            String resendOtp = "";

            if (!userValues.getMobile().contains("+")) {
                userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
            }

            resendOtp = otp.resendMobile(userValues.getEmail(), userValues.getNew_mobile());
            if (!resendOtp.isEmpty() && !resendOtp.equals("0")) {
                otp.sendOtpToMobile(Integer.parseInt(resendOtp), userValues.getNew_mobile());
            }

            if (otp != null) {
                userValues.setResendmobile(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String resendEmailOtp() {
        String resendOtp = "";

        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }
        try {
            if (db.isNoOfOtpAttemptsExceeds(userValues.getEmail())) {
                System.out.println("no OfResendOtp Attempt Exceeded: ");
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside resend email otp::");
                resendOtp = otp.resendEmail(userValues.getEmail(), userValues.getMobile());
                if (!resendOtp.isEmpty() && !resendOtp.equals("0")) {
                    otp.sendOtpToEmail(Integer.parseInt(resendOtp), userValues.getEmail());
                }
                if (otp != null) {
                    int noOfEmailAttemptsLeft = 3 - db.fetchEmailResendAttempts(userValues.getEmail(), userValues.getMobile());
                    userValues.setNoOfEmailAttempts(noOfEmailAttemptsLeft);
                    userValues.setResendemail(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String checkldapmobile() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside checkldapmobile::");
        String capcode = (String) sessionMap.get("captcha");
        System.out.println("%%%%%%@@@@@@ in checkldapmobile userValues.getLoginCaptcha():" + userValues.getLoginCaptcha() + " | capcode:" + capcode);

        if (capcode.equals(userValues.getLoginCaptcha())) {
            userValues.setLoginCaptchaAuthenticated(true);
            if (!userValues.isIsEmailValidated()) {
                userValues.setIsNewUser(db.isNewUser(userValues.getEmail()));
                System.out.println("isNewUser2 = " + userValues.isIsNewUser());
                // If old user
                if (!userValues.isIsNewUser()) {
                    System.out.println("is not new user");
                    userValues.setMobile(db.getMobile(userValues.getEmail()));
                    // generateOtp();
                }
            }
            if (!userValues.getMobile().contains("+")) {
                System.out.println("mobile not contains::::::::::+");
                userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
            }

            userValues.setIsMobileInLdap(ldap.isMobileInLdap(userValues.getMobile()));
            Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(userValues.getMobile());
            userValues.setEmailCountAgainstMobile(emailsAgainstMobile.size());
            if (emailsAgainstMobile.size() > 0) {
                String emailAgainstMobileInString = emailsAgainstMobile.toString();
                userValues.setEmailAgainstMobileNumber(emailAgainstMobileInString);
            }
            System.out.println("emailsAgainstMobile" + emailsAgainstMobile);
            userValues.setMobileOtpActive(otp.isMobileOtpActive(userValues.getMobile(), ""));
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside checkldapmobile::" + userValues.isIsMobileInLdap());
        } else {
            userValues.setLoginCaptchaAuthenticated(false);
        }
        return SUCCESS;
    }

    private void initializeUserFromOAD(Map<String, String> map) {
        Map<String, Object> hashMap = new HashMap<>();
        List<String> rolesList = new ArrayList<>();
        String rolesInString = "";
        userValues.setIsNewUser(true);
        userValues.setEmail(userValues.getEmail());
        if (!map.get("mobile").equals("")) {
            userValues.setMobile(map.get("mobile"));
        }
        userValues.setName(map.get("name"));
        userValues.setTelephone(map.get("ophone"));
        userValues.getUserOfficialData().setDesignation(map.get("designation"));
        userValues.getUserOfficialData().setEmployeeCode(map.get("emp_code"));
        userValues.getUserOfficialData().setOfficeAddress(map.get("address"));
        userValues.getUserOfficialData().setPostingCity(map.get("city"));
        userValues.getUserOfficialData().setPostingState(map.get("add_state"));
        userValues.getUserOfficialData().setOfficePhone(map.get("ophone"));
        userValues.getHodData().setName(map.get("hod_name"));
        userValues.getHodData().setEmail(map.get("hod_email"));
        userValues.getHodData().setMobile(map.get("hod_mobile"));
        userValues.getHodData().setDesignation(map.get("ca_desig"));
        userValues.getHodData().setTelephone(map.get("hod_telephone"));
        userValues.getHodData().setGovEmployee(ldap.emailValidate(map.get("hod_email")));
        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }
        userValues.getImpData().setStates(db.getStates());
        userValues.getUserOfficialData().setOrganizationCategory(map.get("employment"));
        userValues.getUserOfficialData().setMinistry(map.get("ministry"));
        userValues.getUserOfficialData().setDepartment("department");

    }

    private void initializeUserByProfile(Map<String, String> map) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "initializeUserByProfile");
        Map<String, Object> hashMap = new HashMap<>();
        List<String> rolesList = new ArrayList<>();
        String rolesInString = "";
        String email = userValues.getEmail();
        userValues.setEmail(userValues.getEmail());
        userValues.setIsNewUser(false);
        if (!(email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("smssupport@gov.in") || email.equals("support@nkn.in") || email.equals("smssupport@nic.in") || email.equals("vpnsupport@nic.in"))) {
            userValues.setMobile(map.get("mobile"));
        }
        userValues.setName(map.get("name"));
        userValues.setTelephone(map.get("ophone"));
        userValues.getUserOfficialData().setDesignation(map.get("designation"));
        userValues.getUserOfficialData().setEmployeeCode(map.get("emp_code"));
        userValues.getUserOfficialData().setOfficeAddress(map.get("address"));
        userValues.getUserOfficialData().setPostingCity(map.get("city"));
        userValues.getUserOfficialData().setPostingState(map.get("add_state"));
        userValues.getUserOfficialData().setPincode(map.get("pin"));
        userValues.getUserOfficialData().setDepartment(map.get("department"));
        userValues.getUserOfficialData().setMinistry(map.get("ministry"));
        userValues.getUserOfficialData().setState(map.get("state"));
        userValues.getUserOfficialData().setOrganizationCategory(map.get("organization"));
        userValues.getUserOfficialData().setEmployment(map.get("employment"));
        userValues.getUserOfficialData().setOfficePhone(map.get("ophone"));
        userValues.getUserOfficialData().setRphone(map.get("rphone"));
        userValues.getHodData().setName(map.get("hod_name"));
        userValues.getHodData().setEmail(map.get("hod_email"));
        userValues.getHodData().setMobile(map.get("hod_mobile"));
        userValues.getHodData().setDesignation(map.get("ca_desig"));
        userValues.getHodData().setTelephone(map.get("hod_telephone"));
        userValues.getUserOfficialData().setUnder_sec_email(map.get("under_sec_email"));
        userValues.getUserOfficialData().setUnder_sec_name(map.get("under_sec_name"));
        userValues.getUserOfficialData().setUnder_sec_tel(map.get("under_sec_tel"));
        userValues.getUserOfficialData().setUnder_sec_mobile(map.get("under_sec_mobile"));
        userValues.getUserOfficialData().setUnder_sec_desig(map.get("under_sec_desig"));
        userValues.getHodData().setGovEmployee(ldap.emailValidate(map.get("hod_email")));
        userValues.getImpData().setStates(db.getStates());
        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }

    }

    public String verifyEmployeeCode() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "verifyEmployeeCode");

        UserData user = (UserData) sessionMap.get("uservalues");

        if (user.getEmail().equals(userValues.getEmail())) {
            userValuesFromDb = db.fetchUserValuesFromEmpCode(userValues.getUserOfficialData().getEmployeeCode());
            if (user.getMobile().equals(userValuesFromDb.get("mobile"))) {
                initializeUserFromOAD(userValuesFromDb);
                sessionMap.put("uservalues", userValues);
            } else {
                addFieldError("empcode", "Wrong empcode");
            }
        }
        return SUCCESS;
    }

    private void initializeUserFromLDAP() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "initializeUserFromLDAP");

        Map<String, Object> hashMap = new HashMap<>();
        List<String> rolesList = new ArrayList<>();
        String rolesInString = "";
        userValues.setAuthenticated(true);
        userValues.setIsEmailValidated(true);
        userValues.setIsNewUser(true);
        userValues.setEmail((String) userValuesFromLdap.get("email"));
        if (userValuesFromLdap.get("email").equals("support@nic.in") == false && userValuesFromLdap.get("email").equals("support@gov.in") == false && userValuesFromLdap.get("email").equals("support@nkn.in") == false && userValuesFromLdap.get("email").equals("smssupport@gov.in") == false && userValuesFromLdap.get("email").equals("smssupport@nic.in") == false && userValuesFromLdap.get("email").equals("vpnsupport@nic.in") == false) {
            userValues.setMobile((String) userValuesFromLdap.get("mobile"));
        }
        userValues.setName((String) userValuesFromLdap.get("cn"));
        userValues.getUserOfficialData().setEmployeeCode((String) userValuesFromLdap.get("emp_code"));
        userValues.getUserOfficialData().setDesignation((String) userValuesFromLdap.get("desig"));
        userValues.getUserOfficialData().setOfficeAddress((String) userValuesFromLdap.get("address"));
        userValues.getUserOfficialData().setPostingCity((String) userValuesFromLdap.get("city"));
        userValues.getUserOfficialData().setPostingState((String) userValuesFromLdap.get("state"));
        userValues.getUserOfficialData().setOrganizationCategory((String) userValuesFromLdap.get("employment"));
        userValues.getUserOfficialData().setMinistry((String) userValuesFromLdap.get("ministry"));
        userValues.getUserOfficialData().setDepartment((String) userValuesFromLdap.get("department"));
        userValues.getUserOfficialData().setOfficePhone((String) userValuesFromLdap.get("ophone"));
        userValues.setTelephone((String) userValuesFromLdap.get("rphone"));

        userValues.getImpData().setStates(db.getStates());
        if (!userValues.getMobile().contains("+")) {
            userValues.setMobile(userValues.getCountryCode() + userValues.getMobile());
        }

    }

    public UserData getUserValues() {
        return userValues;
    }

    public void setUserValues(UserData userValues) {
        this.userValues = userValues;
    }

    @Override
    public void setSession(Map<String, Object> map) {
        sessionMap = (SessionMap<String, Object>) map;
    }

    public Map<String, Object> fetchRoles(Set<String> aliases) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "getRoles");

        long startTime = System.currentTimeMillis();

        Set<String> roles = new HashSet<>();
        List<String> rolesList = new ArrayList<>();

        List<String> rolesServiceList = new ArrayList<>();// 24thoct19

        HashMap<String, Object> hashMap = new HashMap<>();
        String rolesInString = "";
        Set<String> rolesService = new HashSet<>(); //24thoct19
        if (aliases.size() > 10) {
            aliases = new HashSet<>();
            aliases.add(userValues.getEmail());
        }
        for (String email : aliases) {

            Set<String> roles1 = db.getRoles(email, userValues.getMobile(), roles, userValues.isIsEmailValidated());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " LOGINACTION email alias = " + email + "roles1 in getroles before is " + roles1);

            // start, added on 24thoct19
            if (roles1.contains("email_co")) {
                // add email related forms in
                rolesService.add(Constants.SINGLE_FORM_KEYWORD);
                rolesService.add(Constants.BULK_FORM_KEYWORD);
                rolesService.add(Constants.NKN_SINGLE_FORM_KEYWORD);
                rolesService.add(Constants.NKN_BULK_FORM_KEYWORD);
                rolesService.add(Constants.GEM_FORM_KEYWORD);
                rolesService.add(Constants.IMAP_FORM_KEYWORD);
                rolesService.add(Constants.MOB_FORM_KEYWORD);
                rolesService.add(Constants.LDAP_FORM_KEYWORD);
                rolesService.add(Constants.SMS_FORM_KEYWORD);
                rolesService.add(Constants.IP_FORM_KEYWORD);
                rolesService.add(Constants.DIST_FORM_KEYWORD);
                rolesService.add(Constants.BULKDIST_FORM_KEYWORD);
                rolesService.add(Constants.DNS_FORM_KEYWORD);
                rolesService.add(Constants.WIFI_FORM_KEYWORD);
                rolesService.add(Constants.WIFI_PORT_FORM_KEYWORD);
                rolesService.add(Constants.RELAY_FORM_KEYWORD);
                rolesService.add(Constants.CENTRAL_UTM_FORM_KEYWORD);
                rolesService.add(Constants.EMAILACTIVATE_FORM_KEYWORD);
                rolesService.add(Constants.EMAILDEACTIVATE_FORM_KEYWORD);
                rolesService.add(Constants.DOR_EXT_FORM_KEYWORD);
                rolesService.add(Constants.DAONBOARDING_FORM_KEYWORD);
            }

            if (roles1.contains("vpn_co")) {
                // add email related forms in
                rolesService.add(Constants.VPN_ADD_FORM_KEYWORD);
                rolesService.add(Constants.VPN_BULK_FORM_KEYWORD);
                rolesService.add(Constants.VPN_RENEW_FORM_KEYWORD);
                rolesService.add(Constants.VPN_SINGLE_FORM_KEYWORD);
                rolesService.add(Constants.VPN_SURRENDER_FORM_KEYWORD);
            }

            // end, added on 24thoct19
            for (String role : roles1) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " LOGINACTION role = " + role);
                roles.add(role);
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " LOGINACTION after alias " + email + rolesService);
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " LOGINACTION roles in getroles after is " + roles + " rolesService is " + rolesService);

        if (roles.contains("admin") || roles.contains("sup")) {
            boolean flag = false;
            for (String email : aliases) {
                boolean checkFormsAllowed = db.areFormsAllowedAsAdmin(email);
                System.out.println("checkFormsAllowed = " + checkFormsAllowed);
                if (checkFormsAllowed) {
                    flag = true;
                }
            }
            if (!flag) {
                roles.remove("admin");
                roles.remove("sup");
            }
        }

        int countOfVpnREquests = fetchCountForVpnRequests("", "pending", "all", aliases);
        if (countOfVpnREquests == 0 && !roles.contains("email_co")) {
            roles.remove("co");
            roles.remove("vpn_co");
        }
        if (roles.size() > 0) {
            for (String role : roles) {
                if (rolesInString.isEmpty()) {
                    rolesInString = role;
                } else {
                    rolesInString += "," + role;
                }
            }
        }
        rolesList.addAll(roles);

        rolesServiceList.clear();

        //System.out.println(" rolesService before putting in arraylist is " + rolesService);
        rolesServiceList.addAll(rolesService);// 24thoct19

        hashMap.put("rolesInString", rolesInString);
        hashMap.put("rolesInList", rolesList);
        hashMap.put("rolesServiceInList", rolesServiceList);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  LOGINACTION rolesInString = " + rolesInString);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " LOGINACTION rolesInList = " + rolesList);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " LOGINACTION rolesServiceInList = " + rolesServiceList);

        // start, added on 24thoct19 ,put here by mi on 31thdec19
        List<String> rolesListService = new ArrayList<>();

        rolesListService = (List<String>) hashMap.get("rolesServiceInList");

        userValues.setRolesService(rolesListService);

        // end, added on 24thoct19
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside get roles getRoles rolesInString" + rolesInString + " rolesServiceList " + rolesServiceList);

        //System.out.println(" rolesList inside loginaction is " + rolesList + " rolesServiceList is " + rolesServiceList);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside get roles getRoles rolesInString" + rolesInString);

        long endTime = System.currentTimeMillis();
        //System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        return hashMap;
    }

    public String resendOtpForLogin() {

        try {

            if (userValues.isIsNewUser()) {
                if (!userValues.getNew_mobile().contains("+")) {
                    userValues.setMobile(userValues.getCountryCode() + userValues.getNew_mobile());
                }

                String resendOtpForLogin = otp.resendOtpForLogin(userValues.getEmail(), userValues.getMobile());
                if (!resendOtpForLogin.isEmpty() && !resendOtpForLogin.equals("0")) {
                    otp.sendOtpToMobile(Integer.parseInt(resendOtpForLogin), userValues.getMobile());

                }

            } else {
                if (sessionMap.get("isGov") != null) {
                    if ((boolean) sessionMap.get("isGov")) {
                        if (sessionMap.get("loginTemp") != null && sessionMap.get("authFailed") == null) {
                            if (ldap.authenticate(userValues.getEmail(), userValues.getPassword())) {
                                String resendOtpForLogin = otp.resendOtpForLogin(userValues.getEmail(), ldap.fetchMobileFromLdap(userValues.getEmail()));
                                if (!resendOtpForLogin.isEmpty() && !resendOtpForLogin.equals("0")) {
                                    otp.sendOtpToMobile(Integer.parseInt(resendOtpForLogin), userValues.getMobile());

                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String Otpgen_newmobile() {

        //Added For mobile update
        // Inform inform = new Inform();
        //sessionMap.get("count").toString();
        Set<String> aliases = ldap.fetchAliases(userValues.getEmail());
        //EO added for mobile update
        System.out.println("11111111111111111111111111");
        String returnString = "";

        try {
            if (sessionMap.get("isGov") != null) {
                if ((boolean) sessionMap.get("isGov")) {
                    if (sessionMap.get("loginTemp") != null && sessionMap.get("authFailed") == null) {
                        if (ldap.authenticate(userValues.getEmail(), userValues.getPassword())) {
                            if (userValues.getType() != null && userValues.getType().equalsIgnoreCase("profile")) {
                                String registeredMobile = ldap.fetchMobileFromLdap(userValues.getEmail());
                                if (registeredMobile.length() == 13 && registeredMobile.startsWith("+91")) {
                                    userValues.setCountryCode("+91");
                                    registeredMobile = registeredMobile.substring(3);
                                } else if (registeredMobile.length() == 12 && registeredMobile.startsWith("91")) {
                                    userValues.setCountryCode("91");
                                    registeredMobile = registeredMobile.substring(2);
                                }
                                userValues.setNew_mobile(registeredMobile);

                                if ((userValues.getCountryCode().startsWith("+91") || userValues.getCountryCode().startsWith("91")) && (!userValues.getNew_mobile().matches("^[0-9]{10}$"))) {
                                    returnString = "moberr";
                                } else if ((!userValues.getCountryCode().startsWith("+91") || !userValues.getCountryCode().startsWith("91")) && !userValues.getNew_mobile().matches("^[0-9]{8,15}$")) {
                                    returnString = "moberr";
                                } else {
                                    sessionMap.put("newMobileNumber", userValues.getCountryCode() + userValues.getNew_mobile());
                                    userValues.setIsexistInKavach("false");
                                    for (String email1 : aliases) {
                                        if (db.existInKavach(email1).equals("exist_in_kavach")) {
                                            userValues.setIsexistInKavach("true");
                                            break;
                                        }
                                    }
                                    Map<String, String> map = new HashMap<String, String>();
                                    for (String email : aliases) {
                                        if (!email.isEmpty()) {
                                            map = db.fetchOtpForUpdateMobile(email, userValues.getNew_mobile());
                                            if (map.size() > 0) {
                                                break;
                                            }
                                        }
                                    }

                                    if (map.isEmpty() && userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpGenerate")) {
                                        entities.Random otpgenerate = new entities.Random();
                                        int otp_mob = otpgenerate.random();
                                        System.out.println("new otp_mobile for update mobile" + otp_mob);
                                        HashMap<String, Object> map_sms = new HashMap<>();
                                        map_sms.put("smsbody", "Your NEW OTP is :" + otp_mob + " to update mobile. NICSI");
                                        map_sms.put("mobile", userValues.getCountryCode() + userValues.getNew_mobile());
                                        map_sms.put("templateId", "1107161647440491832");
                                        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                        object.sendSmsOtpRabbitMq(map_sms);
                                        String mobile = ldap.fetchMobileFromLdap(userValues.getEmail());
                                        db.insertNewOtpInUpdateMobile(otp_mob, mobile, userValues.getCountryCode(), userValues.getNew_mobile(), userValues.getEmail(), userValues.getIsexistInKavach());
                                        sessionMap.put("new_otp", otp_mob);

                                        returnString = "newOtp";

                                    } else if (userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpResend")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + "else if email against mobile doesnt contains email ");

                                        if (map.get("otp_attempt") != null && Integer.parseInt(map.get("otp_attempt").toString()) <= 2) {
                                            HashMap<String, Object> map_sms = new HashMap<>();
                                            map_sms.put("smsbody", "Your NEW OTP is :" + map.get("newcode") + " to update mobile. NICSI");
                                            map_sms.put("mobile", userValues.getCountryCode() + userValues.getNew_mobile());
                                            map_sms.put("templateId", "1107161647440491832");
                                            NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                            object.sendSmsOtpRabbitMq(map_sms);
                                            db.updateOtpInUpdateMobile(userValues.getNew_mobile(), userValues.getEmail());
                                            sessionMap.put("new_otp", map.get("newcode"));
                                            sessionMap.put("ref_num", map.get("registration_no"));
                                            System.out.println("session otp_mobile for update mobile" + map.get("newcode"));
                                            returnString = "ResendOtp";

                                        } else {

                                            sessionMap.put("new_otp", map.get("newcode"));
                                            returnString = "ResendOtpLimitExceed";
                                        }

                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + "previous otp email against mobile doesnt contains email ");

                                        sessionMap.put("new_otp", map.get("newcode"));
                                        returnString = "previousOtp";
                                    }

                                }

                                //} else if (userValues.getType().equalsIgnoreCase("mobile")) {
                            } else if (userValues.getType() != null && userValues.getType().equalsIgnoreCase("mobile")) {
                                if (userValues.getNew_mobile().contains("XXXX")) {
                                    String registeredMobile = ldap.fetchMobileFromLdap(userValues.getEmail());
                                    if (registeredMobile.length() == 13 && registeredMobile.startsWith("+91")) {
                                        userValues.setCountryCode("+91");
                                        registeredMobile = registeredMobile.substring(3);
                                    } else if (registeredMobile.length() == 12 && registeredMobile.startsWith("91")) {
                                        userValues.setCountryCode("91");
                                        registeredMobile = registeredMobile.substring(2);
                                    }
                                    userValues.setNew_mobile(registeredMobile);
                                }

                                if ((userValues.getCountryCode().startsWith("+91") || userValues.getCountryCode().startsWith("91")) && (!userValues.getNew_mobile().matches("^[0-9]{10}$"))) {
                                    returnString = "moberr";
                                } else if ((!userValues.getCountryCode().startsWith("+91") || !userValues.getCountryCode().startsWith("91")) && !userValues.getNew_mobile().matches("^[0-9]{8,15}$")) {
                                    returnString = "moberr";
                                } else {
                                    Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(userValues.getCountryCode() + userValues.getNew_mobile());
                                    userValues.setEmailCountAgainstMobile(emailsAgainstMobile.size());
                                    Set<String> emailsAgainstMobileWithoutMask = ldap.fetchPrimaryEmailsAgainstMobileWithoutMask(userValues.getCountryCode() + userValues.getNew_mobile());

                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "emailsAgainstMobileWithoutMask::::::::" + emailsAgainstMobileWithoutMask);
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "emailsAgainstMobile::::::::" + emailsAgainstMobile);
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "emailsAgainstMobile size::::::::" + emailsAgainstMobile.size());
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "userValues.getEmail()::::::::" + userValues.getEmail());
//
//                                if (emailsAgainstMobileWithoutMask.toString().contains(userValues.getEmail())) {
//                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "email against mobile contains email");
//
//                                    returnString = "mobileInLdap";
//                                }
//                                else

                                    if (userValues.getEmail().toLowerCase().contains("@jcn.nic.in") && emailsAgainstMobile.size() > 4) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + "email against mobile size is greater then 3");
                                        returnString = emailsAgainstMobile.toString();
                                    } else {
                                        if (emailsAgainstMobile.size() == 3 || emailsAgainstMobile.size() > 3) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + "email against mobile size is greater then 3");
                                            returnString = emailsAgainstMobile.toString();
                                        } else {
                                            sessionMap.put("newMobileNumber", userValues.getCountryCode() + userValues.getNew_mobile());
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + "email against mobile doesnt contains email " + emailsAgainstMobileWithoutMask.toString() + ":::user email" + userValues.getEmail());

                                            userValues.setIsexistInKavach("false");
                                            for (String email1 : aliases) {
                                                if (db.existInKavach(email1).equals("exist_in_kavach")) {
                                                    userValues.setIsexistInKavach("true");
                                                    break;
                                                }
                                            }
                                            Map<String, String> map = new HashMap<String, String>();
                                            for (String email : aliases) {
                                                if (!email.isEmpty()) {
                                                    map = db.fetchOtpForUpdateMobile(email, userValues.getNew_mobile());
                                                    if (map.size() > 0) {
                                                        break;
                                                    }
                                                }
                                            }

                                            if (map.isEmpty() && userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpGenerate")) {
                                                entities.Random otpgenerate = new entities.Random();
                                                int otp_mob = otpgenerate.random();
                                                System.out.println("new otp_mobile for update mobile" + otp_mob);
                                                HashMap<String, Object> map_sms = new HashMap<>();
                                                map_sms.put("smsbody", "Your NEW OTP is :" + otp_mob + " to update mobile. NICSI");
                                                map_sms.put("mobile", userValues.getCountryCode() + userValues.getNew_mobile());
                                                map_sms.put("templateId", "1107161647440491832");
                                                NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                                object.sendSmsOtpRabbitMq(map_sms);
                                                String mobile = ldap.fetchMobileFromLdap(userValues.getEmail());
                                                db.insertNewOtpInUpdateMobile(otp_mob, mobile, userValues.getCountryCode(), userValues.getNew_mobile(), userValues.getEmail(), userValues.getIsexistInKavach());
                                                sessionMap.put("new_otp", otp_mob);

                                                returnString = "newOtp";

                                            } else if (userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpResend")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + "else if email against mobile doesnt contains email ");

                                                if (map.get("otp_attempt") != null && Integer.parseInt(map.get("otp_attempt").toString()) <= 2) {
                                                    HashMap<String, Object> map_sms = new HashMap<>();
                                                    map_sms.put("smsbody", "Your NEW OTP is :" + map.get("newcode") + " to update mobile. NICSI");
                                                    map_sms.put("mobile", userValues.getCountryCode() + userValues.getNew_mobile());
                                                    map_sms.put("templateId", "1107161647440491832");
                                                    NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                                    object.sendSmsOtpRabbitMq(map_sms);
                                                    db.updateOtpInUpdateMobile(userValues.getNew_mobile(), userValues.getEmail());
                                                    sessionMap.put("new_otp", map.get("newcode"));
                                                    sessionMap.put("ref_num", map.get("registration_no"));
                                                    System.out.println("session otp_mobile for update mobile" + map.get("newcode"));
                                                    returnString = "ResendOtp";

                                                } else {

                                                    sessionMap.put("new_otp", map.get("newcode"));
                                                    returnString = "ResendOtpLimitExceed";
                                                }

                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + "previous otp email against mobile doesnt contains email ");

                                                sessionMap.put("new_otp", map.get("newcode"));
                                                returnString = "previousOtp";
                                            }
                                        }
                                    }
                                }
                            } else {
                                try {

                                    if (!(userValues.getEmail().equalsIgnoreCase("support@gov.in") || userValues.getEmail().equalsIgnoreCase("support@nic.in") || userValues.getEmail().equalsIgnoreCase("support@nkn.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@nic.in") || userValues.getEmail().equalsIgnoreCase("vpnsupport@gov.in") || userValues.getEmail().equalsIgnoreCase("smssupport@nic.in") || userValues.getEmail().equalsIgnoreCase("smssupport@gov.in"))) {
                                        String registeredMobile = ldap.fetchMobileFromLdap(userValues.getEmail());
                                        if (registeredMobile.length() == 13 && registeredMobile.startsWith("+91")) {
                                            userValues.setCountryCode("+91");
                                            registeredMobile = registeredMobile.substring(3);
                                        } else if (registeredMobile.length() == 12 && registeredMobile.startsWith("91")) {
                                            userValues.setCountryCode("91");
                                            registeredMobile = registeredMobile.substring(2);
                                        }
                                        userValues.setNew_mobile(registeredMobile);
                                    }

                                    if ((userValues.getCountryCode().startsWith("+91") || userValues.getCountryCode().startsWith("91")) && (!userValues.getNew_mobile().matches("^[0-9]{10}$"))) {
                                        returnString = "moberr";
                                    } else if ((!userValues.getCountryCode().startsWith("+91") || !userValues.getCountryCode().startsWith("91")) && !userValues.getNew_mobile().matches("^[0-9]{8,15}$")) {
                                        returnString = "moberr";
                                    } else {
                                        sessionMap.put("newMobileNumber", userValues.getCountryCode() + userValues.getNew_mobile());

                                        Map<String, String> map = new HashMap<String, String>();
                                        for (String email : aliases) {
                                            if (!email.isEmpty()) {
                                                map = db.fetchOtpForCheckingResendAttempts(email, sessionMap.get("newMobileNumber").toString());
                                                if (map.size() > 0) {
                                                    break;
                                                }
                                            }
                                        }

                                        if (map.isEmpty() && userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpGenerate")) {
                                            entities.Random otpgenerate = new entities.Random();
                                            int otp_mob = otpgenerate.random();
                                            System.out.println("new otp_mobile to login into eForms" + otp_mob);

                                            Map<String, String> mapp = db.fetchOtp(sessionMap.get("newMobileNumber").toString(), userValues.getEmail());

                                            if (mapp.isEmpty()) {
                                                if (db.insertNewOtp(otp_mob, 0, sessionMap.get("newMobileNumber").toString(), userValues.getEmail())) {
                                                    otp.sendOtpToMobile(otp_mob, sessionMap.get("newMobileNumber").toString());
                                                    System.out.println("OTP Succesfully Inserted");
                                                    sessionMap.put("new_otp", otp_mob);
                                                } else {
                                                    System.out.println("OTP could not be inserted");
                                                }
                                            } else {
                                                otp.sendOtpToMobile(Integer.parseInt(mapp.get("otp_mobile")), sessionMap.get("newMobileNumber").toString());
                                                System.out.println("We did not insert a new row as previous OTP was still valid!!!");
                                                sessionMap.put("new_otp", Integer.parseInt(mapp.get("otp_mobile")));
                                            }
                                            returnString = "newOtp";
                                        } else if (userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpResend")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + "else if email against mobile doesnt contains email ");

                                            if (map.get("otp_attempt_mobile") != null && Integer.parseInt(map.get("otp_attempt_mobile").toString()) <= 2) {
                                                userValues.setNoOfMobileOtpAttempts(2 - Integer.parseInt(map.get("otp_attempt_mobile").toString()));
                                                otp.sendOtpToMobile(Integer.parseInt(map.get("otp_mobile")), sessionMap.get("newMobileNumber").toString());
                                                db.updateResendOtpAttempts(sessionMap.get("newMobileNumber").toString(), userValues.getEmail(), "mobile");
                                                sessionMap.put("new_otp", map.get("otp_mobile"));
                                                System.out.println("session otp_mobile for resending " + map.get("otp_mobile"));
                                                returnString = "ResendOtp";
                                                sessionMap.put("new_otp", Integer.parseInt(map.get("otp_mobile")));
                                            } else {
                                                sessionMap.put("new_otp", map.get("otp_mobile"));
                                                returnString = "ResendOtpLimitExceed";
                                            }
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + "previous otp email against mobile doesnt contains email ");
                                            sessionMap.put("new_otp", map.get("otp_mobile"));
                                            returnString = "previousOtp";
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            userValues.setAuthenticated(true);
                        } else {
                            System.out.println("inside else error");
                            return ERROR;
                        }
                        userValues.setReturnString(returnString);
                    }
                } else {
                    try {
                        if (!userValues.isIsNewUser()) {
                            userValues.setNew_mobile(db.getMobile(userValues.getEmail()).substring(3));
                        }
                        if ((userValues.getCountryCode().startsWith("+91") || userValues.getCountryCode().startsWith("91")) && (!userValues.getNew_mobile().matches("^[0-9]{10}$"))) {
                            returnString = "moberr";
                        } else if ((!userValues.getCountryCode().startsWith("+91") || !userValues.getCountryCode().startsWith("91")) && !userValues.getNew_mobile().matches("^[0-9]{8,15}$")) {
                            returnString = "moberr";
                        } else {
                            sessionMap.put("newMobileNumber", userValues.getCountryCode() + userValues.getNew_mobile());
                            Map<String, String> map = db.fetchOtpForCheckingResendAttempts(userValues.getEmail(), sessionMap.get("newMobileNumber").toString());

                            if (map.isEmpty() && userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpGenerate")) {
                                entities.Random otpgenerate = new entities.Random();
                                int otp_mob = otpgenerate.random();
                                System.out.println("new otp_mobile to login into eForms" + otp_mob);

                                Map<String, String> mapp = db.fetchOtp(sessionMap.get("newMobileNumber").toString(), userValues.getEmail());

                                if (mapp.isEmpty()) {
                                    if (db.insertNewOtp(otp_mob, 0, sessionMap.get("newMobileNumber").toString(), userValues.getEmail())) {
                                        otp.sendOtpToMobile(otp_mob, sessionMap.get("newMobileNumber").toString());
                                        System.out.println("OTP Succesfully Inserted");
                                        sessionMap.put("new_otp", otp_mob);
                                    } else {
                                        System.out.println("OTP could not be inserted");
                                    }
                                } else {
                                    otp.sendOtpToMobile(Integer.parseInt(mapp.get("otp_mobile")), sessionMap.get("newMobileNumber").toString());
                                    System.out.println("We did not insert a new row as previous OTP was still valid!!!");
                                    sessionMap.put("new_otp", Integer.parseInt(mapp.get("otp_mobile")));
                                }
                                returnString = "newOtp";
                            } else if (userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("otpResend")) {

                                System.out.println(ServletActionContext.getRequest().getSession().getId() + "else if email against mobile doesnt contains email ");

                                if (map.get("otp_attempt_mobile") != null && Integer.parseInt(map.get("otp_attempt_mobile").toString()) <= 2) {
                                    userValues.setNoOfMobileOtpAttempts(2 - Integer.parseInt(map.get("otp_attempt_mobile").toString()));
                                    otp.sendOtpToMobile(Integer.parseInt(map.get("otp_mobile")), sessionMap.get("newMobileNumber").toString());
                                    db.updateResendOtpAttempts(sessionMap.get("newMobileNumber").toString(), userValues.getEmail(), "mobile");
                                    sessionMap.put("new_otp", map.get("otp_mobile"));
                                    System.out.println("session otp_mobile for resending" + map.get("otp_mobile"));
                                    returnString = "ResendOtp";
                                    sessionMap.put("new_otp", Integer.parseInt(map.get("otp_mobile")));
                                } else {
                                    sessionMap.put("new_otp", map.get("otp_mobile"));
                                    returnString = "ResendOtpLimitExceed";
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + "previous otp email against mobile doesnt contains email ");
                                sessionMap.put("new_otp", map.get("otp_mobile"));
                                returnString = "previousOtp";
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                userValues.setReturnString(returnString);
            } else {
                userValues.setAuthenticated(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String verify_newmobile_code() {
        String returnString = "";

        //block for 3 failed attempts
        Set<String> aliases = ldap.fetchAliases(userValues.getEmail());
        String name = ldap.fetchNameFromLdap(userValues.getEmail());
        System.out.println("name in verify mobile code::::" + name);

        System.out.println("----" + userValues.getNicDateOfBirth() + " " + userValues.getNicDateOfRetirement() + " " + userValues.getDesignation() + " " + userValues.getDisplayName());
        for (String aliase : aliases) {
            if (db.fetchOtpSaveAttemptsOneHour(aliase, ip).equals("blocked")) {
                userValues.setBlocked(true);

                //sessionMap.put("authFailed", "true");fetchTrailMobileUpdateBlockOneHour
//                if (sessionMap.get("blocked_otp") != null) { //21-apr-2022
                Inform inform = new Inform();
                Map<String, Object> datasent = db.fetchTrailMobileUpdate(aliase);
                //String status = datasent.get("status").toString(); //21-apr-2022
//                    if (status.equals("failed")) { //21-apr-2022
                sessionMap.put("blocked_otp", userValues.getEmail());
                String from = "eforms@nic.in";
                String emailTo = userValues.getEmail();
                StringBuilder sb = new StringBuilder();

                String sub = "eForms : Multiple Login Attempts for updating Mobile";
                //String smsMessage="Dear user,  Your account has been tried to logged in from '"+ip+"' .If not you, Please contact NIC Service desk";
                sb.append("<p>Dear user,</p>  <p>Your email address was being used from <b>" + ip + "</b> to update mobile number.If its not you, Please contact NIC Service desk</p>");
                sb.append("<p>Regards,</p>");
                sb.append("eForms Team NIC");
                //inform.sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);// send sms is given below
                inform.sendToRabbit(from, emailTo, sb.toString(), sub, "", "", null, null, null);

                HashMap<String, Object> map_sms = new HashMap<>();
                map_sms.put("smsbody", "<p>Dear user,</p>  Your email address was being used from <b>" + ip + "</b> to update mobile number.If its not you, Please contact NIC Service desk. NICSI");
                map_sms.put("mobile", userValues.getNew_mobile());
                map_sms.put("templateId", "1107161647440491832");
                NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                object.sendSmsOtpRabbitMq(map_sms);

                //}
//                } // 21-apr-2022
                return SUCCESS;
            }
        }

        if (sessionMap.get("isGov") != null) {
            if ((boolean) sessionMap.get("isGov")) {
                if (sessionMap.get("loginTemp") != null && sessionMap.get("authFailed") == null) {
                    if (ldap.authenticate(userValues.getEmail(), userValues.getPassword())) {

                        if (sessionMap.get("newMobileNumber") != null && sessionMap.get("newMobileNumber").equals(userValues.getNew_mobile())) {

                            if (!userValues.getNewcode().equals(sessionMap.get("new_otp").toString())) {
                                //insert into otp_save_mobile_attempts
                                db.insertOtpAttempts(userValues.getEmail(), userValues.getMobile(), userValues.getNewcode(), ip, "failed");
                                //eo otp_save_mobile_attempts
                                returnString = "newOtpNotMatch";
                                System.out.println("new otp not matchhhhhhhhhhhhhhhhhhhhh");

                            } else if ((userValues.getNewcode().equals(sessionMap.get("new_otp").toString()))) {
                                //String email = userValues.getEmail();
                                boolean isNewUser = userValues.isIsNewUser();
                                System.out.println("isNewUser::::::::" + isNewUser);

                                /* ends here,it will be remove when update-mobile go live*/
                                //String mail = userValues.getEmail();
                                Map userValues1 = new HashMap();
                                for (String email1 : aliases) {
                                    userValues1 = (HashMap) signUpService.getUserValues(email1, userValues.isIsEmailValidated(), userValues.isIsNICEmployee());
                                    if (!userValues1.containsKey("error")) {
                                        break;
                                    }
                                }
                                if (sessionMap.get("comingFromUpdateMobile") != null && sessionMap.get("comingFromUpdateMobile").equals("updateMobile")) {
                                    userValues.setIsexistInKavach("false");
                                    for (String email1 : aliases) {
                                        if (db.existInKavach(email1).equals("exist_in_kavach")) {
                                            userValues.setIsexistInKavach("true");
                                            sessionMap.put("exist_in_kavach", "true");
                                            break;
                                        }
                                    }

                                    for (String email : aliases) {
                                        if (!email.isEmpty()) {
                                            Map<String, String> map = ldap.isUserNICEmployee(userValues.getEmail());
                                            if (db.checkBoForUpdateMobile(map.get("bo")).equals("You_Are_NOT_Eligible")) {
                                                userValues.setIsexistInKavach("true");
                                                sessionMap.put("exist_in_kavach", "true");
                                                break;

                                            }
                                        }
                                    }

                                }
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + "before calling assign roles  ");

                                if (sessionMap.get("comingFromUpdateMobile") == null || (sessionMap.get("exist_in_kavach") != null && sessionMap.get("exist_in_kavach").equals("true"))) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "comingFromUpdateMobile is null  ");
                                    assignRoles(userValues.getEmail(), true, isNewUser);
                                }
                                sessionMap.put("profile-values", userValues1);
                                if (isNewUser) {
                                    returnString = "newuser";
                                    sessionMap.put("profile_present", "false");
                                    //user.setMobile(userValues.getNew_mobile());
                                    //break;
                                } else {
                                    returnString = "olduser";
                                    sessionMap.put("profile_present", "true");
                                }
                                // if (sessionMap.get("comingFrom") != null && sessionMap.get("comingFrom").equals("updateMobile")) {
                                sessionMap.put("update_without_oldmobile", "yes");
                                sessionMap.put("mobileNumber", userValues.getNew_mobile());
                                userValues.setName(name);
                                //}
                                ;
                            } else {
                                returnString = "newOtpNotMatch";
                            }
                            userValues.setAuthenticated(true);
                        } else {
                            userValues.setAuthenticated(false);
                            //returnString = "newOtpNotMatch";
                        }
                    } else {
                        userValues.setAuthenticated(false);
                    }

                }
            }

        }
        userValues.setReturnString(returnString);
        return SUCCESS;
    }

    private String coListData() {
        Connection conSlave = null;
        UserData userdata = (UserData) sessionMap.get("uservalues");
        String aliasescomma = userdata.getAliasesInString();
        //List<String> coListData = new ArrayList<>();

        PreparedStatement psco = null;
        ResultSet rsco = null;
        if (userdata.isIsNICEmployee()) {
            return "Data Found";
        } else {
            try {
                String sql = "SELECT count(DISTINCT `emp_coord_email`) FROM `employment_coordinator` WHERE emp_coord_email IN (" + aliasescomma + ")";
//                conSlave = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                psco = conSlave.prepareStatement(sql);
                rsco = psco.executeQuery();

                if (rsco.next()) {
                    if (rsco.getInt(1) > 0) {
                        return "Data Found";
                    } else {
                        psco = null;
                        rsco = null;
                        sql = "SELECT * FROM `coordinator_list_status` WHERE email_id IN (" + aliasescomma + ") AND `status` = 'a'";
//                        conSlave = DbConnection.getConnection();
                        conSlave = DbConnection.getSlaveConnection(); //29dec2021
                        psco = conSlave.prepareStatement(sql);
                        rsco = psco.executeQuery();

                        if (rsco.next()) {
                            if (rsco.getString("allow_ip") == null || rsco.getString("allow_ip") == ip || rsco.getString("allow_ip").equals("")) {
                                return "Data Found";
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Data Not Found";
        }
    }

    public String findValidUser() {
        String flag = "notshowlink";
        Connection conSlave = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserData userdata = (UserData) sessionMap.get("uservalues");
        String aliasescomma = userdata.getAliasesInString();
        System.out.println("com.org.dao.DnsTeamDao.findValidUser() LoginAliases:::: " + aliasescomma);
        try {
            conSlave = DbConnection.getSlaveConnection();
            String sql = "SELECT count(*) FROM domain_tracker WHERE authorize_email IN(" + aliasescomma + ") and allow_domain_tracker = 'y'";
            ps = conSlave.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    flag = "showlink";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("com.org.controller.LoginAction.findValidUser() FLAG::: " + flag);
        return flag;
    }

    public String sendOtp() {
        String returnString = "";
        try {

            Map<String, String> map = db.fetchOtpForOnBehalfEmail(userValues.getOn_behalf_email(), userValues.getEmail());
            String mobile = ldap.fetchMobileFromLdap(userValues.getOn_behalf_email());

            if (ldap.emailValidate(userValues.getOn_behalf_email())) {
                Set<String> aliases = ldap.fetchAliases(userValues.getEmail());
                for (String aliase : aliases) {
                    if (aliase.contains(userValues.getOn_behalf_email())) {
                        returnString = "same_as_user";
                        break;
                    }
                }
                if (!returnString.equals("same_as_user")) {
                    if (map.isEmpty() && userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("generate")) {
                        entities.Random otpgenerate = new entities.Random();
                        int otp_mob = otpgenerate.random();
                        System.out.println("new otp_mobile for update mobile" + otp_mob);
                        HashMap<String, Object> map_sms = new HashMap<>();
                        map_sms.put("smsbody", "Your NEW OTP is :" + otp_mob + " to update mobile. NICSI");
                        map_sms.put("mobile", mobile);
                        map_sms.put("templateId", "1107161647440491832");
                        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                        object.sendSmsOtpRabbitMq(map_sms);
                        db.updateOtpOnBehalf(otp_mob, userValues.getOn_behalf_email(), userValues.getEmail());
                        sessionMap.put("on_behalf_otp", otp_mob);
                        returnString = "newOtp";
                        //sessionMap.put("ref_num", map.get("registration_no"));

                    } else if (userValues.getOtp_generate_or_resend() != null && userValues.getOtp_generate_or_resend().equals("resend")) {
                        if (map.get("attempt_login_on_behalf") != null && Integer.parseInt(map.get("attempt_login_on_behalf")) <= 2) {
                            HashMap<String, Object> map_sms = new HashMap<>();
                            map_sms.put("smsbody", "Your NEW OTP is :" + map.get("on_behalf_otp") + " to update mobile. NICSI");
                            map_sms.put("mobile", mobile);
                            map_sms.put("templateId", "1107161647440491832");
                            NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                            object.sendSmsOtpRabbitMq(map_sms);
                            db.updateOtpOnBehalf(Integer.parseInt(map.get("on_behalf_otp").toString()), userValues.getOn_behalf_email(), userValues.getEmail());
                            sessionMap.put("on_behalf_otp", map.get("on_behalf_otp"));
                            System.out.println("session otp_mobile for update mobile" + map.get("on_behalf_otp"));
                            returnString = "ResendOtp";
                            //sessionMap.put("ref_num", map.get("registration_no"));
                        } else {

                            sessionMap.put("on_behalf_otp", map.get("on_behalf_otp"));
                            returnString = "ResendOtpLimitExceed";
                            //sessionMap.put("ref_num", map.get("registration_no"));
                        }
                    } else {
                        sessionMap.put("on_behalf_otp", map.get("on_behalf_otp"));
                        returnString = "previousOtp";
                        //sessionMap.put("ref_num", map.get("registration_no"));
                    }
                }
            } else {
                returnString = "nongov";
            }
            userValues.setReturnString(returnString);
            userValues.setMobile(mobile);
        } catch (Exception e) {
            e.printStackTrace();

        }
        System.out.println("returnString@@@@@@@" + returnString);

        return SUCCESS;
    }

    public int fetchCountForVpnRequests(String search, String type, String formName, Set<String> aliases) {
        long startTime = System.currentTimeMillis();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        int cnt = 0;
        pendingStr = "coordinator_pending";
        rejectStr = "coordinator_rejected";
        field = "coordinator_email";
        tot_field = "coordinator_email";
        pendingDate = "coordinator_datetime";
        Connection conSlave = null;
        ResultSet rs = null;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT count(*) FROM final_audit_track WHERE 1 ";
            int i = 1;
            for (String email : aliases) {
                if (i == 1) {
                    qry += " and ( ";
                } else {
                    qry += " OR ";
                }
                qry += "(( status = '" + pendingStr + "' and to_email  = '" + email.toString().trim() + "' ) || (coordinator_email = '" + email.toString().trim() + "' and co_id = 0 ))";
                ++i;
            }

            List<String> aliasesAsList = new ArrayList<>(aliases);
            Set<String> coordsIds = db.fetchCoIds(aliasesAsList);
            String sb = "";
            if (!coordsIds.isEmpty()) {
                for (String coordsId : coordsIds) {
                    sb += "'" + coordsId + "',";
                }
                // 24-feb-2022  COUNT QUERY EXCEPTION You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near ')))' at line 1
                sb = sb.replaceAll("\\s*,\\s*$", "");
                qry += " or ( status = '" + pendingStr + "' and co_id in (" + sb + "))";
            }
            qry += ") and registration_no like 'vpn%'";

            ps = conSlave.prepareStatement(qry);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY is " + ps);

            res1 = ps.executeQuery();
            if (res1.next()) {
                cnt = res1.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY EXCEPTION " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 1: " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 2: " + e.getMessage());

                }
            }
        }
        long endTime = System.currentTimeMillis();
        //System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  end of getcount function ");
        return cnt;
    }

    public String checkRefreshFlag() {
        setRefreshFlag((boolean) sessionMap.get("refresh"));
        return SUCCESS;
    }

    public String fetchSwitchUrl() {
        System.out.println("SESSION VALUES ::: "+sessionMap);
        UserData udata = (UserData) sessionMap.get("uservalues");
        if (udata == null || udata.getRoles() == null) {
            return "logout";
        }
        if (udata.getRoles().contains("admin")) {
            setSwitchUrl("showLinkData?adminValue=admin");
        } else if (udata.getRoles().contains("co")) {
            setSwitchUrl("showLinkData?adminValue=co");
        } else if (udata.getRoles().contains("sup")) {
            setSwitchUrl("showLinkData?adminValue=sup");
        } else if (udata.getRoles().contains("ca")) {
            setSwitchUrl("showLinkData?adminValue=ca");
        } else if (udata.getRoles().contains("user")) {
            setSwitchUrl("showUserData");
        }
        return SUCCESS;
    }
}
