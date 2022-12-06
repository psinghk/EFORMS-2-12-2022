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

public class LdapDao {

    private //Connection con = null;
            String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public void Ldap_tab1(FormBean form_details) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "insert into onlineform_temp_db.ldap_registration_tmp (app_name,app_url,domain,base_ip,service_ip"
                    + ",server_loc,server_loc_other,https,audit,ldap_id1,ldap_id2,uploaded_filename,renamed_filepath,ip)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getApp_name());
            pst.setString(2, form_details.getApp_url());
            pst.setString(3, form_details.getDomain());
            pst.setString(4, form_details.getBase_ip());
            pst.setString(5, form_details.getService_ip());
            pst.setString(6, form_details.getServer_loc());
            pst.setString(7, form_details.getServer_loc_txt());
            pst.setString(8, form_details.getHttps());
            pst.setString(9, form_details.getAudit());
            pst.setString(10, form_details.getLdap_id1());
            pst.setString(11, form_details.getLdap_id2());
            pst.setString(12, form_details.getUploaded_filename());
            pst.setString(13, form_details.getRenamed_filepath());
            pst.setString(14, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "LDAP dao tab 1: " + pst);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in LDAP dao tab 1: " + e.getMessage());
            e.printStackTrace();
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

    public String Ldap_tab2(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Boolean errflag = false;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from ldap_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                String lastst = dbrefno.substring(17, dbrefno.length());
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
            newref = "LDAP-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in LDAP dao tab 2: " + newref);

            String sql = "insert into ldap_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,app_name,app_url,domain,base_ip,service_ip"
                    + ",server_loc,server_loc_other,https,audit,ldap_id1,ldap_id2,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig) "
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
            pst.setString(22, form_details.getApp_name());
            pst.setString(23, form_details.getApp_url());
            pst.setString(24, form_details.getDomain());
            pst.setString(25, form_details.getBase_ip());
            pst.setString(26, form_details.getService_ip());
            pst.setString(27, form_details.getServer_loc());
            pst.setString(28, form_details.getServer_loc_txt());
            pst.setString(29, form_details.getHttps());
            pst.setString(30, form_details.getAudit());
            pst.setString(31, form_details.getLdap_id1());
            pst.setString(32, form_details.getLdap_id2());
            pst.setString(33, form_details.getUploaded_filename());
            pst.setString(34, form_details.getRenamed_filepath());
            pst.setString(35, newref);
            pst.setString(36, ip);
            pst.setString(37, form_details.getCa_design().trim());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "LDAP dao tab 2: " + pst);
            int i = pst.executeUpdate();
        } catch (Exception e) {
            errflag = true;
            newref = "";
            //e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in LDAP dao tab 2: " + e.getMessage());
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
                String search_regnum = "select registration_no from ldap_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(17, dbrefno.length());
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
                newref = "LDAP-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in LDAP dao tab 2: " + newref);

                String sql = "insert into ldap_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,app_name,app_url,domain,base_ip,service_ip"
                        + ",server_loc,server_loc_other,https,audit,ldap_id1,ldap_id2,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig) "
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
                pst.setString(22, form_details.getApp_name());
                pst.setString(23, form_details.getApp_url());
                pst.setString(24, form_details.getDomain());
                pst.setString(25, form_details.getBase_ip());
                pst.setString(26, form_details.getService_ip());
                pst.setString(27, form_details.getServer_loc());
                pst.setString(28, form_details.getServer_loc_txt());
                pst.setString(29, form_details.getHttps());
                pst.setString(30, form_details.getAudit());
                pst.setString(31, form_details.getLdap_id1());
                pst.setString(32, form_details.getLdap_id2());
                pst.setString(33, form_details.getUploaded_filename());
                pst.setString(34, form_details.getRenamed_filepath());
                pst.setString(35, newref);
                pst.setString(36, ip);
                pst.setString(37, form_details.getCa_design().trim());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "LDAP dao tab 2: " + pst);
                int i = pst.executeUpdate();
            } catch (Exception e) {
                // errflag=true;
                newref = "";
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in LDAP dao tab 2: " + e.getMessage());
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
        try {
            con = DbConnection.getConnection();
            String qryStr = "Update ldap_registration set telnet_response = ? where registration_no = ?";
            pst = con.prepareStatement(qryStr);
            pst.setString(1, telnet);
            pst.setString(2, ref_num);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "LDAP dao tab 3: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in LDAP dao tab 3: " + e.getMessage());
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
        try {
            con = DbConnection.getConnection();
            String qryStr = "Update ldap_registration set req_id = ? where registration_no = ?";
            pst = con.prepareStatement(qryStr);
            pst.setString(1, firewall_id);
            pst.setString(2, ref_num);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "LDAP dao tab 4: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.getMessage();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in LDAP dao tab 4: " + e.getMessage());
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
