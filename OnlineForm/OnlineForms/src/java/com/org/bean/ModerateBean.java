/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.bean;

import java.util.Date;

/**
 *
 * @author dell
 */
public class ModerateBean {

    private int id;

    private String t_off_name;
    private String tauth_email;
    private String tmobile;
    
    private String errorMessage;

    public ModerateBean() {
        super();
    }

    public ModerateBean(int id, String t_off_name, String tmobile, String tauth_email) {
        this.id = id;
        this.t_off_name = t_off_name;
        this.tmobile = tmobile;
        this.tauth_email = tauth_email;
        
    }

    public ModerateBean(int i, String in28Minutes, String learn_Struts, Date date, boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getT_off_name() {
        return t_off_name;
    }

    public void setT_off_name(String t_off_name) {
        this.t_off_name = t_off_name;
    }

    public String getTauth_email() {
        return tauth_email;
    }

    public void setTauth_email(String tauth_email) {
        this.tauth_email = tauth_email;
    }

    public String getTmobile() {
        return tmobile;
    }

    public void setTmobile(String tmobile) {
        this.tmobile = tmobile;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

 

    
    
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ModerateBean other = (ModerateBean) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                "Todo [id=%s, t_off_name=%s, tauth_email=%s, tmobile=%s, errorMessage=%s]", id,
                t_off_name, tauth_email, tmobile, errorMessage);
    }
}
