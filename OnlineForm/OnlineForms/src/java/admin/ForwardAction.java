package admin;

import com.google.gson.Gson;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.utility.Constants;
import java.io.File;
import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.servlet.ServletContext;
import com.org.bean.Forms;
import com.org.connections.DbConnection;
import com.org.connections.SingleApplicationContext;
import com.org.connections.SingleApplicationContextSlave;
import com.org.dao.Database;
import com.org.dao.Ldap;
import com.org.service.SignUpService;
import com.org.validation.Validation;
//import com.sun.javafx.scene.control.skin.VirtualFlow;
import entities.LdapQuery;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import utility.ExcelCreator;
import utility.Inform;
import utility.NewFileInputStream;
import utility.ResetCSRFRandom;
import utility.VpnPushApi;

public class ForwardAction extends ActionSupport implements SessionAware {

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
    private final String dnsmailadmin = ctx.getInitParameter("DNS_MAILADMIN");

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
    private String bulk_id;// checkboxes value for bulk mobile id creation
    String moduleEsign;
    private Database db = null;
    private Ldap ldap = null;
    private Map<String, Object> userValuesFromLdap;
    private Map<String, Object> hmTrack = null;
    // start, code added by pr on 20thnov19
    List<Forms> prevrejectdata = new LinkedList<>();

    public List<Forms> getPrevrejectdata() {
        return prevrejectdata;
    }

    public void setPrevrejectdata(List<Forms> prevrejectdata) {
        this.prevrejectdata = prevrejectdata;
    }

    // end, code added by pr on 20thnov19
    public ForwardAction() {
        db = new Database();
        ldap = new Ldap();
        hmTrack = new HashMap<>();
    }

    public Map<String, Object> getHmTrack() {
        return hmTrack;
    }

    public void setHmTrack(Map<String, Object> hmTrack) {
        this.hmTrack = hmTrack;
    }

    public String getModuleEsign() {
        return moduleEsign;
    }

    public void setModuleEsign(String moduleEsign) {
        this.moduleEsign = moduleEsign;
    }

    public String getBulk_id() {
        return bulk_id;
    }

    public void setBulk_id(String bulk_id) {
        this.bulk_id = bulk_id;
    }

    // start, code added by pr on 7thmay19, to incorporate delete functionality in wifi form
    boolean isWifiDelete;

    public boolean isIsWifiDelete() {
        return isWifiDelete;
    }

    public void setIsWifiDelete(boolean isWifiDelete) {
        this.isWifiDelete = isWifiDelete;
    }

    // end, code added by pr on 7thmay19
    /*private final String ldapproviderurl = ctx.getInitParameter("provider-url");
     private final String ldapdn = ctx.getInitParameter("bind-dn");
     private final String ldapdn_pass = ctx.getInitParameter("bind-pass");*/
    private String regNo;
    private String statRemarks;
    private String fwd_coord;
    private String fwd_madmin;
    private String final_sms_id;
    private String po;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null, conSlave = null; // this line added by pr on 4thmay18
    private String uid_exist;
    private boolean isEmptyCoordList = false;

    // start , code added  by pr on 21stjun19, for firewall location to be saved on mark as done
    String location;

    public boolean isIsEmptyCoordList() {
        return isEmptyCoordList;
    }

    public void setIsEmptyCoordList(boolean isEmptyCoordList) {
        this.isEmptyCoordList = isEmptyCoordList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUid_exist() {
        return uid_exist;
    }

    public void setUid_exist(String uid_exist) {
        this.uid_exist = uid_exist;
    }

    public String getApp_ca_type() {
        return app_ca_type;
    }

    public void setApp_ca_type(String app_ca_type) {
        this.app_ca_type = app_ca_type;
    }
    private String app_ca_type;
    private String app_ca_path;

    public String getApp_ca_path() {
        return app_ca_path;
    }

    public void setApp_ca_path(String app_ca_path) {
        this.app_ca_path = app_ca_path;
    }
    Boolean raiseQueryBtn = true;

    public Boolean getRaiseQueryBtn() {
        return raiseQueryBtn;
    }

    public void setRaiseQueryBtn(Boolean raiseQueryBtn) {
        this.raiseQueryBtn = raiseQueryBtn;
    }
    private File csv_file;
    private ArrayList<String> errorArr;

    public ArrayList<String> getErrorArr() {
        return errorArr;
    }

    public void setErrorArr(ArrayList<String> errorArr) {
        this.errorArr = errorArr;
    }

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
    String mobile_ldap_email;
    String formName;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getMobile_ldap_email() {
        return mobile_ldap_email;
    }

    public void setMobile_ldap_email(String mobile_ldap_email) {
        this.mobile_ldap_email = mobile_ldap_email;
    }
    // end, code added by pr on 5thapr18
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
    private String type; // added on 4thdec17 for export excel for error and success documents
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
    // end, code added by pr on 19thfeb18
    // start, code added by pr on 22ndfeb18
    private String choose_da_type;

    public String getChoose_da_type() {
        return choose_da_type;
    }

    public void setChoose_da_type(String choose_da_type) {
        this.choose_da_type = choose_da_type;
    }
    // end, code added by pr on 22ndfeb18
    // start, code added by pr on 3rdjan18
    private String random;

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    // end, code added by pr on 3rdjan18
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
    }

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    private String bo;
    private String domain;

    public String getFinal_sms_id() {
        return final_sms_id;
    }

    public void setFinal_sms_id(String final_sms_id) {
        this.final_sms_id = final_sms_id;
    }

    public String getFwd_madmin() {
        return fwd_madmin;
    }

    public void setFwd_madmin(String fwd_madmin) {
        this.fwd_madmin = fwd_madmin;
    }
    private String add_coord;

    public String getAdd_coord() {
        return add_coord;
    }

    public void setAdd_coord(String add_coord) {
        this.add_coord = add_coord;
    }
    private String msg;
    private Boolean isSuccess;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }
    private Boolean isError;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFwd_coord() {
        return fwd_coord;
    }

    public void setFwd_coord(String fwd_coord) {
        this.fwd_coord = fwd_coord;
    }

    // Integrated by MI on 30thjuly2019
    // added by nikki on 26thjun2019
    private String new_vpn_no;

    public String getNew_vpn_no() {
        return new_vpn_no;
    }

    public void setNew_vpn_no(String new_vpn_no) {
        this.new_vpn_no = new_vpn_no;
    }
    // added by nikki on 26thjun2019
    // start , code added by pr on 9thapr18
    private ArrayList<String> coorddata = new ArrayList<>();
    private ArrayList<String> recpdata = new ArrayList<>();
    private ArrayList<Forms> querydata = new ArrayList<>();
    private ArrayList<String> dadata = new ArrayList<>();
    private ArrayList<String> statusdata = new ArrayList<>();
    // end, code added by pr on 8thmar18
    private ArrayList<String> podata = new ArrayList<>();
    private ArrayList<String> bodata = new ArrayList<>();
    private ArrayList<String> domaindata = new ArrayList<>();
    // start, code added by pr on 24thdec18 for bulk id creation
    private HashMap<String, String> bulkUpdateData = new HashMap<>();
    // end, code added by pr on 24thdec18 for bulk id creation
    private HashMap<String, String> empdata = new HashMap<>();
    private ArrayList<String> madmindata = new ArrayList<>();

    public ArrayList<String> getRecpdata() {
        return recpdata;
    }

    public void setRecpdata(ArrayList<String> recpdata) {
        this.recpdata = recpdata;
    }

    public ArrayList<String> getStatusdata() {
        return statusdata;
    }

    public void setStatusdata(ArrayList<String> statusdata) {
        this.statusdata = statusdata;
    }

    public ArrayList<Forms> getQuerydata() {
        return querydata;
    }

    public void setQuerydata(ArrayList<Forms> querydata) {
        this.querydata = querydata;
    }
    // end , code added by pr on 9thapr18
    // start , code added by pr on 6thapr18
    private String choose_recp;

    public String getChoose_recp() {
        return choose_recp;
    }

    public void setChoose_recp(String choose_recp) {
        this.choose_recp = choose_recp;
    }

    // end , code added by pr on 6thapr18
    // start, code added by pr on 8thmar18
    public ArrayList<String> getDadata() {
        return dadata;
    }

    public void setDadata(ArrayList<String> dadata) {
        this.dadata = dadata;
    }

    public ArrayList<String> getPodata() {
        return podata;
    }

    public void setPodata(ArrayList<String> podata) {
        this.podata = podata;
    }

    public ArrayList<String> getBodata() {
        return bodata;
    }

    public void setBodata(ArrayList<String> bodata) {
        this.bodata = bodata;
    }

    public ArrayList<String> getDomaindata() {
        return domaindata;
    }

    public void setDomaindata(ArrayList<String> domaindata) {
        this.domaindata = domaindata;
    }

    public ArrayList<String> getCoorddata() {
        return coorddata;
    }

    public void setCoorddata(ArrayList<String> coorddata) {
        this.coorddata = coorddata;
    }

    public HashMap<String, String> getBulkUpdateData() {
        return bulkUpdateData;
    }

    public void setBulkUpdateData(HashMap<String, String> bulkUpdateData) {
        this.bulkUpdateData = bulkUpdateData;
    }

    public HashMap<String, String> getEmpdata() {
        return empdata;
    }

    public void setEmpdata(HashMap<String, String> empdata) {
        this.empdata = empdata;
    }

    public ArrayList<String> getMadmindata() {
        return madmindata;
    }

    public void setMadmindata(ArrayList<String> madmindata) {
        this.madmindata = madmindata;
    }

    public String getStatRemarks() {
        return statRemarks;
    }

    public void setStatRemarks(String statRemarks) {
        this.statRemarks = statRemarks;
    }

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

    public String getUsvalue() {
        return usvalue;
    }

    public void setUsvalue(String usvalue) {
        this.usvalue = usvalue;
    }
    String usvalue = "";
    String aid = "";
    String rid = "";

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    // start, code added by pr on 1stmay19
    String emp_type;

    public String getEmp_type() {
        return emp_type;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }

    String prefEmail;

    public String getPrefEmail() {
        return prefEmail;
    }

    public void setPrefEmail(String prefEmail) {
        this.prefEmail = prefEmail;
    }

    // end, code added by pr on 1stmay19
    // start, code added by pr on 29thapr19
    String usOTP = "";

    public String getUsOTP() {
        return usOTP;
    }

    public void setUsOTP(String usOTP) {
        this.usOTP = usOTP;
    }

    private String CSRFRandom;

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    // end, code added by pr on 29thapr19
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

    // below function added by pr on 20thfeb19
    public HashMap<String, String> fetchLoggedinDetails() {
        HashMap<String, String> hm = new HashMap<String, String>();
        if (sessionMap != null && sessionMap.get("uservalues") != null) {
            UserData userdata = (UserData) sessionMap.get("uservalues");
            String email = userdata.getEmail();
            String mobile = userdata.getMobile();
            String name = userdata.getName();
            hm.put("name", name);
            hm.put("email", email);
            hm.put("mobile", mobile);
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == fetchLoggedinDetails in forward action:::::::::::::::::::::::" + hm + "  == USERAGENT " + userAgent + " == ");

        return hm;
    }

    // below function added by pr on 8thjan19, function modified by pr on 20thfeb19
    public String fetchIndividualEmail() {
        String loggedin_email = "";
        String mobile = "";
        if (sessionMap != null && sessionMap.get("uservalues") != null) {
            UserData userdata = (UserData) sessionMap.get("uservalues");
            loggedin_email = userdata.getEmail();
            mobile = userdata.getMobile();
        }

        // this will contain the specific indiviual mobile in case of support and admin logins with group emails like support@nic.in, support@nkn.in, smssupport@nic.in
        // above code commented below added  by pr on 20thfeb19
        if (!mobile.equals("")) {
            String dbEmail = fetchIndividualEmailTable(mobile);
            if (!dbEmail.equals("")) {
                loggedin_email = dbEmail;
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == inside fetchIndividualEmail function individual_loggedin_email value in forward action:::::::::::::::::::::::" + loggedin_email + "  == USERAGENT " + userAgent + " == ");
        return loggedin_email;
    }

    // below function added by pr on 20thfeb19, to get the individual mobile when log in from support ids
    public String fetchIndividualEmailTable(String mobile) {
        PreparedStatement ps = null;
        ResultSet res = null;
        String email = "";
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = " SELECT email_address FROM support_individual_login WHERE mobile = ? and individual_ip=? LIMIT 1 ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, mobile);
            ps.setString(2, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchIndividualEmailTable query is " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                email = res.getString("email_address");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne fetchIndividualEmailTable in ForwardAction class 1 " + e.getMessage());
            // above line commented below added by pr on 23rdapr19                    
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 1 " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 2 " + e.getMessage());
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 3 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 4 " + e.getMessage());
                }
            }
        }
        return email;
    }

    // below function added by pr on 6thjan19    
    public String createSHA(String regNo, String form_type, String ROEmail, String user_sec_email) {
        Boolean flag = false;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside createSHA function ");
        String email = fetchSessionEmail();
        String shValue = DigestUtils.shaHex(regNo + System.currentTimeMillis() + email);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sha value is " + shValue);
        // insert into sha_save table
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = "INSERT INTO sha_save SET sha_reg_no = ?, sha_form_type = ?, sha_ro_email = ?, sha_us_email = ?, sha_value = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, regNo);
            ps.setString(2, form_type);
            ps.setString(3, ROEmail);
            ps.setString(4, user_sec_email);
            ps.setString(5, shValue);
            // System.out.println(" sha save query value is " + ps);
            int res = ps.executeUpdate();
            if (res > 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 1 " + e.getMessage());
            // above line commented below added by pr on 23rdapr19                    
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 5 " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 6 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    Logger.getLogger(ForwardAction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (!flag) // if values are not inserted into the DB then empty string is returned
        {
            shValue = "";
        }
        return shValue;
    }

    public String us_process() {
        msg = "";
        if (sessionMap.get("usmsg") != null) {
            sessionMap.put("usmsg", "");
        }
        String usid = "";
        //System.out.println(" inside us_process function aid value is " + aid + " rid is " + rid);
        // usid is the sha value and usvalue is the approve or reject
        if (aid != null && !aid.equals("")) {
            usvalue = "approve";
            usid = aid;
        }
        if (rid != null && !rid.equals("")) {
            usvalue = "reject";
            usid = rid;
        }
        if ((usvalue.equals("approve") || usvalue.equals("reject"))
                && (usid.matches(shaRegEx))) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside us_process 1 ");

            // first check the regular expression for sha value and usvalue to be approve or reject
            // then check if this ip is blacklisted or not if not
            if (!checkBlacklist()) // if not blacklisted then process the request further else stop here
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside us_process 2  ");

                //then check if this sha value exist in the sha_save table, if yes then update the details in the same table for datetime , ip and sha_status approve or reject
                HashMap hm = checkShaExist(usid);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " hashmap got from sha value is  " + hm);

                String sha_status = "";
                String sha_ro_datetime = ""; // line added by pr on 29thjan19
                String reg_no = "";
                String form_type = ""; // line added by pr on 29thjan19
                if (hm != null && hm.size() > 0) {
                    if (hm.get("sha_status") != null) {
                        sha_status = hm.get("sha_status").toString();
                    }

                    if (sha_status.equals("")) {

                        // start, code added by pr on 29thjan19
                        // first check if sha value needs to be updated or the link has already expired
                        if (hm.get("sha_ro_datetime") != null) {
                            sha_ro_datetime = hm.get("sha_ro_datetime").toString();
                        }
                        if (hm.get("sha_reg_no") != null) {
                            reg_no = hm.get("sha_reg_no").toString();
                        }
                        if (hm.get("sha_form_type") != null) // if added by pr on 29thjan19
                        {
                            form_type = hm.get("sha_form_type").toString();
                        }
                        // System.out.println(" sha_ro_datetime value is " + sha_ro_datetime);
                        if (!sha_ro_datetime.equals("")) {

                            boolean isActive = false;
                            try {
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date currentDate = new Date();
                                Date date1 = null;
                                Date date2 = null;
                                String startDate = sha_ro_datetime;
                                String endDate = format.format(currentDate);
                                date1 = format.parse(startDate);
                                date2 = format.parse(endDate);
                                long getDiff = date2.getTime() - date1.getTime();
                                long diffInHours = TimeUnit.MILLISECONDS.toHours(getDiff);
                                //System.out.println(" timdiff is " + diffInHours);
                                int diffmin = (int) (getDiff / (60 * 1000));
                                // System.out.println(" diffmin is " + diffmin);
                                //if (diffInHours <= 48 || diffmin < (48 * 60)) {
                                //if (diffmin < (48 * 60)) { // 2 days
                                if (diffmin < (168 * 60)) { // 7 days, above line commented this line added by pr on 9thapr19
                                    isActive = true;
                                } else {
                                    // update the sha_save table for sha_status field value to be e and add an entry in the status table and final_audit_track table for US_EXPIRED
                                    updateUSLinkTimeout(usid, reg_no, form_type);
                                }
                            } catch (Exception e) {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside us_process function sha ro time " + e.getMessage());
                                isActive = false;
                            }
                            if (isActive) {
                                // end, code added by pr on 29thjan19
                                // update the table
                                boolean flag = updateSha(usid, usvalue);
                                // update status with US_REJECTED or next level value
                                if (flag) {
                                    // System.out.println(" inside us_process 4 ");
                                    //String form_type = "";
                                    String sha_us_email = "";
                                    //System.out.println(" inside us_process 5 ");
                                    if (hm.get("sha_reg_no") != null) {
                                        reg_no = hm.get("sha_reg_no").toString();
                                    }
                                    if (hm.get("sha_form_type") != null) {
                                        form_type = hm.get("sha_form_type").toString();
                                    }
                                    if (hm.get("sha_us_email") != null) {
                                        sha_us_email = hm.get("sha_us_email").toString();
                                    }
                                    // System.out.println(" hashmap values got are reg_no " + reg_no + " form_type value is " + form_type + " sha_us_email " + sha_us_email);
                                    if (usvalue.equals("approve")) {
                                        executeAtRO(reg_no, form_type, "", sha_us_email);
                                    } else {
                                        updateStatus(reg_no, "us_rejected", form_type, "Rejected by the Under Secretary", "", sha_us_email, "");
                                    }
                                    msg = "Request processed Successfully !";
                                } else {
                                    msg = "Request could not be processed, data not saved !";
                                }
                            } else {
                                //msg = "Under Secretary and above link Timeout !";
                                //msg = "Link was active only for 2 days. Link has been expired."; // line modified by pr on 11thmar19
                                msg = "Link was active only for 7 days. Link has been expired."; // line modified by pr on 9thapr19
                            }
                        } else // if around and else added by pr on 29thjan19
                        {
                            msg = "Request could not be processed !";
                        }
                    } else // else block modified by pr on 30thjan19
                    {
                        if (sha_status.equals("e")) {
                            //msg = "Under Secretary and above link Timeout !";
                            //msg = "Link was active only for 2 days. Link has been expired."; // line modified by pr on 11thmar19                            
                            msg = "Link was active only for 7 days. Link has been expired."; // line modified by pr on 9thapr19                            
                        } else {
                            msg = "Request has already been processed !";
                        }
                    }
                } else // if sha doesnt exist then add in the blacklist_trail table and then  after verifying the count add in the blacklist table
                {
                    msg = "Request not found !";
                    //System.out.println(" inside us_process 6 ");
                    // if it doesnt exist then add in the blacklist_trail table
                    addBlacklistIP(usid);
                }
            } else {
                msg = "This IP has been  blacklisted";
            }
        } else {
            msg = "Invalid Request";
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " just before the response in us_process function msg value is " + msg);

        sessionMap.put("usmsg", msg);
        return SUCCESS;
    }

    // below function added by pr on 29thapr19 to have a middle page in US Link action
    public String us_mid_process() {

        boolean sendOTP = false;

        if (sessionMap.get("under_sec_mobile") == null) {
            sendOTP = true;
        }

        try {

            if (regNo.matches(regNoRegEx)) {

                String formType = "";

                if (regNo.toLowerCase().contains("single")) {
                    formType = Constants.SINGLE_FORM_KEYWORD;
                } else if (regNo.toLowerCase().contains("bulk") && !regNo.toLowerCase().contains("nkn")) {
                    formType = Constants.BULK_FORM_KEYWORD;
                } else if (regNo.toLowerCase().contains("nkn")) {
                    formType = Constants.NKN_SINGLE_FORM_KEYWORD;
                }

                // System.out.println(" formtype value is " + formType + " reg no is " + regNo);
                HashMap<String, String> hm = fetchFormDetail(regNo, formType);

                String name = "", email = "", mobile = "", desig = "", min = "";

                String ro_name = "", ro_email = "", ro_mobile = "", ro_desig = "";

                String under_sec_mobile = "";

                // System.out.println(" hm value is " + hm);
                if (hm != null) {
                    if (hm.get("auth_off_name") != null && !hm.get("auth_off_name").equals("")) {
                        name = hm.get("auth_off_name");

                        sessionMap.put("name", name);

                    }

                    if (hm.get("email") != null && !hm.get("email").equals("")) {
                        email = hm.get("email");

                        sessionMap.put("email", email);
                    }

                    if (hm.get("mobile") != null && !hm.get("mobile").equals("")) {
                        mobile = hm.get("mobile");

                        sessionMap.put("mobile", mobile);

                    }

                    if (hm.get("designation") != null && !hm.get("designation").equals("")) {
                        desig = hm.get("designation");

                        sessionMap.put("desig", desig);
                    }

                    if (hm.get("ministry") != null && !hm.get("ministry").equals("")) {
                        min = hm.get("ministry");

                        sessionMap.put("ministry", min);
                    }

                    if (hm.get("hod_name") != null && !hm.get("hod_name").equals("")) {
                        ro_name = hm.get("hod_name");

                        sessionMap.put("ro_name", ro_name);
                    }

                    if (hm.get("hodemail") != null && !hm.get("hodemail").equals("")) {
                        ro_email = hm.get("hodemail");

                        sessionMap.put("ro_email", ro_email);
                    }

                    if (hm.get("hod_mobile") != null && !hm.get("hod_mobile").equals("")) {
                        ro_mobile = hm.get("hod_mobile");

                        sessionMap.put("ro_mobile", ro_mobile);
                    }

                    if (hm.get("ca_desig") != null && !hm.get("ca_desig").equals("")) {
                        ro_desig = hm.get("ca_desig");

                        sessionMap.put("ro_desig", ro_desig);
                    }

                    if (hm.get("under_sec_mobile") != null && !hm.get("under_sec_mobile").equals("")) {
                        under_sec_mobile = hm.get("under_sec_mobile");

                        sessionMap.put("under_sec_mobile", under_sec_mobile);

                    }

                }

                String sha_value = fetchShaValue(regNo);

                sessionMap.put("sha_value", sha_value);

                sessionMap.put("reg_no", regNo);

                //System.out.println(" under_sec_mobile value is " + under_sec_mobile);
                // send otp to US Mobile, save us mobile and otp in sha_save table only
                if (!under_sec_mobile.equals("")) {

                    //System.out.println(" under_sec_mobile value is " + under_sec_mobile + " inside mobile not equal to empty ");
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " under_sec_mobile value is " + under_sec_mobile + " inside mobile not equal to empty ");

                    if (sendOTP) // it shud shoot a otp sms only if v r coming to this page for the first time if v r refreshing it then sms shud not go, to again send the otp resend button needs to be clicked
                    {
                        sendUSOTP(under_sec_mobile, regNo);
                    }
                }

                // System.out.println(" inside us_mid_process func " + regNo);
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " inside us_mid_process function " + e.getMessage());
        }

        return SUCCESS;
    }

    // below function added by pr on 29thapr19
    public String resendUSOTP() {
        //System.out.println(" inside resend_us_otp function ");

        try {
            if (sessionMap.get("under_sec_mobile") != null && sessionMap.get("reg_no") != null && !sessionMap.get("under_sec_mobile").equals("") && !sessionMap.get("reg_no").equals("")) {
                sendUSOTP(sessionMap.get("under_sec_mobile").toString(), sessionMap.get("reg_no").toString());

                isSuccess = true;

                isError = false;

                msg = "OTP Sent Successfully";
            }
        } catch (Exception e) {
            isSuccess = false;

            isError = true;

            msg = "OTP Sent Successfully";

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " inside resendUSOTP function " + e.getMessage());

        }

        return SUCCESS;
    }

    // below function added by pr on 29thapr19
    public void sendUSOTP(String mobile, String regNo) {
        // get the otp from sha_save if its within the 30 minutes from the first insertion 

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " inside sendUSOTP function ");

        PreparedStatement ps = null;

        ResultSet rs = null;
        //Connection con = null;

        Inform infObj = new Inform();

        String sha_otp = "", sha_otp_datetime = "";

        int random;
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021

            String qry = "SELECT sha_otp, sha_otp_datetime FROM sha_save WHERE sha_reg_no = ?  ORDER BY sha_id desc LIMIT 1 ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, regNo);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " sendUSOTP " + ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                sha_otp = rs.getString("sha_otp");

                sha_otp_datetime = rs.getString("sha_otp_datetime");

            }

            if (sha_otp != null && !sha_otp.equals("") && sha_otp_datetime != null && !sha_otp_datetime.equals("")) {

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                Date currentDate = new Date();

                Date date1 = null;

                Date date2 = null;

                String startDate = sha_otp_datetime;

                String endDate = format.format(currentDate);

                date1 = format.parse(startDate);

                date2 = format.parse(endDate);

                long getDiff = date2.getTime() - date1.getTime();

                long diffInHours = TimeUnit.MILLISECONDS.toHours(getDiff);

                // System.out.println(" timdiff is " + diffInHours);
                int diffmin = (int) (getDiff / (60 * 1000));

                if (diffmin > 30) // if its more than 30 minutes since otp was generated , generate it again
                {
                    random = random();

                    sha_otp = Integer.toString(random);

                    // update it in the table sha_save also            
                    qry = "UPDATE sha_save SET sha_otp = ?, sha_otp_datetime = CURRENT_TIMESTAMP  WHERE sha_reg_no = ? ";

                    ps = con.prepareStatement(qry);

                    ps.setString(1, sha_otp);

                    ps.setString(2, regNo);

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " sendUSOTP update query " + ps);

                    int i = ps.executeUpdate();

                    if (i > 0) {
                        infObj.sendUSOTP(mobile, sha_otp);

                    }

                } else {
                    infObj.sendUSOTP(mobile, sha_otp);
                }

            } else // send it fresh
            {
                random = random();

                sha_otp = Integer.toString(random);

                // update it in the table sha_save also            
                qry = "UPDATE sha_save SET sha_otp = ?, sha_otp_datetime = CURRENT_TIMESTAMP  WHERE sha_reg_no = ? ";

                ps = con.prepareStatement(qry);

                ps.setString(1, sha_otp);

                ps.setString(2, regNo);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " sendUSOTP update query else " + ps);

                int i = ps.executeUpdate();

                if (i > 0) {
                    infObj.sendUSOTP(mobile, sha_otp);

                }

            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateUSLinkTimeout in ForwardAction class exception " + e.getMessage());

            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " sendUSOTP EXCEPTION 1 " + e.getMessage());
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " sendUSOTP EXCEPTION 2 " + e.getMessage());
                }
            }

            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " sendUSOTP EXCEPTION 3 " + e.getMessage());

                }
            }

        }

        // if not then generate and update the otp in the sha_save table
        // get the mobile number of US
    }

    // below function added by pr on 29thapr19
    public static int random() {
        int response = 0;
        SecureRandom rand = new SecureRandom();
        int num1 = rand.nextInt(10);

        while (num1 == 0) {
            num1 = rand.nextInt(10);
        }
        int num2 = rand.nextInt(10);
        int num3 = rand.nextInt(10);
        int num4 = rand.nextInt(10);
        int num5 = rand.nextInt(10);
        int num6 = rand.nextInt(10);
        response = num1 * 100000 + num2 * 10000 + num3 * 1000 + num4 * 100 + num5 * 10 + num6;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "OTP generated: " + response);
        return response;
    }

    // below function added by pr on 29thapr19
    public String fetchShaValue(String regNo) {

        PreparedStatement ps = null;

        ResultSet rs = null;
        //Connection con = null;

        String sha_value = "";
        try {

            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            //String qry = "UPDATE sha_save SET sha_status = 'e', sha_us_datetime = CURRENT_TIMESTAMP WHERE sha_value = ? ";
            String qry = "SELECT sha_value FROM sha_save WHERE sha_reg_no = ? ORDER BY sha_id desc LIMIT 1 ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, regNo);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " fetchShaValue select query is " + ps);

            rs = ps.executeQuery();

            while (rs.next()) {
                sha_value = rs.getString("sha_value");
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateUSLinkTimeout in ForwardAction class exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " fetchShaValue EXCEPTION 1 " + e.getMessage());
                }
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " fetchShaValue EXCEPTION 2 " + e.getMessage());
                }
            }

            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " fetchShaValue EXCEPTION 3 " + e.getMessage());

                }
            }

        }

        return sha_value;

    }

    // below function added by pr on 29thapr19
    public String checkUSOTP() {
        // check the request otp and reg no in the table sha_save if matches send back mesg as error

        boolean otpValid = false;

        // check regex for usOTP and 
        if (regNo != null && usOTP != null && !regNo.equals("") && !usOTP.equals("") && regNo.matches(regNoRegEx) && (usOTP.length() == 6 && usOTP.matches("^[0-9]*$"))) {
            // check in the sha_save table

            PreparedStatement ps = null;

            ResultSet rs = null;
            //Connection con = null;
            try {

                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                String qry = "SELECT * FROM sha_save WHERE sha_reg_no = ?  AND sha_otp = ? ";

                ps = conSlave.prepareStatement(qry);

                ps.setString(1, regNo);

                ps.setString(2, usOTP);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " checkUSOTP " + ps);

                rs = ps.executeQuery();

                while (rs.next()) {
                    otpValid = true;
                }

                if (otpValid) {
                    isSuccess = true;

                    isError = false;

                    msg = "OTP Matched";
                } else {
                    isSuccess = false;

                    isError = true;

                    msg = "OTP Mismatched";
                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne checkUSOTP exception " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne checkUSOTP exception 1 " + e.getMessage());

                    }
                }

                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne checkUSOTP exception 2 " + e.getMessage());
                    }
                }

                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne checkUSOTP exception 3 " + e.getMessage());
                    }
                }

            }

        } else {
            isSuccess = false;

            isError = true;

            msg = "Please enter valid Value";
        }

        return SUCCESS;
    }

    // below function added by pr on 29thjan19
    public void updateUSLinkTimeout(String sha, String reg_no, String form_type) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE sha_save SET sha_status = 'e', sha_ip = ? WHERE sha_value = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, sha);
            ps.setString(2, ip); // ip added by pr on 6thmar19
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " updateUSLinkTimeout " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                // add an entry in the status table 
                qry = " INSERT INTO status SET stat_form_type = ?, stat_reg_no = ?, stat_type = 'us_expired', stat_remarks = 'Link Expired', stat_ip = ?, stat_forwarded_by_ip = ?, "
                        + ""
                        + "stat_forwarded_by_datetime = current_timestamp ";
                ps = con.prepareStatement(qry);
                ps.setString(1, form_type);
                ps.setString(2, reg_no);
                ps.setString(3, ip);
                ps.setString(4, ip);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " updateUSLinkTimeout status entry " + ps);
                ps.executeUpdate();
                // update the entry in final_audit_track
                //qry = " UPDATE final_audit_track SET status = 'us_expired', to_name = '', to_email = '', to_mobile = '', to_datetime = CURRENT_TIMESTAMP WHERE registration_no = ? "; // , to_datetime = CURRENT_TIMESTAMP added by pr on 11thfeb19
                qry = " UPDATE final_audit_track SET status = 'us_expired', to_datetime = CURRENT_TIMESTAMP WHERE registration_no = ? "; // line modified by pr on 9thdec19
                ps = con.prepareStatement(qry);
                ps.setString(1, reg_no);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " updateUSLinkTimeout final_audit_track update " + ps);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateUSLinkTimeout in ForwardAction class exception " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 7 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                   
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 8 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 9 " + e.getMessage());
                }
            }
        }
    }

    // below function added by pr on 6thjan19
    public void addBlacklistIP(String sha) {
        // first add in the table blacklist_trail
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "INSERT INTO blacklist_trail SET ip = ?, sha_entered = ? "; // stat_ip added by pr on 3rdjan19
            ps = con.prepareStatement(qry);
            ps.setString(1, ip);
            ps.setString(2, sha);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " addBlacklistIP " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                // check its count in the same table , if it exceeds the 10 number then add in the blacklist table
                qry = "SELECT ip FROM blacklist_trail WHERE ip = ? "; // stat_ip added by pr on 3rdjan19
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, ip);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " addBlacklistIP  select query " + ps);
                rs = ps.executeQuery();
                int count = 0;
                while (rs.next()) {
                    count++;
                    System.out.println(" inside while for count " + count);
                }
                if (count >= 10) {
                    // insert into the blacklist table
                    qry = "INSERT INTO blacklisted SET ip = ? "; // stat_ip added by pr on 3rdjan19
                    ps = con.prepareStatement(qry);
                    ps.setString(1, ip);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " addBlacklistIP blacklisted insert " + ps);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 10 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 11 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 12 " + e.getMessage());
                }
            }
        }
        //after adding take the count if it exceeds 10 then add in the blacklisted table
    }

    // below function added by pr on 6thjan19
    public boolean checkBlacklist() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isExist = false;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT ip FROM blacklisted WHERE ip = ? "; // stat_ip added by pr on 3rdjan19
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " checkBlacklist " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                isExist = true;
            }
            // start, code added by pr on 6thmar18
            ps.close();
            // end, code added by pr on 6thmar18
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 13 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 14 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 15 " + e.getMessage());
                }
            }
        }
        return isExist;
    }

    // below function added by pr on 6thjan19
    public HashMap checkShaExist(String sha) // if sha exists it returns a hashmap of that sha row else return null
    {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap hm = new HashMap<String, String>();
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            // sha_status column added  by pr on 11thapr19
            String qry = "SELECT sha_reg_no,sha_form_type,sha_ro_email,sha_us_email,sha_ro_datetime, sha_status FROM sha_save WHERE sha_value = ? "; // stat_ip added by pr on 3rdjan19
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, sha);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " checkShaExist " + ps);
            rs = ps.executeQuery();
            //System.out.println(" inside check sha exists if rs next ");
            while (rs.next()) {
                hm.put("sha_reg_no", rs.getString("sha_reg_no"));
                hm.put("sha_form_type", rs.getString("sha_form_type"));
                hm.put("sha_ro_email", rs.getString("sha_ro_email"));
                hm.put("sha_us_email", rs.getString("sha_us_email"));
                hm.put("sha_status", rs.getString("sha_status"));
                hm.put("sha_ro_datetime", rs.getString("sha_ro_datetime")); // line added by pr on 30thjan19
            }
            if (hm.size() == 0) {
                hm = null;
            }
            // start, code added by pr on 6thmar18
            ps.close();
            // end, code added by pr on 6thmar18
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 16 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 17 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 18 " + e.getMessage());
                }
            }
        }
        //System.out.println(" hashmap formed in check sha exist function is " + hm);
        return hm;
    }

    // below function added by pr on 6thjan19
    public boolean updateSha(String sha, String sha_status) {
        PreparedStatement ps = null;
        //Connection con = null;
        boolean flag = false;
        if (sha_status.equals("approve")) {
            sha_status = "a";
        } else {
            sha_status = "r";
        }
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE sha_save SET sha_ip = ?, sha_status = ?, sha_us_datetime = current_timestamp WHERE sha_value = ? "; // stat_ip added by pr on 3rdjan19
            ps = con.prepareStatement(qry);
            ps.setString(1, ip);
            ps.setString(2, sha_status);
            ps.setString(3, sha);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " updateSha " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                flag = true;
            }
            // start, code added by pr on 6thmar18
            ps.close();
            // end, code added by pr on 6thmar18
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 19 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 20 " + e.getMessage());
                }
            }
        }
        return flag;
    }

    public String execute() {
        // forward the request 
        Boolean updateRes = false;
        String regNo = this.regNo;
        regNo = regNo.trim();
        String role = "";
        isSuccess = false;
        isError = false;
        msg = "";

        if ((app_ca_type != null && app_ca_type.equals("online")) || (app_ca_type != null && app_ca_type.equals("manual_upload"))) {
            String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();
            if (!CSRFRandom.equals(SessionCSRFRandom)) {
                hmTrack.put("csrf_error", "CSRF Token is invalid.");
                return SUCCESS;
            }
        } else if ((app_ca_type != null && app_ca_type.equals("rej"))) {
            String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();
            if (!CSRFRandom.equals(SessionCSRFRandom)) {
                hmTrack.put("csrf_error", "CSRF Token is invalid.");
                return SUCCESS;
            }
        }
        if (regNo.matches(regNoRegEx)) {
            // split regNo with ~ to get the form name as well        
            String[] arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
            // arr[0] - > reg no arr[1] form name arr[2] action type like forward or reject or create
            if (arr != null && arr.length == 3) {
                // insert a row in the status table with stat_type as SUPPORT_PENDING
                role = fetchSessionRole();
                if (role.equals(Constants.ROLE_CA)) {
                    if (arr[2].equals("forward")) {
                        // check if the app_ca_type is MANUAL then bypass the below code and send an mobile and sms with sha value to the us
                        // if online then execute the below code

                        String roEmail = fetchSessionEmail(); // line added by pr on 12thjan19
                        //System.out.println(" ro mobile value is " + roEmail);
                        if (emailAvail(roEmail)) {

                        } else {

                        }
                        if (app_ca_type.toLowerCase().contains("manual") && emailAvail(roEmail) && (arr[1].equals(Constants.BULK_FORM_KEYWORD) || arr[1].equals(Constants.SINGLE_FORM_KEYWORD)
                                || arr[1].equals(Constants.NKN_SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.NKN_BULK_FORM_KEYWORD))) { // if its a case of MANUAL then after RO send the request to the Under Secreatry (US), also if the RO is non NIC then only send to US ro mobile available in LDAP check added by pr on 12thjan19
                            if (statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx))) // line modified by pr on 19thfeb18
                            {
                                HashMap detailHM = fetchFormDetail(arr[0], arr[1]);
                                //System.out.println(" form detail hashmap value is " + detailHM);
                                String under_sec_name = "", under_sec_mobile = "", under_sec_email = "";
                                if (detailHM != null && detailHM.size() > 0) {

                                    if (detailHM.get("under_sec_name") != null) {
                                        under_sec_name = detailHM.get("under_sec_name").toString();
                                    }
                                    if (detailHM.get("under_sec_mobile") != null) {
                                        under_sec_mobile = detailHM.get("under_sec_mobile").toString();
                                    }
                                    if (detailHM.get("under_sec_email") != null) {
                                        // System.out.println(" inside detail hm sec mobile not null ");
                                        under_sec_email = detailHM.get("under_sec_email").toString();
                                    }
                                }

                                if (under_sec_email != null && !under_sec_email.equals("")) {
                                    // get the loggged in mobile of the RO and 
                                    String emailRO = fetchSessionEmail();
                                    String sha = createSHA(arr[0], arr[1], emailRO, under_sec_email);
                                    if (!sha.equals("")) {
                                        // add a row in status table
                                        Boolean res = updateStatus(arr[0], "us_pending", arr[1], statRemarks, under_sec_email, "", "");
                                        if (res) {
                                            // update the application_type table for CA related details, line added by pr on 12thapr18
                                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            Inform infObj = new Inform();
                                            infObj.sendUSIntimation(arr[0], arr[1], under_sec_name, under_sec_email, under_sec_mobile, emailRO, statRemarks, sha);
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + arr[0] + ") forwarded to the Under Secretary or other higher authority mentioned in the form for Approval. Please ask him/her to check email for details.";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + arr[0] + ") could not be forwarded to the Under Secretary";
                                        }
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "Application (" + arr[0] + ") could not be forwarded to the Under Secretary";
                                    }
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    msg = "Application (" + arr[0] + ") could not be forwarded to the Under Secretary. Under secretary credentials not found";
                                }
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // constant added  by pr on 28thdec18
                            }
                        } else { // above if and else around added by pr on 6thjan19, if it is a non MANUAL process then process the request here itself
                            //System.out.println(" not manual , execute the current code " + "arr[0]" + arr[0] + "arr[1]" + arr[1]);
                            // call the new extracted function executeAtRO(); // code taken out by pr on 7thjan19
                            executeAtRO(arr[0], arr[1], statRemarks, "");
                        }
                    } else if (arr[2].equals("reject")) {
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " stat remarks value is " + statRemarks);
                        if (statRemarks.matches(statRemarksRegEx)) // line modified by pr on 19thfeb18
                        {
                            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " stat remarks matches ");
                        } else {
                            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " stat remarks doesnt match ");
                            statRemarks = ""; // line added by pr on 7thfeb18 for xss
                        }
                        if (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)) // line modified by pr on 19thfeb18
                        {
                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside reject stat remarks matches ");
                            updateRes = updateStatus(arr[0], "ca_rejected", arr[1], statRemarks, "", "", "");
                            if (updateRes) {
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + arr[0] + ") Rejected Successfully !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + arr[0] + ") could not be Rejected !";
                            }
                        } else {
                            isSuccess = false;
                            isError = true;
                            msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // constant appended by pr on 28thdec18
                        }
                    }
                } else if (role.equals(Constants.ROLE_SUP)) {
                    if (arr[2].equals("reject")) {
                        if (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)) // line modified by pr on 19thfeb18
                        {
                            updateRes = updateStatus(arr[0], "support_rejected", arr[1], statRemarks, "", "", "");
                            if (updateRes) {
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + arr[0] + ") Rejected Successfully !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + arr[0] + ") could not be Rejected !";
                            }
                        } else {
                            statRemarks = ""; // line added by pr on 7thfeb18 for xss
                            isSuccess = false;
                            isError = true;
                            msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // constant appended by pr on 28thdec18
                        }
                    }
                } else if (role.equals(Constants.ROLE_CO)) {
                    if (arr[2].equals("reject")) {
                        if (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)) // line modified by pr on 19thfeb18 
                        {
                            updateRes = updateStatus(arr[0], "coordinator_rejected", arr[1], statRemarks, "", "", "");
                            if (updateRes) {
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + arr[0] + ") Rejected Successfully !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + arr[0] + ") could not be Rejected !";
                            }
                        } else {
                            statRemarks = ""; // line added by pr on 7thfeb18 for xss
                            isSuccess = false;
                            isError = true;
                            msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // constant appended by pr on 28thdec18
                        }
                    }
                } else if (role.equals(Constants.ROLE_MAILADMIN)) // else if added by pr on 26thfeb18 
                {
                    if (arr[2].equals("reject")) {
                        //System.out.println(" inside reject at mailadmin ");
                        if (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)) // line modified by pr on 19thfeb18 
                        {
                            AdminAction adminAction = new AdminAction();

                            if (adminAction.isDomainAPI(arr[0]) && arr[1].equalsIgnoreCase(Constants.DNS_FORM_KEYWORD)) {

                                try {

                                    // call another api if its a case of domainapi
                                    String response1 = "FAIL";

                                    HashMap formDetailHM = fetchFormDetail(arr[0], arr[1]);

                                    // start, code added by pr on 7thjan2020
                                    String req_for = "";

                                    System.out.println(" before dns push api call formDetailHM value is " + formDetailHM);

                                    if (formDetailHM != null) {

                                        req_for = formDetailHM.get("req_for").toString(); // line added by pr on 7thjan2020

                                    } else {

                                        req_for = ""; // line added by pr on 7thjan2020

                                    }

                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " req_for value is " + req_for); // getMessage added by pr on 6thjul18

                                    // end, code added by pr on 7thjan2020
                                    jsn jsnObj = new jsn();

                                    response1 = jsnObj.dns_push_domains(arr[0], req_for, "rejected"); // second attribute added  by pr on 7thjan2020

                                    if (response1.toLowerCase().contains("success")) {

                                        response1 = "SUCCESS";

                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();

                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Inside jsnobj dns_push_domains exception " + e.getMessage()); // getMessage added by pr on 6thjul18

                                }

                            }

                            //System.out.println(" inside reject at mailadmin inside statremarks ");
                            updateRes = updateStatus(arr[0], "mail-admin_rejected", arr[1], statRemarks, "", "", "");
                            //System.out.println(" inside reject at mailadmin inside statremarks  updateRes value is  " + updateRes);
                            if (updateRes) {
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + arr[0] + ") Rejected Successfully !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + arr[0] + ") could not be Rejected !";
                            }
                        } else {
                            statRemarks = ""; // line added by pr on 7thfeb18 for xss
                            isSuccess = false;
                            isError = true;
                            msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // constant appended by pr on 28thdec18
                        }
                    }
                }
            }
        } else // else added by pr on 5thfeb18
        {
            regNo = ""; // line added by pr on 6thfeb18
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside regno mismatch ");
            isSuccess = false;
            isError = true;
            msg = "Action could not be Performed !";
        }
        // start, code added by pr on 8thfeb18 for xss
        if (statRemarks != null && !statRemarks.equals("")) {
            statRemarks = ESAPI.encoder().encodeForHTMLAttribute(statRemarks);
        }
        if (regNo != null && !regNo.equals("")) {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        if (random != null && !random.equals("")) {
            random = ESAPI.encoder().encodeForHTMLAttribute(random);
        }
        sessionMap.put("msg", msg);
        sessionMap.put("issuccessesign", isSuccess);
        sessionMap.put("iserroresign", isError);
        return SUCCESS;
    }

    public void executeAtRO(String reg_no, String form_type, String statRemarks, String sha_us_email) // if the application is at us level and has crossed the RO level, then sha_us_email will have a value
    {
        //12-apr-2022 syndata for both create id and mark as done
//        NewCAHome newCAHome = new NewCAHome();
//        newCAHome.syncData(reg_no);
        //12-apr-2022 syndata for both create id and mark as done
        try {
            String commaSeparatedAliases = "";
            boolean updateAppType = true;
            if (!sha_us_email.equals("")) {
                updateAppType = false;
            }
            Boolean updateRes = false;
            String[] arr = new String[]{reg_no, form_type};
            boolean domainAllowed = false;
            System.out.println(" regno is " + reg_no + " form_type is " + form_type + " arr[0] " + arr[0] + " arr[1] " + arr[1]);
            if (statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx))) // line modified by pr on 19thfeb18
            {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " inside executeAtRO");

                // start, code added by pr on 26thfeb18
                Boolean isNIC = false;
                Boolean isHimachal = false;
                // Changes made by Ashwini on 3rd April 2018
                Boolean isGEM = false;
                Boolean isImapMobile = false;
                HashMap detailHM = fetchFormDetail(arr[0], arr[1]);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                        + " detailHM inside executeatro is " + detailHM);

                String add_state = detailHM.get("add_state").toString();
                String auth_email = detailHM.get("email").toString();
                String employment = detailHM.get("employment").toString();
                //boolean isAuthEmailInLdap = ldap.emailValidate(auth_email);
                String dn = fetchLDAPDN(auth_email, arr[1]).toLowerCase();
                String RO_email = detailHM.get("hodemail").toString();
                String ministry = "";
                if (detailHM.get("ministry") != null) {
                    ministry = detailHM.get("ministry").toString();
                }
                // start, code added by pr on 27thfeb2020
                if (ministry != null && !ministry.isEmpty() && (ministry.contains("Department of space") || ministry.contains("Department of Atomic Energy (DAE)")) && (arr[1].equals(Constants.SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.NKN_BULK_FORM_KEYWORD) || arr[1].equals(Constants.EMAILACTIVATE_FORM_KEYWORD) || arr[1].equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD) || arr[1].equals(Constants.DOR_EXT_FORM_KEYWORD) || arr[1].equals(Constants.BULK_FORM_KEYWORD))) {
                    ministry = "";
                    ministry = detailHM.get("min").toString();
                }

                String emp_type = "";

                if (detailHM != null && detailHM.get("emp_type") != null) {
                    emp_type = detailHM.get("emp_type").toString();
                }

                String org = null;
                //if (arr[1] != null && arr[1].equals(Constants.DIST_FORM_KEYWORD))  // line commented by pr on 20thmay2020
                {
                    if (detailHM != null && detailHM.get("organization") != null) {
                        org = (String) detailHM.get("organization");
                    }
                }
                String dn_forRO = fetchLDAPDN(RO_email, arr[1]).toLowerCase();
                String city = "";
                String address = "";

                if (detailHM != null && detailHM.get("city") != null) {
                    city = (String) detailHM.get("city");
                }

                if (detailHM != null && detailHM.get("address") != null) {
                    address = (String) detailHM.get("address");
                }

//                if (!(arr[1].equals(Constants.VPN_ADD_FORM_KEYWORD) || arr[1].equals(Constants.VPN_RENEW_FORM_KEYWORD) || arr[1].equals(Constants.VPN_SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || arr[1].equals(Constants.VPN_DELETE_FORM_KEYWORD)) && (employment.equalsIgnoreCase("Others") && org.equalsIgnoreCase("DataCentre and Webservices") && add_state.equalsIgnoreCase("MAHARASHTRA")
//                        && city.equalsIgnoreCase("Pune") && (address.toLowerCase().contains("ndc") || address.toLowerCase().contains("national data center")))) {
//                    // send the request to vaij.v@nic.in as coordinator pending when approved by RO
//
//                    String stat_type = "coordinator_pending";
//                    String toWhichAdmin = "Coordinator";
//                    String val = "vaij.v@nic.in"; 
//
//                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, ""); // statRemarks added by pr on 5thjun18
//                    if (updateRes) {
//                        // update the application_type table for CA related details, line added by pr on 12thapr18
//                        if (updateAppType) {
//                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
//                        }
//                        isSuccess = true;
//                        isError = false;
//                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " - " + val + " !";// text changed by pr on 19thfeb18
//                    } else {
//                        isSuccess = false;
//                        isError = true;
//                        msg = "Application (" + reg_no + ") could not be Forwarded to the " + toWhichAdmin + " - " + val + " !";
//                    }
//
//                }// end, code added by pr on 20thmay2020
//                else  code commentted by ashwini sir on 5 AUG 2021///////////
                if (arr[1].equals(Constants.VPN_ADD_FORM_KEYWORD) || arr[1].equals(Constants.VPN_RENEW_FORM_KEYWORD) || arr[1].equals(Constants.VPN_SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || arr[1].equals(Constants.VPN_DELETE_FORM_KEYWORD)) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " inside executeAtRO for VPN form - separate block");

                    String val = "";
                    String stat_type = "", toWhichAdmin = "";
                    boolean isKeyAvailable = false;

                    if (sessionMap != null && sessionMap.get("uservalues") != null) {
                        UserData userdata = (UserData) sessionMap.get("uservalues");
                        if (userdata.isIsHOG() || userdata.isIsHOD()) {
                            //send to Admin Directly
                            stat_type = "mail-admin_pending";
                            toWhichAdmin = "VPN Admin";
                            val = "vpnsupport@nic.in";
                            updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");
                            if (updateRes) {
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                VpnPushApi vpnapi = new VpnPushApi();
                                vpnapi.callVpnWebService(arr[0]);
                                isSuccess = true;
                                isError = false;

                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                            }
                        } else {
                            if ((dn.contains("o=nic employees") || dn.contains("o=nic-official-id") || dn.contains("o=dio"))) {
                                if (userdata.isIsNICEmployee()) {
                                    stat_type = "mail-admin_pending";
                                    toWhichAdmin = "VPN Admin";
                                    val = "vpnsupport@nic.in";
                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");
                                } else {
                                    // Not possible, hence show error 
                                    updateRes = false;
                                    // fetch coordinator from VPN state coordinator table and send the request to VPN coordinator
//                                    String ministry = detailHM.get("ministry").toString();
                                    String organization = detailHM.get("organization").toString();
                                    String coordinators = "";
                                    coordinators = fetchEmpCoordsAtCAForVPN(reg_no);

                                    Set<String> aliases = userdata.getAliases();
                                    boolean isRoCoordinator = false;

                                    if (!coordinators.trim().isEmpty()) {
                                        for (String aliase : aliases) {
                                            if (coordinators.contains(aliase)) {
                                                isRoCoordinator = true;
                                                break;
                                            }
                                        }

                                        if (!isRoCoordinator) {
                                            stat_type = "coordinator_pending";
                                            toWhichAdmin = "Coordinator";
                                            val = coordinators;
                                        } else {
                                            stat_type = "mail-admin_pending";
                                            toWhichAdmin = "Admin";
                                            val = "vpnsupport@nic.in";
                                        }
                                    } else {
                                        stat_type = "support_pending";
                                        toWhichAdmin = "VPN Support";
                                        val = "vpnsupport@nic.in";
                                    }
                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, ""); //statRemarks added by pr on 5thjun18
                                }
                            } else {
                                // fetch coordinator from VPN state coordinator table and send the request to VPN coordinator
//                                String ministry = detailHM.get("ministry").toString();
                                String organization = detailHM.get("organization").toString();
                                String coordinators = "";
                                coordinators = fetchEmpCoordsAtCAForVPN(reg_no);
                                //System.out.println("@@@@@@@@@@coordinators" + coordinators);
                                Set<String> aliases = userdata.getAliases();
                                boolean isRoCoordinator = false;

                                if (!coordinators.trim().isEmpty()) {
                                    for (String aliase : aliases) {
                                        if (coordinators.contains(aliase)) {
                                            isRoCoordinator = true;
                                            break;
                                        }
                                    }
                                    if (!isRoCoordinator) {
                                        stat_type = "coordinator_pending";
                                        toWhichAdmin = "Coordinator";
                                        val = coordinators;
                                    } else {
                                        stat_type = "mail-admin_pending";
                                        toWhichAdmin = "Admin";
                                        val = "vpnsupport@nic.in";
                                    }
                                } else {
                                    stat_type = "support_pending";
                                    toWhichAdmin = "VPN Support";
                                    val = "vpnsupport@nic.in";
                                }
                                updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, ""); //statRemarks added by pr on 5thjun18
                            }

                            if (updateRes) {
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                if (stat_type.equals("mail-admin_pending")) {
                                    VpnPushApi vpnapi = new VpnPushApi();
                                    vpnapi.callVpnWebService(arr[0]);
                                }
                                isSuccess = true;
                                isError = false;

                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                            }
                        }
                    }
                } else if (employment.equalsIgnoreCase("Central") && ministry.equalsIgnoreCase("Department of space")) // else if added by pr on 3rdapr2020
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " inside executeAtRO for DOS and DAE "
                            + "  - separate block");
                    String stat_type = "coordinator_pending";
                    String to_email = "";
                    String to = "Coordinator";

                    List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("Central", "Department of space", "");
                    if (coordEmails != null && !coordEmails.isEmpty()) {
                        for (String coordEmail : coordEmails) {
                            to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                        }

                        //  to_email = to_email.replaceAll(",$", to_email);
                        to_email = to_email.replaceAll(",$", "");
                    }
                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, to_email, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                    if (updateRes) {
                        // update the application_type table for CA related details, line added by pr on 12thapr18
                        if (updateAppType) {
                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                        }
                        isSuccess = true;
                        isError = false;
                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + to + " - " + to_email + " !";// text changed by pr on 19thfeb18
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Application (" + reg_no + ") could not be Forwarded to the " + to + " - " + to_email + " !";
                    }
                } else if (org != null && org.equals("Department of Atomic Energy (DAE)")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " inside executeAtRO for DAE "
                            + "  - separate block");
                    String stat_type = "coordinator_pending";
                    String to_email = "";
                    String to = "Coordinator";
                    List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("Others", "Department of Atomic Energy (DAE)", "");
                    if (coordEmails != null && !coordEmails.isEmpty()) {
                        for (String coordEmail : coordEmails) {
                            to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                        }

//                          to_email = to_email.replaceAll(",$", to_email);
                        to_email = to_email.replaceAll(",$", "");
                    }

                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, to_email, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                    if (updateRes) {
                        // update the application_type table for CA related details, line added by pr on 12thapr18
                        if (updateAppType) {
                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                        }
                        isSuccess = true;
                        isError = false;
                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + to + " - " + to_email + " !";// text changed by pr on 19thfeb18
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Application (" + reg_no + ") could not be Forwarded to the " + to + " - " + to_email + " !";
                    }
                } else if (arr[1].equals(Constants.GEM_FORM_KEYWORD)) // else if added by pr on 21stfeb18 to fwd the gem form from CA -> DA-Admin
                {  //Added by AT on 11th April 

                    isGEM = true;
                    String preferred_email1 = detailHM.get("preferred_email1").toString();
                    String preferred_email2 = detailHM.get("preferred_email2").toString();
                    String da_admin = "";
                    if (preferred_email1.contains("gem.gov.in") && preferred_email2.contains("gem.gov.in")) {
                        //da_admin = "ds.nagalakshmi@gem.gov.in";
                        da_admin = findDa("gem.gov.in");
                        //da_admin = "grm1-gem@gem.gov.in";// 5thmar2020
                    } else if (preferred_email1.contains("gembuyer.in") && preferred_email2.contains("gembuyer.in")) {
                        //da_admin = Constants.GEM_DA_ADMIN;
                        da_admin = findDa("gembuyer.in");
                    } else {
                        //da_admin = "ds.nagalakshmi@gem.gov.in,gemapplicant@gem.gov.in";
                        //da_admin = "ds.nagalakshmi@gem.gov.in,lily.prasad@gem.gov.in"; // line modified by pr on 31stjul19
                        // da_admin = "grm1-gem@gem.gov.in,lily.prasad@gem.gov.in"; // line modified by pr on 31stjul19 , modified on 5thmar2020
                        //da_admin = "grm1-gem@gem.gov.in,vp-product@gem.gov.in"; // line modified by shweta on 24feb21 , modified on 5thmar2020
                        da_admin = findDa("");
                    }
                    updateRes = updateStatus(arr[0], "da_pending", arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                    if (updateRes) {
                        // update the application_type table for CA related details, line added by pr on 12thapr18
                        if (updateAppType) {
                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                        }
                        isSuccess = true;
                        isError = false;
                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the DA-Admin - " + da_admin + " !";// text changed by pr on 19thfeb18
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Application (" + reg_no + ") could not be Forwarded to the DA-Admin - " + da_admin + " !";
                    }
                } else if (arr[1].equals(Constants.DIST_FORM_KEYWORD) && employment.toLowerCase().contains("nkn") && org != null
                        && org.equals("Tamil Nadu Veterinary and Animal Sciences University")) // else if added by pr on 3rdapr2020
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " inside executeAtRO for DLIST form NKN TAMIL NADU  - separate block");

                    List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("Nkn", "Tamil Nadu Veterinary and Animal Sciences University", "");
                    String stat_type = "coordinator_pending";
                    String to_email = "";
                    if (coordEmails != null && !coordEmails.isEmpty()) {
                        for (String coordEmail : coordEmails) {
                            to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                        }
                        // to_email = to_email.replaceAll(",$", to_email);
                        to_email = to_email.replaceAll(",$", "");
                    }

                    String to = "Coordinator";
                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, to_email, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                    if (updateRes) {
                        // update the application_type table for CA related details, line added by pr on 12thapr18
                        if (updateAppType) {
                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                        }
                        isSuccess = true;
                        isError = false;
                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + to + " - " + to_email + " !";// text changed by pr on 19thfeb18
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Application (" + reg_no + ") could not be Forwarded to the " + to + " - " + to_email + " !";
                    }
                } else if (arr[1].equals(Constants.BULKDIST_FORM_KEYWORD) && employment.toLowerCase().contains("nkn") && org != null
                        && org.equals("Tamil Nadu Veterinary and Animal Sciences University")) // else if added by pr on 3rdapr2020
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                            + " inside executeAtRO for BULKDLIST form NKN TAMIL NADU  - separate block");
                    String to = "Coordinator";
                    List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("Nkn", "Tamil Nadu Veterinary and Animal Sciences University", "");
                    String stat_type = "coordinator_pending";
                    String to_email = "";

                    if (coordEmails != null && !coordEmails.isEmpty()) {
                        for (String coordEmail : coordEmails) {
                            to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                        }
                        //  to_email = to_email.replaceAll(",$", to_email);
                        to_email = to_email.replaceAll(",$", "");
                    }

                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, to_email, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                    if (updateRes) {
                        // update the application_type table for CA related details, line added by pr on 12thapr18
                        if (updateAppType) {
                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                        }
                        isSuccess = true;
                        isError = false;
                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + to + " - " + to_email + " !";// text changed by pr on 19thfeb18
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Application (" + reg_no + ") could not be Forwarded to the " + to + " - " + to_email + " !";
                    }
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else form ");
                    if (arr[1].equals(Constants.MOB_FORM_KEYWORD)) // for imap form check for posting state if it is delhi and nic outsourced employee send directly to admin
                    {
                        if (dn.contains("gem.gov.in")) {
                            isGEM = true;
                            //String da_admin = "ds.nagalakshmi@gem.gov.in";
                            //String da_admin = "grm1-gem@gem.gov.in"; // 5thmar2020
                            String da_admin = findDa("gem.gov.in");
                            updateRes = updateStatus(arr[0], "da_pending", arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                            if (updateRes) {
                                // update the application_type table for CA related details, line added by pr on 12thapr18
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the DA-Admin - " + da_admin + " !";// text changed by pr on 19thfeb18
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be Forwarded to the DA-Admin - " + da_admin + " !";
                            }
                        } else if (dn.contains("gem-paid.gov.in")) {
                            isGEM = true;
                            //String da_admin = Constants.GEM_DA_ADMIN;
                            String da_admin = findDa("gembuyer.in");
                            updateRes = updateStatus(arr[0], "da_pending", arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                            if (updateRes) {
                                // update the application_type table for CA related details, line added by pr on 12thapr18
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the DA-Admin - " + da_admin + " !";// text changed by pr on 19thfeb18
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be Forwarded to the DA-Admin - " + da_admin + " !";
                            }
                        }
                    } else if (arr[1].equals(Constants.IMAP_FORM_KEYWORD)) // for imap form check for posting state if it is delhi and nic outsourced employee send directly to admin
                    {
                        if (dn.contains("gem.gov.in")) {
                            isGEM = true;
                            //String da_admin = "ds.nagalakshmi@gem.gov.in";
                            //String da_admin = "grm1-gem@gem.gov.in"; // 5thmar2020
                            String da_admin = findDa("gem.gov.in");
                            updateRes = updateStatus(arr[0], "da_pending", arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                            if (updateRes) {
                                // update the application_type table for CA related details, line added by pr on 12thapr18
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the DA-Admin - " + da_admin + " !";// text changed by pr on 19thfeb18
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be Forwarded to the DA-Admin - " + da_admin + " !";
                            }
                        } else if (dn.contains("gem-paid.gov.in")) {
                            isGEM = true;
                            //String da_admin = Constants.GEM_DA_ADMIN;
                            String da_admin = findDa("gembuyer.in");
                            updateRes = updateStatus(arr[0], "da_pending", arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                            if (updateRes) {
                                // update the application_type table for CA related details, line added by pr on 12thapr18
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the DA-Admin - " + da_admin + " !";// text changed by pr on 19thfeb18
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be Forwarded to the DA-Admin - " + da_admin + " !";
                            }
                        }
                    }
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " before not gem form check ");
                    // start, code added by pr on 20thfeb18
                    // check the  details for the registration number arr[0] , arr[1] contains the form name  if he has selected the state then check the state_coordinator table
                    // in case of madhya pradesh state check for department as well to select the coordinator
                    if (!isGEM) // if not NIC outsourced
                    {
                        HashMap empDetails = fetchEmploymentDetails(arr[1], arr[0]);
                        String department = empDetails.get("department").toString();
                        String state = empDetails.get("state").toString();
//                        String ministry = "";
                        String organization = "";
                        String da_admin = "";
                        String preferred_email1 = "";
                        String preferred_email2 = "";
                        String ipRequestWhichType = "";
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "state =" + add_state + "employment = "
                                + employment + "DN = " + dn + "DN for RO = " + dn_forRO);
                        if ((arr[1].equals(Constants.SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.NKN_SINGLE_FORM_KEYWORD))) {
                            preferred_email1 = detailHM.get("preferred_email1").toString();
                            preferred_email2 = detailHM.get("preferred_email2").toString();
                        } else if ((arr[1].equals(Constants.BULK_FORM_KEYWORD) || arr[1].equals(Constants.NKN_BULK_FORM_KEYWORD))) {
                            preferred_email1 = detailHM.get("preferred_email1").toString();
                            preferred_email2 = detailHM.get("preferred_email1").toString();
                        }
                        String stat_type = "", toWhichAdmin = "";

                        if (preferred_email1.contains("gem.gov.in") && preferred_email2.contains("gem.gov.in")) {
                            Set<String> bo = db.fetchBoFromEmpCoord(detailHM);
                            System.out.println("BO :: " + bo);
                            Set<String> domains = new HashSet<>();
                            for (String boId : bo) {
                                System.out.println("Bo id ::: " + boId);
                                Set<String> domain = ldap.fetchAllowedDomains(boId);
                                if (!domain.isEmpty())// line added on 12thmar2020
                                {
                                    domains.addAll(domain);
                                }
                            }
                            System.out.println("DOMAINS ::::: " + domains);

                            if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                String[] emails = preferred_email1.split(",");
                                domainAllowed = true;
                                if (!domains.isEmpty()) {
                                    for (String email : emails) {
                                        String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                        String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                        if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                            domainAllowed = false;
                                            break;
                                        }
                                    }
                                }
                                if (domainAllowed) {
                                    stat_type = "da_pending";
                                    toWhichAdmin = "Delegated Admin";
                                } else {
                                    stat_type = "coordinator_pending";
                                    toWhichAdmin = "Coordinator";
                                }
                            } else {
                                String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                if (!domains.isEmpty()) {
                                    if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                        stat_type = "da_pending";
                                        toWhichAdmin = "Delegated Admin";
                                    } else {
                                        stat_type = "coordinator_pending";
                                        toWhichAdmin = "Coordinator";
                                    }
                                } else {
                                    stat_type = "da_pending";
                                    toWhichAdmin = "Delegated Admin";
                                }
                            }
                            //da_admin = "ds.nagalakshmi@gem.gov.in";
                            //da_admin = "grm1-gem@gem.gov.in";//5thmar2020
                            da_admin = findDa("gem.gov.in");
                            updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, da_admin, sha_us_email, ""); //statRemarks added by pr on 5thjun18
                            if (updateRes) {
                                //update the application_type table for CA related details, line added by pr on 12thapr18
                                if (updateAppType) {
                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                }
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + da_admin + " ) !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Application (" + reg_no + ") could not be forwarded to " + toWhichAdmin + " ( " + da_admin + " ) !";
                            }
                        } else {
                            if (employment.equalsIgnoreCase("state") && state.equalsIgnoreCase("Himachal Pradesh")) {
                                if (fetchHimachalCoord(department)) {

                                    Set<String> bo = db.fetchBoFromEmpCoord(detailHM);
                                    System.out.println("BO :: " + bo);
                                    Set<String> domains = new HashSet<>();
                                    for (String boId : bo) {
                                        System.out.println("Bo id ::: " + boId);
                                        Set<String> domain = ldap.fetchAllowedDomains(boId);
                                        if (!domain.isEmpty())// line added on 12thmar2020
                                        {
                                            domains.addAll(domain);
                                        }
                                    }

                                    System.out.println("DOMAINS ::::: " + domains);
                                    isHimachal = true;
                                    if (arr[1].equals(Constants.SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.BULK_FORM_KEYWORD)) {

                                        System.out.println(" inside single and bulk block  detailHM is " + detailHM);

                                        //String emp_type = detailHM.get("emp_type").toString(); // line commented by pr on 27thfeb2020
                                        System.out.println(" inside single and bulk block emp_type is " + emp_type);

                                        //**********************starts09nov2020***************************
//                                        if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                            stat_type = "coordinator_pending";
//                                            toWhichAdmin = "Coordinator";
//                                        } else
                                        //**********************ends09nov2020***************************
                                        if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                            String[] emails = preferred_email1.split(",");
                                            domainAllowed = true;
                                            if (!domains.isEmpty()) {
                                                for (String email : emails) {
                                                    String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                    String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                    if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                        domainAllowed = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (domainAllowed) {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                            } else {
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                            }
                                        } else {
                                            String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                            String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                            if (!domains.isEmpty()) {
                                                if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                    stat_type = "da_pending";
                                                    toWhichAdmin = "Delegated Admin";
                                                } else {
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "Coordinator";
                                                }
                                            } else {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                            }
                                        }

                                    } else if (arr[1].equals(Constants.MOB_FORM_KEYWORD) || arr[1].equals(Constants.IMAP_FORM_KEYWORD)) {
                                        stat_type = "da_pending";
                                        toWhichAdmin = "Delegated Admin";
                                    }
                                    List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("State", "Himachal Pradesh", "");

                                    da_admin = "";
                                    if (coordEmails != null && !coordEmails.isEmpty()) {
                                        for (String coordEmail : coordEmails) {
                                            da_admin += coordEmail.split(":")[0].toLowerCase() + ",";
                                        }
                                        // da_admin = da_admin.replaceAll(",$", da_admin);
                                        da_admin = da_admin.replaceAll(",$", "");

                                    }

                                    //da_admin = "kaushal.shailender@nic.in";
                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, da_admin, sha_us_email, "");
                                    if (updateRes) {
                                        if (updateAppType) {
                                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                        }
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to " + toWhichAdmin + " - " + da_admin + " !";// text changed by pr on 19thfeb18
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "Application (" + reg_no + ") could not be Forwarded to " + toWhichAdmin + " - " + da_admin + " !";
                                    }
                                }
                            }
                            HashMap hm = null;
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                    + " inside executeAtRO 5");
                            if (!isHimachal) {
                                hm = fetchEmpCoordsAtCA(arr[0], arr[1]);
                                if (dn.contains("o=nic employees") || dn.contains("o=nic-official-id") || dn.contains("o=dio")) {
                                    if (!((arr[1].equals(Constants.SMS_FORM_KEYWORD)))) {
                                        isNIC = true;
                                        if (arr[1].equals(Constants.IP_FORM_KEYWORD)) {
                                            ipRequestWhichType = (String) detailHM.get("ip_action_request");
                                            if (ipRequestWhichType.equals("ldap")) {
                                                da_admin = "rajesh.singh@nic.in";
                                            } else if (ipRequestWhichType.equals("relay")) {
                                                da_admin = "support@gov.in";
                                            } else if (ipRequestWhichType.equals("sms")) {
                                                da_admin = "smssupport@gov.in";
                                            }
                                            stat_type = "mail-admin_pending";
                                            toWhichAdmin = "Admin";
                                        } else if (arr[1].equals(Constants.LDAP_FORM_KEYWORD)) {
                                            da_admin = "rajesh.singh@nic.in";
                                            stat_type = "mail-admin_pending";
                                            toWhichAdmin = "Admin";
                                        } else if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) {
                                            da_admin = "support@nkn.in";
                                            stat_type = "mail-admin_pending";
                                            toWhichAdmin = "Admin";
                                        } else if (arr[1].equals(Constants.DNS_FORM_KEYWORD)) {
                                            HashMap hm1 = fetchDNSDetail(arr[0]);
                                            String record_mx = "", req_other_record = "";
                                            if (hm1 != null) {
                                                if (hm1.get("record_mx") != null) {
                                                    record_mx = hm1.get("record_mx").toString();
                                                }
                                                if (hm1.get("req_other_record") != null) {
                                                    req_other_record = hm1.get("req_other_record").toString();
                                                }
                                            }
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " req_other_record " + req_other_record
                                                    + " record_mx " + record_mx);
                                            if (req_other_record.equalsIgnoreCase("mx") || (!record_mx.isEmpty())) {

                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " req_other_record " + req_other_record
                                                        + " record_mx " + record_mx + " should go to second coordinator ");

                                                da_admin = DNS_SECOND_COORD;
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                            } else {
                                                da_admin = DNS_MAILADMIN;
                                                stat_type = "mail-admin_pending";
                                                toWhichAdmin = "Admin";
                                            }
                                        } else if (arr[1].equals(Constants.WIFI_PORT_FORM_KEYWORD)) {
                                            SignUpService signUpService = new SignUpService();
                                            boolean roIsHod = signUpService.isHod(RO_email);
                                            boolean roIsHog = signUpService.isHog(RO_email);
                                            List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("Central", "Electronics and Information Technology", "Nic Employees");

                                            da_admin = "";

                                            if (coordEmails != null && !coordEmails.isEmpty()) {
                                                for (String coordEmail : coordEmails) {
                                                    String[] splittedCoordOrDaEmail = coordEmail.split(":");
                                                    if (splittedCoordOrDaEmail[1].equalsIgnoreCase("co")) {
                                                        da_admin += coordEmail.split(":")[0].toLowerCase() + ",";
                                                    }
                                                }

                                                // da_admin = da_admin.replaceAll(",$", da_admin);
                                                da_admin = da_admin.replaceAll(",$", "");
                                            }

                                            if ((roIsHod || roIsHog)) {
                                                //da_admin = "guptakk@nic.in";
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                            } else if ((!roIsHod || !roIsHog) && (dn_forRO.contains("o=nic employees") || dn_forRO.contains("o=nic-official-id"))) {
                                                //da_admin = "guptakk@nic.in";
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                            }
                                        } else {
                                            da_admin = "support@gov.in";
                                            stat_type = "mail-admin_pending";
                                            toWhichAdmin = "Admin";
                                        }
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else 4  da_admin value is " + da_admin);
                                        updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;

                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " - " + da_admin + " !";// text changed by pr on 19thfeb18
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded to the " + toWhichAdmin + " - " + da_admin + " !";
                                        }
                                    }
                                } else if ((dn_forRO.contains("o=nic employees") || dn_forRO.contains("o=nic-official-id") || dn_forRO.contains("o=dio"))) {
                                    if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) {
                                        isNIC = true;
                                        da_admin = "support@nkn.in";
                                        // end, code added by pr on 26thnov18
                                        updateRes = updateStatus(arr[0], "mail-admin_pending", arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;

                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Admin - " + da_admin + " !";// text changed by pr on 19thfeb18
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded to the mail-Admin - " + da_admin + " !";
                                        }
                                    } else if (add_state.equalsIgnoreCase("delhi") && employment.equalsIgnoreCase("central") && (dn.contains("o=nic support outsourced")
                                            || dn.contains("o=nationalknowledgenetwork"))) {
                                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else 5 ");
                                        if (!((arr[1].equals(Constants.SMS_FORM_KEYWORD)))) {
                                            isNIC = true;
                                            if (arr[1].equals(Constants.IP_FORM_KEYWORD)) {
                                                ipRequestWhichType = (String) detailHM.get("ip_action_request");
                                                if (ipRequestWhichType.equals("ldap")) {
                                                    da_admin = "rajesh.singh@nic.in";
                                                } else if (ipRequestWhichType.equals("relay")) {
                                                    da_admin = "support@gov.in";
                                                } else if (ipRequestWhichType.equals("sms")) {
                                                    da_admin = "smssupport@gov.in";
                                                }
                                                stat_type = "mail-admin_pending";
                                                toWhichAdmin = "Admin";
                                            } else if (arr[1].equals(Constants.LDAP_FORM_KEYWORD)) {
                                                da_admin = "rajesh.singh@nic.in";
                                                stat_type = "mail-admin_pending";
                                                toWhichAdmin = "Admin";
                                            } else if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) {
                                                if ((dn.contains("o=momines")) || (dn.contains("o=mom-eservice"))) {
                                                    da_admin = hm.get("co").toString();
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "NIC Coordinator";
                                                } else {
                                                    da_admin = "support@nkn.in";
                                                    stat_type = "mail-admin_pending";
                                                    toWhichAdmin = "Admin";
                                                }
                                            } else if (arr[1].equals(Constants.DNS_FORM_KEYWORD)) {
                                                HashMap hm1 = fetchDNSDetail(arr[0]);
                                                String record_mx = "", req_other_record = "";
                                                if (hm1 != null) {
                                                    if (hm1.get("record_mx") != null) {
                                                        record_mx = hm1.get("record_mx").toString();
                                                    }
                                                    if (hm1.get("req_other_record") != null) {
                                                        req_other_record = hm1.get("req_other_record").toString();
                                                    }
                                                }

                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " 1111 req_other_record " + req_other_record
                                                        + " record_mx " + record_mx);

                                                if (req_other_record.equalsIgnoreCase("mx") || (!record_mx.isEmpty())) {

                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " 1111 req_other_record " + req_other_record
                                                            + " record_mx " + record_mx + " should go to second coordinator ");

                                                    da_admin = DNS_SECOND_COORD;
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "Coordinator";
                                                } else {
                                                    da_admin = DNS_MAILADMIN;
                                                    stat_type = "mail-admin_pending";
                                                    toWhichAdmin = "Admin";
                                                }

                                            } else if (arr[1].equals(Constants.WIFI_PORT_FORM_KEYWORD)) {
                                                List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("Central", "Electronics and Information Technology", "Nic Employees");
                                                da_admin = "";

                                                if (coordEmails != null && !coordEmails.isEmpty()) {
                                                    for (String coordEmail : coordEmails) {
                                                        String[] splittedCoordOrDaEmail = coordEmail.split(":");
                                                        if (splittedCoordOrDaEmail[1].equalsIgnoreCase("co")) {
                                                            da_admin += coordEmail.split(":")[0].toLowerCase() + ",";
                                                        }
                                                    }

                                                    //da_admin = da_admin.replaceAll(",$", da_admin);
                                                    da_admin = da_admin.replaceAll(",$", "");
                                                }

                                                //da_admin = "guptakk@nic.in";
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                            } //*****************************DOR-EXT ***************************************//                    
                                            //                                            else if (arr[1].equals(Constants.DOR_EXT_FORM_KEYWORD)) {
                                            //                                                 da_admin = "support@gov.in";
                                            //                                                    stat_type = "mail-admin_pending";
                                            //                                                    toWhichAdmin = "Admin";
                                            ////                                                if (hm != null && hm.size() > 0) {
                                            ////                                                    for (Object k : hm.keySet()) {
                                            ////                                                        String key = k.toString(); // type whether da or co
                                            ////                                                        if (key.equals("da")) {
                                            ////                                                            stat_type = "da_pending";
                                            ////                                                            toWhichAdmin = "Delegated Admin";
                                            ////                                                            da_admin = hm.get("da").toString();
                                            ////                                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "*******************da_admin :::::::::::::::::: " + da_admin);
                                            ////                                                            break;
                                            ////                                                        } else if (key.equals("co")) {
                                            ////                                                            stat_type = "coordinator_pending";
                                            ////                                                            toWhichAdmin = "Coordinator";
                                            ////                                                            da_admin = hm.get("co").toString();
                                            ////                                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "*******************da_admin :::::::::::::::::: " + da_admin);
                                            ////                                                            break;
                                            ////                                                        } else {
                                            ////                                                            stat_type = "support_pending";
                                            ////                                                            toWhichAdmin = "Support";
                                            ////                                                            da_admin = "support@nic.in";
                                            ////                                                        }
                                            ////                                                    }
                                            ////                                                } else {
                                            ////                                                    stat_type = "support_pending";
                                            ////                                                    toWhichAdmin = "Support";
                                            ////                                                    da_admin = "support@nic.in";
                                            ////                                                }
                                            //                                                System.out.println("*******************stat_type :::::::::::::::::: " + stat_type);
                                            //                                            } //************************DOR-EXT enddddddddd************************************//                 
                                            else {
                                                stat_type = "mail-admin_pending";
                                                toWhichAdmin = "Admin";
                                                da_admin = "support@nic.in";
                                            }
                                            // end, code added by pr on 26thnov18
                                            updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                            if (updateRes) {
                                                //update the application_type table for CA related details, line added by pr on 12thapr18
                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;

                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " - " + da_admin + " !";// text changed by pr on 19thfeb18
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded to the " + toWhichAdmin + " - " + da_admin + " !";
                                            }
                                        } else {
                                            isNIC = true;
                                            da_admin = "smssupport@nic.in";
                                            stat_type = "support_pending";
                                            toWhichAdmin = "SMS Support";
                                            // end, code added by pr on 26thnov18
                                            updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                            if (updateRes) {
                                                //update the application_type table for CA related details, line added by pr on 12thapr18
                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;

                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " - " + da_admin + " !";// text changed by pr on 19thfeb18
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded to the " + toWhichAdmin + " - " + da_admin + " !";
                                            }
                                        }
                                    }
                                } else if ((dn.contains("o=nic support outsourced") || dn.contains("o=nationalknowledgenetwork")) && (dn_forRO.contains("o=nic support outsourced")
                                        || dn_forRO.contains("o=nationalknowledgenetwork"))) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                            + " inside executeAtRO 8");
                                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else 6 ");
                                    isNIC = true;

                                    String toEmailAddress = "";
                                    if (arr[1].equals(Constants.SMS_FORM_KEYWORD)) {
                                        toEmailAddress = "smssupport@gov.in";
                                        stat_type = "support_pending";
                                        toWhichAdmin = "SMS Support";
                                    } else if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) {
                                        if ((dn.contains("o=momines")) || (dn.contains("o=mom-eservice"))) {
                                            toEmailAddress = hm.get("co").toString();
                                            stat_type = "coordinator_pending";
                                            toWhichAdmin = "NIC Coordinator";
                                        } else {
                                            toEmailAddress = "support@nkn.in";
                                            stat_type = "support_pending";
                                            toWhichAdmin = "WiFi Support";
                                        }
                                    } else if (arr[1].equals(Constants.DNS_FORM_KEYWORD)) {
                                        HashMap hm1 = fetchDNSDetail(arr[0]);
                                        String record_mx = "", req_other_record = "";
                                        if (hm1 != null) {
                                            if (hm1.get("record_mx") != null) {
                                                record_mx = hm1.get("record_mx").toString();
                                            }
                                            if (hm1.get("req_other_record") != null) {
                                                req_other_record = hm1.get("req_other_record").toString();
                                            }
                                        }

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " 2222 req_other_record " + req_other_record
                                                + " record_mx " + record_mx);

                                        if (req_other_record.equalsIgnoreCase("mx") || (!record_mx.isEmpty())) {

                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " 2222 req_other_record " + req_other_record
                                                    + " record_mx " + record_mx + " should go to second coordinator ");

                                            toEmailAddress = DNS_SECOND_COORD;
                                            stat_type = "coordinator_pending";
                                            toWhichAdmin = "Coordinator";
                                        } else {
                                            toEmailAddress = "support@gov.in";
                                            stat_type = "support_pending";
                                            toWhichAdmin = "DNS Support";
                                        }
                                    } else if (arr[1].equals(Constants.WIFI_PORT_FORM_KEYWORD)) {
                                        toEmailAddress = "guptakk@nic.in";
                                        stat_type = "coordinator_pending";
                                        toWhichAdmin = "Coordinator";
                                    } else {
                                        toEmailAddress = "support@gov.in";
                                        stat_type = "support_pending";
                                        toWhichAdmin = "Support";
                                    }
                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, toEmailAddress, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                    if (updateRes) {
                                        //update the application_type table for CA related details, line added by pr on 12thapr18
                                        if (updateAppType) {
                                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                        }
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + toEmailAddress + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "Application (" + reg_no + ") could not be Forwarded !";
                                    }
                                } else if (arr[1].equals(Constants.WIFI_FORM_KEYWORD) && (employment.equalsIgnoreCase("central") && empDetails.get("ministry").toString().equalsIgnoreCase("mines") && empDetails.get("department").toString().equalsIgnoreCase("Ministry of Mines") && hm.get("sendtocoord").toString().equalsIgnoreCase("true"))) {
                                    isNIC = true;
                                    //if ((dn.contains("momines")) || (dn.contains("mom-eservice"))) {
                                    da_admin = hm.get("co").toString();
                                    stat_type = "coordinator_pending";
                                    toWhichAdmin = "NIC Coordinator";
//                                    } else {
//                                        da_admin = "support@nkn.in";
//                                        stat_type = "support_pending";
//                                        toWhichAdmin = "WiFi Support";
//                                    }
                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, da_admin, sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                    if (updateRes) {
                                        //update the application_type table for CA related details, line added by pr on 12thapr18
                                        if (updateAppType) {
                                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                        }
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + da_admin + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "Application (" + reg_no + ") could not be Forwarded !";
                                    }
                                }
                            }

                            if (!isNIC && !isHimachal) {
                                String val = "";

                                boolean isKeyAvailable = false;
                                //HashMap hm = fetchEmpCoordsAtCA(arr[0], arr[1]);
                                if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) {
                                    if (hm.isEmpty()) {
                                        val = "support@nkn.in";
                                    } else if (hm.containsKey("co")) {
                                        String coordEmail = hm.get("co").toString();
                                        if (isSupportEmail(coordEmail)) {
                                            val = "support@nkn.in";
                                        }
                                    }
                                }
                                if (empDetails != null && empDetails.get("employment") != null) {
                                    employment = empDetails.get("employment").toString();
                                    state = empDetails.get("state").toString();
                                    department = empDetails.get("department").toString();
                                    ministry = empDetails.get("ministry").toString();
                                    organization = empDetails.get("organization").toString();

                                    if (employment.equalsIgnoreCase("state")) {
                                        ministry = state;
                                    } else if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                                        ministry = ministry;
                                    } else {
                                        ministry = organization;
                                    }
                                }
                                Set<String> bo = db.fetchBoFromEmpCoord(detailHM);
                                System.out.println("BO :: " + bo);
                                Set<String> domains = new HashSet<>();
                                for (String boId : bo) {
                                    System.out.println("Bo id ::: " + boId);
                                    Set<String> domain = ldap.fetchAllowedDomains(boId);
                                    if (domain.size() > 0)// line added on 12thmar2020
                                    {
                                        domains.addAll(domain);
                                    }
                                }

                                System.out.println("DOMAINS ::::: " + domains);
                                if (arr[1].equals(Constants.BULK_FORM_KEYWORD) || arr[1].equals(Constants.SINGLE_FORM_KEYWORD)) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                            + " inside executeAtRO 10 detailHM value here is  " + detailHM);

                                    if (hm != null && hm.size() > 0) {
                                        String fetchedDomain = fetchDomain(employment, ministry, department);

                                        if (fetchedDomain.equalsIgnoreCase("delhi.gov.in")) {
                                            String coEmail = "", daEmail = "";
                                            daEmail = (String) hm.get("da");
                                            coEmail = (String) hm.get("co");

                                            if (coEmail != null && (coEmail.equals("support@nic.in") || coEmail.equals("support@gov.in") || coEmail.equals("support@dummy.nic.in"))) {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            } else if (coEmail != null && !coEmail.isEmpty()) {
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                                val = coEmail;
                                            } else if (daEmail != null && !daEmail.isEmpty()) {
                                                val = daEmail;
                                                if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                                    String[] emails = preferred_email1.split(",");
                                                    domainAllowed = true;
                                                    if (domains.size() > 0) {
                                                        for (String email : emails) {
                                                            String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                            String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                            if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                domainAllowed = false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (domainAllowed) {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "Delegated Admin";
                                                    } else {
                                                        stat_type = "coordinator_pending";
                                                        toWhichAdmin = "Coordinator";
                                                    }
                                                } else {
                                                    String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                    String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                    if (domains.size() > 0) {
                                                        if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        } else {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        }
                                                    } else {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "Delegated Admin";
                                                    }
                                                }
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            }
                                        } else if (employment.equalsIgnoreCase("State") && state.equalsIgnoreCase("Punjab")) {
                                            String coEmail = "", daEmail = "";
                                            if (add_state.equalsIgnoreCase("Punjab")) {
                                                daEmail = (String) hm.get("da");
                                                coEmail = (String) hm.get("co");

                                                if (coEmail != null && (coEmail.equals("support@nic.in") || coEmail.equals("support@gov.in") || coEmail.equals("support@dummy.nic.in"))) {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nic.in";
                                                } else if (coEmail != null && !coEmail.isEmpty()) {
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "Coordinator";
                                                    val = coEmail;
                                                } else if (daEmail != null && !daEmail.isEmpty()) {
                                                    val = daEmail;

                                                    //**********************starts09nov2020***************************
//                                                    if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                                        stat_type = "coordinator_pending";
//                                                        toWhichAdmin = "Coordinator";
//                                                    } else 
                                                    //**********************ends09nov2020***************************
                                                    if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                                        String[] emails = preferred_email1.split(",");
                                                        domainAllowed = true;
                                                        if (domains.size() > 0) {
                                                            for (String email : emails) {
                                                                String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                                String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                                if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                    domainAllowed = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (domainAllowed) {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        } else {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        }
                                                    } else {
                                                        String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                        String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                        if (domains.size() > 0) {
                                                            if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                                stat_type = "da_pending";
                                                                toWhichAdmin = "Delegated Admin";
                                                            } else {
                                                                stat_type = "coordinator_pending";
                                                                toWhichAdmin = "Coordinator";
                                                            }
                                                        } else {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        }
                                                    }
                                                } else {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nic.in";
                                                }
                                            } else {
                                                daEmail = (String) hm.get("da");
                                                val = daEmail;
                                                //**********************starts09nov2020***************************
//                                                if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                                    stat_type = "coordinator_pending";
//                                                    toWhichAdmin = "Coordinator";
//                                                } else 
//                                               //**********************ends09nov2020*************************** 

                                                if (preferred_email1.contains(",") || preferred_email2.contains(",") && (daEmail != null && !daEmail.isEmpty())) {
                                                    String[] emails = preferred_email1.split(",");
                                                    domainAllowed = true;
                                                    if (domains.size() > 0) {
                                                        for (String email : emails) {
                                                            String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                            String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                            if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                domainAllowed = false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (domainAllowed) {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "Delegated Admin";
                                                    } else {
                                                        stat_type = "coordinator_pending";
                                                        toWhichAdmin = "Coordinator";
                                                    }
                                                } else if (daEmail != null && !daEmail.isEmpty()) {
                                                    String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                    String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                    if (domains.size() > 0) {
                                                        if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        } else {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";

                                                        }
                                                    } else {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "Delegated Admin";
                                                    }
                                                } else {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nic.in";
                                                }
                                            }
                                        } else if (employment.equalsIgnoreCase("State") && state.equalsIgnoreCase("Maharashtra") && add_state.equalsIgnoreCase("Maharashtra")) {
                                            String coEmail = "", daEmail = "";
                                            String idType = detailHM.get("id_type").toString();
                                            daEmail = (String) hm.get("da");
                                            coEmail = (String) hm.get("co");
                                            if (coEmail != null) {
                                                coEmail = coEmail.toLowerCase();
                                            }
                                            if (daEmail != null && !daEmail.isEmpty()) {
                                                daEmail = daEmail.toLowerCase();
                                                //**********************starts09nov2020***************************
                                                if (idType.equals("id_name")) {
                                                    if (daEmail.contains("usecy1-dit@mah.gov.in")) {
                                                        if (daEmail.contains(",")) {
                                                            String[] dAs = daEmail.split(",");
                                                            List<String> dAsInList = Arrays.asList(dAs);
                                                            dAsInList.remove("usecy1-dit@mah.gov.in");
                                                            daEmail = String.join(",", dAsInList);
                                                        } else {
                                                            daEmail = "smita.joshi75@nic.in";
                                                        }
                                                    }
                                                }

                                                //**********************ends09nov2020*****************************
                                                val = daEmail;

                                                System.out.println(" emp_type value is " + emp_type);
                                                //**********************starts09nov2020***************************
//                                                if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                                    stat_type = "coordinator_pending";
//                                                    toWhichAdmin = "Coordinator";
//                                                } else
                                                //**********************ends09nov2020***************************  
                                                if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                                    String[] emails = preferred_email1.split(",");
                                                    domainAllowed = true;
                                                    if (domains.size() > 0) {
                                                        for (String email : emails) {
                                                            String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                            String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                            if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                domainAllowed = false;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    if (domainAllowed) {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "Delegated Admin";
                                                    } else {
                                                        stat_type = "coordinator_pending";
                                                        toWhichAdmin = "Coordinator";
                                                    }
                                                } else {
                                                    String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                    String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                    if (domains.size() > 0) {
                                                        if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        } else {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        }
                                                    } else {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "Delegated Admin";
                                                    }
                                                }
                                            } else if (coEmail != null && !coEmail.isEmpty()) {

                                                //  if (coEmail.equalsIgnoreCase("yogesh.tompe@gov.in") || coEmail.equalsIgnoreCase("smita.joshi75@nic.in") || coEmail.equalsIgnoreCase("yogesh.tompe@nic.in") || coEmail.equalsIgnoreCase("smita.joshi75@nic.in")) {
                                                if (coEmail.equalsIgnoreCase("suraj.pangarkar@gov.in") || coEmail.equalsIgnoreCase("smita.joshi75@nic.in") || coEmail.equalsIgnoreCase("suraj.pangarkar@gov.in") || coEmail.equalsIgnoreCase("smita.joshi75@nic.in")) {
                                                    if (idType.equals("id_desig")) {
                                                        coEmail = "suraj.pangarkar@gov.in";
                                                    } else if (idType.equals("id_name")) {
                                                        coEmail = "smita.joshi75@nic.in";
                                                    }
                                                }
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                                val = coEmail;
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            }
                                        } else {
                                            for (Object k : hm.keySet()) {
                                                String key = k.toString(); // type whether da or co
                                                val = hm.get(k).toString().toLowerCase(); // emails comma separated
                                                if (key.equals("da")) {
                                                    isKeyAvailable = true;
                                                    // check if das are comng out to be support@nic.in or any aliases or that then send directly to the mailadmin
                                                    if (val.equals("support@nic.in") || val.equals("support@gov.in") || val.equals("support@dummy.nic.in") || val.equals("support@nkn.in")) {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "Support";
                                                    } else {
                                                        stat_type = "da_pending";
                                                        toWhichAdmin = "DA-Admin";
                                                        // start, code added by pr on 25thsep18, in case of contractual employee for bulk and single forms , dont send to da but to da as coordinator
                                                        //String emp_type = detailHM.get("emp_type").toString(); // line commented by pr on 27thfeb2020

                                                        //**********************starts09nov2020***************************
//                                                        if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                                            stat_type = "coordinator_pending";
//                                                            toWhichAdmin = "Coordinator";
//                                                        } else 
                                                        //**********************ends09nov2020***************************    
                                                        if (preferred_email1.contains(",") || preferred_email2.contains(",") && !val.isEmpty()) {
                                                            String[] emails = preferred_email1.split(",");
                                                            domainAllowed = true;
                                                            if (domains.size() > 0) {
                                                                for (String email : emails) {
                                                                    String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                                    String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                                    if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                        domainAllowed = false;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                            if (domainAllowed) {
                                                                stat_type = "da_pending";
                                                                toWhichAdmin = "Delegated Admin";
                                                            } else {
                                                                stat_type = "coordinator_pending";
                                                                toWhichAdmin = "Coordinator";
                                                            }
                                                        } else if (!val.isEmpty()) {
                                                            String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                            String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                            if (domains.size() > 0) {
                                                                if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                                    domainAllowed = true;
                                                                    stat_type = "da_pending";
                                                                    toWhichAdmin = "Delegated Admin";
                                                                } else {
                                                                    stat_type = "coordinator_pending";
                                                                    toWhichAdmin = "Coordinator";
                                                                }
                                                            } else {
                                                                stat_type = "da_pending";
                                                                toWhichAdmin = "Delegated Admin";
                                                            }
                                                        } else {
                                                            stat_type = "support_pending";
                                                            toWhichAdmin = "Support";
                                                            val = "support@nic.in";
                                                        }
                                                    }
                                                } else if (key.equals("co")) {
                                                    isKeyAvailable = true;
                                                    if ((commaSeparatedAliases.contains(val.toLowerCase())) || (val.contains(RO_email.toLowerCase()))) {
                                                        if (add_state.equalsIgnoreCase("Himachal Pradesh")) {
                                                            String adminCommaSeparated = (String) hm.get("da");
                                                            if (adminCommaSeparated != null && !adminCommaSeparated.isEmpty()) {
                                                                if (adminCommaSeparated.contains("kaushal.shailender@nic.in")) {
                                                                    //**********************starts09nov2020***************************
//                                                                    if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                                                        stat_type = "coordinator_pending";
//                                                                        toWhichAdmin = "Coordinator";
//                                                                    } else 
                                                                    //**********************ends09nov2020*************************** 
                                                                    if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                                                        String[] emails = preferred_email1.split(",");
                                                                        domainAllowed = true;
                                                                        if (domains.size() > 0) {
                                                                            for (String email : emails) {
                                                                                String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                                                String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                                                if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                                    domainAllowed = false;
                                                                                    break;
                                                                                }
                                                                            }
                                                                        }
                                                                        if (domainAllowed) {
                                                                            stat_type = "da_pending";
                                                                            toWhichAdmin = "Delegated Admin";
                                                                        } else {
                                                                            stat_type = "coordinator_pending";
                                                                            toWhichAdmin = "Coordinator";
                                                                        }
                                                                    } else {
                                                                        String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                                        String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                                        if (domains.size() > 0) {
                                                                            if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                                                domainAllowed = true;
                                                                                stat_type = "da_pending";
                                                                                toWhichAdmin = "Delegated Admin";
                                                                            } else {
                                                                                stat_type = "coordinator_pending";
                                                                                toWhichAdmin = "Coordinator";
                                                                            }
                                                                        } else {
                                                                            stat_type = "da_pending";
                                                                            toWhichAdmin = "Delegated Admin";
                                                                        }
                                                                    }
                                                                    val = "kaushal.shailender@nic.in";
                                                                }
                                                            }
                                                        } else {
                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "Admin";
                                                            val = hm.get("madmin").toString(); // line added by pr on 5thmar2020
                                                            if (val == null || val.isEmpty()) {
                                                                val = "support@gov.in";
                                                            }
                                                        }
                                                    } else if (add_state.equalsIgnoreCase("Himachal Pradesh")) {
                                                        String adminCommaSeparated = (String) hm.get("da");
                                                        if (adminCommaSeparated != null && !adminCommaSeparated.isEmpty()) {
                                                            if (adminCommaSeparated.contains("kaushal.shailender@nic.in")) {
                                                                //**********************starts09nov2020***************************
//                                                                if (emp_type.equals("emp_contract") || emp_type.equals("consultant")) {
//                                                                    stat_type = "coordinator_pending";
//                                                                    toWhichAdmin = "Coordinator";
//                                                                } else 
                                                                //**********************ends09nov2020***************************    
                                                                if (preferred_email1.contains(",") || preferred_email2.contains(",")) {

                                                                    String[] emails = preferred_email1.split(",");
                                                                    domainAllowed = true;
                                                                    for (String email : emails) {
                                                                        String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                                        String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                                        if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                            domainAllowed = false;
                                                                            break;
                                                                        }
                                                                    }
                                                                    if (domainAllowed) {
                                                                        stat_type = "da_pending";
                                                                        toWhichAdmin = "Delegated Admin";
                                                                    } else {
                                                                        stat_type = "coordinator_pending";
                                                                        toWhichAdmin = "Coordinator";
                                                                    }
                                                                } else {
                                                                    String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                                    String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();

                                                                    if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                                        domainAllowed = true;
                                                                        stat_type = "da_pending";
                                                                        toWhichAdmin = "Delegated Admin";
                                                                    } else {
                                                                        stat_type = "coordinator_pending";
                                                                        toWhichAdmin = "Coordinator";
                                                                    }
                                                                }
                                                                val = "kaushal.shailender@nic.in";
                                                            } else {
                                                                stat_type = "coordinator_pending";
                                                                toWhichAdmin = "Coordinator";
                                                            }
                                                        }
                                                    } else {
                                                        stat_type = "coordinator_pending";
                                                        toWhichAdmin = "Coordinator";
                                                    }
                                                }
                                                if (isKeyAvailable) {
                                                    break;
                                                }
                                            }
                                        }

                                        // start, code added by pr on 9thmar2020
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                                + " inside executeatRO and BEFORE val value is null or empty val value is " + val);

                                        if (val == null || val.isEmpty() || stat_type == null || stat_type.isEmpty()) {

                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                                    + " inside executeatRO and val value is null or empty ");

                                            // send the request to support as support_pending
                                            val = sup_email;

                                            stat_type = "support_pending";

                                            toWhichAdmin = "Support";
                                        } else if ((val.equals("support@gov.in") || val.equals("support@nic.in")) && stat_type.equals("coordinator_pending"))// else if added by pr on 3thapr2020
                                        {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                                    + " inside executeatRO and val value is " + val + " stat_type is " + stat_type);

                                            val = sup_email;

                                            stat_type = "support_pending";

                                            toWhichAdmin = "Support";
                                        }

                                        // end, code added by pr on 9thmar2020                                        
                                        updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");//statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;

                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded !";
                                        }
                                    } else // if no record found then send to the support
                                    {
                                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside non dns form ");
                                        String toEmailAddress = "";
                                        if (arr[1].equals(Constants.SMS_FORM_KEYWORD)) {
                                            toEmailAddress = "smssupport@gov.in";
                                        } else {
                                            toEmailAddress = "support@gov.in";
                                        }
                                        updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, "", sha_us_email, ""); //statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Support ( " + toEmailAddress + " ) !";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded !";
                                        }
                                    }
                                } else if (arr[1].equals(Constants.NKN_SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.NKN_BULK_FORM_KEYWORD)) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                            + " inside executeAtRO 111");
                                    if (hm != null && hm.size() > 0) {
                                        for (Object k : hm.keySet()) {
                                            String key = k.toString(); // type whether da or co
                                            val = hm.get(k).toString().toLowerCase(); // emails comma separated
                                            if (key.equals("da")) {
                                                isKeyAvailable = true;
                                                // check if das are comng out to be support@nic.in or any aliases or that then send directly to the mailadmin
                                                if (val.equals("support@nic.in") || val.equals("support@gov.in") || val.equals("support@dummy.nic.in") || val.equals("support@nkn.in")) {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                } else {
                                                    stat_type = "da_pending";
                                                    toWhichAdmin = "DA-Admin";
                                                    // start, code added by pr on 25thsep18, in case of contractual employee for bulk and single forms , dont send to da but to da as coordinator
                                                    if (preferred_email1.contains(",") || preferred_email2.contains(",") && !val.isEmpty()) {
                                                        String[] emails = preferred_email1.split(",");
                                                        domainAllowed = true;
                                                        if (domains.size() > 0) {
                                                            for (String email : emails) {
                                                                String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                                String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                                if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                                    domainAllowed = false;
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                        if (domainAllowed) {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        } else {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        }
                                                    } else if (!val.isEmpty()) {
                                                        String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                                        String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                                        if (domains.size() > 0) {
                                                            if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                                domainAllowed = true;
                                                                stat_type = "da_pending";
                                                                toWhichAdmin = "Delegated Admin";
                                                            } else {
                                                                stat_type = "coordinator_pending";
                                                                toWhichAdmin = "Coordinator";
                                                            }
                                                        } else {
                                                            stat_type = "da_pending";
                                                            toWhichAdmin = "Delegated Admin";
                                                        }
                                                    } else {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "Support";
                                                        val = "support@nic.in";
                                                    }
                                                }
                                            } else if (key.equals("co")) {
                                                isKeyAvailable = true;
                                                if ((commaSeparatedAliases.contains(val.toLowerCase())) || (val.contains(RO_email.toLowerCase()))) {
                                                    stat_type = "mail-admin_pending";
                                                    toWhichAdmin = "Admin";
                                                    val = hm.get("madmin").toString(); // line added by pr on 5thmar2020
                                                    if (val == null || val.isEmpty()) {
                                                        val = "support@gov.in";
                                                    }
                                                } else {
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "Coordinator";
                                                }
                                            }
                                            if (isKeyAvailable) {
                                                break;
                                            }
                                        }

                                        updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");//statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;

                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded !";
                                        }
                                    } else // if no record found then send to the support
                                    {
                                        String toEmailAddress = "support@gov.in";

                                        updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, "", sha_us_email, ""); //statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Support ( " + toEmailAddress + " ) !";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded !";
                                        }
                                    }
                                } else if (arr[1].equals(Constants.MOB_FORM_KEYWORD) || arr[1].equals(Constants.IMAP_FORM_KEYWORD)) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                                            + " inside executeAtRO 11");
                                    if (hm != null && hm.size() > 0) {
                                        String coEmail = "", daEmail = "";
                                        daEmail = (String) hm.get("da");
                                        coEmail = (String) hm.get("co");

                                        String fetchedDomain = fetchDomain(employment, ministry, department);
                                        if (employment.equalsIgnoreCase("State") && state.equalsIgnoreCase("Punjab")) {
                                            if (add_state.equalsIgnoreCase("Punjab")) {
                                                if (coEmail != null && (coEmail.equals("support@nic.in") || coEmail.equals("support@gov.in") || coEmail.equals("support@dummy.nic.in"))) {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nic.in";
                                                } else if (coEmail != null && !coEmail.isEmpty()) {
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "Coordinator";
                                                    val = coEmail;
                                                } else if (daEmail != null && !daEmail.isEmpty()) {
                                                    stat_type = "da_pending";
                                                    toWhichAdmin = "Delegated Admin";
                                                    val = daEmail;
                                                } else {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nic.in";
                                                }
                                            } else if (daEmail != null && !daEmail.isEmpty()) {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                                val = daEmail;
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            }
                                        } else if (fetchedDomain.equalsIgnoreCase("delhi.gov.in")) {
                                            if (coEmail != null && (coEmail.equals("support@nic.in") || coEmail.equals("support@gov.in") || coEmail.equals("support@dummy.nic.in"))) {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            } else if (coEmail != null && !coEmail.isEmpty()) {
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                                val = coEmail;
                                            } else if (daEmail != null && !daEmail.isEmpty()) {
                                                val = daEmail;
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            }
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "******empDetails::::::::::::::::::" + empDetails);
                                            Set<String> emp_bos = db.fetchBoFromEmpCoord(empDetails);
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "******emp_bos::::::::::::::::::" + emp_bos);
                                            Set<String> ldap_bos = ldap.fetchBos(empDetails.get("auth_email").toString());
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "********auth_email::::::::*********" + empDetails.get("auth_email").toString());
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "******ldap_bos::::::::::::::::::" + ldap_bos);
                                            boolean matchBos = CollectionUtils.containsAny(emp_bos, ldap_bos);
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "******matchBos::::::::::::::::::" + matchBos);
                                            if (matchBos) {
                                                if (hm.containsKey("da") && hm.get("da") != null) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "******DA::::::::::::::::::" + hm.get("da").toString());
                                                    stat_type = "da_pending";
                                                    toWhichAdmin = "Delegated Admin";
                                                    val = hm.get("da").toString();
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "*******************val :::::::::::::::::: " + val);
                                                } else if (hm.containsKey("co") && hm.get("co") != null) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "******CO::::::::::::::::::" + hm.get("co").toString());
                                                    stat_type = "coordinator_pending";
                                                    toWhichAdmin = "Coordinator";
                                                    val = hm.get("co").toString();
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "*******************val :::::::::::::::::: " + val);
                                                    if ((commaSeparatedAliases.contains(val.toLowerCase())) || (val.contains(RO_email.toLowerCase()))) {
                                                        if (add_state.equalsIgnoreCase("Himachal Pradesh")) {
                                                            String adminCommaSeparated = (String) hm.get("da");
                                                            if (adminCommaSeparated != null && !adminCommaSeparated.isEmpty()) {
                                                                if (adminCommaSeparated.contains("kaushal.shailender@nic.in")) {
                                                                    stat_type = "da_pending";
                                                                    toWhichAdmin = "Delegated Admin";
                                                                    val = "kaushal.shailender@nic.in";
                                                                }
                                                            } else {
                                                                stat_type = "mail-admin_pending";
                                                                toWhichAdmin = "Admin";
                                                                val = "support@nic.in";
                                                            }
                                                        } else {
                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "Admin";
                                                            val = hm.get("madmin").toString(); // line added by pr on 5thmar2020
                                                            if (val == null || val.isEmpty()) {
                                                                val = "support@nic.in";
                                                            }
                                                        }
                                                    } else if (add_state.equalsIgnoreCase("Himachal Pradesh")) {
                                                        String adminCommaSeparated = (String) hm.get("da");
                                                        if (adminCommaSeparated != null && !adminCommaSeparated.isEmpty()) {
                                                            if (adminCommaSeparated.contains("kaushal.shailender@nic.in")) {
                                                                stat_type = "da_pending";
                                                                toWhichAdmin = "Delegated Admin";
                                                                val = hm.get("da").toString();
                                                            } else if (!val.isEmpty()) {
                                                                stat_type = "coordinator_pending";
                                                                toWhichAdmin = "Coordinator";
                                                            } else {
                                                                stat_type = "support_pending";
                                                                toWhichAdmin = "Support";
                                                                val = "support@nic.in";
                                                            }
                                                        } else if (!val.isEmpty()) {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        } else {
                                                            stat_type = "support_pending";
                                                            toWhichAdmin = "Support";
                                                            val = "support@nic.in";
                                                        }
                                                    } else if (!val.isEmpty()) {
                                                        stat_type = "coordinator_pending";
                                                        toWhichAdmin = "Coordinator";
                                                    } else {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "Support";
                                                        val = "support@nic.in";
                                                    }
                                                } else {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nic.in";
                                                }
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nic.in";
                                            }
                                        }
                                        // start, code added by pr on 9thjun2020
                                        if (stat_type.equals("") || val.equals("")) {
                                            stat_type = "support_pending";
                                            toWhichAdmin = "Support";
                                            val = "support@nic.in";
                                        }
                                        // end, code added by pr on 9thjun2020

                                        updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");//statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded !";
                                        }
                                    } else // if no record found then send to the support
                                    {

                                        String toEmailAddress = "support@nic.in";

                                        updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, "", sha_us_email, ""); //statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Support ( " + toEmailAddress + " ) !";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded !";
                                        }
                                    }
                                } else if (arr[1].equals(Constants.LDAP_FORM_KEYWORD) || arr[1].equals(Constants.RELAY_FORM_KEYWORD) || arr[1].equals(Constants.IP_FORM_KEYWORD)) {

                                    // start, code added by pr on 9thjul19
                                    boolean isIPSMS = false;

                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " INSIDE IP LDAP RELAY ");

                                    if (arr[1].equals(Constants.IP_FORM_KEYWORD)) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " INSIDE ONLY IP ");

                                        // check the value of ip_action_request from ip_registration table if it is sms then send to smssupport@gov.in as support pending
                                        String ip_action_request = detailHM.get("ip_action_request").toString();

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " INSIDE ONLY IP ip_action_request value is " + ip_action_request);

                                        if (ip_action_request.equals("sms")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " INSIDE ONLY IP ip_action_request  INSIDE SMS ");

                                            isIPSMS = true;

                                            stat_type = "support_pending";
                                            toWhichAdmin = "Support";
                                            val = "smssupport@nic.in";

                                            updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, val, sha_us_email, "");

                                            if (updateRes) {

                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;

                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                                            }

                                        }
                                    }

                                    if (!isIPSMS) {

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " NOT isIPSMS ");

                                        // end, code added by pr on 9thjul19
                                        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside relay hm.size() is " + hm.size());
                                        if (hm != null && hm.size() > 0) {
                                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside relay  hm not null");
                                            for (Object k : hm.keySet()) // loop will iterate only once
                                            {
                                                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside relay  hm not null for loop");
                                                String key = k.toString(); // type whether da or co
                                                val = hm.get(k).toString(); // emails comma separated
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside relay  hm not null for loop KEY is " + key);
                                                if (key.equals("da")) {
                                                    key = "co";
                                                }

                                                //Forward issue Application number RELAY-FORM202112210003 could not be forwarded further to NIC Delhi team. in relay form on 28 dec 2021
                                                if (key.equals("madmin")) {
                                                    if (val != null && !val.isEmpty() && val.equalsIgnoreCase("support@nic.in")) {
                                                        stat_type = "support_pending";
                                                    }
                                                }
                                                //Forward issue Application number RELAY-FORM202112210003 could not be forwarded further to NIC Delhi team. in relay form on 28 dec 2021

                                                if (key.equals("co")) {
                                                    isKeyAvailable = true;
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside co " + val);
                                                    if (!val.isEmpty()) {
                                                        if (val.contains(auth_email) || val.contains(RO_email)) {
                                                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside if auth mobile or ro mobile ");
                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "Admin";

                                                            /*if (hm.get("da") != null) {
                                                        val = hm.get("da").toString();
                                                    } else if (hm.get("madmin") != null) {
                                                        val = hm.get("madmin").toString();
                                                    } else {
                                                        val = "support@gov.in";
                                                    }*/
                                                            // above code commented below added by pr on 25thun19
                                                            val = "support@gov.in";

                                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " after val not null value of val is " + val);
                                                        } else {
                                                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else for auth mobile not contains ");
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        }
                                                    } else {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "Support";
                                                        val = "support@nic.in";
                                                    }
                                                }
                                                if (isKeyAvailable) {
                                                    break;
                                                }
                                            }
                                            if ((val.equals("support@nic.in") || val.equals("support@gov.in") || val.equals("support@dummy.nic.in"))) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " 1 before update status in relay stat_type " + stat_type + " to user null ");
                                                if (stat_type.equalsIgnoreCase("coordinator_pending")) {
                                                    toWhichAdmin = "Support";
                                                    if (arr[1].equals(Constants.IP_FORM_KEYWORD)) {
                                                        ipRequestWhichType = (String) detailHM.get("ip_action_request");
                                                        if (ipRequestWhichType.equals("sms")) {
                                                            val = "smssupport@gov.in";
                                                        }
                                                    }
                                                    updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, val, sha_us_email, "");//statRemarks modified by pr on 5thjuly18 // commented on 9thjul18 for testing , to be uncommented in production
                                                } else if (stat_type.equalsIgnoreCase("mail-admin_pending")) {
                                                    if (arr[1].equals(Constants.IP_FORM_KEYWORD)) {
                                                        ipRequestWhichType = (String) detailHM.get("ip_action_request");
                                                        if (ipRequestWhichType.equals("sms")) {
                                                            val = "smssupport@gov.in";
                                                            toWhichAdmin = "Support";
                                                            stat_type = "support_pending";
                                                        } else if (ipRequestWhichType.equals("relay")) {
                                                            val = "support@gov.in";
                                                            toWhichAdmin = "Admin";
                                                        } else if (ipRequestWhichType.equals("ldap")) {
                                                            val = "rajesh.singh@nic.in";
                                                            toWhichAdmin = "Admin";
                                                        }
                                                    } else if (arr[1].equals(Constants.LDAP_FORM_KEYWORD)) {
                                                        val = "rajesh.singh@nic.in";
                                                        toWhichAdmin = "Admin";
                                                    }
                                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");
                                                } else {

                                                    //**************************11dec2020**********************************//
//                                      if(stat_type.equalsIgnoreCase("coordinator_pending")){
//                                      boolean chkstatus;
////                                          if (val.contains(",")) {
////                                              String[] coArr = val.split(",");
////                                              for (String coEm : coArr) {
////                                                  chkstatus = chkValidCoord(coEm);
////                                                  if (!chkstatus) {
////                                                      stat_type = "support_pending";
////                                                      toWhichAdmin = "Support";
////                                                      val = "support@gov.in";
////                                                  }
////                                              }
//                                         // }else{
//                                               chkstatus = chkValidCoord(val);
//                                                  if (!chkstatus) {
//                                                      stat_type = "support_pending";
//                                                      toWhichAdmin = "Support";
//                                                      val = "support@gov.in";
//                                                  } 
//                                         // }
//                                    }
                                                    //**************************11dec2020**************************************//
                                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");
                                                }
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "2 before update status in relay stat_type " + stat_type + " to user " + val);
                                                if (arr[1].equals(Constants.IP_FORM_KEYWORD) || arr[1].equals(Constants.SMS_FORM_KEYWORD)) {
                                                    if (arr[1].equals(Constants.IP_FORM_KEYWORD)) {
                                                        ipRequestWhichType = (String) detailHM.get("ip_action_request");
                                                        if (ipRequestWhichType.equals("sms")) {
                                                            val = "smssupport@gov.in";
                                                            toWhichAdmin = "Support";
                                                            stat_type = "support_pending";
                                                        }
                                                    } else {
                                                        val = "smssupport@gov.in";
                                                        toWhichAdmin = "Support";
                                                        stat_type = "support_pending";
                                                    }
                                                }

                                                //added by satya for c and dc  on 31122020
                                                if (stat_type.equalsIgnoreCase("coordinator_pending")) {
                                                    boolean chkstatus;
                                                    if (val.contains(",")) {
                                                        String[] coArr = val.split(",");
                                                        for (String coEm : coArr) {
                                                            chkstatus = chkValidCoord(coEm);
                                                            if (!chkstatus) {
                                                                stat_type = "support_pending";
                                                                toWhichAdmin = "Support";
                                                                val = "support@gov.in";
                                                            }
                                                        }
                                                    } else {
                                                        chkstatus = chkValidCoord(val);
                                                        if (!chkstatus) {
                                                            stat_type = "support_pending";
                                                            toWhichAdmin = "Support";
                                                            val = "support@gov.in";
                                                        }
                                                    }
                                                }
                                                updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");//statRemarks added by pr on 5thjun18 // commented on 9thjul18 for testing , to be uncommented in production
                                            }
                                            if (updateRes) {
                                                //update the application_type table for CA related details, line added by pr on 12thapr18
                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;

                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                                            }
                                        } else {
                                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "3 before update status in relay stat_type SUPPORT_PENDING  to user null ");
                                            String toEmailAddress = "";
                                            if (arr[1].equals(Constants.SMS_FORM_KEYWORD)) {
                                                toEmailAddress = "smssupport@gov.in";
                                            } else {
                                                toEmailAddress = "support@gov.in";
                                            }
                                            updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, "", sha_us_email, ""); //statRemarks added by pr on 5thjun18
                                            if (updateRes) {
                                                //update the application_type table for CA related details, line added by pr on 12thapr18
                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;
                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Support ( " + toEmailAddress + " ) !";
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                                            }
                                        }

                                    }

                                } else if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) // else if added by pr on 14thmar18
                                {
                                    //  System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else 8 ");
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside execute function wifi block ");
                                    /*if (department.contains("MyGov Project") || dn.contains("o=mygov.in")) {
                                        da_admin = "neeta.tandon@nic.in";
                                        updateRes = updateStatus(arr[0], "coordinator_pending", arr[1], "", da_admin, sha_us_email, "");
                                        if (updateRes) {
                                            // update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the coordinator - " + da_admin + " !";// text changed by AT on 13thJune2018
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded to the coordinator - " + da_admin + " !";
                                        }
                                    } else */ // if block commented by pr on 10thsep2020 wrt mobile with subject line "Query Raised/Responded on Reg No SINGLEUSER-FORM202008310209--mobile id of Soumi fro Team MyGov-Urgent please"
                                    if (employment.equalsIgnoreCase("state") && state.equalsIgnoreCase("Assam")) {
                                        updateRes = updateStatus(arr[0], "coordinator_pending", arr[1], statRemarks, "diggy@gov.in", sha_us_email, ""); // statRemarks added by pr on 5thjun18
                                        if (updateRes) {
                                            //update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Coordinator - diggy@gov.in !";// text changed by pr on 19thfeb18
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded to the Coordinator - diggy@gov.in !";
                                        }
                                    } else {

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside wifi mygov else else ");

                                        if (hm != null && hm.size() > 0) {
                                            if (hm.get("co") != null) {
                                                val = hm.get("co").toString().toLowerCase();
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside wifi mygov else else HM not null inside co ");
                                                if ((val.contains(auth_email.toLowerCase())) || (val.contains(RO_email.toLowerCase()))) {
                                                    if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").toString().equalsIgnoreCase("true")) || hm.get("sendtocoord") == null) {
                                                        stat_type = "mail-admin_pending";
                                                        toWhichAdmin = "Admin";
                                                        val = "support@nkn.in";
                                                    } else {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "Support";
                                                        val = "support@nkn.in";
                                                    }
                                                } else if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").toString().equalsIgnoreCase("true")) || hm.get("sendtocoord") == null) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                                                            + " inside wifi sendtocoord null or not null with sendtocoord as true " + hm.get("sendtocoord"));
                                                    if (!val.isEmpty()) {
                                                        stat_type = "coordinator_pending";
                                                        toWhichAdmin = "Coordinator";
                                                    } else {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "Support";
                                                        val = "support@nkn.in";
                                                    }
                                                } else {
                                                    stat_type = "support_pending";
                                                    toWhichAdmin = "Support";
                                                    val = "support@nkn.in";
                                                }
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                val = "support@nkn.in";
                                            }

// end, code added by pr on 19thjul19 
//                                            for (Object k : hm.keySet()) {
//                                                String key = k.toString(); // type whether da or co
//                                                val = hm.get(k).toString().toLowerCase(); // emails comma separated
//                                                if (key.equals("da")) {
//                                                    isKeyAvailable = true;
//                                                    if (val.equals("support@nic.in") || val.equals("support@gov.in") || val.equals("support@dummy.nic.in") || val.equals("support@nkn.in")) {
//                                                        stat_type = "support_pending";
//                                                        toWhichAdmin = "Support";
//                                                        val = "support@nkn.in";
//                                                    } else {
//
//                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside wifi mygov else else HM not null hm value is  " + hm);
//
//                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendtocoord value is " + hm.get("sendtocoord"));
//
//                                                        /*stat_type = "coordinator_pending";
//                                                            toWhichAdmin = "Coordinator";*/
//                                                        // above code commented,  start, code added by pr on 19thjul19
//                                                        if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").toString().equalsIgnoreCase("true")) || hm.get("sendtocoord") == null) {
//                                                            if (!val.isEmpty()) {
//                                                                stat_type = "coordinator_pending";
//                                                                toWhichAdmin = "Coordinator";
//                                                            } else {
//                                                                stat_type = "support_pending";
//                                                                toWhichAdmin = "Support";
//                                                                val = "support@nkn.in";
//                                                            }
//                                                        } else {
//                                                            stat_type = "support_pending";
//                                                            toWhichAdmin = "Support";
//                                                            val = "support@nkn.in";
//                                                        }
//                                                        // end, code added by pr on 19thjul19 
//
//                                                    }
//                                                } else if (key.equals("co")) {
//
//                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside wifi mygov else else HM not null inside co ");
//
//                                                    isKeyAvailable = true;
//                                                    if ((val.contains(auth_email.toLowerCase())) || (val.contains(RO_email.toLowerCase()))) {
//
//                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
//                                                                + " inside wifi mygov else else HM not null inside co  val equals auth emai or ro mobile val is " + val);
//
//                                                        // if around added by pr on 22ndjul19
//                                                        if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").toString().equalsIgnoreCase("true")) || hm.get("sendtocoord") == null) {
//                                                            stat_type = "mail-admin_pending";
//                                                            toWhichAdmin = "Admin";
//
//                                                            /*if (hm.get("madmin") != null) {
//                                                                    val = hm.get("madmin").toString();
//                                                                } else {
//                                                                    val = "support@nkn.in";
//                                                                }*/
//                                                            // above code commented below added by pr on 17thjun19
//                                                            val = "support@nkn.in";
//                                                        } else // else added by pr on 22ndjul19
//                                                        {
//                                                            stat_type = "support_pending";
//                                                            toWhichAdmin = "Support";
//                                                            val = "support@nkn.in";
//                                                        }
//
//                                                    } else {
//
//                                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside wifi mygov else else HM not null inside co ELSE ");
//
//                                                        /*stat_type = "coordinato else {
//                                                da_admin = "support@gov.in";
//                                                stat_type = "mail-admin_pending";
//                                                toWhichAdmin = "Admin";r_pending";
//                                                            toWhichAdmin = "Coordinator";*/
//                                                        // above code commented,  start, code added by pr on 19thjul19
//                                                        if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").toString().equalsIgnoreCase("true")) || hm.get("sendtocoord") == null) {
//                                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
//                                                                    + " inside wifi sendtocoord null or not null with sendtocoord as true " + hm.get("sendtocoord"));
//                                                            if (!val.isEmpty()) {
//                                                                stat_type = "coordinator_pending";
//                                                                toWhichAdmin = "Coordinator";
//                                                            } else {
//                                                                stat_type = "support_pending";
//                                                                toWhichAdmin = "Support";
//                                                                val = "support@nkn.in";
//                                                            }
//
//                                                        } else {
//                                                            stat_type = "support_pending";
//                                                            toWhichAdmin = "Support";
//                                                            val = "support@nkn.in";
//                                                        }
//                                                        // end, code added by pr on 19thjul19 
//
//                                                    }
//                                                }
//                                                if (isKeyAvailable) {
//                                                    break;
//                                                }
//                                            }
                                            updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");//statRemarks added by pr on 5thjun18
                                            if (updateRes) {
                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;

                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                                            }
                                        } else // if no record found then send to the support
                                        {
                                            String toEmailAddress = "";
                                            toEmailAddress = "support@nkn.in";
                                            updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, "", sha_us_email, "");
                                            if (updateRes) {
                                                if (updateAppType) {
                                                    updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                                }
                                                isSuccess = true;
                                                isError = false;
                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Support ( " + toEmailAddress + " ) !";
                                            } else {
                                                isSuccess = false;
                                                isError = true;
                                                msg = "Application (" + reg_no + ") could not be Forwarded !";
                                            }
                                        }
                                    }
                                } else if (arr[1].equals(Constants.DNS_FORM_KEYWORD)) {
                                    String stat_type_dapi = fetchFirstRowStatType(arr[0]);
                                    if (stat_type_dapi.equalsIgnoreCase("domainapi")) {
                                        stat_type = "mail-admin_pending";
                                        toWhichAdmin = "Admin";
                                        val = DNS_MAILADMIN;
                                    } else if (hm != null && hm.size() > 0) {
                                        HashMap hm1 = fetchDNSDetail(arr[0]);
                                        String record_mx = "", req_other_record = "";
                                        if (hm1 != null) {
                                            if (hm1.get("record_mx") != null) {
                                                record_mx = hm1.get("record_mx").toString();
                                            }
                                            if (hm1.get("req_other_record") != null) {
                                                req_other_record = hm1.get("req_other_record").toString();
                                            }
                                        }

                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " 3 req_other_record " + req_other_record
                                                + " record_mx " + record_mx);

                                        if (req_other_record.equalsIgnoreCase("mx") || !record_mx.isEmpty()) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + arr[0] + " 3 req_other_record " + req_other_record
                                                    + " record_mx " + record_mx + " should go to second coordinator ");

                                            val = DNS_SECOND_COORD;
                                            stat_type = "coordinator_pending";
                                            toWhichAdmin = "Coordinator";
                                        } else {
                                            for (Object k : hm.keySet()) {
                                                String key = k.toString(); // type whether da or co
                                                val = hm.get(k).toString().toLowerCase(); // emails comma separated
                                                if (key.equals("da")) {
                                                    isKeyAvailable = true;
                                                    if (val.equals("support@nic.in") || val.equals("support@gov.in") || val.equals("support@dummy.nic.in") || val.equals("support@nkn.in")) {
                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "DNS Support";
                                                        val = "support@gov.in";
                                                    } else if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").equals("true")) || hm.get("sendtocoord") == null) {
                                                        if (!val.isEmpty()) {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        } else {
//                                                                    stat_type = "support_pending";
//                                                                    toWhichAdmin = "Support";
//                                                                    val = "support@gov.in";
                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "DNS Admin";
                                                            val = DNS_MAILADMIN;

                                                        }
                                                    } else {
//                                                                stat_type = "support_pending";
//                                                                toWhichAdmin = "Support";
//                                                                val = "support@gov.in";

                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "DNS Support";
                                                        val = DNS_MAILADMIN;
                                                    }
                                                } else if (key.equals("co")) {
                                                    isKeyAvailable = true;
                                                    if ((val.contains(auth_email.toLowerCase())) || (val.contains(RO_email.toLowerCase()))) {
                                                        if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").equals("true")) || hm.get("sendtocoord") == null) {
                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "Dns Admin";
                                                            val = DNS_MAILADMIN;
                                                        } else {
//                                                                stat_type = "support_pending";
//                                                                toWhichAdmin = "Support";
//                                                                val = "support@gov.in";
                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "DNS Admin";
                                                            val = DNS_MAILADMIN;
                                                        }
                                                    } else if ((hm.get("sendtocoord") != null && hm.get("sendtocoord").equals("true")) || hm.get("sendtocoord") == null) {
                                                        if (!val.isEmpty()) {
                                                            stat_type = "coordinator_pending";
                                                            toWhichAdmin = "Coordinator";
                                                        } else {
//                                                                    stat_type = "support_pending";
//                                                                    toWhichAdmin = "Support";
//                                                                    val = "support@gov.in";

                                                            stat_type = "mail-admin_pending";
                                                            toWhichAdmin = "DNS Admin";
                                                            val = DNS_MAILADMIN;
                                                        }
                                                    } else {
//                                                                stat_type = "support_pending";
//                                                                toWhichAdmin = "Support";
//                                                                val = "support@gov.in";

                                                        stat_type = "support_pending";
                                                        toWhichAdmin = "DNS Support";
                                                        val = DNS_MAILADMIN;
                                                    }
                                                }
                                                if (isKeyAvailable) {
                                                    break;
                                                }
                                            }

                                            if (!isKeyAvailable) {
//                                                    stat_type = "support_pending";
//                                                    toWhichAdmin = "Support";
//                                                    val = "support@gov.in";
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Dns Support";
                                                val = DNS_MAILADMIN;
                                            }
                                        }
                                    } else {
                                        HashMap hm1 = fetchDNSDetail(arr[0]);
                                        String record_mx = "", req_other_record = "";
                                        if (hm1 != null) {
                                            if (hm1.get("record_mx") != null) {
                                                record_mx = hm1.get("record_mx").toString();
                                            }
                                            if (hm1.get("req_other_record") != null) {
                                                req_other_record = hm1.get("req_other_record").toString();
                                            }
                                        }

                                        if (req_other_record.equalsIgnoreCase("mx") || !record_mx.isEmpty()) {
                                            val = DNS_SECOND_COORD;
                                            stat_type = "coordinator_pending";
                                            toWhichAdmin = "Coordinator";
                                        } else {
//                                                stat_type = "support_pending";
//                                                toWhichAdmin = "Support";
//                                                val = "support@gov.in";
                                            stat_type = "support_pending";
                                            toWhichAdmin = "DNS Support";
                                            val = DNS_MAILADMIN;

                                        }
                                    }
                                    updateRes = updateStatus(arr[0], stat_type, arr[1], statRemarks, val, sha_us_email, "");//statRemarks added by pr on 5thjun18
                                    if (updateRes) {
                                        if (updateAppType) {
                                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                        }
                                        isSuccess = true;
                                        isError = false;

                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + val + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "Application (" + reg_no + ") could not be Forwarded !";
                                    }
                                } else if (arr[1].equals(Constants.WIFI_PORT_FORM_KEYWORD)) {
                                    //  System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else 8 ");
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside execute function wifi block ");
                                    ldap = new Ldap();
                                    boolean UsrisGovEmp = ldap.emailValidate(auth_email);
                                    boolean RoisGovEmp = ldap.emailValidate(RO_email);
                                    if ((UsrisGovEmp && RoisGovEmp) || (!UsrisGovEmp && RoisGovEmp)) {
                                        da_admin = "guptakk@nic.in";
                                        updateRes = updateStatus(arr[0], "coordinator_pending", arr[1], "", da_admin, sha_us_email, "");
                                        if (updateRes) {
                                            // update the application_type table for CA related details, line added by pr on 12thapr18
                                            if (updateAppType) {
                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the coordinator - " + da_admin + " !";// text changed by AT on 13thJune2018
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            msg = "Application (" + reg_no + ") could not be Forwarded to the coordinator - " + da_admin + " !";
                                        }

                                    }//else if((!UsrisGovEmp && !RoisGovEmp)){
//                                       da_admin = "guptakk@nic.in";
//                                        updateRes = updateStatus(arr[0], "coordinator_pending", arr[1], "", da_admin, sha_us_email, "");
//                                        if (updateRes) {
//                                            // update the application_type table for CA related details, line added by pr on 12thapr18
//                                            if (updateAppType) {
//                                                updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
//                                            }
//                                            isSuccess = true;
//                                            isError = false;
//                                            msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the coordinator - " + da_admin + " !";// text changed by AT on 13thJune2018
//                                        } else {
//                                            isSuccess = false;
//                                            isError = true;
//                                            msg = "Application (" + reg_no + ") could not be Forwarded to the coordinator - " + da_admin + " !";
//                                        } 
//                                    }
                                } else if (arr[1].equals(Constants.EMAILACTIVATE_FORM_KEYWORD) || arr[1].equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside execute function EmailActivation block ");
                                    Set<String> emp_coord_bos = db.fetchBoFromEmpCoord(empDetails);
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " emp_coord_bos " + emp_coord_bos);
                                    Set<String> ldap_bos = ldap.fetchBos(empDetails.get("auth_email").toString());

                                    domains = new HashSet<>();
                                    for (String boId : emp_coord_bos) {
                                        System.out.println("Bo id ::: " + boId);
                                        Set<String> domain = ldap.fetchAllowedDomains(boId);
                                        if (domain.size() > 0)// line added on 12thmar2020
                                        {
                                            domains.addAll(domain);
                                        }
                                    }
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ldap_bos " + ldap_bos + "  auth_email  " + empDetails.get("auth_email").toString());
                                    boolean matchBos = CollectionUtils.containsAny(emp_coord_bos, ldap_bos);
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " matchBos " + matchBos);
                                    if (hm != null && hm.size() > 0) {
                                        if (matchBos) {
                                            if (hm.containsKey("da")) {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                                fwd_madmin = hm.get("da").toString();
                                            } else if (hm.containsKey("co")) {
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                                fwd_madmin = hm.get("co").toString();
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                fwd_madmin = "support@gov.in";
                                            }
                                        } else {
                                            if (hm.containsKey("co")) {
                                                stat_type = "coordinator_pending";
                                                toWhichAdmin = "Coordinator";
                                                fwd_madmin = hm.get("co").toString();
                                            } else {
                                                stat_type = "support_pending";
                                                toWhichAdmin = "Support";
                                                fwd_madmin = "support@gov.in";
                                            }
                                        }
                                    } else {
                                        stat_type = "support_pending";
                                        toWhichAdmin = "Support";
                                        fwd_madmin = "support@gov.in";
                                    }
                                    boolean res = updateStatus(reg_no, stat_type, formName, statRemarks, fwd_madmin, "", ""); // above line commented below added by pr on 29thjan18  //statRemarks added by pr on 5thjun18  
                                    System.out.println("*******************res :::::::::::::::::: " + res);
                                    this.isDa = true;
                                    if (res) {
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + fwd_madmin + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        if (msg.equals("")) {
                                            msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                                        }
                                    }
                                } else {
                                    String toEmailAddress = "";
                                    if (arr[1].equals(Constants.SMS_FORM_KEYWORD)) {
                                        toEmailAddress = "smssupport@gov.in";
                                    } else if (arr[1].equals(Constants.WIFI_FORM_KEYWORD)) {
                                        toEmailAddress = "support@nkn.in";
                                    } else {
                                        toEmailAddress = "support@gov.in";
                                    }
                                    updateRes = updateStatus(arr[0], "support_pending", arr[1], statRemarks, "", sha_us_email, ""); //statRemarks added by pr on 5thjun18
                                    if (updateRes) {
                                        if (updateAppType) {
                                            updateAppType(arr[0], arr[1], app_ca_type, app_ca_path);
                                        }
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the Support ( " + toEmailAddress + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        msg = "Application (" + reg_no + ") could not be Forwarded !";
                                    }
                                }
                            } //inner if ends here
                        }
                    } // if ends here
                } //else ends here
            } // statremarks if end
            else {
                isSuccess = false;
                isError = true;
                msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // constant added  by pr on 28thdec18
            }

        } catch (Exception e) // try catch added by pr on 23rdapr19
        {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " executeAtRO MAIN EXCEPTION CATCH " + e.getMessage());

            isSuccess = false;

            isError = true;

            msg = "Something went wrong";
        }

    }
// below function added by pr on 24thoct19

    public String fetchFirstRowStatType(String regNo) {
        String stat_type = "";

        try {

            conSlave = DbConnection.getSlaveConnection();

            String qry = "SELECT stat_type FROM status WHERE stat_reg_no = ? ORDER BY stat_id DESC LIMIT 1";

            PreparedStatement ps = conSlave.prepareStatement(qry);

            ps.setString(1, regNo);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "getFirstRowStatType qry :" + ps);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                stat_type = rs.getString("stat_type");
            }

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "getFirstRowStatType Exception :" + e.toString());
        }

        return stat_type;
    }

    public String actionAdmin() {
        isSuccess = true;
        isError = false;
        msg = (String) sessionMap.get("msg");
        return SUCCESS;

    }

    // code taken out from execute function by pr on 7thjan19
    // below function added by pr on 12thapr18
    public Boolean updateAppType(String app_reg_no, String app_form_type, String app_ca_type, String app_ca_path) {
        Boolean updateStatus = false;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = " UPDATE application_type SET app_form_type = ?, app_ca_type = ?, app_ca_path = ?, app_updatedon = current_timestamp WHERE  app_reg_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, app_form_type);
            ps.setString(2, app_ca_type);
            ps.setString(3, app_ca_path);
            ps.setString(4, app_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " updateAppType query is " + ps);
            int res = ps.executeUpdate();
            if (res > 0) {

                // update final_audit_trcak table for this value too
                qry = " UPDATE final_audit_track SET app_ca_type = ? WHERE  registration_no = ? ";
                ps = con.prepareStatement(qry);
                ps.setString(1, app_ca_type);
                ps.setString(2, app_reg_no);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " updateAppType final_audit_track query is " + ps);
                int res1 = ps.executeUpdate();

                if (res1 > 0) {
                    updateStatus = true;
                }

            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insidne updateStatus in ForwardAction class 2" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 21 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "updateAppType EXCEPTION 1 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 22 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "updateAppType EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return updateStatus;
    }

    public String eSignResponsePageAdmin() {
        if (sessionMap.get("esignmessage") != null) {
            return (String) sessionMap.get("esignmessage");
        } else {
            return "{error: 'esignmessage is null'}";
        }
    }

    // below function added by pr on 20thfeb18
    public HashMap<String, String> fetchStateCoordsAtCA(String state, String department) // return key ->  whether da or coordinator, value comma separated emails
    {
        HashMap hm = new HashMap<String, String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT coordinator_email,admin_email FROM state_coordinator WHERE  state = ?  ";
            if (state.equals("Madhya Pradesh") && !department.equals("")) // check department only in caes of madhya pradesh
            {
                qry += " AND department = ? ";
            }
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, state);
            if (state.equals("Madhya Pradesh") && !department.equals("")) // if department needs to be checked
            {
                ps.setString(2, department);
            }
            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchStateCoordsAtCA func query is " + ps);
            rs = ps.executeQuery();
            ArrayList<String> arr = new ArrayList<String>();
            Boolean isDA = true;
            while (rs.next()) {
                String coordinator_email = rs.getString("coordinator_email").trim();
                String admin_email = rs.getString("admin_email").trim();
                arr.add(coordinator_email); // add the cooridnator in arraylist
                // System.out.println(" inside fetchStateCoordsAtCA func value of  "+coordinator_email+ "  admin_email value is  "+admin_email);
                if (!coordinator_email.equalsIgnoreCase(admin_email)) {
                    // System.out.println(" inside fetchStateCoordsAtCA  coord mobile and admin mobile doesnt match  ");
                    isDA = false;
                } else {
                    //System.out.println(" inside fetchStateCoordsAtCA  coord mobile and admin mobile matches  ");
                }
            }
            String csv = String.join(",", arr);
            //System.out.println(" inside fetchStateCoordsAtCA function isDA value is "+isDA+" emails value is "+csv);
            if (isDA) {
                hm.put("da", csv);
            } else {
                hm.put("co", csv);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde fetchStateCoordsAtCA " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 23 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchStateCoordsAtCA EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 24 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchStateCoordsAtCA EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 25 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchStateCoordsAtCA EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        return hm;
    }

    // below function added by pr on 8thmar18
    public HashMap<String, String> fetchEmpCoordsAtCA(String reg_no, String form_type) // return key ->  whether da or coordinator or madmin, value comma separated emails
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getempcoords func reg_no " + reg_no + " form_type " + form_type);
        HashMap empDetails = fetchEmploymentDetails(form_type, reg_no);
        String employment = "", ministry = "", district = "", department = "", organization = "", other_dept = "", state = "", add_state = "", flag = ""; // add_state added by pr on 23rdmar18
        String city = "";// line added by pr on 20thaug2020
        boolean isDAavailable = false;
        String emp_category = "", emp_min_state_org = "", emp_dept = "";
        if (empDetails != null && empDetails.get("employment") != null) {
            employment = empDetails.get("employment").toString();
            if (empDetails.get("ministry") != null) {
                ministry = empDetails.get("ministry").toString();
            }
            if (empDetails.get("department") != null) {
                department = empDetails.get("department").toString();
            }
            if (empDetails.get("organization") != null) {
                organization = empDetails.get("organization").toString();
            }
            if (empDetails.get("state") != null) {
                state = empDetails.get("state").toString();
            }
            if (empDetails.get("other_dept") != null) {
                other_dept = empDetails.get("other_dept").toString();
            }
            // below if added by pr on 23rdmar18
            if (empDetails.get("add_state") != null) {
                add_state = empDetails.get("add_state").toString();
            }
            if (empDetails.get("district") != null) {
                district = empDetails.get("district").toString();
            }
            if (empDetails.get("city") != null) { // code added by pr on 20thaug2020
                city = empDetails.get("city").toString();
            }
        }
        if (employment.equalsIgnoreCase("Central") || employment.equalsIgnoreCase("UT")) {
            emp_category = employment;
            emp_min_state_org = ministry;
            if (department.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                emp_dept = "other"; // above line commented below added by Ashwini on 4th April 2018 so that if its is Centre and department is other then just select the coordinator for that Ministry and other department
                //emp_dept = other_dept;
            } else {
                emp_dept = department;
            }
        } else if (employment.equalsIgnoreCase("State")) {
            emp_category = employment;
            emp_min_state_org = state;
            if (department.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                //emp_dept = other_dept;
                emp_dept = "other"; // above line commented below added by pr on 20thmar18 so that if its is state and state is selected and department is other then just select the coordinator for that state and other department
            } else {
                emp_dept = department;
            }
        } else if (employment.equalsIgnoreCase("Const")) {
            emp_category = employment;
            if (organization.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                //emp_min_state_org = other_dept;
                emp_min_state_org = "other"; // above line commented below added by pr on 5thmar2020
            } else {
                emp_min_state_org = organization;
            }
        } else if (employment.equalsIgnoreCase("Others")) {
            emp_category = employment;
            if (organization.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                //emp_min_state_org = other_dept; 
                emp_min_state_org = "other"; // above line commented below added by pr on 5thmar2020
            } else {
                emp_min_state_org = organization;
            }
        } else if (employment.equalsIgnoreCase("Psu")) {
            emp_category = employment;
            if (organization.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                //emp_min_state_org = other_dept;
                emp_min_state_org = "other"; // above line commented below added by pr on 5thmar2020
            } else {
                emp_min_state_org = organization;
            }
        } else if (employment.equalsIgnoreCase("Nkn")) {
            emp_category = employment;
            if (organization.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                //emp_min_state_org = other_dept;
                emp_min_state_org = "other"; // above line commented below added by pr on 5thmar2020
            } else {
                emp_min_state_org = organization;
            }
        } else if (employment.equalsIgnoreCase("Project")) { // else if added by pr on 9thjun2020
            emp_category = employment;
            if (organization.equalsIgnoreCase("Other") && !other_dept.equalsIgnoreCase("")) {
                emp_min_state_org = "other";
            } else {
                emp_min_state_org = organization;
            }
        }

        HashMap hm = new HashMap<>();
        HashMap firstRecord = new HashMap<>(); // if there is a conflict in the data then just fetch the first  record and send the request to
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        //Connection con = null;
        emp_dept = emp_dept.toLowerCase();
        //System.out.println(" inside getempcoords at CA function  emp_dept value is  " + emp_dept);

        // start, code added by AT on 4thNov19
//        if (employment.equalsIgnoreCase("Central") && emp_min_state_org.equalsIgnoreCase("Statistics and Programme Implementation")) {
//            // then send it to a particular coordinator for every form
//            String co_email = "";
//            if (form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD) || form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || form_type.equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || form_type.equals(Constants.VPN_DELETE_FORM_KEYWORD)
//                    || form_type.equals(Constants.IP_FORM_KEYWORD) || form_type.equals(Constants.DIST_FORM_KEYWORD) || form_type.equals(Constants.BULKDIST_FORM_KEYWORD) || form_type.equals(Constants.LDAP_FORM_KEYWORD)) {
//                co_email = "iqbal.singh@nic.in";
//                hm.put("co", co_email);
//                hm.put("madmin", "support@nic.in");
//            } else if (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.BULK_FORM_KEYWORD) || form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD)
//                    || form_type.equals(Constants.NKN_BULK_FORM_KEYWORD) || form_type.equals(Constants.GEM_FORM_KEYWORD) || form_type.equals(Constants.IMAP_FORM_KEYWORD)
//                    || form_type.equals(Constants.RELAY_FORM_KEYWORD) || form_type.equals(Constants.MOB_FORM_KEYWORD)) {
//                //co_email = "devender.tanwar@nic.in";
//                //co_email = "chhabra.geeta@nic.in";// line modified by pr on 19thmar2020
//                //co_email = "chhabra.geeta@nic.in";// line modified by pr on 21stapr2020 fetch it from db
//
//                hm = fetchDAOrCo(employment, emp_min_state_org, emp_dept);// line added by pr on 21stapr2020
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchDAOrCo result is " + hm);
//
//                //hm.put("da", co_email);
//            } else if (form_type.equals(Constants.RELAY_FORM_KEYWORD)) {// else if added by pr on 21stapr2020
//                //co_email = "devender.tanwar@nic.in";
//                co_email = "chhabra.geeta@nic.in";// line modified by pr on 19thmar2020
//                hm.put("co", co_email);
//                hm.put("madmin", "support@nic.in");
//            } else if (form_type.equals(Constants.SMS_FORM_KEYWORD) || form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
//                co_email = "paliwal.deepa@nic.in";
//                hm.put("co", co_email);
//                hm.put("madmin", "support@nic.in");
//            } else if (form_type.equals(Constants.DNS_FORM_KEYWORD)) {
//                co_email = "m.mahajan@nic.in";
//                hm.put("co", co_email);
//                hm.put("madmin", "support@nic.in");
//            }
//
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                    + " inside fetchEmpCoordsAtCA func INSIDE MOSPI ministry  hm value is " + hm);
//
//            return hm;
//        } else if ((employment.equalsIgnoreCase("Central") && emp_min_state_org.equalsIgnoreCase("Railways,Railnet"))
//                && (form_type.equalsIgnoreCase(Constants.VPN_SINGLE_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_BULK_FORM_KEYWORD)
//                || form_type.equalsIgnoreCase(Constants.VPN_ADD_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_RENEW_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_DELETE_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_SURRENDER_FORM_KEYWORD))) {// only for vpn form
//            // then send it to a particular coordinator for every form
//            String co_email = "ashok.kr@nic.in";
//
//            hm.put("co", co_email);
//            hm.put("madmin", "support@nic.in");
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                    + " inside fetchEmpCoordsAtCA func INSIDE MOSPI ministry  hm value is " + hm);
//
//            return hm;
//        }
        // end, code added by pr on 28thoct19           
        // start, code added by pr on 28thjun19
        if (form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchEmpCoordsAtCA function wifi form ");

            if (ministry.equalsIgnoreCase("Electronics and Information Technology") && (emp_dept.equalsIgnoreCase("National Informatics Centre")
                    || emp_dept.equalsIgnoreCase("National Informatics Centtre Services Incorporated (NICSI)")
                    || emp_dept.equalsIgnoreCase("NIC Cert Division") || emp_dept.equalsIgnoreCase("NIC Employees") || emp_dept.equalsIgnoreCase("NIC Projects")
                    || emp_dept.equalsIgnoreCase("NIC Support Outsourced"))) {
                emp_category = "State";

                emp_min_state_org = add_state;

                emp_dept = "other";

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchEmpCoordsAtCA function wifi form  NIC related dept ");

            } else if (emp_category.equals("State") && emp_min_state_org.equals("Madhya Pradesh"))// else if added  by pr on 1stjul19
            {
                emp_dept = "other";

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchEmpCoordsAtCA function wifi form  State MP to pick other dept coordinator ");
            }

        }

        // end, code added by pr on 28thjun19
        try {

            String qry = "";
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            if (form_type.equalsIgnoreCase(Constants.VPN_SINGLE_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_BULK_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_ADD_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_RENEW_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_DELETE_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_SURRENDER_FORM_KEYWORD)) {
                if (!(add_state.equalsIgnoreCase("delhi") && employment.equalsIgnoreCase("central"))) {
                    emp_category = "State";
                    emp_dept = "other";
                    emp_min_state_org = add_state;
                }
            }

            Set<String> uniqueCoordinators = new HashSet<>();
            Set<String> uniqueAdmins = new HashSet<>();
            Set<String> uniqueDA = new HashSet<>();

            // Find DAs 
            //Added emp_type checks by AT on 7th sep 2020
            //Added emp_type in sql query by AT on 2nd sep 2020
            qry = "SELECT emp_coord_email,emp_admin_email,emp_type FROM employment_coordinator WHERE  emp_category = ? AND emp_min_state_org = ?  ";

            if (!emp_dept.equals("")) // query bifurcated by pr on 9thjul18
            {
                qry += "AND emp_dept = ?   ";
            }
            qry += " AND emp_status = 'a'  AND (emp_type='d' OR emp_type='dc')"; // line modified by pr on 7thfeb19
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, emp_category);
            ps.setString(2, emp_min_state_org);
            if (!emp_dept.equals("")) {
                ps.setString(3, emp_dept);
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "qry :" + ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                String coordinator_email = rs.getString("emp_coord_email").trim();
                String admin_email = rs.getString("emp_admin_email").trim();

                if (coordinator_email.equalsIgnoreCase(admin_email) && !isSupportEmail(coordinator_email)) {
                    uniqueDA.add(coordinator_email);
                }
            }

            // Find Coordinators 
            //Added emp_type checks by AT on 7th sep 2020
            //Added emp_type in sql query by AT on 2nd sep 2020
            qry = "SELECT emp_coord_email,emp_admin_email,emp_type FROM employment_coordinator WHERE  emp_category = ? AND emp_min_state_org = ?  ";

            if (!emp_dept.equals("")) // query bifurcated by pr on 9thjul18
            {
                qry += "AND emp_dept = ?   ";
            }
            qry += " AND emp_status = 'a'  AND (emp_type='c' OR emp_type='dc')"; // line modified by pr on 7thfeb19
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, emp_category);
            ps.setString(2, emp_min_state_org);
            if (!emp_dept.equals("")) {
                ps.setString(3, emp_dept);
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "qry :" + ps);
            rs = ps.executeQuery();

            while (rs.next()) {
                String coordinator_email = rs.getString("emp_coord_email").trim();
                String admin_email = rs.getString("emp_admin_email").trim();

                //Added emp_type checks by AT on 2nd sep 2020
                if (!isSupportEmail(coordinator_email)) {
                    uniqueCoordinators.add(coordinator_email);
                }
                uniqueAdmins.add(admin_email);
            }

            String coordCommaSeparated = "";
            String adminCommaSeparated = "";
            String daCommaSeparated = "";

            String domain = fetchDomain(employment, emp_min_state_org, department);  // added on 07june2022

            if (domain.equalsIgnoreCase("delhi.gov.in") && (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.BULK_FORM_KEYWORD) || form_type.equals(Constants.IMAP_FORM_KEYWORD) || form_type.equals(Constants.MOB_FORM_KEYWORD))) {
                hm.put("co", db.fetchDelhiNodalOfficers(employment, emp_min_state_org, department));
                if (!uniqueDA.isEmpty()) {
                    daCommaSeparated = String.join(",", uniqueDA);
                    if (!daCommaSeparated.isEmpty()) {
                        hm.put("da", daCommaSeparated);
                    }
                } else {
                    adminCommaSeparated = String.join(",", uniqueAdmins);
                    if (!adminCommaSeparated.isEmpty()) {
                        hm.put("madmin", adminCommaSeparated);
                    }
                }
            } else if (employment.equalsIgnoreCase("State") && emp_min_state_org.equalsIgnoreCase("Punjab") && add_state.equalsIgnoreCase("Punjab") && (form_type.equals(Constants.SINGLE_FORM_KEYWORD) || form_type.equals(Constants.BULK_FORM_KEYWORD) || form_type.equals(Constants.IMAP_FORM_KEYWORD) || form_type.equals(Constants.MOB_FORM_KEYWORD))) {
                hm.put("co", db.fetchPunjabNodalOfficers(district));
                if (!uniqueDA.isEmpty()) {
                    daCommaSeparated = String.join(",", uniqueDA);
                    if (!daCommaSeparated.isEmpty()) {
                        hm.put("da", daCommaSeparated);
                    }
                } else {
                    adminCommaSeparated = String.join(",", uniqueAdmins);
                    if (!adminCommaSeparated.isEmpty()) {
                        hm.put("madmin", adminCommaSeparated);
                    }
                }
            } else if (!uniqueDA.isEmpty()) {
                daCommaSeparated = String.join(",", uniqueDA);
                if (!daCommaSeparated.isEmpty()) {
                    hm.put("da", daCommaSeparated);
                }

                if (form_type.equalsIgnoreCase(Constants.DAONBOARDING_FORM_KEYWORD)) {
                    coordCommaSeparated = String.join(",", uniqueCoordinators);
                    if (!coordCommaSeparated.isEmpty()) {
                        hm.put("co", coordCommaSeparated);
                    }
                    hm.remove("da");
                }
            } else // When we have few DAs and Few Coordinators for the same department, we have to select DA only for that department irrespective of Category
            {
                coordCommaSeparated = String.join(",", uniqueCoordinators);
                adminCommaSeparated = String.join(",", uniqueAdmins);
                if (add_state.equalsIgnoreCase("Himachal Pradesh")) {
                    if (!coordCommaSeparated.isEmpty()) {
                        hm.put("co", coordCommaSeparated);
                    }

                    if (adminCommaSeparated.contains("kaushal.shailender@nic.in")) {
                        hm.put("da", "kaushal.shailender@nic.in");
                    } else {
                        hm.put("madmin", "support@nic.in");
                    }
                } else if (employment.equalsIgnoreCase("Central") && (!add_state.equalsIgnoreCase("Delhi"))) {
                    if (emp_dept.contains("CSIRHRDC")) {
                        coordCommaSeparated = String.join(",", uniqueCoordinators);
                        adminCommaSeparated = String.join(",", uniqueAdmins);
                        if (!coordCommaSeparated.isEmpty()) {
                            hm.put("co", coordCommaSeparated);
                        }
                        if (!adminCommaSeparated.isEmpty()) {
                            hm.put("madmin", adminCommaSeparated);
                        }
                    } else if (emp_dept.equalsIgnoreCase("Ministry of Skill Development And Entrepreneurship")) {
                        coordCommaSeparated = String.join(",", uniqueCoordinators);
                        adminCommaSeparated = String.join(",", uniqueAdmins);
                        if (!coordCommaSeparated.isEmpty()) {
                            hm.put("co", coordCommaSeparated);
                        }
                        if (!adminCommaSeparated.isEmpty()) {
                            hm.put("madmin", adminCommaSeparated);
                        }
                    } else if (add_state.equalsIgnoreCase("Kerala")) {
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("State", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            //to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else if (add_state.equalsIgnoreCase("Andaman and Nicobar Islands")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Andaman and Nicobar Islands state : " + add_state);
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("UT", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            // to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else if (add_state.equalsIgnoreCase("Andhra Pradesh")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Andhra Pradesh state : " + add_state);
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("State", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            // to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else if (add_state.equalsIgnoreCase("Arunachal Pradesh")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Arunachal Pradesh state : " + add_state);
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("State", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            // to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else if (add_state.equalsIgnoreCase("Jammu and Kashmir")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Jammu and Kashmir state : " + add_state);
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("UT", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            //   to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else if (add_state.equalsIgnoreCase("Karnataka")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Karnataka state : " + add_state);
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("State", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            // to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else if (add_state.equalsIgnoreCase("Maharashtra")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Form Name 6666666666666666666666666666666666666= " + empDetails.get("form_name"));
                        if (domain.equalsIgnoreCase("ibm.gov.in")) { // added by manikant 23-11-2022 as disccus simran
                            coordCommaSeparated = String.join(",", uniqueCoordinators);
                            adminCommaSeparated = String.join(",", uniqueAdmins);
                            if (!coordCommaSeparated.isEmpty()) {
                                hm.put("co", coordCommaSeparated);
                            }
                            if (!adminCommaSeparated.isEmpty()) {
                                hm.put("madmin", adminCommaSeparated);
                            }
                        } else if (city.equalsIgnoreCase("Nagpur")) // if and else if added by pr on 20thaug2020
                        {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Maharashtra state : " + add_state + " Nagpur city ");
                            //hm.put("co", "la.garje@nic.in");
                            hm.put("co", "sdhoke@nic.in"); // line modified by pr on 14thmay2020, again 2ndjun2020
                            //hm.put("co", "tarun.ss@nic.in");//line modifed by manikant pandey 21-11-2022 
                            hm.put("madmin", "support@gov.in");
                        } else if (city.equalsIgnoreCase("Pune")) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Maharashtra state : " + add_state + " Pune city ");
                            //hm.put("co", "la.garje@nic.in");
                            hm.put("co", "rm.kharse@nic.in"); // line modified by pr on 14thmay2020, again 2ndjun2020
                            hm.put("madmin", "support@gov.in");
                        } else if (coordCommaSeparated != null && !coordCommaSeparated.isEmpty()) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Maharashtra state : " + add_state);
                            //hm.put("co", "la.garje@nic.in");
                            hm.put("co", coordCommaSeparated); // line modified by pr on 14thmay2020, again 2ndjun2020  
                            hm.put("madmin", adminCommaSeparated);
                        } else {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Maharashtra state : " + add_state);
                            hm.put("co", "navare.sr@nic.in");
                            hm.put("madmin", adminCommaSeparated);
                        }
                    } else if (add_state.equalsIgnoreCase("Puducherry")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN Puducherry state : " + add_state);
                        List<String> coordEmails = fetchHardCodedCoordinatorsOrDAs("UT", add_state, "");
                        String to_email = "";
                        if (coordEmails != null && !coordEmails.isEmpty()) {
                            for (String coordEmail : coordEmails) {
                                to_email += coordEmail.split(":")[0].toLowerCase() + ",";
                            }
                            // to_email = to_email.replaceAll(",$", to_email);
                            to_email = to_email.replaceAll(",$", "");
                        }
                        hm.put("co", to_email);
                        hm.put("madmin", "support@gov.in");
                    } else {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "POSTING IS IN STATE OTHER THAN DELHI and above states | state : " + add_state);
                        //Added emp_type in query by AT on 2nd sep 2020
                        qry = "SELECT emp_coord_email,emp_admin_email,emp_type FROM employment_coordinator WHERE  emp_category = ? AND emp_min_state_org = ?  AND emp_dept = ?   ";
                        qry += " AND emp_status = 'a'  AND (emp_type='c' OR emp_type='dc')";
                        ps1 = conSlave.prepareStatement(qry);
                        ps1.setString(1, "State");
                        ps1.setString(2, add_state);
                        ps1.setString(3, "other");
                        rs = ps1.executeQuery();
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "ps1:" + ps1);
                        uniqueCoordinators.clear();
                        boolean isFound = false;
                        while (rs.next()) {
                            isFound = true;
                            String coordinator_email = "";
                            if (rs.getString("emp_coord_email") != null || !rs.getString("emp_coord_email").isEmpty()) {
                                coordinator_email = rs.getString("emp_coord_email").trim();
                            }

                            if (!isSupportEmail(coordinator_email)) {
                                uniqueCoordinators.add(coordinator_email);
                            }
                        }
                        if (isFound) {
                            coordCommaSeparated = String.join(",", uniqueCoordinators);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "csv1:" + coordCommaSeparated);
                            if (!coordCommaSeparated.isEmpty()) {
                                hm.put("co", coordCommaSeparated);
                            }
                            hm.put("madmin", "support@gov.in");
                        }
                    }
                } else {
                    if (!coordCommaSeparated.isEmpty()) {
                        hm.put("co", coordCommaSeparated);
                    }
                    if (!adminCommaSeparated.isEmpty()) {
                        hm.put("madmin", adminCommaSeparated);
                    }
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchEmpCoordsAtCA func only for forms dns wifi vpn before modification hm value is " + hm);
            String sendToEmail = "";
            boolean isCo = false; // to check if it is a co or a da
            boolean isNICEmployee = false;
            ArrayList<String> finalCoArr = new ArrayList<String>();

            if (hm != null) {
                if (hm.get("co") != null || hm.get("da") != null) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchEmpCoordsAtCA func inside co da not null ");

                    if (hm.get("co") != null) {
                        sendToEmail = hm.get("co").toString();
                        isCo = true;
                    } else if (hm.get("da") != null) {
                        sendToEmail = hm.get("da").toString();
                        isCo = false;
                    }

                    // if it contains comma then check for those coordinators which are NIC employees , 
                    //if it has atleast one then only that is returned back with isNICEmployee boolean true, 
                    //else all but with nicEmployee boolean false
                    if (sendToEmail.contains(",")) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchEmpCoordsAtCA func inside sendToEmail contains comma ");

                        String[] coArr = sendToEmail.split(",");

                        for (String coEm : coArr) {
                            if (!coEm.trim().isEmpty()) {
                                if (isNICEmployee(coEm.trim())) {
                                    finalCoArr.add(coEm.trim());
                                    isNICEmployee = true;
                                }
                            }
                        }

                        if (isNICEmployee) {
                            sendToEmail = String.join(",", finalCoArr);
                        }

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchEmpCoordsAtCA func value of isNICEmployee " + isNICEmployee + " sendToEmail value is " + sendToEmail);

                        // convert the above arraylist into a comma separated string and put it back in the hashmap with key as co or da                               
                    } else // just check for only this mobile whether NIC Employee or not
                    {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchEmpCoordsAtCA func inside sendToEmail DOESNT contains comma ");

                        if (isNICEmployee(sendToEmail)) {
                            isNICEmployee = true;
                        }

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchEmpCoordsAtCA func value of isNICEmployee " + isNICEmployee + " sendToEmail value is " + sendToEmail);

                    }

                    if (isCo) // key is coordinator
                    {
                        hm.replace("co", sendToEmail);

                    } else // key is da
                    {
                        hm.replace("da", sendToEmail);
                    }

                    if (isNICEmployee) {
                        hm.put("sendtocoord", "true"); // if it is true it is to be sent to the coordinator as coordinator_pending
                    } else {
                        hm.put("sendtocoord", "false"); // if it is false it is to be sent to the support as support_pending    
                    }
                }
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " inside fetchEmpCoordsAtCA func only for forms dns wifi vpn AFTER modification hm value is " + hm);

            // }
            // end, code added by pr on 19thjul19
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde fetchEmpCoordsAtCA " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 26 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA  EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 27 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA  EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 28 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA  EXCEPTION 3 " + e.getMessage());

                }
            }
        }

        // start, code added by pr on 19thdec19
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA hm value is " + hm + " form_type is " + form_type);

        if (hm.containsValue("shekhawat.ss@nic.in")) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA  hm.containsValue(shekhawat.ss@nic.in) ");

            if (form_type.equals(Constants.WIFI_FORM_KEYWORD) || form_type.equals(Constants.RELAY_FORM_KEYWORD) || form_type.equals(Constants.SMS_FORM_KEYWORD)
                    || form_type.equals(Constants.LDAP_FORM_KEYWORD) || form_type.equals(Constants.DIST_FORM_KEYWORD) || form_type.equals(Constants.BULKDIST_FORM_KEYWORD)) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside shekhawat.ss@nic.in forms ");

                String val = "";
                for (Object s : hm.keySet()) {
                    val = hm.get(s).toString();

                    if (val.toLowerCase().equals("shekhawat.ss@nic.in")) {
                        hm.replace(s, "nagar.mayank@nic.in");
                    }

                }
            }
        }

        //**********************start 14jan2021**************************************************************
        if (hm.containsValue("kumarabhishek@bihar.gov.in")) {
            if (form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA  hm Value=(kumarabhishek@bihar.gov.in) ");

                String preferred_email1 = (String) empDetails.get("preferred_email1");
                String preferred_email2 = (String) empDetails.get("preferred_email2");

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "preferred_email1::::" + preferred_email1 + "  preferred_email2:::" + preferred_email1);

                String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                if (pref1_domain.equalsIgnoreCase("gov.in") && pref2_domain.equalsIgnoreCase("gov.in")) {
                    String val = "";
                    for (Object s : hm.keySet()) {
                        val = hm.get(s).toString();
                        if (val.toLowerCase().equals("kumarabhishek@bihar.gov.in")) {
                            hm.replace(s, "tarkeshwar.p@nic.in");
                        }
                    }
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "hm::::" + hm);

            }
        }
        //**********************end 14jan2021**************************************************************
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmpCoordsAtCA FINALLLLLL hm value is " + hm);

        // end, code added by pr on 19thdec19
        return hm; // this can be null in case of default data or no found data
    }

    // below function added by pr on 21stapr2020
//    public HashMap<String, String> fetchDAOrCo(String emp_category, String emp_min_state_org, String emp_dept) {
//
//        HashMap<String, String> hm = new HashMap<String, String>();
//
//        String emp_coord_email = "", emp_admin_email = "";
//        try {
//
//            String qry = "SELECT emp_coord_email,emp_admin_email, emp_type FROM employment_coordinator WHERE  emp_category = ? AND emp_min_state_org = ?  AND emp_dept = ?   ";
//            qry += " AND emp_status = 'a' ORDER BY emp_id DESC LIMIT 1 ";
//            PreparedStatement ps1 = conSlave.prepareStatement(qry);
//            ps1.setString(1, emp_category);
//            ps1.setString(2, emp_min_state_org);
//            ps1.setString(3, emp_dept);
//
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchDAOrCo query is " + ps1);
//
//            ResultSet rs = ps1.executeQuery();
//
//            if (rs.next()) {
//                if (rs.getString("emp_coord_email") != null && !rs.getString("emp_coord_email").equals("")) {
//                    emp_coord_email = rs.getString("emp_coord_email");
//                }
//
//                if (rs.getString("emp_admin_email") != null && !rs.getString("emp_admin_email").equals("")) {
//                    emp_admin_email = rs.getString("emp_admin_email");
//                }
//            }
//
//            if (!emp_coord_email.isEmpty() && !emp_admin_email.isEmpty() && !isSupportEmail(emp_coord_email)) {
//                if (emp_coord_email.equalsIgnoreCase(emp_admin_email)) {
//                    if (rs.getString("emp_type") == null || rs.getString("emp_type").isEmpty()) {
//                        hm.put("da", emp_coord_email);
//                    } else if (rs.getString("emp_type").equalsIgnoreCase("d") || rs.getString("emp_type").equalsIgnoreCase("dc")) {
//                        hm.put("da", emp_coord_email);
//                    } else if (rs.getString("emp_type").equalsIgnoreCase("c")) {
//                        hm.put("co", emp_coord_email);
//                    } else {
//                        hm.put("da", emp_coord_email);
//                    }
//                } else if (rs.getString("emp_type") == null || rs.getString("emp_type").isEmpty()) {
//                    hm.put("co", emp_coord_email);
//                    hm.put("madmin", emp_admin_email);
//                } else if (rs.getString("emp_type").equalsIgnoreCase("d")) {
//                    hm.put("da", emp_coord_email);
//                } else if (rs.getString("emp_type").equalsIgnoreCase("c") || rs.getString("emp_type").equalsIgnoreCase("dc")) {
//                    hm.put("co", emp_coord_email);
//                    hm.put("madmin", emp_admin_email);
//                } else {
//                    hm.put("co", emp_coord_email);
//                    hm.put("madmin", emp_admin_email);
//                }
//            }
//
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchDAOrCo hm is " + hm);
//
//        } catch (Exception e) {
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchDAOrCo Exception ");
//        }
//
//        return hm;
//    }
    public String fetchDomain(String emp_category, String emp_min_state_org, String emp_dept) {

        HashMap<String, String> hm = new HashMap<String, String>();

        String emp_domain = "";
        try {

            String qry = "SELECT emp_domain FROM employment_coordinator WHERE  emp_category = ? AND emp_min_state_org = ?  AND emp_dept = ? ";

            PreparedStatement ps1 = conSlave.prepareStatement(qry);
            ps1.setString(1, emp_category);
            ps1.setString(2, emp_min_state_org);
            ps1.setString(3, emp_dept);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetch domain query is " + ps1);

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                if (rs.getString("emp_domain") != null) {
                    emp_domain = rs.getString("emp_domain");
                }

            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchdomain hm is " + hm);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchdomain Exception " + e);
        }

        return emp_domain;
    }

    private boolean isSupportEmail(String email) {
        if (email.equals("support@gov.in") || email.equals("support@nic.in") || email.equals("support@dummy.nic.in")) {
            return true;
        }
        return false;
    }

    public String fetchEmpCoordsAtCAForVPN1(String ministry) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String coordCommaSeparated = "";
        try {
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = "SELECT emp_coord_email FROM vpn_coordinator WHERE  emp_min_state_org = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ministry);
            //System.out.println("fetch emp cord at ca for vpn:::::" + ps);
            rs = ps.executeQuery();
            ArrayList<String> coordArr = new ArrayList<>();
            while (rs.next()) {
                String coordinator_email = rs.getString("emp_coord_email").trim();
                coordArr.add(coordinator_email);
            }
            // till here, we got mobile addresses for Coordinators
            Set<String> uniqueCoordinators = new LinkedHashSet<>(coordArr); // contains unique mobile addresses of coordinator

            if (uniqueCoordinators.size() > 0) {
                coordCommaSeparated = String.join(",", uniqueCoordinators);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde fetchEmpCoordsAtCA " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 26 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 27 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                } catch (Exception e) {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 28 " + e.getMessage());
                }
            }
        }
        return coordCommaSeparated;
    }

    public String fetchEmpCoordsAtCAForVPN(String regNumber) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String coordinator_email = "";
        try {
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = "SELECT coordinator_email FROM vpn_registration WHERE  registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNumber);
            //System.out.println("fetch emp cord at ca for vpn:::::" + ps);
            rs = ps.executeQuery();

            ArrayList<String> coordArr = new ArrayList<>();
            if (rs.next()) {
                coordinator_email = rs.getString("coordinator_email").trim();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde fetchEmpCoordsAtCA " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 26 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 27 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 28 " + e.getMessage());
                }
            }
        }
        return coordinator_email;
    }

    public HashMap<String, String> fetchDNSDetail(String regNo) {
        String domain = "";
        HashMap<String, String> hm = new HashMap();
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
            
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT dns_type,record_aaaa,record_mx,record_ptr,record_srv,record_spf,record_txt,req_other_record FROM dns_registration WHERE  registration_no = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde fetchDNSDetail query " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("dns_domain", rs.getString("dns_type"));
                hm.put("record_aaaa", rs.getString("record_aaaa"));
                hm.put("record_mx", rs.getString("record_mx"));
                hm.put("record_ptr", rs.getString("record_ptr"));
                hm.put("record_srv", rs.getString("record_srv"));
                hm.put("record_spf", rs.getString("record_spf"));
                hm.put("record_txt", rs.getString("record_txt"));
                hm.put("req_other_record", rs.getString("req_other_record"));
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde checkAlreadyStatus " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 31 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchDNSDetail  EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 32" + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchDNSDetail  EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 33" + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchDNSDetail  EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        return hm;
    }

    public Boolean statusAlreadyExist(String stat_form_type, String stat_reg_no, String stat_type, String stat_forwarded_by,
            String stat_forwarded_by_user, String stat_forwarded_to, String stat_forwarded_to_user) {
        Boolean isExist = false;
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT stat_id FROM status WHERE  stat_form_type = ? AND stat_reg_no = ? AND stat_type = ? AND "
                    + ""
                    + " stat_forwarded_by = ? AND  stat_forwarded_to = ? AND "
                    + ""
                    + "stat_forwarded_to_user LIKE ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_form_type);
            ps.setString(2, stat_reg_no);
            ps.setString(3, stat_type);
            ps.setString(4, stat_forwarded_by);
            ps.setString(5, stat_forwarded_to);
            ps.setString(6, "%" + stat_forwarded_to_user + "%");
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde statusAlreadyExist query " + ps);

            rs = ps.executeQuery();
            rs.last();
            if (rs.getRow() > 0) {
                isExist = true;
                isSuccess = false;
                isError = true;
                msg = "Same Action already performed ";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " statusAlreadyExist insinde checkAlreadyStatus " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 34 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "statusAlreadyExist EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 35 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "statusAlreadyExist EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 36 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "statusAlreadyExist EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        return isExist;
    }

    // below function added by pr on 8thjan19
    public HashMap<String, String> fetchCADetail(String ca_id) {
        ArrayList arr = new ArrayList<String>();
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet res = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = " SELECT ca_email , ca_mobile, ca_name FROM comp_auth WHERE ca_id = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ca_id);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde fetchCADetail query " + ps);

            res = ps.executeQuery();
            while (res.next()) {
                hm.put("email", res.getString("ca_email"));
                hm.put("mobile", res.getString("ca_mobile"));
                hm.put("name", res.getString("ca_name"));
            }
            //// con.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside updateFormsObj DB connection sms " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 37 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchCADetail EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 38 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchCADetail EXCEPTION 2 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 39 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchCADetail EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        return hm;
    }

    public Boolean updateStatus(String stat_reg_no, String stat_type, String stat_form_type, String stat_remarks, String stat_forwarded_to_user, String stat_forwarded_by_user_us,
            String stat_final_id) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside update status function reg_no is " + stat_reg_no + " stat_type " + stat_type + " stat_form_type " + stat_form_type + " stat_forwarded_to_user " + stat_forwarded_to_user);
        String ca_id = "0"; // only in case of ca panel
        String email = ""; // username in case of support, coordinator and mailadmin and ca as well
        String stat_forwarded_by = "";
        String stat_forwarded_by_user = "";
        String stat_forwarded_to = "";
        Boolean updateStatus = false;
        String role = fetchSessionRole();
        stat_forwarded_by_user = fetchSessionEmail(); // line added by pr on 26thfeb19

        // start, code added by pr on 3rdoct19
        // System.out.println(" inside updatestatus function stat_remarks passed to the function is "+stat_remarks+" statRemarks added from interface is "+statRemarks);
        if ((stat_remarks != null && stat_remarks.isEmpty()) && (statRemarks != null && !statRemarks.isEmpty())) {
            stat_remarks = statRemarks;
        }

        // end, code added by pr on 3rdoct19
        // ca forwarded to support
        if (stat_type.equals("support_pending")) {
            if (role.equals(Constants.ROLE_CO)) {
                stat_forwarded_by = "c";
            } else if (role.equals(Constants.ROLE_CA)) {
                stat_forwarded_by = "ca";
            } else if (role.equals(Constants.ROLE_SUP)) {
                stat_forwarded_by = "s";
            } else if (role.equals(Constants.ROLE_USER)) {
                stat_forwarded_by = "a";
            } else if (role.equals(Constants.ROLE_DA)) {
                stat_forwarded_by = "da";
            } else {
                stat_forwarded_by = "ca";
            }
            stat_forwarded_to = "s";
            if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
                stat_forwarded_to_user = "smssupport@gov.in";
            } else if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_DELETE_FORM_KEYWORD)) {
                stat_forwarded_to_user = "vpnsupport@nic.in";
            } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
                stat_forwarded_to_user = "support@nkn.in";
            } else {
                stat_forwarded_to_user = "support@gov.in";
            }
        } else if (stat_type.equals("coordinator_pending")) // support forwarded to coordinator
        {
            if (role.equals(Constants.ROLE_CO)) {
                stat_forwarded_by = "c";
            } else if (role.equals(Constants.ROLE_CA)) {
                stat_forwarded_by = "ca";
            } else if (role.equals(Constants.ROLE_SUP)) {
                stat_forwarded_by = "s";
            } else if (role.equals(Constants.ROLE_USER)) {
                stat_forwarded_by = "a";
            } else if (role.equals(Constants.ROLE_DA)) {
                stat_forwarded_by = "da";
            } else {
                stat_forwarded_by = "s";
            }
            stat_forwarded_to = "c";
//           if (stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_BULK_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD)) {
//                stat_forwarded_by = "ca";
//            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside coordinator pending stat_forwarded_by " + stat_forwarded_by + " stat_forwarded_by_user " + stat_forwarded_by_user);
        } else if (stat_type.equals("da_pending")) // ca forwarded to da-admin
        {
            stat_forwarded_to = "d";
            if (role.equals(Constants.ROLE_CO)) {
                stat_forwarded_by = "c";
            } else if (role.equals(Constants.ROLE_CA)) {
                stat_forwarded_by = "ca";
            } else if (role.equals(Constants.ROLE_SUP)) {
                stat_forwarded_by = "s";
            } else if (role.equals(Constants.ROLE_USER)) {
                stat_forwarded_by = "a";
            } else if (role.equals(Constants.ROLE_DA)) {
                stat_forwarded_by = "da";
            } else {
                stat_forwarded_by = "c";
            }
        } else if (stat_type.equals("ca_rejected")) // ca rejected
        {
            stat_forwarded_by = "ca";
            stat_forwarded_to = "";
        } else if (stat_type.equals("support_rejected")) // support rejected
        {
            stat_forwarded_by = "s";
            stat_forwarded_to = "";
        } else if (stat_type.equals("mail-admin_pending"))// coordinator forwarded to mail-admin
        {
            if (role.equals(Constants.ROLE_CO)) {
                stat_forwarded_by = "c";
            } else if (role.equals(Constants.ROLE_CA)) {
                stat_forwarded_by = "ca";
            } else if (role.equals(Constants.ROLE_SUP)) {
                stat_forwarded_by = "s";
            } else if (role.equals(Constants.ROLE_USER)) {
                stat_forwarded_by = "a";
            } else if (role.equals(Constants.ROLE_DA)) {
                stat_forwarded_by = "da";
            } else {
                stat_forwarded_by = "c";
            }
            stat_forwarded_to = "m";

            // check the below condition only if it is a case of not ip form, cos in ip form the checks have already been made before calling this function
            if (!stat_form_type.equalsIgnoreCase(Constants.IP_FORM_KEYWORD)) // check added by pr on 25thjun19
            {
                // get the emails from the mailadmin_forms table when the forwarded_to_user is only support@nic.in or support@gov.in or support@dummy.nic.in
                // if forwarding to the mailadmin support then it goes to all those admins responsible for that request present in the mailadmin_forms table

                if (stat_forwarded_to_user.equalsIgnoreCase("support@nic.in") || stat_forwarded_to_user.equalsIgnoreCase("support@nkn.in") || stat_forwarded_to_user.equalsIgnoreCase("support@nic.in") || stat_forwarded_to_user.equalsIgnoreCase("support@dummy.nic.in") || stat_forwarded_to_user.equalsIgnoreCase("smssupport@gov.in") || stat_forwarded_to_user.equalsIgnoreCase("vpnsupport@nic.in")) {
                    ArrayList mailArr = fetchMailAdmins(stat_form_type);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " updatestatus size of mailArr is " + mailArr.size());
                    stat_forwarded_to_user = String.join(",", mailArr);
                }
            }
        } else if (stat_type.equals("coordinator_rejected"))// coordinator rejected
        {
            stat_forwarded_by = "c";
            //stat_forwarded_by_user = fetchSessionEmail();
            stat_forwarded_to = "";
        } else if (stat_type.equals("mail-admin_rejected"))// mailadmin rejected added by pr on 26thfeb18
        {
            stat_forwarded_by = "m";
            //stat_forwarded_by_user = fetchSessionEmail();
            stat_forwarded_to = "";
        } else if (stat_type.equals("completed"))// mail-admin COMPLETED the request
        {
            stat_forwarded_by = "m";
            //stat_forwarded_by_user = fetchSessionEmail();
            stat_forwarded_to = "";
//            if(stat_form_type.equalsIgnoreCase("dns")) {
//                identifyDnsRequestAndUpdateDns_team_data(stat_form_type, stat_reg_no);
//            }
        } else if (stat_type.equals("us_pending"))// else if added by pr on 6thjan19
        {
            stat_forwarded_by = "ca";
            stat_forwarded_to = "us";
        }
        if (!stat_forwarded_by_user_us.equals("")) {
            stat_forwarded_by_user = stat_forwarded_by_user_us;
            stat_forwarded_by = "us";
        }
        //System.out.println(" inside update status stat_forwarded_to " + stat_forwarded_to + " stat_forwarded_to_user " + stat_forwarded_to_user);
        String stat_forwarded_by_email = "";
        String stat_forwarded_by_mobile = "";
        String stat_forwarded_by_name = "";
        String stat_forwarded_by_ip = ip;
        stat_forwarded_by_email = fetchIndividualEmail(); // replace with the new session value individual_loggedin_email 
        if (!stat_forwarded_by_user_us.equals("")) {
            //System.out.println(" stat_forwarded_by_user_us not null  stat_forwarded_by_email value is  " + stat_forwarded_by_email);
            stat_forwarded_by_email = stat_forwarded_by_user_us;
        }
        //System.out.println(" inside updatestatus function stat_forwarded_by_email value is " + stat_forwarded_by_email);
        HashMap loginDetails = fetchLoggedinDetails();
        if (loginDetails != null && loginDetails.size() > 0) {
            if (loginDetails.get("name") != null && !loginDetails.get("name").equals("")) {
                stat_forwarded_by_name = loginDetails.get("name").toString();
            }
            if (loginDetails.get("mobile") != null && !loginDetails.get("mobile").equals("")) {
                stat_forwarded_by_mobile = loginDetails.get("mobile").toString();
            }
            //System.out.println(" inside get logindetails function logged in name is " + stat_forwarded_by_name + " mobile is " + stat_forwarded_by_mobile);
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " before verfy fwd detail stat_forwarded_by " + stat_forwarded_by + " stat_forwarded_by_user " + stat_forwarded_by_user);
        //Boolean isExist = statusAlreadyExist(stat_form_type, stat_reg_no, stat_type, stat_forwarded_by, stat_forwarded_by_user, stat_forwarded_to, stat_forwarded_to_user);
        Boolean isValid = verifyFwdToDetails(stat_reg_no, stat_form_type, stat_forwarded_by, stat_forwarded_by_user); // line added by pr on 12thjan18    they have to be same as the request has been forwarded to details in the previous step

        //if (!(isExist) && isValid) // if modified by pr on 12thjan18
        if (isValid) // if modified by pr on 20thsep19
        {
            stat_forwarded_to_user = updateOrInsertCoordinatorOrDaIds(stat_forwarded_to_user, stat_type, stat_reg_no);
            PreparedStatement ps = null;
            String datetime = "";
            //Connection con = null;
            SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            Date dt = new Date();
            datetime = format.format(dt);
            try {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "Request Forwarding to::::: " + stat_forwarded_to_user);
                con = DbConnection.getConnection();
                String qry = "INSERT INTO status SET  stat_form_type = ?, stat_reg_no = ?, stat_type = ?, "
                        + ""
                        + " stat_forwarded_by = ?, stat_forwarded_by_user = ?,  stat_forwarded_to = ?  , "
                        + ""
                        + "stat_forwarded_to_user = ? , stat_remarks = ?, stat_ip = '" + ip + "', "
                        + ""
                        + "stat_forwarded_by_email = ?, stat_forwarded_by_mobile = ?, stat_forwarded_by_name = ?, stat_forwarded_by_ip = '" + ip + "', "
                        + ""
                        + "stat_forwarded_by_datetime = ?, stat_final_id = ? "; // stat_final_id added by pr on 16thjan19

                ps = con.prepareStatement(qry);
                ps.setString(1, stat_form_type);
                ps.setString(2, stat_reg_no);
                ps.setString(3, stat_type);
                ps.setString(4, stat_forwarded_by);
                ps.setString(5, stat_forwarded_by_user);
                ps.setString(6, stat_forwarded_to);
                ps.setString(7, stat_forwarded_to_user);
                ps.setString(8, stat_remarks);
                // start, code added by pr on 8thjan19
                ps.setString(9, stat_forwarded_by_email);
                ps.setString(10, stat_forwarded_by_mobile);
                ps.setString(11, stat_forwarded_by_name);
                ps.setString(12, datetime); // line added by pr on 16thjan19              
                ps.setString(13, stat_final_id); // line added by pr on 16thjan19              
                // end, code added by pr on 8thjan19
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " update status query is " + ps);

                int res = ps.executeUpdate();
                if (res > 0) {
                    //updateStatus = true; // line commented by pr on 1stsep2020
                    // System.out.println(" inside res greater than 0 before calling sendintimation ");
                    String temp_to_user = stat_forwarded_by_user; // line added by pr on 19thfeb19
                    if (stat_forwarded_by.equals("ca")) {
                        stat_forwarded_by_user = stat_forwarded_by_email;
                    }
                    // call a function to update the final_audit_track table, added by pr on 16thjan19 , flag added by pr on 1stsep2020
                    updateStatus = update_final_trail(stat_reg_no, stat_forwarded_by, stat_type, stat_forwarded_by_user, stat_forwarded_by_mobile, stat_forwarded_by_name, ip,
                            datetime, stat_remarks, stat_final_id, stat_forwarded_to_user); // stat_forwarded_to_user added by pr on 1stfeb19 ,stat_forwarded_by_user added modified on 1stfeb19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateStatus in ForwardAction values updated in Final audit track " + updateStatus);
                    stat_forwarded_by_user = temp_to_user; // revert to the previous value , line added by pr on 19thfeb19
                    //if (!stat_type.equals("COMPLETED") ) // in case of COMPLETED requests maila nd sms will be be sent from the creation related functions
                    // flag added in condition by pr on 1stsep2020
                    if (!stat_type.equals("completed") && !stat_forwarded_to.equals("us") && updateStatus) // line modified by pr on 9thjan19, when forwarding to the under secretary dont send another mail as it has been sent after this code
                    {
                        //System.out.println(" inside before sending sendintimation function call ");
                        Inform informObj = new Inform();
                        informObj.sendIntimation(stat_reg_no, stat_type, stat_form_type, stat_remarks, stat_forwarded_by, stat_forwarded_by_user, stat_forwarded_to, stat_forwarded_to_user);
                    }
                    //System.out.println(" inside res greater than 0 after calling sendintimation ");
                }
                ps.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 3" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 40 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "updateStatus EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 41 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "updateStatus EXCEPTION 2 " + e.getMessage());

                    }
                }
            }
        }
        return updateStatus;
    }

    // below function added by pr on 16thjan19
    public boolean update_final_trail(String registration_no, String role, String status, String email, String mobile, String name, String ip, String datetime, String remarks,
            String stat_final_id, String stat_forwarded_to_user) // stat_forwarded_to_user added by pr on 1stfeb19
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                + " inside update final trail function ");
        boolean flag = false;
        PreparedStatement ps = null;
        String role_email_field = "";
        String role_mobile_field = "";
        String role_name_field = "";
        String role_ip_field = "";
        String role_remarks_field = "";
        String role_datetime = "";
        if (role.equals("ca")) {
            role_email_field = "ca_email";
            role_mobile_field = "ca_mobile";
            role_name_field = "ca_name";
            role_ip_field = "ca_ip";
            role_remarks_field = "ca_remarks";
            role_datetime = "ca_datetime";
        } else if (role.equals("us")) {
            role_email_field = "us_email";
            role_mobile_field = "us_mobile";
            role_name_field = "us_name";
            role_ip_field = "us_ip";
            role_remarks_field = "us_remarks";
            role_datetime = "us_datetime";
        } else if (role.equals("s")) {
            role_email_field = "support_email";
            role_mobile_field = "support_mobile";
            role_name_field = "support_name";
            role_ip_field = "support_ip";
            role_remarks_field = "support_remarks";
            role_datetime = "support_datetime";
        } else if (role.equals("c")) {
            role_email_field = "coordinator_email";
            role_mobile_field = "coordinator_mobile";
            role_name_field = "coordinator_name";
            role_ip_field = "coordinator_ip";
            role_remarks_field = "coordinator_remarks";
            role_datetime = "coordinator_datetime";
        } else if (role.equals("m")) {
            role_email_field = "admin_email";
            role_mobile_field = "admin_mobile";
            role_name_field = "admin_name";
            role_ip_field = "admin_ip";
            role_remarks_field = "admin_remarks";
            role_datetime = "admin_datetime";
        } else if (role.equals("da")) {
            role_email_field = "da_email";
            role_mobile_field = "da_mobile";
            role_name_field = "da_name";
            role_ip_field = "da_ip";
            role_remarks_field = "da_remarks";
            role_datetime = "da_datetime";
        }
        // start, code added by pr on 8thapr19
        String to_email = "";
        if (status.toLowerCase().contains("rejected") || status.toLowerCase().contains("completed")) {
            to_email = email;
        } else {
            to_email = stat_forwarded_to_user;
        }
        // end, code added by pr on 8thapr19
        //to_email = "ashwin@gov.in,tiwari.ashwini@nic.in,pkr.gaurav@supportgov.in,pkr.gaurav@nic.in,tiwari.ashwini@supportgov.in";
        //to_email = "ashwin@gov.in,tiwari.ashwini@nic.in,rahulm.96@nic.in,pkr.gaurav@nic.in";
        //status  = "coordinator_pending";
        //Added by AT on 3rd Sep 2019
        //Modified by AT on 15th July 2022
        //updateOrInsertCoordinatorOrDaIds();
        //Ended by AT on 3rd Sep 2019

        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = "UPDATE final_audit_track SET " + role_email_field + " = ?, " + role_mobile_field + " = ?, " + role_name_field + " = ?, "
                    + ""
                    + " " + role_ip_field + " = ?, " + role_datetime + " = ?, status = ?, to_email = ?  , to_mobile = '', to_name = '', to_datetime = CURRENT_TIMESTAMP  "; // to_datetime added by pr on 11thfeb19 , to_name and mobile updated on 20thfeb19
            if (!remarks.equals("")) {
                qry += ", " + role_remarks_field + " = ? ";
            }
            if (!stat_final_id.equals("")) {
                qry += ", stat_final_id = ? ";
            }
            qry += " WHERE registration_no = ?";
            ps = con.prepareStatement(qry);
            ps.setString(1, email);
            ps.setString(2, mobile);
            ps.setString(3, name);
            ps.setString(4, ip);
            ps.setString(5, datetime);
            ps.setString(6, status);
            //ps.setString(7, stat_forwarded_to_user);
            ps.setString(7, to_email); // above line modified by pr on 8thapr19
            int i = 7;
            if (!remarks.equals("")) {
                i = i + 1;
                ps.setString(i, remarks);
            }
            if (!stat_final_id.equals("")) {
                i = i + 1;
                ps.setString(i, stat_final_id);
            }
            i = i + 1;
            ps.setString(i, registration_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
                    + " update status query in update_final_trail is " + ps);
            int res = ps.executeUpdate();
            if (res > 0) {
                flag = true;
            }
            // start, code added by pr on 6thmar18
            ps.close();
            // end, code added by pr on 6thmar18
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne update_final_trail exception in ForwardAction class 3" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 42 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "update_final_trail EXCEPTION 1 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 43 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "update_final_trail EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return flag;
    }

    private String updateOrInsertCoordinatorOrDaIds_Backup(String to_email, String status, String registration_no) {
        if (status.equals("coordinator_pending") && to_email.contains(",")) {
            String[] emailAddresses = to_email.split(",");
            Set<String> finalUniqueCoords = new HashSet();
            Set<String> uniqueCoords = new HashSet();
            List<String> duplicateValues = new ArrayList<>();
            for (String emailAddresse : emailAddresses) {
                if (finalUniqueCoords.contains(emailAddresse)) {
                    duplicateValues.add(emailAddresse);
                    continue;
                }
                uniqueCoords.add(emailAddresse);
                finalUniqueCoords.addAll(ldap.fetchAliases(emailAddresse));
            }

            Set<String> alias = null, id = null;
            List<Set<String>> ids = new ArrayList<>();
            to_email = "";
            for (String uniqueCoord : uniqueCoords) {
                alias = ldap.fetchAliases(uniqueCoord);
                id = db.fetchCoIds(new ArrayList<>(alias));
                ids.add(id);
                to_email += uniqueCoord + ",";
            }

            to_email = to_email.replaceAll(",$", "").toLowerCase().trim();

//            for (String emailAdrs : emailAddresses) {
//                alias = ldap.getAliases(emailAdrs);
//                id = db.fetchCoIds(new ArrayList<>(alias));
//                ids.add(id);
//            }
            Set<String> commonIdIntersection = db.findIntersection(ids);
            String commaSeperatedString = "";
            for (String str : commonIdIntersection) {
                commaSeperatedString = str;
                break;
            }

            //commaSeperatedString = commaSeperatedString.replaceAll("\\s*,\\s*$", "");
            String newIdForCoord = "";
            if (!commaSeperatedString.isEmpty()) {
                //insert query here for final_audit_track table
                db.updateFinalAuditTrack(registration_no, commaSeperatedString);
            } else {
                //insert query here for coordinator_id table
                newIdForCoord = db.insertIntoCoordsIdTable(to_email);
                if (newIdForCoord.isEmpty()) {
                    db.updateFinalAuditTrack(registration_no, "0");
                } else {
                    db.updateFinalAuditTrack(registration_no, newIdForCoord);
                }
            }
        } else if (status.equals("da_pending") && to_email.contains(",")) {
            String[] emailAddresses = to_email.split(",");

            Set<String> finalUniqueCoords = new HashSet();
            Set<String> uniqueCoords = new HashSet();
            List<String> duplicateValues = new ArrayList<>();
            for (String emailAddresse : emailAddresses) {
                if (finalUniqueCoords.contains(emailAddresse)) {
                    duplicateValues.add(emailAddresse);
                    continue;
                }
                uniqueCoords.add(emailAddresse);
                finalUniqueCoords.addAll(ldap.fetchAliases(emailAddresse));
            }

            Set<String> alias = null, id = null;
            List<Set<String>> ids = new ArrayList<>();
            to_email = "";
            for (String uniqueCoord : uniqueCoords) {
                alias = ldap.fetchAliases(uniqueCoord);
                id = db.fetchCoIds(new ArrayList<>(alias));
                ids.add(id);
                to_email += uniqueCoord + ",";
            }

            to_email = to_email.replaceAll(",$", "").toLowerCase().trim();

//            Set<String> alias = null, id = null;
//            List<Set<String>> ids = new ArrayList<>();
//            for (String emailAdrs : emailAddresses) {
//                alias = ldap.getAliases(emailAdrs);
//                id = db.fetchDaIds(new ArrayList<>(alias));
//                ids.add(id);
//            }
            Set<String> commonIdIntersection = db.findIntersection(ids);
            String commaSeperatedString = "";
            for (String str : commonIdIntersection) {
                commaSeperatedString = str;
                break;
            }

            //commaSeperatedString = commaSeperatedString.replaceAll("\\s*,\\s*$", "");
            String newIdForCoord = "";
            if (!commaSeperatedString.isEmpty()) {
                //insert query here for final_audit_track table
                db.updateFinalAuditTrackForDa(registration_no, commaSeperatedString);
            } else {
                //insert query here for coordinator_id table
                newIdForCoord = db.insertIntoDaIdTable(to_email);
                if (newIdForCoord.isEmpty()) {
                    db.updateFinalAuditTrackForDa(registration_no, "0");
                } else {
                    db.updateFinalAuditTrackForDa(registration_no, newIdForCoord);
                }
            }
        }
        return to_email;
    }

    public String updateOrInsertCoordinatorOrDaIds(String to_email, String status, String registration_no) {
        if (status.equals("coordinator_pending") && to_email.contains(",")) {
            String[] emailAddresses = to_email.split(",");
            Set<String> finalUniqueCoords = new HashSet();
            Set<String> uniqueCoords = new HashSet();
            List<String> duplicateValues = new ArrayList<>();
            for (String emailAddresse : emailAddresses) {
                if (finalUniqueCoords.contains(emailAddresse)) {
                    duplicateValues.add(emailAddresse);
                    continue;
                }
                uniqueCoords.add(emailAddresse);
                finalUniqueCoords.addAll(ldap.fetchAliases(emailAddresse));
            }

            Set<String> alias = null, id = null;
            List<Set<String>> ids = new ArrayList<>();
            to_email = "";
            for (String uniqueCoord : uniqueCoords) {
                alias = ldap.fetchAliases(uniqueCoord);
                id = db.fetchCoIds(new ArrayList<>(alias));
                ids.add(id);
                to_email += uniqueCoord + ",";
            }

            to_email = to_email.replaceAll(",$", "").toLowerCase().trim();

//            for (String emailAdrs : emailAddresses) {
//                alias = ldap.getAliases(emailAdrs);
//                id = db.fetchCoIds(new ArrayList<>(alias));
//                ids.add(id);
//            }
            Set<String> commonIdIntersection = db.findIntersection(ids);
            String commaSeperatedString = "";
            for (String str : commonIdIntersection) {
                commaSeperatedString = str;
                break;
            }

            //commaSeperatedString = commaSeperatedString.replaceAll("\\s*,\\s*$", "");
            String newIdForCoord = "";
            if (!commaSeperatedString.isEmpty()) {
                //insert query here for final_audit_track table
                db.updateFinalAuditTrack(registration_no, commaSeperatedString);
            } else {
                //insert query here for coordinator_id table
                newIdForCoord = db.insertIntoCoordsIdTable(to_email);
                if (newIdForCoord.isEmpty()) {
                    db.updateFinalAuditTrack(registration_no, "0");
                } else {
                    db.updateFinalAuditTrack(registration_no, newIdForCoord);
                }
            }
        } else if (status.equals("da_pending") && to_email.contains(",")) {
            String[] emailAddresses = to_email.split(",");

            Set<String> finalUniqueCoords = new HashSet();
            Set<String> uniqueCoords = new HashSet();
            List<String> duplicateValues = new ArrayList<>();
            for (String emailAddresse : emailAddresses) {
                if (finalUniqueCoords.contains(emailAddresse)) {
                    duplicateValues.add(emailAddresse);
                    continue;
                }
                uniqueCoords.add(emailAddresse);
                finalUniqueCoords.addAll(ldap.fetchAliases(emailAddresse));
            }

            Set<String> alias = null, id = null;
            List<Set<String>> ids = new ArrayList<>();
            to_email = "";
            for (String uniqueCoord : uniqueCoords) {
                alias = ldap.fetchAliases(uniqueCoord);
                id = db.fetchCoIds(new ArrayList<>(alias));
                ids.add(id);
                to_email += uniqueCoord + ",";
            }

            to_email = to_email.replaceAll(",$", "").toLowerCase().trim();

//            Set<String> alias = null, id = null;
//            List<Set<String>> ids = new ArrayList<>();
//            for (String emailAdrs : emailAddresses) {
//                alias = ldap.getAliases(emailAdrs);
//                id = db.fetchDaIds(new ArrayList<>(alias));
//                ids.add(id);
//            }
            Set<String> commonIdIntersection = db.findIntersection(ids);
            String commaSeperatedString = "";
            for (String str : commonIdIntersection) {
                commaSeperatedString = str;
                break;
            }

            //commaSeperatedString = commaSeperatedString.replaceAll("\\s*,\\s*$", "");
            String newIdForCoord = "";
            if (!commaSeperatedString.isEmpty()) {
                //insert query here for final_audit_track table
                db.updateFinalAuditTrackForDa(registration_no, commaSeperatedString);
            } else {
                //insert query here for coordinator_id table
                newIdForCoord = db.insertIntoDaIdTable(to_email);
                if (newIdForCoord.isEmpty()) {
                    db.updateFinalAuditTrackForDa(registration_no, "0");
                } else {
                    db.updateFinalAuditTrackForDa(registration_no, newIdForCoord);
                }
            }
        } else {
            if (status.equals("da_pending")) {
                db.updateFinalAuditTrackForDa(registration_no, "0");
            } else if (status.equals("coordinator_pending")) {
                db.updateFinalAuditTrack(registration_no, "0");
            } else {
                db.updateFinalAuditTrack(registration_no, "0");
                db.updateFinalAuditTrackForDa(registration_no, "0");
            }
        }
        return to_email;
    }

    // below function added by pr on 26thfeb18
    public ArrayList fetchEmailEquivalent() {
        ArrayList email = null;
        /*Map profile_values = (HashMap) sessionMap.get("profile-values");
         //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "MAILADDRESS in admin::: " + profile_values.get("mailequi"));
         if (profile_values.get("mailequi") != null) {
         mobile = (ArrayList) profile_values.get("mailequi");
         //System.out.println(" mailequi value is "+mobile);
         }
         profile_values = null; // line added by pr on 6thmar18*/
        // above code commented below added by pr on 19thfeb19
        if (sessionMap != null && sessionMap.get("uservalues") != null) {
            String em = "";
            UserData userdata = (UserData) sessionMap.get("uservalues");
            em = userdata.getEmail();
            ArrayList<String> ar = new ArrayList<String>();
            ar.add(em);
            email = ar;
            if (userdata.getAliases() != null) {
                //System.out.println(" inside getEmailEquivalent get alias not null  ");
                Set s = (Set) userdata.getAliases();
                // System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);
                ArrayList<String> newAr = new ArrayList<String>();
                newAr.addAll(s);
                email = newAr;
                // System.out.println(" inside getEmailEquivalent get alias not null arraylist value is  " + newAr);
                //this.mobile = (ArrayList) userdata.getAliases();             
            }
        }
        return email;
    }

    public String fetchEmailEquivalent(String inputEmail) {
        String emails = "";

//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null; // line added by pr on 16thoct18

        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            String[] IDs = {"mailequivalentaddress", "mail"};

            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = "(|(mail=" + inputEmail + ")(mailequivalentaddress=" + inputEmail + "))";

            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " search string value is " + t);
                t = t.substring(t.indexOf("{") + 1, t.indexOf("}"));

                String mailStr = t.substring(t.indexOf("mail=mail:"), t.indexOf("mailequivalentaddress=mailequivalentaddress:") - 2);

                mailStr = mailStr.replace("mail=mail: ", "");

                String mailEqvStr = t.substring(t.indexOf("mailequivalentaddress=mailequivalentaddress:"));

                mailEqvStr = mailEqvStr.replace("mailequivalentaddress=mailequivalentaddress: ", "");

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " mailEqvStr value is " + mailEqvStr);

                emails = mailStr.trim() + "," + mailEqvStr.trim();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    //// above line commented below added by pr on 23rdapr19                    
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 44 " + e.getMessage());
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "getEmailEquivalent EXCEPTION 1 " + e.getMessage());
//
//                }
//            }
        }

        return emails;
    }

    public Boolean verifyFwdToDetails_backup(String stat_reg_no, String stat_form_type, String stat_forwarded_by, String stat_forwarded_by_user) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside verifyFwdToDetails function stat_forwarded_by is " + stat_forwarded_by + "  stat_forwarded_by_user is  " + stat_forwarded_by_user);
        Boolean isValid = false;
        PreparedStatement ps = null;
        ResultSet res = null;
        UserData userdata = (UserData) sessionMap.get("uservalues");
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by FROM status  WHERE stat_reg_no = ? ORDER BY stat_id DESC limit 1";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                String stat_reg_no_db = res.getString("stat_reg_no");
                String stat_form_type_db = res.getString("stat_form_type");
                String stat_forwarded_to_db = res.getString("stat_forwarded_to");
                String stat_forwarded_by_db = res.getString("stat_forwarded_by"); // if it is forwarded by support before and again it needs to be forwarded again by support
                String stat_forwarded_to_user_db = res.getString("stat_forwarded_to_user").toLowerCase();
                // to escape this check in case of support user logged in 
                if (stat_forwarded_to_db.equals("s")) {
                    if (stat_forwarded_to_user_db.equals("support@nic.in")) {
                        stat_forwarded_by_user = "support@nic.in";
                    } else if (stat_forwarded_to_user_db.equals("smssupport@nic.in")) {
                        stat_forwarded_by_user = "smssupport@nic.in";
                    } else if (stat_forwarded_to_user_db.equals("vpnsupport@nic.in")) {
                        stat_forwarded_by_user = "vpnsupport@nic.in";
                    }
                }

                if (!stat_forwarded_by.equals("s")) // if added by preeti on 25thjan18
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside authentic CHECK before stat_form_type_db  " + stat_form_type_db + " after  " + stat_form_type
                            + " stat_forwarded_to_db " + stat_forwarded_to_db + " stat_forwarded_by " + stat_forwarded_by
                            + " stat_forwarded_to_user_db " + stat_forwarded_to_user_db + " stat_forwarded_by_user " + stat_forwarded_by_user);
//                    if (stat_forwarded_by.equals("ca")) {
//
//                        //String ca_id_all = get_all_ca_id();
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                                + " inside verifyFwdToDetails function forwarded by ca ca id all are " + stat_forwarded_by_user);
//
//                        /*if (stat_reg_no_db.equals(stat_reg_no) && stat_form_type_db.equals(stat_form_type)
//                                && stat_forwarded_to_db.equals(stat_forwarded_by) && (stat_forwarded_to_user_db.contains(stat_forwarded_by_user)
//                                || ca_id_all.contains(stat_forwarded_to_user_db)))*/
//                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
//                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by) && (stat_forwarded_to_user_db.contains(stat_forwarded_by_user.toLowerCase()))) {
//                            isValid = true;
//                        }
//                    } else 
                    if (stat_forwarded_by.equals("us")) // else if added by pr on 7thjan19 
                    {
                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by) && (stat_forwarded_to_user_db.equalsIgnoreCase(stat_forwarded_by_user))) {
                            isValid = true;
                        }
                    } else {
                        // start, code added by pr on 26thfeb18
                        ArrayList<String> emailEqui = fetchEmailEquivalent();
//                        UserData userdata = (UserData) sessionMap.get("uservalues");
//
//                        String emails = fetchEmailEquivalent(userdata.fetchIndividualEmail());
//
//                        String str[] = emails.split(",");
//
//                        List<String> nl = new ArrayList<>();
//                        nl = Arrays.asList(str);
//
//                        emailEqui.addAll(nl);

                        String aliases = "";
                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by)) // above line commented below added by pr on 5thmar18 
                        {
                            // above code commented below added by pr on 20thjul18 so that the exact mobile is checked for who has been forwarded the request to and who is handling this request
                            if (emailEqui != null && emailEqui.size() > 0) // if added by pr on 26thfeb18
                            {
                                // System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block ");
                                String[] db_fwdto_arr = stat_forwarded_to_user_db.split(",");
                                for (String dbToEmail : db_fwdto_arr) {
                                    //System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block 1 ");

                                    for (String to : emailEqui) {
                                        //  System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block 2 ");
                                        //System.out.println(" inside verify fwd  equi obj value is " + to.toString());
                                        if (dbToEmail.trim().equalsIgnoreCase(to.toString().trim())) {
                                            // System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block 3 ");
                                            isValid = true;
                                            break;
                                        }
                                        if (isValid) {
                                            break;
                                        }
                                    }
                                }
                            }
                            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside authentic ");
                        }
                    }
                } else if (stat_forwarded_by.equalsIgnoreCase(stat_forwarded_to_db)) // now the scenario is it cannot be forwarded again to the coordinator from support modified by pr on 5thjun18
                {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is valid ");
                    isValid = true;
                } else {
                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is not valid ");
                    isValid = false;
                }

                //07-apr-2022
                if (!isValid) {
                    String qry_final = "SELECT * FROM final_audit_track  WHERE registration_no = ?";
                    PreparedStatement ps_final = conSlave.prepareStatement(qry_final);
                    ps_final.setString(1, stat_reg_no);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select FINAL_AUDIT_TRACK query to verfiy details is " + ps_final);
                    ResultSet res_final = ps_final.executeQuery();

                    if (res_final.next()) {
//                        String qry_status = "INSERT INTO status SET stat_type = ?, stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = ?, stat_forwarded_by_user = ?, stat_forwarded_to = ?,"
//                                + ""
//                                + "  stat_forwarded_to_user = ? , stat_remarks = ?, stat_process='online'";

                        String qry_status = "INSERT INTO status SET stat_type = ?, stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = ?, stat_forwarded_by_user = ?, stat_forwarded_to = ?,"
                                + ""
                                + "  stat_forwarded_to_user = ? , stat_remarks = ?";
                        PreparedStatement ps_status = con.prepareStatement(qry_status);
                        ps_status.setString(1, res_final.getString("status"));
                        //ps.setString(2,)
                        ps_status.setString(2, res_final.getString("form_name"));
                        ps_status.setString(3, stat_reg_no);
                        ps_status.setString(4, stat_forwarded_to_db);
                        ps_status.setString(5, stat_forwarded_to_user_db);
                        ps_status.setString(6, findStatForwardedTo(res_final.getString("status")));
                        //ps_status.setString(7, userdata.getEmail());
                        ps_status.setString(7, res_final.getString("to_email"));
                        ps_status.setString(8, "Manually done by the backend team to resolve the conflict");
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insert status for manual query is " + ps_status);
                        int result = ps_status.executeUpdate();
                        if (result > 0) {
                            isValid = true;
                        }
                    }
                }
                //07-apr-2022

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 4" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 45 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 46 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 2 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 47 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isValid in verify details is " + isValid);
        return isValid;
    }

//    public Boolean verifyFwdToDetails(String stat_reg_no, String stat_form_type, String stat_forwarded_by, String stat_forwarded_by_user) {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside verifyFwdToDetails function stat_forwarded_by is " + stat_forwarded_by + "  stat_forwarded_by_user is  " + stat_forwarded_by_user);
//        Boolean isValid = false;
//        PreparedStatement ps = null;
//        ResultSet res = null;
//        try {
//            con = DbConnection.getConnection();
//            conSlave = DbConnection.getSlaveConnection();
//            String qry = "SELECT stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by FROM status  WHERE stat_reg_no = ? ORDER BY stat_id DESC limit 1";
//            ps = conSlave.prepareStatement(qry);
//            ps.setString(1, stat_reg_no);
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps);
//            res = ps.executeQuery();
//            while (res.next()) {
//                String stat_reg_no_db = res.getString("stat_reg_no");
//                String stat_form_type_db = res.getString("stat_form_type");
//                String stat_forwarded_to_db = res.getString("stat_forwarded_to");
//                String stat_forwarded_by_db = res.getString("stat_forwarded_by"); // if it is forwarded by support before and again it needs to be forwarded again by support
//                String stat_forwarded_to_user_db = res.getString("stat_forwarded_to_user").toLowerCase();
//                // to escape this check in case of support user logged in 
//                if (stat_forwarded_to_db.equals("s")) {
//                    stat_forwarded_by_user = stat_forwarded_to_user_db;
//                    isValid = true;
//                } else {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside authentic CHECK before stat_form_type_db  " + stat_form_type_db + " after  " + stat_form_type
//                            + " stat_forwarded_to_db " + stat_forwarded_to_db + " stat_forwarded_by " + stat_forwarded_by
//                            + " stat_forwarded_to_user_db " + stat_forwarded_to_user_db + " stat_forwarded_by_user " + stat_forwarded_by_user);
//                    if (stat_forwarded_by.equals("us")) // else if added by pr on 7thjan19 
//                    {
//                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
//                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by) && (stat_forwarded_to_user_db.equalsIgnoreCase(stat_forwarded_by_user))) {
//                            isValid = true;
//                        }
//                    } else {
//                        // start, code added by pr on 26thfeb18
//                        ArrayList<String> emailEqui = fetchEmailEquivalent();
//                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
//                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by)) {
//                            // above code commented below added by pr on 20thjul18 so that the exact mobile is checked for who has been forwarded the request to and who is handling this request
//                            if (emailEqui != null && emailEqui.size() > 0) {
//                                // System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block ");
//                                String[] db_fwdto_arr = stat_forwarded_to_user_db.split(",");
//                                for (String dbToEmail : db_fwdto_arr) {
//                                    for (String to : emailEqui) {
//                                        if (dbToEmail.trim().equalsIgnoreCase(to.toString().trim())) {
//                                            isValid = true;
//                                            break;
//                                        }
//                                    }
//                                    if (isValid) {
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                    }
//                  }
//                } else if (stat_forwarded_by.equalsIgnoreCase(stat_forwarded_to_db)) // now the scenario is it cannot be forwarded again to the coordinator from support modified by pr on 5thjun18
//                {
//                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is valid ");
//                    isValid = true;
//                } else {
//                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is not valid ");
//                    isValid = false;
//                }
//
//               
////                else if (stat_forwarded_by.equalsIgnoreCase(stat_forwarded_to_db)) // now the scenario is it cannot be forwarded again to the coordinator from support modified by pr on 5thjun18
////                {
////                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is valid ");
////                    isValid = true;
////                }
////                else {
////                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is not valid ");
////                    isValid = false;
////                }
//
//
//                //07-apr-2022
//                if (!isValid) {
//                    String qry_final = "SELECT * FROM final_audit_track  WHERE registration_no = ?";
//                    PreparedStatement ps_final = conSlave.prepareStatement(qry_final);
//                    ps_final.setString(1, stat_reg_no);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select FINAL_AUDIT_TRACK query to verfiy details is " + ps_final);
//                    ResultSet res_final = ps_final.executeQuery();
//
//                    if (res_final.next()) {
//                        String qry_status = "INSERT INTO status SET stat_type = ?, stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = ?, stat_forwarded_by_user = ?, stat_forwarded_to = ?,"
//                                + ""
//                                + "  stat_forwarded_to_user = ? , stat_remarks = ?";
//                        PreparedStatement ps_status = con.prepareStatement(qry_status);
//                        ps_status.setString(1, res_final.getString("status"));
//                        //ps.setString(2,)
//                        ps_status.setString(2, res_final.getString("form_name"));
//                        ps_status.setString(3, stat_reg_no);
//                        ps_status.setString(4, stat_forwarded_to_db);
//                        ps_status.setString(5, stat_forwarded_to_user_db);
//                        ps_status.setString(6, findStatForwardedTo(res_final.getString("status")));
//                        //ps_status.setString(7, userdata.getEmail());
//                        ps_status.setString(7, res_final.getString("to_email"));
//                        ps_status.setString(8, "Manually done by the backend team to resolve the conflict");
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insert status for manual query is " + ps_status);
//                        int result = ps_status.executeUpdate();
//                        if (result > 0) {
//                            isValid = true;
//                        }
//                    }
//                }
//                //07-apr-2022
//               }
//            
//          }catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 4" + e.getMessage());
//        } finally {
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    //// above line commented below added by pr on 23rdapr19                    
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 45 " + e.getMessage());
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 1 " + e.getMessage());
//
//                }
//            }
//            if (res != null) {
//                try {
//                    res.close();
//                } catch (Exception e) {
//                    //// above line commented below added by pr on 23rdapr19                    
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 46 " + e.getMessage());
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 2 " + e.getMessage());
//
//                }
//            }
//            if (con != null) {
//                try {
//                    // con.close();
//                } catch (Exception e) {
//                    //// above line commented below added by pr on 23rdapr19                    
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 47 " + e.getMessage());
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 3 " + e.getMessage());
//
//                }
//            }
//        }
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isValid in verify details is " + isValid);
//        return isValid;
//    }
    public Boolean verifyFwdToDetails(String stat_reg_no, String stat_form_type, String stat_forwarded_by, String stat_forwarded_by_user) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside verifyFwdToDetails function stat_forwarded_by is " + stat_forwarded_by + "  stat_forwarded_by_user is  " + stat_forwarded_by_user);
        Boolean isValid = false;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by FROM status  WHERE stat_reg_no = ? ORDER BY stat_id DESC limit 1";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                String stat_reg_no_db = res.getString("stat_reg_no");
                String stat_form_type_db = res.getString("stat_form_type");
                String stat_forwarded_to_db = res.getString("stat_forwarded_to");
                String stat_forwarded_by_db = res.getString("stat_forwarded_by"); // if it is forwarded by support before and again it needs to be forwarded again by support
                String stat_forwarded_to_user_db = res.getString("stat_forwarded_to_user").toLowerCase();
                if (stat_forwarded_to_db.equals("s")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inside verifyFwdToDetails first if stat_forwarded_to_db = " + stat_forwarded_to_db);
                    stat_forwarded_by_user = stat_forwarded_to_user_db;
                    isValid = true;
                } else if ((stat_forwarded_to_db.equals("c") || stat_forwarded_to_db.equals("m") || stat_forwarded_to_db.equals("d")) && stat_forwarded_by.equals("s")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "Inside verifyFwdToDetails second if stat_forwarded_to_db = " + stat_forwarded_to_db);
                    stat_forwarded_by_user = stat_forwarded_to_user_db;
                    isValid = true;
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside authentic CHECK before stat_form_type_db  " + stat_form_type_db + " after  " + stat_form_type
                            + " stat_forwarded_to_db " + stat_forwarded_to_db + " stat_forwarded_by " + stat_forwarded_by
                            + " stat_forwarded_to_user_db " + stat_forwarded_to_user_db + " stat_forwarded_by_user " + stat_forwarded_by_user);
                    if (stat_forwarded_by.equals("us")) // else if added by pr on 7thjan19 
                    {
                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by) && (stat_forwarded_to_user_db.equalsIgnoreCase(stat_forwarded_by_user))) {
                            isValid = true;
                        }
                    } else {
                        ArrayList<String> emailEqui = fetchEmailEquivalent();
                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by)) {
                            if (emailEqui != null && emailEqui.size() > 0) {
                                String[] db_fwdto_arr = stat_forwarded_to_user_db.split(",");
                                for (String dbToEmail : db_fwdto_arr) {
                                    for (String to : emailEqui) {
                                        if (dbToEmail.trim().equalsIgnoreCase(to.toString().trim())) {
                                            isValid = true;
                                            break;
                                        }
                                    }
                                    if (isValid) {
                                        break;
                                    }
                                }
                            }
                        }

//                        if (!isValid) {
//                            String qry_final = "SELECT * FROM final_audit_track  WHERE registration_no = ?";
//                            PreparedStatement ps_final = conSlave.prepareStatement(qry_final);
//                            ps_final.setString(1, stat_reg_no);
//                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select FINAL_AUDIT_TRACK query to verfiy details is " + ps_final);
//                            ResultSet res_final = ps_final.executeQuery();
//                            if (res_final.next()) {
//                                String qry_status = "INSERT INTO status SET stat_type = ?, stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = ?, stat_forwarded_by_user = ?, stat_forwarded_to = ?,"
//                                        + ""
//                                        + "  stat_forwarded_to_user = ? , stat_remarks = ?";
//                                PreparedStatement ps_status = con.prepareStatement(qry_status);
//                                ps_status.setString(1, res_final.getString("status"));
//                                //ps.setString(2,)
//                                ps_status.setString(2, res_final.getString("form_name"));
//                                ps_status.setString(3, stat_reg_no);
//                                ps_status.setString(4, stat_forwarded_to_db);
//                                ps_status.setString(5, stat_forwarded_to_user_db);
//                                ps_status.setString(6, findStatForwardedTo(res_final.getString("status")));
//                                //ps_status.setString(7, userdata.getEmail());
//                                ps_status.setString(7, res_final.getString("to_email"));
//                                ps_status.setString(8, "Manually done by the backend team to resolve the conflict");
//                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insert status for manual query is " + ps_status);
//                                int result = ps_status.executeUpdate();
//                                if (result > 0) {
//                                    isValid = true;
//                                }
//                            }
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 4" + e.getMessage());

        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 45 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 1 " + e.getMessage());
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 46 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 47 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isValid in verify details is " + isValid);
        return isValid;

    }

    public String findDa(String domain) {
        PreparedStatement ps = null;
        ResultSet res = null;
        List<String> daEmails = new ArrayList<>();
        String finalEmails = "";
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            if (domain == null || domain.isEmpty()) {
                qry = "SELECT email FROM gem_delegated_admins";
                ps = conSlave.prepareStatement(qry);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to fetchDas :: " + ps);
                res = ps.executeQuery();
                while (res.next()) {
                    daEmails.add(res.getString("email"));
                }
            } else {
                qry = "SELECT email FROM gem_delegated_admins WHERE domain = ?";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, domain);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to fetchDas :: " + ps);
                res = ps.executeQuery();
                while (res.next()) {
                    daEmails.add(res.getString("email"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 4" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 45 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 46 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 2 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 47 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        for (String daEmail : daEmails) {
            finalEmails += daEmail + ",";
        }
        finalEmails = finalEmails.replaceAll(",$", "");
        return finalEmails.trim();
    }

    public Boolean verifyFwdToDetails_Backup12March2022(String stat_reg_no, String stat_form_type, String stat_forwarded_by, String stat_forwarded_by_user) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside verifyFwdToDetails function stat_forwarded_by is " + stat_forwarded_by + "  stat_forwarded_by_user is  " + stat_forwarded_by_user);
        Boolean isValid = false;
        PreparedStatement ps = null;
        ResultSet res = null;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by FROM status  WHERE stat_reg_no = ? ORDER BY stat_id DESC limit 1";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                String stat_reg_no_db = res.getString("stat_reg_no");
                String stat_form_type_db = res.getString("stat_form_type");
                String stat_forwarded_to_db = res.getString("stat_forwarded_to");
                String stat_forwarded_by_db = res.getString("stat_forwarded_by"); // if it is forwarded by support before and again it needs to be forwarded again by support
                String stat_forwarded_to_user_db = res.getString("stat_forwarded_to_user").toLowerCase();
                // to escape this check in case of support user logged in 
                if (stat_forwarded_to_db.equals("s")) {
                    if (stat_forwarded_to_user_db.equals("support@nic.in")) {
                        stat_forwarded_by_user = "support@nic.in";
                    } else if (stat_forwarded_to_user_db.equals("smssupport@nic.in")) {
                        stat_forwarded_by_user = "smssupport@nic.in";
                    } else if (stat_forwarded_to_user_db.equals("vpnsupport@nic.in")) {
                        stat_forwarded_by_user = "vpnsupport@nic.in";
                    }
                }

                if (!stat_forwarded_by.equals("s")) // if added by preeti on 25thjan18
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside authentic CHECK before stat_form_type_db  " + stat_form_type_db + " after  " + stat_form_type
                            + " stat_forwarded_to_db " + stat_forwarded_to_db + " stat_forwarded_by " + stat_forwarded_by
                            + " stat_forwarded_to_user_db " + stat_forwarded_to_user_db + " stat_forwarded_by_user " + stat_forwarded_by_user);
//                    if (stat_forwarded_by.equals("ca")) {
//
//                        //String ca_id_all = get_all_ca_id();
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())
//                                + " inside verifyFwdToDetails function forwarded by ca ca id all are " + stat_forwarded_by_user);
//
//                        /*if (stat_reg_no_db.equals(stat_reg_no) && stat_form_type_db.equals(stat_form_type)
//                                && stat_forwarded_to_db.equals(stat_forwarded_by) && (stat_forwarded_to_user_db.contains(stat_forwarded_by_user)
//                                || ca_id_all.contains(stat_forwarded_to_user_db)))*/
//                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
//                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by) && (stat_forwarded_to_user_db.contains(stat_forwarded_by_user.toLowerCase()))) {
//                            isValid = true;
//                        }
//                    } else 
                    if (stat_forwarded_by.equals("us")) // else if added by pr on 7thjan19 
                    {
                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by) && (stat_forwarded_to_user_db.equalsIgnoreCase(stat_forwarded_by_user))) {
                            isValid = true;
                        }
                    } else {
                        // start, code added by pr on 26thfeb18
                        ArrayList<String> emailEqui = fetchEmailEquivalent();
                        UserData userdata = (UserData) sessionMap.get("uservalues");

                        String emails = fetchEmailEquivalent(userdata.fetchIndividualEmail());

                        String str[] = emails.split(",");

                        List<String> nl = new ArrayList<>();
                        nl = Arrays.asList(str);

                        emailEqui.addAll(nl);

                        String aliases = "";
                        if (stat_reg_no_db.equalsIgnoreCase(stat_reg_no) && stat_form_type_db.equalsIgnoreCase(stat_form_type)
                                && stat_forwarded_to_db.equalsIgnoreCase(stat_forwarded_by)) // above line commented below added by pr on 5thmar18 
                        {
                            // above code commented below added by pr on 20thjul18 so that the exact mobile is checked for who has been forwarded the request to and who is handling this request
                            if (emailEqui != null && emailEqui.size() > 0) // if added by pr on 26thfeb18
                            {
                                // System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block ");
                                String[] db_fwdto_arr = stat_forwarded_to_user_db.split(",");
                                for (String dbToEmail : db_fwdto_arr) {
                                    //System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block 1 ");

                                    for (String to : emailEqui) {
                                        //  System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block 2 ");
                                        //System.out.println(" inside verify fwd  equi obj value is " + to.toString());
                                        if (dbToEmail.trim().equalsIgnoreCase(to.toString().trim())) {
                                            // System.out.println(" inside verifyFwdToDetails function and mobile equivalent if block 3 ");
                                            isValid = true;
                                            break;
                                        }
                                        if (isValid) {
                                            break;
                                        }
                                    }
                                }
                            }
                            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside authentic ");
                        }
                    }
                } else if (stat_forwarded_by.equalsIgnoreCase(stat_forwarded_to_db)) // now the scenario is it cannot be forwarded again to the coordinator from support modified by pr on 5thjun18
                {
                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is valid ");
                    isValid = true;
                } else {
                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside verifyFwdToDetails func and  is not valid ");
                    isValid = false;
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne updateStatus in ForwardAction class 4" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 45 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 46 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 2 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 47 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "verifyFwdToDetails EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isValid in verify details is " + isValid);
        return isValid;
    }

    public String fetchCoordinator() {
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isnide getCoords func regNo " + regNo);
        // System.out.println("CCOOO:: "+ coorddata);
        coorddata.clear();
        // System.out.println(" inside get coords ");
        String reg_no = "", formName = "";
        HashMap empDetails = null;
        String[] arr = null;
        //coorddata.clear(); // line added by pr on 5thfeb18
        //if( regNo != null &&  !regNo.equals("") && regNo.matches("^[a-zA-Z0-9~\\-]*$") ) // line modified by pr on 5thfeb18 for xss
        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegEx)) // line modified by pr on 5thfeb18 for xss
        {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside regno matches ");
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        } else {
            regNo = ""; // line added by pr on 6thfeb18
        }
        // in this case action type will be forward only
        if (arr != null && arr.length == 3) {
            //System.out.println(" inside arr length 3 ");
            // get the details of employment from the respective table across the reg no
            reg_no = arr[0];
            formName = arr[1];
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " formName " + formName + " reg_no " + reg_no);
            empDetails = fetchEmploymentDetails(formName, reg_no);
            // System.out.println(" empDetails size is "+empDetails.size());
            if (empDetails != null) {
                empdata.clear();
                empdata = empDetails;
                // get coordinators from a function in an arraylist
                coorddata.clear(); // commented by pr on 5thfeb18
                coorddata = fetchEmploymentCoords(empDetails);

                //below check added by sahil to fetch all coordinator in case got null or empty from above method "fetchEmploymentCoords".
                if (coorddata == null || coorddata.isEmpty()) {
                    setIsEmptyCoordList(true);
                    coorddata = fetchAllCoordinators();

                }
                // System.out.println(" CO coorddata size is "+coorddata.size());
                // add these into a session so as to check while forwarding, code added by pr on 11thjan18
                sessionMap.put("coorddata", coorddata);
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " isnide getCoords func size of coord data is " + coorddata.size());
        return SUCCESS;
    }

    // below function added by pr on 6thmar18
    public String fetchDAs() {
        dadata.clear();
        String reg_no = "", formName = "";
        HashMap empDetails = null;
        String[] arr = null;
        //coorddata.clear(); // line added by pr on 5thfeb18
        //if( regNo != null &&  !regNo.equals("") && regNo.matches("^[a-zA-Z0-9~\\-]*$") ) // line modified by pr on 5thfeb18 for xss
        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegEx)) // line modified by pr on 5thfeb18 for xss
        {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside regno matches ");
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        } else {
            regNo = ""; // line added by pr on 6thfeb18
        }
        // in this case action type will be forward only
        if (arr != null && arr.length == 3) {
            // get the details of employment from the respective table across the reg no
            reg_no = arr[0];
            formName = arr[1];
            empDetails = fetchEmploymentDetails(formName, reg_no);
            //System.out.println(" inside empdetails size is "+empDetails.size() );
            if (empDetails != null) {
                empdata.clear();
                empdata = empDetails;
                // get coordinators from a function in an arraylist
                dadata.clear(); // commented by pr on 5thfeb18
                dadata = fetchEmploymentDAs(empDetails);
                // add these into a session so as to check while forwarding, code added by pr on 11thjan18
                sessionMap.put("coorddata", dadata);
            }
        }
        return SUCCESS;
    }

    public HashMap fetchEmploymentDetails(String formName, String regNo) {
        //HashMap<String, String> hm = new HashMap<String, String>();
        LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>(); // above line commented this added by pr on 2ndmay18        
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchEmploymentDetails regNo value is " + regNo);
        // fetch from sms table employment details
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            //String qry = " SELECT employment, ministry, department, state, organization, other_dept,add_state FROM ";
            String qry = " SELECT employment, ministry, department, state, organization, other_dept,add_state, city, auth_off_name, designation, emp_code, address, pin, "
                    + "  ophone, rphone, mobile, auth_email, hod_email, hod_name, hod_mobile, hod_telephone, ca_desig  ";

            // start, code added by pr on 10thjan2020
            if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
                qry += ", type, emp_type, preferred_email1, preferred_email2 ";
            } else if (formName.equals(Constants.BULK_FORM_KEYWORD)) {
                qry += ", type, emp_type ";
            } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD)) {
                qry += ", preferred_email1, preferred_email2 ";
            }

            qry += "FROM ";

            // end, code added by pr on 10thjan2020
            if (formName.equals(Constants.SMS_FORM_KEYWORD)) {
                qry += " sms_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.BULK_FORM_KEYWORD)) {
                qry += " bulk_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.IP_FORM_KEYWORD)) {
                qry += " ip_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.DIST_FORM_KEYWORD)) {
                qry += " distribution_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.BULKDIST_FORM_KEYWORD)) {          // Added by Rahul jan 2021
                qry += " bulk_distribution_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.GEM_FORM_KEYWORD)) {
                qry += " gem_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.IMAP_FORM_KEYWORD)) {
                qry += " imappop_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.LDAP_FORM_KEYWORD)) {
                qry += " ldap_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.MOB_FORM_KEYWORD)) {
                qry += " mobile_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
                qry += " nkn_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.RELAY_FORM_KEYWORD)) {
                qry += " relay_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
                qry += " single_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.WIFI_FORM_KEYWORD)) {
                qry += " wifi_registration WHERE registration_no = ?  ";
            }// below else if added by pr on 4thjan18
            else if (formName.equals(Constants.WIFI_PORT_FORM_KEYWORD)) {
                qry += " wifiport_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || formName.equals(Constants.VPN_BULK_FORM_KEYWORD) || formName.equals(Constants.VPN_RENEW_FORM_KEYWORD) || formName.equals(Constants.VPN_ADD_FORM_KEYWORD) || formName.equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || formName.equals(Constants.VPN_DELETE_FORM_KEYWORD)) {
                qry += " vpn_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.DNS_FORM_KEYWORD))// else if added by pr on 24thjan18
            {
                qry += " dns_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.WEBCAST_FORM_KEYWORD))// else if added by pr on 24thjan18
            {
                qry += " webcast_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.FIREWALL_FORM_KEYWORD))// else if added by pr on 24thjan18
            {
                qry += " centralutm_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.EMAILACTIVATE_FORM_KEYWORD))// else if added by pr on 24thjan18
            {
                qry += " email_act_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD))// else if added by pr on 24thjan18
            {
                qry += " email_deact_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.DOR_EXT_FORM_KEYWORD))// else if added by sahil on 6 april
            {
                qry += " dor_ext_registration WHERE registration_no = ?  ";
            } else if (formName.equals(Constants.DAONBOARDING_FORM_KEYWORD))// else if added by pr on 24thjan18
            {
                qry += " daonboarding_registration WHERE registration_no = ?  ";
            }

            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchEmploymentDetails query is " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("employment", rs.getString("employment"));
                hm.put("ministry", rs.getString("ministry"));
                hm.put("min", rs.getString("ministry"));
                hm.put("state", rs.getString("state"));
                hm.put("department", rs.getString("department"));
                hm.put("organization", rs.getString("organization"));
                hm.put("other_dept", rs.getString("other_dept"));
                hm.put("district", rs.getString("city"));
                hm.put("form_name", formName); // line added by pr on 26thmar18
                hm.put("add_state", rs.getString("add_state")); // line added by pr on 26thmar18    
                // start, code added by pr on 2ndmay18
                hm.put("auth_off_name", rs.getString("auth_off_name"));
                hm.put("designation", rs.getString("designation"));
                hm.put("emp_code", rs.getString("emp_code"));
                hm.put("address", rs.getString("address"));
                hm.put("pin", rs.getString("pin"));
                hm.put("ophone", rs.getString("ophone"));
                hm.put("rphone", rs.getString("rphone"));
                hm.put("mobile", rs.getString("mobile"));
                hm.put("auth_email", rs.getString("auth_email"));
                hm.put("hod_email", rs.getString("hod_email"));
                hm.put("hod_name", rs.getString("hod_name"));
                hm.put("hod_mobile", rs.getString("hod_mobile"));
                hm.put("hod_telephone", rs.getString("hod_telephone"));
                hm.put("ca_desig", rs.getString("ca_desig"));
                // end, code added by pr on 2ndmay18  
                hm.put("city", rs.getString("city")); // line added by pr 20thaug2020

                // start, code added by pr on 10thjan2020
                if (formName.equals(Constants.SINGLE_FORM_KEYWORD)) {
                    hm.put("emp_type", rs.getString("emp_type"));
                    hm.put("type", rs.getString("type"));
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("preferred_email2", rs.getString("preferred_email2"));
                } else if (formName.equals(Constants.BULK_FORM_KEYWORD)) {
                    hm.put("emp_type", rs.getString("emp_type"));
                    hm.put("type", rs.getString("type"));

                    qry = "select mail from bulk_users WHERE registration_no = ? and error_status = 0";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, regNo);
                    System.out.println("select bulk record:::::::" + ps);
                    rs = ps.executeQuery();
                    // above code commented below added by pr on 19thfeb18
                    String preferredEmail = "";
                    String prefEmailTemp = "";
                    while (rs.next()) {
                        prefEmailTemp = rs.getString("mail").trim().toLowerCase();
                        if (prefEmailTemp.contains("@")) {
                            preferredEmail += prefEmailTemp + ",";
                        }
                    }
                    preferredEmail = preferredEmail.replaceAll("\\s*,\\s*$", "");
                    hm.put("preferred_email1", preferredEmail);
                } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD)) {
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("preferred_email2", rs.getString("preferred_email2"));
                } else if (formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
                    qry = "select mail from bulk_users WHERE registration_no = ? and error_status = 0 ";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, regNo);
                    System.out.println("select bulk record:::::::" + ps);
                    rs = ps.executeQuery();
                    // above code commented below added by pr on 19thfeb18
                    String preferredEmail = "";
                    String prefEmailTemp = "";
                    while (rs.next()) {
                        prefEmailTemp = rs.getString("mail").trim().toLowerCase();
                        if (prefEmailTemp.contains("@")) {
                            preferredEmail += prefEmailTemp + ",";
                        }
                    }
                    preferredEmail = preferredEmail.replaceAll("\\s*,\\s*$", "");
                    hm.put("preferred_email1", preferredEmail);
                }
                // end, code added by pr on 10thjan2020              
            }
            // below lines added by pr on 6thmar18
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne fetchEmploymentDetails in ForwardAction class " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 48 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDetails EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 49 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDetails EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 50 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDetails EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        return hm;
    }

    public ArrayList<String> fetchEmploymentCoords(HashMap empDetails) {
        //System.out.println(" inside fetchEmploymentCoords function ");
        ArrayList<String> empCoords = new ArrayList<String>();
        String employment = "", ministry = "", department = "", organization = "", other_dept = "", state = "";
        String form_name = "", add_state = ""; // line added by pr on 26thmar18
        if (empDetails != null && empDetails.get("employment") != null) {
            employment = empDetails.get("employment").toString();
            if (empDetails.get("ministry") != null) {
                ministry = empDetails.get("ministry").toString();
            }
            if (empDetails.get("department") != null) {
                department = empDetails.get("department").toString();
            }
            if (empDetails.get("state") != null) {
                state = empDetails.get("state").toString();
            }
            if (empDetails.get("organization") != null) {
                organization = empDetails.get("organization").toString();
            }
            if (empDetails.get("other_dept") != null) {
                other_dept = empDetails.get("other_dept").toString();
            }
            // below if added by pr on 26thmar18
            if (empDetails.get("add_state") != null && !empDetails.get("add_state").equals("")) {
                add_state = empDetails.get("add_state").toString();
            }
        }
        // if added by pr on 26thmar18
        if (empDetails.get("form_name") != null && !empDetails.get("form_name").equals("")) {
            form_name = empDetails.get("form_name").toString();
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        if (employment.equalsIgnoreCase("Central") || employment.equalsIgnoreCase("UT")) {
            //if (employment.equalsIgnoreCase("central")) {
            // get ministry and department and other_dept and serach in ministry and ministry_department tables for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // check if this da already exists corresponding to the ministry department
                /*String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                 + " AND emp_dept = ? AND ( emp_coord_email !=  emp_admin_email  ) ";*/
                //below query commented by sahil 15 jul2021
//                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                        + " AND emp_dept = ? AND ( emp_coord_email !=  emp_admin_email  )  ";
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND (emp_type = 'c'  or  emp_type = 'dc')";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, ministry);
                ps.setString(3, department);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " central getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
//                if (form_name.equals("sms")) {
//                    /*qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                     + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email  ) ";*/
//                    //below query commented by sahil 15 jul2021
////                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
////                            + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email  ) AND emp_status = 'a' ";
//                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                            + " AND emp_dept = ? AND ( emp_type = 'c' or emp_type = 'dc'  ) AND emp_status = 'a' ";
//                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setString(1, employment);
//                    ps.setString(2, ministry);
//                    ps.setString(3, department);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " central getcoords query  for da is " + ps);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
//                        {
//                            empCoords.add(rs.getString("emp_coord_email"));
//                        }
//                    }
//                }
                if (!add_state.equalsIgnoreCase("delhi")) // if the state is non delhi then show the coordinators for that state
                {
                    /*qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = 'State' AND emp_min_state_org = ? " +
                     " AND emp_dept = 'other' AND ( emp_coord_email !=  emp_admin_email  ) ";*/
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = 'State' AND emp_min_state_org = ? "
                            + " AND emp_dept = 'other' AND (emp_type = 'c'  or  emp_type = 'dc')"; // above query commented and this added by pr on 27thmar18
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, add_state);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " central getcoords  non delhi query is " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function minstry depatrment " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 51 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 52 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 2 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 53 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 3 " + e.getMessage());

                    }
                }
            }
        } else if (employment.equalsIgnoreCase("state")) {
            Boolean isOther = false; // line added by pr on 23rdmar18
            // get department and other_dept and serach in state_department table for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
                isOther = true; // line added by pr on 23rdmar18                
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                //below query commented by sahil 15 jul2021
//                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                        + " AND emp_dept = ? AND ( emp_coord_email !=  emp_admin_email  )";
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND ( emp_type = 'c' or emp_type = 'dc' )";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, state);
                ps.setString(3, department);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " state getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                if (isOther) // if added by pr on 23rdmar18
                {
                    //below query commented by sahil 15 jul2021
//                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                            + " AND emp_dept = 'other' AND ( emp_coord_email !=  emp_admin_email  )";
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                            + " AND emp_dept = 'other' AND ( emp_type = 'c' or emp_type = 'dc')";
                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, state);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
//                if (form_name.equals("sms")) {
//                    //below query commented by sahil 15 jul2021
////                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
////                            + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email  )";
//                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                            + " AND emp_dept = ? AND ( emp_type = 'c'  or emp_type = 'dc')";
//                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setString(1, employment);
//                    ps.setString(2, state);
//                    ps.setString(3, department);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " state getcoords query is " + ps);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
//                        {
//                            empCoords.add(rs.getString("emp_coord_email"));
//                        }
//                    }
//                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function state dept " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 54 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 4 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 55 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 5 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 56 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 6 " + e.getMessage());

                    }
                }
            }
        } else if (employment.equalsIgnoreCase("others")) {
            // get organization and other_dept and serach in organization table for coordinator
            // if organization contains other then take other_dept as department
            if (organization.equalsIgnoreCase("other") && !other_dept.equals("")) {
                organization = other_dept;
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                //below query commented by sahil 15 jul2021
//                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                        + " AND ( emp_coord_email !=  emp_admin_email  )";
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND ( emp_type = 'c' or emp_type = 'dc')";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
//                if (form_name.equals("sms")) {
//                    //below query commented by sahil 15 jul2021
////                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
////                            + " AND ( emp_coord_email =  emp_admin_email  )";
//                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                            + " AND ( emp_type = 'c' or emp_type  = 'dc')";
//                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setString(1, employment);
//                    ps.setString(2, organization);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
//                        {
//                            empCoords.add(rs.getString("emp_coord_email"));
//                        }
//                    }
//                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function organization  3" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 57 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 6 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 58 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 7 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 59 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 8 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("Psu")) // else if added by pr on 22ndfeb18
        {
            // get organization and other_dept and serach in organization table for coordinator
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                //below query commented by sahil 15 jul2021
//                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                        + " AND ( emp_coord_email !=  emp_admin_email  ) ";
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND ( emp_type = 'c' or emp_type = 'dc' ) ";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " Psu getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
//                if (form_name.equals("sms")) {
//                    //below query commented by sahil 15 jul2021
////                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
////                            + " AND ( emp_coord_email =  emp_admin_email  )";
//                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                            + " AND ( emp_type = 'c' or emp_type  = 'dc'  )";
//                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setString(1, employment);
//                    ps.setString(2, organization);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
//                        {
//                            empCoords.add(rs.getString("emp_coord_email"));
//                        }
//                    }
//                }
                // end, code added by pr on 26thmar18
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function psu_department " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 60 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 9 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 61 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 10 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 62 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 11 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("Const")) // else if added by pr on 22ndfeb18
        {
            // get organization and other_dept and serach in organization table for coordinator
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                //below query commented by sahil 15 jul2021
//                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                        + " AND ( emp_coord_email !=  emp_admin_email  ) ";
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND ( emp_type = 'c' or emp_type = 'dc' ) ";
                qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " Const getcoords query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // start, code added by pr on 26thmar18 , if it an sms form then the DAs must be visible in the choose coordinaor dropdown
//                if (form_name.equals("sms")) {
//                    //below query commented by sahil 15 jul2021
////                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
////                            + " AND ( emp_coord_email =  emp_admin_email  )";
//                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
//                            + " AND ( emp_type = 'c' or emp_type = 'dc' )";
//                    qry += " AND emp_status = 'a' "; // query added by pr on 7thfeb19                  
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setString(1, employment);
//                    ps.setString(2, organization);
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " others getcoords query is " + ps);
//                    rs = ps.executeQuery();
//                    while (rs.next()) {
//                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
//                        {
//                            empCoords.add(rs.getString("emp_coord_email"));
//                        }
//                    }
//                }
                // end, code added by pr on 26thmar18
                // below lines added by pr on 6thmar18
                ps.close();
                rs.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function constitutional_department " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                   
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 63 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 12 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 64 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 13 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                   
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 65 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentCoords EXCEPTION 14 " + e.getMessage());
                    }
                }
            }
        }
        return empCoords;
    }

    public Boolean setEmploymentDAs(HashMap empDetails, String coordinator) {
        Boolean isSet = false;
        String employment = "", ministry = "", department = "", organization = "", other_dept = "", state = "";
        String role = fetchSessionRole(); // line added by pr on 26thmar18
        String logged_in_mail = fetchSessionEmail(); // line added by pr on 26thmar18
        if (empDetails != null && empDetails.get("employment") != null) {
            employment = empDetails.get("employment").toString();
            if (empDetails.get("ministry") != null) {
                ministry = empDetails.get("ministry").toString();
            }
            if (empDetails.get("department") != null) {
                department = empDetails.get("department").toString();
            }
            if (empDetails.get("state") != null) {
                state = empDetails.get("state").toString();
            }
            if (empDetails.get("organization") != null) {
                organization = empDetails.get("organization").toString();
            }
            if (empDetails.get("other_dept") != null) {
                other_dept = empDetails.get("other_dept").toString();
            }
        }
        String emp_mail_acc_cat = "", emp_sms_acc_cat = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        // get acc description from employment_coordinator table based on employment details
        try {
            // below code added by pr on 19thdec17
            con = DbConnection.getConnection();
            // no department here
            String qry = " select emp_mail_acc_cat, emp_sms_acc_cat FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                    + " AND emp_dept = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, employment);
            ps.setString(2, ministry);
            ps.setString(3, department);
            rs = ps.executeQuery();
            while (rs.next()) {
                emp_mail_acc_cat = rs.getString("emp_mail_acc_cat");
                emp_sms_acc_cat = rs.getString("emp_sms_acc_cat");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function constitutional_department " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 66 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 67 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 68 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        if (employment.equalsIgnoreCase("Central") || employment.equalsIgnoreCase("UT")) {
            // if (employment.equalsIgnoreCase("central")) {
            // get ministry and department and other_dept and serach in ministry and ministry_department tables for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // check if this da already exists corresponding to the ministry department
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email  ) AND emp_coord_email = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, ministry);
                ps.setString(3, department);
                ps.setString(4, coordinator);
                rs = ps.executeQuery();
                rs.last();
                if (rs.getRow() > 0) {
                    isSet = false;
                    isError = true;
                    isSuccess = false;
                    msg = "DA-Admin Already Exist!";
                } else {
                    qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_dept = ?, "
                            + "emp_coord_email = ?, emp_admin_email = ? , emp_mail_acc_cat = '" + emp_mail_acc_cat + "', emp_sms_acc_cat = '" + emp_sms_acc_cat + "' "
                            + ""
                            + " , emp_addedby = ?  ";  // query modified by pr on 26thmar18 added emp_addedby
                    ps = con.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, ministry);
                    ps.setString(3, department);
                    ps.setString(4, coordinator);
                    ps.setString(5, coordinator);
                    // start, code added by pr on 26thmar18
                    String addedBy = "";
                    if (role.equals(Constants.ROLE_SUP)) {
                        addedBy = "Support(" + logged_in_mail + ")";
                    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                        addedBy = "Admin(" + logged_in_mail + ")";
                    }
                    ps.setString(6, addedBy);
                    // end, code added by pr on 26thmar18
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside setEmploymentDAs  " + ps);
                    int num = ps.executeUpdate();
                    if (num > 0) {
                        isSet = true;
                    } else {
                        isSet = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function minstry depatrment " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 69 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 4 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 70 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 5 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 71 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 6 " + e.getMessage());

                    }
                }
            }
        } else if (employment.equalsIgnoreCase("state")) {
            // get department and other_dept and serach in state_department table for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND ( emp_coord_email =  emp_admin_email  ) AND emp_coord_email = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, state);
                ps.setString(3, department);
                ps.setString(4, coordinator);
                rs = ps.executeQuery();
                rs.last();
                if (rs.getRow() > 0) {
                    isSet = false;
                    isError = true;
                    isSuccess = false;
                    msg = "DA-Admin Already Exist!";
                } else {
                    qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_dept = ?, emp_coord_email = ?, emp_admin_email = ? , "
                            + ""
                            + "emp_mail_acc_cat = '" + emp_mail_acc_cat + "', emp_sms_acc_cat = '" + emp_sms_acc_cat + "'  , emp_addedby = ? "; // query modified by pr on 26thmar18 added emp_addedby
                    ps = con.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, state);
                    ps.setString(3, department);
                    ps.setString(4, coordinator);
                    ps.setString(5, coordinator);
                    // start, code added by pr on 26thmar18
                    String addedBy = "";
                    if (role.equals(Constants.ROLE_SUP)) {
                        addedBy = "Support(" + logged_in_mail + ")";
                    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                        addedBy = "Admin(" + logged_in_mail + ")";
                    }
                    ps.setString(6, addedBy);
                    // end, code added by pr on 26thmar18
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside setEmploymentDAs  " + ps);
                    int num = ps.executeUpdate();
                    if (num > 0) {
                        isSet = true;
                    } else {
                        isSet = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function state dept " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                   
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 72 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 7 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 73 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 8 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 74 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 9 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("others") || employment.equalsIgnoreCase("Psu") || employment.equalsIgnoreCase("Const") || employment.equalsIgnoreCase("Nkn")) {
            // get organization and other_dept and serach in organization table for coordinator
            // if organization contains other then take other_dept as department
            if (organization.equalsIgnoreCase("other") && !other_dept.equals("")) {
                organization = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                // no department here
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND ( emp_coord_email =  emp_admin_email  ) AND emp_coord_email = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                ps.setString(3, coordinator);
                rs = ps.executeQuery();
                rs.last();
                if (rs.getRow() > 0) {
                    isSet = false;
                    isError = true;
                    isSuccess = false;
                    msg = "DA-Admin Already Exist!";
                } else {
                    qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_coord_email = ?, emp_admin_email = ? , "
                            + ""
                            + "emp_mail_acc_cat = '" + emp_mail_acc_cat + "', emp_sms_acc_cat = '" + emp_sms_acc_cat + "'  , emp_addedby = ?  ";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, organization);
                    ps.setString(3, coordinator);
                    ps.setString(4, coordinator);
                    // start, code added by pr on 26thmar18
                    String addedBy = "";
                    if (role.equals(Constants.ROLE_SUP)) {
                        addedBy = "Support(" + logged_in_mail + ")";
                    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                        addedBy = "Admin(" + logged_in_mail + ")";
                    }
                    ps.setString(5, addedBy);
                    // end, code added by pr on 26thmar18
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside setEmploymentDAs  " + ps);
                    int num = ps.executeUpdate();
                    if (num > 0) {
                        isSet = true;
                    } else {
                        isSet = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function organization 1 " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                   
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 75 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 10 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 76 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 11 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 77 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "setEmploymentDAs EXCEPTION 12 " + e.getMessage());
                    }
                }
            }
        }
        return isSet;
    }

    // below function added by pr on 6thmar18
    public ArrayList<String> fetchEmploymentDAs(HashMap empDetails) {
        ArrayList<String> empCoords = new ArrayList<String>();
        String employment = "", ministry = "", department = "", organization = "", other_dept = "", state = "";
        String add_state = ""; // line added by pr on 26thmar18
        if (empDetails != null && empDetails.get("employment") != null) {
            employment = empDetails.get("employment").toString();
            if (empDetails.get("ministry") != null) {
                ministry = empDetails.get("ministry").toString();
            }
            if (empDetails.get("department") != null) {
                department = empDetails.get("department").toString();
            }
            if (empDetails.get("organization") != null) {
                organization = empDetails.get("organization").toString();
            }
            if (empDetails.get("state") != null) {
                state = empDetails.get("state").toString();
            }
            if (empDetails.get("other_dept") != null) {
                other_dept = empDetails.get("other_dept").toString();
            }
            if (empDetails.get("add_state") != null) {
                add_state = empDetails.get("add_state").toString();
            }
        }
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        if (employment.equalsIgnoreCase("Central") || employment.equalsIgnoreCase("UT")) {
            // if (employment.equalsIgnoreCase("central")) {
            // get ministry and department and other_dept and serach in ministry and ministry_department tables for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                String qry = "select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + ""
                        + "AND emp_dept = ? AND (emp_type = 'd'  or  emp_type = 'dc') AND emp_status = 'a'";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, ministry);
                ps.setString(3, department);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside central query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                if (!add_state.equalsIgnoreCase("delhi")) // if the state is non delhi then show the coordinators for that state
                {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = 'State' AND emp_min_state_org = ? "
                            + " AND emp_dept = 'other' AND (emp_type = 'd'  or  emp_type = 'dc') AND emp_status = 'a'";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, add_state);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " central getcoords  non delhi query is " + ps);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne fetchEmploymentDAs in ForwardAction class " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 78 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 1 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 79 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 2 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 80 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 3 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("state")) {
            Boolean isOther = false; // line added by pr on 23rdmar18
            // get department and other_dept and serach in state_department table for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
                isOther = true; // line added by pr on 23rdmar18                
            }
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = "select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + ""
                        + "AND emp_dept = ? AND (emp_type = 'd'  or  emp_type = 'dc') AND emp_status = 'a'";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, state);
                ps.setString(3, department);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside state query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                if (isOther) // if added by pr on 23rdmar18
                {
                    qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                            + " AND emp_dept = 'other' AND (emp_type = 'd'  or  emp_type = 'dc') AND emp_status = 'a'";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, state);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                        {
                            empCoords.add(rs.getString("emp_coord_email"));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne fetchEmploymentDAs in ForwardAction class " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 81 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 4 " + e.getMessage());

                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 82 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 5 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //// above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 83 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 6 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("others") || employment.equalsIgnoreCase("Psu") || employment.equalsIgnoreCase("Const") || employment.equalsIgnoreCase("Nkn")) {
            // get organization and other_dept and serach in organization table for coordinator
            // if organization contains other then take other_dept as department
            if (organization.equalsIgnoreCase("other") && !other_dept.equals("")) {
                organization = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = "select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + ""
                        + "AND (emp_type = 'd'  or  emp_type = 'dc') AND emp_status = 'a'";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside other query is " + ps);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (!empCoords.contains(rs.getString("emp_coord_email"))) // if id doesnt exist in th e arraylist then add
                    {
                        empCoords.add(rs.getString("emp_coord_email"));
                    }
                }
                // below lines added by pr on 6thmar18
                ps.close();
                rs.close();
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insidne fetchEmploymentDAs in ForwardAction class " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 84 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 7 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 85 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 8 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 86 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 9 " + e.getMessage());
                    }
                }
            }
        }
        return empCoords;
    }

    public Boolean setEmploymentCoords(HashMap empDetails, String coordinator) {
        String role = fetchSessionRole(); // line added by pr on 26thmar18
        String logged_in_mail = fetchSessionEmail(); // line added by pr on 26thmar18
        Boolean isSet = false;
        String employment = "", ministry = "", department = "", organization = "", other_dept = "", state = "";
        if (empDetails != null && empDetails.get("employment") != null) {
            employment = empDetails.get("employment").toString();
            if (empDetails.get("ministry") != null) {
                ministry = empDetails.get("ministry").toString();
            }
            if (empDetails.get("department") != null) {
                department = empDetails.get("department").toString();
            }
            if (empDetails.get("state") != null) {
                state = empDetails.get("state").toString();
            }
            if (empDetails.get("organization") != null) {
                organization = empDetails.get("organization").toString();
            }
            if (empDetails.get("other_dept") != null) {
                other_dept = empDetails.get("other_dept").toString();
            }
        }
        String emp_mail_acc_cat = "", emp_sms_acc_cat = "";
        String emp_admin_email = sup_email; // the admin will be support in case of add coordinator from the support panel
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        // get acc description from employment_coordinator table based on employment details
        try {
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            // no department here
            String qry = " select emp_mail_acc_cat, emp_sms_acc_cat FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                    + " AND emp_dept = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, employment);
            ps.setString(2, ministry);
            ps.setString(3, department);
            rs = ps.executeQuery();
            while (rs.next()) {
                emp_mail_acc_cat = rs.getString("emp_mail_acc_cat");
                emp_sms_acc_cat = rs.getString("emp_sms_acc_cat");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function constitutional_department " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 87 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 10 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 88 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 11 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 89 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 12 " + e.getMessage());
                }
            }
        }
        if (employment.equalsIgnoreCase("Central") || employment.equalsIgnoreCase("UT")) {
            //if (employment.equalsIgnoreCase("central")) {
            // get ministry and department and other_dept and serach in ministry and ministry_department tables for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // check if this da already exists corresponding to the ministry department
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND ( emp_coord_email !=  emp_admin_email  ) AND emp_coord_email = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, ministry);
                ps.setString(3, department);
                ps.setString(4, coordinator);
                rs = ps.executeQuery();
                rs.last();
                if (rs.getRow() > 0) {
                    isSet = false;
                    isError = true;
                    isSuccess = false;
                    msg = "Coordinator Already Exist!";
                } else {
                    qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_dept = ?, "
                            + "emp_coord_email = ?, emp_admin_email = ? , emp_mail_acc_cat = '" + emp_mail_acc_cat + "', emp_sms_acc_cat = '" + emp_sms_acc_cat + "' "
                            + " , emp_addedby = ?  "; // query modified by pr on 26thmar18 added emp_addedby
                    ps = con.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, ministry);
                    ps.setString(3, department);
                    ps.setString(4, coordinator);
                    ps.setString(5, emp_admin_email);
                    // start, code added by pr on 26thmar18
                    String addedBy = "";
                    if (role.equals(Constants.ROLE_SUP)) {
                        addedBy = "Support(" + logged_in_mail + ")";
                    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                        addedBy = "Admin(" + logged_in_mail + ")";
                    }
                    ps.setString(6, addedBy);
                    // end, code added by pr on 26thmar18
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside setEmploymentCoords  " + ps);
                    int num = ps.executeUpdate();
                    if (num > 0) {
                        isSet = true;
                    } else {
                        isSet = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function minstry depatrment " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 90 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 13 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 91 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 14 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 92 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 15 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("state")) {
            // get department and other_dept and serach in state_department table for coordinator
            // if department contains other then take other_dept as department
            if (department.equalsIgnoreCase("other") && !other_dept.equals("")) {
                department = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND emp_dept = ? AND ( emp_coord_email !=  emp_admin_email  ) AND emp_coord_email = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, state);
                ps.setString(3, department);
                ps.setString(4, coordinator);
                rs = ps.executeQuery();
                rs.last();
                if (rs.getRow() > 0) {
                    isSet = false;
                    isError = true;
                    isSuccess = false;
                    msg = "Coordinator Already Exist!";
                } else {
                    qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_dept = ?, emp_coord_email = ?, emp_admin_email = ? , "
                            + "emp_mail_acc_cat = '" + emp_mail_acc_cat + "', emp_sms_acc_cat = '" + emp_sms_acc_cat + "'   , emp_addedby = ?  "; // modified by pr on 26thmar18
                    ps = con.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, state);
                    ps.setString(3, department);
                    ps.setString(4, coordinator);
                    ps.setString(5, emp_admin_email);
                    // start, code added by pr on 26thmar18
                    String addedBy = "";
                    if (role.equals(Constants.ROLE_SUP)) {
                        addedBy = "Support(" + logged_in_mail + ")";
                    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                        addedBy = "Admin(" + logged_in_mail + ")";
                    }
                    ps.setString(6, addedBy);
                    // end, code added by pr on 26thmar18
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside setEmploymentCoords  " + ps);
                    int num = ps.executeUpdate();
                    if (num > 0) {
                        isSet = true;
                    } else {
                        isSet = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function state dept " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 93 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 16 " + e.getMessage());

                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 94 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 17 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 95 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 18 " + e.getMessage());
                    }
                }
            }
        } else if (employment.equalsIgnoreCase("others") || employment.equalsIgnoreCase("Psu") || employment.equalsIgnoreCase("Const") || employment.equalsIgnoreCase("Nkn")) {
            // get organization and other_dept and serach in organization table for coordinator
            // if organization contains other then take other_dept as department
            if (organization.equalsIgnoreCase("other") && !other_dept.equals("")) {
                organization = other_dept;
            }
            try {
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // no department here
                String qry = " select emp_coord_email, emp_admin_email FROM employment_coordinator WHERE emp_category = ? AND emp_min_state_org = ? "
                        + " AND ( emp_coord_email !=  emp_admin_email  ) AND emp_coord_email = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, employment);
                ps.setString(2, organization);
                ps.setString(3, coordinator);
                rs = ps.executeQuery();
                rs.last();
                if (rs.getRow() > 0) {
                    isSet = false;
                    isError = true;
                    isSuccess = false;
                    msg = "Coordinator Already Exist!";
                } else {
                    qry = "INSERT INTO employment_coordinator SET emp_category = ?,  emp_min_state_org = ?, emp_coord_email = ?, emp_admin_email = ? , "
                            + "emp_mail_acc_cat = '" + emp_mail_acc_cat + "', emp_sms_acc_cat = '" + emp_sms_acc_cat + "' , emp_addedby = ?  "; // modified by pr on 26thmar18
                    ps = con.prepareStatement(qry);
                    ps.setString(1, employment);
                    ps.setString(2, organization);
                    ps.setString(3, coordinator);
                    ps.setString(4, emp_admin_email);
                    // start, code added by pr on 26thmar18
                    String addedBy = "";
                    if (role.equals(Constants.ROLE_SUP)) {
                        addedBy = "Support(" + logged_in_mail + ")";
                    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                        addedBy = "Admin(" + logged_in_mail + ")";
                    }
                    ps.setString(5, addedBy);
                    // end, code added by pr on 26thmar18
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside setEmploymentCoords  " + ps);
                    int num = ps.executeUpdate();
                    if (num > 0) {
                        isSet = true;
                    } else {
                        isSet = false;
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function organization 2 " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 96 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 19 " + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 97 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 20 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 98 " + e.getMessage());

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchEmploymentDAs EXCEPTION 21 " + e.getMessage());
                    }
                }
            }
        }
        checkAndUpdateCoordinator(coordinator);// line added by pr on 20thdec17
        return isSet;
    }

    // below function added by pr on 20thdec17
    public void checkAndUpdateCoordinator(String email) {
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select  * FROM coordinator_admin WHERE user_name = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, email);
            rs = ps.executeQuery();
            rs.last();
            if (rs.getRow() == 0) {
                qry = "INSERT INTO coordinator_admin SET user_name = ? ";
                ps = con.prepareStatement(qry);
                ps.setString(1, email);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkAndUpdateCoordinator  " + ps);
                int num = ps.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function coord admin " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 99 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "checkAndUpdateCoordinator EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 100 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "checkAndUpdateCoordinator EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 101 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "checkAndUpdateCoordinator EXCEPTION 3 " + e.getMessage());
                }
            }
        }
    }

    // below function added by pr on 13thmar18
    public String fetchLDAPEmails(String email) {
        String emails = email;
        //ServletContext sctx = getServletContext();
        /*String host = sctx.getInitParameter("host");
         String port = sctx.getInitParameter("port");
         String dn = sctx.getInitParameter("binddn");
         String dn_pass = sctx.getInitParameter("bindpass");*/
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null; // line added by pr on 16thoct18
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress", "cn", "mobile"};
            String[] IDs = {"mailequivalentaddress", "mail"};
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
            NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " search string value is " + t);
                //String uids = t.substring(t.indexOf("uid:") + 5, t.length() - 1);
                t = t.substring(t.indexOf("{") + 1, t.indexOf("}"));
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside curly brackets value is " + t);
                String mailStr = t.substring(t.indexOf("mail=mail:"), t.indexOf("mailequivalentaddress=mailequivalentaddress:") - 2);
                mailStr = mailStr.replace("mail=mail: ", "");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " mail value is " + mailStr);
                String mailEqvStr = t.substring(t.indexOf("mailequivalentaddress=mailequivalentaddress:"));
                mailEqvStr = mailEqvStr.replace("mailequivalentaddress=mailequivalentaddress: ", "");
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " mailEqvStr value is " + mailEqvStr);
                emails = mailStr.trim() + "," + mailEqvStr.trim();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 102 " + e.getMessage());
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchLDAPEmails EXCEPTION " + e.getMessage());
//                }
//            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchLDAPEmails func value of emails is " + emails);
        return emails;
    }

    // below function added by pr on 29thjan19
    public boolean checkCoordAdminExist(String type, String email) {
        boolean isExist = false;
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "";
            if (type.equalsIgnoreCase("m"))// check in mailadmin_forms table
            {
                qry = "select m_id FROM mailadmin_forms WHERE  m_email =  ?  limit 1";
            } else if (type.equalsIgnoreCase("c")) {
                qry = "select emp_id FROM employment_coordinator WHERE  emp_coord_email =  ? and (emp_type = 'c' or emp_type = 'dc') limit 1 "; //else if added by sahil 7july2021
            } else if (type.equalsIgnoreCase("da")) {
                qry = "select emp_id FROM employment_coordinator WHERE  emp_coord_email =  ? and (emp_type = 'd' or emp_type = 'dc') limit 1 "; //else if added by sahil 7july2021

            } else // check in employment_coordinator table emp_coord_email field
            {
                //qry = "select emp_id FROM employment_coordinator WHERE  emp_coord_email =  ? limit 1 ";
                isExist = false;  // line added by sahil 7july2021
                return isExist;
            }
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, email);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkCoordAdminExist function query is " + ps);

            rs = ps.executeQuery();
            while (rs.next()) {
                isExist = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function check dns coord level " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 103 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "checkCoordAdminExist EXCEPTION" + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 104 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "checkCoordAdminExist EXCEPTION 1 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 105 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "checkCoordAdminExist EXCEPTION 2 " + e.getMessage());
                }
            }
        }
        return isExist;
    }

    // below function added by pr on 27thnov19
    public String checkPreviousReject() {

        prevrejectdata = null;

        //System.out.println(" inside checkPreviousReject method value of regNo is "+regNo);
        String[] arr = null;

        String email = "", mobile = "", form_name = "";

        String regNoRegExNew = "^[a-zA-Z0-9~_\\-\\+@.]*$";

        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegExNew)) // line modified by pr on 5thfeb18 for xss
        {
            // System.out.println(" inside regNo not null ");

            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)

            if (arr != null && arr[0] != null) {
                email = arr[0];
            }

            if (arr != null && arr[1] != null) {
                mobile = arr[1];
            }

            if (arr != null && arr[2] != null) {
                form_name = arr[2];
            }

            //System.out.println(" mobile "+mobile+" mobile "+mobile+" form_name "+form_name);
            // get the previous data related to this applicant mobile and mobile from final_audit_track
            prevrejectdata = fetchPreviousReject(email, mobile, form_name);

            // System.out.println(" inside checkPreviousReject function size of PrevRejectData is "+prevrejectdata.size());
            for (Forms obj : prevrejectdata) {
//                frmObj.set_stat_reg_no(registration_no);
//                
//                frmObj.set_stat_type(status);
//                
//                frmObj.setEmail(to_email);
//                
//                frmObj.setCreatedon(to_datetime);
//                
//                frmObj.setReject_remarks(remarks);            

                // System.out.println(" set_stat_reg_no "+obj.get_stat_reg_no()+" .setCreatedon(to_datetime) "+obj.getCreatedon());
            }

        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside checkPreviousReject function prevrejected value is " + prevrejectdata);

        return SUCCESS;
    }

    // below function added by pr on 27thnov19
    public ArrayList<Forms> fetchPreviousReject(String email, String mobile, String form_name) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        String to_email = "", status = "", to_datetime = "", registration_no = "", remarks = "";

        ArrayList<Forms> arr = new ArrayList<Forms>();

        //System.out.println(" inside fetchPreviousReject function mobile "+mobile+" mobile "+mobile+" form_name "+form_name);
        try {
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            qry = "SELECT to_email, status, to_datetime, registration_no, ca_remarks, us_remarks, coordinator_remarks, support_remarks, da_remarks, admin_remarks "
                    + "FROM final_audit_track WHERE  form_name = ? AND applicant_email = ? AND applicant_mobile = ? AND status like '%_rejected' ORDER BY to_datetime DESC ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, form_name);
            ps.setString(2, email);
            ps.setString(3, mobile);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " fetchPreviousReject ps : " + ps);
            rs = ps.executeQuery();

            //HashMap<String, String> hm = null;
            Forms frmObj = null;

            while (rs.next()) {

                to_email = "";
                status = "";
                to_datetime = "";
                registration_no = "";
                remarks = "";

                //hm = new HashMap<String, String>();
                frmObj = new Forms();

                if (rs.getString("to_email") != null && !rs.getString("to_email").equals("")) {
                    to_email = rs.getString("to_email").trim();
                }
                if (rs.getString("status") != null && !rs.getString("status").equals("")) {
                    status = rs.getString("status").trim();
                }
                if (rs.getString("to_datetime") != null && !rs.getString("to_datetime").equals("")) {
                    to_datetime = rs.getString("to_datetime").trim();
                }
                if (rs.getString("registration_no") != null && !rs.getString("registration_no").equals("")) {
                    registration_no = rs.getString("registration_no").trim();
                }
                if (status.toLowerCase().contains("ca")) // ca_rejected
                {
                    remarks = rs.getString("ca_remarks") != null ? rs.getString("ca_remarks").trim() : "";
                } else if (status.toLowerCase().contains("support")) // support_rejected
                {
                    remarks = rs.getString("support_remarks") != null ? rs.getString("support_remarks").trim() : "";
                } else if (status.toLowerCase().contains("us"))// us_rejected
                {
                    remarks = rs.getString("us_remarks") != null ? rs.getString("us_remarks").trim() : "";
                } else if (status.toLowerCase().contains("coordinator"))// coordinator_rejected
                {
                    remarks = rs.getString("coordinator_remarks") != null ? rs.getString("coordinator_remarks").trim() : "";
                } else if (status.toLowerCase().contains("mail-admin"))// mail-admin_rejected
                {
                    remarks = rs.getString("admin_remarks") != null ? rs.getString("admin_remarks").trim() : "";
                } else if (status.toLowerCase().contains("da"))// da_rejected
                {
                    remarks = rs.getString("da_remarks") != null ? rs.getString("da_remarks").trim() : "";
                }
//                
//                hm.put("registration_no", registration_no);
//                hm.put("status", status);
//                hm.put("to_email", to_email);
//                hm.put("to_datetime", to_datetime);
//                hm.put("remarks", remarks);

                frmObj.set_stat_reg_no(registration_no);

                frmObj.set_stat_type(status);

                frmObj.setEmail(to_email);

                frmObj.setCreatedon(to_datetime);

                frmObj.setReject_remarks(remarks);

                arr.add(frmObj);
            }

        } catch (Exception e) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " insinde fetchPreviousReject " + e.getMessage());

            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchPreviousReject EXCEPTION 26 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchPreviousReject EXCEPTION 27 " + e.getMessage());
                }
            }
        }

        return arr;

    }

    // below function modified by pr on 25thjan18
    public String fwdToCoordMadmin() {
        String[] arr = null;
        String reg_no = "", formName = "";
        String role = "";
        msg = "";
        Boolean isImapMobile = false;
        role = fetchSessionRole();
        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegEx)) // line modified by pr on 5thfeb18 for xss
        {
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        } else {
            regNo = ""; // line added by pr on 6thfeb18
        }
        // in this case action type will be forward only
        if (arr != null && arr.length == 3) {

            reg_no = arr[0];
            formName = arr[1];
            this.formName = arr[1];

            //12-apr-2022 syndata for both create id and mark as done
//            NewCAHome newCAHome = new NewCAHome();
//            newCAHome.syncData(arr[0]); // as per line at 8820
            //12-apr-2022 syndata for both create id and mark as done
            if (statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx))) // line modified by pr on 19thfeb18
            {
                if (role.equals(Constants.ROLE_SUP)) {

                    System.out.println(" inside fwdtocoordmadmin role as support ");

                    if (!fwd_coord.isEmpty()) {
                        if (choose_da_type.equals("da") || choose_da_type.equals("c") || choose_da_type.equals("m")) // m condition added by pr on 20thapr18
                        {
                            System.out.println(" inside fwdtocoordmadmin role as support 1 ");

                            Boolean coordErr = false;
                            String[] coords;
                            if (fwd_coord.contains(",")) {
                                coords = fwd_coord.split(",");
                            } else {
                                coords = new String[1];
                                coords[0] = fwd_coord;
                            }
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fwd_coord " + fwd_coord);// line modified by pr on 31stjan2020
                            for (String m : coords) {
                                if (!(m.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$"))) {
                                    coordErr = true;
                                }
                            }
                            if (!coordErr) {
                                boolean allowFwd = false;
                                if (choose_da_type.equals("da") || choose_da_type.equals("c") || choose_da_type.equals("m")) // check in the employment_coordinator table
                                {
                                    if (formName.toLowerCase().contains("vpn") || formName.toLowerCase().contains("change_add")) {// line modified by pr on 20thmar2020
                                        allowFwd = true;
                                    } else {
                                        allowFwd = checkCoordAdminExist(choose_da_type, fwd_coord);
                                    }
                                }
                                if (allowFwd) // if around and else added by pr on 29thjan19
                                {
                                    if (choose_da_type.equals("da")) {
                                        String stat_type_da = "da_pending";
                                        String caption = "DA-Admin";
                                        if (formName.toLowerCase().contains("vpn")) {
                                            stat_type_da = "mail-admin_pending";
                                            caption = "VPN Admin";
                                            fwd_coord = "vpnsupport@nic.in";
                                        } else if (fwd_coord.equals("support@nic.in") || fwd_coord.equals("support@gov.in") || fwd_coord.equals("support@dummy.nic.in")) {
                                            stat_type_da = "mail-admin_pending";
                                            fwd_coord = sup_email;
                                            caption = "Admin";
                                        }

                                        Boolean res = updateStatus(reg_no, stat_type_da, formName, statRemarks, fwd_coord, "", ""); //statRemarks added by pr on 5thjun18, last param added by pr on 7thjan19
                                        if (res) {
                                            // below if added by pr on 3rdsep19
                                            //if (formName.toLowerCase().contains("vpn") && stat_type_da.equals("mail-admin_pending")) {// line modified by pr on 20thmar2020
                                            if ((formName.toLowerCase().contains("vpn") || formName.toLowerCase().contains("change_add")) && stat_type_da.equals("mail-admin_pending")) {

                                                System.out.println(" vpnpushapi called 1 ");

                                                VpnPushApi vpnapi = new VpnPushApi();
                                                vpnapi.callVpnWebService(arr[0]);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and  Forwarded to the " + caption + " " + fwd_coord;
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            if (msg.equals("")) {
                                                msg = "Application (" + reg_no + ") could not be Approved";
                                            }
                                        }
                                    } else if (choose_da_type.equals("c")) {
                                        if (formName.toLowerCase().contains("vpn") || formName.toLowerCase().contains("change_add")) {// line modified by pr on 20thmar2020
                                            for (String coord : coords) {
                                                db.insertIntoEmpCoordForVPN(coord);
                                            }
                                        }
                                        Boolean res = updateStatus(reg_no, "coordinator_pending", formName, statRemarks, fwd_coord, "", ""); //statRemarks added by pr on 5thjun18
                                        if (res) {
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and  Forwarded to the Coordinator " + fwd_coord;
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            if (msg.equals("")) {
                                                msg = "Application (" + reg_no + ") could not be Approved";
                                            }
                                        }
                                    } else if (choose_da_type.equals("m")) // else if added by pr on 20thapr18
                                    {
                                        Boolean res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, fwd_coord, "", ""); //statRemarks added by pr on 5thjun18
                                        if (res) {
                                            // below if added by pr on 3rdsep19
                                            if (formName.toLowerCase().contains("vpn") || formName.toLowerCase().contains("change_add")) {// line modified by pr on 20thmar2020

                                                System.out.println(" vpnpushapi called 2 ");

                                                VpnPushApi vpnapi = new VpnPushApi();
                                                vpnapi.callVpnWebService(arr[0]);
                                            }
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Application (" + reg_no + ") Approved and  Forwarded to the Admin " + fwd_coord;
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            if (msg.equals("")) {
                                                msg = "Application (" + reg_no + ") could not be Approved";
                                            }
                                        }
                                    }
                                } else {
                                    fwd_coord = "";
                                    isSuccess = false;
                                    isError = true;
                                    msg = "The email entered doesnt exist in our system.So request could not be forwarded.";
                                }
                            } else {
                                fwd_coord = "";// line added by pr on 7thfeb18 for xss
                                isSuccess = false;
                                isError = true;
                                msg = "Please select/enter a Valid Coordinator to Forward Request to.";
                            }
                        } else {
                            fwd_coord = "";// line added by pr on 7thfeb18 for xss
                            isSuccess = false;
                            isError = true;
                            msg = "Please choose a DA/Coordinator/Admin."; // msg modified by pr on 20thapr18
                        }
                    } else {
                        isSuccess = false;
                        isError = true;
                        if (msg.isEmpty()) {
                            msg = "Please select/enter a Coordinator to Forward Request to.";
                        }
                    }
                } else if (role.equals(Constants.ROLE_CO)) {
                    Boolean res = false;
                    Boolean madminErr = false;
                    System.out.println(" inside fwdtocoordmadmin role as Coordinator  ");
                    if (formName.equals(Constants.DNS_FORM_KEYWORD)) // if its a dns form
                    {
                        res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, DNS_MAILADMIN, "", ""); // above line commented below added by pr on 29thjan18  //statRemarks added by pr on 5thjun18  
                        if (res) {
                            isSuccess = true;
                            isError = false;
                            msg = "Application (" + reg_no + ") Approved and Forwarded to the Admin " + DNS_MAILADMIN;
                        } else {
                            isSuccess = false;
                            isError = true;
                            if (msg.equals("")) {
                                msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                            }
                        }
                    } else if (formName.equals(Constants.SMS_FORM_KEYWORD)) // if its a sms form forward to the smssupport mailadmin only, added by pr on 23rdmar18
                    {
                        String smssup = "smssupport@gov.in";
                        res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, smssup, "", ""); // above line commented below added by pr on 29thjan18 //statRemarks added by pr on 5thjun18   
                        if (res) {
                            isSuccess = true;
                            isError = false;
                            msg = "Application (" + reg_no + ") Approved and Forwarded to the Admin " + smssup;
                        } else {
                            isSuccess = false;
                            isError = true;
                            if (msg.equals("")) {
                                msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                            }
                        }
                    } else if (formName.equals(Constants.DAONBOARDING_FORM_KEYWORD)) // if its a sms form forward to the smssupport mailadmin only, added by pr on 23rdmar18
                    {
                        String smssup = "rajesh.singh@nic.in";
                        res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, smssup, "", ""); // above line commented below added by pr on 29thjan18 //statRemarks added by pr on 5thjun18   
                        if (res) {
                            isSuccess = true;
                            isError = false;
                            msg = "Application (" + reg_no + ") Approved and Forwarded to the Admin " + smssup;
                        } else {
                            isSuccess = false;
                            isError = true;
                            if (msg.equals("")) {
                                msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                            }
                        }
                    } else if (formName.equals(Constants.VPN_ADD_FORM_KEYWORD) || formName.equals(Constants.VPN_BULK_FORM_KEYWORD) || formName.equals(Constants.VPN_RENEW_FORM_KEYWORD) || formName.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || formName.equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || formName.equals(Constants.VPN_DELETE_FORM_KEYWORD)) // if its a sms form forward to the smssupport mailadmin only, added by pr on 23rdmar18
                    {
                        String smssup = "vpnsupport@nic.in";
                        res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, smssup, "", ""); // above line commented below added by pr on 29thjan18 //statRemarks added by pr on 5thjun18   
                        if (res) {
                            // below lines added by pr on 3rdsep19

                            System.out.println(" vpnpushapi called 3 ");

                            VpnPushApi vpnapi = new VpnPushApi();
                            vpnapi.callVpnWebService(arr[0]);
                            isSuccess = true;
                            isError = false;
                            msg = "Application (" + reg_no + ") Approved and Forwarded to the Admin " + smssup;
                        } else {
                            isSuccess = false;
                            isError = true;
                            if (msg.equals("")) {
                                msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                            }
                        }
                    } else {
                        Boolean daForm = false;
                        if ((arr[1].equals(Constants.BULK_FORM_KEYWORD) || arr[1].equals(Constants.NKN_SINGLE_FORM_KEYWORD)
                                || arr[1].equals(Constants.NKN_BULK_FORM_KEYWORD) || arr[1].equals(Constants.SINGLE_FORM_KEYWORD))) {

                            fwd_madmin = "support@gov.in"; // line added by pr on 5thmar2020, so that if no admin is found then assign the default for the above forms                            
                            reg_no = arr[0];
                            formName = arr[1];
                            daForm = true;
                            HashMap hm = fetchEmpCoordsAtCA(arr[0], arr[1]); // to get mailadmin addresses
                            HashMap empDetails = fetchEmploymentDetails(arr[1], arr[0]);
                            String employment = empDetails.get("employment").toString();
                            String state = empDetails.get("state").toString();
                            String department = empDetails.get("department").toString();
                            String ministry = empDetails.get("ministry").toString();
                            String organization = empDetails.get("organization").toString();

                            if (employment.equalsIgnoreCase("state")) {
                                ministry = state;
                            } else if (employment.equalsIgnoreCase("central") || employment.equalsIgnoreCase("ut")) {
                                ministry = ministry;
                            } else {
                                ministry = organization;
                            }

                            // in case of delhigov.in 
                            String domainFromEmpManage = fetchDomain(employment, ministry, department); // added on 07june2022

                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " size of hm is " + hm.size());
                            String preferred_email1 = "";
                            String preferred_email2 = "";

                            if ((arr[1].equals(Constants.SINGLE_FORM_KEYWORD) || arr[1].equals(Constants.NKN_SINGLE_FORM_KEYWORD))) {
                                preferred_email1 = empDetails.get("preferred_email1").toString();
                                preferred_email2 = empDetails.get("preferred_email2").toString();
                            } else if ((arr[1].equals(Constants.BULK_FORM_KEYWORD) || arr[1].equals(Constants.NKN_BULK_FORM_KEYWORD))) {
                                preferred_email1 = empDetails.get("preferred_email1").toString();
                                preferred_email2 = empDetails.get("preferred_email1").toString();
                            }

                            Set<String> bo = db.fetchBoFromEmpCoord(empDetails);
                            System.out.println("BO :: " + bo);
                            Set<String> domains = new HashSet<>();
                            for (String boId : bo) {
                                System.out.println("Bo id ::: " + boId);
                                Set<String> domain = ldap.fetchAllowedDomains(boId);
                                if (domain.size() > 0)// line added on 12thmar2020
                                {
                                    domains.addAll(domain);
                                }
                            }

                            System.out.println("DOMAINS ::::: " + domains);

                            if (hm != null && hm.size() > 0) {
                                String daEmail = "", coEmail = "", adminEmail = "", stat_type = "", toWhichAdmin = "";

                                if (hm.get("da") != null && !hm.get("da").toString().isEmpty()) {
                                    daEmail = (String) hm.get("da");
                                }

                                if (hm.get("madmin") != null && !hm.get("madmin").toString().isEmpty()) {
                                    adminEmail = (String) hm.get("madmin");
                                }

                                if (!daEmail.isEmpty()) {
                                    fwd_madmin = daEmail;
                                    if (daEmail.equalsIgnoreCase("kaushal.shailender@nic.in")) {
                                        //**********************starts09nov2020***************************
//                                        if (empDetails.get("emp_type").toString().toLowerCase().equals("emp_contract") || empDetails.get("emp_type").toString().toLowerCase().equals("consultant")) {
//                                            stat_type = "mail-admin_pending";
//                                            toWhichAdmin = "Admin";
//                                            fwd_madmin = "support@gov.in";
//                                        } else 
                                        //**********************ends09nov2020***************************
                                        if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                            String[] emails = preferred_email1.split(",");
                                            boolean domainAllowed = true;
                                            if (domains.size() > 0) {
                                                for (String email : emails) {
                                                    String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                    String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                    if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                        domainAllowed = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (domainAllowed) {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                            } else {
                                                stat_type = "mail-admin_pending";
                                                toWhichAdmin = "Admin";
                                                fwd_madmin = "support@gov.in";
                                            }
                                        } else {
                                            String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                            String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                            if (domains.size() > 0) {
                                                if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                    stat_type = "da_pending";
                                                    toWhichAdmin = "Delegated Admin";
                                                } else {
                                                    stat_type = "mail-admin_pending";
                                                    toWhichAdmin = "Admin";
                                                    fwd_madmin = "support@gov.in";
                                                }
                                            } else {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                            }
                                        }
                                    } else if ((empDetails.get("employment").toString().equalsIgnoreCase("State") && empDetails.get("state").toString().equalsIgnoreCase("Punjab")) || domainFromEmpManage.equalsIgnoreCase("delhi.gov.in")) {
                                        if (preferred_email1.contains(",") || preferred_email2.contains(",")) {
                                            String[] emails = preferred_email1.split(",");
                                            boolean domainAllowed = true;
                                            if (domains.size() > 0) {
                                                for (String email : emails) {
                                                    String pref1_domain = email.split("@")[1].trim().toLowerCase();
                                                    String pref2_domain = email.split("@")[1].trim().toLowerCase();

                                                    if (!domains.contains(pref1_domain) || !domains.contains(pref2_domain)) {
                                                        domainAllowed = false;
                                                        break;
                                                    }
                                                }
                                            }
                                            if (domainAllowed) {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                            } else {
                                                stat_type = "mail-admin_pending";
                                                toWhichAdmin = "Admin";
                                                fwd_madmin = "support@gov.in";
                                            }
                                        } else {
                                            String pref1_domain = preferred_email1.split("@")[1].trim().toLowerCase();
                                            String pref2_domain = preferred_email2.split("@")[1].trim().toLowerCase();
                                            if (domains.size() > 0) {
                                                if (domains.contains(pref1_domain) && domains.contains(pref2_domain)) {
                                                    stat_type = "da_pending";
                                                    toWhichAdmin = "Delegated Admin";
                                                } else {
                                                    stat_type = "mail-admin_pending";
                                                    toWhichAdmin = "Admin";
                                                    fwd_madmin = "support@gov.in";
                                                }
                                            } else {
                                                stat_type = "da_pending";
                                                toWhichAdmin = "Delegated Admin";
                                            }
                                        }
                                    } else {
                                        stat_type = "mail-admin_pending";
                                        toWhichAdmin = "Admin";
                                        fwd_madmin = "support@gov.in";
                                    }
                                    //System.out.println("$$$$$$$$$$$$$$$$$$$$$$stat_type:" + stat_type + " | toWhichAdmin:" + toWhichAdmin);
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside madmin not null value is " + fwd_madmin);
                                    res = updateStatus(arr[0], stat_type, formName, statRemarks, fwd_madmin, "", ""); // above line commented below added by pr on 29thjan18  //statRemarks added by pr on 5thjun18  
                                    this.isDa = true;
                                    // if added by pr on 19thfeb18
                                    if (res) {
                                        isSuccess = true;
                                        isError = false;

                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + fwd_madmin + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        if (msg.equals("")) {
                                            msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                                        }
                                    }
                                } else if (!adminEmail.isEmpty()) {
                                    //fwd_madmin = hm.get("madmin").toString();
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside madmin not null value is " + fwd_madmin);
                                    // insert the values in the mailadmin_forms table
                                    /*if (!fwd_madmin.contains(",")) {
                                                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fwd madmin doesnt contain comma ");
                                                checkAndUpdateAdmin(fwd_madmin, arr[1]);
                                            } else {
                                                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fwd madmin DOES contain comma ");
                                                String[] arrAdmin = fwd_madmin.split(",");
                                                for (String s : arrAdmin) {
                                                    checkAndUpdateAdmin(s, arr[1]);
                                                }
                                            }*/ // if else commented by pr on 2ndapr19 
                                    //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fwd to mailadmin fwd_madmin value is " + fwd_madmin);
                                    stat_type = "mail-admin_pending";
                                    toWhichAdmin = "Admin";
                                    fwd_madmin = "support@gov.in";
                                    res = updateStatus(reg_no, stat_type, formName, statRemarks, fwd_madmin, "", ""); // above line commented below added by pr on 29thjan18  //statRemarks added by pr on 5thjun18  
                                    this.isDa = true;
                                    // if added by pr on 19thfeb18
                                    if (res) {
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + fwd_madmin + " ) !";
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        if (msg.equals("")) {
                                            msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                                        }
                                    }
                                } else {
                                    fwd_madmin = "support@gov.in";
                                    this.isDa = false;
                                }
                            } else // if not found any row related to this reqg no 
                            {
                                daForm = false;
                            }
                        } else if (arr[1].equals(Constants.MOB_FORM_KEYWORD) || arr[1].equals(Constants.IMAP_FORM_KEYWORD)) {

                            daForm = true;
                            HashMap hm = fetchEmpCoordsAtCA(arr[0], arr[1]); // to get mailadmin addresses
                            //HashMap empDetails = fetchEmploymentDetails(formName, reg_no);
                            HashMap empDetails = fetchEmploymentDetails(arr[1], arr[0]);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " size of hm is " + hm.size() + "  empDetails  " + empDetails);
                            String stat_type = "", toWhichAdmin = "";

                            //******************starts07oct2020*******fwdToCoordMadmin*****************************                
                            isImapMobile = true;
                            //if (isImapMobile) {
                            Set<String> emp_coord_bos = db.fetchBoFromEmpCoord(empDetails);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " emp_coord_bos " + emp_coord_bos);

                            Set<String> ldap_bos = ldap.fetchBos(empDetails.get("auth_email").toString());

                            /* code addded by MI on 8june2021*/
                            Set<String> domains = new HashSet<>();
                            for (String boId : emp_coord_bos) {
                                System.out.println("Bo id ::: " + boId);
                                Set<String> domain = ldap.fetchAllowedDomains(boId);
                                if (domain.size() > 0)// line added on 12thmar2020
                                {
                                    domains.addAll(domain);
                                }
                            }
                            //String emp_domain = empDetails.get("auth_email").toString().split("@")[1].trim().toLowerCase();
                            /* code ends here, addded by MI on 8june2021*/

                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ldap_bos " + ldap_bos + "  auth_email  " + empDetails.get("auth_email").toString());
                            boolean matchBos = CollectionUtils.containsAny(emp_coord_bos, ldap_bos);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " matchBos " + matchBos);
                            if (hm != null && hm.size() > 0) {
                                //for (Object k : hm.keySet()) {
                                // if (matchBos && domains.contains(emp_domain)) {
                                if (matchBos) {
                                    if (hm.containsKey("da")) {
                                        stat_type = "da_pending";
                                        toWhichAdmin = "Delegated Admin";
                                        fwd_madmin = hm.get("da").toString();
                                    } else {
                                        stat_type = "mail-admin_pending";
                                        toWhichAdmin = "Admin";
                                        fwd_madmin = "support@gov.in";
                                    }
                                } else {
                                    stat_type = "mail-admin_pending";
                                    toWhichAdmin = "Admin";
                                    if (hm.containsKey("madmin") && hm.get("madmin") != null && !hm.get("madmin").toString().isEmpty()) {
                                        fwd_madmin = hm.get("madmin").toString();
                                    } else {
                                        fwd_madmin = "support@gov.in";
                                    }
                                }
                                //}
                            } else {
                                stat_type = "mail-admin_pending";
                                toWhichAdmin = "Admin";
                                fwd_madmin = "support@gov.in";
                            }
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  stat_type :::: " + stat_type + "   fwd_madmin  " + fwd_madmin);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside madmin not null value is " + fwd_madmin);
                            res = updateStatus(reg_no, stat_type, formName, statRemarks, fwd_madmin, "", ""); // above line commented below added by pr on 29thjan18  //statRemarks added by pr on 5thjun18  
                            System.out.println("*******************res :::::::::::::::::: " + res);
                            this.isDa = true;
                            if (res) {
                                isSuccess = true;
                                isError = false;
                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + fwd_madmin + " ) !";
                            } else {
                                isSuccess = false;
                                isError = true;
                                if (msg.equals("")) {
                                    msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
                                }
                            }
                            //}
//                            //********************End07oct2020***************************************      
//                            if (!isImapMobile) {
//                                if (hm != null && hm.size() > 0) {
//                                    for (Object k : hm.keySet()) // loop will iterate only once
//                                    {
//                                        String key = k.toString(); // type whether da or co
//                                        String val = hm.get(k).toString(); // emails comma separated
//                                        if (key.equals("da") && hm.get("da") != null && !val.isEmpty()) {
//                                            if (val.equalsIgnoreCase("kaushal.shailender@nic.in")) {
//                                                stat_type = "da_pending";
//                                                toWhichAdmin = "Delegated Admin";
//                                                fwd_madmin = hm.get("da").toString();
//                                            } else if (empDetails.get("employment").toString().equalsIgnoreCase("State") && empDetails.get("state").toString().equalsIgnoreCase("Punjab")) {
//                                                stat_type = "da_pending";
//                                                toWhichAdmin = "Delegated Admin";
//                                                fwd_madmin = hm.get("da").toString();
//                                            } else {
//                                                stat_type = "mail-admin_pending";
//                                                toWhichAdmin = "Admin";
//                                                fwd_madmin = "support@gov.in";
//                                            }
//                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside madmin not null value is " + fwd_madmin);
//                                            res = updateStatus(reg_no, stat_type, formName, statRemarks, fwd_madmin, "", ""); // above line commented below added by pr on 29thjan18  //statRemarks added by pr on 5thjun18  
//                                            this.isDa = true;
//                                            // if added by pr on 19thfeb18
//                                            if (res) {
//                                                isSuccess = true;
//                                                isError = false;
//
//                                                msg = "Application (" + reg_no + ") Approved and Forwarded Successfully to the " + toWhichAdmin + " ( " + fwd_madmin + " ) !";
//                                            } else {
//                                                isSuccess = false;
//                                                isError = true;
//                                                if (msg.equals("")) {
//                                                    msg = "Application (" + reg_no + ") could not be Approved and could not be Forwarded to Admin";
//                                                }
//                                            }
//                                            break;
//                                        } else if (key.equals("madmin") && hm.get("madmin") != null && !val.isEmpty()) {
//                                            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside madmin not null ");
//                                            fwd_madmin = hm.get("madmin").toString();
//                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside madmin not null value is " + fwd_madmin);
//                                            // insert the values in the mailadmin_forms table
//                                            /*if (!fwd_madmin.contains(",")) {
//                                                // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fwd madmin doesnt contain comma ");
//                                                checkAndUpdateAdmin(fwd_madmin, arr[1]);
//                                            } else {
//                                                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fwd madmin DOES contain comma ");
//                                                String[] arrAdmin = fwd_madmin.split(",");
//                                                for (String s : arrAdmin) {
//                                                    checkAndUpdateAdmin(s, arr[1]);
//                                                }
//                                            }*/ // if else commented by pr on 2ndapr19 
//                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fwd to mailadmin fwd_madmin value is " + fwd_madmin);
//                                            break;
//                                        } else {
//                                            fwd_madmin = "support@gov.in";
//                                        }
//                                    }
//                                } else // if not found any row related to this reqg no 
//                                {
//                                    daForm = false;
//                                }
//                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " daForm value is " + daForm);
                        if (!daForm) // if not DA related forms or if no mailadmin found in the employment_coordinator table then forward directly to mailadmin from the mailadmin_forms table
                        {
                            ArrayList mailArr = null;  // line added by pr on 25thoct18
                            if (arr[1].equals(Constants.IP_FORM_KEYWORD)) // if and else around added by pr on 25thoct18, so decide mailadmin at coordinator approve based on ip_action_request value in ip_registration table
                            {
                                HashMap hmap = fetchFormDetail(arr[0], Constants.IP_FORM_KEYWORD);
                                String ip_action_request = hmap.get("ip_action_request").toString();
                                if (ip_action_request.equals("ldap")) // the mailadmin would be rajesh sir
                                {
                                    fwd_madmin = "rajesh.singh@nic.in";
                                } else if (ip_action_request.equals("sms")) // the mailadmin would be smssupport
                                {
                                    fwd_madmin = "smssupport@nic.in";
                                } else if (ip_action_request.equals("relay")) // the mailadmin would be maiadmins support team
                                {
                                    mailArr = fetchMailAdmins(formName);
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside not da form size of mailArr is " + mailArr.size());
                                    fwd_madmin = String.join(",", mailArr);
                                }
                            } else {
                                //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside not da form ");
                                mailArr = fetchMailAdmins(formName);
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside not da form size of mailArr is " + mailArr.size());
                                fwd_madmin = String.join(",", mailArr);
                            }
                        }
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fwd_madmin value is " + fwd_madmin);
                        if (!this.isDa) {
                            if (fwd_madmin != null && !fwd_madmin.isEmpty()) // in case of daform fwd_madmin value will be set in the above loop with formnames
                            {
//                                String[] madmins;
//                                if (fwd_madmin.contains(",")) {
//                                    madmins = fwd_madmin.split(",");
//                                } else {
//                                    madmins = new String[1];
//                                    madmins[0] = fwd_madmin;
//                                }
//                                for (String m : madmins) {
//                                    if (!(m.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$"))) {
//                                        madminErr = true;
//                                    }
//                                }
                                //if (!madminErr) {
                                res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, fwd_madmin, "", ""); //statRemarks added by pr on 5thjun18
                                if (res) {

                                    isSuccess = true;
                                    isError = false;
                                    msg = "Application (" + reg_no + ") Approved and  Forwarded to the Admin - " + fwd_madmin;
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    if (msg.equals("")) {
                                        msg = "Application (" + reg_no + ") could not be Forwarded to Admin";
                                    }
                                }
//                                } else {
//                                    fwd_madmin = "";
//                                    isSuccess = false;
//                                    isError = true;
//                                    if (msg.equals("")) {
//                                        msg = "Please select a Valid Admin to forward request to.";
//                                    }
//                                }
                            } else {
//                                isSuccess = false;
//                                isError = true;
//                                if (msg.equals("")) {
//                                    msg = "Please select an Admin to forward request to.";
//                                }
                                res = updateStatus(reg_no, "mail-admin_pending", formName, statRemarks, "support@gov.in", "", ""); //statRemarks added by pr on 5thjun18
                                if (res) {
                                    isSuccess = true;
                                    isError = false;
                                    msg = "Application (" + reg_no + ") Approved and  Forwarded to the Admin - " + fwd_madmin;
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    if (msg.equals("")) {
                                        msg = "Application (" + reg_no + ") could not be Forwarded to Admin";
                                    }
                                }
                            }
                        }
                    }
                }
            } // statremarks end here
            else {
                isSuccess = false;
                isError = true;
                msg = "Please enter Valid Remarks";
            }
        }
        if (regNo != null && !regNo.equals("")) {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        if (random != null && !random.equals("")) {
            random = ESAPI.encoder().encodeForHTMLAttribute(random);
        }
        if (fwd_madmin != null && !fwd_madmin.equals("")) {
            fwd_madmin = ESAPI.encoder().encodeForHTMLAttribute(fwd_madmin);
        }
        return SUCCESS;
    }

    public String checkDNSCoordLevel(String regNo) {
        String level = "";
        PreparedStatement ps = null;
        //Connection con = null;
        ResultSet rs = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            /*String qry = "select * FROM status WHERE  stat_form_type  = '" + Constant.DNS_FORM_KEYWORD + "' AND   stat_reg_no = ?  AND stat_type = 'COORDINATOR_PENDING' "
             + ""
             + "ORDER BY stat_createdon DESC LIMIT 1 ";*/
            String qry = "select stat_forwarded_by FROM status WHERE  stat_form_type  = '" + Constants.DNS_FORM_KEYWORD + "' AND   stat_reg_no = ?  AND stat_type = 'coordinator_pending' "
                    + ""
                    + "ORDER BY stat_id DESC LIMIT 1 "; // line modified by pr on 16thoct18            
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("stat_forwarded_by").equals("c")) // if the final record is this
                {
                    level = "second";
                } else if (rs.getString("stat_forwarded_by").equals("ca")) // if the final record is this
                {
                    level = "first";
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function check dns coord level " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 106 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 107 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 108 " + e.getMessage());
                }
            }
        }
        return level;
    }

    public HashMap fetchBulkDetail(String bulk_id) {
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
            String qry = "select fname,lname,department,mobile,state,designation,dob,dor FROM bulk_users WHERE bulk_id = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, bulk_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("department", rs.getString("department"));
                hm.put("city", rs.getString("state"));
                hm.put("mobile", rs.getString("mobile"));
                hm.put("fname", rs.getString("fname"));
                hm.put("lname", rs.getString("lname"));
                hm.put("description", "");
                hm.put("state", rs.getString("state"));
                hm.put("title", rs.getString("designation"));// line modified by pr on 14thmar18
                hm.put("dob", rs.getString("dob"));
                hm.put("dor", rs.getString("dor"));
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set bulk details " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 119" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 120" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 121" + e.getMessage());
                }
            }
        }
        return hm;
    }

    public HashMap fetchBulkDetailsFromBaseTable(String regNumber) {
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
            String qry = "select type,id_type, description FROM bulk_registration WHERE registration_no = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, bulk_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                hm.put("id_type", rs.getString("id_type"));
                hm.put("type", rs.getString("type"));
                hm.put("description", rs.getString("description"));
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set bulk details " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 119" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 120" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 121" + e.getMessage());
                }
            }
        }
        return hm;
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

    //added by nikki on 26thjun2019
    public boolean updateVPN_reg_no(String new_vpn_no, String reg_no) {
        PreparedStatement ps = null;
        Boolean flag = false;
        //Connection con = null;
        try {
            con = DbConnection.getConnection();
            String qry = " UPDATE vpn_registration SET vpn_reg_no = ? WHERE registration_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, new_vpn_no.trim());
            ps.setString(2, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateVPN_reg_no query is " + ps);
            int i = ps.executeUpdate();
            if (i > 0) {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insinde updateVPN_reg_no exception " + e.getMessage());
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

    // function added on 5thdec17 to modify ldap attribute
    public String fetchLDAPDN(String email, String formName) {
        String dn = "";
        if (!email.equals("")) {
            ArrayList<String> data = new ArrayList<String>();
            //ServletContext sctx = getServletContext();
            /*String host = sctx.getInitParameter("host");
             String port = sctx.getInitParameter("port");
             String dn = sctx.getInitParameter("binddn");
             String dn_pass = sctx.getInitParameter("bindpass");*/
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//            ht.put(Context.PROVIDER_URL, providerurlSlave);
//            ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//            ht.put(Context.SECURITY_PRINCIPAL, createdn);
//            ht.put(Context.SECURITY_CREDENTIALS, createpass);
            DirContext ctx = null; // line added  by pr on 16thoct18
            try {
                //ctx = new InitialDirContext(ht);

                ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

                //String[] IDs = {"uid", "mail", "mailequivalentaddress", "cn", "mobile"};
                String[] IDs = {"uid", "mobile", "cn"};
                SearchControls ctls = new SearchControls();
                ctls.setReturningAttributes(IDs);
                ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + "))";
                NamingEnumeration ans = ctx.search("dc=nic,dc=in", filter, ctls);
                if (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    if (!t.equals("")) {
                        String[] arr = t.split(":");
                        if (arr != null && arr.length > 0) {
                            dn = arr[0];
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
            } finally // finally added by pr on 16thoct18
            {
//                if (ctx != null) {
//                    try {
//                        ctx.close();
//                    } catch (Exception e) {
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 143" + e.getMessage());
//                    }
//                }
            }
        }
        return dn;
    }

    public String addCoord() {
        String reg_no = "", formName = "";
        HashMap empDetails = null;
        String[] arr = null;
        msg = "";// line added by pr on 20thdec17
        if (regNo != null && !regNo.equals("")) {
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        }
        if (random.matches(regNoRegEx) && sessionMap.get("random").equals(random)) // if across added by pr on 3rdjan18 to apply csrf token check, line modified by pr on 6thfeb18
        {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside addCoord func and inside random value match with session value ");
            // in this case action type will be forward only
            if (arr != null && arr.length == 3) {
                if (!choose_da_type.equals("") && (choose_da_type.equals("da") || choose_da_type.equals("c"))) // if around and else added by pr on 12thmar18
                {
                    // get the details of employment from the respective table across the reg no
                    reg_no = arr[0];
                    formName = arr[1];
                    empDetails = fetchEmploymentDetails(formName, reg_no);
                    if (empDetails != null) {
                        // get coordinators from a function in an arraylist
                        // first check if the coordinator doesnt exist in the LDAP, if exist then check and insert in the DB
                        if (!add_coord.equals(""))// if else added by pr on 20thdec17
                        {
                            if (add_coord.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                                if (!emailAvail(add_coord))// if else block added by pr on 20thdec17
                                {
                                    if (choose_da_type.equals("c")) // if around added by pr on 12thmar18
                                    {
                                        Boolean res = setEmploymentCoords(empDetails, add_coord);
                                        if (res) {
                                            isSuccess = true;
                                            isError = false;
                                            msg = "Coordinator added Successfully";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " msg value is " + msg);
                                            if (msg.equals(""))// as msg has been set in the above function setEmploymentCoords
                                            {
                                                msg = "Coordinator could not be added";
                                            }
                                        }
                                    } else if (choose_da_type.equals("da")) // else if around added by pr on 12thmar18
                                    {
                                        Boolean res = setEmploymentDAs(empDetails, add_coord);
                                        if (res) {
                                            isSuccess = true;
                                            isError = false;
                                            msg = "DA-Admin added Successfully";
                                        } else {
                                            isSuccess = false;
                                            isError = true;
                                            // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " msg value is " + msg);
                                            if (msg.equals(""))// as msg has been set in the above function setEmploymentCoords
                                            {
                                                msg = "DA-Admin could not be added";
                                            }
                                        }
                                    }
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    msg = "Coordinator could not be added as it Doesnt Exist in the Records.";
                                }
                            } else {
                                add_coord = ""; // line added by pr on 7thfeb18 for xss                            
                                isSuccess = false;
                                isError = true;
                                msg = "Please choose Valid Coordinator to Add.";
                            }
                        } else {
                            isSuccess = false;
                            isError = true;
                            msg = "Please enter Coordinator to add.";
                        }
                    }
                } else {
                    isSuccess = false;
                    isError = true;
                    msg = "Please choose valid DA/Coordinator.";
                }
            }
        } else // else added by pr on 3rdjan18
        {
            random = ""; // line added by pr on 6thfeb18
            isSuccess = false;
            isError = true;
            msg = "Coordinator could not be added.";
        }
        if (regNo != null && !regNo.equals("")) {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        if (random != null && !random.equals("")) {
            random = ESAPI.encoder().encodeForHTMLAttribute(random);
        }
        if (add_coord != null && !add_coord.equals("")) {
            add_coord = ESAPI.encoder().encodeForHTMLAttribute(add_coord);
        }
        return SUCCESS;
    }

    public Boolean emailAvail(String email) {
        Boolean flag = true;
        storeMsg = ""; // line added by pr on 20thaug18
//        Hashtable ht = new Hashtable();
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext context = null;// line added by pr on 16thoct18
        try {
            //context = new InitialDirContext(ht);

            context = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress"};
            String[] IDs = {"uid", "mail", "mailequivalentaddress", "mailMessageStore"}; // line modified by pr on 20thaug18
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //String filter = "(|(mail=" + mobile + ")(mailequivalentaddress=" + mobile + "))";
            String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + ")(uid=" + email + ") )"; // line modified by pr on 19thfeb18
            NamingEnumeration ans = context.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();
                // System.out.println(" search result in mobile avail func for mobile " + mobile + " is " + t);
                String uids = t.substring(t.indexOf("uid:") + 5, t.length() - 1);
                // start, code added by pr on 20thaug18
                //System.out.println(" uids is " + uids);
                String mailMessageStore = t.substring(t.indexOf("mailmessagestore=mailMessageStore:"));
                // System.out.println(" mailMessageStore string value is " + mailMessageStore);
                String[] storeArr = mailMessageStore.split(",");
//                for (String a : storeArr) {
//                    System.out.println(" inside storeArr value is " + a);
//                }
                if (storeArr[0].contains("without-container")) {
                    //System.out.println(" without container / mail-box ");
                    storeMsg = "Application ID ";
                } else {
                    //System.out.println(" with Mailbox ");
                    storeMsg = "Mail ID ";
                }
                // end, code added by pr on 20thaug18                
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (context != null) {
//                try {
//                    context.close();
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 167" + e.getMessage());
//                }
//            }
        }
        return flag;
    }

    // below function added by pr on 19thjul19
    public Boolean isNICEmployee(String email) {
        Boolean flag = false;
        storeMsg = ""; // line added by pr on 20thaug18
//        Hashtable ht = new Hashtable();
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, binddn);
//        ht.put(Context.SECURITY_CREDENTIALS, bindpass);
        DirContext context = null;// line added by pr on 16thoct18
        try {
            //context = new InitialDirContext(ht);

            context = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress"};
            String[] IDs = {"dn"}; // line modified by pr on 20thaug18
            SearchControls ctls = new SearchControls();
            ctls.setReturningAttributes(IDs);
            ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            //String filter = "(|(mail=" + mobile + ")(mailequivalentaddress=" + mobile + "))";
            String filter = "(|(mail=" + email + ")(mailequivalentaddress=" + email + ")(uid=" + email + ") )"; // line modified by pr on 19thfeb18
            NamingEnumeration ans = context.search("dc=nic,dc=in", filter, ctls);
            if (ans.hasMoreElements()) {
                javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                String t = sr.toString();

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isNICEmployee func  search result for email " + email + " is " + t);

                if (t.toLowerCase().contains("nic employees")) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isNICEmployee func for email " + email + " is " + t);

                    flag = true;
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (context != null) {
//                try {
//                    context.close();
//                } catch (Exception e) {
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 167" + e.getMessage());
//                }
//            }
        }

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside isNICEmployee func email " + email + " flag is " + flag);

        return flag;
    }

    // below function added by pr on 3rdapr18
    public String fetchMobileLDAPMail(String mobile) {
        if (mobile.startsWith("+91")) {
            mobile = mobile.substring(3); // start from the index 3
        } else if (mobile.startsWith("91")) {
            mobile = mobile.substring(2);// start from the index 2
        }
        mobile = "+91" + mobile;
        String finalStr = "";
        DirContext ctx = null; // line added by pr on 16thoct18
        try {
//            Hashtable ht = new Hashtable(4);
//            ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
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
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside has more ");
                while (ans.hasMoreElements()) {
                    javax.naming.directory.SearchResult sr = (javax.naming.directory.SearchResult) ans.nextElement();
                    String t = sr.toString();
                    // get substring from start { to }
                    int start = t.indexOf("{");
                    int end = t.indexOf("}");
                    String substr = t.substring(start + 1, end);
                    String temp = "";
                    if (substr.contains("mail:")) // if there is an mobile linked with this mobile then only show them 
                    {
                        // get mail
                        String mail = substr.substring(substr.indexOf("mail:") + 6, substr.indexOf(","));
                        mail = mail.trim();

                        // start code added by pr on 17thdec19
                        temp = mail; // above line commented below added by pr on 29thmay18
                        if (!arr.contains(temp)) {
                            arr.add(temp);
                        }
                        // end code added by pr on 17thdec19

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
                        //temp = uid + "|" + mail + "|" + maileqv;
//                        temp = uid; // above line commented below added by pr on 29thmay18
//                        if (!arr.contains(temp)) {
//                            arr.add(temp);
//                        } // commented by pr on 17thdec19
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
            //      ht.clear();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 169" + e.getMessage());
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Exception in User detail Mobile finding ::::::: " + e);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        }
        return finalStr;
    }

    // fetch ldap id creation related details from the user_reg table
    public HashMap fetchSingleDetail(String regNo) {
        String sms_service = "";
        ResultSet rs = null;
        //Connection con = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        PreparedStatement ps = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            // from request_Flag fields added by pr on 29thapr2020
            String qry = "select auth_off_name,department,city,mobile,description,state,designation,dob,dor,"
                    + "type,request_flag,emp_code, other_applicant_name, other_applicant_mobile, other_applicant_state,"
                    + "other_applicant_dept,other_applicant_empcode,other_applicant_desig,other_applicant_empcode,id_type "
                    + "FROM single_registration WHERE registration_no = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            rs = ps.executeQuery();
            while (rs.next()) {
                // start, code added by pr on 21stdec17
                String fname = "", lname = "";
                // not null check added by pr on 1stmay2020    
                if (rs.getString("request_flag") == null || rs.getString("request_flag").equalsIgnoreCase("n")) {

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
                    hm.put("description", rs.getString("description"));
                    hm.put("state", rs.getString("state"));
                    hm.put("title", rs.getString("designation"));// line modified by pr on 14thmar18
                    hm.put("dob", rs.getString("dob"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("type", rs.getString("type")); // line added by pr on 28thfeb18
                    hm.put("id_type", rs.getString("id_type")); // line added by AT on 27thOct2022
                    hm.put("designation", rs.getString("designation")); // line added by pr on 
                    hm.put("emp_code", rs.getString("emp_code")); // line added by pr on 29thapr2020 

                } else {

                    if (!rs.getString("other_applicant_name").equals("")) {
                        String[] splited = rs.getString("other_applicant_name").split("\\s+");
                        if (splited.length > 0) {
                            fname = splited[0];
                        }
                        if (splited.length > 1) {
                            lname = splited[1];
                        }
                    }
                    hm.put("department", rs.getString("other_applicant_dept"));
                    hm.put("mobile", rs.getString("other_applicant_mobile"));
                    hm.put("fname", fname);
                    hm.put("lname", lname);
                    hm.put("description", rs.getString("description"));
                    hm.put("state", rs.getString("other_applicant_state"));
                    hm.put("title", rs.getString("other_applicant_desig"));
                    hm.put("dob", rs.getString("dob"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("type", rs.getString("type"));
                    hm.put("id_type", rs.getString("id_type")); // line added by AT on 27thOct2022
                    hm.put("designation", rs.getString("other_applicant_desig"));
                    hm.put("emp_code", rs.getString("other_applicant_empcode"));
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " set coordinator function 1" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 113 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 114 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 115 " + e.getMessage());
                }
            }
        }
        return hm;
    }

    public ArrayList fetchMailAdmins(String formName) {
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchMailAdmins ");
        ArrayList<String> arr = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            /*Class.forName("com.mysql.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
            // below code added by pr on 19thdec17
//            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String field = "m_" + formName;
            if (formName.contains("nkn"))// code added by pr on 26thdec17
            {
                field = "m_nkn";
            }
            if (formName.contains("vpn") || formName.contains("change_add"))// code added by pr on 4thjan18
            {
                field = "m_vpn";
            }
            if (formName.contains("emailactivate"))// code added by pr on 26thdec17
            {
                field = "m_email_act";
            }
            if (field.matches("^[a-zA-Z_]*$")) {
                String qry = "select m_email FROM mailadmin_forms WHERE " + field + " = 'y' "
                        + ""
                        + " GROUP BY m_email ORDER BY m_email ASC ";
                ps = conSlave.prepareStatement(qry);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchMailAdmins func query is " + ps);
                rs = ps.executeQuery();

                if (!formName.equals("ldap")) // if around and below else  added by pr on 2ndmay18
                {
                    while (rs.next()) {
                        arr.add(rs.getString("m_email"));
                    }
                } else {
                    arr.add("rajesh.singh@nic.in");
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " set coordinator function mailadmin forms " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 170" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 171" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 172" + e.getMessage());
                }
            }
        }
        if (arr.contains("tiwari.ashwini@nic.in")) {
            arr.remove("tiwari.ashwini@nic.in");
        }
        if (arr.contains("neeraj.tuteja@nic.in")) {
            arr.remove("neeraj.tuteja@nic.in");
        }
        if (arr.contains("rahulm.96@nic.in")) {
            arr.remove("rahulm.96@nic.in");
        }
        if (arr.contains("rajesh.101@nic.in")) {
            arr.remove("rajesh.101@nic.in");
        }
        if (arr.contains("prog18.nhq-dl@nic.in")) {
            arr.remove("prog18.nhq-dl@nic.in");
        }
        if (arr.contains("ssa1.shq.ap@supportgov.in")) {  // Shashikannth Haydarabad 20-jan-2022
            arr.remove("ssa1.shq.ap@supportgov.in");
        }
        if (arr.contains("pkr.gaurav@supportgov.in")) {  // prabhat 
            arr.remove("pkr.gaurav@supportgov.in");
        }
        if (arr.contains("prog15.nhq-dl@nic.in")) {
            arr.remove("prog15.nhq-dl@nic.in");
        }
        if (arr.contains("prog16.nhq-dl@supportgov.in")) {
            arr.remove("prog16.nhq-dl@supportgov.in");
        }
        if (arr.contains("manikant.96@supportgov.in")) {
            arr.remove("manikant.96@supportgov.in");
        }
        if (arr.contains("vivekbaghel.999@supportgov.in")) {
            arr.remove("vivekbaghel.999@supportgov.in");
        }

        return arr;
    }

    public String fetchAdmins() {
        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchAdmins ");
        String reg_no = "", formName = "";
        HashMap empDetails = null;
        String[] arr = null;
        //if( regNo != null &&  !regNo.equals("")  && regNo.matches("^[a-zA-Z0-9~\\-]*$") ) // line modified by pr on 5thfeb18 for xss
        if (regNo != null && !regNo.equals("") && regNo.matches(regNoRegEx)) // line modified by pr on 5thfeb18 for xss
        {
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        } else {
            regNo = ""; // line added by pr on 6thfeb18
        }
        // in this case action type will be forward only
        if (arr != null && arr.length == 3) {
            // get the details of employment from the respective table across the reg no
            reg_no = arr[0];
            formName = arr[1];
            madmindata = fetchMailAdmins(formName);
            // add these into a session so as to check while forwarding, code added by pr on 11thjan18
            sessionMap.put("madmindata", madmindata);
        }
        if (regNo != null && !regNo.equals("")) // line added by pr on 8thfeb18 for xss
        {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        return SUCCESS;
    }

    // below function added by pr on 1stmay19
    public String fetchEmpType() {
        //System.out.println(" inside fetchEmpType regno is " + regNo);

        String[] arr = null;
        if (regNo != null && !regNo.equals("")) {
            arr = regNo.split("~");
        }
        if (arr != null && arr.length >= 2) {
            // System.out.println(" inside fetchimapvalue array value is " + arr + " arr 0 is " + arr[0] + " arr 1 " + arr[1]);
            HashMap hm = fetchFormDetail(arr[0], arr[1]);

            if (hm.get("emp_type") != null && !hm.get("emp_type").toString().equals("")) {
                emp_type = hm.get("emp_type").toString();
            }

            if (hm.get("emp_type") != null && !hm.get("emp_type").toString().equals("")) {
                emp_type = hm.get("emp_type").toString();
            }

            if (hm.get("preferred_email1") != null && !hm.get("preferred_email1").toString().equals("")) {
                prefEmail = hm.get("preferred_email1").toString();
            }

            if (hm.get("preferred_email2") != null && !hm.get("preferred_email2").toString().equals("")) {
                prefEmail += ", " + hm.get("preferred_email2").toString();
            }
        }

        // start, code added by pr on 4thdec19
        domain = LdapQuery.GetTotalDomain(); // line added by pr on 4thdec19

        domain.replace("associateddomain: ", "");

        String[] domainArr = domain.split(","); // line added by pr on 4thdec19

        //domaindata = (ArrayList) Arrays.asList(domainArr);
        domaindata = new ArrayList<>(Arrays.asList(domainArr));

        // end, code added by pr on 4thdec19        
        //System.out.println(" inside fetchEmpType func  regNo is " + regNo + " emp_type is " + emp_type);
        return SUCCESS;
    }

    // end, functions modified by pr on 14thdec17
    // start, code added  by pr on 30thjul19
    public void exportDataExcel() throws Exception {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exportExcel function regNo " + regNo + " type  " + type);
        NewCAHome homeObj = new NewCAHome();
        ArrayList arr = null;

        if (sessionMap.get("exportData") != null) {
            arr = (ArrayList) sessionMap.get("exportData");
        }

        if (arr.size() > 0 && arr.get(0) != null) {
            ExcelCreator excelCreator = new ExcelCreator();
            String excelFileName = "";
            String sheetName = "";//name of sheet
            excelFileName = "/tmp/data.xls";//name of excel file 
            sheetName = "Export Data";//name of sheet
            excelCreator.createWorkbookHash(arr, excelFileName, sheetName);
        }
    }

    public String exportData() throws Exception // mapped in struts.xml
    {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside export function regno is " + regNo + " type is " + type);
        exportDataExcel();

        String filename = "/tmp/data.xls";
        File file = new File(filename);
        if (file.exists()) {
            fileInputStream = new NewFileInputStream(file);
            if (file.exists()) {
            } else {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside file doesnt exists ");
            }
        } else {
            filename = "/tmp/norecord.xls";
            //filename = "F:\\norecord.xls";
            ExcelCreator excelCreator = new ExcelCreator();
            ArrayList arr = new ArrayList();
            HashMap hm = new HashMap();
            hm.put("No Record Found", "");
            arr.add(hm);
            excelCreator.createWorkbookHash(arr, filename, "No Record");
            file = new File(filename);
            fileInputStream = new NewFileInputStream(file);
        }
        return SUCCESS;
    }

    // end, code added by pr on 30thjul19    
    // function added on 5thdec17 to modify ldap attribute
    public HashMap<String, String> fetchFormDetail(String reg_no, String formName) {
        HashMap<String, String> hm = new HashMap();
        PreparedStatement ps = null;
        ResultSet rs = null;
        hm.put("form_type", formName);
        if (formName.equals(Constants.IMAP_FORM_KEYWORD)) {
            // fetch the mobile from the imap table
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization,city,address added by pr on 20thmay2020
                String qry = "select auth_email,hod_email,add_state,mobile,protocol,employment,ministry,department,state,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,organization,city,address,other_dept FROM imappop_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("add_state", rs.getString("add_state")); // line added by pr on 26thfeb18
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    hm.put("protocol", rs.getString("protocol")); // line added by pr on 27thaug18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19     
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 6" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 185" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 186" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 187" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.WIFI_FORM_KEYWORD)) {
            // fetch the mobile from the wifi table
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
//                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,fwd_ofc_email,wifi_type,wifi_value,add_state,mobile,wifi_process,auth_off_name,designation,"
                        + "fwd_ofc_name,fwd_ofc_desig,fwd_ofc_mobile,"
                        + "fwd_ofc_email,fwd_ofc_add,emp_code,employment,state,ministry,department,other_dept,organization,wifi_os1,wifi_os2,"
                        + "wifi_os3,pdf_path,rename_sign_cert,ca_rename_sign_cert,city,address "
                        + "FROM wifi_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    //if( rs.getString("auth_email").equalsIgnoreCase("nic") )
                    {
                        hm.put("email", rs.getString("auth_email"));
                        hm.put("hodemail", rs.getString("fwd_ofc_email"));
                        hm.put("wifi_type", rs.getString("wifi_type"));
                        hm.put("wifi_value", rs.getString("wifi_value"));
                        hm.put("add_state", rs.getString("add_state"));
                        hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                        // start, code added by pr on 7thdec18 for wifi
                        hm.put("wifi_process", rs.getString("wifi_process"));
                        hm.put("auth_off_name", rs.getString("auth_off_name"));
                        hm.put("designation", rs.getString("designation"));
                        hm.put("auth_email", rs.getString("auth_email"));
                        hm.put("fwd_ofc_name", rs.getString("fwd_ofc_name"));
                        hm.put("fwd_ofc_desig", rs.getString("fwd_ofc_desig"));
                        hm.put("fwd_ofc_mobile", rs.getString("fwd_ofc_mobile"));
                        hm.put("fwd_ofc_email", rs.getString("fwd_ofc_email"));
                        hm.put("fwd_ofc_add", rs.getString("fwd_ofc_add"));
                        hm.put("emp_code", rs.getString("emp_code"));

                        //Organization Details
                        hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                        hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                        hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                        hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                        hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                        hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                        hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                        //////////////////
                        hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                        hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                        hm.put("wifi_os1", rs.getString("wifi_os1"));
                        hm.put("wifi_os2", rs.getString("wifi_os2"));
                        hm.put("wifi_os3", rs.getString("wifi_os3"));
                        // end, code added by pr on 7thdec18 for wifi 

                        // start, code added by pr on 2nddec19
                        hm.put("pdf_path", rs.getString("pdf_path"));
                        hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                        hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                        // end, code added by pr on 2nddec19   
                    }
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " wifi email " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 188" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 189" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 190" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.MOB_FORM_KEYWORD)) {
            // fetch the mobile from the mob table
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
//                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
                String qry = "select auth_email,hod_email,new_mobile,country_code,add_state,mobile,employment,ministry,department,state,pdf_path,"
                        + "rename_sign_cert,ca_rename_sign_cert,organization,city,address,nic_dateofbirth,nic_dateofretirement,nic_designation,nic_displayname,other_dept FROM mobile_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("new_mobile", rs.getString("new_mobile"));
                    hm.put("country_code", rs.getString("country_code"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    hm.put("nicDateOfBirth", rs.getString("nic_dateofbirth"));
                    hm.put("nicDateOfRetirement", rs.getString("nic_dateofretirement"));
                    hm.put("designation", rs.getString("nic_designation"));
                    hm.put("displayName", rs.getString("nic_displayname"));
                    // end, code added by pr on 2nddec19   

                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 7 " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 191" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 192" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 193" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.GEM_FORM_KEYWORD))// added by pr on 18thdec17
        {
            // fetch the mobile from the mob table
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
//                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // state column fetched by pr on 23rdapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
                String qry = "select auth_email,auth_off_name,add_state,city,mobile,description,designation,hod_email,department,preferred_email1,preferred_email2,state,dor,"
                        + "employment,ministry,pdf_path,rename_sign_cert,ca_rename_sign_cert,organization,city,address,other_dept "
                        + "FROM gem_registration WHERE registration_no = ?  "; // employment added  by pr on 4thsep19
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                //System.out.println("PS:::::::::##########::::" + ps);
                rs = ps.executeQuery();
                // above code commented below added by pr on 19thfeb18
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
                    hm.put("city", rs.getString("city"));
                    hm.put("mobile", rs.getString("mobile"));
                    hm.put("fname", fname);
                    hm.put("lname", lname);
                    hm.put("description", rs.getString("description"));
                    hm.put("title", rs.getString("designation"));// line modified by pr on 14thmar18
                    hm.put("dob", "");
                    hm.put("dor", rs.getString("dor"));
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("preferred_email2", rs.getString("preferred_email2"));

                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    //////////////////

                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 8 " + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 194" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 195" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 196" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.BULK_FORM_KEYWORD))// added by pr on 13thmar18
        {
            // fetch the mobile from the mob table
            //Connection con = null;
            try {
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();

                // ministry onwards added by pr on 29thapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select type,auth_email,add_state,hod_email,city,mobile,id_type,emp_type,under_sec_name,under_sec_email,under_sec_mobile"
                        + ",under_sec_desig,under_sec_telephone,ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, ca_desig, other_dept, "
                        + "designation,employment,state,pdf_path,rename_sign_cert,ca_rename_sign_cert,city,address FROM bulk_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                // above code commented below added by pr on 19thfeb18
                while (rs.next()) {
                    // start, code added by pr on 21stdec17
                    String fname = "", lname = "";
                    /*if (!rs.getString("auth_off_name").equals("")) {
                     String[] splited = rs.getString("auth_off_name").split("\\s+");
                     if (splited.length > 0) {
                     fname = splited[0];
                     }
                     if (splited.length > 1) {
                     lname = splited[1];
                     }
                     }
                     // end, code added by pr on 21stdec17*/
                    hm.put("type", rs.getString("type"));
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    hm.put("id_type", rs.getString("id_type")); // line added by pr on 29thmay18
                    hm.put("emp_type", rs.getString("emp_type")); // line added by pr on 25thsep18 to send to coordinator in case of contractual employee
                    // start, code added by pr on 6thjan19
                    hm.put("under_sec_name", rs.getString("under_sec_name"));
                    hm.put("under_sec_desig", rs.getString("under_sec_desig"));
                    hm.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                    hm.put("under_sec_email", rs.getString("under_sec_email"));
                    hm.put("under_sec_telephone", rs.getString("under_sec_telephone"));
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : ""); // line added by pr on 25thfeb2020
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   

                }
                qry = "select mail from bulk_users WHERE registration_no = ? and error_status = 0";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                System.out.println("select bulk record:::::::" + ps);
                rs = ps.executeQuery();
                // above code commented below added by pr on 19thfeb18
                String preferredEmail = "";
                String prefEmailTemp = "";
                while (rs.next()) {
                    prefEmailTemp = rs.getString("mail").trim().toLowerCase();
                    if (prefEmailTemp.contains("@")) {
                        preferredEmail += prefEmailTemp + ",";
                    }
                }
                preferredEmail = preferredEmail.replaceAll("\\s*,\\s*$", "");
                hm.put("preferred_email1", preferredEmail);
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 9" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 197" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 198" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 199" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.LDAP_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
//                        + "organization,city,address,other_dept FROM ldap_registration WHERE registration_no = ?  "; // employment 4thsep19
                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
                        + "organization,city,address,other_dept,state.department FROM ldap_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18

                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 10" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 200" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 201" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 202" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.SMS_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
//                        + "organization,city,address,other_dept FROM sms_registration WHERE registration_no = ?  "; // employment 4thsep19
                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
                        + "organization,city,address,other_dept,state,department FROM sms_registration WHERE registration_no = ?  ";

                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 11" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    

                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 203" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 204" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.IP_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile,ip_action_request,employment,ministry,pdf_path,rename_sign_cert,"
//                        + "ca_rename_sign_cert,organization,city,address,other_dept FROM ip_registration WHERE registration_no = ?  "; // employment 4thsep19
                String qry = "select auth_email,add_state,hod_email,mobile,ip_action_request,employment,ministry,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,organization,city,address,other_dept,state,department FROM ip_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    hm.put("ip_action_request", rs.getString("ip_action_request")); // line added by pr on 25thoct18 to distinguish admins at the coordinator level   

                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 12" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 205" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 206" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 207" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.DIST_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry, pdf_path,rename_sign_cert,"
//                        + "ca_rename_sign_cert,organization,city,address,other_dept FROM distribution_registration WHERE registration_no = ?  "; // employment 4thsep19
                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry, pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,organization,city,address,other_dept,state,department FROM distribution_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18

                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19  

                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 13" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 208" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 209" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 210" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.BULKDIST_FORM_KEYWORD))// added by Rahul jan 2021
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,pdf_path,rename_sign_cert,"
//                        + "ca_rename_sign_cert,organization,city,address,other_dept FROM bulk_distribution_registration WHERE registration_no = ?  "; // employment 4thsep19
                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,organization,city,address,other_dept,state,department FROM bulk_distribution_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18

                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");// line added by pr on 20thmay2020
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19  

                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 13" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 208" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 209" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 210" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.DNS_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile, employment,ministry,pdf_path,rename_sign_cert,"
//                        + "ca_rename_sign_cert,req_for,organization,city,address,other_dept FROM dns_registration WHERE registration_no = ?  "; // employment added by pr on 2ndsep19
                String qry = "select auth_email,add_state,hod_email,mobile, employment,ministry,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,req_for,organization,city,address,other_dept,state,department FROM dns_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18

                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    /////////////////// 
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    hm.put("req_for", rs.getString("req_for")); // line added by pr on 8thjan2020
                    // end, code added by pr on 2nddec19   

                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 14" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 211" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 212" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 213" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.RELAY_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,department," //ministey added by rajesh 28dec 2021
//                        + "pdf_path,rename_sign_cert,ca_rename_sign_cert,"
//                        + "organization,city,address,other_dept FROM relay_registration WHERE registration_no = ?  "; // employment 4thsep19
                String qry = "select auth_email,add_state,hod_email,mobile,employment,ministry,department," //ministey added by rajesh 28dec 2021
                        + "pdf_path,rename_sign_cert,ca_rename_sign_cert,"
                        + "organization,city,address,other_dept,state FROM relay_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    ////////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 15" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 214" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 215" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 216" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.SINGLE_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();

                // ministry onwards added by pr on 29thapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,preferred_email1,preferred_email2,id_type,emp_type,under_sec_name,under_sec_desig,"
                        + "under_sec_mobile,under_sec_email,under_sec_telephone,ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, "
                        + "ca_desig, other_dept, designation,employment,state,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,request_flag,other_applicant_mobile,city,address "
                        + "FROM single_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Added by AT on 17th April 2018
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("preferred_email2", rs.getString("preferred_email2"));
                    // start, code added by pr on 19thjul18
                    hm.put("id_type", rs.getString("id_type"));
                    hm.put("emp_type", rs.getString("emp_type")); // line added by pr on 25thsep18 to send to coordinator in case of contractual employee
                    // end, code added by pr on 19thjul18 
                    // start, code added by pr on 6thjan19
                    hm.put("under_sec_name", rs.getString("under_sec_name"));
                    hm.put("under_sec_desig", rs.getString("under_sec_desig"));
                    hm.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                    hm.put("under_sec_email", rs.getString("under_sec_email"));
                    hm.put("under_sec_telephone", rs.getString("under_sec_telephone"));
                    // end, code added by pr on 6thjan19
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////

                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // end, code added by pr on 29thapr19
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   

                    // start, code added by pr on 29thapr2020
                    hm.put("request_flag", rs.getString("request_flag"));
                    hm.put("other_applicant_mobile", rs.getString("other_applicant_mobile"));
                    // end, code added by pr on 29thapr2020
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 16" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 217" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 218" + e.getMessage());
                    }

                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 219" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.NKN_SINGLE_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();

                // ministry onwards added by pr on 29thapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,preferred_email1,preferred_email2,under_sec_name,under_sec_desig,under_sec_mobile,"
                        + "under_sec_email,under_sec_telephone,ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, ca_desig, "
                        + "other_dept, designation,employment,state,pdf_path,rename_sign_cert,ca_rename_sign_cert,city,address, type, id_type FROM nkn_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Added by AT on 17th April 2018
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("preferred_email2", rs.getString("preferred_email2"));
                    // start, code added by pr on 6thjan19
                    hm.put("under_sec_name", rs.getString("under_sec_name"));
                    hm.put("under_sec_desig", rs.getString("under_sec_desig"));
                    hm.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                    hm.put("under_sec_email", rs.getString("under_sec_email"));
                    hm.put("under_sec_telephone", rs.getString("under_sec_telephone"));
                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // end, code added by pr on 29thapr19
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    hm.put("type", rs.getString("type"));
                    hm.put("id_type", rs.getString("id_type"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 17" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 220" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 221" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 222" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.NKN_BULK_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                // ministry onwards added by pr on 29thapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,under_sec_name,under_sec_desig,under_sec_mobile,under_sec_email,"
                        + "under_sec_telephone,ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, ca_desig, other_dept, "
                        + "designation,employment,state,pdf_path,rename_sign_cert,ca_rename_sign_cert,city,address, type, id_type FROM nkn_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    // start, code added by pr on 6thjan19
                    hm.put("under_sec_name", rs.getString("under_sec_name"));
                    hm.put("under_sec_desig", rs.getString("under_sec_desig"));
                    hm.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                    hm.put("under_sec_email", rs.getString("under_sec_email"));
                    hm.put("under_sec_telephone", rs.getString("under_sec_telephone"));
                    // end, code added by pr on 6thjan19
                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // end, code added by pr on 29thapr19
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    hm.put("type", rs.getString("type"));
                    hm.put("id_type", rs.getString("id_type"));
                    // end, code added by pr on 2nddec19 

                    qry = "select mail from bulk_users WHERE registration_no = ?  and error_status = 0";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, reg_no);
                    System.out.println("select bulk record:::::::" + ps);
                    rs = ps.executeQuery();
                    // above code commented below added by pr on 19thfeb18
                    String preferredEmail = "";
                    String prefEmailTemp = "";
                    while (rs.next()) {
                        prefEmailTemp = rs.getString("mail").trim().toLowerCase();
                        if (prefEmailTemp.contains("@")) {
                            preferredEmail += prefEmailTemp + ",";
                        }
                    }
                    preferredEmail = preferredEmail.replaceAll("\\s*,\\s*$", "");
                    hm.put("preferred_email1", preferredEmail);
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 18" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 223" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 224" + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 225" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.VPN_ADD_FORM_KEYWORD) || formName.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || formName.equals(Constants.VPN_RENEW_FORM_KEYWORD) || formName.equals(Constants.VPN_BULK_FORM_KEYWORD) || formName.equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || formName.equals(Constants.VPN_DELETE_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,employment,state,department,ministry,organization,pdf_path,"
                        + "rename_sign_cert,ca_rename_sign_cert,city,address,other_dept FROM vpn_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();

                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 18" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (formName.equals(Constants.WEBCAST_FORM_KEYWORD)) {
            // fetch the mobile from the imap table
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 Connection con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/

                // below code added by pr on 19thdec17
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,employment,state,department,ministry,organization,"
                        + "pdf_path,rename_sign_cert,ca_rename_sign_cert,city,address,other_dept FROM webcast_registration WHERE registration_no = ?  ";

                ps = conSlave.prepareStatement(qry);

                ps.setString(1, reg_no);

                rs = ps.executeQuery();

                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   

                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 6" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (formName.equals(Constants.FIREWALL_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,employment,ministry,hod_email,mobile,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
//                        + "organization,city,address,other_dept FROM centralutm_registration WHERE registration_no = ?  ";
                String qry = "select auth_email,add_state,employment,ministry,hod_email,mobile,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
                        + "organization,city,address,other_dept,state,department FROM centralutm_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();

                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   

                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 18" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (formName.equals(Constants.WIFI_PORT_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection(); //29dec2021
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19, organization ,city,address added by pr on 20thmay2020
//                String qry = "select auth_email,add_state,employment,ministry,hod_email,mobile,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
//                        + "organization,city,address,other_dept FROM wifiport_registration WHERE registration_no = ?  ";
                String qry = "select auth_email,add_state,employment,ministry,hod_email,mobile,pdf_path,rename_sign_cert,ca_rename_sign_cert,"
                        + "organization,city,address,other_dept,state,department FROM wifiport_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();

                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   

                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 18" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (formName.equals(Constants.EMAILACTIVATE_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();

                // ministry onwards added by pr on 29thapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,preferred_email1,dor,under_sec_name,under_sec_desig,"
                        + "under_sec_mobile,under_sec_email,under_sec_telephone,ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, "
                        + "ca_desig, other_dept, designation,employment,state,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,emp_type,work_order,city,address FROM email_act_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Added by AT on 17th April 2018
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("under_sec_name", rs.getString("under_sec_name"));
                    hm.put("under_sec_desig", rs.getString("under_sec_desig"));
                    hm.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                    hm.put("under_sec_email", rs.getString("under_sec_email"));
                    hm.put("under_sec_telephone", rs.getString("under_sec_telephone"));
                    // end, code added by pr on 6thjan19

                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("work_order", rs.getString("work_order"));// line added by pr on 23rdapr2020
                    hm.put("emp_type", rs.getString("emp_type"));// line added by pr on 23rdapr2020
                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // end, code added by pr on 29thapr19
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 16" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 217" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 218" + e.getMessage());
                    }

                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 219" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD))// added by pr on 18thdec17
        {
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();

                // ministry onwards added by pr on 29thapr19
                // ,pdf_path,rename_sign_cert,ca_rename_sign_cert added by pr on 2nddec19,city,address added by pr on 20thmay2020
                String qry = "select auth_email,add_state,hod_email,mobile,preferred_email1,under_sec_name,under_sec_desig,"
                        + "under_sec_mobile,under_sec_email,under_sec_telephone,ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, "
                        + "ca_desig, other_dept, designation,employment,state,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,city,address FROM email_deact_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile")); // line added by pr on 9thapr18
                    //Added by AT on 17th April 2018
                    hm.put("preferred_email1", rs.getString("preferred_email1"));
                    hm.put("under_sec_name", rs.getString("under_sec_name"));
                    hm.put("under_sec_desig", rs.getString("under_sec_desig"));
                    hm.put("under_sec_mobile", rs.getString("under_sec_mobile"));
                    hm.put("under_sec_email", rs.getString("under_sec_email"));
                    hm.put("under_sec_telephone", rs.getString("under_sec_telephone"));
                    // end, code added by pr on 6thjan19

                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////

                    hm.put("city", rs.getString("city"));// line added by pr on 20thmay2020
                    hm.put("address", rs.getString("address"));// line added by pr on 20thmay2020

                    // end, code added by pr on 29thapr19
                    // start, code added by pr on 2nddec19
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    // end, code added by pr on 2nddec19   
                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 16" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 217" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 218" + e.getMessage());
                    }

                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 219" + e.getMessage());
                    }
                }
            }
        } else if (formName.equals(Constants.DOR_EXT_FORM_KEYWORD)) {

            try {

                conSlave = DbConnection.getSlaveConnection();
                String qry = "select auth_email,add_state,hod_email,mobile,dor_email,"
                        + "ministry, department, organization, auth_off_name, designation,hod_name, hod_mobile, "
                        + "ca_desig, other_dept,employment,state,pdf_path,rename_sign_cert,"
                        + "ca_rename_sign_cert,city,address,pre_dor,dor,renamed_filepath,uploaded_filename,dob FROM dor_ext_registration WHERE registration_no = ?  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    hm.put("email", rs.getString("auth_email"));
                    hm.put("add_state", rs.getString("add_state"));
                    hm.put("hodemail", rs.getString("hod_email"));
                    hm.put("mobile", rs.getString("mobile"));
                    hm.put("dor_email", rs.getString("dor_email"));
                    hm.put("auth_off_name", rs.getString("auth_off_name"));
                    hm.put("designation", rs.getString("designation"));
                    hm.put("hod_name", rs.getString("hod_name"));
                    hm.put("hod_mobile", rs.getString("hod_mobile"));
                    hm.put("ca_desig", rs.getString("ca_desig"));
                    //Organization Details
                    hm.put("employment", (rs.getString("employment") != null) ? rs.getString("employment") : "");
                    hm.put("ministry", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("min", (rs.getString("ministry") != null) ? rs.getString("ministry") : "");
                    hm.put("state", (rs.getString("state") != null) ? rs.getString("state") : "");
                    hm.put("department", (rs.getString("department") != null) ? rs.getString("department") : "");
                    hm.put("other_dept", (rs.getString("other_dept") != null) ? rs.getString("other_dept") : "");
                    hm.put("organization", (rs.getString("organization") != null) ? rs.getString("organization") : "");
                    //////////////////
                    hm.put("city", rs.getString("city"));
                    hm.put("address", rs.getString("address"));
                    hm.put("pdf_path", rs.getString("pdf_path"));
                    hm.put("rename_sign_cert", rs.getString("rename_sign_cert"));
                    hm.put("ca_rename_sign_cert", rs.getString("ca_rename_sign_cert"));
                    hm.put("renamed_filepath", rs.getString("renamed_filepath"));
                    hm.put("uploaded_filename", rs.getString("uploaded_filename"));
                    hm.put("dor_email", rs.getString("dor_email"));
                    hm.put("dor", rs.getString("dor"));
                    hm.put("pre_dor", rs.getString("pre_dor"));
                    hm.put("dob", rs.getString("dob"));

                }
            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 16" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 217" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 218" + e.getMessage());
                    }

                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 219" + e.getMessage());
                    }
                }
            }
        }
        return hm;
    }

    // below function added by pr on 3rdjan18
    public String resetRandom() {
        //  System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "inside reset random");
        StringBuilder sb = new StringBuilder();
        String num = "0123456789";
        SecureRandom rmh = new SecureRandom();
        for (int i = 0; i < 32; i++) {
            sb.append(num.charAt(rmh.nextInt(num.length())));
        }
        String ran = sb.toString();
        sessionMap.put("random", ran);
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside resetRandom func value of random num is "+ran+" session value is "+sessionMap.get("random"));
        random = ran;
        return SUCCESS;
    }

    // below function added by pr on 6thmar18
    public String revoke() {
        isSuccess = false;
        isError = false;
        msg = "";
        String role = "";
        String reg_no = "";
        role = fetchSessionRole();
        String email = "";
        email = fetchSessionEmail();
        ArrayList<String> mailEquiArr = fetchEmailEquivalent();
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revoke function statRemarks value is " + statRemarks);
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " random request value is " + random + " session random value is " + sessionMap.get("random"));
        //if( random.matches(regNoRegEx) &&  sessionMap.get("random").equals(random)) // if across added by pr on 3rdjan18 to apply csrf token check , xss added by pr on 6thfeb18
        {
            if (regNo.matches(regNoRegEx)) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside regno matches ");
                String[] arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
                if (arr != null && arr.length == 3) {
                    if (statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx))) // if around and else added by pr on 6thjun18
                    {
                        reg_no = arr[0];
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside statremarks matches ");
                        if (role.equals(Constants.ROLE_CO)) {
                            // revoke at CA level will be directed to the support
                            // delete the entry in status for coordinator pending and add in the trail table all the details
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revoke function and CO block ");
                            Boolean res = revokeAndUpdateStatus(arr[0], arr[1], "co_to_sup", "", statRemarks); // statRemarks added by pr on 6thjun18

                            // start, code added by pr on 26thsep19
                            String textSupEmail = sup_email;

                            if (arr[1].equalsIgnoreCase(Constants.SMS_FORM_KEYWORD)) {
                                textSupEmail = "smssupport@nic.in";
                            } else if (arr[1].equalsIgnoreCase(Constants.VPN_ADD_FORM_KEYWORD) || arr[1].equalsIgnoreCase(Constants.VPN_BULK_FORM_KEYWORD)
                                    || arr[1].equalsIgnoreCase(Constants.VPN_RENEW_FORM_KEYWORD) || arr[1].equalsIgnoreCase(Constants.VPN_SINGLE_FORM_KEYWORD) || arr[1].equalsIgnoreCase(Constants.VPN_SURRENDER_FORM_KEYWORD) || arr[1].equalsIgnoreCase(Constants.VPN_DELETE_FORM_KEYWORD)) {
                                textSupEmail = "vpnsupport@nic.in";
                            }

                            // end, code added by pr on 26thsep19                            
                            if (res) {
                                // maintain a trail
                                //trail(arr[0], "c_to_s", "c", mobile, "Forwarded from Coordinator to Support");
                                isSuccess = true;
                                isError = false;
                                //msg = "Application ("+ reg_no +") Forwarded from Coordinator to the Support (support@gov.in) ";

                                msg = "Application (" + reg_no + ") Forwarded from Coordinator to the Support (" + textSupEmail + ") ";// line modified by pr on 26thsep19

                            } else {
                                isSuccess = false;
                                isError = true;
                                if (msg.equals("")) {
                                    msg = "Application (" + reg_no + ") could not be Forwarded to Support";
                                }
                            }
                        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
                            // if at mailadmin and not support then send request to the support as mailadmin by updating the mailadmin-pending request 
                            // send an update in the trail table
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revoke function and mailadmin block ");
                            //email = "support@nic.in"; // done only for testing done on 23rdapr18
                            if (email.equals("support@nic.in") || email.equals("support@gov.in") || email.equals("support@dummy.nic.in")
                                    || (mailEquiArr != null && mailEquiArr.contains("support@nic.in"))) {
                                //then just forward the request to the DA by fetching the da mobile from the user interface
                                // check if the fwd_coord is a valid mobile
                                if (fwd_coord.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside mailadmin and support email id fwd_coord value is " + fwd_coord);
                                    Boolean res = revokeAndUpdateStatus(arr[0], arr[1], "m_to_d", fwd_coord, statRemarks); // statRemarks added by pr on 6thjun18
                                    if (res) {
                                        //trail(arr[0], "m_to_d", "m", mobile, "Forwarded from Admin to DA-Admin (" + fwd_coord + ")");
                                        isSuccess = true;
                                        isError = false;
                                        msg = "Application (" + reg_no + ") Forwarded from Admin to the DA-Admin " + fwd_coord;
                                    } else {
                                        isSuccess = false;
                                        isError = true;
                                        if (msg.equals("")) {
                                            msg = "Application (" + reg_no + ") could not be Forwarded to DA-Admin";
                                        }
                                    }
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    msg = "DA-Admin Email is not valid";
                                }
                            } else {
                                // then just forward to the support mailadmin 	
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside mailadmin not support  ");
                                Boolean res = revokeAndUpdateStatus(arr[0], arr[1], "m_to_m", "", statRemarks); // statRemarks added by pr on 6thjun18
                                if (res) {
                                    //trail(arr[0], "m_to_m", "m", mobile, "Forwarded from Admin to Support Admin (" + sup_email + ")");
                                    isSuccess = true;
                                    isError = false;
                                    msg = "Application (" + reg_no + ") Forwarded from Admin to the Admin(support@gov.in) ";
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    if (msg.equals("")) {
                                        msg = "Application (" + reg_no + ") could not be Forwarded to Admin";
                                    }
                                }
                            }
                        }
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Please enter Valid Remarks. " + Constants.STAT_REMARKS_TEXT; // line modified by pr on 28thdec18
                    }
                }
            }
        }
        return SUCCESS;
    }

    // below function added by pr on 8thmar18
    public void trail(String tr_reg_no, String tr_action, String tr_by, String tr_by_user, String tr_desc) {
        Boolean done = false;
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        Connection con = null;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " ************ip is " + ip);
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            String qry = "INSERT INTO trail SET tr_reg_no = ?, tr_action = ?, tr_by = ?, tr_by_user = ?, tr_desc = ?, tr_ip = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, tr_reg_no);
            ps.setString(2, tr_action);
            ps.setString(3, tr_by);
            ps.setString(4, tr_by_user);
            ps.setString(5, tr_desc);
            ps.setString(6, ip);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " trail insert query is " + ps);
            int result = ps.executeUpdate();
            //int result = 1;
            if (result > 0) {
                done = true;
            } else {
                done = false;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revoke and update func " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 226" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 227" + e.getMessage());
                }
            }
        }
    }

    public String pullBack() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside pull back func value of reg no is " + regNo);
        Boolean res = revokeAndUpdateStatus(regNo, "", "pull", "", ""); // statRemarks last parameter added by pr on 6thjun18
        if (res) {
            isSuccess = true;
            isError = false;
            msg = "Pull Back Successfull";
        } else {
            isSuccess = false;
            isError = true;
            msg = "Request Could not be pulled back";
        }
        return SUCCESS;
    }

    // below function added by pr on 7thmar18
    //public Boolean revokeAndUpdateStatus(String reg_no, String form_type, String caseType, String fwd_coord) 
    // stat_form_type bug fixed in status table by ram pratap on 12 aug
    public Boolean revokeAndUpdateStatus(String reg_no, String form_type, String caseType, String fwd_coord, String statRemarks) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revokeAndUpdateStatus function  caseType is " + caseType);
        Boolean done = false;
        String stat_forwarded_by = "", stat_forwarded_by_user = "", stat_forwarded_to = "", stat_forwarded_to_user = "";
        String qry = "";
        String role = fetchSessionRole();
        String email = fetchSessionEmail();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String stat_remarks_db = "";

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            if (caseType.equals("co_to_sup")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside co_to_sup ");
                try {
                    try {
                        stat_remarks_db = "Forwarded by coordinator " + email + " to support.";
                        if (!statRemarks.equals("")) {
                            stat_remarks_db += "~User Remarks - " + statRemarks;
                        }

                        if (form_type.equalsIgnoreCase(Constants.SMS_FORM_KEYWORD)) {
                            sup_email = "smssupport@nic.in";
                        } else if (form_type.equalsIgnoreCase(Constants.VPN_ADD_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_BULK_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_RENEW_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_SINGLE_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_SURRENDER_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_DELETE_FORM_KEYWORD)) {
                            sup_email = "vpnsupport@nic.in";
                        } else if (form_type.equalsIgnoreCase(Constants.DNS_FORM_KEYWORD)) {
                            sup_email = dnsmailadmin;
                        }

                        qry = "INSERT INTO status SET stat_type = 'support_pending', stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = 'c', stat_forwarded_by_user = ?, stat_forwarded_to = 's',"
                                + ""
                                + "  stat_forwarded_to_user = '" + sup_email + "' , stat_remarks = ?, stat_process='reverted_c_s'";
                        ps = con.prepareStatement(qry);
                        ps.setString(1, form_type);
                        //ps.setString(2,)
                        ps.setString(2, reg_no.toUpperCase());
                        ps.setString(3, email);
                        ps.setString(4, stat_remarks_db);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup insert query is " + ps);
                        int result = ps.executeUpdate();
                        if (result > 0) {
                            done = true;
                            Inform obj = new Inform();
                            obj.sendRevokeIntimation(reg_no, form_type, "", "", statRemarks);
                        } else {
                            done = false;
                        }
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 19" + e.getMessage());
                    }
                    //System.out.println(" before c to s final audit update value of done is " + done);
                    if (done) {
                        try {
                            qry = "UPDATE final_audit_track SET support_email = null, support_mobile = null, support_name = null, support_ip = null, support_datetime = null, "
                                    + ""
                                    + " support_remarks = null, coordinator_email = null, coordinator_mobile = null, coordinator_name = null, coordinator_ip = null, coordinator_datetime = null, "
                                    + ""
                                    + " coordinator_remarks = null, status = 'support_pending' , to_email = ?, to_datetime = CURRENT_TIMESTAMP, co_id=0,da_id=0 WHERE registration_no = ? ";// to_datetime added by pr on 11thfeb19
                            ps = con.prepareStatement(qry);
                            ps.setString(1, sup_email);
                            ps.setString(2, reg_no);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup final_audit_track query is " + ps);
                            int r = ps.executeUpdate();
                            if (r > 0) {
                                done = true;
                            } else {
                                done = false;
                            }
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup final_audit_track Exception " + e.getMessage());
                            done = false;
                        }
                    }
                    //System.out.println(" after c to s final audit update value of done is " + done);
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 2 " + e.getMessage());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 228" + e.getMessage());
                        }
                    }
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 229" + e.getMessage());
                        }
                    }
                }
            } else if (caseType.equals("m_to_d")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside m_to_d ");
                try {

                    stat_remarks_db = "forwarded by mailadmin(support) to da " + fwd_coord + ".";
                    if (!statRemarks.equals("")) {
                        stat_remarks_db += "~User Remarks - " + statRemarks;
                    }

                    qry = "INSERT INTO status SET stat_type = 'da_pending ', stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = 'm', stat_forwarded_by_user = ?, stat_forwarded_to = 'd',"
                            + ""
                            + "  stat_forwarded_to_user = ? , stat_remarks = ? ,stat_ip = ?,stat_process='forwarded_m_d' ";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, form_type);
                    //ps.setString(2, reg_no);
                    ps.setString(2, reg_no.toUpperCase());
                    ps.setString(3, email);
                    ps.setString(4, fwd_coord);
                    ps.setString(5, stat_remarks_db);
                    ps.setString(6, ip);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " m_to_d update query is " + ps);
                    int result = ps.executeUpdate();
                    if (result > 0) {
                        done = true;
                        Inform obj = new Inform();
                        obj.sendRevokeIntimation(reg_no, form_type, fwd_coord, "", statRemarks);
                    } else {
                        done = false;
                    }
                    //System.out.println(" before m to d final audit update value of done is " + done);
                    if (done) {
                        try {
                            qry = "UPDATE final_audit_track SET admin_email = ?, admin_mobile = ?, admin_name = ?, admin_ip = ?, admin_datetime = current_timestamp, "
                                    + ""
                                    + " admin_remarks = ?, status = 'da_pending', to_email = ?, to_datetime = CURRENT_TIMESTAMP,co_id=0,da_id=0  WHERE registration_no = ? "; // to_datetime = CURRENT_TIMESTAMP added by pr on 11thfeb19
                            ps = con.prepareStatement(qry);
                            ps.setString(1, email);
                            ps.setString(2, "");
                            ps.setString(3, "");
                            ps.setString(4, ip);
                            ps.setString(5, stat_remarks_db);
                            ps.setString(6, fwd_coord);
                            ps.setString(7, reg_no);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup final_audit_track query is " + ps);
                            int r = ps.executeUpdate();
                            if (r > 0) {
                                done = true;
                            } else {
                                done = false;
                            }
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup final_audit_track Exception " + e.getMessage());
                            done = false;
                        }
                    }
                    //System.out.println(" after m to d final audit update value of done is " + done);
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revoke and update func " + e.getMessage());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 230" + e.getMessage());
                        }
                    }
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 231" + e.getMessage());
                        }
                    }
                }
            } else if (caseType.equals("m_to_m")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside m_to_m ");
                try {
                    stat_remarks_db = "forwarded by mailadmin " + email + " to mailadmin Support.";
                    if (!statRemarks.equals("")) {
                        stat_remarks_db += "~User Remarks - " + statRemarks;
                    }

                    qry = "INSERT INTO status SET stat_type = 'mail-admin_pending', stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = 'm', stat_forwarded_by_user = ?, stat_forwarded_to = 'm',"
                            + ""
                            + "  stat_forwarded_to_user = ? , stat_remarks = ? , stat_ip = ?,stat_process='forwarded_m_m' ";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, form_type);
                    ps.setString(2, reg_no.toUpperCase());
                    ps.setString(3, email);
                    ps.setString(4, "support@nic.in");
                    ps.setString(5, stat_remarks_db);
                    ps.setString(6, ip);

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " m_to_m update query is " + ps);
                    int result = ps.executeUpdate();
                    if (result > 0) {
                        done = true;
                        Inform obj = new Inform();
                        obj.sendRevokeIntimation(reg_no, form_type, "", "", statRemarks);
                    } else {
                        done = false;
                    }

                    System.out.println(" before m to m  final audit update value of done is " + done);
                    if (done) {
                        try {
                            qry = "UPDATE final_audit_track SET  to_email = ?, co_id=0,da_id=0, to_datetime = CURRENT_TIMESTAMP  WHERE registration_no = ? ";
                            ps = con.prepareStatement(qry);
                            ps.setString(1, sup_email);
                            ps.setString(2, reg_no);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup final_audit_track query is " + ps);
                            int r = ps.executeUpdate();
                            if (r > 0) {
                                done = true;
                            } else {
                                done = false;
                            }
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup final_audit_track Exception " + e.getMessage());
                            done = false;
                        }
                    }
                    //System.out.println(" after m to m  final audit update value of done is " + done);
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside revoke and update func m_to_m " + e.getMessage());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 232" + e.getMessage());
                        }
                    }
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 233" + e.getMessage());
                        }
                    }
                }
            } else if (caseType.equals("pull")) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside co_to_sup ");
                Boolean isSupportInvolved = false;
                try {
                    qry = "select stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by,stat_forwarded_by_user, stat_form_type FROM status WHERE stat_reg_no = ?  ORDER BY stat_id DESC  "; // line modified by pr on 11thoct18
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, reg_no);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co to sup query is " + qry);
                    rs = ps.executeQuery();
                    String stat_form_type = "";
                    String pullBackFrom = "", pullBackFromUser = "", pullBackDB = "", pullBackInform = "";
                    while (rs.next()) {
                        stat_form_type = rs.getString("stat_form_type");
                        if (rs.getString("stat_type").equals("coordinator_pending") || rs.getString("stat_type").equals("da_pending")
                                || rs.getString("stat_type").equals("mail-admin_pending")) {
                            pullBackFrom = rs.getString("stat_forwarded_to");
                            pullBackFromUser = rs.getString("stat_forwarded_to_user");
                            if (pullBackFrom.equals("d")) {
                                //System.out.println(" inside first row as da pending ");
                                pullBackDB = "DA-Admin(" + pullBackFromUser + ")";
                                pullBackInform = "DA-Admin" + "~" + pullBackFromUser;
                            } else if (pullBackFrom.equals("c")) {
                                // System.out.println(" inside first row as coordinator pending ");
                                pullBackDB = "Coordinator(" + pullBackFromUser + ")";
                                pullBackInform = "Coordinator" + "~" + pullBackFromUser;
                            } else if (pullBackFrom.equals("m")) {
                                //System.out.println(" inside first row as mailadmin pending ");
                                pullBackDB = "Admin(" + pullBackFromUser + ")";
                                pullBackInform = "Admin" + "~" + pullBackFromUser;
                            }
                            break;
                        }

                    }
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside co to support  isSupportInvolved value is " + isSupportInvolved);

                    // start, code added by pr on 22ndoct19
                    if (form_type.equalsIgnoreCase(Constants.SMS_FORM_KEYWORD)) {
                        sup_email = "smssupport@nic.in";
                    } else if (form_type.equalsIgnoreCase(Constants.VPN_ADD_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_BULK_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_RENEW_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_SINGLE_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_SURRENDER_FORM_KEYWORD) || form_type.equalsIgnoreCase(Constants.VPN_DELETE_FORM_KEYWORD)) {
                        sup_email = "vpnsupport@nic.in";
                    }

                    // end, code added by pr on 22ndoct19 
                    try {
                        qry = "INSERT INTO status SET stat_type = 'support_pending', stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = '" + pullBackFrom + "', stat_forwarded_by_user = ?, stat_forwarded_to = 's',"
                                + ""
                                + "  stat_forwarded_to_user = '" + sup_email + "' , stat_remarks = 'pulled back by support (" + rs.getString("stat_forwarded_to_user") + ") from " + pullBackDB + "',stat_process='pulled_s_" + pullBackFrom + "'";
                        ps = con.prepareStatement(qry);
                        ps.setString(1, stat_form_type);
                        ps.setString(2, reg_no.toUpperCase());// line modified by pr on 20thapr18 
                        ps.setString(3, pullBackFromUser);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " co_to_sup insert query is " + ps);
                        int result = ps.executeUpdate();
                        if (result > 0) {
                            done = true;
                            Inform obj = new Inform();
                            obj.sendRevokeIntimation(reg_no, stat_form_type, "", pullBackInform, statRemarks);// , statRemarks added by pr on 6thjun18
                            //trail(reg_no, "pull", "s", mobile, "Pulled back from " + pullBackDB);
                        } else {
                            done = false;
                        }
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 3" + e.getMessage());
                    } finally {
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (Exception e) {
                                // above line commented below added by pr on 23rdapr19                    
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 234" + e.getMessage());
                            }
                        }
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (Exception e) {
                                // above line commented below added by pr on 23rdapr19                    
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 235" + e.getMessage());
                            }
                        }
                    }

                    // System.out.println(" before pull final audit update value of done is " + done);
                    if (done) {
                        try {
                            qry = "UPDATE final_audit_track SET support_email = null, support_mobile = null, support_name = null, support_ip = null, support_datetime = null, "
                                    + ""
                                    + " support_remarks = null, coordinator_email = null, coordinator_mobile = null, coordinator_name = null, coordinator_ip = null, coordinator_datetime = null, "
                                    + ""
                                    + " coordinator_remarks = null, status = 'support_pending' , to_email = ? , to_datetime = CURRENT_TIMESTAMP, co_id=0,da_id=0 WHERE registration_no = ? "; // , to_datetime = CURRENT_TIMESTAMP added by pr on 11thfeb19
                            ps = con.prepareStatement(qry);
                            ps.setString(1, sup_email);
                            ps.setString(2, reg_no);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " pull back final_audit_track query is " + ps);
                            int r = ps.executeUpdate();
                            if (r > 0) {
                                done = true;
                            } else {
                                done = false;
                            }
                        } catch (Exception e) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " pull back final_audit_track Exception " + e.getMessage());
                            done = false;
                        }
                    }
                    //System.out.println(" after pull final audit update value of done is " + done);
                    // end, code added by pr on 31stjan19     
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " imap email 5" + e.getMessage());
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 238" + e.getMessage());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            // above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 239" + e.getMessage());
                        }
                    }
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Exception e) {
                            // above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 240" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // above line commented below added by pr on 23rdapr19                    
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 241" + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 242" + e.getMessage());
                }
            }
        }
        return done;
    }

    // below function added by pr on 7thmar18
    public String addDA() {
        String reg_no = "", formName = "";
        HashMap empDetails = null;
        String[] arr = null;
        msg = "";// line added by pr on 20thdec17
        if (regNo != null && !regNo.equals("")) {
            arr = regNo.split("~"); // array contains first reg_no then form type then action type(reject or forward)
        }
        if (random.matches(regNoRegEx) && sessionMap.get("random").equals(random)) // if across added by pr on 3rdjan18 to apply csrf token check, line modified by pr on 6thfeb18
        {
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside addCoord func and inside random value match with session value ");
            // in this case action type will be forward only
            if (arr != null && arr.length == 3) {
                // get the details of employment from the respective table across the reg no
                reg_no = arr[0];
                formName = arr[1];
                empDetails = fetchEmploymentDetails(formName, reg_no);
                if (empDetails != null) {
                    // get coordinators from a function in an arraylist
                    // first check if the coordinator doesnt exist in the LDAP, if exist then check and insert in the DB
                    if (!add_coord.equals(""))// if else added by pr on 20thdec17
                    {
                        if (add_coord.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
                            if (!emailAvail(add_coord))// if else block added by pr on 20thdec17
                            {
                                Boolean res = setEmploymentDAs(empDetails, add_coord);
                                if (res) {
                                    isSuccess = true;
                                    isError = false;
                                    msg = "DA-Admin added Successfully";
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " msg value is " + msg);
                                    if (msg.equals(""))// as msg has been set in the above function setEmploymentCoords
                                    {
                                        msg = "DA-Admin could not be added";
                                    }
                                }
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "DA-Admin could not be added as it Doesnt Exist in the Records.";
                            }
                        } else {
                            add_coord = ""; // line added by pr on 7thfeb18 for xss                            
                            isSuccess = false;
                            isError = true;
                            msg = "Please choose Valid DA-Admin to Add.";
                        }
                    } else {
                        isSuccess = false;
                        isError = true;
                        msg = "Please enter DA-Admin to add.";
                    }
                }
            }
        } else // else added by pr on 3rdjan18
        {
            random = ""; // line added by pr on 6thfeb18
            isSuccess = false;
            isError = true;
            msg = "DA-Admin could not be added.";
        }
        if (regNo != null && !regNo.equals("")) {
            regNo = ESAPI.encoder().encodeForHTMLAttribute(regNo);
        }
        if (random != null && !random.equals("")) {
            random = ESAPI.encoder().encodeForHTMLAttribute(random);
        }
        if (add_coord != null && !add_coord.equals("")) {
            add_coord = ESAPI.encoder().encodeForHTMLAttribute(add_coord);
        }
        return SUCCESS;
    }

    // abov efunction modified by pr on 24thoct19
    public String raiseQuery() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Inside raiseQuery::::::::::::::::: ");
        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();  // audit by sahil
        System.out.println("CHECKINGGG:::::" + "SessionCSRFRandom:- " + SessionCSRFRandom + "ANDDD : " + "CSRFRandom ::--" + CSRFRandom);
        if (!CSRFRandom.equals(SessionCSRFRandom)) {
            hmTrack.put("csrf_error", "CSRF Token is invalid.");
            System.out.println("invalid csrffff");
            return SUCCESS;
        }
        sessionMap.remove("CSRFRandom");
        // ResetCSRFRandom resetRandom = new ResetCSRFRandom();
        //resetRandom.resetCSRFRandom();
        ArrayList<String> dropDownForRaiseQuery = (ArrayList<String>) sessionMap.get("raiseQueryDropDownList");
        //System.out.println("dropDownForRaiseQuery" + dropDownForRaiseQuery);
        String recipient = "";
        if (choose_recp.equals("u")) {
            recipient = "User";
        } else if (choose_recp.equals("ca")) {
            recipient = "Competent Authority";
        } else if (choose_recp.equals("s")) {
            recipient = "Support";
        } else if (choose_recp.equals("co")) {
            recipient = "NIC Coordinator";
        } else if (choose_recp.equals("da")) {
            recipient = "Delegated Admin";
        } else if (choose_recp.equals("m")) {
            recipient = "Admin";
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reg no value is " + regNo + " type  " + type);
        // add a row in the query_raise table
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside raise query func with type raise " + " choose_recp " + choose_recp + " statRemarks " + statRemarks);

        String qr_forwarded_by_str = "", qr_forwarded_by = "", role = fetchSessionRole();

        if (role.equals(Constants.ROLE_CA)) {
            qr_forwarded_by = "ca";
            qr_forwarded_by_str = "Reporting/Nodal/Forwarding Officer";
        } else if (role.equals(Constants.ROLE_CO)) {
            qr_forwarded_by = "c";
            qr_forwarded_by_str = "Coordinator";
        } else if (role.equals(Constants.ROLE_SUP)) {
            qr_forwarded_by = "s";
            qr_forwarded_by_str = "Support";
        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
            qr_forwarded_by = "m";
            qr_forwarded_by_str = "Admin";
        } else if (role.equals(Constants.ROLE_USER)) {
            qr_forwarded_by = "u";
            qr_forwarded_by_str = "Applicant";
        }

        // person should not be ablet o raise query to himself, and check added by pr on 22ndoct19        
        if (dropDownForRaiseQuery.contains(choose_recp) && !qr_forwarded_by.equals(choose_recp)) {
            ArrayList<String> allowedRegNumbers = (ArrayList) sessionMap.get("regNumberAllowed");  // audit by nikki
            //if (regNo.matches(regNoRegEx)) {
            String reg_no = "", form_name = "";
            String[] arr = regNo.split("~");
            if (arr != null && arr.length == 3) {
                reg_no = arr[0];
                form_name = arr[1];
            }
            //if (regNo.matches(regNoRegEx)) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reg no value is:::: " + regNo + " form_name::::  " + form_name);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " statRemarks is:::: " + statRemarks + " statRemarksRegEx:::  " + statRemarksRegEx);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " CSRFRandom is:::: " + CSRFRandom);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " SessionCSRFRandom:::  " + SessionCSRFRandom);
            // update the query_raise table and send sms mobile to the respective recipient  
            if (statRemarks.matches(statRemarksRegEx)) {
                if (CSRFRandom.equals(SessionCSRFRandom)) {

                    System.out.println("admin.ForwardAction.raiseQuery() --------------------?  " + allowedRegNumbers.contains(reg_no));

                    if (!reg_no.equals("") && !form_name.equals("") && allowedRegNumbers.contains(reg_no)) {  //audit by nikki

                        // if (!reg_no.equals("") && !form_name.equals("") && statRemarks.matches(statRemarksRegEx)) {
                        String qr_form_type = "", qr_reg_no = "", qr_forwarded_by_user = "", qr_forwarded_to = "", qr_forwarded_to_user = "",
                                qr_message = "", status = "";

                        PreparedStatement ps = null;
                        //Connection con = null;

                        String email = fetchSessionEmail();
                        try {
                            qr_form_type = form_name;
                            qr_reg_no = reg_no;

                            qr_forwarded_by_user = fetchSessionEmail();
                            qr_forwarded_to = choose_recp;
                            String mobile = "";
                            ArrayList arrHashMap = null; // get values of emails and mobile from fetchAdminEmail function
                            if (choose_recp.equals("u")) {
                                HashMap hm = fetchFormDetail(reg_no, form_name);
                                if (hm != null && hm.get("email") != null && !hm.get("email").equals("")) {
                                    qr_forwarded_to_user = hm.get("email").toString();
                                }
                                if (hm != null && hm.get("mobile") != null && !hm.get("mobile").equals("")) {
                                    mobile = hm.get("mobile").toString();
                                }
                            } else {
                                arrHashMap = fetchAdminEmail(reg_no, choose_recp);    // its an array list of hashmap with mobile and mobile keys
                                // extract from the above arraylist mobile/s and mobile/s , to save in the db and send intimation to
                                // get the comma separated emails
                                ArrayList<String> temp = new ArrayList<String>(); // to make comma separated value to save in the db table
                                for (Object h : arrHashMap) {
                                    HashMap hm = (HashMap) h;
                                    for (Object k : hm.keySet()) {
                                        String em = hm.get("email").toString();
                                        if (!em.equals("")) {
                                            if (!temp.contains(em)) {
                                                temp.add(em);
                                            }
                                        }
                                        String mob = hm.get("mobile").toString();
                                    }
                                }
                                if (temp != null && temp.size() > 0) {
                                    int i = 1;
                                    for (String t : temp) {
                                        if (i == 1) {
                                            qr_forwarded_to_user = t;
                                        } else {
                                            qr_forwarded_to_user += "," + t;
                                        }
                                        i++;
                                    }
                                }
                            }
                            qr_message = statRemarks;
                            status = fetchStatusOfQuery(email, reg_no, qr_forwarded_to_user, qr_forwarded_by, qr_forwarded_to);

                            if (!status.isEmpty()) {
                                con = DbConnection.getConnection();
                                String qry = "";

                                qry = " INSERT INTO query_raise SET qr_form_type = ?, qr_reg_no = ?, qr_forwarded_by = ?, qr_forwarded_by_user = ?, "
                                        + ""
                                        + "qr_forwarded_to = ?, qr_forwarded_to_user = ?, qr_message = ?, status = ? ";

                                ps = con.prepareStatement(qry);
                                ps.setString(1, qr_form_type);
                                ps.setString(2, qr_reg_no);
                                ps.setString(3, qr_forwarded_by);
                                ps.setString(4, qr_forwarded_by_user);
                                ps.setString(5, qr_forwarded_to);
                                ps.setString(6, qr_forwarded_to_user);
                                ps.setString(7, qr_message);
                                ps.setString(8, status.toLowerCase());

                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " raiseQuery insert query in query raise  is " + ps);
                                //int rs = ps.executeUpdate();
                                int i = ps.executeUpdate();
                                if (i > 0) {
                                    // send mobile and sms to the recipient
                                    Inform obj = new Inform();
                                    String by = qr_forwarded_by_str + "(" + qr_forwarded_by_user + ")";
                                    if (arrHashMap != null && arrHashMap.size() > 0) {
                                        for (Object h : arrHashMap) {
                                            HashMap hm = (HashMap) h;
                                            String em = "", mob = "";
                                            for (Object k : hm.keySet()) {
                                                em = hm.get("email").toString();
                                                mob = hm.get("mobile").toString();
                                            }
                                            obj.sendQueryIntimation(qr_forwarded_to, em, by, qr_message, mob, qr_reg_no); // one by one send to all emails and mobiles
                                        }
                                    } else if (!qr_forwarded_to_user.equals("")) {
                                        obj.sendQueryIntimation(qr_forwarded_to, qr_forwarded_to_user, by, qr_message, mobile, qr_reg_no); // one by one send to all emails and mobiles
                                    }
                                    isSuccess = true;
                                    isError = false;
                                    if (status.equalsIgnoreCase("raised")) {
                                        msg = "Query Raised Successfully";
                                        if (qr_form_type.contains("vpn")) {
                                            if ((role.equals(Constants.ROLE_SUP) || role.equals(Constants.ROLE_MAILADMIN))) {
                                                String onHoldBy = "";
                                                if (role.equals(Constants.ROLE_SUP)) {
                                                    onHoldBy = "vpn_support";
                                                } else {
                                                    onHoldBy = "vpn_admin";
                                                }
                                                if (choose_recp.equalsIgnoreCase("u") || choose_recp.equalsIgnoreCase("ca") || choose_recp.equalsIgnoreCase("co")) {
                                                    if (putOnHold_And_OffHold_For_Vpn(true, "As query has been raised, your request has been put on hold. To off hold it, Kindly respond to the query immediately.", role, email, onHoldBy, qr_reg_no)) {
                                                        //msg = "Query Raised Successfully";

                                                    } else {
                                                        putOnHold_And_OffHold_For_Vpn(false, "As query has been raised, " + qr_reg_no + " has been put off hold.", role, email, onHoldBy, qr_reg_no);
                                                    }
                                                }
                                            } else {
                                                putOnHold_And_OffHold_For_Vpn(false, "As query has been raised, " + qr_reg_no + " has been put off hold.", role, email, qr_forwarded_by_str, qr_reg_no);
                                            }
                                        }
                                    } else {
                                        msg = "Query Responded Successfully";
                                        if (qr_form_type.contains("vpn") && (!role.equals(Constants.ROLE_SUP) && !role.equals(Constants.ROLE_MAILADMIN))) {
                                            qry = "SELECT on_hold_by FROM final_audit_track WHERE registration_no = ? and on_hold=?";
                                            ps = conSlave.prepareStatement(qry);
                                            ps.setString(1, reg_no);
                                            ps.setString(2, "y");
                                            System.out.println("ON_HOLD FETCH QUERY ::" + ps);
                                            ResultSet res = ps.executeQuery();
                                            String onHoldBy = "";
                                            boolean flag = false;
                                            while (res.next()) {
                                                flag = true;
                                                onHoldBy = res.getString("on_hold_by");
                                            }
                                            if (flag) {
                                                if (qr_forwarded_to.equalsIgnoreCase("s") && onHoldBy.equalsIgnoreCase("vpn_support")) {
                                                    if (putOnHold_And_OffHold_For_Vpn(false, "Your request has been put off hold as you have responded to the query.", role, email, "", qr_reg_no)) {
                                                        //msg = "Query Responded Successfully";
                                                    }
                                                } else if (qr_forwarded_to.equalsIgnoreCase("m") && onHoldBy.equalsIgnoreCase("vpn_admin")) {
                                                    if (putOnHold_And_OffHold_For_Vpn(false, "Your request has been put off hold as you have responded to the query.", role, email, "", qr_reg_no)) {
                                                        //msg = "Query Responded Successfully";
                                                    }
                                                } else {
                                                    putOnHold_And_OffHold_For_Vpn(false, "Your request has been put off hold", role, email, qr_forwarded_by_str, qr_reg_no);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    isSuccess = false;
                                    isError = true;
                                    if (status.equalsIgnoreCase("raised")) {
                                        msg = "Query could not be Raised.";
                                    } else {
                                        msg = "Query could not be Responded.";
                                    }
                                }
                            } else {
                                isSuccess = false;
                                isError = true;
                                msg = "Query could not be Raised/Responded.";
                            }
                        } catch (Exception e) {
                            // above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 255" + e.getMessage());
                        } finally {
                            if (ps != null) {
                                try {
                                    ps.close();
                                } catch (Exception e) {
                                    // above line commented below added by pr on 23rdapr19                    
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 256" + e.getMessage());
                                }
                            }
                            if (con != null) {
                                try {
                                    // con.close();
                                } catch (Exception e) {
                                    // above line commented below added by pr on 23rdapr19                    
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 257" + e.getMessage());
                                }
                            }
                        }
                    } else {
                        isSuccess = false;
                        isError = true;
                        //msg = "Invalid Registration No. or Form Name or Message ";
                        msg = "This request is not pending with you.";
                    }
                } else {
                    isSuccess = false;
                    isError = true;
                    //msg = "Invalid Registration No. or Form Name or Message ";
                    msg = "Invalid security token";
                }
            } else {
                isSuccess = false;
                isError = true;
                //msg = "Invalid Registration No. or Form Name or Message ";
                msg = "Please enter valid allowed characters in the Message. " + Constants.STAT_REMARKS_TEXT; // above message modified by pr on 18thjul8, constant appended by pr on 28thdec18
            }
//        } else {
//            isSuccess = false;
//
//            isError = true;
//
//            msg = "Invalid Registration No.";
//        }
        } else {
            isSuccess = false;
            isError = true;
            msg = "Query can not be raised/responded to " + recipient;
        }
        return SUCCESS;
    }

    // added on 26 july by ram partap
    private boolean putOnHold_And_OffHold_For_Vpn(boolean type, String remarks, String role, String email, String onHoldBy, String regNo) {
        PreparedStatement ps = null, psSelect = null;
        ResultSet rs = null;
        String on_hold = "", message = "";
        String queryParamForOnHold = "n";
        boolean result = false;
        if (type) {
            on_hold = "y";
            message = "On";
            queryParamForOnHold = "y";
        } else {
            on_hold = "n";
            message = "Off";
        }

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select * from final_audit_track where on_hold = ? and registration_no = ?";
            psSelect = conSlave.prepareStatement(qry);
            psSelect.setString(1, queryParamForOnHold);
            psSelect.setString(2, regNo);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function  select final audit track query is " + ps);
            rs = psSelect.executeQuery();
            if (rs.next()) {
                return true;
            }

            qry = " UPDATE status SET stat_on_hold = ? WHERE stat_reg_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, on_hold);
            ps.setString(2, regNo);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function  update status query is " + ps);
            int res = ps.executeUpdate();

            qry = " UPDATE final_audit_track SET on_hold = ?, hold_remarks = ?, on_hold_by=? WHERE registration_no = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, on_hold);
            ps.setString(2, remarks); // line added by pr on 23rdaug19
            ps.setString(3, onHoldBy);
            ps.setString(4, regNo);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function  update final_audit_track query is " + ps);
            int res1 = ps.executeUpdate();

            if (res > 0 && res1 > 0) {
                Inform obj = new Inform();
                obj.sendHoldNotification(regNo, role, email, " has been put " + message + " Hold ", remarks);
                result = true;
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside putOnHold function exception catch " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function Replace printstcaktrace EXCEPTION 6 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception ex) {
                    Logger.getLogger(ForwardAction.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return result;
    }

    public String fetchStatusOfQuery(String raiserEmail, String reg_no, String raiseeEmail, String byRole, String toRole) {    // added by AT June-2021
        PreparedStatement ps = null;
        ResultSet res = null;
        int raisedCount = 0, respondedCount = 0;
        Set<String> raiserAliases = new HashSet<>();
        Set<String> raiseeAliases = new HashSet<>();
        String status = "raised";
        if (ldap.emailValidate(raiserEmail)) {
            raiserAliases = ldap.fetchAliases(raiserEmail);
        } else {
            raiserAliases.add(raiserEmail);
        }
        String[] raiseeEmailSplitted = null;
        if (raiseeEmail.contains(",")) {
            raiseeEmailSplitted = raiseeEmail.split(",");
        } else {
            raiseeEmailSplitted = new String[1];
            raiseeEmailSplitted[0] = raiseeEmail;
        }

        for (String recipientEmail : raiseeEmailSplitted) {
            if (ldap.emailValidate(recipientEmail)) {
                raiseeAliases = ldap.fetchAliases(recipientEmail);
            } else {
                raiseeAliases.add(recipientEmail);
            }
        }

        try {
            String qry = "";
            conSlave = DbConnection.getSlaveConnection();
            for (String raiser : raiserAliases) {
                for (String raisee : raiseeAliases) {
                    qry = "SELECT * FROM query_raise WHERE qr_reg_no = ? and qr_forwarded_by_user=? and qr_forwarded_by = ? and qr_forwarded_to = ? and status=?";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, reg_no);
                    ps.setString(2, raisee);
                    ps.setString(3, toRole);
                    ps.setString(4, byRole);
                    ps.setString(5, status);
                    System.out.println("ps 1::" + ps);
                    res = ps.executeQuery();

                    while (res.next()) {
                        String toEmails = res.getString("qr_forwarded_to_user");
                        String[] toEmailsInArray = toEmails.split(",");
                        for (String to : toEmailsInArray) {
                            if (to.equalsIgnoreCase(raiser)) {
                                raisedCount += 1;
                            }
                        }
                    }
                    qry = "SELECT * FROM query_raise WHERE qr_reg_no = ? and qr_forwarded_by_user=? and qr_forwarded_by = ? and qr_forwarded_to = ? and status=?";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, reg_no);
                    ps.setString(2, raiser);
                    ps.setString(3, byRole);
                    ps.setString(4, toRole);
                    ps.setString(5, "responded");
                    System.out.println("ps 2::" + ps);
                    res = ps.executeQuery();

                    while (res.next()) {
                        String toEmails = res.getString("qr_forwarded_to_user");
                        String[] toEmailsInArray = toEmails.split(",");
                        for (String to : toEmailsInArray) {
                            if (to.equalsIgnoreCase(raisee)) {
                                respondedCount += 1;
                            }
                        }
                    }
                }
            }
            if ((raisedCount - respondedCount) > 0) {
                status = "responded";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception is:" + e);
            status = "";
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 256" + e.getMessage());
                    status = "";
                }
            }
        }
        return status;
    }

    // below function added by pr on 9thapr18
    // 0 -> cn (name) 1 -> mobile 2 -> boolean value whether mobile exists in LDAP or not 3 -> cn
    public ArrayList fetchLDAPName(String email) {
        ArrayList<String> data = new ArrayList<String>();
        //ServletContext sctx = getServletContext();
        /*String host = sctx.getInitParameter("host");
         String port = sctx.getInitParameter("port");
         String dn = sctx.getInitParameter("binddn");
         String dn_pass = sctx.getInitParameter("bindpass");*/
//        Hashtable ht = new Hashtable(4);
//        ht.put(Context.INITIAL_CONTEXT_FACTORY, ctxfactory);
//        ht.put(Context.PROVIDER_URL, providerurlSlave);
//        ht.put(Context.SECURITY_AUTHENTICATION, "simple");
//        ht.put(Context.SECURITY_PRINCIPAL, createdn);
//        ht.put(Context.SECURITY_CREDENTIALS, createpass);
        DirContext ctx = null;
        try {
            //ctx = new InitialDirContext(ht);

            ctx = SingleApplicationContextSlave.getLDAPInstance();// line modified by pr on 19thfeb2020

            //String[] IDs = {"uid", "mail", "mailequivalentaddress", "cn", "mobile"};
            String[] IDs = {"uid", "mobile", "cn"};
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
                String mobile_str = t.substring(t.indexOf("mobile:") + 8);
                String mobile = mobile_str.substring(0);
                if (mobile_str.indexOf(",") >= 0) {
                    mobile = mobile_str.substring(0, mobile_str.indexOf(","));
                }
                String cn_str = t.substring(t.indexOf("cn:") + 4);
                String cn = cn_str.substring(0);
                if (cn_str.indexOf(",") >= 0) {
                    cn = cn_str.substring(0, cn_str.indexOf(","));
                }
                String uid_str = t.substring(t.indexOf("uid:") + 5);
                String uid = uid_str.substring(0);
                if (uid_str.indexOf(",") >= 0) {
                    uid = uid_str.substring(0, uid_str.indexOf(","));
                }
                //data.add(cn); // oth position
                data.add(email); // oth position
                data.add(mobile); // 1st position
                data.add("true");
                data.add(cn); // line added by pr on 8thjan19
            } else {
                data.add(email); // oth position
                data.add(""); // 1st position
                data.add("false");
                data.add(""); // line added by pr on 8thjan19
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + e.getMessage());
        } finally // finally added by pr on 16thoct18
        {
//            if (ctx != null) {
//                try {
//                    ctx.close();
//                } catch (Exception e) {
//                    // above line commented below added by pr on 23rdapr19                    
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 258" + e.getMessage());
//                }
//            }
        }
        return data;
    }

    // below function added by pr on 9thapr18
    public ArrayList fetchAdminEmail(String reg_no, String role) {
        ArrayList<HashMap> arr = new ArrayList<HashMap>();
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getadmin email func ");
        String email = "";
        String mobile = "";
        String forwarded_to = "", forwarded_to_user = "", stat_type = "";
        if (role.equals("ca")) {
            stat_type = "ca_pending";
            forwarded_to = "ca";
        } else if (role.equals("s")) {
            stat_type = "support_pending";
            forwarded_to = "s";
        } else if (role.equals("c")) {
            stat_type = "coordinator_pending";
            forwarded_to = "c";
        } else if (role.equals("m")) {
            stat_type = "mail-admin_pending";
            forwarded_to = "m";
        } else if (role.equals("d")) {
            stat_type = "da_pending";
            forwarded_to = "d";
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "  SELECT stat_type,stat_forwarded_to,stat_forwarded_to_user,stat_reg_no FROM status WHERE stat_reg_no = ? and stat_type = ? AND stat_forwarded_to = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            ps.setString(2, stat_type);
            ps.setString(3, forwarded_to);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "  fetchAdminEmail insert query in query raise  is " + ps);
            //int rs = ps.executeUpdate();
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("stat_type").equals("ca_pending")) // only one mobile and mobile will be there
                {
                    //email = fetchCAEmail(rs.getString("stat_forwarded_to_user"));

                    email = rs.getString("stat_forwarded_to_user"); // above line modified by pr on 27thjun19

                    mobile = fetchCAMobile(rs.getString("stat_forwarded_to_user"));
                    HashMap<String, String> hm = new HashMap<String, String>();
                    hm.put("email", email);
                    hm.put("mobile", mobile);
                    arr.add(hm);
                } else {
                    String emails = rs.getString("stat_forwarded_to_user"); // this can be comma separated too                  
                    if (!emails.equals("") && emails.contains(",")) // there can be two cases here one is after this role action has been performed in that case we will get one mobile who performed the next action
                    {
                        // check if further action has been performed by the stat_forwarded_to as stat_forwarded_by , if yes then use that mobile else use all
                        email = fetchOneMail(rs.getString("stat_reg_no"), rs.getString("stat_forwarded_to"));
                        if (!email.equals("")) // means action has been performed after this
                        {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("email", email);
                            ArrayList arrMobile = fetchLDAPName(email);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside get mobile from ldap size of arr list is " + arrMobile.size());
                            if (arrMobile.size() >= 2 && arrMobile.get(1) != null) {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside arr mobile size hreater than 0 equal to 2 ");
                                mobile = arrMobile.get(1).toString();
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " mobile is " + mobile);
                            }
                            Validation validation = new Validation();
                            if (mobile == null || mobile.isEmpty()) {
                                mobile = "";
                            }
                            if (!validation.checkFormat("mobile", mobile)) {
                                mobile = "";
                            }
                            if (mobile.startsWith("+")) {
                                mobile = mobile.substring(1);
                            }
                            hm.put("mobile", mobile);
                            arr.add(hm);
                        } else // use all comma separated and put in hashmap
                        {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside else emails value is " + emails);
                            String[] commaArr = emails.split(",");
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " length of commaarr is " + commaArr.length);
                            for (String e : commaArr) {
                                email = e.trim();
                                HashMap<String, String> hm = new HashMap<String, String>();
                                hm.put("email", email);
                                ArrayList arrMobile = fetchLDAPName(email);
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside get mobile from ldap size of arr list is " + arrMobile.size());
                                if (arrMobile.size() >= 2 && arrMobile.get(1) != null) {
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside arr mobile size hreater than 0 equal to 2 ");
                                    mobile = arrMobile.get(1).toString();
                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " mobile is " + mobile);
                                }
                                Validation validation = new Validation();
                                if (mobile == null || mobile.isEmpty()) {
                                    mobile = "";
                                }
                                if (!validation.checkFormat("mobile", mobile)) {
                                    mobile = "";
                                }
                                if (mobile.startsWith("+")) {
                                    mobile = mobile.substring(1);
                                }

                                hm.put("mobile", mobile);
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside commarr for loop email is " + email + " mobile is " + mobile + " before adding into arraylist ");
                                arr.add(hm);
                            }
                        }
                    } else {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        email = emails;
                        hm.put("email", email);
                        ArrayList arrMobile = fetchLDAPName(email);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside get mobile from ldap size of arr list is " + arrMobile.size());
                        if (arrMobile.size() >= 2 && arrMobile.get(1) != null) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside arr mobile size hreater than 0 equal to 2 ");
                            mobile = arrMobile.get(1).toString();
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " mobile is " + mobile);
                        }
                        Validation validation = new Validation();
                        if (mobile == null || mobile.isEmpty()) {
                            mobile = "";
                        }
                        if (!validation.checkFormat("mobile", mobile)) {
                            mobile = "";
                        }
                        if (mobile.startsWith("+")) {
                            mobile = mobile.substring(1);
                        }
                        hm.put("mobile", mobile);
                        arr.add(hm);
                    }
                }
            }
        } catch (Exception e) {
            // above line commented below added by pr on 23rdapr19                    
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 259" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 260" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 261" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 262" + e.getMessage());
                }
            }
        }
        for (Object h : arr) {
            HashMap hm = (HashMap) h;
            for (Object k : hm.keySet()) {
                String em = hm.get("email").toString();
                String mob = hm.get("mobile").toString();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside admin email emails evaluated are email is " + em + " mobile is " + mob);
            }
        }
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside getadmin email func reg_no " + reg_no + " role is " + role + " email is " + email + " size of arraylist to be returned is  " + arr.size());
        return arr;
    }

    // below function added by pr on 9thapr18
    public String fetchOneMail(String reg_no, String by) {
        String email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "  SELECT stat_forwarded_by_user FROM status WHERE stat_reg_no = ? and stat_forwarded_by = ?  ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            ps.setString(2, by);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "  fetchOneMail insert query in query raise  is " + ps);
            //int rs = ps.executeUpdate();
            rs = ps.executeQuery();
            while (rs.next()) {
                email = rs.getString("stat_forwarded_by_user");
            }
        } catch (Exception e) {
            // above line commented below added by pr on 23rdapr19                    
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 263" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 264" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 265" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 266" + e.getMessage());
                }
            }
        }
        return email;
    }

    // below function added by pr on 9thapr18
    public String fetchCAMobile(String ca_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String mobile = "";
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            //String qry = "  SELECT ca_mobile FROM comp_auth WHERE ca_id = ? ";

            String qry = "  SELECT ca_mobile FROM comp_auth WHERE ca_email = ? "; // line modified by pr on 27thjun19

            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ca_id);
            //System.out.println(" get ca mobile query is "+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                mobile = rs.getString("ca_mobile");
                if (mobile == null || mobile.isEmpty()) {
                    return "";
                }
                Validation validation = new Validation();
                if (!validation.checkFormat("mobile", mobile)) {
                    return "";
                }
                if (mobile.startsWith("+")) {
                    mobile = mobile.substring(1);
                }
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "  get status reg no search results fetchCAEmail " + e.getMessage());
            // above line commented below added by pr on 23rdapr19                    
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 271" + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 272" + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 273" + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 274" + e.getMessage());
                }
            }
        }
        return mobile;
    }

    // below function added by pr on 10thapr18
    public void fetchRecps() {

//        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();
//        
//        if(!CSRFRandom.equals(SessionCSRFRandom)) {
//            hmTrack.put("csrf_error", "CSRF Token is invalid.");
//            return;
//        }
        try {
            Map<String, Object> details = new HashMap<String, Object>();
            String json = null;
            // System.out.println("hi");
            String role = fetchSessionRole();
            querydata.clear();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchRecps func ");
            recpdata.clear();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " regNo value is " + regNo);
            String reg_no = "";
            if (regNo.matches(regNoRegEx)) {
                // arr[0] : Registration Number , arr[1] = Form Type , arr[2] = query_raise 
                String[] arr = regNo.split("~");
                if (arr != null && arr.length == 3) {
                    reg_no = arr[0];
                }
            }
            PreparedStatement ps = null;
            ResultSet rs = null;
            if (!reg_no.equals("")) {
                // fill the recpdata arraylist
                //Connection con = null;
                raiseQueryBtn = true;
                recpdata.addAll(fetchStakeHolders(fetchSessionRole(), reg_no));

                // get the hostory related to this reg no
                try {
                    //con = DbConnection.getConnection();
                    conSlave = DbConnection.getSlaveConnection();
                    String qry = "  SELECT qr_id,qr_form_type,qr_reg_no,qr_forwarded_by,qr_forwarded_by_user,qr_forwarded_to,qr_forwarded_to_user,qr_message,qr_createdon FROM query_raise WHERE qr_reg_no = ? ORDER BY qr_createdon DESC ";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, reg_no);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside fetchRecps func   select query from query_raise is " + ps);
                    //int rs = ps.executeUpdate();
                    rs = ps.executeQuery();
                    String stat_type = "";
                    UserData userdata = (UserData) sessionMap.get("uservalues");
                    while (rs.next()) {
                        boolean account_holder = false;
                        if (rs.getString("qr_forwarded_by_user").equals(userdata.getEmail())) {
                            account_holder = true;
                        }
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: "+  ip + " timestamp: == "+new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) +" == "+" inside while loop in query raise fetchRecps func ");
                        Forms frmObj = new Forms();
                        frmObj.setQr_id(rs.getString("qr_id"));
                        frmObj.setQr_form_type(rs.getString("qr_form_type"));
                        frmObj.setQr_reg_no(rs.getString("qr_reg_no"));
                        frmObj.setQr_forwarded_by(rs.getString("qr_forwarded_by"));
                        frmObj.setQr_forwarded_by_user(rs.getString("qr_forwarded_by_user"));
                        frmObj.setQr_forwarded_to(rs.getString("qr_forwarded_to"));
                        frmObj.setQr_forwarded_to_user(rs.getString("qr_forwarded_to_user"));
                        frmObj.setQr_message(rs.getString("qr_message"));
                        frmObj.setQr_createdon(rs.getString("qr_createdon"));
                        frmObj.setQr_accountholder(account_holder);
                        querydata.add(frmObj);
                        details.put("querydata", querydata);
                    }
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 278" + e.getMessage());
                } finally {
                    if (ps != null) {
                        try {
                            ps.close();
                        } catch (Exception e) {
                            // above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 279" + e.getMessage());
                        }
                    }
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Exception e) {
                            // above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 280" + e.getMessage());
                        }
                    }
                    if (con != null) {
                        try {
                            // con.close();
                        } catch (Exception e) {
                            // above line commented below added by pr on 23rdapr19                    
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 281" + e.getMessage());
                        }
                    }
                }
            }
            System.out.println("querydata:::::::::::" + reg_no);
            System.out.println("querydata:::::::::::" + querydata);
            details.put("raiseQueryBtn", raiseQueryBtn);
            sessionMap.put("raiseQueryDropDownList", recpdata);
            details.put("recpdata", recpdata);
            details.put("querydata", querydata);
            details.put("statusdata", statusdata);
            details.put("fetchSessionEmail", fetchSessionEmail());
            details.put("role", role);
            json = new Gson().toJson(details);
            //System.out.println("json::::::::" + json);
            ServletActionContext.getResponse().setContentType("application/json");
            ServletActionContext.getResponse().getWriter().write(json);
//
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " querydata size is " + querydata.size());
//return SUCCESS;

        } catch (Exception ex) {
            Logger.getLogger(ForwardAction.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean fetchHimachalCoord(String dept) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList admins = new ArrayList();
        try {
            conSlave = DbConnection.getSlaveConnection();

            String sql_comp_qry = "select emp_coord_email,emp_coord_name from employment_coordinator where emp_coord_email!='kaushal.shailender@nic.in' and emp_admin_email='kaushal.shailender@nic.in' and emp_dept=?";
            stmt = conSlave.prepareStatement(sql_comp_qry);
            stmt.setString(1, dept);
            // System.out.println("stattment query in get coord:::::::" + stmt);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // below function added by pr on 6thaug19 , function modified by pr on 23rdaug19   
    public String putOnHold() {

        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();  // audit by sahil
        System.out.println("CHECKINGGG:::::" + "SessionCSRFRandom:- " + SessionCSRFRandom + "ANDDD : " + "CSRFRandom ::--" + CSRFRandom);
        if (!CSRFRandom.equals(SessionCSRFRandom)) {
            hmTrack.put("csrf_error", "CSRF Token is invalid.");
            System.out.println("invalid csrffff");
            return SUCCESS;
        }

        if ((type != null && statRemarks != null && !type.equals("") && type.equalsIgnoreCase("true") || type.equalsIgnoreCase("false"))
                && (statRemarks.equals("") || (!statRemarks.equals("") && statRemarks.matches(statRemarksRegEx)))) {

            Boolean flag = false;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + " inside putOnHold function regNo value is " + regNo + " type value is " + type + " statRemarks value is " + statRemarks);

            PreparedStatement ps = null;

            String on_hold = "", message = "";

            if (type.equalsIgnoreCase("true")) {
                on_hold = "y";

                message = "On";
            } else {
                on_hold = "n";

                message = "Off";
            }

            try {
                con = DbConnection.getConnection();

                String qry = " UPDATE status SET stat_on_hold = ? WHERE stat_reg_no = ? ";
                ps = con.prepareStatement(qry);

                ps.setString(1, on_hold);

                ps.setString(2, regNo);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function  update status query is " + ps);

                int res = ps.executeUpdate();

                qry = " UPDATE final_audit_track SET on_hold = ?, hold_remarks = ? WHERE registration_no = ? "; // query modified by pr on 23rdaug19

                ps = con.prepareStatement(qry);

                ps.setString(1, on_hold);

                ps.setString(2, statRemarks); // line added by pr on 23rdaug19

                ps.setString(3, regNo);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function  update final_audit_track query is " + ps);

                int res1 = ps.executeUpdate();

                if (res > 0 && res1 > 0) {

                    flag = true;

                    isSuccess = true;
                    isError = false;
                    msg = "Application (" + regNo + ") put " + message + " Hold successfully";

                    // start, code added by pr on 28thaug19
                    // send mail and sms to applicant and back heirarchy
                    String email = fetchSessionEmail();

                    String role = fetchSessionRole();

                    Inform obj = new Inform();

                    obj.sendHoldNotification(regNo, role, email, " has been put " + message + " Hold ", statRemarks);

                    // end, code added by pr on 28thaug19
                } else {
                    isSuccess = false;
                    isError = true;
                    msg = "Application (" + regNo + ") could not be put " + message + " Hold";
                }

            } catch (Exception e) {

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside putOnHold function exception catch " + e.getMessage());

            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        // above line commented below added by pr on 23rdapr19                    
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside putOnHold function Replace printstcaktrace EXCEPTION 6 " + e.getMessage());

                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception ex) {
                        Logger.getLogger(ForwardAction.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } else {
            isSuccess = false;
            isError = true;
            msg = "Application (" + regNo + ") could not be processed.Invalid Data.";
        }

        return SUCCESS;
    }

    // below function added  by pr on 23rdaug19
    public String fetchRemarks() {

        // put the value of remarks in msg variable
        //System.out.println(" inside fetchRemarks function reg_no value is " + regNo);
        PreparedStatement ps = null;

        ResultSet rs = null;

        String email = "";
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = " SELECT hold_remarks FROM final_audit_track WHERE registration_no = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchRemarks query is " + ps);

            rs = ps.executeQuery();

            while (rs.next()) {

                statRemarks = rs.getString("hold_remarks");

            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchRemarks function msg is " + statRemarks);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchRemarks catch " + e.toString());
            //e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchRemarks EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchRemarks EXCEPTION 2 " + e.getMessage());

                }
            }

        }

        return SUCCESS;
    }

    public boolean chkValidCoord(String coordEmail) {

        // put the value of remarks in msg variable
        //System.out.println(" inside fetchRemarks function reg_no value is " + regNo);
        PreparedStatement ps = null;

        ResultSet rs = null;
        boolean type = false;
        String emp_type = "";
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = " SELECT emp_type FROM employment_coordinator WHERE emp_coord_email = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, coordEmail);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " chkValidCoord query is " + ps);

            rs = ps.executeQuery();
            while (rs.next()) {
                emp_type = rs.getString("emp_type");
                if (emp_type.contains("c") || emp_type.contains("dc")) {
                    type = true;
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " chkValidCoord emp_type " + emp_type);
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  chkValidCoord catch " + e.toString());
            //e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside chkValidCoord EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside chkValidCoord EXCEPTION 2 " + e.getMessage());

                }
            }

        }

        return type;
    }
//    private void identifyDnsRequestAndUpdateDns_team_data(String stat_form_type, String stat_reg_no) {
//        PreparedStatement ps = null;
//        ResultSet rs = null; 
//        String req_for = "";
//        List<String> fetchUrls = new ArrayList<>();
//        try {
//            conSlave = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
//            String query = "SELECT req_for FROM dns_registration  WHERE registration_no =? ORDER BY id DESC";
//            ps = conSlave.prepareStatement(query);
//            ps.setString(1, stat_reg_no);
//            rs = ps.executeQuery();
//            if(rs.next()) {
//                req_for = rs.getString("req_for");
//            }
//            if(req_for.equals("req_delete")) {
//                ps = null;
//                rs = null;
//                String qry = "SELECT dns_url FROM dns_registration_url WHERE registration_no =?";
//                ps = conSlave.prepareStatement(qry);
//                ps.setString(1, stat_reg_no);
//                rs = ps.executeQuery();
//                while(rs.next()) {
//                    fetchUrls.add(rs.getString("dns_url"));
//                }
//                for (String url : fetchUrls) {
//                    ps = null;
//                    qry = "UPDATE `dns_team_data` SET `status` = '1' WHERE domain = ?";
//                    ps = con.prepareStatement(qry);
//                    ps.setString(1, url);
//                    ps.executeUpdate();
//                }
//            } 
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//         
//        
//    }

    private List<String> fetchStakeHolders(String roleInSession, String regNo) {
        Set<String> recpdata = new HashSet<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (!regNo.isEmpty()) {
            try {
                conSlave = DbConnection.getSlaveConnection();
                String qry = "SELECT status, ca_email, coordinator_email,support_email, admin_email FROM final_audit_track WHERE registration_no = ?";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, regNo);
                rs = ps.executeQuery();
                recpdata.add("u");
                if (rs.next()) {
                    switch (rs.getString("status").toLowerCase()) {
                        case "ca_pending":
                            recpdata.add("ca");
                            break;
                        case "support_pending":
                            if (rs.getString("ca_email") != null && !rs.getString("ca_email").isEmpty()) {
                                recpdata.add("ca");
                            }
                            recpdata.add("s");
                            break;
                        case "coordinator_pending":
                            if ((rs.getString("ca_email") != null && !rs.getString("ca_email").isEmpty()) && (rs.getString("support_email") != null && !rs.getString("support_email").isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("s");
                            } else if ((rs.getString("ca_email") == null || rs.getString("ca_email").isEmpty()) && (rs.getString("support_email") != null && !rs.getString("support_email").isEmpty())) {
                                recpdata.add("s");
                            } else if ((rs.getString("ca_email") != null && !rs.getString("ca_email").isEmpty()) && (rs.getString("support_email") == null || rs.getString("support_email").isEmpty())) {
                                recpdata.add("ca");
                            }
                            recpdata.add("c");
                            break;
                        case "mail-admin_pending":
                            String caEmail = rs.getString("ca_email");
                            String supEmail = rs.getString("support_email");
                            String coordEmail = rs.getString("coordinator_email");
                            if ((caEmail != null && !caEmail.isEmpty()) && (supEmail != null && !supEmail.isEmpty()) && (coordEmail != null && !coordEmail.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("s");
                                recpdata.add("c");
                            } else if ((caEmail == null || caEmail.isEmpty()) && (supEmail != null && !supEmail.isEmpty()) && (coordEmail != null && !coordEmail.isEmpty())) {
                                recpdata.add("s");
                                recpdata.add("c");
                            } else if ((caEmail != null && !caEmail.isEmpty()) && (supEmail == null || supEmail.isEmpty()) && (coordEmail != null && !coordEmail.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("c");
                            } else if ((caEmail != null && !caEmail.isEmpty()) && (supEmail != null && !supEmail.isEmpty()) && (coordEmail == null || coordEmail.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("s");
                            } else if ((caEmail == null || caEmail.isEmpty()) && (supEmail == null || supEmail.isEmpty()) && (coordEmail != null && !coordEmail.isEmpty())) {
                                recpdata.add("c");
                            } else if ((caEmail == null || caEmail.isEmpty()) && (supEmail != null && !supEmail.isEmpty()) && (coordEmail == null || coordEmail.isEmpty())) {
                                recpdata.add("s");
                            } else if ((caEmail != null && !caEmail.isEmpty()) && (supEmail == null || supEmail.isEmpty()) && (coordEmail == null || coordEmail.isEmpty())) {
                                recpdata.add("ca");
                            }
                            recpdata.add("m");
                            break;
                        case "da_pending":
                        case "completed":
                            String caEmail1 = rs.getString("ca_email");
                            String supEmail1 = rs.getString("support_email");
                            String coordEmail1 = rs.getString("coordinator_email");
                            String adminEmail1 = rs.getString("admin_email");
                            if ((caEmail1 != null && !caEmail1.isEmpty()) && (supEmail1 != null && !supEmail1.isEmpty()) && (coordEmail1 != null && !coordEmail1.isEmpty()) && (adminEmail1 != null && !adminEmail1.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("s");
                                recpdata.add("c");
                                recpdata.add("m");
                            } else if ((caEmail1 != null && !caEmail1.isEmpty()) && (supEmail1 != null && !supEmail1.isEmpty()) && (coordEmail1 != null && !coordEmail1.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("s");
                                recpdata.add("c");
                            } else if ((caEmail1 == null || caEmail1.isEmpty()) && (supEmail1 != null && !supEmail1.isEmpty()) && (coordEmail1 != null && !coordEmail1.isEmpty())) {
                                recpdata.add("s");
                                recpdata.add("c");
                            } else if ((caEmail1 != null && !caEmail1.isEmpty()) && (supEmail1 == null || supEmail1.isEmpty()) && (coordEmail1 != null && !coordEmail1.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("c");
                            } else if ((caEmail1 != null && !caEmail1.isEmpty()) && (supEmail1 != null && !supEmail1.isEmpty()) && (coordEmail1 == null || coordEmail1.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("s");
                            } else if ((caEmail1 != null && !caEmail1.isEmpty()) && (adminEmail1 != null && !adminEmail1.isEmpty()) && (coordEmail1 == null || coordEmail1.isEmpty())) {
                                recpdata.add("ca");
                                recpdata.add("m");
                            } else if ((caEmail1 == null || caEmail1.isEmpty()) && (supEmail1 == null || supEmail1.isEmpty()) && (coordEmail1 != null && !coordEmail1.isEmpty())) {
                                recpdata.add("c");
                            } else if ((caEmail1 == null || caEmail1.isEmpty()) && (supEmail1 != null && !supEmail1.isEmpty()) && (coordEmail1 == null || coordEmail1.isEmpty())) {
                                recpdata.add("s");
                            } else if ((caEmail1 != null && !caEmail1.isEmpty()) && (supEmail1 == null || supEmail1.isEmpty()) && (coordEmail1 == null || coordEmail1.isEmpty())) {
                                recpdata.add("ca");
                            }
                            recpdata.add("d");
                            break;
                        default:
                            break;
                    }
                }

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 275" + e.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 276" + e.getMessage());
                    }
                }
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 277" + e.getMessage());
                    }
                }
            }
        }
        return new ArrayList<>(recpdata);
    }

    private ArrayList<String> fetchAllCoordinators() {

        PreparedStatement ps = null;
        ResultSet rs = null;

        Set<String> coords = new HashSet();
        try {

            conSlave = DbConnection.getSlaveConnection();
            String qry = " select emp_coord_email FROM employment_coordinator WHERE (emp_type = 'c'  or  emp_type = 'dc') and (emp_status = 'a')";
            ps = conSlave.prepareStatement(qry);
            rs = ps.executeQuery();

            while (rs.next()) {
                coords.add(rs.getString("emp_coord_email").toLowerCase().trim());
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetch all coordinators " + e.getMessage());
//            e.printStackTrace();
//            e.getMessage();

        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 51 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchAllCoordinators EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 52 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchAllCoordinators EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //// above line commented below added by pr on 23rdapr19                    
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Replace printstcaktrace EXCEPTION 53 " + e.getMessage());

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "fetchAllCoordinators EXCEPTION 3 " + e.getMessage());

                }
            }
        }

        return new ArrayList<String>(coords);
    }

    private String findStatForwardedTo(String status) {
        if (status.contains("manual")) {
            return "a";
        } else if (status.equalsIgnoreCase("coordinator_pending")) {
            return "c";
        } else if (status.equalsIgnoreCase("da_pending")) {
            return "d";
        } else if (status.equalsIgnoreCase("support_pending")) {
            return "s";
        } else if (status.equalsIgnoreCase("mail-admin_pending")) {
            return "m";
        } else if (status.equalsIgnoreCase("ca_pending")) {
            return "ca";
        } else if (status.equalsIgnoreCase("_pending")) {
            return "m";
        } else {
            return "";
        }
    }

    public List<String> fetchHardCodedCoordinatorsOrDAs(String employment, String ministry, String department) throws SQLException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        //String [] coDaData = new String [2];
        String queryDynamic = null;
        List<String> coordinatorsOrDas = new ArrayList();
        try {
            if (employment != null && !employment.isEmpty() && ministry != null && !ministry.isEmpty() && (department == null || department.isEmpty())) {
                queryDynamic = "select da_coord_email, emp_type from hardcoded_coordinators where employment=? and min_state_org=? and emp_status='a';";
                pst = conSlave.prepareStatement(queryDynamic);
                pst.setString(1, employment);
                pst.setString(2, ministry);
            } else if (employment != null && !employment.isEmpty() && ministry != null && !ministry.isEmpty() && department != null && !department.isEmpty()) {
                queryDynamic = "select da_coord_email, emp_type from hardcoded_coordinators where employment=? and min_state_org=? and department = ? and emp_status = 'a';";
                pst = conSlave.prepareStatement(queryDynamic);
                pst.setString(1, employment);
                pst.setString(2, ministry);
                pst.setString(3, department);
            }
            rs = pst.executeQuery();
            while (rs.next()) {
                coordinatorsOrDas.add((rs.getString("da_coord_email").trim() + ":" + rs.getString("emp_type").trim()).toLowerCase().trim());
            }
            return coordinatorsOrDas;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return new ArrayList<>();
    }
}
