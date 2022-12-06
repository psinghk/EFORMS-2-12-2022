/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.dao.VpnDao;
import java.util.Map;
import model.FormBean;

/**
 *
 * @author nikki
 */
public class VpnService {

    VpnDao vpndao = new VpnDao();

    public int Vpn_tab1(FormBean form_details) {
        return vpndao.Vpn_tab1(form_details);
    }

//    public void Vpn_tab2(FormBean form_details, String generated_key) {
//       vpndao.Vpn_tab2(form_details,generated_key);
//    }
    public String Vpn_tab3(FormBean form_details, String filename, Map profile_values, Map vpn_data) {
        return vpndao.Vpn_tab3(form_details, filename, profile_values, vpn_data);
    }

    public String Vpn_renew(FormBean form_details, Map profile_values) {
        return vpndao.Vpn_Renew(form_details, profile_values);
    }

    public Map validateVpnFile(String renamed_filepath) {
        return vpndao.validateVpnFile(renamed_filepath);
    }

    public int vpn_entry_edit(String field_id, String field_data, String role) {
        return vpndao.vpn_entry_edit(field_id, field_data, role);
    }

    public Boolean vpn_entry_get(String field_data) {
        return vpndao.vpn_entry_get(field_data);
    }
  public String Vpn_Surrender(FormBean form_details, Map profile_values) {	
        return vpndao.Vpn_Surrender(form_details, profile_values);	
    }	
    public String Vpn_delete(FormBean form_details, Map profile_values, String[] deleted_values) {	
        return vpndao.Vpn_Delete(form_details, profile_values, deleted_values);	
    }
}
