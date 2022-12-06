package admin;

import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.log.SysoLogger;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.opencsv.CSVReader;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.Forms;
import com.org.bean.UserData;
import com.org.connections.DbConnection;
import com.org.connections.SingleApplicationContext;
import com.org.connections.SingleApplicationContextSlave;
import com.org.dao.DaOnboardingDao;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.EmailService;
import com.org.utility.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.AttributeModificationException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONObject;
import org.owasp.esapi.ESAPI;
import utility.ExcelCreator;
import utility.Inform;
import utility.Mail;
import utility.NewFileInputStream;
import validation.BulkValidation;
import zimbra_soap_v2.Zimbra_SOAP_v2;
import static zimbra_soap_v2.Zimbra_SOAP_v2.GetAccountID;
import static zimbra_soap_v2.Zimbra_SOAP_v2.ModifyAccount;
import static zimbra_soap_v2.Zimbra_SOAP_v2.getAdminAuthToken;

public class AdminAction extends ActionSupport implements SessionAware {

    Map sessionMap;
    ServletContext ctx = getServletContext();
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
    private final String dbhost = ctx.getInitParameter("db-host");
    private final String dbuser = ctx.getInitParameter("db-user");
    private final String dbname = ctx.getInitParameter("db-name");
    private final String dbpass = ctx.getInitParameter("db-pass");
    private final String ctxfactory = ctx.getInitParameter("ctx-factory");
    private final String providerurl = ctx.getInitParameter("provider-url");
    private final String providerurlSlave = ctx.getInitParameter("provider-url-slave");
    private final String binddn = ctx.getInitParameter("bind-dn");
    private final String bindpass = ctx.getInitParameter("bind-pass");
    private final String createdn = ctx.getInitParameter("createdn");
//    private final String createpass = ctx.getInitParameter("createpass");

    Base64.Decoder decoder = Base64.getDecoder();
    private final String createpass = new String(
            decoder.decode(ServletActionContext.getServletContext().getInitParameter("createpass")));

    private final String dbdriver = ctx.getInitParameter("dbdriver");
    private boolean isDa = false;
    String regNoRegEx = "^[a-zA-Z0-9~_\\-]*$"; // line added by pr on 6thfeb18
    String statRemarksRegEx = "^[a-zA-Z@_#0-9&\\?\\s,:'.\\-\\/\\(\\)\\\\]{2,500}$"; // line added by pr on 19thfeb18, & allowed by pr on 19thapr18, : added by pr on 28thdec18 
    String shaRegEx = "^[a-zA-Z0-9]{10,100}$"; // line added by pr on 7thjan19    
    // start, code added by pr on 29thjan18
    private final String DNS_NIC_FIRST_COORD = ctx.getInitParameter("DNS_NIC_FIRST_COORD");
    private final String DNS_GOV_FIRST_COORD = ctx.getInitParameter("DNS_GOV_FIRST_COORD");
    private final String DNS_SECOND_COORD = ctx.getInitParameter("DNS_SECOND_COORD");
    private final String DNS_MAILADMIN = ctx.getInitParameter("DNS_MAILADMIN");
    // end, code added by pr on 29thjan18
    private final String GEM_DA_ADMIN = ctx.getInitParameter("GEM_DA_ADMIN"); // line added by pr on 26thfeb18
    private String sup_email = ctx.getInitParameter("sup_email"); // line added by pr on 8thmar18

    //private String zim_mail_host = ctx.getInitParameter("zim_mail_host"); // line added by pr on 7thmay19, commented on 29thmay19
    private String bulk_id;// checkboxes value for bulk email id creation

    private String CSRFRandom = "";
    private Map<String, Object> hmTrack = null;

    public Map<String, Object> getHmTrack() {
        return hmTrack;
    }

    public void setHmTrack(Map<String, Object> hmTrack) {
        this.hmTrack = hmTrack;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;
    }

    public String getBulk_id() {
        return bulk_id;
    }

    public void setBulk_id(String bulk_id) {
        this.bulk_id = bulk_id;
    }

    private Database db = null;
    private Ldap ldap = null;

    // start, code added by pr on 7thmay19, to incorporate delete functionality in wifi form
    boolean isWifiDelete;

    public boolean isIsWifiDelete() {
        return isWifiDelete;
    }

    public void setIsWifiDelete(boolean isWifiDelete) {
        this.isWifiDelete = isWifiDelete;
    }

    ForwardAction fwdObj;

    public AdminAction() {
        fwdObj = new ForwardAction(); // line added by pr on 2ndaug19

        db = new Database();
        ldap = new Ldap();
        hmTrack = new HashMap<>();
    }

    // end, code added by pr on 7thmay19
    /*private final String ldapproviderurl = ctx.getInitParameter("provider-url");
     private final String ldapdn = ctx.getInitParameter("bind-dn");
     private final String ldapdn_pass = ctx.getInitParameter("bind-pass");*/
    private String po;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null, conSlave = null; // this line added by pr on 4thmay18

    // start, code added by pr on 23rdapr2020
    String dor;

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    // end, code added by pr on 23rdapr2020
    // start , code added  by pr on 21stjun19, for firewall location to be saved on mark as done
    String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String uid_exist;

    public String getUid_exist() {
        return uid_exist;
    }

    public void setUid_exist(String uid_exist) {
        this.uid_exist = uid_exist;
    }

    private ArrayList<String> errorArr;

    public ArrayList<String> getErrorArr() {
        return errorArr;
    }

    public void setErrorArr(ArrayList<String> errorArr) {
        this.errorArr = errorArr;
    }

    private File csv_file;

    public File getCsv_file() {
        return csv_file;
    }

    public void setCsv_file(File csv_file) {
        this.csv_file = csv_file;
    }
    String storeMsg;

    public String getStoreMsg() {
        return storeMsg;
    }

    public void setStoreMsg(String storeMsg) {
        this.storeMsg = storeMsg;
    }
    // start, code added by pr on 5thapr18, to fetch duplictae emails related to the mobile

    String formName;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    String mobile_ldap_email;

    public String getMobile_ldap_email() {
        return mobile_ldap_email;
    }

    public void setMobile_ldap_email(String mobile_ldap_email) {
        this.mobile_ldap_email = mobile_ldap_email;
    }
    // end, code added by pr on 5thapr18
// start, code added by pr on 2ndjan19

    private String ldap_wifi_value;

    public String getLdap_wifi_value() {
        return ldap_wifi_value;
    }

    public void setLdap_wifi_value(String ldap_wifi_value) {
        this.ldap_wifi_value = ldap_wifi_value;
    }

    // end, code added by pr on 2ndjan19    
// start, code added by pr on 14thmar18
    private String wifi_value;

    public String getWifi_value() {
        return wifi_value;
    }

    public void setWifi_value(String wifi_value) {
        this.wifi_value = wifi_value;
    }
    // end, code added by pr on 14thmar18
    // start, code added  by pr on 31staug18
    private String imap_value = "";

    public String getImap_value() {
        return imap_value;
    }

    public void setImap_value(String imap_value) {
        this.imap_value = imap_value;
    }
    // end, code added  by pr on 31staug18

    // start, code added by pr on 13thmar18
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // end, code added by pr on 13thmar18
    // start, code added by pr on 9thmar18
    String field_name = "";

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getField_value() {
        return field_value;
    }

    public void setField_value(String field_value) {
        this.field_value = field_value;
    }
    String field_value = "";
    // end, code added by pr on 9thmar18        
    // start, code added by pr on 19thfeb18
    private String primary_id;

    public String getPrimary_id() {
        return primary_id;
    }

    public void setPrimary_id(String primary_id) {
        this.primary_id = primary_id;
    }

    private String random;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    // end, code added by pr on 3rdjan18
    private String type; // added on 4thdec17 for export excel for error and success documents

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    // start, added on 4thdec17 for bulk_users file download
    private InputStream fileInputStream;

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    // end, added on 4thdec17 for bulk_users file download
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }  // uncommented by pr on 21stoct19

    private String bo;

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    private String domain;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    private String final_sms_id;

    public String getFinal_sms_id() {
        return final_sms_id;
    }

    public void setFinal_sms_id(String final_sms_id) {
        this.final_sms_id = final_sms_id;
    }

//    private String fwd_madmin;
//    
//    public String getFwd_madmin() {
//        return fwd_madmin;
//    }
//
//    public void setFwd_madmin(String fwd_madmin) {
//        this.fwd_madmin = fwd_madmin;
//    }
//    private String add_coord;
//
//    public String getAdd_coord() {
//        return add_coord;
//    }
//
//    public void setAdd_coord(String add_coord) {
//        this.add_coord = add_coord;
//    }
    private Boolean isSuccess;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    private Boolean isError;

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ArrayList<String> podata = new ArrayList<>();

    public ArrayList<String> getPodata() {
        return podata;
    }

    public void setPodata(ArrayList<String> podata) {
        this.podata = podata;
    }

    private ArrayList<String> bodata = new ArrayList<>();

    public ArrayList<String> getBodata() {
        return bodata;
    }

    public void setBodata(ArrayList<String> bodata) {
        this.bodata = bodata;
    }

    private ArrayList<String> domaindata = new ArrayList<>();

    public ArrayList<String> getDomaindata() {
        return domaindata;
    }

    public void setDomaindata(ArrayList<String> domaindata) {
        this.domaindata = domaindata;
    }

    // start, code added by pr on 24thdec18 for bulk id creation
    private HashMap<String, String> bulkUpdateData = new HashMap<>();

    public HashMap<String, String> getBulkUpdateData() {
        return bulkUpdateData;
    }

    public void setBulkUpdateData(HashMap<String, String> bulkUpdateData) {
        this.bulkUpdateData = bulkUpdateData;
    }

    private String statRemarks;

    public String getStatRemarks() {
        return statRemarks;
    }

    public void setStatRemarks(String statRemarks) {
        this.statRemarks = statRemarks;
    }

    private String regNo;

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public Map getSession() {
        return sessionMap;
    }

    @Override
    public void setSession(Map sessionMap) {
        this.sessionMap = sessionMap;
    }
    // start, code added by pr on 26thdec18
    private Boolean toggleBulk;

    public Boolean getToggleBulk() {
        return toggleBulk;
    }

    public void setToggleBulk(Boolean toggleBulk) {
        this.toggleBulk = toggleBulk;
    }

    public String fetchSessionRole() {
        String role = "";
        if (ActionContext.getContext().getSession() != null) {
            this.sessionMap = ActionContext.getContext().getSession();
            if (sessionMap.get("admin_role") != null) // if around added by pr on 7thjan19
            {
                role = sessionMap.get("admin_role").toString();
            }
        }
        return role;
    }

    public String fetchSessionEmail() {
        String email = "";
        if (sessionMap != null && sessionMap.get("uservalues") != null) {
            UserData userdata = (UserData) sessionMap.get("uservalues");
            email = userdata.getEmail();
        }
        return email;
    }

    public Boolean authAvail(String uid) {
        Boolean flag = true;
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext ctx = null;// line added by pr on 16thoct18
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress"};
            String[] IDs = {"uid"};
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
            //String filter = "(|(uid=" + email + ")(mailequivalentaddress=" + email + ") (mail=" + emailNIC + ")(mailequivalentaddress=" + emailNIC + ") )";
            String filter = "(|(uid=" + uid + "))";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();
                String uids = t.substring(t.indexOf("uid:") + 5, t.length() - 1);
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 166" + e.getMessage());
//                }
//            }
        }
        return flag;
    }

    public String createID() throws ParseException // create email id or sms id based on the form type
   {
        System.out.println(" inside createID function value of wifi_value is " + wifi_value);
        String[] arr = null;
        String reg_no = "", formName = "";
        String role = "";
        role = fetchSessionRole();
        // start, code added by pr on 12thapr19 to update in zimbra
        URL url = null;
        String admin_token = "";
        // end, code added by pr on 12thapr19 to update in zimbra
        //if( regNo != null &&  !regNo.equals("")  && regNo.matches("^[a-zA-Z0-9~\\-]*$")  ) // line modified by pr on 5thfeb18 for xss
        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegEx)) // line modified by pr on 5thfeb18 for xss
        {
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        } else {
            regNo = ""; // line added by pr on 6thfeb18
        }
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside createID function in forwardaction.java value of random is " + random + " session random value is " + sessionMap.get("random")+" statRemarks value is "+statRemarks);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createID function in forwardaction.java value of random is " + random + " session random value is " + sessionMap.get("random") + " statRemarks value is " + statRemarks);
        //if (random.matches(regNoRegEx) && sessionMap.get("random").equals(random)) // if across added by pr on 3rdjan18 to apply csrf token check, modified by pr on 6thfeb18, below line modified by pr on 3rdoct19
        if (CSRFRandom.matches(regNoRegEx) && sessionMap.get("CSRFRandom").equals(CSRFRandom) && (statRemarks == null || statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)))) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createID func and inside random value match with session value ");
            // in this case action type will be forward only
            if (arr != null && arr.length == 3) {
                // get the details of employment from the respective table across the reg no
                reg_no = arr[0];
                formName = arr[1];

                //12-apr-2022 syndata for both create id and mark as done
                NewCAHome newCAHome = new NewCAHome();
                newCAHome.syncData(reg_no);
                //12-apr-2022 syndata for both create id and mark as done

                // start, code added by pr on 20thsep19
                String stat_forwarded_by = "";
                if (role.equals(Constants.ROLE_MAILADMIN)) {
                    stat_forwarded_by = "m";
                } else if (role.equals(Constants.ROLE_CO)) {
                    stat_forwarded_by = "c";
                } else if (role.equals(Constants.ROLE_CA)) {
                    stat_forwarded_by = "ca";
                } else if (role.equals(Constants.ROLE_SUP)) {
                    stat_forwarded_by = "s";
                }

                String stat_forwarded_by_user = fetchSessionEmail();

                fwdObj.sessionMap = sessionMap; // line added by pr on 21stoct19  

                if (fwdObj.verifyFwdToDetails(reg_no, formName, stat_forwarded_by, stat_forwarded_by_user)) {

                    // end, code added by pr on 20thsep19
                    // start, code added by pr on 2nddec19  
                    HashMap formDetailHM = null;

                    boolean statusUpdated = false;

                    // end, code added by pr on 2nddec19  
                    if (formName.equals(Constants.SMS_FORM_KEYWORD)) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside create id function sms block final_sms_id is  " + final_sms_id);
                        // check the sms_services in sms table corresponding to teh reg no accordingly create sms ids
                        String sms_service = "";
                        try {
                            HashMap rs = fetchSMSDetail(reg_no);

                            formDetailHM = rs; // line added by pr on 2nddec19

                            if (rs.get("sms_service") != null) {
                                sms_service = rs.get("sms_service").toString();
                            }
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside get sms detail resultset  " + e.getMessage());
                        }

                        /*StringBuilder sb = new StringBuilder();
                    //String num = "0123456789abcdefghijklmnopqrstuvwx";
                    String num = "0123456789abcdefghjkmnopqrstuvwx"; // line modified by pr on 23rdjul18 l and i have been removed
                    SecureRandom rmh = new SecureRandom();
                    for (int i = 0; i < 8; i++) {
                        sb.append(num.charAt(rmh.nextInt(num.length())));
                    }
                    String pass = sb.toString();*/
                        // above code commented below added by pr on 9thaug19
                        String pass = fetchRandomPassword();

                        // end, added by pr on 9thaug19
                        String uid_prefix = final_sms_id, uid = "";
                        ArrayList<String> msgArr = new ArrayList<String>();
                        ArrayList<String> createIDArr = new ArrayList<String>();
                        //if (uid_prefix.matches("^[a-zA-Z]*$")) 
                        if (uid_prefix.matches("^[a-zA-Z]{3,24}$")) // line modified by pr on 21staug18
                        {
                            Boolean flagError = false;
                            if (sms_service.contains("push") || sms_service.contains("pull")) {
                                uid = uid_prefix + ".sms";
                                if (authAvail(uid)) {
                                    createIDArr.add(uid);
                                } else {
                                    flagError = true;
                                    msgArr.add(uid);
                                }
                            }
                            if (sms_service.contains("obd") || sms_service.contains("missedcall")) {
                                uid = uid_prefix + ".obd";
                                if (authAvail(uid)) {
                                    createIDArr.add(uid);
                                } else {
                                    flagError = true;
                                    msgArr.add(uid);
                                }
                            }
                            if (sms_service.contains("otp")) {
                                uid = uid_prefix + ".otp";
                                if (authAvail(uid)) {
                                    createIDArr.add(uid);
                                } else {
                                    flagError = true;
                                    msgArr.add(uid);
                                }
                            }
                            if (sms_service.contains("quicksms")) {
                                uid = uid_prefix + ".qsms";
                                if (authAvail(uid)) {
                                    createIDArr.add(uid);
                                } else {
                                    flagError = true;
                                    msgArr.add(uid);
                                }
                            }
                            if (flagError) // if any of the uid already exists then dont create and show error
                            {
                                String ms = "";
                                if (msgArr.size() > 0) // to concatenate every error message
                                {
                                    int i = 1;
                                    for (String m : msgArr) {
                                        if (i == 1) {
                                            ms = m;
                                        } else {
                                            ms += ", " + m;
                                        }
                                        i++;
                                    }
                                }
                                isSuccess = false;
                                isError = true;
                                if (!ms.isEmpty()) {
                                    msg = "These Ids are not available: " + ms + " Please choose another ID";
                                }
                            } else // create all the ids
                            {
                                ArrayList<String> createdArr = new ArrayList<String>();
                                for (String id : createIDArr) {
                                    if (createSMSId(id, reg_no, pass)) {
                                        createdArr.add(id);
                                    }
                                }
                                String str = "";
                                int i = 0;
                                for (String u : createdArr) {
                                    if (i == 0) {
                                        str = u;
                                    } else {
                                        str += ", " + u;
                                    }
                                    i++;
                                }
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " before string not empty check ");
                                if (!str.isEmpty()) // if around and else added by pr on 21stmar18
                                {
                                    // insert into status table
                                    Boolean r = fwdObj.updateStatus(reg_no, "completed", "sms", "", "", "", str); // value equated by pr on 5thfeb18, str added by pr on 16thjan19

                                    statusUpdated = r; // line added by pr on 2nddec19

                                    // update sms table for final id
                                    if (r) // if around and else added by pr on 5thfeb18
                                    {
                                        updateSMSFinalID(str, reg_no, description); // modified by pr on 14thmar18
                                        // send intimation
                                        Inform infObj = new Inform();
                                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " before send comp intimation 1");
                                        infObj.sendCompIntimation(reg_no, formName, str, pass);
                                        isSuccess = true;
                                        isError = false;
                                        msg = "SMS AUTH ID created : " + str;
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "SMS AUTH ID created : " + str + " but status not updated.";
                                    }
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    msg = "SMS AUTH ID could not be created , SMS service field is blank.";
                                }
                            }
                        } else {
                            isSuccess = false;
                            isError = true;
                            msg = "SMS AUTH ID can be only Alphabets with Length minimum 3 and maximum 24 characters."; // msg modified by pr on 21staug18
                        }
                    } else if (formName.equals(Constants.SINGLE_FORM_KEYWORD) || formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || formName.equals(Constants.GEM_FORM_KEYWORD)) {

                        // start              
                        /*StringBuilder sb = new StringBuilder();
                    //String num = "0123456789abcdefghijklmnopqrstuvwx";
                    String num = "0123456789abcdefghjkmnopqrstuvwx"; // modified by pr on 23rdjul18
                    SecureRandom rmh = new SecureRandom();
                    for (int i = 0; i < 8; i++) {
                        sb.append(num.charAt(rmh.nextInt(num.length())));
                    }
                    String pass = sb.toString();*/
                        // above code commented below added by pr on 9thaug19
                        String pass = fetchRandomPassword();

                        // end, added by pr on 9thaug19
                        Boolean flagError = false;
                        if (!(final_sms_id.matches("^[a-zA-Z0-9\\.\\-]*$") && primary_id.matches("^[a-zA-Z0-9\\.\\-]*$"))) {
                            flagError = true; // it shouldnot proceed further
                            msg = "Please enter valid IDs";
                        } else {
                            // start, code added by pr on 22ndfeb19
                            BulkValidation bval = new BulkValidation();
                            HashMap values = bval.onlyUID(final_sms_id);
                            if (values != null && values.size() > 0) {
                                if (values.get("valid") != null) {
                                    if (values.get("valid").equals("false")) {
                                        flagError = true;
                                    }
                                }
                                if (values.get("errorMsg") != null) {
                                    msg = values.get("errorMsg").toString();
                                }
                            }
                            if (final_sms_id.indexOf(".") == -1 && final_sms_id.indexOf("-") == -1) {
                                flagError = true;
                                msg += " UID " + final_sms_id + " must contain a dot(.) or hyphen(-) or both ";
                            }
                            // end, code added by pr on 22ndfeb19
                            if (!fwdObj.emailAvail(final_sms_id)) {
                                flagError = true;
                                msg = " UID " + final_sms_id + " not available ";
                                // start, code added by pr on 20thaug18
                                if (storeMsg != null && !storeMsg.equals("")) { // storeMsg not null added by pr on 30thdec19
                                    msg += "(" + storeMsg + ")";
                                }
                                // end, code added by pr on 20thaug18
                            }
                            if (domain.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$")) {
                                if (!fwdObj.emailAvail(primary_id + "@" + domain)) {
                                    flagError = true;
                                    if (msg != null && !msg.equals("")) {
                                        msg += " Primary Email " + primary_id + "@" + domain + " not available ";
                                    } else {
                                        msg = " Primary Email " + primary_id + "@" + domain + " not available ";
                                    }
                                    // start, code added by pr on 20thaug18
                                    if (storeMsg != null && !storeMsg.equals("")) { // storeMsg not null added by pr on 30thdec19
                                        msg += "(" + storeMsg + ")";
                                    }
                                    // end, code added by pr on 20thaug18
                                }
                            } else {
                                flagError = true;
                                msg += " Domain is Invalid ";
                            }
                            if (!domain.equals("nic.in")) // if domain selected is not nic.in then only check for mailequivalent address availbality else it will not be created
                            {
                                if (!fwdObj.emailAvail(final_sms_id + "@nic.in")) {
                                    flagError = true;
                                    msg += " MailEquivalentAddress " + final_sms_id + "@nic.in" + " not available ";
                                }
                            }
                        }
                        // start, code added by pr on 24thoct18 for mobile check, so as to restrict the creation of accounts based on uid already attached to the mobile
                        // check for the uids linked with this mobile number and likewise allow creation or show message
                        // get the form detail and from that get the mobile
                        HashMap hmap = fwdObj.fetchFormDetail(arr[0], arr[1]);
                        formDetailHM = hmap; // line added by pr on 2nddec19
                        String mobile = "";
                        if (hmap.get("mobile") != null) {
                            mobile = hmap.get("mobile").toString();

                            // start, code added by pr on 29thapr2020
                            // here check for the other_applicant_mobile depending upon the request_flag value if single form
                            if (hmap.get("request_flag") != null && hmap.get("request_flag").toString().equalsIgnoreCase("y")) {
                                if (hmap.get("other_applicant_mobile") != null) {
                                    mobile = hmap.get("other_applicant_mobile").toString();
                                }
                            }
                            // end, code added by pr on 29thapr2020
                            // get the uids corresponding to this mobile number
                            // below line for emails commented by pr on 12thmay2020
                            //String emails = fwdObj.fetchMobileLDAPMail(mobile); // get the emails associated with this mobile from the LDAP
                            // call the checkmobileduplicate function on this uids
                            //Boolean isAllowCreation = allowMobileDuplicateCreation(arr[0], emails, null); // line modified by pr on 29thoct18 for mobile check
                            Boolean isAllowCreation = allowMobileDuplicateCreation(arr[0], mobile, null); // line modified by pr on 12thmay2020
                            if (!isAllowCreation) {
                                flagError = true;
                                //finalFieldErrorMap.put("mobile_duplicate", "This Mobile is already linked with an account");
                                if (msg != null) {
                                    //msg += " This Mobile is already linked with accounts. With \"-admin\" uid , max 3 accounts allowed.For others max 2 accounts allowed.";
                                    // modified on 20thnov18
                                    msg += " This Mobile is already linked with accounts. With \"-admin\" uid , max 4 accounts allowed.For others max 3 accounts allowed.";
                                } else {
                                    //msg = " This Mobile is already linked with accounts. With \"-admin\" uid , max 3 accounts allowed.For others max 2 accounts allowed.";
                                    // modified on 20thnov18                                
                                    msg = " This Mobile is already linked with accounts. With \"-admin\" uid , max 4 accounts allowed.For others max 3 accounts allowed.";
                                }
                            }
                        }
                        // end, code added by pr on 24thoct18 for mobile check
                        if (flagError) // if any of the uid already exists then dont create and show error
                        {
                            //System.out.println(" inside flagError true in createemailid func ");
                            isSuccess = false;
                            isError = true;
                        } else // create all the ids
                        {

                            String name = "";
                            //System.out.println(" inside flagError false in createemailid func ");
                            String primaryEmail = primary_id + "@" + domain;
                            String mailEquiv = final_sms_id + "@nic.in";
                            // start, code added by pr on 28thfeb18
                            Boolean createAppID = false;
                            if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
                                HashMap hm = fwdObj.fetchSingleDetail(reg_no);

                                name = hm.get("fname").toString();

                                formDetailHM = hmap; // line added by pr on 2nddec19
                                String type = "";
                                if (hm.get("type") != null) {
                                    type = hm.get("type").toString().trim();
                                }
                                if (type.equalsIgnoreCase("app") || type.equalsIgnoreCase("eoffice")) { // eoffice added by pr on 13thsep18
                                    createAppID = true;
                                }
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "type:: " + type);
                            }
                            if (createAppID) {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createAppID true ");

                                // call create app id function, DirContext object added by pr on 8thaug19
                                if (createAppId(null, formName, po, bo, domain, final_sms_id, primaryEmail, reg_no, pass, "", "")) // two parameters added after uid_prefix by pr on 19thfeb18
                                {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createAppID ID creation true ");
                                    // System.out.println(" inside createemailid func create email id true returns ");
                                    // insert into status table
                                    Boolean r = fwdObj.updateStatus(reg_no, "completed", formName, "", "", "", primaryEmail); // line modified primaryEmail added by pr on 16thjan19

                                    statusUpdated = r; // line added by pr on 2nddec19                                
                                    // send intimation
                                    // send intimation
                                    if (r) // if around and else added by pr on 5thfeb18
                                    {
                                        Inform infObj = new Inform();
                                        //infObj.sendCompIntimation(reg_no, formName, uid_prefix + "@" + domain, pass);
                                        infObj.sendCompIntimation(reg_no, formName, primaryEmail, pass); // line modified by pr on 19thfeb18
                                        // update table for final id
                                        //updateFinalID(uid, reg_no, formName);
                                        updateFinalID(primaryEmail, reg_no, formName, description); // line modified by pr on 19thfeb18
                                        isSuccess = true;
                                        isError = false;
                                        msg = " Email ID Created with UID: " + final_sms_id + ", Primary Email : " + primaryEmail + ", MailequivalentAddress  " + mailEquiv;

                                        //try added by ram pratap on 12  
                                        try {
                                            //(String uid_to_api, String email_to_api, String boname, String name_to_api, String mobile_to_api)
                                            post_createId(final_sms_id, mailEquiv, bo, name, mobile);
                                        } catch (MalformedURLException ex) {
                                            Logger.getLogger(AdminAction.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = " Email ID Created with UID: " + final_sms_id + ", Primary Email : " + primaryEmail + ", MailequivalentAddress  " + mailEquiv + ".However, Status could not be updated.";
                                    }
                                } else {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createAppID ID creation false ");
                                    // System.out.println(" inside createemailid func create email id false returns ");
                                    // start, code added by pr on 7thfeb18
                                    po = "";
                                    bo = "";
                                    domain = "";
                                    final_sms_id = "";
                                    // end, code added by pr on 7thfeb18
                                    isSuccess = false;
                                    isError = true;
                                    msg = "UID could not be created : " + final_sms_id;
                                }
                            } else {
                                // start, code added by pr on 12thapr19
                                // check for zimbra token if it is got then proceed else stop here and give an error
                                try {
                                    url = new URL("https://100.80.17.250:7071/service/admin/soap/");
                                    //admin_token = getAdminAuthToken("createuserapp@createaccount.da", "fgaT8.8A_bGm", url);
                                    //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "A@#poi$^KJ39", url); // 2ndjun2020
                                    //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "#ZyPE@8xR#H$TJxT9^H=CTZ@#43f#Dg4dV#Xz@", url);
                                    admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "Q=2E+D4Kf=BYi+K7De=4TjA+xFq6Z=T7+hDs=4x", url);
                                } catch (Exception ex) {
                                    System.out.println("Exception in admin URL hit ::::::::::::::::::");
                                } // commented for testing on 1stnov19, to be uncommented in production

                                //admin_token = "valid";   // to be commented in production, done for testing on 19thaug2020
                                // end, code added by pr on 12thapr19
                                // end, code added by pr on 12thapr19
                                if (!admin_token.isEmpty()) // if around and else added by pr on 12thapr19
                                {
                                    // end, code added by pr on 28thfeb18, first parameter connection object DirContext added by pr on 8thaug19
                                    if (createEmailId(null, formName, po, bo, domain, final_sms_id, primaryEmail, reg_no, pass, "", "")) // two parameters added after uid_prefix by pr on 19thfeb18
                                    {
                                        // start, code added  by pr on 12thapr19
                                        Inform infObj = new Inform();

                                        HashMap<String, Object> zimHM = createZimEmailId(formName, po, bo, domain, final_sms_id, primaryEmail, reg_no, pass, "", "", admin_token, url);

                                        Boolean zimValid = false;
                                        String data = null;
                                        String message = "";
                                        if (zimHM != null && zimHM.size() > 0) {
                                            if (zimHM.get("valid") != null) {
                                                zimValid = (Boolean) zimHM.get("valid");
                                            }
                                            if (zimHM.get("data") != null) {
                                                data = zimHM.get("data").toString();
                                            }
                                            if (zimHM.get("msg") != null) {
                                                message = zimHM.get("msg").toString();
                                            }
                                        }
                                        if (!zimValid) // if id gets created in zimbra too
                                        {
                                            // send mail to zimbra team with the data
                                            System.out.println(" inside zimbra error in forwardaction data is " + data + " zimValid is " + zimValid + " message is " + message);
                                            infObj.sendZimCreateMail(po, bo, data);
                                        } else // else added by pr on 18thjul19
                                        {
                                            // call the function to create the alias
                                            //HashMap hmAlias = createZimEmailAlias(final_sms_id, primaryEmail, admin_token, url, data);

                                            // above line modified by pr on 1staug19
                                            HashMap hmAlias = createZimEmailAliasSingle(final_sms_id, primaryEmail, admin_token, url, data, po, bo);

                                            /*if (hmAlias != null && hmAlias.size() > 0) {
                                            if (hmAlias.get("valid") != null) {
                                                zimValid = (Boolean) hmAlias.get("valid");
                                            }
                                            if (hmAlias.get("data") != null) {
                                                data = hmAlias.get("data").toString();
                                            }
                                            if (hmAlias.get("msg") != null) {
                                                message = hmAlias.get("msg").toString();
                                            }
                                        }

                                        if (!zimValid) // if id gets created in zimbra too
                                        {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + 
                                                    new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after calling createZimEmailAlias in single email creation data is " + data + " zimValid is " + zimValid + " message is " + message);
                                                                                        
                                            infObj.sendZimCreateMail(po, bo, data);
                                        }*/ // code commented by pr on 1staug19
                                        }
                                        // end, code added  by pr on 12thapr19    
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createEmail ID creation true ");
                                        // System.out.println(" inside createemailid func create email id true returns ");
                                        // insert into status table
                                        Boolean r = fwdObj.updateStatus(reg_no, "completed", formName, "", "", "", primaryEmail); // primaryEmail added by pr on 16thjan19
                                        statusUpdated = r; // line added by pr on 2nddec19                                
                                        // send intimation
                                        // send intimation
                                        if (r) // if around and else added by pr on 5thfeb18
                                        {
                                            //Inform infObj = new Inform(); // line commented by pr on 12thapr19
                                            //infObj.sendCompIntimation(reg_no, formName, uid_prefix + "@" + domain, pass);
                                            infObj.sendCompIntimation(reg_no, formName, primaryEmail, pass); // line modified by pr on 19thfeb18
                                            // update table for final id
                                            //updateFinalID(uid, reg_no, formName);
                                            updateFinalID(primaryEmail, reg_no, formName, description); // line modified by pr on 19thfeb18
                                            isSuccess = true;
                                            isError = false;
                                            msg = " Email ID Created with UID: " + final_sms_id + ", Primary Email : " + primaryEmail + ", MailequivalentAddress  " + mailEquiv;
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = " Email ID Created with UID: " + final_sms_id + ", Primary Email : " + primaryEmail + ", MailequivalentAddress  " + mailEquiv + ".However, Status could not be updated.";
                                        }
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createEmail ID creation false ");
                                        // System.out.println(" inside createemailid func create email id false returns ");
                                        // start, code added by pr on 7thfeb18
                                        po = "";
                                        bo = "";
                                        domain = "";
                                        final_sms_id = "";
                                        // end, code added by pr on 7thfeb18
                                        isSuccess = false;
                                        isError = true;
                                        msg = "UID could not be created : " + final_sms_id + " available quota : " + quota;
                                        if (quota <= 0) {
                                            msg = msg + "(Your quota is exhausted or bad response from Ldap)";
                                        }
                                    }
                                } // admin token check added by pr on 12thapr19
                                else // else added by pr on 12thapr19 
                                {
                                    po = "";
                                    bo = "";
                                    domain = "";
                                    final_sms_id = "";
                                    isSuccess = false;
                                    isError = true;
                                    msg = "Zimbra LDAP Token is empty.";
                                }
                            }
                        }

                    } else if (formName.equals(Constants.BULK_FORM_KEYWORD))// it will be created from a separate function
                    {
                    } else // MARK AS DONE
                    {
                        Inform infObj = null; // line taken up from bottom by pr on 7thmay19

                        System.out.println(" statremarks before mark as done are " + statRemarks);
                        if (!statRemarks.equals("") && !statRemarks.matches(statRemarksRegEx)) // line modified by pr on 19thfeb18 
                        {
                            statRemarks = ""; // line added by pr on 7thfeb18 for xss
                            isSuccess = false;
                            isError = true;
                            msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // line modified by pr on 28thdec18
                        } //else if (formName.equals(Constants.WIFI_FORM_KEYWORD) && isWifiDelete)// else if added by pr on 7thmay19, to incorporate delete functionality in wifi form
                        else if (formName.equals(Constants.WIFI_FORM_KEYWORD) && checkDeleteRequest(arr[0]))// modified by pr on 19thaug2020
                        {
                            isWifiDelete = true; // set the value fresh after checking from server side                             
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                    + " inside wifi delete request for reg no " + arr[0]);

                            /*System.out.println(" inside else if wifi delete if isWifiDelete value is " + isWifiDelete);
                            boolean wifiFlag = false;

                            if (wifi_value != null) {
                                if (wifi_value.trim().isEmpty() || wifi_value.trim().equals("0")) {
                                    wifiFlag = true;
                                }
                            } else {
                                wifiFlag = true;
                            }

                            if (wifiFlag) {*/ // code commented by pr on 19thaug2020
                            // delete from wifi_mac_os table with respect to the registration number
                            boolean isDeleted = updateDeleteWifi(reg_no);

                            if (isDeleted) {
                                System.out.println(" inside wifi iswifidelete true is deleted true " + isDeleted);

                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                        + " inside wifi delete request for reg no " + arr[0] + " deleted true ");

                                Boolean status = fwdObj.updateStatus(arr[0], "completed", arr[1], statRemarks, "", "", "");
                                statusUpdated = status; // line added by pr on 2nddec19                                

                                if (infObj == null) // if around added by pr on 18thmar19
                                {
                                    infObj = new Inform();
                                }

                                infObj.sendCompIntimation(arr[0], arr[1], "", "");// these two lines added by pr inside status true by pr on 5thfeb18

                                statRemarks = "";
                                isSuccess = true;
                                isError = false;
                                msg = "Mac Address(s) Deleted Successfully";
                            } else {
                                System.out.println(" inside wifi iswifidelete true is deleted FALSE " + isDeleted);

                                statRemarks = ""; // line added by pr on 7thfeb18 for xss
                                isSuccess = false;
                                isError = true;
                                msg = "Mac Address(s) couldnot be Deleted";
                            }
                            /*} else {
                                statRemarks = ""; // line added by pr on 7thfeb18 for xss
                                isSuccess = false;
                                isError = true;
                                msg = "Mac Address(s) couldnot be Deleted";
                            }*/
                        } else if (formName.equals(Constants.WIFI_FORM_KEYWORD) && !wifi_value.matches("^[0-9]{1,2}$"))// else if added by pr on 10thaug18 
                        {
                            statRemarks = ""; // line added by pr on 7thfeb18 for xss
                            isSuccess = false;
                            isError = true;
                            msg = "Please select Valid Wifi Value";
                        } else if (formName.equals(Constants.EMAILACTIVATE_FORM_KEYWORD) && !validateDORAndUpdate(reg_no, dor))// else if added by pr on 23rdapr2020
                        {
                            statRemarks = "";
                            isSuccess = false;
                            isError = true;
                            msg = "Please select Valid DOR Value";
                        } else {
                            // start , code added by pr on 12thapr19
                            // put a check for zimbra token in  case of wifi , mobile update, imap pop forms because in that case we need to update in the zimbra ldap as well
                            admin_token = "not_valid"; // it will remain so in case of other than mobile, wifi and imap forms
                            if (formName.equals(Constants.MOB_FORM_KEYWORD) || formName.equals(Constants.WIFI_FORM_KEYWORD) || formName.equals(Constants.IMAP_FORM_KEYWORD) || formName.equals(Constants.EMAILACTIVATE_FORM_KEYWORD) || formName.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD) || formName.equals(Constants.DOR_EXT_FORM_KEYWORD)) {

                                if (!isWifiDelete) {

                                    try {
                                        url = new URL("https://100.80.17.250:7071/service/admin/soap/");
                                        //admin_token = getAdminAuthToken("createuserapp@createaccount.da", "fgaT8.8A_bGm", url);
                                        //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "A@#poi$^KJ39", url); 
                                        //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "#ZyPE@8xR#H$TJxT9^H=CTZ@#43f#Dg4dV#Xz@", url);
                                        //   getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "#ZyPE@8xR#H$TJxT9^H=CTZ@#43f#Dg4dV#Xz@", url);
                                        admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "Q=2E+D4Kf=BYi+K7De=4TjA+xFq6Z=T7+hDs=4x", url);

                                    } catch (Exception ex) {
                                        System.out.println("Exception in admin URL in create ID function for mob , wifi, imap forms hit ::::::::::::::::::");
                                    }
                                }
                            } // if block commented by pr on 1stnov19 for testing, to be uncommented in production
                            if (admin_token != null && admin_token.trim().isEmpty()) // this is the case of error in case of mobile, wifi and imap forms
                            {
                                isSuccess = false;
                                isError = true;
                                msg = "Zimbra LDAP Token is empty.";
                            } else {
                                // end, code added by pr on 12thapr19
                                // start, code added for ldap mod on 5thdec17
                                // write code to update an attribute in LDAP in case of forms like wifi, mobile update and imap pop
                                //Inform infObj = null; // line added by pr on 18thmar19 , line commented on 7thmay19
                                if (formName.equals(Constants.MOB_FORM_KEYWORD)) {
                                    HashMap hm = fwdObj.fetchFormDetail(reg_no, formName);
                                    formDetailHM = hm; // line added by pr on 2nddec19
                                    String email = "", new_mobile = "", country_code = "+91", nicDateOfBirth = "", nicDateOfRetirement = "", designation = "", displayName = "";
                                    if (hm != null && hm.get("email") != null) {
                                        if (hm.get("email") != null) {
                                            email = hm.get("email").toString();
                                        }
                                        if (hm.get("new_mobile") != null) {
                                            new_mobile = hm.get("new_mobile").toString();
                                        }
                                        if (hm.get("country_code") != null) {
                                            country_code = hm.get("country_code").toString();
                                        }
                                        if (hm.get("nicDateOfBirth") != null) {

                                            String dateOfBirth = hm.get("nicDateOfBirth").toString();
                                            nicDateOfBirth = utcFormat(dateOfBirth);
                                        }
                                        if (hm.get("nicDateOfRetirement") != null) {
                                            String dateOfRet = hm.get("nicDateOfRetirement").toString();
                                            nicDateOfRetirement = utcFormat(dateOfRet);
                                        }
                                        if (hm.get("designation") != null) {
                                            designation = hm.get("designation").toString();
                                        }
                                        if (hm.get("displayName") != null) {
                                            displayName = hm.get("displayName").toString();
                                        }

                                    }

                                    if (!email.equals("") && !country_code.equals("") && !new_mobile.equals("")) {
                                        String dn = fwdObj.fetchLDAPDN(email, formName);
                                        HashMap<String, String> zim_values = new HashMap<String, String>();
                                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "DN " + dn);
                                        if (!dn.equals("")) {
                                            if ((new_mobile.startsWith("+91") && new_mobile.length() == 13) || (new_mobile.startsWith("91") && new_mobile.length() == 12)) {
                                                modifyLDAPAttribute(dn, "mobile", new_mobile);
                                                modifyLDAPAttribute(dn, "nicDateOfBirth", nicDateOfBirth);
                                                modifyLDAPAttribute(dn, "nicDateOfRetirement", nicDateOfRetirement);
                                                modifyLDAPAttribute(dn, "designation", designation);
                                                modifyLDAPAttribute(dn, "displayName", displayName);

                                                //attributes to update in zimbra ldap////
                                                zim_values.put("mobile", new_mobile);
                                                zim_values.put("legacyDateOfBirth", nicDateOfBirth);
                                                zim_values.put("legacyDateOfRetirement", nicDateOfRetirement);
                                                zim_values.put("title", designation);
                                                zim_values.put("displayName", displayName);
                                                modifyZimLDAPAttribute(admin_token, url, email, zim_values, Constants.MOB_FORM_KEYWORD);// above line commented this line modified by pr on 12thapr19

                                            } else if ((!new_mobile.startsWith("+91") && new_mobile.length() == 10) || (!new_mobile.startsWith("91") && new_mobile.length() == 10)) {
                                                modifyLDAPAttribute(dn, "mobile", country_code + new_mobile);
                                                modifyLDAPAttribute(dn, "nicDateOfBirth", nicDateOfBirth);
                                                modifyLDAPAttribute(dn, "nicDateOfRetirement", nicDateOfRetirement);
                                                modifyLDAPAttribute(dn, "designation", designation);
                                                modifyLDAPAttribute(dn, "displayName", displayName);
                                                // modifyActivateAttribute(dn, zim_values, formName);

                                                zim_values.put("mobile", country_code + new_mobile);
                                                zim_values.put("legacyDateOfBirth", nicDateOfBirth);
                                                zim_values.put("legacyDateOfRetirement", nicDateOfRetirement);
                                                zim_values.put("title", designation);
                                                zim_values.put("displayName", displayName);
                                                modifyZimLDAPAttribute(admin_token, url, email, zim_values, Constants.MOB_FORM_KEYWORD);// above line commented this line modified by pr on 12thapr19

                                            }

                                            // start, code added by pr on 12thapr9
                                            //zim_values.put("form_name", Constants.MOB_FORM_KEYWORD);
                                            // end, code added by pr on 12thapr9
                                        }
                                    }
                                } else if (formName.equals(Constants.WIFI_FORM_KEYWORD)) {
                                    // start, code added by pr on 10thaug18 
                                    // update the wifi value in the DB
                                    // below line added by pr on 14thnov19
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside wifi value valid wifi_value " + wifi_value);
                                    if (!isWifiDelete) {
                                        updateWifiValue(reg_no, wifi_value);
                                    }
                                    // end, code added by pr on 10thaug18 
                                    HashMap hm = fwdObj.fetchFormDetail(reg_no, formName);
                                    formDetailHM = hm; // line added by pr on 2nddec19
                                    String email = "", wifi_type = ""; //wifi_value = "";// wifi_value commented by pr on 14thnov19
                                    if (hm != null && hm.get("email") != null) {
                                        if (hm.get("email") != null) {
                                            email = hm.get("email").toString();
                                        }
                                        if (hm.get("wifi_type") != null) {
                                            wifi_type = hm.get("wifi_type").toString();
                                        }
//                                        if (hm.get("wifi_value") != null) {
//                                            wifi_value = hm.get("wifi_value").toString();
//                                        } // wifi_value commented by pr on 14thnov19
                                    }
                                    if (wifi_value != null && !wifi_value.trim().isEmpty() && !wifi_value.equals("0")) {
                                        if (!email.isEmpty()) {
                                            String dn = fwdObj.fetchLDAPDN(email, formName);
                                            if (!dn.isEmpty()) {
                                                if (!isWifiDelete) {
                                                    modifyLDAPAttribute(dn, "nicWIFI", wifi_value);
                                                    modifyLDAPAttribute(dn, "SunPresencePrivacy", "1");
                                                    // start, code added by pr on 12thapr19
                                                    HashMap<String, String> zim_values = new HashMap<String, String>();
                                                    zim_values.put("legacyWifi", wifi_value);
                                                    //zim_values.put("form_name", Constants.WIFI_FORM_KEYWORD); // line commented by pr on 12thapr19
                                                    modifyZimLDAPAttribute(admin_token, url, email, zim_values, Constants.WIFI_FORM_KEYWORD);
                                                }
                                                // end, code added by pr on 12thapr19
                                                // start, code added by pr on 5thnov18
                                                // generate certificate when wifi_value is 2(vayu) and wifi_os1 or wifi_os2 or wifi_os3 as ios
                                                /*if( hm.get("wifi_process") != null )
                                         {
                                         System.out.println(" inside wifi process not null ");
                                            
                                         if( hm.get("wifi_process").equals("request") )
                                         {
                                         System.out.println(" inside wifi process not null request ");
                                                
                                         if( wifi_value.equals("2") )
                                         {                                                    
                                         System.out.println(" inside wifi value 2 ");
                                                    
                                         String wifi_os1 = "", wifi_os2 = "", wifi_os3 = "";
                                         if (hm.get("wifi_os1") != null) 
                                         {
                                         wifi_os1 = hm.get("wifi_os1").toString();
                                         }
                                         if (hm.get("wifi_os2") != null) 
                                         {
                                         wifi_os2 = hm.get("wifi_os2").toString();
                                         }
                                         if (hm.get("wifi_os3") != null) 
                                         {
                                         wifi_os3 = hm.get("wifi_os3").toString();
                                         }
                                                    
                                         System.out.println(" wifi_os1 value is "+wifi_os1);
                                                    
                                         if( wifi_os1.contains("ios") ||  wifi_os2.contains("ios") || wifi_os3.contains("ios") )
                                         {                                                         
                                         System.out.println(" inside wifi process not null request iossssssssss ");
                                                        
                                         generateWiFiPDF( reg_no );
                                         }
                                         }                                            
                                         }
                                         else if( hm.get("wifi_process").equals("certificate") )
                                         {
                                         System.out.println(" inside wifi process not null certificate ");
                                                
                                         generateWiFiPDF( reg_no );
                                         }                                        
                                        
                                         }*/
                                                // end, code added by pr on 5thnov18                                                                                
                                            }
                                        }
                                    }
                                } else if (formName.equals(Constants.IMAP_FORM_KEYWORD)) { // else if added by pr on 27thaug18
                                    System.out.println(" inside imap pop  imap_value is " + imap_value);
                                    if (imap_value.equals("y")) // if imap_value is y then update in the LDAP as well else dont
                                    {
                                        HashMap hm = fwdObj.fetchFormDetail(reg_no, formName);
                                        formDetailHM = hm; // line added by pr on 2nddec19
                                        String protocol = "", email = "", dn = "", attValue = "";
                                        if (hm != null) {
                                            if (hm.get("protocol") != null) {
                                                protocol = hm.get("protocol").toString().trim();
                                            }
                                            if (hm.get("email") != null) {
                                                email = hm.get("email").toString().trim();
                                            }
                                        }
                                        //dn = fetchLDAPDN(email, formName); // get ldap dn so as to pass inside the function modifyLDAPAttribute(String dn, String attrName, String attrValue)// line commented by pr on 18thmar19
                                        // start, code added by pr on 18thmar19
                                        // get the dn, zimotp present or not, mailallowedaccessservice value from a function that connects with the LDAP
                                        // now check if zimpotp attribute is present , if its not present then do the below as same as earlier
                                        // if zimotp is present then check for http and https presence in mailallowedaccesservice , its present just send a mail to zimbra team
                                        // if http and https is not present then update as done below and send a mail to zimbra team as well.
                                        HashMap<String, String> HMZim = fetchZimOtp(email);
                                        boolean sendZimMail = false;
                                        boolean updateSun = false;
                                        String service = "", isZimotp = "";
                                        if (HMZim != null && HMZim.size() > 0) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchZimOtp hashmap ");
                                            if (HMZim.get("service") != null) {
                                                service = HMZim.get("service").toString();
                                            }
                                            if (HMZim.get("dn") != null) {
                                                dn = HMZim.get("dn").toString();
                                            }
                                            if (HMZim.get("zimotp") != null) {
                                                isZimotp = HMZim.get("zimotp").toString();
                                            }
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchZimOtp hashmap service is " + service + " dn is " + dn + " zimotp is " + isZimotp);
                                            if (isZimotp.equals("false")) {
                                                // update only in sun and dont send mail to zim
                                                updateSun = true;
                                            } else // check if partially migrated or fully migrated by cheching http or https
                                            {
                                                // partially migrated
                                                if (!service.toLowerCase().contains("http")) {
                                                    updateSun = true;
                                                }
                                                sendZimMail = true;
                                            }
                                        }
                                        // end, code added by pr on 18thmar19
                                        if (!dn.equals("")) {
                                            if (updateSun) // if around added by pr on 18thmar19
                                            {
                                                // call a function to get the attvalue to update in the LDAP
                                                attValue = fetchAttributeValue(email, protocol);
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                                        + " before updating allowedserviceaccess value to " + attValue);
                                                modifyLDAPAttribute(dn, "mailallowedserviceaccess", attValue);
                                            }
                                            // start, code added by pr on 18thmar19                                    
                                            if (sendZimMail) {
                                                /*infObj = new Inform();
                                        infObj.sendZimMail(email, protocol);*/
                                                // above code commented below added by pr on 12thapr19
                                                // start, code added by pr on 12thapr19
                                                HashMap<String, String> zim_values = new HashMap<String, String>();
                                                if (protocol.equalsIgnoreCase("imap")) {
                                                    zim_values.put("zimbraImapEnabled", "TRUE");
                                                } else {
                                                    zim_values.put("zimbraImapEnabled", "FALSE");
                                                }
                                                if (protocol.equalsIgnoreCase("pop")) {
                                                    zim_values.put("zimbraPop3Enabled", "TRUE");
                                                } else {
                                                    zim_values.put("zimbraPop3Enabled", "FALSE");
                                                }
                                                //zim_values.put("form_name", Constants.IMAP_FORM_KEYWORD);
                                                modifyZimLDAPAttribute(admin_token, url, email, zim_values, Constants.IMAP_FORM_KEYWORD); // above line commneted this line modified by pr on 12thapr19
                                                // end, code added by pr on 12thapr19                                      
                                            }
                                            // end, code added by pr on 18thmar19
                                        }
                                    }
                                } else if (formName.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {
                                    Map UserDetail = new HashMap();
                                    DaOnboardingDao daDao = new DaOnboardingDao();
                                    List<String> userValue = new ArrayList();
                                    userValue = daDao.fetchUserEmail(reg_no);
                                    String userEmail = "", bo_name = "", coodinater_email = "", vpn_ip = "", user_mobile = "";
                                    userEmail = userValue.get(0);
                                    System.out.println("EmailLLLL:::::::::::::: " + userEmail);
                                    bo_name = userValue.get(1);
                                    vpn_ip = userValue.get(2);
                                    coodinater_email = userValue.get(3);
                                    user_mobile = userValue.get(4);
                                    UserDetail = daDao.fetchUserDetails(userEmail);
                                    System.out.println(" User Eamil :::::::::  " + userEmail);
                                    String responseDaService = "";

                                    // responseDaService = daDao.dabase_entry(bo_name, userEmail, vpn_ip, coodinater_email, ip);
                                    // System.out.print("Details of response Da Service ::::: " + responseDaService);
                                    /*
                                    if (responseDaService.isEmpty() || responseDaService.equalsIgnoreCase("ERROR") || responseDaService.equalsIgnoreCase("")) {
                                        setRespdataserv("err");
                                                    random = "";// line added by pr on 6thfeb18
                                   isSuccess = false;
                                  isError = true;
                                   msg = "ID could not be created. Invalid remarks";// modified by pr on 3rdoct19
                                     return SUCCESS;   
                                     //return ERROR;
                                    }
                                     */
                                    //daDao.insertIntoDaAdmin(bo_name, vpn_ip, coodinater_email, userEmail);
                                    //  String ip = ServletActionContext.getRequest().getRemoteAddr();
                                    //daDao.insertIntoModifiedIpLogs(bo_name, vpn_ip, coodinater_email, userEmail,ip);
                                    //  String dn = fwdObj.fetchLDAPDN("test-bo-admin", formName);
                                    //  System.out.println("bo name ====== :::::::::::::::::: "+ bo_name);
                                    //  String mobile = ldap.fetchMobileFromLdapDaOnboarding("test-bo-admin");
                                    //   System.out.println("mobile:::::" + mobile);

                                    /*
                                    String mobileLdap = mobile.replace('+', '-');
                                    System.out.println(mobileLdap);
                                    String mobilearry[] = mobileLdap.split("-");
                                    //int mobile_number_count = mobilearry.length; 
                                    String FinalMobile = "";
                                    
                                    for (String s : mobilearry) {
                                        FinalMobile = FinalMobile + s + ",+";
                                        System.out.println("final value :::: " + FinalMobile);
                                        //if()
                                    }
                                    FinalMobile=StringUtils.chop(FinalMobile);
                                    FinalMobile=StringUtils.chop(FinalMobile);
                                   // FinalMobile=FinalMobile.replace(',', ' ');
                                   FinalMobile=FinalMobile.replaceFirst(",", " ");
                                    FinalMobile.trim();
                                    
                                    
                                    System.out.println("Finam mobile number is = "+FinalMobile);
                                    /*
                                    for(int i=0; i<mobilearry.length; i++){
                                        System.out.println("mobileNumber= "+mobilearry[i]);
                                        
                                    }
                                    
                                    FinalMobile = FinalMobile.substring(1, FinalMobile.length());
                                                                        System.out.println("final value :::: " + FinalMobile);
                                    System.out.println(" dn :::::::::  " + dn + "mobile:::::" + mobile + "comaMobile:::::" + FinalMobile);
                                     */
                                    //HashMap<String, String> HMZim = fetchZimOtp(coodinater_email);
                                    //     HashMap<String, String> zim_values = new HashMap<String, String>();
                                    //   System.out.println("userValue.get(4)" + userValue.get(4));
                                    // zim_values.put("mobile", mobile + "," + userValue.get(4));
                                    // modifyActivateAttribute(dn, zim_values, formName); // consolidate into a hashmap with key as attribute name and value as attribute value
                                    //modifyZimLDAPAttribute(admin_token, url, coodinater_email, zim_values, Constants.EMAILACTIVATE_FORM_KEYWORD); // above line commneted this line modified by pr on 12thapr19
                                    //   String updated_mobile = ldap.fetchMobileFromLdapDaOnboarding("test-bo-admin");
                                    //  if (updated_mobile.length() > mobile.length()) {
                                    List<String> check_box_values = new ArrayList();
                                    check_box_values = daDao.checkBoxChecking(reg_no);
                                    String valueof_check_box = check_box_values.get(0);

                                    daDao.insertIntoEmploymentCoordinator(bo_name, vpn_ip, coodinater_email, UserDetail, userEmail);

                                    if (valueof_check_box.equalsIgnoreCase("on")) {

                                        daDao.insertIntoIgnore_bo_update_mobile(bo_name, user_mobile, userEmail, reg_no);

                                    }

                                    //  } else {
                                    //        return ERROR;
                                    //     }
                                } else if (formName.equals(Constants.FIREWALL_FORM_KEYWORD)) { // else if added by pr on 21stjun19

                                    System.out.println(" inside FIREWALL_FORM_KEYWORD location value is " + location);

                                    // add code here to add the location in base table
                                    updateFirewallLocation(reg_no, location);

                                } // end, code added for ldap mod on 5thdec17
                                else if (formName.equals(Constants.WIFI_PORT_FORM_KEYWORD)) { // else if added by pr on 21stjun19

                                    System.out.println(" inside WIFI_PORT_FORM_KEYWORD location value is " + location);

                                    // add code here to add the location in base table
                                    updateWifiportLocation(reg_no, location);

                                } else if (formName.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {

                                    HashMap hm = fwdObj.fetchFormDetail(reg_no, formName);

                                    // check for emp type if non regular then check for dob value as passed from admin
                                    // validation check on dor if all well then go ahead else stop and throw error message
                                    formDetailHM = hm; // line added by pr on 2nddec19
                                    String email = "", dn = "", attValue = "";
                                    if (hm != null) {
                                        if (hm.get("preferred_email1") != null) {
                                            email = hm.get("preferred_email1").toString().trim();
                                        }
                                    }
                                    dn = fwdObj.fetchLDAPDN(email, formName);
                                    HashMap<String, String> HMZim = fetchZimOtp(email);
                                    String dexp = hm.get("dor").toString().trim();
                                    String pass = fetchRandomPassword();
                                    HashMap<String, String> zim_values = new HashMap<String, String>();
                                    zim_values.put("mailuserstatus", "active");
                                    zim_values.put("inetuserstatus", "active");
                                    zim_values.put("inetsubscriberaccountid", statRemarks);
                                    zim_values.put("nicAccountExpDate", dexp);
                                    zim_values.put("nicDateOfRetirement", dexp);
                                    zim_values.put("nicnewuser", "true");
                                    zim_values.put("userpassword", pass);
                                    modifyActivateAttribute(dn, zim_values, formName); // consolidate into a hashmap with key as attribute name and value as attribute value
                                    modifyZimLDAPAttribute(admin_token, url, email, zim_values, Constants.EMAILACTIVATE_FORM_KEYWORD); // above line commneted this line modified by pr on 12thapr19

                                } else if (formName.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {
                                    String pass = fetchRandomPassword();
                                    HashMap hm = fwdObj.fetchFormDetail(reg_no, formName);
                                    formDetailHM = hm; // line added by pr on 2nddec19
                                    String email = "", dn = "", attValue = "";
                                    if (hm != null) {
                                        if (hm.get("preferred_email1") != null) {
                                            email = hm.get("preferred_email1").toString().trim();
                                        }
                                    }
                                    dn = fwdObj.fetchLDAPDN(email, formName);
                                    HashMap<String, String> HMZim = fetchZimOtp(email);
                                    HashMap<String, String> zim_values = new HashMap<String, String>();
                                    zim_values.put("mailuserstatus", "inactive");
                                    zim_values.put("inetuserstatus", "inactive");
                                    zim_values.put("inetsubscriberaccountid", statRemarks);
                                    zim_values.put("userpassword", pass);
                                    modifyActivateAttribute(dn, zim_values, formName); // consolidate into a hashmap with key as attribute name and value as attribute value
                                    //modifyZimLDAPAttribute(admin_token, url, email, zim_values, Constants.EMAILDEACTIVATE_FORM_KEYWORD); // above line commneted this line modified by pr on 12thapr19

                                } else if (formName.equals(Constants.DOR_EXT_FORM_KEYWORD)) {

                                    HashMap hm = fwdObj.fetchFormDetail(reg_no, formName);

                                    formDetailHM = hm;
                                    String email = "", dn = "", attValue = "";
                                    if (hm != null) {
                                        if (hm.get("dor_email") != null) {
                                            email = hm.get("dor_email").toString().trim();
                                        }
                                    }
                                    //sahil
                                    dn = fwdObj.fetchLDAPDN(email, formName);
                                    HashMap<String, String> HMZim = fetchZimOtp(email);
                                    String dexp = hm.get("dor").toString().trim();
                                    String dob = hm.get("dob").toString().trim();
                                    String dexp1 = utcFormat(dexp);
                                    String dob1 = utcFormat(dob);

                                    HashMap<String, String> zim_values = new HashMap<String, String>();
                                    zim_values.put("nicAccountExpDate", dexp1);
                                    zim_values.put("nicDateOfRetirement", dexp1);
                                    zim_values.put("nicDateOfBirth", dob1);

                                    modifyActivateAttribute(dn, zim_values, formName);

                                    HashMap<String, String> zim_values1 = new HashMap<String, String>();
                                    zim_values1.put("legacyDateOfRetirement", dexp1);
                                    zim_values1.put("legacyAccountExpDate", dexp1);
                                    zim_values1.put("legacyDateOfBirth", dob1);

                                    System.out.println("legacy map" + zim_values1);

                                    // modifyZimLDAPAttribute(admin_token, url, email, zim_values1, Constants.DOR_EXT_FORM_KEYWORD);
                                }

                                Boolean status = fwdObj.updateStatus(arr[0], "completed", arr[1], statRemarks, "", "", "");
                                statusUpdated = status; // line added by pr on 2nddec19                                
                                //Boolean status = false ; // above line commented this added by pr on 28thaug18 for testing
                                if (status) {
                                    if (formName.equals(Constants.DNS_FORM_KEYWORD)) // else if added by pr on 7thjun18, placed at the this status true block by pr on 20thjun18
                                    {
                                        jsn jsnObj = new jsn(); // taken up from try block by pr on 3rddec19

                                        //HashMap hm = fetchFormDetail(reg_no, formName);
                                        String response = "";
                                        try {
                                            response = jsnObj.hitDNSUrl(reg_no);
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Inside jsnobj hotDNUrl RESPONSE " + response);
                                            if (response != null && !response.isEmpty()) {
                                                if (response.contains("Data pushed successfully")) {
                                                    response = "SUCCESS";
                                                } else {
                                                    response = "FAIL";
                                                }
                                            } else {
                                                response = "FAIL";
                                            }
                                        } catch (Exception ex) {
                                            response = "FAIL";
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Inside jsnobj hotDNUrl exception " + ex.getMessage()); // getMessage added by pr on 6thjul18
                                            //ex.printStackTrace();
                                        }

                                        // start, code added by pr on 3rddec19
                                        String response1 = "";

                                        if (isDomainAPI(reg_no)) // if its a case of domain api then only do the below task
                                        {
                                            try {
                                                // call another api if its a case of domainapi

                                                response1 = "FAIL";

                                                // start, code added by pr on 7thjan2020
                                                String req_for = "";

                                                System.out.println(" before dns push api call formDetailHM value is " + formDetailHM);

                                                if (formDetailHM != null) {
                                                    req_for = formDetailHM.get("req_for").toString(); // line added by pr on 7thjan2020
                                                } else {
                                                    formDetailHM = fwdObj.fetchFormDetail(reg_no, formName);

                                                    req_for = formDetailHM.get("req_for").toString(); // line added by pr on 7thjan2020
                                                }

                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " req_for value is " + req_for); // getMessage added by pr on 6thjul18

                                                // end, code added by pr on 7thjan2020
                                                response1 = jsnObj.dns_push_domains(reg_no, req_for, "approved");
                                                // response1 = jsnObj.dns_push_domains(reg_no, req_for); // second attribute added  by pr on 7thjan2020

                                                if (response1.toLowerCase().contains("success")) {
                                                    response1 = "SUCCESS";
                                                }
                                            } catch (Exception e) {

                                                e.printStackTrace();

                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Inside jsnobj dns_push_domains exception " + e.getMessage()); // getMessage added by pr on 6thjul18
                                            }
                                        }

                                        // end, code added by pr on 3rddec19
                                        // update the response in the dns_api_response table and send emails to the admins related to dns api integration
                                        updateDNSAPIResp(reg_no, response, response1);

                                    }
                                    // send intimation
                                    if (infObj == null) // if around added by pr on 18thmar19
                                    {
                                        infObj = new Inform();
                                    }
                                    infObj.sendCompIntimation(reg_no, formName, "", "");// these two lines added by pr inside status true by pr on 5thfeb18
                                    isSuccess = true;
                                    isError = false;
                                    if (formName.equalsIgnoreCase(Constants.DAONBOARDING_FORM_KEYWORD)) {
                                        msg = "You have successfully completed this request. We are not updating LDAP and DA database, please update it your self.";
                                    } else {
                                        msg = "Status Updated Successfully ";
                                    }
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    msg = "Status could not be Updated Successfully ";
                                }
                            } // admin token else ends here added by pr on 12thapr19
                        } // outer else part ends here
                    }

                    // start, code added by pr on 2nddec19
                    try {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " createid send all files statusUpdated " + statusUpdated); // getMessage added by pr on 6thjul18    

                        if (formDetailHM == null) {
                            formDetailHM = fwdObj.fetchFormDetail(reg_no, formName);
                        }

                        if (statusUpdated) {
                            finalCompleteMailFiles(reg_no, formDetailHM, formName);
                        }
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " createid send files exception " + e.getMessage()); // getMessage added by pr on 6thjul18    
                    }

                    // end, code added by pr on 2nddec19
                }// close of if check for verifytfwdtodetails added by pr on 20thsep19
                else {
                    isSuccess = false;
                    isError = true;
                    msg = "You are not allowed to complete this Request.";
                }

            } else // else added by pr on 5thfeb18
            {
                isSuccess = false;
                isError = true;
                msg = "ID could not be created. Invalid Reg No.";// modified by pr on 3rdoct19
            }
        } else // else added by pr on 3rdjan18
        {
            random = "";// line added by pr on 6thfeb18
            isSuccess = false;
            isError = true;
            msg = "ID could not be created. Invalid remarks";// modified by pr on 3rdoct19
        }
        if (po != null && !po.equals("")) {
            po = ESAPI.encoder().encodeForHTMLAttribute(po);
        }
        if (bo != null && !bo.equals("")) {
            bo = ESAPI.encoder().encodeForHTMLAttribute(bo);
        }
        if (domain != null && !domain.equals("")) {
            domain = ESAPI.encoder().encodeForHTMLAttribute(domain);
        }
        if (final_sms_id != null && !final_sms_id.equals("")) {
            final_sms_id = ESAPI.encoder().encodeForHTMLAttribute(final_sms_id);
        }
        if (regNo != null && !regNo.equals("")) {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        if (statRemarks != null && !statRemarks.equals("")) {
            statRemarks = ESAPI.encoder().encodeForHTMLAttribute(statRemarks);
        }
        if (random != null && !random.equals("")) {
            random = ESAPI.encoder().encodeForHTMLAttribute(random);
        }
        System.out.println(" inside createid function value of msg is " + msg);
        return SUCCESS;
    }

    // method added by ram and ankit on 12 august 
    public String post_createId(String uid_to_api, String email_to_api, String boname, String name_to_api, String mobile_to_api) throws MalformedURLException {

        URL url = new URL("http://10.122.34.101:8082/da/insert/account/");
        try {
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-Type", "application/json");

            JSONObject apiData = new JSONObject();
            apiData.put("uid", uid_to_api);
            apiData.put("email", email_to_api);
            apiData.put("boName", boname);
            apiData.put("name", name_to_api);
            apiData.put("mobile", mobile_to_api);
            apiData.put("inet_userstatus", "Active");
            apiData.put("mail_userstatus", "Active");

            String data = apiData.toString();

            byte[] out = data.getBytes(StandardCharsets.UTF_8);

            OutputStream stream = http.getOutputStream();
            stream.write(out);
            System.out.println(data);
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            http.disconnect();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "success";
    }

    // below function added by pr on 19thaug2020
    public boolean checkDeleteRequest(String regNo) {
        boolean flag = false;

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select wifi_process FROM wifi_registration WHERE registration_no = ? AND trim(wifi_process) = 'req_delete' ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isWifiDelete ps is" + ps);

            rs = ps.executeQuery();
            while (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isWifiDelete exception " + e.getMessage());
        }
        return flag;
    }

    // below function added by pr on 23rdapr2020
    public boolean validateDORAndUpdate(String reg_no, String dor) {
        boolean flag = false;

        try {

            HashMap hm = fwdObj.fetchFormDetail(reg_no, Constants.EMAILACTIVATE_FORM_KEYWORD);

            if (hm != null && hm.size() > 0) {
                String emp_type = "";

                if (hm.get("emp_type") != null) {
                    emp_type = hm.get("emp_type").toString();
                }

                if (emp_type.equals("emp_regular")) {
                    flag = true;
                } else {
                    // check the validity of the dor and then save it in the base table

                    validation.validation validObj = new validation.validation();

                    String msg = validObj.activatedorValidation(dor);

                    if (msg.equals("")) {
                        flag = true;

                        // update it in the db
                        con = DbConnection.getConnection();
                        String qry = " UPDATE " + Constants.EMAILACTIVATE_TABLE_NAME + " SET dor = ? WHERE registration_no = ? ";
                        PreparedStatement ps = con.prepareStatement(qry);
                        ps.setString(1, dor);
                        ps.setString(2, reg_no);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                                + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                + " == " + " insinde validateDORAndUpdate query is " + ps);
                        int i = ps.executeUpdate();

                        if (i > 0) {

                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                    + " == " + " insinde validateDORAndUpdate DOR updated as " + dor + " for reg no " + reg_no);
                        }
                    }
                }

            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " validateDORAndUpdate exception for reg no " + reg_no + " dor " + dor);

        }

        return flag;
    }

    // below function added by pr on 3rddec19
    public boolean isDomainAPI(String reg_no) {
        boolean flag = false;

        PreparedStatement ps = null;

        ResultSet rs = null;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select stat_type FROM status WHERE stat_reg_no = ? AND stat_type = 'domainapi' ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isDomainAPI ps is" + ps);

            rs = ps.executeQuery();
            while (rs.next()) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isDomainAPI exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "isDomainAPI Replace printstcaktrace EXCEPTION 116" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "isDomainAPI Replace printstcaktrace EXCEPTION 117" + e.getMessage());
                }
            }

        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isDomainAPI flag for reg no " + reg_no + " is" + flag);

        return flag;
    }

    // below function added by pr on 5thapr18
    public String fetchMobileDuplicates() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " regno value is " + regNo + " formName value is " + formName);
        mobile_ldap_email = "";
        String mobile = "";
        HashMap hm = null;
        hm = fwdObj.fetchSingleDetail(regNo);
        if (formName != null && formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD) && !regNo.equals("")) {
            hm = fetchNKNDetail(regNo);
        } else if (formName != null && formName.equals(Constants.GEM_FORM_KEYWORD) && !regNo.equals("")) {
            hm = fwdObj.fetchFormDetail(regNo, Constants.GEM_FORM_KEYWORD);
        }
        if (hm != null && hm.get("mobile") != null && !hm.get("mobile").equals("")) {
            mobile = hm.get("mobile").toString();
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " regno value is " + regNo + " mobile is " + mobile);
        if (!mobile.equals("")) {
            //mobile_ldap_email = fetchMobileLDAPMail(mobile);
            mobile_ldap_email = fetchMobileLDAPMailAll(mobile); // line modified by pr on 4thjun18 so that in case of single user form all the uid and mail and mail equivalent are visible 
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " final str value is " + mobile_ldap_email);
        return SUCCESS;
    }

    // fetch ldap id creation related details from the user_reg table
    public HashMap fetchNKNDetail(String regNo) {
        ResultSet rs = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        //Connection con = null;
        PreparedStatement ps = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select auth_off_name,department,city,mobile,state,designation,dob,dor, description, type, id_type FROM nkn_registration WHERE registration_no = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                // start, code added by pr on 21stdec17
                String fname = "", lname = "";
                if (!rs.getString("auth_off_name").equals("")) {
                    String[] splited = rs.getString("auth_off_name").split("\\s+");
                    if (splited.length > 0) {
                        fname = splited[0];
                    }
                    if (splited.length > 1) {
                        lname = splited[1];
                    }
                }
                // end, code added by pr on 21stdec17
                hm.put("department", rs.getString("department"));
                hm.put("city", rs.getString("city"));
                hm.put("mobile", rs.getString("mobile"));
                hm.put("fname", fname);
                hm.put("lname", lname);
                hm.put("title", rs.getString("designation"));// line modified by pr on 14thmar18
                if (rs.getString("description") == null || rs.getString("description").isEmpty()) {
                    hm.put("description", "");
                } else {
                    hm.put("description", rs.getString("description"));
                }
                hm.put("state", rs.getString("state"));
                hm.put("dob", rs.getString("dob"));
                hm.put("dor", rs.getString("dor"));
                hm.put("type", rs.getString("type"));
                hm.put("id_type", rs.getString("id_type"));
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function 2" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 116" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 117" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 118" + e.getMessage());
                }
            }
        }
        return hm;
    }

    int quota;
    Map<String, Object> mapQuota = new HashMap();
//  List<Map<String,Object>> quotaList=new ArrayList<>();

    public Map<String, Object> getMapQuota() {
        return mapQuota;
    }

    public void setMapQuota(Map<String, Object> mapQuota) {
        this.mapQuota = mapQuota;
    }

    // new parameter bulk_mail added by pr on 13thdec17 // // two parameters added after uid  by pr on 19thfeb18, DirContext ctx1 added by pr on 8thaug19
    public Boolean createEmailId(DirContext ctx1, String formName, String po, String bo, String domain, String uid, String mail, String reg_no, String pass, String bulk_id, String bulk_mail) {
        boolean closeLDAPCon = false; // line added by pr on 8thaug19 to identify the method is being called for single or bulk

        String to = "pkr.gaurav@nic.in";
        String from = "eforms@nic.in";
        String msg = "Ldap issue in response for available package";
        String sub = "WRONG VALUE FROM LDAP";
        try {
            Map<String, Object> checkquota = servicePackage(bo.trim().toLowerCase(), po.trim().toLowerCase());
            if (checkquota.get("ldaperr") != null) {
                String ldaperr = checkquota.get("ldaperr").toString();
                if (!ldaperr.isEmpty()) {
                    System.out.println("Ldap Error in service package for available package --------");
                    mapQuota.put(bulk_id, "Problem in Ldap response");
                    Mail.sendMail(to, msg, sub, from);

                    return false;
                }

            }

            if (checkquota.get("availablepackage") != null) {
                quota = (int) checkquota.get("availablepackage");

                //for bulk consider mapQuota and for single mail return false
                if (bulk_mail.contains(domain) || mail.contains(domain)) {
                    if (quota <= 0) {
                        if (reg_no.contains("BULKUSER")) {
                            mapQuota.put(bulk_id, "Your Quota is Exhausted");
                        }
                        // quotaList.add(mapQuota);
                        return false;
                    }
                }
            }
        } catch (NamingException ex) {
            Logger.getLogger(AdminAction.class.getName()).log(Level.SEVERE, null, ex);
        }
        // start, code added by pr on 11thapr18
        po = po.trim().toLowerCase();
        bo = bo.trim().toLowerCase();
        domain = domain.trim().toLowerCase();
        uid = uid.trim().toLowerCase();
        mail = mail.trim().toLowerCase();
        // end, code added by pr on 11thapr18
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde createemail id function formname is " + formName);// one parameters added after uid  by pr on 19thfeb18
        HashMap<String, String> rs = null;
        HashMap<String, String> rsForBulk = null;
        String final_id = uid + "@" + domain; // for forms other than bulk , on top on 13thdec17
        String mobilePrefix = "", description = ""; // 
        String idType = "";
        String mailboxType = "";
        // start, code added by pr on 19thfeb18
        String ldap_uid = "", ldap_mail = "", ldap_mailequivalentaddress = "";
        ldap_uid = uid;
        ldap_mail = mail;
        if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
            rs = fwdObj.fetchSingleDetail(reg_no);
            if (rs.get("type") != null && !rs.get("type").isEmpty()) {
                mailboxType = rs.get("type").trim();
            }
            if (rs.get("id_type") != null && !rs.get("id_type").isEmpty()) {
                idType = rs.get("id_type").trim();
            }
            if (rs.get("description") != null && !rs.get("description").isEmpty()) {
                description = rs.get("description");
            }
        } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD)) {
            // get details from the nkn user table
            rs = fetchNKNDetail(reg_no);
            if (rs.get("description") != null && !rs.get("description").isEmpty()) {
                description = rs.get("description");
            }
            if (rs.get("type") != null && !rs.get("type").isEmpty()) {
                mailboxType = rs.get("type").trim();
            }
            if (rs.get("id_type") != null && !rs.get("id_type").isEmpty()) {
                idType = rs.get("id_type").trim();
            }
        } else if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD))// line modified by pr on 27thdec17 to include bulk nkn
        {
            // get details from the bulk user table
            rs = fwdObj.fetchBulkDetail(bulk_id);
            final_id = bulk_mail;// line added by pr on 13thdec17            
            ldap_mail = final_id; // line added by pr on 19thfeb18            
            //mobilePrefix = "+91";// line added by pr on 13thdec17, in case of bulk no 91 is there   // commented by pr on 10thsep18 as now we are saving + with country code and mobile
            if (formName.equals(Constants.BULK_FORM_KEYWORD)) {
                rsForBulk = fwdObj.fetchBulkDetailsFromBaseTable(reg_no);
                if (rsForBulk.get("type") != null && !rsForBulk.get("type").isEmpty()) {
                    mailboxType = rsForBulk.get("type").trim();
                }
                if (rsForBulk.get("id_type") != null && !rsForBulk.get("id_type").isEmpty()) {
                    idType = rsForBulk.get("id_type").trim();
                }
                if (rsForBulk.get("description") != null && !rsForBulk.get("description").isEmpty()) {
                    description = rsForBulk.get("description");
                }
            } else if (formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
                rsForBulk = fetchNKNDetail(reg_no);
                if (rsForBulk.get("description") != null && !rsForBulk.get("description").isEmpty()) {
                    description = rsForBulk.get("description");
                }
                if (rsForBulk.get("type") != null && !rsForBulk.get("type").isEmpty()) {
                    mailboxType = rsForBulk.get("type").trim();
                }
                if (rsForBulk.get("id_type") != null && !rsForBulk.get("id_type").isEmpty()) {
                    idType = rsForBulk.get("id_type").trim();
                }
            }
        } else if (formName.equals(Constants.GEM_FORM_KEYWORD))// else if added by pr on 18thdec17
        {
            // get details from the nkn user table
            rs = fwdObj.fetchFormDetail(reg_no, formName);
            mailboxType = "gem";
            idType = "id_desig";
        }
        if (!ldap_mail.contains("@nic.in")) // if added  by pr on 25thapr18
        {
            ldap_mailequivalentaddress = uid + "@nic.in";
        }
        Boolean valid = true;
        if (rs != null && (po != null && !po.equals("") && po.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))
                && (bo != null && !bo.equals("") && bo.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))
                && (domain != null && !domain.equals("") && domain.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))) // po bo and domain checks added by pr on 5thfeb18 , underscore allowed by pr on 19thfeb18
        {
            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside rs not null inside createEmailId func ");
            String dn_default = "";

            // FETCH BO DETAILS, START
            String sp2 = "", sp3 = "", sp1 = "", mailhost = "", messagestore = "";
            //DirContext ctx1 = null; // commented by pr on 8thaug19
            //DirContext ctx3 = null;// commented by pr on 8thaug19
            try {

                if (ctx1 == null) // if around added by pr on 8thaug19
                {
//                    Hashtable ht = new Hashtable(4);
//                    ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//                    ht.put(Context.PROVIDER_URL, providerurlSlave);
//                    ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//                    ht.put(Context.SECURITY_PRINCIPAL, createdn);
//                    ht.put(Context.SECURITY_CREDENTIALS, createpass); // code commented by pr on 8thaug19

                    //ctx1 = new InitialDirContext(ht); // line commented by pr on 8thaug19
                    ctx1 = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                    closeLDAPCon = true; // in case of single request close, else not
                }

                String[] IDs = {"sunAvailableServices", "preferredMailHost", "preferredMailMessageStore", "sunAvailableDomainNames"};
                SearchControls ctls = new SearchControls();
                ctls.setReturningAttributes(IDs);
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                dn_default = "o=" + bo.trim() + ",o=" + po.trim() + ",o=nic.in,dc=nic,dc=in";
                String filter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo.trim() + "))";
                NamingEnumeration ans = ctx1.search(dn_default, filter, ctls);
                while (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    String s = t.substring(t.indexOf("sunAvailableServices:") + 22, t.indexOf(",")); //for service package
                    String s2 = t.substring(t.indexOf("preferredMailHost:") + 19, t.length()); //for mailhost
                    mailhost = s2.substring(0, s2.indexOf(",")); //for mailhost
                    String s3 = t.substring(t.indexOf("preferredMailMessageStore:") + 27, t.length()); //for messagestore
                    messagestore = s3.substring(0, s3.lastIndexOf("}")); //for messagestore
                    String s4 = t.substring(t.indexOf("sunAvailableDomainNames:") + 25, t.length());
                    domain = s4.substring(0, s4.lastIndexOf(", preferredmailmessagestore"));
                    sp1 = s.substring(0, s.indexOf(":"));
                    String sp2tmp = s.substring(s.indexOf(":") + 1, s.length());
                    if (sp2tmp.contains(":")) {
                        sp2 = (sp2tmp.substring(0, sp2tmp.indexOf(":"))).trim();
                        sp3 = (sp2tmp.substring(sp2tmp.indexOf(":") + 1, sp2tmp.length())).trim();
                    } else {
                        sp2 = sp2tmp.trim();
                        sp3 = "0";
                    }
                }
                List<String> myList = new ArrayList<String>(Arrays.asList(domain.split(",")));
                ArrayList alnew = new ArrayList();
                Iterator itr = myList.iterator();
                while (itr.hasNext()) {
                    String mail1 = ((String) itr.next()).trim(); //mail made mail1 by pr on 19thfeb18
                    if (mail1.equals("nic.in")) { //mail made mail1 by pr on 19thfeb18
                    } else {
                        alnew.add(mail1); //mail made mail1 by pr on 19thfeb18
                    }
                }
                int i = Integer.parseInt(sp2);
                int j = Integer.parseInt(sp3);
                int k = i - j;
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Error in sunAvailableServices Package =>");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 127" + e.getMessage());
            }
            try {
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside try before attributes ");
                int spcount = 0;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String NewDN = "uid=" + uid + ",ou=People," + dn_default;
                Attribute attr1 = new BasicAttribute("psIncludeInGAB", "true");
                Attribute attr2 = new BasicAttribute("icsFirstDay", "2");
                Attribute attr3 = new BasicAttribute("mailMessageStore", messagestore);
                Attribute attr4 = new BasicAttribute("userPassword", pass);
                Attribute attr5 = new BasicAttribute("iplanet-am-modifiable-by", "cn=Top-level Admin Role,dc=nic,dc=in");
                Attribute attr6 = new BasicAttribute("icsTimezone", "Asia/Calcutta");
                Attribute attr7 = new BasicAttribute("givenName", rs.get("fname"));
                Attribute attr8 = new BasicAttribute("mailUserStatus", "active");
                Attribute attr9 = new BasicAttribute("icsStatus", "Active");
                Attribute attr10 = new BasicAttribute("icsCalendar", uid + "@nic.in");
                Attribute attr11 = new BasicAttribute("inetCOS", "NICUserMailCalendar");
                Attribute attr12 = new BasicAttribute("preferredLocale", "en");
                Attribute attr13 = new BasicAttribute("mailHost", mailhost);
                //Attribute attr14 = new BasicAttribute("mailAutoReplyMode", "echo");
                Attribute attr14 = new BasicAttribute("mailAutoReplyMode", "reply"); // line modified by pr on 23rdapr18
                Attribute attr15 = new BasicAttribute("uid", uid);
                Attribute attr16 = new BasicAttribute("preferredLanguage", "en");
                //Attribute attr17 = new BasicAttribute("mail", final_id);
                Attribute attr17 = new BasicAttribute("mail", ldap_mail); // line modified by pr on 19thfeb18                
                //Attribute attr18 = new BasicAttribute("mailequivalentaddress", uid + "@nic.in");
                Attribute attr18 = null; // line added by pr on 25thapr18
                if (!ldap_mailequivalentaddress.equals("")) // if added by pr on 25thapr18
                {
                    attr18 = new BasicAttribute("mailequivalentaddress", ldap_mailequivalentaddress); // line modified by pr on 19thfeb18
                }
                Attribute attr19 = new BasicAttribute("sn", rs.get("lname"));
                //Attribute attr20 = new BasicAttribute("mailDeliveryOption", "mailbox"); // line commented by pr on 26thjun19
                Attribute attr21 = new BasicAttribute("cn", rs.get("fname") + " " + rs.get("lname"));

                // start, code added by pr on 6thmay19
                Date dt = new Date();

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

                String final_date = format.format(dt);

                String desc = description + " New ID Zimbra : " + final_date;
                // Commented by AT on 27th Oct 2022
//                if (rs.get("description") != null && !rs.get("description").equals("")) {
//                    desc = description + " New ID Zimbra : " + final_date;
//                }
                // Comment ended by AT on 27th Oct 2022
                // end, code added by pr on 6thmay19
                //Attribute attr22 = new BasicAttribute("description", rs.get("description"));
                Attribute attr22 = new BasicAttribute("description", desc); // line modified by pr on 6thmay19

                Attribute attr23 = new BasicAttribute("mailDeferProcessing", "No");
                Attribute attr24 = new BasicAttribute("icsDWPHost", "cs1.nic.in");
                Attribute attr25 = new BasicAttribute("inetUserStatus", "Active");
                //Attribute attr26 = new BasicAttribute("nicNewUser", "true");
                Attribute attr26 = new BasicAttribute("nicNewUser", "false"); // line modified by pr on 3rdmar2020
                Attribute attr27 = new BasicAttribute("sunUCTimeZone", "Asia/Calcutta");
                // start, code added  by pr on 10thsep18, to handle the previous 10 digit india code values 
                if (!rs.get("mobile").startsWith("+") && rs.get("mobile").length() == 10) {
                    mobilePrefix = "+91";
                }
                // end, code added  by pr on 10thsep18                
                Attribute attr28 = new BasicAttribute("mobile", mobilePrefix + rs.get("mobile"));// modified on 12thdec17
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "mobile value is " + mobilePrefix + rs.get("mobile"));
                Attribute attr29 = new BasicAttribute("sunUCDateDelimiter", "");
                Attribute attr30 = new BasicAttribute("sunUCDateFormat", "");
                //Attribute attr31 = new BasicAttribute("sunUCDefaultEmailHandler", "");
                Attribute attr31 = new BasicAttribute("sunUCDefaultEmailHandler", "uc"); // line modified by pr on 23rdapr18
                Attribute attr32 = new BasicAttribute("sunUCColorScheme", "");
                Attribute attr33 = new BasicAttribute("sunUCDefaultApplication", "mail");
                Attribute attr34 = new BasicAttribute("sunUCTimeFormat", "");
                Attribute attr35 = new BasicAttribute("mailQuota", "-1");
                Attribute attr36 = new BasicAttribute("st", rs.get("state"));

                Attribute attr37 = new BasicAttribute("title", rs.get("title"));//designation here modified by pr on 14thmar18
                Attribute attr38 = new BasicAttribute("departmentNumber", rs.get("department"));
                //Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pops:ALL$-imaps:ALL$-smtps:ALL");

                //Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pop:ALL$-pops:ALL$-imaps:ALL$-imap:ALL$-http:ALL$-https:ALL"); // line modified by pr on 15thapr19
                Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pops:ALL$-pop:ALL$-imaps:ALL$-imap:ALL$-smtps:ALL$-smtp:ALL$-https:ALL$-http:ALL"); // line modified by pr on 27thjun19

                Attribute attr40 = null;
                //if ((!rs.get("dob").equals("")) || (rs.get("dob") != null)) 
                if ((rs.get("dob") != null) && (!rs.get("dob").trim().equals(""))) // trim() added by pr on 21stjun18
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside dob not null and not empty dob value is " + rs.get("dob"));
                    String dob = fetchLDAPModifyDate(rs.get("dob").trim()); // line added by pr on 19thfeb18
                    //System.out.println(" inside createEmailId function dob formatted date is "+dob);
                    attr40 = new BasicAttribute("nicDateOfBirth", dob); // line modified by pr on 19thfeb18
                }
                String dor = fetchLDAPModifyDate(rs.get("dor").trim()); // line added by pr on 19thfeb18 // trim() added by pr on 21stjun18
                //System.out.println(" inside createEmailId function dor formatted date is "+dor);
                Attribute attr41 = new BasicAttribute("nicDateOfRetirement", dor); // line modified by pr on 19thfeb18
                Attribute attr42 = new BasicAttribute("nicAccountExpDate", dor); // line modified by pr on 19thfeb18
                //Attribute attr43 = new BasicAttribute("davUniqueId", "0342b55f-9fac-4bd9-9624-nic" + timestamp.getTime() + "single");
                Attribute attr43 = new BasicAttribute("davUniqueId", "0342b55f-9fac-4bd9-9624-nic" + timestamp.getTime() + "eforms-" + reg_no); // line modified by pr on 5thoct18
                //Attribute attr44 = new BasicAttribute("icsExtendedUserPrefs", "ceDefaultAlarmEmail=" + final_id);
                Attribute attr44 = new BasicAttribute("icsExtendedUserPrefs", "ceDefaultAlarmEmail=" + ldap_mail); // line modified by pr on 19thfeb18               
                Attribute attr45 = new BasicAttribute("nicAccountCreationDate", "100");
                Attribute attr46 = new BasicAttribute("nswmExtendedUserPrefs", "meSortOrder=R");

                // start, code added by pr on 15thapr19
                Attribute attr47 = new BasicAttribute("maildeliveryoption", "forward");
                Attribute attr48 = new BasicAttribute("mailforwardingaddress", uid + "@gov.in.local");
                Attribute attr49 = new BasicAttribute("zimotp", "1");
                Attribute attr50 = null;
                if (getDescription() != null && !getDescription().isEmpty()) {
                    attr50 = new BasicAttribute("NicFundingCategory", getDescription());
                }
                Attribute attr51 = null;
                if (mailboxType.equals("gem")) {
                    attr51 = new BasicAttribute("NicAccountType", "2");
                    System.out.println("CREATE Email ID GEM :: Adding NicAccountType = 2");
                } else if (mailboxType.equals("mail")) {
                    if (idType.equalsIgnoreCase("id_desig")) {
                        attr51 = new BasicAttribute("NicAccountType", "2");
                        System.out.println("CREATE Email ID :: Adding NicAccountType = 2");
                    } else if (idType.equalsIgnoreCase("id_name")) {
                        attr51 = new BasicAttribute("NicAccountType", "1");
                        System.out.println("CREATE Email ID :: Adding NicAccountType = 1");
                    }
                } else if (mailboxType.equals("app")) {
                    attr51 = new BasicAttribute("NicAccountType", "3");
                    System.out.println("CREATE Email ID :: Adding NicAccountType = 3");
                } else if (mailboxType.equals("eoffice")) {
                    attr51 = new BasicAttribute("NicAccountType", "4");
                    System.out.println("CREATE Email ID :: Adding NicAccountType = 4");
                } else {
                    // Do nothing.. Once NKN changes in GUI will be done... it will be populated
                }

                // end, code added by pr on 15thapr19
                Attribute oc = new BasicAttribute("ObjectClass");
                oc.add("userpresenceprofile");
                oc.add("sunucpreferences");
                oc.add("iplanet-am-user-service");
                oc.add("iplanet-am-managed-person");
                oc.add("icscalendaruser");
                oc.add("top");
                oc.add("organizationalPerson");
                oc.add("inetadmin");
                oc.add("sunimuser");
                oc.add("nicemailuser");
                oc.add("person");
                oc.add("sunamauthaccountlockout");
                oc.add("inetuser");
                oc.add("inetlocalmailrecipient");
                oc.add("sunpresenceuser");
                oc.add("iplanetpreferences");
                oc.add("ipuser");
                oc.add("inetOrgPerson");
                oc.add("inetsubscriber");
                oc.add("inetmailuser");
                oc.add("daventity");
                BasicAttributes attrs = new BasicAttributes();
                attrs.put(attr1);
                attrs.put(attr2);
                attrs.put(attr3);
                attrs.put(attr4);
                attrs.put(attr5);
                attrs.put(attr6);
                attrs.put(attr7);
                attrs.put(attr8);
                attrs.put(attr9);
                attrs.put(attr10);
                attrs.put(attr11);
                attrs.put(attr12);
                attrs.put(attr13);
                attrs.put(attr14);
                attrs.put(attr15);
                attrs.put(attr16);
                attrs.put(attr17);
                if (!ldap_mailequivalentaddress.equals("")) // if added by pr on 25thapr18
                {
                    attrs.put(attr18);
                }
                attrs.put(attr19);
                //attrs.put(attr20); // line commented by pr on 26thjun19
                attrs.put(attr21);
                attrs.put(attr22);
                attrs.put(attr23);
                attrs.put(attr24);
                attrs.put(attr25);
                attrs.put(attr26);
                attrs.put(attr27);
                attrs.put(attr28);
                attrs.put(attr29);
                attrs.put(attr30);
                attrs.put(attr31);
                attrs.put(attr32);
                attrs.put(attr33);
                attrs.put(attr34);
                attrs.put(attr35);
                attrs.put(attr36);
                attrs.put(attr37);
                attrs.put(attr38);
                attrs.put(attr39);
                //if ((!rs.get("dob").equals("")) || (rs.get("dob") != null)) 
                if ((rs.get("dob") != null) && (!rs.get("dob").trim().equals(""))) // trim() added by pr on 21stjun18
                {
                    attrs.put(attr40);
                }
                attrs.put(attr41);
                attrs.put(attr42);
                attrs.put(attr43);
                attrs.put(attr44);
                attrs.put(attr45);
                attrs.put(attr46);

                // start, code added by pr on 15thapr19
                attrs.put(attr47);

                attrs.put(attr48);

                attrs.put(attr49);
                if (attr50 != null) {
                    attrs.put(attr50);
                    System.out.println("CREATE Email ID :: Added NicFundingCategory = " + getDescription() + " in array");
                }
                if (attr51 != null) {
                    attrs.put(attr51);
                    System.out.println("CREATE Email ID :: Added NicAccountType in array");
                }
                // end, code added by pr on 15thapr19
                attrs.put(oc);
                ctx1.createSubcontext(NewDN, attrs);// to be uncommented in production, done for testing on 20thnov18
                spcount++; //for service package increment after account creation
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "AddUser: added entry " + NewDN + ".");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Account Created:::::::::::::::");
                //for package modification
                if (spcount > 0) {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside spcount greater than 0");
                    try {
                        int newpack = Integer.parseInt(sp3) + spcount;
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "modify code................for service package");
                        //ctx3 = new InitialDirContext(ht); // line commented by pr on 8thaug19
                        ModificationItem[] mods = new ModificationItem[1];
                        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                new BasicAttribute("sunAvailableServices", sp1 + ":" + sp2 + ":" + newpack));

                        //ctx3.modifyAttributes(dn_default, mods);// to be uncommented in production, done for testing on 20thnov18
                        ctx1.modifyAttributes(dn_default, mods);// line modified by pr on 8thaug19

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + mods[0]);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "service package modification done");
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "error in service package modification =>" + e);
                    }
                }
                // send sms to the mobile  in case of bulk form, below code added by pr on 12thdec17
                if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) // line added by pr on 27thdec17 
                {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside createEmailId function in ForwardAction bulk form if  ");
                    Inform infObj = new Inform();
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " mobile is " + mobilePrefix + rs.get("mobile") + " uid is " + uid + " final_id is " + final_id + " password is " + pass);
                    infObj.sendBulkCompIntimationSMS(mobilePrefix + rs.get("mobile"), uid, final_id, pass);
                }
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " before catch ");
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside catch for id creation exception createEmailId ");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + e.getMessage());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 128" + e.getMessage());
                valid = false;
            } finally // finally added by pr on 16thoct18
            {
                // start, code added by pr on 8thaug19
//                if (closeLDAPCon) {
//                    if (ctx1 != null) {
//                        try {
//                            ctx1.close();
//                        } catch (Exception e) {
//                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 129" + e.getMessage());
//                        }
//                    }
//                }

                /*if (ctx1 != null) {
                    try {
                        ctx1.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 129" + e.getMessage());
                    }
                }
                if (ctx3 != null) {
                    try {
                        ctx3.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 130" + e.getMessage());
                    }
                }*/   // code commented by pr on 8thaug19 as the connection is to be closed at a point of calling this function afterwards
            }
        } else {
            valid = false;
        }
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " valid value is " + valid);
        // ADD ID IN LDAP, END
        return valid;
    }

    // below function added by pr on 27thfeb18, DirContext ctx1 added by pr on 8thaug19
    public Boolean createAppId(DirContext ctx1, String formName, String po, String bo, String domain, String uid, String mail, String reg_no, String pass, String bulk_id, String bulk_mail) {

        boolean closeLDAPCon = false; // line added by pr on 8thaug19 to identify the method is being called for single or bulk

        // start, code added by pr on 11thapr18
        po = po.trim().toLowerCase();
        bo = bo.trim().toLowerCase();
        domain = domain.trim().toLowerCase();
        uid = uid.trim().toLowerCase();
        mail = mail.trim().toLowerCase();
        // end, code added by pr on 11thapr18
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde createemail id function formname is " + formName);// one parameters added after uid  by pr on 19thfeb18
        HashMap<String, String> rs = null;
        HashMap<String, String> rsForBulk = null;

        String final_id = uid + "@" + domain; // for forms other than bulk , on top on 13thdec17
        String mobilePrefix = "", description = ""; // 
        // start, code added by pr on 19thfeb18
        String ldap_uid = "", ldap_mail = "", ldap_mailequivalentaddress = "";
        ldap_uid = uid;
        ldap_mail = mail;
        /*if (!mail.contains("@nic.in")) {
         ldap_mailequivalentaddress = uid + "@nic.in";
         }*/ // commented by pr on 25thapr18
        // end, code added by pr on 19thfeb18    
        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " final id is " + final_id);
        if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
            // get details from the single user table
            rs = fwdObj.fetchSingleDetail(reg_no);
            if (rs.get("description") != null && !rs.get("description").isEmpty()) {
                description = rs.get("description");
            }
        } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD)) {
            // get details from the nkn user table
            rs = fetchNKNDetail(reg_no);
            if (rs.get("description") != null && !rs.get("description").isEmpty()) {
                description = rs.get("description");
            }
        } else if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD))// line modified by pr on 27thdec17 to include bulk nkn
        {
            // get details from the bulk user table
            rs = fwdObj.fetchBulkDetail(bulk_id);
            final_id = bulk_mail;// line added by pr on 13thdec17            
            ldap_mail = final_id; // line added by pr on 19thfeb18            
            //mobilePrefix = "+91";// line added by pr on 13thdec17, in case of bulk no 91 is there // line commented by pr on 10thsep18 as we are saving the mobile with + and country code in the bulk_users table
            rsForBulk = fwdObj.fetchBulkDetailsFromBaseTable(reg_no);

            if (rsForBulk.get("description") != null && !rsForBulk.get("description").isEmpty()) {
                description = rsForBulk.get("description");
            }

        } else if (formName.equals(Constants.GEM_FORM_KEYWORD))// else if added by pr on 18thdec17
        {
            // get details from the nkn user table
            rs = fwdObj.fetchFormDetail(reg_no, formName);
            //mobilePrefix = "+91";// line added by pr on 13thdec17, in case of bulk no 91 is there
            if (rs.get("description") != null && !rs.get("description").isEmpty()) {
                description = rs.get("description");
            }
        }
        if (!ldap_mail.contains("@nic.in")) // if added  by pr on 25thapr18
        {
            ldap_mailequivalentaddress = uid + "@nic.in";
        }
        Boolean valid = true;
        if (rs != null && (po != null && !po.equals("") && po.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))
                && (bo != null && !bo.equals("") && bo.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))
                && (domain != null && !domain.equals("") && domain.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))) // po bo and domain checks added by pr on 5thfeb18 , underscore allowed by pr on 19thfeb18
        {
            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside rs not null inside createEmailId func ");
            String dn_default = "";

            // FETCH BO DETAILS, START
            String sp2 = "", sp3 = "", sp1 = "", mailhost = "", messagestore = "";
            /*DirContext ctx1 = null;
            DirContext ctx3 = null; // line added  by pr on 16thoct18 */  // code commented by pr on 8thaug19
            try {

                if (ctx1 == null) // if around added by pr on 8thaug19
                {
//                    Hashtable ht = new Hashtable(4);
//                    ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//                    ht.put(Context.PROVIDER_URL, providerurlSlave);
//                    ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//                    ht.put(Context.SECURITY_PRINCIPAL, createdn);
//                    ht.put(Context.SECURITY_CREDENTIALS, createpass);

                    //ctx1 = new InitialDirContext(ht);
                    ctx1 = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                    closeLDAPCon = true; // in case of single request close, else not
                }

                String[] IDs = {"sunAvailableServices", "preferredMailHost", "preferredMailMessageStore", "sunAvailableDomainNames"};
                SearchControls ctls = new SearchControls();
                ctls.setReturningAttributes(IDs);
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                dn_default = "o=" + bo.trim() + ",o=" + po.trim() + ",o=nic.in,dc=nic,dc=in";
                String filter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo.trim() + "))";
                NamingEnumeration ans = ctx1.search(dn_default, filter, ctls);
                while (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    String s = t.substring(t.indexOf("sunAvailableServices:") + 22, t.indexOf(",")); //for service package
                    String s2 = t.substring(t.indexOf("preferredMailHost:") + 19, t.length()); //for mailhost
                    mailhost = s2.substring(0, s2.indexOf(",")); //for mailhost
                    String s3 = t.substring(t.indexOf("preferredMailMessageStore:") + 27, t.length()); //for messagestore
                    messagestore = s3.substring(0, s3.lastIndexOf("}")); //for messagestore
                    String s4 = t.substring(t.indexOf("sunAvailableDomainNames:") + 25, t.length());
                    domain = s4.substring(0, s4.lastIndexOf(", preferredmailmessagestore"));
                    sp1 = s.substring(0, s.indexOf(":"));
                    String sp2tmp = s.substring(s.indexOf(":") + 1, s.length());
                    if (sp2tmp.contains(":")) {
                        sp2 = (sp2tmp.substring(0, sp2tmp.indexOf(":"))).trim();
                        sp3 = (sp2tmp.substring(sp2tmp.indexOf(":") + 1, sp2tmp.length())).trim();
                    } else {
                        sp2 = sp2tmp.trim();
                        sp3 = "0";
                    }
                }
                List<String> myList = new ArrayList<String>(Arrays.asList(domain.split(",")));
                ArrayList alnew = new ArrayList();
                Iterator itr = myList.iterator();
                while (itr.hasNext()) {
                    String mail1 = ((String) itr.next()).trim(); //mail made mail1 by pr on 19thfeb18
                    if (mail1.equals("nic.in")) { //mail made mail1 by pr on 19thfeb18
                    } else {
                        alnew.add(mail1); //mail made mail1 by pr on 19thfeb18
                    }
                }
                int i = Integer.parseInt(sp2);
                int j = Integer.parseInt(sp3);
                int k = i - j;
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Error in sunAvailableServices Package =>");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 131" + e.getMessage());
            }
            try {
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside try before attributes ");
                int spcount = 0;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String NewDN = "uid=" + uid + ",ou=People," + dn_default;
                Attribute attr1 = new BasicAttribute("psIncludeInGAB", "true");
                Attribute attr2 = new BasicAttribute("icsFirstDay", "2");
                //Attribute attr3 = new BasicAttribute("mailMessageStore", messagestore);
                Attribute attr3 = new BasicAttribute("mailMessageStore", "without-container");
                Attribute attr4 = new BasicAttribute("userPassword", pass);
                Attribute attr5 = new BasicAttribute("iplanet-am-modifiable-by", "cn=Top-level Admin Role,dc=nic,dc=in");
                Attribute attr6 = new BasicAttribute("icsTimezone", "Asia/Calcutta");
                Attribute attr7 = new BasicAttribute("givenName", rs.get("fname"));
                Attribute attr8 = new BasicAttribute("mailUserStatus", "active");
                Attribute attr9 = new BasicAttribute("icsStatus", "Active");
                Attribute attr10 = new BasicAttribute("icsCalendar", uid + "@nic.in");
                Attribute attr11 = new BasicAttribute("inetCOS", "NICUserMailCalendar");
                Attribute attr12 = new BasicAttribute("preferredLocale", "en");
                //Attribute attr13 = new BasicAttribute("mailHost", mailhost);
                Attribute attr13 = new BasicAttribute("mailHost", "ms22.nic.in");
                Attribute attr15 = new BasicAttribute("uid", uid);
                Attribute attr16 = new BasicAttribute("preferredLanguage", "en");
                //Attribute attr17 = new BasicAttribute("mail", final_id);
                Attribute attr17 = new BasicAttribute("mail", ldap_mail); // line modified by pr on 19thfeb18                
                //Attribute attr18 = new BasicAttribute("mailequivalentaddress", uid + "@nic.in");
                Attribute attr18 = null; // line added by pr on 25thapr18
                if (!ldap_mailequivalentaddress.equals("")) // if added by pr on 25thapr18
                {
                    attr18 = new BasicAttribute("mailequivalentaddress", ldap_mailequivalentaddress); // line modified by pr on 19thfeb18
                }
                Attribute attr19 = new BasicAttribute("sn", rs.get("lname"));
                //Attribute attr20 = new BasicAttribute("mailDeliveryOption", "mailbox"); // line commented by pr on 6thmay19
                Attribute attr21 = new BasicAttribute("cn", rs.get("fname") + " " + rs.get("lname"));
                Attribute attr22 = new BasicAttribute("description", description); //Updated by AT on 27th Oct 2022
                Attribute attr23 = new BasicAttribute("mailDeferProcessing", "No");
                Attribute attr24 = new BasicAttribute("icsDWPHost", "cs1.nic.in");
                Attribute attr25 = new BasicAttribute("inetUserStatus", "Active");
                Attribute attr26 = new BasicAttribute("nicNewUser", "true");// as discussed with TM this has been kept to be true only, on 3rdmar2020
                Attribute attr27 = new BasicAttribute("sunUCTimeZone", "Asia/Calcutta");
                // start, code added  by pr on 10thsep18, to handle the previous 10 digit india code values 
                if (!rs.get("mobile").startsWith("+") && rs.get("mobile").length() == 10) {
                    mobilePrefix = "+91";
                }
                // end, code added  by pr on 10thsep18               
                Attribute attr28 = new BasicAttribute("mobile", mobilePrefix + rs.get("mobile"));// modified on 12thdec17
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "mobile value is " + mobilePrefix + rs.get("mobile"));
                Attribute attr29 = new BasicAttribute("sunUCDateDelimiter", "");
                Attribute attr30 = new BasicAttribute("sunUCDateFormat", "");
                Attribute attr32 = new BasicAttribute("sunUCColorScheme", "");
                Attribute attr34 = new BasicAttribute("sunUCTimeFormat", "");
                Attribute attr35 = new BasicAttribute("mailQuota", "-1");
                Attribute attr36 = new BasicAttribute("st", rs.get("state"));
                Attribute attr37 = new BasicAttribute("title", rs.get("title"));
                Attribute attr38 = new BasicAttribute("departmentNumber", rs.get("department"));
                //Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pops:ALL$-imaps:ALL$-smtps:ALL");

                //Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pop:ALL$-pops:ALL$-imaps:ALL$-imap:ALL$-http:ALL$-https:ALL"); // line modified by pr on 15thapr19
                //Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pop:ALL$-pops:ALL$-imaps:ALL$-imap:ALL$-http:ALL$-https:ALL$-smtp:ALL$-smtps:ALL"); // line modified by pr on 6thmay19
                Attribute attr39 = new BasicAttribute("mailallowedserviceaccess", "-smime:ALL$-pops:ALL$-pop:ALL$-imaps:ALL$-imap:ALL$-smtps:ALL$-smtp:ALL$-https:ALL$-http:ALL"); // line modified by pr on 27thjun19

                Attribute attr40 = null;
                //if ((!rs.get("dob").equals("")) || (rs.get("dob") != null)) {
                if ((rs.get("dob") != null) && (!rs.get("dob").trim().equals(""))) // trim() added by pr on 21stjun18
                { // line modified by pr on 18thapr18
                    String dob = fetchLDAPModifyDate(rs.get("dob").trim()); // line added by pr on 19thfeb18 // trim() added by pr on 21stjun18
                    //System.out.println(" inside createEmailId function dob formatted date is "+dob);
                    attr40 = new BasicAttribute("nicDateOfBirth", dob); // line modified by pr on 19thfeb18
                }
                String dor = fetchLDAPModifyDate(rs.get("dor")); // line added by pr on 19thfeb18
                //System.out.println(" inside createEmailId function dor formatted date is "+dor);
                Attribute attr41 = new BasicAttribute("nicDateOfRetirement", dor); // line modified by pr on 19thfeb18
                Attribute attr42 = new BasicAttribute("nicAccountExpDate", dor); // line modified by pr on 19thfeb18
                //Attribute attr43 = new BasicAttribute("davUniqueId", "0342b55f-9fac-4bd9-9624-nic" + timestamp.getTime() + "single");
                Attribute attr43 = new BasicAttribute("davUniqueId", "0342b55f-9fac-4bd9-9624-nic" + timestamp.getTime() + "eforms-" + reg_no); // line modified by pr on 15thfeb19
                //Attribute attr44 = new BasicAttribute("icsExtendedUserPrefs", "ceDefaultAlarmEmail=" + final_id);
                // start, to be removed attributes
                /*Attribute attr44 = new BasicAttribute("icsExtendedUserPrefs", "ceDefaultAlarmEmail=" + ldap_mail); // line modified by pr on 19thfeb18               
                
                 Attribute attr45 = new BasicAttribute("nicAccountCreationDate", "100");
                 Attribute attr33 = new BasicAttribute("sunUCDefaultApplication", "mail");
                
                 Attribute attr46 = new BasicAttribute("nswmExtendedUserPrefs", "meSortOrder=R");
                
                 Attribute attr14 = new BasicAttribute("mailAutoReplyMode", "echo");
                
                 Attribute attr31 = new BasicAttribute("sunUCDefaultEmailHandler", "");*/
                Attribute attr44 = null;
                if (getDescription() != null && !getDescription().isEmpty()) {
                    attr44 = new BasicAttribute("NicFundingCategory", getDescription());
                    System.out.println("CREATE APP ID :: Adding NicFundingCategory = " + getDescription());
                }
                Attribute attr45 = new BasicAttribute("NicAccountType", "3");
                System.out.println("CREATE APP ID :: Adding NicAccountType = 3");
                // end, to be removed attributes
                Attribute oc = new BasicAttribute("ObjectClass");
                oc.add("userpresenceprofile");
                oc.add("sunucpreferences");
                oc.add("iplanet-am-user-service");
                oc.add("iplanet-am-managed-person");
                oc.add("icscalendaruser");
                oc.add("top");
                oc.add("organizationalPerson");
                oc.add("inetadmin");
                oc.add("sunimuser");
                oc.add("nicemailuser");
                oc.add("person");
                oc.add("sunamauthaccountlockout");
                oc.add("inetuser");
                oc.add("inetlocalmailrecipient");
                oc.add("sunpresenceuser");
                oc.add("iplanetpreferences");
                oc.add("ipuser");
                oc.add("inetOrgPerson");
                oc.add("inetsubscriber");
                oc.add("inetmailuser");
                oc.add("daventity");
                BasicAttributes attrs = new BasicAttributes();
                attrs.put(attr1);
                attrs.put(attr2);
                attrs.put(attr3);
                attrs.put(attr4);
                attrs.put(attr5);
                attrs.put(attr6);
                attrs.put(attr7);
                attrs.put(attr8);
                attrs.put(attr9);
                attrs.put(attr10);
                attrs.put(attr11);
                attrs.put(attr12);
                attrs.put(attr13);
                //attrs.put(attr14);
                attrs.put(attr15);
                attrs.put(attr16);
                attrs.put(attr17);
                if (!ldap_mailequivalentaddress.equals("")) // if added by pr on 25thapr18
                {
                    attrs.put(attr18);
                }
                attrs.put(attr19);
                //attrs.put(attr20); // line commented by pr on 6thmay19
                attrs.put(attr21);
                attrs.put(attr22);
                attrs.put(attr23);
                attrs.put(attr24);
                attrs.put(attr25);
                attrs.put(attr26);
                attrs.put(attr27);
                attrs.put(attr28);
                attrs.put(attr29);
                attrs.put(attr30);
                //attrs.put(attr31);
                attrs.put(attr32);
                //attrs.put(attr33);
                attrs.put(attr34);
                attrs.put(attr35);
                attrs.put(attr36);
                attrs.put(attr37);
                attrs.put(attr38);
                attrs.put(attr39);
                //if ((!rs.get("dob").equals("")) || (rs.get("dob") != null)) {
                if ((rs.get("dob") != null) && (!rs.get("dob").trim().equals(""))) // trim() added by pr on 21stjun18
                { // line modified by pr on 18thapr18
                    attrs.put(attr40);
                }
                attrs.put(attr41);
                attrs.put(attr42);
                attrs.put(attr43);
                if (attr44 != null) {
                    attrs.put(attr44);
                    System.out.println("CREATE APP ID :: Adding NicFundingCategory = " + getDescription() + " Added in an Array");
                }
                attrs.put(attr45);
                System.out.println("CREATE APP ID :: Adding NicAccountType = 3 Added in an Array");
                //attrs.put(attr44);
                //attrs.put(attr45);
                //attrs.put(attr46);
                attrs.put(oc);
                ctx1.createSubcontext(NewDN, attrs);// to be uncommented in production done on 1stjun18
                spcount++; //for service package increment after account creation
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " AddUser: added entry " + NewDN + ".");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Account Created:::::::::::::::");
                //for package modification
                if (spcount > 0) {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside spcount greater than 0");
                    try {
                        int newpack = Integer.parseInt(sp3) + spcount;
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "modify code................for service package");

                        //ctx3 = new InitialDirContext(ht); // line commented by pr on 8thaug19
                        ModificationItem[] mods = new ModificationItem[1];
                        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                new BasicAttribute("sunAvailableServices", sp1 + ":" + sp2 + ":" + newpack));

                        //ctx3.modifyAttributes(dn_default, mods);// to be uncommented in production  done on 1stjun18
                        ctx1.modifyAttributes(dn_default, mods);// line modified by pr on 8thaug19

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + mods[0]);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "service package modification done");
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "error in service package modification =>" + e);
                    }
                }
                // send sms to the mobile  in case of bulk form, below code added by pr on 12thdec17
                if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) // line added by pr on 27thdec17 
                {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside createEmailId function in ForwardAction bulk form if  ");
                    Inform infObj = new Inform();
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " mobile is " + mobilePrefix + rs.get("mobile") + " uid is " + uid + " final_id is " + final_id + " password is " + pass);
                    infObj.sendBulkCompIntimationSMS(mobilePrefix + rs.get("mobile"), uid, final_id, pass);
                }
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " before catch ");
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "inside catch for id creation exception createAppId ");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 132" + e.getMessage());
                valid = false;
            } finally // finally added by pr on 16thoct18
            {
                // below code added by pr on 8thaug19
//                if (closeLDAPCon) {
//                    if (ctx1 != null) {
//                        try {
//                            ctx1.close();
//                        } catch (Exception e) {
//                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 129" + e.getMessage());
//                        }
//                    }
//                }

                /*if (ctx1 != null) {
                    try {
                        ctx1.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 133" + e.getMessage());
                    }
                }
                if (ctx3 != null) {
                    try {
                        ctx3.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 134" + e.getMessage());
                    }
                }*/ // code commented by pr on 8thaug19
            }
        } else {
            valid = false;
        }
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " valid value is " + valid);
        // ADD ID IN LDAP, END
        return valid;
    }

    // below function added by pr on 19thfeb18
    public String fetchLDAPModifyDate(String DBDate) // dbdate is in format dd-mm-YYYY , return value of the function should be YYYYMMDD000000Z
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " == " + " inside getLDAPModifyDate function value of DBDate is " + DBDate);
        String LDAPDate = ""; // line added by pr on 21stjun18
        if (DBDate != null && !DBDate.equals("")) // if added by pr on 21stjun18 , updated on 12thapr19
        {
            String dd = "", mm = "", yy = "";
            String[] arr = DBDate.split("-");
            dd = arr[0];
            mm = arr[1];
            yy = arr[2];
            LDAPDate = yy + mm + dd + "000000Z";
        }
        return LDAPDate;
    }

    // below function added by pr on 21stjun19
    public boolean updateFirewallLocation(String reg_no, String locs) {
        PreparedStatement ps = null;
        Boolean flag = false;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = " UPDATE centralutm_registration SET location = ? WHERE registration_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, locs);
            ps.setString(2, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateFirewallLocation query is " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateWifiValue exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 139" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 140" + e.getMessage());
                }
            }
        }

        return flag;

    }

    public boolean updateWifiportLocation(String reg_no, String locs) {
        PreparedStatement ps = null;
        Boolean flag = false;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = " UPDATE wifiport_registration SET location = ? WHERE registration_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, locs);
            ps.setString(2, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateFirewallLocation query is " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateWifiValue exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 139" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 140" + e.getMessage());
                }
            }
        }

        return flag;

    }

    // below function added by pr on 7thmay19
    public boolean updateDeleteWifi(String reg_no) {
        PreparedStatement ps = null;
        Boolean flag = false;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = " UPDATE wifi_mac_os SET status = ? WHERE registration_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, "deleted");
            ps.setString(2, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateDeleteWifi query is " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateWifiValue exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 139" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 140" + e.getMessage());
                }
            }
        }

        return flag;

    }

    // below function added by pr on 12thapr19
    public HashMap<String, Object> createZimEmailId(String formName, String po, String bo, String domain, String uid, String mail, String reg_no, String pass,
            String bulk_id, String bulk_mail, String admin_token, URL url) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function  func parameter values are formName " + formName
                + " po " + po + " bo " + bo + " domain " + domain + " uid " + uid + " mail " + mail + " reg_no " + reg_no + " pass " + pass + " bulk_id " + bulk_id + " bulk_mail " + bulk_mail + " admin_token " + admin_token + " url " + url);
        HashMap<String, Object> resultHM = new HashMap<String, Object>();
        try {
            // start, code added by pr on 11thapr18
            po = po.trim().toLowerCase();
            bo = bo.trim().toLowerCase();
            domain = domain.trim().toLowerCase();
            uid = uid.trim().toLowerCase();
            mail = mail.trim().toLowerCase();
            // end, code added by pr on 11thapr18
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde createemail id function formname is " + formName);// one parameters added after uid  by pr on 19thfeb18
            HashMap<String, String> rs = null;
            String final_id = uid + "@" + domain; // for forms other than bulk , on top on 13thdec17
            String mobilePrefix = ""; // 
            // start, code added by pr on 19thfeb18
            String ldap_uid = "", ldap_mail = "", ldap_mailequivalentaddress = "";
            ldap_uid = uid;
            ldap_mail = mail;
            //ArrayList<String> zim = new ArrayList<String>();
            String zimData = "";
            String message = "";

            if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
                // get details from the single user table
                rs = fwdObj.fetchSingleDetail(reg_no);
            } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD)) {
                // get details from the nkn user table
                rs = fetchNKNDetail(reg_no);
            } else if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD))// line modified by pr on 27thdec17 to include bulk nkn
            {
                // get details from the bulk user table
                rs = fwdObj.fetchBulkDetail(bulk_id);
                final_id = bulk_mail;// line added by pr on 13thdec17            
                ldap_mail = final_id; // line added by pr on 19thfeb18            
                //mobilePrefix = "+91";// line added by pr on 13thdec17, in case of bulk no 91 is there   // commented by pr on 10thsep18 as now we are saving + with country code and mobile
            } else if (formName.equals(Constants.GEM_FORM_KEYWORD))// else if added by pr on 18thdec17
            {
                // get details from the nkn user table
                rs = fwdObj.fetchFormDetail(reg_no, formName);
            }
            if (!ldap_mail.contains("@nic.in")) // if added  by pr on 25thapr18
            {
                ldap_mailequivalentaddress = uid + "@nic.in";
            }
            Boolean valid = true;

            String zim_mail_host = ""; // line added by pr on 29thmay19    

            if (rs != null && (po != null && !po.equals("") && po.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))
                    && (bo != null && !bo.equals("") && bo.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))
                    && (domain != null && !domain.equals("") && domain.matches("^[a-zA-Z#0-9_\\s,'.\\-\\/\\(\\)]{2,150}$"))) {
                HashMap<String, String> zimbra_values = new HashMap<String, String>();

                zim_mail_host = fetchZimMailHost();

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function zim_mail_host value got from DB is " + zim_mail_host);

                zimbra_values.put("zimbraMailHost", zim_mail_host); // line modified by pr on 7thmay19

                zimbra_values.put("legacyUid", uid);
                zimbra_values.put("title", rs.get("title"));
                zimbra_values.put("givenName", rs.get("fname"));
                zimbra_values.put("sn", rs.get("lname"));
                zimbra_values.put("cn", rs.get("fname") + " " + rs.get("lname"));
                zimbra_values.put("displayName", rs.get("fname") + " " + rs.get("lname"));
                zimbra_values.put("mobile", rs.get("mobile"));
                zimbra_values.put("o", rs.get("department"));
                zimbra_values.put("legacyEmployeeDeptName", rs.get("department"));
                zimbra_values.put("st", rs.get("state"));
                if (rs.get("emp_code") != null && !rs.get("emp_code").equals("")) {
                    zimbra_values.put("legacyEmployeeNum", rs.get("emp_code"));
                }
                if ((rs.get("dob") != null) && (!rs.get("dob").trim().equals(""))) {
                    String dob = fetchLDAPModifyDate(rs.get("dob").trim()); // line added by pr on 19thfeb18
                    zimbra_values.put("legacyDateOfBirth", dob);
                }
                String dor = fetchLDAPModifyDate(rs.get("dor").trim()); // line added by pr on 19thfeb18            
                zimbra_values.put("legacyDateOfRetirement", dor);
                zimbra_values.put("legacyAccountExpDate", dor);
                zimbra_values.put("legacyPo", po);
                zimbra_values.put("legacyBo", bo);
                zimbra_values.put("zimbraAccountStatus", "active");
                zimbra_values.put("zimbraPrefLocale", "en");
                //zimbra_values.put("legacyNewUser", "TRUE");
                zimbra_values.put("legacyNewUser", "FALSE"); // line modified by pr on 3rdmar2020
                zimbra_values.put("zimbraImapEnabled", "FALSE");
                zimbra_values.put("zimbraPop3Enabled", "FALSE");
                zimbra_values.put("zimbraFeatureSMIMEEnabled", "FALSE");
                if (rs.get("description") != null && !rs.get("description").equals("")) {

                    // start, code added by pr on 6thmay19
                    Date dt = new Date();

                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

                    String final_date = format.format(dt);

                    String desc = rs.get("description") + " New ID Zimbra : " + final_date;

                    zimbra_values.put("description", desc); // line modified by pr on 6thmay19

                }
                zimbra_values.put("zimbraCOSId", "7c2a4602-b3f6-464a-9b3e-bdd4146c4918");
                System.out.println(" mail before spilt is " + mail);
                String[] mle = mail.split("@");
                System.out.println(" after spilt array size is " + mle.length);
                String mli2 = mle[1]; // suffix part of mail , after @
                System.out.println(" after spilt array  suffix part is " + mli2);

                Map<String, String> respons_domain = zimbra_soap_v2.Zimbra_SOAP_v2.GetAllDomain(admin_token, url, mli2); // commented for testing on 11thapr19, to be uncommented in production

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function zimbra_values are " + zimbra_values);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function GetAllDomain RESPONSE IS " + respons_domain);

                // below try catch added by pr on 12thmay2020 to resolve file with null data issue in mail sent to zimbra team
                try {
                    zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").trim().isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").trim().isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").trim().isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.trim().isEmpty() ? "NA" : ldap_mailequivalentaddress);

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function zimdata is " + zimData);

                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function Exception in zimdata " + e.getMessage());
                }

                if (!respons_domain.containsKey("ERROR")) {

                    // below line commented by pr on 18thjul19 for testing, to be uncommented in production
                    Map<String, String> response = zimbra_soap_v2.Zimbra_SOAP_v2.CreateAccount(mail, pass, admin_token, zimbra_values, url);// commented for testing on 1staug19

                    response.entrySet().stream().map((entry) -> {
                        return entry;
                    }).forEachOrdered((entry) -> {
                        //System.out.println("KEY  " + entry.getKey() + "  Value " + entry.getValue());

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function GetAllDomain RESPONSE NOT ERROR IS " + "KEY  " + entry.getKey() + "  Value " + entry.getValue());

                    });

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function CreateAccount RESPONSE IS " + response);

                    if (!response.containsKey("ERROR")) // if response doesnt contain error then add an account alias
                    {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function NOOOOOOOOO creation error start " + zimData);
                    } else // if it does then add that value in the arraylist
                    {
                        message = "Zimbra Creation Error";
                        valid = false;
                        //zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.isEmpty() ? "NA" : ldap_mailequivalentaddress);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function Zimbra Creation Error " + zimData);
                    }
                } else // if error found return that error and send mail to zimbra regarding that
                {
                    message = "Zimbra Domain not found Error";
                    valid = false;
                    //zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.isEmpty() ? "NA" : ldap_mailequivalentaddress);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function Zimbra Domain not found Error " + zimData);
                }
            } else {
                message = "Invalid PO, BO, Domain (Zimbra)";
                valid = false;
            }
            resultHM.put("valid", valid);
            resultHM.put("data", zimData);
            resultHM.put("msg", message);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function inside zimbra creation function hashmap is " + resultHM + " zimData is " + zimData);
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailId function exception " + e.getMessage());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 135" + e.getMessage());
        } // commented for testing to be uncommented in production on 2ndjul19
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " valid value is " + valid);
        // ADD ID IN LDAP, END

        // start, added by pr on 1stnov19 for testing
//        resultHM.put("valid", true);
//        resultHM.put("data", null);
//        resultHM.put("msg", null);
        // end, added by pr on 1stnov19 for testing, to be commented in production
        return resultHM;
    }

    // below function added by pr on 18thjul19
    public HashMap<String, Object> createZimEmailAlias(String uid, String mail, String admin_token, URL url, String zimData) { // zimData has been passed from createzimemailid function already

        HashMap<String, Object> resultHM = new HashMap<String, Object>();

        mail = mail.trim().toLowerCase();

        String message = "";

        boolean valid = true;

        String ldap_mailequivalentaddress = "";

        if (!mail.contains("@nic.in")) // if added  by pr on 25thapr18
        {
            ldap_mailequivalentaddress = uid + "@nic.in";
        }

        System.out.println(" mail before spilt is " + mail);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " ==  inside createZimEmailAlias function uid " + uid + " mail " + mail + " admin_token " + admin_token + " url " + url + " ldap_mailequivalentaddress " + ldap_mailequivalentaddress);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " ==  inside createZimEmailAlias function NOOOOOOOOO creation error start " + zimData);

        // below line commented by pr on 18thjul19 for testing, comments to be removed in production
        String mail_id = Zimbra_SOAP_v2.GetAccountID(mail, admin_token, url); // prefix String deleted by pr on 17thjul19

        //String mail_id = "SUCCESS"; // line added for testing to be removed in production
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAlias function MAIL ID CREATED IS " + mail_id);

        if (!mail_id.contains("ERROR")) // if around and else added by pr on 7thjun19
        {
            String response_zimbra_alias1 = "", response_zimbra_alias2 = ""; // line added by pr on 6thmay19, modified by pr on 7thjun19

            //String response_zimbra_alias = ""; // line  added by pr on 6thmay19, line commented by pr on 7thjun19
            if (ldap_mailequivalentaddress != null && !ldap_mailequivalentaddress.isEmpty()) {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAlias function INSIDE ldap_mailequivalentaddress " + ldap_mailequivalentaddress);

                // below line commented by pr on 18thjul19 for testing, comments to be removed in production
                response_zimbra_alias1 = Zimbra_SOAP_v2.AddAccountAlias(mail_id, admin_token, ldap_mailequivalentaddress, url); // 1 added by pr on 7thjun19

                //response_zimbra_alias1 = "SUCCESS"; // line added for testing to be removed in production
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAlias function INSIDE ldap_mailequivalentaddress  ALIAS RESPONSE IS " + response_zimbra_alias1);

            }

            // below line commented by pr on 18thjul19 for testing, comments to be removed in production
            response_zimbra_alias2 = Zimbra_SOAP_v2.AddAccountAlias(mail_id, admin_token, uid + "@gov.in.local", url); // 2 added by pr on 7thjun19

            //response_zimbra_alias2 = "SUCCESS"; // line added for testing to be removed in production
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAlias function AFTER ldap_mailequivalentaddress  ALIAS RESPONSE for gov.in.local IS " + response_zimbra_alias2);

            if (response_zimbra_alias1.contains("ERROR") || response_zimbra_alias2.contains("ERROR")) // if block added by pr on 7thjun19
            {
                message = "Zimbra Alias Error";
                valid = false;
                //zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.isEmpty() ? "NA" : ldap_mailequivalentaddress);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAlias function Alias Error " + zimData);
            }

        } else {
            message = "Zimbra GetAccountID Error";
            valid = false;
            //zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.isEmpty() ? "NA" : ldap_mailequivalentaddress);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAlias function Zimbra GetAccountID Error " + zimData);

        }

        resultHM.put("message", message);

        resultHM.put("valid", valid);

        resultHM.put("data", zimData);

        return resultHM;

    }

    // below function added by pr on 29thmay19
    public String fetchZimMailHost() {
        String zim_mail_host = "";
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {

            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT zim_mail_host FROM zim_setting";

            ps = conSlave.prepareStatement(qry);

            rs = ps.executeQuery();

            while (rs.next()) {

                zim_mail_host = rs.getString("zim_mail_host");

            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchZimMailHost exception catch " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchZimMailHost ps exception " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchZimMailHost rs exception " + e.getMessage());
                }
            }
            if (conSlave != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchZimMailHost conSlave exception " + e.getMessage());
                }
            }
        }

        return zim_mail_host;

    }

    // below function added by pr on 18thmar19
    public HashMap<String, String> fetchZimOtp(String email) {
        //email = "dhirendra.nhq@nic.in";
        String dn = "";
        HashMap<String, String> hm = new HashMap<>();
        if (!email.equals("")) {
            ArrayList<String> data = new ArrayList<String>();
            //ServletContext sctx = getServletContext();
            /*String host = sctx.getInitParameter("host");
             String port = sctx.getInitParameter("port");
             String dn = sctx.getInitParameter("binddn");
             String dn_pass = sctx.getInitParameter("bindpass");*/
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurlSlave);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, createdn);
//            ht.put(Context.SECURITY_CREDENTIALS, createpass);
            DirContext ctx = null; // line added  by pr on 16thoct18
            try {
                //ctx = new InitialDirContext(ht);

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //String[] IDs = {"uid", "mail", "mailequivalentaddress", "cn", "mobile"};
                String[] IDs = {"mailAllowedServiceAccess", "zimotp"};
                SearchControls ctls = new SearchControls();
                ctls.setReturningAttributes(IDs);
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
                NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
                if (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    System.out.println(" ldapsetring is " + t);
                    if (!t.equals("")) {
                        String[] arr = t.split(":");
                        if (arr != null && arr.length > 0) {
                            dn = arr[0];
                        }
                    }
                    if (!t.equals("")) {
                        t = t.substring(t.indexOf("{") + 1, t.indexOf("}"));
                        System.out.println(" string between curly braces is " + t);
                        String mobile_str = t.substring(t.indexOf("mailAllowedServiceAccess:") + 26);
                        String mobile = mobile_str.substring(0);
                        if (mobile_str.indexOf(",") >= 0) {
                            mobile = mobile_str.substring(0, mobile_str.indexOf(","));
                        }
                        System.out.println(" mail allowed service value is " + mobile);
                        hm.put("dn", dn);
                        hm.put("service", mobile);
                        if (t.toLowerCase().contains("zimotp")) {
                            hm.put("zimotp", "true");
                        } else {
                            hm.put("zimotp", "false");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage() + " inside fetchZimOtp ");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 136" + e.getMessage());
            } finally // finally added by pr on 16thoct18
            {
//                if (ctx != null) {
//                    try {
//                        ctx.close();
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 137" + e.getMessage());
//                    }
//                }
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside fetchZimOtp before return hashmap is " + hm);
        return hm;
    }

    public String fetchAttributeValue(String email, String protocol) {
        String attValue = "", previousValue = "";
        // get the previous value for the mailallowedserviceaccess attribute value
        previousValue = fetchLDAPMailAttribute(email);
        System.out.println(" inside fetchAttributeValue function previous value is " + previousValue);
        String[] previousArr = previousValue.split("\\$");
        LinkedList<String> newValues = new LinkedList<String>();
        if (!protocol.equals("") && (protocol.equals("imap") || protocol.equals("pop"))) {
            for (String s : previousArr) // add all values in the arraylist
            {
                System.out.println(" value to be put inside the arraylist " + s);
                newValues.add(s);
            }
            if (protocol.equals("pop")) // if pop needs to be added  , only -imap will remain
            {
                // remove -pop
                if (newValues.contains("-pops:ALL")) {
                    newValues.remove("-pops:ALL");
                }
                System.out.println(" newValues arraylist contains  " + newValues);
                if (!newValues.contains("-imaps:ALL")) {
                    System.out.println(" inside newvalues contain -imaps:ALL ");
                    newValues.add("-imaps:ALL");
                }
            } else // if imap needs to be added 
            {
                // remove -imap
                if (newValues.contains("-imaps:ALL")) // only -pop will remain
                {
                    newValues.remove("-imaps:ALL");
                }
                if (!newValues.contains("-pops:ALL")) {
                    newValues.add("-pops:ALL");
                }
            }
            if (newValues.contains("-smtps:ALL")) {
                newValues.remove("-smtps:ALL");
            }
            int i = 1;
            for (String val : newValues) {
                if (i == 1) {
                    attValue += val;
                } else {
                    attValue += "$" + val;
                }
                i++;
            }
        }
        //return previousValue;
        System.out.println(" inside fetchAttributeValue func value of attValue " + attValue);
        return attValue;
    }

    // below function added by pr on 28thaug18
    public String fetchLDAPMailAttribute(String email) {
        String attValue = "";
        //ServletContext sctx = getServletContext();
        /*String host = sctx.getInitParameter("host");
         String port = sctx.getInitParameter("port");
         String dn = sctx.getInitParameter("binddn");
         String dn_pass = sctx.getInitParameter("bindpass");*/
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null; // line added by pr on 16thoct18
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress", "cn", "mobile"};
            String[] IDs = {"mailAllowedServiceAccess"};
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();
                //String uids = t.substring(t.indexOf("uid:") + 5, t.length() - 1);
                t = t.substring(t.indexOf("{") + 1, t.indexOf("}"));
                System.out.println(" mailallowedservice substring value is " + t);
                String str = t.substring(t.indexOf("mailAllowedServiceAccess:") + 25).trim();
                System.out.println(" allowed service string value for email " + email + " is " + str);
                attValue = str;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 138" + e.getMessage());
//                }
//            }
        }
        return attValue;
    }

    // below function added by pr on 10thaug18
    public Boolean updateWifiValue(String reg_no, String wifi_value) {
        // update in the table wifi_registration
        PreparedStatement ps = null;
        Boolean flag = false;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = " UPDATE wifi_registration SET wifi_value = ? WHERE registration_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, wifi_value);
            ps.setString(2, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateWifiValue query is " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateWifiValue exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 139" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 140" + e.getMessage());
                }
            }
        }
        return flag;
    }

    // below function added by pr on 8thjun18, response1 added by pr on 3rddec19
    public void updateDNSAPIResp(String reg_no, String response, String response1) {
        // update in the table dns_api_response
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            //String qry = " INSERT INTO dns_api_response SET api_reg_no = ?, api_response = ? ";, below line modified by pr on 3rddec19
            String qry = " INSERT INTO dns_api_response SET api_reg_no = ?, api_response = ? , api_domain_response = ?";
            ps = con.prepareStatement(qry);
            ps.setString(1, reg_no);
            ps.setString(2, response);
            ps.setString(3, response1); // line added by pr on 3rddec19
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateDNSAPIResp query is " + ps);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateDNSAPIResp exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 141" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 142" + e.getMessage());
                }
            }
        }

        try {

            // send mails if the reponse has failed
            if (response.equalsIgnoreCase("FAIL") || !response1.equalsIgnoreCase("SUCCESS")) {
                String[] admins = {"eforms@nic.in"};
                String sub = "DNS API Response Error for Registration No. - " + reg_no;
                String text = "Dear Admin, <br>DNS API Response for Registration number - " + reg_no + " has not been successful with reason " + response1 + ". <br>Regards,<br>Messaging Team NIC";
                String from = ctx.getInitParameter("send-mail-from");
                for (String to : admins) {
                    Mail.sendMail(to, text, sub, from);
                }
            }

            // below code added by pr on 3rddec19
            if (!response1.toLowerCase().contains("success")) { // response modified with response1 by pr on 10thjan2020
                String[] admins = {"eforms@nic.in"};
                String sub = "DNS Domain API Response Error for Registration No. - " + reg_no;
                String text = "Dear Admin, <br>DNS Domain API Response for Registration number - " + reg_no + " has not been successful. <br>Regards,<br>Messaging Team NIC";
                String from = ctx.getInitParameter("send-mail-from");
                for (String to : admins) {
                    Mail.sendMail(to, text, sub, from);
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Mail sending exception for dns api response" + e.getMessage());
        }
    }

    // function added on 5thdec17 to modify ldap attribute
    public void modifyLDAPAttribute(String dn, String attrName, String attrValue) {
        dn = dn + ",dc=nic,dc=in";
        // Set up the environment for creating the initial context
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null; // line added by pr on 16thoct18
        try {
            // Create the initial context
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String dn = "cn=Ted Geisel, ou=People";
            // Save original attributes
            Attributes orig = ctx.getAttributes(dn,
                    new String[]{attrName});
            // Specify the changes to make
            ModificationItem[] mods = new ModificationItem[1];
            // Replace the "mail" attribute with a new value
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute(attrName, attrValue));
            // Perform the requested modifications on the named object
            ctx.modifyAttributes(dn, mods);// to be uncommented in production
            // Check attributes
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "**** new attributes *****" + dn + " " + attrName + " " + attrValue);
            // Revert changes
            // ctx.modifyAttributes(dn, DirContext.REPLACE_ATTRIBUTE, orig); // to revert back to old attribute value to undo the change effect
            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside modifyLDAPAttribute function before printattrs func call  ");
//            ArrayList attrArr = printAttrs(ctx.getAttributes(dn));
//
//            if (attrArr.contains("nicWIFI")) {
//                // modify the attribute
//
//                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde attribute exists and need to modify it ");
//            } else {
//                // add the attribute    
//
//                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde attribute DOESNT exists and need to add it ");
//            }
            // Check that the attributes got restored
            // Close the context when we're done
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " "
                    + "inside modifyLDAPAttribute function dn is " + dn + " attrName " + attrName + " attrValue is " + attrValue);// line added by pr on 14thnov19
            //ctx.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 144" + e.getMessage());
        }
    }

    public void modifyActivateAttribute(String dn, HashMap<String, String> zim_values, String formName) {
        dn = dn + ",dc=nic,dc=in";
        // Set up the environment for creating the initial context
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//        ht.put(Context.PROVIDER_URL, providerurl);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null; // line added by pr on 16thoct18
        try {
            if (formName.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {
                //ctx = new InitialDirContext(ht);
                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                ModificationItem[] mods = new ModificationItem[1];
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mobile", zim_values.get("mobile")));

                ctx.modifyAttributes(dn, mods);// to be uncommented in production
                //ctx.close();
            }
            if (formName.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {
                //ctx = new InitialDirContext(ht);
                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                ModificationItem[] mods = new ModificationItem[7];
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mailuserstatus", zim_values.get("mailuserstatus")));
                mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("inetuserstatus", zim_values.get("inetuserstatus")));
                mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("inetsubscriberaccountid", zim_values.get("inetsubscriberaccountid")));
                mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicAccountExpDate", zim_values.get("nicAccountExpDate")));
                mods[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicDateOfRetirement", zim_values.get("nicDateOfRetirement")));
                mods[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicnewuser", zim_values.get("true")));
                mods[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userpassword", zim_values.get("userpassword")));
                ctx.modifyAttributes(dn, mods);// to be uncommented in production
                //ctx.close();
            }
            if (formName.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {
                System.out.println("inside email deactivate form ");
                //ctx = new InitialDirContext(ht);

                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                ModificationItem[] mods = new ModificationItem[4];
                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mailuserstatus", zim_values.get("mailuserstatus")));
                mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("inetuserstatus", zim_values.get("inetuserstatus")));
                mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("inetsubscriberaccountid", zim_values.get("inetsubscriberaccountid")));
                mods[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userpassword", zim_values.get("userpassword")));
                ctx.modifyAttributes(dn, mods);// to be uncommented in production
                //ctx.close();
            }
            if (formName.equals(Constants.DOR_EXT_FORM_KEYWORD)) {
                //ctx = new InitialDirContext(ht);
                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                ModificationItem[] mods = new ModificationItem[3];

                mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicAccountExpDate", zim_values.get("nicAccountExpDate")));
                // mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicDateOfRetirement", zim_values.get("nicDateOfRetirement")));
                // mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicDateOfBirth", zim_values.get("nicDateOfBirth")));

                ctx.modifyAttributes(dn, mods);// to be uncommented in production
                //ctx.close();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 144" + e.getMessage());
        }
    }
//  public void modifyActivateAttribute_retired(String dn, HashMap<String, String> zim_values) {
//        System.out.println("statrt process of m,odifying status of email ikn retired :");
//        DirContext ctx = null;
//        try {
//            ctx = SingleApplicationContext.getLDAPInstance();
//            ModificationItem[] mods = new ModificationItem[7];
//            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mailuserstatus", zim_values.get("mailuserstatus")));
//            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("inetuserstatus", zim_values.get("inetuserstatus")));
//            ctx.modifyAttributes(dn, mods);
//
//        } catch (Exception e) {
//            System.out.println("Exception for retired email activation: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//    }

    public boolean modifyAttributeForRetired(String dn, HashMap<String, String> zim_values, String formName) {
        DirContext ctx = null; // line added by pr on 16thoct18
        dn = dn + ",dc=nic,dc=in";
        try {
            ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020
            ModificationItem[] mods = new ModificationItem[3];
            for (Map.Entry<String, String> entry : zim_values.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("KEY ::: " + key + " Value ::: " + value);
            }
            System.out.println("DN :: " + dn);
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicAccountExpDate", zim_values.get("nicAccountExpDate")));
            System.out.println("MOD1 == " + mods[0]);
            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mailuserstatus", "active"));
            System.out.println("MOD2 == " + mods[1]);
            mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("inetuserstatus", "active"));
            System.out.println("MOD3 == " + mods[2]);
            for (ModificationItem mod : mods) {
                System.out.println("MOD value attribute and modification code ::: " + mod.getAttribute() + " : " + mod.getModificationOp());
            }
            //mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicDateOfRetirement", zim_values.get("nicDateOfRetirement")));
            //mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("nicDateOfBirth", zim_values.get("nicDateOfBirth")));
            System.out.println("inside Retired dor update in Ldap, dn and mods: " + ctx + " : " + dn + " :" + mods);
            try {
                ctx.modifyAttributes(dn, mods);// to be uncommented in production
                return true;
            } catch (AttributeModificationException ex) {
                System.out.println("INside exception RETIRED... attribute issue");
                ex.printStackTrace();
            } catch (NamingException ex) {
                System.out.println("INside exception1 RETIRED... namingException issue");
                ex.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 144" + e.getMessage());
        }
        return false;
    }

    // below function added by pr on 12thapr19, string formname added by pr on 12thapr19
    public void modifyZimLDAPAttribute(String admin_token, URL url, String email, HashMap<String, String> zim_values, String formName) {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "  inside modifyZimLDAPAttribute function zim_values passed are  " + zim_values + " for email " + email);
        try {
            if (zim_values != null && zim_values.size() > 0) {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " admin_token " + admin_token + " url " + url);

                String id = GetAccountID(email, admin_token, url); // done for testing on 11thapr19
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside modifyZimLDAPAttribute GET-Account-ID method return value ::::::::::::::::::" + id);

                Map<String, String> respon = ModifyAccount(id, admin_token, zim_values, url); // commented for testing on 11thapr19 to be uncommented

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " respon got from ModifyAccount func in modifyZimLDAPAttribute " + respon);

                //String formName = ""; // line commented by pr on 12thapr19
                String value = "";
                // start, code added for testing by pr on 11thapr19 to be uncommented
//                Map<String, String> respon = new HashMap<String, String>(); 
//
//                respon.put("ERROR", "error");
//                
                // end, code added for testing by pr on 11thapr19
                if (respon.containsKey("ERROR")) {
                    // get the form name from the zim_values hashmap and based on form name pick the value of attribute to modify and pass into the below function
                    /*if (zim_values.get("form_name") != null) {
                        formName = zim_values.get("form_name");
                    }*/   // if commented by 12thapr19
                    String errorVal = respon.get("ERROR"); // line added by pr on 12thapr19

                    if (errorVal != null && !errorVal.contains("no such account")) { // if added by pr on 12thapr19
                        if (formName.trim().equals(Constants.WIFI_FORM_KEYWORD)) {
                            if (zim_values.get("legacyWifi") != null) {
                                value = zim_values.get("legacyWifi");
                            }
                        } else if (formName.trim().equals(Constants.IMAP_FORM_KEYWORD)) {
                            if (zim_values.get("zimbraImapEnabled") != null && zim_values.get("zimbraImapEnabled").equals("TRUE")) {
                                value = "IMAP";
                            }
                            if (zim_values.get("zimbraPop3Enabled") != null && zim_values.get("zimbraPop3Enabled").equals("TRUE")) {
                                value = "POP";
                            }
                        } else if (formName.trim().equals(Constants.MOB_FORM_KEYWORD)) {
                            if (zim_values.get("mobile") != null) {
                                value = zim_values.get("mobile");
                            }
                        } else if (formName.trim().equals("updatemobile")) {
                            if (zim_values.get("mobile") != null) {
                                value = zim_values.get("mobile");
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Error in SOAP Response value from soap Request in modifyZimLDAPAttribute:::::::::::::::: " + respon.get("ERROR").toString());
                        //sending mail for non updated user profile
                        Inform infObj = new Inform();
                        infObj.sendZimMail(email, formName, value);
                    } else {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "inside modifyZimLDAPAttribute:::::::::::::::: function. Its a case of no account exist in zimbra " + respon.get("ERROR").toString());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside modifyZimLDAPAttribute catch function " + e.getMessage());
        }
    }

    public ArrayList printAttrs(Attributes attrs) {
        ArrayList attrArr = new ArrayList();
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde printAttrs function ");
        if (attrs == null) {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "No attributes");
        } else {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside attrs not null ");
            /* Print each attribute */
            try {
                for (NamingEnumeration ae = attrs.getAll();
                        ae.hasMore();) {
                    Attribute attr = (Attribute) ae.next();
                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " printAttrs attribute: " + attr.getID());
                    attrArr.add(attr.getID()); // add attribute name in the arraylist
                    /* print each value */
                    for (NamingEnumeration e = attr.getAll();
                            e.hasMore(););
                }
            } catch (NamingException e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 145" + e.getMessage());
            }
        }
        return attrArr;
    }

    // below function added by pr on 13thmar18
    public void updateDescription(String description, String registration_no) {
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            // below code added by pr on 19thdec17
            con = DbConnection.getConnection();
            String qry = "UPDATE bulk_registration SET description = ? WHERE registration_no = ?";
            ps = con.prepareStatement(qry);
            ps.setString(1, description);
            ps.setString(2, registration_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateDescription func query is  " + ps);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 6" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 151" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 152" + e.getMessage());
                }
            }
        }
    }

    public Boolean checkBulkAndUpdateStatus(String reg_no, String formName) { // line modified by pr on 
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        Boolean isRemaining = false; // taken up by pr on 26thdec18

        String role = fetchSessionRole(); // line added by pr on 24thjul19

        boolean isRejected = false;// line added by pr on 30thjan2020

        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            //conSlave = DbConnection.getSlaveConnection(); // line commented by pr on 10thmay19
            String qry = "SELECT bulk_id FROM bulk_users WHERE registration_no = ? AND is_created = 'n' AND is_rejected = 'n'";
            //ps = conSlave.prepareStatement(qry);

//            ps = conSlave.prepareStatement(qry); // line modified by pr on 10thmay19
//Only for below line , con for master connection for only select from bulk_users , to see whether solution is working 02-mar-2022
            ps = con.prepareStatement(qry); // line modified by pr on 10thmay19

            ps.setString(1, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside checkBulkAndUpdateStatus func query is " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                //22-feb-2022
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                        + " bulk_users bulk_id " + rs.getString("bulk_id"));
                //22-feb-2022
                isRemaining = true;
//                break; // line added by pr on 26thdec18 for bulk // commented on 22-feb-2022 for sysout in catalina
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " isRemaining value is " + isRemaining);
            if (!isRemaining) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isRemaining not ");
                // check if status already updated for COMPLETED value else update just now
                if (!fetchBulkStatusCompleted(reg_no)) {

                    if (role.equals(Constants.ROLE_MAILADMIN)) // if around and below else added by pr on 24thjul19
                    {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchBulkStatusCompleted not ");
                        // update status from here
                        fwdObj.updateStatus(reg_no, "completed", formName, "", "", "", "");
                        Inform infObj = new Inform();
                        // start, code added by pr on 14thdec17
                        setRegNo(reg_no);
                        //setType("success");
                        setType("success_mail"); // line modified by pr on 20thnov18
                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "  inside checkBulkAndUpdateStatus func  before success  exportexcel ");
                        exportExcel();
                        setRegNo(reg_no);
                        //setType("error");
                        setType("error_mail"); // line modified by pr on 20thnov18
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "  inside checkBulkAndUpdateStatus func  before error  exportexcel ");
                        exportExcel(); // need to test it 
                        // end, code added by pr on 14thdec17
                        // send intimation to applicant , support, coordinator
                        infObj.sendBulkCompIntimation(reg_no, formName);// line modified by pr on 27thdec17 for including nkn bulk

                    } else if (role.equals(Constants.ROLE_CO)) // else added by pr on 24thjul19
                    {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isBulkStatusCompleted not ");
                        // update status from here
                        fwdObj.updateStatus(reg_no, "coordinator_rejected", formName, "Rejected All Bulk Users By Coordinator", "", "", "");
                        Inform infObj = new Inform();

                        setRegNo(reg_no);

                        setType("error_mail");

                        exportExcel();

                        infObj.sendBulkCompIntimation(reg_no, formName);
                    } else if (role.equals(Constants.ROLE_CA)) // else added by pr on 6thdec19
                    {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isBulkStatusCompleted not AT RO ROLE ");
                        // update status from here
                        fwdObj.updateStatus(reg_no, "ca_rejected", formName, "Rejected All Bulk Users By Reporting Officer", "", "", "");
                        Inform infObj = new Inform();

                        setRegNo(reg_no);

                        setType("error_mail");

                        exportExcel();

                        infObj.sendBulkCompIntimation(reg_no, formName);
                    }

                }

                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " after inside fetchBulkStatusCompleted not ");
            } else // else added by pr on 30thjan2020
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " isRemaining else");

                if (!role.equals(Constants.ROLE_MAILADMIN)) // for all other than admin admins send the error sheet = rejected emails sheet to all stakeholders and applicant
                {
                    setType("error_mail");

                    exportExcel();

                    Inform infObj = new Inform();

                    infObj.sendBulkRejectIntimation(reg_no);
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 7" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 153" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 154" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 155" + e.getMessage());
                }
            }
        }
        return isRemaining;
    }

    // below function added by pr on 12thdec17
    public Boolean fetchBulkStatusCompleted(String reg_no) // parameter added by pr on 27thdec17 to include bulk nkn
    {
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchBulkStatusCompleted function ");
        Boolean isCompleted = false;
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT stat_id FROM status WHERE stat_reg_no = ? AND stat_type = 'completed' ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchBulkStatusCompleted function query is " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                isCompleted = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 8" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 156" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 157" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 158" + e.getMessage());
                }
            }
        }
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside  fetchBulkStatusCompleted function isCompleted value is  " + isCompleted);
        return isCompleted;
    }

    public HashMap fetchBulkUserDetails(String bulk_id) {
        HashMap hm = new HashMap();
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT registration_no,fname,lname,department,mail,mobile,state,designation,dob,dor,uid,emp_code,allow_creation FROM bulk_users WHERE bulk_id = ?   ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, bulk_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("reg_no", rs.getString("registration_no"));
                hm.put("fname", rs.getString("fname"));
                hm.put("lname", rs.getString("lname"));
                hm.put("designation", rs.getString("designation"));
                hm.put("department", rs.getString("department"));
                hm.put("state", rs.getString("state"));
                hm.put("mobile", rs.getString("mobile"));
                hm.put("dor", rs.getString("dor"));
                hm.put("uid", rs.getString("uid"));
                hm.put("mail", rs.getString("mail"));
                hm.put("dob", rs.getString("dob"));
                hm.put("emp_code", rs.getString("emp_code"));
                // below if added by pr on 29thmay18
                if (rs.getString("allow_creation") != null && !rs.getString("allow_creation").equals("")) {
                    hm.put("allow_creation", rs.getString("allow_creation"));
                } else {
                    hm.put("allow_creation", "");
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchBulkUserDetails insinde checkAlreadyStatus " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 159" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 160" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 161" + e.getMessage());
                }
            }
        }
        return hm;
    }

    public Boolean updateSMSFinalID(String uid, String reg_no, String description) // parameters modified by pr on 13thmar18
    {
        int i = 0;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE sms_registration SET final_id = ?, support_action_taken = 'c', description = ?  WHERE registration_no = ?";
            ps = con.prepareStatement(qry);
            ps.setString(1, uid);
            ps.setString(2, description);
            ps.setString(3, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " updateSMSFinalID query is " + ps);
            i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function sms " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 162" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 163" + e.getMessage());
                }
            }
        }
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    // below function modified by pr on 30thjan18
    public Boolean updateFinalID(String uid, String reg_no, String formName, String description)// for single user and nkn user and gem user , new parameter description added by pr on 13thmar18
    {
        int i = 0;
        PreparedStatement ps = null;
        try {
            con = DbConnection.getConnection();
            String tblName = "";
            if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
                tblName = "single_registration";
            } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD)) {
                tblName = "nkn_registration";
            } else if (formName.equals(Constants.GEM_FORM_KEYWORD)) {
                tblName = "gem_registration";
            }
            /*String qry = "UPDATE " + tblName + " SET final_id = ?, support_action_taken = 'c'  "
             + ""
             + "WHERE registration_no = ?";*/
            // modified by pr on 13thmar18
            String qry = "UPDATE " + tblName + " SET final_id = ?, support_action_taken = 'c', description = ?  "
                    + ""
                    + "WHERE registration_no = ?";
            ps = con.prepareStatement(qry);
            ps.setString(1, uid);
            ps.setString(2, description);  // line added by pr on 13thmar18
            ps.setString(3, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside update final id func query is " + ps);
            i = ps.executeUpdate();
            // start, code added by pr on 12thjan19
            // update in status table
            /*qry = "UPDATE status SET stat_final_id = ?  "
             + ""
             + " WHERE stat_reg_no = ? AND stat_type = 'COMPLETED' ";
             ps = con.prepareStatement(qry);
             ps.setString(1, uid);
             ps.setString(2, reg_no);
             System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + 
             new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside update final id  updating in status table func query is " + ps);
             i = ps.executeUpdate();*/ // commented by pr on 16thjan19
            // end, code added by pr on 12thjan19
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " updateFinalID func " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 164" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 165" + e.getMessage());
                }
            }
        }
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }

    // below function added by pr on 29thmay18
    //public Boolean allowMobileDuplicateCreation(String bulk_id, String registration_no, String uidExist, String allow_creation) 
    //public Boolean allowMobileDuplicateCreation(String registration_no, String uidExist) // signature modified by pr on 24thoct18 for mobile check to use it in case of single nkn gem etc
//    public Boolean allowMobileDuplicateCreation(String registration_no, String uidExist, String allow_creation) {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside allowMobileDuplicateCreation func  registration_no " + registration_no + " uidExist " + uidExist);
//        Boolean allow = true;
//        if (!uidExist.equalsIgnoreCase("n")) // line modified by pr on 24thoct18 to check -admin in the uids 
//        {
//            System.out.println(" inside forwardaction allowMobileDuplicateCreation function uidExist " + uidExist);
//            //uidExist = uidExist+"|"+"pre-admin|"+"test.test"; // just for testing comment in production
//            //uidExist = uidExist+"|"+"test.test"; // just for testing comment in production
//            String[] uidsArr = uidExist.split("\\|");
//            System.out.println(" uidsArr length is " + uidsArr.length);
//            //String[] finalUidArr = new String[uidsArr.length]; // it will contain only the uids without pipe char(|) and <br> tags
//            String[] finalUidArr = new String[uidsArr.length]; // it will contain only the uids without pipe char(|) and <br> tags
//            int i = 0;
//            boolean adminExist = false;
//            for (String uid : uidsArr) {
//                String s = uid.replaceAll("<br>", "");
//                System.out.println(" uid is " + uid + " after replace value is " + s);
//                s = s.toLowerCase();
//                finalUidArr[i] = s; // so as to check -admin in the further process
//                if (s.contains("-admin")) {
//                    adminExist = true;
//                }
//                i++;
//            }
//            //if (finalUidArr.length > 2) // if greater than 2 then simply disallow
//            if (finalUidArr.length > 3) // modified by pr on 20thnov18
//            {
//                System.out.println(" inside 1 len ");
//                allow = false;
//            } //else if (finalUidArr.length == 2 && adminExist) // if count is 2 and any one contains -admin then allow
//            else if (finalUidArr.length == 3 && adminExist) // modified by pr on 20thnov18
//            {
//                System.out.println(" inside 2 len ");
//                allow = true;
//            } //else if (finalUidArr.length == 2 && !adminExist) // if count is 2 and doesnt contain even a single -admin then disallow
//            else if (finalUidArr.length == 3 && !adminExist) // modified by pr on 20thnov18
//            {
//                System.out.println(" inside 3 len ");
//                allow = false;
//            }
//            // start if block added by pr on 29thoct18, apply the check for name based account when already mobile is linked and still there is a room to create other account
//            if (registration_no.contains("BULKUSER-FORM")) // if added by pr on 30thoct18 to check for only bulk user form
//            {
//                if (allow_creation != null) // check the below condition in case of bulk form
//                {
//                    if (allow) {
//                        HashMap hm = fwdObj.fetchFormDetail(registration_no, Constants.BULK_FORM_KEYWORD);
//                        String id_type = hm.get("id_type").toString();
//                        if (id_type.equalsIgnoreCase("id_name")) // if name based check and update the allow_creation field in bulk_users table else ignore
//                        {
//                            // check for the column allow_creation if it is empty then update with n 
//                            if (allow_creation.equals("") || allow_creation.equalsIgnoreCase("n")) // for the first time it will be empty and for the second time when reject remarks are not reset it will be n
//                            {
//                                allow = false;
//                            }
//                            // allow creation value will be made y when user resets the reject remarks in case of a uid exists to empty then allow_creation field will become y                
//                        }
//                    }
//                }
//            }
//            // end if block  added by pr on 29thoct18
//        }
//        return allow;
//    }
    // below function modified by pr on 12thmay2020 
    public Boolean allowMobileDuplicateCreation(String registration_no, String mobile, String allow_creation) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                + " inside allowMobileDuplicateCreation func  mobile " + mobile);
        Boolean allow = true;

        String uidExist = fwdObj.fetchMobileLDAPMail(mobile);
        if (!uidExist.equalsIgnoreCase("")) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " inside allowMobileDuplicateCreation uidExist " + uidExist);

            //uidExist = uidExist+"|"+"pre-admin|"+"test.test"; // just for testing comment in production
            //uidExist = uidExist+"|"+"test.test"; // just for testing comment in production
            String[] uidsArr = uidExist.split("\\|");
            //System.out.println(" uidsArr length is " + uidsArr.length);
            //String[] finalUidArr = new String[uidsArr.length]; // it will contain only the uids without pipe char(|) and <br> tags
            String[] finalUidArr = new String[uidsArr.length]; // it will contain only the uids without pipe char(|) and <br> tags
            int i = 0;
            boolean adminExist = false;
            for (String uid : uidsArr) {
                String s = uid.replaceAll("<br>", "");
                //System.out.println(" uid is " + uid + " after replace value is " + s);
                s = s.toLowerCase();
                finalUidArr[i] = s; // so as to check -admin in the further process
                if (s.contains("-admin")) {
                    adminExist = true;
                }
                i++;
            }
            //if (finalUidArr.length > 2) // if greater than 2 then simply disallow
            if (finalUidArr.length > 3) // modified by pr on 20thnov18
            {
                //System.out.println(" inside 1 len ");
                allow = false;
            } //else if (finalUidArr.length == 2 && adminExist) // if count is 2 and any one contains -admin then allow
            else if (finalUidArr.length == 3 && adminExist) // modified by pr on 20thnov18
            {
                //System.out.println(" inside 2 len ");
                allow = true;
            } //else if (finalUidArr.length == 2 && !adminExist) // if count is 2 and doesnt contain even a single -admin then disallow
            else if (finalUidArr.length == 3 && !adminExist) // modified by pr on 20thnov18
            {
                //System.out.println(" inside 3 len ");
                allow = false;
            }
            // start if block added by pr on 29thoct18, apply the check for name based account when already mobile is linked and still there is a room to create other account
            if (registration_no.contains("BULKUSER-FORM")) // if added by pr on 30thoct18 to check for only bulk user form
            {
                if (allow_creation != null) // check the below condition in case of bulk form
                {
                    if (allow) {
                        HashMap hm = fwdObj.fetchFormDetail(registration_no, Constants.BULK_FORM_KEYWORD);
                        String id_type = hm.get("id_type").toString();
                        if (id_type.equalsIgnoreCase("id_name")) // if name based check and update the allow_creation field in bulk_users table else ignore
                        {
                            // check for the column allow_creation if it is empty then update with n 
                            if (allow_creation.equals("") || allow_creation.equalsIgnoreCase("n")) // for the first time it will be empty and for the second time when reject remarks are not reset it will be n
                            {
                                allow = false;
                            }
                            // allow creation value will be made y when user resets the reject remarks in case of a uid exists to empty then allow_creation field will become y                
                        }
                    }
                }
            }
            // end if block  added by pr on 29thoct18
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                + " inside allowMobileDuplicateCreation func allow value evaluated " + allow + " for mobile " + mobile);

        return allow;
    }

    // below function added by pr on 9thmar18
    public String updateBulk() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " field_name value is " + field_name + " field_value is " + field_value + " bulk_id is " + bulk_id + " uid_exist value is " + uid_exist);
        Boolean isValid = false;
        String errorMsg = "";
        PreparedStatement ps = null;
        //Connection con = null;
        String allow_creation = ""; // line added by pr on 29thmay18
        if (field_name.equals("uid")) {
            String valid = "";
            validation.BulkValidation validObj = new validation.BulkValidation();
            HashMap<String, String> validateResult = validObj.onlyUID(field_value);
            if (validateResult != null) {
                if (validateResult.get("valid") != null) {
                    valid = validateResult.get("valid").toString();
                }
                if (validateResult.get("errorMsg") != null) {
                    errorMsg = validateResult.get("errorMsg").toString();
                }
                if (valid.equals("true")) {
                    isValid = true;
                }
            }
            if (!fwdObj.emailAvail(field_value)) {
                isValid = false;
                errorMsg += field_name + " - " + field_value + " not Available ";
            }
        } else if (field_name.equals("mail")) {
            String valid = "";
            validation.BulkValidation validObj = new validation.BulkValidation();
            HashMap<String, String> validateResult = validObj.onlyMail(field_value);
            if (validateResult != null) {
                if (validateResult.get("valid") != null) {
                    valid = validateResult.get("valid").toString();
                }
                if (validateResult.get("errorMsg") != null) {
                    errorMsg = validateResult.get("errorMsg").toString();
                }
                if (valid.equals("true")) {
                    isValid = true;
                }
            }
            if (!fwdObj.emailAvail(field_value)) {
                isValid = false;
                errorMsg += field_name + " - " + field_value + " not Available ";
            }
        } else if (field_name.equals("reject_remarks")) // else if added by pr on 29thmay18
        {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside filed name remarks and field value is " + field_value);
            if (field_value.matches(statRemarksRegEx)) // it is also resetting the reject remarks in case of uid exists for this mobile 
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside reject remarks field value matches stat remarks  ");
                isValid = true;
            } else if (uid_exist.equals("y") && field_value.equals("")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside reject remarks field value is empty and uid_esist value is y  ");
                allow_creation = "y";
                isValid = true;
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside reject remarks else");
                //errorMsg = "Invalid Remarks";
                errorMsg = "Invalid Remarks. " + Constants.STAT_REMARKS_TEXT; // line modified , constant appended by pr on 28thdec18
            }
        }
        if (isValid) {
            if (field_name.equals("uid") || field_name.equals("mail") || field_name.equals("reject_remarks")) // line modified by pr on 29thmay18
            {
                int rs = 0;
                try {
                    // below code added by pr on 19thdec17
                    con = DbConnection.getConnection();
                    //String qry = "UPDATE bulk_users SET  " + field_name + " = ? WHERE  bulk_id = ?  ";
                    //String qry = "UPDATE bulk_users SET  " + field_name + " = ? , reject_remarks = '' WHERE  bulk_id = ?    "; // reject remarks are made empty
                    String qry = "UPDATE bulk_users SET  " + field_name + " = ?  "; // query modified by pr on 29thmay18
                    if (!allow_creation.equals("")) // if added by pr on 29thmay18
                    {
                        qry += " , allow_creation = 'y' ";
                    }
                    qry += "  WHERE  bulk_id = ?  ";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, field_value);
                    ps.setString(2, bulk_id);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside update bulk users query is " + ps);
                    rs = ps.executeUpdate();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde update bulk_users table " + e.getMessage());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            //// above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 29 " + e.getMessage());

                            // above line commented below added by pr on 23rdapr19
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "updateBulk  EXCEPTION 1 " + e.getMessage());

                        }
                    }
                    if (con != null) {
                        try {
                            // con.close();
                        } catch (Exception e) {
                            //// above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 30 " + e.getMessage());

                            // above line commented below added by pr on 23rdapr19
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "updateBulk  EXCEPTION 2 " + e.getMessage());
                        }
                    }
                }
                if (rs > 0) {
                    isSuccess = true;
                    isError = false;
                    msg = "Field - " + field_name + " updated successfully";
                } else {
                    isSuccess = false;
                    isError = true;
                    msg = "Field - " + field_name + " could not be updated.";
                }
            } else {
                isSuccess = false;
                isError = true;
                msg = "Field - " + field_name + " could not be updated.";
            }
        } else {
            isSuccess = false;
            isError = true;
            msg = "Field - " + field_name + "  could not be updated . Error - " + errorMsg;
        }
        return SUCCESS;
    }

    // below function added by pr on 2ndapr18
    public String updateBulkUpload() {
        String email = "";
        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();
        if (sessionMap != null && sessionMap.get("uservalues") != null) {
            UserData userdata = (UserData) sessionMap.get("uservalues");
            email = userdata.getEmail();
        }

        if (!CSRFRandom.equals(SessionCSRFRandom)) {
            hmTrack.put("csrf_error", "CSRF Token is invalid.");
            return SUCCESS;
        }
        errorArr = new ArrayList<String>();
        File destFile = null; // line added by pr on 6thapr18
        //Connection con = null;
        try {
            Date date = new Date();
            DateFormat dt = new SimpleDateFormat("ddMMyyyyHHmmss");
            String pdate = dt.format(date);
            destFile = new File(ServletActionContext.getServletContext().getInitParameter("excelLocation"), pdate + "_" + email.split("@")[0].trim().toLowerCase() + ".csv");//For live  
            // File destFile = new File("F:\\", pdate + ".csv");
            FileUtils.copyFile(csv_file, destFile);
            // check for lines cannot be more than 3000
            Boolean wrongfileflag = false;
            try (CSVReader reader = new CSVReader(new FileReader(destFile))) {
                String[] line1;
                int length = 0, i = 1;
                while ((line1 = reader.readNext()) != null) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " length is " + length);
                    if (i > 1) {

                        String bulk_id = line1.length > 0 ? line1[0].trim() : "",
                                fname = line1.length > 1 ? line1[1].trim() : "",
                                lname = line1.length > 2 ? line1[2].trim() : "",
                                designation = line1.length > 3 ? line1[3].trim() : "",
                                department = line1.length > 4 ? line1[4].trim() : "",
                                state = line1.length > 5 ? line1[5].trim() : "",
                                mobile = line1.length > 6 ? line1[6].trim() : "",
                                dor = line1.length > 7 ? line1[7].trim() : "",
                                uid = line1.length > 8 ? line1[8].trim() : "",
                                mail = line1.length > 9 ? line1[9].trim() : "",
                                dob = line1.length > 10 ? line1[10].trim() : "",
                                emp_code = line1.length > 11 ? line1[11].trim() : "";
                        System.out.println(ServletActionContext.getRequest().getSession().getId()
                                + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                + " == " + "bulk_id: " + bulk_id + "fname: " + fname
                                + ", lname: " + lname + " , desig: " + designation + " ,dept:  " + department + " , "
                                + "state: " + state + ", mobile: " + mobile + ", dor: " + dor + ", uid: "
                                + uid + ", email: " + mail + ", dob: " + dob + ", emp_code: "
                                + emp_code);
                        // start, validation code
                        Boolean isValid = true;
                        String valid = "", errorMsg = "";
                        validation.BulkValidation validObj = new validation.BulkValidation();
                        HashMap<String, String> finalFieldErrorMap = new HashMap<String, String>();
                        HashMap<String, String> validateResult = null;
                        // fname
                        validateResult = validObj.Fname(fname);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("fname", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after fname " + isValid);
                        validateResult = validObj.Lname(lname);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("lname", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after lname " + isValid);
                        validateResult = validObj.Designation(designation);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("designation", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after designation " + isValid);
                        validateResult = validObj.Department(department);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("department", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after department " + isValid);
                        validateResult = validObj.State(state);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("state", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after state " + isValid);
                        validateResult = validObj.DOR(dor);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("dor", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after dor " + isValid);
                        validateResult = validObj.onlyUID(uid);
                        Boolean checkdbbo = false;
                        String newmail = "";
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("uid", errorMsg);
                            } else if (!fwdObj.emailAvail(uid)) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("uid", "UID already Exists.");
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after uid " + isValid);
                        validateResult = validObj.onlyMail(mail);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("mail", errorMsg);
                            } else if (!fwdObj.emailAvail(mail)) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("mail", "Mail already Exists.");
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after mail " + isValid);
                        validateResult = validObj.DOB(dob);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            if (valid.equals("false")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("dob", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after dob " + isValid);
                        validateResult = validObj.EMPNUMBER(emp_code);
                        if (validateResult != null) {
                            if (validateResult.get("valid") != null) {
                                valid = validateResult.get("valid").toString();
                            }
                            if (validateResult.get("errorMsg") != null) {
                                errorMsg = validateResult.get("errorMsg").toString();
                            }
                            //if( valid.equals("false") )
                            if (valid.equals("false") && !errorMsg.equals("")) {
                                isValid = false;
                                // for invalid record add entry in the hashmap corresponding to the field name
                                finalFieldErrorMap.put("emp_code", errorMsg);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " isValid after emp_code " + isValid);
                        PreparedStatement ps = null;
                        // end, validation code
                        if (isValid) // update records in the bulk_users table
                        {
                            // update query here
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isvalid true ");
                            String updated_by = fetchSessionEmail();
                            try {
                                con = DbConnection.getConnection();
                                /*String qry = "UPDATE bulk_users SET  fname = ?, lname = ?, designation = ?, department = ?, state = ?, mobile = ?, dor = ? ,"
                                + "   uid = ?, mail = ?, dob = ?, emp_code = ?, updatedon = current_timestamp ,  update_flag = 'true' , updated_by = ? "
                                + ""
                                + "  WHERE bulk_id = ?";*/
                                // above line modified by pr on 10thsep18 to remove mobile from updating at the Admin Panel
                                String qry = "UPDATE bulk_users SET  fname = ?, lname = ?, designation = ?, department = ?, state = ?, dor = ? ,"
                                        + "   uid = ?, mail = ?, dob = ?, emp_code = ?, updatedon = current_timestamp ,  update_flag = 'true' , updated_by = ? "
                                        + ""
                                        + "  WHERE bulk_id = ?";
                                ps = con.prepareStatement(qry);
                                ps.setString(1, fname);
                                ps.setString(2, lname);
                                ps.setString(3, designation);
                                ps.setString(4, department);
                                ps.setString(5, state);
                                //ps.setString(6, mobile); // commented by pr on 10thsep18
                                ps.setString(6, dor);
                                ps.setString(7, uid);
                                ps.setString(8, mail);
                                ps.setString(9, dob);
                                ps.setString(10, emp_code);
                                ps.setString(11, updated_by);
                                ps.setString(12, bulk_id);
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " update bulk upload query is " + ps);
                                //int rs = ps.executeUpdate();
                                ps.executeUpdate();
                            } catch (Exception e) {
                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 246" + e.getMessage());
                            } finally {
                                if (ps != null) {
                                    try {
                                        ps.close();
                                    } catch (Exception e) {
                                        // above line commented below added by pr on 23rdapr19
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 247" + e.getMessage());
                                    }
                                }
                            }
                        } else {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isvalid false ");
                            // need to show th emessage in the interface
                            String message = " bulk_id - " + bulk_id + " ,";
                            if (finalFieldErrorMap != null && finalFieldErrorMap.size() > 0) {
                                for (String k : finalFieldErrorMap.keySet()) {
                                    String val = finalFieldErrorMap.get(k);
                                    message += " " + k + " - " + val;
                                }
                                errorArr.add(message);
                            }
                        }
                    }
                    length = length++;
                    i++;
                }
                if (length > 3000) {
                    wrongfileflag = true;
                }

                if (wrongfileflag) {
                    isSuccess = false;
                    isError = true;
                    msg = "Lines in file cannot exceed 3000 lines.";
                } else if (errorArr != null && errorArr.size() > 0) // if there are any errors found in the rows then show them 
                {
                    isSuccess = false;
                    isError = true;
                    msg = "All Rows could not be Updated";
                } else {
                    isSuccess = true;
                    isError = false;
                    msg = "All Rows Updated Successfully";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                isSuccess = false;
                isError = true;
                msg = "File not available to download";
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateBulkUpload func ");
        } catch (Exception e) {
            // above line commented below added by pr on 23rdapr19          
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 248" + e.getMessage());
        } finally // finally added by pr on 6thapr18
        {
            if (destFile.exists()) {
                destFile.delete();
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 249" + e.getMessage());
                }
            }
        }
        return SUCCESS;
    }

    // below function added by pr on 7thdec18 for wifi to generate wifi pdf and mail to the applicant vpn support team and wifi support team, parameter modified by pr on 30thnov18
    public void generateWiFiPDF(String reg_no, String wifi_value) {
        System.out.println(" inside generateWiFiPDF function reg_no " + reg_no + " wifi_value  " + wifi_value);
        HashMap hm = fwdObj.fetchFormDetail(reg_no, Constants.WIFI_FORM_KEYWORD);
        boolean generateWiFiPDF = false;
        // start, code added by pr on 5thnov18
        // generate certificate when wifi_value is 2(vayu) and wifi_os1 or wifi_os2 or wifi_os3 as ios
        if (hm.get("wifi_process") != null) {
            System.out.println(" inside wifi process not null ");
            if (hm.get("wifi_process").equals("request")) {
                System.out.println(" inside wifi process not null request ");
                /*String wifi_value = "";
                
                 if (hm.get("wifi_value") != null)
                 {
                 wifi_value = hm.get("wifi_value").toString();
                 }*/
                if (wifi_value.equals("2")) {
                    System.out.println(" inside wifi value 2 ");
                    String wifi_os1 = "", wifi_os2 = "", wifi_os3 = "";
                    if (hm.get("wifi_os1") != null) {
                        wifi_os1 = hm.get("wifi_os1").toString();
                    }
                    if (hm.get("wifi_os2") != null) {
                        wifi_os2 = hm.get("wifi_os2").toString();
                    }
                    if (hm.get("wifi_os3") != null) {
                        wifi_os3 = hm.get("wifi_os3").toString();
                    }
                    System.out.println(" wifi_os1 value is " + wifi_os1);
                    if (wifi_os1.toLowerCase().contains("ios") || wifi_os2.toLowerCase().contains("ios") || wifi_os3.toLowerCase().contains("ios")
                            || wifi_os1.toLowerCase().contains("apple") || wifi_os2.toLowerCase().contains("apple") || wifi_os3.toLowerCase().contains("apple")) { // tolowercase added by pr on 29thnov18, modified by pr on 12thdec18
                        System.out.println(" inside wifi process not null request iossssssssss ");
                        generateWiFiPDF = true;
                    }
                }
            } else if (hm.get("wifi_process").equals("certificate")) {
                System.out.println(" inside wifi process not null certificate ");
                generateWiFiPDF = true;
            }
        }
        System.out.println(" generateWiFiPDF value is " + generateWiFiPDF);
        // end, code added by pr on 5thnov18      
        if (generateWiFiPDF) {
            System.out.println(" inside generateWiFiPDF function generateWiFiPDF TRUE ");
            String ref_num = reg_no;
            String auth_off_name = "", designation = "", mobile = "", auth_email = "", add_state = "", fwd_ofc_name = "", fwd_ofc_desig = "", fwd_ofc_mobile = "",
                    fwd_ofc_email = "", fwd_ofc_add = "", emp_code = "", employment = "", ministry = "", department = "", other_dept = "", state = "", organization = "";
            if (hm != null) {
                if (hm.get("auth_off_name") != null) {
                    auth_off_name = hm.get("auth_off_name").toString();
                }
                if (hm.get("designation") != null) {
                    designation = hm.get("designation").toString();
                }
                if (hm.get("mobile") != null) {
                    mobile = hm.get("mobile").toString();
                }
                if (hm.get("auth_email") != null) {
                    auth_email = hm.get("auth_email").toString();
                }
                if (hm.get("add_state") != null) {
                    add_state = hm.get("add_state").toString();
                }
                if (hm.get("fwd_ofc_name") != null) {
                    fwd_ofc_name = hm.get("fwd_ofc_name").toString();
                }
                if (hm.get("fwd_ofc_desig") != null) {
                    fwd_ofc_desig = hm.get("fwd_ofc_desig").toString();
                }
                if (hm.get("fwd_ofc_mobile") != null) {
                    fwd_ofc_mobile = hm.get("fwd_ofc_mobile").toString();
                }
                if (hm.get("fwd_ofc_email") != null) {
                    fwd_ofc_email = hm.get("fwd_ofc_email").toString();
                }
                if (hm.get("fwd_ofc_add") != null) {
                    fwd_ofc_add = hm.get("fwd_ofc_add").toString();
                }
                if (hm.get("emp_code") != null) {
                    emp_code = hm.get("emp_code").toString();
                }
                if (hm.get("employment") != null) {
                    employment = hm.get("employment").toString();
                }
                if (hm.get("ministry") != null) {
                    ministry = hm.get("ministry").toString();
                }
                if (hm.get("department") != null) {
                    department = hm.get("department").toString();
                }
                if (hm.get("other_dept") != null) {
                    other_dept = hm.get("other_dept").toString();
                }
                if (hm.get("state") != null) {
                    state = hm.get("state").toString();
                }
                if (hm.get("organization") != null) {
                    organization = hm.get("organization").toString();
                }
            }
            FileOutputStream file = null;
            Document document = null;
            PdfWriter writer = null;
            String fileName = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + ref_num + ".pdf";
            try {
                //file = new FileOutputStream("/eForms/PDF/" + ref_num + ".pdf");
                file = new FileOutputStream(fileName);
                //file = new FileOutputStream("F:/" + ref_num + ".pdf");
                document = new Document();
                writer = PdfWriter.getInstance(document, file);
                document.open();
                Font normalFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
                Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
                Font HeaderFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
                Paragraph emptypara = new Paragraph("\n");
//                Paragraph header = new Paragraph();
//
//                header.add(new Paragraph("National Informatics Centre", HeaderFont));
//                header.add(new Paragraph("Certificate form for Wi-Fi", HeaderFont));
//                
//                //header.add(new Paragraph("SECTION I: SUBSCRIBER INFORMATION FOR CERTIFICATE", HeaderFont));
//
//                header.setAlignment(header.ALIGN_CENTER);
//
//                document.add(header);
                Paragraph foot4 = new Paragraph();
                foot4.add(new Paragraph("National Informatics Centre", HeaderFont));
                foot4.setAlignment(foot4.ALIGN_CENTER);
                document.add(foot4);
                document.add(emptypara);
                foot4 = new Paragraph();
                foot4.add(new Paragraph("Certificate form for Wi-Fi", HeaderFont));
                foot4.setAlignment(foot4.ALIGN_CENTER);
                document.add(foot4);
                document.add(emptypara);
                foot4 = new Paragraph();
                foot4.add(new Paragraph("SECTION I: SUBSCRIBER INFORMATION FOR CERTIFICATE", HeaderFont));
                foot4.setAlignment(foot4.ALIGN_CENTER);
                document.add(foot4);
                document.add(emptypara);
                Paragraph signup_date = new Paragraph("Application No.(Office Use): " + ref_num, boldFont);
                signup_date.setAlignment(signup_date.ALIGN_RIGHT);
                Paragraph acc_num = new Paragraph("Personal Details:", boldFont);
                acc_num.setAlignment(acc_num.ALIGN_LEFT);
                document.add(signup_date);
                document.add(acc_num);
                document.add(emptypara);
                // start,  applicant details table
                PdfPTable table = new PdfPTable(2); // table with 4 columns
                table.setTotalWidth(new float[]{240, 240}); // widths of columns
                table.setLockedWidth(true);
                // first row          
                PdfPCell cell = new PdfPCell(new Phrase("*Full Name:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(auth_off_name, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // second row
                cell = new PdfPCell(new Phrase("*Designation and Emp Code:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // start, code added by pr on 29thnov18
                String desig_emp = designation;
                if (!emp_code.equals("")) {
                    desig_emp += " (" + emp_code + ")";
                }
                // end, code added by pr on 29thnov18
                //cell = new PdfPCell(new Phrase(designation + " (" + emp_code + ")", normalFont)); 
                cell = new PdfPCell(new Phrase(desig_emp, normalFont)); // line modified by pr on 29thnov18
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // third row
                cell = new PdfPCell(new Phrase("*Mobile No.:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(mobile, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // fourth row
                cell = new PdfPCell(new Phrase("*Email:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(auth_email, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // fifth row
//                cell = new PdfPCell(new Phrase("Division/Group (for NIC Employees):", normalFont)); // second row first column
//                //cell.setFixedHeight(30);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setBorder(Rectangle.BOX);
//                table.addCell(cell);
//
//                cell = new PdfPCell(new Phrase("Mail and Messaging Division", normalFont)); // second row second column
//                //cell.setFixedHeight(30);
//                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                cell.setColspan(3);
//                cell.setBorder(Rectangle.BOX);
//                table.addCell(cell);
                // sixth row
                cell = new PdfPCell(new Phrase("Location:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(add_state, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // seventh row
                cell = new PdfPCell(new Phrase("Department/Ministry :", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                String dept_min = "";
                if (employment.equalsIgnoreCase("central")) {
                    if (department.equalsIgnoreCase("other")) {
                        dept_min = other_dept;
                    } else {
                        dept_min = department;
                    }
                    if (!ministry.equals("")) {
                        dept_min = dept_min + "/" + ministry;
                    }
                } else if (employment.equalsIgnoreCase("state")) {
                    if (department.equalsIgnoreCase("other")) {
                        dept_min = other_dept;
                    } else {
                        dept_min = department;
                    }
                } else if (employment.equalsIgnoreCase("Psu") || employment.equalsIgnoreCase("Const") || employment.equalsIgnoreCase("Nkn") || employment.equalsIgnoreCase("Others")) {
                    if (organization.equalsIgnoreCase("other")) {
                        dept_min = other_dept;
                    } else {
                        dept_min = organization;
                    }
                }
                cell = new PdfPCell(new Phrase(dept_min, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                table.setHorizontalAlignment(Element.ALIGN_LEFT);
                document.add(table);
                document.add(emptypara);
                // start, code added by pr on 13thdec18
                Paragraph dec = new Paragraph("Declaration", HeaderFont);
                dec.setAlignment(dec.ALIGN_CENTER);
                document.add(dec);
                document.add(emptypara);
                Paragraph declare = new Paragraph();
                declare.add(new Paragraph("I hereby declare that :", normalFont));
                //tnc.setAlignment(header.ALIGN_CENTER);
                declare.add(new Paragraph("1. The information provided is correct.", normalFont));
                declare.add(new Paragraph("2. I am responsible for the safety of the Digital Certificate, PIN, Username and Password issued for accessing Wi-Fi Service.", normalFont));
                declare.add(new Paragraph("3. I undertake to surrender the Digital certificate on transfer/leaving the division.", normalFont));
                declare.add(new Paragraph("4. The certificate issued will be used only for accessing the NIC Wi-Fi Service.", normalFont));
                declare.add(new Paragraph("5. Will not indulge in any activity and no attempt will be made to gain unauthorized access to other NIC Websites and facilities.", normalFont));
                declare.add(new Paragraph("6. I am responsible for the content/data uploaded in the servers through Wi-Fi.", normalFont));
                dec.setAlignment(declare.ALIGN_LEFT);
                document.add(declare);
                document.add(emptypara);
                // end, code added by pr on 13thdec18
                foot4 = new Paragraph();
                foot4.add(new Paragraph("SECTION II: Verification by NIC-Coordinator", HeaderFont));
                foot4.setAlignment(foot4.ALIGN_CENTER);
                document.add(foot4);
                document.add(emptypara);
                // start,  applicant details table
                table = new PdfPTable(2); // table with 4 columns
                table.setTotalWidth(new float[]{240, 240}); // widths of columns
                table.setLockedWidth(true);
                // first row          
                cell = new PdfPCell(new Phrase("*Full Name:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(fwd_ofc_name, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // second row
                cell = new PdfPCell(new Phrase("*Designation:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(fwd_ofc_desig, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // third row
                cell = new PdfPCell(new Phrase("*Mobile:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(fwd_ofc_mobile, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // fourth row
                cell = new PdfPCell(new Phrase("*Email:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(fwd_ofc_email, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                // sixth row
                cell = new PdfPCell(new Phrase("Location:", normalFont)); // second row first column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(fwd_ofc_add, normalFont)); // second row second column
                //cell.setFixedHeight(30);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setColspan(3);
                cell.setBorder(Rectangle.BOX);
                table.addCell(cell);
                table.setHorizontalAlignment(Element.ALIGN_LEFT);
                document.add(table);
                document.add(emptypara);
                Paragraph foot6 = new Paragraph();
                foot6.add(new Paragraph("This is a Digitally Signed certificate so it doesn't need any stamp.", HeaderFont));
                foot6.setAlignment(foot6.ALIGN_CENTER);
                document.add(foot6);
                String[] attach = new String[]{fileName};
                Inform obj = new Inform();
                // send mails to the applicant, vpn support team, wifi team
                obj.sendWifiPDF(attach, auth_email);
            } catch (Exception e) {
                // above line commented below added by pr on 23rdapr19                    
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 282" + e.getMessage());
                System.out.println("PDF wifi vpn generation exception: " + e.getMessage());
            } finally {
                try {
                    if (document != null) {
                        document.close();
                    }
                    if (file != null) {
                        file.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 283" + e.getMessage());
                }
            }
        }
        // update the values in the DB
        PreparedStatement ps = null; // line added by pr on 7thdec18
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE wifi_registration SET wifi_value_admin = ? ";
            int cnt = 1;
            if (generateWiFiPDF) // if pdf is to be generated then the column cert_mail_sent needs to be updated
            {
                qry += ", cert_mail_sent = ? ";
                cnt++;
            }
            qry += " WHERE  registration_no = ?  ";
            cnt++;
            ps = con.prepareStatement(qry);
            ps.setString(1, wifi_value);
            if (generateWiFiPDF) {
                ps.setString(2, "y");
            }
            ps.setString(cnt, regNo);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchStateCoordsAtCA func query is " + ps);
            int i = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde checkAlreadyStatus " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                   
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 284" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 285" + e.getMessage());
                }
            }
        }
    }

    // function modified by pr on 2ndjan2020
    public String fetchwifivalue() {
        wifi_value = "";
        String[] arr = null;
        if (regNo != null && !regNo.equals("")) {
            arr = regNo.split("~");
        }
        if (arr != null && arr.length > 0) {
            regNo = arr[0];
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Connection con = null;

            String email = "";// line added by pr on 2ndjan2020

            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // auth_email added by pr on 2ndjan2020
                String qry = "SELECT wifi_value, wifi_value_admin, wifi_process, wifi_os1, wifi_os2, wifi_os3, cert_mail_sent, auth_email FROM wifi_registration WHERE  registration_no = ?  "; // line modified by pr on 29thnov18 other attributes are fetched
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNo);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchwifivalue func query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    wifi_value = rs.getString("wifi_value").trim();
                    // start, code added by pr on 29thnov18
                    if (rs.getString("wifi_value_admin") != null && !rs.getString("wifi_value_admin").equals("") && !rs.getString("wifi_value_admin").equals("0")) {
                        wifi_value = rs.getString("wifi_value_admin").trim();
                    }
                    String isIOS = "n";
                    //if( wifi_value.equals("2") ) // if it is for Vayu and os is ios then execute this block
                    {
                        if (rs.getString("wifi_os1") != null && !rs.getString("wifi_os1").equals("")) {
                            System.out.println(" inside wifi_value as 2 1 ");
                            if (rs.getString("wifi_os1").toLowerCase().contains("ios") || rs.getString("wifi_os1").toLowerCase().contains("apple")) // line modified by pr on 12thdec18
                            {
                                System.out.println(" inside wifi_value as 2 2 ");
                                isIOS = "y";
                            }
                        }
                        if (rs.getString("wifi_os2") != null && !rs.getString("wifi_os2").equals("")) {
                            System.out.println(" inside wifi_value as 2 3 ");
                            if (rs.getString("wifi_os2").toLowerCase().contains("ios") || rs.getString("wifi_os2").toLowerCase().contains("apple"))// line modified by pr on 12thdec18
                            {
                                System.out.println(" inside wifi_value as 2 4 ");
                                isIOS = "y";
                            }
                        }
                        if (rs.getString("wifi_os3") != null && !rs.getString("wifi_os3").equals("")) {
                            System.out.println(" inside wifi_value as 2 5 ");
                            if (rs.getString("wifi_os3").toLowerCase().contains("ios") || rs.getString("wifi_os3").toLowerCase().contains("apple"))// line modified by pr on 12thdec18
                            {
                                System.out.println(" inside wifi_value as 2 6 ");
                                isIOS = "y";
                            }
                        }
                    }
                    // create a value in wifi_value which contains 1=> wifi_value, 2=> wifi_process, 3 => y/n based on ios in wifi_os1/wifi_os2/wifi_os3, 4 => certficate related mail already sent to VPN team
                    if (rs.getString("wifi_process") != null && !rs.getString("wifi_process").equals("")) {
                        wifi_value += "~" + rs.getString("wifi_process").trim();
                    }
                    wifi_value += "~" + isIOS;
                    String isMailSent = "n";
                    if (rs.getString("cert_mail_sent") != null && !rs.getString("cert_mail_sent").equals("")) {
                        if (rs.getString("cert_mail_sent") != null && !rs.getString("cert_mail_sent").equals("") && rs.getString("cert_mail_sent").equals("y")) {
                            isMailSent = "y";
                        }
                    }
                    wifi_value += "~" + isMailSent;
                    // end, code added by pr on 29thnov18

                    email = rs.getString("auth_email"); // line added by pr on 2ndjan2020

                }

                // start, code added by pr on 2ndjan2020
                ldap_wifi_value = entities.LdapQuery.fetchwifivalue(email);

                // end, code added by pr on 2ndjan2020
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchwifivalue  wifi_value is  " + wifi_value + " ldap_wifi_value is " + ldap_wifi_value);
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde checkAlreadyStatus " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 243" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 244" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 245" + e.getMessage());
                    }
                }
            }
        }
        return SUCCESS;
    }

    // below function added by pr on 31staug18
    public String fetchimapvalue() {
        imap_value = "";
        System.out.println(" inside fetchimapvalue func value of regno is " + regNo);
        String[] arr = null;
        if (regNo != null && !regNo.equals("")) {
            arr = regNo.split("~");
        }
        if (arr != null && arr.length >= 2) {
            System.out.println(" inside fetchimapvalue array value is " + arr + " arr 0 is " + arr[0] + " arr 1 " + arr[1]);
            HashMap hm = fwdObj.fetchFormDetail(arr[0], arr[1]);
            String protocol = "", email = "", attValue = "";
            if (hm != null) {
                if (hm.get("protocol") != null) {
                    protocol = hm.get("protocol").toString().trim();
                }
                if (hm.get("email") != null) {
                    email = hm.get("email").toString().trim();
                }
            }
            //attValue = fetchAttributeValue( email, protocol ); it gives the to be formed value
            attValue = fetchLDAPMailAttribute(email);
            imap_value = protocol + "~" + attValue;  // append protocol with current ldap value for mailAllowedServiceAccess attribute with a ~
            System.out.println(" inside fetchimapvalue func attvalue is " + attValue + " wifi_value is " + wifi_value);
        }
        return SUCCESS;
    }

    public String fetchdorpdf() {
        imap_value = "";
        System.out.println(" inside fetchdorpdf func value of regno is " + regNo);
        String[] arr = null;
        if (regNo != null && !regNo.equals("")) {
            arr = regNo.split("~");
        }
        if (arr != null && arr.length >= 2) {
            System.out.println(" inside fetchdorpdf array value is " + arr + " arr 0 is " + arr[0] + " arr 1 " + arr[1]);
            HashMap hm = fwdObj.fetchFormDetail(arr[0], arr[1]);
            String emp_type = "", work_order = "", dor = "";
            if (hm != null) {
                if (hm.get("emp_type") != null) {
                    emp_type = hm.get("emp_type").toString().trim();
                }
                if (hm.get("dor") != null) {
                    dor = hm.get("dor").toString().trim();
                }
                if (hm.get("work_order") != null) {
                    work_order = hm.get("work_order").toString().trim();
                }
            }

            imap_value = emp_type + "~" + dor + "~" + work_order;
            System.out.println(" inside fetchdorpdf func attvalue is value t o pass is " + imap_value);
        }
        return SUCCESS;
    }

    // below function added by pr on 1staug19
    public HashMap<String, Object> createZimEmailAliasSingle(final String uid, String mailPassed, final String admin_token, final URL url, final String zimData, final String po, final String bo) { // zimData has been passed from createzimemailid function already

        HashMap<String, Object> resultHM = new HashMap<String, Object>();

        mailPassed = mailPassed.trim().toLowerCase();

        String ldap_mailequ = "";

        if (!mailPassed.contains("@nic.in")) // if added  by pr on 25thapr18
        {
            ldap_mailequ = uid + "@nic.in";
        }

        // start, code added by pr on 1staug19, just to make the variables final in order for them to get accesses in run method below
        final String mail = mailPassed;

        final String ldap_mailequivalentaddress = ldap_mailequ;

        // end, code added by pr on 1staug19
        System.out.println(" mail before spilt is " + mailPassed);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " ==  inside createZimEmailAliasSingle function uid " + uid + " mail " + mailPassed + " admin_token " + admin_token + " url " + url + " ldap_mailequivalentaddress " + ldap_mailequivalentaddress);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " ==  inside createZimEmailAliasSingle function NOOOOOOOOO creation error start " + zimData);

        final String sessionID = ServletActionContext.getRequest().getSession().getId().toString();

        try {

            int delay = 100; //msecs
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {

                    String message = "";

                    boolean valid = true;

                    //message = "All is well";
                    // below line commented by pr on 1staug19 for testing, comments to be removed in production
                    String mail_id = Zimbra_SOAP_v2.GetAccountID(mail, admin_token, url); // prefix String deleted by pr on 17thjul19
                    //String mail_id = "SUCCESS"; // line added for testing to be removed in production on 1staug19

                    System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function MAIL ID CREATED IS " + mail_id);

                    if (!mail_id.contains("ERROR")) // if around and else added by pr on 7thjun19
                    {
                        String response_zimbra_alias1 = "", response_zimbra_alias2 = ""; // line added by pr on 6thmay19, modified by pr on 7thjun19

                        //String response_zimbra_alias = ""; // line  added by pr on 6thmay19, line commented by pr on 7thjun19
                        if (ldap_mailequivalentaddress != null && !ldap_mailequivalentaddress.isEmpty()) {

                            System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function INSIDE ldap_mailequivalentaddress " + ldap_mailequivalentaddress);

                            // below line commented by pr on 18thjul19 for testing, comments to be removed in production
                            response_zimbra_alias1 = Zimbra_SOAP_v2.AddAccountAlias(mail_id, admin_token, ldap_mailequivalentaddress, url); // 1 added by pr on 1staug19
                            //response_zimbra_alias1 = "SUCCESS"; // line added for testing to be removed in production
                            System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function INSIDE ldap_mailequivalentaddress  ALIAS RESPONSE IS " + response_zimbra_alias1);

                        }

                        // below line commented by pr on 18thjul19 for testing, comments to be removed in production
                        response_zimbra_alias2 = Zimbra_SOAP_v2.AddAccountAlias(mail_id, admin_token, uid + "@gov.in.local", url); // 2 added by pr on 1staug19
                        //response_zimbra_alias2 = "SUCCESS"; // line added for testing to be removed in production on 1staug19
                        System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function AFTER ldap_mailequivalentaddress  ALIAS RESPONSE for gov.in.local IS " + response_zimbra_alias2);

                        if (response_zimbra_alias1.contains("ERROR") || response_zimbra_alias2.contains("ERROR")) // if block added by pr on 7thjun19
                        {
                            message = "Zimbra Alias Error";
                            valid = false;
                            //zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.isEmpty() ? "NA" : ldap_mailequivalentaddress);
                            System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function Alias Error " + zimData);
                        }

                    } else {
                        message = "Zimbra GetAccountID Error";
                        valid = false;
                        //zimData = po + ":" + bo + ":" + uid + ":" + rs.get("fname") + ":" + rs.get("lname") + ":" + rs.get("description") + ":" + rs.get("mobile") + ":" + (rs.get("dob") == null || rs.get("dob").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dob")))) + ":" + (rs.get("dor") == null || rs.get("dor").isEmpty() ? "NA" : fetchLDAPModifyDate((rs.get("dor")))) + ":" + rs.get("title") + ":" + rs.get("department") + ":" + rs.get("state") + ":" + (rs.get("emp_code") == null || rs.get("emp_code").isEmpty() ? "NA" : rs.get("emp_code")) + ":" + mail + ":" + (ldap_mailequivalentaddress == null || ldap_mailequivalentaddress.isEmpty() ? "NA" : ldap_mailequivalentaddress);
                        System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function Zimbra GetAccountID Error " + zimData);

                    }

                    if (!valid) // if id gets created in zimbra too
                    {
                        System.out.println(sessionID + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                + " after calling createZimEmailAliasSingle in single email creation data is " + zimData + " zimValid is " + valid + " message is " + message + " po is " + po + " bo is " + bo);

                        Inform infObj = new Inform();

                        infObj.sendZimCreateMail(po, bo, zimData);
                    }

                }

            }, delay);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle catch method exception is " + e.getMessage());

            e.printStackTrace();
        }

        // above code commented by pr on 1stnov19 for testing, to be uncommented in production
        // start, code added by pr on 1stnov19 for testing, to be commented in production
//        resultHM.put("message", null);
//
//        resultHM.put("valid", true);
//
//        resultHM.put("data", null);
//        
        // end, code added by pr on 1stnov19 for testing, to be commented in production
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  inside createZimEmailAliasSingle function ");

        return resultHM;
    }

    // below function added by pr on 26thdec18, to get the reject_remarks created by the system for some bulk ids
    public HashMap fetchRejectRemarks(String bulk_id) {
        HashMap<String, String> hm = new HashMap<String, String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String reject_remarks = "", id = "";
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT reject_remarks, bulk_id FORM bulk_users WHERE bulk_id in (?)";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, bulk_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                reject_remarks = rs.getString("reject_remarks");
                id = rs.getString("bulk_id");
                hm.put(id, reject_remarks);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchRejectRemarks is " + ps);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 250" + e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 251" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 252" + e.getMessage());
                }
            }
        }
        return hm;
    }

    // below function added by pr on 4thapr18, to manually reject the ids which cannot be processed
    public String rejectBulkID() {
        Boolean isRemaining = true; // line added  by pr on 26thdec18  
        HashMap<String, String> hm = null; // line added  by pr on 26thdec18  
        if (bulk_id.matches("^[0-9,]*$")) // only numbers and comma
        {
            hm = fetchRejectRemarks(bulk_id);// line added  by pr on 26thdec18  
            String[] bulk_id_arr = bulk_id.split(",");
            String is_created = "n";
            String is_rejected = "y"; // manually reject only

            // start, code added by pr on 23rdjul19
            String rejected_by = "";

            String role = fetchSessionRole(); // line added by pr on 23rdjul19

            if (role.equals(Constants.ROLE_CO)) {
                rejected_by = "Coordinator( " + fetchSessionEmail() + " )";
            } else if (role.equals(Constants.ROLE_CA)) { // else if added by pr on 6thdec19
                rejected_by = "Reporting Officer( " + fetchSessionEmail() + " )";
            } else {
                rejected_by = "Admin( " + fetchSessionEmail() + " )";
            }

            // end, code added by pr on 23rdjul19
            //String rejected_by = "Admin( " + fetchSessionEmail() + " )"; // line commented by pr on 23rdjul19
            PreparedStatement ps = null;
            //Connection con = null;
            try {
                con = DbConnection.getConnection();
                String qry = "UPDATE bulk_users SET is_created = ?, is_rejected = ?, reject_remarks = CONCAT(reject_remarks, ' Rejected.') ,"
                        + ""
                        + " updatedon = current_timestamp, rejected_by = ?  ";

                // start, code added by pr on 23rdjul19
                if (!statRemarks.equals("")) {
                    qry += " , reject_remarks = ? ";
                } else {
                    qry += " , reject_remarks = CONCAT(reject_remarks, ' Rejected.') ";
                }

                // end, code added by pr on 23rdjul19                
                if (bulk_id_arr.length > 0) {
                    qry += " WHERE ";
                    int i = 1;
                    for (String id : bulk_id_arr) {
                        if (i == 1) {
                            qry += " bulk_id = ?  ";
                        } else {
                            qry += " OR bulk_id = ?  ";
                        }
                        bulkUpdateData.put(id, hm.get(id) + " Rejected." + "~y"); // value => reject remarks none or value ~ show checkbox or not
                        i++;
                    }
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " qry is " + qry);

                ps = con.prepareStatement(qry);
                ps.setString(1, is_created);
                ps.setString(2, is_rejected);
                ps.setString(3, rejected_by);

                int j = 3; // modified by pr on 2nddec19
                if (!statRemarks.equals("")) // if block added by pr on 23rdjul19
                {
                    j++; // added by pr on 2nddec19
                    ps.setString(j, statRemarks);
                }

                j++;

                for (String id : bulk_id_arr) {
                    ps.setString(j, id);
                    j++;
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reject bulk query is " + ps);
                int i = ps.executeUpdate();
                if (i > 0) {
                    isRemaining = checkBulkAndUpdateStatus(regNo, Constants.BULK_FORM_KEYWORD); // line added by pr on 14thdec17 to be added after above query execution modified on 26thdec17,LHS added by pr on 26thdec18
                    isSuccess = true;
                    isError = false;
                    msg = "Records Rejected Successfully.";
                } else {
                    isSuccess = false;
                    isError = true;
                    msg = "Records could not be Rejected.";
                }
                ps.close(); // line added by pr on 6thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 9" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 253" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 254" + e.getMessage());
                    }
                }
            }
        } else {
            isSuccess = false;
            isError = true;
            msg = "Bulk ID is Invalid.";
        }

        //isRemaining = checkBulkAndUpdateStatus(regNo, Constants.BULK_FORM_KEYWORD); // line added by pr on 26thapr19 to resolve bulk request not getting completed issue
        System.out.println(" inside rejectbulkid function isRemaining value is " + isRemaining);

        // start, code added by pr on 26thdec18
        if (isRemaining) {
            toggleBulk = false;
        } else {
            toggleBulk = true;
        }
        // end, code added by pr on 26thdec18        
        return SUCCESS;
    }

    // below function added by pr on 4thjun18 so that in case of single user all emails are visible, returns uids with br tag and pipe ( | ) separation 
    public String fetchMobileLDAPMailAll(String mobile) {
        if (mobile.startsWith("+91")) {
            mobile = mobile.substring(3); // start from the index 3
        } else if (mobile.startsWith("91")) {
            mobile = mobile.substring(2);// start from the index 2
        }
        mobile = "+91" + mobile;
        String finalStr = "";
        DirContext ctx = null;// line added by pr on 16thoct18
        try {
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurlSlave);
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
            //String mobile = "+919810469060";
            //String mobile = "9810469060";
            String filter = "(mobile=" + mobile + ")";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            ArrayList<String> arr = new ArrayList<String>();
            if (ans.hasMoreElements()) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside has more ");
                while (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    // get substring from start { to }
                    int start = t.indexOf("{");
                    int end = t.indexOf("}");
                    String substr = t.substring(start + 1, end);
                    String temp = "";
                    if (substr.contains("mail:")) // if there is an email linked with this mobile then only show them 
                    {
                        // get mail
                        String mail = substr.substring(substr.indexOf("mail:") + 6, substr.indexOf(","));
                        mail = mail.trim();
                        // get maileqv
                        String maileqv = substr.substring(substr.indexOf("mailEquivalentAddress:") + 23, substr.indexOf("uid="));
                        maileqv = maileqv.trim();
                        maileqv = maileqv.substring(0, maileqv.length() - 1);
                        maileqv = maileqv.trim();
                        // get uid
                        String uid = substr.substring(substr.indexOf("uid:") + 5);
                        uid = uid.trim();
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " substring value is " + substr);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " uid is " + uid + " mail is " + mail + " maileqv  " + maileqv);
                        temp = uid + "|" + mail + "|" + maileqv;
                        if (!arr.contains(temp)) {
                            arr.add(temp);
                        }
                    }
                    //System.out.println(" search string value is " + t+" substring value is "+substr);
                }
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside  ELSE  has more ");
            }
            if (arr != null && arr.size() > 0) {
                int i = 1;
                for (String str : arr) {
                    if (i == 1) {
                        finalStr = str;
                    } else {
                        finalStr += "<br>|<br>" + str; // <br> added by pr on 1stjun18
                    }
                    i++;
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " final string is " + finalStr);
            //ctx.close();
//            ht.clear();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 168" + e.getMessage());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Exception in User detail Mobile finding ::::::: " + e);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        }
        return finalStr;
    }

    // below function added by pr on 7thdec18 for wifi
    public String send_wifi_certificate() {
        System.out.println(" inside get wifi value function regNo value is " + regNo + " wifi_value is " + wifi_value);
        if (regNo != null && !regNo.equals("") && wifi_value != null && !wifi_value.equals("")) { // add a regex check for wifi_value attribute and regno
            // update in the LDAP the updated wifi_value 
            HashMap hm = fwdObj.fetchFormDetail(regNo, Constants.WIFI_FORM_KEYWORD);
            String email = "";
            if (hm != null && hm.get("email") != null) {
                if (hm.get("email") != null) {
                    email = hm.get("email").toString();
                }
            }
            if (!wifi_value.equals("") && !wifi_value.equals("0")) {
                if (!email.equals("")) {
                    String dn = fwdObj.fetchLDAPDN(email, Constants.WIFI_FORM_KEYWORD);
                    if (!dn.equals("")) {
                        modifyLDAPAttribute(dn, "nicWIFI", wifi_value);
                    }
                }
            }
            // send mails with the certificate attached
            // update the value of wifi_value_admin and cert_mail_sent columns in wifi_registration table in the below function only it will get updated
            generateWiFiPDF(regNo, wifi_value);
        }
        return SUCCESS;
    }

    public Boolean createSMSId(String uid, String reg_no, String pass) {
        uid = uid.trim().toLowerCase(); // line added by pr on 11thapr18
        HashMap<String, String> rs = fetchSMSDetail(reg_no);
        String bo = "inoc services";
        String po = "Application Services";
        Boolean valid = true;
        if (rs != null) {
            String dn_default = "";
            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurlSlave);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, createdn);
//            ht.put(Context.SECURITY_CREDENTIALS, createpass);
            // FETCH BO DETAILS, START
            String sp2 = "", sp3 = "", sp1 = "", domain = "", mailhost = "", messagestore = "", s = "";
            DirContext ctx1 = null;
            DirContext ctx2 = null;// line added by pr on 16thoct18
            DirContext ctx3 = null;// line added by pr on 16thoct18
            try {
                //ctx1 = new InitialDirContext(ht);

                ctx1 = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                String[] IDs = {"sunAvailableServices", "preferredMailHost", "preferredMailMessageStore", "sunAvailableDomainNames"};
                SearchControls ctls = new SearchControls();
                ctls.setReturningAttributes(IDs);
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                dn_default = "o=" + bo + ",o=" + po + ",o=nic.in,dc=nic,dc=in";
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " dn_default is " + dn_default);
                String filter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo + "))";
                NamingEnumeration ans = ctx1.search(dn_default, filter, ctls);
                while (ans.hasMoreElements()) {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside while loop ");
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    s = t.substring(t.indexOf("sunAvailableServices:") + 22, t.indexOf(",")); //for service package
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "sunAvailableServices is " + s);
                    String s2 = t.substring(t.indexOf("preferredMailHost:") + 19, t.length()); //for mailhost
                    mailhost = s2.substring(0, s2.indexOf(",")); //for mailhost
                    String s3 = t.substring(t.indexOf("preferredMailMessageStore:") + 27, t.length()); //for messagestore
                    messagestore = s3.substring(0, s3.lastIndexOf("}")); //for messagestore
                    String s4 = t.substring(t.indexOf("sunAvailableDomainNames:") + 25, t.length());
                    domain = s4.substring(0, s4.lastIndexOf(", preferredmailmessagestore"));
                    sp1 = s.substring(0, s.indexOf(":"));
                    String sp2tmp = s.substring(s.indexOf(":") + 1, s.length());
                    if (sp2tmp.contains(":")) {
                        sp2 = (sp2tmp.substring(0, sp2tmp.indexOf(":"))).trim();
                        sp3 = (sp2tmp.substring(sp2tmp.indexOf(":") + 1, sp2tmp.length())).trim();
                    } else {
                        sp2 = sp2tmp.trim();
                        sp3 = "0";
                    }
                }
                List<String> myList = new ArrayList<String>(Arrays.asList(domain.split(",")));
                ArrayList alnew = new ArrayList();
                Iterator itr = myList.iterator();
                while (itr.hasNext()) {
                    String mail = ((String) itr.next()).trim();
                    if (mail.equals("nic.in")) {
                    } else {
                        alnew.add(mail);
                    }
                }
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " sp2 is " + sp2 + " sp3 is " + sp3);
                /*int i = Integer.parseInt(sp2);
                 int j = Integer.parseInt(sp3);
                 int k = i - j;*/
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Error in sunAvailableServices Package =>");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 122" + e.getMessage());
            }
            try {
                int spcount = 0;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String NewDN = "uid=" + uid + ",ou=People," + dn_default;
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "new dn is " + NewDN);
                //adding attributes to the ldif
                Attribute attr1 = new BasicAttribute("givenName", uid); // givenname=first name
                Attribute attr2 = new BasicAttribute("sn", "For Application"); //sn=last name
                Attribute attr3 = new BasicAttribute("cn", uid); // cn = givenname+sn
                Attribute attr4 = new BasicAttribute("title", "For Application");//for designation
                Attribute attr5 = new BasicAttribute("departmentNumber", (rs.get("department") != null ? rs.get("department") : ""));//for department/min...
                Attribute attr6 = new BasicAttribute("st", (rs.get("city") != null ? rs.get("city") : ""));     //for state        
                Attribute attr7 = new BasicAttribute("mobile", (rs.get("mobile") != null ? rs.get("mobile") : "")); //for mobile
                Attribute attr8 = new BasicAttribute("uid", uid); //for uid
                Attribute attr9 = new BasicAttribute("userPassword", pass); //for password
                Attribute attr10 = new BasicAttribute("inetUserStatus", "Active");  //for inet status
                Attribute attr11 = new BasicAttribute("preferredLanguage", "en");
                Attribute attr12 = new BasicAttribute("description", "For SMS Application"); // description for free or paid
                Attribute attr13 = null;
                if (getDescription() != null && !getDescription().isEmpty()) {
                    attr13 = new BasicAttribute("NicFundingCategory", getDescription()); // description for free or paid
                    System.out.println("CREATE SMS ID :: Adding NicFundingCategory = " + getDescription());
                }
                Attribute attr14 = new BasicAttribute("nicDateOfRetirement", ""); //For DOR
                Attribute attr15 = new BasicAttribute("nicAccountExpDate", ""); //For Account Expiry
                Attribute attr16 = new BasicAttribute("NicAccountType", "3");
                System.out.println("CREATE SMS ID :: Adding NicAccountType = 3");

                // Attribute attr15 = new BasicAttribute("davUniqueId", "0342b55f-9fac-4bd9-9624-nic" + uniqueid + "a");       // added on 12th june2015
                //for adding object class
                Attribute oc = new BasicAttribute("ObjectClass");
                oc.add("top");
                oc.add("organizationalPerson");
                oc.add("person");
                oc.add("inetuser");
                oc.add("ipuser");
                oc.add("inetOrgPerson");
                oc.add("iplanetpreferences");
                oc.add("nicemailuser");
                //oc.add("daventity");
                //ctx2 = new InitialDirContext(ht);

                ctx2 = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                BasicAttributes attrs = new BasicAttributes();
                attrs.put(attr1);
                attrs.put(attr2);
                attrs.put(attr3);
                attrs.put(attr4);
                attrs.put(attr5);
                attrs.put(attr6);
                attrs.put(attr7);
                attrs.put(attr8);
                attrs.put(attr9);
                attrs.put(attr10);
                attrs.put(attr11);
                attrs.put(attr12);
                if (attr13 != null) {
                    attrs.put(attr13);
                }
                attrs.put(attr14);
                attrs.put(attr15);
                attrs.put(attr16);
                //attrs.put(attr15);
                attrs.put(oc);
                // binding the new user
                ctx2.createSubcontext(NewDN, attrs);// to be uncommented in production  
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "AddUser: added entry " + NewDN + ".");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Account Created:::::::::::::::");
                spcount++; //for service package increment after account creation
                //end
                //for package modification
                if (spcount > 0) {
                    try {
                        int newpack = Integer.parseInt(sp3) + spcount;
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "modify code................for service package");

                        //ctx3 = new InitialDirContext(ht);
                        ctx3 = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                        ModificationItem[] mods = new ModificationItem[1];
                        mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                                new BasicAttribute("sunAvailableServices", sp1 + ":" + sp2 + ":" + newpack));
                        ctx3.modifyAttributes(dn_default, mods);// to be uncommented in production 
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + mods[0]);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "service package modification done");
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "error in service package modification =>" + e);
                        //session.setAttribute("ermsg", "Ooops !!! Cannot modify Service package !!!");
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "inside catch for id creation exception createSMSId ");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + e.getMessage());
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 123" + e.getMessage());
                valid = false;
            } finally // finally added by pr on 16thoct18
            {
//                if (ctx1 != null) {
//                    try {
//                        ctx1.close();
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 124" + e.getMessage());
//                    }
//                }
//                if (ctx2 != null) {
//                    try {
//                        ctx2.close();
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 125" + e.getMessage());
//                    }
//                }
//                if (ctx3 != null) {
//                    try {
//                        ctx3.close();
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 126" + e.getMessage());
//                    }
//                }
            }
        } else {
            valid = false;
        }
        // ADD ID IN LDAP, END
        return valid;
    }

    // below function is used to create a random password
    public String fetchRandomPassword() {
        StringBuilder password = new StringBuilder();
        int j = 0;
        for (int i = 0; i < 10; i++) {
            password.append(fetchRandomPasswordCharacters(j));
            j++;
            if (j == 4) {
                j = 0;
            }
        }
        return password.toString();
    }

    private String fetchRandomPasswordCharacters(int pos) {
        Random randomNum = new Random();
        StringBuilder randomChar = new StringBuilder();
        switch (pos) {
            case 0:
                //randomChar.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(randomNum.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZ".length() - 1)));
                randomChar.append("ABCDEFGHJKMNOPQRSTUVWXYZ".charAt(randomNum.nextInt("ABCDEFGHJKMNOPQRSTUVWXYZ".length() - 1)));// line modified by pr on 23rdjul18
                break;
            case 1:
                randomChar.append("0123456789".charAt(randomNum.nextInt("0123456789".length() - 1)));
                break;
            case 2:
                randomChar.append("@#$%&*".charAt(randomNum.nextInt("@#$%&*".length() - 1)));
                break;
            case 3:
                //randomChar.append("abcdefghijklmnopqrstuvwxyz".charAt(randomNum.nextInt("abcdefghijklmnopqrstuvwxyz".length() - 1)));
                randomChar.append("abcdefghjkmnopqrstuvwxyz".charAt(randomNum.nextInt("abcdefghjkmnopqrstuvwxyz".length() - 1)));// line modified by pr on 23rdjul18
        }
        return randomChar.toString();
    }

    public HashMap fetchSMSDetail(String regNo) {
        String sms_service = "";
        ResultSet rs = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select sms_service,department,city,mobile FROM sms_registration WHERE registration_no = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("sms_service", rs.getString("sms_service"));
                hm.put("department", rs.getString("department"));
                hm.put("city", rs.getString("city"));
                hm.put("mobile", rs.getString("mobile"));
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function sms detail " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 109 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 110 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 112  111 " + e.getMessage());
                }
            }
        }
        return hm;
    }

    // fetch all POs to show in email id creation
    public String fetchPOs() {
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        ArrayList<String> arr = new ArrayList<String>();
        DirContext ctx = null;
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

            String[] IDs = {"o"};
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            /*String returnedAtts[] = {};
             ctls.setReturningAttributes(returnedAtts);
             ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);*/
            String filter = "(objectclass=sunmanagedprovider)";
            NamingEnumeration ans = ctx.search("o=nic.in,dc=nic,dc=in", filter, ctls);
            while (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();
                String uids = t.substring(t.indexOf("o=o:") + 4, t.length());
                uids = uids.replace("}", "");
                arr.add(uids);

            }

            System.out.println("sssss " + arr);
            /* else
             {
             System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "no PO found");
             }*/
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
            e.printStackTrace();
        } finally // finally added by pr on 16thoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 173" + e.getMessage());
//                }
//            }
        }
        //Collections.sort(arr);
        podata.clear();
        podata = arr;
        return SUCCESS;
    }

    public String fetchBOs() {
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchBOs func po value is  " + po);
        //bodata.clear();                
        if (po != null && !po.equals("") && po.matches("^[a-zA-Z#0-9\\s,'.\\-\\/\\(\\)]{2,150}$")) // if added by pr on 5thfeb18
        {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside po valid  " + po);
            String PO = po;
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//            ht.put(Context.PROVIDER_URL, providerurlSlave);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, createdn);
//            ht.put(Context.SECURITY_CREDENTIALS, createpass);
            ArrayList<String> arr = new ArrayList<String>();
            DirContext ctx = null;// line added by pr on 16thoct18
            try {
                //ctx = new InitialDirContext(ht);

                ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                String[] IDs = {"o"};
                SearchControls ctls = new SearchControls();
                ctls.setReturningAttributes(IDs);
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                /*String returnedAtts[] = {};
                 ctls.setReturningAttributes(returnedAtts);
                 ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);*/
                String filter = "(objectclass=sunDelegatedOrganization)";
                //NamingEnumeration ans = ctx.search("o="+PO.trim()+",o=nic.in,dc=nic,dc=in", filter, ctls);
                NamingEnumeration ans = ctx.search("o=" + PO.trim() + ",o=nic.in,dc=nic,dc=in", filter, ctls);
                while (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    String uids = t.substring(t.indexOf("o=o:") + 4, t.length());
                    uids = uids.replace("}", "");
                    if (!uids.contains("primary business organization")) {
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside uids doesnt contain primary bus  uids value is  " + uids);
                        arr.add(uids);
                    }
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " after  inside uids doesnt contain primary bus ");
                }
                /* else
                 {
                 System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "no PO found");
                 }*/
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
                e.printStackTrace();
            } finally // finally added by pr on 16thoct18
            {
//                if (ctx != null) {
//                    try {
//                        ctx.close();
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 174" + e.getMessage());
//                    }
//                }
            }
            //Collections.sort(arr);
            bodata.clear();
            bodata = arr;
        } else {
            po = ""; // line added by pr on 7thfeb18 for xss
        }
        if (po != null && !po.equals("")) // line added by pr on 8thfeb18 for xss
        {
            po = ESAPI.encoder().encodeForHTMLAttribute(po);
        }
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " at the end value is size is " + bodata.size());
        return SUCCESS;
    }

    public String fetchBODomain() {
        System.out.println(" inside  fetchBODomain func po value is  " + po + " bo value is " + bo);
        // domaindata.clear();                
        if (po != null && !po.equals("") && po.matches("^[a-zA-Z#0-9\\s,'.\\-\\/\\(\\)]{2,150}$")
                && bo != null && !bo.equals("") && bo.matches("^[a-zA-Z#0-9\\s_,'.\\-\\/\\(\\)]{2,150}$")) // if added by pr on 5thfeb18 // line modified by pr on 19thfeb18
        {
            System.out.println(" INSIDE REGEX CHECK inside  fetchBODomain func po value is  " + po + " bo value is " + bo);
            String PO = po;
            String BO = bo;
            Set<String> arr = new HashSet<>();
            if (BO.trim().equals("NIC Support Outsourced")) {
                arr.add("supportgov.in");
            } else {
//                Hashtable ht = new Hashtable(4);
//                ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//                ht.put(Context.PROVIDER_URL, providerurlSlave);
//                ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//                ht.put(Context.SECURITY_PRINCIPAL, createdn);
//                ht.put(Context.SECURITY_CREDENTIALS, createpass);
                //ArrayList<String> arr = new ArrayList<String>();

                DirContext ctx = null;// line added by pr on 16thoct18
                try {
                    ctx = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020
                    String[] IDs = {"sunAvailableDomainNames"};
                    SearchControls ctls = new SearchControls();
                    ctls.setReturningAttributes(IDs);
                    ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                    String filter = "(objectclass=sunDelegatedOrganization)";
                    NamingEnumeration ans = ctx.search("o=" + BO.trim() + ",o=" + PO.trim() + ",o=nic.in,dc=nic,dc=in", filter, ctls);
                    while (ans.hasMoreElements()) {
                        javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                        String t = sr.toString();
                        String uids = t.substring(t.indexOf("sunAvailableDomainNames:") + 25, t.length());
                        System.out.println(" fetchBODomain : uids == " + uids);
                        String[] arrDom = uids.split(",");
                        for (String val : arrDom) {
                            val = val.replace("}", "");
                            val = val.trim();
                            arr.add(val);
                        }
                    }
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
                    e.printStackTrace();
                }
            }
            domaindata.clear();
            System.out.println(" fetchBODomain ::: Array == " + arr.toString());
            ArrayList<String> arr1 = new ArrayList<>(arr);
            domaindata = arr1;
        } else // else added by pr on 7thfeb18 for xss
        {
            po = "";
            bo = "";
        }
        if (po != null && !po.equals("")) // line added by pr on 8thfeb18 for xss
        {
            po = ESAPI.encoder().encodeForHTMLAttribute(po);
        }
        if (bo != null && !bo.equals("")) // line added by pr on 8thfeb18 for xss
        {
            bo = ESAPI.encoder().encodeForHTMLAttribute(po);
        }
        return SUCCESS;
    }

    // to create bulk ids, function modified by pr on 16thapr19
    public String createBulkID() {
        //String regNo = "";
        String[] arr = null;
        String formName = "bulk";
        Boolean isRemaining = true; // line added  by pr on 26thdec18        
        //if( regNo != null &&  !regNo.equals("")  && regNo.matches("^[a-zA-Z0-9~\\-]*$")  ) // line modified by pr on 5thfeb18 for xss
        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegEx)) // line modified by pr on 5thfeb18 for xss
        {
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        } else {
            regNo = ""; // line added by pr on 6thfeb18
        }
        // below if modified by pr on 3rdoct19        
        if (CSRFRandom.matches(regNoRegEx) && sessionMap.get("CSRFRandom").equals(CSRFRandom) && arr != null
                && (statRemarks == null || statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)))) // if across added by pr on 3rdjan18 to apply csrf token check , arr not null added by pr on 5thfeb18
        {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside createBulkID func and inside random value match with session value ");
            // in this case action type will be forward only
            if (arr != null && arr.length == 3) // if block extended to whole code by pr on 6thfeb18
            {
                regNo = arr[0];
                formName = arr[1];
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside createBulkID func value of formName is " + formName);
                HashMap rowData = null;
                String[] bulk_id_arr = null;
                HashMap finalFieldErrorMap = new HashMap();// store the field name and error message for a particular bulk_id
                Boolean isValid = false;
                HashMap validateResult = new HashMap();
                String fname = "", lname = "", designation = "", department = "", state = "", mobile = "", dor = "",
                        uid = "", mail = "", dob = "", emp_code = "", valid = "", errorMsg = "";
                bulk_id_arr = bulk_id.split(",");
                // start, code added by pr on 29thmay18
                String uidExist = "";
                String id = "";
                // end, code added by pr on 29thmay18
                // start, code added by pr on 12thapr19 to update in zimbra
                URL url = null;
                String admin_token = "";
                Inform infObj = null;
                // check for zimbra token if it is got then proceed else stop here and give an error

                try {
                    url = new URL("https://100.80.17.250:7071/service/admin/soap/");
                    //admin_token = getAdminAuthToken("createuserapp@createaccount.da", "fgaT8.8A_bGm", url);
                    //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "A@#poi$^KJ39", url);
                    //admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "#ZyPE@8xR#H$TJxT9^H=CTZ@#43f#Dg4dV#Xz@", url);
                    admin_token = getAdminAuthToken("suppapp.admin.legacy@legacyaccount.admin", "Q=2E+D4Kf=BYi+K7De=4TjA+xFq6Z=T7+hDs=4x", url);

                } catch (Exception ex) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createBulkID function Exception in admin URL hit ::::::::::::::::::");
                } // code commented by pr on 9thaug19 for testing, to be uncommented in production

                //admin_token = "valid"; // above code commented this line added only for testing on 12thfeb2020
                ArrayList<String> zimData = new ArrayList<String>(); // this will contain the rows of data that were not created in the zimbra ldap and will be added in a file to be attaced in the mail to zim team
                // end, code added by pr on 12thapr19
                if (!admin_token.isEmpty()) // if around and else added by pr on 12thapr19
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createBulkID function  admin_token not empty  ");

                    // start, code added by pr on 17thjul19
                    // create a arraylist to store the valid ids created in zimbra for which aliases needs to be added 
                    ArrayList<HashMap> aliasToCreateArr = new ArrayList<HashMap>(); // it will store the information hashmaps. hashmap will contain mail, uid, mailequivalentaddress(if any)

                    HashMap<String, String> aliasToCreateHM = null;

                    // end, code added by pr on 17thjul19
                    // start, code added by pr on 18thjul19
                    String ldap_mailequivalentaddress = "";

                    Boolean zimValid = false;

                    String data = null;

                    String message = "";

                    // start, code added by pr on 8thaug19, LDAP connection object is formed once and passed in the createEmailId() function and is closed once after the loop is finished
                    DirContext ctx1 = null;

                    try {
//                        Hashtable ht = new Hashtable(4);
//                        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//
//                        ht.put(Context.PROVIDER_URL, providerurl);
//                        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//                        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//                        ht.put(Context.SECURITY_CREDENTIALS, createpass);

                        //ctx1 = new InitialDirContext(ht);
                        ctx1 = SingleApplicationContext.getLDAPInstance();// line modified by pr on 19thfeb2020

                        // end, code added by pr on 8thaug19                    
                        // end, code added by pr on 18thjul19                    
                        for (String bulk_id : bulk_id_arr) // iterate through each bulk id
                        {
                            // start, code added by pr on 18thjul19

                            ldap_mailequivalentaddress = "";

                            mail = "";

                            uid = "";

                            // end, code added by pr on 18thjul19
                            String[] idArr = bulk_id.split("~"); // line added by pr on 29thmay18
                            id = idArr[0];
                            uidExist = idArr[1];
                            isValid = true;
                            valid = "";
                            errorMsg = "";
                            // get the details for this bulk id
                            rowData = fetchBulkUserDetails(id);
                            if (rowData != null) {
                                // fetch all the values from haspmap from DB into the String variables
                                if (rowData.get("fname") != null) {
                                    fname = rowData.get("fname").toString();
                                    if (!fname.equals("")) // if added by pr on 26thmar18
                                    {
                                        fname = fname.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("lname") != null) {
                                    lname = rowData.get("lname").toString();
                                    if (!lname.equals("")) // if added by pr on 26thmar18
                                    {
                                        lname = lname.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("designation") != null) {
                                    designation = rowData.get("designation").toString();
                                    if (!designation.equals("")) // if added by pr on 26thmar18
                                    {
                                        designation = designation.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("department") != null) {
                                    department = rowData.get("department").toString();
                                    if (!department.equals("")) // if added by pr on 26thmar18
                                    {
                                        department = department.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("state") != null) {
                                    state = rowData.get("state").toString();
                                    if (!state.equals("")) // if added by pr on 26thmar18
                                    {
                                        state = state.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("mobile") != null) {
                                    mobile = rowData.get("mobile").toString();
                                    if (!mobile.equals("")) // if added by pr on 26thmar18
                                    {
                                        mobile = mobile.trim();
                                    }
                                    // start, code added by pr on 10thsep18 
                                    // if db has 10 digit mobile number without + sign then consider it as indian mobile and append +91 else use it as same
                                    if (!mobile.contains("+") && mobile.length() == 10) {
                                        mobile = "+91" + mobile;
                                    }
                                    // end, code added by pr on 10thsep18 
                                }
                                if (rowData.get("dor") != null) {
                                    dor = rowData.get("dor").toString();
                                    if (!dor.equals("")) // if added by pr on 26thmar18
                                    {
                                        dor = dor.trim();
                                    }
                                }
                                if (rowData.get("uid") != null) {
                                    uid = rowData.get("uid").toString();
                                    if (!uid.equals("")) // if added by pr on 26thmar18
                                    {
                                        uid = uid.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("mail") != null) {
                                    mail = rowData.get("mail").toString();
                                    if (!mail.equals("")) // if added by pr on 26thmar18
                                    {
                                        mail = mail.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                if (rowData.get("dob") != null) {
                                    dob = rowData.get("dob").toString();
                                    if (!dob.equals("")) // if added by pr on 26thmar18
                                    {
                                        dob = dob.trim();
                                    }
                                }
                                if (rowData.get("emp_code") != null) {
                                    emp_code = rowData.get("emp_code").toString();
                                    if (!emp_code.equals("")) // if added by pr on 26thmar18
                                    {
                                        emp_code = emp_code.trim().toLowerCase(); // line modified by pr on 11thapr18
                                    }
                                }
                                validation.BulkValidation validObj = new validation.BulkValidation();
                                // fname
                                validateResult = validObj.Fname(fname);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("fname", errorMsg);
                                    }
                                }
                                validateResult = validObj.Lname(lname);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("lname", errorMsg);
                                    }
                                }
                                validateResult = validObj.Designation(designation);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("designation", errorMsg);
                                    }
                                }
                                validateResult = validObj.Department(department);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("department", errorMsg);
                                    }
                                }
                                validateResult = validObj.State(state);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("state", errorMsg);
                                    }
                                }
                                //validateResult = validObj.Mobile(mobile);
                                validateResult = validObj.MobileAdmin(mobile); // line modified by pr on 10thsep18
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("mobile", errorMsg);
                                    }
                                }
                                validateResult = validObj.DOR(dor);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("dor", errorMsg);
                                    }
                                }
                                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " isValid after dor "+isValid);
                                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " bo value is "+bo+" domain is "+domain);
                                validateResult = validObj.Uid(uid, bo); // ctx1 added by pr on 8thaug19
                                Boolean checkdbbo = false;
                                String newmail = "";
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("uid", errorMsg);
                                    }
                                    if (validateResult.get("checkdbbo") != null) {
                                        if (validateResult.get("checkdbbo").toString().equals("true")) {
                                            checkdbbo = true;
                                        } else if (validateResult.get("checkdbbo").toString().equals("false")) {
                                            checkdbbo = false;
                                        }
                                    }
                                    if (validateResult.get("newmail") != null) {
                                        newmail = validateResult.get("newmail").toString();
                                    }
                                }

                                // start, code added by pr on 25thfeb2020
                                Set<String> domains = null;

                                HashMap<String, String> hm = null;

                                try {
                                    // below code modified by pr on 31stjul2020 to incorporate nkn bulk form
                                    EmailService emailservice = new EmailService();

                                    if (formName.equalsIgnoreCase(Constants.NKN_BULK_FORM_KEYWORD)) {
                                        domains = emailservice.getnknDomain();
                                        hm = fwdObj.fetchFormDetail(regNo, Constants.NKN_BULK_FORM_KEYWORD);

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "DOMAINS ALLOWED for regNo ::: " + regNo + " are " + domains);
                                    } else {
                                        hm = fwdObj.fetchFormDetail(regNo, Constants.BULK_FORM_KEYWORD);

                                        HashMap profile_values = new HashMap();

                                        //profile_values = (HashMap) session.get("profile-values");
                                        profile_values.put("form_type", formName);
                                        profile_values.put("emp_type", hm.get("emp_type"));
                                        profile_values.put("id_type", hm.get("id_type"));
                                        profile_values.put("user_employment", hm.get("employment"));
                                        profile_values.put("stateCode", hm.get("state"));
                                        profile_values.put("dept", hm.get("department"));
                                        profile_values.put("department", hm.get("department"));
                                        profile_values.put("Org", hm.get("organization"));
                                        profile_values.put("min", hm.get("min"));

                                        //profile_values.put("dept", hm.get("department"));
                                        System.out.println("profile_values::::::" + profile_values);

                                        domains = emailservice.getDomain(profile_values);
                                        //System.out.println("DOMAINS ALLOWED ::: " + domains);

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "DOMAINS ALLOWED for regNo ::: " + regNo + " are " + domains);

                                    }

                                } catch (Exception e) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside new code for domain calculation for regno " + regNo + " " + e.toString());
                                }

                                // end, code added by pr on 25thfeb2020
                                //validateResult = validObj.Mail(mail,uid, new HashSet<>(Arrays.asList("nic.in"))); // ctx1 added by pr on 8thaug19
                                validateResult = validObj.MailAdmin(mail, uid, domains); // ctx1 added by pr on 8thaug19

                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }

                                    // start, code added by pr on 7thjan2020
                                    String[] mailArr = mail.split("@");

                                    if (mailArr != null && mailArr[1] != null && !mailArr[1].trim().equals(domain)) {
                                        valid = "false";
                                        errorMsg += "Domain Mismatch.";
                                    }

                                    // end, code added by pr on 7thjan2020                                    
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("mail", errorMsg);
                                    }
                                }
                                validateResult = validObj.DOB(dob);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    if (valid.equals("false")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("dob", errorMsg);
                                    }
                                }
                                validateResult = validObj.EMPNUMBER(emp_code);
                                if (validateResult != null) {
                                    if (validateResult.get("valid") != null) {
                                        valid = validateResult.get("valid").toString();
                                    }
                                    if (validateResult.get("errorMsg") != null) {
                                        errorMsg = validateResult.get("errorMsg").toString();
                                    }
                                    //if( valid.equals("false") )
                                    if (valid.equals("false") && !errorMsg.equals("")) {
                                        isValid = false;
                                        // for invalid record add entry in the hashmap corresponding to the field name
                                        finalFieldErrorMap.put("emp_code", errorMsg);
                                    }
                                }
                                // check if uid exists for this mobile or not if yes then check for name based designation based from the base table
                                //if  designation based ignore this check 
                                // else check for the table allow_creation column if it is empty then update it with the value n and the remarks would be added in reject_remarks column
                                // if it contains y then isValid would be true if it contains n then isvalid is false and that id would not be created
                                // start, code added by pr on 30thmay18
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " bulk_id " + bulk_id);
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reg_no " + rowData.get("reg_no").toString());
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " uidExist " + uidExist);
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " allow_creation " + rowData.get("allow_creation").toString());
                                //Boolean isAllowCreation = allowMobileDuplicateCreation(rowData.get("reg_no").toString(), uidExist, rowData.get("allow_creation").toString());
                                Boolean isAllowCreation = allowMobileDuplicateCreation(rowData.get("reg_no").toString(), mobile, rowData.get("allow_creation").toString()); // line modified by pr on 12thmay2020
                                if (!isAllowCreation) {
                                    isValid = false;
                                    //finalFieldErrorMap.put("mobile_duplicate", "This Mobile is already linked with an account");
                                    //finalFieldErrorMap.put("mobile_duplicate", "This Mobile is already linked with accounts. With \"-admin\" uid , max 3 accounts allowed.For others max 2 accounts allowed.");
                                    // above line modified by pr on 20thnov18
                                    finalFieldErrorMap.put("mobile_duplicate", "This Mobile is already linked with accounts. With \"-admin\" uid , max 4 accounts allowed.For others max 3 accounts allowed.");
                                }
                                // do the validations for all other fields
                                msg = "";
                                //Connection con = null;
                                try {
                                    if (isValid) // if all is well then create the id and update in the bulk_users table
                                    {
                                        // update the following fields is_created, is_rejected, reject_remarks, uid_assigned, 
                                        //mail_assigned, updatedon
                                        String pass = fetchRandomPassword();
                                        // start, code added by pr on 13thmar18
                                        // get field value of type whether mail or app
                                        Boolean createAppID = false;

                                        //HashMap<String, String> hm = fwdObj.fetchFormDetail(regNo, Constants.BULK_FORM_KEYWORD); // line commented by pr on 25thfeb2020
                                        String type = "";
                                        // hm not null check added by pr on 31stjul2020
                                        if (hm != null && hm.get("type") != null) {
                                            type = hm.get("type").toString().trim();
                                        }
                                        //if (type.equalsIgnoreCase("app")) {
                                        if (type.equalsIgnoreCase("app") || type.equalsIgnoreCase("eoffice")) { // line modified by pr on 14thsep18
                                            createAppID = true;
                                        }
                                        PreparedStatement ps = null;
                                        // end, code added by pr on 13thmar18
                                        if (!createAppID) {
                                            //if(createEmailId("bulk",  po,  bo,  domain, uid,  regNo, pass, id, mail)), ctx1 added by pr on 8thaug19
                                            if (createEmailId(ctx1, formName, po, bo, domain, uid, "", regNo, pass, id, mail)) // line mdofified by pr on 27thdec17                   
                                            {
                                                // start, code added by pr on 16thapr19, code taken up from after query updation by pr on 16thapr19

                                                try {

                                                    // create the id in zimbra ldap id 
                                                    //HashMap<String, Object> zimHM = createZimEmailId(formName, po, bo, domain, uid, "", regNo, pass, id, mail, admin_token, url); 
                                                    HashMap<String, Object> zimHM = createZimEmailId(formName, po, bo, domain, uid, mail, regNo, pass, id, mail, admin_token, url);
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                                            + " inside bulk create id if after create zim id zimHM hashmap got from zim create function call is " + zimHM);
//                                                Boolean zimValid = false;
//                                                String data = null;
//                                                String message = ""; // commented by pr on 18thjul19

                                                    if (zimHM != null && zimHM.size() > 0) {

                                                        if (zimHM.get("valid") != null) {
                                                            zimValid = (Boolean) zimHM.get("valid");
                                                        }
                                                        if (zimHM.get("data") != null) {
                                                            data = zimHM.get("data").toString();
                                                        }
                                                        if (zimHM.get("msg") != null) {
                                                            message = zimHM.get("msg").toString();
                                                        }

                                                    }

                                                    if (!zimValid) // if id gets created in zimbra too
                                                    {
                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                                                + " inside zimvalid not valid after calling zim create function ");
                                                        // send mail to zimbra team with the data
                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside zimbra error in forwardaction data is " + data + " zimValid is " + zimValid + " message is " + message);
                                                        zimData.add(data);
                                                    } else // else added by pr on 17thjul19, if id has been created then save the info in the hashmap to be used afterwards
                                                    {
                                                        aliasToCreateHM = new HashMap<String, String>();

                                                        aliasToCreateHM.put("mail", mail);

                                                        aliasToCreateHM.put("uid", uid);

                                                        aliasToCreateHM.put("data", data);

                                                        aliasToCreateArr.add(aliasToCreateHM);
                                                    }
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                                            + "  zimData value is  " + zimData);
                                                    // end , code added by pr on 16thapr19                                          

                                                } catch (Exception e) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createBulkID function calling createZimEmailId function EXCEPTION   ");
                                                }

                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createBulkID function  create email id  ");
                                                String is_created = "y";
                                                String is_rejected = "n";
                                                String reject_remarks = "";
                                                String uid_assigned = uid;
                                                String mail_assigned = mail;
                                                try {
                                                    /*Class.forName("com.mysql.jdbc.Driver");
                                             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                                                    // below code added by pr on 19thdec17
                                                    con = DbConnection.getConnection();
                                                    String qry = "UPDATE bulk_users SET is_created = ?, is_rejected = ?, reject_remarks = ? ,"
                                                            + ""
                                                            + " uid_assigned = ?, mail_assigned = ?, updatedon = current_timestamp  "
                                                            + " "
                                                            + ""
                                                            + "WHERE bulk_id = ?";
                                                    ps = con.prepareStatement(qry);
                                                    ps.setString(1, is_created);
                                                    ps.setString(2, is_rejected);
                                                    ps.setString(3, reject_remarks);
                                                    ps.setString(4, uid_assigned);
                                                    ps.setString(5, mail_assigned);
                                                    ps.setString(6, id);

                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users query is" + ps);
                                                    int k = ps.executeUpdate(); // LHS added by pr on 12thapr18
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users result = " + k);
                                                    if (k > 0) // if added by pr on 12thapr18
                                                    {
                                                        isRemaining = checkBulkAndUpdateStatus(regNo, formName); // line added by pr on 14thdec17. LHS added by pr on 26thdec18, line taken inside k > 0 by pr on 16thapr19

                                                        // start, code added by pr on 24thdec18 for bulk creation
                                                        if (reject_remarks.equals("")) {
                                                            reject_remarks = "none";
                                                        }
                                                        bulkUpdateData.put(id, reject_remarks + "~n"); // value => reject remarks none or value ~ show checkbox or not
                                                        // end, code added by pr on 24thdec18 for bulk creation
                                                        isSuccess = true;
                                                        isError = false;
                                                        msg = "Application Processed Successfully with Some or All Email ID Creations !";
                                                    } else {
                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users result not updated, coming to else ");

                                                        isSuccess = false;
                                                        isError = true;
                                                        msg = "Application couldnot be Processed !";
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 3" + e.getMessage());
                                                } finally {
                                                    if (ps != null) {
                                                        try {
                                                            ps.close();
                                                        } catch (Exception e) {
                                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 146" + e.getMessage());
                                                        }
                                                    }
                                                }
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                                        + " inside bulk create id if before create zim id ");

                                            }
                                        } else // ctx1 object passed by pr on 8thaug19
                                        {
                                            if (createAppId(ctx1, formName, po, bo, domain, uid, "", regNo, pass, id, mail)) // two parameters added after uid_prefix by pr on 19thfeb18
                                            {
                                                String is_created = "y";
                                                String is_rejected = "n";
                                                String reject_remarks = "";
                                                String uid_assigned = uid;
                                                String mail_assigned = mail;
                                                try {
                                                    /*Class.forName("com.mysql.jdbc.Driver");
                                             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                                                    // below code added by pr on 19thdec17
                                                    con = DbConnection.getConnection();
                                                    String qry = "UPDATE bulk_users SET is_created = ?, is_rejected = ?, reject_remarks = ? ,"
                                                            + ""
                                                            + " uid_assigned = ?, mail_assigned = ?, updatedon = current_timestamp  "
                                                            + " "
                                                            + ""
                                                            + "WHERE bulk_id = ?";
                                                    ps = con.prepareStatement(qry);
                                                    ps.setString(1, is_created);
                                                    ps.setString(2, is_rejected);
                                                    ps.setString(3, reject_remarks);
                                                    ps.setString(4, uid_assigned);
                                                    ps.setString(5, mail_assigned);
                                                    ps.setString(6, id);
                                                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " insinde create id true in bulk query is " + ps);
                                                    int k = ps.executeUpdate(); // LHS added by pr on 12thapr18
                                                    isRemaining = checkBulkAndUpdateStatus(regNo, formName); // line added by pr on 14thdec17, LHS added by pr on 26thdec18
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users result = " + k);
                                                    if (k > 0) // if added by pr on 12thapr18
                                                    {
                                                        // start, code added by pr on 24thdec18 for bulk creation
                                                        if (reject_remarks.equals("")) {
                                                            reject_remarks = "none";
                                                        }
                                                        bulkUpdateData.put(id, reject_remarks + "~n"); // value => reject remarks none or value ~ show checkbox or not
                                                        // end, code added by pr on 24thdec18 for bulk creation
                                                        isSuccess = true;
                                                        isError = false;
                                                        msg = "Application Processed Successfully with Some or All App ID Creations !";
                                                    } else {
                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users result not updated, coming to else ");

                                                        isSuccess = false;
                                                        isError = true;
                                                        msg = "Application couldnot be Processed !";
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 4" + e.getMessage());
                                                } finally {
                                                    if (ps != null) {
                                                        try {
                                                            ps.close();
                                                        } catch (Exception e) {
                                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 147" + e.getMessage());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        // update bulk_registration for description field
                                    } else // update the bulk_users table for all the error messages 
                                    {
                                        // update the following fields is_created, is_rejected, reject_remarks,updatedon
                                        String str = "";
                                        String allow_creation = ""; // line added by pr on 29thmay18
                                        int i = 1;
                                        for (Object key : finalFieldErrorMap.keySet()) {
                                            if (i == 1) {
                                                str += finalFieldErrorMap.get(key);
                                            } else {
                                                str += "," + finalFieldErrorMap.get(key);
                                            }
                                            // below if added by pr on 29thmay18
                                            if (key.equals("mobile_duplicate")) {
                                                allow_creation = "n";
                                            }
                                        }
                                        String is_created = "n";
                                        //String is_rejected = "y";
                                        String is_rejected = "n"; // line modified by pr on 4thapr18 so that the record could be modified if it can be by the Admin by uploading the file and rejected only by MANUAL intervention
                                        String reject_remarks = str;
                                        PreparedStatement ps = null;
                                        try {
                                            /*Class.forName("com.mysql.jdbc.Driver");
                                     Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                                            // below code added by pr on 19thdec17
                                            con = DbConnection.getConnection();
                                            String qry = "UPDATE bulk_users SET is_created = ?, is_rejected = ?, reject_remarks = ? ,"
                                                    + ""
                                                    + " updatedon = current_timestamp  ";
                                            if (!allow_creation.equals("")) // if added by pr on 29thmay18
                                            {
                                                qry += " , allow_creation = 'n' ";
                                            }
                                            qry += "WHERE bulk_id = ?";
                                            ps = con.prepareStatement(qry);
                                            ps.setString(1, is_created);
                                            ps.setString(2, is_rejected);
                                            ps.setString(3, reject_remarks);
                                            ps.setString(4, id);
                                            int k = ps.executeUpdate();
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users result = " + k);

                                            isRemaining = checkBulkAndUpdateStatus(regNo, formName); // line added by pr on 14thdec17 to be added after above query execution modified on 26thdec17, LHS added by pr on 26thdec18
                                            if (k > 0) // if added by pr on 12thapr18
                                            {
                                                // start, code added by pr on 24thdec18 for bulk creation
                                                if (reject_remarks.equals("")) {
                                                    reject_remarks = "none";
                                                }
                                                bulkUpdateData.put(id, reject_remarks + "~y"); // value => reject remarks none or value ~ show checkbox or not
                                                // end, code added by pr on 24thdec18 for bulk creation
                                                isSuccess = true;
                                                isError = false;
                                                msg = "Application Processed Successfully with some Errors !";
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set update bulk users result not updated, coming to else ");

                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application couldnot be Processed !";
                                            }
                                        } catch (Exception e) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 5" + e.getMessage());
                                        } finally {
                                            if (ps != null) {
                                                try {
                                                    ps.close();
                                                } catch (Exception e) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 148" + e.getMessage());
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function 5" + e.getMessage());
                                } finally {
                                    if (con != null) {
                                        try {
                                            // con.close();
                                        } catch (Exception e) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 149" + e.getMessage());
                                        }
                                    }
                                }

                                isRemaining = checkBulkAndUpdateStatus(regNo, formName); // line added by pr on 18thapr19, to resolve the issue of request not getting completed ( after zimbra integration )
                                updateDescription(description, regNo); // line added by pr on 14thmar18
                            }
                        } // for loop ends here 

                        // start, code added by pr on 17thjul19
                        // iterate through aliasToCreateArr arraylist 
                        for (HashMap emailWOAlias : aliasToCreateArr) // hashmap 
                        {
                            // call the function to create aliases for these already created emails

                            if (emailWOAlias != null && emailWOAlias.size() > 0) {
                                if (emailWOAlias.get("mail") != null) {
                                    mail = emailWOAlias.get("mail").toString();
                                }

                                if (emailWOAlias.get("uid") != null) {
                                    uid = emailWOAlias.get("uid").toString();
                                }

                                if (emailWOAlias.get("data") != null) {
                                    data = emailWOAlias.get("data").toString();
                                }

                                // call the function to create the alias
                                HashMap hmAlias = createZimEmailAlias(uid, mail, admin_token, url, data);

                                if (hmAlias != null && hmAlias.size() > 0) {
                                    if (hmAlias.get("valid") != null) {
                                        zimValid = (Boolean) hmAlias.get("valid");
                                    }
                                    if (hmAlias.get("data") != null) {
                                        data = hmAlias.get("data").toString();
                                    }
                                    if (hmAlias.get("msg") != null) {
                                        message = hmAlias.get("msg").toString();
                                    }
                                }

                                if (!zimValid) // if id gets created in zimbra too
                                {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                            + " inside zimvalid not valid after calling zim create function ");
                                    // send mail to zimbra team with the data
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside zimbra error in forwardaction data is " + data + " zimValid is " + zimValid + " message is " + message);
                                    zimData.add(data);
                                }

                            }

                        }

                        // end, code added by pr on 17thjul19                    
                        if (zimData != null && zimData.size() > 0)// zimData contains all the info that didnt get created in the zimbra ldap, if block added by pr on 12thapr19
                        {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == inside zimdata not null and size greater than 0 zimData values are " + zimData);
                            // create a text file with all the data in arraylist and send a mail    
                            //String fileName = "F://zimtext.txt"; // done for testing
                            String fileName = "/tmp/zimtext.txt"; // to be uncommented in production
                            File file = new File(fileName);
                            try {
                                //Create the file
                                if (file.createNewFile()) {
                                    //System.out.println("File is created!");
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  create id bulk zim error file created  ");
                                } else {
                                    //System.out.println("File already exists.");
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  create id bulk zim error file already exists ");
                                }
                                //Write Content
                                FileWriter writer = new FileWriter(file);
                                String textToWrite = "";
                                for (String str : zimData) {
                                    writer.write("\n" + str);
                                }
                                writer.close();
                            } catch (Exception e) {
                                //System.out.println(" inside catch exception is " + e.getMessage());
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==  create id bulk zim error file inside catch exception is " + e.getMessage());
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 150" + e.getMessage());
                            }
                            // create a fileinputstream object
                            /*InputStream fileInputStream = null;
                        try {
                            fileInputStream = new NewFileInputStream(file);
                            if (file.exists()) {
                                System.out.println(" inside file exists ");
                            } else {
                                System.out.println(" inside file doesnt exists ");
                            }
                        } catch (Exception exp) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ==   inside input stream exception in create zim  " + exp.getMessage());
                        }*/
                            if (infObj == null) // if around added by pr on 4thapr19
                            {
                                infObj = new Inform();
                            }
                            infObj.sendZimBulkMail(po, bo, fileName);
                        }

                    } // try catch added by pr on 8thaug19
                    catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createBulkID function create LDAP Connection exception " + e.getMessage());
                    } finally // finally added by pr on 8thaug19
                    {
//                        if (ctx1 != null) {
//
//                            try {
//
//                                ctx1.close();
//                            } catch (Exception e) {
//                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " ctx1 close exception in createid() " + e.getMessage());
//                            }
//                        }

                    }

                }// check for admin_token not empty
                else {
                    isSuccess = false;
                    isError = true;
                    msg = "Zimbra LDAP Token is empty.";
                }
            }
        } else // else added by pr on 3rdjan18
        {
            random = ""; // line added by pr on 6thfeb18
            isSuccess = false;
            isError = true;
            msg = "Application could not be Processed !";
        }
        // start, code added by pr on 8thfeb18 for xss
        if (po != null && !po.equals("")) {
            po = ESAPI.encoder().encodeForHTMLAttribute(po);
        }
        if (bo != null && !bo.equals("")) {
            bo = ESAPI.encoder().encodeForHTMLAttribute(bo);
        }
        if (domain != null && !domain.equals("")) {
            domain = ESAPI.encoder().encodeForHTMLAttribute(domain);
        }
        if (final_sms_id != null && !final_sms_id.equals("")) {
            final_sms_id = ESAPI.encoder().encodeForHTMLAttribute(final_sms_id);
        }
        if (regNo != null && !regNo.equals("")) {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        if (statRemarks != null && !statRemarks.equals("")) {
            statRemarks = ESAPI.encoder().encodeForHTMLAttribute(statRemarks);
        }
        if (random != null && !random.equals("")) {
            random = ESAPI.encoder().encodeForHTMLAttribute(random);
        }
        // end, code added by pr on 8thfeb18 for xss          
        // start, code added by pr on 26thdec18
        if (isRemaining) {
            toggleBulk = false;
        } else {
            toggleBulk = true;
        }
        // end, code added by pr on 26thdec18        
        return SUCCESS;
    }

    public ArrayList fetchBulkUsersAccToType(String regNo, String type) {
        // start, code added by pr on 20thnov18
        if (type.toLowerCase().contains("success")) {
            type = "success";
        } else if (type.toLowerCase().contains("error")) {
            type = "error";
        }
        // end, code added by pr on 20thnov18
        ArrayList arr = new ArrayList();
        if (type.equals("success")) {
            // fetch all teh records for thsi regno which have is_created y
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
//                con = DbConnection.getConnection();
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                String qry = " SELECT bulk_id,fname,lname,department,mail,mobile,state,designation,dob,dor,uid,emp_code,updatedon,uid_assigned,mail_assigned FROM bulk_users WHERE registration_no = ? AND is_created = 'y' AND is_rejected = 'n' "
                        + ""
                        + "AND  uid_assigned is not null AND mail_assigned is not null ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNo);
                rs = ps.executeQuery();
                HashMap hm = null;
                while (rs.next()) {
                    hm = new LinkedHashMap<String, String>();
                    hm.put("bulk_id", rs.getString("bulk_id"));
                    hm.put("fname", rs.getString("fname"));
                    hm.put("lname", rs.getString("lname"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("department", rs.getString("department"));
                    hm.put("state", rs.getString("state"));
                    hm.put("mobile", rs.getString("mobile"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("uid", rs.getString("uid"));
                    hm.put("mail", rs.getString("mail"));
                    hm.put("dob", rs.getString("dob"));
                    hm.put("emp_code", rs.getString("emp_code"));
                    hm.put("updatedon", rs.getString("updatedon"));
                    hm.put("uid_assigned", rs.getString("uid_assigned"));
                    hm.put("mail_assigned", rs.getString("mail_assigned"));
                    arr.add(hm);
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchBulkUsersAccToType func  qry is " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 176" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 177" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 178" + e.getMessage());
                    }
                }
            }
        } else if (type.equals("error")) {
            // fetch all teh records for thsi regno which have is_created n and reject_remarks not empty
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
//                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                /*String qry = " SELECT * FROM bulk_users WHERE registration_no = ? AND is_created = 'n'  AND is_rejected = 'y'  AND reject_remarks is not null  "
                 + ""
                 + " AND reject_remarks != '' "; // query modified by pr on 13thdec17*/
                String qry = " SELECT bulk_id,fname,lname,department,mail,mobile,state,designation,dob,dor,uid,emp_code,updatedon,uid_assigned,mail_assigned,reject_remarks FROM bulk_users WHERE registration_no = ? AND is_created = 'n'  AND ( is_rejected = 'y'  OR  ( reject_remarks is not null  "
                        + ""
                        + " AND reject_remarks != '' ) ) "; // query modified by pr on 4thapr18 so that  , reject remarks are added by the system after validation and is_rejected y after MANUAL intervention
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNo);
                rs = ps.executeQuery();
                HashMap hm = null;
                while (rs.next()) {
                    hm = new LinkedHashMap<String, String>();
                    hm.put("bulk_id", rs.getString("bulk_id"));
                    hm.put("fname", rs.getString("fname"));
                    hm.put("lname", rs.getString("lname"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("department", rs.getString("department"));
                    hm.put("state", rs.getString("state"));
                    hm.put("mobile", rs.getString("mobile"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("uid", rs.getString("uid"));
                    hm.put("mail", rs.getString("mail"));
                    hm.put("dob", rs.getString("dob"));
                    hm.put("emp_code", rs.getString("emp_code"));
                    hm.put("updatedon", rs.getString("updatedon"));
                    hm.put("uid_assigned", rs.getString("uid_assigned"));
                    hm.put("mail_assigned", rs.getString("mail_assigned"));
                    hm.put("reject_remarks", rs.getString("reject_remarks"));// line added by pr on 12thdec17
                    arr.add(hm);
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set success function bulk_users " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 179" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 180" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 181" + e.getMessage());
                    }
                }
            }
        } else if (type.equals("all")) // else if added by pr on 2ndapr18
        {
            // fetch all teh records for thsi regno which have is_created n and reject_remarks not empty
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Connection con = null;
            try {
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = " SELECT bulk_id,fname,lname,department,mail,mobile,state,designation,dob,dor,uid,emp_code,updatedon,uid_assigned,mail_assigned,reject_remarks FROM bulk_users WHERE registration_no = ? AND is_created = 'n'  AND is_rejected = 'n'  ORDER BY createdon DESC";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNo);
                rs = ps.executeQuery();
                HashMap hm = null;
                while (rs.next()) {
                    hm = new LinkedHashMap<String, String>();
                    hm.put("bulk_id", rs.getString("bulk_id"));
                    hm.put("fname", rs.getString("fname"));
                    hm.put("lname", rs.getString("lname"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("department", rs.getString("department"));
                    hm.put("state", rs.getString("state"));
                    hm.put("mobile", rs.getString("mobile"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("uid", rs.getString("uid"));
                    hm.put("mail", rs.getString("mail"));
                    hm.put("dob", rs.getString("dob"));
                    hm.put("emp_code", rs.getString("emp_code"));
                    hm.put("updatedon", rs.getString("updatedon"));
                    hm.put("uid_assigned", rs.getString("uid_assigned"));
                    hm.put("mail_assigned", rs.getString("mail_assigned"));
                    hm.put("reject_remarks", rs.getString("reject_remarks"));// line added by pr on 12thdec17
                    arr.add(hm);
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set success function bulk_users " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 182" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 183" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 184" + e.getMessage());
                    }
                }
            }
        }
        return arr;
    }

    // start, functions modified by pr on 14thdec17
    public void exportExcel() throws Exception {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportExcel function regNo " + regNo + " type  " + type);
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside  exportExcel() func  ");
        System.out.println(" before getbyulkusersacctotype func call regno is " + regNo + " type value is " + type);
        ArrayList arr = fetchBulkUsersAccToType(regNo, type);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " arr size is " + arr.size());
        if (arr.size() > 0 && arr.get(0) != null) {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside array size greater than 0 ");
            System.out.println(" inside arr size not null and greater than 0 ");
            ExcelCreator excelCreator = new ExcelCreator();
            String excelFileName = "";
            String sheetName = "";//name of sheet
            if (type.equals("success")) {
                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside type success ");
                //excelFileName = "F:\\success.xls";//name of excel file
                excelFileName = "/tmp/success.xls";//name of excel file // to be uncommented in production
                sheetName = "Successfully Created IDS";//name of sheet
            } else if (type.equals("all")) // else if added by pr on 2ndapr18
            {
                excelFileName = "/tmp/all.xls";//name of excel file // to be uncommented in production
                //excelFileName = "F:\\all.xls";//name of excel file 
                sheetName = regNo;//name of sheet
            } else if (type.equals("success_mail")) // else if added by pr on 20thnov18 to have a name to be deleted after sending mail 
            {
                System.out.println(" inside success_mail block in exportExcel function ");
                excelFileName = "/tmp/" + regNo + "_success.xls";
                sheetName = regNo;//name of sheet
            } else if (type.equals("error_mail")) // else if added by pr on 20thnov18 to have a name to be deleted after sending mail
            {
                System.out.println(" inside error_mail block in exportExcel function ");
                excelFileName = "/tmp/" + regNo + "_error.xls";
                sheetName = regNo;//name of sheet
            } else {
                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside type error ");
                //excelFileName = "F:\\error.xls";//name of excel file 
                excelFileName = "/tmp/error.xls";//name of excel file // to be uncommented in production
                sheetName = "IDS not Created";//name of sheet
            }
            excelCreator.createWorkbookHash(arr, excelFileName, sheetName);
        }
    }

    // functions modified by pr on 14thdec17
    public String export() throws Exception // mapped in struts.xml
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside export function regno is " + regNo + " type is " + type);
        exportExcel();
        String filename = "/tmp/error.xls"; // to be uncommented in production
        //String filename = "F:\\error.xls"; // to test on local windows machine
        if (type.equals("success")) {
            filename = "/tmp/success.xls";// to be uncommented in production
            //filename = "F:\\success.xls"; // to test on local windows machine
        } else if (type.equals("all")) // else if added by pr on 2ndapr18
        {
            filename = "/tmp/all.xls";// to be uncommented in production
            //filename = "F:\\all.xls"; // to test on local windows machine
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " filename is " + filename);
        File file = new File(filename);
        if (file.exists()) {
            fileInputStream = new NewFileInputStream(file);
//            if (file.exists()) {
//            } else {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside file doesnt exists ");
//            }
        } else {
            filename = "/tmp/norecord.xls";// to be uncommented in production
            //filename = "F:\\norecord.xls"; //for local path
            ExcelCreator excelCreator = new ExcelCreator();
            ArrayList arr = new ArrayList();
            HashMap hm = new HashMap();
            hm.put("No Record Found", "");
            arr.add(hm);
            excelCreator.createWorkbookHash(arr, filename, "No Record");
            file = new File(filename);
            try {
                fileInputStream = new NewFileInputStream(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                return "filenotfound";
            }
        }
        return SUCCESS;
    }

    // functions added by pr on 28thjan2020
    public String exportIds() throws Exception // mapped in struts.xml
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportIds ");
        //exportExcel();

        // start
        ArrayList arr = fetchAllIdsCreated();

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportIds func arr size is " + arr.size());

        String filename = "/tmp/coordinator.xls", sheetName = "";

        if (arr.size() == 0 || arr.get(0) == null) {

            HashMap hm = new HashMap();
            hm.put("No Record Found", "");

            arr.add(hm);

            sheetName = "No Record";//name of sheet            
        } else {
            sheetName = "Successfully Created IDS";//name of sheet
        }

        ExcelCreator excelCreator = new ExcelCreator();

        excelCreator.createWorkbookHash(arr, filename, sheetName);

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " filename is " + filename + " sheetName " + sheetName + " arr " + arr);
        File file = new File(filename);
        if (file.exists()) {
            fileInputStream = new NewFileInputStream(file);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportIds function file Exists ");
//            if (file.exists()) {
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportIds function file Exists ");
//
//            } else {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportIds function file doesnt exists ");
//            }
        } else {
            return "filenotfound";
        }
        return SUCCESS;
    }

    // below function added by pr on 28thjan2020
    public ArrayList fetchAllIdsCreated() {

        ArrayList arr = new ArrayList();

        PreparedStatement ps = null;
        ResultSet rs = null;

        String email = fetchSessionEmail(); // id of the coordinator whose ids created till date

        ArrayList<String> bulkRegs = new ArrayList();

        try {
//            con = DbConnection.getConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String qry = " SELECT registration_no, stat_final_id, form_name, to_datetime FROM final_audit_track WHERE status = 'completed' and coordinator_email = ? "
                    + ""
                    + "and form_name in ('" + Constants.SINGLE_FORM_KEYWORD + "', '" + Constants.BULK_FORM_KEYWORD + "', '" + Constants.NKN_SINGLE_FORM_KEYWORD + "', '" + Constants.NKN_BULK_FORM_KEYWORD + "', '" + Constants.GEM_FORM_KEYWORD + "')";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, email);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchAllIdsCreated func  qry is " + ps);

            rs = ps.executeQuery();
            HashMap hm = null;
            while (rs.next()) {
                hm = new LinkedHashMap<String, String>();

                if (rs.getString("stat_final_id") != null && !rs.getString("stat_final_id").equals("")) {
                    hm.put("Reg No.", rs.getString("registration_no"));
                    hm.put("Email Created", rs.getString("stat_final_id"));
                    hm.put("Date", rs.getString("to_datetime"));

                    arr.add(hm);
                } else if (rs.getString("form_name").toLowerCase().contains("bulk")) // in case of bulk forms we will have to consult bulk_users table
                {
                    bulkRegs.add("'" + rs.getString("registration_no") + "'");
                }
            }

            // fetch the details from bulk_users table
            qry = "SELECT mail_assigned, registration_no, updatedon FROM bulk_users WHERE is_created = 'y' and registration_no in ";

            qry += "(" + String.join(",", bulkRegs) + ")";

            ps = conSlave.prepareStatement(qry);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchAllIdsCreated func  second qry is " + ps);

            rs = ps.executeQuery();
            hm = null;
            while (rs.next()) {
                hm = new LinkedHashMap<String, String>();

                if (rs.getString("mail_assigned") != null && !rs.getString("mail_assigned").equals("")) {
                    hm.put("Reg No.", rs.getString("registration_no"));
                    hm.put("Email Created", rs.getString("mail_assigned"));
                    hm.put("Date", rs.getString("updatedon"));
                    arr.add(hm);
                }

            }

            // check before sorting
            for (Object obj : arr) {
                HashMap hmEach = (HashMap) obj;

                System.out.println(" after sorting datetime value is " + hmEach.get("Date"));
            }

            // sort the arraylist with dates, it will compare in desc order                
            Collections.sort(arr, new Comparator<HashMap>() {

                public int compare(HashMap hm1, HashMap hm2) {
                    int flag = +1;

                    if (hm1 != null && hm2 != null && hm1.get("Date") != null && hm2.get("Date") != null) {
                        String dt1 = hm1.get("Date").toString();

                        String dt2 = hm2.get("Date").toString();

                        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

                        try {

                            Date dt1Obj = format.parse(dt1);// convert a date string into a Date object

                            Date dt2Obj = format.parse(dt2);// convert a date string into a Date object

                            flag = -dt1Obj.compareTo(dt2Obj);  // so that compareto method can be used which works on String, Date etc classes              

                        } catch (Exception e) {
                            System.out.println(" inside exception " + e.toString());
                        }

                        System.out.println(" flag to be returned from compare method flag " + flag);

                    }

                    return flag;

                }

            });

            // check after sorting
            for (Object obj : arr) {
                HashMap hmEach = (HashMap) obj;

                System.out.println(" after sorting datetime value is " + hmEach.get("Date"));
            }

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchAllIdsCreated func  qry is " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 176" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 177" + e.getMessage());
                }
            }
        }

        return arr;
    }

    // below function added by pr on 2nddec19
    public void finalCompleteMailFiles(String regNo, HashMap formDetail, String whichform) {
        System.out.println(" inside finalCompleteMailFiles regNo is " + regNo + " formDetail value is " + formDetail);

        String pdf_path = "", rename_sign_cert = "", ca_rename_sign_cert = "";

        try {

            if (formDetail != null) {
                if (formDetail.get("pdf_path") != null) {
                    pdf_path = formDetail.get("pdf_path").toString();
                }

                if (formDetail.get("rename_sign_cert") != null) {
                    rename_sign_cert = formDetail.get("rename_sign_cert").toString(); // in case of manual process contains the sealed and scanned file uploaded by user
                }

                if (formDetail.get("ca_rename_sign_cert") != null) {
                    ca_rename_sign_cert = formDetail.get("ca_rename_sign_cert").toString(); // in case of manual process contains the sealed and scanned file uploaded by ca
                }

            }

            if (!pdf_path.equals("") && !pdf_path.toLowerCase().contains("online")) // only cases of manual and esign comes here
            {
                if (pdf_path.toLowerCase().contains("esigned")) // its a case of esign
                {
                    rename_sign_cert = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + "eSigned_user_" + regNo + ".pdf";

                    ca_rename_sign_cert = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + "eSigned_ca_" + regNo + ".pdf";
                }
            }

            ArrayList<String> srcs = new ArrayList<String>();

            if (!rename_sign_cert.equals("")) {
                if (!srcs.contains(rename_sign_cert)) {
                    srcs.add(rename_sign_cert);
                }
            }

            if (!ca_rename_sign_cert.equals("")) {
                if (!srcs.contains(ca_rename_sign_cert)) {
                    srcs.add(ca_rename_sign_cert);
                }
            }

            UserTrack usObj = new UserTrack(sessionMap);// sessionMap passed as parameter by pr on 3rddec19

            usObj.setData(regNo);

            usObj.genPDF();

            usObj.setWhichform(whichform);

            String fil = ServletActionContext.getServletContext().getInitParameter("rawPdfLocation") + regNo + ".pdf";
            //String fil = "C:/Users/NIC/Desktop/Eforms/Archive/IMAPPOP-FORM202207120017(1).pdf";//For Testing On Local 
            if (!srcs.contains(fil)) {
                srcs.add(fil);
            }

            // get the multiple uploaded files by all in doc_upload
            PreparedStatement ps = null;

            ResultSet rs = null;

            String email = "";

            try {

                conSlave = DbConnection.getSlaveConnection();
                String qry = " SELECT doc_path, registration_no FROM doc_upload WHERE registration_no = ? ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNo);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " finalCompleteMailFiles query is " + ps);

                rs = ps.executeQuery();

                while (rs.next()) {

                    fil = rs.getString("doc_path");

                    if (fil != null && !fil.equals("")) {
                        if (!srcs.contains(fil)) {
                            srcs.add(fil);
                        }
                    }
                }

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside finalCompleteMailFiles function msg is ");

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  finalCompleteMailFiles catch " + e.toString());
                //e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                                + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside finalCompleteMailFiles EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                                + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside finalCompleteMailFiles EXCEPTION 2 " + e.getMessage());

                    }
                }

            }

            //String zipFile = "/eForms/archive.zip";//Not In Use
            //String zipFile = "C:/Users/NIC/Desktop/Eforms/Archive/"+ regNo + ".zip";// For Testing On Local 
            String zipFile = "/eForms/archive/" + regNo + ".zip";
            System.out.println("Zipfile     ::" + zipFile);
            Object[] srcFiles = srcs.toArray();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                    + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " before creating zip file in finalCompleteMailFiles files array values are given below: ");

            for (Object s : srcFiles) {
                System.out.println("   before putting inside Zipfile name is " + s);
            }

            //String[] srcFiles = {"/tmp/wifi.pdf", "/tmp/relay.pdf", "/tmp/vpn.pdf"};
            try {
                // create byte buffer

                byte[] buffer = new byte[1024];

                FileOutputStream fos = new FileOutputStream(zipFile);

                ZipOutputStream zos = new ZipOutputStream(fos);

                for (int i = 0; i < srcFiles.length; i++) {
                    File srcFile = new File(srcFiles[i].toString());

                    System.out.println(" filename is " + srcFiles[i].toString());

                    if (srcFile.exists()) {
                        System.out.println(" filename is " + srcFiles[i].toString() + " EXISTS to be included inside zip ");

                        FileInputStream fis = new NewFileInputStream(srcFile);

                        zos.putNextEntry(new ZipEntry(srcFile.getName()));

                        int length;

                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }

                        zos.closeEntry();

                        // close the InputStream
                        fis.close();

                    }
                }

                // start, code added by pr on 2nddec19
                File file = new File(zipFile);
                if (file.exists()) {
                    fileInputStream = new NewFileInputStream(file);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside ZIP file exists ");
                    String[] srcFilesArr = {zipFile};
                    Inform obj = new Inform();
                    obj.sendCompleteFiles(regNo, srcFilesArr);
                    System.out.println("inside  finalCompleteMailFiles() if File is exists  .. !!");
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside ZIP file doesnt exists ");
                }

                // end, code added by pr on 2nddec19    
                // close the ZipOutputStream
                zos.close();
            } catch (IOException e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Error creating zip file : " + e.toString());
            }
            // above code creates a zip file which contains all the above files
            //String[] srcFilesArr = Arrays.copyOf(srcFiles, srcFiles.length, String[].class);        
            //fileInputStream.close(); // to delete the file after a while
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside finalCompleteMailFiles function catch block " + e.toString());
            //e.printStackTrace();
        }
    }

    public String utcFormat(String date) throws ParseException {

        String modifyDate = "";
        //String formatDate = date.replaceAll("-", "") + "000000Z";
        //String formatDate = date.replaceAll("-", "");
        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyyMMdd");
        String formatedDate = newFormat.format(date1);
        System.out.println("FORMAT::::::" + formatedDate);

        modifyDate = formatedDate + "000000Z";
        return modifyDate;

    }

    public Map<String, Object> servicePackage(String bo, String po) throws NamingException {
        String final_bo = "", uid = "";
        String userdn = "";
        DirContext ctx = null;
        NamingEnumeration ne = null;
        Map<String, Object> map = new HashMap<>();

        String[] IDs = {"sunAvailableServices", "sunAvailableDomainNames", "preferredmailhost", "preferredmailmessagestore"};
        SearchControls ctls = new SearchControls();
        ctls.setReturningAttributes(IDs);
        ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

//        String filter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo + "))";
        ctx = SingleApplicationContext.getLDAPInstance();

        //NamingEnumeration ans = ctx.search("o=" + po + ",o=nic.in,dc=nic,dc=in", filter, ctls);
        String availpackage = "";
        String preferredmailhost = "";
        String preferredmailmessagestore = "";
        String s1 = "";
        boolean flag;
        String sp2 = "";
        String sp3 = "";
        Map<String, Object> maperr = new HashMap();
        Map<String, Object> mapsuccess = new HashMap();

        String dn_default = "o=" + bo.trim() + ",o=" + po.trim() + ",o=nic.in,dc=nic,dc=in";
        String filter = "(&(objectclass=sunDelegatedOrganization)(o=" + bo.trim() + "))";
        NamingEnumeration ans = ctx.search(dn_default, filter, ctls);

        while (ans.hasMoreElements()) {

            SearchResult sr = (SearchResult) ans.nextElement();
            Attributes attrs = sr.getAttributes();

            String t = sr.toString();

            System.out.println("CREATEUSER: " + "Under t :" + t);

            if (attrs.get("sunAvailableServices") != null) {
                availpackage = attrs.get("sunAvailableServices").toString().substring(22, attrs.get("sunAvailableServices").toString().length());
            }
            //availpackage = t.substring(t.indexOf("sunAvailableServices:") + 22, t.indexOf(", preferredmailhost")); //for service package
            System.out.println("CREATEUSER: " + "sunAvailableServices value =>>" + availpackage);

            if (attrs.get("preferredmailhost") != null) {
                preferredmailhost = attrs.get("preferredmailhost").toString().substring(19, attrs.get("preferredmailhost").toString().length());
            }
            //preferredmailhost = t.substring(t.indexOf("preferredmailhost:") + 19, t.indexOf(", preferredmailmessagestore")); //for preferredmailhost
            System.out.println("CREATEUSER: " + " preferredmailhost=>>" + preferredmailhost);

            if (attrs.get("preferredmailmessagestore") != null) {
                preferredmailmessagestore = attrs.get("preferredmailmessagestore").toString().substring(27, attrs.get("preferredmailmessagestore").toString().length());
            }
            // preferredmailmessagestore = t.substring(t.indexOf("preferredmailmessagestore:") + 27, t.indexOf(", sunavailabledomainnames")); //for preferredmailhost
            System.out.println("CREATEUSER: " + " preferredmailmessagestore=>>" + preferredmailmessagestore);

            if (attrs.get("sunAvailableDomainNames") != null) {
                s1 = attrs.get("sunAvailableDomainNames").toString().substring(25, attrs.get("sunAvailableDomainNames").toString().length());
            }
            //s1 = t.substring(t.indexOf("sunAvailableDomainNames:") + 25, t.indexOf("}"));
            System.out.println("CREATEUSER: " + "Available doamin =>>" + s1);

            if (availpackage.equals("")) {
                maperr.put("error2", "Error in sunAvailableServices Package => Contact admin");
                System.out.println("CREATEUSER: " + "Error in sunAvailableServices Package => Contact admin");
                flag = false;
            } else if (preferredmailhost.equals("")) {
                maperr.put("error2", "Error in preferredmailhost => Contact admin");
                System.out.println("CREATEUSER: " + "Error in preferredmailhost => Contact admin");
                flag = false;
            } else if (preferredmailmessagestore.equals("")) {
                maperr.put("error2", "Error in preferredmailmessagestore => Contact admin");
                System.out.println("CREATEUSER: " + "Error in preferredmailmessagestore => Contact admin");
                flag = false;
            } else if (s1.equals("")) {
                maperr.put("error2", "Error in sunAvailableDomainNames => Contact admin");
                System.out.println("CREATEUSER: " + "Error in sunAvailableDomainNames => Contact admin");
                flag = false;
            } else {

                Set<String> setdomain = new HashSet();
                String token[] = s1.split(",");
                for (int i = 0; i < token.length; i++) {
                    setdomain.add(token[i].trim());
                }

                if (!availpackage.contains(":")) {
                    System.out.println("admin.AdminAction.servicePackage() + wrong value found for po bo ");
                    mapsuccess.put("ldaperr", "error in available package format");

                } else {

                    String sp1 = availpackage.substring(0, availpackage.indexOf(":"));
                    String sp2tmp = availpackage.substring(availpackage.indexOf(":") + 1, availpackage.length());

                    if (sp2tmp.contains(":")) {
                        sp2 = (sp2tmp.substring(0, sp2tmp.indexOf(":"))).trim();
                        sp3 = (sp2tmp.substring(sp2tmp.indexOf(":") + 1, sp2tmp.length())).trim();
                    } else {
                        System.out.println("CREATEUSER: " + "not created package in this booooooooooooo:::");
                        sp2 = sp2tmp.trim();
                        sp3 = "0";
                    }
                    //count++;
                    System.out.println("CREATEUSER: " + "1st :" + sp1);
                    System.out.println("CREATEUSER: " + "2nd :" + sp2);
                    System.out.println("CREATEUSER: " + "3rd :" + sp3);

                    int i = Integer.parseInt(sp2);
                    int j = Integer.parseInt(sp3);
                    int k = i - j;
//                    session.setAttribute("pvalue", sp1);
//                    session.setAttribute("totalpackage", i);
//                    session.setAttribute("availablepackage", k);
//                    session.setAttribute("created", j);
//                    session.setAttribute("availabledomain", domain);
                    mapsuccess.put("pvalue", sp1);
                    mapsuccess.put("totalpackage", i);
                    mapsuccess.put("availablepackage", k);
                    mapsuccess.put("created", j);
                    mapsuccess.put("availabledomain", domain);
                    System.out.println("CREATEUSER: " + "total package =>" + i);
                    System.out.println("CREATEUSER: " + "available package =>" + k);

                }
            }
        }

        return mapsuccess;
    }
}
