/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author dhiren
 */
public class MobileDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;
    String inserted_regNo = "";

    public void Mobile_tab1(FormBean form_details) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "insert into onlineform_temp_db.mobile_registration_tmp (country_code,new_mobile,ip) values (?,?,?)";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Mobile value :" + form_details.getNew_mobile());
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getCountry_code());
            pst.setString(2, form_details.getNew_mobile());
            pst.setString(3, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "MOBILE dao tab 1 :" + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in MOBILE dao tab 1: " + e.getMessage());
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

    public String Mobile_tab2(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        //Connection con = null;
        Boolean errflag = false;
        String excpn = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;
            int totalLength = 0;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            if (profile_values.get("oldRegNumberFromException") != null && !profile_values.get("oldRegNumberFromException").toString().isEmpty()) {
                dbrefno = profile_values.get("oldRegNumberFromException").toString();
            } else {
                String search_regnum = "select registration_no from mobile_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";// order by added by pr on 9thjun2020
                //pst = conSlave.prepareStatement(search_regnum);
                System.out.println("Searched reg number from base table:: " + search_regnum);;
                pst = conSlave.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                   // dbrefno = "MOBILE-FORM202206300008"; for testing purpose only by rahul
                }
            }

            if (dbrefno == null || dbrefno.isEmpty()) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                totalLength = dbrefno.length();
                String lastst = "";
                lastst = dbrefno.substring(totalLength - 4);

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
            if (profile_values.get("mobile").toString().contains(form_details.getNew_mobile())) {
                newref = "PROFILE-FORM" + pdate1 + newref;
            } else {
                newref = "MOBILE-FORM" + pdate1 + newref;
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in MOBILE tab 2: " + newref);
            String sql = "insert into mobile_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,country_code,new_mobile,registration_no,userip,ca_desig,remarks,remarks_flag,nic_dateofbirth,nic_dateofretirement,nic_designation,nic_displayname) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(18, form_details.getHod_name().trim());
            pst.setString(19, form_details.getHod_email().trim());
            pst.setString(20, form_details.getHod_mobile().trim());
            pst.setString(21, form_details.getHod_tel().trim());
            pst.setString(22, form_details.getCountry_code());
            pst.setString(23, form_details.getNew_mobile());
            pst.setString(24, newref);
            pst.setString(25, ip);
            pst.setString(26, form_details.getCa_design().trim());
            if (form_details.getRemarks().equals("other")) {
                pst.setString(27, form_details.getOther_remarks());
                pst.setString(28, "y");

            } else {
                pst.setString(27, form_details.getRemarks());
                pst.setString(28, "n");

            }
            pst.setString(29, form_details.getNicDateOfBirth().trim());
            pst.setString(30, form_details.getNicDateOfRetirement().trim());
            pst.setString(31, form_details.getDesignation().trim());
            pst.setString(32, form_details.getDisplayName().trim());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "MOBILE dao tab 2 :" + pst);
            int i = pst.executeUpdate();
            inserted_regNo = newref;
        } catch (Exception e) {
            errflag = true;
            //e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in MOBILE dao tab 2: " + e.getMessage());
            excpn = e.getMessage();
            System.out.println("Mobile_tab2()--Inside catch block : " + excpn);
            if (excpn.contains("Duplicate entry")) {
                profile_values.put("oldRegNumberFromException", newref);
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                Mobile_tab2(form_details, profile_values);
                errflag = false;
            }
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
                int totalLength = 0;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from mobile_registration where date(datetime)=date(now()) group by registration_no  order by registration_no desc limit 1";// order by added by pr on 9thjun2020
                pst = con.prepareStatement(search_regnum);
                System.out.println("Searched reg number from base table in errorFlag block:: " + search_regnum);;
                pst = con.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
//                  String lastst = dbrefno.substring(19, dbrefno.length());
                    totalLength = dbrefno.length();
                    String lastst = "";
                    lastst = dbrefno.substring(totalLength - 4);
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

                if (profile_values.get("mobile").toString().contains(form_details.getNew_mobile())) {
                    newref = "PROFILE-FORM" + pdate1 + newref;
                } else {
                    newref = "MOBILE-FORM" + pdate1 + newref;
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in MOBILE tab 2: " + newref);
                String sql = "insert into mobile_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,country_code,new_mobile,registration_no,userip,ca_desig,nic_dateofbirth,nic_dateofretirement,nic_designation,nic_displayname) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(18, form_details.getHod_name().trim());
                pst.setString(19, form_details.getHod_email().trim());
                pst.setString(20, form_details.getHod_mobile().trim());
                pst.setString(21, form_details.getHod_tel().trim());
                pst.setString(22, form_details.getCountry_code());
                pst.setString(23, form_details.getNew_mobile());
                pst.setString(24, newref);
                pst.setString(25, ip);
                pst.setString(26, form_details.getCa_design().trim());
                pst.setString(27, form_details.getNicDateOfBirth().trim());
                pst.setString(28, form_details.getNicDateOfRetirement().trim());
                pst.setString(29, form_details.getDesignation().trim());
                pst.setString(30, form_details.getDisplayName().trim());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "MOBILE dao tab 2 :" + pst);
                int i = pst.executeUpdate();
                inserted_regNo = newref;
            } catch (Exception e) {
                //errflag = true;
                newref = "";
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in MOBILE dao tab 2 inside error flag blcok: " + e.getMessage());
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
        return inserted_regNo;
    }
}
