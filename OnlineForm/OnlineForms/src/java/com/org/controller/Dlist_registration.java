package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.ModerateBean;
import com.org.bean.OwnerBean;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.dao.esignDao;
import com.org.service.DlistService;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import entities.LdapQuery;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import utility.ExcelCreator;
import validation.validation;

public class Dlist_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    Map session;
    DlistService dlistservice = new DlistService();
    UpdateService updateService = new UpdateService();// line added by pr on 1stfeb18
    String cert, whichform;
    esignDao esignDao = null;
    Map<String, Object> error = new HashMap<>();
    Map<String, Object> preview = new HashMap<>();
    validation valid = new validation();
    Map profile_values = new HashMap();
    String data, CSRFRandom, action_type, returnString, request_type, whichemail; // CSRFRandom added by pr on 22ndjan18
    public Map<Object, Object> dlistbulkData = new HashMap<>();
    ProfileService profileService = new ProfileService();
    Map hodDetails = new HashMap();
    private Database db = null;
    Map<String, String> map = new HashMap<>();
    Map<Object, Object> bulkData = new HashMap<>();
    Map<String, Object> bulkData1 = new HashMap<>();
    Map<String, Object> moderatorMap = new HashMap<>();
    Map<String, Object> ownerMap = new HashMap<>();
    Set<ModerateBean> set = new HashSet<>();
    Set<OwnerBean> set1 = new HashSet<>();
    public ModerateBean dlistData = new ModerateBean();
    public OwnerBean dlistData1 = new OwnerBean();
    private List<ModerateBean> moderateVal = new ArrayList<>();
    private static int testCount = 0;
    public int bulkDataId = 0;
    public String type;
    public String user_file;

    public ArrayList cellRecords;
    public ArrayList errorList;
    public ArrayList nonvalid;
    Ldap ldap;

    public Dlist_registration() {
        dlistservice = new DlistService();
        updateService = new UpdateService();
        error = new HashMap<>();
        preview = new HashMap<>();
        valid = new validation();
        dlistData = new ModerateBean();
        dlistData1 = new OwnerBean();
        ldap = new Ldap();
        //   bulkData = new HashMap<>();

        moderatorMap = new HashMap<>();
        ownerMap = new HashMap<>();
        set = new HashSet<ModerateBean>();
        set1 = new HashSet<OwnerBean>();
        bulkData1 = new HashMap<>();
        profile_values = new HashMap();
        profileService = new ProfileService();
        hodDetails = new HashMap();
        map = new HashMap<>();

        if (dlistservice == null) {
            dlistservice = new DlistService();
        }
        if (updateService == null) {
            updateService = new UpdateService();
        }
        if (error == null) {
            error = new HashMap<>();
        }
        if (preview == null) {
            preview = new HashMap<>();
        }
        if (valid == null) {
            valid = new validation();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
        if (profileService == null) {
            profileService = new ProfileService();
        }
        if (hodDetails == null) {
            hodDetails = new HashMap();
        }
        if (map == null) {
            map = new HashMap<>();
        }
        if (dlistData == null) {
            dlistData = new ModerateBean();
        }
        if (dlistData1 == null) {
            dlistData1 = new OwnerBean();
        }

        if (bulkData == null) {
            bulkData = new HashMap<>();
        }
        if (bulkData1 == null) {
            bulkData1 = new HashMap<>();
        }
        if (moderatorMap == null) {
            moderatorMap = new HashMap<>();
        }
        if (ownerMap == null) {
            ownerMap = new HashMap<>();
        }
        if (set == null) {
            set = new HashSet<>();
        }
        if (set1 == null) {
            set1 = new HashSet<>();
        }
    }

    public Map<String, Object> getOwnerMap() {
        return ownerMap;
    }

    public void setOwnerMap(Map<String, Object> ownerMap) {
        this.ownerMap = ownerMap;
    }

    public int getBulkDataId() {
        return bulkDataId;
    }

    public void setBulkDataId(int bulkDataId) {
        this.bulkDataId = bulkDataId;
    }

    public static int getTestCount() {
        return testCount;
    }

    public static void setTestCount(int testCount) {
        Dlist_registration.testCount = testCount;
    }

    public List<ModerateBean> getModerateVal() {
        return moderateVal;
    }

    public void setModerateVal(List<ModerateBean> moderateVal) {
        this.moderateVal = moderateVal;
    }

    public Map<String, Object> getBulkData1() {
        return bulkData1;
    }

    public Map<String, Object> getPreview() {
        return preview;
    }

    public void setPreview(Map<String, Object> preview) {
        this.preview = preview;
    }

    public void setBulkData1(Map<String, Object> bulkData1) {
        this.bulkData1 = bulkData1;
    }

    public Map<String, Object> getModeratorMap() {
        return moderatorMap;
    }

    public void setModeratorMap(Map<String, Object> moderatorMap) {
        this.moderatorMap = moderatorMap;
    }

    public Set<ModerateBean> getSet() {
        return set;
    }

    public void setSet(Set<ModerateBean> set) {
        this.set = set;
    }

    public Set<OwnerBean> getSet1() {
        return set1;
    }

    public void setSet1(Set<OwnerBean> set1) {
        this.set1 = set1;
    }

//    public DlistModeratorBean getDlistData() {
//        return dlistData;
//    }
//
//    public void setDlistData(DlistModeratorBean dlistData) {
//        this.dlistData = dlistData;
//    }
    public ModerateBean getDlistData() {
        return dlistData;
    }

    public void setDlistData(ModerateBean dlistData) {
        this.dlistData = dlistData;
    }

    public OwnerBean getDlistData1() {
        return dlistData1;
    }

    public void setDlistData1(OwnerBean dlistData1) {
        this.dlistData1 = dlistData1;
    }

//    public DlistOwnerBean getDlistData1() {
//        return dlistData1;
//    }
//
//    public void setDlistData1(DlistOwnerBean dlistData1) {
//        this.dlistData1 = dlistData1;
//    }
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

    public Map<Object, Object> getBulkData() {
        return bulkData;
    }

    public void setBulkData(Map<Object, Object> bulkData) {
        this.bulkData = bulkData;
    }

    public String DLIST_tab1() {
        // validation
        error.clear();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            String list_name_error = valid.listValidation(form_details.getList_name());
            boolean list_desc_error = valid.dnstxtValidation(form_details.getDescription_list());
            boolean list_mod_error = valid.checkradioValidation(form_details.getList_mod());
            boolean list_allowed_error = valid.checkradioValidation(form_details.getAllowed_member());
            boolean list_temp_error = valid.checkradioValidation(form_details.getList_temp());
            boolean list_nonnicnet_error = valid.checkradioValidation(form_details.getNon_nicnet());
            boolean total_member_err = valid.webcastamountvalidation(form_details.getMemberCount());
            String list_validdate_error = valid.listtempValidation(form_details.getList_temp(), form_details.getValidity_date());
//            String s = t.audit("sampark.gov.in");
            if (!list_name_error.isEmpty() || !"".equals(list_name_error)) {
                error.put("list_name_error", "E.g: abc.def@lsmgr.nic.in [dot(.) or hyphen(-) with 6-20 characters in list name]");
                form_details.setList_name(ESAPI.encoder().encodeForHTML(form_details.getList_name()));
            }
            if (list_desc_error) {
                error.put("list_desc_error", "Enter Description of List  [characters,dot(.) and whitespace]");
                form_details.setDescription_list(ESAPI.encoder().encodeForHTML(form_details.getDescription_list()));
            }
            if (list_mod_error) {
                error.put("list_mod_error", "Please select, Will the List be moderated");
                form_details.setList_mod(ESAPI.encoder().encodeForHTML(form_details.getList_mod()));
            }
            if (list_allowed_error) {
                error.put("list_allowed_error", "Please select, Are only members allowed to send mails to the list ");
                form_details.setAllowed_member(ESAPI.encoder().encodeForHTML(form_details.getAllowed_member()));
            }
            if (list_temp_error) {
                error.put("list_temp_error", "Please select, Is list temporary");
                form_details.setList_temp(ESAPI.encoder().encodeForHTML(form_details.getList_temp()));
            }
            if (list_nonnicnet_error) {
                error.put("list_nonnicnet_error", "Please select, Will list accept mail from a non-NICNET email address");
                form_details.setNon_nicnet(ESAPI.encoder().encodeForHTML(form_details.getNon_nicnet()));
            }
            if (list_validdate_error.isEmpty() || "".equals(list_validdate_error)) {
            } else {
                error.put("list_validdate_error", list_validdate_error);
                form_details.setValidity_date(ESAPI.encoder().encodeForHTML(form_details.getValidity_date()));
            }
            if (form_details.getMemberCount().isEmpty()) {
                error.put("total_member_err", "Please enter total number of members");
                form_details.setMailsent(ESAPI.encoder().encodeForHTML(form_details.getMemberCount()));
            }

            if (total_member_err == true) {
                error.put("total_member_err", "Please enter total number of members");
                form_details.setMailsent(ESAPI.encoder().encodeForHTML(form_details.getMemberCount()));
            }
            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // end, code added by pr on 22ndjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR in dlist controller tab 1: " + error);
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    int generated_key = dlistservice.DLIST_tab1(form_details);
                    session.put("gen_key", generated_key);
                    session.put("dlist_details", form_details);
                }
            } else {
                data = "";
                returnString = "";
                CSRFRandom = "";
                action_type = "";
            }
            if (session.get("moderatorListData") != null) {
                session.remove("moderatorListData");
            }
            if (session.get("ownerListData") != null) {
                session.remove("ownerListData");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for dlist controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            returnString = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String DLIST_tab2() {
        try {

            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            error.clear();
            form_details = (FormBean) session.get("dlist_details");
            System.out.println("Dlist_DETAILS:::::" + form_details);
            Set<ModerateBean> set = new HashSet<>();
            Set<OwnerBean> set4 = new HashSet<>();
            bulkData.put("moderatorData", session.get("moderatorListData"));
            bulkData.put("ownerData", session.get("ownerListData"));

            if (bulkData.get("moderatorData") == null && bulkData.get("ownerData") == null) {
                error.put("ModeratorData", "please add at least 1 record");
            }

            if (bulkData.get("moderatorData") == null) {
                preview.put("moderatorHide", "moderator data not recorded");

            }
            if (bulkData.get("ownerData") == null) {
                preview.put("ownerHide", "owner data not recorded");

            }

            System.out.println("FORM-DETAILS FOUND SET::::" + form_details.getT_off_name());
            System.out.println("FORM-DETAILS FOUND SET1111::::" + form_details.getTauth_email());

            String list_name, description_list, list_mod, allowed_member, list_temp, validity_date, non_nicnet, memberCount;
            list_name = form_details.getList_name();
            description_list = form_details.getDescription_list();
            list_mod = form_details.getList_mod();
            allowed_member = form_details.getAllowed_member();
            list_temp = form_details.getList_temp();
            validity_date = form_details.getValidity_date();
            non_nicnet = form_details.getNon_nicnet();
            memberCount = form_details.getMemberCount();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);

            String capcode = (String) session.get("captcha");
            System.out.println("session in captcha" + capcode + ":::::::captch in form_details:::::" + form_details.getImgtxt());
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR in dlist controller tab 2: " + error);
            if (error.isEmpty()) {
                form_details.setList_name(list_name);
                form_details.setDescription_list(description_list);
                form_details.setList_mod(list_mod);
                form_details.setAllowed_member(allowed_member);
                form_details.setList_temp(list_temp);
                form_details.setValidity_date(validity_date);
                form_details.setNon_nicnet(non_nicnet);
                form_details.setMemberCount(memberCount);
                if (session.get("update_without_oldmobile").equals("no")) {
                    session.put("dlist_details", form_details);
                    profile_values = (HashMap) session.get("profile-values");
                    String generated_key = session.get("gen_key").toString();
                    dlistservice.DLIST_tab2(profile_values, form_details, generated_key);
                    session.put("gen_key", generated_key);
                }
            } else {
                data = "";
                returnString = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for dlist controller tab 2: " + e.getMessage());
            e.printStackTrace();
            data = "";
            returnString = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String fill_dlist_moderator_tab() {
        try {
            profile_values = (HashMap) session.get("profile-values");
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for dlsit controller tab fill moderator: " + e.getMessage());
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String fill_dlist_owner_tab() {
        try {
            profile_values = (HashMap) session.get("profile-values");
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for dlsit controller tab fill Owner: " + e.getMessage());
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String Bulk_tab1() {
        try {
            System.out.println("Inside bulk tab1");
            String returnString = "";
            UserData userdata = (UserData) session.get("uservalues");
            System.out.println("userdata.getEmail()" + userdata.getEmail());
            System.out.println("userdata.isIsEmailValidated()" + userdata.isIsEmailValidated());
            if (userdata.isIsEmailValidated()) {

                error.clear();
                String capcode = (String) session.get("captcha");
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);

                if (user_file == null || user_file == "" || user_file.equals("")) {
                    error.put("file_err", "Please upload a file");
                }

                String capcode1 = (String) session.get("captcha");
                if (form_details.getImgtxt() != null && !form_details.getImgtxt().equals(capcode1)) {
                    error.put("cap_error", "Please enter Correct Captcha");
                    form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
                }
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

                        Map ExcelValidate = (HashMap) dlistservice.validateBulkExcelFile(renamed_filepath, "bulk", userdata);
                        System.out.println("Data Of File Are :" + ExcelValidate);
                        profile_values = (HashMap) session.get("profile-values");
                        System.out.println("Data Of File Are :" + form_details);
                        if (ExcelValidate.containsKey("errorMessage")) {
                            error.put("file_err", ExcelValidate.get("errorMessage").toString());
                        } else {
                            setCellRecords((ArrayList) ExcelValidate.get("cellRecords"));
                            setErrorList((ArrayList) ExcelValidate.get("errorList"));
                            nonvalid = (ArrayList) ExcelValidate.get("nonvalid");
                            HashMap hm = null;
                            ArrayList errorlist = new ArrayList();

                            for (Iterator it = getErrorList().iterator(); it.hasNext();) {
                                hm = new LinkedHashMap();
                                Object e = it.next();
                                hm.put("ERROR LIST ", e.toString());
                                errorlist.add(hm);
                            }
                            // String refFilename = renamed_filepath.substring(0, renamed_filepath.indexOf("."));
                            String refFilename = renamed_filepath.substring(0, renamed_filepath.length() - 4);
                            ExcelCreator excelCreator = new ExcelCreator();
                            HashMap a = new HashMap();
                            a.put("Details", "NO DATA FOUND");

                            Boolean err = false;
                            if (getCellRecords().size() > 0) {
                                excelCreator.createWorkbookHash(getCellRecords(), refFilename + "-valid.xls", "Success list");
                            } else {
                                getCellRecords().add(a);
                                err = true;
                                excelCreator.createWorkbookHash(getCellRecords(), refFilename + "-valid.xls", "Users which can be created");
                            }
                            if (errorlist.size() > 0) {
                                excelCreator.createWorkbookHash(errorlist, refFilename + "-error.xls", "Error list");
                            } else {
                                errorlist.add(a);
                                excelCreator.createWorkbookHash(errorlist, refFilename + "-error.xls", "Error list");
                            }
                            if (nonvalid.size() > 0) {
                                excelCreator.createWorkbookHash(nonvalid, refFilename + "-notvalid.xls", "Non Valid User");
                            } else {
                                nonvalid.add(a);
                                excelCreator.createWorkbookHash(nonvalid, refFilename + "-notvalid.xls", "Non Valid User");
                            }

                            HashMap cellRecords1 = new HashMap();
                            cellRecords1.put("ExcelFileData", getCellRecords());
                            session.put("ExcelFileData", cellRecords1);
                            session.put("filetodownload", refFilename);
                            session.put("dlist_details", form_details);
                            System.out.println("Data Of Session Are :" + session);

                            if (err) {

                                session.put("error_file", "No valid users, check Error file");
                            } else {
                                if (session.get("error_file") != null) {
                                    session.remove("error_file");
                                }
                                // error.put("file_err", "");
                            }
                        }
                    }
                }
            } else {
                error.put("dlist_info_error", "You may register only for the following services:Dlist Service,VPN Service,Security Audit Service,e-Sampark Service,Cloud Service,Domain Registration Service,Firewall Service,Reservation for video conferencing Service,Web Application Firewall services.To register for other services, please log in with your government email service(NIC) email address");
            }

        } catch (Exception e) {
            e.printStackTrace();
            error.put("error_file", "Please enter data in correct format");
            session.put("error_file", "Please enter data in correct format");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for bulk controller tab 1: " + e.getMessage());
            data = "";
            whichform = "";
            CSRFRandom = "";
        }
        return SUCCESS;
    }

    public String Dlist_Bulk_tab2() {

        try {
            error.clear();
            if (session.get("error_file") != null) {
                error.put("error_file", session.get("error_file").toString());
            }

            if (error.isEmpty()) {
                form_details = (FormBean) session.get("dlist_details");

                String renamed_uploaded_filename = form_details.getRenamed_filepath();

                String str = renamed_uploaded_filename;

                String onlyName = str.replace(".xls", "");

                onlyName = onlyName.trim();

                session.put("valid_filepath", onlyName + "-valid.xls");

                session.put("error_filepath", onlyName + "-error.xls");

                session.put("notvalid_filepath", onlyName + "-notvalid.xls");

                System.out.println(" renamed_uploaded_filename value is " + renamed_uploaded_filename);
                profile_values = (HashMap) session.get("profile-values");
                System.out.println("profile_values:::::::::" + profile_values);
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

    public String DLIST_Bulktab() {
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
            hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }
            dlistbulkData.put("excelFileData", session.get("ExcelFileData"));
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
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
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

                                if (nic_employee && nicemployeeeditable.equals("no")) {

                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");

                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }

                                    }
                                } else {

                                }
                            }
                        } else {

                            if (nic_employee && nicemployeeeditable.equals("no")) {

                                map = db.fetchUserValuesFromOAD(userdata.getUid());
                                if (map.size() > 0) {
                                    if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                        error.put("hod_email_error", "Reporting officer details can not be changed.");

                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                        error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                    }
                                }
                            } else {

                            }
                        }
                    }
                }
                if (ldap_employee) {
                    //hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                    System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                    if (hodDetails.get("bo").toString().equals("no bo")) {
                        error.put("hod_email_error", "Reporting officer should be govt employee");
                    }
                }
            }
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR in dlist controller tab 3: " + error);
            if (getAction_type().equals("submit")) {
                if (!error.isEmpty()) {
                    data = "";
                    returnString = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    // dlist bulk request jan 2021
                    if (session.get("update_without_oldmobile").equals("no")) {
                        String ref_num = dlistservice.DLIST_Bulktab3(form_details, profile_values, dlistbulkData);
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
                                //profile_values.put("ca_mobile", form_details.getHod_mobile().trim());
                                profile_values.put("ca_email", form_details.getHod_email().trim());
                                profile_values.put("hod_name", form_details.getHod_name().trim());
                                profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                profile_values.put("hod_email", form_details.getHod_email().trim());
                                profile_values.put("hod_tel", form_details.getHod_tel().trim());
                            }
                        }
                        session.put("dlist_details", form_details);
                        session.put("ref_num", ref_num);
                    }
                }
            } else if (getAction_type().equals("validate")) {  // june 2021 for edit before submission
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
            } else if (getAction_type().equals("update")) {
                if (error.isEmpty()) {
                    String ref = session.get("ref").toString();
                    String admin_role = "NA";
                    if (session.get("admin_role") != null) {
                        admin_role = session.get("admin_role").toString();
                    }
                    Boolean flag = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                    if (flag && form_details.getModule().equals("user")) {
                        profile_values = (HashMap) session.get("profile-values");
                        form_details.setHod_email(profile_values.get("hod_email").toString());
                        form_details.setHod_name(profile_values.get("hod_name").toString());
                        //form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
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
                returnString = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for dlist controller tab 3: " + e.getMessage());
            e.printStackTrace();
            data = "";
            returnString = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String DLIST_tab3() {
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
            hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
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

            bulkData.put("moderatorData", session.get("moderatorListData"));
            bulkData.put("ownerData", session.get("ownerListData"));
            String list_name_error = null;
            boolean list_desc_error = false;
            boolean list_mod_error = false;
            boolean list_allowed_error = false;
            boolean list_temp_error = false;
            boolean list_nonnicnet_error = false;
            String list_validdate_error = null;

            if (form_details.getList_name() != null) {
                list_name_error = valid.listValidation(form_details.getList_name());
            }
            if (form_details.getDescription_list() != null) {
                list_desc_error = valid.dnstxtValidation(form_details.getDescription_list());
            }
            if (form_details.getList_mod() != null) {
                list_mod_error = valid.checkradioValidation(form_details.getList_mod());
            }
            if (form_details.getAllowed_member() != null) {
                list_allowed_error = valid.checkradioValidation(form_details.getAllowed_member());
            }
            if (form_details.getList_temp() != null) {
                list_temp_error = valid.checkradioValidation(form_details.getList_temp());
            }
            if (form_details.getNon_nicnet() != null) {
                list_nonnicnet_error = valid.checkradioValidation(form_details.getNon_nicnet());
            }

            if (form_details.getList_temp() != null) {
                list_validdate_error = valid.listtempValidation(form_details.getList_temp(), form_details.getValidity_date());
            }

            if (list_name_error != null && (!list_name_error.isEmpty() || !"".equals(list_name_error))) {
                error.put("list_name_error", "Enter Name of the list [characters,dot(.),hiphen(-) and length 6,20]");
                form_details.setList_name(ESAPI.encoder().encodeForHTML(form_details.getList_name()));
            }
            if (list_desc_error) {
                error.put("list_desc_error", "Enter Description of List  [characters,dot(.) and whitespace]");
                form_details.setDescription_list(ESAPI.encoder().encodeForHTML(form_details.getDescription_list()));
            }
            if (list_mod_error) {
                error.put("list_mod_error", "Please select, Will the List be moderated");
                form_details.setList_mod(ESAPI.encoder().encodeForHTML(form_details.getList_mod()));
            }
            if (list_allowed_error) {
                error.put("list_allowed_error", "Please select, Are only members allowed to send mails to the list ");
                form_details.setAllowed_member(ESAPI.encoder().encodeForHTML(form_details.getAllowed_member()));
            }
            if (list_temp_error) {
                error.put("list_temp_error", "Please select, Is list temporary");
                form_details.setList_temp(ESAPI.encoder().encodeForHTML(form_details.getList_temp()));
            }
            if (list_nonnicnet_error) {
                error.put("list_nonnicnet_error", "Please select, Will list accept mail from a non-NICNET email address");
                form_details.setNon_nicnet(ESAPI.encoder().encodeForHTML(form_details.getNon_nicnet()));
            }
            if (list_validdate_error != null && list_validdate_error.equals("") && form_details.getList_temp().equals("yes")) {
                error.put("list_validdate_error", list_validdate_error);
                form_details.setValidity_date(ESAPI.encoder().encodeForHTML(form_details.getValidity_date()));
            }
//            else {
//                error.put("list_validdate_error", list_validdate_error);
//                form_details.setValidity_date(ESAPI.encoder().encodeForHTML(form_details.getValidity_date()));
//            }
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
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
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
                            //if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
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
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
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
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
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
                    //hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                    System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                    if (hodDetails.get("bo").toString().equals("no bo")) {
                        error.put("hod_email_error", "Reporting officer should be govt employee");
                    }
                }
            }
            // end validate profile 20th march
            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // end, code added by pr on 22ndjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR in dlist controller tab 3: " + error);
            if (getAction_type().equals("submit")) {
                if (!error.isEmpty()) {
                    data = "";
                    returnString = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    System.out.println("TAB3333:: set:::::::::" + set);
                    if (session.get("update_without_oldmobile").equals("no")) {
                        String ref_num = dlistservice.DLIST_tab3(form_details, profile_values, bulkData);
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
                                //profile_values.put("ca_mobile", form_details.getHod_mobile().trim());
                                profile_values.put("ca_email", form_details.getHod_email().trim());
                                profile_values.put("hod_name", form_details.getHod_name().trim());
                                profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                profile_values.put("hod_email", form_details.getHod_email().trim());
                                profile_values.put("hod_tel", form_details.getHod_tel().trim());
                            }
                        }
                        session.put("dlist_details", form_details);
                        session.put("ref_num", ref_num);
                    }
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    returnString = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else if (getAction_type().equals("update")) { // else if added by pr on 1stfeb18
                if (error.isEmpty()) {
                    String ref = session.get("ref").toString();
                    String admin_role = "NA";
                    if (session.get("admin_role") != null) {
                        admin_role = session.get("admin_role").toString();
                    }
                    Boolean flag = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                    if (flag && form_details.getModule().equals("user")) {
                        profile_values = (HashMap) session.get("profile-values");
                        form_details.setHod_email(profile_values.get("hod_email").toString());
                        form_details.setHod_name(profile_values.get("hod_name").toString());
                        //form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
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
                returnString = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for dlist controller tab 3: " + e.getMessage());
            e.printStackTrace();
            data = "";
            returnString = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String moderatorDataPost() {
        System.out.println("com.org.controller.Dlist_registration.moderatorDataPost():::: ");
        System.out.println("com.org.controller.Dlist_registration.moderatorDataPost():::: " + dlistData);
        try {
            boolean validEmail = false;
            Ldap l = new Ldap();
            validEmail = l.emailValidate(dlistData.getTauth_email());

            boolean mod_name_error = valid.nameValidation(dlistData.getT_off_name());
            System.out.println("ModeratorName" + mod_name_error);
            if (!mod_name_error) {
                System.out.println("length" + dlistData.getT_off_name().length());
                if (dlistData.getT_off_name().length() > 2) {
                    mod_name_error = true;
                } else {
                    error.put("ModeratorName", "Moderator name cannot be less than 3 characters");
                }
            } else {
                error.put("ModeratorName", "Moderator name cannot be empty");
            }

            Set<ModerateBean> set = null;

            if (session.get("moderatorListData") == null) {
                set = new HashSet<>();
            } else {
                set = (Set<ModerateBean>) session.get("moderatorListData");
                for (ModerateBean mb : set) {
                    if (mb.getTauth_email().equalsIgnoreCase(dlistData.getTauth_email())) {
                        error.put("Mod", "Duplicate entry not allowed");
                    }
                }
            }
            int size = set.size();
            if (error.isEmpty()) {
                if (validEmail) {
                    set.add(new ModerateBean(testCount++, dlistData.getT_off_name(), dlistData.getTmobile(), dlistData.getTauth_email()));

                    session.put("moderatorListData", set);
                    bulkData.put("dnsSingleData", session.get("moderatorListData"));
                    System.out.println("Bulkdata::::" + bulkData);
                } else {
                    error.put("ModeratorEmail", "Moderator email you entered is not exist in Ldap please check and enter the record again");
                    //error.put("ModeratorName","Moderator name cannot be less than 3 characters");
                }
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String singleOwnerDataPost() {

        System.out.println("com.org.controller.Dlist_registration.singleOwnerDataPost():::: ");
        System.out.println("com.org.controller.Dlist_registration.singleOwnerDataPost():::: " + dlistData1);
        try {

            boolean validEmail = false;
            Ldap l = new Ldap();
            validEmail = l.emailValidate(dlistData1.getOwner_email());
            boolean own_name_error = valid.nameValidation(dlistData1.getOwner_name());
            System.out.println("OwnerName" + own_name_error);

            if (!own_name_error) {
                System.out.println("length" + dlistData1.getOwner_name().length());
                if (dlistData1.getOwner_name().length() > 2) {
                    own_name_error = true;
                } else {
                    error.put("OwnerName", "Owner name cannot be less than 3 characters");
                }
            } else {
                error.put("OwnerName", "Owner name cannot be empty");
            }

            Set<OwnerBean> set = null;

            if (session.get("ownerListData") == null) {
                set = new HashSet<>();
            } else {
                set = (Set<OwnerBean>) session.get("ownerListData");
                for (OwnerBean ob : set) {
                    if (ob.getOwner_email().equalsIgnoreCase(dlistData1.getOwner_email())) {
                        error.put("Own", "duplicate entry not allowed");
                    }
                }
            }
            int size = set.size();
            if (error.isEmpty()) {
                if (validEmail) {
                    set.add(new OwnerBean(testCount++, dlistData1.getOwner_name(), dlistData1.getOwner_email(), dlistData1.getOwner_mobile()));

                    session.put("ownerListData", set);
                    bulkData.put("dnsSingleData", session.get("ownerListData"));
                } else {
                    error.put("OwnerEmail", "Owner email you entered is not exist in Ldap please check and enter the record again");
                }
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String singleModeratorDataFetch() {
        Set<ModerateBean> set = (Set<ModerateBean>) session.get("moderatorListData");
        for (ModerateBean moderatorBean : set) {
            if (moderatorBean.getId() == bulkDataId) {
                dlistData = moderatorBean;
                break;
            }
        }
        return SUCCESS;
    }

    public String singleOwnerDataFetch() {
        System.out.println("com.org.controller.Dns_registration.singleOwnerDataFetch() ::: " + dlistData1);
        Set<OwnerBean> set = (Set<OwnerBean>) session.get("ownerListData");
        for (OwnerBean ownerBean : set) {
            if (ownerBean.getId() == bulkDataId) {
                dlistData1 = ownerBean;
                break;
            }
        }
        return SUCCESS;
    }

    public String singleModeratorDataEditPost() {
        System.out.println("com.org.controller.Dlist_registration.singleDnsDataEditPost() ::: " + dlistData);

        if (dlistData.getErrorMessage() == null) {
            Set<ModerateBean> set = null;
            Set<ModerateBean> set1 = null;
            if (session.get("moderatorListData") == null) {
                dlistData.setErrorMessage("This Record does not exist!!!");
            } else {
                set = (Set<ModerateBean>) session.get("moderatorListData");
                set1 = new HashSet<>(set);
                set.remove(dlistData);
                System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() Before:::: " + session.get("moderatorListData"));
                int size = set.size();
                set.add(dlistData);
                if (size == set.size()) {
                    dlistData.setErrorMessage("Duplicate Record!!! Please check again.");
                    //set = new HashSet<>(set1);
                    set = set1;
                }
            }
            System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() After:::: " + session.get("moderatorListData"));
            session.put("moderatorListData", set);
            bulkData.put("dnsSingleData", session.get("moderatorListData"));
        }
        return SUCCESS;
    }

    public String singleOwnerDataEditPost() {
        System.out.println("com.org.controller.Dlist_registration.singleDnsDataEditPost() ::: " + dlistData1);
        if (dlistData1.getErrorMessage() == null) {
            Set<OwnerBean> set = null;
            Set<OwnerBean> set1 = null;
            if (session.get("ownerListData") == null) {
                dlistData1.setErrorMessage("This Record does not exist!!!");
            } else {
                set = (Set<OwnerBean>) session.get("ownerListData");
                set1 = new HashSet<>(set);
                set.remove(dlistData1);
                System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() Before:::: " + session.get("ownerListData"));
                int size = set.size();
                set.add(dlistData1);
                if (size == set.size()) {
                    dlistData1.setErrorMessage("Duplicate Record!!! Please check again.");
                    //set = new HashSet<>(set1);
                    set = set1;
                }
            }
            System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() After:::: " + session.get("ownerListData"));
            session.put("ownerListData", set);
            bulkData.put("dnsSingleData", session.get("ownerListData"));
        }
        return SUCCESS;
    }

    public String singleModeratorDataDeletePost() {
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() ::: " + dlistData);
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() ::: " + bulkDataId);
        Set<ModerateBean> set = null;
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() Before:::: " + session.get("moderatorListData"));
        if (session.get("moderatorListData") == null) {
            dlistData.setErrorMessage("This Record does not exist!!!");
        } else {
            set = (Set<ModerateBean>) session.get("moderatorListData");
            for (ModerateBean dnsBean : set) {
                if (dnsBean.getId() == bulkDataId) {
                    dlistData = dnsBean;
                    break;
                }
            }
            set.remove(dlistData);
        }
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() After:::: " + session.get("moderatorListData"));
        session.put("moderatorListData", set);
        bulkData.put("dnsSingleData", session.get("moderatorListData"));
        return SUCCESS;
    }

    public String singleOwnerDataDeletePost() {
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() ::: " + dlistData1);
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() ::: " + bulkDataId);
        Set<OwnerBean> set = null;
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() Before:::: " + session.get("ownerListData"));
        if (session.get("ownerListData") == null) {
            dlistData1.setErrorMessage("This Record does not exist!!!");
        } else {
            set = (Set<OwnerBean>) session.get("ownerListData");
            for (OwnerBean ownerBean : set) {
                if (ownerBean.getId() == bulkDataId) {
                    dlistData1 = ownerBean;
                    break;
                }
            }
            set.remove(dlistData1);
        }
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() After:::: " + session.get("ownerListData"));
        session.put("ownerListData", set);
        bulkData.put("dnsSingleData", session.get("ownerListData"));
        return SUCCESS;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the dlistbulkData
     */
    public Map<Object, Object> getDlistbulkData() {
        return dlistbulkData;
    }

    /**
     * @param dlistbulkData the dlistbulkData to set
     */
    public void setDlistbulkData(Map<Object, Object> dlistbulkData) {
        this.dlistbulkData = dlistbulkData;
    }

    /**
     * @return the user_file
     */
    public String getUser_file() {
        return user_file;
    }

    /**
     * @param user_file the user_file to set
     */
    public void setUser_file(String user_file) {
        this.user_file = user_file;
    }

    /**
     * @return the cellRecords
     */
    public ArrayList getCellRecords() {
        return cellRecords;
    }

    /**
     * @param cellRecords the cellRecords to set
     */
    public void setCellRecords(ArrayList cellRecords) {
        this.cellRecords = cellRecords;
    }

    /**
     * @return the errorList
     */
    public ArrayList getErrorList() {
        return errorList;
    }

    /**
     * @param errorList the errorList to set
     */
    public void setErrorList(ArrayList errorList) {
        this.errorList = errorList;
    }

    /**
     * @return the nonvalid
     */
    public ArrayList getNonvalid() {
        return nonvalid;
    }

    /**
     * @param nonvalid the nonvalid to set
     */
    public void setNonvalid(ArrayList nonvalid) {
        this.nonvalid = nonvalid;
    }

}
