/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;
import com.org.dao.UpdateMobileDao;
import model.FormBean;
/**
 *
 * @author dhiren
 */
public class UpdateMobileService {
    UpdateMobileDao updatemobiledao = new UpdateMobileDao();
   
    public String update_mobile_tab1(FormBean form_details) {
        return updatemobiledao.UpdateIntoUpdatemobileTable(form_details);
    }
}
