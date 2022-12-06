/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.service.ProfileService;
import com.org.service.SignUpService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import validation.validation;
import com.org.bean.UserData;
import com.org.bean.UserOfficialData;
import com.org.connections.DbConnection;
import com.org.dao.Database;
import com.org.dao.Ldap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import model.Faqs;
import org.jsoup.nodes.Document;

/**
 *
 * @author Nancy
 */
public class Profile extends ActionSupport implements SessionAware {

    FormBean form_details;
    private UserData userValues;
    Map session;
    ProfileService profileService = null;
    SignUpService signup = null;
    List list = null;
    List<Faqs> faq_list = null;
    String returnString = null;
    validation valid = null;
    Map<String, Object> error = null;
    Map hodDetails = null;
    Map underSecDetails = null;
    String json = null;
    String check_validate;
    String email, data;
    Map<String, Object> val = null;
    private Database db = null;
    private Ldap ldap = null;
    public String orgType = "";
    public String depType = "";
    public String orgDept = "";

    // Map userValues = new HashMap();
    String hod_mobile;

    public Profile() {
        profileService = new ProfileService();
        signup = new SignUpService();
        faq_list = new ArrayList<>();
        valid = new validation();
        error = new HashMap<>();
        val = new HashMap<>();
        underSecDetails = new HashMap();
        hodDetails = new HashMap();
        list = new ArrayList();
        ldap = new Ldap();
        if (profileService == null) {
            profileService = new ProfileService();
        }
        if (signup == null) {
            signup = new SignUpService();
        }
        if (faq_list == null) {
            faq_list = new ArrayList<>();
        }
        if (valid == null) {
            valid = new validation();
        }
        if (error == null) {
            error = new HashMap<>();
        }
        if (val == null) {
            val = new HashMap<>();
        }
        if (underSecDetails == null) {
            underSecDetails = new HashMap();
        }
        if (hodDetails == null) {
            hodDetails = new HashMap();
        }
        if (list == null) {
            list = new ArrayList();
        }
    }

    public List<Faqs> getFaq_list() {
        return faq_list;
    }

    public Map<String, Object> getVal() {
        return val;
    }

    public void setVal(Map<String, Object> val) {
        this.val = val;
    }

    public void setFaq_list(List<Faqs> faq_list) {
        this.faq_list = faq_list;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
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

    public String getCheck_validate() {
        return check_validate;
    }

    public void setCheck_validate(String check_validate) {
        this.check_validate = Jsoup.parse(check_validate).text();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map getHodDetails() {
        return hodDetails;
    }

    public void setHodDetails(Map hodDetails) {
        this.hodDetails = hodDetails;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        System.out.println("DATA ::: " + data);
        Document doc = Jsoup.parse(data);
        System.out.println(doc.body().data());
        this.data = Jsoup.parse(data).text();
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public String getDepType() {
        return depType;
    }

    public void setDepType(String depType) {
        this.depType = depType;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgDept() {
        return orgDept;
    }

    public void setOrgDept(String orgDept) {
        this.orgDept = orgDept;
    }

    public String PersonalProfile() {
        try {
            // server side validation
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            boolean name_error = valid.nameValidation(form_details.getUser_name());
            boolean city_error = valid.cityValidation(form_details.getUser_city());
            boolean state_error = valid.addstateValidation(form_details.getUser_state());

            boolean ophone_error = valid.telValidation(form_details.getUser_ophone());
            boolean rphone_error = valid.tel1Validation(form_details.getUser_rphone());
            boolean emp_code_error = valid.employcodevalidation(form_details.getUser_empcode());
            boolean add_error = valid.addValidation(form_details.getUser_address());
            boolean email_error = valid.EmailValidation(form_details.getUser_email());
            boolean desg_error = valid.desigValidation(form_details.getUser_design());
            boolean pin_error = valid.pinValidation(form_details.getUser_pincode());
            UserData userdata = (UserData) session.get("uservalues");
            boolean mobile_error = false;
            if (userdata.isIsEmailValidated()) {
                mobile_error = valid.MobileValidation(userdata.getMobile());
            } else {
                mobile_error = valid.MobileValidation(form_details.getUser_mobile());
            }

            ArrayList b;
            Set s = (Set) userdata.getAliases();
            ArrayList<String> newAr = new ArrayList<String>();
            newAr.addAll(s);
            ArrayList email_aliases = null;
            email_aliases = newAr;
            if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                if (email_aliases.contains(form_details.getUser_email())) {
                } else {
                    error.put("email_error", "Email id should be same");
                }
            } else {

            }
//            if (userdata.getMobile() != null) {
//                if (!userdata.getMobile().equals(form_details.getUser_mobile())) {
//                    error.put("mobile_error", "Mobile number should be same as you have entered");
//                }
//            }
            if (name_error == true) {
                error.put("name_error", "Enter Full Name [Only characters,dot(.) and whitespaces allowed]");
            }
            if (city_error == true) {
                error.put("city_error", "Enter City [Only characters, whitespaces,&,() allowed]");
            }
            if (state_error == true) {
                error.put("state_error", "Please enter State in correct format");
            }
            if (mobile_error == true) {
                error.put("mobile_error", "Enter Mobile Number [e.g:+919999999999]");
            }
            if (ophone_error == true) {
                error.put("ophone_error", "Enter Official Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
            }
            if (rphone_error == true) {
                error.put("rphone_error", "Please Enter Residence Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
            }
            if (emp_code_error == true) {
                error.put("emp_code_error", "Enter Employee Code [Only characters,digits and underscore(_) allowed]");
            }
            if (add_error == true) {
                error.put("add_error", "Enter Your Official Address [Only characters,digits,whitespaces and [. , - # / ( ) ],limit[100] allowed]");
            }
            if (email_error == true) {
                error.put("email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
            }
            if (desg_error == true) {
                error.put("desg_error", "Enter Designation [characters,Alphanumeirc,whitespaces and [. , - &]] allowed");
            }
            if (pin_error == true) {
                error.put("pin_error", "Enter Pin Code [Only digits(6) allowed]");
            }
            if (error.isEmpty()) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "errir is empty for first tab");
                session.put("profile", form_details);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return SUCCESS;
    }

    public String orgProfile() {
        try {
            // server side validation
            userValues = (UserData) session.get("uservalues");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "check validate: " + check_validate);
            UserData userdata = (UserData) session.get("uservalues");
            HodData hoddata = (HodData) userdata.getHodData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            db = new Database();
            ldap = new Ldap();

            if (check_validate.equals("true")) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);
                userValues.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
                form_details.setCa_name(form_details.getHod_name());
                form_details.setCa_email(form_details.getHod_email());
                form_details.setCa_mobile(form_details.getHod_mobile());
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                if (userdata.isIsEmailValidated()) {
                    form_details.setUser_mobile(userdata.getMobile());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString());
                    form_details.setCa_mobile(hodDetails.get("mobile").toString());

                } else if (userValues.getHodData().isGovEmployee()) {
                    form_details.setHod_mobile(hodDetails.get("mobile").toString());
                    form_details.setCa_mobile(hodDetails.get("mobile").toString());

                }

                System.out.println("form_details.getCa_design()" + form_details.getCa_design());
                boolean employment_error = valid.EmploymentValidation(form_details.getUser_employment());
                boolean ca_name_error = valid.nameValidation(form_details.getCa_name());
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean ca_mobile_error = valid.MobileValidation(form_details.getCa_mobile());
                boolean ca_email_error = valid.EmailValidation(form_details.getCa_email());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());

                if (employment_error == true) {
                    error.put("employment_error", "Please enter Employment in correct format");
                } else if (form_details.getUser_employment().equals("Central")) {
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

                if (userdata.isIsEmailValidated() == false) {
                    if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                        if (hodDetails.get("bo").toString().equals("no bo")) {
                            error.put("hod_email_error", "The department selected can only have government reporting officer");
                        }

                    }
                    System.out.println("inside ldap employee false ");
                    if (ca_name_error == true) {
                        error.put("ca_name_error", "Enter Approving Authority Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (form_details.getCa_mobile().trim().startsWith("+")) {

                        if ((ca_mobile_error == true) && (form_details.getCa_mobile().trim().startsWith("+91"))) {

                            error.put("ca_mobile_error", "Enter Approving Authority Mobile Number [e.g: +919999999999][limit 10 digits]");
                        } else if ((ca_mobile_error == true) && (!form_details.getCa_mobile().trim().startsWith("+91"))) {
                            error.put("ca_mobile_error", "Enter Approving Authority Mobile Number [e.g: +01249999999999],[limit 15 digits]");
                        } else if(form_details.getHod_mobile().toString().trim().startsWith("+") && form_details.getHod_mobile().toString().length() != 13) {
                            error.put("hod_mobile_error", "Enter Correct Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                        } else if(form_details.getHod_mobile().toString().trim().startsWith("91") && form_details.getHod_mobile().toString().length() != 12) {
                            error.put("hod_mobile_error", "Enter Correct Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                        }
                    } 
                   
                    else {
                    
                        error.put("ca_mobile_error", "Enter Approving Authority Mobile Number [e.g: +919999999999] limit[13 digits] for india, [e.g: +919999999999][limit 15 digits] for international");
                    }
                    if (ca_email_error == true) {
                        error.put("ca_email_error", "Enter Approving Authority Email [e.g: abc.xyz@zxc.com]");
                    }

                } else if (hodDetails.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Reporting officer should be govt employee");
                }

                if (hodDetails.get("bo").equals("no bo")) {
                    underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                    form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString());
                    if (underSecDetails.get("bo").equals("no bo")) {
                        error.put("under_sec_email_error", "Under secretary should be government employee");
                    } else {
                        boolean under_sec_name_error = false;
                        boolean under_sec_mobile_error = false;
                        boolean under_sec_email_error = false;
                        boolean under_sec_telephone_error = false;
                        boolean under_sec_desg_error = false;
                        if (form_details.getUnder_sec_email() != null && form_details.getUnder_sec_email() != "") {
                            under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                            under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                            under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                            under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                            under_sec_desg_error = valid.desigValidation(form_details.getunder_sec_desig());
                        }
                        if (under_sec_email_error == true) {
                            error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                        }
                        if (under_sec_name_error == true) {
                            error.put("under_sec_name_error", "Enter Under Secretary Name [Only characters,dot(.) and whitespaces allowed]");
                        }
                        if (under_sec_desg_error == true) {
                            error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                        }
                        System.out.println("form_details.getUnder_sec_mobile()." + form_details.getUnder_sec_mobile());
                        if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {

                            if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {

                                error.put("under_sec_mobile_error", "Enter Under secretary Mobile Number [e.g: +919999999999][limit 10 digits]");
                            } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                error.put("under_sec_mobile_error", "Enter Under secretary Mobile Number  [e.g: +01249999999999],[limit 15 digits]");
                            } else {

                            }
                        } else {

                            error.put("under_sec_mobile_error", "Enter Under secretary Mobile Number [e.g: +919999999999] limit[13 digits] for india, [e.g: +919999999999][limit 15 digits] for international");
                        }
//                        if (!underSecDetails.get("mobile").toString().equals(form_details.getUnder_sec_mobile())) {
//                            error.put("under_sec_mobile_error", "Under Secretary mobile no should be same as already exist in our database");
//                        }
                        if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                            error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile");
                        }
                        FormBean p1 = (FormBean) session.get("profile");

                        System.out.println("p1 mobile:::::::" + p1.getUser_email());
                        if (p1.getUser_mobile().equals(form_details.getUnder_sec_mobile())) {
                            error.put("under_sec_mobile_error", "Under secretary mobile can not be same as of user mobile ");
                        }

                        if (under_sec_telephone_error == true) {
                            error.put("under_sec_telephone_error", "Enter Under secretary Telephone number [e.g: 011-1234546]");
                        }
                    }

                    boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                    System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                    if (check_mobile) {
                        error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                    }
                }
                if (hod_name_error == true) {
                    error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                }
                if (form_details.getHod_mobile().toString().trim().startsWith("+")) {
                    if ((hod_mobile_error == true) && (form_details.getHod_mobile().toString().trim().trim().startsWith("+91"))) {
                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                    } else if ((hod_mobile_error == true) && (!form_details.getHod_mobile().toString().trim().startsWith("+91"))) {
                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
                    } else if(form_details.getHod_mobile().toString().trim().startsWith("+") && form_details.getHod_mobile().toString().length() != 13) {
                        error.put("hod_mobile_error", "Enter Correct Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                    }
                   else if(form_details.getHod_mobile().toString().trim().startsWith("91") && form_details.getHod_mobile().toString().length() != 12) {
                            error.put("hod_mobile_error", "Enter Correct Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                        }
                } else {

                    error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer correct Mobile Number with country code");
                }
                if (hod_email_error == true) {
                    error.put("hod_email_error", "Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]");
                }
                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Reporting/Nodal/Forwarding Officer Designation [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                } else {
                    if (form_details.getHod_mobile().equals(userdata.getMobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user mobile");
                    }
//                    if (!hodDetails.get("bo").equals("no bo")) {
//
//                        if (!hodDetails.get("mobile").toString().equals(form_details.getHod_mobile())) {
//                            error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile no should be same as already exist in our database");
//                        }
//                    }
                    if (form_details.getHod_email().equals(userdata.getEmail())) {
                        error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as of user email address");
                    } else {
                        ArrayList b = null;
                        Set s = (Set) userdata.getAliases();
                        ArrayList<String> newAr = new ArrayList<String>();
                        newAr.addAll(s);
                        ArrayList email_aliases = null;
                        email_aliases = newAr;
                        if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                            // b = (ArrayList) userdata.getAliases();
                            if (email_aliases.contains("mdlmail@nic.in") && form_details.getHod_email().contains("@mazdock.com")) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else if (email_aliases.contains(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else //String user_bo = session.get("user_bo").toString();
                            {
                                if (userdata.isIsNICEmployee() && nicemployeeeditable.equals("no")) {
                                    Map<String, String> map = new HashMap<>();
                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");

                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    // DONE BY NIKKI MEENAXI ON 16 feb 
                                    hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                }
                            }
                        } else // String user_bo = session.get("user_bo").toString();
                        {
                            if (userdata.isIsNICEmployee() && nicemployeeeditable.equals("no")) {
                                Map<String, String> map = new HashMap<>();

                                map = db.fetchUserValuesFromOAD(userdata.getUid());
                                if (map.size() > 0) {
                                    if (!map.get("email").equals(form_details.getHod_email())) {
                                        error.put("hod_email_error", "Reporting officer details can not be changed.");

                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email");
                                        error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                    }
                                }
                            } else {
                                // DONE BY NIKKI MEENAXI ON 16 feb 
                                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                            }
                        }

                    }

                }
                if (hod_telephone_error == true) {
                    error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR: " + error);

                if (error.isEmpty()) {
                    FormBean form_details1 = (FormBean) session.get("profile");

                    if (userdata.isIsEmailValidated()) {
                        form_details.setUser_mobile(userdata.getMobile());
                    } else {
                        form_details.setUser_mobile(form_details1.getUser_mobile());
                    }
                    form_details.setUser_name(form_details1.getUser_name());
                    form_details.setUser_email(form_details1.getUser_email());
                    form_details.setUser_mobile(form_details1.getUser_mobile());
                    form_details.setUser_city(form_details1.getUser_city());
                    form_details.setUser_ophone(form_details1.getUser_ophone());
                    form_details.setUser_rphone(form_details1.getUser_rphone());
                    form_details.setUser_address(form_details1.getUser_address());
                    form_details.setUser_state(form_details1.getUser_state());
                    form_details.setUser_design(form_details1.getUser_design());
                    form_details.setUser_empcode(form_details1.getUser_empcode());
                    form_details.setUser_pincode(form_details1.getUser_pincode());
                    form_details.setUser_alt_address(form_details1.getUser_alt_address());
                    session.put("profile", form_details);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SESSION IS ");
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "in check validate false");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DATA: " + data);
                form_details = (FormBean) session.get("profile");
                System.out.println("formdetails::::::::::" + form_details.getUser_email());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "profile: " + session.get("profile"));
                String otp_type = userdata.getVerifiedOtp();
                boolean isIsNewUser = userdata.isIsNewUser();
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                userValues.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
                if (userdata.isIsEmailValidated()) {
                    form_details.setUser_mobile(userdata.getMobile());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString());
                    form_details.setCa_mobile(hodDetails.get("mobile").toString());
//                     if (!hodDetails.get("cn").toString().trim().equals("")) {
//                        form_details.setHod_name(hodDetails.get("cn").toString().trim());
//                        form_details.setCa_name(hodDetails.get("cn").toString().trim());
//                    }
//                    System.out.println("desig::::::"+hodDetails.get("desig").toString());

//                    if (!hodDetails.get("desig").toString().trim().equals("")) {
//                        form_details.setCa_design(hodDetails.get("desig").toString().trim());
//                    }
                } else if (userValues.getHodData().isGovEmployee()) {
                    form_details.setHod_mobile(hodDetails.get("mobile").toString());
                    form_details.setCa_mobile(hodDetails.get("mobile").toString());
                }

                int i = profileService.insert_profile(form_details, isIsNewUser, otp_type, hodDetails, userValues.getAliases());
                Map profile_values = new HashMap();
                if (userdata.isIsEmailValidated()) {
                    profile_values.put("mobile", userdata.getMobile());

                } else {
                    profile_values.put("mobile", form_details.getUser_mobile().trim());

                }
                //  profile_values.put("mobile", form_details.getUser_mobile().trim());
                profile_values.put("email", form_details.getUser_email().trim());
                profile_values.put("cn", form_details.getUser_name().trim());
                profile_values.put("nicCity", form_details.getUser_city().trim());
                profile_values.put("desig", form_details.getUser_design().trim());
                profile_values.put("postalAddress", form_details.getUser_address().trim());
                profile_values.put("ophone", form_details.getUser_ophone().trim());
                profile_values.put("rphone", form_details.getUser_rphone().trim());
                profile_values.put("state", form_details.getUser_state().trim());
                profile_values.put("emp_code", form_details.getUser_empcode().trim());
                profile_values.put("pin", form_details.getUser_pincode().trim());
                profile_values.put("user_alt_address", form_details.getUser_alt_address().trim());
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
                profile_values.put("ca_name", form_details.getCa_name().trim());
                profile_values.put("ca_design", form_details.getCa_design().trim());
                if (form_details.getCa_mobile().trim().startsWith("+91")) {
                    profile_values.put("ca_mobile", form_details.getCa_mobile().trim());
                }
                profile_values.put("ca_email", form_details.getCa_email().trim());
                profile_values.put("hod_name", form_details.getHod_name().trim());
                if (form_details.getHod_mobile().trim().startsWith("+91")) {
                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                } else {
                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                }
                profile_values.put("hod_email", form_details.getHod_email().trim());
                profile_values.put("hod_tel", form_details.getHod_tel().trim());
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
                profile_values.put("ca_name", form_details.getCa_name().trim());
                profile_values.put("ca_design", form_details.getCa_design().trim());
                if (form_details.getCa_mobile().trim().startsWith("+91")) {
                    profile_values.put("ca_mobile", form_details.getCa_mobile().trim());
                }
                profile_values.put("ca_email", form_details.getCa_email().trim());
                profile_values.put("hod_name", form_details.getHod_name().trim());
                if (form_details.getHod_mobile().trim().startsWith("+91")) {
                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                } else {
                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                }
                profile_values.put("hod_email", form_details.getHod_email().trim());
                profile_values.put("hod_tel", form_details.getHod_tel().trim());
                if (hodDetails.get("bo").equals("no bo")) {
                    if (form_details.getUnder_sec_email().trim() != null) {
                        profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                        if (form_details.getUnder_sec_mobile().trim().startsWith("+91")) {
                            profile_values.put("under_sec_mobile", form_details.getUnder_sec_mobile().trim());
                        } else {
                            profile_values.put("under_sec_mobile", form_details.getUnder_sec_mobile().trim());
                        }
                        profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                        profile_values.put("under_sec_tel", form_details.getUnder_sec_tel().trim());
                        profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                    }
                }
                ArrayList mailaddress = entities.LdapQuery.GetMailEqui(form_details.getUser_email().trim());
                profile_values.put("mailequi", mailaddress);

                session.put("profile-values", profile_values);
                userValues.setVerifiedOtp("both");
                userValues.setEmail(form_details.getUser_email().trim());
                if (userdata.isIsEmailValidated()) {
                    userValues.setMobile(userdata.getMobile());

                } else {
                    userValues.setMobile(form_details.getUser_mobile().trim());

                }
                // userValues.setMobile(form_details.getUser_mobile().trim());
                userValues.setName(form_details.getUser_name().trim());
                userValues.setTelephone(form_details.getUser_ophone().trim());
                userValues.getUserOfficialData().setDesignation(form_details.getUser_design().trim());
                userValues.getUserOfficialData().setEmployeeCode(form_details.getUser_empcode().trim());
                userValues.getUserOfficialData().setOfficeAddress(form_details.getUser_address().trim());
                userValues.getUserOfficialData().setPostingCity(form_details.getUser_city().trim());
                userValues.getUserOfficialData().setPostingState(form_details.getUser_state().trim());
                userValues.getUserOfficialData().setOfficePhone(form_details.getUser_ophone().trim());
                userValues.getUserOfficialData().setRphone(form_details.getUser_rphone().trim());
                userValues.getUserOfficialData().setPincode(form_details.getUser_pincode().trim());
                userValues.getUserOfficialData().setUnder_sec_email(form_details.getUnder_sec_email().trim());
                userValues.getUserOfficialData().setUnder_sec_name(form_details.getUnder_sec_name().trim());
                userValues.getUserOfficialData().setUnder_sec_mobile(form_details.getUnder_sec_mobile().trim());
                userValues.getUserOfficialData().setUnder_sec_tel(form_details.getUnder_sec_tel().trim());
                userValues.getUserOfficialData().setUnder_sec_desig(form_details.getunder_sec_desig().trim());
                Set s = (Set) userdata.getAliases();
                userValues.setAliases(s);
                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                    userValues.getUserOfficialData().setDepartment(form_details.getDept().trim());
                    userValues.getUserOfficialData().setMinistry(form_details.getMin().trim());
                    if (form_details.getDept().toLowerCase().trim().equals("other")) {
                        userValues.getUserOfficialData().setOther_dept(form_details.getOther_dept().trim());
                    }
                } else if (form_details.getUser_employment().trim().equals("State")) {
                    userValues.getUserOfficialData().setState(form_details.getStateCode().trim());
                    userValues.getUserOfficialData().setDepartment(form_details.getState_dept().trim());
                    if (form_details.getState_dept().toLowerCase().trim().equals("other")) {

                        System.out.println("form_details.getOther_dept().trim()" + form_details.getOther_dept().trim());
                        userValues.getUserOfficialData().setOther_dept(form_details.getOther_dept().trim());
                    }
                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                    profile_values.put("Org", form_details.getOrg().trim());
                    if (form_details.getOrg().toLowerCase().trim().equals("other")) {
                        userValues.getUserOfficialData().setOther_dept(form_details.getOther_dept().trim());
                    }
                    userValues.getUserOfficialData().setOrganizationCategory(form_details.getOrg().trim());
                }
                userValues.getUserOfficialData().setEmployment(form_details.getUser_employment().trim());
                profile_values.put("user_employment", form_details.getUser_employment().trim());
                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                    profile_values.put("min", form_details.getMin().trim());
                    profile_values.put("dept", form_details.getDept().trim());
                    if (form_details.getDept().toLowerCase().trim().equals("other")) {
                        profile_values.put("other_dept", form_details.getOther_dept().trim());
                    }

                } else if (form_details.getUser_employment().trim().equals("State")) {
                    profile_values.put("dept", form_details.getState_dept().trim());
                    profile_values.put("stateCode", form_details.getStateCode().trim());
                    if (form_details.getState_dept().toLowerCase().trim().equals("other")) {
                        profile_values.put("other_dept", form_details.getOther_dept().trim());
                    }
                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                    profile_values.put("Org", form_details.getOrg().trim());
                    if (form_details.getOrg().toLowerCase().trim().equals("other")) {
                        profile_values.put("other_dept", form_details.getOther_dept().trim());
                    }
                }
                // profile_values.put("other_dept", form_details.getOther_dept().trim());
                profile_values.put("ca_name", form_details.getCa_name().trim());
                profile_values.put("ca_design", form_details.getCa_design().trim());
                if (form_details.getCa_mobile().trim().startsWith("+91")) {
                    profile_values.put("ca_mobile", form_details.getCa_mobile().trim());
                }
                profile_values.put("ca_email", form_details.getCa_email().trim());
                profile_values.put("hod_name", form_details.getHod_name().trim());
                if (form_details.getHod_mobile().trim().startsWith("+91")) {
                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                } else {
                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                }
                profile_values.put("hod_email", form_details.getHod_email().trim());
                profile_values.put("hod_tel", form_details.getHod_tel().trim());
                userValues.getHodData().setName(form_details.getHod_name().trim());
                userValues.getHodData().setEmail(form_details.getHod_email().trim());
                if (form_details.getHod_mobile().trim().startsWith("+91")) {
                    userValues.getHodData().setMobile(form_details.getHod_mobile());
                } else {
                    userValues.getHodData().setMobile(form_details.getHod_mobile());
                }
                userValues.getHodData().setDesignation(form_details.getCa_design().trim());
                userValues.getHodData().setTelephone(form_details.getHod_tel().trim());
                userValues.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
                userValues.setIsNewUser(false);
                List<String> rolesList = new ArrayList<>();
                String rolesInString = "";
                db = new Database();

                if (userdata.isIsEmailValidated()) {
                    profile_values.put("mobile", userdata.getMobile());
//                     if (!userdata.getMobile().contains("+")) {
//                    form_details.setUser_mobile(form_details.getCountry_code() + form_details.getUser_mobile());
//                }

                } else {
                    profile_values.put("mobile", form_details.getUser_mobile().trim());
                    if (!form_details.getUser_mobile().contains("+")) {
                        form_details.setUser_mobile(form_details.getCountry_code() + form_details.getUser_mobile());
                    }

                }
                // profile_values.put("mobile", form_details.getUser_mobile().trim());
                profile_values.put("email", form_details.getUser_email().trim());
                profile_values.put("cn", form_details.getUser_name().trim());
                profile_values.put("nicCity", form_details.getUser_city().trim());
                profile_values.put("desig", form_details.getUser_design().trim());
                profile_values.put("postalAddress", form_details.getUser_address().trim());
                profile_values.put("ophone", form_details.getUser_ophone().trim());
                profile_values.put("rphone", form_details.getUser_rphone().trim());
                profile_values.put("state", form_details.getUser_state().trim());
                profile_values.put("emp_code", form_details.getUser_empcode().trim());
                profile_values.put("pin", form_details.getUser_pincode().trim());
                profile_values.put("user_alt_address", form_details.getUser_alt_address().trim());
                profile_values.put("user_employment", form_details.getUser_employment().trim());
                session.put("user_flag", true);
                session.remove("profile");
                session.put("uservalues", userValues);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "profile: values: " + profile_values);
                if (session.get("role") != null) {
                    ArrayList role = (ArrayList) session.get("role");
                    if (role.contains("user")) {
                        returnString = "user";
                    } else if (role.contains("ca")) {
                        returnString = "ca";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public void centralMin() throws IOException {
        String name = (String) ServletActionContext.getRequest().getParameter("orgType");
        list = profileService.getCentralMinistry(name);
        String json = null;
        json = new Gson().toJson(list);
        ServletActionContext.getResponse().setContentType("application/json");
        ServletActionContext.getResponse().getWriter().write(json);
    }

    public void centralDep() {
        String json = null;
        List deptList = new ArrayList();
        String name = (String) ServletActionContext.getRequest().getParameter("depType");
        deptList = profileService.getCentralDept(name);
        json = new Gson().toJson(deptList);
        ServletActionContext.getResponse().setContentType("application/json");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(Profile.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getDistrict() {
        String json = null;
        List deptList = new ArrayList();
        String name = (String) ServletActionContext.getRequest().getParameter("user_state");
        deptList = profileService.getDistrict(name);
        json = new Gson().toJson(deptList);
        ServletActionContext.getResponse().setContentType("application/json");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(Profile.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getHODdetails() {
        hodDetails = (HashMap) profileService.getHODdetails(email);
        //session.put("hodDetails", hodDetails);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "HOD DETAILS: " + hodDetails);

        return SUCCESS;
    }

    public String eformsFaqs() {
        System.out.println("com.org.controller.Profile.eformsFaqs():::::::: ");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String keyword = (String) ServletActionContext.getRequest().getParameter("srch_faq");
        try {
            if (keyword == null || keyword == "") {
                String qry = "SELECT q_category FROM `feedback` GROUP BY q_category";
                con = DbConnection.getSlaveConnection();
                ps = con.prepareStatement(qry);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Faqs faqs = new Faqs();
                    faqs.setE_cate(rs.getString(1));
                    faq_list.add(faqs);
                }
                for (Faqs l : faq_list) {
                    System.out.println("Category::: " + l.getE_cate());
                    String qury = "SELECT * FROM `feedback` WHERE q_category='" + l.getE_cate() + "'";
                    System.out.println("Query is: " + qury);
                    ps = con.prepareStatement(qury);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Faqs faqs_list = new Faqs();
                        faqs_list.setId(rs.getInt(1));
                        faqs_list.setE_cate(rs.getString(2));
                        faqs_list.setE_question(rs.getString(3));
                        faqs_list.setE_answer(rs.getString(4));
                        list.add(faqs_list);
                    }
                }
            } else {
                eformsfaqSearch(keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String eformsfaqSearch(String keyword) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String qry = "SELECT q_category FROM `feedback` where question like '%" + keyword + "%' or answer like '%" + keyword + "%' GROUP BY q_category";
        con = DbConnection.getSlaveConnection();
        ps = con.prepareStatement(qry);
        rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            Faqs faqs = new Faqs();
            faqs.setE_cate(rs.getString(1));
            faq_list.add(faqs);
            System.out.println("Category::: " + rs.getString(1));
            String qury = "SELECT * FROM `feedback` where question like '%" + keyword + "%' or answer like '%" + keyword + "%' and q_category='" + rs.getString(1) + "' ";
            System.out.println("Query is: " + qury);
            ps = con.prepareStatement(qury);
            ResultSet rs1 = ps.executeQuery();

            while (rs1.next()) {
                count++;
                Faqs faqs_list = new Faqs();
                faqs_list.setId(rs1.getInt(1));
                faqs_list.setE_cate(rs1.getString(2));
                faqs_list.setE_question(rs1.getString(3));
                faqs_list.setE_answer(rs1.getString(4));
                list.add(faqs_list);
            }
        }
        return SUCCESS;
    }

    public String fetchCoordinator() {
        try {
            FormBean fb = new FormBean();
            val.put("coordinator_list", fb.fetchCoordinator(getOrgType(), getDepType(), getOrgDept()));
        } catch (Exception ex) {
            System.out.println("Exception in fetchCoordinator() Method ::" + ex.getMessage());
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    public String fetchUrCoord() {
        UserData userdata = (UserData) session.get("uservalues");
        UserOfficialData empDetails = userdata.getUserOfficialData();
        String ministry = "", department = "", organization = "", other_dept = "", state = "";
        String form_name = "", add_state = ""; // line added by pr on 26thmar18
        if (empDetails.getMinistry() != null && !empDetails.getMinistry().equals("")) {
            ministry = empDetails.getMinistry().toString();
        }
        if (empDetails.getDepartment() != null) {
            department = empDetails.getDepartment().toString();
        }
        if (empDetails.getState() != null && !empDetails.getState().equals("")) {
            ministry = empDetails.getState().toString();
        }
        if (empDetails.getEmployment() != null && !empDetails.getEmployment().equals("")) {
            organization = empDetails.getEmployment();
        }
//        if (empDetails.getEmployment() != null) {
//            other_dept = empDetails.getOther_dept().toString();
//        }
//        // below if added by pr on 26thmar18
//        if (empDetails.getState() != null && !empDetails.getState().equals("")) {
//            add_state = empDetails.getState().toString();
//        }
        try {
            FormBean fb = new FormBean();
            if (!department.toLowerCase().contains("nic")) {
                val.put("coordinator_list", fb.fetchCoordinator(organization, department, ministry));
            }
        } catch (Exception ex) {
            System.out.println("Exception in fetchCoordinator() Method ::" + ex.getMessage());
            ex.printStackTrace();
        }
        return SUCCESS;
    }
}
