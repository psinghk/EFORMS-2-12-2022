package utility;

import com.opensymphony.xwork2.ActionContext;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.connections.SingleApplicationContext;
import com.org.connections.SingleApplicationContextSlave;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.servlet.ServletContext;
import com.org.utility.Constants;
import java.util.Base64;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
import rabbitmq.NotifyThrouhRabbitMQ;

public class Inform {

    String smsip = "";
    String smspass = "";
    String smsuname = "";
    String mailhost = "";
    String mailfrom = "";

    String dbhost = "";
    String dbuser = "";
    String dbname = "";
    String dbpass = "";

    String ctxfactory = "";
    String providerurl = "";
    String binddn = "";
    String bindpass = "";

    String createdn = "";
    String createpass = "";

    String sms_dom_rate = "";

    String sms_dom_exempt_rate = "";

    String sms_inter_rate = "";

    String sms_inter_exempt_rate = "";

    String nicsi_email = "";

    String sup_email = "";// line added by pr on 2ndjan18

    // start, code added by pr on 6thnov18
    String vpn_sup_email = "";

    String wifi_sup_email = "";

    // end, code added by pr on 6thnov18
    Map sessionMap;

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null; // this line added by pr on 15thjun18
    Connection conSlave = null; // this line added by pr on 15thjun18

    NotifyThrouhRabbitMQ rabbitObj; // line added by pr on 23rdoct18 for RabbitMQ integration    

    public Inform() {
        ServletContext ctx = getServletContext();

        smsip = ctx.getInitParameter("sms-ip");
        smspass = ctx.getInitParameter("sms-pass");
        smsuname = ctx.getInitParameter("sms-uname");
        mailhost = ctx.getInitParameter("send-mail");
        mailfrom = ctx.getInitParameter("send-mail-from");

        dbhost = ctx.getInitParameter("db-host");
        dbuser = ctx.getInitParameter("db-user");
        dbname = ctx.getInitParameter("db-name");
        dbpass = ctx.getInitParameter("db-pass");

        ctxfactory = ctx.getInitParameter("ctx-factory");
        providerurl = ctx.getInitParameter("provider-url");
        binddn = ctx.getInitParameter("bind-dn");
        bindpass = ctx.getInitParameter("bind-pass");

        createdn = ctx.getInitParameter("createdn");
//        createpass = ctx.getInitParameter("createpass");

        Base64.Decoder decoder = Base64.getDecoder();
        createpass = new String(
                decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));

        sms_dom_rate = ctx.getInitParameter("sms_dom_rate");

        sms_dom_exempt_rate = ctx.getInitParameter("sms_dom_exempt_rate");

        sms_inter_rate = ctx.getInitParameter("sms_inter_rate");

        sms_inter_exempt_rate = ctx.getInitParameter("sms_inter_exempt_rate");

        nicsi_email = ctx.getInitParameter("nicsi_email");

        sup_email = ctx.getInitParameter("sup_email");

        // start, code added by pr on 6thnov18
        vpn_sup_email = ctx.getInitParameter("vpn_sup_email");

        wifi_sup_email = ctx.getInitParameter("wifi_sup_email");

        // end, code added by pr on 6thnov18
        // start, code added  by pr on 23rdoct18 for rabbitMQ integration
        try {
            if (rabbitObj == null) {
                System.out.println(" CONSTRUCTOR inside sendtorabbit function rabbitobj null ");

                rabbitObj = new NotifyThrouhRabbitMQ();
            } else {
                System.out.println(" CONSTRUCTOR inside sendtorabbit function rabbitobj NOT null ");
            }
        } catch (Exception e) {
            System.out.println("  CONSTRUCTOR inside Inform constructor catch " + e.getMessage());
        }

        // end, code added  by pr on 23rdoct18 for rabbitMQ integration
    }

    // below function added by pr on 23rdoct18 for RabbitMQ integration
    public void sendToRabbit(String from, String to, String msg, String sub, String sms, String mobile, String[] cc, String[] attach, String templateId) {

        if (to.equalsIgnoreCase("vpnsupport@nic.in") || to.equalsIgnoreCase("vpnsupport@gov.in") || to.trim().toLowerCase().contains("vpnsupport"))// added by pr on 18thaug2020, modified on 20thaug2020
        {
            sms = "";
            mobile = "";
        }

        try {
            if (rabbitObj == null) {

                //System.out.println(" inside sendtorabbit function rabbitobj null ");
                rabbitObj = new NotifyThrouhRabbitMQ();
            } else {

                //System.out.println(" inside sendtorabbit function rabbitobj NOT null ");
            }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " inside sendtorabbit function  rabbitobj not null from is " + from + " to is " + to + " msg " + msg + " sub " + sub + " sms " + sms + " mobile " + mobile);

            HashMap<String, Object> map = new HashMap<String, Object>();

            if (from != null && !from.equals("")) {
                map.put("from", from);
            }

            if (to != null && !to.equals("")) {
                map.put("to", to);
            }

            if (msg != null && !msg.equals("")) {
                map.put("mailbody", msg);
            }

            if (sub != null && !sub.equals("")) {
                map.put("subject", sub);
            }

            if (sms != null && !sms.equals("")) {
                map.put("smsbody", sms);
            }

           if (mobile == null || mobile.isEmpty()) {
                mobile = "";
            }
            //Validation validation = new Validation();
//            if (!validation.checkFormat("mobile", mobile)) {
//                mobile = "";
//            }
            if (mobile.startsWith("+")) {
                mobile = mobile.substring(1);
            }

            if (mobile != null && !mobile.equals("")) {
                map.put("mobile", mobile);
            }

            if (templateId != null && !templateId.equals("")) {
                map.put("templateId", templateId);
            }
                if (cc != null && cc.length > 0) {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + "CC : : : : : " + cc);

                for (String c : cc) {

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                            + " inside cc for loop cc email is " + c);

                }

                //ArrayList ccList = (ArrayList) Arrays.asList(cc);
                ArrayList<String> ccList = new ArrayList<String>(Arrays.asList(cc)); // above line commented this uncommented by pr on 8thnov18 for cc

                map.put("cc", ccList);
            }

            if (attach != null && attach.length > 0) {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + "Attachment is there ::::: " + attach);

                for (String c : attach) {

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                            + " inside attach for loop path is " + c);

                }

                //List<String> attachList = Arrays.asList(attach); 
                ArrayList<String> attachList = new ArrayList<String>(Arrays.asList(attach)); // above line commented this uncommented by pr on 6thnov18 for attachment

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + " inside inform before calling rabbit notify attachList class " + attachList.getClass().getName());

                map.put("attachmentpath", attachList);
            }

            rabbitObj.notify(map);

        } catch (Exception e) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " inside sendtorabbit function EXCEPTIONNNNNNNNNNNNN rabbitobj not null from is " + from + " to is " + to + " msg " + msg + " sub " + sub + " sms " + sms + " mobile " + mobile);

            e.printStackTrace();
        }

    }

    public Map getSession() {
        return sessionMap;
    }

    public void setSession(Map sessionMap) {
        this.sessionMap = sessionMap;
    }

    public String fetchSessionRole() {
        String role = "";
        if (ActionContext.getContext().getSession() != null) {
            this.sessionMap = ActionContext.getContext().getSession();
            role = sessionMap.get("admin_role").toString();
        }
        return role;
    }

    // below function added by pr on 7thmar18
//    public String fetchSessionEmail() {
//        String email = "";
//
//        /*if (ActionContext.getContext().getSession() != null) {
//            this.sessionMap = ActionContext.getContext().getSession();
//
//            if (sessionMap.get("admin_email") != null) {
//                email = sessionMap.get("admin_email").toString();
//            }
//        }*/
//        // above code commented below added by pr on 26thfeb18
//        String loggedin_email = "";
//
//        if (sessionMap != null && sessionMap.get("loggedin_email") != null) {
//            loggedin_email = sessionMap.get("loggedin_email").toString();
//
//            //System.out.println(" loggedin_email value is "+loggedin_email);
//        }
//
//        email = loggedin_email;
//
//        return email;
//    }
    // above function modified by pr on 14thjun19
    public String fetchSessionEmail() {
        String email = "";
        if (sessionMap != null && sessionMap.get("uservalues") != null) {
            UserData userdata = (UserData) sessionMap.get("uservalues");
            email = userdata.getEmail();
        }
        return email;
    }

    // below function added by pr on 23rdmar18
    public String fetch_wifi_value(String stat_reg_no) {
        String wifi_value = "";

        PreparedStatement ps = null;

        ResultSet res = null;

        //Connection con = null;
        try {
            // con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry = " SELECT wifi_value FROM wifi_registration WHERE registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            res = ps.executeQuery();
            while (res.next()) {
                wifi_value = res.getString("wifi_value");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return wifi_value;

    }

//    public String sendCompIntimation(String stat_reg_no, String stat_form_type, String uids, String password) {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendCompIntimation func in inform file ");
//
//        String isEmailSent = "false";
//
//        String isSMSSent = "false";
//
//        String from = "eforms@nic.in";
//
//        String emailMessage = "", emailTo = "", emailSubject = "", smsMessage = "", smsMobile = "", userEmail = "", userMobile = "", by = "", to = "", templateId ="";
//        
//        // get user info
//        ArrayList<String> userArr = fetchUserComDetail(stat_reg_no, stat_form_type);
//
//        if (userArr.get(0) != null) {
//            userEmail = userArr.get(0);
//        }
//
//        if (userArr.get(1) != null) {
//            userMobile = userArr.get(1);
//        }
//
//        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD))// completed
//        {
//
//            // send mail and sms to the user 
//            ArrayList userText = fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
//                    password, userMobile, "");
//
//            if (userText.get(0) != null) {
//                emailMessage = userText.get(0).toString();
//            }
//
//            if (userText.get(1) != null) {
//                emailSubject = userText.get(1).toString();
//            }
//
//            if (userText.get(2) != null) {
//                smsMessage = userText.get(2).toString();
//            }
//            if (userText.get(3) != null) {
//                templateId = userText.get(3).toString();
//            }
//
//            emailTo = userEmail;
//
//            smsMobile = userMobile;
//
//            if (!emailTo.equals("")) {
//
//                String[] attachFiles = new String[1];
//
//                //attachFiles[0] = "/opt/tmp/sms.txt";
//                //attachFiles[0] = "/opt/apache-tomcat-9.0.0.M22/webapps/OnlineForms/assets/upload/sms.txt"; // line modified by pr on 25thjan18
//                //attachFiles[0] = "/tmp/sms.txt"; 
//                String[] cc = {"preeti.nhq@nic.in"};
//
//                try {
//
//                    //sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
//                    //Mail.sendMailWithCc(  emailTo,  cc, emailMessage, emailSubject,  from);
//                    //Mail.sendMail(emailTo,emailSubject, emailSubject,  from) ;
//                    // above lines commented below added by pr on 23rdoct18
//                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);// send sms is given below
//
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " send mail with attach exception " + e.getMessage());
//                }
//
//            }
//
//            if (!smsMobile.equals("")) {
//
//                //sendSMS(smsMessage, smsMobile); // line commented by pr on 23rdoct18
//            }
//
//            try {
//
//                sendMailToNICSI(stat_reg_no, userEmail);
//            } catch (Exception e) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendMailToNICSI exception " + e.getMessage());
//            }
//
//        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) // send email and sms for these forms. modified on 18thdec17 for gem
//        {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside single nkn gem else if ");
//
//            // send mail and sms to the user 
//            ArrayList userText = fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
//                    password, userMobile, "");
//
//            if (userText.get(0) != null) {
//                emailMessage = userText.get(0).toString();
//            }
//
//            if (userText.get(1) != null) {
//                emailSubject = userText.get(1).toString();
//            }
//
//            if (userText.get(2) != null) {
//                smsMessage = userText.get(2).toString();
//            }
//            if (userText.get(3) != null) {
//                templateId = userText.get(3).toString();
//            }
//
//            emailTo = userEmail;
//
//            smsMobile = userMobile;
//
//            if (!emailTo.equals("")) {
//
//                /*String[] attachFiles = new String[1];
//
//                attachFiles[0] = "/tmp/single.txt";// line modified on 21stdec17
//
//                //attachFiles[0] = "/tmp/sms.txt"; 
//                String[] cc = {"preeti.nhq@nic.in"};
//
//                try {
//                    sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " send mail exception " + e.getMessage());
//                }*/
//                // above code commented below added by pr on 25thjan18
//                //sendMail(emailTo, emailMessage, emailSubject, from);
//                // above line commented below added by pr on 23rdoct18
//                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);// modified by pr on 21staug2020
//            }
//
////            if (!smsMobile.equals("")) {
////
////                //sendSMS(smsMessage, smsMobile);
////                // above line commented below added by pr on 23rdoct18
////                sendToRabbit("", "", "", "", smsMessage, smsMobile, null, null);
////
////            }
//        } else // send email and sms for all other forms
//        {
//
//            if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) // if and else around added by pr on 14thmar18
//            {
//                // send mail and sms to the user 
//                ArrayList<String> userText = wifiMailText(stat_reg_no);
//
//                if (userText.get(0) != null) {
//                    emailMessage = userText.get(0).toString();
//                }
//
//                if (userText.get(1) != null) {
//                    emailSubject = userText.get(1).toString();
//                }
//
//                if (userText.get(2) != null) {
//                    smsMessage = userText.get(2).toString();
//                }
//
//                if (userText.get(3) != null) {
//                    templateId = userText.get(3).toString();
//                }
//
//                emailTo = userEmail;
//
//                smsMobile = userMobile;
//
//                String wifi_process = ""; // line added by pr on 18thdec18
//
//                HashMap<String, String> wifiHM = fetchwifiDetail(stat_reg_no);
//
//                // below if added by pr on 18thdec18
//                if (!wifiHM.get("wifi_process").equals("")) {
//                    wifi_process = wifiHM.get("wifi_process").toString();
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
//                        String prefix = "/eForms/WIFI/"; // to be uncommented in production
//
//                        attachFiles[0] = prefix + "NAM_installation_procedure_for_Windows_laptops.pdf";
//
//                        attachFiles[1] = prefix + "Wireless_Escalation_Matrix.xls";
//
//                        attachFiles[2] = prefix + "procedure_configure_android.pdf";
//
//                        String wifi_value = fetch_wifi_value(stat_reg_no);
//
//                        /*if (wifi_value.equals("2")) {
//                            attachFiles[3] = prefix + "procedure_configure_iOS_VPNCA.pdf";
//                        } else */// code commented by pr on 24thapr19 as per the mail by wifi team to make it same as others
//                        {
//                            attachFiles[3] = prefix + "procedure_configure_iOS_LDAP.pdf";
//                        }
//
//                        attachFiles[4] = prefix + "procedure_configure_lumia.pdf";
//
//                        for (String s : attachFiles) {
//                            //System.out.println(" inside  attachfiles  loop " + s);
//                        }
//
//                    }
//
//                    try {
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
//            } else {
//
//                // send mail and sms to the user 
//                ArrayList userText = fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
//                        password, userMobile, "");
//
//                if (userText.get(0) != null) {
//                    emailMessage = userText.get(0).toString();
//                }
//
//                if (userText.get(1) != null) {
//                    emailSubject = userText.get(1).toString();
//                }
//
//                if (userText.get(2) != null) {
//                    smsMessage = userText.get(2).toString();
//                }
//                if (userText.get(3) != null) {
//                    templateId = userText.get(3).toString();
//                }
//
//                emailTo = userEmail;
//
//                smsMobile = userMobile;
//
//                /*if (!emailTo.equals("")) {
//                    sendMail(emailTo, emailMessage, emailSubject, from);
//                }
//
//                if (!smsMobile.equals("")) {
//                    sendSMS(smsMessage, smsMobile);
//                }*/
//                // above code commented below line added by pr on 23rdoct18
//                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//
//            }
//
//        }
//
//        // start, code common for all forms
//        //************** send mail and sms to the ca*****************
//        // get ca details email and mobile for this reg_no
//        ArrayList adminText = null;
//
//        ArrayList arrID = null;
//
//        ArrayList fetchRolesByRegNo = fetchRolesByRegNo(stat_reg_no); // line added by pr on 27thfeb18
//
//        System.out.println("********* inside sendcompintimation function rolesbyregno arraylist contains " + fetchRolesByRegNo);
//
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " before wifi and bulk ");
//
//        // if (!stat_form_type.equals(Constants.WIFI_FORM_KEYWORD) && !stat_form_type.equals(Constants.BULK_FORM_KEYWORD))// vpn condition deleted by pr on 5thjan18 
//        {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside not wifi and bulk  uids is  " + uids + " password is " + password);
//
//            if (fetchRolesByRegNo.contains("ca")) // if role contains ca then send mail and sms to ca
//            {
//
//                adminText = fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
//                        password, userMobile, "Reporting/Nodal/Forwarding Officer");// for admin
//                {
//                    emailMessage = adminText.get(0).toString();
//                }
//
//                if (adminText.get(1) != null) {
//                    emailSubject = adminText.get(1).toString();
//                }
//
//                if (adminText.get(2) != null) {
//                    smsMessage = adminText.get(2).toString();
//                }
//                
//                if (adminText.get(3) != null) {
//                    templateId = adminText.get(3).toString();
//                }
//
//                arrID = fetchAdminId(stat_reg_no, "ca");// return 0 -> email and 1 -> mobile
//
//                if (arrID != null && arrID.size() > 0 && arrID.get(0) != null) { // size added by pr on 21stfeb19
//                    emailTo = arrID.get(0).toString();
//                }
//
//                if (arrID != null && arrID.size() > 1 && arrID.get(1) != null) { // size added by pr on 21stfeb19
//                    smsMobile = arrID.get(1).toString();
//                }
//
//                /*if (!emailTo.equals("")) {
//
//                    sendMail(emailTo, emailMessage, emailSubject, from);
//                }
//
//                if (!smsMobile.equals("")) {
//
//                    sendSMS(smsMessage, smsMobile);
//                }*/
//                // above code commented below line added by pr on 23rdoct18
//                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//
//            }
//
//        }
//
//        if (fetchRolesByRegNo.contains("s")) // if role contains support then send mail to support
//        {
//
//            // send mail  to the support
//            adminText = fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
//                    password, userMobile, "Support");// for admin
//
//            if (adminText.get(0) != null) {
//                emailMessage = adminText.get(0).toString();
//            }
//
//            if (adminText.get(1) != null) {
//                emailSubject = adminText.get(1).toString();
//            }
//
//            if (adminText.get(2) != null) {
//                smsMessage = adminText.get(2).toString();
//            }
//            if (adminText.get(3) != null) {
//                templateId = adminText.get(3).toString();
//            }
//
//            /*emailTo = "support@nic.in";
//
//            emailTo = "preeti.nhq@nic.in";*/
//            emailTo = sup_email;// above code modified by pr on 2ndjan18
//
//            if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) // if added by pr on 23rdfeb18
//            {
//                emailTo = "smssupport@gov.in";
//            }
//
//            if (!emailTo.equals("") && !stat_form_type.equals(Constants.DNS_FORM_KEYWORD)
//                    && !stat_form_type.equals(Constants.WIFI_FORM_KEYWORD) && !(stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD)
//                    || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD)))// restrict this code for dns form
//            {
//                //sendMail(emailTo, emailMessage, emailSubject, from);
//
//                // above code commented below line added by pr on 23rdoct18
//                sendToRabbit(from, emailTo, emailMessage, emailSubject, "", "", null, null, templateId);
//
//            }
//
//        }
//
//        if (fetchRolesByRegNo.contains("c")) // if role contains coordinator then send mail and sms to coordinator
//        {
//            System.out.println("************* inside sendcompintimation function getroles contains c ");
//
//            adminText = fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
//                    password, userMobile, "Coordinator");// for admin
//
//            if (adminText.get(0) != null) {
//                emailMessage = adminText.get(0).toString();
//            }
//
//            if (adminText.get(1) != null) {
//                emailSubject = adminText.get(1).toString();
//            }
//
//            if (adminText.get(2) != null) {
//                smsMessage = adminText.get(2).toString();
//            }
//            if (adminText.get(3) != null) {
//                templateId = adminText.get(3).toString();
//            }
//
//            // if added by pr on 29thdec17, deleted by pr on 5thjan18 for vpn form
//            if (!stat_form_type.equals(Constants.DNS_FORM_KEYWORD) && !stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) // if not dns form then execute below else go to else
//            {
//
//                // get coordinator details email and mobile for this reg_no
//                arrID = fetchAdminId(stat_reg_no, "c");// return 0 -> email and 1 -> mobile
//
//                if (arrID != null && arrID.get(0) != null) {
//                    emailTo = arrID.get(0).toString();
//                }
//
//                if (arrID != null && arrID.get(1) != null) {
//                    smsMobile = arrID.get(1).toString();
//                }
//
//                /*if (!emailTo.equals("")) {
//
//                    sendMail(emailTo, emailMessage, emailSubject, from);
//                }
//
//                if (!smsMobile.equals("")) {
//
//                    sendSMS(smsMessage, smsMobile);
//                }*/
//                // above code commented below line added by pr on 23rdoct18
//                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//
//            } else {
//
//                arrID = fetchAdminId(stat_reg_no, "c");
//
//                if (arrID != null && arrID.get(0) != null) {
//
//                    if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.lang.String"))) {
//
//                        emailTo = "";
//
//                        smsMobile = "";
//
//                        emailTo = arrID.get(0).toString();
//
//                        if (arrID.get(1) != null) {
//                            smsMobile = arrID.get(1).toString();
//                        }
//
//                        // send mail and sms only to one coordinators
//                        /*if (!emailTo.equals("")) {
//
//                            sendMail(emailTo, emailMessage, emailSubject, from);
//                        }
//
//                        if (!smsMobile.equals("")) {
//
//                            sendSMS(smsMessage, smsMobile);
//                        }*/
//                        // above code commented below line added by pr on 23rdoct18
//                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//
//                    } else if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.util.HashMap"))) {
//
//                        for (Object hm : arrID)// contains the hashmap of all email and mobile 
//                        {
//
//                            emailTo = "";
//
//                            smsMobile = "";
//
//                            HashMap h = (HashMap) hm;
//
//                            if (h != null) {
//
//                                if (h.get("email") != null) {
//                                    emailTo = h.get("email").toString();
//                                }
//
//                                if (h.get("mobile") != null) {
//                                    smsMobile = h.get("mobile").toString();
//                                }
//
//                                // send mail and sms only to all  coordinators one after another
//                                /*if (!emailTo.equals("")) {
//
//                                    sendMail(emailTo, emailMessage, emailSubject, from);
//                                }
//
//                                if (!smsMobile.equals("")) {
//
//                                    sendSMS(smsMessage, smsMobile);
//                                }*/
//                                // above code commented below line added by pr on 23rdoct18
//                                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//
//                            }
//
//                        }
//
//                    }
//                }
//
//            }
//
//        }
//
//        if (fetchRolesByRegNo.contains("m")) // if role contains mail-admin then send mail and sms to mail-admin
//        {
//            // get mail-admin details email and mobile for this reg_no
//            adminText = fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
//                    password, userMobile, "Admin");// for admin
//            {
//                emailMessage = adminText.get(0).toString();
//            }
//
//            if (adminText.get(1) != null) {
//                emailSubject = adminText.get(1).toString();
//            }
//
//            if (adminText.get(2) != null) {
//                smsMessage = adminText.get(2).toString();
//            }
//            
//            if (adminText.get(3) != null) {
//                templateId = adminText.get(3).toString();
//            }
//
//            arrID = fetchAdminId(stat_reg_no, "m");// return 0 -> email and 1 -> mobile
//
//            if (arrID != null && arrID.get(0) != null) {
//
//                if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.lang.String"))) {
//
//                    emailTo = "";
//
//                    smsMobile = "";
//
//                    emailTo = arrID.get(0).toString();
//
//                    if (arrID.get(1) != null) {
//                        smsMobile = arrID.get(1).toString();
//                    }
//
//                    // send mail and sms only to one coordinators
//                    /*if (!emailTo.equals("")) {
//
//                        sendMail(emailTo, emailMessage, emailSubject, from);
//                    }
//
//                    if (!smsMobile.equals("")) {
//
//                        sendSMS(smsMessage, smsMobile);
//                    }*/
//                    // above code commented below line added by pr on 23rdoct18
//                    if (emailTo.equalsIgnoreCase("vpnsupport@nic.in")) // code added by pr on 18thaug2020
//                    {
//                        smsMessage = "";
//                        smsMobile = "";
//                    }
//
//                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//
//                } else if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.util.HashMap"))) {
//
//                    for (Object hm : arrID)// contains the hashmap of all email and mobile 
//                    {
//
//                        emailTo = "";
//
//                        smsMobile = "";
//
//                        HashMap h = (HashMap) hm;
//
//                        if (h != null) {
//
//                            if (h.get("email") != null) {
//                                emailTo = h.get("email").toString();
//                            }
//
//                            if (h.get("mobile") != null) {
//                                smsMobile = h.get("mobile").toString();
//                            }
//
//                            // send mail and sms only to all  coordinators one after another
//                            /*if (!emailTo.equals("")) {
//
//                                sendMail(emailTo, emailMessage, emailSubject, from);
//                            }
//
//                            if (!smsMobile.equals("")) {
//
//                                sendSMS(smsMessage, smsMobile);
//                            }*/
//                            // above code commented below line added by pr on 23rdoct18
//                            if (emailTo.equalsIgnoreCase("vpnsupport@nic.in")) // code added by pr on 18thaug2020
//                            {
//                                smsMessage = "";
//                                smsMobile = "";
//                            }
//
//                            sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
//                        }
//
//                    }
//
//                }
//            }
//
//        }
//
//        // end code common for all forms
//        return isEmailSent + "," + isSMSSent;
//
//    }ord
    public String sendCompIntimation(String stat_reg_no, String stat_form_type, String uids, String password) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendCompIntimation func in inform file ");

        String isEmailSent = "false";

        String isSMSSent = "false";

        String from = "eforms@nic.in";

        String emailMessage = "",emailMessage1 ="", emailTo = "", emailSubject = "", smsMessage = "",smsMessage1 = "", smsMobile = "", userEmail = "", userMobile = "", by = "", to = "", templateId = "", appllicantEmailWhenOnBehalf = "", applicantMobileWhenOnBehalf = "";
        boolean onBehalf = false;
        // get user info
        ArrayList<String> userArr = fetchUserComDetail(stat_reg_no, stat_form_type);

        if (userArr != null ) {
            if (userArr.size() == 2) {
                onBehalf = false;
                if (userArr.get(0) != null) {
                    userEmail = userArr.get(0);
                }

                if (userArr.get(1) != null) {
                    userMobile = userArr.get(1);
                }
                if (userMobile == null || userMobile.isEmpty()) {
                    userMobile = "";
                }
//        if (!validation.checkFormat("mobile", userMobile)) {
//            userMobile = "";
//        }
                if (userMobile.startsWith("+")) {
                    userMobile = userMobile.substring(1);
                }
            } else {
                onBehalf = true;
                if (userArr.get(0) != null) {
                    userEmail = userArr.get(0);
                }

                if (userArr.get(1) != null) {
                    userMobile = userArr.get(1);
                }
                if (userMobile == null || userMobile.isEmpty()) {
                    userMobile = "";
                }
//        if (!validation.checkFormat("mobile", userMobile)) {
//            userMobile = "";
//        }
                if (userMobile.startsWith("+")) {
                    userMobile = userMobile.substring(1);
                }

                if (userArr.get(2) != null) {
                    appllicantEmailWhenOnBehalf = userArr.get(2);
                }

                if (userArr.get(3) != null) {
                    applicantMobileWhenOnBehalf = userArr.get(3);
                }
            }
        }

        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD))// completed
        {

            // send mail and sms to the user 
            ArrayList userText = Inform.this.fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
                    password, userMobile, "");

            if (userText.get(0) != null) {
                emailMessage = userText.get(0).toString();
            }

            if (userText.get(1) != null) {
                emailSubject = userText.get(1).toString();
            }

            if (userText.get(2) != null) {
                smsMessage = userText.get(2).toString();
            }
            if (userText.get(3) != null) {
                templateId = userText.get(3).toString();
            }

            emailTo = userEmail;

            smsMobile = userMobile;

            if (!emailTo.equals("")) {

                String[] attachFiles = new String[1];

                //attachFiles[0] = "/opt/tmp/sms.txt";
                //attachFiles[0] = "/opt/apache-tomcat-9.0.0.M22/webapps/OnlineForms/assets/upload/sms.txt"; // line modified by pr on 25thjan18
                //attachFiles[0] = "/tmp/sms.txt"; 
                //String[] cc = {"preeti.nhq@nic.in"};

                try {

                    //sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
                    //Mail.sendMailWithCc(  emailTo,  cc, emailMessage, emailSubject,  from);
                    //Mail.sendMail(emailTo,emailSubject, emailSubject,  from) ;
                    // above lines commented below added by pr on 23rdoct18
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);// send sms is given below

                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " send mail with attach exception " + e.getMessage());
                }

            }

            if (!smsMobile.equals("")) {

                //sendSMS(smsMessage, smsMobile); // line commented by pr on 23rdoct18
            }

            try {

                sendMailToNICSI(stat_reg_no, userEmail);
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendMailToNICSI exception " + e.getMessage());
            }

        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) // send email and sms for these forms. modified on 18thdec17 for gem
        {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside single nkn gem else if ");

            if (onBehalf) {
                ArrayList userText = fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
                        password, userEmail, userMobile, "", true);

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }
                
                //add by prabhat 
                if (userText.get(4) != null) {
                    emailMessage1 = userText.get(4).toString();
                }
                
                if (userText.get(5) != null) {
                    smsMessage1 = userText.get(5).toString();
                }

                emailTo = appllicantEmailWhenOnBehalf;

                smsMobile = applicantMobileWhenOnBehalf;
                
               String  OtherUserEmail = userEmail;
               String   OtherUserMobile = userMobile;
                
                if (!emailTo.equals("") && !OtherUserEmail.isEmpty()) {

                    /*String[] attachFiles = new String[1];

                     attachFiles[0] = "/tmp/single.txt";// line modified on 21stdec17

                     //attachFiles[0] = "/tmp/sms.txt"; 
                     String[] cc = {"preeti.nhq@nic.in"};

                     try {
                         sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
                     } catch (Exception e) {
                         System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " send mail exception " + e.getMessage());
                     }*/
                    // above code commented below added by pr on 25thjan18
                    //sendMail(emailTo, emailMessage, emailSubject, from);
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                    sendToRabbit(from, OtherUserEmail, emailMessage1, emailSubject, smsMessage1, OtherUserMobile, null, null, templateId);
                }
            } else { 

                ArrayList userText = fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
                        password, appllicantEmailWhenOnBehalf, userMobile, "", false);

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                emailTo = userEmail;

                smsMobile = userMobile;

                if (!emailTo.equals("")) {

                    /*String[] attachFiles = new String[1];

                    attachFiles[0] = "/tmp/single.txt";// line modified on 21stdec17

                    //attachFiles[0] = "/tmp/sms.txt"; 
                    String[] cc = {"preeti.nhq@nic.in"};

                    try {
                        sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " send mail exception " + e.getMessage());
                    }*/
                    // above code commented below added by pr on 25thjan18
                    //sendMail(emailTo, emailMessage, emailSubject, from);
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);// modified by pr on 21staug2020
                }
            }

//            if (!smsMobile.equals("")) {
//
//                //sendSMS(smsMessage, smsMobile);
//                // above line commented below added by pr on 23rdoct18
//                sendToRabbit("", "", "", "", smsMessage, smsMobile, null, null);
//
//            }
        } else // send email and sms for all other forms
        {
            if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) // if and else around added by pr on 14thmar18
            {
                // send mail and sms to the user 
                ArrayList<String> userText = wifiMailText(stat_reg_no);

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                emailTo = userEmail;
               
                smsMobile = userMobile;

                String wifi_process = "",wifi_value= ""; // line added by pr on 18thdec18

                HashMap<String, String> wifiHM = fetchwifiDetail(stat_reg_no);

                // added by prabhat on 12-08-2022
                if (!wifiHM.get("wifi_process").equals("")) {
                    wifi_process = wifiHM.get("wifi_process").toString();
                }
                 if (!wifiHM.get("wifi_value").equals("")) {
                    wifi_value = wifiHM.get("wifi_value").toString();
                }    
                System.out.println("wifi_process :::::::::::::" + wifi_process);
                System.out.println("wifi_value :::::::::::::" + wifi_value);
                System.out.println("Emailto ::::::::::: " + emailTo);
                
                if (!emailTo.equals("")) {

//                    String[] attachFiles = null; // line added by pr on 18thdec18
//
//                    if (wifi_process.equals("request")) // if around added by pr on 18thdec18 in case of certficate no need to attach files
//                    {
//                        attachFiles = new String[5];
//
//                        
//                        String prefix = "/eForms/WIFI/"; // to be uncommented in production 
//                        attachFiles[0] = prefix + "NAM_installation_procedure_for_Windows_laptops.pdf";
//
//                        attachFiles[1] = prefix + "Wireless_Escalation_Matrix.xls";
//
//                        attachFiles[2] = prefix + "procedure_configure_android.pdf";
//                        attachFiles[3] = prefix + "procedure_configure_iOS_LDAP.pdf";
//                        attachFiles[4] = prefix + "procedure_configure_lumia.pdf";
                          
                       // String wifi_value = fetch_wifi_value(stat_reg_no);
                         ArrayList<String> attachFiles = new ArrayList<>();
                     if (wifi_process.equals("request")) // if around added by pr on 18thdec18 in case of certficate no need to attach files
                    {
                        String prefix = "/eForms/WIFI/"; // to be uncommented in production 
                        attachFiles.add(prefix + "NAM_installation_procedure_for_Windows_laptops.pdf");
                        attachFiles.add(prefix + "Wireless_Escalation_Matrix.xls");
                        attachFiles.add(prefix + "procedure_configure_android.pdf");
                        attachFiles.add(prefix + "procedure_configure_iOS_LDAP.pdf");
                        attachFiles.add(prefix + "procedure_configure_lumia.pdf");

                        /*if (wifi_value.equals("2")) {
                            attachFiles[3] = prefix + "procedure_configure_iOS_VPNCA.pdf";
                        } else */// code commented by pr on 24thapr19 as per the mail by wifi team to make it same as others
                        for (String s : attachFiles) {
                           System.out.println(" inside  attachfiles  loop " + s);
                        }

                    }

                     try {

                        //sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, attachFiles);
                        //Mail.sendMailWithCc(  emailTo,  cc, emailMessage, emailSubject,  from);
                        System.out.println(" before send mail with attach function ");

                        //Mail.sendMailWithAttachOnly( from, emailTo, emailSubject, emailMessage, attachFiles );
                        // above line commented below added  by pr on 23rdoct18
                        String[] filesInStrinArray = attachFiles.toArray(new String[attachFiles.size()]) ;
                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, filesInStrinArray, templateId); // line modified by pr on 21staug2020

                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " send mail with attach exception " + e.getMessage());

                        e.printStackTrace();
                    }

                }

//                if (!smsMobile.equals("")) {
//                    //sendSMS(smsMessage, smsMobile);
//
//                    sendToRabbit("", "", "", "", smsMessage, smsMobile, null, null); // line modified by pr on 23rdoct18                                                        
//
//                }
            } else {

                // send mail and sms to the user 
                ArrayList userText = Inform.this.fetchCompMailText("user", stat_reg_no, stat_form_type, uids,
                        password, userMobile, "");

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                emailTo = userEmail;

                smsMobile = userMobile;

                /*if (!emailTo.equals("")) {
                    sendMail(emailTo, emailMessage, emailSubject, from);
                }

                if (!smsMobile.equals("")) {
                    sendSMS(smsMessage, smsMobile);
                }*/
                // above code commented below line added by pr on 23rdoct18
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

            }

        }

        // start, code common for all forms
        //************** send mail and sms to the ca*****************
        // get ca details email and mobile for this reg_no
        ArrayList adminText = null;

        ArrayList arrID = null;

        ArrayList getRolesByRegNo = fetchRolesByRegNo(stat_reg_no); // line added by pr on 27thfeb18

        System.out.println("********* inside sendcompintimation function rolesbyregno arraylist contains " + getRolesByRegNo);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " before wifi and bulk ");

        // if (!stat_form_type.equals(Constants.WIFI_FORM_KEYWORD) && !stat_form_type.equals(Constants.BULK_FORM_KEYWORD))// vpn condition deleted by pr on 5thjan18 
        {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside not wifi and bulk  uids is  " + uids + " password is " + password);

            if (getRolesByRegNo.contains("ca")) // if role contains ca then send mail and sms to ca
            {

                adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
                        password, userMobile, "Reporting/Nodal/Forwarding Officer");// for admin
                {
                    emailMessage = adminText.get(0).toString();
                }

                if (adminText.get(1) != null) {
                    emailSubject = adminText.get(1).toString();
                }

                if (adminText.get(2) != null) {
                    smsMessage = adminText.get(2).toString();
                }

                if (adminText.get(3) != null) {
                    templateId = adminText.get(3).toString();
                }

                arrID = fetchAdminId(stat_reg_no, "ca");// return 0 -> email and 1 -> mobile

                if (arrID != null && arrID.size() > 0 && arrID.get(0) != null) { // size added by pr on 21stfeb19
                    emailTo = arrID.get(0).toString();
                }

                if (arrID != null && arrID.size() > 1 && arrID.get(1) != null) { // size added by pr on 21stfeb19
                    smsMobile = arrID.get(1).toString();
                }

                /*if (!emailTo.equals("")) {

                    sendMail(emailTo, emailMessage, emailSubject, from);
                }

                if (!smsMobile.equals("")) {

                    sendSMS(smsMessage, smsMobile);
                }*/
                // above code commented below line added by pr on 23rdoct18
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

            }

        }

        if (getRolesByRegNo.contains("s")) // if role contains support then send mail to support
        {

            // send mail  to the support
            adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
                    password, userMobile, "Support");// for admin

            if (adminText.get(0) != null) {
                emailMessage = adminText.get(0).toString();
            }

            if (adminText.get(1) != null) {
                emailSubject = adminText.get(1).toString();
            }

            if (adminText.get(2) != null) {
                smsMessage = adminText.get(2).toString();
            }
            if (adminText.get(3) != null) {
                templateId = adminText.get(3).toString();
            }

            emailTo = sup_email;// above code modified by pr on 2ndjan18

            if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) // if added by pr on 23rdfeb18
            {
                emailTo = "smssupport@gov.in";
            }

            if (!emailTo.equals("") && !stat_form_type.equals(Constants.DNS_FORM_KEYWORD)
                    && !stat_form_type.equals(Constants.WIFI_FORM_KEYWORD) && !(stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD)
                    || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD)))// restrict this code for dns form
            {
                //sendMail(emailTo, emailMessage, emailSubject, from);

                // above code commented below line added by pr on 23rdoct18
                sendToRabbit(from, emailTo, emailMessage, emailSubject, "", "", null, null, templateId);

            }

        }

        if (getRolesByRegNo.contains("c")) // if role contains coordinator then send mail and sms to coordinator
        {
            System.out.println("************* inside sendcompintimation function getroles contains c ");

            adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
                    password, userMobile, "Coordinator");// for admin

            if (adminText.get(0) != null) {
                emailMessage = adminText.get(0).toString();
            }

            if (adminText.get(1) != null) {
                emailSubject = adminText.get(1).toString();
            }

            if (adminText.get(2) != null) {
                smsMessage = adminText.get(2).toString();
            }
            if (adminText.get(3) != null) {
                templateId = adminText.get(3).toString();
            }

            // if added by pr on 29thdec17, deleted by pr on 5thjan18 for vpn form
            if (!stat_form_type.equals(Constants.DNS_FORM_KEYWORD) && !stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) // if not dns form then execute below else go to else
            {

                // get coordinator details email and mobile for this reg_no
                arrID = fetchAdminId(stat_reg_no, "c");// return 0 -> email and 1 -> mobile

                if (arrID != null && arrID.get(0) != null) {
                    emailTo = arrID.get(0).toString();
                }

                if (arrID != null && arrID.get(1) != null) {
                    smsMobile = arrID.get(1).toString();
                }

                /*if (!emailTo.equals("")) {

                    sendMail(emailTo, emailMessage, emailSubject, from);
                }

                if (!smsMobile.equals("")) {

                    sendSMS(smsMessage, smsMobile);
                }*/
                // above code commented below line added by pr on 23rdoct18
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

            } else {

                arrID = fetchAdminId(stat_reg_no, "c");

                if (arrID != null && arrID.get(0) != null) {

                    if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.lang.String"))) {

                        emailTo = "";

                        smsMobile = "";

                        emailTo = arrID.get(0).toString();

                        if (arrID.get(1) != null) {
                            smsMobile = arrID.get(1).toString();
                        }

                        // send mail and sms only to one coordinators
                        /*if (!emailTo.equals("")) {

                            sendMail(emailTo, emailMessage, emailSubject, from);
                        }

                        if (!smsMobile.equals("")) {

                            sendSMS(smsMessage, smsMobile);
                        }*/
                        // above code commented below line added by pr on 23rdoct18
                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                    } else if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.util.HashMap"))) {

                        for (Object hm : arrID)// contains the hashmap of all email and mobile 
                        {

                            emailTo = "";

                            smsMobile = "";

                            HashMap h = (HashMap) hm;

                            if (h != null) {

                                if (h.get("email") != null) {
                                    emailTo = h.get("email").toString();
                                }

                                if (h.get("mobile") != null) {
                                    smsMobile = h.get("mobile").toString();
                                }

                                // send mail and sms only to all  coordinators one after another
                                /*if (!emailTo.equals("")) {

                                    sendMail(emailTo, emailMessage, emailSubject, from);
                                }

                                if (!smsMobile.equals("")) {

                                    sendSMS(smsMessage, smsMobile);
                                }*/
                                // above code commented below line added by pr on 23rdoct18
                                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                            }

                        }

                    }
                }

            }

        }

        if (getRolesByRegNo.contains("m")) // if role contains mail-admin then send mail and sms to mail-admin
        {
            // get mail-admin details email and mobile for this reg_no
            adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, stat_form_type, uids,
                    password, userMobile, "Admin");// for admin
            {
                emailMessage = adminText.get(0).toString();
            }

            if (adminText.get(1) != null) {
                emailSubject = adminText.get(1).toString();
            }

            if (adminText.get(2) != null) {
                smsMessage = adminText.get(2).toString();
            }

            if (adminText.get(3) != null) {
                templateId = adminText.get(3).toString();
            }

            arrID = fetchAdminId(stat_reg_no, "m");// return 0 -> email and 1 -> mobile

            if (arrID != null && arrID.get(0) != null) {

                if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.lang.String"))) {

                    emailTo = "";

                    smsMobile = "";

                    emailTo = arrID.get(0).toString();

                    if (arrID.get(1) != null) {
                        smsMobile = arrID.get(1).toString();
                    }

                    // send mail and sms only to one coordinators
                    /*if (!emailTo.equals("")) {

                        sendMail(emailTo, emailMessage, emailSubject, from);
                    }

                    if (!smsMobile.equals("")) {

                        sendSMS(smsMessage, smsMobile);
                    }*/
                    // above code commented below line added by pr on 23rdoct18
                    if (emailTo.equalsIgnoreCase("vpnsupport@nic.in")) // code added by pr on 18thaug2020
                    {
                        smsMessage = "";
                        smsMobile = "";
                    }

                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                } else if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.util.HashMap"))) {

                    for (Object hm : arrID)// contains the hashmap of all email and mobile 
                    {

                        emailTo = "";

                        smsMobile = "";

                        HashMap h = (HashMap) hm;

                        if (h != null) {

                            if (h.get("email") != null) {
                                emailTo = h.get("email").toString();
                            }

                            if (h.get("mobile") != null) {
                                smsMobile = h.get("mobile").toString();
                            }

                            // send mail and sms only to all  coordinators one after another
                            /*if (!emailTo.equals("")) {

                                sendMail(emailTo, emailMessage, emailSubject, from);
                            }

                            if (!smsMobile.equals("")) {

                                sendSMS(smsMessage, smsMobile);
                            }*/
                            // above code commented below line added by pr on 23rdoct18
                            if (emailTo.equalsIgnoreCase("vpnsupport@nic.in")) // code added by pr on 18thaug2020
                            {
                                smsMessage = "";
                                smsMobile = "";
                            }

                            sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                        }

                    }

                }
            }

        }

        // end code common for all forms
        return isEmailSent + "," + isSMSSent;

    }

    // below function added by pr on 27thfeb18, function modified by pr on 3rddec18
    public ArrayList<String> fetchRolesByRegNo(String stat_reg_no) {
        ArrayList<String> arr = new ArrayList<String>();

        PreparedStatement ps = null;

        ResultSet res = null;

        //Connection con = null;
        try {
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry = " SELECT distinct(stat_type) FROM status WHERE stat_reg_no = ? ";

            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            res = ps.executeQuery();
            String st_type = ""; // line added by pr on 3rddec18
            while (res.next()) {
                st_type = res.getString("stat_type").trim();

                switch (st_type) { // line modified by pr on , default removed from pr on 3rddec18

                    case "ca_pending":
                        arr.add("ca");
                        break;

                    case "support_pending":
                        arr.add("s");
                        break;

                    case "coordinator_pending":
                        arr.add("c");
                        break;

                    case "mail-admin_pending":
                        arr.add("m");
                        break;

                    case "da_pending":
                        arr.add("d");

                    case "us_pending":  // case added by pr on 8thjan19
                        arr.add("us");

                }

            }

            //// con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return arr;
    }

    // below function added by pr on 2nddec19
    public void sendCompleteFiles(String regNo, String[] atFileArr) {
        try {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendCompleteFiles sizde of atFileArr is " + atFileArr.length + " file arr contains " + atFileArr);
            String from = "eforms@nic.in";
            String emailTo = "eformsbackup@nic.in"; // to be uncommented in production
            

            String emailSubject = "Creating Backup of Registration number - " + regNo + " ";
            String emailMessage = "Application on eForms with Registration number - " + regNo + " has been completed and Files attached herewith.<br><br>Regards,<br>eForms Team";
            sendToRabbitEmailBackup(from, emailTo, emailMessage, emailSubject, atFileArr, null);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendCompleteFiles exception " + e.getMessage());
        }
        //sendMail(emailTo, emailMessage, emailSubject, from);       
    }

    
    // below function added by pr on 30thjan2020
    public void sendBulkRejectIntimation(String reg_no) {
        String by = "", title = "", text = "", sub = "", sms = "", from = "eforms@nic.in"; // line added by pr on 2ndmay19;

        // by role
        // by email
        String role = fetchSessionRole();

        String email = fetchSessionEmail();

        // role + sessionEmail
        if (role.equalsIgnoreCase(Constants.ROLE_CA)) {
            by = "Reporting Officer";
        } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN)) {
            by = "Admin";
        } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
            by = "Coordinator";
        } else if (role.equalsIgnoreCase(Constants.ROLE_SUP)) {
            by = "Support";
        }

        by = by + "(" + email + ")";

        String[] atFileArr = new String[1];

        String filePath = "/tmp/" + reg_no + "_error.xls";  // line modified by pr on 20thnov18 for sending in mail

        //filePath = "F:\\error.xls"; // for testing
        File excelfile = new File(filePath);// line modified by pr on 27thdec17

        if (excelfile.exists()) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendBulkRejectIntimation error file exists in inform.java ");

            if (filePath != null)// if added by pr on 31stjan18
            {
                atFileArr[0] = filePath;
            }
        }

        // get all the heirarchy email and mobile and applicant email and mobile from final_audit_track table        
        if (role.equalsIgnoreCase(Constants.ROLE_CA)) {
            by = "Reporting Officer";
        } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN)) {
            by = "Admin";
        } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
            by = "Coordinator";
        } else if (role.equalsIgnoreCase(Constants.ROLE_SUP)) {
            by = "Support";
        }

        by = by + "(" + email + ")";

        String[] cc = {sup_email}; // line modified by pr on 20thnov18

        HashMap<String, String> hm = fetchFinalAdmin(reg_no);

        if (hm != null && hm.size() > 0) {
            String val = "", to = "", mobile = "";

            for (String key : hm.keySet()) {
                if (!key.equalsIgnoreCase(by)) // if the file uploaded by entity is not same as key then only 
                {

                    val = hm.get(key);

                    String[] emMobArr = null;

                    if (val.contains("~")) {
                        emMobArr = val.split("~");
                    }

                    if (emMobArr != null && emMobArr.length > 0) {
                        to = emMobArr[0];

                        if (emMobArr.length == 2) {
                            mobile = emMobArr[1];;
                        }
                    }

                    text = "Dear " + key + ", <br>Emails in ID Creation Request for Registration number - "
                            + reg_no + " has been rejected by " + by + ". Please find the list of emails rejected attached.<br><br>Regards,<br>eForms Team ";

                    sub = "Emails rejected for Registration Number - " + reg_no;

                    sms = "Emails rejected for Registration number - " + reg_no + " by " + by;

                    if (!to.equals("")) {
                        // send mail

                        sendToRabbit(from, to, text, sub, sms, mobile, cc, atFileArr, null);

                    }

                }

            }

        }
    }

    // below function added by pr on 12thdec17, new parameter added formName by pr on 27thdec17 to include nkn bulk, below function modified by pr on 31stjan18
    public String sendBulkCompIntimation(String stat_reg_no, String formName) {
        String isEmailSent = "false";

        String isSMSSent = "false";

        String from = "eforms@nic.in";

        String emailMessage = "", emailTo = "", emailSubject = "", smsMessage = "", smsMobile = "",
                userEmail = "", userMobile = "", by = "", to = "", templateId = "";

        // get user info
        //ArrayList<String> userArr = fetchUserComDetail(stat_reg_no, "bulk");
        ArrayList<String> userArr = fetchUserComDetail(stat_reg_no, formName);// line modified by pr on 27thdec17

        if (userArr.get(0) != null) {
            userEmail = userArr.get(0);
        }

        if (userArr.get(1) != null) {
            userMobile = userArr.get(1);
        }
        if (userMobile == null || userMobile.isEmpty()) {
            userMobile = "";
        }
//        if (!validation.checkFormat("mobile", userMobile)) {
//            userMobile = "";
//        }
        if (userMobile.startsWith("+")) {
            userMobile = userMobile.substring(1);
        }

        // send mail and sms to the user 
        ArrayList userText = Inform.this.fetchCompMailText("user", stat_reg_no, "bulk", "",
                "", "", "");

        if (userText.get(0) != null) {
            emailMessage = userText.get(0).toString();
        }

        if (userText.get(1) != null) {
            emailSubject = userText.get(1).toString();
        }

        if (userText.get(2) != null) {
            smsMessage = userText.get(2).toString();
        }
        if (userText.get(3) != null) {
            templateId = userText.get(3).toString();
        }

        emailTo = userEmail;

        smsMobile = userMobile;

        if (!emailTo.equals("")) {
            ArrayList<String> atFiles = new ArrayList<String>(); // line added by pr on 31stjan18

            //String filePath = "/tmp/success.xls"; 
            String filePath = "/tmp/" + stat_reg_no + "_success.xls"; // line modified by pr on 20thnov18 for sending in mail

            //String filePath = "F:\\success.xls"; // for testing
            File excelfile = new File(filePath);

            if (excelfile.exists()) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside success file exists in inform.java ");

                if (filePath != null) {
                    atFiles.add(filePath);
                }

            }

            //filePath = "/tmp/error.xls"; 
            filePath = "/tmp/" + stat_reg_no + "_error.xls";  // line modified by pr on 20thnov18 for sending in mail

            //filePath = "F:\\error.xls"; // for testing
            excelfile = new File(filePath);// line modified by pr on 27thdec17

            if (excelfile.exists()) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside error file exists in inform.java ");

                if (filePath != null)// if added by pr on 31stjan18
                {
                    atFiles.add(filePath);
                }
            }

            System.out.println(" inside sendbulkintimation function value of filepath is " + filePath + " atFiles " + atFiles);

            //attachFiles[0] = "/tmp/sms.txt"; 
            //String[] cc = {"support@nic.in"};
            String[] cc = {sup_email}; // line modified by pr on 20thnov18

            // start, code added by pr on 31stjan18
            String[] atFileArr = new String[atFiles.size()];

            int k = 0;
            for (String a : atFiles) {
                atFileArr[k] = a;

                k++;
            }

            // end, code added by pr on 31stjan18
            try {

                //sendMailWithAttach(cc, from, emailTo, emailSubject, emailMessage, atFileArr);
                //Mail.sendMailWithAttachOnly( from, emailTo, emailSubject, emailMessage, atFileArr );// above line modified by pr on 4thapr18
                // above code commented below line added by pr on 23rdoct18
                System.out.println(" send to user inside send bulk complete intimation before rabbit call  sizde of atFileArr is " + atFileArr.length);

                //sendToRabbit(from, emailTo, emailMessage, emailSubject, "", "", null, atFileArr);
                sendToRabbit(from, emailTo, emailMessage, emailSubject, "", "", cc, atFileArr, templateId); // line modified by pr on 20thnov18

                // below for added by pr on 31stjan18
                for (String f : atFileArr) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after attachment inside for filename is " + f);

                    excelfile = new File(f);

                    if (excelfile.exists()) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exist filename is " + f);

                        //excelfile.delete();
                        if (excelfile.exists()) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside filename " + f + " STILL exists after deletion ");
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " send mail with attach exception bulk " + e.getMessage());
            }

            //sendMail(emailTo, emailMessage, emailSubject, from);
        }

        if (!smsMobile.equals("")) {

            //sendSMS(smsMessage, smsMobile);
            // above code commented below line added by pr on 23rdoct18
            sendToRabbit("", "", "", "", smsMessage, smsMobile, null, null, templateId);
        }

        // send mail  to the support
        ArrayList adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, "bulk", "",
                "", "", "Support");// for admin

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }
        if (adminText.get(3) != null) {
            templateId = adminText.get(3).toString();
        }

        /*emailTo = "support@nic.in";

        emailTo = "preeti.nhq@nic.in";*/
        emailTo = sup_email;// above code modified by pr on 2ndjan18

        //sendMail(emailTo, emailMessage, emailSubject, from);
        // above code commented below line added by pr on 23rdoct18
        sendToRabbit(from, emailTo, emailMessage, emailSubject, "", "", null, null, templateId);

        adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, "bulk", "",
                "", "", "Coordinator");// for admin

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }

        if (adminText.get(3) != null) {
            templateId = adminText.get(3).toString();
        }

        // get coordinator details email and mobile for this reg_no
        ArrayList arrID = fetchAdminId(stat_reg_no, "c");// return 0 -> email and 1 -> mobile

        /*if (arrID != null && arrID.get(0) != null) {
            emailTo = arrID.get(0).toString();
        }

        if (arrID != null && arrID.get(1) != null) {
            smsMobile = arrID.get(1).toString();
        }*/
        // above code commented below added by pr on 20thnov18
        if (arrID != null && arrID.size() > 0) {

            System.out.println(" start, coordinator send mail to admin 4 ");

            String em = "", mob = "";

            System.out.println(" coordinator size of arrID is " + arrID.size() + " type of array list element is " + arrID.get(0).getClass().getName()); // if it is a string it will be string if it is a hashmap it will be a hashmap only

            if (arrID.get(0) != null && arrID.get(0).getClass().getName().toLowerCase().contains("string")) {
                System.out.println(" coordinator inside arrid type is string ");

                em = arrID.get(0).toString();

                if (arrID.get(1) != null) {
                    mob = arrID.get(1).toString();
                }

                sendToRabbit(from, em, emailMessage, emailSubject, smsMessage, mob, null, null, templateId);
            } else if (arrID.get(0) != null && arrID.get(0).getClass().getName().toLowerCase().contains("hashmap")) {
                System.out.println(" coordinator inside arrid type is Hashmap ");

                for (Object hm : arrID) // arraylist contains hashmap object with email and mobile as keys
                {
                    System.out.println(" coordinator inside for loop start, send mail to admin 4 ");

                    //HashMap test = (HashMap<String, String>) hm;
                    HashMap test = (HashMap) hm; // line modified by pr on 20thnov18

                    em = test.get("email").toString();

                    mob = test.get("mobile").toString();

                    //System.out.println(" inside sendCompIntimation 11 before sendToRabbit smsMessage "+smsMessage+" smsMobile "+smsMobile+" email is "+em+" mob is "+mob);                
                    sendToRabbit(from, em, emailMessage, emailSubject, smsMessage, mob, null, null, templateId);
                }

            }

        }


        /*if (!emailTo.equals("")) {

            sendMail(emailTo, emailMessage, emailSubject, from);
        }

        if (!smsMobile.equals("")) {

            sendSMS(smsMessage, smsMobile);
        }*/
        // above code commented below line added by pr on 23rdoct18
        System.out.println(" before sending mail to coordinator ");

        //sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
        System.out.println(" start, send mail to admin ");

        // get mail-admin details email and mobile for this reg_no
        adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, "bulk", "", "", "", "Admin");// for admin

        System.out.println(" start, send mail to admin 1 ");

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }
        if (adminText.get(3) != null) {
            templateId = adminText.get(3).toString();
        }

        System.out.println(" start, send mail to admin 2 ");

        arrID = fetchAdminId(stat_reg_no, "m");// return 0 -> email and 1 -> mobile

        System.out.println(" start, send mail to admin 3 ");

        /*if (arrID != null && arrID.get(0) != null) {
            emailTo = arrID.get(0).toString();
        }

        if (arrID != null && arrID.get(1) != null) {
            smsMobile = arrID.get(1).toString();
        }*/
        // above code commented below added by pr on 31stoct18
        // start, code added by pr on 31stoct18
        if (arrID != null && arrID.size() > 0) {

            System.out.println(" start, send mail to admin 4 ");

            String em = "", mob = "";

            System.out.println(" size of arrID is " + arrID.size() + " type of array list element is " + arrID.get(0).getClass().getName()); // if it is a string it will be string if it is a hashmap it will be a hashmap only

            if (arrID.get(0) != null && arrID.get(0).getClass().getName().toLowerCase().contains("string")) {
                System.out.println(" inside arrid type is string ");

                em = arrID.get(0).toString();

                if (arrID.get(1) != null) {
                    mob = arrID.get(1).toString();
                }

                sendToRabbit(from, em, emailMessage, emailSubject, smsMessage, mob, null, null, templateId);
            } else if (arrID.get(0) != null && arrID.get(0).getClass().getName().toLowerCase().contains("hashmap")) {
                System.out.println(" inside arrid type is Hashmap ");

                for (Object hm : arrID) // arraylist contains hashmap object with email and mobile as keys
                {
                    System.out.println(" inside for loop start, send mail to admin 4 ");

                    //HashMap test = (HashMap<String, String>) hm;
                    HashMap test = (HashMap) hm; // line modified by pr on 20thnov18

                    em = test.get("email").toString();

                    mob = test.get("mobile").toString();

                    //System.out.println(" inside sendCompIntimation 11 before sendToRabbit smsMessage "+smsMessage+" smsMobile "+smsMobile+" email is "+em+" mob is "+mob);                
                    sendToRabbit(from, em, emailMessage, emailSubject, smsMessage, mob, null, null, templateId);
                }

            }

        }

        System.out.println(" start, send mail to admin 5 ");

        // start, code added by pr on 20thnov18 to send mail and sms to the RO
        adminText = Inform.this.fetchCompMailText("admin", stat_reg_no, "bulk", "",
                "", "", "Reporting Officer");// for admin

        System.out.println(" start, send mail to admin 6 ");

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }
        if (adminText.get(3) != null) {
            templateId = adminText.get(3).toString();
        }

        System.out.println(" start, send mail to admin 7 ");

        arrID = fetchAdminId(stat_reg_no, "ca");// return 0 -> email and 1 -> mobile

        System.out.println(" start, send mail to admin 8 ");

        if (arrID != null && arrID.get(0) != null) {
            emailTo = arrID.get(0).toString();
        }

        if (arrID != null && arrID.get(1) != null) {
            smsMobile = arrID.get(1).toString();
        }

        System.out.println(" before sending mail and sms to RO from " + from + " emailTo " + emailTo + " emailMessage " + emailMessage + " emailSubject " + emailSubject + " smsMessage " + smsMessage + " smsMobile " + smsMobile);

        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

        // end, code added by pr on 20thnov18 to send mail and sms to the RO
        // end, code added by pr on 31stoct18
        /*if (!emailTo.equals("")) {

            sendMail(emailTo, emailMessage, emailSubject, from);
        }

        if (!smsMobile.equals("")) {

            sendSMS(smsMessage, smsMobile);
        }*/
        // above code commented below line added by pr on 23rdoct18
        //sendToRabbit( from,  emailTo,  emailMessage,  emailSubject,  smsMessage,  smsMobile, null, null );                         
        // end code common for all forms
        return isEmailSent + "," + isSMSSent;

    }

    // below function added by pr on 12thdec17
    public String sendBulkCompIntimationSMS(String mobile, String uid, String mail, String password) {
        String isSMSSent = "false";

        String smsMessage = "Dear Applicant, your ID has been Created.New Email Created ID is "
                + mail + ", UID is " + uid + ", Password is " + password + ". NICSI"; // text modified from User to Applicant 
        String templateId = "1107160812077627394";

        if (!mobile.equals("")) {

            //sendSMS(smsMessage, mobile);
            // above code commented below line added by pr on 22ndoct18
            sendToRabbit("", "", "", "", smsMessage, mobile, null, null, templateId);
        }

        return isSMSSent;

    }

    public HashMap fetchSMSDetail(String regNo) {
        String sms_service = "";

        ResultSet rs = null;

        HashMap<String, String> hm = new HashMap<String, String>();

        PreparedStatement ps = null;

        //Connection con = null;
        try {
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry = "select * FROM sms_registration WHERE registration_no = ?  ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, regNo);

            rs = ps.executeQuery();

            while (rs.next()) {
                hm.put("sms_service", rs.getString("sms_service"));

                hm.put("department", rs.getString("department"));

                hm.put("city", rs.getString("city"));

                hm.put("mobile", rs.getString("mobile"));

                hm.put("domestic_traffic", rs.getString("domestic_traffic"));

                hm.put("inter_traffic", rs.getString("inter_traffic"));

                hm.put("flag_sender", rs.getString("flag_sender"));

                hm.put("sender_id", rs.getString("sender_id"));

                hm.put("final_id", rs.getString("final_id"));

                hm.put("address", rs.getString("address"));

                hm.put("ca_name", rs.getString("hod_name"));

                hm.put("ca_email", rs.getString("hod_email"));

                hm.put("ca_mobile", rs.getString("hod_mobile"));

                hm.put("department", rs.getString("department")); // line added by pr on 16thmar18

            }

            //// con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 1" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return hm;
    }

    public void sendMailToNICSI(String reg_no, String userEmail) {
        HashMap hm = fetchSMSDetail(reg_no);

        String sub = "PI For SMS Service Registration for Reg No. - " + reg_no;

        String domestic_traffic = "", inter_traffic = "", flag_sender = "", sender_id = "", final_id = "", department = "";// department addde by pr on 16thmar18

        if (hm.get("domestic_traffic") != null) {
            domestic_traffic = hm.get("domestic_traffic").toString();
        }

        if (hm.get("inter_traffic") != null) {
            inter_traffic = hm.get("inter_traffic").toString();
        }

        if (hm.get("flag_sender") != null) {
            flag_sender = hm.get("flag_sender").toString();
        }

        if (hm.get("sender_id") != null) {
            sender_id = hm.get("sender_id").toString();
        }

        if (hm.get("final_id") != null) {
            final_id = hm.get("final_id").toString();
        }

        if (hm.get("department") != null) {
            department = hm.get("department").toString();
        }

        Double charges = 0.0;

        if (flag_sender.equals("yes")) {

            // check for sender id value if it is there then 
            if (!sender_id.equals("")) {

                charges = Double.parseDouble(domestic_traffic) * Double.parseDouble(sms_dom_exempt_rate);

                if (inter_traffic != "") {
                    charges += Double.parseDouble(inter_traffic) * Double.parseDouble(sms_inter_exempt_rate);

                }
            }

        } else if (flag_sender.equals("no")) {

            //charges =  Integer.parseInt(domestic_traffic)  *  Integer.parseInt(sms_dom_rate) ;
            charges = Double.parseDouble(domestic_traffic) * Double.parseDouble(sms_dom_rate);

            if (inter_traffic != "") {
                charges += Double.parseDouble(inter_traffic) * Double.parseDouble(sms_inter_rate);

            }

        }

        charges = charges * 12;

        String finalCharge = String.valueOf(charges);

        String msg = "<p>Dear NICSI Team,</p>\n"
                + "\n"
                + "<p>&nbsp;</p>\n"
                + "\n"
                + "<p>Kindly generate PI for SMS service with following details:-</p>\n"
                + "\n"
                + "<p>&nbsp;</p>\n"
                + "\n"
                + "<table border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width:500px\">\n"
                + "<tbody>\n"
                + "<tr>\n"
                + "<td><strong>SMS Account Name&nbsp;</strong>:</td>\n"
                + "<td>" + final_id + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Sender ID Category</strong>&nbsp;:</td>\n";

        String str_for_sender_id = "TRAI NON-Exempted";

        if ((sender_id != null && !sender_id.equals(""))) {
            str_for_sender_id = "TRAI Exempted";
        }

        String address = "";

        if (hm.get("address") != null && !hm.get("address").toString().equals("")) {
            String[] add_arr = hm.get("address").toString().split("~");

            int arr_len = add_arr.length;

            if (arr_len > 0) {
                address += add_arr[0];
            }

            if (arr_len > 1) {
                address += " " + add_arr[1];
            }

            if (arr_len > 2) {
                address += " " + add_arr[2];
            }

        }

        msg += "<td>" + str_for_sender_id + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Officer Name</strong>&nbsp;:</td>\n"
                + "<td>" + (hm.get("ca_name") != null && !hm.get("ca_name").toString().equals("") ? hm.get("ca_name").toString() : "") + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Address</strong>&nbsp;:</td>\n"
                + "<td>" + address + "&nbsp; &nbsp;</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Email</strong>&nbsp;:</td>\n"
                + "<td>" + (hm.get("ca_email") != null && !hm.get("ca_email").toString().equals("") ? hm.get("ca_email").toString() : "") + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Mobile</strong>&nbsp;:&nbsp;</td>\n"
                + "<td><strong><strong>" + (hm.get("ca_mobile") != null && !hm.get("ca_mobile").toString().equals("") ? hm.get("ca_mobile").toString() : "") + "</strong></strong></td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Department</strong>&nbsp;:&nbsp;</td>\n"
                + "<td><strong><strong>" + (hm.get("department") != null && !hm.get("department").toString().equals("") ? hm.get("department").toString() : "") + "</strong></strong></td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Monthly Domestic SMS Traffic</strong>&nbsp;:</td>\n"
                + "<td>" + (hm.get("domestic_traffic") != null && !hm.get("domestic_traffic").toString().equals("") ? hm.get("domestic_traffic").toString() : "") + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Monthly International SMS Traffic</strong>&nbsp;:</td>\n"
                + "<td>" + (hm.get("inter_traffic") != null && !hm.get("inter_traffic").toString().equals("") ? hm.get("inter_traffic").toString() : "") + "</td>\n"
                + "</tr>\n"
                + "<tr>\n"
                + "<td><strong>Duration</strong>&nbsp;:</td>\n"
                + "<td>6 Months</td>\n"
                + "</tr>\n"
                + "</tbody>\n"
                + "</table>\n"
                + "\n"
                + "<p>&nbsp;</p>\n"
                + "\n"
                + "<p>Thanks and Regards,</p>\n"
                + "\n"
                + "<p>SMS-Support<br />\n"
                + "Website:&nbsp;<a href=\"https://servicedesk.nic.in/\" target=\"_blank\">https://servicedesk.nic.in</a><br />\n"
                + "NIC&nbsp;Service&nbsp;Desk (Toll-Free):-&nbsp;<a href=\"callto:1800111555\" target=\"_blank\">1800111555</a></p>"; // department added by pr on 16thmar18

        String to = nicsi_email; // replace with the NICSI Email here

        //String cc = user.getItem("auth_email");
        //String[] cc = {userEmail, "smssupport@gov.in"};
        String[] cc = {userEmail, "smssupport@gov.in", "arpita.burman@nic.in", "tiwary@nic.in", "saurabh.j380@gmail.com", "pa.nsp-nicsi@nic.in", "ukjena@nic.in"};// line modified by pr on 16thmar18

        //String[] cc = {userEmail, "rahejapreeti@hotmail.com", "rahejapreeti@gmail.com"};// line modified by pr on 8thnov18 for testing
        //String[] bcc = {"preeti.nhq@nic.in", "tiwari.ashwini@nic.in", "owais@nic.in"};
        String[] bcc = {"preeti.nhq@nic.in"};

        String from = "eforms@nic.in";

        //Mail.sendMailWithCc(  to,  cc,  msg,  sub,  from);
        //Mail.sendMailWithCcBcc(to, cc, bcc, msg, sub, from);
        sendToRabbit(from, to, msg, sub, "", "", cc, null, null); // above line commented this added by pr on 23rdoct18
    }

    // below function added by pr on 5thjun18
    //public String fetchCAEmail(String ca_id) 
    public String fetchCAEmail(String reg_no) {

        String email = "";

        // above code commented below added by pr on 20thfeb19
        HashMap<String, String> hm = fetchCAFromBaseTable(reg_no);

        if (hm != null) {
            if (hm.get("email") != null && hm.get("email").equals("")) {
                email = hm.get("email");
            }
        }

        return email;

    }

    // below function added by pr on 20thfeb19, to replace comp_auth table with base table
    public HashMap<String, String> fetchCAFromBaseTable(String reg_no) {
        HashMap<String, String> hm = new HashMap<String, String>();
        //Connection con = null;

        try {
            //con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Inform.class.getName()).log(Level.SEVERE, null, ex);
        }

        String stat_form_type = reg_no.toLowerCase();

        String tblName = "";

        if (stat_form_type.contains(Constants.SMS_FORM_KEYWORD)) {
            tblName = "sms_registration";

        } else if (stat_form_type.contains(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = "single_registration";

        } else if (stat_form_type.contains(Constants.BULK_FORM_KEYWORD) && !stat_form_type.contains("nkn")) {
            tblName = "bulk_registration";

        } else if (stat_form_type.contains(Constants.IP_FORM_KEYWORD)) {
            tblName = "ip_registration";

        } else if (stat_form_type.contains("nkn")) {

            tblName = "nkn_registration";

        } else if (stat_form_type.contains(Constants.RELAY_FORM_KEYWORD)) {

            tblName = "relay_registration";

        } else if (stat_form_type.contains(Constants.LDAP_FORM_KEYWORD)) {

            tblName = "ldap_registration";

        } else if (stat_form_type.contains(Constants.DIST_FORM_KEYWORD)) {

            tblName = "distribution_registration";

        } else if (stat_form_type.contains(Constants.IMAP_FORM_KEYWORD)) {

            tblName = "imappop_registration";

        } else if (stat_form_type.contains(Constants.GEM_FORM_KEYWORD)) { // start from here till end equals replaced with contains by pr on 14thjun19

            tblName = "gem_registration";

        } else if (stat_form_type.contains(Constants.MOB_FORM_KEYWORD)) {

            tblName = "mobile_registration";

        } else if (stat_form_type.contains(Constants.DNS_FORM_KEYWORD)) {

            tblName = "dns_registration";

        } else if (stat_form_type.contains(Constants.WIFI_FORM_KEYWORD)) {

            tblName = "wifi_registration";

        } else if (stat_form_type.contains(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.contains(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.contains(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.contains(Constants.VPN_RENEW_FORM_KEYWORD) || stat_form_type.contains("vpnrenew")) {

            tblName = "vpn_registration";

        } else if (stat_form_type.equals(Constants.WEBCAST_FORM_KEYWORD)) {

            tblName = "webcast_registration";

        } else if (stat_form_type.equals(Constants.FIREWALL_FORM_KEYWORD)) {

            tblName = "centralutm_registration";

        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {

            tblName = Constants.DAONBOARDING_TABLE_NAME;
        }

        PreparedStatement ps = null;

        ResultSet res = null;

        try {
            if (!tblName.equals("")) {
                String qry = "SELECT hod_name, "
                        + " hod_email, hod_mobile, ca_desig "
                        + "FROM " + tblName + " WHERE registration_no = ? ";

                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getCAFromBaseTable query is " + ps);

                res = ps.executeQuery();

                while (res.next()) {
                    hm.put("name", res.getString("hod_name"));
                    hm.put("email", res.getString("hod_email"));
                    hm.put("mobile", res.getString("hod_mobile"));
                    hm.put("desig", res.getString("ca_desig"));
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getCAFromBaseTable exception " + e.getMessage());
        } finally {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        if (hm != null && hm.size() > 0) {
            return hm;
        } else {
            return null;
        }

    }

    //public ArrayList<String> fetchCADetail(String ca_id) 
    public ArrayList<String> fetchCADetail(String reg_no) // parameters changed by pr on 20thfeb19 
    {
        ArrayList arr = new ArrayList<String>();

        HashMap<String, String> hm = fetchCAFromBaseTable(reg_no);

        if (hm != null) {
            if (hm.get("email") != null && !hm.get("email").equals("")) // 21stfeb19
            {
                arr.add(hm.get("email"));
            }

            if (hm.get("mobile") != null && !hm.get("mobile").equals("")) // 21stfeb19
            {
                arr.add(hm.get("mobile"));
            }
        }

        System.out.println(" inside fetchCADetail function before return statement arraylist is " + arr);

        return arr;
    }

    // this function returns the particular ca, support, coordinator, mail-admin who has finally handled this particular reg no based on admin type
    // return an arraylist with email and mobile as two elements with 0th and 1st positions
    public ArrayList fetchAdminId(String reg_no, String admin_type) {

        String id = "";

        ArrayList arr = null; // arraylist can be of string elements ( for all ) or hashmap type elements (for coordinator only)

        PreparedStatement ps = null;

        ResultSet res = null;

        //Connection con = null;
        try {

//            Class.forName("com.mysql.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);
            //con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection " + e.getMessage());
        }

        if (admin_type.equals("ca")) {
            try {
                String qry = "SELECT stat_forwarded_to_user FROM status "
                        + ""
                        + " WHERE stat_reg_no = ? AND stat_forwarded_to = 'ca' AND stat_type = 'ca_pending' "
                        + ""
                        + "ORDER BY stat_id DESC LIMIT 1";

                ps = conSlave.prepareStatement(qry);

                ps.setString(1, reg_no);

                res = ps.executeQuery();

                while (res.next()) {
                    // set email and mobile in the Forms object

                    String ca_id = res.getString("stat_forwarded_to_user");

                    // get ca details like email and mobile from comp_auth table
                    //arr = fetchCADetail(ca_id);
                    arr = fetchCADetail(reg_no);

                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } else if (admin_type.equals("c")) {
            try {
                String qry = "SELECT stat_forwarded_to_user FROM status "
                        + ""
                        + " WHERE stat_reg_no = ? AND stat_forwarded_to = 'c' AND stat_type = 'coordinator_pending' "
                        + ""
                        + "ORDER BY stat_id DESC LIMIT 1";

                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                res = ps.executeQuery();

                while (res.next()) {
                    // set email and mobile in the Forms object

                    String email = res.getString("stat_forwarded_to_user");

                    // get coordinator details like email and mobile from LDAP
                    if (!email.contains(",")) {
                        arr = fetchLDAPName(email);
                    } else if (email.contains(",")) {
                        arr = new ArrayList();

                        //get the  details for multiple emails from LDAP and store in HashMap and add in the ArrayList
                        String[] emails = email.split(",");

                        for (String e : emails) {
                            ArrayList ldapArr = fetchLDAPName(e);

                            HashMap<String, String> hm = new HashMap<String, String>();

                            //if (ldapArr != null && ldapArr.size() == 3) {
                            if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19
                                Boolean isFound = false;

                                String em = "", mob = "";

                                if (ldapArr.get(0) != null) {
                                    em = ldapArr.get(0).toString();

                                }

                                if (ldapArr.get(1) != null) {
                                    mob = ldapArr.get(1).toString();

                                }

                                if (ldapArr.get(2) != null) {
                                    if (ldapArr.get(2).toString().equals("true")) {
                                        isFound = true;

                                        hm.put("email", em);

                                        hm.put("mobile", mob);

                                    } else {
                                        hm.put("email", em);

                                        hm.put("mobile", "");

                                    }
                                }

                            }

                            arr.add(hm);
                        }

                    }

                }

                //// con.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } else if (admin_type.equals("m")) {
            try {
                String qry = "SELECT stat_forwarded_to_user FROM status "
                        + ""
                        + " WHERE stat_reg_no = ? AND stat_forwarded_to = 'm' AND stat_type = 'mail-admin_pending' "
                        + ""
                        + "ORDER BY stat_id DESC LIMIT 1";

                ps = conSlave.prepareStatement(qry);

                ps.setString(1, reg_no);

                res = ps.executeQuery();

                /*while (res.next()) {
                    // set email and mobile in the Forms object

                    String email = res.getString("stat_forwarded_to_user");

                    // get ca details like email and mobile from comp_auth table
                    arr = fetchLDAPName(email);

                }*/
                while (res.next()) {
                    // set email and mobile in the Forms object

                    String email = res.getString("stat_forwarded_to_user");

                    // get coordinator details like email and mobile from LDAP
                    if (!email.contains(",")) {
                        arr = fetchLDAPName(email); // array list would contain string 0 -> email, 1 -> mobile, 2 -> true false
                    } else if (email.contains(",")) {
                        arr = new ArrayList(); // arraylist would contain a hashmap

                        //get the  details for multiple emails from LDAP and store in HashMap and add in the ArrayList
                        String[] emails = email.split(",");

                        for (String e : emails) {
                            ArrayList ldapArr = fetchLDAPName(e);

                            HashMap<String, String> hm = new HashMap<String, String>();

                            //if (ldapArr != null && ldapArr.size() == 3) {
                            if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19
                                Boolean isFound = false;

                                String em = "", mob = "";

                                if (ldapArr.get(0) != null) {
                                    em = ldapArr.get(0).toString();

                                }

                                if (ldapArr.get(1) != null) {
                                    mob = ldapArr.get(1).toString();

                                }

                                if (ldapArr.get(2) != null) {
                                    if (ldapArr.get(2).toString().equals("true")) {
                                        isFound = true;

                                        hm.put("email", em);

                                        hm.put("mobile", mob);

                                    } else {
                                        hm.put("email", em);

                                        hm.put("mobile", "");

                                    }
                                }

                            }

                            arr.add(hm);
                        }

                    }

                }

                //  // con.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        } else if (admin_type.equals("d")) // else if added by pr on 20thfeb18
        {
            try {
                String qry = "SELECT stat_forwarded_to_user FROM status "
                        + ""
                        + " WHERE stat_reg_no = ? AND stat_forwarded_to = 'd' AND stat_type = 'da_pending' "
                        + ""
                        + "ORDER BY stat_id DESC LIMIT 1";

                ps = conSlave.prepareStatement(qry);

                ps.setString(1, reg_no);

                res = ps.executeQuery();

                while (res.next()) {
                    // set email and mobile in the Forms object

                    String email = res.getString("stat_forwarded_to_user");

                    // get coordinator details like email and mobile from LDAP
                    if (!email.contains(",")) {
                        arr = fetchLDAPName(email);
                    } else if (email.contains(",")) {
                        arr = new ArrayList();

                        //get the  details for multiple emails from LDAP and store in HashMap and add in the ArrayList
                        String[] emails = email.split(",");

                        for (String e : emails) {
                            ArrayList ldapArr = fetchLDAPName(e);

                            HashMap<String, String> hm = new HashMap<String, String>();

                            //if (ldapArr != null && ldapArr.size() == 3) {
                            if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19
                                Boolean isFound = false;

                                String em = "", mob = "";

                                if (ldapArr.get(0) != null) {
                                    em = ldapArr.get(0).toString();

                                }

                                if (ldapArr.get(1) != null) {
                                    mob = ldapArr.get(1).toString();

                                }

                                if (ldapArr.get(2) != null) {
                                    if (ldapArr.get(2).toString().equals("true")) {
                                        isFound = true;

                                        hm.put("email", em);

                                        hm.put("mobile", mob);

                                    } else {
                                        hm.put("email", em);

                                        hm.put("mobile", "");

                                    }
                                }

                            }

                            arr.add(hm);
                        }

                    }

                }

                //// con.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

        }

        return arr;
    }

    // below function added by pr on 22ndfeb18
    public HashMap<String, String> fetchLastUpdatedStatus(String stat_reg_no) {
        HashMap<String, String> hm = new HashMap<String, String>();

        PreparedStatement ps = null;

        ResultSet rs = null;
        //Connection con = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);
            // con = entities.MyConnection.getConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            //String qry = "select * FROM status WHERE stat_reg_no  = ? ORDER BY stat_createdon DESC LIMIT 1 ";
            String qry = "select stat_forwarded_by,stat_forwarded_by_user,stat_forwarded_to,stat_forwarded_to_user,stat_remarks FROM status WHERE stat_reg_no  = ? ORDER BY stat_id DESC LIMIT 1 "; // line modified by pr on 11thoct18

            //ps = conSlave.prepareStatement(qry);
            ps = conSlave.prepareStatement(qry);// modified by pr on 20thjul2020 so as to resolve the replication delay

            ps.setString(1, stat_reg_no);

            rs = ps.executeQuery();

            while (rs.next()) {
                hm.put("stat_forwarded_by", rs.getString("stat_forwarded_by"));

                // code commented by pr on 20thfeb19 as from now on forded_to_user will contain the email only in case of comp auth
                hm.put("stat_forwarded_by_user", rs.getString("stat_forwarded_by_user"));

                hm.put("stat_forwarded_to", rs.getString("stat_forwarded_to"));

                hm.put("stat_forwarded_to_user", rs.getString("stat_forwarded_to_user"));

                hm.put("stat_remarks", rs.getString("stat_remarks")); // line added by pr on 26thfeb18

            }

            //// con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 2" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return hm;
    }

    // 0 -> email message 1 -> email subject 2 -> sms message // get user text in case of forward and reject case
    //public ArrayList<String> fetchUserMailText(String type, String reg_no, String by, String to, String stat_remarks) 
     public ArrayList<String> fetchUserMailText(String type, String reg_no, String by, String to, String stat_remarks, String stat_forwarded_by_user, String stat_forwarded_to_user) // line modified by pr on 5thjun18
    {

        if (stat_forwarded_by_user != null && !stat_forwarded_by_user.equals("")) {

            by = by + "(" + stat_forwarded_by_user + ")";
        }

        if (stat_forwarded_to_user != null && !stat_forwarded_to_user.equals("")) {

            to = to + "(" + stat_forwarded_to_user + ")";
        }

        // end, code added by pr on 22ndfeb18
        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "", templateId = "";

        if (type.equals("f")) {
            text = "Dear Applicant, <br>Your application with Registration number - " + reg_no + " has been Approved and forwarded by the " + by + " to the " + to;

            if (!stat_remarks.equals("")) {
                text += " with Remarks : '" + stat_remarks + "' "; // quotes added by pr on 23rdmar18
            }

            text += " <br>Regards,<br>eForms Team";

            sub = "Application Registration Number - " + reg_no + " Approved and forwarded by " + by;
            ////12/2/2022 invalid template 
            sms = "Dear Applicant, Application No. -  " + reg_no + "  has been Approved and forwarded by the  " + by + "  to the  " + to + " NICSI";
            //sms = "Dear Applicant, Application No. - " + reg_no + " has been Approved and forwarded by the " + by + " to the " + to + " NICSI";
            templateId = "1107160811660394865";

        } else if (type.equals("r")) {
            text = "Dear Applicant, <br>Your application with Registration number - " + reg_no + " has been Rejected by the " + by;

            if (!stat_remarks.equals("")) {
                text += " due to the reason : '" + stat_remarks + "' "; // quotes added by pr on 23rdmar18
            }

            text += " <br>Regards,<br>eForms Team";

            sub = "Dear Applicant, Application No. - " + reg_no + " has been Rejected by the " + by;
            //12/2/2022 invalid template 
           // sms = "Dear Applicant, Application No. - " + reg_no + " has been Rejected by the " + by + " NICSI";
            sms = "Dear Applicant, Application No. -  " + reg_no + "  has been Rejected by the  " + by + " NICSI";
            templateId = "1107160811670816921";
        }

        arr.add(text);

        arr.add(sub);

        arr.add(sms);

        arr.add(templateId);

        return arr;
    }

    // 0 -> email message 1 -> email subject 2 -> sms message // get user text in case of forward and reject case
    //public ArrayList<String> fetchAdminMailText(String type, String reg_no, String by, String to, String stat_remarks, String admin) 
    public ArrayList<String> fetchAdminMailText(String type, String reg_no, String by, String to, String stat_remarks, String admin, String stat_forwarded_by_user,
            String stat_forwarded_to_user, String stat_form_type) // parameters added by pr on 5thjun18, stat_form_type parameter added by pr on 15thnov18
    {
        if (stat_forwarded_by_user != null && !stat_forwarded_by_user.equals("")) {

            by = by + "(" + stat_forwarded_by_user + ")";
        }

        if (stat_forwarded_to_user != null && !stat_forwarded_to_user.equals("")) {

            to = to + "(" + stat_forwarded_to_user + ")";
        }

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "", templateId = "";

        if (type.equals("f")) {
            text = "Dear " + admin + ", <br>An application with Registration number - " + reg_no + " has been Approved and forwarded by the " + by + " to the " + to;

            if (!stat_remarks.equals("")) {
                text += " with Remarks : '" + stat_remarks + "'"; // quotes added by pr on 23rdmar18
            } // if added by pr on 5thjun18 

            // start, code added by pr on 15thnov18
            HashMap<String, String> hm = fetchApplicantRODetail(stat_form_type, reg_no);

            String name = "", email = "", mobile = "", desig = "", min = "";

            String ro_name = "", ro_email = "", ro_mobile = "", ro_desig = "";

            if (hm != null) {
                if (hm.get("name") != null && !hm.get("name").equals("")) {
                    name = hm.get("name");
                }

                if (hm.get("email") != null && !hm.get("email").equals("")) {
                    email = hm.get("email");
                }

                if (hm.get("mobile") != null && !hm.get("mobile").equals("")) {
                    mobile = hm.get("mobile");
                }

                if (hm.get("desig") != null && !hm.get("desig").equals("")) {
                    desig = hm.get("desig");
                }

                if (hm.get("ministry") != null && !hm.get("ministry").equals("")) {
                    min = hm.get("ministry");
                }

                if (hm.get("ro_name") != null && !hm.get("ro_name").equals("")) {
                    ro_name = hm.get("ro_name");
                }

                if (hm.get("ro_email") != null && !hm.get("ro_email").equals("")) {
                    ro_email = hm.get("ro_email");
                }

                if (hm.get("ro_mobile") != null && !hm.get("ro_mobile").equals("")) {
                    ro_mobile = hm.get("ro_mobile");
                }

                if (hm.get("ro_desig") != null && !hm.get("ro_desig").equals("")) {
                    ro_desig = hm.get("ro_desig");
                }
            }

            text += "<br><br>The Applicant and Reporting Officers Details are shown below:<br><br>";

            text += "<h3>Applicant Details</h3><table border='0'>"
                    + "<tr><td>Name </td><td>" + name + "</td></tr>"
                    + "<tr><td>Email </td><td>" + email + "</td></tr>"
                    + "<tr><td>Mobile </td><td>" + mobile + "</td></tr>"
                    + "<tr><td>Designation </td><td>" + desig + "</td></tr>"
                    + "<tr><td>Organization </td><td>" + min + "</td></tr>"
                    + "</table>";

            text += "<br><br>";

            text += "<h3>Reporting Officer's Details</h3><table border='0'>"
                    + "<tr><td>RO Name </td><td>" + ro_name + "</td></tr>"
                    + "<tr><td>RO Email </td><td>" + ro_email + "</td></tr>"
                    + "<tr><td>RO Mobile </td><td>" + ro_mobile + "</td></tr>"
                    + "<tr><td>Designation </td><td>" + ro_desig + "</td></tr>"
                    + "</table>";

            // end, code added by pr on 15thnov18    
            text += " <br>Regards,<br>eForms Team";

            sub = "Application Registration Number - " + reg_no + " Approved and Forwarded by the " + by;

            sms = "Dear " + admin + ", Application No. - " + reg_no + " has been Approved and Forwarded by the " + by + " NICSI";

            templateId = "1107160811684041725";

        } else if (type.equals("r")) {
            text = "Dear " + admin + ", <br>An application with Registration number - " + reg_no + " has been Rejected by the " + by;

            if (!stat_remarks.equals("")) {
                text += " due to the reason : '" + stat_remarks + "'"; // quotes added by pr on 23rdmar18
            }

            text += " <br>Regards,<br>eForms Team";

            sub = "Application No. - " + reg_no + " has been Rejected by the " + by;
            //12/2/2022 invalid template
            sms = "Dear  " + admin + ", Application No. -  " + reg_no + "  has been Rejected by the  " + by + " NICSI";
            //sms = "Dear " + admin + ", Application No. - " + reg_no + " has been Rejected by the " + by + " NICSI";

            templateId = "1107160811697704901";
        }

        arr.add(text);

        arr.add(sub);

        arr.add(sms);
        arr.add(templateId);

        return arr;
    }

    // below function added by pr on 13thfeb19
    public boolean checkVPNIP(String coord_email) {

        boolean isExist = false;

        PreparedStatement ps = null;

        ResultSet rs = null;
        //Connection con = null;

        try {
            // con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry = "select ip FROM employment_coordinator  WHERE emp_coord_email  = ? ORDER BY emp_id DESC LIMIT 1 "; // line modified by pr on 11thoct18

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, coord_email);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside checkVPNIP function  query is " + ps);

            rs = ps.executeQuery();

            while (rs.next()) {

                String ip = rs.getString("ip");

                if (ip != null && !ip.equals("")) {
                    isExist = true;
                }

            }

            //// con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside checkVPNIP function " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return isExist;
    }

    // 0 -> email message 1 -> email subject 2 -> sms message // get approval text for any admin 
    //public ArrayList<String> fetchAppText(String reg_no, String by, String to) 
    public ArrayList<String> fetchAppText(String reg_no, String by, String to, String stat_forwarded_by_user, String stat_forwarded_to_user, String stat_remarks, String stat_form_type) // line modified by pr on 5thjun18, stat_form_type added by pr on 15thnov18
    {
        String templateId = "";
        // above code modified by pr on 5thjun18
        if (stat_forwarded_by_user != null && !stat_forwarded_by_user.equals("")) {

            by = by + "(" + stat_forwarded_by_user + ")";
        }

        if (stat_forwarded_to_user != null && !stat_forwarded_to_user.equals("")) {

            //to = to + "("+stat_forwarded_to_user+")";
        }

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "", note = "";

        //String host = "https://10.1.16.87/OnlineForms/";
        String host = "https://eforms.nic.in/";

        // start, code added by pr on 9thfeb18
        if (to.equals("Admin")) {
            host = "https://eforms.nic.in/";

            // note added by pr on 27thapr19
            note = "<strong>Kindly note :You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer prior to approval or creation of account. If more information is required please use the option 'Raise a Query' and ask for more inputs for verifying credentials.</strong>";

        } else if (to.contains("Coordinator")) {

            Boolean isExist = checkVPNIP(stat_forwarded_to_user); // line added by pr on 13thfeb19

            host = "https://eforms.nic.in/";

            // note added by pr on 27thapr19
            note = "<strong>Kindly note :You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer prior to approval or creation of account. If more information is required please use the option 'Raise a Query' and ask for more inputs for verifying credentials.</strong>";

            if (!isExist) // if around added by pr on 13thfeb19
            {
                // + added by pr on 27thapr19    
                note += "<strong>\n Due to security reasons, we are allowing NIC coordinators only from the allowed VPN IPs. Kindly share your VPN IP with support@gov.in, so that it could be allowed at eForms portal. If you do not have VPN IP, get it from VPN support (vpnsupport@nic.in).\n"
                        + "\n"
                        + "Your access from any IP will be blocked after some time. Hence, you are requested to get the VPN IP to avoid uniterrupted services.</strong>";

            }

        } else if (to.contains("Support")) {
            host = "https://eforms.nic.in/";
        } else if (to.contains("DA-Admin")) {
            host = "https://mailadmin.nic.in/da-admin"; // /da-admin added by pr on 2ndjul19

            // note added by pr on 27thapr19
            note = "<strong>Kindly note :You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer prior to approval or creation of account. If more information is required please use the option 'Raise a Query' and ask for more inputs for verifying credentials.</strong>";

            // + added by pr on 27thapr19
            note += "<strong>\n You have to be on VPN , to be able to access the mentioned link.</strong>";
        }

        // end, code added by pr on 9thfeb18
        text = "Dear " + to + ", <br>An application has been forwarded to you for your approval, with Registration number - " + reg_no + " "
                + ""
                + "by the " + by;

        if (!stat_remarks.equals("")) // if added by pr on 5thjun18
        {
            text += " with Remarks : '" + stat_remarks + "'";
        }

        // start, code added by pr on 15thnov18
        HashMap<String, String> hm = fetchApplicantRODetail(stat_form_type, reg_no);

        String name = "", email = "", mobile = "", desig = "", min = "";

        String ro_name = "", ro_email = "", ro_mobile = "", ro_desig = "";

        if (hm != null) {
            if (hm.get("name") != null && !hm.get("name").equals("")) {
                name = hm.get("name");
            }

            if (hm.get("email") != null && !hm.get("email").equals("")) {
                email = hm.get("email");
            }

            if (hm.get("mobile") != null && !hm.get("mobile").equals("")) {
                mobile = hm.get("mobile");
            }

            if (hm.get("desig") != null && !hm.get("desig").equals("")) {
                desig = hm.get("desig");
            }

            if (hm.get("ministry") != null && !hm.get("ministry").equals("")) {
                min = hm.get("ministry");
            }

            if (hm.get("ro_name") != null && !hm.get("ro_name").equals("")) {
                ro_name = hm.get("ro_name");
            }

            if (hm.get("ro_email") != null && !hm.get("ro_email").equals("")) {
                ro_email = hm.get("ro_email");
            }

            if (hm.get("ro_mobile") != null && !hm.get("ro_mobile").equals("")) {
                ro_mobile = hm.get("ro_mobile");
            }

            if (hm.get("ro_desig") != null && !hm.get("ro_desig").equals("")) {
                ro_desig = hm.get("ro_desig");
            }
        }

        text += "<br><br>The Applicant and Reporting Officers Details are shown below:<br><br>";

        text += "<h3>Applicant Details</h3><table border='0'>"
                + "<tr><td>Name</td><td>" + name + "</td></tr>"
                + "<tr><td>Email</td><td>" + email + "</td></tr>"
                + "<tr><td>Mobile</td><td>" + mobile + "</td></tr>"
                + "<tr><td>Designation</td><td>" + desig + "</td></tr>"
                + "<tr><td>Organization</td><td>" + min + "</td></tr>"
                + "</table>";

        text += "<br><br>";

        text += "<h3>Reporting Officer's Details</h3><table border='0'>"
                + "<tr><td>RO Name</td><td>" + ro_name + "</td></tr>"
                + "<tr><td>RO Email</td><td>" + ro_email + "</td></tr>"
                + "<tr><td>RO Mobile</td><td>" + ro_mobile + "</td></tr>"
                + "<tr><td>Designation</td><td>" + ro_desig + "</td></tr>"
                + "</table>";

        // end, code added by pr on 15thnov18    
        text += "<br>Please <a href='" + host + "'>Click Here</a> (<a href='" + host + "'>" + host + "</a>)to take action <br>" + note
                + "<br><br>Regards,<br>eForms Team";

        sub = "Application Registration Number - " + reg_no + " Forwarded by " + by;

        sms = "Dear " + to + ", Application No. - " + reg_no + " has been Forwarded to you for your approval by the " + by + ".Please Click Here to take action " + host + ". NICSI";
        templateId = "1107160811224464899";
        arr.add(text);

        arr.add(sub);

        arr.add(sms);
        arr.add(templateId);
        return arr;
    }

    public HashMap<String, String> fetchApplicantRODetail(String stat_form_type, String stat_reg_no) {
        HashMap<String, String> hm = new HashMap<String, String>();
        //Connection con = null;
        try {

            //con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection " + e.getMessage());
        }
        String qry = "", tblName = "";

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getUserComDetail in Inform.java value of tblName is " + tblName + " stat_form_type " + stat_form_type);

        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
            tblName = "sms_registration";

        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = "single_registration";

        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
            tblName = "bulk_registration";

        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
            tblName = "ip_registration";

        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {

            tblName = "nkn_registration";

        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {

            tblName = "relay_registration";

        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {

            tblName = "ldap_registration";

        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {

            tblName = "distribution_registration";

        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {

            tblName = "imappop_registration";

        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {

            tblName = "gem_registration";

        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {

            tblName = "mobile_registration";

        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {

            tblName = "dns_registration";

        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {

            tblName = "wifi_registration";

        } else if (stat_form_type.contains(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.contains(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.contains(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.contains(Constants.VPN_RENEW_FORM_KEYWORD) || stat_form_type.contains("vpnrenew")) {

            tblName = "vpn_registration";

        } else if (stat_form_type.equals(Constants.FIREWALL_FORM_KEYWORD)) {

            tblName = "centralutm_registration";

        } else if (stat_form_type.equals(Constants.WEBCAST_FORM_KEYWORD)) {

            tblName = "webcast_registration";

        } else if (stat_form_type.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {

            tblName = "email_act_registration";

        } else if (stat_form_type.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {

            tblName = "email_deact_registration";

        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {

            tblName = Constants.DAONBOARDING_TABLE_NAME;

        } else if (stat_form_type.equals(Constants.WIFI_PORT_FORM_KEYWORD)) {

            tblName = Constants.WIFI_PORT_TABLE_NAME;

        } else if (stat_form_type.equals(Constants.DOR_EXT_FORM_KEYWORD)) {

            tblName = Constants.DOR_EXT_TABLE_NAME;

        }

        qry = "SELECT auth_off_name, auth_email, mobile, designation, ministry, department, organization, other_dept, hod_name, "
                + " hod_email, hod_mobile, ca_desig "
                + "FROM " + tblName + " WHERE registration_no = ?";

        PreparedStatement ps = null;

        ResultSet res = null;

        try {
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getUserComDetail func query is " + ps);

            res = ps.executeQuery();

            String min_dept_org = "";

            while (res.next()) {
                // set email and mobile in the Forms object

                // applicant details
                hm.put("name", res.getString("auth_off_name"));

                hm.put("email", res.getString("auth_email"));

                hm.put("mobile", res.getString("mobile"));

                hm.put("desig", res.getString("designation"));

                if (res.getString("ministry") != null && !res.getString("ministry").equals("")) {
                    min_dept_org = res.getString("ministry");
                }

                if (res.getString("department") != null && !res.getString("department").equals("")) {
                    if (!res.getString("department").equalsIgnoreCase("other")) {
                        min_dept_org += " " + res.getString("department");
                    } else {
                        min_dept_org += " " + res.getString("other_dept");
                    }

                }

                if (res.getString("organization") != null && !res.getString("organization").equals("")) {
                    min_dept_org += " " + res.getString("organization");
                }

                hm.put("ministry", min_dept_org);

                // RO Details
                hm.put("ro_name", res.getString("hod_name"));

                hm.put("ro_email", res.getString("hod_email"));

                hm.put("ro_mobile", res.getString("hod_mobile"));

                hm.put("ro_desig", res.getString("ca_desig"));

            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection: " + stat_form_type + " - " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return hm;

    }

    // text for mail body to send in case of completed action
    public ArrayList<String> fetchCompMailText(String userType, String reg_no, String form_type, String uids,
            String password, String mobile, String adminType) {

        HashMap hm = fetchLastUpdatedStatus(reg_no);

        String stat_forwarded_by_user = hm.get("stat_forwarded_by_user").toString();

        String stat_forwarded_to_user = hm.get("stat_forwarded_to_user").toString();

        String stat_remarks = hm.get("stat_remarks").toString(); // line added by pr on 26thfeb18

        String by = "";
        String to = "";
        String templateId = "";

        if (stat_forwarded_by_user != null && !stat_forwarded_by_user.equals("")) {

            by = by + "(" + stat_forwarded_by_user + ")";
        }

        if (stat_forwarded_to_user != null && !stat_forwarded_to_user.equals("")) {

            to = to + "(" + stat_forwarded_to_user + ")";
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getCompMailText userType is " + userType);

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "";

        if (userType.equals("user")) {
            if (form_type.equals(Constants.SMS_FORM_KEYWORD)) {
                text = "<p>Dear Sir/Madam,</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>As desired the&nbsp;NIC&nbsp;SMS&nbsp;Gateway user account has been created and below mentioned are the login credentials.<br />\n"
                        + "User name: " + uids + "<br />\n"
                        + "Password: will be sent to your mobile number mentioned in the form.(" + mobile + ")</p>\n"
                        + "\n"
                        + "<p><br />\n"
                        + "Kindly integrate the below mentioned url with your application and "
                        + " find below documents for your reference : <br><br> "
                        + ""
                        + ""
                        + "1. <a href='https://msgapp.emailgov.in/OnlineForms/NicGuidelines.jsp'>NIC&nbsp;SMS&nbsp;GATEWAY&nbsp;SERVICE&nbsp;Integration document</a>,<br />"
                        + "2. <a href='https://msgapp.emailgov.in/OnlineForms/Documents.jsp'>TRAI exempted Sender ID template for your reference.</a>&nbsp;&nbsp;</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p><strong>a) Integration URL&nbsp;</strong><br />\n"
                        + "<a href=\"https://smsgw.sms.gov.in/failsafe/HttpLink?username=xxxxxx&amp;pin=xxxxxxx&amp;message=message&amp;mnumber=91XXXXXXXXXX&amp;signature=SENDERID\" target=\"_blank\">https://smsgw.sms.gov.in/failsafe/HttpLink?username=xxxxxx&amp;pin=xxxxxxx&amp;message=message&amp;mnumber=91XXXXXXXXXX&amp;signature=SENDERID</a>&nbsp;</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>Please encode the special character from your pin with below details.<br />\n"
                        + "<br />\n"
                        + "$ - %24<br />\n"
                        + "# - %23<br />\n"
                        + "&amp; - %26<br />\n"
                        + "% - %25<br />\n"
                        + "<br />\n"
                        + "<strong>b) IP whitelisting</strong>&nbsp;</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>kinldy whitelist NIC SMS Gateway IP &amp; DOMAIN at your firewall end.<br />\n"
                        + "<br />\n"
                        + "DOMAIN:smsgw.sms.gov.in<br />\n"
                        + "Push IP:164.100.14.211 ,164.100.112.139<br />\n"
                        + "port: 443</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<ul>\n"
                        + "	<li>Whether you are able to telnet smsgw.sms.gov.in 443 or telnet smsgw.sms.gov.in 80.&nbsp;&nbsp;</li>\n"
                        + "	<li>If it is not happening, And, if you are behind NICNET firewall, please coordinate with Cyber Security Division. Rule must be allowed at firewall to avail the service.</li>\n"
                        + "	<li>If your firewall rule is allowed and still you are not able to telnet, please disable the local firewall at your server else coordinate with Network administrator.</li>\n"
                        + "</ul>\n"
                        + "\n"
                        + "<p><strong>c) TRAI Exempted Sender ID</strong></p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>Please find the TRAI ADDRESS &amp; CONTACT details :</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>Advisor, QoS<br />\n"
                        + "Phone: 23230404, FAX: 23213036 Email:&nbsp;advqos@trai.gov.in<br />\n"
                        + "Mahanagar Doorsanchar Bhawan (next to Zakir Hussain College)<br />\n"
                        + "Jawaharlal Nehru Marg (Old Minto Road) New Delhi: 110 002<br />\n"
                        + "Phone no :&nbsp;<a href=\"callto:91-11-2323 6308\" target=\"_blank\">91-11-2323 6308</a>&nbsp;(Reception)&nbsp;<a href=\"callto:2323 3466,2322\" target=\"_blank\">2323 3466,2322</a><br />\n"
                        + "FAX No. :&nbsp;<a href=\"callto:91-11-2321 3294\" target=\"_blank\">91-11-2321 3294</a>(Reception)<br />\n"
                        + "Website: trai.gov.in</p>\n"
                        + "\n"
                        + "<p><br />\n"
                        + "Please download the TRAI Exempted template from the link mentioned above.Send the Sender ID details to the TRAI as per the template and get approval from TRAI.<br />\n"
                        + "Once TRAI approves, please send the scanned copy of the approval to&nbsp;smssupport@gov.in.<br />\n"
                        + "Once the Sender ID is approved by the TRAI, the same will be configured on&nbsp;SMS&nbsp;gateway. Then you can use the same for sending&nbsp;SMS.<br />\n"
                        + "<br />\n"
                        + "&nbsp;</p>\n"
                        + "\n"
                        + "<p>Thanks and Regards,</p>\n"
                        + "\n"
                        + "<p>SMS-Support<br />\n"
                        + "Website:&nbsp;<a href=\"https://servicedesk.nic.in/\" target=\"_blank\">https://servicedesk.nic.in</a><br />\n"
                        + "NIC&nbsp;Service&nbsp;Desk (Toll-Free):-&nbsp;<a href=\"callto:1800111555\" target=\"_blank\">1800111555</a></p>";

                sub = "Application Registration Number - " + reg_no + " processing Completed.";

                sms = "Dear Applicant, Reg. No.: " + reg_no + " on eForms has been Approved  by " + by + " and ID has been Created.Auth ID is " + uids + " Password is " + password + " NICSI";
                templateId = "1107160811738984079";
            } else if (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || form_type.equals(Constants.GEM_FORM_KEYWORD)) // line modified by pr on 18thdec17
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getCompMailText func in single nkn gem block ");

                text = "Dear Applicant, <br><br>Your application on eForms with Registration number - " + reg_no + " has been approved and ID has been Created by " + by + ".<br>New Email ID is "
                        + uids + "<br>Password has been sent to your registered Mobile "
                        + mobile + ".";

                if (!stat_remarks.equals("")) // if added by pr on 3rdoct19
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team"; // line added by pr on 3rdoct19

                sub = "Application Registration Number - " + reg_no + " processing Completed ";

                /*sms = "Dear Applicant, Application No. - " + reg_no + " on eForms has been Approved by " + by + " and ID has been Created.New Email ID is "
                        + uids + ", Password is " + password; // text changed from user to Applicant by pr on 22ndoct18*/
                // above line modified by pr on 18thapr19
                sms = "Registration No. : " + reg_no + ", status : Completed, ID : " + uids + ", Password : " + password + " , Login @ : https://email.gov.in/ NICSI";
                templateId = "1107160810967654447";
            } else {
                text = "Dear Applicant, <br>Your application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by;

                if (!stat_remarks.equals("")) // if added by pr on 26thfeb18
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team";

                sub = "Application Registration Number - " + reg_no + " processing Completed";

                sms = "Dear Applicant, Your application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by + ". NICSI";
                templateId = "1107160811764409403";

            }

        } else if (userType.equals("admin")) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " getCompMailText func userType admin form_type value is " + form_type);

            if (form_type.equals(Constants.SMS_FORM_KEYWORD)) {
                text = "Dear " + adminType + ", <br><br>An application with Registration number - "
                        + reg_no + " has been completed by " + by + ". and Auth ID has been created.<br>Auth ID is : "
                        + uids + "<br>Password has been sent to the Applicant's registered mobile "
                        + mobile + ".<br><br>Regards,<br>eForms Team";

                sub = "Application Registration Number - " + reg_no + " processing Completed";
                //12/1/2022 for incorrect template
                sms = "Dear  " + adminType + " , Reg. No.:  " + reg_no + "  has been completed and ID has been created is  " + uids + " NICSI";
                //sms = "Dear " + adminType + ", Reg. No.: " + reg_no + " has been completed and ID has been created is " + uids + " NICSI";
                templateId = "1107160811058959715";

            } else if (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || form_type.equals(Constants.GEM_FORM_KEYWORD)) // line modified by pr on 30thjan18
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside userType admin gem block  ");

                text = "Dear " + adminType + ", <br><br>An application on eForms with Registration number - " + reg_no + " "
                        + ""
                        + "has been completed by " + by + " and email has been created.<br>New Email ID is " + uids + "";

                if (!stat_remarks.equals("")) // if added by pr on 3rdoct19
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team";// if added by pr on 3rdoct19

                sub = "Application Registration Number - " + reg_no + " processing Completed";

                sms = "Dear " + adminType + ", Reg. No.: " + reg_no + " has been completed by " + by + "." + " New Email ID has been created.Email is " + uids + " NICSI";
                templateId = "1107160811815380008";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " text is  " + text + " sms is " + sms);
            } else {
                text = "Dear " + adminType + ", <br>An application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by;

                if (!stat_remarks.equals("")) // if added by pr on 26thfeb18
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team";

                sub = "Application Registration Number - " + reg_no + " processing Completed ";

                sms = "Dear " + adminType + ", An application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by + ". NICSI";
                //12/1/2022 for incorrect template
//                templateId = "1107160811058959715";
                templateId = "1107160811141946915";
            }

        }

        arr.add(text);

        arr.add(sub);

        arr.add(sms);

        arr.add(templateId);

        return arr;
    }

    public String sendSMS(String msg, String mobile)// function added by p on 18thmay17
    {

        System.out.println(" inside sendsms function mobile is " + mobile + "  msg is " + msg);

        String mid = "";

//        try {
//            msg = URLEncoder.encode(msg);
//
//            String smspass_encoded = URLEncoder.encode(smspass);
//
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " after smspass_encoded " + smspass_encoded);
//
//            URL ur = new URL("http://" + smsip + "/failsafe/HttpLink?username=" + smsuname + "&pin=" + smspass_encoded + "&message=" + msg + "&mnumber=" + mobile + "&signature=NICSMS&custref=onlineforms");
//
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " SMS URL is " + ur);
//
//           InputStream respons = ur.openStream();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(respons));
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("Request ID=")) {
//                    mid = line.substring(line.indexOf("Request ID=") + 12, line.indexOf("~"));
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ "TEST: " + line);
//                }
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ "MID: '" + mid);
//            } // to be uncommented in production
//
//        } catch (Exception e) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ "Error in SMS Sending =>" + e);
//        }
        return mid;

    }

    // below function modified by pr on 2ndjan18
//    public ArrayList<String> fetchUserComDetail(String stat_reg_no, String stat_form_type) {
//        ArrayList<String> arr = new ArrayList<String>(); // first element emaila nd second element mobile
//        //Connection con = null;
//        try {
////            Class.forName("com.mysql.jdbc.Driver");
////            con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);
//            //con = entities.MyConnection.getConnection();
////            con = DbConnection.getConnection();
//            conSlave = DbConnection.getSlaveConnection();
//        } catch (Exception e) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection " + e.getMessage());
//        }
//        String qry = "", tblName = "";
//
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchUserComDetail in Inform.java value of tblName is " + tblName + " stat_form_type " + stat_form_type);
//
//        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
//            tblName = "sms_registration";
//
//        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
//            tblName = "single_registration";
//
//        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
//            tblName = "bulk_registration";
//
//        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
//            tblName = "ip_registration";
//
//        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
//
//            tblName = "nkn_registration";
//
//        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {
//
//            tblName = "relay_registration";
//
//        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {
//
//            tblName = "ldap_registration";
//
//        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {
//
//            tblName = "distribution_registration";
//
//        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {
//
//            tblName = "imappop_registration";
//
//        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {
//
//            tblName = "gem_registration";
//
//        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {
//
//            tblName = "mobile_registration";
//
//        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {
//
//            tblName = "dns_registration";
//
//        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
//
//            tblName = "wifi_registration";
//
//        } else if (stat_form_type.equals(Constants.WIFI_PORT_FORM_KEYWORD)) {
//
//            tblName = "wifiport_registration";
//
//        } else if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD)) {
//
//            tblName = "vpn_registration";
//
//        } else if (stat_form_type.equals(Constants.WEBCAST_FORM_KEYWORD)) {
//
//            tblName = "webcast_registration";
//
//        } else if (stat_form_type.equals(Constants.FIREWALL_FORM_KEYWORD)) {
//
//            tblName = "centralutm_registration";
//
//        } else if (stat_form_type.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {
//
//            tblName = "email_act_registration";
//
//        } else if (stat_form_type.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {
//
//            tblName = "email_deact_registration";
//
//        } 
//        else if (stat_form_type.equals(Constants.DOR_EXT_FORM_KEYWORD)) {
//
//            tblName = "dor_ext_registration";
//
//        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {
//
//            tblName = Constants.DAONBOARDING_TABLE_NAME;
//
//        }
//
//        qry = "SELECT auth_email, mobile FROM " + tblName + " WHERE registration_no = ?";
//
//        PreparedStatement ps = null;
//
//        ResultSet res = null;
//
//        try {
//            ps = conSlave.prepareStatement(qry);
//            ps.setString(1, stat_reg_no);
//
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchUserComDetail func query is " + ps);
//
//            res = ps.executeQuery();
//            while (res.next()) {
//                // set email and mobile in the Forms object
//                arr.add(res.getString("auth_email"));
//                arr.add(res.getString("mobile"));
//            }
//        } catch (Exception e) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection: " + stat_form_type + " - " + e.getMessage());
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (res != null) {
//                try {
//                    res.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (con != null) {
//                try {
//                    // con.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return arr;
//
//    }
    public ArrayList<String> fetchUserComDetail(String stat_reg_no, String stat_form_type) {
        ArrayList<String> arr = new ArrayList<String>(); // first element emaila nd second element mobile
        //Connection con = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);
            //con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection " + e.getMessage());
        }
        String qry = "", tblName = "";

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getUserComDetail in Inform.java value of tblName is " + tblName + " stat_form_type " + stat_form_type);

        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
            tblName = "sms_registration";

        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = "single_registration";

        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
            tblName = "bulk_registration";

        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
            tblName = "ip_registration";

        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {

            tblName = "nkn_registration";

        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {

            tblName = "relay_registration";

        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {

            tblName = "ldap_registration";

        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {

            tblName = "distribution_registration";

        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {

            tblName = "imappop_registration";

        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {

            tblName = "gem_registration";

        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {

            tblName = "mobile_registration";

        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {

            tblName = "dns_registration";

        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {

            tblName = "wifi_registration";

        } else if (stat_form_type.equals(Constants.WIFI_PORT_FORM_KEYWORD)) {

            tblName = "wifiport_registration";

        } else if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD)) {

            tblName = "vpn_registration";

        } else if (stat_form_type.equals(Constants.WEBCAST_FORM_KEYWORD)) {

            tblName = "webcast_registration";

        } else if (stat_form_type.equals(Constants.FIREWALL_FORM_KEYWORD)) {

            tblName = "centralutm_registration";

        } else if (stat_form_type.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {

            tblName = "email_act_registration";

        } else if (stat_form_type.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {

            tblName = "email_deact_registration";

        } else if (stat_form_type.equals(Constants.DOR_EXT_FORM_KEYWORD)) {

            tblName = "dor_ext_registration";

        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {

            tblName = Constants.DAONBOARDING_TABLE_NAME;

        }

        if (tblName.equalsIgnoreCase("single_registration")){
        qry = "SELECT auth_email, mobile, other_applicant_mobile, other_applicant_email, request_flag FROM " + tblName + " WHERE registration_no = ?";
          }
        else {
          qry = "SELECT auth_email, mobile FROM " + tblName + " WHERE registration_no = ?";
        }
        PreparedStatement ps = null;

        ResultSet res = null;

        try {
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getUserComDetail func query is " + ps);

            res = ps.executeQuery();
            while (res.next()) {
                // set email and mobile in the Forms object

                if (tblName.equalsIgnoreCase("single_registration")) {
                    if (res.getString("request_flag").equalsIgnoreCase("y")) {
                        arr.add(res.getString("other_applicant_email"));
                        arr.add(res.getString("other_applicant_mobile"));
                        arr.add(res.getString("auth_email"));
                        arr.add(res.getString("mobile"));
                    } else {
                        arr.add(res.getString("auth_email"));
                        arr.add(res.getString("mobile"));
                    }

                } else {
                    arr.add(res.getString("auth_email"));
                    arr.add(res.getString("mobile"));
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection: " + stat_form_type + " - " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return arr;

    }

    public void sendUpdateToUser(String stat_reg_no, String stat_form_type, String roleText, String from) {
        // send mail and sms to the user
        String userEmail = "", userMobile = "", emailMessage = "", emailSubject = "", smsMessage = "", templateId = "";
        ArrayList<String> userArr = fetchUserComDetail(stat_reg_no, stat_form_type);

        if (userArr.get(0) != null) {
            userEmail = userArr.get(0);
        }

        if (userArr.get(1) != null) {
            userMobile = userArr.get(1);
        }

        String role = fetchSessionRole();

        ArrayList userText = fetchUpdateMailText("user", stat_reg_no, roleText);

        if (userText.get(0) != null) {
            emailMessage = userText.get(0).toString();
        }

        if (userText.get(1) != null) {
            emailSubject = userText.get(1).toString();
        }

        if (userText.get(2) != null) {
            smsMessage = userText.get(2).toString();
        }
        if (userText.get(3) != null) {
            templateId = userText.get(3).toString();
        }

        /*if (!userEmail.equals("")) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " send update user ");

            sendMail(userEmail, emailMessage, emailSubject, from);
        }

        if (!userMobile.equals("")) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendSMS user ");

            sendSMS(smsMessage, userMobile);
        }*/
        // above line commented below added by pr on 23rdoct18
        sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

    }

    public void sendUpdateToCA(String stat_reg_no, String stat_form_type, String roleText, String from) {

        String emailTo = "", smsMobile = "", emailMessage = "", emailSubject = "", smsMessage = "", templateId = "";

        // send mail and sms to the ca
        ArrayList arrID = fetchAdminId(stat_reg_no, "ca");

        ArrayList adminText = fetchUpdateMailText("ca", stat_reg_no, roleText);

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }
        if (adminText.get(3) != null) {
            templateId = adminText.get(3).toString();
        }

        if (arrID != null && arrID.get(0) != null) {
            emailTo = arrID.get(0).toString();
        }

        if (arrID != null && arrID.get(1) != null) {
            smsMobile = arrID.get(1).toString();
        }

        /*if (!emailTo.equals("")) {

            sendMail(emailTo, emailMessage, emailSubject, from);
        }

        if (!smsMobile.equals("")) {

            sendSMS(smsMessage, smsMobile);
        }*/
        // above line commented below added by pr on 23rdoct18
        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

    }

    public void sendUpdateToSup(String stat_reg_no, String stat_form_type, String roleText, String from) {

        String emailTo = "", smsMobile = "", emailMessage = "", emailSubject = "", smsMessage = "";

        ArrayList adminText = fetchUpdateMailText("sup", stat_reg_no, roleText);

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }

        /*emailTo = "support@nic.in";

        emailTo = "preeti.nhq@nic.in";*/
        emailTo = sup_email;// above code modified by pr on 2ndjan18

        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) // if added by pr on 23rdfeb18
        {
            emailTo = "smssupport@gov.in";
        }

        if (!emailTo.equals("") && !stat_form_type.equals(Constants.DNS_FORM_KEYWORD))// as for dns form support is skipped
        {
            //sendMail(emailTo, emailMessage, emailSubject, from);

            // above line commented below added by pr on 23rdoct18
            sendToRabbit(from, emailTo, emailMessage, emailSubject, "", "", null, null, null);

        }

    }

    // not used as of now in the sendUpdateIntimation function
    public void sendUpdateToCoord(String stat_reg_no, String stat_form_type, String roleText, String from) {

        String emailTo = "", smsMobile = "", emailMessage = "", emailSubject = "", smsMessage = "", templateId = "";

        ArrayList adminText = fetchUpdateMailText("sup", stat_reg_no, roleText);

        if (adminText.get(0) != null) {
            emailMessage = adminText.get(0).toString();
        }

        if (adminText.get(1) != null) {
            emailSubject = adminText.get(1).toString();
        }

        if (adminText.get(2) != null) {
            smsMessage = adminText.get(2).toString();
        }
        if (adminText.get(3) != null) {
            templateId = adminText.get(3).toString();
        }

        if (!stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) // if not dns form then execute below else go to else
        {
            // get coordinator details email and mobile for this reg_no
            ArrayList arrID = fetchAdminId(stat_reg_no, "c");// return 0 -> email and 1 -> mobile

            if (arrID != null && arrID.get(0) != null) {
                emailTo = arrID.get(0).toString();
            }

            if (arrID != null && arrID.get(1) != null) {
                smsMobile = arrID.get(1).toString();
            }

            /*if (!emailTo.equals("")) {
                sendMail(emailTo, emailMessage, emailSubject, from);
            }

            if (!smsMobile.equals("")) {
                sendSMS(smsMessage, smsMobile);
            }*/
            // above line commented below added by pr on 23rdoct18
            sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
        } else {

            ArrayList arrID = fetchAdminId(stat_reg_no, "c");

            if (arrID != null && arrID.get(0) != null) {
                if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.lang.String"))) {
                    emailTo = "";

                    smsMobile = "";

                    emailTo = arrID.get(0).toString();

                    if (arrID.get(1) != null) {
                        smsMobile = arrID.get(1).toString();
                    }

                    // send mail and sms only to one coordinators
                    /*if (!emailTo.equals("")) {
                        sendMail(emailTo, emailMessage, emailSubject, from);
                    }

                    if (!smsMobile.equals("")) {
                        sendSMS(smsMessage, smsMobile);
                    }*/
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                } else if ((arrID.get(0).getClass().getName().trim().equalsIgnoreCase("java.util.HashMap"))) {
                    for (Object hm : arrID)// contains the hashmap of all email and mobile 
                    {
                        emailTo = "";

                        smsMobile = "";

                        HashMap h = (HashMap) hm;

                        if (h != null) {
                            if (h.get("email") != null) {
                                emailTo = h.get("email").toString();
                            }

                            if (h.get("mobile") != null) {
                                smsMobile = h.get("mobile").toString();
                            }

                            // send mail and sms only to all  coordinators one after another
                            /*if (!emailTo.equals("")) {
                                sendMail(emailTo, emailMessage, emailSubject, from);
                            }

                            if (!smsMobile.equals("")) {
                                sendSMS(smsMessage, smsMobile);
                            }*/
                            // above line commented below added by pr on 23rdoct18
                            sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                        }

                    }

                }
            }

        }

    }

    public void sendUpdateIntimation(String role, String reg_no, String form_type) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendUpdateIntimation func in Inform.java value of role is " + role + " reg_no is " + reg_no + " form_type is " + form_type);

        String roleText = "";

        String from = "eforms@nic.in";

        if (role.equals(Constants.ROLE_CA)) {
            //then send email and sms to the user

            roleText = "Reporting/Nodal/Forwarding Officer";

            sendUpdateToUser(reg_no, form_type, roleText, from);

        } else if (role.equals(Constants.ROLE_SUP)) {
            roleText = "Support";

            //then send email and sms to the user
            sendUpdateToUser(reg_no, form_type, roleText, from);

            //then send email and sms to the ca
            sendUpdateToCA(reg_no, form_type, roleText, from);

        } else if (role.equals(Constants.ROLE_CO)) {
            roleText = "Coordinator";

            //then send email and sms to the user
            sendUpdateToUser(reg_no, form_type, roleText, from);

            //then send email and sms to the ca
            if (!form_type.equals(Constants.WIFI_FORM_KEYWORD))// in case of wifi ca is skipped
            {
                sendUpdateToCA(reg_no, form_type, roleText, from);
            }

            //then send email to the support
            // in case of wifi and dns forms support is skipped
            if (!form_type.equals(Constants.WIFI_FORM_KEYWORD) && !form_type.equals(Constants.DNS_FORM_KEYWORD)) {
                sendUpdateToSup(reg_no, form_type, roleText, from);
            }
        } else if (role.equals(Constants.ROLE_CO)) {
            roleText = "Coordinator";

            //then send email and sms to the user
            sendUpdateToUser(reg_no, form_type, roleText, from);

            //then send email and sms to the ca
            if (!form_type.equals(Constants.WIFI_FORM_KEYWORD))// in case of wifi ca is skipped
            {
                sendUpdateToCA(reg_no, form_type, roleText, from);
            }

            //then send email to the support
            // in case of wifi and dns forms support is skipped
            if (!form_type.equals(Constants.WIFI_FORM_KEYWORD) && !form_type.equals(Constants.DNS_FORM_KEYWORD)) {
                sendUpdateToSup(reg_no, form_type, roleText, from);
            }
        }

    }

    // below function added by pr on 20thdec17
    public void sendCancelMail(String reg_no) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendCancelMail func reg_no is " + reg_no);
        String templateId = "";
        ArrayList arrID = fetchAdminId(reg_no, "ca");

        if (arrID != null && arrID.size() > 0) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside arraylist not null size is " + arrID.size());

            String text = "Dear Reporting/Nodal/Forwarding Officer, <br>An application with Registration number - " + reg_no + " on eForms has been Cancelled "
                    + ""
                    + "by the Applicant. <br>Regards,<br>eForms Team";

            String sub = "Application Registration Number - " + reg_no + " cancelled by the Applicant.";

            String sms = "Dear Reporting/Nodal/Forwarding Officer, Application No. - " + reg_no + " has been cancelled by the Applicant. NICSI";
            templateId = "1107160811189565506";

            String emailTo = "", smsMobile = "";

            String from = "eforms@nic.in";

            if (arrID != null && arrID.get(0) != null) {
                emailTo = arrID.get(0).toString();
            }

            if (arrID != null && arrID.get(1) != null) {
                smsMobile = arrID.get(1).toString();
            }

            /*if (!emailTo.equals("")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " user cancel send mail  emailTo  "+emailTo);

                    sendMail(emailTo, text, sub, from);
                }

                if (!smsMobile.equals("")) 
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " user cancel send sms  smsMobile "+smsMobile);

                    sendSMS(sms, smsMobile);
                }*/
            // above line commented below added by pr on 23rdoct18
            sendToRabbit(from, emailTo, text, sub, sms, smsMobile, null, null, templateId);
        }
    }

    // below function added to send email and sms at the time of updation
    // 0 -> email message 1 -> email subject 2 -> sms message // get user text in case of forward and reject case
    public ArrayList<String> fetchUpdateMailText(String userType, String reg_no, String by) {

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "", templateId = "";

        String title = "", addressing = "";

        if (userType.equals("user")) {
            title = "Applicant";

            addressing = "Your";
        } else if (userType.equals("ca")) {
            title = "Reporting/Nodal/Forwarding Officer";

            addressing = "An";
        } else if (userType.equals("sup")) {
            title = "Support";

            addressing = "An";
        } else if (userType.equals("co")) {
            title = "Coordinator";

            addressing = "An";
        }

        text = "Dear " + title + ", <br>" + addressing + " application with Registration number - " + reg_no + " on eForms has been updated "
                + ""
                + "by the " + by + ". <br>Regards,<br>eForms Team";

        sub = "Application Registration Number - " + reg_no + " updated by the " + by;

        sms = "Dear Applicant, Application No. - " + reg_no + " has been updated by the " + by + ". NICSI";
        templateId = "1107160811203378994";

        arr.add(text);

        arr.add(sub);

        arr.add(sms);
        arr.add(templateId);

        return arr;
    }

    // 0 -> cn (name) 1 -> mobile
    public ArrayList fetchLDAPName(String email) {

        ArrayList<String> data = new ArrayList<String>();

        //ServletContext sctx = getServletContext();
        /*String host = sctx.getInitParameter("host");
        String port = sctx.getInitParameter("port");
        String dn = sctx.getInitParameter("binddn");
        String dn_pass = sctx.getInitParameter("bindpass");*/
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null;// line added by pr on 23rdoct18

        try {

            //ctx = new InitialDirContext(ht);
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress", "cn", "mobile"};
            String[] IDs = {"uid", "mobile", "cn"};

            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";

            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();

                //String uids = t.substring(t.indexOf("uid:") + 5, t.length() - 1);
                t = t.substring(t.indexOf("{") + 1, t.indexOf("}"));

                String mobile_str = t.substring(t.indexOf("mobile:") + 8);

                String mobile = mobile_str.substring(0);

                if (mobile_str.indexOf(",") >= 0) {
                    mobile = mobile_str.substring(0, mobile_str.indexOf(","));
                }

                String cn_str = t.substring(t.indexOf("cn:") + 4);

                String cn = cn_str.substring(0);

                if (cn_str.indexOf(",") >= 0) {
                    cn = cn_str.substring(0, cn_str.indexOf(","));
                }

                String uid_str = t.substring(t.indexOf("uid:") + 5);

                String uid = uid_str.substring(0);

                if (uid_str.indexOf(",") >= 0) {
                    uid = uid_str.substring(0, uid_str.indexOf(","));
                }

                //data.add(cn); // oth position
                data.add(email); // oth position

                data.add(mobile); // 1st position

                data.add("true"); // 2nd

                data.add(cn); // 3rd line added by pr on 10thjan19
            } else {
                data.add(email); // oth position

                data.add(""); // 1st position

                data.add("false"); // 2nd

                data.add(""); // 3rd line added by pr on 10thjan19

            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 23rdoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }

        return data;
    }

    //public void sendRevokeIntimation( String reg_no, String form_type, String forwarded_to_user, String pulledFrom ) // pulledFrom parameter added by pr on 16thmar18
    public void sendRevokeIntimation(String reg_no, String form_type, String forwarded_to_user, String pulledFrom, String statRemarks) // modified by pr on 6thjun18, statRemarks parameter added
    {
        System.out.println(" inside sendrevoke func ");

        HashMap<String, String> hm = fetchStatusAdmins(reg_no);
        String templateId = "";

        for (Object o : hm.keySet()) {
            System.out.println(" key is " + o.toString() + " value is " + hm.get(o.toString()));
        }

        String role = fetchSessionRole();

        String email = fetchSessionEmail();

        String from = "eforms@nic.in";

        String userEmail = "", userMobile = "", emailMessage = "", emailSubject = "", smsMessage = "";

        if (role.equals(Constants.ROLE_CO)) // forwarded from coordinator to support
        {
            // send email and sms to the user

            ArrayList<String> userArr = fetchUserComDetail(reg_no, form_type);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

            if (userArr.get(0) != null) {
                userEmail = userArr.get(0);
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

            if (userArr.get(1) != null) {
                userMobile = userArr.get(1);
            }
            //Validation validation = new Validation();
            if (userMobile == null || userMobile.isEmpty()) {
                userMobile = "";
            }
//            if (!validation.checkFormat("mobile", userMobile)) {
//                userMobile = "";
//            }
            if (userMobile.startsWith("+")) {
                userMobile = userMobile.substring(1);
            }

            ArrayList userText = fetchMailText(reg_no, "u", "inform", forwarded_to_user, statRemarks); // 0 -> mailbody, 1-> sub -> sms text, statRemarks added by pr on 6thjun18 forwarded_to_user passed

            if (userText.get(0) != null) {
                emailMessage = userText.get(0).toString();
            }

            if (userText.get(1) != null) {
                emailSubject = userText.get(1).toString();
            }

            if (userText.get(2) != null) {
                smsMessage = userText.get(2).toString();
            }

            if (userText.get(3) != null) {
                templateId = userText.get(3).toString();
            }

            /*if (!userEmail.equals("")) {
                
                System.out.println(" userEmail "+userEmail+" emailMessage "+emailMessage+" emailSubject "+emailSubject );
                
                
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                sendMail(userEmail, emailMessage, emailSubject, from);
            }
            
            if (!userMobile.equals("")) {
                 
                 System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile);
                 
                        sendSMS(smsMessage, userMobile);
                }*/
            // above line commented below added by pr on 23rdoct18
            sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

            // send email and sms to the back heirarchy except support
            if (hm.get("ca") != null) // if exists in the heirarchy
            {
                String ca_id = hm.get("ca").toString();

                //userArr = fetchCADetail(ca_id);
                userArr = fetchCADetail(reg_no); // line modified by pr on 20thfeb19

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

                if (userArr.get(0) != null) {
                    userEmail = userArr.get(0);
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

                if (userArr.get(1) != null) {
                    userMobile = userArr.get(1);
                }
                if (userMobile == null || userMobile.isEmpty()) {
                    userMobile = "";
                }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
                if (userMobile.startsWith("+")) {
                    userMobile = userMobile.substring(1);
                }
                userText = fetchMailText(reg_no, "ca", "inform", forwarded_to_user, statRemarks); // 0 -> mailbody, 1-> sub -> sms text, statRemarks added by pr on 6thjun18 forwarded_to_user passed

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }


                /*if (!userEmail.equals("")) {
                    
                     System.out.println(" userEmail "+userEmail+" emailMessage "+emailMessage+" emailSubject "+emailSubject );
                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                    sendMail(userEmail, emailMessage, emailSubject, from);
                }
                
                if (!userMobile.equals("")) {
                    
                    System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile);
                    
                        sendSMS(smsMessage, userMobile);
                }*/
                // above line commented below added by pr on 23rdoct18
                sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

            }

            // send a different message to support
            if (hm.get("s") != null) // if exists in the heirarchy
            {
                userText = fetchMailText(reg_no, "s", "approve", forwarded_to_user, statRemarks); // 0 -> mailbody, 1-> sub -> sms text, statRemarks added by pr on 6thjun18 forwarded_to_user passed

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                userEmail = sup_email;

                if (!userEmail.equals("")) {

                    System.out.println(" userEmail " + userEmail + " emailMessage " + emailMessage + " emailSubject " + emailSubject);

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                    //sendMail(userEmail, emailMessage, emailSubject, from);
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, userEmail, emailMessage, emailSubject, "", "", null, null, templateId);

                }

            }

        } else if (role.equals(Constants.ROLE_MAILADMIN)) {

            //email = "support@nic.in"; // for testing
            if (email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("support@dummy.nic.in"))// support to DA
            {
                // send email and sms to the user // forwarded_to_user

                ArrayList<String> userArr = fetchUserComDetail(reg_no, form_type);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

                if (userArr.get(0) != null) {
                    userEmail = userArr.get(0);
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

                if (userArr.get(1) != null) {
                    userMobile = userArr.get(1);
                }
                if (userMobile == null || userMobile.isEmpty()) {
                    userMobile = "";
                }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
                if (userMobile.startsWith("+")) {
                    userMobile = userMobile.substring(1);
                }

                ArrayList userText = fetchMailText(reg_no, "u", "inform", forwarded_to_user, statRemarks);// 0 -> mailbody, 1-> sub -> sms text, statRemarks added by pr on 6thjun18 forwarded_to_user passed

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                /*if (!userEmail.equals("")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                    System.out.println(" userEmail "+userEmail+" emailMessage "+emailMessage+" emailSubject "+emailSubject );

                    sendMail(userEmail, emailMessage, emailSubject, from);
                }

                if (!userMobile.equals("")) 
                {
                     
                    System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile );
                     
                    sendSMS(smsMessage, userMobile);
                }*/
                // above line commented below added by pr on 23rdoct18
                sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

                // send email and sms to the back heirarchy 
                // send email and sms to the back heirarchy except support
                if (hm.get("ca") != null) // if exists in the heirarchy
                {
                    String ca_id = hm.get("ca").toString();

                    //userArr = fetchCADetail(ca_id);
                    userArr = fetchCADetail(reg_no); // line modified by pr on 20thfeb19

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

                    if (userArr.get(0) != null) {
                        userEmail = userArr.get(0);
                    }

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

                    if (userArr.get(1) != null) {
                        userMobile = userArr.get(1);
                    }
                    if (userMobile == null || userMobile.isEmpty()) {
                        userMobile = "";
                    }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
                    if (userMobile.startsWith("+")) {
                        userMobile = userMobile.substring(1);
                    }

                    userText = fetchMailText(reg_no, "ca", "inform", forwarded_to_user, statRemarks);// 0 -> mailbody, 1-> sub -> sms text, statRemarks added by pr on 6thjun18

                    if (userText.get(0) != null) {
                        emailMessage = userText.get(0).toString();
                    }

                    if (userText.get(1) != null) {
                        emailSubject = userText.get(1).toString();
                    }

                    if (userText.get(2) != null) {
                        smsMessage = userText.get(2).toString();
                    }
                    if (userText.get(3) != null) {
                        templateId = userText.get(3).toString();
                    }


                    /*if (!userEmail.equals("")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                        System.out.println(" userEmail "+userEmail+" emailMessage "+emailMessage+" emailSubject "+emailSubject );

                        sendMail(userEmail, emailMessage, emailSubject, from);
                    }

                    if (!userMobile.equals("")) 
                    {
                        
                        System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile );
                        
                            sendSMS(smsMessage, userMobile);
                    }*/
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

                }

                // send different message to the DA
                userText = fetchMailText(reg_no, "d", "approve", forwarded_to_user, statRemarks);// 0 -> mailbody, 1-> sub -> sms text , statRemarks added by pr on 6thjun18

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                if (forwarded_to_user.contains(",")) {
                    String[] arr = forwarded_to_user.split(",");

                    for (String s : arr) {
                        if (!s.equals("")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                            System.out.println(" userEmail " + s + " emailMessage " + emailMessage + " emailSubject " + emailSubject);

                            //sendMail(s, emailMessage, emailSubject, from);
                            // above line commented below added by pr on 23rdoct18
                            sendToRabbit(from, s, emailMessage, emailSubject, "", "", null, null, templateId);
                        }
                    }

                } else {

                    if (!forwarded_to_user.equals("")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                        System.out.println(" userEmail " + forwarded_to_user + " emailMessage " + emailMessage + " emailSubject " + emailSubject);

                        //sendMail(forwarded_to_user, emailMessage, emailSubject, from);
                        // above line commented below added by pr on 23rdoct18
                        sendToRabbit(from, forwarded_to_user, emailMessage, emailSubject, "", "", null, null, templateId);

                    }

                    /*if (!userMobile.equals("")) {
                            sendSMS(smsMessage, userMobile);
                    }*/
                }

                // below code added by pr on 9thmar18 to send sms to DAs
                // start, code added by pr on 9thmar18
                String[] daArr = null;
                if (forwarded_to_user.contains(",")) {
                    daArr = userEmail.split(",");
                } else {
                    daArr = new String[1];
                    daArr[0] = userEmail;
                }

                for (String s : daArr) {
                    String adminEmail = s;

                    if (!adminEmail.trim().toLowerCase().contains("vpnsupport"))// if added by pr on 21staug2020
                    {
                        ArrayList ldapArr = fetchLDAPName(adminEmail);

                        //if (ldapArr != null && ldapArr.size() == 3) {
                        if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19                    
                            if (ldapArr.get(1) != null) {
                                userMobile = ldapArr.get(1).toString();

                                if (!userMobile.equals("")) {
                                    System.out.println(" smsMessage " + smsMessage + " userMobile " + userMobile);

                                    //sendSMS(smsMessage, userMobile);
                                    sendToRabbit("", "", "", "", smsMessage, userMobile, null, null, templateId);

                                }

                            }
                        }
                    }

                }

                // send sms to the da as well for the approval
            } else // mailadmin to support mailadmin
            {
                // send email and sms to the user

                ArrayList<String> userArr = fetchUserComDetail(reg_no, form_type);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

                if (userArr.get(0) != null) {
                    userEmail = userArr.get(0);
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

                if (userArr.get(1) != null) {
                    userMobile = userArr.get(1);
                }
                if (userMobile == null || userMobile.isEmpty()) {
                    userMobile = "";
                }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
                if (userMobile.startsWith("+")) {
                    userMobile = userMobile.substring(1);
                }

                ArrayList userText = fetchMailText(reg_no, "u", "inform", forwarded_to_user, statRemarks);// 0 -> mailbody, 1-> sub -> sms text , statRemarks added by pr on 6thjun18 forwarded_to_user passed

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }
                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }


                /*if (!userEmail.equals("")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                    sendMail(userEmail, emailMessage, emailSubject, from);
                }

                if (!userMobile.equals("")) 
                {
                    System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile );
                     
                    sendSMS(smsMessage, userMobile);
                }*/
                // above line commented below added by pr on 23rdoct18
                sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);
                // send email and sms to the back heirarchy 
                if (hm.get("ca") != null) // if exists in the heirarchy
                {
                    String ca_id = hm.get("ca").toString();

                    //userArr = fetchCADetail(ca_id);
                    userArr = fetchCADetail(reg_no); // line modified by pr on 20thfeb19

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

                    if (userArr.get(0) != null) {
                        userEmail = userArr.get(0);
                    }

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

                    if (userArr.get(1) != null) {
                        userMobile = userArr.get(1);
                    }
                    if (userMobile == null || userMobile.isEmpty()) {
                        userMobile = "";
                    }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
                    if (userMobile.startsWith("+")) {
                        userMobile = userMobile.substring(1);
                    }

                    userText = fetchMailText(reg_no, "ca", "inform", forwarded_to_user, statRemarks);// 0 -> mailbody, 1-> sub -> sms text , statRemarks added by pr on 6thjun18 forwarded_to_user passed

                    if (userText.get(0) != null) {
                        emailMessage = userText.get(0).toString();
                    }

                    if (userText.get(1) != null) {
                        emailSubject = userText.get(1).toString();
                    }

                    if (userText.get(2) != null) {
                        smsMessage = userText.get(2).toString();
                    }

                    if (userText.get(3) != null) {
                        templateId = userText.get(3).toString();
                    }

                    /*if (!userEmail.equals("")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                        sendMail(userEmail, emailMessage, emailSubject, from);
                    }

                    if (!userMobile.equals("")) {
                        
                        System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile );
                        
                        sendSMS(smsMessage, userMobile);
                    }*/
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

                }

                // send different message to the mailadmin support
                if (hm.get("m") != null) // if exists in the heirarchy
                {

                    userEmail = hm.get("m").toString();

                    userText = fetchMailText(reg_no, "m", "approve", forwarded_to_user, statRemarks);// 0 -> mailbody, 1-> sub -> sms text, statRemarks added  by pr on 6thjun18 forwarded_to_user passed

                    if (userText.get(0) != null) {
                        emailMessage = userText.get(0).toString();
                    }

                    if (userText.get(1) != null) {
                        emailSubject = userText.get(1).toString();
                    }

                    if (userText.get(2) != null) {
                        smsMessage = userText.get(2).toString();
                    }

                    if (userText.get(3) != null) {
                        templateId = userText.get(3).toString();
                    }


                    /*if (!userEmail.equals("")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                    sendMail(userEmail, emailMessage, emailSubject, from);
                }*/ // commented by pr on 16thmar18
                    // start, code added by pr on 9thmar18
                    String[] arr = null;
                    if (userEmail.contains(",")) {
                        arr = userEmail.split(",");
                    } else {
                        arr = new String[1];
                        arr[0] = userEmail;
                    }

                    for (String s : arr) {
                        String adminEmail = s;

                        ArrayList ldapArr = fetchLDAPName(adminEmail);

                        if (!adminEmail.equals("")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                            //sendMail(adminEmail, emailMessage, emailSubject, from);
                            // above line commented below added by pr on 23rdoct18
                            sendToRabbit(from, adminEmail, emailMessage, emailSubject, "", "", null, null, templateId);

                        } // code taken from above to below by pr on 16thmar18

                        //if (ldapArr != null && ldapArr.size() == 3) {
                        if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19
                            if (ldapArr.get(1) != null) {
                                userMobile = ldapArr.get(1).toString();

                                if (!userMobile.equals("")) {
                                    System.out.println(" smsMessage " + smsMessage + " userMobile " + userMobile);

                                    //sendSMS(smsMessage, userMobile);
                                    // above line commented below added by pr on 23rdoct18
                                    sendToRabbit(from, adminEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

                                }

                            }
                        }

                    }

                }

            }
        } else if (role.equals(Constants.ROLE_SUP)) // pull back by support from coordinator / DA-Admin
        {
            String[] pulledFromArr = null;  // contains the d/c next email id of that d/c

            String pulledFromPanel = "", pulledFromUser = ""; // panel as in d or c user as in email/comma separated emails

            if (!pulledFrom.equals("") && pulledFrom.contains("~")) {
                pulledFromArr = pulledFrom.split("~");

                if (pulledFromArr.length > 0) {
                    if (pulledFromArr[0] != null) // this contains da or coordinator
                    {
                        pulledFromPanel = pulledFromArr[0];
                    }
                }

                if (pulledFromArr.length > 1) {
                    if (pulledFromArr[1] != null) // this contains da or coordinator emails
                    {
                        pulledFromUser = pulledFromArr[1];
                    }
                }
            }

            ArrayList<String> userArr = fetchUserComDetail(reg_no, form_type);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

            if (userArr.get(0) != null) {
                userEmail = userArr.get(0);
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

            if (userArr.get(1) != null) {
                userMobile = userArr.get(1);
            }
            if (userMobile == null || userMobile.isEmpty()) {
                userMobile = "";
            }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
            if (userMobile.startsWith("+")) {
                userMobile = userMobile.substring(1);
            }

            ArrayList userText = fetchPullMailText(reg_no, "u", pulledFromPanel + "(" + pulledFromUser + ")");// 0 -> mailbody, 1-> sub -> sms text

            if (userText.get(0) != null) {
                emailMessage = userText.get(0).toString();
            }

            if (userText.get(1) != null) {
                emailSubject = userText.get(1).toString();
            }

            if (userText.get(2) != null) {
                smsMessage = userText.get(2).toString();
            }

            if (userText.get(3) != null) {
                templateId = userText.get(3).toString();
            }

            /*if (!userEmail.equals("")) {
                
                System.out.println(" userEmail "+userEmail+" emailMessage "+emailMessage+" emailSubject "+emailSubject );
                
                
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                sendMail(userEmail, emailMessage, emailSubject, from);
            }
            
            if (!userMobile.equals("")) {
                 
                 System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile);
                 
                sendSMS(smsMessage, userMobile);
            }*/
            // above line commented below added by pr on 23rdoct18
            sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

            // send email and sms to the back heirarchy except support
            if (hm.get("ca") != null) // if exists in the heirarchy
            {
                String ca_id = hm.get("ca").toString();

                //userArr = fetchCADetail(ca_id);
                userArr = fetchCADetail(reg_no); // line modified by pr on 20thfeb19

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after user com details func call ");

                if (userArr.get(0) != null) {
                    userEmail = userArr.get(0);
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " userEmail is " + userEmail);

                if (userArr.get(1) != null) {
                    userMobile = userArr.get(1);
                }
                if (userMobile == null || userMobile.isEmpty()) {
                    userMobile = "";
                }
//                if (!validation.checkFormat("mobile", userMobile)) {
//                    userMobile = "";
//                }
                if (userMobile.startsWith("+")) {
                    userMobile = userMobile.substring(1);
                }

                userText = fetchPullMailText(reg_no, "ca", pulledFromPanel + "(" + pulledFromUser + ")");// 0 -> mailbody, 1-> sub -> sms text

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }


                /*if (!userEmail.equals("")) {
                    
                     System.out.println(" userEmail "+userEmail+" emailMessage "+emailMessage+" emailSubject "+emailSubject );
                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+ " support_pending sendMail user ");

                    sendMail(userEmail, emailMessage, emailSubject, from);
                }
                
                if (!userMobile.equals("")) {
                    
                    System.out.println(" smsMessage "+smsMessage+" userMobile "+userMobile);
                    
                        sendSMS(smsMessage, userMobile);
                }*/
                // above line commented below added by pr on 23rdoct18
                sendToRabbit(from, userEmail, emailMessage, emailSubject, smsMessage, userMobile, null, null, templateId);

            }

            // send a different message to support
            if (hm.get("s") != null) // if exists in the heirarchy
            {
                userText = fetchPullMailText(reg_no, "s", pulledFromPanel + "(" + pulledFromUser + ")");// 0 -> mailbody, 1-> sub -> sms text

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                userEmail = sup_email;

                if (!userEmail.equals("")) {

                    System.out.println(" userEmail " + userEmail + " emailMessage " + emailMessage + " emailSubject " + emailSubject);

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                    //sendMail(userEmail, emailMessage, emailSubject, from);
                    // above line commented below added by pr on 23rdoct18
                    sendToRabbit(from, userEmail, emailMessage, emailSubject, "", "", null, null, templateId);

                }

            }

            // send pull back information to Da or coordinator 
            if (pulledFromPanel.equalsIgnoreCase("DA-Admin")) {
                userText = fetchPullMailText(reg_no, "d", pulledFromPanel + "(" + pulledFromUser + ")");// 0 -> mailbody, 1-> sub -> sms text

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                // pulledFromUser contains one or more comma separated emails 
                String[] arr = null;
                if (pulledFromUser.contains(",")) {
                    arr = pulledFromUser.split(",");
                } else {
                    arr = new String[1];
                    arr[0] = pulledFromUser;
                }

                for (String s : arr) {
                    String adminEmail = s;

                    ArrayList ldapArr = fetchLDAPName(adminEmail);

                    if (!adminEmail.equals("")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                        //sendMail(adminEmail, emailMessage, emailSubject, from);
                        // above line commented below added by pr on 23rdoct18
                        sendToRabbit(from, adminEmail, emailMessage, emailSubject, "", "", null, null, templateId);//21staug2020

                    } // code taken from above to below by pr on 16thmar18

                    if (!adminEmail.trim().toLowerCase().contains("vpnsupport"))// if added by pr on 21staug2020
                    {

                        //if (ldapArr != null && ldapArr.size() == 3) {
                        if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19
                            if (ldapArr.get(1) != null) {
                                userMobile = ldapArr.get(1).toString();

                                if (!userMobile.equals("")) {
                                    System.out.println(" smsMessage " + smsMessage + " userMobile " + userMobile);

                                    //sendSMS(smsMessage, userMobile);
                                    // above line commented below added by pr on 23rdoct18
                                    sendToRabbit("", "", "", "", smsMessage, userMobile, null, null, templateId);

                                }

                            }
                        }

                    }

                }

            } else if (pulledFromPanel.equalsIgnoreCase("Coordinator")) {
                userText = fetchPullMailText(reg_no, "c", pulledFromPanel + "(" + pulledFromUser + ")");// 0 -> mailbody, 1-> sub -> sms text

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                String[] arr = null;
                if (pulledFromUser.contains(",")) {
                    arr = pulledFromUser.split(",");
                } else {
                    arr = new String[1];
                    arr[0] = pulledFromUser;
                }

                for (String s : arr) {
                    String adminEmail = s;

                    ArrayList ldapArr = fetchLDAPName(adminEmail);

                    if (!adminEmail.equals("")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " support_pending sendMail user ");

                        //sendMail(adminEmail, emailMessage, emailSubject, from);
                        // above line commented below added by pr on 23rdoct18
                        sendToRabbit(from, adminEmail, emailMessage, emailSubject, "", "", null, null, templateId);//21staug2020

                    } // code taken from above to below by pr on 16thmar18

                    if (!adminEmail.trim().toLowerCase().contains("vpnsupport"))// if added by pr on 21staug2020
                    {
                        //if (ldapArr != null && ldapArr.size() == 3) {
                        if (ldapArr != null && ldapArr.size() >= 3) { // line modified by pr on 8thjan19
                            if (ldapArr.get(1) != null) {
                                userMobile = ldapArr.get(1).toString();

                                if (!userMobile.equals("")) {
                                    System.out.println(" smsMessage " + smsMessage + " userMobile " + userMobile);

                                    //sendSMS(smsMessage, userMobile);
                                    // above line commented below added by pr on 23rdoct18
                                    sendToRabbit("", "", "", "", smsMessage, userMobile, null, null, templateId);

                                }

                            }
                        }
                    }
                }

            }

        }

    }

    // below function added by pr on 16thmar18 for pull back text
    public ArrayList<String> fetchPullMailText(String reg_no, String userType, String pulledFrom)// userType -> u -> user, c -> coordinator, ca-> comp auth, s-> support, m -> mailadmin, textType -> inform/approve
    {

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "", by = "", to = "", title = "", note = "", templateId = "";

        String email = fetchSessionEmail();

        String role = fetchSessionRole();

        if (role.equals(Constants.ROLE_SUP)) {
            by = "Support(" + email + ")";
        }

        if (userType.equals("u")) {
            title = "Applicant";
        } else if (userType.equals("c")) {
            title = "Coordinator";
        } else if (userType.equals("ca")) {
            title = "Reporting/Nodal/Forwarding Officer";
        } else if (userType.equals("s")) {
            title = "Support";
        } else if (userType.equals("d")) {
            title = "DA-Admin";
        }

        text = "Dear " + title + ", <br>An application with Registration number - " + reg_no + " has been Pulled back by the " + by + " from " + pulledFrom + "<br>Regards,<br>eForms Team";

        sub = "Application Registration Number - " + reg_no + " has been pulled back by the " + by;

        sms = "Dear " + title + ", Application No. - " + reg_no + " has been pulled back by the " + by + ". NICSI";
        templateId = "1107160811224464899";
        arr.add(text);

        arr.add(sub);

        arr.add(sms);
        arr.add(templateId);

        return arr;
    }

    // below function added by pr on 7thmar18
    //public ArrayList<String> fetchMailText( String reg_no , String userType, String textType, String forwarded_to_user )// userType -> u -> user, c -> coordinator, ca-> comp auth, s-> support, m -> mailadmin, textType -> inform/approve
    public ArrayList<String> fetchMailText(String reg_no, String userType, String textType, String forwarded_to_user, String statRemarks) // statRemarks added by pr on 6thjun18
    {
        ArrayList<String> arr = new ArrayList<String>();
        String text = "", sub = "", sms = "", by = "", to = "", title = "", note = "", templateId = "";

        String email = fetchSessionEmail();

        String role = fetchSessionRole();

        if (role.equals(Constants.ROLE_CO)) {
            by = "Coordinator(" + email + ")";

            to = "Support(" + sup_email + ")";
        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
            by = "Admin(" + email + ")";

            if (email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("support@dummy.nic.in")) {
                to = "DA-Admin (" + forwarded_to_user + ")";
            } else {
                to = "Admin(" + sup_email + ")";
            }

        }

        if (textType.equals("inform")) {

            if (userType.equals("u")) {
                title = "Applicant";
            } else if (userType.equals("c")) {
                title = "Coordinator";
            } else if (userType.equals("ca")) {
                title = "Reporting/Nodal/Forwarding Officer";
            } else if (userType.equals("s")) {
                title = "Support";
            } else if (userType.equals("m")) {
                title = "Admin";
            }

            text = "Dear " + title + ", <br>An application with Registration number - " + reg_no + " has been Forwarded by the " + by + " to " + to;

            if (!statRemarks.equals("")) // if added by pr on 6thjun18
            {
                text += " with Remarks - " + statRemarks;
            }

            text += " <br>Regards,<br>eForms Team";

            sub = "Application Registration Number - " + reg_no + " Forwarded by the " + by;

            sms = "Dear " + title + ", Application No. - " + reg_no + " has been Forwarded by the " + by + ". NICSI";
            templateId = "1107160811237620375";

        } else if (textType.equals("approve")) {

            String host = "https://eforms.nic.in/";

            /*if(  userType.equals("c")  )
            {
                title = "Coordinator";
                
                host = "https://eforms.nic.in/signup_coordinator";
                    
            }
            else if(  userType.equals("ca")  )
            {
                title = "Reporting/Nodal/Forwarding Officer";
                
                
                
            }*/
            if (userType.equals("s")) {
                title = "Support";

                host = "https://eforms.nic.in/";
            } else if (userType.equals("m")) {
                title = "Admin";

                host = "https://eforms.nic.in/";
            } else if (userType.equals("d")) {
                title = "DA-Admin";

                host = "https://mailadmin.nic.in/da-admin"; // /da-admin added by pr on 2ndjul19

                note = "Note : You have to be on VPN , to be able to access the mentioned link.";
            }

            text = "Dear " + title + ", <br>An application has been forwarded to you for your approval, with Registration number - " + reg_no + " "
                    + ""
                    + "by the " + by;

            if (!statRemarks.equals("")) // if added by pr on 6thjun18
            {
                text += " with Remarks - " + statRemarks;
            }

            text += "<br>Please <a href='" + host + "'>Click Here</a> (<a href='" + host + "'>" + host + "</a>) to take action <br>" + note
                    + "<br><br>Regards,<br>eForms Team";

            sub = "Application Registration Number - " + reg_no + " Forwarded by " + by;

            sms = "Dear " + to + ", Application No. - " + reg_no + " has been Forwarded to you for your approval by the " + by + ".Please Click Here to take action " + host + ". NICSI";
            templateId = "1107160811224464899";

        }

        arr.add(text);

        arr.add(sub);

        arr.add(sms);
        arr.add(templateId);

        return arr;
    }

    // below function added by pr on 13thnov18
    public void sendIntimation(String stat_reg_no, String stat_type, String stat_form_type, String stat_remarks, String stat_forwarded_by, String stat_forwarded_by_user,
            String stat_forwarded_to, String stat_forwarded_to_user) {
        // get all the admins associated with this registration number

        System.out.println(" inside send intimation function stat_reg_no " + stat_reg_no + " stat_type " + stat_type + " stat_form_type " + stat_form_type + " stat_remarks " + stat_remarks + " stat_forwarded_by "
                + stat_forwarded_by + " stat_forwarded_by_user " + stat_forwarded_by_user + " stat_forwarded_to " + stat_forwarded_to + " stat_forwarded_to_user " + stat_forwarded_to_user);

        HashMap<String, String> hm = fetchStatusAdmins(stat_reg_no);

        String by = "", to = "";

        ArrayList userText = null;

        String emailMessage = "", emailSubject = "", smsMessage = "", emailTo = "", smsMobile = "", templateId = "";

        ArrayList<String> userArr = fetchUserComDetail(stat_reg_no, stat_form_type);

        String from = "eforms@nic.in";

        String fwdOrRejectFlag = "f";

        String to_user = "", to_email = "", to_mobile = "";

        ArrayList<String> arr = null;

        ArrayList<String> adminText = null;

        ArrayList<String> commaDetail = null;

        // start, code added by pr on 30thnov18
        // get the forwarded by details and to that email as cc the below mail
        try {

            String[] cc = null;

            /*if( stat_forwarded_by.equals("ca") )
        {                
            cc = new String[1];
            
            ArrayList caArr = fetchCADetail(stat_forwarded_by_user);

            cc[0] = caArr.get(0).toString();
        }
        //else 
        else*/ // code commented by pr on 7thfeb19
            if (!stat_forwarded_by.equals("us")) // modified by pr on 11thjan19
            {
                String fwd_by = hm.get(stat_forwarded_by);

                if (fwd_by.contains(",")) {
                    /*String[] fwd_by_arr = fwd_by.split(",");                
                
                cc = fwd_by_arr;*/

                    cc = fwd_by.split(",");

                } else {
                    cc = new String[1];

                    cc[0] = fwd_by; // line added by pr on 3rddec18

                }

            }

            // end, code added by pr on 30thnov18
            if (stat_forwarded_by.equalsIgnoreCase("ca")) {
                by = "Reporting Officer";

                //stat_forwarded_by_user = fetchCAEmail(stat_forwarded_by_user);  // line commented by pr on 20thfeb19          
            } else if (stat_forwarded_by.equalsIgnoreCase("s")) {
                by = "Support";
            } else if (stat_forwarded_by.equalsIgnoreCase("c")) {
                by = "Coordinator";
            } else if (stat_forwarded_by.equalsIgnoreCase("d")) {
                by = "DA-Admin";
            } else if (stat_forwarded_by.equalsIgnoreCase("m")) {
                by = "Admin";
            } else if (stat_forwarded_by.equalsIgnoreCase("us")) { // else if added by pr on 8thjan19
                by = "Under Secretary( or higher auhtority )";
            }

            if (stat_type.toLowerCase().contains("pending")) {

                if (stat_forwarded_to.equalsIgnoreCase("ca")) {
                    to = "Reporting Officer";

                    //stat_forwarded_to_user = fetchCAEmail(stat_forwarded_to_user); // line commented by pr on 20thfeb19                                            
                } else if (stat_forwarded_to.equalsIgnoreCase("s")) {
                    to = "Support";
                } else if (stat_forwarded_to.equalsIgnoreCase("c")) {
                    to = "Coordinator";
                } else if (stat_forwarded_to.equalsIgnoreCase("d")) {
                    to = "DA-Admin";
                } else if (stat_forwarded_to.equalsIgnoreCase("m")) {
                    to = "Admin";
                } else if (stat_forwarded_to.equalsIgnoreCase("us")) { // else if added by pr on 8thjan19
                    to = "Under Secretary( or higher auhtority )";
                }

                userText = fetchUserMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, stat_forwarded_by_user, stat_forwarded_to_user); // line modified by pr on 5thjun18            

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                if (userArr.get(0) != null) {

                    emailTo = userArr.get(0);
                }

                if (userArr.get(1) != null) {

                    smsMobile = userArr.get(1);
                }
                if (smsMobile == null || smsMobile.isEmpty()) {
                    smsMobile = "";
                }
//                if (!validation.checkFormat("mobile", smsMobile)) {
//                    smsMobile = "";
//                }
                if (smsMobile.startsWith("+")) {
                    smsMobile = smsMobile.substring(1);
                }

                // send mail and sms to the user
                //sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, cc, null, templateId); // line modified by pr on 30thnov18

                // send the approval mail to the forwarded to user 
                ArrayList appText = fetchAppText(stat_reg_no, by, to, stat_forwarded_by_user, stat_forwarded_to_user, stat_remarks, stat_form_type); // stat_form_type parameter added by pr on 15thnov18

                if (appText.get(0) != null) {

                    emailMessage = appText.get(0).toString();
                }

                if (appText.get(1) != null) {

                    emailSubject = appText.get(1).toString();
                }

                if (appText.get(2) != null) {

                    smsMessage = appText.get(2).toString();
                }

                if (appText.get(3) != null) {

                    templateId = appText.get(3).toString();
                }

                if (stat_forwarded_to_user.contains(",")) {

                    String[] emails = stat_forwarded_to_user.split(",");

                    for (String e : emails) {

                        emailTo = e;

                        commaDetail = fetchLDAPName(emailTo);

                        smsMobile = commaDetail.get(1);
                        if (smsMobile == null || smsMobile.isEmpty()) {
                            smsMobile = "";
                        }
//                if (!validation.checkFormat("mobile", smsMobile)) {
//                    smsMobile = "";
//                }
                        if (smsMobile.startsWith("+")) {
                            smsMobile = smsMobile.substring(1);
                        }

                        if (emailTo.equalsIgnoreCase("vpnsupport@nic.in"))// added by pr on 18thaug2020
                        {
                            smsMessage = "";
                            smsMobile = "";
                        }

                        // send mail and sms to the user
                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                    }
                } else {

                    // check if it is a ca_id or email            
                    // if ca_id then fetch the email and mobile from the comp_auth table
                    emailTo = stat_forwarded_to_user;

                    commaDetail = fetchLDAPName(emailTo);

                    smsMobile = commaDetail.get(1);

                    if (stat_forwarded_to.equals("s") && stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) // for sms form the approval mail would go to the sms support email
                    {
                        emailTo = "smssupport@nic.in";

                        smsMessage = "";

                        smsMobile = "";
                    }

                    if (emailTo.equalsIgnoreCase("vpnsupport@nic.in"))// added by pr on 18thaug2020
                    {
                        smsMessage = "";
                        smsMobile = "";
                    }

                    // send mail and sms
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                }

            } else if (stat_type.toLowerCase().contains("rejected")) {

                fwdOrRejectFlag = "r"; // set it as r for reject as by default it is set to f            

                userText = fetchUserMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, stat_forwarded_by_user, stat_forwarded_to_user); // line modified by pr on 5thjun18            

                if (userText.get(0) != null) {
                    emailMessage = userText.get(0).toString();
                }

                if (userText.get(1) != null) {
                    emailSubject = userText.get(1).toString();
                }

                if (userText.get(2) != null) {
                    smsMessage = userText.get(2).toString();
                }

                if (userText.get(3) != null) {
                    templateId = userText.get(3).toString();
                }

                if (userArr.get(0) != null) {

                    emailTo = userArr.get(0);
                }

                if (userArr.get(1) != null) {

                    smsMobile = userArr.get(1);
                }
                if (smsMobile == null || smsMobile.isEmpty()) {
                    smsMobile = "";
                }
//                if (!validation.checkFormat("mobile", smsMobile)) {
//                    smsMobile = "";
//                }
                if (smsMobile.startsWith("+")) {
                    smsMobile = smsMobile.substring(1);
                }

                // send mail and sms to the user
                //sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, cc, null, templateId); // line modified by pr on 30thnov18

            }

        } catch (Exception e) {
            System.out.println(" exception is " + e.getMessage());

            e.printStackTrace();
        }

        // intimation mail to the heirarchy admins in the hashmap
        for (String s : hm.keySet()) {

            System.out.println(" inside for loop for hashmap key is " + s);

            to_user = hm.get(s);

            // below line modified by pr on 30thnov18
            //if ( s.equals("ca") && ( ( stat_type.contains("pending")  &&  !stat_forwarded_to.equalsIgnoreCase("ca") ) || stat_type.contains("rejected") ) ) {
            if (s.equals("ca") && ((stat_type.contains("pending") && !stat_forwarded_to.equalsIgnoreCase("ca")) || stat_type.contains("rejected")) && !stat_forwarded_by.equalsIgnoreCase("ca")) {
                //  to_user variable contains the ca_id value and email, mobile needs to be fetched from comp_auth table

                //arr = fetchCADetail(to_user);
                arr = fetchCADetail(stat_reg_no); // line modified by pr on 20thfeb19

                emailTo = arr.get(0);

                smsMobile = arr.get(1);
                if (smsMobile == null || smsMobile.isEmpty()) {
                    smsMobile = "";
                }
//                if (!validation.checkFormat("mobile", smsMobile)) {
//                    smsMobile = "";
//                }
                if (smsMobile.startsWith("+")) {
                    smsMobile = smsMobile.substring(1);
                }

                adminText = fetchAdminMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, "Reporting/Nodal/Forwarding Officer", stat_forwarded_by_user, stat_forwarded_to_user, stat_form_type);// stat_form_type added by pr on 15thnov18

                if (adminText.get(0) != null) {
                    emailMessage = adminText.get(0).toString();
                }

                if (adminText.get(1) != null) {
                    emailSubject = adminText.get(1).toString();
                }

                if (adminText.get(2) != null) {
                    smsMessage = adminText.get(2).toString();
                }

                if (adminText.get(3) != null) {
                    templateId = adminText.get(3).toString();
                }

                // send mail and sms
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

            } else if (s.equals("s") && ((stat_type.contains("pending") && !stat_forwarded_to.equalsIgnoreCase("s")) || stat_type.contains("rejected")) && !stat_forwarded_by.equalsIgnoreCase("s")) { // line modified by pr on 30thnov18
                // send email to support email 

                emailTo = sup_email;

                adminText = fetchAdminMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, "Support", stat_forwarded_by_user, stat_forwarded_to_user, stat_form_type); // stat_form_type added by pr on 15thnov18

                if (adminText.get(0) != null) {
                    emailMessage = adminText.get(0).toString();
                }

                if (adminText.get(1) != null) {
                    emailSubject = adminText.get(1).toString();
                }

                if (adminText.get(2) != null) {
                    smsMessage = adminText.get(2).toString();
                }

                if (adminText.get(3) != null) {
                    templateId = adminText.get(3).toString();
                }

                smsMessage = "";

                smsMobile = "";

                // send mail and sms
                sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                // send mail 
            } else if (s.equals("c") && ((stat_type.contains("pending") && !stat_forwarded_to.equalsIgnoreCase("c")) || stat_type.contains("rejected")) && !stat_forwarded_by.equalsIgnoreCase("c")) { // line modified by pr on 30thnov18
                // email(s) is in the to_user variable and mobile needs to be fetched from fetchLDAPName function

                // if check if it contains comma separated mails if yes then separate and fetch the mobiles of each
                adminText = fetchAdminMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, "Coordinator", stat_forwarded_by_user, stat_forwarded_to_user, stat_form_type); // stat_form_type added by pr on 15thnov18

                if (adminText.get(0) != null) {
                    emailMessage = adminText.get(0).toString();
                }

                if (adminText.get(1) != null) {
                    emailSubject = adminText.get(1).toString();
                }

                if (adminText.get(2) != null) {
                    smsMessage = adminText.get(2).toString();
                }

                if (adminText.get(3) != null) {
                    templateId = adminText.get(3).toString();
                }

                if (to_user.contains(",")) {

                    String[] emails = to_user.split(",");

                    for (String e : emails) {

                        emailTo = e;

                        commaDetail = fetchLDAPName(emailTo);

                        smsMobile = commaDetail.get(1);
                        
                        if (smsMobile == null || smsMobile.isEmpty()) {
                            smsMobile = "";
                        }
//                if (!validation.checkFormat("mobile", smsMobile)) {
//                    smsMobile = "";
//                }
                        if (smsMobile.startsWith("+")) {
                            smsMobile = smsMobile.substring(1);
                        }
                        // send mail and sms
                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                    }
                } else {

                    emailTo = to_user;

                    commaDetail = fetchLDAPName(emailTo);

                    smsMobile = commaDetail.get(1);

                    // send mail and sms
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                }

            } else if (s.equals("d") && ((stat_type.contains("pending") && !stat_forwarded_to.equalsIgnoreCase("d")) || stat_type.contains("rejected"))) {
                // email(s) is in the to_user variable and mobile needs to be fetched from fetchLDAPName function

                // if check if it contains comma separated mails if yes then separate and fetch the mobiles of each
                adminText = fetchAdminMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, "DA-Admin", stat_forwarded_by_user, stat_forwarded_to_user, stat_form_type); // stat_form_type added by pr on 15thnov18

                if (adminText.get(0) != null) {
                    emailMessage = adminText.get(0).toString();
                }

                if (adminText.get(1) != null) {
                    emailSubject = adminText.get(1).toString();
                }

                if (adminText.get(2) != null) {
                    smsMessage = adminText.get(2).toString();
                }

                if (adminText.get(3) != null) {
                    templateId = adminText.get(3).toString();
                }

                commaDetail = null;

                if (to_user.contains(",")) {

                    String[] emails = to_user.split(",");

                    for (String e : emails) {

                        emailTo = e;

                        commaDetail = fetchLDAPName(emailTo);

                        smsMobile = commaDetail.get(1);
                         if (smsMobile == null || smsMobile.isEmpty()) {
                            smsMobile = "";
                        }
//                        if (!validation.checkFormat("mobile", smsMobile)) {
//                            smsMobile = "";
//                        }
                        if (smsMobile.startsWith("+")) {
                            smsMobile = smsMobile.substring(1);
                        }
                        // send mail and sms
                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                    }
                } else {

                    emailTo = to_user;

                    commaDetail = fetchLDAPName(emailTo);

                    smsMobile = commaDetail.get(1);

                    System.out.println(" for DA-admin emailTo value is " + emailTo + " smsMobile value is " + smsMobile);

                    // send mail and sms
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                }

            } else if (s.equals("m") && ((stat_type.contains("pending") && !stat_forwarded_to.equalsIgnoreCase("m")) || stat_type.contains("rejected")) && !stat_forwarded_by.equalsIgnoreCase("m")) { // line modified by pr on 30thnov18
                // email(s) is in the to_user variable and mobile needs to be fetched from fetchLDAPName function

                // if check if it contains comma separated mails if yes then separate and fetch the mobiles of each
                adminText = fetchAdminMailText(fwdOrRejectFlag, stat_reg_no, by, to, stat_remarks, "Admin", stat_forwarded_by_user, stat_forwarded_to_user, stat_form_type);// stat_form_type added by pr on 15thnov18

                if (adminText.get(0) != null) {
                    emailMessage = adminText.get(0).toString();
                }

                if (adminText.get(1) != null) {
                    emailSubject = adminText.get(1).toString();
                }

                if (adminText.get(2) != null) {
                    smsMessage = adminText.get(2).toString();
                }

                if (adminText.get(3) != null) {
                    templateId = adminText.get(3).toString();
                }

                commaDetail = null;

                if (to_user.contains(",")) {
                    String[] emails = to_user.split(",");

                    for (String e : emails) {
                        emailTo = e;

                        commaDetail = fetchLDAPName(emailTo);

                        smsMobile = commaDetail.get(1);

                        // send mail and sms
                        sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);

                    }
                } else {
                    emailTo = to_user;

                    commaDetail = fetchLDAPName(emailTo);

                    smsMobile = commaDetail.get(1);

                    // send mail and sms
                    sendToRabbit(from, emailTo, emailMessage, emailSubject, smsMessage, smsMobile, null, null, templateId);
                }

            }

        }

    }

    // below function added by pr on 7thmar18
    public HashMap<String, String> fetchStatusAdmins(String reg_no) {

        System.out.println(" inside fetchStatusAdmins func regno is " + reg_no);

        HashMap<String, String> hm = new HashMap<String, String>();

        PreparedStatement ps = null;

        ResultSet res = null;
        //Connection con = null;
        try {
            // con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            //String qry = "SELECT * FROM status WHERE stat_reg_no = ? ORDER BY stat_createdon DESC ";
            String qry = "SELECT stat_type,stat_forwarded_to_user FROM status WHERE stat_reg_no = ? ORDER BY stat_id ASC "; // line modified by pr on 11thoct18, desc to asc done by pr on 1stnov19

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchStatusAdmins query is " + ps);

            res = ps.executeQuery();

            while (res.next()) {
                if (res.getString("stat_type").equals("ca_pending")) {
                    hm.put("ca", res.getString("stat_forwarded_to_user"));
                } else if (res.getString("stat_type").equals("support_pending")) {
                    hm.put("s", res.getString("stat_forwarded_to_user"));
                } else if (res.getString("stat_type").equals("coordinator_pending")) {
                    hm.put("c", res.getString("stat_forwarded_to_user"));
                } else if (res.getString("stat_type").equals("mail-admin_pending")) {
                    hm.put("m", res.getString("stat_forwarded_to_user"));
                } else if (res.getString("stat_type").equals("da_pending")) {
                    hm.put("d", res.getString("stat_forwarded_to_user"));
                } else if (res.getString("stat_type").equals("us_pending")) { // else if added by pr on 7thjan19
                    hm.put("us", res.getString("stat_forwarded_to_user"));
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside get status rows  " + " - " + e.getMessage());

            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(" insie fetchStatusAdmins function size of hashmap is " + hm.size());

        return hm;
    }

    public HashMap fetchwifiDetail(String regNo) {
        String sms_service = "";

        ResultSet rs = null;

        HashMap<String, String> hm = new HashMap<String, String>();

        PreparedStatement ps = null;
        //Connection con = null;
        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);
            //con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry = "select wifi_value,wifi_process FROM wifi_registration WHERE registration_no = ?  ";

            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("wifi_value", rs.getString("wifi_value"));
                hm.put("wifi_process", rs.getString("wifi_process")); // line added by pr on 18thdec18 for wifi
            }

            //// con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 3" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return hm;
    }

    // below function added by pr on 14thmar18
    public ArrayList<String> wifiMailText(String reg_no) {

        HashMap hm = fetchLastUpdatedStatus(reg_no);

        String stat_forwarded_by_user = hm.get("stat_forwarded_by_user").toString();

        String stat_forwarded_to_user = hm.get("stat_forwarded_to_user").toString();

        String by = "Admin (" + stat_forwarded_by_user + ")";

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "", wifi_value = "", templateId = "";

        String wifi_process = ""; // line added by pr on 18thdec18

        HashMap<String, String> wifiHM = fetchwifiDetail(reg_no);

        if (!wifiHM.get("wifi_value").equals("")) {
            wifi_value = wifiHM.get("wifi_value").toString();
        }

        // below if added by pr on 18thdec18
        if (!wifiHM.get("wifi_process").equals("")) {
            wifi_process = wifiHM.get("wifi_process").toString();
        }

        if (wifi_process.equals("request")) // if around and else added by pr on 18thdec18
        {
            if (wifi_value.equals("1"))// LEHAR
            {
                //text = "Dear Sir/Ma'am, <br>Thanks for your e-mail.<br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"LEHAR\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"LEHAR\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                sub = "Reg-" + reg_no + " Regarding Wifi Access for LEHAR"; // above and this line modified by pr on 18thdec18

                sms = "Dear Sir/Madam, your request - " + reg_no + " Regarding Wifi Access for LEHAR has been completed by " + by + ". NICSI"; //line modified by pr on 18thdec18
                templateId = "1107160811300645396";

            } else if (wifi_value.equals("30"))//ANANT // exchanged with vayu by pr on 5thapr18
            {
                //text = "Dear Sir/Ma'am, <br>Thanks for your e-mail.<br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"ANANT\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"ANANT\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                sub = "Reg-" + reg_no + " Regarding Wifi Access for ANANT"; // above and this line modified by pr on 18thdec18

                sms = "Dear Sir/Madam, your request - " + reg_no + " Regarding Wifi Access for ANANT has been completed by " + by + ". NICSI";//line modified by pr on 18thdec18
                templateId = "1107160811314133243";

            } else if (wifi_value.equals("7"))//SARAS
            {
                //text = "Dear Sir/Ma'am, <br>Thanks for your e-mail.<br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"SARAS\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                // 24 hours text added by pr on 10thjan2020
                text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"SARAS\" SSID.<br>For any help you can revert us back on wifi@nic.in.Within 24 working hours you will be able to connect<br><br>Regards,<br>NIC Wi-Fi Team";

                sub = "Reg-" + reg_no + " Regarding Wifi Access for SARAS"; // above and this line modified by pr on 18thdec18

                sms = "Dear Sir/Madam, your request - " + reg_no + " Regarding Wifi Access for SARAS has been completed by " + by + ". NICSI";//line modified by pr on 18thdec18
                templateId = "1107160811323366572";
            } else if (wifi_value.equals("23"))//DEVICE
            {
                //text = "Dear Sir/Ma'am, <br>Thanks for your e-mail.<br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"DEVICE\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"DEVICE\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                sub = "Reg-" + reg_no + " Regarding Wifi Access for DEVICE"; // above and this line modified by pr on 18thdec18

                sms = "Dear Sir/Madam, your request - " + reg_no + " Regarding Wifi Access for DEVICE has been completed by " + by + ". NICSI";//line modified by pr on 18thdec18
                templateId = "1107160811349752513";
            } else if (wifi_value.equals("2"))//VAYU
            {
                //text = "Dear Sir/Ma'am, <br>Thanks for your e-mail.<br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"VAYU\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                //text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"VAYU\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Note: iOS devices need WiFi Certificate to connect.<br><br>Regards,<br>NIC Wi-Fi Team";
                // above line modified by pr on 24thapr19 as per the mail by wifi team
                text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your MACs have been added<br>User ID: email id without @nic.in  Password: mail password<br>Kindly find the enclosed procedure for windows laptops, android mobiles, iOS devices and Windows Lumia phones along with Wireless escalation matrix if you face any issues.<br>You have to login through \"VAYU\" SSID.<br>For any help you can revert us back on wifi@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

                sub = "Reg-" + reg_no + " Regarding Wifi Access for VAYU"; // above and this line modified by pr on 18thdec18

                sms = "Dear Sir/Madam, your request - " + reg_no + " Regarding Wifi Access for VAYU has been completed by " + by + ". NICSI";//line modified by pr on 18thdec18
                templateId = "1107160811362578448";
            }

        } else if (wifi_process.equals("req_delete")) // else if added by pr on 7thmay19
        {
            text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your Mac Address(s) has been deleted successfully.<br><br>Regards,<br>NIC Wi-Fi Team";

            sub = "Reg-" + reg_no + " Regarding Wifi Deletion Request";

            sms = "Dear Sir/Madam, Your Mac Address(s) deleted successfully by " + by + ". NICSI";
            templateId = "1107160811375639875";
        } else {
            text = "Dear Sir/Ma'am, <br><br>In reference to e-Form-" + reg_no + ".<br><br>Your Wi-Fi Certificate has been created and sent on your mentioned email ID.<br>Please acknowledge the mail so that VPN team can provide VPN key.<br><br>For any help you can revert us back on vpnsupport@nic.in.<br><br>Regards,<br>NIC Wi-Fi Team";

            sub = "Reg-" + reg_no + " Regarding Wifi Certificate Request";

            sms = "Dear Sir/Madam, your request Regarding Wifi Certificate has been completed by " + by + ". NICSI";
            templateId = "1107160811405666923";
        }

        arr.add(text);

        arr.add(sub);

        arr.add(sms);
        arr.add(templateId);

        return arr;
    }

    public Boolean sendQueryIntimation(String qr_forwarded_to, String qr_forwarded_to_user, String by, String msg, String mobile, String qr_reg_no) {
        Boolean done = false;

        String title = "", templateId = "";

        if (qr_forwarded_to.equals("ca")) {
            title = "Reporting/Nodal/Forwarding Officer";

        } else if (qr_forwarded_to.equals("s")) {
            title = "Support";
        } else if (qr_forwarded_to.equals("c")) {
            title = "Coordinator";
        } else if (qr_forwarded_to.equals("m")) {
            title = "Admin";

        } else if (qr_forwarded_to.equals("d")) {
            title = "DA-Admin";

        } else if (qr_forwarded_to.equals("u")) {
            title = "Applicant";

        }

        int len = msg.length();

        String msgSub = msg;

        if (len > 15) {
            msgSub = msg.substring(0, 15);

            msgSub += "...";

        }

        // note added  by pr on 15thjun18, text updated by pr on 24thapr2020
        String text = "Dear " + title + ", <br>A Query has been Raised/Responded to You by " + by + " on Reg No " + qr_reg_no + ".<br><br> Query is : '<strong>" + msg
                + "'</strong><br><br><strong>Note</strong>: Please don't reply to this mail here, <a href='https://eforms.nic.in/'>Click Here</a> (<a href='https://eforms.nic.in/'>https://eforms.nic.in/</a>) and go to the Query Raise Module in eForms Portal to respond to the Query.  <br><br>Regards,<br>eForms Team";

        String sub = "Query Raised/Responded on Reg No " + qr_reg_no;
         //12/2/2022 invalid template  
        String sms = "Dear   " + title + ", A Query(" + msgSub + ") has been Raised/Responded to You by  " + by + "  on Reg No " + qr_reg_no + " NICSI";
        //String sms = "Dear  " + title + ", A Query(" + msgSub + ") has been Raised/Responded to You by " + by + " on Reg No " + qr_reg_no + " NICSI";
        templateId = "1107160811419632897";
        String from = "eforms@nic.in";

        if (!qr_forwarded_to_user.equals("")) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " before send mail function in inform file to is " + qr_forwarded_to_user + " email botdy is " + text + " subject is " + sub + " from is " + from);

            //sendMail(qr_forwarded_to_user, text, sub, from);
            // above line commented below added by pr on 23rdoct18
            //sendToRabbit(from, qr_forwarded_to_user, text, sub, "", "", null, null);
            sendToRabbit(from, qr_forwarded_to_user, text, sub, sms, mobile, null, null, templateId);// line modified by pr on 21staug2020
        }

//        if (!mobile.equals("")) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " before send sms in inform file sms text is " + sms + " mobile number is " + mobile);
//
//            //sendSMS(sms, mobile);
//            // above line commented below added by pr on 23rdoct18
//            sendToRabbit("", "", "", "", sms, mobile, null, null);
//
//        }
        return done;

    }

    // below function added by pr on 7thdec18
    public void sendWifiPDF(String[] attach, String auth_email) {
        // send pdf to applicant

        String text = "";

        String sub = "";

        String from = "";

        String emailTo = "";

        // send pdf to vpn team
        text = "Dear VPN Team, <br>Please find the WIFI Certificate form attached with this Mail.<br>Kindly create the WIFI Certificate.<br><br>Regards,<br>eForms Team";

        sub = "WIFI Certificate Form";

        from = "eforms@nic.in";

        emailTo = vpn_sup_email;

        String[] cc = new String[]{wifi_sup_email};

        sendToRabbit(from, emailTo, text, sub, "", "", cc, attach, null); // send email to vpn team and cc the wifi team                                                      

    }

    // below function added by pr on 6thjan19 for under secretary
    public void sendUSIntimation(String regNo, String formType, String under_sec_name, String under_sec_email, String under_sec_mobile, String roEmail, String statRemarks, String sha) {

        String title = "", templateId = "";

        HashMap<String, String> hm = fetchApplicantRODetail(formType, regNo);

        String name = "", email = "", mobile = "", desig = "", min = "";

        String ro_name = "", ro_email = "", ro_mobile = "", ro_desig = "";

        if (hm != null) {
            if (hm.get("name") != null && !hm.get("name").equals("")) {
                name = hm.get("name");
            }

            if (hm.get("email") != null && !hm.get("email").equals("")) {
                email = hm.get("email");
            }

            if (hm.get("mobile") != null && !hm.get("mobile").equals("")) {
                mobile = hm.get("mobile");
            }
            
            if (mobile == null || mobile.isEmpty()) {
                mobile = "";
            }
//            Validation validation = new Validation();
//            if (!validation.checkFormat("mobile", mobile)) {
//                mobile = "";
//            }
            if (mobile.startsWith("+")) {
                mobile = mobile.substring(1);
            }

            if (hm.get("desig") != null && !hm.get("desig").equals("")) {
                desig = hm.get("desig");
            }

            if (hm.get("ministry") != null && !hm.get("ministry").equals("")) {
                min = hm.get("ministry");
            }

            if (hm.get("ro_name") != null && !hm.get("ro_name").equals("")) {
                ro_name = hm.get("ro_name");
            }

            if (hm.get("ro_email") != null && !hm.get("ro_email").equals("")) {
                ro_email = hm.get("ro_email");
            }

            if (hm.get("ro_mobile") != null && !hm.get("ro_mobile").equals("")) {
                ro_mobile = hm.get("ro_mobile");
            }

            if (hm.get("ro_desig") != null && !hm.get("ro_desig").equals("")) {
                ro_desig = hm.get("ro_desig");
            }
        }

        // above code modified by pr on 9thjan19
        String text = "<!doctype html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\">\n"
                + "<title>eForms</title>\n"
                + "</head>\n"
                + "	<style>\n"
                + "		p{\n"
                + "			font-family: 'Roboto', sans-serif;\n"
                + "			font-size: 15px;\n"
                + "			color: #676869;\n"
                + "		}\n"
                + "		h1\n"
                + "		{\n"
                + "			font-family: 'Roboto', sans-serif;\n"
                + "			font-size: 18px;\n"
                + "			color: #3B3B3B;\n"
                + "			text-align: left;\n"
                + "			line-height: 15px;\n"
                + "			text-transform: uppercase;\n"
                + "			font-weight: 800;\n"
                + "			color: #1e2022;\n"
                + "		}\n"
                + "\n"
                + "		.text\n"
                + "		{\n"
                + "			text-align:left;\n"
                + "			font-family: 'Roboto', sans-serif;\n"
                + "			font-size: 15px;\n"
                + "			font-weight: 500;\n"
                + "			padding-top: 5px;\n"
                + "			padding-bottom: 5px;\n"
                + "			color: #676869;\n"
                + "		}\n"
                + "	\n"
                + "		\n"
                + "	</style>\n"
                + "\n"
                + "<body>\n"
                + "	\n"
                + "	<table width=\"650\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: 2px solid #8A8A8A; border-radius: 10px;\">\n"
                + "  <tbody><tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "	   <tr>\n"
                + "      <td colspan=\"4\" align=\"center\"><img src=\"https://eforms.nic.in/assets/img/eforms.png\" alt=\"eforms-logo\"></td>\n"
                + "    </tr>\n"
                + "	   <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td width=\"30\">&nbsp;</td>\n"
                + "	  <td colspan=\"2\"><p>Dear Sir/Madam, <br><br>\n"
                + "A request has been forwarded to You by the Reporting Officer(" + roEmail + ") for your approval through eForms with Reg No <strong>" + regNo + "</strong><br><br>\n"
                + "\n";

        if (!statRemarks.equals("")) {
            text += "With Remarks : " + statRemarks + "<br><br>\n";
        }

        text += "\n"
                + "You are requested to do the due diligence and verify the credentials of user and his Reporting Officer before approving the request.The PDF with Form Details has been attached with this mail and the Applicant and Reporting Officers Details are shown below:</p></td>\n"
                + "      <td width=\"20\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "	  <td colspan=\"2\"><h1>Applicant Details</h1></td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\" width=\"100\">Name</td>\n"
                + "      <td class=\"text\">" + name + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Email</td>\n"
                + "      <td class=\"text\">" + email + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Mobile</td>\n"
                + "      <td class=\"text\">" + mobile + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Designation</td>\n"
                + "      <td class=\"text\">" + desig + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	  <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Organization</td>\n"
                + "      <td class=\"text\">" + min + "\n"
                + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	  \n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "	  <td colspan=\"2\"><h1>Reporting Officer's Details</h1></td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "    <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">RO Name</td>\n"
                + "      <td class=\"text\">" + ro_name + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Ro Email</td>\n"
                + "      <td class=\"text\">" + ro_email + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	  <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Ro Mobile</td>\n"
                + "      <td class=\"text\">" + ro_mobile + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td>&nbsp;</td>\n"
                + "      <td class=\"text\">Designation</td>\n"
                + "      <td class=\"text\">" + ro_desig + "</td>\n"
                + "      <td>&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	   <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "	<tr>\n"
                + "	   <td width=\"30\">&nbsp;</td>\n";

        // url modified by pr on 22ndmar19 , it needs to be reverted back to live url       
//        text += "	<td align=\"center\" width=\"300\"><a title='Click to Approve' href=\"https://eforms.nic.in/us_process?aid=" + sha + "\"><img src=\"https://eforms.nic.in/assets/images/green.png\" width=\"150\"></a></td>\n"
//                + "		<td align=\"center\" width=\"300\"><a title='Click to Reject' href=\"https://eforms.nic.in/us_process?rid=" + sha + "\"><img src=\"https://eforms.nic.in/assets/images/red.png\" width=\"150\"></a></td>\n";
        // above code line modified by pr on 29thapr19
        text += "<td align=\"center\" width=\"300\"><a title='Click to Approve' href=\"https://eforms.nic.in/us_mid_process?regNo=" + regNo + "\"><img src=\"https://eforms.nic.in/assets/images/green.png\" width=\"150\"></a></td>\n"
                + "		<td align=\"center\" width=\"300\"><a title='Click to Reject' href=\"https://eforms.nic.in/us_mid_process?regNo=" + regNo + "\"><img src=\"https://eforms.nic.in/assets/images/red.png\" width=\"150\"></a></td>\n";

        text += "	<td width=\"20\">&nbsp;</td>\n"
                + "		\n"
                + "    </tr>\n";

        text += "<tr>\n<td colspan=\"4\">Note: This link will remain active for 7 days. Hence, you are requested to take necessary action before expiry of the link.</td>\n</tr>"; // line added by pr on 9thapr19

        text += "      <tr>\n"
                + "      <td colspan=\"4\">&nbsp;</td>\n"
                + "    </tr>\n"
                + "	  \n"
                + "<tr><td width=\"30\">&nbsp;</td><td colspan=\"3\"><p>Regards, <br>eForms</p></td></tr>"
                + "  \n\n\n\n"
                + "  </tbody>\n"
                + "</table>\n"
                + "\n"
                + "</body>\n"
                + "</html>";

        String sub = "Application Forwarded by Reporting Officer " + roEmail + " through eForms for approval";

        //String sms = "Dear Sir/Madam, An application has been Forwarded by Reporting Officer " + roEmail + " through eForms for your approval.Please check your emailn for details.";
        // line modified by pr on 9thapr19
        String sms = "Sir/Madam, kindly take action on Reg No - " + regNo + ".<br>validity of the link : 7 days, Please check your email for details.<br>Rgds, eForms Team NICSI";
        templateId = "1107160811431804513";
        String from = "eforms@nic.in";

        if (!under_sec_email.equals("")) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " == " + " before send mail function in inform file to is " + under_sec_email + " email botdy is " + text + " subject is " + sub + " from is " + from);

            // since this mail will go only in case of manual process
            // this file must be available
            String[] attach = null;

            // get the ca_rename_sign_cert value from the base table and 
            String filePath = fetchSignedDoc(regNo, formType); // line modified by pr on 11thjan19

            //String  filePath = "/eForms/PDF/"+regNo+".pdf";  // line modified by pr on 20thnov18 for sending in mail
            //String  filePath = "F://eForms/PDF/"+regNo+".pdf";  // line modified by pr on 20thnov18 for sending in mail , done for testing
            if (!filePath.equals("")) {
                File fil = new File(filePath);

                if (fil.exists()) {
                    attach = new String[]{filePath};
                }
            }

            sendToRabbit(from, under_sec_email, text, sub, sms, under_sec_mobile, null, attach, templateId);// modified by pr on 21staug2020

        }

//        if (!under_sec_mobile.equals("")) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                    + " == " + " before send sms in inform file sms text is " + sms + " mobile number is " + under_sec_mobile);
//
//            sendToRabbit("", "", "", "", sms, under_sec_mobile, null, null);
//
//        }
        // send email and sms to the RO as well that the mail and sms has been sent to the under secretary and it can be approved by us within 2 days only
        //text = "Dear Reporting Officer, <br>A request has been forwarded by You to the Under Secretary ( or higher auhtority ) " + under_sec_email + " (" + under_sec_mobile + ") through eForms with Reg No " + regNo;
        // above line modified by pr on 9thapr19
        text = "Dear Sir/Madam, <br>A request " + regNo + " has been sent to \"Under Secretary or equivalent\" having " + under_sec_email + " (" + under_sec_mobile + ").";

        if (!statRemarks.equals("")) // if added by pr on 5thjun18
        {
            text += " with Remarks : <strong>" + statRemarks + "</strong>";
        }

        text += "<br><br>";

        text += "<h3>Applicant Details</h3><table border='0'>"
                + "<tr><td>Name </td><td>" + name + "</td></tr>"
                + "<tr><td>Email </td><td>" + email + "</td></tr>"
                + "<tr><td>Mobile </td><td>" + mobile + "</td></tr>"
                + "<tr><td>Designation </td><td>" + desig + "</td></tr>"
                + "<tr><td>Organization </td><td>" + min + "</td></tr>"
                + "</table>";

        text += "<br><br>";

        //text += "<br><br><strong>The link sent to the under secretary ( or higher auhtority ) will be valid only for a limited time.  <br><br>Regards,<br>eForms Team";
        text += "<br><br><strong>A link has been sent to mentioned email address which will remain active for 7 days. After that, link will get expired and applicant will have to resubmit the request. Hence, you are requested to coordinate with " + under_sec_email + " and get this request approved for early creation of ID.</strong><br><br>Regards,<br>eForms Team"; // line modified by pr on 9thapr19

        sub = "Link sent to the under secretary or equivalent through eForms for Reg No - " + regNo;

        //sms = "Dear Reporting Officer, Link has been sent to the under secretary ( or higher auhtority ) through eForms for Reg No - " + regNo + ". The link will be valid only for a limited Duration.";
        // above line modified by pr on 9thapr19
        sms = "Sir/Madam, Link sent to under secretary ( or equivalent ) for Registration No - " + regNo + ".<br>validity of the link : 7 days<br>Rgds, eForms Team. NICSI";
        templateId = "1107160811624972689";
        from = "eforms@nic.in";

        if (!ro_email.equals("")) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " == " + " before send mail function in inform file to is " + ro_email + " email botdy is " + text + " subject is " + sub + " from is " + from);

            sendToRabbit(from, ro_email, text, sub, sms, ro_mobile, null, null, templateId);// line modified by pr on 21staug2020
        }

//        if (!ro_mobile.equals("")) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                    + " == " + " before send sms in inform file sms text is " + sms + " mobile number is " + ro_mobile);
//
//            sendToRabbit("", "", "", "", sms, ro_mobile, null, null);
//
//        }
        // start, code added by pr on 9thapr19 
        // send email and sms to the applicant as well 
        text = "Dear Sir/Madam, <br>A request " + regNo + " has been sent to \"Under Secretary or equivalent\" having details : " + under_sec_email + " (" + under_sec_mobile + ") by the Reporting Officer(" + ro_email + ").";

        if (!statRemarks.equals("")) {
            text += " with Remarks : <strong>" + statRemarks + "</strong>";
        }

        text += "<br><br>";

        //text += "<br><br><strong>The link sent to the under secretary ( or higher auhtority ) will be valid only for a limited time.  <br><br>Regards,<br>eForms Team";
        text += "<br><br><strong>A link has been sent to mentioned email address which will remain active for 7 days. After that, link will get expired and applicant will have to resubmit the request. Hence, you are requested to coordinate with " + under_sec_email + " and get this request approved for early creation of ID.</strong><br><br>Regards,<br>eForms Team"; // line modified by pr on 9thapr19

        sub = "Link sent to the under secretary or equivalent through eForms for Reg No - " + regNo;

        //sms = "Dear Reporting Officer, Link has been sent to the under secretary ( or higher auhtority ) through eForms for Reg No - " + regNo + ". The link will be valid only for a limited Duration.";
        // above line modified by pr on 9thapr19
        sms = "Sir/Madam, Link sent to under secretary ( or equivalent ) for Registration No - " + regNo + ".<br>validity of the link : 7 days<br>Rgds, eForms Team. NICSI";
        templateId = "1107160811624972689";
        from = "eforms@nic.in";

        if (!email.equals("")) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " == " + " Applicant before send mail function in inform file to is " + ro_email + " email botdy is " + text + " subject is " + sub + " from is " + from);

            sendToRabbit(from, email, text, sub, sms, mobile, null, null, templateId);// line modified by pr on 21staug2020
        }

//        if (!mobile.equals("")) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                    + " == " + " Applicant before send sms in inform file sms text is " + sms + " mobile number is " + ro_mobile);
//
//            sendToRabbit("", "", "", "", sms, mobile, null, null);
//
//        }
        // end, code added by pr on 9thapr19 
    }

    // below function added by pr on 11thjan19
    public String fetchSignedDoc(String reg_no, String stat_form_type) {
        String ca_rename_sign_cert = "";
        //Connection con = null;
        try {

            //con = entities.MyConnection.getConnection();
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getSignedDoc func " + e.getMessage());
        }
        String qry = "", tblName = "";

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " getSignedDoc value of tblName is " + tblName + " stat_form_type " + stat_form_type);

        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
            tblName = "sms_registration";

        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = "single_registration";

        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
            tblName = "bulk_registration";

        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
            tblName = "ip_registration";

        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {

            tblName = "nkn_registration";

        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {

            tblName = "relay_registration";

        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {

            tblName = "ldap_registration";

        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {

            tblName = "distribution_registration";

        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {

            tblName = "imappop_registration";

        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {

            tblName = "gem_registration";

        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {

            tblName = "mobile_registration";

        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {

            tblName = "dns_registration";

        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {

            tblName = "wifi_registration";

        } else if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD)) {

            tblName = "vpn_registration";

        } else if (stat_form_type.equals(Constants.WEBCAST_FORM_KEYWORD)) {

            tblName = "webcast_registration";

        } else if (stat_form_type.equals(Constants.FIREWALL_FORM_KEYWORD)) {

            tblName = "centralutm_registration";

        } else if (stat_form_type.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {

            tblName = "email_act_registration";

        } else if (stat_form_type.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {

            tblName = "email_deact_registration";

        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {

            tblName = Constants.DAONBOARDING_TABLE_NAME;

        }

        qry = "SELECT  ca_rename_sign_cert  FROM " + tblName + " WHERE registration_no = ?";

        PreparedStatement ps = null;

        ResultSet res = null;

        try {
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getUserComDetail func query is " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                ca_rename_sign_cert = res.getString("ca_rename_sign_cert");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection: " + stat_form_type + " - " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ca_rename_sign_cert;
    }

    // below function added by pr on 18thmar19, type is imap or pop
//    public void sendZimMail(String email, String type) 
//    {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " 
//                
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendZimMail function: ");
//        
//        // send pdf to applicant
//
//        String text = "";
//
//        String sub = "";
//
//        String from = "";
//
//        String emailTo = "";
//
//        // send pdf to vpn team
//        text = "Hi, <br>The list of users has been updated inSUNldap but not in ZimbraLDAP.<br>EmailAddress:zimbraImapEnabled:zimbraPop3Enabled<br>";
//
//        if( !email.equals("") && !type.equals("") )
//        {
//            String imapEnable = "FALSE", popEnable = "FALSE";
//            
//            if( type.equalsIgnoreCase("imap") )
//            {
//                imapEnable = "TRUE";
//            }
//            else if( type.equalsIgnoreCase("pop") )
//            {
//                popEnable = "TRUE";
//            }
//            
//            text += email+":"+imapEnable+":"+popEnable+"<br>";
//        }
//        
//        text += "<br>Regards,<br>eForms Team";
//        
//        sub = "[Mailadmin-zim] User Mail Allow Service Report | Due to Error in SUN-eForms Application on IMAP/POP";
//
//        from = "eforms@nic.in";
//        
//        ServletContext ctx = getServletContext(); 
//        
//        String zim_email = ctx.getInitParameter("zim_email");
//        
//        emailTo = zim_email;
//        
//        //String[] cc = {"preeti.nhq@nic.in", "nikki.nhq@nic.in", "dhirendra.nhq@nic.in", "ravinder.nhq@nic.in"};         
//        
//        String[] cc = {"rahejapreeti@gmail.com", "rahejapreeti@hotmail.com"};         
//
//        sendToRabbit(from, emailTo, text, sub, "", "", cc, null); // send email to vpn team and cc the wifi team                                                      
//
//    }
    // below function added by pr on 12thapr19
    public void sendZimCreateMail(String po, String bo, String data) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendZimMail function: ");

        // send pdf to applicant
        String text = "";

        String sub = "";

        String from = "";

        String emailTo = "";

        // send pdf to vpn team
        text = "Hi, <br>The list of users created inSUNldap are attached with this mail. The SYNTAX of the users mentioned in file is:<br>";

        text += "<br>PO:BO:uid:first name:last name:description:mobile:date of birth:date of retirement:designation:department:state:employee code:mail:mail equivalentaddress<br>";

        text += "<br>" + data + "<br>";

        text += "<br>PO: " + po + "<br>";

        text += "<br>BO: " + bo + "<br>";

        text += "<br>NOTE:<br>";

        text += "<br>1. Format: Mobile: [+]country code followed by mobile number (+919898989898)<br>";

        text += "<br>2. Format: Date of Birth: DD-MM-YYYY (if date of birth is present) OR 'NA' (if date of birth is not present)<br>";

        text += "<br>3. Format: Format:Date of Retirement: DD-MM-YYYY<br>";

        text += "<br>4. Format: Employee code: Can contain digits or alphabets or both with length 2-12 e.g: nic12345 OR 'NA' (if employee code is not present)<br>";

        text += "<br>5. Format: Mail equivalntaddress -> NA if no mail equivalent address present<br>";

        text += "<br>All fields are mandatory except Date of Birth, Employee code and mail equivalent address<br>";

        text += "<br>Regards,<br>eForms Team";

        sub = "User Creation Report | Due to Error in eForms Application on creation";

        from = "eforms@nic.in";

        ServletContext ctx = getServletContext();

        String zim_email = ctx.getInitParameter("zim_email");

        emailTo = zim_email;

        String[] cc = {""};// modified on 19thaug2020

        sendToRabbit(from, emailTo, text, sub, "", "", cc, null, null); // send email to vpn team and cc the wifi team                                                      

    }

    // below function added by pr on 12thapr19
    public void sendZimBulkMail(String po, String bo, String attach) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendZimMail function: ");

        // send pdf to applicant
        String text = "";

        String sub = "";

        String from = "";

        String emailTo = "";

        // send pdf to vpn team
        text = "Hi, <br>The list of users created inSUNldap are attached with this mail. The SYNTAX of the users mentioned in file is:<br>";

        text += "<br>PO:BO:uid:first name:last name:description:mobile:date of birth:date of retirement:designation:department:state:employee code:mail:mail equivalentaddress<br>";

        text += "<br>PO: " + po + "<br>";

        text += "<br>BO: " + bo + "<br>";

        text += "<br>NOTE:<br>";

        text += "<br>1. Format: Mobile: [+]country code followed by mobile number (+919898989898)<br>";

        text += "<br>2. Format: Date of Birth: DD-MM-YYYY (if date of birth is present) OR 'NA' (if date of birth is not present)<br>";

        text += "<br>3. Format: Format:Date of Retirement: DD-MM-YYYY<br>";

        text += "<br>4. Format: Employee code: Can contain digits or alphabets or both with length 2-12 e.g: nic12345 OR 'NA' (if employee code is not present)<br>";

        text += "<br>5. Format: Mail equivalntaddress -> NA if no mail equivalent address present<br>";

        text += "<br>All fields are mandatory except Date of Birth, Employee code and mail equivalent address<br>";

        text += "<br>Regards,<br>eForms Team";

        sub = "User Creation Report | Due to Error in eForms Application on creation";

        from = "eforms@nic.in";

        ServletContext ctx = getServletContext();

        String zim_email = ctx.getInitParameter("zim_email");

        emailTo = zim_email;

        String[] cc = {""}; // modified on 19thaug2020

        //String[] cc = {"preeti.nhq@nic.in"};         
        String[] attachArr = {attach};

        File file = new File(attach);

        if (!file.exists()) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendZimBulkMail function: file doesnt exists ");

            attachArr = null;
        }

        sendToRabbit(from, emailTo, text, sub, "", "", cc, attachArr, null); // attachment contains the text file containing the emails(and info) not updated in zimbra ldap
    }

    // below function added by pr on 18thmar19, type is imap or pop
//    public void sendZimMail(String email, String type) 
//    {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " 
//                
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendZimMail function: ");
//        
//        // send pdf to applicant
//
//        String text = "";
//
//        String sub = "";
//
//        String from = "";
//
//        String emailTo = "";
//
//        // send pdf to vpn team
//        text = "Hi, <br>The list of users has been updated inSUNldap but not in ZimbraLDAP.<br>EmailAddress:zimbraImapEnabled:zimbraPop3Enabled<br>";
//
//        if( !email.equals("") && !type.equals("") )
//        {
//            String imapEnable = "FALSE", popEnable = "FALSE";
//            
//            if( type.equalsIgnoreCase("imap") )
//            {
//                imapEnable = "TRUE";
//            }
//            else if( type.equalsIgnoreCase("pop") )
//            {
//                popEnable = "TRUE";
//            }
//            
//            text += email+":"+imapEnable+":"+popEnable+"<br>";
//        }
//        
//        text += "<br>Regards,<br>eForms Team";
//        
//        sub = "[Mailadmin-zim] User Mail Allow Service Report | Due to Error in SUN-eForms Application on IMAP/POP";
//
//        from = "eforms@nic.in";
//        
//        ServletContext ctx = getServletContext(); 
//        
//        String zim_email = ctx.getInitParameter("zim_email");
//        
//        emailTo = zim_email;
//        
//        String[] cc = {"nikki.nhq@nic.in", "dhirendra.nhq@nic.in", "ravinder.nhq@nic.in", "preeti.nhq@nic.in"};         
//        
//        //String[] cc = {"rahejapreeti@gmail.com", "rahejapreeti@hotmail.com"};         
//
//        sendToRabbit(from, emailTo, text, sub, "", "", cc, null); // send email to vpn team and cc the wifi team                                                      
//
//    }     
    // code modified by pr on 12thapr19
    public void sendZimMail(String email, String formName, String value) // value would contain imap or pop in case of imappop, mobile value in case of mobile form, wifi value in case of wifi form
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendZimMail function: ");

        // send pdf to applicant
        String text = "";

        String sub = "";

        String from = "";

        String emailTo = "";

        String formSuffix = "";

        boolean flag = false;

        // send pdf to vpn team
        text = "Hi, <br>The list of users has been updated inSUNldap but not in ZimbraLDAP.";

        if (formName.equals(Constants.IMAP_FORM_KEYWORD)) {
            flag = true;
            text += "<br>EmailAddress:zimbraImapEnabled:zimbraPop3Enabled<br>";
            flag = true;
            if (!email.equals("") && !value.equals("")) {
                String imapEnable = "FALSE", popEnable = "FALSE";

                if (value.equalsIgnoreCase("imap")) {
                    imapEnable = "TRUE";
                } else if (value.equalsIgnoreCase("pop")) {
                    popEnable = "TRUE";
                }

                text += email + ":" + imapEnable + ":" + popEnable + "<br>";
            }

            formSuffix = "IMAP/POP";

        } else if (formName.equals(Constants.MOB_FORM_KEYWORD)) {
            flag = true;
            text += "<br>EmailAddress:Mobile<br>";
            flag = true;

            if (!email.equals("") && !value.equals("")) {
                text += email + ":" + value + "<br>";
            }

            formSuffix = "MOBILE UPDATE";

        } else if (formName.equals(Constants.WIFI_FORM_KEYWORD)) {
            flag = true;
            text += "<br>EmailAddress:WifiValue<br>";
            flag = true;

            if (!email.equals("") && !value.equals("")) {
                text += email + ":" + value + "<br>";
            }

            formSuffix = "WIFI";

        }

        text += "<br>Regards,<br>eForms Team";

        sub = "[Mailadmin-zim] User Mail Allow Service Report | Due to Error in SUN-eForms Application on " + formSuffix;

        from = "eforms@nic.in";

        ServletContext ctx = getServletContext();

        String zim_email = ctx.getInitParameter("zim_email");

        emailTo = zim_email;

              
        String[] cc = {"uma.nhq@nic.in"};// modified on 19thaug2020
      
        if (flag) {
            sendToRabbit(from, emailTo, text, sub, "", "", cc, null, null); // send email to vpn team and cc the wifi team                                                      
        }
    }

    // below function added by pr on 29thapr19
    public void sendUSOTP(String mobile, String otp) {
        try {
            if (rabbitObj == null) {

                //System.out.println(" inside sendtorabbit function rabbitobj null ");
                rabbitObj = new NotifyThrouhRabbitMQ();
            } else {

                //System.out.println(" inside sendtorabbit function rabbitobj NOT null ");
            }

            HashMap<String, Object> map_sms = new HashMap<>();

            String msg = "Your eForms OTP for Approval/Rejection of Request is :" + otp + ".It will be valid for 30 minutes. NICSI";
            map_sms.put("smsbody", msg);
            if (mobile == null || mobile.isEmpty()) {
                mobile = "";
            }
//            Validation validation = new Validation();
//            if (!validation.checkFormat("mobile", mobile)) {
//                mobile = "";
//            }
            if (mobile.startsWith("+")) {
                mobile = mobile.substring(1);
            }
           
            map_sms.put("mobile", mobile);
            map_sms.put("templateId", "1107160811979255773");

            NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();

            rabbitObj.sendSmsOtpRabbitMq(map_sms);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendUSOTP function: " + e.getMessage());

        }

    }

    // below function added by pr on 30thjul2020
    public HashMap<String, String> fetchApplicantDetail(String regNo) {
        HashMap<String, String> hm = new HashMap<String, String>();

        PreparedStatement ps = null;

        ResultSet res = null;

        try {

            String qry = "SELECT applicant_email, applicant_mobile, applicant_name "
                    + "FROM final_audit_track WHERE registration_no = ?";

            conSlave = DbConnection.getSlaveConnection();

            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchApplicantDetail func query is " + ps);

            res = ps.executeQuery();

            while (res.next()) {
                // set email and mobile in the Forms object

                // applicant details
                hm.put("name", res.getString("applicant_name"));

                hm.put("email", res.getString("applicant_email"));

                hm.put("mobile", res.getString("applicant_mobile"));

            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchApplicantDetail DB query: " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return hm;
    }

    // below function added by pr on 28thaug19, to send notification to all the heirarchy when a request is put on hold or off hold
    public void sendHoldNotification(String reg_no, String role, String by_email, String msg, String remarks) {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendHoldNotification function : " + " reg_no is " + reg_no + " role is " + role + " msg is " + msg);

        // get all the heirarchy email and mobile and applicant email and mobile from final_audit_track table
        String by = "", title = "", text = "", sub = "", sms = "", from = "eforms@nic.in", templateId = ""; // line added by pr on 2ndmay19;

        // start, code added by pr on 30thjul2020
        HashMap<String, String> hmApp = fetchApplicantDetail(reg_no);

        String nameApp = "", emailApp = "", mobileApp = "";

        if (hmApp != null) {
            if (hmApp.get("name") != null && !hmApp.get("name").equals("")) {
                nameApp = hmApp.get("name");
            }

            if (hmApp.get("email") != null && !hmApp.get("email").equals("")) {
                emailApp = hmApp.get("email");
            }

            if (hmApp.get("mobile") != null && !hmApp.get("mobile").equals("")) {
                mobileApp = hmApp.get("mobile");
            }
        }

        // end, code added by pr on 30thjul2020
        if (role.equalsIgnoreCase(Constants.ROLE_CA)) {
            by = "Reporting Officer";
        } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN)) {
            by = "Admin";
        } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
            by = "Coordinator";
        } else if (role.equalsIgnoreCase(Constants.ROLE_SUP)) {
            by = "Support";
        }

        HashMap<String, String> hm = fetchFinalAdmin(reg_no);

        if (hm != null && hm.size() > 0) {
            String val = "", to = "", mobile = "";

            for (String key : hm.keySet()) {
                if (!key.equalsIgnoreCase(by)) // if the file uploaded by entity is not same as key then only 
                {

                    val = hm.get(key);

                    String[] emMobArr = null;

                    if (val.contains("~")) {
                        emMobArr = val.split("~");
                    }

                    if (emMobArr != null && emMobArr.length > 0) {
                        to = emMobArr[0];

                        if (emMobArr.length == 2) {
                            mobile = emMobArr[1];;
                        }
                    }

                    text = "Dear " + key + ", <br>An Application with Registration number - "
                            + reg_no + msg;

                    text += " by " + by + "( " + by_email + " ).";

                    if (!remarks.equals("")) {
                        text += " With Remarks - '" + remarks + "'";
                    }

                    // start, code added by pr on 30thjul2020
                    text += "<br><br>The Applicant Details are shown below:<br><br>";

                    text += "<table border='0'>"
                            + "<tr><td>Name </td><td>" + nameApp + "</td></tr>"
                            + "<tr><td>Email </td><td>" + emailApp + "</td></tr>"
                            + "<tr><td>Mobile </td><td>" + mobileApp + "</td></tr>"
                            + "</table>";

                    text += "<br><br>";

                    // end, code added by pr on 30thjul2020                   
                    text += "<br><br>Regards,<br>eForms Team ";

                    sub = "An Application with Registration number - " + reg_no + msg;

                    //sms = "An Application with Registration number - " + reg_no + msg + " by " + by + "( " + by_email + " ).";
                    // line modified by pr on 30thjul2020
                    sms = "An Application with Registration number - " + reg_no + "( " + nameApp + " )" + msg + " by " + by + "( " + by_email + " ). NICSI";
                    templateId = "1107160811636892120";

                    if (!to.equals("")) {
                        // send mail

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside sendHoldNotification function : "
                                + " from is " + from + " to is " + to + " text is " + text + " sub is " + sub + " sms is " + sms + " mobile is " + mobile);

                        sendToRabbit(from, to, text, sub, sms, mobile, null, null, templateId);

                    }

                }

            }

        }

    }

    // below function added by pr on 2ndmay19, to send notification to all the heirarchy when a file is uploaded in multiple uploads
    public void sendUploadNotification(String reg_no, String panel, String by_email) {
        // get all the heirarchy email and mobile and applicant email and mobile from final_audit_track table

        String by = "", title = "", text = "", sub = "", sms = "", from = "eforms@nic.in", templateId = ""; // line added by pr on 2ndmay19;

        /*if (sessionMap != null && sessionMap.get("uservalues") != null) {
            UserData userdata = (UserData) sessionMap.get("uservalues");

            by_email = userdata.getEmail();
        }*/
        if (panel.equalsIgnoreCase("user")) {
            by = "Applicant";
        } else if (panel.equalsIgnoreCase("ca")) {
            by = "Reporting Officer";
        } else if (panel.equalsIgnoreCase("admin")) {
            by = "Admin";
        } else if (panel.equalsIgnoreCase("co")) {
            by = "Coordinator";
        } else if (panel.equalsIgnoreCase("sup")) {
            by = "Support";
        }

        HashMap<String, String> hm = fetchFinalAdmin(reg_no);

        if (hm != null && hm.size() > 0) {
            String val = "", to = "", mobile = "";

            for (String key : hm.keySet()) {
                if (!key.equalsIgnoreCase(by)) // if the file uploaded by entity is not same as key then only 
                {

                    val = hm.get(key);

                    String[] emMobArr = null;

                    if (val.contains("~")) {
                        emMobArr = val.split("~");
                    }

                    if (emMobArr != null && emMobArr.length > 0) {
                        to = emMobArr[0];

                        if (emMobArr.length == 2) {
                            mobile = emMobArr[1];;
                        }
                        //Validation validation = new Validation();
                        if (mobile == null || mobile.isEmpty()) {
                            mobile = "";
                        }
//                        if (!validation.checkFormat("mobile", mobile)) {
//                            mobile = "";
//                        }
                        if (mobile.startsWith("+")) {
                            mobile = mobile.substring(1);
                        }
                    }

                    text = "Dear " + key + ", <br>File has been uploaded in eForms for Registration number - "
                            + reg_no + " by " + by + "( " + by_email + " ). Please click on `Download Multiple Docs` under Action tab in your respective Console, to download/check the File.<br><br>Regards,<br>eForms Team ";

                    sub = "File uploaded for Registration Number - " + reg_no;

                    sms = "File has been uploaded in eforms for Registration number - " + reg_no + " by " + by + "( " + by_email + " ). NICSI";
                    templateId = "1107160811647350746";
                    if (!to.equals("")) {
                        // send mail

                        sendToRabbit(from, to, text, sub, sms, mobile, null, null, templateId);

                    }

                }

            }

        }

    }

    // below function added by pr on 2ndmay19
    public HashMap<String, String> fetchFinalAdmin(String reg_no) {
        HashMap<String, String> hm = new HashMap<String, String>();

        PreparedStatement ps = null;

        ResultSet res = null;

        String applicant_email = "", applicant_mobile = "", ca_email = "", ca_mobile = "", coordinator_email = "", coordinator_mobile = "", support_email = "",
                admin_mobile = "", admin_email = "", to_email = "", status = "", to_mobile = "";

        try {

            conSlave = DbConnection.getSlaveConnection();

            String qry = "SELECT * FROM final_audit_track WHERE registration_no = ? ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getFinalAdmin func query is " + ps);

            res = ps.executeQuery();

            while (res.next()) {
                if (res.getString("applicant_email") != null) {
                    applicant_email = res.getString("applicant_email");
                }

                if (res.getString("applicant_mobile") != null) {
                    applicant_mobile = res.getString("applicant_mobile");
                }

                if (res.getString("ca_email") != null) {
                    ca_email = res.getString("ca_email");
                }

                if (res.getString("ca_mobile") != null) {
                    ca_mobile = res.getString("ca_mobile");
                }

                if (res.getString("coordinator_email") != null) {
                    coordinator_email = res.getString("coordinator_email");
                }

                if (res.getString("coordinator_mobile") != null) {
                    coordinator_mobile = res.getString("coordinator_mobile");
                }

                if (res.getString("support_email") != null) {
                    support_email = res.getString("support_email");
                }

                if (res.getString("admin_mobile") != null) {
                    admin_mobile = res.getString("admin_mobile");
                }

                if (res.getString("admin_email") != null) {
                    admin_email = res.getString("admin_email");
                }

                if (res.getString("to_email") != null) {
                    to_email = res.getString("to_email");
                }

                if (res.getString("status") != null) {
                    status = res.getString("status");
                }

                if (status.equalsIgnoreCase("ca_pending") || status.equalsIgnoreCase("support_pending") || status.equalsIgnoreCase("coordinator_pending") || status.equalsIgnoreCase("mail-admin_pending")) {
                    if (res.getString("to_email") != null) {
                        to_email = res.getString("to_email");

                        if (res.getString("to_mobile") != null) {
                            to_mobile = res.getString("to_mobile");
                        }

                    }

                    String key = "";

                    if (status.equalsIgnoreCase("ca_pending")) {
                        key = "Reporting Officer";
                    } else if (status.equalsIgnoreCase("support_pending")) {
                        key = "Support";
                    } else if (status.equalsIgnoreCase("coordinator_pending")) {
                        key = "Coordinator";
                    } else if (status.equalsIgnoreCase("mail-admin_pending")) {
                        key = "Admin";
                    }

                    hm.put(key, to_email + "~" + to_mobile);
                }

                // put into the hashmap
                if (!applicant_email.equals("")) {
                    hm.put("Applicant", applicant_email + "~" + applicant_mobile);
                }

                if (!ca_email.equals("")) {
                    hm.put("Reporting Officer", ca_email + "~" + ca_mobile);
                }

                if (!support_email.equals("")) {
                    hm.put("Support", support_email + "~");
                }

                if (!coordinator_email.equals("")) {
                    hm.put("Coordinator", coordinator_email + "~" + coordinator_mobile);
                }

                if (!admin_email.equals("")) {
                    hm.put("Admin", admin_email + "~" + admin_mobile);
                }

            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getFinalAdmin function : " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return hm;
    }

    public ArrayList<String> fetchCompMailText(String userType, String reg_no, String form_type, String uids,
            String password, String email, String mobile, String adminType, boolean isApplicant) {

        HashMap hm = fetchLastUpdatedStatus(reg_no);

        String stat_forwarded_by_user = hm.get("stat_forwarded_by_user").toString();

        String stat_forwarded_to_user = hm.get("stat_forwarded_to_user").toString();

        String stat_remarks = hm.get("stat_remarks").toString(); // line added by pr on 26thfeb18

        String by = "";
        String to = "";
        String templateId = "";

        if (stat_forwarded_by_user != null && !stat_forwarded_by_user.equals("")) {

            by = by + "(" + stat_forwarded_by_user + ")";
        }

        if (stat_forwarded_to_user != null && !stat_forwarded_to_user.equals("")) {

            to = to + "(" + stat_forwarded_to_user + ")";
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getCompMailText userType is " + userType);

        ArrayList<String> arr = new ArrayList<String>();

        String text = "", sub = "", sms = "" ,text1 ="", sms1="";

        if (userType.equals("user")) {
            if (form_type.equals(Constants.SMS_FORM_KEYWORD)) {
                text = "<p>Dear Sir/Madam,</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>As desired the&nbsp;NIC&nbsp;SMS&nbsp;Gateway user account has been created and below mentioned are the login credentials.<br />\n"
                        + "User name: " + uids + "<br />\n"
                        + "Password: will be sent to your mobile number mentioned in the form.(" + mobile + ")</p>\n"
                        + "\n"
                        + "<p><br />\n"
                        + "Kindly integrate the below mentioned url with your application and "
                        + " find below documents for your reference : <br><br> "
                        + ""
                        + ""
                        + "1. <a href='https://msgapp.emailgov.in/OnlineForms/NicGuidelines.jsp'>NIC&nbsp;SMS&nbsp;GATEWAY&nbsp;SERVICE&nbsp;Integration document</a>,<br />"
                        + "2. <a href='https://msgapp.emailgov.in/OnlineForms/Documents.jsp'>TRAI exempted Sender ID template for your reference.</a>&nbsp;&nbsp;</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p><strong>a) Integration URL&nbsp;</strong><br />\n"
                        + "<a href=\"https://smsgw.sms.gov.in/failsafe/HttpLink?username=xxxxxx&amp;pin=xxxxxxx&amp;message=message&amp;mnumber=91XXXXXXXXXX&amp;signature=SENDERID\" target=\"_blank\">https://smsgw.sms.gov.in/failsafe/HttpLink?username=xxxxxx&amp;pin=xxxxxxx&amp;message=message&amp;mnumber=91XXXXXXXXXX&amp;signature=SENDERID</a>&nbsp;</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>Please encode the special character from your pin with below details.<br />\n"
                        + "<br />\n"
                        + "$ - %24<br />\n"
                        + "# - %23<br />\n"
                        + "&amp; - %26<br />\n"
                        + "% - %25<br />\n"
                        + "<br />\n"
                        + "<strong>b) IP whitelisting</strong>&nbsp;</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>kinldy whitelist NIC SMS Gateway IP &amp; DOMAIN at your firewall end.<br />\n"
                        + "<br />\n"
                        + "DOMAIN:smsgw.sms.gov.in<br />\n"
                        + "Push IP:164.100.14.211 ,164.100.112.139<br />\n"
                        + "port: 443</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<ul>\n"
                        + "	<li>Whether you are able to telnet smsgw.sms.gov.in 443 or telnet smsgw.sms.gov.in 80.&nbsp;&nbsp;</li>\n"
                        + "	<li>If it is not happening, And, if you are behind NICNET firewall, please coordinate with Cyber Security Division. Rule must be allowed at firewall to avail the service.</li>\n"
                        + "	<li>If your firewall rule is allowed and still you are not able to telnet, please disable the local firewall at your server else coordinate with Network administrator.</li>\n"
                        + "</ul>\n"
                        + "\n"
                        + "<p><strong>c) TRAI Exempted Sender ID</strong></p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>Please find the TRAI ADDRESS &amp; CONTACT details :</p>\n"
                        + "\n"
                        + "<p>&nbsp;</p>\n"
                        + "\n"
                        + "<p>Advisor, QoS<br />\n"
                        + "Phone: 23230404, FAX: 23213036 Email:&nbsp;advqos@trai.gov.in<br />\n"
                        + "Mahanagar Doorsanchar Bhawan (next to Zakir Hussain College)<br />\n"
                        + "Jawaharlal Nehru Marg (Old Minto Road) New Delhi: 110 002<br />\n"
                        + "Phone no :&nbsp;<a href=\"callto:91-11-2323 6308\" target=\"_blank\">91-11-2323 6308</a>&nbsp;(Reception)&nbsp;<a href=\"callto:2323 3466,2322\" target=\"_blank\">2323 3466,2322</a><br />\n"
                        + "FAX No. :&nbsp;<a href=\"callto:91-11-2321 3294\" target=\"_blank\">91-11-2321 3294</a>(Reception)<br />\n"
                        + "Website: trai.gov.in</p>\n"
                        + "\n"
                        + "<p><br />\n"
                        + "Please download the TRAI Exempted template from the link mentioned above.Send the Sender ID details to the TRAI as per the template and get approval from TRAI.<br />\n"
                        + "Once TRAI approves, please send the scanned copy of the approval to&nbsp;smssupport@gov.in.<br />\n"
                        + "Once the Sender ID is approved by the TRAI, the same will be configured on&nbsp;SMS&nbsp;gateway. Then you can use the same for sending&nbsp;SMS.<br />\n"
                        + "<br />\n"
                        + "&nbsp;</p>\n"
                        + "\n"
                        + "<p>Thanks and Regards,</p>\n"
                        + "\n"
                        + "<p>SMS-Support<br />\n"
                        + "Website:&nbsp;<a href=\"https://servicedesk.nic.in/\" target=\"_blank\">https://servicedesk.nic.in</a><br />\n"
                        + "NIC&nbsp;Service&nbsp;Desk (Toll-Free):-&nbsp;<a href=\"callto:1800111555\" target=\"_blank\">1800111555</a></p>";

                sub = "Application Registration Number - " + reg_no + " processing Completed.";

                sms = "Dear Applicant, Reg. No.: " + reg_no + " on eForms has been Approved  by " + by + " and ID has been Created.Auth ID is " + uids + " Password is " + password + " NICSI";
                templateId = "1107160811738984079";
            } else if (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || form_type.equals(Constants.GEM_FORM_KEYWORD)) // line modified by pr on 18thdec17
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getCompMailText func in single nkn gem block ");

                if (isApplicant) {
                    text = "Dear Applicant, <br><br>Your application on eForms with Registration number - " + reg_no + " has been approved and ID has been Created by " + to + ".<br>New Email ID is "
                            + uids + "<br>Password has been sent to user's mobile number ("
                            + mobile + ").";

                    sms = "Registration No. : " + reg_no + ", status : Completed, ID : " + uids + ", Password : sent_to_user , Login @ : https://email.gov.in/ NICSI";
                    //add by prabhat  
                    text1 = "Dear Sir/Madam, <br><br>An ID creation request was submitted in eForms " + email + " with Registration number - " + reg_no + " has been approved and ID has been Created by " + to + ".<br>New Email ID is "
                            + uids + "<br>Password has been sent to your registered Mobile "
                            + mobile + ".";
                    sms1 = "Registration No. : " + reg_no + ", status : Completed, ID : " + uids + ", Password : " + password + " , Login @ : https://email.gov.in/ NICSI";
                } else {
                    text = "Dear Sir/Madam, <br><br>An ID creation request was submitted in eForms for you by " + email + " with Registration number - " + reg_no + " has been approved and ID has been Created by " + to + ".<br>New Email ID is "
                            + uids + "<br>Password has been sent to your registered Mobile "
                            + mobile + ".";
                    sms = "Registration No. : " + reg_no + ", status : Completed, ID : " + uids + ", Password : " + password + " , Login @ : https://email.gov.in/ NICSI";
                }

                if (!stat_remarks.equals("")) // if added by pr on 3rdoct19
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team"; // line added by pr on 3rdoct19

                sub = "Application Registration Number - " + reg_no + " processing Completed ";
                
                /*sms = "Dear Applicant, Application No. - " + reg_no + " on eForms has been Approved by " + by + " and ID has been Created.New Email ID is "
                        + uids + ", Password is " + password; // text changed from user to Applicant by pr on 22ndoct18*/
                // above line modified by pr on 18thapr19
                templateId = "1107160810967654447";
            } else {
                text = "Dear Applicant, <br>Your application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by;

                if (!stat_remarks.equals("")) // if added by pr on 26thfeb18
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team";

                sub = "Application Registration Number - " + reg_no + " processing Completed";

                sms = "Dear Applicant, Your application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by + ". NICSI";
                templateId = "1107160811764409403";

            }

        } else if (userType.equals("admin")) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " getCompMailText func userType admin form_type value is " + form_type);

            if (form_type.equals(Constants.SMS_FORM_KEYWORD)) {
                text = "Dear " + adminType + ", <br><br>An application with Registration number - "
                        + reg_no + " has been completed by " + by + ". and Auth ID has been created.<br>Auth ID is : "
                        + uids + "<br>Password has been sent to the Applicant's registered mobile "
                        + mobile + ".<br><br>Regards,<br>eForms Team";

                sub = "Application Registration Number - " + reg_no + " processing Completed";
                //12/1/2022 for incorrect template
                sms = "Dear  " + adminType + " , Reg. No.:  " + reg_no + "  has been completed and ID has been created is  " + uids + " NICSI";
                //sms = "Dear " + adminType + ", Reg. No.: " + reg_no + " has been completed and ID has been created is " + uids + " NICSI";
                templateId = "1107160811058959715";

            } else if (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || form_type.equals(Constants.GEM_FORM_KEYWORD)) // line modified by pr on 30thjan18
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside userType admin gem block  ");

                text = "Dear " + adminType + ", <br><br>An application on eForms with Registration number - " + reg_no + " "
                        + ""
                        + "has been completed by " + by + " and email has been created.<br>New Email ID is " + uids + "";

                if (!stat_remarks.equals("")) // if added by pr on 3rdoct19
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team";// if added by pr on 3rdoct19

                sub = "Application Registration Number - " + reg_no + " processing Completed";

                sms = "Dear " + adminType + ", Reg. No.: " + reg_no + " has been completed by " + by + "." + " New Email ID has been created.Email is " + uids + " NICSI";
                templateId = "1107160811815380008";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " text is  " + text + " sms is " + sms);
            } else {
                text = "Dear " + adminType + ", <br>An application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by;

                if (!stat_remarks.equals("")) // if added by pr on 26thfeb18
                {
                    text += " with Remarks - " + stat_remarks;
                }

                text += "<br><br>Regards,<br>eForms Team";

                sub = "Application Registration Number - " + reg_no + " processing Completed ";
                
                //12/1/2022 for incorrect template
                sms = "Dear " + adminType + ", An application with Registration number - "
                        + reg_no + " on eForms has been completed by " + by + ". NICSI";
//                templateId = "1107160811058959715";
                templateId = "1107160811141946915";
            }

        }

        arr.add(text);

        arr.add(sub);

        arr.add(sms);

        arr.add(templateId);
        if(text1!=null && !text1.isEmpty()){ //added by prabhat-rahul
        arr.add(text1);   
        arr.add(sms1);
        }
        return arr;
    }
    
    private void sendToRabbitEmailBackup(String from, String emailTo, String emailMessage, String emailSubject, String[] atFileArr, Object object) {
        HashMap<String, Object> emailDetails = new HashMap<String, Object>();
        ArrayList<String> attachList = null;
        try {
            if (rabbitObj == null) {
                rabbitObj = new NotifyThrouhRabbitMQ();
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " inside sendToRabbitEmailBackup function ");
            if (from != null && !from.equals("")) {
                emailDetails.put("from", from);
            }
            if (emailTo != null && !emailTo.equals("")) {
                emailDetails.put("to", emailTo);
            }
            if (emailMessage != null && !emailMessage.equals("")) {
                emailDetails.put("mailbody", emailMessage);
            }
            if (emailSubject != null && !emailSubject.equals("")) {
                emailDetails.put("subject", emailSubject);
            }
            if (atFileArr != null && atFileArr.length > 0) {
                attachList = new ArrayList<String>(Arrays.asList(atFileArr));
                emailDetails.put("attachedFile", attachList);
            }
            rabbitObj.notifyEmailBackup(emailDetails);
            System.out.println("inside  sendToRabbitEmailBackup After calling notifyEmailBackup..!! ");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
