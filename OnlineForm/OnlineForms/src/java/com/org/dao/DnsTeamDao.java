/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import com.org.bean.DnsTeamBean;
import com.org.bean.DnsTeamBeanValidation;
import com.org.connections.DbConnection;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Avinash
 */
public class DnsTeamDao {

    Connection con = null;
    Connection conSlave = null;

    public DnsTeamDao() {
        try {
            conSlave = DbConnection.getSlaveConnection();
            con = DbConnection.getConnection();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DnsTeamDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String fetchDnsTeamData(HttpServletRequest request) {
        String[] cols = {"id", "domain", "ip", "applicant_name", "applicant_email", "applicant_mobile"};
        String table = "dns_team_data";

        /* Response will be a String of JSONObject type */
        JSONObject result = new JSONObject();

        /* JSON Array to store each row of the data table */
        JSONArray array = new JSONArray();

        int amount = 5;
        /* Amount in Show Entry drop down */
        int start = 0;
        /* Counter for Paging, initially 0 */
        int echo = 0;
        /* Maintains the request number, initially 0 */
        int col = 0;
        /* Column number in the datatable */

        String id = "";
        String name = "";
        String domain = "";
        String ip = "";
        String mobile = "";
        String email = "";
        /* Below variables store the options to create the Query */
        String dir = "desc";
        String sStart = request.getParameter("iDisplayStart");
        String sAmount = request.getParameter("iDisplayLength");
        String sEcho = request.getParameter("sEcho");
        String sCol = request.getParameter("iSortCol_0");
        String sdir = request.getParameter("sSortDir_0");

        /* Below will be used when Search per column is used. In this example we are using common search. */
        id = request.getParameter("sSearch_0");
        domain = request.getParameter("sSearch_1");
        ip = request.getParameter("sSearch_2");
        email = request.getParameter("sSearch_3");
        name = request.getParameter("sSearch_4");
        mobile = request.getParameter("sSearch_5");

        List<String> sArray = new ArrayList<String>();
        if (id != null && !id.equals("")) {
            String sId = " id like '%" + id + "%'";
            sArray.add(sId);
        }
        if (name != null && !name.isEmpty()) {
            String sName = " applicant_name like '%" + name + "%'";
            sArray.add(sName);
        }
        if (email != null && !email.isEmpty()) {
            String sEmail = " applicant_email like '%" + email + "%'";
            sArray.add(sEmail);
        }
        if (mobile != null && !mobile.isEmpty()) {
            String sMobile = " applicant_mobile like '%" + mobile + "%'";
            sArray.add(sMobile);
        }
        if (domain != null && !domain.isEmpty()) {
            String sDomain = " applicant_domain like '%" + domain + "%'";
            sArray.add(sDomain);
        }
        if (ip != null && !ip.isEmpty()) {
            String sIp = " applicant_ip like '%" + ip + "%'";
            sArray.add(sIp);
        }

        String individualSearch = "";
        if (sArray.size() == 1) {
            individualSearch = sArray.get(0);
        } else if (sArray.size() > 1) {
            for (int i = 0; i < sArray.size() - 1; i++) {
                individualSearch += sArray.get(i) + " and ";
            }
            individualSearch += sArray.get(sArray.size() - 1);
        }

        /* Start value from which the records need to be fetched */
        if (sStart != null) {
            start = Integer.parseInt(sStart);
            if (start < 0) {
                start = 0;
            }
        }

        /* Total number of records to be fetched */
        if (sAmount != null) {
            amount = Integer.parseInt(sAmount);
            if (amount < 5 || amount > 100) {
                amount = 5;
            }
        }


        /* Counter of the request sent from Data table */
        if (sEcho != null) {
            echo = Integer.parseInt(sEcho);
        }

        /* Column number */
        if (sCol != null) {
            col = Integer.parseInt(sCol);
            if (col < 0 || col > 5) {
                col = 0;
            }
        }

        /* Sorting order */
        if (sdir != null) {
            if (!sdir.equals("asc")) {
                dir = "desc";
            }
        }
        String colName = cols[col];

        /* This is show the total count of records in Data base table */
        int total = 0;
        try {
            String sql = "SELECT count(*) FROM " + table+" WHERE delete_status = 0";
            PreparedStatement ps = conSlave.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt("count(*)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* This is total number of records that is available for the specific search query */
        int totalAfterFilter = total;

        try {
            String searchSQL = "";
            String sql = "SELECT * FROM " + table+" WHERE delete_status = 0";
            String searchTerm = request.getParameter("sSearch");
            String globeSearch = " and (id like '%" + searchTerm + "%'"
                    + " or applicant_name like '%" + searchTerm + "%'"
                    + " or applicant_email like '%" + searchTerm + "%'"
                    + " or applicant_mobile like '%" + searchTerm + "%'"
                    + " or ip like '%" + searchTerm + "%'"
                    + " or domain like '%" + searchTerm + "%')";
            if (searchTerm != "" && individualSearch != "") {
                searchSQL = globeSearch + " and " + individualSearch;
            } else if (individualSearch != "") {
                searchSQL = " where " + individualSearch;
            } else if (searchTerm != "") {
                searchSQL = globeSearch;
            }
            sql += searchSQL;
            sql += " order by " + colName + " " + dir;
            sql += " limit " + start + ", " + amount;

            PreparedStatement ps = conSlave.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JSONArray ja = new JSONArray();
                int ids = Integer.parseInt(rs.getString("id"));
                ja.put(rs.getString("id"));
                ja.put(rs.getString("domain"));
                ja.put(rs.getString("ip"));
                ja.put(rs.getString("applicant_name"));
                ja.put(rs.getString("applicant_email"));
                ja.put(rs.getString("applicant_mobile"));
                ja.put("<span class='btn btn-warning mr-2' onclick='dsnRecordEdit("+ids+")'>Edit</span><span class='btn btn-danger' onclick='dsnRecordDelete("+ids+")'>Delete</span>");
                array.put(ja);
            }
            String sql2 = "SELECT count(*) FROM " + table+" WHERE delete_status = 0";
            if (searchTerm != "") {
                sql2 += searchSQL;
                PreparedStatement ps2 = conSlave.prepareStatement(sql2);
                ResultSet rs2 = ps2.executeQuery();
                if (rs2.next()) {
                    totalAfterFilter = rs2.getInt("count(*)");
                }
            }
            result.put("iTotalRecords", total);
            result.put("iTotalDisplayRecords", totalAfterFilter);
            result.put("aaData", array);
            result.put("sEcho", echo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
        //return "dskjns";
    }

    public DnsTeamBean fetchDnsTeamData(String id) {
        String table = "dns_team_data";
        DnsTeamBean dnsTeamBean = new DnsTeamBean();
        try {
            String sql = "SELECT * FROM " + table+" WHERE id=? and delete_status = 0";
            PreparedStatement ps = conSlave.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dnsTeamBean.setDomain(rs.getString("domain"));
                dnsTeamBean.setIp(rs.getString("ip"));
                dnsTeamBean.setEmail(rs.getString("applicant_email"));
                dnsTeamBean.setMobile(rs.getString("applicant_mobile"));
                dnsTeamBean.setName(rs.getString("applicant_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return dnsTeamBean;
    }
    
    public boolean domainExistsInTeamData(String domain) {
        boolean flag = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String qry = "select count(*) from dns_team_data where domain=?";
            ps = conSlave.prepareStatement(qry);
            ps.setString(1, domain);
            rs = ps.executeQuery();
            if(rs.next()) {
                if(rs.getInt(1) > 0) {
                    flag = true;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public String addRecordInTeamData(DnsTeamBeanValidation dnsTrckerFrmData) {
        System.out.println("com.org.controller.DnsTeamController.dnsTrckerFrmDataPost()::: "+dnsTrckerFrmData.getDomain());
        String flag = "";
        PreparedStatement ps = null;
        try {
            String ReturnString = "";
            try {
                InetAddress yes = java.net.InetAddress.getByName(dnsTrckerFrmData.getDomain());
                ReturnString = yes.getHostAddress();
                System.out.println("ReturnString in fetch domain" + ReturnString);
                if(!ReturnString.equals("")) {
                    int i = 0;
                    String qry = "INSERT INTO `dns_team_data` (`domain`, `ip`, `applicant_email`, `applicant_name`, `applicant_mobile`) VALUES (?, ?, ?, ?, ?);";
                    ps = con.prepareStatement(qry);
                    ps.setString(1, dnsTrckerFrmData.getDomain());
                    ps.setString(2, dnsTrckerFrmData.getIp());
                    ps.setString(3, dnsTrckerFrmData.getEmail());
                    ps.setString(4, dnsTrckerFrmData.getName());
                    ps.setString(5, dnsTrckerFrmData.getMobile());
                    i = ps.executeUpdate();
                    if(i > 0) {
                        flag = "Inserted";
                    }
                }
            } catch (Exception ex) {
                flag = "Domain ("+dnsTrckerFrmData.getDomain()+") does not exists in nsLookup, so could not able to add this domain.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public List<Object> fetchRecordInTeamData(int dnsTeamId) {
        //DnsTeamBean dnsTeamBeans = new DnsTeamBean();
        List<Object> dataCollection = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            int i = 0;
            String qry = "SELECT * FROM dns_team_data WHERE id = ?";
            ps = conSlave.prepareStatement(qry);
            ps.setInt(1, dnsTeamId);
            rs = ps.executeQuery();
            if(rs.next()) {
                Map<String,String> dataCol = new HashMap<>();
                dataCol.put("domain", rs.getString("domain"));
                dataCol.put("email", rs.getString("applicant_email"));
                dataCol.put("ip", rs.getString("ip"));
                dataCol.put("dnsteamid", rs.getString("id"));
                dataCol.put("name", rs.getString("applicant_name"));
                dataCol.put("mobile", rs.getString("applicant_mobile"));
                dataCollection.add(dataCol);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataCollection;
    }

    public String updateRecordInTeamData(DnsTeamBeanValidation dnsTrckerFrmData) {
        System.out.println("com.org.controller.DnsTeamController.dnsTrckerFrmDataPost()::: "+dnsTrckerFrmData.getDomain());
        String flag = "";
        PreparedStatement ps = null;
        try {
            int i = 0;
            String qry = "UPDATE `dns_team_data` SET `ip` = ?, `applicant_email` = ?, `applicant_name` = ?, `applicant_mobile` = ? WHERE id=?";
            ps = con.prepareStatement(qry);
            ps.setString(1, dnsTrckerFrmData.getIp());
            ps.setString(2, dnsTrckerFrmData.getEmail());
            ps.setString(3, dnsTrckerFrmData.getName());
            ps.setString(4, dnsTrckerFrmData.getMobile());
            ps.setInt(5, dnsTrckerFrmData.getId());
            i = ps.executeUpdate();
            if(i > 0) {
                updateMobileNumber(dnsTrckerFrmData.getEmail(), dnsTrckerFrmData.getMobile());
                flag = "Updated";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public String deleetRecordInTeamData(int dnsTeamId) {
        String flag = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT domain FROM dns_team_data WHERE id = ?";
            ps = con.prepareCall(sql);
            ps.setInt(1, dnsTeamId);
            rs = ps.executeQuery();
            if(rs.next()) {
                int counter = 0;
                String url = rs.getString("domain");
                String ReturnString = "";
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                String sql1 = "SELECT count(domain) FROM dns_team_data WHERE domain = ?";
                ps1 = conSlave.prepareStatement(sql1);
                ps1.setString(1, url);
                rs1 = ps1.executeQuery();
                if(rs1.next()) {
                    counter = rs1.getInt(1);
                }
                if(counter == 1) {
                    try {
                        InetAddress yes = java.net.InetAddress.getByName(url);
                        ReturnString = yes.getHostAddress();
                        System.out.println("ReturnString in fetch domain" + ReturnString);
                        flag = "Dmain ("+url+") could not deleted! Please Try again.";
                    } catch (Exception ex) {
                        ReturnString = "";
                        flag = delete_url_func(url, dnsTeamId);
                    }
                } else {
                    flag = delete_url_func(url, dnsTeamId);
                }
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("com.org.dao.DnsTeamDao.deleetRecordInTeamData() FLAG::::: "+flag);
        return flag;
    }
    
    public String delete_url_func(String url, int dnsTeamId) {
        String flag = "";
        try {
            int i = 0;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "UPDATE `dns_team_data` SET `delete_status` = '1' WHERE `dns_team_data`.`id` = ?";
            ps = con.prepareCall(sql);
            ps.setInt(1, dnsTeamId);
            i = ps.executeUpdate();
            if (i > 0) {
                flag = "Dmain ("+url+") has been deleted Successfully!";
            } else {
                flag = "Dmain ("+url+") could not deleted! Please Try again.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean findValidUser(String aliases) {
        System.out.println("com.org.dao.DnsTeamDao.findValidUser() Aliases:::: "+aliases);
        boolean flag = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT count(*) FROM domain_tracker WHERE authorize_email IN("+aliases+")";
            ps = conSlave.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()) {
                if(rs.getInt(1) > 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    private void updateMobileNumber(String email, String mobile) {
        PreparedStatement ps = null;
        try {
            String qry = "UPDATE `dns_team_data` SET `applicant_mobile` = ? WHERE `applicant_email` = ?";
            ps = con.prepareStatement(qry);
            ps.setString(1, mobile);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
