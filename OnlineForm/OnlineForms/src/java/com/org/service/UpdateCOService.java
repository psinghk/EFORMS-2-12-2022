/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.SignUpDao;
import com.org.dao.UpdateCODao;
import java.util.List;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author nikki
 */
public class UpdateCOService {
    UpdateCODao updatecodao = new UpdateCODao();
    SignUpDao signupdao = new SignUpDao();
    
    public List updateCO_getdata(String formname) {
        return updatecodao.updateCO_getdata(formname);
    }

    public Map getCaValues(String ca_email) {
        return signupdao.getLdapValues(ca_email);
    }

    public void insertCO_details(FormBean form_details, String formname, String email) {
        updatecodao.insertCO_details(form_details, formname, email);
    }

    public String check_otherdept(String data, String test, String add_data, String dept) {
        return updatecodao.check_otherdept(data,test,add_data, dept);
    }

    public Map updateCO_getCOdata(String data, String add_data) {
        return updatecodao.updateCO_getCOdata(data,add_data);
    }

    public void updateCO_details(FormBean form_details, String add_data, String email, int emp_id) {
        updatecodao.updateCO_details(form_details,add_data,email, emp_id);
    }

    public void update_ministry(FormBean form_details, String email) {
        updatecodao.update_ministry(form_details,email);
    }

    public void update_dept(FormBean form_details, String email) {
        updatecodao.update_dept(form_details,email);
    }

    public void updateCO_deleteCO(String data, String add_data, String email) {
        updatecodao.updateCO_deleteCO(data,add_data,email);
    }
}