
package com.org.dao;

import com.opensymphony.xwork2.ActionSupport;
import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;


public class SmsDao extends ActionSupport {

    public SmsDao() {
    }
    
    String ip = ServletActionContext.getRequest().getRemoteAddr(); 
    Connection con = null;
    Connection conSlave = null;

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int SMS_tab1(Map profile_values, FormBean form_details) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        int generatedKey = 0;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "insert into onlineform_temp_db.sms_registration_tmp (sms_service,pull_url,pull_keyword,short_flag,short_note,"
                    + "app_name,app_url,sms_usage,server_loc,server_loc_other,base_ip,service_ip) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?)";
            String sms_service_final = form_details.getSmsservice1();
            pst = con.prepareStatement(sql, pst.RETURN_GENERATED_KEYS);
            pst.setString(1, sms_service_final.trim());
            pst.setString(2, form_details.getPull_url());
            pst.setString(3, form_details.getPull_keyword());
            pst.setString(4, form_details.getS_code());
            pst.setString(5, form_details.getShort_code());
            pst.setString(6, form_details.getApp_name());
            pst.setString(7, form_details.getApp_url());
            pst.setString(8, form_details.getSms_usage());
            pst.setString(9, form_details.getServer_loc());
            pst.setString(10, form_details.getServer_loc_txt());
            pst.setString(11, form_details.getBase_ip());
            pst.setString(12, form_details.getService_ip());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 1 : " + pst);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 1 : " + e.getMessage());
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

    public void SMS_tab2(Map profile_values, FormBean form_details, String generated_key) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String sql = "";
            sql = "update onlineform_temp_db.sms_registration_tmp set tech_name=?,tech_desig=?,tech_emp_code=?,tech_address=?,tech_city=?,"
                    + "tech_state=?,tech_pin=?,tech_ophone=?,tech_rphone=?,tech_mobile=?,tech_email=? where temp_reg_no=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getT_off_name());
            pst.setString(2, form_details.getTdesignation());
            pst.setString(3, form_details.getTemp_code());
            pst.setString(4, form_details.getTaddrs());
            pst.setString(5, form_details.getTcity());
            pst.setString(6, form_details.getTstate());
            pst.setString(7, form_details.getTpin());
            pst.setString(8, form_details.getTtel_ofc());
            pst.setString(9, form_details.getTtel_res());
            pst.setString(10, form_details.getTmobile());
            pst.setString(11, form_details.getTauth_email());
            pst.setString(12, generated_key);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 2 : " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 2 : " + e.getMessage());
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

    public void SMS_tab3(Map profile_values, FormBean form_details, String generated_key) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String sql = "";
            sql = "update onlineform_temp_db.sms_registration_tmp set bowner_name=?,bowner_desig=?,bowner_emp_code=?,bowner_address=?,bowner_city=?,"
                    + "bowner_state=?,bowner_pin=?,bowner_ophone=?,bowner_rphone=?,bowner_mobile=?,bowner_email=? where temp_reg_no=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getBauth_off_name());
            pst.setString(2, form_details.getBdesignation());
            pst.setString(3, form_details.getBemp_code());
            pst.setString(4, form_details.getBaddrs());
            pst.setString(5, form_details.getBcity());
            pst.setString(6, form_details.getBstate());
            pst.setString(7, form_details.getBpin());
            pst.setString(8, form_details.getBtel_ofc());
            pst.setString(9, form_details.getBtel_res());
            pst.setString(10, form_details.getBmobile());
            pst.setString(11, form_details.getBauth_email());
            pst.setString(12, generated_key);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 3 : " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 3 : " + e.getMessage());
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

    public Map<String, Object> SMS_tab4(FormBean form_details, String generated_key) {
        Map<String, Object> prvwdetails = new HashMap<String, Object>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String sql = "";
            sql = "update onlineform_temp_db.sms_registration_tmp set audit=?,audit_date=?,staging_ip=?,"
                    + "flag_sender=?,sender_id=?,domestic_traffic=?,inter_traffic=? where temp_reg_no=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getAudit());
            pst.setString(2, form_details.getDatepicker1());
            pst.setString(3, form_details.getStaging_ip());
            pst.setString(4, form_details.getSender());
            pst.setString(5, form_details.getSender_id());
            pst.setString(6, form_details.getDomestic_traf());
            pst.setString(7, form_details.getInter_traf());
            pst.setString(8, generated_key);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 4 : " + pst);
            pst.executeUpdate();

            String sql1 = "";
            sql1 = "select * from onlineform_temp_db.sms_registration_tmp where temp_reg_no=?";
            pst = conSlave.prepareStatement(sql1);
            pst.setString(1, generated_key);
            rs = pst.executeQuery();
            while (rs.next()) {
                prvwdetails.put("sms_service", rs.getString("sms_service"));
                prvwdetails.put("pull_url", rs.getString("pull_url"));
                prvwdetails.put("pull_keyword", rs.getString("pull_keyword"));
                prvwdetails.put("short_flag", rs.getString("short_flag"));
                prvwdetails.put("short_note", rs.getString("short_note"));
                prvwdetails.put("app_name", rs.getString("app_name"));
                prvwdetails.put("app_url", rs.getString("app_url"));
                prvwdetails.put("sms_usage", rs.getString("sms_usage"));
                prvwdetails.put("server_loc", rs.getString("server_loc"));
                prvwdetails.put("server_loc_other", rs.getString("server_loc_other"));
                prvwdetails.put("base_ip", rs.getString("base_ip"));
                prvwdetails.put("service_ip", rs.getString("service_ip"));
                prvwdetails.put("tech_name", rs.getString("tech_name"));
                prvwdetails.put("tech_desig", rs.getString("tech_desig"));
                prvwdetails.put("tech_emp_code", rs.getString("tech_emp_code"));
                prvwdetails.put("tech_address", rs.getString("tech_address"));
                prvwdetails.put("tech_city", rs.getString("tech_city"));
                prvwdetails.put("tech_state", rs.getString("tech_state"));
                prvwdetails.put("tech_pin", rs.getString("tech_pin"));
                prvwdetails.put("tech_ophone", rs.getString("tech_ophone"));
                prvwdetails.put("tech_rphone", rs.getString("tech_rphone"));
                prvwdetails.put("tech_mobile", rs.getString("tech_mobile"));
                prvwdetails.put("tech_email", rs.getString("tech_email"));
                prvwdetails.put("bowner_name", rs.getString("bowner_name"));
                prvwdetails.put("bowner_desig", rs.getString("bowner_desig"));
                prvwdetails.put("bowner_emp_code", rs.getString("bowner_emp_code"));
                prvwdetails.put("bowner_address", rs.getString("bowner_address"));
                prvwdetails.put("bowner_state", rs.getString("bowner_state"));
                prvwdetails.put("bowner_city", rs.getString("bowner_city"));
                prvwdetails.put("bowner_pin", rs.getString("bowner_pin"));
                prvwdetails.put("bowner_ophone", rs.getString("bowner_ophone"));
                prvwdetails.put("bowner_rphone", rs.getString("bowner_rphone"));
                prvwdetails.put("bowner_mobile", rs.getString("bowner_mobile"));
                prvwdetails.put("bowner_email", rs.getString("bowner_email"));
                prvwdetails.put("audit", rs.getString("audit"));
                prvwdetails.put("audit_date", rs.getString("audit_date"));
                prvwdetails.put("staging_ip", rs.getString("staging_ip"));
                prvwdetails.put("flag_sender", rs.getString("flag_sender"));
                prvwdetails.put("sender_id", rs.getString("sender_id"));
                prvwdetails.put("domestic_traffic", rs.getString("domestic_traffic"));
                prvwdetails.put("inter_traffic", rs.getString("inter_traffic"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 4 : " + e.getMessage());
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
        return prvwdetails;
    }

    public String SMS_tab5(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null,rs2=null;
        //Connection con = null;
        Boolean errflag = false;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from sms_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

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
            newref = "SMS-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in SMS dao tab 5: " + newref);
            String category = "";
            if (profile_values.get("user_employment").toString().equals("Central")) {
                String qu = "SELECT sms_account_category FROM ministry_department where dept=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("dept").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("sms_account_category");
                } else {
                    category = "paid";
                }
                
            } else if (profile_values.get("user_employment").toString().equals("Others")) {
                String qu = "SELECT sms_account_category FROM organization where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("sms_account_category");
                } else {
                    category = "paid";
                }
               
            } else {
                category = "paid";
            }
            String sql = "insert into sms_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,sms_service,pull_url,pull_keyword,short_flag,short_note,"
                    + "app_name,app_url,sms_usage,server_loc,server_loc_other,base_ip,service_ip,tech_name,tech_desig,tech_emp_code,tech_address,tech_city,"
                    + "tech_state,tech_pin,tech_ophone,tech_rphone,tech_mobile,tech_email,bowner_name,bowner_desig,bowner_emp_code,"
                    + "bowner_address,bowner_city,bowner_state,bowner_pin,bowner_ophone,bowner_rphone,bowner_mobile,bowner_email,"
                    + "audit,audit_date,staging_ip,flag_sender,sender_id,domestic_traffic,inter_traffic,registration_no,userip,datetime,description,ca_desig) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?)";

            String sms_service_final = form_details.getSmsservice1();
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
            pst.setString(22, sms_service_final.trim());
            pst.setString(23, form_details.getPull_url());
            pst.setString(24, form_details.getPull_keyword());
            pst.setString(25, form_details.getS_code());
            pst.setString(26, form_details.getShort_code());
            pst.setString(27, form_details.getApp_name());
            pst.setString(28, form_details.getApp_url());
            pst.setString(29, form_details.getSms_usage());
            pst.setString(30, form_details.getServer_loc());
            pst.setString(31, form_details.getServer_loc_txt());
            pst.setString(32, form_details.getBase_ip());
            pst.setString(33, form_details.getService_ip());
            pst.setString(34, form_details.getT_off_name());
            pst.setString(35, form_details.getTdesignation());
            pst.setString(36, form_details.getTemp_code());
            pst.setString(37, form_details.getTaddrs());
            pst.setString(38, form_details.getTcity());
            pst.setString(39, form_details.getTstate());
            pst.setString(40, form_details.getTpin());
            pst.setString(41, form_details.getTtel_ofc());
            pst.setString(42, form_details.getTtel_res());
            pst.setString(43, form_details.getTmobile());
            pst.setString(44, form_details.getTauth_email());
            pst.setString(45, form_details.getBauth_off_name());
            pst.setString(46, form_details.getBdesignation());
            pst.setString(47, form_details.getBemp_code());
            pst.setString(48, form_details.getBaddrs());
            pst.setString(49, form_details.getBcity());
            pst.setString(50, form_details.getBstate());
            pst.setString(51, form_details.getBpin());
            pst.setString(52, form_details.getBtel_ofc());
            pst.setString(53, form_details.getBtel_res());
            pst.setString(54, form_details.getBmobile());
            pst.setString(55, form_details.getBauth_email());
            pst.setString(56, form_details.getAudit());
            pst.setString(57, form_details.getDatepicker1());
            pst.setString(58, form_details.getStaging_ip());
            pst.setString(59, form_details.getSender());
            pst.setString(60, form_details.getSender_id());
            pst.setString(61, form_details.getDomestic_traf());
            pst.setString(62, form_details.getInter_traf());
            pst.setString(63, newref);
            pst.setString(64, ip);
            pst.setString(65, category);
            pst.setString(66, form_details.getCa_design().trim());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 5 : " + pst);
            pst.executeUpdate();

        } catch (Exception e) {
            errflag = true;
            newref = "";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 5 : " + e.getMessage());
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
                String search_regnum = "select registration_no from sms_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

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
                newref = "SMS-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in SMS dao tab 5: " + newref);
                String category = "";
                if (profile_values.get("user_employment").toString().equals("Central")) {
                    String qu = "SELECT sms_account_category FROM ministry_department where dept=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("dept").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("sms_account_category");
                    } else {
                        category = "paid";
                    }
                    
                } else if (profile_values.get("user_employment").toString().equals("Others")) {
                    String qu = "SELECT sms_account_category FROM organization where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                   rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("sms_account_category");
                    } else {
                        category = "paid";
                    }
                   
                } else {
                    category = "paid";
                }
                String sql = "insert into sms_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,sms_service,pull_url,pull_keyword,short_flag,short_note,"
                        + "app_name,app_url,sms_usage,server_loc,server_loc_other,base_ip,service_ip,tech_name,tech_desig,tech_emp_code,tech_address,tech_city,"
                        + "tech_state,tech_pin,tech_ophone,tech_rphone,tech_mobile,tech_email,bowner_name,bowner_desig,bowner_emp_code,"
                        + "bowner_address,bowner_city,bowner_state,bowner_pin,bowner_ophone,bowner_rphone,bowner_mobile,bowner_email,"
                        + "audit,audit_date,staging_ip,flag_sender,sender_id,domestic_traffic,inter_traffic,registration_no,userip,datetime,description,ca_desig) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?)";

                String sms_service_final = form_details.getSmsservice1();
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
                pst.setString(22, sms_service_final.trim());
                pst.setString(23, form_details.getPull_url());
                pst.setString(24, form_details.getPull_keyword());
                pst.setString(25, form_details.getS_code());
                pst.setString(26, form_details.getShort_code());
                pst.setString(27, form_details.getApp_name());
                pst.setString(28, form_details.getApp_url());
                pst.setString(29, form_details.getSms_usage());
                pst.setString(30, form_details.getServer_loc());
                pst.setString(31, form_details.getServer_loc_txt());
                pst.setString(32, form_details.getBase_ip());
                pst.setString(33, form_details.getService_ip());
                pst.setString(34, form_details.getT_off_name());
                pst.setString(35, form_details.getTdesignation());
                pst.setString(36, form_details.getTemp_code());
                pst.setString(37, form_details.getTaddrs());
                pst.setString(38, form_details.getTcity());
                pst.setString(39, form_details.getTstate());
                pst.setString(40, form_details.getTpin());
                pst.setString(41, form_details.getTtel_ofc());
                pst.setString(42, form_details.getTtel_res());
                pst.setString(43, form_details.getTmobile());
                pst.setString(44, form_details.getTauth_email());
                pst.setString(45, form_details.getBauth_off_name());
                pst.setString(46, form_details.getBdesignation());
                pst.setString(47, form_details.getBemp_code());
                pst.setString(48, form_details.getBaddrs());
                pst.setString(49, form_details.getBcity());
                pst.setString(50, form_details.getBstate());
                pst.setString(51, form_details.getBpin());
                pst.setString(52, form_details.getBtel_ofc());
                pst.setString(53, form_details.getBtel_res());
                pst.setString(54, form_details.getBmobile());
                pst.setString(55, form_details.getBauth_email());
                pst.setString(56, form_details.getAudit());
                pst.setString(57, form_details.getDatepicker1());
                pst.setString(58, form_details.getStaging_ip());
                pst.setString(59, form_details.getSender());
                pst.setString(60, form_details.getSender_id());
                pst.setString(61, form_details.getDomestic_traf());
                pst.setString(62, form_details.getInter_traf());
                pst.setString(63, newref);
                pst.setString(64, ip);
                pst.setString(65, category);
                pst.setString(66, form_details.getCa_design().trim());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 5 : " + pst);
                pst.executeUpdate();

            } catch (Exception e) {
                //errflag = true;
                newref = "";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 5 : " + e.getMessage());
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

    public Map<String, Object> fill_sms_admin_tab(String otp_type, String email, String mobile) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        //Connection con = null;
        Map<String, Object> details = new HashMap<String, Object>();
        try {

//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            // get bean of profile            
            String sql = "";
            if (otp_type.equals("emailOnly")) {
                sql = "select email,mobile,name,ophone,rphone,designation,emp_code,address,city,add_state,pin from user_profile where email=?";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, email);
            } else if (otp_type.equals("mobileOnly")) {
                sql = "select * from user_profile where mobile=?";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, mobile);
            } else if (otp_type.equals("both")) {
                sql = "select * from user_profile where email=? and mobile=?";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, email);
                pst.setString(2, mobile);
            } else {

            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 6 : " + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                details.put("detail_email", rs.getString("email"));
                details.put("detail_mobile", rs.getString("mobile"));
                details.put("detail_name", rs.getString("name"));
                details.put("detail_ophone", rs.getString("ophone"));
                details.put("detail_rphone", rs.getString("rphone"));
                details.put("detail_desig", rs.getString("designation"));
                details.put("detail_empcode", rs.getString("emp_code"));
                details.put("detail_address", rs.getString("address"));
                details.put("detail_city", rs.getString("city"));
                details.put("detail_state", rs.getString("add_state"));
                details.put("detail_pin", rs.getString("pin"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 6 : " + e.getMessage());
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
        return details;
    }

    public void insertTelnet(String telnet, String ref_num) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qryStr = "Update sms_registration set telnet_response = ? where registration_no = ?";
            pst = con.prepareStatement(qryStr);
            pst.setString(1, telnet);
            pst.setString(2, ref_num);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 7 : " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 7 : " + e.getMessage());
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

    public void insertFirewallId(String firewall_id, String ref_num) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qryStr = "Update sms_registration set req_id = ? where registration_no = ?";
            pst = con.prepareStatement(qryStr);
            pst.setString(1, firewall_id);
            pst.setString(2, ref_num);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SMS dao tab 8 : " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SMS dao tab 8 : " + e.getMessage());
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
}
