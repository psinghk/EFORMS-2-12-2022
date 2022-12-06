package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.FirewallService;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import com.org.service.WifiportService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import validation.validation;

public class Wifiport_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    Map session;
    Map profile_values = null;
    private List<FormBean> valueObj;
    private WifiportService wifiportservice = null;
    public String action_type;
    public String data;
    public String wifiport_sourceip;
    public String wifiport_destinationip;
    public String wifiport_service;
    public String wifiport_port;
    validation valid = null;
    public String wifiport_action;
    public String wifiport_time;
    private String remarks;
    public String ReturnString;
    public String request_type;
    public String cert;
    public String cert1;
    public String purpose;
    Map<String, Object> error = null;
    UpdateService updateService = null;
    boolean allowed_user;
    private Database db = null;
    Map<String, String> map = null;
    Map hodDetails = null;
    ProfileService profileService = null;
    Ldap ldap;

    public Wifiport_registration() {
        wifiportservice = new WifiportService();
        valid = new validation();
        error = new HashMap();
        updateService = new UpdateService();
        map = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        profile_values = new HashMap();
        ldap = new Ldap();
        if (wifiportservice == null) {
            wifiportservice = new WifiportService();
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

    public boolean isAllowed_user() {
        return allowed_user;
    }

    public void setAllowed_user(boolean allowed_user) {
        this.allowed_user = allowed_user;
    }

    public List<FormBean> getValueObj() {
        return valueObj;
    }

    public void setValueObj(List<FormBean> valueObj) {
        this.valueObj = valueObj;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public WifiportService getWifiportservice() {
        return wifiportservice;
    }

    public void setWifiportservice(WifiportService wifiportservice) {
        this.wifiportservice = wifiportservice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getWifiport_sourceip() {
        return wifiport_sourceip;
    }

    public void setWifiport_sourceip(String wifiport_sourceip) {
        this.wifiport_sourceip = wifiport_sourceip;
    }

    public String getWifiport_destinationip() {
        return wifiport_destinationip;
    }

    public void setWifiport_destinationip(String wifiport_destinationip) {
        this.wifiport_destinationip = wifiport_destinationip;
    }

    public String getWifiport_service() {
        return wifiport_service;
    }

    public void setWifiport_service(String wifiport_service) {
        this.wifiport_service = wifiport_service;
    }

    public String getWifiport_port() {
        return wifiport_port;
    }

    public void setWifiport_port(String wifiport_port) {
        this.wifiport_port = wifiport_port;
    }

    public String getWifiport_time() {
        return wifiport_time;
    }

    public void setWifiport_time(String wifiport_time) {
        this.wifiport_time = wifiport_time;
    }

    public String getReturnString() {
        return ReturnString;
    }

    public void setReturnString(String ReturnString) {
        this.ReturnString = ReturnString;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getCert1() {
        return cert1;
    }

    public void setCert1(String cert1) {
        this.cert1 = cert1;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String Wifiport_tab1() {
        System.out.println("purpose val@@@@@@@@@@@@" + getPurpose());
        try {
            for (FormBean model : valueObj) {
                boolean source_ip_error = false;
                boolean destination_ip_error = false;
                if (model.getSourceip().equals("[]")) {
                    source_ip_error = true;
                } else {
                    source_ip_error = valid.firewallipvalidation(model.getSourceip());
                }
                if (model.getDestinationip().equals("[]")) {
                    destination_ip_error = true;
                } else {
                    destination_ip_error = valid.firewallipvalidation(model.getDestinationip());
                }
                System.out.println("destination_ip_error" + destination_ip_error);
                System.out.println("inside wifiport tab1111111111111111111111111111" + model.getAction());
                boolean service_error = valid.dnsCnameValidation(model.getService());
                boolean ports_error = valid.firewallportValidation(model.getPorts());
                boolean action_error = valid.dnsCnameValidation(model.getAction());
                // boolean timeperiod_error = valid.numericdayValidation_firewall(model.getTimeperiod(), "days");
                boolean timeperiod_error = valid.serviceValidation(model.getTimeperiod());
                System.out.println("getPurpose():::::::" + getPurpose());
                boolean purpose_error = valid.purposeValidation(getPurpose());
                // System.out.println("timeperiod_error######################" + timeperiod_error);
                if (!model.getPorts().equals("") || !model.getPorts().isEmpty()) {
                    if (model.getPorts().contains("-")) {
                        String[] parts;
                        parts = model.getPorts().split("-");
                        int part1 = Integer.parseInt(parts[0]); // 004
                        int part2 = Integer.parseInt(parts[1]);
                        if (model.getService().equals("tcp") || model.getService().equals("udp")) {
                            if (part1 <= 65535 && part2 <= 65535) {

                            } else {
                                error.put("ports_error", "Port should be less then or equal to 65535 for " + model.getService() + " service");

                            }
                        }

                    } else {
                        int Port_val = Integer.parseInt(model.getPorts());
                        //String Port_val = 
                        if (model.getService().equals("tcp") || model.getService().equals("udp")) {
                            if (Port_val <= 65535) {
                            } else {
                                error.put("ports_error", "Port should be less then or equal to 65535 for " + model.getService() + " service");
                            }

                        }
                    }
                }
                if (source_ip_error == true) {
                    error.put("source_ip_error", "Enter the Source IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                }

                if (destination_ip_error == true) {
                    error.put("destination_ip_error", "Enter the Destination IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                }

                if (service_error == true) {
                    error.put("service_error", "Enter the service in correct format");
                }

                if (!model.getService().equals("icmp")) {
                    if (ports_error == true) {
                        error.put("port_error", "Enter the Port in correct format hyphen[-] allowed only");
                    }
                }

                if (action_error == true) {
                    error.put("action_error", "Enter the Action in correct format");
                }

                if (timeperiod_error == true) {
                    error.put("timeperiod_error", "Enter the Time/Period [Numeric only], days should be greater than 1 less than 730)");
                }
                if (purpose_error == true) {
                    error.put("purpose_error", "Enter Purpose of the request [characters,dot(.) and whitespace allowed] ");
                }
            }

            System.out.println("error ::::::::" + error);
            purpose = getPurpose();
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    wifiportservice.Wifiport_tab1(valueObj, purpose);
                    profile_values = (HashMap) session.get("profile-values");
                }

                //session.put("dns_details", form_details);
            } else {

                data = "";
                action_type = "";
                ReturnString = "";
            }

        } catch (ArrayIndexOutOfBoundsException e) {

            e.printStackTrace();
        }
        return "success";
    }

    public String Wifiport_tab2() {
        try {
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = userdata.getHodData();
            db = new Database();
            System.out.println("data:::::::::::::" + data);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = (FormBean) mapper.readValue(data, FormBean.class);
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());
            profile_values = (HashMap) session.get("profile-values");
            db = new Database();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }
            System.out.println("purposeeeeeeeeeeeeee" + form_details.getRemarks());
            try {
                ArrayList SourceIP = new ArrayList(Arrays.asList(wifiport_sourceip.split(";")));

                System.out.println("SourceIP::::::::" + SourceIP);

                ArrayList DestinationIP = new ArrayList(Arrays.asList(wifiport_destinationip.split(";")));

                System.out.println("DestinationIP::::::::" + DestinationIP);

                ArrayList Service = new ArrayList(Arrays.asList(wifiport_service.split(";")));

                System.out.println("Service::::::::" + Service);

                ArrayList Port = new ArrayList(Arrays.asList(wifiport_port.split(";")));

                System.out.println("Port::::::::" + Port);

                System.out.println("Port in arraylist:::::::::::" + Port + "size::::::" + Port.size());
                ArrayList Action = new ArrayList(Arrays.asList(wifiport_action.split(";")));

                System.out.println("Action::::::::" + Action);

                ArrayList Timeperiod = new ArrayList(Arrays.asList(wifiport_time.split(";")));

                System.out.println("Timeperiod::::::::" + Timeperiod);

                boolean purpose_error = valid.purposeValidation(form_details.getRemarks());

                System.out.println("purpose::::::::" + form_details.getRemarks());

                for (int j = 0; j < SourceIP.size(); j++) {
                    boolean source_ip_error = valid.firewallipvalidation(SourceIP.get(j).toString());
                    boolean destination_ip_error = valid.firewallipvalidation(DestinationIP.get(j).toString());
                    boolean service_error = valid.dnsCnameValidation(Service.get(j).toString());
                    if (!Service.get(j).toString().equals("icmp")) {
                        if (!Port.get(j).toString().equals("") || !Port.get(j).toString().isEmpty()) {
                            boolean ports_error = valid.firewallportValidation(Port.get(j).toString());
                            if (ports_error == true) {
                                error.put("ports_error", "Enter the Port in correct format hyphen[-] allowed only");
                            }
                            String[] parts;
                            parts = Port.get(j).toString().split("-");
                            if (!Service.get(j).toString().equals("icmp")) {
                                if (Port.get(j).toString().contains("-")) {
                                    int part1 = Integer.parseInt(parts[0]); // 004
                                    int part2 = Integer.parseInt(parts[1]);
                                    if (part1 <= 65535 && part2 <= 65535) {
                                    } else {
                                        error.put("ports_error", "Port should be less then or equal to 65535 for " + Service.get(j).toString() + " service");
                                    }
                                } else {
                                    int Port_val = Integer.parseInt(Port.get(j).toString());
                                    if (Port_val <= 65535) {
                                    } else {
                                        error.put("ports_error", "Port should be less then or equal to 65535 for " + Service + " service");

                                    }
                                }
                            }
                        }
                    }
                    System.out.println("inside wifiport tab2222222222222222222222222" + Action.get(j).toString());
                    boolean action_error = valid.dnsCnameValidation(Action.get(j).toString());
                    boolean timeperiod_error = valid.serviceValidation(Timeperiod.get(j).toString());
                    // boolean timeperiod_error = valid.numericdayValidation_firewall(Timeperiod.get(j).toString(), "days");

                    if (source_ip_error == true) {
                        error.put("source_ip_error", "Enter the Source IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                    }
                    if (destination_ip_error == true) {
                        error.put("destination_ip_error", "Enter the Destination IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                    }
                    if (service_error == true) {
                        error.put("service_error", "Enter the service in correct format");
                    }
                    if (action_error == true) {
                        error.put("action_error", "Enter the Action in correct format");
                    }
                    if (timeperiod_error == true) {
                        error.put("timeperiod_error", "Enter the Time/Period [Numeric only], days should be greater than 1 less than 730)");
                    }
                    if (purpose_error == true) {
                        error.put("purpose_error", "Enter Purpose of the request [characters,dot(.) and whitespace allowed]");
                    }

                }
                boolean employment_error = valid.EmploymentValidation(form_details.getUser_employment());
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
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == ORG EMPTY");
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
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == ORG EMPTY");
                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn")) {
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
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == ORG EMPTY");
                            }
                        }
                    }
                }
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                if (form_details.getModule() == null) {
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());

                    System.out.println("Hod Mobile:::::::::::::::" + form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.roTelValidation(form_details.getHod_tel());
                    System.out.println("ca desig error in wifiport" + ca_desg_error);
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Designation [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespace allowed]");
                    }
                    if (form_details.getHod_mobile().trim().startsWith("+")) {
                        if (hod_mobile_error == true && form_details.getHod_mobile().trim().startsWith("+91")) {
                            error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");
                        } else if (hod_mobile_error == true && !form_details.getHod_mobile().trim().startsWith("+91")) {
                            error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
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
                        if (form_details.getHod_email().equals(form_details.getUser_email())) {
                            error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                        } else {
                            Set s = userdata.getAliases();
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
                                } else if (nic_employee && nicemployeeeditable.equals("no")) {
                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        if (!((String) map.get("hod_email")).equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");
                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is nic email in imap controller tab 2");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is nic email in imap controller tab 2");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                }
                            } else if (nic_employee && nicemployeeeditable.equals("no")) {
                                map = db.fetchUserValuesFromOAD(userdata.getUid());
                                if (map.size() > 0) {
                                    if (!((String) map.get("hod_email")).equals(form_details.getHod_email())) {
                                        error.put("hod_email_error", "Reporting officer details can not be changed.");
                                    } else {

                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is nic email in imap controller tab 2");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {

                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is nic email in imap controller tab 2");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                        error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
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
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == error Map for imappop controller tab 2: " + error);

                if (getAction_type().equals("submit")) {
                    if (!error.isEmpty()) {
                        data = "";

                        action_type = "";
                    } else {
                        session.put("wifiport_details", form_details);
                        form_details.setSourceip(wifiport_sourceip);
                        form_details.setDestinationip(wifiport_destinationip);
                        form_details.setPorts(wifiport_port);
                        form_details.setService(wifiport_service);
                        form_details.setTimeperiod(wifiport_time);
                        form_details.setRemarks(form_details.getRemarks());
                        if (wifiport_action.equals("")) {
                            form_details.setAction("Default");
                        } else {
                            form_details.setAction(wifiport_action);
                        }
                        if (session.get("update_without_oldmobile").equals("no")) {
                            profile_values = (HashMap) session.get("profile-values");
                            String ref_num = wifiportservice.Wifiport_tab2(form_details, profile_values);
                            session.put("ref_num", ref_num);
                            profile_values = (HashMap) session.get("profile-values");

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
                                    } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn")) {
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
                        action_type = "";
                    }
                } else if (action_type.equals("update")) {
                    if (error.isEmpty()) {
                        String ref = session.get("ref").toString();
                        String admin_role = "NA";
                        if (session.get("admin_role") != null) {
                            admin_role = session.get("admin_role").toString();
                        }

                        form_details.setSourceip(wifiport_sourceip);
                        form_details.setDestinationip(wifiport_destinationip);
                        form_details.setService(wifiport_service);
                        form_details.setPorts(wifiport_port);
                        form_details.setTimeperiod(wifiport_time);
                        form_details.setAction(wifiport_action);
                        form_details.setRemarks(form_details.getRemarks());
                        Boolean fl = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                        if (fl.booleanValue() && form_details.getModule().equals("user")) {
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
                                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn")) {
                                    profile_values.put("Org", form_details.getOrg().trim());
                                }
                                profile_values.put("other_dept", form_details.getOther_dept().trim());
                            }
                        }
                    } else {
                        data = "";
                        ReturnString = "";
                        action_type = "";
                    }
                } else {

                    data = "";

                    action_type = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

//    public String fetchWifiportUsers() {
//        UserData userdata = (UserData) session.get("uservalues");
//        allowed_user = wifiportservice.WifiportAllowedUsers(userdata.getAliases());
//        System.out.println("allowed_user in fetch firewall users:::::::" + allowed_user);
//        return "success";
//    }
}
