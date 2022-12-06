/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.utility;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
/**
 *
 * @author nikki
 */
public class SmsSender {
    private static ServletContext ctx = getServletContext();
    private static final String smshost = ctx.getInitParameter("sms-ip");
    private static final String smspass = ctx.getInitParameter("sms-pass");
    private static final String smsuser = ctx.getInitParameter("sms-uname");
    
    public static String sendSms(String mobile, String msg) {
        String pas = URLEncoder.encode(smspass);
        String mid = "";
        try {
            URL ur = new URL("http://" + smshost + "/failsafe/HttpLink?username=" + smsuser + "&pin=" + pas + "&message=" + msg + "&mnumber=" + mobile + "&signature=EFORMS");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "url is " + ur);
            InputStream respons = ur.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(respons));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Request ID=")) {
                    mid = line.substring(line.indexOf("Request ID=") + 12, line.indexOf("~"));
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "TEST: " + line + "---- MID: "+ mid);
                }                
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mid;
    }
}
