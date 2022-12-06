package com.org.dao;

import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

public class WifiDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;
    List<FormBean> formBeanList = new ArrayList<FormBean>();

    public void Wifi_tab1(FormBean form_details) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "insert into onlineform_temp_db.wifi_registration_tmp (wifi_type,wifi_mac1,wifi_mac2,wifi_mac3,wifi_os1,wifi_os2,wifi_os3,wifi_mac4,wifi_os4,ip)"
                    + "values (?,?,?,?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getWifi_type());
            pst.setString(2, form_details.getWifi_mac1());
            pst.setString(3, form_details.getWifi_mac2());
            pst.setString(4, form_details.getWifi_mac3());
            pst.setString(5, form_details.getWifi_os1());
            pst.setString(6, form_details.getWifi_os2());
            pst.setString(7, form_details.getWifi_os3());
            pst.setString(8, form_details.getWifi_mac4());
            pst.setString(9, form_details.getWifi_os4());
            pst.setString(10, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WIFI dao tab 1: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in WIFI dao tab 1: " + e.getMessage());
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

    public void Wifi_tab2(FormBean form_details) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile            
            String sql = "insert into onlineform_temp_db.wifi_registration_tmp (wifi_type,wifi_request,wifi_time,wifi_duration,ip)"
                    + "values (?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, form_details.getWifi_type());
            pst.setString(2, form_details.getWifi_request());
            pst.setString(3, form_details.getWifi_time());
            pst.setString(4, form_details.getWifi_duration());
            pst.setString(5, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WIFI dao tab 2: " + pst);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in WIFI dao tab 2: " + e.getMessage());
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

    public String Wifi_tab3(FormBean form_details, int wifi_value, Map profile_values) {
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
            String search_regnum = "select registration_no from wifi_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";
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
            newref = "WIFI-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in WIFI dao tab 3: " + newref);
            String sql = "insert into wifi_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                    + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,wifi_type,wifi_mac1,wifi_mac2,wifi_mac3,"
                    + "wifi_os1,wifi_os2,wifi_os3,wifi_request,wifi_time,wifi_duration,wifi_value,registration_no,userip,ca_desig,fwd_ofc_name,fwd_ofc_email,fwd_ofc_mobile,fwd_ofc_desig,fwd_ofc_add,fwd_ofc_tel,wifi_process,device_type1,device_type2,device_type3,wifi_mac4,wifi_os4,device_type4) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            pst.setString(22, form_details.getWifi_type());
            if (form_details.getWifi_type().equals("nic")) {
                pst.setString(23, form_details.getWifi_mac1());
                pst.setString(24, form_details.getWifi_mac2());
                pst.setString(25, form_details.getWifi_mac3());
                pst.setString(26, form_details.getWifi_os1());
                pst.setString(27, form_details.getWifi_os2());
                pst.setString(28, form_details.getWifi_os3());
                pst.setString(29, "");
                pst.setString(30, "");
                pst.setString(31, "");
            } else {
                pst.setString(23, "");
                pst.setString(24, "");
                pst.setString(25, "");
                pst.setString(26, "");
                pst.setString(27, "");
                pst.setString(28, "");
                pst.setString(29, form_details.getWifi_request());
                pst.setString(30, form_details.getWifi_time());
                pst.setString(31, form_details.getWifi_duration());
            }
            pst.setInt(32, wifi_value);
            pst.setString(33, newref);
            pst.setString(34, ip);
            pst.setString(35, form_details.getCa_design().trim());
            pst.setString(36, form_details.getFwd_ofc_name());
            pst.setString(37, form_details.getFwd_ofc_email());
            pst.setString(38, form_details.getFwd_ofc_mobile());
            pst.setString(39, form_details.getFwd_ofc_design());
            pst.setString(40, form_details.getFwd_ofc_add());
            pst.setString(41, form_details.getFwd_ofc_tel());
            pst.setString(42, form_details.getWifi_process());
            pst.setString(43, form_details.getDevice_type1());
            pst.setString(44, form_details.getDevice_type2());
            pst.setString(45, form_details.getDevice_type3());
            pst.setString(46, form_details.getWifi_mac4());
            pst.setString(47, form_details.getWifi_os4());
            pst.setString(48, form_details.getDevice_type4());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WIFI dao tab ravinder 3: " + pst);
            int i = pst.executeUpdate();
        } catch (Exception e) {
            newref = "";
            errflag = true;
            // e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in WIFI dao tab 3: " + e.getMessage());
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
                String search_regnum = "select registration_no from wifi_registration where date(datetime)=date(now()) group by registration_no order by registration_no desc limit 1";
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
                newref = "WIFI-FORM" + pdate1 + newref;
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "REF NUMBER in WIFI dao tab 3: " + newref);
                String sql = "insert into wifi_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,"
                        + "other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,wifi_type,wifi_mac1,wifi_mac2,wifi_mac3,"
                        + "wifi_os1,wifi_os2,wifi_os3,wifi_request,wifi_time,wifi_duration,wifi_value,registration_no,userip,ca_desig,fwd_ofc_name,fwd_ofc_email,fwd_ofc_mobile,fwd_ofc_desig,fwd_ofc_add,fwd_ofc_tel,wifi_process,device_type1,device_type2,device_type3,wifi_mac4,wifi_os4,device_type4) "
                        + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
                pst.setString(22, form_details.getWifi_type());
                if (form_details.getWifi_type().equals("nic")) {
                    pst.setString(23, form_details.getWifi_mac1());
                    pst.setString(24, form_details.getWifi_mac2());
                    pst.setString(25, form_details.getWifi_mac3());
                    pst.setString(26, form_details.getWifi_os1());
                    pst.setString(27, form_details.getWifi_os2());
                    pst.setString(28, form_details.getWifi_os3());
                    pst.setString(29, "");
                    pst.setString(30, "");
                    pst.setString(31, "");
                } else {
                    pst.setString(23, "");
                    pst.setString(24, "");
                    pst.setString(25, "");
                    pst.setString(26, "");
                    pst.setString(27, "");
                    pst.setString(28, "");
                    pst.setString(29, form_details.getWifi_request());
                    pst.setString(30, form_details.getWifi_time());
                    pst.setString(31, form_details.getWifi_duration());
                }
                pst.setInt(32, wifi_value);
                pst.setString(33, newref);
                pst.setString(34, ip);
                pst.setString(35, form_details.getCa_design().trim());
                pst.setString(36, form_details.getFwd_ofc_name());
                pst.setString(37, form_details.getFwd_ofc_email());
                pst.setString(38, form_details.getFwd_ofc_mobile());
                pst.setString(39, form_details.getFwd_ofc_design());
                pst.setString(40, form_details.getFwd_ofc_add());
                pst.setString(41, form_details.getFwd_ofc_tel());
                pst.setString(42, form_details.getWifi_process());
                pst.setString(43, form_details.getDevice_type1());
                pst.setString(44, form_details.getDevice_type2());
                pst.setString(45, form_details.getDevice_type3());
                pst.setString(46, form_details.getWifi_mac4());
                pst.setString(47, form_details.getWifi_os4());
                pst.setString(48, form_details.getDevice_type4());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "WIFI dao tab 3: " + pst);
                int i = pst.executeUpdate();
            } catch (Exception e) {
                newref = "";
                // errflag=true;
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "exception in WIFI dao tab 3: " + e.getMessage());
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

    public List<FormBean> fetchApplicantData(Set<String> email) {
        System.out.println("inside dao");

        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            PreparedStatement pst = null;
            ResultSet rs = null;
            PreparedStatement pst1 = null;
            ResultSet rs1 = null;
            for (String aliases : email) {
                pst = conSlave.prepareStatement("select wifi_mac1, wifi_mac2, wifi_mac3,wifi_mac4 ,wifi_os1, wifi_os2, wifi_os3,wifi_os4,registration_no from  wifi_registration where"
                        + " registration_no in (select registration_no from final_audit_track where status = 'completed' and applicant_email = ? and registration_no like 'WIFI-FORM%') and wifi_process='request'  "
                );

                //pst = con.prepareStatement("select m.wifi_mac1, m.wifi_mac2, m.wifi_mac3, m.wifi_os1, m.wifi_os2,m.registration_no from wifi_registration m join final_audit_track f  join wifi_mac_os o where f.registration_no = m.registration_no and f.status='completed'");
                pst.setString(1, aliases);
                System.out.println("PST:: " + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
                    FormBean formBean = new FormBean();
                    formBean.setWifi_mac1(rs.getString("wifi_mac1"));
                    formBean.setWifi_os1(rs.getString("wifi_os1"));
                    formBean.setWifi_mac2(rs.getString("wifi_mac2"));
                    formBean.setWifi_os2(rs.getString("wifi_os2"));
                    formBean.setWifi_mac3(rs.getString("wifi_mac3"));
                    formBean.setWifi_mac4(rs.getString("wifi_mac4"));
                    formBean.setWifi_os3(rs.getString("wifi_os3"));
                    formBean.setWifi_os4(rs.getString("wifi_os4"));

                    formBean.setRegistration_no(rs.getString("registration_no"));
                    formBeanList.add(formBean);
                    System.out.println("formbean mac1:::::::" + rs.getString("wifi_mac1"));
                    System.out.println("formbean mac1:::::::" + rs.getString("wifi_mac2"));
                    System.out.println("formbean mac1:::::::" + rs.getString("wifi_mac3"));
                    System.out.println("formbean mac1:::::::" + rs.getString("wifi_mac4"));

                }
                //System.out.println("form bean in while lopp:::::::"+formBean.getWifi_mac1()+formBean.getWifi_mac2());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception eeeeeeeeee" + e);
        }

        return formBeanList;
    }

    public List<FormBean> fetchApplicantPendingData(Set<String> email) {
        System.out.println("inside fetch applicant pending dao");

        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021

            PreparedStatement pst = null;
            ResultSet rs = null;
            PreparedStatement pst1 = null;
            ResultSet rs1 = null;
            for (String aliases : email) {
                pst = conSlave.prepareStatement("select wifi_mac1, wifi_mac2, wifi_mac3,wifi_mac4, wifi_os1, wifi_os2, wifi_os3,wifi_os4,registration_no from  wifi_registration where"
                        + " registration_no in (select registration_no from final_audit_track where (status like '%pending%' OR status like '%manual_upload%') and applicant_email = ? and registration_no like 'WIFI-FORM%') and wifi_process='request' and pdf_path!=''"
                );

                //pst = con.prepareStatement("select m.wifi_mac1, m.wifi_mac2, m.wifi_mac3, m.wifi_os1, m.wifi_os2,m.registration_no from wifi_registration m join final_audit_track f  join wifi_mac_os o where f.registration_no = m.registration_no and f.status='completed'");
                pst.setString(1, aliases);
                System.out.println("PST for pending data:: " + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
                    FormBean formBean = new FormBean();
                    formBean.setWifi_mac1(rs.getString("wifi_mac1"));
                    formBean.setWifi_os1(rs.getString("wifi_os1"));
                    formBean.setWifi_mac2(rs.getString("wifi_mac2"));
                    formBean.setWifi_os2(rs.getString("wifi_os2"));
                    formBean.setWifi_mac3(rs.getString("wifi_mac3"));
                    formBean.setWifi_os3(rs.getString("wifi_os3"));
                    formBean.setWifi_mac4(rs.getString("wifi_mac4"));
                    formBean.setWifi_os4(rs.getString("wifi_os4"));
                    formBean.setRegistration_no(rs.getString("registration_no"));
                    formBeanList.add(formBean);
                    System.out.println("formbean mac1:::::::" + rs.getString("wifi_mac1"));
                    System.out.println("formbean mac2:::::::" + rs.getString("wifi_mac2"));
                    System.out.println("formbean mac3:::::::" + rs.getString("wifi_mac3"));
                    System.out.println("formbean mac4:::::::" + rs.getString("wifi_mac4"));
                }
                //System.out.println("form bean in while lopp:::::::"+formBean.getWifi_mac1()+formBean.getWifi_mac2());
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception eeeeeeeeee" + e);
        }

        return formBeanList;
    }

    public void saveDeleteReq(List<FormBean> mac_list2) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile

            for (FormBean formBean : mac_list2) {
                String sql = "insert into onlineform_temp_db.wifi_mac_os (wifi_mac,wifi_os,registration_no)"
                        + "values (?,?,?)";
                pst = con.prepareStatement(sql);
                pst.setString(1, formBean.getWifi_mac1());
                pst.setString(2, formBean.getWifi_os1());
                pst.setString(3, formBean.getRegistration_no());

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "saveDeleteReq: " + pst);
                pst.executeUpdate();
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "saveDeleteReq: " + e.getMessage());
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

    public void Wifi_mac_os(FormBean formBean) {
        PreparedStatement pst = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            // get bean of profile

            String sql = "insert into wifi_mac_os (wifi_mac,wifi_os,registration_no,auth_email)"
                    + "values (?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, formBean.getWifi_mac1());
            pst.setString(2, formBean.getWifi_os1());
            pst.setString(3, formBean.getRegistration_no());
            pst.setString(4, formBean.getUser_email());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "saveDeleteReqfinalllllllllll: " + pst);
            pst.executeUpdate();

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "saveDeleteReqfinalllllllllll: " + e.getMessage());
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

    public List<String> getDeletedWifiList(Set<String> email) {
        List list = new ArrayList();
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021

            PreparedStatement pst = null;
            ResultSet rs = null;
            for (String aliases : email) {
                pst = conSlave.prepareStatement("select wifi_mac, wifi_os, registration_no from  wifi_mac_os where status = 'deleted' and auth_email=? group by wifi_mac");
                pst.setString(1, aliases);
                System.out.println("PST:: " + pst);
                rs = pst.executeQuery();
                while (rs.next()) {
//                formBean1.setWifi_mac1(rs.getString("wifi_mac"));
//                formBean1.setWifi_os1(rs.getString("wifi_os"));
//                formBean1.setRegistration_no(rs.getString("registration_no"));
                    list.add(rs.getString("wifi_mac").toString());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception eeeeeeeeee" + e);
        }
        return list;
    }

    public List<String> featchMacFromWifiMacTable(String email) {    //Added by Rahul, Manikant Dec2021
        boolean flag = true;
        List<String> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        String date = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps1 = conSlave.prepareStatement("select wifi_mac from wifi_mac_os where status='deleted' AND auth_email in ('" + email + "')");
            System.out.println("inside count of user with this mac of MAC_TABLE::::::::::" + ps1);
            ResultSet rset3 = ps1.executeQuery();
            System.out.println("com.org.dao.WifiDao.featchMacFromWifiMacTable()");
            while (rset3.next()) {
                list.add(rset3.getString(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    ///// // below methods added by ravinder 2019-12-19
    // this method will return 
    public List<String> countOfMacWithThisUser(String email) {
        List<String> list = new ArrayList<>();
//        java.sql.Date date = null;
        String date = "";
        System.out.println("email in dao countOfMacWithThisUser:" + email);
        try {
            conSlave = DbConnection.getSlaveConnection();
//            PreparedStatement ps1 = conSlave.prepareStatement("select auth_email,wifi_mac1,wifi_mac2,wifi_mac3,datetime from wifi_registration where auth_email = ? and registration_no not in ( select registration_no from final_audit_track where registration_no like 'WIFI%' and status like '%rejected%' or status like '%cancel%' or status like '%expired%')");
            PreparedStatement ps1 = conSlave.prepareStatement("select auth_email,wifi_mac1,wifi_mac2,wifi_mac3,wifi_mac4,datetime from wifi_registration where auth_email in (" + email + ") and registration_no not in ( select registration_no from final_audit_track where form_name = 'wifi' and status= 'da_rejected' or status= 'ca_rejected' or status='support_rejected' or status='mail-admin_rejected' or status='coordinator_rejected' or status = 'cancel') and wifi_process = 'request' and (pdf_path!=null || pdf_path!='')");
//            ps1.setString(1, email);
            System.out.println("ps for countOfMacWithThisUser:" + ps1);
            ResultSet rset_user = ps1.executeQuery();
            while (rset_user.next()) {
                System.out.println("DATETIME WITH MAC:" + rset_user.getString("datetime"));

                date = rset_user.getString("datetime");

                if (rset_user.getString(2) != null && (!rset_user.getString(2).equals(""))) {
                    if (!confirmMacInDeleteTable(email, rset_user.getString(2), date)) {
                        list.add(rset_user.getString(2));
                    }
                }

                if (rset_user.getString(3) != null && (!rset_user.getString(3).equals(""))) {
                    if (!confirmMacInDeleteTable(email, rset_user.getString(3), date)) {
                        list.add(rset_user.getString(3));
                    }
                }

                if (rset_user.getString(4) != null && (!rset_user.getString(4).equals(""))) {
                    if (!confirmMacInDeleteTable(email, rset_user.getString(4), date)) {
                        list.add(rset_user.getString(4));
                    }
                }

                if (rset_user.getString(5) != null && (!rset_user.getString(5).equals(""))) {
                    if (!confirmMacInDeleteTable(email, rset_user.getString(5), date)) {
                        list.add(rset_user.getString(5));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        System.out.println("below there is value of x");
//        set.stream().forEach(x -> System.out.println("x:"+x));
        return list;
    }

//    public  boolean confirmMacInDeleteTable(String email, String mac, java.sql.Date date) {
    public boolean confirmMacInDeleteTable(String email, String mac, String date) {
        boolean flag = true;

        try {
            conSlave = DbConnection.getSlaveConnection();
//            PreparedStatement ps1 = conSlave.prepareStatement("select registration_no,auth_email,datetime from wifi_registration where registration_no in (select registration_no from wifi_mac_os where auth_email = ? and wifi_mac = ?) and datetime > ?");
            PreparedStatement ps1 = conSlave.prepareStatement("select registration_no,auth_email,datetime from wifi_registration where registration_no in (select registration_no from wifi_mac_os where auth_email in (" + email + ") and wifi_mac = ?  and status='deleted') and datetime > ?");
//            ps1.setString(1, email);
            ps1.setString(1, mac);
//            ps1.setDate(2, date);
            ps1.setString(2, date);

            System.out.println("ps for confirmMacInDeleteTable:" + ps1);
            ResultSet rset2 = ps1.executeQuery();

            if (!rset2.next()) {
                flag = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

//    public  Set countOfUserWithThisMac(String mac_address, String commawiseAliasses) {
//        Set set = new HashSet();
////        java.sql.Date date = null;
//        String date = null;
//        try {
//            conSlave = DbConnection.getSlaveConnection();
//            PreparedStatement ps1 = conSlave.prepareStatement("select auth_email,wifi_mac1,wifi_mac2,wifi_mac3,datetime from wifi_registration where ( wifi_mac1 = ? or wifi_mac2 = ? or wifi_mac3 = ? ) and registration_no not in ( select registration_no from final_audit_track where registration_no like 'WIFI%' and status like '%rejected%' or status like '%cancel%' or status like '%expired%')  and wifi_process = 'request'");
//            ps1.setString(1, mac_address);
//            ps1.setString(2, mac_address);
//            ps1.setString(3, mac_address);
//            ResultSet rset2 = ps1.executeQuery();
//            while (rset2.next()) {
////                date = rset2.getDate("datetime");
//                date = rset2.getString("datetime");
//               if(confirmMacFromDeleteTable(rset2.getString("auth_email"), mac_address, date, commawiseAliasses)){
//                   set.add(rset2.getString("auth_email"));
//               }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return set;
//    }
    public Map<String, String> countOfUserWithThisMac(String mac_address) {
        Map<String, String> map = new HashMap<>();
//        java.sql.Date date = null;
        String date = null;
        String status = null;
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps1 = conSlave.prepareStatement("select auth_email,wifi_mac1,wifi_mac2,wifi_mac3,wifi_mac4,datetime from wifi_registration where ( wifi_mac1 = ? or wifi_mac2 = ? or wifi_mac3 = ? or wifi_mac4 = ?) and registration_no not in ( select registration_no from final_audit_track where form_name = 'wifi' and status= 'da_rejected' or status= 'ca_rejected' or status='support_rejected' or status='mail-admin_rejected' or status='coordinator_rejected' or status = 'cancel')  and wifi_process = 'request' and (pdf_path!=null || pdf_path!='') ");
            ps1.setString(1, mac_address);
            ps1.setString(2, mac_address);
            ps1.setString(3, mac_address);
            ps1.setString(4, mac_address);
            System.out.println("inside count of user with this mac::::::::::" + ps1);
            ResultSet rset2 = ps1.executeQuery();
            while (rset2.next()) {
                date = rset2.getString("datetime");
                map.put(date, rset2.getString("auth_email"));
                System.out.println("auth_email$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$:" + rset2.getString("auth_email"));
            }
            PreparedStatement ps2 = conSlave.prepareStatement("select * from wifi_mac_os where wifi_mac = ?");
            ps2.setString(1, mac_address);
            System.out.println("inside count of user with this mac::::::::::" + ps1);
            ResultSet rset3 = ps2.executeQuery();
            System.out.println("in wifi tab1 featch data from mac table:" + rset3);
            while (rset3.next()) {
                status = rset3.getString("status");
                map.put("statusInWifiMacTable", status);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

//    public  boolean confirmMacFromDeleteTable(String email, String mac, java.sql.Date date, String commawiseAliasses) {
    public boolean confirmMacFromDeleteTable(String mac, String date, String commawiseAliasses) {
        boolean flag = true;
        System.out.println("confirmMacFromDeleteTable :  commawiseAliasses:" + commawiseAliasses + " | mac:" + mac + " | date:" + date);
        try {
            conSlave = DbConnection.getSlaveConnection();
            PreparedStatement ps1 = conSlave.prepareStatement("select registration_no,auth_email,datetime from wifi_registration where registration_no in (select registration_no from wifi_mac_os where (auth_email in (" + commawiseAliasses + ") and wifi_mac = ?) or (auth_email in (" + commawiseAliasses + ") and wifi_mac = ?) and status='deleted' ) and datetime > ?");
//            ps1.setString(1, email);
            ps1.setString(1, mac);
            ps1.setString(2, mac);
//            ps1.setDate(4, date);
            ps1.setString(3, date);
            System.out.println("ps for confirmMacFromDeleteTable:" + ps1);
            ResultSet rset2 = ps1.executeQuery();

            while (rset2.next()) {
                //this indicates that (time of the deleted mac) is greater so it means this mac is deleted to whom it was allocated so this can be added by any other user
                flag = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

}
