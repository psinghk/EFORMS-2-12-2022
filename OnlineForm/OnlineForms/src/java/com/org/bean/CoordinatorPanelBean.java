/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.bean;

/**
 *
 * @author Satya
 */
public class CoordinatorPanelBean {
   
    //**Emp coordinator table variables
    private int dept_id;
   private int emp_id;
   private String emp_category;
   private String  emp_min_state_org;
   private String emp_dept;
   private String emp_sub_dept;
   private String emp_mail_acc_cat;
   private String emp_sms_acc_cat;
   private String emp_domain;
   private String emp_bo_id;
   private String emp_coord_mobile;
   private String emp_coord_name;
   private String emp_coord_email;
   private String emp_admin_email;
   private String emp_status;
   private String emp_createdon;
   private String emp_addedby;
   private String emp_ip;
   private String stateCode;
   private String state_dept;
   private String org;
   private String other_dept;
   private String requested_to;
   
   private int ca_id;
   private String ca_name;
   private String ca_email;
   private String ca_mobile;
   private String ca_createdon;
   private String ca_status;
   private int total_count_pending;
   private int total_count_completed;
   private int total_count_forwarded;
   private int total_count_rejected;
   //**EO Emp coordinator table variables
   
   private String login_user_email; 
   private String to_email;
   private String to_name;
   private String to_mobile;
   private String form_name;
   private String request_pending;
   private String status;
   private String response_status;
   
//bean for final audit track
   private int trackid;
   private String registration_no;
   private String applicant_email;
   private String applicant_mobile;
   private String applicant_name;
   private String applicant_ip;
   private String applicant_datetime;
   private String applicant_remarks;
   private String upload_work_order;
  // private String ca_email;
   //private String ca_mobile;
  // private String ca_name;
   private String ca_ip;
   private String ca_datetime;
   private String ca_remarks;
   private String us_email;
   private String us_mobile;
   private String us_name;
   private String us_ip;
   private String us_datetime;
   private String us_remarks;
   private String coordinator_email;
   private String coordinator_mobile;
   private String coordinator_name;
   private String coordinator_ip;
   private String coordinator_datetime;
   private String coordinator_remarks;
   private String support_email;
   private String support_mobile;
   private String support_name;
   private String support_ip;
   private String support_datetime;
   private String support_remarks;
   private String da_email;
   private String da_mobile;
   private String da_name;
   private String da_ip;
   private String da_datetime;
   private String da_remarks;
   private String admin_email;
   private String admin_mobile;
   private String admin_name;
   private String admin_ip;
   private String admin_datetime;
   private String admin_remarks;
    private String stat_final_id;
    
    private int pending_request_count;
    private int completed_request_count;
    public int forwarded_request_count;
    public int rejected_request_count;
    
    private String stat_type;
    private String stat_forwarded_by;
    private String stat_forwarded_by_user;
    private String stat_forwarded_to;
    private String stat_forwarded_to_user;
    private String stat_remarks;
    
    private String requested_by;
    //private String status;
    //private String form_name;
    //private String to_email;
    //private String to_mobile;
    //private String to_name;
    private String to_datetime;
    private String app_user_type;
    private String on_hold;
    private String app_ca_type;
    private int co_id;
    private String vpn_reg_no;
    private String sign_cert;
    private String rename_sign_cert;
    private String ca_sign_cert;
    private String ca_rename_sign_cert;
    private int count;
    
    private String hog_name;
    private String hog_email;
    private String hog_mobile;
    private String type;
    
      private String hod_name;
    private String hod_email;
    private String hod_mobile;
    
    private String old_dept;
    private String new_dept;
    
    //added for hog
    private String emp_category_hog;
    private String emp_min_state_org_hog;
    private String stateCode_hog;
    private String org_hog;
    public String getHog_name() {
        return hog_name;
    }

    public void setHog_name(String hog_name) {
        this.hog_name = hog_name;
    }

    public String getHog_email() {
        return hog_email;
    }

    public void setHog_email(String hog_email) {
        this.hog_email = hog_email;
    }

    public String getHog_mobile() {
        return hog_mobile;
    }

    public void setHog_mobile(String hog_mobile) {
        this.hog_mobile = hog_mobile;
    }

    public String getHod_name() {
        return hod_name;
    }

    public void setHod_name(String hod_name) {
        this.hod_name = hod_name;
    }

    public String getHod_email() {
        return hod_email;
    }

    public void setHod_email(String hod_email) {
        this.hod_email = hod_email;
    }

    public String getHod_mobile() {
        return hod_mobile;
    }

    public void setHod_mobile(String hod_mobile) {
        this.hod_mobile = hod_mobile;
    }
    
    

    public int getTrackid() {
        return trackid;
    }

    public void setTrackid(int trackid) {
        this.trackid = trackid;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getApplicant_email() {
        return applicant_email;
    }

    public void setApplicant_email(String applicant_email) {
        this.applicant_email = applicant_email;
    }

    public String getApplicant_mobile() {
        return applicant_mobile;
    }

    public void setApplicant_mobile(String applicant_mobile) {
        this.applicant_mobile = applicant_mobile;
    }

    public String getApplicant_name() {
        return applicant_name;
    }

    public void setApplicant_name(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    public String getApplicant_ip() {
        return applicant_ip;
    }

    public void setApplicant_ip(String applicant_ip) {
        this.applicant_ip = applicant_ip;
    }

    public String getApplicant_datetime() {
        return applicant_datetime;
    }

    public void setApplicant_datetime(String applicant_datetime) {
        this.applicant_datetime = applicant_datetime;
    }

    public String getApplicant_remarks() {
        return applicant_remarks;
    }

    public void setApplicant_remarks(String applicant_remarks) {
        this.applicant_remarks = applicant_remarks;
    }

    public String getUpload_work_order() {
        return upload_work_order;
    }

    public void setUpload_work_order(String upload_work_order) {
        this.upload_work_order = upload_work_order;
    }

    public String getCa_ip() {
        return ca_ip;
    }

    public void setCa_ip(String ca_ip) {
        this.ca_ip = ca_ip;
    }

    public String getCa_datetime() {
        return ca_datetime;
    }

    public void setCa_datetime(String ca_datetime) {
        this.ca_datetime = ca_datetime;
    }

    public String getCa_remarks() {
        return ca_remarks;
    }

    public void setCa_remarks(String ca_remarks) {
        this.ca_remarks = ca_remarks;
    }

    public String getUs_email() {
        return us_email;
    }

    public void setUs_email(String us_email) {
        this.us_email = us_email;
    }

    public String getUs_mobile() {
        return us_mobile;
    }

    public void setUs_mobile(String us_mobile) {
        this.us_mobile = us_mobile;
    }

    public String getUs_name() {
        return us_name;
    }

    public void setUs_name(String us_name) {
        this.us_name = us_name;
    }

    public String getUs_ip() {
        return us_ip;
    }

    public void setUs_ip(String us_ip) {
        this.us_ip = us_ip;
    }

    public String getUs_datetime() {
        return us_datetime;
    }

    public void setUs_datetime(String us_datetime) {
        this.us_datetime = us_datetime;
    }

    public String getUs_remarks() {
        return us_remarks;
    }

    public void setUs_remarks(String us_remarks) {
        this.us_remarks = us_remarks;
    }

    public String getCoordinator_email() {
        return coordinator_email;
    }

    public void setCoordinator_email(String coordinator_email) {
        this.coordinator_email = coordinator_email;
    }

    public String getCoordinator_mobile() {
        return coordinator_mobile;
    }

    public void setCoordinator_mobile(String coordinator_mobile) {
        this.coordinator_mobile = coordinator_mobile;
    }

    public String getCoordinator_name() {
        return coordinator_name;
    }

    public void setCoordinator_name(String coordinator_name) {
        this.coordinator_name = coordinator_name;
    }

    public String getCoordinator_ip() {
        return coordinator_ip;
    }

    public void setCoordinator_ip(String coordinator_ip) {
        this.coordinator_ip = coordinator_ip;
    }

    public String getCoordinator_datetime() {
        return coordinator_datetime;
    }

    public void setCoordinator_datetime(String coordinator_datetime) {
        this.coordinator_datetime = coordinator_datetime;
    }

    public String getCoordinator_remarks() {
        return coordinator_remarks;
    }

    public void setCoordinator_remarks(String coordinator_remarks) {
        this.coordinator_remarks = coordinator_remarks;
    }

    public String getSupport_email() {
        return support_email;
    }

    public void setSupport_email(String support_email) {
        this.support_email = support_email;
    }

    public String getSupport_mobile() {
        return support_mobile;
    }

    public void setSupport_mobile(String support_mobile) {
        this.support_mobile = support_mobile;
    }

    public String getSupport_name() {
        return support_name;
    }

    public void setSupport_name(String support_name) {
        this.support_name = support_name;
    }

    public String getSupport_ip() {
        return support_ip;
    }

    public void setSupport_ip(String support_ip) {
        this.support_ip = support_ip;
    }

    public String getSupport_datetime() {
        return support_datetime;
    }

    public void setSupport_datetime(String support_datetime) {
        this.support_datetime = support_datetime;
    }

    public String getSupport_remarks() {
        return support_remarks;
    }

    public void setSupport_remarks(String support_remarks) {
        this.support_remarks = support_remarks;
    }

    public String getDa_email() {
        return da_email;
    }

    public void setDa_email(String da_email) {
        this.da_email = da_email;
    }

    public String getDa_mobile() {
        return da_mobile;
    }

    public void setDa_mobile(String da_mobile) {
        this.da_mobile = da_mobile;
    }

    public String getDa_name() {
        return da_name;
    }

    public void setDa_name(String da_name) {
        this.da_name = da_name;
    }

    public String getDa_ip() {
        return da_ip;
    }

    public void setDa_ip(String da_ip) {
        this.da_ip = da_ip;
    }

    public String getDa_datetime() {
        return da_datetime;
    }

    public void setDa_datetime(String da_datetime) {
        this.da_datetime = da_datetime;
    }

    public String getDa_remarks() {
        return da_remarks;
    }

    public void setDa_remarks(String da_remarks) {
        this.da_remarks = da_remarks;
    }

    public String getAdmin_email() {
        return admin_email;
    }

    public void setAdmin_email(String admin_email) {
        this.admin_email = admin_email;
    }

    public String getAdmin_mobile() {
        return admin_mobile;
    }

    public void setAdmin_mobile(String admin_mobile) {
        this.admin_mobile = admin_mobile;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getAdmin_ip() {
        return admin_ip;
    }

    public void setAdmin_ip(String admin_ip) {
        this.admin_ip = admin_ip;
    }

    public String getAdmin_datetime() {
        return admin_datetime;
    }

    public void setAdmin_datetime(String admin_datetime) {
        this.admin_datetime = admin_datetime;
    }

    public String getAdmin_remarks() {
        return admin_remarks;
    }

    public void setAdmin_remarks(String admin_remarks) {
        this.admin_remarks = admin_remarks;
    }

    public String getStat_final_id() {
        return stat_final_id;
    }

    public void setStat_final_id(String stat_final_id) {
        this.stat_final_id = stat_final_id;
    }

    public String getTo_datetime() {
        return to_datetime;
    }

    public void setTo_datetime(String to_datetime) {
        this.to_datetime = to_datetime;
    }

    public String getApp_user_type() {
        return app_user_type;
    }

    public void setApp_user_type(String app_user_type) {
        this.app_user_type = app_user_type;
    }

    public String getOn_hold() {
        return on_hold;
    }

    public void setOn_hold(String on_hold) {
        this.on_hold = on_hold;
    }

    public String getApp_ca_type() {
        return app_ca_type;
    }

    public void setApp_ca_type(String app_ca_type) {
        this.app_ca_type = app_ca_type;
    }

    public int getCo_id() {
        return co_id;
    }

    public void setCo_id(int co_id) {
        this.co_id = co_id;
    }

    public String getVpn_reg_no() {
        return vpn_reg_no;
    }

    public void setVpn_reg_no(String vpn_reg_no) {
        this.vpn_reg_no = vpn_reg_no;
    }

    public String getSign_cert() {
        return sign_cert;
    }

    public void setSign_cert(String sign_cert) {
        this.sign_cert = sign_cert;
    }

    public String getRename_sign_cert() {
        return rename_sign_cert;
    }

    public void setRename_sign_cert(String rename_sign_cert) {
        this.rename_sign_cert = rename_sign_cert;
    }

    public String getCa_sign_cert() {
        return ca_sign_cert;
    }

    public void setCa_sign_cert(String ca_sign_cert) {
        this.ca_sign_cert = ca_sign_cert;
    }

    public String getCa_rename_sign_cert() {
        return ca_rename_sign_cert;
    }

    public void setCa_rename_sign_cert(String ca_rename_sign_cert) {
        this.ca_rename_sign_cert = ca_rename_sign_cert;
    }
    
    
    

      
   
   
   
   
//**Emp coordinator get set   

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_category() {
        return emp_category;
    }

    public void setEmp_category(String emp_category) {
        this.emp_category = emp_category;
    }

    public String getEmp_min_state_org() {
        return emp_min_state_org;
    }

    public void setEmp_min_state_org(String emp_min_state_org) {
        this.emp_min_state_org = emp_min_state_org;
    }

    public String getEmp_dept() {
        return emp_dept;
    }

    public void setEmp_dept(String emp_dept) {
        this.emp_dept = emp_dept;
    }

    public String getEmp_sub_dept() {
        return emp_sub_dept;
    }

    public void setEmp_sub_dept(String emp_sub_dept) {
        this.emp_sub_dept = emp_sub_dept;
    }

    public String getEmp_mail_acc_cat() {
        return emp_mail_acc_cat;
    }

    public void setEmp_mail_acc_cat(String emp_mail_acc_cat) {
        this.emp_mail_acc_cat = emp_mail_acc_cat;
    }

    public String getEmp_sms_acc_cat() {
        return emp_sms_acc_cat;
    }

    public void setEmp_sms_acc_cat(String emp_sms_acc_cat) {
        this.emp_sms_acc_cat = emp_sms_acc_cat;
    }

    public String getEmp_domain() {
        return emp_domain;
    }

    public void setEmp_domain(String emp_domain) {
        this.emp_domain = emp_domain;
    }

    public String getEmp_bo_id() {
        return emp_bo_id;
    }

    public void setEmp_bo_id(String emp_bo_id) {
        this.emp_bo_id = emp_bo_id;
    }

    public String getEmp_coord_mobile() {
        return emp_coord_mobile;
    }

    public void setEmp_coord_mobile(String emp_coord_mobile) {
        this.emp_coord_mobile = emp_coord_mobile;
    }

    public String getEmp_coord_name() {
        return emp_coord_name;
    }

    public void setEmp_coord_name(String emp_coord_name) {
        this.emp_coord_name = emp_coord_name;
    }

    public String getEmp_coord_email() {
        return emp_coord_email;
    }

    public void setEmp_coord_email(String emp_coord_email) {
        this.emp_coord_email = emp_coord_email;
    }

    public String getEmp_admin_email() {
        return emp_admin_email;
    }

    public void setEmp_admin_email(String emp_admin_email) {
        this.emp_admin_email = emp_admin_email;
    }

  
    public String getEmp_createdon() {
        return emp_createdon;
    }

    public void setEmp_createdon(String emp_createdon) {
        this.emp_createdon = emp_createdon;
    }

    public String getEmp_addedby() {
        return emp_addedby;
    }

    public void setEmp_addedby(String emp_addedby) {
        this.emp_addedby = emp_addedby;
    }

    public String getEmp_ip() {
        return emp_ip;
    }

    public void setEmp_ip(String emp_ip) {
        this.emp_ip = emp_ip;
    }

  
    public String getEmp_status() {
        return emp_status;
    }

    public void setEmp_status(String emp_status) {
        this.emp_status = emp_status;
    }

    public int getCa_id() {
        return ca_id;
    }

    public void setCa_id(int ca_id) {
        this.ca_id = ca_id;
    }

    public String getCa_name() {
        return ca_name;
    }

    public void setCa_name(String ca_name) {
        this.ca_name = ca_name;
    }

    public String getCa_email() {
        return ca_email;
    }

    public void setCa_email(String ca_email) {
        this.ca_email = ca_email;
    }

    public String getCa_mobile() {
        return ca_mobile;
    }

    public void setCa_mobile(String ca_mobile) {
        this.ca_mobile = ca_mobile;
    }

    public String getCa_createdon() {
        return ca_createdon;
    }

    public void setCa_createdon(String ca_createdon) {
        this.ca_createdon = ca_createdon;
    }

    public String getCa_status() {
        return ca_status;
    }

    public void setCa_status(String ca_status) {
        this.ca_status = ca_status;
    }
   
   
   
//*********************
    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_mobile() {
        return to_mobile;
    }

    public void setTo_mobile(String to_mobile) {
        this.to_mobile = to_mobile;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public String getRequest_pending() {
        return request_pending;
    }

    public void setRequest_pending(String request_pending) {
        this.request_pending = request_pending;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse_status() {
        return response_status;
    }

    public void setResponse_status(String response_status) {
        this.response_status = response_status;
    }

    /**
     * @return the login_user_email
     */
    public String getLogin_user_email() {
        return login_user_email;
    }

    /**
     * @param login_user_email the login_user_email to set
     */
    public void setLogin_user_email(String login_user_email) {
        this.login_user_email = login_user_email;
    }

    /**
     * @return the stateCode
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * @param stateCode the stateCode to set
     */
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * @return the state_dept
     */
    public String getState_dept() {
        return state_dept;
    }

    /**
     * @param state_dept the state_dept to set
     */
    public void setState_dept(String state_dept) {
        this.state_dept = state_dept;
    }

    /**
     * @return the org
     */
    public String getOrg() {
        return org;
    }

    /**
     * @param org the org to set
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the requested_by
     */
    public String getRequested_by() {
        return requested_by;
    }

    /**
     * @param requested_by the requested_by to set
     */
    public void setRequested_by(String requested_by) {
        this.requested_by = requested_by;
    }

    /**
     * @return the other_dept
     */
    public String getOther_dept() {
        return other_dept;
    }

    /**
     * @param other_dept the other_dept to set
     */
    public void setOther_dept(String other_dept) {
        this.other_dept = other_dept;
    }

    public int getPending_request_count() {
        return pending_request_count;
    }

    public void setPending_request_count(int pending_request_count) {
        this.pending_request_count = pending_request_count;
    }

    public int getCompleted_request_count() {
        return completed_request_count;
    }

    public void setCompleted_request_count(int completed_request_count) {
        this.completed_request_count = completed_request_count;
    }

    /**
     * @return the requested_to
     */
    public String getRequested_to() {
        return requested_to;
    }

    /**
     * @param requested_to the requested_to to set
     */
    public void setRequested_to(String requested_to) {
        this.requested_to = requested_to;
    }

    /**
     * @return the forwarded_request_count
     */
    public int getForwarded_request_count() {
        return forwarded_request_count;
    }

    /**
     * @param forwarded_request_count the forwarded_request_count to set
     */
    public void setForwarded_request_count(int forwarded_request_count) {
        this.forwarded_request_count = forwarded_request_count;
    }

    /**
     * @return the rejected_request_count
     */
    public int getRejected_request_count() {
        return rejected_request_count;
    }

    /**
     * @param rejected_request_count the rejected_request_count to set
     */
    public void setRejected_request_count(int rejected_request_count) {
        this.rejected_request_count = rejected_request_count;
    }

    /**
     * @return the dept_id
     */
    public int getDept_id() {
        return dept_id;
    }

    /**
     * @param dept_id the dept_id to set
     */
    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    /**
     * @return the stat_type
     */
    public String getStat_type() {
        return stat_type;
    }

    /**
     * @param stat_type the stat_type to set
     */
    public void setStat_type(String stat_type) {
        this.stat_type = stat_type;
    }

    public String getStat_forwarded_by() {
        return stat_forwarded_by;
    }

    public void setStat_forwarded_by(String stat_forwarded_by) {
        this.stat_forwarded_by = stat_forwarded_by;
    }

    public String getStat_forwarded_by_user() {
        return stat_forwarded_by_user;
    }

    public void setStat_forwarded_by_user(String stat_forwarded_by_user) {
        this.stat_forwarded_by_user = stat_forwarded_by_user;
    }

    public String getStat_forwarded_to() {
        return stat_forwarded_to;
    }

    public void setStat_forwarded_to(String stat_forwarded_to) {
        this.stat_forwarded_to = stat_forwarded_to;
    }

    public String getStat_forwarded_to_user() {
        return stat_forwarded_to_user;
    }

    public void setStat_forwarded_to_user(String stat_forwarded_to_user) {
        this.stat_forwarded_to_user = stat_forwarded_to_user;
    }

    public String getStat_remarks() {
        return stat_remarks;
    }

    public void setStat_remarks(String stat_remarks) {
        this.stat_remarks = stat_remarks;
    }

    /**
     * @return the old_dept
     */
    public String getOld_dept() {
        return old_dept;
    }

    /**
     * @param old_dept the old_dept to set
     */
    public void setOld_dept(String old_dept) {
        this.old_dept = old_dept;
    }

    /**
     * @return the new_dept
     */
    public String getNew_dept() {
        return new_dept;
    }

    /**
     * @param new_dept the new_dept to set
     */
    public void setNew_dept(String new_dept) {
        this.new_dept = new_dept;
    }

  
    /**
     * @return the total_count_pending
     */
    public int getTotal_count_pending() {
        return total_count_pending;
    }

    /**
     * @param total_count_pending the total_count_pending to set
     */
    public void setTotal_count_pending(int total_count_pending) {
        this.total_count_pending = total_count_pending;
    }

    public int getTotal_count_completed() {
        return total_count_completed;
    }

    public void setTotal_count_completed(int total_count_completed) {
        this.total_count_completed = total_count_completed;
    }

    public int getTotal_count_forwarded() {
        return total_count_forwarded;
    }

    public void setTotal_count_forwarded(int total_count_forwarded) {
        this.total_count_forwarded = total_count_forwarded;
    }

    public int getTotal_count_rejected() {
        return total_count_rejected;
    }

    public void setTotal_count_rejected(int total_count_rejected) {
        this.total_count_rejected = total_count_rejected;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getEmp_category_hog() {
        return emp_category_hog;
    }

    public void setEmp_category_hog(String emp_category_hog) {
        this.emp_category_hog = emp_category_hog;
    }

    public String getEmp_min_state_org_hog() {
        return emp_min_state_org_hog;
    }

    public void setEmp_min_state_org_hog(String emp_min_state_org_hog) {
        this.emp_min_state_org_hog = emp_min_state_org_hog;
    }

    public String getStateCode_hog() {
        return stateCode_hog;
    }

    public void setStateCode_hog(String stateCode_hog) {
        this.stateCode_hog = stateCode_hog;
    }

    public String getOrg_hog() {
        return org_hog;
    }

    public void setOrg_hog(String org_hog) {
        this.org_hog = org_hog;
    }

    /**
     * @return the count_pending
     */
 

    /**
     * @return the pending_request_count
     */


   
   
   
   
}
