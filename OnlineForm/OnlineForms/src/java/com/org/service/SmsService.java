/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.opensymphony.xwork2.ActionSupport;
import com.org.dao.SmsDao;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author Nikki
 */
public class SmsService extends ActionSupport {
    
    public SmsService() {
    }
    
    SmsDao smsdao = new SmsDao();
    
    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int SMS_tab1(Map profile_values, FormBean form_details) {
        return smsdao.SMS_tab1( profile_values, form_details);
    }

    public Map<String, Object> fill_sms_admin_tab(String otp_type, String email, String mobile) {
        return smsdao.fill_sms_admin_tab(otp_type, email, mobile);
    }

    public void SMS_tab2( Map profile_values, FormBean form_details, String generated_key) {
        smsdao.SMS_tab2( profile_values, form_details, generated_key);
    }

    public void SMS_tab3(Map profile_values, FormBean form_details, String generated_key) {
        smsdao.SMS_tab3( profile_values, form_details, generated_key);
    }

    public Map<String, Object> SMS_tab4(FormBean form_details, String generated_key) {        
        return smsdao.SMS_tab4(form_details, generated_key);
    }

    public String SMS_tab5(FormBean form_details, Map profile_values) {
       return smsdao.SMS_tab5( form_details,profile_values);
    }

    public void insertTelnet(String telnet, String ref_num) {
        smsdao.insertTelnet(telnet,ref_num);
    }

    public void insertFirewallId(String firewall_id, String ref_num) {
        smsdao.insertFirewallId(firewall_id,ref_num);
    }
    
}
