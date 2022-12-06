/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.bean.EmailBean;
import com.org.bean.UserData;
import com.org.bean.ValidatedEmailBean;
import com.org.dao.EmailDao;
import com.org.dao.contracts.DomainInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;

/**
 *
 * @author Nikki
 */
public class EmailService implements DomainInterface {

    //EmailDao emaildao = new EmailDao();
    EmailDao emaildao = new EmailDao();
    public void Single_tab1(FormBean form_details) {
        emaildao.Single_tab1(form_details);
    }

    public String Single_tab2(FormBean form_details, Map profile_values) {
        return emaildao.Single_tab2(form_details, profile_values);
    }

    public void Bulk_tab2(FormBean form_details) {
        emaildao.Bulk_tab2(form_details);
    }

    public String Bulk_tab3(FormBean form_details, String filename, Map profile_values, int campaignId) {
        return emaildao.Bulk_tab3(form_details, filename, profile_values, campaignId);
    }

    public void Nkn_single_tab1(FormBean form_details) {
        emaildao.Nkn_single_tab1(form_details);
    }

    public String Nkn_tab2(FormBean form_details, String filename, Map profile_values) {
        return emaildao.Nkn_tab2(form_details, filename, profile_values);
    }

    public void Gem_tab1(FormBean form_details) {
        emaildao.Gem_tab1(form_details);
    }

    public String Gem_tab2(FormBean form_details, Map profile_values) {
        return emaildao.Gem_tab2(form_details, profile_values);
    }

    public void Nkn_bulk_tab1(FormBean form_details) {
        emaildao.Nkn_bulk_tab1(form_details);
    }

    public Map validateBulkFile(String renamed_filepath, String whichform) {
        return emaildao.validateBulkFile(renamed_filepath, whichform);
    }

    public Map validateBulkCSVFileNKN(String renamed_filepath, String whichform, String id_type, String emp_type, UserData userdata, String department) {
        return emaildao.validateBulkCSVFileNKN(renamed_filepath, whichform, id_type, emp_type, userdata, department);
    }
public Map validateBulkCSVFile(String renamed_filepath, String whichform, String id_type, String emp_type, String uploaded_filename, UserData userdata, String department) {
        return emaildao.validateBulkCSVFileBackup(renamed_filepath, whichform, id_type, emp_type, uploaded_filename, userdata, department);
    }

    public Map<String, Object> fetchBulkUploadData(int campaignId) {
        if (emaildao != null) {
            return emaildao.fetchBulkUploadData(campaignId);
        } else {
            return new HashMap<>();
        }

    }

    // Added by manikant 07/09/2022
    public int fetchCountOfSuccessfulRecords(int campaignId) {
        if (emaildao != null) {
            return emaildao.fetchCountOfSuccessfulRecords(campaignId);
        } else {
            return -1;
        }

    }
    // Added by manikant 07/09/2022

    public Map<String, Object> fetchSuccessBulkData(int id) {
        if (emaildao != null) {
            return emaildao.fetchSuccessBulkData(id);
        } else {
            return new HashMap<>();
        }

    }

    public List<EmailBean> fetchSuccessBulkData(String regNumber) {
        if (emaildao != null) {
            return emaildao.fetchSuccessBulkData(regNumber);
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public Set<String> getDomain(Map profile) {
        return emaildao.getDomain(profile);
    }

    // below function added by pr on 31stjul2020
    public Set<String> getnknDomain() {
        return emaildao.getnknDomain();
    }

    public void email_act_tab1(FormBean form_details) {
        emaildao.email_act_tab1(form_details);
    }

    public String email_act_tab2(FormBean form_details, Map profile_values, String emailReq) {
        return emaildao.email_act_tab2(form_details, profile_values, emailReq);
    }

    public void email_deact_tab1(FormBean form_details) {
        emaildao.email_deact_tab1(form_details);
    }

    public String email_deact_tab2(FormBean form_details, Map profile_values) {
        return emaildao.email_deact_tab2(form_details, profile_values);
    }

    public Boolean checkEmailFromTableSingleBulkGem(String userAliases, String caAliases) {
        //return emaildao.checkEmailFromTableSingleBulkGem(email, auth_email);
        return emaildao.checkEmailFromTableSingleBulkGemNew(userAliases, caAliases);
    }
    
//     public Boolean checkEmailFromTableBulk(String email,String auth_email) {
//        return emaildao.checkEmailFromTableBulk(email,auth_email);
//    }
//      public Boolean checkEmailFromTablegem(String email,String auth_email) {
//        return emaildao.checkEmailFromTablegem(email,auth_email);
//    }

    public Boolean checkEmailForCoord(String bo, String auth_email) {
        //return emaildao.checkEmailForCoord(bo, auth_email);
        return emaildao.checkEmailForCoordNew(bo, auth_email);
    }

    public String fetchCoordinator(String dept) {
        return emaildao.fetchCoordinator(dept);
    }

    public void dor_ext_tab1(FormBean form_details) {
        emaildao.dor_ext_tab1(form_details);
    }

    public String dor_ext_tab2(FormBean form_details, Map profile_values, String emailReq, String uploaded_filename, String renamed_filepath) {
        return emaildao.dor_ext_tab2(form_details, profile_values, emailReq, uploaded_filename, renamed_filepath);
    }

    public String dor_ext_retired_tab2(UserData userdata, String oldDor, String newDor) {
        return emaildao.dor_ext_retired_tab2(userdata, oldDor, newDor, "");
    }

    public String dor_ext_retired_tab2_new(String email, String name, String mobile, String oldDor, String newDor) {
        return emaildao.dor_ext_retired_tab2_new(email, name, mobile, oldDor, newDor);
    }

    public String EmaildiscardCampaign(int EmailbulkDataId) {
        if (emaildao != null) {
            return emaildao.EmaildiscardCampaign(EmailbulkDataId);
        } else {
            return "";
        }
    }

    public Map<String, Object> fetchCompleteCampaignData(Set<String> aliases) {
        if (emaildao != null) {
            return emaildao.fetchCompleteCampaignData(aliases);
        } else {
            return new HashMap<>();
        }

    }

    public EmailBean bulkDataEdit(int emailbulkDataId, int campaignId) {
        if (emaildao != null) {
            return emaildao.fetchSingleBulkRecord(emailbulkDataId, campaignId);
        } else {
            return new EmailBean();
        }

    }

    public int updateBulkEditTable(EmailBean emailEditData, String errMsg, boolean error, boolean isDuplicate, boolean toBeDeleted, String request_type) {
        if (emaildao != null) {
            return emaildao.updateBulkEditTable(emailEditData, errMsg, error, isDuplicate, toBeDeleted, request_type);
        } else {
            return -1;
        }

    }

    public FormBean fetchFormDetails(int campaignId) {
        if (emaildao != null) {
            return emaildao.fetchFormDetails(campaignId);
        } else {
            return new FormBean();
        }

    }

    public boolean duplicateRecord(EmailBean emailEditData, int campaignId) {
        if (emaildao != null) {
            return emaildao.duplicateRecord(emailEditData, campaignId);
        } else {
            return false;
        }

    }

    public List<Integer> fetchIDOfDuplicateRecord(ValidatedEmailBean emailEditData, int campaignId) {
        if (emaildao != null) {
            return emaildao.fetchIDOfDuplicateRecord(emailEditData, campaignId);
        } else {
            return new ArrayList<>();
        }

    }

    public int fetchCampaignId(String regNumber) {
        if (emaildao != null) {
            return emaildao.fetchCampaignId(regNumber);
        } else {
            return -1;
        }
    }

    public Object fetchSuccessBulkDataUsingReg(String reg_no) {
        if (emaildao != null) {
            return emaildao.fetchSuccessBulkDataUsingReg(reg_no);
        } else {
            return null;
        }

    }

    public List<Map<String, String>> fetchEmailCampaigns(Set<String> aliases) {
        if (emaildao != null) {
            return emaildao.fetchEmailCampaigns(aliases);
        } else {
            return new ArrayList<>();
        }
    }

}
