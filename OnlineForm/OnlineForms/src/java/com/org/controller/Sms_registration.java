package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.ProfileService;
import com.org.service.SmsService;
import com.org.service.UpdateService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author Nikki
 */
public class Sms_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    public String action_type, data;
    String[] sms_service;
    Map session;
    SmsService smsservice = null;
    UpdateService updateService = null;// line added by pr on 1stfeb18
    String returnString;
    Map<String, Object> prvwdetails = null;
    Map<String, Object> error = null;
    Map profile_values = null;
    validation valid = null;
    ProfileService profileService = null;
    Map hodDetails = null;
    private Database db = null;
    Map<String, String> map = null;
    // start, code added by pr on 22ndjan18
    String CSRFRandom;
    Ldap ldap;
    Map smsTotal =null;
    public Sms_registration() {
        smsservice = new SmsService();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        error = new HashMap<>();
        valid = new validation();
        updateService = new UpdateService();
        profile_values = new HashMap();
        prvwdetails = new HashMap<>();
        db = new Database();
        map = new HashMap<>();
        ldap = new Ldap();
        if (smsservice == null) {
            smsservice = new SmsService();
        }
        if (map == null) {
            map = new HashMap<>();
        }
        if (db == null) {
            db = new Database();
        }
        if (prvwdetails == null) {
            prvwdetails = new HashMap<>();
        }
        if (smsTotal == null) {
            smsTotal = new HashMap<>();
        }
        if (valid == null) {
            valid = new validation();
        }
        if (error == null) {
            error = new HashMap();
        }
        if (updateService == null) {
            updateService = new UpdateService();
        }
        if (hodDetails == null) {
            hodDetails = new HashMap();
        }
        if (profileService == null) {
            profileService = new ProfileService();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
    }
    public Map getSmsTotal() {
        return smsTotal;
    }

    public void setSmsTotal(Map smsTotal) {
        this.smsTotal = smsTotal;
    }
    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    // end, code added by pr on 22ndjan18
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public String[] getSms_service() {
        return sms_service;
    }

    public void setSms_service(String[] sms_service) {
        this.sms_service = sms_service;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = Jsoup.parse(returnString).text();
    }

    public Map<String, Object> getPrvwdetails() {
        return prvwdetails;
    }

    public void setPrvwdetails(Map<String, Object> prvwdetails) {
        this.prvwdetails = prvwdetails;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = Jsoup.parse(action_type).text();

    }

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
    }

    public String SMS_tab1() {
        // validation
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            String sms_service_final = Arrays.toString(sms_service).replace("[", "");
            sms_service_final = sms_service_final.replace("]", "");
            form_details.setSmsservice1(sms_service_final);
            boolean pull_url_error = valid.pullurlValidation(form_details.getPull_url());
            boolean pull_keyword_error = valid.pullkeywordValidation(form_details.getPull_keyword());
            boolean scode_chk_error = valid.serviceValidation(form_details.getS_code());
            boolean snote_error = valid.scodeValidation(form_details.getShort_code());
            boolean app_name_error = valid.nameSmsValidation(form_details.getApp_name());
            boolean app_url_error = valid.pullurlValidation(form_details.getApp_url());
            boolean purpose_error = valid.purposeValidation(form_details.getSms_usage());
            //  boolean server_loc_error = valid.addValidation(form_details.getServer_loc());
            boolean base_ip_error = valid.baseipValidation(form_details.getBase_ip());
            boolean service_ip_error = valid.serviceipValidation(form_details.getService_ip());
            if (sms_service_final.matches("^[a-zA-Z\\,]{2,100}$")) {
                if (sms_service_final.contains("pull")) {
                    if (pull_url_error == true) {
                        error.put("pull_url_error", "Enter URL (e.g: https://abc.com)");
                        form_details.setPull_url(ESAPI.encoder().encodeForHTML(form_details.getPull_url()));
                    }
                    if (pull_keyword_error == true) {
                        error.put("pull_keyword_error", "Enter Keyword [Alphanumeric,length 1 to 15 digits]");
                        form_details.setPull_keyword(ESAPI.encoder().encodeForHTML(form_details.getPull_keyword()));
                    }
                    if (form_details.getS_code().equals("n") || form_details.getS_code().equals("y") || form_details.getS_code().equals("b")) {

                    } else {
                        error.put("snote_flag_err", "Please select Short code");
                        form_details.setShort_code(ESAPI.encoder().encodeForHTML(form_details.getShort_code()));
                    }
                    if (!form_details.getS_code().equals("n")) {
                        if (snote_error == true) {
                            error.put("snote_error", "Enter short code [Only Numeric are allowed, length 3 to 10 ]");
                            form_details.setShort_code(ESAPI.encoder().encodeForHTML(form_details.getShort_code()));
                        }
                    }
                }
                if (!sms_service_final.equals("quicksms")) {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "here");
                    if (app_name_error == true) {
                        error.put("app_name_error", "Enter Name of the Applicaion [characters,Numbers,dot(.) and whitespace]");
                        form_details.setApp_name(ESAPI.encoder().encodeForHTML(form_details.getApp_name()));
                    }
                    if (app_url_error == true) {
                        error.put("app_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                        form_details.setApp_url(ESAPI.encoder().encodeForHTML(form_details.getApp_url()));
                    }
                    if (purpose_error == true) {
                        error.put("purpose_error", "Enter Purpose of the application [characters,dot(.) and whitespace]");
                        form_details.setSms_usage(ESAPI.encoder().encodeForHTML(form_details.getSms_usage()));
                    }

                    if (form_details.getServer_loc().equals("Other")) {
                        boolean server_txt_error = valid.addValidation(form_details.getServer_loc_txt());
                        if (server_txt_error == true) {
                            error.put("server_txt_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed");
                            form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                        }
                    } else {
                        boolean server_loc_error = valid.addValidation(form_details.getServer_loc());
                        if (server_loc_error == true) {
                            error.put("server_loc_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed");
                            form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
                            form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                        }
                    }

//                    if (server_loc_error == true) {
//                        error.put("server_loc_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed");
//                        form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
//                    }
                }
            } else {
                error.put("sms_serv_err", "Please choose appropriate SMS Service");
                //form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
                sms_service = null;
            }

            if (base_ip_error == true) {
                error.put("base_ip_error", "Enter Application IP1 [e.g: 10.10.10.10]");
                form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
            }
            if (service_ip_error == true) {
                error.put("service_ip_error", "Enter Application IP2 [e.g: 10.10.10.10]");
                form_details.setService_ip(ESAPI.encoder().encodeForHTML(form_details.getService_ip()));
            }

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            // end, code added by pr on 22ndjan18
            // set bean in session
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map in SMS controller tab 1: " + error);
            if (error.isEmpty()) {
                Map profile_values = (HashMap) session.get("profile-values");
                if (session.get("update_without_oldmobile").equals("no")) {
                    int generated_key = smsservice.SMS_tab1(profile_values, form_details);
                    session.put("gen_key", generated_key);
                    session.put("flag", "1");
                    session.put("sms_details1", form_details);
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 1: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
        }
        return SUCCESS;
    }

    public String SMS_tab2() {
        // validation
        try {

            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);     
            System.out.println("state ::::" + form_details.getTstate() + "city:::::::" + form_details.getTcity());
            boolean t_name_error = valid.nameValidation(form_details.getT_off_name());
            boolean t_desig_error = valid.desigValidation(form_details.getTdesignation());
            boolean t_empcode_error = valid.employcodevalidation(form_details.getTemp_code());
            boolean t_address_error = valid.addValidation(form_details.getTaddrs());
            boolean t_city_error = valid.cityValidation(form_details.getTcity());

            if (form_details.getTstate() == null) {
                error.put("t_state_error", "Please update state (where you are posted) and district in profile");
            }
            boolean t_state_error = valid.addstateValidation(form_details.getTstate());
            boolean t_pin_error = valid.pinValidation(form_details.getTpin());
            boolean t_tel1_error = valid.telValidation(form_details.getTtel_ofc());
            boolean t_tel2_error = valid.tel1Validation(form_details.getTtel_res());
            boolean t_mobile_error = valid.MobileValidation(form_details.getTmobile());
            boolean t_email_error = valid.EmailValidation(form_details.getTauth_email());

            if (t_name_error == true) {
                error.put("t_name_error", "Enter Name of The Admin [characters,dot(.) and whitespace]");
                form_details.setT_off_name(ESAPI.encoder().encodeForHTML(form_details.getT_off_name()));
            }
            if (t_desig_error == true) {
                error.put("t_desig_error", "Enter Designation [characters,Alphanumeric,whitespace and [. , - &]]");
                form_details.setTdesignation(ESAPI.encoder().encodeForHTML(form_details.getTdesignation()));
            }
            if (t_empcode_error == true) {
                error.put("t_empcode_error", "Enter Admin Employee Code [Only characters and digits allowed]");
                form_details.setTemp_code(ESAPI.encoder().encodeForHTML(form_details.getTemp_code()));
            }
            if (t_address_error == true) {
                error.put("t_address_error", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                form_details.setTaddrs(ESAPI.encoder().encodeForHTML(form_details.getTaddrs()));
            }
            if (t_city_error == true) {
                error.put("t_city_error", "Enter City [Only characters, whitespace,&,() allowed]");
                form_details.setTcity(ESAPI.encoder().encodeForHTML(form_details.getTcity()));
            }
            if (t_state_error == true) {
                error.put("t_state_error", "Please enter Technical admin State in correct format");
                form_details.setTstate(ESAPI.encoder().encodeForHTML(form_details.getTstate()));
            }
            if (t_pin_error == true) {
                error.put("t_pin_error", "Enter Pin Code [Only digits(6) allowed]");
                form_details.setTpin(ESAPI.encoder().encodeForHTML(form_details.getTpin()));
            }
            if (t_tel1_error == true) {
                error.put("t_tel1_error", "Enter Telephone number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                form_details.setTtel_ofc(ESAPI.encoder().encodeForHTML(form_details.getTtel_ofc()));
            }
            if (t_tel2_error == true) {
                error.put("t_tel2_error", "Enter Telephone number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                form_details.setTtel_res(ESAPI.encoder().encodeForHTML(form_details.getTtel_res()));
            }
            if (t_mobile_error == true) {
                error.put("t_mobile_error", "Enter Mobile [e.g: +919999999999]");
                form_details.setTmobile(ESAPI.encoder().encodeForHTML(form_details.getTmobile()));
            }
            if (t_email_error == true) {
                error.put("t_email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                form_details.setTauth_email(ESAPI.encoder().encodeForHTML(form_details.getTauth_email()));
            }

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            // end, code added by pr on 22ndjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map in SMS controller tab 2: " + error);
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    session.put("sms_details", form_details);
                    Map profile_values = (HashMap) session.get("profile-values");
                    String generated_key = session.get("gen_key").toString();
                    smsservice.SMS_tab2(profile_values, form_details, generated_key);
                    session.put("flag", "2");
                    session.put("sms_details2", form_details);
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 2: " + e.getMessage());
            e.printStackTrace();
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
        }
        return SUCCESS;
    }

    public String SMS_tab3() {
        // validation
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            boolean b_name_error = valid.nameValidation(form_details.getBauth_off_name());
            boolean b_desig_error = valid.desigValidation(form_details.getBdesignation());
            boolean b_empcode_error = valid.employcodevalidation(form_details.getBemp_code());
            boolean b_address_error = valid.addValidation(form_details.getBaddrs());
            boolean b_city_error = valid.cityValidation(form_details.getBcity());
            boolean b_state_error = valid.addstateValidation(form_details.getBstate());
            boolean b_pin_error = valid.pinValidation(form_details.getBpin());
            boolean b_tel1_error = valid.telValidation(form_details.getBtel_ofc());
            boolean b_tel2_error = valid.tel1Validation(form_details.getBtel_res());
            boolean b_mobile_error = valid.MobileValidation(form_details.getBmobile());
            boolean b_email_error = valid.EmailValidation(form_details.getBauth_email());

            if (b_name_error == true) {
                error.put("b_name_error", "Enter Name of The Owner [characters,dot(.) and whitespace]");
                form_details.setBauth_off_name(ESAPI.encoder().encodeForHTML(form_details.getBauth_off_name()));
            }
            if (b_desig_error == true) {
                error.put("b_desig_error", "Enter Designation [characters,Alphanumeric,whitespace and [. , - &]]");
                form_details.setBdesignation(ESAPI.encoder().encodeForHTML(form_details.getBdesignation()));
            }
            if (b_empcode_error == true) {
                error.put("b_empcode_error", "Enter Admin Employee Code [Only characters and digits allowed]");
                form_details.setBemp_code(ESAPI.encoder().encodeForHTML(form_details.getBemp_code()));
            }
            if (b_address_error == true) {
                error.put("b_address_error", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                form_details.setBaddrs(ESAPI.encoder().encodeForHTML(form_details.getBaddrs()));
            }
            if (b_city_error == true) {
                error.put("b_city_error", "Enter City [Only characters, whitespace,&,() allowed]");
                form_details.setBcity(ESAPI.encoder().encodeForHTML(form_details.getBcity()));
            }
            if (b_state_error == true) {
                error.put("b_state_error", "Please Enter State in correct format");
                form_details.setBstate(ESAPI.encoder().encodeForHTML(form_details.getBstate()));
            }
            if (b_pin_error == true) {
                error.put("b_pin_error", "Enter Pin Code [Only digits(6) allowed]");
                form_details.setBpin(ESAPI.encoder().encodeForHTML(form_details.getBpin()));
            }
            if (b_tel1_error == true) {
                error.put("b_tel1_error", "Enter Telephone Number STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]");
                form_details.setBtel_ofc(ESAPI.encoder().encodeForHTML(form_details.getBtel_ofc()));
            }
            if (b_tel2_error == true) {
                error.put("b_tel2_error", "Enter Telephone Number STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]");
                form_details.setBtel_res(ESAPI.encoder().encodeForHTML(form_details.getBtel_res()));
            }
            if (b_mobile_error == true) {
                error.put("b_mobile_error", "Enter Mobile [e.g: +919999999999]");
                form_details.setBmobile(ESAPI.encoder().encodeForHTML(form_details.getBmobile()));
            }
            if (b_email_error == true) {
                error.put("b_email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                form_details.setBauth_email(ESAPI.encoder().encodeForHTML(form_details.getBauth_email()));
            }

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            // end, code added by pr on 22ndjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map in SMS controller tab 3: " + error);
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    Map profile_values = (HashMap) session.get("profile-values");
                    String generated_key = session.get("gen_key").toString();
                    smsservice.SMS_tab3(profile_values, form_details, generated_key);
                    session.put("flag", "3");
                    session.put("sms_details3", form_details);
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 3: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
        }
        return SUCCESS;
    }

    public String SMS_tab4() {
        // validation
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            boolean ip_staging_error = valid.ipstagingvalidation(form_details.getStaging_ip());
            boolean domestic_traf_error = valid.domesticTrafficvalidation(form_details.getDomestic_traf());
            boolean inter_traf_error = valid.intertrafvalidation(form_details.getInter_traf());
            boolean sender_id_error = valid.senderidValidation(form_details.getSender_id());
            String sms_service_final = Arrays.toString(sms_service).replace("[", "");
            sms_service_final = sms_service_final.replace("]", "");
            if (form_details.getAudit().equals("No")) {
                if (form_details.getDatepicker1().equals("")) {
                    error.put("audit_date_err", "Enter Audit Clearence Date");
                    form_details.setDatepicker1(ESAPI.encoder().encodeForHTML(form_details.getDatepicker1()));
                }
            } else if (form_details.getAudit().equals("Yes")) {

            } else {
                error.put("audit_error", "Select application security audit cleared");
                form_details.setAudit(ESAPI.encoder().encodeForHTML(form_details.getAudit()));
            }

            if (ip_staging_error == true) {
                error.put("ip_staging_error", "Enter IP of Staging Server [e.g: 10.10.10.10]");
                form_details.setStaging_ip(ESAPI.encoder().encodeForHTML(form_details.getStaging_ip()));
            }



       
            if (!sms_service_final.equals("quicksms")) {
                if (domestic_traf_error == true) {
                    error.put("domestic_traf_error", "Enter Your Projected Monthly Traffic not less than 1000[Only digits allowed]");
                    form_details.setDomestic_traf(ESAPI.encoder().encodeForHTML(form_details.getDomestic_traf()));
                }
                if (inter_traf_error == true) {
                    error.put("inter_traf_error", "Enter International Traffic [Only digits allowed (minimum 1000)]");
                    form_details.setInter_traf(ESAPI.encoder().encodeForHTML(form_details.getInter_traf()));
                }

                if (form_details.getSender().equals("Yes")) {
                    if (sender_id_error == true) {
                        error.put("sender_id_error", "For characters [length 6 only], for digits [length min:4, max:7 only]");
                        form_details.setSender_id(ESAPI.encoder().encodeForHTML(form_details.getSender_id()));
                    }
                } else if (form_details.getSender().equals("No")) {


                } else {
                    error.put("sender_err", "Choose Do you have TRAI exempted Sender Id?");
                    form_details.setSender_id(ESAPI.encoder().encodeForHTML(form_details.getSender_id()));
                }
            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }

            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map in SMS controller tab 4: " + error);
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    profile_values = (HashMap) session.get("profile-values");
                    String generated_key = session.get("gen_key").toString();
                    hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                    String bo = hodDetails.get("bo").toString();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                    profile_values.put("bo_check", bo);
                    prvwdetails = smsservice.SMS_tab4(form_details, generated_key);
                    session.put("flag", "4");
                    session.put("sms_details4", form_details);

                    smsTotal.put("smsDetails1", session.get("sms_details1"));
                    smsTotal.put("smsDetails2", session.get("sms_details2"));
                    smsTotal.put("smsDetails3", session.get("sms_details3"));
                    smsTotal.put("smsDetails4", session.get("sms_details4"));
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 4: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
        }

        return SUCCESS;
    }

    public String SMS_tab5() {
        try {
            db = new Database();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            profile_values = (HashMap) session.get("profile-values");
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
             String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            if (!hodDetails.get("bo").toString().equals("no bo")) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }

            String sms_service_final = Arrays.toString(sms_service).replace("[", "");
            sms_service_final = sms_service_final.replace("]", "");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == sms_service_final in sms tab5" + sms_service_final);
            form_details.setSmsservice1(sms_service_final);
            boolean pull_url_error = valid.pullurlValidation(form_details.getPull_url());
            boolean pull_keyword_error = valid.pullkeywordValidation(form_details.getPull_keyword());
            boolean scode_chk_error = valid.serviceValidation(form_details.getS_code());
            boolean snote_error = valid.scodeValidation(form_details.getShort_code());
            boolean app_name_error = valid.nameSmsValidation(form_details.getApp_name());
            boolean app_url_error = valid.pullurlValidation(form_details.getApp_url());
            boolean purpose_error = valid.purposeValidation(form_details.getSms_usage());
            // boolean server_loc_error = valid.nameValidation(form_details.getServer_loc());
            boolean base_ip_error = valid.baseipValidation(form_details.getBase_ip());
            boolean service_ip_error = valid.serviceipValidation(form_details.getService_ip());
            boolean t_name_error = valid.nameValidation(form_details.getT_off_name());
            boolean t_desig_error = valid.desigValidation(form_details.getTdesignation());
            boolean t_empcode_error = valid.employcodevalidation(form_details.getTemp_code());
            boolean t_address_error = valid.addValidation(form_details.getTaddrs());
            boolean t_city_error = valid.cityValidation(form_details.getTcity());
            boolean t_state_error = valid.addstateValidation(form_details.getTstate());
            boolean t_pin_error = valid.pinValidation(form_details.getTpin());
            boolean t_tel1_error = valid.telValidation(form_details.getTtel_ofc());
            boolean t_tel2_error = valid.tel1Validation(form_details.getTtel_res());
            boolean t_mobile_error = valid.MobileValidation(form_details.getTmobile());
            boolean t_email_error = valid.EmailValidation(form_details.getTauth_email());
            boolean b_name_error = valid.nameValidation(form_details.getBauth_off_name());
            boolean b_desig_error = valid.desigValidation(form_details.getBdesignation());
            boolean b_empcode_error = valid.employcodevalidation(form_details.getBemp_code());
            boolean b_address_error = valid.addValidation(form_details.getBaddrs());
            boolean b_city_error = valid.cityValidation(form_details.getBcity());
            boolean b_state_error = valid.addstateValidation(form_details.getBstate());
            boolean b_pin_error = valid.pinValidation(form_details.getBpin());
            boolean b_tel1_error = valid.telValidation(form_details.getBtel_ofc());
            boolean b_tel2_error = valid.tel1Validation(form_details.getBtel_res());
            boolean b_mobile_error = valid.MobileValidation(form_details.getBmobile());
            boolean b_email_error = valid.EmailValidation(form_details.getBauth_email());
            boolean ip_staging_error = valid.ipstagingvalidation(form_details.getStaging_ip());
            boolean domestic_traf_error = valid.domesticTrafficvalidation(form_details.getDomestic_traf());
            boolean inter_traf_error = valid.intertrafvalidation(form_details.getInter_traf());
            boolean sender_id_error = valid.senderidValidation(form_details.getSender_id());

            // if (sms_service_final.matches("^[a-zA-Z\\,]{2,100}$")) {
            if (sms_service_final.contains("pull")) {
                if (pull_url_error == true) {
                    error.put("pull_url_error", "Enter URL (e.g: https://abc.com)");
                    form_details.setPull_url(ESAPI.encoder().encodeForHTML(form_details.getPull_url()));
                }
                if (pull_keyword_error == true) {
                    error.put("pull_keyword_error", "Enter Keyword [Alphanumeric,length 1 to 15 digits]");
                    form_details.setPull_keyword(ESAPI.encoder().encodeForHTML(form_details.getPull_keyword()));
                }
                if (form_details.getS_code().equals("n") || form_details.getS_code().equals("y") || form_details.getS_code().equals("b")) {

                } else {
                    error.put("snote_flag_err", "Please select Short code");
                    form_details.setShort_code(ESAPI.encoder().encodeForHTML(form_details.getShort_code()));
                }
                if (!form_details.getS_code().equals("n")) {
                    if (snote_error == true) {
                        error.put("snote_error", "Enter short code [Only Numeric are allowed, length 3 to 10 ]");
                        form_details.setShort_code(ESAPI.encoder().encodeForHTML(form_details.getShort_code()));
                    }
                }
            }
            if (!sms_service_final.equals("quicksms")) {
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "here");
                if (app_name_error == true) {
                    error.put("app_name_error", "Enter Name of the Applicaion [characters,Numbers,dot(.) and whitespace]");
                    form_details.setApp_name(ESAPI.encoder().encodeForHTML(form_details.getApp_name()));
                }
                if (app_url_error == true) {
                    error.put("app_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                    form_details.setApp_url(ESAPI.encoder().encodeForHTML(form_details.getApp_url()));
                }
                if (purpose_error == true) {
                    error.put("purpose_error", "Enter Purpose of the application [characters,dot(.) and whitespace]");
                    form_details.setSms_usage(ESAPI.encoder().encodeForHTML(form_details.getSms_usage()));
                }
//                if (server_loc_error == true) {
//                    error.put("server_loc_error", "Enter Server Location [characters,dot(.) and whitespace]");
//                    form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
//                }

                if (form_details.getServer_loc().equals("Other")) {
                    boolean server_txt_error = valid.addValidation(form_details.getServer_loc_txt());
                    if (server_txt_error == true) {
                        error.put("server_txt_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed");
                        form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                    }
                } else {
                    boolean server_loc_error = valid.addValidation(form_details.getServer_loc());
                    if (server_loc_error == true) {
                        error.put("server_loc_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed");
                        form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
                        form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                    }
                }
            }
//            } else {
//                error.put("sms_serv_err", "Enter Server Location [characters,dot(.) and whitespace]");
//                //form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
//                sms_service = null;
//            }
            if (base_ip_error == true) {
                error.put("base_ip_error", "Enter Application IP1 [e.g: 10.10.10.10]");
                form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
            }
            if (service_ip_error == true) {
                error.put("service_ip_error", "Enter Application IP2 [e.g: 10.10.10.10]");
                form_details.setService_ip(ESAPI.encoder().encodeForHTML(form_details.getService_ip()));
            }

            if (t_name_error == true) {
                error.put("t_name_error", "Enter Name of The Admin [characters,dot(.) and whitespace]");
                form_details.setT_off_name(ESAPI.encoder().encodeForHTML(form_details.getT_off_name()));
            }
            if (t_desig_error == true) {
                error.put("t_desig_error", "Enter Designation [characters,Alphanumeric,whitespace and [. , - &]]");
                form_details.setTdesignation(ESAPI.encoder().encodeForHTML(form_details.getTdesignation()));
            }
            if (t_empcode_error == true) {
                error.put("t_empcode_error", "Enter Admin Employee Code [Only characters and digits allowed]");
                form_details.setTemp_code(ESAPI.encoder().encodeForHTML(form_details.getTemp_code()));
            }
            if (t_address_error == true) {
                error.put("t_address_error", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                form_details.setTaddrs(ESAPI.encoder().encodeForHTML(form_details.getTaddrs()));
            }
            if (t_city_error == true) {
                error.put("t_city_error", "Enter City [Only characters, whitespace allowed]");
                form_details.setTcity(ESAPI.encoder().encodeForHTML(form_details.getTcity()));
            }
            if (t_state_error == true) {
                error.put("t_state_error", "Please enter Technical admin State in correct format");
                form_details.setTstate(ESAPI.encoder().encodeForHTML(form_details.getTstate()));
            }
            if (t_pin_error == true) {
                error.put("t_pin_error", "Enter Pin Code [Only digits(6) allowed]");
                form_details.setTpin(ESAPI.encoder().encodeForHTML(form_details.getTpin()));
            }
            if (t_tel1_error == true) {
                error.put("t_tel1_error", "Enter Telephone number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                form_details.setTtel_ofc(ESAPI.encoder().encodeForHTML(form_details.getTtel_ofc()));
            }
            if (t_tel2_error == true) {
                error.put("t_tel2_error", "Enter Telephone number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                form_details.setTtel_res(ESAPI.encoder().encodeForHTML(form_details.getTtel_res()));
            }
            if (t_mobile_error == true) {
                error.put("t_mobile_error", "Enter Mobile [e.g: +919999999999]");
                form_details.setTmobile(ESAPI.encoder().encodeForHTML(form_details.getTmobile()));
            }
            if (t_email_error == true) {
                error.put("t_email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                form_details.setTauth_email(ESAPI.encoder().encodeForHTML(form_details.getTauth_email()));
            }
            if (b_name_error == true) {
                error.put("b_name_error", "Enter Name of The Owner [characters,dot(.) and whitespace]");
                form_details.setBauth_off_name(ESAPI.encoder().encodeForHTML(form_details.getBauth_off_name()));
            }
            if (b_desig_error == true) {
                error.put("b_desig_error", "Enter Designation [characters,Alphanumeric,whitespace and [. , - &]]");
                form_details.setBdesignation(ESAPI.encoder().encodeForHTML(form_details.getBdesignation()));
            }
            if (b_empcode_error == true) {
                error.put("b_empcode_error", "Enter Admin Employee Code [Only characters and digits allowed]");
                form_details.setBemp_code(ESAPI.encoder().encodeForHTML(form_details.getBemp_code()));
            }
            if (b_address_error == true) {
                error.put("b_address_error", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                form_details.setBaddrs(ESAPI.encoder().encodeForHTML(form_details.getBaddrs()));
            }
            if (b_city_error == true) {
                error.put("b_city_error", "Enter City [Only characters, whitespace,&,() allowed]");
                form_details.setBcity(ESAPI.encoder().encodeForHTML(form_details.getBcity()));
            }
            if (b_state_error == true) {
                error.put("b_state_error", "Please Enter State in correct format");
                form_details.setBstate(ESAPI.encoder().encodeForHTML(form_details.getBstate()));
            }
            if (b_pin_error == true) {
                error.put("b_pin_error", "Enter Pin Code [Only digits(6) allowed]");
                form_details.setBpin(ESAPI.encoder().encodeForHTML(form_details.getBpin()));
            }
            if (b_tel1_error == true) {
                error.put("b_tel1_error", "Enter Telephone Number STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]");
                form_details.setBtel_ofc(ESAPI.encoder().encodeForHTML(form_details.getBtel_ofc()));
            }
            if (b_tel2_error == true) {
                error.put("b_tel2_error", "Enter Telephone Number STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]");
                form_details.setBtel_res(ESAPI.encoder().encodeForHTML(form_details.getBtel_res()));
            }
            if (b_mobile_error == true) {
                error.put("b_mobile_error", "Enter Mobile [e.g: +919999999999]");
                form_details.setBmobile(ESAPI.encoder().encodeForHTML(form_details.getBmobile()));
            }
            if (b_email_error == true) {
                error.put("b_email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                form_details.setBauth_email(ESAPI.encoder().encodeForHTML(form_details.getBauth_email()));
            }
            if (form_details.getAudit().equals("No")) {
                if (form_details.getDatepicker1().equals("")) {
                    error.put("audit_date_err", "Enter Audit Clearence Date");
                    form_details.setDatepicker1(ESAPI.encoder().encodeForHTML(form_details.getDatepicker1()));
                }
            } else if (form_details.getAudit().equals("Yes")) {

            } else {
                error.put("audit_error", "Select application security audit cleared");
                form_details.setAudit(ESAPI.encoder().encodeForHTML(form_details.getAudit()));
            }

            if (ip_staging_error == true) {
                error.put("ip_staging_error", "Enter IP of Staging Server [e.g: 10.10.10.10]");
                form_details.setStaging_ip(ESAPI.encoder().encodeForHTML(form_details.getStaging_ip()));
            }

            if (domestic_traf_error == true) {
                error.put("domestic_traf_error", "Enter Your Projected Monthly Traffic not less than 1000[Only digits allowed]");
                form_details.setDomestic_traf(ESAPI.encoder().encodeForHTML(form_details.getDomestic_traf()));
            }
            if (inter_traf_error == true) {
                error.put("inter_traf_error", "Enter International Traffic [Only digits allowed (minimum 1000)]");
                form_details.setInter_traf(ESAPI.encoder().encodeForHTML(form_details.getInter_traf()));
            }

            if (form_details.getSender().equals("Yes")) {
                if (sender_id_error == true) {
                    error.put("sender_id_error", "For characters [length 6 only], for digits [length min:4, max:7 only]");
                    form_details.setSender_id(ESAPI.encoder().encodeForHTML(form_details.getSender_id()));
                }
            } else if (form_details.getSender().equals("No")) {

            } else {
                error.put("sender_err", "Choose Do you have TRAI exempted Sender Id?");
                form_details.setSender_id(ESAPI.encoder().encodeForHTML(form_details.getSender_id()));
            }

            if (ldap_employee) {
                System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                if (hodDetails.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Reporting officer should be govt employee");
                }
            }

            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // validate profile 20th march
            boolean employment_error = valid.EmploymentValidation(form_details.getUser_employment());

            if (employment_error == true) {
                error.put("employment_error", "Please enter Employment in correct format");
            } else {
                if (form_details.getUser_employment().equals("Central")) {
                    if (form_details.getMin() == null) {
                        error.put("ministry_error", "Please enter Ministry in correct format");
                    } else if (form_details.getDept() == null) {
                        error.put("dept_error", "Please enter Department in correct format");
                    } else {
                        boolean ministry_error = valid.MinistryValidation(form_details.getMin());
                        boolean dept_error = valid.MinistryValidation(form_details.getDept());
                        if (ministry_error == true) {
                            error.put("ministry_error", "Please enter Ministry in correct format");
                        }
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        System.out.println("minlist" + minlist);
                        System.out.println("form_details.getMin()" + form_details.getMin());
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("ministry_error", "Please enter Ministry in correct format");
                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");

                        }
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
                        }

                        List deplist = profileService.getCentralDept(form_details.getMin());
                        System.out.println("deplist" + deplist);
                        if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!deplist.contains(form_details.getDept())) {
                                error.put("dept_error", "Please enter Ministry in correct format");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getDept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("State")) {
                    if (form_details.getState_dept() == null) {
                        error.put("state_dept_error", "Please enter State Department in correct format");
                    } else if (form_details.getStateCode() == null) {
                        error.put("state_error", "Please enter State in correct format");
                    } else {

                        boolean state_dept_error = valid.MinistryValidation(form_details.getState_dept());
                        boolean state_error = valid.nameValidation(form_details.getStateCode());
                        if (state_dept_error == true) {
                            error.put("state_dept_error", "Please enter State Department in correct format");
                        }
                        if (state_error == true) {
                            error.put("state_error", "Please enter State in correct format");
                        }

                        List statedeplist = profileService.getCentralDept(form_details.getStateCode());
                        System.out.println("statedeplist" + statedeplist);
                        if (!form_details.getState_dept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!statedeplist.contains(form_details.getState_dept())) {
                                error.put("state_dept_error", "Please enter correct State Department");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getState_dept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                    if (form_details.getOrg() == null) {
                        error.put("org_error", "Please enter Organization in correct format");
                    } else {
                        boolean org_error = valid.MinistryValidation(form_details.getOrg());
                        if (org_error == true) {
                            error.put("org_error", "Please enter Organization in correct format");
                        }

                        List orglist = profileService.getCentralMinistry(form_details.getUser_employment());
                        System.out.println("orglist@@@@@@@@" + orglist);
                        if (!form_details.getOrg().toLowerCase().equalsIgnoreCase("other")) {
                            if (!orglist.contains(form_details.getOrg())) {
                                error.put("org_error", "Please enter correct department");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getOrg().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                }
            }

            if (form_details.getModule() == null) {
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.roTelValidation(form_details.getHod_tel());
                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Designation [characters,Alphanumeric,whitespace and [. , - &]] allowed");
                }
                if (hod_name_error == true) {
                    error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespace allowed]");
                }
                if (form_details.getHod_mobile().trim().startsWith("+")) {
                    if ((hod_mobile_error == true) && (form_details.getHod_mobile().trim().startsWith("+91"))) {
                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                    } else if ((hod_mobile_error == true) && (!form_details.getHod_mobile().trim().startsWith("+91"))) {
                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
                    } else {

                    }
                } else {

                    error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                }
                if (!hodDetails.get("bo").toString().equals("no bo")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + "inside not equal no::::" + form_details.getHod_mobile() + "hod mobile from hoddetails" + hodDetails.get("mobile"));
//                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
//                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");
//
//                    }
                }
                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                    error.put("hod_email_error", "Please change it in your profile and then come back");

                }
                if (hod_telephone_error == true) {
                    error.put("hod_telephone_error", "Please enter Reporting/Nodal/Forwarding Officer Telephone in correct format");
                }
                if (hod_email_error == true) {
                    error.put("hod_email_error", "Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]");
                } else {
                    // check for nic domain
                    if (form_details.getHod_email().equals(form_details.getUser_email())) {
                        error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                    } else {
                        //Map a = (HashMap) session.get("profile-values");
                        Set s = (Set) userdata.getAliases();

                        System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);

                        ArrayList<String> newAr = new ArrayList<String>();

                        newAr.addAll(s);
                        ArrayList email_aliases = null;
                        email_aliases = newAr;
                        if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                            //b = (ArrayList) userdata.getAliases();
                            //b = (ArrayList) a.get("mailequi");
                            if (email_aliases.contains("mdlmail@nic.in") && form_details.getHod_email().contains("@mazdock.com")) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else if (email_aliases.contains(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else {
                                //String user_bo = session.get("user_bo").toString();
                                if (nic_employee && nicemployeeeditable.equals("no")) {

                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");

                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in ip controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in ip controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in ip controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in ip controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }

                                    }
                                } else {
                                    // DONE BY NIKKI MEENAXI ON 16 feb 
                                    //  hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                }
                            }
                        } else {
                            //String user_bo = session.get("user_bo").toString();
                            if (nic_employee && nicemployeeeditable.equals("no")) {
                                map = db.fetchUserValuesFromOAD(userdata.getUid());
                                if (map.size() > 0) {
                                    if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                        error.put("hod_email_error", "Reporting officer details can not be changed.");

                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in ip controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in ip controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in ip controller tab 3");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in ip controller tab 3");
                                        error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                    }

                                }
                            } else {
                                // DONE BY NIKKI MEENAXI ON 16 feb 
                                // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                            }
                        }
                    }
                }
            }
            // end validate profile 20th march
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map in SMS controller tab 5: " + error);
            if (getAction_type().equals("confirm")) {

                if (!error.isEmpty()) {

                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                    sms_service = null;
                } else {

                    //profile_values = (HashMap) session.get("profile-values");
                    session.put("sms_details", form_details);
                    if (session.get("update_without_oldmobile").equals("no")) {
                        String ref_num = smsservice.SMS_tab5(form_details, profile_values);
                        if (ref_num != null) {
                            int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                            if (i > 0) {
                                profile_values.put("user_employment", form_details.getUser_employment().trim());
                                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                                    profile_values.put("min", form_details.getMin().trim());
                                    profile_values.put("dept", form_details.getDept().trim());
                                } else if (form_details.getUser_employment().trim().equals("State")) {
                                    profile_values.put("dept", form_details.getState_dept().trim());
                                    profile_values.put("stateCode", form_details.getStateCode().trim());
                                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                                    profile_values.put("Org", form_details.getOrg().trim());
                                }
                                profile_values.put("other_dept", form_details.getOther_dept().trim());
                                profile_values.put("ca_name", form_details.getHod_name().trim());
                                profile_values.put("ca_design", form_details.getCa_design().trim());
                                profile_values.put("ca_mobile", form_details.getHod_mobile().trim());
                                profile_values.put("ca_email", form_details.getHod_email().trim());
                                profile_values.put("hod_name", form_details.getHod_name().trim());
                                profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                profile_values.put("hod_email", form_details.getHod_email().trim());
                                profile_values.put("hod_tel", form_details.getHod_tel().trim());
                            }
                        }
                        session.put("ref_num", ref_num);
                        session.put("flag", "5");
                    }
                }
            } else if (getAction_type().equals("validate")) {

                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                    sms_service = null;
                }
            } else if (getAction_type().equals("update")) // else if added by pr on 1stfeb18
            {

                if (error.isEmpty()) {

                    String ref = session.get("ref").toString();
                    //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                    String admin_role = "NA";
                    if (session.get("admin_role") != null) {
                        admin_role = session.get("admin_role").toString();
                    }
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "form_details.get service::" + form_details.getSmsservice1() + "sms service" + sms_service_final);
                    Boolean flag = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                    if (flag && form_details.getModule().equals("user")) {
                        profile_values = (HashMap) session.get("profile-values");
                        form_details.setHod_email(profile_values.get("hod_email").toString());
                        form_details.setHod_name(profile_values.get("hod_name").toString());
                        form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
                        form_details.setHod_tel(profile_values.get("hod_tel").toString());
                        form_details.setCa_design(profile_values.get("ca_design").toString());
                        int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                        if (i > 0) {
                            profile_values.put("user_employment", form_details.getUser_employment().trim());
                            if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                                profile_values.put("min", form_details.getMin().trim());
                                profile_values.put("dept", form_details.getDept().trim());
                            } else if (form_details.getUser_employment().trim().equals("State")) {
                                profile_values.put("dept", form_details.getState_dept().trim());
                                profile_values.put("stateCode", form_details.getStateCode().trim());
                            } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                                profile_values.put("Org", form_details.getOrg().trim());
                            }
                            profile_values.put("other_dept", form_details.getOther_dept().trim());
                        }
                    }

                } else {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 5: " + e.getMessage());
            e.printStackTrace();
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
        }
        return SUCCESS;
    }

    public String SMS_tab6() {

        try {
            String ref_num = session.get("ref_num").toString();
            if (form_details.getTelnet().equals("y") || form_details.getTelnet().equals("n")) {
                smsservice.insertTelnet(form_details.getTelnet(), ref_num);
                if (form_details.getTelnet().equals("y")) {
                    session.put("flag", "7");
                } else {
                    session.put("flag", "6");
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
                form_details.setTelnet(ESAPI.encoder().encodeForHTML(form_details.getTelnet()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 6: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
            form_details.setTelnet(ESAPI.encoder().encodeForHTML(form_details.getTelnet()));
        }

        return SUCCESS;
    }

    public String SMS_tab7() {

        try {
            String ref_num = session.get("ref_num").toString();
            if (form_details.getFirewall_id().matches("^[a-zA-Z0-9 \\/\\-\\,\\.\\(\\)]{1,50}$")) {
                smsservice.insertFirewallId(form_details.getFirewall_id(), ref_num);
                session.put("flag", "7");
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
                sms_service = null;
                form_details.setFirewall_id(ESAPI.encoder().encodeForHTML(form_details.getFirewall_id()));
                error.put("firewall_err", "Please enter Firewall request ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 7: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
            form_details.setFirewall_id(ESAPI.encoder().encodeForHTML(form_details.getFirewall_id()));
        }
        return SUCCESS;
    }

    public void fill_sms_admin_tab() {
        String json = null;
        Map profile_values = (HashMap) session.get("profile-values");

        UserData userdata = (UserData) session.get("uservalues");
        String otp_type = userdata.getVerifiedOtp();
        String email = profile_values.get("email").toString();
        String mobile = profile_values.get("mobile").toString();
        Map<String, Object> details = new HashMap<String, Object>();
        try {
            details = smsservice.fill_sms_admin_tab(otp_type, email, mobile);
            json = new Gson().toJson(details);
            ServletActionContext.getResponse().setContentType("application/json");
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS controller tab 8: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            sms_service = null;
        }
    }
}