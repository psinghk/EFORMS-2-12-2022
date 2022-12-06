/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.dao.esignDao;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
/**
 *
 * @author nikki
 */
public class esignService{
    
    esignDao  esigndao = null;
    
    public esignService() {
        esigndao = new esignDao();
    }
    
    public Map<String, Object> consent(String check, String ref_num, String formtype, FormBean form_details, Map profile_values, boolean isHog,boolean isHod, String category, String ministry, String department) {
         System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method with esign_admin in service");
        return esigndao.consent(check,ref_num,formtype,form_details, profile_values, isHog,isHod, category, ministry, department);
    }
    
    public Map<String, Object> consentRetired(String check, String ref_num, String formtype, FormBean form_details) {
         System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "call consent method for retired officers");
        return esigndao.consentRetired(check,ref_num,formtype,form_details);
    }

    public Map getUserValues(String email) {
        return esigndao.getUserValues(email);
    }
     
    
    
}
