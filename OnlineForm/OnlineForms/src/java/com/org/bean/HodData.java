package com.org.bean;
public class HodData {
    private String email,name,mobile,telephone,designation;
    boolean nicemployee, govEmployee, nonGovEmployee;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephoone) {
        this.telephone = telephoone;
    }
    public String getDesignation() {
        return designation;
    }
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public boolean isNicemployee() {
        return nicemployee;
    }

    public void setNicemployee(boolean nicemployee) {
        this.nicemployee = nicemployee;
    }

    public boolean isGovEmployee() {
        return govEmployee;
    }

    public void setGovEmployee(boolean govEmployee) {
        this.govEmployee = govEmployee;
    }

    public boolean isNonGovEmployee() {
        return nonGovEmployee;
    }

    public void setNonGovEmployee(boolean nonGovEmployee) {
        this.nonGovEmployee = nonGovEmployee;
    }
}
