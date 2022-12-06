package admin;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.google.gson.Gson;
import com.org.connections.DbConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import validation.validation;
//import static xmlresponse.Xmlresponse.con;

/**
 *
 * @author nitin
 */
public class jsn_backup15022022 {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null; // this line added by pr on 20thjun18
    Connection condb = null; // this line added by pr on 20thjun18

    public void doTrustToCertificates() throws Exception {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    return;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    return;
                }
            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    // above function commented below added by pr on 20thjun18
    public String hitDNSUrl(String registration_no) throws SQLException, Exception {
        String response = "";
        PreparedStatement pst = null;
        ResultSet rs = null;

        ResultSet rs1 = null;
        //Connection con = null;
        try {
            con = DbConnection.getSlaveConnection();
            condb = DbConnection.getConnection();
            String qry = "select registration_no,dns_type,record_mx,record_ptr,record_srv,record_spf,record_txt,record_dmarc,auth_email,mobile,hod_email,hod_mobile,employment,"
                    + "ministry,department,other_dept,state,organization,server_location,record_mx1,record_ptr1,req_for, emp_code, datetime, service_url "
                    + "from dns_registration WHERE registration_no = ? ";

            pst = con.prepareStatement(qry);
            pst.setString(1, registration_no);
            System.out.println(" jsn query is " + pst);
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();

            HashMap<Object, Object> hm = new HashMap<Object, Object>(); // line added by pr on 13thjun18

            while (rs.next()) {

                int numColumns = rsmd.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    hm.put(column_name, rs.getObject(column_name)); // line added by pr on 13thjun18                               
                }

                String dnsRegister = rs.getString("registration_no"); // Entry for registration number
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dnsRegister:::" + dnsRegister);

                ArrayList<String> arrlist_cname = new ArrayList<String>(); // line added by pr on 13thjun18                               

                ArrayList<String> arrlist_newip = new ArrayList<String>(); // line added by pr on 13thjun18                               

                ArrayList<String> arrlist_oldip = new ArrayList<String>(); // line added by pr on 13thjun18                               

                ArrayList<String> arrlist_dnsurl = new ArrayList<String>(); // line added by pr on 13thjun18      

                validation validObj = new validation(); // line added by pr on 26thjul18

                //pst = con.prepareStatement("select * from dns_registration_newip where registration_no = ?");
                pst = con.prepareStatement("select newip from dns_registration_newip where registration_no = ?");// modified by pr on 7thjul2020
                pst.setString(1, dnsRegister);
                rs1 = pst.executeQuery();

                String invalidIPButValidURL = ""; // line added by pr on 26thjul18

                while (rs1.next()) {

                    //arrlist_newip.add(rs1.getString(2));  // line added by pr on 13thjun18                               
                    // above line modified with the below code by pr on 26thjul18
                    if (rs1.getString("newip") != null && !validObj.dnsipvalidation(rs1.getString("newip").trim())) // if the ip is valid then it should go to the newip
                    {
                        System.out.println(" inside newip dnsip validation true ");

                        arrlist_newip.add(rs1.getString("newip"));  // line added by pr on 13thjun18
                    } else if (rs1.getString("newip") != null && !validObj.dnsurlvalidation(rs1.getString("newip").trim()))// if the ip is invalid means it is a domain name then it should be stored in a variable to be used in dnsurl or cname
                    {
                        arrlist_newip.add(getDomainURLIP(rs1.getString("newip").trim()));  // this will nslookup the domain and returns the url

                        invalidIPButValidURL = rs1.getString("newip");
                    }

                }

                hm.put("newip", arrlist_newip); // line added by pr on 13thjun18                               

                pst = con.prepareStatement("select dns_url from dns_registration_url where registration_no = ?");
                pst.setString(1, dnsRegister);
                rs1 = pst.executeQuery();

                Boolean isDomainExist = false; // line added by pr on 26thjul18                        

                while (rs1.next()) {

                    //arrlist_dnsurl.add(rs1.getString(2));  // line added by pr on 13thjun18   
                    // above line commented below added by pr on 26thjul18
                    if (rs1.getString("dns_url") != null && !rs1.getString("dns_url").equals(""))// first check if there is some value in the domain for this reg no, if yes then skip assigneing the invalid ip to it else assign
                    {
                        isDomainExist = true;

                        arrlist_dnsurl.add(rs1.getString("dns_url"));  // line added by pr on 13thjun18                               
                    }

                }

                // below if added by pr on 26thjul18
                if (!invalidIPButValidURL.equals("") && !isDomainExist) // if there is no domain and invalidIPButValidURL exists  ,if invalid ip was found but it was a valid domain  then add that here
                {
                    arrlist_dnsurl.add(invalidIPButValidURL);
                }

                hm.put("dnsurl", arrlist_dnsurl); // line added by pr on 13thjun18                                            

                pst = con.prepareStatement("select cname from dns_registration_cname where registration_no = ?");
                pst.setString(1, dnsRegister);
                rs1 = pst.executeQuery();

                while (rs1.next()) {

                    arrlist_cname.add(rs1.getString("cname"));  // line added by pr on 13thjun18                               

                }

                // start, code added by pr on 26thjul18
                // remove from the arrlist_cname the values that exist in the arrlist_dnsurl
                arrlist_cname.removeAll(arrlist_dnsurl);

                if (!invalidIPButValidURL.equals("") && isDomainExist) // if ip was invalid and dns url exists then add in the cname field the given invalid url
                {
                    if (!arrlist_cname.contains(invalidIPButValidURL)) {
                        arrlist_cname.add(invalidIPButValidURL);
                    }
                }

                // end, code added by pr on 26thjul18                        
                hm.put("cname", arrlist_cname); // line added by pr on 13thjun18                               

                pst = con.prepareStatement("select oldip from dns_registration_oldip where registration_no = ?");
                pst.setString(1, dnsRegister);
                rs1 = pst.executeQuery();

                while (rs1.next()) {

                    arrlist_oldip.add(rs1.getString("oldip"));  // line added by pr on 13thjun18                               
                }

                hm.put("oldip", arrlist_oldip); // line added by pr on 13thjun18                               

                // start, code added by pr on 12thjun18
                // get the status related entries
                if (rs.getString("req_for").equals("req_new")) {
                    System.out.println("arrlist_newip.size()" + arrlist_newip.size());
                    for (int j = 0; j < arrlist_newip.size(); j++) {

                        String sql = "insert into domain_ip (dns_url,ip)"
                                + "values (?, ?)";
                        pst = condb.prepareStatement(sql);
                        pst.setString(1, arrlist_dnsurl.get(j).toString());
                        pst.setString(2, arrlist_newip.get(j).toString());
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==insert query for new request " + "e: " + pst);
                        pst.executeUpdate();
                    }
                }
                if (rs.getString("req_for").equals("req_delete")) {
                    System.out.println("arrlist_newip.size()" + arrlist_newip.size());
                    for (int j = 0; j < arrlist_newip.size(); j++) {

                        String sql = "delete from domain_ip where dns_url =? and ip=?";
                        pst = condb.prepareStatement(sql);
                        pst.setString(1, arrlist_dnsurl.get(j).toString());
                        pst.setString(2, arrlist_newip.get(j).toString());
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==delete query for delete request " + "e: " + pst);
                        pst.executeUpdate();
                    }
                }
                if (rs.getString("req_for").equals("req_modify")) {
                    for (int j = 0; j < arrlist_newip.size(); j++) {
                        String sql4 = "select count(*) from domain_ip where ip=? and dns_url=?";
                        pst = con.prepareStatement(sql4);
                        pst.setString(1, arrlist_oldip.get(j).toString());
                        pst.setString(2, arrlist_dnsurl.get(j).toString());
                        System.out.println("select query for modify request::::::::::" + pst);
                        rs = pst.executeQuery();
                        if (rs.next()) {
                            int rowCount = rs.getInt("count(*)");
                            if (rowCount > 0) {
                                String update = "update domain_ip set ip=? where ip =? and dns_url=?";
                                pst = con.prepareStatement(update);
                                pst.setString(1, arrlist_newip.get(j).toString());
                                pst.setString(2, arrlist_oldip.get(j).toString());
                                pst.setString(3, arrlist_dnsurl.get(j).toString());
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for update domain ip: " + pst);
                                pst.executeUpdate();
                            }
                        }
                    }
                }

                pst = con.prepareStatement("select stat_type,stat_forwarded_to_user,stat_forwarded_by,stat_forwarded_by_user,stat_createdon from status where stat_reg_no = ? ORDER BY stat_createdon ASC ");

                pst.setString(1, dnsRegister);

                rs1 = pst.executeQuery();

                while (rs1.next()) {
                    if (rs1.getString("stat_type") != null && !rs1.getString("stat_type").equals("") && rs1.getString("stat_type").equalsIgnoreCase("ca_pending")) {
                        String ROEmail = fetchCAEmail(rs1.getString("stat_forwarded_to_user"));
                    } //else if( rs1.getString("stat_type") != null  && rs1.getString("stat_type").equalsIgnoreCase("mail-admin_pending") )// forwarded_by not null check added by pr on 6thjul18
                    else if (rs1.getString("stat_type") != null && rs1.getString("stat_type").equalsIgnoreCase("mail-admin_pending") && (rs1.getString("stat_forwarded_by") != null && rs1.getString("stat_forwarded_by").equalsIgnoreCase("c"))) // line modified by pr on 22ndjun18
                    {
                        hm.put("req_approved_by_coordinator", rs1.getString("stat_forwarded_by_user")); // line added by pr on 13thjun18                               
                        hm.put("req_approved_by_coordinator_datetime", rs1.getString("stat_createdon")); // line added by pr on 13thjun18                               
                    } else if (rs1.getString("stat_type") != null && rs1.getString("stat_type").equalsIgnoreCase("completed")) {
                        hm.put("req_implemented_by_admin", rs1.getString("stat_forwarded_by_user")); // line added by pr on 13thjun18                               
                        hm.put("req_implemented_by_admin_datetime", rs1.getString("stat_createdon")); // line added by pr on 13thjun18                               
                    }

                    if (rs1.getString("stat_forwarded_by") != null && rs1.getString("stat_forwarded_by").equals("ca") && !rs1.getString("stat_type").equals("ca_rejected")) {
                        hm.put("req_approved_by_RO_datetime", rs1.getString("stat_createdon")); // line added by pr on 13thjun18                               
                    }

                }

            }

            for (Object k : hm.keySet()) {
                System.out.println("********** inside hm iterate key is " + k.toString() + " value is " + hm.get(k));
            }

            LinkedHashMap<Object, Object> hmFinal = new LinkedHashMap<Object, Object>();

            hmFinal.put("registration_no", "");

            hmFinal.put("dns_type", "");

            hmFinal.put("req_for", "");

            hmFinal.put("oldip", "");

            hmFinal.put("newip", "");

            hmFinal.put("dnsurl", "");

            hmFinal.put("server_location", "");

            hmFinal.put("cname", "");

            //hmFinal.put("record_aaaa", "");
            hmFinal.put("record_mx", "");

            hmFinal.put("record_mx1", "");

            hmFinal.put("record_ptr", "");

            hmFinal.put("record_ptr1", "");

            hmFinal.put("record_srv", "");

            hmFinal.put("record_spf", "");

            hmFinal.put("record_txt", "");

            hmFinal.put("record_dmarc", "");

            hmFinal.put("service_url_flag", "");

            hmFinal.put("applicant_email", "");

            hmFinal.put("applicant_mobile", "");

            hmFinal.put("applicant_emp_code", "");

            hmFinal.put("employment", "");

            hmFinal.put("ministry", "");

            hmFinal.put("organization", "");

            hmFinal.put("department", "");

            hmFinal.put("other_dept", "");

            hmFinal.put("state", "");

            hmFinal.put("submission_datetime", "");

            hmFinal.put("hod_email", "");

            hmFinal.put("hod_mobile", "");

            hmFinal.put("req_approved_by_RO_datetime", "");

            hmFinal.put("req_approved_by_coordinator", "");

            hmFinal.put("req_approved_by_coordinator_datetime", "");

            hmFinal.put("req_implemented_by_admin", "");

            hmFinal.put("req_implemented_by_admin_datetime", "");

            JSONArray jsonObj = null;
            JSONObject obj = new JSONObject();

            for (Object k : hmFinal.keySet()) {
                if (k.equals("registration_no")) {
                    hmFinal.replace("registration_no", hm.get("registration_no"));
                } else if (k.equals("dns_type")) {
                    hmFinal.replace("dns_type", hm.get("dns_type"));
                } else if (k.equals("req_for")) {
                    hmFinal.replace("req_for", hm.get("req_for"));
                } else if (k.equals("oldip")) {
                    hmFinal.replace("oldip", hm.get("oldip"));
                } else if (k.equals("newip")) {
                    hmFinal.replace("newip", hm.get("newip"));
                } else if (k.equals("server_location")) {
                    hmFinal.replace("server_location", hm.get("server_location"));
                } else if (k.equals("cname")) {
                    hmFinal.replace("cname", hm.get("cname"));
                } else if (k.equals("dnsurl")) {
                    hmFinal.replace("dnsurl", hm.get("dnsurl"));
                } //                    else if( k.equals("record_aaaa") )
                //                    {                        
                //                       hmFinal.replace("record_aaaa", hm.get("record_aaaa"));
                //                    }
                else if (k.equals("record_mx")) {
                    hmFinal.replace("record_mx", hm.get("record_mx"));
                } else if (k.equals("record_mx1")) {
                    hmFinal.replace("record_mx1", hm.get("record_mx1"));
                } else if (k.equals("record_ptr")) {
                    hmFinal.replace("record_ptr", hm.get("record_ptr"));
                } else if (k.equals("record_ptr1")) {
                    hmFinal.replace("record_ptr1", hm.get("record_ptr1"));
                } else if (k.equals("record_srv")) {
                    hmFinal.replace("record_srv", hm.get("record_srv"));
                } else if (k.equals("record_spf")) {
                    hmFinal.replace("record_spf", hm.get("record_spf"));
                } else if (k.equals("record_txt")) {
                    hmFinal.replace("record_txt", hm.get("record_txt"));
                } else if (k.equals("record_dmarc")) {
                    hmFinal.replace("record_dmarc", hm.get("record_dmarc"));
                } else if (k.equals("service_url_flag")) {
                    String str = "no";

                    if (hm.get("service_url").equals("service_url")) {
                        str = "yes";
                    }

                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside hm k is service_url its value is " + hm.get("service_url") + " str value is " + str);

                    hmFinal.replace("service_url_flag", str);
                } else if (k.equals("applicant_email")) {
                    hmFinal.replace("applicant_email", hm.get("auth_email"));
                } else if (k.equals("applicant_mobile")) {
                    hmFinal.replace("applicant_mobile", hm.get("mobile"));
                } else if (k.equals("applicant_emp_code")) {
                    //hmFinal.replace("applicant_emp_code", hm.get("emp_code"));

                    if (hm.get("emp_code") != null && !hm.get("emp_code").equals("")) // if around and else added by pr on 11thjul18
                    {
                        hmFinal.replace("applicant_emp_code", hm.get("emp_code").toString().trim());
                    } else {
                        hmFinal.replace("applicant_emp_code", "Not Available");
                    }

                } else if (k.equals("employment")) {
                    hmFinal.replace("employment", hm.get("employment"));
                } else if (k.equals("ministry")) {
                    hmFinal.replace("ministry", hm.get("ministry"));
                } else if (k.equals("organization")) {
                    hmFinal.replace("organization", hm.get("organization"));
                } else if (k.equals("department")) {
                    hmFinal.replace("department", hm.get("department"));
                } else if (k.equals("other_dept")) {
                    hmFinal.replace("other_dept", hm.get("other_dept"));
                } else if (k.equals("state")) {
                    hmFinal.replace("state", hm.get("state"));
                } else if (k.equals("submission_datetime")) {
                    hmFinal.replace("submission_datetime", hm.get("datetime"));
                } else if (k.equals("hod_email")) {
                    hmFinal.replace("hod_email", hm.get("hod_email"));
                } else if (k.equals("hod_mobile")) {
                    hmFinal.replace("hod_mobile", hm.get("hod_mobile"));
                } else if (k.equals("req_approved_by_RO_datetime")) {
                    hmFinal.replace("req_approved_by_RO_datetime", hm.get("req_approved_by_RO_datetime"));
                } else if (k.equals("req_approved_by_coordinator")) {
                    hmFinal.replace("req_approved_by_coordinator", hm.get("req_approved_by_coordinator"));
                } else if (k.equals("req_approved_by_coordinator_datetime")) {
                    hmFinal.replace("req_approved_by_coordinator_datetime", hm.get("req_approved_by_coordinator_datetime"));
                } else if (k.equals("req_implemented_by_admin")) {
                    hmFinal.replace("req_implemented_by_admin", hm.get("req_implemented_by_admin"));
                } else if (k.equals("req_implemented_by_admin_datetime")) {
                    hmFinal.replace("req_implemented_by_admin_datetime", hm.get("req_implemented_by_admin_datetime"));
                }
//                if (hm.get("req_for").equals("req_new")) {
//
//                    String sql = "insert into domain_ip (dns_url,ip)"
//                            + "values (?, ?)";
//                    pst = condb.prepareStatement(sql);
//                    pst.setString(1, hm.get("dnsurl").toString());
//                    pst.setString(2, hm.get("newip").toString());
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==sql3 " + "e: " + sql);
//                    pst.executeUpdate();
//
//                }
//                if (hm.get("req_for").equals("req_new")) {
//
//                    String sql4 = "select count(*) from domain_ip where ip=?";
//                    pst = con.prepareStatement(sql4);
//                    pst.setString(1, hm.get("oldip").toString());
//                    System.out.println("query 1::::::::::" + pst);
//                    rs = pst.executeQuery();
//                    if (rs.next()) {
//                        int rowCount = rs.getInt("count(*)");
//                        if (rowCount > 0) {
//                            String update = "update domain_ip set ip=? where ip =?";
//                            pst = condb.prepareStatement(update);
//                            pst.setString(1, hm.get("newip").toString());
//                            pst.setString(2, hm.get("oldip").toString());
//                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for update domain ip: " + pst);
//                            pst.executeUpdate();
//                        }
//                    }
//
//                }

            }

            String json = new Gson().toJson(hmFinal);

            String jsonRequest = "[" + json + "]"; // line modified by pr on 20thjun18

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "************ gson formed is " + jsonRequest);

            try {
                // below code to be uncommented in production done for testing on 26thjul18

                doTrustToCertificates();

                URL url = new URL("https://personnel.nic.in/webservice_ldap/consumejson.php");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                int len = jsonRequest.length();

                conn.setRequestProperty("Content-Length", String.valueOf(len));

                conn.setDoOutput(true);

                conn.getOutputStream().write(jsonRequest.getBytes("UTF-8"));

                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;) {
                    sb.append((char) c);
                }

                response = sb.toString();

                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " RESPONSE IS " + response);

            } catch (Exception e) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " inside exception ");

                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "MySQL Connection Exception" + e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
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
            if (rs1 != null) {
                try {
                    rs1.close();
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

        // end, code to connect to the url and read ots response
        return response;

    }

    // below function added by pr on 3rddec19  , new param req_for added by pr on 8thjan2020
//      public String dns_push_domains(String reg, String req_for, String status) {
//        String result = "";
//
//        String ticket = "close";
//
//        String subdomain = "", newip = "";
//
//        PreparedStatement pst = null;
//        ResultSet rs = null;
//
//        if (status.equals("approved")) {
//            try {
//                con = DbConnection.getSlaveConnection();
//
//                String qry = "SELECT ip.newip, url.dns_url FROM dns_registration_url url JOIN dns_registration_newip ip ON url.registration_no = ip.registration_no WHERE "
//                        + "url.registration_no = ? ";
//
//                pst = con.prepareStatement(qry);
//                pst.setString(1, reg);
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains query for approval is " + pst);
//
//                rs = pst.executeQuery();
//
//                while (rs.next()) {
//
//                    subdomain = rs.getString("dns_url");
//
//                    newip = rs.getString("newip");
//                }
//
//            } catch (Exception e) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains MySQL Connection Exception" + e);
//            }
//
//            if (!subdomain.isEmpty() && !newip.isEmpty()) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " subdomain " + subdomain + " , newip " + newip + " found for reg no " + reg);
//
//                try {
//                    String reg_no = "{\"subdomain\": \"" + subdomain + "\",\"mapped_ip\": \"" + newip + "\",\"status\": \"1\",\"ticket\": \"" + ticket + "\",\"eform_Id\":\"" + reg + "\",\"request_type\":\"" + req_for + "\",\"request_status\":\"" + status + "\"}";
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reg no value is " + reg_no);
//
//                    String vpn_url = "http://govinda.nic.in/manage/api_sync_subdomainstatus.php";
//                    URL obj = new URL(vpn_url);
//                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//                    con.setRequestMethod("POST");
//                    con.setRequestProperty("Content-Type", "application/json");
//                    con.setDoOutput(true);
//                    try (OutputStream os = con.getOutputStream()) {
//                        byte[] input = reg_no.getBytes("utf-8");
//                        os.write(input, 0, input.length);
//                    }
//                    String urlParameters = reg_no;
//                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                    wr.flush();
//                    wr.close();
//                    int responseCode = con.getResponseCode();
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Post parameters : " + urlParameters + " Response Code : " + responseCode);
//
//                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String inputLine;
//                    StringBuffer response = new StringBuffer();
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "API Response ::" + response.toString());
//
//                    result = response.toString();
//
//                } catch (Exception e) {
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains MySQL Connection Exception 2" + e.toString());
//
//                    e.printStackTrace();
//                }
//
//            } else {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " subdomain , newip not found for reg no " + reg);
//            }
//        } else {
//
//            try {
//                con = DbConnection.getSlaveConnection();
//
//                String qry = "SELECT registration_no FROM dns_registration WHERE registration_no = ? ";
//                pst = con.prepareStatement(qry);
//                pst.setString(1, reg);
//
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains query for reject is " + pst);
//
//                rs = pst.executeQuery();
//
//                if (rs.next()) {
//                    subdomain = rs.getString("registration_no");
//                }
//            } catch (Exception e) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains MySQL Connection Exception" + e);
//            }
//
//            if (!subdomain.isEmpty()) {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " subdomain " + subdomain + " , newip " + newip + " found for reg no " + reg);
//
//                try {
//                    //String reg_no = "{\"subdomain\": \"" + subdomain + "\",\"mapped_ip\": \"" + newip + "\",\"status\": \"1\",\"ticket\": \"" + ticket + "\",\"eform_Id\":\"" + reg + "\"}";
//                    // String req_status="approved";
//                    // above line modified by pr on 8thjan2020
//                    String reg_no = "{\"subdomain\": \"" + subdomain + "\",\"mapped_ip\": \"" + newip + "\",\"status\": \"1\",\"ticket\": \"" + ticket + "\",\"eform_Id\":\"" + reg + "\",\"request_type\":\"" + req_for + "\",\"request_status\":\"" + status + "\"}";
//                    //String reg_no = "{\"subdomain\": \"" + subdomain + "\",\"mapped_ip\": \"" + newip + "\",\"status\": \"1\",\"ticket\": \"" + ticket + "\",\"eform_Id\":\"" + reg + "\",\"request_type\":\"" + req_for + "\"}";
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reg no value is " + reg_no);
//
//                    String vpn_url = "http://govinda.nic.in/manage/api_sync_subdomainstatus.php";
//                    URL obj = new URL(vpn_url);
//                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//                    con.setRequestMethod("POST");
//                    con.setRequestProperty("Content-Type", "application/json");
//                    con.setDoOutput(true);
//                    try (OutputStream os = con.getOutputStream()) {
//                        byte[] input = reg_no.getBytes("utf-8");
//                        os.write(input, 0, input.length);
//                    }
//                    String urlParameters = reg_no;
//                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//                    wr.flush();
//                    wr.close();
//                    int responseCode = con.getResponseCode();
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Post parameters : " + urlParameters + " Response Code : " + responseCode);
//
//                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String inputLine;
//                    StringBuffer response = new StringBuffer();
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "API Response ::" + response.toString());
//
//                    result = response.toString();
//
//                } catch (Exception e) {
//
//                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                            + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains MySQL Connection Exception 2" + e.toString());
//
//                    e.printStackTrace();
//                }
//
//            } else {
//                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
//                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " subdomain , newip not found for reg no " + reg);
//            }
//
//        }
//
//        return result;
//    } 
    // above method commented below added by pr on 31stmar2020  
    public String dns_push_domains(String reg, String req_for, String status) {
        String result = "";

        String ticket = "close";

        String subdomain = "", newip = "", cname = ""; // cname added by pr on 12thjun2020
        Set<String> subdomainSet = new HashSet<>();
        Set<String> newipSet = new HashSet<>();
        Set<String> cnameSet = new HashSet<>();

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DbConnection.getSlaveConnection();

//                String qry = "SELECT ip.newip, url.dns_url FROM dns_registration_url url JOIN dns_registration_newip ip ON url.registration_no = ip.registration_no WHERE "
//                        + "url.registration_no = ? ";
            // above query modified by pr on 12thjun2020  
            String qry = "SELECT ip.newip, url.dns_url, cn.cname FROM dns_registration_url url "
                    + "JOIN dns_registration_newip ip ON url.registration_no = ip.registration_no "
                    + "JOIN dns_registration_cname cn ON url.registration_no = cn.registration_no "
                    + "WHERE "
                    + "url.registration_no = ? ";

            pst = con.prepareStatement(qry);
            pst.setString(1, reg);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains query for " + status + " is " + pst);

            rs = pst.executeQuery();

            while (rs.next()) {
                subdomainSet.add(rs.getString("dns_url"));
                newipSet.add(rs.getString("newip"));
                cnameSet.add(rs.getString("cname"));
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains MySQL Connection Exception" + e);
        }

        //if (!subdomain.isEmpty() && !newip.isEmpty()) {
        for (String cnames : cnameSet) {
            cname += cnames + ";";
        }

        cname = cname.replaceAll(";$", "");

        for (String newips : newipSet) {
            newip += newips + ";";
        }

        newip = newip.replaceAll(";$", "");

        for (String subdomains : subdomainSet) {
            subdomain += subdomains + ";";
        }

        subdomain = subdomain.replaceAll(";$", "");

        try {
            //String reg_no = "{\"subdomain\": \"" + subdomain + "\",\"mapped_ip\": \"" + newip + "\",\"status\": \"1\",\"ticket\": \"" + ticket + "\",\"eform_Id\":\"" + reg + "\",\"request_type\":\"" + req_for + "\",\"request_status\":\"" + status + "\"}";

            String reg_no = "{\"subdomain\": \"" + subdomain + "\",\"mapped_ip\": \"" + newip + "\",\"cname\": \"" + cname + "\",\"status\": \"1\",\"ticket\": \"" + ticket + "\",\"eform_Id\":\"" + reg + "\",\"request_type\":\"" + req_for + "\",\"request_status\":\"" + status + "\"}";

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " reg no value is " + reg_no);

            String vpn_url = "https://govinda.nic.in/manage/api_sync_subdomainstatus.php";
            URL obj = new URL(vpn_url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = reg_no.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            String urlParameters = reg_no;
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "Post parameters : " + urlParameters + " Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "API Response ::" + response.toString());

            result = response.toString();

        } catch (Exception e) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains MySQL Connection Exception 2" + e.toString());

            e.printStackTrace();
        }

        return result;
    }

    public String fetchCAEmail(String ca_id) {
        PreparedStatement ps = null;
        //Connection con = null;

        ResultSet rs = null;

        String email = "";

        try {
            con = DbConnection.getSlaveConnection();
//            con = DbConnection.getConnection();
            String qry = "  SELECT ca_email FROM comp_auth WHERE ca_id = ? ";
            ps = con.prepareStatement(qry);
            ps.setString(1, ca_id);
            rs = ps.executeQuery();
            while (rs.next()) {
                email = rs.getString("ca_email");
            }
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "  get status reg no search results fetchCAEmail " + e.getMessage());
            e.printStackTrace();
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

        return email;

    }

    // below function added by pr on 26thjul18 
    public static String getDomainURLIP(String domain) {
        String ip = "";

        try {

            InetAddress inetHost = InetAddress.getByName(domain);
            String hostName = inetHost.getHostName();

            ip = inetHost.getHostAddress();

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == "
                    + "  The host name was: " + hostName + " The hosts IP address is: " + inetHost.getHostAddress());

        } catch (UnknownHostException ex) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == Unrecognized host");

        }

        return ip;

    }

}
