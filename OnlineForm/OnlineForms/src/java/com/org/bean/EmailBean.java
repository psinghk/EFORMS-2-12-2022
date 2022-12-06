package com.org.bean;

public class EmailBean {

    private int id = -1;
    private int campaignId = -1;
    private String fname ="";
    private String lname ="";
    private String state ="";
    private String mail ="";
    private String designation ="";
    private String mobile ="";
    private String country_code ="";
    private String department ="";
    private String registration_No ="";
    private String errorMessage = "";
    private String dor = "";
    private String dob = "";
    private String uid = "";
    private String empcode = "";
    private String acc_cat ="";

    public String getAcc_cat() {
        return acc_cat;
    }

    public void setAcc_cat(String acc_cat) {
        this.acc_cat = acc_cat;
    }


    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        EmailBean emailData = (EmailBean) obj;
        if (emailData.id == this.id) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
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
    

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getRegistration_No() {
        return registration_No;
    }

    public void setRegistration_No(String registration_No) {
        this.registration_No = registration_No;
    }

@Override
    public String toString() {
        return String.format("ID : " + id + " fname : " + fname + " lname: " + lname + " mail:" + mail + " department: " + department + " designation : " + designation + " mobile : " + mobile + " state : " + state +  " Error : " + errorMessage );
    }
}
