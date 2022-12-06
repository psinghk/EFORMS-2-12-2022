/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.LdapDao;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author Nikki
 */
public class LdapService {
    LdapDao ldapdao = new LdapDao();

    public void Ldap_tab1(FormBean form_details) {
         ldapdao.Ldap_tab1(form_details);
    }

    public String Ldap_tab2(FormBean form_details, Map profile_values) {
        return ldapdao.Ldap_tab2(form_details , profile_values);
    }

    public void insertTelnet(String telnet, String ref_num) {
        ldapdao.insertTelnet(telnet,ref_num);
    }

    public void insertFirewallId(String firewall_id, String ref_num) {
        ldapdao.insertFirewallId(firewall_id,ref_num);
    }
}
