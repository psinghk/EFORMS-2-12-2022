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
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.LdapService;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import org.xml.sax.SAXException;
import validation.validation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Nikki
 */
public class Ldap_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    Map session;
    Map profile_values = null;
    LdapService ldapservice = null;
    UpdateService updateService = null;// line added by pr on 1stfeb18
    String data, cert, CSRFRandom, app_url, returnString; // CSRFRandom added by pr on 22ndjan18
    ProfileService profileService = null;
    Map hodDetails = null;
    private Database db = null;
    Map<String, String> map = null;

    validation valid = null;
    Map<String, Object> error = null;
    String action_type;
    Ldap ldap;
    

    public Ldap_registration() {
        ldapservice = new LdapService();
        valid = new validation();
        error = new HashMap();
        updateService = new UpdateService();
        map = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        profile_values = new HashMap();
        ldap = new Ldap();
        if (ldapservice == null) {
            ldapservice = new LdapService();
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
        if (map == null) {
            map = new HashMap();
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

    public String fetchCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = Jsoup.parse(cert).text();
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

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
        this.action_type = action_type;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    HttpServletResponse response = ServletActionContext.getResponse();

    public String Ldap_tab1() {
        try {
            error.clear();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DATA: "+ data);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "APP NAME: "+ form_details.getApp_name());
            boolean app_name_error = valid.nameValidation(form_details.getApp_name());
            boolean app_url_error = valid.pullurlValidation(form_details.getApp_url());
            boolean ip1_error = valid.baseipValidation(form_details.getBase_ip());
            boolean ip2_error = valid.serviceipValidation(form_details.getService_ip());
            boolean domain_error = valid.addValidation(form_details.getDomain());

            if (form_details.getServer_loc().equals("Other")) {
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
            if (app_name_error == true) {
                error.put("app_name_error", "Enter Name of the Applicaion [characters,dot(.) and whitespaces],limit 1-50");
                form_details.setApp_name(ESAPI.encoder().encodeForHTML(form_details.getApp_name()));
            }
            if (app_url_error == true) {
                error.put("app_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                form_details.setApp_url(ESAPI.encoder().encodeForHTML(form_details.getApp_url()));
            }
            if (ip1_error == true) {
                error.put("ip1_error", "Enter Application IP1 [e.g: 10.10.10.10]");
                form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
            }
            if (ip2_error == true) {
                error.put("ip2_error", "Enter Application IP2 [e.g: 10.10.10.10]");
                form_details.setService_ip(ESAPI.encoder().encodeForHTML(form_details.getService_ip()));
            }
            if (domain_error == true) {
                error.put("domain_error", "Enter Domain/Group Of People who will access this application,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                form_details.setDomain(ESAPI.encoder().encodeForHTML(form_details.getDomain()));
            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }

            if (form_details.getAudit().equals("yes")) {
                if (!cert.equals("true")) {
                    error.put("file_err", "Please upload Security audit clearance certificate in PDF format only");
                    form_details.setAudit(ESAPI.encoder().encodeForHTML(form_details.getAudit()));
                }
            } else if (form_details.getAudit().equals("no")) {
                boolean email1_error = valid.EmailValidation(form_details.getLdap_id1());
                if (email1_error) {
                    error.put("specific_id1_err", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                    form_details.setLdap_id1(ESAPI.encoder().encodeForHTML(form_details.getLdap_id1()));
                }
                if (form_details.getLdap_id2().equals("") || form_details.getLdap_id2() == null) {

                } else {
                    boolean email2_error = valid.EmailValidation(form_details.getLdap_id2());
                    if (email2_error) {
                        error.put("specific_id2_err", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                        form_details.setLdap_id2(ESAPI.encoder().encodeForHTML(form_details.getLdap_id2()));
                    }
                }
            } else {
                error.put("audit_err", "Please select correct option");
                form_details.setLdap_id1(ESAPI.encoder().encodeForHTML(form_details.getLdap_id1()));
                form_details.setLdap_id2(ESAPI.encoder().encodeForHTML(form_details.getLdap_id2()));
                form_details.setAudit(ESAPI.encoder().encodeForHTML(form_details.getAudit()));
            }

            if (!form_details.getHttps().equals("yes")) {
                error.put("https_err", "Please select correct option");
                form_details.setHttps(ESAPI.encoder().encodeForHTML(form_details.getHttps()));
            }

            // start, code added by pr on 10thjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for ldap controller tab 1: " + error);
            // end, code added by pr on 10thjan18
            if (error.isEmpty()) {
                if (session.get("uploaded_filename") != null && form_details.getAudit().equals("yes")) {
                    String uploaded_filename = session.get("uploaded_filename").toString();
                    String renamed_filepath = session.get("renamed_filepath").toString();
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                } else {
                    form_details.setUploaded_filename("");
                    form_details.setRenamed_filepath("");
                }
                if (session.get("update_without_oldmobile").equals("no")) {
                    ldapservice.Ldap_tab1(form_details);
                    profile_values = (HashMap) session.get("profile-values");
                    System.out.println("profile values::::::::: in ldap1" + profile_values);
                }
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in ldap controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";

        }
        return SUCCESS;
    }

    public String Ldap_tab2() {
        try {
            db = new Database();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
             String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
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
            boolean app_name_error = valid.nameValidation(form_details.getApp_name());
            boolean app_url_error = valid.pullurlValidation(form_details.getApp_url());
            boolean ip1_error = valid.baseipValidation(form_details.getBase_ip());
            boolean ip2_error = valid.serviceipValidation(form_details.getService_ip());
            boolean domain_error = valid.addValidation(form_details.getDomain());

            if (form_details.getServer_loc().equals("Other")) {
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
            if (app_name_error == true) {
                error.put("app_name_error", "Enter Name of the Applicaion [characters,dot(.) and whitespaces],limit 1-50");
                //form_details.setApp_name(ESAPI.encoder().encodeForHTML(form_details.getApp_name()));
            }
            if (app_url_error == true) {
                error.put("app_url_error", "Enter Application URL [e.g: (https://abc.com)]");
                form_details.setApp_url(ESAPI.encoder().encodeForHTML(form_details.getApp_url()));
            }
            if (ip1_error == true) {
                error.put("ip1_error", "Enter Application IP1 [e.g: 10.10.10.10]");
                form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
            }
            if (ip2_error == true) {
                error.put("ip2_error", "Enter Application IP2 [e.g: 10.10.10.10]");
                form_details.setService_ip(ESAPI.encoder().encodeForHTML(form_details.getService_ip()));
            }
            if (domain_error == true) {
                error.put("domain_error", "Enter Domain/Group Of People who will access this application,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                form_details.setDomain(ESAPI.encoder().encodeForHTML(form_details.getDomain()));
            }

            if (form_details.getAudit().equals("yes")) {
//                if (!cert.equals("true")) {
//                    error.put("file_err", "Please upload Security audit clearance certificate in PDF format only");
//                    form_details.setAudit(ESAPI.encoder().encodeForHTML(form_details.getAudit()));
//                }
            } else if (form_details.getAudit().equals("no")) {
                boolean email1_error = valid.EmailValidation(form_details.getLdap_id1());
                if (email1_error) {
                    error.put("specific_id1_err", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                    form_details.setLdap_id1(ESAPI.encoder().encodeForHTML(form_details.getLdap_id1()));
                }
                if (form_details.getLdap_id2().equals("") || form_details.getLdap_id2() == null) {

                } else {
                    boolean email2_error = valid.EmailValidation(form_details.getLdap_id2());
                    if (email2_error) {
                        error.put("specific_id2_err", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                        form_details.setLdap_id2(ESAPI.encoder().encodeForHTML(form_details.getLdap_id2()));
                    }
                }
            } else {
                form_details.setLdap_id1(ESAPI.encoder().encodeForHTML(form_details.getLdap_id1()));
                form_details.setLdap_id2(ESAPI.encoder().encodeForHTML(form_details.getLdap_id2()));
                form_details.setAudit(ESAPI.encoder().encodeForHTML(form_details.getAudit()));
            }

            if (!form_details.getHttps().equals("yes")) {
                error.put("https_err", "Please select correct option");
                form_details.setHttps(ESAPI.encoder().encodeForHTML(form_details.getHttps()));
            }

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            // end, code added by pr on 22ndjan18
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
            System.out.println("form_details.getHod_email()" + form_details.getHod_email());
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            System.out.println("hodDetails##############" + hodDetails);
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
                            //if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                            //b = (ArrayList) userdata.getAliases();
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
                                // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                            }
                        }
                    }
                }
            }
            if (ldap_employee) {

                System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                if (hodDetails.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Reporting officer should be govt employee");
                }

            }
            // end validate profile 20th march
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for ldap controller tab 2: " + error);

            if (getAction_type().equals("submit")) {
                if (session.get("uploaded_filename") != null) {
                    if (form_details.getAudit().equals("yes")) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    } else {
                        form_details.setUploaded_filename("");
                        form_details.setRenamed_filepath("");
                    }
                } else {
                    String uploaded_filename = "";
                    String renamed_filepath = "";
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                }
                if (!error.isEmpty()) {
                    data = "";
                    cert = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("ldap_details", form_details);
                    if (session.get("update_without_oldmobile").equals("no")) {
                        String ref_num = ldapservice.Ldap_tab2(form_details, profile_values);
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
                    }
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    cert = "";
                    CSRFRandom = "";
                    action_type = "";
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
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in ldap controlloer tab 2: " + e.getMessage());
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String Ldap_tab3() {
        try {
            String ref_num = session.get("ref_num").toString();
            if (form_details.getTelnet().equals("y") || form_details.getTelnet().equals("n")) {
                ldapservice.insertTelnet(form_details.getTelnet(), ref_num);
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
                form_details.setTelnet(ESAPI.encoder().encodeForHTML(form_details.getTelnet()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
            form_details.setTelnet(ESAPI.encoder().encodeForHTML(form_details.getTelnet()));
        }

        return SUCCESS;
    }

    public String Ldap_tab4() {
        try {
            String ref_num = session.get("ref_num").toString();
            if (form_details.getFirewall_id().matches("^[a-zA-Z0-9]{1,50}$")) {
                ldapservice.insertFirewallId(form_details.getFirewall_id(), ref_num);
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
                form_details.setFirewall_id(ESAPI.encoder().encodeForHTML(form_details.getFirewall_id()));
                error.put("firewall_err", "Please enter Firewall request ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";
            form_details.setFirewall_id(ESAPI.encoder().encodeForHTML(form_details.getFirewall_id()));
        }

        return SUCCESS;
    }

    public String verify_url() {
        String s = "";

        try {
            URL url = new URL(app_url);
            String domain = url.getHost();

            s = audit(domain);
            //s = "Under Audit";
            if (s.contains("Under Audit")) {
                returnString = "noaudit";

            } else if (s.contains("Not Audited")) {
                returnString = "noaudit";
            } else {
                returnString = "audit";
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Ldap_registration.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("s::::::"+s);
        return SUCCESS;
    }

    public static String audit(String empno) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        URL url;
        String ret_val = "";
        //String http = "https://personnel.nic.in/webservice_ldap/emdmaster.php?processid=EmployeeMaster&var=" + empno;
        String http = "https://personnel.nic.in/wcarws/wcar_data_service.php?format=xml&url=" + empno;
        try {

            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] trust_mgr = get_trust_mgr();
            ssl_ctx.init(null,
                    trust_mgr,
                    new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx
                    .getSocketFactory());

            url = new URL(http);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            //con.setRequestMethod("HEAD");
            con.setConnectTimeout(10000); //set timeout to 10 seconds
            String res = print_content(con);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            InputSource is;
            try {
                builder = factory.newDocumentBuilder();
                is = new InputSource(new StringReader(res));
                Document doc = builder.parse(is);

                System.out.println("output ::::::" + doc);
                NodeList audit_status = doc.getElementsByTagName("audit_status");
                //System.out.println(audit_status.item(0).getTextContent());
                if (audit_status.item(0).getTextContent() != null) {
                    ret_val = audit_status.item(0).getTextContent();
                }
            } catch (ParserConfigurationException e) {
            } catch (SAXException e) {
            } catch (IOException e) {
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return ret_val;
    }

    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        }};
        return certs;
    }

    private static String print_content(HttpsURLConnection con) {
        String res = "";
        if (con != null) {

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null) {

                    res += input;
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

}
