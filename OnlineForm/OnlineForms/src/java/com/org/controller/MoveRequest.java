package com.org.controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.MoveReqBean;
import com.org.bean.UserData;
import com.org.connections.SingleApplicationContextSlave;
import com.org.dao.Database;
import com.org.dao.FinalAuditDetails;
import com.org.dao.Ldap;
import com.org.dao.MoveReqDao;
import com.org.dto.StatusDetails;
import com.org.service.MoveReqService;
import com.org.service.ProfileService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import validation.validation;

@lombok.Data
public class MoveRequest extends ActionSupport implements SessionAware {

    private UserData userValues;
    Map session;
    HashSet<String> rolls = new HashSet<>();
    Map profile_values = null;
    Map<String, Object> error = new HashMap();
    Ldap ldap = null;

    List<FinalAuditDetails> use_data;

    public List<FinalAuditDetails> getUse_data() {
        return use_data;
    }

    public void setUse_data(List<FinalAuditDetails> use_data) {
        this.use_data = use_data;
    }

    boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public HashSet<String> getRolls() {
        return rolls;
    }

    public String getInputCurrentCoordinatorEmail() {
        return inputCurrentCoordinatorEmail;
    }

    public void setInputCurrentCoordinatorEmail(String inputCurrentCoordinatorEmail) {
        this.inputCurrentCoordinatorEmail = inputCurrentCoordinatorEmail;
    }

    public void setRolls(HashSet<String> rolls) {
        this.rolls = rolls;
    }

    public String getInputPendingStatus() {
        return inputPendingStatus;
    }

    public void setInputPendingStatus(String inputPendingStatus) {
        this.inputPendingStatus = inputPendingStatus;
    }

    String inputCurrentCoordinatorEmail, inputPendingStatus, to_email, roles, mobile;
    List<Map<String, String>> listEmpCategoryMinistryDept;
    Map<String, String> radioEmpCatMinDept;
    List<FinalAuditDetails> pendingCoordinatorRequests;
    ArrayList<String> coordinatorEmailsFromCoordinatorEmail;
    String inputNewCoordinatorEmail;
    Map<String, String> success = new HashMap<String, String>();

    public List<Map<String, String>> getListEmpCategoryMinistryDept() {
        return listEmpCategoryMinistryDept;
    }

    public void setListEmpCategoryMinistryDept(List<Map<String, String>> listEmpCategoryMinistryDept) {
        this.listEmpCategoryMinistryDept = listEmpCategoryMinistryDept;
    }

    public UserData getUserValues() {
        return userValues;
    }

    public void setUserValues(UserData userValues) {
        this.userValues = userValues;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    String data, CSRFRandom, ReturnString;
    validation valid = null;
    ProfileService profileService = null;
    Map<String, String> valueData = new HashMap<>();

    public Map<String, String> getValueData() {
        return valueData;
    }

    public void setValueData(Map<String, String> valueData) {
        this.valueData = valueData;
    }

    // start, code added by pr on 10thjan18
    private Database db = null;
    Map<String, String> map = null;

    public MoveRequest() {
        ///   imappopservice = new ImapPopService();
        valid = new validation();

        map = new HashMap();
        db = new Database();
        ldap = new Ldap();
        profileService = new ProfileService();
        profile_values = new HashMap();

        if (valid == null) {
            valid = new validation();
        }

        if (map == null) {
            map = new HashMap();
        }

        //    if (moveBean == null) {
        //     moveBean = new MoveReqBean();
        //  }
        if (profileService == null) {
            profileService = new ProfileService();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }

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

    Map hodDetails = new HashMap();

    Set<String> OldEmproles1 = new HashSet<>();
    Set<String> NewEmproles1 = new HashSet<>();
    String emp_category = "";
    String emp_min_state_org = "";
    String emp_dept = "";
    String newcategory = "";
    String newmin_state_org = "";
    String newdepartment = "";


    public List<FinalAuditDetails> fetchAllPendingRequsts(String email) {
        MoveReqService moveReqService = new MoveReqService();
        return moveReqService.getAllPendingRequsts(email);
    }

    public String fetchEmploymentDetailsOfCoord() {
        error.clear();
        String empType = "";
        String status = "";
        System.out.println("INSIDE fetchEmploymentDetailsOfCoord");
        //String ldapMobile = fetchMobileFromLdap(inputCurrentCoordinatorEmail);
        Set<String> aliases = ldap.fetchAliases(inputCurrentCoordinatorEmail);
        if (valid.EmailValidation(inputCurrentCoordinatorEmail)) {
            error.put("currentCoordinatorEmail", "Please enter correct email addressss");
        } else if (!inputPendingStatus.equalsIgnoreCase("coordinator_pending") && !inputPendingStatus.equalsIgnoreCase("da_pending")) {
            error.put("pendingStatus", "Select pending status");
        } else {
            listEmpCategoryMinistryDept = new ArrayList<Map<String, String>>();
            for (String aliase : aliases) {
                if (aliase != null && inputPendingStatus.contains("coordinator_pending")) {
                    empType = "c";
                    status = "coordinator_pending";
                    listEmpCategoryMinistryDept.addAll(db.fetchFromEmpCordinator(aliase, empType));
                } else if (aliase != null && inputPendingStatus.contains("da_pending")) {
                    empType = "d";
                    status = "da_pending";
                    listEmpCategoryMinistryDept.addAll(db.fetchFromEmpCordinator(aliase, empType));
                }
            }
        }
        return SUCCESS;
    }

    public String fetchRequestsOfCoordAndShowValidCoords() {
        error.clear();
        String empType = "";
        String status = "";
        System.out.println("INSIDE fetchRequestsOfCoordAndShowValidCoords");
        // inputCurrentCoordinatorEmail and radioEmpCatMinDept must be passed from client (i.e. js)
        if (valid.EmailValidation(inputCurrentCoordinatorEmail)) {
            error.put("currentCoordinatorEmail", "Please enter correct email addressss");
        } else if (!inputPendingStatus.equalsIgnoreCase("coordinator_pending") && !inputPendingStatus.equalsIgnoreCase("da_pending")) {
            error.put("pendingStatus", "Select pending status");
        } else if (radioEmpCatMinDept == null) {
            error.put("radioEmpCatMinDept", "Please select at least one record!!!");
        } else {
            if (inputPendingStatus.contains("coordinator_pending")) {
                empType = "c";
                status = "coordinator_pending";

            } else if (inputPendingStatus.contains("da_pending")) {
                empType = "d";
                status = "da_pending";

            }
            pendingCoordinatorRequests = db.fetchAllPendingRequstsfromFinalAuditTrack(radioEmpCatMinDept, inputCurrentCoordinatorEmail, status);
            coordinatorEmailsFromCoordinatorEmail = db.fetchCoordinatorEmailFromCoordinatorEmailForMoveRequests(inputCurrentCoordinatorEmail, empType);
            coordinatorEmailsFromCoordinatorEmail.remove(inputCurrentCoordinatorEmail);
        }
        return SUCCESS;
    }

    public String moveRequestsToSelectedCoord() {
        error.clear();
        String empType = "";
        String status = "";

        if (inputPendingStatus.contains("coordinator_pending")) {
            empType = "c";
            status = "coordinator_pending";

        } else if (inputPendingStatus.contains("da_pending")) {
            empType = "d";
            status = "da_pending";

        }
        System.out.println("INSIDE MOVE REQUEST TO SHOW CATEGORY MINISTRY AND DEPARTMENT");
        coordinatorEmailsFromCoordinatorEmail = db.fetchCoordinatorEmailFromCoordinatorEmailForMoveRequests(inputCurrentCoordinatorEmail, empType);

        // coordinatorEmailsFromCoordinatorEmail and inputNewCoordinatorEmail and (pendingCoordinatorRequests)list of Registration Numbers will be shared by js
        if (valid.EmailValidation(inputNewCoordinatorEmail)) {
            error.put("currentCoordinatorEmail", "Selected email address is not in correct format!!!");
        } else if (inputNewCoordinatorEmail == null) {
            error.put("inputNewCoordinatorEmail", "Invalid selected/entered Coordinator email");
        } else if (coordinatorEmailsFromCoordinatorEmail.contains(inputNewCoordinatorEmail)) {
            updateRecords(pendingCoordinatorRequests, inputNewCoordinatorEmail, status);
            success.put("success", "Requests moved succesFully");
        }
        return SUCCESS;
    }
    
      public String userRollsValue() {
        System.out.println("value of User Rolls ::");
        String value = "da_pending".toString();
        System.out.println("value of User Rolls :::::" + value);
        return value;
    }

    public String updateRecords(List<FinalAuditDetails> details, String updateEmail, String status) {
        MoveReqService moveReqService = new MoveReqService();

        return moveReqService.updateRecords(details, updateEmail, status);
    }

    public String inserStatusRecords(List<FinalAuditDetails> details, String updateEmail, String roll, String hodMobile, UserData userData) throws ClassNotFoundException {
        MoveReqService moveReqService = new MoveReqService();
        return moveReqService.inserStatusRecords(details, updateEmail, roll, hodMobile, userData);
    }

    private Map<String, String> fetchApplicantEmail(String empCategory, String cordMin, String cordDept) {
        MoveReqService moveReqService = new MoveReqService();
        return moveReqService.fetchApplicantEmail(empCategory, cordMin, cordDept);
    }
}
