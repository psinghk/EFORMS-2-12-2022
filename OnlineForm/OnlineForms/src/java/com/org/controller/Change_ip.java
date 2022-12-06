package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.ChangeIpService;
import com.org.service.UpdateService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;
import com.org.service.ProfileService;
import java.util.List;
import java.util.Set;

public class Change_ip extends ActionSupport implements SessionAware {

    ProfileService profileService = null;
    FormBean form_details;
    String data, action_type;
    ChangeIpService changeipservice = null;
    UpdateService updateService = null; // line added by pr on 1stfeb18
    Map session;
    Map profile_values = null;
    validation valid = null;
    Map<String, Object> error = null;
    Map hodDetails = null;
    Map<String, String> map = null;
    // start, code added by pr on 19thjan18
    String CSRFRandom;
    private Database db = null;
    Ldap ldap;

    public Change_ip() {
        profileService = new ProfileService();
        changeipservice = new ChangeIpService();
        updateService = new UpdateService();
        error = new HashMap<>();
        map = new HashMap<>();
        profile_values = new HashMap();
        valid = new validation();
        hodDetails = new HashMap();
        ldap = new Ldap();

        if (profileService == null) {
            profileService = new ProfileService();
        }
        if (changeipservice == null) {
            changeipservice = new ChangeIpService();
        }
        if (updateService == null) {
            updateService = new UpdateService();
        }
        if (error == null) {
            error = new HashMap<>();
        }
        if (map == null) {
            map = new HashMap<>();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
        if (valid == null) {
            valid = new validation();
        }
        if (hodDetails == null) {
            hodDetails = new HashMap();
        }
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    // end, code added by pr on 19thjan18
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public Map getSession() {
        return session;
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

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
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

    public String ip_tab1() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setCSRFRandom(CSRFRandom); // line added by pr on 19thjan18
            form_details.setIp_type("addip");
            error.clear();
            if (form_details.getReq_for().matches("^[a-zA-Z]{2,10}$")) {
                if (form_details.getReq_for().equals("sms")) {
                    boolean sms_acc_error = valid.accnameValidation(form_details.getAccount_name());
                    if (sms_acc_error == true) {
                        error.put("sms_acc_error", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 25 characters]");
                        form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getAccount_name()));
                    }
                }
                if (form_details.getReq_for().equals("ldap")) {
                    boolean ldap_acc_error = valid.accnameValidation(form_details.getLdap_account_name());
                    if (ldap_acc_error == true) {
                        error.put("ldap_acc_error", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 25 characters]");
                        form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getLdap_account_name()));
                    }
                    boolean ldap_url_error = valid.pullurlValidation(form_details.getLdap_url());
                    if (ldap_url_error == true) {
                        error.put("ldap_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                        form_details.setLdap_url(ESAPI.encoder().encodeForHTML(form_details.getLdap_url()));
                    }
                    boolean ldap_alocate_error = valid.addValidation(form_details.getLdap_auth_allocate());
                    System.out.println("ldap_alocate_error:::::" + ldap_alocate_error);
                    if (ldap_alocate_error == true) {
                        error.put("ldap_alocate_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                        form_details.setLdap_auth_allocate(ESAPI.encoder().encodeForHTML(form_details.getLdap_auth_allocate()));
                    }
                }
                if (form_details.getReq_for().equals("relay")) {
                    boolean relay_app_error = valid.nameValidation(form_details.getRelay_app());
                    if (relay_app_error == true) {
                        error.put("app_name_error", "Enter Application Name, [characters limit[50] only,dot(,),comma(,) whitespace allowed]");
                        form_details.setRelay_app(ESAPI.encoder().encodeForHTML(form_details.getRelay_app()));
                    }
                    boolean relay_old_ip_error = valid.serviceipValidation(form_details.getRelay_old_ip());
                    if (relay_old_ip_error == true) {
                        error.put("relay_app_ip_error", "Enter IP Address [e.g. 10.1.1.1]");
                        form_details.setRelay_old_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_old_ip()));
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
                }
            } else {
                error.put("show_req_for", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]");
                form_details.setReq_for(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
            }
            boolean ip1_error = valid.baseipValidation(form_details.getAdd_ip1());
            boolean ip2_error = valid.serviceipValidation(form_details.getAdd_ip2());
            boolean ip3_error = valid.serviceipValidation(form_details.getAdd_ip3());
            boolean ip4_error = valid.serviceipValidation(form_details.getAdd_ip4());
            if (ip1_error == true) {
                error.put("ip1_error", "Enter IP Address 1 [e.g. 10.1.1.1]");
                form_details.setAdd_ip1(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip1()));
            }
            if (ip2_error == true) {
                error.put("ip2_error", "Enter IP Address 2 [e.g. 10.1.1.1]");
                form_details.setAdd_ip2(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip2()));
            }
            if (ip3_error == true) {
                error.put("ip3_error", "Enter IP Address 3 [e.g. 10.1.1.1]");
                form_details.setAdd_ip3(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip3()));
            }
            if (ip4_error == true) {
                error.put("ip4_error", "Enter IP Address 4 [e.g. 10.1.1.1]");
                form_details.setAdd_ip4(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip4()));
            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }

            // start, code added by pr on 19thjan18
            String CSRFRandom = session.get("CSRFRandom").toString();
            if (!form_details.getCSRFRandom().equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "error Map for ip controller tab 1: " + error);
            // end, code added by pr on 19thjan18
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    changeipservice.Insertip_tab1(form_details, "addip");
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("ip_details", form_details);
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for ip controller tab 1: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String ip_tab2() {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setIp_type("changeip");
            error.clear();
            if (form_details.getReq_for().matches("^[a-zA-Z]{2,10}$")) {
                if (form_details.getReq_for().equals("sms")) {
                    boolean sms_acc_error = valid.accnameValidation(form_details.getAccount_name());
                    if (sms_acc_error == true) {
                        error.put("sms_acc_error", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 25 characters]");
                        form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getAccount_name()));
                    }
                }
                if (form_details.getReq_for().equals("ldap")) {
                    boolean ldap_acc_error = valid.accnameValidation(form_details.getLdap_account_name());
                    if (ldap_acc_error == true) {
                        error.put("ldap_acc_error", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 25 characters]");
                        form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getLdap_account_name()));
                    }
                    boolean ldap_url_error = valid.pullurlValidation(form_details.getLdap_url());
                    if (ldap_url_error == true) {
                        error.put("ldap_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                        form_details.setLdap_url(ESAPI.encoder().encodeForHTML(form_details.getLdap_url()));
                    }
                    boolean ldap_alocate_error = valid.addValidation(form_details.getLdap_auth_allocate());

                    if (ldap_alocate_error == true) {
                        error.put("ldap_alocate_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                        form_details.setLdap_auth_allocate(ESAPI.encoder().encodeForHTML(form_details.getLdap_auth_allocate()));
                    }
                }
                if (form_details.getReq_for().equals("relay")) {
                    boolean relay_app_error = valid.nameValidation(form_details.getRelay_app());
                    if (relay_app_error == true) {
                        error.put("app_name_error", "Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespace allowed]");
                        form_details.setRelay_app(ESAPI.encoder().encodeForHTML(form_details.getRelay_app()));
                    }
//                    boolean relay_old_ip_error = valid.serviceipValidation(form_details.getRelay_old_ip());
//                    if (relay_old_ip_error == true) {
//                        error.put("relay_app_ip_error", "Enter IP Address [e.g. 10.1.1.1]");
//                        form_details.setRelay_old_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_old_ip()));
//                    }
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
            } else {
                error.put("show_req_for", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]");
                form_details.setReq_for(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
            }
            boolean ipold1_error = valid.baseipValidation(form_details.getOld_ip1());
            boolean ipnew1_error = valid.baseipValidation(form_details.getNew_ip1());
            boolean ipold2_error = valid.serviceipValidation(form_details.getOld_ip2());
            boolean ipnew2_error = valid.serviceipValidation(form_details.getNew_ip2());
            boolean ipold3_error = valid.serviceipValidation(form_details.getOld_ip3());
            boolean ipnew3_error = valid.serviceipValidation(form_details.getNew_ip3());
            boolean ipold4_error = valid.serviceipValidation(form_details.getOld_ip4());
            boolean ipnew4_error = valid.serviceipValidation(form_details.getNew_ip4());

            if (ipold1_error == true) {
                error.put("ipold1_error", "Please enter OLD IP address1 in correct format");
                form_details.setOld_ip1(ESAPI.encoder().encodeForHTML(form_details.getOld_ip1()));
            }
            if (ipnew1_error == true) {
                error.put("ipnew1_error", "Please enter NEW IP address1 in correct format");
                form_details.setNew_ip1(ESAPI.encoder().encodeForHTML(form_details.getNew_ip1()));
            }
            if (ipold2_error == true) {
                error.put("ipold2_error", "Please enter OLD IP address2 in correct format");
                form_details.setOld_ip2(ESAPI.encoder().encodeForHTML(form_details.getOld_ip2()));
            }
            if (ipnew2_error == true) {
                error.put("ipnew2_error", "Please enter NEW IP address2 in correct format");
                form_details.setNew_ip2(ESAPI.encoder().encodeForHTML(form_details.getNew_ip2()));
            }
            if (ipold3_error == true) {
                error.put("ipold3_error", "Please enter OLD IP address3 in correct format");
                form_details.setOld_ip3(ESAPI.encoder().encodeForHTML(form_details.getOld_ip3()));
            }
            if (ipnew3_error == true) {
                error.put("ipnew3_error", "Please enter NEW IP address3 in correct format");
                form_details.setNew_ip3(ESAPI.encoder().encodeForHTML(form_details.getNew_ip3()));
            }
            if (ipold4_error == true) {
                error.put("ipold4_error", "Please enter OLD IP address4 in correct format");
                form_details.setOld_ip4(ESAPI.encoder().encodeForHTML(form_details.getOld_ip4()));
            }
            if (ipnew4_error == true) {
                error.put("ipnew4_error", "Please enter NEW IP address4 in correct format");
                form_details.setNew_ip4(ESAPI.encoder().encodeForHTML(form_details.getNew_ip4()));
            }
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "error Map for ip controller tab 2: " + error);
            // end, code added by pr on 22ndjan18
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    changeipservice.Insertip_tab2(form_details, "changeip");
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("ip_details", form_details);
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for ip controller tab 2: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String ip_tab3() {
        db = new Database();
        UserData userdata = (UserData) session.get("uservalues");
        HodData hoddata = (HodData) userdata.getHodData();
        boolean ldap_employee = userdata.isIsEmailValidated();
        boolean nic_employee = userdata.isIsNICEmployee();
        String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
        String ip_type = "";
        if (getAction_type().equals("update")) {
            ip_type = session.get("ip_type").toString();
        } else {
            form_details = (FormBean) session.get("ip_details");
            ip_type = form_details.getIp_type();
        }
        error.clear();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setIp_type(ip_type);
            profile_values = (HashMap) session.get("profile-values");
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                if (!hodDetails.get("cn").toString().trim().equals("")) {
//                    form_details.setHod_name(hodDetails.get("cn").toString().trim());
//                    form_details.setCa_name(hodDetails.get("cn").toString().trim());
//                }
//
//                if (!hodDetails.get("desig").toString().trim().equals("")) {
//                    form_details.setCa_design(hodDetails.get("desig").toString().trim());
//                }

            }

            if (form_details.getIp_type().equals("addip")) {
                if (form_details.getReq_for().matches("^[a-zA-Z]{2,10}$")) {
                    if (form_details.getReq_for().equals("sms")) {
                        boolean sms_acc_error = valid.accnameValidation(form_details.getAccount_name());
                        if (sms_acc_error == true) {
                            error.put("sms_acc_error", "Enter Account Name, Alphanumeric length(5-25),dot(.),hyphen(-),underscore(_) allowed");
                            form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getAccount_name()));
                        }
                    }
                    if (form_details.getReq_for().equals("ldap")) {
                        boolean ldap_acc_error = valid.accnameValidation(form_details.getLdap_account_name());
                        if (ldap_acc_error == true) {
                            error.put("ldap_acc_error", "Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 25 characters]");
                            form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getLdap_account_name()));
                        }
                        boolean ldap_url_error = valid.pullurlValidation(form_details.getLdap_url());
                        if (ldap_url_error == true) {
                            error.put("ldap_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                            form_details.setLdap_url(ESAPI.encoder().encodeForHTML(form_details.getLdap_url()));
                        }
                        boolean ldap_alocate_error = valid.addValidation(form_details.getLdap_auth_allocate());

                        if (ldap_alocate_error == true) {
                            error.put("ldap_alocate_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                            form_details.setLdap_auth_allocate(ESAPI.encoder().encodeForHTML(form_details.getLdap_auth_allocate()));
                        }
                    }
                    if (form_details.getReq_for().equals("relay")) {
                        boolean relay_app_error = valid.nameValidation(form_details.getRelay_app());
                        if (relay_app_error == true) {
                            error.put("app_name_error", "Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespace allowed]");
                            form_details.setRelay_app(ESAPI.encoder().encodeForHTML(form_details.getRelay_app()));
                        }
                        boolean relay_old_ip_error = valid.serviceipValidation(form_details.getRelay_old_ip());
                        if (relay_old_ip_error == true) {
                            error.put("relay_app_ip_error", "Enter IP Address [e.g. 10.1.1.1]");
                            form_details.setRelay_old_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_old_ip()));
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
                    }
                } else {
                    error.put("show_req_for", "Enter Account Name, Alphanumeric length(5-15),dot(.),hyphen(-),underscore(_) allowed");
                    form_details.setReq_for(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
                }
                boolean ip1_error = valid.baseipValidation(form_details.getAdd_ip1());
                boolean ip2_error = valid.serviceipValidation(form_details.getAdd_ip2());
                boolean ip3_error = valid.serviceipValidation(form_details.getAdd_ip3());
                boolean ip4_error = valid.serviceipValidation(form_details.getAdd_ip4());

                if (ip1_error == true) {
                    error.put("ip1_error", "Enter ADD IP1 in Correct Format");
                    form_details.setAdd_ip1(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip1()));
                }
                if (ip2_error == true) {
                    error.put("ip2_error", "Enter ADD IP2 in Correct Format");
                    form_details.setAdd_ip2(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip2()));
                }
                if (ip3_error == true) {
                    error.put("ip3_error", "Enter ADD IP3 in Correct Format");
                    form_details.setAdd_ip3(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip3()));
                }
                if (ip4_error == true) {
                    error.put("ip4_error", "Enter ADD IP4 in Correct Format");
                    form_details.setAdd_ip4(ESAPI.encoder().encodeForHTML(form_details.getAdd_ip4()));
                }
            } else if (form_details.getIp_type().equals("changeip")) {
                if (form_details.getReq_for().matches("^[a-zA-Z]{2,10}$")) {
                    if (form_details.getReq_for().equals("sms")) {
                        boolean sms_acc_error = valid.accnameValidation(form_details.getAccount_name());
                        if (sms_acc_error == true) {
                            error.put("sms_acc_error", "Enter Account Name, Alphanumeric length(5-25),dot(.),hyphen(-),underscore(_) allowed");
                            form_details.setAccount_name(ESAPI.encoder().encodeForHTML(form_details.getAccount_name()));
                        }
                    }
                    if (form_details.getReq_for().equals("relay")) {
                        boolean relay_app_error = valid.nameValidation(form_details.getRelay_app());
                        if (relay_app_error == true) {
                            error.put("app_name_error", "Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespace allowed]");
                            form_details.setRelay_app(ESAPI.encoder().encodeForHTML(form_details.getRelay_app()));
                        }
//                        boolean relay_old_ip_error = valid.serviceipValidation(form_details.getRelay_old_ip());
//                        if (relay_old_ip_error == true) {
//                            error.put("relay_app_ip_error", "Enter IP Address [e.g. 10.1.1.1]");
//                            form_details.setRelay_old_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_old_ip()));
//                        }
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
                } else {
                    error.put("show_req_for", "Enter Account Name, Alphanumeric length(5-15),dot(.),hyphen(-),underscore(_) allowed");
                    form_details.setReq_for(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
                }
                boolean ipold1_error = valid.baseipValidation(form_details.getOld_ip1());
                boolean ipnew1_error = valid.baseipValidation(form_details.getNew_ip1());
                if (!form_details.getOld_ip2().equals("") && !form_details.getOld_ip2().equals("")) {
                    boolean ipold2_error = valid.serviceipValidation(form_details.getOld_ip2());
                    boolean ipnew2_error = valid.serviceipValidation(form_details.getNew_ip2());
                    if (ipold2_error == true) {
                        error.put("ipold2_error", "Please enter OLD IP address2 in correct format");
                        form_details.setOld_ip2(ESAPI.encoder().encodeForHTML(form_details.getOld_ip2()));
                    }
                    if (ipnew2_error == true) {
                        error.put("ipnew2_error", "Please enter NEW IP address2 in correct format");
                        form_details.setNew_ip2(ESAPI.encoder().encodeForHTML(form_details.getNew_ip2()));
                    }
                }
                if (form_details.getOld_ip3().equals("") && form_details.getOld_ip3() != null) {
                    boolean ipold3_error = valid.serviceipValidation(form_details.getOld_ip3());
                    boolean ipnew3_error = valid.serviceipValidation(form_details.getNew_ip3());
                    if (ipold3_error == true) {
                        error.put("ip3_error", "Please enter IP address3 in correct format");
                        form_details.setOld_ip3(ESAPI.encoder().encodeForHTML(form_details.getOld_ip3()));
                    }
                    if (ipnew3_error == true) {
                        error.put("ip3_error", "Please enter IP address3 in correct format");
                        form_details.setNew_ip3(ESAPI.encoder().encodeForHTML(form_details.getNew_ip3()));
                    }
                }
                if (form_details.getOld_ip4().equals("") && form_details.getOld_ip4() != null) {
                    boolean ipold4_error = valid.serviceipValidation(form_details.getOld_ip4());
                    boolean ipnew4_error = valid.serviceipValidation(form_details.getNew_ip4());

                    if (ipold4_error == true) {
                        error.put("ip4_error", "Please enter IP address4 in correct format");
                        form_details.setOld_ip4(ESAPI.encoder().encodeForHTML(form_details.getOld_ip4()));
                    }
                    if (ipnew4_error == true) {
                        error.put("ip4_error", "Please enter IP address4 in correct format");
                        form_details.setNew_ip4(ESAPI.encoder().encodeForHTML(form_details.getNew_ip4()));
                    }
                }
                if (ipold1_error == true) {
                    error.put("ipold1_error", "Please enter OLD IP address1 in correct format");
                    form_details.setOld_ip1(ESAPI.encoder().encodeForHTML(form_details.getOld_ip1()));
                }
                if (ipnew1_error == true) {
                    error.put("ipnew1_error", "Please enter NEW IP address1 in correct format");
                    form_details.setNew_ip1(ESAPI.encoder().encodeForHTML(form_details.getNew_ip1()));
                }
            } else {
                error.put("iptype_error", "Incorrect ip_type");
                form_details.setIp_type(ESAPI.encoder().encodeForHTML(form_details.getIp_type()));
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
                boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());
                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
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
                if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                    error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

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
                        // Map a = (HashMap) session.get("profile-values");

                        Set s = (Set) userdata.getAliases();

                        System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);

                        ArrayList<String> newAr = new ArrayList<String>();

                        newAr.addAll(s);
                        ArrayList email_aliases = null;
                        email_aliases = newAr;
                        if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                            //b = (ArrayList) userdata.getAliases();
                            if (email_aliases.contains("mdlmail@nic.in") && form_details.getHod_email().contains("@mazdock.com")) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else if (email_aliases.contains(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else {
                                // String user_bo = session.get("user_bo").toString();
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

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for ip controller tab 3: " + error);

            if (ldap_employee) {

                System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                if (hodDetails.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Reporting officer should be govt employee");
                }

            }

            // end, code added by pr on 22ndjan18
            if (getAction_type().equals("submit")) {
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    String ref_num = "";
                    if (session.get("update_without_oldmobile").equals("no")) {
                        ref_num = changeipservice.Insertip_tab3(form_details, profile_values);
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

                        session.put("ip_details", form_details);
                        session.put("ref_num", ref_num);
                    }
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (getAction_type().equals("update")) // else if added by pr on 1stfeb18
            {
                System.out.println("inside update change ip");
                if (error.isEmpty()) {
                    String ref = session.get("ref").toString();
                    //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                    String admin_role = "NA";
                    if (session.get("admin_role") != null) {
                        admin_role = session.get("admin_role").toString();
                    }
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
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for ip controller tab 3: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;

    }

}
