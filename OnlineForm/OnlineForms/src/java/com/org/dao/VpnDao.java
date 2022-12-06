/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import com.org.connections.DbConnection;
import com.org.service.ProfileService;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.FormBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import validation.BulkValidation;

/**
 *
 * @author nikki
 */
public class VpnDao {

    private //Connection con = null;
            String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public boolean vpn_check_exp_reg(String email, String reg_no) {
        boolean flag = false;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "vpn_check_exp_reg called BRO......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;

            String email_all = "";
            if (email.contains(",")) {
                String a[] = email.split(",");
                for (int i = 0; i < a.length; i++) {
                    email_all += "applicant_email = " + a[i] + " or ";
                }
                email_all = email_all.substring(0, email_all.length() - 3);
            }

            String sql = "select status,form_name from final_audit_track where (" + email_all + ") and status like '%_pending' and form_name='vpn_renew'and vpn_reg_no ='" + reg_no + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Bro this is the query :::::::::" + sql);
            pst = conSlave.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }

        return flag;
    }

    public String Vpn_Surrender(FormBean form_details, Map profile_values) {
        System.out.println("inside Vpn_Surrender");
        String dbrefno = "", newref = "";
        
//        String hodMobile = form_details.getHod_mobile().trim();
//        String hodEmail = form_details.getHod_email().trim();
//        Ldap ldap = new Ldap();
//        ProfileService profileService = new ProfileService();
//        if (ldap.emailValidate(hodEmail)) {
//            if (hodMobile.contains("X")) {
//                HashMap hodDetails = (HashMap) profileService.getHODdetails(hodEmail);
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                profile_values.put("hod_mobile", form_details.getHod_mobile());
//            }
//        }

        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            String search_regnum = "select registration_no from vpn_registration where date(datetime)=date(now()) and registration_no like 'VPNSURRENDER%' group by registration_no order by registration_no desc limit 1";
            System.out.println("query:::::::::::::::"+search_regnum);
            pst = conSlave.prepareStatement(search_regnum);
           
            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            pst.close();
            rs1.close();
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(25, dbrefno.length());
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
            newref = "VPNSURRENDER-FORM" + pdate1 + newref;
             System.out.println("inside Vpn_Surrender newref"+newref);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);

            String sql = "insert into vpn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                    + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                    + "registration_no,userip,renew_flag,vpn_reg_no,user_type,ca_desig,remarks,coordinator_email) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
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
            pst.setString(12, profile_values.get("user_employment").toString());
            if (profile_values.get("user_employment").toString().equals("Central") || profile_values.get("user_employment").toString().equals("UT")) {
                pst.setString(13, profile_values.get("min").toString());   // ministry
                pst.setString(14, profile_values.get("dept").toString());  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, "");                      // state
                pst.setString(17, "");   //org
            } else if (profile_values.get("user_employment").equals("Others") || profile_values.get("user_employment").equals("Psu") || profile_values.get("user_employment").equals("Const") || profile_values.get("user_employment").equals("Nkn") || profile_values.get("user_employment").equals("Project")) {
                pst.setString(13, "");   // ministry
                pst.setString(14, "");  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, "");                      // state
                pst.setString(17, profile_values.get("Org").toString());   //org
            } else {
                pst.setString(13, "");   // ministry
                pst.setString(14, profile_values.get("dept").toString());  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, profile_values.get("stateCode").toString());                      // state
                pst.setString(17, "");   //org
            }

            if (profile_values.get("user_employment").toString().equals("Central") || profile_values.get("user_employment").toString().equals("UT")) {
                if (profile_values.get("min").toString().trim().equalsIgnoreCase("Railways,Railnet")) {
                    pst.setString(18, "Sh, Dharmendra Singh");
                    pst.setString(19, "dtele@rb.railnet.gov.in");
                    pst.setString(20, "+919810370486");
                    pst.setString(21, "011-23388504");
                } else {
                    pst.setString(18, profile_values.get("hod_name").toString());
                    pst.setString(19, profile_values.get("hod_email").toString());
                    pst.setString(20, profile_values.get("hod_mobile").toString());
                    pst.setString(21, profile_values.get("hod_tel").toString());

                }
            } else {
                pst.setString(18, profile_values.get("hod_name").toString());
                pst.setString(19, profile_values.get("hod_email").toString());
                pst.setString(20, profile_values.get("hod_mobile").toString());
                pst.setString(21, profile_values.get("hod_tel").toString());
            }
//            pst.setString(18, profile_values.get("hod_name").toString());
//            pst.setString(19, profile_values.get("hod_email").toString());
//            pst.setString(20, profile_values.get("hod_mobile").toString());
//            pst.setString(21, profile_values.get("hod_tel").toString());

            //pst.setString(22, form_details.getReq_for());          
            pst.setString(22, newref);
            pst.setString(23, ip);
            pst.setBoolean(24, true);
            pst.setString(25, form_details.getVpn_reg_no());
            pst.setString(26, form_details.getReq_for());
            pst.setString(27, profile_values.get("ca_design").toString());
            pst.setString(28, form_details.getRemarks());
            if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                pst.setString(29, form_details.getVpn_coo());
            } else {
                pst.setString(29, "");

            }
            //pst.setString(29, form_details.getVpn_coo_email()); //form_details.getVpn_coo()
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();
            pst.close();

            // con.close();
        } catch (Exception e) {
            newref = "";
            e.printStackTrace();
        }
        return newref;
    }

    public String Vpn_Delete(FormBean form_details, Map profile_values, String[] deleted_values) {

        String dbrefno = "", newref = "";
//        String hodMobile = form_details.getHod_mobile().trim();
//        String hodEmail = form_details.getHod_email().trim();
//        Ldap ldap = new Ldap();
//        ProfileService profileService = new ProfileService();
//        if (ldap.emailValidate(hodEmail)) {
//            if (hodMobile.contains("X")) {
//                HashMap hodDetails = (HashMap) profileService.getHODdetails(hodEmail);
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                profile_values.put("hod_mobile", form_details.getHod_mobile());
//            }
//        }

        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            String search_regnum = "select registration_no from vpn_registration where date(datetime)=date(now()) and registration_no like 'VPNDELETE%' group by registration_no order by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            System.out.println("DBREFNO::: " + dbrefno);
            pst.close();
            rs1.close();
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(22, dbrefno.length());
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
            System.out.println("NEW REF NO::: " + newref);
            newref = "VPNDELETE-FORM" + pdate1 + newref;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);

            String sql = "insert into vpn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                    + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                    + "registration_no,userip,renew_flag,vpn_reg_no,user_type,ca_desig,remarks,coordinator_email) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
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
            pst.setString(12, profile_values.get("user_employment").toString());
            if (profile_values.get("user_employment").toString().equals("Central") || profile_values.get("user_employment").toString().equals("UT")) {
                pst.setString(13, profile_values.get("min").toString());   // ministry
                pst.setString(14, profile_values.get("dept").toString());  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, "");                      // state
                pst.setString(17, "");   //org
            } else if (profile_values.get("user_employment").equals("Others") || profile_values.get("user_employment").equals("Psu") || profile_values.get("user_employment").equals("Const") || profile_values.get("user_employment").equals("Nkn") || profile_values.get("user_employment").equals("Project")) {
                pst.setString(13, "");   // ministry
                pst.setString(14, "");  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, "");                      // state
                pst.setString(17, profile_values.get("Org").toString());   //org
            } else {
                pst.setString(13, "");   // ministry
                pst.setString(14, profile_values.get("dept").toString());  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, profile_values.get("stateCode").toString());                      // state
                pst.setString(17, "");   //org
            }

            if (profile_values.get("user_employment").toString().equals("Central") || profile_values.get("user_employment").toString().equals("UT")) {
                if (profile_values.get("min").toString().trim().equalsIgnoreCase("Railways,Railnet")) {
                    pst.setString(18, "Sh, Dharmendra Singh");
                    pst.setString(19, "dtele@rb.railnet.gov.in");
                    pst.setString(20, "+919810370486");
                    pst.setString(21, "011-23388504");
                } else {
                    pst.setString(18, profile_values.get("hod_name").toString());
                    pst.setString(19, profile_values.get("hod_email").toString());
                    pst.setString(20, profile_values.get("hod_mobile").toString());
                    pst.setString(21, profile_values.get("hod_tel").toString());

                }
            } else {
                pst.setString(18, profile_values.get("hod_name").toString());
                pst.setString(19, profile_values.get("hod_email").toString());
                pst.setString(20, profile_values.get("hod_mobile").toString());
                pst.setString(21, profile_values.get("hod_tel").toString());
            }
//            pst.setString(18, profile_values.get("hod_name").toString());
//            pst.setString(19, profile_values.get("hod_email").toString());
//            pst.setString(20, profile_values.get("hod_mobile").toString());
//            pst.setString(21, profile_values.get("hod_tel").toString());

            //pst.setString(22, form_details.getReq_for());          
            pst.setString(22, newref);
            pst.setString(23, ip);
            pst.setBoolean(24, true);
            pst.setString(25, form_details.getVpn_reg_no());
            pst.setString(26, form_details.getReq_for());
            pst.setString(27, profile_values.get("ca_design").toString());
            pst.setString(28, form_details.getRemarks());
            if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                pst.setString(29, form_details.getVpn_coo());
            } else {
                pst.setString(29, "");

            }
            //pst.setString(29, form_details.getVpn_coo_email()); //form_details.getVpn_coo()
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();
            pst.close();
            String all_deletd_values = "";
            for (String a : deleted_values) {

                System.out.println("DELETED VALUES::: " + a + form_details.getVpn_reg_no());
                all_deletd_values = a;
            }
            // insert in new table -------------- DELETED VALUES  
            String[] split_delete = all_deletd_values.split("#");
            String server_ip = "", server_location = "", det_port = "", service = "";
            for (int k = 0; k < split_delete.length; k++) {

                String[] h = split_delete[k].split("~");

                try {
                    //System.out.println("1:" + h[1] + "2:" + h[2] + "3:" + h[3] + "4:" + h[4]);
                    server_ip = h[1];
                    server_location = h[2];
                    det_port = h[3];
                    service = h[4];
                } catch (Exception e) {

                }
                System.out.println("IP:" + server_ip + "location:" + server_location + "port:" + det_port + "service:" + service);
                String vpn_delete_query = "insert into vpn_registration_delete (registration_no,server_ip,location,port,service,datetime,vpn_reg) values (?,?,?,?,?,now(),?)";
                pst = con.prepareStatement(vpn_delete_query);
                pst.setString(1, newref);
                pst.setString(2, server_ip);
                pst.setString(3, server_location);
                pst.setString(4, det_port);
                pst.setString(5, service);
                pst.setString(6, form_details.getVpn_reg_no());
                int update_count = pst.executeUpdate();
                pst.close();

            }

//            
            // con.close();
        } catch (Exception e) {
            newref = "";
            e.printStackTrace();
        }
        return newref;
    }

    public boolean vpn_check_surrender_reg(String email, String reg_no) {
        boolean flag = false;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "vpn_surrender_exp_reg called BRO......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;

            String email_all = "";
            if (email.contains(",")) {
                String a[] = email.split(",");
                for (int i = 0; i < a.length; i++) {
                    email_all += "applicant_email = " + a[i] + " or ";
                }
                email_all = email_all.substring(0, email_all.length() - 3);
            }

            String sql = "select status,form_name from final_audit_track where (" + email_all + ") and status like '%_pending' and form_name='vpn_surrender' and vpn_reg_no ='" + reg_no + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Bro this is the query :::::::::" + sql);
            pst = conSlave.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }

        return flag;
    }

    public int Vpn_tab1(FormBean form_details) {
        int generatedKey = 0;
        /*
        try {

            con = DbConnection.getConnection();
            // get bean of profile
            PreparedStatement pst = null;
            String sql = "insert into onlineform_temp_db.vpn_registration_tmp (user_type,ip_type,ip1,ip2,server_location,server_loc_other,app_url,dest_port,ip)"
                    + "values (?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql, pst.RETURN_GENERATED_KEYS);
            pst.setString(1, form_details.getReq_for());
            pst.setString(2, form_details.getIp_type());
            if (form_details.getIp_type().equals("single")) {
                pst.setString(3, form_details.getNew_ip1());
                pst.setString(4, "");
            } else {
                pst.setString(3, form_details.getNew_ip2());
                pst.setString(4, form_details.getNew_ip3());
            }
            pst.setString(5, form_details.getServer_loc());
            pst.setString(6, form_details.getServer_loc_txt());
            pst.setString(7, form_details.getApp_url());
            pst.setString(8, form_details.getDest_port());
            pst.setString(9, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps: " + pst);
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();

            if (rs.next()) {
                generatedKey = rs.getInt(1);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "seesion gen key is::::" + generatedKey);
            }
            rs.close();
            pst.close();
            // con.close();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inserted record's ID: " + generatedKey);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
         */
        return generatedKey;
    }

//    public void Vpn_tab2(FormBean form_details, String generated_key) {
//        try {
//
//            con = DbConnection.getConnection();
//            // get bean of profile
//            PreparedStatement pst = null;
//            String sql = "";
//            if (form_details.getReq_for().equals("single")) {
//                sql = "update onlineform_temp_db.vpn_registration_tmp set name=?,designation=?,email=?,mobile=?,address=? where id=?";
//                pst = con.prepareStatement(sql);
//                pst.setString(1, form_details.getT_off_name());
//                pst.setString(2, form_details.getTdesignation());
//                pst.setString(3, form_details.getTauth_email());
//                pst.setString(4, form_details.getTmobile());
//                pst.setString(5, form_details.getTaddrs());
//                pst.setString(6, generated_key);
//            } else if (form_details.getReq_for().equals("bulk")) {
//                sql = "update onlineform_temp_db.vpn_registration_tmp set uploaded_filename=?,renamed_filepath=? where id=?";
//                pst = con.prepareStatement(sql);
//                pst.setString(1, form_details.getUploaded_filename());
//                pst.setString(2, form_details.getRenamed_filepath());
//                pst.setString(3, generated_key);
//            }
//            pst.executeUpdate();
//            pst.close();
//            // con.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public String Vpn_tab3(FormBean form_details, String filename, Map profile_values, Map vpn_data) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "VPN DATA IN DAO:: " + vpn_data);
//        String hodMobile = form_details.getHod_mobile().trim();
//        String hodEmail = form_details.getHod_email().trim();
//        Ldap ldap = new Ldap();
//        ProfileService profileService = new ProfileService();
//        if (ldap.emailValidate(hodEmail)) {
//            if (hodMobile.contains("X")) {
//                HashMap hodDetails = (HashMap) profileService.getHODdetails(hodEmail);
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                profile_values.put("hod_mobile", form_details.getHod_mobile());
//            }
//        }
        String dbrefno = "", newref = "";
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            if (form_details.getReq_for().equals("bulk")) {
                String search_regnum = "select registration_no from vpn_registration where date(datetime)=date(now()) and registration_no like 'VPNBULK%' group by registration_no order by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                ResultSet rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                pst.close();
                rs1.close();
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(21, dbrefno.length());
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
                newref = "VPNBULK-FORM" + pdate1 + newref;
            } else if (form_details.getReq_for().equals("change_add")) {
                String search_regnum = "select registration_no from vpn_registration where date(datetime)=date(now()) and registration_no like 'VPNMOD%' group by registration_no order by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                ResultSet rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                pst.close();
                rs1.close();
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(21, dbrefno.length());
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
                newref = "VPNMOD-FORM" + pdate1 + newref;
            } else {
                String search_regnum = "select registration_no from vpn_registration where date(datetime)=date(now()) and registration_no like 'VPN-FORM%' group by registration_no order by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                ResultSet rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                pst.close();
                rs1.close();
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
                newref = "VPN-FORM" + pdate1 + newref;
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
            String sql = "";

            if (form_details.getReq_for().equals("change_add")) { // add new IP 
                sql = "insert into vpn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                        + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                        + "user_type,registration_no,userip,ca_desig,vpn_reg_no,remarks,coordinator_email) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            } else {
                sql = "insert into vpn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                        + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                        + "user_type,registration_no,userip,ca_desig,remarks,coordinator_email) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            pst = con.prepareStatement(sql);
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

            if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                if (form_details.getMin().trim().equalsIgnoreCase("Railways,Railnet")) {
                    pst.setString(18, "Sh, Dharmendra Singh");
                    pst.setString(19, "dtele@rb.railnet.gov.in");
                    pst.setString(20, "+919810370486");
                    pst.setString(21, "011-23388504");
                } else {
//                    pst.setString(18, form_details.getHod_name());
//                    pst.setString(19, form_details.getHod_email());
//                    pst.setString(20, form_details.getHod_mobile());
//                    pst.setString(21, form_details.getHod_tel());
                    pst.setString(18, profile_values.get("hod_name").toString());
                    pst.setString(19, profile_values.get("hod_email").toString());
                    pst.setString(20, profile_values.get("hod_mobile").toString());
                    pst.setString(21, profile_values.get("hod_tel").toString());
                }
            } else {
                pst.setString(18, profile_values.get("hod_name").toString());
                pst.setString(19, profile_values.get("hod_email").toString());
                pst.setString(20, profile_values.get("hod_mobile").toString());
                pst.setString(21, profile_values.get("hod_tel").toString());
            }
            if (form_details.getReq_for().equalsIgnoreCase("single")) {
                pst.setString(22, "vpn_single");
            } else {
                pst.setString(22, form_details.getReq_for());
            }
            pst.setString(23, newref);
            pst.setString(24, ip);
            pst.setString(25, profile_values.get("ca_design").toString());
            if (form_details.getVpn_reg_no() != null) {
                pst.setString(26, form_details.getVpn_reg_no());
                pst.setString(27, form_details.getRemarks());
                if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                    pst.setString(28, form_details.getVpn_coo());
                } else {
                    pst.setString(28, "");

                }

            } else {
                pst.setString(26, form_details.getRemarks());
                if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                    pst.setString(27, form_details.getVpn_coo());
                } else {
                    pst.setString(27, "");

                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();
            pst.close();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "vpn_data:: " + vpn_data);
            if (i > 0) {
                Iterator<Map.Entry<String, Map<String, String>>> parent = vpn_data.entrySet().iterator();
                while (parent.hasNext()) {
                    Map.Entry<String, Map<String, String>> parentPair = parent.next();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());

                    pst = con.prepareStatement("insert into vpn_entries(registration_no,ip_type,ip1,ip2,server_location,server_loc_other,app_url,dest_port,datetime,action_type) values (?,?,?,?,?,?,?,?,now(),?)");
                    pst.setString(1, newref);
                    pst.setString(2, parentPair.getValue().get("ip_type"));
                    if (parentPair.getValue().get("ip_type").equals("single")) {
                        pst.setString(3, parentPair.getValue().get("ip1"));
                        pst.setString(4, "");
                    } else {
                        pst.setString(3, parentPair.getValue().get("ip2"));
                        pst.setString(4, parentPair.getValue().get("ip3"));
                    }
                    pst.setString(5, parentPair.getValue().get("server_loc"));
                    pst.setString(6, parentPair.getValue().get("server_loc_txt"));
                    pst.setString(7, parentPair.getValue().get("app_url"));
                    pst.setString(8, parentPair.getValue().get("dest_port"));
                    pst.setString(9, parentPair.getValue().get("action_type"));
                    pst.executeUpdate();
                    pst.close();
                }

            }

            if (form_details.getReq_for().equals("bulk")) {
                List<FormBean> userList = readExcelFile(filename);
                for (FormBean bulkUser : userList) {
                    bulkUser.setRegistration_no(newref);
                    insertVPnUser(bulkUser);
                }
            }
            // con.close();
        } catch (Exception e) {
            newref = "";
            e.printStackTrace();
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$" + newref);
        return newref;
    }

   //  NITIN -START
    public String Vpn_Renew(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
//        String hodMobile = form_details.getHod_mobile().trim();
//        String hodEmail = form_details.getHod_email().trim();
//        Ldap ldap = new Ldap();
//        ProfileService profileService = new ProfileService();
//        if (ldap.emailValidate(hodEmail)) {
//            if (hodMobile.contains("X")) {
//                HashMap hodDetails = (HashMap) profileService.getHODdetails(hodEmail);
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                profile_values.put("hod_mobile", form_details.getHod_mobile());
//            }
//        }
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            String search_regnum = "select registration_no from vpn_registration where date(datetime)=date(now()) and registration_no like 'VPNRENEW%' group by registration_no order by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            pst.close();
            rs1.close();
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(21, dbrefno.length());
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
            newref = "VPNRENEW-FORM" + pdate1 + newref;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);

            String sql = "insert into vpn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                    + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                    + "registration_no,userip,renew_flag,vpn_reg_no,user_type,ca_desig,remarks,coordinator_email) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
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
            pst.setString(12, profile_values.get("user_employment").toString());
            if (profile_values.get("user_employment").toString().equals("Central") || profile_values.get("user_employment").toString().equals("UT")) {
                pst.setString(13, profile_values.get("min").toString());   // ministry
                pst.setString(14, profile_values.get("dept").toString());  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, "");                      // state
                pst.setString(17, "");   //org
            } else if (profile_values.get("user_employment").equals("Others") || profile_values.get("user_employment").equals("Psu") || profile_values.get("user_employment").equals("Const") || profile_values.get("user_employment").equals("Nkn") || profile_values.get("user_employment").equals("Project")) {
                pst.setString(13, "");   // ministry
                pst.setString(14, "");  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, "");                      // state
                pst.setString(17, profile_values.get("Org").toString());   //org
            } else {
                pst.setString(13, "");   // ministry
                pst.setString(14, profile_values.get("dept").toString());  // dept
                pst.setString(15, profile_values.get("other_dept").toString());   // other 
                pst.setString(16, profile_values.get("stateCode").toString());                      // state
                pst.setString(17, "");   //org
            }

            if (profile_values.get("user_employment").toString().equals("Central") || profile_values.get("user_employment").toString().equals("UT")) {
                if (profile_values.get("min").toString().trim().equalsIgnoreCase("Railways,Railnet")) {
                    pst.setString(18, "Sh, Dharmendra Singh");
                    pst.setString(19, "dtele@rb.railnet.gov.in");
                    pst.setString(20, "+919810370486");
                    pst.setString(21, "011-23388504");
                } else {
                    pst.setString(18, profile_values.get("hod_name").toString());
                    pst.setString(19, profile_values.get("hod_email").toString());
                    pst.setString(20, profile_values.get("hod_mobile").toString());
                    pst.setString(21, profile_values.get("hod_tel").toString());

                }
            } else {
                pst.setString(18, profile_values.get("hod_name").toString());
                pst.setString(19, profile_values.get("hod_email").toString());
                pst.setString(20, profile_values.get("hod_mobile").toString());
                pst.setString(21, profile_values.get("hod_tel").toString());
            }
//            pst.setString(18, profile_values.get("hod_name").toString());
//            pst.setString(19, profile_values.get("hod_email").toString());
//            pst.setString(20, profile_values.get("hod_mobile").toString());
//            pst.setString(21, profile_values.get("hod_tel").toString());

            //pst.setString(22, form_details.getReq_for());          
            pst.setString(22, newref);
            pst.setString(23, ip);
            pst.setBoolean(24, true);
            pst.setString(25, form_details.getVpn_reg_no());
            pst.setString(26, form_details.getReq_for());
            pst.setString(27, profile_values.get("ca_design").toString());
            pst.setString(28, form_details.getRemarks());
            if (!form_details.getVpn_coo().equalsIgnoreCase("na")) {
                pst.setString(29, form_details.getVpn_coo());
            } else {
                pst.setString(29, "");

            }
            //pst.setString(29, form_details.getVpn_coo_email()); //form_details.getVpn_coo()
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();
            pst.close();

            // con.close();
        } catch (Exception e) {
            newref = "";
            e.printStackTrace();
        }
        return newref;
    }   
    
    // NITIN -END
    public List readExcelFile(String filename) {
        List<FormBean> userList = new ArrayList<FormBean>();
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(filename);

            org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                while (rowIterator.hasNext()) {          // row iterate
                    FormBean formbean = new FormBean();
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if (row.getRowNum() == 0) {
                        continue; //just skip the rows if row number is 0 
                    }
                    while (cellIterator.hasNext()) {        // column iterate
                        Cell cell = cellIterator.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        switch (cell.getColumnIndex()) {
                            case 0:
                                formbean.setFname(cell.getStringCellValue());
                                break;
                            case 1:
                                formbean.setUser_design(cell.getStringCellValue());
                                break;
                            case 2:
                                formbean.setUser_mobile(cell.getStringCellValue());
                                break;
                            case 3:
                                formbean.setMail(cell.getStringCellValue());
                                break;
                            case 4:
                                formbean.setUser_address(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                    }
                    userList.add(formbean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return userList;
    }

    private void insertVPnUser(FormBean bu) {

        String sql = "insert into vpn_users (registration_no,name,designation,mobile,email,address) values (?,?,?,?,?,?)";
        try {
            con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, bu.getRegistration_no());
            ps.setString(2, bu.getFname());
            ps.setString(3, bu.getUser_design());
            ps.setString(4, bu.getUser_mobile());
            ps.setString(5, bu.getMail());
            ps.setString(6, bu.getUser_address());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query of insert vpn user" + ps);

            ps.executeUpdate();
            ps.close();
            // con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }

    }

    public Map validateVpnFile(String renamed_filepath) {

        Map ExcelValidate = new HashMap();
        FileInputStream fis = null;
        try {
            // get total domain
            System.out.println("AAAAAAAAAAAA: " + renamed_filepath);
            fis = new FileInputStream(renamed_filepath);

            org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            FormBean formbean = new FormBean();
            BulkValidation bulkValidate = new BulkValidation();
            Map values = new HashMap();
            ArrayList errorList = new ArrayList();    // error Arraylist
            ArrayList validUsers = new ArrayList();    // users without errors
            ArrayList notvalidUsers = new ArrayList();    // users with errors

            int count = 1;
            if (rowIterator.hasNext()) {
                while (rowIterator.hasNext()) {          // row iterate
                    boolean flag = true;
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {        // column iterate
                        Cell cell = cellIterator.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        switch (cell.getColumnIndex()) {
                            case 0:
                                formbean.setFname(cell.getStringCellValue());
                                break;
                            case 1:
                                formbean.setUser_design(cell.getStringCellValue());
                                break;
                            case 2:
                                formbean.setUser_mobile(cell.getStringCellValue());
                                break;
                            case 3:
                                formbean.setMail(cell.getStringCellValue());
                                break;
                            case 4:
                                formbean.setUser_address(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                    }

                    // start validating
                    // case 1 : fname
                    if (formbean.getFname() == null) {
                        errorList.add("Name can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        values = bulkValidate.Fname(formbean.getFname().trim());
                        if (values.get("valid").equals("true")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " name ok");
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_design() == null) {
                        errorList.add("Designation can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        values = bulkValidate.Designation(formbean.getUser_design().trim());
                        if (values.get("valid").equals("true")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "designation ok");
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_mobile() == null) {
                        errorList.add("Mobile can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        //values = bulkValidate.Mobile(formbean.getUser_mobile().trim());

                        values = bulkValidate.Mobile(formbean.getUser_mobile().trim(), ""); // line modified by pr on 10thsep18

                        if (values.get("valid").equals("true")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "mobile ok");
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    if (formbean.getMail() == null) {
                        errorList.add("Email address can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        values = bulkValidate.Email(formbean.getMail().trim());
                        if (values.get("valid").equals("true")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "mail ok");
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_address() == null) {
                        errorList.add("Address can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        values = bulkValidate.Address(formbean.getUser_address().trim());
                        if (values.get("valid").equals("true")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "add ok");
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    // finish validation check flag value
                    HashMap hm = new LinkedHashMap();
                    if (flag) {   // the row is ok with no error
                        hm.clear();
                        hm.put("Name", formbean.getFname());
                        hm.put("Designation", formbean.getUser_design());
                        hm.put("Mobile(10 digit)", formbean.getUser_mobile());
                        hm.put("Email Address", formbean.getMail());
                        hm.put("Address", formbean.getUser_address());

                        validUsers.add(hm);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "adding in valid");
                    } else {      // row has error

                        hm.clear();
                        hm.put("Name", formbean.getFname());
                        hm.put("Designation", formbean.getUser_design());
                        hm.put("Mobile(10 digit)", formbean.getUser_mobile());
                        hm.put("Email Address", formbean.getMail());
                        hm.put("Address", formbean.getUser_address());

                        notvalidUsers.add(hm);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "adding in non valid");
                    }

                    count++;
                }

                ExcelValidate.put("validUsers", validUsers);
                ExcelValidate.put("notvalidUsers", notvalidUsers);
                ExcelValidate.put("errorList", errorList);
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "FILE IS EMPTY");
                ExcelValidate.put("errorMessage", "Please upload file in correct format.");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERRORLIST: " + errorList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ExcelValidate;
    }

    public int vpn_entry_edit(String field_id, String field_data, String role) {
        String token[] = field_data.split(",");
        String sql = "update vpn_entries set deleted_flag=?,deleted_by=? where registration_no=? and id=?";
        int i = 0;
        try {
            con = DbConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            if (field_id.equals("disable_vpnentry")) {
                ps.setString(1, "Y");
            } else if (field_id.equals("enable_vpnentry")) {
                ps.setString(1, "N");
            }
            ps.setString(2, role);
            ps.setString(3, token[1]);
            ps.setInt(4, Integer.parseInt(token[0]));
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query of update vpn entries table: " + ps);
            i = ps.executeUpdate();

            sql = "select deleted_flag from vpn_entries where registration_no=?";
            Connection conSlave = DbConnection.getSlaveConnection();
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, token[1]);
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()) {

            }

            ps.close();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return i;
    }

    public Boolean vpn_entry_get(String field_data) {
        String token[] = field_data.split(",");
        Boolean flag = false;
        try {
            String sql = "select distinct deleted_flag from vpn_entries where registration_no=?";
            Connection conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps = conSlave.prepareStatement(sql);
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, token[1]);
            ResultSet rs1 = ps.executeQuery();
            while (rs1.next()) {
                if (rs1.getString("deleted_flag").equals("N")) {
                    flag = true; // means show approve button
                }
            }
            ps.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return flag;
    }

}
