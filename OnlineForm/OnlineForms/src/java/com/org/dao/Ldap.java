package com.org.dao;

import com.org.connections.LdapConnection;
import com.org.connections.LdapConnectionSlave;
import com.org.connections.SingleApplicationBindContext;
import com.org.connections.SingleApplicationBindContextSlave;
import com.org.connections.SingleApplicationContext;
import com.org.connections.SingleApplicationContextSlave;
import com.org.services.Authenticate;
import com.org.services.LdapServices;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.cn;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.postalAddress;

public class Ldap implements Authenticate, LdapServices {

    LdapConnection ldapCon;
    LdapConnectionSlave ldapConSlave;

    public Ldap() {
        ldapCon = new LdapConnection();
        ldapConSlave = new LdapConnectionSlave();
    }

    @Override
    public boolean authenticate(String email, String password) {
        //System.out.println("Inside Authenticate");
        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null, ctx1 = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(&(inetuserstatus=active)(mailuserstatus=active)))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
            if (returnString) {
                try {
                    ctx1 = ldapConSlave.getConnectionSlave(userdn, password);

                    //ctx1 = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020
                    if (ctx1 == null) {
                        returnString = false;
                    } else {
                        //System.out.println("binding with Password SUCCESSFUL");
                        returnString = true;
                    }
                } catch (Exception ex) {
                    returnString = false;
                }
            }
        } catch (NamingException ex) {
            //SingleApplicationContext.recreateInstance(); // line added by pr on 2ndsep2020
            returnString = false;
        } finally {
            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
                if (ctx1 != null) {
                    ctx1.close();
                }
                if (ne != null) {
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }
        return returnString;
    }

    public Map<String, String> isUserNICEmployee(String email) {
        String final_bo = "", uid = "";
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, String> map = new HashMap<>();

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("uid") != null) {
                    String uid_tmp = attrs.get("uid").toString();
                    uid = uid_tmp.substring(5, uid_tmp.length());
                    if (uid == null || uid.equals("")) {
                        uid = "";
                    }
                }

                map.put("uid", uid);
                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                final_bo = user_bo.replace("o=", "");
                map.put("bo", final_bo);
                // System.out.println("final_bo::::::::" + final_bo);
                if ((final_bo.equalsIgnoreCase("NIC Employees")) || (final_bo.equalsIgnoreCase("dio")) || (final_bo.equalsIgnoreCase("nic-official-id"))) {
                    map.put("isNICEmployee", "yes");
                } else {
                    map.put("isNICEmployee", "no");
                }
            }
        } catch (NamingException ex) {
            map.put("isNICEmployee", "no");
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return map;
    }

    public String isUserNICEmployeeButNotContainsNicEmployees(String email) {
        String final_bo = "", uid = "";
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, String> map = new HashMap<>();
        String isEditable = "";

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("uid") != null) {
                    String uid_tmp = attrs.get("uid").toString();
                    uid = uid_tmp.substring(5, uid_tmp.length());
                    if (uid == null || uid.equals("")) {
                        uid = "";
                    }
                }

                map.put("uid", uid);
                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                final_bo = user_bo.replace("o=", "");
                map.put("bo", final_bo);
                // System.out.println("final_bo::::::::" + final_bo);
                //final_bo = "nic-official-id";
                if (final_bo.equalsIgnoreCase("NIC Employees")) {
                    // map.put("isNICEmployee", "yes");
                    isEditable = "no";
                } else if (final_bo.equalsIgnoreCase("dio") || final_bo.equalsIgnoreCase("nic-official-id")) {
                    isEditable = "yes";
                    // map.put("isNICEmployee", "editable");
                } else {
                    // map.put("isNICEmployee", "no");
                    isEditable = "yes";
                }
            }
        } catch (NamingException ex) {
            map.put("isNICEmployee", "no");
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return isEditable;
    }

    public String fetchMobileFromLdap(String email) {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String mobile_ldap = "";

        String[] attr = {"mobile"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020                
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("mobile") != null) {
                    String tmp = attrs.get("mobile").toString();
                    mobile_ldap = tmp.substring(8, tmp.length());
                    mobile_ldap = mobile_ldap.replaceAll("[-;.,\\s]", "");
                    if (mobile_ldap == null || mobile_ldap.equals("")) {
                        mobile_ldap = "";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return mobile_ldap;
    }

    public String fetchNameFromLdap(String email) {
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String cn = "", initials = "", final_name = "";;

        String[] attr = {"initials", "cn"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020                
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("initials") != null) {
                    String tmp = attrs.get("initials").toString();
                    initials = tmp.substring(10, tmp.length());
                    initials = initials.replaceAll("[-;,\\s]", "");
                    if (initials == null || initials.equals("")) {
                        initials = "";
                    }
                }

                if (attrs.get("cn") != null) {
                    String tmp = attrs.get("cn").toString();
                    cn = tmp.substring(4, tmp.length());
                    cn = cn.replaceAll("[-;.,]", "");
                    if (cn == null || cn.equals("")) {
                        cn = "";
                    }
                }

                if (initials.equals("")) {
                    final_name = cn;
                } else {
                    final_name = initials + " " + cn;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return final_name;
    }

    public Map<String, Object> fetchLdapValues(String email) {
        //System.out.println("Inside getLdapValues " + email);
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> aliases = new HashSet<>();
        Map<String, Object> ldapValues = new HashMap<>();
        String mobile_ldap = "", email_ldap = "", cn = "", nicCity = "", ophone = "", rphone = "", postalAddress = "", desig = "", initials = "", state = "", employeeNumber = "", uid = "", mailequivalentaddress = "", nicwifi = "", mailAllowedServiceAccess = "";

        String[] attr = {"mobile", "mail", "cn", "nicCity", "telephoneNumber", "postalAddress", "title", "initials", "st", "homephone", "employeeNumber", "uid", "mailequivalentaddress", "mailAllowedServiceAccess", "nicwifi"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                String final_bo = user_bo.replace("o=", "");

                if (attrs.get("uid") != null) {
                    String uid_tmp = attrs.get("uid").toString();
                    uid = uid_tmp.substring(5, uid_tmp.length());
                    if (uid == null || uid.equals("")) {
                        uid = "";
                    }
                }

                //System.out.println("UID : " + uid);
                if (attrs.get("mobile") != null) {
                    String tmp = attrs.get("mobile").toString();
                    mobile_ldap = tmp.substring(8, tmp.length());
                    mobile_ldap = mobile_ldap.replaceAll("[-;.,\\s]", "");
                    if (mobile_ldap == null || mobile_ldap.equals("")) {
                        mobile_ldap = "";
                    }
                }

                //System.out.println("Mobile : " + mobile_ldap);
                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    if (email_ldap == null || email_ldap.equals("")) {
                        email_ldap = "";
                    }
                }

                aliases.add(email_ldap.trim());

                if (attrs.get("initials") != null) {
                    String tmp = attrs.get("initials").toString();
                    initials = tmp.substring(10, tmp.length());
                    initials = initials.replaceAll("[-;,\\s]", "");
                    if (initials == null || initials.equals("")) {
                        initials = "";
                    }
                }

                if (attrs.get("cn") != null) {
                    String tmp = attrs.get("cn").toString();
                    cn = tmp.substring(4, tmp.length());
                    cn = cn.replaceAll("[-;.,]", "");
                    if (cn == null || cn.equals("")) {
                        cn = "";
                    }
                }

                if (attrs.get("nicCity") != null) {
                    String tmp = attrs.get("nicCity").toString();
                    nicCity = tmp.substring(9, tmp.length());
                    nicCity = nicCity.replaceAll("[-;.,]", "");
                    if (nicCity == null || nicCity.equals("")) {
                        nicCity = "";
                    }
                }

                if (attrs.get("telephoneNumber") != null) {
                    String tmp = attrs.get("telephoneNumber").toString();
                    ophone = tmp.substring(17, tmp.length());
                    ophone = ophone.replaceAll("[;.,\\s]", "");
                    if (ophone == null || ophone.equals("")) {
                        ophone = "";
                    }
                }

                if (attrs.get("homephone") != null) {
                    String tmp = attrs.get("homephone").toString();
                    rphone = tmp.substring(11, tmp.length());
                    rphone = rphone.replaceAll("[;.,\\s]", "");
                    if (rphone == null || rphone.equals("")) {
                        rphone = "";
                    }
                }

                if (attrs.get("postalAddress") != null) {
                    String tmp = attrs.get("postalAddress").toString();
                    postalAddress = tmp.substring(15, tmp.length());
                    postalAddress = postalAddress.replaceAll("[;]", "");
                    if (postalAddress == null || postalAddress.equals("")) {
                        postalAddress = "";
                    }
                }

                if (attrs.get("title") != null) {
                    String tmp = attrs.get("title").toString();
                    desig = tmp.substring(7, tmp.length());
                    desig = desig.replaceAll("[;,]", "");
                    if (desig == null || desig.equals("")) {
                        desig = "";
                    }
                }

                if (attrs.get("st") != null) {
                    String tmp = attrs.get("st").toString();
                    state = tmp.substring(4, tmp.length());
                    state = state.replaceAll("[-;.,]", "");
                    if (state == null || state.equals("")) {
                        state = "";
                    }
                }

                if (attrs.get("employeeNumber") != null) {
                    String tmp = attrs.get("employeeNumber").toString();
                    employeeNumber = tmp.substring(16, tmp.length());
                    employeeNumber = employeeNumber.replaceAll("[-;.,]", "");
                    if (employeeNumber == null || employeeNumber.equals("")) {
                        employeeNumber = "";
                    }
                }

                if (attrs.get("mailequivalentaddress") != null) {
                    String tmp = attrs.get("mailequivalentaddress").toString();
                    mailequivalentaddress = tmp.substring(23, tmp.length());
                    if (mailequivalentaddress == null || mailequivalentaddress.equals("")) {
                        mailequivalentaddress = "";
                    }
                }

                if (mailequivalentaddress.contains(",")) {
                    String token[] = mailequivalentaddress.split(",");
                    for (int i = 0; i < token.length; i++) {
                        if (!token[i].trim().equals(email)) {
                            aliases.add(token[i].trim());
                        }
                    }
                } else {
                    if (!mailequivalentaddress.equals("")) {
                        if (!mailequivalentaddress.trim().equals(email)) {
                            aliases.add(mailequivalentaddress.trim());
                        }
                    }
                }

                if (attrs.get("nicwifi") != null) {
                    String tmp = attrs.get("nicwifi").toString();
                    nicwifi = tmp.substring(9, tmp.length());
                    if (nicwifi == null || nicwifi.equals("")) {
                        nicwifi = "error";
                    }
                }

                if (attrs.get("mailAllowedServiceAccess") != null) {
                    String tmp = attrs.get("mailAllowedServiceAccess").toString();
                    mailAllowedServiceAccess = tmp.substring(26, tmp.length());
                    if (mailAllowedServiceAccess == null || mailAllowedServiceAccess.equals("")) {
                        mailAllowedServiceAccess = "";
                    }
                }

                String final_name = "";

                if (initials.equals("")) {
                    final_name = cn;
                } else {
                    final_name = initials + " " + cn;
                }
                if ((!email.equals("support@nic.in")) && (!email.equals("support@gov.in"))) {
                    ldapValues.put("mobile", mobile_ldap.trim());
                } else {

                }

                ldapValues.put("email", email.trim());
                ldapValues.put("mobile", mobile_ldap);
                ldapValues.put("cn", final_name.trim());
                ldapValues.put("city", nicCity.trim());
                ldapValues.put("desig", desig.trim());
                ldapValues.put("address", postalAddress.trim());
                ldapValues.put("ophone", ophone.trim());
                ldapValues.put("rphone", rphone.trim());
                ldapValues.put("state", state.trim());
                ldapValues.put("emp_code", employeeNumber.trim());
                ldapValues.put("bo", final_bo);
                ldapValues.put("uid", uid);
                ldapValues.put("aliases", aliases);
                ldapValues.put("nicwifi", nicwifi);
                ldapValues.put("mailallowedserviceaccess", mailAllowedServiceAccess);
                ldapValues.put("source", "ldap");

                if (final_bo.equalsIgnoreCase("NIC Employees")) {
                    ldapValues.put("employment", "Central");
                    ldapValues.put("min", "Electronics and Information Technology");
                    ldapValues.put("dept", "National Informatics Centre");

                    //GET Reporting/Nodal/Forwarding Officer VALUES
                    String rep_off = "";
//                    //rep_off = reporting_officer(uid);
//                    if (rep_off != "") {
//                        Map tmp_map = getRepValues(rep_off);
//                        ldapValues.put("hod_name", tmp_map.get("cn"));
//                        ldapValues.put("hod_mobile", tmp_map.get("mobile"));
//                        ldapValues.put("hod_email", tmp_map.get("email"));
//                        ldapValues.put("hod_tel", tmp_map.get("ophone"));
//                    }
                }
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER IN LDAP");
            } else {
                // user not present in ldap, everything will be blank for the user(in this case set NA)  
                ldapValues.put("mobile", mobile_ldap);
                ldapValues.put("email", email);
                ldapValues.put("cn", cn);
                ldapValues.put("nicCity", nicCity);
                ldapValues.put("desig", desig);
                ldapValues.put("postalAddress", postalAddress);
                ldapValues.put("ophone", ophone);
                if (!rphone.equals("") || rphone != null) {
                    ldapValues.put("rphone", rphone);
                } else {
                    ldapValues.put("rphone", "");
                }
                ldapValues.put("state", state);
                ldapValues.put("emp_code", employeeNumber);
                ldapValues.put("bo", "no bo");
                ldapValues.put("uid", uid);
                ldapValues.put("error", "User Not valid");
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER NOT IN LDAP");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
                if (ne != null) {
                    ldapValues.put("mobile", mobile_ldap);
                    ldapValues.put("email", email);
                    ldapValues.put("cn", cn);
                    ldapValues.put("nicCity", nicCity);
                    ldapValues.put("desig", desig);
                    ldapValues.put("postalAddress", postalAddress);
                    ldapValues.put("ophone", ophone);
                    if (!rphone.equals("") || rphone != null) {
                        ldapValues.put("rphone", rphone);
                    } else {
                        ldapValues.put("rphone", "");
                    }
                    ldapValues.put("state", state);
                    ldapValues.put("emp_code", employeeNumber);
                    ldapValues.put("bo", "no bo");
                    ldapValues.put("uid", uid);
                    ldapValues.put("error", "User Not valid");
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }
//        System.out.println("Values from LDAP1 : " + ldapValues.toString());
//        for (Map.Entry<String, Object> entry : ldapValues.entrySet()) {
//            System.out.println("Key = " + entry.getKey()
//                    + ", Value = " + entry.getValue());
//        }

        return ldapValues;
    }

    public Map<String, Object> getDorDobDoEValues(String email) {
        //System.out.println("Inside getLdapValues " + email);
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, Object> ldapValues = new HashMap<>();
        String dor = "", doe = "", dob = "";

        String[] attr = {"nicDateOfRetirement", "nicAccountExpDate", "nicDateOfBirth"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";

                if (attrs.get("nicDateOfRetirement") != null) {
                    String tmp = attrs.get("nicDateOfRetirement").toString();
                    System.out.println("DOR::::::::" + tmp);
                    dor = tmp.substring(21, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }

                if (attrs.get("nicAccountExpDate") != null) {
                    String tmp = attrs.get("nicAccountExpDate").toString();
                    System.out.println("Date Of Account Expiry::::::::" + tmp);
                    doe = tmp.substring(19, tmp.length());
                    doe = doe.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + doe);
                    if (doe == null || doe.equals("")) {
                        doe = "";
                    }
                }

                if (attrs.get("nicDateOfBirth") != null) {
                    String tmp = attrs.get("nicDateOfBirth").toString();
                    System.out.println("DOB::::::::" + tmp);
                    dob = tmp.substring(15, tmp.length());
                    dob = dob.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dob);
                    if (dob == null || dob.equals("")) {
                        dob = "";
                    }
                }

                ldapValues.put("dor", dor);
                ldapValues.put("dob", dob);
                ldapValues.put("doe", doe);
                ldapValues.put("dn", userdn);
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER IN LDAP");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {

        }
        return ldapValues;
    }

    public Map<String, Object> fetchDorDobDoEValuesForRetiredEmployees(String email) {
        //System.out.println("Inside getLdapValues " + email);
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, Object> ldapValues = new HashMap<>();
        String dor = "", doe = "", dob = "";

        String[] attr = {"nicDateOfRetirement", "nicAccountExpDate", "nicDateOfBirth"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";

        try {
            try {
                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";

                if (attrs.get("nicDateOfRetirement") != null) {
                    String tmp = attrs.get("nicDateOfRetirement").toString();
                    System.out.println("DOR::::::::" + tmp);
                    dor = tmp.substring(21, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }

                if (attrs.get("nicAccountExpDate") != null) {
                    String tmp = attrs.get("nicAccountExpDate").toString();
                    System.out.println("Date Of Account Expiry::::::::" + tmp);
                    doe = tmp.substring(19, tmp.length());
                    doe = doe.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + doe);
                    if (doe == null || doe.equals("")) {
                        doe = "";
                    }
                }

                if (attrs.get("nicDateOfBirth") != null) {
                    String tmp = attrs.get("nicDateOfBirth").toString();
                    System.out.println("DOB::::::::" + tmp);
                    dob = tmp.substring(15, tmp.length());
                    dob = dob.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dob);
                    if (dob == null || dob.equals("")) {
                        dob = "";
                    }
                }

                ldapValues.put("dor", dor);
                ldapValues.put("dob", dob);
                ldapValues.put("doe", doe);
                ldapValues.put("dn", userdn);
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER IN LDAP");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {

        }
        return ldapValues;
    }

    public Map<String, Object> fetchDorDobDoEValuesForServingEmployees(String email) {
        //System.out.println("Inside getLdapValues " + email);
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, Object> ldapValues = new HashMap<>();
        String dor = "", doe = "", dob = "";

        String[] attr = {"nicDateOfRetirement", "nicAccountExpDate", "nicDateOfBirth"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";

                if (attrs.get("nicDateOfRetirement") != null) {
                    String tmp = attrs.get("nicDateOfRetirement").toString();
                    System.out.println("DOR::::::::" + tmp);
                    dor = tmp.substring(21, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }

                if (attrs.get("nicAccountExpDate") != null) {
                    String tmp = attrs.get("nicAccountExpDate").toString();
                    System.out.println("Date Of Account Expiry::::::::" + tmp);
                    doe = tmp.substring(19, tmp.length());
                    doe = doe.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + doe);
                    if (doe == null || doe.equals("")) {
                        doe = "";
                    }
                }

                if (attrs.get("nicDateOfBirth") != null) {
                    String tmp = attrs.get("nicDateOfBirth").toString();
                    System.out.println("DOB::::::::" + tmp);
                    dob = tmp.substring(15, tmp.length());
                    dob = dob.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dob);
                    if (dob == null || dob.equals("")) {
                        dob = "";
                    }
                }

                ldapValues.put("dor", dor);
                ldapValues.put("dob", dob);
                ldapValues.put("doe", doe);
                ldapValues.put("dn", userdn);
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER IN LDAP");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {

        }
        return ldapValues;
    }

    
    public String fetchDorFromLdap(String email) {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String dor = "";

        String[] attr = {"nicDateOfRetirement"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020                
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("nicDateOfRetirement") != null) {
                    String tmp = attrs.get("nicDateOfRetirement").toString();
                    System.out.println("DOR::::::::" + tmp);
                    dor = tmp.substring(21, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return dor;
    }

    public String fetchDateOfAccountExpiryFromLdap(String email) {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String dor = "";

        String[] attr = {"nicAccountExpDate"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + ")))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020             
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("nicAccountExpDate") != null) {
                    String tmp = attrs.get("nicAccountExpDate").toString();
                    System.out.println("Date Of Account Expiry::::::::" + tmp);
                    dor = tmp.substring(19, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return dor;
    }

    public String fetchDobFromLdap(String email) throws ParseException {
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String dob = "";

        String[] attr = {"nicDateOfBirth"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContext.getLDAPInstance();
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);

            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("nicDateOfBirth") != null) {
                    String tmp = attrs.get("nicDateOfBirth").toString();
                    System.out.println("DOB::::::::" + tmp);
                    dob = tmp.substring(15, tmp.length());
                    System.out.println("sahillllll ::::::::::::" + dob);
                    dob = dob.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dob);
                    if (dob == null || dob.equals("")) {
                        dob = "";
                    }

                    if (dob.contains("Z")) {
                        dob = dob.replace(dob.substring(dob.length() - 7), "");
                        Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dob);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String formatedDate = newFormat.format(date1);
                        System.out.println("finallllllllll::::" + formatedDate);
                        dob = "";
                        dob = formatedDate;

                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return dob;
    }

    public Set<String> fetchAliases(String email) {
        System.out.println("Inside getLdapValues, getAliases of email  " + email);
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> aliases = new HashSet<>();
        aliases.add(email);
        String email_ldap = "", mailequivalentaddress = "";

        String[] attr = {"mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                String final_bo = user_bo.replace("o=", "");

                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    if (email_ldap == null || email_ldap.equals("")) {
                        email_ldap = "";
                    }
                }
                //System.out.println("EMAIL : " + email_ldap);
                aliases.add(email_ldap.trim());

                if (attrs.get("mailequivalentaddress") != null) {
                    String tmp = attrs.get("mailequivalentaddress").toString();
                    mailequivalentaddress = tmp.substring(23, tmp.length());
                    if (mailequivalentaddress == null || mailequivalentaddress.equals("")) {
                        mailequivalentaddress = "";
                    }
                }

                if (mailequivalentaddress.contains(",")) {
                    String token[] = mailequivalentaddress.split(",");
                    for (int i = 0; (i < token.length && aliases.size() < 11); i++) {
                        if (!token[i].trim().equals(email)) {
                            aliases.add(token[i].trim());
                        }
                    }
                } else {
                    if (!mailequivalentaddress.equals("")) {
                        if (!mailequivalentaddress.trim().equals(email)) {
                            aliases.add(mailequivalentaddress.trim());
                        }
                    }
                }

            } else {
                //System.out.println("COULD NOT FIND ANY ALIASES");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
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

        if (aliases.size() >= 10) {
            aliases.clear();
            aliases.add(email);
        }
        System.out.println("complete the process of get aliases of email:" + email);
        return aliases;
    }

    /**
     * Checks email in entire LDAP and returns true : if ID is present else
     * returns false
     */
    @Override
    public boolean emailValidate(String email) {
        //System.out.println("EMAIL : " + email);
        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(&(inetuserstatus=active)(mailuserstatus=active)))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            System.out.println("****************** INSIDE EXCEPTION WHILE EMAIL VALIDATION IN LDAP FOR EMAIL >>>>>>>>>  " + email + "  *************************** ");
            SingleApplicationContextSlave.recreateInstance(); // line added by pr on 2ndsep2020
            SingleApplicationBindContextSlave.recreateInstance();// line added by pr on 2ndsep2020 , so that if the ldap server has been restarted then again a fresh object can be created
            returnString = false;
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
        System.out.println("returnString in emailValidate" + returnString);
        return returnString;
    }

    // on 9th nov 2021 by ashwini instruction due to bhaskar.reddy@nic.in inactive duplicate issue 
    @Override
    public boolean emailValidate_for_all_without_status(String email) {
        //System.out.println("EMAIL : " + email);
        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        // String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(&(inetuserstatus=active)(mailuserstatus=active)))";
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + ")))"; // on 9th nov 2021 by ashwini instruction due to bhaskar.reddy@nic.in inactive duplicate issue 
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            System.out.println("****************** INSIDE EXCEPTION WHILE EMAIL VALIDATION IN LDAP FOR EMAIL >>>>>>>>>  " + email + "  *************************** ");
            SingleApplicationContextSlave.recreateInstance(); // line added by pr on 2ndsep2020
            SingleApplicationBindContextSlave.recreateInstance();// line added by pr on 2ndsep2020 , so that if the ldap server has been restarted then again a fresh object can be created
            returnString = false;
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
        //System.out.println("returnString in emailValidate" + returnString);
        return returnString;
    }

    /**
     * Checks uid in entire LDAP and returns true : if ID is present else
     * returns false
     */
    @Override
    public boolean uidValidate(String uid) {
        //System.out.println("UID : " + uid);
        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //String searchFilter = "(&(uid =" + uid + ")(&(inetuserstatus=active)(mailuserstatus=active)))";

        // above line modified by pr on 3rdmar2020
        String searchFilter = "(&(uid =" + uid + "))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            returnString = false;
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
        //System.out.println("returnString in emailValidate" + returnString);
        return returnString;
    }

    public boolean uidEmailValidate(String uid, String email) {
        //System.out.println("EMAIL : " + email + " UID ::: " + uid);
        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        //String searchFilter = "(&(|(uid=" + uid + ")(mail=" + email + ")(mailequivalentaddress=" + email + "))(&(inetuserstatus=active)(mailuserstatus=active)))";
        // above line modified by pr on 3rdmar2020, as discussed with AT Sir, statsu needs to be removed
        String searchFilter = "(&(|(uid=" + uid + ")(mail=" + email + ")(mailequivalentaddress=" + email + ")))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            returnString = false;
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
        // System.out.println("returnString in emailValidate" + returnString);
        return returnString;
    }

    public boolean isMobileInLdap(String mobile) {

        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(mobile=" + mobile + ")";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            returnString = false;
        } finally {
            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
                if (ne != null) {
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }
        return returnString;
    }

    public String fetchEmailFromLDap(String mobile) {

        String userdn = "";
        String returnString = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String email_ldap = "";
        String[] attr = {"mail"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(mobile=" + mobile + ")";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                Attributes attrs = sr.getAttributes();

                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    if (email_ldap == null || email_ldap.equals("")) {
                        email_ldap = "";
                    }
                }
                // System.out.println("EMAIL : " + email_ldap);

                // email_ldap = attrs.get("mail").toString();
                returnString = email_ldap;
            } else {
                returnString = "false";
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            returnString = "false";
        } finally {
            try {
//                if (ctx != null) {
//                   // ctx.close();
//                }
                if (ne != null) {
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }
        return returnString;
    }

    public Set<String> fetchEmailsAgainstMobile(String mobile) {
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> aliases = new HashSet<>();
        Map<String, Object> ldapValues = new HashMap<>();

        String[] attr = {"uid", "mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(mobile=" + mobile + ")(&(inetuserstatus=active)(mailuserstatus=active)))";
        String uid = "";
        Set<String> uids = new HashSet<>();
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            // System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("uid") != null) {
                    String uid_tmp = attrs.get("uid").toString();
                    uid = uid_tmp.substring(5, uid_tmp.length());
                    if (uid == null || uid.equals("")) {
                        uid = "";
                    }
                }
                uids.add(uid);
            }

            //System.out.println("uid@@@@@@@@@@" + uid);
        } catch (NamingException ex) {
            ex.printStackTrace();
        } finally {
//            try {
//                if (ctx != null) {
//                   ctx.close();
//                }
//            } 
//            catch (NamingException ex) {
//            
//            }
        }
        return uids;
    }

    public Set<String> fetchPrimaryEmailsAgainstMobile(String mobile) {
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> aliases = new HashSet<>();
        Map<String, Object> ldapValues = new HashMap<>();

        String[] attr = {"uid", "mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(mobile=" + mobile + ")(&(inetuserstatus=active)(mailuserstatus=active)))";
        String email_ldap = "", final_email_ldap = "";
        Set<String> emails = new HashSet<>();
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                // System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    String uid[] = email_ldap.split("@");
                    String startEmail = uid[0].substring(0, uid[0].length() / 2);
                    int loopLength = uid[0].length() - uid[0].length() / 2;
                    String x = "";
                    for (int i = 0; i < loopLength; i++) {
                        x += "X";
                    }
                    final_email_ldap = startEmail + x + "@" + uid[1];

                }

                emails.add(final_email_ldap);
            }

            //System.out.println("uid@@@@@@@@@@" + emails);
        } catch (NamingException ex) {
            ex.printStackTrace();
        } finally {
//            try {
//                if (ctx != null) {
//                   ctx.close();
//                }
//            } 
//            catch (NamingException ex) {
//            
//            }
        }
        return emails;
    }

    public Set<String> fetchPrimaryEmailsAgainstMobileWithoutMask(String mobile) {
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> aliases = new HashSet<>();
        Map<String, Object> ldapValues = new HashMap<>();

        String[] attr = {"uid", "mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(mobile=" + mobile + ")(&(inetuserstatus=active)(mailuserstatus=active)))";
        String email_ldap = "", final_email_ldap = "", mailequivalentaddress = "";
        Set<String> emails = new HashSet<>();
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                // System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    String uid[] = email_ldap.split("@");
                    // String startEmail = uid[0].substring(0, uid[0].length() / 2);
                    //int loopLength = uid[0].length() - uid[0].length() / 2;
                    //String x = "";
//                    for (int i = 0; i < loopLength; i++) {
//                        x += "X";
//                    }
                    //final_email_ldap = startEmail + x + "@" + uid[1];

                }

                aliases.add(email_ldap);

                if (attrs.get("mailequivalentaddress") != null) {
                    String tmp = attrs.get("mailequivalentaddress").toString();
                    mailequivalentaddress = tmp.substring(23, tmp.length());
                    if (mailequivalentaddress == null || mailequivalentaddress.equals("")) {
                        mailequivalentaddress = "";
                    }
                }

                if (mailequivalentaddress.contains(",")) {
                    String token[] = mailequivalentaddress.split(",");
                    for (int i = 0; i < token.length; i++) {
                        if (!token[i].trim().equals(email_ldap)) {
                            aliases.add(token[i].trim());
                        }
                    }
                } else {
                    if (!mailequivalentaddress.equals("")) {
                        if (!mailequivalentaddress.trim().equals(email_ldap)) {
                            aliases.add(mailequivalentaddress.trim());
                        }
                    }
                }
            }

            //System.out.println("uid@@@@@@@@@@" + emails);
        } catch (NamingException ex) {
            ex.printStackTrace();
        } finally {
//            try {
//                if (ctx != null) {
//                   ctx.close();
//                }
//            } 
//            catch (NamingException ex) {
//            
//            }
        }
        return aliases;
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

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                // System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                userdn = sr.getName() + ",dc=nic,dc=in";
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
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
        return userdn;
    }

    public Set<String> fetchAllowedDomains(String bo) {
        //Set<String> domains = new HashSet<>();

        //System.out.println(" bo value is " + bo);
        String domains = "";
        Set<String> domainsInSet = new HashSet<>();
        Set<String> set = new HashSet<>();
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String[] attr = {"dn", "sunAvailableDomainNames"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo + "))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                //System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            // System.out.println("NE : " + ne);
            while (ne.hasMoreElements()) {
                // System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                // System.out.println(" search result is " + sr);
                if (attrs.get("sunAvailableDomainNames") != null) {
                    domains = attrs.get("sunAvailableDomainNames").toString();
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in servicePackage ()" + e.getMessage());
            e.printStackTrace();
        }

        //System.out.println("domains value is " + domains);
        if (!domains.isEmpty())// line added by pr on 12thmar2020
        {
            domains = domains.substring(24, domains.length());
            String[] allowedDomains = domains.split(",");
            for (String allowedDomain : allowedDomains) {
                domainsInSet.add(allowedDomain.trim().toLowerCase());
            }
        }

        return domainsInSet;
    }

    public boolean isEmailInactive(String email) {
        //System.out.println("EMAIL : " + email);
        String userdn = "";
        boolean returnString = false;
        DirContext ctx = null;
        NamingEnumeration ne = null;

        String[] attr = {"uid"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(&(inetuserstatus=inactive)(mailuserstatus=inactive)))";
        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                userdn = sr.getName() + ",dc=nic,dc=in";
                returnString = true;
            } else {
                returnString = false;
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
            returnString = false;
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
        //System.out.println("returnString in emailValidate" + returnString);
        return returnString;
    }

    //******************starts07oct2020**********************************          
    public Set<String> fetchBos(String email) {
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> bos = new HashSet<>();
        String[] attr = {"mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
        try {
            try {
                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                String final_bo = user_bo.replace("o=", "");
                bos.add(final_bo.toLowerCase());
            } else {
                //System.out.println("COULD NOT FIND ANY ELEMENT");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
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
        //   bos.add("retiredoficers");
        return bos;
    }

  public String fetchBo(String email) {
        String userdn = "";
        String finalBo = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> bos = new HashSet<>();
        String[] attr = {"mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
        try {
            try {
                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                String final_bo = user_bo.replace("o=", "");
                bos.add(final_bo.toLowerCase());
            } else {
                //System.out.println("COULD NOT FIND ANY ELEMENT");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ne != null) {
                    ne.close();
                }
            } catch (NamingException ex) {
            }
        }
        System.out.println(bos);
        //   bos.add("retiredoficers");
        if (!bos.isEmpty()) {
            finalBo = bos.iterator().next();
        }
        return finalBo;
    }
  
  public String freeOrPaid(String bo) {
        String domains = "";
        Set<String> domainsInSet = new HashSet<>();
        Set<String> set = new HashSet<>();
        DirContext ctx = null;
        NamingEnumeration ne = null;
        String[] attr = {"dn","sunEnableGAB"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo + "))";
        try {
            try {
                //ctx = ldapCon.getContext();
                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ne = ctx.search("o=nic.in,dc=nic,dc=in", searchFilter, sc);
            while (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();
                if(bo.contains("primary business organization"))
                    continue;
                if (attrs.get("sunEnableGAB") != null) {
                    domains = attrs.get("sunEnableGAB").toString();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in servicePackage ()" + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("sunEnableGAB value is " + domains);
        if (!domains.isEmpty()){
            domains = domains.substring(14, domains.length());
            if(domains.contains("true"))
                return "paid";
            return "free";
        }
        return "paid";
    }

    public Set<String> fetchBosForRetiredEmployees(String email) {
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Set<String> bos = new HashSet<>();
        String[] attr = {"mail", "mailequivalentaddress"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
        //String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + ")))";
        try {
            try {
                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020
            } catch (Exception ex) {
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                userdn = sr.getName() + ",dc=nic,dc=in";
                String[] exploded = userdn.split(",");
                String user_bo = exploded[2];
                String final_bo = user_bo.replace("o=", "");
                bos.add(final_bo.toLowerCase());
            } else {
                //System.out.println("COULD NOT FIND ANY ELEMENT");
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
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
        //   bos.add("retiredoficers");
        return bos;
    }

    public Map<String, Object> fetchAttrUpdateMobile(String email) throws ParseException {
        String uid = "", dor = "", dob = "", desig = "", displayName = "";

        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, Object> map = new HashMap<>();

        String[] attr = {"uid", "nicDateOfRetirement", "nicDateOfBirth", "title", "displayName"};
        SearchControls sc = new SearchControls();
        sc.setReturningAttributes(attr);
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";

        try {
            try {
                //ctx = ldapCon.getContext();

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                System.out.println("CTX : " + ctx);
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();
                Logger.getLogger(Ldap.class.getName()).log(Level.SEVERE, null, ex);
            }
            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            //System.out.println("NE : " + ne);
            if (ne.hasMoreElements()) {
                //System.out.println("Inside NE");
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                if (attrs.get("uid") != null) {
                    String uid_tmp = attrs.get("uid").toString();
                    uid = uid_tmp.substring(5, uid_tmp.length());
                    if (uid == null || uid.equals("")) {
                        uid = "";
                    }
                }
                if (attrs.get("nicDateOfRetirement") != null) {
                    String tmp = attrs.get("nicDateOfRetirement").toString();
                    System.out.println("DOR::::::::" + tmp);
                    dor = tmp.substring(21, tmp.length());
                    dor = dor.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dor);
                    if (dor == null || dor.equals("")) {
                        dor = "";
                    }
                    if (dor.contains("Z")) {
                        dor = dor.replace(dor.substring(dor.length() - 7), "");
                        Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dor);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String formatedDate = newFormat.format(date1);
                        System.out.println("finallllllllll::::" + formatedDate);
                        dor = formatedDate;
                        // session.put("dateOfRetirement", dateOfRetirement);

                    }
                }

                if (attrs.get("nicDateOfBirth") != null) {
                    String tmp = attrs.get("nicDateOfBirth").toString();
                    System.out.println("DOB::::::::" + tmp);
                    dob = tmp.substring(15, tmp.length());
                    System.out.println("sahillllll ::::::::::::" + dob);
                    dob = dob.replaceAll("[;.,\\s]", "");
                    System.out.println("inside ifffff ::::::::::::" + dob);
                    if (dob == null || dob.equals("")) {
                        dob = "";
                    }

                    if (dob.contains("Z")) {
                        dob = dob.replace(dob.substring(dob.length() - 7), "");
                        Date date1 = new SimpleDateFormat("yyyyMMdd").parse(dob);
                        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
                        String formatedDate = newFormat.format(date1);
                        System.out.println("finallllllllll::::" + formatedDate);
                        dob = "";
                        dob = formatedDate;

                    }

                }

                if (attrs.get("title") != null) {
                    String tmp = attrs.get("title").toString();
                    desig = tmp.substring(7, tmp.length());
                    desig = desig.replaceAll("[;,]", "");
                    if (desig == null || desig.equals("")) {
                        desig = "";
                    }
                }
                if (attrs.get("displayName") != null) {
                    String tmp = attrs.get("displayName").toString();
                    displayName = tmp.substring(13, tmp.length());
                    displayName = displayName.replaceAll("[;,]", "");
                    if (displayName == null || displayName.equals("")) {
                        displayName = "";
                    }
                }

                map.put("uid", uid);
                map.put("nicDateOfRetirement", dor);
                map.put("nicDateOfBirth", dob);
                map.put("designation", desig);
                map.put("displayName", displayName);

            }
        } catch (NamingException ex) {
            map.put("isNICEmployee", "no");
            ex.printStackTrace();
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
        } finally {
//            try {
//                if (ctx != null) {
//                    //ctx.close();
//                }
//                if (ne != null) {
//                    ne.close();
//                }
//            } catch (NamingException ex) {
//            }
        }
        return map;
    }

    //********************End07oct2020***************************************
}
