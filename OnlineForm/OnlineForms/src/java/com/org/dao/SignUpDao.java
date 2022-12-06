package com.org.dao;

import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.connections.SingleApplicationContext;
import com.org.connections.SingleApplicationContextSlave;
import com.org.utility.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
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

public class SignUpDao extends ActionSupport {

    private final String ctxfactory = ServletActionContext.getServletContext().getInitParameter("ctx-factory");
    private final String providerurl = ServletActionContext.getServletContext().getInitParameter("provider-url");
    private final String binddn = ServletActionContext.getServletContext().getInitParameter("createdn");
//    private final String bindpass = ServletActionContext.getServletContext().getInitParameter("createpass");

    Base64.Decoder decoder = Base64.getDecoder();
    private final String bindpass = new String(
            decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));

    private final String nic_bo = ServletActionContext.getServletContext().getInitParameter("nic_bo");
    private Database db = null;

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
    ArrayList bo_list = new ArrayList();

    // start, code added by pr on 6thsep19
    String sessionid = ServletActionContext.getRequest().getSession().getId();

    ArrayList filterForms = null;
    ArrayList<String> supForms = null;

    // end, code added by pr on 6thsep19    
    public SignUpDao() {
        db = new Database();
        filterForms = new ArrayList();
        supForms = new ArrayList<>();
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isHog(String email) {
//Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        String hogEmail = "", hogUid = "";
        ArrayList role = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean flag = false;
        String uid = "";
        try {
//            Hashtable ht = new Hashtable();
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurl);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, binddn);
//            ht.put(Context.SECURITY_CREDENTIALS, bindpass);

            DirContext ctx = null;
            NamingEnumeration ne = null;
            String[] attr = {"uid"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attr);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "";
            searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
            try {
                //ctx = new InitialDirContext(ht);

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
                if (ne.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                    Attributes attrs = sr.getAttributes();
                    if (attrs.get("uid") != null) {
                        String uid_tmp = attrs.get("uid").toString();
                        uid = uid_tmp.substring(5, uid_tmp.length());
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP  :::::::" + uid);
                        if (uid == null || uid.equals("")) {
                            uid = "";
                        }
                    }
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP : " + email);
                }

                if (uid != "") {
                    ps = conSlave.prepareStatement("select hog_email from nicemployee_reporting where  user_uid = ?");
                    ps.setString(1, uid);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        hogEmail = rs.getString("hog_email");
                    }
                }

                searchFilter = "(&(|(mail=" + hogEmail + ")(mailequivalentaddress=" + hogEmail + "))(inetuserstatus=active)(mailuserstatus=active))";
                ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
                if (ne.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                    Attributes attrs = sr.getAttributes();
                    if (attrs.get("uid") != null) {
                        String uid_tmp = attrs.get("uid").toString();
                        hogUid = uid_tmp.substring(5, uid_tmp.length());
                        if (uid.equalsIgnoreCase(hogUid)) {
                            flag = true;
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP  :::::::" + uid);

                    }
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP : " + email);
                }

               // ht.clear();
            } catch (NamingException ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
            } finally {
                try {
//                    if (ctx != null) {
//                        //ctx.close();
//                    }
                    if (ne != null) {
                        ne.close();
                    }
                } catch (NamingException ex) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER " + uid + " IS HOG: " + flag);
        return flag;
    }

    public boolean isHod(String email) {
//Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        String hodEmail = "", hodUid = "";
        ArrayList role = new ArrayList();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean flag = false;
        String uid = "";
        try {
//            Hashtable ht = new Hashtable();
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurl);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, binddn);
//            ht.put(Context.SECURITY_CREDENTIALS, bindpass);

            DirContext ctx = null;
            NamingEnumeration ne = null;
            String[] attr = {"uid"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attr);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "";
            searchFilter = "(&(|(mail=" + email + ")(mailequivalentaddress=" + email + "))(inetuserstatus=active)(mailuserstatus=active))";
            try {
                //ctx = new InitialDirContext(ht);

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
                if (ne.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                    Attributes attrs = sr.getAttributes();
                    if (attrs.get("uid") != null) {
                        String uid_tmp = attrs.get("uid").toString();
                        uid = uid_tmp.substring(5, uid_tmp.length());
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP  :::::::" + uid);
                        if (uid == null || uid.equals("")) {
                            uid = "";
                        }
                    }
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP : " + email);
                }

                if (uid != "") {
                    ps = conSlave.prepareStatement("select hod_email from nicemployee_reporting where  user_uid = ?");
                    ps.setString(1, uid);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        hodEmail = rs.getString("hod_email");
                    }
                }

                searchFilter = "(&(|(mail=" + hodEmail + ")(mailequivalentaddress=" + hodEmail + "))(inetuserstatus=active)(mailuserstatus=active))";
                ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
                if (ne.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                    Attributes attrs = sr.getAttributes();
                    if (attrs.get("uid") != null) {
                        String uid_tmp = attrs.get("uid").toString();
                        hodUid = uid_tmp.substring(5, uid_tmp.length());
                        if (uid.equalsIgnoreCase(hodUid)) {
                            flag = true;
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP  :::::::" + uid);
                    }
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "HOD Email not found in LDAP : " + email);
                }
               // ht.clear();
            } catch (NamingException ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
            } finally {
                try {
//                    if (ctx != null) {
//                        //ctx.close();
//                    }
                    if (ne != null) {
                        ne.close();
                    }
                } catch (NamingException ex) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER " + uid + " IS HOG: " + flag);
        return flag;
    }

    public Map getLdapValues(String email) {
//        Hashtable ht = new Hashtable();
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);

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
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                String userdn = sr.getName() + ",dc=nic,dc=in";
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
                if (attrs.get("mobile") != null) {
                    String tmp = attrs.get("mobile").toString();
                    mobile_ldap = tmp.substring(8, tmp.length());
                    mobile_ldap = mobile_ldap.replaceAll("[-;.,\\s]", "");
                    if (mobile_ldap == null || mobile_ldap.equals("")) {
                        mobile_ldap = "";
                    }
                }
                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    if (email_ldap == null || email_ldap.equals("")) {
                        email_ldap = "";
                    }
                }
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

                String final_name = "";

                if (initials.equals("")) {
                    final_name = cn;
                } else {
                    final_name = initials + " " + cn;
                }

                ldapValues.put("mobile", mobile_ldap.trim());
                ldapValues.put("email", email.trim());
                ldapValues.put("cn", final_name.trim());
                ldapValues.put("nicCity", nicCity.trim());
                ldapValues.put("desig", desig.trim());
                ldapValues.put("postalAddress", postalAddress.trim());
                ldapValues.put("ophone", ophone.trim());
                ldapValues.put("rphone", rphone.trim());
                ldapValues.put("state", state.trim());
                ldapValues.put("emp_code", employeeNumber.trim());
                ldapValues.put("bo", final_bo);
                ldapValues.put("uid", uid);
                ldapValues.put("source", "ldap");

                if (final_bo.equalsIgnoreCase("NIC Employees")) {
                    ldapValues.put("user_employment", "Central");
                    ldapValues.put("min", "Electronics and Information Technology");
                    ldapValues.put("dept", "National Informatics Centre");

                    //GET Reporting/Nodal/Forwarding Officer VALUES
                    String rep_off = "";

                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER IN LDAP");
            } else {
                // user not present in ldap, everything will be blank for the user(in this case set NA)  
                ldapValues.put("mobile", mobile_ldap);
                ldapValues.put("email", email);
                ldapValues.put("cn", cn);
                ldapValues.put("nicCity", nicCity);
                ldapValues.put("desig", desig);
                ldapValues.put("postalAddress", postalAddress);
                ldapValues.put("ophone", ophone);
                ldapValues.put("rphone", rphone);
                ldapValues.put("state", state);
                ldapValues.put("emp_code", employeeNumber);
                ldapValues.put("bo", "no bo");
                ldapValues.put("uid", uid);
                ldapValues.put("error", "User Not valid");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER NOT IN LDAP");
            }
           // ht.clear();
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
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

        return ldapValues;

    }

    public String CheckExistUser(String email, String mobile, String check_user_type, String whichform) {
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        String db_email = "na";
        System.out.println("whichform in check exist user" + whichform);
        try {
            if (whichform.equals("track")) {

                ps = conSlave.prepareStatement("select email from user_profile where email=? and mobile = ?");
                ps.setString(1, email);
                ps.setString(2, mobile);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ps: " + ps);
                rs = ps.executeQuery();
                if (rs.next()) {
                    db_email = rs.getString("email");
                } else {
                }
            } else if (whichform.equals("ca")) {
                ps = conSlave.prepareStatement("select ca_email from comp_auth where ca_email=? and ca_mobile like ?");
                ps.setString(1, email);
                ps.setString(2, "%" + mobile + "%");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ps: " + ps);
                rs = ps.executeQuery();
                if (rs.next()) {
                    db_email = rs.getString("ca_email");
                } else {
                }
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return db_email;
    }

    public Map getUserValues(String email, boolean ldapEmployee, boolean nicEmployee) {
        System.out.println("inise get usr values:::::::::::" + ldapEmployee);
        System.out.println("inise get usr values email:::::::::::" + email);
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        Map profile_values = new HashMap();
        try {
            //        con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            System.out.println("inside get true ldap employee");
            String sql = "select * from user_profile where email=?";
            ps = conSlave.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "PSSSS: " + ps);
            if (rs.next()) {
                System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
                profile_values.put("mobile", rs.getString("mobile"));
                profile_values.put("email", email);
                profile_values.put("cn", rs.getString("name").trim());
                profile_values.put("nicCity", rs.getString("city").trim());
                profile_values.put("desig", rs.getString("designation").trim());
                profile_values.put("postalAddress", rs.getString("address").trim());
                profile_values.put("ophone", rs.getString("ophone").trim());
                profile_values.put("rphone", rs.getString("rphone").trim());
                profile_values.put("state", rs.getString("add_state").trim());
                profile_values.put("emp_code", rs.getString("emp_code").trim());
                profile_values.put("pin", rs.getString("pin").trim());
                profile_values.put("user_employment", rs.getString("employment").trim());
                profile_values.put("min", rs.getString("ministry").trim());
                profile_values.put("dept", rs.getString("department").trim());
                profile_values.put("stateCode", rs.getString("state").trim());
                profile_values.put("Org", rs.getString("organization").trim());
                profile_values.put("other_dept", rs.getString("other_dept").trim());
                profile_values.put("ca_name", rs.getString("ca_name").trim());
                profile_values.put("ca_design", rs.getString("ca_desig").trim());
                profile_values.put("ca_mobile", rs.getString("ca_mobile").trim());
                profile_values.put("ca_email", rs.getString("ca_email").trim());
                profile_values.put("hod_name", rs.getString("hod_name").trim());
                profile_values.put("hod_mobile", rs.getString("hod_mobile").trim());
                profile_values.put("hod_email", rs.getString("hod_email").trim());
                profile_values.put("hod_tel", rs.getString("hod_telephone").trim());
                profile_values.put("nicemployee", nicEmployee);
                if (ldapEmployee == false) {
                    profile_values.put("under_sec_name", rs.getString("under_sec_name").trim());
                    profile_values.put("under_sec_desig", rs.getString("under_sec_desig").trim());
                    profile_values.put("under_sec_mobile", rs.getString("under_sec_mobile").trim());
                    profile_values.put("under_sec_email", rs.getString("under_sec_email").trim());
                    profile_values.put("under_sec_tel", rs.getString("under_sec_telephone").trim());
                    profile_values.put("user_alt_address", rs.getString("user_alt_address").trim());
                }
                profile_values.put("source", "db");
                System.out.println("YYYYYYYYYYYYYY");
                System.out.println("profile values in dao get user values:::::: " + profile_values);

            } else {
                System.out.println("profile values in dao else");
                profile_values.put("error", "User Not valid");
            }

        } catch (Exception e) {

        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }

                if (con != null) {
                    // con.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profile_values;
    }

    public Map getUserValues_all(String email) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        Map profile_values = new HashMap();
        try {
            StringTokenizer token = new StringTokenizer(email, ",");
            int count = token.countTokens();
            for (int j = 0; j <= count; j++) {
                if (token.hasMoreTokens()) {
                    String a = token.nextToken();
//                    con = DbConnection.getConnection();
                    conSlave = DbConnection.getSlaveConnection();
                    String sql = "select * from user_profile where email=?";
                    ps = conSlave.prepareStatement(sql);
                    ps.setString(1, a);
                    rs = ps.executeQuery();
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "PSSSS: " + ps);
                    if (rs.next()) {
                        profile_values.clear();
                        profile_values.put("mobile", rs.getString("mobile"));
                        profile_values.put("email", a);
                        profile_values.put("cn", rs.getString("name").trim());
                        profile_values.put("nicCity", rs.getString("city").trim());
                        profile_values.put("desig", rs.getString("designation").trim());
                        profile_values.put("postalAddress", rs.getString("address").trim());
                        profile_values.put("ophone", rs.getString("ophone").trim());
                        profile_values.put("rphone", rs.getString("rphone").trim());
                        profile_values.put("state", rs.getString("add_state").trim());
                        profile_values.put("emp_code", rs.getString("emp_code").trim());
                        profile_values.put("pin", rs.getString("pin").trim());
                        profile_values.put("user_employment", rs.getString("employment").trim());
                        profile_values.put("min", rs.getString("ministry").trim());
                        profile_values.put("dept", rs.getString("department").trim());
                        profile_values.put("stateCode", rs.getString("state").trim());
                        profile_values.put("Org", rs.getString("organization").trim());
                        profile_values.put("other_dept", rs.getString("other_dept").trim());
                        profile_values.put("ca_name", rs.getString("ca_name").trim());
                        profile_values.put("ca_design", rs.getString("ca_desig").trim());
                        profile_values.put("ca_mobile", rs.getString("ca_mobile").trim());
                        profile_values.put("ca_email", rs.getString("ca_email").trim());
                        profile_values.put("hod_name", rs.getString("hod_name").trim());
                        profile_values.put("hod_mobile", rs.getString("hod_mobile").trim());
                        profile_values.put("hod_email", rs.getString("hod_email").trim());
                        profile_values.put("hod_tel", rs.getString("hod_telephone").trim());
                        profile_values.put("user_alt_address", rs.getString("user_alt_address").trim());
                        profile_values.put("source", "db");
                        break;
                    } else {
                        profile_values.put("error", "User Not valid");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }

                if (con != null) {
                    // con.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return profile_values;
    }

    public void insert_loginDetails(String email, String role) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            String update = "insert into login_details(email,ip,role,remarks,login_time) values (?,?,?,?,now())";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(update);
            ps.setString(1, email);
            ps.setString(2, ip);
            ps.setString(3, role);
            ps.setString(4, "Authentication Sucessfull");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLogoutDetails(String email) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {

            String update = "update login_details set logout_time = now() where email = ? order by login_time desc limit 1";
            con = DbConnection.getConnection();
            ps = con.prepareStatement(update);
            ps.setString(1, email);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // check dashboard user by dhirendra
    public boolean dashboard_user(ArrayList new_email) {
        boolean flg = false;
        String subQuery = "";
        int i = 0;
        for (Object s : new_email) {
            if (i == 0) {
                subQuery = "email = '" + s + "'";
            } else {
                subQuery += " or email = '" + s + "'";
            }
            ++i;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String sql = "SELECT count(*) FROM dashboard_admin where " + subQuery;
            System.out.println("SQL query to check Admin for DASHBOARD = " + sql);
            ps = conSlave.prepareStatement(sql);
            rs = ps.executeQuery();
            int result = 0;
            if (rs.next()) {
                result = rs.getInt(1);
            }

            if (result > 0) {
                flg = true;
            } else {
                flg = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flg;
    }
    //end dash board

    public String otpGeneration_updatemobile(String oldcode, String newcode, String mobile, String new_mobile, UserData userdata) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "inside save mobile");
        System.out.println("new_mobile::::::::" + new_mobile);
        String returnString = "";
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String email : userdata.getAliases()) {
        try {

            ResultSet rs = null;
            ResultSet rs1 = null;
            String sql1 = "select * from user_profile where email=? and hod_mobile =?";
            ps = conSlave.prepareStatement(sql1);
            ps.setString(1, email);
            ps.setString(2, new_mobile);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == query1" + ps);

            rs = ps.executeQuery();
            if (rs.next()) {
                returnString = "mobilesamehod";
                break;

            } else {

                String sql2 = "select * from user_profile where email=? and under_sec_mobile =?";
                ps = conSlave.prepareStatement(sql2);
                ps.setString(1, email);
                ps.setString(2, new_mobile);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == query2" + ps);
                rs = ps.executeQuery();
                if (rs.next()) {
                    returnString = "mobilesameus";
                    break;

                } else {
                    String sql_profile = "select * from user_profile where email = ?";
                    ps = conSlave.prepareStatement(sql_profile);
                    ps.setString(1, email);
                    

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == query3" + ps);
                    rs = ps.executeQuery();
                    if (rs.next()) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "inside save mobile if record foun in user profile");

                        String update = "insert into otp_save_update_mobile(oldcode,newcode,mobile,new_mobile,updated_time) values (?,?,?,?,now())";

                        ps = con.prepareStatement(update);
                        ps.setString(1, oldcode);
                        ps.setString(2, newcode);
                        ps.setString(3, mobile);
                        ps.setString(4, new_mobile);

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "inside save mobile insert query " + ps);

                        int res = ps.executeUpdate();

                        if (res > 0) {

                            String update_1 = "update user_profile set mobile=? where email =?";
                            ps = con.prepareStatement(update_1);
                            ps.setString(1, new_mobile);
                            ps.setString(2, email);
                          
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "inside save mobile update query" + ps);

                            int i = ps.executeUpdate();
                           
                            returnString = "success";
                            break;
                            
                        }
                    } 

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        }
        return returnString;

    }

    public String checkrequest(String email) {
        String returnString = "";
        System.out.println("inside dao check reuqest");
        //Connection con = null;
        try {
            //System.out.println("inside ldap condition");
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {

           // String sql1 = "select * from final_audit_track where applicant_email =? and form_name in ( 'single', 'nkn_single','nkn_bulk', 'bulk') and (status ='manual_upload' OR status ='completed' OR status = 'ca_pending' OR status = 'coordinator_pending' OR status = 'mail-admin_pending' OR status = 'da_pending' OR status = 'support_pending' OR status = 'us_pending')";
            String sql1 = "select * from final_audit_track where applicant_email =? and form_name in ( 'single', 'nkn_single','nkn_bulk', 'bulk') and (status ='manual_upload' OR status = 'ca_pending' OR status = 'coordinator_pending' OR status = 'mail-admin_pending' OR status = 'da_pending' OR status = 'support_pending' OR status = 'us_pending')";
           ps1 = conSlave.prepareStatement(sql1);
            ps1.setString(1, email);
            System.out.println("ps1:::::::::" + ps1);
            rs1 = ps1.executeQuery();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "PSSSS: in user exist table " + ps1);
            if (rs1.next()) {
                System.out.println("User with pending");
                returnString = "pending";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return returnString;
    }

    // start, code added by pr on 6thsep19
    public Map settingSessionCount(String role, ArrayList<String> email) {

        Map DAOGotMap = new HashMap<>();

        Map DAOGotFFMap = new HashMap<>(); // to store filterform arraylist

        Map finalMap = new HashMap<>();
        Map<String, ArrayList<String>> formsMap = showFilters(role, email); // sets the global filterForms with what forms we have to show in filter
        ArrayList<String> supForms = formsMap.get("allowedforms");
        //System.out.println(" inside settingSessionCount role is "+role+" email is "+email);

        // set the counts in the bean
        int pendingCount = fetchCount(supForms, "pending", role, email);
        int totalCount = fetchCount(supForms, "total", role, email);
        int newCount = fetchCount(supForms, "new", role, email);
        int completeCount = fetchCount(supForms, "completed", role, email);

        //System.out.println("Inside settingSessionCount func  role is "+role+" pendingCount "+pendingCount+" totalCount is "+totalCount+" newCount is "+newCount+" completeCount "+completeCount);
        DAOGotMap.put(role + "totalcount", totalCount);
        DAOGotMap.put(role + "newcount", newCount);
        DAOGotMap.put(role + "pendingcount", pendingCount);
        DAOGotMap.put(role + "completecount", completeCount);

        System.out.println(sessionid + " == " + "SESSION MAP : Total Count " + DAOGotMap.get(role + "totalcount"));
        System.out.println(sessionid + " == " + "SESSION MAP : pendingcount " + DAOGotMap.get(role + "pendingcount"));
        System.out.println(sessionid + " == " + "SESSION MAP : newcount " + DAOGotMap.get(role + "newcount"));
        System.out.println(sessionid + " == " + "SESSION MAP : completecount " + DAOGotMap.get(role + "completecount"));

        // System.out.println(" inside setting session count session values are below : ");
//        
//        for( Object k : DAOGotMap.keySet() )
//        {
//            //System.out.println(" key is "+k+" value is "+DAOGotMap.get(k));
//        }     
        //return DAOGotMap;        
        ArrayList<String> arr = formsMap.get("forms");

        System.out.println(" filterforms arraylist for role " + role + " is " + arr);

        DAOGotFFMap.put(role + "filterForms", arr);

        finalMap.put("counts", DAOGotMap);
        finalMap.put("forms", DAOGotFFMap);
        //DAOGotFFMap.put(role+"filterForms", filterForms);

        //finalMap.put("forms", DAOGotFFMap);
//        System.out.println(" inside loginaction counts key value is "+DAOGotMap+" forms key value is "+filterForms); 
//                
//        System.out.println(" inside settingSessionCount function finalMap values are "+finalMap);        
        return finalMap;

    }

    public Map<String, ArrayList<String>> showFilters(String role, ArrayList<String> email) {
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        ArrayList<String> filterForms = new ArrayList<>();
        ArrayList<String> supForms = new ArrayList<>();
        Map<String, ArrayList<String>> map = new HashMap<>();
        switch (role) {
            case Constants.ROLE_CA:
                pendingStr = "ca_pending";
                rejectStr = "ca_rejected";
                field = "ca_email";
                tot_field = "ca_email";
                pendingDate = "ca_datetime";
                break;
            case Constants.ROLE_SUP:
                pendingStr = "support_pending";
                rejectStr = "support_rejected";
                field = "support_email";
                tot_field = "support_email";
                pendingDate = "support_datetime";
                break;
            case Constants.ROLE_CO:
                pendingStr = "coordinator_pending";
                rejectStr = "coordinator_rejected";
                field = "coordinator_email";
                tot_field = "coordinator_email";
                pendingDate = "coordinator_datetime";
                break;
            case Constants.ROLE_MAILADMIN:
                pendingStr = "mail-admin_pending";
                rejectStr = "mail-admin_rejected";
                field = "admin_email";
                tot_field = "admin_email";
                pendingDate = "admin_datetime";
                break;
            case Constants.ROLE_USER:
                pendingStr = "manual_upload";
                rejectStr = "cancel";
                field = "applicant_email";
                tot_field = "applicant_email";
                pendingDate = "applicant_datetime";
                break;
            default:
                break;
        }
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT distinct form_name FROM final_audit_track WHERE 1 ";
            System.out.println("ROLE IN SHOWFILTER : " + role);
            if (email != null) {
                System.out.println(" inside email not null ");
                int i = 1;
                for (Object s : email) {
                    switch (role) {
                        case Constants.ROLE_CA:
                            if (i == 1) {
                                qry += " and ( ";
                            } else {
                                qry += " OR ";
                            }
                            qry += "((status = '" + pendingStr + "' and  to_email  = '" + s.toString().trim() + "') || ca_email = '" + s.toString().trim() + "')";
                            break;
                        case Constants.ROLE_CO:
                            if (i == 1) {
                                qry += " and ( ";
                            } else {
                                qry += " OR ";
                            }
                            //modified by AT to improvise query on 3rd Sep 2019
                            qry += "(( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "' ) || (coordinator_email = '" + s.toString().trim() + "' and co_id = 0 ))";
                            break;
                    }
                    i++;
                }

                // need to get id from coordinator id : modified by AT to improvise query on 3rd Sep 2019
                if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    Set<String> coordsIds = db.fetchCoIds(email);
                    String sb = "";
                    if (!coordsIds.isEmpty()) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }

                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        System.out.println("my sb:" + sb);

                        qry += " or ( status = '" + pendingStr + "' and co_id in (" + sb + ")) ";
                    }
                }

                if (role.equalsIgnoreCase(Constants.ROLE_CA) || role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    qry += " ) ";
                } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN)) {
                    qry += " and (status = 'completed' || status = 'mail-admin_rejected' || status = 'mail-admin_pending')";
                }
                System.out.println("QUERY IN FILTER: " + qry);
            }

            Set<String> emailSet = new HashSet<>(email);
            supForms = db.fetchAllowedFormsForAdmins(emailSet);

            ps = conSlave.prepareStatement(qry);
            res1 = ps.executeQuery();
            while (res1.next()) {
                String form_name = res1.getString("form_name");
                System.out.println("SUPFORMS IN SHOWFILTER ::::::::::::::::::::: " + supForms);
                if (role.equals(Constants.ROLE_SUP) || role.equals(Constants.ROLE_MAILADMIN)) {
                    if (supForms != null) {
                        if (supForms.contains("'" + form_name + "'")) {
                            System.out.println("FORM :::::::::::::: " + form_name + " getting allowed");
                            filterForms.add(form_name);
                        }
                    }
                } else {
                    filterForms.add(form_name);
                }
            }
            System.out.println(" inside show filter filterForms value is " + filterForms);
            map.put("forms", filterForms);
            map.put("allowedforms", supForms);
        } catch (Exception e) {
            System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " showFilters EXCEPTION " + e.getMessage());

            e.printStackTrace();

        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(sessionid + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showFilters EXCEPTION 2 " + e.getMessage());
                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(sessionid + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showFilters EXCEPTION 3 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(sessionid + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showFilters EXCEPTION 4 " + e.getMessage());
                }
            }
        }
        return map;
    }

    public int fetchCount(ArrayList<String> supForms, String type, String role, ArrayList<String> email) {
        System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  start of getcount function ");

        //System.out.println(" inside fetccount func admin_role value is "+role);
        long startTime = System.currentTimeMillis();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        int cnt = 0;

        String formName = "all";

        if (supForms != null) {
            if (supForms.size() > 0) {
                csvForms = String.join(",", supForms);
            }
        }

        //role = sessionMap.get("admin_role").toString();
        switch (role) {
            case Constants.ROLE_CA:
                pendingStr = "ca_pending";
                rejectStr = "ca_rejected";
                field = "ca_email";
                tot_field = "ca_email";
                pendingDate = "ca_datetime";
                break;
            case Constants.ROLE_SUP:
                pendingStr = "support_pending";
                rejectStr = "support_rejected";
                field = "support_email";
                tot_field = "support_email";
                pendingDate = "support_datetime";
                break;
            case Constants.ROLE_CO:
                pendingStr = "coordinator_pending";
                rejectStr = "coordinator_rejected";
                field = "coordinator_email";
                tot_field = "coordinator_email";
                pendingDate = "coordinator_datetime";
                break;
            case Constants.ROLE_MAILADMIN:
                pendingStr = "mail-admin_pending";
                rejectStr = "mail-admin_rejected";
                field = "admin_email";
                tot_field = "admin_email";
                pendingDate = "admin_datetime";
                break;
            case Constants.ROLE_USER:
                pendingStr = "manual_upload";
                rejectStr = "cancel";
                field = "applicant_email";
                tot_field = "applicant_email";
                pendingDate = "applicant_datetime";
                break;
        }
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT count(*) FROM final_audit_track WHERE 1 ";

            // qry += " AND on_hold = 'n' "; // so as to get only those applications which are NOT on hold    , in case of pending requests           
            switch (type) {
                case "new":
                    qry += "  and status = '" + pendingStr + "' and DATE( to_datetime) = CURDATE() ";
                    break;
                case "pending":
                    qry += "  and  status = '" + pendingStr + "'  ";
                    break;
                case "completed":
                    qry += "  and status = 'completed' ";
                    break;
                case "rejected":
                    qry += "  and status = '" + rejectStr + "' ";
                    break;
                case "forwarded":
                    qry += "  AND  status != '" + rejectStr + "' and status != '" + pendingStr + "'";
                    break;
            }
            //System.out.println("  email value is " + email);
            if (email != null) {
                //System.out.println(" inside email not null ");
                int i = 1;
                for (Object s : email) {
                    switch (role) {
                        case Constants.ROLE_CA:
                            if (i == 1) {
                                qry += " and ( ";
                            } else {
                                qry += " OR ";
                            }
                            switch (type) {
                                case "completed":
                                case "forwarded":
                                case "rejected":
                                    qry += " ( ca_email = '" + s.toString().trim() + "') ";
                                    break;
                                case "pending":
                                case "new":
                                    qry += "( to_email  = '" + s.toString().trim() + "' )";
                                    break;
                                case "total":
                                    qry += "(( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "' ) || ca_email = '" + s.toString().trim() + "')";
                                    break;
                                default:
                                    qry += " ( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "'  ) ";
                                    break;
                            }
                            break;
                        case Constants.ROLE_CO:
                            if (i == 1) {
                                qry += " and ( ";
                            } else {
                                qry += " OR ";
                            }
                            //modified by AT to improvise query on 3rd Sep 2019
                            switch (type) {
                                case "completed":
                                case "forwarded":
                                case "rejected":
                                    qry += " ( coordinator_email = '" + s.toString().trim() + "' and co_id = 0 ) ";
                                    break;
                                case "pending":
                                case "new":
                                    qry += "( to_email  = '" + s.toString().trim() + "'  and co_id = 0 )";
                                    break;
                                case "total":
                                    qry += "(( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "' ) || (coordinator_email = '" + s.toString().trim() + "' and co_id = 0 ))";
                                    break;
                                default:
                                    qry += " ( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "' ) ";
                                    break;
                            }
                            break;
                    }
                    i++;
                }

                // need to get id from coordinator id : modified by AT to improvise query on 3rd Sep 2019
                if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    Set<String> coordsIds = db.fetchCoIds(email);
                    String sb = "";
                    if (!coordsIds.isEmpty()) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }

                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        System.out.println("my sb:" + sb);

                        switch (type) {
                            case "completed":
                            case "forwarded":
                            case "rejected":
                            case "pending":
                            case "new":
                                qry += " or co_id in (" + sb + ") ";
                                break;
                            case "total":
                                qry += " or ( status = '" + pendingStr + "' and co_id in (" + sb + ")) ";
                                break;
                            default:
                                qry += " or ( status = '" + pendingStr + "' and co_id in (" + sb + ")) ";
                                break;
                        }
                    }
                }

                if (role.equalsIgnoreCase(Constants.ROLE_CA) || role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    qry += " ) ";
                } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) && type.equals("total")) {
                    qry += " and (status = 'completed' || status = 'mail-admin_rejected' || status = 'mail-admin_pending')";
                }
            }

            //System.out.println("FORM NAME : " + formName);
            if (!formName.equals("all")) {
                if (supForms != null) {
                    //System.out.println("SUPFORMS : " + supForms);
                    if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                        if (supForms.contains("'" + formName + "'")) {
                            if (formName.contains("nkn")) {
                                qry += " and  form_name like ? ";
                            } else {

                                qry += " and  form_name = ? ";
                            }
                        }
                    } else {
                        if (formName.contains("nkn")) {
                            qry += " and  form_name like ? ";
                        } else {

                            qry += " and  form_name = ? ";
                        }
                    }
                } else {
                    if (formName.contains("nkn")) {
                        qry += " and  form_name like ? ";
                    } else {

                        qry += " and  form_name = ? ";
                    }
                }
            } else {
                if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                    System.out.println("INSIDE ELSEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE" + csvForms);
                    if (!csvForms.isEmpty()) {

                        qry += " and  form_name in (" + csvForms + ") ";
                    }
                }
            }

            // start, code added by pr on 2ndjul19, so that 
            if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) && email.contains("rajesh.singh@nic.in") && csvForms.contains("ip")) {
                if (type.equals("pending") || type.equals("new")) {
                    qry += " AND to_email = 'rajesh.singh@nic.in'";
                } else {
                    qry += " AND  ( to_email = 'rajesh.singh@nic.in' OR  ( admin_email = 'rajesh.singh@nic.in'  OR  admin_email = 'rajs@nic.in' "
                            + " OR  admin_email = 'rajesh.singh@gov.in' OR  admin_email = 'rajesh.singh@dummy.nic.in'  OR  admin_email = 'rajs@dummy.nic.in' "
                            + "OR  admin_email = 'rajesh.singh@nkn.in'  OR  admin_email = 'rajs@vastu13.nic.in' ) ) ";
                }
            }

            // end, code added by pr on 2ndjul19
            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;

            System.out.println(" inside signupdao fetch count function filterForms value is " + filterForms);

            if (!formName.equals("all")) {
                if (supForms != null) {
                    if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                        if (supForms.contains("'" + formName + "'")) {
                            pscnt = pscnt + 1;
                            if (formName.contains("nkn")) {
                                formName = "%nkn%";
                            }
                            ps.setString(pscnt, formName);
                        }
                    } else {
                        pscnt = pscnt + 1;
                        if (formName.contains("nkn")) {
                            formName = "%nkn%";
                        }
                        ps.setString(pscnt, formName);
                    }
                } else {
                    pscnt = pscnt + 1;
                    if (formName.contains("nkn")) {
                        formName = "%nkn%";
                    }
                    ps.setString(pscnt, formName);
                }
            }
            System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY for role " + role + " and type " + type + "  is " + ps);
            res1 = ps.executeQuery();
            if (res1.next()) {
                cnt = res1.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY EXCEPTION " + e.getMessage());

            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 1: " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 2: " + e.getMessage());

                }
            }
//            if (con != null) {
//                try {
//                    // con.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 3: " + e.getMessage());
//
//                }
//            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  end of getcount function ");
        return cnt;
    }

    public List getCentralMinistry(UserData userdata) {

        System.out.println("inside get central ministry");
        List list = new ArrayList();
        List deptlist = new ArrayList();
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProfileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String ministry = userdata.getUserOfficialData().getMinistry();
        String employment = userdata.getUserOfficialData().getEmployment();
        String department = userdata.getUserOfficialData().getDepartment();
        String stateName = userdata.getUserOfficialData().getState();
        String postingState = userdata.getUserOfficialData().getPostingState();
        String organization = userdata.getUserOfficialData().getOrganizationCategory();
        String toBeSearched = "";
        //department ="Department of Expenditure (other than O/o CGA)";
        //System.out.println("dept::::::" + department);
        // department ="";

        try {

            stmt1 = conSlave.prepareStatement("SELECT department FROM department_not_showing_vpn");
            rs1 = stmt1.executeQuery();
            System.out.println("STATEMENT1" + stmt1);
            int i = 0;
            while (rs1.next()) {
                ++i;
                if (rs1.getString("department") != null && !rs1.getString("department").isEmpty()) {
                    deptlist.add(rs1.getString("department"));
                }
            }
           
            if (!(deptlist.contains(department)
                    || department.toLowerCase().equals("other") || department.contains("National Informatics "))) {

                if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                    toBeSearched = ministry;
                    stmt1 = conSlave.prepareStatement("SELECT distinct(emp_coord_email),emp_coord_name,emp_dept FROM vpn_coordinator where emp_min_state_org=? and emp_dept=? and emp_status='a'");
                    stmt1.setString(1, toBeSearched);
                    stmt1.setString(2, department);
                    rs1 = stmt1.executeQuery();
                    System.out.println("STATEMENT1" + stmt1);
                    i = 0;
                    while (rs1.next()) {
                        ++i;
                        if (rs1.getString("emp_coord_name") != null && !rs1.getString("emp_coord_name").isEmpty()) {
                            list.add(rs1.getString("emp_coord_name") + "(" + rs1.getString("emp_coord_email") + ")".trim());
                        } else {
                            list.add(rs1.getString("emp_coord_email"));
                        }
                    }

                    if (i <= 0) {
                        stmt1 = conSlave.prepareStatement("SELECT distinct(emp_coord_email),emp_coord_name,emp_dept FROM vpn_coordinator where emp_min_state_org=? and emp_status='a'");
                        stmt1.setString(1, toBeSearched);
                        System.out.println("STATEMENT2" + stmt1);
                        rs1 = stmt1.executeQuery();
                        while (rs1.next()) {
                            if (rs1.getString("emp_coord_name") != null && !rs1.getString("emp_coord_name").isEmpty()) {
                                list.add(rs1.getString("emp_coord_name") + "(" + rs1.getString("emp_coord_email") + ")".trim());
                            } else {
                                list.add(rs1.getString("emp_coord_email"));
                            }
                        }
                    }
                    System.out.println("stmt1" + stmt1);
                } else {
                    toBeSearched = organization;
                    stmt1 = conSlave.prepareStatement("SELECT distinct(emp_coord_email),emp_coord_name,emp_dept FROM vpn_coordinator where emp_min_state_org=? and emp_status='a'");
                    stmt1.setString(1, toBeSearched);
                    rs1 = stmt1.executeQuery();
                    System.out.println("STATEMENT3" + stmt1);
                    while (rs1.next()) {
                        if (!rs1.getString("emp_coord_name").equals("")) {
                            list.add(rs1.getString("emp_coord_name") + "(" + rs1.getString("emp_coord_email") + ")".trim());
                        } else {
                            list.add(rs1.getString("emp_coord_email"));
                        }
                    }
                }
            }
            System.out.println("LIST IN DAOO:: " + list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public List getStateCoordinator(UserData userValues) {
        List list = new ArrayList();
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProfileDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement stmt1 = null;
        ResultSet rs = null;

        String postingState = userValues.getUserOfficialData().getPostingState();
       
        try {
            stmt1 = conSlave.prepareStatement("SELECT distinct(emp_coord_email),emp_coord_name FROM vpn_coordinator where emp_min_state_org=? and emp_status='a'");
            stmt1.setString(1, postingState);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "PS: " + stmt1);
            rs = stmt1.executeQuery();
            while (rs.next()) {
                if (!rs.getString("emp_coord_name").equals("")) {
                    list.add(rs.getString("emp_coord_name") + "(" + rs.getString("emp_coord_email") + ")".trim());
                } else {
                    list.add(rs.getString("emp_coord_email"));
                }
            }
            System.out.println("LIST IN DAOO:: " + list);
        } catch (Exception e) {

        } finally {
            try {
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (con != null) {
                    // con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }


    public Set<String> fetchIdMapped(String email) {
        String returnString = "";
        System.out.println("inside dao check reuqest");
        Set<String> emailIdsCreated = new HashSet<>();
        //Connection con = null;
        try {
            //System.out.println("inside ldap condition");
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        try {

            String sql1 = "select stat_final_id from final_audit_track where applicant_email =? and form_name in ( 'single', 'nkn_single','nkn_bulk', 'bulk') and status ='completed'";
            ps1 = conSlave.prepareStatement(sql1);
            ps1.setString(1, email);
            System.out.println("ps1:::::::::" + ps1);
            rs1 = ps1.executeQuery();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "PSSSS: in user exist table " + ps1);
            while (rs1.next()) {
                emailIdsCreated.add(rs1.getString("stat_final_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs2 != null) {
                try {
                    rs2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return emailIdsCreated;
    }


    public Map getHodValues(String email) {
//        Hashtable ht = new Hashtable();
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);

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
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            ne = ctx.search("dc=nic,dc=in", searchFilter, sc);
            if (ne.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ne.nextElement();
                Attributes attrs = sr.getAttributes();

                String userdn = sr.getName() + ",dc=nic,dc=in";
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
                if (attrs.get("mobile") != null) {
                    String tmp = attrs.get("mobile").toString();
                    mobile_ldap = tmp.substring(8, tmp.length());
                    mobile_ldap = mobile_ldap.replaceAll("[-;.,\\s]", "");
                    if (mobile_ldap == null || mobile_ldap.equals("")) {
                        mobile_ldap = "";
                    }
                }
                if (attrs.get("mail") != null) {
                    String tmp = attrs.get("mail").toString();
                    email_ldap = tmp.substring(6, tmp.length());
                    email_ldap = email_ldap.replaceAll("[;,\\s]", "");
                    if (email_ldap == null || email_ldap.equals("")) {
                        email_ldap = "";
                    }
                }
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

                String final_name = "";

                if (initials.equals("")) {
                    final_name = cn;
                } else {
                    final_name = initials + " " + cn;
                }
                String ROendMobile = "", ROfinalString = "";
                if (!mobile_ldap.equals("")) {
                    System.out.println("mobile on profile" + mobile_ldap);
                    String ROstartMobile = mobile_ldap.substring(0, 3);

                    if (mobile_ldap.contains("+91")) {
                        ROendMobile = mobile_ldap.substring(10, 13);
                    } else if (mobile_ldap.startsWith("91")) {
                        ROendMobile = mobile_ldap.substring(10, 12);
                    } else {
                        ROendMobile = mobile_ldap.substring(mobile_ldap.length() - 4);
                        // endMobile = userdata.getMobile().substring(8, 10);
                    }
                    ROfinalString = ROstartMobile + "XXXXXXX" + ROendMobile;
                }
                ldapValues.put("mobile", ROfinalString.trim());
                ldapValues.put("email", email.trim());
                ldapValues.put("cn", final_name.trim());
                ldapValues.put("desig", desig.trim());
                //ldapValues.put("postalAddress", postalAddress.trim());
                ldapValues.put("ophone", ophone);
                ldapValues.put("bo", final_bo);
                ldapValues.put("uid", uid);
                ldapValues.put("source", "ldap");

                if (final_bo.equalsIgnoreCase("NIC Employees")) {
                    ldapValues.put("user_employment", "Central");
                    ldapValues.put("min", "Electronics and Information Technology");
                    ldapValues.put("dept", "National Informatics Centre");

                    //GET Reporting/Nodal/Forwarding Officer VALUES
                    String rep_off = "";

                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER IN LDAP");
            } else {
                // user not present in ldap, everything will be blank for the user(in this case set NA)  
                ldapValues.put("mobile", mobile_ldap);
                ldapValues.put("email", email);
                ldapValues.put("cn", cn);
                ldapValues.put("desig", desig);
                //ldapValues.put("postalAddress", postalAddress);
                ldapValues.put("ophone", ophone);
                ldapValues.put("bo", "no bo");
                ldapValues.put("uid", uid);
                ldapValues.put("error", "User Not valid");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "USER NOT IN LDAP");
            }
           // ht.clear();
        } catch (NamingException ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == SIGUNUPDAO == USERAGENT " + userAgent + " == " + "ex: " + ex.getMessage());
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

        return ldapValues;

    }



}
