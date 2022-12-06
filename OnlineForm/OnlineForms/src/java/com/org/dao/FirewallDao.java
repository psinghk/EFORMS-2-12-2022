package com.org.dao;

import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

public class FirewallDao {

    private Connection con = null;
    private Connection conSlave = null;
    String ip = ServletActionContext.getRequest().getRemoteAddr();

    public void Firewall_tab1(List<FormBean> valueObj, String purpose) {
        String sourceip = "";
        String destinationip = "";
        String service = "";
        String ports = "";
        String action = "";
        String timeperiod = "";
        for (FormBean model : valueObj) {

            sourceip = sourceip + model.getSourceip().concat(",");
            destinationip = destinationip + model.getDestinationip().concat(",");
            service = service + model.getService().concat(",");
            ports = ports + model.getPorts().concat(",");
            action = action + model.getAction().concat(",");
            timeperiod = timeperiod + model.getTimeperiod().concat(",");
        }

        try {
            con = DbConnection.getConnection();
            // this.con = MyConnection.getConnection();

            PreparedStatement pst = null;
            sourceip = sourceip.substring(0, sourceip.length() - 1);
            destinationip = destinationip.substring(0, destinationip.length() - 1);
            service = service.substring(0, service.length() - 1);
            ports = ports.substring(0, ports.length() - 1);
            action = action.substring(0, action.length() - 1);
            timeperiod = timeperiod.substring(0, timeperiod.length() - 1);
            String sql = "insert into onlineform_temp_db.firewall_entries(sourceIP,destinationIP,service,ports,action,timePeriod,purpose)values (?,?,?,?,?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, sourceip);
            pst.setString(2, destinationip);
            pst.setString(3, service);
            pst.setString(4, ports);
            pst.setString(5, action);
            pst.setString(6, timeperiod);
            pst.setString(7, purpose);
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == Firewall dao tab 1: " + pst);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == exception in Firewall DAO tab 1: " + e.getMessage());
        }
    }

    public String Firewall_tab2(FormBean form_details, Map profile_values) {
        System.out.println("in dao tab2");
        String dbrefno = "", newref = "";
        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Boolean errflag = Boolean.valueOf(false);
        try {
            //this.con = MyConnection.getConnection();
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
            String search_regnum = "select registration_no from centralutm_registration where date(datetime)=date(now()) group by registration_no desc limit 1";
            System.out.println("search num:::::" + search_regnum);
            pst = conSlave.prepareStatement(search_regnum);

            rs1 = pst.executeQuery();
            if (rs1.next()) {
                System.out.println("if get reg no");
                dbrefno = rs1.getString("registration_no");
            }
            if (dbrefno == null || dbrefno.equals("")) {
                System.out.println("if reg no null");
                int newrefno = 1;
                newref = "000" + newrefno;
            } else {

                System.out.println("else ");
                String lastst = dbrefno.substring(24, dbrefno.length());
                System.out.println("lastst" + lastst);
                int last = Integer.parseInt(lastst);
                int newrefno = last + 1;
                int len = Integer.toString(newrefno).length();
                if (len == 1) {
                    newref = "000" + newrefno;
                } else if (len == 2) {
                    newref = "00" + newrefno;
                } else if (len == 3) {
                    newref = "0" + newrefno;
                }

                System.out.println("else newref:::::" + newref);
            }
            //13,15
            newref = "CENTRALUTM-FORM" + pdate1 + newref;
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == REF NUMBER DNS dao tab 2: " + newref);

            String sql = "insert into centralutm_registration (auth_email,mobile,auth_off_name,ophone,rphone,designation,emp_code,address,city,add_state,pin,employment,ministry,department,other_dept,state,organization,hod_name,hod_email,hod_mobile,hod_telephone,registration_no,ca_desig,purpose) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            pst = con.prepareStatement(sql);
            pst.setString(1, profile_values.get("email").toString());
            pst.setString(2, profile_values.get("mobile").toString());
            pst.setString(3, profile_values.get("cn").toString());
            pst.setString(4, profile_values.get("ophone").toString());
            pst.setString(5, profile_values.get("rphone").toString());
            pst.setString(6, profile_values.get("desig").toString());
            pst.setString(7, form_details.getUser_empcode());
            pst.setString(8, profile_values.get("postalAddress").toString());
            pst.setString(9, profile_values.get("nicCity").toString());
            pst.setString(10, profile_values.get("state").toString());
            pst.setString(11, profile_values.get("pin").toString());
            pst.setString(12, form_details.getUser_employment().trim());
            if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                pst.setString(13, form_details.getMin().trim());
                pst.setString(14, form_details.getDept().trim());
                pst.setString(15, form_details.getOther_dept().trim());
                pst.setString(16, "");
                pst.setString(17, "");
            } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                pst.setString(13, "");
                pst.setString(14, "");
                pst.setString(15, form_details.getOther_dept().trim());
                pst.setString(16, "");
                pst.setString(17, form_details.getOrg().trim());
            } else {
                pst.setString(13, "");
                pst.setString(14, form_details.getState_dept().trim());
                pst.setString(15, form_details.getOther_dept().trim());
                pst.setString(16, form_details.getStateCode().trim());
                pst.setString(17, "");
            }
            pst.setString(18, form_details.getHod_name().trim());
            pst.setString(19, form_details.getHod_email().trim());
            pst.setString(20, form_details.getHod_mobile().trim());
            pst.setString(21, form_details.getHod_tel().trim());
            pst.setString(22, newref);
            pst.setString(23, form_details.getCa_design().trim());
            pst.setString(24, form_details.getRemarks().trim());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == Firewall dao tab 2: " + pst);

            int i = pst.executeUpdate();
            pst.close();

            if (i > 0) {
                ArrayList SourceIP = new ArrayList(Arrays.asList(form_details.getSourceip().split(";")));
                ArrayList DestinationIP = new ArrayList(Arrays.asList(form_details.getDestinationip().split(";")));
                ArrayList Service = new ArrayList(Arrays.asList(form_details.getService().split(";")));
                ArrayList Port = new ArrayList(Arrays.asList(form_details.getPorts().split(";")));
                ArrayList Action = new ArrayList(Arrays.asList(form_details.getAction().split(";")));
                ArrayList Timeperiod = new ArrayList(Arrays.asList(form_details.getTimeperiod().split(";")));
                for (int j = 0; j < SourceIP.size(); j++) {
                    pst = con.prepareStatement("insert into firewall_entries (registration_no,sourceIP,destinationIP,service,ports,action,timePeriod) values(?,?,?,?,?,?,?)");
                    pst.setString(1, newref);
                    pst.setString(2, SourceIP.get(j).toString());
                    pst.setString(3, DestinationIP.get(j).toString());
                    pst.setString(4, Service.get(j).toString());
                    if (!Service.get(j).toString().equals("icmp")) {
                        pst.setString(5, Port.get(j).toString());
                    } else {
                        pst.setString(5, "");
                    }
                    pst.setString(6, Action.get(j).toString());
                    pst.setString(7, Timeperiod.get(j).toString());
                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == Firewall dao tab 2: " + pst);
                    pst.executeUpdate();
                }

            }

        } catch (Exception e) {
            errflag = Boolean.valueOf(true);

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == exception in Firewall dao tab 2: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return newref;
    }

    public boolean firewallAllowedUsers(Set<String> aliases) {

        PreparedStatement pst = null;
        ResultSet rs1 = null;
        Boolean errflag = false;
        String commaSeparatedAliases = "";
        for (String email : aliases) {
            commaSeparatedAliases += "'" + email + "',";
        }
        commaSeparatedAliases = commaSeparatedAliases.replaceAll(",$", "");

        try {
            con = DbConnection.getConnection();
            conSlave = DbConnection.getSlaveConnection(); //29dec2021
            String search_regnum = "select email from utm_allowed_users where email in (" + commaSeparatedAliases + ")";
            pst = conSlave.prepareStatement(search_regnum);
            System.out.println("firewallAllowedUsers@@@@@@@@@@@@@@@" + pst);
            rs1 = pst.executeQuery();
            if (rs1.next()) {
                errflag = true;
            }
        } catch (Exception e) {
            errflag = true;

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == exception in Firewall dao tab 2: " + e.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("in dao firewallAllowedUsers errflag" + errflag);
        return errflag.booleanValue();
    }
}
