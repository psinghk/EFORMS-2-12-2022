/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.WebcastDao;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author dhiren
 */
public class WebcastService {

    WebcastDao webcastdao = new WebcastDao();

    public int Webcast_tab1(FormBean form_details) {
        return webcastdao.Webcast_tab1(form_details);
    }

    public String Webcast_tab2(FormBean form_details, Map profile_values) {
        return webcastdao.Webcast_tab2(form_details, profile_values);
    }
//    public Map<String, Object> DLIST_tab2(Map profile_values, FormBean form_details, String generated_key) {
//        return dlistdao.DLIST_tab2(profile_values, form_details, generated_key);
//    }
//
//    public String DLIST_tab3(FormBean form_details, Map profile_values) {
//        return dlistdao.DLIST_tab3(form_details,profile_values);
//    }

}
