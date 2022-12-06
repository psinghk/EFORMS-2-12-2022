package com.org.service;
import com.org.dao.EmailDao;
import com.org.dao.contracts.DomainInterface;
import java.util.Map;
import java.util.Set;
import model.FormBean;

public class BulkEmailService implements DomainInterface {
    EmailDao emaildao = new EmailDao();

//    public void Single_tab1(FormBean form_details) {
//        emaildao.Single_tab1(form_details);
//    }
//
//    public String Single_tab2(FormBean form_details, Map profile_values) {
//        return emaildao.Single_tab2(form_details,profile_values);
//    }
//
//    public void Bulk_tab2(FormBean form_details) {
//        emaildao.Bulk_tab2(form_details);
//    }
//
//    public String Bulk_tab3(FormBean form_details, String filename, Map profile_values) {
//        return emaildao.Bulk_tab3(form_details, filename,profile_values);
//    }
//
//    public void Nkn_single_tab1(FormBean form_details) {
//        emaildao.Nkn_single_tab1(form_details);
//    }
//
//    public String Nkn_tab2(FormBean form_details, String filename, Map profile_values) {
//        return emaildao.Nkn_tab2(form_details, filename,profile_values);
//    }
//
//    public void Gem_tab1(FormBean form_details) {
//        emaildao.Gem_tab1(form_details);
//    }
//
//    public String Gem_tab2(FormBean form_details, Map profile_values) {
//        return emaildao.Gem_tab2(form_details,profile_values);
//    }
//
//    public void Nkn_bulk_tab1(FormBean form_details) {
//         emaildao.Nkn_bulk_tab1(form_details);
//    }
//
//    public  Map validateBulkFile(String renamed_filepath,String whichform) {
//       return emaildao.validateBulkFile(renamed_filepath, whichform);
//    }
//    
//    public  Map validateBulkCSVFile(String renamed_filepath,String whichform,String id_type) {
//       return emaildao.validateBulkCSVFile(renamed_filepath, whichform,id_type);
//    }

    @Override
    public Set<String> getDomain(Map profile) {
        return emaildao.getDomainForBulk(profile);
    }
    
//     public void email_act_tab1(FormBean form_details) {
//        emaildao.email_act_tab1(form_details);
//    }
//     
//      public String email_act_tab2(FormBean form_details, Map profile_values,String emailReq) {
//        return emaildao.email_act_tab2(form_details,profile_values,emailReq);
//    }
}
