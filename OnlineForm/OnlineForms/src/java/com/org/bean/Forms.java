package com.org.bean;

public class Forms 
{    
    String hod_email,hod_mobile,sign_cert,rename_sign_cert,ca_sign_cert,ca_rename_sign_cert,pdf_path, status, supportRemarks;
    
    // start, code added by pr on 6thaug19
    
    boolean on_hold, qr_accountholder ;
    boolean isRaiseQueryEnabled;
    String showQueryResponse;

    public boolean isIsRaiseQueryEnabled() {
        return isRaiseQueryEnabled;
    }

    public void setIsRaiseQueryEnabled(boolean isRaiseQueryEnabled) {
        this.isRaiseQueryEnabled = isRaiseQueryEnabled;
    }

    public String getShowQueryResponse() {
        return showQueryResponse;
    }

    public void setShowQueryResponse(String showQueryResponse) {
        this.showQueryResponse = showQueryResponse;
    }
    
    public String getSupportRemarks() {
        return supportRemarks;
    }

    public void setSupportRemarks(String supportRemarks) {
        this.supportRemarks = supportRemarks;
    }
    
    public boolean isQr_accountholder() {
        return qr_accountholder;
    }

    // start, code added by pr on 28thdec17
    public void setQr_accountholder(boolean qr_accountholder) {
        this.qr_accountholder = qr_accountholder;
    }

    public boolean isOn_hold() {
        return on_hold;
    }

    public void setOn_hold(boolean on_hold) {
        this.on_hold = on_hold;
    }   
    
    // end, code added by pr on 6thaug19    

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

    public String getPdf_path() {
        return pdf_path;
    }

    public void setPdf_path(String pdf_path) {
        this.pdf_path = pdf_path;
    }
    
    // start, code added on 5thjun18
    
    Boolean isReject;

    public Boolean getIsReject() {
        return isReject;
    }

    public void setIsReject(Boolean isReject) {
        this.isReject = isReject;
    }
    
    
    // end, code added on 5thjun18
    
    // start, code added by pr on 22ndmay18 for datatable
    
    String actions ;

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }
    
    // end, code added by pr on 22ndmay18 for datatable
    
    
    // start, code added by pr on 3rdapr18, to fetch duplictae emails related to the mobile
    
    String mobile_ldap_email;

    public String getMobile_ldap_email() {
        return mobile_ldap_email;
    }

    public void setMobile_ldap_email(String mobile_ldap_email) {
        this.mobile_ldap_email = mobile_ldap_email;
    }
        
    // end, code added by pr on 3rdapr18
        
    // start, code added by pr on 29thmay19, added to include the form specific info on mouseover in the list pages
    
    String form_detail ;    
    
    public String getForm_detail() {
        return form_detail;
    }

    public void setForm_detail(String form_detail) {
        this.form_detail = form_detail;
    }
    
    // end, code added by pr on 29thmay19
    
    
    // status table related fields
    
    String stat_id;
    
    String stat_form_type;
    
    String stat_reg_no;
    
    String stat_type;
    
    String stat_forwarded_by;
    
    String stat_forwarded_by_user;
    
    String stat_forwarded_to;
    
    String stat_forwarded_to_user;
    
    String stat_remarks;
    
    String stat_createdon;
    
    Boolean showAction; // added on 15thnov17 to decide to show the forward/reject action in a particluar module based on role

    Boolean showResponse, showWorkOrder;

    Boolean showdnsEdit;
    Boolean showdnsCancel;
     public Boolean getShowdnsCancel() {
        return showdnsCancel;
    }

    public void setShowdnsCancel(Boolean showdnsCancel) {
        this.showdnsCancel = showdnsCancel;
    }
    String statusApi;

    public Boolean getShowWorkOrder() {
        return showWorkOrder;
    }

    public void setShowWorkOrder(Boolean showWorkOrder) {
        this.showWorkOrder = showWorkOrder;
    }

    public String getStatusApi() {
        return statusApi;
    }

    public void setStatusApi(String statusApi) {
        this.statusApi = statusApi;
    }

    public Boolean getShowdnsEdit() {
        return showdnsEdit;
    }

    public void setShowdnsEdit(Boolean showdnsEdit) {
        this.showdnsEdit = showdnsEdit;
    }
    
    
    // start, code added by pr on 
    
    Boolean showQuery; // added by pr on 9thapr18

    public Boolean getShowQuery() {
        return showQuery;
    }

    public void setShowQuery(Boolean showQuery) {
        this.showQuery = showQuery;
    }
    Boolean showQueryBy;  // added by rahul march 2021
    public Boolean getShowQueryBy() {
        return showQueryBy;
    }

    public void setShowQueryBy(Boolean showQueryBy) {
        this.showQueryBy = showQueryBy;
    }
    // end, code added by pr on 
    
    // start, code added by pr on 25thjul19
    
    String showErrorSuccess;
    
    public String getShowErrorSuccess() {
        return showErrorSuccess;
    }

    public void setShowErrorSuccess(String showErrorSuccess) {
        this.showErrorSuccess = showErrorSuccess;
    }
    
    // end, code added by pr on 25thjul19    
    
    
    Boolean showCreateAction ; // to show create id/mark as done in case of mail admin panel and for pending with mail-admin status

    Boolean showBulkLink;
    
    // start, code added by pr on 19thapr18
        
    String app_user_type;
    
    String app_ca_type;

    public String getApp_user_type() {
        return app_user_type;
    }

    public void setApp_user_type(String app_user_type) {
        this.app_user_type = app_user_type;
    }

    public String getApp_ca_type() {
        return app_ca_type;
    }

    public void setApp_ca_type(String app_ca_type) {
        this.app_ca_type = app_ca_type;
    }
    
    // end, code added by pr on 19thapr18
    
    
    // start, code added by pr on 12thapr18
    
    Boolean showApprove; // to show aprove link at the CA level based on some values in case of manual upload by user

    public Boolean getShowApprove() {
        return showApprove;
    }

    public void setShowApprove(Boolean showApprove) {
        this.showApprove = showApprove;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }
    
    String appType; // to pass app_type value i.e manual, esigned, online to decide whether popup needs to be opened
    
    
    // end, code added by pr on 12thapr18
    
    
    // start, code added by pr on 9thapr18 , query raise related fields
    
    
    String qr_id ;

    public String getQr_id() {
        return qr_id;
    }

    public void setQr_id(String qr_id) {
        this.qr_id = qr_id;
    }

    public String getQr_form_type() {
        return qr_form_type;
    }

    public void setQr_form_type(String qr_form_type) {
        this.qr_form_type = qr_form_type;
    }

    public String getQr_reg_no() {
        return qr_reg_no;
    }

    public void setQr_reg_no(String qr_reg_no) {
        this.qr_reg_no = qr_reg_no;
    }

    public String getQr_forwarded_by() {
        return qr_forwarded_by;
    }

    public void setQr_forwarded_by(String qr_forwarded_by) {
        this.qr_forwarded_by = qr_forwarded_by;
    }

    public String getQr_forwarded_by_user() {
        return qr_forwarded_by_user;
    }

    public void setQr_forwarded_by_user(String qr_forwarded_by_user) {
        this.qr_forwarded_by_user = qr_forwarded_by_user;
    }

    public String getQr_forwarded_to() {
        return qr_forwarded_to;
    }

    public void setQr_forwarded_to(String qr_forwarded_to) {
        this.qr_forwarded_to = qr_forwarded_to;
    }

    public String getQr_forwarded_to_user() {
        return qr_forwarded_to_user;
    }

    public void setQr_forwarded_to_user(String qr_forwarded_to_user) {
        this.qr_forwarded_to_user = qr_forwarded_to_user;
    }

    public String getQr_message() {
        return qr_message;
    }

    public void setQr_message(String qr_message) {
        this.qr_message = qr_message;
    }

    public String getQr_createdon() {
        return qr_createdon;
    }

    public void setQr_createdon(String qr_createdon) {
        this.qr_createdon = qr_createdon;
    }
    
    String qr_form_type ;
    
    String qr_reg_no;
    
    String qr_forwarded_by;
    
    String qr_forwarded_by_user;
    
    String qr_forwarded_to;
    
    String qr_forwarded_to_user;
    
    String qr_message;
    
    String qr_createdon;
        
    // end, code added by pr on 9thapr18
        
    // start, code added by pr on 19thjul19
    
    String rejected_by;

    public String getRejected_by() {
        return rejected_by;
    }

    public void setRejected_by(String rejected_by) {
        this.rejected_by = rejected_by;
    }
            
    // end, code added by pr on 19thjul19    
    
    // start, code added by pr on 28thdec17
    
    String is_created;
        
    String is_rejected;

    public String getIs_created() {
        return is_created;
    }

    public void setIs_created(String is_created) {
        this.is_created = is_created;
    }

    public String getIs_rejected() {
        return is_rejected;
    }

    public void setIs_rejected(String is_rejected) {
        this.is_rejected = is_rejected;
    }
     
    
    // end, code added by pr on 28thdec17
    
     // start, code added by pr on 29thmay18
    
    String reject_remarks;
    
    public String getReject_remarks() {
        return reject_remarks;
    }

    public void setReject_remarks(String reject_remarks) {
        this.reject_remarks = reject_remarks;
    }     
    
    // end, code added by pr on 29thmay18
    
    
    
    
    
     // start, code added on 13thdec17 to hide create link in bulk after completion
    
    Boolean showBulkCreateAction;

    public Boolean getShowBulkCreateAction() {
        return showBulkCreateAction;
    }

    public void setShowBulkCreateAction(Boolean showBulkCreateAction) {
        this.showBulkCreateAction = showBulkCreateAction;
    }
    
    // end, code added on 13thdec17 to hide create link in bulk after completion
    
    // start, code added by pr on 12thdec19
    
    Boolean showBulkUpdateAction;

    public Boolean getShowBulkUpdateAction() {
        return showBulkUpdateAction;
    }

    public void setShowBulkUpdateAction(Boolean showBulkUpdateAction) {
        this.showBulkUpdateAction = showBulkUpdateAction;
    }
    
    // end, code added by pr on 12thdec19
    
    String bulk_id;

    public String getBulk_id() {
        return bulk_id;
    }

    public void setBulk_id(String bulk_id) {
        this.bulk_id = bulk_id;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }
    
    
    
    
    
    // start, code added by pr on 13thaug18
    
    String name;

    String designation;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
       
    // end, code added by pr on 13thaug18
       
    
    String createdon;

    public Forms() {
    }

    public Boolean getShowBulkLink() {
        return showBulkLink;
    }

    public void setShowBulkLink(Boolean showBulkLink) {
        this.showBulkLink = showBulkLink;
    }
    
    public Boolean getShowCreateAction() {
        return showCreateAction;
    }

    public void setShowCreateAction(Boolean showCreateAction) {
        this.showCreateAction = showCreateAction;
    }
    
    
    public Boolean getShowAction() {
        return showAction;
    }

    public void setShowAction(Boolean showAction) {
        this.showAction = showAction;
    }
 
    // sms table related fields
    
    //String admin_email;
    
    String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    // bulk
    
    String email;
    
    // change ip
    
    //String administrator_contact ;
    
    //String administrator_email ;
    
    // dist
    
    String applicant_mobile;
    
    String uid;// added on 22ndnov17 for bulk users
    
    //String applicant_email;
    
    // gem
    
    //String aemail;
    
    // imap
    
    //String email;
    
    // ldap
    
    // mobile change
    
    //String old_mobile;
    
    // nkn
    
    // relay
    
    //String admin_mobile;
    
    // single
    
    //String alternate_email;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

 
    
    public  Forms( String stat_id, String  stat_form_type,String stat_reg_no, 
            String  stat_type,String stat_forwarded_by,String  stat_forwarded_by_user, 
            String stat_forwarded_to,String  stat_forwarded_to_user,String   stat_remarks,  String  stat_createdon, 
            Boolean showAction, Boolean showCreateAction, Boolean showBulkLink )
    {
        this.stat_id = stat_id;
        
        this.stat_form_type = stat_form_type;
        
        this.stat_reg_no = stat_reg_no;
        
        this.stat_type = stat_type;
        
        this.stat_forwarded_by = stat_forwarded_by;
        
        this.stat_forwarded_by_user = stat_forwarded_by_user;
        
        this.stat_forwarded_to = stat_forwarded_to;
        
        this.stat_forwarded_to_user = stat_forwarded_to_user;
        
        this.stat_remarks = stat_remarks;
        
        this.stat_createdon = stat_createdon;
        
        this.showAction = showAction;
        
        this.showCreateAction = showCreateAction;
        
        this.showBulkLink = showBulkLink;
    }

    public String get_stat_id() {
        return stat_id;
    }

    public void set_stat_id(String stat_id) {
        this.stat_id = stat_id;
    }

    public String get_stat_form_type() {
        return stat_form_type;
    }

    public void set_stat_form_type(String stat_form_type) {
        this.stat_form_type = stat_form_type;
    }

    public String get_stat_reg_no() {
        return stat_reg_no;
    }

    public void set_stat_reg_no(String stat_reg_no) {
        this.stat_reg_no = stat_reg_no;
    }

    public String get_stat_type() {
        return stat_type;
    }

    public void set_stat_type(String stat_type) {
        this.stat_type = stat_type;
    }

    public String get_stat_forwarded_by() {
        return stat_forwarded_by;
    }

    public void set_stat_forwarded_by(String stat_forwarded_by) {
        this.stat_forwarded_by = stat_forwarded_by;
    }

    public String get_stat_forwarded_by_user() {
        return stat_forwarded_by_user;
    }

    public void set_stat_forwarded_by_user(String stat_forwarded_by_user) {
        this.stat_forwarded_by_user = stat_forwarded_by_user;
    }

    public String get_stat_forwarded_to() {
        return stat_forwarded_to;
    }

    public void set_stat_forwarded_to(String stat_forwarded_to) {
        this.stat_forwarded_to = stat_forwarded_to;
    }

    public String get_stat_forwarded_to_user() {
        return stat_forwarded_to_user;
    }

    public void set_stat_forwarded_to_user(String stat_forwarded_to_user) {
        this.stat_forwarded_to_user = stat_forwarded_to_user;
    }

    public String get_stat_remarks() {
        return stat_remarks;
    }

    public void set_stat_remarks(String stat_remarks) {
        this.stat_remarks = stat_remarks;
    }

    public String get_stat_createdon() {
        return stat_createdon;
    }

    public void set_stat_createdon(String stat_createdon) {
        this.stat_createdon = stat_createdon;
    }
    

    /*public String get_admin_email() {
        return admin_email;
    }

    public void set_admin_email(String admin_email) {
        this.admin_email = admin_email;
    }*/

    public String get_mobile() {
        return mobile;
    }

    public void set_mobile(String mobile) {
        this.mobile = mobile;
    }

   
    /*public String get_administrator_contact() {
        return administrator_contact;
    }

    public void set_administrator_contact(String administrator_contact) {
        this.administrator_contact = administrator_contact;
    }

    public String get_administrator_email() {
        return administrator_email;
    }

    public void set_administrator_email(String administrator_email) {
        this.administrator_email = administrator_email;
    }*/

    public String get_applicant_mobile() {
        return applicant_mobile;
    }

    public void set_applicant_mobile(String applicant_mobile) {
        this.applicant_mobile = applicant_mobile;
    }

    /*public String get_applicant_email() {
        return applicant_email;
    }

    public void set_applicant_email(String applicant_email) {
        this.applicant_email = applicant_email;
    }

    public String get_aemail() {
        return aemail;
    }

    public void set_aemail(String aemail) {
        this.aemail = aemail;
    }*/

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

    /*public String get_old_mobile() {
        return old_mobile;
    }

    public void set_old_mobile(String old_mobile) {
        this.old_mobile = old_mobile;
    }

    public String get_admin_mobile() {
        return admin_mobile;
    }

    public void set_admin_mobile(String admin_mobile) {
        this.admin_mobile = admin_mobile;
    }

    public String get_alternate_email() {
        return alternate_email;
    }

    public void set_alternate_email(String alternate_email) {
        this.alternate_email = alternate_email;
    }*/
    
      public Boolean getShowResponse() {
        return showResponse;
    }

    public void setShowResponse(Boolean showResponse) {
        this.showResponse = showResponse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
