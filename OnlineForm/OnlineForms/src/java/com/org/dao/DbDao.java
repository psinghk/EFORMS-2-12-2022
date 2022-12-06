package com.org.dao;

import com.org.bean.UserData;
import com.org.bean.UserOfficialData;
import com.org.connections.DbConnection;
import com.org.dao.contracts.IDao;
import com.org.utility.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import model.Person;
import org.apache.struts2.ServletActionContext;

public class DbDao implements IDao {

    private static final String pari_timeout = ServletActionContext.getServletContext().getInitParameter("pari_timeout");

    private static final String dbname = ServletActionContext.getServletContext().getInitParameter("db-name");
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    //String ip = "10.26.112.140";
    Connection con = null;
    Connection conSlave = null;
    Map profile_values = new HashMap();
    Ldap ldap = null;

    String sessionid = ServletActionContext.getRequest().getSession().getId(); // line added by pr on 13thsep19

    public DbDao() {
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        ldap = new Ldap();
        Connection con = null; // this and below line added by pr on 24thmay18
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "IP in start #########: " + ip);
    }

    public int countRows(String tableName, String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        int rowCount = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            if (!email.isEmpty()) {
                ps = conSlave.prepareStatement("select count(*) from " + tableName + " where email = ?");
                ps.setString(1, email);
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for count rows: " + ps);
//            } else {
//                ps = con.prepareStatement("select count(*) from " + tableName);
//            }
            rs = ps.executeQuery();
            if (rs.next()) {
                rowCount = rs.getInt("count(*)");
            }
        } catch (SQLException | ClassNotFoundException ex) {
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
        return rowCount;
    }

    public boolean isNewUser(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select email from " + Constants.PROFILE_TABLE + " where email = ?");
            ps.setString(1, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for is new user: " + ps);
            rs = ps.executeQuery();
        } catch (SQLException | ClassNotFoundException ex) {
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
        return true;
    }

    public List<String> getStates() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        List<String> states = new ArrayList<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select distinct stname from " + Constants.STATE_TABLE + " order by stname");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for get state: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                states.add(rs.getString("stname").toUpperCase());
            }
        } catch (SQLException | ClassNotFoundException ex) {
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
        return states;
    }

    public Set<String> getRoles(String email, String mobile, Set<String> roles1, boolean isIsEmailValidated) {
        Set<String> roles = new HashSet<>();
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        int rowCount = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (!roles1.contains("admin")) {
                if (isIsEmailValidated) {
                    if (email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("smssupport@gov.in") || email.equals("support@nkn.in") || email.equals("smssupport@nic.in") || email.equals("vpnsupport@nic.in")) {
                        ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where mobile = ?  and individual_ip=?");
                        ps.setString(1, mobile);
                        ps.setString(2, ip);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                        //System.out.println("PS1 : " + ps);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            rowCount = rs.getInt("count(*)");
                        }
                        if (rowCount > 0) {
                            roles.add("admin");
                        }
                    } else {
                        ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where email_address = ? and individual_ip=?");
                        ps.setString(1, email);
                        ps.setString(2, ip);
                        //System.out.println("PS1 : " + ps);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            rowCount = rs.getInt("count(*)");
                        }
                        if (rowCount > 0) {
                            roles.add("admin");
                        }
                    }
                }

            }
            if (!roles1.contains("sup")) {
                ps = conSlave.prepareStatement("select count(*) from " + Constants.SUPPORT_TABLE + " where ip = ?");
                ps.setString(1, ip);
                System.out.println("PS1 : " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rowCount = rs.getInt("count(*)");
                }
                if (rowCount > 0) {
                    if (isIsEmailValidated) {
                        if (email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("smssupport@gov.in") || email.equals("support@nkn.in") || email.equals("smssupport@nic.in") || email.equals("vpnsupport@nic.in")) {
                            ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where mobile = ?");
                            ps.setString(1, mobile);
                            //System.out.println("PS1 : " + ps);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                rowCount = rs.getInt("count(*)");
                            }
                            if (rowCount > 0) {
                                roles.add("sup");
                            }
                        } else {
                            ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where email_address = ?");
                            ps.setString(1, email);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                            rs = ps.executeQuery();
                            while (rs.next()) {
                                rowCount = rs.getInt("count(*)");
                            }
                            if (rowCount > 0) {
                                roles.add("sup");
                            }
                        }
                    }
                }

            }
            if (!roles1.contains("email_co")) {
                if (!(email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("smssupport@gov.in") || email.equals("support@nkn.in") || email.equals("smssupport@nic.in") || email.equals("vpnsupport@nic.in"))) {
                    ps = conSlave.prepareStatement("select ip from " + Constants.COORDINATOR_TABLE + " where emp_coord_email = ?");
                    ps.setString(1, email);
                    //ps.setString(2, ip);
                    //System.out.println("PS2 : " + ps);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
                    rs = ps.executeQuery();
                    String ip = "";
                    while (rs.next()) {
                        //rowCount = rs.getInt("count(*)");
                        //if (rowCount > 0) {
                        if (rs.getString("ip") == null) {
                            continue;
                        }
                        ip = rs.getString("ip").trim();

                        if (rs.getString("ip").contains(",")) {
                            boolean localFlag = false;
                            String[] ipList = rs.getString("ip").split(",");
                            for (String name : ipList) {
                                if ((name.trim()).equals(this.ip)) {
                                    localFlag = true;
                                    roles.add("co");
                                    roles.add("email_co");
                                    break;
                                }
                            }
                            if (localFlag) {
                                break;
                            }
                        }

                        if (ip.equalsIgnoreCase(this.ip)) {
                            roles.add("co");
                            roles.add("email_co");
                            break;
                        }

//                            if (rs.getString("ip") != null) {
//                                if (rs.getString("ip").contains(",")) {
//                                    String[] ipList = rs.getString("ip").split(",");
//                                    for (String name : ipList) {
//                                        if (name.equals(ip)) {
//                                            roles.add("co");
//                                            roles.add("email_co");
//                                            break;
//                                        }
//                                    }
//                                } else if (rs.getString("ip").equals(ip)) {
//                                    roles.add("co");
//                                    roles.add("email_co");
//                                    break;
//                                } else if (rs.getString("ip").equals("") || rs.getString("ip") == null) {
//                                    roles.add("co");
//                                    roles.add("email_co");
//                                    break;
//                                } else {
//                                    //break;
//                                }
//                            } else {
//                                roles.add("co");
//                                roles.add("email_co");
//                                break;
//                            }
                        //}
                    }

                }
            }
            if (!roles1.contains("vpn_co")) {
                ps = conSlave.prepareStatement("select count(*) from " + Constants.VPN_COORDINATOR_TABLE + " where emp_coord_email = ?");
                ps.setString(1, email);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rowCount = rs.getInt("count(*)");
                    if (rowCount > 0) {
                        roles.add("co");
                        roles.add("vpn_co");// 24thoct19
                        break;
//                        if (rs.getString("ip") != null) {
//                            if (rs.getString("ip").contains(",")) {
//                                String[] ipList = rs.getString("ip").split(",");
//                                for (String name : ipList) {
//                                    if (name.equals(ip)) {
//                                        roles.add("co");
//                                        roles.add("vpn_co");// 24thoct19
//                                        break;
//                                    }
//                                }
//                            } else if (rs.getString("ip").equals(ip)) {
//                                roles.add("co");
//                                roles.add("vpn_co");// 24thoct19
//                                break;
//                            } else if (rs.getString("ip").equals("") || rs.getString("ip") == null) {
//                                roles.add("co");
//                                roles.add("vpn_co");// 24thoct19
//                                break;
//                            } else {
//                                //break;
//                            }
//                        } else {
//                            roles.add("co");
//                            roles.add("vpn_co");// 24thoct19
//                            break;
//                        }
                    }
                }
            }

            if (!roles1.contains("ca")) {
                ps = conSlave.prepareStatement("select count(*) from " + Constants.CA_TABLE + " where ca_email = ?");
                ps.setString(1, email);
                //System.out.println("PS2 : " + ps);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rowCount = rs.getInt("count(*)");
                }
                if (rowCount > 0) {

                    roles.add("ca");
                }
            }
            if (!roles1.contains("dashboard")) {
                ps = conSlave.prepareStatement("select count(*) from " + Constants.DASHBOARD_TABLE + " where email = ?");
                ps.setString(1, email);
                //System.out.println("PS2 : " + ps);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rowCount = rs.getInt("count(*)");
                }
                if (rowCount > 0) {
                    roles.add("dashboard");
                }
            }
            if (!roles1.contains("user")) {
                roles.add("user");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for get roles: " + ps);
        } catch (SQLException | ClassNotFoundException ex) {
            // Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
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

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside getroles in DbDao roles : " + roles);

        return roles;
    }

    public String fetchMobileFromProfile(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        String mobile = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select mobile from " + Constants.PROFILE_TABLE + " where email = ?");
            ps.setString(1, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchMobileFromProfile: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                mobile = rs.getString("mobile");
            }
        } catch (ClassNotFoundException | SQLException ex) {
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
        return mobile;
    }

    public boolean insertOtp(int otp_mobile, int otp_email, String mobile, String email) {
        Map<String, String> map = fetchOtp(email, mobile);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("KEY " + key + "and value " + value);
        }
        if (map.isEmpty()) {
            PreparedStatement ps = null;
            int rs;
            //Connection con = null;
            try {

//                otp_mobile = 123456;
//                otp_email = 123456;
                con = DbConnection.getConnection();

                ps = con.prepareStatement("insert into " + Constants.OTP_TABLE + "(mobile,email,otp_mobile,otp_email,ip_address,attempt_login) VALUES(?,?,?,?,?,?)");
                ps.setString(1, mobile);
                ps.setString(2, email);
                ps.setInt(3, otp_mobile);
                ps.setInt(4, otp_email);
                ps.setString(5, ip);
                ps.setInt(6, 0);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertOtp: " + ps);
                rs = ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        // con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public Map<String, String> fetchOtp(String email, String mobile) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();
        //Connection con = null;
        try {
            try {
                conSlave = DbConnection.getSlaveConnection();
//                con = DbConnection.getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = conSlave.prepareStatement("select otp_mobile,otp_email,attempt_login from " + Constants.OTP_TABLE + " where email = ? and mobile= ? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and attempt_login < 6");
            ps.setString(1, email);
            ps.setString(2, mobile);
            //System.out.println("Query : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchOtp: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("otp_mobile", rs.getString("otp_mobile"));
                map.put("otp_email", rs.getString("otp_email"));
                map.put("otp_attempt", rs.getString("attempt_login"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public Map<String, String> fetchUserProfile(String email, String mobile) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        Map<String, String> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select * from " + Constants.PROFILE_TABLE + " where email = ?");
            ps.setString(1, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchUserProfile: " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                map.put("email", rs.getString("email"));
                map.put("mobile", rs.getString("mobile"));
                map.put("name", rs.getString("name"));
                map.put("ophone", rs.getString("ophone"));
                map.put("rphone", rs.getString("rphone"));
                map.put("designation", rs.getString("designation"));
                map.put("emp_code", rs.getString("emp_code"));
                map.put("address", rs.getString("address"));
                map.put("city", rs.getString("city"));
                map.put("add_state", rs.getString("add_state"));
                map.put("pin", rs.getString("pin"));
                map.put("employment", rs.getString("employment"));
                map.put("ministry", rs.getString("ministry"));
                map.put("state", rs.getString("state"));
                map.put("department", rs.getString("department"));
                map.put("other_dept", rs.getString("other_dept"));
                map.put("organization", rs.getString("organization"));
                map.put("hod_name", rs.getString("hod_name"));
                map.put("ca_desig", rs.getString("ca_desig"));
                map.put("hod_mobile", rs.getString("hod_mobile"));
                map.put("hod_email", rs.getString("hod_email"));
                map.put("hod_telephone", rs.getString("hod_telephone"));
                map.put("under_sec_email", rs.getString("under_sec_email"));
                map.put("under_sec_name", rs.getString("under_sec_name"));
                map.put("under_sec_tel", rs.getString("under_sec_telephone"));
                map.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                map.put("under_sec_desig", rs.getString("under_sec_desig"));
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

    public Map<String, String> fetchUserProfileFromOADOnEmpCode(String empCode) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select * from " + Constants.RO_TABLE + " where user_empcode = ?");
            ps.setString(1, empCode);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchUserProfileFromOADOnEmpCode: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("email", rs.getString("user_email"));
                map.put("mobile", rs.getString("user_mobile"));
                map.put("name", rs.getString("user_title") + rs.getString("user_name"));
                map.put("ophone", rs.getString("user_telephone"));
                map.put("designation", rs.getString("user_design"));
                map.put("emp_code", rs.getString("user_empcode"));
                map.put("address", rs.getString("user_address"));
                map.put("dob", rs.getString("user_dob"));
                map.put("city", rs.getString("user_placeofposting"));
                map.put("add_state", rs.getString("user_postedstate"));
                map.put("hod_name", rs.getString("rep_name"));
                map.put("hod_desig", rs.getString("rep_design"));
                map.put("hod_mobile", rs.getString("rep_mobile"));
                map.put("hod_email", rs.getString("rep_email"));
                map.put("hod_telephone", rs.getString("rep_telephone"));
                map.put("employment", "Central");
                map.put("ministry", "Electronics and Information Technology");
                map.put("department", "National Informatics Centre");
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

    public Map<String, String> fetchUserProfileFromOAD(String uid) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();
        //System.out.println("MAP111 = " + map);
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select * from " + Constants.RO_TABLE + " where user_uid = ?");
            ps.setString(1, uid);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchUserProfileFromOAD: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("email", rs.getString("user_mail"));
                map.put("mobile", rs.getString("user_mobile"));
                map.put("name", rs.getString("user_title") + rs.getString("user_name"));
                map.put("ophone", rs.getString("user_telephone"));
                map.put("designation", rs.getString("user_design"));
                map.put("emp_code", rs.getString("user_empcode"));
                map.put("address", rs.getString("user_address"));
                map.put("dob", rs.getString("user_dob"));
                map.put("city", rs.getString("user_placeofposting"));
                map.put("add_state", rs.getString("user_postedstate"));
                map.put("hod_name", rs.getString("rep_name"));
                map.put("hod_desig", rs.getString("rep_design"));
                map.put("hod_mobile", rs.getString("rep_mobile"));
                map.put("hod_email", rs.getString("rep_mail"));
                map.put("hod_telephone", rs.getString("rep_telephone"));
                map.put("employment", "Central");
                map.put("ministry", "Electronics and Information Technology");
                map.put("department", "National Informatics Centre");
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
        System.out.println("MAP222 = " + map);
        return map;
    }

    @Override
    public List<Map<String, String>> select(String table, HashMap<String, String> hm) {

        System.out.println("Inside Select");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<String> set = new TreeSet<>();
        List<Map<String, String>> resultSet = new ArrayList<>();
        String table_columns = "";
        int nmcount = 0;
        if (hm != null) {
            nmcount = hm.size();
        }
        Connection con1 = null;
        try {
//   As per Ashwini sir this method is not used anywhere so it is not modified for slave DB
            con1 = DbConnection.getConnectionForSchema();
            ps = con1.prepareStatement("select COLUMN_NAME from information_schema.columns where TABLE_SCHEMA= ? and TABLE_NAME = ? order by ORDINAL_POSITION");
            ps.setString(1, dbname);
            ps.setString(2, table);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for select: " + ps);
            //System.out.println("Query : " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                set.add(rs.getString("COLUMN_NAME"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] column_name = set.toArray(new String[set.size()]);
        if (nmcount != 0) {
            String key = "";
            String value = "";
            int i = 0;
            for (Map.Entry<String, String> entry : hm.entrySet()) {
                key = (String) entry.getKey();
                table_columns += key;
                value = (String) entry.getValue();
                table_columns += " = '" + value + "'";
                if (i < (nmcount - 1)) {
                    table_columns += "and";
                }
                i++;
            }
            //Connection con = null;
            try {
                conSlave = DbConnection.getSlaveConnection();
//                con = DbConnection.getConnection();
                ps = conSlave.prepareStatement("select * from " + table + " where " + table_columns);
                rs = ps.executeQuery();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for select: " + ps);
                //System.out.println("Query : " + ps);
                while (rs.next()) {
                    for (int j = 0; j < column_name.length; j++) {
                        hm.put(column_name[j], rs.getString(column_name[j]));
                    }
                    resultSet.add(hm);
                    hm.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
                    if (con1 != null) {
                        con1.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            //Connection con = null;
            try {
                ps = conSlave.prepareStatement("select * from " + table);
//                ps = con.prepareStatement("select * from " + table);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for select: " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    for (int j = 0; j < column_name.length; j++) {
                        hm.put(column_name[j], rs.getString(column_name[j]));
                    }
                    resultSet.add(hm);
                    hm.clear();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
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
                    if (con1 != null) {
                        con1.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        for (Map<String, String> temp : resultSet) {
            System.out.println("Inside db");
            for (Map.Entry<String, String> entry : temp.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("KEY : " + key + " And VALUE : " + value);
            }
        }
        System.out.println("Coming out of select");
        return resultSet;
    }

    @Override
    public List<Map<String, String>> select(String tableName) {
        return select(tableName, null);
    }

    @Override
    public boolean insert(String table, HashMap<String, String> hm) {
        boolean flag = false;
        String table_columns = "";
        PreparedStatement ps = null;
        int nmcount = hm.size();
        if (nmcount != 0) {
            Set set = hm.entrySet();
            Iterator itr = set.iterator();
            for (int i = 0; i < nmcount; i++) {
                if (itr.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr.next();
                    String key = (String) pair.getKey();
                    table_columns += key;
                    String value = (String) pair.getValue();
                    table_columns += " = '" + value + "'";
                    if (i < (nmcount - 1)) {
                        table_columns += ",";
                    }
                }
            }
            //Connection con = null;
            try {
                ps = con.prepareStatement("insert into '" + table + "' set " + table_columns);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insert: " + ps);
                int j = ps.executeUpdate();
                if (j == 1) {
                    flag = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (con != null) {
                        // con.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    @Override
    public boolean update(String table, HashMap<String, String> hm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(String table, HashMap<String, String> hm) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    boolean isEmailOtpActive(String email) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        PreparedStatement ps = null;
        ResultSet rset = null;
        boolean flag = false;
        //System.out.println("inside isEmailOtpActive :::::::");
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select * from (select datetime,now() - interval 30 minute as past from " + Constants.OTP_TABLE + " where email=? and otp_email !=0 order by datetime desc limit 1) as mytimetable where datetime > past");
            ps.setString(1, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for isEmailOtpActive: " + ps);
            rset = ps.executeQuery();
            if (rset.next()) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rset != null) {
                    rset.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    boolean isMobileOtpActive(String mobile, String countryCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        PreparedStatement ps = null;
        ResultSet rset = null;
        //Connection con = null;
        boolean flag = false;
        // System.out.println("inside isMobileOtpActive :::::::");
        if (!mobile.contains("+")) {
            mobile = countryCode + mobile;
        }
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("select * from (select datetime,now() - interval 30 minute as past from " + Constants.OTP_TABLE + " where mobile=? and otp_mobile !=0 order by datetime desc limit 1) as mytimetable where datetime > past");
            ps.setString(1, mobile);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for isMobileOtpActive: " + ps);
            rset = ps.executeQuery();
            if (rset.next()) {
                flag = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rset != null) {
                    rset.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    String verifyOTP(String email, String mobile, String email_otp, String mobile_otp, boolean isEmailOtpActive, boolean isMobileOtpActive) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        PreparedStatement ps = null;
        ResultSet rset = null;
        String flag = "";
        //System.out.println("inside otp verify:::::::" + mobile);
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            if ((mobile_otp != null || !mobile_otp.equals("")) && (email_otp == null || email_otp.equals(""))) { // When mobile OTP is not null while email is NULL
                ps = conSlave.prepareStatement("SELECT otp_mobile FROM otp_save where mobile =? and otp_mobile=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                ps.setString(1, mobile);
                ps.setString(2, mobile_otp);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP1: " + ps);
                rset = ps.executeQuery();
                if (rset.next()) {
                    flag = "mobileOnly";
                } else {
                    flag = UserBlock(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);
                }
            } else if ((mobile_otp == null || mobile_otp.equals("")) && (email_otp != null || !email_otp.equals(""))) { // When mobile OTP is null while email is not NULL
                ps = conSlave.prepareStatement("SELECT otp_email FROM otp_save where email = ? and otp_email=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                ps.setString(1, email);
                ps.setString(2, email_otp);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP2: " + ps);
                rset = ps.executeQuery();
                if (rset.next()) {
                    flag = "emailOnly";
                } else {
                    flag = UserBlock(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);
                }
            } else { // When mobile OTP is not null and email is not NULL
                System.out.println("isEmailOtpActive : " + isEmailOtpActive + "isMobileOtpActive" + isMobileOtpActive);
                if (!isEmailOtpActive && !isMobileOtpActive) {
                    ps = conSlave.prepareStatement("SELECT otp_mobile FROM otp_save where mobile =? and email = ? and otp_email=? and otp_mobile=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                    ps.setString(1, mobile);
                    ps.setString(2, email);
                    ps.setString(3, email_otp);
                    ps.setString(4, mobile_otp);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP3: " + ps);
                    rset = ps.executeQuery();
                    if (rset.next()) {
                        flag = "both";
                    } else {
                        flag = UserBlock(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);
                    }
                } else if (!isEmailOtpActive) {
                    ps = conSlave.prepareStatement("SELECT otp_email FROM otp_save where email = ? and otp_email=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                    ps.setString(1, email);
                    ps.setString(2, email_otp);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP2: " + ps);
                    rset = ps.executeQuery();
                    if (rset.next()) {
                        ps = conSlave.prepareStatement("SELECT otp_mobile FROM otp_save where mobile = ? and otp_mobile=? and otp_mobile != 0 and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                        ps.setString(1, mobile);
                        ps.setString(2, mobile_otp);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP2: " + ps);
                        rset = ps.executeQuery();
                        if (rset.next()) {
                            flag = "both";
                        }
                    } else {
                        flag = UserBlock(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);
                    }
                } else if (!isMobileOtpActive) {
                    ps = conSlave.prepareStatement("SELECT otp_mobile FROM otp_save where mobile =? and otp_mobile=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                    ps.setString(1, mobile);
                    ps.setString(2, mobile_otp);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP1: " + ps);
                    rset = ps.executeQuery();
                    if (rset.next()) {
                        ps = conSlave.prepareStatement("SELECT otp_email FROM otp_save where email = ? and otp_email=? and otp_email != 0 and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                        ps.setString(1, email);
                        ps.setString(2, email_otp);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP2: " + ps);
                        rset = ps.executeQuery();
                        if (rset.next()) {
                            flag = "both";
                        } else {
                            flag = UserBlock(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);
                        }
                    }
                } else {
                    ps = conSlave.prepareStatement("SELECT otp_email FROM otp_save where email = ? and otp_email=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                    ps.setString(1, email);
                    ps.setString(2, email_otp);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP2: " + ps);
                    rset = ps.executeQuery();
                    if (rset.next()) {
                        ps = conSlave.prepareStatement("SELECT otp_mobile FROM otp_save where mobile = ? and otp_mobile=? and otp_mobile != 0 and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                        ps.setString(1, mobile);
                        ps.setString(2, mobile_otp);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP2: " + ps);
                        rset = ps.executeQuery();
                        if (rset.next()) {
                            flag = "both";
                        }
                    } else {
                        flag = UserBlock(email, mobile, email_otp, mobile_otp, isEmailOtpActive, isMobileOtpActive);
                    }
                }
                if (!flag.isEmpty()) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rset != null) {
                    rset.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // System.out.println("flag:::::::::::" + flag);
        return flag;
    }

    public String UserBlock(String email, String mobile, String email_otp, String mobile_otp, boolean isEmailOtpActive, boolean isMobileOtpActive) {
        String flag = "";
        try {
            PreparedStatement ps = null;
            ResultSet rset = null;
            ps = conSlave.prepareStatement("SELECT otp_mobile,attempt_login FROM otp_save where mobile =? and email=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and attempt_login <= 8");
            ps.setString(1, mobile);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for verifyOTP1: " + ps);
            rset = ps.executeQuery();
            if (rset.next()) {
                int i = rset.getInt("attempt_login");
                i = i + 1;
                ps = null;
                String update = "update otp_save set attempt_login=? where mobile =? and email=? and active = 1 and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and attempt_login < 8";
                ps = con.prepareStatement(update);
                ps.setInt(1, i);
                ps.setString(2, mobile);
                ps.setString(3, email);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendEmail2: " + ps);
                ps.executeUpdate();
                if (rset.getInt("attempt_login") >= 8) {
                    flag = "block";
                }
            }
        } catch (Exception e) {

        }

        return flag;

    }

    //edit by sanjeev
    public String resendOtpForLogin(String email, String mobile) {
        //System.out.println("resend mobile in db dao");
        String otp = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            String ip = ServletActionContext.getRequest().getRemoteAddr();
            Connection con = null;
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String sql = "SELECT otp_mobile,resend_otp FROM otp_save where mobile =? and email = ? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and resend_otp <2 and active = 1";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile1: " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                otp = Integer.toString(rs.getInt("otp_mobile"));
                int i = rs.getInt("resend_otp");
                i = i + 1;
                ps = null;
                String update = "update otp_save set resend_otp=? where mobile =? and email = ? and otp_mobile=?";
                ps = con.prepareStatement(update);
                ps.setInt(1, i);
                ps.setString(2, mobile);
                ps.setString(3, email);
                ps.setInt(4, Integer.parseInt(otp));
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile2: " + ps);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return otp;
    }

    public String resendMobile(String email, String mobile) {
        //System.out.println("resend mobile in db dao");
        String otp = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            String ip = ServletActionContext.getRequest().getRemoteAddr();
            Connection con = null;
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String sql = "SELECT newcode,resend_otp FROM otp_save_update_mobile where mobile =? and email = ? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and resend_otp <2 and active = 1";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile1: " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                otp = Integer.toString(rs.getInt("newcode"));
                int i = rs.getInt("resend_otp");
                i = i + 1;
                ps = null;
                String update = "update otp_save set resend_otp=? where mobile =? and email = ? and newcode=?";
                ps = con.prepareStatement(update);
                ps.setInt(1, i);
                ps.setString(2, mobile);
                ps.setString(3, email);
                ps.setInt(4, Integer.parseInt(otp));
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile2: " + ps);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return otp;
    }

    public String resendEmail(String mobile, String email) {
        String otp = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            String ip = ServletActionContext.getRequest().getRemoteAddr();
            Connection con = null;
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            String sql = "SELECT otp_email,resend_otp_email FROM otp_save where mobile =? and email = ? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and resend_otp_email <3 and active = 1";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendEmail1: " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                otp = Integer.toString(rs.getInt("otp_email"));
                int i = rs.getInt("resend_otp_email");
                i = i + 1;
                ps = null;
                String update = "update otp_save set resend_otp_email=? where mobile =? and email = ? and otp_email=?";
                ps = con.prepareStatement(update);
                ps.setInt(1, i);
                ps.setString(2, mobile);
                ps.setString(3, email);
                ps.setInt(4, Integer.parseInt(otp));
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendEmail2: " + ps);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return otp;
    }

    public String fetchOTPOnNewMobile(String email, String mobile) {
        //System.out.println("resend mobile in db dao");
        String otp = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            String ip = ServletActionContext.getRequest().getRemoteAddr();
            Connection con = null;
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String sql = "SELECT new,resend_otp FROM otp_save where mobile =? and email = ? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and resend_otp <2 and active = 1";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile1: " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                otp = Integer.toString(rs.getInt("otp_mobile"));
                int i = rs.getInt("resend_otp");
                i = i + 1;
                ps = null;
                String update = "update otp_save set resend_otp=? where mobile =? and email = ? and otp_mobile=?";
                ps = con.prepareStatement(update);
                ps.setInt(1, i);
                ps.setString(2, mobile);
                ps.setString(3, email);
                ps.setInt(4, Integer.parseInt(otp));
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile2: " + ps);
                ps.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return otp;
    }

    public String checkSupportEmail(String mobile) {
        String returnString = "";
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            String sql1 = "select email_address from support_individual_login where mobile  =?";
            ps1 = conSlave.prepareStatement(sql1);
            ps1.setString(1, mobile);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for checkSupportEmail1: " + ps1);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                // returnString = "valid";
                String sql2 = "select ip from support_ip where ip =?";
                ps2 = conSlave.prepareStatement(sql2);
                ps2.setString(1, ip);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for checkSupportEmail2: " + ps2);
                rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    returnString = rs1.getString("email_address");
                } else {
                    returnString = "invalid";
                }
            } else {
                returnString = "invalid";
            }
        } catch (Exception e) {
        } finally {
            try {
                if (ps1 != null) {
                    ps1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (ps2 != null) {
                    ps2.close();
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
        return returnString;
    }

    public String checkAdminEmail(String mobile) {
        String returnString = "";
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            String sql1 = "select email_address from support_individual_login where mobile =? and individual_ip = ?";
            ps1 = conSlave.prepareStatement(sql1);
            ps1.setString(1, mobile);
            ps1.setString(2, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for checkSupportEmail2: " + ps1);
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                // System.out.println("rs1.getString(\"email_address\")" + rs1.getString("email_address"));
                returnString = rs1.getString("email_address");
                //System.out.println("in doa return string is:::::" + returnString);
            } else {
                returnString = "invalid";
                //System.out.println("in dao else return string is:::::" + returnString);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (ps1 != null) {
                    ps1.close();
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
        return returnString;
    }

    public void insertLoginDetails(String email, String role, String login_status) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            String update = "insert into login_details(email,ip,role,remarks,login_time,sessionId) values (?,?,?,?,now(),?)";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(update);
            ps.setString(1, email);
            ps.setString(2, ip);
            ps.setString(3, role);
            ps.setString(4, login_status);
            ps.setString(5, ServletActionContext.getRequest().getSession().getId());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertLoginDetails: " + ps);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertIntoDigitalTable(String email, String role) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            String update = "insert into digital_login_details(email,ip,role,remarks,login_time) values (?,?,?,?,now())";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(update);
            ps.setString(1, email);
            ps.setString(2, ip);
            ps.setString(3, role);
            ps.setString(4, "Authentication Sucessfull");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertLoginDetails: " + ps);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLogoutDetails(String email, String logout_status) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            String update = "update login_details set logout_time = now(),logout_status=? where email = ? order by login_time desc limit 1";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(update);
            ps.setString(1, logout_status);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for updateLogoutDetails: " + ps);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    public boolean checkMailAdminFormsAllowed(String email_to_check) {
//        PreparedStatement ps = null;
//        boolean b = true;
//        
//        try {
//            conSlave = DbConnection.getSlaveConnection();
//            String checkAdminAllowed = "select m_email from mailadmin_forms where m_single= 'n' and  m_bulk= 'n' and  m_nkn = 'n' and  m_relay = 'n' and  m_ldap= 'n' and  m_dlist = 'n' and  m_sms = 'n' and  m_ip= 'n' and  m_imappop = 'n' and  m_gem = 'n' and  m_mobile= 'n' and  m_dns = 'n' and  m_wifi= 'n' and  m_vpn = 'n' and m_email = ?";
//            ps = conSlave.prepareStatement(checkAdminAllowed);
//            ps.setString(1, email_to_check);
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for checkAdminAllowed: " + ps);
//            ResultSet rset = ps.executeQuery();
//            while (rset.next()) {
//                b = false;
//            }
//            ps.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (ps != null) {
//                    ps.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return b;
//    }
    // MODIFIED BY AT on 15thapr19

    public boolean checkMailAdminFormsAllowed(String email_to_check) {
        PreparedStatement ps = null;
        boolean b = true;
        boolean result = true;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String checkAdminAllowed = "select m_email from mailadmin_forms where m_email = ?";
            ps = conSlave.prepareStatement(checkAdminAllowed);
            ps.setString(1, email_to_check);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for checkAdminAllowed: " + ps);
            ResultSet rset = ps.executeQuery();
            while (rset.next()) {
                result = false;
                checkAdminAllowed = "select m_email from mailadmin_forms where m_single= 'n' and  m_bulk= 'n' and  m_nkn = 'n' and  m_relay = 'n' and  m_ldap= 'n' and  m_dlist = 'n' and  m_sms = 'n' and  m_ip= 'n' and  m_imappop = 'n' and  m_gem = 'n' and  m_mobile= 'n' and  m_dns = 'n' and  m_wifi= 'n' and  m_vpn = 'n' and m_webcast= 'n' and m_email_act='n' and m_email_deact='n' and m_dor_ext = 'n' and m_email = ?";
                ps = conSlave.prepareStatement(checkAdminAllowed);
                ps.setString(1, email_to_check);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for checkAdminAllowed: " + ps);
                rset = ps.executeQuery();
                while (rset.next()) {
                    b = false;
                    break;
                }
                if (!b) {
                    break;
                }
            }
            if (result && b) {
                b = false;
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    public ArrayList<String> fetchAllowedFormsForAdmins(Set<String> aliases) {
        PreparedStatement ps = null;
        ResultSet res = null;
        Set<String> supForms = new HashSet<>();

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = " SELECT * FROM mailadmin_forms WHERE ";
            if (aliases != null && aliases.size() > 0) {
                int i = 1;
                for (String email : aliases) {
                    if (i == 1) {
                        qry += " m_email = '" + email + "'";
                    } else {
                        qry += " OR  m_email = '" + email + "'";
                    }
                    i++;
                }
            }
            ps = conSlave.prepareStatement(qry);
            //System.out.println(" insie get form names " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                if (res.getString("m_daonboarding").equals("y")) {
                    supForms.add("'" + Constants.DAONBOARDING_FORM_KEYWORD + "'");
                }
                if (res.getString("m_single").equals("y")) {
                    supForms.add("'" + Constants.SINGLE_FORM_KEYWORD + "'");
                }
                if (res.getString("m_bulk").equals("y")) {
                    supForms.add("'" + Constants.BULK_FORM_KEYWORD + "'");
                }
                if (res.getString("m_nkn").equals("y")) {
                    supForms.add("'" + Constants.NKN_SINGLE_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.NKN_BULK_FORM_KEYWORD + "'");
                }
                if (res.getString("m_relay").equals("y")) {
                    supForms.add("'" + Constants.RELAY_FORM_KEYWORD + "'");
                }
                if (res.getString("m_ldap").equals("y")) {
                    supForms.add("'" + Constants.LDAP_FORM_KEYWORD + "'");
                }
                if (res.getString("m_dlist").equals("y")) {
                    supForms.add("'" + Constants.DIST_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.BULKDIST_FORM_KEYWORD + "'");
                }
                if (res.getString("m_sms").equals("y")) {
                    supForms.add("'" + Constants.SMS_FORM_KEYWORD + "'");
                }
                if (res.getString("m_ip").equals("y")) {
                    supForms.add("'" + Constants.IP_FORM_KEYWORD + "'");
                }
                if (res.getString("m_imappop").equals("y")) {
                    supForms.add("'" + Constants.IMAP_FORM_KEYWORD + "'");
                }
                if (res.getString("m_gem").equals("y")) {
                    supForms.add("'" + Constants.GEM_FORM_KEYWORD + "'");
                }
                if (res.getString("m_mobile").equals("y")) {
                    supForms.add("'" + Constants.MOB_FORM_KEYWORD + "'");
                }
                if (res.getString("m_dns").equals("y")) {
                    supForms.add("'" + Constants.DNS_FORM_KEYWORD + "'");
                }
                if (res.getString("m_wifi").equals("y")) {
                    supForms.add("'" + Constants.WIFI_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.WIFI_PORT_FORM_KEYWORD + "'");
                }
                if (res.getString("m_vpn").equals("y")) {
                    supForms.add("'" + Constants.VPN_SINGLE_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.VPN_BULK_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.VPN_ADD_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.VPN_RENEW_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.VPN_SURRENDER_FORM_KEYWORD + "'");
                    supForms.add("'" + Constants.VPN_DELETE_FORM_KEYWORD + "'");
                }
                if (res.getString("m_centralutm").equals("y")) {
                    supForms.add("'" + Constants.CENTRAL_UTM_FORM_KEYWORD + "'");
                }
                if (res.getString("m_webcast").equals("y")) {
                    supForms.add("'" + Constants.WEBCAST_FORM_KEYWORD + "'");
                }
                if (res.getString("m_email_act").equals("y")) {
                    supForms.add("'" + Constants.EMAILACTIVATE_FORM_KEYWORD + "'");
                }
                if (res.getString("m_email_deact").equals("y")) {
                    supForms.add("'" + Constants.EMAILDEACTIVATE_FORM_KEYWORD + "'");
                }
                if (res.getString("m_dor_ext").equals("y")) {
                    supForms.add("'" + Constants.DOR_EXT_FORM_KEYWORD + "'");
                }
                if (res.getString("m_dor_ext_retired").equals("y")) {
                    supForms.add("'" + Constants.DOR_EXT_RT_FORM_KEYWORD + "'");
                }
            }
        } catch (Exception ex) {
            System.out.println(sessionid + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayList<String> uniqueAllowedForms = new ArrayList<>(supForms);
        int i = 0;
//        for (String uniqueAllowedForm : supForms) {
//            // System.out.println("Sup Form " + i++ + uniqueAllowedForm);
//        }

        return uniqueAllowedForms;
    }

    public String fetchIndividualEmail(String mobile) {
        PreparedStatement ps = null;
        ResultSet res = null;

        String email = "";
        System.out.println("start process of fetch Individual Email based on mobile and ip: ");
        try {
            conSlave = DbConnection.getSlaveConnection();

            ps = conSlave.prepareStatement("select email_address from " + Constants.ADMIN_TABLE + " where mobile = ?  and individual_ip=?");
            ps.setString(1, mobile);
            ps.setString(2, ip);

            System.out.println("PS1 : " + ps);

            res = ps.executeQuery();

            while (res.next()) {
                email = res.getString("email_address");
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return email;
    }

    public Map getCaValues(String email) {

        //System.out.println("inise get usr values email:::::::::::" + email);
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;

        try {
            //        con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            String sql = "select * from comp_auth where ca_email=?";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, email);
            System.out.println("query##############" + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                //System.out.println("if rs next");
                profile_values.put("hod_name", rs.getString("ca_name").trim());
                profile_values.put("hod_mobile", rs.getString("ca_mobile").trim());
                profile_values.put("hod_email", rs.getString("ca_email").trim());
                // System.out.println("profile_values############" + profile_values);

            }

        } catch (Exception e) {

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
        // System.out.println("profile values in db dao");
        return profile_values;
    }

    public boolean updateSummaryTable(String role, List<String> listOfCounts, Map<String, Boolean> mapOfCounts, Set<String> aliases) {
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "";
        boolean flag = false;
        try {
            con = DbConnection.getConnection();
            query = "update summary_table set ";

            for (String countName : listOfCounts) {
                if (mapOfCounts.get(countName)) {
                    query += countName + " = " + countName + " + 1 , ";
                } else {
                    query += countName + " = " + countName + " - 1 , ";
                }
            }

            query = query.replaceAll("\\s*,\\s*$", "");

            query += " where role = '" + role + "' and ";

            for (String emailAddress : aliases) {
                query += " email = '" + emailAddress + "' or ";
            }

            query = query.replaceAll("\\s*or\\s*$", "");

            System.out.println("QUERY for Updating Summary Table : " + query);

            ps = con.prepareStatement(query);
            if (ps.executeUpdate() == 1) {
                flag = true;
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public boolean fetchAuditTable(String email_to_check) {
        PreparedStatement ps = null;
        boolean b = true;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String checkAdminAllowed = "select * from login_details where email= ? and sessionId=?";
            ps = conSlave.prepareStatement(checkAdminAllowed);
            ps.setString(1, email_to_check);
            ps.setString(2, ServletActionContext.getRequest().getSession().getId());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchAuditTable: " + ps);
            ResultSet rset = ps.executeQuery();
            while (rset.next()) {
                b = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    // function modified by pr on 12thsep19
//    public boolean insertIntoEmpCoordForVPN(String coordinator) {
//
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        boolean isSet = false;
//
//        // start, code added by pr on 12thsep19
//        boolean toInsert = true;
//
//        // first get the aliases for this coordinator email
//        Ldap ld = new Ldap();
//
//        Set<String> aliases = ld.getAliases(coordinator);
//
//        // end, code added by pr on 12thsep19
//        try {
//            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
//
//            // start, code added by pr on 12thsep19
//            // then check for each alias in employment coordinator table
//            String qryS = "select count(*) from " + Constants.COORDINATOR_TABLE;
//
//            int k = 1;
//
//            for (String s : aliases) {
//                if (k == 1) {
//                    qryS += " WHERE ( emp_coord_email = ? ";
//                } else {
//                    qryS += " OR emp_coord_email = ? ";
//                }
//
//                k++;
//            }
//
//            qryS += ") ";
//
//            ps = conSlave.prepareStatement(qryS);
//
//            for (int i = 1; i <= aliases.size(); i++) {
//                ps.setString(i, coordinator);
//            }
//
//            // System.out.println(" inside insertIntoEmpCoordForVPN search query : " + ps);
//            rs = ps.executeQuery();
//
//            while (rs.next()) {
//                int rowCount = rs.getInt("count(*)");
//                if (rowCount > 0) {
//                    toInsert = false;
//                }
//            }
//            // System.out.println("toInsert:::::::::" + toInsert);
//            if (toInsert) {
//                // end, code added by pr on 12thsep19
//                System.out.println("if toInsert true");
//                con = DbConnection.getConnection();
//                String qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_dept = ?, emp_coord_email = ?, emp_admin_email = ? , emp_status = ?";
//                ps = con.prepareStatement(qry);
//                ps.setString(1, "Central");
//                ps.setString(2, "Electronics and Information Technology");
//                ps.setString(3, "National Informatics Centre");
//                ps.setString(4, coordinator);
//                ps.setString(5, "support@gov.in");
//                ps.setString(6, "i");
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside  insertIntoEmpCoordForVPN " + ps);
//                int num = ps.executeUpdate();
//                if (num > 0) {
//                    isSet = true;
//                }
//
//            } // if added by pr on 12thsep19
//
//        } catch (Exception ex) {
//            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return isSet;
//    }
    // function updated by pr on 6thjan2020
    public boolean insertIntoEmpCoordForVPN(String coordinator) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isSet = false;

        // start, code added by pr on 12thsep19
        boolean toInsert = true;

        // first get the aliases for this coordinator email
        Ldap ld = new Ldap();

        Set<String> aliases = ld.fetchAliases(coordinator);

        // end, code added by pr on 12thsep19
        try {
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();

            // start, code added by pr on 12thsep19
            // then check for each alias in employment coordinator table
            String qryS = "select count(*) from " + Constants.VPN_COORDINATOR_TABLE; // COORDINATOR_TABLE replaced with VPN_COORDINATOR_TABLE  by pr on 6thjan2020

            int k = 1;

            for (String s : aliases) {
                if (k == 1) {
                    qryS += " WHERE ( emp_coord_email = ? ";
                } else {
                    qryS += " OR emp_coord_email = ? ";
                }

                k++;
            }

            qryS += ") ";

            ps = conSlave.prepareStatement(qryS);

            for (int i = 1; i <= aliases.size(); i++) {
                ps.setString(i, coordinator);
            }

            // System.out.println(" inside insertIntoEmpCoordForVPN search query : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside insertIntoEmpCoordForVPN search query : " + ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                int rowCount = rs.getInt("count(*)");
                if (rowCount > 0) {
                    toInsert = false;
                }
            }
            // System.out.println("toInsert:::::::::" + toInsert);
            if (toInsert) {
                // end, code added by pr on 12thsep19
                System.out.println("if toInsert true");
                con = DbConnection.getConnection();

                // table to insert into updated by pr on 6thjan2020, and columns updated too
                String qry = "INSERT INTO " + Constants.VPN_COORDINATOR_TABLE + " SET emp_category = ?,  emp_min_state_org = ?, emp_dept = ?, emp_coord_email = ?, emp_status = ?";
                ps = con.prepareStatement(qry);
                ps.setString(1, "Central");
                ps.setString(2, "Electronics and Information Technology");
                ps.setString(3, "National Informatics Centre");
                ps.setString(4, coordinator);
                ps.setString(5, "i");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside  insertIntoEmpCoordForVPN " + ps);
                int num = ps.executeUpdate();
                if (num > 0) {
                    isSet = true;
                }

            } // if added by pr on 12thsep19

        } catch (Exception ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isSet;
    }

    public ArrayList<String> empCoordData(HashMap empDetails) {
        //System.out.println(" inside fetchEmploymentCoords function ");
        ArrayList<String> empCoords = new ArrayList<String>();
        String employment = "", ministry = "", department = "", organization = "", other_dept = "", state = "";
        String form_name = "", add_state = ""; // line added by pr on 26thmar18
        if (empDetails != null && empDetails.get("employment") != null) {
            employment = empDetails.get("employment").toString();
            if (empDetails.get("ministry") != null) {
                ministry = empDetails.get("ministry").toString();
            }
            if (empDetails.get("department") != null) {
                department = empDetails.get("department").toString();
            }
            if (empDetails.get("state") != null) {
                state = empDetails.get("state").toString();
            }
            if (empDetails.get("organization") != null) {
                organization = empDetails.get("organization").toString();
            }
            if (empDetails.get("other_dept") != null) {
                other_dept = empDetails.get("other_dept").toString();
            }
            // below if added by pr on 26thmar18
            if (empDetails.get("add_state") != null && !empDetails.get("add_state").equals("")) {
                add_state = empDetails.get("add_state").toString();
            }
        }
        // if added by pr on 26thmar18
//        if (empDetails.get("form_name") != null && !empDetails.get("form_name").equals("")) {
//            form_name = empDetails.get("form_name").toString();
//        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        if (employment.equalsIgnoreCase("central")) {
            // get ministry and department and other_dept and serach in ministry and ministry_department tables for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // check if this da already exists corresponding to the ministry department
                /*String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                 + " AND emp_dept = ? AND ( emp_coord_email !=  emp_admin_email  ) ";*/
                String qry = " select emp_coord_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ?  ";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, ministry);
                ps.setString(3, department);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " central getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function minstry depatrment " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 51 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 52 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 2 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 53 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 3 " + e.getMessage());

                    }
                }
            }
        } else if (employment.equalsIgnoreCase("state")) {
            Boolean isOther = false; // line added by pr on 23rdmar18
            // get department and other_dept and serach in state_department table for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
                isOther = true; // line added by pr on 23rdmar18                
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ?";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, state);
                ps.setString(3, department);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " state getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                if (isOther) // if added by pr on 23rdmar18
                {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                            + " AND emp_dept = 'other' ";
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, state);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
                if (form_name.equals("sms")) {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                            + " AND emp_dept = ? ";
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, state);
                    ps.setString(3, department);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " state getcoords query is " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function state dept " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 54 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 4 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 55 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 5 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 56 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 6 " + e.getMessage());

                    }
                }
            }
        } else if (employment.equalsIgnoreCase("others")) {
            // get organization and other_dept and serach in organization table for coordinator
            // if organization contains other then take other_dept as department
            if (organization.equalsIgnoreCase("other") && !other_dept.equals("")) {
                organization = other_dept;
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? ";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
                if (form_name.equals("sms")) {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? ";
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, organization);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function organization  3" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 57 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 6 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 58 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 7 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 59 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 8 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("Psu")) // else if added by pr on 22ndfeb18
        {
            // get organization and other_dept and serach in organization table for coordinator
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? ";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " Psu getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
                if (form_name.equals("sms")) {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? ";
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, organization);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function psu_department " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 60 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 9 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 61 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 10 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 62 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 11 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("Const")) // else if added by pr on 22ndfeb18
        {
            // get organization and other_dept and serach in organization table for coordinator
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? ";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " Const getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
                if (form_name.equals("sms")) {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? ";
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, organization);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // end, code added by pr on 26thmar18
                // below lines added by pr on 6thmar18
                ps.close();
                rs.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function constitutional_department " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                   
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 63 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 12 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 64 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 13 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                   
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 65 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 14 " + e.getMessage());
                    }
                }
            }
        }
        return empCoords;
    }

    public String fetchRo(Set<String> email) {
        String ro = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            String sql = "select rep_mail from " + Constants.RO_TABLE + " where ";
            if (email != null && email.size() > 0) {
                int i = 1;
                for (String email1 : email) {
                    if (i == 1) {
                        sql += " user_mail = '" + email1 + "'";
                    } else {
                        sql += " OR  user_mail = '" + email1 + "'";
                    }
                    i++;
                }
            }

            ps = conSlave.prepareStatement(sql);
            System.out.println("query##############" + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                ro = rs.getString("rep_mail");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        //System.out.println("profile values in db dao");
        return ro;
    }

    public Set<String> fetchCoIds(List<String> aliases) {
        PreparedStatement ps = null;
        ResultSet res = null;
        Set<String> ids = new HashSet<>();
        String query = "select id from coordinator_ids where";
        try {
            conSlave = DbConnection.getSlaveConnection();

            for (String emailAddress : aliases) {
                query += " email = '" + emailAddress + "' or ";
            }

            query = query.replaceAll("\\s*or\\s*$", "");

            ps = conSlave.prepareStatement(query);
            //System.out.println("PS1 : " + ps);
            res = ps.executeQuery();
            String id = "";
            while (res.next()) {
                id = res.getString("id");
                ids.add(id);
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ids;
    }

    public Set<String> findIntersection(List<Set<String>> collections) {
        boolean first = true;
        Set<String> commonIds = new HashSet<>();
        for (Set<String> commonSet : collections) {
            if (first) {
                commonIds.addAll(commonSet);
                first = false;
            } else {
                commonIds.retainAll(commonSet);
            }
        }
        return commonIds;
    }

    public String insertIntoCoordsIdTable(String to_email) {
        boolean flag = false;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet res = null;
        Set<String> ids = new HashSet<>();
        String query = "";
        String[] emailAddresses = to_email.split(",");
        String id = "";

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            query = "select (max(id)+1) as newid from coordinator_ids";
            ps = conSlave.prepareStatement(query);
            System.out.println("PS1 : " + ps);
            res = ps.executeQuery();

//            System.out.println("RES>NEXT =" + res.next() + res);
            if (res.next()) {
                // System.out.println("INSIDE if");

                if (res.getString("newid") != null) {
                    id = res.getString("newid");
                } else {
                    id = "1";
                }

                for (String emailAdrs : emailAddresses) {
                    query = "insert into coordinator_ids(id,email) value(?,?)";
                    ps1 = con.prepareStatement(query);
                    ps1.setString(1, id);
                    ps1.setString(2, emailAdrs);
                    ps1.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    public boolean updateFinalAuditTrack(String registration_no, String co_id) {
        boolean flag = false;
        PreparedStatement ps = null;
        ResultSet res = null;

        String query = "";

        try {
            con = DbConnection.getConnection();
            query = "update final_audit_track set co_id =? where registration_no = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, co_id);
            ps.setString(2, registration_no);
            int i = ps.executeUpdate();

            if (i > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside update final audit track " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public boolean updateFinalAuditTrackForDa(String registration_no, String da_id) {
        boolean flag = false;
        PreparedStatement ps = null;
        ResultSet res = null;

        String query = "";

        try {
            con = DbConnection.getConnection();
            query = "update final_audit_track set da_id =? where registration_no = ?";
            ps = con.prepareStatement(query);
            ps.setString(1, da_id);
            ps.setString(2, registration_no);
            int i = ps.executeUpdate();

            if (i > 0) {
                flag = true;
            }

        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside update final audit track " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public boolean isRequestClosed(String regNumber) {
        if (regNumber == null) {
            return false;
        }

        if (regNumber.isEmpty()) {
            return false;
        }
        boolean flag = false;
        PreparedStatement ps = null;
        ResultSet res = null;
        String query = "";
        String status = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            query = "select status from final_audit_track where registration_no = ?";
            ps = conSlave.prepareStatement(query);
            ps.setString(1, regNumber);
            res = ps.executeQuery();
            if (res.next()) {
                status = res.getString("status");
                if ((status.equalsIgnoreCase("completed") || status.contains("cancel") || status.contains("reject")) && (!status.contains("pending"))) {
                    flag = true;
                }
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside update final audit track " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    public ArrayList<String> setAllowedRegistrationNumbersBackup(String aliasesInString) {
        ArrayList<String> allowedRegNumbers = new ArrayList<>();

        //where FIND_IN_SET (stat_forwarded_to_user,'prakash@nic.in')
        String[] arr = aliasesInString.split(",");
        StringBuffer cond = new StringBuffer();
        for (String str : arr) {
            if (!cond.toString().isEmpty()) {
                cond.append(" OR ");
            }
            cond.append("FIND_IN_SET(" + str + ", STAT_FORWARDED_TO_USER)");
            cond.append(" OR FIND_IN_SET(" + str + ", STAT_FORWARDED_BY_EMAIL)");
        }
        String qry = "SELECT stat_reg_no FROM status where " + cond.toString();
        System.out.println(qry);

        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps = conSlave.prepareStatement(qry);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                allowedRegNumbers.add(rs.getString("stat_reg_no"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allowedRegNumbers;
    }

    public ArrayList<String> setAllowedRegistrationNumbers(String aliasesInString, List<String> roles, ArrayList<String> formsAllowed, Set<String> aliases) {
        ArrayList<String> allowedRegNumbers = new ArrayList<>();
        String qry = "";
        String finalFormNames = "";
        for (String formName : formsAllowed) {
            finalFormNames += formName + ",";
        }
        System.out.println("Roles :::::" + roles.toString());
        finalFormNames = finalFormNames.replaceAll(",$", "").trim();
        //roles = Arrays.asList("user","ca");
        if (roles.contains("sup") || roles.contains("admin")) {
            System.out.println("Inside sup or admin role ...");
            qry = "SELECT registration_no FROM final_audit_track where form_name in (" + finalFormNames + ")";
            System.out.println("FOR SUPORT OR ADMIN ::: " + qry);
            try {
                conSlave = DbConnection.getSlaveConnection();
                PreparedStatement ps = conSlave.prepareStatement(qry);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    allowedRegNumbers.add(rs.getString("registration_no"));
                }

                for (String aliase : aliases) {
                    qry = "SELECT registration_no FROM final_audit_track where to_email like '%" + aliase + "%'"; //12-04-2022 
                    ps = conSlave.prepareStatement(qry);
                    System.out.println("2ND QUERY FOR SUPPORT AND ADMIN ::: " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        allowedRegNumbers.add(rs.getString("registration_no"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Inside co or ro role ... else block");
            try {
                conSlave = DbConnection.getSlaveConnection();
                qry = "SELECT registration_no FROM final_audit_track where applicant_email in (" + aliasesInString + ") or ca_email in (" + aliasesInString + ")";
                PreparedStatement ps = conSlave.prepareStatement(qry);
                System.out.println("FOR OTHER ROLES :::: " + qry);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    allowedRegNumbers.add(rs.getString("registration_no"));
                }

                if (roles.contains("email_co") || roles.contains("vpn_co")) {
                    for (String aliase : aliases) {
                        qry = "SELECT registration_no FROM final_audit_track where coordinator_email like '%" + aliase + "%'"; //12-04-2022 
                        ps = conSlave.prepareStatement(qry);
                        System.out.println("2ND QUERY FOR CO ROLES ::: " + qry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            allowedRegNumbers.add(rs.getString("registration_no"));
                        }
                    }
                }

                for (String aliase : aliases) {
                    qry = "SELECT registration_no FROM final_audit_track where to_email like '%" + aliase + "%'"; //12-04-2022 
                    ps = conSlave.prepareStatement(qry);
                    System.out.println("2ND QUERY FOR OTHER ROLES :::: " + qry);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        allowedRegNumbers.add(rs.getString("registration_no"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return allowedRegNumbers;
    }

    public Boolean isRegistrationNumberAllowed(String aliasesInString, List<String> roles, ArrayList<String> formsAllowed, Set<String> aliases, String regNumber) {
        String qry = "";
        boolean flag = false;
        String finalFormNames = "";
        for (String formName : formsAllowed) {
            finalFormNames += formName + ",";
        }
        System.out.println("Roles :::::" + roles.toString());
        finalFormNames = finalFormNames.replaceAll(",$", "").trim();

        //roles = Arrays.asList("user","ca");
        if (roles.contains("sup") || roles.contains("admin")) {
            System.out.println("Inside sup or admin role ...");
            qry = "SELECT registration_no FROM final_audit_track where registration_no=? and form_name in (" + finalFormNames + ")";

            try {
                conSlave = DbConnection.getSlaveConnection();
                PreparedStatement ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNumber);
                System.out.println("FOR SUPORT OR ADMIN ::: " + ps);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    flag = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!flag && !roles.contains("sup") && !roles.contains("admin")) {
            System.out.println("Inside co or ro role ... else block");
            try {
                conSlave = DbConnection.getSlaveConnection();
                qry = "SELECT registration_no FROM final_audit_track where registration_no=? and (to_email in (" + aliasesInString + ") or applicant_email in (" + aliasesInString + ") or ca_email in (" + aliasesInString + ") or coordinator_email in (" + aliasesInString + ") or support_email in (" + aliasesInString + ") or admin_email in (" + aliasesInString + ") or da_email in (" + aliasesInString + ") or us_email in (" + aliasesInString + "))";
                PreparedStatement ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNumber);
                System.out.println("FOR OTHER ROLES :::: " + ps);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    flag = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!flag) {
            try {
                qry = "SELECT to_email FROM final_audit_track where registration_no=?";  //31-10-2022
                PreparedStatement ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNumber);
                System.out.println("FINAL CHeck On reg number FOR multiple COs in to_email :::: " + ps);
                ResultSet rs = ps.executeQuery();
                String to_email = "";
                while (rs.next()) {
                    to_email = rs.getString("to_email");

                    for (String aliase : aliases) {
                        if (to_email.contains(aliase)) {
                            flag = true;
                            break;
                        }
                    }
                    if(flag)
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return flag;
    }
    
     public String fetchPunjabNodalOfficers(String district) {
        String co_email = "";
        Set<String> punjabNodalOfficers = new HashSet();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT email FROM punjab_district_nodal_officers WHERE district = ?";
            PreparedStatement ps = conSlave.prepareStatement(qry);
            ps.setString(1, district);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                punjabNodalOfficers.add(rs.getString("email").toLowerCase().trim());
            }

            for (String punjabNodalOfficer : punjabNodalOfficers) {
                co_email += punjabNodalOfficer + ",";
            }

            co_email = co_email.replaceAll(",$", "");
            co_email = co_email.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return co_email;
    }

    public Set<String> fetchBoFromEmpCoord(Map<String, String> profile) {
        Set<String> bos = new HashSet<>();

        try {
            PreparedStatement ps = null;
            ResultSet res = null;
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            if (profile.get("employment").trim().equalsIgnoreCase("central") || profile.get("employment").trim().equalsIgnoreCase("ut")) {
                qry = " select distinct(emp_bo_id) as bo FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND emp_bo_id IS NOT NULL";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, profile.get("employment").trim());
                if (profile.get("ministry") != null) {
                    ps.setString(2, profile.get("ministry").trim());
                } else if (profile.get("min") != null) {
                    ps.setString(2, profile.get("min").trim());
                } else {
                    ps.setString(2, "");
                }
                ps.setString(3, profile.get("department").trim());
            } else if (profile.get("employment").trim().equalsIgnoreCase("state")) {
                qry = " select distinct(emp_bo_id) as bo FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND emp_bo_id IS NOT NULL";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, profile.get("employment").trim());
                ps.setString(2, profile.get("state").trim());
                ps.setString(3, profile.get("department").trim());
            } else {
                qry = " select distinct(emp_bo_id) as bo FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? AND emp_bo_id IS NOT NULL";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, profile.get("employment").trim());
                //ps.setString(2, profile.get("ministry").trim());
                ps.setString(2, profile.get("organization").trim());//line modified by pr on 9thjun2020
            }
            System.out.println("Query to fetch out BO :::: " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                if (res.getString("bo") != null && !res.getString("bo").isEmpty() && !res.getString("bo").trim().equalsIgnoreCase("null")) {
                    bos.add(res.getString("bo").trim().toLowerCase());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Bo in fetchBoFromEmpCoord " + bos);
        return bos;
    }

    public String fetchPunjabDA(String employment, String ministry, String department) {
        String da_email = "";
        try {
            PreparedStatement ps = null;
            ResultSet res = null;

            conSlave = DbConnection.getSlaveConnection();
            String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                    + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email )";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, employment);
            ps.setString(2, ministry);
            ps.setString(3, department);

            res = ps.executeQuery();
            if (res.next()) {
                System.out.println("ir rs next in fetchDa" + ps);

                if (da_email.isEmpty()) {
                    da_email = res.getString("emp_admin_email");
                } else {
                    da_email += "," + res.getString("emp_admin_email");
                }
            } else {

                qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email )";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, ministry);
                ps.setString(3, "Government of Punjab");
                System.out.println("else rs next in fetchDa" + ps);
                res = ps.executeQuery();
                if (res.next()) {
                    if (da_email.isEmpty()) {
                        da_email = res.getString("emp_admin_email");
                    } else {
                        da_email += "," + res.getString("emp_admin_email");
                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return da_email;
    }

    boolean isReservedKeyWord(String uid) {
        boolean flag = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement puid = conSlave.prepareStatement("select uid from uidcheck where uid=?");
            puid.setString(1, uid);
            ResultSet ruid = puid.executeQuery();
            if (ruid.next()) {
                flag = true;
            }
        } catch (Exception e) {
            flag = true;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }
        return flag;
    }

    public Map<String, String> fetchOtpForUpdateMobile(String email, String mobile) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();

        try {
            try {
                conSlave = DbConnection.getSlaveConnection();
//                con = DbConnection.getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = conSlave.prepareStatement("select newcode,attempt_login,registration_no from " + Constants.OTP_UPDATE_MOBILE_TABLE + " where email = ? and new_mobile=? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and attempt_login <= 3");
            ps.setString(1, email);
            ps.setString(2, mobile);

            //System.out.println("Query : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchOtp: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("newcode", rs.getString("newcode"));
                map.put("otp_attempt", rs.getString("attempt_login"));
                map.put("registration_no", rs.getString("registration_no"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public Map<String, String> fetchFailedLoginAttempts(List<String> aliases) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();

        try {
            try {
                conSlave = DbConnection.getSlaveConnection();
//                con = DbConnection.getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = conSlave.prepareStatement("select newcode,attempt_login,registration_no from " + Constants.OTP_UPDATE_MOBILE_TABLE + " where email = ? and new_mobile=? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and attempt_login <= 3");
//            ps.setString(1, email);
//            ps.setString(2, mobile);

            //System.out.println("Query : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchOtp: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("newcode", rs.getString("newcode"));
                map.put("otp_attempt", rs.getString("attempt_login"));
                map.put("registration_no", rs.getString("registration_no"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public Map<String, String> fetchOtpForOnBehalfEmail(String OnBehalfEmail, String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();

        try {
            try {
                conSlave = DbConnection.getSlaveConnection();
//                con = DbConnection.getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = conSlave.prepareStatement("select registration_no,on_behalf_otp,attempt_login_on_behalf from " + Constants.OTP_UPDATE_MOBILE_TABLE + " where on_behalf_email = ? and email=? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and attempt_login_on_behalf <= 3");
            ps.setString(1, OnBehalfEmail);
            ps.setString(2, email);
            //System.out.println("Query : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchOtp: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("on_behalf_otp", rs.getString("on_behalf_otp"));
                map.put("attempt_login_on_behalf", rs.getString("attempt_login_on_behalf"));
                map.put("registration_no", rs.getString("registration_no"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public String insertNewOtpInUpdateMobile(int otp_mobile, String mobile, String country_code, String new_mobile, String email, String existInKavach) {
        Map<String, String> map = fetchOtpForUpdateMobile(email, new_mobile);
        String dbrefno = "", newref = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("KEY " + key + "and value " + value);
        }
        if (map.isEmpty()) {
            PreparedStatement ps = null;

            ResultSet rs = null;
            int newrefno;

            try {
                if (existInKavach.equals("false")) {
                    Date date1 = new Date();
                    DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                    String pdate1 = dateFormat1.format(date1);
                    String search_regnum = "select registration_no from otp_save_update_mobile where date(updated_time)=date(now()) group by registration_no order by registration_no desc limit 1";// order by added by pr on 9thjun2020
                    ps = conSlave.prepareStatement(search_regnum);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        dbrefno = rs.getString("registration_no");
                    }
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

                    newref = "UPDATEMOBILE-FORM" + pdate1 + newref;
                }

                con = DbConnection.getConnection();
                ps = con.prepareStatement("insert into " + Constants.OTP_UPDATE_MOBILE_TABLE + "(newcode,mobile,country_code,new_mobile,email,ip_address,attempt_login,registration_no) VALUES(?,?,?,?,?,?,?,?)");
                ps.setInt(1, otp_mobile);
                ps.setString(2, mobile);
                ps.setString(3, country_code);
                ps.setString(4, new_mobile);
                ps.setString(5, email);
                ps.setString(6, ip);
                ps.setInt(7, 1);
                ps.setString(8, newref);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertOtp: " + ps);
                int k = ps.executeUpdate();
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                return "false";
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
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

    public boolean updateOtpOnBehalf(int otp_mobile, String OnBehalfemail, String email) {
        Map<String, String> map = fetchOtpForOnBehalfEmail(OnBehalfemail, email);
        System.out.println("map.get(\"attempt_login_on_behalf\")" + map.get("attempt_login_on_behalf"));
        //Connection con = null;
        PreparedStatement ps = null;
        int rs;
        try {
            if (map.isEmpty()) {
                con = DbConnection.getConnection();
                ps = con.prepareStatement("update " + Constants.OTP_UPDATE_MOBILE_TABLE + " set attempt_login_on_behalf =?,on_behalf_otp=?,on_behalf_email=? where email =? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW()");
                ps.setInt(1, 1);
                ps.setInt(2, otp_mobile);
                ps.setString(3, OnBehalfemail);
                ps.setString(4, email);
            } else {
                con = DbConnection.getConnection();
                ps = con.prepareStatement("update " + Constants.OTP_UPDATE_MOBILE_TABLE + " set attempt_login_on_behalf =?,on_behalf_otp=? where on_behalf_email =? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and attempt_login_on_behalf <= 2");
                ps.setInt(1, Integer.parseInt(map.get("attempt_login_on_behalf").toString()) + 1);
                ps.setInt(2, Integer.parseInt(map.get("on_behalf_otp")));
                ps.setString(3, OnBehalfemail);
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertOtp: " + ps);
            rs = ps.executeUpdate();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean updateOtpInUpdateMobile(String new_mobile, String email) {
        Map<String, String> map = fetchOtpForUpdateMobile(email, new_mobile);
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("KEY " + key + "and value " + value);
//        }
//        if (map.isEmpty()) {
        PreparedStatement ps = null;
        int rs;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            ps = con.prepareStatement("update " + Constants.OTP_UPDATE_MOBILE_TABLE + " set attempt_login =? where new_mobile =? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and attempt_login <= 2");
            ps.setInt(1, Integer.parseInt(map.get("otp_attempt").toString()) + 1);
            ps.setString(2, new_mobile);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertOtp: " + ps);
            rs = ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // }
        }
        return true;
    }

    public boolean insertMobileTrail(String emailid, String ip, String attemptcounts, String status, String useragent) {
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();

            String sql = "insert into update_mobile_trail(emailid,ip,attemptcounts,status,useragent) values(?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, emailid);
            ps.setString(2, ip);
            ps.setString(3, attemptcounts);
            ps.setString(4, status);
            ps.setString(5, useragent);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("inserted");
            } else {
                System.out.println("not inserted");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public boolean insertMobileTrailAttempts(String emailid, String ip, String attemptcounts, String status, String useragent) {
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();

            String sql = "insert into update_mobile_trail_attempts (emailid,ip,attemptcounts,status,useragent) values(?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, emailid);
            ps.setString(2, ip);
            ps.setString(3, attemptcounts);
            ps.setString(4, status);
            ps.setString(5, useragent);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("inserted");
            } else {
                System.out.println("not inserted");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    //otp insertion
    public boolean insertOtpAttempts(String emailid, String mobile, String otp, String ip, String status) {
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();

            String sql = "insert into otp_save_mobile_attempts(emailid,mobile,otp,status,ip) values(?,?,?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, emailid);
            ps.setString(2, mobile);
            ps.setString(3, otp);
            ps.setString(4, status);
            ps.setString(5, ip);
            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("inserted");
                return true;
            } else {
                System.out.println("not inserted");
                return false;
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean updateMobileTrailAttempts(String emailid, String ip, String attemptcounts, String status) {
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();

            String sql = "update update_mobile_trail_attempts set attemptcounts=?,status=? where emailid=? or ip=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, attemptcounts);
            ps.setString(2, status);

            ps.setString(3, emailid);

            int i = ps.executeUpdate();
            if (i > 0) {
                System.out.println("inserted");
            } else {
                System.out.println("not inserted");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public Map<String, Object> fetchTrailMobileUpdate(String emailid) {
        Map<String, Object> map = new HashMap<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String sql = "select emailid,ip,attemptcounts,status from update_mobile_trail where TIMESTAMPDIFF (MINUTE,createdtimestamp,now()) < 60 and emailid =? order by createdtimestamp desc limit 1";

            try {
                ps = conSlave.prepareStatement(sql);
                ps.setString(1, emailid);

                rs = ps.executeQuery();
                while (rs.next()) {
                    map.put("emailid", rs.getString(1));
                    map.put("ip", rs.getString(2));
                    map.put("attemptcounts", rs.getString(3));
                    map.put("status", rs.getString(4));

                }
            } catch (SQLException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }

//    public String resendMobileForUpdateMobile(String mobile, String new_mobile, String email) {
//        System.out.println("new mobile resendMobileForUpdateMobile in db dao" + new_mobile);
//        String otp = "";
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        //Connection con = null;
//        try {
//            String ip = ServletActionContext.getRequest().getRemoteAddr();
//            Connection con = null;
//            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
//            conSlave = DbConnection.getSlaveConnection();
//            String sql = "SELECT newcode,attempt_login FROM otp_save_update_mobile where mobile =? and new_mobile=? and email = ? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW() and attempt_login <2";
//            ps = conSlave.prepareStatement(sql);
//            ps.setString(1, mobile);
//            ps.setString(2, new_mobile);
//            ps.setString(3, email);
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile1: " + ps);
//            rs = ps.executeQuery();
//            if (rs.next()) {
//                otp = Integer.toString(rs.getInt("newcode"));
//                int i = rs.getInt("attempt_login");
//                i = i + 1;
//                ps = null;
//                String update = "update otp_save_update_mobile set attempt_login=? where mobile =? and email = ? and newcode=?";
//                ps = con.prepareStatement(update);
//                ps.setInt(1, i);
//                ps.setString(2, mobile);
//                ps.setString(3, email);
//                ps.setInt(4, Integer.parseInt(otp));
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile2: " + ps);
//                ps.executeUpdate();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
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
//        return otp;
//    }
    //ADDED FOR SSO
    public Map<String, Object> fetchLoginDetailsSso(String emailid) {
        Map<String, Object> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();

            String sql = "select email,ip,role,remarks,login_time,logout_time,logout_status from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < ? and email =? and logout_time is null;";
            PreparedStatement ps = conSlave.prepareStatement(sql);
            ps.setString(1, pari_timeout);
            ps.setString(2, emailid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put("email", rs.getString(1));
                map.put("ip", rs.getString(2));
                map.put("role", rs.getString(3));
                map.put("remarks", rs.getString(4));
                map.put("login_time", rs.getString(5));
                map.put("logout_time", rs.getString(6));
                map.put("logout_status", rs.getString(7));

            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public String checkEmailForUpdateMobile(String email) {
        String flag = "eligible_for_apply";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "checkEmailForUpdateMobile......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;
            String sql = "select * from ignore_email_update_mobile where email= '" + email + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "checkEmailForUpdateMobile this is the query :::::::::" + sql);
            pst = conSlave.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                flag = "You_Are_NOT_Eligible";
            } else {
                flag = "eligible_for_apply";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }

        return flag;
    }

    public String checkBoForUpdateMobile(String bo) {
        String flag = "eligible_for_apply";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "checkEmailForUpdateMobile......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;
            String sql = "select * from ignore_bo_update_mobile where bo= '" + bo + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "checkEmailForUpdateMobile this is the query :::::::::" + sql);
            pst = conSlave.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                flag = "You_Are_NOT_Eligible";
            } else {
                flag = "eligible_for_apply";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }

        return flag;
    }

    public String existInKavach(String email) {
        String flag = "exist_not_in_kavach";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "checkEmailForUpdateMobile......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;
            String sql = "select * from ignore_existing_kavach_users where email= '" + email + "'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "checkEmailForUpdateMobile this is the query :::::::::" + sql);
            pst = conSlave.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                flag = "exist_in_kavach";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        }

        return flag;
    }

    public boolean MobileRequestAlreadyExist(String email) {
        Date date = new Date();
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String pdate = dt.format(date);
        boolean flag = false;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "vpn_check_exp_reg called BRO......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;
            String sql = "select * from otp_save_update_mobile where email= '" + email + "' and action='successfully updated'  and updated_time like '%" + pdate + "%'";
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " query :::::::::" + sql);
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

    public boolean MobileRequestAlreadyPending(String email) {
        Date date = new Date();
        DateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        String pdate = dt.format(date);
        boolean flag = false;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "vpn_check_exp_reg called BRO......");
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;
            String sql = "select status,form_name from final_audit_track where  applicant_email = ? and status like '%_pending' and form_name='mobile'";
            pst = conSlave.prepareStatement(sql);
            pst.setString(1, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "this is the query :::::::::" + pst);

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

    public int fetchCountOfWrongPasswordAttempts(Set<String> aliases, String mobile) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String sql = "select * from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < 60 and email =? order by login_time desc";
            for (String emailid : aliases) {
                try {
                    ps = conSlave.prepareStatement(sql);
                    ps.setString(1, emailid);
                    rs = ps.executeQuery();
                    while (rs.next()) {

                        if ((emailid.equalsIgnoreCase("support@gov.in") || emailid.equalsIgnoreCase("support@nic.in") || emailid.equalsIgnoreCase("support@nkn.in") || emailid.equalsIgnoreCase("vpnsupport@nic.in") || emailid.equalsIgnoreCase("vpnsupport@gov.in") || emailid.equalsIgnoreCase("smssupport@nic.in") || emailid.equalsIgnoreCase("smssupport@gov.in"))) {
                            if (rs.getString("remarks").equalsIgnoreCase("Authentication fail due to wrong password with mobile no:" + mobile)) {
                                ++count;
                            }

                        } else {
                            if (rs.getString("remarks").equalsIgnoreCase("Authentication fail due to wrong password")) {
                                ++count;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    public boolean isUserBlockedDueToWrongPasswordAttempts(Set<String> aliases, String mobile) {
        PreparedStatement ps = null, pst = null;
        ResultSet rs = null, rst = null;
        int count = 0;
        boolean flag = false;
        try {
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String sql = "select * from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < 60 and email =? order by login_time desc";
            outer:
            for (String emailid : aliases) {
                try {
                    ps = conSlave.prepareStatement(sql);
                    ps.setString(1, emailid);
                    System.out.println("inside isUserBlockedDueToWrongPasswordAttempts:: " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if ((emailid.equalsIgnoreCase("support@gov.in") || emailid.equalsIgnoreCase("support@nic.in") || emailid.equalsIgnoreCase("support@nkn.in") || emailid.equalsIgnoreCase("vpnsupport@nic.in") || emailid.equalsIgnoreCase("vpnsupport@gov.in") || emailid.equalsIgnoreCase("smssupport@nic.in") || emailid.equalsIgnoreCase("smssupport@gov.in"))) {
                            if (rs.getString("remarks").equalsIgnoreCase("Blocked due to wrong consecutive 6 passwords with mobile no:" + mobile)) {
                                flag = true;
                                break;
                            }
                        } else {
                            if (rs.getString("remarks").equalsIgnoreCase("Blocked due to wrong consecutive 6 passwords")) {
                                flag = true;
                                break;
                            }
                        }
                    }
                    String sql1 = "";

                    if (!flag) {
                        sql1 = "select * from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < 60 and email =? order by login_time desc";
                        pst = conSlave.prepareStatement(sql1);
                        pst.setString(1, emailid);
                        rst = pst.executeQuery();
                        while (rst.next()) {
                            if ((emailid.equalsIgnoreCase("support@gov.in") || emailid.equalsIgnoreCase("support@nic.in") || emailid.equalsIgnoreCase("support@nkn.in") || emailid.equalsIgnoreCase("vpnsupport@nic.in") || emailid.equalsIgnoreCase("vpnsupport@gov.in") || emailid.equalsIgnoreCase("smssupport@nic.in") || emailid.equalsIgnoreCase("smssupport@gov.in"))) {
                                if (rst.getString("remarks").equalsIgnoreCase("Authentication Sucessfull with mobile no:" + mobile)) //Authentication Sucessfull with mobile no:" + userValues.getMobile()
                                {
                                    count = 0;
                                    break outer;
                                }
                                if (rst.getString("remarks").equalsIgnoreCase("Authentication fail due to incorrect otp on mobile:" + mobile)) {  //Authentication fail due to incorrect otp on mobile:"+userValues.getMobile()
                                    count++;
                                }
                                if (count >= 6) {
                                    flag = true;
                                    break;
                                }

                            } else {
                                if (rst.getString("remarks").equalsIgnoreCase("Authentication Sucessfull")) {
                                    count = 0;
                                    break outer;
                                }
                                if (rst.getString("remarks").equalsIgnoreCase("Authentication fail due to incorrect otp")) {
                                    count++;
                                }
                                if (count >= 6) {
                                    flag = true;
                                    break;
                                }
                            }
                        }
//                        if ((emailid.equalsIgnoreCase("support@gov.in") || emailid.equalsIgnoreCase("support@nic.in") || emailid.equalsIgnoreCase("support@nkn.in") || emailid.equalsIgnoreCase("vpnsupport@nic.in") || emailid.equalsIgnoreCase("vpnsupport@gov.in") || emailid.equalsIgnoreCase("smssupport@nic.in") || emailid.equalsIgnoreCase("smssupport@gov.in"))) {
//                            sql1 = "select * from otp_save where TIMESTAMPDIFF (MINUTE,datetime,now()) < 60 and email=? and mobile=? order by datetime desc";
//                            pst = conSlave.prepareStatement(sql1);
//                            pst.setString(1, emailid);
//                            pst.setString(2, mobile);
//                        } else {
//                            sql1 = "select * from otp_save where TIMESTAMPDIFF (MINUTE,datetime,now()) < 60 and email=? order by datetime desc";
//                            pst = conSlave.prepareStatement(sql1);
//                            pst.setString(1, emailid);
//                        }
//
//                        rst = pst.executeQuery();
//                        while (rst.next()) {
//                            if (rst.getInt("attempt_login") >= 6) {
//                                flag = true;
//                                break;
//                            }
//                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (rst != null) {
                        try {
                            rst.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (pst != null) {
                        try {
                            pst.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flag;
    }

    public String fetchTrailMobileUpdateOneHour(String emailid, String ip) {
        Map<String, Object> map = new HashMap<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String flag = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String sql = "select ip,status from update_mobile_trail where TIMESTAMPDIFF (MINUTE,createdtimestamp,now()) < 60 and emailid =? order by createdtimestamp desc";
            try {
                ps = conSlave.prepareStatement(sql);
                ps.setString(1, emailid);
                rs = ps.executeQuery();
                int count = 0;
                while (rs.next()) {
                    if (rs.getString("status").equalsIgnoreCase("failed")) {
                        ++count;
                    } else if (count < 3) {
                        count = 0;
                    }

                    //int count = rs.getString("ip");
                    if (count >= 3) {
                        flag = "blocked";
                        break;
                    }
                }

                sql = "select ip,status from otp_save_mobile_attempts where TIMESTAMPDIFF (MINUTE,createdtimestamp,now()) < 60 and emailid =? order by createdtimestamp desc";

                ps = conSlave.prepareStatement(sql);
                ps.setString(1, emailid);
                rs = ps.executeQuery();
                count = 0;
                while (rs.next()) {
                    if (rs.getString("status").equalsIgnoreCase("failed")) {
                        ++count;
                    } else if (count < 3) {
                        count = 0;
                    }

                    //int count = rs.getString("ip");
                    if (count >= 3) {
                        flag = "blocked";
                        break;
                    }
                }

//                if (!flag.equalsIgnoreCase("blocked")) {
//                    sql = "select count(*) as c from update_mobile_trail where TIMESTAMPDIFF (MINUTE,createdtimestamp,now()) < 60 and ip =? and status=?";
//                    ps = con.prepareStatement(sql);
//                    ps.setString(1, ip);
//                    ps.setString(2, "failed");
//
//                    rs = ps.executeQuery();
//                    if (rs.next()) {
//                        int count = rs.getInt("c");
//                        if (count >= 3) {
//                            flag = "blocked";
//                        }
//                    }
//                }
            } catch (SQLException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flag;
    }

    public String fetchOtpSaveAttemptsOneHour(String emailid, String ip) {
        Map<String, Object> map = new HashMap<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String flag = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String sql = "select count(*) as count, ip,status from otp_save_mobile_attempts where TIMESTAMPDIFF (MINUTE,createdtimestamp,now()) < 60 and emailid =? and status='failed' order by createdtimestamp desc";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, emailid);
            System.out.println("PSSSS" + ps);
            rs = ps.executeQuery();
            //int count = 0;
            if (rs.next()) {

//                if (rs.getString("status").equalsIgnoreCase("failed")) {
//                    ++count;
//                } else {
//                    if (count < 3) {
//                        count = 0;
//                    }
//                }
                int count = rs.getInt("count");
                if (count >= 3) {
                    flag = "blocked";
                }
                System.out.println("count:::::::::::::::" + count);
            }

//                if (!flag.equalsIgnoreCase("blocked")) {
//                    sql = "select count(*) as c from otp_save_mobile_attempts where TIMESTAMPDIFF (MINUTE,createdtimestamp,now()) < 60 and ip =? and status=?";
//                    ps = con.prepareStatement(sql);
//                    ps.setString(1, ip);
//                    ps.setString(2, "failed");
//
//                    rs = ps.executeQuery();
//                    if (rs.next()) {
//                        int count = rs.getInt("c");
//                        if (count >= 3) {
//                            flag = "blocked";
//                        }
//                    }
//                }
        } catch (Exception ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return flag;
    }

    //added for sso sessionout
    public Map<String, Object> fetchLoginDetailsSsoCallback(String emailid) {
        Map<String, Object> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();

            //String sql = "select email,ip,role,remarks,login_time,logout_time,logout_status from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < ? and email =? and (logout_status !='logout by eforms' || logout_status!='logged out forcefully as sessionout')  order by login_time desc limit 1;";
            String sql = "select email,ip,role,remarks,login_time,logout_time,logout_status from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < ? and email =? order by login_time desc limit 1;";
            PreparedStatement ps = conSlave.prepareStatement(sql);
            ps.setString(1, pari_timeout);
            ps.setString(2, emailid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                map.put("email", rs.getString(1));
                map.put("ip", rs.getString(2));
                map.put("role", rs.getString(3));
                map.put("remarks", rs.getString(4));
                map.put("login_time", rs.getString(5));
                map.put("logout_time", rs.getString(6));
                map.put("logout_status", rs.getString(7));
                map.put("status", "sessionoutfalse");
            }

            //added for logout check
            if (map.isEmpty()) {
                String sql2 = "select email,ip,role,remarks,login_time,logout_time,logout_status from login_details where email =? order by login_time desc limit 1";
                PreparedStatement ps2 = conSlave.prepareStatement(sql2);
                ps2.setString(1, emailid);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    //map.put("status", "sessionoutfalse");
                    if (rs2.getString("logout_status") != null && rs2.getString("logout_status").contains("logout by parichay")) {
                        map.put("status", "sessionoutfalse");
                    }
                } else {
                    map.put("status", "sessionoutfalse");
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public void updateLogoutDetailsForcefully(String email, String logout_status) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            String update = "update login_details set logout_time = now(),login_time=now(),logout_status=? where email = ? order by login_time desc limit 1";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(update);
            ps.setString(1, logout_status);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for updateLogoutDetails: " + ps);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int insertParichayDataStorage(Person person) {
        PreparedStatement ps = null;
        int retval = 0;
        System.out.println("Start process of insertion into parichay_data_storage Table :::");
        String sql = "insert into parichay_data_storage(local_token_id,email,browser_id,username,session_id,status,token_valid,url,reason,mobile_no) values(?,?,?,?,?,?,?,?,?,?)";
        try {
            con = DbConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, person.getLocalTokenId());
            ps.setString(2, person.getEmail());
            ps.setString(3, person.getBrowserId());
            ps.setString(4, person.getUserName());
            ps.setString(5, person.getSessionId());
            ps.setString(6, person.getStatus());
            ps.setString(7, person.getTokenValid());
            ps.setString(8, person.getUrl());
            ps.setString(9, person.getReason());
            ps.setString(10, person.getMobileNo());
            retval = ps.executeUpdate();
            if (retval > 0) {
                System.out.println("INSERTED PARICHAY DETAILS ::::" + person.getUserName() + "::::" + person.getEmail());
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retval;
    }

    public Map<String, Object> fetchFromParichayDataStorage(String email) {
        Map<String, Object> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String sql = "select local_token_id,session_id,browser_id,username from parichay_data_storage where email=? order by id desc limit 1";
            PreparedStatement ps = conSlave.prepareStatement(sql);

            ps.setString(1, email);
            ps.executeQuery();
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {

                map.put("localTokenId", rs.getString(1));
                map.put("sessionId", rs.getString(2));
                map.put("browserId", rs.getString(3));
                map.put("username", rs.getString(4));

            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return map;
    }

    Set<String> getRolesForSSO(String email, String mobile) {
        Set<String> roles = new HashSet<>();
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        int rowCount = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();

            if (email.contains("support@nic.in") || email.contains("support@gov.in") || email.contains("smssupport@gov.in") || email.contains("support@nkn.in") || email.contains("smssupport@nic.in") || email.contains("vpnsupport@nic.in")) {
                ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where mobile = ?  and individual_ip=?");
                ps.setString(1, mobile);
                ps.setString(2, ip);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rowCount = rs.getInt("count(*)");
                }
                if (rowCount > 0) {
                    roles.add("admin");
                }
            } else {
                ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where email_address in (" + email + ") and individual_ip=?");
                ps.setString(1, ip);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    rowCount = rs.getInt("count(*)");
                }
                if (rowCount > 0) {
                    roles.add("admin");
                }
            }
            ps = conSlave.prepareStatement("select count(*) from " + Constants.SUPPORT_TABLE + " where ip = ?");
            ps.setString(1, ip);
            System.out.println("PS1 : " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                rowCount = rs.getInt("count(*)");
            }
            if (rowCount > 0) {

                if (email.contains("support@nic.in") || email.contains("support@gov.in") || email.contains("smssupport@gov.in") || email.contains("support@nkn.in") || email.contains("smssupport@nic.in") || email.contains("vpnsupport@nic.in")) {
                    ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where mobile = ?");
                    ps.setString(1, mobile);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        rowCount = rs.getInt("count(*)");
                    }
                    if (rowCount > 0) {
                        roles.add("sup");
                    }
                } else {
                    ps = conSlave.prepareStatement("select count(*) from " + Constants.ADMIN_TABLE + " where email_address in (" + email + ")");
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS1 : " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        rowCount = rs.getInt("count(*)");
                    }
                    if (rowCount > 0) {
                        roles.add("sup");
                    }
                }
            }
            if (!(email.contains("support@nic.in") || email.contains("support@gov.in") || email.contains("smssupport@gov.in") || email.contains("support@nkn.in") || email.contains("smssupport@nic.in") || email.contains("vpnsupport@nic.in"))) {
                ps = conSlave.prepareStatement("select ip from " + Constants.COORDINATOR_TABLE + " where emp_coord_email in (" + email + ")");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
                rs = ps.executeQuery();
                String ip = "";
                while (rs.next()) {
                    if (rs.getString("ip") == null) {
                        continue;
                    }
                    ip = rs.getString("ip").trim();

                    if (rs.getString("ip").contains(",")) {
                        boolean localFlag = false;
                        String[] ipList = rs.getString("ip").split(",");
                        for (String name : ipList) {
                            if ((name.trim()).equals(this.ip)) {
                                localFlag = true;
                                roles.add("co");
                                roles.add("email_co");
                                break;
                            }
                        }
                        if (localFlag) {
                            break;
                        }
                    }

                    if (ip.equalsIgnoreCase(this.ip)) {
                        roles.add("co");
                        roles.add("email_co");
                        break;
                    }
                }
            }
            ps = conSlave.prepareStatement("select count(*) from " + Constants.VPN_COORDINATOR_TABLE + " where emp_coord_email in (" + email + ")");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                rowCount = rs.getInt("count(*)");
                if (rowCount > 0) {
                    roles.add("co");
                    roles.add("vpn_co");// 24thoct19
                    break;
                }
            }
            ps = conSlave.prepareStatement("select count(*) from " + Constants.CA_TABLE + " where ca_email in (" + email + ")");
            //System.out.println("PS2 : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                rowCount = rs.getInt("count(*)");
            }
            if (rowCount > 0) {

                roles.add("ca");
            }
            ps = conSlave.prepareStatement("select count(*) from " + Constants.DASHBOARD_TABLE + " where email in (" + email + ")");
            //System.out.println("PS2 : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "PS2 : " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                rowCount = rs.getInt("count(*)");
            }
            if (rowCount > 0) {
                roles.add("dashboard");
            }

            roles.add("user");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for get roles: " + ps);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
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
        return roles;
    }

    ArrayList<String> setAllowedRegistrationNumbersFromStatusTable(String aliasesInString, List<String> roles, ArrayList<String> formsAllowed, Set<String> aliases) {
        ArrayList<String> allowedRegNumbers = new ArrayList<>();
        String qry = "";
        try {
            if (!roles.contains("sup") && !roles.contains("admin")) {
                conSlave = DbConnection.getSlaveConnection();
                qry = "SELECT stat_reg_no FROM status where stat_forwarded_by_email in (" + aliasesInString + ") or stat_forwarded_by_user in (" + aliasesInString + ")  stat_forwarded_to_user in (" + aliasesInString + ")";
                PreparedStatement ps = conSlave.prepareStatement(qry);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    allowedRegNumbers.add(rs.getString("stat_reg_no"));
                }

                if (roles.contains("email_co") || roles.contains("vpn_co")) {
                    for (String aliase : aliases) {
                        qry = "SELECT stat_reg_no FROM status where stat_forwarded_to_user like '%" + aliase + "%'"; //12-04-2022 
                        ps = conSlave.prepareStatement(qry);
                        rs = ps.executeQuery();
                        while (rs.next()) {
                            allowedRegNumbers.add(rs.getString("stat_reg_no"));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return allowedRegNumbers;
    }

    Set<String> fetchDaIds(List<String> aliases) {
        PreparedStatement ps = null;
        ResultSet res = null;
        Set<String> ids = new HashSet<>();
        String query = "select id from delegated_admin_ids where";
        try {
            conSlave = DbConnection.getSlaveConnection();

            for (String emailAddress : aliases) {
                query += " email = '" + emailAddress + "' or ";
            }

            query = query.replaceAll("\\s*or\\s*$", "");

            ps = conSlave.prepareStatement(query);
            //System.out.println("PS1 : " + ps);
            res = ps.executeQuery();
            String id = "";
            while (res.next()) {
                id = res.getString("id");
                ids.add(id);
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ids;
    }

    String insertIntoDaIdTable(String to_email) {
        boolean flag = false;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet res = null;
        Set<String> ids = new HashSet<>();
        String query = "";
        String[] emailAddresses = to_email.split(",");
        String id = "";

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            query = "select (max(id)+1) as newid from delegated_admin_ids";
            ps = conSlave.prepareStatement(query);
            System.out.println("PS1 : " + ps);
            res = ps.executeQuery();

//            System.out.println("RES>NEXT =" + res.next() + res);
            if (res.next()) {
                // System.out.println("INSIDE if");

                if (res.getString("newid") != null) {
                    id = res.getString("newid");
                } else {
                    id = "1";
                }

                for (String emailAdrs : emailAddresses) {
                    query = "insert into delegated_admin_ids(id,email) value(?,?)";
                    ps1 = con.prepareStatement(query);
                    ps1.setString(1, id);
                    ps1.setString(2, emailAdrs);
                    ps1.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    public List<Map<String, String>> fetchFromEmpCordinator(String email, String empType) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, String>> list = new ArrayList();

        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("SELECT * FROM employment_coordinator WHERE emp_coord_email=? AND (emp_type = 'dc' OR emp_type = ?)");
            ps.setString(1, email);
            ps.setString(2, empType);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchFromEmpCordinator " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> map = new HashMap<>();
                map.put("emp_category", rs.getString("emp_category"));
                map.put("emp_min_state_org", rs.getString("emp_min_state_org"));
                map.put("emp_dept", rs.getString("emp_dept"));
                map.put("alias", email);
                list.add(map);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Map<String, String> fetchEmpDepAndEmpMinVpnCordinator(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();
        //System.out.println("MAP111 = " + map);
        //Connection con = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            ps = conSlave.prepareStatement("SELECT * FROM `vpn_coordinator` WHERE emp_coord_email=?");
            ps.setString(1, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchEmpDepAndEmpMinVpnCordinator " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {

                map.put("emp_min_state_org", rs.getString("emp_min_state_org"));
                map.put("emp_dept", rs.getString("emp_dept"));

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
                if (conSlave != null) {
                    conSlave.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    List<FinalAuditDetails> fetchAllPendingRequstsfromFinalAuditTrack(Map<String, String> radioEmpCatMinDept, String email, String status) {
        Set<String> aliases = new HashSet<>();
        List<FinalAuditDetails> list = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
//        aliases = fetchAliases(email);
        aliases = ldap.fetchAliases(email);
        String emp_category = "", emp_min_state_org = "", emp_dept = "";
        if (radioEmpCatMinDept != null) {
            emp_category = radioEmpCatMinDept.get("emp_category");
            emp_min_state_org = radioEmpCatMinDept.get("emp_min_state_org");
            emp_dept = radioEmpCatMinDept.get("emp_dept");
        }
        try {
            for (String emails : aliases) {

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String sql = "select * from final_audit_track WHERE to_email = ? and status = ? and category = ? and min_state_org = ? and department = ? ";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, emails);
                pst.setString(2, status);
                pst.setString(3, emp_category);
                pst.setString(4, emp_min_state_org);
                pst.setString(5, emp_dept);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "pst for fetchAllPendingRequstsfromFinalAuditTrack " + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
                    FinalAuditDetails map = new FinalAuditDetails();
                    map.setRegistrationNumber(rs.getString("registration_no"));
                    map.setStatus(rs.getString("status"));
                    map.setFormName(rs.getString("form_name"));
                    map.setToEmail(rs.getString("to_email"));
                    list.add(map);
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

    ArrayList<String> fetchCoordinatorEmailFromCoordinatorEmailForMoveRequests(String coord_email, String emp_type) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps = conSlave.prepareStatement("select * from employment_coordinator where emp_coord_email = ? and (emp_type = 'dc' or emp_type = ?)");
            ps.setString(1, coord_email);
            ps.setString(2, emp_type);
            ResultSet rs = ps.executeQuery();
            String emp_category = "", emp_min_state_org = "", emp_dept = "";
            boolean flag = false;
            while (rs.next()) {
                emp_category = rs.getString("emp_category");
                emp_min_state_org = rs.getString("emp_min_state_org");
                emp_dept = rs.getString("emp_dept");
                flag = true;
            }
            if (flag) {

                ps = conSlave.prepareStatement("select * from employment_coordinator where emp_category = ? and emp_min_state_org = ? and emp_dept = ? and emp_type = ? and emp_status = 'a'");
                ps.setString(1, emp_category);
                ps.setString(2, emp_min_state_org);
                ps.setString(3, emp_dept);
                ps.setString(4, emp_type);
                rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(rs.getString("emp_coord_email"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String fetchCoordinatorOrDaEmailByBo(String bo) {
        String daInString = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps = conSlave.prepareStatement("select emp_coord_email,emp_admin_email,emp_type from employment_coordinator where emp_bo_id = ? and emp_status = 'a'");
            ps.setString(1, bo);
            ResultSet rs = ps.executeQuery();
            String emp_coord_email = "", emp_admin_email = "", emp_type = "", coord_mobile = "";
            Set<String> uniqueDas = new HashSet<>();
            Set<String> uniqueCoords = new HashSet<>();

            while (rs.next()) {
                emp_coord_email = rs.getString("emp_coord_email");
                emp_admin_email = rs.getString("emp_admin_email");
                coord_mobile = ldap.fetchMobileFromLdap(emp_coord_email);
                emp_type = rs.getString("emp_type");
                if (emp_type.equalsIgnoreCase("dc")) {
                    if (emp_coord_email.equalsIgnoreCase(emp_admin_email) && !isSupportEmail(emp_coord_email)) {
                        uniqueDas.add(emp_coord_email + ":" + coord_mobile.trim());
                    } else if (!isSupportEmail(emp_coord_email)) {
                        uniqueCoords.add(emp_coord_email + ":" + coord_mobile.trim());
                    }
                } else if (emp_type.equalsIgnoreCase("d")) {
                    if (emp_coord_email.equalsIgnoreCase(emp_admin_email) && !isSupportEmail(emp_coord_email)) {
                        uniqueDas.add(emp_coord_email + ":" + coord_mobile.trim());
                    }
                } else {
                    if (!isSupportEmail(emp_coord_email)) {
                        uniqueCoords.add(emp_coord_email + ":" + coord_mobile.trim());
                    }
                }
            }

            if (!uniqueDas.isEmpty()) {
                for (String uniqueDa : uniqueDas) {
                    daInString += uniqueDa + ",";
                }
            } else {
                for (String uniqueCoord : uniqueCoords) {
                    daInString += uniqueCoord + ",";
                }
            }

            daInString = daInString.replaceAll(",$", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return daInString;
    }

    private boolean isSupportEmail(String email) {
        if (email.equals("support@gov.in") || email.equals("support@nic.in") || email.equals("support@dummy.nic.in")) {
            return true;
        }
        return false;
    }

    public static Set<String> fetchAliases(String email) {
        System.out.println("Inside getLdapValues, getAliases of email  " + email);
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> aliases = new HashSet<>();
        aliases.add(email);
        String email_ldap = "", mailequivalentaddress = "";

        String[] attr = {"mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        Hashtable ht = new Hashtable();
        try {
            try {
                //ctx = ldapCon.getContext();

                System.out.println("Inside SingleApplicationContextSlave Constructor ...");

                ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                ht.put(Context.PROVIDER_URL, "ldap://10.122.34.101:389");
                ht.put(Context.SECURITY_AUTHENTICATION, "simple");
                ht.put(Context.SECURITY_PRINCIPAL, "uid=nikki.nhq,ou=People,o=NIC Support Outsourced,o=NIC Support,o=nic.in,dc=nic,dc=in");
                ht.put(Context.SECURITY_CREDENTIALS, "Nikki@389");
//                ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//                ht.put(Context.PROVIDER_URL, "ldap://10.103.12.15:389");
//                ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//                ht.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager");
//                ht.put(Context.SECURITY_CREDENTIALS, "@#Qwedsa");

                try {
                    ctx = new InitialDirContext(ht);
                    System.out.println("CTX value : " + ctx);
                } catch (Exception e) {
                    ctx = null;
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in LDAPConnection.java.get: " + e.getMessage());
                }
                System.out.println("Final CTX : " + ctx);

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();

            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                String final_bo = user_bo.replace("o=", "");

                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    if (email_ldap == null || email_ldap.equals("")) {
                        email_ldap = "";
                    }
                }
                //System.out.println("EMAIL : " + email_ldap);
                aliases.add(email_ldap.trim());

                if (attrs.get("mailequivalentaddress") != null) {
                    String tmp = attrs.get("mailequivalentaddress").toString();
                    mailequivalentaddress = tmp.substring(23, tmp.length());
                    if (mailequivalentaddress == null || mailequivalentaddress.equals("")) {
                        mailequivalentaddress = "";
                    }
                }

                if (mailequivalentaddress.contains(",")) {
                    String token[] = mailequivalentaddress.split(",");
                    for (int i = 0; (i < token.length && aliases.size() < 11); i++) {
                        if (!token[i].trim().equals(email)) {
                            aliases.add(token[i].trim());
                        }
                    }
                } else if (!mailequivalentaddress.equals("")) {
                    if (!mailequivalentaddress.trim().equals(email)) {
                        aliases.add(mailequivalentaddress.trim());
                    }
                }

            } else {
                //System.out.println("COULD NOT FIND ANY ALIASES");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
                if (ne != null) {
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }

        if (aliases.size() >= 10) {
            aliases.clear();
            aliases.add(email);
        }
        System.out.println("complete the process of get aliases of email:" + email);
        return aliases;
    }

    String fetchDelhiNodalOfficers(String employment, String ministry, String department) {
        String co_email = "";
        Set<String> delhiNodalOfficers = new HashSet();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT email FROM delhi_nodal_officers WHERE category=? and ministry=? and (department is null or department = ?)";
            PreparedStatement ps = conSlave.prepareStatement(qry);
            ps.setString(1, employment);
            ps.setString(2, ministry);
            ps.setString(3, department);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                delhiNodalOfficers.add(rs.getString("email").toLowerCase().trim());
            }

            for (String delhiNodalOfficer : delhiNodalOfficers) {
                co_email += delhiNodalOfficer + ",";
            }

            co_email = co_email.replaceAll(",$", "");
            co_email = co_email.trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return co_email;
    }

    public Map<String, String> fetchOtpForCheckingResendAttempts(String email, String mobile) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<String, String> map = new HashMap<>();

        try {
            try {
                conSlave = DbConnection.getSlaveConnection();
//                con = DbConnection.getConnection();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            }
            ps = conSlave.prepareStatement("select * from " + Constants.OTP_TABLE + " where email = ? and mobile=? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and resend_otp <= 3 and attempt_login <=6");
            ps.setString(1, email);
            ps.setString(2, mobile);

            //System.out.println("Query : " + ps);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for fetchOtp: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put("otp_mobile", rs.getString("otp_mobile"));
                map.put("otp_email", rs.getString("otp_email"));
                map.put("otp_attempt_mobile", rs.getString("resend_otp"));
                map.put("otp_attempt_email", rs.getString("resend_otp_email"));
                map.put("attempt_login", rs.getString("attempt_login"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
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

    public boolean updateResendOtpAttempts(String new_mobile, String email, String forWhich) {
        Map<String, String> map = fetchOtpForCheckingResendAttempts(email, new_mobile);
        if (map != null && map.isEmpty()) {
            return false;
        }

        PreparedStatement ps = null;
        int rs;
        try {
            con = DbConnection.getConnection();
            if (forWhich.equalsIgnoreCase("mobile")) {
                ps = con.prepareStatement("update " + Constants.OTP_TABLE + " set resend_otp=? where mobile =? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                ps.setInt(1, Integer.parseInt(map.get("otp_attempt_mobile").toString()) + 1);
                ps.setString(2, new_mobile);
            } else {
                ps = con.prepareStatement("update " + Constants.OTP_TABLE + " set resend_otp_email=? where email =? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW()");
                ps.setInt(1, Integer.parseInt(map.get("otp_attempt_mobile").toString()) + 1);
                ps.setString(2, email);
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertOtp: " + ps);
            rs = ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // }
        }
        return true;
    }

    void updateBlockUserTable(String email, String message) {
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();
            String sql = "insert into login_details(email,ip,role,remarks,login_time,sessionId) values (?,?,?,?,now(),?)";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, ip);
            ps.setString(3, "");
            ps.setString(4, message);
            ps.setString(5, ServletActionContext.getRequest().getSession().getId());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for insertLoginDetails: " + ps);
            ps.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    boolean isNoOfOtpAttemptsExceeds(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean flag = false;
        try {
            con = DbConnection.getConnection();

            String query = "select * from otp_save where TIMESTAMPDIFF (MINUTE,datetime,now()) < 60 and email =? order by datetime desc";
            ps = con.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            while (rs.next()) {
                int resendOtp = rs.getInt("resend_otp_email");
                if (resendOtp > 2) {
                    flag = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        return flag;
    }

    boolean isUserBlockedDueToWrongOtpAttempts(Set<String> aliases, String mobile) {
        PreparedStatement pst = null;
        ResultSet rst = null;
        boolean flag = false;
        int count = 0;
        Set<String> lowerCaseAliase = new HashSet<>();
        for (String email : aliases) {
            lowerCaseAliase.add(email.toLowerCase());
        }
        try {
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            outer:
            for (String emailid : lowerCaseAliase) {
                try {
                    String sql = "select * from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < 60 and email =? order by login_time desc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, emailid);
                    rst = pst.executeQuery();
                    while (rst.next()) {
                        if ((emailid.equalsIgnoreCase("support@gov.in") || emailid.equalsIgnoreCase("support@nic.in") || emailid.equalsIgnoreCase("support@nkn.in") || emailid.equalsIgnoreCase("vpnsupport@nic.in") || emailid.equalsIgnoreCase("vpnsupport@gov.in") || emailid.equalsIgnoreCase("smssupport@nic.in") || emailid.equalsIgnoreCase("smssupport@gov.in"))) {
                            if (rst.getString("remarks").equalsIgnoreCase("Authentication Sucessfull with mobile no:" + mobile)) //Authentication Sucessfull with mobile no:" + userValues.getMobile()
                            {
                                count = 0;
                                break outer;
                            }
                            if (rst.getString("remarks").equalsIgnoreCase("Authentication fail due to incorrect otp on mobile:" + mobile)) {  //Authentication fail due to incorrect otp on mobile:"+userValues.getMobile()
                                count++;
                            }
                            if (count >= 6) {
                                flag = true;
                                break;
                            }

                        } else {
                            if (rst.getString("remarks").equalsIgnoreCase("Authentication Sucessfull")) {
                                count = 0;
                                break outer;
                            }
                            if (rst.getString("remarks").equalsIgnoreCase("Authentication fail due to incorrect otp")) {
                                count++;
                            }
                            if (count >= 6) {
                                flag = true;
                                break;
                            }
                        }

                    }

                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (rst != null) {
                        try {
                            rst.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (pst != null) {
                        try {
                            pst.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        return flag;
    }

//    boolean isUserBlockedDueToWrongOtpAttempts(Set<String> aliases, String mobile) {
//        PreparedStatement pst = null;
//        ResultSet rst = null;
//        boolean flag = false;
//        try {
//            conSlave = DbConnection.getSlaveConnection(); //29dec2021
//            for (String emailid : aliases) {
//                try {
//                    if ((emailid.equalsIgnoreCase("support@gov.in") || emailid.equalsIgnoreCase("support@nic.in") || emailid.equalsIgnoreCase("support@nkn.in") || emailid.equalsIgnoreCase("vpnsupport@nic.in") || emailid.equalsIgnoreCase("vpnsupport@gov.in") || emailid.equalsIgnoreCase("smssupport@nic.in") || emailid.equalsIgnoreCase("smssupport@gov.in"))) {
//                        String sql1 = "select * from otp_save where TIMESTAMPDIFF (MINUTE,datetime,now()) < 60 and email=? and mobile=? order by datetime desc";
//                        pst = conSlave.prepareStatement(sql1);
//                        pst.setString(1, emailid);
//                        pst.setString(2, mobile);
//
//                    } else {
//                        String sql1 = "select * from otp_save where TIMESTAMPDIFF (MINUTE,datetime,now()) < 60 and email=? order by datetime desc";
//                        pst = conSlave.prepareStatement(sql1);
//                        pst.setString(1, emailid);
//                    }
//                    rst = pst.executeQuery();
//                    while (rst.next()) {
//                        if (rst.getInt("attempt_login") >= 6) {
//                            flag = true;
//                            break;
//                        }
//                    }
//
//                } catch (SQLException ex) {
//                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//                } finally {
//                    if (rst != null) {
//                        try {
//                            rst.close();
//                        } catch (SQLException ex) {
//                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                    if (pst != null) {
//                        try {
//                            pst.close();
//                        } catch (SQLException ex) {
//                            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                    }
//                }
//
//            }
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return flag;
//    }
    boolean isUserBlockDuetoWrongEmailOtpOrMobileOtp(Set<String> aliases) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "";
        boolean flag = false;
        try {
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            for (String email : aliases) {
                sql = "select * from login_details where TIMESTAMPDIFF (MINUTE,login_time,now()) < 30 and email =? order by login_time desc";
                try {
                    ps = conSlave.prepareStatement(sql);
                    ps.setString(1, email);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getString("remarks").equalsIgnoreCase("Authentication Sucessfull")) {
                            count = 0;
                            break;
                        }
                        if (rs.getString("remarks").equalsIgnoreCase("Authentication fail due to incorrect otp")) {
                            count++;
                        }
                        if (count >= 6) {
                            flag = true;
                            break;
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DbDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return flag;
    }

    int fetchEmailResendAttempts(String email, String mobile) {
        int noOfresendEmailAttempted = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            String ip = ServletActionContext.getRequest().getRemoteAddr();
            Connection con = null;
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            String sql = "SELECT resend_otp_email FROM otp_save where mobile =? and email = ? and date_add(datetime, INTERVAL 30 MINUTE) >= NOW() and resend_otp_email <=3 and active = 1";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, mobile);
            ps.setString(2, email);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendEmail1: " + ps);
            rs = ps.executeQuery();
            if (rs.next()) {
                noOfresendEmailAttempted = rs.getInt("resend_otp_email");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return noOfresendEmailAttempted;
    }

}
