 
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
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.dao.VpnDao;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import com.org.service.VpnService;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.FormBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author nikki
 */
public class Vpn_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    public String action_type, ip_type, vpn_form_type;
    Map session;
    VpnService vpnservice = null;
    String returnString, returnString1, returnString2, returnString3, returnString4, vpn_reg_no;
    ;
    Map<String, Object> consentreturn = null;
    UpdateService updateService = null;// line added by pr on 6thfeb18
    ProfileService profileService = null;
    Map hodDetails = null;
    String vpn_ip, delete_chk_value;
    Map vpn_data = null;
    String[] deleted_value;
    Map<String, Object> error = null;
    validation valid = null;
    Map profile_values = null;
    String data, cert;
    String field_id, field_data;

    public Vpn_registration() {
        vpnservice = new VpnService();
        vpn_data = new HashMap();
        hodDetails = new HashMap();
        profileService = new ProfileService();
        error = new HashMap<>();
        valid = new validation();
        updateService = new UpdateService();
        profile_values = new HashMap();
        consentreturn = new HashMap<>();
        if (vpnservice == null) {
            vpnservice = new VpnService();
        }
        if (vpn_data == null) {
            vpn_data = new HashMap();
        }
        if (consentreturn == null) {
            consentreturn = new HashMap<>();
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

    public String getDelete_chk_value() {
        return delete_chk_value;
    }

    public void setDelete_chk_value(String delete_chk_value) {
        this.delete_chk_value = delete_chk_value;
    }

    public String[] getDeleted_value() {
        return deleted_value;
    }

    public void setDeleted_value(String[] deleted_value) {
        this.deleted_value = deleted_value;
    }

    public String getVpn_reg_no() {
        return vpn_reg_no;
    }

    public void setVpn_reg_no(String vpn_reg_no) {
        this.vpn_reg_no = vpn_reg_no;
    }

    public Map<String, Object> getConsentreturn() {
        return consentreturn;
    }

    public void setConsentreturn(Map<String, Object> consentreturn) {
        this.consentreturn = consentreturn;
    }

    public Map getHodDetails() {
        return hodDetails;
    }

    public void setHodDetails(Map hodDetails) {
        this.hodDetails = hodDetails;
    }

    // start, code added by pr on 23rdjan18
    String CSRFRandom;

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    public String getIp_type() {
        return ip_type;
    }

    public void setIp_type(String ip_type) {
        this.ip_type = ip_type;
    }

    public String getVpn_form_type() {
        return vpn_form_type;
    }

    public void setVpn_form_type(String vpn_form_type) {
        this.vpn_form_type = vpn_form_type;
    }

    // end, code added by pr on 23rdjan18
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

    public String fetchCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = Jsoup.parse(cert).text();
    }

    public String getReturnString1() {
        return returnString1;
    }

    public void setReturnString1(String returnString1) {
        this.returnString1 = returnString1;
    }

    public String getReturnString2() {
        return returnString2;
    }

    public void setReturnString2(String returnString2) {
        this.returnString2 = returnString2;
    }

    public String getReturnString3() {
        return returnString3;
    }

    public void setReturnString3(String returnString3) {
        this.returnString3 = returnString3;
    }

    public String getReturnString4() {
        return returnString4;
    }

    public void setReturnString4(String returnString4) {
        this.returnString4 = returnString4;
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    String api_response;

    public String getApi_response() {
        return api_response;
    }

    public void setApi_response(String api_response) {
        this.api_response = api_response;
    }

    public String getVpn_ip() {
        return vpn_ip;
    }

    public void setVpn_ip(String vpn_ip) {
        this.vpn_ip = vpn_ip;
    }

    public Map getVpn_data() {
        return vpn_data;
    }

    public void setVpn_data(Map vpn_data) {
        this.vpn_data = vpn_data;
    }

    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    public String getField_data() {
        return field_data;
    }

    public void setField_data(String field_data) {
        this.field_data = field_data;
    }
    // String url = "http://10.1.146.228/JavaAPI/rest/user/"; // testing june 2022 with telnet
    //String url = "http://10.1.145.59/JavaAPI/rest/user/"; // testing
    String url = "http://myvpnapi.nic.in/JavaAPI/rest/user/"; // for all
    //String url = "http://10.1.145.59/JavaAPI/rest/user/"; // production

    public String vpn_check_exp() {
        UserData userdata = (UserData) session.get("uservalues");
        String email = userdata.getAliasesInString().replaceAll("'", "\"");
        VpnDao dao = new VpnDao();
        boolean val = false;
        if (!email.equals("")) {
            val = dao.vpn_check_exp_reg(email, vpn_reg_no);
        }
        api_response = Boolean.toString(val);
        System.out.println(" this is the retured value in vpn check exp ::" + val);
        return SUCCESS;
    }

    public String vpn_check_surrender() {
        UserData userdata = (UserData) session.get("uservalues");
        String email = userdata.getAliasesInString().replaceAll("'", "\"");
        VpnDao dao = new VpnDao();
        boolean val = false;
        if (!email.equals("")) {
            val = dao.vpn_check_surrender_reg(email, vpn_reg_no);
        }
        api_response = Boolean.toString(val);
        System.out.println(" this is the retured value from surrender check::" + val);
        return SUCCESS;
    }

    public String vpn_fatch() {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("VPN_fatch ::" + data + "::");
        // call vpn fatch api
        try {
            System.out.println("API RESPONSE :::" + post_api(data));

            // inputstream = new ByteArrayInputStream(post_api(data).getBytes(StandardCharsets.UTF_8));
            //inputstream.reset();
            api_response = post_api(data);
        } catch (Exception e) {
            System.out.println("################" + e);
        }
        return SUCCESS;
    }

    public String vpn_fatch_renew() {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("VPN_fatch_renew ::" + data + "::");
        // call vpn fatch api
        try {
            System.out.println("API RESPONSE :::" + post_api(data));
            // inputstream = new ByteArrayInputStream(post_api(data).getBytes(StandardCharsets.UTF_8));
            //inputstream.reset();
            api_response = post_api(data);
        } catch (Exception e) {
            System.out.println("################" + e);
        }
        return SUCCESS;
    }

    public String post_api_vpnnumbers(Map session) throws MalformedURLException, IOException {
        UserData userdata = (UserData) session.get("uservalues");
        //  String url = "http://10.1.146.228/JavaAPI/rest/user/exist"; // testing
        // String url = "http://10.1.145.59/JavaAPI/rest/user/exist"; // production
        URL obj = new URL(url + "exist");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        String user_name = "eforms";
        String password = "eforms@#api78$";
        String userCredentials = "username : eforms password : eforms@#api78$";
        byte[] plainCredsBytes = userCredentials.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
        //con.setRequestProperty("Authorization", base64Creds);
        System.out.println("Inside VPN API :::::" + userdata.getAliasesInString());
        con.setRequestProperty("Authorization", "Basic ZWZvcm1zOmVmb3Jtc0AjYXBpNzgk");
        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
        con.setRequestProperty("Content-Type", "application/json");
        String email_to_api = userdata.getAliasesInString().replaceAll("'", "\"");
        String urlParameters = "{ \"email\": [" + email_to_api + "], \"mobile\": \"" + userdata.getMobile() + "\" }";
        //String urlParameters = "{ \"vpn_registration_no\": \"" + vpn_no + "\", \"email\": \"" + userdata.getEmail() + "\", \"mobile\": \"" + userdata.getMobile() + "\" }";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url + "exist");
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println("API Response from VPN Exist ::" + response.toString());
        return response.toString();
    }

    public String vpn_exists() {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("VPN_Exists ::" + data + "::");
        // call vpn fatch api
        try {
//            System.out.println("API RESPONSE :::" + post_api(data));
            // inputstream = new ByteArrayInputStream(post_api(data).getBytes(StandardCharsets.UTF_8));
            //inputstream.reset();
            api_response = post_api_vpnnumbers();
        } catch (Exception e) {
            System.out.println("################" + e);
        }
        return SUCCESS;
    }

    public String post_api_vpnnumbers() throws MalformedURLException, IOException {
        UserData userdata = (UserData) session.get("uservalues");
        //  String url = "http://10.1.146.228/JavaAPI/rest/user/exist"; // testing
        // String url = "http://10.1.145.59/JavaAPI/rest/user/exist"; // production
        URL obj = new URL(url + "exist");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        String user_name = "eforms";
        String password = "eforms@#api78$";
        String userCredentials = "username : eforms password : eforms@#api78$";
        byte[] plainCredsBytes = userCredentials.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
        //con.setRequestProperty("Authorization", base64Creds);
        System.out.println("Inside VPN API :::::" + userdata.getAliasesInString());
        con.setRequestProperty("Authorization", "Basic ZWZvcm1zOmVmb3Jtc0AjYXBpNzgk");
        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
        con.setRequestProperty("Content-Type", "application/json");
        String email_to_api = userdata.getAliasesInString().replaceAll("'", "\"");
        String urlParameters = "{ \"email\": [" + email_to_api + "], \"mobile\": \"" + userdata.getMobile() + "\" }";
        //String urlParameters = "{ \"vpn_registration_no\": \"" + vpn_no + "\", \"email\": \"" + userdata.getEmail() + "\", \"mobile\": \"" + userdata.getMobile() + "\" }";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url + "exist");
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println("API Response from VPN Exist ::" + response.toString());
        return response.toString();
    }

    public String post_api(String vpn_no) throws MalformedURLException, IOException {

        UserData userdata = (UserData) session.get("uservalues");

        // String url = "http://10.1.146.228/JavaAPI/rest/user/fetch"; // testing
        // String url = "http://10.1.145.59/JavaAPI/rest/user/fetch"; // production
        URL obj = new URL(url + "fetch");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        String user_name = "eforms";
        String password = "eforms@#api78$";
        String userCredentials = "username : eforms password : eforms@#api78$";
        byte[] plainCredsBytes = userCredentials.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
        //con.setRequestProperty("Authorization", base64Creds);
        System.out.println("#$#$#$#$#$###$#$#$:::::" + userdata.getAliasesInString());
        con.setRequestProperty("Authorization", "Basic ZWZvcm1zOmVmb3Jtc0AjYXBpNzgk");

        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
        con.setRequestProperty("Content-Type", "application/json");

        String email_to_api = userdata.getAliasesInString().replaceAll("'", "\"");
        String urlParameters = "{ \"vpn_registration_no\": \"" + vpn_no + "\", \"email\": [" + email_to_api + "], \"mobile\": \"" + userdata.getMobile() + "\" }";

        //String urlParameters = "{ \"vpn_registration_no\": \"" + vpn_no + "\", \"email\": \"" + userdata.getEmail() + "\", \"mobile\": \"" + userdata.getMobile() + "\" }";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url + "fetch");
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println("API Response from" + response.toString());

        return response.toString();
    }

//       // calling from esigDao
    public String post_api2(String vpn_no, String email, String mobile, String all_aliases) throws MalformedURLException, IOException {

        System.out.println("##############################################################################" + vpn_no + "::" + email + ":::" + mobile + "::" + all_aliases);
//       System.out.println("SESSIONN: "+ session);
//        if(session.get("uservalues")!=null){
//            System.out.println("aaaaaaaaaaaaa");
//        }else{
//            System.out.println("bbbbbbbbbbbbbbbbbb");
//        }
        // UserData userdata = (UserData) session.get("uservalues");
        //String url = "http://10.1.145.59/JavaAPI/rest/user/fetch";

        //String url = "http://10.1.146.228/JavaAPI/rest/user/fetch"; // testing
        // String url = "http://10.1.145.59/JavaAPI/rest/user/fetch"; // production
        URL obj = new URL(url + "fetch");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        String user_name = "eforms";
        String password = "eforms@#api78$";
        String userCredentials = "username : eforms password : eforms@#api78$";
        byte[] plainCredsBytes = userCredentials.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
        //con.setRequestProperty("Authorization", base64Creds);

        con.setRequestProperty("Authorization", "Basic ZWZvcm1zOmVmb3Jtc0AjYXBpNzgk");

        //con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
        con.setRequestProperty("Content-Type", "application/json");
        //String email_to_api=userdata.getAliasesInString().replaceAll("'", "\"");
        String urlParameters = "{ \"vpn_registration_no\": \"" + vpn_no + "\", \"email\": [" + all_aliases + "], \"mobile\": \"" + mobile + "\" }";
        // String urlParameters = "{ \"vpn_registration_no\": \"" + vpn_no + "\", \"email\": \"" + email + "\", \"mobile\": \"" + mobile + "\" }";

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url + "fetch");
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        System.out.println(response.toString());

        return response.toString();
    }

    public String vpn_surrender() {
        try {
            System.out.print("into vpn surrender FORM::::" + data);
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            profile_values = (HashMap) session.get("profile-values");
//            
//            UserData userdata = (UserData) session.get("uservalues");
//            boolean ldap_employee = userdata.isIsEmailValidated();
//            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
//            if (ldap_employee) {
//                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//            }
//            System.out.println("profile values#####:::::::::::" + profile_values);
//            System.out.println("vpn_coo :: " + form_details.getVpn_coo_email());
//            System.out.println("form_details.getReq_for()" + form_details.getReq_for());
            if (form_details.getReq_co() != null) {
                if (form_details.getReq_co().equals("central_co")) {
                    //System.out.println("form_details.getVpn_coo()" + form_details.getVpn_coo());
                    // if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                    if (form_details.getVpn_coo() != null) {
                        boolean vpn_coord_error = valid.EmailValidation(form_details.getVpn_coo());
                        if (vpn_coord_error == true) {
                            returnString2 = "Please select coordinator email address from dropdown";
                        } else {
                            returnString2 = "";
                        }
                    } else {
                        returnString2 = "";
                    }
//                    } else {
//                        returnString2 = "";
//                    }
//            } else {
//                returnString2 = "";
//            }
                }
                if (form_details.getReq_co().equals("state_co")) {

                    // if (!form_details.getVpn_coo1().equalsIgnoreCase("na")) {
                    //if (form_details.getVpn_coo1() != null && !form_details.getVpn_coo1().equals("")) {
                    if (form_details.getVpn_coo1() != null) {
                        form_details.setVpn_coo(form_details.getVpn_coo1());
                        System.out.println("after set vpn coo1:::::" + form_details.getVpn_coo());
                        boolean vpn_coord_error1 = valid.EmailValidation(form_details.getVpn_coo());
                        System.out.println("after set vpn coo1 vpn_coord_error:::::" + vpn_coord_error1);
                        if (vpn_coord_error1 == true) {
                            returnString3 = "Please select coordinator email address from dropdown";
                        } else {
                            returnString3 = "";
                        }
                    } else {
                        returnString3 = "";
                    }
                    // }
//                        } else {
//                            returnString3 = "";
//                        }
                }

            }

            if (form_details.getReq_for().equals("vpn_surrender")) {
                System.out.print("Surrender VPN FORM ::::");
                String generated_key = vpnservice.Vpn_Surrender(form_details, profile_values);
                session.put("gen_key", generated_key);
                session.put("ref_num", generated_key);
                session.put("vpn_details", form_details);
                session.put("form_text", "VPN RENEW");
                // 
//                esignService s= new esignService();
//                
//                consentreturn = s.consent("check", generated_key, "vpn_renew", form_details, profile_values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String vpn_delete() {
        try {
            System.out.println("ARRAY::: " + Arrays.toString(deleted_value));
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            //form_details.setVpn_reg_no(vpn_reg_no);
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("profile values#####:::::::::::" + profile_values);
//            
//            UserData userdata = (UserData) session.get("uservalues");
//            boolean ldap_employee = userdata.isIsEmailValidated();
//            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
//            if (ldap_employee) {
//                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//            }
            System.out.println("vpn_coo :: " + form_details.getVpn_coo_email());
            if (form_details.getReq_for().equals("vpn_delete")) {
//                System.out.print("Renew VPN FORM ::::");
                String generated_key = vpnservice.Vpn_delete(form_details, profile_values, deleted_value);
                session.put("gen_key", generated_key);
                session.put("ref_num", generated_key);
                session.put("vpn_details", form_details);
                session.put("form_text", "VPN DELETE");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String vpn_tab1() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            form_details = mapper.readValue(data, FormBean.class);

            System.out.println("DATA: " + data);
            System.out.println("AAA: " + vpn_ip);

            String[] vpn_type = vpn_ip.split(",");
            System.out.println("delete_chk_value@@@@@@@@" + delete_chk_value);
            String[] vpn_action_type = delete_chk_value.split(",");
            System.out.println("vpn_action_type@@@@@@@" + vpn_action_type);

            List<String> vpnip_1 = Arrays.asList(form_details.getVpn_new_ip1());
            List<String> vpnip_2 = Arrays.asList(form_details.getVpn_new_ip2());
            List<String> vpnip_3 = Arrays.asList(form_details.getVpn_new_ip3());
            List<String> vpnapp_url = Arrays.asList(form_details.getVpn_app_url());
            List<String> vpndest_port = Arrays.asList(form_details.getVpn_dest_port());
            List<String> vpnserver_loc = Arrays.asList(form_details.getVpn_server_loc());
            List<String> vpnserver_loctxt = Arrays.asList(form_details.getVpn_server_loc_txt());

            System.out.println("VPN 1: " + vpnip_1);
            System.out.println("VPN 2: " + vpnip_2);
            System.out.println("VPN 3: " + vpnip_3);
            System.out.println("VPN APP: " + vpnapp_url);
            System.out.println("VPN PORT: " + vpndest_port);
            System.out.println("VPN SER: " + vpnserver_loc);
            System.out.println("VPN SER LOC: " + vpnserver_loctxt);
            vpn_data.clear();
            for (int i = 0; i < vpn_type.length; i++) {
                Map div_error = new LinkedHashMap();
                boolean new_ip1_err = false, new_ip2_err = false, new_ip3_err = false, dest_port_err = false;
                if (vpn_type[i].equals("single")) {
                    new_ip1_err = valid.vpnValidation(vpnip_1.get(i));
                    if (new_ip1_err) {
                        div_error.put("new_ip1_err", "Enter IP Address [e.g: 10.10.10.10]");
                    }
                } else if (vpn_type[i].equals("range")) {
                    new_ip2_err = valid.vpnValidation(vpnip_2.get(i));
                    new_ip3_err = valid.vpnValidation(vpnip_3.get(i));
                    if (new_ip2_err) {
                        div_error.put("new_ip2_err", "Enter IP Address [e.g: 10.10.10.10]");
                    }
                    if (new_ip3_err) {
                        div_error.put("new_ip3_err", "Enter IP Address [e.g: 10.10.10.10]");
                    }
                }
                if (vpnserver_loc.get(i).equals("Other")) {
                    boolean server_txt_error = valid.addValidation(vpnserver_loc.get(i));
                    if (server_txt_error == true) {
                        div_error.put("server_txt_error", "Enter Server Location [characters,dot(.) and whitespace]");
                    }
                } else {
                    boolean server_loc_error = valid.addValidation(vpnserver_loc.get(i));
                    if (server_loc_error == true) {
                        div_error.put("server_loc_error", "Enter Server Location [characters,dot(.) and whitespace]");
                    }
                }
                System.out.println("AAA UURRLRLLL:: " + vpnapp_url.get(i));
                boolean app_url_err = valid.vpnurlValidation(vpnapp_url.get(i));
                System.out.println("APP URRL ERRR::: " + app_url_err);
                if (app_url_err) {
                    div_error.put("app_url_err", "Enter Application URL [e.g: (abc.com)]");
                }

                if (vpndest_port.get(i).contains(",")) {
                    String dest_port[] = vpndest_port.get(i).split(",");
                    for (String a : dest_port) {
                        dest_port_err = valid.portValidation(a);
                        if (dest_port_err) {
                            div_error.put("dest_port_err", "Enter Destination Port [e.g: [80,443] OR [8080-8081]]");
                        }
                    }
                } else if (vpndest_port.get(i).contains("-")) {
                    String dest_port[] = vpndest_port.get(i).split("-");
                    for (String a : dest_port) {
                        dest_port_err = valid.portValidation(a);
                        if (dest_port_err) {
                            div_error.put("dest_port_err", "Enter Destination Port [e.g: [80,443] OR [8080-8081]]");
                        }
                    }
                } else {
                    dest_port_err = valid.portValidation(vpndest_port.get(i));
                    if (dest_port_err) {
                        div_error.put("dest_port_err", "Enter Destination Port [e.g: [80,443] OR [8080-8081]]");
                    }
                }

                System.out.println("AAAAAAAAAAA: " + div_error);
                if (!div_error.isEmpty()) {
                    error.put("div_" + i, div_error);
                } else {
                    Map vpn_div_data = new LinkedHashMap();
                    vpn_div_data.put("ip_type", vpn_type[i]);
                    vpn_div_data.put("ip1", vpnip_1.get(i));
                    vpn_div_data.put("ip2", vpnip_2.get(i));
                    vpn_div_data.put("ip3", vpnip_3.get(i));
                    if (vpn_type[i].equals("single")) {
                        vpn_div_data.put("ip", vpnip_1.get(i));
                    } else {
                        vpn_div_data.put("ip", vpnip_2.get(i) + " - " + vpnip_3.get(i));
                    }
                    vpn_div_data.put("app_url", vpnapp_url.get(i));
                    vpn_div_data.put("dest_port", vpndest_port.get(i));
                    vpn_div_data.put("server_loc", vpnserver_loc.get(i));
                    vpn_div_data.put("server_loc_txt", vpnserver_loctxt.get(i));
                    if (vpnserver_loc.get(i).equalsIgnoreCase("other")) {
                        vpn_div_data.put("server", vpnserver_loctxt.get(i));
                    } else {
                        vpn_div_data.put("server", vpnserver_loc.get(i));
                    }

                    try {
                        // System.out.println("vpn_action_type[i]"+vpn_action_type[i]);
                        if (vpn_action_type[i] != null) {
                            if (delete_chk_value.contains("1")) {
                                System.out.println("vpn_action_type[i]" + vpn_action_type[i]);
                                vpn_div_data.put("action_type", "Delete");
                            } else {
                                vpn_div_data.put("action_type", "Add");
                            }
                        } else {
                            vpn_div_data.put("action_type", "Add");
                        }
                    } catch (Exception e) {
                        vpn_div_data.put("action_type", "Add");
                    }

                    System.out.println("VPN :::: " + vpn_div_data);
                    vpn_data.put("div_" + i, vpn_div_data);
                    System.out.println("VPN DATA::: " + vpn_data);
                }
                System.out.println("ERROORR INLLOOPPPP::: " + error);
            }
            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                returnString = "Please enter Correct Captcha";
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            } else {
                returnString = "";
            }
//            
            //if (form_details.getVpn_coo() != null && !form_details.getVpn_coo().equals("")) {
            System.out.println("inside vpn coordinator id" + form_details.getVpn_coo());
            System.out.println("form_details.getReq_for()" + form_details.getReq_for());
            if (form_details.getReq_co() != null) {

                if (form_details.getReq_co().equals("central_co")) {
                    System.out.println("form_details.getVpn_coo()" + form_details.getVpn_coo());
                    // if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                    boolean vpn_coord_error = valid.EmailValidation(form_details.getVpn_coo());
                    if (vpn_coord_error == true) {
                        returnString2 = "Please select coordinator email address from dropdown";
                    } else {
                        returnString2 = "";
                    }
//                    } else {
//                        returnString2 = "";
//                    }
//            } else {
//                returnString2 = "";
//            }
                }
                if (form_details.getReq_co().equals("state_co")) {
                    System.out.println("form_details.getVpn_coo1()" + form_details.getVpn_coo1());
                    // if (!form_details.getVpn_coo1().equalsIgnoreCase("na")) {

                    //if (form_details.getVpn_coo1() != null && !form_details.getVpn_coo1().equals("")) {
                    form_details.setVpn_coo(form_details.getVpn_coo1());
                    System.out.println("after set vpn coo1:::::" + form_details.getVpn_coo());
                    boolean vpn_coord_error1 = valid.EmailValidation(form_details.getVpn_coo());
                    System.out.println("after set vpn coo1 vpn_coord_error:::::" + vpn_coord_error1);
                    if (vpn_coord_error1 == true) {
                        returnString3 = "Please select coordinator email address from dropdown";
                    } else {
                        returnString3 = "";
                    }
                    // }
//                        } else {
//                            returnString3 = "";
//                        }
                }

            }
            System.out.println("RETURN STRING 2:: " + returnString2);
            boolean remarks_err = valid.vpnRemarksValidation(form_details.getRemarks());
            if (remarks_err) {
                returnString1 = "Enter Remarks [Only characters,digits,whitespaces and [. , - # / ( ) ],limit[100] allowed] ";
            } else {
                returnString1 = "";
            }

            // start, code added by pr on 23rdjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }
            System.out.println("ERRROORRRR: " + error);

            // end, code added by pr on 23rdjan18
            if (error.isEmpty()) {
                if (session.get("update_without_oldmobile").equals("no")) {
                    //  int generated_key = vpnservice.Vpn_tab1(form_details);
                    //  session.put("gen_key", generated_key);
                    session.put("vpn_details", form_details);
                    profile_values = (HashMap) session.get("profile-values");
                    hodDetails = (HashMap) profileService.getHODdetails(profile_values.get("hod_email").toString());
                    String bo = hodDetails.get("bo").toString();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR Map for single controller tab 1: " + bo);
                    profile_values.put("bo_check", bo);
                    System.out.println("Profile values:::::::::::" + session.get("profile-values"));

                    System.out.println("VVVV: " + vpn_data);
                    session.put("vpn_data", vpn_data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String vpn_renew() {
        try {
            System.out.print("into vpn renew FORM::::");
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            profile_values = (HashMap) session.get("profile-values");
            System.out.println("profile values#####:::::::::::" + profile_values);

//            UserData userdata = (UserData) session.get("uservalues");
//            boolean ldap_employee = userdata.isIsEmailValidated();
//            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
//            if (ldap_employee) {
//                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//            }
            if (form_details.getReq_co() != null) {
                System.out.println("inside reqcOOOOOOOO");
                if (form_details.getReq_co().equals("central_co")) {
                    System.out.println("inside central reqcOOOOOOOO");
                    //System.out.println("form_details.getVpn_coo()" + form_details.getVpn_coo());
                    // if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                    if (form_details.getVpn_coo() != null) {
                        boolean vpn_coord_error = valid.EmailValidation(form_details.getVpn_coo());
                        if (vpn_coord_error == true) {
                            returnString2 = "Please select coordinator email address from dropdown";
                        } else {
                            returnString2 = "";
                        }
                    } else {
                        returnString2 = "";
                    }
//                    } else {
//                        returnString2 = "";
//                    }
//            } else {
//                returnString2 = "";
//            }
                }
                if (form_details.getReq_co().equals("state_co")) {
                    System.out.println("inside state reqcOOOOOOOO");
                    // if (!form_details.getVpn_coo1().equalsIgnoreCase("na")) {
                    //if (form_details.getVpn_coo1() != null && !form_details.getVpn_coo1().equals("")) {
                    if (form_details.getVpn_coo1() != null) {
                        form_details.setVpn_coo(form_details.getVpn_coo1());
                        System.out.println("after set vpn coo1:::::" + form_details.getVpn_coo());
                        boolean vpn_coord_error1 = valid.EmailValidation(form_details.getVpn_coo());
                        System.out.println("after set vpn coo1 vpn_coord_error:::::" + vpn_coord_error1);
                        if (vpn_coord_error1 == true) {
                            returnString3 = "Please select coordinator email address from dropdown";
                        } else {
                            returnString3 = "";
                        }
                    } else {
                        returnString3 = "";
                    }
                    // }
//                        } else {
//                            returnString3 = "";
//                        }
                }

            }

            //System.out.println("vpn_coo :: " + form_details.getVpn_coo_email());
            if (form_details.getReq_for().equals("vpn_renew")) {
                System.out.print("Renew VPN FORM ::::");
                String generated_key = vpnservice.Vpn_renew(form_details, profile_values);
                session.put("gen_key", generated_key);
                session.put("ref_num", generated_key);
                session.put("vpn_details", form_details);
                session.put("form_text", "VPN RENEW");
                // 
//                esignService s= new esignService();
//                
//                consentreturn = s.consent("check", generated_key, "vpn_renew", form_details, profile_values);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String vpn_tab2() {
        try {
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            profile_values = (HashMap) session.get("profile-values");
            form_details = (FormBean) session.get("vpn_details");

            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            if (ldap_employee) {
                if (hodDetails.get("mobile").toString() != null && !hodDetails.get("mobile").toString().isEmpty()) {
                    boolean hod_mobile_error = valid.MobileValidation(hodDetails.get("mobile").toString());
                    if (!hod_mobile_error) {
                        profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                        form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
                    }
                }
            }
            String req_for = "", uploaded_filename = "", renamed_filepath = "", Vpn_reg_no = "";
            if (!action_type.equals("update")) {
                req_for = form_details.getReq_for();
                uploaded_filename = form_details.getUploaded_filename();
                renamed_filepath = form_details.getRenamed_filepath();
                Vpn_reg_no = form_details.getVpn_reg_no();
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setReq_for(req_for);
            form_details.setUploaded_filename(uploaded_filename);
            form_details.setRenamed_filepath(renamed_filepath);
            form_details.setVpn_reg_no(Vpn_reg_no);
            //   System.out.println("hod email::::::"+profile_values.get("hod_email").toString()+"form hod email "+form_details.getHod_email());
            // changed on 1st september 2020 by MI because prevously code written above where form details not defined so hod email was null

            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());

            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }
            String filename = "";
            vpn_data = (HashMap) session.get("vpn_data");

//            boolean new_ip1_err = false, new_ip2_err = false, new_ip3_err = false, dest_port_err = false;
//
//            if (form_details.getIp_type().equals("single")) {
//                new_ip1_err = valid.baseipValidation(form_details.getNew_ip1());
//            } else if (form_details.getIp_type().equals("range")) {
//                new_ip2_err = valid.baseipValidation(form_details.getNew_ip2());
//                new_ip3_err = valid.baseipValidation(form_details.getNew_ip3());
//            }
//
//            if (new_ip1_err) {
//                error.put("new_ip1_err", "Enter IP Address [e.g: 10.10.10.10]");
//            }
//            if (new_ip2_err) {
//                error.put("new_ip2_err", "Enter IP Address [e.g: 10.10.10.10]");
//            }
//            if (new_ip3_err) {
//                error.put("new_ip3_err", "Enter IP Address [e.g: 10.10.10.10]");
//            }
//
//            if (form_details.getServer_loc().equals("Other")) {
//                boolean server_txt_error = valid.addValidation(form_details.getServer_loc_txt());
//                if (server_txt_error == true) {
//                    error.put("server_txt_error", "Enter Server Location [characters,dot(.) and whitespace]");
//                }
//            } else {
//                boolean server_loc_error = valid.addValidation(form_details.getServer_loc());
//                if (server_loc_error == true) {
//                    error.put("server_loc_error", "Enter Server Location [characters,dot(.) and whitespace]");
//                }
//            }
//
//            boolean app_url_err = valid.pullurlValidation(form_details.getApp_url());
//
//            if (app_url_err) {
//                error.put("app_url_err", "Enter Application URL [e.g: (https://abc.com)]");
//            }
//
//            if (form_details.getDest_port().contains(",")) {
//                String dest_port[] = form_details.getDest_port().split(",");
//                for (String a : dest_port) {
//                    dest_port_err = valid.portValidation(a);
//                    if (dest_port_err) {
//                        error.put("dest_port_err", "Enter Destination Port [e.g: 80,443]");
//                    }
//                }
//            } else {
//                dest_port_err = valid.portValidation(form_details.getDest_port());
//                if (dest_port_err) {
//                    error.put("dest_port_err", "Enter Destination Port [e.g: 80,443]");
//                }
//            }
            // start, code added by pr on 23rdjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " session csrf random value is " + SessionCSRFRandom + " form hidden csrf random is " + CSRFRandom);

            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            // end, code added by pr on 23rdjan18
            // validate profile 20th march
            /*
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
            hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
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
                    error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer's email should be same in our database");

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
                            if (email_aliases.contains(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else {
                                //String user_bo = session.get("user_bo").toString();
                                if (nic_employee && nicemployeeeditable.equals("no")) {
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
                                    //  hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
                                }
                            }
                        } else {
                            // String user_bo = session.get("user_bo").toString();
                            if (nic_employee && nicemployeeeditable.equals("no")) {
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
                                // hodDetails = (HashMap) profileService.getLdapValues(form_details.getHod_email());
                            }
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
             */
            // end validate profile 20th march                
            if (getAction_type().equals("validate")) {

                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                    if (form_details.getMin() == null) {
                        error.put("min_error", "Please Enter Ministry: ");
                    }
                    if (form_details.getDept() == null) {
                        error.put("dept_error", "Please Enter Department: ");
                    }
                } else if (form_details.getUser_employment().trim().equals("State")) {
                    if (form_details.getDept() == null) {
                        error.put("dept_error", "Please Enter Department: ");
                    }
                    if (form_details.getStateCode() == null) {
                        error.put("StateCode_error", "Please Enter State : ");
                    }
                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                    if (form_details.getOrg() == null) {
                        error.put("org_error", "Please Enter Orgnanization : ");
                    }
                }

                System.out.println("-----------------------------------------");

                //profile_values = (HashMap) session.get("profile-values");
                //session.put("vpn_details", form_details);
//                System.out.println("-----------------------------------------");
//                String ref_num = vpnservice.Vpn_tab3(form_details, filename, profile_values, vpn_data);
//                if (ref_num != null) {
                //int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                //if (i > 0) {
//                    profile_values.put("user_employment", form_details.getUser_employment().trim());
//                    if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
//                        profile_values.put("min", form_details.getMin().trim());
//                        profile_values.put("dept", form_details.getDept().trim());
//                    } else if (form_details.getUser_employment().trim().equals("State")) {
//                        profile_values.put("dept", form_details.getState_dept().trim());
//                        profile_values.put("stateCode", form_details.getStateCode().trim());
//                    } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
//                        profile_values.put("Org", form_details.getOrg().trim());
//                    }
//                    profile_values.put("other_dept", form_details.getOther_dept().trim());
//                    profile_values.put("ca_name", form_details.getHod_name().trim());
//                    profile_values.put("ca_design", form_details.getCa_design().trim());
//                    profile_values.put("ca_mobile", form_details.getHod_mobile().trim());
//                    profile_values.put("ca_email", form_details.getHod_email().trim());
//                    profile_values.put("hod_name", form_details.getHod_name().trim());
//                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
//                    profile_values.put("hod_email", form_details.getHod_email().trim());
//                    profile_values.put("hod_tel", form_details.getHod_tel().trim());
                //}
                // }
//                if (!error.isEmpty()) {
//                    data = "";
//                    CSRFRandom = "";
//                    action_type = "";
//                }
            } else if (getAction_type().equals("submit")) {
                if (!error.isEmpty()) {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                } else if (session.get("update_without_oldmobile").equals("no")) {
                    profile_values = (HashMap) session.get("profile-values");
                    session.put("vpn_details", form_details);
                    System.out.println("-----------------------------------------");
                    String ref_num = vpnservice.Vpn_tab3(form_details, filename, profile_values, vpn_data);
                    if (ref_num != null) {
                        //int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                        //if (i > 0) {
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
                        profile_values.put("ca_name", profile_values.get("hod_name").toString().trim());
                        profile_values.put("ca_design", form_details.getCa_design().trim());
                        profile_values.put("ca_mobile", profile_values.get("hod_mobile").toString().trim());
                        profile_values.put("ca_email", form_details.getHod_email().trim());
                        profile_values.put("hod_name", profile_values.get("hod_name").toString().trim());
                        profile_values.put("hod_mobile", profile_values.get("hod_mobile").toString().trim());
                        profile_values.put("hod_email", form_details.getHod_email().trim());
                        profile_values.put("hod_tel", form_details.getHod_tel().trim());
                        //}
                    }
                    session.put("ref_num", ref_num);
                }
            } else if (action_type.equals("update")) {
                String ref = session.get("ref").toString();
                String admin_role = "NA";
                if (session.get("admin_role") != null) {
                    admin_role = session.get("admin_role").toString();
                }
                Boolean fl = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                if (fl && form_details.getModule().equals("user")) {
                    // profile_values = (HashMap) session.get("profile-values");
                    System.out.println("SSSSSS: " + session);
                    System.out.println("preofile_values: " + profile_values);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String fill_profile() {

        try {
            profile_values = (HashMap) session.get("profile-values");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Profile Values for moderator tab::" + profile_values);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public String vpn_entry_edit() {
        System.out.println("1: " + field_id);
        System.out.println("2: " + field_data);
        String role = session.get("admin_role").toString();
        int i = vpnservice.vpn_entry_edit(field_id, field_data, role);
        System.out.println("iiiiiiiiiiiii: " + i);
        if (i > 0) {
            returnString = "done";

            Boolean flag = vpnservice.vpn_entry_get(field_data);
            if (flag) {
                // flag true = show approve button
                // flag false = dont show approve button
                action_type = "approve";
            } else {
                action_type = "disapprove";
            }
        } else {
            returnString = "not done";
        }
        return SUCCESS;
    }
}
