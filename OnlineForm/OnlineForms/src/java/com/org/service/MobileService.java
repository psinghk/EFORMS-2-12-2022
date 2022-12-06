/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;
import com.org.dao.MobileDao;
import java.util.Map;
import model.FormBean;
/**
 *
 * @author dhiren
 */
public class MobileService {
    MobileDao mobiledao = new MobileDao();
    public void Mobile_tab1(FormBean form_details) {
         mobiledao.Mobile_tab1(form_details);
    }
    public String Mobile_tab2(FormBean form_details, Map profile_values) {
        return mobiledao.Mobile_tab2(form_details,profile_values);
    }
}
