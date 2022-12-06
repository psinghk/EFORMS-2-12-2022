package com.org.service;
import com.org.dao.FirewallDao;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;

public class FirewallService {

    FirewallDao firewalldao = new FirewallDao();

    public void Firewall_tab1(List<FormBean> formDetails,String purpose) {
        this.firewalldao.Firewall_tab1(formDetails,purpose);
    }

    public String Firewall_tab2(FormBean form_details, Map profile_values) {
        return this.firewalldao.Firewall_tab2(form_details, profile_values);
    }

    public boolean firewallAllowedUsers(Set<String> email) {
        return this.firewalldao.firewallAllowedUsers(email);
    }
}
