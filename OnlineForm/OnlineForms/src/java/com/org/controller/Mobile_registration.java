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
import com.org.dao.esignDao;
import com.org.service.MobileService;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import entities.LdapQuery;
import entities.Random;
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
 * @author dhiren
 */
public class Mobile_registration extends ActionSupport implements SessionAware {

    Map session;
    FormBean form_details;
    MobileService mobileservice = null;
    UpdateService updateService = null;// line added by pr on 1stfeb18
    Map profile_values = null;
    validation valid = null;
    Map<String, Object> error = null;
    public String action_type, data, ReturnString;
    ProfileService profileService = null;
    Map hodDetails = null;
    esignDao esignDao = null;
    // start, code added by pr on 22ndjan18
    String CSRFRandom;
    private Database db = null;
    Map<String, String> map = null;
    private UserData userValues;
    private Ldap ldap = null;

    public Mobile_registration() {
        mobileservice = new MobileService();
        valid = new validation();
        error = new HashMap();
        updateService = new UpdateService();
        map = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        profile_values = new HashMap();
        esignDao = new esignDao();
        db = new Database();
        if (mobileservice == null) {
            mobileservice = new MobileService();
        }
        if (esignDao == null) {
            esignDao = new esignDao();
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

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    // end, code added by pr on 22ndjan18
    public void setSession(Map session) {
        this.session = session;
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public String getReturnString() {
        return ReturnString;
    }

    public void setReturnString(String ReturnString) {
        this.ReturnString = Jsoup.parse(ReturnString).text();
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public String Mobile_tab1() {
        try {
            ldap = new Ldap();
            error.clear();
            UserData userdata = (UserData) session.get("uservalues");
            Map temp = (HashMap) session.get("profile-values");
            System.out.println("HashMap) session.get(\"profile-values\")" + (HashMap) session.get("profile-values"));
            String email = temp.get("email").toString();
            String old_mobile = entities.LdapQuery.GetMobile(email);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            System.out.println("newotp in session" + session.get("new_otp") + "otp code" + form_details.getNewcode());
            String new_mobile = form_details.getNew_mobile();
            String type = "";
            if (old_mobile.contains(new_mobile)) {
                type = "profile";
            } else {
                type = "mobile";
            }
            if (form_details.getNicDateOfBirth().isEmpty()) {
                error.put("nicDateOfBirth_err", "Date of birth cannot be left blank");
                form_details.setNicDateOfBirth(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfBirth()));
            }

            if (!form_details.getNicDateOfBirth().isEmpty()) {
                String msg = valid.dobValidation(form_details.getNicDateOfBirth());
                if (!msg.isEmpty()) {
                    error.put("nicDateOfBirth_err", msg);
                    form_details.setNicDateOfBirth(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfBirth()));
                }
            }

            if (!form_details.getNicDateOfRetirement().isEmpty()) {
                String msg = valid.dorValidationMobile(form_details.getNicDateOfRetirement(), form_details.getNicDateOfBirth());
                if (!msg.isEmpty()) {
                    error.put("nicDateOfRetirement_err", msg);
                    form_details.setNicDateOfRetirement(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfRetirement()));
                }
            }

            if (form_details.getNicDateOfRetirement().isEmpty()) {
                error.put("nicDateOfRetirement_err", "Date of Retirement cannot be left blank");
                form_details.setNicDateOfRetirement(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfRetirement()));
            }

            if (form_details.getDesignation().isEmpty()) {
                error.put("designation_err", "Designation cannot be left blank");
                form_details.setDesignation(ESAPI.encoder().encodeForHTML(form_details.getDesignation()));
            }

            if (form_details.getDisplayName().isEmpty()) {
                error.put("displayName_err", "Display name cannot be left blank");
                form_details.setDisplayName(ESAPI.encoder().encodeForHTML(form_details.getDisplayName()));
            }

            for (String email1 : userdata.getAliases()) {
                if (!email1.isEmpty()) {
                    if (db.MobileRequestAlreadyExist(email1)) {
                        error.put("mobile_request_error", "You can not fill more than one mobile updation request per calendar day.");
                    }
                }
            }

            for (String email1 : userdata.getAliases()) {
                if (!email1.isEmpty()) {
                    if (db.MobileRequestAlreadyPending(email)) {
                        error.put("mobile_req_pending_error", "Please wait for your submitted request to be either completed or rejected to generate a new request.");
                    }
                }
            }

            if (form_details.getCountry_code() != null) {
                if (!form_details.getCountry_code().matches("^[+0-9]{2,8}$")) {
                    error.put("country_code_err", "Please select Correct Country code");
                    form_details.setCountry_code(ESAPI.encoder().encodeForHTML(form_details.getCountry_code()));
                }
                boolean mobile_error = valid.UpdateMobileValidation(form_details.getNew_mobile(), form_details.getCountry_code());
                if (mobile_error == true) {
                    error.put("mobile_error", "Enter the Mobile Number 10 digits for india [e.g.: 9999999999 ], OR [8-12] digits for international");
                    form_details.setNew_mobile(ESAPI.encoder().encodeForHTML(form_details.getNew_mobile()));
                }

                if (type.equalsIgnoreCase("mobile")) {
                    Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(form_details.getCountry_code() + form_details.getNew_mobile());
                    System.out.println("emailsAgainstMobile" + emailsAgainstMobile);
                    System.out.println("sizeeeeeeee" + emailsAgainstMobile.size());
                    if (email.toLowerCase().contains("@jcn.nic.in") && emailsAgainstMobile.size() > 4) {
                        String emailAgainstMobileInString = emailsAgainstMobile.toString();
                        error.put("mobile_error", "There are already 5 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                    } else {
                        if (emailsAgainstMobile.size() > 3 || emailsAgainstMobile.size() == 3) {
                            String emailAgainstMobileInString = emailsAgainstMobile.toString();
                            error.put("mobile_error", "There are already 3 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                        }
                    }
                }
            } else {
                boolean mobile_error = valid.MobileValidation(form_details.getNew_mobile());
                if (mobile_error == true) {
                    error.put("mobile_error", "Enter the Mobile Number 10 digits for india [e.g.: 9999999999 ], OR [8-12] digits for international");
                    form_details.setNew_mobile(ESAPI.encoder().encodeForHTML(form_details.getNew_mobile()));
                }
                if (type.equalsIgnoreCase("mobile")) {
                    Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(form_details.getCountry_code() + form_details.getNew_mobile());
                    System.out.println("emailsAgainstMobile" + emailsAgainstMobile);
                    System.out.println("sizeeeeeeee" + emailsAgainstMobile.size());
                    if (email.toLowerCase().contains("@jcn.nic.in") && emailsAgainstMobile.size() > 4) {
                        String emailAgainstMobileInString = emailsAgainstMobile.toString();
                        error.put("mobile_error", "There are already 5 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                    } else {
                        if (emailsAgainstMobile.size() > 3 || emailsAgainstMobile.size() == 3) {
                            String emailAgainstMobileInString = emailsAgainstMobile.toString();
                            error.put("mobile_error", "There are already 3 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                        }
                    }
                }
            }

            if (session.get("update_without_oldmobile").equals("no")) {
                if (session.get("new_otp") != null) {
                    if (!form_details.getNewcode().trim().equals(session.get("new_otp").toString())) {
                        error.put("otp_error", "Please enter correct otp code");
                        form_details.setNewcode(ESAPI.encoder().encodeForHTML(form_details.getNewcode()));
                    }
                }
            }
            if (session.get("update_without_oldmobile").equals("yes")) {

                if (!form_details.getNew_mobile().equals(session.get("mobileNumber"))) {
                    error.put("mobile_error", "mobile number can not be changed");
                }
            }
            System.out.println("remarks::::" + form_details.getRemarks());
            if (form_details.getRemarks().equals("")) {
                error.put("remarks_error", "Please select remarks");
                form_details.setNewcode(ESAPI.encoder().encodeForHTML(form_details.getRemarks()));
            }
            if (form_details.getRemarks().equals("other")) {
                if (form_details.getOther_remarks().equals("")) {
                    error.put("remarks_other_error", "Please enter remarks");
                    form_details.setNewcode(ESAPI.encoder().encodeForHTML(form_details.getRemarks()));
                }
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for Mobile controller tab 1: " + error);
            // end, code added by pr on 22ndjan18
            if (error.isEmpty()) {
                System.out.println("if error is empty::::::" + (HashMap) session.get("profile-values"));
                //  if (session.get("update_without_oldmobile").equals("no")) {
                mobileservice.Mobile_tab1(form_details);
                //added by sanjeev 03-08-2022
                Map<String, Object> ldapValues = ldap.fetchAttrUpdateMobile(email);
                String nicDateOfBirthOld = (String) ldapValues.get("nicDateOfBirth");
                String nicDateOfRetirementOld = (String) ldapValues.get("nicDateOfRetirement");
                String designationOld = (String) ldapValues.get("designation");
                String mobileOld = ldap.fetchMobileFromLdap(email);
                String displayNameOld = ldap.fetchNameFromLdap(email);
                profile_values = (HashMap) session.get("profile-values");
                System.out.println("ldap Details-----::nicDateOfBirthOld:" + nicDateOfBirthOld + "  nicDateOfRetirementOld:" + nicDateOfRetirementOld + "   designationOld:" + designationOld + " mobileOld:" + mobileOld + "                                  displayNameOld:" + displayNameOld);
                profile_values.put("nicDateOfBirthOld", nicDateOfBirthOld);
                profile_values.put("nicDateOfRetirementOld", nicDateOfRetirementOld);
                profile_values.put("designationOld", designationOld);
                profile_values.put("displayNameOld", displayNameOld);
                profile_values.put("mobileOld", mobileOld);

                
                profile_values.put("Formtype",type);
                // }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
//             data = "";
//            CSRFRandom = "";
//            action_type = "";
            e.printStackTrace();

        }
        return SUCCESS;
    }

    public String Mobile_tab2() {
        try {
            db = new Database();
            ldap = new Ldap();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            error.clear();
            Map temp = (HashMap) session.get("profile-values");
            String email = temp.get("email").toString();
            String old_mobile = entities.LdapQuery.GetMobile(email);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            profile_values = (HashMap) session.get("profile-values");
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            String new_mobile = form_details.getNew_mobile();
            String type = "";
            if (old_mobile.contains(new_mobile)) {
                type = "profile";
            } else {
                type = "mobile";
            }

            if (form_details.getNicDateOfBirth().isEmpty()) {
                error.put("nicDateOfBirth_err", "Date of birth cannot be left blank");
                form_details.setNicDateOfBirth(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfBirth()));
            }

            if (!form_details.getNicDateOfBirth().isEmpty()) {
                String msg = valid.dobValidation(form_details.getNicDateOfBirth());
                if (!msg.isEmpty()) {
                    error.put("nicDateOfBirth_err", msg);
                    form_details.setNicDateOfBirth(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfBirth()));
                }
            }

            if (!form_details.getNicDateOfRetirement().isEmpty()) {
                String msg = valid.dorValidationMobile(form_details.getNicDateOfRetirement(), form_details.getNicDateOfBirth());
                if (!msg.isEmpty()) {
                    error.put("nicDateOfRetirement_err", msg);
                    form_details.setNicDateOfRetirement(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfRetirement()));
                }
            }

            if (form_details.getNicDateOfRetirement().isEmpty()) {
                error.put("nicDateOfRetirement_err", "Date of Retirement cannot be left blank");
                form_details.setNicDateOfRetirement(ESAPI.encoder().encodeForHTML(form_details.getNicDateOfRetirement()));
            }

            if (form_details.getDesignation().isEmpty()) {
                error.put("designation_err", "Designation cannot be left blank");
                form_details.setDesignation(ESAPI.encoder().encodeForHTML(form_details.getDesignation()));
            }

            if (form_details.getDisplayName().isEmpty()) {
                error.put("displayName_err", "Display name cannot be left blank");
                form_details.setDisplayName(ESAPI.encoder().encodeForHTML(form_details.getDisplayName()));
            }

            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }

            for (String email1 : userdata.getAliases()) {
                if (!email1.isEmpty()) {
                    if (db.MobileRequestAlreadyExist(email1)) {
                        error.put("mobile_request_error", "You can not fill more than one mobile updation request per calendar day.");
                    }
                }
            }

            for (String email1 : userdata.getAliases()) {
                if (!email1.isEmpty()) {
                    if (db.MobileRequestAlreadyPending(email)) {
                        error.put("mobile_req_pending_error", "Please wait for your submitted request to be either completed or rejected to generate a new request.");
                    }
                }
            }

            if (form_details.getCountry_code() != null) {
                if (!form_details.getCountry_code().matches("^[+0-9]{2,8}$")) {
                    error.put("country_code_err", "Please select Correct Country code");
                    form_details.setCountry_code(ESAPI.encoder().encodeForHTML(form_details.getCountry_code()));
                }
                boolean mobile_error = valid.UpdateMobileValidation(form_details.getNew_mobile(), form_details.getCountry_code());
                if (mobile_error == true) {
                    error.put("mobile_error", "Enter the Mobile Number 10 digits for india [e.g.: 9999999999 ], OR [8-12] digits for international");
                    form_details.setNew_mobile(ESAPI.encoder().encodeForHTML(form_details.getNew_mobile()));
                }

                if (type.equalsIgnoreCase("mobile")) {
                    Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(form_details.getCountry_code() + form_details.getNew_mobile());
                    System.out.println("emailsAgainstMobile" + emailsAgainstMobile);
                    System.out.println("sizeeeeeeee" + emailsAgainstMobile.size());
                    if (email.toLowerCase().contains("@jcn.nic.in") && emailsAgainstMobile.size() > 4) {
                        String emailAgainstMobileInString = emailsAgainstMobile.toString();
                        error.put("mobile_error", "There are already 5 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                    } else {
                        if (emailsAgainstMobile.size() > 3 || emailsAgainstMobile.size() == 3) {
                            String emailAgainstMobileInString = emailsAgainstMobile.toString();
                            error.put("mobile_error", "There are already 3 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                        }
                    }
                }
            } else {
                boolean mobile_error = valid.MobileValidation(form_details.getNew_mobile());
                if (mobile_error == true) {
                    error.put("mobile_error", "Enter the Mobile Number 10 digits for india [e.g.: 9999999999 ], OR [8-12] digits for international");
                    form_details.setNew_mobile(ESAPI.encoder().encodeForHTML(form_details.getNew_mobile()));
                }

                if (type.equalsIgnoreCase("mobile")) {
                    Set<String> emailsAgainstMobile = ldap.fetchPrimaryEmailsAgainstMobile(form_details.getCountry_code() + form_details.getNew_mobile());
                    System.out.println("emailsAgainstMobile" + emailsAgainstMobile);
                    System.out.println("sizeeeeeeee" + emailsAgainstMobile.size());
                    if (email.toLowerCase().contains("@jcn.nic.in") && emailsAgainstMobile.size() > 4) {
                        String emailAgainstMobileInString = emailsAgainstMobile.toString();
                        error.put("mobile_error", "There are already 5 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                    } else {
                        if (emailsAgainstMobile.size() > 3 || emailsAgainstMobile.size() == 3) {
                            String emailAgainstMobileInString = emailsAgainstMobile.toString();
                            error.put("mobile_error", "There are already 3 email addresses (" + emailAgainstMobileInString + ") associated with this mobile number.So, the same mobile number can't be updated.");//text change 20thmay2020
                        }
                    }
                }
            }

            if (session.get("update_without_oldmobile").equals("yes")) {
                if (!form_details.getNew_mobile().equals(session.get("mobileNumber"))) {
                    error.put("mobile_error", "mobile number can not be changed");
                }
            }

            if (form_details.getRemarks().equals("")) {
                error.put("remarks_error", "Please select remarks");
                form_details.setNewcode(ESAPI.encoder().encodeForHTML(form_details.getRemarks()));
            }
            if (form_details.getRemarks().equals("other")) {
                if (form_details.getOther_remarks().equals("")) {
                    error.put("remarks_other_error", "Please enter remarks");
                    form_details.setNewcode(ESAPI.encoder().encodeForHTML(form_details.getRemarks()));
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
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
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
                        //Map a = (HashMap) session.get("profile-values");
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
                                // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
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
            }
            // end validate profile 20th march

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for Mobile controller tab 2: " + error);
            // end, code added by pr on 22ndjan18
            if (getAction_type().equals("confirm")) {
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    // if (session.get("update_without_oldmobile").equals("no")) {
                    session.put("mobile_details", form_details);
                    String ref_num = mobileservice.Mobile_tab2(form_details, profile_values);
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
                    // }
                }
            } else if (getAction_type().equals("validate")) {
                if (error.isEmpty()) {
                    if (form_details.getUser_employment().toString().equalsIgnoreCase("State") && form_details.getStateCode().toString().equalsIgnoreCase("Himachal Pradesh")) {

                        ArrayList<String> Cadata = new ArrayList<String>();
                        System.out.println("form_details.getState_dept().toString()" + form_details.getState_dept().toString());
                        Cadata = esignDao.fetchHimachalCoord(form_details.getState_dept().toString());
                        if (Cadata.size() > 0) {
                            if (Cadata.get(0).toString().equals("nodata")) {

                            } else {
                                System.out.println("else have data");
                                String hodMobile = LdapQuery.GetMobile(Cadata.get(0));
                                form_details.setHod_mobile(hodMobile);
                                form_details.setHod_name(Cadata.get(1));
                                form_details.setHod_email(Cadata.get(0));

                            }

                        }
                    }
                }
                if (!error.isEmpty()) {
                    data = "";
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
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for Mobile controller tab 1: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

}
