package com.org.controller;

import com.org.dao.Ldap;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import org.apache.struts2.ServletActionContext;

public class Test {
    
    Connection con = null;
    Connection conSlave = null;
    
    private static final String dbhost = "localhost:3306";
    private static final String dbuser = "root";
    private static final String dbname = "onlineform";
    private static final String dbpass = "root@123";
    
    private static final String url = "jdbc:mysql://" + dbhost + "/" + dbname + "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
    
    private final String ctxfactory = "com.sun.jndi.ldap.LdapCtxFactory";
//    private final String providerurl = "ldap://10.120.43.66:389";
        private final String providerurl = "ldap://10.122.34.101:389";
    private final String binddn = "uid=nikki.nhq,ou=People,o=NIC Support Outsourced,o=Nic Support,o=nic.in,dc=nic,dc=in";
   // private final String bindpass = "Admin@#1231";
    private final String bindpass = "Nikki@389";
    private Hashtable ht = new Hashtable();
    private DirContext ctx = null;
 
    public DirContext getContext() throws ClassNotFoundException {
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
        ht.put(Context.PROVIDER_URL, providerurl);
        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
        ht.put(Context.SECURITY_PRINCIPAL, binddn);
        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        try {
            ctx = new InitialDirContext(ht);
            
            
            System.out.println("CTX value : " + ctx);
        } catch (Exception e) {
            ctx = null;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Exception in LDAPConnection.java.get: " + e.getMessage());
        }
        System.out.println("Final CTX : " + ctx);
        return ctx;
    }
    
    
    
    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.fetchDn("tiwari.ashwini@nic.in"));
        System.out.println(test.fetchAllowedDomains("NIC Support Outsourced"));
    }
    
    public String fetchDn(String email) {
        Map<String, String> map = new HashMap<>();
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String[] attr = {"dn"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(&(inetuserstatus=active)(mailuserstatus=active)))";
        String uid = "", userdn = "";

        try {
            try {
                //ctx = ldapCon.getContext();
                ctx = getContext();
                System.out.println("CTX : " + ctx);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                userdn = sr.getName() + ",dc=nic,dc=in";
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ctx != null) {
                    ctx.close();
                }
            } catch (NamingException ex) {
            }
        }
        return userdn;
    }

    public String fetchAllowedDomains(String bo) {
        //Set<String> domains = new HashSet<>();
        String domains = "";
        Set<String> set = new HashSet<>();
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String[] attr = {"dn","sunAvailableDomainNames"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
//        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo + "))";
        try {
            try {
                //ctx = ldapCon.getContext();
                ctx = getContext();
                System.out.println("CTX : " + ctx);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                
                if (attrs.get("sunAvailableDomainNames") != null) {
                    domains = attrs.get("sunAvailableDomainNames").toString();
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in servicePackage ()" + e.getMessage());
            e.printStackTrace();
        }
        return domains;
    }

}
