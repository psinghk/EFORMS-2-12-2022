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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author dhiren
 */
public class WebcastDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public int Webcast_tab1(FormBean form_details) {
        int generatedKey = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile     
            String sql = "";
            if (form_details.getReq_for().equals("live")) {
                sql = "insert into onlineform_temp_db.webcast_registration_tmp(request_type,event_name_eng,event_name_hindi,event_start,event_end,event_type,event_telecast,"
                        + "channel_name,live_feed,vc_id,conf_radio,conf_name,conf_type,conf_city,conf_schedule,conf_session,conf_bw,conf_provider,conf_event_hired,"
                        + "conf_flash,conf_video,conf_contact,payment,cheque_no,cheque_amount,cheque_date,bank_name,remarks,ip,hall_type,hall_number,local_setup,"
                        + "event_coo_name,event_coo_email,event_coo_design,event_coo_mobile,event_coo_address) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, pst.RETURN_GENERATED_KEYS);
                pst.setString(1, form_details.getReq_for());
                pst.setString(2, form_details.getEvent_name_eng());
                pst.setString(3, form_details.getEvent_name_hin());
                pst.setString(4, form_details.getEvent_start_date());
                pst.setString(5, form_details.getEvent_end_date());
                pst.setString(6, form_details.getEvent_type());
                pst.setString(7, form_details.getTelecast());
                pst.setString(8, form_details.getChannel_name());
                pst.setString(9, form_details.getLive_feed());
                pst.setString(10, form_details.getVc_id());
                if (form_details.getConf_radio().equalsIgnoreCase("yes")) {
                    pst.setString(11, "conference");
                } else if (form_details.getConf_radio().equalsIgnoreCase("no")) {
                    pst.setString(11, "workshop");
                }
                pst.setString(12, form_details.getConf_name());
                pst.setString(13, form_details.getConf_type());
                pst.setString(14, form_details.getConf_city());
                pst.setString(15, form_details.getConf_schedule());
                pst.setString(16, form_details.getConf_session());
                pst.setString(17, form_details.getConf_bw());
                pst.setString(18, form_details.getConf_provider());
                pst.setString(19, form_details.getConf_event_hired());
                pst.setString(20, form_details.getConf_flash());
                pst.setString(21, form_details.getConf_video());
                pst.setString(22, form_details.getConf_contact());
                pst.setString(23, form_details.getPayment());
                pst.setString(24, form_details.getCheque_no());
                pst.setString(25, form_details.getCheque_amount());
                pst.setString(26, form_details.getCheque_date());
                pst.setString(27, form_details.getBank_name());
                pst.setString(28, form_details.getRemarks());
                pst.setString(29, ip);
                pst.setString(30, form_details.getHall_type());
                pst.setString(31, form_details.getHall_number());
                pst.setString(32, form_details.getLocal_setup());
                pst.setString(33, form_details.getEvent_coo_name());
                pst.setString(34, form_details.getEvent_coo_email());
                pst.setString(35, form_details.getEvent_coo_design());
                pst.setString(36, form_details.getEvent_coo_mobile());
                pst.setString(37, form_details.getEvent_coo_address());
            } else if (form_details.getReq_for().equals("demand")) {
                sql = "insert into onlineform_temp_db.webcast_registration_tmp(request_type,event_size,media_format,payment,cheque_no,cheque_amount,"
                        + "cheque_date,bank_name,remarks,ip,event_no) values(?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql, pst.RETURN_GENERATED_KEYS);
                pst.setString(1, form_details.getReq_for());
                pst.setString(2, form_details.getEvent_size());
                pst.setString(3, form_details.getMedia_format());
                pst.setString(4, form_details.getPayment());
                pst.setString(5, form_details.getCheque_no());
                pst.setString(6, form_details.getCheque_amount());
                pst.setString(7, form_details.getCheque_date());
                pst.setString(8, form_details.getBank_name());
                pst.setString(9, form_details.getRemarks());
                pst.setString(10, ip);
                pst.setString(11, form_details.getEvent_no());
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WEBCAST dao tab 1: " + pst);
            //  pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception  in WEBCAST dao tab 1: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
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
        return generatedKey;
    }

    public String Webcast_tab2(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        //Connection con = null;
        Boolean errflag = false;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from webcast_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");

            }

            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(20, dbrefno.length());

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
            newref = "WEBCAST-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in WEBCAST dao tab 2: " + newref);
            String sql = "";

            sql = "insert into webcast_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,request_type,event_name_eng,event_name_hindi,event_start,event_type,event_telecast,channel_name,live_feed,vc_id,event_size,"
                    + "media_format,payment,cheque_no,cheque_amount,cheque_date,bank_name,conf_name,conf_type,conf_city,conf_schedule,conf_session,conf_bw,conf_provider,"
                    + "conf_event_hired,conf_flash,conf_video,conf_contact,remarks,fwd_ofc_name,fwd_ofc_email,fwd_ofc_mobile,fwd_ofc_desig,fwd_ofc_add,fwd_ofc_tel,hod_name,hod_email,"
                    + "hod_mobile,hod_telephone,ca_desig,registration_no,userip,conf_radio,hall_type,hall_number,event_end,local_setup,event_no,event_coo_name,event_coo_email,event_coo_design,event_coo_mobile,event_coo_address)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(18, form_details.getReq_for());

            if (form_details.getReq_for().equals("live")) {
                pst.setString(19, form_details.getEvent_name_eng());
                pst.setString(20, form_details.getEvent_name_hin());
                pst.setString(21, form_details.getEvent_start_date());
                pst.setString(22, form_details.getEvent_type());
                pst.setString(23, form_details.getTelecast());
                if (form_details.getTelecast().equals("yes")) {
                    pst.setString(24, form_details.getChannel_name());
                    pst.setString(25, "");
                } else {
                    pst.setString(24, "");
                    pst.setString(25, form_details.getLive_feed());
                }
                if (form_details.getLive_feed().equalsIgnoreCase("Through VC")) {
                    pst.setString(26, form_details.getVc_id());
                } else {
                    pst.setString(26, "");
                }
                pst.setString(27, form_details.getEvent_size());
                pst.setString(28, form_details.getMedia_format());
                pst.setString(29, form_details.getPayment());
                if (form_details.getPayment().equals("yes")) {
                    pst.setString(30, form_details.getCheque_no());
                    pst.setString(31, form_details.getCheque_amount());
                    pst.setString(32, form_details.getCheque_date());
                    pst.setString(33, form_details.getBank_name());
                } else {
                    pst.setString(30, "");
                    pst.setString(31, "");
                    pst.setString(32, "");
                    pst.setString(33, "");
                }

                pst.setString(34, form_details.getConf_name());
                pst.setString(35, form_details.getConf_type());
                pst.setString(36, form_details.getConf_city());
                pst.setString(37, form_details.getConf_schedule());
                pst.setString(38, form_details.getConf_session());
                pst.setString(39, form_details.getConf_bw());
                pst.setString(40, form_details.getConf_provider());
                pst.setString(41, form_details.getConf_event_hired());
                pst.setString(42, form_details.getConf_flash());
                pst.setString(43, form_details.getConf_video());
                if (form_details.getConf_video().equals("yes")) {
                    pst.setString(44, form_details.getConf_contact());
                } else {
                    pst.setString(44, "");
                }
            } else if (form_details.getReq_for().equals("demand")) {
                pst.setString(19, "");
                pst.setString(20, "");
                pst.setString(21, "");
                pst.setString(22, "");
                pst.setString(23, "");
                pst.setString(24, "");
                pst.setString(25, "");
                pst.setString(26, "");
                pst.setString(27, form_details.getEvent_size());
                pst.setString(28, form_details.getMedia_format());
                pst.setString(29, form_details.getPayment());
                if (form_details.getPayment().equals("yes")) {
                    pst.setString(30, form_details.getCheque_no());
                    pst.setString(31, form_details.getCheque_amount());
                    pst.setString(32, form_details.getCheque_date());
                    pst.setString(33, form_details.getBank_name());
                } else {
                    pst.setString(30, "");
                    pst.setString(31, "");
                    pst.setString(32, "");
                    pst.setString(33, "");
                }
                pst.setString(34, "");
                pst.setString(35, "");
                pst.setString(36, "");
                pst.setString(37, "");
                pst.setInt(38, 0);
                pst.setString(39, "");
                pst.setString(40, "");
                pst.setString(41, "");
                pst.setString(42, "");
                pst.setString(43, "");
                pst.setString(44, "");
            }
            pst.setString(45, form_details.getRemarks());
            pst.setString(46, form_details.getFwd_ofc_name());
            pst.setString(47, form_details.getFwd_ofc_email());
            pst.setString(48, form_details.getFwd_ofc_mobile());
            pst.setString(49, form_details.getFwd_ofc_design());
            pst.setString(50, form_details.getFwd_ofc_add());
            pst.setString(51, form_details.getFwd_ofc_tel());
            pst.setString(52, profile_values.get("ca_name").toString());
            pst.setString(53, profile_values.get("hod_email").toString());
            pst.setString(54, profile_values.get("ca_mobile").toString());
            pst.setString(55, profile_values.get("hod_tel").toString());
            pst.setString(56, profile_values.get("ca_design").toString());
            pst.setString(57, newref);
            pst.setString(58, ip);
            if (form_details.getReq_for().equals("live")) {
                if (form_details.getConf_radio().equalsIgnoreCase("yes")) {
                    pst.setString(59, "conference");
                } else if (form_details.getConf_radio().equalsIgnoreCase("no")) {
                    pst.setString(59, "workshop");
                }
                pst.setString(60, form_details.getHall_type());
                pst.setString(61, form_details.getHall_number());
                pst.setString(62, form_details.getEvent_end_date());
                if (form_details.getConf_flash().equals("no")) {
                    pst.setString(63, form_details.getLocal_setup());
                } else {
                    pst.setString(63, "");
                }
                pst.setString(64, "");
                pst.setString(65, form_details.getEvent_coo_name());
                pst.setString(66, form_details.getEvent_coo_email());
                pst.setString(67, form_details.getEvent_coo_design());
                pst.setString(68, form_details.getEvent_coo_mobile());
                pst.setString(69, form_details.getEvent_coo_address());
            } else {
                pst.setString(59, "");
                pst.setString(60, "");
                pst.setString(61, "");
                pst.setString(62, "");
                pst.setString(63, "");
                pst.setString(64, form_details.getEvent_no());
                pst.setString(65, "");
                pst.setString(66, "");
                pst.setString(67, "");
                pst.setString(68, "");
                pst.setString(69, "");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WEBCAST dao tab 2 :" + pst);
            int i = pst.executeUpdate();

            // insert into table
            if (i > 0 && form_details.getWebcast_uploaded_files()!=null) {
                Map<String, String> filename = (HashMap) form_details.getWebcast_uploaded_files();
                for (Map.Entry<String, String> entry : filename.entrySet()) {
                    pst = con.prepareStatement("insert into webcast_uploaded_files values(?,?,?,now())");
                    pst.setString(1, newref);
                    pst.setString(2, entry.getKey());
                    pst.setString(3, entry.getValue());
                    pst.executeUpdate();
                }
            }

        } catch (Exception e) {
            errflag = true;
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in WEBCAST dao tab 2: " + e.getMessage());
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
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from webcast_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");

                }

                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(20, dbrefno.length());

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
                newref = "WEBCAST-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in WEBCAST dao tab 2: " + newref);

                String sql = "";

                sql = "insert into webcast_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,request_type,event_name_eng,event_name_hindi,event_start,event_type,event_telecast,channel_name,live_feed,vc_id,event_size,"
                        + "media_format,payment,cheque_no,cheque_amount,cheque_date,bank_name,conf_name,conf_type,conf_city,conf_schedule,conf_session,conf_bw,conf_provider,"
                        + "conf_event_hired,conf_flash,conf_video,conf_contact,remarks,fwd_ofc_name,fwd_ofc_email,fwd_ofc_mobile,fwd_ofc_desig,fwd_ofc_add,fwd_ofc_tel,hod_name,hod_email,"
                        + "hod_mobile,hod_telephone,ca_desig,registration_no,userip,conf_radio,hall_type,hall_number,event_end,local_setup,event_no,event_coo_name,event_coo_email,event_coo_design,event_coo_mobile,event_coo_address)"
                        + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(18, form_details.getReq_for());

                if (form_details.getReq_for().equals("live")) {
                    pst.setString(19, form_details.getEvent_name_eng());
                    pst.setString(20, form_details.getEvent_name_hin());
                    pst.setString(21, form_details.getEvent_start_date());
                    pst.setString(22, form_details.getEvent_type());
                    pst.setString(23, form_details.getTelecast());
                    if (form_details.getTelecast().equals("yes")) {
                        pst.setString(24, form_details.getChannel_name());
                        pst.setString(25, "");
                    } else {
                        pst.setString(24, "");
                        pst.setString(25, form_details.getLive_feed());
                    }
                    if (form_details.getLive_feed().equalsIgnoreCase("Through VC")) {
                        pst.setString(26, form_details.getVc_id());
                    } else {
                        pst.setString(26, "");
                    }
                    pst.setString(27, form_details.getEvent_size());
                    pst.setString(28, form_details.getMedia_format());
                    pst.setString(29, form_details.getPayment());
                    if (form_details.getPayment().equals("yes")) {
                        pst.setString(30, form_details.getCheque_no());
                        pst.setString(31, form_details.getCheque_amount());
                        pst.setString(32, form_details.getCheque_date());
                        pst.setString(33, form_details.getBank_name());
                    } else {
                        pst.setString(30, "");
                        pst.setString(31, "");
                        pst.setString(32, "");
                        pst.setString(33, "");
                    }

                    pst.setString(34, form_details.getConf_name());
                    pst.setString(35, form_details.getConf_type());
                    pst.setString(36, form_details.getConf_city());
                    pst.setString(37, form_details.getConf_schedule());
                    pst.setString(38, form_details.getConf_session());
                    pst.setString(39, form_details.getConf_bw());
                    pst.setString(40, form_details.getConf_provider());
                    pst.setString(41, form_details.getConf_event_hired());
                    pst.setString(42, form_details.getConf_flash());
                    pst.setString(43, form_details.getConf_video());
                    if (form_details.getConf_video().equals("yes")) {
                        pst.setString(44, form_details.getConf_contact());
                    } else {
                        pst.setString(44, "");
                    }
                } else if (form_details.getReq_for().equals("demand")) {
                    pst.setString(19, "");
                    pst.setString(20, "");
                    pst.setString(21, "");
                    pst.setString(22, "");
                    pst.setString(23, "");
                    pst.setString(24, "");
                    pst.setString(25, "");
                    pst.setString(26, "");
                    pst.setString(27, form_details.getEvent_size());
                    pst.setString(28, form_details.getMedia_format());
                    pst.setString(29, form_details.getPayment());
                    if (form_details.getPayment().equals("yes")) {
                        pst.setString(30, form_details.getCheque_no());
                        pst.setString(31, form_details.getCheque_amount());
                        pst.setString(32, form_details.getCheque_date());
                        pst.setString(33, form_details.getBank_name());
                    } else {
                        pst.setString(30, "");
                        pst.setString(31, "");
                        pst.setString(32, "");
                        pst.setString(33, "");
                    }
                    pst.setString(34, "");
                    pst.setString(35, "");
                    pst.setString(36, "");
                    pst.setString(37, "");
                    pst.setInt(38, 0);
                    pst.setString(39, "");
                    pst.setString(40, "");
                    pst.setString(41, "");
                    pst.setString(42, "");
                    pst.setString(43, "");
                    pst.setString(44, "");
                }
                pst.setString(45, form_details.getRemarks());
                pst.setString(46, form_details.getFwd_ofc_name());
                pst.setString(47, form_details.getFwd_ofc_email());
                pst.setString(48, form_details.getFwd_ofc_mobile());
                pst.setString(49, form_details.getFwd_ofc_design());
                pst.setString(50, form_details.getFwd_ofc_add());
                pst.setString(51, form_details.getFwd_ofc_tel());
                pst.setString(52, profile_values.get("ca_name").toString());
                pst.setString(53, profile_values.get("hod_email").toString());
                pst.setString(54, profile_values.get("ca_mobile").toString());
                pst.setString(55, profile_values.get("hod_tel").toString());
                pst.setString(56, profile_values.get("ca_design").toString());
                pst.setString(57, newref);
                pst.setString(58, ip);
                if (form_details.getReq_for().equals("live")) {
                    if (form_details.getConf_radio().equalsIgnoreCase("yes")) {
                        pst.setString(59, "conference");
                    } else if (form_details.getConf_radio().equalsIgnoreCase("no")) {
                        pst.setString(59, "workshop");
                    }
                    pst.setString(60, form_details.getHall_type());
                    pst.setString(61, form_details.getHall_number());
                    pst.setString(62, form_details.getEvent_end_date());
                    if (form_details.getConf_flash().equals("no")) {
                        pst.setString(63, form_details.getLocal_setup());
                    } else {
                        pst.setString(63, "");
                    }
                    pst.setString(64, "");
                    pst.setString(65, form_details.getEvent_coo_name());
                    pst.setString(66, form_details.getEvent_coo_email());
                    pst.setString(67, form_details.getEvent_coo_design());
                    pst.setString(68, form_details.getEvent_coo_mobile());
                    pst.setString(69, form_details.getEvent_coo_address());
                } else {
                    pst.setString(59, "");
                    pst.setString(60, "");
                    pst.setString(61, "");
                    pst.setString(62, "");
                    pst.setString(63, "");
                    pst.setString(64, form_details.getEvent_no());
                    pst.setString(65, "");
                    pst.setString(66, "");
                    pst.setString(67, "");
                    pst.setString(68, "");
                    pst.setString(69, "");
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WEBCAST dao tab 2 :" + pst);
                int i = pst.executeUpdate();

                // insert into table
                if (i > 0 && form_details.getWebcast_uploaded_files()!=null) {
                    Map<String, String> filename = (HashMap) form_details.getWebcast_uploaded_files();
                    for (Map.Entry<String, String> entry : filename.entrySet()) {
                        pst = con.prepareStatement("insert into webcast_uploaded_files values(?,?,?,now())");
                        pst.setString(1, newref);
                        pst.setString(2, entry.getKey());
                        pst.setString(3, entry.getValue());
                        pst.executeUpdate();
                    }
                }
            } catch (Exception e) {
                errflag = true;
                // e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in WEBCAST dao tab 2: " + e.getMessage());
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
}
