/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import com.org.bean.CoordinatorPanelBean;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.service.ProfileService;
import com.org.utility.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts2.ServletActionContext;
import utility.Inform;

/**
 *
 * @author Satya
 */
public class CoordinatorPanelDao {
     String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;
    //NOT USED HERE
    public Map<String,Object> previewRequest(String registration_no){
        PreparedStatement pst = null;
        ResultSet rs=null;
        Map<String,Object> map=new HashMap<String,Object>();
        
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String tablename = myTable(registration_no);
            
             String sql="select registration_no,auth_off_name,designation,address,city,add_state,pin,ophone,rphone,mobile,auth_email,userip,hod_name,hod_email,hod_mobile,hod_telephone,ca_desig,employment,ministry,department,other_dept from "+tablename+" where registration_no=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, registration_no);
             rs=pst.executeQuery();
             while(rs.next()){
                 map.put("registration_no", rs.getString(1));
                 map.put("auth_off_name", rs.getString(2));
                 map.put("designation", rs.getString(3));
                 map.put("address", rs.getString(4));
                 map.put("city", rs.getString(5));
                 map.put("add_state", rs.getString(6));
                 map.put("pin", rs.getString(7));
                 map.put("ophone", rs.getString(8));
                 map.put("rphone", rs.getString(9));
                 map.put("mobile", rs.getString(10));
                 map.put("auth_email", rs.getString(11));
                 map.put("userip", rs.getString(12));
                 map.put("hod_name", rs.getString(13));
                 map.put("hod_email", rs.getString(14));
                 map.put("hod_mobile", rs.getString(15));
                 map.put("hod_telephone", rs.getString(16));
                 map.put("ca_desig", rs.getString(17));
                 map.put("employment", rs.getString(18));
                 map.put("ministry", rs.getString(19));
                 map.put("department", rs.getString(20));
                 map.put("other_dept", rs.getString(21));
             }
             
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());
         }
         
              finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
           return map;
    }
    //PullbackAll
    
      public Map<String,Object> pullbackAllRequestHog(String registration_no,String loggedinHog){
     
     PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
         try {
             con = DbConnection.getConnection();
            
         String to_email=loggedinHog;
          List<CoordinatorPanelBean> hog_details = getMinistryInfoOfHOG(loggedinHog);
         String hog_name = hog_details.get(0).getHog_name();
         String hog_mobile = hog_details.get(0).getHog_mobile();
        //  String type = hog_details.get(0).getType();
        String regno[]=registration_no.split(",");
        for(int k=0;k<regno.length;k++){
        
        String sql=null;
     
              sql = "update final_audit_track set status='hog_pending',to_email=?,to_mobile=?,to_name=?,to_datetime=?,hog_name=?,hog_mobile=?,hog_email=?,hog_ip=? where registration_no=?";

             pst = con.prepareStatement(sql);
             pst.setString(1, to_email);
             pst.setString(2, "");
             pst.setString(3, "");
             pst.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
           
                pst.setString(5, hog_name);
                pst.setString(6, hog_mobile);
           
             pst.setString(7,loggedinHog);
             pst.setString(8,ip);
             pst.setString(9, regno[k]);
             int i=pst.executeUpdate();
             if(i>0){
                  String sql_status="insert into status(stat_form_type,stat_reg_no,stat_type,stat_forwarded_by_user,stat_remarks,stat_active,stat_forwarded_by_name,stat_forwarded_by_email,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_to,stat_forwarded_by) values(?,?,?,?,?,?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql_status);
             pst.setString(1, formtype(regno[k]));
             pst.setString(2, regno[k]);
             pst.setString(3, "hog_pending");
             pst.setString(4, loggedinHog);
             pst.setString(5, "");
             pst.setString(6, "a");
             pst.setString(7, "");
             pst.setString(8, loggedinHog);
             pst.setString(9, ip);
             pst.setTimestamp(10, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(11, "h");
             pst.setString(12, "c");
           int j=pst.executeUpdate();
            if(j>0){
           
           map.put("status_"+regno[k], "Pulled Successfully  ");
           map.put("to_email_"+regno[k], to_email);
           }
           else{
           map.put("status_"+regno[k], "Failed to Approve");
           }
             }
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());

         }
            finally {
           try {
               if (pst != null) {
                   pst.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

     return map;
     }
    
    
    //EO pullback all
    
    //pullback request hog
       public Map<String,Object> pullbackRequestHog(String registration_no,String loggedinHog){
     
     PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
         try {
             con = DbConnection.getConnection();
             String formname=formtype(registration_no);
        
         String to_email=loggedinHog;
         List<CoordinatorPanelBean> hog_details = getMinistryInfoOfHOG(loggedinHog);
         String hog_name = hog_details.get(0).getHog_name();
         String hog_mobile = hog_details.get(0).getHog_mobile();
        // String type = hog_details.get(0).getType();
         String sql=null;
      
              sql = "update final_audit_track set status='hog_pending',to_email=?,to_mobile=?,to_name=?,to_datetime=?,hog_name=?,hog_mobile=?,hog_email=?,hog_ip=? where registration_no=?";

         
             pst = con.prepareStatement(sql);
             pst.setString(1, to_email);
             pst.setString(2, "");
             pst.setString(3, "");
             pst.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
               
                 pst.setString(5, hog_name);
                 pst.setString(6, hog_mobile);
          
             pst.setString(7,loggedinHog);
             pst.setString(8,ip);
             pst.setString(9, registration_no);
             int i=pst.executeUpdate();
         
             if(i>0){
                  String sql_status="insert into status(stat_form_type,stat_reg_no,stat_type,stat_forwarded_by_user,stat_remarks,stat_active,stat_forwarded_by_name,stat_forwarded_by_email,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_to,stat_forwarded_by) values(?,?,?,?,?,?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql_status);
             pst.setString(1, formtype(registration_no));
             pst.setString(2, registration_no);
             pst.setString(3, "hog_pending");
            
             pst.setString(4, loggedinHog);
             pst.setString(5, "");
             pst.setString(6, "a");
             pst.setString(7, "");
             pst.setString(8, loggedinHog);
             pst.setString(9, ip);
             pst.setTimestamp(10, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(11, "h");
             pst.setString(12, "c");
           int j=pst.executeUpdate();
            if(j>0){
           
           map.put("status", "Pulled Successfully  ");
           map.put("to_email", to_email);
            String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "pulled back request successfully by "+loggedinHog);
                  pst.setString(2, loggedinHog);
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
                  
           }
           else{
           map.put("status", "Failed to Approve");
           }
             
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
         try {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());
             String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
             pst=con.prepareStatement(sql_logs);
             pst.setString(1, ex.getMessage()+" pulled back request failed by "+loggedinHog);
             pst.setString(2, loggedinHog);
             pst.executeUpdate();
             System.out.println("Activity Logs Inserted");
         } catch (SQLException ex1) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

         }

     return map;
     }
       

       
    
    //end of pullback request hog
     public Map<String,Object> approveAllRequestHog(String registration_no,String loggedinHog){
     
     PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
         try {
             con = DbConnection.getConnection();
             
             
              String regno[]=registration_no.split(",");
        for(int k=0;k<regno.length;k++){
        
        
             
             String formname=formtype(regno[k]);
         ArrayList<String> madminarr = fetchMailAdmins(formname);
         String to_email="";
         for(String madmin:madminarr){
         to_email=to_email+","+madmin;
         }
         to_email=to_email.replaceAll(",$", "").replaceAll("^,", "");
         
         
         
         
         
             String sql = "update final_audit_track set status='mail-admin_pending',to_email=?,to_mobile=?,to_name=?,to_datetime=? where registration_no=?";
             pst = con.prepareStatement(sql);
             pst.setString(1, to_email);
             pst.setString(2, "");
             pst.setString(3, "");
             pst.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(5, regno[k]);
             int i=pst.executeUpdate();
             if(i>0){
                  String sql_status="insert into status(stat_form_type,stat_reg_no,stat_type,stat_forwarded_by_user,stat_remarks,stat_active,stat_forwarded_by_name,stat_forwarded_by_email,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_to,stat_forwarded_to_user) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql_status);
             pst.setString(1, formtype(registration_no));
             pst.setString(2, regno[k]);
             pst.setString(3, "mail-admin_pending");
             pst.setString(4, loggedinHog);
             pst.setString(5, "");
             pst.setString(6, "a");
             pst.setString(7, "");
             pst.setString(8, loggedinHog);
             pst.setString(9, ip);
             pst.setTimestamp(10, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
              pst.setString(11, "h");
             pst.setString(12, "m");
             pst.setString(13, to_email);
           int j=pst.executeUpdate();
            if(j>0){
           
           map.put("status", "Approved Successfully and send to admin ");
           map.put("to_email_"+regno[k], to_email);
           
           
//            CoordinatorPanelBean cpanelbean = getApplicantdetailsFinalAudit(regno[k]);
//            Inform inform=new Inform();
//            String body="Your registration no "+regno[k]+"  has been approved and sent to mail admin "+to_email;
//            String mobile=cpanelbean.getApplicant_mobile();
//            String[] cc=null;
//            String[] attach=null;
//            String from = loggedinHog;
//            String to=cpanelbean.getApplicant_email();
//            String msg="Your registration no "+regno[k]+"  has been approved and sent to mail admin "+to_email;
//            String sub="Request movement for  "+regno[k];
//            String sms="Your registration no "+regno[k]+"  has been approved and sent to mail admin "+to_email;
//            String msghog="Registration no "+regno[k]+" has been approved successfully by you";
//            String mobilehog=null;
//            String smshog=null;
//            inform.sendToRabbit(from, to, msg, sub, sms, mobile, cc, attach);
//            inform.sendToRabbit(from, loggedinHog, msghog, sub, smshog, mobilehog, cc, attach);

  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
             pst=con.prepareStatement(sql_logs);
             pst.setString(1, " approve all request successfull by "+loggedinHog);
             pst.setString(2, loggedinHog);
             pst.executeUpdate();
             System.out.println("Activity Logs Inserted");
           }
           else{
           map.put("status_"+regno[k], "Failed to Approve");
           }
            
             }
             
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
         try {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());
             String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
             pst=con.prepareStatement(sql_logs);
             pst.setString(1, " approve all request failed by "+loggedinHog);
             pst.setString(2, loggedinHog);
             pst.executeUpdate();
             System.out.println("Activity Logs Inserted");
         } catch (SQLException ex1) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

         }

     return map;
     }
    
     public Map<String,Object> approveRequestHog(String registration_no,String loggedinHog){
     
     PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
         try {
             con = DbConnection.getConnection();
             String formname=formtype(registration_no);
         ArrayList<String> madminarr = fetchMailAdmins(formname);
         String to_email="";
         for(String madmin:madminarr){
         to_email=to_email+","+madmin;
         }
         to_email=to_email.replaceAll(",$", "").replaceAll("^,", "");
         
         
             String sql = "update final_audit_track set status='mail-admin_pending',to_email=?,to_mobile=?,to_name=?,to_datetime=? where registration_no=?";
             pst = con.prepareStatement(sql);
             pst.setString(1, to_email);
             pst.setString(2, "");
             pst.setString(3, "");
             pst.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(5, registration_no);
             int i=pst.executeUpdate();
             if(i>0){
                  String sql_status="insert into status(stat_form_type,stat_reg_no,stat_type,stat_forwarded_by_user,stat_remarks,stat_active,stat_forwarded_by_name,stat_forwarded_by_email,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by,stat_forwarded_to,stat_forwarded_to_user) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql_status);
             pst.setString(1, formtype(registration_no));
             pst.setString(2, registration_no);
             pst.setString(3, "mail-admin_pending");
             pst.setString(4, loggedinHog);
             pst.setString(5, "");
             pst.setString(6, "a");
             pst.setString(7, "");
             pst.setString(8, loggedinHog);
             pst.setString(9, ip);
             pst.setTimestamp(10, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(11, "h");
             pst.setString(12, "m");
             pst.setString(13, to_email);
           int j=pst.executeUpdate();
            if(j>0){
           
           map.put("status", "Approved Successfully and send to admin ");
           map.put("to_email", to_email);
           
//            CoordinatorPanelBean cpanelbean = getApplicantdetailsFinalAudit(registration_no);
//            Inform inform=new Inform();
//            String body="Your registration no "+registration_no+"  has been approved and sent to mail admin "+to_email;
//            String mobile=cpanelbean.getApplicant_mobile();
//            String[] cc=null;
//            String[] attach=null;
//            String from = loggedinHog;
//            String to=cpanelbean.getApplicant_email();
//            String msg="Your registration no "+registration_no+"  has been approved and sent to mail admin "+to_email;
//            String sub="Request movement for  "+registration_no;
//            String sms="Your registration no "+registration_no+"  has been approved and sent to mail admin "+to_email;
//            String msghog="Registration no "+registration_no+" has been approved successfully by you";
//            String mobilehog=null;
//            String smshog=null;
//            inform.sendToRabbit(from, to, msg, sub, sms, mobile, cc, attach);
//            inform.sendToRabbit(from, loggedinHog, msghog, sub, smshog, mobilehog, cc, attach);

  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
             pst=con.prepareStatement(sql_logs);
             pst.setString(1, " approve request successfull by "+loggedinHog);
             pst.setString(2, loggedinHog);
             pst.executeUpdate();
             System.out.println("Activity Logs Inserted");
           }
           else{
           map.put("status", "Failed to Approve");
           }
             
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
         try {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());
             String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
             pst=con.prepareStatement(sql_logs);
             pst.setString(1, " approve request failed by "+loggedinHog);
             pst.setString(2, loggedinHog);
             pst.executeUpdate();
             System.out.println("Activity Logs Inserted");
         } catch (SQLException ex1) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

         }

     return map;
     }
    
     public CoordinatorPanelBean getApplicantdetailsFinalAudit(String registration_no){
         PreparedStatement pst = null;
         ResultSet rs=null;
          CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select applicant_email,applicant_mobile,applicant_name from final_audit_track where registration_no=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1,registration_no);
             rs=pst.executeQuery();
             while(rs.next()){
              
               cpanelbean.setApplicant_email(rs.getString(1));
               cpanelbean.setApplicant_mobile(rs.getString(2));
               cpanelbean.setApplicant_name(rs.getString(3));
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if (rs != null) {
                   rs.close();
               }


           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         return cpanelbean;
     
     }
     
    public Map<String,Object> rejectRequestHog(String registration_no,String loggedinHog){
     PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
         try {
             con = DbConnection.getConnection();
             String sql="update final_audit_track set status='hog_rejected',to_datetime=? where registration_no=?";
             pst=con.prepareStatement(sql);
             pst.setTimestamp(1, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(2, registration_no);
             int i=pst.executeUpdate();
             if(i>0){
             String sql_status="insert into status(stat_form_type,stat_reg_no,stat_type,stat_forwarded_by_user,stat_remarks,stat_active,stat_forwarded_by_name,stat_forwarded_by_email,stat_forwarded_by_ip,stat_forwarded_by_datetime,stat_forwarded_by) values(?,?,?,?,?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql_status);
             pst.setString(1, formtype(registration_no));
             pst.setString(2, registration_no);
             pst.setString(3, "hog_rejected");
             pst.setString(4, "");
             pst.setString(5, "");
             pst.setString(6, "a");
             pst.setString(7, "");
             pst.setString(8, loggedinHog);
             pst.setString(9, ip);
             pst.setTimestamp(10, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
             pst.setString(11, "h");
           int j=pst.executeUpdate();
           if(j>0){
           
           map.put("status", "Rejected_Successfully");
           
//                       CoordinatorPanelBean cpanelbean = getApplicantdetailsFinalAudit(registration_no);
//            Inform inform=new Inform();
//            String body="Your registration no "+registration_no+"  has been rejected";
//            String mobile=cpanelbean.getApplicant_mobile();
//            String[] cc=null;
//            String[] attach=null;
//            String from = loggedinHog;
//            String to=cpanelbean.getApplicant_email();
//            String msg="Your registration no "+registration_no+"  has been rejected by "+loggedinHog;
//            String sub="Request movement for  "+registration_no;
//            String sms="Your registration no "+registration_no+"  has been rejected by "+loggedinHog;
//            String msghog="Registration no "+registration_no+" has been approved successfully by you";
//            String mobilehog=null;
//            String smshog=null;
//            inform.sendToRabbit(from, to, msg, sub, sms, mobile, cc, attach);
//            inform.sendToRabbit(from, loggedinHog, msghog, sub, smshog, mobilehog, cc, attach);
           
           
           }
           else{
           map.put("status", "Failed to Reject");
           }
             
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    
    
    return map;
    }
    
    //Add departments
    public Map<String,Object> addDepartmentHOG(CoordinatorPanelBean cpanelbean){
     int i=0;
    PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
       
         try {
             con = DbConnection.getConnection();
             String sql="insert into employment_coordinator (emp_category,emp_min_state_org,emp_dept,emp_coord_mobile,emp_coord_name,emp_coord_email,ip) values(?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getEmp_category());
             
//             pst.setString(2, cpanelbean.getEmp_min_state_org());
//             pst.setString(3, cpanelbean.getEmp_dept());
              switch (cpanelbean.getEmp_category()) {
                 case "Central":
                     pst.setString(2, cpanelbean.getEmp_min_state_org());
                     pst.setString(3, cpanelbean.getEmp_dept());
                     break;
                 case "State":
                     pst.setString(2, cpanelbean.getStateCode());
                     pst.setString(3, cpanelbean.getState_dept());
                     break;
                 case "Psu":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "Const":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                     
                 case "Nkn":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "Project":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "UT":
                     pst.setString(2, cpanelbean.getEmp_min_state_org());
                     pst.setString(3, cpanelbean.getEmp_dept());
                     break;
                 case "Others":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                  
                     break;
                 default:
                     break;
             }
           
             
             pst.setString(4, cpanelbean.getEmp_coord_mobile());
             pst.setString(5, cpanelbean.getEmp_coord_name());
             pst.setString(6, cpanelbean.getEmp_coord_email());
             pst.setString(7, cpanelbean.getEmp_ip());
            i= pst.executeUpdate();
            if(i>0){
            map.put("status", "department inserted");
              String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Co "+ cpanelbean.getEmp_coord_email()+"inserted add department by "+cpanelbean.getLogin_user_email());
                  pst.setString(2, cpanelbean.getLogin_user_email());
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
            
            }
            else{
                 map.put("status", "failed");
                  
            }
             
         } catch (ClassNotFoundException | SQLException ex) {
         try {
             String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
             pst=con.prepareStatement(sql_logs);
             pst.setString(1, ex.getMessage()+" Co "+ cpanelbean.getEmp_coord_email()+"inserted by "+cpanelbean.getLogin_user_email());
             pst.setString(2, cpanelbean.getLogin_user_email());
             pst.executeUpdate();
             System.out.println("Activity Logs Inserted");
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
             map.put("status", ex.getMessage());
         } catch (SQLException ex1) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
         }
         }
            finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    
         return map;
    }
    
     public Map<String, Object> addDepartmentRequest(CoordinatorPanelBean cpanelbean){
    int i=0;
    PreparedStatement pst = null;
         Map<String,Object> map = new HashMap<>();
         try {
             String hog="";
             con = DbConnection.getConnection();
             String sql="insert into hog_dept_request_status(emp_category,emp_min_state_org,emp_dept,other_dept_name,emp_email,requested_to,status) values(?,?,?,?,?,?,?)";
             pst = con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getEmp_category());
             pst.setString(2, cpanelbean.getEmp_min_state_org());
             pst.setString(3, cpanelbean.getEmp_dept());
             pst.setString(4, cpanelbean.getOther_dept());
             pst.setString(5, cpanelbean.getRequested_by());
             pst.setString(6, cpanelbean.getRequested_to());
             pst.setString(7, cpanelbean.getStatus());
              i=pst.executeUpdate();
              if(i>0){
                map.put("status", "Requested");
                map.put("hog_email", hog);
                
              }
              else{
               map.put("status", "Request failed");
              }
              
             
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             map.put("status", "Request failed");
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
            finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    
    
        return map;
    }
 
     //track user request not needed here
      public  Map<String,Object> trackRequestByHOG(String registration_no){
      PreparedStatement pst = null;
      ResultSet rs=null;
      List<CoordinatorPanelBean> list=new ArrayList<>();
        Map<String,Object> map=new HashMap();
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
                      String sql="select stat_reg_no,stat_form_type,stat_type,stat_forwarded_by,stat_forwarded_by_user,stat_forwarded_to,stat_forwarded_to_user,stat_remarks,stat_ip from status where stat_reg_no=?";
                      pst=conSlave.prepareStatement(sql);
                      pst.setString(1,registration_no);
                      rs=pst.executeQuery();
                      while(rs.next()){
                      CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                      cpanelbean.setRegistration_no(rs.getString(1));
                      cpanelbean.setForm_name(rs.getString(2));
                      cpanelbean.setStat_type(rs.getString(3));
                      cpanelbean.setStat_forwarded_by(rs.getString(4));
                      cpanelbean.setStat_forwarded_by_user(rs.getString(5));
                      cpanelbean.setStat_forwarded_to(rs.getString(6));
                      cpanelbean.setStat_forwarded_to_user(rs.getString(7));
                      list.add(cpanelbean);
                      
                      }
                      if(!list.isEmpty()){
                      map.put("status", "success");
                      map.put("list", list);
                      }
                      else{
                          map.put("status", "failed");
                      }
                      

         } catch (ClassNotFoundException | SQLException ex) {
               map.put("status", ex.getMessage());
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
         
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if (rs != null) {
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
         return map;
     }
     
      //public  Map<String,Object> updateDepartmentHOG(String category,String ministry,String old_dept,String new_dept){
     public  Map<String,Object> updateDepartmentHOG(CoordinatorPanelBean cpanelbean){
      PreparedStatement pst = null;
        Map<String,Object> map=new HashMap();
         try {
             con = DbConnection.getConnection();
             String sql="update employment_coordinator set emp_dept=? where emp_category=? and emp_min_state_org=? and emp_dept=?";
             pst=con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getNew_dept());
             
      
             
              switch (cpanelbean.getEmp_category()) {
                 case "Central":
                     pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getEmp_min_state_org());
                     pst.setString(4, cpanelbean.getEmp_dept());
                     break;
                 case "State":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getStateCode());
                     pst.setString(4, cpanelbean.getState_dept());
                     break;
                 case "Psu":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getOrg());
                     pst.setString(4, "");
                     break;
                 case "Const":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getOrg());
                     pst.setString(4, "");
                     break;
                     
                 case "Nkn":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getOrg());
                     pst.setString(4, "");
                     break;
                 case "Project":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getOrg());
                     pst.setString(4, "");
                     break;
                 case "UT":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getEmp_min_state_org());
                     pst.setString(4, cpanelbean.getEmp_dept());
                     break;
                 case "Others":
                      pst.setString(2, cpanelbean.getEmp_category());
                     pst.setString(3, cpanelbean.getOrg());
                     pst.setString(4, "");
                  
                     break;
                 default:
                     break;
             }
             int i = pst.executeUpdate();
             if(i>0){
             map.put("status", "updated");
                 //update in all base tables all old_dept with new_dept
                 List<String> tablelist = myTableList();
                 for(String tablename:tablelist){
                      
                 //update in table the dept
                        if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("Central")) {
                    
                           sql = "update " + tablename + " set department=? where employment = ? and ministry = ? and department = ?";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, cpanelbean.getNew_dept());
                            pst.setString(2, cpanelbean.getEmp_category());
                            pst.setString(3, cpanelbean.getEmp_min_state_org());
                            pst.setString(4, cpanelbean.getEmp_dept());
                            int j = pst.executeUpdate();
                            if(j>0){
                                System.out.println("updated:::::::"+tablename);
                            }
                        
                    }
                        
                        else if (cpanelbean.getEmp_category()  != null && cpanelbean.getEmp_category() .equalsIgnoreCase("UT")) {
                    
                           sql = "update " + tablename + " set department=? where employment = ? and ministry = ? and department = ?";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, cpanelbean.getNew_dept());
                            pst.setString(2, cpanelbean.getEmp_category());
                            pst.setString(3, cpanelbean.getEmp_min_state_org());
                            pst.setString(4, cpanelbean.getEmp_dept());
                            int j = pst.executeUpdate();
                              if(j>0){
                                System.out.println("updated:::::::: "+tablename);
                            }
                        
                    }
                          else if (cpanelbean.getEmp_category()   != null && cpanelbean.getEmp_category()  .equalsIgnoreCase("State")) {
                    
                           sql = "update " + tablename + " set department=? where employment = ? and state = ? and department = ?";
                            pst = con.prepareStatement(sql);
                            pst.setString(1, cpanelbean.getNew_dept());
                            pst.setString(2, cpanelbean.getEmp_category());
                            pst.setString(3, cpanelbean.getStateCode());
                            pst.setString(4, cpanelbean.getState_dept());
                            int j = pst.executeUpdate();
                              if(j>0){
                                System.out.println("updated::::::: "+tablename);
                            }
                        
                    }
                        
                        //else check employment,organization,department
                 
                 
                 }
                 
                 //update user_profile
           if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("Central")) {
           String sql_user_profile="update user_profile set department=? where employment=? and ministry=? and department=? "; 
           pst=con.prepareStatement(sql_user_profile);
               pst.setString(1, cpanelbean.getNew_dept());
               pst.setString(2, cpanelbean.getEmp_category());
               pst.setString(3, cpanelbean.getEmp_min_state_org());
               pst.setString(4, cpanelbean.getEmp_dept());
             int k = pst.executeUpdate();
             
             if(k>0){
                 System.out.println("updated:::::::department in userprofile "+cpanelbean.getNew_dept());
             }
           }
               if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("state")) {
           String sql_user_profile="update user_profile set department=? where employment=? and state=? and department=? "; 
           pst=con.prepareStatement(sql_user_profile);
                   pst.setString(1, cpanelbean.getNew_dept());
                   pst.setString(2, cpanelbean.getEmp_category());
                   pst.setString(3, cpanelbean.getStateCode());
                   pst.setString(4, cpanelbean.getState_dept());
             int k = pst.executeUpdate();
             
             if(k>0){
                 System.out.println("updated:::::::department in userprofile "+cpanelbean.getNew_dept());
             }
           }
                   if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("UT")) {
           String sql_user_profile="update user_profile set department=? where employment=? and ministry=? and department=? "; 
           pst=con.prepareStatement(sql_user_profile);
                pst.setString(1, cpanelbean.getNew_dept());
                   pst.setString(2, cpanelbean.getEmp_category());
                   pst.setString(3, cpanelbean.getEmp_min_state_org());
                   pst.setString(4, cpanelbean.getEmp_dept());
             int k = pst.executeUpdate();
             
             if(k>0){
                 System.out.println("updated:::::::department in userprofile "+cpanelbean.getNew_dept());
             }
           }
             
             }
             else{
             map.put("status", "failed");
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             map.put("status", ex.getMessage());
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
            finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
        
        return map;
     }
     
     
      public  Map<String,Object> updateDepartmentPendingRequestById(CoordinatorPanelBean cpanelbean){
        PreparedStatement pst = null;
        Map<String,Object> map=new HashMap();
       
        
         try {
             con = DbConnection.getConnection();
             String sql = "update hog_dept_request_status set other_dept_name=? where id=?";
             pst = con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getOther_dept());
             pst.setInt(2, cpanelbean.getDept_id());
             int i=pst.executeUpdate();
             if(i>0){
                map.put("status","success");

             }
             else{
                  map.put("status","failed");
             }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
              map.put("status","failed");
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
              finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    return map;
    }
      public String getDepartmentRequestedBy(int id){
       PreparedStatement pst = null;
        ResultSet rs = null;
        String requested_by="";
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_email from hog_dept_request_status where id=?";
             pst=conSlave.prepareStatement(sql);
             pst.setInt(1, id);
             rs=pst.executeQuery();
             while(rs.next()){
                 requested_by=rs.getString(1);
             
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
              finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         return requested_by;
         
      }
     
      public  Map<String,Object> viewDepartmentPendingRequestById(int id){
        PreparedStatement pst = null;
        ResultSet rs = null;
         int totalcount=0;
        List<CoordinatorPanelBean> list = new ArrayList<>();
        Map<String,Object> map=new HashMap();
       
        
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_category,emp_min_state_org,emp_dept,emp_email,status,id,other_dept_name,id from hog_dept_request_status where status='pending' and id=?";
             pst=conSlave.prepareStatement(sql);
             pst.setInt(1, id);
                     
             rs=pst.executeQuery();
             while(rs.next()){
             CoordinatorPanelBean cpanelBean=new CoordinatorPanelBean();
             cpanelBean.setEmp_category(rs.getString(1));
             cpanelBean.setEmp_min_state_org(rs.getString(2));
             cpanelBean.setEmp_dept(rs.getString(3));
            
             cpanelBean.setRequested_by(rs.getString(4));
             cpanelBean.setStatus(rs.getString(5));
             cpanelBean.setEmp_id(rs.getInt(6));
             //Editable
             cpanelBean.setOther_dept(rs.getString(7));
              //end Editable
             cpanelBean.setDept_id(rs.getInt(8));
             list.add(cpanelBean);
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
              map.put("status","failed");
              map.put("totalcount",totalcount);
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
              finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
         map.put("status",list);
    return map;
    }
     
     
    public  Map<String,Object> showDepartmentPendingRequest(String email){
        PreparedStatement pst = null;
        ResultSet rs = null;
         int totalcount=0;
        List<CoordinatorPanelBean> list = new ArrayList<>();
        Map<String,Object> map=new HashMap();
        int count=checkHOG(email);
        if (count == 0) {
            map.put("status", "You are not an Hog");
            map.put("totalcount",totalcount);
            return map;
        }
        
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
//             Ldap ldap=new Ldap();
//            Set<String> aliases = ldap.getAliases(email);
//            for(String alias:aliases){
             String sql="select emp_category,emp_min_state_org,emp_dept,emp_email,status,id,other_dept_name,id from hog_dept_request_status where status='pending' and requested_to=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, email);
                     
             rs=pst.executeQuery();
             while(rs.next()){
             CoordinatorPanelBean cpanelBean=new CoordinatorPanelBean();
             cpanelBean.setEmp_category(rs.getString(1));
             cpanelBean.setEmp_min_state_org(rs.getString(2));
             cpanelBean.setEmp_dept(rs.getString(3));
             cpanelBean.setRequested_by(rs.getString(4));
             cpanelBean.setStatus(rs.getString(5));
             cpanelBean.setEmp_id(rs.getInt(6));
             cpanelBean.setOther_dept(rs.getString(7));
             cpanelBean.setDept_id(rs.getInt(8));
              totalcount++;
             list.add(cpanelBean);
             }
             
           // }
             
         } catch (ClassNotFoundException | SQLException ex) {
              map.put("status","failed");
              map.put("totalcount",totalcount);
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
               finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
         map.put("status",list);
         map.put("totalcount",totalcount);
    return map;
    }
    
   
    //NOT USED WRONG CODE
     public int approveDepartment(String emp_category,String emp_min_state_org,String department,String approver_email,String emp_coord_email){
        int i=0,j=0;
        PreparedStatement pst = null;
        ResultSet rs = null;
         try {
             con = DbConnection.getConnection();
             String sql="update hog_dept_request_status  set status='approved' where emp_dept='other' and other_dept_name=? and requested_to=?";
             pst=con.prepareStatement(sql);
             pst.setString(1, department);
             pst.setString(2, approver_email);
             i=pst.executeUpdate();
             if(i>0){
                // CoordinatorPanelBean cpanelBean = getApprovedDepartmentForInsert(department);
                 
                 String sql_update="update employment_coordinator set emp_dept=? where emp_category=? and emp_min_state_org=? and emp_coord_email=?";
                 pst=con.prepareStatement(sql_update);
                 pst.setString(1, department);
                 pst.setString(2,emp_category);
                 pst.setString(3, emp_min_state_org);
                 pst.setString(4, emp_coord_email);
                 j=pst.executeUpdate();
                 
                  
                 
             }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
               finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    return j;
    }
   //insert into employment_coordinator (emp_category,emp_min_state_org,emp_dept,emp_coord_email) values(?,?,?,?) 
    public Map<String,Object> fetchDepartmentsToApproved(int id){
        Map<String,Object> map=new HashMap<String,Object>();
         PreparedStatement pst = null;
        ResultSet rs = null;
        
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_category,emp_min_state_org,other_dept_name,emp_email,requested_to from hog_dept_request_status where id=?";
             pst=conSlave.prepareStatement(sql);
             pst.setInt(1, id);
             rs=pst.executeQuery();
             while(rs.next()){
             map.put("emp_category", rs.getString(1));
             map.put("emp_min_state_org", rs.getString(2));
             map.put("other_dept_name", rs.getString(3));
             map.put("emp_email", rs.getString(4));
             map.put("requested_to", rs.getString(5));
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         return map;
    }
    public Map<String,Object> approvePendingDepartments(int id,String emp_coord_email){
        int i=0,j=0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Map<String,Object> map=new HashMap<String,Object>();
        String emp_email="";
        String hog_email="";
         try {
             con = DbConnection.getConnection();
             String sql="update hog_dept_request_status  set status='approved' where id=?";
             pst=con.prepareStatement(sql);
             pst.setInt(1, id);
             i=pst.executeUpdate();
             
             
             
             if(i>0){
                
                 Map<String, Object> dept = fetchDepartmentsToApproved(id);
                 String emp_category = (String) dept.get("emp_category");
                 String emp_min_state_org = (String) dept.get("emp_min_state_org");
                 String other_dept_name = (String) dept.get("other_dept_name");
                  emp_email=(String) dept.get("emp_email");
                  hog_email=(String) dept.get("requested_to");
                 
                 String sqlinsert="insert into employment_coordinator (emp_category,emp_min_state_org,emp_dept,emp_coord_email) values(?,?,?,?)";
                 pst=con.prepareStatement(sqlinsert);
                 pst.setString(1, emp_category);
                 pst.setString(2, emp_min_state_org);
                 pst.setString(3, other_dept_name);
                 pst.setString(4, emp_coord_email);
                 j=pst.executeUpdate();
                 if(j>0){
                  map.put("status", "Approved");
                    //***Send email to user if approved***
            Inform inform=new Inform();
            String body="Hello "+emp_email+", your department request has been approved by hog "+hog_email;
            String mobile="";
            String[] cc=null;
            String[] attach=null;
            String from = hog_email;
            String to=emp_email;
            String msg="Hello "+emp_email+", your department request has been approved by hog "+hog_email;
            String sub="New coordinator has been added";
            String sms="Hello "+emp_email+", your department request has been approved by hog "+hog_email;;
            
            //inform.sendToRabbit(from, to, msg, sub, sms, mobile, cc, attach);

 String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Hello "+emp_email+", your department request has been approved by hog "+hog_email);
                  pst.setString(2, hog_email);
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
                  
                 }
                 else{
                 map.put("status", "Something went Wrong");
                 }
         
                 
             }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
            try {
                Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
                map.put("status", ex.getMessage());
                String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                pst=con.prepareStatement(sql_logs);
                pst.setString(1, ex.getMessage()+" "+emp_email+",  department request failed to approved by hog "+hog_email);
                pst.setString(2, hog_email);
                pst.executeUpdate();
                System.out.println("Activity Logs Inserted");
            } catch (SQLException ex1) {
                Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
            }
                  
         }
               finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    return map;
    }
    
    public  Map<String,Object>  getEmpEmailRejectDept(int id){
        String email="";
        String requested_to="";
         PreparedStatement pst = null;
        ResultSet rs = null;
        Map<String,Object> map=new HashMap<>();
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_email,requested_to from hog_dept_request_status where id=?";
             pst=conSlave.prepareStatement(sql);
             pst.setInt(1, id);
             rs=pst.executeQuery();
             if(rs.next()){
             email=rs.getString(1);
             requested_to=rs.getString(2);
            
             }
              map.put("email", email);
              map.put("requested_to", requested_to);
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
               rs.close();
                       
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
        
        
        return map;
    }
     public Map<String,Object> rejectDepartment(String id){
        int i=0,j=0;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Map<String,Object> map=new HashMap<>();
         try {
             con = DbConnection.getConnection();
             String sql="update hog_dept_request_status  set status='rejected' where id=?";
             pst=con.prepareStatement(sql);
             pst.setString(1, id);
             i=pst.executeUpdate();
           if(i>0){
           map.put("status","Rejected Successfully");
           
            Map<String,Object>  details=getEmpEmailRejectDept(Integer.parseInt(id));
            String emp_email=(String)details.get("email");
            String requested_to=(String)details.get("requested_to");
            Inform inform=new Inform();
            String body="Hello "+emp_email+", your department request has been rejected by "+requested_to;
            String mobile="";
            String[] cc=null;
            String[] attach=null;
            String from = requested_to;
            String to=emp_email;
            String msg="Hello "+emp_email+", your department request has been rejected by "+requested_to;
            String sub="New coordinator has been added";
            String sms="Hello "+emp_email+", your department request has been rejected by "+requested_to;;
            
           // inform.sendToRabbit(from, to, msg, sub, sms, mobile, cc, attach);
           
           }
           else{
           map.put("status","failed");
           }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
               map.put("status",ex.getMessage());
         }
               finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    return map;
    }
 
    
    public int checkHOG(String email){
       String sql = "select count(*) as count from hog_ministry where hog_email=?";
       PreparedStatement pst = null;
       ResultSet rs = null;
       int count = 0;
       try {
           con = DbConnection.getConnection();
           conSlave = DbConnection.getSlaveConnection(); //29dec2021
           pst = conSlave.prepareStatement(sql);
           pst.setString(1, email);
           rs = pst.executeQuery();
           while (rs.next()) {
               count++;

           }
       } catch (ClassNotFoundException | SQLException ex) {
           Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
       }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    return count;
    }
    //code for getting all Pending Entries of coordinator according to ministry
 
     
 
    public String getHog(String ministry){
     PreparedStatement pst = null;
     ResultSet rs=null;
     String hog="";
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql = "select hog_email from hog_ministry where ministry=?";
             pst = conSlave.prepareStatement(sql);
             pst.setString(1, ministry);
             rs = pst.executeQuery();
             while (rs.next()) {
                 hog = rs.getString(1);
             }

              
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
               finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         return hog;
    }
    
    public ArrayList<String> getHogMinistry(String email){
        ArrayList<String> list=new ArrayList<String>();
        PreparedStatement pst = null;
         ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select ministry from hog_ministry where hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, email);
             rs=pst.executeQuery();
             while(rs.next()){
                 list.add(rs.getString(1));
             
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
            return list;
    }
    
    
 
   
    
  
    //Insert into employment_coordinator table
        //main method
   public HashMap<String, Object> insertCoordinatorTable(CoordinatorPanelBean cpanelbean){
          HashMap<String, Object> map = new HashMap<String, Object>();

         int i = 0;
         PreparedStatement pst = null;
         ResultSet rsfaudit=null;
         ResultSet rs1=null;
         try {
             
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="insert into employment_coordinator(emp_category,emp_min_state_org,emp_dept,emp_sub_dept,"
                     + "emp_mail_acc_cat,emp_sms_acc_cat,emp_domain,emp_bo_id,emp_coord_mobile,emp_coord_name,"
                     + "emp_coord_email,emp_admin_email,emp_status,emp_addedby,ip) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
             pst=con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getEmp_category());
             
             switch (cpanelbean.getEmp_category()) {
                 case "Central":
                     pst.setString(2, cpanelbean.getEmp_min_state_org());
                     pst.setString(3, cpanelbean.getEmp_dept());
                     break;
                 case "State":
                     pst.setString(2, cpanelbean.getStateCode());
                     pst.setString(3, cpanelbean.getState_dept());
                     break;
                 case "Psu":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "Const":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                     
                 case "Nkn":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "Project":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "UT":
                     pst.setString(2, cpanelbean.getEmp_min_state_org());
                     pst.setString(3, cpanelbean.getEmp_dept());
                     break;
                 case "Others":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                  
                     break;
                 default:
                     break;
             }
             //to be switched
//             if(cpanelbean.getEmp_dept().equals("other")){
//                Map<String, Object> add_dept = addDepartmentRequest(cpanelbean);
//                     String status = (String) add_dept.get("status");
//                     String hog_email = (String) add_dept.get("hog_email");
//                     if (status.equals("Requested")) {
//                         map.put("status_others", "Your Department '" + cpanelbean.getOther_dept() + "' is sent for approval to hog " + hog_email);
//                     } else {
//                         map.put("status", "Something went wrong");
//                         return map;
//                     }
//             
//             }
             
             pst.setString(4, cpanelbean.getEmp_sub_dept());
             pst.setString(5, cpanelbean.getEmp_mail_acc_cat());
             pst.setString(6, cpanelbean.getEmp_sms_acc_cat());
             pst.setString(7, cpanelbean.getEmp_domain());
             pst.setString(8, cpanelbean.getEmp_bo_id());
             pst.setString(9, cpanelbean.getEmp_coord_mobile());
             pst.setString(10, cpanelbean.getEmp_coord_name());
             pst.setString(11, cpanelbean.getEmp_coord_email());
             pst.setString(12, cpanelbean.getEmp_admin_email());
             pst.setString(13, "a");
             pst.setString(14, cpanelbean.getLogin_user_email());
             pst.setString(15, cpanelbean.getEmp_ip());
             i = pst.executeUpdate();
             if(i>0){
                map.put("status", "inserted");
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Co "+ cpanelbean.getEmp_coord_email()+"inserted by "+cpanelbean.getLogin_user_email());
                  pst.setString(2, cpanelbean.getLogin_user_email());
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
                  
                  
                  
                  
                  //insertion in to_email;
                  String sqlfinalaudittrack="select track_id, registration_no,to_email from final_audit_track where status='coordinator_pending'";
                  pst=conSlave.prepareStatement(sqlfinalaudittrack);
                  rsfaudit=pst.executeQuery();
                  String query="";
                  while(rsfaudit.next()){
                      int track_id = rsfaudit.getInt(1);
                      String regno = rsfaudit.getString(2);
                      String to_email = rsfaudit.getString(3);
                   
                       String tablename = myTable(regno);
                         if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("Central")) {
                        if (cpanelbean.getEmp_dept()==null||cpanelbean.getEmp_dept().isEmpty()) {
                            query = "select id from " + tablename + " where employment = 'Central' and ministry = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, cpanelbean.getEmp_min_state_org());
                            pst.setString(2, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = 'Central' and ministry = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, cpanelbean.getEmp_min_state_org());
                            pst.setString(2, cpanelbean.getEmp_dept());
                            pst.setString(3, regno);
                        }
                    } else if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("UT")) {
                        if (cpanelbean.getEmp_dept().isEmpty()) {
                            query = "select id from " + tablename + " where employment = ? and ministry = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, cpanelbean.getEmp_category());
                            pst.setString(2, cpanelbean.getEmp_min_state_org());
                            pst.setString(3, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = ? and ministry = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, cpanelbean.getEmp_category());
                            pst.setString(2,  cpanelbean.getEmp_min_state_org());
                            pst.setString(3, cpanelbean.getEmp_dept());
                            pst.setString(4, regno);
                        }
                    } 
                     else if (cpanelbean.getEmp_category() != null && cpanelbean.getEmp_category().equalsIgnoreCase("State")) {
                        if (cpanelbean.getEmp_dept().isEmpty()) {
                            query = "select id from " + tablename + " where employment = ? and state = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, cpanelbean.getEmp_category());
                            pst.setString(2, cpanelbean.getStateCode());
                            pst.setString(3, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = ? and state = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, cpanelbean.getEmp_category());
                            pst.setString(2,  cpanelbean.getStateCode());
                            pst.setString(3, cpanelbean.getState_dept());
                            pst.setString(4, regno);
                        }
                    } 
                    
                     else  {
                        query = "select id from " + tablename + " where employment = ? and organization = ? and registration_no=?";
                        pst = conSlave.prepareStatement(query);
                        pst.setString(1, cpanelbean.getEmp_category());
                        pst.setString(2, cpanelbean.getOrg());
                        pst.setString(3, regno);
                    }
                        rs1=pst.executeQuery();
                        if(rs1.next()){
                            System.out.println("registration no to email data be changed in final audit track");
                            //delete contains of coord_email_aliases_tobe_deleted in to_email and update finalaudittrack
                           //pass to replace to_email.... coord_email_aliases_tobe_deleted,to_email if not match no replacement
                            to_email = addMatchers(cpanelbean.getEmp_coord_email(), to_email);
                            updateFinalAuditTrack(track_id, to_email, regno,"insert");
                        }
                   
                  }
                  
                  
                  
                  
                  
                  
                  
             }
             
             //***Send email to support for BO id insertion***
//            Inform inform=new Inform();
//            String body="Coordinator "+cpanelbean.getCoordinator_email()+" of department "+cpanelbean.getEmp_dept()+" has been added by "+cpanelbean.getLogin_user_email()+". Please enter the further details required for this coordinator";
//            String mobile="";
//            String[] cc=null;
//            String[] attach=null;
//            String from = cpanelbean.getLogin_user_email();
//            String to="support@gov.in";
//            String msg="Coordinator "+cpanelbean.getCoordinator_email()+" of department "+cpanelbean.getEmp_dept()+" has been added by "+cpanelbean.getLogin_user_email()+". Please enter the further details required for this coordinator";;
//            String sub="New coordinator has been added";
//            String sms=null;
//            
//            inform.sendToRabbit(from, to, msg, sub, sms, mobile, cc, attach);
             
             
             
             
           
             
         } catch (ClassNotFoundException | SQLException ex) {
              try {
                  map.put("status", ex.getMessage());
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, ex.getMessage()+"--Co "+ cpanelbean.getEmp_coord_email()+"inserted by "+cpanelbean.getLogin_user_email());
                  pst.setString(2, cpanelbean.getLogin_user_email());
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
                  Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
              } catch (SQLException ex1) {
                  Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
              }
         }
           finally {
            try {
                if (pst != null) {
                    pst.close();
                }
              
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
   return map;
   }
     //EO Insert into employment_coordinator table
    
   //viewAll Coordinators
       //main method
   
  
   
  public List<CoordinatorPanelBean> getMinistryInfoOfHOG(String hog_email)
   {
      List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
       PreparedStatement pst = null;
       ResultSet rs = null;
       String sql="select category,ministry,hog_name,hog_mobile,hog_email from hog_ministry where hog_email=?";
           
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, hog_email);
             rs=pst.executeQuery();
             while(rs.next()){
                CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                cpanelbean.setEmp_category(rs.getString(1));
                cpanelbean.setEmp_min_state_org(rs.getString(2));
                cpanelbean.setHog_name(rs.getString(3));
                cpanelbean.setHog_mobile(rs.getString(4));
                cpanelbean.setHog_email(rs.getString(5));
//                cpanelbean.setType(rs.getString(6));
                list.add(cpanelbean);
             
             }
         } catch (SQLException | ClassNotFoundException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
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
//   
//   public List<CoordinatorPanelBean> getMinistryInfoOfHOD(String hod_email)
//   {
//      List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
//       PreparedStatement pst = null;
//       ResultSet rs = null;
//       String sql="select category,ministry,hod_name,hod_mobile,hod_email,type from hod_ministry where hod_email=?";
//           
//         try {
//             con = DbConnection.getConnection();
//             pst=con.prepareStatement(sql);
//             pst.setString(1, hod_email);
//             rs=pst.executeQuery();
//             while(rs.next()){
//                CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
//                cpanelbean.setEmp_category(rs.getString(1));
//                cpanelbean.setEmp_min_state_org(rs.getString(2));
//                cpanelbean.setHod_name(rs.getString(3));
//                cpanelbean.setHod_mobile(rs.getString(4));
//                // cpanelbean.setType(rs.getString(5));
//                cpanelbean.setHod_email(rs.getString(5));
//               
//                list.add(cpanelbean);
//             
//             }
//         } catch (SQLException | ClassNotFoundException ex) {
//             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
//         }
//           finally {
//           try {
//               if (pst != null) {
//                   pst.close();
//               }
//             
//
//           } catch (Exception e) {
//               e.printStackTrace();
//           }
//       }
//      
//       return list;
//   }
   
   
   
   
   public Map<String,Object> getCountsRequestCoordinator(String loggedinHog){
   PreparedStatement pst=null;
   Map<String,Object> map=new HashMap();
   ResultSet rs=null;

         try {
             con = DbConnection.getConnection();
             CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();            
             cpanelbean.setPending_request_count(getTotalPendingRequestCountOfCoordinator(loggedinHog));
             cpanelbean.setCompleted_request_count(getTotalCompletedRequestCountOfCoordinator(loggedinHog));
             cpanelbean.setForwarded_request_count(getTotalForwardedRequestCountOfCoordinator(loggedinHog));
             cpanelbean.setRejected_request_count(getTotalRejectedRequestCountOfCoordinator(loggedinHog));
        
             map.put("totalpendingcountcoord", cpanelbean.getPending_request_count());
            
             map.put("totalcompletedcountcoord", cpanelbean.getCompleted_request_count());
             
             map.put("totalforwardedcountcoord", cpanelbean.getForwarded_request_count());
             
             map.put("totalrejectedcountcoord", cpanelbean.getRejected_request_count());
             
             map.put("status", "success");

         } catch (ClassNotFoundException ex) {
             map.put("status", ex.getMessage());
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
        finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
   
   
   return map;
   }
   

   
   public List<CoordinatorPanelBean> viewAllCoordinators(String loggedinHog,UserData userdata){
   List<CoordinatorPanelBean> list=new ArrayList<>();
   PreparedStatement pst=null;
   ResultSet rs=null;
   
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
       List<CoordinatorPanelBean> minList = null;
       minList = getMinistryInfoOfHOG(loggedinHog);
      
 
                  int count_pending=0; 
                  int count_completed=0;
                  int count_forwarded=0;
                  int count_rejected=0;
                  
                      
         for(CoordinatorPanelBean min:minList){
             String sql="select emp_id,emp_category,emp_min_state_org,emp_dept,emp_sub_dept,"
                     + "emp_mail_acc_cat,emp_sms_acc_cat,emp_domain,emp_bo_id,emp_coord_mobile,emp_coord_name,"
                     + "emp_coord_email,emp_admin_email,emp_status,emp_createdon,emp_addedby,ip from employment_coordinator where emp_status='a' and emp_category=? and emp_min_state_org=?";
             pst = conSlave.prepareStatement(sql);
             pst.setString(1, min.getEmp_category());
             pst.setString(2, min.getEmp_min_state_org());
             rs=pst.executeQuery();
             while(rs.next()){
             CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
             cpanelbean.setEmp_id(rs.getInt("emp_id"));
             cpanelbean.setEmp_category(rs.getString("emp_category"));
             cpanelbean.setEmp_min_state_org(rs.getString("emp_min_state_org"));
             cpanelbean.setEmp_dept(rs.getString("emp_dept"));
             cpanelbean.setEmp_sub_dept(rs.getString("emp_sub_dept"));
             cpanelbean.setEmp_mail_acc_cat(rs.getString("emp_mail_acc_cat"));
             cpanelbean.setEmp_sms_acc_cat(rs.getString("emp_sms_acc_cat"));
             cpanelbean.setEmp_domain(rs.getString("emp_domain"));
             cpanelbean.setEmp_bo_id(rs.getString("emp_bo_id"));
             cpanelbean.setEmp_coord_mobile(rs.getString("emp_coord_mobile"));
             cpanelbean.setEmp_coord_name(rs.getString("emp_coord_name"));
             cpanelbean.setEmp_coord_email(rs.getString("emp_coord_email"));
             cpanelbean.setEmp_admin_email(rs.getString("emp_admin_email"));
             
             cpanelbean.setEmp_status(rs.getString("emp_status"));
             cpanelbean.setEmp_createdon(rs.getString("emp_createdon"));
             cpanelbean.setEmp_addedby(rs.getString("emp_addedby"));
             cpanelbean.setEmp_ip(rs.getString("ip"));
             //overridden for check
                 int temppending = getPendingRequestCountOfCoordinatorByIdMinwise(cpanelbean.getEmp_id(), loggedinHog);
                 cpanelbean.setPending_request_count(temppending);
                 count_pending = count_pending + temppending;
                 cpanelbean.setTotal_count_pending(count_pending);
                 
                 //overridden for check
                 int tempcompleted = getCompletedRequestCountOfCoordinatorByIdMinwise(cpanelbean.getEmp_id(), loggedinHog);
                 cpanelbean.setCompleted_request_count(tempcompleted);
                 count_completed = count_completed + tempcompleted;
                 cpanelbean.setTotal_count_completed(count_completed);
                 
                 int tempforwarded = getForwardedRequestCountOfCoordinatorByIdMinwise(cpanelbean.getEmp_id(), loggedinHog);
                 cpanelbean.setForwarded_request_count(tempforwarded);
                 count_forwarded = count_forwarded + tempforwarded;
                 cpanelbean.setTotal_count_forwarded(count_forwarded);
                 
                 int temprejected = getRejectedRequestCountOfCoordinatorByIdMinwise(cpanelbean.getEmp_id(), loggedinHog);
                 cpanelbean.setRejected_request_count(temprejected);
                 count_rejected = count_rejected + temprejected;
                 cpanelbean.setTotal_count_rejected(count_rejected);
                 
              list.add(cpanelbean);
            
             }
            
         }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
   
        finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
   
   
   return list;
   }
 
   
    public int getPendingRequestCountOfCoordinatorByIdMinwise(int id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         Map<String, Object> coorddetails = getCoordinatorEmail(id);
//          map.put("emp_category", rs.getString(1));
//               map.put("emp_min_state_org", rs.getString(2));
//               map.put("emp_dept", rs.getString(3));
//               map.put("emp_coord_email", rs.getString(4));
         String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");

       System.out.println("-----"+coord_email+"-----"+loggedinHog);
       
         List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
       
         try {
             
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             
          
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile from final_audit_track where status='coordinator_pending' and to_email like '%"+coord_email+"%'";
             pst=conSlave.prepareStatement(sql);
             
             
             rs=pst.executeQuery();
             while(rs.next()){
                 String regno=rs.getString(2);
                 String tablename = myTable(regno);
                  if(category.equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                  else if(category.equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                   else if(category.equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                   else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                    count++;
                 }
        
             
             }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
    
   //STREAM API TEST 
  
    //NOT USED
    public int getStreamCall(String alias,String category,String min_state_org){
        int count=0;
           PreparedStatement pst=null;
             
             ResultSet rs=null;
             ResultSet rs1=null;
         try {
          
             
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile from final_audit_track where status='coordinator_pending' and to_email like '%"+alias+"%'";
             pst=con.prepareStatement(sql);
             
             
             rs=pst.executeQuery();
             while(rs.next()){
                 String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(category.equalsIgnoreCase("Central")){
                     String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                     pst=con.prepareStatement(sqlbase);
                     pst.setString(1, category);
                     pst.setString(2, min_state_org);
                     //pst.setString(3, dept);
                     pst.setString(3, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){
                     String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                     pst=con.prepareStatement(sqlbase);
                     pst.setString(1, category);
                     pst.setString(2, min_state_org);
                     //pst.setString(3, dept);
                     pst.setString(3, regno);
                 }
                 else if(category.equalsIgnoreCase("UT")){
                     String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                     pst=con.prepareStatement(sqlbase);
                     pst.setString(1, category);
                     pst.setString(2, min_state_org);
                     //pst.setString(3, dept);
                     pst.setString(3, regno);
                 }
                 else{
                     String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                     pst=con.prepareStatement(sqlbase);
                     pst.setString(1, category);
                     pst.setString(2, min_state_org);
                     //pst.setString(3, dept);
                     pst.setString(3, regno);
                 }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                     count++;
                 }
             }
         } catch (SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
               if(rs1!=null){
                   rs1.close();
               }
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         return count;
    }
    //********************HOG STATS*****************
      public int getTotalPendingRequestCountOfHOG(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
       
         try {
             
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
                 
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile from final_audit_track where status='hog_pending' and hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, loggedinHog);
             //Check here for ministry and department
             
             rs=pst.executeQuery();
             while(rs.next()){
                 String regno=rs.getString(2);
                 String tablename = myTable(regno);
                   if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 
                 pst.setString(3, regno);
                   }
                  else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                   }
                   else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                   }
                    else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                   }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                    count++;
                 }
             }
             }
             
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
      
      
      
       public int getTotalCompletedRequestCountOfHOG(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
             String sql="select * from final_audit_track where hog_email=? and status='Completed'";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, loggedinHog);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                   else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                    else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             }
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
       return count;
   }
       
          public int getTotalForwardedRequestCountOfHOG(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
             String sql="select * from final_audit_track where hog_email=? and status!='Completed'";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, loggedinHog);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                  if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                   else { 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             }
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
    
       public int getTotalRejectedRequestCountOfHog(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
             String sql="select * from final_audit_track where status='hog_rejected' and hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, loggedinHog);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                   else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 
                 
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             }
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
    
    
    //End of Stream api test
   //NOT USED HERE 
    public int getTotalPendingRequestCountOfCoordinator(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
       
         try {
             
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
                 
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile from final_audit_track where status='coordinator_pending'";
             pst=conSlave.prepareStatement(sql);
             //Check here for ministry and department
             
             rs=pst.executeQuery();
             while(rs.next()){
                 String regno=rs.getString(2);
                 String tablename = myTable(regno);
                   if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 
                 pst.setString(3, regno);
                   }
                  else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                   }
                   else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                   }
                    else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                   }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                    count++;
                 }
             }
             }
             
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
             finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
   
   //NOT USED HERE
    public int getTotalCompletedRequestCountOfCoordinator(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
             String sql="select * from final_audit_track where coordinator_email!='' and status='Completed'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                   else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                    else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             }
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
       return count;
   }
    
    
     public int getCompletedRequestCountOfCoordinatorByIdMinwise(int id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
    
      Map<String, Object> coorddetails = getCoordinatorEmail(id);
//         
         String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");
          
//          Ldap ldap=new Ldap();
//         Set<String> aliases = ldap.getAliases(coord_email);
//          
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             //for(String alias:aliases){
             String sql="select * from final_audit_track where coordinator_email=? and status='Completed'";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coord_email);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(category.equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                  pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else if(category.equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             //}
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
       return count;
   }
    
 
    //NOT USED HERE
      public int getTotalForwardedRequestCountOfCoordinator(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
             String sql="select * from final_audit_track where coordinator_email!='' and status!='Completed'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                  if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                   else { 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             }
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
      
        public int getForwardedRequestCountOfCoordinatorByIdMinwise(int id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
              Map<String, Object> coorddetails = getCoordinatorEmail(id);
//         
         String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");
             
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
//              Ldap ldap=new Ldap();
//         Set<String> aliases = ldap.getAliases(coord_email);
         
         //for(String alias:aliases){
             String sql="select * from final_audit_track where coordinator_email=? and status!='Completed'";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coord_email);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(category.equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                  pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                  else if(category.equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                   else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
            // }
         }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
  
     //NOT USED HERE
      public int getTotalRejectedRequestCountOfCoordinator(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             for(CoordinatorPanelBean min:minlist){
             String sql="select * from final_audit_track where status='coordinator_rejected'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(min.getEmp_category().equalsIgnoreCase("Central")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 else if(min.getEmp_category().equalsIgnoreCase("State")){ 
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                   else if(min.getEmp_category().equalsIgnoreCase("UT")){ 
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 else{ 
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                 }
                 
                 
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
             }
             }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
      
       public int getRejectedRequestCountOfCoordinatorByIdMinwise(int id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
         try {
                Map<String, Object> coorddetails = getCoordinatorEmail(id);
         String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             
//              Ldap ldap=new Ldap();
//         Set<String> aliases = ldap.getAliases(coord_email);
         
            // for(String alias:aliases){
             String sql="select * from final_audit_track where coordinator_email=? and status='coordinator_rejected'";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coord_email);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(category.equalsIgnoreCase("Central")){     
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){     
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1,category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                    else if(category.equalsIgnoreCase("UT")){     
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                  pst.setString(4, regno);
                 }
                    else {     
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 
                 rs1=pst.executeQuery();
                 if (rs1.next()) {

                     count++;
                 }
             
            // }
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
 
     //aliases check
        public List<CoordinatorPanelBean> getPendingRequestOfHog(String loggedinHog){
  
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
        
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
   List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
                for(CoordinatorPanelBean min:minlist){
                 
             
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where status='hog_pending' and to_email like '%"+loggedinHog+"%'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                if(min.getEmp_category().equalsIgnoreCase("Central")){    
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                }
                else if(min.getEmp_category().equalsIgnoreCase("State")){
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                }
                else if(min.getEmp_category().equalsIgnoreCase("UT")){
                String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                
                }
                else{
                String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                
                }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                 
                 System.out.println("Filter against ministry");
                 CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                 cpanelbean.setTrackid(rs.getInt(1));
                 cpanelbean.setRegistration_no(rs.getString(2));
                 cpanelbean.setApplicant_email(rs.getString(3));
                 cpanelbean.setApplicant_mobile(rs.getString(4));
                 cpanelbean.setApplicant_name(rs.getString(5));
                 cpanelbean.setCa_email(rs.getString(6));
                 cpanelbean.setCa_mobile(rs.getString(7));
                 cpanelbean.setStatus(rs.getString(8));
                 list.add(cpanelbean);
                 
             }
             }
             
                }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
         
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
       return list;
   }
        
          public int getPendingRequestCountOfHog(String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
   List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
                for(CoordinatorPanelBean min:minlist){
                 
             
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where status='hog_pending' and to_email like '%"+loggedinHog+"%'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                  if(min.getEmp_category().equalsIgnoreCase("Central")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("State")){  
                   String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                     else if(min.getEmp_category().equalsIgnoreCase("UT")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                    else{  
                   String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                 
                 count++;
                 
             }
             }
                }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
       
     
          
            public List<CoordinatorPanelBean> getPendingRequestOfCoordinatorMinwise(int emp_id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         //  String coordinator_email =getCoordinatorEmail(emp_id);
//         HashMap<String, Object> val = getCoordinatorEmail(emp_id);
//         String coordinator_email=(String)val.get("emp_coord_email");
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
   List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             Map<String, Object> coorddetails = getCoordinatorEmail(emp_id);
                  String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");
//             Ldap ldap=new Ldap();
//      Set<String> aliases = ldap.getAliases(coord_email);
//       for(String alias:aliases){
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where status='coordinator_pending' and to_email like '%"+coord_email+"%'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 //check change sql for state ..category is state then change ministry to state
                 //if category is UT change ministry to organization
                 if(category.equalsIgnoreCase("Central")){  
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){
                 
                    String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                  pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                  else if(category.equalsIgnoreCase("UT")){
                 
                    String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                  else{
                      
                    String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                 
                 System.out.println("Filter against ministry");
                 CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                 cpanelbean.setTrackid(rs.getInt(1));
                 cpanelbean.setRegistration_no(rs.getString(2));
                 cpanelbean.setApplicant_email(rs.getString(3));
                 cpanelbean.setApplicant_mobile(rs.getString(4));
                 cpanelbean.setApplicant_name(rs.getString(5));
                 cpanelbean.setCa_email(rs.getString(6));
                 cpanelbean.setCa_mobile(rs.getString(7));
                 cpanelbean.setStatus(rs.getString(8));
                 list.add(cpanelbean);
                 
             }
          // }
             }
             
                
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return list;
   }
          
   
       public List<CoordinatorPanelBean> getForwardedRequestOfCoordinatorbyIdMinwise(int emp_id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         //  String coordinator_email =getCoordinatorEmail(emp_id);
//         HashMap<String, Object> val = getCoordinatorEmail(emp_id);
//         String coordinator_email=(String)val.get("emp_coord_email");
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
   List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
         try {
              Map<String, Object> coorddetails = getCoordinatorEmail(emp_id);
                  String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");
             
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
                 
//              Ldap ldap=new Ldap();
//         Set<String> aliases = ldap.getAliases(coord_email);
//         for(String alias:aliases){
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where status!='completed' and coordinator_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coord_email);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                  if(category.equalsIgnoreCase("Central")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 
                  }
                  else if(category.equalsIgnoreCase("State")){  
                 String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                    else if(category.equalsIgnoreCase("UT")){  
                 String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                  }
                    else{
                 String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                    }
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                 
                 System.out.println("Filter against ministry");
                 CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                 cpanelbean.setTrackid(rs.getInt(1));
                 cpanelbean.setRegistration_no(rs.getString(2));
                 cpanelbean.setApplicant_email(rs.getString(3));
                 cpanelbean.setApplicant_mobile(rs.getString(4));
                 cpanelbean.setApplicant_name(rs.getString(5));
                 cpanelbean.setCa_email(rs.getString(6));
                 cpanelbean.setCa_mobile(rs.getString(7));
                 cpanelbean.setStatus(rs.getString(8));
                 list.add(cpanelbean);
                 
             }
            // }
         }
             
                
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return list;
   }

      
        public List<CoordinatorPanelBean> getRejectRequestOfCoordinatorByIdMinwise(int emp_id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         //  String coordinator_email =getCoordinatorEmail(emp_id);
//         HashMap<String, Object> val = getCoordinatorEmail(emp_id);
//         String coordinator_email=(String)val.get("emp_coord_email");
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
   List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
         try {
               Map<String, Object> coorddetails = getCoordinatorEmail(emp_id);
                  String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");
            
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
                 
//              Ldap ldap=new Ldap();
//         Set<String> aliases = ldap.getAliases(coord_email);
//         for(String alias:aliases){
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where status='rejected' and coordinator_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coord_email);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(category.equalsIgnoreCase("Central")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){  
                   String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, regno);
                 }
                 else if(category.equalsIgnoreCase("UT")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, regno);
                 }
                  else{  
                   String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, regno);
                 }
                 
                 rs1=pst.executeQuery();
                 if(rs1.next()){
                 
                 System.out.println("Filter against ministry");
                 CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                 cpanelbean.setTrackid(rs.getInt(1));
                 cpanelbean.setRegistration_no(rs.getString(2));
                 cpanelbean.setApplicant_email(rs.getString(3));
                 cpanelbean.setApplicant_mobile(rs.getString(4));
                 cpanelbean.setApplicant_name(rs.getString(5));
                 cpanelbean.setCa_email(rs.getString(6));
                 cpanelbean.setCa_mobile(rs.getString(7));
                 cpanelbean.setStatus(rs.getString(8));
                 list.add(cpanelbean);
                 
             //}
             }
         }
             
                
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return list;
   }
      
        public int getPendingRequestCountOfCoordinator(int emp_id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
         //  String coordinator_email =getCoordinatorEmail(emp_id);
         HashMap<String, Object> val = getCoordinatorEmail(emp_id);
         String coordinator_email=(String)val.get("emp_coord_email");
    List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
   List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
       System.out.println("-----"+coordinator_email);
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
                for(CoordinatorPanelBean min:minlist){
                 
             
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where status='coordinator_pending' and to_email like '%"+coordinator_email+"%'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 
                 
                  String regno=rs.getString(2);
                 String tablename = myTable(regno);
                  if(min.getEmp_category().equalsIgnoreCase("Central")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("State")){  
                   String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  else if(min.getEmp_category().equalsIgnoreCase("UT")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                    else{  
                   String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, min.getEmp_category());
                 pst.setString(2, min.getEmp_min_state_org());
                 pst.setString(3, regno);
                  }
                  
                 rs1=pst.executeQuery();
                 
                 if(rs1.next()){
                 
                count++;
                 
             }
             }
             
                }
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return count;
   }
        
         public List<CoordinatorPanelBean> getCompletedRequestOfCoordinatorByIdMinwise(int emp_id,String loggedinHog){
   int count=0;
   PreparedStatement pst=null;
   ResultSet rs=null;
   ResultSet rs1=null;
       List<CoordinatorPanelBean> minlist = getMinistryInfoOfHOG(loggedinHog);
          List<CoordinatorPanelBean> list=new ArrayList<CoordinatorPanelBean>();
         
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             Map<String, Object> coorddetails = getCoordinatorEmail(emp_id);
                  String category = (String)coorddetails.get("emp_category");
         String min_state_org = (String)coorddetails.get("emp_min_state_org");
         String dept = (String)coorddetails.get("emp_dept");
          String coord_email = (String)coorddetails.get("emp_coord_email");   
//                 Ldap ldap=new Ldap();
//         Set<String> aliases = ldap.getAliases(coord_email);
//             for(String alias:aliases){
             String sql="select track_id,registration_no,applicant_email,applicant_mobile,applicant_name,ca_email,ca_mobile,status from final_audit_track where coordinator_email=? and status='Completed'";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coord_email);
             rs=pst.executeQuery();
              while(rs.next()){
                  
                     String regno=rs.getString(2);
                 String tablename = myTable(regno);
                 if(category.equalsIgnoreCase("Central")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                  pst.setString(3, dept);
                 pst.setString(4, regno);
                 }
                 else if(category.equalsIgnoreCase("State")){  
                   String sqlbase="select employment,state from "+tablename+" where employment=? and state=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                  pst.setString(3, dept);
                 pst.setString(4, regno);
                 
                 }
                   else if(category.equalsIgnoreCase("UT")){  
                   String sqlbase="select employment,ministry from "+tablename+" where employment=? and ministry=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                 pst.setString(4, regno);
                 
                 }
                 
                   else{
                       String sqlbase="select employment,organization from "+tablename+" where employment=? and organization=? and department=? and registration_no=?";
                 pst=conSlave.prepareStatement(sqlbase);
                 pst.setString(1, category);
                 pst.setString(2, min_state_org);
                 pst.setString(3, dept);
                  pst.setString(4, regno);
                   }
                 
                 rs1=pst.executeQuery();
                  if(rs1.next()){
                 CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
                 cpanelbean.setTrackid(rs.getInt(1));
                 cpanelbean.setRegistration_no(rs.getString(2));
                 cpanelbean.setApplicant_email(rs.getString(3));
                 cpanelbean.setApplicant_mobile(rs.getString(4));
                 cpanelbean.setApplicant_name(rs.getString(5));
                 cpanelbean.setCa_email(rs.getString(6));
                 cpanelbean.setCa_mobile(rs.getString(7));
                 cpanelbean.setStatus(rs.getString(8));
                 list.add(cpanelbean);
                  }
                 
            // }
             }
                
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
                if(rs1!=null){
                   rs1.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return list;
   }
        
    
    public List<CoordinatorPanelBean> viewAllCoordinatorsByHOGMinistry(String ministry){
   List<CoordinatorPanelBean> list=new ArrayList<>();
   PreparedStatement pst=null;
   ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_id,emp_category,emp_min_state_org,emp_dept,emp_sub_dept,"
                     + "emp_mail_acc_cat,emp_sms_acc_cat,emp_domain,emp_bo_id,emp_coord_mobile,emp_coord_name,"
                     + "emp_coord_email,emp_admin_email,emp_status,emp_createdon,emp_addedby,ip from employment_coordinator where emp_status='a' and emp_min_state_org=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, ministry);
             rs=pst.executeQuery();
             while(rs.next()){
             CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
             cpanelbean.setEmp_id(rs.getInt("emp_id"));
             cpanelbean.setEmp_category(rs.getString("emp_category"));
             cpanelbean.setEmp_min_state_org(rs.getString("emp_min_state_org"));
             cpanelbean.setEmp_dept(rs.getString("emp_dept"));
             cpanelbean.setEmp_sub_dept(rs.getString("emp_sub_dept"));
             cpanelbean.setEmp_mail_acc_cat(rs.getString("emp_mail_acc_cat"));
             cpanelbean.setEmp_sms_acc_cat(rs.getString("emp_sms_acc_cat"));
             cpanelbean.setEmp_domain(rs.getString("emp_domain"));
             cpanelbean.setEmp_bo_id(rs.getString("emp_bo_id"));
             cpanelbean.setEmp_coord_mobile(rs.getString("emp_coord_mobile"));
             cpanelbean.setEmp_coord_name(rs.getString("emp_coord_name"));
             cpanelbean.setEmp_coord_email(rs.getString("emp_coord_email"));
             cpanelbean.setEmp_admin_email(rs.getString("emp_admin_email"));
             
             cpanelbean.setEmp_status(rs.getString("emp_status"));
             cpanelbean.setEmp_createdon(rs.getString("emp_createdon"));
             cpanelbean.setEmp_addedby(rs.getString("emp_addedby"));
             cpanelbean.setEmp_ip(rs.getString("ip"));
             list.add(cpanelbean);
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
   
        finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
   
   
   
   return list;
   }
    //EO viewAll Coordinators
   
   //view by coordinator id
        //main method
   public Map<String,Object> viewCoordinatorsById(int emp_id,String loggedinHog){
   
   Map<String,Object> map=new HashMap();
   PreparedStatement pst=null;
   ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_id,emp_category,emp_min_state_org,emp_dept,emp_sub_dept,"
                     + "emp_mail_acc_cat,emp_sms_acc_cat,emp_domain,emp_bo_id,emp_coord_mobile,emp_coord_name,"
                     + "emp_coord_email,emp_admin_email,emp_status,emp_createdon,emp_addedby,ip from employment_coordinator where emp_id=? and emp_status='a'";
             pst=conSlave.prepareStatement(sql);
             pst.setInt(1, emp_id);
             rs=pst.executeQuery();
             while(rs.next()){
             map.put("emp_id", rs.getInt("emp_id"));
             map.put("emp_category", rs.getString("emp_category"));
             map.put("emp_min_state_org", rs.getString("emp_min_state_org"));
             map.put("emp_dept", rs.getString("emp_dept"));
             map.put("emp_sub_dept", rs.getString("emp_sub_dept"));
             map.put("emp_mail_acc_cat", rs.getString("emp_mail_acc_cat"));
             map.put("emp_sms_acc_cat", rs.getString("emp_sms_acc_cat"));
             map.put("emp_domain", rs.getString("emp_domain"));
             map.put("emp_bo_id", rs.getString("emp_bo_id"));
             map.put("emp_coord_mobile", rs.getString("emp_coord_mobile"));
             map.put("emp_coord_name", rs.getString("emp_coord_name"));
             map.put("emp_coord_email", rs.getString("emp_coord_email"));
             map.put("emp_admin_email", rs.getString("emp_admin_email"));
             map.put("emp_status", rs.getString("emp_status"));
             map.put("emp_createdon", rs.getString("emp_createdon"));
             map.put("emp_addedby", rs.getString("emp_addedby"));
             map.put("emp_ip", rs.getString("ip"));
           
//                 HashMap<String, Object> val = getCoordinatorEmail(emp_id);
//                 String coord_email=(String)val.get("emp_coord_email");
              //map.put("pending_request_count",getPendingRequestCountOfCoordinator(coord_email,loggedinHog));
              map.put("pending_request_count",getPendingRequestCountOfCoordinatorByIdMinwise(emp_id, loggedinHog));
             // map.put("completed_request_count",getCompletedRequestCountOfCoordinator(coord_email,loggedinHog));
              map.put("completed_request_count",getCompletedRequestCountOfCoordinatorByIdMinwise(emp_id,loggedinHog));
               //map.put("forwarded_request_count",getForwardedRequestCountOfCoordinator(coord_email,loggedinHog));
                map.put("forwarded_request_count",getForwardedRequestCountOfCoordinatorByIdMinwise(emp_id,loggedinHog));
               // map.put("rejected_request_count",getRejectedRequestCountOfCoordinator(coord_email,loggedinHog));
             map.put("rejected_request_count",getRejectedRequestCountOfCoordinatorByIdMinwise(emp_id,loggedinHog));
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
   
              finally {
            try {
                if (pst != null) {
                    pst.close();
                }
              
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
   
   return map;
   
   
   
   
   }
   
   
       //main method
    public int updateCoordinator(CoordinatorPanelBean cpanelbean){
    int i=0,j=0;
      PreparedStatement pst=null; 
      ResultSet rs=null;
      ResultSet rsfaudit=null;
      ResultSet rs1=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
          
             int emp_id=cpanelbean.getEmp_id();
             //before updating coordinator get  details of existing coordinator
             Map<String, Object> coord_details = getCoordinatorEmail(emp_id);
             
             
             String sql="update employment_coordinator set emp_category=?,emp_min_state_org=?,emp_dept=?,emp_sub_dept=?,"
                     + "emp_mail_acc_cat=?,emp_sms_acc_cat=?,emp_domain=?,emp_bo_id=?,emp_coord_mobile=?,emp_coord_name=?,emp_coord_email=?,emp_admin_email=?,"
                     + "emp_status=?,emp_addedby=?,ip=? where emp_id=?";
               pst=con.prepareStatement(sql);
               pst.setString(1, cpanelbean.getEmp_category());
            
               switch (cpanelbean.getEmp_category()) {
                 case "Central":
                     pst.setString(2, cpanelbean.getEmp_min_state_org());
                     pst.setString(3, cpanelbean.getEmp_dept());
                     break;
                 case "State":
                     pst.setString(2, cpanelbean.getStateCode());
                     pst.setString(3, cpanelbean.getState_dept());
                     break;
                 case "Psu":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "Const":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                     
                 case "Nkn":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "Project":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     break;
                 case "UT":
                     pst.setString(2, cpanelbean.getEmp_min_state_org());
                     pst.setString(3, cpanelbean.getEmp_dept());
                     break;
                 case "Others":
                     pst.setString(2, cpanelbean.getOrg());
                     pst.setString(3, "");
                     //addDepartmentRequest(cpanelbean);
                     
                     break;
                 default:
                     break;
             }
            
             
             pst.setString(4, cpanelbean.getEmp_sub_dept());
             pst.setString(5, cpanelbean.getEmp_mail_acc_cat());
             pst.setString(6, cpanelbean.getEmp_sms_acc_cat());
             pst.setString(7, cpanelbean.getEmp_domain());
             pst.setString(8, cpanelbean.getEmp_bo_id());
             pst.setString(9, cpanelbean.getEmp_coord_mobile());
             pst.setString(10, cpanelbean.getEmp_coord_name());
             pst.setString(11, cpanelbean.getEmp_coord_email());
             pst.setString(12, cpanelbean.getEmp_admin_email());
             pst.setString(13, "a");
             pst.setString(14, cpanelbean.getEmp_addedby());
             pst.setString(15, cpanelbean.getEmp_ip());
             pst.setInt(16, cpanelbean.getEmp_id());
             i = pst.executeUpdate();
              if(i>0){
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "co "+ cpanelbean.getEmp_coord_email()+" updated by "+cpanelbean.getLogin_user_email());
                  pst.setString(2, cpanelbean.getLogin_user_email());
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
             
            String query = "";
                  String coord_email_tobe_replaced = (String) coord_details.get("emp_coord_email");
                  String emp_category = (String) coord_details.get("emp_category");
                  String emp_min_state_org = (String) coord_details.get("emp_min_state_org");
                  String emp_dept = (String) coord_details.get("emp_dept");
                  //Alias fetch for the coordinators to be deleted
                  Ldap ldap = new Ldap();
                  Set<String> coord_email_tobe_replacedAliases = ldap.fetchAliases(coord_email_tobe_replaced);
                  String coord_email_aliases_tobe_replaced = "";
                  for (String replaceCoord : coord_email_tobe_replacedAliases) {
                      coord_email_aliases_tobe_replaced += "'" + replaceCoord + "',";
                  }
                  coord_email_aliases_tobe_replaced = coord_email_aliases_tobe_replaced.replaceAll(",$", "");
                  //End Alias

                     String sqlfinalaudittrack="select track_id,registration_no,to_email from final_audit_track where status='coordinator_pending'";
                  pst=conSlave.prepareStatement(sqlfinalaudittrack);
                  rsfaudit=pst.executeQuery();
                  while(rsfaudit.next()){
                      int track_id = rsfaudit.getInt(1);
                      String regno = rsfaudit.getString(2);
                      String to_email = rsfaudit.getString(3);
                   
                       String tablename = myTable(regno);
                         if (emp_category != null && emp_category.equalsIgnoreCase("Central")) {
                        if (emp_dept==null||emp_dept.isEmpty()) {
                            query = "select id from " + tablename + " where employment = 'Central' and ministry = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_min_state_org);
                            pst.setString(2, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = 'Central' and ministry = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_min_state_org);
                            pst.setString(2, emp_dept);
                            pst.setString(3, regno);
                        }
                    } else if (emp_category != null && emp_category.equalsIgnoreCase("UT")) {
                        if (emp_dept.isEmpty()) {
                            query = "select id from " + tablename + " where employment = ? and ministry = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = ? and ministry = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, emp_dept);
                            pst.setString(4, regno);
                        }
                    }
                    
                    else if (emp_category != null && emp_category.equalsIgnoreCase("State")) {
                        if (emp_dept.isEmpty()) {
                            query = "select id from " + tablename + " where employment = ? and state = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = ? and state = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, emp_dept);
                            pst.setString(4, regno);
                        }
                    }
                    
                    
                    else {
                        query = "select id from " + tablename + " where employment = ? and organization = ? and registration_no=?";
                        pst = conSlave.prepareStatement(query);
                        pst.setString(1, emp_category);
                        pst.setString(2, emp_min_state_org);
                        pst.setString(3, regno);
                    }
                        rs1=pst.executeQuery();
                        if(rs1.next()){
                            System.out.println("registration no to email data be changed in final audit track");
                            //delete contains of coord_email_aliases_tobe_deleted in to_email and update finalaudittrack
                           //pass to replace to_email.... coord_email_aliases_tobe_deleted,to_email if not match no replacement
                            to_email = updateMatchers(coord_email_aliases_tobe_replaced, to_email,cpanelbean.getEmp_coord_email());
                            updateFinalAuditTrack(track_id, to_email, regno,"update");
                        }
                   
                  }


              }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
        try {
            String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
            pst=con.prepareStatement(sql_logs);
            pst.setString(1, ex.getMessage()+"Co "+ cpanelbean.getEmp_coord_email()+"updated by "+cpanelbean.getLogin_user_email());
            pst.setString(2, cpanelbean.getLogin_user_email());
            pst.executeUpdate();
            System.out.println("Activity Logs Inserted");
            Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex1) {
            Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
        }
         }
         
         finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
        return i;
    }
    
  
     public int deleteCoordinatorById(int emp_id,String loginuser){
       int i=0,j;
       PreparedStatement pst=null;   
        ResultSet rs1=null;
        ResultSet rsfaudit=null;
         try {     
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             
             String sql="update employment_coordinator set emp_status='i' where emp_id=?";
             pst=con.prepareStatement(sql);
             pst.setInt(1, emp_id);
             i=pst.executeUpdate();
              if(i>0){
                  //logs
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Co id "+emp_id+" deleted by "+loginuser);
                  pst.setString(2, loginuser);
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
                //EoLogs
              
                  Map<String, Object> coord_details = getCoordinatorEmail(emp_id);
                  String query = "";
                  String coord_email_tobe_deleted = (String) coord_details.get("emp_coord_email");
                  String emp_category = (String) coord_details.get("emp_category");
                  String emp_min_state_org = (String) coord_details.get("emp_min_state_org");
                  String emp_dept = (String) coord_details.get("emp_dept");
                  //Alias fetch for the coordinators to be deleted
                  Ldap ldap = new Ldap();
                  Set<String> coord_email_tobe_deletedAliases = ldap.fetchAliases(coord_email_tobe_deleted);
                  String coord_email_aliases_tobe_deleted = "";
                  for (String deletionCoord : coord_email_tobe_deletedAliases) {
                      coord_email_aliases_tobe_deleted += "'" + deletionCoord + "',";
                  }
                  coord_email_aliases_tobe_deleted = coord_email_aliases_tobe_deleted.replaceAll(",$", "");
                  //End Alias


                   
                // List<String> coo_pending_regnos = getAllRegistrationNoCoPending();
                  String sqlfinalaudittrack="select track_id, registration_no,to_email from final_audit_track where status='coordinator_pending'";
                  pst=conSlave.prepareStatement(sqlfinalaudittrack);
                  rsfaudit=pst.executeQuery();
                  while(rsfaudit.next()){
                      int track_id = rsfaudit.getInt(1);
                      String regno = rsfaudit.getString(2);
                      String to_email = rsfaudit.getString(3);
                   
                       String tablename = myTable(regno);
                         if (emp_category != null && emp_category.equalsIgnoreCase("Central")) {
                        if (emp_dept==null||emp_dept.isEmpty()) {
                            query = "select id from " + tablename + " where employment = 'Central' and ministry = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_min_state_org);
                            pst.setString(2, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = 'Central' and ministry = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_min_state_org);
                            pst.setString(2, emp_dept);
                            pst.setString(3, regno);
                        }
                    } else if (emp_category != null && emp_category.equalsIgnoreCase("UT") || emp_category != null && emp_category.equalsIgnoreCase("State")) {
                        if (emp_dept.isEmpty()) {
                            query = "select id from " + tablename + " where employment = ? and ministry = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = ? and ministry = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, emp_dept);
                            pst.setString(4, regno);
                        }
                    }
                     else if (emp_category != null && emp_category.equalsIgnoreCase("State")) {
                        if (emp_dept.isEmpty()) {
                            query = "select id from " + tablename + " where employment = ? and state = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, regno);
                        } else {
                            query = "select id from " + tablename + " where employment = ? and state = ? and department = ? and registration_no=?";
                            pst = conSlave.prepareStatement(query);
                            pst.setString(1, emp_category);
                            pst.setString(2, emp_min_state_org);
                            pst.setString(3, emp_dept);
                            pst.setString(4, regno);
                        }
                    }
                    
                    else {
                        query = "select id from " + tablename + " where employment = ? and organization = ? and registration_no=?";
                        pst = conSlave.prepareStatement(query);
                        pst.setString(1, emp_category);
                        pst.setString(2, emp_min_state_org);
                        pst.setString(3, regno);
                    }
                        rs1=pst.executeQuery();
                        if(rs1.next()){
                            System.out.println("registration no to email data be changed in final audit track");
                            //delete contains of coord_email_aliases_tobe_deleted in to_email and update finalaudittrack
                           //pass to replace to_email.... coord_email_aliases_tobe_deleted,to_email if not match no replacement
                            to_email = removeMatchers(coord_email_aliases_tobe_deleted, to_email);
                            updateFinalAuditTrack(track_id, to_email, regno,"delete");
                        }
                   
                  }
           
              }
         } catch (ClassNotFoundException | SQLException ex) {
           try {
               String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
               pst=con.prepareStatement(sql_logs);
               pst.setString(1, ex.getMessage()+" Co id "+emp_id+" deleted by "+loginuser);
               pst.setString(2, loginuser);
               pst.executeUpdate();
               System.out.println("Activity Logs Inserted");
               Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SQLException ex1) {
               Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex1);
           }
         }
              finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs1!=null){
                   rs1.close();
               }
               if(rsfaudit!=null){
                   rsfaudit.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
         return i;
     }
     
    public List<String> getAllRegistrationNoCoPending(){
         PreparedStatement pst=null;
         ResultSet rs=null;
         List<String> list=new ArrayList<>();

         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select registration_no from final_audit_track where status='coordinator_pending'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
              list.add(rs.getString(1));
             }
                     
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
            finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
                }
            

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
            return list;

    }
    public int updateFinalAuditTrack(int track_id,String to_email,String registration_no,String action){
    int i=0,k=0;
    PreparedStatement pst=null;
    PreparedStatement pst1=null;
    ResultSet rs2=null;
         try {
             con=DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             if(!to_email.isEmpty()){
             String sql="update final_audit_track set to_email=? where track_id=? and registration_no=?";
             pst=con.prepareStatement(sql);
             pst.setString(1, to_email);
             pst.setInt(2, track_id);
              pst.setString(3, registration_no);
             i=pst.executeUpdate();
             }
             else{
              if (registration_no.startsWith("VPN")) {
                        to_email = "vpnsupport@nic.in";
                    } else if (registration_no.startsWith("WIFI")) {
                        to_email = "support@nkn.in";
                    } else {
                        to_email = "support@gov.in";
                    }
             String sql="update final_audit_track set to_email=?,status='support_pending',to_mobile='',to_name='' where track_id=? and registration_no=?";
             pst=con.prepareStatement(sql);
             pst.setString(1, to_email);
             pst.setInt(2, track_id);
             pst.setString(3, registration_no);

             i=pst.executeUpdate();
             
      
             if(!action.equals("update")){
                 pst1 = conSlave.prepareStatement("select stat_id from " + Constants.STATUS_TABLE + " where stat_reg_no ='" + registration_no + "' and stat_id in (select max(stat_id) from status group by stat_reg_no)");
                 rs2 = pst1.executeQuery();
                 if (rs2.next()) {
                     int stat_id = rs2.getInt("stat_id");
                     pst1 = con.prepareStatement("update " + Constants.STATUS_TABLE + " set stat_forwarded_to_user = ?,stat_type=?,stat_forwarded_to=? where stat_id = ?");
                     pst1.setString(1, to_email);
                     pst1.setString(2, "support_pending");
                     pst1.setString(3, "s");
                     pst1.setInt(4, stat_id);
                     k = pst1.executeUpdate();
                     if (k > 0) {
                         System.out.println("status_table Updated successfully for " + registration_no);
                     }
                 }
             }
             }
             
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
                if (pst1 != null) {
                   pst1.close();
               }
               if(rs2!=null){
                   rs2.close();
               }
               
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
    
    return i;
    }
     
     
     //to be checked
 
    public int countCoordinatorsgroup(int co_id){
     PreparedStatement pst=null;
     ResultSet rs=null;
     int count=0;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select * from coordinator_ids where id=?";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
             count++;
             
             }
          
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
                }
            

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
        return count;
     }
    
    public int changeCoidinFinalauditTrack(int co_id){
          PreparedStatement pst=null;
          ResultSet rs=null;
          int i=0;
         try {
             String sqlupdate="update final_audit_track set status='support_pending' , co_id=0  where co_id=? and status='coordinator_pending'";
             pst=con.prepareStatement(sqlupdate);
             pst.setInt(1, co_id);
             i= pst.executeUpdate();
             
             
         } catch (SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
           try {
               if (pst != null) {
                   pst.close();
               }
              
             

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
        
        
    return i;
    }
    
      
      
     //After delete performed on coordinator
    //SELECT coordinator_email FROM `final_audit_track` where coordinator_email is NOT NULL
     public HashMap<String,Object> getCoordinatorEmail(int emp_id){
      PreparedStatement pst=null;   
      ResultSet rs=null;
      // String coord_email="";
       HashMap<String,Object> map=new HashMap<>();
          try {     
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select emp_category,emp_min_state_org,emp_dept,emp_coord_email from employment_coordinator where emp_id="+emp_id;
             
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
              
               map.put("emp_category", rs.getString(1));
               map.put("emp_min_state_org", rs.getString(2));
               map.put("emp_dept", rs.getString(3));
               map.put("emp_coord_email", rs.getString(4));
             }
             
          }
          catch(Exception e){
          e.printStackTrace();
          }
            finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null){
                   rs.close();
               }
               

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         return map;
     }
     /*
        String emp_coord_email=rs.getString(1);
                 if(emp_coord_email.contains(",")){
                     String[] arr = emp_coord_email.split(",");
                     for(int i=0;i<arr.length;i++){
                         
                     }
                 }
     
     */
     
     
     
    public Map<String,Object> checkCoordinatorExistance(String coordinator_email){
      PreparedStatement pst=null;   
      ResultSet rs=null;
      Map<String,Object> map=new HashMap();
      int count=0;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select count(*) from employment_coordinator where emp_coord_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, coordinator_email);
             rs=pst.executeQuery();
             if(rs.next()){
             count=rs.getInt(1);
             }
             if(count>0){
             map.put("status", "exists");
             }
             else{
             map.put("status", "not exists");
             }
         } catch (ClassNotFoundException | SQLException ex) {
             map.put("status",ex.getMessage());
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
         return map;
     }
     //RO Management
      public int insertRoTable(CoordinatorPanelBean cpanelbean){
         int i = 0;
         
          Ldap ldap = new Ldap();
          ProfileService profileService = new ProfileService();
          if (ldap.emailValidate(cpanelbean.getCa_mobile())) {
              if (cpanelbean.getCa_mobile().contains("X")) {
                  HashMap hodDetails = (HashMap) profileService.getHODdetails(cpanelbean.getCa_mobile());
                  cpanelbean.setCa_mobile(hodDetails.get("mobile").toString().trim());
              }
          }
         
         
         PreparedStatement pst = null;
         try {
             
             con = DbConnection.getConnection();
             String sql="insert into comp_auth(ca_name,ca_email,ca_mobile,ca_status) values(?,?,?,?)";
             pst=con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getCa_name());
             pst.setString(2, cpanelbean.getCa_email());
             pst.setString(3, cpanelbean.getCa_mobile());
             pst.setString(4, "a");
             i = pst.executeUpdate();
             if(i>0){
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Ro "+ cpanelbean.getCa_email()+" Inserted by "+cpanelbean.getLogin_user_email());
                  pst.setString(2, cpanelbean.getLogin_user_email());
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
             }
            
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           finally {
            try {
                if (pst != null) {
                    pst.close();
                }
              
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
   return i;
   }
     
     
      public List<CoordinatorPanelBean> viewAllRo(){
   List<CoordinatorPanelBean> list=new ArrayList<>();
   PreparedStatement pst=null;
   ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select ca_id,ca_name,ca_email,ca_mobile,ca_createdon,ca_status from comp_auth where ca_status='a'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
             CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
             cpanelbean.setCa_id(rs.getInt("ca_id"));
             cpanelbean.setCa_name(rs.getString("ca_name"));
             cpanelbean.setCa_email(rs.getString("ca_email"));
             cpanelbean.setCa_mobile(rs.getString("ca_mobile"));
             cpanelbean.setCa_createdon(rs.getString("ca_createdon"));
             cpanelbean.setCa_status(rs.getString("ca_status"));
            
             list.add(cpanelbean);
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
   
        finally {
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
      
      
     
     
      public Map<String,Object> viewRoById(int emp_id){
   
   Map<String,Object> map=new HashMap();
   PreparedStatement pst=null;
   ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select ca_id,ca_name,ca_email,ca_mobile,ca_createdon,ca_status from comp_auth where ca_id=?";
             pst=conSlave.prepareStatement(sql);
             pst.setInt(1, emp_id);
             rs=pst.executeQuery();
             while(rs.next()){
             map.put("ca_id", rs.getInt("ca_id"));
             map.put("ca_name", rs.getString("ca_name"));
             map.put("ca_email", rs.getString("ca_email"));
             map.put("ca_mobile", rs.getString("ca_mobile"));
             map.put("ca_createdon", rs.getString("ca_createdon"));
             map.put("ca_status", rs.getString("ca_status"));
             
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
   
              finally {
            try {
                if (pst != null) {
                    pst.close();
                }
              
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
   
   return map;
   }
     
      
       public int updateRo(CoordinatorPanelBean cpanelbean){
    int i=0;
      PreparedStatement pst=null;   
         try {
             con = DbConnection.getConnection();
             String sql="update comp_auth set ca_name=?,ca_email=?,ca_mobile=?,ca_status=? where ca_id=?";
             pst = con.prepareStatement(sql);
             pst.setString(1, cpanelbean.getCa_name());
             pst.setString(2, cpanelbean.getCa_email());
             pst.setString(3, cpanelbean.getCa_mobile());
             pst.setString(4, "a");
             pst.setInt(5, cpanelbean.getEmp_id());
             i=pst.executeUpdate();
              if(i>0){
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Ro "+ cpanelbean.getCa_email()+" Table updated by "+cpanelbean.getLogin_user_email());
                  pst.setString(2, cpanelbean.getLogin_user_email());
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
             }
            
             
             
             
         } catch (ClassNotFoundException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
         
         finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
        return i;
    }
     
       
        public int deleteRoById(int emp_id,String loginuser){
       int i=0;
       PreparedStatement pst=null;   
         try {     
             con = DbConnection.getConnection();
             String sql="update comp_auth set ca_status='i' where ca_id=?";
             pst=con.prepareStatement(sql);
             pst.setInt(1, emp_id);
             i=pst.executeUpdate();
              if(i>0){
                  String sql_logs="insert into activity_logs(remarks,inserted_by) values(?,?)";
                  pst=con.prepareStatement(sql_logs);
                  pst.setString(1, "Ro id "+emp_id+" deleted by "+loginuser);
                  pst.setString(2, loginuser);
                  pst.executeUpdate();
                  System.out.println("Activity Logs Inserted");
             }
            
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
              finally {
           try {
               if (pst != null) {
                   pst.close();
               }

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
         return i;
     }
     //EO RO Management
     
     
    public ArrayList<CoordinatorPanelBean>  getStatsPendingRequestRO(){
         ArrayList<CoordinatorPanelBean> pendinglistRO=new ArrayList<CoordinatorPanelBean>();
         PreparedStatement pst = null;
         ResultSet rs=null;
         try {
            
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="SELECT to_email,to_name,to_mobile,count(*) as request_pending ,status FROM `final_audit_track` WHERE status='ca_pending' group by to_email";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 CoordinatorPanelBean coordbean =new CoordinatorPanelBean();
                     coordbean.setTo_email(rs.getString("to_email"));
                     coordbean.setTo_name(rs.getString("to_name"));
                     coordbean.setTo_mobile(rs.getString("to_mobile"));
                    // coordbean.setForm_name(rs.getString("form_name"));
                     coordbean.setRequest_pending(rs.getString("request_pending"));
                     coordbean.setStatus(rs.getString("status"));
                     coordbean.setResponse_status("Pending Requests for RO");
                     pendinglistRO.add(coordbean);
             }
         
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
         
       finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
      return pendinglistRO;
           
    }
   
    
    
    
     public ArrayList<CoordinatorPanelBean>  getStatsPendingRequestCO(){
         ArrayList<CoordinatorPanelBean> pendinglistCO=new ArrayList<CoordinatorPanelBean>();
         PreparedStatement pst = null;
         ResultSet rs=null;
         try {
            
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="SELECT to_email,to_name,to_mobile,count(*) as request_pending ,status FROM `final_audit_track` WHERE status='co_pending' group by to_email";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                 CoordinatorPanelBean coordbean =new CoordinatorPanelBean();
                     coordbean.setTo_email(rs.getString("to_email"));
                     coordbean.setTo_name(rs.getString("to_name"));
                     coordbean.setTo_mobile(rs.getString("to_mobile"));
                    // coordbean.setForm_name(rs.getString("form_name"));
                     coordbean.setRequest_pending(rs.getString("request_pending"));
                     coordbean.setStatus(rs.getString("status"));
                     coordbean.setResponse_status("Pending Requests for CO");
                     pendinglistCO.add(coordbean);
             }
             
         } catch (ClassNotFoundException ex) {
             ex.printStackTrace();
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SQLException ex) {
              ex.printStackTrace();
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
         
       finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
     return pendinglistCO;
           
    }
     
     
      public String myTable(String registration_number) {
        String table = "";
        if (registration_number.contains("IMAPPOP-FORM")) {
            table = "imappop_registration";
        } else if (registration_number.contains("DLIST-FORM")) {
            table = "distribution_registration";
        } else if (registration_number.contains("LDAP-FORM")) {
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
        } else if (registration_number.contains("EMAILACTIVATE-FORM")) {
            table = "email_act_registration";
        } else if (registration_number.contains("EMAILDEACTIVATE-FORM")) {
            table = "email_deact_registration";
        }
        return table;
    }
      
      
      public List<String>  myTableList() {
          List<String> list=new ArrayList<>();
          list.add("imappop_registration");
          list.add("distribution_registration");
          list.add("ldap_registration");
          list.add("relay_registration");
          list.add("ip_registration");
          list.add("single_registration");
          list.add("bulk_registration");
          list.add("nkn_registration");
          list.add("sms_registration");
          list.add("mobile_registration");
          list.add("gem_registration");
          list.add("dns_registration");
          list.add("wifi_registration");
          list.add("webcast_registration");
          list.add("vpn_registration");
          list.add("email_act_registration");
          list.add("email_deact_registration");
       
        return list;
    }
      
      
      
       public String formtype(String registration_number) {
        String formtype = "";
        if (registration_number.contains("IMAPPOP-FORM")) {
            formtype = "imappop";
        } else if (registration_number.contains("DLIST-FORM")) {
            formtype = "dlist";
        } else if (registration_number.contains("LDAP-FORM")) {
            formtype = "ldap";
        } else if (registration_number.contains("RELAY-FORM")) {
            formtype = "relay";
        } else if (registration_number.contains("IP-FORM")) {
            formtype = "ip";
        } else if (registration_number.contains("SINGLEUSER-FORM")) {
            formtype = "single";
        } else if (registration_number.contains("BULKUSER-FORM")) {
            formtype = "bulkuser";
        } else if (registration_number.contains("NKN-FORM")) {
            formtype = "nkn";
        } else if (registration_number.contains("NKN-BULK-FORM")) {
            formtype = "nkn-bulk";
        } else if (registration_number.contains("SMS-FORM")) {
            formtype = "sms";
        } else if (registration_number.contains("MOBILE")) {
            formtype = "mobile";
        } else if (registration_number.contains("GEM-FORM")) {
            formtype = "gem";
        } else if (registration_number.contains("DNS-FORM")) {
            formtype = "dns";
        } else if (registration_number.contains("WIFI-FORM")) {
            formtype = "wifi";
        } else if (registration_number.contains("WEBCAST-FORM")) {
            formtype = "webcast";
        } else if (registration_number.contains("VPN")) {
            formtype = "vpn";
        } else if (registration_number.contains("EMAILACTIVATE-FORM")) {
            formtype = "emailactivate";
        } else if (registration_number.contains("EMAILDEACTIVATE-FORM")) {
            formtype = "emaildeactivate";
        }
        return formtype;
    }
   
   public String removeMatchers(String deletetionEmails,String email){
      String to_email=email.trim();
      
         System.out.println("---"+to_email);
  //String to_email="shweta.nhq@nic.in";
         String emailtobedeleted = deletetionEmails.trim().replace("'", "");
         if (to_email.contains(",")) {
             //first_line
             if (emailtobedeleted.contains(",")) {
                 String coord[] = emailtobedeleted.split(",");
                 for (int i = 0; i < coord.length; i++) {
                     to_email = to_email.replace(coord[i], "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 }
             } else {
                 to_email = to_email.replace(emailtobedeleted, "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 System.out.println("to_email=" + to_email);
             }

         } else {
                if (emailtobedeleted.contains(",")) {
                 String coord[] = emailtobedeleted.split(",");
                 for (int i = 0; i < coord.length; i++) {
                     to_email = to_email.replace(coord[i], "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 }
             } else {
                 to_email = to_email.replace(emailtobedeleted, "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 System.out.println("to_email=" + to_email);
             } //move to support panel and co_id reset also
         }
          System.out.println("to_email=" + to_email);
          return to_email;
   }
public String updateMatchers(String emailtobereplaceded,String to_emailed,String replacementEmail){
    String emailtobereplaced=emailtobereplaceded.trim().replace("'", "");
    String to_email=to_emailed.trim();
         if (to_email.contains(",")) {
             if (emailtobereplaced.contains(",")) {
                 String coord[] = emailtobereplaced.split(",");
                 for (int i = 0; i < coord.length; i++) {
                     to_email = to_email.replace(coord[i], replacementEmail).replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 }
             } else {
                 to_email = to_email.replace(emailtobereplaced, replacementEmail).replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 System.out.println("to_email=" + to_email);
             }

         } else {
                if (emailtobereplaced.contains(",")) {
                 String coord[] = emailtobereplaced.split(",");
                 for (int i = 0; i < coord.length; i++) {
                     to_email = to_email.replace(coord[i], replacementEmail).replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 }
             } else {
                 to_email = to_email.replace(emailtobereplaced, replacementEmail).replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 System.out.println("to_email=" + to_email);
             }
         }
          System.out.println("to_email=" + to_email);
          return to_email;
   }


public String addMatchers(String emailtobeadded,String to_emailed){
    String emailtobeadd = emailtobeadded.trim().replace("'", "");
    String to_email = to_emailed.trim();
    to_email = to_email + "," + emailtobeadd;
    System.out.println("to_email=" + to_email);
    System.out.println("to_email=" + to_email);
    return to_email;
   }
   
   
   
     public static void main(String args[]){
//aliases
 String to_email="humaida@nic.in,meenaxi.nhq@nic.in";
         System.out.println("---"+to_email);
  //String to_email="shweta.nhq@nic.in";
         String emailtobedeleted = "meenaxi.nhq@nic.in,meenaxi.nhq@dummynic.in";
         if (to_email.contains(",")) {
             //first_line
             if (emailtobedeleted.contains(",")) {
                 String coord[] = emailtobedeleted.split(",");
                 for (int i = 0; i < coord.length; i++) {
                     to_email = to_email.replace(coord[i], "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 }
             } else {
                 to_email = to_email.replace(emailtobedeleted, "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 System.out.println("to_email=" + to_email);
             }

         } else {
                if (emailtobedeleted.contains(",")) {
                 String coord[] = emailtobedeleted.split(",");
                 for (int i = 0; i < coord.length; i++) {
                     to_email = to_email.replace(coord[i], "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 }
             } else {
                 to_email = to_email.replace(emailtobedeleted, "").replace(",,", ",").replaceAll(",$", "").replaceAll("^,", "");
                 System.out.println("to_email=" + to_email);
             } //move to support panel and co_id reset also
         }
          System.out.println("to_email=" + to_email);
     }
   
     public List<String> getCategoryHOG(String hog_email){
     
         ArrayList<String> list = new ArrayList<String>();
         PreparedStatement pst = null;
         ResultSet rs=null;
       
            
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select distinct category from hog_ministry where hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, hog_email);
             rs=pst.executeQuery();
             while(rs.next()){
             list.add(rs.getString(1));
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
                finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
          return list;
     }
     
     
       public List<String> getCategoryMinistryHOG(String hog_email,String category){
     
         ArrayList<String> list = new ArrayList<String>();
         PreparedStatement pst = null;
         ResultSet rs=null;
       
            
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select distinct ministry from hog_ministry where hog_email=? and category=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, hog_email);
             pst.setString(2, category);
             rs=pst.executeQuery();
             while(rs.next()){
             list.add(rs.getString(1));
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
                finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
          return list;
     }
       
          public List<String> getCategoryMinistryDepartmentHOG(String category,String ministry){
     
         ArrayList<String> list = new ArrayList<String>();
         PreparedStatement pst = null;
         ResultSet rs=null;
       
            
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select distinct emp_dept from employment_coordinator where emp_category=? and emp_min_state_org=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, category);
             pst.setString(2, ministry);
             rs=pst.executeQuery();
             while(rs.next()){
             list.add(rs.getString(1));
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
                finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
          return list;
     }
          
          
          //get All categories
 public List<String> getAllCategory(){
     
         ArrayList<String> list = new ArrayList<String>();
         PreparedStatement pst = null;
         ResultSet rs=null;
       
            
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select distinct emp_category from employment_coordinator";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
             list.add(rs.getString(1));
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
                finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
          return list;
     }
 
 public List<String> getAllCategoryMinistry(String category){
     
         ArrayList<String> list = new ArrayList<String>();
         PreparedStatement pst = null;
         ResultSet rs=null;
       
            
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select distinct emp_min_state_org from employment_coordinator where emp_category=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, category);
             rs=pst.executeQuery();
             while(rs.next()){
             list.add(rs.getString(1));
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
                finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
          return list;
     }
 
  public List<String> getAllCategoryMinistryDepartment(String category,String ministry){
     
         ArrayList<String> list = new ArrayList<String>();
         PreparedStatement pst = null;
         ResultSet rs=null;
       
            
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select distinct emp_dept from employment_coordinator where emp_category=? and emp_min_state_org=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, category);
             pst.setString(2, ministry);
             rs=pst.executeQuery();
             while(rs.next()){
             list.add(rs.getString(1));
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
                finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
         
          return list;
     }

//End of get All categories          
          //***********HOG STATS******************
          public int getHogPendingCount(String hog_email){
          int count=0;
           PreparedStatement pst = null;
         ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select * from final_audit_track where status='hog_pending' and to_email like '%"+hog_email+"%'";
             pst=conSlave.prepareStatement(sql);
             rs=pst.executeQuery();
             while(rs.next()){
                count++;
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
          
          return count;
          }
          
           public int getHogForwardCount(String hog_email){
          int count=0;
           PreparedStatement pst = null;
         ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select * from final_audit_track where status!='completed' and hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, hog_email);
             rs=pst.executeQuery();
             while(rs.next()){
                count++;
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
          
          return count;
          }
       
              public int getHogRejectCount(String hog_email){
          int count=0;
           PreparedStatement pst = null;
         ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select * from final_audit_track where status='hog_rejected' and hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, hog_email);
             rs=pst.executeQuery();
             while(rs.next()){
                count++;
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
          
          return count;
          }
              
               public int getHogCompletedCount(String hog_email){
          int count=0;
           PreparedStatement pst = null;
         ResultSet rs=null;
         try {
             con = DbConnection.getConnection();
             conSlave = DbConnection.getSlaveConnection(); //29dec2021
             String sql="select * from final_audit_track where status='completed' and hog_email=?";
             pst=conSlave.prepareStatement(sql);
             pst.setString(1, hog_email);
             rs=pst.executeQuery();
             while(rs.next()){
                count++;
             }
             
         } catch (ClassNotFoundException | SQLException ex) {
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
          finally {
           try {
               if (pst != null) {
                   pst.close();
               }
               if(rs!=null)
                   rs.close();

           } catch (Exception e) {
               e.printStackTrace();
           }
       }
          
          return count;
          }
           
           //***********EO HOG STATS******************
               
               
        public ArrayList fetchMailAdmins(String formName) {
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchMailAdmins ");
        ArrayList<String> arr = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
           
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String field = "m_" + formName;
            if (formName.contains("nkn"))// code added by pr on 26thdec17
            {
                field = "m_nkn";
            }
            if (formName.contains("vpn") || formName.contains("change_add"))// code added by pr on 4thjan18
            {
                field = "m_vpn";
            }
            if (formName.contains("emailactivate"))// code added by pr on 26thdec17
            {
                field = "m_email_act";
            }
            if (field.matches("^[a-zA-Z_]*$")) {
                String qry = "select m_email FROM mailadmin_forms WHERE " + field + " = 'y' "
                        + ""
                        + " GROUP BY m_email ORDER BY m_email ASC ";
                ps = conSlave.prepareStatement(qry);
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchMailAdmins func query is " + ps);
                rs = ps.executeQuery();
                if (!formName.equals("ldap")) // if around and below else  added by pr on 2ndmay18
                {
                    while (rs.next()) {
                        arr.add(rs.getString("m_email"));
                    }
                } else {
                    arr.add("rajesh.singh@nic.in");
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function mailadmin forms " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 170" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 171" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 172" + e.getMessage());
                }
            }
        }
       if(arr.contains("tiwari.ashwini@nic.in")){
            arr.remove("tiwari.ashwini@nic.in");
        }
          if(arr.contains("neeraj.tuteja@nic.in")){
            arr.remove("neeraj.tuteja@nic.in");
        }
            if(arr.contains("rahulm.96@nic.in")){
            arr.remove("rahulm.96@nic.in");
        }
              if(arr.contains("rajesh.101@nic.in")){
            arr.remove("rajesh.101@nic.in");
        }
                if(arr.contains("prog18.nhq-dl@nic.in")){
            arr.remove("prog18.nhq-dl@nic.in");
        }
                 if(arr.contains("ssa1.shq.ap@supportgov.in")){  // Shashikannth Haydarabad 20-jan-2022
            arr.remove("ssa1.shq.ap@supportgov.in");
        }
                  if(arr.contains("pkr.gaurav@supportgov.in")){  // prabhat  
            arr.remove("pkr.gaurav@supportgov.in");
        }
                  if (arr.contains("prog15.nhq-dl@nic.in")) {
            arr.remove("prog15.nhq-dl@nic.in");
        }
          if (arr.contains("prog16.nhq-dl@supportgov.in")) {
            arr.remove("prog16.nhq-dl@supportgov.in");
        }
           if (arr.contains("manikant.96@supportgov.in")) {
            arr.remove("manikant.96@supportgov.in");
        }
            if (arr.contains("vivekbaghel.999@supportgov.in")) {
            arr.remove("vivekbaghel.999@supportgov.in");
        }
        return arr;
    }
    
        
        public Map<String,Object> hogRegistration(CoordinatorPanelBean coordinatorPanelBean){
            int i=0;
            HashMap<String, Object> map = new HashMap<String, Object>();
        String sql="insert into hog_ministry (hog_email,category,ministry,hog_name,hog_mobile) values(?,?,?,?,?)";
        PreparedStatement pst = null;
         try {
                
             con = DbConnection.getConnection();
             pst=con.prepareStatement(sql);
             pst.setString(1, coordinatorPanelBean.getHog_email());
             pst.setString(2, coordinatorPanelBean.getEmp_category_hog());
              switch (coordinatorPanelBean.getEmp_category_hog()) {
                 case "Central":
                      pst.setString(3, coordinatorPanelBean.getEmp_min_state_org_hog());
                     break;
                 case "State":
                      pst.setString(3, coordinatorPanelBean.getStateCode_hog());
                     break;
                 case "Psu":
                    pst.setString(3, coordinatorPanelBean.getOrg_hog());
                     break;
                 case "Const":
                     pst.setString(3,coordinatorPanelBean.getOrg_hog());
                    
                     break;
                     
                 case "Nkn":
                     pst.setString(3, coordinatorPanelBean.getOrg_hog());
                     
                     break;
                 case "Project":
                     pst.setString(3, coordinatorPanelBean.getOrg_hog());
                     
                     break;
                 case "UT":
                     pst.setString(3, coordinatorPanelBean.getOrg_hog());
                     
                     break;
                 case "Others":
                     pst.setString(3, coordinatorPanelBean.getOrg_hog());
                     
                  
                     break;
                 default:
                     break;
             }
             
             
           
             pst.setString(4, coordinatorPanelBean.getHog_name());
             pst.setString(5, coordinatorPanelBean.getHog_mobile());
             i= pst.executeUpdate();
             if(i>0){
             map.put("status","success");
             }
             else{
             map.put("status", "failure");
             }
            
         } catch (ClassNotFoundException | SQLException ex) {
              map.put("status", ex.getMessage());
             Logger.getLogger(CoordinatorPanelDao.class.getName()).log(Level.SEVERE, null, ex);
         }
           
         return map;
      
}
       
}
