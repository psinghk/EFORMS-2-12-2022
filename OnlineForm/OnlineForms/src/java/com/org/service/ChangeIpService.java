/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.ChangeIpDao;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author Nancy
 */
public class ChangeIpService {

    ChangeIpDao changeipdao = new ChangeIpDao();

    public void Insertip_tab1(FormBean form_details, String ip_type) {
        changeipdao.ip_tab1(form_details, ip_type);
    }

    public void Insertip_tab2(FormBean form_details, String ip_type) {
        changeipdao.ip_tab2(form_details, ip_type);
    }

    public String Insertip_tab3(FormBean form_details, Map profile_values) {
        return changeipdao.ip_tab3(form_details, profile_values);
    }

}
