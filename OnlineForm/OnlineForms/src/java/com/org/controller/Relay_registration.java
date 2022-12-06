package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.ProfileService;
import com.org.service.RelayService;
import com.org.service.UpdateService;
import entities.LdapQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author Nancy
 */
public class Relay_registration extends ActionSupport implements SessionAware {

    RelayService relayservice = new RelayService();
    UpdateService updateService = new UpdateService();// line added by pr on 1stfeb18
    FormBean form_details;
    String data, cert, hardwarecert, action_type, relay_ip, old_relay_ip, relay_sender_id;
    Map session;
    Map profile_values = new HashMap();
    validation valid = new validation();
    Map<String, Object> error = new HashMap<String, Object>();
    ProfileService profileService = new ProfileService();
    Map hodDetails = new HashMap();
    private Database db = null;
    Map<String, String> map = new HashMap<>();
    // start, code added by pr on 22ndjan18
    String CSRFRandom;
    Map userDetails = new HashMap();
    private Ldap ldap = null;
    String domainMX;

    public String getDomainMX() {
        return domainMX;
    }

    public void setDomainMX(String domainMX) {
        this.domainMX = domainMX;
    }

    public String getRelay_sender_id() {
        return relay_sender_id;
    }

    public void setRelay_sender_id(String relay_sender_id) {
        this.relay_sender_id = relay_sender_id;
    }

    public Map getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Map userDetails) {
        this.userDetails = userDetails;
    }

    public String getOld_relay_ip() {
        return old_relay_ip;
    }

    public void setOld_relay_ip(String old_relay_ip) {
        this.old_relay_ip = old_relay_ip;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    // end, code added by pr on 22ndjan18
    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public String getRelay_ip() {
        return relay_ip;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = Jsoup.parse(cert).text();
    }

    public String getHardwarecert() {
        return hardwarecert;
    }

    public void setHardwarecert(String hardwarecert) {
        this.hardwarecert = hardwarecert;
    }

    public void setRelay_ip(String relay_ip) {
        this.relay_ip = Jsoup.parse(relay_ip).text();
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

    public String getData() {
        return data;
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

    public String relay_tab1() {
        try {
            // System.out.println("data" + data);
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            ldap = new Ldap();
            System.out.println("form_details are ffffrfrfrff ::::::::" + form_details);
            boolean app_name_error = valid.nameValidation(form_details.getRelay_app_name());
            boolean app_url_error = valid.relayUrlValidation(form_details.getRelay_app_url());
            boolean division_error = valid.nameValidation(form_details.getDivision());
            boolean os_error = valid.addValidation(form_details.getOs());
            relay_sender_id = form_details.getRelay_sender_id();
            if (!form_details.getReq_().equals("req_delete")) {
                if (relay_sender_id == null) {
                    error.put("sender_id_error", "Please enter correct sender id, it should be exist in ldap");
                } else {
                    fetchMx();
                }
                System.out.println("domainMX" + domainMX);
                form_details.setDomain_mx(domainMX);
                boolean domain_mx_error = valid.vpnRemarksValidation(form_details.getDomain_mx());
                boolean sender_id_error = valid.EmailValidation(form_details.getRelay_sender_id());

                if (!ldap.emailValidate(form_details.getRelay_sender_id())) {
                    error.put("sender_id_error", "Please enter correct sender id, it should be exist in ldap.");
                } else {
                    if (sender_id_error == true) {
                        error.put("sender_id_error", "Enter Sender id in correct format");
                    }

                }
                System.out.println("inside not delete request");
                boolean total_mail_err = valid.webcastamountvalidation(form_details.getMailsent());
                boolean point_name_error = valid.nameValidation(form_details.getPoint_name());
                boolean point_email_error = valid.EmailValidation(form_details.getPoint_email());
                boolean point_mobile_error = valid.MobileValidation(form_details.getMobile_number());
                boolean point_tel_error = valid.telValidation(form_details.getLandline_number());
                boolean auth_id_error = valid.EmailValidation(form_details.getRelay_auth_id());
                if (point_name_error == true) {
                    error.put("p_name_error", "Please Enter Applicant Name [characters,dot(.) and whitespace]");
                    form_details.setPoint_name(ESAPI.encoder().encodeForHTML(form_details.getPoint_name()));
                }
                if (point_email_error == true) {
                    error.put("p_email_error", "Please Enter Applicant Email ");
                    form_details.setPoint_email(ESAPI.encoder().encodeForHTML(form_details.getPoint_email()));
                }
                if (point_mobile_error == true) {
                    error.put("p_mobile_error", "Please Enter Applicant Mobile [e.g:+919999999999]");
                    form_details.setMobile_number(ESAPI.encoder().encodeForHTML(form_details.getMobile_number()));
                }
                if (point_tel_error == true) {
                    error.put("p_tel_error", "Please Enter Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                    form_details.setLandline_number(ESAPI.encoder().encodeForHTML(form_details.getLandline_number()));
                }
                if (total_mail_err == true) {
                    error.put("total_mail_err", "Please enter total number of mails");
                    form_details.setMailsent(ESAPI.encoder().encodeForHTML(form_details.getMailsent()));
                }

                if (form_details.getRelay_sender_id() == "" || form_details.getRelay_sender_id() == null) {
                    error.put("sender_id_error", "Please Enter Correct Sender Id");
                    form_details.setRelay_sender_id(ESAPI.encoder().encodeForHTML(form_details.getRelay_sender_id()));
                }
                if (!form_details.getSmtp_port().equals("465") && !form_details.getSmtp_port().equals("25")) {
                    error.put("port_err", "Please Enter Correct port ");
                }
                if (form_details.getSmtp_port().equals("465")) {
                    if (form_details.getRelay_auth_id().equals("")) {
                        error.put("auth_id_error", "Please Enter Correct Auth Id");
                        form_details.setRelay_auth_id(ESAPI.encoder().encodeForHTML(form_details.getRelay_auth_id()));
                    } else {
                        if (!ldap.emailValidate(form_details.getRelay_auth_id())) {
                            error.put("auth_id_error", "Email address does not exist in ldap");
                        } else {
                            if (auth_id_error == true) {
                                error.put("auth_id_error", "Enter auth id in correct format");
                            }

                        }

                    }
                }
                System.out.println("fform_details.getSecurity_exp_date()" + form_details.getSecurity_exp_date() + "form_details.getIp_staging()" + form_details.getIp_staging());
                if (form_details.getSecurity_audit().equalsIgnoreCase("Software")) {

                    if (cert == null && form_details.getIp_staging() == null) {
                        form_details.setIp_staging("no");

                        if (!cert.equals("true")) {
                            error.put("file_err", "Please upload Security audit clearance certificate in PDF format only");
                        }

                    } else {
                        if (!cert.equals("true") && form_details.getIp_staging() == null) {

                            error.put("file_err", "Please upload Security audit clearance certificate in PDF format only");
                        }

                    }
                    if (form_details.getSecurity_exp_date() == "" && form_details.getIp_staging() == null) {

                        error.put("audit_date_err", "Please select Security Audit");
                    }

                } else if (form_details.getSecurity_audit().equalsIgnoreCase("Hardware")) {
                    System.out.println("hardwarecert::::" + hardwarecert);
                    if (hardwarecert == null && form_details.getIp_staging() == null) {
                        form_details.setIp_staging("no");
                        if (!hardwarecert.equals("true")) {
                            error.put("hardware_file_err", "Please exemption certificate in PDF format only");
                        }
                    } else {
                        if (hardwarecert != null && !hardwarecert.equals("true") && form_details.getIp_staging() == null) {
                            error.put("hardware_file_err", "Please exemption certificate in PDF format only");
                        }

                    }

                }

//                if (form_details.getIp_staging() == null) {
//                    System.out.println("ip staging:::::::::::" + form_details.getIp_staging());
//                    form_details.setIp_staging("no");
//                    System.out.println("cert::::::::" + cert);
//
//                } else {
//                    form_details.setIp_staging("yes");
//                    if (form_details.getSecurity_exp_date().equals("")) {
//                        error.put("audit_date_err", "Enter Security Audit Expiry Date");
//                        form_details.setDatepicker1(ESAPI.encoder().encodeForHTML(form_details.getDatepicker1()));
//                    }
//                }
                if (domain_mx_error == true) {
                    error.put("domain_mx_error", "Please enter MX of the Domain");
                    form_details.setDomain_mx(ESAPI.encoder().encodeForHTML(form_details.getDomain_mx()));
                }

                if (!form_details.getDomain_mx().equals("mailgwgov.nic.in") && !form_details.getDomain_mx().equals("mailgw.nic.in")) {
                    if (form_details.getSpf() == null && form_details.getDkim() == null && form_details.getDmarc() == null) {
                        error.put("record_err", "Please check other record addition");
                        form_details.setSpf(ESAPI.encoder().encodeForHTML(form_details.getSpf()));
                        form_details.setDkim(ESAPI.encoder().encodeForHTML(form_details.getDkim()));
                        form_details.setDmarc(ESAPI.encoder().encodeForHTML(form_details.getDmarc()));
                    }
                }

                if (sender_id_error == true) {
                    error.put("sender_id_error", "Please Enter Correct Sender Id");
                    form_details.setSender_id(ESAPI.encoder().encodeForHTML(form_details.getSender_id()));
                }
                form_details.setOld_relay_ip(old_relay_ip);
                if (form_details.getReq_().equals("req_add") || form_details.getReq_().equals("req_modify")) {
                    if (old_relay_ip.contains(";")) {
                        String[] relayip2 = getOld_relay_ip().split(";");
                        for (String ip : relayip2) {
                            boolean ip1_error = valid.baseipValidation(ip);
                            if (ip1_error == true) {
                                error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                                form_details.setOld_relay_ip(ESAPI.encoder().encodeForHTML(form_details.getOld_relay_ip()));
                            }
                        }
                    } else {
                        boolean ip1_error = valid.baseipValidation(old_relay_ip);
                        if (ip1_error == true) {
                            error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                            form_details.setOld_relay_ip(ESAPI.encoder().encodeForHTML(form_details.getOld_relay_ip()));
                        }
                    }

                }
            }

            form_details.setRelay_ip(relay_ip);
            System.out.println("relay_ip@@@@@@@" + relay_ip);
            if (relay_ip.contains(";")) {
                String[] relayip1 = getRelay_ip().split(";");
                for (String ip : relayip1) {
                    boolean ip1_error = valid.baseipValidation(ip);
                    if (ip1_error == true) {
                        error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                        form_details.setRelay_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_ip()));
                    }
                }
            } else {
                boolean ip1_error = valid.baseipValidation(relay_ip);
                if (ip1_error == true) {
                    error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                    form_details.setRelay_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_ip()));
                }
            }
            // if (form_details.getReq_().equals("req_modify")) {

            //  }
            //   boolean app_url_error = false;
            if (app_url_error == true) {
                error.put("app_url_error", "Enter Application URL, [characters only limit[50],dot(,),comma(,) whitespaces allowed]");
                form_details.setRelay_app_url(ESAPI.encoder().encodeForHTML(form_details.getRelay_app_url()));
            }
//            if (form_details.getRelay_app_url() == "" || form_details.getRelay_app_url() == null) {
//                error.put("app_url_error", "Please Enter Application Url");
//                form_details.setRelay_app_url(ESAPI.encoder().encodeForHTML(form_details.getRelay_app_url()));
//            }

            if (app_name_error == true) {
                error.put("app_name_error", "Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespaces allowed]");
                form_details.setRelay_app_name(ESAPI.encoder().encodeForHTML(form_details.getRelay_app_name()));
            }
            if (division_error == true) {
                error.put("division_error", "Enter Name of Division, [characters only limit[50], dot(,),comma(,) whitespaces allowed]");
                form_details.setDivision(ESAPI.encoder().encodeForHTML(form_details.getDivision()));
            }
            if (os_error == true) {
                error.put("os_error", "Enter Operating System (Name, Version), [Only characters limit[100],whitespaces,comma(,),hypen(-) allowed]");
                form_details.setOs(ESAPI.encoder().encodeForHTML(form_details.getOs()));
            }

            if (form_details.getServer_loc().equals("other")) {
                boolean server_txt_error = valid.addValidation(form_details.getServer_loc_txt());
                if (server_txt_error == true) {
                    error.put("server_txt_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                    form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                }
            } else {
                boolean server_loc_error = valid.addValidation(form_details.getServer_loc());
                if (server_loc_error == true) {
                    error.put("server_loc_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                    form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
                    form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                }
            }

            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }

            System.out.println("form_details.getSecurity_audit()" + form_details.getSecurity_audit() + "form_details.getSecurity_exp_date()" + form_details.getSecurity_exp_date());

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            // end, code added by pr on 22ndjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for relay controller tab 1: " + error);
            if (error.isEmpty()) {
                System.out.println("ip staging::::::" + form_details.getIp_staging());
                System.out.println("upload file name:::" + session.get("uploaded_filename"));
                if (session.get("uploaded_filename") != null && form_details.getIp_staging() == null) {
                    form_details.setIp_staging("no");
                    if (session.get("uploaded_filename") != null && form_details.getIp_staging().equals("no")) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    } else {
                        form_details.setUploaded_filename("");
                        form_details.setRenamed_filepath("");
                    }
                }
                if (session.get("hardware_uploaded_filename") != null && form_details.getSecurity_audit() != null) {
                    form_details.setIp_staging("no");
                    if (session.get("hardware_uploaded_filename") != null && form_details.getSecurity_audit().equalsIgnoreCase("hardware")) {
                        //String hardware_uploaded_filename = session.get("hardware_uploaded_filename").toString();
                        //String hardware_renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setHardware_uploaded_filename(session.get("hardware_uploaded_filename").toString());
                        form_details.setHardware_renamed_filepath(session.get("hardware_renamed_filepath").toString());
                    } else {
                        form_details.setHardware_uploaded_filename("");
                        form_details.setHardware_renamed_filepath("");
                    }
                }
                if (session.get("update_without_oldmobile").equals("no")) {
                    relayservice.RELAY_tab1(form_details, getRelay_ip());
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("relay_details", form_details);
                }
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
                relay_ip = "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for relay controller tab 1: " + ex.getMessage());
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
            relay_ip = "";
        }
        return SUCCESS;
    }

    public String relay_tab2() {
        try {
            db = new Database();
            ldap = new Ldap();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            error.clear();
            String staging_ip = "";

            if (!getAction_type().equals("update")) // if added by pr on 1stfeb18
            {
                form_details = (FormBean) session.get("relay_details");
                staging_ip = form_details.getIp_staging();

            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            profile_values = (HashMap) session.get("profile-values");
            form_details
                    = mapper.readValue(data, FormBean.class
                    );
            System.out.println("data for subbbbb::::::::::::::::::" + form_details);
            System.out.println("old_relay_ip::::::::" + old_relay_ip);
            form_details.setRelay_ip(relay_ip);
            form_details.setOld_relay_ip(old_relay_ip);
            form_details.setIp_staging(staging_ip);

            hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());

            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

            }
            boolean app_name_error = valid.nameValidation(form_details.getRelay_app_name());
            // boolean app_url_error = valid.relayUrlValidation(form_details.getRelay_app_url());
            boolean division_error = valid.nameValidation(form_details.getDivision());
            boolean os_error = valid.addValidation(form_details.getOs());

            System.out.println("form_details.getReq_()" + form_details.getReq_());
            if (!form_details.getReq_().equals("req_delete")) {
                boolean domain_mx_error = valid.vpnRemarksValidation(form_details.getDomain_mx());
                boolean total_mail_err = valid.webcastamountvalidation(form_details.getMailsent());
                boolean point_name_error = valid.nameValidation(form_details.getPoint_name());
                boolean point_email_error = valid.EmailValidation(form_details.getPoint_email());
                boolean point_mobile_error = valid.MobileValidation(form_details.getMobile_number());
                boolean point_tel_error = valid.telValidation(form_details.getLandline_number());
                boolean auth_id_error = valid.EmailValidation(form_details.getRelay_auth_id());
                boolean sender_id_error = valid.EmailValidation(form_details.getRelay_sender_id());
                if (form_details.getSmtp_port().equals("465")) {
                    if (form_details.getRelay_auth_id().equals("")) {
                        error.put("auth_id_error", "Please Enter Correct Auth Id");
                        form_details.setRelay_auth_id(ESAPI.encoder().encodeForHTML(form_details.getRelay_auth_id()));
                    } else {
                        if (!ldap.emailValidate(form_details.getRelay_auth_id())) {
                            error.put("auth_id_error", "Email address does not exist in ldap");
                        } else {
                            if (auth_id_error == true) {
                                error.put("auth_id_error", "Enter auth id in correct format");
                            }

                        }

                    }
                }
                if (domain_mx_error == true) {
                    error.put("domain_mx_error", "Please enter MX of the Domain");
                    form_details.setDomain_mx(ESAPI.encoder().encodeForHTML(form_details.getDomain_mx()));
                }
                if (total_mail_err == true) {
                    error.put("total_mail_err", "Please enter total number of mails");
                    form_details.setDomain_mx(ESAPI.encoder().encodeForHTML(form_details.getDomain_mx()));
                }
                if (point_name_error == true) {
                    error.put("p_name_error", "Please Enter Applicant Name [characters,dot(.) and whitespace]");
                    form_details.setPoint_name(ESAPI.encoder().encodeForHTML(form_details.getPoint_name()));
                }
                if (point_email_error == true) {
                    error.put("p_email_error", "Please Enter Applicant Email ");
                    form_details.setPoint_email(ESAPI.encoder().encodeForHTML(form_details.getPoint_email()));
                }
                if (point_mobile_error == true) {
                    error.put("p_mobile_error", "Please Enter Applicant Mobile [e.g:+919999999999]");
                    form_details.setMobile_number(ESAPI.encoder().encodeForHTML(form_details.getMobile_number()));
                }
                if (point_tel_error == true) {
                    error.put("p_tel_error", "Please Enter Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                    form_details.setLandline_number(ESAPI.encoder().encodeForHTML(form_details.getLandline_number()));
                }
                if (form_details.getReq_().equals("req_add") || form_details.getReq_().equals("req_modify")) {
                    if (old_relay_ip.contains(";")) {
                        String[] relayip2 = getOld_relay_ip().split(";");
                        for (String ip : relayip2) {
                            boolean ip1_error = valid.baseipValidation(ip);
                            if (ip1_error == true) {
                                error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                                form_details.setOld_relay_ip(ESAPI.encoder().encodeForHTML(form_details.getOld_relay_ip()));
                            }
                        }
                    } else {
                        boolean ip1_error = valid.baseipValidation(old_relay_ip);
                        if (ip1_error == true) {
                            error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                            form_details.setOld_relay_ip(ESAPI.encoder().encodeForHTML(form_details.getOld_relay_ip()));
                        }
                    }
                }
                relay_sender_id = form_details.getRelay_sender_id();
                fetchMx();
                if (!ldap.emailValidate(form_details.getRelay_sender_id())) {
                    error.put("sender_id_error", "Please enter correct sender id, it should be exist in ldap.");
                } else {
                    if (sender_id_error == true) {
                        error.put("sender_id_error", "Enter Sender id in correct format");
                    }

                }
            }

            if (relay_ip.contains(";")) {
                String[] relayip1 = getRelay_ip().split(";");
                for (String ip : relayip1) {
                    boolean ip1_error = valid.baseipValidation(ip);
                    if (ip1_error == true) {
                        error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                        form_details.setRelay_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_ip()));
                    }
                }
            } else {
                boolean ip1_error = valid.baseipValidation(relay_ip);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ip1 err: " + ip1_error);
                if (ip1_error == true) {
                    error.put("ip1_error", "Enter the IP Address [e.g.: 164.100.X.X ]");
                    form_details.setRelay_ip(ESAPI.encoder().encodeForHTML(form_details.getRelay_ip()));
                }

            }

            //if (form_details.getReq_().equals("req_modify")) {
            //}
            if (app_name_error == true) {
                error.put("app_name_error", "Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespaces allowed]");
                form_details.setRelay_app_name(ESAPI.encoder().encodeForHTML(form_details.getRelay_app_name()));
            }
            if (division_error == true) {
                error.put("division_error", "Enter Name of Division, [characters only limit[50], dot(,),comma(,) whitespaces allowed]");
                form_details.setDivision(ESAPI.encoder().encodeForHTML(form_details.getDivision()));
            }
            if (os_error == true) {
                error.put("os_error", "Enter Operating System (Name, Version), [Only characters limit[100],whitespaces,comma(,),hypen(-) allowed]");
                form_details.setOs(ESAPI.encoder().encodeForHTML(form_details.getOs()));
            }
            if (form_details.getServer_loc().equals("other")) {
                boolean server_txt_error = valid.addValidation(form_details.getServer_loc_txt());
                if (server_txt_error == true) {
                    error.put("server_txt_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                    form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                }
            } else {
                boolean server_loc_error = valid.addValidation(form_details.getServer_loc());
                if (server_loc_error == true) {
                    error.put("server_loc_error", "Enter Server Location [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                    form_details.setServer_loc(ESAPI.encoder().encodeForHTML(form_details.getServer_loc()));
                    form_details.setServer_loc_txt(ESAPI.encoder().encodeForHTML(form_details.getServer_loc_txt()));
                }
            }

            // start, code added by pr on 22ndjan18
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
            // hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
            if (form_details.getModule() == null) {
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.roTelValidation(form_details.getHod_tel());
                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                }
                if (hod_name_error == true) {
                    error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
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
//                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
//                    error.put("hod_email_error", "Please change it in your profile and then come back");
//
//                }

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
                        // ArrayList b = null;
                        Set s = (Set) userdata.getAliases();

                        System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);

                        ArrayList<String> newAr = new ArrayList<String>();

                        newAr.addAll(s);
                        ArrayList email_aliases = null;
                        email_aliases = newAr;
                        if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {    // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                            // b = (ArrayList) a.get("mailequi");
                            // b = (ArrayList) userdata.getAliases();
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
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in imap controller tab 2");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {

                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in imap controller tab 2");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    // DONE BY NIKKI MEENAXI ON 16 feb 
                                    //  hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
                                }
                            }
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
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in imap controller tab 2");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in imap controller tab 2");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                        error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                    }

                                }
                            } else {
                                // DONE BY NIKKI MEENAXI ON 16 feb 
                                // hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
                            }
                        }
                    }
                }
            }
            // end validate profile 20th march

            if (ldap_employee) {

                System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                if (hodDetails.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Reporting officer should be govt employee");
                }

            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for relay controller tab 2: " + error);

            // end, code added by pr on 22ndjan18    
            if (getAction_type().equals("submit")) {
                if (session.get("uploaded_filename") != null && form_details.getIp_staging() != null) {
                    if (session.get("uploaded_filename") != null) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    } else {
                        String uploaded_filename = "";
                        String renamed_filepath = "";
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    }
                }
                if (session.get("hardware_uploaded_filename") != null && form_details.getSecurity_audit() != null) {

                    if (session.get("hardware_uploaded_filename") != null && form_details.getSecurity_audit().equalsIgnoreCase("hardware")) {
                        //String hardware_uploaded_filename = session.get("hardware_uploaded_filename").toString();
                        //String hardware_renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setHardware_uploaded_filename(session.get("hardware_uploaded_filename").toString());
                        form_details.setHardware_renamed_filepath(session.get("hardware_renamed_filepath").toString());
                    } else {
                        form_details.setHardware_uploaded_filename("");
                        form_details.setHardware_renamed_filepath("");
                    }
                }
                if (!error.isEmpty()) {
                    data = "";
                    cert = "";
                    CSRFRandom = "";
                    action_type = "";
                    relay_ip = "";
                } else {
                    profile_values = (HashMap) session.get("profile-values");
                    if (session.get("update_without_oldmobile").equals("no")) {
                        String ref_num = relayservice.RELAY_tab2(form_details, getRelay_ip(), getOld_relay_ip(), profile_values);
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
                        session.put("relay_details", form_details);
                        session.put("ref_num", ref_num);
                    }
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    cert = "";
                    CSRFRandom = "";
                    action_type = "";
                    relay_ip = "";
                }
            } else if (getAction_type().equals("update")) // else if added by pr on 1stfeb18
            {
                if (error.isEmpty()) {
                    // String uploaded_filename = session.get("uploaded_filename").toString();
                    //String renamed_filepath = session.get("renamed_filepath").toString();
                    //form_details.setUploaded_filename(uploaded_filename);
                    //form_details.setRenamed_filepath(renamed_filepath);
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
                cert = "";
                CSRFRandom = "";
                action_type = "";
                relay_ip = "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for relay controller tab 2: " + ex.getMessage());
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
            relay_ip = "";
        }

        return SUCCESS;
    }

    public String relay_tab6() {

        try {
            String ref_num = session.get("ref_num").toString();
            if (form_details.getTelnet().equals("y") || form_details.getTelnet().equals("n")) {
                relayservice.insertTelnet(form_details.getTelnet(), ref_num);
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
                relay_ip = "";
                form_details.setTelnet(ESAPI.encoder().encodeForHTML(form_details.getTelnet()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
            relay_ip = "";
            form_details.setTelnet(ESAPI.encoder().encodeForHTML(form_details.getTelnet()));
        }

        return SUCCESS;
    }

    public String relay_tab7() {

        try {
            String ref_num = session.get("ref_num").toString();
            if (form_details.getFirewall_id().matches("^[a-zA-Z0-9 \\/\\-\\,\\.\\(\\)]{1,50}$")) {
                relayservice.insertFirewallId(form_details.getFirewall_id(), ref_num);
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
                relay_ip = "";
                form_details.setFirewall_id(ESAPI.encoder().encodeForHTML(form_details.getFirewall_id()));
                error.put("firewall_err", "Please enter Firewall request ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
            relay_ip = "";
            form_details.setFirewall_id(ESAPI.encoder().encodeForHTML(form_details.getFirewall_id()));
        }

        return SUCCESS;
    }

    public String filling_relay_tab() {
        String json = null;

        UserData userdata = (UserData) session.get("uservalues");
        profile_values = (HashMap) session.get("profile-values");
        userDetails.put("name", userdata.getName());
        userDetails.put("email", userdata.getEmail());
        userDetails.put("mobile", userdata.getMobile());
        userDetails.put("ophone", userdata.getTelephone());
        System.out.println("inside filling_relay_tab" + userdata.getEmail());
        return SUCCESS;

    }

    public static String[] lookupMailHosts(String domainName) throws NamingException {
        InitialDirContext iDirC = new InitialDirContext();
        Attributes attributes = iDirC.getAttributes("dns:/" + domainName, new String[]{"MX"});
        Attribute attributeMX = attributes.get("MX");
        if (attributeMX == null) {
            return (new String[]{domainName});
        }
        String[][] pvhn = new String[attributeMX.size()][2];
        for (int i = 0; i < attributeMX.size(); i++) {
            pvhn[i] = ("" + attributeMX.get(i)).split("\\s+");
        }
        Arrays.sort(pvhn, new Comparator<String[]>() {
            public int compare(String[] o1, String[] o2) {
                return (Integer.parseInt(o1[0]) - Integer.parseInt(o2[0]));
            }
        });
        String[] sortedHostNames = new String[pvhn.length];
        for (int i = 0; i < pvhn.length; i++) {
            sortedHostNames[i] = pvhn[i][1].endsWith(".")
                    ? pvhn[i][1].substring(0, pvhn[i][1].length() - 1) : pvhn[i][1];
        }
        return sortedHostNames;
    }

    public String fetchMx() {

        ArrayList<String> domaindata = new ArrayList<>();
        boolean sender_id_error = valid.EmailValidation(relay_sender_id);
        ldap = new Ldap();
        if (!ldap.emailValidate(relay_sender_id)) {
            error.put("sender_id_error", "Please enter correct sender id, it should be exist in ldap.");
        } else {
            if (sender_id_error == true) {
                error.put("sender_id_error", "Enter Sender id in correct format");
            } else {
                if (relay_sender_id.contains("@")) {
                    String[] mle = relay_sender_id.split("@");
                    String domain = LdapQuery.GetTotalDomain(); // line added by pr on 4thdec19
                    domain.replace("associateddomain: ", "");
                    String[] domainArr = domain.split(","); // line added by pr on 4thdec19
                    //domaindata = (ArrayList) Arrays.asList(domainArr);
                    domaindata = new ArrayList<>(Arrays.asList(domainArr));
                    try {
                        String[] mxDomain = lookupMailHosts(mle[1]);
                        domainMX = mxDomain[0];

                        System.out.println("domain mx:" + domainMX);

                    } catch (NamingException ex) {
                        Logger.getLogger(Relay_registration.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        }

        return SUCCESS;
    }

}
