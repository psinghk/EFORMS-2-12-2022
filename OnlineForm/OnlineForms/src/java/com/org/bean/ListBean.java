package com.org.bean;

import java.util.ArrayList;

public class ListBean {

    // start, code added by pr on 9thjan18
    private ArrayList<String> filterForms;
    
    public ArrayList<String> getFilterForms() {
        return filterForms;
    }

    public void setFilterForms(ArrayList<String> filterForms) {
        this.filterForms = filterForms;
    }

    // end, code added by pr on 9thjan18
     // start, code added on 8thnov17
    private String console = "Admin Console";

    private String roleHead = "";

    private String role = "";

    private String totalRequest = "";

    private String newRequest = "";

    private String pendingRequest = "";

    private String completeRequest = "";

    private String rejectedRequest = "";

    private ArrayList<Forms> data;

    private String heading = "APPLICATION REQUESTS";

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public ArrayList<Forms> getData() {
        return data;
    }

    public void setData(ArrayList<Forms> data) {
        this.data = data;
    }

    public String getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(String totalRequest) {
        this.totalRequest = totalRequest;
    }

    public String getNewRequest() {
        return newRequest;
    }

    public void setNewRequest(String newRequest) {
        this.newRequest = newRequest;
    }

    public String getPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(String pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public String getCompleteRequest() {
        return completeRequest;
    }

    public void setCompleteRequest(String completeRequest) {
        this.completeRequest = completeRequest;
    }

    public String getRejectedRequest() {
        return rejectedRequest;
    }

    public void setRejectedRequest(String rejectedRequest) {
        this.rejectedRequest = rejectedRequest;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public String getRoleHead() {
        return roleHead;
    }

    public void setRoleHead(String roleHead) {
        this.roleHead = roleHead;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // end, code added on 8thnov17
}
