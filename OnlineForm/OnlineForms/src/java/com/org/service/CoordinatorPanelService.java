/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.bean.CoordinatorPanelBean;
import com.org.bean.UserData;
import com.org.dao.CoordinatorPanelDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Satya
 */
public class CoordinatorPanelService {

//       
//       public Map<String, Object>  addDepartmentRequest(CoordinatorPanelBean cpanelBean){
//           CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
//           Map<String, Object> map = cpaneldao.addDepartmentRequest(cpanelBean);
//           return map;
//       }
    
     public Map<String,Object> getCountsRequestCoordinator(String loggedinHog){
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.getCountsRequestCoordinator(loggedinHog);
        return map;
     
     }
    public Map<String,Object> rejectDepartment(String id){
    
    CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.rejectDepartment(id);
        return map;
    
    }
    public Map<String, Object> approvePendingDepartments(int id, String coordinator_email) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.approvePendingDepartments(id, coordinator_email);
        return map;
    }

    
   public Map<String,Object> getMinistryInfoOfHOG(String email){
       Map<String, Object> map = new HashMap<String, Object>();
       CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
       List<CoordinatorPanelBean> list = cpaneldao.getMinistryInfoOfHOG(email);
       map.put("min", list);
       return map;
   }
        public Map<String, Object>  previewRequest(String registration_no){
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
            Map<String, Object> map = cpaneldao.previewRequest(registration_no);
            return map;
        }
        
      public Map<String, Object> trackRequestByHOG(String registration_no) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.trackRequestByHOG(registration_no);
        return map;
    }
        
         public Map<String, Object> updateDepartmentHOG(String emp_category,String emp_min_state_org,String emp_dept,String new_dept,String state_code,String org,String state_dept){
          CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
          CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
       cpanelbean.setEmp_category(emp_category);
       cpanelbean.setEmp_min_state_org(emp_min_state_org);
       cpanelbean.setEmp_dept(emp_dept);
       //cpanelbean.setOld_dept(emp_dept);
       cpanelbean.setStateCode(state_code);
       cpanelbean.setState_dept(state_dept);
       cpanelbean.setNew_dept(new_dept);
       cpanelbean.setOrg(org);
          
        //Map<String, Object> map = cpaneldao.updateDepartmentHOG(category,ministry,old_dept,new_dept);
        Map<String, Object> map = cpaneldao.updateDepartmentHOG(cpanelbean);
         return map;
         }
     
    public Map<String, Object> updateDepartmentPendingRequestById(CoordinatorPanelBean cpanelbean) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.updateDepartmentPendingRequestById(cpanelbean);
        return map;
    }

        public Map<String, Object> viewDepartmentPendingRequestById(int id){
            CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
            Map<String, Object> map = cpaneldao.viewDepartmentPendingRequestById(id);
            return map;
        }
        
         public Map<String, Object>  showDepartmentPendingRequest(String email){
           CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
           Map<String, Object> map = cpaneldao.showDepartmentPendingRequest(email);
           return map;
       }
         //NOT USED UNUSED CODE
         public Map<String, Object> approveDepartment(String emp_category,String emp_min_state_org,String department,String approver_email,String emp_coord_email){
          HashMap<String, Object> map = new HashMap<String, Object>();
          CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
          int i=cpaneldao.approveDepartment( emp_category, emp_min_state_org,department,approver_email, emp_coord_email);
          if (i > 0) {
               map.put("status", "approved");
           } else {
               map.put("status", "failed");
           }
           return map;
          
          
         }
         
          public Map<String,Object> pullbackAllRequestHog(String registration_no,String loggedinHog){
              CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
              Map<String, Object> map = cpaneldao.pullbackAllRequestHog(registration_no, loggedinHog);
              return map;
    }
         
         public Map<String, Object> pullbackRequestHog(String registration_no, String loggedinHog) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.pullbackRequestHog(registration_no, loggedinHog);
        return map;
    }
        
    public Map<String, Object> approveAllRequestHog(String registration_no, String loggedinHog) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.approveAllRequestHog(registration_no, loggedinHog);
        return map;

    }
         
         public Map<String, Object> approveRequestHog(String registration_no, String loggedinHog) {
              CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
               Map<String, Object> map = cpaneldao.approveRequestHog(registration_no, loggedinHog);
               return map;
             
             
         }
         
        public Map<String, Object> rejectRequestHog(String registration_no, String loggedinHog) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = cpaneldao.rejectRequestHog(registration_no, loggedinHog);
        return map;
    }
    public Map<String, Object> getPendingRequestOfHog(String loggedinHog){
     HashMap<String, Object> map = new HashMap<>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        List<CoordinatorPanelBean> list = cpaneldao.getPendingRequestOfHog(loggedinHog);
        int count = cpaneldao.getPendingRequestCountOfHog(loggedinHog);
         if (!list.isEmpty()) {
            map.put("status", list);
            map.put("totalcount", count);
        } else {
            map.put("status", "empty");
            map.put("totalcount", count);
        }
        return map;
    }
        
        public Map<String, Object> getPendingRequestOfCoordinator(int emp_id,String loggedinHog) {
        HashMap<String, Object> map = new HashMap<>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
       // List<CoordinatorPanelBean> list = cpaneldao.getPendingRequestOfCoordinator(emp_id,loggedinHog);
        List<CoordinatorPanelBean> list = cpaneldao.getPendingRequestOfCoordinatorMinwise(emp_id,loggedinHog);
       int count = cpaneldao.getPendingRequestCountOfCoordinatorByIdMinwise(emp_id,loggedinHog);
        if (!list.isEmpty()) {
            map.put("status", list);
            map.put("totalcount", count);
            
        } else {
            map.put("status", "empty");
            map.put("totalcount", count);
        }
        return map;

    }
        
         public Map<String, Object> getCompletedRequestOfCoordinator(int emp_id,String loggedinHog) {
        HashMap<String, Object> map = new HashMap<>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        List<CoordinatorPanelBean> list = cpaneldao.getCompletedRequestOfCoordinatorByIdMinwise(emp_id,loggedinHog);
        if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;

    }
         
            public Map<String, Object> getForwardedRequestOfCoordinator(int emp_id,String loggedinHog) {
        HashMap<String, Object> map = new HashMap<>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        List<CoordinatorPanelBean> list = cpaneldao.getForwardedRequestOfCoordinatorbyIdMinwise(emp_id,loggedinHog);
        if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;

    }
            
                      public Map<String, Object> getRejectedRequestOfCoordinator(int emp_id,String loggedinHog) {
        HashMap<String, Object> map = new HashMap<>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        List<CoordinatorPanelBean> list = cpaneldao.getRejectRequestOfCoordinatorByIdMinwise(emp_id,loggedinHog);
        if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;

    }
         
   
        
    public HashMap<String, Object> insertCoordinatorTable(CoordinatorPanelBean cpanelbean) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        HashMap<String, Object> map = cpaneldao.insertCoordinatorTable(cpanelbean);
        return map;
    }
    
    public Map<String, Object> viewAllCoordinators(String loggedinUser,UserData userdata) {
        Map<String, Object> map = new HashMap<String, Object>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        
        List<CoordinatorPanelBean> list = cpaneldao.viewAllCoordinators(loggedinUser,userdata);
        if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;
    }
     public Map<String, Object> checkCoordinatorExistance(String coordinator_email){
     
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
         Map<String, Object> map = cpaneldao.checkCoordinatorExistance(coordinator_email);
         return map;
     }
    
    public Map<String, Object> viewCoordinatorsById(int emp_id,String loggedinHog) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String,Object> map=new HashMap();
        Map<String, Object> mapdata = cpaneldao.viewCoordinatorsById(emp_id,loggedinHog);
        if (!mapdata.isEmpty()) {
            map.put("status", mapdata);
        } else {
            map.put("status", "empty");
        }
        return map;
    }
    
    
    
     public Map<String, Object> updateCoordinator(CoordinatorPanelBean cpanelbean) {
      CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
      Map<String,Object> map=new HashMap();
        int i = cpaneldao.updateCoordinator(cpanelbean);
        if (i > 0) {
            map.put("status", "updated");
        } else {
            map.put("status", "failed");
        }
        return map;
     }
    
     public Map<String, Object> deleteCoordinatorById(int emp_id,String loginuser) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = new HashMap();
        int i = cpaneldao.deleteCoordinatorById(emp_id,loginuser);
        if (i > 0) {
            map.put("status", "deleted");
        } else {
            map.put("status", "unable to delete");
        }
        return map;

    }
     
       public Map<String, Object> addDepartmentHOG(String emp_category,String emp_min_state_org,String emp_dept,String emp_coord_mobile,String emp_coord_name,String emp_coord_email,String emp_ip,String state_code,String state_dept,String org){
       CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
       CoordinatorPanelBean cpanelbean=new CoordinatorPanelBean();
       cpanelbean.setEmp_category(emp_category);
       cpanelbean.setEmp_min_state_org(emp_min_state_org);
       cpanelbean.setEmp_dept(emp_dept);
       cpanelbean.setEmp_coord_mobile(emp_coord_mobile);
       cpanelbean.setEmp_coord_name(emp_coord_name);
       cpanelbean.setEmp_coord_email(emp_coord_email);
       cpanelbean.setEmp_ip(emp_ip);
       cpanelbean.setStateCode(state_code);
       cpanelbean.setState_dept(state_dept);;
       cpanelbean.setOrg(org);
       Map<String, Object> map = cpaneldao.addDepartmentHOG(cpanelbean);
       
           return map;
       }
       
       
     public Map<String, Object> getCategoryHOG(String hog_email){
     Map<String, Object> map = new HashMap<String, Object>();
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
           List<String> list = cpaneldao.getCategoryHOG(hog_email);
       if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;
     
     }
     
     
      public List<String> getAllCategory() {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        return cpaneldao.getAllCategory();
    }
        
     
     public List<String> getAllCategoryMinistry(String category){
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
     return cpaneldao.getAllCategoryMinistry(category);
      
     
     }
     
           public Map<String, Object> getAllCategoryMinistryDepartment(String category,String ministry){
     Map<String, Object> map = new HashMap<String, Object>();
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
           List<String> list = cpaneldao.getAllCategoryMinistryDepartment(category, ministry);
       if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;
     
     }
             
             
       public Map<String, Object> getCategoryMinistryHOG(String hog_email,String category){
     Map<String, Object> map = new HashMap<String, Object>();
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
           List<String> list = cpaneldao.getCategoryMinistryHOG(hog_email,category);
       if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;
     
     }
       
            public Map<String, Object> getCategoryMinistryDepartmentHOG(String category,String ministry){
     Map<String, Object> map = new HashMap<String, Object>();
     CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
           List<String> list = cpaneldao.getCategoryMinistryDepartmentHOG(category, ministry);
       if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;
     
     }
       
       
     //RO Panel
      public HashMap<String, Object> insertRoTable(CoordinatorPanelBean cpanelbean) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        int i = cpaneldao.insertRoTable(cpanelbean);
        if (i > 0) {
            map.put("status", "inserted");
        } else {
            map.put("status", "failed");
        }
        return map;
    }
     
      public Map<String, Object> viewAllRo() {
        Map<String, Object> map = new HashMap<String, Object>();
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        List<CoordinatorPanelBean> list = cpaneldao.viewAllRo();
        if (!list.isEmpty()) {
            map.put("status", list);
        } else {
            map.put("status", "empty");
        }
        return map;
    }
      
        public Map<String, Object> viewRoById(int emp_id) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String,Object> map=new HashMap();
        Map<String, Object> mapdata = cpaneldao.viewRoById(emp_id);
        if (!mapdata.isEmpty()) {
            map.put("status", mapdata);
        } else {
            map.put("status", "empty");
        }
        return map;
    }
        
      public Map<String, Object> updateRo(CoordinatorPanelBean cpanelbean) {
      CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
      Map<String,Object> map=new HashMap();
        int i = cpaneldao.updateRo(cpanelbean);
        if (i > 0) {
            map.put("status", "updated");
        } else {
            map.put("status", "failed");
        }
        return map;
     }
    
          
        public Map<String, Object> deleteRoById(int emp_id, String loginuser) {
        CoordinatorPanelDao cpaneldao = new CoordinatorPanelDao();
        Map<String, Object> map = new HashMap();
        int i = cpaneldao.deleteRoById(emp_id, loginuser);
        if (i > 0) {
            map.put("status", "deleted");
        } else {
            map.put("status", "unable to delete");
        }
        return map;

    }
    
     
     
        
        
        
        
    
    public Map<String, Object> getStatsPendingRequestRO() {
        CoordinatorPanelDao coorddao = new CoordinatorPanelDao();
        ArrayList<CoordinatorPanelBean> list = coorddao.getStatsPendingRequestRO();
         Map<String, Object> map = new HashMap();

        if (!list.isEmpty()) {
             map.put("status",list);
            
        } else {
             map.put("status","No Pending Status for CO");
        }
        return map;

    }

    public Map<String, Object>  getStatsPendingRequestCO() {
        CoordinatorPanelDao coorddao = new CoordinatorPanelDao();
        ArrayList<CoordinatorPanelBean> list = coorddao.getStatsPendingRequestRO();
        Map<String, Object> map = new HashMap();
        if (!list.isEmpty()) {
             map.put("status",list);
            
        } else {
             map.put("status","No Pending Status for CO");
        }
        return map;
    }
    
    public Map<String, Object> hogRegistration(CoordinatorPanelBean coordinatorPanelBean) {
        CoordinatorPanelDao coorddao = new CoordinatorPanelDao();
        Map<String, Object> map = coorddao.hogRegistration(coordinatorPanelBean);
        return map;
    }
}
