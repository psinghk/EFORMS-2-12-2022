package com.org.controller;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.SignUpService;
import com.org.utility.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.HEAD;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import rabbitmq.NotifyThrouhRabbitMQ;

/**
 *
 * @author Nancy
 */
public class Signup extends ActionSupport implements SessionAware {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
    Map session;
    String mobile;
    String email;
    String password;
    String returnString;
    String otpreq;
    String otpemail;
    String new_email;
    String new_profile_email;
    String new_mobile;
    String check_user_type;
    String newuser;
    String whichform;
    String view;
    String dbreturn;
    ArrayList role = new ArrayList();
    Map userValues = new HashMap();
    Map hodValues = new HashMap();
    SignUpService signup = new SignUpService();
    String hogValues;
    ArrayList state_name = new ArrayList();
    String country_code;
    String oldcode;
    String newcode, user_alt_address;
    private Ldap ldap = null;
    List list = new ArrayList();
    private Database db = null;

    public Signup() {
        ldap = new Ldap();
        role = new ArrayList();
        userValues = new HashMap();
        hodValues = new HashMap();
        signup = new SignUpService();
        state_name = new ArrayList();
        list = new ArrayList();
        if (ldap == null) {
            ldap = new Ldap();
        }
        if (role == null) {
            role = new ArrayList();
        }
        if (userValues == null) {
            userValues = new HashMap();
        }
        if (hodValues == null) {
            hodValues = new HashMap();
        }
        if (signup == null) {
            signup = new SignUpService();
        }
        if (state_name == null) {
            state_name = new ArrayList();
        }
        if (list == null) {
            list = new ArrayList();
        }
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    List statecolist = new ArrayList();

    public List getStatecolist() {
        return statecolist;
    }

    public void setStatecolist(List statecolist) {
        this.statecolist = statecolist;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = Jsoup.parse(mobile).text();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Jsoup.parse(email).text();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Jsoup.parse(password).text();
    }

    public String getOtpreq() {
        return otpreq;
    }

    public void setOtpreq(String otpreq) {
        this.otpreq = Jsoup.parse(otpreq).text();
    }

    public String getOtpemail() {
        return otpemail;
    }

    public void setOtpemail(String otpemail) {
        this.otpemail = Jsoup.parse(otpemail).text();
    }

    public String getNew_email() {
        return new_email;
    }

    public void setNew_email(String new_email) {
        this.new_email = Jsoup.parse(new_email).text();
    }

    public String getNew_profile_email() {
        return new_profile_email;
    }

    public void setNew_profile_email(String new_profile_email) {
        this.new_profile_email = Jsoup.parse(new_profile_email).text();
    }

    public String getNew_mobile() {
        return new_mobile;
    }

    public void setNew_mobile(String new_mobile) {
        this.new_mobile = Jsoup.parse(new_mobile).text();
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = Jsoup.parse(returnString).text();
    }

    public ArrayList getRole() {
        return role;
    }

    public void setRole(ArrayList role) {
        this.role = role;
    }

    public Map getUserValues() {
        return userValues;
    }

    public void setUserValues(Map userValues) {
        this.userValues = userValues;
    }

    public String getCheck_user_type() {
        return check_user_type;
    }

    public void setCheck_user_type(String check_user_type) {
        this.check_user_type = Jsoup.parse(check_user_type).text();
    }

    public String getNewuser() {
        return newuser;
    }

    public void setNewuser(String newuser) {
        this.newuser = Jsoup.parse(newuser).text();
    }

    public String getUser_alt_address() {
        return user_alt_address;
    }

    public void setUser_alt_address(String user_alt_address) {
        this.user_alt_address = user_alt_address;
    }

    public String getWhichform() {
        return whichform;
    }

    public void setWhichform(String whichform) {
        this.whichform = Jsoup.parse(whichform).text();
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = Jsoup.parse(view).text();
    }

    public Map getHodValues() {
        return hodValues;
    }

    public void setHodValues(Map hodValues) {
        this.hodValues = hodValues;
    }

    public String getDbreturn() {
        return dbreturn;
    }

    public void setDbreturn(String dbreturn) {
        this.dbreturn = Jsoup.parse(dbreturn).text();
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getOldcode() {
        return oldcode;
    }

    public void setOldcode(String oldcode) {
        this.oldcode = oldcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String execute() throws Exception {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "IN MODULE: " + view);

        session.put("user_role", view);

        return SUCCESS;
    }

    public String checkProfile() {
        System.out.println("session in before profile values::::" + session.get("profile-values"));
        db = new Database();
        boolean flag = false;
        UserData userdata = (UserData) session.get("uservalues");
        System.out.println("UID###############" + userdata.getUid() + "name@@@@@" + userdata.getName() + userdata.getHodData().getEmail() + "hod telephone" + userdata.getHodData().getTelephone() + "userdata.isIsNewUserforService()" + userdata.isIsNewUserforService());
        whichform = Jsoup.parse(whichform).text();
        if (whichform.matches("^[a-zA-Z.]{2,20}$")) {
            Set<String> aliases = userdata.getAliases();
            for (String useremail : aliases) {
                if (!useremail.isEmpty()) {
                    if (!db.isNewUser(useremail)) {
                        userdata.setIsNewUser(false);
                        break;
                    }
                }
            }

////<<<<<<< HEAD
            System.out.println("HOD EMAIL by MI" + userdata.getHodData().getName());
            System.out.println("user is in check profile::::" + userdata.isIsNewUser());
            fetchhmValue(userdata);
            flag = flagForService(userdata, flag);
            System.out.println("After HOD EMAIL by MI" + userdata.getHodData().getName());
            System.out.println("session in after profile values::::" + session.get("profile-values"));
////=======
//        
//            boolean flag = false;
//            if (whichform.equals("dlist")) {
//                view = "Distribution_registration";
//                flag = true;
//            }  else if(whichform.equals("bulkdlist")){
//               view =  "bulk_distribution_registration";
//               flag = true;
//            } else if (whichform.equals("dns")) {
//                view = "Dns_registration";
//                flag = true;
//            } else if (whichform.equals("email")) {
//                System.out.println("inside email::::::::::::");
//                session.remove("prEmployment");
//                view = "Email_registration";
//                flag = true;
//            } else if (whichform.equals("imap")) {
//                view = "ImapPop_registration";
//                flag = true;
//            } else if (whichform.equals("ip")) {
//                view = "Change_ip";
//                flag = true;
//            } else if (whichform.equals("ldap")) {
//                view = "Ldap_registration";
//                flag = true;
//            } else if (whichform.equals("mobile")) {
//                view = "Mobile_registration";
//                flag = true;
//            } else if (whichform.equals("nkn")) {
//                view = "Nkn_registration";
//                flag = true;
//            } else if (whichform.equals("relay")) {
//                view = "Relay_registration";
//                flag = true;
//            } else if (whichform.equals("sms")) {
//                view = "Sms_Registration";
//                session.put("flag", 0);
//                flag = true;
//            } else if (whichform.equals("wifi")) {
//                view = "Wifi_registration";
//                flag = true;
//	    } else if (whichform.equals("wifiport")) {
//                view = "Wifiport_registration";
//                flag = true;
//            } else if (this.whichform.equals("utm")) {
//                System.out.println("equal::::::::::");
//                this.view = "Firewall_registration";
//                flag = true;
//
//            } else if (whichform.equals("vpn")) {
//                view = "Vpn_registration";
//                //view = "https://vpn.nic.in";
//                flag = true;
//                try {
//                    if ((userValues.get("user_employment").toString().equalsIgnoreCase("state"))) {
//                        statecolist = signup.getStateCoordinator(userValues);
//                        session.put("vpn_state_coo_select", statecolist);
//                        session.put("vpn_coo_select", "");
//                    } else if ((!userValues.get("user_employment").toString().equalsIgnoreCase("state")) && (!userValues.get("state").toString().equalsIgnoreCase("delhi"))) {
//                        list = signup.getCentralMinistry(userValues);
//                        System.out.println("list::::::::" + list);
//                        session.put("vpn_coo_select", list);
//                        statecolist = signup.getStateCoordinator(userValues);
//                        System.out.println("statecolist::::::::" + statecolist);
//                        session.put("vpn_state_coo_select", statecolist);
//                    } else if ((!userValues.get("user_employment").toString().equalsIgnoreCase("state")) && (userValues.get("state").toString().equalsIgnoreCase("delhi"))) {
//                        list = signup.getCentralMinistry(userValues);
//                        session.put("vpn_coo_select", list);
//                        session.put("vpn_state_coo_select", "");
//
//                    }
//>>>>>>> rahul_bulkdlist

            System.out.println("flag::::::::" + flag);
            if (flag) {

            } else {
                whichform = ESAPI.encoder().encodeForHTML(whichform);
            }
        } else {
            whichform = ESAPI.encoder().encodeForHTML(whichform);
        }

        return SUCCESS;
    }

    /*---------------------------------------------------------------------Update mobile----------------------------------------------------------------------------*/
    public String OtpGenerate_updatemobile() {
        try {
            UserData userdata = (UserData) session.get("uservalues");
            mobile = userdata.getMobile();
            entities.Random otpgenerate = new entities.Random();
            int otp_mob = otpgenerate.random();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "OLd OTP Code: " + otp_mob);

            System.out.println("otp_mobile for update mobile" + otp_mob);
            HashMap<String, Object> map_sms = new HashMap<>();
            map_sms.put("smsbody", "Your eForms OTP is :" + otp_mob + " to update mobile. NICSI");
            map_sms.put("mobile", mobile);
            map_sms.put("templateId", "1107161647437800614");
            NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
            object.sendSmsOtpRabbitMq(map_sms);
            session.put("old_otp", otp_mob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    
    public String OtpGenerate_newmobile() {
        
        try {
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("inside generate otp new mobile" + new_mobile);
            
            if ((!new_mobile.startsWith("+91")) && (!new_mobile.matches("^[0-9]{10}$"))) {

                returnString = "moberr";
            } else {
                if ((new_mobile.startsWith("+91") || new_mobile.startsWith("91")) && (!new_mobile.matches("^[+0-9]{13}$"))) {

                    returnString = "moberr";

                } else if ((!new_mobile.startsWith("+91") || !new_mobile.startsWith("91")) && !new_mobile.matches("^[+0-9]{8,15}$")) {

                    returnString = "moberr";

                } else {
                    System.out.println("555555555555");

                    if ((!new_mobile.contains("+91")) && (country_code.equals("+91")) && (new_mobile.matches("^[+0-9]{10}$"))) {
                        new_mobile = country_code + new_mobile;
                        if (session.get("new_mobile_for_update") == null) {
                            session.put("new_mobile_for_update", new_mobile);
                        }
                    }
                  
                   
                    Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(new_mobile);
                    System.out.println("emailsAgainstMobile"+emailsAgainstMobile);
                    if (emailsAgainstMobile.size() > 3) {
                        String emailAgainstMobileInString = emailsAgainstMobile.toString();
                        returnString = "There is already an email address (" + emailAgainstMobileInString + ") associated with this mobile number so the same mobile number can't be updated";

                        //text change 20thmay2020
                    }  else {
                        int otp_mob = 0;
                        if (session.get("new_otp") != null && !session.get("new_otp").equals("")) {
                            if (session.get("new_mobile_for_update") != null && !session.get("new_mobile_for_update").equals(new_mobile)) {
                                entities.Random otpgenerate = new entities.Random();
                                otp_mob = otpgenerate.random();
                                System.out.println("new otp_mobile for update mobile" + otp_mob);
                                HashMap<String, Object> map_sms = new HashMap<>();
                                map_sms.put("smsbody", "Your NEW OTP is :" + otp_mob + " to update mobile. NICSI");
                                map_sms.put("mobile", new_mobile);
                                map_sms.put("templateId", "1107161647440491832");
                                NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                object.sendSmsOtpRabbitMq(map_sms);
                                if (otp_mob != 0) {
                                    session.put("new_otp", otp_mob);
                                }

                            } else {
                                if (!session.get("new_otp_store").equals(session.get("new_otp"))) {
                                    entities.Random otpgenerate = new entities.Random();
                                    otp_mob = otpgenerate.random();
                                    System.out.println("new otp_mobile for update mobile" + otp_mob);
                                    HashMap<String, Object> map_sms = new HashMap<>();
                                    map_sms.put("smsbody", "Your NEW OTP is :" + otp_mob + " to update mobile. NICSI");
                                    map_sms.put("mobile", new_mobile);
                                    map_sms.put("templateId", "1107161647440491832");
                                    NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                                    object.sendSmsOtpRabbitMq(map_sms);
                                    if (otp_mob != 0) {
                                        session.put("new_otp", otp_mob);
                                    }

                                } else {
                                    otp_mob = Integer.parseInt(session.get("new_otp").toString());
                                    session.put("new_otp", otp_mob);
                                }
                            }
                        } else {
                            entities.Random otpgenerate = new entities.Random();
                            otp_mob = otpgenerate.random();
                            System.out.println("new otp_mobile for update mobile" + otp_mob);
                            HashMap<String, Object> map_sms = new HashMap<>();
                            map_sms.put("smsbody", "Your NEW OTP is :" + otp_mob + " to update mobile. NICSI");
                            map_sms.put("mobile", new_mobile);
                            map_sms.put("templateId", "1107161647440491832");
                            NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                            object.sendSmsOtpRabbitMq(map_sms);
                            if (otp_mob != 0) {
                                session.put("new_otp_store", otp_mob);
                                session.put("new_otp", otp_mob);
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

    

    public String verify_old_code() {
        //String res = "";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "inside verify old code");

        if (!oldcode.matches("^[0-9]{6}$")) {
            returnString = "old_code_err";
        } else {
            if (oldcode.equals(session.get("old_otp").toString())) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "inside verify old code if matched both code");

                returnString = "success";
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "inside verify old code if unsuccess");

                returnString = "unsuccess";
            }
        }

        return SUCCESS;
    }

    public String verify_new_code() {
        //String res = "";
        // Map profile_values = new HashMap();
        UserData userdata = (UserData) session.get("uservalues");
        mobile = userdata.getMobile();
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "inside verify NEW code");

        Map profile_values = (HashMap) session.get("profile-values");
        if (!oldcode.matches("^[0-9]{6}$")) {
            returnString = "new_code_err";
        } else {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "newcode" + newcode + "new code in session:" + session.get("new_otp").toString());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "newcode" + oldcode + "new code in session:" + session.get("old_otp").toString());
            if (!newcode.equals(session.get("new_otp").toString())) {
                returnString = "newOtpNotMatch";
                System.out.println("1:::::::::::::");

            } else if (!oldcode.equals(session.get("old_otp").toString())) {

                returnString = "oldOtpNotMatch";
                System.out.println("2:::::::::::::");

            } else if ((oldcode.equals(session.get("old_otp").toString())) && (newcode.equals(session.get("new_otp").toString()))) {
                System.out.println("3:::::::::::::");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "inside verify NEW code if mathes both code");

                //String email = userdata.getEmail();
                returnString = signup.savemobile(oldcode, newcode, mobile, new_mobile, userdata);
                if (returnString.equals("sucess")) {
                    userdata.setMobile(new_mobile);
                    session.put("uservalues", userdata);
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUP == USERAGENT " + userAgent + " == " + "returnstring" + returnString);

                session.put("profile-values", profile_values);

            } else {
                System.out.println("4:::::::::::::");
                returnString = "unsuccess";
            }
        }

        return SUCCESS;
    }

    public String verify_new_code_update_mobile() {
        //String res = "";
        // Map profile_values = new HashMap();
        if (!newcode.equals(session.get("new_otp").toString())) {
            returnString = "newOtpNotMatch";

        } else if (newcode.equals(session.get("new_otp").toString())) {
            returnString = "success";
        } else {
            System.out.println("4:::::::::::::");
            returnString = "unsuccess";
        }

        return SUCCESS;
    }

    public String checkrequest(String email) {
        returnString = signup.checkrequest(email);
        return returnString;

    }

    public Set<String> fetchIdMapped(String email) {
        return signup.fetchIdMapped(email);
    }

    public boolean flagForService(UserData userdata, boolean flag) {
        System.out.println("user is in flagForService::::" + userdata.isIsNewUser() + "whichform" + whichform);
        
        // boolean flag = false;
        if (!userdata.isIsEmailValidated()) {
            if (userdata.isIsNewUser()) {
                if (whichform.equals(Constants.PROFILE_FORM_KEYWORD)) {
                    view = "profile";
                    flag = true;
                } else {
                    returnString = "profileNotFound";
                }
            } else {
                if (whichform.equals("email")) {
                    view = "Email_registration";
                    flag = true;
                } else if (whichform.equals("utm")) {

                    view = "Firewall_registration";
                    flag = true;

                } else if (whichform.equals("vpn")) {
                    view = "Vpn_registration";
                    flag = true;
                    try {

                        if ((userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state"))) {
                            statecolist = signup.getStateCoordinator(userdata);
                            session.put("vpn_state_coo_select", statecolist);
                            session.put("vpn_coo_select", "");
                        } else if ((!userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state")) && (!userdata.getUserOfficialData().getPostingState().equalsIgnoreCase("delhi"))) {
                            list = signup.getCentralMinistry(userdata);
                            System.out.println("list::::::::" + list);
                            session.put("vpn_coo_select", list);
                            statecolist = signup.getStateCoordinator(userdata);
                            System.out.println("statecolist::::::::" + statecolist);
                            session.put("vpn_state_coo_select", statecolist);
                        } else if ((!userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state")) && (userdata.getUserOfficialData().getPostingState().equalsIgnoreCase("delhi"))) {
                            list = signup.getCentralMinistry(userdata);
                            session.put("vpn_coo_select", list);
                            session.put("vpn_state_coo_select", "");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (whichform.equals("sms")) {
                    view = "Sms_Registration";
                    session.put("flag", 0);
                    flag = true;

                } else if (whichform.equals(Constants.PROFILE_FORM_KEYWORD)) {
                    view = "profile";
                    flag = true;
                    String email = "";

                } else if (whichform.equals("webcast")) {
                    view = "Webcast_registration";
                    flag = true;
                } else {
                    returnString = "nongov";

                }
                
                
                //for audit purpose
//                UserData udata = (UserData)session.get("uservalues");      
//                if(!email.equals(udata.getEmail())){
//                    returnString="nongov";
//                    flag=false;
//        }
//        

            }
        } else {

            if (userdata.isIsNewUser()) {
                if (whichform.equals(Constants.PROFILE_FORM_KEYWORD)) { // else if added by pr on 31stdec19
                    view = "profile";
                    flag = true;
                } else {
                    returnString = "profileNotFound";
                }

            } else if (userdata.isIsNewUserforService() && !whichform.equals(Constants.PROFILE_FORM_KEYWORD)) {
                returnString = "pendingldap";
            } else {
                if (session.get("update_without_oldmobile").equals("yes")) {
                    returnString = "updatemobile";

                } else {
                    if (whichform.equals("dlist")) {
                        view = "Distribution_registration";
                        flag = true;
                    } else if (whichform.equals("bulkdlist")) {
                        view = "bulk_distribution_registration";
                        flag = true;
                    } else if (whichform.equals("dns")) {
                        view = "Dns_registration";
                        flag = true;
                    } else if (whichform.equals("email")) {
                        System.out.println("inside email::::::::::::");
                        view = "Email_registration";
                        flag = true;
                    } else if (whichform.equals("imap")) {
                        view = "ImapPop_registration";
                        flag = true;
                    } else if (whichform.equals("ip")) {
                        view = "Change_ip";
                        flag = true;
                    } else if (whichform.equals("ldap")) {
                        view = "Ldap_registration";
                        flag = true;
                    } else if (whichform.equals("mobile")) {
                        view = "Mobile_registration";
                        flag = true;
                    } else if (whichform.equals("nkn")) {
                        view = "Nkn_registration";
                        flag = true;
                    } else if (whichform.equals("relay")) {
                        view = "Relay_registration";
                        flag = true;
                    } else if (whichform.equals("sms")) {
                        view = "Sms_Registration";
                        session.put("flag", 0);
                        flag = true;
                    } else if (whichform.equals("wifi")) {
                        view = "Wifi_registration";
                        flag = true;
                    } else if (whichform.equals("wifiport")) {
                        view = "Wifiport_registration";
                        flag = true;
                    } else if (whichform.equalsIgnoreCase("coopannel")) {
                        view = "cooPannel";
                        flag = true;
                    } else if (whichform.equals("utm")) {

                        view = "Firewall_registration";
                        flag = true;

                    } else if (whichform.equals("vpn")) {
                        view = "Vpn_registration";
                        try {
                            if ((userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state"))) {
                                statecolist = signup.getStateCoordinator(userdata);
                                session.put("vpn_state_coo_select", statecolist);
                                session.put("vpn_coo_select", "");
                            } else if ((!userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state")) && (!userdata.getUserOfficialData().getPostingState().equalsIgnoreCase("delhi"))) {
                                list = signup.getCentralMinistry(userdata);
                                System.out.println("list::::::::" + list);
                                session.put("vpn_coo_select", list);
                                statecolist = signup.getStateCoordinator(userdata);
                                System.out.println("statecolist::::::::" + statecolist);
                                session.put("vpn_state_coo_select", statecolist);
                            } else if ((!userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state")) && (userdata.getUserOfficialData().getPostingState().equalsIgnoreCase("delhi"))) {
                                list = signup.getCentralMinistry(userdata);
                                session.put("vpn_coo_select", list);
                                session.put("vpn_state_coo_select", "");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (whichform.equals(Constants.PROFILE_FORM_KEYWORD)) { // else if added by pr on 31stdec19
                        view = "profile";
                        flag = true;

                    } else if (whichform.equals("cloud")) {
                        view = "https://cloud.gov.in/registration.php";
                        flag = true;
                    } else if (whichform.equals("firewall")) {
                        String ip = ServletActionContext.getRequest().getRemoteAddr();
                        if (ip.startsWith("10.")) {
                            view = "https://farps.nic.in/";
                        } else {
                            view = "modal";
                        }
                        flag = true;
                    } else if (whichform.equals("gov")) {
                        view = "https://registry.gov.in";
                        flag = true;
                    } else if (whichform.equals("audit")) {
                        view = "https://asams.nic.in/ASAMS/index.aspx";
                        flag = true;
                    } else if (whichform.equals("security")) {
                        view = "https://soc.nic.in";
                        flag = true;
                    } else if (whichform.equals("waf")) {
                        view = "https://security.nic.in";
                        flag = true;
                    } else if (whichform.equals("sampark")) {
                        view = "https://sampark.gov.in/Sampark/guidelines/guidelines.html";
                        flag = true;
                    } else if (whichform.equals("video")) {
                        view = "http://reserve.nic.in";
                        flag = true;
                    } else if (whichform.equals("webcast")) {
                        view = "Webcast_registration";
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    public Map fetchhmValue(UserData userdata) {
        email = userdata.getEmail();
        Set<String> aliases = userdata.getAliases();
        Map hm = new HashMap();
        if (!userdata.isIsEmailValidated()) {
            if (!userdata.isIsNewUser()) {
                for (String useremail : aliases) {
                    hm = signup.getUserValues(useremail, userdata.isIsEmailValidated(), userdata.isIsNICEmployee());
                    if (hm.size() > 0) {
                        session.put("profile-values", hm);
                        getValuesFromDbInUserValues(hm);
                    }

                }
            }
        }

        return hm;
    }

    public void getValuesFromDbInUserValues(Map hm) {
        /* for previous session use*/
        UserData userdata = (UserData) session.get("uservalues");
        userdata.setMobile(hm.get("mobile").toString());
        userdata.setEmail(hm.get("email").toString());
        userdata.setName(hm.get("cn").toString());
        userdata.setTelephone(hm.get("ophone").toString());
        userdata.getUserOfficialData().setDesignation(hm.get("desig").toString());
        userdata.getUserOfficialData().setEmployeeCode(hm.get("emp_code").toString());
        userdata.getUserOfficialData().setOfficeAddress(hm.get("postalAddress").toString());
        userdata.getUserOfficialData().setPostingCity(hm.get("nicCity").toString());
        userdata.getUserOfficialData().setPostingState(hm.get("state").toString());
        userdata.getUserOfficialData().setOfficePhone(hm.get("ophone").toString());
        userdata.getUserOfficialData().setRphone(hm.get("rphone").toString());
        userdata.getUserOfficialData().setPincode(hm.get("pin").toString());
        if (hm.get("under_sec_email") != null) {
            userdata.getUserOfficialData().setUnder_sec_email(hm.get("under_sec_email").toString());
            userdata.getUserOfficialData().setUnder_sec_name(hm.get("under_sec_name").toString());
            userdata.getUserOfficialData().setUnder_sec_mobile(hm.get("under_sec_mobile").toString());
            userdata.getUserOfficialData().setUnder_sec_tel(hm.get("under_sec_tel").toString());
            userdata.getUserOfficialData().setUnder_sec_desig(hm.get("under_sec_desig").toString());
        }
        if (hm.get("user_employment").toString().toLowerCase().equals("central") || hm.get("user_employment").toString().toLowerCase().equals("UT")) {
            userdata.getUserOfficialData().setDepartment(hm.get("dept").toString());
            userdata.getUserOfficialData().setMinistry(hm.get("min").toString());
            if (hm.get("dept").toString().toLowerCase().trim().equals("other")) {
                userdata.getUserOfficialData().setOther_dept(hm.get("other_dept").toString());
            }
        } else if (hm.get("user_employment").toString().toLowerCase().equals("state")) {
            userdata.getUserOfficialData().setState(hm.get("stateCode").toString());
            userdata.getUserOfficialData().setDepartment(hm.get("dept").toString());
            if (hm.get("dept").toString().toLowerCase().trim().equals("other")) {
                userdata.getUserOfficialData().setOther_dept(hm.get("other_dept").toString());
            }
        } else if (hm.get("user_employment").toString().toLowerCase().equals("Others") || hm.get("user_employment").toString().toLowerCase().equals("Psu") || hm.get("user_employment").toString().toLowerCase().equals("Const") || hm.get("user_employment").toString().toLowerCase().equals("Nkn") || hm.get("user_employment").toString().toLowerCase().equals("Project")) {
            userdata.getUserOfficialData().setOrganizationCategory(hm.get("Org").toString());
            if (hm.get("Org").toString().toLowerCase().equals("other")) {
                userdata.getUserOfficialData().setOther_dept(hm.get("other_dept").toString());
            }
        }
        userdata.getUserOfficialData().setEmployment(hm.get("user_employment").toString());
        userdata.getHodData().setEmail(hm.get("hod_email").toString());
        if (hm.get("hod_mobile").toString().startsWith("+91")) {
            userdata.getHodData().setMobile(hm.get("hod_mobile").toString());
        } else {
            userdata.getHodData().setMobile(hm.get("hod_mobile").toString());
        }
        userdata.getHodData().setDesignation(hm.get("ca_design").toString());
        userdata.getHodData().setTelephone(hm.get("hod_tel").toString());
        userdata.getHodData().setGovEmployee(ldap.emailValidate(hm.get("hod_email").toString()));
        session.put("uservalues", userdata);

//             
    }

}
