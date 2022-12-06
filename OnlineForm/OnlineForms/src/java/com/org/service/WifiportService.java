package com.org.service;
import com.org.dao.FirewallDao;
import com.org.dao.WifiportDao;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;

public class WifiportService {

    WifiportDao wifiportdao = new WifiportDao();

    public void Wifiport_tab1(List<FormBean> formDetails,String purpose) {
        this.wifiportdao.Wifiport_tab1(formDetails,purpose);
    }

    public String Wifiport_tab2(FormBean form_details, Map profile_values) {
        return this.wifiportdao.Wifiport_tab2(form_details, profile_values);
    }

    public boolean WifiportAllowedUsers(Set<String> email) {
        return this.wifiportdao.WifiportAllowedUsers(email);
    }
}
