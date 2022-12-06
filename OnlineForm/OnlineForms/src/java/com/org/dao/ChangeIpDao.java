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
 * @author Nancy
 */
public class ChangeIpDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public void ip_tab1(FormBean form_details, String ip_type) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {

            con = DbConnection.getConnection();
            String sql = "insert into onlineform_temp_db.ip_registration_temp(ip_action_request,account_name,ip1,ip2,ip3,ip4,userip,ip_change_request,app_name,app_ip,server_loc,server_loc_other)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getReq_for());
            pst.setString(2, form_details.getAccount_name());
            if (!form_details.getAdd_ip1().equals("")) {
                pst.setString(3, form_details.getAdd_ip1());
            } else {
                pst.setString(3, "");
            }
            if (!form_details.getAdd_ip2().equals("")) {

                pst.setString(4, form_details.getAdd_ip2());
            } else {
                pst.setString(4, "");
            }
            if (!form_details.getAdd_ip3().equals("")) {

                pst.setString(5, form_details.getAdd_ip3());
            } else {
                pst.setString(5, "");
            }
            if (!form_details.getAdd_ip4().equals("")) {

                pst.setString(6, form_details.getAdd_ip4());
            } else {
                pst.setString(6, "");
            }

            pst.setString(7, ip);
            pst.setString(8, ip_type);
            pst.setString(9, form_details.getRelay_app());
            pst.setString(10, form_details.getRelay_old_ip());
            pst.setString(11, form_details.getServer_loc());
            pst.setString(12, form_details.getServer_loc_txt());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IP dao tab 1: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in IP dao tab 1: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    public void ip_tab2(FormBean form_details, String ip_type) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String sql = "insert into onlineform_temp_db.ip_registration_temp(ip_action_request,account_name,ip1,ip2,ip3,ip4,userip,ip_change_request,app_name,app_ip,server_loc,server_loc_other)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getReq_for());
            pst.setString(2, form_details.getAccount_name());

            if (form_details.getOld_ip1().equals("") || form_details.getOld_ip1() == null) {
                pst.setString(3, "");
            } else {
                pst.setString(3, form_details.getOld_ip1() + ";" + form_details.getNew_ip1());
            }
            if (form_details.getOld_ip2().equals("") || form_details.getOld_ip2() == null) {
                pst.setString(4, "");
            } else {
                pst.setString(4, form_details.getOld_ip2() + ";" + form_details.getNew_ip2());
            }
            if (form_details.getOld_ip3().equals("") || form_details.getOld_ip3() == null) {
                pst.setString(5, "");
            } else {
                pst.setString(5, form_details.getOld_ip3() + ";" + form_details.getNew_ip3());
            }
            if (form_details.getOld_ip4().equals("") || form_details.getOld_ip4() == null) {
                pst.setString(6, "");
            } else {
                pst.setString(6, form_details.getOld_ip4() + ";" + form_details.getNew_ip4());
            }
            pst.setString(7, ip);
            pst.setString(8, ip_type);
            pst.setString(9, form_details.getRelay_app());
            pst.setString(10, form_details.getRelay_old_ip());
            pst.setString(11, form_details.getServer_loc());
            pst.setString(12, form_details.getServer_loc_txt());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IP dao tab 2: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in IP dao tab 2: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
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

    public String ip_tab3(FormBean form_details, Map profile_values) {

        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Boolean errflag = false;
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from ip_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query for ip dao tab3 get ref num ::: " + pst);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                String lastst = dbrefno.substring(15, dbrefno.length());
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
            newref = "IP-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "NEW REF NUMBER for ip dao in tab 3: " + newref);

            String sql = "insert into ip_registration(auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,ip_change_request,ip_action_request,account_name,ip1,ip2,ip3,ip4,app_name,app_ip,"
                    + "server_loc,server_loc_other,userip,registration_no,ca_desig,ldap_url,ldap_auth_allocate)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getIp_type());
            pst.setString(23, form_details.getReq_for());
            if (form_details.getReq_for().equals("sms")) {
                pst.setString(24, form_details.getAccount_name());
            } else if (form_details.getReq_for().equals("ldap")) {
                pst.setString(24, form_details.getLdap_account_name());
            } else {
                pst.setString(24, "");
            }

            if (form_details.getIp_type().equals("addip")) {
                pst.setString(25, form_details.getAdd_ip1());
                pst.setString(26, form_details.getAdd_ip2());
                pst.setString(27, form_details.getAdd_ip3());
                pst.setString(28, form_details.getAdd_ip4());

            } else {
                if (form_details.getOld_ip1().equals("") || form_details.getOld_ip1() == null) {
                    pst.setString(25, "");
                } else {
                    pst.setString(25, form_details.getOld_ip1() + ";" + form_details.getNew_ip1());
                }
                if (form_details.getOld_ip2().equals("") || form_details.getOld_ip2() == null) {
                    pst.setString(26, "");
                } else {
                    pst.setString(26, form_details.getOld_ip2() + ";" + form_details.getNew_ip2());
                }
                if (form_details.getOld_ip3().equals("") || form_details.getOld_ip3() == null) {
                    pst.setString(27, "");
                } else {
                    pst.setString(27, form_details.getOld_ip3() + ";" + form_details.getNew_ip3());
                }
                if (form_details.getOld_ip4().equals("") || form_details.getOld_ip4() == null) {
                    pst.setString(28, "");
                } else {
                    pst.setString(28, form_details.getOld_ip4() + ";" + form_details.getNew_ip4());
                }
            }
            if (form_details.getReq_for().equals("relay")) {
                pst.setString(29, form_details.getRelay_app());
                pst.setString(30, form_details.getRelay_old_ip());
                pst.setString(31, form_details.getServer_loc());
                pst.setString(32, form_details.getServer_loc_txt());
            } else {
                pst.setString(29, "");
                pst.setString(30, "");
                pst.setString(31, "");
                pst.setString(32, "");

            }
            pst.setString(33, ip);
            pst.setString(34, newref);
            pst.setString(35, form_details.getCa_design().trim());
            pst.setString(36, form_details.getLdap_url());
            pst.setString(37, form_details.getLdap_auth_allocate());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query for ip dao tab3 :::" + pst);

            int i = pst.executeUpdate();
            // 20th march
        } catch (Exception e) {
            errflag = true;
            newref = "";

            //e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for ip dao tab3: " + e.getMessage());
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

            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (errflag) {
            try {
                conSlave = DbConnection.getSlaveConnection();
                con = DbConnection.getConnection();
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from ip_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query for ip dao tab3 get ref num ::: " + pst);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(15, dbrefno.length());
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
                newref = "IP-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "NEW REF NUMBER for ip dao in tab 3: " + newref);

                String sql = "insert into ip_registration(auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,ip_change_request,ip_action_request,account_name,ip1,ip2,ip3,ip4,app_name,app_ip,"
                        + "server_loc,server_loc_other,userip,registration_no,ca_desig,ldap_url,ldap_auth_allocate)"
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

                pst.setString(22, form_details.getIp_type());
                pst.setString(23, form_details.getReq_for());
                if (form_details.getReq_for().equals("sms")) {
                    pst.setString(24, form_details.getAccount_name());
                } else if (form_details.getReq_for().equals("ldap")) {
                    pst.setString(24, form_details.getLdap_account_name());
                } else {
                    pst.setString(24, "");
                }

                if (form_details.getIp_type().equals("addip")) {
                    pst.setString(25, form_details.getAdd_ip1());
                    pst.setString(26, form_details.getAdd_ip2());
                    pst.setString(27, form_details.getAdd_ip3());
                    pst.setString(28, form_details.getAdd_ip4());

                } else {
                    if (form_details.getOld_ip1().equals("") || form_details.getOld_ip1() == null) {
                        pst.setString(25, "");
                    } else {
                        pst.setString(25, form_details.getOld_ip1() + ";" + form_details.getNew_ip1());
                    }
                    if (form_details.getOld_ip2().equals("") || form_details.getOld_ip2() == null) {
                        pst.setString(26, "");
                    } else {
                        pst.setString(26, form_details.getOld_ip2() + ";" + form_details.getNew_ip2());
                    }
                    if (form_details.getOld_ip3().equals("") || form_details.getOld_ip3() == null) {
                        pst.setString(27, "");
                    } else {
                        pst.setString(27, form_details.getOld_ip3() + ";" + form_details.getNew_ip3());
                    }
                    if (form_details.getOld_ip4().equals("") || form_details.getOld_ip4() == null) {
                        pst.setString(28, "");
                    } else {
                        pst.setString(28, form_details.getOld_ip4() + ";" + form_details.getNew_ip4());
                    }
                }
                if (form_details.getReq_for().equals("relay")) {
                    pst.setString(29, form_details.getRelay_app());
                    pst.setString(30, form_details.getRelay_old_ip());
                    pst.setString(31, form_details.getServer_loc());
                    pst.setString(32, form_details.getServer_loc_txt());
                } else {
                    pst.setString(29, "");
                    pst.setString(30, "");
                    pst.setString(31, "");
                    pst.setString(32, "");

                }
                pst.setString(33, ip);
                pst.setString(34, newref);
                pst.setString(35, form_details.getCa_design().trim());
                pst.setString(36, form_details.getLdap_url());
                pst.setString(37, form_details.getLdap_auth_allocate());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query for ip dao tab3 :::" + pst);

                int i = pst.executeUpdate();
                // 20th march
            } catch (Exception e) {
                //errflag = true;
                newref = "";

                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception for ip dao tab3: " + e.getMessage());
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

                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return newref;
    }

}
