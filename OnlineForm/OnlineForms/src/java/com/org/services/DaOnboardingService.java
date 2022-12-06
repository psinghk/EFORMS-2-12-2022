/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.services;

import com.org.dao.DaOnboardingDao;
import com.org.dao.Database;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import model.FormBean;

public class DaOnboardingService {

    DaOnboardingDao daDao = new DaOnboardingDao();
    private Database db = null;

    public String daOnboarding_tab(FormBean form_details, Map profile_values) {
        return daDao.daOnboarding_tab(form_details, profile_values);
    }

//public Set<String> 
    public String fetchBoName(String employment, String ministry, String department) {

        if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("state")) {
            return daDao.fetchBoName(employment, ministry, department);
            
        } else {
            return daDao.fetchBoName(employment, ministry, null);
        }
        //return daDao.fetchBoName(departmentName);
    }

}
