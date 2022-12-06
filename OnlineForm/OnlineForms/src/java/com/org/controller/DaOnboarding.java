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
import com.opensymphony.xwork2.inject.Container;
import com.org.bean.UserData;
import com.org.dao.DaOnboardingDao;
import com.org.dao.Ldap;
import com.org.dao.esignDao;

import com.org.service.ProfileService;
import com.org.service.UpdateService;

import java.util.HashMap;
import java.util.Map;
import model.FormBean;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import validation.validation;

import com.org.service.DaOnboardingService;
import entities.LdapQuery;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Gyan
 */
public class DaOnboarding extends ActionSupport implements SessionAware {

    
    FormBean form_details;
    Map session;
    Map profile_values = null;
    Map<String, String> VpnNumberDetails = new HashMap<>();
    UpdateService updateService = null;// line added by pr on 1stfeb18
    String data= "", cert ="", CSRFRandom ="", app_url ="", returnString =""; // CSRFRandom added by pr on 22ndjan18
    ProfileService profileService = null;
    Map hodDetails = null;
    String boName;
    String[] coordEmails;
    Vpn_registration vpnRegistration;

    Map<String, String> map = null;

    validation valid = null;
    Map<String, Object> error = null;
    String action_type;
    esignDao esignDao;

    DaOnboardingService daOnboardingService;

    public DaOnboarding() {

        vpnRegistration = new Vpn_registration();
        valid = new validation();
        error = new HashMap();
        updateService = new UpdateService();
        map = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        profile_values = new HashMap();
        daOnboardingService = new DaOnboardingService();
        esignDao = new esignDao();
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

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public String getReturnString() {
        return returnString;
    }

    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
    }

    public UpdateService getUpdateService() {
        return updateService;
    }

    public void setUpdateService(UpdateService updateService) {
        this.updateService = updateService;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

   

    public String[] getCoordEmails() {
    return coordEmails;
    }

    public void setCoordEmails(String[] coordEmails) {
        this.coordEmails = coordEmails;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

  public ProfileService getProfileService() {
        return profileService;
    }

    public void setProfileService(ProfileService profileService) {
        this.profileService = profileService;
    }

    public Map getHodDetails() {
        return hodDetails;
    }

    public void setHodDetails(Map hodDetails) {
        this.hodDetails = hodDetails;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public validation getValid() {
        return valid;
    }

    public void setValid(validation valid) {
        this.valid = valid;
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
        this.action_type = action_type;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public void setSession(Map session) {
        this.session = session;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

//    public String fetchBoName() {
//        profile_values = (HashMap) session.get("profile-values");
//        String employment = profile_values.get("user_employment").toString();
//         String ministry ="";
//         String department ="";
//         System.out.println("in bo name");
//          if (employment.equals("Central") || employment.trim().equals("UT")) {
//                               ministry = profile_values.get("min").toString();
//                                department= profile_values.get("dept").toString();
//                            } else if (employment.trim().equals("State")) {
//                                department= profile_values.get("dept").toString();
//                                ministry = profile_values.get("stateCode").toString();
//                            } else if (employment.trim().equals("Others") || employment.trim().equals("Psu") || employment.trim().equals("Const") || employment.trim().equals("Nkn") || employment.trim().equals("Project")) {
//                                ministry = profile_values.get("Org").toString();
//                            }
//       
//        System.out.println("in profile_values = " +profile_values);
//        String departmentName = daOnboardingService.fetchBoName(employment, ministry, department);
//       // String departmentName = daOnboardingService.fetchBo((String)profile_values.get("email"));
//        session.put("daonboarding_boname", departmentName);
//        boName = departmentName;
//        DaOnboardingDao daonboardingdao = new DaOnboardingDao();
//
//        HashSet<String> hs = daonboardingdao.getCoordEmail((HashMap) profile_values);
//       if(hs.isEmpty()||hs.size()==0){
//            hs.add("support@gov.in");
//                     
//        }
//        coordEmails = hs.toArray(new String[hs.size()]);
//        return SUCCESS;
//
//    }

    /*  public String getVpnIp() {
        String getAllDetails = "";

        try {
            getAllDetails = vpnRegistration.post_api_vpnnumbers();
            api_response = getAllDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return "DataNotFound";
        }

        return getAllDetails;
    }
     */
    public String daOnboardin_tab1() {
        System.out.println("inside of Daonboarding tab 1 ");
        try {
            error.clear();
            Ldap ldap = new Ldap();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            System.out.println("value of checkbox in daonbording= " + form_details.daon_mobile);
            //form_details.setCSRFRandom(CSRFRandom);
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("profile values are ::::: " + profile_values);
            String email = profile_values.get("email").toString();
            System.out.println("email in onboarding tab 1" + email);
            //String BoName = fetchBoName();
            String fetchedBo = ldap.fetchBo(email);
            session.put("daonboarding_boname", fetchedBo);
            form_details.setBoName(session.get("daonboarding_boname").toString());
            String BoName = form_details.getBoName();

            System.out.println("BOName is ::::::::::" + BoName);
            System.out.println((UserData) session.get("uservalues"));

            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
                System.out.println("in csrf ");
            }

            List<String> vpnNo = form_details.getDa_vpn_reg_no();
            String vpn_number = String.join(",", vpnNo);
            vpn_number = vpn_number.replaceAll(",", "").trim();
            //List<String> vpn_number = form_details.getDa_vpn_reg_no();
            
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
          //commented by mohit sharma 20/sep/2021
            /*
            if (vpn_number.isEmpty() || vpn_number == null) {
                error.put("vpn_error", "VPN Number Is Empty");
            } else if (error.isEmpty()) {
                com.org.controller.Vpn_registration vpn_registration = new com.org.controller.Vpn_registration();
                String vpn_api_response = vpn_registration.post_api_vpnnumbers(session);
                if (vpn_api_response.contains("\"ipaddress\":\"" + vpn_number + "\"")) {
                    session.put("daonboarding_vpn_reg_no", vpn_number);
                }
            }
         */
            
            if (!form_details.getEligibility().equals("department") && !form_details.getEligibility().equals("psu")) {
                error.put("cap_error", "Please enter correct eligibility");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }
            
            System.out.print("vpn no :::::::::::::::::::::::::" + form_details.getVpn_reg_no() + "=" + vpn_number);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception for onboarding controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            CSRFRandom = "";
            returnString = "";
            action_type = "";
        }
        return SUCCESS;
    }

    public String daOnboardin_tab2() {
        Vpn_registration post_api_vpnnumbers = new Vpn_registration();
        System.out.println("this is methord callin on submit ");

        System.out.println("inside of daonboardig tab 2 ::::::::::: ");
        try {

            error.clear();

            System.out.print("vpnNUmber MAP ::::::: " + VpnNumberDetails);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            System.out.print("data = :::::::::::::::::::::::::::::::::::::::::::" + data);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setBoName((String) session.get("daonboarding_boname"));
            form_details.setVpn_reg_no((String) session.get("daonboarding_vpn_reg_no"));
            System.out.println("SESSIONNNNN" + session.get("uploaded_filename"));
            System.out.println("value of checkbox in daonbording:::::::::  ::: :: : = " +form_details.daon_mobile);

            System.out.println("the value of eligibility is::::::::: ---- ::::::::  = "+form_details.getEligibility());
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }
            if (getAction_type().equals("validate")) {
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

                String SessionCSRFRandom = session.get("CSRFRandom").toString();
                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                    System.out.println("in csrf ");
                }

                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            }

            if (getAction_type().equals("submit")) {
                if (session.get("uploaded_filename") != null) {
                    String uploaded_filename = session.get("uploaded_filename").toString();
                    String renamed_filepath = session.get("renamed_filepath").toString();
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                } else {
                    String uploaded_filename = "";
                    String renamed_filepath = "";
                    form_details.setUploaded_filename(uploaded_filename);
                    form_details.setRenamed_filepath(renamed_filepath);
                }
                System.out.println("before if " + !error.isEmpty());

                String SessionCSRFRandom = session.get("CSRFRandom").toString();
                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                    System.out.println("in csrf ");
                }

                if (!error.isEmpty()) {
                    data = "";
                    cert = "";
                    CSRFRandom = "";
                    action_type = "";
                } else {
                    System.out.println("in else " + !error.isEmpty());
                    System.out.print("session values:::::::: " + session.get("profile-values"));
                    profile_values = (HashMap) session.get("profile-values");
                    System.out.println("profile values::::::::: in ldap1" + profile_values);

                    session.put("daonboarding_details", form_details);

                    //insert into databases;
                    String ref_num = daOnboardingService.daOnboarding_tab(form_details, profile_values);

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
                    session.put("da_onboarding", "true");
                    session.put("da_onboarding_2", "true");
                    //dao
                    //generate
                }
            } else if (getAction_type().equals("validate")) {
                if (!error.isEmpty()) {
                    data = "";
                    cert = "";
                    CSRFRandom = "";
                    action_type = "";
                }
            } else {
                data = "";
                cert = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in ldap controller tab 1: " + e.getMessage());
            e.printStackTrace();
            data = "";
            cert = "";
            CSRFRandom = "";
            action_type = "";

        }
        return SUCCESS;
    }

}
