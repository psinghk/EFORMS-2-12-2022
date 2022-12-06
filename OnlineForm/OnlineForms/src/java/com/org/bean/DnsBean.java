package com.org.bean;

public class DnsBean {

    private int id = -1;
    private String domain = "";
    private String cname = "";
    private String new_ip = "";
    private String old_ip = "";
    private String errorMessage = "";
    private String req_ = "";
    private String req_other_add = "";
    private String cname_txt = "";
    private String mx_txt = "";
    private String ptr_txt = "";
    private String txt_txt = "";
    private String srv_txt = "";
    private String spf_txt = "";
    private String dmarc_txt = "";
    private String old_cname_txt = "";
    private String old_mx_txt = "";
    private String old_ptr_txt = "";
    private String old_txt_txt = "";
    private String old_srv_txt = "";
    private String old_spf_txt = "";
    private String old_dmarc_txt = "";
    private String dns_loc = "";
    private String migration_date = "";
    private int campaignId = -1;
    private String registrationNumber = "";
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        DnsBean dnsData = (DnsBean) obj;
        if (dnsData.id == this.id) {
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getNew_ip() {
        return new_ip;
    }

    public void setNew_ip(String new_ip) {
        this.new_ip = new_ip;
    }

    public String getOld_ip() {
        return old_ip;
    }

    public void setOld_ip(String old_ip) {
        this.old_ip = old_ip;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getReq_() {
        return req_;
    }

    public void setReq_(String req_) {
        this.req_ = req_;
    }

    public String getReq_other_add() {
        return req_other_add;
    }

    public void setReq_other_add(String req_other_add) {
        this.req_other_add = req_other_add;
    }

    public String getCname_txt() {
        return cname_txt;
    }

    public void setCname_txt(String cname_txt) {
        this.cname_txt = cname_txt;
    }

    public String getMx_txt() {
        return mx_txt;
    }

    public void setMx_txt(String mx_txt) {
        this.mx_txt = mx_txt;
    }

    public String getPtr_txt() {
        return ptr_txt;
    }

    public void setPtr_txt(String ptr_txt) {
        this.ptr_txt = ptr_txt;
    }

    public String getTxt_txt() {
        return txt_txt;
    }

    public void setTxt_txt(String txt_txt) {
        this.txt_txt = txt_txt;
    }

    public String getSrv_txt() {
        return srv_txt;
    }

    public void setSrv_txt(String srv_txt) {
        this.srv_txt = srv_txt;
    }

    public String getSpf_txt() {
        return spf_txt;
    }

    public void setSpf_txt(String spf_txt) {
        this.spf_txt = spf_txt;
    }

    public String getDmarc_txt() {
        return dmarc_txt;
    }

    public void setDmarc_txt(String dmarc_txt) {
        this.dmarc_txt = dmarc_txt;
    }

    public String getOld_cname_txt() {
        return old_cname_txt;
    }

    public void setOld_cname_txt(String old_cname_txt) {
        this.old_cname_txt = old_cname_txt;
    }

    public String getOld_mx_txt() {
        return old_mx_txt;
    }

    public void setOld_mx_txt(String old_mx_txt) {
        this.old_mx_txt = old_mx_txt;
    }

    public String getOld_ptr_txt() {
        return old_ptr_txt;
    }

    public void setOld_ptr_txt(String old_ptr_txt) {
        this.old_ptr_txt = old_ptr_txt;
    }

    public String getOld_txt_txt() {
        return old_txt_txt;
    }

    public void setOld_txt_txt(String old_txt_txt) {
        this.old_txt_txt = old_txt_txt;
    }

    public String getOld_srv_txt() {
        return old_srv_txt;
    }

    public void setOld_srv_txt(String old_srv_txt) {
        this.old_srv_txt = old_srv_txt;
    }

    public String getOld_spf_txt() {
        return old_spf_txt;
    }

    public void setOld_spf_txt(String old_spf_txt) {
        this.old_spf_txt = old_spf_txt;
    }

    public String getOld_dmarc_txt() {
        return old_dmarc_txt;
    }

    public void setOld_dmarc_txt(String old_dmarc_txt) {
        this.old_dmarc_txt = old_dmarc_txt;
    }

    public String getDns_loc() {
        return dns_loc;
    }

    public void setDns_loc(String dns_loc) {
        this.dns_loc = dns_loc;
    }

    public String getMigration_date() {
        return migration_date;
    }

    public void setMigration_date(String migration_date) {
        this.migration_date = migration_date;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString() {
        return String.format("ID : " + id + " Domain : " + domain + " New Ip: " + new_ip + " Old Ip :" + old_ip + " cname: " + cname + " cname_txt : " + cname_txt + " mx : " + mx_txt + " spf : " + spf_txt + " srv : " + srv_txt + " txt : " + txt_txt + " ptr : " + ptr_txt + " dmarc : " + dmarc_txt + " Location : " + dns_loc + " Migration Date : " + migration_date + " Error : " + errorMessage + "Request Type : " + req_);
    }
}
