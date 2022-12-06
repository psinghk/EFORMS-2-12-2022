/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.org.bean.MoveReqBean;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.dto.StatusDetails;
import com.org.utility.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author Mohit Sharma
 */
public class MoveReqDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;
    Date date = new Date();
    DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String pdate = dt.format(date);

    public List<FinalAuditDetails> getAllPendingRequsts(String email) {
        Ldap ldap = new Ldap();
        Set<String> aliases = new HashSet<>();
        aliases = ldap.fetchAliases(email);
        System.out.println("inside of dao in Move request");
        PreparedStatement pst = null;
        // String to_email = email;
        List<FinalAuditDetails> list = new ArrayList<>();
        HashSet<String> rolls = new HashSet<>();
        try {
            for (String to_email : aliases) {
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                ResultSet rs = null;
                String sql = "select * from final_audit_track WHERE to_email ='" + to_email + "'and status LIKE '%_pending%' ";
                pst = conSlave.prepareStatement(sql);
                rs = pst.executeQuery();
                System.out.println("rs::::values" + rs);
                while (rs.next()) {

                    FinalAuditDetails finalAuditDetails = new FinalAuditDetails();
                    finalAuditDetails.setRegistrationNumber(rs.getString("registration_no"));
                    finalAuditDetails.setApplicantEmail(rs.getString("applicant_email"));
                    finalAuditDetails.setStatus(rs.getString("status"));
                    finalAuditDetails.setFormName(rs.getString("form_name"));
                    finalAuditDetails.setToMobile(rs.getString("to_mobile"));
                    finalAuditDetails.setToEmail(rs.getString("to_email"));
                    finalAuditDetails.setToName(rs.getString("to_name"));
                    list.add(finalAuditDetails);
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
        return list;

    }

//    public List<FinalAuditDetails> getAllPendingRequstsByRoll(String email, String status) {
//        Ldap ldap = new Ldap();
//        Set<String> aliases = new HashSet<>();
//        aliases = ldap.getAliases(email);
//        System.out.println("inside of dao in Move request");
//        PreparedStatement pst = null;
//        //String to_email = email;
//        //String status = "completed";
//        List<FinalAuditDetails> list = new ArrayList<>();
//        HashSet<String> rolls = new HashSet<>();
//        try {
//            for (String to_email : aliases) {
//
//                con = DbConnection.getConnection();
//                conSlave = DbConnection.getSlaveConnection(); //29dec2021
//                ResultSet rs = null;
//                String sql = "select * from final_audit_track WHERE to_email ='" + to_email + "' and status = '" + status + "' ";
//
//                pst = conSlave.prepareStatement(sql);
//                // pst.executeUpdate();
//                rs = pst.executeQuery();
//                System.out.println("rs::::values" + rs);
//                // if(sql.equalsIgnoreCase(to_email)){}
//                while (rs.next()) {
//
//                    FinalAuditDetails finalAuditDetails = new FinalAuditDetails();
//
//                    finalAuditDetails.setRegistrationNumber(rs.getString("registration_no"));
//                    finalAuditDetails.setApplicantEmail(rs.getString("applicant_email"));
//                    finalAuditDetails.setStatus(rs.getString("status"));
//                    finalAuditDetails.setFormName(rs.getString("form_name"));
//                    finalAuditDetails.setToMobile(rs.getString("to_mobile"));
//                    finalAuditDetails.setToEmail(rs.getString("to_email"));
//                    //finalAuditDetails.set(rs.getString("app_user_type"));
//                    finalAuditDetails.setToName(rs.getString("to_name"));
//                    list.add(finalAuditDetails);
//
//                    //break;
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            try {
//                if (pst != null) {
//                    pst.close();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return list;
//
//    }
    public List<FinalAuditDetails> getAllPendingRequstsByFinalAuditTrack(Map<String, String> radioEmpCatMinDept, String email, String status) {
        Ldap ldap = new Ldap();
        Set<String> aliases = new HashSet<>();
        aliases = ldap.fetchAliases(email);
        System.out.println("inside of dao in Move request");
        PreparedStatement pst = null;
        //String to_email = email;
        //String status = "completed";
        List<FinalAuditDetails> list = new ArrayList<>();
        HashSet<String> rolls = new HashSet<>();
        try {
            for (String to_email : aliases) {

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                ResultSet rs = null;
                String sql = "select * from final_audit_track WHERE to_email ='" + to_email + "' and status = '" + status + "' ";

                pst = conSlave.prepareStatement(sql);
                // pst.executeUpdate();
                rs = pst.executeQuery();
                System.out.println("rs::::values" + rs);
                // if(sql.equalsIgnoreCase(to_email)){}
                while (rs.next()) {

                    FinalAuditDetails finalAuditDetails = new FinalAuditDetails();

                    finalAuditDetails.setRegistrationNumber(rs.getString("registration_no"));
                    finalAuditDetails.setApplicantEmail(rs.getString("applicant_email"));
                    finalAuditDetails.setStatus(rs.getString("status"));
                    finalAuditDetails.setFormName(rs.getString("form_name"));
                    finalAuditDetails.setToMobile(rs.getString("to_mobile"));
                    finalAuditDetails.setToEmail(rs.getString("to_email"));
                    //finalAuditDetails.set(rs.getString("app_user_type"));
                    finalAuditDetails.setToName(rs.getString("to_name"));
                    list.add(finalAuditDetails);

                    //break;
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
        return list;

    }

    public String updateAllRecoads(List<FinalAuditDetails> details, String updateEmail,String status) {

        System.out.println("inside of dao updateAllRecoads");
        PreparedStatement pst = null;
        String update = updateEmail;
        int totalEntryCount = 0;
        int value = 0;
        ResultSet rs ;
        for (Iterator itr = details.iterator(); itr.hasNext();) {
            System.out.println("itrater::::::" + itr);
            FinalAuditDetails audit = (FinalAuditDetails) itr.next();
            if (audit.getRegistrationNumber() != null && audit.getRegistrationNumber() != "") {
                String registration = audit.getRegistrationNumber();

                try {
                    con = DbConnection.getConnection();
                    String sql = "update final_audit_track set to_email=? where registration_no=?";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, updateEmail);
                    pst.setString(2, registration);
                    pst.executeUpdate();
                    
                    sql = "update status set stat_forwarded_to_user=?, stat_remarks = 'Move Request' where stat_id in (select max(stat_id) from status where stat_reg_no=? AND stat_type =?)";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, updateEmail);
                    pst.setString(2, registration);
                    pst.setString(3, status);

                    pst.executeUpdate();
                    value = totalEntryCount++;

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in Update All Recoads: " + e.getMessage());
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

        }
        System.out.println("Total Record Updated:::::::::: " + value);

        return SUCCESS;
    }

    
    public String updateFinalRecords(String updateEmail, String updateMobile, String updateName, String registration) {

        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();

            String sql = "update final_audit_track set to_email=?,to_mobile=?,to_name=? where registration_no=?";

            pst = con.prepareStatement(sql);
            pst.setString(1, updateEmail);
            pst.setString(2, updateMobile);
            pst.setString(3, updateName);
            pst.setString(4, registration);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in Update All Recoads: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return SUCCESS;
    }

    public String insertIntoStatus(List<FinalAuditDetails> AllDetails, String updateEmail, String roll, String hodMobile, UserData userData) throws ClassNotFoundException {
        con = DbConnection.getConnection();
        PreparedStatement pst = null;
        System.out.println("insertIntoStatus:::::::::::");
        int totalEntryCount = 0;
        int value = 0;
        try {

            String sql = "insert into status(stat_form_type,stat_reg_no,stat_type,stat_forwarded_by,stat_forwarded_by_user,stat_forwarded_to,stat_forwarded_to_user,"
                    + "stat_remarks,stat_active,stat_forwarded_by_email,stat_forwarded_by_mobile)values (?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);

            for (int i = 0; i < AllDetails.size(); i++) {

                pst.setString(1, AllDetails.get(i).getFormName());
                pst.setString(2, AllDetails.get(i).getRegistrationNumber());
                pst.setString(3, AllDetails.get(i).getStatus());
                pst.setString(4, "s");
//                if (AllDetails.get(i).getStatus().equalsIgnoreCase("ca_pending")) {
//                    pst.setString(4, "a");
//                } else if (AllDetails.get(i).getStatus().equalsIgnoreCase("support_pending")) {
//                    pst.setString(4, "ca");
//                } else if (AllDetails.get(i).getStatus().equalsIgnoreCase("da_pending")) {
//                    pst.setString(4, "s");
//                } else {
//                    pst.setString(4, "");
//                }

                pst.setString(5, userData.getEmail());

                if (AllDetails.get(i).getStatus().equalsIgnoreCase("ca_pending")) {
                    pst.setString(6, "ca");
                } else {
                    pst.setString(6, "");
                }

                pst.setString(7, updateEmail);
                pst.setString(8, " Request Moved by Support ");
                pst.setString(9, "a");
                pst.setString(10, userData.getEmail());

                if (!userData.getMobile().isEmpty()) {
                    pst.setString(11, userData.getMobile());
                } else {
                    pst.setString(11, "");
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == MoveRequest dao tab 2: " + pst);
                pst.executeUpdate();
                value = totalEntryCount++;
                // pst.close();
            }
            System.out.println("Total Entry inserted:::::::::: " + value);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in Update All Recoads: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    }

    public String updateIntoStatus(List<FinalAuditDetails> AllDetails, String updateEmail, String roll, String hodMobile, UserData userData) throws Exception {

        return SUCCESS;
    }

    public Map<String, String> finalStatus(String applicantEmail, String oldToEmail, String newToEmail, String status) {

        Map<String, String> map = new HashMap<>();
        try {
            String sql = "select * from final_audit_track where applicant_email = ? and to_email = ? and status = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, applicantEmail);
            ps.setString(2, oldToEmail);
            ps.setString(3, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put("registration_no", rs.getString("registration_no"));

            }
        } catch (SQLException e) {

        }
        return map;
    }

    public Map<String, String> fetchApplicantEmail(String empCategory, String cordMin, String cordDept) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        Map<String, String> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("SELECT email FROM `user_profile` WHERE ministry=? OR department= ? ");
            ps.setString(1, cordMin);
            ps.setString(2, cordDept);
            //ps.setString(3, empCategory);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchApplicantEmail: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("email", rs.getString("email"));
//                map.put("mobile", rs.getString("mobile"));
//                map.put("name", rs.getString("name"));
//                map.put("ophone", rs.getString("ophone"));
//                map.put("rphone", rs.getString("rphone"));
//                map.put("designation", rs.getString("designation"));
//                map.put("emp_code", rs.getString("emp_code"));
//                map.put("address", rs.getString("address"));
//                map.put("city", rs.getString("city"));
//                map.put("add_state", rs.getString("add_state"));
//                map.put("pin", rs.getString("pin"));
//                map.put("employment", rs.getString("employment"));
//                map.put("ministry", rs.getString("ministry"));
//                map.put("state", rs.getString("state"));
//                map.put("department", rs.getString("department"));
//                map.put("other_dept", rs.getString("other_dept"));
//                map.put("organization", rs.getString("organization"));
//                map.put("hod_name", rs.getString("hod_name"));
//                map.put("ca_desig", rs.getString("ca_desig"));
//                map.put("hod_mobile", rs.getString("hod_mobile"));
//                map.put("hod_email", rs.getString("hod_email"));
//                map.put("hod_telephone", rs.getString("hod_telephone"));
//                map.put("under_sec_email", rs.getString("under_sec_email"));
//                map.put("under_sec_name", rs.getString("under_sec_name"));
//                map.put("under_sec_tel", rs.getString("under_sec_telephone"));
//                map.put("under_sec_mobile", rs.getString("under_sec_mobile"));
//                map.put("under_sec_desig", rs.getString("under_sec_desig"));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
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
        return map;
    }

//    public Map<String, String> fetchDepartment(String email, Set<String> roles1) {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        //Connection con = null;
//        Map<String, String> map = new HashMap<>();
//        try {
//            conSlave = DbConnection.getSlaveConnection();
////            con = DbConnection.getConnection();
//            ps = conSlave.prepareStatement("select  from " + Constants.COORDINATOR_TABLE + " where employment = ? and ministry = ? and department = ?");
//            ps.setString(1, employment);
//            ps.setString(2, ministry);
//            ps.setString(3, department);
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchUserProfile: " + ps);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                map.put("email", rs.getString("email"));
//                map.put("mobile", rs.getString("mobile"));
//                map.put("name", rs.getString("name"));
//                map.put("ophone", rs.getString("ophone"));
//                map.put("rphone", rs.getString("rphone"));
//                map.put("designation", rs.getString("designation"));
//                map.put("emp_code", rs.getString("emp_code"));
//                map.put("address", rs.getString("address"));
//                map.put("city", rs.getString("city"));
//                map.put("add_state", rs.getString("add_state"));
//                map.put("pin", rs.getString("pin"));
//                map.put("employment", rs.getString("employment"));
//                map.put("ministry", rs.getString("ministry"));
//                map.put("state", rs.getString("state"));
//                map.put("department", rs.getString("department"));
//                map.put("other_dept", rs.getString("other_dept"));
//                map.put("organization", rs.getString("organization"));
//                map.put("hod_name", rs.getString("hod_name"));
//                map.put("ca_desig", rs.getString("ca_desig"));
//                map.put("hod_mobile", rs.getString("hod_mobile"));
//                map.put("hod_email", rs.getString("hod_email"));
//                map.put("hod_telephone", rs.getString("hod_telephone"));
//                map.put("under_sec_email", rs.getString("under_sec_email"));
//                map.put("under_sec_name", rs.getString("under_sec_name"));
//                map.put("under_sec_tel", rs.getString("under_sec_telephone"));
//                map.put("under_sec_mobile", rs.getString("under_sec_mobile"));
//                map.put("under_sec_desig", rs.getString("under_sec_desig"));
//            }
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//                if (rs != null) {
//                    rs.close();
//                }
//                if (con != null) {
//                    // con.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return map;
//    }
}
