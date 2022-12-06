/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.connections.SingleApplicationContext;
import com.org.dao.DaOnboardingDao;
import com.org.dao.Database;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

public class DaOnboardingService {

    
    private final String ctxfactory = ServletActionContext.getServletContext().getInitParameter("ctx-factory");
    private final String providerurl = ServletActionContext.getServletContext().getInitParameter("provider-url");
    private final String binddn = ServletActionContext.getServletContext().getInitParameter("createdn");
//    private final String bindpass = ServletActionContext.getServletContext().getInitParameter("createpass");

    Base64.Decoder decoder = Base64.getDecoder();
    private final String bindpass = new String(
            decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));

    private final String nic_bo = ServletActionContext.getServletContext().getInitParameter("nic_bo");
    
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
  /*  
     public String fetchBo(String email) {
        Hashtable ht = new Hashtable();
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);

        ht.put(Context.PROVIDER_URL, providerurl);
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
        ht.put(Context.SECURITY_PRINCIPAL, binddn);
        ht.put(Context.SECURITY_CREDENTIALS, bindpass);

        DirContext ctx = null;
        NamingEnumeration ne = null;
        String[] attr = {"mobile", "mail", "cn", "nicCity", "telephoneNumber", "postalAddress", "title", "initials", "st", "homephone", "employeeNumber", "uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "";
        searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        String mobile_ldap = "", email_ldap = "", cn = "", nicCity = "", ophone = "", rphone = "", postalAddress = "", desig = "", initials = "", state = "", employeeNumber = "", uid = "";
        Map ldapValues = new HashMap();
        String final_bo = "";
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                String userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                final_bo = user_bo.replace("o=", "");

            }
            ht.clear();
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + " == " + "ex: " + ex.getMessage());
        } finally {
            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
                if (ne != null) {
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }

        return final_bo;

    }
    */
    
}
