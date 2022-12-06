
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author DJ
 */
public class UserRoLdapUpdate {

    private static Connection con = null;
    private static Connection conSlave = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;
    private static Statement st = null;
    private static DirContext ctx = null;
    private static ModificationItem[] mods;
    static String providerurl = "ldap://10.122.34.101:389";
    static String binddn = "uid=nikki.nhq,ou=People,o=NIC Support Outsourced,o=NIC Support,o=nic.in,dc=nic,dc=in";
    //static String bindpass = "#@XDr#=V@#Dafqr@#mtF==2KeV@#DafnDaf=qrtFWDr=#@Jq==r@#mtFpQFWDrH@#WD==r#fT==vB3nH$@#Wp==QFWDrH@#WDr#nDafqrt=3d=@#";
    static String bindpass = "Nikki@389";
    String dbname = "onlineform_test";
    String db_pass = "";
    String dbdriver = "com.mysql.jdbc.Driver";
    String dburl = "jdbc:mysql://localhost:3306/";
    String db_user = "root";
    
    //on 19-Jan-2022
    String dbnameSlave = "onlineform";
    String db_passSlave = "Slave#$%123";

    String dburlSlave = "jdbc:mysql://192.168.1.100:3306/";
    String db_userSlave = "slave_user";

    static String smtphost = "relay.nic.in";
    static String mailfrom = "no-reply@nic.in";
    static String uid, mail, mobile, cn, title, expdate, ophone, address, alias, tmp, dbrep_mail, tl, dn, stdcode;
    static boolean flag;
    //Rahul's changes start
    static List<String> userWithoutRepOfficers = new ArrayList<String>();
    static List<Map<String, String>> userWithEmptyUserEmail = new ArrayList<>();
    static List<Map<String, Object>> userWithInvalidMobile = new ArrayList<>();
    //Rahul's changes finished

    // constructor call for getting DB connection
    public UserRoLdapUpdate() {
        try {
            Class.forName(dbdriver);
            con = DriverManager.getConnection(dburl + dbname, db_user, db_pass);
            conSlave = DriverManager.getConnection(dburlSlave + dbnameSlave, db_userSlave, db_passSlave); 
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        UserRoLdapUpdate urlu = new UserRoLdapUpdate();
        t.start();
    }

    public static Thread t = new Thread() {

        public void run() {

            try {
                Map<String, Object> ldapmap = ldap_Connection(); // this map contains directoryConetext and SearchControl objects
                Map<String, String> map = new HashMap<>();
                List<Map<String, String>> allOadData = new ArrayList<>();
                Map temp1 = new HashMap<>();
                Map<String, Map> allUserLmapData = new HashMap<>();
                NamingEnumeration ne = null;
                SearchControls sc = null;
                Map olduser_lmap = new HashMap();
                Map ldapmodify = new HashMap(); // Used to keep attributes and values which need to be updated on LDAP
                StringBuilder sb = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                // Map ldap_Values = new HashMap();
                //Map roldap_Values = new HashMap();
                //Set userequival = new HashSet();
                Set roequival = new HashSet();
                Attributes attrs = null;
                javax.naming.directory.SearchResult sr = null;
                String base_dn = "dc=nic,dc=in";
                String searchFilter1, searchFilter2;

                File file = new File("C:\\Users\\SHIVAM\\Downloads\\OAD.xml");
//an instance of factory that gives a document builder  
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//an instance of builder to parse the specified xml file  
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(file);
                doc.getDocumentElement().normalize();
                //Document doc = apiXmlParser("EMD");                                     //now call API to get details
                System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getElementsByTagName("Detail");

                if (ldapmap != null && !ldapmap.containsKey("error")) {
                    ctx = (DirContext) ldapmap.get("dircontext");
                    sc = (SearchControls) ldapmap.get("search");

                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Map user_lmap = new HashMap(); //Local map to keep attributes and values which we are updating in LDAP
                        Map ro_lmap = new HashMap();
                        Map ldap_Values = new HashMap();
                        Map roldap_Values = new HashMap();
                        Set userequival = new HashSet();
                        Node nNode = nList.item(temp);

                        try {
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                map.put("user_mail", eElement.getElementsByTagName("email_id").item(0).getTextContent());
                                map.put("user_employeeNumber", eElement.getElementsByTagName("emp_code").item(0).getTextContent());
                                map.put("user_title", eElement.getElementsByTagName("emp_title").item(0).getTextContent());
                                map.put("user_name", eElement.getElementsByTagName("emp_name").item(0).getTextContent());
                                map.put("user_dob", eElement.getElementsByTagName("date_of_birth").item(0).getTextContent());
                                map.put("user_status", eElement.getElementsByTagName("emp_status").item(0).getTextContent());
                                map.put("user_design", eElement.getElementsByTagName("emp_desig").item(0).getTextContent());
                                map.put("user_placeofposting", eElement.getElementsByTagName("place_of_posting").item(0).getTextContent());
                                map.put("user_postedstate", eElement.getElementsByTagName("state_posted").item(0).getTextContent());
                                map.put("user_mobile", eElement.getElementsByTagName("mobile").item(0).getTextContent());
                                map.put("stdcode", eElement.getElementsByTagName("stdcode").item(0).getTextContent());
                                map.put("user_telephone", eElement.getElementsByTagName("contactno").item(0).getTextContent());
                                map.put("rep_title", eElement.getElementsByTagName("reporting_off_title").item(0).getTextContent());
                                map.put("rep_name", eElement.getElementsByTagName("reporting_off_name").item(0).getTextContent());
                                map.put("rep_empcode", eElement.getElementsByTagName("reporting_off_code").item(0).getTextContent());
                                map.put("rep_mail", eElement.getElementsByTagName("reporting_off_email").item(0).getTextContent());
                                map.put("rep_postedstate", eElement.getElementsByTagName("rep_state_posted").item(0).getTextContent());
                                map.put("hog_title", eElement.getElementsByTagName("hog_title").item(0).getTextContent());
                                map.put("hog_name", eElement.getElementsByTagName("hog_name").item(0).getTextContent());
                                map.put("hog_code", eElement.getElementsByTagName("hog_code").item(0).getTextContent());
                                map.put("hog_email", eElement.getElementsByTagName("hog_email").item(0).getTextContent());
                                map.put("hod_title", eElement.getElementsByTagName("hod_title").item(0).getTextContent());
                                map.put("hod_name", eElement.getElementsByTagName("hod_name").item(0).getTextContent());
                                map.put("hod_code", eElement.getElementsByTagName("hod_code").item(0).getTextContent());
                                map.put("hod_email", eElement.getElementsByTagName("hod_email").item(0).getTextContent());
                                map.put("user_is", eElement.getElementsByTagName("is_dio").item(0).getTextContent());         //check the attribute value
                                //System.out.println("Map value fetched from OAD : for user having email = " + map.get("user_mail") + "  ::: Map Value ::: " + map);
                                if (map.get("user_mail") != null && !map.get("user_mail").isEmpty() && !map.get("user_mail").equals("admin.tn@nic.in")) {

                                    try {
                                        //Ldap Search for User and RO details
                                        if (!map.get("rep_mail").isEmpty()) {     //check reporting email is not null and empty
                                            searchFilter1 = "(|(mail=" + map.get("user_mail") + ")(mailequivalentaddress=" + map.get("user_mail") + ")(uid=" + map.get("user_mail") + "))";
                                            ne = ctx.search(base_dn, searchFilter1, sc);

                                            if (ne.hasMoreElements()) {
                                                tl = "";
                                                dn = "";
                                                sr = (javax.naming.directory.SearchResult) ne.nextElement();
                                                tl = sr.getName();
                                                dn = tl + "," + base_dn;
                                                attrs = sr.getAttributes();
                                                //Fetching existing details of the user from LDAP
                                                user_lmap = search_Ldap(attrs, ldap_Values, userequival);
                                                System.out.println("User existing details in LDAP :: " + user_lmap);
                                                if (!olduser_lmap.isEmpty()) {
                                                    olduser_lmap.clear();
                                                }
                                                //olduser_lmap contains the existing or old details of user in LDAP
                                                olduser_lmap.putAll(user_lmap);
                                                System.out.println("DN :: value=" + dn + " ::: User MAP Value :::::::::::::::::::::::::::::::::::::::::: ==>>" + user_lmap);
                                            }

                                            //Start Ldap modify code
                                            if (!ldapmodify.isEmpty()) {
                                                ldapmodify.clear();
                                            }

                                            //comparing existing values in LDAP and OAD, if there is mismatch in mobile number, name and designation, same has to be updated in LDAP too
                                            mobile = "";
                                            mobile = map.get("user_mobile");

                                            if (StringUtils.isNotBlank(mobile) && !user_lmap.get("mobile").toString().equalsIgnoreCase("+91" + mobile) && mobile.matches("^[0-9]{10}$")) {
                                                ldapmodify.put("mobile", "+91" + map.get("user_mobile"));
                                                user_lmap.put("mobile", map.get("user_mobile"));
                                            }

                                            cn = "";
                                            cn = map.get("user_name");
                                            if (StringUtils.isNotBlank(cn) && !user_lmap.get("cn").toString().equalsIgnoreCase(cn)) {
                                                ldapmodify.put("cn", map.get("user_name"));
                                                user_lmap.put("cn", map.get("user_name"));
                                            }

                                            title = "";
                                            title = map.get("user_design");
                                            if (StringUtils.isNotBlank(title) && !user_lmap.get("designation").toString().equalsIgnoreCase(title)) {
                                                ldapmodify.put("title", map.get("user_design"));
                                                user_lmap.put("designation", map.get("user_design"));
                                            }

                                            ophone = "";
                                            ophone = map.get("user_telephone");
                                            if (!map.get("user_telephone").equals("")) {
                                                if (!map.get("stdcode").equals("")) {
                                                    if (map.get("stdcode").contains("11")) {
                                                        map.put("stdcode", "0" + map.get("stdcode"));
                                                    }
                                                    ophone = map.get("stdcode") + "-" + map.get("user_telephone");
                                                } else {
                                                    ophone = map.get("user_telephone");
                                                }

                                            }
                                            if (StringUtils.isNotBlank(ophone) && !user_lmap.get("ophone").toString().equalsIgnoreCase(ophone)) {
                                                user_lmap.put("ophone", ophone);
                                            }
                                            user_lmap.put("email_from_oad", map.get("user_mail").toString());
                                            System.out.println("user_lmap ::: " + user_lmap);
                                            allUserLmapData.put(map.get("user_mail").toString(), user_lmap);
                                            System.out.println("allUserLmapData ::: " + allUserLmapData);
                                            // Here user_lmap contains the latest values of user, fetched from OAD
                                            if (!ldapmodify.isEmpty()) {
                                                System.out.println("Map value ::: " + ldapmodify);
                                                System.out.println("USER_LMAP :- " + user_lmap);
                                                System.out.println("OLDUSER_LMAP :-" + olduser_lmap);
                                                System.out.println("LDAP MODIFY :- " + ldapmodify);
                                                flag = ldap_Modify(ldapmodify, ctx, dn, olduser_lmap, sb, sb1);
                                                System.out.println("Finally User Ldap Map Value NOW :::::::::::::::::::::::::::::::==> " + user_lmap);
                                            }

                                            //End Ldap modify code
                                            //Code to get RO details from LDAP
                                            searchFilter2 = "(|(mail=" + map.get("rep_mail") + ")(mailequivalentaddress=" + map.get("rep_mail") + ")(uid=" + map.get("rep_mail") + "))";
                                            ne = ctx.search(base_dn, searchFilter2, sc);

                                            if (ne.hasMoreElements()) {
                                                sr = (javax.naming.directory.SearchResult) ne.nextElement();
                                                attrs = sr.getAttributes();
                                                ro_lmap = search_Ldap(attrs, roldap_Values, roequival);
                                                System.out.println("RO MAP Value ::::::::::::::::::::::::::::::::::::::::::::: ==>> " + ro_lmap);
                                            }
                                            // map => OAD data
                                            // user_lmap => contains latest data (few from OAD(mobile, cn, designation, ophone) and rest from LDAP)
                                            // ro_lmap => conatins data from ldap only (cn, mobile, designation, telephone etc)
                                            insert_tempTable(con, map, user_lmap, ro_lmap);

                                        } // Rahul's changes start
                                        else {
                                            if (!map.get("user_mail").equalsIgnoreCase("dg@nic.in")) {
                                                userWithoutRepOfficers.add(map.get("user_mail"));
                                            }
                                        }

                                        //Rahul's changes finished
                                    } catch (Exception ex) {
                                        System.out.println("Exception in reporting Officer API hit for UID :::");
                                        ex.printStackTrace();
                                        t.sleep(5000);
                                    }

                                } else {
                                    userWithEmptyUserEmail.add(map);
                                }
                                // for user mail is null from api
                                //temp1.putAll(map);
                                //if (map != null || !map.isEmpty()) {
                                //    map.clear();
                                //}
                            }
                        } catch (Exception ex) {
                            System.out.println(" :::::::::::::::::::: Exception :::: :::::: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                        //allOadData.add(temp1);
                        allOadData.add(map);
                        //System.out.println("List of OAD data ::: " + allOadData);
                    }
                    System.out.println("size of allOadData " + allOadData.size());
                    for (Map<String, String> map1 : allOadData) {
                        System.out.println("map1:::::::::::::::::::::::::::::::::: " + map1);

                        Map user_lmap = new HashMap();
                        //user_lmap.clear();
                        if (map1.get("user_mail") != null && !map1.get("user_mail").isEmpty() && !map1.get("user_mail").equals("admin.tn@nic.in") && !map1.get("rep_mail").isEmpty()) {
                            System.out.println(" Inside if block ..." + map1.get("user_mail"));
                            System.out.println(" Inside if block allUserLmapData size " + allUserLmapData.size());
                            user_lmap = allUserLmapData.get(map1.get("user_mail"));
                            System.out.println("MAP1 beforeupdate_tempTableWithRoData " + map1 + "      " + "user_lmap" + user_lmap);
                            if (user_lmap != null) {
                                update_tempTableWithRoData(con, map1, user_lmap);
                                UserProfile_UpdateUser(con, map1, user_lmap);
                                UserProfile_UpdateRo(con, map1, user_lmap);
                            }
                        }
                    }

                    insert_BackupandoriginalTable(con);
                } else {
                    System.out.println("Ldap connection problem " + ldapmap.get("error"));
                }
                if (userWithoutRepOfficers.size() > 0) {
                    send_Mail_OAD_Team(userWithoutRepOfficers);
                }
                if (userWithEmptyUserEmail.size() > 0) {
                    send_Mail_OAD_Team_For_Blank_UserEmail(userWithEmptyUserEmail);
                }
                if (userWithInvalidMobile.size() > 0) {
                    send_Mail_OAD_Team_For_Blank_Mobile(userWithInvalidMobile);
                }
                sendNotificatioMail();

            } catch (Exception ex) {

                try {
                    /*ps = con.prepareStatement("INSERT INTO exception (user_mail,exception,method)"
                            + "values(?,?,?)");
                    ps.setString(1, "");
                    ps.setString(2, ex.getMessage());
                    ps.setString(3, "insert3");
                    ps.executeUpdate();*/
                } catch (Exception e) {

                }
                System.out.println("Exception :::: in finally1111111 :::" + ex.getMessage() + "\n\n");
                ex.printStackTrace();
            } finally {
                try {
                    ctx.close();
                    rs.close();
                    ps.close();
                    con.close();

                } catch (Exception ex) {
                    try {
                        /*ps = con.prepareStatement("INSERT INTO exception (user_mail,exception,method)"
                                + "values(?,?,?)");
                        ps.setString(1, "");
                        ps.setString(2, ex.getMessage());
                        ps.setString(3, "insert4");
                        ps.executeUpdate();*/
                    } catch (Exception e) {

                    }
                    System.out.println("Exception :::: in finally22222 :::" + ex.getMessage() + "\n\n");
                    ex.printStackTrace();
                }
            }
        }

    };

    // Ldap Search
    public static Map search_Ldap(Attributes attrs, Map ldap_Values, Set equi) {
        if (!ldap_Values.isEmpty()) {
            ldap_Values.clear();
        }

        if (!equi.isEmpty()) {
            equi.clear();
        }
        uid = "";
        mail = "";
        mobile = "";
        cn = "";
        title = "";
        ophone = "";
        address = "";
        alias = "";
        tmp = "";

        if (attrs.get("uid") != null) {
            tmp = attrs.get("uid").toString();
            uid = tmp.substring(5, tmp.length());
            if (uid == null || uid.equals("")) {
                uid = "";
            }
        }

        tmp = "";
        if (attrs.get("mail") != null) {
            tmp = attrs.get("mail").toString();
            mail = tmp.substring(6, tmp.length());
            if (mail == null || mail.equals("")) {
                mail = "";
            }
        }

        tmp = "";
        if (attrs.get("mobile") != null) {
            tmp = attrs.get("mobile").toString();
            mobile = tmp.substring(8, tmp.length());
            if (mobile == null || mobile.equals("")) {
                mobile = "";
            }
        }

        tmp = "";
        if (attrs.get("cn") != null) {
            tmp = attrs.get("cn").toString();
            cn = tmp.substring(4, tmp.length());
            if (cn == null || cn.equals("")) {
                cn = "";
            }
        }

        tmp = "";
        if (attrs.get("title") != null) {
            tmp = attrs.get("title").toString();
            title = tmp.substring(7, tmp.length());
            if (title == null || title.equals("")) {
                title = "";
            }
        }

        tmp = "";
        if (attrs.get("telephoneNumber") != null) {
            tmp = attrs.get("telephoneNumber").toString();
            ophone = tmp.substring(17, tmp.length());
            //ophone = ophone.replaceAll("[;.,\\s]", "");
            if (ophone.contains(",")) {
                String token[] = ophone.split(",");
                ophone = token[0];
            }
            if (ophone == null || ophone.equals("")) {
                ophone = "";
            }
        }

        tmp = "";
        if (attrs.get("postaladdress") != null) {
            tmp = attrs.get("postaladdress").toString();
            address = tmp.substring(15, tmp.length());
            address = address.replaceAll("[-;.,]", "");
            if (address == null || address.equals("")) {
                address = "";
            }
        }

        tmp = "";
        if (attrs.get("mailequivalentaddress") != null) {
            tmp = attrs.get("mailequivalentaddress").toString();
            alias = tmp.substring(23, tmp.length());
            if (alias == null || alias.equals("")) {
                alias = "";
            }
        }
        equi.add(mail);
        if (alias.contains(",")) {
            String token[] = alias.split(",");
            for (int i = 0; i < token.length; i++) {
                equi.add(token[i].trim());
            }
        } else {
            if (!alias.equals("")) {
                equi.add(alias.trim());
            }
        }

        ldap_Values.put("uid", uid);
        ldap_Values.put("mail", mail);
        ldap_Values.put("mobile", mobile);
        ldap_Values.put("cn", cn);
        ldap_Values.put("designation", title);
        ldap_Values.put("ophone", ophone);
        ldap_Values.put("address", address);
        ldap_Values.put("alias", equi);
        return ldap_Values;

    }

    // for Creating LDAP Connection
    public static Map ldap_Connection() throws NamingException {
        Map<String, Object> map = new HashMap<String, Object>();
        DirContext ctx = null;
        try {

            Hashtable ht = new Hashtable(4);
            ht.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ht.put(Context.PROVIDER_URL, providerurl);
            ht.put(Context.SECURITY_PRINCIPAL, binddn);
            ht.put(Context.SECURITY_CREDENTIALS, bindpass);
            ctx = new InitialDirContext(ht);
            map.put("dircontext", ctx);
            String[] attr = {"uid", "mail", "mobile", "cn", "title", "postaladdress", "telephoneNumber", "mailequivalentaddress"};
            SearchControls sc = new SearchControls();
            sc.setReturningAttributes(attr);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
            map.put("search", sc);

        } catch (Exception e) {
            if (ctx != null) {
                ctx.close();
            }
            System.out.println("Ldap connection error :::::");
            map.put("error", "Ldap connection failuer please try again ::::");
        }

        return map;
    }

    // for insertion into temp table
    //user_lmap contains details of the users from OAD (and LDAP)
    //ro_lmap contains details of the RO from OAD (As we are taking data from OAD and updating it in LDAP first)
    public static boolean insert_tempTable(Connection con, Map map, Map user_lmap, Map ro_lmap) { //for reporting_officer check or updating
        boolean flag = false;
        try {
            ps = con.prepareStatement("insert into temp_userandreporting (user_uid, user_mail, user_empcode, user_title, user_name, "
                    + "user_design, user_mobile, user_telephone, user_address, user_dob, "
                    + "user_placeofposting, user_postedstate, user_status, user_is, rep_mail, "
                    + "rep_title, rep_name, rep_design, rep_mobile, rep_telephone, rep_empcode, rep_postedstate, "
                    + "hog_title, hog_name, hog_code, hog_email, hod_title, hod_name, hod_code, hod_email)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            ps.setString(1, user_lmap.get("uid").toString());
            ps.setString(2, map.get("user_mail").toString());
            ps.setString(3, map.get("user_employeeNumber").toString());
            ps.setString(4, map.get("user_title").toString());
            ps.setString(5, map.get("user_name").toString());
            ps.setString(6, map.get("user_design").toString());
            if (map.get("user_mobile").toString().length() == 10) {
                ps.setString(7, "+91" + map.get("user_mobile").toString());
            } else if (map.get("user_mobile").toString().startsWith("+91")) {
                ps.setString(7, map.get("user_mobile").toString());
            } else if (map.get("user_mobile").toString().startsWith("91")) {
                ps.setString(7, "+" + map.get("user_mobile").toString());
            } else {
                //userWithInvalidMobile.put(map.get("user_mail").toString(),map);
                userWithInvalidMobile.add(map);
                return false;
            }
            ps.setString(8, user_lmap.get("ophone").toString());
            ps.setString(9, user_lmap.get("address").toString());
            ps.setString(10, map.get("user_dob").toString());
            ps.setString(11, map.get("user_placeofposting").toString());
            ps.setString(12, map.get("user_postedstate").toString());
            ps.setString(13, map.get("user_status").toString());
            ps.setString(14, map.get("user_is").toString());
            ps.setString(15, map.get("rep_mail").toString());
            ps.setString(16, map.get("rep_title").toString());
            ps.setString(17, map.get("rep_name").toString());
            ps.setString(18, ro_lmap.get("designation").toString());
            ps.setString(19, ro_lmap.get("mobile").toString());
            ps.setString(20, ro_lmap.get("ophone").toString());
            ps.setString(21, map.get("rep_empcode").toString());
            ps.setString(22, map.get("rep_postedstate").toString());
            ps.setString(23, map.containsKey("hog_title") ? map.get("hog_title").toString() : "NA");
            ps.setString(24, map.containsKey("hog_name") ? map.get("hog_name").toString() : "NA");
            ps.setString(25, map.containsKey("hog_code") ? map.get("hog_code").toString() : "NA");
            ps.setString(26, map.containsKey("hog_email") ? map.get("hog_email").toString() : "NA");
            ps.setString(27, map.containsKey("hod_title") ? map.get("hod_title").toString() : "NA");
            ps.setString(28, map.containsKey("hod_name") ? map.get("hod_name").toString() : "NA");
            ps.setString(29, map.containsKey("hod_code") ? map.get("hod_code").toString() : "NA");
            ps.setString(30, map.containsKey("hod_email") ? map.get("hod_email").toString() : "NA");
            ps.executeUpdate();
            System.out.println("data inserted temp_userandreporting table sucessfully as :" + ps);

        } catch (Exception ex) {
            try {
                ps = con.prepareStatement("INSERT INTO exception (user_mail,exception,method)"
                        + "values(?,?,?)");
                ps.setString(1, map.get("user_mail").toString());
                ps.setString(2, ex.getMessage());
                ps.setString(3, "insert_tempTable");
                ps.executeUpdate();
                userWithInvalidMobile.add(map);
            } catch (Exception e) {

            }
            System.out.println("Exception in insertion insert_tempTable ::::::::::::  USER mail ::::" + map.get("user_mail").toString() + " :::::::::::::::::: " + ex);
        }

        return flag;
    }

    public static boolean update_tempTableWithRoData(Connection con, Map map, Map user_lmap) {
        try {
//            ps = con.prepareStatement("select user_design,user_mobile,user_telephone from temp_userandreporting where user_mail = ?");
            ps = conSlave.prepareStatement("select user_design,user_mobile,user_telephone from temp_userandreporting where user_mail = ?");

            ps.setString(1, map.get("rep_mail").toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                String update = "update temp_userandreporting set rep_design=?,rep_mobile=?,rep_telephone=? where rep_mail = ?";
                ps = con.prepareStatement(update);
                ps.setString(1, rs.getString("user_design"));
                ps.setString(2, rs.getString("user_mobile"));
                ps.setString(3, rs.getString("user_telephone"));
                ps.setString(4, map.get("rep_mail").toString());
                System.out.println("data updated sucessfully as" + ps);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean UserProfile_UpdateUser(Connection con, Map map, Map user_lmap) {
        System.out.println("inside user profile update user:::::::::::::::::::");

        flag = false;
        dbrep_mail = "";
        String cdate = "";
        Set ml = (Set) user_lmap.get("alias");
        String emailInOad = user_lmap.get("email_from_oad").toString();
        Calendar cal = Calendar.getInstance();
        cdate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        System.out.println("Alaises ::::::::::::::::::::userprofileupdate::::::::::::::" + ml);
        try {
            for (Object a : ml) {
                //ps = con.prepareStatement("select * from user_profile where email = ?");
                 ps = conSlave.prepareStatement("select * from user_profile where email = ?");
                ps.setString(1, a.toString());
                // System.out.println("select query::::::::::::::::::::::::::::::::::::" + ps);
                rs = ps.executeQuery();
                if (rs.next()) {
                    dbrep_mail = a.toString();
                    System.out.println("USER Email found in user_profile table:::" + dbrep_mail);
                    //for update backup
                    ps = con.prepareStatement("INSERT INTO backup_userprofile(email,mobile,name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,other_dept,state,organization,ca_name,ca_desig,ca_mobile,ca_email,hod_name,hod_email,hod_mobile,hod_telephone,userip,user_alt_address,under_sec_name,under_sec_desig,under_sec_mobile,under_sec_email,under_sec_telephone) SELECT email,mobile,name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,other_dept,state,organization,ca_name,ca_desig,ca_mobile,ca_email,hod_name,hod_email,hod_mobile,hod_telephone,userip,user_alt_address,under_sec_name,under_sec_desig,under_sec_mobile,under_sec_email,under_sec_telephone FROM user_profile where email = ?");
                    ps.setString(1, dbrep_mail);
                    ps.executeUpdate();
                    System.out.println(" row(s)affected into backup_userprofile with email :::" + dbrep_mail);
                    break;

                }
            }
            if (dbrep_mail != null && !dbrep_mail.isEmpty()) {
                System.out.println("updating user_profile for :::" + dbrep_mail);
              //  ps = con.prepareStatement("select rep_mail,rep_name,rep_design,rep_telephone,rep_mobile,user_name from temp_userandreporting where user_mail = ?");
                ps = conSlave.prepareStatement("select rep_mail,rep_name,rep_design,rep_telephone,rep_mobile,user_name from temp_userandreporting where user_mail = ?");
                ps.setString(1, emailInOad);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String qry = "UPDATE user_profile SET ca_name = ?, ca_desig=?, ca_mobile=?, ca_email=?, hod_name=?, hod_email=?, hod_mobile=?, hod_telephone=?, mobile=?, name=?, designation=?  WHERE email = ?";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, rs.getString("rep_name"));
                    ps.setString(2, rs.getString("rep_design"));
                    ps.setString(3, rs.getString("rep_mobile"));
                    ps.setString(4, rs.getString("rep_mail"));
                    ps.setString(5, rs.getString("rep_name"));
                    ps.setString(6, rs.getString("rep_mail"));
                    ps.setString(7, rs.getString("rep_mobile"));
                    ps.setString(8, rs.getString("rep_telephone"));
                    ps.setString(9, "+91" + map.get("user_mobile").toString());
                    ps.setString(10, map.get("user_name").toString());
                    ps.setString(11, map.get("user_design").toString());
                    ps.setString(12, dbrep_mail);
                    int i = ps.executeUpdate();
                    if (i > 0) {

                        System.out.println("Finally user_profile table update for User in user_profile ::" + dbrep_mail + cdate + " Query detials :::" + ps);
                        ps = con.prepareStatement("INSERT INTO reporting_officer_by_script(user_mail,rep_mail,rep_name,rep_design,rep_mobile,rep_telephone,datetime)"
                                + "values(?,?,?,?,?,?,?)");
                        ps.setString(1, dbrep_mail);
                        ps.setString(2, rs.getString("rep_mail"));
                        ps.setString(3, rs.getString("rep_name"));
                        ps.setString(4, rs.getString("rep_design"));
                        ps.setString(5, rs.getString("rep_mobile"));
                        ps.setString(6, rs.getString("rep_telephone"));
                        ps.setString(7, cdate);
                        ps.executeUpdate();
                    }

                }
                // System.out.println("Finally user_profile table update for User in reporting_officer_by_script::" + dbrep_mail + cdate+" Query detials :::" + ps);
            }

        } catch (Exception ex) {
            System.out.println("Exception in user_profile update ::::::::::::::::::;" + ex.getMessage());
            try {
                ps = con.prepareStatement("INSERT INTO exception (user_mail,exception,method)"
                        + "values(?,?,?)");
                ps.setString(1, dbrep_mail);
                ps.setString(2, ex.getMessage());
                ps.setString(3, "UserProfile_UpdateUser");
                ps.executeUpdate();
            } catch (Exception e) {

            }

            ex.printStackTrace();
        }

        return flag;
    }

    public static boolean UserProfile_UpdateRo(Connection con, Map map, Map user_lmap) {
        System.out.println("inside user profile update ro:::::::::::::::::::");

        flag = false;
        dbrep_mail = "";
        Set ml = (Set) user_lmap.get("alias");
        String emailInOad = user_lmap.get("email_from_oad").toString();
        System.out.println("Alaises ::::::::::::::::::::userprofileupdateRO::::::::::::::" + ml);
        try {
            for (Object a : ml) {
              //  ps = con.prepareStatement("select * from user_profile where hod_email = ?");
                 ps = conSlave.prepareStatement("select * from user_profile where hod_email = ?");
                ps.setString(1, a.toString());
                rs = ps.executeQuery();

                if (rs.next()) {
                    dbrep_mail = a.toString();
                    System.out.println("USER Email found in user_profile table:::" + dbrep_mail);
                    //for update backup
                    ps = con.prepareStatement("INSERT INTO backup_userprofile(email,mobile,name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,other_dept,state,organization,ca_name,ca_desig,ca_mobile,ca_email,hod_name,hod_email,hod_mobile,hod_telephone,userip,user_alt_address,under_sec_name,under_sec_desig,under_sec_mobile,under_sec_email,under_sec_telephone) SELECT email,mobile,name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,other_dept,state,organization,ca_name,ca_desig,ca_mobile,ca_email,hod_name,hod_email,hod_mobile,hod_telephone,userip,user_alt_address,under_sec_name,under_sec_desig,under_sec_mobile,under_sec_email,under_sec_telephone FROM user_profile where email = ?");
                    ps.setString(1, dbrep_mail);
                    ps.executeUpdate();
                    System.out.println(" row(s)affected into backup_userprofile with email :::" + dbrep_mail);
                    break;

                }
            }

            if (dbrep_mail != null && !dbrep_mail.equals("")) {
                System.out.println("updating user_profile for :::" + dbrep_mail);

              //  ps = con.prepareStatement("select user_name,user_design,user_telephone,user_mobile from temp_userandreporting where user_mail = ?");
                 ps = conSlave.prepareStatement("select user_name,user_design,user_telephone,user_mobile from temp_userandreporting where user_mail = ?");
                ps.setString(1, emailInOad);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String qry = "UPDATE user_profile SET ca_name = ?, ca_desig=?, ca_mobile=?, hod_name=?, hod_mobile=?, hod_telephone=?  WHERE hod_email = ?";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, rs.getString("user_name"));
                    ps.setString(2, rs.getString("user_design"));
                    ps.setString(3, rs.getString("user_mobile"));
                    ps.setString(4, rs.getString("user_name"));
                    ps.setString(5, rs.getString("user_mobile"));
                    ps.setString(6, rs.getString("user_telephone"));
                    ps.setString(7, dbrep_mail);
                    ps.executeUpdate();
                }
                System.out.println("Finally user_profile table update for User ::" + dbrep_mail + " Query detials :::" + ps);
            }

        } catch (Exception ex) {
            System.out.println("Exception in user_profile update UserProfile_UpdateRo ::::::::::::::::::;" + ex.getMessage());
            try {
                ps = con.prepareStatement("INSERT INTO exception (user_mail,exception,method)"
                        + "values(?,?,?)");
                ps.setString(1, dbrep_mail);
                ps.setString(2, ex.getMessage());
                ps.setString(3, "UserProfile_UpdateRo");
                ps.executeUpdate();
            } catch (Exception e) {

            }
            ex.printStackTrace();
        }

        return flag;
    }

    // for insertion into temp table
    public static void insert_BackupandoriginalTable(Connection con) {
        try {
            st = con.createStatement();

            int rows = st.executeUpdate("INSERT INTO backup_userandreporting SELECT * FROM nicemployee_reporting");
            if (rows == 0) {
                System.out.println("Don't add any row!");
            } else {
                System.out.println(rows + " row(s)affected into backup_userandreporting.");

            }

            st.executeUpdate("TRUNCATE nicemployee_reporting");

            int row = st.executeUpdate("INSERT INTO nicemployee_reporting SELECT * FROM temp_userandreporting");
            if (row == 0) {
                System.out.println("Don't add any row!");
            } else {
                System.out.println(row + " row(s)affected into nicemployee_reporting.=============================================================================INSERT INTO nicemployee_reporting SELECT * FROM temp_userandreporting");

            }
            st.executeUpdate("TRUNCATE temp_userandreporting");
        } catch (Exception ex) {
            System.out.println("exception in backup mathod" + ex.getMessage());
            ex.printStackTrace();
            //System.out.println("exception in backup mathod");
        }

    }

    // for LDAP Modification
    public static boolean ldap_Modify(Map map, DirContext ctx, String dn, Map oldmap, StringBuilder sb, StringBuilder sb1) throws NamingException {
        flag = false;
        sb.setLength(0);
        sb1.setLength(0);
        try {

            mods = new ModificationItem[map.size()];
            Set set = map.entrySet();
            Iterator itr = set.iterator();
            for (int i = 0; i < map.size(); i++) {
                if (itr.hasNext()) {
                    Map.Entry pair = (Map.Entry) itr.next();
                    String key = (String) pair.getKey();
                    String value = (String) pair.getValue();
                    System.out.println("Key :" + key + " :::: Value :" + value);

                    mods[i] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(key, value));

                    if (key.equalsIgnoreCase("mobile")) {
                        sb1.append("<tr><td>Mobile Number</td><td>" + oldmap.get("mobile").toString() + "</td><td>" + value + "</td></tr>").append("</br>");

                    } else if (key.equalsIgnoreCase("cn")) {
                        sb1.append("<tr><td>Name</td><td>" + oldmap.get("cn").toString() + "</td><td>" + value + "</td></tr>").append("</br>");

                    } else if (key.equalsIgnoreCase("title")) {
                        sb1.append("<tr><td>Designation</td><td>" + oldmap.get("designation").toString() + "</td><td>" + value + "</td></tr>").append("</br>");

                    } else if (key.equalsIgnoreCase("telephonenumber")) {
                        sb1.append("<tr><td>Telephone Number</td><td>" + oldmap.get("ophone").toString() + "</td><td>" + value + "</td></tr>").append("</br>");

                    }

                }
            }
            ctx.modifyAttributes(dn, mods);
            System.out.println("Modify sucessfully now :::::" + ctx);

            try {
                ps = con.prepareStatement("insert into ldap_userprofile (uid, email, oldmobile, newmobile, oldname, newname, olddesignation, newdesignation, oldtelephonenumber, newtelephonenumber)"
                        + "values(?,?,?,?,?,?,?,?,?,?)");
                ps.setString(1, oldmap.get("uid").toString());
                ps.setString(2, oldmap.get("mail").toString());
                ps.setString(3, oldmap.get("mobile").toString());
                ps.setString(4, map.containsKey("mobile") ? map.get("mobile").toString() : "NA");
                ps.setString(5, oldmap.get("cn").toString());
                ps.setString(6, map.containsKey("cn") ? map.get("cn").toString() : "NA");
                ps.setString(7, oldmap.get("designation").toString());
                ps.setString(8, map.containsKey("title") ? map.get("title").toString() : "NA");
                ps.setString(9, oldmap.get("ophone").toString());
                ps.setString(10, map.containsKey("telephonenumber") ? map.get("telephonenumber").toString() : "NA");
                ps.executeUpdate();

                System.out.println("data inserted of Ldap Modify into ldap_userprofile table sucessfully as :" + ps);

            } catch (Exception ex) {
                System.out.println("Exception in ldap_userprofile table insertion SQL :::::::::::::" + ps);
                ex.printStackTrace();
            }

            // Start mail sending
            try {
                sb.append("<p>Dear Sir/Madam,</p>");
                sb.append("    Following data have been updated in your LDAP profile. We are taking this data from OAD. Hence, you are requested to contact OAD division for any further clarification.");
                sb.append("<table border='1' cellpadding='0' cellspacing='0'");
                sb.append("<tr><td><b>Attribute Name</b></td><td><b>Old_Value</b></td><td><b>New_Value</b></td></tr>").append("</br>");
                sb.append(sb1);
                sb.append("</table>").append("</br>");
                sb.append("</br>Regards,</br>");
                sb.append("Messaging Team NIC");

                send_Mail(oldmap.get("mail").toString(), sb.toString(), "LDAP Profile Update");
            } catch (Exception ex) {
                System.out.println("Exception in mail Sending :::::::::::::::" + ex.getMessage());
                ex.printStackTrace();
            }
            // end mail sending

        } catch (Exception ex) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$ Exception in values updation in LDAP $$$$$$$$$$$$$$$$$$$$");
            ex.printStackTrace();
        }

        return flag;
    }

    // Mail Sending
    public static boolean send_Mail(String email, String msg, String subject) {
        boolean flag = false;

        Properties props = new Properties();
        props.put("mail.smtp.host", smtphost);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailfrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("support@gov.in"));
            message.setSubject(subject);
            message.setContent(msg, "text/html");
            Transport.send(message);
            System.out.println("Mail send to ========================> " + email + " :::: Subject: ==> " + subject + " :::: Message Body: ==>" + msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("\n send_Mail Method Exception ::::::::::::::" + e.getMessage());
        }
        return flag;
    }

    // for API URL hit
    public static Document apiXmlParser(String empno) {

        Map<String, String> map = new HashMap<String, String>();
        URL url;
        Document doc = null;
        String http = "https://personnel.nic.in/webservice_ldap/emdmaster.php?processid=EmployeeMaster&var=EMD";
        try {

            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
            TrustManager[] trust_mgr = get_trust_mgr();
            ssl_ctx.init(null, trust_mgr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
            url = new URL(http);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(100000); //set timeout to 10 seconds
            String res = print_content(con);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            InputSource is;

            try {

                builder = factory.newDocumentBuilder();
                is = new InputSource(new StringReader(res));
                doc = builder.parse(is);
                doc.getDocumentElement().normalize();

            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }

        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        return doc;
    }

    // for ignoreing certificate
    private static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String t) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String t) {
            }
        }};
        return certs;
    }

    // for response printing
    private static String print_content(HttpsURLConnection con) {
        String res = "";
        if (con != null) {

            try {

                //System.out.println("****** Content of the URL ********");
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null) {

                    res += input;
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    //Rahul's changes to send mail
    public static boolean send_Mail_OAD_Team(List<String> emailList) {
        String cdate = "", previous_date = "";
        Calendar cal = Calendar.getInstance();
        cdate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        previous_date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        System.out.println("Current_date :::" + cdate + " <====================================================> Previous_date :::" + previous_date);
        String to = "nic-oad@nic.in,gaurav.bali@nic.in", subject = "OAD : Users with missing Reporting Officer's email (" + cdate + ")", from = "tiwari.ashwini@nic.in", cc = "sriram@nic.in,prog17.nhq-dl@nic.in,nitins.nhq@nic.in,tiwari.ashwini@nic.in";
        //String to = "tiwari.ashwini@nic.in", subject = "OAD : Users with missing Reporting Officer's email (" + cdate + ")", from = "tiwari.ashwini@nic.in", cc = "prog17.nhq-dl@nic.in,nitins.nhq@nic.in,tiwari.ashwini@nic.in";

        StringBuffer sb = new StringBuffer();
        sb.append("<p>Dear Team,</p>");
        sb.append("Kindly find the list of employees (received from you through API) having blank reporting officer's emails for date (" + previous_date + ").<br></br/><br></br/>");
        sb.append("<table border='1'>");
        sb.append("<tr><th>Sr no.</th><th>Email</th></tr>");
        int i = 1;
        for (String string : emailList) {
            sb.append("<tr><td>" + i + "</td><td>" + string + "</td></tr>");
            ++i;
        }

        sb.append("</table>");
        sb.append("<br></br>Regards,<br></br>");
        sb.append("Ashwini Kumar Tiwari");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtphost);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailfrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("support@gov.in"));
            message.setSubject(subject);
            message.setContent(sb.toString(), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("\n send_Mail Method Exception ::::::::::::::" + e.getMessage());
        }
        return flag;
    }

    private static boolean send_Mail_OAD_Team_For_Blank_UserEmail(List<Map<String, String>> map) {
        String cdate = "", previous_date = "";
        Calendar cal = Calendar.getInstance();
        cdate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        previous_date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        System.out.println("Current_date :::" + cdate + " <====================================================> Previous_date :::" + previous_date);
        String to = "pkr.gaurav@nic.in,manikant.96@nic.in", subject = "OAD : Users with missing email addresses(" + cdate + ")", from = "tiwari.ashwini@nic.in", cc = "tiwari.ashwini@nic.in,neeraj.tuteja@nic.in";

        StringBuffer sb = new StringBuffer();
        sb.append("<p>Dear Team,</p>");
        sb.append("Kindly find the list of employees (received from you through API) having blank user's emails for date (" + previous_date + ").<br></br/><br></br/>");
        sb.append("<table border='1'>");
        sb.append("<tr><th>Sr no.</th><th>Email</th></tr>");
        int i = 1;
        for (Map<String, String> string : map) {
            sb.append("<tr><td>" + i + "</td><td>" + string.toString() + "</td></tr>");
            ++i;
        }

        sb.append("</table>");
        sb.append("<br></br>Regards,<br></br>");
        sb.append("Ashwini Kumar Tiwari");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtphost);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailfrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            //message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("support@gov.in"));
            message.setSubject(subject);
            message.setContent(sb.toString(), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("\n send_Mail Method Exception ::::::::::::::" + e.getMessage());
        }
        return flag;
    }

    private static boolean send_Mail_OAD_Team_For_Blank_Mobile(List<Map<String, Object>> map) {
        String cdate = "", previous_date = "";
        Calendar cal = Calendar.getInstance();
        cdate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        previous_date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        System.out.println("Current_date :::" + cdate + " <====================================================> Previous_date :::" + previous_date);
        String to = "pkr.gaurav@nic.in,manikant.96@nic.in", subject = "OAD : Users with missing email addresses(" + cdate + ")", from = "tiwari.ashwini@nic.in", cc = "tiwari.ashwini@nic.in,neeraj.tuteja@nic.in";

        StringBuffer sb = new StringBuffer();
        sb.append("<p>Dear Team,</p>");
        sb.append("Kindly find the list of employees (received from you through API) having blank user's emails for date (" + previous_date + ").<br></br/><br></br/>");
        sb.append("<table border='1'>");
        sb.append("<tr><th>Sr no.</th><th>Email</th></tr>");
        int i = 1;
        for (Map<String, Object> string : map) {
            sb.append("<tr><td>" + i + "</td><td>" + string.toString() + "</td></tr>");
            ++i;
        }

        sb.append("</table>");
        sb.append("<br></br>Regards,<br></br>");
        sb.append("Ashwini Kumar Tiwari");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtphost);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailfrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            // message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("support@gov.in"));
            message.setSubject(subject);
            message.setContent(sb.toString(), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("\n send_Mail Method Exception ::::::::::::::" + e.getMessage());
        }
        return flag;
    }

    private static void sendNotificatioMail() {
        String cdate = "", previous_date = "";
        Calendar cal = Calendar.getInstance();
        cdate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        previous_date = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        System.out.println("Current_date :::" + cdate + " <====================================================> Previous_date :::" + previous_date);
        // String to = "nic-oad@nic.in,gaurav.bali@nic.in", subject = "OAD : Users with missing Reporting Officer's email (" + cdate + ")", from = "tiwari.ashwini@nic.in", cc = "sriram@nic.in,prog17.nhq-dl@nic.in,nitins.nhq@nic.in,tiwari.ashwini@nic.in";
        String to = "tiwari.ashwini@nic.in", subject = "eForms-OAD : NIC Employee table has been updated (" + cdate + ")", from = "no-reply@nic.in", cc = "prog17.nhq-dl@nic.in,tiwari.ashwini@nic.in";

        StringBuffer sb = new StringBuffer();
        sb.append("<p>Dear Team,</p>");
        sb.append("NIC Employee table has been successfully updated with OAD data for date (" + previous_date + ").<br></br><br></br>");
        sb.append("</table>");
        sb.append("<br></br>Regards,<br></br>");
        sb.append("Messaging Team NIC");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtphost);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailfrom));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
            message.setSubject(subject);
            message.setContent(sb.toString(), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("\n send_Mail Method Exception ::::::::::::::" + e.getMessage());
        }

    }
}
