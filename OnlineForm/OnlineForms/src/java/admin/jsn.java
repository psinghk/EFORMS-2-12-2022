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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
public class jsn {

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
        boolean flag = false;

        ResultSet rs1 = null;
        //Connection con = null;
        try {
            con = DbConnection.getSlaveConnection();
            condb = DbConnection.getConnection();

            String qry = "select registration_no,auth_email,auth_off_name,designation, address, city, add_state, pin, ophone, rphone, mobile,userip, datetime, hod_email,hod_mobile,hod_name, hod_telephone, ca_desig, employment,ministry,department,other_dept,state,organization,emp_code, req_for, req_other_record, server_location from dns_registration WHERE registration_no = ?";
            pst = con.prepareStatement(qry);
            pst.setString(1, registration_no);
            System.out.println(" sql query is " + pst);
            rs = pst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            String requestTypeFromBaseTable = "", requestOtherAddFromBaseTable = "", server_locationFromBaseTable = "";
            HashMap<Object, Object> hm = new HashMap<Object, Object>();
            //HashMap<Object, Object> hm2 = new HashMap<Object, Object>();

            if (rs.next()) {

                int numColumns = rsmd.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String column_name = rsmd.getColumnName(i);
                    hm.put(column_name, rs.getObject(column_name)); // line added by pr on 13thjun18                               
                }
                if (hm.containsKey("req_for")) {
                    hm.remove("req_for");
                }
                if (hm.containsKey("req_other_record")) {
                    hm.remove("req_other_record");
                }
                if (rs.getString("req_for") != null && !rs.getString("req_for").isEmpty()) {
                    requestTypeFromBaseTable = rs.getString("req_for").toLowerCase().trim();
                } else {
                    requestTypeFromBaseTable = "req_new";
                }
                if (rs.getString("req_other_record") != null && !rs.getString("req_other_record").isEmpty()) {
                    requestOtherAddFromBaseTable = rs.getString("req_other_record").trim();
                } else {
                    requestOtherAddFromBaseTable = "AAAA";
                }

                if (rs.getString("server_location") != null && !rs.getString("server_location").isEmpty()) {
                    server_locationFromBaseTable = rs.getString("server_location").trim();
                } else {
                    server_locationFromBaseTable = "";
                }

                String dnsRegister = rs.getString("registration_no"); // Entry for registration number
                String requestType = "", requestOtherAdd = "";
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dnsRegister:::" + dnsRegister);
                ArrayList<String> arrlist_cname = new ArrayList<String>(); // line added by pr on 13thjun18                               
                ArrayList<String> arrlist_newip = new ArrayList<String>(); // line added by pr on 13thjun18                               
                ArrayList<String> arrlist_oldip = new ArrayList<String>(); // line added by pr on 13thjun18                               
                ArrayList<String> arrlist_dnsurl = new ArrayList<String>(); // line added by pr on 13thjun18      

                validation validObj = new validation(); // line added by pr on 26thjul18
                pst = con.prepareStatement("select newip from dns_registration_newip where registration_no = ?");// modified by pr on 7thjul2020
                pst.setString(1, dnsRegister);
                rs1 = pst.executeQuery();
                String invalidIPButValidURL = ""; // line added by pr on 26thjul18

                while (rs1.next()) {
                    //arrlist_newip.add(rs1.getString(2));  // line added by pr on 13thjun18                               
                    // above line modified with the below code by pr on 26thjul18
                    // newIP can be URL also for s3waas... hence, below code is written to check the validation of both
                    if (rs1.getString("newip") != null && !validObj.dnsipvalidation(rs1.getString("newip").trim())) // if the ip is valid then it should go to the newip
                    {
                        System.out.println(" inside newip dnsip validation true ");
                        arrlist_newip.add(rs1.getString("newip"));  // line added by pr on 13thjun18
                    } else if (rs1.getString("newip") != null && !validObj.dnsurlvalidation(rs1.getString("newip").trim()))// if the ip is invalid means it is a domain name then it should be stored in a variable to be used in dnsurl or cname
                    {
                        // Came here because newIP validation failed
                        System.out.println(" inside invalid new ip but valid URL ");
                        arrlist_newip.add(getDomainURLIP(rs1.getString("newip").trim()));  // this will nslookup the domain and returns the url
                        invalidIPButValidURL = rs1.getString("newip");
                    }
                }
                //hm.put("newip", arrlist_newip); // line added by pr on 13thjun18                               

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

                //hm.put("dnsurl", arrlist_dnsurl); // line added by pr on 13thjun18                                            
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
                //hm.put("cname", arrlist_cname); // line added by pr on 13thjun18                               
                pst = con.prepareStatement("select oldip from dns_registration_oldip where registration_no = ?");
                pst.setString(1, dnsRegister);
                rs1 = pst.executeQuery();

                while (rs1.next()) {
                    arrlist_oldip.add(rs1.getString("oldip"));  // line added by pr on 13thjun18                               
                }

                //hm.put("oldip", arrlist_oldip); // line added by pr on 13thjun18                               
                qry = "SELECT id, request_type,req_other_add FROM `dns_bulk_campaigns` where discard_status=0 and registration_no=?";
                pst = con.prepareStatement(qry);
                pst.setString(1, registration_no);
                System.out.println(" jsn query is " + pst);
                rs = pst.executeQuery();

                Integer campaingnId = -1;
                if (rs.next()) {
                    //Request Type means new, modify or delete
                    requestType = rs.getString("request_type").toLowerCase().trim();
                    // requestOtherAdd means which record it is .... by default record will be AAAA
                    requestOtherAdd = rs.getString("req_other_add").toLowerCase().trim();
                    campaingnId = rs.getInt("id");
                } else {
                    // It came here because, this request was submitted through API call either through s3waas or registry.gov.in
                    // Here, request Type we can find in dns_registration table
                    // request other add could also be found from there itself
                    requestType = requestTypeFromBaseTable;
                    requestOtherAdd = requestOtherAddFromBaseTable;
                    flag = true;
                }

                /*
                    In all these cases, There can be any record type... So, in all the below 3 cases, code should be written for all these cases....
                For AAAA : table is dns_bulk_upload
                For TXT : table is dns_bulk_txt and so on.. 
                
                So you need to 
                **/
                if (requestType.equalsIgnoreCase("req_modify")) {
                    hm.put("requestType", "req_update");
                } else {
                    hm.put("requestType", requestType);
                }

                List<Object> newList = new ArrayList<>();
                String sql5 = "", column_name = "", old_column_name = "";
                requestOtherAdd = requestOtherAdd.toLowerCase();

                if (requestOtherAdd == null) {
                    requestOtherAdd = "aaaa";
                } else if (requestOtherAdd.isEmpty()) {
                    requestOtherAdd = "aaaa";
                }
                hm.put("recordType", requestOtherAdd.toUpperCase());
                if (!flag) {
                    switch (requestOtherAdd) {
                        case "cname":
                        case "dmarc":
                        case "mx":
                        case "ptr":
                        case "spf":
                        case "srv":
                        case "txt":
                            sql5 = "select * from dns_bulk_" + requestOtherAdd + " where registration_no = ? and error_status=0 and delete_status=0";
                            column_name = requestOtherAdd;
                            old_column_name = "old_" + requestOtherAdd;

                            pst = condb.prepareStatement(sql5);
                            pst.setString(1, dnsRegister);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for other record addition txt: " + pst);
                            rs1 = pst.executeQuery();
                            while (rs1.next()) {

                                Map<String, String> tmp = new HashMap<>();
                                tmp.put("domain", (rs1.getString("domain")));
                                if (rs1.getString(column_name) != null && !rs1.getString(column_name).toString().isEmpty()) {
                                    tmp.put(column_name, (rs1.getString(column_name)));
                                } else {
                                    tmp.put(column_name, column_name + " not filled by user");
                                }
                                if (rs1.getString(old_column_name) != null && !rs1.getString(old_column_name).toString().isEmpty()) {
                                    tmp.put(old_column_name, (rs1.getString(old_column_name)));
                                } else {
                                    tmp.put(old_column_name, old_column_name + " not filled by user");
                                }

                                if (rs1.getString("location") != null && !rs1.getString("location").toString().isEmpty()) {
                                    tmp.put("location", (rs1.getString("location")));
                                } else {
                                    tmp.put("location", "location" + " not filled by user");
                                }
                                newList.add(tmp);
                            }
                            break;
                        default:
                            sql5 = "select * from dns_bulk_upload where registration_no = ? and error_status=0 and delete_status=0";
                            pst = condb.prepareStatement(sql5);
                            pst.setString(1, dnsRegister);
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for other record addition txt: " + pst);
                            rs1 = pst.executeQuery();
                            while (rs1.next()) {

                                Map<String, String> tmp = new HashMap<>();
                                tmp.put("domain", (rs1.getString("domain")));
                                if (rs1.getString("cname") != null && !rs1.getString("cname").toString().isEmpty()) {
                                    tmp.put("cname", (rs1.getString("cname")));
                                } else {
                                    tmp.put("cname", "cname" + " not filled by user");
                                }

                                if (rs1.getString("old_ip") != null && !rs1.getString("old_ip").toString().isEmpty()) {
                                    tmp.put("old_ip", (rs1.getString("old_ip")));
                                } else {
                                    tmp.put("old_ip", "old_ip" + " not filled by user");
                                }

                                if (rs1.getString("new_ip") != null && !rs1.getString("new_ip").toString().isEmpty()) {
                                    tmp.put("new_ip", (rs1.getString("new_ip")));
                                } else {
                                    tmp.put("new_ip", "new_ip" + " not filled by user");
                                }
                                if (rs1.getString("location") != null && !rs1.getString("location").toString().isEmpty()) {
                                    tmp.put("location", (rs1.getString("location")));
                                } else {
                                    tmp.put("location", "location" + " not filled by user");
                                }
                                newList.add(tmp);
                            }
                            break;
                    }

                    hm.put("dnsData", newList);
                    System.out.println("newList for request_type new:::::::" + newList);

                    if (requestType.equals("req_new")) {
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
                    } else if (requestType.equals("req_delete")) {
                        System.out.println("arrlist_newip.size()" + arrlist_newip.size());
                        for (int j = 0; j < arrlist_newip.size(); j++) {
                            String sql = "delete from domain_ip where dns_url =? and ip=?";
                            pst = condb.prepareStatement(sql);
                            pst.setString(1, arrlist_dnsurl.get(j).toString());
                            pst.setString(2, arrlist_newip.get(j).toString());
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==delete query for delete request " + "e: " + pst);
                            pst.executeUpdate();
                        }
                    } else if (requestType.equals("req_modify")) {
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
                } else {
                    // We have to add 4 lists what we have recovered from tables 
//                    Map<String, Object> tmp = new HashMap<>();    // commented by rahul 21-02-2022
//                   hm.put("recordType", requestOtherAdd);
//                    tmp.put("domain", arrlist_dnsurl);
//                    tmp.put("cname", arrlist_cname);
//                    tmp.put("new_ip", arrlist_newip);
//                    tmp.put("old_ip", arrlist_oldip);
//                    tmp.put("location", server_locationFromBaseTable);
//                    newList.add(tmp);

                    String domain = "";                      // added by ashwini sir feb2022
                    String cname = "Not filled by user";
                    String oldIp = "Not filled by user";
                    String location = "Not filled by user";
                    String newIp = "Not filled by user";
                    if (arrlist_dnsurl.size() > 0) {
                        domain = arrlist_dnsurl.get(0);
                        if (arrlist_cname != null && arrlist_cname.size() > 0) {
                            cname = String.join(", ", arrlist_cname);
                        }
                        if (arrlist_oldip != null && arrlist_oldip.size() > 0) {
                            oldIp = String.join(", ", arrlist_oldip);
                        }
                        if (server_locationFromBaseTable != null && !server_locationFromBaseTable.isEmpty()) {
                            location = server_locationFromBaseTable;
                        }
                        if (arrlist_newip != null && arrlist_newip.size() > 0) {
                            newIp = String.join(", ", arrlist_newip);
                        }
                    }
                    if (!domain.isEmpty()) {
                        Map<String, Object> tmp = new HashMap<>();
                        tmp.put("cname", cname);
                        tmp.put("domain", domain);
                        tmp.put("new_ip", newIp);
                        tmp.put("old_ip", oldIp);
                        tmp.put("location", location);
                        newList.add(tmp);
                    }

                    hm.put("dnsData", newList);
                    System.out.println("newList for request_type new:::::::" + newList);

                    int sizeOfIps = arrlist_newip.size();
                    int sizeOfUrls = arrlist_dnsurl.size();

                    if (requestType.equals("req_new")) {
                        System.out.println("arrlist_newip.size()" + arrlist_newip.size());

                        for (int j = 0; j < arrlist_newip.size(); j++) {
                            String sql = "insert into domain_ip (dns_url,ip)"
                                    + "values (?, ?)";
                            pst = condb.prepareStatement(sql);
                            pst.setString(1, arrlist_dnsurl.get(0).toString());
                            pst.setString(2, arrlist_newip.get(j).toString());
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==insert query for new request " + "e: " + pst);
                            pst.executeUpdate();
                        }
                    } else if (requestType.equals("req_delete")) {
                        System.out.println("arrlist_newip.size()" + arrlist_newip.size());
                        //for (int j = 0; j < arrlist_newip.size(); j++) {
                        String sql = "delete from domain_ip where dns_url =?";
                        pst = condb.prepareStatement(sql);
                        pst.setString(1, arrlist_dnsurl.get(0).toString());
                        //pst.setString(2, arrlist_newip.get(j).toString());
                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " ==delete query for delete request " + "e: " + pst);
                        pst.executeUpdate();
                        //}
                    } else if (requestType.equals("req_modify")) {
                        String sql = "delete from domain_ip where dns_url =?";
                        pst = condb.prepareStatement(sql);
                        pst.setString(1, arrlist_dnsurl.get(0).toString());
                        pst.executeUpdate();
                        for (int j = 0; j < arrlist_newip.size(); j++) {
                            String update = "insert into domain_ip (dns_url,ip) values (?, ?)";
                            pst = con.prepareStatement(update);
                            pst.setString(1, arrlist_newip.get(j).toString());
                            pst.setString(2, arrlist_dnsurl.get(0).toString());
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for update domain ip: " + pst);
                            pst.executeUpdate();
                        }
                    }
                }

                //}
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

            String json = new Gson().toJson(hm);

            String jsonRequest = "[" + json + "]"; // line modified by pr on 20thjun18

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == " + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "************ gson formed is " + jsonRequest);

            try {
                // below code to be uncommented in production done for testing on 26thjul18

                doTrustToCertificates();

                //URL url = new URL("https://personnel.nic.in/webservice_ldap/consumejson.php"); //commented by rahul 16-feb-2022
                URL url = new URL("https://personnel.nic.in/webservice_ldap/dnsdatapush.php");
                //URL url = new URL("");

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
            e.printStackTrace();
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

            // above query modified by pr on 12thjun2020  
//            String qry = "SELECT ip.newip, url.dns_url, cn.cname FROM dns_registration_url url "
//                    + "JOIN dns_registration_newip ip ON url.registration_no = ip.registration_no "
//                    + "JOIN dns_registration_cname cn ON url.registration_no = cn.registration_no "
//                    + "WHERE "
//                    + "url.registration_no = ? ";
            String qry = "SELECT dns_url FROM dns_registration_url where registration_no=?";
            pst = con.prepareStatement(qry);
            pst.setString(1, reg);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains query for URLS " + status + " is " + pst);

            rs = pst.executeQuery();

            while (rs.next()) {
                subdomainSet.add(rs.getString("dns_url"));
            }

            qry = "SELECT newip FROM dns_registration_newip where registration_no=?";
            pst = con.prepareStatement(qry);
            pst.setString(1, reg);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains query for IPs " + status + " is " + pst);

            rs = pst.executeQuery();

            while (rs.next()) {
                newipSet.add(rs.getString("newip"));
            }

            qry = "SELECT cname FROM dns_registration_cname where registration_no=?";
            pst = con.prepareStatement(qry);
            pst.setString(1, reg);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + "dns_push_domains query for CNAMES " + status + " is " + pst);

            rs = pst.executeQuery();

            while (rs.next()) {
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
