/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.service.ProfileService;
import com.org.service.UpdateCOService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author nikki
 */
public class UpdateCO extends ActionSupport implements SessionAware {

    UpdateCOService updatecoservice = new UpdateCOService();
    ProfileService profileService = new ProfileService();
    Map session;
    String data, add_data, test, returnString, dept;
    List list = new ArrayList();
    Map map = new HashMap();
    Map co_map = new HashMap();
    Map<String, Object> error = new HashMap<String, Object>();
    FormBean form_details;
    validation valid = new validation();
    int emp_id;

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public UpdateCO() {
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getAdd_data() {
        return add_data;
    }

    public void setAdd_data(String add_data) {
        this.add_data = add_data;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    public Map getCo_map() {
        return co_map;
    }

    public void setCo_map(Map co_map) {
        this.co_map = co_map;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
    

    public String execute() throws Exception {
        ArrayList<String> formlist = new ArrayList<>();
        formlist.clear();
        if (session.get("adminfilterForms") != null) {
            formlist = (ArrayList) session.get("adminfilterForms");
        }
        UserData userdata = (UserData) session.get("uservalues");

        if (formlist.contains("vpn")) {
            if (formlist.contains("change_add")) {
                formlist.remove("change_add");
            }
            if (formlist.contains("vpn_renew")) {
                formlist.remove("vpn_renew");
            }
            if (formlist.contains("vpn_single")) {
                formlist.remove("vpn_single");
            }
        } else {
            if (userdata.getEmail().equalsIgnoreCase("Sandeep.kumar9@nic.in") || userdata.getEmail().equalsIgnoreCase("vp.agrawal@nic.in")) {
                formlist.add("vpn");
            }
            if (formlist.contains("change_add") || formlist.contains("vpn_renew") || formlist.contains("vpn_single")) {
                if (!formlist.contains("vpn")) {
                    formlist.add("vpn");
                }
                if (formlist.contains("change_add")) {
                    formlist.remove("change_add");
                }
                if (formlist.contains("vpn_renew")) {
                    formlist.remove("vpn_renew");
                }
                if (formlist.contains("vpn_single")) {
                    formlist.remove("vpn_single");
                }
            }
        }

        if (formlist.contains("email")) {
            if (formlist.contains("nkn_bulk") || formlist.contains("nkn_single") || formlist.contains("bulk") || formlist.contains("single") || formlist.contains("gem")) {
                if (formlist.contains("nkn_bulk")) {
                    formlist.remove("nkn_bulk");
                }
                if (formlist.contains("nkn_single")) {
                    formlist.remove("nkn_single");
                }
                if (formlist.contains("bulk")) {
                    formlist.remove("bulk");
                }
                if (formlist.contains("single")) {
                    formlist.remove("single");
                }
                if (formlist.contains("gem")) {
                    formlist.remove("gem");
                }
            }
        } else {
            if (formlist.contains("nkn_bulk") || formlist.contains("nkn_single") || formlist.contains("bulk") || formlist.contains("single") || formlist.contains("gem")) {
                formlist.add("email");
                if (formlist.contains("nkn_bulk")) {
                    formlist.remove("nkn_bulk");
                }
                if (formlist.contains("nkn_single")) {
                    formlist.remove("nkn_single");
                }
                if (formlist.contains("bulk")) {
                    formlist.remove("bulk");
                }
                if (formlist.contains("single")) {
                    formlist.remove("single");
                }
                if (formlist.contains("gem")) {
                    formlist.remove("gem");
                }
            }
        }

        Collections.sort(formlist);
        session.put("adminFormCo", formlist);
        return SUCCESS;
    }

    public String updateCO_getdata() {
        String formname = data;
        list = updatecoservice.updateCO_getdata(formname);
        return SUCCESS;
    }

    public String getCaValues() {
        String ca_email = data;
        map = updatecoservice.getCaValues(ca_email);
        return SUCCESS;
    }

    public String check_otherdept() {
        returnString = updatecoservice.check_otherdept(data, test, add_data, dept);
        return SUCCESS;
    }

    public String updateCO_getCOdata() {
        co_map.clear();
        co_map = updatecoservice.updateCO_getCOdata(data, add_data);
        return SUCCESS;
    }

    public String add_coordinator() {
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);

            boolean useremployment_error = valid.EmploymentValidation(form_details.getUser_employment());

            if (add_data.equalsIgnoreCase("email")) {
                if (useremployment_error) {
                    error.put("useremployment_error", "Please enter Employment in correct format");
                } else {
                    if (form_details.getUser_employment().equals("Central")) {
                        if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                            error.put("minerror", "Please enter Ministry in correct format");
                        } else if (form_details.getDept() == null || valid.MinistryValidation(form_details.getDept())) {
                            error.put("deperror", "Please enter Department in correct format");
                        } else {
                            List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                            if (!minlist.contains(form_details.getMin())) {
                                error.put("minerror", "Please enter Ministry in correct format");
                            }
                            List deplist = profileService.getCentralDept(form_details.getMin());
                            if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                                if (!deplist.contains(form_details.getDept())) {
                                    error.put("deperror", "Please enter Department in correct format");
                                }
                            }
                        }
                    } else if (form_details.getUser_employment().equals("State")) {
                        if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                            error.put("minerror", "Please enter State in correct format");
                        } else if (form_details.getDept() == null || valid.MinistryValidation(form_details.getDept())) {
                            error.put("deperror", "Please enter State Department in correct format");
                        } else {
                            List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                            if (!minlist.contains(form_details.getMin())) {
                                error.put("minerror", "Please enter State in correct format");
                            }
                            List deplist = profileService.getCentralDept(form_details.getMin());
                            if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                                if (!deplist.contains(form_details.getDept())) {
                                    error.put("deperror", "Please enter State Department in correct format");
                                }
                            }
                        }
                    } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const")
                            || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                        if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                            error.put("minerror", "Please enter Organization in correct format");
                        } else {
                            List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                            if (!minlist.contains(form_details.getMin())) {
                                error.put("minerror", "Please enter Organization in correct format");
                            }
                        }
                    }
                    if (form_details.getDept().toLowerCase().equals("other")) {
                        String a = updatecoservice.check_otherdept(form_details.getUser_employment(), form_details.getMin(), add_data, form_details.getDept());
                        if (a.equalsIgnoreCase("NA")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("other_dep_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println("ORG EMPTY");
                            }
                        } else {
                            error.put("deperror", "Co-ordinator already exists for this selection");
                        }
                    }
                }

                if (form_details.getDomain() == null || "".equals(form_details.getDomain())) {

                } else {
                    boolean domain_error = valid.accnameValidation(form_details.getDomain());
                    if (domain_error) {
                        error.put("domain_error", "Enter Domain Name [characters and dot(.)]");
                        form_details.setDomain(ESAPI.encoder().encodeForHTML(form_details.getDomain()));
                    }
                }
            } else {  // for all the other forms -- dept is optional
                System.out.println("form_details.getDept():: " + form_details.getDept());
                if (useremployment_error) {
                    error.put("useremployment_error", "Please enter Employment in correct format");
                } else {
                    if (form_details.getUser_employment().equals("Central")) {
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if ((form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) && !minlist.contains(form_details.getMin())) {
                            error.put("minerror", "Please enter Ministry in correct format");
                        } else {
                            if (form_details.getDept() == "" || form_details.getDept() == null) {
                                // do nothing
                            } else {
                                List deplist = profileService.getCentralDept(form_details.getMin());
                                if (valid.MinistryValidation(form_details.getDept())) {
                                    error.put("deperror", "Please enter Department in correct format");
                                } else {
                                    if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                                        if (!deplist.contains(form_details.getDept())) {
                                            error.put("deperror", "Please enter Department in correct format");
                                        }
                                    }
                                }
                            }
                        }
                    } else if (form_details.getUser_employment().equals("State")) {
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if ((form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) && !minlist.contains(form_details.getMin())) {
                            error.put("minerror", "Please enter State in correct format");
                        } else {
                            if (form_details.getDept() == "" || form_details.getDept() == null) {
                                // do nothing
                            } else {
                                List deplist = profileService.getCentralDept(form_details.getMin());
                                if (valid.MinistryValidation(form_details.getDept())) {
                                    error.put("deperror", "Please enter State Department in correct format");
                                } else {
                                    if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                                        if (!deplist.contains(form_details.getDept())) {
                                            error.put("deperror", "Please enter State Department in correct format");
                                        }
                                    }
                                }
                            }
                        }
                    } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const")
                            || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                        if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                            error.put("minerror", "Please enter Organization in correct format");
                        } else {
                            List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                            if (!minlist.contains(form_details.getMin())) {
                                error.put("minerror", "Please enter Organization in correct format");
                            }
                        }
                    }
                    if (form_details.getDept().toLowerCase().equals("other")) {
                        String a = updatecoservice.check_otherdept(form_details.getUser_employment(), form_details.getMin(), add_data,form_details.getDept());
                        if (a.equalsIgnoreCase("NA")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("other_dep_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println("ORG EMPTY");
                            }
                        } else {
                            error.put("deperror", "Co-ordinator already exists for this selection");
                        }
                    }
                }

            }

            boolean ca_email_error = valid.EmailValidation(form_details.getCa_email());
            if (ca_email_error) {
                error.put("ca_email_error", "Enter Co-ordinator email address in correct format");
                form_details.setCa_email(ESAPI.encoder().encodeForHTML(form_details.getCa_email()));
            } else {
                map = (HashMap) profileService.getHODdetails(form_details.getCa_email());
                if (map.get("bo").toString().equals("no bo")) {
                    error.put("ca_email_error", "Email address not present in LDAP");
                    form_details.setCa_email(ESAPI.encoder().encodeForHTML(form_details.getCa_email()));
                } else {
                    if (!form_details.getCa_name().equalsIgnoreCase(map.get("cn").toString())) {
                        error.put("ca_name_error", "Enter Co-ordinator name in correct format");
                        form_details.setCa_name(ESAPI.encoder().encodeForHTML(form_details.getCa_name()));
                    }

                    if (!form_details.getCa_mobile().equalsIgnoreCase(map.get("mobile").toString())) {
                        error.put("ca_mobile_error", "Enter Co-ordinator mobile in correct format");
                        form_details.setCa_mobile(ESAPI.encoder().encodeForHTML(form_details.getCa_mobile()));
                    }
                }
            }

            boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
            if (hod_email_error) {
                error.put("hod_email_error", "Enter Admin email address in correct format");
                form_details.setHod_email(ESAPI.encoder().encodeForHTML(form_details.getHod_email()));
            } else {
                map.clear();
                map = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                if (map.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Email address not present in LDAP");
                    form_details.setHod_email(ESAPI.encoder().encodeForHTML(form_details.getHod_email()));
                }
            }

            if (form_details.getBase_ip() == null || "".equals(form_details.getBase_ip())) {

            } else {
                if (form_details.getBase_ip().contains(",")) {
                    String ip[] = form_details.getBase_ip().split(",");
                    for (String i : ip) {
                        if (valid.baseipValidation(i.trim())) {
                            error.put("base_ip_error", "Enter ip in correct format - " + i);
                            form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
                        }
                    }
                } else {
                    if (valid.baseipValidation(form_details.getBase_ip())) {
                        error.put("base_ip_error", "Enter ip in correct format");
                        form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
                    }
                }
            }
            if (!error.isEmpty()) {
                data = "";

            } else {
                // insert data
                System.out.println("NO ERROR :: " + add_data);
                Map profile_values = (HashMap) session.get("profile-values");
                updatecoservice.insertCO_details(form_details, add_data, profile_values.get("email").toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String update_coordinator() {
        try {
            error.clear();
            System.out.println("DATA:: " + data);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);

            if (add_data.equalsIgnoreCase("email")) {
                if (form_details.getDomain() == null || "".equals(form_details.getDomain())) {

                } else {
                    boolean domain_error = valid.accnameValidation(form_details.getDomain());
                    if (domain_error) {
                        error.put("domain_error", "Enter Domain Name [characters and dot(.)]");
                        form_details.setDomain(ESAPI.encoder().encodeForHTML(form_details.getDomain()));
                    }
                }
            }
            if (add_data.equalsIgnoreCase("vpn")) {
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
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("ministry_error", "Please enter Ministry in correct format");
                        }
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
                        }

                        List deplist = profileService.getCentralDept(form_details.getMin());
                        if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!deplist.contains(form_details.getDept())) {
                                error.put("dept_error", "Please enter Ministry in correct format");
                            }
                        }
                        if (form_details.getDept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(" == " + "ORG EMPTY");
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
                        if (!form_details.getState_dept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!statedeplist.contains(form_details.getState_dept())) {
                                error.put("state_dept_error", "Please enter correct State Department");
                            }
                        }
                        if (form_details.getState_dept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(" == " + "ORG EMPTY");
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
                            }
                        }
                        if (form_details.getOrg().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(" == " + "ORG EMPTY");
                            }
                        }
                    }
                }
            }

            boolean ca_email_error = valid.EmailValidation(form_details.getCa_email());
            if (ca_email_error) {
                error.put("ca_email_error", "Enter Co-ordinator email address in correct format");
                form_details.setCa_email(ESAPI.encoder().encodeForHTML(form_details.getCa_email()));
            } else {
                map = (HashMap) profileService.getHODdetails(form_details.getCa_email());
                if (map.get("bo").toString().equals("no bo")) {
                    error.put("ca_email_error", "Email address not present in LDAP");
                    form_details.setCa_email(ESAPI.encoder().encodeForHTML(form_details.getCa_email()));
                } else {
                    if (!form_details.getCa_name().equalsIgnoreCase(map.get("cn").toString())) {
                        error.put("ca_name_error", "Enter Co-ordinator name in correct format");
                        form_details.setCa_name(ESAPI.encoder().encodeForHTML(form_details.getCa_name()));
                    }

                    if (!form_details.getCa_mobile().equalsIgnoreCase(map.get("mobile").toString())) {
                        error.put("ca_mobile_error", "Enter Co-ordinator mobile in correct format");
                        form_details.setCa_mobile(ESAPI.encoder().encodeForHTML(form_details.getCa_mobile()));
                    }
                }
            }

            boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
            if (hod_email_error) {
                error.put("hod_email_error", "Enter Admin email address in correct format");
                form_details.setHod_email(ESAPI.encoder().encodeForHTML(form_details.getHod_email()));
            } else {
                map.clear();
                map = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                if (map.get("bo").toString().equals("no bo")) {
                    error.put("hod_email_error", "Email address not present in LDAP");
                    form_details.setHod_email(ESAPI.encoder().encodeForHTML(form_details.getHod_email()));
                }
            }

            if (form_details.getBase_ip() == null || "".equals(form_details.getBase_ip())) {

            } else {
                if (form_details.getBase_ip().contains(",")) {
                    String ip[] = form_details.getBase_ip().split(",");
                    for (String i : ip) {
                        if (valid.baseipValidation(i.trim())) {
                            error.put("base_ip_error", "Enter ip in correct format - " + i);
                            form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
                        }
                    }
                } else {
                    if (valid.baseipValidation(form_details.getBase_ip())) {
                        error.put("base_ip_error", "Enter ip in correct format");
                        form_details.setBase_ip(ESAPI.encoder().encodeForHTML(form_details.getBase_ip()));
                    }
                }
            }
            System.out.println("ERROR:: " + error);
            if (!error.isEmpty()) {
                data = "";

            } else {
                // insert data
                System.out.println("NO ERROR :: " + add_data);
                Map profile_values = (HashMap) session.get("profile-values");
                updatecoservice.updateCO_details(form_details, add_data, profile_values.get("email").toString(), emp_id);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String update_ministry() {
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );

            if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                error.put("minerror", "Please enter Ministry in correct format");
            } else {
                List minlist = profileService.getCentralMinistry("Central");
                if (!minlist.contains(form_details.getMin())) {
                    error.put("minerror", "Please enter Ministry in correct format");
                }
            }

            boolean dept_other_error = valid.MinistryValidation(form_details.getNew_min_dept());
            if (dept_other_error == true) {
                error.put("new_min_dept_error", "Please enter New Ministry Name in correct format");
            }
            System.out.println("ERROR:: " + error);
            if (!error.isEmpty()) {
                data = "";
            } else {
                // insert data             
                Map profile_values = (HashMap) session.get("profile-values");
                updatecoservice.update_ministry(form_details, profile_values.get("email").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String update_dept() {
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details
                    = mapper.readValue(data, FormBean.class
                    );

            boolean useremployment_error = valid.EmploymentValidation(form_details.getUser_employment());
            if (useremployment_error) {
                error.put("useremployment_error", "Please enter Employment in correct format");
            } else {
                if (form_details.getUser_employment().equals("Central")) {
                    if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                        error.put("minerror", "Please enter Ministry in correct format");
                    } else if (form_details.getDept() == null || valid.MinistryValidation(form_details.getDept())) {
                        error.put("deperror", "Please enter Department in correct format");
                    } else {
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("minerror", "Please enter Ministry in correct format");
                        }
                        List deplist = profileService.getCentralDept(form_details.getMin());
                        if (!deplist.contains(form_details.getDept())) {
                            error.put("deperror", "Please enter Department in correct format");
                        }
                    }
                } else if (form_details.getUser_employment().equals("State")) {
                    if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                        error.put("minerror", "Please enter State in correct format");
                    } else if (form_details.getDept() == null || valid.MinistryValidation(form_details.getDept())) {
                        error.put("deperror", "Please enter State Department in correct format");
                    } else {
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("minerror", "Please enter State in correct format");
                        }
                        List deplist = profileService.getCentralDept(form_details.getMin());
                        if (!deplist.contains(form_details.getDept())) {
                            error.put("deperror", "Please enter State Department in correct format");
                        }
                    }
                } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const")
                        || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                    if (form_details.getMin() == null || valid.MinistryValidation(form_details.getMin())) {
                        error.put("minerror", "Please enter Organization in correct format");
                    } else {
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("minerror", "Please enter Organization in correct format");
                        }
                    }
                }
                boolean dept_other_error = valid.MinistryValidation(form_details.getNew_min_dept());
                if (dept_other_error == true) {
                    error.put("new_min_dept_error", "Please enter New Department Name in correct format");
                }
            }
            if (!error.isEmpty()) {
                data = "";
            } else {
                // insert data
                Map profile_values = (HashMap) session.get("profile-values");
                updatecoservice.update_dept(form_details, profile_values.get("email").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String updateCO_deleteCO() {
        Map profile_values = (HashMap) session.get("profile-values");
        updatecoservice.updateCO_deleteCO(data, add_data, profile_values.get("email").toString());
        return SUCCESS;
    }
}