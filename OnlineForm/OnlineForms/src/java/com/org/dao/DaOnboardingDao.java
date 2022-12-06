/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.MoveReqBean;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.dto.StatusDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import com.org.dao.SignUpDao;
import com.org.service.ProfileService;
import entities.LdapQuery;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.TreeSet;
import org.apache.commons.codec.binary.Base64;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Mohit Sharma
 */
public class DaOnboardingDao extends ActionSupport implements SessionAware {

    Map session;
    Map profile_values = new HashMap();
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public Map getSession() {
        return session;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
    }

    public String daOnboarding_tab(FormBean form_details, Map profile_values) {
//        String hodMobile = form_details.getHod_mobile().trim();
//        String hodEmail = form_details.getHod_email().trim();
//        Ldap ldap = new Ldap();
//        ProfileService profileService = new ProfileService();
//        if (ldap.emailValidate(hodEmail)) {
//            if (hodMobile.contains("X")) {
//                HashMap hodDetails = (HashMap) profileService.getHODdetails(hodEmail);
//                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
//                profile_values.put("hod_mobile", form_details.getHod_mobile());
//            }
//        }

        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        //Connection con = null;
        String CoodinaterEmail = "";
        //CoodinaterEmail = fetchCoordinatorEmail(form_details.getState_dept().trim());
        Iterator iterator = getCoordEmail((HashMap) profile_values).iterator();
        CoodinaterEmail = (String) iterator.next();
        System.out.println("Coordinater email ::::::::: " + CoodinaterEmail);
        Boolean errflag = false;
        String check_box_valu = "";
        String vpnNumber = "";
        if (form_details.getDa_vpn_reg_no().size() != 0 || form_details.getDa_vpn_reg_no() != null) {
            vpnNumber = form_details.getDa_vpn_reg_no().get(0).toString();
        }
        try {
            if (form_details.daon_mobile != null && form_details.daon_mobile.equalsIgnoreCase("on")) {
                System.out.println("value of daonbording checkbox value :::::" + form_details.daon_mobile);
                check_box_valu = form_details.daon_mobile;
            } else {
                check_box_valu = "off";
            }
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from daonboarding_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
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

            newref = "DAONBOARDING-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in IMAP dao tab 2: " + newref);
            String sql = "insert into daonboarding_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,registration_no,userip,ca_desig,uploaded_filename,renamed_filepath,vpn_reg_no,bo_name,coordinator_email,check_box,eligibility) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            System.out.println("profile_values = " + profile_values);
            //System.out.println("pst = " + pst);
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
            System.out.println("form details ::::::::::::::::" + form_details);
            System.out.println("form details ::::::::::::::::" + form_details.getUser_employment());
            System.out.println("form details ::::::::::::::::" + form_details.getUser_employment().trim());
            String userEmp = "";
            userEmp = form_details.getUser_employment().trim();

            System.out.println("UserEmp details :::::: " + userEmp);
            pst.setString(12, form_details.getUser_employment().trim());
            System.out.println("in dao ::::::::::::::::");
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
            pst.setString(22, newref);
            pst.setString(23, ip);
            pst.setString(24, form_details.getCa_design().trim());
            pst.setString(25, form_details.getUploaded_filename().trim());
            pst.setString(26, form_details.getRenamed_filepath().trim());
            pst.setString(27, vpnNumber);
            pst.setString(28, form_details.getBoName().trim());
            pst.setString(29, CoodinaterEmail);
            pst.setString(30, check_box_valu);
            pst.setString(31, form_details.getEligibility());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DA Onboarding dao tab  :" + pst);
            int i = pst.executeUpdate();
        } catch (Exception e) {
            errflag = true;
            newref = "";
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DA Onboarding dao tab : " + e.getMessage());
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

        return newref;
    }

    /*
    public String fetchCoordinatorEmail(String departmentName) {
        Set<String> bos = new HashSet<>();
        String coordEmail = "";
        try {
            PreparedStatement ps = null;
            ResultSet res = null;
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = " select emp_coord_email FROM employment_coordinator WHERE emp_dept = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, departmentName);

            System.out.println("Query to fetch out BO :::: " + ps);
            res = ps.executeQuery();
            while (res.next()) {

                coordEmail = res.getString("emp_coord_email");

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Bo in fetchBoFromEmpCoord " + bos);
        return coordEmail;
    }
     */
    //public Set<String>
    public String fetchBoName(String emp_category, String emp_min_state_org, String departmentName) {
        Set<String> bos = new HashSet<>();
        String boName = "";
        try {
            PreparedStatement ps = null;
            ResultSet res = null;
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = " select distinct(emp_bo_id) as bo FROM employment_coordinator WHERE "
                    + " emp_category = ? and emp_min_state_org = ?  ";
            if (departmentName != null && !departmentName.isEmpty()) {
                qry += "and emp_dept = ?";
            }

            ps = conSlave.prepareStatement(qry);
            ps.setString(1, emp_category);
            ps.setString(2, emp_min_state_org);
            if (departmentName != null && !departmentName.isEmpty()) {
                ps.setString(3, departmentName);
            }
            System.out.println("Query to fetch out BO :::: " + ps);
            System.out.println("Query =  to fetch out BO :::: " + ps);
            System.out.println("res = " + res);
            res = ps.executeQuery();
            while (res.next()) {
                if (res.getString("bo") != null) {
                    bos.add(res.getString("bo").trim().toLowerCase());
                    boName += res.getString("bo").trim().toLowerCase()+",";
                    System.out.println("BoName is HasSet ::::::: " + bos);
                    System.out.println("BoName is String ::::::: " + boName);
                }
            }
            boName = boName.replaceAll(",$", "");
            System.out.println("BoName is ::::::: " + boName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Bo in fetchBoFromEmpCoord " + bos);
        return boName;
    }

    public List<String> fetchUserEmail(String ragistrationNumber) {

        List<String> userValues = new ArrayList();
        try {
            PreparedStatement ps = null;
            ResultSet res = null;
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = " select auth_email, bo_name,vpn_reg_no,coordinator_email,mobile  FROM daonboarding_registration WHERE registration_no = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ragistrationNumber);

            System.out.println("Query to fetch out BO :::: " + ps);
            res = ps.executeQuery();
            while (res.next()) {

                userValues.add(res.getString("auth_email"));
                userValues.add(res.getString("bo_name"));
                userValues.add(res.getString("vpn_reg_no"));
                userValues.add(res.getString("coordinator_email"));
                userValues.add(res.getString("mobile"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return userValues;
    }

    public Map fetchUserDetails(String userEmail) {
        Map userValues = new HashMap();
        Map<String, String> map = new HashMap<>();
        Ldap ldap = new Ldap();
        SignUpDao signup = new SignUpDao();
        boolean ldapEmployee = ldap.emailValidate(userEmail);
        map = ldap.isUserNICEmployee(userEmail);
        boolean nicEmployee = map.get("isNICEmployee").equals("yes");
        userValues = (HashMap) signup.getUserValues(userEmail, ldapEmployee, nicEmployee);
        System.out.println("Bo in fetchBoFromEmpCoord " + userEmail);
        System.out.println("userValues ::::::::::::::::::::::::::::::::::::::::::: " + userValues);
        return userValues;
    }

    public List<String> checkBoxChecking(String ragistrationNumber) {

        List<String> check_box_values = new ArrayList();
        try {
            PreparedStatement ps = null;
            ResultSet res = null;
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = " select check_box  FROM daonboarding_registration WHERE registration_no = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ragistrationNumber);

            System.out.println("Query to fetch out BO :::: " + ps);
            res = ps.executeQuery();
            while (res.next()) {

                check_box_values.add(res.getString("check_box"));

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return check_box_values;
    }

    public String insertIntoEmploymentCoordinator(String boName, String vpn_ip, String coordinater_email, Map UserDetail, String userEmail) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        //Connection con = null;
        Boolean errflag = false;

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in IMAP dao tab 2: " + newref);
            String sql = "insert into employment_coordinator(emp_category,emp_min_state_org,emp_dept,emp_bo_id,emp_coord_email,emp_admin_email,ip,emp_type) "
                    + "values (?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            System.out.println("profile_values = " + UserDetail);
            System.out.println("pst = " + pst);
            pst.setString(1, UserDetail.get("user_employment").toString());
            pst.setString(2, UserDetail.get("min").toString());
            pst.setString(3, UserDetail.get("dept").toString());
            pst.setString(4, boName.toString());
            pst.setString(5, coordinater_email);
            pst.setString(6, userEmail); // auth eail
            pst.setString(7, vpn_ip);
            pst.setString(8, "d");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DA Onboarding dao tab  :" + pst);
            int i = pst.executeUpdate();
        } catch (Exception e) {
            errflag = true;
            newref = "";
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DA Onboarding dao tab : " + e.getMessage());
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

        return newref;
    }

    public String insertIntoIgnore_bo_update_mobile(String boName, String da_mobile, String da_email, String registrationNumber) {
        // String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        //Connection con = null;
        Boolean errflag = false;

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in Insert Into ignore_bo_update_mobile: " + registrationNumber);
            String sql = "insert into ignore_bo_update_mobile(bo_name,da_email,da_mobile) "
                    + "values (?,?,?)";
            pst = con.prepareStatement(sql);
            System.out.println("profile_values = " + da_email);
            System.out.println("pst = " + pst);
            pst.setString(1, boName);
            pst.setString(2, da_email);
            pst.setString(3, da_mobile);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DA Onboarding dao tab  :" + pst);
            int i = pst.executeUpdate();
        } catch (Exception e) {
            errflag = true;
            registrationNumber = "";
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DA Onboarding dao tab : " + e.getMessage());
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

        return registrationNumber;
    }

    /*
    public String dabase_entry(String boAdmin, String daEmailId, String daVpnIp, String daAdminEmailId, String daAdminIp) {
        //  String url = "http://10.1.146.228/JavaAPI/rest/user/exist"; // testing
        // String url = "http://10.1.145.59/JavaAPI/rest/user/exist"; // production
        try {
            System.out.println("in dbase ");
            //URL obj = new URL("http://localhost:8090/eform/onboard/da");
            //URL obj = new URL("http://l0.122.34.101:8081/eform/onboard/da");
            URL obj = new URL("http://localhost:8083/eform/onboard/da");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
            con.setRequestProperty("Content-Type", "application/json");
            String urlParameters = "{ \"boAdmin\": \"" + boAdmin + "\", \"daEmailId\": \"" + daEmailId + "\" , \"daVpnIp\": \"" + daVpnIp + "\" , \"daAdminEmailId\": \"" + daAdminEmailId + "\" , \"daAdminIP\": \"" + daAdminIp + "\" }";
            System.out.println("urlParameters ::::::::::::: " + urlParameters);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print result
            System.out.println("API Response from VPN Exist ::" + response.toString());
            return response.toString();
        } catch (Exception e) {
            System.out.println("exception :::::::::::::::::::::::::::::::" + e);
            e.printStackTrace();
            return "ERROR";
        }

    }
     */
    public boolean isSupportEmail(String email) {
        if (email.equals("support@gov.in") || email.equals("support@nic.in") || email.equals("support@dummy.nic.in")
                || email.equals("support@nkn.in") || email.equals("vpnsupport@nic.in")
                || email.equals("vpnsupport@gov.in") || email.equals("smssupport@gov.in")
                || email.equals("smssupport@nic.in")) {
            return true;
        }
        return false;
    }

    public HashSet<HashMap<String, String>> getCoordEmailDao(String employment, String emp_min_state_org, String emp_dept, String status) {
        HashSet<HashMap<String, String>> coordEmail = new HashSet<HashMap<String, String>>();
        ResultSet rs = null;
        PreparedStatement pst = null;
        String sql = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            if (emp_dept != null) {
//                sql = "select distinct emp_coord_email, emp_type, emp_admin_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =? and status = ? order by emp_domain asc";
                sql = "select distinct emp_coord_email, emp_type, emp_admin_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =?  order by emp_domain asc";
            } else {
//                sql = "select distinct emp_coord_email, emp_type, emp_admin_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and status = ? order by emp_domain asc";
                sql = "select distinct emp_coord_email, emp_type, emp_admin_email from employment_coordinator where emp_category = ? and emp_min_state_org =?  order by emp_domain asc";
            }
            pst = conSlave.prepareStatement(sql);
            pst.setString(1, employment);
            pst.setString(2, emp_min_state_org);
            if (emp_dept != null) {
                pst.setString(3, emp_dept);
//                pst.setString(4, status);
            } else {
//                pst.setString(3, status);
            }
            System.out.println("sql= " + pst);
            rs = pst.executeQuery();
            System.out.println("rs = " + rs.toString());
            while (rs.next()) {
                System.out.println("in rs ");
                HashMap<String, String> hm = new HashMap<>();
                System.out.println(" rs " + rs.getString("emp_coord_email"));
                hm.put("emp_coord_email", rs.getString("emp_coord_email"));
                hm.put("emp_type", rs.getString("emp_type"));
                hm.put("emp_admin_email", rs.getString("emp_admin_email"));
                coordEmail.add(hm);
                System.out.println("in hm " + hm.get("emp_coord_email"));
            }
            System.out.println("in hm " + coordEmail);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return coordEmail;
    }

    public HashSet<String> getCoordEmail(HashMap profile_values) {

        String employment = (String) profile_values.get("user_employment");
        System.out.println("inside coord email 1111111111111111111111111111" + employment);
        String emp_min_state_org = "";
        String emp_dept = "";
        String postingState = (String) profile_values.get("state");
//        String stateCode = (String) profile_values.get("stateCode");
//        String Org = (String) profile_values.get("Org");

        if (employment.equals("Central") || employment.trim().equals("UT")) {
            emp_min_state_org = profile_values.get("min").toString();
            emp_dept = profile_values.get("dept").toString();
        } else if (employment.trim().equals("State")) {
            emp_dept = profile_values.get("dept").toString();
            emp_min_state_org = profile_values.get("stateCode").toString();
        } else if (employment.trim().equals("Others") || employment.trim().equals("Psu") || employment.trim().equals("Const") || employment.trim().equals("Nkn") || employment.trim().equals("Project")) {
            emp_min_state_org = profile_values.get("Org").toString();
        }

        HashSet<HashMap<String, String>> da = null;
        HashSet<String> finalDa = new HashSet<>();
        // HashSet<String> domyCoordinatorForTesting = new HashSet<>();
        // domyCoordinatorForTesting.add("meenaxi.nhq@nic.in");
        try {
            if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                if (employment.equalsIgnoreCase("central") && !postingState.equalsIgnoreCase("Delhi")) {
                    if (postingState.equalsIgnoreCase("Kerala")) {
                        finalDa.add("r.abhilash@nic.in");
                    } else if (postingState.equalsIgnoreCase("Andaman and Nicobar Islands")) {
                        finalDa.add("gana.tn@nic.in");
                    } else if (postingState.equalsIgnoreCase("Andhra Pradesh")) {
                        finalDa.add("ramakanth@nic.in");
                    } else if (postingState.equalsIgnoreCase("Arunachal Pradesh")) {
                        finalDa.add("opung.ering@nic.in");
                    } else if (postingState.equalsIgnoreCase("Jammu and Kashmir")) {
                        finalDa.add("sudhir.sharma@nic.in");
                    } else if (postingState.equalsIgnoreCase("Karnataka")) {
                        finalDa.add("sujatha.dpawar@nic.in");
                    } else if (postingState.equalsIgnoreCase("Maharashtra")) {
                        if (((String) profile_values.get("nicCity")).equalsIgnoreCase("Nagpur")) {
                            finalDa.add("sdhoke@nic.in");
                        } else if (((String) profile_values.get("nicCity")).equalsIgnoreCase("Pune")) {
                            finalDa.add("rm.kharse@nic.in");
                        } else {
                            finalDa.add("navare.sr@nic.in");
                        }
                    } else if (((String) profile_values.get("add_state")).equalsIgnoreCase("Puducherry")) {
                        finalDa.add("raja.pon@nic.in");
                    } else {
                        System.out.println("in nkn ");
                        da = getCoordEmailDao(employment, emp_min_state_org, emp_dept, "a");
                        if (da != null || da.size() == 0) {
                            da = getCoordEmailDao("State", emp_min_state_org, "other", "a");
                        }
                    }
                }
            } else if (employment.equalsIgnoreCase("state")) {
                System.out.println("in state ");
                da = getCoordEmailDao(employment, emp_min_state_org, emp_dept, "a");
            } else {
                System.out.println("in emp dept null");
                da = getCoordEmailDao(employment, emp_min_state_org, null, "a");
            }

            if (null != da) {
                for (HashMap<String, String> emailCoordinators : da) {
                    if (emailCoordinators.get("emp_type") == null || emailCoordinators.get("emp_type").isEmpty()
                            || emailCoordinators.get("emp_type").equalsIgnoreCase("c")
                            || emailCoordinators.get("emp_type").equalsIgnoreCase("dc")) {
                        if (!emailCoordinators.get("emp_coord_email").equalsIgnoreCase(emailCoordinators.get("emp_admin_email"))) {
                            if (isSupportEmail(emailCoordinators.get("emp_coord_email"))) {
                                continue;
                            }
                            finalDa.add(emailCoordinators.get("emp_coord_email"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("finalDa::::::::::::" + finalDa);
        if (finalDa.size() == 0 || finalDa.isEmpty()) {
            finalDa.add("support@nic.in");
        }
        return finalDa;
    }

}
