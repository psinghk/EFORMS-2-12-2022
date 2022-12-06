/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import model.FormBean;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author dhiren
 */
public class UpdateMobileDao {

    String ip = ServletActionContext.getRequest().getRemoteAddr();
    Connection con = null;
    Connection conSlave = null;

    public String UpdateIntoUpdatemobileTable(FormBean form_details) {

        String otp = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        //Connection con = null;
        try {

            String dbrefno = "", newref = "";
            int newrefno;
            Date date1 = new Date();
            DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
            String pdate1 = dateFormat1.format(date1);
           
            con = DbConnection.getConnection();
            String update = "update otp_save_update_mobile set mobile=?,initials=?,fname=?,mname=?,lname=?,ip_address=?,radio_on_behalf=?,reason=?,other_reason=?,relation=?,relation_other=?,on_behalf_email=? where new_mobile =? and email = ? and newcode=? and date_add(updated_time, INTERVAL 30 MINUTE) >= NOW()";
            ps = con.prepareStatement(update);
            ps.setString(1, form_details.getMobile_number());
            ps.setString(2, form_details.getInitials());
            ps.setString(3, form_details.getFname());
            ps.setString(4, form_details.getMname());
            ps.setString(5, form_details.getLname());
            ps.setString(6, ip);
            ps.setString(7, form_details.getRadioOnBehalf());
            ps.setString(8, form_details.getReason());
            ps.setString(9, form_details.getReason_txt());
            ps.setString(10, form_details.getRelationWithOwner());
            ps.setString(11, form_details.getOtherRelation());
            ps.setString(12, form_details.getOn_behalf_email());
            ps.setString(13, form_details.getNew_mobile());
            ps.setString(14, form_details.getUser_email());
            ps.setString(15, form_details.getNewcode());

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ps for resendMobile2: " + ps);
            int i = ps.executeUpdate();
            

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
        return "true";
    }

}
