/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.controller;

import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.DnsTeamBean;
import com.org.bean.DnsTeamBeanValidation;
import com.org.bean.UserData;
import com.org.dao.Ldap;
import com.org.service.DnsService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author Avinash
 */
public class DnsTeamController extends ActionSupport implements SessionAware {

    DnsTeamBeanValidation dnsTrckerFrmData = null;
    DnsService dnsservice = null;
    Map session;
    public String dnsTeamData;
    public String rcd_inserted = "";
    public int dnsTeamId;
    List<Object> dnsTractFetchData = new ArrayList<>();

    public DnsTeamController() {
        dnsTrckerFrmData = new DnsTeamBeanValidation();
        dnsservice = new DnsService();
        if (dnsservice == null) {
            dnsservice = new DnsService();
        }
        if (dnsTrckerFrmData == null) {
            dnsTrckerFrmData = new DnsTeamBeanValidation();
        }
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public List<Object> getDnsTractFetchData() {
        return dnsTractFetchData;
    }

    public void setDnsTractFetchData(List<Object> dnsTractFetchData) {
        this.dnsTractFetchData = dnsTractFetchData;
    }

    public int getDnsTeamId() {
        return dnsTeamId;
    }

    public void setDnsTeamId(int dnsTeamId) {
        this.dnsTeamId = dnsTeamId;
    }

    public String getDnsTeamData() {
        return dnsTeamData;
    }

    public void setDnsTeamData(String dnsTeamData) {
        this.dnsTeamData = dnsTeamData;
    }

    public DnsTeamBeanValidation getDnsTrckerFrmData() {
        return dnsTrckerFrmData;
    }

    public void setDnsTrckerFrmData(DnsTeamBeanValidation dnsTrckerFrmData) {
        this.dnsTrckerFrmData = dnsTrckerFrmData;
    }

    public String getRcd_inserted() {
        return rcd_inserted;
    }

    public void setRcd_inserted(String rcd_inserted) {
        this.rcd_inserted = rcd_inserted;
    }

    public void fetchDnsTeamData() {
        UserData userdata = (UserData) session.get("uservalues");
        HttpServletRequest request = ServletActionContext.getRequest();
        if (dnsservice.findValidUser(userdata.getAliasesInString())) {
            try {
                ServletActionContext.getResponse().getWriter().write(dnsservice.fetchDnsTeamData(request));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void fetchDuplicateDomain() {
        HttpServletRequest request = ServletActionContext.getRequest();
        try {
            ServletActionContext.getResponse().getWriter().write(dnsservice.fetchDnsTeamData(request));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String dnsTrackerFrmDataPost() {
        System.out.println("com.org.controller.DnsTeamController.dnsTrckerFrmDataPost()::: " + getDnsTrckerFrmData().getDomain());
        if (dnsTrckerFrmData.getErrorMessage().isEmpty()) {
            DnsTeamBean dnsTeamBean = dnsservice.fetchDnsTeamData(String.valueOf(dnsTrckerFrmData.getId()));
            if (dnsTrckerFrmData.getId() > 0) {
                rcd_inserted = dnsservice.updateRecordInTeamData(dnsTrckerFrmData);
            } else {
                rcd_inserted = dnsservice.addRecordInTeamData(dnsTrckerFrmData);
            }

            if (!rcd_inserted.isEmpty()) {
                //Discard pending campaigns for this updation
                Ldap ldap = new Ldap();
                Set<String> aliases = ldap.fetchAliases(dnsTeamBean.getEmail());
                for (String aliase : aliases) {
                    dnsservice.discardCampaign(aliase);
                }
                aliases = ldap.fetchAliases(dnsTrckerFrmData.getEmail());
                for (String aliase : aliases) {
                    dnsservice.discardCampaign(aliase);
                }
                dnsservice.discardCampaignThroughDomains(dnsTeamBean.getDomain());
                dnsservice.discardCampaignThroughIPs(dnsTeamBean.getIp());
            }
        }
        return SUCCESS;
    }

    public String dnsTrackerFrmDataPostFetch() {
        System.out.println("com.org.controller.DnsTeamController.dnsTrackerFrmDataPostFetch()ID::: " + dnsTeamId);
        if (dnsTeamId > 0) {
            dnsTractFetchData = dnsservice.fetchRecordInTeamData(dnsTeamId);
        }
        return SUCCESS;
    }

    public String dnsTrackerFrmDataPostDelete() {
        rcd_inserted = dnsservice.deleetRecordInTeamData(dnsTeamId);
        return SUCCESS;
    }
}
