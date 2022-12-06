/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;
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
public class SMS_Sender {
    ServletContext ctx = getServletContext();
    private final String smshost = ctx.getInitParameter("sms-ip");
    private final String smspass = ctx.getInitParameter("sms-pass");
    private final String smsuser = ctx.getInitParameter("sms-uname");
    public String Sms_Sender(String mobile, String msg) {
        String pas = URLEncoder.encode(smspass);
        String mid = "";
        mobile = mobile.replace("+", "");
//        try {
//            URL ur = new URL("http://" + smshost + "/failsafe/HttpLink?username=" + smsuser + "&pin=" + pas + "&message=" + msg + "&mnumber=" + mobile + "&signature=NICSMS");
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "url is " + ur);
//            InputStream respons = ur.openStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(respons));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("Request ID=")) {
//                    mid = line.substring(line.indexOf("Request ID=") + 12, line.indexOf("~"));
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "TEST: " + line + "---- MID: " + mid);
//                }
//            }
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return mid;
    }
    //below function added by pr on 29thjun18
    public String Sms_Sender_otp(String mobile, String msg) {
        String pas = URLEncoder.encode(smspass);
        String mid = "";
        mobile = mobile.replace("+", "");
        System.out.println("mobile in sms sender:::::::" + mobile);
//        try {
//            URL ur = new URL("http://" + smshost + "/failsafe/HttpLink?username=" + smsuser + "&pin=" + pas + "&message=" + msg + "&mnumber=" + mobile + "&signature=NICSMS");
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "url is " + ur);
//            InputStream respons = ur.openStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(respons));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("Request ID=")) {
//                    mid = line.substring(line.indexOf("Request ID=") + 12, line.indexOf("~"));
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "TEST: " + line + "---- MID: " + mid);
//                }
//            }
//            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return mid;
    }
}
