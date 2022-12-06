package admin;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.UserData;
import com.org.utility.Constants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.org.bean.Forms;
import com.org.bean.ListBean;
import com.org.bean.StatusTable;
import com.org.connections.DbConnection;
import com.org.dao.Database;
import com.org.dao.Ldap;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
import utility.ExcelCreator;
import utility.NewFileInputStream;
import validation.validation;

public class NewCAHome extends ActionSupport implements SessionAware, ServletResponseAware {

    Map sessionMap;
    private static Random rand = new Random((new Date()).getTime());
    private PreparedStatement pst = null;
    private String role = "";
    private ListBean listBeanObj;//  code added on 8thnov17
    private Map<String, Object> hmTrack = null;
    private Database db = null;
    private String adminValue = "";
    private String actype = "";
    private String reg_no;
    private String ca_id = "";
    private String loggedin_email = "";
    private ArrayList email = null;
    private boolean exportFlag = false;
    private Boolean raiseQueryBtn = true;
    private String CSRFRandom = "";
    private int position = -1;
    private String searchedEmail;
    private String searchedMobile;//their getters and setters

    public String getSearchedEmail() {
        return searchedEmail;
    }

    public void setSearchedEmail(String searchedEmail) {
        this.searchedEmail = searchedEmail;
    }

    public String getSearchedMobile() {
        return searchedMobile;
    }

    public void setSearchedMobile(String searchedMobile) {
        this.searchedMobile = searchedMobile;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getRaiseQueryBtn() {
        return raiseQueryBtn;
    }

    public void setRaiseQueryBtn(Boolean raiseQueryBtn) {
        this.raiseQueryBtn = raiseQueryBtn;
    }

    public boolean isExportFlag() {
        return exportFlag;
    }

    public void setExportFlag(boolean exportFlag) {
        this.exportFlag = exportFlag;
    }

    private HashMap<String, String> empDetailsHM = new HashMap<String, String>();

    // start, code added by pr on 4thsep19
    private HttpServletResponse response;

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getServletResponse() {
        return response;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;
    }

    // end, code added by pr on 4thsep19
    private List<String> searchdata = new ArrayList<String>();
    ArrayList<String> supForms = null; // supForms will contain all the form name like 'single','bulk','nkn' and so on ...
    private ArrayList<String> role_head = null;
    private ArrayList<String> roles_service = null; // added on 24thoct19
    List<Forms> bulkUsersdata = new ArrayList();
    private List<String> searchformdata = new ArrayList<String>();
    List<Forms> bulkAllUsersdata = new ArrayList();
    private ArrayList<String> filterForms;
    private List list = new ArrayList<>();
    List<Forms> aaData = new LinkedList<>();
    List<Forms> data = new LinkedList<>();
    String app_user_type = "";
    String app_ca_type = "";
    String skeyword = "";
    String srole = "", trole = "", forward = "";
    private String msg = "";
    String appType = "";
    private String frm[];
    private String stat[];
    public String sEcho;
    public String sSearch;
    public String sSortDirection;
    public String sColumns;
    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null, conSlave = null; // this and below line added by pr on 24thmay18
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
    Boolean showPullBack;
    Boolean showQuery = false;
    Boolean showQueryBy = false;   // added by rahul march 2021
    String showQueryRespone = "";   //added by rahul june-2021
    Boolean showAction = false;
    Boolean showBulkLink = false;
    Boolean showCreateAction = false;
    Boolean showBulkCreateAction = false; // line added by pr on 13thdec17
    Boolean showBulkUpdateAction = false; // line added by pr on 12thdec19
    Boolean showApprove = true;
    public int iDisplayLength;
    public int iDisplayStart;
    public int iColumns;
    public int iSortingCols;
    public int iSortColumnIndex;
    public int iTotalRecords;
    public int iTotalDisplayRecords;
    String sessionid = ServletActionContext.getRequest().getSession().getId(); // line added by pr on 4thsep19

    // start, code added by pr on 30thjul19
    private String queryfilter[];

    public String[] getQueryfilter() {
        return queryfilter;
    }

    public void setQueryfilter(String[] queryfilter) {
        this.queryfilter = queryfilter;
    }

    private String querydate;

    public String getQuerydate() {
        return querydate;
    }

    public void setQuerydate(String querydate) {
        this.querydate = querydate;
    }

    private InputStream fileInputStream;

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public String getShowQueryRespone() {
        return showQueryRespone;
    }

    public void setShowQueryRespone(String showQueryRespone) {
        this.showQueryRespone = showQueryRespone;
    }

    // end, code added by pr on 30thjul19
    public NewCAHome() {
        listBeanObj = new ListBean();
        filterForms = new ArrayList<>();
        db = new Database();
        hmTrack = new HashMap<>();
    }

    public String fetch_role() {
        if (ActionContext.getContext().getSession() != null) {
            this.sessionMap = ActionContext.getContext().getSession();
            if (sessionMap.get("admin_role") != null) {
                this.role = sessionMap.get("admin_role").toString();
            }
        }
        //System.out.println(" inside fetch_role function value of role is " + role);
        return this.role;
    }

    public String getApp_user_type() {
        return app_user_type;
    }

    public void setApp_user_type(String app_user_type) {
        this.app_user_type = app_user_type;
    }

    public String getApp_ca_type() {
        return app_ca_type;
    }

    public void setApp_ca_type(String app_ca_type) {
        this.app_ca_type = app_ca_type;
    }

    public Boolean getShowPullBack() {
        return showPullBack;
    }

    public void setShowPullBack(Boolean showPullBack) {
        this.showPullBack = showPullBack;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public Map<String, Object> getHmTrack() {
        return hmTrack;
    }

    public void setHmTrack(Map<String, Object> hmTrack) {
        this.hmTrack = hmTrack;
    }

    public String getTrole() {
        return trole;
    }

    public void setTrole(String trole) {
        this.trole = trole;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public HashMap<String, String> getEmpDetailsHM() {
        return empDetailsHM;
    }

    public void setEmpDetailsHM(HashMap<String, String> empDetailsHM) {
        this.empDetailsHM = empDetailsHM;
    }

    public List<String> getSearchdata() {
        return searchdata;
    }

    public void setSearchdata(List<String> searchdata) {
        this.searchdata = searchdata;
    }

    public Boolean getShowQuery() {
        return showQuery;
    }

    public void setShowQuery(Boolean showQuery) {
        this.showQuery = showQuery;
    }

    public Boolean getShowQueryBy() {
        return showQueryBy;
    }

    public void setShowQueryBy(Boolean showQueryBy) {
        this.showQueryBy = showQueryBy;
    }

    public List<String> getSearchformdata() {
        return searchformdata;
    }

    public void setSearchformdata(List<String> searchformdata) {
        this.searchformdata = searchformdata;
    }

    public String getSkeyword() {
        return skeyword;
    }

    public void setSkeyword(String skeyword) {
        this.skeyword = skeyword;
    }

    public String getSrole() {
        return srole;
    }

    public void setSrole(String srole) {
        this.srole = srole;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getShowApprove() {
        return showApprove;
    }

    public void setShowApprove(Boolean showApprove) {
        this.showApprove = showApprove;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public ArrayList<String> getFilterForms() {
        return filterForms;
    }

    public void setFilterForms(ArrayList<String> filterForms) {
        this.filterForms = filterForms;
    }

    public List<Forms> getBulkAllUsersdata() {
        return bulkAllUsersdata;
    }

    public void setBulkAllUsersdata(List<Forms> bulkAllUsersdata) {
        this.bulkAllUsersdata = bulkAllUsersdata;
    }

    public List<Forms> getBulkUsersdata() {
        return bulkUsersdata;
    }

    public void setBulkUsersdata(List<Forms> bulkUsersdata) {
        this.bulkUsersdata = bulkUsersdata;
    }

    public String getActype() {
        return actype;
    }

    public void setActype(String actype) {
        this.actype = actype;
    }

    public String getAdminValue() {
        return adminValue;
    }

    public void setAdminValue(String adminValue) {
        this.adminValue = adminValue;
    }

    public ListBean getListBeanObj() {
        return listBeanObj;
    }

    public void setListBeanObj(ListBean listBeanObj) {
        this.listBeanObj = listBeanObj;
    }

    public String[] getFrm() {
        return frm;
    }

    public void setFrm(String[] frm) {
        this.frm = frm;
    }

    public String[] getStat() {
        return stat;
    }

    public void setStat(String[] stat) {
        this.stat = stat;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setSession(Map sessionMap) {
        this.sessionMap = sessionMap;
    }

    public List<Forms> getAaData() {
        return aaData;
    }

    public void setAaData(List<Forms> aaData) {
        this.aaData = aaData;
    }

    public List<Forms> getData() {
        return data;
    }

    public void setData(List<Forms> data) {
        this.data = data;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public String getsSearch() {
        return sSearch;
    }

    public void setsSearch(String sSearch) {
        this.sSearch = sSearch;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiColumns() {
        return iColumns;
    }

    public void setiColumns(int iColumns) {
        this.iColumns = iColumns;
    }

    public int getiSortingCols() {
        return iSortingCols;
    }

    public void setiSortingCols(int iSortingCols) {
        this.iSortingCols = iSortingCols;
    }

    public int getiSortColumnIndex() {
        return iSortColumnIndex;
    }

    public void setiSortColumnIndex(int iSortColumnIndex) {
        this.iSortColumnIndex = iSortColumnIndex;
    }

    public String getsSortDirection() {
        return sSortDirection;
    }

    public void setsSortDirection(String sSortDirection) {
        this.sSortDirection = sSortDirection;
    }

    public String getsColumns() {
        return sColumns;
    }

    public void setsColumns(String sColumns) {
        this.sColumns = sColumns;
    }

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String execute() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initializeSessionValues() {

        try {
            UserData userdata = (UserData) sessionMap.get("uservalues");
            System.out.println("userdata::::::" + userdata.toString());
            //System.out.println("userdata::::::" + userdata.getEmail() + userdata.getMobile());
            this.role_head = (ArrayList) userdata.getRoles();
            System.out.println("showlinkdata user role:::::::::" + role_head);
            // start, code added by pr on 24thoct19

            this.roles_service = (ArrayList) userdata.getRolesService();
            System.out.println("showlinkdata user roles_service:::::::::" + roles_service);

            // end, code added by pr on 24thoct19
            this.loggedin_email = userdata.getEmail();
            Set s = (Set) userdata.getAliases();
            //System.out.println(" inside getEmailEquivalent get alias not null intialize set value is  " + s);
            ArrayList<String> newAr = new ArrayList<>();
            newAr.addAll(s);
            this.email = newAr;
            this.supForms = (ArrayList<String>) userdata.getFormsAllowed();
            System.out.println("Forms Allowed : " + this.supForms);
        } catch (Exception e) // try catch added by pr on 25thapr19
        {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " initializeSessionValues MAIN EXCEPTION CATCH " + e.getMessage());
        }

    }

    public ArrayList<String> showFilters(ArrayList<String> aliases) {
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
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
            //System.out.println("ROLE IN SHOWFILTER : " + role);
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
                        //System.out.println("my sb:" + sb);

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
            filterForms.clear();
            ps = conSlave.prepareStatement(qry);
            res1 = ps.executeQuery();
            while (res1.next()) {
                String form_name = res1.getString("form_name");
                // System.out.println("SUPFORMS IN SHOWFILTER ::::::::::::::::::::: " + supForms);
                if (role.equals(Constants.ROLE_SUP) || role.equals(Constants.ROLE_MAILADMIN)) {
                    if (supForms != null) {
                        if (supForms.contains("'" + form_name + "'")) {
                            //System.out.println("FORM :::::::::::::: " + form_name + " getting allowed");
                            filterForms.add(form_name);
                        }
                    }
                } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) // if and else around added by pr on 23rdoct19
                {
                    UserData userdata = (UserData) sessionMap.get("uservalues");
                    System.out.println("IN SHOWFILTER For Coordinator (" + userdata.getEmail() + ") :::" + this.roles_service.toString());
                    this.roles_service = (ArrayList) userdata.getRolesService();
                    System.out.println("Roles: " + this.roles_service);
                    if (roles_service.contains(form_name)) {
                        filterForms.add(form_name);
                    }
                    System.out.println("FILTERS for (" + userdata.getEmail() + ") ::: " + filterForms.toString());
                } else {
                    filterForms.add(form_name);
                }
            }
            //System.out.println(" inside show filter filterForms value is " + filterForms);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " showFilters EXCEPTION " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showFilters EXCEPTION 2 " + e.getMessage());
                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showFilters EXCEPTION 3 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showFilters EXCEPTION 4 " + e.getMessage());
                }
            }
        }
        return filterForms;
    }

//    public Boolean queryRaiseStatus(String reg_no) {    // Commented by AT & RM June-2021
//        Boolean queryStatus = false;
//        String role = fetch_role();
//        String forwarded_to = "", forwarded_to_user = "";
//        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " only email is  " + email);
//        String email = String.join(",", this.email);
//        forwarded_to_user = email;
//        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " email with mailequivalents is  " + email);
//        if (role.equals(Constants.ROLE_CA)) {
//            forwarded_to = "ca";
//        } else if (role.equals(Constants.ROLE_CO)) {
//            forwarded_to = "c";
//        } else if (role.equals(Constants.ROLE_SUP)) {
//            forwarded_to = "s";
//        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
//            forwarded_to = "m";
//        }
//        //Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            //con = DbConnection.getConnection();
//            conSlave = DbConnection.getSlaveConnection();
//            String qry = "  SELECT qr_forwarded_to,qr_forwarded_to_user FROM query_raise WHERE qr_reg_no = ? ORDER BY qr_createdon DESC LIMIT 1 ";
//            ps = conSlave.prepareStatement(qry);
//            ps.setString(1, reg_no);
//            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " query raise status query is  " + ps);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                if (rs.getString("qr_forwarded_to").equals(forwarded_to) && forwarded_to.equals("s")) {
//                    queryStatus = true;
//                } else if (rs.getString("qr_forwarded_to").equals(forwarded_to) && (rs.getString("qr_forwarded_to_user").equals(forwarded_to_user) || forwarded_to_user.contains(rs.getString("qr_forwarded_to_user")))) {
//                    queryStatus = true;
//                }
//            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//
//            // above line commented below added by pr on 23rdapr19
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 1 " + e.getMessage());
//
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 2 " + e.getMessage());
//                }
//            }
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 3 " + e.getMessage());
//                }
//            }
//            if (con != null) {
//                try {
//                    // con.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 4 " + e.getMessage());
//
//                }
//            }
//        }
//        return queryStatus;
//    }
//    
//      public Boolean queryRaiseStatusby(String reg_no) {   // added by rahul jan 2021
//        Boolean queryStatus1 = false;
//        String role = fetch_role();
//              String forwarded_by = "", forwarded_by_user = "";
//        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " only email is  " + email);
//        String email = String.join(",", this.email);
//        // System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " email with mailequivalents is  " + email);
//        if (role.equals(Constants.ROLE_CA)) {
//            forwarded_by = "ca";
//        } else if (role.equals(Constants.ROLE_CO)) {
//            forwarded_by = "c";
//        } else if (role.equals(Constants.ROLE_SUP)) {
//            forwarded_by = "s";
//        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
//            forwarded_by = "m";
//        }
//        //Connection con = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            //con = DbConnection.getConnection();
//            conSlave = DbConnection.getSlaveConnection();
//            String qry = "  SELECT qr_forwarded_by,qr_forwarded_by_user FROM query_raise WHERE qr_reg_no = ? ORDER BY qr_createdon DESC LIMIT 1 ";
//            ps = conSlave.prepareStatement(qry);
//            ps.setString(1, reg_no);
//            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " query raise status query is  " + ps);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                if (rs.getString("qr_forwarded_by").equals(forwarded_by) && forwarded_by.equals("ca")) {
//                    queryStatus1 = true;     
//                }
//               else if (rs.getString("qr_forwarded_by").equals(forwarded_by) && forwarded_by.equals("m")) {
//                    queryStatus1 = true;     
//                }
//               else if (rs.getString("qr_forwarded_by").equals(forwarded_by) && forwarded_by.equals("c")) {
//                    queryStatus1 = true;     
//                }
//               else if (rs.getString("qr_forwarded_by").equals(forwarded_by) && forwarded_by.equals("s")) {
//                    queryStatus1 = true;     
//                }
//                else if (rs.getString("qr_forwarded_by").equals(forwarded_by) && (rs.getString("qr_forwarded_by_user").equals(forwarded_by_user) || forwarded_by_user.contains(rs.getString("qr_forwarded_by_user")))) {
//                    queryStatus1 = true;
//                }
//            }
//        } catch (Exception e) {
//            //e.printStackTrace();
//
//            // above line commented below added by pr on 23rdapr19
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 1 " + e.getMessage());
//
//        } finally {
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 2 " + e.getMessage());
//                }
//            }
//            if (ps != null) {
//                try {
//                    ps.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 3 " + e.getMessage());
//                }
//            }
//            if (con != null) {
//                try {
//                    // con.close();
//                } catch (Exception e) {
//                    //e.printStackTrace();
//
//                    // above line commented below added by pr on 23rdapr19
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 4 " + e.getMessage());
//                }
//            }
//        }
//        return queryStatus1;
//    }
    public String queryRaiseStatusNew(String regNo) {   // added by rahul June 2021
        String status = "";
        Integer raisedQueriesCount = 0, respondedQueriesCount = 0, neutralQueriesCount = 0;

        UserData userdata = (UserData) sessionMap.get("uservalues");
        String loggedin_email = userdata.getEmail();
        Set<String> aliases = (Set<String>) userdata.getAliases(); // iterate through this set and check if it is equal to qr_forwarded_by_user 
        List<Integer> res = new ArrayList<Integer>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean flag = false;
        boolean innerFlag = false;
        String roleInSession = fetch_role();
        String currentrole = "";
        switch (roleInSession) {
            case Constants.ROLE_USER:
                currentrole = "u";
                break;
            case Constants.ROLE_CA:
                currentrole = "ca";
                break;
            case Constants.ROLE_SUP:
                currentrole = "s";
                break;
            case Constants.ROLE_MAILADMIN:
                currentrole = "m";
                break;
            case Constants.ROLE_DA:
                currentrole = "da";
                break;
            case Constants.ROLE_CO:
                currentrole = "c";
                break;
        }
        /**
         * 0 implies raised, 1 implies responded, -1 implies Done/neutral
         */
        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select * from query_raise where  qr_reg_no = ? and (qr_forwarded_by = ? or qr_forwarded_to = ?) order by qr_createdon asc";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            ps.setString(2, currentrole);
            ps.setString(3, currentrole);
            System.out.println("Table featched data: " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                flag = true;
                innerFlag = false;
                status = rs.getString("status");
                String toRole = rs.getString("qr_forwarded_to");
                String fromRole = rs.getString("qr_forwarded_by");
                String from = rs.getString("qr_forwarded_by_user");
                String to = rs.getString("qr_forwarded_to_user");

                if (status.equals("responded")) {
                    for (String loggedInEmail : aliases) {
                        innerFlag = true;
                        if (loggedInEmail.equalsIgnoreCase(from) && currentrole.equalsIgnoreCase(fromRole)) {
                            res.add(-1);
                            raisedQueriesCount = raisedQueriesCount - 1;
                        } else {
                            String[] toEmails = to.split(",");
                            for (String toEmail : toEmails) {
                                if (loggedInEmail.equalsIgnoreCase(toEmail) && currentrole.equalsIgnoreCase(toRole)) {
                                    res.add(-1);
                                    respondedQueriesCount = respondedQueriesCount - 1;
                                }
                            }
                        }
                    }
                } else if (status.equals("raised")) {
                    for (String loggedInEmail : aliases) {
                        String[] toEmails = to.split(",");
                        for (String toEmail : toEmails) {
                            if (loggedInEmail.equalsIgnoreCase(toEmail) && currentrole.equalsIgnoreCase(toRole)) {
                                innerFlag = true;
                                res.add(0);
                            }
                        }
                    }
                }

                if (!innerFlag) {
                    for (String loggedInEmail : aliases) {
                        if (loggedInEmail.equalsIgnoreCase(from) && currentrole.equalsIgnoreCase(fromRole)) {
                            innerFlag = true;
                            res.add(1);
                        }
                    }
                }
            }

            if (!flag) {
                showQueryRespone = "";
            }

            for (Integer re : res) {
                if (re == 0) {
                    raisedQueriesCount += 1;
                } else if (re == 1) {
                    respondedQueriesCount += 1;
                } else {
                    neutralQueriesCount += 1;
                }
            }

            if (raisedQueriesCount > 0 && respondedQueriesCount > 0) {
                showQueryRespone = "both";
            } else if (raisedQueriesCount > 0 && respondedQueriesCount <= 0) {
                showQueryRespone = "raised";
            } else if (respondedQueriesCount > 0 && raisedQueriesCount <= 0) {
                showQueryRespone = "responded";
            } else {
                showQueryRespone = "";
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 1 " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 2 " + e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 3 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside queryRaiseStatus EXCEPTION 4 " + e.getMessage());
                }
            }
        }
        return showQueryRespone;
    }

    // below function modified by pr on 24thmay18 to have the hashmap check for not null, app_user_type and app_ca_type added by pr on 18thsep19
    public Forms updateFormsObj(Forms frmObj, String app_user_type, String app_ca_type) {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateFormsObj app_user_type " + app_user_type + " app_ca_type " + app_ca_type);

        Forms obj = frmObj;
        String stat_form_type = frmObj.get_stat_form_type();
        String stat_reg_no = frmObj.get_stat_reg_no();
        String tblName = "";

        String form_fields = ""; // line added by pr on 29thmay19 to fetch form specific fields which are not common

        if (stat_form_type.equals(Constants.SMS_FORM_KEYWORD)) {
            tblName = "sms_registration";
        } else if (stat_form_type.equals(Constants.SINGLE_FORM_KEYWORD)) {
            tblName = "single_registration";
        } else if (stat_form_type.equals(Constants.BULK_FORM_KEYWORD)) {
            tblName = "bulk_registration";
        } else if (stat_form_type.equals(Constants.IP_FORM_KEYWORD)) {
            tblName = "ip_registration";

            form_fields = ", ip_action_request"; // line added by pr on 29thmay19 to fetch form specific fields which are not common

        } else if (stat_form_type.equals(Constants.NKN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
            tblName = "nkn_registration";
        } else if (stat_form_type.equals(Constants.RELAY_FORM_KEYWORD)) {
            tblName = "relay_registration";
        } else if (stat_form_type.equals(Constants.LDAP_FORM_KEYWORD)) {
            tblName = "ldap_registration";
        } else if (stat_form_type.equals(Constants.DIST_FORM_KEYWORD)) {
            tblName = "distribution_registration";
        } else if (stat_form_type.equals(Constants.BULKDIST_FORM_KEYWORD)) {
            tblName = "bulk_distribution_registration";
        } else if (stat_form_type.equals(Constants.IMAP_FORM_KEYWORD)) {
            tblName = "imappop_registration";
        } else if (stat_form_type.equals(Constants.GEM_FORM_KEYWORD)) {
            tblName = "gem_registration";
        } else if (stat_form_type.equals(Constants.MOB_FORM_KEYWORD)) {
            tblName = "mobile_registration";
        } else if (stat_form_type.equals(Constants.PROFILE_FORM_KEYWORD)) {
            tblName = "mobile_registration";
        } else if (stat_form_type.equals(Constants.DNS_FORM_KEYWORD)) {
            tblName = "dns_registration";
        } else if (stat_form_type.equals(Constants.WIFI_FORM_KEYWORD)) {
            tblName = "wifi_registration";
        } else if (stat_form_type.equals(Constants.VPN_ADD_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_SINGLE_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_RENEW_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_SURRENDER_FORM_KEYWORD) || stat_form_type.equals(Constants.VPN_DELETE_FORM_KEYWORD)) {
            tblName = "vpn_registration";
        } else if (stat_form_type.equals(Constants.CENTRAL_UTM_FORM_KEYWORD)) {
            tblName = "centralutm_registration";
        } else if (stat_form_type.equals(Constants.WEBCAST_FORM_KEYWORD)) {
            tblName = "webcast_registration";
        } else if (stat_form_type.equals(Constants.EMAILACTIVATE_FORM_KEYWORD)) {
            tblName = "email_act_registration";
        } else if (stat_form_type.equals(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) {
            tblName = "email_deact_registration";
        } else if (stat_form_type.equals(Constants.DAONBOARDING_FORM_KEYWORD)) {
            tblName = "daonboarding_registration";
        }

//        String qry = " SELECT auth_email, mobile, hod_email,hod_mobile,sign_cert,rename_sign_cert, ca_sign_cert, ca_rename_sign_cert, "
//                + ""
//                + "auth_off_name, designation, auth_off_name , pdf_path, registration_no " + form_fields + " FROM " + tblName + " WHERE registration_no = ? ";
        // query modified by pr on 18thsep19
        String qry = " SELECT sign_cert,rename_sign_cert, ca_sign_cert, ca_rename_sign_cert "
                + ""
                + ", pdf_path, registration_no " + form_fields + " FROM " + tblName + " WHERE registration_no = ? ";

        PreparedStatement ps = null;
        ResultSet res = null;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateFormsObj ps " + ps);
            res = ps.executeQuery();
            while (res.next()) {

//                obj.set_email(res.getString("auth_email"));
//                obj.set_mobile(res.getString("mobile"));
                //obj.setHod_email(res.getString("hod_email")); // line commented by pr on 18thsep19
                //obj.setHod_mobile(res.getString("hod_mobile")); // line commented by pr on 18thsep19
                obj.setSign_cert(res.getString("sign_cert"));
                obj.setRename_sign_cert(res.getString("rename_sign_cert"));
                obj.setCa_sign_cert(res.getString("ca_sign_cert"));
                obj.setCa_rename_sign_cert(res.getString("ca_rename_sign_cert"));
                //obj.setName(res.getString("auth_off_name"));
                //obj.setDesignation(res.getString("designation")); // line commented by pr on 18thsep19

                // start, code added by pr on 29thmay19
                if (tblName.equals("ip_registration")) {
                    obj.setForm_detail(res.getString("ip_action_request"));
                }
//                else if (tblName.equals("bulk_registration")) {
//                    String bulk_count = fetchBulkUsersCount(stat_reg_no);
//
//                    obj.setForm_detail(bulk_count);
//                }

                // end, code added by pr on 29thmay19
                if (res.getString("pdf_path") != null && !res.getString("pdf_path").contains("eSigned_") && !res.getString("pdf_path").contains("online processing")) {
                    obj.setPdf_path("manual_upload");
                } else {
                    obj.setPdf_path(res.getString("pdf_path"));
                }

//                HashMap<String, String> hm = fetchAppDetails(res.getString("registration_no")); // moved outside if by pr on 19thapr18
//                String app_user_type = "", app_ca_type = ""; // this code is modified by pr on 23rdmay18
//                if (hm != null && hm.size() > 0) {
//                    if (hm.get("app_user_type").contains("esign")) {
//                        app_user_type = "Esigned";
//                    } else if (hm.get("app_user_type").contains("online")) {
//                        app_user_type = "Online";
//                    } else if (hm.get("app_user_type").contains("manual_upload")) {
//                        app_user_type = "Manual";
//                    }
//                    if (hm.get("app_ca_type").contains("esign")) {
//                        app_ca_type = "Esigned";
//                    } else if (hm.get("app_ca_type").contains("online")) {
//                        app_ca_type = "Online";
//                    } else if (hm.get("app_ca_type").contains("manual_upload")) {
//                        app_ca_type = "Manual";
//                    }
//                }
//                app_user_type = "User: " + app_user_type;
//                if (!app_ca_type.equals("")) {
//                    app_user_type += "<br>RO: " + app_ca_type;
//                }
//                obj.setApp_user_type(app_user_type);
//                obj.setApp_ca_type(app_ca_type);
                if (role.equals(Constants.ROLE_CA)) //if its a CA panel check for the below 
                {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " role ca and regi no is " + res.getString("registration_no"));
                    obj.setAppType(app_user_type);

                    //System.out.println(" hm.get(\"app_user_type\") " + hm.get("app_user_type"));
                    //System.out.println(" app_user_type " + app_user_type);
                    if (app_user_type != null && app_user_type.equals("manual_upload")) // line updated by pr on 5thjun18
                    {
                        if (res.getString("ca_sign_cert") != null && !res.getString("ca_sign_cert").equals("")) // if user has uploaded a file in case of MANUAL then only the request could be forwarded
                        {
                            obj.setShowApprove(true);
                        } else {
                            obj.setShowApprove(false);
                        }
                    } else {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside manual upload else for reg no " + res.getString("registration_no"));
                        obj.setShowApprove(true);
                    }
                } else if (role.equals(Constants.ROLE_MAILADMIN)) //if its a Mailadmin  panel approve should not appear if else added by pr on 18thapr18
                {
                    obj.setShowApprove(false);
                    obj.setAppType("");
                } else {
                    obj.setShowApprove(true);
                    obj.setAppType("");
                }
//                String id = "actions_" + res.getString("registration_no");
//                obj.setActions("<div id='" + id + "'></div>"); // set the id of the actions span so that it could be replaced dynamically in the jsp script
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateFormsObj Exception 1: " + e.getMessage());
            //e.printStackTrace(); // line commented by pr on 23rdapr19
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateFormsObj Exception 2: " + e.getMessage());
                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateFormsObj Exception 3: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside updateFormsObj Exception 4: " + e.getMessage());
                }
            }
        }
        return obj;
    }

    public String fetchBulkUsersCount(String reg_no) {
        PreparedStatement ps = null;
        ResultSet res = null;

        int count = 0;

        int created = 0, rejected = 0;

        String countStr = "";

        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT is_created, is_rejected  FROM bulk_users WHERE registration_no = ? ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, reg_no);

            res = ps.executeQuery();

            while (res.next()) {
                count++;

                if (res.getString("is_created").equalsIgnoreCase("y")) {
                    created++;
                }

                if (res.getString("is_rejected").equalsIgnoreCase("y")) {
                    rejected++;
                }

            }
            // below lines added by pr on 6thmar18
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside inside get app details func : " + " - " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsersCount Exception 1: " + e.getMessage());

                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsersCount Exception 2: " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsersCount Exception 3: " + e.getMessage());
                }
            }
        }

        countStr = "Total Users : " + count;

        if (created > 0) {
            countStr += ", Created : " + created;
        }

        if (rejected > 0) {
            countStr += ", Rejected : " + rejected;
        }

        return countStr;

    }

    // below function added by pr on 12thapr18
    public HashMap<String, String> fetchAppDetails(String regNo) {
        PreparedStatement ps = null;
        ResultSet res = null;
        //Connection con = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT app_form_type,app_reg_no,app_user_type,app_user_path,app_ca_type,app_ca_path FROM application_type WHERE app_reg_no = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, regNo);
            res = ps.executeQuery();
            while (res.next()) {
                hm.put("app_form_type", res.getString("app_form_type"));
                hm.put("app_reg_no", res.getString("app_reg_no"));
                if (res.getString("app_user_type") != null) // if else around added by pr on 19thapr18
                {
                    hm.put("app_user_type", res.getString("app_user_type"));
                } else {
                    hm.put("app_user_type", "");
                }
                hm.put("app_user_path", res.getString("app_user_path"));
                if (res.getString("app_ca_type") != null) // if else around added by pr on 19thapr18
                {
                    hm.put("app_ca_type", res.getString("app_ca_type"));
                } else {
                    hm.put("app_ca_type", "");
                }
                hm.put("app_ca_path", res.getString("app_ca_path"));
            }
            // below lines added by pr on 6thmar18
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside inside get app details func : " + " - " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAppDetails Exception 1: " + e.getMessage());

                }
            }
            if (res != null) {
                try {
                    res.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAppDetails Exception 2: " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAppDetails Exception 3: " + e.getMessage());
                }
            }
        }
        return hm;
    }

    //modified by AT on 2ndmay19
    public int fetchCount(String search, String type, String formName) {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  start of getcount function ");
        System.out.println("Start process of fetchCount for ro to admin role: ");
        // start, code added by pr on 26thjun19
        ArrayList<String> queryRaiseData = null;

        /*if (type.equals("query")) // if query raise data needs to be shown
        {
            type = "total"; // treat it as total
            queryRaiseData = fetchQueryRaisedForms();
        }

        if (type.equals("query_ans")) // if added by pr on 27thjun19
        {
            type = "total"; // treat it as total
            queryRaiseData = fetchQueryAnsweredForms();
        }*/
        // above code commented and start below added by pr on 11thjul19
        String querytype = "";

        String querystart = null, queryend = null;

        if (querydate != null && !querydate.equals("")) {
            String[] queryArr = querydate.split(" - ");

            querystart = returnDBDate(queryArr[0]); // mm/dd/yyyy as input yyyy-mm-dd as output

            queryend = returnDBDate(queryArr[1]); // mm/dd/yyyy as input yyyy-mm-dd as output    

        }

        if (queryfilter != null && queryfilter.length > 0) {
//            for (String str : queryfilter) {
//                System.out.println(" inside getDataPage query filter loop " + str);
//            }

            querytype = queryfilter[0];
        }

        if (querytype.equals("query")) // if query raise data needs to be shown
        {
            queryRaiseData = fetchQueryRaisedForms(querystart, queryend);
        } else if (querytype.equals("query_ans")) // if added by pr on 27thjun19
        {
            queryRaiseData = fetchQueryAnsweredForms(querystart, queryend);
        }

        // end code added by pr on 11thjul19
        // start, code added by pr on 6thaug19
        boolean showOnHold = false;

        if (type.equals("onhold")) // if added by pr on 27thjun19
        {
            type = "pending"; // treat it as total
            showOnHold = true;
        }
        // end, code added by pr on 6thaug19
        UserData userdata = (UserData) sessionMap.get("uservalues");
        long startTime = System.currentTimeMillis();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        int cnt = 0;
        if (supForms != null) {
            if (supForms.size() > 0) {
                csvForms = String.join(",", supForms);
            }
        }
        role = sessionMap.get("admin_role").toString();
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

            if (showOnHold) {
                qry += " AND ( on_hold = 'y' "; // so as to get only those applications which are on hold  // ( added by pr on 7thjan2020                   
            }

//            else {
//                qry += " AND on_hold = 'n' "; // so as to get only those applications which are NOT on hold    , in case of pending requests
//            }
            if (!search.equals("")) {
                statusString = "";

                if (Constants.CA_PENDING.contains(search)) {
                    statusString = "ca_pending";
                } else if (Constants.COORDINATOR_PENDING.contains(search)) {
                    statusString = "coordinator_pending";
                } else if (Constants.SUPPORT_PENDING.contains(search)) {
                    statusString = "support_pending";
                } else if (Constants.MAIL_ADMIN_PENDING.contains(search)) {
                    statusString = "mail-admin_pending";
                } else if (Constants.CA_REJECTED.contains(search)) {
                    statusString = "ca_rejected";
                } else if (Constants.COORDINATOR_REJECTED.contains(search)) {
                    statusString = "coordinator_rejected";
                } else if (Constants.SUPPORT_REJECTED.contains(search)) {
                    statusString = "support_rejected";
                } else if (Constants.MAIL_ADMIN_REJECTED.contains(search)) {
                    statusString = "mail-admin_rejected";
                } else if (Constants.COMPLETED.contains(search)) {
                    statusString = "completed";
                }

                if (statusString.equals("")) {
                    statusString = search;
                }

                qry += " AND ( applicant_email LIKE ?  OR applicant_name LIKE ? OR applicant_mobile LIKE ? OR registration_no LIKE ?  OR to_datetime LIKE ? OR status LIKE '%" + statusString + "%' OR app_user_type LIKE ? OR app_ca_type LIKE ?)";
            }
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
                    if (coordsIds.size() != 0) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }

                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        //System.out.println("my sb:" + sb);

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
                    System.out.println("Query for Coordinator (" + userdata.getEmail() + ") ::: " + qry);
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
                                //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                                qry += " and  form_name = ? ";
                            }
                        }
                    } else if (formName.contains("nkn")) {
                        qry += " and  form_name like ? ";
                    } else {
                        //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYZZZZZZZZZZZZZZZZZ");
                        qry += " and  form_name = ? ";
                    }
                } else if (formName.contains("nkn")) {
                    qry += " and  form_name like ? ";
                } else {
                    //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYZZZZZZZZZZZZZZZZZ");
                    qry += " and  form_name = ? ";
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                if (!csvForms.isEmpty()) {
                    qry += " and  form_name in (" + csvForms + ") ";
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) // else if added by pr on 24thoct19
            {
                System.out.println(" before considering forms for coordinator (" + userdata.getEmail() + ")query is " + qry);

                if (roles_service != null) {
                    qry += " and form_name in ( ";

                    int i = 1;
                    for (String frm : roles_service) {
                        if (i > 1) {
                            qry += ",";
                        }

                        qry += "'" + frm + "'";

                        i++;
                    }

                    qry += ")";

                }

                System.out.println(" AFTER considering forms for coordinator (" + userdata.getEmail() + ") query is " + qry);
            }

            // start, code added by pr on 26thjun19
            // put a check on form reg nos here in case of query raise filter , below code added by pr on 26thjun19
            if (queryRaiseData != null) {
                String arrString = String.join("\",\"", queryRaiseData);

                arrString = "\"" + arrString + "\"";

                qry += " AND registration_no in (" + arrString + ") ";
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

            // start, code added by pr on 7thjan2020
            //System.out.println(" role value is "+role+" BEFOREEEEEEEEE  inside the on hold where clause in count query 1");
            if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                //System.out.println(" inside the on hold where clause in count query ");

                qry += " AND on_hold = 'n' ";
            }

            // end, code added by pr on 7thjan2020 
            // end, code added by pr on 2ndjul19
            // start, code added by pr on 6thaug19        
            // end, code added by pr on 6thaug19            
            // end, code added by pr on 26thjun19
            System.out.println(" Query for Count for (" + userdata.getEmail() + ") query is " + qry);
            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;
            if (!search.equals("")) {

                for (int i = 0; i < 7; i++) {
                    pscnt = pscnt + 1;
                    ps.setString(pscnt, "%" + search + "%");
                }
            }
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY is " + ps);
            res1 = ps.executeQuery();
            if (res1.next()) {
                cnt = res1.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY EXCEPTION " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 1: " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 2: " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getCount EXCEPTION 3: " + e.getMessage());

                }
            }
        }
        long endTime = System.currentTimeMillis();
        //System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  end of getcount function ");
        return cnt;
    }

    // below function added by pr on 26thjul19, this gives the total data without pagination or limit data
    public ArrayList<Forms> fetchDataPageExport(String search, String type, String formName) {

//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  start of fetchDataPageExport function ");
        // start, code added by pr on 26thjun19
        ArrayList<String> queryRaiseData = null;

//        if (type.equals("query")) {
//            type = "total";
//            queryRaiseData = fetchQueryRaisedForms();
//        }
//
//        if (type.equals("query_ans")) {
//            type = "total";
//            queryRaiseData = fetchQueryAnsweredForms();
//        }
        // above code commented and start below added by pr on 30thjul19
        String querytype = "";

        String querystart = null, queryend = null;

        if (querydate != null && !querydate.equals("")) {
            String[] queryArr = querydate.split(" - ");

            querystart = returnDBDate(queryArr[0]); // mm/dd/yyyy as input yyyy-mm-dd as output

            queryend = returnDBDate(queryArr[1]); // mm/dd/yyyy as input yyyy-mm-dd as output    

        }

        if (queryfilter != null && queryfilter.length > 0) {
//            for (String str : queryfilter) {
//                System.out.println(" inside getDataPage query filter loop " + str);
//            }

            querytype = queryfilter[0];
        }

        if (querytype.equals("query")) // if query raise data needs to be shown
        {
            queryRaiseData = fetchQueryRaisedForms(querystart, queryend);
        } else if (querytype.equals("query_ans")) // if added by pr on 27thjun19
        {
            queryRaiseData = fetchQueryAnsweredForms(querystart, queryend);
        }

        // end code added by pr on 30thjul19
        // start, code added by pr on 6thaug19
        boolean showOnHold = false;

        if (type.equals("onhold")) {
            type = "pending";
            showOnHold = true;
        }

        // end, code added by pr on 6thaug19        
        // end, code added by pr on 26thjun19
        long startTime = System.currentTimeMillis();
        //System.out.println(" inside get fetchDataPageExport role is " + role);
        ArrayList<Forms> totalArr = new ArrayList<Forms>();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        if (supForms != null) {
            if (supForms.size() > 0) {
                csvForms = String.join(",", supForms);
            }
        }
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
            // applicant details added by pr on 18thsep19
            String qry = "SELECT status,form_name,track_id,registration_no,to_datetime,applicant_email,ca_email,coordinator_email,support_email,da_email,admin_email,"
                    + "to_email,app_ca_type,app_user_type, on_hold,applicant_name,applicant_mobile,applicant_email,support_remarks FROM final_audit_track WHERE 1 "; // on_hold column added by pr on 6thaug19

            if (showOnHold) {
                qry += " AND ( on_hold = 'y' "; // so as to get only those applications which are on hold  // ( added by pr on 7thjan2020                    
            }

//            else {
//
//                if (type.equals("pending") || type.equals("new")) // if we are showing the pending or new requests then show only unhold request but in total all must be visible
//                {
//                    qry += " AND  on_hold = 'n' "; // so as to get only those applications which are NOT on hold    , in case of pending requests
//                }
//            }
            if (Constants.CA_PENDING.contains(search)) {
                statusString = "ca_pending";
            } else if (Constants.COORDINATOR_PENDING.contains(search)) {
                statusString = "coordinator_pending";
            } else if (Constants.SUPPORT_PENDING.contains(search)) {
                statusString = "support_pending";
            } else if (Constants.MAIL_ADMIN_PENDING.contains(search)) {
                statusString = "mail-admin_pending";
            } else if (Constants.CA_REJECTED.contains(search)) {
                statusString = "ca_rejected_pending";
            } else if (Constants.COORDINATOR_REJECTED.contains(search)) {
                statusString = "coordinator_rejected";
            } else if (Constants.SUPPORT_REJECTED.contains(search)) {
                statusString = "support_rejected";
            } else if (Constants.MAIL_ADMIN_REJECTED.contains(search)) {
                statusString = "mail-admin_rejected";
            } else if (Constants.COMPLETED.contains(search)) {
                statusString = "completed";
            }

            if (!search.equals("")) {
                statusString = "";
                if (search.contains("re") || search.contains("fo") || search.contains("no")) {
                    statusString = "ca";
                } else if (search.contains("co")) {
                    statusString = "coordinator";
                } else if (search.contains("su")) {
                    statusString = "support";
                } else if (search.contains("ad")) {
                    statusString = "mail-admin";
                }

                if (statusString.equals("")) {
                    statusString = search;
                }

                qry += " AND ( applicant_email LIKE ?  OR applicant_name LIKE ? OR applicant_mobile LIKE ? OR registration_no LIKE ?  OR to_datetime LIKE ? OR status LIKE '" + statusString + "%' OR app_user_type LIKE ? OR app_ca_type LIKE ?) ";
            }
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
                                    qry += " ( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "') ";
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
                    //System.out.println("QUERY : " + qry);
                }

                //modified by AT to improvise query on 3rd Sep 2019
                // need to get id from coordinator id
                if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    Set<String> coordsIds = db.fetchCoIds(email);

                    String sb = "";
                    if (coordsIds.size() != 0) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }

                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        //System.out.println("my sb:" + sb);

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
            if (!formName.equals("all")) {
                if (supForms != null) {
                    if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                        if (supForms.contains("'" + formName + "'")) {
                            if (formName.contains("nkn")) {
                                qry += " and  form_name like ? ";
                            } else {
                                qry += " and  form_name = ? ";
                            }
                        }
                    } else if (formName.contains("nkn")) {
                        qry += " and  form_name like ? ";
                    } else {
                        qry += " and  form_name = ? ";
                    }
                } else if (formName.contains("nkn")) {
                    qry += " and  form_name like ? ";
                } else {
                    qry += " and  form_name = ? ";
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                if (!csvForms.isEmpty()) {
                    qry += " and  form_name in (" + csvForms + ") "; // line added by pr on 21stfeb18
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) // else if added by pr on 24thoct19
            {
                //System.out.println(" before considering forms for coordinator query is " + qry);

                if (roles_service != null) {
                    qry += " and form_name in ( ";

                    int i = 1;
                    for (String frm : roles_service) {
                        if (i > 1) {
                            qry += ",";
                        }

                        qry += "'" + frm + "'";

                        i++;
                    }

                    qry += ")";

                }

                //System.out.println(" AFTER considering forms for coordinator query is " + qry);
            }

            // start, code added by pr on 26thjun19
            // put a check on form reg nos here in case of query raise filter , below code added by pr on 26thjun19
            if (queryRaiseData != null) {
                String arrString = String.join("\",\"", queryRaiseData);

                arrString = "\"" + arrString + "\"";

                qry += " AND registration_no in (" + arrString + ") ";
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

            qry += " ORDER BY to_datetime DESC ";
            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;
            if (!search.equals("")) {
                for (int i = 0; i < 7; i++) {
                    pscnt = pscnt + 1;
                    ps.setString(pscnt, "" + search + "%");
                }
            }
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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchDataPageExport fetchDataPageExport QUERY is " + ps);
            res1 = ps.executeQuery();
            Forms frmObj = null;

            String showErrorSuccess = ""; // line added by pr on 25thjul19            

            while (res1.next()) {

                showErrorSuccess = ""; // line added by pr on 25thjul19                            

                showAction = fetchRoleBasedShowAction(res1.getString("status"), res1.getString("form_name"));
                String status = res1.getString("status");
                if (status == null) {
                    status = "";
                }
                String applicant_email = res1.getString("applicant_email");
                if (applicant_email == null) {
                    applicant_email = "";
                }
                String ca_email = res1.getString("ca_email");
                if (ca_email == null) {
                    ca_email = "";
                }
                String support_email = res1.getString("support_email");
                if (support_email == null) {
                    support_email = "";
                }
                String coordinator_email = res1.getString("coordinator_email");
                if (coordinator_email == null) {
                    coordinator_email = "";
                }
                String da_email = res1.getString("da_email");
                if (da_email == null) {
                    da_email = "";
                }
                String admin_email = res1.getString("admin_email");
                if (admin_email == null) {
                    admin_email = "";
                }
                String to_email = res1.getString("to_email");
                if (to_email == null) {
                    to_email = "";
                }
                String stat_forwarded_by = "";
                if (status.equals("ca_pending") && !applicant_email.isEmpty()) {
                    stat_forwarded_by = "a";
                } else if (status.equals("coordinator_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty() && !support_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (ca_email.isEmpty() && support_email.isEmpty()) {
                        stat_forwarded_by = "a";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else {
                        stat_forwarded_by = "sup";
                    }
                } else if (status.equals("support_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else {
                        stat_forwarded_by = "a";
                    }
                } else if (status.equals("mail-admin_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty() && !support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (!ca_email.isEmpty() && !support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && !support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && !support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else if (ca_email.isEmpty() && support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "a";
                    }
                }
                // String[] datetime = res1.getString("to_datetime").split(" ");
                frmObj = new Forms(res1.getString("track_id"), res1.getString("form_name"),
                        res1.getString("registration_no"), fetchStatTypeString(res1.getString("status")),
                        stat_forwarded_by, "",
                        "", "",
                        "", res1.getString("to_datetime"), showAction, showCreateAction, showBulkLink); // 31stjan19

                frmObj.setShowBulkCreateAction(showBulkCreateAction);// line added by pr on 13thdec17
                frmObj.setShowBulkUpdateAction(showBulkUpdateAction);// line added by pr on 12thdec19    
                showQueryRespone = queryRaiseStatusNew(res1.getString("registration_no")); // added by rahul june 2021
                frmObj.setShowQueryResponse(showQueryRespone);
                frmObj.setIsRaiseQueryEnabled(raiseQueryBtn);

//                showQuery = queryRaiseStatus(res1.getString("registration_no")); // 31stjan19
//                frmObj.setShowQuery(showQuery);
//               showQueryBy = queryRaiseStatusby(res1.getString("registration_no")); // march 2021
//                frmObj.setShowQueryBy(showQueryBy);
                // start, code added by pr on 6thaug19
                if (res1.getString("on_hold").equalsIgnoreCase("y")) {
                    frmObj.setOn_hold(true);
                } else {
                    frmObj.setOn_hold(false);
                }

                // end, code added by pr on 6thaug19
                //frmObj = updateFormsObj(frmObj);
                // start, code added by pr on 18thsep19
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchDataPageExport STARTTTT callUpdateObj value is ");
                frmObj.set_mobile(res1.getString("applicant_mobile"));

                frmObj.setName(res1.getString("applicant_name"));

                frmObj.setEmail(res1.getString("applicant_email"));
                frmObj.set_stat_forwarded_to_user(res1.getString("to_email")); // line added by pr on 13thnov19

                boolean callUpdateObj = false;

                if (res1.getString("form_name").equals(Constants.IP_FORM_KEYWORD)) {
                    callUpdateObj = true;
                } else if (res1.getString("form_name").equals(Constants.BULK_FORM_KEYWORD)) {
                    String bulk_count = fetchBulkUsersCount(res1.getString("registration_no"));

                    frmObj.setForm_detail(bulk_count);
                }

                app_user_type = "";

                app_ca_type = "";

                if (res1.getString("app_user_type") != null) {
                    if (res1.getString("app_user_type").contains("esign")) {
                        app_user_type = "Esigned";
                    } else if (res1.getString("app_user_type").contains("online")) {
                        app_user_type = "Online";
                    } else if (res1.getString("app_user_type").contains("manual_upload")) {
                        app_user_type = "Manual";

                        callUpdateObj = true;
                    }
                }

                app_user_type = "User: " + app_user_type;

                if (res1.getString("app_ca_type") != null) {
                    if (res1.getString("app_ca_type").contains("esign")) {
                        app_ca_type = "Esigned";
                    } else if (res1.getString("app_ca_type").contains("online")) {
                        app_ca_type = "Online";
                    } else if (res1.getString("app_ca_type").contains("manual_upload")) {
                        app_ca_type = "Manual";
                    }

                    if (!res1.getString("app_ca_type").equals("")) {
                        app_user_type += "<br>RO: " + app_ca_type;
                    }
                }

                frmObj.setApp_user_type(app_user_type);
                frmObj.setApp_ca_type(app_ca_type);

                String id = "actions_" + res1.getString("registration_no");
                frmObj.setActions("<div id='" + id + "'></div>");

//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchDataPageExport callUpdateObj value is " + callUpdateObj);
                // start, code added by pr on 25thjul19
                if (res1.getString("form_name").toLowerCase().contains("bulk")) {
                    showErrorSuccess = showErrorSuccess(res1.getString("registration_no"));
                }

                frmObj.setShowErrorSuccess(showErrorSuccess);
                frmObj.setSupportRemarks(res1.getString("support_remarks"));

                // end, code added by pr on 25thjul19
                if (callUpdateObj) {
                    frmObj = updateFormsObj(frmObj, res1.getString("app_user_type"), res1.getString("app_ca_type"));
                } else if (role.equals(Constants.ROLE_CA)) //if its a CA panel check for the below 
                {
                    frmObj.setAppType(app_user_type);

                    frmObj.setShowApprove(true);

                } else if (role.equals(Constants.ROLE_MAILADMIN)) //if its a Mailadmin  panel approve should not appear if else added by pr on 18thapr18
                {
                    frmObj.setShowApprove(false);
                    frmObj.setAppType("");
                } else {
                    frmObj.setShowApprove(true);
                    frmObj.setAppType("");
                }

                // end, code added by pr on 18thsep19
                if (frmObj != null) {
                    totalArr.add(frmObj);
                    frmObj = null;
                }
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  ############################### ERRROR  inside fetchDataPageExport " + ex.getMessage());
            //ex.printStackTrace(); // line commented by pr on 23rdapr19
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchDataPageExport EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchDataPageExport EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchDataPageExport EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        //System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  end of fetchDataPageExport function ");

        // clear session data
        sessionMap.put("exportData", null);

        ArrayList<HashMap> exportArr = new ArrayList<HashMap>();

        //exportArr.add(firstElement);
        //System.out.println(" firstelemnt hashmap value is "+firstElement);
        String strToReplace = "";

        String stat_type = "";

        String app_user_type = "";

        for (Forms frm : totalArr) // iterate over the array and convert the values into hashmap with key and value, the first row of array contains the hashmap of all the keys 
        {
            HashMap<String, String> nextElem = new HashMap<String, String>();

            nextElem.put("stat_reg_no", frm.get_stat_reg_no());

            nextElem.put("email", frm.getEmail());

            strToReplace = "<span class='" + frm.get_stat_reg_no() + "_status'>";

            // below 3 lines are added to remove span already added 
            stat_type = frm.get_stat_type();

            stat_type = stat_type.replace(strToReplace, "");

            stat_type = stat_type.replace("</span>", "");

            nextElem.put("stat_type", stat_type);

            app_user_type = frm.getApp_user_type();

            app_user_type = app_user_type.replace("<br>", ", ");

            nextElem.put("app_user_type", app_user_type);

            nextElem.put("stat_createdon", frm.get_stat_createdon());

            exportArr.add(nextElem);

            //System.out.println("fetchDataPageExport nextElem hashmap value is " + nextElem);
        }

        sessionMap.put("exportData", exportArr);

        return totalArr;
    }

    // below function added by pr on 30thjul19, converting calendar date in mm/dd/yyyy into DB Date i.e yyyy-mm-dd
    public String returnDBDate(String calDate) {
        String finalDate = "";

        String[] arr = calDate.split("/");

        finalDate = arr[2] + "-" + arr[0] + "-" + arr[1];

        return finalDate;
    }

    //modified by AT on 2ndmay19
    public ArrayList<Forms> fetchDataPage(String search, String type, String start, String limit, String formName) {
        ArrayList<String> queryRaiseData = null;

        String querytype = "";

        String querystart = null, queryend = null;

        if (querydate != null && !querydate.equals("")) {
            String[] queryArr = querydate.split(" - ");

            querystart = returnDBDate(queryArr[0]); // mm/dd/yyyy as input yyyy-mm-dd as output

            queryend = returnDBDate(queryArr[1]); // mm/dd/yyyy as input yyyy-mm-dd as output    

        }

        if (queryfilter != null && queryfilter.length > 0) {
//            for (String str : queryfilter) {
//                System.out.println(" inside getDataPage query filter loop " + str);
//            }

            querytype = queryfilter[0];
        }

        if (querytype.equals("query")) // if query raise data needs to be shown
        {
            queryRaiseData = fetchQueryRaisedForms(querystart, queryend);
        } else if (querytype.equals("query_ans")) // if added by pr on 27thjun19
        {
            queryRaiseData = fetchQueryAnsweredForms(querystart, queryend);
        }

        // end code added by pr on 30thjul19
        // start, code added by pr on 6thaug19
        boolean showOnHold = false;

        if (type.equals("onhold")) {
            type = "pending";
            showOnHold = true;
        }

        // end, code added by pr on 6thaug19        
        // end, code added by pr on 26thjun19
        long startTime = System.currentTimeMillis();
        //System.out.println(" inside get datapage role is " + role);
        ArrayList<Forms> totalArr = new ArrayList<Forms>();
        ArrayList<Forms> totalArrForExport = new ArrayList<Forms>();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        if (supForms != null) {

            System.out.println(" supForms in data page is " + supForms);

            if (supForms.size() > 0) {
                csvForms = String.join(",", supForms);
            }
        }

        System.out.println(" csvForms in data page is " + csvForms);

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
            // applicant details added by pr on 18thsep19
            String qry = "SELECT status,form_name,track_id,registration_no,to_datetime,applicant_email,ca_email,coordinator_email,support_email,da_email,admin_email,"
                    + "to_email,app_ca_type,app_user_type, on_hold,applicant_name,applicant_mobile,applicant_email,support_remarks FROM final_audit_track WHERE 1 "; // on_hold column added by pr on 6thaug19

            if (showOnHold) {
                qry += " AND ( on_hold = 'y' "; // so as to get only those applications which are on hold   // ( added by pr on 7thjan2020                   
            }

//            else {
//
//                if (type.equals("pending") || type.equals("new")) // if we are showing the pending or new requests then show only unhold request but in total all must be visible
//                {
//                    qry += " AND  on_hold = 'n' "; // so as to get only those applications which are NOT on hold    , in case of pending requests
//                }
//            }
            if (Constants.CA_PENDING.contains(search)) {
                statusString = "ca_pending";
            } else if (Constants.COORDINATOR_PENDING.contains(search)) {
                statusString = "coordinator_pending";
            } else if (Constants.SUPPORT_PENDING.contains(search)) {
                statusString = "support_pending";
            } else if (Constants.MAIL_ADMIN_PENDING.contains(search)) {
                statusString = "mail-admin_pending";
            } else if (Constants.CA_REJECTED.contains(search)) {
                statusString = "ca_rejected_pending";
            } else if (Constants.COORDINATOR_REJECTED.contains(search)) {
                statusString = "coordinator_rejected";
            } else if (Constants.SUPPORT_REJECTED.contains(search)) {
                statusString = "support_rejected";
            } else if (Constants.MAIL_ADMIN_REJECTED.contains(search)) {
                statusString = "mail-admin_rejected";
            } else if (Constants.COMPLETED.contains(search)) {
                statusString = "completed";
            }

            if (!search.equals("")) {
                statusString = "";
                if (search.contains("re") || search.contains("fo") || search.contains("no")) {
                    statusString = "ca";
                } else if (search.contains("co")) {
                    statusString = "coordinator";
                } else if (search.contains("su")) {
                    statusString = "support";
                } else if (search.contains("ad")) {
                    statusString = "mail-admin";
                }

                if (statusString.equals("")) {
                    statusString = search;
                }

                qry += " AND ( applicant_email LIKE ?  OR applicant_name LIKE ? OR applicant_mobile LIKE ? OR registration_no LIKE ?  OR to_datetime LIKE ? OR status LIKE '" + statusString + "%' OR app_user_type LIKE ? OR app_ca_type LIKE ?) ";
            }
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
                                    qry += " ( status = '" + pendingStr + "' and to_email  = '" + s.toString().trim() + "') ";
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
                    //System.out.println("QUERY : " + qry);
                }

                //modified by AT to improvise query on 3rd Sep 2019
                // need to get id from coordinator id
                if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    Set<String> coordsIds = db.fetchCoIds(email);

                    String sb = "";
                    if (coordsIds.size() != 0) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }

                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        //System.out.println("my sb:" + sb);

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
            if (!formName.equals("all")) {
                if (supForms != null) {
                    if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                        if (supForms.contains("'" + formName + "'")) {
                            if (formName.contains("nkn")) {
                                qry += " and  form_name like ? ";
                            } else {
                                qry += " and  form_name = ? ";
                            }
                        }
                    } else if (formName.contains("nkn")) {
                        qry += " and  form_name like ? ";
                    } else {
                        qry += " and  form_name = ? ";
                    }
                } else if (formName.contains("nkn")) {
                    qry += " and  form_name like ? ";
                } else {
                    qry += " and  form_name = ? ";
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                if (!csvForms.isEmpty()) {
                    qry += " and  form_name in (" + csvForms + ") "; // line added by pr on 21stfeb18
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) // else if added by pr on 24thoct19
            {
                //System.out.println(" before considering forms for coordinator query is " + qry);

                if (roles_service != null) {
                    qry += " and form_name in ( ";

                    int i = 1;
                    for (String frm : roles_service) {
                        if (i > 1) {
                            qry += ",";
                        }

                        qry += "'" + frm + "'";

                        i++;
                    }

                    qry += ")";

                }

                //System.out.println(" AFTER considering forms for coordinator query is " + qry);
            }

            // start, code added by pr on 26thjun19
            // put a check on form reg nos here in case of query raise filter , below code added by pr on 26thjun19
            if (queryRaiseData != null) {
                String arrString = String.join("\",\"", queryRaiseData);

                arrString = "\"" + arrString + "\"";

                qry += " AND registration_no in (" + arrString + ") ";
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

            // start, code added by pr on 7thjan2020
            System.out.println(" role value is " + role + "BEFOREEEEEEEEE inside the on hold where clause in fetchDataPage query 2 ");

            if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                System.out.println(" inside the on hold where clause in fetchDataPage query ");

                qry += " AND on_hold = 'n' ";
            }

            // end, code added by pr on 7thjan2020  
            if (exportFlag) {
                qry += " ORDER BY to_datetime DESC ";
            } else {
                qry += " ORDER BY to_datetime DESC LIMIT " + start + ", " + limit;
            }
            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;
            if (!search.equals("")) {
                for (int i = 0; i < 7; i++) {
                    pscnt = pscnt + 1;
                    ps.setString(pscnt, "" + search + "%");
                }
            }
            if (!formName.equals("all")) {
                if (supForms != null) {

                    System.out.println("NITIN TEST :::" + supForms);

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
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchDataPage getDataPage QUERY is " + ps);
            res1 = ps.executeQuery();
            Forms frmObj = null;

            String showErrorSuccess = ""; // line added by pr on 25thjul19            

            while (res1.next()) {

                showErrorSuccess = ""; // line added by pr on 25thjul19                            

                showAction = fetchRoleBasedShowAction(res1.getString("status"), res1.getString("form_name"));
                if (!res1.getString("form_name").equalsIgnoreCase(Constants.DOR_EXT_RETIRED_FORM_KEYWORD)) {
                    raiseQueryBtn = isRaiseQueryEnabled(fetch_role(), res1.getString("registration_no"));
                } else {
                    raiseQueryBtn = false;
                }
                System.out.println(ServletActionContext.getRequest().getSession().getId() + "raiseQueryBtn value in fetchDataPage" + raiseQueryBtn);
                String status = res1.getString("status");
                if (status == null) {
                    status = "";
                }
                String applicant_email = res1.getString("applicant_email");
                if (applicant_email == null) {
                    applicant_email = "";
                }
                String ca_email = res1.getString("ca_email");
                if (ca_email == null) {
                    ca_email = "";
                }
                String support_email = res1.getString("support_email");
                if (support_email == null) {
                    support_email = "";
                }
                String coordinator_email = res1.getString("coordinator_email");
                if (coordinator_email == null) {
                    coordinator_email = "";
                }
                String da_email = res1.getString("da_email");
                if (da_email == null) {
                    da_email = "";
                }
                String admin_email = res1.getString("admin_email");
                if (admin_email == null) {
                    admin_email = "";
                }
                String to_email = res1.getString("to_email");
                if (to_email == null) {
                    to_email = "";
                }
                String stat_forwarded_by = "";
                if (status.equals("ca_pending") && !applicant_email.isEmpty()) {
                    stat_forwarded_by = "a";
                } else if (status.equals("coordinator_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty() && !support_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (ca_email.isEmpty() && support_email.isEmpty()) {
                        stat_forwarded_by = "a";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else {
                        stat_forwarded_by = "sup";
                    }
                } else if (status.equals("support_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else {
                        stat_forwarded_by = "a";
                    }
                } else if (status.equals("mail-admin_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty() && !support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (!ca_email.isEmpty() && !support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && !support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && !support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else if (ca_email.isEmpty() && support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "a";
                    }
                }
                // String[] datetime = res1.getString("to_datetime").split(" ");
                frmObj = new Forms(res1.getString("track_id"), res1.getString("form_name"),
                        res1.getString("registration_no"), fetchStatTypeString(res1.getString("status")),
                        stat_forwarded_by, "",
                        "", "",
                        "", res1.getString("to_datetime"), showAction, showCreateAction, showBulkLink); // 31stjan19

                frmObj.setShowBulkCreateAction(showBulkCreateAction);// line added by pr on 13thdec17
                frmObj.setShowBulkUpdateAction(showBulkUpdateAction);// line added by pr on 12thdec19  
                showQueryRespone = queryRaiseStatusNew(res1.getString("registration_no")); // added by rahul june 2021
                frmObj.setShowQueryResponse(showQueryRespone);
                frmObj.setIsRaiseQueryEnabled(raiseQueryBtn);
//                showQuery = queryRaiseStatus(res1.getString("registration_no")); // 31stjan19
//                frmObj.setShowQuery(showQuery);
//                showQueryBy = queryRaiseStatusby(res1.getString("registration_no")); // march 2021
//                frmObj.setShowQueryBy(showQueryBy);
                // start, code added by pr on 6thaug19
                if (res1.getString("on_hold").equalsIgnoreCase("y")) {
                    frmObj.setOn_hold(true);
                } else {
                    frmObj.setOn_hold(false);
                }

                // end, code added by pr on 6thaug19
                //frmObj = updateFormsObj(frmObj);
                // start, code added by pr on 18thsep19
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchDataPage STARTTTT callUpdateObj value is ");
                frmObj.set_mobile(res1.getString("applicant_mobile"));

                frmObj.setName(res1.getString("applicant_name"));

                frmObj.setEmail(res1.getString("applicant_email"));
                frmObj.set_stat_forwarded_to_user(res1.getString("to_email")); // line added by pr on 13thnov19

                boolean callUpdateObj = false;

                if (res1.getString("form_name").equals(Constants.IP_FORM_KEYWORD)) {
                    callUpdateObj = true;
                } else if (res1.getString("form_name").equals(Constants.BULK_FORM_KEYWORD)) {
                    String bulk_count = fetchBulkUsersCount(res1.getString("registration_no"));

                    frmObj.setForm_detail(bulk_count);
                }

                app_user_type = "";

                app_ca_type = "";

                if (res1.getString("app_user_type") != null) {
                    if (res1.getString("app_user_type").contains("esign")) {
                        app_user_type = "Esigned";
                    } else if (res1.getString("app_user_type").contains("online")) {
                        app_user_type = "Online";
                    } else if (res1.getString("app_user_type").contains("manual")) {
                        app_user_type = "Manual";
                        callUpdateObj = true;
                    }
                }

                app_user_type = "User: " + app_user_type;

                if (res1.getString("app_ca_type") != null) {
                    if (res1.getString("app_ca_type").contains("esign")) {
                        app_ca_type = "Esigned";
                    } else if (res1.getString("app_ca_type").contains("online")) {
                        app_ca_type = "Online";
                    } else if (res1.getString("app_ca_type").contains("manual")) {
                        app_ca_type = "Manual";
                    }

                    if (!res1.getString("app_ca_type").equals("")) {
                        app_user_type += "<br>RO: " + app_ca_type;
                    }
                }

                frmObj.setApp_user_type(app_user_type);
                frmObj.setApp_ca_type(app_ca_type);

                String id = "actions_" + res1.getString("registration_no");
                frmObj.setActions("<div id='" + id + "'></div>");

//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchDataPage callUpdateObj value is " + callUpdateObj);
                // start, code added by pr on 25thjul19
                if (res1.getString("form_name").toLowerCase().contains("bulk")) {
                    showErrorSuccess = showErrorSuccess(res1.getString("registration_no"));
                }

                frmObj.setShowErrorSuccess(showErrorSuccess);
                frmObj.setSupportRemarks(res1.getString("support_remarks"));

                // end, code added by pr on 25thjul19
                if (callUpdateObj) {
                    frmObj = updateFormsObj(frmObj, res1.getString("app_user_type"), res1.getString("app_ca_type"));
                } else if (role.equals(Constants.ROLE_CA)) //if its a CA panel check for the below 
                {
                    frmObj.setAppType(app_user_type);

                    frmObj.setShowApprove(true);

                } else if (role.equals(Constants.ROLE_MAILADMIN)) //if its a Mailadmin  panel approve should not appear if else added by pr on 18thapr18
                {
                    frmObj.setShowApprove(false);
                    frmObj.setAppType("");
                } else {
                    frmObj.setShowApprove(true);
                    frmObj.setAppType("");
                }

                // end, code added by pr on 18thsep19
                if (frmObj != null) {
                    totalArr.add(frmObj);
                    frmObj = null;
                }
            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  ############################### ERRROR  inside getDataPage " + ex.getMessage());
            //ex.printStackTrace(); // line commented by pr on 23rdapr19
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        long endTime = System.currentTimeMillis();

        if (exportFlag) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  exporting started ");
            sessionMap.put("exportData", null);
            ArrayList<HashMap> exportArr = new ArrayList<>();

            String strToReplace = "";
            String stat_type = "";
            String app_user_type = "";
            // iterate over the array and convert the values into hashmap with key and value, the first row of array contains the hashmap of all the keys 
            for (Forms frm : totalArr) {
                HashMap<String, String> nextElem = new HashMap<>();
                nextElem.put("stat_reg_no", frm.get_stat_reg_no());
                nextElem.put("email", frm.getEmail());
                strToReplace = "<span class='" + frm.get_stat_reg_no() + "_status'>";
                stat_type = frm.get_stat_type();
                stat_type = stat_type.replace(strToReplace, "");
                stat_type = stat_type.replace("</span>", "");
                nextElem.put("stat_type", stat_type);
                app_user_type = frm.getApp_user_type();
                app_user_type = app_user_type.replace("<br>", ", ");
                nextElem.put("app_user_type", app_user_type);
                nextElem.put("stat_createdon", frm.get_stat_createdon());
                exportArr.add(nextElem);
            }
            sessionMap.put("exportData", exportArr);
            //exportData(exportArr);
        }

        boolean flagToReturnExportedData = false;
        if (sessionMap.get("exportData") == null) {
            exportFlag = true;
            flagToReturnExportedData = true;
            totalArrForExport = totalArr;
            fetchDataPage(search, type, start, limit, formName);
        }

        if (flagToReturnExportedData) {
            return totalArrForExport;
        } else {
            return totalArr;
        }
    }

    public String exportData() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside export function ");
        String excelFileName = "";
        String sheetName = "";//name of sheet
        boolean flag = true;
        ArrayList<HashMap> exportArr = (ArrayList<HashMap>) sessionMap.get("exportData");
        System.out.println("ARRAY ::: " + exportArr);
        try {
            if (exportArr.size() > 0 && exportArr.get(0) != null) {
                ExcelCreator excelCreator = new ExcelCreator();
                excelFileName = "/tmp/data.xls";//name of excel file
                sheetName = "Export Data";//name of sheet
                excelCreator.createWorkbookHash(exportArr, excelFileName, sheetName);
            }
            File file = new File(excelFileName);
            if (file.exists()) {
                fileInputStream = new NewFileInputStream(file);
                if (file.exists()) {
                } else {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside file doesnt exists ");
                }
            } else {
                excelFileName = "/tmp/norecord.xls";
                //filename = "F:\\norecord.xls";
                ExcelCreator excelCreator = new ExcelCreator();
                ArrayList arr = new ArrayList();
                HashMap hm = new HashMap();
                hm.put("No Record Found", "");
                arr.add(hm);
                excelCreator.createWorkbookHash(arr, excelFileName, "No Record");
                file = new File(excelFileName);
                fileInputStream = new NewFileInputStream(file);
            }
        } catch (Exception ex) {
            flag = false;
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    // below function added by pr on 25thjul19    
    public String showErrorSuccess(String reg_no) {
        PreparedStatement ps = null;
        ResultSet res1 = null;

        ArrayList<String> statTypeArr = new ArrayList<String>();

        String returnString = "";

        try {
            conSlave = DbConnection.getSlaveConnection();

            String qry = "SELECT stat_type FROM status WHERE stat_reg_no = ? order by stat_id desc ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, reg_no);

            res1 = ps.executeQuery();

            while (res1.next()) {

                statTypeArr.add(res1.getString("stat_type").toLowerCase());

            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers EXCEPTION 1 " + e.getMessage());
                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers EXCEPTION 2 " + e.getMessage());
                }
            }
        }

        //if (statTypeArr.contains("coordinator_pending")) {
        if (statTypeArr.contains("coordinator_pending") || statTypeArr.contains("ca_pending")) { // line modified by pr on 6thdec19
            returnString = "error";
        }

        if (statTypeArr.contains("mail-admin_pending")) {
            returnString += "success";
        }

        return returnString;

    }

    //modified by AT on 2ndmay19
    public ArrayList<Forms> fetchFilteredDataPage(String search, String[] type, String start, String limit, String[] formName) {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  start of fetchFilteredDataPage function ");

        ArrayList<String> queryRaiseData = null;
        ArrayList<String> queryAnsweredData = null;

        // start below code added by pr on 30thjul19
        String querytype = "";

        String querystart = null, queryend = null;

        if (querydate != null && !querydate.equals("")) {
            String[] queryArr = querydate.split(" - ");
            querystart = returnDBDate(queryArr[0]); // mm/dd/yyyy as input yyyy-mm-dd as output
            queryend = returnDBDate(queryArr[1]); // mm/dd/yyyy as input yyyy-mm-dd as output    
        }

        if (queryfilter != null && queryfilter.length > 0) {
//            for (String str : queryfilter) {
//                System.out.println(" inside fetchFilteredDataPage query filter loop " + str);
//            }

            querytype = queryfilter[0];
        }

        // start, code added by pr on 18thsep2020
        System.out.println(" queryfilter size is " + queryfilter.length + " querytype value is " + querytype);

        if (querytype.equalsIgnoreCase("query")) {
            queryRaiseData = fetchQueryRaisedForms(querystart, queryend);
        } else if (querytype.equalsIgnoreCase("query_ans")) {
            queryRaiseData = new ArrayList<>();
            queryAnsweredData = fetchQueryAnsweredForms(querystart, queryend);
        }
        // end, code added by pr on 18thsep2020

        // end code added by pr on 30thjul19
        long startTime = System.currentTimeMillis();
        //System.out.println(" inside get fetchFilteredDataPage role is " + role);
        ArrayList<Forms> totalArr = new ArrayList<Forms>();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        boolean showOnHold = false;
        Set<String> coordsIds = null;

        List<String> typeList = new ArrayList<>(Arrays.asList(type));
        //if (typeList.contains("onhold")) {
        if (typeList.contains("onhold") || typeList.contains("offhold")) {// line modified by pr on 27thapr2020
            showOnHold = true;
        }

        if (supForms != null) {

            System.out.println(" inside supform not null " + supForms);

            if (supForms.size() > 0) {
                csvForms = String.join(",", supForms);
            }
        }

        System.out.println(" csvForms is " + csvForms);

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
            // applicant details added by pr on 18thsep19
            String qry = "SELECT status,form_name,track_id,registration_no,to_datetime,applicant_email,ca_email,coordinator_email,support_email,da_email,admin_email,"
                    + "to_email,app_ca_type,app_user_type, on_hold,applicant_name,applicant_mobile,applicant_email,support_remarks FROM final_audit_track WHERE 1 "; // on_hold column added by pr on 6thaug19

            if (Constants.CA_PENDING.contains(search)) {
                statusString = "ca_pending";
            } else if (Constants.COORDINATOR_PENDING.contains(search)) {
                statusString = "coordinator_pending";
            } else if (Constants.SUPPORT_PENDING.contains(search)) {
                statusString = "support_pending";
            } else if (Constants.MAIL_ADMIN_PENDING.contains(search)) {
                statusString = "mail-admin_pending";
            } else if (Constants.CA_REJECTED.contains(search)) {
                statusString = "ca_rejected_pending";
            } else if (Constants.COORDINATOR_REJECTED.contains(search)) {
                statusString = "coordinator_rejected";
            } else if (Constants.SUPPORT_REJECTED.contains(search)) {
                statusString = "support_rejected";
            } else if (Constants.MAIL_ADMIN_REJECTED.contains(search)) {
                statusString = "mail-admin_rejected";
            } else if (Constants.COMPLETED.contains(search)) {
                statusString = "completed";
            }

            if (!search.equals("")) {
                statusString = "";
                if (search.contains("re") || search.contains("fo") || search.contains("no")) {
                    statusString = "ca";
                } else if (search.contains("co")) {
                    statusString = "coordinator";
                } else if (search.contains("su")) {
                    statusString = "support";
                } else if (search.contains("ad")) {
                    statusString = "mail-admin";
                }

                if (statusString.equals("")) {
                    statusString = search;
                }

                qry += " AND ( applicant_email LIKE ?  OR applicant_name LIKE ? OR applicant_mobile LIKE ? OR registration_no LIKE ?  OR to_datetime LIKE ? OR status LIKE '%" + statusString + "%' OR app_user_type LIKE ? OR app_ca_type LIKE ?) ";
            }
            boolean condition_flag = false;
            String condition_str = "";

            if (showOnHold) {

                if (typeList.contains("onhold")) // if around and else if added by pr on 27thapr2020
                {
                    qry += "and ( on_hold = 'y' ";// ( added by pr on 7thjan2020          
                } else if (typeList.contains("offhold")) {
                    qry += "and ( on_hold = 'n' ";// ( added by pr on 7thjan2020          
                }
            }

            int i = 0;
            if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                coordsIds = db.fetchCoIds(email);
            }

            for (String types : type) {
                //System.out.println("1st for block : " + types);
                //if (!(types.equalsIgnoreCase("onhold") || types.equalsIgnoreCase("query") || types.equalsIgnoreCase("query_ans"))) {
                // line modified by pr on 27thapr2020
                if (!(types.equalsIgnoreCase("onhold") || types.equalsIgnoreCase("offhold") || types.equalsIgnoreCase("query") || types.equalsIgnoreCase("query_ans"))) {
                    if (i == 0) {
                        if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                            if (!coordsIds.isEmpty()) {
                                condition_str = " AND ((";
                            } else {
                                condition_str = " AND (";
                            }
                        } else {
                            condition_str = " AND (";
                        }
                    } else {
                        condition_str = "OR ";
                    }
                }
                String subQuery = generateQueryForAliases(email, types);
                switch (types) {
                    case "new":
                        qry += condition_str + " (status = '" + pendingStr + "' and DATE( to_datetime) = CURDATE() )";
                        //System.out.println(" qry 4 is " + qry);
                        i++;
                        break;
                    case "pending":
                        qry += condition_str + "( status = '" + pendingStr + "' ";
                        i++;
                        break;
                    case "completed":
                        qry += condition_str + "( status = 'completed' ";
                        i++;
                        break;
                    case "rejected":
                        qry += condition_str + "( status = '" + rejectStr + "' ";
                        i++;
                        break;
                    case "forwarded":
                        if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN)) {
                            qry += condition_str + "( status = 'completed' ";
                        } else {
                            qry += condition_str + "( (status != '" + rejectStr + "' and status != '" + pendingStr + "') ";
                        }
                        i++;
                        break;
//                    case "query":
//                        queryRaiseData = fetchQueryRaisedForms(querystart, queryend);// empty parameters passed by pr on 24thsep19
//                        break;
//                    case "query_ans":
//                        queryAnsweredData = fetchQueryAnsweredForms(querystart, queryend);// empty parameters passed by pr on 24thsep19
//                        break; // commented by pr on 18thsep2020
                }

                if (!subQuery.isEmpty()) {
                    qry += " AND " + subQuery + " ) ";
                } else {
                    qry += ")";
                }
            }
            if (i > 0) {
                qry += ")";
            }

            //System.out.println("  email value is " + email);
            boolean flagP, flagT;
            if (email != null) {
                // System.out.println(" inside email not null ");
                //modified by AT to improvise query on 3rd Sep 2019
                // need to get id from coordinator id
                if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    String sb = "";
                    if (!coordsIds.isEmpty()) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }
                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        //System.out.println("my sb:" + sb);
                        qry += " or co_id in (" + sb + ") )";
                    }
                }
            }

            condition_flag = false;
            i = 0;
            for (String form : formName) {
                i++;

                //System.out.println("FORM NAME = " + form);
                if (!form.equalsIgnoreCase("all")) {
                    if (supForms != null) {
                        if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                            if (!condition_flag) {
                                condition_str = " AND (";
                                condition_flag = true;
                            } else {
                                condition_str = " OR ";
                            }
                            if (supForms.contains("'" + form + "'")) {
                                if (form.contains("nkn")) {
                                    qry += condition_str + " form_name like ? ";
                                } else {
                                    qry += condition_str + " form_name = ? ";
                                }
                            }
                        } else if (form.contains("nkn")) {
                            if (!condition_flag) {
                                condition_str = " AND (";
                                condition_flag = true;
                            } else {
                                condition_str = " OR ";
                            }
                            qry += condition_str + " form_name like ? ";
                        } else {
                            if (!condition_flag) {
                                condition_str = " AND (";
                                condition_flag = true;
                            } else {
                                condition_str = " OR ";
                            }
                            //System.out.println(" fetchFilteredDataPage INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                            qry += condition_str + " form_name = ? ";
                        }
                    } else if (form.contains("nkn")) {
                        qry += condition_str + " form_name like ? ";
                    } else {
                        qry += condition_str + " form_name = ? ";
                    }
                } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                    if (!csvForms.isEmpty()) {
                        qry += " and form_name in (" + csvForms + ") "; // line added by pr on 21stfeb18
                    }
                } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) // else if added by pr on 24thoct19
                {
                    // System.out.println(" before considering forms for coordinator query is " + qry);

                    if (roles_service != null) {
                        qry += " and form_name in ( ";

                        int j = 1;
                        for (String frm : roles_service) {
                            if (j > 1) {
                                qry += ",";
                            }

                            qry += "'" + frm + "'";

                            j++;
                        }

                        qry += ")";

                    }

                    //System.out.println(" AFTER considering forms for coordinator query is " + qry);
                }
            }

            if (condition_flag) {
                qry += ")";
            }

            // start, code added by pr on 26thjun19
            // put a check on form reg nos here in case of query raise filter , below code added by pr on 26thjun19
            //System.out.println(" inside filtered data page function queryRaiseData value is " + queryRaiseData);
            if (queryAnsweredData != null) {
                if (queryAnsweredData.size() > 0) {
                    queryRaiseData.addAll(queryAnsweredData);
                }
            }

            if (queryRaiseData != null) {
                String arrString = String.join("\",\"", queryRaiseData);

                arrString = "\"" + arrString + "\"";
                //System.out.println("REGNUMBERS : " + arrString);

                qry += " AND registration_no in (" + arrString + ") ";
            }
            // start, code added by pr on 2ndjul19, so that 
            if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) && email.contains("rajesh.singh@nic.in") && csvForms.contains("ip")) {
                flagP = false;
                flagT = false;
                for (String types : type) {
                    if (types.equals("pending")) {
                        if (!flagP) {
                            qry += " AND to_email = 'rajesh.singh@nic.in'";
                            flagP = true;
                        }
                    } else if (!flagT) {
                        qry += " AND  ( to_email = 'rajesh.singh@nic.in' OR  ( admin_email = 'rajesh.singh@nic.in'  OR  admin_email = 'rajs@nic.in' "
                                + " OR  admin_email = 'rajesh.singh@gov.in' OR  admin_email = 'rajesh.singh@dummy.nic.in'  OR  admin_email = 'rajs@dummy.nic.in' "
                                + "OR  admin_email = 'rajesh.singh@nkn.in'  OR  admin_email = 'rajs@vastu13.nic.in' ) ) ";
                        flagT = true;
                    }
                }
            }

            if (exportFlag) {
                qry += " ORDER BY to_datetime DESC ";
            } else {
                qry += " ORDER BY to_datetime DESC LIMIT " + start + ", " + limit;
            }

            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;
            if (!search.equals("")) {
                for (i = 0; i < 7; i++) {
                    pscnt = pscnt + 1;
                    //System.out.println("PSCNT 1" + pscnt);
                    ps.setString(pscnt, "%" + search + "%");
                }
            }
            for (String form : formName) {
                // System.out.println("FORM NAME While assigning = " + form);
                if (!form.equalsIgnoreCase("all")) {
                    if (supForms != null) {
                        if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                            if (supForms.contains("'" + form + "'")) {
                                pscnt = pscnt + 1;
                                if (form.contains("nkn")) {
                                    form = "%nkn%";
                                }
                                // System.out.println("PSCNT 2 " + pscnt);
                                ps.setString(pscnt, form);
                            }
                        } else {
                            pscnt = pscnt + 1;
                            if (form.contains("nkn")) {
                                form = "%nkn%";
                            }
                            //System.out.println("PSCNT 3 " + pscnt);
                            ps.setString(pscnt, form);
                        }
                    } else {
                        pscnt = pscnt + 1;
                        if (form.contains("nkn")) {
                            form = "%nkn%";
                        }
                        //System.out.println("PSCNT 4 " + pscnt);
                        ps.setString(pscnt, form);
                    }
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchFilteredDataPage getDataPage QUERY is " + ps);
            res1 = ps.executeQuery();
            Forms frmObj = null;
            String showErrorSuccess = ""; // line added by pr on 25thjul19            
            while (res1.next()) {

                showErrorSuccess = ""; // line added by pr on 25thjul19               

                showAction = fetchRoleBasedShowAction(res1.getString("status"), res1.getString("form_name"));
                raiseQueryBtn = isRaiseQueryEnabled(fetch_role(), res1.getString("registration_no"));

                String status = res1.getString("status");
                if (status == null) {
                    status = "";
                }
                String applicant_email = res1.getString("applicant_email");
                if (applicant_email == null) {
                    applicant_email = "";
                }
                String ca_email = res1.getString("ca_email");
                if (ca_email == null) {
                    ca_email = "";
                }
                String support_email = res1.getString("support_email");
                if (support_email == null) {
                    support_email = "";
                }
                String coordinator_email = res1.getString("coordinator_email");
                if (coordinator_email == null) {
                    coordinator_email = "";
                }
                String da_email = res1.getString("da_email");
                if (da_email == null) {
                    da_email = "";
                }
                String admin_email = res1.getString("admin_email");
                if (admin_email == null) {
                    admin_email = "";
                }
                String to_email = res1.getString("to_email");
                if (to_email == null) {
                    to_email = "";
                }
                String stat_forwarded_by = "";
                if (status.equals("ca_pending") && !applicant_email.isEmpty()) {
                    stat_forwarded_by = "a";
                } else if (status.equals("coordinator_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty() && !support_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (ca_email.isEmpty() && support_email.isEmpty()) {
                        stat_forwarded_by = "a";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else {
                        stat_forwarded_by = "sup";
                    }
                } else if (status.equals("support_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else {
                        stat_forwarded_by = "a";
                    }
                } else if (status.equals("mail-admin_pending") && !applicant_email.isEmpty()) {
                    if (!ca_email.isEmpty() && !support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (!ca_email.isEmpty() && !support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && !support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && support_email.isEmpty() && !coordinator_email.isEmpty()) {
                        stat_forwarded_by = "co";
                    } else if (ca_email.isEmpty() && !support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "sup";
                    } else if (!ca_email.isEmpty() && support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "ca";
                    } else if (ca_email.isEmpty() && support_email.isEmpty() && coordinator_email.isEmpty()) {
                        stat_forwarded_by = "a";
                    }
                }
                //String[] datetime = res1.getString("to_datetime").split(" ");
                frmObj = new Forms(res1.getString("track_id"), res1.getString("form_name"),
                        res1.getString("registration_no"), fetchStatTypeString(res1.getString("status")),
                        stat_forwarded_by, "",
                        "", "",
                        "", res1.getString("to_datetime"), showAction, showCreateAction, showBulkLink); // 31stjan19

                frmObj.setShowBulkCreateAction(showBulkCreateAction);// line added by pr on 13thdec17
                frmObj.setShowBulkUpdateAction(showBulkUpdateAction);// line added by pr on 12thdec19      
                showQueryRespone = queryRaiseStatusNew(res1.getString("registration_no")); //  added by rahul june 2021
                frmObj.setShowQueryResponse(showQueryRespone);
                frmObj.setIsRaiseQueryEnabled(raiseQueryBtn);
//                showQuery = queryRaiseStatus(res1.getString("registration_no")); // 31stjan19
//                frmObj.setShowQuery(showQuery);
//                showQueryBy = queryRaiseStatusby(res1.getString("registration_no")); // march 2021
//                frmObj.setShowQueryBy(showQueryBy);
                // start, code added by pr on 6thaug19
                if (res1.getString("on_hold").equalsIgnoreCase("y")) {
                    frmObj.setOn_hold(true);
                } else {
                    frmObj.setOn_hold(false);
                }

                // end, code added by pr on 6thaug19                
                //frmObj = updateFormsObj(frmObj);
                // start, code added by pr on 18thsep19
                frmObj.set_mobile(res1.getString("applicant_mobile"));

                frmObj.setName(res1.getString("applicant_name"));

                frmObj.setEmail(res1.getString("applicant_email"));
                frmObj.set_stat_forwarded_to_user(res1.getString("to_email")); // line added by pr on 13thnov19

                boolean callUpdateObj = false;

                if (res1.getString("form_name").equals(Constants.IP_FORM_KEYWORD)) {
                    callUpdateObj = true;
                } else if (res1.getString("form_name").equals(Constants.BULK_FORM_KEYWORD)) {
                    String bulk_count = fetchBulkUsersCount(res1.getString("registration_no"));

                    frmObj.setForm_detail(bulk_count);
                }

                app_user_type = "";

                app_ca_type = "";

                if (res1.getString("app_user_type") != null) {
                    if (res1.getString("app_user_type").contains("esign")) {
                        app_user_type = "Esigned";
                    } else if (res1.getString("app_user_type").contains("online")) {
                        app_user_type = "Online";
                    } else if (res1.getString("app_user_type").contains("manual_upload")) {
                        app_user_type = "Manual";

                        callUpdateObj = true;
                    }
                }

                app_user_type = "User: " + app_user_type;

                if (res1.getString("app_ca_type") != null) {
                    if (res1.getString("app_ca_type").contains("esign")) {
                        app_ca_type = "Esigned";
                    } else if (res1.getString("app_ca_type").contains("online")) {
                        app_ca_type = "Online";
                    } else if (res1.getString("app_ca_type").contains("manual_upload")) {
                        app_ca_type = "Manual";
                    }

                    if (!res1.getString("app_ca_type").equals("")) {
                        app_user_type += "<br>RO: " + app_ca_type;
                    }
                }

                frmObj.setApp_user_type(app_user_type);
                frmObj.setApp_ca_type(app_ca_type);

                String id = "actions_" + res1.getString("registration_no");
                frmObj.setActions("<div id='" + id + "'></div>");

//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  inside fetchFilteredDataPage callUpdateObj value is " + callUpdateObj);
                // start, code added by pr on 25thjul19
                if (res1.getString("form_name").toLowerCase().contains("bulk")) {
                    showErrorSuccess = showErrorSuccess(res1.getString("registration_no"));
                }

                frmObj.setShowErrorSuccess(showErrorSuccess);
                frmObj.setSupportRemarks(res1.getString("support_remarks"));
                // end, code added by pr on 25thjul19
                if (callUpdateObj) {
                    frmObj = updateFormsObj(frmObj, res1.getString("app_user_type"), res1.getString("app_ca_type"));
                } else if (role.equals(Constants.ROLE_CA)) //if its a CA panel check for the below 
                {
                    frmObj.setAppType(app_user_type);

                    frmObj.setShowApprove(true);

                } else if (role.equals(Constants.ROLE_MAILADMIN)) //if its a Mailadmin  panel approve should not appear if else added by pr on 18thapr18
                {
                    frmObj.setShowApprove(false);
                    frmObj.setAppType("");
                } else {
                    frmObj.setShowApprove(true);
                    frmObj.setAppType("");
                }

                // end, code added by pr on 18thsep19
                if (frmObj != null) {
                    totalArr.add(frmObj);
                    frmObj = null;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  ############################### ERRROR  inside getDataPage " + ex.getMessage());
            //ex.printStackTrace(); // line commented by pr on 23rdapr19
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        if (exportFlag) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  exporting started ");
            sessionMap.put("exportData", null);
            ArrayList<HashMap> exportArr = new ArrayList<>();

            String strToReplace = "";
            String stat_type = "";
            String app_user_type = "";
            // iterate over the array and convert the values into hashmap with key and value, the first row of array contains the hashmap of all the keys 
            for (Forms frm : totalArr) {
                HashMap<String, String> nextElem = new HashMap<>();
                nextElem.put("stat_reg_no", frm.get_stat_reg_no());
                nextElem.put("email", frm.getEmail());
                strToReplace = "<span class='" + frm.get_stat_reg_no() + "_status'>";
                stat_type = frm.get_stat_type();
                stat_type = stat_type.replace(strToReplace, "");
                stat_type = stat_type.replace("</span>", "");
                nextElem.put("stat_type", stat_type);
                app_user_type = frm.getApp_user_type();
                app_user_type = app_user_type.replace("<br>", ", ");
                nextElem.put("app_user_type", app_user_type);
                nextElem.put("stat_createdon", frm.get_stat_createdon());
                exportArr.add(nextElem);
            }
            sessionMap.put("exportData", exportArr);
            //exportData(exportArr);
        }
        return totalArr;
    }

    private String generateQueryForAliases(List<String> email, String types) {
        String subQuery = "(";
        if (email != null) {
            //System.out.println(" inside generateQueryForAliases email not null ");
            for (Object s : email) {
                // System.out.println("2nd for block : " + s);
                switch (role) {
                    case Constants.ROLE_CA:
                        switch (types) {
                            case "completed":
                            case "forwarded":
                            case "rejected":
                                subQuery += " ca_email = '" + s.toString().trim() + "' OR ";
                                break;
                            case "pending":
                            case "new":
                                subQuery += " to_email  = '" + s.toString().trim() + "' OR ";
                                break;
                        }
                        break;
                    case Constants.ROLE_CO:
                        switch (types) {
                            case "completed":
                            case "forwarded":
                            case "rejected":
                                subQuery += " ( coordinator_email = '" + s.toString().trim() + "' and co_id = 0 ) OR ";
                                break;
                            case "pending":
                            case "new":
                                subQuery += " ( to_email  = '" + s.toString().trim() + "'  and co_id = 0 ) OR ";
                                break;
                        }
                        break;
                }
            }
        }

        subQuery = subQuery.replaceAll("\\s* OR \\s*$", "");
        if (!subQuery.equals("(")) {
            subQuery += ")";
        } else {
            subQuery = "";
        }
        //System.out.println("generateQueryForAliases SUBQUERY : " + subQuery);
        return subQuery;
    }

    //modified by AT on 2ndmay19
    public int fetchFilteredCount(String search, String[] type, String start, String limit, String[] formName) {
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  start of getDataPage function ");

        // start below code added by pr on 30thjul19
        String querytype = "";

        String querystart = null, queryend = null;

        if (querydate != null && !querydate.equals("")) {
            String[] queryArr = querydate.split(" - ");

            querystart = returnDBDate(queryArr[0]); // mm/dd/yyyy as input yyyy-mm-dd as output

            queryend = returnDBDate(queryArr[1]); // mm/dd/yyyy as input yyyy-mm-dd as output    

        }

        if (queryfilter != null && queryfilter.length > 0) {
//            for (String str : queryfilter) {
//                System.out.println(" inside getDataPage query filter loop " + str);
//            }

            querytype = queryfilter[0];
        }
        // end code added by pr on 30thjul19

        int cnt = 0;
        ArrayList<String> queryRaiseData = null;
        ArrayList<String> queryAnsweredData = null;

        // start, code added by pr on 18thsep2020
        System.out.println(" queryfilter size is " + queryfilter.length + " querytype value is " + querytype);

        if (querytype.equalsIgnoreCase("query")) {
            queryRaiseData = fetchQueryRaisedForms(querystart, queryend);
        } else if (querytype.equalsIgnoreCase("query_ans")) {
            queryRaiseData = new ArrayList<>();
            queryAnsweredData = fetchQueryAnsweredForms(querystart, queryend);
        }
        // end, code added by pr on 18thsep2020

        long startTime = System.currentTimeMillis();
        //System.out.println(" inside get datapage role is " + role);
        ArrayList<Forms> totalArr = new ArrayList<Forms>();
        String pendingStr = "", rejectStr = "", pendingDate = "";
        PreparedStatement ps = null;
        ResultSet res1 = null;
        String csvForms = "";
        String field = "", tot_field = "";
        String email1 = "";
        String statusString = "";
        boolean showOnHold = false;
        Set<String> coordsIds = null;

        List<String> typeList = new ArrayList<>(Arrays.asList(type));
        //if (typeList.contains("onhold")) {
        if (typeList.contains("onhold") || typeList.contains("offhold")) {// line modified by pr on 27thapr2020
            showOnHold = true;
        }

        if (supForms != null) {
            if (supForms.size() > 0) {
                csvForms = String.join(",", supForms);
            }
        }

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
            String qry = "SELECT count(*) FROM final_audit_track WHERE 1 "; // on_hold column added by pr on 6thaug19

            //System.out.println(" qry 1 is " + qry);
            if (Constants.CA_PENDING.contains(search)) {
                statusString = "ca_pending";
            } else if (Constants.COORDINATOR_PENDING.contains(search)) {
                statusString = "coordinator_pending";
            } else if (Constants.SUPPORT_PENDING.contains(search)) {
                statusString = "support_pending";
            } else if (Constants.MAIL_ADMIN_PENDING.contains(search)) {
                statusString = "mail-admin_pending";
            } else if (Constants.CA_REJECTED.contains(search)) {
                statusString = "ca_rejected_pending";
            } else if (Constants.COORDINATOR_REJECTED.contains(search)) {
                statusString = "coordinator_rejected";
            } else if (Constants.SUPPORT_REJECTED.contains(search)) {
                statusString = "support_rejected";
            } else if (Constants.MAIL_ADMIN_REJECTED.contains(search)) {
                statusString = "mail-admin_rejected";
            } else if (Constants.COMPLETED.contains(search)) {
                statusString = "completed";
            }

            if (!search.equals("")) {
                statusString = "";
                if (search.contains("re") || search.contains("fo") || search.contains("no")) {
                    statusString = "ca";
                } else if (search.contains("co")) {
                    statusString = "coordinator";
                } else if (search.contains("su")) {
                    statusString = "support";
                } else if (search.contains("ad")) {
                    statusString = "mail-admin";
                }

                if (statusString.equals("")) {
                    statusString = search;
                }

                qry += " AND ( applicant_email LIKE ?  OR applicant_name LIKE ? OR applicant_mobile LIKE ? OR registration_no LIKE ?  OR to_datetime LIKE ? OR status LIKE '" + statusString + "%' OR app_user_type LIKE ? OR app_ca_type LIKE ?) ";

                //System.out.println(" qry 2 is " + qry);
            }
            boolean condition_flag = false;
            String condition_str = "";

            if (showOnHold) {

                if (typeList.contains("onhold")) // if around and else if added by pr on 27thapr2020
                {
                    qry += "and ( on_hold = 'y' ";// ( added by pr on 7thjan2020          
                } else if (typeList.contains("offhold")) {
                    qry += "and ( on_hold = 'n' ";// ( added by pr on 7thjan2020          
                }
                // System.out.println(" qry 3 is " + qry);
            }

            int i = 0;

            if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                coordsIds = db.fetchCoIds(email);
            }

            for (String types : type) {
                //System.out.println("1st for block : " + types);
                //if (!(types.equalsIgnoreCase("onhold") || types.equalsIgnoreCase("query") || types.equalsIgnoreCase("query_ans"))) {
                // above line modified by pr on 27thapr2020
                if (!(types.equalsIgnoreCase("onhold") || types.equalsIgnoreCase("offhold") || types.equalsIgnoreCase("query") || types.equalsIgnoreCase("query_ans"))) {
                    if (i == 0) {
                        if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                            if (!coordsIds.isEmpty()) {
                                condition_str = " AND ((";
                            } else {
                                condition_str = " AND (";
                            }
                        } else {
                            condition_str = " AND (";
                        }
                    } else {
                        condition_str = "OR ";
                    }
                }

                String subQuery = generateQueryForAliases(email, types);
                switch (types) {
                    case "pending":
                        qry += condition_str + "( status = '" + pendingStr + "' ";
                        i++;
                        break;
                    case "completed":
                        qry += condition_str + "( status = 'completed' ";
                        i++;
                        break;
                    case "rejected":
                        qry += condition_str + "( status = '" + rejectStr + "' ";
                        i++;
                        break;
                    case "forwarded":
                        if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN)) {
                            qry += condition_str + "( status = 'completed' ";
                        } else {
                            qry += condition_str + "( (status != '" + rejectStr + "' and status != '" + pendingStr + "') ";
                        }
                        i++;
                        break;
//                    case "query":
//                        queryRaiseData = fetchQueryRaisedForms(querystart, queryend);// empty parameters passed by pr on 24thsep19
//                        break;
//                    case "query_ans":
//                        queryAnsweredData = fetchQueryAnsweredForms(querystart, queryend);// empty parameters passed by pr on 24thsep19
//                        break; // lines commented by pr on 18thsep2020
                }

                if (!subQuery.isEmpty()) {
                    qry += " AND " + subQuery + " ) ";
                } else {
                    qry += ")";
                }
            }
            if (i > 0) {
                qry += ")";
            }

            // System.out.println("  email value is " + email);
            boolean flagP, flagT;
            if (email != null) {
                //System.out.println(" inside email not null ");
                //modified by AT to improvise query on 3rd Sep 2019
                // need to get id from coordinator id
                if (role.equalsIgnoreCase(Constants.ROLE_CO)) {
                    String sb = "";
                    if (!coordsIds.isEmpty()) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }
                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        // System.out.println("my sb:" + sb);
                        qry += " or co_id in (" + sb + ") )";
                    }
                }
            }

            condition_flag = false;
            i = 0;
            for (String form : formName) {
                i++;

                //System.out.println("FORM NAME = " + form);
                if (!form.equalsIgnoreCase("all")) {
                    if (supForms != null) {
                        if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                            if (!condition_flag) {
                                condition_str = " AND (";
                                condition_flag = true;
                            } else {
                                condition_str = " OR ";
                            }
                            if (supForms.contains("'" + form + "'")) {
                                if (form.contains("nkn")) {
                                    qry += condition_str + " form_name like ? ";

                                    // System.out.println(" qry 27 is " + qry);
                                } else {
                                    qry += condition_str + " form_name = ? ";

                                    //System.out.println(" qry 28 is " + qry);
                                }
                            }
                        } else if (form.contains("nkn")) {
                            if (!condition_flag) {
                                condition_str = " AND (";
                                condition_flag = true;
                            } else {
                                condition_str = " OR ";
                            }
                            qry += condition_str + " form_name like ? ";

                            //System.out.println(" qry 29 is " + qry);
                        } else {
                            if (!condition_flag) {
                                condition_str = " AND (";
                                condition_flag = true;
                            } else {
                                condition_str = " OR ";
                            }
                            //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                            qry += condition_str + " form_name = ? ";

                            //System.out.println(" qry 30 is " + qry);
                        }
                    } else if (form.contains("nkn")) {
                        qry += condition_str + " form_name like ? ";

                        // System.out.println(" qry 31 is " + qry);
                    } else {
                        qry += condition_str + " form_name = ? ";

                        //System.out.println(" qry 32 is " + qry);
                    }
                } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                    if (!csvForms.isEmpty()) {
                        qry += " and form_name in (" + csvForms + ") "; // line added by pr on 21stfeb18

                        //System.out.println(" qry 33 is " + qry);
                    }
                } else if (role.equalsIgnoreCase(Constants.ROLE_CO)) // else if added by pr on 24thoct19
                {
                    //System.out.println(" before considering forms for coordinator query is " + qry);

                    if (roles_service != null) {
                        qry += " and form_name in ( ";

                        int j = 1;
                        for (String frm : roles_service) {
                            if (j > 1) {
                                qry += ",";
                            }

                            qry += "'" + frm + "'";

                            j++;
                        }

                        qry += ")";

                    }

                    //System.out.println(" AFTER considering forms for coordinator query is " + qry);
                }
            }

            if (condition_flag) {
                qry += ")";
            }

            // start, code added by pr on 26thjun19
            // put a check on form reg nos here in case of query raise filter , below code added by pr on 26thjun19
            if (queryAnsweredData != null) {
                if (queryAnsweredData.size() > 0) {
                    queryRaiseData.addAll(queryAnsweredData);
                }
            }

            if (queryRaiseData != null) {
                String arrString = String.join("\",\"", queryRaiseData);

                arrString = "\"" + arrString + "\"";
                //System.out.println("REGNUMBERS : " + arrString);

                qry += " AND registration_no in (" + arrString + ") ";
            }
            // start, code added by pr on 2ndjul19, so that 
            if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) && email.contains("rajesh.singh@nic.in") && csvForms.contains("ip")) {
                flagP = false;
                flagT = false;
                for (String types : type) {
                    if (types.equals("pending") || types.equals("new")) {
                        if (!flagP) {
                            qry += " AND to_email = 'rajesh.singh@nic.in'";
                            flagP = true;
                        }
                    } else if (!flagT) {
                        qry += " AND  ( to_email = 'rajesh.singh@nic.in' OR  ( admin_email = 'rajesh.singh@nic.in'  OR  admin_email = 'rajs@nic.in' "
                                + " OR  admin_email = 'rajesh.singh@gov.in' OR  admin_email = 'rajesh.singh@dummy.nic.in'  OR  admin_email = 'rajs@dummy.nic.in' "
                                + "OR  admin_email = 'rajesh.singh@nkn.in'  OR  admin_email = 'rajs@vastu13.nic.in' ) ) ";
                        flagT = true;
                    }
                }
            }

            //qry += " ORDER BY to_datetime DESC LIMIT " + start + ", " + limit;
            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;
            if (!search.equals("")) {
                for (i = 0; i < 7; i++) {
                    pscnt = pscnt + 1;
                    //System.out.println("PSCNT 1" + pscnt);
                    ps.setString(pscnt, "" + search + "%");
                }
            }
            for (String form : formName) {
                //System.out.println("FORM NAME While assigning = " + form);
                if (!form.equalsIgnoreCase("all")) {
                    if (supForms != null) {
                        if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                            if (supForms.contains("'" + form + "'")) {
                                pscnt = pscnt + 1;
                                if (form.contains("nkn")) {
                                    form = "%nkn%";
                                }
                                // System.out.println("PSCNT 2 " + pscnt);
                                ps.setString(pscnt, form);
                            }
                        } else {
                            pscnt = pscnt + 1;
                            if (form.contains("nkn")) {
                                form = "%nkn%";
                            }
                            //System.out.println("PSCNT 3 " + pscnt);
                            ps.setString(pscnt, form);
                        }
                    } else {
                        pscnt = pscnt + 1;
                        if (form.contains("nkn")) {
                            form = "%nkn%";
                        }
                        // System.out.println("PSCNT 4 " + pscnt);
                        ps.setString(pscnt, form);
                    }
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchFilteredDataPage getDataPage QUERY is " + ps);
            res1 = ps.executeQuery();
            Forms frmObj = null;
            if (res1.next()) {
                cnt = res1.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  ############################### ERRROR  inside getDataPage " + ex.getMessage());
            //ex.printStackTrace(); // line commented by pr on 23rdapr19
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 1 " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 2 " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getDataPage EXCEPTION 3 " + e.getMessage());
                }
            }
        }
        return cnt;
    }

    // below function added by pr on 26thjun19, start, end added by pr on 12thjul19
    public ArrayList<String> fetchQueryRaisedForms(String start, String end) {

        // System.out.println(" inside fetchQueryRaisedForms start is " + start + " end is " + end);
        ArrayList<String> arr = new ArrayList<String>();

        String role = fetch_role();

        String qr_forwarded_by = "";

        if (role.equals(Constants.ROLE_CA)) {
            qr_forwarded_by = "ca";

        } else if (role.equals(Constants.ROLE_CO)) {
            qr_forwarded_by = "c";

        } else if (role.equals(Constants.ROLE_SUP)) {
            qr_forwarded_by = "s";

        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
            qr_forwarded_by = "m";

        } else if (role.equals(Constants.ROLE_USER)) {
            qr_forwarded_by = "u";

        }

        UserData userdata = (UserData) sessionMap.get("uservalues");

        String loggedin_email = userdata.getEmail();

        Set aliases = (Set) userdata.getAliases(); // iterate through this set and check if it is equal to qr_forwarded_by_user 

        PreparedStatement ps = null;
        ResultSet res1 = null;

        try {

            conSlave = DbConnection.getSlaveConnection();
            String qry = "SELECT DISTINCT(qr_reg_no)  qr_reg_no FROM query_raise WHERE qr_forwarded_by = ?  ";

            if (!qr_forwarded_by.equals("s")) {

                int i = 1;

                for (Object alias : aliases) {
                    if (i == 1) {
                        qry += " AND ( qr_forwarded_by_user = ?";
                    } else {
                        qry += " OR qr_forwarded_by_user = ?";

                    }

                    i++;
                }

                qry += " ) ";

            }

            // below if added by pr on 12thjul19
            if (start != null && !start.equals("") && end != null && !end.equals("")) // end date is incremented because the DB date contains date time both 
            {
                qry += " AND qr_createdon  between '" + start + "' AND  DATE_ADD('" + end + "', INTERVAL '1' DAY) ";
            }

            qry += " ORDER BY qr_createdon DESC ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, qr_forwarded_by);

            if (!qr_forwarded_by.equals("s")) {

                int j = 2;

                for (Object alias : aliases) {
                    // System.out.println(" inside fetchQueryRaisedForms function alias is " + alias.toString());

                    ps.setString(j, alias.toString());

                    j++;

                }

            }

            //System.out.println(" fetchQueryRaisedForms query is " + ps);
            res1 = ps.executeQuery();
            Forms frmObj = null;
            while (res1.next()) {

                arr.add(res1.getString("qr_reg_no"));

            }
        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchQueryRaisedForms Exception catch " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchQueryRaisedForms EXCEPTION 1 " + e.getMessage());
                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchQueryRaisedForms EXCEPTION 2 " + e.getMessage());
                }
            }

        }

        return arr;

    }

    // below function added by pr on 27thjun19, start, end added by pr on 12thjul19
    public ArrayList<String> fetchQueryAnsweredForms(String start, String end) {
        ArrayList<String> arr = new ArrayList<String>();

        String role = fetch_role();

        String qr_forwarded_to = "";

        if (role.equals(Constants.ROLE_CA)) {
            qr_forwarded_to = "ca";

        } else if (role.equals(Constants.ROLE_CO)) {
            qr_forwarded_to = "c";

        } else if (role.equals(Constants.ROLE_SUP)) {
            qr_forwarded_to = "s";

        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
            qr_forwarded_to = "m";

        } else if (role.equals(Constants.ROLE_USER)) {
            qr_forwarded_to = "u";

        }

        UserData userdata = (UserData) sessionMap.get("uservalues");

        String loggedin_email = userdata.getEmail();

        Set aliases = (Set) userdata.getAliases(); // iterate through this set and check if it is equal to qr_forwarded_by_user 

        PreparedStatement ps = null;
        ResultSet res1 = null;

        try {

            conSlave = DbConnection.getSlaveConnection();

            String qry = "SELECT qr_reg_no FROM query_raise WHERE qr_id IN (SELECT MAX(qr_id) FROM query_raise GROUP BY qr_reg_no ) AND qr_forwarded_to = ? ";

            if (!qr_forwarded_to.equals("s")) {

                int i = 1;

                for (Object alias : aliases) {
                    if (i == 1) {
                        qry += " AND ( qr_forwarded_to_user = ?";
                    } else {
                        qry += " OR qr_forwarded_to_user = ?";

                    }

                    i++;
                }

                qry += " ) ";

            }

            // below if added by pr on 12thjul19
            if (start != null && !start.equals("") && end != null && !end.equals("")) // end date is incremented because the DB date contains date time both 
            {
                qry += " AND qr_createdon  between '" + start + "' AND  DATE_ADD('" + end + "', INTERVAL '1' DAY) ";
            }

            qry += " ORDER BY qr_createdon DESC ";

            ps = conSlave.prepareStatement(qry);

            ps.setString(1, qr_forwarded_to);

            if (!qr_forwarded_to.equals("s")) {

                int j = 2;

                for (Object alias : aliases) {
                    // System.out.println(" inside fetchQueryAnsweredForms function alias is " + alias.toString());

                    ps.setString(j, alias.toString());

                    j++;

                }

            }

            //  System.out.println(" fetchQueryAnsweredForms query is " + ps);
            res1 = ps.executeQuery();
            Forms frmObj = null;
            while (res1.next()) {

                arr.add(res1.getString("qr_reg_no"));

            }

        } catch (Exception ex) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getfetchQueryAnsweredForms Exception catch " + ex.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchQueryAnsweredForms EXCEPTION 1 " + e.getMessage());
                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchQueryAnsweredForms EXCEPTION 2 " + e.getMessage());
                }
            }

        }

        return arr;

    }

    // below function added by pr on 21stfeb18
    public ArrayList checkSupMadminForm()// it returns true if the form should be visible to support or mailadmin else false 
    {
        initializeSessionValues();
        if (role.equals(Constants.ROLE_SUP) || role.equals(Constants.ROLE_MAILADMIN)) {
            PreparedStatement ps = null;
            ResultSet res = null;
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = " SELECT m_single,m_bulk,m_nkn,m_relay,m_ldap,m_dlist,m_ip,m_sms,m_wifi,m_imappop,m_gem,m_mobile,m_dns,m_vpn,m_webcast,m_email_act,m_email_deact,m_wifiport,m_daonboarding FROM mailadmin_forms WHERE ";
                // System.out.println(" email in checj " + email);
                if (email != null && email.size() > 0) {
                    int i = 1;
                    for (Object o : email) {
                        if (i == 1) {
                            qry += " m_email = '" + o.toString() + "'";
                        } else {
                            qry += " OR  m_email = '" + o.toString() + "'";
                        }
                        i++;
                    }
                }
                qry += " ORDER BY m_id DESC LIMIT 1  ";
                ps = conSlave.prepareStatement(qry);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside get form names " + ps);

                res = ps.executeQuery();
                while (res.next()) {
                    if (res.getString("m_single").equals("y")) {
                        supForms.add("'" + Constants.SINGLE_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_bulk").equals("y")) {
                        supForms.add("'" + Constants.BULK_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_nkn").equals("y")) {
                        supForms.add("'" + Constants.NKN_SINGLE_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.NKN_BULK_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_relay").equals("y")) {
                        supForms.add("'" + Constants.RELAY_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_ldap").equals("y")) {
                        supForms.add("'" + Constants.LDAP_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dlist").equals("y")) {
                        supForms.add("'" + Constants.DIST_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dlist").equals("y")) {
                        supForms.add("'" + Constants.BULKDIST_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_sms").equals("y")) {
                        supForms.add("'" + Constants.SMS_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_ip").equals("y")) {
                        supForms.add("'" + Constants.IP_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_imappop").equals("y")) {
                        supForms.add("'" + Constants.IMAP_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_gem").equals("y")) {
                        supForms.add("'" + Constants.GEM_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_mobile").equals("y")) {
                        supForms.add("'" + Constants.MOB_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dns").equals("y")) {
                        supForms.add("'" + Constants.DNS_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_wifi").equals("y")) {
                        supForms.add("'" + Constants.WIFI_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_vpn").equals("y")) {
                        supForms.add("'" + Constants.VPN_SINGLE_FORM_KEYWORD + "'");
                        // supForms.add("'" + Constants.VPN_BULK_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_ADD_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_RENEW_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_SURRENDER_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_DELETE_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dor_ext").equals("y")) {
                        supForms.add("'" + Constants.DOR_EXT_FORM_KEYWORD + "'");
                    }

                    if (res.getString("m_webcast").equals("y")) {
                        supForms.add("'" + Constants.WEBCAST_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_centralutm").equals("y")) {
                        supForms.add("'" + Constants.FIREWALL_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_email_act").equals("y")) {
                        supForms.add("'" + Constants.EMAILACTIVATE_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_email_deact").equals("y")) {
                        supForms.add("'" + Constants.EMAILDEACTIVATE_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_wifiport").equals("y")) {
                        supForms.add("'" + Constants.WIFI_PORT_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_daonboarding").equals("y")) {
                        supForms.add("'" + Constants.DAONBOARDING_FORM_KEYWORD + "'");
                    }
                }
                // below lines added by pr on 6thmar18
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function EXCEPTION " + e.getMessage());

                    }
                }
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function EXCEPTION 2 " + e.getMessage());
                    }
                }
            }
        } else {
            supForms.add("'" + Constants.SINGLE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.BULK_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.NKN_SINGLE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.NKN_BULK_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.RELAY_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.LDAP_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.DIST_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.BULKDIST_FORM_KEYWORD + "'");  // Added by Rahul jan 2021
            supForms.add("'" + Constants.SMS_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.IP_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.IMAP_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.GEM_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.MOB_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.DNS_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.WIFI_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.WIFI_PORT_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_SINGLE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.WEBCAST_FORM_KEYWORD + "'");
            // supForms.add("'" + Constants.VPN_BULK_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_ADD_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_RENEW_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_DELETE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_SURRENDER_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.FIREWALL_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.DOR_EXT_FORM_KEYWORD + "'");
        }
        return supForms;
    }

    public String fetchStatTypeString(String stat_type) {
        String type = "";
        if (stat_type.equals("ca_pending")) {
            type = Constants.CA_PENDING;
        } else if (stat_type.equals("ca_rejected")) {
            type = Constants.CA_REJECTED;
        } else if (stat_type.equals("support_pending")) {
            type = Constants.SUPPORT_PENDING;
        } else if (stat_type.equals("support_rejected")) {
            type = Constants.SUPPORT_REJECTED;
        } else if (stat_type.equals("coordinator_pending")) {
            type = Constants.COORDINATOR_PENDING;
        } else if (stat_type.equals("coordinator_rejected")) {
            type = Constants.COORDINATOR_REJECTED;
        } else if (stat_type.equals("completed")) {
            type = Constants.COMPLETED;
        } else if (stat_type.equals("cancel")) {
            type = Constants.CANCEL;
        } else if (stat_type.equals("mail-admin_pending")) {
            type = Constants.MAIL_ADMIN_PENDING;
        } else if (stat_type.equals("mail-admin_rejected")) {
            type = Constants.MAIL_ADMIN_REJECTED;
        } else if (stat_type.equals("da_pending")) // else if added by pr on 20thfeb18
        {
            type = Constants.DA_PENDING;
        } else if (stat_type.equals("da_rejected")) // else if added by pr on 6thapr18
        {
            type = Constants.DA_REJECTED;
        } else if (stat_type.equals("api")) // else if added by pr on 6thapr18
        {
            type = Constants.PENDING_API;
        } else if (stat_type.equals("domainapi")) // else if added by pr on 6thapr18
        {
            type = Constants.PENDING_API;
        } else if (stat_type.equals("manual_upload")) // else if added by pr on 10thmay18
        {
            type = Constants.manual_upload;
        } else if (stat_type.equals("us_pending")) // start,code added  by pr on 8thjan19
        {
            type = "Pending with Under Secretary and above";
        } else if (stat_type.equals("us_rejected")) {
            type = "Rejected by Under Secretary";
        } else if (stat_type.equals("us_expired")) {
            type = "Under Secretary Link Timeout";
        }// end, code added  by pr on 8thjan19
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + 
//                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getStatTypeString is " + type);

        return type;

    }

    public String showLinkData() {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside show link data function fisrt line ");

        String returnResponse = "error"; // line taken up from the bottom by pr on 25thapr19

        try {

            initializeSessionValues();
            String sessAdminRole = "";
            // set the admin role accroding to the highest priority
            if (role_head.contains("admin")) {
                sessAdminRole = "admin";
            } else if (role_head.contains("co")) {
                sessAdminRole = "co";
            } else if (role_head.contains("sup")) {
                sessAdminRole = "sup";
            } else if (role_head.contains("ca")) {
                sessAdminRole = "ca";
            } else if (role_head.contains("user")) {
                sessAdminRole = "user";
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside show link data function admin role value is" + adminValue);

            listBeanObj = new ListBean();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside show link data function adminValue " + adminValue);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside show link data function role_head " + role_head);

            if ((adminValue.equals("admin")) && (!role_head.contains("admin"))) {
                //returnResponse = "error";
                adminValue = "";
            } else if ((adminValue.equals("sup")) && (!role_head.contains("sup"))) {
                //returnResponse = "error";
                adminValue = "";
            } else if ((adminValue.equals("co")) && (!role_head.contains("co"))) {
                //returnResponse = "error";
                adminValue = "";
            } else if ((adminValue.equals("ca")) && (!role_head.contains("ca"))) {
                //returnResponse = "error";
                adminValue = "";
            }

            if (sessionMap != null) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside show link data function loggedin_email " + loggedin_email);

                if (loggedin_email != null && role_head != null && (role_head.contains("sup") || role_head.contains("admin")
                        || role_head.contains("co") || role_head.contains("ca"))) {
                    returnResponse = "success";
                    if (!adminValue.isEmpty()) {
                        sessionMap.put("user_role", adminValue);
                        sessionMap.put("admin_role", adminValue);
                    } else {
                        sessionMap.put("user_role", sessAdminRole); // line added by pr on 15thfeb19
                        sessionMap.put("admin_role", sessAdminRole);
                    }
                    sessionMap.put("admin_email", loggedin_email); // above code commented and this added by pr on 19thdec17
                    if (sessionMap.get("admin_role") != null && !sessionMap.get("admin_role").toString().isEmpty()) {
                        role = sessionMap.get("admin_role").toString();
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ROLE:::::: "+ role);
                        if (role.equals("ca")) {
                            listBeanObj.setConsole("Reporting/Nodal/Forwarding Officer Console");
                            listBeanObj.setRoleHead("Reporting/Nodal/Forwarding Officer");
                        } else if (role.equals("sup")) {
                            listBeanObj.setConsole("Support Console");
                            listBeanObj.setRoleHead("Support");
                        } else if (role.equals("co")) {
                            listBeanObj.setConsole("Coordinator Console");
                            listBeanObj.setRoleHead("Coordinator");
                        } else if (role.equals("admin")) {
                            listBeanObj.setConsole("Admin Console");
                            listBeanObj.setRoleHead("Admin");
                        } else if (role.equals("user")) {
                            listBeanObj.setConsole("User Console");
                            listBeanObj.setRoleHead("User");
                        }
                    } else {
                        role = "";
                    }

                } else {
                    returnResponse = "logout";
                }
            }

        } catch (Exception e) // try catch added by pr on 25thapr19
        {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " showLinkData MAIN EXCEPTION CATCH " + e.getMessage());
        }

        return returnResponse;
    }

    // below function added by pr on 19thmar19
    public String fetchAllCount() {

        initializeSessionValues();
        // set the counts in the bean
        int pendingCount = fetchCount("", "pending", "all");
        int totalCount = fetchCount("", "total", "all");
        int newCount = fetchCount("", "new", "all");
        int completeCount = fetchCount("", "completed", "all");
        listBeanObj.setTotalRequest(Integer.toString(totalCount));
        listBeanObj.setNewRequest(Integer.toString(newCount));
        listBeanObj.setPendingRequest(Integer.toString(pendingCount));
        listBeanObj.setCompleteRequest(Integer.toString(completeCount));

        // System.out.println("Inserted into session");
        sessionMap.put("totalcount", totalCount);
        sessionMap.put("newcount", newCount);
        sessionMap.put("pendingcount", pendingCount);
        sessionMap.put("completecount", completeCount);

        // set the filter names 
        showFilters(this.email);
        if (filterForms != null) {
            listBeanObj.setFilterForms(filterForms);
        }
        return SUCCESS;
    }

    public void setParam(HttpServletRequest request) {
        if (request.getParameter("sEcho") != null && request.getParameter("sEcho") != "") {
            this.sEcho = request.getParameter("sEcho");
            this.sSearch = request.getParameter("sSearch");
            this.sColumns = request.getParameter("sColumns");
            this.iDisplayStart = Integer.parseInt(request.getParameter("iDisplayStart"));
            this.iDisplayLength = Integer.parseInt(request.getParameter("iDisplayLength"));
            this.iColumns = Integer.parseInt(request.getParameter("iColumns"));
            this.iSortingCols = Integer.parseInt(request.getParameter("iSortingCols"));
            this.iSortColumnIndex = Integer.parseInt(request.getParameter("iSortCol_0"));
            this.sSortDirection = request.getParameter("sSortDir_0");
        }
    }

    public String rawShowLinkData() {
        initializeSessionValues();
        if (listBeanObj == null) {
            listBeanObj = new ListBean();
        }
        List<Forms> dataa = new ArrayList<>();

        role = fetch_role();
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside showlinkdata role value is " + role + " loggedin_email is " + loggedin_email + " sup size is " + supForms.size());
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String eventRequest = request.getParameter("action");
        setParam(request);
        String sEcho = this.sEcho;
        List<Forms> dat = null;

        // start, code added by pr on 6thsep19
        //System.out.println(" inside rawshowlinkdata sessionMap is " + sessionMap + " role is " + role);
        int totalCount = Integer.parseInt(sessionMap.get(role + "totalcount").toString());

        listBeanObj.setTotalRequest(Integer.toString(totalCount));

        int newCount = Integer.parseInt(sessionMap.get(role + "newcount").toString());

        listBeanObj.setNewRequest(Integer.toString(newCount));

        int pendingCount = Integer.parseInt(sessionMap.get(role + "pendingcount").toString());

        listBeanObj.setPendingRequest(Integer.toString(pendingCount));

        int completeCount = Integer.parseInt(sessionMap.get(role + "completecount").toString());

        listBeanObj.setCompleteRequest(Integer.toString(completeCount));

        //System.out.println(" inside rawsholinkdata function totalcount " + totalCount + " newcount " + newCount + " pendingcount " + pendingCount + " completecount " + completeCount);
        // end, code added by pr on 6thsep19
        if (eventRequest.equals("onload") || eventRequest.equals("pending")) // if coming for the first time show the pending requests
        {
            pendingCount = fetchCount(this.sSearch, "pending", "all"); // int removed by pr on 6thsep19
            sessionMap.put("pendingcount", pendingCount);
            //System.out.println("PENDING Count : " + pendingCount);
            iTotalRecords = pendingCount;
            dat = fetchDataPage(this.sSearch, "pending", Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), "all");

            //fetchDataPageExport(this.sSearch, "pending", "all"); // line added by pr on 26thjul19
            listBeanObj.setHeading("TOTAL PENDING REQUESTS");
            listBeanObj.setPendingRequest(Integer.toString(pendingCount)); // line added by pr on 28thmay18
        } else if (eventRequest.equals("total")) // if coming for the first time show the pending requests
        {
            totalCount = fetchCount(this.sSearch, "total", "all"); // int removed by pr on 6thsep19
            sessionMap.put("totalcount", totalCount);
            iTotalRecords = totalCount;
            dat = fetchDataPage(this.sSearch, "total", Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), "all");

            //fetchDataPageExport(this.sSearch, "total", "all"); // line added by pr on 26thjul19
            listBeanObj.setHeading("TOTAL REQUESTS");
            listBeanObj.setTotalRequest(Integer.toString(totalCount)); // line added by pr on 28thmay18
        } else if (eventRequest.equals("new")) // if coming for the first time show the pending requests
        {
            newCount = fetchCount(this.sSearch, "new", "all"); // int removed by pr on 6thsep19
            sessionMap.put("newcount", newCount);
            iTotalRecords = newCount;
            dat = fetchDataPage(this.sSearch, "new", Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), "all");

            //fetchDataPageExport(this.sSearch, "new", "all"); // line added by pr on 26thjul19
            listBeanObj.setHeading("TODAY'S PENDING REQUESTS");
            listBeanObj.setNewRequest(Integer.toString(newCount)); // line added by pr on 28thmay18
        } else if (eventRequest.equals("completed")) // if coming for the first time show the pending requests
        {
            completeCount = fetchCount(this.sSearch, "completed", "all"); // int removed by pr on 6thsep19
            sessionMap.put("completecount", completeCount);
            iTotalRecords = completeCount;
            dat = fetchDataPage(this.sSearch, "completed", Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), "all");

            //fetchDataPageExport(this.sSearch, "completed", "all"); // line added by pr on 26thjul19
            listBeanObj.setHeading("COMPLETED REQUESTS");
            listBeanObj.setCompleteRequest(Integer.toString(completeCount)); // line added by pr on 28thmay18
        } else if (eventRequest.equals("filter")) // if coming for the first time show the pending requests
        {
            //System.out.println("INSIDE FILTERS");
            String frm = request.getParameter("frm");
            String stat = request.getParameter("stat");
            //System.out.println("Form is " + frm + " AND State is " + stat);
            String forms[] = null;
            String status[] = null;
            dat = new ArrayList<>();
            List<Forms> filteredData = null;
            if (!frm.isEmpty()) {
                forms = frm.split(",");
                //System.out.println(Arrays.toString(forms));
            }
            if (!stat.isEmpty()) {
                status = stat.split(",");
                //System.out.println(Arrays.toString(status));
            }
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside filter block in rawshowlinkdata frm value is " + frm + " stat value is " + stat);
            int filterCount = 0;
            dat.clear();
            if (stat.isEmpty() && frm.isEmpty()) {
                //System.out.println("Inside second IF...");
                filterCount = fetchCount(this.sSearch, "total", "all");
                dat = fetchDataPage(this.sSearch, "total", Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), "all");

                //fetchDataPageExport(this.sSearch, "total", "all"); // line added by pr on 26thjul19
            } else if (stat.isEmpty() && !frm.isEmpty()) {
                //System.out.println("Inside second ELSE...");
                status = new String[]{"forwarded", "pending"};

                filterCount = fetchFilteredCount(this.sSearch, status, "", "", forms);
//                for (String f : forms) {
//                    filterCount += fetchCount(this.sSearch, "total", f);
//                }
                dat = fetchFilteredDataPage(this.sSearch, status, Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), forms);
                //dat = fetchDataPage(this.sSearch, , Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), forms);

                //fetchDataPageExport(this.sSearch, "total", "all"); // line added by pr on 26thjul19
            } else if (!stat.isEmpty() && frm.isEmpty()) {
                forms = new String[]{"all"};
                //System.out.println("Inside second Last ELSE...");
                filterCount = fetchFilteredCount(this.sSearch, status, "", "", forms);
                dat = fetchFilteredDataPage(this.sSearch, status, Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), forms);

                //fetchDataPageExport(this.sSearch, stat, "all"); // line added by pr on 26thjul19
            } else {
                //System.out.println("Inside LAST ELSE...");
                filterCount = fetchFilteredCount(this.sSearch, status, "", "", forms);
                dat = fetchFilteredDataPage(this.sSearch, status, Integer.toString(this.iDisplayStart), Integer.toString(this.iDisplayLength), forms);

                //fetchDataPageExport(this.sSearch, stat, "all"); // line added by pr on 26thjul19
            }
            iTotalRecords = filterCount;
            listBeanObj.setHeading("FILTERED REQUESTS");
        }

        int p = 1;
        for (Forms c : dat) {
            //System.out.println("Inside compare : " + c.get_stat_type().toLowerCase().contains(this.sSearch.toLowerCase()) + "    stat type      " + c.get_stat_type().toLowerCase() + "Searched Text " + this.sSearch.toLowerCase());

            //System.out.println("");
            if (c.get_stat_reg_no().toLowerCase().contains(this.sSearch.toLowerCase())
                    || c.get_email().toLowerCase().contains(this.sSearch.toLowerCase())
                    || c.get_stat_type().toLowerCase().contains(this.sSearch.toLowerCase())
                    || c.getName().toLowerCase().contains(this.sSearch.toLowerCase())
                    //|| c.getDesignation().toLowerCase().contains(this.sSearch.toLowerCase()) // commented by pr on 20thsep19
                    || c.get_stat_createdon().toLowerCase().contains(this.sSearch.toLowerCase())
                    || c.getApp_user_type().toLowerCase().contains(this.sSearch.toLowerCase())
                    || c.getApp_ca_type().toLowerCase().contains(this.sSearch.toLowerCase())) {
                dataa.add(c);
                p++;
            }
            if (!this.sSearch.equals("")) {
                iTotalRecords = dataa.size();
            }
        }
        iTotalDisplayRecords = iTotalRecords;// number of companies that match search criterion should be returned
        final int sortColumnIndex = this.iSortColumnIndex;
        final int sortDirection = this.sSortDirection.equals("asc") ? -1 : 1;
        Collections.sort(dataa, new Comparator<Forms>() {
            @Override
            public int compare(Forms c1, Forms c2) {
                switch (sortColumnIndex) {
                    case 0:
                        return c1.get_stat_reg_no().compareTo(c2.get_stat_reg_no()) * sortDirection;
                    case 1:
                        return c1.get_email().compareTo(c2.get_email()) * sortDirection;
                    case 2:
                        return c1.get_stat_type().compareTo(c2.get_stat_type()) * sortDirection;
                    case 3:
                        return c1.getApp_user_type().compareTo(c2.getApp_user_type()) * sortDirection;
                    case 4:
                        return c1.get_stat_createdon().compareTo(c2.get_stat_createdon()) * sortDirection;
                }
                return 0;
            }
        });
        this.aaData = dataa;
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  aadata is " + this.aaData);
        return SUCCESS;
    }

    public Boolean fetchRoleBasedShowAction(String stat_type, String formName) {
        role = fetch_role();
        showAction = false;
        showCreateAction = false;
        showBulkCreateAction = true; // line added by pr on 13thdec17
        showBulkLink = false;
        showBulkUpdateAction = true;
        if (role.equals(Constants.ROLE_CA) && stat_type.equals("ca_pending")) {
            showAction = true;
            // start, code added by pr on 12thdec19
            if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
//                showBulkUpdateAction = true; 
                showBulkCreateAction = false;
                showBulkLink = true;
            }
            // end, code added by pr on 12thdec19
        } else if (role.equals(Constants.ROLE_SUP) && stat_type.equals("support_pending")) {
            showAction = true;
        } else if (role.equals(Constants.ROLE_CO) && (stat_type.equals("coordinator_pending"))) {
            showAction = true;
            // start, code added by pr on 12thdec19
            if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
//                showBulkUpdateAction = true; 
                showBulkCreateAction = false;
                showBulkLink = true;
            }
            // end, code added by pr on 12thdec19

        } else if (role.equals(Constants.ROLE_MAILADMIN)) {
            showAction = false;
            if (stat_type.equals("mail-admin_pending")) {
                showCreateAction = true;
                showAction = true;
            }
            if (formName.equals(Constants.BULK_FORM_KEYWORD) || formName.equals(Constants.NKN_BULK_FORM_KEYWORD)) {
                showCreateAction = false;
                showBulkLink = true;
            }
//            else if (formName.equals(Constants.VPN_BULK_FORM_KEYWORD)) // else if added by pr on 8thjan18
//            {
//                showCreateAction = true;
//                showBulkLink = true;
//                showBulkCreateAction = false;
//                if (stat_type.equals("completed")) // if added by pr on 13thdec17
//                {
//                    showCreateAction = false;
//                }
//            } // commented by pr on 12thdec19
        }
        if (stat_type.equals("completed")) // if added by pr on 13thdec17
        {
            //showBulkCreateAction = false;                    
            showBulkLink = false; // above line commented this line added by pr on 12thdec19
        }
        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchRoleBasedShowAction stat_type is "+stat_type+" role is "+role+" showAction value is "+showAction);
        return showAction;
    }

    // below function modified by pr on 18thdec17
    public String fetchBulkUsers() {

        //System.out.println(" inside fetchBulkUsers function ");
        bulkUsersdata.clear();
        initializeSessionValues();
        if (reg_no != null && !reg_no.equals("") && reg_no.matches("^[a-zA-Z0-9~\\-]*$")) // line modified by pr on 5thfeb18 for xss
        {
            PreparedStatement ps = null;
            ResultSet res1 = null;
            //Connection con = null;
            try {
                /*Class.forName("com.mysql.jdbc.Driver");
                 con = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + dbname, dbuser, dbpass);*/
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = "SELECT mobile,mail,uid,bulk_id,createdon,reject_remarks FROM  bulk_users WHERE registration_no = ?  AND is_created = 'n' AND is_rejected = 'n'  ";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                //System.out.println(" inside getBulkUsers function query is  "+ps);
                res1 = ps.executeQuery();
                Forms frmObj = null;
                while (res1.next()) {
                    frmObj = new Forms();
                    frmObj.set_mobile(res1.getString("mobile"));
                    frmObj.set_email(res1.getString("mail"));
                    frmObj.setUid(res1.getString("uid"));
                    frmObj.setBulk_id(res1.getString("bulk_id"));
                    frmObj.setCreatedon(res1.getString("createdon"));
                    if (res1.getString("reject_remarks") != null) // if added by pr on 31stmay18
                    {
                        frmObj.setReject_remarks(res1.getString("reject_remarks"));
                    } else {
                        frmObj.setReject_remarks("");
                    }
                    ForwardAction fwdObj = new ForwardAction();
                    String emails = fwdObj.fetchMobileLDAPMail(res1.getString("mobile")); // get the emails associated with this mobile from the LDAP
                    frmObj.setMobile_ldap_email(emails);
                    bulkUsersdata.add(frmObj);
                }
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers " + ex.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers EXCEPTION 1 " + e.getMessage());
                    }
                }
                if (res1 != null) {
                    try {
                        res1.close();
                    } catch (Exception e) {
                        //e.printStackTrace();
                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers EXCEPTION 2 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers EXCEPTION 3 " + e.getMessage());
                    }
                }
            }
        }
        //System.out.println(" bulkUsersdata size is "+bulkUsersdata.size() );
        return SUCCESS;
    }

    // below function added by pr on 18thdec17, below function modified by pr on 8thjan18
    public String fetchAllBulkUsers() {
        initializeSessionValues();
        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();

        if (!CSRFRandom.equals(SessionCSRFRandom)) {
            hmTrack.put("csrf_error", "CSRF Token is invalid.");
            return SUCCESS;
        }

        if (reg_no != null && !reg_no.equals("") && reg_no.matches("^[a-zA-Z0-9_~\\-]*$")) // line modified by pr on 5thfeb18 for xss , _ added by pr on 30thoct18
        {
            //System.out.println(" inside fetchAllBulkUsers function reg no nut null ");
            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside fetchAllBulkUsers if reg no ");
            String[] arr = reg_no.split("~"); // array contains first reg_no then form type then action type(reject or forward)
            if (arr != null && arr.length == 2) {
                String regNo = arr[0];
                String formName = arr[1];
                bulkAllUsersdata.clear();
                if (!formName.equals(Constants.VPN_BULK_FORM_KEYWORD))// if bulk or nkn bulk form
                {
                    //System.out.println(" inside fetchAllBulkUsers function reg no nut null NOT vpn bulk ");
                    PreparedStatement ps = null;
                    ResultSet res1 = null;
                    //Connection con = null;
                    try {
                        //con = DbConnection.getConnection();
                        conSlave = DbConnection.getSlaveConnection();

                        // start, code added by pr on 24thdec19
                        String qry1 = "SELECT status FROM  final_audit_track WHERE registration_no = ? ";
                        ps = conSlave.prepareStatement(qry1);
                        ps.setString(1, regNo);
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " query to fetch status is " + ps);
                        res1 = ps.executeQuery();

                        //String current_stat_type = res1.getString("status");
                        String current_stat_type = "";
                        while (res1.next()) {
                            current_stat_type = res1.getString("status");

                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " fetch status status got is " + current_stat_type);

                        }

                        // end, code added by pr on 24thdec19      
                        // reject_remarks, rejected_by fields added by pr on 19thjul19
                        String qry = "SELECT mobile,mail,uid,bulk_id,createdon,is_created,is_rejected,rejected_by,reject_remarks FROM  bulk_users WHERE registration_no = ? AND error_status = 0 ";
                        ps = conSlave.prepareStatement(qry);
                        ps.setString(1, regNo);
                        //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " query is "+ps);
                        res1 = ps.executeQuery();
                        Forms frmObj = null;
                        while (res1.next()) {
                            //System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside next ");                
                            frmObj = new Forms();
                            frmObj.set_mobile(res1.getString("mobile"));
                            frmObj.set_email(res1.getString("mail"));
                            frmObj.setUid(res1.getString("uid"));
                            frmObj.setBulk_id(res1.getString("bulk_id"));
                            frmObj.setCreatedon(res1.getString("createdon"));
                            frmObj.setIs_created(res1.getString("is_created"));
                            frmObj.setIs_rejected(res1.getString("is_rejected"));

                            // start, code added by pr on 19thjul19
                            if (res1.getString("rejected_by") == null) {
                                frmObj.setRejected_by("-");
                            } else {
                                frmObj.setRejected_by(res1.getString("rejected_by"));
                            }

                            if (res1.getString("reject_remarks") == null) {
                                frmObj.setReject_remarks("");
                            } else {
                                frmObj.setReject_remarks(res1.getString("reject_remarks"));
                            }

                            // start, code added by pr on 17thdec19
                            ForwardAction fwdObj = new ForwardAction();
                            //System.out.println(" before checking mobile email for mobile "+res1.getString("mobile"));
                            String emails = fwdObj.fetchMobileLDAPMail(res1.getString("mobile")); // get the emails associated with this mobile from the LDAP
                            //System.out.println(" before checking mobile email for mobile "+res1.getString("mobile")+" emails are "+emails);
                            frmObj.setMobile_ldap_email(emails);

                            // end, code added by pr on 17thdec19
                            //System.out.println(" rejected by value is " + frmObj.getRejected_by() + " reject remarks " + frmObj.getReject_remarks());
                            // end, code added by pr on 19thjul19 
                            frmObj.set_stat_type(current_stat_type); // line added by pr on 24thdec19

                            bulkAllUsersdata.add(frmObj);

                        }
                        // below lines added by pr on 6thmar18
                    } catch (Exception ex) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers " + ex.getMessage());
                    } finally {
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (Exception e) {
                                //e.printStackTrace();

                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAllBulkUsers EXCEPTION 1 " + e.getMessage());

                            }
                        }
                        if (res1 != null) {
                            try {
                                res1.close();
                            } catch (Exception e) {
                                //e.printStackTrace();

                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAllBulkUsers EXCEPTION 2 " + e.getMessage());
                            }
                        }
                        if (con != null) {
                            try {
                                // con.close();
                            } catch (Exception e) {
                                //e.printStackTrace();

                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAllBulkUsers EXCEPTION 3 " + e.getMessage());
                            }
                        }
                    }
                } else {
                    PreparedStatement ps = null;
                    ResultSet res1 = null;
                    //Connection con = null;
                    try {
                        //con = DbConnection.getConnection();
                        conSlave = DbConnection.getSlaveConnection();
                        String qry = "SELECT mobile,email,name,vpn_id,datetime FROM  vpn_users WHERE registration_no = ? ";
                        ps = conSlave.prepareStatement(qry);
                        ps.setString(1, regNo);
                        res1 = ps.executeQuery();
                        Forms frmObj = null;
                        while (res1.next()) {
                            frmObj = new Forms();
                            frmObj.set_mobile(res1.getString("mobile"));
                            frmObj.set_email(res1.getString("email"));
                            frmObj.setUid(res1.getString("name"));
                            frmObj.setBulk_id(res1.getString("vpn_id"));
                            frmObj.setCreatedon(res1.getString("datetime"));
                            bulkAllUsersdata.add(frmObj);
                        }
                    } catch (Exception ex) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getBulkUsers " + ex.getMessage());
                    } finally {
                        if (ps != null) {
                            try {
                                ps.close();
                            } catch (Exception e) {
                                //e.printStackTrace();

                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAllBulkUsers EXCEPTION 4 " + e.getMessage());

                            }
                        }
                        if (res1 != null) {
                            try {
                                res1.close();
                            } catch (Exception e) {
                                //e.printStackTrace();

                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAllBulkUsers EXCEPTION 5 " + e.getMessage());

                            }
                        }
                        if (con != null) {
                            try {
                                // con.close();
                            } catch (Exception e) {
                                //e.printStackTrace();

                                // above line commented below added by pr on 23rdapr19
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchAllBulkUsers EXCEPTION 6 " + e.getMessage());

                            }
                        }
                    }
                }
            }
        }
        return SUCCESS;
    }

    // modified by pr on 25thoct19
    public String searchStatusByRegNoBacup() {
        initializeSessionValues();
        showPullBack = false; // line addde by pr on 15thmar18
        searchdata.clear(); // line added by pr on 2ndmay18                
        empDetailsHM.clear(); // line added by pr on 2ndmay18                
        if (reg_no != null && !reg_no.equals("") && reg_no.matches("^[a-zA-Z0-9~\\-]*$")) // line modified by pr on 5thfeb18 for xss
        {
            PreparedStatement ps = null;
            ResultSet res1 = null;
            //Connection con = null;
            String form_name = ""; // line added by pr on 24thoct19
            try {
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                //String qry = "SELECT * FROM status WHERE stat_reg_no = ? ORDER BY stat_createdon DESC ";
                // stat_form_type added by pr on 24thoct19
                String qry = "SELECT stat_type,stat_forwarded_to_user,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_by,stat_remarks,stat_createdon,stat_form_type FROM status WHERE stat_reg_no = ? ORDER BY stat_id DESC "; // line modified by pr on 11thoct18
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo query is " + ps);

                res1 = ps.executeQuery();
                Forms frmObj = null;
                int i = 1; // line added by pr on 15thmar18
                String finalStatType = ""; // line added by pr on 15thmar18
                while (res1.next()) {
                    if (i == 1) // if added by pr on 15thmar18
                    {
                        finalStatType = res1.getString("stat_type");
                    }
                    //System.out.println(" inside while ");
                    String stat_forwarded_by_user = "", stat_forwarded_to_user = "", stat_forwarded_by = "", stat_forwarded_to = "", stat_remarks = "";

                    if (res1.getString("stat_forwarded_to_user") != null) {
                        stat_forwarded_to_user = res1.getString("stat_forwarded_to_user");
                    }
                    if (res1.getString("stat_forwarded_by") != null) {
                        stat_forwarded_by_user = res1.getString("stat_forwarded_by_user");
                    }
                    // end, code added by pr on 28thmar19
                    stat_forwarded_to = res1.getString("stat_forwarded_to");
                    if (stat_forwarded_to == null) {
                        stat_forwarded_to = "";
                    }
                    stat_forwarded_by = res1.getString("stat_forwarded_by");
                    if (stat_forwarded_by == null) {
                        stat_forwarded_by = "";
                    }
                    stat_remarks = res1.getString("stat_remarks");
                    if (stat_remarks == null) {
                        stat_remarks = "-";
                    } else // else added by pr on 7thdec18 to fix the bug for remarks coming under date field
                    {
                        if (stat_remarks.contains("~")) {
                            stat_remarks = stat_remarks.replace("~", "");
                        }
                    }
                    String str = res1.getString("stat_type") + "~" + stat_forwarded_by + "~" + stat_forwarded_by_user + "~" + stat_forwarded_to
                            + "~" + stat_forwarded_to_user + "~" + stat_remarks + "~" + res1.getString("stat_createdon");
                    //System.out.println(" inside searchStatusByRegNo func string to pass to js is " + str);

                    form_name = res1.getString("stat_form_type"); // line added by pr on 24thoct19

                    searchdata.add(str);
                    i++; // line added by pr on 15thmar18
                }
                if (searchdata.size() > 0) // if around added by pr on 2ndmay18
                {
                    // below if added by pr on 15thmar18
                    //if (finalStatType.equals("COORDINATOR_PENDING") || finalStatType.equals("DA_PENDING")) 
                    if (finalStatType.equals("coordinator_pending") || finalStatType.equals("da_pending") || finalStatType.equals("mail-admin_pending")) // line modified by pr on 15thnov18 to provide pull back from admin
                    {
                        showPullBack = true;
                    }
                    // get employment related details as well and store in a hashmap , start, code added by pr on 2ndmay18
                    ForwardAction fwdObj = new ForwardAction();
                    String formName = "";
                    formName = form_name; // line added by pr on 24thoct19
                    //System.out.println(" reg_no value is "+reg_no);
                    if (reg_no.toLowerCase().contains("single")) {
                        formName = Constants.SINGLE_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("sms")) {
                        formName = Constants.SMS_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("relay")) {
                        formName = Constants.RELAY_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("ldap")) {
                        formName = Constants.LDAP_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("bulkuser")) {
                        formName = Constants.BULK_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("bulkdlist")) { // Added by Rahul jan 2021
                        formName = Constants.BULKDIST_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("dlist")) {
                        formName = Constants.DIST_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("dns")) {
                        formName = Constants.DNS_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("gem")) {
                        formName = Constants.GEM_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("imappop")) {
                        formName = Constants.IMAP_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("wifiport")) {
                        formName = Constants.WIFI_PORT_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("ip")) {
                        formName = Constants.IP_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("nkn")) {
                        formName = Constants.NKN_SINGLE_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("wifi")) {
                        formName = Constants.WIFI_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("webcast")) {
                        formName = Constants.WEBCAST_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("vpn")) {
                        formName = Constants.VPN_SINGLE_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("centralutm")) {
                        formName = Constants.FIREWALL_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("dor_ext")) {
                        formName = Constants.DOR_EXT_FORM_KEYWORD;
                    }

                    //System.out.println(" formName value is "+formName);
                    if (!formName.equals("")) {
                        //System.out.println(" before empDetailsHM filling formnanme is " + formName + " reg_no is " + reg_no);
                        empDetailsHM = fwdObj.fetchEmploymentDetails(formName, reg_no);
                    }
                    //end, code added by pr on 2ndmay18
                }

//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo before try end empDetailsHM " + empDetailsHM + " searchdata is " + searchdata);
                // below lines added by pr on 6thmar18
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo " + ex.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (res1 != null) {
                    try {
                        res1.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 2 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 3 " + e.getMessage());

                    }
                }
            }
        }
        return SUCCESS;
    }

    public String searchStatusByRegNo() throws SQLException {
        initializeSessionValues();
        validation isNotValidEmail = new validation();

        if (searchedEmail != null && !searchedEmail.isEmpty() && !isNotValidEmail.EmailValidation(searchedEmail)) {
            Set<String> aliasisEmail = new Ldap().fetchAliases(searchedEmail);

            for (String email : aliasisEmail) {
                PreparedStatement pst = null;
                ResultSet rs = null;
                //Connection con = null;
                String form_name = ""; // line added by pr on 24thoct19
                try {
                    // below code added by pr on 19thdec17
                    //con = DbConnection.getConnection();
                    conSlave = DbConnection.getSlaveConnection();
                    String query = "SELECT * FROM final_audit_track where applicant_email=?";
                    pst = conSlave.prepareStatement(query);
                    pst.setString(1, email);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        String registrionNo = rs.getString("registration_no");
                        String applicantEmail = rs.getString("applicant_email");
                        String applicantMobileNo = rs.getString("applicant_mobile");
                        String status = rs.getString("status");
                        String date = rs.getString("to_datetime");

                        String str = registrionNo + "~" + applicantEmail + "~" + applicantMobileNo + "~" + status + "~" + date;
                        searchdata.add(str);
                        if (rs.isFirst()) {
                            ForwardAction fwdObj = new ForwardAction();
                            String formName = ""; // line added by pr on 24thoct19
                            //System.out.println(" reg_no value is "+reg_no);
                            if (registrionNo.toLowerCase().contains("single")) {
                                formName = Constants.SINGLE_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("sms")) {
                                formName = Constants.SMS_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("relay")) {
                                formName = Constants.RELAY_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("ldap")) {
                                formName = Constants.LDAP_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("bulkuser")) {
                                formName = Constants.BULK_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("bulkdlist")) { // Added by Rahul jan 2021
                                formName = Constants.BULKDIST_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("dlist")) {
                                formName = Constants.DIST_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("dns")) {
                                formName = Constants.DNS_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("gem")) {
                                formName = Constants.GEM_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("imappop")) {
                                formName = Constants.IMAP_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("wifiport")) {
                                formName = Constants.WIFI_PORT_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("ip")) {
                                formName = Constants.IP_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("nkn")) {
                                formName = Constants.NKN_SINGLE_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("wifi")) {
                                formName = Constants.WIFI_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("webcast")) {
                                formName = Constants.WEBCAST_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("vpn")) {
                                formName = Constants.VPN_SINGLE_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("centralutm")) {
                                formName = Constants.FIREWALL_FORM_KEYWORD;
                            } else if (registrionNo.toLowerCase().contains("dor_ext")) {
                                formName = Constants.DOR_EXT_FORM_KEYWORD;
                            }

                            empDetailsHM = fwdObj.fetchEmploymentDetails(formName, registrionNo);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(NewCAHome.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (pst != null) {
                        pst.close();
                    }

                }
            }
        } else if (reg_no != null && !reg_no.equals("") && reg_no.matches("^[a-zA-Z0-9~\\-]*$")) // line modified by pr on 5thfeb18 for xss
        {
            showPullBack = false; // line addde by pr on 15thmar18
            //searchdata.clear(); // line added by pr on 2ndmay18                
            empDetailsHM.clear(); // line added by pr on 2ndmay18   

            syncData(reg_no);
            PreparedStatement ps = null;
            ResultSet res1 = null;
            //Connection con = null;
            String form_name = ""; // line added by pr on 24thoct19
            try {
                // below code added by pr on 19thdec17
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                //String qry = "SELECT * FROM status WHERE stat_reg_no = ? ORDER BY stat_createdon DESC ";
                // stat_form_type added by pr on 24thoct19
                String qry = "SELECT stat_type,stat_forwarded_to_user,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_by,stat_remarks,stat_createdon,stat_form_type FROM status WHERE stat_reg_no = ? ORDER BY stat_id DESC "; // line modified by pr on 11thoct18
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo query is " + ps);

                res1 = ps.executeQuery();
                Forms frmObj = null;
                int i = 1; // line added by pr on 15thmar18
                String finalStatType = ""; // line added by pr on 15thmar18
                while (res1.next()) {
                    if (i == 1) // if added by pr on 15thmar18
                    {
                        finalStatType = res1.getString("stat_type");;
                    }
                    //System.out.println(" inside while ");
                    String stat_forwarded_by_user = "", stat_forwarded_to_user = "", stat_forwarded_by = "", stat_forwarded_to = "", stat_remarks = "";

                    if (res1.getString("stat_forwarded_to_user") != null) {
                        stat_forwarded_to_user = res1.getString("stat_forwarded_to_user");
                    }
                    if (res1.getString("stat_forwarded_by") != null) {
                        stat_forwarded_by_user = res1.getString("stat_forwarded_by_user");
                    }
                    // end, code added by pr on 28thmar19
                    stat_forwarded_to = res1.getString("stat_forwarded_to");
                    if (stat_forwarded_to == null) {
                        stat_forwarded_to = "";
                    }
                    stat_forwarded_by = res1.getString("stat_forwarded_by");
                    if (stat_forwarded_by == null) {
                        stat_forwarded_by = "";
                    }
                    stat_remarks = res1.getString("stat_remarks");
                    if (stat_remarks == null) {
                        stat_remarks = "-";
                    } else // else added by pr on 7thdec18 to fix the bug for remarks coming under date field
                    if (stat_remarks.contains("~")) {
                        stat_remarks = stat_remarks.replace("~", "");
                    }
                    String str = res1.getString("stat_type") + "~" + stat_forwarded_by + "~" + stat_forwarded_by_user + "~" + stat_forwarded_to
                            + "~" + stat_forwarded_to_user + "~" + stat_remarks + "~" + res1.getString("stat_createdon");
                    //System.out.println(" inside searchStatusByRegNo func string to pass to js is " + str);

                    form_name = res1.getString("stat_form_type"); // line added by pr on 24thoct19

                    searchdata.add(str);
                    i++; // line added by pr on 15thmar18
                }
                if (searchdata.size() > 0) // if around added by pr on 2ndmay18
                {
                    // below if added by pr on 15thmar18
                    //if (finalStatType.equals("COORDINATOR_PENDING") || finalStatType.equals("DA_PENDING")) 
                    if (finalStatType.equals("coordinator_pending") || finalStatType.equals("da_pending") || finalStatType.equals("mail-admin_pending")) // line modified by pr on 15thnov18 to provide pull back from admin
                    {
                        showPullBack = true;
                    }
                    // get employment related details as well and store in a hashmap , start, code added by pr on 2ndmay18
                    ForwardAction fwdObj = new ForwardAction();
                    String formName = "";
                    formName = form_name; // line added by pr on 24thoct19
                    //System.out.println(" reg_no value is "+reg_no);
                    if (reg_no.toLowerCase().contains("single")) {
                        formName = Constants.SINGLE_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("sms")) {
                        formName = Constants.SMS_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("relay")) {
                        formName = Constants.RELAY_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("ldap")) {
                        formName = Constants.LDAP_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("bulkuser")) {
                        formName = Constants.BULK_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("bulkdlist")) { // Added by Rahul jan 2021
                        formName = Constants.BULKDIST_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("dlist")) {
                        formName = Constants.DIST_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("dns")) {
                        formName = Constants.DNS_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("gem")) {
                        formName = Constants.GEM_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("imappop")) {
                        formName = Constants.IMAP_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("wifiport")) {
                        formName = Constants.WIFI_PORT_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("ip")) {
                        formName = Constants.IP_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("nkn")) {
                        formName = Constants.NKN_SINGLE_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("wifi")) {
                        formName = Constants.WIFI_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("webcast")) {
                        formName = Constants.WEBCAST_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("vpn")) {
                        formName = Constants.VPN_SINGLE_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("centralutm")) {
                        formName = Constants.FIREWALL_FORM_KEYWORD;
                    } else if (reg_no.toLowerCase().contains("dor_ext")) {
                        formName = Constants.DOR_EXT_FORM_KEYWORD;
                    }

                    //System.out.println(" formName value is "+formName);
                    if (!formName.equals("")) {
                        //System.out.println(" before empDetailsHM filling formnanme is " + formName + " reg_no is " + reg_no);
                        empDetailsHM = fwdObj.fetchEmploymentDetails(formName, reg_no);
                    }
                    //end, code added by pr on 2ndmay18
                }

//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo before try end empDetailsHM " + empDetailsHM + " searchdata is " + searchdata);
                // below lines added by pr on 6thmar18
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo " + ex.getMessage());
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 1 " + e.getMessage());

                    }
                }
                if (res1 != null) {
                    try {
                        res1.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 2 " + e.getMessage());
                    }
                }
                if (con != null) {
                    try {
                        // con.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 3 " + e.getMessage());

                    }
                }
            }
        }
        return SUCCESS;
    }

//    public String searchStatusByRegNo() throws SQLException {
//        initializeSessionValues();
//        validation isNotValidEmail=new validation();
//     
//        if(searchedEmail!=null && !searchedEmail.isEmpty() && !isNotValidEmail.EmailValidation(searchedEmail))
//        {
//          Set<String> aliasisEmail=new Ldap().fetchAliases(searchedEmail);
//            
//            for(String email:aliasisEmail)
//            {
//             PreparedStatement pst = null;
//             ResultSet rs = null;
//            //Connection con = null;
//            String form_name = ""; // line added by pr on 24thoct19
//            try {
//                // below code added by pr on 19thdec17
//                //con = DbConnection.getConnection();
//                conSlave = DbConnection.getSlaveConnection();
//                String query="SELECT * FROM final_audit_track where applicant_email=?";
//                 pst=conSlave.prepareStatement(query);
//                pst.setString(1,email);
//                rs= pst.executeQuery();
//                while(rs.next())
//                {
//                String registrionNo=rs.getString("registration_no");
//                String applicantEmail=rs.getString("applicant_email");
//                String applicantMobileNo=rs.getString("applicant_mobile");
//                String status=rs.getString("status");
//                String date=rs.getString("to_datetime");
//             
//                String str=registrionNo+"~"+applicantEmail+"~"+applicantMobileNo+"~"+status+"~"+date;
//                searchdata.add(str);
//                if(rs.isFirst()){
//                ForwardAction fwdObj = new ForwardAction();
//               String  formName = ""; // line added by pr on 24thoct19
//                    //System.out.println(" reg_no value is "+reg_no);
//                    if (registrionNo.toLowerCase().contains("single")) {
//                        formName = Constants.SINGLE_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("sms")) {
//                        formName = Constants.SMS_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("relay")) {
//                        formName = Constants.RELAY_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("ldap")) {
//                        formName = Constants.LDAP_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("bulkuser")) {
//                        formName = Constants.BULK_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("bulkdlist")) { // Added by Rahul jan 2021
//                        formName = Constants.BULKDIST_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("dlist")) {
//                        formName = Constants.DIST_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("dns")) {
//                        formName = Constants.DNS_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("gem")) {
//                        formName = Constants.GEM_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("imappop")) {
//                        formName = Constants.IMAP_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("wifiport")) {
//                        formName = Constants.WIFI_PORT_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("ip")) {
//                        formName = Constants.IP_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("nkn")) {
//                        formName = Constants.NKN_SINGLE_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("wifi")) {
//                        formName = Constants.WIFI_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("webcast")) {
//                        formName = Constants.WEBCAST_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("vpn")) {
//                        formName = Constants.VPN_SINGLE_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("centralutm")) {
//                        formName = Constants.FIREWALL_FORM_KEYWORD;
//                    } else if (registrionNo.toLowerCase().contains("dor_ext")) {
//                        formName = Constants.DOR_EXT_FORM_KEYWORD;
//                    }
//
//        validation isNotValidEmail = new validation();
//
//
//        if (searchedEmail != null && !searchedEmail.isEmpty() && !isNotValidEmail.EmailValidation(searchedEmail)) {
//            Set<String> aliasisEmail1 = new Ldap().fetchAliases(searchedEmail);
//
//            for (String email : aliasisEmail1) {
//                PreparedStatement pst = null;
//                ResultSet rs = null;
//                //Connection con = null;
//                String form_name = ""; // line added by pr on 24thoct19
//                try {
//                    // below code added by pr on 19thdec17
//                    //con = DbConnection.getConnection();
//                    conSlave = DbConnection.getSlaveConnection();
//                    String query = "SELECT * FROM final_audit_track where applicant_email=?";
//                    pst = conSlave.prepareStatement(query);
//                    pst.setString(1, email);
//                    rs = pst.executeQuery();
//                    while (rs.next()) {
//                        String registrionNo = rs.getString("registration_no");
//                        String applicantEmail = rs.getString("applicant_email");
//                        String applicantMobileNo = rs.getString("applicant_mobile");
//                        String status = rs.getString("status");
//                        String date = rs.getString("to_datetime");
//
//                        String str = registrionNo + "~" + applicantEmail + "~" + applicantMobileNo + "~" + status + "~" + date;
//                        searchdata.add(str);
//                        if (rs.isFirst()) {
//                            ForwardAction fwdObj = new ForwardAction();
//                            String formName = ""; // line added by pr on 24thoct19
//                            //System.out.println(" reg_no value is "+reg_no);
//                            if (registrionNo.toLowerCase().contains("single")) {
//                                formName = Constants.SINGLE_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("sms")) {
//                                formName = Constants.SMS_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("relay")) {
//                                formName = Constants.RELAY_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("ldap")) {
//                                formName = Constants.LDAP_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("bulkuser")) {
//                                formName = Constants.BULK_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("bulkdlist")) { // Added by Rahul jan 2021
//                                formName = Constants.BULKDIST_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("dlist")) {
//                                formName = Constants.DIST_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("dns")) {
//                                formName = Constants.DNS_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("gem")) {
//                                formName = Constants.GEM_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("imappop")) {
//                                formName = Constants.IMAP_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("wifiport")) {
//                                formName = Constants.WIFI_PORT_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("ip")) {
//                                formName = Constants.IP_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("nkn")) {
//                                formName = Constants.NKN_SINGLE_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("wifi")) {
//                                formName = Constants.WIFI_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("webcast")) {
//                                formName = Constants.WEBCAST_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("vpn")) {
//                                formName = Constants.VPN_SINGLE_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("centralutm")) {
//                                formName = Constants.FIREWALL_FORM_KEYWORD;
//                            } else if (registrionNo.toLowerCase().contains("dor_ext")) {
//                                formName = Constants.DOR_EXT_FORM_KEYWORD;
//                            }
//
//                            empDetailsHM = fwdObj.fetchEmploymentDetails(formName, registrionNo);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            Logger.getLogger(NewCAHome.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                    if (pst != null) {
//                        pst.close();
//                    }
//
//                }
//            }
//        } else if (reg_no != null && !reg_no.equals("") && reg_no.matches("^[a-zA-Z0-9~\\-]*$")) // line modified by pr on 5thfeb18 for xss
//        {
//            showPullBack = false; // line addde by pr on 15thmar18
//            //searchdata.clear(); // line added by pr on 2ndmay18                
//            empDetailsHM.clear(); // line added by pr on 2ndmay18   
//
//            syncData(reg_no);
//            PreparedStatement ps = null;
//            ResultSet res1 = null;
//            //Connection con = null;
//            String form_name = ""; // line added by pr on 24thoct19
//            try {
//                // below code added by pr on 19thdec17
//                //con = DbConnection.getConnection();
//                conSlave = DbConnection.getSlaveConnection();
//                //String qry = "SELECT * FROM status WHERE stat_reg_no = ? ORDER BY stat_createdon DESC ";
//                // stat_form_type added by pr on 24thoct19
//                String qry = "SELECT stat_type,stat_forwarded_to_user,stat_forwarded_to,stat_forwarded_by_user,stat_forwarded_by,stat_remarks,stat_createdon,stat_form_type FROM status WHERE stat_reg_no = ? ORDER BY stat_id DESC "; // line modified by pr on 11thoct18
//                ps = conSlave.prepareStatement(qry);
//                ps.setString(1, reg_no);
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo query is " + ps);
//
//                res1 = ps.executeQuery();
//                Forms frmObj = null;
//                int i = 1; // line added by pr on 15thmar18
//                String finalStatType = ""; // line added by pr on 15thmar18
//                while (res1.next()) {
//                    if (i == 1) // if added by pr on 15thmar18
//                    {
//                        finalStatType = res1.getString("stat_type");;
//                    }
//                    //System.out.println(" inside while ");
//                    String stat_forwarded_by_user = "", stat_forwarded_to_user = "", stat_forwarded_by = "", stat_forwarded_to = "", stat_remarks = "";
//
//                    if (res1.getString("stat_forwarded_to_user") != null) {
//                        stat_forwarded_to_user = res1.getString("stat_forwarded_to_user");
//                    }
//                    if (res1.getString("stat_forwarded_by") != null) {
//                        stat_forwarded_by_user = res1.getString("stat_forwarded_by_user");
//                    }
//                    // end, code added by pr on 28thmar19
//                    stat_forwarded_to = res1.getString("stat_forwarded_to");
//                    if (stat_forwarded_to == null) {
//                        stat_forwarded_to = "";
//                    }
//                    stat_forwarded_by = res1.getString("stat_forwarded_by");
//                    if (stat_forwarded_by == null) {
//                        stat_forwarded_by = "";
//                    }
//                    stat_remarks = res1.getString("stat_remarks");
//                    if (stat_remarks == null) {
//                        stat_remarks = "-";
//                    } else // else added by pr on 7thdec18 to fix the bug for remarks coming under date field
//                    if (stat_remarks.contains("~")) {
//                        stat_remarks = stat_remarks.replace("~", "");
//                    }
//                    String str = res1.getString("stat_type") + "~" + stat_forwarded_by + "~" + stat_forwarded_by_user + "~" + stat_forwarded_to
//                            + "~" + stat_forwarded_to_user + "~" + stat_remarks + "~" + res1.getString("stat_createdon");
//                    //System.out.println(" inside searchStatusByRegNo func string to pass to js is " + str);
//
//                    form_name = res1.getString("stat_form_type"); // line added by pr on 24thoct19
//
//                    searchdata.add(str);
//                    i++; // line added by pr on 15thmar18
//                }
//                if (searchdata.size() > 0) // if around added by pr on 2ndmay18
//                {
//                    // below if added by pr on 15thmar18
//                    //if (finalStatType.equals("COORDINATOR_PENDING") || finalStatType.equals("DA_PENDING")) 
//                    if (finalStatType.equals("coordinator_pending") || finalStatType.equals("da_pending") || finalStatType.equals("mail-admin_pending")) // line modified by pr on 15thnov18 to provide pull back from admin
//                    {
//                        showPullBack = true;
//                    }
//                    // get employment related details as well and store in a hashmap , start, code added by pr on 2ndmay18
//                    ForwardAction fwdObj = new ForwardAction();
//                    String formName = "";
//                    formName = form_name; // line added by pr on 24thoct19
//                    //System.out.println(" reg_no value is "+reg_no);
//                    if (reg_no.toLowerCase().contains("single")) {
//                        formName = Constants.SINGLE_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("sms")) {
//                        formName = Constants.SMS_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("relay")) {
//                        formName = Constants.RELAY_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("ldap")) {
//                        formName = Constants.LDAP_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("bulkuser")) {
//                        formName = Constants.BULK_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("bulkdlist")) { // Added by Rahul jan 2021
//                        formName = Constants.BULKDIST_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("dlist")) {
//                        formName = Constants.DIST_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("dns")) {
//                        formName = Constants.DNS_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("gem")) {
//                        formName = Constants.GEM_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("imappop")) {
//                        formName = Constants.IMAP_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("wifiport")) {
//                        formName = Constants.WIFI_PORT_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("ip")) {
//                        formName = Constants.IP_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("nkn")) {
//                        formName = Constants.NKN_SINGLE_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("wifi")) {
//                        formName = Constants.WIFI_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("webcast")) {
//                        formName = Constants.WEBCAST_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("vpn")) {
//                        formName = Constants.VPN_SINGLE_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("centralutm")) {
//                        formName = Constants.FIREWALL_FORM_KEYWORD;
//                    } else if (reg_no.toLowerCase().contains("dor_ext")) {
//                        formName = Constants.DOR_EXT_FORM_KEYWORD;
//                    }
//
//                    //System.out.println(" formName value is "+formName);
//                    if (!formName.equals("")) {
//                        //System.out.println(" before empDetailsHM filling formnanme is " + formName + " reg_no is " + reg_no);
//                        empDetailsHM = fwdObj.fetchEmploymentDetails(formName, reg_no);
//                    }
//                    //end, code added by pr on 2ndmay18
//                }
//
////                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
////                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo before try end empDetailsHM " + empDetailsHM + " searchdata is " + searchdata);
//                // below lines added by pr on 6thmar18
//            } catch (Exception ex) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo " + ex.getMessage());
//            } finally {
//                if (ps != null) {
//                    try {
//                        ps.close();
//                    } catch (Exception e) {
//                        //e.printStackTrace();
//
//                        // above line commented below added by pr on 23rdapr19
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 1 " + e.getMessage());
//
//                    }
//                }
//                if (res1 != null) {
//                    try {
//                        res1.close();
//                    } catch (Exception e) {
//                        //e.printStackTrace();
//
//                        // above line commented below added by pr on 23rdapr19
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 2 " + e.getMessage());
//                    }
//                }
//                if (con != null) {
//                    try {
//                        // con.close();
//                    } catch (Exception e) {
//                        //e.printStackTrace();
//
//                        // above line commented below added by pr on 23rdapr19
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByRegNo EXCEPTION 3 " + e.getMessage());
//
//                    }
//                }
//            }
//        }
//        return SUCCESS;
//    }
//
//    public String searchStatusByKeyword() {
//        initializeSessionValues();
//        searchformdata.clear();
//        msg = "";
//        if (skeyword != null && !skeyword.equals("") && (skeyword.matches("^[a-zA-Z0-9\\.\\+~\\- ]*$") || skeyword.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$"))
//                && srole != null && !srole.equals("") && (srole.matches("^[a-zA-Z0-9]*$"))) // line modified by pr on 5thfeb18 for xss
//        {
//            Boolean isKeyEmail = false;
//            ArrayList mailaddress = null;
//            if (skeyword.matches("^[\\w\\-\\.\\+]+@[a-zA-Z0-9\\.\\-]+.[a-zA-z0-9]{2,4}$")) {
//                isKeyEmail = true;
//            }
//            if (isKeyEmail) {
//                mailaddress = entities.LdapQuery.GetMailEqui(skeyword);
//            }
//            //System.out.println(" isKeyEmail value for " + skeyword + " is " + isKeyEmail + " mailaddress is " + mailaddress);
//            // if its an email then search in the LDAP for alias and save them in a arraylist            
//            try {
//                searchForms(skeyword, srole, isKeyEmail, mailaddress); // this will fill the searchformdata arraylist if records are found 
//                if (searchformdata == null || searchformdata.size() == 0) {
//                    msg = "No Forms Found !";
//                }
//            } catch (Exception ex) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
//                        + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchStatusByKeyword 1 " + ex.getMessage());
//            }
//        } else {
//            msg = "Please enter valid Keyword and choose valid Role";
//        }
//        //System.out.println(" searchformdata inside searchStatusByKeyword function is " + searchformdata);
//        return SUCCESS;
//    }
    // below function added by pr on 16thaug18
    public void searchForms(String skeyword, String srole, Boolean isKeyEmail, ArrayList mailaddress) {
        // based on srole fill the searchformdata arraylist
        initializeSessionValues();
        PreparedStatement ps = null;
        ResultSet res1 = null;
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            if (srole.equals("user")) {
                // search in all tables 
                String[] formNames = {"bulk_registration", "distribution_registration", "dns_registration", "gem_registration", "ip_registration", "imappop_registration", "ldap_registration",
                    "mobile_registration", "nkn_registration", "relay_registration", "single_registration", "sms_registration", "wifi_registration"}; // imappop_registration added by pr on 29thaug18
                try {
                    String str = "";
                    for (String form : formNames) {
                        String qry = "SELECT registration_no  FROM " + form + " WHERE ";
                        if (isKeyEmail) // if the keyword entered is an email then search in auth_email exact match
                        {
                            int i = 1;
                            for (Object email : mailaddress) {
                                if (i > 1) {
                                    qry += " OR ";
                                }
                                qry += " auth_email = '" + email + "'  ";
                                i++;
                            }
                        } else // search in name and designation with like 
                        {
                            qry += " auth_off_name like '" + skeyword + "%'  OR designation like '" + skeyword + "%'  OR mobile like '" + skeyword + "%' OR auth_email like '" + skeyword + "%' "
                                    + " OR add_state LIKE  '" + skeyword + "%' "; // state added by pr on 20thaug18
                        }
                        qry += " ORDER BY datetime desc  ";
                        ps = conSlave.prepareStatement(qry);
                        res1 = ps.executeQuery();
                        while (res1.next()) {
                            str = res1.getString("registration_no");
                            searchformdata.add(str);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 1 " + ex.getMessage());
                }
            } else if (srole.equals("ca")) {
                try {
                    // first search in comp_auth all the ca_ids formed from mailaddress emails                
                    // below code added by pr on 19thdec17
                    String str = "", qry = "";
                    ArrayList<String> ca_id_arr = new ArrayList<String>();
                    if (isKeyEmail) // if the keyword entered is an email then search in auth_email exact match
                    {
                        try {
                            qry = " SELECT ca_id, ca_email FROM comp_auth WHERE  "; // ca_email added by pr on 28thmar19
                            int i = 1;
                            for (Object email : mailaddress) {
                                if (i > 1) {
                                    qry += " OR ";
                                }
                                //qry += " auth_email = '" + email + "'  ";
                                qry += " ca_email = '" + email + "' ";
                                i++;
                            }
                            ps = conSlave.prepareStatement(qry);
                            //System.out.println(" ca related query 1 with keyword is " + ps);
                            res1 = ps.executeQuery();
                            while (res1.next()) {
                                //ca_id_arr.add(res1.getString("ca_id"));
                                ca_id_arr.add(res1.getString("ca_email")); // line modified by pr on 28thmar19
                            }
                        } catch (Exception ex) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 2 " + ex.getMessage());
                        }
                    } else // search in name and designation with like 
                    {
                        try {
                            qry = "SELECT ca_id, ca_email FROM comp_auth WHERE  ca_email LIKE '" + skeyword + "%'  OR ca_name LIKE '" + skeyword + "%' OR ca_mobile LIKE '" + skeyword + "%' ";
                            ps = conSlave.prepareStatement(qry);
                            //System.out.println(" ca id query 1 is " + ps);
                            res1 = ps.executeQuery();
                            while (res1.next()) {
                                //ca_id_arr.add(res1.getString("ca_id"));
                                ca_id_arr.add(res1.getString("ca_email")); // line modified by pr on 28thmar19
                            }
                        } catch (Exception ex) {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 3 " + ex.getMessage());
                        }
                    }
                    if (ca_id_arr.size() > 0) {
                        qry = "SELECT stat_reg_no  FROM status WHERE ";
                        int i = 1;
                        for (String ca_id : ca_id_arr) {
                            if (i > 1) {
                                qry += " OR ";
                            }
                            qry += " stat_forwarded_to_user = '" + ca_id + "' ";
                            i++;
                        }
                        qry += " ORDER BY stat_id desc  ";
                        ps = conSlave.prepareStatement(qry);
                        //System.out.println(" ca id query 2 is " + ps);
                        res1 = ps.executeQuery();
                        while (res1.next()) {
                            str = res1.getString("stat_reg_no");
                            searchformdata.add(str);
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 4 " + ex.getMessage());
                }
            } else if (srole.equals("co")) {
                try {
                    // below code added by pr on 19thdec17
                    String str = "";
                    String qry = "SELECT stat_reg_no  FROM status WHERE  stat_type = 'coordinator_pending' ";
                    if (isKeyEmail) // if the keyword entered is an email then search in auth_email exact match
                    {
                        int i = 1;
                        for (Object email : mailaddress) {
                            if (i > 1) {
                                qry += " OR ";
                            } else {
                                qry += "  AND ( ";
                            }
                            //qry += " auth_email = '" + email + "'  ";
                            qry += " stat_forwarded_to_user = '" + email + "'   OR stat_forwarded_to_user LIKE '%," + email + ",%'  OR stat_forwarded_to_user LIKE '%," + email + "'  "
                                    + "  OR stat_forwarded_to_user LIKE '" + email + ",%' OR stat_forwarded_to_user LIKE '%, " + email + ",%'   OR stat_forwarded_to_user LIKE '%, " + email + "'  ";
                            i++;
                        }
                        qry += " ) ";
                    } else // search in name and designation with like 
                    {
                        qry += " AND stat_forwarded_to_user LIKE '%" + skeyword + "%'  ";
                    }
                    //qry += " ORDER BY stat_createdon desc  ";
                    qry += " ORDER BY stat_id desc  "; // line modified by pr on 11thoct18
                    ps = conSlave.prepareStatement(qry);
                    res1 = ps.executeQuery();
                    while (res1.next()) {
                        str = res1.getString("stat_reg_no");
                        searchformdata.add(str);
                    }
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 5 " + e.getMessage());
                    //e.printStackTrace(); // commented by pr on 23rdapr19
                }
            } else if (srole.equals("da")) {
                try {
                    String str = "";
                    String qry = "SELECT stat_reg_no  FROM status WHERE  stat_type = 'da_pending' ";
                    if (isKeyEmail) // if the keyword entered is an email then search in auth_email exact match
                    {
                        int i = 1;
                        for (Object email : mailaddress) {
                            if (i > 1) {
                                qry += " OR ";
                            } else {
                                qry += "  AND ( ";
                            }
                            qry += " stat_forwarded_to_user = '" + email + "'   OR stat_forwarded_to_user LIKE '%," + email + ",%'  OR stat_forwarded_to_user LIKE '%," + email + "'  "
                                    + "  OR stat_forwarded_to_user LIKE '" + email + ",%' OR stat_forwarded_to_user LIKE '%, " + email + ",%'   OR stat_forwarded_to_user LIKE '%, " + email + "'  ";
                            i++;
                        }
                        qry += " ) ";
                    } else // search in name and designation with like 
                    {
                        qry += " AND stat_forwarded_to_user LIKE '%" + skeyword + "%' ";
                    }
                    //qry += " ORDER BY stat_createdon desc  ";
                    qry += " ORDER BY stat_id desc  ";
                    ps = conSlave.prepareStatement(qry);
                    res1 = ps.executeQuery();
                    while (res1.next()) {
                        str = res1.getString("stat_reg_no");
                        searchformdata.add(str);
                    }
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 6 " + e.getMessage());
                    //e.printStackTrace(); // commented by pr on 23rdapr19
                }
            } else if (srole.equals("admin")) {
                try {
                    String str = "";
                    String qry = "SELECT stat_reg_no  FROM status WHERE  stat_type = 'mail-admin_pending' ";
                    if (isKeyEmail) // if the keyword entered is an email then search in auth_email exact match
                    {
                        int i = 1;
                        for (Object email : mailaddress) {
                            if (i > 1) {
                                qry += " OR ";
                            } else {
                                qry += "  AND ( ";
                            }
                            qry += " stat_forwarded_to_user = '" + email + "'   OR stat_forwarded_to_user LIKE '%," + email + ",%'  OR stat_forwarded_to_user LIKE '%," + email + "'  "
                                    + "  OR stat_forwarded_to_user LIKE '" + email + ",%' OR stat_forwarded_to_user LIKE '%, " + email + ",%'   OR stat_forwarded_to_user LIKE '%, " + email + "'  ";
                            i++;
                        }
                        qry += " ) ";
                    } else // search in name and designation with like 
                    {
                        qry += " AND stat_forwarded_to_user LIKE '%" + skeyword + "%'  ";
                    }
                    qry += " ORDER BY stat_id desc  "; // line modified by pr on 11thoct18
                    ps = conSlave.prepareStatement(qry);
                    res1 = ps.executeQuery();
                    while (res1.next()) {
                        str = res1.getString("stat_reg_no");
                        searchformdata.add(str);
                    }
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 6 " + e.getMessage());
                    //e.printStackTrace(); // commented by pr on 23rdapr19
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();

            // above line commented below added by pr on 23rdapr19
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                    + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 6 EXCEPTION 1 " + ex.getMessage());

        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 6 EXCEPTION 2 " + e.getMessage());

                }
            }
            if (res1 != null) {
                try {
                    res1.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 6 EXCEPTION 3 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside searchForms 6 EXCEPTION 4 " + e.getMessage());

                }
            }
        }
    }

    public String fetchCAEmail(String ca_id) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String email = "";
        //Connection con = null;
        try {
            //con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();
            String qry = "  SELECT ca_email FROM comp_auth WHERE ca_id = ? ";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, ca_id);
            //System.out.println(" get ca email query is "+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                email = rs.getString("ca_email");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  get status reg no search results fetchCAEmail " + e.getMessage());
            //e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchCAEmail EXCEPTION 1 " + e.getMessage());

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchCAEmail EXCEPTION 2 " + e.getMessage());

                }
            }
            if (con != null) {
                try {
                    // con.close();
                } catch (Exception e) {
                    //e.printStackTrace();

                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchCAEmail EXCEPTION 3 " + e.getMessage());

                }
            }
        }
        return email;
    }

    //Add on 08-04-2022  
    private String findDoerEmailFromFinalAuditTrack(String status, String regNo, String applicantEmail, String caEmail, String usEmail, String supportEmail, String coordinatorEmail, String daEmail, String adminEmail) {
        if (status.contains("manual") || status.contains("domainapi") || status.contains("api")) {
            return applicantEmail;
        } else if (status.equalsIgnoreCase("coordinator_pending")) {
            return coordinatorEmail;
        } else if (status.equalsIgnoreCase("da_pending")) {
            return daEmail;
        } else if (status.equalsIgnoreCase("support_pending")) {
            return supportEmail;
        } else if (status.equalsIgnoreCase("mail-admin_pending")) {
            return adminEmail;
        } else if (status.equalsIgnoreCase("ca_pending")) {
            return caEmail;
        } else if (status.equalsIgnoreCase("us_pending")) {
            return usEmail;
        } else {
            return "";
        }
    }

    public void syncData(String stat_reg_no) {
        Map<String, Integer> map = new HashMap<>();
        map.put("manual_upload", 0);
        map.put("domainapi", 0);
        map.put("api", 0);
        map.put("cancel", 3);
        map.put("ca_pending", 4);
        map.put("ca_rejected", 5);
        map.put("us_pending", 6);
        map.put("us_rejected", 7);
        map.put("support_pending", 8);
        map.put("support_rejected", 9);
        map.put("coordinator_pending", 10);
        map.put("coordinator_rejected", 11);
        map.put("hog_pending", 12);
        map.put("hog_rejected", 13);
        map.put("mail-admin_pending", 14);
        map.put("mail-admin_rejected", 15);
        map.put("da_pending", 16);
        map.put("da_rejected", 17);
        Boolean isValid = false;
        PreparedStatement ps = null;
        ResultSet res = null;
        String toEmail = "", status = "", caEmail = "", supportEmail = "", usEmail = "", coordinatorEmail = "", daEmail = "", adminEmail = "", formName = "", applicantEmail = "";
        //Connection con = null;
        String stat_type_temp = "";
        int stat_id_temp = -1;
        String stat_reg_no_db_temp = "";
        String stat_form_type_db_temp = "";
        String stat_forwarded_to_db_temp = "";
        String stat_forwarded_by_db_temp = ""; // if it is forwarded by support before and again it needs to be forwarded again by support
        String stat_forwarded_to_user_db_temp = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry_final = "SELECT * FROM final_audit_track  WHERE registration_no = ?";
            PreparedStatement ps_final = conSlave.prepareStatement(qry_final);
            ps_final.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select FINAL_AUDIT_TRACK query to verfiy details is " + ps_final);
            ResultSet res_final = ps_final.executeQuery();

            if (res_final.first()) {
                if (res_final.getString("to_email") != null) {
                    toEmail = res_final.getString("to_email").trim();
                } else {
                    toEmail = "";
                }
                if (res_final.getString("status") != null) {
                    status = res_final.getString("status").trim();
                } else {
                    status = "";
                }
                if (res_final.getString("ca_email") != null) {
                    caEmail = res_final.getString("ca_email").trim();
                } else {
                    caEmail = "";
                }
                if (res_final.getString("support_email") != null) {
                    supportEmail = res_final.getString("support_email").trim();
                } else {
                    supportEmail = "";
                }
                if (res_final.getString("us_email") != null) {
                    usEmail = res_final.getString("us_email").trim();
                } else {
                    usEmail = "";
                }
                if (res_final.getString("da_email") != null) {
                    daEmail = res_final.getString("da_email").trim();
                } else {
                    daEmail = "";
                }
                if (res_final.getString("admin_email") != null) {
                    adminEmail = res_final.getString("admin_email").trim();
                } else {
                    adminEmail = "";
                }
                if (res_final.getString("coordinator_email") != null) {
                    coordinatorEmail = res_final.getString("coordinator_email").trim();
                } else {
                    coordinatorEmail = "";
                }
                if (res_final.getString("form_name") != null) {
                    formName = res_final.getString("form_name").trim();
                } else {
                    formName = "";
                }
                if (res_final.getString("applicant_email") != null) {
                    applicantEmail = res_final.getString("applicant_email").trim();
                } else {
                    applicantEmail = "";
                }

            }

            String qry = "SELECT stat_id,stat_type,stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by, stat_forwarded_by_user FROM status  WHERE stat_reg_no = ? ORDER BY stat_id DESC";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps);
            res = ps.executeQuery();
            String stat_type = "", stat_reg_no_db = "", stat_form_type_db = "", stat_forwarded_to_db = "", stat_forwarded_by_db = "", stat_forwarded_by_user_db = "", stat_forwarded_to_user_db = "";
            int stat_id = -1;
            while (res.next()) {
                if (res.getString("stat_type") != null) {
                    stat_type = res.getString("stat_type").trim();
                } else {
                    stat_type = "";
                }
                if (res.getInt("stat_id") != -1) {
                    stat_id = res.getInt("stat_id");
                } else {
                    stat_id = -1;
                }
                if (res.getString("stat_reg_no") != null) {
                    stat_reg_no_db = res.getString("stat_reg_no").trim();
                } else {
                    stat_reg_no_db = "";
                }
                if (res.getString("stat_form_type") != null) {
                    stat_form_type_db = res.getString("stat_form_type").trim();
                } else {
                    stat_form_type_db = "";
                }
                if (res.getString("stat_forwarded_to") != null) {
                    stat_forwarded_to_db = res.getString("stat_forwarded_to").trim();
                } else {
                    stat_forwarded_to_db = "";
                }
                if (res.getString("stat_forwarded_by") != null) {
                    stat_forwarded_by_db = res.getString("stat_forwarded_by").trim(); // if it is forwarded by support before and again it needs to be forwarded again by support
                } else {
                    stat_forwarded_by_db = "";
                }
                if (res.getString("stat_forwarded_by_user") != null) {
                    stat_forwarded_by_user_db = res.getString("stat_forwarded_by_user").trim();
                } else {
                    stat_forwarded_by_user_db = "";
                }
                if (res.getString("stat_forwarded_to_user") != null) {
                    stat_forwarded_to_user_db = res.getString("stat_forwarded_to_user").trim();
                } else {
                    stat_forwarded_to_user_db = "";
                }
                String updateQry = "";
                String stat_forwarded_by_db_manually = "", stat_forwarded_by_user_db_manually = "";
                if (status.equalsIgnoreCase(stat_type)) {
                    if (status.equalsIgnoreCase("completed")) {
                        if (stat_forwarded_by_db != null && !stat_forwarded_by_db.isEmpty()) {
                            stat_forwarded_by_db_manually = stat_forwarded_by_db;
                        } else {
                            stat_forwarded_by_db_manually = "m";
                        }
                        if (stat_forwarded_by_user_db != null && !stat_forwarded_by_user_db.isEmpty()) {
                            stat_forwarded_by_user_db_manually = stat_forwarded_by_user_db;
                        } else {
                            stat_forwarded_by_user_db_manually = adminEmail;
                        }

                        if (!formName.equalsIgnoreCase(stat_form_type_db)) {
                            updateQry = "UPDATE status SET stat_forwarded_by=? , stat_forwarded_by_user  = ?, stat_forwarded_by_email=?,stat_form_type  = ?  where stat_id=?";
                            ps = con.prepareStatement(updateQry);
                            ps.setString(1, stat_forwarded_by_db_manually);
                            ps.setString(2, stat_forwarded_by_user_db_manually);
                            ps.setString(3, stat_forwarded_by_user_db_manually);
                            ps.setString(4, formName);
                            ps.setInt(5, stat_id);
                            int updatedRow = ps.executeUpdate();
                            if (updatedRow > 0) {
                                return;
                            }
                        } else {
                            updateQry = "UPDATE status SET stat_forwarded_by=? , stat_forwarded_by_user  = ?, stat_forwarded_by_email=? where stat_id=?";
                            ps = con.prepareStatement(updateQry);
                            ps.setString(1, stat_forwarded_by_db_manually);
                            ps.setString(2, stat_forwarded_by_user_db_manually);
                            ps.setString(3, stat_forwarded_by_user_db_manually);
                            ps.setInt(4, stat_id);
                            int updatedRow = ps.executeUpdate();
                            if (updatedRow > 0) {
                                return;
                            }
                        }
                    } else if (!toEmail.equalsIgnoreCase(stat_forwarded_to_user_db) || !formName.equalsIgnoreCase(stat_form_type_db) || stat_forwarded_by_db == null || stat_forwarded_by_db.isEmpty() || stat_forwarded_by_user_db == null || stat_forwarded_by_user_db.isEmpty() || stat_forwarded_to_user_db == null || stat_forwarded_to_user_db.isEmpty()) {
                        // Sync the data 
                        // Update stat forwarded to user db in status table .....

                        updateQry = "UPDATE status SET stat_forwarded_to_user  = ?,stat_form_type  = ? , stat_forwarded_to=?, stat_forwarded_by=?, stat_forwarded_by_user=?, stat_forwarded_by_email=? where stat_id=?";
                        ps = con.prepareStatement(updateQry);
                        ps.setString(1, toEmail);
                        ps.setString(2, formName);
                        ps.setString(3, findStatForwardedTo(status));
                        if (stat_type_temp.isEmpty()) {
                            String qry1 = "SELECT stat_id,stat_type,stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by, stat_forwarded_by_user FROM status  WHERE stat_reg_no = ?";
                            PreparedStatement ps1 = conSlave.prepareStatement(qry1);
                            ps1.setString(1, stat_reg_no);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps1);
                            ResultSet res1 = ps1.executeQuery();
                            String previous_status = "", prev_stat_forwarded_to = "", prev_stat_forwarded_to_user = "";
                            while (res1.next()) {
                                if (!status.equalsIgnoreCase(res1.getString("stat_type"))) {
                                    previous_status = res1.getString("stat_type");
                                    prev_stat_forwarded_to = res1.getString("stat_forwarded_to");
                                    prev_stat_forwarded_to_user = res1.getString("stat_forwarded_to_user");
                                }
                                break;
                            }
                            stat_type_temp = previous_status;
                        }
                        ps.setString(4, findStatForwardedTo(stat_type_temp));
                        ps.setString(5, findDoerEmailFromFinalAuditTrack(stat_type_temp, stat_reg_no, applicantEmail, caEmail, usEmail, supportEmail, coordinatorEmail, daEmail, adminEmail));
                        ps.setString(6, findDoerEmailFromFinalAuditTrack(stat_type_temp, stat_reg_no, applicantEmail, caEmail, usEmail, supportEmail, coordinatorEmail, daEmail, adminEmail));
                        ps.setInt(7, stat_id);
                        int updatedRow = ps.executeUpdate();
                        if (updatedRow > 0) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (map.get(status) > map.get(stat_type)) {
                    // Insert row in status table
                    String qry_status = "INSERT INTO status SET stat_type = ?, stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = ?, stat_forwarded_by_user = ?, stat_forwarded_to = ?,"
                            + ""
                            + "  stat_forwarded_to_user = ? , stat_remarks = ?"; //, stat_process='online'
                    PreparedStatement ps_status = con.prepareStatement(qry_status);
                    ps_status.setString(1, status);
                    //ps.setString(2,)
                    ps_status.setString(2, formName);
                    ps_status.setString(3, stat_reg_no);
                    ps_status.setString(4, stat_forwarded_to_db);
                    ps_status.setString(5, stat_forwarded_to_user_db);
                    ps_status.setString(6, findStatForwardedTo(res_final.getString("status")));
                    ps_status.setString(7, res_final.getString("to_email"));
                    ps_status.setString(8, "Manually done by the backend team to resolve the conflict");
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insert status for manual query is " + ps_status);
                    int result = ps_status.executeUpdate();
                    return;
                } else {
                    // delete row from status table 
                    updateQry = "delete from status where stat_id=?";
                    ps = conSlave.prepareStatement(updateQry);
                    ps.setInt(1, stat_id);
                    ps.executeUpdate();
                }

                stat_type_temp = stat_type;
                stat_id_temp = stat_id;
                stat_reg_no_db_temp = stat_reg_no_db;
                stat_form_type_db_temp = stat_form_type_db;
                stat_forwarded_to_db_temp = stat_forwarded_to_db;
                stat_forwarded_by_db_temp = stat_forwarded_by_db; // if it is forwarded by support before and again it needs to be forwarded again by support
                stat_forwarded_to_user_db_temp = stat_forwarded_to_user_db;
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
        return;
    }

    public void syncData_Backup(String stat_reg_no) {
        Map<String, Integer> map = new HashMap<>();
        map.put("manual_upload", 0);
        map.put("domainapi", 0);
        map.put("api", 0);
        map.put("cancel", 3);
        map.put("ca_pending", 4);
        map.put("ca_rejected", 5);
        map.put("us_pending", 6);
        map.put("us_rejected", 7);
        map.put("support_pending", 8);
        map.put("support_rejected", 9);
        map.put("coordinator_pending", 10);
        map.put("coordinator_rejected", 11);
        map.put("hog_pending", 12);
        map.put("hog_rejected", 13);
        map.put("mail-admin_pending", 14);
        map.put("mail-admin_rejected", 15);
        map.put("da_pending", 16);
        map.put("da_rejected", 17);
        Boolean isValid = false;
        PreparedStatement ps = null;
        ResultSet res = null;
        String toEmail = "", status = "", caEmail = "", supportEmail = "", usEmail = "", coordinatorEmail = "", daEmail = "", adminEmail = "", formName = "", applicantEmail = "";
        //Connection con = null;
        String stat_type_temp = "";
        int stat_id_temp = -1;
        String stat_reg_no_db_temp = "";
        String stat_form_type_db_temp = "";
        String stat_forwarded_to_db_temp = "";
        String stat_forwarded_by_db_temp = ""; // if it is forwarded by support before and again it needs to be forwarded again by support
        String stat_forwarded_to_user_db_temp = "";
        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection();

            String qry_final = "SELECT * FROM final_audit_track  WHERE registration_no = ?";
            PreparedStatement ps_final = conSlave.prepareStatement(qry_final);
            ps_final.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select FINAL_AUDIT_TRACK query to verfiy details is " + ps_final);
            ResultSet res_final = ps_final.executeQuery();

            if (res_final.next()) {
                toEmail = res_final.getString("to_email");
                status = res_final.getString("status");
                caEmail = res_final.getString("ca_email");
                supportEmail = res_final.getString("support_email");
                usEmail = res_final.getString("us_email");
                daEmail = res_final.getString("da_email");
                adminEmail = res_final.getString("admin_email");
                coordinatorEmail = res_final.getString("coordinator_email");
                formName = res_final.getString("form_name");
                applicantEmail = res_final.getString("applicant_email");

            }

            String qry = "SELECT stat_id,stat_type,stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by, stat_forwarded_by_user FROM status  WHERE stat_reg_no = ? ORDER BY stat_id DESC";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, stat_reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps);
            res = ps.executeQuery();
            String stat_type = "", stat_reg_no_db = "", stat_form_type_db = "", stat_forwarded_to_db = "", stat_forwarded_by_db = "", stat_forwarded_by_user_db = "", stat_forwarded_to_user_db = "";
            int stat_id = -1;
            while (res.next()) {
                if (res.getString("stat_type") != null) {
                    stat_type = res.getString("stat_type").trim();
                } else {
                    stat_type = "";
                }
                if (res.getInt("stat_id") != -1) {
                    stat_id = res.getInt("stat_id");
                } else {
                    stat_id = -1;
                }
                if (res.getString("stat_reg_no") != null) {
                    stat_reg_no_db = res.getString("stat_reg_no").trim();
                } else {
                    stat_reg_no_db = "";
                }
                if (res.getString("stat_form_type") != null) {
                    stat_form_type_db = res.getString("stat_form_type").trim();
                } else {
                    stat_form_type_db = "";
                }
                if (res.getString("stat_forwarded_to") != null) {
                    stat_forwarded_to_db = res.getString("stat_forwarded_to").trim();
                } else {
                    stat_forwarded_to_db = "";
                }
                if (res.getString("stat_forwarded_by") != null) {
                    stat_forwarded_by_db = res.getString("stat_forwarded_by").trim(); // if it is forwarded by support before and again it needs to be forwarded again by support
                } else {
                    stat_forwarded_by_db = "";
                }
                if (res.getString("stat_forwarded_by_user") != null) {
                    stat_forwarded_by_user_db = res.getString("stat_forwarded_by_user").trim();
                } else {
                    stat_forwarded_by_user_db = "";
                }
                if (res.getString("stat_forwarded_to_user") != null) {
                    stat_forwarded_to_user_db = res.getString("stat_forwarded_to_user").trim();
                } else {
                    stat_forwarded_to_user_db = "";
                }
                String updateQry = "";
                String stat_forwarded_by_db_manually = "", stat_forwarded_by_user_db_manually = "";
                if (status.equalsIgnoreCase(stat_type)) {
                    if (status.equalsIgnoreCase("completed")) {
                        if (stat_forwarded_by_db != null && !stat_forwarded_by_db.isEmpty()) {
                            stat_forwarded_by_db_manually = stat_forwarded_by_db;
                        } else {
                            stat_forwarded_by_db_manually = "m";
                        }
                        if (stat_forwarded_by_user_db != null && !stat_forwarded_by_user_db.isEmpty()) {
                            stat_forwarded_by_user_db_manually = stat_forwarded_by_user_db;
                        } else {
                            stat_forwarded_by_user_db_manually = adminEmail;
                        }

                        if (!formName.equalsIgnoreCase(stat_form_type_db)) {
                            updateQry = "UPDATE status SET stat_forwarded_by=? , stat_forwarded_by_user  = ?, stat_forwarded_by_email=?,stat_form_type  = ?  where stat_id=?";
                            ps = conSlave.prepareStatement(updateQry);
                            ps.setString(1, stat_forwarded_by_db_manually);
                            ps.setString(2, stat_forwarded_by_user_db_manually);
                            ps.setString(3, stat_forwarded_by_user_db_manually);
                            ps.setString(4, formName);
                            ps.setInt(5, stat_id);
                            int updatedRow = ps.executeUpdate();
                            if (updatedRow > 0) {
                                return;
                            }
                        } else {
                            updateQry = "UPDATE status SET stat_forwarded_by=? , stat_forwarded_by_user  = ?, stat_forwarded_by_email=? where stat_id=?";
                            ps = conSlave.prepareStatement(updateQry);
                            ps.setString(1, stat_forwarded_by_db_manually);
                            ps.setString(2, stat_forwarded_by_user_db_manually);
                            ps.setString(3, stat_forwarded_by_user_db_manually);
                            ps.setInt(4, stat_id);
                            int updatedRow = ps.executeUpdate();
                            if (updatedRow > 0) {
                                return;
                            }
                        }
                    } else if (!toEmail.equalsIgnoreCase(stat_forwarded_to_user_db) || !formName.equalsIgnoreCase(stat_form_type_db) || stat_forwarded_by_db == null || stat_forwarded_by_db.isEmpty() || stat_forwarded_by_user_db == null || stat_forwarded_by_user_db.isEmpty() || stat_forwarded_to_user_db == null || stat_forwarded_to_user_db.isEmpty()) {
                        // Sync the data 
                        // Update stat forwarded to user db in status table .....

                        updateQry = "UPDATE status SET stat_forwarded_to_user  = ?,stat_form_type  = ? , stat_forwarded_to=?, stat_forwarded_by=?, stat_forwarded_by_user=?, stat_forwarded_by_email=? where stat_id=?";
                        ps = conSlave.prepareStatement(updateQry);
                        ps.setString(1, toEmail);
                        ps.setString(2, formName);
                        ps.setString(3, findStatForwardedTo(status));
                        if (stat_type_temp.isEmpty()) {
                            String qry1 = "SELECT count(*) as c FROM status  WHERE stat_reg_no = ?";
                            PreparedStatement ps1 = conSlave.prepareStatement(qry1);
                            ps1.setString(1, stat_reg_no);
                            ResultSet res1 = ps1.executeQuery();
                            int secondLastStatId = -1;
                            int totalRecords = -1;
                            while (res1.next()) {
                                totalRecords = res1.getInt("c");
                            }

                            qry1 = "SELECT stat_id FROM status  WHERE stat_reg_no = ?";
                            ps1 = conSlave.prepareStatement(qry1);
                            ps1.setString(1, stat_reg_no);
                            res1 = ps1.executeQuery();
                            for (int i = 0; i < totalRecords - 1; i++) {
                                secondLastStatId = res1.getInt("stat_id");
                            }

                            qry1 = "SELECT stat_id,stat_type,stat_reg_no,stat_form_type,stat_forwarded_to,stat_forwarded_to_user,stat_forwarded_by, stat_forwarded_by_user FROM status  WHERE stat_id=?";
                            ps1 = conSlave.prepareStatement(qry1);
                            ps1.setInt(1, secondLastStatId);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " select query to verfiy details is " + ps1);
                            res1 = ps1.executeQuery();
                            String previous_status = "", prev_stat_forwarded_to = "", prev_stat_forwarded_to_user = "";
                            if (res1.next()) {
                                previous_status = res1.getString("stat_type");
                                prev_stat_forwarded_to = res1.getString("stat_forwarded_to");
                                prev_stat_forwarded_to_user = res1.getString("stat_forwarded_to_user");
                            }
                            stat_type_temp = previous_status;
                        }
                        ps.setString(4, findStatForwardedTo(stat_type_temp));
                        ps.setString(5, findDoerEmailFromFinalAuditTrack(stat_type_temp, stat_reg_no, applicantEmail, caEmail, usEmail, supportEmail, coordinatorEmail, daEmail, adminEmail));
                        ps.setString(6, findDoerEmailFromFinalAuditTrack(stat_type_temp, stat_reg_no, applicantEmail, caEmail, usEmail, supportEmail, coordinatorEmail, daEmail, adminEmail));
                        ps.setInt(7, stat_id);
                        int updatedRow = ps.executeUpdate();
                        if (updatedRow > 0) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else if (map.get(status) > map.get(stat_type)) {
                    // Insert row in status table
                    String qry_status = "INSERT INTO status SET stat_type = ?, stat_form_type = ?, stat_reg_no = ?, stat_forwarded_by = ?, stat_forwarded_by_user = ?, stat_forwarded_to = ?,"
                            + ""
                            + "  stat_forwarded_to_user = ? , stat_remarks = ?"; //, stat_process='online'
                    PreparedStatement ps_status = con.prepareStatement(qry_status);
                    ps_status.setString(1, status);
                    //ps.setString(2,)
                    ps_status.setString(2, formName);
                    ps_status.setString(3, stat_reg_no);
                    ps_status.setString(4, stat_forwarded_to_db);
                    ps_status.setString(5, stat_forwarded_to_user_db);
                    ps_status.setString(6, findStatForwardedTo(res_final.getString("status")));
                    ps_status.setString(7, res_final.getString("to_email"));
                    ps_status.setString(8, "Manually done by the backend team to resolve the conflict :: called synData()");
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " insert status for manual query is " + ps_status);
                    int result = ps_status.executeUpdate();
                    return;
                } else {
                    // delete row from status table 
                    updateQry = "delete from status where stat_id=?";
                    ps = conSlave.prepareStatement(updateQry);
                    ps.setInt(1, stat_id);
                    ps.executeUpdate();
                }

                stat_type_temp = stat_type;
                stat_id_temp = stat_id;
                stat_reg_no_db_temp = stat_reg_no_db;
                stat_form_type_db_temp = stat_form_type_db;
                stat_forwarded_to_db_temp = stat_forwarded_to_db;
                stat_forwarded_by_db_temp = stat_forwarded_by_db; // if it is forwarded by support before and again it needs to be forwarded again by support
                stat_forwarded_to_user_db_temp = stat_forwarded_to_user_db;
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
        return;
    }// CREATED AS A BACKUP METHOD BY PRABHAT ON 03-08-2022 

    private String findStatForwardedTo(String status) {
        if (status.contains("manual") || status.contains("domainapi") || status.contains("api")) {
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
        } else if (status.equalsIgnoreCase("us_pending")) {
            return "u";
        } else {
            return "";
        }
    }

    public String fetchTrackAllBackup() {
        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();

        if (!CSRFRandom.equals(SessionCSRFRandom)) {
            hmTrack.put("csrf_error", "CSRF Token is invalid.");
            return SUCCESS;
        }
        UserData userdata = (UserData) sessionMap.get("uservalues");

        //System.out.println(" inside fetchtrackall function reg_no value is " + reg_no);
        List<String> arrRoles = new ArrayList<>(); // this will store all the roles
        List<String> arrFinalRoles = new ArrayList<>(); // this will store all the roles
        List<String> arrTRoles = new ArrayList<>(); // this will store all the roles
        String msg = ""; // this will store all the details
        String status = "", status_msg = "", statusFromDb = ""; // this will store the info whether it is pending, rejected/us_expired or completed , this will contain values like p, r, c
        PreparedStatement ps = null;
        ResultSet rs = null;
        String applicantname = "";
        String applicant_mob = "";
        String app_email = "";
        String form_name = "";
        String app_sub_date = "";
        String recv_date = "", recv_email = "", reqProcessedAs = "", app_date_text = "", senderEmail = "", senderMobile = "", senderName = "", submitTime = "", forwardedBy = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select stat_type,stat_forwarded_by,stat_forwarded_to from status where stat_reg_no = ?  order by stat_id";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchTrackAll query is " + ps);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                arrRoles.add(rs.getString("stat_forwarded_by") + "=>" + rs.getString("stat_forwarded_to"));
                if (rs.isLast() && i > 0) {
                    arrRoles.add(rs.getString("stat_forwarded_to"));
                }
                ++i;
            }
            Collections.reverse(arrRoles);
            for (String arrRole : arrRoles) {
                if (arrRole != null) {
                    arrFinalRoles.add(arrRole);
//                    if (!arrRole.equals("null")) {
//                        String[] splitted = arrRole.split("=>");
//                        if (!splitted[0].equals("null")) {
//                            arrFinalRoles.add(arrRole);
//                        }
//                    }
                }
            }
            Collections.reverse(arrFinalRoles);

            arrRoles = arrFinalRoles;
            srole = arrRoles.get(arrRoles.size() - 1);

            qry = "select applicant_email, applicant_mobile, applicant_name, applicant_ip, applicant_datetime, applicant_remarks, to_email, to_datetime, status, form_name from final_audit_track where registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                statusFromDb = rs.getString("status").toLowerCase();
                if (rs.getString("status").toLowerCase().contains("pending") || rs.getString("status").toLowerCase().contains("manual")) {
                    status = "pending";
                } else if (rs.getString("status").toLowerCase().contains("rejected") || rs.getString("status").toLowerCase().contains("cancel") || rs.getString("status").toLowerCase().contains("us_expired")) {
                    status = "reject";
                } else if (rs.getString("status").toLowerCase().contains("completed")) {
                    status = "active";
                }

                applicantname = rs.getString("applicant_name");
                applicant_mob = rs.getString("applicant_mobile");
                app_email = rs.getString("applicant_email");
                form_name = rs.getString("form_name");
                app_sub_date = rs.getString("applicant_datetime");
                recv_date = rs.getString("to_datetime");
                recv_email = rs.getString("to_email");
                status_msg = getStatTypeString(rs.getString("status").toLowerCase()) + "(" + recv_email + ")";

                if (rs.getString("status").toLowerCase().contains("pending")) {
                    reqProcessedAs = "Pending";
                    app_date_text = "Pending Since ";
                } else if (rs.getString("status").toLowerCase().contains("rejected")) {
                    reqProcessedAs = "Rejected";
                    app_date_text = "Rejection Date";
                } else if (rs.getString("status").toLowerCase().contains("cancel")) {
                    reqProcessedAs = "Cancelled";
                    app_date_text = "Cancellation Date";
                } else if (rs.getString("status").toLowerCase().contains("completed")) {
                    reqProcessedAs = "Completed";
                    app_date_text = "Completion Date";
                } else {
                    app_date_text = "Submission Date";
                }
            }

            qry = "select * from status where stat_reg_no = ? order by stat_id desc limit 1";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                senderEmail = rs.getString("stat_forwarded_by_email");
                senderMobile = rs.getString("stat_forwarded_by_mobile");
                senderName = rs.getString("stat_forwarded_by_name");
                submitTime = rs.getString("stat_forwarded_by_datetime");
                forwardedBy = rs.getString("stat_forwarded_by");
                if (senderEmail == null) {
                    senderEmail = rs.getString("stat_forwarded_by_user");
                } else if (senderEmail.isEmpty()) {
                    senderEmail = rs.getString("stat_forwarded_by_user");
                }

                if (senderEmail == null) {
                    if (forwardedBy != null && forwardedBy.equalsIgnoreCase("a")) {
                        senderEmail = userdata.getEmail();
                        if (senderName == null) {
                            senderName = "Yourself ";
                        } else if (senderName.isEmpty()) {
                            senderName = "Yourself ";
                        }
                    }
                } else if (senderEmail.isEmpty()) {
                    if (forwardedBy != null && forwardedBy.equalsIgnoreCase("a")) {
                        senderEmail = userdata.getEmail();
                        if (senderName == null) {
                            senderName = "Yourself ";
                        } else if (senderName.isEmpty()) {
                            senderName = "Yourself ";
                        }
                    }
                }
            }
            if (status.equals("")) {
                status = "Application cancelled by User.";
            }

            if (senderEmail != null && senderName != null) {
                if (senderEmail.isEmpty() && senderName.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> ";
                } else if (senderEmail.isEmpty() && !senderName.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderName;
                } else if (senderName.isEmpty() && !senderEmail.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderEmail;
                } else {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderName + "(" + senderEmail + ")";
                }
            } else if (senderEmail == null && senderName != null) {
                if (senderName.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> ";
                } else {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderName;
                }
            } else if (senderName == null && senderEmail != null) {
                if (senderEmail.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> ";
                } else {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderEmail;
                }
            } else if (senderEmail == null && senderName == null) {
                msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> ";
            }

            hmTrack.put("roles", arrRoles);
            hmTrack.put("troles", arrTRoles);
            hmTrack.put("status", status);
            hmTrack.put("name", applicantname);
            hmTrack.put("mob", applicant_mob);
            hmTrack.put("email", app_email);
            hmTrack.put("form_name", form_name);
            hmTrack.put("forword_date", app_sub_date);
            hmTrack.put("msg", msg);
            hmTrack.put("reg_no", reg_no);
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchTrackAll hmTrack valyue is " + hmTrack);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackAll catch " + e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());
                }
            }
        }
        return SUCCESS;
    }

    // Addition of some code on 08-04-2022  By AT  
    public String fetchTrackAll() {
        String SessionCSRFRandom = sessionMap.get("CSRFRandom").toString();

        if (!CSRFRandom.equals(SessionCSRFRandom)) {
            hmTrack.put("csrf_error", "CSRF Token is invalid.");
            return SUCCESS;
        }
        UserData userdata = (UserData) sessionMap.get("uservalues");

        //System.out.println(" inside fetchtrackall function reg_no value is " + reg_no);
        List<String> arrRoles = new ArrayList<>(); // this will store all the roles
        List<String> arrFinalRoles = new ArrayList<>(); // this will store all the roles
        List<String> arrTRoles = new ArrayList<>(); // this will store all the roles
        String msg = ""; // this will store all the details
        String status = "", status_msg = "", statusFromDb = "", formType = ""; // this will store the info whether it is pending, rejected/us_expired or completed , this will contain values like p, r, c
        PreparedStatement ps = null;
        ResultSet rs = null;
        String applicantname = "";
        String applicant_mob = "";
        String app_email = "";
        String form_name = "";
        String app_sub_date = "";
        String recv_date = "", recv_email = "", reqProcessedAs = "", app_date_text = "", senderEmail = "", senderMobile = "", senderName = "", submitTime = "", forwardedBy = "", recipientType = "", remarks = "";

        syncData(reg_no);

        try {
            conSlave = DbConnection.getSlaveConnection();

            String qry = "select * from status where stat_reg_no = ?  order by stat_id";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchTrackAll query is " + ps);
            rs = ps.executeQuery();
            int i = 0;
            while (rs.next()) {
                status = rs.getString("stat_type");
                forwardedBy = rs.getString("stat_forwarded_by");
                recipientType = rs.getString("stat_forwarded_to");
                senderMobile = rs.getString("stat_forwarded_by_mobile");
                remarks = rs.getString("stat_remarks");
                senderEmail = rs.getString("stat_forwarded_by_email");
                senderName = rs.getString("stat_forwarded_by_name");
                submitTime = rs.getString("stat_forwarded_by_datetime");
                formType = rs.getString("stat_form_type");
                if (i == 0) {
                    if (status.contains("api") || status.contains("manual")) {
                        arrRoles.add("a=>a");
                    } else {
                        arrRoles.add("a=>" + recipientType);
                    }
                } else {
                    arrRoles.add(forwardedBy + "=>" + recipientType);
                }

                if (i == 0 && rs.isLast()) {
                    if (arrRoles.size() > 0) {
                        if (!formType.equalsIgnoreCase("dor_ext_retired")) {
                            arrRoles.add(recipientType + "=>");
                        }
                    }
                } else if (rs.isLast() && i > 0) {
                    if (arrRoles.size() > 0) {
                        arrRoles.add(recipientType);
                    }

                    if (senderEmail == null) {
                        senderEmail = rs.getString("stat_forwarded_by_user");
                    } else if (senderEmail.isEmpty()) {
                        senderEmail = rs.getString("stat_forwarded_by_user");
                    }

                    if (senderEmail == null) {
                        if (forwardedBy != null && forwardedBy.equalsIgnoreCase("a") || forwardedBy.isEmpty()) {
                            senderEmail = userdata.getEmail();
                            if (senderName == null) {
                                senderName = "Applicant ";
                            } else if (senderName.isEmpty()) {
                                senderName = "Applicant ";
                            }
                        }
                    } else if (senderEmail.isEmpty()) {
                        if (forwardedBy != null && forwardedBy.equalsIgnoreCase("a") || forwardedBy.isEmpty()) {
                            senderEmail = userdata.getEmail();
                            if (senderName == null) {
                                senderName = "Applicant ";
                            } else if (senderName.isEmpty()) {
                                senderName = "Applicant ";
                            }
                        }
                    }
                }
                ++i;
            }
//            Collections.reverse(arrRoles);
//            for (String arrRole : arrRoles) {
//                if (arrRole != null) {
//                    arrFinalRoles.add(arrRole);
//                    if (!arrRole.equals("null")) {
//                        String[] splitted = arrRole.split("=>");
//                        if (!splitted[0].equals("null")) {
//                            arrFinalRoles.add(arrRole);
//                        }
//                    }
//                }
//            }
//            Collections.reverse(arrFinalRoles);
//
//            arrRoles = arrFinalRoles;
            srole = arrRoles.get(arrRoles.size() - 1);

            qry = "select applicant_email, applicant_mobile, applicant_name, applicant_ip, applicant_datetime, applicant_remarks, to_email, to_datetime, status, form_name from final_audit_track where registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                statusFromDb = rs.getString("status").toLowerCase();
                if (rs.getString("status").toLowerCase().contains("pending") || rs.getString("status").toLowerCase().contains("manual")) {
                    status = "pending";
                } else if (rs.getString("status").toLowerCase().contains("rejected") || rs.getString("status").toLowerCase().contains("cancel") || rs.getString("status").toLowerCase().contains("us_expired")) {
                    status = "reject";
                } else if (rs.getString("status").toLowerCase().contains("completed")) {
                    status = "active";
                    if (!formType.equalsIgnoreCase("dor_ext_retired")) {
                        int arrRolesSize = arrRoles.size();
                        if (!arrRoles.get(arrRolesSize - 1).isEmpty()) {
                            arrRoles.add("");
                        }
                    }
                }

                applicantname = rs.getString("applicant_name");
                applicant_mob = rs.getString("applicant_mobile");
                app_email = rs.getString("applicant_email");
                form_name = rs.getString("form_name");
                app_sub_date = rs.getString("applicant_datetime");
                recv_date = rs.getString("to_datetime");
                recv_email = rs.getString("to_email");
                if (!formType.equalsIgnoreCase("dor_ext_retired")) {
                    status_msg = getStatTypeString(rs.getString("status").toLowerCase()) + "(" + recv_email + ")";
                } else {
                    status_msg = getStatTypeString(rs.getString("status").toLowerCase());
                }

                if (rs.getString("status").toLowerCase().contains("pending")) {
                    reqProcessedAs = "Pending";
                    app_date_text = "Pending Since ";
                } else if (rs.getString("status").toLowerCase().contains("rejected")) {
                    reqProcessedAs = "Rejected";
                    app_date_text = "Rejection Date";
                } else if (rs.getString("status").toLowerCase().contains("cancel")) {
                    reqProcessedAs = "Cancelled";
                    app_date_text = "Cancellation Date";
                } else if (rs.getString("status").toLowerCase().contains("completed")) {
                    reqProcessedAs = "Completed";
                    app_date_text = "Completion Date";
                } else {
                    app_date_text = "Submission Date";
                }
            }

            if (status.isEmpty()) {
                status = "Application cancelled by User.";
            }

            if (senderEmail != null && senderName != null) {
                if (senderEmail.isEmpty() && senderName.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> =>Remarks->" + remarks;
                } else if (senderEmail.isEmpty() && !senderName.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderName + "=>Remarks->" + remarks;
                } else if (senderName.isEmpty() && !senderEmail.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderEmail + "=>Remarks->" + remarks;
                } else {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderName + "(" + senderEmail + ")" + "=>Remarks->" + remarks;
                }
            } else if (senderEmail == null && senderName != null) {
                if (senderName.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> " + "=>Remarks->" + remarks;
                } else {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderName + "=>Remarks->" + remarks;
                }
            } else if (senderName == null && senderEmail != null) {
                if (senderEmail.isEmpty()) {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> " + "=>Remarks->" + remarks;
                } else {
                    msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details->" + senderEmail + "=>Remarks->" + remarks;
                }
            } else if (senderEmail == null && senderName == null) {
                msg = "Status->" + status_msg + "=>" + app_date_text + "->" + recv_date + "=>Sender Details-> " + "=>Remarks->" + remarks;
            }

            hmTrack.put("roles", arrRoles);
            hmTrack.put("troles", arrTRoles);
            hmTrack.put("status", status);
            hmTrack.put("name", applicantname);
            hmTrack.put("mob", applicant_mob);
            hmTrack.put("email", app_email);
            hmTrack.put("form_name", form_name);
            hmTrack.put("forword_date", app_sub_date);
            hmTrack.put("msg", msg);
            hmTrack.put("reg_no", reg_no);
//            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside fetchTrackAll hmTrack valyue is " + hmTrack);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackAll catch " + e.toString());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());
                }
            }
        }
        return SUCCESS;
    }

    // Addition of some code on 08-04-2022  By AT  
    public String fetchTrackByRoleBAckup19042022() {
        hmTrack.clear();
        UserData userdata = (UserData) sessionMap.get("uservalues");
        //System.out.println(" inside fetchTrackRole function reg_no value is " + reg_no + " rolo is " + srole);
        String status = "", recv_date = "", recv_email = "", app_date = "", remarks = "", sender_details = "", msg = "", role_app = "", reqProcessedAs = "", app_date_text = "", email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", forwarder = "", stat_process = "", forwardedBy = "";

        try {
            conSlave = DbConnection.getSlaveConnection();

//            System.out.println("srole::: " + srole);
//            System.out.println("trole::: " + trole);
            String qry = "";
            if (trole.equalsIgnoreCase("undefined")) {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_to = ? order by stat_id limit 1";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, srole);
            } else if (srole.equalsIgnoreCase("null") && trole.equalsIgnoreCase("null")) {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_by IS NULL AND stat_forwarded_to IS NULL order by stat_id limit 1";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
            } else if (trole.equalsIgnoreCase("null")) {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_by = ? AND stat_forwarded_to IS NULL order by stat_id limit 1";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, srole);
            } else {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_by = ? AND stat_forwarded_to = ?";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, srole);
                ps.setString(3, trole);
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
            rs = ps.executeQuery();
            if (srole.equals("")) {
                role_app = "yourself";
            } else if (srole.equals("a")) {
                role_app = "Applicant";
            } else if (srole.matches("c")) {
                role_app = "Coordinator";
            } else if (srole.matches("ca")) {
                role_app = "Reporting/Forwarding/Nodal Officer";
            } else if (srole.equals("d")) {
                role_app = "DA-Admin";
            } else if (srole.equals("m")) {
                role_app = "Admin";
            } else if (srole.equals("s")) {
                role_app = "Support";
            } else if (srole.equals("us")) {
                role_app = "Under Secretary";
            }

            if (forward.equals("")) {
                forwarder = "yourself";
            } else if (forward.equals("a")) {
                forwarder = "Applicant";
            } else if (forward.matches("c")) {
                forwarder = "Coordinator";
            } else if (forward.matches("ca")) {
                forwarder = "Reporting/Forwarding/Nodal Officer";
            } else if (forward.equals("d")) {
                forwarder = "DA-Admin";
            } else if (forward.equals("m")) {
                forwarder = "Admin";
            } else if (forward.equals("s")) {
                forwarder = "Support";
            } else if (forward.equals("us")) {
                forwarder = "Under Secretary";
            }

            if (rs.next()) {
                status = "";
                remarks = rs.getString("stat_remarks");
                if (remarks == null) {
                    remarks = "";
                }
                recv_date = rs.getString("stat_createdon");
                current_user = rs.getString("stat_forwarded_by_email");// need 
                forwardedBy = rs.getString("stat_forwarded_by");
                String toEmail = fetchToEmail(reg_no);
                if (current_user == null) {
                    current_user = rs.getString("stat_forwarded_by_user");
                } else if (current_user.isEmpty()) {
                    current_user = rs.getString("stat_forwarded_by_user");
                }

                if (current_user == null) {
                    if (forwardedBy.equalsIgnoreCase("a")) {
                        current_user = userdata.getEmail();
                    }
                } else if (current_user.isEmpty()) {
                    if (forwardedBy.equalsIgnoreCase("a")) {
                        current_user = userdata.getEmail();
                    }
                }

                if (current_user == null) {
                    sender_details = forwarder + "&ensp;";
                } else if (!current_user.isEmpty()) {
                    sender_details = forwarder + "&ensp;(" + current_user + ")";
                } else {
                    sender_details = forwarder + "&ensp;";
                }
                //sender_details = forwarder + "&ensp;(" + current_user + ")";
                recv_email = rs.getString("stat_forwarded_to_user");
                stat_process = rs.getString("stat_process");

                if (forward.equalsIgnoreCase("")) {
                    if (rs.getString("stat_type").toLowerCase().contains("pending") && srole.equalsIgnoreCase("a")) {
                        reqProcessedAs = "Approved";
                        app_date_text = "Submission Date->" + recv_date;
                        if (current_user == null) {
                            status = "Form has been successfully submitted by applicant and forwarded to (" + findRole(trole) + ")";
                        } else if (!current_user.isEmpty()) {
                            status = "Form has been successfully submitted by applicant (" + current_user + ") and forwarded to (" + findRole(trole) + ")";
                        } else {
                            status = "Form has been successfully submitted by applicant and forwarded to (" + findRole(trole) + ")";
                        }
                        recv_date = "";
                    } else if (rs.getString("stat_type").toLowerCase().contains("manual") && srole.equalsIgnoreCase("a")) {
                        //System.out.println("LAST ELSE HAHAHAH");
                        reqProcessedAs = "Pending";
                        app_date_text = "Submission Date->" + recv_date;
                        if (current_user == null) {
                            status = "You have chosen manual option to submit. Hence, request is pending with you only.";
                        } else if (!current_user.isEmpty()) {
                            status = "You have chosen manual option to submit. Hence, request is pending with you only. (" + current_user + ")";
                        } else {
                            status = "You have chosen manual option to submit. Hence, request is pending with you only.";
                        }
                        recv_date = "";
                    } else if (rs.getString("stat_type").toLowerCase().contains("api") && srole.equalsIgnoreCase("a")) {
                        reqProcessedAs = "Pending";
                        app_date_text = "Submission Date->" + recv_date;
                        if (current_user == null) {
                            status = "You have DNS Domain API option to submit. Hence, request is pending with you only.";
                        } else if (!current_user.isEmpty()) {
                            status = "You have chosen DNS Domain API option to submit. Hence, request is pending with you only. (" + current_user + ")";
                        } else {
                            status = "You have chosen DNS Domain API option to submit. Hence, request is pending with you only.";
                        }
                        recv_date = "";
                    } else if (rs.getString("stat_type").toLowerCase().contains("cancel") && srole.equalsIgnoreCase("a")) {
                        reqProcessedAs = "Cancelled";
                        app_date_text = "Cancellation Date->" + recv_date;
                        if (current_user == null) {
                            status = "Request has been cancelled by you.";
                        } else if (!current_user.isEmpty()) {
                            status = "Request has been cancelled by you (" + current_user + ").";
                        } else {
                            status = "Request has been cancelled by you.";
                        }
                        recv_date = "";
                    }
                } else if (trole.equalsIgnoreCase("undefined") || trole.isEmpty()) {
                    if (rs.getString("stat_type").toLowerCase().contains("pending")) {
                        if (rs.getString("stat_on_hold").toLowerCase().equalsIgnoreCase("y")) {
                            reqProcessedAs = "On Hold";
                            app_date_text = "On Hold Since->" + recv_date;
                        } else {
                            if (toEmail != null && !toEmail.isEmpty()) {
                                status = "Pending with " + role_app + "(" + toEmail + ")";
                            } else {
                                status = "Pending with " + role_app;
                            }
                            app_date_text = "Pending Since->" + recv_date;
                        }
                    } else if (rs.getString("stat_type").toLowerCase().contains("rejected")) {
                        if (toEmail != null && !toEmail.isEmpty()) {
                            status = "Rejected by " + role_app + "(" + toEmail + ")";
                        } else {
                            status = "Rejected by " + role_app;
                        }
                        app_date_text = "Rejection Date->" + recv_date;
                    } else if (rs.getString("stat_type").toLowerCase().equalsIgnoreCase("completed")) {
                        if (toEmail != null && !toEmail.isEmpty()) {
                            status = "Completed by " + role_app + "(" + toEmail + ")";
                        } else {
                            status = "Completed by " + role_app;
                        }
                        app_date_text = "Completion Date->" + recv_date;
                    }
                } else if (rs.getString("stat_type").toLowerCase().contains("pending")) {
                    //System.out.println("STAT PROCESS = " + stat_process);
                    if (!(stat_process == null || stat_process.isEmpty())) {
                        String[] aa = stat_process.split("_");
                        String process = aa[0];
                        String actionBy = aa[1];
                        String actionFor = aa[2];

                        if (process.equalsIgnoreCase("pulled")) {
                            status = "Pulled by " + findRole(actionBy) + " from " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("reverted")) {
                            status = "Reverted by " + findRole(actionBy) + " to " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("forwarded")) {
                            status = "Forwarded by " + findRole(actionBy) + " to " + findRole(actionFor);
                        }
                    } else if (forward.equalsIgnoreCase("a") && srole.equalsIgnoreCase("a")) {
                        if (current_user == null) {
                            status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else if (current_user.isEmpty()) {
                            status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else {
                            status = "Submitted by " + role_app + "(" + current_user + ") and forwarded to (" + findRole(trole) + ")";
                        }
                        app_date_text = "Submission Date->" + recv_date;
                    } else {
                        if (current_user == null) {
                            status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else if (current_user.isEmpty()) {
                            status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else {
                            status = "Approved by " + role_app + "(" + current_user + ") and forwarded to (" + findRole(trole) + ")";
                        }
                        app_date_text = "Approving Date->" + recv_date;
                    }
                }

                //System.out.println(" recv_date is " + recv_date + " sender_details is " + sender_details + " status is " + status);
            }

            if (!forward.isEmpty()) {
                qry = " SELECT * FROM status WHERE  stat_reg_no = ?  AND ( stat_forwarded_by = ? OR stat_forwarded_to = ? )";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, forward);
                ps.setString(3, srole);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
                rs = ps.executeQuery();
                if (rs.next()) {
                    recv_date = rs.getString("stat_createdon");
                    current_user = rs.getString("stat_forwarded_by_email");
                    forwardedBy = rs.getString("stat_forwarded_by");
                    if (current_user == null) {
                        current_user = rs.getString("stat_forwarded_by_user");
                    } else if (current_user.isEmpty()) {
                        current_user = rs.getString("stat_forwarded_by_user");
                    }

                    if (current_user == null) {
                        if (forwardedBy.equalsIgnoreCase("a")) {
                            current_user = userdata.getEmail();
                        }
                    } else if (current_user.isEmpty()) {
                        if (forwardedBy.equalsIgnoreCase("a")) {
                            current_user = userdata.getEmail();
                        }
                    }
                    if (current_user == null) {
                        sender_details = forwarder + "&ensp;";
                    } else if (current_user.isEmpty()) {
                        sender_details = forwarder + "&ensp;";
                    } else {
                        sender_details = forwarder + "&ensp;(" + current_user + ")";
                    }
                }
            }
            if (status.equals("")) {
                status = "Application cancelled by User.";
            }

            msg = "Status->" + status + "=>Receiving Date->" + recv_date + "=>" + app_date_text + "=>Remarks->" + remarks + "=>Sender Details->" + sender_details;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchTrackByRole msg value is " + msg);
            hmTrack.put("msg", msg);
            hmTrack.put("reg_no", reg_no);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  get status reg no search results getCAEmail " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return SUCCESS;
    }

    public String fetchTrackByRole() {
        hmTrack.clear();
        UserData userdata = (UserData) sessionMap.get("uservalues");
        String status = "", recv_date = "", recv_email = "", app_date = "", remarks = "", sender_details = "", msg = "", role_app = "", reqProcessedAs = "", app_date_text = "", email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", forwarder = "", stat_process = "", forwardedBy = "", recv_by = "", forwardedByTable = "", forwardedToFromTable = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select * from status where stat_reg_no=? order by stat_id";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
            rs = ps.executeQuery();
            List<StatusTable> statusTableList = new ArrayList<StatusTable>();
            while (rs.next()) {
                //Initialize StatusTable with values from status Table 
                StatusTable statusTable = new StatusTable();
                forwarder = (rs.getString("stat_forwarded_by_user") == null || rs.getString("stat_forwarded_by_user").isEmpty()) ? rs.getString("stat_forwarded_by_email") : rs.getString("stat_forwarded_by_user");
                forwardedBy = (rs.getString("stat_forwarded_by") != null) ? rs.getString("stat_forwarded_by") : "";
                recv_email = (rs.getString("stat_forwarded_to_user") != null) ? rs.getString("stat_forwarded_to_user") : "";
                recv_by = (rs.getString("stat_forwarded_to") != null) ? rs.getString("stat_forwarded_to") : "";
                statusTable.setFormType(rs.getString("stat_form_type"));
                statusTable.setStatus(rs.getString("stat_type").toLowerCase());
                statusTable.setSenderType(forwardedBy);
                statusTable.setSenderEmail(forwarder);
                statusTable.setRecipientType(recv_by);
                statusTable.setRecipient(recv_email);
                statusTable.setRemarks(rs.getString("stat_remarks"));
                statusTable.setSenderDatetime(rs.getString("stat_forwarded_by_datetime"));
                statusTable.setSenderMobile(rs.getString("stat_forwarded_by_mobile"));
                statusTable.setSenderName(rs.getString("stat_forwarded_by_name"));
                statusTable.setCreatedon(rs.getString("stat_createdon"));
                statusTable.setSubmissionType((rs.getString("stat_process") != null) ? rs.getString("stat_process") : "");
                statusTable.setId((long) rs.getInt("stat_id"));
                statusTable.setOnholdStatus(rs.getString("stat_on_hold"));
                statusTable.setRegistrationNo(reg_no);
                statusTableList.add(statusTable);
            }

            if ((statusTableList.size() - 1) < position) {
                position = statusTableList.size() - 1;
            }

            // Then we will fetch the data as per position and will manipulate message accordingly
            StatusTable currentClickedData = statusTableList.get(position);

            srole = currentClickedData.getSenderType() != null ? currentClickedData.getSenderType() : "";
            trole = currentClickedData.getRecipientType() != null ? currentClickedData.getRecipientType() : "";

            if (srole.isEmpty()) {
                role_app = "Applicant";
            } else if (srole.equals("a")) {
                role_app = "Applicant";
            } else if (srole.matches("c")) {
                role_app = "Coordinator";
            } else if (srole.matches("ca")) {
                role_app = "Reporting/Forwarding/Nodal Officer";
            } else if (srole.equals("d")) {
                role_app = "DA-Admin";
            } else if (srole.equals("m")) {
                role_app = "Admin";
            } else if (srole.equals("s")) {
                role_app = "Support";
            } else if (srole.equals("us")) {
                role_app = "Under Secretary";
            }

            if (forward.isEmpty()) {
                forwarder = "Applicant";
            } else if (forward.equals("a")) {
                forwarder = "Applicant";
            } else if (forward.matches("c")) {
                forwarder = "Coordinator";
            } else if (forward.matches("ca")) {
                forwarder = "Reporting/Forwarding/Nodal Officer";
            } else if (forward.equals("d")) {
                forwarder = "DA-Admin";
            } else if (forward.equals("m")) {
                forwarder = "Admin";
            } else if (forward.equals("s")) {
                forwarder = "Support";
            } else if (forward.equals("us")) {
                forwarder = "Under Secretary";
            }

            status = "";
            remarks = currentClickedData.getRemarks();
            if (remarks == null) {
                remarks = "";
            }
            recv_date = currentClickedData.getCreatedon();
            current_user = currentClickedData.getSenderEmail();// need 
            forwardedBy = currentClickedData.getSenderType();
            //String toEmail = fetchToEmail(reg_no);
            String applicantEmail = fetchApplicantEmail(reg_no);
            String currentStatus = currentClickedData.getStatus();
            recv_email = currentClickedData.getRecipient();
            stat_process = currentClickedData.getSubmissionType();
            long id = currentClickedData.getId();
            String forwardedTo = currentClickedData.getRecipientType();
            String recvDateInString = recv_date;
            //sender_details = forwarder + "&ensp;(" + current_user + ")";

            if (current_user == null) {
                if (forwardedBy.equalsIgnoreCase("a") || forwardedBy.isEmpty()) {
                    current_user = applicantEmail;
                }
                if (!current_user.isEmpty()) {
                    sender_details = forwarder + "&ensp;(" + current_user + ")";
                } else {
                    sender_details = forwarder + "&ensp;";
                }
            } else if (current_user.isEmpty()) {
                if (forwardedBy.equalsIgnoreCase("a") || forwardedBy.isEmpty()) {
                    current_user = applicantEmail;
                }
                if (!current_user.isEmpty()) {
                    sender_details = forwarder + "&ensp;(" + current_user + ")";
                } else {
                    sender_details = forwarder + "&ensp;";
                }
            } else {
                sender_details = role_app + "&ensp;(" + current_user + ")";
            }

            if (forwardedBy.equals("h")) {
                forwardedByTable = "HOG";
            } else if (forwardedBy.equals("a") || forwardedBy.isEmpty()) {
                forwardedByTable = "Applicant";
            } else if (forwardedBy.matches("co")) {
                forwardedByTable = "Coordinator";
            } else if (forwardedBy.matches("ca")) {
                forwardedByTable = "Reporting/Forwarding/Nodal Officer";
            } else if (forwardedBy.equals("d")) {
                forwardedByTable = "DA-Admin";
            } else if (forwardedBy.equals("m")) {
                forwardedByTable = "Admin";
            } else if (forwardedBy.equals("s")) {
                forwardedByTable = "Support";
            } else if (forwardedBy.equals("us")) {
                forwardedByTable = "Under Secretary";
            }

            if (forwardedTo.equals("a")) {
                forwardedToFromTable = "Applicant";
            } else if (forwardedTo.matches("co")) {
                forwardedToFromTable = "Coordinator";
            } else if (forwardedTo.matches("ca")) {
                forwardedToFromTable = "Reporting/Forwarding/Nodal Officer";
            } else if (forwardedTo.equals("d")) {
                forwardedToFromTable = "DA-Admin";
            } else if (forwardedTo.equals("m")) {
                forwardedToFromTable = "Admin";
            } else if (forwardedTo.equals("s")) {
                forwardedToFromTable = "Support";
            } else if (forwardedTo.equals("us")) {
                forwardedToFromTable = "Under Secretary";
            } else if (forwardedTo.equals("h")) {
                forwardedToFromTable = "HOG";
            }

            if (forward.equalsIgnoreCase("undefined") || forward.isEmpty()) {
                if (currentStatus.contains("pending") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Approved";
                    app_date_text = "Submission Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "Form has been successfully submitted by applicant " + fetchApplicantEmail(reg_no) + " and forwarded to (" + findRole(trole) + " : " + recv_email + ")";
                        //   status = "Form has been successfully submitted by applicant and forwarded to " + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    } else {
                        status = "Form has been successfully submitted by applicant (" + current_user + ") and forwarded to ("
                                + findRole(trole) + " : " + recv_email + ")";
                        // status = "Form has been successfully submitted by applicant (" + current_user + ") and forwarded to "+ findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    }
                } else if (currentStatus.contains("api") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Pending";
                    app_date_text = "Submission Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "You have got a request from Domain registrar. Request is pending with you only (" + fetchApplicantEmail(reg_no) + ")";
                    } else {
                        status = "You have got a request from Domain registrar. Request is pending with you only. ("
                                + current_user + ")";
                    }
                } else if (currentStatus.contains("manual") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Pending";
                    app_date_text = "Submission Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "You have chosen manual option to submit. Hence, request is pending with you only (" + fetchApplicantEmail(reg_no) + ")";
                    } else {
                        status = "You have chosen manual option to submit. Hence, request is pending with you only. ("
                                + current_user + ")";
                    }
                } else if (currentStatus.contains("cancel") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Cancelled";
                    app_date_text = "Cancellation Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "Request has been cancelled by you (" + fetchApplicantEmail(reg_no) + ")";
                    } else {
                        status = "Request has been cancelled by you (" + current_user + ").";
                    }
                }
                recvDateInString = "";
            } else if (trole.equalsIgnoreCase("undefined") || trole.isEmpty()) {
                if (currentStatus.contains("pending")) {
                    if (currentClickedData.getOnholdStatus().toLowerCase().equalsIgnoreCase("y")) {
                        reqProcessedAs = "On Hold";
                        app_date_text = "On Hold Since->" + recv_date;
                    } else {
                        if (recv_email != null && !recv_email.isEmpty()) {
                            status = "Pending with " + role_app + "(" + recv_email + ")";
                        } else {
                            status = "Pending with " + role_app + "(" + fetchToEmail(reg_no) + ")";
                        }
                        app_date_text = "Pending Since->" + recv_date;
                    }
                } else if (currentStatus.contains("rejected")) {
                    if (recv_email != null && !recv_email.isEmpty()) {
                        status = "Rejected by " + role_app + "(" + recv_email + ")";
                    } else {
                        status = "Rejected by " + role_app;
                    }
                    app_date_text = "Rejection Date->" + recv_date;
                } else if (currentStatus.equalsIgnoreCase("completed")) {
                    if (recv_email != null && !recv_email.isEmpty()) {
                        status = "Completed by " + role_app + "(" + recv_email + ")";
                    } else {
                        // status = "Completed by " + role_app ;   // commented by rahul 27 may 2022
                        status = "Completed by " + role_app + " (" + fetchAdminEmail(reg_no) + ")";
                    }
                    app_date_text = "Completion Date->" + recv_date;
                }
            } else if (currentStatus.contains("pending")) {
                if (!(stat_process == null || stat_process.isEmpty())) {
                    if (!stat_process.equalsIgnoreCase("online")) {
                        String[] aa = stat_process.split("_");
                        String process = aa[0];
                        String actionBy = aa[1];
                        String actionFor = aa[2];

                        if (process.equalsIgnoreCase("pulled")) {
                            status = "Pulled by " + findRole(actionBy) + " from " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("reverted")) {
                            status = "Reverted by " + findRole(actionBy) + " to " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("forwarded")) {
                            status = "Forwarded by " + findRole(actionBy) + " to " + findRole(actionFor);
                        }
                    } else {
                        status = "Forwarded by " + forwardedByTable + " to " + forwardedToFromTable + "(" + recv_email + ")";
                    }
                } else if (forward.equalsIgnoreCase("a") && srole.equalsIgnoreCase("a")) {
                    if (current_user == null) {
                        status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + " : " + fetchApplicantEmail(reg_no) + ")";
                        //  status = "Submitted by " + role_app + " and forwarded to " + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    } else if (current_user.isEmpty()) {
                        status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + " : " + fetchApplicantEmail(reg_no) + ")";
                    } else {
                        status = "Submitted by " + role_app + "(" + current_user + ") and forwarded to ("
                                + findRole(trole) + ")";
                    }
                    app_date_text = "Submission Date->" + recv_date;
                } else {
                    if (current_user == null) {
                        // status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";  // commented by rahul may 
                        status = "Approved by " + role_app + " and forwarded to " + findRole(trole) + " : " + recv_email + ")";
                    } else if (current_user.isEmpty()) {
                        // status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        status = "Approved by " + role_app + " and forwarded to " + findRole(trole) + " : " + recv_email + ")";
                    } else if (srole.equals("c") && trole.equals("h")) {
                        status = "Pull from Coordinator and forwarded to HOG (" + current_user + ")";
                    } else {
//                                status = "Approved by " + role_app + "(" + current_user + ") and forwarded to ("
//                                        + findRole(trole) + ")";

                        status = "Approved by " + role_app + "(" + current_user + ") and forwarded to "
                                + findRole(trole) + " : " + recv_email + ")";
                    }
                    app_date_text = "Approving Date->" + recv_date;
                }
            }
            boolean statusFlag = false;
//            if (!forward.isEmpty()) {
//                if (id > 0) {
//                    qry = " SELECT * FROM status WHERE  stat_id = ?";
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setLong(1, id);
//                    rs = ps.executeQuery();
//                    if (rs.next()) {
//                        statusFlag = true;
//                        recv_date = rs.getString("stat_createdon");
//                        current_user = rs.getString("stat_forwarded_by_email");
//                        forwardedBy = rs.getString("stat_forwarded_by");
//                        if (current_user == null) {
//                            current_user = rs.getString("stat_forwarded_by_user");
//                        } else if (current_user.isEmpty()) {
//                            current_user = rs.getString("stat_forwarded_by_user");
//                        }
//
//                        if (current_user == null) {
//                            if (forwardedBy.equalsIgnoreCase("a")) {
//                                current_user = userdata.getEmail();
//                            }
//                        } else if (current_user.isEmpty()) {
//                            if (forwardedBy.equalsIgnoreCase("a")) {
//                                current_user = userdata.getEmail();
//                            }
//                        }
//                        if (current_user == null) {
//                            sender_details = forwarder + "&ensp;";
//                        } else if (current_user.isEmpty()) {
//                            sender_details = forwarder + "&ensp;";
//                        } else {
//                            sender_details = forwarder + "&ensp;(" + current_user + ")";
//                        }
//                    }
//                }
//            }

//            if (!statusFlag) {
//                qry = "select * from final_audit_track where registration_no=?";
//                ps = conSlave.prepareStatement(qry);
//                ps.setString(1, reg_no);
//                rs = ps.executeQuery();
//                if (rs.next()) {
//                    if (rs.getString("status").contains("cancel")) {
//                        status = "Application cancelled by User.";
//                        msg = "Status->" + status + "=>Cancellation Date->" + rs.getString("to_datetime")
//                                + "=>Cancelled by->Yourself(" + rs.getString("applicant_email") + ")";
//                    } else {
//                        msg = "Status->Please check the inputs provided by you.";
//                    }
//                }
//            }
            //if (msg.isEmpty()) {
            //msg = "Status->" + status + "=>Receiving Date->" + recv_date + "=>" + app_date_text + "=>Remarks->"
            //        + remarks + "=>Sender Details->" + sender_details;
            msg = "Status->" + status + "=>" + app_date_text + "=>Remarks->"
                    + remarks + "=>Sender Details->" + sender_details;
            //}

//            if (msg.isEmpty()) {
//                hmTrack.put("msg", "Status->Please check the inputs provided by you.");
//            } else {
            hmTrack.put("msg", msg);
            //}
            hmTrack.put("reg_no", reg_no);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  get status reg no search results getCAEmail " + e.getMessage());
            hmTrack.put("msg", "");
            hmTrack.put("reg_no", reg_no);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return SUCCESS;
    }

    public String fetchTrackByRoleBackup30May2022() {
        hmTrack.clear();
        UserData userdata = (UserData) sessionMap.get("uservalues");
        String status = "", recv_date = "", recv_email = "", app_date = "", remarks = "", sender_details = "", msg = "", role_app = "", reqProcessedAs = "", app_date_text = "", email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", forwarder = "", stat_process = "", forwardedBy = "", recv_by = "", forwardedByTable = "", forwardedToFromTable = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = "select * from status where stat_reg_no=? order by stat_id";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
            rs = ps.executeQuery();
            List<StatusTable> statusTableList = new ArrayList<StatusTable>();
            while (rs.next()) {
                //Initialize StatusTable with values from status Table 
                StatusTable statusTable = new StatusTable();
                forwarder = (rs.getString("stat_forwarded_by_user") == null || rs.getString("stat_forwarded_by_user").isEmpty()) ? rs.getString("stat_forwarded_by_email") : rs.getString("stat_forwarded_by_user");
                forwardedBy = (rs.getString("stat_forwarded_by") != null) ? rs.getString("stat_forwarded_by") : "";
                recv_email = (rs.getString("stat_forwarded_to_user") != null) ? rs.getString("stat_forwarded_to_user") : "";
                recv_by = (rs.getString("stat_forwarded_to") != null) ? rs.getString("stat_forwarded_to") : "";
                statusTable.setFormType(rs.getString("stat_form_type"));
                statusTable.setStatus(rs.getString("stat_type").toLowerCase());
                statusTable.setSenderType(forwardedBy);
                statusTable.setSenderEmail(forwarder);
                statusTable.setRecipientType(recv_by);
                statusTable.setRecipient(recv_email);
                statusTable.setRemarks(rs.getString("stat_remarks"));
                statusTable.setSenderDatetime(rs.getString("stat_forwarded_by_datetime"));
                statusTable.setSenderMobile(rs.getString("stat_forwarded_by_mobile"));
                statusTable.setSenderName(rs.getString("stat_forwarded_by_name"));
                statusTable.setCreatedon(rs.getString("stat_createdon"));
                statusTable.setSubmissionType((rs.getString("stat_process") != null) ? rs.getString("stat_process") : "");
                statusTable.setId((long) rs.getInt("stat_id"));
                statusTable.setOnholdStatus(rs.getString("stat_on_hold"));
                statusTableList.add(statusTable);
            }

            if ((statusTableList.size() - 1) < position) {
                position = statusTableList.size() - 1;
            }

            // Then we will fetch the data as per position and will manipulate message accordingly
            StatusTable currentClickedData = statusTableList.get(position);

            srole = currentClickedData.getSenderType() != null ? currentClickedData.getSenderType() : "";
            trole = currentClickedData.getRecipientType() != null ? currentClickedData.getRecipientType() : "";

            if (srole.isEmpty()) {
                role_app = "Applicant";
            } else if (srole.equals("a")) {
                role_app = "Applicant";
            } else if (srole.matches("c")) {
                role_app = "Coordinator";
            } else if (srole.matches("ca")) {
                role_app = "Reporting/Forwarding/Nodal Officer";
            } else if (srole.equals("d")) {
                role_app = "DA-Admin";
            } else if (srole.equals("m")) {
                role_app = "Admin";
            } else if (srole.equals("s")) {
                role_app = "Support";
            } else if (srole.equals("us")) {
                role_app = "Under Secretary";
            }

            if (forward.isEmpty()) {
                forwarder = "Applicant";
            } else if (forward.equals("a")) {
                forwarder = "Applicant";
            } else if (forward.matches("c")) {
                forwarder = "Coordinator";
            } else if (forward.matches("ca")) {
                forwarder = "Reporting/Forwarding/Nodal Officer";
            } else if (forward.equals("d")) {
                forwarder = "DA-Admin";
            } else if (forward.equals("m")) {
                forwarder = "Admin";
            } else if (forward.equals("s")) {
                forwarder = "Support";
            } else if (forward.equals("us")) {
                forwarder = "Under Secretary";
            }

            status = "";
            remarks = currentClickedData.getRemarks();
            if (remarks == null) {
                remarks = "";
            }
            recv_date = currentClickedData.getCreatedon();
            current_user = currentClickedData.getSenderEmail();// need 
            forwardedBy = currentClickedData.getSenderType();
            //String toEmail = fetchToEmail(reg_no);
            String applicantEmail = fetchApplicantEmail(reg_no);
            String currentStatus = currentClickedData.getStatus();

            if (current_user == null) {
                if (forwardedBy.equalsIgnoreCase("a") || forwardedBy.isEmpty()) {
                    current_user = applicantEmail;
                }
                if (!current_user.isEmpty()) {
                    sender_details = forwarder + "&ensp;(" + current_user + ")";
                } else {
                    sender_details = forwarder + "&ensp;";
                }
            } else if (current_user.isEmpty()) {
                if (forwardedBy.equalsIgnoreCase("a") || forwardedBy.isEmpty()) {
                    current_user = applicantEmail;
                }
                if (!current_user.isEmpty()) {
                    sender_details = forwarder + "&ensp;(" + current_user + ")";
                } else {
                    sender_details = forwarder + "&ensp;";
                }
            } else {
                sender_details = role_app + "&ensp;(" + current_user + ")";
            }

            //sender_details = forwarder + "&ensp;(" + current_user + ")";
            recv_email = currentClickedData.getRecipient();
            stat_process = currentClickedData.getSubmissionType();
            long id = currentClickedData.getId();
            String forwardedTo = currentClickedData.getRecipientType();
            String recvDateInString = recv_date;

            if (forwardedBy.equals("h")) {
                forwardedByTable = "HOG";
            } else if (forwardedBy.equals("a") || forwardedBy.isEmpty()) {
                forwardedByTable = "Applicant";
            } else if (forwardedBy.matches("co")) {
                forwardedByTable = "Coordinator";
            } else if (forwardedBy.matches("ca")) {
                forwardedByTable = "Reporting/Forwarding/Nodal Officer";
            } else if (forwardedBy.equals("d")) {
                forwardedByTable = "DA-Admin";
            } else if (forwardedBy.equals("m")) {
                forwardedByTable = "Admin";
            } else if (forwardedBy.equals("s")) {
                forwardedByTable = "Support";
            } else if (forwardedBy.equals("us")) {
                forwardedByTable = "Under Secretary";
            }

            if (forwardedTo.equals("a")) {
                forwardedToFromTable = "Applicant";
            } else if (forwardedTo.matches("co")) {
                forwardedToFromTable = "Coordinator";
            } else if (forwardedTo.matches("ca")) {
                forwardedToFromTable = "Reporting/Forwarding/Nodal Officer";
            } else if (forwardedTo.equals("d")) {
                forwardedToFromTable = "DA-Admin";
            } else if (forwardedTo.equals("m")) {
                forwardedToFromTable = "Admin";
            } else if (forwardedTo.equals("s")) {
                forwardedToFromTable = "Support";
            } else if (forwardedTo.equals("us")) {
                forwardedToFromTable = "Under Secretary";
            } else if (forwardedTo.equals("h")) {
                forwardedToFromTable = "HOG";
            }

            if (forward.equalsIgnoreCase("undefined") || forward.isEmpty()) {
                if (currentStatus.contains("pending") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Approved";
                    app_date_text = "Submission Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "Form has been successfully submitted by applicant and forwarded to (" + findRole(trole)
                                + ")";
                        //   status = "Form has been successfully submitted by applicant and forwarded to " + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    } else {
                        status = "Form has been successfully submitted by applicant (" + current_user + ") and forwarded to ("
                                + findRole(trole) + ")";
                        // status = "Form has been successfully submitted by applicant (" + current_user + ") and forwarded to "+ findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    }
                } else if (currentStatus.contains("api") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Pending";
                    app_date_text = "Submission Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "You have got a request from Domain registrar. Request is pending with you only.";
                    } else {
                        status = "You have got a request from Domain registrar. Request is pending with you only. ("
                                + current_user + ")";
                    }
                } else if (currentStatus.contains("manual") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Pending";
                    app_date_text = "Submission Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "You have chosen manual option to submit. Hence, request is pending with you only.";
                    } else {
                        status = "You have chosen manual option to submit. Hence, request is pending with you only. ("
                                + current_user + ")";
                    }
                } else if (currentStatus.contains("cancel") && (srole.equalsIgnoreCase("a") || srole.isEmpty())) {
                    reqProcessedAs = "Cancelled";
                    app_date_text = "Cancellation Date->" + recv_date;
                    if ((current_user == null) || (current_user != null && current_user.isEmpty())) {
                        status = "Request has been cancelled by you.";
                    } else {
                        status = "Request has been cancelled by you (" + current_user + ").";
                    }
                }
                recvDateInString = "";
            } else if (trole.equalsIgnoreCase("undefined") || trole.isEmpty()) {
                if (currentStatus.contains("pending")) {
                    if (currentClickedData.getOnholdStatus().toLowerCase().equalsIgnoreCase("y")) {
                        reqProcessedAs = "On Hold";
                        app_date_text = "On Hold Since->" + recv_date;
                    } else {
                        if (recv_email != null && !recv_email.isEmpty()) {
                            status = "Pending with " + role_app + "(" + recv_email + ")";
                        } else {
                            status = "Pending with " + role_app;
                        }
                        app_date_text = "Pending Since->" + recv_date;
                    }
                } else if (currentStatus.contains("rejected")) {
                    if (recv_email != null && !recv_email.isEmpty()) {
                        status = "Rejected by " + role_app + "(" + recv_email + ")";
                    } else {
                        status = "Rejected by " + role_app;
                    }
                    app_date_text = "Rejection Date->" + recv_date;
                } else if (currentStatus.equalsIgnoreCase("completed")) {
                    if (recv_email != null && !recv_email.isEmpty()) {
                        status = "Completed by " + role_app + "(" + recv_email + ")";
                    } else {
                        // status = "Completed by " + role_app ;   // commented by rahul 27 may 2022
                        status = "Completed by " + role_app + " (" + fetchAdminEmail(reg_no) + ")";
                    }
                    app_date_text = "Completion Date->" + recv_date;
                }
            } else if (currentStatus.contains("pending")) {
                if (!(stat_process == null || stat_process.isEmpty())) {
                    if (!stat_process.equalsIgnoreCase("online")) {
                        String[] aa = stat_process.split("_");
                        String process = aa[0];
                        String actionBy = aa[1];
                        String actionFor = aa[2];

                        if (process.equalsIgnoreCase("pulled")) {
                            status = "Pulled by " + findRole(actionBy) + " from " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("reverted")) {
                            status = "Reverted by " + findRole(actionBy) + " to " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("forwarded")) {
                            status = "Forwarded by " + findRole(actionBy) + " to " + findRole(actionFor);
                        }
                    } else {
                        status = "Forwarded by " + forwardedByTable + " to " + forwardedToFromTable + "(" + recv_email + ")";
                    }
                } else if (forward.equalsIgnoreCase("a") && srole.equalsIgnoreCase("a")) {
                    if (current_user == null) {
                        status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        //  status = "Submitted by " + role_app + " and forwarded to " + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    } else if (current_user.isEmpty()) {
                        status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                    } else {
                        status = "Submitted by " + role_app + "(" + current_user + ") and forwarded to ("
                                + findRole(trole) + ")";
                    }
                    app_date_text = "Submission Date->" + recv_date;
                } else {
                    if (current_user == null) {
                        // status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";  // commented by rahul may 
                        status = "Approved by " + role_app + " and forwarded to " + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    } else if (current_user.isEmpty()) {
                        // status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        status = "Approved by " + role_app + " and forwarded to " + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    } else if (srole.equals("c") && trole.equals("h")) {
                        status = "Pull from Coordinator and forwarded to HOG (" + current_user + ")";
                    } else {
//                                status = "Approved by " + role_app + "(" + current_user + ") and forwarded to ("
//                                        + findRole(trole) + ")";

                        status = "Approved by " + role_app + "(" + current_user + ") and forwarded to "
                                + findRole(trole) + " (" + fetchToEmail(reg_no) + ")";
                    }
                    app_date_text = "Approving Date->" + recv_date;
                }
            }
            boolean statusFlag = false;
//            if (!forward.isEmpty()) {
//                if (id > 0) {
//                    qry = " SELECT * FROM status WHERE  stat_id = ?";
//                    ps = conSlave.prepareStatement(qry);
//                    ps.setLong(1, id);
//                    rs = ps.executeQuery();
//                    if (rs.next()) {
//                        statusFlag = true;
//                        recv_date = rs.getString("stat_createdon");
//                        current_user = rs.getString("stat_forwarded_by_email");
//                        forwardedBy = rs.getString("stat_forwarded_by");
//                        if (current_user == null) {
//                            current_user = rs.getString("stat_forwarded_by_user");
//                        } else if (current_user.isEmpty()) {
//                            current_user = rs.getString("stat_forwarded_by_user");
//                        }
//
//                        if (current_user == null) {
//                            if (forwardedBy.equalsIgnoreCase("a")) {
//                                current_user = userdata.getEmail();
//                            }
//                        } else if (current_user.isEmpty()) {
//                            if (forwardedBy.equalsIgnoreCase("a")) {
//                                current_user = userdata.getEmail();
//                            }
//                        }
//                        if (current_user == null) {
//                            sender_details = forwarder + "&ensp;";
//                        } else if (current_user.isEmpty()) {
//                            sender_details = forwarder + "&ensp;";
//                        } else {
//                            sender_details = forwarder + "&ensp;(" + current_user + ")";
//                        }
//                    }
//                }
//            }

//            if (!statusFlag) {
//                qry = "select * from final_audit_track where registration_no=?";
//                ps = conSlave.prepareStatement(qry);
//                ps.setString(1, reg_no);
//                rs = ps.executeQuery();
//                if (rs.next()) {
//                    if (rs.getString("status").contains("cancel")) {
//                        status = "Application cancelled by User.";
//                        msg = "Status->" + status + "=>Cancellation Date->" + rs.getString("to_datetime")
//                                + "=>Cancelled by->Yourself(" + rs.getString("applicant_email") + ")";
//                    } else {
//                        msg = "Status->Please check the inputs provided by you.";
//                    }
//                }
//            }
            //if (msg.isEmpty()) {
            //msg = "Status->" + status + "=>Receiving Date->" + recv_date + "=>" + app_date_text + "=>Remarks->"
            //        + remarks + "=>Sender Details->" + sender_details;
            msg = "Status->" + status + "=>" + app_date_text + "=>Remarks->"
                    + remarks + "=>Sender Details->" + sender_details;
            //}

//            if (msg.isEmpty()) {
//                hmTrack.put("msg", "Status->Please check the inputs provided by you.");
//            } else {
            hmTrack.put("msg", msg);
            //}
            hmTrack.put("reg_no", reg_no);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  get status reg no search results getCAEmail " + e.getMessage());
            hmTrack.put("msg", "");
            hmTrack.put("reg_no", reg_no);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return SUCCESS;
    }

    public String fetchTrackByRoleBackup() {
        hmTrack.clear();
        UserData userdata = (UserData) sessionMap.get("uservalues");
        //System.out.println(" inside fetchTrackRole function reg_no value is " + reg_no + " rolo is " + srole);
        String status = "", recv_date = "", recv_email = "", app_date = "", remarks = "", sender_details = "", msg = "", role_app = "", reqProcessedAs = "", app_date_text = "", email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", forwarder = "", stat_process = "", forwardedBy = "";

        try {
            conSlave = DbConnection.getSlaveConnection();

//            System.out.println("srole::: " + srole);
//            System.out.println("trole::: " + trole);
            String qry = "";
            if (trole.equalsIgnoreCase("undefined")) {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_to = ? order by stat_id limit 1";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, srole);
            } else if (srole.equalsIgnoreCase("null") && trole.equalsIgnoreCase("null")) {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_by IS NULL AND stat_forwarded_to IS NULL order by stat_id limit 1";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
            } else if (trole.equalsIgnoreCase("null")) {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_by = ? AND stat_forwarded_to IS NULL order by stat_id limit 1";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, srole);
            } else {
                qry = "SELECT * FROM status WHERE  stat_reg_no = ?  AND stat_forwarded_by = ? AND stat_forwarded_to = ?";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, srole);
                ps.setString(3, trole);
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
            rs = ps.executeQuery();
            if (srole.equals("")) {
                role_app = "yourself";
            } else if (srole.equals("a")) {
                role_app = "Applicant";
            } else if (srole.matches("c")) {
                role_app = "Coordinator";
            } else if (srole.matches("ca")) {
                role_app = "Reporting/Forwarding/Nodal Officer";
            } else if (srole.equals("d")) {
                role_app = "DA-Admin";
            } else if (srole.equals("m")) {
                role_app = "Admin";
            } else if (srole.equals("s")) {
                role_app = "Support";
            } else if (srole.equals("us")) {
                role_app = "Under Secretary";
            }

            if (forward.equals("")) {
                forwarder = "yourself";
            } else if (forward.equals("a")) {
                forwarder = "Applicant";
            } else if (forward.matches("c")) {
                forwarder = "Coordinator";
            } else if (forward.matches("ca")) {
                forwarder = "Reporting/Forwarding/Nodal Officer";
            } else if (forward.equals("d")) {
                forwarder = "DA-Admin";
            } else if (forward.equals("m")) {
                forwarder = "Admin";
            } else if (forward.equals("s")) {
                forwarder = "Support";
            } else if (forward.equals("us")) {
                forwarder = "Under Secretary";
            }

            if (rs.next()) {
                status = "";
                remarks = rs.getString("stat_remarks");
                if (remarks == null) {
                    remarks = "";
                }
                recv_date = rs.getString("stat_createdon");
                current_user = rs.getString("stat_forwarded_by_email");
                forwardedBy = rs.getString("stat_forwarded_by");
                String toEmail = fetchToEmail(reg_no);
                if (current_user == null) {
                    current_user = rs.getString("stat_forwarded_by_user");
                } else if (current_user.isEmpty()) {
                    current_user = rs.getString("stat_forwarded_by_user");
                }

                if (current_user == null) {
                    if (forwardedBy.equalsIgnoreCase("a")) {
                        current_user = userdata.getEmail();
                    }
                } else if (current_user.isEmpty()) {
                    if (forwardedBy.equalsIgnoreCase("a")) {
                        current_user = userdata.getEmail();
                    }
                }

                if (current_user == null) {
                    sender_details = forwarder + "&ensp;";
                } else if (!current_user.isEmpty()) {
                    sender_details = forwarder + "&ensp;(" + current_user + ")";
                } else {
                    sender_details = forwarder + "&ensp;";
                }
                //sender_details = forwarder + "&ensp;(" + current_user + ")";
                recv_email = rs.getString("stat_forwarded_to_user");
                stat_process = rs.getString("stat_process");

                if (forward.equalsIgnoreCase("")) {
                    if (rs.getString("stat_type").toLowerCase().contains("pending") && srole.equalsIgnoreCase("a")) {
                        reqProcessedAs = "Approved";
                        app_date_text = "Submission Date->" + recv_date;
                        if (current_user == null) {
                            status = "Form has been successfully submitted by applicant and forwarded to (" + findRole(trole) + ")";
                        } else if (!current_user.isEmpty()) {
                            status = "Form has been successfully submitted by applicant (" + current_user + ") and forwarded to (" + findRole(trole) + ")";
                        } else {
                            status = "Form has been successfully submitted by applicant and forwarded to (" + findRole(trole) + ")";
                        }
                        recv_date = "";
                    } else if (rs.getString("stat_type").toLowerCase().contains("manual") && srole.equalsIgnoreCase("a")) {
                        //System.out.println("LAST ELSE HAHAHAH");
                        reqProcessedAs = "Pending";
                        app_date_text = "Submission Date->" + recv_date;
                        if (current_user == null) {
                            status = "You have chosen manual option to submit. Hence, request is pending with you only.";
                        } else if (!current_user.isEmpty()) {
                            status = "You have chosen manual option to submit. Hence, request is pending with you only. (" + current_user + ")";
                        } else {
                            status = "You have chosen manual option to submit. Hence, request is pending with you only.";
                        }
                        recv_date = "";
                    } else if (rs.getString("stat_type").toLowerCase().contains("api") && srole.equalsIgnoreCase("a")) {
                        reqProcessedAs = "Pending";
                        app_date_text = "Submission Date->" + recv_date;
                        if (current_user == null) {
                            status = "You have DNS Domain API option to submit. Hence, request is pending with you only.";
                        } else if (!current_user.isEmpty()) {
                            status = "You have chosen DNS Domain API option to submit. Hence, request is pending with you only. (" + current_user + ")";
                        } else {
                            status = "You have chosen DNS Domain API option to submit. Hence, request is pending with you only.";
                        }
                        recv_date = "";
                    } else if (rs.getString("stat_type").toLowerCase().contains("cancel") && srole.equalsIgnoreCase("a")) {
                        reqProcessedAs = "Cancelled";
                        app_date_text = "Cancellation Date->" + recv_date;
                        if (current_user == null) {
                            status = "Request has been cancelled by you.";
                        } else if (!current_user.isEmpty()) {
                            status = "Request has been cancelled by you (" + current_user + ").";
                        } else {
                            status = "Request has been cancelled by you.";
                        }
                        recv_date = "";
                    }
                } else if (trole.equalsIgnoreCase("undefined") || trole.isEmpty()) {
                    if (rs.getString("stat_type").toLowerCase().contains("pending")) {
                        if (rs.getString("stat_on_hold").toLowerCase().equalsIgnoreCase("y")) {
                            reqProcessedAs = "On Hold";
                            app_date_text = "On Hold Since->" + recv_date;
                        } else {
                            if (toEmail != null && !toEmail.isEmpty()) {
                                status = "Pending with " + role_app + "(" + toEmail + ")";
                            } else {
                                status = "Pending with " + role_app;
                            }
                            app_date_text = "Pending Since->" + recv_date;
                        }
                    } else if (rs.getString("stat_type").toLowerCase().contains("rejected")) {
                        if (toEmail != null && !toEmail.isEmpty()) {
                            status = "Rejected by " + role_app + "(" + toEmail + ")";
                        } else {
                            status = "Rejected by " + role_app;
                        }
                        app_date_text = "Rejection Date->" + recv_date;
                    } else if (rs.getString("stat_type").toLowerCase().equalsIgnoreCase("completed")) {
                        if (toEmail != null && !toEmail.isEmpty()) {
                            status = "Completed by " + role_app + "(" + toEmail + ")";
                        } else {
                            status = "Completed by " + role_app;
                        }
                        app_date_text = "Completion Date->" + recv_date;
                    }
                } else if (rs.getString("stat_type").toLowerCase().contains("pending")) {
                    //System.out.println("STAT PROCESS = " + stat_process);
                    if (!(stat_process == null || stat_process.isEmpty())) {
                        String[] aa = stat_process.split("_");
                        String process = aa[0];
                        String actionBy = aa[1];
                        String actionFor = aa[2];

                        if (process.equalsIgnoreCase("pulled")) {
                            status = "Pulled by " + findRole(actionBy) + " from " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("reverted")) {
                            status = "Reverted by " + findRole(actionBy) + " to " + findRole(actionFor);
                        } else if (process.equalsIgnoreCase("forwarded")) {
                            status = "Forwarded by " + findRole(actionBy) + " to " + findRole(actionFor);
                        }
                    } else if (forward.equalsIgnoreCase("a") && srole.equalsIgnoreCase("a")) {
                        if (current_user == null) {
                            status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else if (current_user.isEmpty()) {
                            status = "Submitted by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else {
                            status = "Submitted by " + role_app + "(" + current_user + ") and forwarded to (" + findRole(trole) + ")";
                        }
                        app_date_text = "Submission Date->" + recv_date;
                    } else {
                        if (current_user == null) {
                            status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else if (current_user.isEmpty()) {
                            status = "Approved by " + role_app + " and forwarded to (" + findRole(trole) + ")";
                        } else {
                            status = "Approved by " + role_app + "(" + current_user + ") and forwarded to (" + findRole(trole) + ")";
                        }
                        app_date_text = "Approving Date->" + recv_date;
                    }
                }

                //System.out.println(" recv_date is " + recv_date + " sender_details is " + sender_details + " status is " + status);
            }

            if (!forward.isEmpty()) {
                qry = " SELECT * FROM status WHERE  stat_reg_no = ?  AND ( stat_forwarded_by = ? OR stat_forwarded_to = ? )";
                ps = conSlave.prepareStatement(qry);
                ps.setString(1, reg_no);
                ps.setString(2, forward);
                ps.setString(3, srole);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
                rs = ps.executeQuery();
                if (rs.next()) {
                    recv_date = rs.getString("stat_createdon");
                    current_user = rs.getString("stat_forwarded_by_email");
                    forwardedBy = rs.getString("stat_forwarded_by");
                    if (current_user == null) {
                        current_user = rs.getString("stat_forwarded_by_user");
                    } else if (current_user.isEmpty()) {
                        current_user = rs.getString("stat_forwarded_by_user");
                    }

                    if (current_user == null) {
                        if (forwardedBy.equalsIgnoreCase("a")) {
                            current_user = userdata.getEmail();
                        }
                    } else if (current_user.isEmpty()) {
                        if (forwardedBy.equalsIgnoreCase("a")) {
                            current_user = userdata.getEmail();
                        }
                    }
                    if (current_user == null) {
                        sender_details = forwarder + "&ensp;";
                    } else if (current_user.isEmpty()) {
                        sender_details = forwarder + "&ensp;";
                    } else {
                        sender_details = forwarder + "&ensp;(" + current_user + ")";
                    }
                }
            }
            if (status.equals("")) {
                status = "Application cancelled by User.";
            }

            msg = "Status->" + status + "=>Receiving Date->" + recv_date + "=>" + app_date_text + "=>Remarks->" + remarks + "=>Sender Details->" + sender_details;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchTrackByRole msg value is " + msg);
            hmTrack.put("msg", msg);
            hmTrack.put("reg_no", reg_no);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  get status reg no search results getCAEmail " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return SUCCESS;
    }

    private String fetchToEmail(String regNo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", qry = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            qry = "select to_email from final_audit_track where registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                current_user = rs.getString("to_email").toLowerCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return current_user;
    }

    private String fetchAdminEmail(String regNo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", qry = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            qry = "select admin_email from final_audit_track where registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                current_user = rs.getString("admin_email").toLowerCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return current_user;
    }

    private String fetchApplicantEmail(String regNo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String current_user = "", qry = "";

        try {
            conSlave = DbConnection.getSlaveConnection();
            qry = "select applicant_email from final_audit_track where registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            rs = ps.executeQuery();
            if (rs.next()) {
                current_user = rs.getString("applicant_email").toLowerCase();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return current_user;
    }

    public String findRole(String forward) {
        if (forward.equals("")) {
            return "yourself";
        } else if (forward.equals("a")) {
            return "Applicant";
        } else if (forward.matches("c")) {
            return "Coordinator";
        } else if (forward.matches("ca")) {
            return "Reporting/Forwarding/Nodal Officer";
        } else if (forward.equals("d")) {
            return "DA-Admin";
        } else if (forward.equals("m")) {
            return "Admin";
        } else if (forward.equals("s")) {
            return "Support";
        } else if (forward.equals("us")) {
            return "Under Secretary";
        } else {
            return "";
        }
    }

    public String getStatTypeString(String stat_type) {
        String type = "";
        if (stat_type.equals("ca_pending")) {
            type = Constants.CA_PENDING;
        } else if (stat_type.equals("ca_rejected")) {
            type = Constants.CA_REJECTED;
        } else if (stat_type.equals("support_pending")) {
            type = Constants.SUPPORT_PENDING;
        } else if (stat_type.equals("support_rejected")) {
            type = Constants.SUPPORT_REJECTED;
        } else if (stat_type.equals("coordinator_pending")) {
            type = Constants.COORDINATOR_PENDING;
        } else if (stat_type.equals("coordinator_rejected")) {
            type = Constants.COORDINATOR_REJECTED;
        } else if (stat_type.equals("completed")) {
            type = Constants.COMPLETED;
        } else if (stat_type.equals("cancel")) {
            type = Constants.CANCEL;
        } else if (stat_type.equals("mail-admin_pending")) {
            type = Constants.MAIL_ADMIN_PENDING;
        } else if (stat_type.equals("mail-admin_rejected")) {
            type = Constants.MAIL_ADMIN_REJECTED;
        } else if (stat_type.equals("da_pending")) // else if added by pr on 20thfeb18
        {
            type = Constants.DA_PENDING;
        } else if (stat_type.equals("da_rejected")) // else if added by pr on 6thapr18
        {
            type = Constants.DA_REJECTED;
        } else if (stat_type.equals("api")) // else if added by pr on 6thapr18
        {
            type = Constants.PENDING_API;
        } else if (stat_type.equals("domainapi")) // else if added by pr on 6thapr18
        {
            type = Constants.PENDING_API;
        } else if (stat_type.equals("manual_upload")) // else if added by pr on 10thmay18
        {
            type = Constants.manual_upload;
        } else if (stat_type.equals("us_pending")) // start,code added  by pr on 8thjan19
        {
            type = "Pending with Under Secretary and above";
        } else if (stat_type.equals("us_rejected")) {
            type = "Rejected by Under Secretary";
        } else if (stat_type.equals("us_expired")) {
            type = "Under Secretary Link Timeout";
        }// end, code added  by pr on 8thjan19
//        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + 
//                new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " getStatTypeString is " + type);

        return type;

    }

    public String fetchCurrentStatus() {
        hmTrack.clear();
        //System.out.println(" inside fetchCurrentStatus function reg_no value is " + reg_no + " rolo is " + srole);
        String status = "", recv_date = "", recv_email = "", app_date = "", remarks = "", sender_details = "", msg = "", role_app = "", reqProcessedAs = "", app_date_text = "", email = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean reqProcessed = false;

        try {
            conSlave = DbConnection.getSlaveConnection();
            String qry = " SELECT * FROM final_audit_track WHERE  registration_no = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, reg_no);
            ps.setString(2, srole);
            ps.setString(3, trole);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  fetchTrackRole query is " + ps);
            rs = ps.executeQuery();
            if (srole.equals("a")) {
                role_app = "Applicant";
            } else if (srole.matches("c")) {
                role_app = "Coordinator";
            } else if (srole.matches("ca")) {
                role_app = "Reporting/Forwarding/Nodal Officer";
            } else if (srole.equals("d")) {
                role_app = "DA-Admin";
            } else if (srole.equals("m")) {
                role_app = "Mail Admin";
            } else if (srole.equals("s")) {
                role_app = "Support";
            } else if (srole.equals("us")) {
                role_app = "Under Secretary";
            }

            if (rs.next()) {
                status = "";
                remarks = rs.getString("stat_remarks");
                recv_date = rs.getString("stat_createdon");
                sender_details = role_app + "&ensp;(" + rs.getString("stat_forwarded_by_user") + ")";
                recv_email = rs.getString("stat_forwarded_to_user");

                if (rs.getString("stat_type").toLowerCase().contains("pending")) {
                    reqProcessedAs = "Approved";
                    app_date_text = "Approving Date";
                } else if (rs.getString("stat_type").toLowerCase().contains("rejected")) {
                    reqProcessedAs = "Rejected";
                    app_date_text = "Rejection Date";
                }

                if (srole.equals("a")) {
                    status = "Form has been successfully submitted by User";
                }

                if (status.equals("")) // if the request has not been forwarded by this role as of now then by will not be there thefore the above if wuld not have executed
                {
                    status = getStatTypeString(rs.getString("stat_type"));
                }

                if (!srole.equals("a")) {
                    status = reqProcessedAs + " by " + role_app;
                }
                //System.out.println(" recv_date is " + recv_date + " sender_details is " + sender_details + " status is " + status);
            }

            if (recv_email != null && !recv_email.equals("")) {
                status = status + " (" + recv_email + ")";
            }
            if (status.equals("")) {
                status = "Application cancelled by User.";
            }

            msg = "Status->" + status + "=>Receiving Date->" + recv_date + "=>" + app_date_text + "->" + app_date + "=>Remarks->" + remarks + "=>Sender Details->" + sender_details;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " fetchTrackByRole msg value is " + msg);
            hmTrack.put("msg", msg);
            hmTrack.put("reg_no", reg_no);

        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  get status reg no search results getCAEmail " + e.getMessage());
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 1 " + e.getMessage());
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // above line commented below added by pr on 23rdapr19
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip
                            + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside getCAEmail EXCEPTION 2 " + e.getMessage());

                }
            }
        }
        return SUCCESS;
    }

    /* Tracker Code End From Here */
    // start, code added by pr on 4thsep19
    //public Map settingSessionCount(String role, ArrayList<String> email) {
    public String settingSessionCount() {
        role = fetch_role();
        UserData userdata = (UserData) sessionMap.get("uservalues");
        Set s = (Set) userdata.getAliases();
        //System.out.println(" inside settingSessionCount set value is  " + s);
        ArrayList<String> newAr = new ArrayList<>();
        newAr.addAll(s);
        email = newAr;
        Map DAOGotMap = new HashMap<>();
        HashMap finalMap = new HashMap<>();
        //System.out.println(" inside settingSessionCount role is " + role + " email is " + email);
        supForms = userdata.getFormsAllowed();
        // set the counts in the bean
        int pendingCount = fetchCount("pending", role, email);
        int totalCount = fetchCount("total", role, email);
        int newCount = fetchCount("new", role, email);
        int completeCount = fetchCount("completed", role, email);
        //System.out.println("Inside settingSessionCount func  role is " + role + " pendingCount " + pendingCount + " totalCount is " + totalCount + " newCount is " + newCount + " completeCount " + completeCount);
        DAOGotMap.put(role + "totalcount", totalCount);
        DAOGotMap.put(role + "newcount", newCount);
        DAOGotMap.put(role + "pendingcount", pendingCount);
        DAOGotMap.put(role + "completecount", completeCount);
        sessionMap.put(role + "totalcount", totalCount);
        sessionMap.put(role + "newcount", newCount);
        sessionMap.put(role + "pendingcount", pendingCount);
        sessionMap.put(role + "completecount", completeCount);
//        System.out.println(sessionid + " == " + "SESSION MAP : Total Count " + DAOGotMap.get(role + "totalcount"));
//        System.out.println(sessionid + " == " + "SESSION MAP : pendingcount " + DAOGotMap.get(role + "pendingcount"));
//        System.out.println(sessionid + " == " + "SESSION MAP : newcount " + DAOGotMap.get(role + "newcount"));
//        System.out.println(sessionid + " == " + "SESSION MAP : completecount " + DAOGotMap.get(role + "completecount"));
//
//        System.out.println(" inside setting session count session values are below : ");
        for (Object k : DAOGotMap.keySet()) {
            //System.out.println(" key is " + k + " value is " + DAOGotMap.get(k));
        }

        //return DAOGotMap;        
        finalMap.put("counts", DAOGotMap);

        finalMap.put("forms", filterForms);

//        System.out.println(" inside loginaction counts key value is " + DAOGotMap + " forms key value is " + filterForms);
//
//        System.out.println(" inside settingSessionCount function finalMap values are " + finalMap);
//        
//        String filterFormsStr = "";
//        
//        if( filterForms != null && filterForms.size() > 0 )
//        {
//            filterFormsStr = String.join(":", filterForms);
//        }
        try {
            response.setContentType("text/event-stream, charset=UTF-8");

            String realCount = totalCount + ":" + newCount + ":" + pendingCount + ":" + completeCount;

//            if( !filterFormsStr.equals("") )
//            {
//                realCount += ":"+filterFormsStr;
//            }
            PrintWriter pw = response.getWriter();
            pw.write("data: " + realCount + "\n\n");
            //System.out.println("THIS IS LIT " + realCount);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SUCCESS;

    }

    // below function added by pr on 21stfeb18
    public ArrayList checkSupMadminForm(String role, ArrayList<String> email)// it returns true if the form should be visible to support or mailadmin else false 
    {
        //System.out.println(" inside  checkSupMadminForm function role is " + role + " email is " + email);

        ArrayList<String> supForms = new ArrayList<String>(); // supForms will contain all the form name like 'single','bulk','nkn' and so on ...        

        //initializeSessionValues();
        if (role.equals(Constants.ROLE_SUP) || role.equals(Constants.ROLE_MAILADMIN)) {
            PreparedStatement ps = null;
            ResultSet res = null;
            //Connection con = null;
            try {
                //con = DbConnection.getConnection();
                conSlave = DbConnection.getSlaveConnection();
                String qry = " SELECT m_single,m_bulk,m_nkn,m_relay,m_ldap,m_dlist,m_ip,m_sms,m_wifi,m_imappop,m_gem,m_mobile,m_dns,m_vpn,m_webcast,m_wifiport,m_daonboarding FROM mailadmin_forms WHERE ";
                //System.out.println(" email in checj " + email);
                if (email != null && email.size() > 0) {
                    int i = 1;
                    for (Object o : email) {
                        if (i == 1) {
                            qry += " m_email = '" + o.toString() + "'";
                        } else {
                            qry += " OR  m_email = '" + o.toString() + "'";
                        }
                        i++;
                    }
                }
                qry += " ORDER BY m_id DESC LIMIT 1  ";
                ps = conSlave.prepareStatement(qry);

                System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside get form names " + ps);

                res = ps.executeQuery();
                while (res.next()) {
                    if (res.getString("m_single").equals("y")) {
                        supForms.add("'" + Constants.SINGLE_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_bulk").equals("y")) {
                        supForms.add("'" + Constants.BULK_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_nkn").equals("y")) {
                        supForms.add("'" + Constants.NKN_SINGLE_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.NKN_BULK_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_relay").equals("y")) {
                        supForms.add("'" + Constants.RELAY_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_ldap").equals("y")) {
                        supForms.add("'" + Constants.LDAP_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dlist").equals("y")) {
                        supForms.add("'" + Constants.DIST_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dlist").equals("y")) {
                        supForms.add("'" + Constants.BULKDIST_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_sms").equals("y")) {
                        supForms.add("'" + Constants.SMS_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_ip").equals("y")) {
                        supForms.add("'" + Constants.IP_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_imappop").equals("y")) {
                        supForms.add("'" + Constants.IMAP_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_gem").equals("y")) {
                        supForms.add("'" + Constants.GEM_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_mobile").equals("y")) {
                        supForms.add("'" + Constants.MOB_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dns").equals("y")) {
                        supForms.add("'" + Constants.DNS_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_wifi").equals("y")) {
                        supForms.add("'" + Constants.WIFI_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_wifiport").equals("y")) {
                        supForms.add("'" + Constants.WIFI_PORT_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_vpn").equals("y")) {
                        supForms.add("'" + Constants.VPN_SINGLE_FORM_KEYWORD + "'");
                        // supForms.add("'" + Constants.VPN_BULK_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_ADD_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_RENEW_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_DELETE_FORM_KEYWORD + "'");
                        supForms.add("'" + Constants.VPN_SURRENDER_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_dor_ext").equals("y")) {
                        supForms.add("'" + Constants.DOR_EXT_FORM_KEYWORD + "'");
                    }
                    if (res.getString("m_daonboarding").equals("y")) {
                        supForms.add("'" + Constants.DAONBOARDING_FORM_KEYWORD + "'");
                    }
                }
                // below lines added by pr on 6thmar18
            } catch (Exception ex) {
                System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function " + ex.getMessage());

                ex.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function EXCEPTION " + e.getMessage());

                    }
                }
                if (res != null) {
                    try {
                        res.close();
                    } catch (Exception e) {
                        //e.printStackTrace();

                        // above line commented below added by pr on 23rdapr19
                        System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function EXCEPTION 1 " + e.getMessage());

                    }
                }
//                if (con != null) {
//                    try {
//                        // con.close();
//                    } catch (Exception e) {
//                        //e.printStackTrace();
//
//                        // above line commented below added by pr on 23rdapr19
//                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " inside checkSupMadminForm function EXCEPTION 2 " + e.getMessage());
//                    }
//                }
            }
        } else {
            supForms.add("'" + Constants.SINGLE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.BULK_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.NKN_SINGLE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.NKN_BULK_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.RELAY_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.LDAP_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.DIST_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.BULKDIST_FORM_KEYWORD + "'");   //Added by Rahul jan 2021
            supForms.add("'" + Constants.SMS_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.IP_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.IMAP_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.GEM_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.MOB_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.DNS_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.WIFI_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.WIFI_PORT_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_SINGLE_FORM_KEYWORD + "'");
            // supForms.add("'" + Constants.VPN_BULK_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_ADD_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_RENEW_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_DELETE_FORM_KEYWORD + "'");
            supForms.add("'" + Constants.VPN_SURRENDER_FORM_KEYWORD + "'");
        }

        filterForms = supForms;

        return supForms;
    }

    // New fetch count ... for maintaining session counts only
    public int fetchCount(String type, String role, ArrayList<String> email) {
//        System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  start of getcount function ");
//
//        System.out.println(" inside fetccount func admin_role value is " + role);

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

        //ArrayList<String> supForms = null; // supForms will contain all the form name like 'single','bulk','nkn' and so on ...
        //supForms = checkSupMadminForm(role, email);
        //System.out.println(" inside fetccount supForms value is " + supForms);
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

            //qry += " AND on_hold = 'n' "; // so as to get only those applications which are NOT on hold    , in case of pending requests           
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
                    if (coordsIds.size() != 0) {
                        for (String coordsId : coordsIds) {
                            sb += "'" + coordsId + "',";
                        }

                        sb = sb.replaceAll("\\s*,\\s*$", "");
                        //System.out.println("my sb:" + sb);

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
                                //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                                qry += " and  form_name = ? ";
                            }
                        }
                    } else if (formName.contains("nkn")) {
                        qry += " and  form_name like ? ";
                    } else {
                        //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
                        qry += " and  form_name = ? ";
                    }
                } else if (formName.contains("nkn")) {
                    qry += " and  form_name like ? ";
                } else {
                    //System.out.println("INSIDE QUERYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYZZZZZZZZZZZZZZZZZ");
                    qry += " and  form_name = ? ";
                }
            } else if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                if (!csvForms.isEmpty()) {
                    qry += " and  form_name in (" + csvForms + ") ";
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
            // start, code added by pr on 7thjan2020
            System.out.println(" role value is " + role + "BEFOREEEEEEEEE inside the on hold where clause in fetchDataPage query 2 ");

            if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                System.out.println(" inside the on hold where clause in fetchDataPage query ");

                qry += " AND on_hold = 'n' ";
            }

            // end, code added by pr on 7thjan2020  
            ps = conSlave.prepareStatement(qry);
            int pscnt = 0;

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
//            System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
//                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " COUNT QUERY for role " + role + " and type " + type + "  is " + ps);
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
//        System.out.println("TOTAL TIME  TAKEN : " + (endTime - startTime));
//        System.out.println(sessionid + " == IP: " + ip + " timestamp: == "
//                + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + "  end of getcount function ");
        return cnt;
    }

    // end, code added by pr on 4thsep19
//     public static String encodeURI(String value) throws UnsupportedEncodingException {
//
//        BASE64Encoder encoder = new BASE64Encoder();
//
//        byte[] salt = new byte[8];
//
//        rand.nextBytes(salt);
//
//        return encoder.encode(salt)
//                + encoder.encode(value.getBytes());
//
//    }
    public boolean isRaiseQueryEnabled(String roleInSession, String regNo) {
        try {
            Set<String> recpdata = new HashSet<>();
            PreparedStatement ps = null;
            ResultSet rs = null;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " inside isRaiseQueryEnabled roleInSession" + roleInSession);

            if (!regNo.isEmpty()) {
                try {
                    conSlave = DbConnection.getSlaveConnection();
                    String qry = "SELECT status, ca_email, coordinator_email,support_email, admin_email FROM final_audit_track WHERE registration_no = ?";
                    ps = conSlave.prepareStatement(qry);
                    ps.setString(1, regNo);
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " inside isRaiseQueryEnabled PS" + ps);

                    rs = ps.executeQuery();
                    recpdata.add("u");
                    if (rs.next()) {
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " inside isRaiseQueryEnabled status" + rs.getString("status").toLowerCase());
                        String caEmail = "";
                        String supEmail = "";
                        String coordEmail = "";
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
                            case "completed":
                                caEmail = rs.getString("ca_email");
                                supEmail = rs.getString("support_email");
                                coordEmail = rs.getString("coordinator_email");
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
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " inside isRaiseQueryEnabled recpdata" + recpdata);
                                break;
                            default:
                                break;
                        }
                    }

                } catch (Exception e) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " Replace printstcaktrace EXCEPTION 275" + e.getMessage());
                    e.printStackTrace();
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
            String currentrole = "";
            switch (roleInSession) {
                case Constants.ROLE_USER:
                    currentrole = "u";
                    break;
                case Constants.ROLE_CA:
                    currentrole = "ca";
                    break;
                case Constants.ROLE_SUP:
                    currentrole = "s";
                    break;
                case Constants.ROLE_MAILADMIN:
                    currentrole = "m";
                    break;
                case Constants.ROLE_DA:
                    currentrole = "da";
                    break;
                case Constants.ROLE_CO:
                    currentrole = "c";
                    break;
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " inside isRaiseQueryEnabled recpdata::::" + recpdata + "::::currentrole:::" + currentrole);

            if (recpdata.contains(currentrole)) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(ForwardAction.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
