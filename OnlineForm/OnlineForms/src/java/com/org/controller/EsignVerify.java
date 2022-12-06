package com.org.controller;

import admin.AdminAction;
import admin.ForwardAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.Serializable;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import org.nic.eoffice.esign.model.EsignRequest;
import org.nic.eoffice.esign.model.EsignRequestResponse;
import com.opensymphony.xwork2.ActionSupport;
import com.org.dao.EsignUtilDao;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.dao.Ldap;
import com.org.service.esignService;
import com.org.utility.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.nic.eoffice.esign.EsignResponse;
import static zimbra_soap_v2.Zimbra_SOAP_v2.getAdminAuthToken;

public class EsignVerify extends ActionSupport implements SessionAware, Serializable {

    private static final long serialVersionUID = 1L;
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    private Map<String, Object> session;
    private String xmlContent;
    private String serviceURL;
    private String respon;
    private String gatewayURL;
    private UserData userValues = new UserData();

    FormBean form_details;
    ForwardAction fwd = new ForwardAction();
    private final EsignUtilDao esignUtilDao;
    Map<String, Object> consentreturn = new HashMap<>();
    esignService esignService = null;
    ForwardAction fwdObj;
    AdminAction adminAction;

    public EsignVerify() {
        this.esignUtilDao = new EsignUtilDao();
        esignService = new esignService();
        fwdObj = new ForwardAction(); // line added by pr on 2ndaug19
        adminAction = new AdminAction();
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
        userValues = (UserData) session.get("uservalues");
        Map profile_values = (HashMap) session.get("profile-values");
        String formType = session.get("form_type").toString();
        String update_without_oldmobile = session.get("update_without_oldmobile").toString();
        String check = session.get("check").toString();
        String json = null;
        String eMail = "";
        String role = "";
        String outputFilename = "";
        String inputFileName = ref_num + ".pdf";
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
        setServiceURL("https://nic-esign2gateway.nic.in/eSign21/acceptClient");
        setGatewayURL("https://nic-esign2gateway.nic.in/eSign21/acceptClient");
//        setServiceURL("https://nic-esigngateway.nic.in/eSign21/acceptClient");
//        setGatewayURL("https://nic-esigngateway.nic.in/eSign21/acceptClient");
        if (userValues != null) {
            setUserName(userValues.getName());
        }

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
        EsignRequest req = esignUtilDao.getEsignFileRequest(inputFileName, outputFilename, pageNo, cordinates, uid, userValues, txn);
        System.out.println(req);
        EsignRequestResponse esignRequestResponse = esignUtilDao.getRequestXML(req);
        //session.put("esignRequestResponse", esignRequestResponse);
        //session.put("txn", txn);
        //System.out.println("EsignRequestResponse : " + esignRequestResponse + " TXN : " + txn);

        try {
            ObjectMapper myObjectMapper = new ObjectMapper();
            myObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            session.put("profile-values", profile_values);
            session.put("uservalues", userValues);
            json = myObjectMapper.writeValueAsString(session);
            //System.out.println("JSON VALUE for SESSION :::::" + json);
            //json = new ObjectMapper().writeValueAsString(session);
            //System.out.println("JSON VALUE for SESSION :::::" + json);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(EsignVerify.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "insert into esigntransaction (txn, inputfilename, outputfilename, pageno, cordinates, uid, email, ref_num,role, form_type, session_values,mobile_flag,check_flag) values(?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";
        if (userValues != null) {
            eMail = userValues.getAliasesInString();
        }
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
            ps.execute();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EsignVerify.class.getName()).log(Level.ALL.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EsignVerify.class.getName()).log(Level.SEVERE, null, ex);
        }

        setXmlContent(esignRequestResponse.getRequestXml());
        System.out.println(esignRequestResponse.getRequestXml());
        return SUCCESS;
    }

    public String esignPdfForRetiredOfficials() {
        String ref_num = (String) session.get("ref_num");
        String formType = session.get("form_type").toString();
        String check = session.get("check").toString();
        String json = null;
        String email = session.get("email").toString();
        String eMail = "";
        String outputFilename = "eSigned_retired_user_" + ref_num + ".pdf";
        String inputFileName = ref_num + ".pdf";

        String pageNo = "";
        String cordinates = "";
        String uid = "";
        setServiceURL("https://nic-esign2gateway.nic.in/eSign21/acceptClient");
        setGatewayURL("https://nic-esign2gateway.nic.in/eSign21/acceptClient");
//        setServiceURL("https://nic-esigngateway.nic.in/eSign21/acceptClient");
//        setGatewayURL("https://nic-esigngateway.nic.in/eSign21/acceptClient");
        setUserName(session.get("name").toString());

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
        EsignRequest req = esignUtilDao.getEsignFileRequestForRetiredOffcials(inputFileName, outputFilename, pageNo, cordinates, uid, session.get("name").toString(), txn);
        System.out.println(req);
        EsignRequestResponse esignRequestResponse = esignUtilDao.getRequestXML(req);

        try {
            ObjectMapper myObjectMapper = new ObjectMapper();
            myObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            json = myObjectMapper.writeValueAsString(session);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(EsignVerify.class.getName()).log(Level.SEVERE, null, ex);
        }

        String sql = "insert into esigntransaction (txn, inputfilename, outputfilename, pageno, cordinates, uid, email, ref_num,role, form_type, session_values,mobile_flag,check_flag) values(?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)";
        Ldap ldap = new Ldap();
        Set<String> aliases = ldap.fetchAliases(eMail);
        String commaSeparatedAliases = "";

        int sizeOfAliases = aliases.size();

        if (sizeOfAliases > 1 && sizeOfAliases <= 10) {
            for (String email1 : aliases) {
                commaSeparatedAliases += "'" + email1 + "',";
            }
            commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");
        } else if (sizeOfAliases == 1) {
            commaSeparatedAliases = "'" + aliases.iterator().next() + "'";
        }

        eMail = commaSeparatedAliases;

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
            ps.setString(9, "retireduser");
            ps.setString(10, formType);
            ps.setString(11, json);
            ps.setString(12, "false");
            ps.setString(13, check);
            ps.execute();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EsignVerify.class.getName()).log(Level.ALL.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EsignVerify.class.getName()).log(Level.SEVERE, null, ex);
        }

        setXmlContent(esignRequestResponse.getRequestXml());
        System.out.println(esignRequestResponse.getRequestXml());
        return SUCCESS;
    }

    public String eSignResponsePage() throws IOException, Exception {
        System.out.println("Inside eSign Response...... ");

        String ref_num = "";
        String formType = "";
        String txn = "";
        boolean ishog = false;
        boolean ishod = false;
        Ldap ldap = new Ldap();
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
        String inputFileName = null, outputFileName = null, pageNo = null, cordinates = null, uid = null, role = null, json = null, mobile_flag = null, check = null;
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

        String userValuesJson = null, formDetailsJson = null;

        Map<String, Object> response = null;
        UserData userValues = null;
        FormBean formBean = null;
        //String formDetails="";
        try {
            String formBeanDetails = "";
            if (formType.equals("vpn_single") || formType.equals("vpn_bulk") || formType.equals("vpn_renew") || formType.equals("change_add") || formType.equals("vpn_surrender")) {
                formBeanDetails = "vpn_details";
            } else if (formType.equals("nkn_single") || formType.equals("nkn_bulk")) {
                formBeanDetails = "nkn_details";
            } else if (formType.equals("distributionlist")) {
                formBeanDetails = "dlist_details";
            } else {
                formBeanDetails = formType + "_details";
            }
            response = new ObjectMapper().readValue(json, HashMap.class);
            userValuesJson = new ObjectMapper().writeValueAsString(response.get("uservalues"));
            userValues = new ObjectMapper().readValue(userValuesJson, UserData.class);
            formDetailsJson = new ObjectMapper().writeValueAsString(response.get(formBeanDetails));
            formBean = new ObjectMapper().readValue(formDetailsJson, FormBean.class);
            System.out.println("UserValues retired ::: " + userValues);
            System.out.println("formbean retired ::: " + formBean);
        } catch (IOException ex) {
            System.out.println("Error.....");
            Logger.getLogger(EsignVerify.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.remove("uservalues");
        response.remove(formType + "_details");

        for (Map.Entry<String, Object> entry : response.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            session.put(key, value);
        }

//        ServletActionContext.getContext().setSession(response);
//        this.session = ServletActionContext.getContext().getSession();
        System.out.println("SESSION VALUES ::: " + session);
        session.put("uservalues", userValues);
        if (formType.equals("distributionlist")) {
            session.put("dlist_details", formBean);
        } else if (formType.equals("vpn_single") || formType.equals("vpn_bulk") || formType.equals("vpn_renew") || formType.equals("change_add") || formType.equals("vpn_surrender")) {
            session.put("vpn_details", formBean);
        } else if (formType.equals("nkn_single") || formType.equals("nkn_bulk")) {
            session.put("nkn_details", formBean);
        } else {
            session.put(formType + "_details", formBean);
        }

        req = esignUtilDao.getEsignFileRequest(inputFileName, outputFileName, pageNo, cordinates, uid, userValues, txn);
        System.out.println(req);
        EsignRequestResponse esignRequestResponse = esignUtilDao.getRequestXML(req);
        EsignResponse resp = new EsignResponse();
        String esignRes = "";
        ref_num = registrationNo;
        Map profile_values = (HashMap) session.get("profile-values");
        String responseXml = getRespon();
        EsignRequestResponse esignreqsp1 = resp.getEsignResponse(esignRequestResponse.getContentType(), esignRequestResponse.getAppearance(), responseXml); // This method will sign pdf
        System.out.println("Esign Response Status " + esignreqsp1.getRespStatus());
        System.out.println("Error Message if failed " + esignreqsp1.getErrorMessage());
        System.out.println("Error Code ::" + esignreqsp1.getErrorCode());

        System.out.println("formType ::" + formType);
        System.out.println("===============================================================================================================");

        if (formType.equals("dns")) {
            session.put("form_text", "Domain Name System Services");
        } else if (formType.equals("sms")) {
            session.put("form_text", "Short Messaging Services");
        } else if (formType.equals("wifi")) {
            session.put("form_text", "Wi-Fi Services");
        } else if (formType.equals("single") || formType.equals("gem") || formType.equals("bulk") || formType.equals("nkn_single") || formType.equals("nkn_bulk")) {
            session.put("form_text", "Email Services for Government of India");
        } else if (formType.equals("imappop")) {
            session.put("form_text", "Enable or Disable IMAP/POP");
        } else if (formType.equals("mobile")) {
            session.put("form_text", "Update Your Mobile Number");
        } else if (formType.equals("ldap")) {
            session.put("form_text", "Authentication Services (LDAP)");
        } else if (formType.equals("distributionlist") || formType.equals("dlist")) {
            formType = "distributionlist";
            session.put("form_text", "Distribution List Services");
        } else if (formType.equals("ip")) {
            session.put("form_text", "Add/Change an IP for other services");
        } else if (formType.equals("relay")) {
            session.put("form_text", "SMTP Gateway Services (Relay)");
        } else if (formType.equals("vpn_single") || formType.equals("vpn_bulk") || formType.equals("vpn_renew") || formType.equals("change_add") || formType.equals("vpn_surrender")) {
            session.put("form_text", "Virtual Private Network Services");
        } else if (formType.equals("webcast")) {
            session.put("form_text", "Webcast Services");
        } else if (formType.equals("centralutm")) {
            session.put("form_text", "Central UTM Services");
        } else if (formType.equals("wifiport")) {
            session.put("form_text", "Wi-Fi Port Services");
        } else if (formType.equals("email_act")) {
            session.put("form_text", "Email Activate Services");
        } else if (formType.equals("email_deact")) {
            session.put("form_text", "Email Deactivate Services");
        } else if (formType.equals("dor_ext")) {
            session.put("form_text", "Email Account validation Extension Service");
        } else if (formType.equals("dor_ext_retired")) {
            session.put("form_text", "Email Account validation Extension Service for Retired Officials");
        } else {
            formType = "";
        }

        if (esignreqsp1.getRespStatus() == 0) {
            if (role.equals(Constants.ROLE_CA)) {
                fwd.setRegNo(ref_num + "~" + formType + "~forward");
                fwd.setApp_ca_path("/eForms/PDF");
                fwd.setApp_ca_type("esign");
                fwd.setStatRemarks("esigned request at RO level");
                esignRes = "error";
                session.put("moduleEsign", "esignFail");
            } else {
                session.put("moduleEsign", "esignFail");
                esignRes = "success";
            }
        } else {
            if (role.equals(Constants.ROLE_CA)) {
                if (formType.equals("distributionlist")) {
                    formType = "dlist";
                }
                fwd.setRegNo(ref_num + "~" + formType + "~forward");
                fwd.setApp_ca_path("/eForms/PDF");
                fwd.setApp_ca_type("esign");
                fwd.setStatRemarks("esigned request at RO level");
                String result = fwd.execute();
                if (result.equals("success")) {
                    String responseMessageForEsign = fwd.getMsg();
                    boolean eSignIsSuccess = fwd.getIsSuccess();
                    boolean eSignIsError = fwd.getIsError();
                    session.put("moduleEsign", "esign");
                    esignRes = "error";
                }
            } else {
                String state_min = "";
                System.out.println("Before entering into if block retired UserValues ::: " + userValues + " formbean :: " + formBean);
                if (formBean != null & userValues != null) {
                    userValues = (UserData) session.get("uservalues");

                    String category = userValues.getUserOfficialData().getEmployment();   //02june2022
                    String department = "";
                    if (category != null && category.equalsIgnoreCase("central") || category != null && category.equalsIgnoreCase("ut")) {
                        String ministry = userValues.getUserOfficialData().getMinistry();
                        if (ministry != null && !(ministry.isEmpty())) {
                            state_min = ministry;
                        }
                        department = userValues.getUserOfficialData().getDepartment();
                    } else if (category != null && category.equalsIgnoreCase("state")) {
                        String state = userValues.getUserOfficialData().getState();
                        if (state != null && !(state.isEmpty())) {
                            state_min = state;
                        }
                        department = userValues.getUserOfficialData().getDepartment();
                    } else {
                        String org = userValues.getUserOfficialData().getOrganizationCategory();
                        if (org != null && !(org.isEmpty())) {
                            state_min = org;
                        }
                    }

                    consentreturn = esignService.consent("esign_update", ref_num, formType, formBean, profile_values, userValues.isIsHOG(), userValues.isIsHOD(), category, state_min, department);
                    session.put("moduleEsign", "esign");
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + formType);
                    String mob = "";
                    if (formType.equals("mobile")) {
                        if (session.get("mobileNumber") != null) {
                            mob = session.get("mobileNumber").toString();

                        }
                        String dn = fwdObj.fetchLDAPDN(userValues.getEmail(), formType);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + dn);

                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DN " + dn);
                        if (session.get("mobileNumber") != null) {
                            if (!dn.equals("")) {

                                adminAction.modifyLDAPAttribute(dn, "mobile", mob);
                                // start, code added by pr on 12thapr9

                                HashMap<String, String> zim_values = new HashMap<String, String>();
                                zim_values.put("mobile", mob);
                                //zim_values.put("form_name", Constants.MOB_FORM_KEYWORD);
                                URL url = null;
                                String admin_token = "";
                                url = new URL("https://100.80.17.250:7071/service/admin/soap/");
                                //admin_token = getAdminAuthToken("createuserapp@createaccount.da", "fgaT8.8A_bGm", url);
                                //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "A@#poi$^KJ39", url);
                                //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "#ZyPE@8xR#H$TJxT9^H=CTZ@#43f#Dg4dV#Xz@", url);
                                admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "Q=2E+D4Kf=BYi+K7De=4TjA+xFq6Z=T7+hDs=4x", url);
                                adminAction.modifyZimLDAPAttribute(admin_token, url, userValues.getEmail(), zim_values, Constants.MOB_FORM_KEYWORD);// above line commented this line modified by pr on 12thapr19
                                // end, code added by pr on 12thapr9
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "=========esign succes ");
                            }
                        }
                    }
                    esignRes = "success";
                } else {
                    session.put("moduleEsign", "esignFail");
                    esignRes = "success";
                }

            }
        }
        session.put("ref_num", ref_num);
        session.put("form_type", formType);
        session.put("mobile_flag", mobile_flag);
        session.put("check_flag", check);
        return esignRes;
    }

    public String eSignResponsePageForRetiredOfficials() throws IOException, Exception {
        System.out.println("Inside eSign Response retired...... ");

        String ref_num = "";
        String formType = "";
        String txn = "";
        Ldap ldap = new Ldap();
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
        String inputFileName = null, outputFileName = null, pageNo = null, cordinates = null, uid = null, role = null, json = null, mobile_flag = null, check = null;
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

          System.out.println("json of retired in  retired eSignResponsePageForRetiredOfficials() " + json);
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
            System.out.println("Json retired " + formDetailsJson);
            System.out.println("response retired " + response);
            System.out.println("formbean retired ::: " + formBean.toString());
        } catch (IOException ex) {
            System.out.println("Error.....");
            Logger.getLogger(EsignVerify.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.remove(formType + "_details");

        for (Map.Entry<String, Object> entry : response.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            session.put(key, value);
        }

        System.out.println("SESSION VALUES retired ::: " + session);

        req = esignUtilDao.getEsignFileRequestForRetiredOffcials(inputFileName, outputFileName, pageNo, cordinates, uid, session.get("name").toString(), txn);
        System.out.println("req of retired: "+req);
        EsignRequestResponse esignRequestResponse = esignUtilDao.getRequestXML(req);
        EsignResponse resp = new EsignResponse();
        ref_num = registrationNo;

        String responseXml = getRespon();
        EsignRequestResponse esignreqsp1 = resp.getEsignResponse(esignRequestResponse.getContentType(), esignRequestResponse.getAppearance(), responseXml); // This method will sign pdf
        System.out.println("Esign Response Status " + esignreqsp1.getRespStatus());
        System.out.println("Error Message if failed " + esignreqsp1.getErrorMessage());
        System.out.println("Error Code ::" + esignreqsp1.getErrorCode());

        System.out.println("formType ::" + formType);
        System.out.println("===============================================================================================================");

        session.put("form_text", "Email Account validation Extension Service for Retired Officials");

        if (formBean != null) {
            consentreturn = esignService.consentRetired("esign_update", ref_num, formType, formBean);
            session.put("moduleEsign", "esign");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "========= " + formType);
            session.put("message", "Your request for extension of Account has been successfully completed!!!. Now, your account will expire on "+formBean.getSingle_dor());
        } else {
            session.put("moduleEsign", "esignFail");
        }

        session.put("ref_num", ref_num);
        session.put("form_type", formType);
        session.put("mobile_flag", "false");
        session.put("check_flag", "false");
        return "success";
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
}
