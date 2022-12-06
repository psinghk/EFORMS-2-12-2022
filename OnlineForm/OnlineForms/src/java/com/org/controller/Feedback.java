/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;
import com.org.connections.DbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Faqs;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.owasp.esapi.ESAPI;

/**
 *
 * @author NIC
 */
public class Feedback extends ActionSupport implements SessionAware {

    Map session;
    private String name, email, msg, imgtxt_feedback,CSRFRandom;
    Map res_msg = null;
    Connection con = null;

    public List<Faqs> faq_list = new ArrayList<>();
    public List list = new ArrayList<>();
    


    public Feedback() {
        if (res_msg == null) {
            res_msg = new HashMap();
        }
//        if (faq_list == null) {
//            faq_list = new ArrayList<>();
//        }
//        if (list == null) {
//            list = new ArrayList<>();
//        }
    }

    public Map getRes_msg() {
        return res_msg;
    }

    public void setRes_msg(Map res_msg) {
        this.res_msg = res_msg;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImgtxt_feedback() {
        return imgtxt_feedback;
    }

    public void setImgtxt_feedback(String imgtxt_feedback) {
        this.imgtxt_feedback = imgtxt_feedback;
    }


    public List<Faqs> getFaq_list() {
        return faq_list;
    }

    public void setFaq_list(List<Faqs> faq_list) {
        this.faq_list = faq_list;

    }
    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = CSRFRandom;

    }

    public String feedback_us() {
        System.out.println("Name: " + getName() + " ::Email:: " + getEmail() + " ::Msg:: " + getMsg() + " ::Captcha:: " + getImgtxt_feedback());
        String capcode = (String) session.get("captcha");
        System.out.println("Captcha Code: " + session.get("captcha"));

        PreparedStatement ps = null;
        try {
            if (!getImgtxt_feedback().equals(capcode)) {
                res_msg.put("cap_error", "Please enter Correct Captcha");
            } else {
                String s = "insert into e_feedback (name, email, feedback) values(?,?,?)";
                con = DbConnection.getConnection();
                ps = con.prepareStatement(s);
                ps.setString(1, getName());
                ps.setString(2, getEmail());
                ps.setString(3, getMsg());
                ps.executeUpdate();

                res_msg.put("cap_succ", "Thank you! We have recived your Feedback.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("kjghadfhgsfhgsdjfgdjsf");
        session.remove("captcha");
        return SUCCESS;
    }
    public String eformsFaqs() {
        System.out.println("com.org.controller.Profile.eformsFaqs():::::::: ");
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String keyword = (String) ServletActionContext.getRequest().getParameter("srch_faq");
        try {
            if (keyword == null || keyword == "") {
                String qry = "SELECT q_category FROM `feedback` GROUP BY q_category";
                con = DbConnection.getSlaveConnection();
                ps = con.prepareStatement(qry);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Faqs faqs = new Faqs();
                    faqs.setE_cate(rs.getString(1));
                    faq_list.add(faqs);
                }
                for (Faqs l : faq_list) {
                    System.out.println("Category::: " + l.getE_cate());
                    String qury = "SELECT * FROM `feedback` WHERE q_category='" + l.getE_cate() + "'";
                    System.out.println("Query is: " + qury);
                    ps = con.prepareStatement(qury);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        Faqs faqs_list = new Faqs();
                        faqs_list.setId(rs.getInt(1));
                        faqs_list.setE_cate(rs.getString(2));
                        faqs_list.setE_question(rs.getString(3));
                        faqs_list.setE_answer(rs.getString(4));
                        list.add(faqs_list);
                    }
                }
            } else {
                eformsfaqSearch(keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }

    public String eformsfaqSearch(String keyword) throws SQLException, ClassNotFoundException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String qry = "SELECT q_category FROM `feedback` where question like '%" + keyword + "%' or answer like '%" + keyword + "%' GROUP BY q_category";
        con = DbConnection.getSlaveConnection();
        ps = con.prepareStatement(qry);
        rs = ps.executeQuery();
        int count = 0;
        while (rs.next()) {
            Faqs faqs = new Faqs();
            faqs.setE_cate(rs.getString(1));
            faq_list.add(faqs);
            System.out.println("Category::: " + rs.getString(1));
            String qury = "SELECT * FROM `feedback` where question like '%" + keyword + "%' or answer like '%" + keyword + "%' and q_category='" + rs.getString(1) + "' ";
            System.out.println("Query is: " + qury);
            ps = con.prepareStatement(qury);
            ResultSet rs1 = ps.executeQuery();

            while (rs1.next()) {
                count++;
                Faqs faqs_list = new Faqs();
                faqs_list.setId(rs1.getInt(1));
                faqs_list.setE_cate(rs1.getString(2));
                faqs_list.setE_question(rs1.getString(3));
                faqs_list.setE_answer(rs1.getString(4));
                list.add(faqs_list);
            }
        }
        return SUCCESS;
    }
}
