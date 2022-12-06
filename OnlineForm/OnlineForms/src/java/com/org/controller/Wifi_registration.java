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
import com.org.bean.UserData;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.ProfileService;
import com.org.service.SignUpService;
import com.org.service.UpdateService;
import com.org.service.WifiService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author nikki
 */
public class Wifi_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    Map session;
    Map profile_values = null;
    Map userValues = null;
    WifiService wifiservice = null;
    UpdateService updateService = null;// line added by pr on 1stfeb18
    SignUpService signup = null;
    String wifi_values = ServletActionContext.getServletContext().getInitParameter("wifi_values");
    validation valid = null;
    Map<String, Object> error = null;
    public String action_type, data, email;
    ProfileService profileService = null;
    Map hodDetails = null;
    Map fwdOfcDetails = null;
    private Ldap ldap = null;
    // start, code added by pr on 22ndjan18
    String CSRFRandom;
    private Database db = null;
    Map<String, String> map = null;

    List<FormBean> mac_list = null;
    List<FormBean> mac_list2 = null;
    List<FormBean> mac_list3 = null;

    public Wifi_registration() {
        System.out.println("IN CONSTRUCTOR");
        mac_list = new ArrayList<>();
        mac_list2 = new ArrayList<>();
        mac_list3 = new ArrayList<>();
        map = new HashMap<>();
        ldap = new Ldap();
        db = new Database();
        fwdOfcDetails = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        error = new HashMap<>();
        valid = new validation();
        signup = new SignUpService();
        updateService = new UpdateService();
        wifiservice = new WifiService();
        profile_values = new HashMap();
        userValues = new HashMap();

        if (wifiservice == null) {
            wifiservice = new WifiService();
        }
        if (userValues == null) {
            userValues = new HashMap();
        }
        if (mac_list == null) {
            mac_list = new ArrayList<>();
        }
        if (mac_list2 == null) {
            mac_list2 = new ArrayList<>();
        }
        if (mac_list3 == null) {
            mac_list3 = new ArrayList<>();
        }
        if (ldap == null) {
            ldap = new Ldap();
        }
        if (db == null) {
            db = new Database();
        }
        if (fwdOfcDetails == null) {
            fwdOfcDetails = new HashMap();
        }
        if (signup == null) {
            signup = new SignUpService();
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

    public List<FormBean> getMac_list2() {
        return mac_list2;
    }

    public void setMac_list2(List<FormBean> mac_list2) {
        this.mac_list2 = mac_list2;
    }

    public List<FormBean> getMac_list() {
        return mac_list;
    }

    public void setMac_list(List<FormBean> mac_list) {
        this.mac_list = mac_list;
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

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map getUserValues() {
        return userValues;
    }

    public void setUserValues(Map userValues) {
        this.userValues = userValues;
    }

    public String Wifi_tab1() {
        try {
            System.out.println("inside wifitab1");
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setWifi_type("nic");

            boolean mac1_error = false;
            boolean ranmac1_error = false;
            boolean ranmac2_error = false;
            boolean ranmac3_error = false;
            boolean ranmac4_error = false;
            boolean os1_error = false;

            if (form_details.getWifi_process().equals("certificate")) {

            } else if (form_details.getWifi_process().equals("req_delete")) {

                String[] deleteMacId = form_details.getDelete_mac_id();
                if (deleteMacId == null) {
                    error.put("deleteMacId_error", "Please Select Atleast one check box");
                } else {
                    for (String delObj : deleteMacId) {
                        String[] data = delObj.split(",");
                        FormBean newFormBean = new FormBean();
                        newFormBean.setWifi_mac1(data[0]);
                        newFormBean.setWifi_os1(data[1]);
                        newFormBean.setRegistration_no(data[2]);
                        mac_list2.add(newFormBean);
                    }
                    session.put("maclist2", mac_list2);
                }
            } else {

                String commaSeperatedStringOfAliases = userdata.getAliasesInString();
                List<String> ccOfMac = wifiservice.countOfMacWithThisUser(commaSeperatedStringOfAliases);
                List<String> llp = new ArrayList<>();
                ccOfMac.stream().forEach(p -> {
                    llp.add(p);
                });
                
                // ---added by rahul, manikant Dec 2021--
                List deletedMacFromTable=new ArrayList();
                String userEmail= userdata.getEmail();
                deletedMacFromTable =wifiservice.featchMacFromWifiMacTable(userEmail);
                int macCountFromMacTable=deletedMacFromTable.size(); 
                //----------
                
                if (!form_details.getWifi_type().equals("nic")) {
                    error.put("wifitype_err", "Incorrect option");
                    form_details.setWifi_type(ESAPI.encoder().encodeForHTML(form_details.getWifi_type()));
                }
                if (form_details.getWifi_process().equals("request")) {
                    mac1_error = valid.macvalidation(form_details.getWifi_mac1());
                    ranmac1_error=valid.macvalidationRandomized(form_details.getWifi_mac1());
                    
                    os1_error = valid.osValidation(form_details.getWifi_os1());
                    if (mac1_error == true) {
                        error.put("mac1_error", "Enter Valid MAC Address");
                        form_details.setWifi_mac1(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac1()));
                    } else {
                        llp.add(form_details.getWifi_mac1());
                    }
                     if (ranmac1_error == true) {
                            error.put("mac1_error", "It is a random mac and please request for the device mac ");
                            form_details.setWifi_mac1(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac1()));
                        } 
                    
                    
                    if (!form_details.getWifi_mac2().equals("")) {
                        boolean mac2_error = valid.macvalidation(form_details.getWifi_mac2());
                        ranmac2_error=valid.macvalidationRandomized(form_details.getWifi_mac2());
                         
                        if (mac2_error == true) {
                            error.put("mac2_error", "Enter Valid MAC Address");
                            form_details.setWifi_mac2(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac2()));
                        } else {
                            llp.add(form_details.getWifi_mac2());
                        }
                        if (ranmac2_error == true) {
                            error.put("mac2_error", "It is a random mac and please request for the device mac ");
                            form_details.setWifi_mac2(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac2()));
                        } 
                    }
                    if (!form_details.getWifi_mac3().equals("")) {
                        boolean mac3_error = valid.macvalidation(form_details.getWifi_mac3());
                        ranmac3_error=valid.macvalidationRandomized(form_details.getWifi_mac3());
                        
                        if (mac3_error == true) {
                            error.put("mac3_error", "Enter Valid MAC Address");
                            form_details.setWifi_mac3(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac3()));
                        } else {
                            llp.add(form_details.getWifi_mac3());
                        }
                         if (ranmac3_error == true) {
                            error.put("mac3_error", "It is a random mac and please request for the device mac ");
                            form_details.setWifi_mac3(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac3()));
                        } 
                    }

                    //------------added by mohit sharma -------------------------
                    if (!form_details.getWifi_mac4().equals("")) {
                        boolean mac4_error = valid.macvalidation(form_details.getWifi_mac4());
                        ranmac4_error=valid.macvalidationRandomized(form_details.getWifi_mac4());
                       
                        if (mac4_error == true) {
                            error.put("mac4_error", "Enter Valid MAC Address");
                            form_details.setWifi_mac4(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac4()));
                        } else {
                            llp.add(form_details.getWifi_mac4());
                        }
                        if (ranmac4_error == true) {
                            error.put("mac4_error", "It is a random mac and please request for the device mac ");
                            form_details.setWifi_mac4(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac4()));
                        } 
                    }
                    if (os1_error == true) {
                        error.put("os1_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                        form_details.setWifi_os1(ESAPI.encoder().encodeForHTML(form_details.getWifi_os1()));
                    }

                    if (!form_details.getWifi_os2().equals("")) {
                        boolean os2_error = valid.osValidation(form_details.getWifi_os2());
                        if (os2_error == true) {
                            error.put("os2_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                            form_details.setWifi_os2(ESAPI.encoder().encodeForHTML(form_details.getWifi_os2()));
                        }
                    }
                    if (!form_details.getWifi_os3().equals("")) {
                        boolean os3_error = valid.osValidation(form_details.getWifi_os3());
                        if (os3_error == true) {
                            error.put("os3_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                            form_details.setWifi_os3(ESAPI.encoder().encodeForHTML(form_details.getWifi_os3()));
                        }
                    }
                    //------------added by mohit sharma -------------------------

                    if (!form_details.getWifi_os4().equals("")) {
                        boolean os4_error = valid.osValidation(form_details.getWifi_os4());
                        if (os4_error == true) {
                            error.put("os4_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                            form_details.setWifi_os4(ESAPI.encoder().encodeForHTML(form_details.getWifi_os4()));
                        }
                    }

                    if (!form_details.getDevice_type1().equals("")) {
                        boolean deviceType1_error = valid.osValidation(form_details.getDevice_type1());
                        if (deviceType1_error) {
                            error.put("device1_error", "Device type not in correct format");
                            form_details.setDevice_type1(ESAPI.encoder().encodeForHTML(form_details.getDevice_type1()));
                        }
                    }

                    if (!form_details.getDevice_type2().equals("")) {
                        boolean deviceType2_error = valid.osValidation(form_details.getDevice_type2());
                        if (deviceType2_error) {
                            error.put("device2_error", "Device type not in correct format");
                            form_details.setDevice_type2(ESAPI.encoder().encodeForHTML(form_details.getDevice_type2()));
                        }
                    }

                    if (!form_details.getDevice_type3().equals("")) {
                        boolean deviceType3_error = valid.osValidation(form_details.getDevice_type3());
                        if (deviceType3_error) {
                            error.put("device3_error", "Device type not in correct format");
                            form_details.setDevice_type3(ESAPI.encoder().encodeForHTML(form_details.getDevice_type3()));
                        }
                    }

                    //------------added by mohit sharma -------------------------
                    if (!form_details.getDevice_type4().equals("")) {
                        boolean deviceType4_error = valid.osValidation(form_details.getDevice_type4());
                        if (deviceType4_error) {
                            error.put("device4_error", "Device type not in correct format");
                            form_details.setDevice_type4(ESAPI.encoder().encodeForHTML(form_details.getDevice_type4()));
                        }
                    }

                    //       below lines added by ravinder
                    int macCount = llp.size();
                    System.out.println("macCount:" + macCount);
                    int macCountduplicate;
                    macCount = macCount-macCountFromMacTable;   //added by rahul, manikant Dec 2021
                    if (macCount > 4) {
                        error.put("mac1_error", "A user can not have more than 4 Mac address");
                        if (!form_details.getWifi_mac2().equals("")) {
                            error.put("mac2_error", "A user can not have more than 4 Mac address");
                        }
                        if (!form_details.getWifi_mac3().equals("")) {
                            error.put("mac3_error", "A user can not have more than 4 Mac address");
                            System.out.println("mac can not be assigned to the user as he/she already has 4 mac");
                        }
                        if (!form_details.getWifi_mac4().equals("")) {
                            error.put("mac4_error", "A user can not have more than 4 Mac address");
                            System.out.println("mac can not be assigned to the user as he/she already has 4 mac");
                        }
                    } else {
                        Set<String> ssMac1 = new HashSet<>();
                        Set<String> ssMac2 = new HashSet<>();
                        Set<String> ssMac3 = new HashSet<>();
                        Set<String> ssMac4 = new HashSet<>();

                        if ((form_details.getWifi_mac1() != null) && (!form_details.getWifi_mac1().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1());
                            String status =mm.get("statusInWifiMacTable");
                            if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac1(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac1.add(form_details.getWifi_mac1());
                                    }
                                });
                            }
                        }
                        System.out.println("mac1:::::::::::" + ssMac1 + "ssMac1.size()" + ssMac1.size());
                        if ((form_details.getWifi_mac2() != null) && (!form_details.getWifi_mac2().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac2());
                             if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac2(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac2.add(form_details.getWifi_mac1());
                                    }
                                });
                            }
                        }

                        if ((form_details.getWifi_mac3() != null) && (!form_details.getWifi_mac3().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac3());
                            if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac3(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac3.add(form_details.getWifi_mac1());
                                    }
                                });

                            }
                        }

                        //------------added by mohit sharma---------------------------------------
                        if ((form_details.getWifi_mac4() != null) && (!form_details.getWifi_mac4().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac4());
                             if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac4(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac4.add(form_details.getWifi_mac1());
                                    }
                                });

                            }
                        }

                        int countOfUserWithMac1 = ssMac1.size();
                        int countOfUserWithMac2 = ssMac2.size();
                        int countOfUserWithMac3 = ssMac3.size();
                        int countOfUserWithMac4 = ssMac4.size();

                        System.out.println("countOfUserWithMac1" + countOfUserWithMac1);
                        Set<String> set = llp.stream().filter(x -> (Collections.frequency(llp, x) > 1)).collect(Collectors.toList()).stream().collect(Collectors.toSet());
                        macCountduplicate = set.size();
                        System.out.println("macCountduplicate:" + macCountduplicate + " | total mac size:" + macCount);

                        if (macCountduplicate > 1) {
                            for (String str : set) {
                                if (str.equalsIgnoreCase(form_details.getWifi_mac1())) {
                                    error.put("mac1_error", "User having duplicate mac address");
                                } else if (str.equalsIgnoreCase(form_details.getWifi_mac2())) {
                                    error.put("mac2_error", "User having duplicate mac address");
                                } else if (str.equalsIgnoreCase(form_details.getWifi_mac3())) {
                                    error.put("mac3_error", "User having duplicate mac address");
                                } else if (str.equalsIgnoreCase(form_details.getWifi_mac4())) {
                                    error.put("mac4_error", "User having duplicate mac address");
                                } else {
                                    error.put("mac1_error", "User having duplicate mac address in previous requests");
                                    error.put("mac2_error", "User having duplicate mac address in previous requests");
                                    error.put("mac3_error", "User having duplicate mac address in previous requests");
                                    error.put("mac4_error", "User having duplicate mac address in previous requests");
                                }
                            }
                        } else if (countOfUserWithMac1 > 0) {
                            System.out.println("if count of user with mac1:::::::" + countOfUserWithMac1);
                            error.put("mac1_error", "Mac address already assigned to a user");
                        } else if (countOfUserWithMac2 > 0) {
                            error.put("mac2_error", "Mac address already assigned to a user");
                        } else if (countOfUserWithMac3 > 0) {
                            error.put("mac3_error", "Mac address already assigned to a user");
                        } else if (countOfUserWithMac4 > 0) {
                            error.put("mac4_error", "Mac address already assigned to a user");
                        }
                    }

                }
            }
            profile_values = (HashMap) session.get("profile-values");
            String user_bo = userdata.getUserBo();
            String dept = profile_values.get("dept").toString();
            session.put("dept", dept);
            System.out.println("user_bo:::" + user_bo + "dept" + dept);

            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            // start, code added by pr on 23rdjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for wifi controller tab 1: " + error);
            // end, code added by pr on 23rdjan18
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    System.out.println("is empty");
                    profile_values = (HashMap) session.get("profile-values");

                    //    payal agarwal
                    if (form_details.getWifi_process().equals("req_delete")) {
                        wifiservice.saveDeleteReq(mac_list2);
                    } else {
                        wifiservice.Wifi_tab1(form_details);
                    }
                    session.put("wifi_details", form_details);
                }
            } else {
                System.out.println("is empty else");
                data = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for wifi controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            CSRFRandom = "";
            action_type = "";
            form_details = null;
        }
        return SUCCESS;
    }

    public String Wifi_tab2() {
        try {
            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setWifi_type("guest");
            if (!form_details.getWifi_type().equals("guest")) {
                form_details.setWifi_type(ESAPI.encoder().encodeForHTML(form_details.getWifi_type()));
            }
            if (form_details.getWifi_duration().equals("days")) {
                boolean tduration_error = valid.numericdayValidation(form_details.getWifi_time(), "day");
                if (tduration_error == true) {
                    error.put("tduration_error", "Enter Time Duration [Numeric only], upto 90 days only");
                    form_details.setWifi_time(ESAPI.encoder().encodeForHTML(form_details.getWifi_time()));
                }
            } else if (form_details.getWifi_duration().equals("months")) {
                boolean tduration_error = valid.numericValidation(form_details.getWifi_time(), "months");
                if (tduration_error == true) {
                    //error.put("tduration_error", "Enter Time Duration [Numeric only]");
                    error.put("tduration_error", "Enter Time Duration [Numeric only], upto 3 months only");
                    form_details.setWifi_time(ESAPI.encoder().encodeForHTML(form_details.getWifi_time()));
                }
            } else {
                //error.put("tduration_error", "Enter Time Duration [Numeric only], upto 3 months only");
                form_details.setWifi_duration(ESAPI.encoder().encodeForHTML(form_details.getWifi_duration()));
            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            // start, code added by pr on 23rdjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // end, code added by pr on 23rdjan18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for wifi controller tab 2: " + error);
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    wifiservice.Wifi_tab2(form_details);
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("wifi_details", form_details);
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for wifi controller tab 2: " + e.getMessage());
            e.printStackTrace();
            data = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String Wifi_tab3() {
        String wifi_type = "";
        if (getAction_type().equals("update")) {
            wifi_type = session.get("wifi_type").toString();
            System.out.println("wift typeeeeeeeeee" + wifi_type);
        } else {
            form_details = (FormBean) session.get("wifi_details");
            wifi_type = form_details.getWifi_type();
        }
        try {
            db = new Database();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setWifi_type(wifi_type);
             String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

            profile_values = (HashMap) session.get("profile-values");
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }
            System.out.println("form_details.getWifi_process()" + form_details.getWifi_process());
            if (form_details.getWifi_type().equals("nic")) {
                if (form_details.getWifi_process().equals("certificate")) {

                } //payal agarwal 24-0419
                // start//
                else if (form_details.getWifi_process().equals("req_delete")) {
                } //end// 
                else {
                    // changes by ravinder
//                    Set<String> setAliasesForDevice = userdata.getAliases();
                    String commaSeperatedStringOfAliases = userdata.getAliasesInString();
                    List<String> ccOfMac = wifiservice.countOfMacWithThisUser(commaSeperatedStringOfAliases);
                    List<String> llp = new ArrayList<>();
                    ccOfMac.stream().forEach(p -> {
                        llp.add(p);
                    });
                // ---added by rahul, manikant Dec 2021--
                List deletedMacFromTable=new ArrayList();
                String userEmail= userdata.getEmail();
                deletedMacFromTable =wifiservice.featchMacFromWifiMacTable(userEmail);
                int macCountFromMacTable=deletedMacFromTable.size(); 
                //----------
                    boolean mac1_error = valid.macvalidation(form_details.getWifi_mac1());
                    boolean os1_error = valid.osValidation(form_details.getWifi_os1());
                    boolean device_type1_error = valid.osValidation(form_details.getDevice_type1());

                    if (!form_details.getWifi_type().equals("nic")) {
                        error.put("wifitype_err", "Incorrect option");
                        form_details.setWifi_type(ESAPI.encoder().encodeForHTML(form_details.getWifi_type()));
                    }
                    if (mac1_error == true) {
                        error.put("mac1_error", "Enter Valid MAC Address");
                        form_details.setWifi_mac1(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac1()));
                    } else {
                        llp.add(form_details.getWifi_mac1());
                    }

                    if (device_type1_error) {
                        error.put("device_type1_error", "Please choose correct device");
                        form_details.setDevice_type1(ESAPI.encoder().encodeForHTML(form_details.getDevice_type1()));
                    }

                    if (!form_details.getWifi_mac2().equals("")) {
                        boolean mac2_error = valid.macvalidation(form_details.getWifi_mac2());
                        if (mac2_error == true) {
                            error.put("mac2_error", "Enter Valid MAC Address");
                            form_details.setWifi_mac2(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac2()));
                        } else {
                            llp.add(form_details.getWifi_mac2());
                        }
                    }

                    if (!form_details.getDevice_type2().equals("")) {
                        boolean Device_type2_error = valid.osValidation(form_details.getDevice_type2());
                        if (Device_type2_error) {
                            error.put("Device_type2_error", "Please choose correct device");
                            form_details.setDevice_type2(ESAPI.encoder().encodeForHTML(form_details.getDevice_type2()));
                        }
                    }

                    if (!form_details.getWifi_mac3().equals("")) {
                        boolean mac3_error = valid.macvalidation(form_details.getWifi_mac3());
                        if (mac3_error == true) {
                            error.put("mac3_error", "Enter Valid MAC Address");
                            form_details.setWifi_mac3(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac3()));
                        } else {
                            llp.add(form_details.getWifi_mac3());
                        }
                    }

                    if (!form_details.getDevice_type3().equals("")) {
                        boolean Device_type3_error = valid.osValidation(form_details.getDevice_type3());
                        if (Device_type3_error) {
                            error.put("Device_type3_error", "Please choose correct device");
                            form_details.setDevice_type3(ESAPI.encoder().encodeForHTML(form_details.getDevice_type3()));
                        }
                    }

                    //-------------------------------------mohit sharma----------------------------------------------------------------------
                    if (!form_details.getWifi_mac4().equals("")) {
                        boolean mac4_error = valid.macvalidation(form_details.getWifi_mac4());
                        if (mac4_error == true) {
                            error.put("mac4_error", "Enter Valid MAC Address");
                            form_details.setWifi_mac4(ESAPI.encoder().encodeForHTML(form_details.getWifi_mac4()));
                        } else {
                            llp.add(form_details.getWifi_mac4());
                        }
                    }

                    if (!form_details.getDevice_type4().equals("")) {
                        boolean Device_type4_error = valid.osValidation(form_details.getDevice_type4());
                        if (Device_type4_error) {
                            error.put("Device_type4_error", "Please choose correct device");
                            form_details.setDevice_type4(ESAPI.encoder().encodeForHTML(form_details.getDevice_type4()));
                        }
                    }

                    //       below lines added by ravinder
                    int macCount = llp.size();
                    System.out.println("macCount:" + macCount);
                    int macCountduplicate;
                    macCount = macCount-macCountFromMacTable;   //added by rahul, manikant Dec 2021

                    if (macCount > 4) {
                        error.put("mac1_error", "A user can not have more than 4 Mac address");
                        error.put("mac2_error", "A user can not have more than 4 Mac address");
                        error.put("mac3_error", "A user can not have more than 4 Mac address");
                        error.put("mac4_error", "A user can not have more than 4 Mac address");
                        System.out.println("mac can not be assigned to the user as he/she already has 4 mac");
                    } else {
                        Set<String> ssMac1 = new HashSet<>();
                        Set<String> ssMac2 = new HashSet<>();
                        Set<String> ssMac3 = new HashSet<>();
                        Set<String> ssMac4 = new HashSet<>();

                        if ((form_details.getWifi_mac1() != null) && (!form_details.getWifi_mac1().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                           Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1());
                           String status =mm.get("statusInWifiMacTable");
                            if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac1(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac1.add(form_details.getWifi_mac1());
                                    }
                                });
                            }
                        }
                        System.out.println("mac1:::::::::::" + ssMac1 + "ssMac1.size()" + ssMac1.size());
                        if ((form_details.getWifi_mac2() != null) && (!form_details.getWifi_mac2().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac2());
                             if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac2(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac1.add(form_details.getWifi_mac1());
                                    }
                                });
                            }
                        }

                        if ((form_details.getWifi_mac3() != null) && (!form_details.getWifi_mac3().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac3());
                             if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac3(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac1.add(form_details.getWifi_mac1());
                                    }
                                });

                            }
                        }

                        //------------------------------------added by mohit sharma--------------------------------
                        if ((form_details.getWifi_mac4() != null) && (!form_details.getWifi_mac4().equals(""))) {
//                            ssMac1 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac1(), commaSeperatedStringOfAliases);
                            Map<String, String> mm = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac4());
                             if (mm.size() > 0 && !(mm.containsValue("deleted"))) {
                                mm.entrySet().stream().forEach(ent -> {
                                    String myDateString = ent.getKey();
                                    Set<String> aliasesForMac = ldap.fetchAliases(ent.getValue());

                                    int sizeOfAliasesForMac = aliasesForMac.size();
                                    String commaSeparatedAliasesForMac = "";
                                    if (sizeOfAliasesForMac > 1) {
                                        for (String email : aliasesForMac) {
                                            commaSeparatedAliasesForMac += "'" + email + "',";
                                        }
                                        commaSeparatedAliasesForMac = commaSeparatedAliasesForMac.replaceAll(",$", "");
                                    } else if (sizeOfAliasesForMac == 1) {
                                        commaSeparatedAliasesForMac = "'" + aliasesForMac.iterator().next() + "'";
                                    }

                                    if (wifiservice.confirmMacFromDeleteTable(form_details.getWifi_mac4(), myDateString, commaSeparatedAliasesForMac)) {
                                        ssMac1.add(form_details.getWifi_mac1());
                                    }
                                });

                            }
                        }
                        //----------------------------------------end by mohit shrama--------------------------------------------------

//                        if ((form_details.getWifi_mac2() != null) && (!form_details.getWifi_mac2().equals(""))) {
//                            ssMac2 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac2(), commaSeperatedStringOfAliases);
//                        }
//                        if ((form_details.getWifi_mac3() != null) && (!form_details.getWifi_mac3().equals(""))) {
//                            ssMac3 = wifiservice.countOfUserWithThisMac(form_details.getWifi_mac3(), commaSeperatedStringOfAliases);
//                        }
                        int countOfUserWithMac1 = ssMac1.size();
                        int countOfUserWithMac2 = ssMac2.size();
                        int countOfUserWithMac3 = ssMac3.size();
                        int countOfUserWithMac4 = ssMac4.size();

                        System.out.println("countOfUserWithMac1" + countOfUserWithMac1);
                        Set<String> set = llp.stream().filter(x -> (Collections.frequency(llp, x) > 1)).collect(Collectors.toList()).stream().collect(Collectors.toSet());
                        macCountduplicate = set.size();
                        System.out.println("macCountduplicate:" + macCountduplicate + " | total mac size:" + macCount);

                        if (macCountduplicate > 1) {
                            for (String str : set) {
                                if (str.equalsIgnoreCase(form_details.getWifi_mac1())) {
                                    error.put("mac1_error", "User having duplicate mac address");
                                } else if (str.equalsIgnoreCase(form_details.getWifi_mac2())) {
                                    error.put("mac2_error", "User having duplicate mac address");
                                } else if (str.equalsIgnoreCase(form_details.getWifi_mac3())) {
                                    error.put("mac3_error", "User having duplicate mac address");
                                } else if (str.equalsIgnoreCase(form_details.getWifi_mac4())) {
                                    error.put("mac4_error", "User having duplicate mac address");
                                } else {
                                    error.put("mac1_error", "User having duplicate mac address in previous requests");
                                    error.put("mac2_error", "User having duplicate mac address in previous requests");
                                    error.put("mac3_error", "User having duplicate mac address in previous requests");
                                    error.put("mac4_error", "User having duplicate mac address in previous requests");
                                }
                            }
                        } else if (countOfUserWithMac1 > 0) {
                            System.out.println("if count of user with mac1:::::::" + countOfUserWithMac1);
                            error.put("mac1_error", "Mac address already assigned to a user");
                        } else if (countOfUserWithMac2 > 0) {
                            error.put("mac2_error", "Mac address already assigned to a user");
                        } else if (countOfUserWithMac3 > 0) {
                            error.put("mac3_error", "Mac address already assigned to a user");
                        } else if (countOfUserWithMac4 > 0) {
                            error.put("mac4_error", "Mac address already assigned to a user");
                        }
                    }

                    if (os1_error == true) {
                        error.put("os1_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                        form_details.setWifi_os1(ESAPI.encoder().encodeForHTML(form_details.getWifi_os1()));
                    }

                    if (!form_details.getWifi_os2().equals("")) {
                        boolean os2_error = valid.osValidation(form_details.getWifi_os2());
                        if (os2_error == true) {
                            error.put("os2_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                            form_details.setWifi_os2(ESAPI.encoder().encodeForHTML(form_details.getWifi_os2()));
                        }
                    }
                    if (!form_details.getWifi_os3().equals("")) {
                        boolean os3_error = valid.osValidation(form_details.getWifi_os3());
                        if (os3_error == true) {
                            error.put("os3_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                            form_details.setWifi_os3(ESAPI.encoder().encodeForHTML(form_details.getWifi_os3()));
                        }
                    }

                    if (!form_details.getWifi_os4().equals("")) {
                        boolean os4_error = valid.osValidation(form_details.getWifi_os4());
                        if (os4_error == true) {
                            error.put("os4_error", "Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]");
                            form_details.setWifi_os4(ESAPI.encoder().encodeForHTML(form_details.getWifi_os4()));
                        }
                    }
                }
                //profile_values = (HashMap) session.get("profile-values");
                // String user_bo = session.get("user_bo").toString();
                String dept = "";

                if (form_details.getDept().equals("Department of Electronics and Information Technology (Deity), MyGov Project")) {
                    dept = profile_values.get("dept").toString();
                } else {
                    dept = form_details.getDept();
                }
                session.put("dept", dept);

            } else if (form_details.getWifi_type().equals("guest")) {
                if (form_details.getWifi_duration().equals("days")) {
                    boolean tduration_error = valid.numericdayValidation(form_details.getWifi_time(), "day");
                    if (tduration_error == true) {
                        error.put("tduration_error", "Enter Time Duration [Numeric only], upto 90 days only");
                        form_details.setWifi_time(ESAPI.encoder().encodeForHTML(form_details.getWifi_time()));
                    }
                } else if (form_details.getWifi_duration().equals("months")) {
                    boolean tduration_error = valid.numericValidation(form_details.getWifi_time(), "months");
                    if (tduration_error == true) {
                        //error.put("tduration_error", "Enter Time Duration [Numeric only]");
                        error.put("tduration_error", "Enter Time Duration [Numeric only], upto 3 months only");
                        form_details.setWifi_time(ESAPI.encoder().encodeForHTML(form_details.getWifi_time()));
                    }
                } else {
                    error.put("wifiduration_error", "Enter Time Duration [Numeric only], upto 3 months only");
                    form_details.setWifi_duration(ESAPI.encoder().encodeForHTML(form_details.getWifi_duration()));
                }
            } else {
                error.put("wifiduration_error", "Enter Time Duration [Numeric only], upto 3 months only");
                form_details.setWifi_type(ESAPI.encoder().encodeForHTML(form_details.getWifi_type()));
            }
            // start, code added by pr on 23rdjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            // validate profile 20th march
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
                } else // check for nic domain
                if (form_details.getHod_email().equals(form_details.getUser_email())) {
                    error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                } else {
                    // Map a = (HashMap) session.get("profile-values");
                    //ArrayList b = null;
                    //if (a.get("mailequi") != null && !a.get("mailequi").equals("") && !a.get("mailequi").toString().trim().equals("[]")) {
                    // b = (ArrayList) a.get("mailequi");
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
                        } else //String user_bo = session.get("user_bo").toString();
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
                    } else {

                        // String user_bo = session.get("user_bo").toString();
                        String dept = profile_values.get("dept").toString();
                        session.put("dept", dept);
                        //System.out.println("user_bo:::" + user_bo + "dept" + dept);
                        //System.out.println("form_details.getDept()".getDept());
                        if (!nic_employee && !dept.contains("MyGov Project")) {

                            String e = form_details.getHod_email();
                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in imap controller tab 2");
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in imap controller tab 2");
                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                            }
                        } else {
                            // DONE BY NIKKI MEENAXI ON 16 feb 
                            // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for wifi controller tab 3: " + error);
            // end, code added by pr on 23rdjan18
            if (getAction_type().equals("confirm")) {
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    int wifi_value = 0;
                    //profile_values = (HashMap) session.get("profile-values");
                    if (!form_details.getWifi_process().equals("req_delete")) {

                        if (ldap_employee) {
                            // ldap user
                            // get bo of user
                            userValues = (HashMap) profileService.getHODdetails(profile_values.get("email").toString());
                            String ldapWIFIvalue = entities.LdapQuery.fetchwifivalue(profile_values.get("email").toString());
                            System.out.println("ldap: " + ldapWIFIvalue);
                            if (ldapWIFIvalue.equals("error")) {
                                String bo = userValues.get("bo").toString();
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Bo for user " + bo);
                                Map bocheck = new HashMap();
                                String[] token = wifi_values.split("~");
                                for (String wifi_token : token) {
                                    String[] final_token = wifi_token.split(":");
                                    bocheck.put(final_token[0], final_token[1]);
                                }
                                if (bocheck.containsKey(bo)) {
                                    wifi_value = Integer.parseInt(bocheck.get(bo).toString());
                                } else {
                                    wifi_value = Integer.parseInt(bocheck.get("ministry").toString());
                                }
                            } else {
                                wifi_value = Integer.parseInt(ldapWIFIvalue);
                            }
                        }
                        form_details.setWifi_bo(wifi_value);
                        System.out.println("wifi_value:::::::" + wifi_value);
                        // if(userValues.get(""))
                        // String user_bo = session.get("user_bo").toString();
                        String dept = profile_values.get("dept").toString();
                        // System.out.println("user bo::::::::::::::" + user_bo);
                        if (dept.contains("MyGov Project")) {
                            System.out.println("inside mygov");
                            wifi_value = Integer.parseInt("7");
                        }
                    }
                    session.put("wifi_details", form_details);

                    //payal agarwal 24-0419
                    // start//
                    String ref_num = null;
                    if (session.get("update_without_oldmobile").equals("no")) {
                        if (form_details.getWifi_process().equals("req_delete")) {

                            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA : ");
                            userdata = (UserData) session.get("uservalues");
                            System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                            String email = userdata.getEmail();

                            ref_num = wifiservice.Wifi_tab3(form_details, wifi_value, profile_values);

                            mac_list2 = (List<FormBean>) session.get("maclist2");
                            form_details.setMac_list(mac_list2);
                            for (FormBean formBean : mac_list2) {
                                System.out.println("inside foreachhhhhhhhhhhh");
                                formBean.setRegistration_no(ref_num);
                                formBean.setUser_email(email);
                                wifiservice.Wifi_mac_os(formBean);
                            }

                        } //end// 
                        else {

                            System.out.println("my device ravinder:" + form_details.getDevice_type1());
                            ref_num = wifiservice.Wifi_tab3(form_details, wifi_value, profile_values);
                        }

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
                    //user_bo = session.get("user_bo").toString();
                    if (!nic_employee) {
                        profile_values.put("fwd_ofc_name", form_details.getFwd_ofc_name().trim());
                        profile_values.put("fwd_ofc_mobile", form_details.getFwd_ofc_email().trim());
                        profile_values.put("fwd_ofc_email", form_details.getFwd_ofc_email().trim());
                        profile_values.put("fwd_ofc_tel", form_details.getFwd_ofc_tel().trim());
                        profile_values.put("fwd_ofc_desig", form_details.getFwd_ofc_design().trim());
                        profile_values.put("fwd_ofc_add", form_details.getFwd_ofc_add().trim());
                    }
                    session.put("profile-values", profile_values);
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    profile_values = (HashMap) session.get("profile-values");
                    //String user_bo = session.get("user_bo").toString();
                    String dept = profile_values.get("dept").toString();
                    session.put("dept", dept);
                    // System.out.println("user_bo:::" + user_bo + "dept" + dept);
                    //System.out.println("form_details.getDept()".getDept());
                    if (!nic_employee && !dept.contains("MyGov Project")) {
                        //if (!user_bo.equals("NIC Employees")) {
                        profile_values.put("fwd_ofc_name", form_details.getFwd_ofc_name().trim());
                        profile_values.put("fwd_ofc_mobile", form_details.getFwd_ofc_mobile().trim());
                        profile_values.put("fwd_ofc_email", form_details.getFwd_ofc_email().trim());
                        profile_values.put("fwd_ofc_tel", form_details.getFwd_ofc_tel().trim());
                        profile_values.put("fwd_ofc_desig", form_details.getFwd_ofc_design().trim());
                        profile_values.put("fwd_ofc_add", form_details.getFwd_ofc_add().trim());
                    }
                    session.put("profile-values", profile_values);
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for wifi controller tab 3: " + e.getMessage());
            e.printStackTrace();
            data = "";
            CSRFRandom = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String checkbo() {
        userValues = (HashMap) profileService.getHODdetails(email);
        String bo = userValues.get("bo").toString();
        if (bo.equals("NIC Employees")) {
            error.put("bocheck", "true");
        } else {
            error.put("bocheck", "false");
        }

        return SUCCESS;
    }

    //Payal Agarwal   11-04-19
    public void fetchApplicantData() {
        String json = null;
        try {

            UserData userdata = (UserData) session.get("uservalues");
            Set<String> aliases = new HashSet<>();
            List<FormBean> formBeans = new ArrayList();
            List<String> formBean1 = new ArrayList();
            aliases = userdata.getAliases();
            formBeans = wifiservice.fetchApplicantData(aliases);
            formBean1 = wifiservice.getDeletedWifiList(aliases);
            for (FormBean formBean : formBeans) {
                if (formBean.getWifi_mac1() != null && !formBean.getWifi_mac1().equalsIgnoreCase("")) {
                    System.out.println("first loop" + formBean.getWifi_mac1());
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac1());
                    newFormBean.setWifi_os1(formBean.getWifi_os1());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());
                    mac_list3.add(newFormBean);

                }
                if (formBean.getWifi_mac2() != null && !formBean.getWifi_mac2().equalsIgnoreCase("")) {
                    System.out.println("second loop" + formBean.getWifi_mac2());
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac2());
                    newFormBean.setWifi_os1(formBean.getWifi_os2());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());

                    mac_list3.add(newFormBean);

                }
                if (formBean.getWifi_mac3() != null && !formBean.getWifi_mac3().equalsIgnoreCase("")) {
                    System.out.println("third loop");
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac3());
                    newFormBean.setWifi_os1(formBean.getWifi_os3());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());

                    mac_list3.add(newFormBean);
                }

                //----------------added by mohit sharma ----------------------------------------
                if (formBean.getWifi_mac4() != null && !formBean.getWifi_mac4().equalsIgnoreCase("")) {
                    System.out.println("forth loop");
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac4());
                    newFormBean.setWifi_os1(formBean.getWifi_os4());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());

                    mac_list3.add(newFormBean);
                }
            }

            mac_list = new ArrayList(mac_list3);
            if (formBean1.size() > 0) {
                for (FormBean fb : mac_list3) {
                    for (int i = 0; i < formBean1.size(); i++) {
                        if (fb.getWifi_mac1().equalsIgnoreCase(formBean1.get(i))) {
                            mac_list.remove(fb);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json = new Gson().toJson(mac_list);
        ServletActionContext.getResponse().setContentType("application/json");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(Wifi_registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("listttttttttt" + mac_list);

    }

    public void fetchApplicantPendingData() {
        String json = null;
        try {

            UserData userdata = (UserData) session.get("uservalues");
            Set<String> aliases = new HashSet<>();
            List<FormBean> formBeans = new ArrayList();
            List<String> formBean1 = new ArrayList();
            aliases = userdata.getAliases();
            formBeans = wifiservice.fetchApplicantPendingData(aliases);
            formBean1 = wifiservice.getDeletedWifiList(aliases);
            for (FormBean formBean : formBeans) {
                if (formBean.getWifi_mac1() != null && !formBean.getWifi_mac1().equalsIgnoreCase("")) {
                    System.out.println("first loop" + formBean.getWifi_mac1());
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac1());
                    newFormBean.setWifi_os1(formBean.getWifi_os1());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());
                    mac_list3.add(newFormBean);

                }
                if (formBean.getWifi_mac2() != null && !formBean.getWifi_mac2().equalsIgnoreCase("")) {
                    System.out.println("second loop" + formBean.getWifi_mac2());
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac2());
                    newFormBean.setWifi_os1(formBean.getWifi_os2());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());

                    mac_list3.add(newFormBean);

                }
                if (formBean.getWifi_mac3() != null && !formBean.getWifi_mac3().equalsIgnoreCase("")) {
                    System.out.println("third loop");
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac3());
                    newFormBean.setWifi_os1(formBean.getWifi_os3());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());

                    mac_list3.add(newFormBean);
                }
                //----------------added by mohit sharma ----------------------------------------

                if (formBean.getWifi_mac4() != null && !formBean.getWifi_mac4().equalsIgnoreCase("")) {
                    System.out.println("forth loop");
                    FormBean newFormBean = new FormBean();
                    newFormBean.setWifi_mac1(formBean.getWifi_mac4());
                    newFormBean.setWifi_os1(formBean.getWifi_os4());
                    newFormBean.setRegistration_no(formBean.getRegistration_no());

                    mac_list3.add(newFormBean);
                }
            }

            mac_list = new ArrayList(mac_list3);
//            if (formBean1.size() > 0) {
//                for (FormBean fb : mac_list3) {
//                    for (int i = 0; i < formBean1.size(); i++) {
//                        if (fb.getWifi_mac1().equalsIgnoreCase(formBean1.get(i))) {
//                            mac_list.remove(fb);
//                        }
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json = new Gson().toJson(mac_list);
        ServletActionContext.getResponse().setContentType("application/json");
        try {
            ServletActionContext.getResponse().getWriter().write(json);
        } catch (IOException ex) {
            Logger.getLogger(Wifi_registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("listttttttttt" + mac_list);

    }
}
