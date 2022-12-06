package admin;

import static com.opensymphony.xwork2.Action.SUCCESS;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.org.bean.ListBean;
import com.org.connections.DbConnection;
import java.sql.SQLException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author DJ
 * @Reviewer Ashwini
 */
public class DashBoard extends ActionSupport implements SessionAware {

    Map sessionMap;
    String userAgent = ServletActionContext.getRequest().getHeader("User-Agent");
    private String showValue = "";
    private String dateRange = "";
    private String filter = "";
    public static ListBean listBeanObj;
    private String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Map<String, Object> val = new HashMap<>();
    JSONArray ja;
    JSONArray ja1;
    int totalCount = 0;
    int totalpending = 0;
    int totalComplete = 0;
    int totalRejected = 0;

    @Override
    public void setSession(Map sessionMap) {
        this.sessionMap = sessionMap;
    }

    public String getShowValue() {
        return showValue;
    }

    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ListBean getListBeanObj() {
        return listBeanObj;
    }

    public void setListBeanObj(ListBean listBeanObj) {
        this.listBeanObj = listBeanObj;
    }

    public JSONArray getJa() {
        return ja;
    }

    public void setJa(JSONArray ja) {
        this.ja = ja;
    }

    public JSONArray getJa1() {
        return ja1;
    }

    public void setJa1(JSONArray ja1) {
        this.ja1 = ja1;
    }

    public Map<String, Object> getVal() {
        return val;
    }

    public void setVal(Map<String, Object> val) {
        this.val = val;
    }

    public String showDashBoard() { //displaying dashboard by dhiren at 30May2018
        String returnResponse = "error";
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == In dashboard ::::: == USERAGENT " + userAgent + " == ");
        listBeanObj = new ListBean();
        if (sessionMap != null) {
            if (!showValue.isEmpty() && showValue.equalsIgnoreCase("dash")) {
                totalCount = fetchDashboardCount("all", "", "", "");
                totalpending = fetchDashboardCount("pending", "", "", "");
                totalComplete = fetchDashboardCount("completed", "", "", "");
                totalRejected = fetchDashboardCount("rejected", "", "", "");
                listBeanObj.setTotalRequest(Integer.toString(totalCount));
                listBeanObj.setPendingRequest(Integer.toString(totalpending));
                listBeanObj.setCompleteRequest(Integer.toString(totalComplete));
                listBeanObj.setRejectedRequest(Integer.toString(totalRejected));
                returnResponse = SUCCESS;
            }
        }
        return returnResponse;
    }

    public String RequestCounter() throws SQLException, ClassNotFoundException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            int count = 0;
            String qry = "SELECT count(*) FROM `user_profile`";
            con = DbConnection.getSlaveConnection();
            ps = con.prepareStatement(qry);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            System.out.println("In Login Index Page");
            totalCount = fetchDashboardCount("all", "", "", "");
            totalpending = fetchDashboardCount("pending", "", "", "");
            totalComplete = fetchDashboardCount("completed", "", "", "");
            val.put("totalrequest", totalCount);
            val.put("totalpending", totalpending);
            val.put("totalComplete", totalComplete);
            val.put("usercount", count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps.close();
            rs.close();
        }
        return SUCCESS;
    }

    public String RequestCount() {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == In dashboard Show Request Count For:::::::::::::::::::::::" + showValue + " :::: Date Range Value :" + dateRange + ":::::  Filter Value :" + filter + " == USERAGENT " + userAgent + " == ");
        int single = 0, bulk = 0, dlist = 0, bulkdlist=0, dns = 0, gem = 0, imappop = 0, ip = 0, ldap = 0, mobile = 0, nkn_bulk = 0, nkn_single = 0, relay = 0, sms = 0, wifi = 0, total = 0, pending = 0, rejected = 0, completed = 0;
        int da_pending = 0, ro_pending = 0, support_pending = 0, mailadmin_pending = 0, coordinator_pending = 0, manual = 0;
        int da_rejected = 0, ro_rejected = 0, support_rejected = 0, mailadmin_rejected = 0, coordinator_rejected = 0, manual_rejected = 0;
        LocalDate date1 = null, date2 = null;
        String dt = "";
        if (dateRange == null || dateRange == "" || dateRange.equals("")) {
            System.out.println("Date Range is blank ::::");
        } else if (dateRange.contains("-")) {
            String[] str = dateRange.split("\\-");
            String start = str[0].trim();
            String end = str[1].trim();
            System.out.println("start =>" + start);
            System.out.println("End   =>" + end);
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
                date1 = LocalDate.parse(start, formatter);
                date2 = LocalDate.parse(end, formatter);
                System.out.println("date ::: Start ::" + date1);
                System.out.println("date ::: End   ::" + date2);
                if (date1.compareTo(date2) == 0) {
                    dt = "date(to_datetime) ='" + date1 + "'";
                } else {
                    dt = "date(to_datetime) between '" + date1 + "' and '" + date2 + "'";
                }
            } catch (Exception ex) {
                Logger.getLogger(NewCAHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((showValue.equals("total") || showValue.equals("pending") || showValue.equals("rejected") || showValue.equals("completed") || showValue.equals("select")) && (dateRange.equals("") && filter.equals("all"))) {
            if (!showValue.equals("select")) {
                single = fetchDashboardCount("single", showValue, "", filter);
                bulk = fetchDashboardCount("bulk", showValue, "", filter);
                dlist = fetchDashboardCount("dlist", showValue, "", filter);
                bulkdlist = fetchDashboardCount("bulkdlist", showValue, "", filter); // Added by Rahul jan 2021
                dns = fetchDashboardCount("dns", showValue, "", filter);
                gem = fetchDashboardCount("gem", showValue, "", filter);
                imappop = fetchDashboardCount("imappop", showValue, "", filter);
                ip = fetchDashboardCount("ip", showValue, "", filter);
                ldap = fetchDashboardCount("ldap", showValue, "", filter);
                mobile = fetchDashboardCount("mobile", showValue, "", filter);
                nkn_bulk = fetchDashboardCount("nkn_bulk", showValue, "", filter);
                nkn_single = fetchDashboardCount("nkn_single", showValue, "", filter);
                relay = fetchDashboardCount("relay", showValue, "", filter);
                sms = fetchDashboardCount("sms", showValue, "", filter);
                wifi = fetchDashboardCount("wifi", showValue, "", filter);
            }
            if (showValue.equals("pending")) {
                da_pending = fetchDashCount(showValue, "da_pending", dt, filter);
                ro_pending = fetchDashCount(showValue, "ca_pending", dt, filter);
                support_pending = fetchDashCount(showValue, "support_pending", dt, filter);
                mailadmin_pending = fetchDashCount(showValue, "mail-admin_pending", dt, filter);
                coordinator_pending = fetchDashCount(showValue, "coordinator_pending", dt, filter);
                manual = fetchDashCount(showValue, "manual", dt, filter);
                val.put("da_pending", da_pending);
                val.put("ro_pending", ro_pending);
                val.put("support_pending", support_pending);
                val.put("mailadmin_pending", mailadmin_pending);
                val.put("coordinator_pending", coordinator_pending);
                val.put("manual", manual);
            } else if (showValue.equals("rejected")) {
                da_rejected = fetchDashCount(showValue, "da_rejected", dt, filter);
                ro_rejected = fetchDashCount(showValue, "ca_rejected", dt, filter);
                support_rejected = fetchDashCount(showValue, "support_rejected", dt, filter);
                mailadmin_rejected = fetchDashCount(showValue, "mail-admin_rejected", dt, filter);
                coordinator_rejected = fetchDashCount(showValue, "coordinator_rejected", dt, filter);
                manual_rejected = fetchDashCount(showValue, "cancel", dt, filter);
                val.put("da_rejected", da_rejected);
                val.put("ro_rejected", ro_rejected);
                val.put("support_rejected", support_rejected);
                val.put("mailadmin_rejected", mailadmin_rejected);
                val.put("coordinator_rejected", coordinator_rejected);
                val.put("manual_rejected", manual_rejected);
            } else if (showValue.equals("select")) {

                setShowValue("dash");
                showDashBoard();

                single = fetchDashboardCount("single", "pending", "", filter);
                bulk = fetchDashboardCount("bulk", "pending", "", filter);
                dlist = fetchDashboardCount("dlist", "pending", "", filter);
                bulkdlist = fetchDashboardCount("bulkdlist", "pending", "", filter);
                dns = fetchDashboardCount("dns", "pending", "", filter);
                gem = fetchDashboardCount("gem", "pending", "", filter);
                imappop = fetchDashboardCount("imappop", "pending", "", filter);
                ip = fetchDashboardCount("ip", "pending", "", filter);
                ldap = fetchDashboardCount("ldap", "pending", "", filter);
                mobile = fetchDashboardCount("mobile", "pending", "", filter);
                nkn_bulk = fetchDashboardCount("nkn_bulk", "pending", "", filter);
                nkn_single = fetchDashboardCount("nkn_single", "pending", "", filter);
                relay = fetchDashboardCount("relay", "pending", "", filter);
                sms = fetchDashboardCount("sms", "pending", "", filter);
                wifi = fetchDashboardCount("wifi", "pending", "", filter);
                da_pending = fetchDashCount(showValue, "da_pending", dt, filter);
                ro_pending = fetchDashCount(showValue, "ca_pending", dt, filter);
                support_pending = fetchDashCount(showValue, "support_pending", dt, filter);
                mailadmin_pending = fetchDashCount(showValue, "mail-admin_pending", dt, filter);
                coordinator_pending = fetchDashCount(showValue, "coordinator_pending", dt, filter);
                manual = fetchDashCount(showValue, "manual", dt, filter);
                val.put("da_pending", da_pending);
                val.put("ro_pending", ro_pending);
                val.put("support_pending", support_pending);
                val.put("mailadmin_pending", mailadmin_pending);
                val.put("coordinator_pending", coordinator_pending);
                val.put("manual", manual);
            }
            if (!showValue.equals("select")) {
                val.put("totalrequest", fetchDashboardCount("all", "", dt, ""));
                val.put("totalpending", fetchDashboardCount("pending", "", dt, ""));
                val.put("totalComplete", fetchDashboardCount("completed", "", dt, ""));
                val.put("totalRejected", fetchDashboardCount("rejected", "", dt, ""));
            }
            //for filter button click with dateRange and filter=all
        } else if ((showValue.equals("total") || showValue.equals("pending") || showValue.equals("rejected") || showValue.equals("completed")) && (!dateRange.equals("") && filter.equals("all"))) {
            System.out.println("check for date range::::" + date1 + " /" + date2);
            single = fetchDashboardCount("single", showValue, dt, filter);
            bulk = fetchDashboardCount("bulk", showValue, dt, filter);
            dlist = fetchDashboardCount("dlist", showValue, dt, filter);
            bulkdlist = fetchDashboardCount("bulkdlist", showValue, dt, filter);  //added by rahul jan 2021
            dns = fetchDashboardCount("dns", showValue, dt, filter);
            gem = fetchDashboardCount("gem", showValue, dt, filter);
            imappop = fetchDashboardCount("imappop", showValue, dt, filter);
            ip = fetchDashboardCount("ip", showValue, dt, filter);
            ldap = fetchDashboardCount("ldap", showValue, dt, filter);
            mobile = fetchDashboardCount("mobile", showValue, dt, filter);
            nkn_bulk = fetchDashboardCount("nkn_bulk", showValue, dt, filter);
            nkn_single = fetchDashboardCount("nkn_single", showValue, dt, filter);
            relay = fetchDashboardCount("relay", showValue, dt, filter);
            sms = fetchDashboardCount("sms", showValue, dt, filter);
            wifi = fetchDashboardCount("wifi", showValue, dt, filter);
            if (showValue.equals("pending")) {
                da_pending = fetchDashCount(showValue, "da_pending", dt, filter);
                ro_pending = fetchDashCount(showValue, "ca_pending", dt, filter);
                support_pending = fetchDashCount(showValue, "support_pending", dt, filter);
                mailadmin_pending = fetchDashCount(showValue, "mail-admin_pending", dt, filter);
                coordinator_pending = fetchDashCount(showValue, "coordinator_pending", dt, filter);
                manual = fetchDashCount(showValue, "manual", dt, filter);
                val.put("da_pending", da_pending);
                val.put("ro_pending", ro_pending);
                val.put("support_pending", support_pending);
                val.put("mailadmin_pending", mailadmin_pending);
                val.put("coordinator_pending", coordinator_pending);
                val.put("manual", manual);
            } else if (showValue.equals("rejected")) {
                da_rejected = fetchDashCount(showValue, "da_rejected", dt, filter);
                ro_rejected = fetchDashCount(showValue, "ca_rejected", dt, filter);
                support_rejected = fetchDashCount(showValue, "support_rejected", dt, filter);
                mailadmin_rejected = fetchDashCount(showValue, "mail-admin_rejected", dt, filter);
                coordinator_rejected = fetchDashCount(showValue, "coordinator_rejected", dt, filter);
                manual_rejected = fetchDashCount(showValue, "cancel", dt, filter);
                val.put("da_rejected", da_rejected);
                val.put("ro_rejected", ro_rejected);
                val.put("support_rejected", support_rejected);
                val.put("mailadmin_rejected", mailadmin_rejected);
                val.put("coordinator_rejected", coordinator_rejected);
                val.put("manual_rejected", manual_rejected);
            }
            val.put("totalrequest", fetchDashboardCount("all", "", dt, ""));
            val.put("totalpending", fetchDashboardCount("pending", "", dt, ""));
            val.put("totalComplete", fetchDashboardCount("completed", "", dt, ""));
            val.put("totalRejected", fetchDashboardCount("rejected", "", dt, ""));
            //for filter button click without dateRange and filter=form
        } else if ((showValue.equals("single") || showValue.equals("bulk") || showValue.equals("nkn_single") || showValue.equals("nkn_bulk") || showValue.equals("gem") || showValue.equals("mobile") || showValue.equals("imappop") || showValue.equals("dlist") || showValue.equals("bulkdlist") || showValue.equals("ip") || showValue.equals("ldap") || showValue.equals("relay") || showValue.equals("sms") || showValue.equals("wifi") || showValue.equals("dns")) && (dateRange.equals("") && filter.equals("form"))) {
            val.put("totalrequest", fetchDashboardCount(showValue, "total", "", filter));
            val.put("totalpending", fetchDashboardCount(showValue, "pending", "", filter));
            val.put("totalRejected", fetchDashboardCount(showValue, "rejected", "", filter));
            val.put("totalComplete", fetchDashboardCount(showValue, "completed", "", filter));
            //for stats 13July2018
            da_pending = fetchDashCount(showValue, "da_pending", "", filter);
            ro_pending = fetchDashCount(showValue, "ca_pending", "", filter);
            support_pending = fetchDashCount(showValue, "support_pending", "", filter);
            mailadmin_pending = fetchDashCount(showValue, "mail-admin_pending", "", filter);
            coordinator_pending = fetchDashCount(showValue, "coordinator_pending", "", filter);
            manual = fetchDashCount(showValue, "manual", "", filter);
            da_rejected = fetchDashCount(showValue, "da_rejected", "", filter);
            ro_rejected = fetchDashCount(showValue, "ca_rejected", "", filter);
            support_rejected = fetchDashCount(showValue, "support_rejected", "", filter);
            mailadmin_rejected = fetchDashCount(showValue, "mail-admin_rejected", "", filter);
            coordinator_rejected = fetchDashCount(showValue, "coordinator_rejected", "", filter);
            manual_rejected = fetchDashCount(showValue, "cancel", "", filter);
            //for filter button click with dateRange and filter=form
        } else if ((showValue.equals("single") || showValue.equals("bulk") || showValue.equals("nkn_single") || showValue.equals("nkn_bulk") || showValue.equals("gem") || showValue.equals("mobile") || showValue.equals("imappop") || showValue.equals("dlist") || showValue.equals("bulkdlist") || showValue.equals("ip") || showValue.equals("ldap   ") || showValue.equals("relay") || showValue.equals("sms") || showValue.equals("wifi") || showValue.equals("dns")) && (!dateRange.equals("") && filter.equals("form"))) {
            val.put("totalrequest", fetchDashboardCount(showValue, "total", dt, filter));
            val.put("totalpending", fetchDashboardCount(showValue, "pending", dt, filter));
            val.put("totalRejected", fetchDashboardCount(showValue, "rejected", dt, filter));
            val.put("totalComplete", fetchDashboardCount(showValue, "completed", dt, filter));
            System.out.println("in date range :::" + dt);
            //for stats 13July2018
            da_pending = fetchDashCount(showValue, "da_pending", dt, filter);
            ro_pending = fetchDashCount(showValue, "ca_pending", dt, filter);
            support_pending = fetchDashCount(showValue, "support_pending", dt, filter);
            mailadmin_pending = fetchDashCount(showValue, "mail-admin_pending", dt, filter);
            coordinator_pending = fetchDashCount(showValue, "coordinator_pending", dt, filter);
            manual = fetchDashCount(showValue, "manual", dt, filter);
            da_rejected = fetchDashCount(showValue, "da_rejected", dt, filter);
            ro_rejected = fetchDashCount(showValue, "ca_rejected", dt, filter);
            support_rejected = fetchDashCount(showValue, "support_rejected", dt, filter);
            mailadmin_rejected = fetchDashCount(showValue, "mail-admin_rejected", dt, filter);
            coordinator_rejected = fetchDashCount(showValue, "coordinator_rejected", dt, filter);
            manual_rejected = fetchDashCount(showValue, "cancel", dt, filter);
        }
        //System.out.println("SUM of request " + showValue + "::::" + (single + bulk + dlist + dns + gem + imappop + ip + ldap + mobile + nkn_bulk + nkn_single + relay + sms + wifi));
        //JSONArray ja = null;
        if (filter.equals("all") || filter.equals("")) {
            ja = chartPrepare(single, bulk, dlist, bulkdlist, dns, gem, imappop, ip, ldap, mobile, nkn_bulk, nkn_single, relay, sms, wifi);
        } else {
            ja = chartPrepareForm(da_pending, ro_pending, support_pending, mailadmin_pending, coordinator_pending, manual);
            ja1 = chartPrepareForm1(da_rejected, ro_rejected, support_rejected, mailadmin_rejected, coordinator_rejected, manual_rejected);
        }
        //System.out.println("chart data list  for pending:::::::::::: JSONArray" + ja);
        return SUCCESS;
    }

    public int fetchDashboardCount(String form_search, String status_type, String date1, String date2) {         //get dashboard count by dhiren at 30May2018
        int count = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            String qry = " select count(*) from final_audit_track ";
            if (form_search.equalsIgnoreCase("all") && status_type.equals("") && (date1.equals("") || !date1.equals("")) && date2.equals("")) { //for total request
                if (!date1.equals("")) {
                    qry += "where " + date1;
                } else {
                }
            } else if (form_search.equalsIgnoreCase("pending") && status_type.equals("") && (date1.equals("") || !date1.equals("")) && date2.equals("")) { //for total Pending request
                if (!date1.equals("")) {
                    qry += " where ( status = 'api' OR status = 'manual' OR status = 'manual_upload' OR  status = 'ca_pending' OR  status = 'us_pending' OR  status = 'coordinator_pending' OR  status = 'support_pending' OR  status = 'da_pending' OR  status = 'mail-admin_pending' ) and " + date1;
                } else {
                    qry += " where (status = 'api' OR status = 'manual' OR status = 'manual_upload' OR  status = 'ca_pending' OR  status = 'us_pending' OR  status = 'coordinator_pending' OR  status = 'support_pending' OR  status = 'da_pending' OR  status = 'mail-admin_pending')";
                }
            } else if (form_search.equalsIgnoreCase("completed") && status_type.equals("") && (date1.equals("") || !date1.equals("")) && date2.equals("")) { //for total completed request
                if (!date1.equals("")) {
                    qry += " where (status ='" + form_search + "') and " + date1;
                } else {
                    qry += " where (status ='" + form_search + "')";
                }
            } else if (form_search.equalsIgnoreCase("rejected") && status_type.equals("") && (date1.equals("") || !date1.equals("")) && date2.equals("")) {    //for total rejected request
                if (!date1.equals("")) {
                    qry += " where (status = 'cancel' OR status LIKE '%" + form_search + "%') and " + date1;
                } else {
                    qry += " where (status = 'cancel' OR status LIKE '%" + form_search + "%') ";
                }
                //for without date range filter=all
            } else if ((form_search.equalsIgnoreCase("bulk") || form_search.equalsIgnoreCase("dlist") || form_search.equalsIgnoreCase("bulkdlist") || form_search.equalsIgnoreCase("dns") || form_search.equalsIgnoreCase("gem")
                    || form_search.equalsIgnoreCase("imappop") || form_search.equalsIgnoreCase("ip") || form_search.equalsIgnoreCase("ldap") || form_search.equalsIgnoreCase("mobile")
                    || form_search.equalsIgnoreCase("nkn_bulk") || form_search.equalsIgnoreCase("nkn_single") || form_search.equalsIgnoreCase("relay") || form_search.equalsIgnoreCase("single")
                    || form_search.equalsIgnoreCase("sms") || form_search.equalsIgnoreCase("wifi")) && (status_type.equals("total") || status_type.equals("pending") || status_type.equals("rejected") || status_type.equals("completed")) && date1.equals("") && (date2.equals("all") || date2.equals("form"))) {
                if (status_type.equals("total")) {
                    qry += " where form_name ='" + form_search + "'";
                } else if (status_type.equals("pending")) {
                    qry += " where form_name ='" + form_search + "' and (status = 'api' OR status = 'manual' OR status = 'manual_upload' OR  status = 'ca_pending' OR  status = 'us_pending' OR  status = 'coordinator_pending' OR  status = 'support_pending' OR  status = 'da_pending' OR  status = 'mail-admin_pending')";
                } else if (status_type.equals("rejected")) {
                    qry += " where form_name ='" + form_search + "' and (status = 'us_rejected' OR status = 'ca_rejected' OR status = 'support_rejected' OR status = 'coordinator_rejected' OR status = 'da_rejected' OR status = 'mail-admin_rejected' OR status = 'cancel')";
                } else if (status_type.equals("completed")) {
                    qry += " where form_name ='" + form_search + "' and (status ='" + status_type + "')";
                }
                //for date range filter=all
            } else if ((form_search.equalsIgnoreCase("bulk") || form_search.equalsIgnoreCase("dlist") || form_search.equalsIgnoreCase("bulkdlist") || form_search.equalsIgnoreCase("dns") || form_search.equalsIgnoreCase("gem")
                    || form_search.equalsIgnoreCase("imappop") || form_search.equalsIgnoreCase("ip") || form_search.equalsIgnoreCase("ldap") || form_search.equalsIgnoreCase("mobile")
                    || form_search.equalsIgnoreCase("nkn_bulk") || form_search.equalsIgnoreCase("nkn_single") || form_search.equalsIgnoreCase("relay") || form_search.equalsIgnoreCase("single")
                    || form_search.equalsIgnoreCase("sms") || form_search.equalsIgnoreCase("wifi")) && (status_type.equals("total") || status_type.equals("pending") || status_type.equals("rejected") || status_type.equals("completed")) && !date1.equals("") && (date2.equals("all") || date2.equals("form"))) {
                if (status_type.equals("total")) {
                    qry += " where form_name ='" + form_search + "' and " + date1;
                } else if (status_type.equals("pending")) {
                    qry += " where form_name ='" + form_search + "' and (status = 'api' OR status = 'manual' OR status = 'manual_upload' OR  status = 'ca_pending' OR  status = 'us_pending' OR  status = 'coordinator_pending' OR  status = 'support_pending' OR  status = 'da_pending' OR  status = 'mail-admin_pending') and " + date1;
                } else if (status_type.equals("rejected")) {
                    qry += " where form_name ='" + form_search + "' and (status = 'us_rejected' OR status = 'ca_rejected' OR status = 'support_rejected' OR status = 'coordinator_rejected' OR status = 'da_rejected' OR status = 'mail-admin_rejected' OR status = 'cancel') and " + date1;
                } else if (status_type.equals("completed")) {
                    qry += " where form_name ='" + form_search + "' and (status ='" + status_type + "') and " + date1;
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == fetchDashboardCount query:::::::::::::::::::::::" + qry + "  == USERAGENT " + userAgent + " == ");
            ps = con.prepareStatement(qry);
            System.out.println("SQL QUERY:::: "+qry);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return count;
    }

    public int fetchDashCount(String form_name, String status, String date1, String filter) {         //get dashboard count by dhiren at 13July2018
        int count = 0;
        ResultSet rs = null;
        PreparedStatement ps = null;
        //Connection con = null;
        try {
            con = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            String qry = " select count(*) from final_audit_track ";
            if (filter.equalsIgnoreCase("form") && date1.equals("")) { //for without date request
                if (status.equalsIgnoreCase("manual")) {
                    qry += "  where form_name='" + form_name + "' and  (status = 'api' OR status = 'manual' OR status = 'manual_upload')";
                } else {
                    qry += "  where form_name='" + form_name + "' and status='" + status + "'";
                }
            } else if (filter.equalsIgnoreCase("form") && !date1.equals("")) { //for with date request
                if (status.equalsIgnoreCase("manual")) {
                    qry += "  where  form_name ='" + form_name + "' and (status = 'api' OR status = 'manual' OR status = 'manual_upload') and " + date1;
                } else {
                    qry += "  where form_name='" + form_name + "' and status='" + status + "' and " + date1;
                }
            } else if (filter.equalsIgnoreCase("all") && date1.equals("")) {
                if (status.equalsIgnoreCase("manual")) {
                    qry += "  where (status = 'api' OR status = 'manual' OR status = 'manual_upload')";
                } else {
                    qry += "  where status='" + status + "'";
                }
            } else if (filter.equalsIgnoreCase("all") && !date1.equals("")) {
                if (status.equalsIgnoreCase("manual")) {
                    qry += "  where (status = 'api' OR status = 'manual' OR status = 'manual_upload') and " + date1;
                } else {
                    qry += "  where status='" + status + "' and " + date1;
                }
            }
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " == fetchDashCount query:::::::::::::::::::::::" + qry + "  == USERAGENT " + userAgent + " == ");

            ps = con.prepareStatement(qry);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        return count;
    }

    public JSONArray chartPrepareForm(int da_pending, int ro_pending, int support_pending, int mailadmin_pending, int coordinator_pending, int manual) {
        JSONObject jo;
        JSONArray ja = new JSONArray();
        jo = new JSONObject();
        jo.put("country", "Support Pending");
        jo.put("visits", support_pending);
        jo.put("color", "#F8FF01");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Mail-Admin Pending");
        jo.put("visits", mailadmin_pending);
        jo.put("color", "#B0DE09");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "DA Pending");
        jo.put("visits", da_pending);
        jo.put("color", "#04D215");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Coordinator Pending");
        jo.put("visits", coordinator_pending);
        jo.put("color", "#0D52D1");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "RO Pending");
        jo.put("visits", ro_pending);
        jo.put("color", "#0D8ECF");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "User Pending");
        jo.put("visits", manual);
        jo.put("color", "#2A0CD0");
        ja.add(jo);
        return ja;
    }

    public JSONArray chartPrepareForm1(int da_rejected, int ro_rejected, int support_rejected, int mailadmin_rejected, int coordinator_rejected, int manual_rejected) {
        JSONObject jo;
        JSONArray ja = new JSONArray();
        jo = new JSONObject();
        jo.put("country", "Support Rejected");
        jo.put("visits", support_rejected);
        jo.put("color", "#8A0CCF");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Mail-Admin Rejected");
        jo.put("visits", mailadmin_rejected);
        jo.put("color", "#CD0D74");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "DA Rejected");
        jo.put("visits", da_rejected);
        jo.put("color", "#FF0F00");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Coordinator Rejected");
        jo.put("visits", coordinator_rejected);
        jo.put("color", "#DDDDDD");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "RO Rejected");
        jo.put("visits", ro_rejected);
        jo.put("color", "#0D8ECF");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "User Rejected");
        jo.put("visits", manual_rejected);
        jo.put("color", "#FF6600");
        ja.add(jo);
        return ja;
    }

    public JSONArray chartPrepare(int single, int bulk, int dlist, int bulkdlist, int dns, int gem, int imappop, int ip, int ldap, int mobile, int nkn_bulk, int nkn_single, int relay, int sms, int wifi) {
        JSONObject jo;
        JSONArray ja = new JSONArray();
        jo = new JSONObject();
        jo.put("country", "Single Email");
        jo.put("visits", single);
        jo.put("color", "#FF0F00");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Bulk Email");
        jo.put("visits", bulk);
        jo.put("color", "#FF6600");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "NKN Single");
        jo.put("visits", nkn_single);
        jo.put("color", "#FF9E01");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "NKN Bulk");
        jo.put("visits", nkn_bulk);
        jo.put("color", "#FCD202");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Gem Email");
        jo.put("visits", gem);
        jo.put("color", "#F8FF01");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Mobile Update");
        jo.put("visits", mobile);
        jo.put("color", "#B0DE09");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Imap/Pop");
        jo.put("visits", imappop);
        jo.put("color", "#04D215");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Distribution List");
        jo.put("visits", dlist);
        jo.put("color", "#0D8ECF");
        ja.add(jo);
         jo = new JSONObject();
        jo.put("country", "Bulk Distribution List");
        jo.put("visits", bulkdlist);
        jo.put("color", "#0D8ECF");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "IP Change");
        jo.put("visits", ip);
        jo.put("color", "#0D52D1");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Ldap Auth");
        jo.put("visits", ldap);
        jo.put("color", "#2A0CD0");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Relay");
        jo.put("visits", relay);
        jo.put("color", "#8A0CCF");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "SMS");
        jo.put("visits", sms);
        jo.put("color", "#CD0D74");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "Wi-Fi");
        jo.put("visits", wifi);
        jo.put("color", "#754DEB");
        ja.add(jo);
        jo = new JSONObject();
        jo.put("country", "DNS");
        jo.put("visits", dns);
        jo.put("color", "#DDDDDD");
        ja.add(jo);
        return ja;
    }
    // End dashboard
}
