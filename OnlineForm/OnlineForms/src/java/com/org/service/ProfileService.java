/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.ProfileDao;
import com.org.dao.SignUpDao;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;

/**
 *
 * @author Nancy
 */
public class ProfileService {

    ProfileDao profileDao = new ProfileDao();
    SignUpDao signUpDao = new SignUpDao();
    
    public List getCentralMinistry(String name) {
        return profileDao.getCentralMinistry(name);
    }

    public List getCentralDept(String name) {
         return profileDao.getCentralDept(name);
    }

     public int insert_profile(FormBean form_details, boolean user_flag, String otp_type,Map hodDetails,Set<String> aliases) {
        return profileDao.insert_profile(form_details, user_flag,otp_type,hodDetails,aliases);
    }
     public Map getLdapValues(String email) {
        return signUpDao.getLdapValues(email);
    }

    public Map getHODdetails(String email) {
        return signUpDao.getLdapValues(email);
    }
       public List getDistrict(String name) {
        return profileDao.getDistrict(name);
    }

       
    

    
    
}
