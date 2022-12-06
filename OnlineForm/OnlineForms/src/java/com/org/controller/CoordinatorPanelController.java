/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.CoordinatorPanelBean;
import com.org.bean.UserData;
import com.org.service.CoordinatorPanelService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author Satya
 */
public class CoordinatorPanelController extends ActionSupport implements SessionAware {

    CoordinatorPanelBean cpanelbean;
    int emp_id;
    Map session;
    public String data;

    Map<String, Object> map;
    CoordinatorPanelService cpanelservice;
    public String jsonStr_new_categories;
    public String jsonStr_new_ministries;

    public CoordinatorPanelController() {

        map = new HashMap<String, Object>();
        cpanelservice = new CoordinatorPanelService();
    }

    //***Crud Operations**********************************
    public String jsonStr;

    //Register hog if empty
    public String hogRegistration() throws IOException {
        Map<String, Object> errormap = new HashMap<>();
        System.out.println("inside Hog Registration");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//     Map<String, Object> err = checkAddCoordValidations(cpanelbean);
//        if (!err.isEmpty()) {
//            Gson gsonObj = new Gson();
//            jsonStr = gsonObj.toJson(err);
//            return SUCCESS;
//        }
        data = htmlEncoding(data);
        cpanelbean = mapper.readValue(data, CoordinatorPanelBean.class);
        errormap = checkAddHogValidations(cpanelbean);

        if (!errormap.isEmpty()) {
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(errormap);
            return SUCCESS;
        }
        UserData userdata = (UserData) session.get("uservalues");
        cpanelbean.setHog_name(userdata.getName());
        cpanelbean.setHog_mobile(userdata.getMobile());
        cpanelbean.setHog_email(userdata.getEmail());
        map = cpanelservice.hogRegistration(cpanelbean);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String insertCoordinatorTable() throws IOException {
        System.out.println("inside insertCoordinator " + data);
        // data = data.replace("<", "&lt;");
        //data = data.replace(">", "&gt;");

        data = htmlEncoding(data);
        System.out.println("data:::::::" + data);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        cpanelbean = mapper.readValue(data, CoordinatorPanelBean.class);

        Map<String, Object> err = checkAddCoordValidations(cpanelbean);
        if (!err.isEmpty()) {
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(err);
            return SUCCESS;
        }
        //give validation
        UserData userdata = (UserData) session.get("uservalues");
        cpanelbean.setLogin_user_email(userdata.getEmail());
        //cpanelbean.setOther_dept("Department of Testing");
        map = cpanelservice.insertCoordinatorTable(cpanelbean);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String fetchCountsRequestCoordinator() {
        UserData userdata = (UserData) session.get("uservalues");
        String loggedinUser = userdata.getEmail();
        map = cpanelservice.getCountsRequestCoordinator(loggedinUser);
        System.out.println("map:::::" + map);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }

    public String viewAllCoordinators() {
        Map<String, Object> new_categories = new HashMap<>();
        System.out.println("View All Coordinators");
        UserData userdata = (UserData) session.get("uservalues");
        String loggedinUser = userdata.getEmail();
        map = cpanelservice.viewAllCoordinators(loggedinUser, userdata);
        // List<CoordinatorPanelBean> status = (List<CoordinatorPanelBean>)map.get("status");
        if (map.get("status").equals("empty")) {
            List<String> categories = cpanelservice.getAllCategory();
            new_categories.put("all_allowed_categories", categories);

        } else if (!map.get("status").equals("empty")) {

            Map<String, Object> categoryHog = cpanelservice.getCategoryHOG(loggedinUser);
            List<String> table_category = (List<String>) categoryHog.get("status");
            List<String> all_categories = cpanelservice.getAllCategory();
            if (all_categories.contains("Others")) {
                all_categories.remove("Others");
            }
            if (all_categories.contains("Other")) {
                all_categories.remove("Other");
            }
            //all_categories.removeAll(table_category);
            System.out.println("removed categories====" + table_category);
            new_categories.put("all_allowed_categories", all_categories);
        }

        System.out.println("map:::::" + map);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        jsonStr_new_categories = gsonObj.toJson(new_categories);
        System.out.println("JSON :::::" + jsonStr);
        System.out.println("JSON :::::" + jsonStr_new_categories);
        return SUCCESS;
    }

    List<String> table_categorymin;

    public String fetchAllCategoryMinistry() {
        Map<String, Object> new_ministries = new HashMap<>();
        UserData userdata = (UserData) session.get("uservalues");

        Map<String, Object> category_min_Hog = cpanelservice.getCategoryMinistryHOG(userdata.getEmail(), emp_category);
        if (!category_min_Hog.get("status").equals("empty")) {
            table_categorymin = (List<String>) category_min_Hog.get("status");
        }
        List<String> all_ministries = cpanelservice.getAllCategoryMinistry(emp_category);
        if (table_categorymin == null) {
            new_ministries.put("all_allowed_ministries", all_ministries);
        } else {
            all_ministries.removeAll(table_categorymin);
            System.out.println("removed ministries====" + table_categorymin);
            new_ministries.put("all_allowed_ministries", all_ministries);
        }

        Gson gsonObj = new Gson();
        jsonStr_new_ministries = gsonObj.toJson(new_ministries);
        System.out.println("JSON :::::" + jsonStr);
        System.out.println("JSON :::::" + jsonStr_new_ministries);
        return SUCCESS;
    }

    public String fetchAllCategoryMinistryDepartmentHOG() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getAllCategoryMinistryDepartment(emp_category, emp_min_state_org);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String viewCoordinatorsById() {
        UserData userdata = (UserData) session.get("uservalues");
        String loggedinHog = userdata.getEmail();
        map = cpanelservice.viewCoordinatorsById(emp_id, loggedinHog);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String updateCoordinator() throws IOException {
        data = htmlEncoding(data);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        cpanelbean = mapper.readValue(data, CoordinatorPanelBean.class);
        UserData userdata = (UserData) session.get("uservalues");
        Map<String, Object> err = checkAddCoordValidations(cpanelbean);
        if (!err.isEmpty()) {
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(err);
            return SUCCESS;
        }

        cpanelbean.setLogin_user_email(userdata.getEmail());
        map = cpanelservice.updateCoordinator(cpanelbean);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String deleteCoordinatorById() {
//        if (emp_id == 0) {
//            Map<String, Object> err = new HashMap();
//            err.put("emp_id", "Emp id cannot be null");
//            Gson gsonObj = new Gson();
//            jsonStr = gsonObj.toJson(err);
//            return SUCCESS;
//        }
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.deleteCoordinatorById(emp_id, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String coordinator_email;

    public String fetchPendingRequestOfCoordinator() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getPendingRequestOfCoordinator(emp_id, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String fetchCompletedRequestOfCoordinator() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getCompletedRequestOfCoordinator(emp_id, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String fetchForwardedRequestOfCoordinator() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getForwardedRequestOfCoordinator(emp_id, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String fetchRejectedRequestOfCoordinator() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getRejectedRequestOfCoordinator(emp_id, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    //min wise coordinator pending status
//    public String getMinWisePendingReqCoord(){
//        UserData userdata = (UserData) session.get("uservalues");
//        map = cpanelservice.getMinWisePendingReqCoord(userdata.getEmail());
//        Gson gsonObj = new Gson();
//        jsonStr = gsonObj.toJson(map);
//        return SUCCESS;
//    }
    //min wise coordinator Completed status
//    public String getMinWiseCompletedReqCoord(){
//        UserData userdata = (UserData) session.get("uservalues");
//        map = cpanelservice.getMinWiseCompletedReqCoord(userdata.getEmail());
//        Gson gsonObj = new Gson();
//        jsonStr = gsonObj.toJson(map);
//        return SUCCESS;
//    }
//    
    public String emp_category;
    public String emp_min_state_org;
    public String emp_dept;
    public String approver_email;
    public String emp_coord_mobile;
    public String emp_coord_name;
    public String emp_ip;

    public String fetchCategoryHOG() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getCategoryHOG(userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String fetchCategoryMinistryHOG() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getCategoryMinistryHOG(userdata.getEmail(), emp_category);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String fetchCategoryMinistryDepartmentHOG() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getCategoryMinistryDepartmentHOG(emp_category, emp_min_state_org);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String add_dept_emp_category;
    public String add_dept_emp_min_state_org;
    public String add_dept_emp_dept;
    public String state_code;
    public String state_dept;
    public String org;

    public String add_dept_state_code;
    public String add_dept_state_dept;
    public String add_dept_org;
    public String add_dept_emp_coord_mobile;

    public String addDepartmentHOG() {
        UserData userdata = (UserData) session.get("uservalues");
        Map<String, Object> err = checkAddDepartmentValidations(add_dept_emp_category, add_dept_emp_min_state_org, add_dept_emp_dept, emp_coord_email, emp_ip, add_dept_state_code, add_dept_org, add_dept_state_dept);
        if (!err.isEmpty()) {
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(err);
            return SUCCESS;
        }
        add_dept_emp_category = htmlEncoding(add_dept_emp_category);
        add_dept_emp_min_state_org = htmlEncoding(add_dept_emp_min_state_org);
        add_dept_emp_dept = htmlEncoding(add_dept_emp_dept);
        emp_coord_mobile = htmlEncoding(emp_coord_mobile);
        emp_coord_name = htmlEncoding(emp_coord_name);
        emp_coord_email = htmlEncoding(emp_coord_email);
        state_dept = htmlEncoding(state_dept);
        add_dept_state_code = htmlEncoding(add_dept_state_code);
        add_dept_state_dept = htmlEncoding(add_dept_state_dept);
        add_dept_org = htmlEncoding(add_dept_org);
        map = cpanelservice.addDepartmentHOG(add_dept_emp_category, add_dept_emp_min_state_org, add_dept_emp_dept, emp_coord_mobile, emp_coord_name, emp_coord_email, emp_ip, add_dept_state_code, add_dept_state_dept, add_dept_org);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    //called internally along with insertion
//     public String addDepartmentRequest(){
//         CoordinatorPanelBean copanel = new CoordinatorPanelBean();
//         UserData userdata = (UserData) session.get("uservalues");
//         copanel.setLogin_user_email(userdata.getEmail());
//         copanel.setEmp_category(emp_category);
//         copanel.setEmp_min_state_org(emp_min_state_org);
//         copanel.setEmp_dept(emp_dept);
//         map = cpanelservice.addDepartmentRequest(cpanelbean);
//         Gson gsonObj = new Gson();
//         jsonStr = gsonObj.toJson(map);
//         return SUCCESS;
//     }
    //track request by hog
    public String trackRequestByHOG() {
        map = cpanelservice.trackRequestByHOG(registration_no);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }

    //Department update by hog in all tables
    public String new_dept;

    public String updateDepartmentHOG() {
        Map<String, Object> err = checkUpdateDepartmentValidations(emp_category, emp_min_state_org, emp_dept, new_dept, state_code, state_dept);
        if (!err.isEmpty()) {
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(err);
            return SUCCESS;

        }
        emp_category = htmlEncoding(emp_category);
        emp_min_state_org = htmlEncoding(emp_min_state_org);
        emp_dept = htmlEncoding(emp_dept);
        new_dept = htmlEncoding(new_dept);
        state_code = htmlEncoding(state_code);
        org = htmlEncoding(org);
        state_dept = htmlEncoding(state_dept);

        map = cpanelservice.updateDepartmentHOG(emp_category, emp_min_state_org, emp_dept, new_dept, state_code, org, state_dept);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }
    public String other_dept;

    public String updateDepartmentPendingRequestById() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // cpanelbean = mapper.readValue(getData(), CoordinatorPanelBean.class);
        Map<String, Object> err = new HashMap();

        if (other_dept == null || other_dept.equals("")) {
            err.put("other_dept", "invalid");
        }
        if (!err.isEmpty()) {
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(err);
            return SUCCESS;
        }
        UserData userdata = (UserData) session.get("uservalues");
        cpanelbean = new CoordinatorPanelBean();
        cpanelbean.setLogin_user_email(userdata.getEmail());
        dept_id = htmlEncoding(dept_id);
        other_dept = htmlEncoding(other_dept);
        cpanelbean.setDept_id(Integer.parseInt(dept_id));
        cpanelbean.setOther_dept(other_dept);

        map = cpanelservice.updateDepartmentPendingRequestById(cpanelbean);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }

    public String dept_id;

    public String viewDepartmentPendingRequestById() {
        //             if (dept_id.equals("")||dept_id.isEmpty()) {
//            Map<String, Object> err = new HashMap();
//            err.put("dept_id", "Department id cannot be null");
//            Gson gsonObj = new Gson();
//            jsonStr = gsonObj.toJson(err);
//            return SUCCESS;
//        }
        Map jsmap = cpanelservice.viewDepartmentPendingRequestById(Integer.parseInt(dept_id));
        System.out.println("map:::::" + jsmap);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(jsmap);
        return SUCCESS;
    }

    public String showDepartmentPendingRequest() {
        UserData userdata = (UserData) session.get("uservalues");
        Map jsmap = cpanelservice.showDepartmentPendingRequest(userdata.getEmail());
        System.out.println("map:::::" + jsmap);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(jsmap);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }
    public String emp_coord_email;
    public String department;

    public String approveDepartment() {
        //             if (id==0) {
//            Map<String, Object> err = new HashMap();
//            err.put("id", "Department id cannot be null");
//            Gson gsonObj = new Gson();
//            jsonStr = gsonObj.toJson(err);
//            return SUCCESS;
//        }
        if (coordinator_email == null || coordinator_email.equals("")) {
            Map<String, Object> err = new HashMap();
            err.put("coordinator_email", "invalid");
            if (!err.isEmpty()) {
                Gson gsonObj = new Gson();
                jsonStr = gsonObj.toJson(err);
                return SUCCESS;
            }

        }

        String status = checkCoordinator(coordinator_email);
        Map<String, Object> err = new HashMap();
        System.out.println("inside panel");
        if (status.equals("exists")) {
            System.out.println("inside exists");
            map = cpanelservice.approvePendingDepartments(Integer.parseInt(id), coordinator_email);
            System.out.println("map:::::" + map);
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(map);
            System.out.println("JSON :::::" + jsonStr);
        } else {
            System.out.println("inside not exists");
            err.put("status", "notValid");
            Gson gsonObj = new Gson();
            jsonStr = gsonObj.toJson(err);
            System.out.println("JSON :::::" + err);
        }

        return SUCCESS;
    }
    public String id;

    public String rejectDepartment() {
//                      if (id.equals("")||id.isEmpty()) {
//            Map<String, Object> err = new HashMap();
//            err.put("id", "Department id cannot be null");
//            Gson gsonObj = new Gson();
//            jsonStr = gsonObj.toJson(err);
//            return SUCCESS;
//        }

        map = cpanelservice.rejectDepartment(id);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }
    public String registration_no;

    //not used
    public String previewRequest() {
//                     if (registration_no.equals("")|| registration_no.isEmpty()) {
//            Map<String, Object> err = new HashMap();
//            err.put("id", "Department id cannot be null");
//            Gson gsonObj = new Gson();
//            jsonStr = gsonObj.toJson(err);
//            return SUCCESS;
//        }
        map = cpanelservice.previewRequest(registration_no);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);

        return SUCCESS;
    }

    public String pullbackAllRequestHog() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.pullbackAllRequestHog(registration_no, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String checkCoordinator(String coordinator_email) {
        map = cpanelservice.checkCoordinatorExistance(coordinator_email);
        String status = (String) map.get("status");
        return status;
    }

    public String pullbackRequestHog() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.pullbackRequestHog(registration_no, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String fetchPendingRequestOfHog() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.getPendingRequestOfHog(userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String approveAllRequestHog() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.approveAllRequestHog(registration_no, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String approveRequestHog() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.approveRequestHog(registration_no, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String fetchMinistryInfoOfHOG() {
        UserData userdata = (UserData) session.get("uservalues");
        Map<String, Object> map = cpanelservice.getMinistryInfoOfHOG(userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String rejectRequestHog() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.rejectRequestHog(registration_no, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;

    }

    public String insertRoTable() throws IOException {
        htmlEncoding(data);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        cpanelbean = mapper.readValue(data, CoordinatorPanelBean.class);
        UserData userdata = (UserData) session.get("uservalues");
        cpanelbean.setLogin_user_email(userdata.getEmail());
        map = cpanelservice.insertRoTable(cpanelbean);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    public String viewAllRo() {
        System.out.println("View All RO");
        map = cpanelservice.viewAllRo();
        System.out.println("map:::::" + map);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String viewRoId() {
        map = cpanelservice.viewRoById(emp_id);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String updateRo() throws IOException {
        htmlEncoding(data);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        cpanelbean = mapper.readValue(data, CoordinatorPanelBean.class);
        UserData userdata = (UserData) session.get("uservalues");
        cpanelbean.setLogin_user_email(userdata.getEmail());
        map = cpanelservice.updateRo(cpanelbean);
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;
    }

    public String deleteRoById() {
        UserData userdata = (UserData) session.get("uservalues");
        map = cpanelservice.deleteRoById(emp_id, userdata.getEmail());
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        return SUCCESS;
    }

    //***EO Crud Operations****************************
    public String fetchStatsROPendingInfo() {
        CoordinatorPanelService coordservice = new CoordinatorPanelService();
        map = coordservice.getStatsPendingRequestRO();
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }

    public String fetchStatsCOPendingInfo() {
        CoordinatorPanelService coordservice = new CoordinatorPanelService();
        map = coordservice.getStatsPendingRequestCO();
        Gson gsonObj = new Gson();
        jsonStr = gsonObj.toJson(map);
        System.out.println("JSON :::::" + jsonStr);
        return SUCCESS;

    }

    public Map<String, Object> checkAddCoordValidations(CoordinatorPanelBean cpanelbean) {
        Map<String, Object> err = new HashMap<>();
        validation v = new validation();
        if (cpanelbean.getEmp_category() == null || cpanelbean.getEmp_category().equals("") || cpanelbean.getEmp_category().isEmpty()) {
            err.put("emp_category", "invalid");
        } else if (cpanelbean.getEmp_category().equals("Central")) {
            if (cpanelbean.getEmp_min_state_org() == null || cpanelbean.getEmp_min_state_org().equals("") || cpanelbean.getEmp_min_state_org().isEmpty()) {

                err.put("emp_min_state_org", "invalid");

            }
            if (cpanelbean.getEmp_dept() == null || cpanelbean.getEmp_dept().equals("")) {
                err.put("emp_dept", "invalid");
            }
        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("State")) {
            if (cpanelbean.getStateCode() == null || cpanelbean.getStateCode().equals("")) {

                err.put("state_code", "invalid");

            }
            if (cpanelbean.getState_dept() == null || cpanelbean.getState_dept().equals("")) {
                err.put("state_dept", "invalid");
            }
        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("UT")) {
            if (cpanelbean.getEmp_min_state_org() == null || cpanelbean.getEmp_min_state_org().equals("")) {

                err.put("emp_min_state_org", "invalid");

            }
            if (cpanelbean.getEmp_dept() == null || cpanelbean.getEmp_dept().equals("")) {
                err.put("emp_dept", "invalid");
            }
        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("Const")) {
            if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {

                err.put("org", "invalid");

            }

        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("Psu")) {
            if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {

                err.put("org", "invalid");

            }

        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("Nkn")) {
            if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {

                err.put("org", "invalid");

            }

        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("Project")) {
            if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {

                err.put("org", "invalid");

            }

        } else if (cpanelbean.getEmp_category().equalsIgnoreCase("Others")) {
            if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {

                err.put("org", "invalid");

            }
            if (cpanelbean.getOther_dept() == null || cpanelbean.getOther_dept().equals("")) {

                err.put("other_dept", "invalid");

            }

        }

        if (cpanelbean.getEmp_coord_name() == null || cpanelbean.getEmp_coord_name() != null && cpanelbean.getEmp_coord_name().equals("")) {
            err.put("emp_coord_name", "invalid");

        }
        if (cpanelbean.getEmp_coord_email() != null) {
            boolean flag = v.EmailValidation(cpanelbean.getEmp_coord_email());
            if (flag) {
                err.put("emp_coord_email", "invalid");
            }
        }

        if (cpanelbean.getEmp_coord_mobile() == null || cpanelbean.getEmp_coord_mobile().equals("")) {
            err.put("emp_coord_mobile", "invalid");
        }

        if (cpanelbean.getEmp_coord_mobile() != null) {
            boolean flag = v.MobileValidation(cpanelbean.getEmp_coord_mobile());
            if (flag) {
                err.put("emp_coord_mobile", "invalid");
            }
        }

        if (cpanelbean.getEmp_ip() == null || cpanelbean.getEmp_ip().isEmpty()) {

            err.put("emp_ip", "invalid");

        }
        if (cpanelbean.getEmp_ip() != null) {
            boolean flag = v.baseipValidation(cpanelbean.getEmp_ip());
            if (flag) {
                err.put("emp_ip", "invalid");
            }

        }

        return err;

    }

    public Map<String, Object> checkAddDepartmentValidations(String add_dept_emp_category, String add_dept_emp_min_state_org, String add_dept_emp_dept, String add_dept_emp_coord_email, String add_dept_emp_ip, String add_dept_state_code, String add_dept_org, String add_dept_state_dept) {
        Map<String, Object> err = new HashMap<>();
        err.clear();
        validation v = new validation();
        if (add_dept_emp_category == null || add_dept_emp_category.equals("")) {
            err.put("emp_category", "invalid");
        } else if (add_dept_emp_category.equals("Central") || add_dept_emp_category.equals("UT")) {
            if (add_dept_emp_min_state_org.equals("") || add_dept_emp_min_state_org.isEmpty()) {

                err.put("add_dept_emp_min_state_org", "invalid");

            }
            if (add_dept_emp_dept == null || add_dept_emp_dept.equals("")) {
                err.put("emp_dept", "invalid");
            }
        } else if (add_dept_emp_category.equals("State")) {
            if (add_dept_state_code == null || add_dept_state_code.equals("") || add_dept_state_code.isEmpty()) {

                err.put("add_dept_state_code", "invalid");

            }
            if (add_dept_state_dept == null || add_dept_state_dept.isEmpty()) {
                err.put("emp_dept", "invalid");
            }
        }

//         else if(add_dept_emp_category.equals("UT")){
//      if (add_dept_org==null|| add_dept_org.equals("")||add_dept_org.isEmpty()) {
//        
//          err.put("add_dept_org", "invalid");
//         
//      }
//       if (add_dept_emp_dept == null || add_dept_emp_dept.equals("")) {
//              err.put("add_dept_emp_dept", "invalid");
//          }
//      }
//      
        if (add_dept_emp_coord_email == null || add_dept_emp_coord_email.equals("")) {
            err.put("add_dept_emp_coord_email", "invalid");
        } else if (add_dept_emp_coord_email != null) {
            boolean flag = v.EmailValidation(add_dept_emp_coord_email);
            if (flag) {
                err.put("add_dept_emp_coord_email", "invalid");
            }
        }
        if (add_dept_emp_ip == null || add_dept_emp_ip.equals("")) {
            err.put("add_dept_emp_ip", "invalid");
        }

//         else if(add_dept_emp_ip!=null ){
//      boolean flag = v.baseipValidation(add_dept_emp_ip);
//          if(flag){
//           err.put("add_dept_emp_ip", "invalid");
//          }
//      }
        return err;

    }
    //cpanelservice.updateDepartmentHOG(emp_category, emp_min_state_org, emp_dept, new_dept,state_code,org,state_dept);
    //update dept

    public Map<String, Object> checkUpdateDepartmentValidations(String add_dept_emp_category, String add_dept_emp_min_state_org, String add_dept_emp_dept, String new_dept, String add_dept_state_code, String add_dept_state_dept) {
        Map<String, Object> err = new HashMap<>();
        err.clear();
        validation v = new validation();
        if (add_dept_emp_category == null || add_dept_emp_category.equals("")) {
            err.put("emp_category", "invalid");
        } else if (add_dept_emp_category.equals("Central") || add_dept_emp_category.equals("UT")) {
            if (add_dept_emp_min_state_org.equals("") || add_dept_emp_min_state_org.isEmpty()) {

                err.put("add_dept_emp_min_state_org", "invalid");

            }
            if (add_dept_emp_dept == null || add_dept_emp_dept.equals("")) {
                err.put("emp_dept", "invalid");
            }
        } else if (add_dept_emp_category.equals("State")) {
            if (add_dept_state_code == null || add_dept_state_code.equals("") || add_dept_state_code.isEmpty()) {

                err.put("add_dept_state_code", "invalid");

            }
            if (add_dept_state_dept == null || add_dept_state_dept.isEmpty()) {
                err.put("emp_dept", "invalid");
            }
        }

        if (new_dept == null || new_dept.isEmpty()) {
            err.put("new_dept", "invalid");
        }

        return err;

    }

    public Map<String, Object> checkAddHogValidations(CoordinatorPanelBean cpanelbean) {
        Map<String, Object> err = new HashMap<>();
        validation v = new validation();
        if (cpanelbean.getEmp_category_hog() == null || cpanelbean.getEmp_category_hog().equals("") || cpanelbean.getEmp_category_hog().isEmpty()) {
            err.put("emp_category_hog", "invalid");
        }

//      else if (cpanelbean.getEmp_category_hog().equals("Central")) {
//          if (cpanelbean.getEmp_min_state_org_hog() == null || cpanelbean.getEmp_min_state_org_hog().equals("") || cpanelbean.getEmp_min_state_org_hog().isEmpty()) {
//
//              err.put("emp_min_state_org_hog", "invalid");
//
//          }
//         
//      } 
//      else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("State")) {
//          if (cpanelbean.getStateCode_hog() == null || cpanelbean.getStateCode_hog().equals("")) {
//
//              err.put("state_code_hog", "invalid");
//
//          }
//          
//      } 
//     else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("UT")) {
//          if (cpanelbean.getEmp_min_state_org_hog() == null || cpanelbean.getEmp_min_state_org_hog().equals("")) {
//
//              err.put("org_hog", "invalid");
//
//          }
//          
//      } 
//      else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("Const")) {
//          if (cpanelbean.getOrg_hog() == null || cpanelbean.getOrg_hog().equals("")) {
//
//              err.put("org_hog", "invalid");
//
//          }
//
//      }
//      else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("Psu")) {
//          if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {
//
//              err.put("org_hog", "invalid");
//
//          }
//
//      } 
//      else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("Nkn")) {
//          if (cpanelbean.getOrg() == null || cpanelbean.getOrg().equals("")) {
//
//              err.put("org_hog", "invalid");
//
//          }
//
//      } else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("Project")) {
//          if (cpanelbean.getOrg_hog() == null || cpanelbean.getOrg_hog().equals("")) {
//
//              err.put("org_hog", "invalid");
//
//          }
//
//      } else if (cpanelbean.getEmp_category_hog().equalsIgnoreCase("Others")) {
//          if (cpanelbean.getOrg_hog() == null || cpanelbean.getOrg_hog().equals("")) {
//
//              err.put("org_hog", "invalid");
//
//          }
//         
//
//      }
        return err;

    }

    public CoordinatorPanelBean getCpanelbean() {
        return cpanelbean;
    }

    public void setCpanelbean(CoordinatorPanelBean cpanelbean) {
        this.cpanelbean = cpanelbean;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the jsonStr
     */
    public String getJsonStr() {
        return jsonStr;
    }

    /**
     * @param jsonStr the jsonStr to set
     */
    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    /**
     * @return the emp_category
     */
    public String getEmp_category() {
        return emp_category;
    }

    /**
     * @param emp_category the emp_category to set
     */
    public void setEmp_category(String emp_category) {
        this.emp_category = emp_category;
    }

    /**
     * @return the emp_min_state_org
     */
    public String getEmp_min_state_org() {
        return emp_min_state_org;
    }

    /**
     * @param emp_min_state_org the emp_min_state_org to set
     */
    public void setEmp_min_state_org(String emp_min_state_org) {
        this.emp_min_state_org = emp_min_state_org;
    }

    /**
     * @return the emp_dept
     */
    public String getEmp_dept() {
        return emp_dept;
    }

    /**
     * @param emp_dept the emp_dept to set
     */
    public void setEmp_dept(String emp_dept) {
        this.emp_dept = emp_dept;
    }

    /**
     * @return the approver_email
     */
    public String getApprover_email() {
        return approver_email;
    }

    /**
     * @param approver_email the approver_email to set
     */
    public void setApprover_email(String approver_email) {
        this.approver_email = approver_email;
    }

    /**
     * @return the emp_coord_email
     */
    public String getEmp_coord_email() {
        return emp_coord_email;
    }

    /**
     * @param emp_coord_email the emp_coord_email to set
     */
    public void setEmp_coord_email(String emp_coord_email) {
        this.emp_coord_email = emp_coord_email;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the coordinator_email
     */
    public String getCoordinator_email() {
        return coordinator_email;
    }

    /**
     * @param coordinator_email the coordinator_email to set
     */
    public void setCoordinator_email(String coordinator_email) {
        this.coordinator_email = coordinator_email;
    }

    /**
     * @return the add_dept_emp_category
     */
    public String getAdd_dept_emp_category() {
        return add_dept_emp_category;
    }

    /**
     * @param add_dept_emp_category the add_dept_emp_category to set
     */
    public void setAdd_dept_emp_category(String add_dept_emp_category) {
        this.add_dept_emp_category = add_dept_emp_category;
    }

    /**
     * @return the add_dept_emp_min_state_org
     */
    public String getAdd_dept_emp_min_state_org() {
        return add_dept_emp_min_state_org;
    }

    /**
     * @param add_dept_emp_min_state_org the add_dept_emp_min_state_org to set
     */
    public void setAdd_dept_emp_min_state_org(String add_dept_emp_min_state_org) {
        this.add_dept_emp_min_state_org = add_dept_emp_min_state_org;
    }

    /**
     * @return the add_dept_emp_dept
     */
    public String getAdd_dept_emp_dept() {
        return add_dept_emp_dept;
    }

    /**
     * @param add_dept_emp_dept the add_dept_emp_dept to set
     */
    public void setAdd_dept_emp_dept(String add_dept_emp_dept) {
        this.add_dept_emp_dept = add_dept_emp_dept;
    }

    /**
     * @return the registration_no
     */
    public String getRegistration_no() {
        return registration_no;
    }

    /**
     * @param registration_no the registration_no to set
     */
    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the dept_id
     */
    public String getDept_id() {
        return dept_id;
    }

    /**
     * @param dept_id the dept_id to set
     */
    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    /**
     * @return the other_dept
     */
    public String getOther_dept() {
        return other_dept;
    }

    /**
     * @param other_dept the other_dept to set
     */
    public void setOther_dept(String other_dept) {
        this.other_dept = other_dept;
    }

    /**
     * @return the new_dept
     */
    public String getNew_dept() {
        return new_dept;
    }

    /**
     * @param new_dept the new_dept to set
     */
    public void setNew_dept(String new_dept) {
        this.new_dept = new_dept;
    }

    /**
     * @return the add_dept_state_code
     */
    public String getAdd_dept_state_code() {
        return add_dept_state_code;
    }

    /**
     * @param add_dept_state_code the add_dept_state_code to set
     */
    public void setAdd_dept_state_code(String add_dept_state_code) {
        this.add_dept_state_code = add_dept_state_code;
    }

    /**
     * @return the add_dept_state_dept
     */
    public String getAdd_dept_state_dept() {
        return add_dept_state_dept;
    }

    /**
     * @param add_dept_state_dept the add_dept_state_dept to set
     */
    public void setAdd_dept_state_dept(String add_dept_state_dept) {
        this.add_dept_state_dept = add_dept_state_dept;
    }

    /**
     * @return the add_dept_org
     */
    public String getAdd_dept_org() {
        return add_dept_org;
    }

    /**
     * @param add_dept_org the add_dept_org to set
     */
    public void setAdd_dept_org(String add_dept_org) {
        this.add_dept_org = add_dept_org;
    }

    /**
     * @return the add_dept_emp_coord_mobile
     */
    public String getAdd_dept_emp_coord_mobile() {
        return add_dept_emp_coord_mobile;
    }

    /**
     * @param add_dept_emp_coord_mobile the add_dept_emp_coord_mobile to set
     */
    public void setAdd_dept_emp_coord_mobile(String add_dept_emp_coord_mobile) {
        this.add_dept_emp_coord_mobile = add_dept_emp_coord_mobile;
    }

    /**
     * @return the jsonStr_new_categories
     */
    public String getJsonStr_new_categories() {
        return jsonStr_new_categories;
    }

    /**
     * @param jsonStr_new_categories the jsonStr_new_categories to set
     */
    public void setJsonStr_new_categories(String jsonStr_new_categories) {
        this.jsonStr_new_categories = jsonStr_new_categories;
    }

    /**
     * @return the jsonStr_new_ministries
     */
    public String getJsonStr_new_ministries() {
        return jsonStr_new_ministries;
    }

    /**
     * @param jsonStr_new_ministries the jsonStr_new_ministries to set
     */
    public void setJsonStr_new_ministries(String jsonStr_new_ministries) {
        this.jsonStr_new_ministries = jsonStr_new_ministries;
    }

    public String htmlEncoding(String data) {
        data = StringUtils.replaceAll(data, "<", "&lt;");
        data = StringUtils.replaceAll(data, ">", "&gt;");
        if (data != null) {
            if (data.contains(";") || data.startsWith("+") && data.contains(".") || data.contains("-") || data.contains("@")) {

            } else {
                data = ESAPI.encoder().encodeForHTML(data);
            }
        }
        return data;

    }

}
