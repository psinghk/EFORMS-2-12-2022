/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;
import com.org.dao.ImapPopDao;
import java.util.Map;
import model.FormBean;
/**
 *
 * @author dhiren
 */
public class ImapPopService {
    ImapPopDao imappopdao = new ImapPopDao();
   
    public void IMAPPOP_tab1(FormBean form_details) {
         imappopdao.IMAPPOP_tab1(form_details);
    }
    public String IMAPPOP_tab2(FormBean form_details, Map profile_values) {
        return imappopdao.IMAPPOP_tab2(form_details, profile_values);
    }
}
