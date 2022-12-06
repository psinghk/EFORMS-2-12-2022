package com.org.bean;

import validation.validation;

public class ValidatedDnsBean extends DnsBean {

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
    private validation validation1 = new validation();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ValidatedDnsBean dnsData = (ValidatedDnsBean) obj;
        if (dnsData.id == this.id) {
            return true;
        }

        //return (dnsData.id == this.id);
        boolean result = false;
        switch (req_other_add) {
            case "":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.new_ip.equalsIgnoreCase(this.new_ip);
                break;
            case "cname":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.cname_txt.equalsIgnoreCase(this.cname_txt);
                break;
            case "mx":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.mx_txt.equalsIgnoreCase(this.mx_txt);
                break;
            case "spf":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.spf_txt.equalsIgnoreCase(this.spf_txt);
                break;
            case "srv":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.srv_txt.equalsIgnoreCase(this.srv_txt);
                break;
            case "txt":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.txt_txt.equalsIgnoreCase(this.txt_txt);
                break;
            case "ptr":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.ptr_txt.equalsIgnoreCase(this.ptr_txt);
                break;
            case "dmarc":
                result = dnsData.domain.equalsIgnoreCase(this.domain) && dnsData.dmarc_txt.equalsIgnoreCase(this.dmarc_txt);
                break;
        }

        return result;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public String getDns_loc() {
        return dns_loc;
    }

    public void setDns_loc(String dns_loc) {
        if (dns_loc.isEmpty()) {
            this.errorMessage += "Location of the server can not be empty. | ";
        } else {
            if (validation1.addValidation(dns_loc)) {
                if (dns_loc.length() < 2) {
                    this.errorMessage += "Length of server location must be at least 2 " + " | ";
                } else if (dns_loc.length() > 100) {
                    this.errorMessage += "Length of server location can not be more than 100 " + " | ";
                } else {
                    this.errorMessage += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed. | ";
                }
            } else {
                this.dns_loc = dns_loc;
            }
        }
    }

    public String getMigration_date() {
        return migration_date;
    }

    public void setMigration_date(String migration_date) {
        if (!migration_date.isEmpty()) {
            String msg = validation1.dnsDateValidation(migration_date);
            if (!msg.isEmpty()) {
                this.migration_date = migration_date;
                this.errorMessage += msg + " | ";
            } else {
                this.migration_date = migration_date;
            }
        } else {
            this.migration_date = migration_date;
        }
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        if (domain.isEmpty()) {
            this.errorMessage += "Domain can not be empty. | ";
        } else {
            if (validation1.dnsurlvalidation(domain)) {
                this.errorMessage += "Enter valid DNS URL [e.g.: demo.nic.in or demo.gov.in]. | ";
            } else {
                this.domain = domain;
            }
        }
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        if (!cname.isEmpty() && validation1.cnamevalidation(cname)) {
            this.errorMessage += "Enter valid CNAME [e.g.: demo.nic.in or demo.gov.in]. |  ";
        } else {
            this.cname = cname;
        }
    }

    public String getNew_ip() {
        return new_ip;
    }

    public void setNew_ip(String new_ip) {
        if (!new_ip.isEmpty() && validation1.dnsmodifyipvalidation(new_ip)) {
            this.errorMessage += "Please enter valid new IP. | ";
        } else {
            this.new_ip = new_ip;
        }
    }

    public String getOld_ip() {
        return old_ip;
    }

    public void setOld_ip(String old_ip) {
        if (validation1.dnsmodifyipvalidation(old_ip)) {
            this.errorMessage += "Please enter valid old IP. | ";
        } else {
            this.old_ip = old_ip;
        }
    }

    public String getErrorMessage() {
        this.errorMessage = this.errorMessage.replaceAll("\\s*\\|\\s*$", "");
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
        if (!cname_txt.isEmpty() && validation1.cnamevalidation(cname_txt)) {
            this.errorMessage += "Enter valid CNAME [e.g.: demo.nic.in or demo.gov.in]. |  ";
        } else {
            this.cname_txt = cname_txt;
        }
    }

    public String getMx_txt() {
        return mx_txt;
    }

    public void setMx_txt(String mx_txt) {
        if (!mx_txt.isEmpty() && validation1.dnsServiceValidation(mx_txt)) {
            this.errorMessage += "Enter MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed. |  ";
        } else {
            this.mx_txt = mx_txt;
        }
    }

    public String getPtr_txt() {
        return ptr_txt;
    }

    public void setPtr_txt(String ptr_txt) {
        if (!ptr_txt.isEmpty() && validation1.dnsmodifyipvalidation(ptr_txt)) {
            this.errorMessage += "Enter valid PTR i.e it should be in IPV4/IPV6 format. | ";
        } else {
            this.ptr_txt = ptr_txt;
        }
    }

    public String getTxt_txt() {
        return txt_txt;
    }

    public void setTxt_txt(String txt_txt) {
        if (!txt_txt.isEmpty() && validation1.dnstxtValidation(txt_txt)) {
            if (txt_txt.length() < 2) {
                this.errorMessage += "Length of TXT must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of TXT can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid TXT value [limit 2-300]. | ";
            }
        } else {
            this.txt_txt = txt_txt;
        }
    }

    public String getSrv_txt() {
        return srv_txt;
    }

    public void setSrv_txt(String srv_txt) {
        if (!srv_txt.isEmpty() && validation1.dnstxtValidation(srv_txt)) {
            if (srv_txt.length() < 2) {
                this.errorMessage += "Length of SRV must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of SRV can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid SRV value [limit 2-300]. | ";
            }
        } else {
            this.srv_txt = srv_txt;
        }
    }

    public String getSpf_txt() {
        return spf_txt;
    }

    public void setSpf_txt(String spf_txt) {
        if (!spf_txt.isEmpty() && validation1.dnstxtValidation(spf_txt)) {
            if (spf_txt.length() < 2) {
                this.errorMessage += "Length of SPF must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of SPF can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid SPF value [limit 2-300]. | ";
            }
        } else {
            this.spf_txt = spf_txt;
        }
    }

    public String getDmarc_txt() {
        return dmarc_txt;
    }

    public void setDmarc_txt(String dmarc_txt) {
        if (!dmarc_txt.isEmpty() && validation1.dnstxtValidation(dmarc_txt)) {
            if (dmarc_txt.length() < 2) {
                this.errorMessage += "Length of DMARC must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of DMARC can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid DMARC value [limit 2-300]. | ";
            }
        } else {
            this.dmarc_txt = dmarc_txt;
        }
    }

    public String getOld_cname_txt() {
        return old_cname_txt;
    }

    public void setOld_cname_txt(String old_cname_txt) {
        if (!old_cname_txt.isEmpty() && validation1.cnamevalidation(old_cname_txt)) {
            this.errorMessage += "Enter valid old CNAME [e.g.: demo.nic.in or demo.gov.in]. |  ";
        } else {
            this.old_cname_txt = old_cname_txt;
        }
    }

    public String getOld_mx_txt() {
        return old_mx_txt;
    }

    public void setOld_mx_txt(String old_mx_txt) {
        if (!old_mx_txt.isEmpty() && validation1.dnsServiceValidation(old_mx_txt)) {
            this.errorMessage += "Enter valid old MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed. |  ";
        } else {
            this.old_mx_txt = old_mx_txt;
        }
    }

    public String getOld_ptr_txt() {
        return old_ptr_txt;
    }

    public void setOld_ptr_txt(String old_ptr_txt) {
        if (!old_ptr_txt.isEmpty() && validation1.dnsmodifyipvalidation(old_ptr_txt)) {
            this.errorMessage += "Enter valid old PTR i.e. it should be in IPV4/IPV6 format. | ";
        } else {
            this.old_ptr_txt = old_ptr_txt;
        }
    }

    public String getOld_txt_txt() {
        return old_txt_txt;
    }

    public void setOld_txt_txt(String old_txt_txt) {
        if (!old_txt_txt.isEmpty() && validation1.dnstxtValidation(old_txt_txt)) {
            if (txt_txt.length() < 2) {
                this.errorMessage += "Length of TXT must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of TXT can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid TXT value [limit 2-300]. | ";
            }
        } else {
            this.old_txt_txt = old_txt_txt;
        }
    }

    public String getOld_srv_txt() {
        return old_srv_txt;
    }

    public void setOld_srv_txt(String old_srv_txt) {
        if (!old_srv_txt.isEmpty() && validation1.dnstxtValidation(old_srv_txt)) {
            if (srv_txt.length() < 2) {
                this.errorMessage += "Length of SRV must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of SRV can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid SRV value [limit 2-300]. | ";
            }
        } else {
            this.old_srv_txt = old_srv_txt;
        }
    }

    public String getOld_spf_txt() {
        return old_spf_txt;
    }

    public void setOld_spf_txt(String old_spf_txt) {
        if (!old_spf_txt.isEmpty() && validation1.dnstxtValidation(old_spf_txt)) {
            if (spf_txt.length() < 2) {
                this.errorMessage += "Length of SPF must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of SPF can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid SPF value [limit 2-300]. | ";
            }
        } else {
            this.old_spf_txt = old_spf_txt;
        }
    }

    public String getOld_dmarc_txt() {
        return old_dmarc_txt;
    }

    public void setOld_dmarc_txt(String old_dmarc_txt) {
        if (!old_dmarc_txt.isEmpty() && validation1.dnstxtValidation(old_dmarc_txt)) {
            if (dmarc_txt.length() < 2) {
                this.errorMessage += "Length of DMARC must be at least 2 " + " | ";
            } else if (txt_txt.length() > 300) {
                this.errorMessage += "Length of DMARC can not be more than 300 " + " | ";
            } else {
                this.errorMessage += "Enter valid DMARC value [limit 2-300]. | ";
            }
        } else {
            this.old_dmarc_txt = old_dmarc_txt;
        }
    }

    @Override
    public String toString() {
        return String.format("ID : " + id + " Domain : " + domain + " New Ip: " + new_ip + " Old Ip :" + old_ip + " cname: " + cname + " cname_txt : " + cname_txt + " mx : " + mx_txt + " spf : " + spf_txt + " srv : " + srv_txt + " txt : " + txt_txt + " ptr : " + ptr_txt + " dmarc : " + dmarc_txt + " Location : " + dns_loc + " Migration Date : " + migration_date + " Error : " + errorMessage + "Request Type : " + req_);
    }
}
