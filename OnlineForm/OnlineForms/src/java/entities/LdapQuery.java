package entities;

import com.org.connections.SingleApplicationBindContext;
import com.org.connections.SingleApplicationBindContextSlave;
import com.org.connections.SingleApplicationContext;
import com.org.connections.SingleApplicationContextSlave;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;

public class LdapQuery {

    public static String GetTotalDomain() {
        String providerurl = getServletContext().getInitParameter("provider-url");
        String binddn = getServletContext().getInitParameter("bind-dn");
        String bindpass = getServletContext().getInitParameter("bind-pass");
        String ctxfactory = getServletContext().getInitParameter("ctx-factory");
        ArrayList domain = new ArrayList();
        String domains = null;
        Hashtable ht = new Hashtable(4);
        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);

        ht.put(Context.PROVIDER_URL, providerurl);
        ht.put(Context.SECURITY_PRINCIPAL, binddn);
        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext ctx = null;
        try {
            //ctx = new InitialDirContext(ht);
            
            //ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            ctx = SingleApplicationBindContextSlave.getLDAPInstance();// line modified by pr on 28thfeb2020
            
            String[] ID = {"associateddomain"};
            SearchControls ctls1 = new SearchControls();
            ctls1.setReturningAttributes(ID);
            ctls1.setSearchScope(2);
            String filter1 = "(associateddomain=*)";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter1, ctls1);
            while (ans.hasMoreElements()) {
                            
                //System.out.println(" inside while ");
                
                SearchResult sr = (SearchResult) ans.nextElement();
                Attributes attrib = sr.getAttributes();
                                
                domains = attrib.get("associateddomain").toString();
                
            }
            ht.clear();
                       
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
        } finally {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception ex) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ctx close problem" + ex.getMessage());
//                }
//            }
        }
        
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "domains "+domains);
        
        return domains;
    }    
    
    public static String GetmailAllowedServiceAccess(String email) {
//        String providerurl = getServletContext().getInitParameter("provider-url");
//        String binddn = getServletContext().getInitParameter("bind-dn");
//        String bindpass = getServletContext().getInitParameter("bind-pass");
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext ctx = null;
        String[] attr = {"mailAllowedServiceAccess"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "";
        searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        String mailAllowedServiceAccess = "";
        try {
            //ctx = new InitialDirContext(ht);
            
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            NamingEnumeration ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                if (attrs.get("mailAllowedServiceAccess") != null) {
                    String tmp = attrs.get("mailAllowedServiceAccess").toString();
                    mailAllowedServiceAccess = tmp.substring(26, tmp.length());
                    if (mailAllowedServiceAccess == null || mailAllowedServiceAccess.equals("")) {
                        mailAllowedServiceAccess = "";
                    }
                }
            }
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
//            }
//            //catch (NamingException ex) {
//            catch (Exception ex) {
//            }
        }
        return mailAllowedServiceAccess;
    }

    public static String GetMobile(String email) {
//        String providerurl = getServletContext().getInitParameter("provider-url");
//        String binddn = getServletContext().getInitParameter("bind-dn");
//        String bindpass = getServletContext().getInitParameter("bind-pass");
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext ctx = null;
        String[] attr = {"mobile"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "";
        searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        String mobile = "";
        try {
            //ctx = new InitialDirContext(ht);
            
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            NamingEnumeration ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                if (attrs.get("mobile") != null) {
                    String tmp = attrs.get("mobile").toString();
                    mobile = tmp.substring(8, tmp.length());
                    if (mobile == null || mobile.equals("")) {
                        mobile = "";
                    }
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user not exists");
                mobile = "error";
            }
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    ctx.close();
//                }
//            } 
//            catch (NamingException ex) {
//            
//            }
        }
        return mobile;
    }

    public static ArrayList GetMailEqui(String email) {
//        String providerurl = getServletContext().getInitParameter("provider-url");
//        String binddn = getServletContext().getInitParameter("bind-dn");
//        String bindpass = getServletContext().getInitParameter("bind-pass");
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext ctx = null;
        String[] attr = {"mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "";
        searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        String mail = "";
        String mailequivalentaddress = "";
        ArrayList mailaddress = new ArrayList();
        try {
            //ctx = new InitialDirContext(ht);
            
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            NamingEnumeration ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    mail = tmp.substring(6, tmp.length());
                    if (mail == null || mail.equals("")) {
                        mail = "";
                    }
                }
                if (attrs.get("mailequivalentaddress") != null) {
                    String tmp = attrs.get("mailequivalentaddress").toString();
                    mailequivalentaddress = tmp.substring(23, tmp.length());
                    if (mailequivalentaddress == null || mailequivalentaddress.equals("")) {
                        mailequivalentaddress = "";
                    }
                }
                mailaddress.add(email);
                if (!email.equals(mail)) {
                    mailaddress.add(mail.trim());
                }
                if (mailequivalentaddress.contains(",")) {
                    String token[] = mailequivalentaddress.split(",");
                    for (int i = 0; i < token.length; i++) {
                        if (!token[i].trim().equals(email)) {
                            mailaddress.add(token[i].trim());
                        }
                    }
                } else {
                    if (!mailequivalentaddress.equals("")) {
                        if (!mailequivalentaddress.trim().equals(email)) {
                            mailaddress.add(mailequivalentaddress.trim());
                        }
                    }
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user not exists");
                mail = "error";
            }
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    ctx.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return mailaddress;
    }

    public static String fetchwifivalue(String email) {
//        String providerurl = getServletContext().getInitParameter("provider-url");
//        String binddn = getServletContext().getInitParameter("bind-dn");
//        String bindpass = getServletContext().getInitParameter("bind-pass");
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext ctx = null;
        String[] attr = {"nicwifi"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "";
        searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        String nicwifi = "error";
        try {
            //ctx = new InitialDirContext(ht);
            
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            NamingEnumeration ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                if (attrs.get("nicwifi") != null) {
                    String tmp = attrs.get("nicwifi").toString();
                    nicwifi = tmp.substring(9, tmp.length());
                    if (nicwifi == null || nicwifi.equals("")) {
                        nicwifi = "error";
                    }
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user not exists");
                nicwifi = "error";
            }
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    ctx.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return nicwifi;
    }

    public static String CheckMobile(String mobile) {
        String finalStr = "";
        DirContext ctx = null;
        try {
//            String ctxfactory = getServletContext().getInitParameter("ctx-factory");
//            String providerurl = getServletContext().getInitParameter("provider-url-slave");
//            String binddn = getServletContext().getInitParameter("bind-dn");
//            String bindpass = getServletContext().getInitParameter("bind-pass");
//            String createdn = getServletContext().getInitParameter("createdn");
//            String createpass = getServletContext().getInitParameter("createpass");

            Base64.Decoder decoder = Base64.getDecoder();
            String createpass = new String(decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));
        	
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurl);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, createdn);
//            ht.put(Context.SECURITY_CREDENTIALS, createpass);
            //ctx = new InitialDirContext(ht);
            
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            //String[] IDs = {"mail", "mobile", "mailEquivalentAddress", "uid"};
            String[] IDs = {"uid", "mail", "mailEquivalentAddress"};
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(mobile=" + mobile + ")";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            ArrayList<String> arr = new ArrayList<String>();
            if (ans.hasMoreElements()) {
                String uid = "";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP:  timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside has more ");
                while (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    Attributes attrs = sr.getAttributes();
                    System.out.println("attrs::::" + attrs);
//             
                    // get substring from start { to }
                    int start = t.indexOf("{");
                    int end = t.indexOf("}");
                    String substr = t.substring(start + 1, end);
                    String temp = "";
                    if (substr.contains("uid:")) // if there is an email linked with this mobile then only show them 
                    {
                        uid = substr.substring(substr.indexOf("uid:") + 5);
                        if (uid.equals("")) {
                            temp = uid;
                        } else {
                            if (uid.trim().endsWith(",")) {
                                uid = uid.trim().substring(0, uid.trim().length() - 1);
                            }
                            temp = uid;
                        }
                    }
                    if (!arr.contains(temp)) {
                        arr.add(temp);
                    }
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == CheckMobile" + " " + arr.size());
                System.out.println("arr::::::::::" + arr);
                if (arr.size() <= 2) {
                    finalStr = "success";
                } else {
                    if (arr.size() == 3 && arr.toString().contains("-admin")) {
                        System.out.println(" contains");
                        finalStr = "create";
                    } else if (arr.size() == 3 && !arr.toString().contains("-admin")) {
                        System.out.println("not contains");
                        finalStr = "block";
                    } else if (arr.size() > 3) {
                        finalStr = "block";
                    }
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == finalStr" + " " + finalStr);
                System.out.println("arr.size()" + arr.size());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " final string is " + finalStr);
                //ctx.close();
                //ht.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Exception in User detail Mobile finding ::::::: " + e);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP:  timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
//            } catch (Exception e) {
//            }
        }
        return finalStr;
    }

    public static String CheckLDAPMobile(String mobile) {
        String finalStr = "";
        DirContext ctx = null;
        try {
//            String ctxfactory = getServletContext().getInitParameter("ctx-factory");
//            String providerurl = getServletContext().getInitParameter("provider-url");
//            String binddn = getServletContext().getInitParameter("bind-dn");
//            String bindpass = getServletContext().getInitParameter("bind-pass");
//            String createdn = getServletContext().getInitParameter("createdn");
//            String createpass = getServletContext().getInitParameter("createpass");

            Base64.Decoder decoder = Base64.getDecoder();
            String createpass = new String(
        			decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));
        	
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//            ht.put(Context.PROVIDER_URL, providerurl);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, createdn);
//            ht.put(Context.SECURITY_CREDENTIALS, createpass);
            //ctx = new InitialDirContext(ht);
            
            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            
            //String[] IDs = {"mail", "mobile", "mailEquivalentAddress", "uid"};
            String[] IDs = {"uid", "mail", "mailEquivalentAddress"};
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(mobile=" + mobile + ")";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            ArrayList<String> arr = new ArrayList<String>();
            if (ans.hasMoreElements()) {
                String uid = "";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP:  timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside has more ");
                finalStr = "success";
                // System.out.println("arr::::::::::"+arr);
                System.out.println("arr.size()" + arr.size());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " final string is " + finalStr);
                //ctx.close();
               // ht.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Exception in User detail Mobile finding ::::::: " + e);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP:  timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    ctx.close();
//                }
//            } catch (Exception e) {
//            }
        }
        System.out.println("finalStr:::::::" + finalStr);
        return finalStr;
    }
}
