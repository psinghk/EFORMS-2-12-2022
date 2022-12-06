/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import java.util.ArrayList;

/**
 *
 * @author SHIVAM
 */
public class Test1 {
    
    
    public static void main(String[] args) {  
        
         
        
//        public void TestRabbit(){
//            String stat_form_type ="wifi";
//            String emailMessage ="",emailSubject="",smsMessage="" ,templateId="",emailTo="" ;
//            
//         if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) // if and else around added by pr on 14thmar18
//            {
//                // send mail and sms to the user 
////                ArrayList<String> userText = wifiMailText(stat_reg_no);
////
////                if (userText.get(0) != null) {
////                    emailMessage = userText.get(0).toString();
////                }
////
////                if (userText.get(1) != null) {
////                    emailSubject = userText.get(1).toString();
////                }
////
////                if (userText.get(2) != null) {
////                    smsMessage = userText.get(2).toString();
////                }
////
////                if (userText.get(3) != null) {
////                    templateId = userText.get(3).toString();
////                }
//
//                //emailTo = userEmail;
//                 
//                String smsMobile = "";
//                 String stat_reg_no="";
//                String wifi_process = "",wifi_value= ""; // line added by pr on 18thdec18
//
//                HashMap<String, String> wifiHM = fetchwifiDetail(stat_reg_no);
//
//                // below if added by pr on 18thdec18
//                if (!wifiHM.get("wifi_process").equals("")) {
//                    wifi_process = wifiHM.get("wifi_process").toString();
//                }
//                 if (!wifiHM.get("wifi_value").equals("")) {
//                    wifi_value = wifiHM.get("wifi_value").toString();
//                }    
//                
//                if (!emailTo.equals("")) {
//
//                    String[] attachFiles = null; // line added by pr on 18thdec18
//
//                    if (wifi_process.equals("request")) // if around added by pr on 18thdec18 in case of certficate no need to attach files
//                    {
//                        attachFiles = new String[5];
//
//                        String prefix = " "; // to be uncommented in production
//
//                        attachFiles[0] = prefix + "NAM_installation_procedure_for_Windows_laptops.pdf";
//
//                        attachFiles[1] = prefix + "Wireless_Escalation_Matrix.xls";
//
//                        attachFiles[2] = prefix + "procedure_configure_android.pdf";
//                        attachFiles[3] = prefix + "procedure_configure_iOS_LDAP.pdf";
//                        attachFiles[4] = prefix + "procedure_configure_lumia.pdf";
//                         for (String s : attachFiles) {
//                           System.out.println(" inside  attachfiles  loop " + s);
//                        }
//
//                    }
//                       // String wifi_value = fetch_wifi_value(stat_reg_no);
//
//                        /*if (wifi_value.equals("2")) {
//                            attachFiles[3] = prefix + "procedure_configure_iOS_VPNCA.pdf";
//                        } else */// code commented by pr on 24thapr19 as per the mail by wifi team to make it same as others
//                        
//                        try {
//
//                        //sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
//                        //Mail.sendMailWithCc(  emailTo,  cc, emailMessage, emailSubject,  from);
//                        System.out.println(" before send mail with attach function ");
//
//                        //Mail.sendMailWithAttachOnly( from, emailTo, emailSubject, emailMessage, attachFiles );
//                        // above line commented below added  by pr on 23rdoct18
//                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, attachFiles, templateId); // line modified by pr on 21staug2020
//
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " send mail with attach exception " + e.getMessage());
//
//                        e.printStackTrace();
//                    }
//
//                }
//
////                if (!smsMobile.equals("")) {
////                    //sendSMS(smsMessage, smsMobile);
////
////                    sendToRabbit("", "", "", "", smsMessage, smsMobile, null, null); // line modified by pr on 23rdoct18                                                        
//// 
////                }
//            }
//        }
        
    }
    
    
    
}
