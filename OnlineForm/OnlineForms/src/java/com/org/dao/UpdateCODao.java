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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author nikki
 */
public class UpdateCODao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public List updateCO_getdata(String formname) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List list = new ArrayList();
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (formname.equalsIgnoreCase("email")) {
                pst = conSlave.prepareStatement("select * from employment_coordinator");
            } else if (formname.equalsIgnoreCase("dlist")) {
                pst = conSlave.prepareStatement("select * from distributionlist_coordinator");
            }
            else if (formname.equalsIgnoreCase("bulkdlist")) {
                pst = conSlave.prepareStatement("select * from distributionlist_coordinator");
            }
            else if (formname.equalsIgnoreCase("dns")) {
                pst = conSlave.prepareStatement("select * from dns_coordinator");
            } else if (formname.equalsIgnoreCase("imappop")) {
                pst = conSlave.prepareStatement("select * from imappop_coordinator");
            } else if (formname.equalsIgnoreCase("ip")) {
                pst = conSlave.prepareStatement("select * from ip_coordinator");
            } else if (formname.equalsIgnoreCase("ldap")) {
                pst = conSlave.prepareStatement("select * from ldap_coordinator");
            } else if (formname.equalsIgnoreCase("mobile")) {
                pst = conSlave.prepareStatement("select * from mobile_coordinator");
            } else if (formname.equalsIgnoreCase("relay")) {
                pst = conSlave.prepareStatement("select * from relay_coordinator");
            } else if (formname.equalsIgnoreCase("sms")) {
                pst = conSlave.prepareStatement("select * from sms_coordinator");
            } else if (formname.equalsIgnoreCase("vpn")) {
                pst = conSlave.prepareStatement("select * from vpn_coordinator where emp_status='a'");
            } else if (formname.equalsIgnoreCase("webcast")) {
                pst = conSlave.prepareStatement("select * from webcast_coordinator");
            } else if (formname.equalsIgnoreCase("wifi")) {
                pst = conSlave.prepareStatement("select * from wifi_coordinator");
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                model.UpdateCO obj = new model.UpdateCO();
                obj.setEmpcat(rs.getString("emp_category"));
                obj.setEmpmin(rs.getString("emp_min_state_org"));
                obj.setEmpdep(rs.getString("emp_dept"));
                if (formname.equalsIgnoreCase("email")) {
                    obj.setDom(rs.getString("emp_domain"));
                }
                obj.setCname(rs.getString("emp_coord_name"));
                obj.setCemail(rs.getString("emp_coord_email"));
                obj.setCmobile(rs.getString("emp_coord_mobile"));
                obj.setAemail(rs.getString("emp_admin_email"));
                obj.setIp(rs.getString("ip"));
                obj.setUpdateButton("<span class='btn btn-warning btn-sm updateEmp' id='" + rs.getInt("emp_id") + "' >Update</span>");
                if (formname.equalsIgnoreCase("vpn")) {
                    obj.setDeleteButton("<span class='btn btn-danger btn-sm deleteEmp' id='" + rs.getInt("emp_id") + "' >Delete</span>");
                }
                list.add(obj);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertCO_details(FormBean form_details, String formname, String email) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            ResultSet rs = null;
            int generatedKey = 0;
            String tablename = "";
            if (formname.equalsIgnoreCase("email")) {
                pst = con.prepareStatement("insert into employment_coordinator(emp_category,emp_min_state_org,emp_dept,emp_sub_dept,emp_domain,emp_coord_email,emp_coord_name,"
                        + "emp_coord_mobile,emp_admin_email,emp_addedby,ip) values(?,?,?,?,?,?,?,?,?,?,?) ", pst.RETURN_GENERATED_KEYS);
                pst.setString(1, form_details.getUser_employment());
                pst.setString(2, form_details.getMin());
                pst.setString(3, form_details.getDept());
                pst.setString(4, form_details.getOther_dept());
                pst.setString(5, form_details.getDomain());
                pst.setString(6, form_details.getCa_email());
                pst.setString(7, form_details.getCa_name());
                pst.setString(8, form_details.getCa_mobile());
                pst.setString(9, form_details.getHod_email());
                pst.setString(10, email);
                pst.setString(11, form_details.getBase_ip());
                System.out.println("COORDINATOR UPDATE :: PS: " + pst);
                tablename = "employment_coordinator";
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedKey = rs.getInt(1);
                }
            } else {
                pst = con.prepareStatement("insert into " + formname + "_coordinator(emp_category,emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_email,emp_coord_name,"
                        + "emp_coord_mobile,emp_admin_email,emp_addedby,ip) values(?,?,?,?,?,?,?,?,?,?) ", pst.RETURN_GENERATED_KEYS);
                pst.setString(1, form_details.getUser_employment());
                pst.setString(2, form_details.getMin());
                pst.setString(3, form_details.getDept());
                pst.setString(4, form_details.getOther_dept());
                pst.setString(5, form_details.getCa_email());
                pst.setString(6, form_details.getCa_name());
                pst.setString(7, form_details.getCa_mobile());
                pst.setString(8, form_details.getHod_email());
                pst.setString(9, email);
                pst.setString(10, form_details.getBase_ip());
                System.out.println("COORDINATOR UPDATE :: PS: " + pst);
                tablename = formname + "_coordinator";
                pst.executeUpdate();
                rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    generatedKey = rs.getInt(1);
                }
            }

            pst = con.prepareStatement("insert into update_coordinator_details(emp_id,emp_category,emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_email,emp_updatedby,"
                    + "emp_updatedon,emp_action,emp_table,ip) values(?,?,?,?,?,?,?,now(),?,?,?) ");
            pst.setInt(1, generatedKey);
            pst.setString(2, form_details.getUser_employment());
            pst.setString(3, form_details.getMin());
            pst.setString(4, form_details.getDept());
            pst.setString(5, form_details.getOther_dept());
            pst.setString(6, form_details.getCa_email());
            pst.setString(7, email);
            pst.setString(8, "insert");
            pst.setString(9, tablename);
            pst.setString(10, ip);
            System.out.println("COORDINATOR UPDATE :: PS: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String check_otherdept(String data, String test, String add_data, String dept) { // data - category , test - ministry, add_data - formname
        String returnString = "";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (add_data.equalsIgnoreCase("email")) {
                pst = conSlave.prepareStatement("select emp_category,emp_min_state_org from employment_coordinator where emp_category=? and emp_min_state_org=? and emp_dept=?");
                pst.setString(1, data);
                pst.setString(2, test);
                pst.setString(3, dept);
                System.out.println("COORDINATOR UPDATE :: PS:: " + pst);
                rs = pst.executeQuery();
                if (rs.next()) {
                    returnString = "value found";
                } else {
                    returnString = "NA";
                }
            } else {
                pst = conSlave.prepareStatement("select emp_category,emp_min_state_org from " + add_data + "_coordinator where emp_category=? and emp_min_state_org=? and emp_dept=?");
                pst.setString(1, data);
                pst.setString(2, test);
                pst.setString(3, dept);
                System.out.println("COORDINATOR UPDATE :: PS:: " + pst);
                rs = pst.executeQuery();
                if (rs.next()) {
                    returnString = "value found";
                } else {
                    returnString = "NA";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public Map updateCO_getCOdata(String data, String add_data) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Map coordinator_data = new HashMap();
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (add_data.equalsIgnoreCase("email")) {
                pst = conSlave.prepareStatement("select * from employment_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("dlist")) {
                pst = conSlave.prepareStatement("select * from distributionlist_coordinator where emp_id=?");
            } 
            else if (add_data.equalsIgnoreCase("bulkdlist")) {
                pst = conSlave.prepareStatement("select * from distributionlist_coordinator where emp_id=?");
            }
            else if (add_data.equalsIgnoreCase("dns")) {
                pst = conSlave.prepareStatement("select * from dns_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("imappop")) {
                pst = conSlave.prepareStatement("select * from imappop_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("ip")) {
                pst = conSlave.prepareStatement("select * from ip_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("ldap")) {
                pst = conSlave.prepareStatement("select * from ldap_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("mobile")) {
                pst = conSlave.prepareStatement("select * from mobile_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("relay")) {
                pst = conSlave.prepareStatement("select * from relay_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("sms")) {
                pst = conSlave.prepareStatement("select * from sms_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("vpn")) {
                pst = conSlave.prepareStatement("select * from vpn_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("webcast")) {
                pst = conSlave.prepareStatement("select * from webcast_coordinator where emp_id=?");
            } else if (add_data.equalsIgnoreCase("wifi")) {
                pst = conSlave.prepareStatement("select * from wifi_coordinator where emp_id=?");
            }
            pst.setString(1, data);
            System.out.println("COORDINATOR UPDATE :: PS:: " + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                coordinator_data.put("user_employment", rs.getString("emp_category"));
                coordinator_data.put("min", rs.getString("emp_min_state_org"));
                coordinator_data.put("dept", rs.getString("emp_dept"));
                coordinator_data.put("other_dept", rs.getString("emp_sub_dept"));
                if (add_data.equalsIgnoreCase("email")) {
                    coordinator_data.put("domain", rs.getString("emp_domain"));
                }
                coordinator_data.put("ca_email", rs.getString("emp_coord_email"));
                coordinator_data.put("ca_name", rs.getString("emp_coord_name"));
                coordinator_data.put("ca_mobile", rs.getString("emp_coord_mobile"));
                coordinator_data.put("hod_email", rs.getString("emp_admin_email"));
                coordinator_data.put("base_ip", rs.getString("ip"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinator_data;
    }

    public void updateCO_details(FormBean form_details, String formname, String email, int emp_id) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String old_ca_email = "";
            ResultSet rs = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
            int i = 0;
            if (formname.equalsIgnoreCase("email")) {
                pst = conSlave.prepareStatement("select * from employment_coordinator where emp_id=?");
                pst.setInt(1, emp_id);
                System.out.println("COORDINATOR UPDATE :: PS1: " + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
                    old_ca_email = rs.getString("emp_coord_email");
                }
                rs.close();

                pst = con.prepareStatement("update employment_coordinator set emp_coord_email = ?,emp_coord_name = ?,emp_coord_mobile = ?,emp_domain=?,emp_admin_email=?,"
                        + "ip=? where emp_id = ?");
                pst.setString(1, form_details.getCa_email());
                pst.setString(2, form_details.getCa_name());
                pst.setString(3, form_details.getCa_mobile());
                pst.setString(4, form_details.getDomain());
                pst.setString(5, form_details.getHod_email());
                pst.setString(6, form_details.getBase_ip());
                pst.setInt(7, emp_id);
                System.out.println("COORDINATOR UPDATE :: PS2: " + pst);
                i = pst.executeUpdate();

                PreparedStatement ps1_update_logs = con.prepareStatement("insert into employment_coordinator_before_update (action_datetime,ip,login_email,emp_id,emp_category,"
                        + "emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_name,emp_coord_email,emp_admin_email,emp_status,emp_createdon) select now(),ip,?,emp_id,emp_category,"
                        + "emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_name,emp_coord_email,emp_admin_email,emp_status,emp_createdon from employment_coordinator where emp_id=?");
                ps1_update_logs.setString(1, email);
                ps1_update_logs.setInt(2, emp_id);
                System.out.println("COORDINATOR UPDATE :: PSS:: " + ps1_update_logs);
                ps1_update_logs.executeUpdate();
            } else {
                pst = conSlave.prepareStatement("select * from " + formname + "_coordinator where emp_id=?");
                pst.setInt(1, emp_id);
                System.out.println("COORDINATOR UPDATE :: PS1: " + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
                    old_ca_email = rs.getString("emp_coord_email");
                }
                rs.close();
                if (formname.equalsIgnoreCase("vpn")) {
                    pst = con.prepareStatement("update " + formname + "_coordinator set emp_coord_email = ?,emp_coord_name = ?,emp_coord_mobile = ?,emp_admin_email=?,"
                            + "ip=?,emp_min_state_org=?,emp_dept=?,emp_sub_dept=? where emp_id = ?");
                    pst.setString(1, form_details.getCa_email());
                    pst.setString(2, form_details.getCa_name());
                    pst.setString(3, form_details.getCa_mobile());
                    pst.setString(4, form_details.getHod_email());
                    pst.setString(5, form_details.getBase_ip());
                    pst.setString(6, form_details.getMin());
                    pst.setString(7, form_details.getDept());
                    pst.setString(8, form_details.getOther_dept());
                    pst.setInt(9, emp_id);
                    System.out.println("COORDINATOR UPDATE :: PS2: " + pst);
                    i = pst.executeUpdate();
                } else {
                    pst = con.prepareStatement("update " + formname + "_coordinator set emp_coord_email = ?,emp_coord_name = ?,emp_coord_mobile = ?,emp_admin_email=?,"
                            + "ip=? where emp_id = ?");
                    pst.setString(1, form_details.getCa_email());
                    pst.setString(2, form_details.getCa_name());
                    pst.setString(3, form_details.getCa_mobile());
                    pst.setString(4, form_details.getHod_email());
                    pst.setString(5, form_details.getBase_ip());
                    pst.setInt(6, emp_id);
                    System.out.println("COORDINATOR UPDATE :: PS2: " + pst);
                    i = pst.executeUpdate();
                }

                PreparedStatement ps1_update_logs = con.prepareStatement("insert into employment_coordinator_before_update (action_datetime,ip,login_email,emp_id,emp_category,"
                        + "emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_name,emp_coord_email,emp_admin_email,emp_status,emp_createdon) select now(),ip,?,emp_id,emp_category,"
                        + "emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_name,emp_coord_email,emp_admin_email,emp_status,emp_createdon from " + formname + "_coordinator where emp_id=?");
                ps1_update_logs.setString(1, email);
                ps1_update_logs.setInt(2, emp_id);
                System.out.println("COORDINATOR UPDATE :: PSS:: " + ps1_update_logs);
                ps1_update_logs.executeUpdate();
            }

            if (i > 0) {

                String employment = "";
                String ministry = "";
                String department = "";
                String state = "";
                String organization = "";
                if (formname.equalsIgnoreCase("email")) {
                    pst = conSlave.prepareStatement("select stat_form_type,stat_reg_no,stat_id from status where (stat_type='coordinator_pending' or (stat_type='da_pending')) and stat_forwarded_to_user = ? "
                            + "and stat_id in (select max(stat_id) from status group by stat_reg_no) and (stat_form_type='single' or stat_form_type='bulk' or stat_form_type='gem' or "
                            + "stat_form_type='nkn_single' or stat_form_type='nkn_bulk') order by stat_reg_no");
                } else if (formname.equalsIgnoreCase("vpn")) {
                    pst = conSlave.prepareStatement("select stat_form_type,stat_reg_no,stat_id from status where (stat_type='coordinator_pending' or (stat_type='da_pending')) and stat_forwarded_to_user = ? "
                            + "and stat_id in (select max(stat_id) from status group by stat_reg_no) and (stat_form_type='vpn_single' or stat_form_type='vpn_renew' or stat_form_type='change_add') order by stat_reg_no");
                } else {
                    pst = conSlave.prepareStatement("select stat_form_type,stat_reg_no,stat_id from status where (stat_type='coordinator_pending' or (stat_type='da_pending')) and stat_forwarded_to_user = ? "
                            + "and stat_id in (select max(stat_id) from status group by stat_reg_no) and stat_form_type='" + formname + "' order by stat_reg_no");
                }

                pst.setString(1, old_ca_email);
                System.out.println("COORDINATOR UPDATE :: PS3: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    int stat_id = rs1.getInt("stat_id");
                    String registration_no = rs1.getString("stat_reg_no");
                    String tablename = myTable(registration_no);
                    String form_type = rs1.getString("stat_form_type");
                    PreparedStatement psta = conSlave.prepareStatement("select employment,ministry,department,state,organization from " + tablename + " where registration_no=?");
                    psta.setString(1, registration_no);
                    System.out.println("COORDINATOR UPDATE :: PSTA after PS3: " + psta);
                    ResultSet rs3 = psta.executeQuery();
                    while (rs3.next()) {
                        employment = rs3.getString("employment");
                        ministry = rs3.getString("ministry");
                        department = rs3.getString("department");
                        state = rs3.getString("state");
                        organization = rs3.getString("organization");
                    }
                    rs3.close();
                    boolean update = false;
                    if (formname.equals("email")) {
                        if (form_details.getUser_employment().equalsIgnoreCase("Central")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("State")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("Others") || form_details.getUser_employment().equalsIgnoreCase("Psu") || form_details.getUser_employment().equalsIgnoreCase("Const")
                                || form_details.getUser_employment().equalsIgnoreCase("Nkn") || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(organization)) {
                                update = true;
                            }
                        }
                    } else if (formname.equals("vpn")) {

                        if (form_details.getUser_employment().equalsIgnoreCase("Central")) {
                            if (form_details.getDept().equals("") || form_details.getDept() == null) {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry)) {
                                    update = true;
                                }
                            } else {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry) && form_details.getDept().equalsIgnoreCase(department)) {
                                    update = true;
                                }
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("State")) {
                            if (form_details.getDept().equals("") || form_details.getDept() == null) {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state)) {
                                    update = true;
                                }
                            } else {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state) && form_details.getDept().equalsIgnoreCase(department)) {
                                    update = true;
                                }
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("Others") || form_details.getUser_employment().equalsIgnoreCase("Psu") || form_details.getUser_employment().equalsIgnoreCase("Const")
                                || form_details.getUser_employment().equalsIgnoreCase("Nkn") || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(organization)) {
                                update = true;
                            }
                        }
                    } else {
                        if (form_details.getUser_employment().equalsIgnoreCase("Central")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("State")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("Others") || form_details.getUser_employment().equalsIgnoreCase("Psu") || form_details.getUser_employment().equalsIgnoreCase("Const")
                                || form_details.getUser_employment().equalsIgnoreCase("Nkn") || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(organization)) {
                                update = true;
                            }
                        }
                    }
                    System.out.println("UPDATE: " + update);
                    if (update) {
                        if (form_details.getCa_email().equalsIgnoreCase(form_details.getHod_email())) {
                            PreparedStatement pst1 = con.prepareStatement("update status set stat_forwarded_to_user = ?,stat_type=?,stat_forwarded_to=? where stat_id = ?");
                            pst1.setString(1, form_details.getCa_email());
                            pst1.setString(2, "da_pending");
                            pst1.setString(3, "d");
                            pst1.setInt(4, stat_id);
                            System.out.println("COORDINATOR UPDATE :: PS4: " + pst1);
                            pst1.executeUpdate();
                        } else {
                            PreparedStatement pst1 = con.prepareStatement("update status set stat_forwarded_to_user = ?,stat_type=?,stat_forwarded_to=? where stat_id = ?");
                            pst1.setString(1, form_details.getCa_email());
                            pst1.setString(2, "coordinator_pending");
                            pst1.setString(3, "c");
                            pst1.setInt(4, stat_id);
                            System.out.println("COORDINATOR UPDATE :: PS4: " + pst1);
                            pst1.executeUpdate();
                        }

                        if (formname.equals("vpn")) { // update base tale also for vpn_coordinator column
                            PreparedStatement pst1 = con.prepareStatement("update vpn_registration set coordinator_email = ? where registration_no = ?");
                            pst1.setString(1, form_details.getCa_email());
                            pst1.setString(2, registration_no);
                            System.out.println("COORDINATOR UPDATE :: PS7 : " + pst);
                            pst1.executeUpdate();
                        }
                    } else {
                        if (formname.equals("vpn")) {
                            PreparedStatement pst1 = con.prepareStatement("INSERT INTO status SET stat_type = 'support_pending', stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = 'c', stat_forwarded_by_user = ?"
                                    + ", stat_forwarded_to = 's',stat_forwarded_to_user = 'vpnsupport@nic.in' , stat_remarks = ?, stat_process='reverted_c_s'");
                            pst1.setString(1, form_type);
                            pst1.setString(2, registration_no.toUpperCase());
                            pst1.setString(3, old_ca_email);
                            pst1.setString(4, "min/dept changed, request moved to support");
                            System.out.println("COORDINATOR UPDATE :: PS4: " + pst1);
                            pst1.executeUpdate();
                        }
                    }
                }
                rs1.close();

                if (formname.equalsIgnoreCase("email")) {
                    pst = conSlave.prepareStatement("select track_id,registration_no from final_audit_track where (status='coordinator_pending' or status='da_pending') and to_email=?"
                            + " and (form_name='single' or form_name='bulk' or form_name='gem' or form_name='nkn_single' or form_name='nkn_bulk')");
                } else if (formname.equalsIgnoreCase("vpn")) {
                    pst = conSlave.prepareStatement("select track_id,registration_no from final_audit_track where (status='coordinator_pending' or status='da_pending') and to_email=?"
                            + " and (form_name='vpn_single' or form_name='vpn_renew' or form_name='change_add')");
                } else {
                    pst = conSlave.prepareStatement("select track_id,registration_no from final_audit_track where (status='coordinator_pending' or status='da_pending') and to_email=?"
                            + " and form_name='" + formname + "'");
                }
                pst.setString(1, old_ca_email);
                System.out.println("COORDINATOR UPDATE :: PS5: " + pst);
                rs2 = pst.executeQuery();
                while (rs2.next()) {
                    int track_id = rs2.getInt("track_id");
                    String registration_no = rs2.getString("registration_no");
                    String tablename = myTable(registration_no);

                    pst = conSlave.prepareStatement("select employment,ministry,department,state,organization from " + tablename + " where registration_no=?");
                    pst.setString(1, registration_no);
                    ResultSet rs5 = pst.executeQuery();
                    while (rs5.next()) {
                        employment = rs5.getString("employment");
                        ministry = rs5.getString("ministry");
                        department = rs5.getString("department");
                        state = rs5.getString("state");
                        organization = rs5.getString("organization");
                    }
                    rs5.close();
                    boolean update = false;
                    if (formname.equals("email")) {
                        if (form_details.getUser_employment().equalsIgnoreCase("Central")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("State")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("Others") || form_details.getUser_employment().equalsIgnoreCase("Psu") || form_details.getUser_employment().equalsIgnoreCase("Const")
                                || form_details.getUser_employment().equalsIgnoreCase("Nkn") || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(organization)) {
                                update = true;
                            }
                        }
                    } else if (formname.equals("vpn")) {

                        if (form_details.getUser_employment().equalsIgnoreCase("Central")) {
                            if (form_details.getDept().equals("") || form_details.getDept() == null) {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry)) {
                                    update = true;
                                }
                            } else {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry) && form_details.getDept().equalsIgnoreCase(department)) {
                                    update = true;
                                }
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("State")) {
                            if (form_details.getDept().equals("") || form_details.getDept() == null) {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state)) {
                                    update = true;
                                }
                            } else {
                                if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state) && form_details.getDept().equalsIgnoreCase(department)) {
                                    update = true;
                                }
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("Others") || form_details.getUser_employment().equalsIgnoreCase("Psu") || form_details.getUser_employment().equalsIgnoreCase("Const")
                                || form_details.getUser_employment().equalsIgnoreCase("Nkn") || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(organization)) {
                                update = true;
                            }
                        }
                    } else {
                        if (form_details.getUser_employment().equalsIgnoreCase("Central")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(ministry) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("State")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(state) && form_details.getDept().equalsIgnoreCase(department)) {
                                update = true;
                            }
                        } else if (form_details.getUser_employment().equalsIgnoreCase("Others") || form_details.getUser_employment().equalsIgnoreCase("Psu") || form_details.getUser_employment().equalsIgnoreCase("Const")
                                || form_details.getUser_employment().equalsIgnoreCase("Nkn") || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                            if (form_details.getUser_employment().equalsIgnoreCase(employment) && form_details.getMin().equalsIgnoreCase(organization)) {
                                update = true;
                            }
                        }
                    }

                    if (update) {
                        if (form_details.getCa_email().equalsIgnoreCase(form_details.getHod_email())) {
                            PreparedStatement pst1 = con.prepareStatement("update final_audit_track set to_email = ?,to_mobile=?, to_name=?,status=? where track_id = ?");
                            pst1.setString(1, form_details.getCa_email());
                            pst1.setString(2, form_details.getCa_mobile());
                            pst1.setString(3, form_details.getCa_name());
                            pst1.setString(4, "da_pending");
                            pst1.setInt(5, track_id);
                            System.out.println("COORDINATOR UPDATE :: PS6: " + pst);
                            pst1.executeUpdate();
                        } else {
                            PreparedStatement pst1 = con.prepareStatement("update final_audit_track set to_email = ?,to_mobile=?, to_name=?,status=? where track_id = ?");
                            pst1.setString(1, form_details.getCa_email());
                            pst1.setString(2, form_details.getCa_mobile());
                            pst1.setString(3, form_details.getCa_name());
                            pst1.setString(4, "coordinator_pending");
                            pst1.setInt(5, track_id);
                            System.out.println("COORDINATOR UPDATE :: PS6: " + pst);
                            pst1.executeUpdate();
                        }
                    } else {
                        if (formname.equals("vpn")) {
                            PreparedStatement pst1 = con.prepareStatement("UPDATE final_audit_track SET support_email = null, support_mobile = null, support_name = null, support_ip = null, support_datetime = null"
                                    + ",support_remarks = ?, status = 'support_pending' , to_email = ?, to_datetime = CURRENT_TIMESTAMP WHERE registration_no = ?");
                            pst1.setString(1, "min/dept changed, request moved to support");
                            pst1.setString(2, "vpnsupport@nic.in");
                            pst1.setString(3, registration_no.toUpperCase());
                            System.out.println("COORDINATOR UPDATE :: PS6: " + pst);
                            pst1.executeUpdate();
                        }
                    }
                }
                rs2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update_ministry(FormBean form_details, String email) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            pst = con.prepareStatement("update user_profile set ministry = ? where ministry=?");
            pst.setString(1, form_details.getNew_min_dept());
            pst.setString(2, form_details.getMin());
            System.out.println("COORDINATOR UPDATE :: PS1: " + pst);
            pst.executeUpdate();

            PreparedStatement ps1_userprofile_update_log = con.prepareStatement("insert into user_profile_update_ministry_log(action_datetime,ip,login_email,ministry_old,ministry_new) values(now(),?,?,?,?)");
            ps1_userprofile_update_log.setString(1, ip);
            ps1_userprofile_update_log.setString(2, email);
            ps1_userprofile_update_log.setString(3, form_details.getMin());
            ps1_userprofile_update_log.setString(4, form_details.getNew_min_dept());
            ps1_userprofile_update_log.executeUpdate();

            pst = con.prepareStatement("update employment_coordinator set emp_min_state_org = ? where emp_min_state_org=?");
            pst.setString(1, form_details.getNew_min_dept());
            pst.setString(2, form_details.getMin());
            System.out.println("COORDINATOR UPDATE :: PS2: " + pst);
            pst.executeUpdate();

            ResultSet rs = null;
            String ministry = "";

            pst = conSlave.prepareStatement("select stat_reg_no from status where stat_type in ('ca_pending','coordinator_pending','da_pending','mail-admin_pending','manual',"
                    + "'manual_upload','support_pending') and stat_id in (select max(stat_id) from status group by stat_reg_no) order by stat_reg_no");
            System.out.println("COORDINATOR UPDATE :: PS3: " + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                String registration_no = rs.getString("stat_reg_no");
                String tablename = myTable(registration_no);

                pst = conSlave.prepareStatement("select ministry from " + tablename + " where registration_no=?");
                pst.setString(1, registration_no);
                ResultSet rs1 = pst.executeQuery();
                while (rs1.next()) {
                    ministry = rs1.getString("ministry");
                }
                rs1.close();

                if (form_details.getMin().equalsIgnoreCase(ministry)) {
                    PreparedStatement pst1 = con.prepareStatement("update " + tablename + " set ministry = ? where registration_no=?");
                    pst1.setString(1, form_details.getNew_min_dept());
                    pst1.setString(2, registration_no);
                    System.out.println("COORDINATOR UPDATE :: PS4: " + pst);
                    pst1.executeUpdate();

                    PreparedStatement ps5__basetable_update_log = con.prepareStatement("insert into base_table_update_ministry_log(action_datetime,ip,login_email,ministry_old,ministry_new,registration_no) values(now(),?,?,?,?,?)");
                    ps5__basetable_update_log.setString(1, ip);
                    ps5__basetable_update_log.setString(2, email);
                    ps5__basetable_update_log.setString(3, form_details.getMin());
                    ps5__basetable_update_log.setString(4, form_details.getNew_min_dept());
                    ps5__basetable_update_log.setString(5, registration_no);
                    ps5__basetable_update_log.executeUpdate();
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("COORDINATOR UPDATE :: EE: " + e.getMessage());
        }

    }

    public void update_dept(FormBean form_details, String email) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            if (form_details.getUser_employment().equalsIgnoreCase("psu") || form_details.getUser_employment().equalsIgnoreCase("nkn")
                    || form_details.getUser_employment().equalsIgnoreCase("const") || form_details.getUser_employment().equalsIgnoreCase("Others")
                    || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                pst = conSlave.prepareStatement("select organization,other_dept from user_profile where employment =? and (organization =? or other_dept = ?)");
                pst.setString(1, form_details.getUser_employment());
                pst.setString(2, form_details.getMin());
                pst.setString(3, form_details.getNew_min_dept());
                ResultSet r = pst.executeQuery();
                while (r.next()) {
                    if (form_details.getMin().equalsIgnoreCase(r.getString("organization").trim())) {
                        PreparedStatement pst1 = con.prepareStatement("update user_profile set organization = ? where employment = ? and organization = ? ");
                        pst1.setString(1, form_details.getNew_min_dept());
                        pst1.setString(2, form_details.getUser_employment());
                        pst1.setString(3, form_details.getMin());
                        System.out.println("COORDINATOR UPDATE :: PSS:: " + pst1);
                        pst1.executeUpdate();
                        pst1.close();

                        PreparedStatement ps1_userprofile_update_log = con.prepareStatement("insert into user_profile_update_department_log(action_datetime,ip,login_email,department_old,department_new) values(now(),?,?,?,?)");
                        ps1_userprofile_update_log.setString(1, ip);
                        ps1_userprofile_update_log.setString(2, email);
                        ps1_userprofile_update_log.setString(3, form_details.getMin());
                        ps1_userprofile_update_log.setString(4, form_details.getNew_min_dept());
                        System.out.println("COORDINATOR UPDATE :: PSS:: " + ps1_userprofile_update_log);
                        ps1_userprofile_update_log.executeUpdate();
                    } else if (form_details.getMin().equalsIgnoreCase(r.getString("other_dept").trim())) {
                        System.out.println("COORDINATOR UPDATE :: CAN NOT UPDATE OTHER DEPT");
                    }
                }
                pst = con.prepareStatement("update employment_coordinator set emp_min_state_org = ? where emp_category = ? and emp_min_state_org = ? ");
                pst.setString(1, form_details.getNew_min_dept());
                pst.setString(2, form_details.getUser_employment());
                pst.setString(3, form_details.getMin());
                System.out.println("COORDINATOR UPDATE :: PS2: " + pst);
                pst.executeUpdate();
            } else {
                pst = con.prepareStatement("update user_profile set department = ? where employment = ? and ministry = ? and department = ? ");
                pst.setString(1, form_details.getNew_min_dept());
                pst.setString(2, form_details.getUser_employment());
                pst.setString(3, form_details.getMin());
                pst.setString(4, form_details.getDept());
                System.out.println("COORDINATOR UPDATE :: PS1: " + pst);
                pst.executeUpdate();

                PreparedStatement ps1_userprofile_update_log = con.prepareStatement("insert into user_profile_update_department_log(action_datetime,ip,login_email,department_old,department_new) values(now(),?,?,?,?)");
                ps1_userprofile_update_log.setString(1, ip);
                ps1_userprofile_update_log.setString(2, email);
                ps1_userprofile_update_log.setString(3, form_details.getDept());  ///
                ps1_userprofile_update_log.setString(4, form_details.getNew_min_dept());
                System.out.println("COORDINATOR UPDATE :: PSS:: " + ps1_userprofile_update_log);
                ps1_userprofile_update_log.executeUpdate();

                pst = con.prepareStatement("update employment_coordinator set emp_dept = ? where emp_category = ? and emp_min_state_org = ? and emp_dept = ?");
                pst.setString(1, form_details.getNew_min_dept());
                pst.setString(2, form_details.getUser_employment());
                pst.setString(3, form_details.getMin());
                pst.setString(4, form_details.getDept());
                System.out.println("COORDINATOR UPDATE :: PS2: " + pst);
                pst.executeUpdate();

                PreparedStatement ps1_employment_coordinator_update_log = con.prepareStatement("insert into employment_coordinator_update_department_log(action_datetime,ip,login_email,department_old,department_new) values(now(),?,?,?,?)");
                ps1_employment_coordinator_update_log.setString(1, ip);
                ps1_employment_coordinator_update_log.setString(2, email);
                ps1_employment_coordinator_update_log.setString(3, form_details.getDept());
                ps1_employment_coordinator_update_log.setString(4, form_details.getNew_min_dept());
                System.out.println("COORDINATOR UPDATE :: PSS:: " + ps1_employment_coordinator_update_log);
                ps1_employment_coordinator_update_log.executeUpdate();
            }

            ResultSet rs = null;
            String employment = "";
            String ministry = "";
            String department = "";

            pst = conSlave.prepareStatement("select stat_reg_no from status where stat_type in ('ca_pending','coordinator_pending','da_pending','mail-admin_pending',"
                    + "'manual','manual_upload','support_pending') and stat_id in (select max(stat_id) from status group by stat_reg_no) order by stat_reg_no");
            System.out.println("COORDINATOR UPDATE :: PS3: " + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                String registration_no = rs.getString("stat_reg_no");
                String tablename = myTable(registration_no);

                pst = conSlave.prepareStatement("select employment,ministry,department from " + tablename + " where registration_no=?");
                pst.setString(1, registration_no);
                ResultSet rs1 = pst.executeQuery();
                while (rs1.next()) {
                    employment = rs1.getString("employment");
                    ministry = rs1.getString("ministry");
                    department = rs1.getString("department");
                }
                rs1.close();

                if (form_details.getUser_employment().equalsIgnoreCase("psu") || form_details.getUser_employment().equalsIgnoreCase("nkn")
                        || form_details.getUser_employment().equalsIgnoreCase("const") || form_details.getUser_employment().equalsIgnoreCase("Others")
                        || form_details.getUser_employment().equalsIgnoreCase("Project")) {
                    if (form_details.getMin().equalsIgnoreCase("Other")) {
                        System.out.println("COORDINATOR UPDATE :: CAN NOT UPDATE OTHER");
                    } else {
                        if (form_details.getMin().equalsIgnoreCase(ministry)) {
                            PreparedStatement pst1 = con.prepareStatement("update " + tablename + " set organization = ? where registration_no=? and organization = ? and employment = ?");
                            pst1.setString(1, form_details.getNew_min_dept());
                            pst1.setString(2, registration_no);
                            pst1.setString(3, form_details.getMin());
                            pst1.setString(4, form_details.getUser_employment());
                            System.out.println("COORDINATOR UPDATE :: PS4: " + pst);
                            pst1.executeUpdate();

                            PreparedStatement ps5__basetable_update_log = con.prepareStatement("insert into base_table_update_department_log(action_datetime,ip,login_email,department_old,department_new,registration_no) values(now(),?,?,?,?,?)");
                            ps5__basetable_update_log.setString(1, ip);
                            ps5__basetable_update_log.setString(2, email);
                            ps5__basetable_update_log.setString(3, form_details.getMin());
                            ps5__basetable_update_log.setString(4, form_details.getNew_min_dept());
                            ps5__basetable_update_log.setString(5, registration_no);
                            System.out.println("COORDINATOR UPDATE :: PSS:: " + ps5__basetable_update_log);
                            ps5__basetable_update_log.executeUpdate();
                        }
                    }
                } else {
                    if (ministry.equalsIgnoreCase(form_details.getMin()) && employment.equalsIgnoreCase(form_details.getUser_employment()) && department.equalsIgnoreCase(form_details.getDept())) {
                        PreparedStatement pst1 = con.prepareStatement("update " + tablename + " set department = ? where registration_no=?");
                        pst1.setString(1, form_details.getNew_min_dept());
                        pst1.setString(2, registration_no);
                        System.out.println("COORDINATOR UPDATE :: PS4: " + pst);
                        pst1.executeUpdate();

                        PreparedStatement ps5__basetable_update_log = con.prepareStatement("insert into base_table_update_department_log(action_datetime,ip,login_email,department_old,department_new,registration_no) values(now(),?,?,?,?,?)");
                        ps5__basetable_update_log.setString(1, ip);
                        ps5__basetable_update_log.setString(2, email);
                        ps5__basetable_update_log.setString(3, form_details.getDept());
                        ps5__basetable_update_log.setString(4, form_details.getNew_min_dept());
                        ps5__basetable_update_log.setString(5, registration_no);
                        System.out.println("COORDINATOR UPDATE :: PSS:: " + ps5__basetable_update_log);
                        ps5__basetable_update_log.executeUpdate();
                    }
                }
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("COORDINATOR UPDATE :: EE: " + e.getMessage());
        }
    }

    public void updateCO_deleteCO(String data, String formname, String email) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String old_ca_email = "";

            pst = con.prepareStatement("update " + formname + "_coordinator set emp_status='i' where emp_id=?");
            pst.setString(1, data);
            System.out.println("COORDINATOR UPDATE :: PS: " + pst);
            int i = pst.executeUpdate();
            if (i > 0) {
                PreparedStatement pst1 = conSlave.prepareStatement("select * from " + formname + "_coordinator where emp_id=?");
                pst1.setString(1, data);
                ResultSet rs = pst1.executeQuery();
                if (rs.next()) {
                    pst = con.prepareStatement("insert into update_coordinator_details(emp_id,emp_category,emp_min_state_org,emp_dept,emp_sub_dept,emp_coord_email,emp_updatedby,"
                            + "emp_updatedon,emp_action,emp_table,ip) values(?,?,?,?,?,?,?,now(),?,?,?) ");
                    pst.setInt(1, rs.getInt("emp_id"));
                    pst.setString(2, rs.getString("emp_category"));
                    pst.setString(3, rs.getString("emp_min_state_org"));
                    pst.setString(4, rs.getString("emp_dept"));
                    pst.setString(5, rs.getString("emp_sub_dept"));
                    pst.setString(6, rs.getString("emp_coord_email"));
                    pst.setString(7, email);
                    pst.setString(8, "delete");
                    pst.setString(9, formname + "_coordinator");
                    pst.setString(10, ip);
                    old_ca_email = rs.getString("emp_coord_email");
                    System.out.println("COORDINATOR UPDATE :: PS: " + pst);
                    pst.executeUpdate();
                }
                rs.close();

                // update status and final_audit_track table
                String sup_email = "";
                if (formname.equalsIgnoreCase("vpn")) {
                    sup_email = "vpnsupport@nic.in";
                    pst = conSlave.prepareStatement("select stat_form_type,stat_reg_no,stat_id from status where (stat_type='coordinator_pending' or (stat_type='da_pending')) and stat_forwarded_to_user = ? "
                            + "and stat_id in (select max(stat_id) from status group by stat_reg_no) and (stat_form_type='vpn_single' or stat_form_type='vpn_renew' or stat_form_type='change_add') order by stat_reg_no");
                }
                pst.setString(1, old_ca_email);
                System.out.println("COORDINATOR UPDATE :: PS3: " + pst);
                ResultSet rs1 = pst.executeQuery();
                while (rs1.next()) {
                    String registration_no = rs1.getString("stat_reg_no");
                    String form_type = rs1.getString("stat_form_type");
                    pst1 = con.prepareStatement("INSERT INTO status SET stat_type = 'support_pending', stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = 'c', stat_forwarded_by_user = ?"
                            + ", stat_forwarded_to = 's',stat_forwarded_to_user = '" + sup_email + "' , stat_remarks = ?, stat_process='reverted_c_s'");
                    pst1.setString(1, form_type);
                    pst1.setString(2, registration_no.toUpperCase());
                    pst1.setString(3, old_ca_email);
                    pst1.setString(4, "coordinator deleted, request moved to support");
                    System.out.println("COORDINATOR UPDATE :: PS4: " + pst1);
                    pst1.executeUpdate();
                }
                rs1.close();

                if (formname.equalsIgnoreCase("email")) {
                    pst = conSlave.prepareStatement("select track_id,registration_no from final_audit_track where (status='coordinator_pending' or status='da_pending') and to_email=?"
                            + " and (form_name='single' or form_name='bulk' or form_name='gem' or form_name='nkn_single' or form_name='nkn_bulk')");
                } else if (formname.equalsIgnoreCase("vpn")) {
                    pst = conSlave.prepareStatement("select track_id,registration_no from final_audit_track where (status='coordinator_pending' or status='da_pending') and to_email=?"
                            + " and (form_name='vpn_single' or form_name='vpn_renew' or form_name='change_add')");
                } else {
                    pst = conSlave.prepareStatement("select track_id,registration_no from final_audit_track where (status='coordinator_pending' or status='da_pending') and to_email=?"
                            + " and form_name='" + formname + "'");
                }

                pst.setString(1, old_ca_email);
                System.out.println("COORDINATOR UPDATE :: PS5: " + pst);
                ResultSet rs2 = pst.executeQuery();
                while (rs2.next()) {
                    String registration_no = rs2.getString("registration_no");

                    pst1 = con.prepareStatement("UPDATE final_audit_track SET support_email = null, support_mobile = null, support_name = null, support_ip = null, support_datetime = null"
                            + ",support_remarks = ?, status = 'support_pending' , to_email = ?, to_datetime = CURRENT_TIMESTAMP WHERE registration_no = ?");
                    pst1.setString(1, "coordinator deleted, request moved to support");
                    pst1.setString(2, sup_email);
                    pst1.setString(3, registration_no.toUpperCase());
                    System.out.println("COORDINATOR UPDATE :: PS6: " + pst);
                    pst1.executeUpdate();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String myTable(String registration_number) {
        String table = "";
        if (registration_number.contains("IMAPPOP-FORM")) {
            table = "imappop_registration";
        } else if (registration_number.contains("DLIST-FORM")) {
            table = "distribution_registration";
        }
        else if (registration_number.contains("BULKDLIST")) {
            table = "bulk_distribution_registration";            //Added by Rahul jan 2021
        }
        else if (registration_number.contains("LDAP-FORM")) {
            table = "ldap_registration";
        } else if (registration_number.contains("RELAY-FORM")) {
            table = "relay_registration";
        } else if (registration_number.contains("IP-FORM")) {
            table = "ip_registration";
        } else if (registration_number.contains("SINGLEUSER-FORM")) {
            table = "single_registration";
        } else if (registration_number.contains("BULKUSER-FORM")) {
            table = "bulk_registration";
        } else if (registration_number.contains("NKN-FORM")) {
            table = "nkn_registration";
        } else if (registration_number.contains("NKN-BULK-FORM")) {
            table = "bulk_registration";
        } else if (registration_number.contains("SMS-FORM")) {
            table = "sms_registration";
        } else if (registration_number.contains("MOBILE")) {
            table = "mobile_registration";
        } else if (registration_number.contains("GEM-FORM")) {
            table = "gem_registration";
        } else if (registration_number.contains("DNS-FORM")) {
            table = "dns_registration";
        } else if (registration_number.contains("WIFI-FORM")) {
            table = "wifi_registration";
        } else if (registration_number.contains("WEBCAST-FORM")) {
            table = "webcast_registration";
        } else if (registration_number.contains("VPN")) {
            table = "vpn_registration";
        }
        return table;
    }
}