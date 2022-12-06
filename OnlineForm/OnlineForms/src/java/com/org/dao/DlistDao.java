package com.org.dao;

import com.google.gson.Gson;
import com.itextpdf.text.log.SysoLogger;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.org.bean.ModerateBean;
import com.org.bean.OwnerBean;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import validation.DlistValidation;
import validation.validation;

public class DlistDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection conSlave = null;
    Connection con = null;
    Ldap ldap=new Ldap();
    public int DLIST_tab1(FormBean form_details) {
        int generatedKey = 0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "insert into onlineform_temp_db.distributionlist_registration_tmp (list_name,list_description,list_moderated,"
                    + "allowed_member_mail,other_member_mail,list_temp,valid_date,ip)"
                    + "values (?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql, pst.RETURN_GENERATED_KEYS);
            pst.setString(1, form_details.getList_name());
            pst.setString(2, form_details.getDescription_list());
            pst.setString(3, form_details.getList_mod());
            pst.setString(4, form_details.getAllowed_member());
            pst.setString(5, form_details.getNon_nicnet());
            pst.setString(6, form_details.getList_temp());
            pst.setString(7, form_details.getValidity_date());
            pst.setString(8, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DLIST dao tab 1: " + pst);
            pst.executeUpdate();
            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                generatedKey = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception  in DLIST dao tab 1: " + e.getMessage());
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

    public Map<String, Object> DLIST_tab2(Map profile_values, FormBean form_details, String generated_key) {
        Map<String, Object> prvwdetails = new HashMap<String, Object>();
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "";
            sql = "update onlineform_temp_db.distributionlist_registration_tmp set moderator_name=?,moderator_email=?,moderator_mobile=?"
                    + " where id=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getT_off_name());
            pst.setString(2, form_details.getTauth_email());
            pst.setString(3, form_details.getTmobile());
            pst.setString(4, generated_key);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DLIST dao tab 2: " + pst);
            pst.executeUpdate();
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
        return prvwdetails;
    }

    public String DLIST_tab3(FormBean form_details, Map profile_values , Map bulkData) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        //Connection con = null;
        Boolean errflag = false;
        try {
            
           System.out.println("bulkDataaa" + bulkData.get("moderatorData"));
           System.out.println("bulkDataaa" + bulkData.get("ownerData"));
           System.out.println("start process of insertion into DLIST Base Table: ");
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from distribution_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
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
            newref = "DLIST-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in DLIST dao 3: " + newref);
            String sql = "insert into distribution_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                    + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                    + "list_name,list_description,list_moderated,allowed_member_mail,other_member_mail,list_temp,valid_date,moderator_name,moderator_email,moderator_mobile,"
                    + "registration_no,userip,ca_desig,member_count) "
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
            pst.setString(22, form_details.getList_name());
            pst.setString(23, form_details.getDescription_list());
            pst.setString(24, form_details.getList_mod());
            pst.setString(25, form_details.getAllowed_member());
            pst.setString(26, form_details.getNon_nicnet());
            pst.setString(27, form_details.getList_temp());
            if (form_details.getList_temp().equalsIgnoreCase("yes")) {
                pst.setString(28, form_details.getValidity_date());
            } else {
                pst.setString(28, "");
            }
            pst.setString(29, form_details.getT_off_name());
            pst.setString(30, form_details.getTauth_email());
            pst.setString(31, form_details.getTmobile());
            pst.setString(32, newref);
            pst.setString(33, ip);
            pst.setString(34, form_details.getCa_design().trim());
            pst.setString(35, form_details.getMemberCount());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DLIST dao tab 3: " + pst);
            int i = pst.executeUpdate();
            if(i > 0){
                  insertModDetails(newref,bulkData);
               System.out.println("DAOOOOOOOOOOOOOOO::: " + bulkData);
              // System.out.println("DAOOOOOOOOOOOOOOO::: " + set1);
                }
            // 20th march
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DLIST dao tab 3: " + e.getMessage());
            errflag = true;
            newref = "";
            //e.printStackTrace();
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
                conSlave = DbConnection.getSlaveConnection();
                con = DbConnection.getConnection();
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from distribution_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
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
                newref = "DLIST-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in DLIST dao 3: " + newref);
                String sql = "insert into distribution_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                        + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                        + "list_name,list_description,list_moderated,allowed_member_mail,other_member_mail,list_temp,valid_date,moderator_name,moderator_email,moderator_mobile,"
                        + "registration_no,userip,ca_desig,member_count) "
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
                pst.setString(22, form_details.getList_name());
                pst.setString(23, form_details.getDescription_list());
                pst.setString(24, form_details.getList_mod());
                pst.setString(25, form_details.getAllowed_member());
                pst.setString(26, form_details.getNon_nicnet());
                pst.setString(27, form_details.getList_temp());
                if (form_details.getList_temp().equalsIgnoreCase("yes")) {
                    pst.setString(28, form_details.getValidity_date());
                } else {
                    pst.setString(28, "");
                }
                pst.setString(29, form_details.getT_off_name());
                pst.setString(30, form_details.getTauth_email());
                pst.setString(31, form_details.getTmobile());
                pst.setString(32, newref);
                pst.setString(33, ip);
                pst.setString(34, form_details.getCa_design().trim());
                pst.setString(35, form_details.getMemberCount());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DLIST dao tab 3: " + pst);
                int i = pst.executeUpdate();
                
                // 20th march
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DLIST dao tab 3: " + e.getMessage());
                //errflag = true;
                newref = "";
                e.printStackTrace();
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
    
        public String DLIST_Bulktab3(FormBean form_details, Map profile_values, Map dlistbulkData) {
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
      
        Boolean errflag = false;
        try {

            System.out.println("dlistBulkData" + dlistbulkData.get("excelFileData"));
    
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from bulk_distribution_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            pst = conSlave.prepareStatement(search_regnum);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                dbrefno = rs1.getString("registration_no");
            }
            if (dbrefno == null || dbrefno.equals("")) {
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
            newref = "BULKDLIST-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in DLIST dao 3: " + newref);
            String sql = "insert into bulk_distribution_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                    + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                    + "list_name,list_description,list_moderated,allowed_member_mail,other_member_mail,list_temp,valid_date,moderator_name,moderator_email,moderator_mobile,"
                    + "registration_no,userip,ca_desig) "
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
            pst.setString(22, "");
            pst.setString(23, "");
              
            pst.setString(24, "");
            pst.setString(25,"");
            pst.setString(26, form_details.getNon_nicnet());
            pst.setString(27, "");
            pst.setString(28, "");
            pst.setString(29, "");
            pst.setString(30, "");
            pst.setString(31, "");
            pst.setString(32, newref);
            pst.setString(33, ip);
            pst.setString(34, form_details.getCa_design().trim());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DLIST dao tab 3: " + pst);
            int i = pst.executeUpdate();
            if (i > 0) {
               insertModDetails1(newref, dlistbulkData);
                insertExcelFileDetails(newref,dlistbulkData);
                System.out.println("DAOOOOOOOO::: " + dlistbulkData);
                
            }
           
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DLIST dao tab 3: " + e.getMessage());
            errflag = true;
            newref = "";
            
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
                conSlave = DbConnection.getSlaveConnection();
                con = DbConnection.getConnection();
                int newrefno;
                Date date1 = new Date();
                DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
                String pdate1 = dateFormat1.format(date1);
                String search_regnum = "select registration_no from bulk_distribution_registration where date(datetime)=date(now())";
                pst = conSlave.prepareStatement(search_regnum);
                rs1 = pst.executeQuery();
                if (rs1.next()) {
                    dbrefno = rs1.getString("registration_no");
                }
                if (dbrefno == null || dbrefno.equals("")) {
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
                newref = "BULKDLIST-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in DLIST dao 3: " + newref);
                String sql = "insert into bulk_distribution_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,"
                        + "city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,"
                        + "list_name,list_description,list_moderated,allowed_member_mail,other_member_mail,list_temp,valid_date,moderator_name,moderator_email,moderator_mobile,"
                        + "registration_no,userip,ca_desig) "
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
            pst.setString(22, "");
            pst.setString(23, "");
              
            pst.setString(24, "");
            pst.setString(25,"");
            pst.setString(26, form_details.getNon_nicnet());
            pst.setString(27, "");
            pst.setString(28, "");
            pst.setString(29, "");
            pst.setString(30, "");
            pst.setString(31, "");
            pst.setString(32, newref);
            pst.setString(33, ip);
            pst.setString(34, form_details.getCa_design().trim());
           
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DLIST dao tab 3: " + pst);
                int i = pst.executeUpdate();

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in DLIST dao tab 3: " + e.getMessage());
                //errflag = true;
                newref = "";
                e.printStackTrace();
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

    public Map validateBulkExcelile(String renamed_filepath, String whichform, UserData userdata) throws FileNotFoundException, IOException 
       {
        Map ExcelValidate = new HashMap<>();
        List cellRecords=new ArrayList();
        Map values = new HashMap();
        DlistValidation dlistValidate = new DlistValidation();
     
        ArrayList errorList = new ArrayList();
         ArrayList nonvalid = new ArrayList();
        try {
            File file = new File(renamed_filepath);   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
           int lastRow=sheet.getLastRowNum();   
           int b = sheet.getFirstRowNum();
           
           // if excel sheet is blanck
           if(sheet.getRow(b)!=null){
                Cell ln = wb.getSheetAt(0).getRow(0).getCell(0);  //first cell List name
                Cell ld = wb.getSheetAt(0).getRow(0).getCell(1);
                 String s0=ln.toString();
                 String s1=ld.toString();                           
               // for remoove heading of file
                if((s0.contains("List name")||s0.equalsIgnoreCase("List name"))==false){
                   
                     int k;
                    for ( k = 0; k <= lastRow; k++) {
                     boolean flag = true;
                        Cell c0 = wb.getSheetAt(0).getRow(k).getCell(0);  //first cell List name
                        Cell c1 = wb.getSheetAt(0).getRow(k).getCell(1);
                        Cell c2 = wb.getSheetAt(0).getRow(k).getCell(2);
                        Cell c3 = wb.getSheetAt(0).getRow(k).getCell(3);
                        Cell c4 = wb.getSheetAt(0).getRow(k).getCell(4);
                        Cell c5 = wb.getSheetAt(0).getRow(k).getCell(5);
                        Cell c6 = wb.getSheetAt(0).getRow(k).getCell(6);
                        Cell c7 = wb.getSheetAt(0).getRow(k).getCell(7);
                        Cell c8 = wb.getSheetAt(0).getRow(k).getCell(8);
                        c8.setCellType(c8.CELL_TYPE_STRING);
                        Cell c9 = wb.getSheetAt(0).getRow(k).getCell(9);
                        Cell c10 = wb.getSheetAt(0).getRow(k).getCell(10);
                        Cell c11 = wb.getSheetAt(0).getRow(k).getCell(11);
                        c11.setCellType(c11.CELL_TYPE_STRING);
                        Cell c12 = wb.getSheetAt(0).getRow(k).getCell(12);
                        Cell c13 = wb.getSheetAt(0).getRow(k).getCell(13);
                             
                        FormBean form_details = new FormBean();

//                        form_details.setList_name(c0.toString());
//                        form_details.setDescription_list(c1.toString());  // commented by rahul june 2021
                        if(c0==null){
                          form_details.setList_name("");
                        }
                        else{
                             form_details.setList_name(c0.toString());
                        }
                         if(c1==null){
                          form_details.setDescription_list("");
                        }
                        else{
                             form_details.setDescription_list(c1.toString());
                        }
                        if(c2==null){
                        form_details.setList_mod("");
                        }
                        else{
                        form_details.setList_mod(c2.toString());
                        }
                        if(c3==null){
                        form_details.setAllowed_member("");
                        }
                        else{
                        form_details.setAllowed_member(c3.toString());
                        }
                        if(c4==null){
                        form_details.setList_temp("");
                        }
                        else{
                        form_details.setList_temp(c4.toString());
                        }
                        if(c5==null){
                        form_details.setMail_Acceptance("");
                        }
                        else{
                        form_details.setMail_Acceptance(c5.toString());
                        }
//                        form_details.setAllowed_member(c3.toString());
//                        form_details.setList_temp(c4.toString());
//                        form_details.setMail_Acceptance(c5.toString());   //code commented by rahul june 2021

                      if(c6==null){
                          form_details.setOwner_Name("");
                      }
                      else{
                        form_details.setOwner_Name(c6.toString());
                      }
                      if(c7==null){
                          form_details.setOwner_Email("");
                      }
                      else{
                        form_details.setOwner_Email(c7.toString());
                      }
                      if(c8==null){
                          form_details.setOwner_Mobile("");
                      }
                      else{
                        form_details.setOwner_Mobile(c8.toString());
                      }
                        if(c9==null)
                        {
                         form_details.setModerator_Name("");
                        }
                        else{
                        form_details.setModerator_Name(c9.toString());
                        }
                        if(c10==null)
                        {
                         form_details.setModerator_Email("");
                        }
                        else{
                        form_details.setModerator_Email(c10.toString());
                        }
                        if(c11==null)
                        {
                         form_details.setModerator_Mobile("");
                        }
                        else{
                        form_details.setModerator_Mobile(c11.toString());
                        }      
                        
                        if(c12==null)
                        {
                         form_details.setOwner_Admin("");
                        }
                        else{
                        form_details.setOwner_Admin(c12.toString());
                        }    
                        if(c13==null)
                        {
                         form_details.setModerator_Admin("");
                        }
                        else{
                        form_details.setModerator_Admin(c13.toString());
                        }                          
//                        form_details.setOwner_Admin(c12.toString());
//                        form_details.setModerator_Admin(c13.toString());   // commented by rahul june 2021

                    validation valid = new validation();
                   
                    if (form_details.getList_name() == null) {
                        errorList.add("List name can not be blank. Where row is:" + k + " and column is:1");
                        flag = false;
                    }
                    else 
                    {
                            String msg = valid.listValidation(form_details.getList_name().toLowerCase().trim());
                        if (!msg.equals("")) {
                            errorList.add(msg + " Where row is:" + k + " and column is:1");
                            flag = false;
                        }
                    }
                    if (form_details.getDescription_list() == null) {
                        errorList.add("List Discription can not be blank. Where row is:" + k + " and column is:2");
                        flag = false;
                    } else {
                       boolean listdesc=valid.dnstxtValidation(form_details.getDescription_list().trim());
                        if (listdesc) {
                            errorList.add("Please Enter list decription In correct Formate "+ " Where row is:" + k + " and column is:2");
                            flag = false;
                        }
                    }

                    if (form_details.getList_mod() == null) {
                        errorList.add("List Moderated can not be blank. Where row is:" + k + " and column is:3");
                        flag = false;
                    } else {
                       boolean listmod = valid.checkradioValidation(form_details.getList_mod().trim());
                        if (listmod) {
                            errorList.add("Please Enter List Moderated in correct format" + " Where row is:" + k + " and column is:3");
                            flag = false;
                        }
                    }

                    if (form_details.getAllowed_member() == null) {
                        errorList.add("Is Member Allowed can not be blank. Where row is:" + k + " and column is:4");
                        flag = false;
                    } else {
                        boolean memberallow=valid.checkradioValidation(form_details.getAllowed_member().trim());
                        if (memberallow) {
                            errorList.add("Please Enter no of allowed members "+ " Where row is:" + k + " and column is:4");
                            flag = false;
                        }
                    }

                    if (form_details.getList_temp() == null) {
                        errorList.add("Is ListTemp can not be blank. Where row is:" +k + " and column is:5");
                        flag = false;
                    } else {
                        boolean listTemp=valid.checkradioValidation(form_details.getList_temp().trim());
                        if (listTemp) {
                            errorList.add("Please Enter List temp in correct format" + " Where row is:" + k + " and column is:5");
                            flag = false;
                        }
                    }
                    if (form_details.getMail_Acceptance() == null) {
                        errorList.add("Mail Acceptance can not be blank. Where row is:" + k + " and column is:6");
                        flag = false;
                    } else {
                       HashMap mailact = dlistValidate.MailAcceptance(form_details.getMail_Acceptance().trim());
                        if (mailact.get("valid").equals("false")) {
                            errorList.add(values.get("errorMsg").toString() + " Where row is:" + k + " and column is:5");
                            flag = false;
                        }
                    }

                      String email="";
                      if(!form_details.getOwner_Email().trim().isEmpty()) {
                      boolean valowner = ldap.emailValidate(form_details.getOwner_Email().trim());
                        if (valowner==false) {
                            errorList.add("Please Enter Owner email in correct format" + " Where row is:" + k + " and column is:8");
                            flag = false;
                            email="ownnotvalid";
                        }
                    }
                    if(form_details.getModerator_Email() != ""||form_details.getModerator_Email() != null||!form_details.getModerator_Email().isEmpty()) {
                      boolean valmod = ldap.emailValidate(form_details.getModerator_Email().trim());
                        if (valmod==false) {
                            errorList.add("Please Enter Moderator email in correct format" + " Where row is:" + k + " and column is:11");
                            flag = false;
                            email="modnotvalid";
                        }
                    }
                   //EO validation error check
                    if (email!="") {
                    HashMap hm = new LinkedHashMap();
                    hm.clear();   
                    hm.put("List Name", form_details.getList_name());
                    hm.put("List Discription", form_details.getDescription_list());
                    hm.put("List Moderated", form_details.getList_mod());
                    hm.put("IsMemberAllowed", form_details.getAllowed_member());
                    hm.put("Is ListTemp", form_details.getList_temp());
                    hm.put("Mail Acceptance", form_details.getMail_Acceptance());
                    hm.put("Owner Name", form_details.getOwner_Name());
                    hm.put("Owner Email", form_details.getOwner_Email());
                    hm.put("Owner Mobile", form_details.getOwner_Mobile());
                    hm.put("Moderator Name", form_details.getModerator_Name());
                    hm.put("Moderator Email", form_details.getModerator_Email());
                    hm.put("Moderator Mobile", form_details.getModerator_Mobile());
                    hm.put("Owner Admin", form_details.getOwner_Admin());
                    hm.put("Moderator Admin", form_details.getModerator_Admin());
                    
                        nonvalid.add(hm);
                    }
                   if (flag) {
                    HashMap hm = new LinkedHashMap();
                    hm.clear();   
                    hm.put("List Name", form_details.getList_name());
                    hm.put("List Discription", form_details.getDescription_list());
                    hm.put("List Moderated", form_details.getList_mod());
                    hm.put("IsMemberAllowed", form_details.getAllowed_member());
                    hm.put("Is ListTemp", form_details.getList_temp());
                    hm.put("Mail Acceptance", form_details.getMail_Acceptance());
                    hm.put("Owner Name", form_details.getOwner_Name());
                    hm.put("Owner Email", form_details.getOwner_Email());
                    hm.put("Owner Mobile", form_details.getOwner_Mobile());
                    hm.put("Moderator Name", form_details.getModerator_Name());
                    hm.put("Moderator Email", form_details.getModerator_Email());
                    hm.put("Moderator Mobile", form_details.getModerator_Mobile());
                    hm.put("Owner Admin", form_details.getOwner_Admin());
                    hm.put("Moderator Admin", form_details.getModerator_Admin());
                    
                        cellRecords.add(hm);
                    }
                   else 
                   {      // row has error
                    HashMap hm = new LinkedHashMap();
                    hm.clear();   
                    hm.put("List Name", form_details.getList_name());
                    hm.put("List Discription", form_details.getDescription_list());
                    hm.put("List Moderated", form_details.getList_mod());
                    hm.put("IsMemberAllowed", form_details.getAllowed_member());
                    hm.put("Is ListTemp", form_details.getList_temp());
                    hm.put("Mail Acceptance", form_details.getMail_Acceptance());
                    hm.put("Owner Name", form_details.getOwner_Name());
                    hm.put("Owner Email", form_details.getOwner_Email());
                    hm.put("Owner Mobile", form_details.getOwner_Mobile());
                    hm.put("Moderator Name", form_details.getModerator_Name());
                    hm.put("Moderator Email", form_details.getModerator_Email());
                    hm.put("Moderator Mobile", form_details.getModerator_Mobile());
                    hm.put("Owner Admin", form_details.getOwner_Admin());
                    hm.put("Moderator Admin", form_details.getModerator_Admin());
                    
                        errorList.add(hm);
                        
                    }
                    
               }        
                }               
                else{
                  ExcelValidate.put("errorMessage", "Please remove heading from file like list name, list desc");  
                }
        } // if block close
        else{
               ExcelValidate.put("errorMessage", "File can not be empty.");
                }
                    ExcelValidate.put("cellRecords", cellRecords);
                    ExcelValidate.put("errorList", errorList);
                    ExcelValidate.put("nonvalid", nonvalid);

                System.out.println("");
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            ExcelValidate.put("errorMessage", "File is not in correct format,please refer to the link on the form page: 'Click here to download Sample Excel-Format'");

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return ExcelValidate;
    }

    public List<Object> fetchCoordinator() {
        List<Object> coordinator_list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Object> tmp_cord_list = null;
        try {
            String sql = "SELECT `emp_id`,`emp_category`,`emp_min_state_org`,`emp_dept`,`emp_coord_mobile`,`emp_coord_name`,`emp_coord_email` FROM `employment_coordinator`";
            conSlave = DbConnection.getSlaveConnection();
            ps = conSlave.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                tmp_cord_list = new ArrayList<>();
                tmp_cord_list.add(rs.getInt(1));
                tmp_cord_list.add(rs.getString(2));
                tmp_cord_list.add(rs.getString(3));
                tmp_cord_list.add(rs.getString(4));
                tmp_cord_list.add(rs.getString(5));
                tmp_cord_list.add(rs.getString(6));
                tmp_cord_list.add(rs.getString(7));
                coordinator_list.add(tmp_cord_list);
            }
            System.out.println("coordinator :::" + coordinator_list);

        } catch (Exception ex) {
            System.out.println("Exception in fetchCoordinator() Method ::" + ex.getMessage() + " :::ps val :: " + ps);
            ex.printStackTrace();
        }
        System.out.println("Coordinator List:: " + coordinator_list);
        return coordinator_list;
    }
    
    public List<Object> fetchCoordinator(String orgType, String dptType, String orgDept) {
        List<Object> coordinator_list = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Object> tmp_cord_list = null;
        try {
            System.out.println("orgType::: "+orgType);
            System.out.println("dptType::: "+dptType);
            System.out.println("orgDept::: "+orgDept);
            String sql = "SELECT `emp_id`,`emp_category`,`emp_min_state_org`,`emp_dept`,`emp_coord_mobile`,`emp_coord_name`,`emp_coord_email` FROM `employment_coordinator`";
            if(!orgType.equals("") && dptType.equals("")) {
                sql = "SELECT `emp_id`,`emp_category`,`emp_min_state_org`,`emp_dept`,`emp_coord_mobile`,`emp_coord_name`,`emp_coord_email` FROM `employment_coordinator` WHERE `emp_category`='"+orgType+"'";
            }
            if(!orgType.equals("") && !dptType.equals("") && orgDept.equals("")){
                sql = "SELECT `emp_id`,`emp_category`,`emp_min_state_org`,`emp_dept`,`emp_coord_mobile`,`emp_coord_name`,`emp_coord_email` FROM `employment_coordinator` WHERE `emp_category`='"+orgType+"' AND `emp_min_state_org` = '"+dptType+"'";
            }
            if(!orgType.equals("") && !dptType.equals("") && !orgDept.equals("")){
                sql = "SELECT `emp_id`,`emp_category`,`emp_min_state_org`,`emp_dept`,`emp_coord_mobile`,`emp_coord_name`,`emp_coord_email` FROM `employment_coordinator` WHERE `emp_category`='"+orgType+"' AND `emp_min_state_org` = '"+orgDept+"' AND `emp_dept` = '"+dptType+"'";
                System.out.println("Quary is:  "+sql);
            }
            conSlave = DbConnection.getSlaveConnection();
            ps = conSlave.prepareStatement(sql);
            System.out.println("SQL:: "+sql);
            System.out.println("PS:: "+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                tmp_cord_list = new ArrayList<>();
                tmp_cord_list.add(rs.getInt(1));
                tmp_cord_list.add(rs.getString(2));
                tmp_cord_list.add(rs.getString(3));
                tmp_cord_list.add(rs.getString(4));
                tmp_cord_list.add(rs.getString(5));
                tmp_cord_list.add(rs.getString(6));
                tmp_cord_list.add(rs.getString(7));
                coordinator_list.add(tmp_cord_list);
            }
        } catch (Exception ex) {
            System.out.println("Exception in fetchCoordinator() Method ::" + ex.getMessage() + " :::ps val :: " + ps);
            ex.printStackTrace();
        }
        return coordinator_list;
    }

    private String insertModDetails(String newref, Map bulkData) {
        PreparedStatement pst = null;
        //Connection con = null;
        System.out.println("start process of insertion of owner, moderators details into dlist_moderator table:");
        try {

            con = DbConnection.getConnection();

            String sql = "";
            if (bulkData.get("ownerData") != null) {
                String form_type = "owner";
                sql = "insert into dlist_moderator (registration_no, name , email, mobile,form_type)" + "values (?,?,?,?,?)";
                pst = con.prepareStatement(sql);

                Set<OwnerBean> od = (Set<OwnerBean>) bulkData.get("ownerData");
                for (OwnerBean ob : od) {
                    pst.setString(1, newref);
                    pst.setString(2, ob.getOwner_name());
                    pst.setString(3, ob.getOwner_email());
                    pst.setString(4, ob.getOwner_mobile());
                    pst.setString(5, form_type);
                    pst.executeUpdate();
                }

            }

            if (bulkData.get("moderatorData") != null) {
                String form_type = "moderator";
                sql = "insert into dlist_moderator (registration_no, name , email, mobile,form_type)" + "values (?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                Set<ModerateBean> mv = (Set<ModerateBean>) bulkData.get("moderatorData");
                for (ModerateBean mb : mv) {
                    pst.setString(1, newref);
                    pst.setString(2, mb.getT_off_name());
                    pst.setString(3, mb.getTauth_email());
                    pst.setString(4, mb.getTmobile());
                    pst.setString(5, form_type);
                    pst.executeUpdate();
                }

            }

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
        return SUCCESS;

    }
 private String insertModDetails1(String newref, Map dlistbulkData1) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {

            con = DbConnection.getConnection();

            String sql = "";
            if (dlistbulkData1.get("excelFileData") != null) {
                String form_type = "owner";
                sql = "insert into dlist_moderator (registration_no, name , email, mobile,form_type)" + "values (?,?,?,?,?)";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "insertModDetails1: " + sql);
                pst = con.prepareStatement(sql);

               //Start Json Insertion
             String jsstr = new Gson().toJson(dlistbulkData1);
            JSONObject json=new JSONObject(jsstr);
            String exceldata = json.get("excelFileData").toString();
            //System.out.println("====>>> "+exceldata);
            
             JSONObject json1=new JSONObject(exceldata);
            String exceldata1 = json1.get("ExcelFileData").toString();
            //System.out.println("====>>> "+exceldata1);
             
            JSONArray jarr=new JSONArray(exceldata1);
            for(int i=0;i<jarr.length();i++){
                JSONObject newobj=new JSONObject(jarr.get(i).toString());
                
                 pst.setString(1, newref);
                pst.setString(2,newobj.get("Owner Name").toString());
                pst.setString(3,newobj.get("Owner Email").toString());
                pst.setString(4,newobj.get("Owner Mobile").toString());
                pst.setString(5, form_type);
                     pst.executeUpdate();    
            }
            }
            if (dlistbulkData1.get("excelFileData") != null) {
                String form_type = "moderator";
                sql = "insert into dlist_moderator (registration_no, name , email, mobile,form_type)" + "values (?,?,?,?,?)";
                pst = con.prepareStatement(sql);
          //Start Json Insertion
             String jsstr = new Gson().toJson(dlistbulkData1);
          JSONObject json=new JSONObject(jsstr);
            String exceldata = json.get("excelFileData").toString();
           // System.out.println("====>>> "+exceldata);
            
             JSONObject json1=new JSONObject(exceldata);
            String exceldata1 = json1.get("ExcelFileData").toString();
           // System.out.println("====>>> "+exceldata1);
             
            JSONArray jarr=new JSONArray(exceldata1);
            for(int i=0;i<jarr.length();i++){
                //System.out.println("--"+jarr.get(i));
                JSONObject newobj=new JSONObject(jarr.get(i).toString());
                
                 pst.setString(1, newref);
                pst.setString(2,newobj.get("Owner Name").toString());
                pst.setString(3,newobj.get("Owner Email").toString());
                pst.setString(4,newobj.get("Owner Mobile").toString());
                 pst.setString(5, form_type);
                     pst.executeUpdate();  
            }
            }

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
        return SUCCESS;

    }
    
    private String insertExcelFileDetails(String newref, Map dlistbulkData2) {
        PreparedStatement pst = null;
        try {
            con = DbConnection.getConnection();
            String sql = "";
            if (dlistbulkData2.get("excelFileData") != null) {
               
                sql = "insert into dlist_bulk(registration_no, list_name , description_list, list_mod, allowed_member, list_temp, mail_Acceptance, owner_Name, Owner_Email, owner_Mobile, moderator_Name, moderator_Email, moderator_Mobile, owner_Admin, moderator_Admin)" + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = con.prepareStatement(sql);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "insertExcelFileDetails: " + sql);
                //Start Json Insertion
             String jsstr = new Gson().toJson(dlistbulkData2);
          JSONObject json=new JSONObject(jsstr);
            String exceldata = json.get("excelFileData").toString();
           // System.out.println("====>>> "+exceldata);
            
             JSONObject json1=new JSONObject(exceldata);
            String exceldata1 = json1.get("ExcelFileData").toString();
           // System.out.println("====>>> "+exceldata1);
            JSONArray jarr=new JSONArray(exceldata1);
            for(int i=0;i<jarr.length();i++){
            
              //  System.out.println("--"+jarr.get(i));
                JSONObject newobj=new JSONObject(jarr.get(i).toString());
                 pst.setString(1, newref);
                pst.setString(2,newobj.get("List Name").toString());
                pst.setString(3,newobj.get("List Discription").toString());
                pst.setString(4,newobj.get("List Moderated").toString());
                pst.setString(5,newobj.get("IsMemberAllowed").toString());
                pst.setString(6,newobj.get("Is ListTemp").toString());
                pst.setString(7,newobj.get("Mail Acceptance").toString());
                pst.setString(8,newobj.get("Owner Name").toString());
                pst.setString(9,newobj.get("Owner Email").toString());
                pst.setString(10,newobj.get("Owner Mobile").toString());
                pst.setString(11,newobj.get("Moderator Name").toString());
                pst.setString(12,newobj.get("Moderator Email").toString());
                pst.setString(13,newobj.get("Moderator Mobile").toString());
                pst.setString(14,newobj.get("Owner Admin").toString());
                pst.setString(15,newobj.get("Moderator Admin").toString());
                 pst.executeUpdate();
                
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                   
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SUCCESS;
    } 
}
