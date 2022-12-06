package com.org.dao;

import com.opencsv.CSVReader;
import com.org.bean.DnsBean;
import com.org.bean.ValidatedDnsBean;
import com.org.connections.DbConnection;
import com.org.utility.Constants;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import validation.validation;

public class DnsDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public void DNS_tab1(FormBean form_details) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile
            String sql = "insert into onlineform_temp_db.dns_registration_tmp (dns_type,old_url,old_ip,domain_new_ip,dns_cname,"
                    + "domain_server,record_mx,record_ptr,record_srv,record_spf,record_txt,ip,record_dmarc,req_for,record_mx1,record_ptr1,service_url,migration_date)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            //pst.setString(1, form_details.getDns_domain());
            pst.setString(1, "NA");
            pst.setString(2, form_details.getDns_url());
            pst.setString(3, form_details.getDns_oldip());
            pst.setString(4, form_details.getDns_newip());
            pst.setString(5, form_details.getDns_cname());
            pst.setString(6, form_details.getDns_loc());
            pst.setString(7, form_details.getRequest_mx());
            pst.setString(8, form_details.getRequest_ptr());
            pst.setString(9, form_details.getRequest_srv());
            pst.setString(10, form_details.getRequest_spf());
            pst.setString(11, form_details.getRequest_txt());
            pst.setString(12, ip);
            pst.setString(13, form_details.getRequest_dmarc());
            pst.setString(14, form_details.getReq_());
            pst.setString(15, form_details.getRequest_mx1());
            pst.setString(16, form_details.getRequest_ptr1());
            pst.setString(17, form_details.getUrl());
            pst.setString(18, form_details.getMigration_date());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 1: " + pst);
            //pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DNS dao tab 1: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String DNS_tab2(FormBean form_details, Map profile_values, String filename, String upload_filename, String rename_filename, int campaignId) {
        boolean errflag = false;
        String newref = "";
        int iResult = -1;
        int jResult = -1;
        int kResult = -1;
        int lResult = -1;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            con.setAutoCommit(false);
            newref = fetchNewRegNumber(con);
            if (!newref.isEmpty()) {
                if (insertIntoBaseTable(con, form_details, profile_values, filename, upload_filename, rename_filename, newref) > 0) {
                    if (form_details.getRequest_type().equals("dns_bulk")) {
                        List<DnsBean> userList = fetchSuccessfulBulkUploadData(campaignId, form_details.getReq_other_add());
                        String domain = "", cname = "", newIp = "";
                        int id = -1;
                        if (form_details.getReq_other_add().isEmpty()) {
                            for (DnsBean dnsData : userList) {
                                domain = dnsData.getDomain();
                                cname = dnsData.getCname();
                                newIp = dnsData.getNew_ip();
                                id = dnsData.getId();
                                iResult = insertIntoUrlTable(newref, domain, id);
                                if (iResult != -1) {
                                    kResult = insertIntoNewIpTable(newref, newIp, id);
                                    lResult = insertIntoCnameTable(newref, cname, id);
                                    if (form_details.getReq_() != null && form_details.getReq_().equalsIgnoreCase("req_modify")) {
                                        jResult = insertIntoOldIpTable(newref, dnsData.getOld_ip(), id);
                                    } else {
                                        jResult = 0;
                                    }
                                } else {
                                    break;
                                }
                            }

                            if (iResult == -1 || jResult == -1 || kResult == -1 || lResult == -1) {
                                errflag = true;
                                newref = "";
                                con.rollback();
                            } else {
                                int i = updateRegNumbersInBulkTable(form_details.getReq_other_add(), newref, campaignId);
                                if (i > 0) {
                                    if (updateDnsCampaign(campaignId, newref) != -1) {
                                        con.commit();
                                        con.setAutoCommit(false);
                                    } else {
                                        errflag = true;
                                        newref = "";
                                        con.rollback();
                                    }
                                } else {
                                    errflag = true;
                                    newref = "";
                                    con.rollback();
                                }
                            }
                        } else {
                            int i = updateRegNumbersInBulkTable(form_details.getReq_other_add(), newref, campaignId);
                            if (i > 0) {
                                if (updateDnsCampaign(campaignId, newref) != -1) {
                                    con.commit();
                                    con.setAutoCommit(false);
                                } else {
                                    errflag = true;
                                    newref = "";
                                    con.rollback();
                                }
                            } else {
                                errflag = true;
                                newref = "";
                                con.rollback();
                            }
                        }
                    } else {
                        StringTokenizer token = new StringTokenizer(form_details.getDns_url(), ";");
                        int count = token.countTokens();
                        for (int j = 0; j <= count; j++) {
                            if (token.hasMoreTokens()) {
                                iResult = insertIntoUrlTable(newref, token.nextToken());
                                if (iResult == -1) {
                                    break;
                                }
                            }
                        }
                        if (iResult != -1) {
                            if (form_details.getReq_() != null && form_details.getReq_().equalsIgnoreCase("req_modify")) {
                                System.out.println("form_details.getDns_oldip()" + form_details.getDns_oldip());
                                StringTokenizer token1 = new StringTokenizer(form_details.getDns_oldip(), ";");
                                int count1 = token1.countTokens();
                                for (int k = 0; k <= count1; k++) {
                                    if (token1.hasMoreTokens()) {
                                        jResult = insertIntoOldIpTable(newref, token1.nextToken());
                                        if (jResult == -1) {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                jResult = 0;
                            }
                            if (form_details.getReq_() != null && (form_details.getReq_().equalsIgnoreCase("req_modify") || form_details.getReq_().equalsIgnoreCase("req_new"))) {
                                System.out.println("form_details.getDns_newip()" + form_details.getDns_newip());
                                StringTokenizer token3 = new StringTokenizer(form_details.getDns_newip(), ";");
                                int count3 = token3.countTokens();
                                for (int k = 0; k <= count3; k++) {
                                    if (token3.hasMoreTokens()) {
                                        kResult = insertIntoNewIpTable(newref, token3.nextToken());
                                        if (kResult == -1) {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                StringTokenizer token3 = new StringTokenizer(form_details.getDns_newip(), ";");
                                int count3 = token3.countTokens();
                                if (count3 == 0) {
                                    for (int k = 0; k <= count3; k++) {
                                        if (token3.hasMoreTokens()) {
                                            kResult = insertIntoNewIpTable(newref, token3.nextToken());
                                            if (kResult == -1) {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    kResult = 0;
                                }
                            }
                            StringTokenizer token2 = new StringTokenizer(form_details.getDns_cname(), ";");
                            int count2 = token2.countTokens();
                            if (count2 != 0) {
                                for (int l = 0; l <= count2; l++) {
                                    if (token2.hasMoreTokens()) {
                                        lResult = insertIntoCnameTable(newref, token2.nextToken());
                                        if (lResult == -1) {
                                            break;
                                        }
                                    }
                                }
                            } else {
                                lResult = 0;
                            }
                        }

                        if (iResult == -1 || jResult == -1 || kResult == -1 || lResult == -1) {
                            errflag = true;
                            newref = "";
                            con.rollback();
                        } else {
                            con.commit();
                        }
                    }
                } else {
                    errflag = true;
                    newref = "";
                    con.rollback();
                }
            } else {
                errflag = true;
                newref = "";
            }
            con.setAutoCommit(true);
        } catch (Exception e) {
            try {
                con.rollback();
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            errflag = true;
            newref = "";
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DNS dao tab 2: " + e.getMessage());
        }

        return newref;
    }

    public String DNS_tab22(FormBean form_details, Map profile_values, String filename, String upload_filename, String rename_filename) {
        //System.out.println("form_details.getRequest_type() in dao" + form_details.getRequest_type());
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Boolean errflag = false;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            // below query updated by pr on 21stapr2020
            String search_regnum = "select registration_no from dns_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(16, dbrefno.length());

                    int last = Integer.parseInt(lastst);
                    newrefno = last + 1;
                    int len = Integer.toString(newrefno).length();
                    if (len == 1) {
                        newref = "000" + newrefno;
                    } else if (len == 2) {
                        newref = "00" + newrefno;
                    } else if (len == 3) {
                        newref = "0" + newrefno;
                    }
                }
                newref = "DNS-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER DNS dao tab 2: " + newref);

                String sql = "insert into dns_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dns_type,record_mx,record_ptr,"
                        + "record_srv,record_spf,record_txt,registration_no,userip,record_dmarc,req_for,ca_desig,form_type,uploaded_filename,renamed_filepath,server_location,record_mx1,record_ptr1,service_url,migration_date) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                if (pst != null) {
                    pst.setString(1, profile_values.get("email").toString());
                    pst.setString(2, profile_values.get("mobile").toString());
                    pst.setString(3, profile_values.get("cn").toString());
                    pst.setString(4, profile_values.get("ophone").toString());
                    pst.setString(5, profile_values.get("rphone").toString());
                    pst.setString(6, profile_values.get("desig").toString());
                    pst.setString(7, form_details.getUser_empcode());
                    pst.setString(8, profile_values.get("postalAddress").toString());
                    pst.setString(9, profile_values.get("nicCity").toString());
                    pst.setString(10, profile_values.get("state").toString());
                    pst.setString(11, profile_values.get("pin").toString());
                    // 20th march
                    pst.setString(12, form_details.getUser_employment().trim());
                    if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                        pst.setString(13, form_details.getMin().trim());   // ministry
                        pst.setString(14, form_details.getDept().trim());  // dept
                        pst.setString(15, form_details.getOther_dept().trim());   // other 
                        pst.setString(16, "");                      // state
                        pst.setString(17, "");   //org
                    } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                        pst.setString(13, "");   // ministry
                        pst.setString(14, "");  // dept
                        pst.setString(15, form_details.getOther_dept().trim());   // other 
                        pst.setString(16, "");                      // state
                        pst.setString(17, form_details.getOrg().trim());   //org
                    } else {
                        pst.setString(13, "");   // ministry
                        pst.setString(14, form_details.getState_dept().trim());  // dept
                        pst.setString(15, form_details.getOther_dept().trim());   // other 
                        pst.setString(16, form_details.getStateCode().trim());                      // state
                        pst.setString(17, "");   //org
                    }
                    pst.setString(18, form_details.getHod_name().trim());
                    pst.setString(19, form_details.getHod_email().trim());
                    pst.setString(20, form_details.getHod_mobile().trim());
                    pst.setString(21, form_details.getHod_tel().trim());

                    if (form_details.getRequest_type().equals("dns_bulk")) {
                        pst.setString(22, "");
                    } else {
                        if (form_details.getDns_url() != null && form_details.getDns_url().contains("gov")) {
                            pst.setString(22, "gov");
                        } else {
                            pst.setString(22, "nic");
                        }
                    }
                    pst.setString(23, form_details.getRequest_mx());
                    pst.setString(24, form_details.getRequest_ptr());
                    pst.setString(25, form_details.getRequest_srv());
                    pst.setString(26, form_details.getRequest_spf());
                    pst.setString(27, form_details.getRequest_txt());
                    pst.setString(28, newref);
                    pst.setString(29, ip);
                    pst.setString(30, form_details.getRequest_dmarc());
                    pst.setString(31, form_details.getReq_());
                    pst.setString(32, form_details.getCa_design().trim());
                    pst.setString(33, form_details.getRequest_type());
                    pst.setString(34, upload_filename);
                    pst.setString(35, rename_filename);
                    pst.setString(36, form_details.getDns_loc());
                    pst.setString(37, form_details.getRequest_mx1());
                    pst.setString(38, form_details.getRequest_ptr1());
                    pst.setString(39, form_details.getUrl());
                    pst.setString(40, form_details.getMigration_date());

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);

                    int i = pst.executeUpdate();

                    if (i > 0) {

                        if (form_details.getRequest_type().equals("dns_bulk")) {
                            List<FormBean> userList = readCSVFile(filename, form_details.getReq_());
                            for (FormBean bulkUser : userList) {
                                bulkUser.setRegistration_no(newref);

                                insertDnsUser(bulkUser, "dns_bulk", form_details.getReq_());
                            }
                        } else {

                            StringTokenizer token = new StringTokenizer(form_details.getDns_url(), ";");
                            int count = token.countTokens();
                            for (int j = 0; j <= count; j++) {
                                if (token.hasMoreTokens()) {
                                    pst = con.prepareStatement("insert into dns_registration_url values(?,?)");
                                    pst.setString(1, newref);
                                    pst.setString(2, token.nextToken());
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);
                                    pst.executeUpdate();
                                }
                            }
                            System.out.println("form_details.getDns_oldip()" + form_details.getDns_oldip());
                            StringTokenizer token1 = new StringTokenizer(form_details.getDns_oldip(), ";");
                            int count1 = token1.countTokens();
                            for (int k = 0; k <= count1; k++) {
                                if (token1.hasMoreTokens()) {
                                    pst = con.prepareStatement("insert into dns_registration_oldip values(?,?)");
                                    pst.setString(1, newref);
                                    pst.setString(2, token1.nextToken());
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);
                                    pst.executeUpdate();
                                }
                            }
                            if (form_details.getDns_newip().startsWith(";")) {
                                form_details.setDns_newip("blank" + form_details.getDns_newip());
                            }
                            if (form_details.getDns_newip().endsWith(";")) {
                                form_details.setDns_newip(form_details.getDns_newip() + "blank");
                            }
                            String[] token3 = form_details.getDns_newip().split(";");
                            for (String a : token3) {
                                pst = con.prepareStatement("insert into dns_registration_newip values(?,?)");
                                pst.setString(1, newref);
                                if (a.equals("blank")) {
                                    a = "";
                                }
                                pst.setString(2, a);
                                pst.executeUpdate();
                            }

                            if (form_details.getDns_cname().startsWith(";")) {
                                form_details.setDns_cname("blank" + form_details.getDns_cname());
                            }
                            if (form_details.getDns_cname().endsWith(";")) {
                                form_details.setDns_cname(form_details.getDns_cname() + "blank");
                            }
                            String[] token2 = form_details.getDns_cname().split(";");
                            for (String a : token2) {
                                pst = con.prepareStatement("insert into dns_registration_cname values(?,?)");
                                pst.setString(1, newref);
                                if (a.equals("blank")) {
                                    a = "";
                                }
                                pst.setString(2, a);
                                pst.executeUpdate();
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            errflag = true;
            newref = "";
            // e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DNS dao tab 2: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (errflag) {
            try {
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from dns_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);
                if (pst != null) {
                    rs1 = pst.executeQuery();
                    if (rs1.next()) {
                        dbrefno = rs1.getString("registration_no");
                    }
                    if (dbrefno == null || dbrefno.equals("")) {
                        newrefno = 1;
                        newref = "000" + newrefno;

                    } else {
                        String lastst = dbrefno.substring(16, dbrefno.length());

                        int last = Integer.parseInt(lastst);
                        newrefno = last + 1;
                        int len = Integer.toString(newrefno).length();
                        if (len == 1) {
                            newref = "000" + newrefno;
                        } else if (len == 2) {
                            newref = "00" + newrefno;
                        } else if (len == 3) {
                            newref = "0" + newrefno;
                        }
                    }
                    newref = "DNS-FORM" + pdate1 + newref;
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER DNS dao tab 2: " + newref);

                    String sql = "insert into dns_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                            + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dns_type,record_mx,record_ptr,"
                            + "record_srv,record_spf,record_txt,registration_no,userip,record_dmarc,req_for,ca_desig,form_type,uploaded_filename,renamed_filepath,server_location,record_mx1,record_ptr1,migration_date) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setString(1, profile_values.get("email").toString());
                        pst.setString(2, profile_values.get("mobile").toString());
                        pst.setString(3, profile_values.get("cn").toString());
                        pst.setString(4, profile_values.get("ophone").toString());
                        pst.setString(5, profile_values.get("rphone").toString());
                        pst.setString(6, profile_values.get("desig").toString());
                        pst.setString(7, profile_values.get("emp_code").toString());
                        pst.setString(8, profile_values.get("postalAddress").toString());
                        pst.setString(9, profile_values.get("nicCity").toString());
                        pst.setString(10, profile_values.get("state").toString());
                        pst.setString(11, profile_values.get("pin").toString());
                        // 20th march
                        pst.setString(12, form_details.getUser_employment().trim());
                        if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                            pst.setString(13, form_details.getMin().trim());   // ministry
                            pst.setString(14, form_details.getDept().trim());  // dept
                            pst.setString(15, form_details.getOther_dept().trim());   // other 
                            pst.setString(16, "");                      // state
                            pst.setString(17, "");   //org
                        } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                            pst.setString(13, "");   // ministry
                            pst.setString(14, "");  // dept
                            pst.setString(15, form_details.getOther_dept().trim());   // other 
                            pst.setString(16, "");                      // state
                            pst.setString(17, form_details.getOrg().trim());   //org
                        } else {
                            pst.setString(13, "");   // ministry
                            pst.setString(14, form_details.getState_dept().trim());  // dept
                            pst.setString(15, form_details.getOther_dept().trim());   // other 
                            pst.setString(16, form_details.getStateCode().trim());                      // state
                            pst.setString(17, "");   //org
                        }
                        pst.setString(18, form_details.getHod_name().trim());
                        pst.setString(19, form_details.getHod_email().trim());
                        pst.setString(20, form_details.getHod_mobile().trim());
                        pst.setString(21, form_details.getHod_tel().trim());

                        if (form_details.getRequest_type().equals("dns_bulk")) {
                            pst.setString(22, "");
                        } else {

                            if (form_details.getDns_domain().equals("gov")) {
                                pst.setString(22, "gov");
                            } else {
                                pst.setString(22, "nic");
                            }
                        }
                        pst.setString(23, form_details.getRequest_mx());
                        pst.setString(24, form_details.getRequest_ptr());
                        pst.setString(25, form_details.getRequest_srv());
                        pst.setString(26, form_details.getRequest_spf());
                        pst.setString(27, form_details.getRequest_txt());
                        pst.setString(28, newref);
                        pst.setString(29, ip);
                        pst.setString(30, form_details.getRequest_dmarc());
                        pst.setString(31, form_details.getReq_());
                        pst.setString(32, form_details.getCa_design().trim());
                        pst.setString(33, form_details.getRequest_type());
                        pst.setString(34, upload_filename);
                        pst.setString(35, rename_filename);
                        pst.setString(36, form_details.getDns_loc());
                        pst.setString(37, form_details.getRequest_mx1());
                        pst.setString(38, form_details.getRequest_ptr1());
                        pst.setString(39, form_details.getUrl());
                        pst.setString(40, form_details.getMigration_date());

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);

                        int i = pst.executeUpdate();
                        pst.close();

                        if (i > 0) {

                            if (form_details.getRequest_type().equals("dns_bulk")) {
                                List<FormBean> userList = readCSVFile(filename, form_details.getReq_());
                                for (FormBean bulkUser : userList) {
                                    bulkUser.setRegistration_no(newref);

                                    insertDnsUser(bulkUser, "dns_bulk", form_details.getReq_());
                                }
                            } else {

                                StringTokenizer token = new StringTokenizer(form_details.getDns_url(), ";");
                                int count = token.countTokens();
                                for (int j = 0; j <= count; j++) {
                                    if (token.hasMoreTokens()) {
                                        pst = con.prepareStatement("insert into dns_registration_url values(?,?)");
                                        pst.setString(1, newref);
                                        pst.setString(2, token.nextToken());
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);
                                        pst.executeUpdate();
                                    }
                                }
                                System.out.println("form_details.getDns_oldip()" + form_details.getDns_oldip());
                                StringTokenizer token1 = new StringTokenizer(form_details.getDns_oldip(), ";");
                                int count1 = token1.countTokens();
                                for (int k = 0; k <= count1; k++) {
                                    if (token1.hasMoreTokens()) {
                                        pst = con.prepareStatement("insert into dns_registration_oldip values(?,?)");
                                        pst.setString(1, newref);
                                        pst.setString(2, token1.nextToken());
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);
                                        pst.executeUpdate();
                                    }
                                }
                                System.out.println("form_details.getDns_newip()" + form_details.getDns_newip());
                                StringTokenizer token3 = new StringTokenizer(form_details.getDns_newip(), ";");
                                int count3 = token3.countTokens();
                                for (int k = 0; k <= count3; k++) {
                                    if (token3.hasMoreTokens()) {
                                        pst = con.prepareStatement("insert into dns_registration_newip values(?,?)");
                                        pst.setString(1, newref);
                                        pst.setString(2, token3.nextToken());
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);
                                        pst.executeUpdate();
                                    }
                                }
                                StringTokenizer token2 = new StringTokenizer(form_details.getDns_cname(), ";");
                                int count2 = token2.countTokens();
                                for (int l = 0; l <= count2; l++) {
                                    if (token2.hasMoreTokens()) {
                                        pst = con.prepareStatement("insert into dns_registration_cname values(?,?)");
                                        pst.setString(1, newref);
                                        pst.setString(2, token2.nextToken());
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);
                                        pst.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // errflag=true;
                newref = "";
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DNS dao tab 2: " + e.getMessage());
            } finally {
                try {
                    if (pst != null) {
                        pst.close();
                    }
                    if (rs1 != null) {
                        rs1.close();
                    }
                    if (con != null) {
                        // con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return newref;
    }

    private String fetchNewRegNumber(Connection con) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from dns_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = con.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(16, dbrefno.length());
                    int last = Integer.parseInt(lastst);
                    newrefno = last + 1;
                    int len = Integer.toString(newrefno).length();
                    if (len == 1) {
                        newref = "000" + newrefno;
                    } else if (len == 2) {
                        newref = "00" + newrefno;
                    } else if (len == 3) {
                        newref = "0" + newrefno;
                    }
                }
                newref = "DNS-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER DNS dao tab 2: " + newref);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newref;
    }

    private int insertIntoBaseTable(Connection con, FormBean form_details, Map profile_values, String filename, String upload_filename, String rename_filename, String newref) {
        PreparedStatement pst = null;
        int i = -1;
        try {
            String sql = "insert into dns_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dns_type,record_mx,record_ptr,"
                    + "record_srv,record_spf,record_txt,registration_no,userip,record_dmarc,req_for,ca_desig,form_type,uploaded_filename,renamed_filepath,server_location,record_mx1,record_ptr1,service_url,migration_date, req_other_record) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            if (pst != null) {
                pst.setString(1, profile_values.get("email").toString());
                pst.setString(2, profile_values.get("mobile").toString());
                pst.setString(3, profile_values.get("cn").toString());
                pst.setString(4, profile_values.get("ophone").toString());
                pst.setString(5, profile_values.get("rphone").toString());
                pst.setString(6, profile_values.get("desig").toString());
                pst.setString(7, form_details.getUser_empcode());
                pst.setString(8, profile_values.get("postalAddress").toString());
                pst.setString(9, profile_values.get("nicCity").toString());
                pst.setString(10, profile_values.get("state").toString());
                pst.setString(11, profile_values.get("pin").toString());
                // 20th march
                pst.setString(12, form_details.getUser_employment().trim());
                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                    pst.setString(13, form_details.getMin().trim());   // ministry
                    pst.setString(14, form_details.getDept().trim());  // dept
                    pst.setString(15, form_details.getOther_dept().trim());   // other 
                    pst.setString(16, "");                      // state
                    pst.setString(17, "");   //org
                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                    pst.setString(13, "");   // ministry
                    pst.setString(14, "");  // dept
                    pst.setString(15, form_details.getOther_dept().trim());   // other 
                    pst.setString(16, "");                      // state
                    pst.setString(17, form_details.getOrg().trim());   //org
                } else {
                    pst.setString(13, "");   // ministry
                    pst.setString(14, form_details.getState_dept().trim());  // dept
                    pst.setString(15, form_details.getOther_dept().trim());   // other 
                    pst.setString(16, form_details.getStateCode().trim());                      // state
                    pst.setString(17, "");   //org
                }
                pst.setString(18, form_details.getHod_name().trim());
                pst.setString(19, form_details.getHod_email().trim());
                pst.setString(20, form_details.getHod_mobile().trim());
                pst.setString(21, form_details.getHod_tel().trim());

                if (form_details.getRequest_type().equals("dns_bulk")) {
                    pst.setString(22, "");
                } else {

                    if (form_details.getDns_url() != null && form_details.getDns_url().contains("gov")) {
                        pst.setString(22, "gov");
                    } else {
                        pst.setString(22, "nic");
                    }
                }
                pst.setString(23, form_details.getRequest_mx());
                pst.setString(24, form_details.getRequest_ptr());
                pst.setString(25, form_details.getRequest_srv());
                pst.setString(26, form_details.getRequest_spf());
                pst.setString(27, form_details.getRequest_txt());
                pst.setString(28, newref);
                pst.setString(29, ip);
                pst.setString(30, form_details.getRequest_dmarc());
                pst.setString(31, form_details.getReq_());
                pst.setString(32, form_details.getCa_design().trim());
                pst.setString(33, form_details.getRequest_type());
                pst.setString(34, upload_filename);
                pst.setString(35, rename_filename);
                pst.setString(36, form_details.getDns_loc());
                pst.setString(37, form_details.getRequest_mx1());
                pst.setString(38, form_details.getRequest_ptr1());
                pst.setString(39, form_details.getUrl());
                pst.setString(40, form_details.getMigration_date());
                pst.setString(41, form_details.getReq_other_add());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DNS dao tab 2: " + pst);

                i = pst.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public List readCSVFile(String filename, String Req_) {
        List<FormBean> userList = new ArrayList<FormBean>();
        CSVReader reader = null;
        try {

            String[] line1;
            reader = new CSVReader(new FileReader(filename), ',', '\"', 1);

            while ((line1 = reader.readNext()) != null) {
                System.out.println("is not equal to null");
                FormBean formbean = new FormBean();
                System.out.println("lin1 length:::" + line1.length);
                //System.out.println("line1[0]"+line1[0]+"line1[1]"+line1[1]+"line1[2]"+line1[2]+"line1[3]"+line1[3]);
                formbean.setDns_url("");
                formbean.setDns_oldip("");
                formbean.setDns_cname("");
                formbean.setDns_newip("");
                formbean.setDns_url(line1[0]);
                formbean.setDns_cname(line1[1]);
                formbean.setDns_newip(line1[2]);

                if (Req_.equals("req_modify")) {

                    System.out.println("inside modify:::" + line1[3]);

                    formbean.setDns_oldip(line1[3]);

                }

                userList.add(formbean);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    private int insertIntoOldIpTable(String regNumber, String oldIp, int dnsId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = -1;
        if (!oldIp.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_oldip (registration_no,oldip,dns_id) values (?,?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, oldIp);
                    ps.setInt(3, dnsId);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            i = 0;
        }
        return i;
    }

    private int insertIntoOldIpTable(String regNumber, String oldIp) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = -1;
        if (!oldIp.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_oldip (registration_no,oldip) values (?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, oldIp);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            i = 0;
        }
        return i;
    }

    public int updateOldIpTable(DnsBean dnsEditData) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = -1;
        try {
            con = DbConnection.getConnection();
            String sql = "update dns_registration_oldip set oldip = ? where dns_id = ?";
            ps = con.prepareStatement(sql);
            if (ps != null) {
                ps.setString(1, dnsEditData.getOld_ip());
                ps.setInt(2, dnsEditData.getId());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                i = ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    private int insertIntoNewIpTable(String regNumber, String newIp, int dnsId) {
        PreparedStatement ps = null;
        int i = -1;
        if (!newIp.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_newip (registration_no,newip,dns_id) values (?,?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, newIp);
                    ps.setInt(3, dnsId);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            i = 0;
        }
        return i;
    }

    private int insertIntoNewIpTable(String regNumber, String newIp) {
        PreparedStatement ps = null;
        int i = -1;
        if (!newIp.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_newip (registration_no,newip) values (?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, newIp);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            i = 0;
        }
        return i;
    }

    public int updateNewIpTable(DnsBean dnsEditData) {
        PreparedStatement ps = null;
        int i = -1;

        try {
            con = DbConnection.getConnection();
            String sql = "update dns_registration_newip set newip=? where dns_id=?";
            ps = con.prepareStatement(sql);
            if (ps != null) {
                ps.setString(1, dnsEditData.getNew_ip());
                ps.setInt(2, dnsEditData.getId());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                i = ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return i;
    }

    private int insertIntoCnameTable(String regNumber, String cname, int dnsId) {
        PreparedStatement ps = null;
        int i = -1;
        if (!cname.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_cname (registration_no,cname,dns_id) values (?,?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, cname);
                    ps.setInt(3, dnsId);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            i = 0;
        }
        return i;
    }

    private int insertIntoCnameTable(String regNumber, String cname) {
        PreparedStatement ps = null;
        int i = -1;
        if (!cname.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_cname (registration_no,cname) values (?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, cname);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            i = 0;
        }
        return i;
    }

    public int updateCnameTable(DnsBean dnsEditData) {
        PreparedStatement ps = null;
        int i = -1;

        try {
            con = DbConnection.getConnection();
            String sql = "update dns_registration_cname set cname=? where dns_id=?";
            ps = con.prepareStatement(sql);
            if (ps != null) {
                ps.setString(1, dnsEditData.getCname());
                ps.setInt(2, dnsEditData.getId());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                i = ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return i;
    }

    private int insertIntoUrlTable(String regNumber, String dnsUrl, int dnsId) {
        PreparedStatement ps = null;
        int i = -1;
        if (!dnsUrl.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_url (registration_no,dns_url,dns_id) values (?,?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, dnsUrl);
                    ps.setInt(3, dnsId);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    private int insertIntoUrlTable(String regNumber, String dnsUrl) {
        PreparedStatement ps = null;
        int i = -1;
        if (!dnsUrl.isEmpty()) {
            try {
                con = DbConnection.getConnection();
                String sql = "insert into dns_registration_url (registration_no,dns_url) values (?,?)";
                ps = con.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, regNumber);
                    ps.setString(2, dnsUrl);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                    i = ps.executeUpdate();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }

    public int updateUrlTable(DnsBean dnsEditData) {
        PreparedStatement ps = null;
        int i = -1;
        try {
            con = DbConnection.getConnection();
            String sql = "update dns_registration_url set dns_url = ? where dns_id = ?";
            ps = con.prepareStatement(sql);
            if (ps != null) {
                ps.setString(1, dnsEditData.getDomain());
                ps.setInt(2, dnsEditData.getId());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                i = ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return i;
    }

    private void insertDnsUser(FormBean bu, String form_type, String Req_) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            System.out.println("bu new ip_________________insert user " + bu.getDns_newip());
            //Connection con = null;

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            if (Req_.equals("req_modify")) {
                if (!bu.getDns_oldip().trim().equals("")) {
                    String sql = "insert into dns_registration_oldip(registration_no,oldip)"
                            + "values (?, ?)";
                    ps = con.prepareStatement(sql);
                    if (ps != null) {
                        ps.setString(1, bu.getRegistration_no());
                        ps.setString(2, bu.getDns_oldip());
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + ps);
                        ps.executeUpdate();
                    }
                }
            }
            System.out.println("bu.getDns_cname()" + bu.getDns_cname());
            if (!bu.getDns_cname().trim().equals("")) {
                System.out.println("inside here");
                String sql1 = "insert into dns_registration_cname(registration_no,cname)"
                        + "values (?, ?)";
                ps = con.prepareStatement(sql1);
                if (ps != null) {
                    ps.setString(1, bu.getRegistration_no());
                    ps.setString(2, bu.getDns_cname());
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql1 " + "e: " + ps);
                    ps.executeUpdate();
                }

            }
            System.out.println("bu.getDns_url()" + bu.getDns_url());
            if (!bu.getDns_url().trim().equals("")) {
                String sql2 = "insert into dns_registration_url(registration_no,dns_url)"
                        + "values (?, ?)";
                ps = con.prepareStatement(sql2);
                if (ps != null) {
                    ps.setString(1, bu.getRegistration_no());
                    ps.setString(2, bu.getDns_url());
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql2 " + "e: " + ps);
                    ps.executeUpdate();
                }

            }
            if (!bu.getDns_newip().trim().equals("")) {
                System.out.println("bu.getDns_newip()" + bu.getDns_newip());
                String sql3 = "insert into dns_registration_newip(registration_no,newip)"
                        + "values (?, ?)";
                ps = con.prepareStatement(sql3);
                if (ps != null) {
                    ps.setString(1, bu.getRegistration_no());
                    ps.setString(2, bu.getDns_newip());
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql3 " + "e: " + sql3);
                    ps.executeUpdate();
                }
            }
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
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //
    //

    public Map validateBulkDnsCSVFile(String renamed_filepath, String whichform, String req_) {
        Map ExcelValidate = new HashMap();
        CSVReader reader = null;
        String d = "";
        boolean file_flag = true;
        try {
            FormBean formbean = new FormBean();
            validation valid = new validation();
            ArrayList errorList = new ArrayList();    // error Arraylist
            ArrayList validUsers = new ArrayList();    // users without errors
            ArrayList notvalidUsers = new ArrayList();    // users with errors
            reader = new CSVReader(new FileReader(renamed_filepath));
            String[] line;
            int counter = 0;
            while ((line = reader.readNext()) != null) {
                counter++;
            }
            reader.close();
            if (counter == 0) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "FILE IS EMPTY");
                ExcelValidate.put("errorMessage", "File seems empty.");
            } else {
                int count = 1;
                reader = new CSVReader(new FileReader(renamed_filepath));
                String[] line1 = reader.readNext();
                while ((line1 = reader.readNext()) != null) {
                    System.out.println("lin1 size:::::::" + line1.length);
                    boolean flag = true;
                    formbean.setDns_url("");
                    formbean.setDns_oldip("");
                    formbean.setDns_cname("");
                    formbean.setDns_newip("");

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "req_ value" + req_);
                    formbean.setDns_url(line1[0]);
                    if (req_.equals("req_modify")) {
                        if (line1.length > 1) {
                            if (line1[2] != null) {
                                formbean.setDns_newip(line1[2].trim());
                            }
                            if (line1[1] != null) {
                                formbean.setDns_cname(line1[1].trim());
                            }
                            if (line1[3] != null) {
                                formbean.setDns_oldip(line1[3].trim());
                            }

                            if (!line1[3].isEmpty()) {
                                if (valid.dnsipvalidation(formbean.getDns_oldip().trim())) {
                                    errorList.add("Enter the OLD IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                    flag = false;
                                }
                            }

                        }

                    } else {
                        formbean.setDns_url(line1[0]);
                        if (line1.length > 1 && line1[1] != null || !line1[1].equals("")) {
                            formbean.setDns_cname(line1[1].trim());
                            // do something
                        }

                        if (line1.length > 1 && line1[2] != null || !line1[2].equals("")) {
                            formbean.setDns_newip(line1[2].trim());
                        }

                    }
                    if (req_.equals("req_modify")) {
                        if (line1.length > 1 && !line1[3].equals("")) {
                            boolean dns_oldip_error = valid.dnsipvalidation(formbean.getDns_oldip().trim());
                            if (dns_oldip_error == true) {
                                errorList.add("Enter the OLD IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                flag = false;
                            }
                        }

                    }
                    String ReturnString = "";
                    if (req_.equals("req_modify")) {
                        if (!line1[2].isEmpty()) {
                            if (line1[2].startsWith("164.100") || line1[2].startsWith("14.139") || line1[2].startsWith("180.149")) {

                                ReturnString = checkIP(line1[2]);
                            }
                        }
                        if (ReturnString != "") {
                            errorList.add("Domain already exist with this ip");
                            flag = false;
                        }

                    } else if (req_.equals("req_new")) {
                        if (!line1[2].isEmpty()) {
                            if (line1[2].startsWith("164.100") || line1[2].startsWith("14.139") || line1[2].startsWith("180.149")) {

                                ReturnString = checkIP(line1[2]);
                            }
                        }
                        if (ReturnString != "") {
                            errorList.add("Third level domain already exist with this ip");
                            flag = false;
                        }
                        int dotCount = countChar(line1[0], '.');
                        if (dotCount <= 2) {

                            ReturnString = checkDomain(line1[0]);
                            if (ReturnString.startsWith("164.100") || ReturnString.startsWith("14.139") || ReturnString.startsWith("180.149")) {
                                if (ReturnString != "") {
                                    errorList.add("Ip already exist with this domain." + line1[0]);
                                    flag = false;
                                }
                            }

                        }
                    }
                    System.out.println("line1.length" + line1.length + "line1 value" + line1[1]);

                    if (line1.length > 1 && !line1[1].equals("")) {

                        boolean dns_loc_error = valid.cnamevalidation(formbean.getDns_cname().trim());
                        if (dns_loc_error == true) {
                            errorList.add("Enter the CNAME [e.g.: demo.nic.in or demo.gov.in] allowed");
                            flag = false;
                        }
                    }
                    if (line1.length > 1 && !line1[2].equals("")) {

                        boolean dns_ip_error = valid.dnsipvalidation(formbean.getDns_newip().trim());
                        if (dns_ip_error == true) {
                            errorList.add("Enter the IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                            flag = false;
                        }
                    }

                    if (formbean.getDns_url() != null || formbean.getDns_url() != "") {

                        boolean dns_url_error = valid.dnsurlvalidation(formbean.getDns_url().trim());
                        if (dns_url_error == true) {
                            errorList.add("Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]");
                            flag = false;
                        }
                    }

                    String url = line1[0];
                    String[] a = url.split(".");
                    int lastIndex, secondLastIndex;
                    lastIndex = url.lastIndexOf('.');
                    secondLastIndex = url.lastIndexOf('.', lastIndex - 1);
                    int count_char = countChar(url, '.');
                    if (count_char >= 3) {
                        d = url.substring(secondLastIndex + 1, lastIndex) + url.substring(lastIndex, url.length());
                        if (d.equals("gov.in")) {

                            errorList.add("Please come through from registry.gov.in.");
                            flag = false;
                        }
                    }

                    HashMap hm = new LinkedHashMap();
                    System.out.println("flag  in validate :::" + flag);
                    if (flag) {   // the row is ok with no error
                        hm.clear();
                        hm.put("Domain", formbean.getDns_url());
                        hm.put("Cname", formbean.getDns_cname());
                        hm.put("NEW Ip", formbean.getDns_newip());

                        if (req_.equals("req_modify")) {
                            System.out.println("inside request modify");
                            hm.put("OLD Ip", formbean.getDns_oldip());
                        }
                        System.out.println("hm:::::::::::::::::" + hm);
                        validUsers.add(hm);
                    } else {      // row has error
                        hm.clear();
                        hm.put("Domain", formbean.getDns_url());
                        hm.put("Cname", formbean.getDns_cname());
                        hm.put("NEW Ip", formbean.getDns_newip());
                        hm.put("OLD Ip", formbean.getOld_ip());
                        notvalidUsers.add(hm);
                    }
                    count++;
                    ExcelValidate.put("validUsers", validUsers);
                    ExcelValidate.put("notvalidUsers", notvalidUsers);
                    ExcelValidate.put("errorList", errorList);

                }
                reader.close();
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERRORLIST: " + errorList);
            }

        } catch (Exception e) {

            file_flag = false;
            e.printStackTrace();
        }

        if (file_flag == false) {
            ExcelValidate.put("errorMessage", "Please Read instructions carefully and upload file in correct format.");

        } else {

        }

        return ExcelValidate;
    }

    public void DNS_tab3(FormBean form_details) {
        System.out.println("in tab-3");
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into onlineform_temp_db.dns_registration_tmp (dns_type,old_url,old_ip,domain_new_ip,dns_cname,"
                    + "domain_server,record_mx,record_ptr,record_srv,record_spf,record_txt,ip,record_dmarc,req_for,uploaded_filename,renamed_filepath)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            if (pst != null) {
                //pst.setString(1, form_details.getDns_domain());
                pst.setString(1, "NA");
                pst.setString(2, form_details.getDns_url());
                pst.setString(3, form_details.getDns_oldip());
                pst.setString(4, form_details.getDns_newip());
                pst.setString(5, form_details.getDns_cname());
                pst.setString(6, form_details.getDns_loc());
                pst.setString(7, form_details.getRequest_mx());
                pst.setString(8, form_details.getRequest_ptr());
                pst.setString(9, form_details.getRequest_srv());
                pst.setString(10, form_details.getRequest_spf());
                pst.setString(11, form_details.getRequest_txt());
                pst.setString(12, ip);
                pst.setString(13, form_details.getRequest_dmarc());
                pst.setString(14, form_details.getReq_());
                pst.setString(15, form_details.getUploaded_filename());
                pst.setString(16, form_details.getRenamed_filepath());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
                //pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String checkDomain(String url) {
        String ReturnString = "";
        try {
            InetAddress yes = java.net.InetAddress.getByName(url);
            ReturnString = yes.getHostAddress();
            System.out.println("ReturnString in fetch domain" + ReturnString);
        } catch (Exception ex) {
            if (ex.getMessage().contains("unknown error")) {
                ReturnString = "";
            }
        }
        return ReturnString;
    }

    public String checkIP(String ip) {
        String ReturnString = "";
        System.out.println("inside check ip");
        ReturnString = getDomainName(ip);
        int dotCount = 0;
        if (ReturnString.equals("")) {
            DnsDao dnsDao = new DnsDao();
            ReturnString = dnsDao.dbReverseDNSLookup(ip);
            dotCount = countChar(ReturnString, '.');
            if (dotCount <= 2 && !ReturnString.equals("")) {
            } else {
                ReturnString = "";
            }
        } else {
            dotCount = countChar(ReturnString, '.');
            if (dotCount > 2) {
                ReturnString = "";
            }
        }
        return ReturnString;
    }

    public String getDomainName(String ip) {
        String cmd = "dig -x " + ip + " +short";
        return digHostName(cmd);
    }

    private String digHostName(String cmd) {
        String s = null;
        String hostString = "";
        boolean flag = false;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                flag = true;
                hostString += s + ",";
            }
            if (!flag) {
                hostString = "";
            }

            // read any errors from the attempted command
//			while ((s = stdError.readLine()) != null) {
//				System.out.println(s);
//			}
// 
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            s = "";
            //e.printStackTrace();
        }
        hostString = hostString.replaceAll("\\s*,\\s*$", "");
        return hostString;
    }

    public String dbReverseDNSLookup(String ip) {
        System.out.println("inside db server dns lookup");
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String domainName = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String search_regnum = "select status,to_datetime,registration_no from final_audit_track where registration_no in (select registration_no from dns_registration_newip where newip= '" + ip + "') and status = 'completed' order by registration_no desc";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                LinkedHashSet<String> statusTimeStamp = new LinkedHashSet<>();
                while (rs1.next()) {
                    statusTimeStamp.add(rs1.getString("registration_no"));
                }

                if (statusTimeStamp.size() > 0) {
                    for (String regNumber : statusTimeStamp) {
                        String reqType = checkDnsRequestType(regNumber);
                        if (reqType.equals("req_delete")) {
                            domainName = "";
                        } else {
                            domainName = fetchUrl(regNumber);
                            if (domainName.equals("notfound")) {
                                domainName = "";
                            } else {
                                String fetchedIp = fetchMappedIpWithDomain(domainName);
                                if (!fetchedIp.equalsIgnoreCase(ip)) {
                                    domainName = "";
                                } else {
                                    //verify IP through nslookup
                                    String ipFromNslookup = checkDomain(domainName);
                                    if (!ipFromNslookup.equalsIgnoreCase(ip)) {
                                        domainName = "";
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return domainName;
    }

    public String fetchMappedIpWithDomain(String ReturnString) {
        System.out.println("inside db server finding mapped IP");
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String domainName = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String search_regnum = "select status,to_datetime,registration_no from final_audit_track where registration_no in (select registration_no from dns_registration_url where dns_url= '" + ReturnString + "') and status = 'completed' order by registration_no desc";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                LinkedHashSet<String> statusTimeStamp = new LinkedHashSet<>();
                while (rs1.next()) {
                    statusTimeStamp.add(rs1.getString("registration_no"));
                }

                if (statusTimeStamp.size() > 0) {
                    for (String regNumber : statusTimeStamp) {
                        String reqType = checkDnsRequestType(regNumber);
                        if (reqType.equals("req_new") || reqType.equals("req_modify")) {
                            domainName = fetchIp(regNumber);
                            if (domainName.equals("notfound")) {
                                domainName = "";
                            }
                        }
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return domainName;
    }

    public String fetchIp(String regNumber) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "notfound";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String search_regnum = "select newip from dns_registration_newip where registration_no = '" + regNumber + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("newip");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String fetchUrl(String regNumber) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "notfound";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String search_regnum = "select dns_url from dns_registration_url where registration_no = '" + regNumber + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("dns_url");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int countChar(String str, char c) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }

        return count;
    }

    private String checkDnsRequestType(String regNumber) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "notfound";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String search_regnum = "select req_for from dns_registration where registration_no = '" + regNumber + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("req_for");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean checkDnsEformsFinalMapping(String url, String flag) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (flag.equalsIgnoreCase("url")) {
                search_regnum = "select ip from dns_final_mapping where  domain = '" + url + "'";
            } else if (flag.equalsIgnoreCase("ip")) {
                search_regnum = "select domain from dns_final_mapping where ip = '" + url + "'";
            }

            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String checkDnsDataFromDnsAdminFinalMapping(String url, String flag) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (flag.equalsIgnoreCase("url")) {
                search_regnum = "select ip from dns_team_data where  domain = '" + url + "' and delete_status = 0";
            } else if (flag.equalsIgnoreCase("ip")) {
                search_regnum = "select domain from dns_team_data where ip = '" + url + "' and delete_status = 0";
            }
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    if (flag.equalsIgnoreCase("url")) {
                        result = rs1.getString("ip");
                        break;
                    } else if (flag.equalsIgnoreCase("ip")) {
                        result = rs1.getString("domain");
                        if (isThirdLevelDomain(result)) {
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean isThirdLevelDomain(String domain) {
        int count_char = countChar(domain, '.');
        if (count_char == 3 && domain.startsWith("www.")) {
            return true;
        } else if (count_char < 3) {
            return true;
        } else {
            return false;
        }
    }

    private Set<String> fetchOwnersFromDnsEformsFinalMapping(String url) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Set<String> result = new HashSet<>();
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            search_regnum = "select applicant_email from dns_final_mapping where domain = '" + url + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    result.add(rs1.getString("applicant_email"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Set<String> fetchOwnersFromDnsAdminFinalMapping(String url) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Set<String> result = new HashSet<>();
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            search_regnum = "select applicant_email from dns_team_data where domain = '" + url + "' and delete_status = 0";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    result.add(rs1.getString("applicant_email"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Set<String> fetchOwner(String url) {
        Set<String> completedRegNumbers = fetchCompletedDnsRegistrationNumbers(url, "url");
        Set<String> ownersFromEformsTable = new HashSet<>();
        if (!completedRegNumbers.isEmpty()) {
            for (String regNo : completedRegNumbers) {
                ownersFromEformsTable.add(fetchApplicant(regNo));
            }
        }
        Set<String> ownersFromDnsTeamTable = fetchOwnersFromDnsAdminFinalMapping(url);
        Set<String> result = new HashSet<>();
        result.addAll(ownersFromEformsTable);
        result.addAll(ownersFromDnsTeamTable);
        return result;
    }

    public String fetchApplicant(String ref) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String search_regnum = "select auth_email from dns_registration where registration_no = '" + ref + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("auth_email");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Set<String> fetchCompletedDnsRegistrationNumbers(String url, String flag) {
        Set<String> regNumbers = fetchRegNumbers(url, flag);
        if (!regNumbers.isEmpty()) {
            return fetchCompletedRegNumbers(regNumbers);
        } else {
            return new HashSet<>();
        }
    }

    public TreeSet<String> checkInBaseTable(Set<String> completedRegNumbers) {
        String commaSeparatedRegNumbers = "";
        for (String regNumber : completedRegNumbers) {
            commaSeparatedRegNumbers += "'" + regNumber + "',";
        }

        commaSeparatedRegNumbers = commaSeparatedRegNumbers.replaceAll("\\s*,\\s*$", "");

        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String search_regnum = "";
        TreeSet<String> result = new TreeSet<>();
        boolean flag = true;
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (!commaSeparatedRegNumbers.isEmpty()) {
                search_regnum = "select registration_no,req_for from dns_registration where registration_no in (" + commaSeparatedRegNumbers + ")";
                System.out.println("QUERY ::: " + search_regnum);
                pst = conSlave.prepareStatement(search_regnum);
                if (pst != null) {
                    rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        String r = rs1.getString("registration_no") + ":" + rs1.getString("req_for");
                        result.add(r);
                    }
//                if (result.first().equalsIgnoreCase("req_delete")) {
//                    flag = false;
//                }
                }
            } else {
                flag = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Set<String> fetchRegNumbers(String url, String flag) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Set<String> result = new HashSet<>();
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (flag.equalsIgnoreCase("url")) {
                search_regnum = "select registration_no from dns_registration_url where dns_url = '" + url + "'";
            } else if (flag.equalsIgnoreCase("ip")) {
                search_regnum = "select registration_no from dns_registration_newip where newip = '" + url + "'";
            }
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    result.add(rs1.getString("registration_no"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Set<String> fetchCompletedRegNumbers(Set<String> regNumbers) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Set<String> result = new HashSet<>();
        String search_regnum = "";
        String commaSeparatedRegNumbers = "";
        for (String regNumber : regNumbers) {
            commaSeparatedRegNumbers += "'" + regNumber + "',";
        }

        commaSeparatedRegNumbers = commaSeparatedRegNumbers.replaceAll("\\s*,\\s*$", "");

        try {
            conSlave = DbConnection.getSlaveConnection();
            if (!commaSeparatedRegNumbers.isEmpty()) {
                search_regnum = "select registration_no from final_audit_track where status = 'completed' and registration_no in (" + commaSeparatedRegNumbers + ")";
                System.out.println("QUERY ::: " + search_regnum);
                pst = conSlave.prepareStatement(search_regnum);
                if (pst != null) {
                    rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        result.add(rs1.getString("registration_no"));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String fetchMappedIp(String regNo) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            search_regnum = "select newip from dns_registration_newip where registration_no = '" + regNo + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("newip");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String fetchMappedDomain(String regNo) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";
        String search_regnum = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            search_regnum = "select dns_url from dns_registration_url where registration_no = '" + regNo + "'";
            System.out.println("QUERY ::: " + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
            if (pst != null) {
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("dns_url");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void addValidDataInBulkTable(FormBean form_details, String validUser, String renamed_filepath, Map profile_values, int random) {
        System.out.println("addDataInBulkTable::: " + form_details.getDns_url());
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into dns_bulk_upload (campaign_id,domain,cname,new_ip,old_ip) values (?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            if (pst != null) {
                pst.setInt(1, random);
                pst.setString(2, form_details.getDns_url());
                pst.setString(3, form_details.getDns_cname());
                pst.setString(4, form_details.getDns_newip());
                pst.setString(5, form_details.getDns_oldip());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addInvalidDataInBulkTable(FormBean form_details, String errMsg, String renamed_filepath, Map profile_values, int random) {
        System.out.println("addInvalidDataInBulkTable::: " + form_details.getDns_url());
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile
            String sql = "insert into dns_bulk_upload (campaign_id,domain,cname,new_ip,old_ip,dns_error,error_status) values (?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            if (pst != null) {
                pst.setInt(1, random);
                pst.setString(2, form_details.getDns_url());
                pst.setString(3, form_details.getDns_cname());
                pst.setString(4, form_details.getDns_newip());
                pst.setString(5, form_details.getDns_oldip());
                pst.setString(6, errMsg);
                pst.setString(7, "1");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addValidRecordsInBulkTable(DnsBean dnsData, int random, String tableName) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            String sql = "insert into " + tableName + " ";
            switch (dnsData.getReq_other_add()) {
                case "cname":
                    sql += "(campaign_id,domain,old_cname,cname,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_cname_txt().isEmpty()) ? "N/A" : dnsData.getOld_cname_txt());
                        pst.setString(4, (dnsData.getCname_txt().isEmpty()) ? "N/A" : dnsData.getCname_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "mx":
                    sql += "(campaign_id,domain,old_mx,mx,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_mx_txt().isEmpty()) ? "N/A" : dnsData.getOld_mx_txt());
                        pst.setString(4, (dnsData.getMx_txt().isEmpty()) ? "N/A" : dnsData.getMx_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "txt":
                    sql += "(campaign_id,domain,old_txt,txt,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_txt_txt().isEmpty()) ? "N/A" : dnsData.getOld_txt_txt());
                        pst.setString(4, (dnsData.getTxt_txt().isEmpty()) ? "N/A" : dnsData.getTxt_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "ptr":
                    sql += "(campaign_id,domain,old_ptr,ptr,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_ptr_txt().isEmpty()) ? "N/A" : dnsData.getOld_ptr_txt());
                        pst.setString(4, (dnsData.getPtr_txt().isEmpty()) ? "N/A" : dnsData.getPtr_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "spf":
                    sql += "(campaign_id,domain,old_spf,spf,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_spf_txt().isEmpty()) ? "N/A" : dnsData.getOld_spf_txt());
                        pst.setString(4, (dnsData.getSpf_txt().isEmpty()) ? "N/A" : dnsData.getSpf_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "srv":
                    sql += "(campaign_id,domain,old_srv,srv,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_srv_txt().isEmpty()) ? "N/A" : dnsData.getOld_srv_txt());
                        pst.setString(4, (dnsData.getSrv_txt().isEmpty()) ? "N/A" : dnsData.getSrv_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "dmarc":
                    sql += "(campaign_id,domain,old_dmarc,dmarc,location,migration_date) values (?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_dmarc_txt().isEmpty()) ? "N/A" : dnsData.getOld_dmarc_txt());
                        pst.setString(4, (dnsData.getDmarc_txt().isEmpty()) ? "N/A" : dnsData.getDmarc_txt());
                        pst.setString(5, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(6, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "":
                    sql += "(campaign_id,domain,cname,new_ip,old_ip,location,migration_date) values (?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getCname().isEmpty()) ? "N/A" : dnsData.getCname());
                        pst.setString(4, (dnsData.getNew_ip().isEmpty()) ? "N/A" : dnsData.getNew_ip());
                        pst.setString(5, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_ip().isEmpty()) ? "N/A" : dnsData.getOld_ip());
                        pst.setString(6, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(7, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
            if (pst != null) {
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addInvalidRecordsInBulkTable(DnsBean dnsData, String errMsg, int random, String tableName) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            String sql = "insert into " + tableName + " ";
            switch (dnsData.getReq_other_add()) {
                case "cname":
                    sql += "(campaign_id,domain,old_cname,cname,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_cname_txt().isEmpty()) ? "N/A" : dnsData.getOld_cname_txt());
                        pst.setString(4, (dnsData.getCname_txt().isEmpty()) ? "N/A" : dnsData.getCname_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "mx":
                    sql += "(campaign_id,domain,old_mx,mx,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_mx_txt().isEmpty()) ? "N/A" : dnsData.getOld_mx_txt());
                        pst.setString(4, (dnsData.getMx_txt().isEmpty()) ? "N/A" : dnsData.getMx_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "txt":
                    sql += "(campaign_id,domain,old_txt,txt,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_txt_txt().isEmpty()) ? "N/A" : dnsData.getOld_txt_txt());
                        pst.setString(4, (dnsData.getTxt_txt().isEmpty()) ? "N/A" : dnsData.getTxt_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "ptr":
                    sql += "(campaign_id,domain,old_ptr,ptr,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_ptr_txt().isEmpty()) ? "N/A" : dnsData.getOld_ptr_txt());
                        pst.setString(4, (dnsData.getPtr_txt().isEmpty()) ? "N/A" : dnsData.getPtr_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "spf":
                    sql += "(campaign_id,domain,old_spf,spf,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_spf_txt().isEmpty()) ? "N/A" : dnsData.getOld_spf_txt());
                        pst.setString(4, (dnsData.getSpf_txt().isEmpty()) ? "N/A" : dnsData.getSpf_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "srv":
                    sql += "(campaign_id,domain,old_srv,srv,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_srv_txt().isEmpty()) ? "N/A" : dnsData.getOld_srv_txt());
                        pst.setString(4, (dnsData.getSrv_txt().isEmpty()) ? "N/A" : dnsData.getSrv_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "dmarc":
                    sql += "(campaign_id,domain,old_dmarc,dmarc,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_dmarc_txt().isEmpty()) ? "N/A" : dnsData.getOld_dmarc_txt());
                        pst.setString(4, (dnsData.getDmarc_txt().isEmpty()) ? "N/A" : dnsData.getDmarc_txt());
                        pst.setString(5, errMsg);
                        pst.setString(6, "1");
                        pst.setString(7, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(8, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
                case "":
                    sql += "(campaign_id,domain,cname,new_ip,old_ip,dns_error,error_status,location,migration_date) values (?,?,?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    if (pst != null) {
                        pst.setInt(1, random);
                        pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                        pst.setString(3, (dnsData.getCname().isEmpty()) ? "N/A" : dnsData.getCname());
                        pst.setString(4, (dnsData.getNew_ip().isEmpty()) ? "N/A" : dnsData.getNew_ip());
                        pst.setString(5, (dnsData.getReq_().equals("req_modify") && dnsData.getOld_ip().isEmpty()) ? "N/A" : dnsData.getOld_ip());
                        pst.setString(6, errMsg);
                        pst.setString(7, "1");
                        pst.setString(8, (dnsData.getDns_loc().isEmpty()) ? "N/A" : dnsData.getDns_loc());
                        pst.setString(9, (dnsData.getMigration_date().isEmpty()) ? "N/A" : dnsData.getMigration_date());
                    }
                    break;
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
            if (pst != null) {
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Object> fetchBulkUploadData(int campaignId, String request_other_add) {
        Map<String, Object> bulkData = new HashMap<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            System.out.println("fetchSuccessfulBulkUploadData:::: campaignId:: " + campaignId + " request_other_add::::: " + request_other_add);
            List<DnsBean> validData = fetchSuccessfulBulkUploadData(campaignId, request_other_add);
            bulkData.put("validRecord", validData);
            List<DnsBean> invalidData = fetchUnSuccessfulBulkUploadData(campaignId, request_other_add);
            bulkData.put("invalidRecord", invalidData);
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT * FROM `dns_bulk_campaigns` WHERE `id` = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
            }
            Map<String, String> tmpRecord = new HashMap<>();
            if (rs1.next()) {
                tmpRecord.put("campaignId", rs1.getString("id"));
                tmpRecord.put("uploaded_file", rs1.getString("uploaded_file"));
                tmpRecord.put("renamed_file", rs1.getString("file_path"));
                tmpRecord.put("submitted_at", rs1.getString("submitted_at"));
                tmpRecord.put("request_type", rs1.getString("request_type"));
                tmpRecord.put("request_other_add", rs1.getString("req_other_add"));
                tmpRecord.put("owner_name", rs1.getString("owner_name"));
                tmpRecord.put("owner_email", rs1.getString("owner_email"));
            }
            bulkData.put("formDetails", tmpRecord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bulkData;
    }

    public List<DnsBean> fetchSuccessfulBulkUploadData(int campaignId, String request_other_add) {
        List<DnsBean> bulkData = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id,domain,old_cname,cname,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname_txt(rs1.getString("cname"));
                            dnsData.setOld_cname_txt(rs1.getString("old_cname"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id,domain,old_mx,mx,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setMx_txt(rs1.getString("mx"));
                            dnsData.setOld_mx_txt(rs1.getString("old_mx"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id,domain,old_txt,txt,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setTxt_txt(rs1.getString("txt"));
                            dnsData.setOld_txt_txt(rs1.getString("old_txt"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id,domain,old_ptr,ptr,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setPtr_txt(rs1.getString("ptr"));
                            dnsData.setOld_ptr_txt(rs1.getString("old_ptr"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id,domain,old_spf,spf,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSpf_txt(rs1.getString("spf"));
                            dnsData.setOld_spf_txt(rs1.getString("old_spf"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id,domain,old_srv,srv,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSrv_txt(rs1.getString("srv"));
                            dnsData.setOld_srv_txt(rs1.getString("old_srv"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id,domain,old_dmarc,dmarc,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setDmarc_txt(rs1.getString("dmarc"));
                            dnsData.setOld_dmarc_txt(rs1.getString("old_dmarc"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id,domain,cname,new_ip,old_ip,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname(rs1.getString("cname"));
                            dnsData.setOld_ip(rs1.getString("old_ip"));
                            dnsData.setNew_ip(rs1.getString("new_ip"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            if (rs1.getString("migration_date") == null) {
                                dnsData.setMigration_date("");
                            } else {
                                dnsData.setMigration_date(rs1.getString("migration_date"));
                            }
                            bulkData.add(dnsData);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bulkData;
    }

    public List<DnsBean> fetchUnSuccessfulBulkUploadData(int campaignId, String request_other_add) {
        List<DnsBean> bulkData = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id,domain,old_cname,cname,dns_error,location,migration_date  from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname_txt(rs1.getString("cname"));
                            dnsData.setOld_cname_txt(rs1.getString("old_cname"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id,domain,old_mx,mx,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setMx_txt(rs1.getString("mx"));
                            dnsData.setOld_mx_txt(rs1.getString("old_mx"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id,domain,old_txt,txt,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setTxt_txt(rs1.getString("txt"));
                            dnsData.setOld_txt_txt(rs1.getString("old_txt"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id,domain,old_ptr,ptr,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setPtr_txt(rs1.getString("ptr"));
                            dnsData.setOld_ptr_txt(rs1.getString("old_ptr"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id,domain,old_spf,spf,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSpf_txt(rs1.getString("spf"));
                            dnsData.setOld_spf_txt(rs1.getString("old_spf"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id,domain,old_srv,srv,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSrv_txt(rs1.getString("srv"));
                            dnsData.setOld_srv_txt(rs1.getString("old_srv"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id,domain,old_dmarc,dmarc,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setDmarc_txt(rs1.getString("dmarc"));
                            dnsData.setOld_dmarc_txt(rs1.getString("old_dmarc"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id,domain,cname,new_ip,old_ip,dns_error,location,migration_date from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname(rs1.getString("cname"));
                            dnsData.setOld_ip(rs1.getString("old_ip"));
                            dnsData.setNew_ip(rs1.getString("new_ip"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bulkData;
    }

    public Map<String, Object> bulkDataDelete1(int bulkDataId, int camaignId, String request_other_add, String req_for) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            con = DbConnection.getConnection();
            String qry = "";
            rs1 = null;
            pst = null;
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }

            qry = "UPDATE " + tableName + " set delete_status = 1 where id=?";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, bulkDataId);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fetchBulkUploadData(camaignId, request_other_add);
    }

    public DnsBean fetchSingleBulkRecord(int bulkDataId, String request_other_add) {
        DnsBean dnsData = new DnsBean();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname_txt(rs1.getString("cname"));
                            dnsData.setOld_cname_txt(rs1.getString("old_cname"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setOld_mx_txt(rs1.getString("old_mx"));
                            dnsData.setMx_txt(rs1.getString("mx"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setOld_txt_txt(rs1.getString("old_txt"));
                            dnsData.setTxt_txt(rs1.getString("txt"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setOld_ptr_txt(rs1.getString("old_ptr"));
                            dnsData.setPtr_txt(rs1.getString("ptr"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setOld_spf_txt(rs1.getString("old_spf"));
                            dnsData.setSpf_txt(rs1.getString("spf"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setOld_srv_txt(rs1.getString("old_srv"));
                            dnsData.setSrv_txt(rs1.getString("srv"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setOld_dmarc_txt(rs1.getString("old_dmarc"));
                            dnsData.setDmarc_txt(rs1.getString("dmarc"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "SELECT * from " + tableName + " where id=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, bulkDataId);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            dnsData.setId(bulkDataId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname(rs1.getString("cname"));
                            dnsData.setNew_ip(rs1.getString("new_ip"));
                            dnsData.setOld_ip(rs1.getString("old_ip"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            dnsData.setCampaignId(rs1.getInt("campaign_id"));
                            dnsData.setErrorMessage(rs1.getString("dns_error"));
                            dnsData.setRegistrationNumber(rs1.getString("registration_no"));
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dnsData;
    }

    public boolean duplicateRecord(DnsBean dnsEditData, String requestType, int campaignId, String req_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean flag = false;

        String domain = dnsEditData.getDomain();
        String newIp = dnsEditData.getNew_ip();
        String oldIp = dnsEditData.getOld_ip();
        String cName = dnsEditData.getCname();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            String tableName = "";
            switch (req_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and cname=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getCname_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and mx=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getMx_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and txt=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getTxt_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and ptr=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getPtr_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and spf=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getSpf_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and srv=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getSrv_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and dmarc=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getDmarc_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    if (requestType.equalsIgnoreCase("req_delete")) {
                        qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and error_status=? and delete_status=?";
                        pst = conSlave.prepareStatement(qry);
                        if (pst != null) {
                            pst.setInt(1, campaignId);
                            pst.setString(2, dnsEditData.getDomain());
                            pst.setInt(3, 0);
                            pst.setInt(4, 0);
                        }
                    } else {
                        qry = "SELECT * from " + tableName + " where campaign_id=? and domain=? and new_ip=? and error_status=? and delete_status=?";
                        pst = conSlave.prepareStatement(qry);
                        if (pst != null) {
                            pst.setInt(1, campaignId);
                            pst.setString(2, dnsEditData.getDomain());
                            pst.setString(3, dnsEditData.getNew_ip());
                            pst.setInt(4, 0);
                            pst.setInt(5, 0);
                        }
                    }
                    break;
            }
            if (pst != null) {
                rs1 = pst.executeQuery();

                if (rs1.next()) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public Map<String, List<String>> fetchAlreadyAppliedDomains(int campaignId, String req_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean flag = false;
        List<String> alreadyAllowedDomains = new ArrayList<>();
        List<String> alreadyAllowedIps = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            switch (req_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "SELECT domain,cname from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("cname"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "SELECT domain,mx from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("mx"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "SELECT domain,txt from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("txt"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "SELECT domain,ptr from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("ptr"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "SELECT domain,spf from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("spf"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "SELECT domain,srv from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("srv"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "SELECT domain,dmarc from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            alreadyAllowedDomains.add(rs1.getString("domain") + ":" + rs1.getString("dmarc"));
                        }
                        map.put("domains", alreadyAllowedDomains);
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "SELECT domain,new_ip from " + tableName + " where campaign_id=? and error_status='0' and delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        rs1 = pst.executeQuery();

                        while (rs1.next()) {
                            if (isThirdLevelDomain(rs1.getString("domain"))) {
                                alreadyAllowedDomains.add(rs1.getString("domain"));
                                alreadyAllowedIps.add(rs1.getString("new_ip"));
                            }
                        }
                        map.put("domains", alreadyAllowedDomains);
                        map.put("ips", alreadyAllowedIps);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public Map<String, List<String>> fetchAlreadyAppliedDomainsForAAAARecords(int campaignId, String req_other_add) {
        PreparedStatement pst = null, pst1 = null;
        ResultSet rs1 = null, rs2 = null;
        List<String> alreadyAllowedDomains = new ArrayList<>();
        List<String> alreadyAllowedIps = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT domain from " + Constants.DNS_BULK_UPLOAD + " where campaign_id=? and error_status='0' and delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
                String qry1 = "";
                while (rs1.next()) {
                    if (isThirdLevelDomain(rs1.getString("domain"))) {
                        qry1 = "SELECT new_ip from " + Constants.DNS_BULK_UPLOAD + " where campaign_id=? and domain=? and error_status='0' and delete_status='0'";
                        pst1 = conSlave.prepareStatement(qry);
                        if (pst1 != null) {
                            pst1.setInt(1, campaignId);
                            pst1.setString(2, rs1.getString("domain"));
                            rs2 = pst1.executeQuery();
                            while (rs2.next()) {
                                alreadyAllowedIps.add(rs2.getString("new_ip"));
                            }
                            map.put(rs1.getString("domain"), alreadyAllowedIps);
                        }
                    }
                }

                qry = "SELECT new_ip from " + Constants.DNS_BULK_UPLOAD + " where campaign_id=? and error_status='0' and delete_status='0'";
                pst = conSlave.prepareStatement(qry);
                if (pst != null) {
                    pst.setInt(1, campaignId);
                    rs1 = pst.executeQuery();

                    while (rs1.next()) {
                        qry1 = "SELECT domain from " + Constants.DNS_BULK_UPLOAD + " where campaign_id=? and new_ip=? and error_status='0' and delete_status='0'";
                        pst1 = conSlave.prepareStatement(qry1);
                        if (pst != null) {
                            pst1.setInt(1, campaignId);
                            pst1.setString(2, rs1.getString("new_ip"));
                            rs2 = pst1.executeQuery();
                            while (rs2.next()) {
                                if (isThirdLevelDomain(rs2.getString("domain"))) {
                                    alreadyAllowedDomains.add(rs2.getString("domain"));
                                }
                            }
                            map.put(rs1.getString("new_ip"), alreadyAllowedDomains);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public int updateBulkEditTable(DnsBean dnsEditData, String errMsg, boolean error, boolean isDuplicate, boolean toBeDeleted, String request_other_add, String request_type) {
        int result = -1;
        PreparedStatement pst = null;

        int id = dnsEditData.getId();

        String qry = "";
        int errorStatus, deleteStatus;

        if (toBeDeleted) {
            if (!error) {
                errMsg = "";
                errorStatus = 0;
                deleteStatus = 1;
            } else {
                errorStatus = 1;
                deleteStatus = 1;
            }
        } else {
            if (!error) {
                errMsg = "";
                errorStatus = 0;
                deleteStatus = 0;
            } else {
                errorStatus = 1;
                deleteStatus = 0;
            }
        }
        try {
            con = DbConnection.getConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_cname=?, cname=?, location=?,migration_date=?,dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_cname_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_cname_txt());
                            pst.setString(3, (dnsEditData.getCname_txt().isEmpty()) ? "N/A" : dnsEditData.getCname_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_mx=?, mx=?,location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_mx_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_mx_txt());
                            pst.setString(3, (dnsEditData.getMx_txt().isEmpty()) ? "N/A" : dnsEditData.getMx_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_txt=?, txt=?, location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_txt_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_txt_txt());
                            pst.setString(3, (dnsEditData.getTxt_txt().isEmpty()) ? "N/A" : dnsEditData.getTxt_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_ptr=?, ptr=?,location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_ptr_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_ptr_txt());
                            pst.setString(3, (dnsEditData.getPtr_txt().isEmpty()) ? "N/A" : dnsEditData.getPtr_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_spf=?, spf=?,location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_spf_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_spf_txt());
                            pst.setString(3, (dnsEditData.getSpf_txt().isEmpty()) ? "N/A" : dnsEditData.getSpf_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_srv=?, srv=?,location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_srv_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_srv_txt());
                            pst.setString(3, (dnsEditData.getSrv_txt().isEmpty()) ? "N/A" : dnsEditData.getSrv_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, old_dmarc=?, dmarc=?,location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (request_type.equals("req_modify") && dnsEditData.getOld_dmarc_txt().isEmpty()) ? "N/A" : dnsEditData.getOld_dmarc_txt());
                            pst.setString(3, (dnsEditData.getDmarc_txt().isEmpty()) ? "N/A" : dnsEditData.getDmarc_txt());
                            pst.setString(4, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(5, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(6, errMsg);
                            pst.setInt(7, errorStatus);
                            pst.setInt(8, deleteStatus);
                            pst.setInt(9, id);
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    if (!toBeDeleted) {
                        qry = "update " + tableName + " set domain=?, cname=?, new_ip=?, old_ip=?,location=?,migration_date=?, dns_error=?, error_status=?, delete_status=? where id=?";
                        pst = con.prepareStatement(qry);
                        if (pst != null) {
                            pst.setString(1, (dnsEditData.getDomain().isEmpty()) ? "N/A" : dnsEditData.getDomain());
                            pst.setString(2, (dnsEditData.getCname().isEmpty()) ? "N/A" : dnsEditData.getCname());
                            pst.setString(3, (dnsEditData.getNew_ip().isEmpty()) ? "N/A" : dnsEditData.getNew_ip());
                            pst.setString(4, (request_type.equals("req_modify") && dnsEditData.getOld_ip().isEmpty()) ? "N/A" : dnsEditData.getOld_ip());
                            pst.setString(5, (dnsEditData.getDns_loc().isEmpty()) ? "N/A" : dnsEditData.getDns_loc());
                            pst.setString(6, (dnsEditData.getMigration_date().isEmpty()) ? "N/A" : dnsEditData.getMigration_date());
                            pst.setString(7, errMsg);
                            pst.setInt(8, errorStatus);
                            pst.setInt(9, deleteStatus);
                            pst.setInt(10, id);
                        }
                    }
                    break;
            }

            if (toBeDeleted) {
                qry = "update " + tableName + " set error_status=?, delete_status=? where id=?";
                pst = con.prepareStatement(qry);
                if (pst != null) {
                    pst.setInt(1, errorStatus);
                    pst.setInt(2, deleteStatus);
                    pst.setInt(3, id);
                }
            }

            System.out.println("PST :::: " + pst);
            if (pst != null) {
                result = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public int updateRegNumbersInBulkTable(String request_other_add, String regNumber, int campaignId) {
        PreparedStatement pst = null;
        int result = -1;
        String qry = "";
        try {
            con = DbConnection.getConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }

            qry = "update " + tableName + " set registration_no=? where campaign_id=?";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regNumber);
                pst.setInt(2, campaignId);
                System.out.println("PST :::: " + pst);
                result = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int insertIntoDnsCampaign(int random, Map profile, String renamed_filepath, String uploaded_file, FormBean form_details) {
        PreparedStatement pst = null;
        int result = -1;
        String qry = "";
        try {
            con = DbConnection.getConnection();
            qry = "insert into dns_bulk_campaigns (id, owner_name,owner_email,file_path,uploaded_file,request_type,record_mx,record_mx1,record_ptr,record_ptr1,record_spf,record_srv,record_txt,record_dmarc,server_location, req_other_add) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(qry);
            if (pst != null && profile != null) {
                pst.setInt(1, random);
                pst.setString(2, "");
                pst.setString(3, (profile.get("email") != null) ? profile.get("email").toString() : "");
                pst.setString(4, renamed_filepath);
                pst.setString(5, uploaded_file);
                pst.setString(6, form_details.getReq_());
                pst.setString(7, form_details.getRequest_mx());
                pst.setString(8, form_details.getRequest_mx1());
                pst.setString(9, form_details.getRequest_ptr());
                pst.setString(10, form_details.getRequest_ptr1());
                pst.setString(11, form_details.getRequest_spf());
                pst.setString(12, form_details.getRequest_srv());
                pst.setString(13, form_details.getRequest_txt());
                pst.setString(14, form_details.getRequest_dmarc());
                pst.setString(15, form_details.getDns_loc());
                pst.setString(16, form_details.getReq_other_add());
                result = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int updateDnsCampaign(int id, String regNumber) {
        PreparedStatement pst = null;
        int result = -1;
        String qry = "";
        try {
            con = DbConnection.getConnection();
            qry = "update dns_bulk_campaigns set registration_no=?, status=? where id=?";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regNumber);
                pst.setInt(2, 1);
                pst.setInt(3, id);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + pst);
                result = pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean alreadyAappliedForThisDomain(DnsBean dnsData, int campaignId, String domain) {
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select domain,new_ip from dns_bulk_upload where campaign_id=? and id=? and error_status=? and delete_status=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setInt(2, dnsData.getId());
                pst.setInt(3, 0);
                pst.setInt(4, 0);
                System.out.println("QUERY ::: " + pst);
                rs1 = pst.executeQuery();

                if (rs1.next()) {
                    if (!rs1.getString("domain").equals(domain)) {
                        qry = "select * from dns_bulk_upload where campaign_id=? and domain=? and error_status=? and delete_status=?";
                        pst = conSlave.prepareStatement(qry);
                        if (pst != null) {
                            pst.setInt(1, campaignId);
                            pst.setString(2, domain);
                            pst.setInt(3, 0);
                            pst.setInt(4, 0);
                            rs2 = pst.executeQuery();
                            if (rs2.next()) {
                                result = true;
                            }
                        }
                    } else {
                        result = true;
                    }
                } else {
                    qry = "select * from dns_bulk_upload where campaign_id=? and domain=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, domain);
                        pst.setInt(3, 0);
                        pst.setInt(4, 0);
                        rs2 = pst.executeQuery();
                        if (rs2.next()) {
                            result = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean alreadyAappliedForThisIp(DnsBean dnsData, int campaignId, String newIp) {
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select domain,new_ip from dns_bulk_upload where campaign_id=? and id=? and error_status=? and delete_status=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setInt(2, dnsData.getId());
                pst.setInt(3, 0);
                pst.setInt(4, 0);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    if (!rs1.getString("new_ip").equals(newIp)) {
                        qry = "select * from dns_bulk_upload where campaign_id=? and new_ip=? and error_status=? and delete_status=?";
                        pst = conSlave.prepareStatement(qry);
                        if (pst != null) {
                            pst.setInt(1, campaignId);
                            pst.setString(2, newIp);
                            pst.setInt(3, 0);
                            pst.setInt(4, 0);
                            rs2 = pst.executeQuery();
                            if (rs2.next()) {
                                result = true;
                            }
                        }
                    } else {
                        result = true;
                    }
                } else {
                    qry = "select * from dns_bulk_upload where campaign_id=? and new_ip=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, newIp);
                        pst.setInt(3, 0);
                        pst.setInt(4, 0);
                        rs2 = pst.executeQuery();
                        if (rs2.next()) {
                            result = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Map<String, Object> fetchSuccessBulkData(int campaignId, String req_for, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Map<String, Object> validData = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            List<DnsBean> bulkData = null;
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id,domain,old_cname,cname,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname_txt(rs1.getString("cname"));
                            dnsData.setOld_cname_txt(rs1.getString("old_cname"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id,domain,old_mx,mx,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setMx_txt(rs1.getString("mx"));
                            dnsData.setOld_mx_txt(rs1.getString("old_mx"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id,domain,old_txt,txt,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setTxt_txt(rs1.getString("txt"));
                            dnsData.setOld_txt_txt(rs1.getString("old_txt"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id,domain,old_ptr,ptr,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setPtr_txt(rs1.getString("ptr"));
                            dnsData.setOld_ptr_txt(rs1.getString("old_ptr"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id,domain,old_spf,spf,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSpf_txt(rs1.getString("spf"));
                            dnsData.setOld_spf_txt(rs1.getString("old_spf"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id,domain,old_srv,srv,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSrv_txt(rs1.getString("srv"));
                            dnsData.setOld_srv_txt(rs1.getString("old_srv"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id,domain,old_dmarc,dmarc,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setDmarc_txt(rs1.getString("dmarc"));
                            dnsData.setOld_dmarc_txt(rs1.getString("old_dmarc"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id,domain,cname,new_ip,old_ip,location,migration_date from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setId(rs1.getInt("id"));
                            dnsData.setCampaignId(campaignId);
                            dnsData.setReq_other_add(request_other_add);
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname(rs1.getString("cname"));
                            if (req_for != null && req_for.equalsIgnoreCase("req_modify")) {
                                dnsData.setOld_ip(rs1.getString("old_ip"));
                            }
                            dnsData.setNew_ip(rs1.getString("new_ip"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
            }
            validData.put("validRecord", bulkData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return validData;
    }

    public List<DnsBean> fetchSuccessBulkData(String regNumber, String req_for, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        List<DnsBean> bulkData = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";

            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id,domain,old_cname,cname,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname_txt(rs1.getString("cname"));
                            dnsData.setOld_cname_txt(rs1.getString("old_cname"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id,domain,old_mx,mx,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setMx_txt(rs1.getString("mx"));
                            dnsData.setOld_mx_txt(rs1.getString("old_mx"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id,domain,old_txt,txt,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setTxt_txt(rs1.getString("txt"));
                            dnsData.setOld_txt_txt(rs1.getString("old_txt"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id,domain,old_ptr,ptr,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setPtr_txt(rs1.getString("ptr"));
                            dnsData.setOld_ptr_txt(rs1.getString("old_ptr"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id,domain,old_spf,spf,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSpf_txt(rs1.getString("spf"));
                            dnsData.setOld_spf_txt(rs1.getString("old_spf"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id,domain,old_srv,srv,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setSrv_txt(rs1.getString("srv"));
                            dnsData.setOld_srv_txt(rs1.getString("old_srv"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id,domain,old_dmarc,dmarc,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setDmarc_txt(rs1.getString("dmarc"));
                            dnsData.setOld_dmarc_txt(rs1.getString("old_dmarc"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id,domain,cname,new_ip,old_ip,location,migration_date from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        bulkData = new ArrayList<>();
                        while (rs1.next()) {
                            DnsBean dnsData = new DnsBean();
                            dnsData.setDomain(rs1.getString("domain"));
                            dnsData.setCname(rs1.getString("cname"));
                            if (req_for != null && req_for.equalsIgnoreCase("req_modify")) {
                                dnsData.setOld_ip(rs1.getString("old_ip"));
                            }
                            dnsData.setNew_ip(rs1.getString("new_ip"));
                            dnsData.setDns_loc(rs1.getString("location"));
                            dnsData.setMigration_date(rs1.getString("migration_date"));
                            bulkData.add(dnsData);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bulkData;
    }

    public int fetchCountOfSuccessfulRecords(int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int i = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "select count(*) as count from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    i = rs1.getInt("count");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public List<Map<String, String>> fetchOpenCampaigns(Set<String> aliases) {
        List<Map<String, String>> campaigndata = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            for (String email : aliases) {
                String qry = "SELECT * FROM `dns_bulk_campaigns` WHERE owner_email = ? and status='0' and discard_status='0'";
                pst = conSlave.prepareStatement(qry);
                if (pst != null) {
                    pst.setString(1, email);
                    rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        Map<String, String> tmpRecord = new HashMap<>();
                        tmpRecord.put("id", rs1.getString("id"));
                        tmpRecord.put("request_type", rs1.getString("request_type"));
                        tmpRecord.put("req_other_add", rs1.getString("req_other_add"));
                        tmpRecord.put("uploaded_file", rs1.getString("uploaded_file"));
                        tmpRecord.put("renamed_file", rs1.getString("file_path"));
                        tmpRecord.put("submitted_at", rs1.getString("submitted_at"));
                        campaigndata.add(tmpRecord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return campaigndata;
    }

    public int fetchCampaignId(String regno) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int camId = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT id FROM `dns_bulk_campaigns` WHERE registration_no = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regno);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    camId = rs1.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return camId;
    }

    public int fetchCampaignId(int id, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int camId = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "SELECT campaign_id FROM " + tableName + " WHERE id = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, id);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    camId = rs1.getInt("campaign_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return camId;
    }

    public String fetchRegNumberFromCampaignTable(int id) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String regNumber = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT registration_no FROM `dns_bulk_campaigns` WHERE id = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, id);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    regNumber = rs1.getString("registration_no");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return regNumber;
    }

    public String discardCampaign(int camaignId) {
        PreparedStatement pst = null;
        String responseResult = "";
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE dns_bulk_campaigns set discard_status=1 WHERE id=? and discard_status=0";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, camaignId);
                int i = pst.executeUpdate();
                if (i > 0) {
                    responseResult = "Campaign Discard Successfully.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }

    public Map<String, Object> deleteSingleUploadRecord(int bulkDataId, String req_for, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int campaign_id = 0;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }

            String qry = "SELECT campaign_id from " + tableName + " where id=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, bulkDataId);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    campaign_id = rs1.getInt("campaign_id");
                    rs1 = null;
                    pst = null;
                    qry = "UPDATE " + tableName + " set delete_status = 1 where id=?";
                    pst = con.prepareStatement(qry);
                    pst.setInt(1, bulkDataId);
                    pst.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fetchBulkUploadData(campaign_id, request_other_add);
    }

    public Object fetchSuccessBulkDataUsingReg(String reg_no, String req_for, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int campaign_id = 0;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT id from dns_bulk_campaigns where registration_no=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, reg_no);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    campaign_id = rs1.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fetchSuccessBulkData(campaign_id, req_for, request_other_add);
    }

    public FormBean fetchFormDetails(int campaignId) {
        FormBean form_details = new FormBean();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT * FROM `dns_bulk_campaigns` WHERE `id` = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    form_details.setReq_(rs1.getString("request_type"));
                    form_details.setRequest_mx(rs1.getString("record_mx"));
                    form_details.setRequest_mx1(rs1.getString("record_mx1"));
                    form_details.setRequest_dmarc(rs1.getString("record_dmarc"));
                    form_details.setRequest_ptr(rs1.getString("record_ptr"));
                    form_details.setRequest_ptr1(rs1.getString("record_ptr1"));
                    form_details.setRequest_spf(rs1.getString("record_spf"));
                    form_details.setRequest_srv(rs1.getString("record_srv"));
                    form_details.setRequest_txt(rs1.getString("record_txt"));
                    form_details.setDns_loc(rs1.getString("server_location"));
                    form_details.setUploaded_filename(rs1.getString("uploaded_file"));
                    form_details.setRenamed_filepath(rs1.getString("file_path"));
                    form_details.setUser_email(rs1.getString("owner_email"));
                    form_details.setUser_name(rs1.getString("owner_name"));
                    form_details.setRequest_type("dns_bulk");
                    form_details.setUrl("web_url");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return form_details;
    }

    public int fetchCampaignIdFromBulkUploadTable(int id, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int camId = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "SELECT campaign_id FROM " + tableName + " WHERE id = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, id);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    camId = rs1.getInt("campaign_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return camId;
    }

    public boolean areThereSuccessfulEntries(int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "SELECT * FROM " + tableName + " WHERE `campaign_id` = ? AND error_status= '0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
                System.out.println("com.org.dao.DnsDao.fetchBulkUploadData() PST::: " + pst);
                List validData = new ArrayList<>();
                if (rs1.next()) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String fetchRequestOtherAdd(int campaignId) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT req_other_add from dns_bulk_campaigns where id=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("req_other_add");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String fetchRequestType(int campaignId) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT request_type from dns_bulk_campaigns where id=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("request_type");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String fetchRequestOtherAdd(String regNumber) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        String result = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT req_other_record from dns_registration where registration_no=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regNumber);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    result = rs1.getString("req_other_record");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Map<String, Object> fetchCompleteCampaignData(Set<String> aliases) {
        Map<String, Object> bulkCampData = new HashMap<>();
        try {
            String pending_qry = "SELECT id,uploaded_file,file_path,submitted_at FROM `dns_bulk_campaigns` WHERE owner_email = ? and status='0' and discard_status='0'";
            bulkCampData.put("pending", fetchSeprateCampData(pending_qry, aliases));

            String completed_qry = "SELECT id,uploaded_file,file_path,submitted_at FROM `dns_bulk_campaigns` WHERE owner_email = ? and status='1' and discard_status='0'";
            bulkCampData.put("complete", fetchSeprateCampData(completed_qry, aliases));

            String discard_qry = "SELECT id,uploaded_file,file_path,submitted_at FROM `dns_bulk_campaigns` WHERE owner_email = ? and status='0' and discard_status='1'";
            bulkCampData.put("discard", fetchSeprateCampData(discard_qry, aliases));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bulkCampData;
    }

    private Object fetchSeprateCampData(String qry, Set<String> aliases) {
        List<Map<String, String>> campaigndata = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            for (String email : aliases) {
                pst = conSlave.prepareStatement(qry);
                if (pst != null) {
                    pst.setString(1, email);
                    rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        Map<String, String> tmpRecord = new HashMap<>();
                        tmpRecord.put("id", rs1.getString("id"));
                        tmpRecord.put("uploaded_file", rs1.getString("uploaded_file"));
                        tmpRecord.put("renamed_file", rs1.getString("file_path"));
                        tmpRecord.put("submitted_at", rs1.getString("submitted_at"));
                        campaigndata.add(tmpRecord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return campaigndata;
    }

    public boolean isRecordAlreadyThere(DnsBean dnsEditData, int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and cname=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getCname_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and mx=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getMx_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and txt=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getTxt_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and ptr=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getPtr_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and spf=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getSpf_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and srv=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getSrv_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and dmarc=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getDmarc_txt());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id from " + tableName + " where campaign_id=? and domain=? and new_ip=?  AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getNew_ip());
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean isNewRequest(String regNumber, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id from " + tableName + " where registration_no=? AND error_status='0' AND delete_status='0'";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean isNewRequestCompleteCheck(String regNumber, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    qry = "select id from " + tableName + " where registration_no=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setString(1, regNumber);
                        System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                        rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            result = true;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int fetchCountOfSuccessfulRecordsAgainstDomain(DnsBean dnsData, int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int i = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "select count(*) as count from " + tableName + " where campaign_id=? AND domain=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    i = rs1.getInt("count");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public boolean isIPmappedAgainstDomainNICIp(DnsBean dnsData, int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            String tableName = "";
//            switch (request_other_add) {
//                case "cname":
//                    tableName = Constants.DNS_BULK_CNAME;
//                    break;
//                case "mx":
//                    tableName = Constants.DNS_BULK_MX;
//                    break;
//                case "txt":
//                    tableName = Constants.DNS_BULK_TXT;
//                    break;
//                case "ptr":
//                    tableName = Constants.DNS_BULK_PTR;
//                    break;
//                case "spf":
//                    tableName = Constants.DNS_BULK_SPF;
//                    break;
//                case "srv":
//                    tableName = Constants.DNS_BULK_SRV;
//                    break;
//                case "dmarc":
//                    tableName = Constants.DNS_BULK_DMARC;
//                    break;
//                case "":
//                    tableName = Constants.DNS_BULK_UPLOAD;
//                    break;
//            }
            String qry = "select new_ip from " + Constants.DNS_BULK_UPLOAD + " where campaign_id=? AND domain=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, (dnsData.getDomain().isEmpty()) ? "N/A" : dnsData.getDomain());
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    if (doesIPbelongToNIC(rs1.getString("new_ip"))) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean doesIPbelongToNIC(String ip) {
        return ip.startsWith("164.100") || ip.startsWith("14.139") || ip.startsWith("180.149") || ip.startsWith("10.");
    }

    public int fetchCountOfSuccessfulRecordsAgainstIP(DnsBean dnsData, int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int i = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "select count(*) as count from " + tableName + " where campaign_id=? AND new_ip=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, dnsData.getNew_ip());
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    i = rs1.getInt("count");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public int fetchIDForDomain(String domain, int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int i = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "select id from " + tableName + " where campaign_id=? AND domain=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, domain);
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    i = rs1.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public int fetchIDForIP(String new_ip, int campaignId, String request_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int i = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            switch (request_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    break;
            }
            String qry = "select id from " + tableName + " where campaign_id=? AND new_ip=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, new_ip);
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    i = rs1.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public boolean isDomainMappedAgainstIPThirdLevel(String newIp, int campaignId, String req_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean result = false;
        try {
            conSlave = DbConnection.getSlaveConnection();

            String qry = "select domain from " + Constants.DNS_BULK_UPLOAD + " where campaign_id=? AND new_ip=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, newIp);
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    if (isThirdLevelDomain(rs1.getString("domain"))) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<Integer> fetchIDOfDuplicateRecord(ValidatedDnsBean dnsEditData, String request_type, int campaignId, String req_other_add) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;

        List<Integer> ids = new ArrayList<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            String tableName = "";
            switch (req_other_add) {
                case "cname":
                    tableName = Constants.DNS_BULK_CNAME;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and cname=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getCname_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "mx":
                    tableName = Constants.DNS_BULK_MX;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and mx=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getMx_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "txt":
                    tableName = Constants.DNS_BULK_TXT;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and txt=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getTxt_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "ptr":
                    tableName = Constants.DNS_BULK_PTR;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and ptr=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getPtr_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "spf":
                    tableName = Constants.DNS_BULK_SPF;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and spf=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getSpf_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "srv":
                    tableName = Constants.DNS_BULK_SRV;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and srv=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getSrv_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "dmarc":
                    tableName = Constants.DNS_BULK_DMARC;
                    qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and dmarc=? and error_status=? and delete_status=?";
                    pst = conSlave.prepareStatement(qry);
                    if (pst != null) {
                        pst.setInt(1, campaignId);
                        pst.setString(2, dnsEditData.getDomain());
                        pst.setString(3, dnsEditData.getDmarc_txt());
                        pst.setInt(4, 0);
                        pst.setInt(5, 0);
                    }
                    break;
                case "":
                    tableName = Constants.DNS_BULK_UPLOAD;
                    if (request_type.equalsIgnoreCase("req_delete")) {
                        qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and error_status=? and delete_status=?";
                        pst = conSlave.prepareStatement(qry);
                        if (pst != null) {
                            pst.setInt(1, campaignId);
                            pst.setString(2, dnsEditData.getDomain());
                            pst.setInt(3, 0);
                            pst.setInt(4, 0);
                        }
                    } else {
                        qry = "SELECT id from " + tableName + " where campaign_id=? and domain=? and new_ip=? and error_status=? and delete_status=?";
                        pst = conSlave.prepareStatement(qry);
                        if (pst != null) {
                            pst.setInt(1, campaignId);
                            pst.setString(2, dnsEditData.getDomain());
                            pst.setString(3, dnsEditData.getNew_ip());
                            pst.setInt(4, 0);
                            pst.setInt(5, 0);
                        }
                    }
                    break;
            }
            if (pst != null) {
                rs1 = pst.executeQuery();

                while (rs1.next()) {
                    ids.add(rs1.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ids;
    }

    public String discardCampaign(String aliase) {
        PreparedStatement pst = null;
        String responseResult = "";
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE dns_bulk_campaigns set discard_status=1 WHERE owner_email=? and discard_status=0";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, aliase);
                int i = pst.executeUpdate();
                if (i > 0) {
                    responseResult = "Campaign Discard Successfully.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }

    public Set<Integer> fetchCampaignsAgainstDomain(String domain) {
        Set<Integer> ids = new HashSet<>();

        List<String> tables = Arrays.asList(Constants.DNS_BULK_CNAME, Constants.DNS_BULK_MX, Constants.DNS_BULK_TXT, Constants.DNS_BULK_PTR, Constants.DNS_BULK_SPF, Constants.DNS_BULK_SRV, Constants.DNS_BULK_DMARC, Constants.DNS_BULK_UPLOAD);

        Set<Integer> tempIds = null;
        for (String table : tables) {
            tempIds = fetchCampaignsFromTables(table, domain);
            ids.addAll(tempIds);
        }
        return ids;
    }

    private Set<Integer> fetchCampaignsFromTables(String tableName, String domain) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;

        Set<Integer> ids = new HashSet<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT id from " + tableName + " where domain=? and error_status=? and delete_status=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, domain);
                pst.setInt(2, 0);
                pst.setInt(3, 0);

                rs1 = pst.executeQuery();

                while (rs1.next()) {
                    ids.add(rs1.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ids;
    }

    public Set<Integer> fetchCampaignsAgainstIp(String ip) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;

        Set<Integer> ids = new HashSet<>();

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT id from " + Constants.DNS_BULK_UPLOAD + " where new_ip=? and error_status=? and delete_status=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, ip);
                pst.setInt(2, 0);
                pst.setInt(3, 0);

                rs1 = pst.executeQuery();

                while (rs1.next()) {
                    ids.add(rs1.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ids;
    }
}
