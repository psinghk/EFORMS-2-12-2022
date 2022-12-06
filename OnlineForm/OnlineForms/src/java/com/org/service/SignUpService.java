/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.bean.UserData;
import com.org.dao.LoadData;
import com.org.dao.SignUpDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author pari
 */
public class SignUpService {

    SignUpDao signUpDao = new SignUpDao();
    LoadData loaddata = new LoadData();
//    public String emailMobileAuth(String email, String mobile){
//        return signUpDao.getEmailMobileAuth(email, mobile);
//    }

    public boolean isHog(String email) {
        return signUpDao.isHog(email);
    }

    public boolean isHod(String email) {
        return signUpDao.isHod(email);
    }

    public Map getUserValues(String email, boolean ldapEmployee, boolean nicEmployee) {
        return signUpDao.getUserValues(email, ldapEmployee, nicEmployee);
    }

    public Map getUserValues_all(String email) {
        return signUpDao.getUserValues_all(email);
    }

    public Map<String, Object> getState() {
        return loaddata.getState();
    }

    public String CheckExistUser(String email, String mobile, String check_user_type, String whichform) {
        return signUpDao.CheckExistUser(email, mobile, check_user_type, whichform);
    }

    // check dashboard user by dhirendra
    public boolean dashboard_user(ArrayList new_email) {
        return signUpDao.dashboard_user(new_email);
    }
    //end dash board

    public String savemobile(String oldcode, String newcode, String mobile, String new_mobile, UserData userdata) {
        return signUpDao.otpGeneration_updatemobile(oldcode, newcode, mobile, new_mobile, userdata);
    }

    public String checkrequest(String email) {
        return signUpDao.checkrequest(email);
    }

    public Map getLdapValues(String email) {
        return signUpDao.getLdapValues(email);
    }

    // below code added by pr on 6thsep19
    public Map settingSessionCount(String role, ArrayList<String> email) {
        return signUpDao.settingSessionCount(role, email);
    }

    public List getCentralMinistry(UserData userdata) {
        return signUpDao.getCentralMinistry(userdata);
    }
     public List getStateCoordinator(UserData userdata) {
        return signUpDao.getStateCoordinator(userdata);
    }
     
     public Set<String> fetchIdMapped(String email) {
        return signUpDao.fetchIdMapped(email);
    }

}
