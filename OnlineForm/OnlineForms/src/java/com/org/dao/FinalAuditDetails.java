/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.dao;

/**
 *
 * @author Mohit Sharma
 */
public class FinalAuditDetails {
    
    private String registrationNumber;
    private String applicantEmail;
    private String status;
    private String formName;
    private String toEmail;
    private String toMobile;
    private String toName;

     public FinalAuditDetails() {
    }
     
    
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getToMobile() {
        return toMobile;
    }

    public void setToMobile(String toMobile) {
        this.toMobile = toMobile;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    

    @Override
    public String toString() {
        return "FinalAuditDetails{" + "registrationNumber=" + registrationNumber + ", applicantEmail=" + applicantEmail + ", status=" + status + ", formName=" + formName + ", toEmail=" + toEmail + ", toMobile=" + toMobile + ", toName=" + toName + '}';
    }

   
    
}
