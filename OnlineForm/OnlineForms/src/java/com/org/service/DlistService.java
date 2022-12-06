/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;
import com.org.bean.ModerateBean;
import com.org.bean.OwnerBean;
import com.org.bean.UserData;
import com.org.dao.DlistDao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import model.FormBean;
/**
 *
 * @author dhiren
 */
public class DlistService {
    DlistDao dlistdao = new DlistDao();
    public int DLIST_tab1(FormBean form_details) {
        return dlistdao.DLIST_tab1(form_details);
    }
    public Map<String, Object> DLIST_tab2(Map profile_values, FormBean form_details, String generated_key) {
        return dlistdao.DLIST_tab2(profile_values, form_details, generated_key);
    }
    public String DLIST_tab3(FormBean form_details, Map profile_values, Map bulkData) {
        return dlistdao.DLIST_tab3(form_details,profile_values,bulkData);
    }
      public Map validateBulkExcelFile(String renamed_filepath,String whichform,UserData userdata) throws FileNotFoundException, IOException {
     
       return dlistdao.validateBulkExcelile(renamed_filepath, whichform, userdata); 
    }
    
    public String DLIST_Bulktab3(FormBean form_details, Map profile_values, Map dlistbulkData) {
        return dlistdao.DLIST_Bulktab3(form_details,profile_values,dlistbulkData);
    }
}
