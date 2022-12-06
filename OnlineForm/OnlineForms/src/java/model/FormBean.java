package model;

import com.org.dao.DlistDao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nikki
 */
public class FormBean {

    DlistDao dlistDao = new DlistDao();
    String formid, new_min_dept;
    String nic_employee, module;
    //firewall 

    String sourceip, destinationip, service, ports, action, timeperiod;
    //email activate
    String act_email1;
    //email deactivate
    String deact_email1;

    String dor_email;

    String p_single_dor, fixed_dor_for_retired;
    //sms tab 1
    // String[] sms_service;
    String smsservice1;
    String pull_url, pull_keyword, s_code, short_code, app_name, app_url, sms_usage, server_loc, server_loc_txt, base_ip, service_ip;
    //sms tab 2
    String t_off_name, tdesignation, temp_code, taddrs, tcity, tstate, tpin, ttel_ofc, ttel_res, tmobile, tauth_email;
    //sms tab 3
    String bauth_off_name, bdesignation, bemp_code, baddrs, bcity, bstate, bpin, btel_ofc, btel_res, bmobile, bauth_email;
    //sms tab 4
    String audit, datepicker1, staging_ip, sender, sender_id, domestic_traf, inter_traf, imgtxt;
    //telnet tab
    String telnet;
    // firewall tab
    String firewall_id;
    // profile tab
    String user_name, user_mobile, user_email, user_ophone, user_rphone, user_design, user_empcode, user_address, user_city, user_state, user_alt_address;
    String user_pincode, user_employment, ca_name, ca_design, ca_mobile, ca_email, hod_name, hod_tel, hod_mobile, hod_email;
    String min, dept, state_dept, stateCode, org, other_dept, under_sec_email, under_sec_name, under_sec_mobile, under_sec_tel, under_sec_desig;

    // dns tab 2 ,3, 4
    String dns_domain, dns_url, dns_cname, dns_oldip, dns_newip, dns_loc, request_cname_chk, request_aaaa, request_mx, request_mx1, request_ptr, request_ptr1, request_srv, request_spf, request_txt, request_dmarc, request_dmarc_chk, request_aaaa_chk, request_mx_chk, request_ptr_chk, request_srv_chk, request_spf_chk, request_txt_chk, req_, old_url, old_ip, url, dns_user_empcode, migration_date;
    ;

    // wifi form
    String wifi_type, wifi_mac1, wifi_mac2, wifi_mac3, wifi_mac4, wifi_os1, wifi_os2, wifi_os3, wifi_os4, wifi_request, wifi_time, wifi_duration, wifi_process;
    String[] delete_mac_id;
    String[] cancel_mac_id;
    List<FormBean> mac_list = new ArrayList<>();
    // Webcast form
    //String event_name_eng,event_name_hin,event_date,event_type,telecast,channel_name,live_feed,vc_id,event_size,media_format,cheque_no,cheque_amount,cheque_date,bank_name,conf_name,conf_type,conf_city,conf_schedule,conf_session,conf_bw,conf_provider,conf_event_hired,conf_flash,conf_video,conf_contact,remarks,payment;
    String event_name_eng, event_name_hin, event_type, telecast, channel_name, live_feed, vc_id, event_size, media_format, cheque_no, cheque_amount, cheque_date, bank_name, conf_name, conf_type, conf_city, conf_schedule, conf_session, conf_bw, conf_provider, conf_event_hired, conf_flash, conf_video, conf_contact, remarks, payment, conf_radio, hall_type, hall_number;
    String event_start_date, event_end_date, local_setup, event_no, event_coo_name, event_coo_email, event_coo_design, event_coo_mobile, event_coo_address;
    Map webcast_uploaded_files;

    String device_type1;
    String device_type2;
    String device_type3;
    String device_type4;
    String req_other_add = "";
    int campaign_id = -1;
    String submittedAt = "";

    //
    private String nicDateOfBirth;
    private String nicDateOfRetirement;
    private String designation;
    private String displayName;

    int wifi_bo;

    //DaOnboarding 
    String boName;
    public String eligibility;
    public String daon_mobile;
    public List<String> da_vpn_reg_no;

    public String getFixed_dor_for_retired() {
        return fixed_dor_for_retired;
    }

    public void setFixed_dor_for_retired(String fixed_dor_for_retired) {
        this.fixed_dor_for_retired = fixed_dor_for_retired;
    }
    
    public String getDaon_mobile() {
        return daon_mobile;
    }

    public void setDaon_mobile(String daon_mobile) {
        this.daon_mobile = daon_mobile;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getBoName() {
        return boName;
    }

    public void setBoName(String boName) {
        this.boName = boName;
    }

    public String getWifi_mac4() {
        return wifi_mac4;
    }

    public void setWifi_mac4(String wifi_mac4) {
        this.wifi_mac4 = wifi_mac4;
    }

    public String getWifi_os4() {
        return wifi_os4;
    }

    public void setWifi_os4(String wifi_os4) {
        this.wifi_os4 = wifi_os4;
    }

    public String getDevice_type4() {
        return device_type4;
    }

    public void setDevice_type4(String device_type4) {
        this.device_type4 = device_type4;
    }

    public String getDor_email() {
        return dor_email;
    }

    public void setDor_email(String dor_email) {
        this.dor_email = dor_email;
    }

    public String getP_single_dor() {
        return p_single_dor;
    }

    public void setP_single_dor(String p_single_dor) {
        this.p_single_dor = p_single_dor;
    }

    public String getSecurity_device_type() {
        return security_device_type;
    }

    public void setSecurity_device_type(String security_device_type) {
        this.security_device_type = security_device_type;
    }

    public String getPoint_name() {
        return point_name;
    }

    public void setPoint_name(String point_name) {
        this.point_name = point_name;
    }

    public String getPoint_email() {
        return point_email;
    }

    public void setPoint_email(String point_email) {
        this.point_email = point_email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getLandline_number() {
        return landline_number;
    }

    public void setLandline_number(String landline_number) {
        this.landline_number = landline_number;
    }

    public String getSecurity_audit() {
        return security_audit;
    }

    public void setSecurity_audit(String security_audit) {
        this.security_audit = security_audit;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(String submittedAt) {
        this.submittedAt = submittedAt;
    }

    public int getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(int campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getDevice_type1() {
        return device_type1;
    }

    public String getReq_other_add() {
        return req_other_add;
    }

    public void setReq_other_add(String req_other_add) {
        this.req_other_add = req_other_add;
    }

    public void setDevice_type1(String device_type1) {
        this.device_type1 = device_type1;
    }

    public String getDevice_type2() {
        return device_type2;
    }

    public void setDevice_type2(String device_type2) {
        this.device_type2 = device_type2;
    }

    public String getDevice_type3() {
        return device_type3;
    }

    public void setDevice_type3(String device_type3) {
        this.device_type3 = device_type3;
    }

    public String[] getDelete_mac_id() {
        return delete_mac_id;
    }

    public void setDelete_mac_id(String[] delete_mac_id) {
        this.delete_mac_id = delete_mac_id;
    }

    public String[] getCancel_mac_id() {
        return cancel_mac_id;
    }

    public void setCancel_mac_id(String[] cancel_mac_id) {
        this.cancel_mac_id = cancel_mac_id;
    }

    public List<FormBean> getMac_list() {
        return mac_list;
    }

    public void setMac_list(List<FormBean> mac_list) {
        this.mac_list = mac_list;
    }

    public List<String> getDa_vpn_reg_no() {
        return da_vpn_reg_no;
    }

    public void setDa_vpn_reg_no(List<String> da_vpn_reg_no) {
        this.da_vpn_reg_no = da_vpn_reg_no;
    }

    // single form
    String single_dob, single_dor, single_id_type, single_email1, single_email2, single_emp_type, req_user_type;
    String applicant_single_dob, applicant_single_dor, applicant_single_id_type, applicant_single_email1, applicant_single_email2, applicant_single_emp_type, applicant_employment, applicant_Org, applicant_min, applicant_dept, applicant_stateCode, applicant_Smi, applicant_other_dept, applicant_req_for, applicant_name, applicant_empcode, applicant_mobile, applicant_email, applicant_design, applicant_state;

    // ldap form  [app_name,app_url,base_ip,service_ip,server_loc,server_loc_txt,audit used from sms tab]
    String domain, https, ldap_id1, ldap_id2;

    // for Imap POP protocal
    String protocol;
    //for new update mobile
    String radioOnBehalf, relationWithOwner, otherRelation, on_behalf_email, newcodeonbehalf, reason, reason_txt, initials, mname;

    //for Mobile change request
    String country_code, new_mobile, newcode, other_remarks;

    // for uploaded file name
    String uploaded_filename, renamed_filepath;

    // nkn form
    String inst_name, inst_id, nkn_project, request_type,nkn_id_type,nkn_req_for,nkn_bulk_id_type,nkn_bulk_req_for;

    public String getNkn_id_type() {
        return nkn_id_type;
    }

    public void setNkn_id_type(String nkn_id_type) {
        this.nkn_id_type = nkn_id_type;
    }

    public String getNkn_req_for() {
        return nkn_req_for;
    }

    public void setNkn_req_for(String nkn_req_for) {
        this.nkn_req_for = nkn_req_for;
    }

    public String getNkn_bulk_id_type() {
        return nkn_bulk_id_type;
    }

    public void setNkn_bulk_id_type(String nkn_bulk_id_type) {
        this.nkn_bulk_id_type = nkn_bulk_id_type;
    }

    public String getNkn_bulk_req_for() {
        return nkn_bulk_req_for;
    }

    public void setNkn_bulk_req_for(String nkn_bulk_req_for) {
        this.nkn_bulk_req_for = nkn_bulk_req_for;
    }

    // gem form
    String pse, pse_ministry, pse_state, pse_district, fwd_ofc_name, fwd_ofc_mobile, fwd_ofc_email, fwd_ofc_tel, fwd_ofc_design, fwd_ofc_add, primary_user, primary_user_id, role_assign;

    // for bulk users;
    String fname, lname, uid, mail, registration_no, acc_cat;

    // distribution-list start
    String list_name, description_list, list_mod, allowed_member, list_temp, validity_date, non_nicnet, memberCount;

    //distribution-list new for dlist bulk 
    String mail_Acceptance, owner_Name, Owner_Email, owner_Mobile,
            moderator_Name, moderator_Email, moderator_Mobile, owner_Admin, moderator_Admin;

    public String getMail_Acceptance() {
        return mail_Acceptance;
    }

    public void setMail_Acceptance(String mail_Acceptance) {
        this.mail_Acceptance = mail_Acceptance;
    }

    public String getOwner_Name() {
        return owner_Name;
    }

    public void setOwner_Name(String owner_Name) {
        this.owner_Name = owner_Name;
    }

    public String getOwner_Email() {
        return Owner_Email;
    }

    public void setOwner_Email(String Owner_Email) {
        this.Owner_Email = Owner_Email;
    }

    public String getOwner_Mobile() {
        return owner_Mobile;
    }

    public void setOwner_Mobile(String owner_Mobile) {
        this.owner_Mobile = owner_Mobile;
    }

    public String getModerator_Name() {
        return moderator_Name;
    }

    public void setModerator_Name(String moderator_Name) {
        this.moderator_Name = moderator_Name;
    }

    public String getModerator_Email() {
        return moderator_Email;
    }

    public void setModerator_Email(String moderator_Email) {
        this.moderator_Email = moderator_Email;
    }

    public String getModerator_Mobile() {
        return moderator_Mobile;
    }

    public void setModerator_Mobile(String moderator_Mobile) {
        this.moderator_Mobile = moderator_Mobile;
    }

    public String getOwner_Admin() {
        return owner_Admin;
    }

    public void setOwner_Admin(String owner_Admin) {
        this.owner_Admin = owner_Admin;
    }

    public String getModerator_Admin() {
        return moderator_Admin;
    }

    public void setModerator_Admin(String moderator_Admin) {
        this.moderator_Admin = moderator_Admin;
    }

    //for change ip
    String req_for, account_name, add_ip1, add_ip2, add_ip3, add_ip4, old_ip1, new_ip1, old_ip2, new_ip2, old_ip3, new_ip3, old_ip4, new_ip4, ip_type, relay_app, relay_old_ip, ldap_account_name, ldap_url, ldap_auth_allocate, mailsent;
    ;

    
     //for Relay
    String relay_ip, relay_app_name, division, os, relay_server_loc, ip_staging, relay_uploaded_filename, relay_renamed_filepath, relay_app_url, relay_sender_id, domain_mx, smtp_port, spf, dkim, dmarc, relay_auth_id, old_relay_ip, otp_mail_service, mail_type_trans_mail, mail_type_reg_mail, mail_type_forgotpass, mail_type_alert, mail_type_other, hardware_uploaded_filename, hardware_renamed_filepath;
    String security_device_type, point_name, point_contact, point_email, mobile_number, landline_number, security_audit, mail_type, other_mail_type, security_exp_date, is_hosted;

    public String getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }

    // VPN form
    String dest_port;

    // VPN form
    String[] vpn_ip_type, vpn_new_ip1, vpn_new_ip2, vpn_new_ip3, vpn_app_url, vpn_dest_port, vpn_server_loc, vpn_server_loc_txt;
    String vpn_coo, vpn_coo_email, vpn_coo1, req_co, vpn_reg_no;

    //action type for all forms
    String action_type;

    // start, code added by pr on 10thjan18 to prevent CSRF 
    String CSRFRandom;

    Map vpn_data = new HashMap();

    public String getOther_mail_type() {
        return other_mail_type;
    }

    public void setOther_mail_type(String other_mail_type) {
        this.other_mail_type = other_mail_type;
    }

    public String getSecurity_exp_date() {
        return security_exp_date;
    }

    public void setSecurity_exp_date(String security_exp_date) {
        this.security_exp_date = security_exp_date;
    }

    public String getPoint_contact() {
        return point_contact;
    }

    public void setPoint_contact(String point_contact) {
        this.point_contact = point_contact;
    }

    public String getMail_type() {
        return mail_type;
    }

    public void setMail_type(String mail_type) {
        this.mail_type = mail_type;
    }

    public String getRelay_app_url() {
        return relay_app_url;
    }

    public void setRelay_app_url(String relay_app_url) {
        this.relay_app_url = relay_app_url;
    }

    public String getRelay_sender_id() {
        return relay_sender_id;
    }

    public void setRelay_sender_id(String relay_sender_id) {
        this.relay_sender_id = relay_sender_id;
    }

    public String getDomain_mx() {
        return domain_mx;
    }

    public void setDomain_mx(String domain_mx) {
        this.domain_mx = domain_mx;
    }

    public String getSmtp_port() {
        return smtp_port;
    }

    public void setSmtp_port(String smtp_port) {
        this.smtp_port = smtp_port;
    }

    public String getSpf() {
        return spf;
    }

    public void setSpf(String spf) {
        this.spf = spf;
    }

    public String getDkim() {
        return dkim;
    }

    public void setDkim(String dkim) {
        this.dkim = dkim;
    }

    public String getDmarc() {
        return dmarc;
    }

    public void setDmarc(String dmarc) {
        this.dmarc = dmarc;
    }

    public String getRelay_auth_id() {
        return relay_auth_id;
    }

    public void setRelay_auth_id(String relay_auth_id) {
        this.relay_auth_id = relay_auth_id;
    }

    public String getOld_relay_ip() {
        return old_relay_ip;
    }

    public void setOld_relay_ip(String old_relay_ip) {
        this.old_relay_ip = old_relay_ip;
    }

    public String getOtp_mail_service() {
        return otp_mail_service;
    }

    public void setOtp_mail_service(String otp_mail_service) {
        this.otp_mail_service = otp_mail_service;
    }

    public String getMail_type_trans_mail() {
        return mail_type_trans_mail;
    }

    public void setMail_type_trans_mail(String mail_type_trans_mail) {
        this.mail_type_trans_mail = mail_type_trans_mail;
    }

    public String getMail_type_reg_mail() {
        return mail_type_reg_mail;
    }

    public void setMail_type_reg_mail(String mail_type_reg_mail) {
        this.mail_type_reg_mail = mail_type_reg_mail;
    }

    public String getMail_type_forgotpass() {
        return mail_type_forgotpass;
    }

    public void setMail_type_forgotpass(String mail_type_forgotpass) {
        this.mail_type_forgotpass = mail_type_forgotpass;
    }

    public String getMail_type_alert() {
        return mail_type_alert;
    }

    public void setMail_type_alert(String mail_type_alert) {
        this.mail_type_alert = mail_type_alert;
    }

    public String getMail_type_other() {
        return mail_type_other;
    }

    public void setMail_type_other(String mail_type_other) {
        this.mail_type_other = mail_type_other;
    }

    public String getHardware_uploaded_filename() {
        return hardware_uploaded_filename;
    }

    public void setHardware_uploaded_filename(String hardware_uploaded_filename) {
        this.hardware_uploaded_filename = hardware_uploaded_filename;
    }

    public String getHardware_renamed_filepath() {
        return hardware_renamed_filepath;
    }

    public void setHardware_renamed_filepath(String hardware_renamed_filepath) {
        this.hardware_renamed_filepath = hardware_renamed_filepath;
    }

    public String getNic_employee() {
        return nic_employee;
    }

    public void setNic_employee(String nic_employee) {
        this.nic_employee = nic_employee;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;
    }

    // end, code added by pr on 10thjan18
    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getSmsservice1() {
        return smsservice1;
    }

    public void setSmsservice1(String smsservice1) {
        this.smsservice1 = smsservice1;
    }

    public String getPull_url() {
        return pull_url;
    }

    public void setPull_url(String pull_url) {
        this.pull_url = pull_url;
    }

    public String getPull_keyword() {
        return pull_keyword;
    }

    public void setPull_keyword(String pull_keyword) {
        this.pull_keyword = pull_keyword;
    }

    public String getS_code() {
        return s_code;
    }

    public void setS_code(String s_code) {
        this.s_code = s_code;
    }

    public String getShort_code() {
        return short_code;
    }

    public void setShort_code(String short_code) {
        this.short_code = short_code;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }

    public String getSms_usage() {
        return sms_usage;
    }

    public void setSms_usage(String sms_usage) {
        this.sms_usage = sms_usage;
    }

    public String getServer_loc() {
        return server_loc;
    }

    public void setServer_loc(String server_loc) {
        this.server_loc = server_loc;
    }

    public String getServer_loc_txt() {
        return server_loc_txt;
    }

    public void setServer_loc_txt(String server_loc_txt) {
        this.server_loc_txt = server_loc_txt;
    }

    public String getBase_ip() {
        return base_ip;
    }

    public void setBase_ip(String base_ip) {
        this.base_ip = base_ip;
    }

    public String getService_ip() {
        return service_ip;
    }

    public void setService_ip(String service_ip) {
        this.service_ip = service_ip;
    }

    public String getT_off_name() {
        return t_off_name;
    }

    public void setT_off_name(String t_off_name) {
        this.t_off_name = t_off_name;
    }

    public String getTdesignation() {
        return tdesignation;
    }

    public void setTdesignation(String tdesignation) {
        this.tdesignation = tdesignation;
    }

    public String getTemp_code() {
        return temp_code;
    }

    public void setTemp_code(String temp_code) {
        this.temp_code = temp_code;
    }

    public String getTaddrs() {
        return taddrs;
    }

    public void setTaddrs(String taddrs) {
        this.taddrs = taddrs;
    }

    public String getTcity() {
        return tcity;
    }

    public void setTcity(String tcity) {
        this.tcity = tcity;
    }

    public String getTstate() {
        return tstate;
    }

    public void setTstate(String tstate) {
        this.tstate = tstate;
    }

    public String getTpin() {
        return tpin;
    }

    public void setTpin(String tpin) {
        this.tpin = tpin;
    }

    public String getTtel_ofc() {
        return ttel_ofc;
    }

    public void setTtel_ofc(String ttel_ofc) {
        this.ttel_ofc = ttel_ofc;
    }

    public String getTtel_res() {
        return ttel_res;
    }

    public void setTtel_res(String ttel_res) {
        this.ttel_res = ttel_res;
    }

    public String getTmobile() {
        return tmobile;
    }

    public void setTmobile(String tmobile) {
        this.tmobile = tmobile;
    }

    public String getTauth_email() {
        return tauth_email;
    }

    public void setTauth_email(String tauth_email) {
        this.tauth_email = tauth_email;
    }

    public String getBauth_off_name() {
        return bauth_off_name;
    }

    public void setBauth_off_name(String bauth_off_name) {
        this.bauth_off_name = bauth_off_name;
    }

    public String getBdesignation() {
        return bdesignation;
    }

    public void setBdesignation(String bdesignation) {
        this.bdesignation = bdesignation;
    }

    public String getBemp_code() {
        return bemp_code;
    }

    public void setBemp_code(String bemp_code) {
        this.bemp_code = bemp_code;
    }

    public String getBaddrs() {
        return baddrs;
    }

    public void setBaddrs(String baddrs) {
        this.baddrs = baddrs;
    }

    public String getBcity() {
        return bcity;
    }

    public void setBcity(String bcity) {
        this.bcity = bcity;
    }

    public String getBstate() {
        return bstate;
    }

    public void setBstate(String bstate) {
        this.bstate = bstate;
    }

    public String getBpin() {
        return bpin;
    }

    public void setBpin(String bpin) {
        this.bpin = bpin;
    }

    public String getBtel_ofc() {
        return btel_ofc;
    }

    public void setBtel_ofc(String btel_ofc) {
        this.btel_ofc = btel_ofc;
    }

    public String getBtel_res() {
        return btel_res;
    }

    public void setBtel_res(String btel_res) {
        this.btel_res = btel_res;
    }

    public String getBmobile() {
        return bmobile;
    }

    public void setBmobile(String bmobile) {
        this.bmobile = bmobile;
    }

    public String getBauth_email() {
        return bauth_email;
    }

    public void setBauth_email(String bauth_email) {
        this.bauth_email = bauth_email;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getDatepicker1() {
        return datepicker1;
    }

    public void setDatepicker1(String datepicker1) {
        this.datepicker1 = datepicker1;
    }

    public String getStaging_ip() {
        return staging_ip;
    }

    public void setStaging_ip(String staging_ip) {
        this.staging_ip = staging_ip;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getDomestic_traf() {
        return domestic_traf;
    }

    public void setDomestic_traf(String domestic_traf) {
        this.domestic_traf = domestic_traf;
    }

    public String getInter_traf() {
        return inter_traf;
    }

    public void setInter_traf(String inter_traf) {
        this.inter_traf = inter_traf;
    }

    public String getImgtxt() {
        return imgtxt;
    }

    public void setImgtxt(String imgtxt) {
        this.imgtxt = imgtxt;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_ophone() {
        return user_ophone;
    }

    public void setUser_ophone(String user_ophone) {
        this.user_ophone = user_ophone;
    }

    public String getUser_rphone() {
        return user_rphone;
    }

    public void setUser_rphone(String user_rphone) {
        this.user_rphone = user_rphone;
    }

    public String getUser_design() {
        return user_design;
    }

    public void setUser_design(String user_design) {
        this.user_design = user_design;
    }

    public String getUser_empcode() {
        return user_empcode;
    }

    public void setUser_empcode(String user_empcode) {
        this.user_empcode = user_empcode;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getUser_pincode() {
        return user_pincode;
    }

    public void setUser_pincode(String user_pincode) {
        this.user_pincode = user_pincode;
    }

    public String getUser_employment() {
        return user_employment;
    }

    public void setUser_employment(String user_employment) {
        this.user_employment = user_employment;
    }

    public String getCa_name() {
        return ca_name;
    }

    public void setCa_name(String ca_name) {
        this.ca_name = ca_name;
    }

    public String getCa_design() {
        return ca_design;
    }

    public void setCa_design(String ca_design) {
        this.ca_design = ca_design;
    }

    public String getCa_mobile() {
        return ca_mobile;
    }

    public void setCa_mobile(String ca_mobile) {
        this.ca_mobile = ca_mobile;
    }

    public String getCa_email() {
        return ca_email;
    }

    public void setCa_email(String ca_email) {
        this.ca_email = ca_email;
    }

    public String getHod_name() {
        return hod_name;
    }

    public void setHod_name(String hod_name) {
        this.hod_name = hod_name;
    }

    public String getHod_tel() {
        return hod_tel;
    }

    public void setHod_tel(String hod_tel) {
        this.hod_tel = hod_tel;
    }

    public String getHod_mobile() {
        return hod_mobile;
    }

    public void setHod_mobile(String hod_mobile) {
        this.hod_mobile = hod_mobile;
    }

    public String getHod_email() {
        return hod_email;
    }

    public void setHod_email(String hod_email) {
        this.hod_email = hod_email;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getState_dept() {
        return state_dept;
    }

    public void setState_dept(String state_dept) {
        this.state_dept = state_dept;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOther_dept() {
        return other_dept;
    }

    public void setOther_dept(String other_dept) {
        this.other_dept = other_dept;
    }

    public String getTelnet() {
        return telnet;
    }

    public void setTelnet(String telnet) {
        this.telnet = telnet;
    }

    public String getFirewall_id() {
        return firewall_id;
    }

    public void setFirewall_id(String firewall_id) {
        this.firewall_id = firewall_id;
    }

    public String getDns_domain() {
        return dns_domain;
    }

    public void setDns_domain(String dns_domain) {
        this.dns_domain = dns_domain;
    }

    public String getDns_url() {
        return dns_url;
    }

    public void setDns_url(String dns_url) {
        this.dns_url = dns_url;
    }

    public String getDns_loc() {
        return dns_loc;
    }

    public void setDns_loc(String dns_loc) {
        this.dns_loc = dns_loc;
    }

    public String getDns_cname() {
        return dns_cname;
    }

    public void setDns_cname(String dns_cname) {
        this.dns_cname = dns_cname;
    }

    public String getDns_oldip() {
        return dns_oldip;
    }

    public void setDns_oldip(String dns_oldip) {
        this.dns_oldip = dns_oldip;
    }

    public String getDns_newip() {
        return dns_newip;
    }

    public void setDns_newip(String dns_newip) {
        this.dns_newip = dns_newip;
    }

    public String getRequest_mx1() {
        return request_mx1;
    }

    public void setRequest_mx1(String request_mx1) {
        this.request_mx1 = request_mx1;
    }

    public String getRequest_ptr1() {
        return request_ptr1;
    }

    public void setRequest_ptr1(String request_ptr1) {
        this.request_ptr1 = request_ptr1;
    }

    public String getRequest_aaaa() {
        return request_aaaa;
    }

    public void setRequest_aaaa(String request_aaaa) {
        this.request_aaaa = request_aaaa;
    }

    public String getRequest_mx() {
        return request_mx;
    }

    public void setRequest_mx(String request_mx) {
        this.request_mx = request_mx;
    }

    public String getRequest_ptr() {
        return request_ptr;
    }

    public void setRequest_ptr(String request_ptr) {
        this.request_ptr = request_ptr;
    }

    public String getRequest_srv() {
        return request_srv;
    }

    public void setRequest_srv(String request_srv) {
        this.request_srv = request_srv;
    }

    public String getRequest_spf() {
        return request_spf;
    }

    public void setRequest_spf(String request_spf) {
        this.request_spf = request_spf;
    }

    public String getRequest_txt() {
        return request_txt;
    }

    public void setRequest_txt(String request_txt) {
        this.request_txt = request_txt;
    }

    public String getWifi_type() {
        return wifi_type;
    }

    public void setWifi_type(String wifi_type) {
        this.wifi_type = wifi_type;
    }

    public String getWifi_mac1() {
        return wifi_mac1;
    }

    public void setWifi_mac1(String wifi_mac1) {
        this.wifi_mac1 = wifi_mac1;
    }

    public String getWifi_mac2() {
        return wifi_mac2;
    }

    public void setWifi_mac2(String wifi_mac2) {
        this.wifi_mac2 = wifi_mac2;
    }

    public String getWifi_mac3() {
        return wifi_mac3;
    }

    public void setWifi_mac3(String wifi_mac3) {
        this.wifi_mac3 = wifi_mac3;
    }

    public String getWifi_os1() {
        return wifi_os1;
    }

    public void setWifi_os1(String wifi_os1) {
        this.wifi_os1 = wifi_os1;
    }

    public String getWifi_os2() {
        return wifi_os2;
    }

    public void setWifi_os2(String wifi_os2) {
        this.wifi_os2 = wifi_os2;
    }

    public String getWifi_os3() {
        return wifi_os3;
    }

    public void setWifi_os3(String wifi_os3) {
        this.wifi_os3 = wifi_os3;
    }

    public String getWifi_request() {
        return wifi_request;
    }

    public void setWifi_request(String wifi_request) {
        this.wifi_request = wifi_request;
    }

    public String getWifi_time() {
        return wifi_time;
    }

    public void setWifi_time(String wifi_time) {
        this.wifi_time = wifi_time;
    }

    public String getWifi_duration() {
        return wifi_duration;
    }

    public void setWifi_duration(String wifi_duration) {
        this.wifi_duration = wifi_duration;
    }

    public String getSingle_dob() {
        return single_dob;
    }

    public void setSingle_dob(String single_dob) {
        this.single_dob = single_dob;
    }

    public String getSingle_dor() {
        return single_dor;
    }

    public void setSingle_dor(String single_dor) {
        this.single_dor = single_dor;
    }

    public String getSingle_id_type() {
        return single_id_type;
    }

    public void setSingle_id_type(String single_id_type) {
        this.single_id_type = single_id_type;
    }

    public String getSingle_email1() {
        return single_email1;
    }

    public void setSingle_email1(String single_email1) {
        this.single_email1 = single_email1;
    }

    public String getSingle_email2() {
        return single_email2;
    }

    public void setSingle_email2(String single_email2) {
        this.single_email2 = single_email2;
    }

    public String getSingle_emp_type() {
        return single_emp_type;
    }

    public void setSingle_emp_type(String single_emp_type) {
        this.single_emp_type = single_emp_type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHttps() {
        return https;
    }

    public void setHttps(String https) {
        this.https = https;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getNew_mobile() {
        return new_mobile;
    }

    public void setNew_mobile(String new_mobile) {
        this.new_mobile = new_mobile;
    }

    public String getRequest_aaaa_chk() {
        return request_aaaa_chk;
    }

    public void setRequest_aaaa_chk(String request_aaaa_chk) {
        this.request_aaaa_chk = request_aaaa_chk;
    }

    public String getRequest_mx_chk() {
        return request_mx_chk;
    }

    public void setRequest_mx_chk(String request_mx_chk) {
        this.request_mx_chk = request_mx_chk;
    }

    public String getRequest_ptr_chk() {
        return request_ptr_chk;
    }

    public void setRequest_ptr_chk(String request_ptr_chk) {
        this.request_ptr_chk = request_ptr_chk;
    }

    public String getRequest_srv_chk() {
        return request_srv_chk;
    }

    public void setRequest_srv_chk(String request_srv_chk) {
        this.request_srv_chk = request_srv_chk;
    }

    public String getRequest_spf_chk() {
        return request_spf_chk;
    }

    public void setRequest_spf_chk(String request_spf_chk) {
        this.request_spf_chk = request_spf_chk;
    }

    public String getRequest_txt_chk() {
        return request_txt_chk;
    }

    public void setRequest_txt_chk(String request_txt_chk) {
        this.request_txt_chk = request_txt_chk;
    }

    public String getLdap_id1() {
        return ldap_id1;
    }

    public void setLdap_id1(String ldap_id1) {
        this.ldap_id1 = ldap_id1;
    }

    public String getLdap_id2() {
        return ldap_id2;
    }

    public void setLdap_id2(String ldap_id2) {
        this.ldap_id2 = ldap_id2;
    }

    public String getUploaded_filename() {
        return uploaded_filename;
    }

    public void setUploaded_filename(String uploaded_filename) {
        this.uploaded_filename = uploaded_filename;
    }

    public String getRenamed_filepath() {
        return renamed_filepath;
    }

    public void setRenamed_filepath(String renamed_filepath) {
        this.renamed_filepath = renamed_filepath;
    }

    public String getInst_name() {
        return inst_name;
    }

    public void setInst_name(String inst_name) {
        this.inst_name = inst_name;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getNkn_project() {
        return nkn_project;
    }

    public void setNkn_project(String nkn_project) {
        this.nkn_project = nkn_project;
    }

    public String getPse() {
        return pse;
    }

    public void setPse(String pse) {
        this.pse = pse;
    }

    public String getPse_ministry() {
        return pse_ministry;
    }

    public void setPse_ministry(String pse_ministry) {
        this.pse_ministry = pse_ministry;
    }

    public String getPse_state() {
        return pse_state;
    }

    public void setPse_state(String pse_state) {
        this.pse_state = pse_state;
    }

    public String getPse_district() {
        return pse_district;
    }

    public void setPse_district(String pse_district) {
        this.pse_district = pse_district;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }

    public String getDescription_list() {
        return description_list;
    }

    public void setDescription_list(String description_list) {
        this.description_list = description_list;
    }

    public String getList_mod() {
        return list_mod;
    }

    public void setList_mod(String list_mod) {
        this.list_mod = list_mod;
    }

    public String getAllowed_member() {
        return allowed_member;
    }

    public void setAllowed_member(String allowed_member) {
        this.allowed_member = allowed_member;
    }

    public String getList_temp() {
        return list_temp;
    }

    public void setList_temp(String list_temp) {
        this.list_temp = list_temp;
    }

    public String getValidity_date() {
        return validity_date;
    }

    public void setValidity_date(String validity_date) {
        this.validity_date = validity_date;
    }

    public String getNon_nicnet() {
        return non_nicnet;
    }

    public void setNon_nicnet(String non_nicnet) {
        this.non_nicnet = non_nicnet;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getAcc_cat() {
        return acc_cat;
    }

    public void setAcc_cat(String acc_cat) {
        this.acc_cat = acc_cat;
    }

    public String getReq_for() {
        return req_for;
    }

    public void setReq_for(String req_for) {
        this.req_for = req_for;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAdd_ip1() {
        return add_ip1;
    }

    public void setAdd_ip1(String add_ip1) {
        this.add_ip1 = add_ip1;
    }

    public String getAdd_ip2() {
        return add_ip2;
    }

    public void setAdd_ip2(String add_ip2) {
        this.add_ip2 = add_ip2;
    }

    public String getAdd_ip3() {
        return add_ip3;
    }

    public void setAdd_ip3(String add_ip3) {
        this.add_ip3 = add_ip3;
    }

    public String getAdd_ip4() {
        return add_ip4;
    }

    public void setAdd_ip4(String add_ip4) {
        this.add_ip4 = add_ip4;
    }

    public String getOld_ip1() {
        return old_ip1;
    }

    public void setOld_ip1(String old_ip1) {
        this.old_ip1 = old_ip1;
    }

    public String getNew_ip1() {
        return new_ip1;
    }

    public void setNew_ip1(String new_ip1) {
        this.new_ip1 = new_ip1;
    }

    public String getOld_ip2() {
        return old_ip2;
    }

    public void setOld_ip2(String old_ip2) {
        this.old_ip2 = old_ip2;
    }

    public String getNew_ip2() {
        return new_ip2;
    }

    public void setNew_ip2(String new_ip2) {
        this.new_ip2 = new_ip2;
    }

    public String getOld_ip3() {
        return old_ip3;
    }

    public void setOld_ip3(String old_ip3) {
        this.old_ip3 = old_ip3;
    }

    public String getNew_ip3() {
        return new_ip3;
    }

    public void setNew_ip3(String new_ip3) {
        this.new_ip3 = new_ip3;
    }

    public String getOld_ip4() {
        return old_ip4;
    }

    public void setOld_ip4(String old_ip4) {
        this.old_ip4 = old_ip4;
    }

    public String getNew_ip4() {
        return new_ip4;
    }

    public void setNew_ip4(String new_ip4) {
        this.new_ip4 = new_ip4;
    }

    public String getIp_type() {
        return ip_type;
    }

    public void setIp_type(String ip_type) {
        this.ip_type = ip_type;
    }

    public String getRelay_app() {
        return relay_app;
    }

    public void setRelay_app(String relay_app) {
        this.relay_app = relay_app;
    }

    public String getRelay_old_ip() {
        return relay_old_ip;
    }

    public void setRelay_old_ip(String relay_old_ip) {
        this.relay_old_ip = relay_old_ip;
    }

    public String getRelay_ip() {
        return relay_ip;
    }

    public void setRelay_ip(String relay_ip) {
        this.relay_ip = relay_ip;
    }

    public String getRelay_app_name() {
        return relay_app_name;
    }

    public void setRelay_app_name(String relay_app_name) {
        this.relay_app_name = relay_app_name;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getRelay_server_loc() {
        return relay_server_loc;
    }

    public void setRelay_server_loc(String relay_server_loc) {
        this.relay_server_loc = relay_server_loc;
    }

    public String getIp_staging() {
        return ip_staging;
    }

    public void setIp_staging(String ip_staging) {
        this.ip_staging = ip_staging;
    }

    public String getRelay_uploaded_filename() {
        return relay_uploaded_filename;
    }

    public void setRelay_uploaded_filename(String relay_uploaded_filename) {
        this.relay_uploaded_filename = relay_uploaded_filename;
    }

    public String getRelay_renamed_filepath() {
        return relay_renamed_filepath;
    }

    public void setRelay_renamed_filepath(String relay_renamed_filepath) {
        this.relay_renamed_filepath = relay_renamed_filepath;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getDest_port() {
        return dest_port;
    }

    public void setDest_port(String dest_port) {
        this.dest_port = dest_port;
    }

    public String getRequest_cname_chk() {
        return request_cname_chk;
    }

    public void setRequest_cname_chk(String request_cname_chk) {
        this.request_cname_chk = request_cname_chk;
    }

    public void setRequest_dmarc(String request_dmarc) {
        this.request_dmarc = request_dmarc;
    }

    public void setRequest_dmarc_chk(String request_dmarc_chk) {
        this.request_dmarc_chk = request_dmarc_chk;
    }

    public String getRequest_dmarc() {
        return request_dmarc;
    }

    public String getRequest_dmarc_chk() {
        return request_dmarc_chk;
    }

    public String getReq_() {
        return req_;
    }

    public void setReq_(String req_) {
        this.req_ = req_;
    }

    public String getOld_url() {
        return old_url;
    }

    public void setOld_url(String old_url) {
        this.old_url = old_url;
    }

    public String getOld_ip() {
        return old_ip;
    }

    public void setOld_ip(String old_ip) {
        this.old_ip = old_ip;
    }

    public String getFwd_ofc_name() {
        return fwd_ofc_name;
    }

    public void setFwd_ofc_name(String fwd_ofc_name) {
        this.fwd_ofc_name = fwd_ofc_name;
    }

    public String getFwd_ofc_mobile() {
        return fwd_ofc_mobile;
    }

    public void setFwd_ofc_mobile(String fwd_ofc_mobile) {
        this.fwd_ofc_mobile = fwd_ofc_mobile;
    }

    public String getFwd_ofc_email() {
        return fwd_ofc_email;
    }

    public void setFwd_ofc_email(String fwd_ofc_email) {
        this.fwd_ofc_email = fwd_ofc_email;
    }

    public String getFwd_ofc_tel() {
        return fwd_ofc_tel;
    }

    public void setFwd_ofc_tel(String fwd_ofc_tel) {
        this.fwd_ofc_tel = fwd_ofc_tel;
    }

    public String getFwd_ofc_design() {
        return fwd_ofc_design;
    }

    public void setFwd_ofc_design(String fwd_ofc_design) {
        this.fwd_ofc_design = fwd_ofc_design;
    }

    public String getFwd_ofc_add() {
        return fwd_ofc_add;
    }

    public void setFwd_ofc_add(String fwd_ofc_add) {
        this.fwd_ofc_add = fwd_ofc_add;
    }

    public String getPrimary_user() {
        return primary_user;
    }

    public void setPrimary_user(String primary_user) {
        this.primary_user = primary_user;
    }

    public String getPrimary_user_id() {
        return primary_user_id;
    }

    public void setPrimary_user_id(String primary_user_id) {
        this.primary_user_id = primary_user_id;
    }

    public String getRole_assign() {
        return role_assign;
    }

    public void setRole_assign(String role_assign) {
        this.role_assign = role_assign;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser_alt_address() {
        return user_alt_address;
    }

    public void setUser_alt_address(String user_alt_address) {
        this.user_alt_address = user_alt_address;
    }

    public String getDns_user_empcode() {
        return dns_user_empcode;
    }

    public void setDns_user_empcode(String dns_user_empcode) {
        this.dns_user_empcode = dns_user_empcode;
    }

    public String getWifi_process() {
        return wifi_process;
    }

    public void setWifi_process(String wifi_process) {
        this.wifi_process = wifi_process;
    }

    public int getWifi_bo() {
        return wifi_bo;
    }

    public void setWifi_bo(int wifi_bo) {
        this.wifi_bo = wifi_bo;
    }

    public String getUnder_sec_email() {
        return under_sec_email;
    }

    public void setUnder_sec_email(String under_sec_email) {
        this.under_sec_email = under_sec_email;
    }

    public String getUnder_sec_name() {
        return under_sec_name;
    }

    public void setUnder_sec_name(String under_sec_name) {
        this.under_sec_name = under_sec_name;
    }

    public String getUnder_sec_mobile() {
        return under_sec_mobile;
    }

    public void setUnder_sec_mobile(String under_sec_mobile) {
        this.under_sec_mobile = under_sec_mobile;
    }

    public String getUnder_sec_tel() {
        return under_sec_tel;
    }

    public void setUnder_sec_tel(String under_sec_tel) {
        this.under_sec_tel = under_sec_tel;
    }

    public String getunder_sec_desig() {
        return under_sec_desig;
    }

    public void setunder_sec_desig(String under_sec_desig) {
        this.under_sec_desig = under_sec_desig;
    }

    public String getLdap_account_name() {
        return ldap_account_name;
    }

    public void setLdap_account_name(String ldap_account_name) {
        this.ldap_account_name = ldap_account_name;
    }

    public String getLdap_url() {
        return ldap_url;
    }

    public void setLdap_url(String ldap_url) {
        this.ldap_url = ldap_url;
    }

    public String getLdap_auth_allocate() {
        return ldap_auth_allocate;
    }

    public void setLdap_auth_allocate(String ldap_auth_allocate) {
        this.ldap_auth_allocate = ldap_auth_allocate;
    }

    public String[] getVpn_ip_type() {
        return vpn_ip_type;
    }

    public void setVpn_ip_type(String[] vpn_ip_type) {
        this.vpn_ip_type = vpn_ip_type;
    }

    public String[] getVpn_new_ip1() {
        return vpn_new_ip1;
    }

    public void setVpn_new_ip1(String[] vpn_new_ip1) {
        this.vpn_new_ip1 = vpn_new_ip1;
    }

    public String[] getVpn_new_ip2() {
        return vpn_new_ip2;
    }

    public void setVpn_new_ip2(String[] vpn_new_ip2) {
        this.vpn_new_ip2 = vpn_new_ip2;
    }

    public String[] getVpn_new_ip3() {
        return vpn_new_ip3;
    }

    public void setVpn_new_ip3(String[] vpn_new_ip3) {
        this.vpn_new_ip3 = vpn_new_ip3;
    }

    public String[] getVpn_app_url() {
        return vpn_app_url;
    }

    public void setVpn_app_url(String[] vpn_app_url) {
        this.vpn_app_url = vpn_app_url;
    }

    public String[] getVpn_dest_port() {
        return vpn_dest_port;
    }

    public void setVpn_dest_port(String[] vpn_dest_port) {
        this.vpn_dest_port = vpn_dest_port;
    }

    public String[] getVpn_server_loc() {
        return vpn_server_loc;
    }

    public void setVpn_server_loc(String[] vpn_server_loc) {
        this.vpn_server_loc = vpn_server_loc;
    }

    public String[] getVpn_server_loc_txt() {
        return vpn_server_loc_txt;
    }

    public void setVpn_server_loc_txt(String[] vpn_server_loc_txt) {
        this.vpn_server_loc_txt = vpn_server_loc_txt;
    }

    public String getVpn_reg_no() {
        return vpn_reg_no;
    }

    public void setVpn_reg_no(String vpn_reg_no) {
        this.vpn_reg_no = vpn_reg_no;
    }

    public Map getVpn_data() {
        return vpn_data;
    }

    public void setVpn_data(Map vpn_data) {
        this.vpn_data = vpn_data;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getVpn_coo() {
        return vpn_coo;
    }

    public void setVpn_coo(String vpn_coo) {
        this.vpn_coo = vpn_coo;
    }

    public String getSourceip() {
        return sourceip;
    }

    public void setSourceip(String sourceip) {
        this.sourceip = sourceip;
    }

    public String getDestinationip() {
        return destinationip;
    }

    public void setDestinationip(String destinationip) {
        this.destinationip = destinationip;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPorts() {
        return ports;
    }

    public void setPorts(String ports) {
        this.ports = ports;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimeperiod() {
        return timeperiod;
    }

    public void setTimeperiod(String timeperiod) {
        this.timeperiod = timeperiod;
    }

    public String getEvent_name_eng() {
        return event_name_eng;
    }

    public void setEvent_name_eng(String event_name_eng) {
        this.event_name_eng = event_name_eng;
    }

    public String getEvent_name_hin() {
        return event_name_hin;
    }

    public void setEvent_name_hin(String event_name_hin) {
        this.event_name_hin = event_name_hin;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getTelecast() {
        return telecast;
    }

    public void setTelecast(String telecast) {
        this.telecast = telecast;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getLive_feed() {
        return live_feed;
    }

    public void setLive_feed(String live_feed) {
        this.live_feed = live_feed;
    }

    public String getVc_id() {
        return vc_id;
    }

    public void setVc_id(String vc_id) {
        this.vc_id = vc_id;
    }

    public String getEvent_size() {
        return event_size;
    }

    public void setEvent_size(String event_size) {
        this.event_size = event_size;
    }

    public String getMedia_format() {
        return media_format;
    }

    public void setMedia_format(String media_format) {
        this.media_format = media_format;
    }

    public String getCheque_no() {
        return cheque_no;
    }

    public void setCheque_no(String cheque_no) {
        this.cheque_no = cheque_no;
    }

    public String getCheque_amount() {
        return cheque_amount;
    }

    public void setCheque_amount(String cheque_amount) {
        this.cheque_amount = cheque_amount;
    }

    public String getCheque_date() {
        return cheque_date;
    }

    public void setCheque_date(String cheque_date) {
        this.cheque_date = cheque_date;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getConf_name() {
        return conf_name;
    }

    public void setConf_name(String conf_name) {
        this.conf_name = conf_name;
    }

    public String getConf_type() {
        return conf_type;
    }

    public void setConf_type(String conf_type) {
        this.conf_type = conf_type;
    }

    public String getConf_city() {
        return conf_city;
    }

    public void setConf_city(String conf_city) {
        this.conf_city = conf_city;
    }

    public String getConf_schedule() {
        return conf_schedule;
    }

    public void setConf_schedule(String conf_schedule) {
        this.conf_schedule = conf_schedule;
    }

    public String getConf_session() {
        return conf_session;
    }

    public void setConf_session(String conf_session) {
        this.conf_session = conf_session;
    }

    public String getConf_bw() {
        return conf_bw;
    }

    public void setConf_bw(String conf_bw) {
        this.conf_bw = conf_bw;
    }

    public String getConf_provider() {
        return conf_provider;
    }

    public void setConf_provider(String conf_provider) {
        this.conf_provider = conf_provider;
    }

    public String getConf_event_hired() {
        return conf_event_hired;
    }

    public void setConf_event_hired(String conf_event_hired) {
        this.conf_event_hired = conf_event_hired;
    }

    public String getConf_flash() {
        return conf_flash;
    }

    public void setConf_flash(String conf_flash) {
        this.conf_flash = conf_flash;
    }

    public String getConf_video() {
        return conf_video;
    }

    public void setConf_video(String conf_video) {
        this.conf_video = conf_video;
    }

    public String getConf_contact() {
        return conf_contact;
    }

    public void setConf_contact(String conf_contact) {
        this.conf_contact = conf_contact;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getConf_radio() {
        return conf_radio;
    }

    public void setConf_radio(String conf_radio) {
        this.conf_radio = conf_radio;
    }

    public String getHall_type() {
        return hall_type;
    }

    public void setHall_type(String hall_type) {
        this.hall_type = hall_type;
    }

    public String getHall_number() {
        return hall_number;
    }

    public void setHall_number(String hall_number) {
        this.hall_number = hall_number;
    }

    public String getEvent_start_date() {
        return event_start_date;
    }

    public void setEvent_start_date(String event_start_date) {
        this.event_start_date = event_start_date;
    }

    public String getEvent_end_date() {
        return event_end_date;
    }

    public void setEvent_end_date(String event_end_date) {
        this.event_end_date = event_end_date;
    }

    public String getVpn_coo_email() {
        return vpn_coo_email;
    }

    public void setVpn_coo_email(String vpn_coo_email) {
        this.vpn_coo_email = vpn_coo_email;
    }

    public String getLocal_setup() {
        return local_setup;
    }

    public void setLocal_setup(String local_setup) {
        this.local_setup = local_setup;
    }

    public String getEvent_no() {
        return event_no;
    }

    public void setEvent_no(String event_no) {
        this.event_no = event_no;
    }

    public String getEvent_coo_name() {
        return event_coo_name;
    }

    public void setEvent_coo_name(String event_coo_name) {
        this.event_coo_name = event_coo_name;
    }

    public String getEvent_coo_email() {
        return event_coo_email;
    }

    public void setEvent_coo_email(String event_coo_email) {
        this.event_coo_email = event_coo_email;
    }

    public String getEvent_coo_design() {
        return event_coo_design;
    }

    public void setEvent_coo_design(String event_coo_design) {
        this.event_coo_design = event_coo_design;
    }

    public String getEvent_coo_mobile() {
        return event_coo_mobile;
    }

    public void setEvent_coo_mobile(String event_coo_mobile) {
        this.event_coo_mobile = event_coo_mobile;
    }

    public Map getWebcast_uploaded_files() {
        return webcast_uploaded_files;
    }

    public void setWebcast_uploaded_files(Map webcast_uploaded_files) {
        this.webcast_uploaded_files = webcast_uploaded_files;
    }

    public String getEvent_coo_address() {
        return event_coo_address;
    }

    public void setEvent_coo_address(String event_coo_address) {
        this.event_coo_address = event_coo_address;
    }

    public String getMigration_date() {
        return migration_date;
    }

    public void setMigration_date(String migration_date) {
        this.migration_date = migration_date;
    }

    public String getVpn_coo1() {
        return vpn_coo1;
    }

    public void setVpn_coo1(String vpn_coo1) {
        this.vpn_coo1 = vpn_coo1;
    }

    public String getReq_co() {
        return req_co;
    }

    public void setReq_co(String req_co) {
        this.req_co = req_co;
    }

    public String getNew_min_dept() {
        return new_min_dept;
    }

    public void setNew_min_dept(String new_min_dept) {
        this.new_min_dept = new_min_dept;
    }

    public List<Object> fetchCoordinator(String orgType, String dptType, String orgDept) {
        return dlistDao.fetchCoordinator(orgType, dptType, orgDept);
    }

    public String getAct_email1() {
        return act_email1;
    }

    public void setAct_email1(String act_email1) {
        this.act_email1 = act_email1;
    }

    public String getNewcode() {
        return newcode;
    }

    public void setNewcode(String newcode) {
        this.newcode = newcode;
    }

    public String getOther_remarks() {
        return other_remarks;
    }

    public void setOther_remarks(String other_remarks) {
        this.other_remarks = other_remarks;
    }

    public String getReq_user_type() {
        return req_user_type;
    }

    public void setReq_user_type(String req_user_type) {
        this.req_user_type = req_user_type;
    }

    public String getApplicant_employment() {
        return applicant_employment;
    }

    public void setApplicant_employment(String applicant_employment) {
        this.applicant_employment = applicant_employment;
    }

    public String getApplicant_Org() {
        return applicant_Org;
    }

    public void setApplicant_Org(String applicant_Org) {
        this.applicant_Org = applicant_Org;
    }

    public String getApplicant_min() {
        return applicant_min;
    }

    public void setApplicant_min(String applicant_min) {
        this.applicant_min = applicant_min;
    }

    public String getApplicant_stateCode() {
        return applicant_stateCode;
    }

    public void setApplicant_stateCode(String applicant_stateCode) {
        this.applicant_stateCode = applicant_stateCode;
    }

    public String getApplicant_Smi() {
        return applicant_Smi;
    }

    public void setApplicant_Smi(String applicant_Smi) {
        this.applicant_Smi = applicant_Smi;
    }

    public String getApplicant_other_dept() {
        return applicant_other_dept;
    }

    public void setApplicant_other_dept(String applicant_other_dept) {
        this.applicant_other_dept = applicant_other_dept;
    }

    public String getApplicant_single_dob() {
        return applicant_single_dob;
    }

    public void setApplicant_single_dob(String applicant_single_dob) {
        this.applicant_single_dob = applicant_single_dob;
    }

    public String getApplicant_single_dor() {
        return applicant_single_dor;
    }

    public void setApplicant_single_dor(String applicant_single_dor) {
        this.applicant_single_dor = applicant_single_dor;
    }

    public String getApplicant_single_id_type() {
        return applicant_single_id_type;
    }

    public void setApplicant_single_id_type(String applicant_single_id_type) {
        this.applicant_single_id_type = applicant_single_id_type;
    }

    public String getApplicant_single_email1() {
        return applicant_single_email1;
    }

    public void setApplicant_single_email1(String applicant_single_email1) {
        this.applicant_single_email1 = applicant_single_email1;
    }

    public String getApplicant_single_email2() {
        return applicant_single_email2;
    }

    public void setApplicant_single_email2(String applicant_single_email2) {
        this.applicant_single_email2 = applicant_single_email2;
    }

    public String getApplicant_single_emp_type() {
        return applicant_single_emp_type;
    }

    public void setApplicant_single_emp_type(String applicant_single_emp_type) {
        this.applicant_single_emp_type = applicant_single_emp_type;
    }

    public String getApplicant_req_for() {
        return applicant_req_for;
    }

    public void setApplicant_req_for(String applicant_req_for) {
        this.applicant_req_for = applicant_req_for;
    }

    public String getApplicant_name() {
        return applicant_name;
    }

    public void setApplicant_name(String applicant_name) {
        this.applicant_name = applicant_name;
    }

    public String getApplicant_empcode() {
        return applicant_empcode;
    }

    public void setApplicant_empcode(String applicant_empcode) {
        this.applicant_empcode = applicant_empcode;
    }

    public String getApplicant_mobile() {
        return applicant_mobile;
    }

    public void setApplicant_mobile(String applicant_mobile) {
        this.applicant_mobile = applicant_mobile;
    }

    public String getApplicant_email() {
        return applicant_email;
    }

    public void setApplicant_email(String applicant_email) {
        this.applicant_email = applicant_email;
    }

    public String getApplicant_design() {
        return applicant_design;
    }

    public void setApplicant_design(String applicant_design) {
        this.applicant_design = applicant_design;
    }

    public String getApplicant_state() {
        return applicant_state;
    }

    public void setApplicant_state(String applicant_state) {
        this.applicant_state = applicant_state;
    }

    public String getApplicant_dept() {
        return applicant_dept;
    }

    public void setApplicant_dept(String applicant_dept) {
        this.applicant_dept = applicant_dept;
    }

    public String getDeact_email1() {
        return deact_email1;
    }

    public void setDeact_email1(String deact_email1) {
        this.deact_email1 = deact_email1;
    }

    public String getIs_hosted() {
        return is_hosted;
    }

    public void setIs_hosted(String is_hosted) {
        this.is_hosted = is_hosted;
    }

    public String getMailsent() {
        return mailsent;
    }

    public void setMailsent(String mailsent) {
        this.mailsent = mailsent;
    }

    public String getRadioOnBehalf() {
        return radioOnBehalf;
    }

    public void setRadioOnBehalf(String radioOnBehalf) {
        this.radioOnBehalf = radioOnBehalf;
    }

    public String getRelationWithOwner() {
        return relationWithOwner;
    }

    public void setRelationWithOwner(String relationWithOwner) {
        this.relationWithOwner = relationWithOwner;
    }

    public String getOtherRelation() {
        return otherRelation;
    }

    public void setOtherRelation(String otherRelation) {
        this.otherRelation = otherRelation;
    }

    public String getOn_behalf_email() {
        return on_behalf_email;
    }

    public void setOn_behalf_email(String on_behalf_email) {
        this.on_behalf_email = on_behalf_email;
    }

    public String getNewcodeonbehalf() {
        return newcodeonbehalf;
    }

    public void setNewcodeonbehalf(String newcodeonbehalf) {
        this.newcodeonbehalf = newcodeonbehalf;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason_txt() {
        return reason_txt;
    }

    public void setReason_txt(String reason_txt) {
        this.reason_txt = reason_txt;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getNicDateOfBirth() {
        return nicDateOfBirth;
    }

    public void setNicDateOfBirth(String nicDateOfBirth) {
        this.nicDateOfBirth = nicDateOfBirth;
    }

    public String getNicDateOfRetirement() {
        return nicDateOfRetirement;
    }

    public void setNicDateOfRetirement(String nicDateOfRetirement) {
        this.nicDateOfRetirement = nicDateOfRetirement;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "FormBean{" + "dlistDao=" + dlistDao + ", formid=" + formid + ", new_min_dept=" + new_min_dept + ", nic_employee=" + nic_employee + ", module=" + module + ", sourceip=" + sourceip + ", destinationip=" + destinationip + ", service=" + service + ", ports=" + ports + ", action=" + action + ", timeperiod=" + timeperiod + ", act_email1=" + act_email1 + ", deact_email1=" + deact_email1 + ", dor_email=" + dor_email + ", p_single_dor=" + p_single_dor + ", fixed_dor_for_retired=" + fixed_dor_for_retired + ", smsservice1=" + smsservice1 + ", pull_url=" + pull_url + ", pull_keyword=" + pull_keyword + ", s_code=" + s_code + ", short_code=" + short_code + ", app_name=" + app_name + ", app_url=" + app_url + ", sms_usage=" + sms_usage + ", server_loc=" + server_loc + ", server_loc_txt=" + server_loc_txt + ", base_ip=" + base_ip + ", service_ip=" + service_ip + ", t_off_name=" + t_off_name + ", tdesignation=" + tdesignation + ", temp_code=" + temp_code + ", taddrs=" + taddrs + ", tcity=" + tcity + ", tstate=" + tstate + ", tpin=" + tpin + ", ttel_ofc=" + ttel_ofc + ", ttel_res=" + ttel_res + ", tmobile=" + tmobile + ", tauth_email=" + tauth_email + ", bauth_off_name=" + bauth_off_name + ", bdesignation=" + bdesignation + ", bemp_code=" + bemp_code + ", baddrs=" + baddrs + ", bcity=" + bcity + ", bstate=" + bstate + ", bpin=" + bpin + ", btel_ofc=" + btel_ofc + ", btel_res=" + btel_res + ", bmobile=" + bmobile + ", bauth_email=" + bauth_email + ", audit=" + audit + ", datepicker1=" + datepicker1 + ", staging_ip=" + staging_ip + ", sender=" + sender + ", sender_id=" + sender_id + ", domestic_traf=" + domestic_traf + ", inter_traf=" + inter_traf + ", imgtxt=" + imgtxt + ", telnet=" + telnet + ", firewall_id=" + firewall_id + ", user_name=" + user_name + ", user_mobile=" + user_mobile + ", user_email=" + user_email + ", user_ophone=" + user_ophone + ", user_rphone=" + user_rphone + ", user_design=" + user_design + ", user_empcode=" + user_empcode + ", user_address=" + user_address + ", user_city=" + user_city + ", user_state=" + user_state + ", user_alt_address=" + user_alt_address + ", user_pincode=" + user_pincode + ", user_employment=" + user_employment + ", ca_name=" + ca_name + ", ca_design=" + ca_design + ", ca_mobile=" + ca_mobile + ", ca_email=" + ca_email + ", hod_name=" + hod_name + ", hod_tel=" + hod_tel + ", hod_mobile=" + hod_mobile + ", hod_email=" + hod_email + ", min=" + min + ", dept=" + dept + ", state_dept=" + state_dept + ", stateCode=" + stateCode + ", org=" + org + ", other_dept=" + other_dept + ", under_sec_email=" + under_sec_email + ", under_sec_name=" + under_sec_name + ", under_sec_mobile=" + under_sec_mobile + ", under_sec_tel=" + under_sec_tel + ", under_sec_desig=" + under_sec_desig + ", dns_domain=" + dns_domain + ", dns_url=" + dns_url + ", dns_cname=" + dns_cname + ", dns_oldip=" + dns_oldip + ", dns_newip=" + dns_newip + ", dns_loc=" + dns_loc + ", request_cname_chk=" + request_cname_chk + ", request_aaaa=" + request_aaaa + ", request_mx=" + request_mx + ", request_mx1=" + request_mx1 + ", request_ptr=" + request_ptr + ", request_ptr1=" + request_ptr1 + ", request_srv=" + request_srv + ", request_spf=" + request_spf + ", request_txt=" + request_txt + ", request_dmarc=" + request_dmarc + ", request_dmarc_chk=" + request_dmarc_chk + ", request_aaaa_chk=" + request_aaaa_chk + ", request_mx_chk=" + request_mx_chk + ", request_ptr_chk=" + request_ptr_chk + ", request_srv_chk=" + request_srv_chk + ", request_spf_chk=" + request_spf_chk + ", request_txt_chk=" + request_txt_chk + ", req_=" + req_ + ", old_url=" + old_url + ", old_ip=" + old_ip + ", url=" + url + ", dns_user_empcode=" + dns_user_empcode + ", migration_date=" + migration_date + ", wifi_type=" + wifi_type + ", wifi_mac1=" + wifi_mac1 + ", wifi_mac2=" + wifi_mac2 + ", wifi_mac3=" + wifi_mac3 + ", wifi_mac4=" + wifi_mac4 + ", wifi_os1=" + wifi_os1 + ", wifi_os2=" + wifi_os2 + ", wifi_os3=" + wifi_os3 + ", wifi_os4=" + wifi_os4 + ", wifi_request=" + wifi_request + ", wifi_time=" + wifi_time + ", wifi_duration=" + wifi_duration + ", wifi_process=" + wifi_process + ", delete_mac_id=" + delete_mac_id + ", cancel_mac_id=" + cancel_mac_id + ", mac_list=" + mac_list + ", event_name_eng=" + event_name_eng + ", event_name_hin=" + event_name_hin + ", event_type=" + event_type + ", telecast=" + telecast + ", channel_name=" + channel_name + ", live_feed=" + live_feed + ", vc_id=" + vc_id + ", event_size=" + event_size + ", media_format=" + media_format + ", cheque_no=" + cheque_no + ", cheque_amount=" + cheque_amount + ", cheque_date=" + cheque_date + ", bank_name=" + bank_name + ", conf_name=" + conf_name + ", conf_type=" + conf_type + ", conf_city=" + conf_city + ", conf_schedule=" + conf_schedule + ", conf_session=" + conf_session + ", conf_bw=" + conf_bw + ", conf_provider=" + conf_provider + ", conf_event_hired=" + conf_event_hired + ", conf_flash=" + conf_flash + ", conf_video=" + conf_video + ", conf_contact=" + conf_contact + ", remarks=" + remarks + ", payment=" + payment + ", conf_radio=" + conf_radio + ", hall_type=" + hall_type + ", hall_number=" + hall_number + ", event_start_date=" + event_start_date + ", event_end_date=" + event_end_date + ", local_setup=" + local_setup + ", event_no=" + event_no + ", event_coo_name=" + event_coo_name + ", event_coo_email=" + event_coo_email + ", event_coo_design=" + event_coo_design + ", event_coo_mobile=" + event_coo_mobile + ", event_coo_address=" + event_coo_address + ", webcast_uploaded_files=" + webcast_uploaded_files + ", device_type1=" + device_type1 + ", device_type2=" + device_type2 + ", device_type3=" + device_type3 + ", device_type4=" + device_type4 + ", req_other_add=" + req_other_add + ", campaign_id=" + campaign_id + ", submittedAt=" + submittedAt + ", nicDateOfBirth=" + nicDateOfBirth + ", nicDateOfRetirement=" + nicDateOfRetirement + ", designation=" + designation + ", displayName=" + displayName + ", wifi_bo=" + wifi_bo + ", boName=" + boName + ", eligibility=" + eligibility + ", daon_mobile=" + daon_mobile + ", da_vpn_reg_no=" + da_vpn_reg_no + ", single_dob=" + single_dob + ", single_dor=" + single_dor + ", single_id_type=" + single_id_type + ", single_email1=" + single_email1 + ", single_email2=" + single_email2 + ", single_emp_type=" + single_emp_type + ", req_user_type=" + req_user_type + ", applicant_single_dob=" + applicant_single_dob + ", applicant_single_dor=" + applicant_single_dor + ", applicant_single_id_type=" + applicant_single_id_type + ", applicant_single_email1=" + applicant_single_email1 + ", applicant_single_email2=" + applicant_single_email2 + ", applicant_single_emp_type=" + applicant_single_emp_type + ", applicant_employment=" + applicant_employment + ", applicant_Org=" + applicant_Org + ", applicant_min=" + applicant_min + ", applicant_dept=" + applicant_dept + ", applicant_stateCode=" + applicant_stateCode + ", applicant_Smi=" + applicant_Smi + ", applicant_other_dept=" + applicant_other_dept + ", applicant_req_for=" + applicant_req_for + ", applicant_name=" + applicant_name + ", applicant_empcode=" + applicant_empcode + ", applicant_mobile=" + applicant_mobile + ", applicant_email=" + applicant_email + ", applicant_design=" + applicant_design + ", applicant_state=" + applicant_state + ", domain=" + domain + ", https=" + https + ", ldap_id1=" + ldap_id1 + ", ldap_id2=" + ldap_id2 + ", protocol=" + protocol + ", radioOnBehalf=" + radioOnBehalf + ", relationWithOwner=" + relationWithOwner + ", otherRelation=" + otherRelation + ", on_behalf_email=" + on_behalf_email + ", newcodeonbehalf=" + newcodeonbehalf + ", reason=" + reason + ", reason_txt=" + reason_txt + ", initials=" + initials + ", mname=" + mname + ", country_code=" + country_code + ", new_mobile=" + new_mobile + ", newcode=" + newcode + ", other_remarks=" + other_remarks + ", uploaded_filename=" + uploaded_filename + ", renamed_filepath=" + renamed_filepath + ", inst_name=" + inst_name + ", inst_id=" + inst_id + ", nkn_project=" + nkn_project + ", request_type=" + request_type + ", pse=" + pse + ", pse_ministry=" + pse_ministry + ", pse_state=" + pse_state + ", pse_district=" + pse_district + ", fwd_ofc_name=" + fwd_ofc_name + ", fwd_ofc_mobile=" + fwd_ofc_mobile + ", fwd_ofc_email=" + fwd_ofc_email + ", fwd_ofc_tel=" + fwd_ofc_tel + ", fwd_ofc_design=" + fwd_ofc_design + ", fwd_ofc_add=" + fwd_ofc_add + ", primary_user=" + primary_user + ", primary_user_id=" + primary_user_id + ", role_assign=" + role_assign + ", fname=" + fname + ", lname=" + lname + ", uid=" + uid + ", mail=" + mail + ", registration_no=" + registration_no + ", acc_cat=" + acc_cat + ", list_name=" + list_name + ", description_list=" + description_list + ", list_mod=" + list_mod + ", allowed_member=" + allowed_member + ", list_temp=" + list_temp + ", validity_date=" + validity_date + ", non_nicnet=" + non_nicnet + ", memberCount=" + memberCount + ", mail_Acceptance=" + mail_Acceptance + ", owner_Name=" + owner_Name + ", Owner_Email=" + Owner_Email + ", owner_Mobile=" + owner_Mobile + ", moderator_Name=" + moderator_Name + ", moderator_Email=" + moderator_Email + ", moderator_Mobile=" + moderator_Mobile + ", owner_Admin=" + owner_Admin + ", moderator_Admin=" + moderator_Admin + ", req_for=" + req_for + ", account_name=" + account_name + ", add_ip1=" + add_ip1 + ", add_ip2=" + add_ip2 + ", add_ip3=" + add_ip3 + ", add_ip4=" + add_ip4 + ", old_ip1=" + old_ip1 + ", new_ip1=" + new_ip1 + ", old_ip2=" + old_ip2 + ", new_ip2=" + new_ip2 + ", old_ip3=" + old_ip3 + ", new_ip3=" + new_ip3 + ", old_ip4=" + old_ip4 + ", new_ip4=" + new_ip4 + ", ip_type=" + ip_type + ", relay_app=" + relay_app + ", relay_old_ip=" + relay_old_ip + ", ldap_account_name=" + ldap_account_name + ", ldap_url=" + ldap_url + ", ldap_auth_allocate=" + ldap_auth_allocate + ", mailsent=" + mailsent + ", relay_ip=" + relay_ip + ", relay_app_name=" + relay_app_name + ", division=" + division + ", os=" + os + ", relay_server_loc=" + relay_server_loc + ", ip_staging=" + ip_staging + ", relay_uploaded_filename=" + relay_uploaded_filename + ", relay_renamed_filepath=" + relay_renamed_filepath + ", relay_app_url=" + relay_app_url + ", relay_sender_id=" + relay_sender_id + ", domain_mx=" + domain_mx + ", smtp_port=" + smtp_port + ", spf=" + spf + ", dkim=" + dkim + ", dmarc=" + dmarc + ", relay_auth_id=" + relay_auth_id + ", old_relay_ip=" + old_relay_ip + ", otp_mail_service=" + otp_mail_service + ", mail_type_trans_mail=" + mail_type_trans_mail + ", mail_type_reg_mail=" + mail_type_reg_mail + ", mail_type_forgotpass=" + mail_type_forgotpass + ", mail_type_alert=" + mail_type_alert + ", mail_type_other=" + mail_type_other + ", hardware_uploaded_filename=" + hardware_uploaded_filename + ", hardware_renamed_filepath=" + hardware_renamed_filepath + ", security_device_type=" + security_device_type + ", point_name=" + point_name + ", point_contact=" + point_contact + ", point_email=" + point_email + ", mobile_number=" + mobile_number + ", landline_number=" + landline_number + ", security_audit=" + security_audit + ", mail_type=" + mail_type + ", other_mail_type=" + other_mail_type + ", security_exp_date=" + security_exp_date + ", is_hosted=" + is_hosted + ", dest_port=" + dest_port + ", vpn_ip_type=" + vpn_ip_type + ", vpn_new_ip1=" + vpn_new_ip1 + ", vpn_new_ip2=" + vpn_new_ip2 + ", vpn_new_ip3=" + vpn_new_ip3 + ", vpn_app_url=" + vpn_app_url + ", vpn_dest_port=" + vpn_dest_port + ", vpn_server_loc=" + vpn_server_loc + ", vpn_server_loc_txt=" + vpn_server_loc_txt + ", vpn_coo=" + vpn_coo + ", vpn_coo_email=" + vpn_coo_email + ", vpn_coo1=" + vpn_coo1 + ", req_co=" + req_co + ", vpn_reg_no=" + vpn_reg_no + ", action_type=" + action_type + ", CSRFRandom=" + CSRFRandom + ", vpn_data=" + vpn_data + '}';
    }
    
    

}
