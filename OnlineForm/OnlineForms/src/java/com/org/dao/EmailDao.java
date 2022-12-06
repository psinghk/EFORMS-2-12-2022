package com.org.dao;

import com.opencsv.CSVReader;
import com.org.bean.EmailBean;
import com.org.bean.UserData;
import com.org.bean.ValidatedEmailBean;
import com.org.connections.DbConnection;
import com.org.connections.LdapConnection;
import com.org.connections.SingleApplicationContextSlave;
import com.org.service.EmailService;
import com.org.utility.Constants;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import model.FormBean;
import validation.BulkValidation;
import entities.LdapQuery;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.directory.DirContext;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;

public class EmailDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;
    int iCampaignId;
    Map<Object, Object> session = new HashMap<Object, Object>();
    Map profile_values = new HashMap();
    String dbhost = getServletContext().getInitParameter("db-host");
    String dbuser = getServletContext().getInitParameter("db-user");
    String dbname = getServletContext().getInitParameter("db-name");
    String dbpass = getServletContext().getInitParameter("db-pass");
    String dbdriver = getServletContext().getInitParameter("dbdriver");
    String ctxfactory = getServletContext().getInitParameter("ctx-factory");
    String providerurl = getServletContext().getInitParameter("provider-url");
    String binddn = getServletContext().getInitParameter("bind-dn");
    String bindpass = getServletContext().getInitParameter("bind-pass");
    boolean erroWhileInsertingDataForRetired = false;
    String inserted_reg_number = "";

    public void Single_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {

            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into onlineform_temp_db.single_registration_tmp (dob,dor,id_type,preferred_email1,preferred_email2,emp_type,ip,type)"
                    + "values (?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getSingle_dob());
            pst.setString(2, form_details.getSingle_dor());
            pst.setString(3, form_details.getSingle_id_type());
            pst.setString(4, form_details.getSingle_email1());
            pst.setString(5, form_details.getSingle_email2());
            pst.setString(6, form_details.getSingle_emp_type());
            pst.setString(7, ip);
            pst.setString(8, form_details.getReq_for());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SINGLE dao tab 1: " + pst);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in SINGLE DAO tab 1: " + e.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String Single_tab2(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;

            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from single_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";// order by added by pr on 31stjul2020
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            ;
            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                String lastst = dbrefno.substring(23, dbrefno.length());
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
            newref = "SINGLEUSER-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "NEW REF NUMBER for single dao in tab 2: " + newref);
            String category = "";
            if (profile_values.get("user_employment").toString().equals("Central")) {
                String qu = "SELECT mail_account_category FROM ministry_department where dept=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("dept").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else if (profile_values.get("user_employment").toString().equals("Others")) {
                String qu = "SELECT mail_account_category FROM organization where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else if (profile_values.get("user_employment").toString().equals("Psu")) {
                String qu = "SELECT mail_account_category FROM psu_department where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else {
                category = "free";
            }

            String sql = "insert into single_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dob,dor,id_type,preferred_email1,"
                    + "preferred_email2,emp_type,registration_no,userip,description,type,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone,other_applicant_email,other_applicant_name,other_applicant_mobile,other_applicant_state,other_applicant_dept,other_applicant_empcode,request_flag,other_applicant_desig) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

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
            if (form_details.getReq_user_type().equalsIgnoreCase("other_user")) {
                form_details.setSingle_dob(form_details.getApplicant_single_dob());
                form_details.setSingle_dor(form_details.getApplicant_single_dor());
                form_details.setSingle_email1(form_details.getApplicant_single_email1());
                form_details.setSingle_email2(form_details.getApplicant_single_email2());
                form_details.setSingle_id_type(form_details.getApplicant_single_id_type());
                form_details.setSingle_emp_type(form_details.getApplicant_single_emp_type());
                form_details.setReq_for(form_details.getApplicant_req_for());
            }
            pst.setString(22, form_details.getSingle_dob());
            pst.setString(23, form_details.getSingle_dor());
            pst.setString(24, form_details.getSingle_id_type());
            pst.setString(25, form_details.getSingle_email1());
            pst.setString(26, form_details.getSingle_email2());
            pst.setString(27, form_details.getSingle_emp_type());
            pst.setString(28, newref);
            pst.setString(29, ip);
            pst.setString(30, category);
            pst.setString(31, form_details.getReq_for());
            pst.setString(32, form_details.getCa_design().trim());
            pst.setString(33, profile_values.get("under_sec_email").toString());
            pst.setString(34, profile_values.get("under_sec_name").toString());
            pst.setString(35, profile_values.get("under_sec_mobile").toString());
            pst.setString(36, profile_values.get("under_sec_desig").toString());
            pst.setString(37, profile_values.get("under_sec_tel").toString());
            pst.setString(38, form_details.getApplicant_email());
            pst.setString(39, form_details.getApplicant_name());
            pst.setString(40, form_details.getApplicant_mobile());
            pst.setString(41, form_details.getApplicant_state());
            String employment_min_dept = "";
            if (form_details.getApplicant_employment().equalsIgnoreCase("Central")) {
                if (form_details.getApplicant_dept().equalsIgnoreCase("other")) {
                    employment_min_dept = form_details.getApplicant_employment() + "~" + form_details.getApplicant_min() + "~" + form_details.getApplicant_dept() + "~" + form_details.getApplicant_other_dept();
                } else {
                    employment_min_dept = form_details.getApplicant_employment() + "~" + form_details.getApplicant_min() + "~" + form_details.getApplicant_dept();
                }
            } else if (form_details.getApplicant_employment().equalsIgnoreCase("State")) {
                if (form_details.getApplicant_Smi().equalsIgnoreCase("other")) {

                    employment_min_dept = form_details.getApplicant_employment() + "~" + form_details.getApplicant_stateCode() + "~" + form_details.getApplicant_Smi() + "~" + form_details.getApplicant_other_dept();
                } else {
                    employment_min_dept = form_details.getApplicant_employment() + "~" + form_details.getApplicant_stateCode() + "~" + form_details.getApplicant_Smi();

                }
            } else if (form_details.getApplicant_Org().equalsIgnoreCase("other")) {

                employment_min_dept = form_details.getApplicant_employment() + "~" + form_details.getApplicant_Org() + "~" + form_details.getApplicant_other_dept();
            } else {
                employment_min_dept = form_details.getApplicant_employment() + "~" + form_details.getApplicant_Org();
            }
            pst.setString(42, employment_min_dept);
            pst.setString(43, form_details.getApplicant_empcode());
            if (form_details.getReq_user_type().equalsIgnoreCase("other_user")) {
                pst.setString(44, "y");
            } else {
                pst.setString(44, "n");
            }
            pst.setString(45, form_details.getApplicant_design());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SINGLE dao tab 2: " + pst);
            int i = pst.executeUpdate();
        } catch (Exception e) {
            errflag = true;
            newref = "";
            // e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                int newrefno;

                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                //String search_regnum = "select registration_no from single_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
                // line modified by pr on 1stmar2020
                String search_regnum = "select registration_no from single_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";

                pst = conSlave.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }

                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(23, dbrefno.length());
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
                newref = "SINGLEUSER-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "NEW REF NUMBER for single dao in tab 2: " + newref);
                String category = "";
                if (profile_values.get("user_employment").toString().equals("Central")) {
                    String qu = "SELECT mail_account_category FROM ministry_department where dept=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("dept").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else if (profile_values.get("user_employment").toString().equals("Others")) {
                    String qu = "SELECT mail_account_category FROM organization where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else if (profile_values.get("user_employment").toString().equals("Psu")) {
                    String qu = "SELECT mail_account_category FROM psu_department where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else {
                    category = "free";
                }

                String sql = "insert into single_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dob,dor,id_type,preferred_email1,"
                        + "preferred_email2,emp_type,registration_no,userip,description,type,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
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
                pst.setString(22, form_details.getSingle_dob());
                pst.setString(23, form_details.getSingle_dor());
                pst.setString(24, form_details.getSingle_id_type());
                pst.setString(25, form_details.getSingle_email1());
                pst.setString(26, form_details.getSingle_email2());
                pst.setString(27, form_details.getSingle_emp_type());
                pst.setString(28, newref);
                pst.setString(29, ip);
                pst.setString(30, category);
                pst.setString(31, form_details.getReq_for());
                pst.setString(32, form_details.getCa_design().trim());
                pst.setString(33, profile_values.get("under_sec_email").toString());
                pst.setString(34, profile_values.get("under_sec_name").toString());
                pst.setString(35, profile_values.get("under_sec_mobile").toString());
                pst.setString(36, profile_values.get("under_sec_desig").toString());
                pst.setString(37, profile_values.get("under_sec_tel").toString());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SINGLE dao tab 2: " + pst);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                newref = "";
                e.printStackTrace();
            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
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

    public void Bulk_tab2(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {

            con = DbConnection.getConnection();
            // get bean of profile
            pst = null;
            String sql = "insert into onlineform_temp_db.bulk_registration_tmp (id_type,uploaded_filename,renamed_filepath,ip)"
                    + "values (?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getSingle_id_type());
            pst.setString(2, form_details.getUploaded_filename());
            pst.setString(3, form_details.getRenamed_filepath());
            pst.setString(4, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String Bulk_tab33Backup(FormBean form_details, String filename, Map profile_values, int campaignId) { // not used
        System.out.println("form details get employee description:::" + form_details.getSingle_emp_type());
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            pst = null;
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from bulk_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }

            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(21, dbrefno.length());
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
            newref = "BULKUSER-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
            String category = "";
            if (profile_values.get("user_employment").toString().equals("Central")) {
                String qu = "SELECT mail_account_category FROM ministry_department where dept=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("dept").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else if (profile_values.get("user_employment").toString().equals("Others")) {
                String qu = "SELECT mail_account_category FROM organization where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else if (profile_values.get("user_employment").toString().equals("Psu")) {
                String qu = "SELECT mail_account_category FROM psu_department where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else {
                category = "free";
            }

            String sql = "insert into bulk_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,id_type,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,type,emp_type,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getSingle_id_type());
            pst.setString(23, form_details.getUploaded_filename());
            pst.setString(24, form_details.getRenamed_filepath());
            pst.setString(25, newref);
            pst.setString(26, ip);
            pst.setString(27, form_details.getCa_design().trim());
            pst.setString(28, form_details.getReq_for());
            pst.setString(29, form_details.getSingle_emp_type());
            pst.setString(30, profile_values.get("under_sec_email").toString());
            pst.setString(31, profile_values.get("under_sec_name").toString());
            pst.setString(32, profile_values.get("under_sec_mobile").toString());
            pst.setString(33, profile_values.get("under_sec_desig").toString());
            pst.setString(34, profile_values.get("under_sec_tel").toString());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();

            List<FormBean> userList = readCSVFile(filename);
            for (FormBean bulkUser : userList) {
                bulkUser.setRegistration_no(newref);
                bulkUser.setAcc_cat(category);
                insertBulkUser(bulkUser, "email_bulk");
            }

        } catch (Exception e) {
            newref = "";
            //e.printStackTrace();
            errflag = true;
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                pst = null;
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from bulk_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";

                pst = conSlave.prepareStatement(search_regnum);

                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }

                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(21, dbrefno.length());
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
                newref = "BULKUSER-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
                String category = "";
                if (profile_values.get("user_employment").toString().equals("Central")) {
                    String qu = "SELECT mail_account_category FROM ministry_department where dept=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("dept").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else if (profile_values.get("user_employment").toString().equals("Others")) {
                    String qu = "SELECT mail_account_category FROM organization where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else if (profile_values.get("user_employment").toString().equals("Psu")) {
                    String qu = "SELECT mail_account_category FROM psu_department where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else {
                    category = "free";
                }

                String sql = "insert into bulk_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,id_type,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,type,emp_type,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getSingle_id_type());
                pst.setString(23, form_details.getUploaded_filename());
                pst.setString(24, form_details.getRenamed_filepath());
                pst.setString(25, newref);
                pst.setString(26, ip);
                pst.setString(27, form_details.getCa_design().trim());
                pst.setString(28, form_details.getReq_for());
                pst.setString(29, form_details.getSingle_emp_type());
                pst.setString(30, profile_values.get("under_sec_email").toString());
                pst.setString(31, profile_values.get("under_sec_name").toString());
                pst.setString(32, profile_values.get("under_sec_mobile").toString());
                pst.setString(33, profile_values.get("under_sec_desig").toString());
                pst.setString(34, profile_values.get("under_sec_tel").toString());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
                int i = pst.executeUpdate();

                List<FormBean> userList = readCSVFile(filename);
                System.out.println("userList" + userList);
                for (FormBean bulkUser : userList) {
                    bulkUser.setRegistration_no(newref);
                    bulkUser.setAcc_cat(category);
                    insertBulkUser(bulkUser, "email_bulk");
                }

            } catch (Exception e) {
                newref = "";
                e.printStackTrace();

            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
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

    public String Bulk_tab3(FormBean form_details, String filename, Map profile_values, int campaignId) {
        System.out.println("form details get employee description:::" + form_details.getSingle_emp_type());
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            pst = null;
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from bulk_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);

            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }

            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;

            } else {
                String lastst = dbrefno.substring(21, dbrefno.length());
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
            newref = "BULKUSER-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
            String category = "";
            if (profile_values.get("user_employment").toString().equals("Central")) {
                String qu = "SELECT mail_account_category FROM ministry_department where dept=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("dept").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else if (profile_values.get("user_employment").toString().equals("Others")) {
                String qu = "SELECT mail_account_category FROM organization where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else if (profile_values.get("user_employment").toString().equals("Psu")) {
                String qu = "SELECT mail_account_category FROM psu_department where org_name=?";
                pst = conSlave.prepareStatement(qu);
                pst.setString(1, profile_values.get("Org").toString());
                rs2 = pst.executeQuery();
                if (rs2.next()) {
                    category = rs2.getString("mail_account_category");
                } else {
                    category = "free";
                }

            } else {
                category = "free";
            }

            String sql = "insert into bulk_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,id_type,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,type,emp_type,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getSingle_id_type());
            pst.setString(23, form_details.getUploaded_filename());
            pst.setString(24, form_details.getRenamed_filepath());
            pst.setString(25, newref);
            pst.setString(26, ip);
            pst.setString(27, form_details.getCa_design().trim());
            pst.setString(28, form_details.getReq_for());
            pst.setString(29, form_details.getSingle_emp_type());
            pst.setString(30, profile_values.get("under_sec_email").toString());
            pst.setString(31, profile_values.get("under_sec_name").toString());
            pst.setString(32, profile_values.get("under_sec_mobile").toString());
            pst.setString(33, profile_values.get("under_sec_desig").toString());
            pst.setString(34, profile_values.get("under_sec_tel").toString());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();
//            EmailBean emailBean = new EmailBean();
//            emailBean.setAcc_cat(category);
            i = updateRegNumbersInBulkTable(newref, campaignId, category);
            if (i > 0) {
                updateEmailCampaign(campaignId, newref);
            }

        } catch (Exception e) {
            newref = "";
            //e.printStackTrace();
            errflag = true;
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                pst = null;
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from bulk_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";

                pst = conSlave.prepareStatement(search_regnum);

                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }

                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(21, dbrefno.length());
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
                newref = "BULKUSER-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
                String category = "";
                if (profile_values.get("user_employment").toString().equals("Central")) {
                    String qu = "SELECT mail_account_category FROM ministry_department where dept=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("dept").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else if (profile_values.get("user_employment").toString().equals("Others")) {
                    String qu = "SELECT mail_account_category FROM organization where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else if (profile_values.get("user_employment").toString().equals("Psu")) {
                    String qu = "SELECT mail_account_category FROM psu_department where org_name=?";
                    pst = conSlave.prepareStatement(qu);
                    pst.setString(1, profile_values.get("Org").toString());
                    rs2 = pst.executeQuery();
                    if (rs2.next()) {
                        category = rs2.getString("mail_account_category");
                    } else {
                        category = "free";
                    }

                } else {
                    category = "free";
                }

                String sql = "insert into bulk_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,id_type,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,type,emp_type,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getSingle_id_type());
                pst.setString(23, form_details.getUploaded_filename());
                pst.setString(24, form_details.getRenamed_filepath());
                pst.setString(25, newref);
                pst.setString(26, ip);
                pst.setString(27, form_details.getCa_design().trim());
                pst.setString(28, form_details.getReq_for());
                pst.setString(29, form_details.getSingle_emp_type());
                pst.setString(30, profile_values.get("under_sec_email").toString());
                pst.setString(31, profile_values.get("under_sec_name").toString());
                pst.setString(32, profile_values.get("under_sec_mobile").toString());
                pst.setString(33, profile_values.get("under_sec_desig").toString());
                pst.setString(34, profile_values.get("under_sec_tel").toString());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
                int i = pst.executeUpdate();
                i = updateRegNumbersInBulkTable(newref, campaignId, category);
                if (i > 0) {
                    updateEmailCampaign(campaignId, newref);
                }

            } catch (Exception e) {
                newref = "";
                e.printStackTrace();

            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
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

    public void Nkn_single_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside try: ");
            con = DbConnection.getConnection();
            // get bean of profile
            pst = null;
            String sql = "insert into onlineform_temp_db.nkn_registration_tmp (request_type,inst_name,inst_id,nkn_project,dob,dor,preferred_email1,preferred_email2,ip)"
                    + "values (?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getRequest_type());
            pst.setString(2, form_details.getInst_name());
            pst.setString(3, form_details.getInst_id());
            pst.setString(4, form_details.getNkn_project());
            pst.setString(5, form_details.getSingle_dob());
            pst.setString(6, form_details.getSingle_dor());
            pst.setString(7, form_details.getSingle_email1());
            pst.setString(8, form_details.getSingle_email2());
            pst.setString(9, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps: " + pst);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {

            if (pst != null) {
                try {
                    pst.close();
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

    }

    public void Nkn_bulk_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {

            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into onlineform_temp_db.nkn_registration_tmp (request_type,inst_name,inst_id,nkn_project,uploaded_filename,renamed_filepath,ip)"
                    + "values (?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getRequest_type());
            pst.setString(2, form_details.getInst_name());
            pst.setString(3, form_details.getInst_id());
            pst.setString(4, form_details.getNkn_project());
            pst.setString(5, form_details.getUploaded_filename());
            pst.setString(6, form_details.getRenamed_filepath());
            pst.setString(7, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps: " + pst);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {

            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String Nkn_tab2(FormBean form_details, String filename, Map profile_values) {
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            if (form_details.getRequest_type().equals("nkn_bulk")) {
                String search_regnum = "select registration_no from nkn_registration where date(datetime)=date(now()) and registration_no like 'NKN-BULK%' group by registration_no order by registration_no desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);

                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }

                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;

                } else {
                    String lastst = dbrefno.substring(21, dbrefno.length());
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
                newref = "NKN-BULK-FORM" + pdate1 + newref;
            } else {
                String search_regnum = "select registration_no from nkn_registration where date(datetime)=date(now()) and registration_no like 'NKN-FORM%' group by registration_no order by registration_no  desc limit 1";
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
                newref = "NKN-FORM" + pdate1 + newref;
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);

            String sql = "insert into nkn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,request_type,inst_name,inst_id,nkn_project,dob,dor,preferred_email1,"
                    + "preferred_email2,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone,type,id_type) "
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
            pst.setString(22, form_details.getRequest_type());
            pst.setString(23, form_details.getInst_name());
            pst.setString(24, form_details.getInst_id());
            pst.setString(25, form_details.getNkn_project());
            pst.setString(26, form_details.getSingle_dob());
            pst.setString(27, form_details.getSingle_dor());
            pst.setString(28, form_details.getSingle_email1());
            pst.setString(29, form_details.getSingle_email2());
            pst.setString(30, form_details.getUploaded_filename());
            pst.setString(31, form_details.getRenamed_filepath());
            pst.setString(32, newref);
            pst.setString(33, ip);
            pst.setString(34, form_details.getCa_design().trim());
            pst.setString(35, profile_values.get("under_sec_email").toString());
            pst.setString(36, profile_values.get("under_sec_name").toString());
            pst.setString(37, profile_values.get("under_sec_mobile").toString());
            pst.setString(38, profile_values.get("under_sec_desig").toString());
            pst.setString(39, profile_values.get("under_sec_tel").toString());
            pst.setString(40, form_details.getNkn_req_for());
            pst.setString(41, form_details.getNkn_id_type());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
            int i = pst.executeUpdate();

            if (form_details.getRequest_type().equals("nkn_bulk")) {
                List<FormBean> userList = readCSVFile(filename);
                for (FormBean bulkUser : userList) {
                    bulkUser.setRegistration_no(newref);
                    bulkUser.setAcc_cat("free");
                    insertBulkUser(bulkUser, "nkn_bulk");
                }
            }

        } catch (Exception e) {
            newref = "";
            errflag = true;
            //           e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
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

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                pst = null;
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);

                if (form_details.getRequest_type().equals("nkn_bulk")) {
                    String search_regnum = "select registration_no from nkn_registration where date(datetime)=date(now()) and registration_no like 'NKN-BULK%' group by registration_no order by registration_no  desc limit 1";
                    pst = conSlave.prepareStatement(search_regnum);

                    rs1 = pst.executeQuery();
                    if (rs1.next()) {
                        dbrefno = rs1.getString("registration_no");
                    }

                    if (dbrefno == null || dbrefno.equals("")) {
                        newrefno = 1;
                        newref = "000" + newrefno;

                    } else {
                        String lastst = dbrefno.substring(21, dbrefno.length());
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
                    newref = "NKN-BULK-FORM" + pdate1 + newref;
                } else {
                    String search_regnum = "select registration_no from nkn_registration where date(datetime)=date(now()) and registration_no like 'NKN-FORM%' group by registration_no order by registration_no  desc limit 1";
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
                    newref = "NKN-FORM" + pdate1 + newref;
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);

                String sql = "insert into nkn_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,request_type,inst_name,inst_id,nkn_project,dob,dor,preferred_email1,"
                        + "preferred_email2,uploaded_filename,renamed_filepath,registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone,type,id_type) "
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
                pst.setString(22, form_details.getRequest_type());
                pst.setString(23, form_details.getInst_name());
                pst.setString(24, form_details.getInst_id());
                pst.setString(25, form_details.getNkn_project());
                pst.setString(26, form_details.getSingle_dob());
                pst.setString(27, form_details.getSingle_dor());
                pst.setString(28, form_details.getSingle_email1());
                pst.setString(29, form_details.getSingle_email2());
                pst.setString(30, form_details.getUploaded_filename());
                pst.setString(31, form_details.getRenamed_filepath());
                pst.setString(32, newref);
                pst.setString(33, ip);
                pst.setString(34, form_details.getCa_design().trim());
                pst.setString(35, profile_values.get("under_sec_email").toString());
                pst.setString(36, profile_values.get("under_sec_name").toString());
                pst.setString(37, profile_values.get("under_sec_mobile").toString());
                pst.setString(38, profile_values.get("under_sec_desig").toString());
                pst.setString(39, profile_values.get("under_sec_tel").toString());
                pst.setString(40, form_details.getNkn_req_for());
                pst.setString(41, form_details.getNkn_id_type());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + pst);
                int i = pst.executeUpdate();

                if (form_details.getRequest_type().equals("nkn_bulk")) {
                    List<FormBean> userList = readCSVFile(filename);
                    for (FormBean bulkUser : userList) {
                        bulkUser.setRegistration_no(newref);
                        bulkUser.setAcc_cat("free");
                        insertBulkUser(bulkUser, "nkn_bulk");
                    }
                }

            } catch (Exception e) {
                newref = "";
                //errflag=true;
                e.printStackTrace();
            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
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

    public void Gem_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {
            System.out.println("form_details.getFwd_ofc_name()" + form_details.getFwd_ofc_name() + "form_details.getFwd_ofc_email" + form_details.getFwd_ofc_email());
            con = DbConnection.getConnection();
            // get bean of profile
            pst = null;
            String sql = "insert into onlineform_temp_db.gem_registration_tmp (pse,pse_ministry,pse_state,pse_district,dob,dor,preferred_email1,preferred_email2,traffic,ip,primary_user,primary_user_id,role_assign)"
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getPse());
            pst.setString(2, form_details.getPse_ministry());
            if (form_details.getPse().equals("central_pse")) {
                pst.setString(3, "");
            } else {
                pst.setString(3, form_details.getPse_state());
            }
            pst.setString(4, form_details.getPse_district());
            pst.setString(5, form_details.getSingle_dob());
            pst.setString(6, form_details.getSingle_dor());
            pst.setString(7, form_details.getSingle_email1());
            pst.setString(8, form_details.getSingle_email2());
            pst.setString(9, form_details.getDomestic_traf());
            pst.setString(10, ip);
            pst.setString(11, form_details.getPrimary_user());
            pst.setString(12, form_details.getPrimary_user_id());
            pst.setString(13, form_details.getRole_assign());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {

            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String Gem_tab2(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside try of gem tab 2");
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from gem_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "select query in gem tab 2" + pst);
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
            newref = "GEM-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
            System.out.println("form_details.getFwd_ofc_name()" + form_details.getFwd_ofc_name() + "form_details.getFwd_ofc_email" + form_details.getFwd_ofc_email() + form_details.getCa_design());
            String sql = "insert into gem_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,pse,pse_ministry,pse_state,pse_district,dob,dor,preferred_email1,preferred_email2,"
                    + "traffic,registration_no,userip,description,ca_desig,fwd_ofc_name,fwd_ofc_email,fwd_ofc_mobile,fwd_ofc_desig,fwd_ofc_add,fwd_ofc_tel,primary_user,primary_user_id,role_assign) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getPse());
            pst.setString(23, form_details.getPse_ministry());
            if (form_details.getPse().equals("central_pse")) {
                pst.setString(24, "");
            } else {
                pst.setString(24, form_details.getPse_state());
            }
            pst.setString(25, form_details.getPse_district());
            pst.setString(26, form_details.getSingle_dob());
            pst.setString(27, form_details.getSingle_dor());
            pst.setString(28, form_details.getSingle_email1());
            pst.setString(29, form_details.getSingle_email2());
            pst.setString(30, form_details.getDomestic_traf());
            pst.setString(31, newref);
            pst.setString(32, ip);
            pst.setString(33, "paid");
            pst.setString(34, form_details.getCa_design().trim());
            pst.setString(35, form_details.getFwd_ofc_name());
            pst.setString(36, form_details.getFwd_ofc_email());
            pst.setString(37, form_details.getFwd_ofc_mobile());
            pst.setString(38, form_details.getFwd_ofc_design());
            pst.setString(39, form_details.getFwd_ofc_add());
            pst.setString(40, form_details.getFwd_ofc_tel());
            pst.setString(41, form_details.getPrimary_user());
            pst.setString(42, form_details.getPrimary_user_id());
            pst.setString(43, form_details.getRole_assign());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "insert query PS: " + pst);
            int i = pst.executeUpdate();

        } catch (Exception e) {
            errflag = true;
            newref = "";
            //e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
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
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside try of gem tab 2");
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                pst = null;
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from gem_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "select query in gem tab 2" + pst);
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
                newref = "GEM-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER: " + newref);
                System.out.println("form_details.getFwd_ofc_name()" + form_details.getFwd_ofc_name() + "form_details.getFwd_ofc_email" + form_details.getFwd_ofc_email() + form_details.getCa_design());
                String sql = "insert into gem_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,pse,pse_ministry,pse_state,pse_district,dob,dor,preferred_email1,preferred_email2,"
                        + "traffic,registration_no,userip,description,ca_desig,fwd_ofc_name,fwd_ofc_email,fwd_ofc_mobile,fwd_ofc_desig,fwd_ofc_add,fwd_ofc_tel,primary_user,primary_user_id,role_assign) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getPse());
                pst.setString(23, form_details.getPse_ministry());
                if (form_details.getPse().equals("central_pse")) {
                    pst.setString(24, "");
                } else {
                    pst.setString(24, form_details.getPse_state());
                }
                pst.setString(25, form_details.getPse_district());
                pst.setString(26, form_details.getSingle_dob());
                pst.setString(27, form_details.getSingle_dor());
                pst.setString(28, form_details.getSingle_email1());
                pst.setString(29, form_details.getSingle_email2());
                pst.setString(30, form_details.getDomestic_traf());
                pst.setString(31, newref);
                pst.setString(32, ip);
                pst.setString(33, "paid");
                pst.setString(34, form_details.getCa_design().trim());
                pst.setString(35, form_details.getFwd_ofc_name());
                pst.setString(36, form_details.getFwd_ofc_email());
                pst.setString(37, form_details.getFwd_ofc_mobile());
                pst.setString(38, form_details.getFwd_ofc_design());
                pst.setString(39, form_details.getFwd_ofc_add());
                pst.setString(40, form_details.getFwd_ofc_tel());
                pst.setString(41, form_details.getPrimary_user());
                pst.setString(42, form_details.getPrimary_user_id());
                pst.setString(43, form_details.getRole_assign());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "insert query PS: " + pst);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                // errflag = true;
                newref = "";
                e.printStackTrace();
            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
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

    public Map validateBulkFile(String renamed_filepath, String whichform) {

        LdapConnection ldapcon = new LdapConnection();
        DirContext ctx = null;
        try {
            //ctx = ldapcon.getContext();

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

        } //catch (ClassNotFoundException ex) {
        catch (Exception ex) {
            Logger.getLogger(EmailDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        Map ExcelValidate = new HashMap();
        FileInputStream fis = null;
        try {
            // get total domain
            String domain = "";
//            if (whichform.equals("nkn")) {
//                //domain = "nkn.in";
//                domain = getDomain();
//            } else {
//                domain = LdapQuery.GetTotalDomain();
//                domain = domain.replace("associateddomain:", "");
//            }

            fis = new FileInputStream(renamed_filepath);

            org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            FormBean formbean = new FormBean();
            BulkValidation bulkValidate = new BulkValidation();
            Map values = new HashMap();
            ArrayList errorList = new ArrayList();    // error Arraylist
            ArrayList validUsers = new ArrayList();    // users without errors
            ArrayList notvalidUsers = new ArrayList();    // users with errors

            int count = 1;
            if (rowIterator.hasNext()) {
                while (rowIterator.hasNext()) {
                    // row iterate
                    formbean.setFname("");
                    formbean.setLname("");
                    formbean.setUser_design("");
                    formbean.setDept("");
                    formbean.setUser_state("");
                    formbean.setUser_mobile("");
                    formbean.setSingle_dor("");
                    formbean.setUid("");
                    formbean.setMail("");
                    formbean.setSingle_dob("");
                    formbean.setUser_empcode("");
                    formbean.setCountry_code("");

                    boolean flag = true;
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {        // column iterate
                        Cell cell = cellIterator.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        switch (cell.getColumnIndex()) {
                            case 0:
                                formbean.setFname(cell.getStringCellValue());
                                break;
                            case 1:
                                formbean.setLname(cell.getStringCellValue());
                                break;
                            case 2:
                                formbean.setUser_design(cell.getStringCellValue());
                                break;
                            case 3:
                                formbean.setDept(cell.getStringCellValue());
                                break;
                            case 4:
                                formbean.setUser_state(cell.getStringCellValue());
                                break;
                            case 5:
                                formbean.setCountry_code(cell.getStringCellValue());
                                break;
                            case 6:
                                formbean.setUser_mobile(cell.getStringCellValue());
                                break;
                            case 7:
                                formbean.setSingle_dor(cell.getStringCellValue());
                                break;
                            case 8:
                                formbean.setUid(cell.getStringCellValue());
                                break;
                            case 9:
                                formbean.setMail(cell.getStringCellValue());
                                break;
                            case 10:
                                formbean.setSingle_dob(cell.getStringCellValue());
                                break;
                            case 11:
                                formbean.setUser_empcode(cell.getStringCellValue());
                                break;

                            default:
                                break;
                        }
                    }

                    // start validating
                    // case 1 : fname
                    if (formbean.getFname() == null) {
                        errorList.add("First name can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        values = bulkValidate.Fname(formbean.getFname().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    if (formbean.getLname() == null) {
                        errorList.add("Last name can not be blank. Where row is:" + count + " and column is:2");
                        flag = false;
                    } else {
                        values = bulkValidate.Lname(formbean.getLname().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:2");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_design() == null) {
                        errorList.add("Designation can not be blank. Where row is:" + count + " and column is:3");
                        flag = false;
                    } else {
                        values = bulkValidate.Designation(formbean.getUser_design().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:3");
                            flag = false;
                        }
                    }

                    if (formbean.getDept() == null) {
                        errorList.add("Department/Ministry can not be blank. Where row is:" + count + " and column is:4");
                        flag = false;
                    } else {
                        values = bulkValidate.Department(formbean.getDept().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:4");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_state() == null) {
                        errorList.add("State can not be blank. Where row is:" + count + " and column is:5");
                        flag = false;
                    } else {
                        values = bulkValidate.State(formbean.getUser_state().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:5");
                            flag = false;
                        }
                    }
                    if (formbean.getCountry_code() == null) {
                        errorList.add("Country code can not be blank. Where row is:" + count + " and column is:6");
                        flag = false;
                    } else {
                        values = bulkValidate.countrycode(formbean.getCountry_code().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:6");
                            flag = false;
                        }
                    }
                    if (formbean.getUser_mobile() == null) {
                        errorList.add("Mobile can not be blank. Where row is:" + count + " and column is:7");
                        flag = false;
                    } else {
                        values = bulkValidate.Mobile(formbean.getUser_mobile().trim(), formbean.getCountry_code());
                        if (values.get("valid").equals("true")) {

                            String mobile = formbean.getCountry_code() + formbean.getUser_mobile();
                            //String mobile = "+919958510444";
                            String check_mobile = entities.LdapQuery.CheckMobile(mobile);

                            if (check_mobile.equalsIgnoreCase("success")) {

                            } else if (check_mobile.equalsIgnoreCase("create")) {

                            } else if (check_mobile.equalsIgnoreCase("block")) {
                                errorList.add("Email ID already registered with this mobile number" + " Where row is:" + count + " and column is:7");

                                flag = false;

                            } else {

                            }

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:7");
                            flag = false;
                        }
                    }

                    if (formbean.getSingle_dor() == null) {
                        errorList.add("Date of Retirement can not be blank. Where row is:" + count + " and column is:8");
                        flag = false;
                    } else {
                        values = bulkValidate.DOR(formbean.getSingle_dor().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:8");
                            flag = false;
                        }
                    }
                    String newmail = "";
                    if (formbean.getUid() == null) {
                        errorList.add("Email id can not be blank. Where row is:" + count + " and column is:9");
                        flag = false;
                    } else {
                        values = bulkValidate.Uid(formbean.getUid().trim(), "nocheck");
                        if (values.get("valid").equals("true")) {

                            newmail = values.get("newmail").toString();
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:9");
                            flag = false;
                        }
                    }

                    if (formbean.getMail() == null) {
                        errorList.add("Email address can not be blank. Where row is:" + count + " and column is:10");
                        flag = false;
                    } else {
                        values = bulkValidate.Mail(formbean.getMail().trim(), formbean.getUid().trim(), new HashSet<>(Arrays.asList("nic.in")));
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:10");
                            flag = false;
                        }
                    }

                    if (formbean.getSingle_dob() != null && !formbean.getSingle_dob().equals("")) {
                        values = bulkValidate.DOB(formbean.getSingle_dob().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:11");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_empcode() != null && !formbean.getUser_empcode().equals("")) {
                        values = bulkValidate.EMPNUMBER(formbean.getUser_empcode().trim());
                        if (values.get("valid").equals("true")) {

                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:12");
                            flag = false;
                        }
                    } else {

                    }

                    // finish validation check flag value
                    HashMap hm = new LinkedHashMap();
                    if (flag) {   // the row is ok with no error
                        hm.clear();
                        hm.put("First Name", formbean.getFname());
                        hm.put("Last Name", formbean.getLname());
                        hm.put("Designation", formbean.getUser_design());
                        hm.put("Department/ Ministry", formbean.getDept());
                        hm.put("State", formbean.getUser_state());
                        hm.put("Mobile(10 digit)", formbean.getUser_mobile());
                        hm.put("Date of Retirement(dd-mm-yyyy)", formbean.getSingle_dor());
                        hm.put("Email ID", formbean.getUid());
                        hm.put("Email address", formbean.getMail());
                        hm.put("Date of Birth(dd-mm-yyyy)", formbean.getSingle_dob());
                        hm.put("Employee Code", formbean.getUser_empcode());
                        hm.put("Country Code", formbean.getCountry_code());

                        validUsers.add(hm);
                    } else {      // row has error

                        hm.clear();
                        hm.put("First Name", formbean.getFname());
                        hm.put("Last Name", formbean.getLname());
                        hm.put("Designation", formbean.getUser_design());
                        hm.put("Department/ Ministry", formbean.getDept());
                        hm.put("State", formbean.getUser_state());
                        hm.put("Mobile(10 digit)", formbean.getUser_mobile());
                        hm.put("Date of Retirement(dd-mm-yyyy)", formbean.getSingle_dor());
                        hm.put("Email ID", formbean.getUid());
                        hm.put("Email address", formbean.getMail());
                        hm.put("Date of Birth(dd-mm-yyyy)", formbean.getSingle_dob());
                        hm.put("Employee Code", formbean.getUser_empcode());
                        hm.put("Country Code", formbean.getCountry_code());
                        notvalidUsers.add(hm);
                    }

                    count++;
                }

                ExcelValidate.put("validUsers", validUsers);
                ExcelValidate.put("notvalidUsers", notvalidUsers);
                ExcelValidate.put("errorList", errorList);
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "FILE IS EMPTY");
                ExcelValidate.put("errorMessage", "Please upload file in correct format.");
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERRORLIST: " + errorList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }

        return ExcelValidate;
    }

    public Map validateBulkCSVFileNKN(String renamed_filepath, String whichform, String id_type, String emp_type, UserData userdata, String department) {
        System.out.println("id_type::::::::" + id_type);
        Map ExcelValidate = new HashMap();
        BulkValidation bulkValidate = new BulkValidation();
        EmailService bulkEmailService = new EmailService();
        Map values = new HashMap();
        ArrayList nodomain = new ArrayList();
        ArrayList errorList = new ArrayList();    // error Arraylist
        ArrayList validUsers = new ArrayList();    // users without errors
        ArrayList notvalidUsers = new ArrayList();

        CSVReader reader = null;

        try {
            reader = new CSVReader(new FileReader(renamed_filepath));
            int count = 0;
            String[] line1;

            while ((line1 = reader.readNext()) != null) {
                ++count;
                boolean flag = true;
                FormBean formbean = new FormBean();

                if (line1[0] != null && !line1[0].contains("First Name")) {
                    formbean.setFname(line1[0]);
                    formbean.setLname(line1[1]);
                    formbean.setUser_design(line1[2]);
                    formbean.setDept(line1[3]);
                    formbean.setUser_state(line1[4]);
                    formbean.setCountry_code(line1[5]);
                    formbean.setUser_mobile(line1[6]);
                    formbean.setSingle_dor(line1[7]);
                    formbean.setUid(line1[8]);
                    formbean.setMail(line1[9]);

                    if (line1.length == 11) {
                        formbean.setSingle_dob(line1[10]);
                    } else if (line1.length == 12) {
                        formbean.setSingle_dob(line1[10]);
                        formbean.setUser_empcode(line1[11]);
                    }
                    if (formbean.getFname() == null) {
                        errorList.add("First name can not be blank. Where row is:" + count + " and column is:1");
                        flag = false;
                    } else {
                        values = bulkValidate.Fname(formbean.getFname().toLowerCase().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:1");
                            flag = false;
                        }
                    }

                    if (formbean.getLname() == null) {
                        errorList.add("Last name can not be blank. Where row is:" + count + " and column is:2");
                        flag = false;
                    } else {
                        values = bulkValidate.Lname(formbean.getLname().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:2");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_design() == null) {
                        errorList.add("Designation can not be blank. Where row is:" + count + " and column is:3");
                        flag = false;
                    } else {
                        values = bulkValidate.Designation(formbean.getUser_design().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:3");
                            flag = false;
                        }
                    }

                    if (formbean.getDept() == null) {
                        errorList.add("Posting State can not be blank. Where row is:" + count + " and column is:4");
                        flag = false;
                    } // Deparment validation  Added by Rahul 
                    else if (formbean.getDept() != null && department != null && !formbean.getDept().equalsIgnoreCase(department)) {
                        errorList.add("user department and login user department must be same: . Where row is:" + count + " and column is:4");
                        flag = false;
                    } else {
                        values = bulkValidate.Department(formbean.getDept().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:4");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_state() == null) {
                        errorList.add("State can not be blank. Where row is:" + count + " and column is:5");
                        flag = false;
                    } else {
                        values = bulkValidate.State(formbean.getUser_state().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:5");
                            flag = false;
                        }
                    }
                    if (formbean.getCountry_code() == null) {
                        errorList.add("Country code can not be blank. Where row is:" + count + " and column is:6");
                        flag = false;
                    } else {
                        values = bulkValidate.countrycode(formbean.getCountry_code().trim());
                        if (values.get("valid").equals("true")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "last name ok");
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:6");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_mobile() == null) {
                        errorList.add("Mobile can not be blank. Where row is:" + count + " and column is:7");
                        flag = false;
                    } else {
                        values = bulkValidate.Mobile(formbean.getUser_mobile().trim(), formbean.getCountry_code().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:7");
                            flag = false;
                        }
                    }

                    if (formbean.getSingle_dor() == null) {
                        errorList.add("Date of Retirement can not be blank. Where row is:" + count + " and column is:8");
                        flag = false;
                    } else {
                        values = bulkValidate.DOR(formbean.getSingle_dor().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:8");
                            flag = false;
                        }
                    }

                    if (formbean.getUid() == null) {
                        errorList.add("Email id can not be blank. Where row is:" + count + " and column is:9");
                        flag = false;
                    } else {
                        values = bulkValidate.Uid(formbean.getUid().trim(), id_type);
                        if (values.get("valid").equals("true")) {
                        } else {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:9");
                            flag = false;
                        }
                    }

                    if (formbean.getMail() == null) {
                        errorList.add("Email address can not be blank. Where row is:" + count + " and column is:10");
                        flag = false;
                    } else {
                        Map profile = new HashMap();
                        profile.put("dept", formbean.getDept().toLowerCase());
                        profile.put("emp_type", emp_type);
                        profile.put("form_type", whichform);
                        profile.put("id_type", id_type);
                        profile.put("user_employment", userdata.getUserOfficialData().getEmployment());
                        profile.put("stateCode", userdata.getUserOfficialData().getState());
                        profile.put("department", userdata.getUserOfficialData().getDepartment());
                        profile.put("min", userdata.getUserOfficialData().getMinistry());
                        profile.put("Org", userdata.getUserOfficialData().getOrganizationCategory());

//                    if (userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("central") || userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state")) {
//                        if (!formbean.getDept().toLowerCase().equalsIgnoreCase(userdata.getUserOfficialData().getDepartment())) {
//                            errorList.add("Entered department does not match with your department. Where row is:" + count + " and column is:4");
//                            flag = false;
//                        }
//                    } else if (!formbean.getDept().toLowerCase().equalsIgnoreCase(userdata.getUserOfficialData().getOrganizationCategory())) {
//                        errorList.add("Entered organization does not match with your organization. Where row is:" + count + " and column is:4");
//                        flag = false;
//                    }
                        values = bulkValidate.Mail(formbean.getMail().trim(), formbean.getUid().trim(), bulkEmailService.getDomain(profile));

                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:10");
                            flag = false;
                        }
                    }

                    if (formbean.getSingle_dob() != null && !formbean.getSingle_dob().equals("")) {
                        values = bulkValidate.DOB(formbean.getSingle_dob().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:11");
                            flag = false;
                        }
                    }

                    if (formbean.getUser_empcode() != null && !formbean.getUser_empcode().equals("")) {
                        values = bulkValidate.EMPNUMBER(formbean.getUser_empcode().trim());
                        if (values.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + count + " and column is:12");
                            flag = false;
                        }
                    }
                    HashMap hm = new LinkedHashMap();
                    hm.clear();
                    hm.put("First Name", formbean.getFname());
                    hm.put("Last Name", formbean.getLname());
                    hm.put("Designation", formbean.getUser_design());
                    hm.put("Posting State", formbean.getDept());
                    hm.put("State", formbean.getUser_state());
                    hm.put("Country Code", formbean.getCountry_code());
                    hm.put("Mobile(10 digit)", formbean.getUser_mobile());
                    hm.put("Date of Retirement(dd-mm-yyyy)", formbean.getSingle_dor());
                    hm.put("Login UID", formbean.getUid());
                    hm.put("Email address", formbean.getMail());
                    hm.put("Date of Birth(dd-mm-yyyy)", formbean.getSingle_dob());
                    hm.put("Employee Code", formbean.getUser_empcode());

                    if (flag) {   // the row is ok with no error
                        validUsers.add(hm);
                    } else {      // row has error

                        notvalidUsers.add(hm);

                    }
                } else {
                    ExcelValidate.put("errorMessage", "Please remove headings from csv file like First Name etc.");
                    break;
                }
            }
            reader.close();
            ExcelValidate.put("nodomain", nodomain);
            ExcelValidate.put("validUsers", validUsers);
            ExcelValidate.put("notvalidUsers", notvalidUsers);

            ExcelValidate.put("errorList", errorList);
            if (count == 0) {
                ExcelValidate.put("errorMessage", "File can not be empty.");
            }
        } catch (IOException e) {
            ExcelValidate.put("errorMessage", "Some issues in File upload!!! Please try after some time...");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            ExcelValidate.put("errorMessage", "File is not in correct format,please refer to the link on the form page: 'Click here to download Sample CSV-Format'");

        }
        return ExcelValidate;
    }

    public Map validateBulkCSVFileBackup(String renamed_filepath, String whichform, String id_type, String emp_type, String uploaded_filename, UserData userdata, String department) {  // Added By Manikant pandey
        System.out.println("id_type::::::::" + id_type);
        String errorInString = "";
        Map ExcelValidate = new HashMap();
        BulkValidation bulkValidate = new BulkValidation();
        EmailService bulkEmailService = new EmailService();
        Map values = new HashMap();
        int campaignId = -1;
        ArrayList nodomain = new ArrayList();
        ArrayList validUsers = new ArrayList();
        ArrayList notvalidUsers = new ArrayList();
        boolean flag = true, originalFlag = true, campaign_flag = false;
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(renamed_filepath));
            int count = 0;
            String[] line1;
            while ((line1 = reader.readNext()) != null) {
                ++count;
                FormBean formbean = new FormBean();

                if (line1[0] != null && !line1[0].contains("First Name")) {
                    formbean.setFname(line1[0]);
                    formbean.setLname(line1[1]);
                    formbean.setUser_design(line1[2]);
                    formbean.setDept(line1[3]);
                    formbean.setUser_state(line1[4]);
                    formbean.setCountry_code(line1[5]);
                    formbean.setUser_mobile(line1[6]);
                    formbean.setSingle_dor(line1[7]);
                    formbean.setUid(line1[8]);
                    formbean.setMail(line1[9]);

                    if (line1.length == 11) {
                        formbean.setSingle_dob(line1[10]);
                    } else if (line1.length == 12) {
                        formbean.setSingle_dob(line1[10]);
                        formbean.setUser_empcode(line1[11]);
                    }

                    if (!campaign_flag) {
                        Random rand = new Random();
                        int random = rand.nextInt(1000000000);
                        campaignId = addEmailCampaign(random, userdata, renamed_filepath, uploaded_filename, whichform, id_type, emp_type);
                        campaign_flag = true;
                    }
                    if (campaignId != -1) {
                        iCampaignId = campaignId;
                        session.put("campaign_id", campaignId);

                        if (formbean.getFname() == null) {
                            errorInString += "First name can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.Fname(formbean.getFname().toLowerCase().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter First name correct format";
                                flag = false;
                            }
                        }

                        if (formbean.getLname() == null) {
                            errorInString += "Last name can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.Lname(formbean.getLname().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter Last name correct format";
                                flag = false;
                            }
                        }

                        if (formbean.getUser_design() == null) {
                            errorInString += "Designation can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.Designation(formbean.getUser_design().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter Designation Correct Format.";
                                flag = false;
                            }
                        }

                        if (formbean.getDept() == null) {
                            errorInString += "Department can not be blank";
                            flag = false;
                        } // Deparment validation  Added by Rahul 
                        else if (formbean.getDept() != null && department != null && !formbean.getDept().equalsIgnoreCase(department)) {
                            errorInString += "Please Enter Department Correct Format and user department and login user department must be same.";
                            flag = false;
                        } else {
                            values = bulkValidate.Department(formbean.getDept().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter Department Correct Format.";
                                flag = false;
                            }
                        }

                        if (formbean.getUser_state() == null) {
                            errorInString += "State can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.State(formbean.getUser_state().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter State Correct.";
                                flag = false;
                            }
                        }
                        if (formbean.getCountry_code() == null) {
                            errorInString += "Country code can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.countrycode(formbean.getCountry_code().trim());
                            if (values.get("valid").equals("true")) {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "last name ok");
                            } else {
                                errorInString += "Please Enter Country code correct Format. [e.g  +91 ]";
                                flag = false;
                            }
                        }

                        if (formbean.getUser_mobile() == null) {
                            errorInString += "Mobile can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.Mobile(formbean.getUser_mobile().trim(), formbean.getCountry_code().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter Mobile number correct Format.";
                                flag = false;
                            }
                        }

                        if (formbean.getSingle_dor() == null) {
                            errorInString += "Date of Retirement can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.DOR(formbean.getSingle_dor().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please select Date of Retairment in correct format.";
                                flag = false;
                            }
                        }
                        if (formbean.getUid() == null) {
                            errorInString += "Email id can not be blank.";
                            flag = false;
                        } else {
                            values = bulkValidate.Uid(formbean.getUid().trim(), id_type);
                            if (values.get("valid").equals("true")) {
                            } else {
                                errorInString += "Please Enter Email id Correct format.";
                                flag = false;
                            }
                        }

                        if (formbean.getMail() == null) {
                            errorInString += "Email address can not be blank.";
                            flag = false;
                        } else {
                            Map profile = new HashMap();
                            profile.put("dept", formbean.getDept().toLowerCase());
                            profile.put("emp_type", emp_type);
                            profile.put("form_type", whichform);
                            profile.put("id_type", id_type);
                            profile.put("user_employment", userdata.getUserOfficialData().getEmployment());
                            profile.put("stateCode", userdata.getUserOfficialData().getState());
                            profile.put("department", userdata.getUserOfficialData().getDepartment());
                            profile.put("min", userdata.getUserOfficialData().getMinistry());
                            profile.put("Org", userdata.getUserOfficialData().getOrganizationCategory());

//                    if (userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("central") || userdata.getUserOfficialData().getEmployment().equalsIgnoreCase("state")) {
//                        if (!formbean.getDept().toLowerCase().equalsIgnoreCase(userdata.getUserOfficialData().getDepartment())) {
//                            errorList.add("Entered department does not match with your department. Where row is:" + count + " and column is:4");
//                            flag = false;
//                        }
//                    } else if (!formbean.getDept().toLowerCase().equalsIgnoreCase(userdata.getUserOfficialData().getOrganizationCategory())) {
//                        errorList.add("Entered organization does not match with your organization. Where row is:" + count + " and column is:4");
//                        flag = false;
//                    }
                            values = bulkValidate.Mail(formbean.getMail().trim(), formbean.getUid().trim(), bulkEmailService.getDomain(profile));

                            if (values.get("valid").equals("false")) {
                                errorInString += (values.get("errorMsg").toString());
                                flag = false;
                            }
                        }
//
                        if (formbean.getSingle_dob() != null && !formbean.getSingle_dob().equals("")) {
                            values = bulkValidate.DOB(formbean.getSingle_dob().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please select Date of Birth in correct format.";
                                flag = false;
                            }
                        }

                        if (formbean.getUser_empcode() != null && !formbean.getUser_empcode().equals("")) {
                            values = bulkValidate.EMPNUMBER(formbean.getUser_empcode().trim());
                            if (values.get("valid").equals("false")) {
                                errorInString += "Please Enter Employee Code in correct format.";
                                flag = false;
                            }
                        }
                        HashMap hm = new LinkedHashMap();
                        hm.clear();
                        hm.put("First Name", formbean.getFname());
                        hm.put("Last Name", formbean.getLname());
                        hm.put("Designation", formbean.getUser_design());
                        hm.put("Posting State", formbean.getDept());
                        hm.put("State", formbean.getUser_state());
                        hm.put("Country Code", formbean.getCountry_code());
                        hm.put("Mobile(10 digit)", formbean.getUser_mobile());
                        hm.put("Date of Retirement(dd-mm-yyyy)", formbean.getSingle_dor());
                        hm.put("Login UID", formbean.getUid());
                        hm.put("Email address", formbean.getMail());
                        hm.put("Date of Birth(dd-mm-yyyy)", formbean.getSingle_dob());
                        hm.put("Employee Code", formbean.getUser_empcode());
                        if (flag) {   // the row is ok with no error
                            validUsers.add(hm);
                            //addValidRecordsInBulkTable(formbean, campaignId, "email_bulk", Constants.BULK_USER);
                        } else {      // row has error
                            notvalidUsers.add(hm);
                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                            //addInvalidRecordsInBulkTable(formbean, errorInString, campaignId, "email_bulk", Constants.BULK_USER);
                        }
                    } else {
                        ExcelValidate.put("errorMessage", "Campaign could not be generated!!!");
                    }
                } else {
                    ExcelValidate.put("errorMessage", "Please remove headings from csv file like First Name etc.");
                    break;
                }

            }
            reader.close();
            ExcelValidate.put("campaign_id", session.get("campaign_id"));
            ExcelValidate.put("nodomain", nodomain);
            ExcelValidate.put("validUsers", validUsers);
            ExcelValidate.put("notvalidUsers", notvalidUsers);

            ExcelValidate.put("errorInString", errorInString);
            if (count == 0) {
                ExcelValidate.put("errorMessage", "File can not be empty.");
            }

        } catch (IOException e) {
            ExcelValidate.put("errorMessage", "Some issues in File upload!!! Please try after some time...");
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            ExcelValidate.put("errorMessage", "File is not in correct format,please refer to the link on the form page: 'Click here to download Sample CSV-Format'");

        }

        return ExcelValidate;
    }

    public List readCSVFile(String filename) {
        List<FormBean> userList = new ArrayList<FormBean>();
        CSVReader reader = null;
        try {

            String[] line1;
            reader = new CSVReader(new FileReader(filename), ',', '\"', 1);

            while ((line1 = reader.readNext()) != null) {
                FormBean formbean = new FormBean();
                formbean.setFname("");
                formbean.setLname("");
                formbean.setUser_design("");
                formbean.setDept("");
                formbean.setUser_state("");
                formbean.setCountry_code("");
                formbean.setUser_mobile("");
                formbean.setSingle_dor("");
                formbean.setUid("");
                formbean.setMail("");
                formbean.setSingle_dob("");
                formbean.setUser_empcode("");
                formbean.setFname(line1[0]);
                formbean.setLname(line1[1]);
                formbean.setUser_design(line1[2]);
                formbean.setDept(line1[3]);
                formbean.setUser_state(line1[4]);
                formbean.setCountry_code(line1[5]);
                formbean.setUser_mobile(line1[6]);
                formbean.setSingle_dor(line1[7]);
                formbean.setUid(line1[8]);
                formbean.setMail(line1[9]);

                if (line1.length == 11) {
                    formbean.setSingle_dob(line1[10]);
                } else if (line1.length == 12) {
                    formbean.setSingle_dob(line1[10]);
                    formbean.setUser_empcode(line1[11]);
                }
                userList.add(formbean);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userList;
    }

    private void insertBulkUser(FormBean bu, String form_type) {

        String sql = "insert into bulk_users (registration_no,form_type, fname, lname, designation,department, state, mobile, dor, uid,mail,"
                + "dob,emp_code,acc_cat,updatedon) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,now())";
        //Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bu.getRegistration_no());
            ps.setString(2, form_type);
            ps.setString(3, bu.getFname());
            ps.setString(4, bu.getLname());
            ps.setString(5, bu.getUser_design());
            ps.setString(6, bu.getDept());
            ps.setString(7, bu.getUser_state());
            ps.setString(8, '+' + bu.getCountry_code() + bu.getUser_mobile());
            ps.setString(9, bu.getSingle_dor());
            ps.setString(10, bu.getUid());
            ps.setString(11, bu.getMail());
            ps.setString(12, bu.getSingle_dob());
            ps.setString(13, bu.getUser_empcode());
            ps.setString(14, bu.getAcc_cat());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query of insert bulk user" + ps);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {

            if (ps != null) {
                try {
                    ps.close();
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

    }

    public List readExcelFile(String filename) {
        List<FormBean> userList = new ArrayList<FormBean>();
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(filename);

            org.apache.poi.ss.usermodel.Workbook workbook = WorkbookFactory.create(fis);

            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                while (rowIterator.hasNext()) {          // row iterate
                    FormBean formbean = new FormBean();
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    if (row.getRowNum() == 0) {
                        continue; //just skip the rows if row number is 0 
                    }
                    while (cellIterator.hasNext()) {        // column iterate
                        Cell cell = cellIterator.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        switch (cell.getColumnIndex()) {
                            case 0:
                                formbean.setFname(cell.getStringCellValue());
                                break;
                            case 1:
                                formbean.setLname(cell.getStringCellValue());
                                break;
                            case 2:
                                formbean.setUser_design(cell.getStringCellValue());
                                break;
                            case 3:
                                formbean.setDept(cell.getStringCellValue());
                                break;
                            case 4:
                                formbean.setUser_state(cell.getStringCellValue());
                                break;
                            case 5:
                                formbean.setCountry_code(cell.getStringCellValue());
                                break;
                            case 6:
                                formbean.setUser_mobile(cell.getStringCellValue());
                                break;
                            case 7:
                                formbean.setSingle_dor(cell.getStringCellValue());
                                break;
                            case 8:
                                formbean.setUid(cell.getStringCellValue());
                                break;
                            case 9:
                                formbean.setMail(cell.getStringCellValue());
                                break;
                            case 10:
                                formbean.setSingle_dob(cell.getStringCellValue());
                                break;
                            case 11:
                                formbean.setUser_empcode(cell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
                    }
                    userList.add(formbean);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return userList;
    }

    public Set<String> getDomain(Map profile) {

        TreeSet<String> finaldomain = new TreeSet<>();
        ResultSet rs = null;
        PreparedStatement pst = null;
        String employment = profile.get("user_employment").toString();
        String form_type = profile.get("form_type").toString();
        String emp_type = profile.get("emp_type").toString();
        String id_type = profile.get("id_type").toString();

        try {
            if (form_type.contains("gem")) {

                return new HashSet<>(Arrays.asList("gembuyer.in"));
            } //            else if (emp_type.equals("emp_contract")) {
            //                return new HashSet<>(Arrays.asList("supportgov.in"));
            //            } else if (emp_type.equals("consultant")) {
            //                return new HashSet<>(Arrays.asList("govcontractor.in"));
            //            } 
            else {
                conSlave = DbConnection.getSlaveConnection();
                pst = null;
                String sql = "";
                if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                    sql = "select distinct emp_domain,emp_mail_acc_cat from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =?  order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile.get("min").toString());
                    if (profile.get("form_type").toString().equalsIgnoreCase("bulk")) {
                        pst.setString(3, profile.get("department").toString());
                    } else {
                        pst.setString(3, profile.get("dept").toString());
                    }
                } else if (employment.equalsIgnoreCase("state")) {
                    sql = "select distinct emp_domain,emp_mail_acc_cat from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =? order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile.get("stateCode").toString());
                    if (profile.get("form_type").toString().equalsIgnoreCase("bulk")) {
                        pst.setString(3, profile.get("department").toString());
                    } else {
                        pst.setString(3, profile.get("dept").toString());
                    }
                } else {
                    sql = "select distinct emp_domain,emp_mail_acc_cat from employment_coordinator where emp_category = ? and emp_min_state_org = ? order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile.get("Org").toString());
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "query of getDomain" + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
                    if (rs.getString("emp_domain") != null && !rs.getString("emp_domain").isEmpty() && !rs.getString("emp_domain").trim().equalsIgnoreCase("null")) {
                        if (rs.getString("emp_mail_acc_cat") != null && rs.getString("emp_mail_acc_cat").trim().equalsIgnoreCase("paid")) {
                            finaldomain.add(rs.getString("emp_domain").trim().toLowerCase());
                        } else if (emp_type.equals("emp_contract")) {
                            finaldomain.add("supportgov.in");
                        } else if (emp_type.equals("consultant")) {
                            finaldomain.add("govcontractor.in");
                        } else {
                            finaldomain.add(rs.getString("emp_domain").trim().toLowerCase());
                        }
                    }
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "finaldomain" + finaldomain);

                if (finaldomain.isEmpty()) {
                    if (form_type.contains("nkn")) {
                        return getnknDomain();
                    } else {
                        String domain = LdapQuery.GetTotalDomain();
                        domain = domain.replace("associateddomain:", "");
                        if (domain.contains(",")) {
                            String[] token1 = domain.split(",");
                            for (String a : token1) {
                                if (a.trim().equalsIgnoreCase("nic.in @nic.in")) {
                                    finaldomain.add("nic.in");
                                }
                                finaldomain.add(a.trim().toLowerCase());
                            }
                        } else { //if (!domain.contains("nic.in"))  this was removed
                            finaldomain.add(domain.toLowerCase());
                        }
                    }
                }
            }
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
        return finaldomain;
    }

    public void email_act_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {

            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into onlineform_temp_db.emailact_registration_tmp (email,dor,ip)"
                    + "values (?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getSingle_email1());
            pst.setString(2, form_details.getSingle_dor());
            pst.setString(3, ip);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String email_act_tab2(FormBean form_details, Map profile_values, String emailReq) {
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;

            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from email_act_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            ;
            String table = "";
            if (emailReq.equals("email_act")) {
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(26, dbrefno.length());
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
                table = "email_act_registration";
                newref = "EMAILACTIVATE-FORM" + pdate1 + newref;
                String category = "";
                String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,preferred_email1,dor,"
                        + " registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone,work_order,emp_type,renamed_filepath) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getAct_email1());
                pst.setString(23, form_details.getSingle_dor());
                pst.setString(24, newref);
                pst.setString(25, ip);
                pst.setString(26, form_details.getCa_design().trim());
                pst.setString(27, profile_values.get("under_sec_email").toString());
                pst.setString(28, profile_values.get("under_sec_name").toString());
                pst.setString(29, profile_values.get("under_sec_mobile").toString());
                pst.setString(30, profile_values.get("under_sec_desig").toString());
                pst.setString(31, profile_values.get("under_sec_tel").toString());
                pst.setString(32, form_details.getUploaded_filename());
                pst.setString(33, form_details.getSingle_emp_type());
                pst.setString(34, form_details.getRenamed_filepath());
                if (form_details.getUploaded_filename() != "") {

                }
                int i = pst.executeUpdate();

            } else {
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(28, dbrefno.length());
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
                table = "email_deact_registration";
                newref = "EMAILDEACTIVATE-FORM" + pdate1 + newref;
                form_details.setSingle_email1(profile_values.get("email").toString());
                String category = "";
                String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,preferred_email1,"
                        + " registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getSingle_email1());
                pst.setString(23, newref);
                pst.setString(24, ip);
                pst.setString(25, form_details.getCa_design().trim());
                pst.setString(26, profile_values.get("under_sec_email").toString());
                pst.setString(27, profile_values.get("under_sec_name").toString());
                pst.setString(28, profile_values.get("under_sec_mobile").toString());
                pst.setString(29, profile_values.get("under_sec_desig").toString());
                pst.setString(30, profile_values.get("under_sec_tel").toString());
                int i = pst.executeUpdate();

            }
        } catch (Exception e) {
            errflag = true;
            // e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                int newrefno;

                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from email_act_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                ;
                String table = "";
                if (emailReq.equals("email_activate")) {
                    if (dbrefno == null || dbrefno.equals("")) {
                        newrefno = 1;
                        newref = "000" + newrefno;
                    } else {
                        String lastst = dbrefno.substring(26, dbrefno.length());
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
                    table = "email_act_registration";
                    newref = "EMAILACTIVATE-FORM" + pdate1 + newref;
                    String category = "";
                    String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                            + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,preferred_email1,dor,"
                            + " registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                    pst.setString(22, form_details.getSingle_email1());
                    pst.setString(23, form_details.getSingle_dor());
                    pst.setString(24, newref);
                    pst.setString(25, ip);
                    pst.setString(26, form_details.getCa_design().trim());
                    pst.setString(27, profile_values.get("under_sec_email").toString());
                    pst.setString(28, profile_values.get("under_sec_name").toString());
                    pst.setString(29, profile_values.get("under_sec_mobile").toString());
                    pst.setString(30, profile_values.get("under_sec_desig").toString());
                    pst.setString(31, profile_values.get("under_sec_tel").toString());

                    int i = pst.executeUpdate();
                } else {
                    if (dbrefno == null || dbrefno.equals("")) {
                        newrefno = 1;
                        newref = "000" + newrefno;
                    } else {
                        String lastst = dbrefno.substring(28, dbrefno.length());
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
                    table = "email_deact_registration";
                    newref = "EMAILDEACTIVATE-FORM" + pdate1 + newref;
                    form_details.setSingle_email1(profile_values.get("email").toString());
                    String category = "";
                    String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                            + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,preferred_email1,"
                            + " registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                    pst.setString(22, form_details.getSingle_email1());
                    pst.setString(23, newref);
                    pst.setString(24, ip);
                    pst.setString(25, form_details.getCa_design().trim());
                    pst.setString(26, profile_values.get("under_sec_email").toString());
                    pst.setString(27, profile_values.get("under_sec_name").toString());
                    pst.setString(28, profile_values.get("under_sec_mobile").toString());
                    pst.setString(29, profile_values.get("under_sec_desig").toString());
                    pst.setString(30, profile_values.get("under_sec_tel").toString());

                    int i = pst.executeUpdate();
                }

            } catch (Exception e) {
                errflag = true;
                // e.printStackTrace();
            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
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

    public void email_deact_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {

            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into onlineform_temp_db.emaildeact_registration_tmp (email,ip)"
                    + "values (?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getSingle_email1());
            pst.setString(2, form_details.getSingle_dor());
            pst.setString(3, ip);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String email_deact_tab2(FormBean form_details, Map profile_values) {
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;

            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from email_deact_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            ;
            String table = "";

            if (dbrefno == null || dbrefno.equals("")) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                String lastst = dbrefno.substring(28, dbrefno.length());
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
            newref = "EMAILDEACTIVATE-FORM" + pdate1 + newref;
            form_details.setSingle_email1(profile_values.get("email").toString());
            String category = "";
            String sql = "insert into  email_deact_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,preferred_email1,"
                    + " registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getDeact_email1());
            pst.setString(23, newref);
            pst.setString(24, ip);
            pst.setString(25, form_details.getCa_design().trim());
            pst.setString(26, profile_values.get("under_sec_email").toString());
            pst.setString(27, profile_values.get("under_sec_name").toString());
            pst.setString(28, profile_values.get("under_sec_mobile").toString());
            pst.setString(29, profile_values.get("under_sec_desig").toString());
            pst.setString(30, profile_values.get("under_sec_tel").toString());
            int i = pst.executeUpdate();

        } catch (Exception e) {
            errflag = true;
            // e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                int newrefno;

                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from email_act_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                ;
                String table = "";
                if (dbrefno == null || dbrefno.equals("")) {
                    newrefno = 1;
                    newref = "000" + newrefno;
                } else {
                    String lastst = dbrefno.substring(28, dbrefno.length());
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
                table = "email_deact_registration";
                newref = "EMAILDEACTIVATE-FORM" + pdate1 + newref;
                form_details.setSingle_email1(profile_values.get("email").toString());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "NEW REF NUMBER for single dao in tab 2: " + newref);
                String category = "";
                String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,preferred_email1,"
                        + " registration_no,userip,ca_desig,under_sec_email,under_sec_name,under_sec_mobile,under_sec_desig,under_sec_telephone) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getSingle_email1());
                pst.setString(23, newref);
                pst.setString(24, ip);
                pst.setString(25, form_details.getCa_design().trim());
                pst.setString(26, profile_values.get("under_sec_email").toString());
                pst.setString(27, profile_values.get("under_sec_name").toString());
                pst.setString(28, profile_values.get("under_sec_mobile").toString());
                pst.setString(29, profile_values.get("under_sec_desig").toString());
                pst.setString(30, profile_values.get("under_sec_tel").toString());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "SINGLE dao tab 2: " + pst);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                errflag = true;
                // e.printStackTrace();
            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
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

    public Set<String> getDomainForBulk(Map profile) {

        Set<String> finaldomain = new HashSet<>();
        ResultSet rs = null;
        PreparedStatement pst = null;

        try {
            conSlave = DbConnection.getSlaveConnection();
            pst = null;
            String department = profile.get("dept").toString();
            String empType = profile.get("emp_type").toString();
            String formType = profile.get("form_type").toString();
//            if (empType.equals("emp_contract")) {
//                finaldomain.add("supportgov.in");
//            } else if (empType.equals("consultant")) {
//                finaldomain.add("govcontractor.in");
//            }
//            
//            else {
            String sql = "";
            if (formType.contains("nkn")) {
                sql = "select distinct emp_domain,emp_mail_acc_cat from employment_coordinator where emp_min_state_org = ?";
            } else {
                sql = "select distinct emp_domain,emp_mail_acc_cat from employment_coordinator where emp_dept = ?";
            }
            pst = conSlave.prepareStatement(sql);
            pst.setString(1, department);
            System.out.println("DOMAIN QUERY :::: " + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                //finaldomain.add(rs.getString("emp_domain").trim().toLowerCase());
                if (rs.getString("emp_domain") != null && !rs.getString("emp_domain").isEmpty() && !rs.getString("emp_domain").trim().equalsIgnoreCase("null")) {
                    if (rs.getString("emp_mail_acc_cat") != null && rs.getString("emp_mail_acc_cat").trim().equalsIgnoreCase("paid")) {
                        finaldomain.add(rs.getString("emp_domain").trim().toLowerCase());
                    } else if (empType.equals("emp_contract")) {
                        finaldomain.add("supportgov.in");
                    } else if (empType.equals("consultant")) {
                        finaldomain.add("govcontractor.in");
                    } else {
                        finaldomain.add(rs.getString("emp_domain").trim().toLowerCase());
                    }
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "finaldomain " + finaldomain);
            if (finaldomain.isEmpty()) {
                if (empType.isEmpty()) {
                    return getnknDomain();
                } else {
                    String domain = LdapQuery.GetTotalDomain();
                    domain = domain.replace("associateddomain:", "");
                    domain = domain + ",nic.in";
                    if (domain.contains(",")) {
                        String[] token1 = domain.split(",");
                        for (String a : token1) {
                            if (a.trim().equalsIgnoreCase("nic.in @nic.in")) {
                                finaldomain.add("nic.in");
                            }
                            finaldomain.add(a.trim());
                        }
                    } else // if (!domain.contains("nic.in")) {
                    {
                        finaldomain.add(domain);
                    }
                }
            }
            // }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
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
        return finaldomain;
    }

    public Set<String> getnknDomain() {
        ResultSet rs = null;
        PreparedStatement pst = null;
        Set<String> domains = new HashSet<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            // get bean of profile
            pst = null;
            String sql = "select distinct domain from nkn_domain";
            pst = conSlave.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                domains.add(rs.getString("domain").trim().toLowerCase());
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
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
        return domains;
    }

    public boolean checkEmailFromTableSingleBulkGem(String email, String auth_email) {
        boolean response = false;
        System.out.println("inside dao check email from db");
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement pst = null;
            ResultSet rs = null;
            String registration_no = "";
            String sql = "select final_id,auth_email,registration_no from single_registration where final_id = ?";
            pst = conSlave.prepareStatement(sql);
            pst.setString(1, email);
            System.out.println("11111111111111111" + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                String regNo = rs.getString("registration_no");
                if (rs.getString("final_id").equals(email)) {
                    sql = "select ca_email from final_audit_track where registration_no =?";
                }
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, regNo);
                rs = pst.executeQuery();
                while (rs.next()) {

                    if (rs.getString("ca_email").equals(auth_email)) {
                        System.out.println("1111111111 in dao");
                        response = true;
                        break;
                    }
                }
            }

            if (!response) {
                sql = "select mail_assigned,registration_no from bulk_users where mail_assigned = ?";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, email);
                rs = pst.executeQuery();
                while (rs.next()) {
                    if ((rs.getString("mail_assigned").equals(email))) {
                        System.out.println("2222222222222 in dao");
                        registration_no = rs.getString("registration_no");
                        sql = "select ca_email,applicant_email from final_audit_track where registration_no =? and status='completed'";
                        pst = conSlave.prepareStatement(sql);
                        pst.setString(1, registration_no);
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            if (rs.getString("ca_email").equals(auth_email)) {
                                System.out.println("333333333 in dao");
                                response = true;
                                break;
                            }
                        }

                    }
                }
            }
            if (!response) {
                sql = "select final_id,auth_email,registration_no from gem_registration where final_id = ?";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, email);
                rs = pst.executeQuery();
                while (rs.next()) {
                    registration_no = rs.getString("registration_no");
                    if (rs.getString("final_id").equals(email)) {
                        sql = "select ca_email from final_audit_track where registration_no =?";
                        pst = conSlave.prepareStatement(sql);
                        pst.setString(1, registration_no);
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            if (rs.getString("ca_email").equals(auth_email)) {
                                System.out.println("444444444444 in dao");
                                response = true;
                                break;
                            }
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return response;
    }

    public boolean checkEmailFromTableSingleBulkGemNew(String userAliases, String caAliases) {
        boolean response = false;
        System.out.println("inside dao check email from db");
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement pst = null;
            ResultSet rs = null;
            String registration_no = "";
            String sql = "select final_id,auth_email,registration_no from single_registration where final_id in (" + userAliases + ")";
            pst = conSlave.prepareStatement(sql);
            System.out.println("11111111111111111" + pst);
            rs = pst.executeQuery();
            while (rs.next()) {
                String regNo = rs.getString("registration_no");
                sql = "select ca_email from final_audit_track where registration_no =?";
                pst = conSlave.prepareStatement(sql);
                pst.setString(1, regNo);
                rs = pst.executeQuery();
                while (rs.next()) {
                    if (caAliases.toLowerCase().contains(rs.getString("ca_email").toLowerCase())) {
                        System.out.println("1111111111 in dao");
                        response = true;
                        break;
                    }
                }
            }

            if (!response) {
                sql = "select mail_assigned,registration_no from bulk_users where mail_assigned in (" + userAliases + ")";
                pst = conSlave.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    registration_no = rs.getString("registration_no");
                    sql = "select ca_email,applicant_email from final_audit_track where registration_no =? and status='completed'";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, registration_no);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (caAliases.toLowerCase().contains(rs.getString("ca_email").toLowerCase())) {
                            System.out.println("333333333 in dao");
                            response = true;
                            break;
                        }
                    }
                }
            }
            if (!response) {
                sql = "select final_id,auth_email,registration_no from gem_registration where final_id in (" + userAliases + ")";
                pst = conSlave.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    registration_no = rs.getString("registration_no");
                    sql = "select ca_email from final_audit_track where registration_no =?";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, registration_no);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (caAliases.toLowerCase().contains(rs.getString("ca_email").toLowerCase())) {
                            System.out.println("444444444444 in dao");
                            response = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean checkEmailForCoord(String email, String auth_email) {
        boolean response = false;
        System.out.println("inside dao checkEmailForCoord from db");
        Map profile_values = new HashMap();
        profile_values = getUserValues(email);
        if (profile_values.get("user_employment") != null) {
            String employment = profile_values.get("user_employment").toString();
            String sql = "";
            try {
                conSlave = DbConnection.getSlaveConnection();
                PreparedStatement pst = null;
                ResultSet rs = null;

                if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                    sql = "select distinct emp_coord_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =?  order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile_values.get("min").toString());
                    pst.setString(3, profile_values.get("dept").toString());
                } else if (employment.equalsIgnoreCase("state")) {
                    sql = "select distinct emp_coord_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =? order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile_values.get("stateCode").toString());
                    pst.setString(3, profile_values.get("dept").toString());
                } else {
                    sql = "select distinct emp_coord_email from employment_coordinator where emp_category = ? and emp_min_state_org = ? order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile_values.get("Org").toString());
                }
                rs = pst.executeQuery();
                while (rs.next()) {
                    if (rs.getString("emp_coord_email").equals(auth_email)) {
                        response = true;
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return response;
    }

    public boolean checkEmailForCoordNew(String userAliases, String caAliases) {
        boolean response = false;
        System.out.println("inside dao checkEmailForCoord from db");
        Map profile_values = new HashMap();
        profile_values = getUserValuesNew(userAliases);
        if (profile_values.get("user_employment") != null) {
            String employment = profile_values.get("user_employment").toString();
            String sql = "";
            try {
                conSlave = DbConnection.getSlaveConnection();
                PreparedStatement pst = null;
                ResultSet rs = null;

                if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                    sql = "select distinct emp_coord_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =?  order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile_values.get("min").toString());
                    pst.setString(3, profile_values.get("dept").toString());
                } else if (employment.equalsIgnoreCase("state")) {
                    sql = "select distinct emp_coord_email from employment_coordinator where emp_category = ? and emp_min_state_org =? and emp_dept =? order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile_values.get("stateCode").toString());
                    pst.setString(3, profile_values.get("dept").toString());
                } else {
                    sql = "select distinct emp_coord_email from employment_coordinator where emp_category = ? and emp_min_state_org = ? order by emp_domain asc";
                    pst = conSlave.prepareStatement(sql);
                    pst.setString(1, employment);
                    pst.setString(2, profile_values.get("Org").toString());
                }
                rs = pst.executeQuery();
                while (rs.next()) {
                    if (caAliases.toLowerCase().contains(rs.getString("emp_coord_email").toLowerCase())){
                        response = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public Map getUserValues(String email) {
        Map profile_values = new HashMap();
        try {
            String sql = "select * from user_profile where email=?";
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, email);
            System.out.println("in get user values::::");
            rs = ps.executeQuery();
            if (rs.next()) {
                profile_values.put("user_employment", rs.getString("employment").trim());
                profile_values.put("min", rs.getString("ministry").trim());
                profile_values.put("dept", rs.getString("department").trim());
                profile_values.put("stateCode", rs.getString("state").trim());
                profile_values.put("Org", rs.getString("organization").trim());
                profile_values.put("other_dept", rs.getString("other_dept").trim());

            }
        } catch (Exception e) {
        }

        return profile_values;
    }
    
    public Map getUserValuesNew(String aliases) {
        Map profile_values = new HashMap();
        try {
            String sql = "select * from user_profile where email in ("+aliases+")";
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = conSlave.prepareStatement(sql);
            System.out.println("in get user values::::");
            rs = ps.executeQuery();
            if (rs.next()) {
                profile_values.put("user_employment", rs.getString("employment").trim());
                profile_values.put("min", rs.getString("ministry").trim());
                profile_values.put("dept", rs.getString("department").trim());
                profile_values.put("stateCode", rs.getString("state").trim());
                profile_values.put("Org", rs.getString("organization").trim());
                profile_values.put("other_dept", rs.getString("other_dept").trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profile_values;
    }

    public String fetchCoordinator(String dept) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "enter in fetchCoordinator ");
        List emails = new ArrayList<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String sql = "select emp_coord_email from employment_coordinator where emp_dept=? AND emp_type IN('c','dc') AND emp_coord_email NOT LIKE '%support%' and emp_status='a' limit 3";
            PreparedStatement ps = null;
            ResultSet rs = null;
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, dept);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps::::: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                emails.add(rs.getString("emp_coord_email"));
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "emails::::: " + emails.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emails.toString();
    }

    public void dor_ext_tab1(FormBean form_details) {
        //Connection con = null;
        PreparedStatement pst = null;
        try {

            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into onlineform_temp_db.dor_ext_registration_tmp (email,dor,ip)"
                    + "values (?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getSingle_email1());
            pst.setString(2, form_details.getSingle_dor());
            pst.setString(3, ip);
            pst.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
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
    }

    public String dor_ext_tab2(FormBean form_details, Map profile_values, String emailReq, String uploaded_filename, String renamed_filepath) {
        String dbrefno = "", newref = "";
        Boolean errflag = false;
        //Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;

            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from dor_ext_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            ;
            String table = "";

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
            table = "dor_ext_registration";
            newref = "DOREXT-FORM" + pdate1 + newref;
            String category = "";
            String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dor_email,dor,"
                    + " registration_no,userip,ca_desig,pre_dor,uploaded_filename,renamed_filepath,id_type,emp_type,dob) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getDor_email());
            pst.setString(23, form_details.getSingle_dor());
            pst.setString(24, newref);
            pst.setString(25, ip);
            pst.setString(26, form_details.getCa_design().trim());
            pst.setString(27, form_details.getP_single_dor());
            pst.setString(28, uploaded_filename);
            pst.setString(29, renamed_filepath);
            pst.setString(30, form_details.getSingle_id_type());
            pst.setString(31, form_details.getSingle_emp_type());
            pst.setString(32, form_details.getSingle_dob());

            int i = pst.executeUpdate();

        } catch (Exception e) {
            errflag = true;
            e.printStackTrace();
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                int newrefno;

                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from dor_ext_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
                pst = conSlave.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                ;
                String table = "";
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
                table = "dor_ext_registration";
                newref = "DOREXT-FORM" + pdate1 + newref;
                String category = "";
                String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,dor_email,dor,"
                        + " registration_no,userip,ca_desig,pre_dor,uploaded_filename,renamed_filepath,id_type,emp_type,dob) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getDor_email());
                pst.setString(23, form_details.getSingle_dor());
                pst.setString(24, newref);
                pst.setString(25, ip);
                pst.setString(26, form_details.getCa_design().trim());
                pst.setString(27, form_details.getP_single_dor());
                pst.setString(28, uploaded_filename);
                pst.setString(29, renamed_filepath);
                pst.setString(30, form_details.getSingle_id_type());
                pst.setString(31, form_details.getSingle_emp_type());
                pst.setString(32, form_details.getSingle_dob());

                int i = pst.executeUpdate();

            } catch (Exception e) {
                errflag = true;
                // e.printStackTrace();
            } finally {
                if (rs1 != null) {
                    try {
                        rs1.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs2 != null) {
                    try {
                        rs2.close();
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

    public String dor_ext_retired_tab2(UserData userdata, String oldDor, String newDor, String newref) {
        String dbrefno = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        String excpn = "";

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;

            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

//            if (erroWhileInsertingDataForRetired) {
//                dbrefno = newref;
//            } else {
            String search_regnum = "select registration_no from dor_ext_retired_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
                //  dbrefno = "DOREXTRT-FORM202208010008";
            }
            // }

            if (dbrefno == null || dbrefno.isEmpty()) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                String lastst = dbrefno.substring(22, dbrefno.length());
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
            String table = "dor_ext_retired_registration";
            newref = "DOREXTRT-FORM" + pdate1 + newref;
            String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,designation,dor_email,new_dor,prev_dor,"
                    + "registration_no,userip,form_type) values (?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, userdata.getEmail());
            pst.setString(2, userdata.getMobile());
            pst.setString(3, userdata.getName());
            pst.setString(4, userdata.getDesignation());
            pst.setString(5, userdata.getEmail());
            pst.setString(6, newDor);
            pst.setString(7, oldDor);
            pst.setString(8, newref);
            pst.setString(9, ip);
            pst.setString(10, "dor_ext_retired");
            int i = pst.executeUpdate();
//            if (i < 0) {
//                erroWhileInsertingDataForRetired = true;
//                dor_ext_retired_tab2(userdata, oldDor, newDor, newref);
//            }
            // inserted_reg_number = newref;
        } catch (Exception e) {
            e.printStackTrace();
            excpn = e.getMessage();
            System.out.println("dor_ext_retired_tab2--Inside catch block : " + excpn);
//            erroWhileInsertingDataForRetired = true; 
//            dor_ext_retired_tab2(userdata, oldDor, newDor, newref);
//            erroWhileInsertingDataForRetired = false; 
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return newref;
    }

    public String dor_ext_retired_tab2_new(String email, String name, String mobile, String oldDor, String newDor) {
        String dbrefno = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        String excpn = "", newref = "";

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            int newrefno;

            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);

            String search_regnum = "select registration_no from dor_ext_retired_registration where date(datetime)=date(now()) group by registration_no order by registration_no  desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }

            if (dbrefno == null || dbrefno.isEmpty()) {
                newrefno = 1;
                newref = "000" + newrefno;
            } else {
                String lastst = dbrefno.substring(22, dbrefno.length());
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
            String table = "dor_ext_retired_registration";
            newref = "DOREXTRT-FORM" + pdate1 + newref;
            String sql = "insert into " + table + " (auth_email,mobile,auth_off_name,designation,dor_email,new_dor,prev_dor,"
                    + "registration_no,userip,form_type) values (?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, email);
            pst.setString(2, mobile);
            pst.setString(3, name);
            pst.setString(4, "");
            pst.setString(5, email);
            pst.setString(6, newDor);
            pst.setString(7, oldDor);
            pst.setString(8, newref);
            pst.setString(9, ip);
            pst.setString(10, "dor_ext_retired");
            int i = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            excpn = e.getMessage();
            System.out.println("dor_ext_retired_tab2--Inside catch block : " + excpn);
        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
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
        return newref;
    }

    public int addEmailCampaign(int random, UserData userdata, String renamed_filepath, String uploaded_filename, String whichform, String idType, String empType) {   // Added By Manikant pandey
        int result = -1;
        if (renamed_filepath != null && uploaded_filename != null) {
            result = insertIntoEmailCampaign(random, userdata, renamed_filepath, uploaded_filename, whichform, idType, empType);
            while (result <= 0) {
                Random rand = new Random();
                random = rand.nextInt(1000000000);
                result = insertIntoEmailCampaign(random, userdata, renamed_filepath, uploaded_filename, whichform, idType, empType);
            }
        } else {
            random = result;
        }
        return random;
    }

    //added by pkr and manikant
    public int insertIntoEmailCampaign(int random, UserData userdata, String renamed_filepath, String uploaded_filename, String whichform, String idType, String empType) {
        PreparedStatement pst = null;
        int result = -1;
        String qry = "";
        try {
            con = DbConnection.getConnection();
            qry = "insert into email_bulk_campaigns (id,form_name,id_type,emp_type,owner_name,owner_email,file_path,uploaded_file) values(?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(qry);
            if (pst != null && userdata != null) {
                pst.setInt(1, random);
                pst.setString(2, whichform);
                pst.setString(3, idType);
                pst.setString(4, empType);
                pst.setString(5, userdata.getName());
                pst.setString(6, userdata.getEmail());
                pst.setString(7, renamed_filepath);
                pst.setString(8, uploaded_filename);
                result = pst.executeUpdate();

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
        return result;

    }

    public void addValidRecordsInBulkTable(EmailBean formbean, int random, String form_type, String tableName) { // Added By Manikant pandey
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            String sql = "insert into " + tableName + " ";
            sql += "(campaign_id,form_type, fname, lname, designation,department, state, mobile, dor, uid, mail, dob, emp_code, acc_cat, updatedon) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
            pst = con.prepareStatement(sql);
            if (pst != null) {
                pst.setInt(1, random);
                pst.setString(2, form_type);
                pst.setString(3, (formbean.getFname().isEmpty()) ? "N/A" : formbean.getFname());
                pst.setString(4, (formbean.getLname().isEmpty()) ? "N/A" : formbean.getLname());
                pst.setString(5, (formbean.getDesignation().isEmpty()) ? "N/A" : formbean.getDesignation());
                pst.setString(6, (formbean.getDepartment().isEmpty()) ? "N/A" : formbean.getDepartment());
                pst.setString(7, (formbean.getState().isEmpty()) ? "N/A" : formbean.getState());
                pst.setString(8, '+' + formbean.getCountry_code() + formbean.getMobile());
                pst.setString(9, (formbean.getDor().isEmpty()) ? "N/A" : formbean.getDor());
                pst.setString(10, (formbean.getUid().isEmpty()) ? "N/A" : formbean.getUid());
                pst.setString(11, (formbean.getMail().isEmpty()) ? "N/A" : formbean.getMail());
                pst.setString(12, formbean.getDob());
                pst.setString(13, formbean.getEmpcode());
                pst.setString(14, formbean.getAcc_cat());

            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
            if (pst != null) {
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
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

    public void addInvalidRecordsInBulkTable(EmailBean formbean, ArrayList errMsg, int random, String form_type, String tableName) { // Added By Manikant pandey
        PreparedStatement pst = null;
        try {
            String[] str = new String[errMsg.size()];
            for (int i = 0; i < errMsg.size(); i++) {
                str[i] = (String) errMsg.get(i);
            }
            for (String error : str) {
                System.out.println(error);

                con = DbConnection.getConnection();
                String sql = "insert into " + tableName + " ";
                sql += "(campaign_id,form_type, fname, lname,designation,department, state, mobile, dor, uid,mail,dob,emp_code,acc_cat,email_error,error_status,updatedon) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";

                pst = con.prepareStatement(sql);
                if (pst != null) {
                    pst.setInt(1, random);
                    pst.setString(2, form_type);
                    pst.setString(3, (formbean.getFname().isEmpty()) ? "N/A" : formbean.getFname());
                    pst.setString(4, (formbean.getLname().isEmpty()) ? "N/A" : formbean.getLname());
                    pst.setString(5, (formbean.getDesignation().isEmpty()) ? "N/A" : formbean.getDesignation());
                    pst.setString(6, (formbean.getDepartment().isEmpty()) ? "N/A" : formbean.getDepartment());
                    pst.setString(7, (formbean.getState().isEmpty()) ? "N/A" : formbean.getState());
                    pst.setString(8, '+' + formbean.getCountry_code() + formbean.getMobile());
                    pst.setString(9, (formbean.getDor().isEmpty()) ? "N/A" : formbean.getDor());
                    pst.setString(10, (formbean.getUid().isEmpty()) ? "N/A" : formbean.getUid());
                    pst.setString(11, (formbean.getMail().isEmpty()) ? "N/A" : formbean.getMail());
                    pst.setString(12, formbean.getDob());
                    pst.setString(13, formbean.getEmpcode());
                    pst.setString(14, formbean.getAcc_cat());
                    pst.setString(15, error);
                    pst.setString(16, "1");
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps tab3: " + pst);
            if (pst != null) {
                pst.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
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

    public Map<String, Object> fetchBulkUploadData(int campaignId) {
        Map<String, Object> bulkData = new HashMap<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            System.out.println("fetchSuccessfulBulkUploadData:::: campaignId:: " + campaignId);
            List<EmailBean> validData = fetchSuccessfulBulkUploadData(campaignId);
            bulkData.put("validRecord", validData);
            List<EmailBean> invalidData = fetchUnSuccessfulBulkUploadData(campaignId);
            bulkData.put("invalidRecord", invalidData);
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT * FROM `email_bulk_campaigns` WHERE `id` = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
            }
            Map<String, String> tmpRecord = new HashMap<>();
            if (rs1.next()) {
                tmpRecord.put("campaignId", rs1.getString("id"));
                tmpRecord.put("uploaded_file", rs1.getString("uploaded_file"));
                tmpRecord.put("renamed_file", rs1.getString("file_path"));
                tmpRecord.put("submitted_at", rs1.getString("submitted_at"));
                tmpRecord.put("owner_name", rs1.getString("owner_name"));
                tmpRecord.put("owner_email", rs1.getString("owner_email"));
            }
            bulkData.put("formDetails", tmpRecord);
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return bulkData;
    }

    public List<EmailBean> fetchSuccessfulBulkUploadData(int campaignId) {
        List<EmailBean> bulkData = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = Constants.BULK_USER;
            String qry = "";
            qry = "select bulk_id,campaign_id,registration_no,fname,lname,state,mail,designation,department,mobile,dor,dob,emp_code from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    EmailBean emailData = new EmailBean();
                    emailData.setId(rs1.getInt("bulk_id"));
                    emailData.setCampaignId(campaignId);
                    emailData.setFname(rs1.getString("fname"));
                    emailData.setLname(rs1.getString("lname"));
                    emailData.setMail(rs1.getString("mail"));
                    emailData.setDepartment(rs1.getString("department"));
                    emailData.setDesignation(rs1.getString("designation"));
                    emailData.setMobile(rs1.getString("mobile"));
                    emailData.setState(rs1.getString("state"));
                    emailData.setDor(rs1.getString("dor"));
                    emailData.setDob(rs1.getString("dob"));
                    emailData.setEmpcode(rs1.getString("emp_code"));
                    bulkData.add(emailData);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return bulkData;

    }

    public List<EmailBean> fetchUnSuccessfulBulkUploadData(int campaignId) {
        List<EmailBean> bulkData = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = Constants.BULK_USER;
            String qry = "";
            qry = "select bulk_id,campaign_id,registration_no,fname,lname,state,mail,designation,department,mobile,dor,dob,emp_code,email_error from " + tableName + " where campaign_id=? AND error_status='1' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                System.out.println("com.org.dao.DnsDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    EmailBean emailData = new EmailBean();
                    emailData.setId(rs1.getInt("bulk_id"));
                    emailData.setCampaignId(campaignId);
                    emailData.setFname(rs1.getString("fname"));
                    emailData.setLname(rs1.getString("lname"));
                    emailData.setMail(rs1.getString("mail"));
                    emailData.setDepartment(rs1.getString("department"));
                    emailData.setDesignation(rs1.getString("designation"));
                    emailData.setMobile(rs1.getString("mobile"));
                    emailData.setState(rs1.getString("state"));
                    emailData.setDor(rs1.getString("dor"));
                    emailData.setDob(rs1.getString("dob"));
                    emailData.setEmpcode(rs1.getString("emp_code"));
                    emailData.setErrorMessage(rs1.getString("email_error"));
                    bulkData.add(emailData);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return bulkData;
    }

    public int fetchCountOfSuccessfulRecords(int campaignId) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int i = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select count(*) as count from bulk_users where campaign_id=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                System.out.println("com.org.dao.emailDao.fetchSuccessBulkData() PST::: " + pst);
                rs1 = pst.executeQuery();
                while (rs1.next()) {
                    i = rs1.getInt("count");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return i;

    }

    // added by manikant 07/09/2022 
    public Map<String, Object> fetchSuccessBulkData(int campaignId) {
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Map<String, Object> validData = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            List<EmailBean> bulkData = null;
            tableName = Constants.BULK_USER;
            qry = "select bulk_id,campaign_id,fname,lname,dor,dob,state,mail,department,designation,mobile from " + tableName + " where campaign_id=? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                System.out.println("fetchSuccessBulkData PST::: " + pst);
                rs1 = pst.executeQuery();
                bulkData = new ArrayList<>();
                while (rs1.next()) {
                    EmailBean emaildata = new EmailBean();
                    emaildata.setId(rs1.getInt("bulk_id"));
                    emaildata.setCampaignId(campaignId);
                    emaildata.setFname(rs1.getString("fname"));
                    emaildata.setLname(rs1.getString("lname"));
                    emaildata.setMail(rs1.getString("mail"));
                    emaildata.setState(rs1.getString("state"));
                    emaildata.setDob(rs1.getString("dob"));
                    emaildata.setDor(rs1.getString("dor"));
                    emaildata.setDepartment(rs1.getString("department"));
                    emaildata.setDesignation(rs1.getString("designation"));
                    emaildata.setMobile(rs1.getString("mobile"));
                    bulkData.add(emaildata);
                }
            }
            validData.put("validRecord", bulkData);
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return validData;
    }

    public int updateRegNumbersInBulkTable(String regNumber, int campaignId, String category) {
        PreparedStatement pst = null;
        int result = -1;
        String qry = "";
        try {
            con = DbConnection.getConnection();
            String tableName = Constants.BULK_USER;
            qry = "update " + tableName + " set registration_no=?, acc_cat=? where campaign_id=?";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regNumber);
                pst.setString(2, category);
                pst.setInt(3, campaignId);
                System.out.println("PST :::: " + pst);
                result = pst.executeUpdate();
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
        return result;

    }

    public int updateEmailCampaign(int id, String regNumber) {
        PreparedStatement pst = null;
        int result = -1;
        String qry = "";
        try {
            con = DbConnection.getConnection();
            qry = "update email_bulk_campaigns set registration_no=?, status=? where id=?";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regNumber);
                pst.setInt(2, 1);
                pst.setInt(3, id);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql " + "e: " + pst);
                result = pst.executeUpdate();
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
        return result;

    }

    public String EmaildiscardCampaign(int camaignId) {
        PreparedStatement pst = null;
        String responseResult = "";
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE email_bulk_campaigns set discard_status=1 WHERE id=? and discard_status=0";
            pst = con.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, camaignId);
                int i = pst.executeUpdate();
                if (i > 0) {
                    responseResult = "Campaign Discard Successfully.";
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
        return responseResult;

    }

    public Map<String, Object> fetchCompleteCampaignData(Set<String> aliases) {
        Map<String, Object> bulkCampData = new HashMap<>();
        try {
            String pending_qry = "SELECT id,uploaded_file,file_path,submitted_at FROM `email_bulk_campaigns` WHERE owner_email = ? and status='0' and discard_status='0'";
            bulkCampData.put("pending", fetchSeprateCampData(pending_qry, aliases));

            String completed_qry = "SELECT id,uploaded_file,file_path,submitted_at FROM `email_bulk_campaigns` WHERE owner_email = ? and status='1' and discard_status='0'";
            bulkCampData.put("complete", fetchSeprateCampData(completed_qry, aliases));

            String discard_qry = "SELECT id,uploaded_file,file_path,submitted_at FROM `email_bulk_campaigns` WHERE owner_email = ? and status='0' and discard_status='1'";
            bulkCampData.put("discard", fetchSeprateCampData(discard_qry, aliases));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bulkCampData;

    }

    public Object fetchSeprateCampData(String qry, Set<String> aliases) {
        List<Map<String, String>> campaigndata = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            for (String email : aliases) {
                pst = conSlave.prepareStatement(qry);
                if (pst != null) {
                    pst.setString(1, email);
                    rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        Map<String, String> tmpRecord = new HashMap<>();
                        tmpRecord.put("id", rs1.getString("id"));
                        tmpRecord.put("uploaded_file", rs1.getString("uploaded_file"));
                        tmpRecord.put("renamed_file", rs1.getString("file_path"));
                        tmpRecord.put("submitted_at", rs1.getString("submitted_at"));
                        campaigndata.add(tmpRecord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return campaigndata;
    }

    public EmailBean fetchSingleBulkRecord(int emailbulkDataId, int campaignId) {
        EmailBean emaildata = new EmailBean();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            String tableName = "";
            tableName = Constants.BULK_USER;
            qry = "SELECT * from " + tableName + " where bulk_id=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, emailbulkDataId);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    emaildata.setId(emailbulkDataId);
                    emaildata.setFname(rs1.getString("fname"));
                    emaildata.setLname(rs1.getString("lname"));
                    emaildata.setMail(rs1.getString("mail"));
                    emaildata.setState(rs1.getString("state"));
                    emaildata.setDesignation(rs1.getString("designation"));
                    emaildata.setDepartment(rs1.getString("department"));
                    emaildata.setMobile(rs1.getString("mobile"));
                    emaildata.setCampaignId(rs1.getInt("campaign_id"));
                    emaildata.setRegistration_No(rs1.getString("registration_no"));
                    //emaildata.setDob(rs1.getString("dob"));
                    emaildata.setDor(rs1.getString("dor"));
                    //emaildata.setEmpcode(rs1.getString("emp_code"));
                    emaildata.setUid(rs1.getString("uid"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }

        return emaildata;

    }

    public int updateBulkEditTable(EmailBean emailEditData, String errMsg, boolean error, boolean duplicate, boolean toBeDeleted, String request_type) {
        int result = -1;
        PreparedStatement pst = null;
        String qry = "";
        int errorStatus, deleteStatus;
        int id = emailEditData.getId();

        if (toBeDeleted) {
            if (!error) {
                errMsg = "";
                errorStatus = 0;
                deleteStatus = 1;
            } else {
                errorStatus = 1;
                deleteStatus = 1;
            }
        } else {
            if (!error) {
                errMsg = "";
                errorStatus = 0;
                deleteStatus = 0;
            } else {
                errorStatus = 1;
                deleteStatus = 0;
            }
        }
        try {
            con = DbConnection.getConnection();
            String tableName = "";
            tableName = Constants.BULK_USER;
            if (!toBeDeleted) {
                qry = " update " + tableName + " set fname=?,lname=?,designation=?,department=?,state =?, mobile=?,dor=?,uid=?,mail=?,dob=?,emp_code=?,email_error=?, error_status=?,delete_status=? where bulk_id=?";
                pst = con.prepareStatement(qry);
                if (pst != null) {
                    pst.setString(1, (emailEditData.getFname().isEmpty()) ? "N/A" : emailEditData.getFname());
                    pst.setString(2, (emailEditData.getLname().isEmpty()) ? "N/A" : emailEditData.getLname());
                    pst.setString(3, (emailEditData.getDesignation().isEmpty()) ? "N/A" : emailEditData.getDesignation());
                    pst.setString(4, (emailEditData.getDepartment().isEmpty()) ? "N/A" : emailEditData.getDepartment());
                    pst.setString(5, (emailEditData.getState().isEmpty()) ? "N/A" : emailEditData.getState());
                    pst.setString(6, (emailEditData.getMobile()));
                    pst.setString(7, (emailEditData.getDor().isEmpty()) ? "N/A" : emailEditData.getDor());
                    pst.setString(8, (emailEditData.getUid().isEmpty()) ? "N/A" : emailEditData.getUid());
                    pst.setString(9, (emailEditData.getMail().isEmpty()) ? "N/A" : emailEditData.getMail());
                    pst.setString(10, emailEditData.getDob());
                    pst.setString(11, emailEditData.getEmpcode());
                    pst.setString(12, errMsg);
                    pst.setInt(13, errorStatus);
                    pst.setInt(14, deleteStatus);
                    pst.setInt(15, id);
                }
            }
            if (toBeDeleted) {
                qry = "update " + tableName + " set error_status=?, delete_status=? where bulk_id=?";
                pst = con.prepareStatement(qry);
                if (pst != null) {
                    pst.setInt(1, errorStatus);
                    pst.setInt(2, deleteStatus);
                    pst.setInt(3, id);
                }
            }

            System.out.println("PST :::: " + pst);
            if (pst != null) {
                result = pst.executeUpdate();
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

        return result;
    }

    public FormBean fetchFormDetails(int campaignId) {
        FormBean form_detail = new FormBean();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT * FROM `email_bulk_campaigns` WHERE `id` = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    form_detail.setUser_name(rs1.getString("owner_name"));
                    form_detail.setUser_email(rs1.getString("owner_email"));
                    form_detail.setUploaded_filename(rs1.getString("uploaded_file"));
                    form_detail.setRenamed_filepath(rs1.getString("file_path"));
                    form_detail.setSubmittedAt("submitted_at");
                    form_detail.setSingle_id_type(rs1.getString("id_type"));
                    form_detail.setSingle_emp_type(rs1.getString("emp_type"));
                    form_detail.setFormid(rs1.getString("form_name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return form_detail;

    }

    public boolean duplicateRecord(EmailBean emailEditData, int campaignId) { // Added By Manikant pandey
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        boolean flag = false;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            String tableName = "";
            tableName = Constants.BULK_USER;
            qry = "SELECT * from " + tableName + " where campaign_id=? and fname=? and lname=? and error_status=? and delete_status=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, emailEditData.getFname());
                pst.setString(3, emailEditData.getLname());
//                pst.setString(4, emailEditData.getMail());
//                pst.setString(5, emailEditData.getUid());
//                pst.setString(6, emailEditData.getState());
//                pst.setString(7, emailEditData.getDepartment());
//                pst.setString(8, emailEditData.getDesignation());
//                pst.setString(9, emailEditData.getDob());
//                pst.setString(10, emailEditData.getMobile());
//                pst.setString(11, emailEditData.getEmpcode());
                pst.setInt(4, 0);
                pst.setInt(5, 0);
            }
            if (pst != null) {
                rs1 = pst.executeQuery();
                System.out.println("com.org.dao.EmailDao.duplicateRecord()::::::" + pst);

                if (rs1.next()) {
                    flag = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return flag;

    }

    public List<Integer> fetchIDOfDuplicateRecord(ValidatedEmailBean emailEditData, int campaignId) {  // Added By Manikant pandey
        PreparedStatement pst = null;
        ResultSet rs1 = null;

        List<Integer> ids = new ArrayList<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            String tableName = "";
            tableName = Constants.BULK_USER;
            qry = "SELECT bulk_id from " + tableName + " where campaign_id=? and mail=? and fname=? and error_status=? and delete_status=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setInt(1, campaignId);
                pst.setString(2, emailEditData.getMail());
                pst.setString(3, emailEditData.getFname());
                pst.setInt(4, 0);
                pst.setInt(5, 0);
            }
            if (pst != null) {
                rs1 = pst.executeQuery();

                while (rs1.next()) {
                    ids.add(rs1.getInt("bulk_id"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return ids;

    }

    public int fetchCampaignId(String regno) { // Added By Manikant pandey
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int camId = -1;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT id FROM `email_bulk_campaigns` WHERE registration_no = ?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regno);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    camId = rs1.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return camId;
    }

    public Object fetchSuccessBulkDataUsingReg(String reg_no) { // Added By Manikant pandey
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        int campaign_id = 0;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT id from email_bulk_campaigns where registration_no=?";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, reg_no);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    campaign_id = rs1.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return fetchSuccessBulkData(campaign_id);

    }

    public List<EmailBean> fetchSuccessBulkData(String regNumber) { // Added By Manikant pandey
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        List<EmailBean> bulkData = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            String tableName = "";
            String qry = "";
            tableName = Constants.BULK_USER;
            qry = "select bulk_id,fname,lname,state,mail,state,department,designation,mobile,dor from " + tableName + " where registration_no =? AND error_status='0' AND delete_status='0'";
            pst = conSlave.prepareStatement(qry);
            if (pst != null) {
                pst.setString(1, regNumber);
                System.out.println("fetchSuccessBulkData PST::: " + pst);
                rs1 = pst.executeQuery();
                bulkData = new ArrayList<>();
                while (rs1.next()) {
                    EmailBean emaildata = new EmailBean();
                    emaildata.setId(rs1.getInt("bulk_id"));
                    emaildata.setFname(rs1.getString("fname"));
                    emaildata.setLname(rs1.getString("lname"));
                    emaildata.setMail(rs1.getString("mail"));
                    emaildata.setState(rs1.getString("state"));
                    emaildata.setDepartment(rs1.getString("department"));
                    emaildata.setDesignation(rs1.getString("designation"));
                    emaildata.setMobile(rs1.getString("mobile"));
                    emaildata.setDor(rs1.getString("dor"));
                    bulkData.add(emaildata);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return bulkData;

    }

    public List<Map<String, String>> fetchEmailCampaigns(Set<String> aliases) { // Added By Manikant pandey
        List<Map<String, String>> campaigndata = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            for (String email : aliases) {
                String qry = "SELECT * FROM `email_bulk_campaigns` WHERE owner_email = ? and status='0' and discard_status='0'";
                pst = conSlave.prepareStatement(qry);
                if (pst != null) {
                    pst.setString(1, email);
                    rs1 = pst.executeQuery();
                    while (rs1.next()) {
                        Map<String, String> tmpRecord = new HashMap<>();
                        tmpRecord.put("id", rs1.getString("id"));
                        tmpRecord.put("uploaded_file", rs1.getString("uploaded_file"));
                        tmpRecord.put("renamed_file", rs1.getString("file_path"));
                        tmpRecord.put("submitted_at", rs1.getString("submitted_at"));
                        campaigndata.add(tmpRecord);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        return campaigndata;

    }

    public Map<String, String> fetchProfileFromBaseTable(String registrationNumber) { // not used
        String employment = "", ministry = "", department = "", emp_type = "";
        Boolean errflag = false;
        PreparedStatement pst = null;
        ResultSet rs1 = null, rs2 = null;
        Map<String, String> map = new HashMap<>();
        try {
            conSlave = DbConnection.getSlaveConnection();
            pst = null;
            String search_regnum = "select * from bulk_registration where registration_no = ?";
            pst = conSlave.prepareStatement(search_regnum);
            pst.setString(1, registrationNumber);
            rs1 = pst.executeQuery();
            if (rs1.next()) {

                employment = rs1.getString("employment");
                if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                    ministry = rs1.getString("ministry");
                    department = rs1.getString("department");
                    emp_type = rs1.getString("emp_type");
                } else if (employment.equalsIgnoreCase("state")) {
                    ministry = rs1.getString("state");
                    department = rs1.getString("department");
                    emp_type = rs1.getString("emp_type");
                } else {
                    ministry = rs1.getString("organization");
                    department = "";
                    emp_type = rs1.getString("emp_type");
                }
            }
            map.put("employment", employment);
            map.put("ministry", ministry);
            map.put("department", department);
            map.put("emp_type", emp_type);
        } catch (Exception e) {

        } finally {
            if (rs1 != null) {
                try {
                    rs1.close();
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
            if (con != null) {
                try {
                    // con.close();                                                                                                         
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return map;
    }
}
