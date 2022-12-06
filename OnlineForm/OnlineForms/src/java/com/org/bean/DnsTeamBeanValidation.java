/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.bean;

import com.org.service.DnsService;
import java.util.HashMap;
import java.util.Map;
import validation.validation;

/**
 *
 * @author Avinash
 */
public class DnsTeamBeanValidation {
    private int id;
    private String name;
    private String ip;
    private String email;
    private String mobile;
    private String domain;
    private Map<String, Object> errorMessage = new HashMap<>();
    private validation validation1 = new validation();
    private DnsService dnsService = new DnsService();

    public DnsTeamBeanValidation() {
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty()) {
            this.errorMessage.put("name_err", "Please enter Owner name.");
        } else {
            this.name = name;
        }
    }

    public String getIp() {
        return ip;
    }

    public Map<String, Object> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Map<String, Object> errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setIp(String ip) {
        if (ip.isEmpty() || validation1.dnsmodifyipvalidation(ip)) {
            if (ip.isEmpty() || validation1.cnamevalidation(ip)) { 
                this.errorMessage.put("ip_err", "Please enter valid CNAME or IP.");
            } else {
                this.ip = ip;
            }
        } else {
            this.ip = ip;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.isEmpty() || validation1.EmailValidation(email)) {
            this.errorMessage.put("email_err", "Please enter Valid Owner email.");
        } else {
            this.email = email;
        }
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        if (mobile.isEmpty() || validation1.MobileValidation(mobile)) {
            this.errorMessage.put("mobile_err", "Please enter Valid Owner Contact Number.");
        } else {
            this.mobile = mobile;
        }
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        int id1 = id;
        if (domain.isEmpty()) {
            this.errorMessage.put("domain_err", "Domain can not be empty.");
        } else {
            if (validation1.dnsurlvalidation(domain)) {
                this.errorMessage.put("domain_err", "Enter valid DNS URL [e.g.: demo.nic.in or demo.gov.in].");
            } else if(dnsService.domainExists(domain) && id1 > 0) {
                this.errorMessage.put("domain_err", "This Domain already Exsits.");
            } else {
                this.domain = domain;
            }
        }
    }
    
    

    public DnsTeamBeanValidation(String name, String ip, String email, String mobile, String domain) {
        this.name = name;
        this.ip = ip;
        this.email = email;
        this.mobile = mobile;
        this.domain = domain;
    }

    public DnsTeamBeanValidation(int id, String name, String ip, String email, String mobile, String domain) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.email = email;
        this.mobile = mobile;
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "DnsTeamBean{" + "id=" + id + ", name=" + name + ", ip=" + ip + ", email=" + email + ", mobile=" + mobile + ", domain=" + domain + ", errorMessage=" + errorMessage + ", validation1=" + validation1 + '}';
    }
    
}
