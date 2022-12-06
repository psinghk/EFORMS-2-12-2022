package com.org.controller;

import admin.AdminAction;
import admin.ForwardAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ActionContext;
import java.io.Serializable;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.nic.eoffice.esign.model.EsignRequest;
import org.nic.eoffice.esign.model.EsignRequestResponse;
import com.opensymphony.xwork2.ActionSupport;
import com.org.connections.DbConnection;
import com.org.dao.EsignUtilDaoForUpdateMobile;
import com.org.dao.Ldap;
import com.org.service.esignService;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FormBean;
import org.apache.catalina.core.ApplicationContext;
import org.apache.struts2.ServletActionContext;
import org.nic.eoffice.esign.EsignResponse;
import rabbitmq.NotifyThrouhRabbitMQ;
import utility.Inform;
import static zimbra_soap_v2.Zimbra_SOAP_v2.getAdminAuthToken;

public class EsignVerifyForUpdateMobile extends ActionSupport implements SessionAware, Serializable {
private static final String niccert = ServletActionContext.getServletContext().getInitParameter("niccert");
    private static final long serialVersionUID = 1L;
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    private Map<String, Object> session;
    private String xmlContent;
    private String serviceURL;
    private String respon;
    private String gatewayURL;
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");

    FormBean form_details;
    ForwardAction fwd = new ForwardAction();
    private final EsignUtilDaoForUpdateMobile esignUtilDao;
    Map<String, Object> consentreturn = new HashMap<>();
    esignService esignService = null;
    ForwardAction fwdObj;
    AdminAction adminAction;
    Ldap ldap;

    public EsignVerifyForUpdateMobile() {
        this.esignUtilDao = new EsignUtilDaoForUpdateMobile();
        esignService = new esignService();
        fwdObj = new ForwardAction(); // line added by pr on 2ndaug19
        adminAction = new AdminAction();
        ldap = new Ldap();
    }

    public String getGatewayURL() {
        return gatewayURL;
    }

    public void setGatewayURL(String gatewayURL) {
        this.gatewayURL = gatewayURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    private String userName;

    public String execute() {
        String ref_num = (String) session.get("ref_num");
        String formType = session.get("form_type").toString();
        String name = session.get("form_type").toString();
        String update_without_oldmobile = session.get("update_without_oldmobile").toString();
        String check = session.get("check").toString();
        String json = null;
        String eMail = "";
        String role = "";
        String outputFilename = "";
        String inputFileName = ref_num + ".pdf";

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inside eSign Response......: ref_num" + ref_num + "formType" + formType + "check" + check + "inputFileName" + inputFileName);
        if (session.get("admin_role") != null) {
            role = session.get("admin_role").toString();
            outputFilename = "eSigned_" + role + "_" + ref_num + ".pdf";
        } else {
            role = "user";
            outputFilename = "eSigned_" + "user_" + ref_num + ".pdf";
        }
        String pageNo = "";
        String cordinates = "";
        String uid = "";
//        setServiceURL("https://nic-esign2gateway.nic.in/esign/acceptClient");
//        setGatewayURL("https://nic-esign2gateway.nic.in/esign/acceptClient");
        setServiceURL("https://nic-esign2gateway.nic.in/eSign21/acceptClient");
        setGatewayURL("https://nic-esign2gateway.nic.in/eSign21/acceptClient");
        setUserName(name);
        java.util.Date date = new java.util.Date();
        DateFormat dt = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String pdate = dt.format(date);
        int txnId = esignUtilDao.getTransactionId() + 1;
        String str = "";
        if (txnId > -1) {
            str = String.format("%06d", txnId);
            if (str.length() > 6) {
                str = str.substring(1);
            }
        }
        String txn = "032-EFORMS-" + pdate + "-" + str;
        EsignRequest req = esignUtilDao.getEsignFileRequest(inputFileName, outputFilename, pageNo, cordinates, uid, name, txn);
        System.out.println(req);
        EsignRequestResponse esignRequestResponse = esignUtilDao.getRequestXML(req);
        try {
            json = new ObjectMapper().writeValueAsString(session);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(EsignVerifyForUpdateMobile.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "insert into esigntransaction (txn, inputfilename, outputfilename, pageno, cordinates, uid, email, ref_num,role, form_type, session_values,mobile_flag,check_flag) values(?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";
        String registrationNo = (String) session.get("ref_num");
        try {
            Connection con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, txn);
            ps.setString(2, inputFileName);
            ps.setString(3, outputFilename);
            ps.setString(4, pageNo);
            ps.setString(5, cordinates);
            ps.setString(6, uid);
            ps.setString(7, eMail);
            ps.setString(8, registrationNo);
            ps.setString(9, role);
            ps.setString(10, formType);
            ps.setString(11, json);
            ps.setString(12, update_without_oldmobile);
            ps.setString(13, check);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inside execute......: PS" + ps);
            ps.execute();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EsignVerifyForUpdateMobile.class.getName()).log(Level.ALL.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EsignVerifyForUpdateMobile.class.getName()).log(Level.SEVERE, null, ex);
        }

        setXmlContent(esignRequestResponse.getRequestXml());
        System.out.println(esignRequestResponse.getRequestXml());
        return SUCCESS;
    }

    public String ESignResponsePageForUpdateMobile() throws IOException, Exception {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inside eSign Response......: ");
        Inform inform = new Inform();
        String ref_num = "";
        String formType = "";
        String txn = "";
        boolean ishog = false;
        boolean ishod = false;

        if (getRespon() != null) {
            txn = getRespon().substring(getRespon().indexOf("txn"), getRespon().indexOf("><"));
        }
        String[] str = txn.split("=");
        txn = str[1].substring(1, str[1].length() - 1);
        String sql = "select * from esigntransaction where txn = '" + txn + "'";

        Connection con = DbConnection.getConnection();
        Connection conSlave = DbConnection.getSlaveConnection(); //29dec2021
//        Statement st = con.createStatement();
        Statement st = conSlave.createStatement(); //29dec2021
        ResultSet rs = st.executeQuery(sql);
        System.out.println("ST :: " + st);
        EsignRequest req = null;
        String txnEmail = null;
        String registrationNo = null;
        String inputFileName = null, outputFileName = null, pageNo = null, cordinates = null, uid = null, role = null, json = null, mobile_flag = null, check = null, new_mobile = null, new_code = null, user_email = null, initials = null, fname = null, mname = null, lname = null;
        if (rs.next()) {
            txnEmail = rs.getString("email");
            registrationNo = rs.getString("ref_num");
            inputFileName = rs.getString("inputfilename");
            outputFileName = rs.getString("outputfilename");
            pageNo = rs.getString("pageno");
            cordinates = rs.getString("cordinates");
            uid = rs.getString("uid");
            role = rs.getString("role");
            formType = rs.getString("form_type");
            json = rs.getString("session_values");
            mobile_flag = rs.getString("mobile_flag");
            check = rs.getString("check_flag");

            sql = "delete from esigntransaction where txn = '" + txn + "'";
            st = con.createStatement();
            st.executeUpdate(sql);
        }
        if (txnEmail == null) {
            return "logout";
        }
        String formDetailsJson = null;
        Map<String, Object> response = null;
        FormBean formBean = null;
        try {
            String formBeanDetails = "";
            formBeanDetails = formType + "_details";
            response = new ObjectMapper().readValue(json, HashMap.class);
            formDetailsJson = new ObjectMapper().writeValueAsString(response.get(formBeanDetails));
            formBean = new ObjectMapper().readValue(formDetailsJson, FormBean.class);
        } catch (IOException ex) {
            System.out.println("Error.....");
            Logger.getLogger(EsignVerifyForUpdateMobile.class.getName()).log(Level.SEVERE, null, ex);
        }
        response.remove(formType + "_details");
        for (Map.Entry<String, Object> entry : response.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            session.put(key, value);
        }

        System.out.println("SESSION VALUES ::: " + session);
        session.put(formType + "_details", formBean);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Form Bean......: " + formBean);
        req = esignUtilDao.getEsignFileRequest(inputFileName, outputFileName, pageNo, cordinates, uid, formBean.getFname(), txn);
        System.out.println(req);
        EsignRequestResponse esignRequestResponse = esignUtilDao.getRequestXML(req);
        EsignResponse resp = new EsignResponse();
        String esignRes = "";
        ref_num = registrationNo;
        //Map profile_values = (HashMap) session.get("profile-values");
        String responseXml = getRespon();
        EsignRequestResponse esignreqsp1 = resp.getEsignResponse(esignRequestResponse.getContentType(), esignRequestResponse.getAppearance(), responseXml); // This method will sign pdf
        System.out.println("Esign Response Status " + esignreqsp1.getRespStatus());
        System.out.println("Error Message if failed " + esignreqsp1.getErrorMessage());
        System.out.println("Error Code ::" + esignreqsp1.getErrorCode());
        System.out.println("formType ::" + formType);
        System.out.println("===============================================================================================================");
        session.put("form_text", "Update Your Mobile Number With Esign");

        if (esignreqsp1.getRespStatus() == 0) {
            session.put("moduleEsign", "esignFail");
            esignRes = "success";

        } else {
            String dn = fwdObj.fetchLDAPDN(formBean.getUser_email(), formType);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + dn);
            
            String dateOfRet=formBean.getNicDateOfRetirement();
            String nicDateOfRetirement = utcFormat(dateOfRet);
            
            String dateOfBirth=formBean.getNicDateOfBirth();
            String nicDateOfBirth = utcFormat(dateOfBirth);
            
            System.out.println("Attributed added nicDateOfBirth --");
            
            if (!dn.equals("")) {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + "91" + formBean.getNew_mobile());
                adminAction.modifyLDAPAttribute(dn, "title", formBean.getInitials());
                adminAction.modifyLDAPAttribute(dn, "givenName", formBean.getFname());
                adminAction.modifyLDAPAttribute(dn, "nicmiddlename", formBean.getMname());
                adminAction.modifyLDAPAttribute(dn, "cn", formBean.getFname() + " " + formBean.getMname() + " " + formBean.getLname());
                adminAction.modifyLDAPAttribute(dn, "sn", formBean.getLname());
                adminAction.modifyLDAPAttribute(dn, "displayName", formBean.getFname() + " " + formBean.getLname());
                adminAction.modifyLDAPAttribute(dn, "mobile", "+91" + formBean.getNew_mobile());
                
                adminAction.modifyLDAPAttribute(dn, "nicDateOfBirth", nicDateOfBirth);
                adminAction.modifyLDAPAttribute(dn, "nicDateOfRetirement", nicDateOfRetirement);
                adminAction.modifyLDAPAttribute(dn, "designation", formBean.getDesignation());
                
                // start, code added by pr on 12thapr9
                HashMap<String, String> zim_values = new HashMap<String, String>();
                zim_values.put("mobile", "+91" + formBean.getNew_mobile());
                zim_values.put("title", formBean.getInitials());
                zim_values.put("givenName", formBean.getFname());
                zim_values.put("nicmiddlename", formBean.getMname());
                zim_values.put("sn", formBean.getLname());
                zim_values.put("cn", formBean.getFname() + " " + formBean.getLname());
                zim_values.put("displayName", formBean.getFname() + " " + formBean.getLname());
                
                zim_values.put("legacyDateOfBirth", nicDateOfBirth);
                zim_values.put("legacyDateOfRetirement", nicDateOfRetirement);
                
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + "ZIM Values" + zim_values);
                //zim_values.put("form_name", Constants.MOB_FORM_KEYWORD);
                URL url = null;
                String admin_token = "";
                
                
                
                url = new URL("https://100.80.17.250:7071/service/admin/soap/");
                //admin_token = getAdminAuthToken("createuserapp@createaccount.da", "fgaT8.8A_bGm", url);
                //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "A@#poi$^KJ39", url);
                //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "#ZyPE@8xR#H$TJxT9^H=CTZ@#43f#Dg4dV#Xz@", url);
                admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "Q=2E+D4Kf=BYi+K7De=4TjA+xFq6Z=T7+hDs=4x", url);
                
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + "Admin Token" + admin_token);
                adminAction.modifyZimLDAPAttribute(admin_token, url, formBean.getUser_email(), zim_values, "updatemobile");// above line commented this line modified by pr on 12thapr19
                // end, code added by pr on 12thapr9
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "=========esign succes " + session);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inside else update mobile......: " + formType);
                esignRes = "success1";
                PreparedStatement ps = null;
                String update = "update otp_save_update_mobile set action='successfully updated',esign_transaction_id=?,filename=?,esign_filepath=? where new_mobile =? and email = ? and newcode=? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW()";
                ps = con.prepareStatement(update);
                ps.setString(1, txn);
                ps.setString(2, inputFileName);
                ps.setString(3, ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + outputFileName);
                ps.setString(4, formBean.getNew_mobile());
                ps.setString(5, formBean.getUser_email());
                ps.setString(6, formBean.getNewcode());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "update query========= " + ps);
                int i = ps.executeUpdate();

                String mobile = ldap.fetchMobileFromLdap(formBean.getUser_email());

                String from = "eforms@nic.in";
                String emailTo = formBean.getUser_email();
                String sub = "eForms : Mobile number successfully updated";
                StringBuilder sb = new StringBuilder();
                sb.append("<p>Dear user,</p>");
                sb.append("<p>Mobile number against your NIC email address has been updated. Your current mobile number can no longer be used to access NIC email address <b>" + formBean.getUser_email() + "</b>. </p>");
                sb.append("If you have not done the updation -- Please call on 1800 or drop a mail to servicedesk@nic.in NICSI");
                inform.sendToRabbit(from, emailTo, sb.toString(), sub, "", "", null, null, null);

                sb = new StringBuilder();
                sb.append("<p>Dear Team,</p>");
                sb.append("Whenever user comes to update mobile portal and initiates the update after authentication through LDAP, we inform the user regarding over mail and SMS and we ask him/her to report the incident to NIC Cert if this was not initiated by him/her. </br>");
                sb.append("<p>It is the part of the process to secure user's accounts. In case, user reports to you, you may use below information to support the user or to take necessary action :-</p>");
                sb.append("<h3>Applicant Details</h3><table border='0'>"
                        + "<tr><td>Email</td><td>" + formBean.getUser_email() + "</td></tr>"
                        + "<tr><td>IP from which this activity was done</td><td>" + ip + "</td></tr>"
                        + "<tr><td>Browser</td><td>" + userAgent + "</td></tr>"
                        + "<tr><td>Timestamp</td><td>" + "  " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "</td></tr>"
                        + "</table>");

                sb.append("<p>Regards,</p>");
                sb.append("eForms Team NIC");
                sb.append("<p>Regards,</p>");
                sb.append("eForms Team NIC");
                String from1 = "eforms@nic.in";
               // String emailTo1 = "satya.nhq@nic.in";
               String emailTo1=niccert;
                String sub1 = "eForms : Mobile number successfully updated";
                inform.sendToRabbit(from1, emailTo1, sb.toString(), sub1, "", "", null, null, null);

                HashMap<String, Object> map_sms = new HashMap<>();
                map_sms.put("smsbody", "Mobile number against your NIC email address has been updated. "
                        + "Your current mobile number can no longer be used to access NIC email address " + formBean.getUser_email()
                        + ". If you have not done the updation -- Please call on 1800 or drop a mail to servicedesk@nic.in NICSI");
                map_sms.put("mobile", mobile);
                map_sms.put("templateId", "1107162237774418371");
                NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
                object.sendSmsOtpRabbitMq(map_sms);
                //session.clear();

            }

        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + "esignRes" + esignRes);
        session.put("ref_num", ref_num);
        session.put("form_type", formType);
        session.put("mobile_flag", mobile_flag);
        session.put("check_flag", check);
        session.put("esignRes", esignRes);
        session.put("empName", ldap.fetchNameFromLdap(formBean.getUser_email()));
        session.put("empMobile", ldap.fetchMobileFromLdap(formBean.getUser_email()));
        
        ServletActionContext.getRequest().getSession().setAttribute("empName", ldap.fetchNameFromLdap(formBean.getUser_name()));
        ServletActionContext.getRequest().getSession().setAttribute("ref_num", ref_num);
        ServletActionContext.getRequest().getSession().setAttribute("form_type", formType);
        ServletActionContext.getRequest().getSession().setAttribute("mobile_flag", mobile_flag);
        ServletActionContext.getRequest().getSession().setAttribute("check_flag", check);
        ServletActionContext.getRequest().getSession().setAttribute("esignRes", esignRes);
        ServletActionContext.getRequest().getSession().setAttribute("empName", ldap.fetchNameFromLdap(formBean.getUser_email()));
        ServletActionContext.getRequest().getSession().setAttribute("empMobile", ldap.fetchMobileFromLdap(formBean.getUser_email()));
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + "session" + session);
        System.out.println(ServletActionContext.getRequest().getSession().getAttribute("empName") + " == " + "========= " + "session" );
        return esignRes;
    }

    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    public Map getSession() {
        return session;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    public String getServiceURL() {
        return serviceURL;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getRespon() {
        return respon;
    }

    public void setRespon(String respon) {
        this.respon = respon;
    }
    
        public String utcFormat(String date) throws ParseException {
        
        String modifyDate = "";
        //String formatDate = date.replaceAll("-", "") + "000000Z";
        //String formatDate = date.replaceAll("-", "");
        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyyMMdd");
        String formatedDate = newFormat.format(date1);
        System.out.println("FORMAT::::::" + formatedDate);
        
        modifyDate = formatedDate +"000000Z";
        return modifyDate;

    }
}
