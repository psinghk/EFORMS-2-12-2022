/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import com.org.service.WebcastService;
import java.util.ArrayList;
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
 * @author nikki
 */
public class Webcast_registration extends ActionSupport implements SessionAware {

    String printlog;
    FormBean form_details;
    Map session;
    Map<String, Object> error = new HashMap<String, Object>();
    WebcastService webcastservice = new WebcastService();
    UpdateService updateService = new UpdateService();// line added by pr on 1stfeb18
    validation valid = new validation();
    Map profile_values = new HashMap();
    String data, CSRFRandom, action_type, returnString, cert; // CSRFRandom added by pr on 22ndjan18
    ProfileService profileService = new ProfileService();
    Map hodDetails = new HashMap();
    Map fwdofcDetails = new HashMap();

    // start, code added by pr on 22ndjan18
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

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
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

    public Map getFwdofcDetails() {
        return fwdofcDetails;
    }

    public void setFwdofcDetails(Map fwdofcDetails) {
        this.fwdofcDetails = fwdofcDetails;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public Webcast_registration() {
        printlog = ServletActionContext.getRequest().getSession().getId() + " == Webcast.registration.java == ";
    }

    public String Webcast_tab1() {

        // validation
        error.clear();
        UserData userdata = (UserData) session.get("uservalues");
        profile_values = (HashMap) session.get("profile-values");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            if (form_details.getReq_for().matches("^[a-zA-Z]{2,10}$")) {
                returnString = form_details.getReq_for();
                if (form_details.getReq_for().equals("live")) {

                    boolean event_coo_name_error = valid.nameValidation(form_details.getEvent_coo_name());
                    if (event_coo_name_error) {
                        error.put("event_coo_name_err", "Enter Name [characters,dot(.) and whitespace]");
                        form_details.setEvent_coo_name(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_name()));
                    }
                    boolean event_coo_desig_error = valid.desigValidation(form_details.getEvent_coo_design());
                    if (event_coo_desig_error) {
                        error.put("event_coo_desig_err", "Enter Designation [characters,Alphanumeirc,whitespace and [. , - &]]");
                        form_details.setEvent_coo_design(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_design()));
                    }
                    boolean event_coo_mobile_error = valid.WebcastMobileValidation(form_details.getEvent_coo_mobile());
                    if (event_coo_mobile_error) {
                        error.put("event_coo_mobile_err", "Enter Mobile Number [e.g: 9999999999] limit 10 digits");
                        form_details.setEvent_coo_mobile(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_mobile()));
                    }
                    boolean event_coo_email_error = valid.EmailValidation(form_details.getEvent_coo_email());
                    if (event_coo_email_error) {
                        error.put("event_coo_email_err", "Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                        form_details.setEvent_coo_email(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_email()));
                    }
                    boolean event_coo_address_error = valid.addValidation(form_details.getEvent_coo_address());
                    if (event_coo_address_error) {
                        error.put("event_coo_address_err", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                        form_details.setEvent_coo_address(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_address()));
                    }

                    boolean event_name_eng_error = valid.addValidation(form_details.getEvent_name_eng());
                    if (event_name_eng_error) {
                        error.put("event_name_eng_err", "Please enter Event name / description");
                        form_details.setEvent_name_eng(ESAPI.encoder().encodeForHTML(form_details.getEvent_name_eng()));
                    }
                    boolean event_name_hin_error = valid.hinditextValidation(form_details.getEvent_name_hin());
                    if (event_name_hin_error) {
                        error.put("event_name_hin_err", "Please enter Event name / description");
                        form_details.setEvent_name_hin(ESAPI.encoder().encodeForHTML(form_details.getEvent_name_hin()));
                    }
                    String start_daet = form_details.getEvent_start_date();
                    String event_startdate_err = valid.event_startValidation(form_details.getEvent_start_date());
                    System.out.println("event_startdate_err"+event_startdate_err);
                    if (!event_startdate_err.equalsIgnoreCase("NA")) {
                        error.put("event_startdate_err", "Please select event start datetime in correct format");
                        form_details.setEvent_start_date(ESAPI.encoder().encodeForHTML(form_details.getEvent_start_date()));
                    }
                    String event_enddate_err = valid.event_endValidation(start_daet, form_details.getEvent_end_date());
                    if (!event_enddate_err.equalsIgnoreCase("NA")) {
                        error.put("event_enddate_err", "Please select event end datetime in correct format");
                        form_details.setEvent_end_date(ESAPI.encoder().encodeForHTML(form_details.getEvent_end_date()));
                    }

                    boolean event_type_error = valid.eventTypeValidation(form_details.getEvent_type());
                    if (event_type_error) {
                        error.put("event_type_err", "Please select Type of Event");
                        form_details.setEvent_type(ESAPI.encoder().encodeForHTML(form_details.getEvent_type()));
                    }
                    boolean telecast_error = valid.checkradioValidation(form_details.getTelecast());
                    boolean channel_name_error = false;
                    boolean live_feed_error = false;
                    boolean vc_id_error = false;
                    if (telecast_error) {
                        error.put("telecast_err", "Is live telecast will be available on DD?");
                        form_details.setTelecast(ESAPI.encoder().encodeForHTML(form_details.getTelecast()));
                    } else {
                        if (form_details.getTelecast().equals("yes")) {
                            channel_name_error = valid.institueIdValidationnotnull(form_details.getChannel_name());
                            if (channel_name_error) {
                                error.put("channel_name_err", "Please enter Channel name");
                                form_details.setChannel_name(ESAPI.encoder().encodeForHTML(form_details.getChannel_name()));
                            }
                        } else if (form_details.getTelecast().equals("no")) {
                            live_feed_error = valid.eventTypeValidation(form_details.getLive_feed());
                            if (live_feed_error) {
                                error.put("live_feed_err", "How Are you planning to get live audio/video feed?");
                                form_details.setLive_feed(ESAPI.encoder().encodeForHTML(form_details.getLive_feed()));
                            } else {
                                if (form_details.getLive_feed().equals("Through VC")) {
                                    vc_id_error = valid.vcidvalidation(form_details.getVc_id());
                                    if (vc_id_error) {
                                        error.put("vc_id_err", "Please enter VC ID ");
                                        form_details.setVc_id(ESAPI.encoder().encodeForHTML(form_details.getVc_id()));
                                    }
                                }
                            }
                        }
                    }

                    boolean conf_radio_error = valid.checkradioValidation(form_details.getConf_radio());
                    if (conf_radio_error) {
                        error.put("conf_radio_err", "Is it a Conference/Workshop ?");
                        form_details.setConf_radio(ESAPI.encoder().encodeForHTML(form_details.getConf_radio()));
                    } else {
                        boolean conf_name_error = valid.addValidation(form_details.getConf_name());
                        if (conf_name_error) {
                            error.put("conf_name_err", "Enter Name of Conference/Workshop");
                            form_details.setConf_name(ESAPI.encoder().encodeForHTML(form_details.getConf_name()));
                        }
                        boolean conf_type_error = valid.eventTypeValidation(form_details.getConf_type());
                        if (conf_type_error) {
                            error.put("conf_type_err", "Please select Type of Event");
                            form_details.setConf_type(ESAPI.encoder().encodeForHTML(form_details.getConf_type()));
                        }
                        boolean conf_city_error = valid.cityValidation(form_details.getConf_city());
                        if (conf_city_error) {
                            error.put("conf_city_err", "Please enter City and Venue of Conference/Workshop");
                            form_details.setConf_city(ESAPI.encoder().encodeForHTML(form_details.getConf_city()));
                        }
                        boolean conf_schedule_error = valid.confscheduleValidation(form_details.getConf_schedule());
                        if (conf_schedule_error) {
                            error.put("conf_schedule_err", "Please enter Conference/Workshop schedule /Program details with no. of days");
                            form_details.setConf_schedule(ESAPI.encoder().encodeForHTML(form_details.getConf_schedule()));
                        }
                        boolean conf_session_error = valid.confSessionValidation(form_details.getConf_session());
                        if (conf_session_error) {
                            error.put("conf_session_err", "Please enter Number of parallel sessions");
                            form_details.setConf_session(ESAPI.encoder().encodeForHTML(form_details.getConf_session()));
                        }
                        boolean conf_bw_error = valid.institueIdValidationnotnull(form_details.getConf_bw());
                        if (conf_bw_error) {
                            error.put("conf_bw_err", "Please enter Internet Connectivity /Leased line");
                            form_details.setConf_bw(ESAPI.encoder().encodeForHTML(form_details.getConf_bw()));
                        }
                        boolean conf_provider_error = valid.addValidation(form_details.getConf_provider());
                        if (conf_provider_error) {
                            error.put("conf_provider_err", "Please Enter Internet/Leased line Service provider Name");
                            form_details.setConf_provider(ESAPI.encoder().encodeForHTML(form_details.getConf_provider()));
                        }
                        boolean conf_event_hired_error = valid.checkradioValidation(form_details.getConf_event_hired());
                        if (conf_event_hired_error) {
                            error.put("conf_event_hired_err", "Please select correct option");
                            form_details.setConf_event_hired(ESAPI.encoder().encodeForHTML(form_details.getConf_event_hired()));
                        }
                        boolean conf_flash_error = valid.checkradioValidation(form_details.getConf_flash());
                        if (conf_flash_error) {
                            error.put("conf_flash_err", "Please select correct option");
                            form_details.setConf_flash(ESAPI.encoder().encodeForHTML(form_details.getConf_flash()));
                        } else {
                            if (form_details.getConf_flash().equals("no")) {
                                boolean local_setup_error = valid.institueIdValidation(form_details.getLocal_setup());
                                if (local_setup_error) {
                                    error.put("conf_flash_err", "Please enter local setup details");
                                    form_details.setLocal_setup(ESAPI.encoder().encodeForHTML(form_details.getLocal_setup()));
                                }
                            }
                        }
                        boolean conf_video_error = valid.checkradioValidation(form_details.getConf_video());
                        if (conf_video_error) {
                            error.put("conf_video_err", "Please select correct option");
                            form_details.setConf_video(ESAPI.encoder().encodeForHTML(form_details.getConf_video()));
                        } else {
                            if (form_details.getConf_video().equals("yes")) {
                                boolean conf_contact_error = valid.addValidation(form_details.getConf_contact());
                                if (conf_contact_error) {
                                    error.put("conf_contact_err", "Please enter contact details of video agency");
                                    form_details.setConf_contact(ESAPI.encoder().encodeForHTML(form_details.getConf_contact()));
                                }
                            }
                        }
                        boolean hall_type_error = valid.eventTypeValidation(form_details.getHall_type());
                        if (hall_type_error) {
                            error.put("hall_type_err", "Please select correct option");
                            form_details.setHall_type(ESAPI.encoder().encodeForHTML(form_details.getHall_type()));
                        } else {
                          
                            if (form_details.getHall_type().equals("multiple")) {
                                
                                boolean hall_multiple_error = valid.confSessionValidation(form_details.getHall_number());
                               
                                if (hall_multiple_error) {
                                    error.put("hall_type_err", "Please enter Numbers of Hall");
                                    form_details.setHall_number(ESAPI.encoder().encodeForHTML(form_details.getHall_number()));
                                }
                            }
                        }

                    }

                } else if (form_details.getReq_for().equals("demand")) {

                    boolean event_no_error = valid.institueIdValidationnotnull(form_details.getEvent_no());
                    if (event_no_error) {
                        error.put("event_no_err", "Please enter Total number of video clips");
                        form_details.setEvent_no(ESAPI.encoder().encodeForHTML(form_details.getEvent_no()));
                    }
                    boolean event_size_error = valid.institueIdValidationnotnull(form_details.getEvent_size());
                    if (event_size_error) {
                        error.put("event_size_err", "Please enter Total size in GB");
                        form_details.setEvent_size(ESAPI.encoder().encodeForHTML(form_details.getEvent_size()));
                    }
                    boolean media_format_error = valid.employcodevalidation(form_details.getMedia_format());
                    if (media_format_error) {
                        error.put("media_format_err", "Please select Media Format provided");
                        form_details.setMedia_format(ESAPI.encoder().encodeForHTML(form_details.getMedia_format()));
                    }

                }
            } else {
                error.put("req_for_err", "Please choose one option for webcast service");
                form_details.setReq_for(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
            }

            boolean payment_error = valid.checkradioValidation(form_details.getPayment());

            if (payment_error) {
                error.put("payment_err", "Is Payment applicable?");
                form_details.setPayment(ESAPI.encoder().encodeForHTML(form_details.getPayment()));
            } else {
                if (form_details.getPayment().equals("yes")) {
                    boolean cheque_no_error = valid.chequeValidation(form_details.getCheque_no());
                    if (cheque_no_error) {
                        error.put("cheque_no_err", "Please enter Cheque/DD No/NEFT No/RTGS No");
                        form_details.setCheque_no(ESAPI.encoder().encodeForHTML(form_details.getCheque_no()));
                    }
                    boolean cheque_amount_error = valid.webcastamountvalidation(form_details.getCheque_amount());
                    if (cheque_amount_error) {
                        error.put("amount_err", "Please enter Amount,only digits allowed");
                        form_details.setCheque_amount(ESAPI.encoder().encodeForHTML(form_details.getCheque_amount()));
                    }
                    String cheque_date_error = valid.chequeDateValidation(form_details.getCheque_date());
                    if (!cheque_date_error.isEmpty() || !"".equals(cheque_date_error)) {
                        error.put("cheque_date_err", "Enter Date [DD-MM-YYYY]");
                        form_details.setCheque_date(ESAPI.encoder().encodeForHTML(form_details.getCheque_date()));
                    }
                    boolean bank_name_error = valid.banknameValidation(form_details.getBank_name());
                    if (bank_name_error) {
                        error.put("bank_name_err", "Enter Bank & Branch");
                        form_details.setBank_name(ESAPI.encoder().encodeForHTML(form_details.getBank_name()));
                    }
                }
            }

            boolean hod_mobile_error = valid.MobileValidation(form_details.getFwd_ofc_mobile());
            if (hod_mobile_error) {
                error.put("fwd_mobile_err", "Enter Mobile Number [e.g: +919999999999]");
                form_details.setFwd_ofc_mobile(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_mobile()));
            }
            boolean hod_email_error = valid.EmailValidation(form_details.getFwd_ofc_email());
            if (hod_email_error) {
                error.put("fwd_email_err", "Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                form_details.setFwd_ofc_email(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_email()));
            }
            boolean hod_telephone_error = valid.roTelValidation(form_details.getFwd_ofc_tel());
            if (hod_telephone_error) {
                error.put("fwd_tel_err", "Enter Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                form_details.setFwd_ofc_tel(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_tel()));
            }
            boolean hod_address_error = valid.addValidation(form_details.getFwd_ofc_add());
            if (hod_address_error) {
                error.put("fwd_add_err", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                form_details.setFwd_ofc_add(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_add()));
            }

            boolean hod_name_error = valid.nameValidation(form_details.getFwd_ofc_name());
            if (hod_name_error) {
                error.put("fwd_name_err", "Enter Name [characters,dot(.) and whitespace]");
                form_details.setFwd_ofc_name(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_name()));
            }
            boolean hod_desig_error = valid.desigValidation(form_details.getFwd_ofc_design());
            if (hod_desig_error) {
                error.put("fwd_desig_err", "Enter Designation [characters,Alphanumeirc,whitespace and [. , - &]]");
                form_details.setFwd_ofc_design(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_design()));
            }

            // check for nic domain
            if (!form_details.getFwd_ofc_email().equals("")) {
                fwdofcDetails = (HashMap) profileService.getHODdetails(form_details.getFwd_ofc_email());
                String bo = fwdofcDetails.get("bo").toString();
                if (bo.equals("NIC Employees")) {

                } else {
                    error.put("fwd_email_err", "Forwarding officer can only be an NIC Employee");
                }
                if (!fwdofcDetails.get("mobile").equals(form_details.getFwd_ofc_mobile())) {
                    error.put("fwd_mobile_err", "Forwarding officer's mobile number should be same in ldap");

                }
            }

            Set s = (Set) userdata.getAliases();
            ArrayList<String> newAr = new ArrayList<String>();
            newAr.addAll(s);
            ArrayList email_aliases = null;
            email_aliases = newAr;
            if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                if (email_aliases.contains(form_details.getFwd_ofc_email())) {
                    error.put("fwd_email_err", "Forwarding Officer email can not be same as user email address or its aliases.");
                }
            }

            if (!fwdofcDetails.get("mobile").equals(form_details.getFwd_ofc_mobile())) {
                error.put("fwd_mobile_err", "Forwarding officer's mobile number should be same in ldap");
            }

            boolean remarks_error = valid.remarksValidation(form_details.getRemarks());
            if (remarks_error) {
                error.put("remarks_err", "Please enter any other details or remarks");
                form_details.setRemarks(ESAPI.encoder().encodeForHTML(form_details.getRemarks()));
            }
//            if (!cert.equals("true")) {
//                error.put("file_err", "Please upload Letter/Schedule/Agenda in PDF/JPG format (less than 1mb)");
//            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // end, code added by pr on 22ndjan18
            System.out.println(printlog + "ERROR in webcast controller tab 1: " + error);
            if (error.isEmpty()) {
                if (form_details.getReq_for().equals("live") && cert.equals("true")) {
                    if (session.get("webcast_uploaded_files") != null) {
                        form_details.setWebcast_uploaded_files((Map) session.get("webcast_uploaded_files"));
                    } else {
                        form_details.setWebcast_uploaded_files(null);
                    }
                } else {
                    session.remove("webcast_uploaded_files");
                }
                int generated_key = webcastservice.Webcast_tab1(form_details);
                session.put("gen_key", generated_key);
                session.put("webcast_details", form_details);
            } else {
                data = "";
                //returnString = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            System.out.println(printlog + "exception for webcast controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            returnString = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String Webcast_tab2() {
        try {
            UserData userdata = (UserData) session.get("uservalues");
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            
            profile_values = (HashMap) session.get("profile-values");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            form_details = mapper.readValue(data, FormBean.class);
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }

            if (form_details.getReq_for().matches("^[a-zA-Z]{2,10}$")) {
                returnString = form_details.getReq_for();
                if (form_details.getReq_for().equals("live")) {

                    boolean event_coo_name_error = valid.nameValidation(form_details.getEvent_coo_name());
                    if (event_coo_name_error) {
                        error.put("event_coo_name_err", "Enter Name [characters,dot(.) and whitespace]");
                        form_details.setEvent_coo_name(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_name()));
                    }
                    boolean event_coo_desig_error = valid.desigValidation(form_details.getEvent_coo_design());
                    if (event_coo_desig_error) {
                        error.put("event_coo_desig_err", "Enter Designation [characters,Alphanumeirc,whitespace and [. , - &]]");
                        form_details.setEvent_coo_design(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_design()));
                    }
                    boolean event_coo_mobile_error = valid.WebcastMobileValidation(form_details.getEvent_coo_mobile());
                    if (event_coo_mobile_error) {
                        error.put("event_coo_mobile_err", "Enter Mobile Number [e.g: +919999999999]");
                        form_details.setEvent_coo_mobile(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_mobile()));
                    }
                    boolean event_coo_email_error = valid.EmailValidation(form_details.getEvent_coo_email());
                    if (event_coo_email_error) {
                        error.put("event_coo_email_err", "Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                        form_details.setEvent_coo_email(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_email()));
                    }
                    boolean event_coo_address_error = valid.addValidation(form_details.getEvent_coo_address());
                    if (event_coo_address_error) {
                        error.put("event_coo_address_err", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                        form_details.setEvent_coo_address(ESAPI.encoder().encodeForHTML(form_details.getEvent_coo_address()));
                    }

                    boolean event_name_eng_error = valid.addValidation(form_details.getEvent_name_eng());
                    if (event_name_eng_error) {
                        error.put("event_name_eng_err", "Please enter Event name / description");
                        form_details.setEvent_name_eng(ESAPI.encoder().encodeForHTML(form_details.getEvent_name_eng()));
                    }
                    boolean event_name_hin_error = valid.hinditextValidation(form_details.getEvent_name_hin());
                    if (event_name_hin_error) {
                        error.put("event_name_hin_err", "Please enter Event name / description");
                        form_details.setEvent_name_hin(ESAPI.encoder().encodeForHTML(form_details.getEvent_name_hin()));
                    }
//                    boolean event_date_error = valid.institueIdValidationnotnull(form_details.getEvent_date());
//                    if (event_date_error) {
//                        error.put("event_date_err", "Please enter Date and timings of Event");
//                        form_details.setEvent_date(ESAPI.encoder().encodeForHTML(form_details.getEvent_date()));
//                    }

                    String start_daet = form_details.getEvent_start_date();
                    String event_startdate_err = valid.event_startValidation(form_details.getEvent_start_date());
                    System.out.println("event_startdate_err"+event_startdate_err);
                    if (!event_startdate_err.equalsIgnoreCase("NA")) {
                        error.put("event_startdate_err", "Please select event start datetime in correct format");
                        form_details.setEvent_start_date(ESAPI.encoder().encodeForHTML(form_details.getEvent_start_date()));
                    }
                    String event_enddate_err = valid.event_endValidation(start_daet, form_details.getEvent_end_date());
                    if (!event_enddate_err.equalsIgnoreCase("NA")) {
                        error.put("event_enddate_err", "Please select event end datetime in correct format");
                        form_details.setEvent_end_date(ESAPI.encoder().encodeForHTML(form_details.getEvent_end_date()));
                    }
                    boolean event_type_error = valid.eventTypeValidation(form_details.getEvent_type());
                    if (event_type_error) {
                        error.put("event_type_err", "Please select Type of Event");
                        form_details.setEvent_type(ESAPI.encoder().encodeForHTML(form_details.getEvent_type()));
                    }
                    boolean telecast_error = valid.checkradioValidation(form_details.getTelecast());
                    boolean channel_name_error = false;
                    boolean live_feed_error = false;
                    boolean vc_id_error = false;
                    if (telecast_error) {
                        error.put("telecast_err", "Is live telecast will be available on DD?");
                        form_details.setTelecast(ESAPI.encoder().encodeForHTML(form_details.getTelecast()));
                    } else {
                        if (form_details.getTelecast().equals("yes")) {
                            channel_name_error = valid.institueIdValidationnotnull(form_details.getChannel_name());
                            if (channel_name_error) {
                                error.put("channel_name_err", "Please enter Channel name");
                                form_details.setChannel_name(ESAPI.encoder().encodeForHTML(form_details.getChannel_name()));
                            }
                        } else if (form_details.getTelecast().equals("no")) {
                            live_feed_error = valid.eventTypeValidation(form_details.getLive_feed());
                            if (live_feed_error) {
                                error.put("live_feed_err", "How Are you planning to get live audio/video feed?");
                                form_details.setLive_feed(ESAPI.encoder().encodeForHTML(form_details.getLive_feed()));
                            } else {
                                if (form_details.getLive_feed().equals("Through VC")) {
                                    vc_id_error = valid.vcidvalidation(form_details.getVc_id());
                                    if (vc_id_error) {
                                        error.put("vc_id_err", "Please enter VC ID limit[5]");
                                        form_details.setVc_id(ESAPI.encoder().encodeForHTML(form_details.getVc_id()));
                                    }
                                }
                            }
                        }
                    }

                    boolean conf_radio_error = valid.checkradioValidation(form_details.getConf_radio());
                    if (conf_radio_error) {
                        error.put("conf_radio_err", "Is it a Conference/Workshop ?");
                        form_details.setConf_radio(ESAPI.encoder().encodeForHTML(form_details.getConf_radio()));
                    } else {
                        boolean conf_name_error = valid.addValidation(form_details.getConf_name());
                        if (conf_name_error) {
                            error.put("conf_name_err", "Enter Name of Conference/Workshop");
                            form_details.setConf_name(ESAPI.encoder().encodeForHTML(form_details.getConf_name()));
                        }
                        boolean conf_type_error = valid.eventTypeValidation(form_details.getConf_type());
                        if (conf_type_error) {
                            error.put("conf_type_err", "Please select Type of Event");
                            form_details.setConf_type(ESAPI.encoder().encodeForHTML(form_details.getConf_type()));
                        }
                        boolean conf_city_error = valid.cityValidation(form_details.getConf_city());
                        if (conf_city_error) {
                            error.put("conf_city_err", "Please enter City and Venue of Conference/Workshop");
                            form_details.setConf_city(ESAPI.encoder().encodeForHTML(form_details.getConf_city()));
                        }
                        boolean conf_schedule_error = valid.confscheduleValidation(form_details.getConf_schedule());
                        if (conf_schedule_error) {
                            error.put("conf_schedule_err", "Please enter Conference/Workshop schedule /Program details with no. of days");
                            form_details.setConf_schedule(ESAPI.encoder().encodeForHTML(form_details.getConf_schedule()));
                        }
                        boolean conf_session_error = valid.confSessionValidation(form_details.getConf_session());
                        if (conf_session_error) {
                            error.put("conf_session_err", "Please enter Number of parallel sessions");
                            form_details.setConf_session(ESAPI.encoder().encodeForHTML(form_details.getConf_session()));
                        }
                        boolean conf_bw_error = valid.institueIdValidationnotnull(form_details.getConf_bw());
                        if (conf_bw_error) {
                            error.put("conf_bw_err", "Please enter Internet Connectivity /Leased line");
                            form_details.setConf_bw(ESAPI.encoder().encodeForHTML(form_details.getConf_bw()));
                        }
                        boolean conf_provider_error = valid.addValidation(form_details.getConf_provider());
                        if (conf_provider_error) {
                            error.put("conf_provider_err", "Enter Internet/Leased line Service provider Name");
                            form_details.setConf_provider(ESAPI.encoder().encodeForHTML(form_details.getConf_provider()));
                        }
                        boolean conf_event_hired_error = valid.checkradioValidation(form_details.getConf_event_hired());
                        if (conf_event_hired_error) {
                            error.put("conf_event_hired_err", "Please select correct option");
                            form_details.setConf_event_hired(ESAPI.encoder().encodeForHTML(form_details.getConf_event_hired()));
                        }
                        boolean conf_flash_error = valid.checkradioValidation(form_details.getConf_flash());
                        if (conf_flash_error) {
                            error.put("conf_flash_err", "Please select correct option");
                            form_details.setConf_flash(ESAPI.encoder().encodeForHTML(form_details.getConf_flash()));
                        } else {
                            if (form_details.getConf_flash().equals("no")) {
                                boolean local_setup_error = valid.institueIdValidation(form_details.getLocal_setup());
                                if (local_setup_error) {
                                    error.put("conf_flash_err", "Please enter local setup details");
                                    form_details.setLocal_setup(ESAPI.encoder().encodeForHTML(form_details.getLocal_setup()));
                                }
                            }
                        }
                        boolean conf_video_error = valid.checkradioValidation(form_details.getConf_video());
                        if (conf_video_error) {
                            error.put("conf_video_err", "Please select correct option");
                            form_details.setConf_video(ESAPI.encoder().encodeForHTML(form_details.getConf_video()));
                        } else {
                            if (form_details.getConf_video().equals("yes")) {
                                boolean conf_contact_error = valid.addValidation(form_details.getConf_contact());
                                if (conf_contact_error) {
                                    error.put("conf_contact_err", "Please enter contact details of video agency");
                                    form_details.setConf_contact(ESAPI.encoder().encodeForHTML(form_details.getConf_contact()));
                                }
                            }
                        }
                        boolean hall_type_error = valid.eventTypeValidation(form_details.getHall_type());
                        if (hall_type_error) {
                            error.put("hall_type_err", "Please select correct option");
                            form_details.setHall_type(ESAPI.encoder().encodeForHTML(form_details.getHall_type()));
                        } else {
                            if (form_details.getHall_type().equals("multiple")) {
                                boolean hall_multiple_error = valid.confSessionValidation(form_details.getHall_number());
                                if (hall_multiple_error) {
                                    error.put("hall_type_err", "Please enter Numbers of Hall");
                                    form_details.setHall_number(ESAPI.encoder().encodeForHTML(form_details.getHall_number()));
                                }
                            }
                        }

                    }

                } else if (form_details.getReq_for().equals("demand")) {

                    boolean event_no_error = valid.institueIdValidationnotnull(form_details.getEvent_no());
                    if (event_no_error) {
                        error.put("event_no_err", "Please enter Total number of video clips");
                        form_details.setEvent_no(ESAPI.encoder().encodeForHTML(form_details.getEvent_no()));
                    }
                    boolean event_size_error = valid.institueIdValidationnotnull(form_details.getEvent_size());
                    if (event_size_error) {
                        error.put("event_size_err", "Please enter Total size in GB");
                        form_details.setEvent_size(ESAPI.encoder().encodeForHTML(form_details.getEvent_size()));
                    }
                    boolean media_format_error = valid.employcodevalidation(form_details.getMedia_format());
                    if (media_format_error) {
                        error.put("media_format_err", "Please select Media Format provided");
                        form_details.setMedia_format(ESAPI.encoder().encodeForHTML(form_details.getMedia_format()));
                    }
                }
            } else {
                error.put("req_for_err", "Please choose one option for webcast service");
                form_details.setReq_for(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
            }

            boolean payment_error = valid.checkradioValidation(form_details.getPayment());

            if (payment_error) {
                error.put("payment_err", "Is Payment applicable?");
                form_details.setPayment(ESAPI.encoder().encodeForHTML(form_details.getPayment()));
            } else {
                if (form_details.getPayment().equals("yes")) {
                    boolean cheque_no_error = valid.chequeValidation(form_details.getCheque_no());
                    if (cheque_no_error) {
                        error.put("cheque_no_err", "Please enter Cheque/DD No/NEFT No/RTGS No");
                        form_details.setCheque_no(ESAPI.encoder().encodeForHTML(form_details.getCheque_no()));
                    }
                    boolean cheque_amount_error = valid.webcastamountvalidation(form_details.getCheque_amount());
                    if (cheque_amount_error) {
                        error.put("amount_err", "Please enter Amount,only digits allowed");
                        form_details.setCheque_amount(ESAPI.encoder().encodeForHTML(form_details.getCheque_amount()));
                    }
                    String cheque_date_error = valid.chequeDateValidation(form_details.getCheque_date());
                    if (!cheque_date_error.isEmpty() || !"".equals(cheque_date_error)) {
                        error.put("cheque_date_err", "Enter Date [DD-MM-YYYY]");
                        form_details.setCheque_date(ESAPI.encoder().encodeForHTML(form_details.getCheque_date()));
                    }
                    boolean bank_name_error = valid.banknameValidation(form_details.getBank_name());
                    if (bank_name_error) {
                        error.put("bank_name_err", "Enter Bank & Branch");
                        form_details.setBank_name(ESAPI.encoder().encodeForHTML(form_details.getBank_name()));
                    }
                }
            }

            boolean remarks_error = valid.remarksValidation(form_details.getRemarks());
            if (remarks_error) {
                error.put("remarks_err", "Please enter any other details or remarks");
                form_details.setRemarks(ESAPI.encoder().encodeForHTML(form_details.getRemarks()));
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
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("ministry_error", "Please enter Ministry in correct format");
                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");

                        }
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
                        }

                        List deplist = profileService.getCentralDept(form_details.getMin());
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
                                System.out.println(printlog + "ORG EMPTY");
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
                                System.out.println(printlog + "ORG EMPTY");
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
                                System.out.println(printlog + "ORG EMPTY");
                            }
                        }
                    }
                }
            }
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (form_details.getModule() == null) {
                boolean hod_mobile_error = valid.MobileValidation(form_details.getFwd_ofc_mobile());
                if (hod_mobile_error) {
                    error.put("fwd_mobile_err", "Enter Mobile Number [e.g: +919999999999]");
                    form_details.setFwd_ofc_mobile(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_mobile()));
                }
                boolean hod_email_error = valid.EmailValidation(form_details.getFwd_ofc_email());
                if (hod_email_error) {
                    error.put("fwd_email_err", "Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                    form_details.setFwd_ofc_email(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_email()));
                }
                boolean hod_telephone_error = valid.telValidation(form_details.getFwd_ofc_tel());
                if (hod_telephone_error) {
                    error.put("fwd_tel_err", "Enter Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                    form_details.setFwd_ofc_tel(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_tel()));
                }
                boolean hod_address_error = valid.addValidation(form_details.getFwd_ofc_add());
                if (hod_address_error) {
                    error.put("fwd_add_err", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                    form_details.setFwd_ofc_add(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_add()));
                }

                boolean hod_name_error = valid.nameValidation(form_details.getFwd_ofc_name());
                if (hod_name_error) {
                    error.put("fwd_name_err", "Enter Name [characters,dot(.) and whitespace]");
                    form_details.setFwd_ofc_name(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_name()));
                }
                boolean hod_desig_error = valid.desigValidation(form_details.getFwd_ofc_design());
                if (hod_desig_error) {
                    error.put("fwd_desig_err", "Enter Designation [characters,Alphanumeirc,whitespace and [. , - &]]");
                    form_details.setFwd_ofc_design(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_design()));
                }

                // check for nic domain
                if (!form_details.getFwd_ofc_email().equals("")) {
                    fwdofcDetails = (HashMap) profileService.getHODdetails(form_details.getFwd_ofc_email());
                    String bo = fwdofcDetails.get("bo").toString();
                    if (bo.equals("NIC Employees")) {

                    } else {
                        error.put("fwd_email_err", "Forwarding officer can only be an NIC Employee");
                    }
                    if (!fwdofcDetails.get("mobile").equals(form_details.getFwd_ofc_mobile())) {
                        error.put("fwd_mobile_err", "Forwarding officer's mobile number should be same in ldap");

                    }
                }

                Set s = (Set) userdata.getAliases();
                ArrayList<String> newAr = new ArrayList<String>();
                newAr.addAll(s);
                ArrayList email_aliases = null;
                email_aliases = newAr;
                if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                    if (email_aliases.contains(form_details.getFwd_ofc_email())) {
                        error.put("fwd_email_err", "Forwarding Officer email can not be same as user email address or its aliases.");
                    }
                }

                if (!fwdofcDetails.get("mobile").equals(form_details.getFwd_ofc_mobile())) {
                    error.put("fwd_mobile_err", "Forwarding officer's mobile number should be same in ldap");
                }
            }
            // end validate profile 20th march

            System.out.println(printlog + "ERROR Map for Webcast controller tab 2: " + error);

            if (getAction_type().equals("submit")) {

                if (session.get("webcast_uploaded_files") != null) {
                    form_details.setWebcast_uploaded_files((Map) session.get("webcast_uploaded_files"));
                } else {
                    form_details.setWebcast_uploaded_files(null);
                }
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("webcast_details", form_details);
                    String ref_num = webcastservice.Webcast_tab2(form_details, profile_values);
                    if (ref_num != null) {
                        form_details.setCa_design(profile_values.get("ca_design").toString());
                        form_details.setHod_email(profile_values.get("hod_email").toString());
                        form_details.setHod_mobile(profile_values.get("ca_mobile").toString());
                        form_details.setHod_name(profile_values.get("ca_name").toString());
                        form_details.setHod_tel(profile_values.get("hod_tel").toString());
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
                            profile_values.put("fwd_ofc_name", form_details.getFwd_ofc_name().trim());
                            profile_values.put("fwd_ofc_mobile", form_details.getFwd_ofc_email().trim());
                            profile_values.put("fwd_ofc_email", form_details.getFwd_ofc_email().trim());
                            profile_values.put("fwd_ofc_tel", form_details.getFwd_ofc_tel().trim());
                            profile_values.put("fwd_ofc_desig", form_details.getFwd_ofc_design().trim());
                            profile_values.put("fwd_ofc_add", form_details.getFwd_ofc_add().trim());
                        }
                    }
                    session.put("ref_num", ref_num);
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    //returnString = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    profile_values = (HashMap) session.get("profile-values");

                    String dept = profile_values.get("dept").toString();
                    session.put("dept", dept);
                    //if (!user_bo.equals("NIC Employees")) {
                    profile_values.put("fwd_ofc_name", form_details.getFwd_ofc_name().trim());
                    profile_values.put("fwd_ofc_mobile", form_details.getFwd_ofc_mobile().trim());
                    profile_values.put("fwd_ofc_email", form_details.getFwd_ofc_email().trim());
                    profile_values.put("fwd_ofc_tel", form_details.getFwd_ofc_tel().trim());
                    profile_values.put("fwd_ofc_desig", form_details.getFwd_ofc_design().trim());
                    profile_values.put("fwd_ofc_add", form_details.getFwd_ofc_add().trim());

                    session.put("profile-values", profile_values);

                }
            } else if (action_type.equals("update")) {
                if (session.get("webcast_uploaded_files") != null) {
                    form_details.setWebcast_uploaded_files((Map) session.get("webcast_uploaded_files"));
                } else {
                    form_details.setWebcast_uploaded_files(null);
                }
                String ref = session.get("ref").toString();
                //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                String admin_role = "NA";
                if (session.get("admin_role") != null) {
                    admin_role = session.get("admin_role").toString();
                }

                Boolean fl = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                if (fl && form_details.getModule().equals("user")) {
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
                returnString = "";
                action_type = "";
            }

        } catch (Exception e) {
            System.out.println(printlog + "exception for Webcast controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            returnString = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

}
