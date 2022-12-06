package com.org.controller;

import admin.ForwardAction;
import admin.UserTrack;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.dao.Ldap;
import com.org.service.SignUpService;
import com.org.service.esignService;
import com.org.utility.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import utility.Inform;

/**
 *
 * @author Ashwini
 */
public class e_signing extends ActionSupport implements SessionAware {

    private static final String niccert = ServletActionContext.getServletContext().getInitParameter("niccert");
    FormBean form_details;
    String check, formtype, aadhaar, otp;
    esignService esignService = null;
    Map session;
    Map<String, Object> consentreturn = null;
    Map esignreturn = null;
    String otpErrMsg;
    private UserData userValues;
    Map profile_values = null;
    private SignUpService signUpService = null;
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    private String CSRFRandom;
    private Map<String, Object> hmTrack = null;

    public Map<String, Object> getHmTrack() {
        return hmTrack;
    }

    public void setHmTrack(Map<String, Object> hmTrack) {
        this.hmTrack = hmTrack;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;
    }

    public e_signing() {
        consentreturn = new HashMap<>();
        esignService = new esignService();
        profile_values = new HashMap();
        esignreturn = new HashMap();
        if (consentreturn == null) {
            consentreturn = new HashMap<>();
        }
        if (esignService == null) {
            esignService = new esignService();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
        if (esignreturn == null) {
            esignreturn = new HashMap();
        }
    }

    // start, code added by pr on 12thapr18
    String ref_num = "";

    public String getRef_num() {
        return ref_num;
    }

    public void setRef_num(String ref_num) {
        this.ref_num = ref_num;
    }

    // end, code added by pr on 12thapr18
    private InputStream fileInputStream;

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    String esignedfilename;

    public String getEsignedfilename() {
        return esignedfilename;
    }

    public void setEsignedfilename(String esignedfilename) {
        this.esignedfilename = Jsoup.parse(esignedfilename).text();
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = Jsoup.parse(check).text();
    }

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = Jsoup.parse(formtype).text();
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = Jsoup.parse(aadhaar).text();
    }

    public Map<String, Object> getConsentreturn() {
        return consentreturn;
    }

    public void setConsentreturn(Map<String, Object> consentreturn) {
        this.consentreturn = consentreturn;
    }

    public Map getEsignreturn() {
        return esignreturn;
    }

    public void setEsignreturn(Map esignreturn) {
        this.esignreturn = esignreturn;
    }

    public String getOtpErrMsg() {
        return otpErrMsg;
    }

    public void setOtpErrMsg(String otpErrMsg) {
        this.otpErrMsg = Jsoup.parse(otpErrMsg).text();
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = Jsoup.parse(otp).text();
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String Esign() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside Esign" + formtype);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside Esign check::::::::::::" + check);
        UserData userdata = (UserData) session.get("uservalues");
        userValues = (UserData) session.get("uservalues");
        profile_values = (Map) session.get("profile-values");
        boolean ldap_employee = false;
        boolean nic_employee = false;
        if (userdata != null) {
            ldap_employee = userdata.isIsEmailValidated();
            nic_employee = userdata.isIsNICEmployee();
        }

        if (session.get("esign_submit") != null) {
            session.remove("esign_submit");
        }

        //update form_text to form_type 
        session.put("esign_submit", formtype);

        if (formtype.equals("dns")) {
            session.put("form_text", "Domain Name System Services");
        } else if (formtype.equals("sms")) {
            session.put("form_text", "Short Messaging Services");
        } else if (formtype.equals("wifi")) {
            session.put("form_text", "Wi-Fi Services");
        } else if (formtype.equals("single") || formtype.equals("gem") || formtype.equals("bulk") || formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
            session.put("form_text", "Email Services for Government of India");
        } else if (formtype.equals("imappop")) {
            session.put("form_text", "Enable or Disable IMAP/POP");
        } else if (formtype.equals("mobile")) {
            session.put("form_text", "Update Your Profile/Mobile Number");
        } else if (formtype.equals("ldap")) {
            session.put("form_text", "Authentication Services (LDAP)");
        } else if (formtype.equals("distributionlist")) {
            session.put("form_text", "Distribution List Services");
        } else if (formtype.equals("bulkdistributionlist")) {
            session.put("form_text", "Distribution List Services");
        } else if (formtype.equals("ip")) {
            session.put("form_text", "Add/Change an IP for other services");
        } else if (formtype.equals("relay")) {
            session.put("form_text", "SMTP Gateway Services (Relay)");
        } else if (formtype.equals("vpn_single") || formtype.equals("vpn_bulk") || formtype.equals("vpn_renew") || formtype.equals("change_add") || formtype.equals("vpn_surrender")) {
            session.put("form_text", "Virtual Private Network Services");
        } else if (formtype.equals("webcast")) {
            session.put("form_text", "Webcast Services");
        } else if (formtype.equals("centralutm")) {
            session.put("form_text", "Central UTM Services");
        } else if (formtype.equals("wifiport")) {
            session.put("form_text", "Wi-Fi Port Services");
        } else if (formtype.equals("email_act")) {
            session.put("form_text", "Email Activate Services");
        } else if (formtype.equals("email_deact")) {
            session.put("form_text", "Email Deactivate Services");
        } else if (formtype.equals("dor_ext")) {
            session.put("form_text", "DOR-EXT Services");
        } else if (formtype.equals("dor_ext_retired")) {
            session.put("form_text", "DOR-EXT For Retired Officials Services");
        } else if (formtype.equals("daonboarding")) {
            session.put("form_text", "DA Onboarding Services");
        } else {
            formtype = "";
        }
        // done for gmail id 23 feb
        // redone for gmail id 16 march
        String hod_email = "";
        Map profile_values = (HashMap) session.get("profile-values");
        if (formtype.equals("gem")) {
            form_details = (FormBean) session.get(formtype + "_details");
            // String user_bo = session.get("user_bo").toString();
            if (form_details != null) {
                if (nic_employee) {
                    hod_email = form_details.getHod_email();
                } else {
                    hod_email = form_details.getFwd_ofc_email();
                }
            }
        } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
            form_details = (FormBean) session.get("nkn_details");
            hod_email = profile_values.get("hod_email").toString();
        } else if (formtype.equals("distributionlist")) {
            form_details = (FormBean) session.get("dlist_details");
            hod_email = profile_values.get("hod_email").toString();
        } else if (formtype.equals("bulkdistributionlist")) {
            form_details = (FormBean) session.get("dlist_details");
            hod_email = profile_values.get("hod_email").toString();
        } else if (formtype.equals("vpn_single") || formtype.equals("vpn_bulk") || formtype.equals("vpn_renew") || formtype.equals("change_add") || formtype.equals("vpn_surrender")) {
            form_details = (FormBean) session.get("vpn_details");
            String all_aliases = userdata.getAliasesInString().replaceAll("'", "\"");
            if (form_details != null) {
                form_details.setMail(all_aliases);
                Map vpn_data = (HashMap) session.get("vpn_data");
                form_details.setVpn_data(vpn_data);
            }
            hod_email = profile_values.get("hod_email").toString();

        } else {
            form_details = (FormBean) session.get(formtype + "_details");
            hod_email = profile_values.get("hod_email").toString();

        }
        session.put("esign_hod_email", hod_email);
        session.put("form_type", formtype);
        session.put("admin_role", "user");
        session.put("check", check);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside Esign admin role is::::" + session.get("admin_role"));
        return SUCCESS;
    }

    public String Consent() {
        String SessionCSRFRandom = session.get("CSRFRandom").toString();

//        if(!CSRFRandom.equals(SessionCSRFRandom)) {
//            hmTrack.put("csrf_error", "CSRF Token is invalid.");
//            return SUCCESS;
//        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside consent check::::::::::::" + check);
        UserData userdata = (UserData) session.get("uservalues");
        Inform inform = new Inform();
        boolean ldap_employee = false;
        boolean nic_employee = false;
        boolean isHog = false;
        boolean isHod = false;
        if (userdata != null) {
            ldap_employee = userdata.isIsEmailValidated();
            if (formtype.equalsIgnoreCase("dor_ext_retired")) {
                nic_employee = false;
                isHog = false;
                isHod = false;
            } else {
                nic_employee = userdata.isIsNICEmployee();
                isHog = userdata.isIsHOG();
                isHod = userdata.isIsHOD();
            }
        }
        try {
            //if (formtype.matches("^[a-zA-Z\\_]{2,20}$") && check.matches("^[a-zA-Z]{2,20}$")) {
            String ref_num = (String) session.get("ref_num");
            System.out.println("inside consentv :::::: formtype" + formtype);
            if (formtype.equals("dns")) {
                form_details = (FormBean) session.get("dns_details");
                System.out.println("inside consentv :::::: formtype" + form_details.getDns_url());
            } else if (formtype.equals("sms")) {
                form_details = (FormBean) session.get("sms_details");
            } else if (formtype.equals("wifi")) {
                form_details = (FormBean) session.get("wifi_details");
            } else if (formtype.equals("wifiport")) {
                form_details = (FormBean) session.get("wifiport_details");
            } else if (formtype.equals("single")) {
                form_details = (FormBean) session.get("single_details");
            } else if (formtype.equals("imappop")) {
                form_details = (FormBean) session.get("imappop_details");
            } else if (formtype.equals("mobile")) {
                form_details = (FormBean) session.get("mobile_details");
            } else if (formtype.equals("ldap")) {
                form_details = (FormBean) session.get("ldap_details");
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                form_details = (FormBean) session.get("nkn_details");
            } else if (formtype.equals("gem")) {
                form_details = (FormBean) session.get("gem_details");
            } else if (formtype.equals("bulk")) {
                form_details = (FormBean) session.get("bulk_details");
            } else if (formtype.equals("distributionlist")) {
                form_details = (FormBean) session.get("dlist_details");
            } else if (formtype.equals("bulkdistributionlist")) {
                form_details = (FormBean) session.get("dlist_details");
            } else if (formtype.equals("ip")) {
                form_details = (FormBean) session.get("ip_details");
            } else if (formtype.equals("relay")) {
                form_details = (FormBean) session.get("relay_details");
            } else if (formtype.equals("vpn_single") || formtype.equals("vpn_bulk") || formtype.equals("vpn_renew") || formtype.equals("change_add") || formtype.equals("vpn_surrender")) {
                System.out.println("inside condition of vpn surrender");
                form_details = (FormBean) session.get("vpn_details");
                String all_aliases = userdata.getAliasesInString().replaceAll("'", "\"");
                if (form_details != null) {
                    form_details.setMail(all_aliases);
                    Map vpn_data = (HashMap) session.get("vpn_data");
                    form_details.setVpn_data(vpn_data);
                }
            } else if (formtype.equals("vpn_single") || formtype.equals("vpn_bulk") || formtype.equals("vpn_renew") || formtype.equals("change_add") || formtype.equals("vpn_surrender")) {
                form_details = (FormBean) session.get("vpn_details");
                String all_aliases = userdata.getAliasesInString().replaceAll("'", "\"");
                if (form_details != null) {
                    form_details.setMail(all_aliases);
                    Map vpn_data = (HashMap) session.get("vpn_data");
                    form_details.setVpn_data(vpn_data);
                }
            } else if (formtype.equals("webcast")) {
                form_details = (FormBean) session.get("webcast_details");
            } else if (formtype.equals("centralutm")) {
                form_details = (FormBean) session.get("firewall_details");

            } else if (formtype.equals("email_act")) {
                form_details = (FormBean) session.get("emailactive_details");

            } else if (formtype.equals("email_deact")) {
                form_details = (FormBean) session.get("emailactive_details");

            } else if (formtype.equals("dor_ext")) {
                form_details = (FormBean) session.get("dor_ext_details");
            } else if (formtype.equals("dor_ext_retired")) {
                form_details = (FormBean) session.get("dor_ext_retired_details");
                profile_values = (Map) session.get("profile-values");
            } else if (formtype.equals("daonboarding")) {
                form_details = (FormBean) session.get("daonboarding_details");
                session.put("da_onboarding", "false");
            } else {
                formtype = "";
            }
            session.put("form_type", formtype);
            session.put("check", check);
            // String nic_employee = session.get("user_bo").toString();
            if (form_details != null) {
                if (nic_employee) {
                    form_details.setNic_employee("true");
                } else {
                    form_details.setNic_employee("false");
                }
            }
            Map profile_values = (HashMap) session.get("profile-values");
            String state_min = "";
            userValues = (UserData) session.get("uservalues");
            String category = "";
            String department = "";
            if (!formtype.equalsIgnoreCase("dor_ext_retired")) {
                category = userValues.getUserOfficialData().getEmployment();   //02june2022

                if (category != null && category.equalsIgnoreCase("central") || category != null && category.equalsIgnoreCase("ut")) {
                    String ministry = userValues.getUserOfficialData().getMinistry();
                    if (ministry != null && !(ministry.isEmpty())) {
                        state_min = ministry;
                    }
                    department = userValues.getUserOfficialData().getDepartment();
                } else if (category != null && category.equalsIgnoreCase("state")) {
                    String state = userValues.getUserOfficialData().getState();
                    if (state != null && !(state.isEmpty())) {
                        state_min = state;
                    }
                    department = userValues.getUserOfficialData().getDepartment();
                } else {
                    String org = userValues.getUserOfficialData().getOrganizationCategory();
                    if (org != null && !(org.isEmpty())) {
                        state_min = org;
                    }
                }
            } else {
                profile_values.put("min", null);
                //profile_values.put("dept", null);
                //profile_values.put("org", null);
            }

            if (check != null && formtype != null && form_details != null && profile_values != null) {
                consentreturn = esignService.consent(check, ref_num, formtype, form_details, profile_values, isHog, isHod, category, state_min, department);

                if ((session.get("comingFromUpdateMobile") != null && session.get("comingFromUpdateMobile").equals("updateMobile")) && (session.get("exist_in_kavach") != null && session.get("exist_in_kavach").equals("true"))) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<p>Dear Team,</p>");
                    sb.append("<p>This mail is just for information that user has successfully submitted the request for updating his/her mobile number in eForms. You may use this mail for your reference when user comes back to you and raises any security concern.</p>");
                    sb.append("<h3>Applicant Details</h3><table border='0'>"
                            + "<tr><td>Registration Number</td><td>" + ref_num + "</td></tr>"
                            + "<tr><td>Email</td><td>" + userdata.getEmail() + "</td></tr>"
                            + "<tr><td>IP from which this activity was done</td><td>" + ip + "</td></tr>"
                            + "<tr><td>Browser</td><td>" + userAgent + "</td></tr>"
                            + "<tr><td>Timestamp</td><td>" + "  " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "</td></tr>"
                            + "</table>");

                    sb.append("<p>Regards,</p>");
                    sb.append("eForms Team NIC");

                    String from1 = "eforms@nic.in";
                    //String emailTo1 = "satya.nhq@nic.in";//niccert
                    String emailTo1 = niccert;
                    String sub1 = "eForms : Mobile number successfully updated";
                    inform.sendToRabbit(from1, emailTo1, sb.toString(), sub1, "", "", null, null, null);
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "consentreturn: " + consentreturn);
            session.remove("flag");
            //session.remove("ref_num");
//            } else {
//                formtype = "";
//                check = "";
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String ConsentRetired() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside consentretired check::::::::::::" + check);
        String email = session.get("email").toString();
        String mobile = session.get("mobile").toString();
        String name = session.get("name").toString();
        String newDor = session.get("newdor").toString();
        String oldDor = session.get("dor").toString();

        try {
            String ref_num = (String) session.get("ref_num");
            formtype = "dor_ext_retired";
            form_details = new FormBean();
            form_details.setP_single_dor(oldDor);
            form_details.setApplicant_mobile(mobile);
            form_details.setApplicant_name(name);
            form_details.setApplicant_design("");
            form_details.setApplicant_email(email);
            form_details.setSingle_dor(newDor);
            session.put("form_type", "dor_ext_retired");
            session.put("form_text", "DOR-EXT For Retired Officials Services");
            session.put("dor_ext_retired_details", form_details);
            session.put("check", check);

            consentreturn = esignService.consentRetired(check, ref_num, formtype, form_details);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "consentreturn: " + consentreturn);
            session.remove("flag");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    // below function added by pr on 12thapr18
    public String ConsentAdmin() {
        SignUpService signUpService = new SignUpService();
        Ldap ldap = new Ldap();
        Map<String, String> map = new HashMap<>();
        Inform inform = new Inform();
        String state_min = "";
        try {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside consent admin");
            UserTrack usertrack = new UserTrack();
            boolean nic_employee = false;
            String role = "", esignName = "Admin";
            FormBean form_details = usertrack.fetchFormData(ref_num, formtype);// get the details from DB
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside OtpAadhaarAdmin func in e_signing file form_details size is" + form_details);
            if (session.get("admin_role") != null) {
                role = session.get("admin_role").toString();
            }
            if (role.equals(Constants.ROLE_CA)) {
                esignName = form_details.getHod_name();
            } else if (role.equals(Constants.ROLE_CO)) {
                String coordEmail = "";

                if (session.get("email") != null) {
                    coordEmail = session.get("email").toString();
                }

                if (!coordEmail.equals("")) {
                    ForwardAction fwdActionObj = new ForwardAction();

                    ArrayList esignNameArr = fwdActionObj.fetchLDAPName(coordEmail);

                    if (esignNameArr.get(1) != null) {
                        esignName = esignNameArr.get(1).toString();
                    }

                }
            }
            //Map profile_values = (Map) session.get("profile_values");
            //System.out.println("MAAAPPPPPP " + profile_values);
            if (form_details != null) // if added by pr on 22ndmar18
            {
                userValues = (UserData) session.get("uservalues");

                String category = userValues.getUserOfficialData().getEmployment();   //02june2022
                String department = "";
                if (category != null && category.equalsIgnoreCase("central") || category != null && category.equalsIgnoreCase("ut")) {
                    String ministry = userValues.getUserOfficialData().getMinistry();
                    if (ministry != null && !(ministry.isEmpty())) {
                        state_min = ministry;
                    }
                    department = userValues.getUserOfficialData().getDepartment();
                } else if (category != null && category.equalsIgnoreCase("state")) {
                    String state = userValues.getUserOfficialData().getState();
                    if (state != null && !(state.isEmpty())) {
                        state_min = state;
                    }
                    department = userValues.getUserOfficialData().getDepartment();
                } else {
                    String org = userValues.getUserOfficialData().getOrganizationCategory();
                    if (org != null && !(org.isEmpty())) {
                        state_min = org;
                    }
                }

                String email = "";
                String mobile = "";
                String name = "";
                if (userValues != null) {
                    email = userValues.getEmail();
                    mobile = userValues.getMobile();
                    name = userValues.getName();
                }
                map = ldap.isUserNICEmployee(userValues.getEmail());
                form_details.setNic_employee(map.get("isNICEmployee"));
                session.put("ref_num", ref_num);
                session.put("form_type", formtype);
                session.put("moduleEsign", "online");
                session.put("admin_role", "ca");
                if (profile_values != null) {
                    profile_values.put("hod_name", name);
                    profile_values.put("hod_email", email);
                    profile_values.put("hod_mobile", mobile);
                    profile_values.put("hod_name", name);
                    profile_values.put("hod_email", email);
                    profile_values.put("hod_mobile", mobile);
                }
                boolean ishog = false;
                boolean ishod = false;
                if (signUpService.isHod(email)) {
                    ishod = true;

                } else {
                    ishod = false;
                }
                if (signUpService.isHog(email)) {
                    ishog = true;

                } else {
                    ishog = false;
                }
                if (formtype.equals("distributionlist") || formtype.equals("dlist")) {
                    formtype = "distributionlist";
                }
                if (esignService != null) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin");
                    if (profile_values != null && formtype != null) {
                        session.put("check", "esign_admin");
                        consentreturn = esignService.consent("esign_admin", ref_num, formtype, form_details, profile_values, ishog, ishod, category, state_min, department);
                        if (session.get("comingFromUpdateMobile") != null && session.get("exist_in_kavach") != null && session.get("exist_in_kavach").equals("true")) {

                            StringBuilder sb = new StringBuilder();
                            sb.append("<p>Dear Team,</p>");
                            sb.append("This mail is just for information that user has successfully submitted the request for updating his/her mobile number in eForms. You may use this mail for your reference when user comes back to you and raises any security concern. </br>");
                            sb.append("<h3>Applicant Details</h3><table border='0'>"
                                    + "<tr><td>Registration Number</td><td>" + ref_num + "</td></tr>"
                                    + "<tr><td>Email</td><td>" + userValues.getEmail() + "</td></tr>"
                                    + "<tr><td>IP from which this activity was done</td><td>" + ip + "</td></tr>"
                                    + "<tr><td>Browser</td><td>" + userAgent + "</td></tr>"
                                    + "<tr><td>Timestamp</td><td>" + "  " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "</td></tr>"
                                    + "</table>");

                            sb.append("<p>Regards,</p>");
                            sb.append("eForms Team NIC");
                            sb.append("<p>Regards,</p>");
                            sb.append("eForms Team NIC");
                            String from1 = "eforms@nic.in";
                            String emailTo1 = niccert;
                            String sub1 = "eForms : Mobile number successfully updated";
                            inform.sendToRabbit(from1, emailTo1, sb.toString(), sub1, "", "", null, null, null);

                        }

                    }
                } else {
                    System.out.println("esignService is NULL");
                }
            } else {
                System.out.println(" inside form_details object null in e_Signing . java ");
            }
            System.out.println("consentreturn: " + consentreturn);

        } catch (IOException ex) {
            Logger.getLogger(e_signing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(e_signing.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(e_signing.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SUCCESS;
    }

    public String DownloadPDF() {

//        String filename = "/eForms/PDF/" + session.get("ref_num").toString() + ".pdf";
        String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + session.get("ref_num").toString() + ".pdf";

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "filename: " + filename);
        esignedfilename = session.get("ref_num").toString() + ".pdf";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "esigned: " + esignedfilename);

        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("FILE DID NOT EXIST AT SERVER ::: " + filename);
                return "filenotfound";
            }
            fileInputStream = new FileInputStream(new File(filename));
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return SUCCESS;
    }

}
