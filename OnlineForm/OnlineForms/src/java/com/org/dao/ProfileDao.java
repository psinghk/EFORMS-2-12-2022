package com.org.dao;

import com.org.bean.CoordinatorPanelBean;
import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

public class ProfileDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public List getCentralMinistry(String name) {
        List list = new ArrayList();
        String ministry="";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProfileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        try {

            stmt1 = conSlave.prepareStatement("SELECT distinct emp_min_state_org FROM employment_coordinator where emp_category=? group by emp_min_state_org");
            stmt1.setString(1, name);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + stmt1);
            rs = stmt1.executeQuery();
            while (rs.next()) {
                    ministry = rs.getString(1).replaceAll("( +)"," ").trim();
                 // System.out.println("ministry :: "+ministry);
                    list.add(ministry);    
            }
        } catch (Exception e) {

        } finally {
            try {
                if (stmt1 != null) {
                    stmt1.close();
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

        return list;
    }

    public List getCentralDept(String name) {

        List list = new ArrayList();
        PreparedStatement ps = null, ps1 = null;
        ResultSet rs = null, rs1 = null;
        String department = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            ps = conSlave.prepareStatement("SELECT DISTINCT(emp_dept) FROM employment_coordinator where emp_min_state_org=? ORDER BY emp_dept='Other' ASC, emp_dept ASC");
            ps.setString(1, name.replace("&amp;", "&"));
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString(1) != null && !rs.getString(1).equals("")) {
                    department = rs.getString(1).replaceAll("( +)"," ").trim();
                    
             System.out.println("DEPARTMENT :: "+department);
                    list.add(department);
                }
            }
            if (list.isEmpty()) {
                ps1 = conSlave.prepareStatement("SELECT DISTINCT(emp_domain) FROM employment_coordinator where emp_min_state_org=?");
                ps1.setString(1, name.replace("&amp;", "&"));
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + ps1);
                rs1 = ps1.executeQuery();
                
                while (rs1.next()) {
                    department = rs.getString(1).replaceAll("( +)"," ").trim();
                    
System.out.println("DEPARTMENT :: "+department);
                    list.add(department);
                }
            }

            //}
//            rs.close();
//            stmt1.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps1 != null) {
                    ps1.close();
                }
                if (rs != null) {
                    rs.close();
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

        return list;

    }

    public int insert_profile(FormBean form_details, boolean user_flag, String otp_type, Map hodDetails, Set<String> aliases) {
        System.out.println("inisde here");
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        String user_email = "";
        //Connection con = null;
        int i = 0;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            System.out.println("user_flag" + user_flag);
            if (user_flag == true) {
                for (String emailaddress : aliases) {
                    String search_record_dns = "select auth_email from dns_registration where auth_email = ?";
                    pst2 = conSlave.prepareStatement(search_record_dns);
                    pst2.setString(1, emailaddress);
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        user_email = rs2.getString("auth_email");

                        String query = "update dns_registration set emp_code=?,auth_off_name=?,designation=?,address=?,city=?,add_state=?,"
                                + "pin=?,ophone=?,rphone=?,mobile=?,hod_name=?,hod_email=?,hod_mobile=?,"
                                + "hod_telephone=?,ca_desig=?,userip=?,employment=?,ministry=?,department=?,other_dept=?,state=?,organization=? where auth_email=?";
                        pst1 = con.prepareStatement(query);

                        pst1.setString(1, form_details.getUser_empcode());
                        pst1.setString(2, form_details.getUser_name());
                        pst1.setString(3, form_details.getUser_design());
                        pst1.setString(4, form_details.getUser_address());
                        pst1.setString(5, form_details.getUser_city());
                        pst1.setString(6, form_details.getUser_state());
                        pst1.setString(7, form_details.getUser_pincode());
                        pst1.setString(8, form_details.getUser_ophone());
                        pst1.setString(9, form_details.getUser_rphone());
                        pst1.setString(10, form_details.getUser_mobile());
                        pst1.setString(11, form_details.getHod_name());
                        pst1.setString(12, form_details.getHod_email());
                        pst1.setString(13, form_details.getHod_mobile());
                        pst1.setString(14, form_details.getHod_tel());
                        pst1.setString(15, form_details.getCa_design());
                        pst1.setString(16, ip);
                        pst1.setString(17, form_details.getUser_employment());

                        if (form_details.getUser_employment().equals("Central") || form_details.getUser_employment().equals("UT")) {
                            pst1.setString(18, form_details.getMin());
                            pst1.setString(20, form_details.getDept());
                            pst1.setString(21, "");
                            pst1.setString(22, "");
                        } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                            pst1.setString(18, "");
                            pst1.setString(20, "");
                            pst1.setString(21, "");
                            pst1.setString(22, form_details.getOrg());
                        } else {
                            pst1.setString(18, "");
                            pst1.setString(20, form_details.getState_dept());
                            pst1.setString(21, form_details.getStateCode());
                            pst1.setString(22, "");
                        }
                        pst1.setString(19, form_details.getOther_dept());
                        pst1.setString(23, emailaddress);
                        i = pst1.executeUpdate();
                        if (i > 0) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "i: " + i);
                            break;
                        }

                    }
                }

                String query = "INSERT INTO user_profile(email,mobile,name,ophone,rphone,designation,emp_code,address,city,add_state,pin,"
                        + "employment,ca_name,ca_desig,ca_mobile,ca_email,hod_name,hod_email,hod_mobile,hod_telephone,userip,"
                        + "ministry,department,other_dept,state,organization,user_alt_address,under_sec_name,under_sec_desig,under_sec_mobile,under_sec_email,under_sec_telephone) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(query);
                pst.setString(1, form_details.getUser_email());
                pst.setString(2, form_details.getUser_mobile());
                pst.setString(3, form_details.getUser_name());
                pst.setString(4, form_details.getUser_ophone());
                pst.setString(5, form_details.getUser_rphone());
                pst.setString(6, form_details.getUser_design());
                pst.setString(7, form_details.getUser_empcode());
                pst.setString(8, form_details.getUser_address());
                pst.setString(9, form_details.getUser_city());
                pst.setString(10, form_details.getUser_state());
                pst.setString(11, form_details.getUser_pincode());
                pst.setString(12, form_details.getUser_employment());
                pst.setString(13, form_details.getCa_name());
                pst.setString(14, form_details.getCa_design());
                pst.setString(15, form_details.getCa_mobile().trim());
                pst.setString(16, form_details.getCa_email());
                pst.setString(17, form_details.getHod_name());
                pst.setString(18, form_details.getHod_email());
                pst.setString(19, form_details.getHod_mobile().trim());
                pst.setString(20, form_details.getHod_tel());
                pst.setString(21, ip);

                if (form_details.getUser_employment().equals("Central") || form_details.getUser_employment().equals("UT")) {
                    pst.setString(22, form_details.getMin());
                    pst.setString(23, form_details.getDept());
                    pst.setString(25, "");
                    pst.setString(26, "");
                } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                    pst.setString(22, "");
                    pst.setString(23, "");
                    pst.setString(25, "");
                    pst.setString(26, form_details.getOrg());
                } else {
                    pst.setString(22, "");
                    pst.setString(23, form_details.getState_dept());
                    pst.setString(25, form_details.getStateCode());
                    pst.setString(26, "");
                }
                pst.setString(24, form_details.getOther_dept());
                pst.setString(27, form_details.getUser_alt_address());
                pst.setString(28, form_details.getUnder_sec_name());
                pst.setString(29, form_details.getunder_sec_desig());
                pst.setString(30, form_details.getUnder_sec_mobile());
                pst.setString(31, form_details.getUnder_sec_email());
                pst.setString(32, form_details.getUnder_sec_tel());
                i = pst.executeUpdate();
                System.out.println("pst $$$$$$$$$$$$$$$$$" + pst);
            } else {
                for (String emailaddress : aliases) {
                    String search_record_dns = "select auth_email from dns_registration where auth_email = ?";
                    pst2 = conSlave.prepareStatement(search_record_dns);
                    pst2.setString(1, emailaddress);
                    ResultSet rs2 = pst2.executeQuery();
                    if (rs2.next()) {
                        user_email = rs2.getString("auth_email");
                        System.out.println("user_email in dao::::" + user_email);

                        String query = "update dns_registration set emp_code=?,auth_off_name=?,designation=?,address=?,city=?,add_state=?,"
                                + "pin=?,ophone=?,rphone=?,mobile=?,hod_name=?,hod_email=?,hod_mobile=?,"
                                + "hod_telephone=?,ca_desig=?,userip=?,employment=?,ministry=?,department=?,other_dept=?,state=?,organization=? where auth_email=?";
                        pst1 = con.prepareStatement(query);

                        pst1.setString(1, form_details.getUser_empcode());
                        pst1.setString(2, form_details.getUser_name());
                        pst1.setString(3, form_details.getUser_design());
                        pst1.setString(4, form_details.getUser_address());
                        pst1.setString(5, form_details.getUser_city());

                        pst1.setString(6, form_details.getUser_state());
                        pst1.setString(7, form_details.getUser_pincode());
                        pst1.setString(8, form_details.getUser_ophone());
                        pst1.setString(9, form_details.getUser_rphone());
                        pst1.setString(10, form_details.getUser_mobile());
                        pst1.setString(11, form_details.getHod_name());
                        pst1.setString(12, form_details.getHod_email());
                        pst1.setString(13, form_details.getHod_mobile());
                        pst1.setString(14, form_details.getHod_tel());
                        pst1.setString(15, form_details.getCa_design());
                        pst1.setString(16, ip);
                        pst1.setString(17, form_details.getUser_employment());

                        if (form_details.getUser_employment().equals("Central") || form_details.getUser_employment().equals("UT")) {
                            pst1.setString(18, form_details.getMin());
                            pst1.setString(20, form_details.getDept());
                            pst1.setString(21, "");
                            pst1.setString(22, "");
                        } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                            pst1.setString(18, "");
                            pst1.setString(20, "");
                            pst1.setString(21, "");
                            pst1.setString(22, form_details.getOrg());
                        } else {
                            pst1.setString(18, "");
                            pst1.setString(20, form_details.getState_dept());
                            pst1.setString(21, form_details.getStateCode());
                            pst1.setString(22, "");
                        }
                        pst1.setString(19, form_details.getOther_dept());
                        pst1.setString(23, emailaddress);
                        //pst1.setString(23, form_details.getUser_email());
                        System.out.println("i::inside else::::$$$$$$$$$$::::" + pst1);
                        i = pst1.executeUpdate();
                        if (i > 0) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "i: " + i);
                            break;
                        }

                    }
                }

                System.out.println("alias value of set:::::::" + aliases);
                for (String emailaddress : aliases) {
                    String query = "update user_profile set name=?,ophone=?,rphone=?,designation=?,emp_code=?,address=?,city=?,add_state=?,"
                            + "pin=?,employment=?,ca_name=?,ca_desig=?,ca_mobile=?,ca_email=?,hod_name=?,hod_email=?,hod_mobile=?,"
                            + "hod_telephone=?,userip=?,ministry=?,department=?,other_dept=?,state=?,organization=?,user_alt_address=?,under_sec_name=?,under_sec_desig=?,under_sec_mobile=?,under_sec_email=?,under_sec_telephone=? where email=?";
                    pst = con.prepareStatement(query);

                    pst.setString(1, form_details.getUser_name());
                    pst.setString(2, form_details.getUser_ophone());
                    pst.setString(3, form_details.getUser_rphone());
                    pst.setString(4, form_details.getUser_design());
                    pst.setString(5, form_details.getUser_empcode());
                    pst.setString(6, form_details.getUser_address());
                    pst.setString(7, form_details.getUser_city());
                    pst.setString(8, form_details.getUser_state());
                    pst.setString(9, form_details.getUser_pincode());
                    pst.setString(10, form_details.getUser_employment());
                    pst.setString(11, form_details.getCa_name());
                    pst.setString(12, form_details.getCa_design());
                    pst.setString(13, form_details.getCa_mobile());
                    pst.setString(14, form_details.getCa_email());
                    pst.setString(15, form_details.getHod_name());
                    pst.setString(16, form_details.getHod_email());
                    pst.setString(17, form_details.getHod_mobile());
                    pst.setString(18, form_details.getHod_tel());
                    pst.setString(19, ip);

                    if (form_details.getUser_employment().equals("Central") || form_details.getUser_employment().equals("UT")) {
                        pst.setString(20, form_details.getMin());
                        pst.setString(21, form_details.getDept());
                        pst.setString(23, "");
                        pst.setString(24, "");
                    } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                        pst.setString(20, "");
                        pst.setString(21, "");
                        pst.setString(23, "");
                        pst.setString(24, form_details.getOrg());
                    } else {
                        pst.setString(20, "");
                        pst.setString(21, form_details.getState_dept());
                        pst.setString(23, form_details.getStateCode());
                        pst.setString(24, "");
                    }
                    pst.setString(22, form_details.getOther_dept());
                    //ADDED FOR COORDINATOR PANNEL
                      if(form_details.getOther_dept()!=null && !form_details.getOther_dept().equals("")){
                        System.out.println("Entering here");
                        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
                        CoordinatorPanelBean cpanelbean = new CoordinatorPanelBean();
                        cpanelbean.setEmp_category(form_details.getUser_employment());
                        cpanelbean.setEmp_min_state_org(form_details.getMin());
                        cpanelbean.setEmp_dept("others");
                        cpanelbean.setOther_dept(form_details.getOther_dept());
                        cpanelbean.setRequested_by(form_details.getUser_email());
                        //String hog = cpaneldao.getHog(form_details.getMin());
                        //cpanelbean.setRequested_to(hog);
                        cpanelbean.setStatus("pending");
                        cpaneldao.addDepartmentRequest(cpanelbean);
                    }
                      //EO ADDED FOR COORDINATOR PANNEL
                    
                    pst.setString(25, form_details.getUser_alt_address());
                    pst.setString(26, form_details.getUnder_sec_name());
                    pst.setString(27, form_details.getunder_sec_desig());
                    pst.setString(28, form_details.getUnder_sec_mobile());
                    pst.setString(29, form_details.getUnder_sec_email());
                    pst.setString(30, form_details.getUnder_sec_tel());
                    pst.setString(31, emailaddress);
                    //  pst.setString(32, form_details.getUser_mobile());
                    i = pst.executeUpdate();
                    if (i > 0) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "i: " + i);
                        break;
                    }

                }
                //HOD & HOG VALUES UPDATE
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PSS: " + pst);

            System.out.println("i for profile:::::::::" + i);
        } catch (Exception e) {
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
        return i;
    }

    public int update_profile(FormBean form_details, String email) {
        System.out.println("mobile:::" + form_details.getUser_mobile());
        System.out.println("email:::" + form_details.getUser_email());
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        String user_email = "";

        int i = 0;
        try {
            con = DbConnection.getConnection();
            String query = "update user_profile set employment=?,ca_name=?,ca_desig=?,ca_mobile=?,ca_email=?,hod_name=?,hod_email=?,hod_mobile=?,"
                    + "hod_telephone=?,userip=?,ministry=?,department=?,other_dept=?,state=?,organization=?,under_sec_name=?,under_sec_desig=?,under_sec_mobile=?,under_sec_email=?,under_sec_telephone=? where email=?";
            pst = con.prepareStatement(query);
            pst.setString(1, form_details.getUser_employment());
            pst.setString(2, form_details.getHod_name());
            pst.setString(3, form_details.getCa_design());
            pst.setString(4, form_details.getHod_mobile());
            pst.setString(5, form_details.getHod_email());
            pst.setString(6, form_details.getHod_name());
            pst.setString(7, form_details.getHod_email());
            pst.setString(8, form_details.getHod_mobile());
            pst.setString(9, form_details.getHod_tel());
            pst.setString(10, ip);

            if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                pst.setString(11, form_details.getMin().trim());   // ministry
                pst.setString(12, form_details.getDept().trim());  // dept
                pst.setString(13, form_details.getOther_dept().trim());   // other 
                pst.setString(14, "");                      // state
                pst.setString(15, "");   //org
            } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                pst.setString(11, "");   // ministry
                pst.setString(12, "");  // dept
                pst.setString(13, form_details.getOther_dept().trim());   // other 
                pst.setString(14, "");                      // state
                pst.setString(15, form_details.getOrg().trim());   //org
            } else {
                pst.setString(11, "");   // ministry
                pst.setString(12, form_details.getState_dept().trim());  // dept
                pst.setString(13, form_details.getOther_dept().trim());   // other 
                pst.setString(14, form_details.getStateCode().trim());                      // state
                pst.setString(15, "");   //org
            }
            pst.setString(16, form_details.getUnder_sec_name());
            pst.setString(17, form_details.getunder_sec_desig());
            pst.setString(18, form_details.getUnder_sec_mobile());
            pst.setString(19, form_details.getUnder_sec_email());
            pst.setString(20, form_details.getUnder_sec_tel());

            pst.setString(21, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PSS in profile dao update from preview: " + pst);
            i = pst.executeUpdate();

        } catch (Exception e) {
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
        return i;
    }

    public List getDistrict(String name) {
        List list = new ArrayList();
        String district="";
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProfileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement stmt1 = null;
        ResultSet rs = null;
        try {
            stmt1 = conSlave.prepareStatement("SELECT  distname FROM district where stname=? group by distname");
            stmt1.setString(1, name);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + stmt1);
            rs = stmt1.executeQuery();
            while (rs.next()) {
                  district = rs.getString(1).replaceAll("( +)"," ").trim();
                  System.out.println("district :: "+district);
                  list.add(district);   
            }
        } catch (Exception e) {

        } finally {
            try {
                if (stmt1 != null) {
                    stmt1.close();
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

        return list;
    }
}
