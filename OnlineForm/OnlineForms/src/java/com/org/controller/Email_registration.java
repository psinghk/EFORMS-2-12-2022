package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.bean.UserOfficialData;
import com.org.connections.SingleApplicationContext;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.dao.esignDao;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import entities.LdapQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import utility.ExcelCreator;
import validation.BulkValidation;
import validation.validation;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.org.bean.EmailBean;
import com.org.bean.ValidatedEmailBean;
import com.org.connections.SingleApplicationContextSlave;
import com.org.dao.DbDao;
import com.org.dao.EmailDao;
import com.org.service.EmailService;
import com.org.utility.Constants;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Random;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Nikki
 */
public class Email_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    String boForUserRetiringThisMonth;
    Map session = new HashMap<>();
    //private SessionMap<String, Object> session;
    Map profile_values = null;
    EmailBean emaildata = new EmailBean();
    EmailService emailservice = null;
    UpdateService updateService = null;// line added by pr on 6thfeb18
    String data, request_type, whichemail, cert, whichform, action_type, CSRFRandom, emailrequest, form_type, id_type, emp_type, email_id, dateOfRetirement, loggedInEmail, formTypeDor, dateOfBirth;
    ProfileService profileService = null;
    Signup signup = null;
    Map error = null;
    Map hodDetails = null;
    Map underSecDetails = null;
    private Ldap ldap = null;
    Map fwdofcDetails = null;
    esignDao esignDao = null;
    Map<String, String> map = null;
    private Database db = null;
    private Set<String> allowedDomains = null;
    String employment, ministry, department, state, stateDepartment, organization, user_type, applicant_mobile, yourCoordinator;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    List<Map<String, String>> campaigns = new ArrayList<>();
    public int EmailbulkDataId = 0;
    public String EmailbulkDataStr = "";
    public ValidatedEmailBean emailEditData = new ValidatedEmailBean();
    Map<String, Object> bulkData = new HashMap<>();
    BulkValidation bulkValidate = new BulkValidation();
    validation valid = new validation();
    int iCampaignId;
    //int campaignId;
    String emailbulkData = "";
    String coordsOrDas = "";
    String errorMessageRetiring = "";

    public Email_registration() {
        emailservice = new EmailService();
        map = new HashMap<>();
        ldap = new Ldap();
        db = new Database();
        emaildata = new EmailBean();
        underSecDetails = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        error = new HashMap<>();
        valid = new validation();
        signup = new Signup();
        updateService = new UpdateService();
        profile_values = new HashMap();
        fwdofcDetails = new HashMap();
        esignDao = new esignDao();
        bulkData = new HashMap<>();
        emailEditData = new ValidatedEmailBean();

        if (emailservice == null) {
            emailservice = new EmailService();
        }
        if (fwdofcDetails == null) {
            fwdofcDetails = new HashMap();
        }
        if (esignDao == null) {
            esignDao = new esignDao();
        }
        if (underSecDetails == null) {
            underSecDetails = new HashMap();
        }
        if (ldap == null) {
            ldap = new Ldap();
        }
        if (db == null) {
            db = new Database();
        }

        if (signup == null) {
            signup = new Signup();
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
        if (campaigns == null) {
            campaigns = new ArrayList<>();
        }
        if (emaildata == null) {
            emaildata = new EmailBean();
        }
        if (bulkData == null) {
            bulkData = new HashMap<>();
        }

        if (emailEditData == null) {
            emailEditData = new ValidatedEmailBean();
        }
    }

    public String getErrorMessageRetiring() {
        return errorMessageRetiring;
    }

    public void setErrorMessageRetiring(String errorMessageRetiring) {
        this.errorMessageRetiring = errorMessageRetiring;
    }
    
    public String getCoordsOrDas() {
        return coordsOrDas;
    }

    public void setCoordsOrDas(String coordsOrDas) {
        this.coordsOrDas = coordsOrDas;
    }

    public String getBoForUserRetiringThisMonth() {
        return boForUserRetiringThisMonth;
    }

    public void setBoForUserRetiringThisMonth(String boForUserRetiringThisMonth) {
        this.boForUserRetiringThisMonth = boForUserRetiringThisMonth;
    }

    public EmailBean getEmailBean() {
        return emaildata;
    }

    public void setEmailBean(EmailBean emaildata) {
        this.emaildata = emaildata;
    }

    public String getEmail_id() {
        return email_id;
    }

    public EmailBean getEmaildata() {
        return emaildata;
    }

    public void setEmaildata(EmailBean emaildata) {
        this.emaildata = emaildata;
    }

    public ValidatedEmailBean getEmailEditData() {
        return emailEditData;
    }

    public void setEmailEditData(ValidatedEmailBean emailEditData) {
        this.emailEditData = emailEditData;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getEmailbulkDataId() {
        return EmailbulkDataId;
    }

    public void setEmailbulkDataId(int EmailbulkDataId) {
        this.EmailbulkDataId = EmailbulkDataId;
    }

    public String getEmailbulkDataStr() {
        return EmailbulkDataStr;
    }

    public void setEmailbulkDataStr(String EmailbulkDataStr) {
        this.EmailbulkDataStr = EmailbulkDataStr;
    }

    public Map<String, Object> getBulkData() {
        return bulkData;
    }

    public void setBulkData(Map<String, Object> bulkData) {
        this.bulkData = bulkData;
    }

    public String getEmailbulkData() {
        return emailbulkData;
    }

    public void setEmailbulkData(String emailbulkData) {
        this.emailbulkData = emailbulkData;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDateOfRetirement() {
        return dateOfRetirement;
    }

    public void setDateOfRetirement(String dateOfRetirement) {
        this.dateOfRetirement = dateOfRetirement;
    }

    public String getYourCoordinator() {
        return yourCoordinator;
    }

    public String getLoggedInEmail() {
        return loggedInEmail;
    }

    public void setLoggedInEmail(String loggedInEmail) {
        this.loggedInEmail = loggedInEmail;
    }

    public String getFormTypeDor() {
        return formTypeDor;
    }

    public void setFormTypeDor(String formTypeDor) {
        this.formTypeDor = formTypeDor;
    }

    public void setYourCoordinator(String yourCoordinator) {
        this.yourCoordinator = yourCoordinator;
    }

    public String getApplicant_mobile() {
        return applicant_mobile;
    }

    public void setApplicant_mobile(String applicant_mobile) {
        this.applicant_mobile = applicant_mobile;
    }

    public List<Map<String, String>> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Map<String, String>> campaigns) {
        this.campaigns = campaigns;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getMinistry() {
        return ministry;
    }

    public void setMinistry(String ministry) {
        this.ministry = ministry;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDepartment() {
        return stateDepartment;
    }

    public void setStateDepartment(String stateDepartment) {
        this.stateDepartment = stateDepartment;
    }

    public String getOrganization() {
        return organization;
    }

    public int getiCampaignId() {
        return iCampaignId;
    }

    public void setiCampaignId(int iCampaignId) {
        this.iCampaignId = iCampaignId;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getForm_type() {
        return form_type;
    }

    public void setForm_type(String form_type) {
        this.form_type = form_type;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }

    public Set<String> getAllowedDomains() {
        return allowedDomains;
    }

    public String getEmailrequest() {
        return emailrequest;
    }

    public void setEmailrequest(String emailrequest) {
        this.emailrequest = emailrequest;
    }

    public Map getUnderSecDetails() {
        return underSecDetails;
    }

    public void setUnderSecDetails(Map underSecDetails) {
        this.underSecDetails = underSecDetails;
    }

    public Map getHodDetails() {
        return hodDetails;
    }

    public void setHodDetails(Map hodDetails) {
        this.hodDetails = hodDetails;
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

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = Jsoup.parse(request_type).text();
    }

    @Override
    public void setSession(Map session) {
        this.session = session;
    }

    public Map getSession() {
        return session;
    }

//    public SessionMap<String, Object> getSession() {
//        return session;
//    }
//
//    public void setSession(SessionMap<String, Object> session) {
//        this.session = session;
//    }
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

    public Map getError() {
        return error;
    }

    public void setError(Map error) {
        this.error = error;
    }

    public String getWhichemail() {
        return whichemail;
    }

    public void setWhichemail(String whichemail) {
        this.whichemail = Jsoup.parse(whichemail).text();
    }

    public String getWhichform() {
        return whichform;
    }

    public void setWhichform(String whichform) {
        this.whichform = Jsoup.parse(whichform).text();
    }

    public String execute() throws Exception {

        System.out.println("in executeeeeeeeeee");
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = Jsoup.parse(action_type).text();
    }
    Boolean dup_flag = false;

    public String initializeEmailPage() {
        if (session.get("profile-values") != null) {
            profile_values = (HashMap) session.get("profile-values");
            String dept = "";

            if (profile_values.get("user_employment").equals("Others") || profile_values.get("user_employment").equals("Psu") || profile_values.get("user_employment").equals("Const") || profile_values.get("user_employment").equals("Nkn") || profile_values.get("user_employment").equals("Project")) {

                dept = profile_values.get("Org").toString();
            } else {
                dept = profile_values.get("dept").toString();
            }

            yourCoordinator = fetchCoordinator(dept);
        }
        return SUCCESS;
    }

    public String Single_tab1() {
        try {
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("profile_values in seesion" + profile_values.get("email"));
//            if (!userdata.isIsEmailValidated()) {
//                System.out.println("if not validated");
//                returnString = signup.checkrequest(userdata.getEmail());
//                System.out.println("returnString" + returnString);
//            }

            if (!userdata.isIsEmailValidated()) {
                System.out.println("if not validated");

                Set<String> emailsAlreadyCreated = signup.fetchIdMapped(userdata.getEmail());
                boolean flag = false;
                for (String emailCreated : emailsAlreadyCreated) {
                    if (ldap.emailValidate(emailCreated)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    returnString = signup.checkrequest(userdata.getEmail());
                } else {
                    returnString = "completed";
                }

                System.out.println("returnString" + returnString);
            }
            if (!returnString.equals("pending") && !returnString.equals("completed")) {

                error.clear();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);
                if (!userdata.isIsNICEmployee() && form_details.getReq_user_type().equals("other_user")) {

                    error.put("email_info_error1", "Please come with government email address to apply for other applicant");

                } else {

                    System.out.println("form details applocant employmnet:::::::::" + form_details.getApplicant_employment() + "profilevalues" + profile_values);
                    Boolean flag = false;
                    //String dup_email = "";

//                if (session.get("dupemail") != null) {
//                    dup_email = session.get("dupemail").toString();
//                } else {
//                    dup_email = "";
//                }
//                if (dup_email.equals("yes")) {
//                    flag = true;
//                    error.put("dup_err", "Already we have e-Mail address registered against this mobile number");
//                    form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
//                } else {
                    if (form_details.getReq_user_type().equals("self")) {
                        if (session.get("profile-values") != null) {
                            profile_values = (HashMap) session.get("profile-values");
                            employment = profile_values.get("user_employment").toString();
                            if (profile_values.get("min") != null) {
                                ministry = profile_values.get("min").toString();
                            }
                            if (profile_values.get("dept") != null) {
                                department = profile_values.get("dept").toString();
                            }
                            if (profile_values.get("stateCode") != null) {
                                state = profile_values.get("stateCode").toString();
                            }
                            if (profile_values.get("dept") != null) {
                                stateDepartment = profile_values.get("dept").toString();

                            }
                            if (profile_values.get("Org") != null) {
                                organization = profile_values.get("Org").toString();

                            }
                            session.put("prEmployment", employment);
                            session.put("prMinistry", ministry);
                            session.put("prDepartment", department);
                            session.put("prState", state);
                            session.put("prStateDepartment", stateDepartment);
                            session.put("prOrganization", organization);
                        }
                    } else {

                        employment = form_details.getApplicant_employment();
                        ministry = form_details.getApplicant_min();
                        department = form_details.getApplicant_dept();
                        state = form_details.getApplicant_stateCode();
                        stateDepartment = form_details.getApplicant_Smi();
                        organization = form_details.getApplicant_Org();
                        user_type = form_details.getReq_user_type();
                        applicant_mobile = form_details.getApplicant_mobile();
                    }

                    if (!form_details.getReq_user_type().equals("self")) {
                        form_details.setReq_for(form_details.getApplicant_req_for());
                        form_details.setSingle_id_type(form_details.getApplicant_single_id_type());
                        form_details.setSingle_emp_type(form_details.getApplicant_single_emp_type());
                        form_details.setSingle_email1(form_details.getApplicant_single_email1());
                        form_details.setSingle_email2(form_details.getApplicant_single_email2());
                        form_details.setSingle_dob(form_details.getApplicant_single_dob());
                        form_details.setSingle_dor(form_details.getApplicant_single_dor());

                        boolean applicant_name_error = valid.nameValidation(form_details.getApplicant_name());
                        boolean applicant_mobile_error = valid.applicantMobileValidation(form_details.getApplicant_mobile());

                        boolean applicant_desg_error = valid.desigValidation(form_details.getApplicant_design());
                        boolean applicant_emp_code_error = valid.employcodevalidation(form_details.getApplicant_empcode());
                        boolean applicant_email_error = valid.EmailValidation(form_details.getApplicant_email());
                        boolean applicant_state_error = valid.addstateValidation(form_details.getApplicant_state());
                        if (applicant_name_error == true) {
                            error.put("applicant_name_error", "Enter Full Name [Only characters,dot(.) and whitespaces allowed]");
                        }

                        if (applicant_state_error == true) {
                            error.put("applicant_state_error", "Please enter State in correct format");
                        }
                        if (applicant_mobile_error == true) {
                            error.put("applicant_mobile_error", "Enter Mobile Number [e.g:+919999999999]");
                        }

                        if (applicant_emp_code_error == true) {
                            error.put("applicant_emp_code_error", "Enter Employee Code [Only characters,digits and underscore(_) allowed]");
                        }

                        if (applicant_email_error == true) {
                            error.put("applicant_email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                        }
                        if (applicant_desg_error == true) {
                            error.put("applicant_desg_error", "Enter Designation [characters,Alphanumeirc,whitespaces and [. , - &]] allowed");
                        }
                        System.out.println("form_details.getApplicant_employment()" + form_details.getApplicant_employment());
                        boolean employment_error = valid.EmploymentValidation(form_details.getApplicant_employment());
                        System.out.println("employment_error" + employment_error);
                        if (employment_error == true) {
                            error.put("applicant_employment_error", "Please enter Employment in correct format");
                        } else {
                            if (form_details.getApplicant_employment().equals("Central")) {
                                if (form_details.getApplicant_min() == null) {
                                    error.put("applicant_minerror", "Please enter Ministry in correct format");
                                } else if (form_details.getApplicant_dept() == null) {
                                    error.put("applicant_deperror", "Please enter Department in correct format");
                                } else {
                                    boolean ministry_error = valid.MinistryValidation(form_details.getApplicant_min());
                                    boolean dept_error = valid.MinistryValidation(form_details.getApplicant_dept());
                                    if (ministry_error == true) {
                                        error.put("applicant_minerror", "Please enter Ministry in correct format");
                                    }
                                    List minlist = profileService.getCentralMinistry(form_details.getApplicant_employment());
                                    System.out.println("minlist" + minlist);
                                    System.out.println("form_details.getMin()" + form_details.getApplicant_min());
                                    if (!minlist.contains(form_details.getApplicant_min())) {
                                        error.put("applicant_minerror", "Please enter Ministry in correct format");
                                        // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");

                                    }
                                    if (dept_error == true) {
                                        error.put("applicant_dept_error", "Please enter Department in correct format");
                                    }

                                    List deplist = profileService.getCentralDept(form_details.getApplicant_min());
                                    System.out.println("deplist" + deplist);
                                    if (!form_details.getApplicant_dept().toLowerCase().equalsIgnoreCase("other")) {
                                        if (!deplist.contains(form_details.getApplicant_dept())) {
                                            error.put("applicant_dept_error", "Please enter Ministry in correct format");
                                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                                        }
                                    }
                                    if (form_details.getApplicant_dept().toLowerCase().equals("other")) {
                                        if (form_details.getApplicant_other_dept() != null || !form_details.getApplicant_other_dept().equals("")) {
                                            boolean applicant_dept_other_error = valid.MinistryValidation(form_details.getApplicant_other_dept());
                                            if (applicant_dept_other_error == true) {
                                                error.put("applicant_dept_other_error", "Please enter Other Department in correct format");
                                            }
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                                        }
                                    }
                                }
                            } else if (form_details.getApplicant_employment().equals("State")) {
                                if (form_details.getApplicant_Smi() == null) {
                                    error.put("state_dept_error", "Please enter State Department in correct format");
                                } else if (form_details.getApplicant_stateCode() == null) {
                                    error.put("applicant_state_error", "Please enter State in correct format");
                                } else {

                                    boolean state_dept_error = valid.MinistryValidation(form_details.getApplicant_Smi());
                                    boolean state_error = valid.nameValidation(form_details.getApplicant_stateCode());
                                    if (state_dept_error == true) {
                                        error.put("applicant_state_dept_error", "Please enter State Department in correct format");
                                    }
                                    if (state_error == true) {
                                        error.put("applicant_state_error", "Please enter State in correct format");
                                    }

                                    List<String> statedeplist = profileService.getCentralDept(form_details.getApplicant_stateCode());

                                    if (!form_details.getApplicant_Smi().toLowerCase().equalsIgnoreCase("other")) {
                                        if (!statedeplist.contains(form_details.getApplicant_Smi())) {
                                            error.put("applicant_state_dept_error", "Please enter correct State Department");
                                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                                        }
                                    }
                                    if (form_details.getApplicant_Smi().toLowerCase().equals("other")) {
                                        if (form_details.getApplicant_other_dept() != null || !form_details.getApplicant_other_dept().equals("")) {
                                            boolean dept_other_error = valid.MinistryValidation(form_details.getApplicant_other_dept());
                                            if (dept_other_error == true) {
                                                error.put("applicant_dept_other_error", "Please enter Other Department in correct format");
                                            }
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                                        }
                                    }
                                }
                            } else if (form_details.getApplicant_employment().equals("Others") || form_details.getApplicant_employment().equals("Psu") || form_details.getApplicant_employment().equals("Const") || form_details.getApplicant_employment().equals("Nkn") || form_details.getApplicant_employment().equals("Project")) {

                                if (form_details.getApplicant_Org() == null) {

                                    error.put("applicant_org_error", "Please enter Organization in correct format");
                                } else {
                                    boolean org_error = valid.MinistryValidation(form_details.getApplicant_Org());
                                    if (org_error == true) {
                                        error.put("applicant_org_error", "Please enter Organization in correct format");
                                    }

                                    List orglist = profileService.getCentralMinistry(form_details.getApplicant_employment());
                                    System.out.println("orglist@@@@@@@@" + orglist);
                                    if (!form_details.getApplicant_Org().toLowerCase().equalsIgnoreCase("other")) {
                                        if (!orglist.contains(form_details.getApplicant_Org())) {
                                            error.put("applicant_org_error", "Please enter correct department");
                                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                                        }
                                    }
                                    if (form_details.getApplicant_Org().toLowerCase().equals("other")) {
                                        if (form_details.getApplicant_other_dept() != null || !form_details.getApplicant_other_dept().equals("")) {
                                            boolean dept_other_error = valid.MinistryValidation(form_details.getApplicant_other_dept());
                                            if (dept_other_error == true) {
                                                error.put("applicant_dept_other_error", "Please enter Other Department in correct format");
                                            }
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                                        }
                                    }
                                }
                            }
                        }

                    }
                    boolean pref1_error = valid.EmailValidation(form_details.getSingle_email1());
                    boolean pref2_error = valid.EmailValidation(form_details.getSingle_email2());
                    if (form_details.getReq_for().equals("mail") || form_details.getReq_for().equals("app") || form_details.getReq_for().equals("eoffice")) {

                    } else {
                        flag = true;
                        error.put("req_for_err", "Choose Type of Mail ID");
                        form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
                    }

                    if (form_details.getSingle_id_type().equals("id_name") || form_details.getSingle_id_type().equals("id_desig")) {

                    } else {
                        flag = true;
                        error.put("single_id_type_err", "Choose Email address preference");
                        form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_id_type()));
                    }

                    if (form_details.getSingle_emp_type().equals("emp_regular") || form_details.getSingle_emp_type().equals("emp_contract") || form_details.getSingle_emp_type().equals("consultant")) {

                    } else {
                        flag = true;
                        error.put("single_emp_type_err", "Choose Employee Description");
                        form_details.setSingle_emp_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_emp_type()));
                    }

                    if (pref1_error == true) {
                        error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email1());
                        setWhichemail("1");
                        setWhichform("single_form1," + form_details.getSingle_emp_type() + "," + form_details.getSingle_id_type());
                        checkAvailableEmail();
                    }
                    if (pref2_error == true) {
                        error.put("pref2_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email2());
                        setWhichemail("2");
                        setWhichform("single_form1," + form_details.getSingle_emp_type() + "," + form_details.getSingle_id_type());
                        checkAvailableEmail();
                    }
                    if (form_details.getSingle_email1().equals(form_details.getSingle_email2())) {
                        error.put("pref2_error", "Preferred Email2 cannot be same as Preferred Email1");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    }
                    if (form_details.getSingle_email1().contains("eauth.in")) {     //changed by vivek on 21 Feb 2022
                        form_details.setReq_for("app");
                    }
                    String dob_err = valid.dobValidation(form_details.getSingle_dob());
                    if (!dob_err.isEmpty() || !"".equals(dob_err)) {
                        flag = true;
                        error.put("dob_err", dob_err);
                        form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
                    }
                    String dor_err = valid.dorValidation(form_details.getSingle_dor(), form_details.getSingle_dob());
                    if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                        flag = true;
                        error.put("dor_err", dor_err);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                    }
                    String capcode = (String) session.get("captcha");
                    if (!form_details.getImgtxt().equals(capcode)) {
                        error.put("cap_error", "Please enter Correct Captcha");
                        form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                    }

                    // start, code added by pr on 22ndjan18
                    String SessionCSRFRandom = session.get("CSRFRandom").toString();
                    if (!SessionCSRFRandom.equals(CSRFRandom)) {
                        flag = true;
                        error.put("csrf_error", "Invalid Security Token !");
                    }

                    if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {
                        if (session.get("update_without_oldmobile").equals("no")) {
                            emailservice.Single_tab1(form_details);

                            System.out.println("profile_values.get(\"hod_email\")###############" + profile_values.get("hod_email"));
                            hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                            String bo = hodDetails.get("bo").toString();
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                            profile_values.put("bo_check", bo);
                        }
                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                    //}

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + error);
                    // end, code added by pr on 22ndjan18

                }
            } else {
                error.put("email_info_error", "Your request is already pending/completed so you can not fill any more request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String Single_tab2() {
        db = new Database();
        UserData userdata = (UserData) session.get("uservalues");
        boolean ldap_employee = userdata.isIsEmailValidated();
        boolean nic_employee = userdata.isIsNICEmployee();
        HodData hoddata = (HodData) userdata.getHodData();
        UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();
        String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
        ldap = new Ldap();

        try {
            error.clear();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IN TAB 2");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            if (!userdata.isIsNICEmployee() && form_details.getReq_user_type().equals("other_user")) {
                error.put("email_info_error1", "Please come with government email address to apply for other applicant");

            } else {

                Boolean flag = false;
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                String bo = hodDetails.get("bo").toString();
                underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                String bo_undersec = underSecDetails.get("bo").toString();
                profile_values = (HashMap) session.get("profile-values");
                userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
                if (ldap_employee) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                } else {
                    if (userdata.getHodData().isGovEmployee()) {
                        profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                        form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                    }
                }

                if (form_details.getReq_user_type().equals("self")) {

                    boolean pref1_error = valid.EmailValidation(form_details.getSingle_email1());
                    boolean pref2_error = valid.EmailValidation(form_details.getSingle_email2());
                    if (form_details.getReq_for() != null) {
                        if (form_details.getReq_for().equals("mail") || form_details.getReq_for().equals("app") || form_details.getReq_for().equals("eoffice")) {

                        } else {
                            flag = true;
                            error.put("req_for_err", "Choose Type of Mail ID");
                            form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
                        }
                    } else {
                        flag = true;
                        error.put("req_for_err", "Choose Type of Mail ID");
                    }
                    if (form_details.getSingle_id_type() != null) {
                        if (form_details.getSingle_id_type().equals("id_name") || form_details.getSingle_id_type().equals("id_desig")) {

                        } else {
                            flag = true;
                            error.put("single_id_type_err", "Choose Email address preference");
                            form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_id_type()));
                        }

                        if (form_details.getSingle_emp_type() != null) {
                            if (form_details.getSingle_emp_type().equals("emp_regular") || form_details.getSingle_emp_type().equals("emp_contract") || form_details.getSingle_emp_type().equals("consultant")) {

                            } else {
                                flag = true;
                                error.put("single_emp_type_err", "Choose Employee Description");
                                form_details.setSingle_emp_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_emp_type()));
                            }
                        } else {
                            flag = true;
                            error.put("single_emp_type_err", "Choose Employee Description");
                            form_details.setSingle_emp_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_emp_type()));
                        }
                    } else {
                        flag = true;
                        error.put("single_emp_type_err", "Choose Employee Description");
                        form_details.setSingle_emp_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_emp_type()));
                    }

                    session.put("prEmployment", form_details.getUser_employment());
                    session.put("prMinistry", form_details.getMin());
                    session.put("prDepartment", form_details.getDept());
                    session.put("prState", form_details.getStateCode());
                    session.put("prStateDepartment", form_details.getDept());
                    session.put("prOrganization", form_details.getOrg());
                    if (pref1_error == true) {
                        error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email1());
                        setWhichemail("1");
                        setWhichform("single_form2," + form_details.getSingle_emp_type() + "," + form_details.getSingle_id_type());
                        checkAvailableEmail();
                    }
                    if (pref2_error == true) {
                        error.put("pref2_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email2());
                        setWhichemail("2");
                        setWhichform("single_form2," + form_details.getSingle_emp_type() + "," + form_details.getSingle_id_type());
                        checkAvailableEmail();
                    }

                    if (form_details.getSingle_email1().equals(form_details.getSingle_email2())) {
                        error.put("pref2_error", "Preferred Email2 cannot be same as Preferred Email1");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    }

                    String dob_err = valid.dobValidation(form_details.getSingle_dob());
                    if (!dob_err.isEmpty() || !"".equals(dob_err)) {
                        error.put("dob_err", dob_err);
                        form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
                        flag = true;
                    }
                    String dor_err = valid.dorValidation(form_details.getSingle_dor(), form_details.getSingle_dob());
                    if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                        error.put("dor_err", dor_err);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                        flag = true;
                    }

                } else {
                    boolean applicant_name_error = valid.nameValidation(form_details.getApplicant_name());
                    boolean applicant_mobile_error = valid.applicantMobileValidation(form_details.getApplicant_mobile());
                    System.out.println("applicant_mobile_error in single tab2" + applicant_mobile_error);
                    boolean applicant_desg_error = valid.desigValidation(form_details.getApplicant_design());
                    boolean applicant_emp_code_error = valid.employcodevalidation(form_details.getApplicant_empcode());
                    boolean applicant_email_error = valid.EmailValidation(form_details.getApplicant_email());
                    boolean applicant_state_error = valid.addstateValidation(form_details.getApplicant_state());
                    if (applicant_name_error == true) {
                        error.put("applicant_name_error", "Enter Full Name [Only characters,dot(.) and whitespaces allowed]");
                    }

                    if (applicant_state_error == true) {
                        error.put("applicant_state_error", "Please enter State in correct format");
                    }
                    if (applicant_mobile_error == true) {
                        error.put("applicant_mobile_error", "Enter Mobile Number [e.g:+919999999999]");
                    }

                    if (applicant_emp_code_error == true) {
                        error.put("applicant_emp_code_error", "Enter Employee Code [Only characters,digits and underscore(_) allowed]");
                    }

                    if (applicant_email_error == true) {
                        error.put("applicant_email_error", "Enter Email Address [e.g: abc.xyz@zxc.com]");
                    }
                    if (applicant_desg_error == true) {
                        error.put("applicant_desg_error", "Enter Designation [characters,Alphanumeirc,whitespaces and [. , - &]] allowed");
                    }

                    boolean employment_error = valid.EmploymentValidation(form_details.getApplicant_employment());
                    if (employment_error == true) {
                        error.put("applicant_employment_error", "Please enter Employment in correct format");
                    } else {
                        if (form_details.getApplicant_employment().equals("Central")) {
                            if (form_details.getApplicant_min() == null) {
                                error.put("applicant_minerror", "Please enter Ministry in correct format");
                            } else if (form_details.getApplicant_dept() == null) {
                                error.put("applicant_deperror", "Please enter Department in correct format");
                            } else {
                                boolean ministry_error = valid.MinistryValidation(form_details.getApplicant_min());
                                boolean dept_error = valid.MinistryValidation(form_details.getApplicant_dept());
                                if (ministry_error == true) {
                                    error.put("applicant_minerror", "Please enter Ministry in correct format");
                                }
                                List minlist = profileService.getCentralMinistry(form_details.getApplicant_employment());
                                System.out.println("minlist" + minlist);
                                System.out.println("form_details.getMin()" + form_details.getApplicant_min());
                                if (!minlist.contains(form_details.getApplicant_min())) {
                                    error.put("applicant_minerror", "Please enter Ministry in correct format");
                                    // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");

                                }
                                if (dept_error == true) {
                                    error.put("applicant_dept_error", "Please enter Department in correct format");
                                }

                                List deplist = profileService.getCentralDept(form_details.getApplicant_min());
                                System.out.println("deplist" + deplist);
                                if (!form_details.getApplicant_dept().toLowerCase().equalsIgnoreCase("other")) {
                                    if (!deplist.contains(form_details.getApplicant_dept())) {
                                        error.put("applicant_dept_error", "Please enter Ministry in correct format");
                                        // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                                    }
                                }
                                if (form_details.getApplicant_dept().toLowerCase().equals("other")) {
                                    if (form_details.getApplicant_other_dept() != null || !form_details.getApplicant_other_dept().equals("")) {
                                        boolean applicant_dept_other_error = valid.MinistryValidation(form_details.getApplicant_other_dept());
                                        if (applicant_dept_other_error == true) {
                                            error.put("applicant_dept_other_error", "Please enter Other Department in correct format");
                                        }
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                                    }
                                }
                            }
                        } else if (form_details.getApplicant_employment().equals("State")) {
                            if (form_details.getApplicant_Smi() == null) {
                                error.put("state_dept_error", "Please enter State Department in correct format");
                            } else if (form_details.getApplicant_stateCode() == null) {
                                error.put("applicant_state_error", "Please enter State in correct format");
                            } else {

                                boolean state_dept_error = valid.MinistryValidation(form_details.getApplicant_Smi());
                                boolean state_error = valid.nameValidation(form_details.getApplicant_stateCode());
                                if (state_dept_error == true) {
                                    error.put("applicant_state_dept_error", "Please enter State Department in correct format");
                                }
                                if (state_error == true) {
                                    error.put("applicant_state_error", "Please enter State in correct format");
                                }

                                List<String> statedeplist = profileService.getCentralDept(form_details.getApplicant_stateCode());

                                if (!form_details.getApplicant_Smi().toLowerCase().equalsIgnoreCase("other")) {
                                    if (!statedeplist.contains(form_details.getApplicant_Smi())) {
                                        error.put("applicant_state_dept_error", "Please enter correct State Department");
                                        // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                                    }
                                }
                                if (form_details.getApplicant_Smi().toLowerCase().equals("other")) {
                                    if (form_details.getApplicant_other_dept() != null || !form_details.getApplicant_other_dept().equals("")) {
                                        boolean dept_other_error = valid.MinistryValidation(form_details.getApplicant_other_dept());
                                        if (dept_other_error == true) {
                                            error.put("applicant_dept_other_error", "Please enter Other Department in correct format");
                                        }
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                                    }
                                }
                            }
                        } else if (form_details.getApplicant_employment().equals("Others") || form_details.getApplicant_employment().equals("Psu") || form_details.getApplicant_employment().equals("Const") || form_details.getApplicant_employment().equals("Nkn") || form_details.getApplicant_employment().equals("Project")) {
                            if (form_details.getOrg() == null) {
                                error.put("applicant_org_error", "Please enter Organization in correct format");
                            } else {
                                boolean org_error = valid.MinistryValidation(form_details.getApplicant_Org());
                                if (org_error == true) {
                                    error.put("applicant_org_error", "Please enter Organization in correct format");
                                }

                                List orglist = profileService.getCentralMinistry(form_details.getApplicant_employment());
                                System.out.println("orglist@@@@@@@@" + orglist);
                                if (!form_details.getApplicant_Org().toLowerCase().equalsIgnoreCase("other")) {
                                    if (!orglist.contains(form_details.getApplicant_Org())) {
                                        error.put("applicant_org_error", "Please enter correct department");
                                        // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                                    }
                                }
                                if (form_details.getApplicant_Org().toLowerCase().equals("other")) {
                                    if (form_details.getApplicant_other_dept() != null || !form_details.getApplicant_other_dept().equals("")) {
                                        boolean dept_other_error = valid.MinistryValidation(form_details.getApplicant_other_dept());
                                        if (dept_other_error == true) {
                                            error.put("applicant_dept_other_error", "Please enter Other Department in correct format");
                                        }
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                                    }
                                }
                            }
                        }
                    }

                    boolean pref1_error = valid.EmailValidation(form_details.getApplicant_single_email1());
                    boolean pref2_error = valid.EmailValidation(form_details.getApplicant_single_email2());
                    if (form_details.getApplicant_req_for() != null) {
                        if (form_details.getApplicant_req_for().equals("mail") || form_details.getApplicant_req_for().equals("app") || form_details.getApplicant_req_for().equals("eoffice")) {

                        } else {
                            flag = true;
                            error.put("applicant_req_for_err", "Choose Type of Mail ID");
                            form_details.setApplicant_single_id_type(ESAPI.encoder().encodeForHTML(form_details.getApplicant_req_for()));
                        }
                    } else {
                        flag = true;
                        error.put("applicant_req_for_err", "Choose Type of Mail ID");
                        form_details.setApplicant_single_id_type(ESAPI.encoder().encodeForHTML(form_details.getApplicant_req_for()));
                    }

                    if (form_details.getApplicant_single_id_type().equals("id_name") || form_details.getApplicant_single_id_type().equals("id_desig")) {

                    } else {
                        flag = true;
                        error.put("applicant_single_id_type_err", "Choose Email address preference");
                        form_details.setApplicant_single_id_type(ESAPI.encoder().encodeForHTML(form_details.getApplicant_single_id_type()));
                    }
                    if (form_details.getApplicant_single_emp_type().equals("emp_regular") || form_details.getApplicant_single_emp_type().equals("emp_contract") || form_details.getApplicant_single_emp_type().equals("consultant")) {

                    } else {
                        flag = true;
                        error.put("applicant_single_emp_type_err", "Choose Employee Description");
                        form_details.setSingle_emp_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_emp_type()));
                    }
                    session.put("prEmployment", form_details.getApplicant_employment());
                    session.put("prMinistry", form_details.getApplicant_min());
                    session.put("prDepartment", form_details.getApplicant_dept());
                    session.put("prState", form_details.getApplicant_stateCode());
                    session.put("prStateDepartment", form_details.getApplicant_Smi());
                    session.put("prOrganization", form_details.getApplicant_Org());
                    user_type = form_details.getReq_user_type();
                    applicant_mobile = form_details.getApplicant_mobile();
                    if (pref1_error == true) {
                        error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                        flag = true;
                    } else {
                        setData(form_details.getApplicant_single_email1());
                        setWhichemail("1");
                        setWhichform("single_form2," + form_details.getApplicant_single_emp_type() + "," + form_details.getApplicant_single_id_type());
                        checkAvailableEmail();
                    }
                    if (pref2_error == true) {
                        error.put("pref2_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setApplicant_single_email2(ESAPI.encoder().encodeForHTML(form_details.getApplicant_single_email2()));
                        flag = true;
                    } else {
                        setData(form_details.getApplicant_single_email2());
                        setWhichemail("2");
                        setWhichform("single_form2," + form_details.getApplicant_single_emp_type() + "," + form_details.getApplicant_single_id_type());
                        checkAvailableEmail();
                    }

                    if (form_details.getApplicant_single_email1().equals(form_details.getApplicant_single_email2())) {
                        error.put("pref2_error", "Preferred Email2 cannot be same as Preferred Email1");
                        form_details.setApplicant_single_email2(ESAPI.encoder().encodeForHTML(form_details.getApplicant_single_email2()));
                        flag = true;
                    }

                    String dob_err = valid.dobValidation(form_details.getApplicant_single_dob());
                    if (!dob_err.isEmpty() || !"".equals(dob_err)) {
                        error.put("dob_err", dob_err);
                        form_details.setApplicant_single_dob(ESAPI.encoder().encodeForHTML(form_details.getApplicant_single_dob()));
                        flag = true;
                    }
                    String dor_err = valid.dorValidation(form_details.getApplicant_single_dor(), form_details.getApplicant_single_dob());
                    if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                        error.put("dor_err", dor_err);
                        form_details.setApplicant_single_dor(ESAPI.encoder().encodeForHTML(form_details.getApplicant_single_dor()));
                        flag = true;
                    }

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

                            List<String> statedeplist = profileService.getCentralDept(form_details.getStateCode());

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
                    if (bo.equals("no bo")) {
                        if (ldap_employee) {

                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        } else {
                            if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                                if (hodDetails.get("bo").toString().equals("no bo")) {
                                    error.put("hod_email_error1", "Reporting officer should be govt employee");
                                }
                            }
                            boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                            System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                            if (check_mobile) {
                                error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                            }
                        }

                        underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                        form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString());

                        if (underSecDetails.get("bo").equals("no bo")) {
                            error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                        } else {

                        }

                        if (bo_undersec.equals("no bo")) {
                            error.put("under_sec_email_error", "Under secretary should be government employee");
                        } else {

                            boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                            boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                            boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                            boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                            System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                            boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                            System.out.println("inisde else in validation:::::::::::::");
                            if (under_sec_email_error == true) {
                                error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                            }
                            if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                                error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                            }
                            if (under_sec_desg_error == true) {
                                error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                            }
                            if (under_sec_name_error == true) {
                                error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                            }

                            if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                            }
                            if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                                error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                            }

                            if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                                if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                                } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                                } else {

                                }
                            } else {

                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                            }
                            if (under_sec_telephone_error == true) {
                                error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                            }
                        }
                    } else {

//                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
//                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");
//
//                    }
                        if (nic_employee && nicemployeeeditable.equals("no")) {
                            map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.get("hod_mobile") != null) {
                                form_details.setHod_mobile(map.get("hod_mobile"));
                            }

                            //map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.size() > 0) {
                                if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting officer details can not be changed.");

                                } else {
                                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                        error.put("hod_email_error", "Please change it in your profile and then come back");

                                    }
                                }
                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        }

                    }
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());

                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile no.");
                    }
                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile no.");
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
                                // ArrayList b = null;
                                //if (userdata.getAliases() != null && !userdata.getAliases().equals("") && !userdata.getAliases().toString().trim().equals("[]")) {
                                // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                                //  b = (ArrayList) userdata.getAliases();
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
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                                } else {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                    error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                                }
                                            }
                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
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
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
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
                    flag = true;
                    error.put("csrf_error", "Invalid Security Token !");
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 2: " + error);
                // end, code added by pr on 22ndjan18
                if (getAction_type().equals("submit")) {

                    System.out.println("inside submit::::::::::::in tab2");
                    if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {

                        // if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain")) {
                        String dup_exist = "";

                        if (session.get("dupemail") != null) {
                            dup_exist = session.get("dupemail").toString();
                        }
                        System.out.println("dup_exist:::::" + dup_exist);

                        if (form_details.getUnder_sec_email().trim() != null) {
                            profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                            profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                            profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                            profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());

                        } else {

                            profile_values.put("under_sec_email", "");
                            profile_values.put("under_sec_mobile", "");
                            profile_values.put("under_sec_name", "");
                            profile_values.put("under_sec_desig", "");
                            profile_values.put("under_sec_tel", "");
                        }
                        if (session.get("nodomain") != null) {
                            form_details.setAction(session.get("nodomain").toString());
                        } else {
                            form_details.setAction("");
                        }

                        if (session.get("update_without_oldmobile").equals("no")) {
                            String ref_num = emailservice.Single_tab2(form_details, profile_values);
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
                                    if (form_details.getUnder_sec_email().trim() != null) {
                                        profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                                        profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                                        profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                                        profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                                    } else {

                                        profile_values.put("under_sec_email", "");
                                        profile_values.put("under_sec_mobile", "");
                                        profile_values.put("under_sec_name", "");
                                        profile_values.put("under_sec_desig", "");
                                        profile_values.put("under_sec_tel", "");
                                    }
                                }
                            }
                            session.put("single_details", form_details);
                            session.put("ref_num", ref_num);
                        }
                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else if (getAction_type().equals("validate")) {
                    System.out.println("inside validate::::::::::::in tab2" + error);
                    if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain")) {
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
                    } else {

                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else if (getAction_type().equals("update")) // else if added by pr on 6thfeb18
                {

                    System.out.println("inside update::::::::::::in tab2");
                    if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {
                        String ref = session.get("ref").toString();
                        //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                        String admin_role = "NA";
                        if (session.get("admin_role") != null) {
                            admin_role = session.get("admin_role").toString();
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "before update :::::::::");
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
                        action_type = "";
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 2: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Bulk_tab1backup() {
        try {
            ArrayList validUsers = null;
            ArrayList notvalidUsers = null;
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata.getEmail()" + userdata.getEmail());
            System.out.println("userdata.isIsEmailValidated()" + userdata.isIsEmailValidated());
            if (userdata.isIsEmailValidated()) {

                error.clear();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details
                        = mapper.readValue(data, FormBean.class
                        );
                if (form_details.getReq_for().equals("mail") || form_details.getReq_for().equals("app") || form_details.getReq_for().equals("eoffice")) {

                } else {
                    error.put("req_for_err", "Choose Type of Mail ID");
                    form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getReq_for()));
                }
                if (form_details.getSingle_id_type().equals("id_name") || form_details.getSingle_id_type().equals("id_desig")) {

                } else {
                    error.put("single_id_type_err", "Choose Email address preference");
                    form_details.setSingle_id_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_id_type()));
                }

                if (form_details.getSingle_emp_type().equals("emp_regular") || form_details.getSingle_emp_type().equals("emp_contract") || form_details.getSingle_emp_type().equals("consultant")) {

                } else {
                    // flag = true;
                    error.put("bulk_emp_type_err", "Choose Employee Description");
                    form_details.setSingle_emp_type(ESAPI.encoder().encodeForHTML(form_details.getSingle_emp_type()));
                }

                String capcode = (String) session.get("captcha");
                if (!form_details.getImgtxt().equals(capcode)) {
                    error.put("cap_error", "Please enter Correct Captcha");
                    form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                }

                if (!cert.equals("true")) {
                    error.put("file_err", "Please upload the CSV file in correct format");
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERREO map for bulk controller tab 1: " + error);
                String SessionCSRFRandom = session.get("CSRFRandom").toString();

                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                }
                if (error.isEmpty()) {

                    if (session.get("uploaded_filename") != null && session.get("renamed_filepath") != null) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                        //Map ExcelValidate = (HashMap) emailservice.validateBulkCSVFile(renamed_filepath, "bulk", form_details.getSingle_id_type(), form_details.getSingle_emp_type(), userdata, department);
                        Map ExcelValidate = (HashMap) emailservice.validateBulkCSVFile(renamed_filepath, "bulk", form_details.getSingle_id_type(), form_details.getSingle_emp_type(), form_details.getUploaded_filename(), userdata, department);
                        if (ExcelValidate.containsKey("errorMessage")) {
                            error.put("file_err", ExcelValidate.get("errorMessage").toString());
                        } else {
                            ArrayList nodomain = (ArrayList) ExcelValidate.get("nodomain");
                            validUsers = (ArrayList) ExcelValidate.get("validUsers");
                            notvalidUsers = (ArrayList) ExcelValidate.get("notvalidUsers");
                            ArrayList errorList = (ArrayList) ExcelValidate.get("errorInString");
                            ArrayList dup_error = (ArrayList) ExcelValidate.get("dup_error");
                            String dupexist = "";
//                            session.put("dupemail", "");
//                            if (dup_error.contains("dup_exist")) {
//
//                                session.put("dupemail", "yes");
//
//                            }
                            if (nodomain.contains("yes")) {
                                session.put("nodomain", "yes");
                            }
                            System.out.println("dup_error:::" + dup_error);
                            HashMap hm = null;
                            ArrayList errorlist = new ArrayList();

                            for (Iterator it = errorList.iterator(); it.hasNext();) {
                                hm = new LinkedHashMap();
                                String e = (String) it.next();
                                hm.put("ERROR LIST ", e);
                                errorlist.add(hm);
                            }

                            String refFilename = renamed_filepath.substring(0, renamed_filepath.indexOf("."));

                            ExcelCreator excelCreator = new ExcelCreator();
                            HashMap a = new HashMap();
                            a.put("Details", "NO DATA FOUND");

                            Boolean err = false;
                            if (validUsers.size() > 0) {
                                excelCreator.createCSV(validUsers, refFilename + "-valid.csv", "Users which can be created");
                            } else {
                                err = true;
                                validUsers.add(a);
                                excelCreator.createCSV(validUsers, refFilename + "-valid.csv", "Users which can be created");
                            }
                            if (notvalidUsers.size() > 0) {
                                excelCreator.createCSV(notvalidUsers, refFilename + "-notvalid.csv", "Users which can not be created");
                            } else {
                                notvalidUsers.add(a);
                                excelCreator.createCSV(notvalidUsers, refFilename + "-notvalid.csv", "Users which can not be created");
                            }
                            if (errorlist.size() > 0) {
                                excelCreator.createCSV(errorlist, refFilename + "-error.csv", "Error list");
                            } else {
                                errorlist.add(a);
                                excelCreator.createCSV(errorlist, refFilename + "-error.csv", "Error list");
                            }
                            System.out.println("form details employee desc::" + form_details.getSingle_emp_type());
                            session.put("filetodownload", refFilename);
                            session.put("bulk_details", form_details);
                            int campaignId = (int) ExcelValidate.get("campaign_id");
                            session.put("campaign_id", campaignId);
                            iCampaignId = campaignId;
                            bulkData = emailservice.fetchBulkUploadData(campaignId);
                            profile_values = (HashMap) session.get("profile-values");
                            if (err) {
                                //error.put("file_err", "No users to create, check Error file");
                                session.put("error_file", "No users to create, check Error file");
                            } else {
                                if (session.get("error_file") != null) {
                                    session.remove("error_file");
                                }
                                error.put("file_err", "");
                            }
                        }
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                error.put("email_info_error", "You may register only for the following services:Email Service,VPN Service,Security Audit Service,e-Sampark Service,Cloud Service,Domain Registration Service,Firewall Service,Reservation for video conferencing Service,Web Application Firewall services.To register for other services, please log in with your government email service(NIC) email address");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for bulk controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Bulk_tab1() {
        try {
            ArrayList validUsers = null;
            ArrayList notvalidUsers = null;
            error.clear();
            bulkData.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata.getEmail()" + userdata.getEmail());
            System.out.println("userdata.isIsEmailValidated()" + userdata.isIsEmailValidated());
            if (userdata.isIsEmailValidated()) {
                String capcode = (String) session.get("captcha");
                if (!form_details.getImgtxt().equals(capcode)) {
                    error.put("cap_error", "Please enter Correct Captcha");
                    form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                }

                if (!cert.equals("true")) {
                    error.put("file_err", "Please upload the CSV file in correct format");
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERREO map for bulk controller tab 1: " + error);
                String SessionCSRFRandom = session.get("CSRFRandom").toString();

                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                }
                String uploaded_filename = "";
                String renamed_filepath = "";
                String refFilename = "";
                if (error.isEmpty()) {

                    if (session.get("uploaded_filename") != null && session.get("renamed_filepath") != null && !session.get("uploaded_filename").equals("") && !session.get("renamed_filepath").equals("")) {
                        uploaded_filename = session.get("uploaded_filename").toString();
                        renamed_filepath = session.get("renamed_filepath").toString();
                        refFilename = renamed_filepath.substring(0, renamed_filepath.length() - 4);

                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);

                        Map ExcelValidate = null;
                        ExcelValidate = (HashMap) validateBulkCSVFile(renamed_filepath, "bulk", form_details.getSingle_id_type(), form_details.getSingle_emp_type(), form_details.getUploaded_filename(), userdata, department);
                        if (ExcelValidate.containsKey("errorMessage")) {
                            error.put("file_err", ExcelValidate.get("errorMessage").toString());
                        } else {
                            ArrayList nodomain = (ArrayList) ExcelValidate.get("nodomain");
                            validUsers = (ArrayList) ExcelValidate.get("validUsers");
                            notvalidUsers = (ArrayList) ExcelValidate.get("notvalidUsers");
                            ArrayList<String> errorList = (ArrayList<String>) ExcelValidate.get("errorInString");
                            ArrayList<String> dup_error = (ArrayList<String>) ExcelValidate.get("dup_error");

                            System.out.println("dup_error:::" + dup_error);
                            HashMap hm = null;
                            ArrayList errorlist = new ArrayList();

                            for (Iterator it = errorList.iterator(); it.hasNext();) {
                                hm = new LinkedHashMap();
                                String e = (String) it.next();
                                hm.put("ERROR LIST ", e);
                                errorlist.add(hm);
                            }
                            //String refFilename = renamed_filepath.substring(0, renamed_filepath.indexOf("."));

                            ExcelCreator excelCreator = new ExcelCreator();
                            HashMap a = new HashMap();
                            a.put("Details", "NO DATA FOUND");
                            Boolean err = false;
                            if (validUsers.size() > 0) {
                                excelCreator.createCSV(validUsers, refFilename + "-valid.csv", "Users which can be created");
                            } else {
                                err = true;
                                validUsers.add(a);
                                excelCreator.createCSV(validUsers, refFilename + "-valid.csv", "Users which can be created");
                            }
                            if (notvalidUsers.size() > 0) {
                                excelCreator.createCSV(notvalidUsers, refFilename + "-notvalid.csv", "Users which can not be created");
                            } else {
                                notvalidUsers.add(a);
                                excelCreator.createCSV(notvalidUsers, refFilename + "-notvalid.csv", "Users which can not be created");
                            }
                            if (errorlist.size() > 0) {
                                excelCreator.createCSV(errorlist, refFilename + "-error.csv", "Error list");
                            } else {
                                errorlist.add(a);
                                excelCreator.createCSV(errorlist, refFilename + "-error.csv", "Error list");
                            }
                            System.out.println("form details employee desc::" + form_details.getSingle_emp_type());
                            session.put("filetodownload", refFilename);
                            session.put("bulk_details", form_details);
                            int campaignId = (int) session.get("campaign_id");
                            session.put("campaign_id", campaignId);
                            iCampaignId = campaignId;
                            bulkData = emailservice.fetchBulkUploadData(campaignId);
                            profile_values = (HashMap) session.get("profile-values");
                            if (err) {
                                //error.put("file_err", "No users to create, check Error file");
                                session.put("error_file", "No users to create, check Error file");
                            } else {
                                if (session.get("error_file") != null) {
                                    session.remove("error_file");
                                }
                                error.put("file_err", "");
                            }

                        }
                    }

                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                error.put("email_info_error", "You may register only for the following services:Email Service(Single Subscription),VPN Service,Security Audit Service,e-Sampark Service,Cloud Service,Domain Registration Service,Firewall Service,Reservation for video conferencing Service,Web Application Firewall services.To register for other services, please log in with your government email service(NIC) email address");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for bulk controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Bulk_tab2() { // not use

        try {
            error.clear();
            if (session.get("error_file") != null) {
                error.put("error_file", session.get("error_file").toString());
            }
            if (error.isEmpty()) {
                form_details = (FormBean) session.get("bulk_details");
                String renamed_uploaded_filename = form_details.getRenamed_filepath();
                String str = renamed_uploaded_filename;
                String onlyName = str.replace(".csv", "");
                onlyName = onlyName.trim();
                session.put("valid_filepath", onlyName + "-valid.csv");
                session.put("error_filepath", onlyName + "-error.csv");
                session.put("notvalid_filepath", onlyName + "-notvalid.csv");
                System.out.println(" renamed_uploaded_filename value is " + renamed_uploaded_filename);
                profile_values = (HashMap) session.get("profile-values");
                System.out.println("profile_values:::::::::" + profile_values);
                if (session.get("update_without_oldmobile").equals("no")) {
                    emailservice.Bulk_tab2(form_details);
                    hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                    String bo = hodDetails.get("bo").toString();
                    profile_values.put("bo_check", bo);
                }
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for bulk controller tab 2: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Email_bulk_tab2() {
        System.out.println("inside email bulk tab222222222222222222");
        int campaignId = (int) session.get("campaign_id");
        iCampaignId = campaignId;
        form_details = (FormBean) session.get("bulk_details");
        if (emailservice.fetchCountOfSuccessfulRecords(campaignId) > 0) {
            try {
                error.clear();
                if (error.isEmpty()) {
                    bulkData = emailservice.fetchSuccessBulkData(campaignId);
                    profile_values = (HashMap) session.get("profile-values");
                } else {
                    data = "";
                }
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for bulk controller tab 2: " + e.getMessage());
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }
        }
        return SUCCESS;
    }

    public String Bulk_tab3() {
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        }
        if (campaignId == -1) {
            campaignId = iCampaignId;
        }
        if (campaignId != -1) {
            error.clear();
            try {
                db = new Database();
                UserData userdata = (UserData) session.get("uservalues");
                boolean ldap_employee = userdata.isIsEmailValidated();
                boolean nic_employee = userdata.isIsNICEmployee();
                HodData hoddata = (HodData) userdata.getHodData();
                UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();
                String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);
                String uploaded_filename = session.get("uploaded_filename").toString();
                String renamed_filepath = session.get("renamed_filepath").toString();
                form_details.setUploaded_filename(uploaded_filename);
                form_details.setRenamed_filepath(renamed_filepath);
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                String bo = hodDetails.get("bo").toString();
                underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                String bo_undersec = underSecDetails.get("bo").toString();
                profile_values = (HashMap) session.get("profile-values");
                ldap = new Ldap();
                userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
                if (ldap_employee) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                } else {
                    if (userdata.getHodData().isGovEmployee()) {
                        profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                        form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
                    }
                }
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

                    if (bo.equals("no bo")) {

                        if (ldap_employee) {

                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        } else {
                            if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                                if (hodDetails.get("bo").toString().equals("no bo")) {
                                    error.put("hod_email_error1", "Reporting officer should be govt employee");
                                }

                            }

                            boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                            System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                            if (check_mobile) {
                                error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                            }

                        }

                        underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                        if (underSecDetails.get("bo").equals("no bo")) {
                            error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                        } else {

                        }

                        if (bo_undersec.equals("no bo")) {
                            error.put("under_sec_email_error", "Under secretary should be government employee");
                        } else {

                            form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString().trim());
                            boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                            boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                            boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                            boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                            System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                            boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                            System.out.println("inisde else in validation:::::::::::::");
                            if (under_sec_email_error == true) {
                                error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                            }
                            if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                                error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                            }
                            if (under_sec_desg_error == true) {
                                error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                            }
                            if (under_sec_name_error == true) {
                                error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                            }

                            if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                            }
                            if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                                error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                            }

                            if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                                if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                                } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                                } else {

                                }
                            } else {

                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                            }
                            if (under_sec_telephone_error == true) {
                                error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                            }
                        }
                    } else {

                        if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                            error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                        }
                        System.out.println("nic_employee in bulk tab 2:::::::::::" + nic_employee);
                        if (nic_employee && nicemployeeeditable.equals("no")) {

                            map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.size() > 0) {
                                if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting officer details can not be changed.");

                                } else {
                                    System.out.println("inside nic employee");
                                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                        error.put("hod_email_error", "Please change it in your profile and then come back");

                                    }
                                }
                            } else {
                                System.out.println("inside nic employee");
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        }

                    }
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.roTelValidation(form_details.getHod_tel());
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile");
                    }
                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile");
                    }

                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile no.");
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
                            Set s = (Set) userdata.getAliases();

                            System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);

                            ArrayList<String> newAr = new ArrayList<String>();

                            newAr.addAll(s);
                            ArrayList email_aliases = null;
                            email_aliases = newAr;
                            if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {

                                // Map a = (HashMap) session.get("profile-values");
//                        ArrayList b = null;
//                        if (userdata.getAliases() != null && !userdata.getAliases().equals("") && !userdata.getAliases().toString().trim().equals("[]")) {
//                            b = (ArrayList) userdata.getAliases();
//                            // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                                //b = (ArrayList) a.get("mailequi");
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

                                }
                            }
                        }
                    }
                }
                String SessionCSRFRandom = session.get("CSRFRandom").toString();

                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for bulk controller tab 3: " + error);

                if (getAction_type().equals("submit")) {
                    if (!error.isEmpty()) {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                    } else {

                        String filename = session.get("filetodownload").toString() + "-valid.csv";
                        campaignId = (int) session.get("campaign_id");
                        profile_values = (HashMap) session.get("profile-values");

                        String dup_exist = "";

                        if (session.get("dupemail") != null) {
                            dup_exist = session.get("dupemail").toString();
                        }

                        if (form_details.getUnder_sec_email().trim() != null) {
                            profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                            // profile_values.put("under_sec_mobile", form_details.getUnder_sec_mobile().trim());
                            profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                            profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                            profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());
                        } else {

                            profile_values.put("under_sec_email", "");
                            profile_values.put("under_sec_mobile", "");
                            profile_values.put("under_sec_name", "");
                            profile_values.put("under_sec_desig", "");
                            profile_values.put("under_sec_tel", "");
                        }
                        if (session.get("update_without_oldmobile").equals("no")) {
                            String ref_num = emailservice.Bulk_tab3(form_details, filename, profile_values, campaignId);
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
                                    //   profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                    profile_values.put("hod_email", form_details.getHod_email().trim());
                                    profile_values.put("hod_tel", form_details.getHod_tel().trim());

                                }
                            }
                            session.put("bulk_details", form_details);
                            session.put("ref_num", ref_num);

//                            UserTrack userTrack = new UserTrack();
//                            userTrack.docUploader_bulk_tab3(ref_num);
                        }
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
                e.printStackTrace();
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }
        }
        return SUCCESS;
    }

    public String Nkn_single_tab1() {
        try {
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata.getEmail()" + userdata.getEmail());
            System.out.println("userdata.isIsEmailValidated()" + userdata.isIsEmailValidated());
            if (!userdata.isIsEmailValidated()) {
                System.out.println("if not validated");
                returnString = signup.checkrequest(userdata.getEmail());
                System.out.println("returnString" + returnString);
            }
            if (!returnString.equals("pending") && !returnString.equals("completed")) {
                error.clear();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details
                        = mapper.readValue(data, FormBean.class
                        );

                if (session.get("profile-values") != null) {
                    profile_values = (HashMap) session.get("profile-values");
                    employment = profile_values.get("user_employment").toString();
                    session.put("prEmployment", employment);
                    if (profile_values.get("min") != null) {
                        ministry = profile_values.get("min").toString();
                        session.put("prMinistry", ministry);
                    }
                    if (profile_values.get("dept") != null) {
                        department = profile_values.get("dept").toString();
                        session.put("prDepartment", department);
                    }
                    if (profile_values.get("stateCode") != null) {
                        state = profile_values.get("stateCode").toString();
                        session.put("prState", state);
                    }
                    if (profile_values.get("dept") != null) {
                        stateDepartment = profile_values.get("dept").toString();
                        session.put("prStateDepartment", stateDepartment);
                    }
                    if (profile_values.get("Org") != null) {
                        organization = profile_values.get("Org").toString();
                        session.put("prOrganization", organization);
                    }
                }
                boolean pref1_error = valid.EmailValidation(form_details.getSingle_email1());
                boolean pref2_error = valid.EmailValidation(form_details.getSingle_email2());
                System.out.println("form_details.getInst_name()" + form_details.getInst_name());
                boolean inst_name_error = valid.nameValidation(form_details.getInst_name());
                System.out.println("inst_name_error" + inst_name_error);
                boolean inst_id_error = valid.institueIdValidation(form_details.getInst_id());
                boolean project_name_error = valid.nameValidation(form_details.getNkn_project());
                Boolean flag = false;
                String dup_email = "";
                if (session.get("dupemail") != null) {
                    dup_email = session.get("dupemail").toString();
                } else {
                    dup_email = "";
                }

                if (dup_email.equals("yes")) {
                    flag = true;
                    error.put("dup_err", "Already we have e-Mail address registered against this mobile number");

                } else {
                    if (inst_name_error == true) {
                        System.out.println("inside inst_name_error true");
                        error.put("inst_name_error", "Enter Institute Name [Only characters (limit 1-50),whitespace,comma(,) allowed]");
                        form_details.setInst_name(ESAPI.encoder().encodeForHTML(form_details.getInst_name()));
                        flag = true;
                    }
                    if (inst_id_error == true) {
                        error.put("inst_id_error", "Enter Institute ID [Alphanumeric (limit 1-50),dot(.),comma(,),hypen(-) allowed]");
                        form_details.setInst_id(ESAPI.encoder().encodeForHTML(form_details.getInst_id()));
                        flag = true;
                    }
                    if (project_name_error == true) {
                        error.put("project_name_error", "Enter Name of Project NKN [Only characters,whitespace,comma(,)allowed]");
                        form_details.setNkn_project(ESAPI.encoder().encodeForHTML(form_details.getNkn_project()));
                        flag = true;
                    }
                    if (pref1_error == true) {
                        error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email1());
                        setWhichemail("1");
                        setWhichform("nkn_single_form1");
                        checkAvailableEmail();
                    }
                    if (pref2_error == true) {
                        error.put("pref2_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email2());
                        setWhichemail("2");
                        setWhichform("nkn_single_form1");
                        checkAvailableEmail();
                    }

                    if (form_details.getSingle_email1().equals(form_details.getSingle_email2())) {
                        error.put("pref2_error", "Preferred Email2 cannot be same as Preferred Email1");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    }

                    String dob_err = valid.dobValidation(form_details.getSingle_dob());
                    if (!dob_err.isEmpty() || !"".equals(dob_err)) {
                        error.put("dob_err", dob_err);
                        form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
                        flag = true;
                    }
                    String dor_err = valid.dorValidation(form_details.getSingle_dor(), form_details.getSingle_dob());
                    if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                        error.put("dor_err", dor_err);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                        flag = true;
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
                        flag = true;
                    }
                }
                // end, code added by pr on 22ndjan18
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for nkn controller tab 1: " + error);
                //  if (error.isEmpty()) {
                if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {

                    if (session.get("update_without_oldmobile").equals("no")) {
                        form_details.setRequest_type(request_type);

                        emailservice.Nkn_single_tab1(form_details);

                        profile_values = (HashMap) session.get("profile-values");

                        hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                        String bo = hodDetails.get("bo").toString();
                        profile_values.put("bo_check", bo);
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }

            } else {
                error.put("email_info_error", "Your request is already pending/completed so you can not fill any more request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for nkn controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;

    }

    public String Nkn_bulk_tab1() {
        try {

            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata.getEmail()" + userdata.getEmail());
            System.out.println("userdata.isIsEmailValidated()" + userdata.isIsEmailValidated());
            if (userdata.isIsEmailValidated()) {
                error.clear();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details
                        = mapper.readValue(data, FormBean.class
                        );
                form_details.setRequest_type(request_type);
                String capcode = (String) session.get("captcha");
                boolean inst_name_error = valid.nameValidation(form_details.getInst_name());
                boolean inst_id_error = valid.institueIdValidation(form_details.getInst_id());
                boolean project_name_error = valid.nameValidation(form_details.getNkn_project());
                if (inst_name_error == true) {
                    error.put("inst_name_error", "Enter Institute Name [Only characters(limit 1-50) ,whitespace,comma(,) allowed] ");
                    form_details.setInst_name(ESAPI.encoder().encodeForHTML(form_details.getInst_name()));
                }
                if (inst_id_error == true) {
                    error.put("inst_id_error", "Enter Institute ID [Alphanumeric (limit 1-50),dot(.),comma(,),hypen(-) allowed]");
                    form_details.setInst_id(ESAPI.encoder().encodeForHTML(form_details.getInst_id()));
                }
                if (project_name_error == true) {
                    error.put("project_name_error", "Enter Name of Project NKN [Only characters (limit 1-50),whitespace,comma(,)allowed]");
                    form_details.setNkn_project(ESAPI.encoder().encodeForHTML(form_details.getNkn_project()));
                }
                if (!form_details.getImgtxt().equals(capcode)) {
                    error.put("cap_error", "Please enter Correct Captcha");
                    form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                }
                if (!cert.equals("true")) {
                    System.out.println("inside true::::::::::::::");
                    error.put("file_err", "Please upload the CSV file");
                }

                // start, code added by pr on 23rdjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();

                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                }

                // end, code added by pr on 23rdjan18
                if (error.isEmpty()) {

                    if (session.get("uploaded_filename") != null && session.get("renamed_filepath") != null) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                        // validate excel file and store in buffer files

                        Map ExcelValidate = (HashMap) emailservice.validateBulkCSVFileNKN(renamed_filepath, "nkn", "", "", userdata, department);
                        //Map ExcelValidate = (HashMap) emailservice.validateBulkCSVFile(renamed_filepath, "nkn", "", "", form_details.getUploaded_filename(), userdata, department);
                        if (ExcelValidate.containsKey("errorMessage")) {
                            error.put("file_err", ExcelValidate.get("errorMessage").toString());
                        } else {
                            ArrayList validUsers = (ArrayList) ExcelValidate.get("validUsers");
                            ArrayList notvalidUsers = (ArrayList) ExcelValidate.get("notvalidUsers");
                            ArrayList errorList = (ArrayList) ExcelValidate.get("errorList");
                            HashMap hm = null;
                            ArrayList errorlist = new ArrayList();
                            for (Iterator it = errorList.iterator(); it.hasNext();) {
                                hm = new LinkedHashMap();
                                String e = (String) it.next();
                                hm.put("ERROR LIST ", e);
                                errorlist.add(hm);
                            }

                            //String refFilename = renamed_filepath.substring(0, renamed_filepath.indexOf("."));
                            String refFilename = renamed_filepath.substring(0, renamed_filepath.length() - 4);
                            ExcelCreator excelCreator = new ExcelCreator();
                            HashMap a = new HashMap();
                            a.put("Details", "NO DATA FOUND");

                            Boolean err = false;
                            if (validUsers.size() > 0) {
                                excelCreator.createCSV(validUsers, refFilename + "-valid.csv", "Users which can be created");
                            } else {
                                validUsers.add(a);
                                err = true;
                                excelCreator.createCSV(validUsers, refFilename + "-valid.csv", "Users which can be created");
                            }
                            if (notvalidUsers.size() > 0) {
                                excelCreator.createCSV(notvalidUsers, refFilename + "-notvalid.csv", "Users which can not be created");
                            } else {
                                notvalidUsers.add(a);
                                excelCreator.createCSV(notvalidUsers, refFilename + "-notvalid.csv", "Users which can not be created");
                            }
                            if (errorlist.size() > 0) {
                                excelCreator.createCSV(errorlist, refFilename + "-error.csv", "Error list");
                            } else {
                                errorlist.add(a);
                                excelCreator.createCSV(errorlist, refFilename + "-error.csv", "Error list");
                            }

                            session.put("filetodownload", refFilename);
                            session.put("nkn_details", form_details);
                            if (err) {
                                session.put("error_file", "No users to create, check Error file");
                            } else {
                                if (session.get("error_file") != null) {
                                    session.remove("error_file");
                                }
                                error.put("file_err", "");
                            }

                        }
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                error.put("email_info_error", "You may register only for the following services:Email Service,VPN Service,Security Audit Service,e-Sampark Service,Cloud Service,Domain Registration Service,Firewall Service,Reservation for video conferencing Service,Web Application Firewall services.To register for other services, please log in with your government email service(NIC) email address");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for nkn bulk controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Nkn_bulk_tab2() {

        try {
            error.clear();
            if (session.get("error_file") != null) {
                error.put("error_file", session.get("error_file").toString());
            }
            if (error.isEmpty()) {
                form_details = (FormBean) session.get("nkn_details");
                System.out.println("form_details:::" + form_details.getInst_name());
                emailservice.Nkn_bulk_tab1(form_details);
                profile_values = (HashMap) session.get("profile-values");
                hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                String bo = hodDetails.get("bo").toString();
                profile_values.put("bo_check", bo);
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Nkn_tab2() {
        try {
            db = new Database();
            ldap = new Ldap();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );
            form_details.setRequest_type(request_type);

            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            String bo = hodDetails.get("bo").toString();
            underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
            String bo_undersec = underSecDetails.get("bo").toString();
            userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

            } else {
                if (userdata.getHodData().isGovEmployee()) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                }
            }
            if (request_type == null && session.get("request_type") != null) {
                request_type = session.get("request_type").toString();
            }
            if (request_type.equals("nkn_bulk")) {
                boolean flag = false;
                if (session.get("uploaded_filename") != null && session.get("renamed_filepath") != null) {
                    String uploaded_filename = session.get("uploaded_filename").toString();
                    String renamed_filepath = session.get("renamed_filepath").toString();
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                }

                boolean inst_name_error = valid.nameValidation(form_details.getInst_name());
                boolean inst_id_error = valid.institueIdValidation(form_details.getInst_id());
                boolean project_name_error = valid.nameValidation(form_details.getNkn_project());
                if (inst_name_error == true) {
                    error.put("inst_name_error", "Enter Institute Name [Only characters (limit 1-50),whitespace,comma(,) allowed]");
                    form_details.setInst_name(ESAPI.encoder().encodeForHTML(form_details.getInst_name()));
                    flag = true;
                }
                if (inst_id_error == true) {
                    error.put("inst_id_error", "Enter Institute ID [Alphanumeric (limit 1-50),dot(.),comma(,),hypen(-) allowed]");
                    form_details.setInst_id(ESAPI.encoder().encodeForHTML(form_details.getInst_id()));
                    flag = true;
                }
                if (project_name_error == true) {
                    error.put("project_name_error", "Enter Name of Project NKN [Only characters (limit 1-50),whitespace,comma(,)allowed]");
                    form_details.setNkn_project(ESAPI.encoder().encodeForHTML(form_details.getNkn_project()));
                    flag = true;
                }

                // start, code added by pr on 22ndjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();

                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                    flag = true;
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
                profile_values = (HashMap) session.get("profile-values");

                if (form_details.getModule() == null) {

                    if (bo.equals("no bo")) {

                        if (ldap_employee) {

                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        } else {
                            if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                                if (hodDetails.get("bo").toString().equals("no bo")) {
                                    error.put("hod_email_error1", "Reporting officer should be govt employee");
                                }

                            }

                            boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                            System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                            if (check_mobile) {
                                error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                            }

                        }

                        underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                        if (underSecDetails.get("bo").equals("no bo")) {
                            error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                        } else {

                        }

                        if (bo_undersec.equals("no bo")) {
                            error.put("under_sec_email_error", "Under secretary should be government employee");
                        } else {
                            form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString().trim());
                            boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                            boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                            boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                            boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                            System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                            boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                            System.out.println("inisde else in validation:::::::::::::");
                            if (under_sec_email_error == true) {
                                error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                            }
                            if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                                error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                            }
                            if (under_sec_desg_error == true) {
                                error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                            }
                            if (under_sec_name_error == true) {
                                error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                            }

                            if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                            }
                            if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                                error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                            }

                            if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                                if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                                } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                                } else {

                                }
                            } else {

                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                            }
                            if (under_sec_telephone_error == true) {
                                error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                            }
                        }
                    } else {

                        if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                            error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                        }
                        if (nic_employee && nicemployeeeditable.equals("no")) {
                            map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.size() > 0) {
                                if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting officer details can not be changed.");

                                } else {
                                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                        error.put("hod_email_error", "Please change it in your profile and then come back");

                                    }
                                }
                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }

                            }
                        }

                    }

                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }

                    if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile");
                    }
                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile");
                    }
                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile no.");
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
                        if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                            error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                        }
                        if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                            error.put("hod_email_error", "Please change it in your profile and then come back");
                        }
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
//                            ArrayList b = null;
//                            if (userdata.getAliases() != null && !userdata.getAliases().equals("") && !userdata.getAliases().toString().trim().equals("[]")) {
                            Set s = (Set) userdata.getAliases();

                            System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);

                            ArrayList<String> newAr = new ArrayList<String>();

                            newAr.addAll(s);
                            ArrayList email_aliases = null;
                            email_aliases = newAr;
                            if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {

                                // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
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

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for nkn controller tab 2: " + error);

                if (getAction_type().equals("submit")) {
                    if (error.isEmpty()) {
                        session.put("nkn_details", form_details);
                        String filename = session.get("filetodownload").toString() + "-valid.csv";
                        profile_values = (HashMap) session.get("profile-values");
                        String dup_exist = "";

                        if (session.get("dupemail") != null) {
                            dup_exist = session.get("dupemail").toString();
                        }
                        if (form_details.getUnder_sec_email().trim() != null) {
                            profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                            //profile_values.put("under_sec_mobile", form_details.getUnder_sec_mobile().trim());
                            profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                            profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                            profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());
                        } else {

                            profile_values.put("under_sec_email", "");
                            profile_values.put("under_sec_mobile", "");
                            profile_values.put("under_sec_name", "");
                            profile_values.put("under_sec_desig", "");
                            profile_values.put("under_sec_tel", "");
                        }
                        if (session.get("update_without_oldmobile").equals("no")) {
                            String ref_num = emailservice.Nkn_tab2(form_details, filename, profile_values);
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
                                    // profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                    profile_values.put("hod_email", form_details.getHod_email().trim());
                                    profile_values.put("hod_tel", form_details.getHod_tel().trim());

                                }
                            }
                            session.put("ref_num", ref_num);
                        }
                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
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
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else if (getAction_type().equals("update")) // else if added by pr on 1stfeb18
                {
                    //if (error.isEmpty()) {
                    if (error.isEmpty()) {
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
                        action_type = "";
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }

            } else if (request_type.equals("nkn_single")) {
                boolean flag = false;
                boolean pref1_error = valid.EmailValidation(form_details.getSingle_email1());
                boolean pref2_error = valid.EmailValidation(form_details.getSingle_email2());
                boolean inst_name_error = valid.nameValidation(form_details.getInst_name());
                boolean inst_id_error = valid.institueIdValidation(form_details.getInst_id());
                boolean project_name_error = valid.nameValidation(form_details.getNkn_project());

                session.put("prEmployment", form_details.getUser_employment());
                session.put("prMinistry", form_details.getMin());
                session.put("prDepartment", form_details.getDept());
                session.put("prState", form_details.getStateCode());
                session.put("prStateDepartment", form_details.getDept());
                session.put("prOrganization", form_details.getOrg());
                if (inst_name_error == true) {
                    error.put("inst_name_error", "Enter Institute Name [Only characters (limit 1-50),whitespace,comma(,) allowed]");
                    form_details.setInst_name(ESAPI.encoder().encodeForHTML(form_details.getInst_name()));
                    flag = true;
                }
                if (inst_id_error == true) {
                    error.put("inst_id_error", "Enter Institute ID [Alphanumeric (limit 1-50),dot(.),comma(,),hypen(-) allowed]");
                    form_details.setInst_id(ESAPI.encoder().encodeForHTML(form_details.getInst_id()));
                    flag = true;
                }
                if (project_name_error == true) {
                    error.put("project_name_error", "Enter Name of Project NKN [Only characters,whitespace,comma(,)allowed]");
                    form_details.setNkn_project(ESAPI.encoder().encodeForHTML(form_details.getNkn_project()));
                    flag = true;
                }
                if (pref1_error == true) {
                    error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                    form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                    flag = true;
                } else {
                    setData(form_details.getSingle_email1());
                    setWhichemail("1");
                    setWhichform("nkn_form2");
                    checkAvailableEmail();
                }
                if (pref2_error == true) {
                    error.put("pref2_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                    form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                    flag = true;
                } else {
                    setData(form_details.getSingle_email2());
                    setWhichemail("2");
                    setWhichform("nkn_form2");
                    checkAvailableEmail();
                }
                String dob_err = valid.dobValidation(form_details.getSingle_dob());
                if (!dob_err.isEmpty() || !"".equals(dob_err)) {
                    error.put("dob_err", dob_err);
                    form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
                    flag = true;
                }
                String dor_err = valid.dorValidation(form_details.getSingle_dor(), form_details.getSingle_dob());
                if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                    error.put("dor_err", dor_err);
                    form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                    flag = true;
                }

                // start, code added by pr on 22ndjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();

                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                    flag = true;
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

                if (form_details.getModule() == null) {

                    if (bo.equals("no bo")) {

                        if (ldap_employee) {

                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        } else {
                            if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                                if (hodDetails.get("bo").toString().equals("no bo")) {
                                    error.put("hod_email_error1", "Reporting officer should be govt employee");
                                }

                            }

                            boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                            System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                            if (check_mobile) {
                                error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                            }

                        }

                        underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                        if (underSecDetails.get("bo").equals("no bo")) {
                            error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                        } else {

                        }

                        if (bo_undersec.equals("no bo")) {
                            error.put("under_sec_email_error", "Under secretary should be government employee");
                        } else {
                            profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString().trim());
                            form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString().trim());
                            boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                            boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                            boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                            boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                            System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                            boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                            System.out.println("inisde else in validation:::::::::::::");
                            if (under_sec_email_error == true) {
                                error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                            }
                            if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                                error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                            }
                            if (under_sec_desg_error == true) {
                                error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                            }
                            if (under_sec_name_error == true) {
                                error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                            }

                            if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                            }
                            if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                                error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                            }

                            if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                                if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                                } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                                } else {

                                }
                            } else {

                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                            }
                            if (under_sec_telephone_error == true) {
                                error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                            }
                        }
                    } else {

                        if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                            error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                        }
                        if (nic_employee && nicemployeeeditable.equals("no")) {
                            map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.size() > 0) {
                                if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting officer details can not be changed.");

                                } else {
                                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                        error.put("hod_email_error", "Please change it in your profile and then come back");
                                    }
                                }
                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        }

                    }
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile");
                    }
                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile");
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
                            //ArrayList b = null;
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

                if (getAction_type().equals("submit")) {
                    if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {
                        session.put("nkn_details", form_details);
                        profile_values = (HashMap) session.get("profile-values");
                        String dup_exist = "";

                        if (session.get("dupemail") != null) {
                            dup_exist = session.get("dupemail").toString();
                        }

                        System.out.println("profile values in nkn single tab2 before call funtion" + session.get("profile-values"));
                        System.out.println("form_details in nkn single tab2 before call funtion" + form_details);
                        //System.out.println("profile_values before nkn2"+profile_values);

                        if (form_details.getUnder_sec_email().trim() != null) {
                            profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                            //profile_values.put("under_sec_mobile", form_details.getUnder_sec_mobile().trim());
                            profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                            profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                            profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());
                        } else {

                            profile_values.put("under_sec_email", "");
                            profile_values.put("under_sec_mobile", "");
                            profile_values.put("under_sec_name", "");
                            profile_values.put("under_sec_desig", "");
                            profile_values.put("under_sec_tel", "");
                        }

                        if (session.get("update_without_oldmobile").equals("no")) {
                            String ref_num = emailservice.Nkn_tab2(form_details, "", profile_values);
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
                                    // profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                    profile_values.put("hod_email", form_details.getHod_email().trim());
                                    profile_values.put("hod_tel", form_details.getHod_tel().trim());

                                }
                            }
                            session.put("ref_num", ref_num);
                        }
                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else if (getAction_type().equals("validate")) {
                    if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {

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

                    } else {

                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else if (getAction_type().equals("update")) // else if added by pr on 1stfeb18
                {
                    //if (error.isEmpty()) {
                    if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {
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
                        action_type = "";
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Gem_tab1() {
        try {
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata.getEmail()" + userdata.getEmail());
            System.out.println("userdata.isIsEmailValidated()" + userdata.isIsEmailValidated());
            if (!userdata.isIsEmailValidated()) {
                System.out.println("if not validated");
                returnString = signup.checkrequest(userdata.getEmail());
                System.out.println("returnString" + returnString);
            }
            if (!returnString.equals("pending") && !returnString.equals("completed")) {

                error.clear();
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details
                        = mapper.readValue(data, FormBean.class
                        );

                boolean email1_pse_error = valid.EmailValidation(form_details.getSingle_email1());
                boolean email2_pse_error = valid.EmailValidation(form_details.getSingle_email2());
                boolean traffic_pse_error = valid.domesticTrafficvalidation(form_details.getDomestic_traf());

                Boolean flag = false;
                String dup_email = "";
                if (session.get("dupemail") != null) {
                    dup_email = session.get("dupemail").toString();
                } else {
                    dup_email = "";
                }

                if (dup_email.equals("yes")) {
                    flag = true;
                    error.put("dup_err", "Already we have e-Mail address registered against this mobile number");

                } else {
                    if (form_details.getPse().equals("")) {
                        error.put("pse_error", "Please select Organization Category");
                        form_details.setPse(ESAPI.encoder().encodeForHTML(form_details.getPse()));
                        flag = true;
                    } else if (form_details.getPse().equalsIgnoreCase("central_pse")) {
                        boolean central_pse_error = valid.controllingministryValidation(form_details.getPse_ministry());
                        if (form_details.getPse_ministry().equals("")) {
                            error.put("central_pse_error", "Please select Controlling Ministry");
                            form_details.setPse_ministry(ESAPI.encoder().encodeForHTML(form_details.getPse_ministry()));
                            flag = true;
                        } else if (central_pse_error) {
                            error.put("central_pse_error", "Please select correct Controlling Ministry");
                            form_details.setPse_ministry(ESAPI.encoder().encodeForHTML(form_details.getPse_ministry()));
                            flag = true;
                        }
                    } else if (form_details.getPse().equalsIgnoreCase("state_pse")) {
                        boolean state_pse_error = valid.controllingministryValidation(form_details.getPse_state());
                        boolean district_pse_error = valid.controllingministryValidation(form_details.getPse_district());
                        if (form_details.getPse_state().equals("")) {
                            error.put("state_pse_error", "Please select State (Where PSE is located) ");
                            form_details.setPse_state(ESAPI.encoder().encodeForHTML(form_details.getPse_state()));
                            flag = true;
                        } else if (state_pse_error) {
                            error.put("state_pse_error", "Please select correct State (Where PSE is located) ");
                            form_details.setPse_state(ESAPI.encoder().encodeForHTML(form_details.getPse_state()));
                            flag = true;
                        }
                        if (form_details.getPse_district().equals("")) {
                            error.put("district_pse_error", "Please Enter District Name (Where applicant is posted)");
                            form_details.setPse_district(ESAPI.encoder().encodeForHTML(form_details.getPse_district()));
                            flag = true;
                        } else if (district_pse_error) {
                            error.put("district_pse_error", "Please Enter District Name [Only characters,dot(.) amp(&) and whitespace allowed]");
                            form_details.setPse_district(ESAPI.encoder().encodeForHTML(form_details.getPse_district()));
                            flag = true;
                        }
                    } else {
                        error.put("pse_error", "Please select correct Organization Category");
                        form_details.setPse(ESAPI.encoder().encodeForHTML(form_details.getPse()));
                        flag = true;
                    }
                    String capcode = (String) session.get("captcha");
                    if (!form_details.getImgtxt().equals(capcode)) {
                        error.put("cap_error", "Please enter Correct Captcha");
                        form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                    }

//            String dob_err = valid.dobValidation(form_details.getSingle_dob());
//            if (!dob_err.isEmpty() || !"".equals(dob_err)) {
//                error.put("dob_err", dob_err);
//                form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
//                flag = true;
//            }
                    if (session.get("profile-values") != null) {
                        profile_values = (HashMap) session.get("profile-values");
                        employment = profile_values.get("user_employment").toString();
                        session.put("prEmployment", employment);
                        if (profile_values.get("min") != null) {
                            ministry = profile_values.get("min").toString();
                            session.put("prMinistry", ministry);
                        }
                        if (profile_values.get("dept") != null) {
                            department = profile_values.get("dept").toString();
                            session.put("prDepartment", department);
                        }
                        if (profile_values.get("stateCode") != null) {
                            state = profile_values.get("stateCode").toString();
                            session.put("prState", state);
                        }
                        if (profile_values.get("dept") != null) {
                            stateDepartment = profile_values.get("dept").toString();
                            session.put("prStateDepartment", stateDepartment);
                        }
                        if (profile_values.get("Org") != null) {
                            organization = profile_values.get("Org").toString();
                            session.put("prOrganization", organization);
                        }
                    }

                    String dor_err = valid.gemdorValidation(form_details.getSingle_dor());
                    if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                        error.put("dor_err", dor_err);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                        flag = true;
                    }
                    if (email1_pse_error) {
                        error.put("email1_pse_error", "Enter Email Address [e.g: abc.xyz@gembuyer.in]");
                        form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email1());
                        setWhichemail("1");
                        setWhichform("gem_form1");
                        checkAvailableEmail();
                    }
                    if (email2_pse_error) {
                        error.put("email2_pse_error", "Enter Email Address [e.g: abc.xyz@gembuyer.in]");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    } else {
                        setData(form_details.getSingle_email2());
                        setWhichemail("2");
                        setWhichform("gem_form1");
                        checkAvailableEmail();
                    }
                    if (form_details.getSingle_email1().equals(form_details.getSingle_email2())) {
                        error.put("email2_pse_error", "Preferred Email2 cannot be same as Preferred Email1");
                        form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                        flag = true;
                    }
                    if (traffic_pse_error) {
                        error.put("traffic_pse_error", "Please Enter Your Projected Monthly Traffic OR Correct Monthly Traffic [should be Numeric,minimum 1000] ");
                        form_details.setDomestic_traf(ESAPI.encoder().encodeForHTML(form_details.getDomestic_traf()));
                        flag = true;
                    }
                    System.out.println("form_details.getFwd_ofc_mobile()" + form_details.getFwd_ofc_mobile());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getFwd_ofc_mobile());
                    if (hod_mobile_error) {
                        error.put("fwd_mobile_err", "Enter Mobile Number [e.g: +919999999999]");
                        form_details.setFwd_ofc_mobile(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_mobile()));
                        flag = true;
                    }
                    boolean hod_email_error = valid.EmailValidation(form_details.getFwd_ofc_email());
                    if (hod_email_error) {
                        error.put("fwd_email_err", "Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                        form_details.setFwd_ofc_email(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_email()));
                        flag = true;
                    }
                    boolean hod_telephone_error = valid.telValidation(form_details.getFwd_ofc_tel());
                    if (hod_telephone_error) {
                        error.put("fwd_tel_err", "Enter Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                        form_details.setFwd_ofc_tel(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_tel()));
                        flag = true;
                    }
                    boolean hod_address_error = valid.addValidation(form_details.getFwd_ofc_add());
                    if (hod_address_error) {
                        error.put("fwd_add_err", "Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                        form_details.setFwd_ofc_add(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_add()));
                        flag = true;
                    }

                    boolean hod_name_error = valid.nameValidation(form_details.getFwd_ofc_name());
                    System.out.println("hod_name_error" + hod_name_error);
                    if (hod_name_error) {
                        System.out.println("inisde name error");
                        error.put("fwd_name_err", "Enter Name [characters,dot(.) and whitespace]");
                        form_details.setFwd_ofc_name(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_name()));
                        flag = true;
                    }
                    boolean hod_desig_error = valid.desigValidation(form_details.getFwd_ofc_design());
                    if (hod_desig_error) {
                        error.put("fwd_desig_err", "Enter Designation [characters,Alphanumeirc,whitespace and [. , - &]]");
                        form_details.setFwd_ofc_design(ESAPI.encoder().encodeForHTML(form_details.getFwd_ofc_design()));
                        flag = true;
                    }

                    // check for nic domain
                    if (!form_details.getFwd_ofc_email().equals("")) {
                        fwdofcDetails = (HashMap) profileService.getHODdetails(form_details.getFwd_ofc_email());

                        if (fwdofcDetails.containsKey("error")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in gem controller tab 1");
                            error.put("fwd_email_err", "This is not a valid Government email address.");
                        } else {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic emailin gem controller tab 1");
                        }

                    }
                    if (!fwdofcDetails.get("mobile").equals(form_details.getFwd_ofc_mobile())) {
                        error.put("fwd_mobile_err", "Forwarding officer's mobile number should be same in ldap");

                    }

                    String SessionCSRFRandom = session.get("CSRFRandom").toString();

                    if (!SessionCSRFRandom.equals(CSRFRandom)) {
                        error.put("csrf_error", "Invalid Security Token !");
                        flag = true;
                    }
                    // System.out.println("form_details.getPrimary_user()"+form_details.getPrimary_user());
                    System.out.println("form_details.getPrimary_user()" + form_details.getPrimary_user());
                    if (form_details.getPrimary_user() != null) {

                        boolean primary_user_error = valid.checkradioValidation(form_details.getPrimary_user());

                        if (primary_user_error) {

                            error.put("primary_user_err", "Please select Primary User value");
                            form_details.setPrimary_user(ESAPI.encoder().encodeForHTML(form_details.getPrimary_user()));
                            flag = true;
                        }

                    } else {

                        error.put("primary_user_err", "Please select Primary User value");
                        form_details.setPrimary_user(ESAPI.encoder().encodeForHTML(form_details.getPrimary_user()));
                        flag = true;
                    }
                    if (form_details.getPrimary_user() != null) {
                        if (form_details.getPrimary_user().equals("yes")) {
                            boolean primary_userid_error = valid.addValidation(form_details.getPrimary_user_id());
                            if (primary_userid_error) {
                                error.put("primary_id_err", "Please Enter User Id");
                                form_details.setPrimary_user_id(ESAPI.encoder().encodeForHTML(form_details.getPrimary_user_id()));
                                flag = true;
                            }
                        }
                    }

                    boolean role_assign_err = valid.nameValidation(form_details.getRole_assign());
                    if (role_assign_err) {
                        error.put("role_assign_err", "Please Enter Role to be assign.");
                        form_details.setRole_assign(ESAPI.encoder().encodeForHTML(form_details.getRole_assign()));
                        flag = true;
                    }
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "error Map in gem controller tab 1: " + error);

                // end, code added by pr on 22ndjan18
                if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {
                    if (session.get("update_without_oldmobile").equals("no")) {
                        emailservice.Gem_tab1(form_details);
                        System.out.println("form_details.getgetPrimary_user" + form_details.getPrimary_user());
                        profile_values = (HashMap) session.get("profile-values");
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }

            } else {
                error.put("email_info_error", "Your request is already pending/completed so you can not fill any more request");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in gem controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String Gem_tab2() {
        try {
            db = new Database();
            ldap = new Ldap();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );
            session.put("prEmployment", form_details.getUser_employment());
            session.put("prMinistry", form_details.getMin());
            session.put("prDepartment", form_details.getDept());
            session.put("prState", form_details.getStateCode());
            session.put("prStateDepartment", form_details.getDept());
            session.put("prOrganization", form_details.getOrg());
            boolean flag = false;
            boolean email1_pse_error = valid.EmailValidation(form_details.getSingle_email1());
            boolean email2_pse_error = valid.EmailValidation(form_details.getSingle_email2());
            boolean traffic_pse_error = valid.domesticTrafficvalidation(form_details.getDomestic_traf());
            profile_values = (HashMap) session.get("profile-values");
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());

            userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

            } else {
                if (userdata.getHodData().isGovEmployee()) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                }
            }
            if (form_details.getPse().equals("")) {
                error.put("pse_error", "Please select Organization Category");
                form_details.setPse(ESAPI.encoder().encodeForHTML(form_details.getPse()));
                flag = true;
            } else if (form_details.getPse().equalsIgnoreCase("central_pse")) {
                boolean central_pse_error = valid.controllingministryValidation(form_details.getPse_ministry());
                if (form_details.getPse_ministry().equals("")) {
                    error.put("central_pse_error", "Please select Controlling Ministry");
                    form_details.setPse_ministry(ESAPI.encoder().encodeForHTML(form_details.getPse_ministry()));
                    flag = true;
                } else if (central_pse_error) {
                    error.put("central_pse_error", "Please select correct Controlling Ministry");
                    form_details.setPse_ministry(ESAPI.encoder().encodeForHTML(form_details.getPse_ministry()));
                    flag = true;
                }
            } else if (form_details.getPse().equalsIgnoreCase("state_pse")) {
                boolean state_pse_error = valid.controllingministryValidation(form_details.getPse_state());
                boolean district_pse_error = valid.controllingministryValidation(form_details.getPse_district());
                if (form_details.getPse_state().equals("")) {
                    error.put("state_pse_error", "Please select State (Where PSE is located) ");
                    form_details.setPse_state(ESAPI.encoder().encodeForHTML(form_details.getPse_state()));
                    flag = true;
                } else if (state_pse_error) {
                    error.put("state_pse_error", "Please select correct State (Where PSE is located) ");
                    form_details.setPse_state(ESAPI.encoder().encodeForHTML(form_details.getPse_state()));
                    flag = true;
                }
                if (form_details.getPse_district().equals("")) {
                    error.put("district_pse_error", "Please Enter District Name (Where applicant is posted)");
                    form_details.setPse_district(ESAPI.encoder().encodeForHTML(form_details.getPse_district()));
                    flag = true;
                } else if (district_pse_error) {
                    error.put("district_pse_error", "Please Enter correct District Name (Where applicant is posted)");
                    form_details.setPse_district(ESAPI.encoder().encodeForHTML(form_details.getPse_district()));
                    flag = true;
                }
            } else {
                error.put("pse_error", "Please select correct Organization Category");
                form_details.setPse(ESAPI.encoder().encodeForHTML(form_details.getPse()));
                flag = true;
            }

//            String dob_err = valid.dobValidation(form_details.getSingle_dob());
//            if (!dob_err.isEmpty() || !"".equals(dob_err)) {
//                error.put("dob_err", dob_err);
//                flag = true;
//                form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
//            }
            String dor_err = valid.gemdorValidation(form_details.getSingle_dor());
            if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                error.put("dor_err", dor_err);
                flag = true;
                form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
            }
            System.out.println("form_details.getPrimary_user()" + form_details.getPrimary_user());
            if (form_details.getPrimary_user() != null) {

                boolean primary_user_error = valid.checkradioValidation(form_details.getPrimary_user());

                if (primary_user_error) {

                    error.put("primary_user_err", "Please select Primary User value");
                    form_details.setPrimary_user(ESAPI.encoder().encodeForHTML(form_details.getPrimary_user()));
                    flag = true;
                }

            } else {

                error.put("primary_user_err", "Please select Primary User value");
                form_details.setPrimary_user(ESAPI.encoder().encodeForHTML(form_details.getPrimary_user()));
                flag = true;
            }
            if (form_details.getPrimary_user().equals("yes")) {

                boolean primary_userid_error = valid.addValidation(form_details.getPrimary_user_id());
                if (primary_userid_error) {

                    error.put("primary_id_err", "Please Enter User Id");
                    form_details.setPrimary_user_id(ESAPI.encoder().encodeForHTML(form_details.getPrimary_user_id()));
                    flag = true;
                }
            }

            boolean role_assign_err = valid.nameValidation(form_details.getRole_assign());
            if (role_assign_err) {
                error.put("role_assign_err", "Please Enter Role to be assign.");
                form_details.setRole_assign(ESAPI.encoder().encodeForHTML(form_details.getRole_assign()));
                flag = true;
            }
            if (email1_pse_error) {
                error.put("email1_pse_error", "Enter Email Address [e.g: abc.xyz@gembuyer.in]");
                flag = true;
                form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
            } else {
                setData(form_details.getSingle_email1());
                setWhichemail("1");
                setWhichform("gem_form2");
                checkAvailableEmail();
            }
            if (email2_pse_error) {
                error.put("email2_pse_error", "Enter Email Address [e.g: abc.xyz@gembuyer.in]");
                flag = true;
                form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
            } else {
                setData(form_details.getSingle_email2());
                setWhichemail("2");
                setWhichform("gem_form2");
                checkAvailableEmail();
            }

            if (form_details.getSingle_email1().equals(form_details.getSingle_email2())) {
                error.put("email2_pse_error", "Preferred Email2 cannot be same as Preferred Email1");
                form_details.setSingle_email2(ESAPI.encoder().encodeForHTML(form_details.getSingle_email2()));
                flag = true;
            }

            if (traffic_pse_error) {
                error.put("traffic_pse_error", "Enter Your Project Monthly Traffic, Numeric Value Only (Minimum 1000)");
                flag = true;
                form_details.setDomestic_traf(ESAPI.encoder().encodeForHTML(form_details.getDomestic_traf()));
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

                boolean fwd_desg_error = valid.addValidation(form_details.getFwd_ofc_design());
                boolean fwd_name_error = valid.nameValidation(form_details.getFwd_ofc_name());
                boolean fwd_mobile_error = valid.MobileValidation(form_details.getFwd_ofc_mobile());
                boolean fwd_email_error = valid.EmailValidation(form_details.getFwd_ofc_email());
                boolean fwd_telephone_error = valid.telValidation(form_details.getFwd_ofc_tel());

                if (!form_details.getFwd_ofc_email().equals("")) {
                    fwdofcDetails = (HashMap) profileService.getHODdetails(form_details.getFwd_ofc_email());

                    if (fwdofcDetails.containsKey("error")) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in gem controller tab 1");
                        error.put("fwd_email_err", "This is not a valid Government email address.");
                    } else {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic emailin gem controller tab 1");
                    }
                }

                if (fwd_desg_error == true) {
                    error.put("fwd_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                }
                if (fwd_name_error == true) {
                    error.put("fwd_name_error", "Enter Forwarding Officer Name [Only characters,dot(.) and whitespace allowed]");
                }
                if (form_details.getFwd_ofc_mobile().trim().startsWith("+")) {
                    if ((fwd_mobile_error == true) && (form_details.getFwd_ofc_mobile().trim().startsWith("+91"))) {
                        error.put("fwd_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                    } else if ((fwd_mobile_error == true) && (!form_details.getFwd_ofc_mobile().trim().startsWith("+91"))) {
                        error.put("fwd_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
                    } else {

                    }
                } else {

                    error.put("fwd_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                }

                if (!fwdofcDetails.get("mobile").equals(form_details.getFwd_ofc_mobile())) {
                    error.put("fwd_mobile_err", "Forwarding officer's mobile number should be same in ldap");

                }
                if (fwd_telephone_error == true) {
                    error.put("fwd_telephone_error", "Please enter Reporting/Nodal/Forwarding Officer Telephone in correct format");
                }

                if (!hodDetails.get("bo").toString().equals("no bo")) {
                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                    }
                    if (nic_employee && nicemployeeeditable.equals("no")) {
                        map = db.fetchUserValuesFromOAD(userdata.getUid());
                        if (map.size() > 0) {
                            if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting officer details can not be changed.");

                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        } else {
                            if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                error.put("hod_email_error", "Please change it in your profile and then come back");

                            }

                        }
                    }
                }

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
                if (!hodDetails.get("bo").toString().equals("no bo")) {
                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                    }
                    if (nic_employee && nicemployeeeditable.equals("no")) {
                        map = db.fetchUserValuesFromOAD(userdata.getUid());
                        if (map.size() > 0) {
                            if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting officer details can not be changed.");

                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        } else {
                            if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                error.put("hod_email_error", "Please change it in your profile and then come back");

                            }

                        }
                    }
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
                        ArrayList b = null;
                        //if (userdata.getAliases() != null && !userdata.getAliases().equals("") && !userdata.getAliases().toString().trim().equals("[]")) {
                        //  b = (ArrayList) userdata.getAliases();
                        // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                        //b = (ArrayList) a.get("mailequi");
                        Set s = (Set) userdata.getAliases();

                        System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);

                        ArrayList<String> newAr = new ArrayList<String>();

                        newAr.addAll(s);
                        ArrayList email_aliases = null;
                        email_aliases = newAr;
                        if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {

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

            if (ldap_employee) {

                System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                if (hodDetails.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Reporting officer should be govt employee");
                }

            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for gem controller tab 2: " + error);

            if (getAction_type().equals("submit")) {
                if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {
                    session.put("gem_details", form_details);

                    String dup_exist = "";

                    if (session.get("dupemail") != null) {
                        dup_exist = session.get("dupemail").toString();
                    }
                    hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                    System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
//                    if (hodDetails.get("bo").toString().equals("no bo")) {
//                        if (form_details.getUnder_sec_email().trim() != null) {
//                            profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
//                            profile_values.put("under_sec_mobile", form_details.getUnder_sec_mobile().trim());
//                            profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
//                            profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
//                            profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
//                        } else {
//
//                            profile_values.put("under_sec_email", "");
//                            profile_values.put("under_sec_mobile", "");
//                            profile_values.put("under_sec_name", "");
//                            profile_values.put("under_sec_desig", "");
//                            profile_values.put("under_sec_tel", "");
//                        }
//                    }
                    if (session.get("update_without_oldmobile").equals("no")) {
                        String ref_num = emailservice.Gem_tab2(form_details, profile_values);
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

                    profile_values.put("fwd_ofc_name", form_details.getFwd_ofc_name().trim());
                    profile_values.put("fwd_ofc_mobile", form_details.getFwd_ofc_mobile().trim());
                    profile_values.put("fwd_ofc_email", form_details.getFwd_ofc_email().trim());
                    profile_values.put("fwd_ofc_tel", form_details.getFwd_ofc_tel().trim());
                    profile_values.put("fwd_ofc_desig", form_details.getFwd_ofc_design().trim());
                    profile_values.put("fwd_ofc_add", form_details.getFwd_ofc_add().trim());

                    //ArrayList mailaddress = entities.LdapQuery.GetMailEqui(form_details.getUser_email().trim());
                    //profile_values.put("mailequi", mailaddress);
                    session.put("profile-values", profile_values);
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (getAction_type().equals("validate")) {
                if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {
                    profile_values = (HashMap) session.get("profile-values");
                    // String ref_num = emailservice.Gem_tab2(form_details, profile_values);
                    // session.put("ref_num", ref_num);

                    profile_values.put("fwd_ofc_name", form_details.getFwd_ofc_name().trim());
                    profile_values.put("fwd_ofc_mobile", form_details.getFwd_ofc_mobile().trim());
                    profile_values.put("fwd_ofc_email", form_details.getFwd_ofc_email().trim());
                    profile_values.put("fwd_ofc_tel", form_details.getFwd_ofc_tel().trim());
                    profile_values.put("fwd_ofc_desig", form_details.getFwd_ofc_design().trim());
                    profile_values.put("fwd_ofc_add", form_details.getFwd_ofc_add().trim());

                    //ArrayList mailaddress = entities.LdapQuery.GetMailEqui(form_details.getUser_email().trim());
                    //profile_values.put("mailequi", mailaddress);
                    session.put("profile-values", profile_values);
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (getAction_type().equals("update")) // else if added by pr on 1stfeb18
            {
                //if (error.isEmpty()) {
                if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) {
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
                    action_type = "";
                }
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String email_act_tab1() {
        try {
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            profile_values = (HashMap) session.get("profile-values");
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            Boolean flag = false;
            String capcode = (String) session.get("captcha");
            System.out.println("form_details.getImgtxt()" + form_details.getImgtxt() + "capcode" + capcode);
            System.out.println("Value of cert in EmailAct:::" + cert);
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                flag = true;
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for email activate controller tab 1: " + error);
            System.out.println("error:::::" + error);
            System.out.println("flag::::::::" + flag + "error.get(\"errorMsg1\")" + error.get("errorMsg1"));
            // end, code added by pr on 22ndjan18
            session.put("emailrequest", emailrequest);
            if (emailrequest.equals("email_act")) {
                boolean pref1_error = valid.EmailValidation(form_details.getAct_email1());
                if (pref1_error == true) {
                    error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                    form_details.setSingle_email1(ESAPI.encoder().encodeForHTML(form_details.getSingle_email1()));
                    flag = true;
                } else {
                    setData(form_details.getAct_email1());
                    setWhichemail("1");
                    setWhichform("email_act1," + "nocheck" + "," + null);
                    checkPrefmail();
                }
                String dor_err = valid.activatedorValidation(form_details.getSingle_dor());
                if (!dor_err.isEmpty() || !"".equals(dor_err)) {
                    flag = true;
                    error.put("dor_err", dor_err);
                    form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                }
                System.out.println("cert@@@@@@@" + cert + "uploaded file name" + session.get("uploaded_filename"));
                if (!form_details.getSingle_emp_type().equals("emp_regular")) {
                    if (session.get("hardware_uploaded_filename") != null) {
                        if (!cert.equals("true")) {
                            flag = true;
                            error.put("file_err", "Please upload work order in PDF format only");
                        }
                    }
                }

                if ((!flag) && error.get("errorMsg1").toString().contains("is available for activation")) {
                    if (session.get("update_without_oldmobile").equals("no")) {
                        emailservice.email_act_tab1(form_details);
                        hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                        String bo = hodDetails.get("bo").toString();
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                        profile_values.put("bo_check", bo);
                        if (session.get("hardware_uploaded_filename") != null) {
                            String uploaded_filename = session.get("hardware_uploaded_filename").toString();
                            String renamed_filepath = session.get("hardware_renamed_filepath").toString();
                            form_details.setUploaded_filename(uploaded_filename);
                            form_details.setRenamed_filepath(renamed_filepath);
                        } else {
                            form_details.setUploaded_filename("");
                            form_details.setRenamed_filepath("");
                        }
                    }
                } else {
                    System.out.println("inside else");
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                boolean pref1_error = valid.EmailValidation(form_details.getDeact_email1());
                if (pref1_error == true) {
                    error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                    form_details.setDeact_email1(ESAPI.encoder().encodeForHTML(form_details.getDeact_email1()));
                    flag = true;
                } else {
                    setData(form_details.getDeact_email1());
                    setWhichemail("1");
                    setWhichform("email_deact," + "nocheck" + "," + null);
                    checkPrefmailForDeactivation();
                }
                if ((!flag) && error.get("errorMsg1").toString().contains("is available for deactivation")) {
                    emailservice.email_act_tab1(form_details);
                    hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                    String bo = hodDetails.get("bo").toString();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                    profile_values.put("bo_check", bo);

                } else {
                    System.out.println("inside else");
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String email_act_tab2() {
        db = new Database();
        UserData userdata = (UserData) session.get("uservalues");

        boolean ldap_employee = userdata.isIsEmailValidated();
        boolean nic_employee = userdata.isIsNICEmployee();
        HodData hoddata = (HodData) userdata.getHodData();
        UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();
        String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
        ldap = new Ldap();

        try {
            error.clear();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IN TAB 2");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );
            boolean pref1_error = valid.EmailValidation(form_details.getAct_email1());
            Boolean flag = false;
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            String bo = hodDetails.get("bo").toString();
            underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
            String bo_undersec = underSecDetails.get("bo").toString();
            profile_values = (HashMap) session.get("profile-values");
            userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

            } else {
                if (userdata.getHodData().isGovEmployee()) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                }
            }

            if (pref1_error == true) {
                error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                form_details.setAct_email1(ESAPI.encoder().encodeForHTML(form_details.getAct_email1()));
                flag = true;
            } else {
                setData(form_details.getAct_email1());
                setWhichemail("1");
                setWhichform("email_act2," + "nocheck" + "," + null);
                checkPrefmail();
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
                if (bo.equals("no bo")) {
                    if (ldap_employee) {

                        error.put("hod_email_error", "Reporting officer should be govt employee");
                    } else {
                        if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                            if (hodDetails.get("bo").toString().equals("no bo")) {
                                error.put("hod_email_error1", "Reporting officer should be govt employee");
                            }
                        }
                        boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                        System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                        if (check_mobile) {
                            error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                        }
                    }

                    underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                    form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString());

                    if (underSecDetails.get("bo").equals("no bo")) {
                        error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                    } else {

                    }

                    if (bo_undersec.equals("no bo")) {
                        error.put("under_sec_email_error", "Under secretary should be government employee");
                    } else {

                        boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                        boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                        boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                        boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                        System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                        boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                        System.out.println("inisde else in validation:::::::::::::");
                        if (under_sec_email_error == true) {
                            error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                        }
                        if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                            error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                        }
                        if (under_sec_desg_error == true) {
                            error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                        }
                        if (under_sec_name_error == true) {
                            error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                        }
                        if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                            error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                        }

                        if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                            error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                        }
                        if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                            error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                        }

                        if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                            if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                            } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                            } else {

                            }
                        } else {

                            error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                        }
                        if (under_sec_telephone_error == true) {
                            error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                        }
                    }
                } else {

//                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
//                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");
//
//                    }
                    if (nic_employee && nicemployeeeditable.equals("no")) {
                        map = db.fetchUserValuesFromOAD(userdata.getUid());
                        if (map.get("hod_mobile") != null) {
                            form_details.setHod_mobile(map.get("hod_mobile"));
                        }

                        //map = db.fetchUserValuesFromOAD(userdata.getUid());
                        if (map.size() > 0) {
                            if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting officer details can not be changed.");

                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        } else {
                            if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                error.put("hod_email_error", "Please change it in your profile and then come back");

                            }
                        }
                    }

                }
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());

                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                }
                if (hod_name_error == true) {
                    error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                }
                if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                    error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile no.");
                }
                if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                    error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile no.");
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
                            // ArrayList b = null;
                            //if (userdata.getAliases() != null && !userdata.getAliases().equals("") && !userdata.getAliases().toString().trim().equals("[]")) {
                            // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                            //  b = (ArrayList) userdata.getAliases();
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
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
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
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
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
                flag = true;
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 2: " + error);
            // end, code added by pr on 22ndjan18
            if (getAction_type().equals("submit")) {

                System.out.println("inside submit::::::::::::in tab2");
                // if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {

                // if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain")) {
                String dup_exist = "";

                if (session.get("dupemail") != null) {
                    dup_exist = session.get("dupemail").toString();
                }
                System.out.println("dup_exist:::::" + dup_exist);

                if (form_details.getUnder_sec_email().trim() != null) {
                    profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                    profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                    profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                    profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                    profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());

                } else {

                    profile_values.put("under_sec_email", "");
                    profile_values.put("under_sec_mobile", "");
                    profile_values.put("under_sec_name", "");
                    profile_values.put("under_sec_desig", "");
                    profile_values.put("under_sec_tel", "");
                }
                if (session.get("nodomain") != null) {
                    form_details.setAction(session.get("nodomain").toString());
                } else {
                    form_details.setAction("");
                }

                System.out.println("inside submit::::::::;" + session.get("hardware_uploaded_filename"));
                if (session.get("hardware_uploaded_filename") != null) {
                    String uploaded_filename = session.get("hardware_uploaded_filename").toString();
                    String renamed_filepath = session.get("hardware_renamed_filepath").toString();
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                } else {
                    String uploaded_filename = "";
                    String renamed_filepath = "";
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                }

                // form_details.setRequest_type(emailReq);
                if (session.get("update_without_oldmobile").equals("no")) {
                    String ref_num = emailservice.email_act_tab2(form_details, profile_values, "email_act");
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
                            if (form_details.getUnder_sec_email().trim() != null) {
                                profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                                profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                                profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                                profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            } else {

                                profile_values.put("under_sec_email", "");
                                profile_values.put("under_sec_mobile", "");
                                profile_values.put("under_sec_name", "");
                                profile_values.put("under_sec_desig", "");
                                profile_values.put("under_sec_tel", "");
                            }
                        }
                        //}
                        session.put("emailactive_details", form_details);
                        session.put("ref_num", ref_num);

                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                }
            } else if (getAction_type().equals("validate")) {
                System.out.println("inside validate::::::::::::in tab2" + error);
                if ((!flag) && error.get("errorMsg1").toString().contains("is available for activation")) {
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
                } else {

                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (getAction_type().equals("update")) // else if added by pr on 6thfeb18
            {
                System.out.println("inside update::::::::::::in tab2" + error);
                if ((!flag) && error.get("errorMsg1").toString().contains("is available for activation")) {
                    System.out.println("inside update::::::::::::in tab2" + error);
                    //if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {
                    String ref = session.get("ref").toString();
                    //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                    String admin_role = "NA";
                    if (session.get("admin_role") != null) {
                        admin_role = session.get("admin_role").toString();
                    }
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "before update :::::::::");
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
                        // }
                    } else {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                }
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 2: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String email_deact_tab1() {
        try {
            UserData userdata = (UserData) session.get("uservalues");
            profile_values = (HashMap) session.get("profile-values");
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            Boolean flag = false;
            String capcode = (String) session.get("captcha");
            System.out.println("form_details.getImgtxt()" + form_details.getImgtxt() + "capcode" + capcode);
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                flag = true;
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for email deactivate controller tab 1: " + error);
            boolean pref1_error = valid.EmailValidation(form_details.getDeact_email1());
            if (pref1_error == true) {
                error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                form_details.setDeact_email1(ESAPI.encoder().encodeForHTML(form_details.getDeact_email1()));
                flag = true;
            } else {
                setData(form_details.getDeact_email1());
                setWhichemail("1");
                setWhichform("email_deact," + "nocheck" + "," + null);
                checkPrefmailForDeactivation();
            }
            // if ((!flag) && error.get("errorMsg1").toString().contains("is available for deactivation")) {
            if ((!flag)) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    emailservice.email_deact_tab1(form_details);
                    hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                    String bo = hodDetails.get("bo").toString();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                    profile_values.put("bo_check", bo);
                }

            } else {
                System.out.println("inside elseeeeeeeeeeee");
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String email_deact_tab2() {
        db = new Database();
        UserData userdata = (UserData) session.get("uservalues");
        boolean ldap_employee = userdata.isIsEmailValidated();
        boolean nic_employee = userdata.isIsNICEmployee();
        HodData hoddata = (HodData) userdata.getHodData();
        UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();
        //ldap = new Ldap();
        String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

        try {
            error.clear();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IN TAB 2");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            boolean pref1_error = valid.EmailValidation(form_details.getDeact_email1());
            Boolean flag = false;
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            String bo = hodDetails.get("bo").toString();
            underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
            String bo_undersec = underSecDetails.get("bo").toString();
            profile_values = (HashMap) session.get("profile-values");
            userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

            } else {
                if (userdata.getHodData().isGovEmployee()) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());

                }
            }

            if (pref1_error == true) {
                error.put("pref1_error", "Enter Email Address [e.g: abc.xyz@gov.in]");
                form_details.setDeact_email1(ESAPI.encoder().encodeForHTML(form_details.getDeact_email1()));
                flag = true;
            } else {
                setData(form_details.getDeact_email1());
                setWhichemail("1");
                setWhichform("email_deact2," + "nocheck" + "," + null);
                checkPrefmailForDeactivation();
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
                if (bo.equals("no bo")) {
                    if (ldap_employee) {

                        error.put("hod_email_error", "Reporting officer should be govt employee");
                    } else {
                        if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                            if (hodDetails.get("bo").toString().equals("no bo")) {
                                error.put("hod_email_error1", "Reporting officer should be govt employee");
                            }
                        }
                        boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                        System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                        if (check_mobile) {
                            error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                        }
                    }

                    underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                    form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString());

                    if (underSecDetails.get("bo").equals("no bo")) {
                        error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                    } else {

                    }

                    if (bo_undersec.equals("no bo")) {
                        error.put("under_sec_email_error", "Under secretary should be government employee");
                    } else {

                        boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                        boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                        boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                        boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                        System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                        boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                        System.out.println("inisde else in validation:::::::::::::");
                        if (under_sec_email_error == true) {
                            error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                        }
                        if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                            error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                        }
                        if (under_sec_desg_error == true) {
                            error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                        }
                        if (under_sec_name_error == true) {
                            error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                        }
                        if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                            error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                        }

                        if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                            error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                        }
                        if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                            error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                        }

                        if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                            if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                            } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                            } else {

                            }
                        } else {

                            error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                        }
                        if (under_sec_telephone_error == true) {
                            error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                        }
                    }
                } else {

//                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
//                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");
//
//                    }
                    if (nic_employee && nicemployeeeditable.equals("no")) {
                        map = db.fetchUserValuesFromOAD(userdata.getUid());
                        if (map.get("hod_mobile") != null) {
                            form_details.setHod_mobile(map.get("hod_mobile"));
                        }

                        //map = db.fetchUserValuesFromOAD(userdata.getUid());
                        if (map.size() > 0) {
                            if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting officer details can not be changed.");

                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        } else {
                            if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                error.put("hod_email_error", "Please change it in your profile and then come back");

                            }
                        }
                    }

                }
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());

                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                }
                if (hod_name_error == true) {
                    error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                }
                if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                    error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile no.");
                }
                if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                    error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile no.");
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
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
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
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
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
                flag = true;
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 2: " + error);
            // end, code added by pr on 22ndjan18
            if (getAction_type().equals("submit")) {

                System.out.println("inside submit::::::::::::in tab2");
                // if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {

                // if (!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain")) {
                String dup_exist = "";

                if (session.get("dupemail") != null) {
                    dup_exist = session.get("dupemail").toString();
                }
                System.out.println("dup_exist:::::" + dup_exist);

                if (form_details.getUnder_sec_email().trim() != null) {
                    profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                    profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                    profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                    profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                    profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());

                } else {

                    profile_values.put("under_sec_email", "");
                    profile_values.put("under_sec_mobile", "");
                    profile_values.put("under_sec_name", "");
                    profile_values.put("under_sec_desig", "");
                    profile_values.put("under_sec_tel", "");
                }
                // form_details.setRequest_type(emailReq);
                if (session.get("update_without_oldmobile").equals("no")) {
                    String ref_num = emailservice.email_deact_tab2(form_details, profile_values);
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
                            if (form_details.getUnder_sec_email().trim() != null) {
                                profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                                profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                                profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                                profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            } else {

                                profile_values.put("under_sec_email", "");
                                profile_values.put("under_sec_mobile", "");
                                profile_values.put("under_sec_name", "");
                                profile_values.put("under_sec_desig", "");
                                profile_values.put("under_sec_tel", "");
                            }
                        }
                        //}
                        session.put("emailactive_details", form_details);
                        session.put("ref_num", ref_num);
                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                }
            } else if (getAction_type().equals("validate")) {
                System.out.println("inside validate::::::::::::in tab2" + error);
                if ((!flag) && error.get("errorMsg1").toString().contains("is available for deactivation")) {
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
                } else {

                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (getAction_type().equals("update")) // else if added by pr on 6thfeb18
            {
                System.out.println("inside update::::::::::::in tab2" + error + "errormsg1" + error.get("errorMsg1").toString());
                if ((!flag) && error.get("errorMsg1").toString().contains("is available for deactivation")) {
                    System.out.println("inside update::::::::::::in tab2" + error);
                    //if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {
                    String ref = session.get("ref").toString();
                    //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                    String admin_role = "NA";
                    if (session.get("admin_role") != null) {
                        admin_role = session.get("admin_role").toString();
                    }
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "before update :::::::::");
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
                        // }
                    } else {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                }
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 2: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

//    public String checkAvailableEmailFromAjax() {
//        UserData userdata = (UserData) session.get("uservalues");
//        Ldap ldap = new Ldap();
//        BulkValidation bulkValidate = new BulkValidation();
//        Map values = new HashMap();
//        String mobile = "";
//        if (session.get("authMobile") != null) {
//            mobile = session.get("authMobile").toString();
//        } else {
//            mobile = userdata.getMobile();
//        }
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "which: " + whichform);
//        String token[] = whichform.split(",");
//        String formType = token[0];
//        String emp_detail = "";
//        String id_type = "";
//
//        if (data.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$") && whichemail.matches("^[0-9]{1}$") && whichform.matches("^[a-zA-Z0-9,\\_]{2,50}$")) {
//            String[] mle = data.split("@");
//            String uid = mle[0];
//            String domainentered = mle[1];
//
//            if (!formType.contains("nkn") && !formType.contains("gem")) {
//                emp_detail = token[1];
//                id_type = token[2];
//            } else {
//                emp_detail = "";
//                id_type = "";
//            }
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "employement before session" + employment);
//            if (employment != null && !employment.isEmpty()) {
//            } else {
//                if (session.get("profile-values") != null) {
//                    profile_values = (HashMap) session.get("profile-values");
//                    employment = profile_values.get("user_employment").toString();
//                    if (profile_values.get("min") != null) {
//                        ministry = profile_values.get("min").toString();
//                    }
//                    if (profile_values.get("dept") != null) {
//                        department = profile_values.get("dept").toString();
//                    }
//                    if (profile_values.get("stateCode") != null) {
//                        state = profile_values.get("stateCode").toString();
//                    }
//                    if (profile_values.get("dept") != null) {
//                        stateDepartment = profile_values.get("dept").toString();
//                    }
//                    if (profile_values.get("Org") != null) {
//                        organization = profile_values.get("Org").toString();
//                    }
//                }
//
//            }
////            if (employment == null || employment.isEmpty()) {
////                profile_values = (HashMap) session.get("profile-values");
////                employment = profile_values.get("user_employment").toString();
////                ministry = profile_values.get("min").toString();
////                department = profile_values.get("dept").toString();
////                state = profile_values.get("stateCode").toString();
////                stateDepartment = profile_values.get("dept").toString();
////                organization = profile_values.get("Org").toString();
////            }
//
//            session.put("prEmployment", employment);
//            session.put("prMinistry", ministry);
//            session.put("prDepartment", department);
//            session.put("prState", state);
//            session.put("prStateDepartment", stateDepartment);
//            session.put("prOrganization", organization);
//
//            Map<String, String> previewProfile = new HashMap<>();
//            previewProfile.put("form_type", formType);
//            previewProfile.put("emp_type", emp_detail);
//            previewProfile.put("user_employment", employment);
//            previewProfile.put("min", ministry);
//            previewProfile.put("stateCode", state);
//            previewProfile.put("Org", organization);
//            previewProfile.put("id_type", id_type);
//            if (employment.equalsIgnoreCase("state")) {
//                previewProfile.put("dept", stateDepartment);
//            } else {
//                previewProfile.put("dept", department);
//            }
//
//            System.out.println("profile_values::::::" + previewProfile);
//            Set<String> domains = emailservice.getDomain(previewProfile);
//            System.out.println("DOMAINS ALLOWED ::: " + domains);
//            Set<String> emailsAgainstMobile = ldap.fetchEmailsAgainstMobile(mobile);
//
//            if (emailsAgainstMobile.size() > 3) {
//                session.put("dupemail", "yes");
//                error.put("errorMsgDup", "There are more than 3 email addresses already registered against your mobile number (" + mobile + ")");
//            } else if (emailsAgainstMobile.size() == 3) {
//                if (emailsAgainstMobile.contains("-admin")) {
//                    session.put("dupemail_create", "yes");
//                    error.put("errorMsgDup_create", "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
//                } else {
//                    session.put("dupemail", "yes");
//                    error.put("errorMsgDup", "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
//                }
//            } else {
//                session.put("dupemail_create", "");
//                session.put("dupemail", "");
//            }
//
//            if (whichform.contains("single_form1") || whichform.contains("single_form2") || whichform.contains("singleuser_preview")) {
//                values = bulkValidate.Uid(uid, id_type);
//            } else {
//                values = bulkValidate.Uid(uid, "nocheck");
//            }
//
//            if (values.get("valid").equals("true")) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "check for mail");
//                values = bulkValidate.Mail(data, uid, domains);
//                if (values.get("valid").equals("true")) {
//                    error.put("errorMsg" + whichemail, data + " is available for creation");
//                } else {
//                    error.put("errorMsg" + whichemail, values.get("errorMsg").toString());
//                }
//            } else {
//                error.put("errorMsg" + whichemail, values.get("errorMsg").toString());
//            }
//
//        } else {
//            if (whichform.equals("gem_form1") || whichform.equals("gem_form2")) {
//                error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@gembuyer.in]");
//            } else if (whichform.contains("single_form1") || whichform.contains("single_form2") || whichform.contains("singleuser_preview")) {
//                emp_detail = token[1];
//                if (emp_detail.equals("emp_regular")) {
//                    error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@gov.in]");
//                } else if (emp_detail.equals("emp_contract")) {
//                    error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@supportgov.in]");
//                } else {
//                    error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@govcontractor.in]");
//                }
//            } else if (whichform.equals("nkn_single_form1") || whichform.equals("nkn_form2")) {
//                error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@nkn.in]");
//            }
//        }
//        setError(error);
//        return SUCCESS;
//    }
    public String checkAvailableEmail() {

        System.out.println("");
        UserData userdata = (UserData) session.get("uservalues");
        Ldap ldap = new Ldap();
        BulkValidation bulkValidate = new BulkValidation();
        Map values = new HashMap();

        String mobile = "";
        if (applicant_mobile != null && applicant_mobile != "") {
            mobile = applicant_mobile;
            System.out.println("inside if not null applicant mobile" + mobile);
        } else {

            if (session.get("authMobile") != null) {
                mobile = session.get("authMobile").toString();
            } else {
                mobile = userdata.getMobile();
            }

            System.out.println("inside else null applicant mobile" + mobile);

        }
        System.out.println("mobile::::::::::::" + mobile);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "which: " + whichform);
        String token[] = whichform.split(",");
        String formType = token[0];
        String emp_detail = "";
        String id_type = "";

        if (data.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$") && whichemail.matches("^[0-9]{1}$") && whichform.matches("^[a-zA-Z0-9,\\_]{2,50}$")) {
            String[] mle = data.split("@");
            String uid = mle[0];
            String domainentered = mle[1];

            if (!formType.contains("gem")) {
                emp_detail = token[1];
                id_type = token[2];
            } else {
                emp_detail = "";
                id_type = "id_desig";
            }

            if (session.get("prEmployment") != null) {
                employment = session.get("prEmployment").toString();
                if (session.get("prMinistry") != null) {
                    ministry = session.get("prMinistry").toString();
                }
                if (session.get("prDepartment") != null) {
                    department = session.get("prDepartment").toString();
                }
                if (session.get("prState") != null) {
                    state = session.get("prState").toString();
                }
                if (session.get("prStateDepartment") != null) {
                    stateDepartment = session.get("prStateDepartment").toString();
                }
                if (session.get("prOrganization") != null) {
                    organization = session.get("prOrganization").toString();
                }
            } else {
                if (employment == null) {
                    employment = userdata.getUserOfficialData().getEmployment();
                    ministry = userdata.getUserOfficialData().getMinistry();
                    department = userdata.getUserOfficialData().getDepartment();
                    state = userdata.getUserOfficialData().getState();
                    stateDepartment = userdata.getUserOfficialData().getDepartment();
                    organization = userdata.getUserOfficialData().getOrganizationCategory();

                }
            }

            Map<String, String> previewProfile = new HashMap<>();
            previewProfile.put("form_type", formType);
            previewProfile.put("emp_type", emp_detail);
            previewProfile.put("user_employment", employment);
            previewProfile.put("min", ministry);
            previewProfile.put("stateCode", state);
            previewProfile.put("Org", organization);
            previewProfile.put("id_type", id_type);
            if (employment.equalsIgnoreCase("state")) {
                previewProfile.put("dept", stateDepartment);
            } else {
                previewProfile.put("dept", department);
            }

            System.out.println("profile_values::::::" + previewProfile);
            Set<String> domains = emailservice.getDomain(previewProfile);
            System.out.println("DOMAINS ALLOWED ::: " + domains);
            Set<String> emailsAgainstMobile = ldap.fetchEmailsAgainstMobile(mobile);
            System.out.println("emailsAgainstMobile" + emailsAgainstMobile + "emailsAgainstMobile.size()" + emailsAgainstMobile.size());

            if (domains.contains("jcn.nic.in") && emailsAgainstMobile.size() > 5) {
                error.put("errorMsg" + whichemail, "There are already 5 email addresses registered against your mobile number (" + mobile + ")");
            } else if (emailsAgainstMobile.size() > 3) {
                session.put("dupemail", "yes");
                //error.put("errorMsgDup", "There are more than 3 email addresses already registered against your mobile number (" + mobile + ")");
                error.put("errorMsg" + whichemail, "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
            } else if (emailsAgainstMobile.size() == 3) {
                if (emailsAgainstMobile.contains("-admin")) {
                    session.put("dupemail_create", "yes");
                    //error.put("errorMsgDup_create", "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
                    error.put("errorMsg" + whichemail, "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
                } else {
                    session.put("dupemail", "yes");
                    //  error.put("errorMsgDup", "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
                    error.put("errorMsg" + whichemail, "There are already 3 email addresses registered against your mobile number (" + mobile + ")");
                }
            } else {
                session.put("dupemail_create", "");
                session.put("dupemail", "");

                if (whichform.contains("single_form1") || whichform.contains("single_form2") || whichform.contains("singleuser_preview")) {
                    values = bulkValidate.Uid(uid, id_type);
                } else {
                    values = bulkValidate.Uid(uid, "nocheck");
                }

                if (values.get("valid").equals("true")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "check for mail");
                    values = bulkValidate.Mail(data, uid, domains);
                    if (values.get("valid").equals("true")) {
                        error.put("errorMsg" + whichemail, data + " is available for creation");
                    } else {
                        error.put("errorMsg" + whichemail, values.get("errorMsg").toString());
                    }
                } else {
                    error.put("errorMsg" + whichemail, values.get("errorMsg").toString());
                }
            }

        } else {
            if (whichform.equals("gem_form1") || whichform.equals("gem_form2")) {
                error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@gembuyer.in]");
            } else if (whichform.contains("single_form1") || whichform.contains("single_form2") || whichform.contains("singleuser_preview")) {
                emp_detail = token[1];
                if (emp_detail.equals("emp_regular")) {
                    error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@gov.in]");
                } else if (emp_detail.equals("emp_contract")) {
                    error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@supportgov.in]");
                } else {
                    error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@govcontractor.in]");
                }
            } else if (whichform.equals("nkn_single_form1") || whichform.equals("nkn_form2")) {
                error.put("errorMsg" + whichemail, "Enter Email Address [e.g: abc.xyz@nkn.in]");
            }
        }

        setError(error);
        return SUCCESS;
    }

    public String checkPrefmailForDeactivation() {
        System.out.println("data:::::::" + data);
        String[] email = data.toString().split("@");
        String uid = email[0];
        UserData userdata = (UserData) session.get("uservalues");
        System.out.println("user email:::::" + userdata.getEmail());
        Ldap ldap = new Ldap();
        Set<String> aliases = userdata.getAliases();
        if (!data.equals("")) {
            if (ldap.emailValidate(data)) {
                if (ldap.emailValidate(userdata.getEmail())) {
                    if (aliases.contains(data)) {
                            error.put("errorMsg2", data + " is available for deactivation");
                         }
                    
                     else if (checkEmailFromTableSingleBulkGem(data, userdata.getEmail())) {
                        System.out.println("111111@@@@@@@");
                        error.put("errorMsg2", data + " is available for deactivation");

                    } else if(checkEmailForCoord(data, userdata.getEmail())) {
                        System.out.println("2222222222@@@@@@@");
                        error.put("errorMsg2", data + " is available for deactivation");
                    } else {

                        error.put("errorMsg1", data + " is not available for deactivation");
                    }
                } else {
                    error.put("errorMsg1", "Please login with Government email address only");
                }
                setError(error);
                System.out.println("error in checkprefemail:::::::" + error);

            } else {
                error.put("errorMsg1", "Please enter Government email address only");
            }
        }

        return SUCCESS;
    }

    public String checkPrefmail() {
        System.out.println("data:::::::" + data);
        String[] email = data.toString().split("@");
        String uid = email[0];
        UserData userdata = (UserData) session.get("uservalues");
        String[] UserUid = userdata.getEmail().split("@");
        String loggedInUserUid = UserUid[0];
        System.out.println("user email:::::" + userdata.getEmail());
        Ldap ldap = new Ldap();
        if (!data.equals("")) {
            if (ldap.uidEmailValidate(uid, data)) {
                if (ldap.uidEmailValidate(loggedInUserUid, userdata.getEmail())) {
                    if (ldap.isEmailInactive(data)) {
                        if (checkEmailFromTableSingleBulkGem(data, userdata.getEmail())) {
                            System.out.println("Email not available in single table: ");
                            error.put("errorMsg1", data + " is available for activation");

                        } else if (checkEmailForCoord(data, userdata.getEmail())) {
                            System.out.println("2 : Error regarding employment coordinator table");
                            error.put("errorMsg1", data + " is available for activation");
                        } else {

                            error.put("errorMsg1", data + " is not available for activation");
                        }
                        setError(error);
                        System.out.println("error in checkprefemail:::::::" + error);
                    } else {
                        error.put("errorMsg1", data + " is already activated");
                    }
                } else {
                    error.put("errorMsg1", "Please login with Government email address only");
                }
            } else {
                error.put("errorMsg1", "Please enter Government email address only");
            }

        }

        return SUCCESS;
    }

    public boolean checkEmailFromTableSingleBulkGem(String email, String auth_email) {
        System.out.println("inside check email from db");
        Map<String, String> map = fetchAliasesOfUserAndCa(email, auth_email);
        String activateEmail = map.get("user_aliases");
        String loggedEmail = map.get("ca_aliases");
        return emailservice.checkEmailFromTableSingleBulkGem(activateEmail, loggedEmail);
    }
    
    private Map<String, String> fetchAliasesOfUserAndCa(String email, String auth_email){
        Ldap ldap = new Ldap();
        Map<String, String> map = new HashMap<>();
                
        Set<String> aliases = ldap.fetchAliases(email);
        String commaSeparatedAliases = "";

        int sizeOfAliases = aliases.size();

        if (sizeOfAliases > 1 && sizeOfAliases <= 10) {
            for (String email1 : aliases) {
                commaSeparatedAliases += "'" + email1 + "',";
            }
            commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
        } else if (sizeOfAliases == 1) {
            commaSeparatedAliases = "'" + aliases.iterator().next() + "'";
        }
        map.put("user_aliases", commaSeparatedAliases);
        
        Set<String> aliases_auth_email = ldap.fetchAliases(auth_email);
        String commaSeparatedAliases_auth_email = "";

        int sizeOfAliases_auth_email = aliases_auth_email.size();

        if (sizeOfAliases_auth_email > 1 && sizeOfAliases_auth_email <= 10) {
            for (String email1 : aliases_auth_email) {
                commaSeparatedAliases_auth_email += "'" + email1 + "',";
            }
            commaSeparatedAliases_auth_email = commaSeparatedAliases_auth_email.replaceAll(",$", "");
        } else if (sizeOfAliases_auth_email == 1) {
            commaSeparatedAliases_auth_email = "'" + aliases_auth_email.iterator().next() + "'";
        }

        map.put("ca_aliases", commaSeparatedAliases_auth_email);
        return map;
    }

    public boolean checkEmailForCoord(String email, String auth_email) {
        Map BoDetails = new HashMap();
        BoDetails = (HashMap) profileService.getHODdetails(email);
        Map<String, String> map = fetchAliasesOfUserAndCa(email, auth_email);
        String activateEmail = map.get("user_aliases");
        String loggedEmail = map.get("ca_aliases");
        boolean emailFind = emailservice.checkEmailForCoord(activateEmail, loggedEmail);
        return emailFind;

    }

    public static Set<String> convertStringToSet(String str) {
        Set<String> elements = new HashSet<>();
        if (!str.isEmpty()) {
            String[] arr = null;
            arr = str.split(",");
            for (int i = 0; i < arr.length; i++) {
                elements.add(arr[i]);
            }
        }
        return elements;
    }

    public static String convertSetToString(Set<String> co) {
        String string = String.join(", ", co);
        return (string.length() != 0) ? string.toString() : "";
    }

    public String fetchDomainData() {
        profile_values = (HashMap) session.get("profile-values");
        System.out.println("profile values:::::::::" + profile_values);
        profile_values.put("form_type", form_type);
        profile_values.put("emp_type", emp_type);
        profile_values.put("id_type", id_type);
        allowedDomains = emailservice.getDomain(profile_values);
        loggedInEmail = profile_values.get("email").toString(); //line added by sahil on 27 july 2021
        if (profile_values.get("form_type").toString().equalsIgnoreCase("dor_ext")) {
            formTypeDor = "dor_ext";
        }
        return SUCCESS;
    }

    private boolean isEmailDomainValidForDor(String email) {
        profile_values = (HashMap) session.get("profile-values");
        System.out.println("profile values:::::::::" + profile_values);
        profile_values.put("form_type", "dor_ext");
        profile_values.put("emp_type", "");
        profile_values.put("id_type", "");
        allowedDomains = emailservice.getDomain(profile_values);
        String[] email_split = email.split("@");
        return allowedDomains.contains(email_split[1]);
    }

    public String fetchDomainDataforApplicant() {
        System.out.println("fetch for applicant");
        Map profile_values = new HashMap();
        // profile_values = (HashMap) session.get("profile-values");
        System.out.println("profile values:::::::::" + profile_values);
        profile_values.put("form_type", form_type);
        profile_values.put("emp_type", emp_type);
        profile_values.put("id_type", id_type);
        System.out.println("profile_values in fetch domain for applicant" + profile_values);

        if (department.contains("~")) {
            String[] department_val = department.split("~");
            System.out.println("department_val" + department_val);
            String employment = "", min = "", departmentVal = "", stateCode = "", org = "";
            employment = department_val[0];
            profile_values.put("user_employment", employment);
            if (employment.toLowerCase().equals("central")) {
                min = department_val[1];
                departmentVal = department_val[2];
                profile_values.put("min", min);
                profile_values.put("dept", departmentVal);
            }
            if (employment.toLowerCase().equals("state")) {
                stateCode = department_val[1];
                departmentVal = department_val[2];
                profile_values.put("stateCode", stateCode);
                profile_values.put("dept", departmentVal);
            }

            if (employment.toLowerCase().equals("ut") || employment.toLowerCase().equals("nkn") || employment.toLowerCase().equals("psu") || employment.toLowerCase().equals("others") || employment.toLowerCase().equals("project") || employment.toLowerCase().equals("const")) {
                org = department_val[1];
                profile_values.put("Org", org);

            }
        }
        allowedDomains = emailservice.getDomain(profile_values);
        System.out.println("allowed domain in fetch for applicant" + allowedDomains);
        return SUCCESS;
    }

    private String fetchCoordinator(String dept) {
        return emailservice.fetchCoordinator(dept);
    }

    public String dor_ext_tab1() {
        try {
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata:::::::::::" + userdata);
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("userdata:::::::::::" + userdata);
            profile_values = (HashMap) session.get("profile-values");
            String prevDoe = "", dob = "", newDor = "";
            if (session.get("dateOfRetirement") != null) {
                prevDoe = session.get("dateOfRetirement").toString();
            }
            if (session.get("dateOfBirth") != null) {
                dob = session.get("dateOfBirth").toString();
            }
            if (session.get("newdor") != null) {
                newDor = session.get("newdor").toString();
            }
            error.clear();
            if (prevDoe.isEmpty() || dob.isEmpty() || newDor.isEmpty()) {
                error.put("alert_error", "Please refresh the page and try again.");
            }

            if (error.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);
                String dateOfBirth = "";
                String dateOfAccountExpiry = "";
                Boolean flag = false;
                String capcode = (String) session.get("captcha");
                System.out.println("form_details.getImgtxt()" + form_details.getImgtxt() + "capcode" + capcode);
                if (!form_details.getImgtxt().equals(capcode)) {
                    error.put("cap_error", "Please enter Correct Captcha");
                    form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                }
                form_details.setDor_email(userdata.getEmail());
                // System.out.println("DATE OF RETIREMENT PREVIOUS" + session.get("dateOfRetirement").toString());
                //  System.out.println("FORM DATA PREVIOUS DATE OF RETIREMENT" + form_details.getP_single_dor());
                if (!form_details.getP_single_dor().equals(prevDoe)) {
                    error.put("p_dor_error", " previous date of retirement can't be changed");
                    flag = true;
                }

                // start, code added by pr on 22ndjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();
                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    flag = true;
                    error.put("csrf_error", "Invalid Security Token !");
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for email activate controller tab 1: " + error);
                System.out.println("error:::::" + error);
                System.out.println("flag::::::::" + flag + "error.get(\"errorMsg1\")" + error.get("errorMsg1"));
                // end, code added by pr on 22ndjan18
                session.put("emailrequest", emailrequest);

                if (emailrequest.equals("dor_ext")) {
                    //if (form_details.getSingle_emp_type().equalsIgnoreCase("emp_regular")) {
                    dateOfBirth = dob;
                    System.out.println("DATE OF BIRTH:::::::" + dateOfBirth);

                    dateOfAccountExpiry = form_details.getSingle_dor();
                    System.out.println("DATE OF Account Expiry:::::::" + dateOfAccountExpiry);

                    String dor_err1 = valid.dorValidationCustom(dateOfAccountExpiry, dateOfBirth);
                    System.out.println("MSSG FROM VALIDATION:::::::" + dor_err1);
                    if (!dor_err1.isEmpty()) {
                        error.put("dor_err1", dor_err1);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                        flag = true;
                    }
                    String dor_err2 = valid.dorValidationCustomForDoR(dateOfAccountExpiry, prevDoe);
                    if (!dor_err2.isEmpty() || !"".equals(dor_err2)) {
                        error.put("dor_err", dor_err2);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                        flag = true;
                    }
                    String dor_err3 = valid.dorValidationCustom(form_details.getSingle_dor());
                    if (!dor_err3.isEmpty() || !"".equals(dor_err3)) {
                        error.put("dor_err", dor_err3);
                        form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                        flag = true;
                    }

//                    } else {
//                        String dor_err = valid.dorValidationCustom(form_details.getSingle_dor());
//                        if (!dor_err.isEmpty() || !"".equals(dor_err)) {
//                            error.put("dor_err", dor_err);
//                            form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
//                            flag = true;
//                        }
//                    }
                    if (form_details.getSingle_emp_type().equals("consultant") || form_details.getSingle_emp_type().equals("emp_contract")) {

                        System.out.println("cert@@@@@@@" + cert + "uploaded file name" + session.get("uploaded_filename"));

                        if (session.get("uploaded_filename") == null) {
                            if (!cert.equals("true")) {
                                flag = true;
                                error.put("file_err", "Please upload work order in PDF format only");
                            }
                        }
                    }

                    if (form_details.getSingle_id_type().isEmpty()) {

                        error.put("id_typeErr", "employee id type not checked");
                        flag = true;
                    }
                    if (form_details.getSingle_emp_type().isEmpty()) {

                        error.put("single_emp_typeErr", "employee type type not checked");
                        flag = true;
                    }

                    if ((!flag) && error.isEmpty()) {

                        hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                        String bo = hodDetails.get("bo").toString();
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                        profile_values.put("bo_check", bo);
                        if (session.get("uploaded_filename") != null) {
                            String uploaded_filename = session.get("uploaded_filename").toString();
                            String renamed_filepath = session.get("renamed_filepath").toString();
                            form_details.setUploaded_filename(uploaded_filename);
                            //form_details.setRenamed_filepath(renamed_filepath);
                        } else {
                            form_details.setUploaded_filename("");
                            form_details.setRenamed_filepath("");
                        }
                    } else {
                        System.out.println("inside elseeeeeeeeeeee");
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }

        return SUCCESS;
    }

    public String dor_ext_tab2() {
        db = new Database();
        UserData userdata = (UserData) session.get("uservalues");

        String prevDoe = "", dob = "", newDor = "";
        if (session.get("dateOfRetirement") != null) {
            prevDoe = session.get("dateOfRetirement").toString();
        }
        if (session.get("dateOfBirth") != null) {
            dob = session.get("dateOfBirth").toString();
        }
        if (session.get("newdor") != null) {
            newDor = session.get("newdor").toString();
        }
        error.clear();
        if (prevDoe.isEmpty() || dob.isEmpty() || newDor.isEmpty()) {
            error.put("alert_error", "Please refresh the page and try again.");
        }

        if (error.isEmpty()) {
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

            try {
                //error.clear();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IN TAB 2");
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);

                form_details.setDor_email(userdata.getEmail());
                form_details.setP_single_dor(prevDoe);
                // boolean pref1_error = valid.EmailValidation(form_details.getDor_email());
                Boolean flag = false;
                String dateOfBirth = "";
                String dateOfAccountExpiry = "";
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                String bo = hodDetails.get("bo").toString();
                underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                String bo_undersec = underSecDetails.get("bo").toString();
                profile_values = (HashMap) session.get("profile-values");
                userdata.getHodData().setGovEmployee(ldap.emailValidate(form_details.getHod_email()));

                form_details.setDor_email(userdata.getEmail());

                dateOfBirth = dob;
                System.out.println("DATE OF BIRTH:::::::" + dateOfBirth);

                dateOfAccountExpiry = form_details.getSingle_dor();
                System.out.println("DATE OF Account Expiry:::::::" + dateOfAccountExpiry);

                String dor_err1 = valid.dorValidationCustom(dateOfAccountExpiry, dateOfBirth);
                System.out.println("MSSG FROM VALIDATION:::::::" + dor_err1);
                if (!dor_err1.isEmpty()) {
                    error.put("dor_err1", dor_err1);
                    form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                    flag = true;
                }
                String dor_err2 = valid.dorValidationCustomForDoR(dateOfAccountExpiry, prevDoe);
                if (!dor_err2.isEmpty() || !"".equals(dor_err2)) {
                    error.put("dor_err", dor_err2);
                    form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                    flag = true;
                }
                String dor_err3 = valid.dorValidationCustom(form_details.getSingle_dor());
                if (!dor_err3.isEmpty() || !"".equals(dor_err3)) {
                    error.put("dor_err", dor_err3);
                    form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                    flag = true;
                }

                if (form_details.getSingle_id_type().isEmpty()) {
                    error.put("id_typeErr", "employee id type not checked");
                    flag = true;
                }

                if (form_details.getSingle_emp_type().isEmpty()) {
                    error.put("single_emp_typeErr", "employee type type not checked");
                    flag = true;
                }

                if (form_details.getSingle_dob().isEmpty()) {
                    System.out.println("dob  is empty");
                    error.put("dob_error", "Date of Birth is required");
                    form_details.setSingle_dob(ESAPI.encoder().encodeForHTML(form_details.getSingle_dob()));
                    flag = true;
                }

                if (!form_details.getP_single_dor().equals(prevDoe)) {
                    error.put("p_dor_error", " previous date of retirement can't change");
                    flag = true;
                }

                if (form_details.getSingle_emp_type().equals("consultant") || form_details.getSingle_emp_type().equals("emp_contract")) {

                    System.out.println("cert@@@@@@@" + cert + "uploaded file name" + session.get("uploaded_filename"));

                    if (session.get("uploaded_filename") == null) {
                        if (!cert.equals("true")) {
                            flag = true;
                            error.put("file_err", "Please upload work order in PDF format only");
                        }
                    }
                }

                if (ldap_employee) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
                } else {
                    if (userdata.getHodData().isGovEmployee()) {
                        profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                        form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
                    }
                }

                setData(userdata.getEmail());
                setWhichemail("1");
                setWhichform("dor_ext2," + "nocheck" + "," + null);

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
                    if (bo.equals("no bo")) {
                        if (ldap_employee) {
                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        } else {
                            if (form_details.getMin().equalsIgnoreCase("Electronics and Information Technology") && (form_details.getDept().contains("NIC") || form_details.getDept().equalsIgnoreCase("National Informatics Centre") || form_details.getDept().equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)"))) {
                                if (hodDetails.get("bo").toString().equals("no bo")) {
                                    error.put("hod_email_error1", "Reporting officer should be govt employee");
                                }
                            }
                            boolean check_mobile = ldap.isMobileInLdap(form_details.getHod_mobile().trim());
                            System.out.println("check_mobile:::::::::::::::::::" + check_mobile);
                            if (check_mobile) {
                                error.put("hod_mobile_error", "Entered mobile number has government email address associated with it.Request you to enter the governmnet email address for the same");
                            }
                        }

                        underSecDetails = (HashMap) profileService.getHODdetails(form_details.getUnder_sec_email());
                        form_details.setUnder_sec_mobile(underSecDetails.get("mobile").toString());

                        if (underSecDetails.get("bo").equals("no bo")) {
                            error.put("under_sec_email_error", "Under Secretary email id should be government email id");

                        } else {

                        }

                        if (bo_undersec.equals("no bo")) {
                            error.put("under_sec_email_error", "Under secretary should be government employee");
                        } else {
                            boolean under_sec_name_error = valid.nameValidation(form_details.getUnder_sec_name());
                            boolean under_sec_mobile_error = valid.MobileValidation(form_details.getUnder_sec_mobile());
                            boolean under_sec_email_error = valid.EmailValidation(form_details.getUnder_sec_email());
                            boolean under_sec_telephone_error = valid.telValidation(form_details.getUnder_sec_tel());
                            System.out.println("under_sec_telephone_error::::::" + under_sec_telephone_error);
                            boolean under_sec_desg_error = valid.addValidation(form_details.getunder_sec_desig());
                            System.out.println("inisde else in validation:::::::::::::");
                            if (under_sec_email_error == true) {
                                error.put("under_sec_email_error", "Enter Under secretary Email [e.g: abc.xyz@zxc.com]");
                            }
                            if (form_details.getUnder_sec_email().equals(form_details.getHod_email())) {
                                error.put("under_sec_email_error", "Under secretary Email address can not be same as of reporting officer email address");
                            }
                            if (under_sec_desg_error == true) {
                                error.put("under_sec_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                            }
                            if (under_sec_name_error == true) {
                                error.put("under_sec_name_error", "Enter Under Secretary  Name [Only characters,dot(.) and whitespaces allowed]");
                            }
                            if (form_details.getUnder_sec_mobile().equals(profile_values.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's can not be same as of user mobile no.");
                            }

                            if (!form_details.getUnder_sec_mobile().equals(underSecDetails.get("mobile"))) {
                                error.put("under_sec_mobile_error", "Under secretary mobile's  no should be same as in ldap");

                            }
                            if (!form_details.getUnder_sec_email().equals(userofficialdata.getUnder_sec_email())) {
                                error.put("under_sec_email_error", "Under secretary mobile's email should be same in our database");

                            }

                            if (form_details.getUnder_sec_mobile().trim().startsWith("+")) {
                                if ((under_sec_mobile_error == true) && (form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary  Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                                } else if ((under_sec_mobile_error == true) && (!form_details.getUnder_sec_mobile().trim().startsWith("+91"))) {
                                    error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number [e.g: +919999999999],[limit 15 digits]");
                                } else {

                                }
                            } else {

                                error.put("under_sec_mobile_error", "Please Enter Under Secretary Mobile Number with country code");
                            }
                            if (under_sec_telephone_error == true) {
                                error.put("under_sec_telephone_error", "Please enter Under Secretary Telephone in correct format");
                            }
                        }
                    } else {

//                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
//                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");
//
//                    }
                        if (nic_employee && nicemployeeeditable.equals("no")) {
                            map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.get("hod_mobile") != null) {
                                form_details.setHod_mobile(map.get("hod_mobile"));
                            }

                            //map = db.fetchUserValuesFromOAD(userdata.getUid());
                            if (map.size() > 0) {
                                if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting officer details can not be changed.");

                                } else {
                                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                        error.put("hod_email_error", "Please change it in your profile and then come back");

                                    }
                                }
                            } else {
                                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                                    error.put("hod_email_error", "Please change it in your profile and then come back");

                                }
                            }
                        }

                    }
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());

                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (form_details.getHod_mobile().equals(profile_values.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of user moile no.");
                    }
                    if (form_details.getHod_mobile().equals(form_details.getUnder_sec_mobile())) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer mobile can not be same as of Under secretary mobile no.");
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
                                // ArrayList b = null;
                                //if (userdata.getAliases() != null && !userdata.getAliases().equals("") && !userdata.getAliases().toString().trim().equals("[]")) {
                                // if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                                //  b = (ArrayList) userdata.getAliases();
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
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                                } else {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                    error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                                }
                                            }
                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }

                                        }
                                    } else {

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
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in single controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in single controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }

                                    }
                                } else {

                                }

                            }
                        }
                    }
                }

                // end validate profile 20th march
                // start, code added by pr on 22ndjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();
                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    flag = true;
                    error.put("csrf_error", "Invalid Security Token !");
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 2: " + error);
                // end, code added by pr on 22ndjan18
                if (getAction_type().equals("submit")) {

                    System.out.println("inside submit::::::::::::in tab2");

                    String dup_exist = "";

                    if (session.get("dupemail") != null) {
                        dup_exist = session.get("dupemail").toString();
                    }
                    System.out.println("dup_exist:::::" + dup_exist);

                    if (flag || !error.isEmpty()) {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    } else {
                        if (form_details.getUnder_sec_email().trim() != null) {
                            profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                            profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                            profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                            profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                            profile_values.put("under_sec_mobile", underSecDetails.get("mobile").toString());

                        } else {

                            profile_values.put("under_sec_email", "");
                            profile_values.put("under_sec_mobile", "");
                            profile_values.put("under_sec_name", "");
                            profile_values.put("under_sec_desig", "");
                            profile_values.put("under_sec_tel", "");
                        }
                        if (session.get("nodomain") != null) {
                            form_details.setAction(session.get("nodomain").toString());
                        } else {
                            form_details.setAction("");
                        }

                        String uploaded_filename = "";
                        String renamed_filepath = "";
                        System.out.println("inside submit::::::::;" + session.get("uploaded_filename"));
                        if (session.get("uploaded_filename") != null) {
                            uploaded_filename = session.get("uploaded_filename").toString();
                            renamed_filepath = session.get("renamed_filepath").toString();
                            // form_details.setUploaded_filename(uploaded_filename);
                            // form_details.setRenamed_filepath(renamed_filepath);
                        } else {
                            uploaded_filename = "";
                            renamed_filepath = "";
                            form_details.setUploaded_filename(uploaded_filename);
                            form_details.setRenamed_filepath(renamed_filepath);
                        }

                        if (session.get("hardware_uploaded_filename") != null) {
                            uploaded_filename = session.get("hardware_uploaded_filename").toString();
                            renamed_filepath = session.get("hardware_renamed_filepath").toString();
                            // form_details.setUploaded_filename(uploaded_filename);
                            //  form_details.setRenamed_filepath(renamed_filepath);
                        }
//                else {
//                    String uploaded_filename = "";
//                    String renamed_filepath = "";
//                    form_details.setUploaded_filename(uploaded_filename);
//                    form_details.setRenamed_filepath(renamed_filepath);
//                }

                        // form_details.setRequest_type(emailReq);
                        String ref_num = emailservice.dor_ext_tab2(form_details, profile_values, "dor_ext", uploaded_filename, renamed_filepath);
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
                                if (form_details.getUnder_sec_email().trim() != null) {
                                    profile_values.put("under_sec_email", form_details.getUnder_sec_email().trim());
                                    profile_values.put("under_sec_name", form_details.getUnder_sec_name().trim());
                                    profile_values.put("under_sec_desig", form_details.getunder_sec_desig());
                                    profile_values.put("under_sec_tel", form_details.getUnder_sec_tel());
                                } else {

                                    profile_values.put("under_sec_email", "");
                                    profile_values.put("under_sec_mobile", "");
                                    profile_values.put("under_sec_name", "");
                                    profile_values.put("under_sec_desig", "");
                                    profile_values.put("under_sec_tel", "");
                                }
                            }
                            //}
                            session.put("dor_ext_details", form_details);
                            session.put("ref_num", ref_num);
                        } else {
                            data = "";
                            request_type = "";
                            whichemail = "";
                            cert = "";
                            whichform = "";
                            CSRFRandom = "";
                            action_type = "";
                        }
                    }
                } else if (getAction_type().equals("validate")) {
                    System.out.println("inside validate::::::::::::in tab2" + error);
                    //  if ((!flag) && error.get("errorMsg1").toString().contains("is available for activation")) {
                    if ((!flag) && error.isEmpty()) {
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

                        if (session.get("uploaded_filename") != null) {
                            String uploaded_filename = session.get("uploaded_filename").toString();
                            String renamed_filepath = session.get("renamed_filepath").toString();
                            // form_details.setUploaded_filename(uploaded_filename);
                            //form_details.setRenamed_filepath(renamed_filepath);
                        } else {
                            form_details.setUploaded_filename("");
                            form_details.setRenamed_filepath("");
                        }

                        if (session.get("hardware_uploaded_filename") != null) {
                            String uploaded_filename = session.get("hardware_uploaded_filename").toString();
                            String renamed_filepath = session.get("hardware_renamed_filepath").toString();
                            // form_details.setUploaded_filename(uploaded_filename);
                            //form_details.setRenamed_filepath(renamed_filepath);
                        }
//               else {
////                    String uploaded_filename = "";
////                    String renamed_filepath = "";
////                    form_details.setUploaded_filename(uploaded_filename);
////                    form_details.setRenamed_filepath(renamed_filepath);
//                }

                    } else {
                        data = "";
                        request_type = "";
                        whichemail = "";
                        cert = "";
                        whichform = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else if (getAction_type().equals("update")) // else if added by pr on 6thfeb18
                {
                    System.out.println("inside update::::::::::::in tab2" + error);
                    if (session.get("uploaded_filename") != null) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    } else {
                        form_details.setUploaded_filename("");
                        form_details.setRenamed_filepath("");
                    }

                    if (session.get("hardware_uploaded_filename") != null) {
                        String uploaded_filename = session.get("hardware_uploaded_filename").toString();
                        String renamed_filepath = session.get("hardware_renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    }
//                else {
//                    String uploaded_filename = "";
//                    String renamed_filepath = "";
//                    form_details.setUploaded_filename(uploaded_filename);
//                    form_details.setRenamed_filepath(renamed_filepath);
//                }

                    if ((!flag) && error.isEmpty()) {
                        System.out.println("inside update::::::::::::in tab2" + error);
                        //if ((!flag && error.get("errorMsg2").toString().contains("is available for creation") && error.get("errorMsg1").toString().contains("is available for creation")) || (!flag && error.get("errorMsg1").toString().contains("This email address is available for creation and this is the new domain") && error.get("errorMsg2").toString().contains("This email address is available for creation and this is the new domain"))) {
                        String ref = session.get("ref").toString();
                        //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                        String admin_role = "NA";
                        if (session.get("admin_role") != null) {
                            admin_role = session.get("admin_role").toString();
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "before update :::::::::");
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
                            // }
                        } else {
                            data = "";
                            CSRFRandom = "";
                            action_type = "";
                        }
                    }
                } else {
                    data = "";
                    request_type = "";
                    whichemail = "";
                    cert = "";
                    whichform = "";
                    CSRFRandom = "";
                    action_type = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for single controller tab 2: " + e.getMessage());
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }
        }
        return SUCCESS;
    }

    public String fetchDor() throws ParseException {

        //boolean email_id_error = valid.EmailValidation(email_id);
        ldap = new Ldap();
        if (!ldap.emailValidate(email_id)) {
            error.put("email_id_error", "email id doesn't exist in ldap.");
            //  session.put("email_id_error", "email id doesn't exist in ldap");
        } else {
            String date = "";
            String dateOfBirth1 = "";

            Map<String, Object> ldapValues = ldap.fetchDorDobDoEValuesForServingEmployees(email_id);
            date = ldapValues.get("doe").toString().toLowerCase();
            dateOfBirth1 = ldapValues.get("dob").toString().toLowerCase();

            if (dateOfBirth1.isEmpty()) {
                error.put("alert_error", "DoB can not be null or empty. Kindly raise a ticket at https://servicedesk@nic.in and get your DoB updated first in our NIC repository.");
            }

            if (error.isEmpty()) {
                if (date.contains("z")) {
                    date = date.replace(date.substring(date.length() - 7), "");
                    Date date1 = new SimpleDateFormat("yyyyMMdd").parse(date);
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formatedDate = newFormat.format(date1);
                    System.out.println("finallllllllll::::" + formatedDate);
                    dateOfRetirement = formatedDate;
                } else {
                    dateOfRetirement = date;
                }

                if (dateOfBirth1.contains("z")) {
                    dateOfBirth1 = dateOfBirth1.replace(dateOfBirth1.substring(dateOfBirth1.length() - 7), "");
                    Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dateOfBirth1);
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formatedDate = newFormat.format(date1);
                    System.out.println("finallllllllll::::" + formatedDate);
                    dateOfBirth = formatedDate;
                } else {
                    dateOfBirth = date;
                }

                System.out.println("finalllllyy222222222 INELSEEEEEE::::" + dateOfBirth);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, 1);
                Date nyear = cal.getTime();

                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime oneYearAfter = nyear.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                String afterOneYearDate = oneYearAfter.format(format);

                LocalDate convertedDate = LocalDate.parse(afterOneYearDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                convertedDate = convertedDate.withDayOfMonth(
                        convertedDate.getMonth().length(convertedDate.isLeapYear()));

                String newDor = convertedDate.format(format);  // add 1 year
                String preDor = dateOfRetirement;
                // String preDor = "20-09-2022";

                String msg = valid.dorValidationCustomServing(newDor, preDor);

                if (!msg.isEmpty()) {
                    error.put("alert_error", msg);
                } else {
                    session.put("dateOfRetirement", dateOfRetirement);
                    session.put("dateOfBirth", dateOfBirth);
                    session.put("newdor", newDor);
                }
            }
        }
        return SUCCESS;
    }

    public String fetchDateOfAccountExpiry() throws ParseException {
        ldap = new Ldap();
        if (!ldap.emailValidate(email_id)) {
            error.put("email_id_error", "email id doesn't exist in ldap.");
        } else {
            String date = "";
            date = fetchDateOfAccountExpiryFromLdap(email_id);
            if (date.contains("Z")) {
                date = date.replace(date.substring(date.length() - 7), "");
                Date date1 = new SimpleDateFormat("yyyyMMdd").parse(date);
                SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                String formatedDate = newFormat.format(date1);
                System.out.println("finallllllllll::::" + formatedDate);
                dateOfRetirement = formatedDate;
                session.put("dateOfRetirement", dateOfRetirement);
            } else {
                dateOfRetirement = date;
                session.put("dateOfRetirement", dateOfRetirement);
            }
        }
        return SUCCESS;
    }

    public String fetchDorFromLdap(String email) {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String dor = "";

        String[] attr = {"nicDateOfRetirement"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020                
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger
                        .getLogger(Ldap.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("nicDateOfRetirement") != null) {
                    String tmp = attrs.get("nicDateOfRetirement").toString();
                    System.out.println("DOR::::::::" + tmp);
                    dor = tmp.substring(21, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return dor;
    }

    public String fetchDateOfAccountExpiryFromLdap(String email) {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String dor = "";

        String[] attr = {"nicAccountExpDate"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020             
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger
                        .getLogger(Ldap.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("nicAccountExpDate") != null) {
                    String tmp = attrs.get("nicAccountExpDate").toString();
                    System.out.println("Date Of Account Expiry::::::::" + tmp);
                    dor = tmp.substring(19, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return dor;
    }

    public String fetchDobFromLdap(String email) throws ParseException {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String dob = "";

        String[] attr = {"nicDateOfBirth"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContext.getLDAPInstance();
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger
                        .getLogger(Ldap.class
                                .getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("nicDateOfBirth") != null) {
                    String tmp = attrs.get("nicDateOfBirth").toString();
                    System.out.println("DOB::::::::" + tmp);
                    dob = tmp.substring(15, tmp.length());
                    System.out.println("sahillllll ::::::::::::" + dob);
                    dob = dob.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dob);
                    if (dob == null || dob.equals("")) {
                        dob = "";
                    }

                    if (dob.contains("Z")) {
                        dob = dob.replace(dob.substring(dob.length() - 7), "");
                        Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dob);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String formatedDate = newFormat.format(date1);
                        System.out.println("finallllllllll::::" + formatedDate);
                        dob = "";
                        dob = formatedDate;

                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return dob;
    }

    public String dor_ext_retired_tab1() {
        Ldap l = new Ldap();
        try {
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata:::::::::::" + userdata);
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("userdata:::::::::::" + userdata);
            profile_values = (HashMap) session.get("profile-values");
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );
            String dateOfBirth = "";
            String dateOfAccountExpiry = "";
            Boolean flag = false;
            String formatedDate = "";
            String capcode = (String) session.get("captcha");
            System.out.println("form_details.getImgtxt()" + form_details.getImgtxt() + "capcode" + capcode);
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            form_details.setDor_email(userdata.getEmail());

            if (!l.emailValidate(userdata.getEmail())) {
                error.put("email_id_error", "email id doesn't exist in ldap.");
            } else {
                String date = "", dor = "";
                date = fetchDateOfAccountExpiryFromLdap(userdata.getEmail());
                dor = fetchDorFromLdap(userdata.getEmail());
                //date = form_details.getP_single_dor();
                if (date.contains("Z")) {
                    date = date.replace(date.substring(date.length() - 7), "");
                    Date date1 = new SimpleDateFormat("yyyyMMdd").parse(date);
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    formatedDate = newFormat.format(date1);
                    System.out.println("finallllllllll::::" + formatedDate);
                    dateOfRetirement = formatedDate;
                    session.put("dateOfRetirement", dateOfRetirement);
                } else {
                    dateOfRetirement = date;
                    session.put("dateOfRetirement", dateOfRetirement);
                }

                if (dor.contains("Z")) {
                    dor = dor.replace(dor.substring(dor.length() - 7), "");
                    Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dor);
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    formatedDate = newFormat.format(date1);
                    session.put("dor", formatedDate);
                } else {
                    session.put("dor", dor);
                }
            }

            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                flag = true;
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for Dor Extention nfor retired Emp controller tab 1: " + error);
            System.out.println("error:::::" + error);
            session.put("emailrequest", emailrequest);

            if (emailrequest.equals("dor_ext")) {
                dateOfAccountExpiry = form_details.getSingle_dor();
                System.out.println("DATE OF Account Expiry:::::::" + dateOfAccountExpiry);

                String dor_err1 = valid.dorValidationCustomRetired(dateOfAccountExpiry, dateOfRetirement);
                System.out.println("MSSG FROM VALIDATION:::::::" + dor_err1);
                if (!dor_err1.isEmpty()) {
                    error.put("dor_err1", dor_err1);
                    form_details.setSingle_dor(ESAPI.encoder().encodeForHTML(form_details.getSingle_dor()));
                    flag = true;
                }
                Set<String> bos = l.fetchBos(userdata.getEmail());
                System.out.println("com.org.controller.Email_registration.dor_ext_retired_tab1() Bos:" + bos.toString());

                boolean isBoAllowed = false;
                for (String bo : bos) {
                    if (bo.equalsIgnoreCase("retiredoficers")) {
                        isBoAllowed = true;
                        break;
                    }
                }
                System.out.println("isBoAllowed :" + isBoAllowed);
                if (!isBoAllowed) {
                    error.put("retired_dn_error", "As per our record, you do not seem to be a retired official!!!");
                    flag = true;
                }
                System.out.println("flag retired :" + flag);
                if (!flag) {
                    form_details.setFixed_dor_for_retired(session.get("dor").toString());
                    form_details.setP_single_dor(dateOfRetirement);
                    form_details.setApplicant_mobile(userdata.getMobile());
                    form_details.setApplicant_name(userdata.getName());
                    form_details.setApplicant_design(userdata.getDesignation());
                    form_details.setApplicant_email(userdata.getEmail());
                    form_details.setSingle_dor(form_details.getSingle_dor());
                    session.put("newdor", form_details.getSingle_dor());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for  controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String dor_ext_retired_tab2() {
        try {
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata:::::::::::" + userdata);
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("userdata:::::::::::" + userdata);
            profile_values = (HashMap) session.get("profile-values");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );
            error.clear();
            String oldDor = session.get("dateOfRetirement").toString();
            String newDor = session.get("newdor").toString();
            String ref_num = emailservice.dor_ext_retired_tab2(userdata, oldDor, newDor);
            if (ref_num != null && !ref_num.isEmpty()) {
                form_details.setP_single_dor(oldDor);
                form_details.setApplicant_mobile(userdata.getMobile());
                form_details.setApplicant_name(userdata.getName());
                form_details.setApplicant_design(userdata.getDesignation());
                form_details.setApplicant_email(userdata.getEmail());
                form_details.setSingle_dor(newDor);
                form_details.setFixed_dor_for_retired(session.get("dor").toString());
                session.put("dor_ext_retired_details", form_details);
                session.put("ref_num", ref_num);
                session.put("uservalues", userdata);
                session.put("profile-values", profile_values);
            } else {
                data = "";
                request_type = "";
                whichemail = "";
                cert = "";
                whichform = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for  controller tab 1: " + e.getMessage());
            data = "";
            request_type = "";
            whichemail = "";
            cert = "";
            whichform = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String shareDaDetailsForRetiredEmployees() {
        //UserData userdata = (UserData) session.get("uservalues");
//        session.put("mobile",userdata.getMobile());
//        session.put("name",userdata.getName());
//        session.put("email",userdata.getEmail());
//        
//        System.out.println("userdata:::::::::::" + userdata);
        Map<String, Object> ldapValues = ldap.fetchDorDobDoEValuesForRetiredEmployees(session.get("email").toString());
        String dob = ldapValues.get("dob").toString();
        String finalDob = "";
        try {
            finalDob = utcFormat(dob);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        session.put("retired_dob", finalDob);
        Set<String> fetchedBo = (Set<String>) session.get("bos");
        for (String bo : fetchedBo) {
            if (!fetchedBo.isEmpty()) {
                session.put("das_coords", db.fetchCoordinatorOrDaEmailByBo(bo));
            } else {
                session.put("das_coords", "");
            }
        }

        return SUCCESS;
    }

    public String fetchBoForEmployeeRetiringThisMonth() {
        UserData userdata = (UserData) session.get("uservalues");
        String email = userdata.getEmail();
        String bo = ldap.fetchBo(email);
        session.put("dor_ext_bo", bo);
        String doeFinal = "", dorFinal = "";
        if (bo.equalsIgnoreCase("retiredoficers")) {
            setBoForUserRetiringThisMonth("retired");
        } else if (bo.equalsIgnoreCase("nic support outsourced")) {
            setBoForUserRetiringThisMonth("outsourced");
        } else {
            Map<String, Object> ldapValues = ldap.fetchDorDobDoEValuesForRetiredEmployees(email);
            String doe = ldapValues.get("doe").toString().toLowerCase();
            String dor = ldapValues.get("dor").toString().toLowerCase();
            try {
                if (doe.contains("z")) {
                    doe = doe.replace(doe.substring(doe.length() - 7), "");
                    Date date1 = new SimpleDateFormat("yyyyMMdd").parse(doe);
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formatedDate = newFormat.format(date1);
                    System.out.println("finallllllllll::::" + formatedDate);
                    doeFinal = formatedDate;
                } else {
                    doeFinal = doe;
                }

                if (dor.contains("z")) {
                    dor = dor.replace(dor.substring(dor.length() - 7), "");
                    Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dor);
                    SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String formatedDate = newFormat.format(date1);
                    System.out.println("finallllllllll::::" + formatedDate);
                    dorFinal = formatedDate;
                } else {
                    dorFinal = dor;
                }

                String preDor = dorFinal;
                // String preDor = "20-09-2022";

                String msg = valid.dorValidationCustomRetiring(preDor);
                if (!msg.isEmpty()) {
                    setBoForUserRetiringThisMonth("error");
                    setErrorMessageRetiring(msg);
                } else {
                    setBoForUserRetiringThisMonth(ldap.freeOrPaid(bo));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        if (getBoForUserRetiringThisMonth().equalsIgnoreCase("paid")) {
            setCoordsOrDas(db.fetchCoordinatorOrDaEmailByBo(bo));
        } else {
            setCoordsOrDas("");
        }
        return SUCCESS;
    }

    public String utcFormat(String date) throws ParseException {

        String modifyDate = "";
        //String formatDate = date.replaceAll("-", "") + "000000Z";
        //String formatDate = date.replaceAll("-", "");
        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyyMMdd");
        String formatedDate = newFormat.format(date1);
        System.out.println("FORMAT::::::" + formatedDate);

        modifyDate = formatedDate + "000000Z";
        return modifyDate;

    }

    public String fetchEmailCampaigns() {
        if (session != null) {
            if (session.get("uservalues") != null) {
                UserData userdata = (UserData) session.get("uservalues");
                Set<String> aliases = userdata.getAliases();
                aliases.add(userdata.getEmail());
                campaigns = emailservice.fetchEmailCampaigns(aliases);
            }
        }
        return SUCCESS;
    }

    public String EmaildiscardCampaign() {  // Added By Manikant pandey
        session.remove("campaign_id");
        session.remove("uploaded_filename");
        session.remove("renamed_filepath");
        EmailbulkDataStr = emailservice.EmaildiscardCampaign(EmailbulkDataId);
        return SUCCESS;
    }

    public String fetchEmailCampaignData() {  // Added By Manikant pandey
        int campaignId = -1;
        campaignId = EmailbulkDataId;
        iCampaignId = EmailbulkDataId;
        session.put("campaign_id", campaignId);
        bulkData = emailservice.fetchBulkUploadData(campaignId);
        Map<String, String> map = (Map<String, String>) bulkData.get("formDetails");
        FormBean form_details = new FormBean();
        form_details.setCampaign_id(campaignId);
        form_details.setReq_(map.get("request_type"));
        form_details.setUploaded_filename(map.get("uploaded_file"));
        form_details.setRenamed_filepath(map.get("renamed_file"));
        form_details.setUser_email(map.get("owner_email"));
        form_details.setUser_name(map.get("owner_name"));
        form_details.setSubmittedAt(map.get("submitted_at"));
        session.put("campaign_id", campaignId);
        session.put("bulk_details", form_details);
        session.put("uploaded_filename", map.get("uploaded_file"));
        session.put("renamed_filepath", map.get("renamed_file"));
        session.put("filetodownload", map.get("renamed_file"));
        session.put("req_", form_details.getReq_());
        return SUCCESS;
    }

    public String fetchCompleteCampaignData() {   // Added By Manikant pandey

        if (session != null && session.get("uservalues") != null) {
            UserData userdata = (UserData) session.get("uservalues");
            Set<String> aliases = userdata.getAliases();
            aliases.add(userdata.getEmail());
            bulkData = emailservice.fetchCompleteCampaignData(aliases);
        }
        return SUCCESS;
    }

    public String bulkDataEdit() {  // Added By Manikant pandey
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        }
        emaildata = emailservice.bulkDataEdit(getEmailbulkDataId(), campaignId);
        session.put("campaign_id", emaildata.getCampaignId());
        return SUCCESS;
    }

    public String bulkDataDelete() {   // Added By Manikant pandey
        emailEditData.setId(EmailbulkDataId);
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        }
        EmailBean emailBean = emailservice.bulkDataEdit(getEmailbulkDataId(), campaignId);
        session.put("campaign_id", emailBean.getCampaignId());
        DbDao dbDao = new DbDao();
        if (emailBean.getRegistration_No() == null || emailBean.getRegistration_No().isEmpty()) {
            if (emailservice.updateBulkEditTable(emailEditData, "", true, false, true, request_type) > 0) {
                bulkData = emailservice.fetchBulkUploadData(campaignId);
            }
        } else if (!dbDao.isRequestClosed(emailBean.getRegistration_No())) {
            switch (emailservice.fetchCountOfSuccessfulRecords((int) session.get("campaign_id"))) {
                case 0:
                    bulkData.clear();
                    bulkData.put("Error", "There is nothing to delete. This request must be rejected.");
                    break;
                case 1:
                    bulkData.clear();
                    bulkData.put("Error", "This is the only record!!! Hence, can not be deleted. You may reject this request.");
                    break;
                default:
                    if (emailservice.updateBulkEditTable(emailBean, "", true, false, true, request_type) > 0) {
                        bulkData = emailservice.fetchBulkUploadData(campaignId);
                    }
                    break;
            }
        } else {
            bulkData = emailservice.fetchBulkUploadData(campaignId);
        }
        return SUCCESS;
    }

    public String bulkEmailDataEditPost() throws IOException {   // Added By Manikant pandey

        if (emailEditData.getErrorMessage().isEmpty()) {
            int campaignId = -1;
            if (session.get("campaign_id") != null) {
                campaignId = (int) session.get("campaign_id");
            } else {
                campaignId = iCampaignId;
                session.put("campaign_id", campaignId);
            }
            FormBean form_detail = emailservice.fetchFormDetails(campaignId);
            UserData userdata = (UserData) session.get("uservalues");
            String error = validateEmailBulkEditData(emailEditData, userdata, form_detail.getFormid(), form_detail.getSingle_id_type(), form_detail.getSingle_emp_type());
            if (!error.isEmpty()) {
                emailEditData.setErrorMessage(error);
                return ERROR;
            }

            if (emailservice.duplicateRecord(emailEditData, campaignId)) {
                EmailBean emailFetchedData = emailservice.bulkDataEdit(emailEditData.getId(), campaignId);
                if (emailFetchedData.getErrorMessage() != null && !emailFetchedData.getErrorMessage().isEmpty()) {
                    emailEditData.setErrorMessage("Duplicate record!!! Kindly delete this.");
                    return ERROR;
                } else {
                    List<Integer> ids = emailservice.fetchIDOfDuplicateRecord(emailEditData, campaignId);
                    if (ids.size() > 1) {
                        emailEditData.setErrorMessage("This record is already there!!!");
                        return ERROR;
                    } else if (ids.size() == 1) {
                        if (ids.get(0) != emailEditData.getId()) {
                            emailEditData.setErrorMessage("This record is already there!!!");
                            return ERROR;
                        }
                    }

                    DbDao dbDao = new DbDao();
                    if (!dbDao.isRequestClosed(emailFetchedData.getRegistration_No())) {
                        emailservice.updateBulkEditTable(emailEditData, "", false, false, false, request_type);
                        bulkData = emailservice.fetchBulkUploadData(campaignId);
                    } else {
                        bulkData = emailservice.fetchBulkUploadData(campaignId);
                    }
                    return SUCCESS;
                }
            } else {
                String errorMessage = "";
                // errorMessage = validateEmailBulkEditData(emailEditData);

                if (errorMessage.isEmpty()) {
                    emailEditData.setErrorMessage("");
                    EmailBean emailBean = emailservice.bulkDataEdit(getEmailbulkDataId(), campaignId);
                    DbDao dbDao = new DbDao();
                    if (!dbDao.isRequestClosed(emailBean.getRegistration_No())) {
                        emailservice.updateBulkEditTable(emailEditData, "", false, false, false, request_type);
                        bulkData = emailservice.fetchBulkUploadData(campaignId);
                    } else {
                        bulkData = emailservice.fetchBulkUploadData(campaignId);
                    }
                    return SUCCESS;

                } else {
                    emailEditData.setErrorMessage(errorMessage);
                    return ERROR;
                }

            }
        } else {
            return ERROR;
        }

    }

    private String validateEmailBulkEditData(EmailBean emailData, UserData userdata, String whichform, String id_type, String emp_type) {  // Added By Manikant pandey
        Map values = new HashMap();
        EmailDao emaildao = new EmailDao();
        String regNumber = "";
        boolean flag = true;
        String errorInString = "";
        int campaignId = iCampaignId;
        session.put("campaign_id", campaignId);

        try {
            if (emailData.getFname() == null) {
                errorInString += "First name can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.Fname(emailData.getFname().toLowerCase().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please Enter First name correct format";
                    flag = false;
                }
            }

            if (emailData.getLname() == null) {
                errorInString += "Last name can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.Lname(emailData.getLname().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please Enter Last name correct format";
                    flag = false;
                }
            }

            if (emailData.getDesignation() == null) {
                errorInString += "Designation can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.Designation(emailData.getDesignation().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please Enter Designation Correct Format.";
                    flag = false;
                }
            }

            if (emailData.getDepartment() == null) {
                errorInString += "Department can not be blank";
                flag = false;
            } else if (emailData.getDepartment() != null && department != null && !emailData.getDepartment().equalsIgnoreCase(department)) {
                errorInString += "Please Enter Department Correct Format and user department and login user department must be same.";
                flag = false;
            } else {
                values = bulkValidate.Department(emailData.getDepartment().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please Enter Department Correct Format.";
                    flag = false;
                }
            }

            if (emailData.getState() == null) {
                errorInString += "State can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.State(emailData.getState().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please Enter State Correct.";
                    flag = false;
                }
            }
            if (emailData.getMobile() == null) {
                errorInString += "Mobile can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.Mobile(emailData.getMobile().trim(), emailData.getCountry_code().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please Enter Mobile number correct Format.";
                    flag = false;
                }
            }

            if (emailData.getDor() == null) {
                errorInString += "Date of Retirement can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.DOR(emailData.getDor().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please select Date of Retairment in correct format.";
                    flag = false;
                }
            }

            if (emailData.getMail() == null) {
                errorInString += "Email address can not be blank.";
                flag = false;
            } else {

                Map profile = new HashMap();
                profile.put("dept", emailData.getDepartment().toLowerCase());
                profile.put("emp_type", emp_type);
                profile.put("form_type", whichform);
                profile.put("id_type", id_type);
                profile.put("user_employment", userdata.getUserOfficialData().getEmployment());
                profile.put("stateCode", userdata.getUserOfficialData().getState());
                profile.put("department", userdata.getUserOfficialData().getDepartment());
                profile.put("min", userdata.getUserOfficialData().getMinistry());
                profile.put("Org", userdata.getUserOfficialData().getOrganizationCategory());
                Set<String> domain = null;
                //Set<String> allowedDomains = null;
//                if (session.get("allowed_domains") != null) {
//                    allowedDomains = (Set<String>) session.get("allowed_domains");

                values = bulkValidate.Mail(emailData.getMail().trim(), emailData.getUid().trim(), emailservice.getDomain(profile));
                if (values.get("valid").equals("false")) {
                    errorInString += (values.get("errorMsg").toString());
                    flag = false;
                } //} 
                else {
                    Map<String, String> user = emaildao.fetchProfileFromBaseTable(regNumber);
                    domain = emailservice.getDomain(user);
                    values = bulkValidate.Mail(emailData.getMail().trim(), emailData.getUid().trim(), domain);
                    if (values.get("valid").equals("false")) {
                        errorInString += (values.get("errorMsg").toString());
                        flag = false;
                    }
                }

            }
            if (emailData.getUid() == null) {
                errorInString += "Email id can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.Uid(emailData.getUid(), id_type);
                if (values.get("valid").equals("true")) {
                } else {
                    errorInString += "Please Enter Email id Correct format.";
                    flag = false;
                }
            }
            if (emailData.getDor() == null) {
                errorInString += "Date of Retirement can not be blank.";
                flag = false;
            } else {
                values = bulkValidate.DOR(emailData.getDor().trim());
                if (values.get("valid").equals("false")) {
                    errorInString += "Please select Date of Retairment in correct format.";
                    flag = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");

        return errorInString;
    }

    public Map validateBulkCSVFile(String renamed_filepath, String whichform, String id_type, String emp_type, String uploaded_filename, UserData userdata, String department) {  // Added By Manikant pandey
        System.out.println("id_type::::::::" + id_type);
        EmailDao emaildao = new EmailDao();
        ///String errorInString = "";
        Map ExcelValidate = new HashMap();
        BulkValidation bulkValidate = new BulkValidation();
        EmailService bulkEmailService = new EmailService();
        Map values = new HashMap();
        //int campaignId = -1;
        ArrayList nodomain = new ArrayList();
        ArrayList validUsers = new ArrayList();
        ArrayList notvalidUsers = new ArrayList();
        ArrayList errorInString = new ArrayList();
        try {

            Set<EmailBean> emaildata = null;
            if (session.get("bulkFileContent") != null) {
                emaildata = (Set<EmailBean>) session.get("bulkFileContent");
            } else {
                emaildata = new HashSet<>();
            }

            System.out.println("EmailData ::: " + emaildata);

            if (emaildata.size() <= 0) {
                ExcelValidate.put("errorMessage", "Please enter domain or Upload valid file. You can not keep it empty!!!");
            } else {
                boolean flag = true, originalFlag = true;
                Random rand = new Random();
                int random = rand.nextInt(1000000000);
                int campaignId = emaildao.addEmailCampaign(random, userdata, renamed_filepath, uploaded_filename, whichform, id_type, emp_type);
                if (campaignId != -1) {
                    iCampaignId = campaignId;
                    session.put("campaign_id", campaignId);
                    for (EmailBean formbean : emaildata) {
                        //errorInString = "";
                        flag = true;
                        originalFlag = true;
                        if (formbean.getFname() == null) {
                            errorInString.add("First name can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.Fname(formbean.getFname().toLowerCase().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter First name correct format");
                                flag = false;
                            }
                        }

                        if (formbean.getLname() == null) {
                            errorInString.add("Last name can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.Lname(formbean.getLname().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter Last name correct format");
                                flag = false;
                            }
                        }

                        if (formbean.getDesignation() == null) {
                            errorInString.add("Designation can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.Designation(formbean.getDesignation().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter Designation Correct Format.");
                                flag = false;
                            }
                        }

                        if (formbean.getDepartment() == null) {
                            errorInString.add("Department can not be blank");
                            flag = false;
                        } else if (formbean.getDepartment() != null && department != null && !formbean.getDepartment().equalsIgnoreCase(department)) {
                            errorInString.add("Please Enter Department Correct Format and user department and login user department must be same.");
                            flag = false;
                        } else {
                            values = bulkValidate.Department(formbean.getDepartment().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter Department Correct Format.");
                                flag = false;
                            }
                        }

                        if (formbean.getState() == null) {
                            errorInString.add("State can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.State(formbean.getState().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter State Correct.");
                                flag = false;
                            }
                        }
                        if (formbean.getCountry_code() == null) {
                            errorInString.add("Country code can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.countrycode(formbean.getCountry_code().trim());
                            if (values.get("valid").equals("true")) {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "last name ok");
                            } else {
                                errorInString.add("Please Enter Country code correct Format. [e.g  +91 ]");
                                flag = false;
                            }
                        }

                        if (formbean.getMobile() == null) {
                            errorInString.add("Mobile can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.Mobile(formbean.getMobile().trim(), formbean.getCountry_code().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter Mobile number correct Format.");
                                flag = false;
                            }
                        }

                        if (formbean.getDor() == null) {
                            errorInString.add("Date of Retirement can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.DOR(formbean.getDor().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please select Date of Retairment in correct format.");
                                flag = false;
                            }
                        }
                        if (formbean.getUid() == null) {
                            errorInString.add("Email id can not be blank.");
                            flag = false;
                        } else {
                            values = bulkValidate.Uid(formbean.getUid().trim(), id_type);
                            if (values.get("valid").equals("true")) {
                            } else {
                                errorInString.add("Please Enter Email id Correct format.");
                                flag = false;
                            }
                        }

                        if (formbean.getMail() == null) {
                            errorInString.add("Email address can not be blank.");
                            flag = false;
                        } else {
                            Map profile = new HashMap();
                            profile.put("dept", formbean.getDepartment().toLowerCase());
                            profile.put("emp_type", emp_type);
                            profile.put("form_type", whichform);
                            profile.put("id_type", id_type);
                            profile.put("user_employment", userdata.getUserOfficialData().getEmployment());
                            profile.put("stateCode", userdata.getUserOfficialData().getState());
                            profile.put("department", userdata.getUserOfficialData().getDepartment());
                            profile.put("min", userdata.getUserOfficialData().getMinistry());
                            profile.put("Org", userdata.getUserOfficialData().getOrganizationCategory());
                            Set<String> allowedDomains = bulkEmailService.getDomain(profile);
                            session.put("allowed_domains", allowedDomains);
                            values = bulkValidate.Mail(formbean.getMail().trim(), formbean.getUid().trim(), allowedDomains);
                            if (values.get("valid").equals("false")) {
                                errorInString.add(values.get("errorMsg").toString());
                                flag = false;
                            }
                        }
                        if (formbean.getDob() != null && !formbean.getDob().equals("")) {
                            values = bulkValidate.DOB(formbean.getDob().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please select Date of Birth in correct format.");
                                flag = false;
                            }
                        }

                        if (formbean.getEmpcode() != null && !formbean.getEmpcode().equals("")) {
                            values = bulkValidate.EMPNUMBER(formbean.getEmpcode().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString.add("Please Enter Employee Code in correct format.");
                                flag = false;
                            }
                        }
                        HashMap hm = new LinkedHashMap();
                        hm.clear();
                        hm.put("First Name", formbean.getFname());
                        hm.put("Last Name", formbean.getLname());
                        hm.put("Designation", formbean.getDesignation());
                        hm.put("Posting State", formbean.getDepartment());
                        hm.put("State", formbean.getState());
                        hm.put("Country Code", formbean.getCountry_code());
                        hm.put("Mobile(10 digit)", formbean.getMobile());
                        hm.put("Date of Retirement(dd-mm-yyyy)", formbean.getDor());
                        hm.put("Login UID", formbean.getUid());
                        hm.put("Email address", formbean.getMail());
                        hm.put("Date of Birth(dd-mm-yyyy)", formbean.getDob());
                        hm.put("Employee Code", formbean.getEmpcode());
                        if (flag) {   // the row is ok with no error
                            validUsers.add(hm);
                            emaildao.addValidRecordsInBulkTable(formbean, campaignId, "email_bulk", Constants.BULK_USER);
                        } else {
                            notvalidUsers.add(hm);
                            emaildao.addInvalidRecordsInBulkTable(formbean, errorInString, campaignId, "email_bulk", Constants.BULK_USER);
                        }
                    }
                } else {
                    ExcelValidate.put("errorMessage", "Campaign could not be generated!!!");
                }
                ExcelValidate.put("nodomain", nodomain);
                ExcelValidate.put("validUsers", validUsers);
                ExcelValidate.put("notvalidUsers", notvalidUsers);
                ExcelValidate.put("errorInString", errorInString);

                if (session.get("fileContent") != null) {
                    session.remove("fileContent");
                }
                if (session.get("populatedList") != null) {
                    session.remove("populatedList");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ExcelValidate;
    }

}
