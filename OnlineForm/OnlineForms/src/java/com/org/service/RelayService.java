/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.RelayDao;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author Nancy
 */
public class RelayService {

    RelayDao relaydao = new RelayDao();

    public void RELAY_tab1(FormBean form_details, String ip) {
        relaydao.RELAY_tab1(form_details, ip);
    }

    public String RELAY_tab2(FormBean form_details, String relay_ip,String old_relay_ip, Map profile_values) {
        return relaydao.RELAY_tab2(form_details, relay_ip,old_relay_ip, profile_values);
    }

    public void insertTelnet(String telnet, String ref_num) {
        relaydao.insertTelnet(telnet, ref_num);
    }

    public void insertFirewallId(String firewall_id, String ref_num) {
        relaydao.insertFirewallId(firewall_id, ref_num);
    
    }

//   

}
