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
import com.org.service.ImapPopService;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import entities.LdapQuery;
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

public class ImapPop_registration extends ActionSupport implements SessionAware {

    Map session;
    FormBean form_details;
    Map profile_values = null;
    ImapPopService imappopservice = null;
    UpdateService updateService = null;// line added by pr on 6thfeb18
    Map<String, Object> error = null;
    String data, CSRFRandom, ReturnString;
    validation valid = null;
    ProfileService profileService = null;
    esignDao esignDao = new esignDao();
    Map hodDetails = null;
    // start, code added by pr on 10thjan18
    private String random;
    private Database db = null;
    Map<String, String> map = null;
    Ldap ldap;

    public ImapPop_registration() {
        imappopservice = new ImapPopService();
        valid = new validation();
        error = new HashMap();
        updateService = new UpdateService();
        map = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        profile_values = new HashMap();
        esignDao = new esignDao();
        ldap = new Ldap();
        if (imappopservice == null) {
            imappopservice = new ImapPopService();
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

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }
    // start, code added by pr on 6thfeb18
    String action_type;

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = Jsoup.parse(action_type).text();
    }

    // end, code added by pr on 6thfeb18
    // end, code added by pr on 10thjan18
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    @Override
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

    public String IMAPPOP_tab1() {
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            //form_details.setCSRFRandom(CSRFRandom);
            profile_values = (HashMap) session.get("profile-values");
            String email = profile_values.get("email").toString();
            System.out.println("email in imappop tab 1" + email);
            String mailAllowedServiceAccess = entities.LdapQuery.GetmailAllowedServiceAccess(email);
//            if(!form_details.getProtocol().equals("pop") || !form_details.getProtocol().equals("imap"))
//            {
//                error.put("protocol_err", "Please select correct protocol");
//            }
            if (!mailAllowedServiceAccess.contains("pop") && form_details.getProtocol().equals("pop")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for imappop controller tab 1: pop already enabled");
                error.put("service_pop_err", "POP is already enable");
            } else {
                System.out.println("pop can ");
            }
            if (!mailAllowedServiceAccess.contains("imap") && form_details.getProtocol().equals("imap")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for imappop controller tab 1: pop already enabled");
                error.put("service_imap_err", "IMAP is already enable");
            } else {
                System.out.println("imap can ");
            }
            if (form_details.getProtocol().equals("imap") || form_details.getProtocol().equals("pop")) {
            } else {
                error.put("protocol_err", "Please select correct protocol");
                form_details.setProtocol(ESAPI.encoder().encodeForHTML(form_details.getProtocol()));
            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            // start, code added by pr on 10thjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // end, code added by pr on 10thjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "error Map for imappop controller tab 1: " + error);
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    imappopservice.IMAPPOP_tab1(form_details);
                    profile_values = (HashMap) session.get("profile-values");
                }
            } else {
                data = "";
                CSRFRandom = "";
                ReturnString = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for imappop controller tab 1: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            ReturnString = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String IMAPPOP_tab2() {
        try {

            profile_values = (HashMap) session.get("profile-values");
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

            ObjectMapper mapper = new ObjectMapper();
            db = new Database();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setCSRFRandom(CSRFRandom); // line added by pr on 11thjan18
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
            String email = userdata.getEmail();
            System.out.println("email in imappop2:::::::" + email);
            String mailAllowedServiceAccess = entities.LdapQuery.GetmailAllowedServiceAccess(email);
            System.out.println("mailAllowedServiceAccess in imap pop2" + mailAllowedServiceAccess);

            if (form_details.getProtocol().equals("imap") || form_details.getProtocol().equals("pop")) {
            } else {
                error.put("protocol_err", "Please select correct protocol");
                form_details.setProtocol(ESAPI.encoder().encodeForHTML(form_details.getProtocol()));
            }
            String SessionCSRFRandom = session.get("CSRFRandom").toString(); // line added by pr on 11thjan18
            if (!SessionCSRFRandom.equals(CSRFRandom))// if else around added by pr on 11thjan18
            {
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
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (form_details.getModule() == null) {
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.roTelValidation(form_details.getHod_tel());
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
                        //Map a = (HashMap) session.get("profile-values");
                        Set s = (Set) userdata.getAliases();
                        System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);
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
            if (!mailAllowedServiceAccess.contains("imap") && form_details.getProtocol().equals("imap")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for imappop controller tab 1: pop already enabled");
                error.put("service_imap_err", "IMAP is already enable");
            } else {
                System.out.println("imap can ");
            }
            if (!mailAllowedServiceAccess.contains("pop") && form_details.getProtocol().equals("pop")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for imappop controller tab 1: pop already enabled");
                error.put("service_pop_err", "POP is already enable");
            } else {
                System.out.println("pop can ");
            }
            // end validate profile 20th march
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "error Map for imappop controller tab 2: " + error);
            if (getAction_type().equals("submit")) {
                System.out.println("inside submit#########################");
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    if (session.get("update_without_oldmobile").equals("no")) {
                        session.put("imappop_details", form_details);
                        String ref_num = imappopservice.IMAPPOP_tab2(form_details, profile_values);
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

                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (action_type.equals("update")) {
                email = userdata.getEmail();
                System.out.println("email in imappop2 update:::::::" + email);
                String ref = session.get("ref").toString();
                //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                String admin_role = "NA";
                if (session.get("admin_role") != null) {
                    admin_role = session.get("admin_role").toString();
                }
                UserData userdata1 = (UserData) session.get("uservalues");
                System.out.println("EMAIL IN UPDATE " + userdata1.getEmail());
                Boolean fl = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                if (fl && form_details.getModule().equals("user")) {

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
                ReturnString = "";
                action_type = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for imappop controller tab 2: " + error);
            data = "";
            CSRFRandom = "";
            ReturnString = "";
            action_type = "";
        }
        return SUCCESS;
    }

}
