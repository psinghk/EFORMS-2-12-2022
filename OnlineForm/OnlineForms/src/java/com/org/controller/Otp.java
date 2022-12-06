package com.org.controller;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.dao.Database;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import rabbitmq.NotifyThrouhRabbitMQ;

public class Otp {

    private Database db = null;
    private String name, email, msg;
    NotifyThrouhRabbitMQ object = null;
    HashMap<String, Object> map_email = null;
    HashMap<String, Object> map_sms = null;
    private Map<String, Object> success_msg = null;

    public Map<String, Object> getSuccess_msg() {
        return success_msg;
    }

    public void setSuccess_msg(Map<String, Object> success_msg) {
        this.success_msg = success_msg;
    }

    public Otp() {
        db = new Database();
        object = new NotifyThrouhRabbitMQ();
        map_email = new HashMap<>();
        map_sms = new HashMap<>();
        if (db == null) {
            db = new Database();
        }
        if (object == null) {
            object = new NotifyThrouhRabbitMQ();
        }
        if (map_email == null) {
            map_email = new HashMap<>();
        }
        if (map_sms == null) {
            map_sms = new HashMap<>();
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    boolean sendOtpToMobile(int random, String mobile) {
        System.out.println("OTP to mobile number " + mobile);
        String msg = "Your eForms OTP for login is :" + random + ". Validity of this OTP is 30 minutes. NICSI";
        map_sms.put("smsbody", msg);
        map_sms.put("mobile", mobile);
        map_sms.put("templateId", "1107160811837372722");
        object.sendSmsOtpRabbitMq(map_sms);

        //String ack = SmsSender.sendSms(mobile, msg);
//        if(ack.toLowerCase().contains("accepted"))
//            return true;
//        else 
//            return false;
        return true;
    }

    boolean sendOtpToEmail(int random, String email) {
        System.out.println("OTP to email " + email);
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear Sir/Madam,</p>");
        sb.append("Your OTP for eForms is <b>").append(random).append("</b>.Validity of this OTP is 30 minutes.<br>");
        sb.append("<br>Regards,<br>");
        sb.append("eForms Team NIC");
        map_email.put("from", "");
        map_email.put("subject", "eForms : User Registration OTP");
        map_email.put("mailbody", sb.toString());
        map_email.put("attachmentpath", "");
        map_email.put("to", email);
        object.sendEmailOtpRabbitMq(map_email);
        //EmailSender.sendEmail(email, sb.toString(), "User Registration OTP");
        return true;
    }

    boolean isEmailOtpActive(String email) {
        return db.isEmailOtpActive(email);
    }

    boolean isMobileOtpActive(String mobile, String countryCode) {
        return db.isMobileOtpActive(mobile, countryCode);
    }

    public String resendMobile(String email, String mobile) {
        return db.resendMobile(email, mobile);
    }
//resendOtpForLogin
    public String resendEmail(String email, String mobile) {
        return db.resendEmail(email, mobile);
    }
    
    public String resendOtpForLogin(String email, String mobile) {
         return db.resendOtpForLogin(email, mobile);
    }

    public String feedback_us() {
        System.out.println("Name: " + getName() + " ::Email:: " + getEmail() + " ::Msg:: " + getMsg());
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String s = "insert into e_feedback (name, email, feedback) values(?,?,?)";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(s);
            ps.setString(1, getName());
            ps.setString(2, getEmail());
            ps.setString(3, getMsg());
            ps.executeUpdate();
            ps.close();
            // con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
}
