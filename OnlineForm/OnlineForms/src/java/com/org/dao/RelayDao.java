package com.org.dao;

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

public class RelayDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public void RELAY_tab1(FormBean form_details, String relayip) {
        //int temp_common_id = id;
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile
            String sql = "insert into onlineform_temp_db.relay_registration_tmp (division_name,app_name,os,"
                    + "app_ip,staging_ip,server_loc,server_loc_other,userip,uploaded_filename,renamed_filepath)"
                    + "values (?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getDivision());
            pst.setString(2, form_details.getRelay_app_name());
            pst.setString(3, form_details.getOs());
            pst.setString(4, relayip);
            pst.setString(5, form_details.getIp_staging());
            pst.setString(6, form_details.getServer_loc());
            pst.setString(7, form_details.getServer_loc_txt());
            pst.setString(8, ip);
            pst.setString(9, form_details.getUploaded_filename());
            pst.setString(10, form_details.getRenamed_filepath());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "RELAY dao tab 1: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in RELAY dao tab 1 : " + e.getMessage());
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

    public String RELAY_tab2(FormBean form_details, String relay_ip, String old_relay_ip, Map profile_values) {

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
            String search_regnum = "select registration_no from relay_registration where date(datetime)=date(now()) group by registration_no ORDER BY registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");

            }
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(18, dbrefno.length());

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
            newref = "RELAY-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in RELAY dao tab 2: " + newref);

            String sql = "insert into relay_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,division_name,app_name,os,"
                    + "app_ip,staging_ip,server_loc,server_loc_other,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,relay_app_url,"
                    + "relay_sender_id,domain_mx,port,spf,dkim,dmarc,relay_auth_id,relay_old_ip,relay_mailsent,req_type,point_mobile_number,point_email,point_name,landline_number,security_audit,security_exp_date,other_mail_type,point_contact,is_hosted_nic,mail_type,hardware_filename,renamed_hardware_filepath) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getDivision());
            pst.setString(23, form_details.getRelay_app_name());
            pst.setString(24, form_details.getOs());
            pst.setString(25, relay_ip);
            pst.setString(26, form_details.getIp_staging());
            pst.setString(27, form_details.getServer_loc());
            pst.setString(28, form_details.getServer_loc_txt());
            pst.setString(29, form_details.getUploaded_filename());
            pst.setString(30, form_details.getRenamed_filepath());
            pst.setString(31, newref);
            pst.setString(32, ip);
            pst.setString(33, form_details.getCa_design().trim());
            pst.setString(34, form_details.getRelay_app_url());
            pst.setString(35, form_details.getRelay_sender_id());
            pst.setString(36, form_details.getDomain_mx());
            pst.setString(37, form_details.getSmtp_port());
            pst.setString(38, form_details.getSpf());
            pst.setString(39, form_details.getDkim());
            pst.setString(40, form_details.getDmarc());
            pst.setString(41, form_details.getRelay_auth_id());
            pst.setString(42, old_relay_ip);
            pst.setString(43, form_details.getMailsent());
            pst.setString(44, form_details.getReq_());
            pst.setString(45, form_details.getMobile_number());
            pst.setString(46, form_details.getPoint_email());
            pst.setString(47, form_details.getPoint_name());
            pst.setString(48, form_details.getLandline_number());
            pst.setString(49, form_details.getSecurity_audit());
            pst.setString(50, form_details.getSecurity_exp_date());
            pst.setString(51, form_details.getOther_mail_type());
            pst.setString(52, form_details.getPoint_contact());
            pst.setString(53, form_details.getIs_hosted());
            if (form_details.getOtp_mail_service() != null) {
            } else {
                form_details.setOtp_mail_service("otp_mail_service_no");
            }
            if (form_details.getMail_type_trans_mail() != null) {
            } else {
                form_details.setMail_type_trans_mail("mail_type_trans_mail_no");
            }
            if (form_details.getMail_type_reg_mail() != null) {
            } else {
                form_details.setMail_type_reg_mail("mail_type_reg_mail_no");
            }
            if (form_details.getMail_type_forgotpass() != null) {
            } else {
                form_details.setMail_type_forgotpass("mail_type_forgotpass_no");
            }
            if (form_details.getMail_type_alert() != null) {
            } else {
                form_details.setMail_type_alert("mail_type_alert_no");
            }
            if (form_details.getMail_type_other() != null) {
            } else {
                form_details.setMail_type_other("mail_type_other_no");
            }
            if (form_details.getOther_mail_type() != null) {
            } else {
                form_details.setOther_mail_type("other_mail_type_no");
            }

            pst.setString(53, form_details.getIs_hosted());
            pst.setString(54, form_details.getOtp_mail_service()+","+form_details.getMail_type_trans_mail()+","+form_details.getMail_type_reg_mail()+","+form_details.getMail_type_forgotpass()+","+form_details.getMail_type_alert()+","+form_details.getMail_type_other()+","+form_details.getOther_mail_type());
            pst.setString(55, form_details.getHardware_uploaded_filename());
            pst.setString(56, form_details.getHardware_renamed_filepath());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "RELAY dao tab 2: " + pst);
            int i = pst.executeUpdate();

        } catch (Exception e) {
            errflag = true;
            newref = "";
            // e.printStackTrace();
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in RELAY dao tab 2 : " + e.getMessage());
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
                String search_regnum = "select registration_no from relay_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");

                }
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(18, dbrefno.length());

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
                newref = "RELAY-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in RELAY dao tab 2: " + newref);

                String sql = "insert into relay_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,division_name,app_name,os,"
                        + "app_ip,staging_ip,server_loc,server_loc_other,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,point_mobile_number,point_email,point_name,landline_number,security_audit,security_exp_date,other_mail_type,point_contact) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getDivision());
                pst.setString(23, form_details.getRelay_app_name());
                pst.setString(24, form_details.getOs());
                pst.setString(25, relay_ip);
                pst.setString(26, form_details.getIp_staging());
                pst.setString(27, form_details.getServer_loc());
                pst.setString(28, form_details.getServer_loc_txt());
                pst.setString(29, form_details.getUploaded_filename());
                pst.setString(30, form_details.getRenamed_filepath());
                pst.setString(31, newref);
                pst.setString(32, ip);
                pst.setString(33, form_details.getCa_design().trim());
                pst.setString(34, form_details.getMobile_number());
                pst.setString(35, form_details.getPoint_email());
                pst.setString(36, form_details.getPoint_name());
                pst.setString(37, form_details.getLandline_number());
                pst.setString(38, form_details.getSecurity_audit());
                pst.setString(39, form_details.getSecurity_exp_date());
                pst.setString(40, form_details.getOther_mail_type());
                pst.setString(41, form_details.getPoint_contact());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "RELAY dao tab 2: " + pst);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                //errflag=true;
                newref = "";
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in RELAY dao tab 2 : " + e.getMessage());
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

    public void insertTelnet(String telnet, String ref_num) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qryStr = "Update relay_registration set telnet_response = ? where registration_no = ?";
            pst = con.prepareStatement(qryStr);
            pst.setString(1, telnet);
            pst.setString(2, ref_num);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "RELAY dao tab 3 : " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in RELAY dao tab 3 : " + e.getMessage());
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
            String qryStr = "Update relay_registration set req_id = ? where registration_no = ?";
            pst = con.prepareStatement(qryStr);
            pst.setString(1, firewall_id);
            pst.setString(2, ref_num);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "RELAY dao tab 4: " + pst);
            pst.executeUpdate();

        } catch (Exception e) {
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in RELAY dao tab 4 : " + e.getMessage());
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
