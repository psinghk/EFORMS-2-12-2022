package com.org.dao;

import admin.AdminAction;
import admin.ForwardAction;
import admin.UserTrack;
import com.itextpdf.text.Element;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.org.bean.Forms;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.controller.Vpn_registration;
import com.org.service.DnsService;
import com.org.service.ProfileService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import entities.LdapQuery;
import java.awt.Color;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import rabbitmq.NotifyThrouhRabbitMQ;
import utility.VpnPushApi;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class esignDao {

    private String printlog = "";
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    private Connection con = null;
    private Connection conSlave = null;
    private Date date = null;
    private DateFormat dt = null;
    String pdate = null;
    FormBean form_details;
    ForwardAction fwd = null;
    private String himachalDept;
    private Database db = null;
    private Ldap ldap = null;

    public String getHimachalDept() {
        return himachalDept;
    }

    public void setHimachalDept(String himachalDept) {
        this.himachalDept = himachalDept;
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public esignDao() {
        printlog = ServletActionContext.getRequest().getSession().getId() + " == esignDao.java == ";
        date = new Date();
        dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pdate = dt.format(date);
        fwd = new ForwardAction();
        form_details = new FormBean();
        db = new Database();
        ldap = new Ldap();
    }

    public Map<String, Object> consent(String check, String ref_num, String formtype, FormBean form_details, Map profile_values, boolean isHog, boolean isHod, String category, String ministry, String department) {
        //isHog = true;
        Map<String, Object> consentreturn = new HashMap<String, Object>();
        this.form_details = form_details;
        String CaName = "";
        String CaMobile = "";
        String CaEmail = "";
        String tableName = "";
        String UserMobile = "", UserEmail = ""; // above lines commented this added by pr on 12thapr18      
        if (profile_values != null) // if around added by pr on 12thapr18
        {
            if (!check.equals("esign_admin")) {
                UserMobile = profile_values.get("mobile").toString();
                UserEmail = profile_values.get("email").toString();
            }
            if (formtype.equals("distributionlist")) {
                CaName = profile_values.get("hod_name").toString();
                CaEmail = profile_values.get("hod_email").toString();
                CaMobile = profile_values.get("hod_mobile").toString();
                form_details.setHod_mobile(CaMobile);
                tableName = "distribution_registration";
            } else if (formtype.equals("bulkdistributionlist")) {     //Added by Rahul jan 2021
                CaName = profile_values.get("hod_name").toString();
                CaEmail = profile_values.get("hod_email").toString();
                CaMobile = profile_values.get("hod_mobile").toString();
                form_details.setHod_mobile(CaMobile);
                tableName = "bulk_distribution_registration";
            } else if (formtype.equals("vpn_single") || formtype.equals("vpn_bulk") || formtype.equals("vpn_renew") || formtype.equals("change_add") || formtype.equals("vpn_surrender") || formtype.equals("vpn_delete")) {
                CaName = profile_values.get("hod_name").toString();
                CaEmail = profile_values.get("hod_email").toString();
                CaMobile = profile_values.get("hod_mobile").toString();
                form_details.setHod_mobile(CaMobile);
                tableName = "vpn_registration";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                CaName = profile_values.get("hod_name").toString();
                CaEmail = profile_values.get("hod_email").toString();
                CaMobile = profile_values.get("hod_mobile").toString();
                form_details.setHod_mobile(CaMobile);
                tableName = "nkn_registration";
            } else if (formtype.equalsIgnoreCase("dor_ext_retired")) {
                form_details.setHod_mobile(CaMobile);
                tableName = "dor_ext_retired_registration";
            } else if (formtype.equals("gem")) {

                if (!check.equals("esign_admin")) {

                    CaName = form_details.getFwd_ofc_name();
                    CaEmail = form_details.getFwd_ofc_email();
                    CaMobile = form_details.getFwd_ofc_mobile();
                    tableName = formtype + "_registration";

                } else {

                    tableName = formtype + "_registration";
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin in esign dao  else");

                CaName = profile_values.get("hod_name").toString();
                CaEmail = profile_values.get("hod_email").toString();
                CaMobile = profile_values.get("hod_mobile").toString();
                form_details.setHod_mobile(CaMobile);
                tableName = formtype + "_registration";
            }
        }

        try {
            conSlave = DbConnection.getSlaveConnection();
            String ref_exist = "select * from " + tableName + " where registration_no ='" + ref_num + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin in esign dao   " + ref_exist);

            PreparedStatement stmt = conSlave.prepareStatement(ref_exist);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println(printlog + "if not found records in main table");
                consentreturn.put("checkreturn", "unsuccess");
            } else {
                String min_val = "";
                if (profile_values.get("min") != null) // if around added by pr on 12thapr18
                {

                    if (profile_values.get("min") != null) {
                        if (profile_values.get("min").toString() != "") {
                            min_val = profile_values.get("min").toString();
                        }
                    }
                    if (profile_values.get("dept") != null) {
                        if (profile_values.get("dept").toString() != "") {
                            min_val = profile_values.get("dept").toString();
                        }
                    }
                    if (profile_values.get("Org") != null) {
                        if (profile_values.get("Org").toString() != "") {
                            min_val = profile_values.get("Org").toString();
                        }
                    }
                }
//                boolean isPdfPathExist = false;
//
//                String pdfpath_exist = "select pdf_path from " + tableName + " where registration_no ='" + ref_num + "'";
//                stmt = conSlave.prepareStatement(pdfpath_exist);
//                rs = stmt.executeQuery();
//                while (rs.next()) {
//                    {
//                        String pdfPath = rs.getString("pdf_path");
//                        if (pdfPath != null && !pdfPath.equals("")) {
//
//                            isPdfPathExist = true;
//                        }
//                    }
//                }
//
//                if (!isPdfPathExist) {

//below if block commented by sahil on 16 august 2021 as per the instruction of ashwini sir
//                if (profile_values.get("min") != null) {
//
//                    if (profile_values.get("min").toString().equalsIgnoreCase("External Affairs") && profile_values.get("dept").toString().equalsIgnoreCase("Indian Council for Cultural Relations(ICCR)")) {
//                        CaName = profile_values.get("hod_name").toString();
//                        CaEmail = profile_values.get("hod_email").toString();
//                        CaMobile = profile_values.get("hod_mobile").toString();
//                    } else if (profile_values.get("min").toString().equalsIgnoreCase("External Affairs")) {
//                        CaName = "Dr S JANAKIRAMAN";
//
//                        CaMobile = "+919384664224";
//
//                        CaEmail = "jsegit@mea.gov.in";
//                    }
//                }
                if (check.equals("online")) {
                    consentreturn.put("checkreturn", check);
                    consentreturn.put("Ref_string", "Your form has been submitted and your Registration number is <span class='reg_no_sty'>" + ref_num + "</span>.");
                    String file = "online processing";

                    switch (formtype) {

                        case "wifi":

//                                if (form_details.getWifi_process().equals("req_delete")) {
//                                    // this block is sending wifi requests directly to the admin
//                                    ArrayList<String> coorddata = new ArrayList<String>();
//                                    coorddata = getWifiAdmin();
//                                    String coordinators = String.join(",", coorddata);
//                                    UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values);
//                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
//                                    SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
//
//                                } else 
                            if (form_details.getNic_employee().equals("true")) {
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = getWifiAdmin();
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);

                            } else {
                                if (profile_values.get("dept") != null) {
                                    if (profile_values.get("dept").toString() != "" && profile_values.get("dept").toString().contains("Rajasthan Legislative Assembly")) {
                                        CaEmail = "mishra.vinod@nic.in";
                                        CaMobile = "+919462660986";
                                        CaName = "Vinod Mishra";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "wifiport":
                            if ((isHog || isHod) && form_details.getNic_employee().equals("true")) {
                                //if (isHog || isHod) {
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata.add("guptakk@nic.in");
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusWifoport(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                //  } else{       
//                                     UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values);
//                                     System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
//                                     SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                // }
                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
//                                    if (profile_values.get("dept") != null) {
//                                        if (profile_values.get("dept").toString() != "" && profile_values.get("dept").toString().contains("Rajasthan Legislative Assembly")) {
//                                            CaEmail = "mishra.vinod@nic.in";
//                                            CaMobile = "+919462660986";
//                                            CaName = "Vinod Mishra";
//                                        }
//                                    }
//                                    UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values);
//                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
//                                    SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "imappop":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                //coorddata =  null;
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else if (profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());
                                if (Cadata.size() > 0) {
                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    }
                                }

                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "mobile":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else if (profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                if (Cadata.size() > 0) {

                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    }
                                }

                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "relay":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "vpn_single":
                        case "vpn_bulk":
                        case "vpn_renew":
                        case "vpn_surrender":
                        case "vpn_delete":
                        case "change_add":
                            if (isHog || isHod) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                VpnPushApi vpnapi = new VpnPushApi();
                                vpnapi.callVpnWebService(ref_num);
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else {
                                CaName = profile_values.get("hod_name").toString();

                                CaMobile = profile_values.get("hod_mobile").toString();

                                CaEmail = profile_values.get("hod_email").toString();
                                if (profile_values.get("min") != null) {

                                    if (profile_values.get("min").toString().equalsIgnoreCase("Railways,Railnet")) {
                                        CaName = "Sh, Dharmendra Singh";

                                        CaMobile = "+919810370486";

                                        CaEmail = "dtele@rb.railnet.gov.in";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "centralutm":
                            admin.ForwardAction ad = new admin.ForwardAction();
                            ArrayList<String> coorddata = new ArrayList<>();
//                            coorddata = new ArrayList<String>();
//                            coorddata = ad.getMailAdmins(formtype);
                            String coordinators = "hasan@gov.in,ashish@nic.in,sseca2.sp-dl@nkn.in,tiwari.ashwini@nic.in";
                            //String coordinators = "tiwari.ashwini@nic.in";
                            //coorddata = (ArrayList<String>) Arrays.asList(coordinators.split(","));

                            //coorddata.add(coordinators);
                            // above line commented below three added by pr on 21stjun19
                            coorddata.add("hasan@gov.in");

                            coorddata.add("ashish@nic.in");

                            coorddata.add("sseca2.sp-dl@nkn.in");

                            UpdateStatusFirewall(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                            System.out.println(printlog + " == status, comp auth, user check, and filepath is updated!!!!");
                            SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            break;
                        case "webcast":
                            admin.ForwardAction ad1 = new admin.ForwardAction();
                            coorddata = new ArrayList<String>();
                            coorddata = ad1.fetchMailAdmins(formtype);
                            coordinators = String.join(",", coorddata);
                            UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                            SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            break;
                        default:
                            if ((formtype.equals("single") || formtype.equals("bulk")) && (form_details.getReq_for().equals("eoffice"))) {
                                coorddata = new ArrayList<String>();
                                coordinators = "rachna_sri@nic.in";
                                coorddata.add(coordinators);
                                UpdateStatusSingle_bulk(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else if ((formtype.equals("single") || formtype.equals("bulk") || formtype.equals("nkn")) && profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                if (Cadata.size() > 0) {
                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                    }
                                }
                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            if ((formtype.equals("single"))) {
                                if (form_details.getAction().equals("yes")) {

                                    SendMailRajp(formtype, ref_num, UserEmail, UserMobile, form_details);
                                }
                            }

                    }

                    SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                } else if (check.equals("esign")) {
                    consentreturn.put("checkreturn", check);
                    consentreturn.put("Ref_string", "Your form has been submitted and your Registration number is <span class='reg_no_sty'>" + ref_num + "</span>.");
                    if (formtype.equalsIgnoreCase("dor_ext_retired")) {
                        GenerateWhichPDF(form_details, ref_num, formtype);
                    } else {
                        form_details.setUser_employment(profile_values.get("user_employment").toString());
                        if (profile_values.get("user_employment").toString().equals("Central")) {
                            form_details.setMin(profile_values.get("min").toString());
                            form_details.setDept(profile_values.get("dept").toString());
                            form_details.setStateCode("");
                            form_details.setState_dept("");
                            form_details.setOrg("");
                        } else if (profile_values.get("user_employment").toString().equals("Others") || profile_values.get("user_employment").toString().equals("Psu") || profile_values.get("user_employment").toString().equals("Const")) {
                            form_details.setMin("");
                            form_details.setDept("");
                            form_details.setStateCode("");
                            form_details.setState_dept("");
                            form_details.setOrg(profile_values.get("Org").toString());
                        } else {
                            form_details.setMin("");
                            form_details.setDept("");
                            form_details.setStateCode(profile_values.get("stateCode").toString());
                            form_details.setState_dept(profile_values.get("dept").toString());
                            form_details.setOrg("");
                        }
                        form_details.setOther_dept(profile_values.get("other_dept").toString());
                        form_details.setUser_name(profile_values.get("cn").toString());
                        form_details.setUser_email(profile_values.get("email").toString());
                        form_details.setUser_mobile(profile_values.get("mobile").toString());
                        form_details.setUser_address(profile_values.get("postalAddress").toString());
                        form_details.setUser_ophone(profile_values.get("ophone").toString());
                        form_details.setUser_rphone(profile_values.get("rphone").toString());
                        form_details.setUser_city(profile_values.get("nicCity").toString());
                        form_details.setUser_state(profile_values.get("state").toString());
                        form_details.setUser_empcode(profile_values.get("emp_code").toString());
                        form_details.setUser_pincode(profile_values.get("pin").toString());
                        form_details.setUser_design(profile_values.get("desig").toString());
                        form_details.setHod_email(profile_values.get("hod_email").toString());
                        form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
                        form_details.setHod_name(profile_values.get("hod_name").toString());
                        form_details.setHod_tel(profile_values.get("hod_tel").toString());
                        form_details.setCa_design(profile_values.get("ca_design").toString());
                        form_details.setCa_email(profile_values.get("ca_email").toString());
                        form_details.setCa_mobile(profile_values.get("ca_mobile").toString());
                        form_details.setCa_name(profile_values.get("ca_name").toString());
                        GenerateWhichPDF(form_details, ref_num, formtype);
                    }
                } else if (check.equals("manual_upload")) {
                    consentreturn.put("checkreturn", "manual_upload");
                    consentreturn.put("Ref_string", "Your Registration number " + ref_num + " has been created successfully");
//                        String file = "/eForms/PDF/" + ref_num + ".pdf";
                    String file = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
                    switch (formtype) {
                        case "vpn_single":
                        case "vpn_bulk":
                        case "vpn_renew":
                        case "vpn_surrender":
                        case "vpn_delete":
                        case "change_add":

//                            if (isHog || isHod) {
//                                admin.ForwardAction ad = new admin.ForwardAction();
//                                ArrayList<String> coorddata = new ArrayList<>();
//                                coorddata = ad.fetchMailAdmins(formtype);
//                                String coordinators = String.join(",", coorddata);
//                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values);
//                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "status, comp auth, user check, and filepath is updated!!!!");
//                                VpnPushApi vpnapi = new VpnPushApi();
//                                vpnapi.callVpnWebService(ref_num);
//                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
//                            } else {
                            CaName = profile_values.get("hod_name").toString();
                            CaMobile = profile_values.get("hod_mobile").toString();
                            CaEmail = profile_values.get("hod_email").toString();
                            if (profile_values.get("min") != null) {
                                if (profile_values.get("min").toString().equalsIgnoreCase("Railways,Railnet")) {
                                    CaName = "Sh, Dharmendra Singh";
                                    CaMobile = "+919810370486";
                                    CaEmail = "dtele@rb.railnet.gov.in";
                                }
                            }
                            UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                            //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            // }
                            break;
                        case "wifi":
//                                if (form_details.getWifi_process().equals("req_delete")) {
//                                    // this block is sending wifi requests directly to the admin
//                                    ArrayList<String> coorddata = new ArrayList<String>();
//                                    coorddata = getWifiAdmin();
//                                    String coordinators = String.join(",", coorddata);
//                                    UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values);
//                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
//                                    SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
//
//                                } else 
                            if (form_details.getNic_employee().equals("true")) {
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = getWifiAdmin();
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusManual(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else {
                                if (profile_values.get("dept") != null) {
                                    if (profile_values.get("dept").toString() != "" && profile_values.get("dept").toString().equalsIgnoreCase("the Rajasthan Legislative Assembly")) {
                                        CaEmail = "mishra.vinod@nic.in";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                // SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                            }
                            break;
                        case "wifiport":
                            if (form_details.getNic_employee().equals("true")) {
                                if (isHog || isHod) {
                                    ArrayList<String> coorddata = new ArrayList<String>();
                                    coorddata.add("guptakk@nic.in");
                                    String coordinators = String.join(",", coorddata);
                                    UpdateStatusWifoport(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                    // SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                } else {
                                    UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                    // SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                }
                            } else {
                                if (profile_values.get("dept") != null) {
                                    if (profile_values.get("dept").toString() != "" && profile_values.get("dept").toString().contains("Rajasthan Legislative Assembly")) {
                                        CaEmail = "mishra.vinod@nic.in";
                                        CaMobile = "+919462660986";
                                        CaName = "Vinod Mishra";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                // SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "imappop":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusManual(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else if (profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                if (Cadata.size() > 0) {
                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    }
                                }

                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "mobile":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusManual(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
                            } else if (profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                if (Cadata.size() > 0) {
                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    }
                                }

                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "relay":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusManual(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "centralutm":
                            admin.ForwardAction ad = new admin.ForwardAction();
                            ArrayList<String> coorddata = new ArrayList<>();
//                            coorddata = new ArrayList<String>();
//                            coorddata = ad.getMailAdmins(formtype);
                            String coordinators = "hasan@gov.in,ashish@nic.in,sseca2.sp-dl@nkn.in,tiwari.ashwini@nic.in";
                            //String coordinators = "tiwari.ashwini@nic.in";
                            //coorddata = (ArrayList<String>) Arrays.asList(coordinators.split(","));

                            //coorddata.add(coordinators);
                            // above line commented below three added by pr on 21stjun19
                            coorddata.add("hasan@gov.in");

                            coorddata.add("ashish@nic.in");

                            coorddata.add("sseca2.sp-dl@nkn.in");

                            UpdateStatusFirewall(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                            System.out.println(printlog + " status, comp auth, user check, and filepath is updated!!!!");
                            //SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
                            break;
                        case "webcast":
                            admin.ForwardAction ad1 = new admin.ForwardAction();
                            coorddata = new ArrayList<String>();
                            coorddata = ad1.fetchMailAdmins(formtype);
                            coordinators = String.join(",", coorddata);
                            UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                            //SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            break;
                        default:
                            if ((formtype.equals("single") || formtype.equals("bulk")) && (form_details.getReq_for().equals("eoffice"))) {
                                coorddata = new ArrayList<String>();
                                coordinators = "rachna_sri@nic.in";
                                coorddata.add(coordinators);
                                UpdateStatusSingle_bulk(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else if ((formtype.equals("single") || formtype.equals("bulk") || formtype.equals("nkn")) && profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                if (Cadata.size() > 0) {
                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                    }
                                }
                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            if ((formtype.equals("single"))) {
                                if (form_details.getAction().equals("yes")) {

                                    SendMailRajp(formtype, ref_num, UserEmail, UserMobile, form_details);
                                }
                            }

                    }
                    form_details.setUser_employment(profile_values.get("user_employment").toString());
                    if (profile_values.get("user_employment").toString().equals("Central")) {
                        form_details.setMin(profile_values.get("min").toString());
                        form_details.setDept(profile_values.get("dept").toString());
                        form_details.setStateCode("");
                        form_details.setState_dept("");
                        form_details.setOrg("");
                    } else if (profile_values.get("user_employment").toString().equals("Others") || profile_values.get("user_employment").toString().equals("Psu") || profile_values.get("user_employment").toString().equals("Const")) {
                        form_details.setMin("");
                        form_details.setDept("");
                        form_details.setStateCode("");
                        form_details.setState_dept("");
                        form_details.setOrg(profile_values.get("Org").toString());
                    } else {
                        form_details.setMin("");
                        form_details.setDept("");
                        form_details.setStateCode(profile_values.get("stateCode").toString());
                        form_details.setState_dept(profile_values.get("dept").toString());
                        form_details.setOrg("");
                    }
                    form_details.setOther_dept(profile_values.get("other_dept").toString());
                    form_details.setUser_name(profile_values.get("cn").toString());
                    form_details.setUser_email(profile_values.get("email").toString());
                    form_details.setUser_mobile(profile_values.get("mobile").toString());
                    form_details.setUser_address(profile_values.get("postalAddress").toString());
                    form_details.setUser_ophone(profile_values.get("ophone").toString());
                    form_details.setUser_rphone(profile_values.get("rphone").toString());
                    form_details.setUser_city(profile_values.get("nicCity").toString());
                    form_details.setUser_state(profile_values.get("state").toString());
                    form_details.setUser_empcode(profile_values.get("emp_code").toString());
                    form_details.setUser_pincode(profile_values.get("pin").toString());
                    form_details.setUser_design(profile_values.get("desig").toString());
                    form_details.setHod_email(profile_values.get("hod_email").toString());
                    form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
                    form_details.setHod_name(profile_values.get("hod_name").toString());
                    form_details.setHod_tel(profile_values.get("hod_tel").toString());
                    form_details.setCa_design(profile_values.get("ca_design").toString());
                    form_details.setCa_email(profile_values.get("ca_email").toString());
                    form_details.setCa_mobile(profile_values.get("ca_mobile").toString());
                    form_details.setCa_name(profile_values.get("ca_name").toString());
                    GenerateWhichPDF(form_details, ref_num, formtype);
                    SendMailManual(formtype, ref_num, UserEmail, UserMobile, file);
                } else if (check.equals("esign_update")) {
                    consentreturn.put("checkreturn", check);
                    consentreturn.put("Ref_string", "Your form has been submitted and your Registration number is <span class='reg_no_sty'>" + ref_num + "</span>.");
                    String file = "";
                    //file = "/eForms/PDF/eSigned_" + ref_num + ".pdf";
                    file = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation") + "eSigned_" + ref_num + ".pdf";
                    switch (formtype) {
                        case "vpn_single":
                        case "vpn_bulk":
                        case "vpn_renew":
                        case "change_add":
                            if (isHog || isHod) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                VpnPushApi vpnapi = new VpnPushApi();
                                vpnapi.callVpnWebService(ref_num);
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            } else {
                                CaName = profile_values.get("hod_name").toString();

                                CaMobile = profile_values.get("hod_mobile").toString();

                                CaEmail = profile_values.get("hod_email").toString();
                                if (profile_values.get("min") != null) {

                                    if (profile_values.get("min").toString().equalsIgnoreCase("Railways,Railnet")) {
                                        CaName = "Sh, Dharmendra Singh";

                                        CaMobile = "+919810370486";

                                        CaEmail = "dtele@rb.railnet.gov.in";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                            }
                            break;
                        case "wifi":
//                                if (form_details.getWifi_process().equals("req_delete")) {
//                                    // this block is sending wifi requests directly to the admin
//                                    ArrayList<String> coorddata = new ArrayList<String>();
//                                    coorddata = getWifiAdmin();
//                                    String coordinators = String.join(",", coorddata);
//                                    UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values);
//                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
//                                    SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val);
//
//                                } else 
                            if (form_details.getNic_employee().equals("true")) {
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = getWifiAdmin();
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            } else {
                                if (profile_values.get("dept") != null) {
                                    if (profile_values.get("dept").toString() != "" && profile_values.get("dept").toString().equalsIgnoreCase("the Rajasthan Legislative Assembly")) {
                                        CaEmail = "mishra.vinod@nic.in";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            }
                            break;
                        case "wifiport":
                            if (form_details.getNic_employee().equals("true")) {
                                if (isHog || isHod) {
                                    ArrayList<String> coorddata = new ArrayList<String>();
                                    coorddata.add("guptakk@nic.in");
                                    String coordinators = String.join(",", coorddata);
                                    UpdateStatusWifoport(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                    SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                    SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                                } else {
                                    UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                    SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                    SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                                }
                            } else {
                                if (profile_values.get("dept") != null) {
                                    if (profile_values.get("dept").toString() != "" && profile_values.get("dept").toString().contains("Rajasthan Legislative Assembly")) {
                                        CaEmail = "mishra.vinod@nic.in";
                                        CaMobile = "+919462660986";
                                        CaName = "Vinod Mishra";
                                    }
                                }
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            }
                            break;
                        case "imappop":
                            System.out.println("form_details.getNic_employee() " + form_details.getNic_employee());
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            } else if (profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                ArrayList<String> Cadata = new ArrayList<String>();
                                Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                if (Cadata.size() > 0) {
                                    if (Cadata.get(0).toString().equals("nodata")) {
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                        SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);

                                    } else {
                                        CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                        if (CaMobile.contains("error")) {
                                            CaMobile = "";
                                        }
                                        CaEmail = Cadata.get(0).toString();
                                        CaName = Cadata.get(1).toString();
                                        UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                        System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                        SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                        SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);

                                    }
                                }

                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            }
                            break;
                        case "mobile":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                //SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                //SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                                SendCompleteMail(formtype, ref_num, UserEmail, UserMobile, form_details);
                            } else {
                                if (profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                    ArrayList<String> Cadata = new ArrayList<String>();
                                    Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                    if (Cadata.size() > 0) {
                                        if (Cadata.get(0).toString().equals("nodata")) {
                                            UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                            //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                        } else {
                                            CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                            if (CaMobile.contains("error")) {
                                                CaMobile = "";
                                            }
                                            CaEmail = Cadata.get(0).toString();
                                            CaName = Cadata.get(1).toString();
                                            UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                            //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);

                                        }
                                    }

                                } else {
                                    UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                    //SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                }
                                SendCompleteMail(formtype, ref_num, UserEmail, UserMobile, form_details);
                            }
                            break;
                        case "relay":
                            if (form_details.getNic_employee().equals("true")) {
                                admin.ForwardAction ad = new admin.ForwardAction();
                                ArrayList<String> coorddata = new ArrayList<String>();
                                coorddata = ad.fetchMailAdmins(formtype);
                                String coordinators = String.join(",", coorddata);
                                UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            } else {
                                UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            }
                            break;
                        case "centralutm":
                            admin.ForwardAction ad = new admin.ForwardAction();
                            ArrayList<String> coorddata = new ArrayList<>();
//                            coorddata = new ArrayList<String>();
//                            coorddata = ad.getMailAdmins(formtype);
                            String coordinators = "hasan@gov.in,ashish@nic.in,sseca2.sp-dl@nkn.in,tiwari.ashwini@nic.in";
                            //String coordinators = "tiwari.ashwini@nic.in";
                            //coorddata = (ArrayList<String>) Arrays.asList(coordinators.split(","));

                            //coorddata.add(coordinators);
                            // above line commented below three added by pr on 21stjun19
                            coorddata.add("hasan@gov.in");

                            coorddata.add("ashish@nic.in");

                            coorddata.add("sseca2.sp-dl@nkn.in");

                            UpdateStatusFirewall(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                            SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            break;
                        case "webcast":
                            admin.ForwardAction ad1 = new admin.ForwardAction();
                            coorddata = new ArrayList<String>();
                            coorddata = ad1.fetchMailAdmins(formtype);
                            coordinators = String.join(",", coorddata);
                            UpdateStatusImap(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                            SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                            break;
                        case "dor_ext":
//                            System.out.println("Inside Dor_ext Retired Emp Esing Submission");
//                            System.out.println("Login ip for Dor_ext of Retired Emp: " + ip);
//                            UserEmail = profile_values.get("email").toString();
//
//                            //----------------------------------- start process of Updation in Ldap
//                            ForwardAction fwdObj = new ForwardAction();
//                            AdminAction aa = new AdminAction();
//
//                            String email = "",
//                             dn = "",
//                             attValue = "";
//
//                            email = form_details.getDor_email().toString();
//                            dn = fwdObj.fetchLDAPDN(email, formtype);
//                            HashMap<String, String> HMZim = aa.fetchZimOtp(email);
//                            String dexp = form_details.getSingle_dor();
//                            String dob = form_details.getSingle_dob();
//                            String dexp1 = utcFormat(dexp);
//                            String dob1 = utcFormat(dob);
//
//                            HashMap<String, String> zim_values = new HashMap<String, String>();
//                            zim_values.put("nicAccountExpDate", dexp1);
//                            zim_values.put("nicDateOfRetirement", dexp1);
//                            zim_values.put("nicDateOfBirth", dob1);
//
//                            aa.modifyActivateAttribute(dn, zim_values, formtype);
//                            System.out.println("Zim value for Retired Emp:" + zim_values);
////                                HashMap<String, String> zim_values1 = new HashMap<String, String>();
////                                zim_values1.put("legacyDateOfRetirement", dexp1);
////                                zim_values1.put("legacyAccountExpDate", dexp1);
////                                zim_values1.put("legacyDateOfBirth", dob1);
////
////                                System.out.println("legacy map" + zim_values1);
//                            //----------------------------------- End process of Updation in Ldap
//                            UpdateStatusForRetiredEmp(ref_num, UserMobile, formtype, check, profile_values, category, ministry, department);
//                            System.out.println(printlog + "status, comp auth, user check, is updated!!!!");
//                            SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);

                            break;
                        case "dor_ext_retired":
                            System.out.println("Inside Dor_ext Retired Emp Esing Submission");
                            System.out.println("Login ip for Dor_ext of Retired Emp: " + ip);
                            UserEmail = profile_values.get("email").toString();

                            //----------------------------------- start process of Updation in Ldap
                            ForwardAction fwdObj = new ForwardAction();
                            AdminAction aa = new AdminAction();

                            String email = "",
                             dn = "",
                             attValue = "";

                            email = form_details.getApplicant_email().toString();
                            dn = fwdObj.fetchLDAPDN(email, formtype);
                            HashMap<String, String> HMZim = aa.fetchZimOtp(email);
                            String dexp = form_details.getSingle_dor();
                            String dob = form_details.getSingle_dob();
                            String fixedDor = form_details.getFixed_dor_for_retired();
                            String dexp1 = utcFormat(dexp);
                            String dob1 = utcFormat(dob);
                            String fixedDor1 = utcFormat(fixedDor);

                            HashMap<String, String> zim_values = new HashMap<String, String>();
                            zim_values.put("nicAccountExpDate", dexp1);
                            zim_values.put("nicDateOfRetirement", fixedDor1);
                            zim_values.put("nicDateOfBirth", dob1);
                            zim_values.put("mailuserstatus", "active");
                            zim_values.put("inetuserstatus", "active");
                            aa.modifyActivateAttribute(dn, zim_values, formtype);
                            System.out.println("Zim value for Retired Emp:" + zim_values);
                            //----------------------------------- End process of Updation in Ldap
                            //  UpdateStatusForRetiredEmp(ref_num, UserMobile, formtype, check, profile_values, category, ministry, department);
                            System.out.println(printlog + "status, comp auth, user check, is updated!!!!");
                            SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            break;
                        default:
                            if ((formtype.equals("single") || formtype.equals("bulk")) && (form_details.getReq_for().equals("eoffice"))) {
                                coorddata = new ArrayList<String>();
                                coordinators = "rachna_sri@nic.in";
                                coorddata.add(coordinators);
                                UpdateStatusSingle_bulk(ref_num, UserMobile, formtype, check, tableName, file, coordinators, profile_values, category, ministry, department);
                                System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                SendMailCO(formtype, ref_num, coorddata, UserEmail, profile_values, min_val, form_details);
                                SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                            } else {
                                if ((formtype.equals("single") || formtype.equals("bulk") || formtype.equals("nkn") || formtype.equals("gem")) && profile_values.get("user_employment").toString().toLowerCase().equalsIgnoreCase("state") && profile_values.get("stateCode").toString().toLowerCase().equals("himachal pradesh")) {
                                    ArrayList<String> Cadata = new ArrayList<String>();
                                    Cadata = fetchHimachalCoord(profile_values.get("dept").toString());

                                    if (Cadata.size() > 0) {
                                        if (Cadata.get(0).toString().equals("nodata")) {
                                            UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                            SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                            SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);

                                        } else {
                                            CaMobile = LdapQuery.GetMobile(Cadata.get(0));
                                            if (CaMobile.contains("error")) {
                                                CaMobile = "";
                                            }
                                            CaEmail = Cadata.get(0).toString();
                                            CaName = Cadata.get(1).toString();
                                            UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                            System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                            SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                            SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);

                                        }
                                    }

                                } else {
                                    UpdateStatus(CaName, CaMobile, CaEmail, ref_num, UserMobile, formtype, check, tableName, file, profile_values, category, ministry, department);
                                    System.out.println(printlog + "status, comp auth, user check, and filepath is updated!!!!");
                                    SendMailCA(formtype, ref_num, CaEmail, CaMobile, UserEmail, profile_values, min_val);
                                    SendMailUser(formtype, ref_num, UserEmail, UserMobile, form_details);
                                }
                                break;

                            }

                            if ((formtype.equals("single"))) {
                                if (form_details.getAction().equals("yes")) {

                                    SendMailRajp(formtype, ref_num, UserEmail, UserMobile, form_details);
                                }
                            }
                    }

                } else if (check.equals("esign_admin")) // else if added by pr on 12thapr18
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin in esign dao if check is esign_admin" + formtype + "regstration no " + ref_num);

                    consentreturn.put("checkreturn", check);
                    consentreturn.put("Ref_string", "Your form has been submitted and your Registration number is <span class='reg_no_sty'>" + ref_num + "</span>.");
                    GenerateWhichPDF(form_details, ref_num, formtype);
                }
                //}
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return consentreturn;
    }

    public Map<String, Object> consentRetired(String check, String ref_num, String formtype, FormBean form_details) {
        Map<String, Object> consentreturn = new HashMap<String, Object>();
        this.form_details = form_details;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String ref_exist = "select * from dor_ext_retired_registration where registration_no ='" + ref_num + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin in esign dao   " + ref_exist);
            stmt = conSlave.prepareStatement(ref_exist);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println(printlog + "if not found records in main table");
                consentreturn.put("checkreturn", "unsuccess");
            } else {
                consentreturn.put("checkreturn", check);
                consentreturn.put("Ref_string", "Your form has been submitted and your Registration number is <span class='reg_no_sty'>" + ref_num + "</span>.");
                if (check.equals("esign")) {
                    GenerateWhichPDF(form_details, ref_num, formtype);
                    updateBaseTableWithFilePath(ref_num);
                } else if (check.equals("esign_update")) {
                    System.out.println("Inside Dor_ext Retired Emp Esing Submission");
                    System.out.println("Login ip for Dor_ext of Retired Emp: " + ip);
                    //----------------------------------- start process of Updation in Ldap
                    ForwardAction fwdObj = new ForwardAction();
                    AdminAction aa = new AdminAction();

                    String email = "", dn = "";
                    email = form_details.getApplicant_email().toString();
                    dn = fwdObj.fetchLDAPDN(email, formtype);
                    String dexp = form_details.getSingle_dor();
                    //Ldap ldap = new Ldap();
                    //Map<String, Object> ldapValues = ldap.getDorDobDoEValuesForRetiredEmployees(email);
                    //String fixedDor = ldapValues.get("dor").toString();
                    String dexp1 = utcFormat(dexp);
                    System.out.println("Retired ::: Account exp date to be updated == "+dexp1);
                    //String dob = ldapValues.get("dob").toString();

                    HashMap<String, String> zim_values = new HashMap<String, String>();
                    zim_values.put("nicAccountExpDate", dexp1);
                    //zim_values.put("nicDateOfRetirement", fixedDor);
                    //zim_values.put("nicDateOfBirth", dob);
//                    zim_values.put("mailuserstatus", "active");
//                    zim_values.put("inetuserstatus", "active");
                    if (!aa.modifyAttributeForRetired(dn, zim_values, formtype)) {
                        consentreturn.put("checkreturn", check);
                        consentreturn.put("Ref_string", "Your form could not be submitted. Kindly drop a mail to <span class='reg_no_sty'>eforms@nic.in</span>.");
                    } else if (UpdateStatusForRetiredEmp(ref_num, form_details.getApplicant_mobile(), formtype, check, email, form_details.getApplicant_name())) {
                        System.out.println(printlog + "status, comp auth, user check, is updated!!!!");
                        SendMailUser(formtype, ref_num, email, form_details.getApplicant_mobile(), form_details);
                    } else {
                        consentreturn.put("checkreturn", check);
                        consentreturn.put("Ref_string", "Your form could not be submitted. Kindly drop a mail to <span class='reg_no_sty'>eforms@nic.in</span>.");
                    }
                    System.out.println("Zim value for Retired Emp:" + zim_values);
                    //----------------------------------- End process of Updation in Ldap
                } else if (check.equals("esign_admin")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin in esign dao if check is esign_admin" + formtype + "regstration no " + ref_num);
                    GenerateWhichPDF(form_details, ref_num, formtype);
                    updateBaseTableWithFilePath(ref_num);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    // con.close();
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
        }
        return consentreturn;
    }

    public ArrayList getWifiAdmin() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList admins = new ArrayList();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String sql_comp_qry = "select distinct m_email from mailadmin_forms where m_wifi='y'";
            stmt = conSlave.prepareStatement(sql_comp_qry);
            rs = stmt.executeQuery();
            while (rs.next()) {
                admins.add(rs.getString("m_email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
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
        }
        return admins;
    }

    public ArrayList fetchHimachalCoord(String dept) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList admins = new ArrayList();
        try {
            conSlave = DbConnection.getSlaveConnection();

            String sql_comp_qry = "select emp_coord_email,emp_coord_name from employment_coordinator where emp_coord_email!='kaushal.shailender@nic.in' and emp_admin_email='kaushal.shailender@nic.in' and emp_dept=?";
            stmt = conSlave.prepareStatement(sql_comp_qry);
            stmt.setString(1, dept);
            System.out.println(printlog + "statement query in fetchHimachalCoord:::::::" + stmt);
            rs = stmt.executeQuery();
            if (rs.next()) {
                admins.add(0, rs.getString("emp_coord_email"));
                admins.add(1, rs.getString("emp_coord_name"));
            } else {
                admins.add("nodata");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
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
        }
        return admins;
    }

    public void UpdateStatus(String CaName, String CaMobile, String CaEmail, String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, Map profile_values, String category, String ministry, String department) {

        String upload_work_order = "n";
        PreparedStatement stmt = null;
        ResultSet rs_comp = null;
        ResultSet rs = null, rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement ps22 = null;
        try {
            if (formtype.contains("email_act")) {
                System.out.println("form_details.getUploaded_filename" + form_details.getUploaded_filename());
                if ((form_details.getUploaded_filename() != null) && !form_details.getUploaded_filename().equals("") && !form_details.getSingle_emp_type().equals("emp_regular")) {
                    upload_work_order = "y";
                } else if (form_details.getSingle_emp_type().equals("emp_regular")) {
                    upload_work_order = "y";
                }

            }
//            Ldap ldap = new Ldap();
//            ProfileService profileService = new ProfileService();
//            if (ldap.emailValidate(CaEmail)) {
//                if (CaMobile.contains("X")) {
//                    HashMap hodDetails = (HashMap) profileService.getHODdetails(CaEmail);
//                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                    CaMobile = form_details.getHod_mobile();
//                }
//            }

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String sql_comp_qry = "select * from comp_auth where ca_email = ? and ca_mobile = ? ";
            stmt = conSlave.prepareStatement(sql_comp_qry);
            stmt.setString(1, CaEmail);
            stmt.setString(2, CaMobile);
            System.out.println(printlog + "stmt" + stmt);
            rs_comp = stmt.executeQuery();
            if (!rs_comp.next()) {
                String sql_comp = "INSERT INTO comp_auth (ca_name,ca_email,ca_mobile) values (?,?,?)";
                stmt = con.prepareStatement(sql_comp, stmt.RETURN_GENERATED_KEYS);
                stmt.setString(1, CaName);
                stmt.setString(2, CaEmail);
                stmt.setString(3, CaMobile);
                System.out.println(printlog + "insert 2nd query " + stmt);
                int i = stmt.executeUpdate();
                rs1 = stmt.getGeneratedKeys();
                try {
                    if (rs1.next()) {
                        int id = rs1.getInt(1);
                        System.out.println(printlog + id);
                        stmt = null;
                        if ("manual_upload".equals(check)) {
                            String to_user = profile_values.get("email").toString();
                            String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_to_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                            stmt = con.prepareStatement(sql);
                            stmt.setString(1, ref_num);
                            if (formtype.equals("distributionlist")) {
                                stmt.setString(2, "dlist");
                            } else if (formtype.equals("bulkdistributionlist")) {
                                stmt.setString(2, "bulkdlist");
                            } else {
                                stmt.setString(2, formtype);
                            }
                            // stmt.setString(3, "ca");
                            // stmt.setInt(4, id);
                            stmt.setString(3, check);
                            stmt.setString(4, profile_values.get("email").toString());
                            stmt.setString(5, profile_values.get("cn").toString());
                            stmt.setString(6, profile_values.get("mobile").toString());
                            stmt.setString(7, ip);
                            stmt.setString(8, pdate);
                            stmt.setString(9, "a");
                            stmt.setString(10, "a");
                            stmt.setString(11, profile_values.get("email").toString());
                            stmt.setString(12, profile_values.get("email").toString());
                            System.out.println(printlog + "3rd query of status " + stmt);
                            int j = stmt.executeUpdate();
                            System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                            if (j > 0) {
                                String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,upload_work_order,category, min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                stmt = con.prepareStatement(sql_final_track);
                                stmt.setString(1, ref_num);
                                stmt.setString(2, profile_values.get("email").toString());
                                stmt.setString(3, profile_values.get("mobile").toString());
                                stmt.setString(4, profile_values.get("cn").toString());
                                stmt.setString(5, ip);
                                stmt.setString(6, pdate);
                                stmt.setString(7, check);
                                if (formtype.equals("distributionlist")) {
                                    stmt.setString(8, "dlist");
                                } else if (formtype.equals("bulkdistributionlist")) {
                                    stmt.setString(8, "bulkdlist");
                                } else {
                                    stmt.setString(8, formtype);
                                }
                                stmt.setString(9, to_user);
                                stmt.setString(10, profile_values.get("mobile").toString());
                                stmt.setString(11, profile_values.get("cn").toString());
                                stmt.setString(12, pdate);
                                stmt.setString(13, check);
                                if (formtype.contains("vpn")) {
                                    if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                                        stmt.setString(14, form_details.getVpn_reg_no());
                                    } else {
                                        stmt.setString(14, "");
                                    }
                                } else {
                                    stmt.setString(14, "");
                                }
                                if (upload_work_order.equals("y")) {
                                    stmt.setString(15, "y");
                                } else {
                                    stmt.setString(15, "n");
                                }
                                stmt.setString(16, category);
                                stmt.setString(17, ministry);
                                stmt.setString(18, department);
                                System.out.println(printlog + "3rd query of final_audit_track " + stmt);
                                int k = stmt.executeUpdate();
                                updateCoIdOrDaId(ref_num, to_user, check);
                                System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                            }
                        } else {

                            String status = "", to = "", to_email = "";
//                            if (formtype.equals("mobile") && check.equals("esign_update")) {
//                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside form is mobile and esign update ");
//
//                                status = "completed";
//                                CaMobile = "";
//                                CaName = "";
//
//                            } else {
                            status = "ca_pending";
                            to = "ca";
                            to_email = CaEmail;
                            //}
                            String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?)";
                            stmt = con.prepareStatement(sql);
                            stmt.setString(1, ref_num);
                            if (formtype.equals("distributionlist")) {
                                stmt.setString(2, "dlist");
                            } else if (formtype.equals("bulkdistributionlist")) {
                                stmt.setString(2, "bulkdlist");
                            } else {
                                stmt.setString(2, formtype);
                            }
                            stmt.setString(3, to);
                            stmt.setString(4, to_email);
                            //stmt.setInt(4, id);
                            stmt.setString(5, profile_values.get("email").toString());
                            stmt.setString(6, profile_values.get("mobile").toString());
                            stmt.setString(7, profile_values.get("cn").toString());
                            stmt.setString(8, ip);
                            stmt.setString(9, pdate);
                            stmt.setString(10, "a");
                            stmt.setString(11, profile_values.get("email").toString());
                            System.out.println(printlog + "3rd query of status " + stmt);
                            int j = stmt.executeUpdate();
                            System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CA_PENDING: " + j);
                            if (j > 0) {
                                String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,upload_work_order, category, min_state_org, department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                stmt = con.prepareStatement(sql_final_track);
                                stmt.setString(1, ref_num);
                                stmt.setString(2, profile_values.get("email").toString());
                                stmt.setString(3, profile_values.get("mobile").toString());
                                stmt.setString(4, profile_values.get("cn").toString());
                                stmt.setString(5, ip);
                                stmt.setString(6, pdate);
                                stmt.setString(7, status);
                                if (formtype.equals("distributionlist")) {
                                    stmt.setString(8, "dlist");
                                } else if (formtype.equals("bulkdistributionlist")) {
                                    stmt.setString(8, "bulkdlist");
                                } else {
                                    stmt.setString(8, formtype);
                                }
                                stmt.setString(9, to_email);
                                stmt.setString(10, CaMobile);
                                stmt.setString(11, CaName);
                                stmt.setString(12, pdate);
                                stmt.setString(13, check);
                                if (formtype.contains("vpn")) {
                                    if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                                        stmt.setString(14, form_details.getVpn_reg_no());
                                    } else {
                                        stmt.setString(14, "");
                                    }
                                } else {
                                    stmt.setString(14, "");
                                }
                                if (upload_work_order.equals("y")) {
                                    stmt.setString(15, "y");
                                } else {
                                    stmt.setString(15, "n");
                                }
                                stmt.setString(16, category);
                                stmt.setString(17, ministry);
                                stmt.setString(18, department);
                                System.out.println(printlog + "3rd query of final_audit_track " + stmt);
                                int k = stmt.executeUpdate();
                                updateCoIdOrDaId(ref_num, to_email, status);
                                System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CA_PENDING: " + k);
                            }

                        }
                        //stmt.close();
                    }
                } catch (Exception ex) {
                    System.out.println("Inside eSignDAO 3rd query final audit track exception " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    try {
                        if (rs1 != null) {
                            rs1.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //rs1.close();
            } else {
                stmt = null;

                try {
                    if ("manual_upload".equals(check)) {
                        String to_user = profile_values.get("email").toString();
                        String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by) values (?,?,?,?,?,?,?,?,?)";
                        //String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type ) values (?,?,?)";
                        stmt = con.prepareStatement(sql);
                        stmt.setString(1, ref_num);
                        if (formtype.equals("distributionlist")) {
                            stmt.setString(2, "dlist");
                        } else if (formtype.equals("bulkdistributionlist")) {
                            stmt.setString(2, "bulkdlist");
                        } else {
                            stmt.setString(2, formtype);
                        }
                        stmt.setString(3, check);
                        stmt.setString(4, profile_values.get("email").toString());
                        stmt.setString(5, profile_values.get("mobile").toString());
                        stmt.setString(6, profile_values.get("cn").toString());
                        stmt.setString(7, ip);
                        stmt.setString(8, pdate);
                        stmt.setString(9, "a");
                        System.out.println(printlog + "3rd query of status " + stmt);
                        int i = stmt.executeUpdate();
                        System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + i);
                        if (i > 0) {
                            String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,upload_work_order,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            stmt = con.prepareStatement(sql_final_track);
                            stmt.setString(1, ref_num);
                            stmt.setString(2, profile_values.get("email").toString());
                            stmt.setString(3, profile_values.get("mobile").toString());
                            stmt.setString(4, profile_values.get("cn").toString());
                            stmt.setString(5, ip);
                            stmt.setString(6, pdate);
                            stmt.setString(7, check);
                            if (formtype.equals("distributionlist")) {
                                stmt.setString(8, "dlist");
                            } else if (formtype.equals("bulkdistributionlist")) {
                                stmt.setString(8, "bulkdlist");
                            } else {
                                stmt.setString(8, formtype);
                            }
                            stmt.setString(9, to_user);
                            stmt.setString(10, profile_values.get("mobile").toString());
                            stmt.setString(11, profile_values.get("cn").toString());
                            stmt.setString(12, pdate);
                            stmt.setString(13, check);
                            if (formtype.contains("vpn")) {
                                if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                                    stmt.setString(14, form_details.getVpn_reg_no());
                                } else {
                                    stmt.setString(14, "");
                                }
                            } else {
                                stmt.setString(14, "");
                            }
                            if (upload_work_order.equals("y")) {
                                stmt.setString(15, "y");
                            } else {
                                stmt.setString(15, "n");
                            }
                            stmt.setString(16, category);
                            stmt.setString(17, ministry);
                            stmt.setString(18, department);
                            System.out.println(printlog + "3rd query of final_audit_track " + stmt);
                            int k = stmt.executeUpdate();
                            updateCoIdOrDaId(ref_num, to_user, check);
                            System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                        }
                    } else {
                        String status = "", to = "", to_email = "";
//                        if (formtype.equals("mobile") && check.equals("esign_update")) {
//                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside form is mobile and esign update ");
//
//                            status = "completed";
//                            CaMobile = "";
//                            CaName = "";
//
//                        } else {
                        status = "ca_pending";
                        to = "ca";
                        to_email = CaEmail;
                        // }
                        String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by ) values (?,?,?,?,?,?,?,?,?,?)";
                        stmt = con.prepareStatement(sql);
                        stmt.setString(1, ref_num);
                        stmt.setString(2, formtype);
                        if (formtype.equals("distributionlist")) {
                            stmt.setString(2, "dlist");
                        } else if (formtype.equals("bulkdistributionlist")) {
                            stmt.setString(2, "bulkdlist");
                        } else {
                            stmt.setString(2, formtype);
                        }
                        stmt.setString(3, to);
                        stmt.setString(4, to_email);
                        //stmt.setInt(4, rs_comp.getInt("ca_id"));
                        stmt.setString(5, profile_values.get("email").toString());
                        stmt.setString(6, profile_values.get("mobile").toString());
                        stmt.setString(7, profile_values.get("cn").toString());
                        stmt.setString(8, ip);
                        stmt.setString(9, pdate);
                        stmt.setString(10, "a");
                        System.out.println(printlog + "3rd query of status " + stmt);
                        int i = stmt.executeUpdate();
                        System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CA_PENDING: " + i);
                        if (i > 0) {
                            String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,upload_work_order,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            stmt = con.prepareStatement(sql_final_track);
                            stmt.setString(1, ref_num);
                            stmt.setString(2, profile_values.get("email").toString());
                            stmt.setString(3, profile_values.get("mobile").toString());
                            stmt.setString(4, profile_values.get("cn").toString());
                            stmt.setString(5, ip);
                            stmt.setString(6, pdate);
                            stmt.setString(7, status);
                            if (formtype.equals("distributionlist")) {
                                stmt.setString(8, "dlist");
                            } else if (formtype.equals("bulkdistributionlist")) {
                                stmt.setString(8, "bulkdlist");
                            } else {
                                stmt.setString(8, formtype);
                            }
                            stmt.setString(9, to_email);
                            stmt.setString(10, CaMobile);
                            stmt.setString(11, CaName);
                            stmt.setString(12, pdate);
                            stmt.setString(13, check);
                            if (formtype.contains("vpn")) {
                                if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                                    stmt.setString(14, form_details.getVpn_reg_no());
                                } else {
                                    stmt.setString(14, "");
                                }
                            } else {
                                stmt.setString(14, "");
                            }
                            if (upload_work_order.equals("y")) {
                                stmt.setString(15, "y");
                            } else {
                                stmt.setString(15, "n");
                            }
                            stmt.setString(16, category);
                            stmt.setString(17, ministry);
                            stmt.setString(18, department);
                            System.out.println(printlog + "3rd query of final_audit_track " + stmt);
                            int k = stmt.executeUpdate();
                            updateCoIdOrDaId(ref_num, to_email, status);
                            System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CA_PENDING: " + k);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Inside eSignDAO 3rd query final audit track exception " + e.getMessage());
                    e.printStackTrace();
                }

            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";
            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            String sql1 = null;
            sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
            ps22 = con.prepareStatement(sql1);
            ps22.setString(1, filePath);
            ps22.setString(2, ref_num);
            System.out.println(printlog + "PST:: " + ps22);
            int k = ps22.executeUpdate();
            System.out.println(printlog + "FILE PATH UPDATED: " + k);
            //code modified by MI on 12th APR 18
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, filePath);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int i = ps.executeUpdate();
            //rs.close();
            //  update file path
        } catch (Exception e) {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs_comp != null) {
                    rs_comp.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    // con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (ps22 != null) {
                    ps22.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateCoIdOrDaId(String registration_no, String to_email, String status) {
        //Added by AT on 6th June 2022
        if (status.equals("coordinator_pending") && to_email.contains(",")) {
            String[] emailAddresses = to_email.split(",");
            Set<String> alias = null, id = null;
            List<Set<String>> ids = new ArrayList<>();
            for (String emailAdrs : emailAddresses) {
                alias = ldap.fetchAliases(emailAdrs);
                id = db.fetchCoIds(new ArrayList<>(alias));
                ids.add(id);
            }

            Set<String> commonIdIntersection = db.findIntersection(ids);
            String commaSeperatedString = "";
            for (String str : commonIdIntersection) {
                commaSeperatedString = str;
                break;
            }

            //commaSeperatedString = commaSeperatedString.replaceAll("\\s*,\\s*$", "");
            String newIdForCoord = "";
            if (!commaSeperatedString.isEmpty()) {
                //insert query here for final_audit_track table
                db.updateFinalAuditTrack(registration_no, commaSeperatedString);
            } else {
                //insert query here for coordinator_id table
                newIdForCoord = db.insertIntoCoordsIdTable(to_email);
                if (newIdForCoord.isEmpty()) {
                    db.updateFinalAuditTrack(registration_no, "0");
                } else {
                    db.updateFinalAuditTrack(registration_no, newIdForCoord);
                }
            }
        } else if (status.equals("da_pending") && to_email.contains(",")) {
            String[] emailAddresses = to_email.split(",");
            Set<String> alias = null, id = null;
            List<Set<String>> ids = new ArrayList<>();
            for (String emailAdrs : emailAddresses) {
                alias = ldap.fetchAliases(emailAdrs);
                id = db.fetchDaIds(new ArrayList<>(alias));
                ids.add(id);
            }

            Set<String> commonIdIntersection = db.findIntersection(ids);
            String commaSeperatedString = "";
            for (String str : commonIdIntersection) {
                commaSeperatedString = str;
                break;
            }

            //commaSeperatedString = commaSeperatedString.replaceAll("\\s*,\\s*$", "");
            String newIdForCoord = "";
            if (!commaSeperatedString.isEmpty()) {
                //insert query here for final_audit_track table
                db.updateFinalAuditTrackForDa(registration_no, commaSeperatedString);
            } else {
                //insert query here for coordinator_id table
                newIdForCoord = db.insertIntoDaIdTable(to_email);
                if (newIdForCoord.isEmpty()) {
                    db.updateFinalAuditTrackForDa(registration_no, "0");
                } else {
                    db.updateFinalAuditTrackForDa(registration_no, newIdForCoord);
                }
            }
        }
        //Ended by AT on 6th June 2022
    }

    public void UpdateStatusWifi(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, String coordinators, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            if ("manual_upload".equals(check)) {
                String to_user = profile_values.get("email").toString();
                //String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type ) values (?,?,?)";
                String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_to_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ref_num);
                stmt.setString(2, formtype);
                stmt.setString(3, check);
                stmt.setString(4, profile_values.get("email").toString());
                stmt.setString(5, profile_values.get("mobile").toString());
                stmt.setString(6, profile_values.get("cn").toString());
                stmt.setString(7, ip);
                stmt.setString(8, pdate);
                stmt.setString(9, "a");
                stmt.setString(10, "a");
                stmt.setString(11, profile_values.get("email").toString());
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, check);
                    stmt.setString(8, formtype);
                    stmt.setString(9, to_user);
                    stmt.setString(10, profile_values.get("mobile").toString());
                    stmt.setString(11, profile_values.get("cn").toString());
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);

                    if (formtype.contains("vpn")) {
                        if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                            stmt.setString(14, form_details.getVpn_reg_no());
                        } else {
                            stmt.setString(14, "");
                        }
                    } else {
                        stmt.setString(14, "");
                    }
                    stmt.setString(15, category);
                    stmt.setString(16, ministry);
                    stmt.setString(17, department);
                    System.out.println(printlog + "3rd query of final_audit_track " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_user, check);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                }
            } else {
                String status = "ca_pending";
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user ) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, "ca");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
//                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);
                if (j > 0) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "check value insert query1");
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, coordinators);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    if (formtype.contains("vpn")) {
                        if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                            stmt.setString(14, form_details.getVpn_reg_no());
                        } else {
                            stmt.setString(14, "");
                        }
                    } else {
                        stmt.setString(14, "");
                    }
                    stmt.setString(15, category);
                    stmt.setString(16, ministry);
                    stmt.setString(17, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, coordinators, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);
                }
            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";

            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            PreparedStatement ps = null;
            stmt = null;
            String sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
            stmt = con.prepareStatement(sql1);
            stmt.setString(1, filePath);
            stmt.setString(2, ref_num);
            int k = stmt.executeUpdate();
            System.out.println(printlog + "FILE PATH UPDATED: " + k);
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, filePath);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void UpdateStatusBulk(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            if ("manual_upload".equals(check)) {
                String to_user = "";
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, check);
                stmt.setString(4, "s");
                if (formtype.equalsIgnoreCase("wifi")) {
                    stmt.setString(5, "support@nkn.in");
                } else {
                    stmt.setString(5, "support@nic.in");
                }
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, check);
                    stmt.setString(8, formtype);
                    if (formtype.equalsIgnoreCase("wifi")) {
                        stmt.setString(9, "support@nkn.in");
                        to_user = "support@nkn.in";
                    } else {
                        stmt.setString(9, "support@nic.in");
                        to_user = "support@nic.in";
                    }
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_user, check);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                }
            } else {
                String status = "mail-admin_pending";
                String to_email = "";
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, "s");
                if (formtype.equalsIgnoreCase("wifi")) {
                    stmt.setString(5, "support@nkn.in");
                } else {
                    stmt.setString(5, "support@nic.in");
                }
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    if (formtype.equalsIgnoreCase("wifi")) {
                        stmt.setString(9, "support@nkn.in");
                        to_email = "support@nkn.in";
                    } else {
                        stmt.setString(9, "support@nic.in");
                        to_email = "support@nic.in";
                    }
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_email, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);
                }
            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";

            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            stmt = null;
            try {
                //  update file path
                String sql1 = null;
                sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
                ps22 = con.prepareStatement(sql1);
                ps22.setString(1, filePath);
                ps22.setString(2, ref_num);
                System.out.println(printlog + "PST:: " + ps22);
                int k = ps22.executeUpdate();
                System.out.println(printlog + "FILE PATH UPDATED: " + k);
                String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
                ps = con.prepareStatement(qryStr);
                ps.setString(1, formtype);
                ps.setString(2, ref_num);
                ps.setString(3, check);
                ps.setString(4, filePath);
                //end here, code modified by MI on 12th APR 18
                int i = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ps22 != null) {
                    try {
                        ps22.close();
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
            }
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void UpdateStatusImap(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, String coordinators, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        String status = "mail-admin_pending";
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            if ("manual_upload".equals(check)) {
                String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ref_num);
                stmt.setString(2, formtype);
                stmt.setString(3, status);
                stmt.setString(4, "m");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type, category, min_state_org, department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, coordinators);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, coordinators, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);

                }

            } else {
                String to = "", to_email = "";
//                if (formtype.equals("mobile") && check.equals("esign_update")) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside form is mobile and esign update ");
//                    status = "completed";
//
//                } else {
                //status = "mail-admin_pending";
                to = "m";
                to_email = coordinators;
                // }
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, to);
                stmt.setString(5, to_email);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());

                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, to_email);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_email, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);
                }

            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";

            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            stmt = null;
            //rs.close();
            //  update file path
            String sql1 = null;
            sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
            ps22 = con.prepareStatement(sql1);
            ps22.setString(1, filePath);
            ps22.setString(2, ref_num);
            System.out.println(printlog + "PST:: " + ps22);
            int k = ps22.executeUpdate();
            System.out.println(printlog + "FILE PATH UPDATED: " + k);
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, filePath);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps22 != null) {
                try {
                    ps22.close();
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
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void UpdateStatusWifoport(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, String coordinators, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        String status = "coordinator_pending";
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            if ("manual_upload".equals(check)) {
                String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ref_num);
                stmt.setString(2, formtype);
                stmt.setString(3, status);
                stmt.setString(4, "c");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type, category, min_state_org, department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, coordinators);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, coordinators, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                }

            } else {
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, "c");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());

                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, coordinators);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, coordinators, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);
                }

            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";

            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            stmt = null;
            //rs.close();
            //  update file path
            String sql1 = null;
            sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
            ps22 = con.prepareStatement(sql1);
            ps22.setString(1, filePath);
            ps22.setString(2, ref_num);
            System.out.println(printlog + "PST:: " + ps22);
            int k = ps22.executeUpdate();
            System.out.println(printlog + "FILE PATH UPDATED: " + k);
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, filePath);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps22 != null) {
                try {
                    ps22.close();
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
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void UpdateStatusManual(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, String coordinators, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            if ("manual_upload".equals(check)) {
                String to_user = profile_values.get("email").toString();
                String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_to_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ref_num);
                stmt.setString(2, formtype);
                stmt.setString(3, check);
                stmt.setString(4, profile_values.get("email").toString());
                stmt.setString(5, profile_values.get("mobile").toString());
                stmt.setString(6, profile_values.get("cn").toString());
                stmt.setString(7, ip);
                stmt.setString(8, pdate);
                stmt.setString(9, "a");
                stmt.setString(10, "a");
                stmt.setString(11, profile_values.get("email").toString());
                stmt.setString(12, to_user);

                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, check);
                    stmt.setString(8, formtype);
                    stmt.setString(9, to_user);
                    stmt.setString(10, profile_values.get("mobile").toString());
                    stmt.setString(11, profile_values.get("cn").toString());
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);

                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_user, check);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                }
            } else {
                String status = "mail-admin_pending";
                String to_user = profile_values.get("email").toString();
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user ) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, "m");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, to_user);
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,vpn_reg_no,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, to_user);
                    stmt.setString(10, profile_values.get("mobile").toString());
                    stmt.setString(11, profile_values.get("cn").toString());
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    if (formtype.contains("vpn")) {
                        if (form_details.getVpn_reg_no() != null && !form_details.getVpn_reg_no().isEmpty()) {
                            stmt.setString(14, form_details.getVpn_reg_no());
                        } else {
                            stmt.setString(14, "");
                        }
                    } else {
                        stmt.setString(14, "");
                    }
                    stmt.setString(15, category);
                    stmt.setString(16, ministry);
                    stmt.setString(17, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_user, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);
                }
            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";

            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            stmt = null;
            //  update file path
            String sql1 = null;
            sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
            ps22 = con.prepareStatement(sql1);
            ps22.setString(1, filePath);
            ps22.setString(2, ref_num);
            System.out.println(printlog + "PST:: " + ps22);
            int k = ps22.executeUpdate();
            System.out.println(printlog + "FILE PATH UPDATED: " + k);
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, filePath);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps22 != null) {
                try {
                    ps22.close();
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
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void UpdateStatusSingle_bulk(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, String coordinators, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            if ("manual_upload".equals(check)) {
                String to_user = profile_values.get("email").toString();
                String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_to_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ref_num);
                stmt.setString(2, formtype);
                stmt.setString(3, check);
                stmt.setString(4, profile_values.get("email").toString());
                stmt.setString(5, profile_values.get("mobile").toString());
                stmt.setString(6, profile_values.get("cn").toString());
                stmt.setString(7, ip);
                stmt.setString(8, pdate);
                stmt.setString(9, "a");
                stmt.setString(10, "a");
                stmt.setString(11, profile_values.get("email").toString());
                stmt.setString(12, to_user);

                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, check);
                    stmt.setString(8, formtype);
                    stmt.setString(9, to_user);
                    stmt.setString(10, profile_values.get("mobile").toString());
                    stmt.setString(11, profile_values.get("cn").toString());
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_user, check);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                }
            } else {
                String status = "coordinator_pending";
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, "c");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                stmt.setString(12, profile_values.get("email").toString());
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, coordinators);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, coordinators, status);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);
                }
            }
            //stmt.close();
            if (formtype.equals("ip")) {
                formtype = "changeip";
            } else if (formtype.equals("nkn_single") || formtype.equals("nkn_bulk")) {
                formtype = "nkn";

            } else if (formtype.equals("mobile")) {
                formtype = "mobile_update";
            }
            //  update file path
            String sql1 = null;
            sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
            ps22 = con.prepareStatement(sql1);
            ps22.setString(1, filePath);
            ps22.setString(2, ref_num);
            System.out.println(printlog + "PST:: " + ps22);
            int k = ps22.executeUpdate();
            System.out.println(printlog + "FILE PATH UPDATED: " + k);
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, filePath);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps22 != null) {
                try {
                    ps22.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void UpdateStatusFirewall(String ref_num, String UserMobile, String formtype, String check, String tableName, String filePath, String coordinators, Map profile_values, String category, String ministry, String department) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            if ("manual_upload".equals(check)) {
                String to_email = profile_values.get("email").toString();
                String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by) values (?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, ref_num);
                stmt.setString(2, formtype);
                stmt.setString(3, check);
                stmt.setString(4, to_email);
                stmt.setString(5, profile_values.get("mobile").toString());
                stmt.setString(6, profile_values.get("cn").toString());
                stmt.setString(7, ip);
                stmt.setString(8, pdate);
                stmt.setString(9, "a");
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: " + j);
                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, check);
                    stmt.setString(8, formtype);
                    stmt.setString(9, to_email);
                    stmt.setString(10, profile_values.get("mobile").toString());
                    stmt.setString(11, profile_values.get("cn").toString());
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, to_email, check);
                    System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
                }
            } else {
                String status = "coordinator_pending";
                String sql = "INSERT INTO status (stat_form_type,stat_reg_no,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by) values (?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql);
                stmt.setString(1, formtype);
                stmt.setString(2, ref_num);
                stmt.setString(3, status);
                stmt.setString(4, "c");
                stmt.setString(5, coordinators);
                stmt.setString(6, profile_values.get("email").toString());
                stmt.setString(7, profile_values.get("mobile").toString());
                stmt.setString(8, profile_values.get("cn").toString());
                stmt.setString(9, ip);
                stmt.setString(10, pdate);
                stmt.setString(11, "a");
                System.out.println(printlog + "3rd query of status " + stmt);
                int j = stmt.executeUpdate();
                System.out.println(printlog + " == VALUE INSERTED IN STATUS TABLE FOR CO_PENDING: " + j);

                if (j > 0) {
                    String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type,category,min_state_org,department) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    stmt = this.con.prepareStatement(sql_final_track);
                    stmt.setString(1, ref_num);
                    stmt.setString(2, profile_values.get("email").toString());
                    stmt.setString(3, profile_values.get("mobile").toString());
                    stmt.setString(4, profile_values.get("cn").toString());
                    stmt.setString(5, ip);
                    stmt.setString(6, pdate);
                    stmt.setString(7, status);
                    stmt.setString(8, formtype);
                    stmt.setString(9, coordinators);
                    stmt.setString(10, "");
                    stmt.setString(11, "");
                    stmt.setString(12, pdate);
                    stmt.setString(13, check);
                    stmt.setString(14, category);
                    stmt.setString(15, ministry);
                    stmt.setString(16, department);
                    System.out.println(printlog + "3rd query of status " + stmt);
                    int k = stmt.executeUpdate();
                    updateCoIdOrDaId(ref_num, coordinators, status);
                    System.out.println(printlog + " == VALUE INSERTED IN final_audit_track TABLE FOR CO_PENDING: " + k);

                    Ldap ldap = new Ldap();
                    Database db = new Database();
                    if (coordinators.contains(",")) {
                        String[] emailAddresses = coordinators.split(",");
                        Set<String> alias = null, id = null;
                        List<Set<String>> ids = new ArrayList<>();
                        for (String emailAdrs : emailAddresses) {
                            alias = ldap.fetchAliases(emailAdrs);
                            id = db.fetchCoIds(new ArrayList<>(alias));
                            ids.add(id);
                        }

                        Set<String> commonIdIntersection = db.findIntersection(ids);
                        String commaSeperatedString = "";
                        for (String str : commonIdIntersection) {
                            commaSeperatedString = str;
                            break;
                        }

                        String newIdForCoord = "";
                        if (!commaSeperatedString.isEmpty()) {
                            //insert query here for final_audit_track table
                            db.updateFinalAuditTrack(ref_num, commaSeperatedString);
                        } else {
                            //insert query here for coordinator_id table
                            newIdForCoord = db.insertIntoCoordsIdTable(coordinators);
                            if (newIdForCoord.isEmpty()) {
                                db.updateFinalAuditTrack(ref_num, "0");
                            } else {
                                db.updateFinalAuditTrack(ref_num, newIdForCoord);
                            }
                        }
                    }
                }
            }

            ps22 = null;
            ps = null;

            try {

                String sql1 = "update " + tableName + " set pdf_path=? where registration_no=?";
                ps22 = this.con.prepareStatement(sql1);
                ps22.setString(1, filePath);
                ps22.setString(2, ref_num);
                System.out.println(printlog + " PST:: " + ps22);
                int k = ps22.executeUpdate();
                System.out.println(printlog + " FILE PATH UPDATED: " + k);

                String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
                ps = this.con.prepareStatement(qryStr);
                ps.setString(1, formtype);
                ps.setString(2, ref_num);
                ps.setString(3, check);
                ps.setString(4, filePath);
                System.out.println(printlog + "PS: " + ps);

                int i = ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (ps22 != null) {
                    try {
                        ps22.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void SendMailRajp(String formtype, String ref_num, String UserEmail, String UserMobile, FormBean form_details) {
        String form_name = "";
        if (formtype.equals("bulk")) {
            form_name = "Bulk User Email";
        } else if (formtype.equals("single")) {
            form_name = "Single User Email";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear Sir/Madam,</p>");
        sb.append("<p>As this is a request for a new domain and if you think that this is valid request, so you are requested to create a new Business Organization (BO) and allow this domain (DOMAIN). </p>");
        sb.append("<p><b>Once this domain is allowed, ID could be created.</b></p>");
        sb.append("<p>Regards,</p>");
        sb.append("eForms Team NIC");
        System.out.println(printlog + "SendMailRajp :: " + sb);
        HashMap<String, Object> map = new HashMap<>();
        map.put("from", "");
        map.put("subject", ref_num + " Submitted");
        map.put("mailbody", sb.toString());
        map.put("to", "rajp@nic.in");
        map.put("cc", null);
        map.put("attachmentpath", null);
        map.put("mobile", UserMobile);
        map.put("smsbody", "");
        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
        object.notify(map);
    }

    public void SendCompleteMail(String formtype, String ref_num, String UserEmail, String UserMobile, FormBean form_details) {

        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear Sir/Madam,</p>");
        sb.append("<p>Your application with Registration number -" + ref_num + " on eForms has been completed</p>");
        sb.append("<p>Regards,</p>");
        sb.append("eForms Team NIC");
        System.out.println(printlog + "SendMailUser :: " + sb);
        HashMap<String, Object> map = new HashMap<>();
        map.put("from", "");
        map.put("subject", ref_num + " Submitted");
        map.put("mailbody", sb.toString());
        map.put("to", UserEmail);
        map.put("cc", null);
        map.put("attachmentpath", null);
        map.put("mobile", UserMobile);

        map.put("smsbody", "Dear Sir/Madam , An application with Registration number - " + ref_num + "  on eForms has been completed. NICSI");
        map.put("templateId", "1107160811935140690");
        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
        object.notify(map);
    }

    public void SendMailUser(String formtype, String ref_num, String UserEmail, String UserMobile, FormBean form_details) {
        System.out.println("Formtype: " + formtype + "ref_num: " + ref_num + "UserEmail: " + UserEmail + "UserMobile: " + UserMobile);
        System.out.println("form_details in SendMailUser method: " + form_details);
        String form_name = "";
        String wifi_process = "";
        int wifi_bo = 0;
        if (formtype.equals("imappop")) {
            form_name = "IMAP/POP";
        } else if (formtype.equals("bulk")) {
            form_name = "Bulk User Email";
        } else if (formtype.equals("ip")) {
            form_name = "Change/Add IP";
        } else if (formtype.equals("distributionlist")) {
            form_name = "Distribution List";
        } else if (formtype.equals("bulkdistributionlist")) {
            form_name = "Distribution List";                     // Added by Rahul jan 2021
        } else if (formtype.equals("ldap")) {
            form_name = "LDAP Authentication";
        } else if (formtype.equals("nkn_single")) {
            form_name = "NKN User Email";
        } else if (formtype.equals("nkn_bulk")) {
            form_name = "NKN Bulk User Email";
        } else if (formtype.equals("relay")) {
            form_name = "Smtp/Relay";
        } else if (formtype.equals("sms")) {
            form_name = "NIC-SMS Gateway";
        } else if (formtype.equals("single")) {
            form_name = "Single User Email";
        } else if (formtype.equals("gem")) {
            form_name = "Gem User";
        } else if (formtype.equals("mobile")) {
            form_name = "Mobile Update";
        } else if (formtype.equals("wifi")) {
            form_name = "WIFI";
            wifi_process = form_details.getWifi_process();
            wifi_bo = form_details.getWifi_bo();
        } else if (formtype.equals("dns")) {
            form_name = "DNS";
        } else if (formtype.equals("vpn_single") || formtype.equals("change_add")) {
            form_name = "VPN Single User";
        } else if (formtype.equals("vpn_bulk")) {
            form_name = "VPN Bulk User";
        } else if (formtype.equals("vpn_renew")) { // NITIN ADDED
            form_name = "VPN Renew";
        } else if (formtype.equals("webcast")) {
            form_name = "WEBCAST";
        } else if (formtype.equals("vpn_surrender")) { // NITIN ADDED	
            form_name = "VPN Surrender";
        } else if (formtype.equals("vpn_delete")) { // NITIN ADDED	
            form_name = "VPN Delete";
        } else if (formtype.equals("dor_ext")) {
            form_name = "Account expiry extension";
        } else if (formtype.equals("dor_ext_retired")) {
            form_name = "Account expiry extension";
        }
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> map = new HashMap<>();
        System.out.println("Formtype: " + formtype);

        if (formtype.equals("dor_ext_retired")) {

            sb.append("<p>Dear Sir/Madam,</p>");
            sb.append("<p>Validity of your email account has been extended till " + form_details.getSingle_dor() + " successfully against a reference number " + ref_num + "</p>");
            sb.append("<p>Please keep this reference number for any futhur assistance.</p>");
            sb.append("For any assistance, please contact on <b>1800-111-555</b> or raise a ticket at https://servicedesk.nic.in</br>");
            sb.append("<p>Regards,</p>");
            sb.append("eForms Team NIC");
            System.out.println(printlog + "SendMailUser retired :: " + sb);

            map.put("from", "");
            map.put("subject", "Account validity extended (" + ref_num + ")");
            map.put("mailbody", sb.toString());
            map.put("to", UserEmail);
            map.put("cc", null);
            map.put("attachmentpath", null);
            map.put("mobile", UserMobile);
            map.put("smsbody", "Dear Sir/Madam, Validity of your email account (" + UserEmail + ") has been extended till " + form_details.getSingle_dor() + " against reference number " + ref_num + ". -- Regards, eForms Team, NIC");
            map.put("templateId", "1107166210188401585");
            System.out.println("Value of Map for retired Emp: " + map);
        } else {
            sb.append("<p>Dear Sir/Madam,</p>");
            sb.append("<p>Your request has been successfully submitted and forwarded for approval to your Reporting Officer. </p>");
            sb.append("<p><b>Once approved by Reporting Officer, the request will move to NIC Co-ordinator/DA. And finally, after approval from NIC Co-ordinator/DA the request will be forwarded to Admins for necessary action.</b></p>");
            sb.append("<p><b>Please Note: To process your request on priority, Please share your documents for verification purpose to the coordinator, So that it could be processed on priority.</b></p>");
            sb.append("<p>You can track the status of your request at any point using this reference number <b>").append(ref_num).append("</b> using the link https://eforms.nic.in/ </p>");
            sb.append("<p>Please keep this reference number for any futhur action needed.</p>");
            if (wifi_process.equals("certificate")) {
                sb.append("Since, you have requested for Wi-Fi Certificate so it will take some time for creation.\n"
                        + "Thanks for your patience.");
            } else if ((wifi_process.equals("request")) && (wifi_bo == 2) && (form_details.getWifi_os1().equals("ios") || form_details.getWifi_os1().equals("apple") || (form_details.getWifi_os2().equals("ios") || form_details.getWifi_os2().equals("apple")) || (form_details.getWifi_os3().equals("ios") || form_details.getWifi_os3().equals("apple")))) {
                sb.append("Since one of your devices are iOS/Apple, so Wi-Fi Certificate needs to be generated or if Certificate for Wi-Fi has already been created then same can be used.\n"
                        + "New Certificate creation will take some time. Thanks for your patience.</br>");
            }
            sb.append("For any assistance, please contact on <b>1800-111-555</b> or mail us to servicedesk.nic.in</br>");
            sb.append("<p>Regards,</p>");
            sb.append("eForms Team NIC");
            System.out.println(printlog + "SendMailUser :: " + sb);

            map.put("from", "");
            map.put("subject", ref_num + " Submitted");
            map.put("mailbody", sb.toString());
            map.put("to", UserEmail);
            map.put("cc", null);
            map.put("attachmentpath", null);
            map.put("mobile", UserMobile);
            map.put("smsbody", "Your " + form_name + " request has been successfully submitted. Please keep this reference number " + ref_num + " to check the status of your application. To track the status of your application use this link https://eforms.nic.in/ NICSI");
            map.put("templateId", "1107160811935140690");
        }
        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
        object.notify(map);
    }

    public void SendMailCA(String formtype, String ref_num, String CaEmail, String CaMobile, String UserEmail, Map profile_values, String min_val) {
        String form_name = "";
        if (formtype.equals("imappop")) {
            form_name = "IMAP/POP";
        } else if (formtype.equals("bulk")) {
            form_name = "Bulk User Email";
        } else if (formtype.equals("ip")) {
            form_name = "Change/Add IP";
        } else if (formtype.equals("distributionlist")) {
            form_name = "Distribution List";
        } else if (formtype.equals("bulkdistributionlist")) {    //Added by Rahul jan 2021
            form_name = "Distribution List";
        } else if (formtype.equals("ldap")) {
            form_name = "LDAP Authentication";
        } else if (formtype.equals("nkn_single")) {
            form_name = "NKN User Email";
        } else if (formtype.equals("nkn_bulk")) {
            form_name = "NKN Bulk User Email";
        } else if (formtype.equals("relay")) {
            form_name = "Smtp/Relay";
        } else if (formtype.equals("sms")) {
            form_name = "NIC-SMS Gateway";
        } else if (formtype.equals("single")) {
            form_name = "Single User Email";
        } else if (formtype.equals("gem")) {
            form_name = "Gem User";
        } else if (formtype.equals("mobile")) {
            form_name = "Mobile Update";
        } else if (formtype.equals("wifi")) {
            form_name = "WIFI";
        } else if (formtype.equals("dns")) {
            form_name = "DNS";
        } else if (formtype.equals("vpn_single")) {
            form_name = "VPN Single User";
        } else if (formtype.equals("vpn_bulk")) {
            form_name = "VPN Bulk User";
        } else if (formtype.equals("webcast")) {
            form_name = "WEBCAST";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear Sir/Madam,</p>");

        sb.append(UserEmail).append(" has submitted the ").append(form_name).append(" request form and he/she has selected you as his/her Reporting/Nodal/Forwarding Officer. Please verify the details mentioned in the form by using the link https://eforms.nic.in/ and approve the same for further process.");
        sb.append("You can login using ").append(CaEmail).append(".").append(" NICSI");
        sb.append("<p>The Applicant Details are shown below:</p>");
        sb.append("<h3>Applicant Details</h3><table border='0'>"
                + "<tr><td>Name</td><td>" + profile_values.get("cn") + "</td></tr>"
                + "<tr><td>Email</td><td>" + profile_values.get("email") + "</td></tr>"
                + "<tr><td>Mobile</td><td>" + profile_values.get("mobile") + "</td></tr>"
                + "<tr><td>Designation</td><td>" + profile_values.get("desig") + "</td></tr>"
                + "<tr><td>Organization</td><td>" + "  " + min_val + "</td></tr>"
                + "</table>");
        sb.append("<p><strong>Kindly note : You are requested to verify the credentials and authenticity of the applicant prior to approval of request. If more information is required please use the option \"RAISE A QUERY\" and ask for more inputs for verifying credentials</strong></p>");
        sb.append("<p>Regards,</p>");
        sb.append("eForms Team NIC");
        System.out.println(printlog + "SendMailCA :: " + sb);
        //Email_Sender em = new Email_Sender();
        //em.Email_sender(CaEmail, sb.toString(), ref_num + " Submitted");
        //String msg = URLEncoder.encode(UserEmail + " has submitted the " + form_name + " request form and he/she has selected you as his/her Reporting/Nodal/Forwarding Officer. Please verify the details mentioned in the form by using the link https://eforms.nic.in/signup_ca and approve the same for further process. You can login using " + CaEmail + " and " + CaMobile + ".");
        //SMS_Sender sm = new SMS_Sender();
        //String m_id = sm.Sms_Sender(CaMobile, msg);

        HashMap<String, Object> map = new HashMap<>();
        map.put("from", "");
        map.put("subject", ref_num + " Submitted");
        map.put("mailbody", sb.toString());
        map.put("cc", null);

        map.put("attachmentpath", null);

        map.put("to", CaEmail);

        map.put("mobile", CaMobile);
        //12/2/2022 invalid template
        map.put("smsbody", UserEmail + "  has submitted the  " + form_name + " request form and he/she has selected you as his/her Reporting/Nodal/Forwarding Officer.Please verify the details mentioned in the form by using the link https://eforms.nic.in/ and approve the same for further process. You can login using  " + CaEmail + " . NICSI");
        //map.put("smsbody", UserEmail + " has submitted the " + form_name + " request form and he/she has selected you as his/her Reporting/Nodal/Forwarding Officer. Please verify the details mentioned in the form by using the link https://eforms.nic.in/ and approve the same for further process. You can login using " + CaEmail + ". NICSI");
        map.put("templateId", "1107160811860414640");
        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
        object.notify(map);
    }

    public void SendMailCO(String formtype, String ref_num, ArrayList coordinators, String UserEmail, Map profile_values, String min_val, FormBean form_details) {
        String form_name = "";
        if (formtype.equals("imappop")) {
            form_name = "IMAP/POP";
        } else if (formtype.equals("bulk")) {
            form_name = "Bulk User Email";
        } else if (formtype.equals("ip")) {
            form_name = "Change/Add IP";
        } else if (formtype.equals("distributionlist")) {
            form_name = "Distribution List";
        } else if (formtype.equals("bulkdistributionlist")) {    //Added by Rahul jan 2021
            form_name = "Distribution List";
        } else if (formtype.equals("ldap")) {
            form_name = "LDAP Authentication";
        } else if (formtype.equals("nkn_single")) {
            form_name = "NKN User Email";
        } else if (formtype.equals("nkn_bulk")) {
            form_name = "NKN Bulk User Email";
        } else if (formtype.equals("relay")) {
            form_name = "Smtp/Relay";
        } else if (formtype.equals("sms")) {
            form_name = "NIC-SMS Gateway";
        } else if (formtype.equals("single")) {
            form_name = "Single User Email";
        } else if (formtype.equals("gem")) {
            form_name = "Gem User";
        } else if (formtype.equals("mobile")) {
            form_name = "Mobile Update";
        } else if (formtype.equals("wifi")) {
            form_name = "WIFI";
        } else if (formtype.equals("dns")) {
            form_name = "DNS";
        } else if (formtype.equals("vpn_single") || formtype.equals("change_add")) {
            form_name = "VPN Single User";
        } else if (formtype.equals("vpn_bulk")) {
            form_name = "VPN Bulk User";
        } else if (formtype.equals("vpn_renew")) { // NITIN ADDED
            form_name = "VPN Renew";
        } else if (formtype.equals("webcast")) {
            form_name = "WEBCAST";
        } else if (formtype.equals("vpn_surrender")) { // NITIN ADDED	
            form_name = "VPN Surrender";
        } else if (formtype.equals("vpn_delete")) { // NITIN ADDED	
            form_name = "VPN Delete";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear Sir/Madam,</p>");
        sb.append(UserEmail).append(" has submitted the ").append(form_name).append(" request form. Please verify the details mentioned in the form by using the link https://eforms.nic.in/ and approve the same for further process. You can login using your email address.");
        sb.append("For any assistance, please contact on <b>1800-111-555</b> or mail us to servicedesk.nic.in.</br>");
        sb.append("<p>The Applicant and Reporting Officer Details are shown below:</p>");
        sb.append("<h3>Applicant Details</h3><table border='0'>"
                + "<tr><td>Name</td><td>" + profile_values.get("cn") + "</td></tr>"
                + "<tr><td>Email</td><td>" + profile_values.get("email") + "</td></tr>"
                + "<tr><td>Mobile</td><td>" + profile_values.get("mobile") + "</td></tr>"
                + "<tr><td>Designation</td><td>" + profile_values.get("desig") + "</td></tr>"
                + "<tr><td>Organization</td><td>" + "  " + min_val + "</td></tr>"
                + "</table>");

        sb.append("<h3>Reporting Officer Details</h3><table border='0'>"
                + "<tr><td>Name</td><td>" + profile_values.get("ca_name") + "</td></tr>"
                + "<tr><td>Email</td><td>" + profile_values.get("ca_email") + "</td></tr>"
                + "<tr><td>Mobile</td><td>" + profile_values.get("ca_mobile") + "</td></tr>"
                + "<tr><td>Designation</td><td>" + profile_values.get("ca_design") + "</td></tr>"
                + "</table>");
        sb.append("<p><strong>Kindly note : You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer prior to approval of request. If more information is required please use the option \"RAISE A QUERY\" and ask for more inputs for verifying credentials</strong></p>");
        sb.append("<p>Regards,</p>");
        sb.append("eForms Team NIC");
        System.out.println(printlog + "SendMailCA :: " + sb);
        //Email_Sender em = new Email_Sender();
//        for (Object to : coordinators) {
//            em.Email_sender(to.toString(), sb.toString(), ref_num + " Submitted");
//        }
//        
        HashMap<String, Object> map = new HashMap<>();
        map.put("from", "");
        map.put("subject", ref_num + " Submitted");
        map.put("mailbody", sb.toString());
        map.put("cc", null);
        if (formtype.equals("webcast")) {
            if (form_details.getWebcast_uploaded_files() != null) {
                ArrayList webcast_file = new ArrayList();
                Map<String, String> filename = (HashMap) form_details.getWebcast_uploaded_files();
                for (Map.Entry<String, String> entry : filename.entrySet()) {
                    webcast_file.add(entry.getValue());
                }
                //webcast_file.add("/eForms/PDF/" + ref_num + ".pdf");
                webcast_file.add(ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf");
                System.out.println(printlog + webcast_file);
                map.put("attachmentpath", webcast_file);
            } else {
                map.put("attachmentpath", null);
            }
        } else {
            map.put("attachmentpath", null);
        }
        map.put("to", coordinators);

        sb.setLength(0);

        String mobileNumbers = "";
        ArrayList<String> mobile = new ArrayList<>();
        for (Object to : coordinators) {
            mobileNumbers = LdapQuery.GetMobile((String) to);
            mobile.add(mobileNumbers);

        }
        if (!mobile.contains("+917835829239")) {
            String smsbody = UserEmail + " has submitted the " + form_name + " request form. Please verify the details mentioned in the form by using the link https://eforms.nic.in/ and approve the same for further process. You can login using your email address. For any assistance, please contact on <b>1800-111-555</b> or mail us to servicedesk.nic.in. NICSI";
            map.put("mobile", mobile);
            map.put("smsbody", smsbody);
            map.put("templateId", "1107160811899783785");
        }
        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
        object.notify(map);

    }

    public void SendMailManual(String formtype, String ref_num, String UserEmail, String UserMobile, String filepath) {
        String form_name = "";
        if (formtype.equals("imappop")) {
            form_name = "IMAP/POP";
        } else if (formtype.equals("bulk")) {
            form_name = "Bulk User Email";
        } else if (formtype.equals("ip")) {
            form_name = "Change/Add IP";
        } else if (formtype.equals("distributionlist")) {
            form_name = "Distribution List";
        } else if (formtype.equals("bulkdistributionlist")) {    //Added by Rahul jan 2021
            form_name = "Distribution List";
        } else if (formtype.equals("ldap")) {
            form_name = "LDAP Authentication";
        } else if (formtype.equals("nkn_single")) {
            form_name = "NKN User Email";
        } else if (formtype.equals("nkn_bulk")) {
            form_name = "NKN Bulk User Email";
        } else if (formtype.equals("relay")) {
            form_name = "Smtp/Relay";
        } else if (formtype.equals("sms")) {
            form_name = "NIC-SMS Gateway";
        } else if (formtype.equals("single")) {
            form_name = "Single User Email";
        } else if (formtype.equals("gem")) {
            form_name = "Gem User";
        } else if (formtype.equals("mobile")) {
            form_name = "Mobile Update";
        } else if (formtype.equals("wifi")) {
            form_name = "WIFI";
        } else if (formtype.equals("dns")) {
            form_name = "DNS";
        } else if (formtype.equals("vpn_single") || formtype.equals("change_add")) {
            form_name = "VPN Single User";
        } else if (formtype.equals("vpn_bulk")) {
            form_name = "VPN Bulk User";
        } else if (formtype.equals("vpn_renew")) { // NITIN ADDED
            form_name = "VPN Renew";
        } else if (formtype.equals("webcast")) {
            form_name = "WEBCAST";
        } else if (formtype.equals("vpn_surrender")) { // NITIN ADDED	
            form_name = "VPN Surrender";
        } else if (formtype.equals("vpn_delete")) { // NITIN ADDED	
            form_name = "VPN Delete";
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<p>Dear Sir/Madam,</p>");
            sb.append("Your ").append(form_name).append(" has been successfully submitted. Please keep this reference number ").append(ref_num).append(" to check the status of your application. <br/> ");
            sb.append("For any assistance, please contact on <b>1800-111-555</b> or mail us to servicedesk.nic.in</br>");
            sb.append("<p>Regards,</p>");
            sb.append("eForms Team NIC");
            System.out.println(printlog + "SendMailManual :: " + sb);
            HashMap<String, Object> map = new HashMap<>();
            map.put("from", "");
            map.put("subject", ref_num + " Submitted");
            map.put("mailbody", sb.toString());
            map.put("attachmentpath", filepath);
            map.put("to", UserEmail);
            map.put("mobile", UserMobile);
            map.put("smsbody", "Your " + form_name + " request has been successfully submitted. Please keep this reference number " + ref_num + " to check the status of your application. NICSI");
            map.put("templateId", "1107160811911149023");
            NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
            object.notify(map);
        } catch (Exception e) {
            System.out.println(printlog + "Error in Mail Sending =>" + e);
        }
    }

    private void SendMailSup(String formtype, String ref_num, String UserEmail) {
        String form_name = "";
        if (formtype.equals("wifi")) {
            form_name = "WIFI";
        } else if (formtype.equals("bulk")) {
            form_name = "Bulk User Email";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<p>Dear Support,</p>");
        sb.append(UserEmail).append(" has submitted the ").append(form_name).append(" request form. Please verify the details mentioned in the form by using the link https://eforms.nic.in/ and approve the same for further process.");
        sb.append("For any assistance, please contact on <b>1800-111-555</b> or mail us to servicedesk.nic.in.</br>");
        sb.append("<p>Regards,</p>");
        sb.append("eForms Team NIC");
        System.out.println(printlog + "SendMailSup :: " + sb);
        HashMap<String, Object> map = new HashMap<>();
        if (formtype.equals("wifi")) {
            map.put("to", "support@nkn.in");
        } else if (formtype.equals("bulk")) {
            map.put("to", "support@gov.in");
        }
        map.put("from", "");
        map.put("subject", ref_num + " Submitted");
        map.put("mailbody", sb.toString());
        map.put("cc", null);
        map.put("attachmentpath", null);
        map.put("mobile", "");
        map.put("smsbody", "");
        map.put("templateId", "1107160811911149023");
        NotifyThrouhRabbitMQ object = new NotifyThrouhRabbitMQ();
        object.notify(map);
    }

    // PDF Generations
    public String GenerateWhichPDF(FormBean form_details, String ref_num, String formtype) {
        System.out.println(printlog + " inside generate which pdf  ref_num " + ref_num + " formtype " + formtype);
        if (formtype.equals("dlist")) {
            formtype = "distributionlist";
        } else if (formtype.equals("bulkdlist")) {
            formtype = "bulkdistributionlist";
        }
        switch (formtype) {
            case "dor_ext":
                GeneratePDF_dor(form_details, ref_num);
                break;
            case "dor_ext_retired":
                GeneratePDF_retired_dor(form_details, ref_num);
                break;
            case "ip":
                GeneratePDF_ip(form_details, ref_num);
                break;
            case "distributionlist":
                GeneratePDF_distlist(form_details, ref_num);
                break;
            case "bulkdistributionlist":
                GeneratePDF_bulkdlist(form_details, ref_num);
                break;
            case "dns":
                GeneratePDF_dns(form_details, ref_num);
                break;
            case "single":
                GeneratePDF_single(form_details, ref_num);
                break;
            case "bulk":
                GeneratePDF_bulk(form_details, ref_num);
                break;
            case "gem":
                GeneratePDF_gem(form_details, ref_num);
                break;
            case "nkn_single":
            case "nkn_bulk":
                GeneratePDF_nkn(form_details, ref_num, formtype);
                break;
            case "imappop":
                GeneratePDF_imappop(form_details, ref_num);
                break;
            case "ldap":
                GeneratePDF_ldap(form_details, ref_num);
                break;
            case "mobile":
                GeneratePDF_mobile(form_details, ref_num);
                break;
            case "relay":
                GeneratePDF_relay(form_details, ref_num);
                break;
            case "sms":
                GeneratePDF_sms(form_details, ref_num);
                break;
            case "vpn_single":
            case "vpn_bulk":
            case "vpn_renew":
            case "vpn_surrender":
            case "vpn_delete":
            case "change_add":
                GeneratePDF_vpn(form_details, ref_num, formtype);
                break;
            case "wifi":
                GeneratePDF_wifi(form_details, ref_num);
                break;
            case "wifiport":
                GeneratePDF_wifiport(form_details, ref_num);
                break;
            case "webcast":
                GeneratePDF_webcast(form_details, ref_num);
                break;
            case "centralutm":
                GeneratePDF_firewall(form_details, ref_num);
                break;
            case "email_act":
                GeneratePDF_activate(form_details, ref_num);
                break;
            case "email_deact":
                GeneratePDF_deactivate(form_details, ref_num);
                break;
            case "daonboarding":
                admin.UserTrack usertrack = new admin.UserTrack();
                ResultSet rs = null,
                 rs1 = null,
                 rs2 = null,
                 rs3 = null,
                 rs4 = null;
                PreparedStatement pst = null;
                usertrack.callDaOnbodard(pst, ref_num, rs, rs1);
                //GeneratePDF_daonboarding(form_details, ref_num);
                break;
        }
        return SUCCESS;
    }

    public String GeneratePDF_dor(FormBean form_details, String ref_num) {
        System.out.println(printlog + " inside GeneratePDF_dor which pdf  ref_num " + ref_num);

        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Extention of Account Validity Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));

            details.add(new Paragraph("5) Date Of Birth* : " + form_details.getSingle_dob() + "               Date Of Retirement* : " + form_details.getSingle_dor(), normalFont));
            if (form_details.getUser_empcode() == null || form_details.getUser_empcode().equals("")) {
                details.add(new Paragraph("6) Designation* : " + form_details.getUser_design(), normalFont));
            } else {
                details.add(new Paragraph("7) Designation* : " + form_details.getUser_design() + "               Employee Code : " + form_details.getUser_empcode(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("8) Telephone Number(O)* : " + form_details.getUser_ophone(), normalFont));
            } else {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone() + "               Telephone Number(R) : " + form_details.getUser_rphone(), normalFont));
            }
            details.add(new Paragraph("10) Official Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("11) City* : " + form_details.getUser_city() + "           State* : " + form_details.getUser_state() + "           Pin Code* : " + form_details.getUser_pincode(), normalFont));

            if (form_details.getUnder_sec_email() != null && !form_details.getUnder_sec_email().equals("")) {
                details.add(new Paragraph("12) Under Secretary Name* : " + form_details.getUnder_sec_name() + "     Email* : " + form_details.getUnder_sec_email(), normalFont));
                details.add(new Paragraph("13) Under Secretary Mobile* : " + form_details.getUnder_sec_mobile() + "     Designation* : " + form_details.getunder_sec_desig(), normalFont));
            }
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. The validity of account will be 12 months from the date of intergration.", normalFont));
            tncpoints.add(new Paragraph("2. The applicant agrees to transfer the fund within 7 days from the date of PI generation by NICSI.", normalFont));
            tncpoints.add(new Paragraph("3. NIC reserves te right to deactivate the account anytime due to non-payment of dues.", normalFont));
            tncpoints.add(new Paragraph("4. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("5. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("6. By not doing so (point no. 4 & 5 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("7. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("8.  Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("9. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("10. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("11. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("12. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("13. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("14. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("15. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("16. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("17. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("18. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("19. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("20. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("21. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));// email modified by pr on 14thjan19
            tncpoints.add(new Paragraph("22. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("23. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("24. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("25. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
            System.out.println(printlog + " inside GeneratePDF_dor END  ref_num " + ref_num);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF Dor exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_retired_dor(FormBean form_details, String ref_num) {
        System.out.println(printlog + " inside GeneratePDF_dor which pdf  ref_num " + ref_num);
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String filename = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Extention of Account Validity Form", boldFont));
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph("1) Name* : " + form_details.getApplicant_name(), normalFont));
            details.add(new Paragraph("2) Email* : " + form_details.getApplicant_email(), normalFont));
            details.add(new Paragraph("3) Mobile* : " + form_details.getApplicant_mobile(), normalFont));
            details.add(new Paragraph("4) Previous Date of Retirement* : " + form_details.getP_single_dor(), normalFont));
            details.add(new Paragraph("5) New Date Of Retirement* : " + form_details.getSingle_dor(), normalFont));
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("                                       Signature of the Applicant with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. The validity of account will be maximum 12 months from the date of integration.", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to keep the given user id and password a secret. ", normalFont));
            tncpoints.add(new Paragraph("3. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("4. By not doing so (point no. 2 & 3 above) the account may be compromised by hackers and the hacker can use the same account for sending suspicious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("5. Do not use the Save password option on the browser when you are prompted for it.", normalFont));
            tncpoints.add(new Paragraph("6. Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("7. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("8. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("9. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("10. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("11. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("12. By default accounts will be given access over WEB only (https://mail.gov.in).", normalFont));
            tncpoints.add(new Paragraph("13. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("14. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("15. Contact our 24x7 support if you have any problems. Phone 1800-111-555 or you can send mail to servicedesk.nic.in.", normalFont));
            tncpoints.add(new Paragraph("16. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("17. NIC does not capture any Aadhaar related information.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
            System.out.println(printlog + " inside GeneratePDF_dor END  ref_num " + ref_num);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF Dor exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_webcast(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();

            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Font hindiFont = FontFactory.getFont("/eForms/FreeSans.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            Paragraph emptypara = new Paragraph("\n");

            Paragraph header = new Paragraph();

            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("WEBCAST Service Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
                    + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);

            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);

            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email(), normalFont));
            details.add(new Paragraph("4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Forwarding Officer Name* : " + form_details.getFwd_ofc_name() + "   Email* : " + form_details.getFwd_ofc_email(), normalFont));
            details.add(new Paragraph("6) Forwarding Officer Mobile* : " + form_details.getFwd_ofc_mobile() + "   Designation* : " + form_details.getFwd_ofc_design(), normalFont));
            details.add(new Paragraph("7) Forwarding Officer Address* : " + form_details.getFwd_ofc_add() + "   Telephone* : " + form_details.getFwd_ofc_tel(), normalFont));

            details.add(new Paragraph("8) Type of Request:* : " + form_details.getReq_for().toUpperCase(), normalFont));
            if (form_details.getReq_for().equals("live")) {
                details.add(new Paragraph("9) Event Name/Description (English)* : " + form_details.getEvent_name_eng(), normalFont));
                details.add(new Paragraph("10) Event Name/Description (Hindi)* : " + form_details.getEvent_name_hin(), hindiFont));
                details.add(new Paragraph("11) Date and timings of Event* : " + form_details.getEvent_start_date() + " to " + form_details.getEvent_end_date() + "   Type of Event* : " + form_details.getEvent_type(), normalFont));
                details.add(new Paragraph("12) Event Coordinator Name* : " + form_details.getEvent_coo_name() + "   Email* : " + form_details.getEvent_coo_email(), normalFont));
                details.add(new Paragraph("13) Event Coordinator Mobile* : " + form_details.getEvent_coo_mobile() + "   Designation* : " + form_details.getEvent_coo_design(), normalFont));
                details.add(new Paragraph("14) Event Coordinator Address* : " + form_details.getEvent_coo_address(), normalFont));
                details.add(new Paragraph("15) Is live telecast will be available on DD* : " + form_details.getTelecast(), normalFont));
                if (form_details.getTelecast().equals("yes")) {
                    details.add(new Paragraph("16) Channel Name* : " + form_details.getChannel_name(), normalFont));
                } else if (form_details.getTelecast().equals("no")) {
                    if (form_details.getLive_feed().equalsIgnoreCase("Through VC")) {
                        details.add(new Paragraph("16) How Are you planning to get live audio/video feed ?* : " + form_details.getLive_feed() + "   VC ID* : " + form_details.getVc_id(), normalFont));
                    } else {
                        details.add(new Paragraph("16) How Are you planning to get live audio/video feed ?* : " + form_details.getLive_feed(), normalFont));
                    }
                }
                details.add(new Paragraph("17) Is it a Conference/Workshop?* : " + form_details.getConf_radio(), normalFont));
                details.add(new Paragraph("18) Name of Conference/Workshop* : " + form_details.getConf_name(), normalFont));
                details.add(new Paragraph("19) Type of Conference/Workshop* : " + form_details.getConf_type() + "   City and Venue of Conference/Workshop* : " + form_details.getConf_city(), normalFont));
                details.add(new Paragraph("20) Conference/Workshop schedule /Program details with no. of days* : " + form_details.getConf_schedule(), normalFont));
                if (form_details.getHall_type().equalsIgnoreCase("multiple")) {
                    details.add(new Paragraph("21) Number of parallel sessions* : " + form_details.getConf_session() + "   Number Of Halls* : " + form_details.getHall_type() + "(" + form_details.getHall_number() + ")", normalFont));
                } else {
                    details.add(new Paragraph("21) Number of parallel sessions* : " + form_details.getConf_session() + "   Number Of Halls* : " + form_details.getHall_type(), normalFont));
                }
                details.add(new Paragraph("22) Internet Connectivity /Leased line* : " + form_details.getConf_bw() + "   Internet/Leased line Service provider Name* : " + form_details.getConf_provider(), normalFont));
                if (form_details.getConf_flash().equals("yes")) {
                    details.add(new Paragraph("23) Event Management company hired ?* : " + form_details.getConf_event_hired() + "   Do this agency able to stream live using Flash Live Media Encoder* : " + form_details.getConf_flash(), normalFont));
                } else {
                    details.add(new Paragraph("23) Event Management company hired ?* : " + form_details.getConf_event_hired() + "   Do this agency able to stream live using Flash Live Media Encoder* : " + form_details.getConf_flash() + " (" + form_details.getLocal_setup() + ")", normalFont));
                }
                if (form_details.getConf_video().equals("yes")) {
                    details.add(new Paragraph("24) Video coverage agency hired or not?* : " + form_details.getConf_video() + "   Contact details of video agency* : " + form_details.getConf_contact(), normalFont));
                } else {
                    details.add(new Paragraph("24) Video coverage agency hired or not?* : " + form_details.getConf_video(), normalFont));
                }
                details.add(new Paragraph("25) Payment details applicable or not?* : " + form_details.getPayment(), normalFont));
                if (form_details.getPayment().equals("yes")) {
                    details.add(new Paragraph("26) Cheque/DD No/NEFT No/RTGS No : " + form_details.getCheque_no() + "   Amount : " + form_details.getCheque_amount() + "   Date : " + form_details.getCheque_date(), normalFont));
                    details.add(new Paragraph("27) Bank & Branch : " + form_details.getBank_name(), normalFont));
                    if (form_details.getRemarks() == null || form_details.getRemarks().equals("")) {

                    } else {
                        details.add(new Paragraph("28) Remarks : " + form_details.getRemarks(), normalFont));
                    }
                } else if (form_details.getRemarks() == null || form_details.getRemarks().equals("")) {

                } else {
                    details.add(new Paragraph("26) Remarks : " + form_details.getRemarks(), normalFont));
                }
            } else if (form_details.getReq_for().equals("demand")) {
                details.add(new Paragraph("9) Total number of video clips* : " + form_details.getEvent_no(), normalFont));
                details.add(new Paragraph("10) Total size in GB* : " + form_details.getEvent_size(), normalFont));
                details.add(new Paragraph("11) Media Format provided* : " + form_details.getMedia_format(), normalFont));
                details.add(new Paragraph("12) Payment details applicable or not?* : " + form_details.getPayment(), normalFont));
                if (form_details.getPayment().equals("yes")) {
                    details.add(new Paragraph("13) Cheque/DD No/NEFT No/RTGS No : " + form_details.getCheque_no() + "   Amount : " + form_details.getCheque_amount() + "   Date : " + form_details.getCheque_date(), normalFont));
                    details.add(new Paragraph("14) Bank & Branch : " + form_details.getBank_name(), normalFont));
                    if (form_details.getRemarks() == null || form_details.getRemarks().equals("")) {

                    } else {
                        details.add(new Paragraph("15) Remarks : " + form_details.getRemarks(), normalFont));
                    }
                } else if (form_details.getRemarks() == null || form_details.getRemarks().equals("")) {

                } else {
                    details.add(new Paragraph("13) Remarks : " + form_details.getRemarks(), normalFont));
                }
            }

            document.add(details);

            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Forwarding Officer with date and seal ", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_firewall(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }
            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font smallBoldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("SMS Services Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email(), normalFont));
            details.add(new Paragraph("4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile(), normalFont));

            details.add(new Paragraph("8) Central UTM Entry Details : ", normalFont));
            details.add(new Paragraph("SourceIP* : " + form_details.getSourceip().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("DestinationIP* : " + form_details.getDestinationip().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Service* : " + form_details.getService().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Port* : " + form_details.getPorts().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Action* : " + form_details.getAction().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Time Period* : " + form_details.getTimeperiod().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("9) Purpose : " + form_details.getRemarks(), normalFont));
            document.add(details);

            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Forwarding Officer with date and seal ", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();

            tncpoints.add(new Paragraph("1. Entries marked with asterisk  (*) are mandatory", normalFont));
            tncpoints.add(new Paragraph("2. Only three devices allowed per user ID.", normalFont));
            tncpoints.add(new Paragraph("3. For iPhone/iPad/MAC, write ios(version) in Operating System.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + " == PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    public String GeneratePDF_ip(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String request_type;
            if (form_details.getIp_type().equals("addip")) {
                request_type = "8) Request* : ADD IP Request for : " + form_details.getReq_for();
            } else {
                request_type = "8) Request* : Change IP Request for : " + form_details.getReq_for();
            }
            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("IP Change/Add Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph(request_type, normalFont));
            if (form_details.getReq_for().equalsIgnoreCase("sms")) {
                details.add(new Paragraph("  a) SMS Account Name* : " + form_details.getAccount_name(), normalFont));
            }

            if (form_details.getReq_for().equalsIgnoreCase("relay")) {
                details.add(new Paragraph("  a) Application Name* : " + form_details.getRelay_app(), normalFont));
                if (form_details.getServer_loc().equals("Other")) {
                    details.add(new Paragraph("  b) Server Location* : " + form_details.getServer_loc_txt(), normalFont));
                } else {
                    details.add(new Paragraph("  b) Server Location* : " + form_details.getServer_loc(), normalFont));
                }
                if (form_details.getRelay_old_ip().equals("") || form_details.getRelay_old_ip() == null) {
                } else {
                    details.add(new Paragraph("  c) Old IP Address : " + form_details.getRelay_old_ip(), normalFont));
                }
            }
            if (form_details.getReq_for().equalsIgnoreCase("ldap")) {
                details.add(new Paragraph("  a) Application Name* : " + form_details.getLdap_account_name(), normalFont));

                details.add(new Paragraph("  b) URL of the application* : " + form_details.getLdap_url(), normalFont));

                details.add(new Paragraph("  c) LDAP auth id allocated: " + form_details.getLdap_auth_allocate(), normalFont));
            }
            details.add(new Paragraph("9) IP Details* : ", normalFont));
            document.add(details);
            document.add(emptypara);
            PdfPTable iptable = null;
            if (form_details.getIp_type().equals("addip")) {
                iptable = new PdfPTable(4); // 4 columns.
                PdfPCell cell1 = new PdfPCell(new Paragraph("IP 1", boldFont));
                PdfPCell cell2 = new PdfPCell(new Paragraph("IP 2", boldFont));
                PdfPCell cell3 = new PdfPCell(new Paragraph("IP 3", boldFont));
                PdfPCell cell4 = new PdfPCell(new Paragraph("IP 4", boldFont));
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                iptable.addCell(cell1);
                iptable.addCell(cell2);
                iptable.addCell(cell3);
                iptable.addCell(cell4);
                iptable.addCell(new Paragraph(form_details.getAdd_ip1(), normalFont));
                iptable.addCell(new Paragraph(form_details.getAdd_ip2(), normalFont));
                iptable.addCell(new Paragraph(form_details.getAdd_ip3(), normalFont));
                iptable.addCell(new Paragraph(form_details.getAdd_ip4(), normalFont));
            } else {
                iptable = new PdfPTable(2); // 2 columns.
                PdfPCell cell1 = new PdfPCell(new Paragraph("OLD IP", boldFont));
                PdfPCell cell2 = new PdfPCell(new Paragraph("NEW IP", boldFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                iptable.addCell(cell1);
                iptable.addCell(cell2);
                iptable.addCell(new Paragraph(form_details.getOld_ip1(), normalFont));
                iptable.addCell(new Paragraph(form_details.getNew_ip1(), normalFont));
                if (!form_details.getOld_ip2().equals("") && form_details.getOld_ip2() != null) {
                    iptable.addCell(new Paragraph(form_details.getOld_ip2(), normalFont));
                    iptable.addCell(new Paragraph(form_details.getNew_ip2(), normalFont));
                }
                if (!form_details.getOld_ip3().equals("") && form_details.getOld_ip3() != null) {
                    iptable.addCell(new Paragraph(form_details.getOld_ip3(), normalFont));
                    iptable.addCell(new Paragraph(form_details.getNew_ip3(), normalFont));
                }
                if (!form_details.getOld_ip4().equals("") && form_details.getOld_ip4() != null) {
                    iptable.addCell(new Paragraph(form_details.getOld_ip4(), normalFont));
                    iptable.addCell(new Paragraph(form_details.getNew_ip4(), normalFont));
                }
            }
            document.add(iptable);
            document.add(emptypara);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            // footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            // footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("3. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("4. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("5. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("6. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("7. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    private String GeneratePDF_distlist(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Distribution List Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Name of List* : " + form_details.getList_name(), normalFont));
            details.add(new Paragraph("9) Description of List* : " + form_details.getDescription_list(), normalFont));
            details.add(new Paragraph("10) Moderator Name* : " + form_details.getT_off_name(), normalFont));
            details.add(new Paragraph("11) Moderator Email* : " + form_details.getTauth_email(), normalFont));
            details.add(new Paragraph("12) Moderator Mobile* : " + form_details.getTmobile(), normalFont));
            details.add(new Paragraph("13) Will the List be moderated* : " + form_details.getList_mod(), normalFont));
            details.add(new Paragraph("14) Are only members allowed to send mails to the list* : " + form_details.getAllowed_member(), normalFont));
            if (form_details.getList_temp().equals("yes")) {
                details.add(new Paragraph("15) Is list temporary* : " + form_details.getList_temp() + "   Validity Date* : " + form_details.getValidity_date(), normalFont));
            } else {
                details.add(new Paragraph("15) Is list temporary* : " + form_details.getList_temp(), normalFont));
            }
            details.add(new Paragraph("16) Will list accept mail from a non-NICNET email address (from internet like gmail, yahoo etc)* : " + form_details.getNon_nicnet(), normalFont));
            document.add(details);
            document.add(emptypara);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Entries marked with asterisk  (*) are mandatory", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("3. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("4. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("5. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("6. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("7. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("8. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("9. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    private String GeneratePDF_bulkdlist(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        ResultSet rs4;
        PreparedStatement pst = null;
        try {
            String final_dept;
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Bulk Distribution List Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            Paragraph details_bulk = new Paragraph();

            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));

            pst = conSlave.prepareStatement("select id,list_name,description_list,list_mod,allowed_member,list_temp,mail_Acceptance,owner_Name,Owner_Email,owner_Mobile,moderator_Name,moderator_Email,moderator_Mobile,owner_Admin,moderator_Admin  from dlist_bulk where registration_no= ? ");
            pst.setString(1, ref_num);
            rs4 = pst.executeQuery();
            List<Object> ExcelFileDetails = new ArrayList<>();
            int tem = 1;
            while (rs4.next()) {
                Paragraph details1 = new Paragraph();

                details1.add(new Paragraph(" Name of List* : " + rs4.getString("list_name"), normalFont));
                details1.add(new Paragraph(" Description of List* : " + rs4.getString("description_list"), normalFont));
                details1.add(new Paragraph(" list_mod* : " + rs4.getString("list_mod"), normalFont));
                details1.add(new Paragraph(" Allowed Member* : " + rs4.getString("allowed_member"), normalFont));
                details1.add(new Paragraph(" List Temp* : " + rs4.getString("list_temp"), normalFont));
                details1.add(new Paragraph(" Mail Acceptance : " + rs4.getString("mail_Acceptance"), normalFont));
                details1.add(new Paragraph(" Owner Name* : " + rs4.getString("owner_Name"), normalFont));
                details1.add(new Paragraph(" Owner Email* : " + rs4.getString("Owner_Email"), normalFont));
                details1.add(new Paragraph(" Owner Mobile* : " + rs4.getString("owner_Mobile"), normalFont));
                details1.add(new Paragraph(" Moderator Name* : " + rs4.getString("moderator_Name"), normalFont));
                details1.add(new Paragraph(" Moderator Email* : " + rs4.getString("moderator_Email"), normalFont));
                details1.add(new Paragraph(" Moderator Mobile* : " + rs4.getString("moderator_Mobile"), normalFont));
                details1.add(new Paragraph(" Owner Admin : " + rs4.getString("owner_Admin"), normalFont));
                details1.add(new Paragraph(" Moderator Admin : " + rs4.getString("moderator_Admin"), normalFont));
                details_bulk.add(details1);
                tem++;
            }
            document.add(details);
            document.add(details_bulk);
            document.add(emptypara);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Entries marked with asterisk  (*) are mandatory", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("3. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("4. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("5. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("6. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("7. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("8. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("9. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    private String GeneratePDF_dns(FormBean form_details, String refno) {
        PreparedStatement pst = null;
        ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null;
        String sql = "select * from dns_registration where registration_no=?";
        pst = null;
        Document document = null;
        PdfWriter writer = null;
        UserTrack userTrack = new UserTrack();
        String json = null;
        Map<String, Object> prvwdetails = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            pst = conSlave.prepareStatement(sql);
            pst.setString(1, refno);
            rs = pst.executeQuery();
            while (rs.next()) {
                try {
                    String final_dept;
                    switch (rs.getString("employment")) {
                        case "Central":
                            if (rs.getString("department").equalsIgnoreCase("other")) {
                                final_dept = "1) Ministry/Department* : " + rs.getString("ministry") + " / " + rs.getString("other_dept");
                            } else {
                                final_dept = "1) Ministry/Department* : " + rs.getString("ministry") + " / " + rs.getString("department");
                            }
                            break;
                        case "Others":
                        case "Psu":
                        case "Const":
                        case "Nkn":
                            if (rs.getString("organization").equalsIgnoreCase("other")) {
                                final_dept = "1) Organization Name* : " + rs.getString("organization") + " / " + rs.getString("other_dept");  // other dept added by pr on 10thjul19
                            } else {
                                final_dept = "1) Organization Name* : " + rs.getString("organization");  // other department added by pr on 10thjul19
                            }
                            break;
                        default:
                            if (rs.getString("department").equalsIgnoreCase("other")) {
                                final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("other_dept");
                            } else {
                                final_dept = "1) State/Department* : " + rs.getString("state") + " / " + rs.getString("department");
                            }

                            break;
                    }

                    //FileOutputStream file = new FileOutputStream("/eForms/PDF/" + refno + ".pdf");
                    String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + refno + ".pdf";
                    FileOutputStream file = new FileOutputStream(filename);
                    document = new Document();
                    writer = PdfWriter.getInstance(document, file);
                    document.open();
                    Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
                    Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
                    Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
                    Paragraph emptypara = new Paragraph("\n");
                    Paragraph header = new Paragraph();
                    header.add(new Paragraph("Government of India", HeaderFont));
                    header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
                    header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
                    if (rs.getString("req_for").equals("req_modify")) {
                        header.add(new Paragraph("MODIFY DNS Entry Request Form", boldFont));
                    } else if (rs.getString("req_for").equals("req_delete")) {
                        header.add(new Paragraph("DNS Entry DELETE Request Form", boldFont));
                    } else {
                        header.add(new Paragraph("NEW DNS Entry Request Form", boldFont));
                    }
                    header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                            + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont)); // line modified by pr on 18thjan19
                    header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
                    header.setAlignment(header.ALIGN_CENTER);
                    document.add(header);
                    document.add(emptypara);
                    Paragraph reg_num = new Paragraph("Registration number :" + refno, boldFont);
                    reg_num.setAlignment(reg_num.ALIGN_RIGHT);
                    document.add(reg_num);
                    document.add(emptypara);
                    Paragraph details = new Paragraph();
                    details.add(new Paragraph(final_dept, normalFont));
                    details.add(new Paragraph("2) Applicant Name* : " + rs.getString("auth_off_name"), normalFont));
                    details.add(new Paragraph("3) Applicant Email* : " + rs.getString("auth_email") + "       4) Applicant Mobile* : " + rs.getString("mobile"), normalFont));
                    details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + rs.getString("hod_name"), normalFont));
                    details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + rs.getString("hod_email"), normalFont));
                    details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + rs.getString("hod_mobile") + "   Designation* : " + rs.getString("ca_desig"), normalFont));
                    DnsService dnsservice = new DnsService();
                    if (!dnsservice.isNewRequest(refno, rs.getString("req_other_record"))) {
                        details.add(new Paragraph("8) DNS Entry Details : ", normalFont));
                        if (rs.getString("form_type").equals("dns_single")) {
                            pst = conSlave.prepareStatement("select dns_url from dns_registration_url where registration_no=?");
                            pst.setString(1, refno);
                            rs1 = pst.executeQuery();
                            String url = "";
                            while (rs1.next()) {
                                url += rs1.getString("dns_url").concat(";");
                            }
                            if (url.length() > 0) {
                                String domain_url = url.substring(0, url.length() - 1);
                                if (domain_url.contains(";")) {
                                    details.add(new Paragraph("DNS URL* : " + domain_url.replaceAll(";", " , "), normalFont));
                                } else {
                                    details.add(new Paragraph("DNS URL* : " + domain_url, normalFont));
                                }
                            }
                            pst = conSlave.prepareStatement("select newip from dns_registration_newip where registration_no=?");
                            pst.setString(1, refno);
                            rs2 = pst.executeQuery();
                            String ip = "";
                            while (rs2.next()) {
                                ip += rs2.getString("newip").concat(";");
                            }
                            if (ip.length() > 0) {
                                String domain_newip = ip.substring(0, ip.length() - 1);
                                if (domain_newip.contains(";")) {
                                    details.add(new Paragraph("NEW Application IP : " + domain_newip.replaceAll(";", " , "), normalFont));
                                } else {
                                    details.add(new Paragraph("NEW Application IP : " + domain_newip, normalFont));
                                }
                            }
                            pst = conSlave.prepareStatement("select cname from dns_registration_cname where registration_no=?");
                            pst.setString(1, refno);
                            rs3 = pst.executeQuery();
                            String loc = "";
                            while (rs3.next()) {
                                loc += rs3.getString("cname").concat(";");
                            }
                            if (loc.length() > 0) {
                                String domain_loc = loc.substring(0, loc.length() - 1);
                                if (domain_loc.contains(";")) {
                                    details.add(new Paragraph("CNAME : " + domain_loc.replaceAll(";", " , "), normalFont));
                                } else {
                                    details.add(new Paragraph("CNAME : " + domain_loc, normalFont));
                                }
                            }
                            if (rs.getString("req_for").equals("req_modify")) {
                                pst = conSlave.prepareStatement("select oldip from dns_registration_oldip where registration_no=?");
                                pst.setString(1, refno);
                                rs4 = pst.executeQuery();
                                String oldip = "";
                                while (rs4.next()) {
                                    oldip += rs4.getString("oldip").concat(";");
                                }
                                if (oldip.length() > 0) {
                                    String domain_oldip = oldip.substring(0, oldip.length() - 1);
                                    if (domain_oldip.contains(";")) {
                                        details.add(new Paragraph(" OLD Application IP : " + domain_oldip.replaceAll(";", " , "), normalFont));
                                    } else {
                                        details.add(new Paragraph(" OLD Application IP : " + domain_oldip, normalFont));
                                    }
                                }
                            }
                            if (rs.getString("record_mx") == null || rs.getString("record_mx").equals("")) {
                                details.add(new Paragraph("9) MX : NA", normalFont));
                            } else {
                                details.add(new Paragraph("9) MX : " + rs.getString("record_mx"), normalFont));
                            }
                            if (rs.getString("record_ptr") == null || rs.getString("record_ptr").equals("")) {
                                details.add(new Paragraph("10) PTR : NA", normalFont));
                            } else {
                                details.add(new Paragraph("10) PTR : " + rs.getString("record_ptr"), normalFont));
                            }
                            if (rs.getString("record_srv") == null || rs.getString("record_srv").equals("")) {
                                details.add(new Paragraph("11) SRV : NA", normalFont));
                            } else {
                                details.add(new Paragraph("11) SRV : " + rs.getString("record_srv"), normalFont));
                            }
                            if (rs.getString("record_spf") == null || rs.getString("record_spf").equals("")) {
                                details.add(new Paragraph("12) SPF : NA", normalFont));
                            } else {
                                details.add(new Paragraph("12) SPF : " + rs.getString("record_spf"), normalFont));
                            }
                            if (rs.getString("record_txt") == null || rs.getString("record_txt").equals("")) {
                                details.add(new Paragraph("13) TXT : NA", normalFont));
                            } else {
                                details.add(new Paragraph("13) TXT : " + rs.getString("record_txt"), normalFont));
                            }
                            if (rs.getString("record_dmarc") == null || rs.getString("record_dmarc").equals("")) {
                                details.add(new Paragraph("14) DMARC : NA", normalFont));
                            } else {
                                details.add(new Paragraph("14) DMARC : " + rs.getString("record_dmarc"), normalFont));
                            }
                            details.add(new Paragraph("15) DNS Server Location* : " + rs.getString("server_location").replaceAll(";", " , "), normalFont));
                        } else {
                            pst = conSlave.prepareStatement("select dns_url from dns_registration_url where registration_no=?");
                            pst.setString(1, refno);
                            rs1 = pst.executeQuery();
                            String url = "";
                            while (rs1.next()) {
                                url += rs1.getString("dns_url").concat(";");
                            }
                            if (url.length() > 0) {
                                String domain_url = url.substring(0, url.length() - 1);
                                if (domain_url.contains(";")) {
                                    details.add(new Paragraph("DNS URL* : " + domain_url.replaceAll(";", " , "), normalFont));
                                } else {
                                    details.add(new Paragraph("DNS URL* : " + domain_url, normalFont));
                                }
                            }
                            pst = conSlave.prepareStatement("select newip from dns_registration_newip where registration_no=?");
                            pst.setString(1, refno);
                            rs2 = pst.executeQuery();
                            String ip = "";
                            while (rs2.next()) {
                                ip += rs2.getString("newip").concat(";");
                            }
                            if (ip.length() > 0) {
                                String domain_newip = ip.substring(0, ip.length() - 1);
                                if (domain_newip.contains(";")) {
                                    details.add(new Paragraph("NEW Application IP : " + domain_newip.replaceAll(";", " , "), normalFont));
                                } else {
                                    details.add(new Paragraph("NEW Application IP : " + domain_newip, normalFont));
                                }
                            }
                            pst = conSlave.prepareStatement("select cname from dns_registration_cname where registration_no=?");
                            pst.setString(1, refno);
                            rs3 = pst.executeQuery();
                            String loc = "";
                            while (rs3.next()) {
                                loc += rs3.getString("cname").concat(";");
                            }
                            if (loc.length() > 0) {
                                String domain_loc = loc.substring(0, loc.length() - 1);
                                if (domain_loc.contains(";")) {
                                    details.add(new Paragraph("CNAME : " + domain_loc.replaceAll(";", " , "), normalFont));
                                } else {
                                    details.add(new Paragraph("CNAME : " + domain_loc, normalFont));
                                }
                            }
                            if (rs.getString("req_for").equals("req_modify")) {
                                pst = conSlave.prepareStatement("select oldip from dns_registration_oldip where registration_no=?");
                                pst.setString(1, refno);
                                rs4 = pst.executeQuery();
                                String oldip = "";
                                while (rs4.next()) {
                                    oldip += rs4.getString("oldip").concat(";");
                                }
                                if (oldip.length() > 0) {
                                    String domain_oldip = oldip.substring(0, oldip.length() - 1);
                                    if (domain_oldip.contains(";")) {
                                        details.add(new Paragraph("OLD Application IP : " + domain_oldip.replaceAll(";", " , "), normalFont));
                                    } else {
                                        details.add(new Paragraph("OLD Application IP : " + domain_oldip, normalFont));
                                    }
                                }
                            }
                            if (rs.getString("record_mx") == null || rs.getString("record_mx").equals("")) {
                                details.add(new Paragraph("9) MX : NA", normalFont));
                            } else {
                                details.add(new Paragraph("9) MX : " + rs.getString("record_mx"), normalFont));
                            }
                            if (rs.getString("record_ptr") == null || rs.getString("record_ptr").equals("")) {
                                details.add(new Paragraph("10) PTR : NA", normalFont));
                            } else {
                                details.add(new Paragraph("10) PTR : " + rs.getString("record_ptr"), normalFont));
                            }
                            if (rs.getString("record_srv") == null || rs.getString("record_srv").equals("")) {
                                details.add(new Paragraph("11) SRV : NA", normalFont));
                            } else {
                                details.add(new Paragraph("11) SRV : " + rs.getString("record_srv"), normalFont));
                            }
                            if (rs.getString("record_spf") == null || rs.getString("record_spf").equals("")) {
                                details.add(new Paragraph("12) SPF : NA", normalFont));
                            } else {
                                details.add(new Paragraph("12) SPF : " + rs.getString("record_spf"), normalFont));
                            }
                            if (rs.getString("record_txt") == null || rs.getString("record_txt").equals("")) {
                                details.add(new Paragraph("13) TXT : NA", normalFont));
                            } else {
                                details.add(new Paragraph("13) TXT : " + rs.getString("record_txt"), normalFont));
                            }
                            if (rs.getString("record_dmarc") == null || rs.getString("record_dmarc").equals("")) {
                                details.add(new Paragraph("14) DMARC : NA", normalFont));
                            } else {
                                details.add(new Paragraph("14) DMARC : " + rs.getString("record_dmarc"), normalFont));
                            }
                            if (rs.getString("req_for").equals("req_modify")) {
                                if (rs.getString("record_mx1") == null || rs.getString("record_mx1").equals("")) {
                                    details.add(new Paragraph("15 NEW MX : NA", normalFont));
                                } else {
                                    details.add(new Paragraph("15) NEW MX : " + rs.getString("record_mx1"), normalFont));
                                }
                                if (rs.getString("record_ptr1") == null || rs.getString("record_ptr1").equals("")) {
                                    details.add(new Paragraph("16) NEW PTR : NA", normalFont));
                                } else {
                                    details.add(new Paragraph("16) NEW PTR : " + rs.getString("record_ptr1"), normalFont));
                                }
                            }
                        }
                        document.add(details);
                        document.add(emptypara);
                        // above code commented below added by pr on 12thjan19
                        Paragraph footer = new Paragraph();
                        footer.add(new Paragraph(""));
                        footer.add(new Paragraph(""));
                        footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
                        footer.add(new Paragraph(""));
                        footer.add(new Paragraph(""));
                        footer.setAlignment(footer.ALIGN_JUSTIFIED);
                        document.add(footer);
                        Paragraph footer1 = new Paragraph();
                        footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
                        footer1.setAlignment(footer.ALIGN_RIGHT);
                        document.add(footer1);
                        document.add(emptypara);
                        // below function call added by pr on 3rdjan19, table created with status flow
                    } else {
                        document.add(details);
                        document.add(emptypara);
                        // above code commented below added by pr on 12thjan19
                        Paragraph footer = new Paragraph();
                        footer.add(new Paragraph(""));
                        footer.add(new Paragraph(""));
                        footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
                        footer.add(new Paragraph(""));
                        footer.add(new Paragraph(""));
                        footer.setAlignment(footer.ALIGN_JUSTIFIED);
                        document.add(footer);
                        Paragraph footer1 = new Paragraph();
                        footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
                        footer1.setAlignment(footer.ALIGN_RIGHT);
                        document.add(footer1);
                        document.add(emptypara);
                        // below function call added by pr on 3rdjan19, table created with status flow
                        userTrack.createDNSTable(refno, document, header, emptypara, normalFont, boldFont, rs.getString("req_other_record"), rs.getString("req_for"));
                    }
                    userTrack.createStatusTable(refno, document, header, emptypara, normalFont, boldFont, rs.getString("auth_email"), rs.getString("mobile"), rs.getString("userip"));
                    document.close();
//                    try {
//                        genfileDownload = refno + ".pdf";
//                        fileInputStream = new FileInputStream(new File("/eForms/PDF/" + refno + ".pdf"));
//                    } catch (Exception e) {
//                        System.out.println(printlog + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "e: " + e.getMessage());
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(printlog + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "PDF DNS exception: " + e.getMessage());
                }
                return SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "PDF DNS exception: " + e.getMessage());
        }

        return SUCCESS;
    }

    public String GeneratePDF_single(FormBean form_details, String ref_num) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside generate pdf single consentreturn");

        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Single User Subscription Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Date Of Birth* : " + form_details.getSingle_dob() + "               Date Of Retirement* : " + form_details.getSingle_dor(), normalFont));
            if (form_details.getUser_empcode() == null || form_details.getUser_empcode().equals("")) {
                details.add(new Paragraph("9) Designation* : " + form_details.getUser_design(), normalFont));
            } else {
                details.add(new Paragraph("9) Designation* : " + form_details.getUser_design() + "               Employee Code : " + form_details.getUser_empcode(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("10) Telephone Number(O)* : " + form_details.getUser_ophone(), normalFont));
            } else {
                details.add(new Paragraph("10) Telephone Number(O)* : " + form_details.getUser_ophone() + "               Telephone Number(R) : " + form_details.getUser_rphone(), normalFont));
            }
            details.add(new Paragraph("11) Official Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("12) City* : " + form_details.getUser_city() + "           State* : " + form_details.getUser_state() + "           Pin Code* : " + form_details.getUser_pincode(), normalFont));

            // start, code added by pr on 11thjul19
            String mailType = "";
            if (form_details.getReq_for() != null) {
                String reqFor = form_details.getReq_for();
                if (reqFor != null) {
                    if (reqFor.trim().toLowerCase().equals("mail")) {
                        mailType = "Mail user (with mailbox)";
                    } else if (reqFor.trim().toLowerCase().equals("app")) {
                        //mailType = "Application user (without mailbox)";
                        mailType = "Application user (without mail box(Eoffice-auth))"; // line modified by pr on 5thmay2020
                    } else if (reqFor.trim().toLowerCase().equals("eoffice")) {
                        mailType = "e-office-srilanka";
                    }
                }
            }

            // end, code added by pr on 11thjul19            
            //details.add(new Paragraph("13) Type of Mail ID* : " + form_details.getReq_for(), normalFont));
            details.add(new Paragraph("13) Type of Mail ID* : " + mailType, normalFont)); // line modified by pr on 11thjul19

            if (form_details.getSingle_id_type().equals("id_name")) {
                details.add(new Paragraph("14) Email address preference* : Name Based", normalFont));
            } else {
                details.add(new Paragraph("14) Email address preference* : Designation/Office based id", normalFont));
            }

            details.add(new Paragraph("15) Preferred Email Address 1* : " + form_details.getSingle_email1(), normalFont));
            details.add(new Paragraph("16) Preferred Email Address 2* : " + form_details.getSingle_email2(), normalFont));

            if (form_details.getSingle_emp_type().equals("emp_regular")) {
                details.add(new Paragraph("17 Employee Description* : Regular Employee", normalFont));
            } else if (form_details.getSingle_emp_type().equals("consultant")) {
                details.add(new Paragraph("17 Employee Description* : Consultant", normalFont));
            } else {
                details.add(new Paragraph("17) Employee Description* : FMS Support Staffs", normalFont));
            }

            if (form_details.getUnder_sec_email() != null && !form_details.getUnder_sec_email().equals("")) {
                details.add(new Paragraph("18) Under Secretary Name* : " + form_details.getUnder_sec_name() + "     Email* : " + form_details.getUnder_sec_email(), normalFont));
                details.add(new Paragraph("19) Under Secretary Mobile* : " + form_details.getUnder_sec_mobile() + "     Designation* : " + form_details.getunder_sec_desig(), normalFont));
            }

            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("2. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("3. By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("4. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("5. Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("6. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("7. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("8. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("9. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("10. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("11. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("12. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("13. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("14. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("15. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("16. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("17. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("18. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.add(new Paragraph("19. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("20. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("21. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("22. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("23. Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate to NIC about their willingness to retain the id through NIC coordinator prior to retirement.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_bulk(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        UserTrack userTrack = new UserTrack();
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Bulk User Subscription Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));

            //details.add(new Paragraph("8) Date Of Birth* : " + form_details.getSingle_dob() + "               Date Of Retirement* : " + form_details.getSingle_dor(), normalFont));
            if (form_details.getUser_empcode() == null || form_details.getUser_empcode().equals("")) {
                details.add(new Paragraph("8) Designation* : " + form_details.getUser_design(), normalFont));
            } else {
                details.add(new Paragraph("8) Designation* : " + form_details.getUser_design() + "               Employee Code : " + form_details.getUser_empcode(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone(), normalFont));
            } else {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone() + "               Telephone Number(R) : " + form_details.getUser_rphone(), normalFont));
            }

            details.add(new Paragraph("10) Official Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("11) City* : " + form_details.getUser_city() + "           State* : " + form_details.getUser_state() + "           Pin Code* : " + form_details.getUser_pincode(), normalFont));

            // start, code added by pr on 11thjul19
            String mailType = "";

            String reqFor = form_details.getReq_for();
            if (reqFor != null) {
                if (reqFor.trim().toLowerCase().equals("mail")) {
                    mailType = "Mail user (with mailbox)";
                } else if (reqFor.trim().toLowerCase().equals("app")) {
                    //mailType = "Application user (without mailbox)";
                    mailType = "Application user (without mail box(Eoffice-auth))"; // line modified by pr on 5thmay2020
                } else if (reqFor.trim().toLowerCase().equals("eoffice")) {
                    mailType = "e-office-srilanka";
                }
            }

            // end, code added by pr on 11thjul19             
            //details.add(new Paragraph("12) Type of Mail ID* : " + form_details.getReq_for(), normalFont));
            details.add(new Paragraph("12) Type of Mail ID* : " + mailType, normalFont));// line modified by pr on 11thjul19

            if (form_details.getSingle_id_type().equals("id_name")) {
                details.add(new Paragraph("13) Email address preference* : Name Based", normalFont));
            } else {
                details.add(new Paragraph("13) Email address preference* : Designation/Office based id", normalFont));
            }

            if (form_details.getSingle_emp_type().equals("emp_regular")) {
                details.add(new Paragraph("14) Employee Description* : Govt/Psu Official", normalFont));
            } else if (form_details.getSingle_emp_type().equals("consultant")) {
                details.add(new Paragraph("14) Employee Description* : Consultant ", normalFont));
            } else {
                details.add(new Paragraph("14) Employee Description* : FMS Support Staffs ", normalFont));
            }

            if (form_details.getUnder_sec_email() != null && !form_details.getUnder_sec_email().equals("")) {
                details.add(new Paragraph("15) Under Secretary Name* : " + form_details.getUnder_sec_name() + "     Email* : " + form_details.getUnder_sec_email(), normalFont));
                details.add(new Paragraph("16) Under Secretary Mobile* : " + form_details.getUnder_sec_mobile() + "     Designation* : " + form_details.getunder_sec_desig(), normalFont));
            }
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("2. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("3. By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("4. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("5.  Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("6. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("7. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("8. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("9. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("10. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("11. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("12. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("13. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("14. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("15. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("16. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("17. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("18. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont)); // email modified by pr on 14thjan19
            tncpoints.add(new Paragraph("19. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("20. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("21. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("22. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("23. Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate to NIC about their willingness to retain the id through NIC coordinator prior to retirement.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            userTrack.createEmailTable(ref_num, document, header, emptypara, normalFont, boldFont);
            //document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_gem(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept = null;
            switch (form_details.getPse()) {
                case "central_pse":
                    final_dept = "1) Controlling Ministry* : " + form_details.getPse_ministry();
                    break;
                case "state_pse":
                    final_dept = "1) State (Where PSE is located)/District Name (Where applicant is posted)* : " + form_details.getPse_state() + " / " + form_details.getPse_district();
                    break;
            }
            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Gem User Subscription Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Date Of Birth* : " + form_details.getSingle_dob() + "               Date Of Retirement* : " + form_details.getSingle_dor(), normalFont));
            if (form_details.getUser_empcode() == null || form_details.getUser_empcode().equals("")) {
                details.add(new Paragraph("9) Designation* : " + form_details.getUser_design(), normalFont));
            } else {
                details.add(new Paragraph("9) Designation* : " + form_details.getUser_design() + "               Employee Code : " + form_details.getUser_empcode(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("10) Telephone Number(O)* : " + form_details.getUser_ophone(), normalFont));
            } else {
                details.add(new Paragraph("10) Telephone Number(O)* : " + form_details.getUser_ophone() + "               Telephone Number(R) : " + form_details.getUser_rphone(), normalFont));
            }
            details.add(new Paragraph("11) Official Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("12) City* : " + form_details.getUser_city() + "           State* : " + form_details.getUser_state() + "           Pin Code* : " + form_details.getUser_pincode(), normalFont));
            details.add(new Paragraph("13) Preferred Email Address 1 1* : " + form_details.getSingle_email1(), normalFont));
            details.add(new Paragraph("14) Preferred Email Address 1 2* : " + form_details.getSingle_email2(), normalFont));
            details.add(new Paragraph("15) Project Monthly Traffic* : " + form_details.getDomestic_traf(), normalFont));
            if (form_details.getUnder_sec_email() != null && !form_details.getUnder_sec_email().equals("")) {
                details.add(new Paragraph("16) Under Secretary Name* : " + form_details.getUnder_sec_name() + "     Email* : " + form_details.getUnder_sec_email(), normalFont));
                details.add(new Paragraph("17) Under Secretary Mobile* : " + form_details.getUnder_sec_mobile() + "     Designation* : " + form_details.getunder_sec_desig(), normalFont));
            }
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. The validity of account will be 12 months from the date of intergration.", normalFont));
            tncpoints.add(new Paragraph("2. The applicant agrees to transfer the fund within 7 days from the date of PI generation by NICSI.", normalFont));
            tncpoints.add(new Paragraph("3. NIC reserves te right to deactivate the account anytime due to non-payment of dues.", normalFont));
            tncpoints.add(new Paragraph("4. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("5. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("6. By not doing so (point no. 4 & 5 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("7. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("8.  Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("9. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("10. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("11. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("12. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("13. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("14. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("15. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("16. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("17. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("18. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("19. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("20. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("21. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));// email modified by pr on 14thjan19
            tncpoints.add(new Paragraph("22. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("23. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("24. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("25. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_imappop(FormBean form_details, String ref_num) {
        System.out.println("generate pdf imappop:::::::::::::::");
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DEPT TAARARRRR " + form_details.getDept());
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Imap POP Update Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email(), normalFont));
            details.add(new Paragraph("4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Protocol selected to be enabled* : " + form_details.getProtocol().toUpperCase(), normalFont));
            document.add(details);
            document.add(emptypara);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal ", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("3. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("4. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("5. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("6. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:  ", normalFont));
            tncpoints.add(new Paragraph("    Trash - 7 days ", normalFont));
            tncpoints.add(new Paragraph("    ProbablySpam – 7 days", normalFont));
            tncpoints.add(new Paragraph("7. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("8. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("9. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.add(new Paragraph("10. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("11. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("12. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("13. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_ldap(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("LDAP Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Name of the Application* : " + form_details.getApp_name() + "                    Application URL* : " + form_details.getApp_url(), normalFont));
            if (form_details.getService_ip() == null || form_details.getService_ip().equals("")) {
                details.add(new Paragraph("9) IP from which you will access LDAP Server : " + form_details.getBase_ip(), normalFont));
            } else {
                details.add(new Paragraph("9) IP from which you will access LDAP Server : " + form_details.getBase_ip() + " , " + form_details.getService_ip(), normalFont));
            }
            details.add(new Paragraph("10) Domain/Group Of People who will access this application* : " + form_details.getDomain(), normalFont));
            if (form_details.getServer_loc().equals("Other")) {
                details.add(new Paragraph("11) Server Location* : " + form_details.getServer_loc_txt(), normalFont));
            } else {
                details.add(new Paragraph("11) Server Location* : " + form_details.getServer_loc(), normalFont));
            }
            details.add(new Paragraph("12) Is the application enabled over https* : " + form_details.getHttps(), normalFont));
            details.add(new Paragraph("13) Is the application security audit cleared* : " + form_details.getAudit(), normalFont));
            if (form_details.getAudit().equals("no")) {
                if (form_details.getLdap_id2() == null || form_details.getLdap_id2().equals("")) {
                    details.add(new Paragraph("14)  Id which will access ldap server* : " + form_details.getLdap_id1(), normalFont));
                } else {
                    details.add(new Paragraph("14)  Id which will access ldap server* : " + form_details.getLdap_id1() + " , " + form_details.getLdap_id2(), normalFont));
                }
            }
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Account Category: Free/ Paid", normalFont));
            billing.add(new Paragraph("If free, on What Basis:", normalFont));
            billing.add(new Paragraph("If paid, Project No. :", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Activation:Application login ID: _______________________ has been activated for the LDAP Authentication Service", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of the In charge"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Concerned Department/ Ministry shall be solely responsible for all the information, contents, data send and received using NIC LDAP Authentication under this Agreement. Concerned Department/ Ministry further acknowledges that it shall be solely responsible and undertake to maintain complete authenticity of the information/data sent and/or received and takes all possible steps and measures to ensure that consistent authentic information is transmitted.", normalFont));
            tncpoints.add(new Paragraph("2. Concerned Department/ Ministry shall keep the account information such as userid, password provided obtained for LDAP Authentication in safe custody to avoid any misue by unauthorised users.", normalFont));
            tncpoints.add(new Paragraph("3. I will take one id per application. ID assigned for authentication with one application will not be used by another application. I understand the risks involved . I hereby authorize NIC support cell to deactivate the id in case of misuse/abuse.", normalFont));
            tncpoints.add(new Paragraph("4. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("5. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_mobile(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Mobile Update Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email(), normalFont));
            details.add(new Paragraph("4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) New Mobile Number* : " + form_details.getCountry_code() + form_details.getNew_mobile(), normalFont));
            document.add(details);
            document.add(emptypara);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("2. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("3. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("4. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("5. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("6. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("7. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_nkn(FormBean form_details, String ref_num, String formtype) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("NKN Email Subscription Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            if (form_details.getUser_empcode() == null || form_details.getUser_empcode().equals("")) {
                details.add(new Paragraph("8) Designation* : " + form_details.getUser_design(), normalFont));
            } else {
                details.add(new Paragraph("8) Designation* : " + form_details.getUser_design() + "               Employee Code : " + form_details.getUser_empcode(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone(), normalFont));
            } else {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone() + "               Telephone Number(R) : " + form_details.getUser_rphone(), normalFont));
            }
            details.add(new Paragraph("10) Official Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("11) City* : " + form_details.getUser_city() + "           State* : " + form_details.getUser_state() + "           Pin Code* : " + form_details.getUser_pincode(), normalFont));
            details.add(new Paragraph("12) Institute Name* : " + form_details.getInst_name(), normalFont));
            details.add(new Paragraph("13) Institute ID* : " + form_details.getInst_id(), normalFont));
            details.add(new Paragraph("14) Name of Project NKN* : " + form_details.getNkn_project(), normalFont));
            if (formtype.equals("nkn_single")) {
                details.add(new Paragraph("15) Date Of Birth* : " + form_details.getSingle_dob() + "               Date Of Retirement* : " + form_details.getSingle_dor(), normalFont));
                details.add(new Paragraph("16) Preferred Email Address 1 1* : " + form_details.getSingle_email1(), normalFont));
                details.add(new Paragraph("17) Preferred Email Address 1 2* : " + form_details.getSingle_email2(), normalFont));
            }
            if (form_details.getUnder_sec_email() != null && !form_details.getUnder_sec_email().equals("")) {
                details.add(new Paragraph("18) Under Secretary Name* : " + form_details.getUnder_sec_name() + "     Name* : " + form_details.getUnder_sec_email(), normalFont));
                details.add(new Paragraph("19) Under Secretary Mobile* : " + form_details.getUnder_sec_mobile() + "     Designation* : " + form_details.getunder_sec_desig(), normalFont));
            }
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("2. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("3. By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("4. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("5.  Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("6. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("7. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("8. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("9. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("10. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("11. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("12. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic).  For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("13. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("14. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("15. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("16. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("17. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("18. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.add(new Paragraph("19. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("20. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("21. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("22. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("23. Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate to NIC about their willingness to retain the id through NIC coordinator prior to retirement.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_relay(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;

            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/
            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Relay Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            if (form_details.getRelay_ip().contains(";")) {
                details.add(new Paragraph("8) Application IP* : " + form_details.getRelay_ip().replaceAll(";", " , "), normalFont));
            } else {
                details.add(new Paragraph("8) Application IP* : " + form_details.getRelay_ip(), normalFont));
            }
            details.add(new Paragraph("9) Application Name* : " + form_details.getRelay_app_name(), normalFont));
            details.add(new Paragraph("10) Name of Division* : " + form_details.getDivision(), normalFont));
            details.add(new Paragraph("11) Operating System (Name, Version) * : " + form_details.getOs(), normalFont));
            if (form_details.getServer_loc().equals("Other")) {
                details.add(new Paragraph("12) Server Location* : " + form_details.getServer_loc_txt(), normalFont));
            } else {
                details.add(new Paragraph("12) Server Location* : " + form_details.getServer_loc(), normalFont));
            }
            details.add(new Paragraph("13) Staging Server? (If yes IP will be allowed maximum for 15 days )* : " + form_details.getIp_staging().toUpperCase(), normalFont));
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Fill one form for one IP only.", normalFont));
            tncpoints.add(new Paragraph("2. If administrator is from outside NIC, please get the form approved by NIC coordinator.", normalFont));
            tncpoints.add(new Paragraph("3. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("4. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_vpn(FormBean form_details, String ref_num, String formtype) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            System.out.println("form_details.getUser_employment()" + form_details.getUser_employment() + "form_details.getMin()" + form_details.getMin() + "form_details.getDept()" + form_details.getDept());
            String final_dept;
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }
            System.out.println("below department");

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();

            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);

            Paragraph emptypara = new Paragraph("\n");

            Paragraph header = new Paragraph();

            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            if (form_details.getReq_for().equals("vpn_renew")) {
                header.add(new Paragraph("VPN Renewal Request Form", boldFont));
            } else if (form_details.getReq_for().equals("vpn_surrender")) {
                header.add(new Paragraph("VPN Surrender Request Form", boldFont));
            } else if (form_details.getReq_for().equals("vpn_delete")) {
                header.add(new Paragraph("VPN Delete Request Form", boldFont));
            } else {
                header.add(new Paragraph("VPN Subscription Form", boldFont));
            }

            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);

            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);

            if (form_details.getReq_for().equals("vpn_renew") || form_details.getReq_for().equals("change_add") || form_details.getReq_for().equals("vpn_surrender") || form_details.getReq_for().equals("vpn_delete")) {
                Paragraph vpn_num = new Paragraph("VPN number :" + form_details.getVpn_reg_no().trim(), boldFont);
                reg_num.setAlignment(vpn_num.ALIGN_RIGHT);
                document.add(vpn_num);
                document.add(emptypara);
            }
            Vpn_registration reg_obj = new Vpn_registration();
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email(), normalFont));
            details.add(new Paragraph("4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));

            // start, code added by pr on 27thaug19
            details.add(new Paragraph("5) Applicant Designation* : " + form_details.getUser_design(), normalFont));

            details.add(new Paragraph("6) Applicant Employee Code : " + (form_details.getUser_empcode() != null ? form_details.getUser_empcode() : "-"), normalFont));

            details.add(new Paragraph("7) Applicant Postal Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("8) Applicant State Of Posting* : " + form_details.getUser_state(), normalFont));
            details.add(new Paragraph("9) Applicant District* : " + form_details.getUser_city(), normalFont));
            details.add(new Paragraph("10) Applicant Pincode* : " + form_details.getUser_pincode(), normalFont));

            // end, code added by pr on 27thaug19
            details.add(new Paragraph("11) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("12) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("13) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            if (form_details.getVpn_coo() != null) {
                if (!form_details.getVpn_coo().equals("")) {
                    details.add(new Paragraph("14) Co-ordinator Email* : " + form_details.getVpn_coo(), normalFont));
                }
            }
            if (form_details.getVpn_coo1() != null) {
                if (!form_details.getVpn_coo1().equals("")) {
                    details.add(new Paragraph("15) Co-ordinator Email* : " + form_details.getVpn_coo1(), normalFont));
                }
            }
            details.add(new Paragraph("NOTE: Details of already allowed ips* : ", normalFont));
            document.add(details);
            document.add(emptypara);
            if (form_details.getReq_for().equals("vpn_renew") || form_details.getReq_for().equals("change_add") || form_details.getReq_for().equals("vpn_surrender") || form_details.getReq_for().equals("vpn_delete")) {
                String api_resp = reg_obj.post_api2(form_details.getVpn_reg_no().trim(), form_details.getUser_email(), form_details.getUser_mobile(), form_details.getMail());

                PdfPTable iptable = null;
                iptable = new PdfPTable(4); // 2 columns.
                PdfPCell cell1 = new PdfPCell(new Paragraph("Server IP", boldFont));
                PdfPCell cell2 = new PdfPCell(new Paragraph("Server Location", boldFont));
                PdfPCell cell3 = new PdfPCell(new Paragraph("Destination Port", boldFont));
                PdfPCell cell4 = new PdfPCell(new Paragraph("Application URL", boldFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                iptable.addCell(cell1);
                iptable.addCell(cell2);
                iptable.addCell(cell3);
                iptable.addCell(cell4);

                JSONObject json = new JSONObject(api_resp);
                JSONArray access = json.getJSONArray("access_details");
                for (int i = 0; i < access.length(); i++) {
                    JSONObject detail = access.getJSONObject(i);
                    String serip = detail.getString("serip");
                    String serloc = detail.getString("serloc");
                    String destport = detail.getString("destport");
                    String desc_service = detail.getString("desc_service");
                    iptable.addCell(new Paragraph(serip, normalFont));
                    iptable.addCell(new Paragraph(serloc, normalFont));
                    iptable.addCell(new Paragraph(destport, normalFont));
                    iptable.addCell(new Paragraph(desc_service, normalFont));
                }

                document.add(iptable);
                document.add(emptypara);
            }

            if (!form_details.getReq_for().equals("vpn_renew") && !form_details.getReq_for().equals("vpn_surrender") && !form_details.getReq_for().equals("vpn_delete")) {
                Paragraph details1 = new Paragraph();
                details1.add(new Paragraph("NOTE: Details of applied ips* : ", normalFont));
                document.add(details1);
                document.add(emptypara);
                Map vpn_data = (HashMap) form_details.getVpn_data();
                PdfPTable iptable1 = null;
                iptable1 = new PdfPTable(5); // 2 columns.
                PdfPCell cell11 = new PdfPCell(new Paragraph("Server IP", boldFont));
                PdfPCell cell12 = new PdfPCell(new Paragraph("Server Location", boldFont));
                PdfPCell cell13 = new PdfPCell(new Paragraph("Destination Port", boldFont));
                PdfPCell cell14 = new PdfPCell(new Paragraph("Application URL", boldFont));
                PdfPCell cell15 = new PdfPCell(new Paragraph("Action Type", boldFont));
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
                iptable1.addCell(cell11);
                iptable1.addCell(cell12);
                iptable1.addCell(cell13);
                iptable1.addCell(cell14);
                iptable1.addCell(cell15);

                Iterator<Map.Entry<String, Map<String, String>>> parent = vpn_data.entrySet().iterator();
                while (parent.hasNext()) {
                    Map.Entry<String, Map<String, String>> parentPair = parent.next();
                    iptable1.addCell(new Paragraph(parentPair.getValue().get("ip"), normalFont));
                    iptable1.addCell(new Paragraph(parentPair.getValue().get("server"), normalFont));
                    iptable1.addCell(new Paragraph(parentPair.getValue().get("dest_port"), normalFont));
                    iptable1.addCell(new Paragraph(parentPair.getValue().get("app_url"), normalFont));
                    iptable1.addCell(new Paragraph(parentPair.getValue().get("action_type"), normalFont));
                }
                document.add(iptable1);
            }
            if (form_details.getReq_for().equals("vpn_delete")) {
                Paragraph details1 = new Paragraph();
                details1.add(new Paragraph("NOTE: Details of Deleted ips* : ", normalFont));
                document.add(details1);
                document.add(emptypara);
                PdfPTable iptable1 = null;
                iptable1 = new PdfPTable(4); // 2 columns.
                PdfPCell cell11 = new PdfPCell(new Paragraph("Server IP", boldFont));
                PdfPCell cell12 = new PdfPCell(new Paragraph("Server Location", boldFont));
                PdfPCell cell13 = new PdfPCell(new Paragraph("Destination Port", boldFont));
                PdfPCell cell14 = new PdfPCell(new Paragraph("Application URL", boldFont));
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                iptable1.addCell(cell11);
                iptable1.addCell(cell12);
                iptable1.addCell(cell13);
                iptable1.addCell(cell14);
                conSlave = DbConnection.getSlaveConnection();
                String sql = "select * from vpn_registration_delete where registration_no=?";
                PreparedStatement pst = null;
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, ref_num);
                ResultSet res = pst.executeQuery();
                while (res.next()) {
                    iptable1.addCell(new Paragraph(res.getString("server_ip"), normalFont));
                    iptable1.addCell(new Paragraph(res.getString("location"), normalFont));
                    iptable1.addCell(new Paragraph(res.getString("port"), normalFont));
                    iptable1.addCell(new Paragraph(res.getString("service"), normalFont));
                }
                res.close();
                document.add(iptable1);
            }

            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);

            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Fill one form for one IP only.", normalFont));
            tncpoints.add(new Paragraph("2. If administrator is from outside NIC, please get the form approved by NIC coordinator.", normalFont));
            tncpoints.add(new Paragraph("3. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_wifi(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;

            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/
            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("WIFI Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
                    + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            document.add(details);
            document.add(emptypara);
            if (form_details.getWifi_process().equals("request")) {
                if (form_details.getWifi_type().equals("nic")) {
                    PdfPTable iptable = new PdfPTable(2); // 4 columns.
                    PdfPCell cell1 = new PdfPCell(new Paragraph("MAC Address of the Device", boldFont));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Operating System of the Device", boldFont));
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    iptable.addCell(cell1);
                    iptable.addCell(cell2);
                    iptable.addCell(new Paragraph(form_details.getWifi_mac1(), normalFont));
                    iptable.addCell(new Paragraph(form_details.getWifi_os1(), normalFont));
                    if (!form_details.getWifi_mac2().equals("") && form_details.getWifi_mac2() != null) {
                        iptable.addCell(new Paragraph(form_details.getWifi_mac2(), normalFont));
                        iptable.addCell(new Paragraph(form_details.getWifi_os2(), normalFont));
                    }
                    if (!form_details.getWifi_mac3().equals("") && form_details.getWifi_mac3() != null) {
                        iptable.addCell(new Paragraph(form_details.getWifi_mac3(), normalFont));
                        iptable.addCell(new Paragraph(form_details.getWifi_os3(), normalFont));
                    }
                    document.add(iptable);
                } else {
                    PdfPTable iptable = new PdfPTable(2); // 4 columns.
                    PdfPCell cell1 = new PdfPCell(new Paragraph("WIFI Request For", boldFont));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Time Duration", boldFont));

                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

                    iptable.addCell(cell1);
                    iptable.addCell(cell2);

                    iptable.addCell(new Paragraph(form_details.getWifi_request(), normalFont));
                    iptable.addCell(new Paragraph(form_details.getWifi_time() + " " + form_details.getWifi_duration(), normalFont));
                    document.add(iptable);
                }
            } //payal agarwal
            else if (form_details.getWifi_process().equals("req_delete")) {
                if (form_details.getWifi_type().equals("nic")) {
                    PdfPTable iptable = new PdfPTable(2); // 4 columns.
                    PdfPCell cell1 = new PdfPCell(new Paragraph("MACC Address of the Device", boldFont));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Operating System of the Device", boldFont));

                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);

                    iptable.addCell(cell1);
                    iptable.addCell(cell2);

                    List<FormBean> macList = form_details.getMac_list();

                    for (FormBean formBean : macList) {
                        iptable.addCell(new Paragraph(formBean.getWifi_mac1(), normalFont));
                        iptable.addCell(new Paragraph(formBean.getWifi_os1(), normalFont));
                    }
                    document.add(iptable);
                } else {
                    PdfPTable iptable = new PdfPTable(2); // 4 columns.
                    PdfPCell cell1 = new PdfPCell(new Paragraph("WIFI Request For", boldFont));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Time Duration", boldFont));

                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    iptable.addCell(cell1);
                    iptable.addCell(cell2);
                    iptable.addCell(new Paragraph(form_details.getWifi_request(), normalFont));
                    iptable.addCell(new Paragraph(form_details.getWifi_time() + " " + form_details.getWifi_duration(), normalFont));
                    document.add(iptable);
                }
            }
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Entries marked with asterisk  (*) are mandatory", normalFont));
            tncpoints.add(new Paragraph("2. Only three devices allowed per user ID.", normalFont));
            tncpoints.add(new Paragraph("3. For iPhone/iPad/MAC, write ios(version) in Operating System.", normalFont));
            tncpoints.add(new Paragraph("4. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_wifiport(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;

            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/
            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("WIFI Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
                    + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", normalFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", normalFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("Source IP* : " + form_details.getSourceip().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Destination IP* : " + form_details.getDestinationip().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Service* : " + form_details.getService().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Port* : " + form_details.getPorts().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Action* : " + form_details.getAction().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("TimePeriod* : " + form_details.getTimeperiod().replaceAll(";", " , "), normalFont));
            details.add(new Paragraph("Purpose* : " + form_details.getRemarks(), normalFont));

            document.add(details);
            document.add(emptypara);

            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", normalFont));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Entries marked with asterisk  (*) are mandatory", normalFont));
            tncpoints.add(new Paragraph("2. Only three devices allowed per user ID.", normalFont));
            tncpoints.add(new Paragraph("3. For iPhone/iPad/MAC, write ios(version) in Operating System.", normalFont));
            tncpoints.add(new Paragraph("4. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_sms(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;

            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/
            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font smallBoldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("SMS Services Request Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont));// line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            if (form_details.getUser_empcode() == null || form_details.getUser_empcode().equals("")) {
                details.add(new Paragraph("8) Designation* : " + form_details.getUser_design(), normalFont));
            } else {
                details.add(new Paragraph("8) Designation* : " + form_details.getUser_design() + "               Employee Code : " + form_details.getUser_empcode(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone(), normalFont));
            } else {
                details.add(new Paragraph("9) Telephone Number(O)* : " + form_details.getUser_ophone() + "               Telephone Number(R) : " + form_details.getUser_rphone(), normalFont));
            }
            details.add(new Paragraph("10) Official Address* : " + form_details.getUser_address(), normalFont));
            details.add(new Paragraph("11) City* : " + form_details.getUser_city() + "           State* : " + form_details.getUser_state() + "           Pin Code* : " + form_details.getUser_pincode(), normalFont));
            if (form_details.getSmsservice1().equals("quicksms")) {
                details.add(new Paragraph("12) SMS Service required* : quickSMS", normalFont));
            } else if (form_details.getSmsservice1().equals("otp")) {
                details.add(new Paragraph("12) SMS Service required* : OTP Service", normalFont));
            } else {
                details.add(new Paragraph("12) SMS Service required* : " + form_details.getSmsservice1(), normalFont));
            }
            if (!form_details.getPull_url().equals("") && !form_details.getPull_keyword().equals("")) {
                if (form_details.getShort_code().equals("")) {
                    details.add(new Paragraph("   a) If PULL, URL path* : " + form_details.getPull_url() + "               b) Keyword* : " + form_details.getPull_keyword(), normalFont));
                } else {
                    details.add(new Paragraph("   a) If PULL, URL path* : " + form_details.getPull_url() + "               b) Keyword* : " + form_details.getPull_keyword(), normalFont));
                    details.add(new Paragraph("   c) Short Code* : " + form_details.getShort_code(), normalFont));
                }
            }
            if (form_details.getSmsservice1().equals("quicksms")) {
                details.add(new Paragraph("13) Name of the Application : QuickSms" + "               Application URL* : https://quicksms.emailgov.in/", normalFont));
                details.add(new Paragraph("14) Server Location : Shastri Park" + "               Purpose of the application : NIC Web Console to send SMS", normalFont));
                details.add(new Paragraph("15) IP form which quicksms will be access : IP1* : " + form_details.getBase_ip() + "               IP2 : " + form_details.getService_ip(), normalFont));
            } else {
                details.add(new Paragraph("13) Name of the Application* : " + form_details.getApp_name() + "               Application URL* : " + form_details.getApp_url(), normalFont));
                if (form_details.getServer_loc().equals("Other")) {
                    details.add(new Paragraph("14) Server Location* : " + form_details.getServer_loc_txt() + "               Purpose of the application : " + form_details.getSms_usage(), normalFont));
                } else {
                    details.add(new Paragraph("14) Server Location* : " + form_details.getServer_loc() + "               Purpose of the application : " + form_details.getSms_usage(), normalFont));
                }
                details.add(new Paragraph("15) IP form which SMS Gateway will be access : IP1* : " + form_details.getBase_ip() + "               IP2 : " + form_details.getService_ip(), normalFont));
            }
            if (form_details.getStaging_ip().equals("")) {
                details.add(new Paragraph("16) IP of Staging Server Required for Testing : NA ", normalFont));
            } else {
                details.add(new Paragraph("16) IP of Staging Server Required for Testing : " + form_details.getStaging_ip(), normalFont));
            }
            if (form_details.getInter_traf().equals("")) {
                details.add(new Paragraph("17) Projected Domestic Monthly SMS traffic* : " + form_details.getDomestic_traf() + "               Projected International Monthly SMS traffic : NA", normalFont));
            } else {
                details.add(new Paragraph("17) Projected Domestic Monthly SMS traffic : " + form_details.getDomestic_traf() + "               Projected International Monthly SMS traffic : " + form_details.getInter_traf(), normalFont));
            }
            if (form_details.getSmsservice1().equals("quicksms")) {
            } else if (form_details.getAudit().equals("No")) {
                details.add(new Paragraph("18) Application security audit cleared* : " + form_details.getAudit() + "               Audit Date by when it will be cleared : " + form_details.getDatepicker1(), normalFont));
            } else {
                details.add(new Paragraph("18) Application security audit cleared* : " + form_details.getAudit(), normalFont));
            }
            if (form_details.getSender().equals("Yes")) {
                details.add(new Paragraph("19) Do you have TRAI exempted Sender Id* : " + form_details.getSender() + "               Sender ID : " + form_details.getSender_id(), normalFont));
            } else {
                details.add(new Paragraph("19) Do you have TRAI exempted Sender Id* : " + form_details.getSender(), normalFont));
            }
            details.add(new Paragraph(""));
            details.add(new Paragraph("Technical Admin Details: ", boldFont));
            details.add(new Paragraph("20) Name* : " + form_details.getT_off_name(), normalFont));
            details.add(new Paragraph("21) Email* : " + form_details.getTauth_email() + "               22) Mobile : " + form_details.getTmobile(), normalFont));
            if (form_details.getTemp_code() == null || form_details.getTemp_code().equals("")) {
                details.add(new Paragraph("23) Designation* : " + form_details.getTdesignation(), normalFont));
            } else {
                details.add(new Paragraph("23) Designation* : " + form_details.getTdesignation() + "               Employee Code : " + form_details.getTemp_code(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("24) Telephone Number(O)* : " + form_details.getTtel_ofc(), normalFont));
            } else {
                details.add(new Paragraph("24) Telephone Number(O)* : " + form_details.getTtel_ofc() + "               Telephone Number(R) : " + form_details.getTtel_res(), normalFont));
            }
            details.add(new Paragraph("25) Official Address* : " + form_details.getTaddrs(), normalFont));
            details.add(new Paragraph("26) City* : " + form_details.getTcity() + "           State* : " + form_details.getTstate() + "           Pin Code* : " + form_details.getTpin(), normalFont));
            details.add(new Paragraph(""));
            details.add(new Paragraph("Billing Owner Details: ", boldFont));
            details.add(new Paragraph("27) Name* : " + form_details.getBauth_off_name(), normalFont));
            details.add(new Paragraph("28) Email* : " + form_details.getBauth_email() + "               29) Mobile : " + form_details.getBmobile(), normalFont));
            if (form_details.getBemp_code() == null || form_details.getBemp_code().equals("")) {
                details.add(new Paragraph("30) Designation* : " + form_details.getBdesignation(), normalFont));
            } else {
                details.add(new Paragraph("30) Designation* : " + form_details.getBdesignation() + "               Employee Code : " + form_details.getBemp_code(), normalFont));
            }
            if (form_details.getUser_rphone() == null || form_details.getUser_rphone().equals("")) {
                details.add(new Paragraph("31) Telephone Number(O)* : " + form_details.getBtel_ofc(), normalFont));
            } else {
                details.add(new Paragraph("31) Telephone Number(O)* : " + form_details.getBtel_ofc() + "               Telephone Number(R) : " + form_details.getBtel_res(), normalFont));
            }
            details.add(new Paragraph("32) Official Address* : " + form_details.getBaddrs(), normalFont));
            details.add(new Paragraph("32) City* : " + form_details.getBcity() + "           State* : " + form_details.getBstate() + "           Pin Code* : " + form_details.getBpin(), normalFont));
            // document.newPage();
            details.add(new Paragraph("*Application must be on HTTPS due to security policy.", smallBoldFont));
            details.add(new Paragraph("*The application will be suspended if security audit certificate is not submitted within the date mentioned above.", smallBoldFont));
            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph("Signature of the Applicant with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Reporting/Nodal/Forwarding Officer with date and seal", normalFont));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            // line commented by MI,suggested by Taha sir
//                        billing.add(new Paragraph("Account Category: Free", normalFont));
//                        billing.add(new Paragraph("NIC Will Not Charge: " + " Apex Org (O/o President/Vice President/Parliament/Cabinet Sectt.)/ PMO/NIC/DeitY", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("Email:________________________________", normalFont));
            billing.add(new Paragraph("Telephone:____________________________Name & Designation:____________________________", normalFont));
            billing.add(new Paragraph("Mobile:_______________________________", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("Signature of NIC Coordinator/Reporting/Nodal/Forwarding Officer/SIO with seal:_______________________________", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("Billing Division(NICSI):", boldFont));
            billing.add(new Paragraph("File Number:________________________________", normalFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:____________________________ ", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("Signature"));
            document.add(billing);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. The ID provided to you is for a single application ONLY. If a user department/division wishes to integrate additional applications with the NIC SMS Gateway, then an id pertaining to that application needs to be created. Id created for application A cannot be used for application B. The user department needs to contact NIC for creation of the id for application B. Any violation detected by the user department will result in termination of the application integration. It is the responsibility of undersigned to ensure the same.", normalFont));
            tncpoints.add(new Paragraph("2. The authorized officer is the officer at the level of undersecretary or above working in Ministries/Departments/Statutory Bodies/Autonomous Bodies of both Central and State/UT Governments.", normalFont));
            tncpoints.add(new Paragraph("3. Concerned Department/Ministry shall be solely responsible for all the information, contents, data send and received using NIC SMS gateway under this agreement. Concerned Department/Ministry further acknowledges that it shall be solely responsible and undertake to maintain complete authenticity of the information/data sent and/or received and takes all possible steps and measures to ensure that consistent authentic information is transmitted.", normalFont));
            tncpoints.add(new Paragraph("4. Concerned Department/ Ministry shall be solely responsible at its own costs and expenses for obtaining and maintaining all necessary approvals, sanctions, permissions, and licenses as for sending and receiving SMS from the relevant authorities and/or regulatory bodies, as the case may be.", normalFont));
            tncpoints.add(new Paragraph("5. International SMS will only be given after approval from Reporting/Nodal/Forwarding Officer.", normalFont));
            tncpoints.add(new Paragraph("6. NIC reserves the right to change any parameter relating integration of application and changing criteria of SMS sent.", normalFont));
            tncpoints.add(new Paragraph("7. NIC will not share the details regarding the traffic or id details with anyone without the due authorization from the Reporting/Nodal/Forwarding Officer of the concerned department", normalFont));
            tncpoints.add(new Paragraph("8. Concerned Department/ Ministry shall , at all times during the Term, fully comply with the regulations and directions issued by TRAI (Telecom Regulatory Authority of India) and the Department of Telecommunications, Government of India, from time to time relating to the duties and obligations of the other service provider under the Agreement", normalFont));
            tncpoints.add(new Paragraph("9. Concerned Department/ Ministry undertakes that it shall be fully responsible and liable for clearance of, in relation to third party, all rights including, but not limited to, copyrights, right to privacy / publicity, etc. in relation to the publicity undertaken by Concerned Department/Ministry as well as acquiring, propagating, publicizing, sharing and/or using the requisite intellectual property rights including trademark and copyrights of any third party for the SMS’s being transmitted by NIC SMS Gateway.", normalFont));
            tncpoints.add(new Paragraph("10. Concerned Department/Ministry shall keep the account information such as userid, password provided, obtained from SMS gateway operations in safe custody to avoid any misuse by unauthorized users.", normalFont));
            tncpoints.add(new Paragraph("11. Any unauthorized commercial use of the services is expressly prohibited and concerned Department/Ministry shall be solely responsible for all acts or omissions that occur under its account or password, including the content of any transmissions through the services and the concerned Department/ Ministry shall strictly not: ", normalFont));
            tncpoints.add(new Paragraph("    a) Use the service in connection with junk Short Messages, spamming or any unsolicited Short Messages (commercial or otherwise).", normalFont));
            tncpoints.add(new Paragraph("    b) Harvest or otherwise collect information about others, including email addresses, without their consent.", normalFont));
            tncpoints.add(new Paragraph("    c) Create a false identity mobile phone address or header, or otherwise attempt to mislead others as to the identity of the sender or the origin of the message.", normalFont));
            tncpoints.add(new Paragraph("    d) Transmit through the Service, associated with the Service or publishing with the Service unlawful, harassing, libelous, abusive, threatening, harmful, vulgar, obscene or otherwise objectionable material of any kind or nature.", normalFont));
            tncpoints.add(new Paragraph("    e) Transmit any material that may infringe the intellectual property rights or other rights of third parties, including trademark, copyright or right of publicity.", normalFont));
            tncpoints.add(new Paragraph("    f) Libel, defame or slander any person, or infringe upon any person's privacy rights.", normalFont));
            tncpoints.add(new Paragraph("    g) Transmit any material that contains viruses, Trojan horses, worms, time bombs; cancel bots, or any other harmful or deleterious programs.", normalFont));
            tncpoints.add(new Paragraph("    h) Interfere with or disrupt networks connected to the Service or violate the regulations, policies or procedures of such networks.", normalFont));
            tncpoints.add(new Paragraph("    i) Attempt to gain unauthorized access to the Service, other accounts, computer systems or networks connected to the Service, through password mining or any other means.", normalFont));
            tncpoints.add(new Paragraph("    j) Interfere with another user's use and enjoyment of the Service or another entity's use and enjoyment of similar services or engage in any other activity that SMS Service Providers believes could subject it to criminal liability or civil penalty or judgment. Concerned Department/ Ministry is fully responsible for the content sent through Short Messages from their respective application or otherwise.", normalFont));
            tncpoints.add(new Paragraph("    k) Send any Short Messages to any numbers listed under ‘Do Not Disturb’ category.", normalFont));
            tncpoints.add(new Paragraph("12. The user agrees to transfer a 6-month advance payment to NICSI based on the projected traffic prior to integration of the application.", normalFont));
            tncpoints.add(new Paragraph("13. SMS Support shall share the billable SMS count. If the undersigned does not give go-ahead/raise query within 05 working days, it will be assumed that the SMS count is certified for bill realization.", normalFont));
            tncpoints.add(new Paragraph("14. The list of chargeable error codes are placed at http://sms.gov.in.", normalFont));
            tncpoints.add(new Paragraph("15. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("16. The department shall abide by the payment terms and conditions of NICSI at all time during and after the usage of SMS gateway services and shall pay all outstanding dues if any.", normalFont));
            tncpoints.add(new Paragraph("17. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_activate(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Single User Subscription Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));
            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Email Address* : " + form_details.getAct_email1(), normalFont));
            details.add(new Paragraph("9) Date of Retirement* : " + form_details.getSingle_dor(), normalFont));

            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("2. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("3. By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("4. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("5. Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("6. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("7. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("8. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("9. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("10. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("11. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("12. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("13. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("14. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("15. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("16. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("17. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("18. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.add(new Paragraph("19. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("20. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("21. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("22. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("23. Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate to NIC about their willingness to retain the id through NIC coordinator prior to retirement.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String GeneratePDF_deactivate(FormBean form_details, String ref_num) {
        FileOutputStream file = null;
        Document document = null;
        PdfWriter writer = null;
        try {
            String final_dept;
            /*switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }*/

            // above code modified by pr on 11thjul19
            switch (form_details.getUser_employment()) {
                case "Central":
                    if (form_details.getDept().equalsIgnoreCase("other")) {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Ministry/Department* : " + form_details.getMin() + " / " + form_details.getDept();
                    }
                    break;
                case "Others":
                case "Psu":
                case "Const":
                case "Nkn":
                    if (form_details.getOrg().equalsIgnoreCase("other")) {
                        final_dept = "1) Organization Name* : " + form_details.getOrg() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) Organization Name* : " + form_details.getOrg();
                    }
                    break;
                default:
                    if (form_details.getState_dept().equalsIgnoreCase("other")) {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getOther_dept();
                    } else {
                        final_dept = "1) State/Department* : " + form_details.getStateCode() + " / " + form_details.getState_dept();
                    }
                    break;
            }

            //file = new FileOutputStream("/eForms/PDF/" + ref_num + ".pdf");
            String filename = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            file = new FileOutputStream(filename);
            //file = new FileOutputStream("F:\\" + ref_num + ".pdf"); // for testing , to be commented in production  on 12thapr18
            document = new Document();
            writer = PdfWriter.getInstance(document, file);
            document.open();
            Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
            Font smallFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
            Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph emptypara = new Paragraph("\n");
            Paragraph header = new Paragraph();
            header.add(new Paragraph("Government of India", HeaderFont));
            header.add(new Paragraph("Ministry of Electronics & Information Technology", HeaderFont));
            header.add(new Paragraph("NATIONAL INFORMATICS CENTRE", HeaderFont));
            header.add(new Paragraph("Single User Subscription Form", boldFont));
            /*header.add(new Paragraph("(In case of manual submission, the completed application form, duly signed by the concerned Project Coordinator"
             + " /Reporting/Nodal/Forwarding Officer of the concerned NIC Cell, should be submitted to Support Center at 'iNOC, A4B2 Bay, A-Block C.G.O. Complex')", smallFont));*/
            header.add(new Paragraph("(In case of manual submission, the completed application form, duly sealed and signed, must be uploaded back "
                    + "to eForms portal by the applicant and his/her Reporting/Nodal/Forwarding Officer)", smallFont)); // line modified by pr on 18thjan19
            header.add(new Paragraph("Please sign on each page. Entries marked asterisk(*) are mandatory", normalFont));
            header.setAlignment(header.ALIGN_CENTER);
            document.add(header);
            document.add(emptypara);
            Paragraph reg_num = new Paragraph("Registration number :" + ref_num, boldFont);
            reg_num.setAlignment(reg_num.ALIGN_RIGHT);
            document.add(reg_num);
            document.add(emptypara);
            Paragraph details = new Paragraph();
            details.add(new Paragraph(final_dept, normalFont));

            details.add(new Paragraph("2) Applicant Name* : " + form_details.getUser_name(), normalFont));
            details.add(new Paragraph("3) Applicant Email* : " + form_details.getUser_email() + "       4) Applicant Mobile* : " + form_details.getUser_mobile(), normalFont));
            details.add(new Paragraph("5) Reporting/Nodal/Forwarding Officer Name* : " + form_details.getHod_name(), normalFont));
            details.add(new Paragraph("6) Reporting/Nodal/Forwarding Officer Email* : " + form_details.getHod_email(), normalFont));
            details.add(new Paragraph("7) Reporting/Nodal/Forwarding Officer Mobile* : " + form_details.getHod_mobile() + "   Designation* : " + form_details.getCa_design(), normalFont));
            details.add(new Paragraph("8) Email Address* : " + form_details.getSingle_email1(), normalFont));
            details.add(new Paragraph("9) Date of Retirement* : " + form_details.getSingle_dor(), normalFont));

            document.add(details);
            Paragraph footer = new Paragraph();
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph("Signature of the Applicant with date and seal     Signature of Reporting/Nodal/Forwarding Officer with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer.add(new Paragraph(""));
            footer.add(new Paragraph(""));
            footer.setAlignment(footer.ALIGN_JUSTIFIED);
            document.add(footer);
            Paragraph footer1 = new Paragraph();
            footer1.add(new Paragraph("Signature of NIC COORDINATOR with date and seal", FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.NORMAL)));
            footer1.setAlignment(footer.ALIGN_RIGHT);
            document.add(footer1);
            document.add(emptypara);
            Paragraph billing = new Paragraph();
            PdfPTable table_user = new PdfPTable(1);
            PdfPCell cell1 = new PdfPCell(new Paragraph("FOR OFFICE USE", boldFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell1.setBackgroundColor(Color.GRAY);
            table_user.addCell(cell1);
            billing.add(table_user);
            billing.add(new Paragraph("Billing Division(RR Section):", boldFont));
            billing.add(new Paragraph("Payment Processed: Yes/ No:", normalFont));
            billing.add(new Paragraph(""));
            billing.add(new Paragraph("User ID Creation:", boldFont));
            billing.add(new Paragraph("Assigned login ID:________________________________Domain:_______________________________", normalFont));
            billing.add(new Paragraph("Remarks(BO/PO):______________________________________________", normalFont));
            document.add(billing);
            document.add(emptypara);
            Paragraph inoc = new Paragraph();
            //inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Signature of iNOC incharge                                                                        Signature of the Operator"));
            inoc.add(new Paragraph(""));
            inoc.add(new Paragraph("Name & Designation:__________________________"));
            inoc.setAlignment(inoc.ALIGN_RIGHT);
            document.add(inoc);
            document.newPage();
            Paragraph tnc = new Paragraph();
            tnc.add(new Paragraph("Terms And Conditions", boldFont));
            tnc.setAlignment(header.ALIGN_CENTER);
            document.add(tnc);
            //document.add(emptypara);
            Paragraph tncpoints = new Paragraph();
            tncpoints.add(new Paragraph("1. Users are requested to keep the given userid and password a secret.", normalFont));
            tncpoints.add(new Paragraph("2. Please change your password at least once in every three months.", normalFont));
            tncpoints.add(new Paragraph("3. By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.", normalFont));
            tncpoints.add(new Paragraph("4. Do not use the Save password option on the browser when you are prompted for it .", normalFont));
            tncpoints.add(new Paragraph("5. Do not use your Government email address to register on public sites.", normalFont));
            tncpoints.add(new Paragraph("6. Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.", normalFont));
            tncpoints.add(new Paragraph("7. Users are requested to install the personal firewall software to secure their machine and e-mail traffic.", normalFont));
            tncpoints.add(new Paragraph("8. Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.", normalFont));
            tncpoints.add(new Paragraph("9. If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.", normalFont));
            tncpoints.add(new Paragraph("10. NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.", normalFont));
            tncpoints.add(new Paragraph("11. NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: https://msgapp.emailgov.in/docs/assets/download/POP.pdf", normalFont));
            tncpoints.add(new Paragraph("12. By default accounts will be given access over WEB only (https://mail.gov.in). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.", normalFont));
            tncpoints.add(new Paragraph("13. NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.", normalFont));
            tncpoints.add(new Paragraph("14. User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.", normalFont));
            tncpoints.add(new Paragraph("15. Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as: Trash - 7 days, ProbablySpam - 7 days", normalFont));
            tncpoints.add(new Paragraph("16. NIC account will be deactivated, if not used for 90 days.", normalFont));
            tncpoints.add(new Paragraph("17. Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.", normalFont));
            tncpoints.add(new Paragraph("18. Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to servicedesk.nic.in", normalFont));
            tncpoints.add(new Paragraph("19. Please note that advance payment is a must for paid users.", normalFont));
            tncpoints.add(new Paragraph("20. NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.", normalFont));
            tncpoints.add(new Paragraph("21. NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.", normalFont));
            tncpoints.add(new Paragraph("22. NIC does not capture any aadhaar related information.", normalFont));
            tncpoints.add(new Paragraph("23. Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate to NIC about their willingness to retain the id through NIC coordinator prior to retirement.", normalFont));
            tncpoints.setAlignment(header.ALIGN_LEFT);
            document.add(tncpoints);
            //document.add(emptypara);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(printlog + "PDF IP exception: " + e.getMessage());
        } finally {
            try {
                if (document != null) {
                    document.close();
                }
                if (file != null) {
                    file.close();
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public Map getUserValues(String email) {

        System.out.println("inise get usr values email:::::::::::" + email);
        Statement st = null;
        ResultSet rs = null;
        //Connection con = null;
        Map profile_values = new HashMap();
        try {
            //        con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            System.out.println("inside get true ldap employee");
            String sql = "select * from user_profile where email in (" + email + ")";
            System.out.println("query::::::::::::::::" + sql + "conSlave" + conSlave);

            st = conSlave.createStatement();

            System.out.println("query::::::::::::::::" + st + "conSlave" + conSlave);
            rs = st.executeQuery(sql);
            if (rs.next()) {
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                profile_values.put("mobile", rs.getString("mobile"));
                profile_values.put("email", rs.getString("email"));
                profile_values.put("cn", rs.getString("name").trim());
                profile_values.put("nicCity", rs.getString("city").trim());
                profile_values.put("desig", rs.getString("designation").trim());
                profile_values.put("postalAddress", rs.getString("address").trim());
                profile_values.put("ophone", rs.getString("ophone").trim());
                profile_values.put("rphone", rs.getString("rphone").trim());
                profile_values.put("state", rs.getString("add_state").trim());
                profile_values.put("emp_code", rs.getString("emp_code").trim());
                profile_values.put("pin", rs.getString("pin").trim());
                profile_values.put("user_employment", rs.getString("employment").trim());
                profile_values.put("min", rs.getString("ministry").trim());
                profile_values.put("dept", rs.getString("department").trim());
                profile_values.put("stateCode", rs.getString("state").trim());
                profile_values.put("Org", rs.getString("organization").trim());
                profile_values.put("other_dept", rs.getString("other_dept").trim());
                profile_values.put("ca_name", rs.getString("ca_name").trim());
                profile_values.put("ca_design", rs.getString("ca_desig").trim());
                profile_values.put("ca_mobile", rs.getString("ca_mobile").trim());
                profile_values.put("ca_email", rs.getString("ca_email").trim());
                profile_values.put("hod_name", rs.getString("hod_name").trim());
                profile_values.put("hod_mobile", rs.getString("hod_mobile").trim());
                profile_values.put("hod_email", rs.getString("hod_email").trim());
                profile_values.put("hod_tel", rs.getString("hod_telephone").trim());

            } else {
                System.out.println("----------------------------Error----------------------------");
                profile_values.put("error", "User Not valid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }

                if (con != null) {
                    // con.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profile_values;
    }

    public boolean UpdateStatusForRetiredEmp(String ref_num, String mobile, String formtype, String check, String email, String name) {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement ps22 = null;
        PreparedStatement ps = null;
        String status = "completed";
        int k = -1;
        String file = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation") + "eSigned_" + ref_num + ".pdf";

        try {
            con = DbConnection.getConnection();
            String qryStr = "INSERT INTO application_type (app_form_type,app_reg_no,app_user_type,app_user_path) values (?,?,?,?)";
            ps = con.prepareStatement(qryStr);
            ps.setString(1, formtype);
            ps.setString(2, ref_num);
            ps.setString(3, check);
            ps.setString(4, file);
            System.out.println(printlog + "PS: " + ps);
            //end here, code modified by MI on 12th APR 18
            int l = ps.executeUpdate();
            String sql = "INSERT INTO status (stat_reg_no,stat_form_type,stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by_email,stat_forwarded_by_mobile,stat_forwarded_by_name,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_by_user, stat_remarks) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, ref_num);
            stmt.setString(2, formtype);
            stmt.setString(3, status);
            stmt.setString(4, "");
            stmt.setString(5, "");
            stmt.setString(6, email);
            stmt.setString(7, mobile);
            stmt.setString(8, name);
            stmt.setString(9, ip);
            stmt.setString(10, pdate);
            stmt.setString(11, "a");
            stmt.setString(12, email);
            stmt.setString(13, "Request completed automatically through eSign");
            System.out.println(printlog + "3rd query of status " + stmt);
            int j = stmt.executeUpdate();
            System.out.println(printlog + "VALUE INSERTED IN STATUS TABLE FOR MANUAL: in UpdateStatusForRetiredEmp " + j);
            if (j > 0) {
                String sql_final_track = "INSERT INTO final_audit_track (registration_no,applicant_email,applicant_mobile,applicant_name,applicant_ip,applicant_datetime,status,form_name,to_email,to_mobile,to_name,to_datetime,app_user_type, applicant_remarks) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                stmt = con.prepareStatement(sql_final_track);
                stmt.setString(1, ref_num);
                stmt.setString(2, email);
                stmt.setString(3, mobile);
                stmt.setString(4, name);
                stmt.setString(5, ip);
                stmt.setString(6, pdate);
                stmt.setString(7, status);
                stmt.setString(8, formtype);
                stmt.setString(9, "");
                stmt.setString(10, "");
                stmt.setString(11, "");
                stmt.setString(12, pdate);
                stmt.setString(13, check);
                stmt.setString(14, "Request completed automatically through eSign");
                System.out.println(printlog + "3rd query of status " + stmt);
                k = stmt.executeUpdate();
                System.out.println(printlog + "VALUE INSERTED IN final_audit_track TABLE FOR MANUAL: " + k);
            }
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps22 != null) {
                try {
                    ps22.close();
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
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (k > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String utcFormat(String date) throws ParseException {

        String modifyDate = "";
        //String formatDate = date.replaceAll("-", "") + "000000Z";
        //String formatDate = date.replaceAll("-", "");
        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyyMMdd");
        String formatedDate = newFormat.format(date1);
        System.out.println("FORMAT::::::" + formatedDate);

        modifyDate = formatedDate + "000000Z";
        return modifyDate;

    }

    private void updateBaseTableWithFilePath(String ref_num) {
        PreparedStatement ps = null;
        String file = ServletActionContext.getServletContext().getInitParameter("retiredOfficersPdfLocation") + ref_num + ".pdf";
        try {
            con = DbConnection.getConnection();
            String sql = "update dor_ext_retired_registration set pdf_path=? where registration_no=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, file);
            ps.setString(2, ref_num);
            System.out.println(printlog + "PST:: " + ps);
            int k = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(printlog + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
