/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.bean;

/**
 *
 * @author dell
 */
public class DlistModeratorBean {
    
    
    
    private String t_off_name;
    private String tauth_email;
    private String tmobile;
    private String errorMessage;    

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
    
    
    
}
