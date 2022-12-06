package com.org.bean;

import validation.validation;

public class ValidatedEmailBean extends EmailBean {

    private int id = -1;
    private int campaignId = -1;
    private String registrationNumber = "";
    private String fname = "";
    private String lname = "";
    private String state = "";
    private String mail = "";
    private String designation = "";
    private String mobile = "";
    private String country_code = "";
    private String department = "";
    private String errorMessage = "";
    private String dor = "";
    private String dob = "";
    private String uid = "";
    private String empcode = "";

    public validation validation1 = new validation();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ValidatedEmailBean emailData = (ValidatedEmailBean) obj;
        if (emailData.id == this.id) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getstate() {
        return state;
    }

    public void setstate(String state) {
        this.state = state;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        if (!mobile.isEmpty() && validation1.MobileValidation(mobile)) {
            this.errorMessage += "Enter valid MOBILE NUMBER  ";
        }
        this.mobile = mobile;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public validation getValidation1() {
        return validation1;
    }

    public void setValidation1(validation validation1) {
        this.validation1 = validation1;
    }

    public String getfname() {
        return fname;
    }

    public void setfname(String fname) {
        if (!fname.isEmpty() && validation1.nameValidation(fname)) {
            this.errorMessage += "Enter valid FNAME ";
        } else {
            this.fname = fname;
        }
    }

    public String getErrorMessage() {
        this.errorMessage = this.errorMessage.replaceAll("\\s*\\|\\s*$", "");
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    @Override
    public String toString() {
        return String.format("ID : " + id + " fname : " + fname + " lname: " + lname + " mail:" + mail + " department: " + department + " designation : " + designation + " mobile : " + mobile + " state : " + state + " Error : " + errorMessage);
    }

}
