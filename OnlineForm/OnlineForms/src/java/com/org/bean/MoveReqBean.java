/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.bean;

/**
 *
 * @author Gyan
 */
public class MoveReqBean {

    private String femail;
    private String cemail;
    private String roles;


    public MoveReqBean() {
    }

    public MoveReqBean(String femail, String cemail, String roles) {
        this.femail = femail;
        this.cemail = cemail;
        this.roles = roles;
    }
    
    

    public String getFemail() {
        return femail;
    }

    public void setFemail(String femail) {
        this.femail = femail;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "MoveReqBean{" + "femail=" + femail + ", cemail=" + cemail + ", roles=" + roles + '}';
    }

    
    
    
}
