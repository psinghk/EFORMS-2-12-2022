/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.WifiDao;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;

/**
 *
 * @author nikki
 */
public class WifiService {

    WifiDao wifidao = new WifiDao();

    public void Wifi_tab1(FormBean form_details) {
        wifidao.Wifi_tab1(form_details);
    }

    public void Wifi_tab2(FormBean form_details) {
        wifidao.Wifi_tab2(form_details);
    }

    public String Wifi_tab3(FormBean form_details, int wifi_value, Map profile_values) {
        return wifidao.Wifi_tab3(form_details, wifi_value, profile_values);
    }

    public List<FormBean> fetchApplicantData(Set<String> email) throws SQLException {
        return wifidao.fetchApplicantData(email);
    }

    public List<FormBean> fetchApplicantPendingData(Set<String> email) throws SQLException {
        return wifidao.fetchApplicantPendingData(email);
    }

    public void saveDeleteReq(List<FormBean> mac_list2) {
        wifidao.saveDeleteReq(mac_list2);
    }

    public void Wifi_mac_os(FormBean formBean) {
        wifidao.Wifi_mac_os(formBean);
    }
    
    

    public List<String> getDeletedWifiList(Set<String> email) {
        return wifidao.getDeletedWifiList(email);
    }

// below methods added by ravinder 2019-12-19
    public List<String> countOfMacWithThisUser(String email) {
        return wifidao.countOfMacWithThisUser(email);
    }
    public List<String> featchMacFromWifiMacTable(String email) {
        return wifidao.featchMacFromWifiMacTable(email);
    }
//    public Set<String> countOfUserWithThisMac(String mac_address, String commaWiseAliasses) {
//    return wifidao.countOfUserWithThisMac(mac_address,commaWiseAliasses);
//    }
    public Map<String, String> countOfUserWithThisMac(String mac_address) {
        return wifidao.countOfUserWithThisMac(mac_address);
    }

    public boolean confirmMacFromDeleteTable(String mac, String date, String commawiseAliasses) {
        return wifidao.confirmMacFromDeleteTable(mac, date, commawiseAliasses);
    }

// below methods added by ravinder 2019-12-19
}
