/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.bean;

/**
 *
 * @author Avinash
 */
public class OwnerBean {
    private int id;

    private String owner_name;
    private String owner_email;
    private String owner_mobile;
    private String errorMessage;

    public OwnerBean(int id, String owner_name, String owner_email, String owner_mobile) {
        this.id = id;
        this.owner_name = owner_name;
        this.owner_email = owner_email;
        this.owner_mobile = owner_mobile;
    }

    public OwnerBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public void setOwner_email(String owner_email) {
        this.owner_email = owner_email;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public void setOwner_mobile(String owner_mobile) {
        this.owner_mobile = owner_mobile;
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
        OwnerBean other = (OwnerBean) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
             return String.format(
                "Todo [id=%s, owner_name=%s, owner_email=%s, owner_mobile=%s, errorMessage=%s]", id,
                owner_name, owner_name, owner_mobile, errorMessage);
    }
    
    
    
    
    
    
}
