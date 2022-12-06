
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;

/**
 *
 * @author SHIVAM
 */
public class A {

    Connection con = null;

    //Connection conSlave = null;
    private static final String dbhost = "localhost:3306";
    private static final String dbuser = "root";
    private static final String dbname = "onlineform";
    private static final String dbpass = "@Forms@123";

    private static final String dbhostSlave = "localhost:3306";
    private static final String dbuserSlave = "root";
    private static final String dbnameSlave = "onlineform";
    private static final String dbpassSlave = "root@123";

    private static final String url = "jdbc:mysql://" + dbhost + "/" + dbname + "?autoReconnect=true&useUnicode=true&characterEncoding=utf-8";

    private final String ctxfactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private final String providerurl = "ldap://auths.nic.in:389 ldap://auths2.nic.in:389";
    private final String binddn = "uid=eforms.auth,ou=People,o=inoc services,o=Application Services,o=nic.in,dc=nic,dc=in";
    private final String bindpass = "Ty@#12Qe";
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

            //System.out.println("CTX value : " + ctx);
        } catch (Exception e) {
            ctx = null;

        }
        //System.out.println("Final CTX : " + ctx);
        return ctx;

    }

    public static void main(String[] args) {

        A a = new A();
        a.fetchdata();

    }

    public String fetchdata() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conSlave = null;
        System.out.println("inside fetch data");
        //ldap = new Ldap();
        //System.out.print(ldap);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conSlave = (Connection) DriverManager.getConnection(url, dbuser, dbpass);
            // conSlave = DbConnection.getSlaveConnection();
            ps = conSlave.prepareStatement("select * from mobile_registration where datetime like '%2021-06-%'");
            //System.out.println("inside ps::::" + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                fetchMobileFromLdap(rs.getString("auth_email"));
                //System.out.println("email" + rs.getString("auth_email"));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return "test";

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

                ctx = getContext();
                //ctx = ldapCon.getContext();

                //ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020
            } //catch (ClassNotFoundException ex) {
            catch (Exception ex) {
                ex.printStackTrace();

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

        if (mobile_ldap.startsWith("+91+91")) {
            try {
                //mobile_ldap = mobile_ldap.substring(3, 16);
                PreparedStatement ps = null;
                int rs;
                Connection con = null;
                Class.forName("com.mysql.jdbc.Driver");
                con = (Connection) DriverManager.getConnection(url, dbuser, dbpass);

                ps = con.prepareStatement("insert into filtered_mobile_email (mobile,email) VALUES(?,?)");
                ps.setString(1, mobile_ldap);
                ps.setString(2, email);
                System.out.println("PS:::::::::::::"+ps);
                rs = ps.executeUpdate();
                System.out.println("mobile in ldap" + mobile_ldap.substring(3, 16));

            } catch (Exception e) {

            }

        }

        return mobile_ldap;
    }

}
