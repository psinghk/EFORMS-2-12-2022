package com.org.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import com.org.bean.DnsBean;
import com.org.bean.HodData;
import com.org.bean.UserData;
import com.org.bean.ValidatedDnsBean;
import com.org.dao.Database;
import com.org.dao.DbDao;
import com.org.dao.DnsDao;
import com.org.dao.Ldap;
import com.org.service.DnsService;
import com.org.service.ProfileService;
import com.org.service.UpdateService;
import com.org.utility.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import model.FormBean;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.jsoup.Jsoup;
import org.owasp.esapi.ESAPI;
import validation.validation;

/**
 *
 * @author Nancy
 */
public class Dns_registration extends ActionSupport implements SessionAware {

    FormBean form_details;
    DnsService dnsservice = new DnsService();
    UpdateService updateService = new UpdateService();// line added by pr on 6thfeb18
    Map session;
    Map dnsDetails = new HashMap();
    validation valid = new validation();
    Map<String, Object> error = new HashMap<String, Object>();
    public String action_type, data, dns_url, dns_cname, dns_oldip, dns_newip, dns_loc, ReturnString, request_type, cert, cert1, stat_type, domain;
    Map profile_values = new HashMap();
    ProfileService profileService = new ProfileService();
    Map hodDetails = new HashMap();
    private Database db = null;
    Map<String, String> map = new HashMap<>();
    public int bulkDataId = 0;
    public String bulkDataStr = "";
    boolean flag_url_exist = false;
    public ValidatedDnsBean dnsEditData = new ValidatedDnsBean();
    public DnsBean dnsData = new DnsBean();
    Map<String, String> domainOldIp = new HashMap<>();
    Map<String, String> domainNewIp = new HashMap<>();
    Map<String, String> domainCnames = new HashMap<>();
    Map<String, String> mappedDomainWithIp = new HashMap<>();
    Map<String, Object> bulkData = new HashMap<>();
    List<Map<String, String>> campaigns = new ArrayList<>();
    String regNumber = "";
    String CSRFRandom;
    boolean areThereSuccessfulRecords = false;
    int iCampaignId;
    Ldap ldap;

    public Dns_registration() {
        dnsservice = new DnsService();
        updateService = new UpdateService();
        dnsDetails = new HashMap();
        valid = new validation();
        error = new HashMap<>();
        profile_values = new HashMap();
        hodDetails = new HashMap();
        map = new HashMap<>();
        dnsEditData = new ValidatedDnsBean();
        dnsData = new DnsBean();
        domainOldIp = new HashMap<>();
        domainNewIp = new HashMap<>();
        domainCnames = new HashMap<>();
        mappedDomainWithIp = new HashMap<>();
        bulkData = new HashMap<>();
        campaigns = new ArrayList<>();
        ldap = new Ldap();
        if (dnsservice == null) {
            dnsservice = new DnsService();
        }
        if (updateService == null) {
            updateService = new UpdateService();
        }
        if (dnsDetails == null) {
            dnsDetails = new HashMap();
        }
        if (valid == null) {
            valid = new validation();
        }
        if (error == null) {
            error = new HashMap<>();
        }
        if (profile_values == null) {
            profile_values = new HashMap();
        }
        if (hodDetails == null) {
            hodDetails = new HashMap();
        }
        if (map == null) {
            map = new HashMap<>();
        }
        if (dnsEditData == null) {
            dnsEditData = new ValidatedDnsBean();
        }
        if (dnsData == null) {
            dnsData = new DnsBean();
        }
        if (domainOldIp == null) {
            domainOldIp = new HashMap<>();
        }
        if (domainNewIp == null) {
            domainNewIp = new HashMap<>();
        }
        if (domainCnames == null) {
            domainCnames = new HashMap<>();
        }
        if (mappedDomainWithIp == null) {
            mappedDomainWithIp = new HashMap<>();
        }
        if (bulkData == null) {
            bulkData = new HashMap<>();
        }
        if (campaigns == null) {
            campaigns = new ArrayList<>();
        }
    }

    public DnsBean getDnsData() {
        return dnsData;
    }

    public void setDnsData(DnsBean dnsData) {
        this.dnsData = dnsData;
    }

    public int getiCampaignId() {
        return iCampaignId;
    }

    public void setiCampaignId(int iCampaignId) {
        this.iCampaignId = iCampaignId;
    }

    public String getCSRFRandom() {
        return CSRFRandom;
    }

    public void setCSRFRandom(String CSRFRandom) {
        this.CSRFRandom = Jsoup.parse(CSRFRandom).text();
    }

    public boolean isAreThereSuccessfulRecords() {
        return areThereSuccessfulRecords;
    }

    public void setAreThereSuccessfulRecords(boolean areThereSuccessfulRecords) {
        this.areThereSuccessfulRecords = areThereSuccessfulRecords;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public ValidatedDnsBean getDnsEditData() {
        return dnsEditData;
    }

    public void setDnsEditData(ValidatedDnsBean dnsEditData) {
        this.dnsEditData = dnsEditData;
    }

    public List<Map<String, String>> getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(List<Map<String, String>> campaigns) {
        this.campaigns = campaigns;
    }

    // end, code added by pr on 22ndjan18
    public void setSession(Map session) {
        this.session = session;
    }

    public FormBean getForm_details() {
        return form_details;
    }

    public void setForm_details(FormBean form_details) {
        this.form_details = form_details;
    }

    public String getReturnString() {
        return ReturnString;
    }

    public void setReturnString(String ReturnString) {
        this.ReturnString = Jsoup.parse(ReturnString).text();
    }

    public Map fetchDNSDetails() {
        return dnsDetails;
    }

    public void setDnsDetails(Map dnsDetails) {
        this.dnsDetails = dnsDetails;
    }

    public Map<String, Object> getError() {
        return error;
    }

    public void setError(Map<String, Object> error) {
        this.error = error;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = Jsoup.parse(action_type).text();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = Jsoup.parse(data).text();
    }

    public String getDns_url() {
        return dns_url;
    }

    public void setDns_url(String dns_url) {
        this.dns_url = Jsoup.parse(dns_url).text();
    }

    public String getDns_cname() {
        return dns_cname;
    }

    public void setDns_cname(String dns_cname) {
        this.dns_cname = dns_cname;
    }

    public String getDns_oldip() {
        return dns_oldip;
    }

    public void setDns_oldip(String dns_oldip) {
        this.dns_oldip = dns_oldip;
    }

    public String getDns_newip() {
        return dns_newip;
    }

    public void setDns_newip(String dns_newip) {
        this.dns_newip = dns_newip;
    }

    public String getDns_loc() {
        return dns_loc;
    }

    public void setDns_loc(String dns_loc) {
        this.dns_loc = Jsoup.parse(dns_loc).text();
    }

    public Map getProfile_values() {
        return profile_values;
    }

    public void setProfile_values(Map profile_values) {
        this.profile_values = profile_values;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String fetchCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getCert1() {
        return cert1;
    }

    public void setCert1(String cert1) {
        this.cert1 = cert1;
    }

    public String getStat_type() {
        return stat_type;
    }

    public void setStat_type(String stat_type) {
        this.stat_type = stat_type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Map<String, Object> getBulkData() {
        return bulkData;
    }

    public void setBulkData(Map<String, Object> bulkData) {
        this.bulkData = bulkData;
    }

    public int getBulkDataId() {
        return bulkDataId;
    }

    public void setBulkDataId(int bulkDataId) {
        this.bulkDataId = bulkDataId;
    }

    public String getBulkDataStr() {
        return bulkDataStr;
    }

    public void setBulkDataStr(String bulkDataStr) {
        this.bulkDataStr = bulkDataStr;
    }

    private boolean allRadioButtonsUnChecked() {

        if (form_details.getRequest_mx_chk() == null && form_details.getRequest_ptr_chk() == null && form_details.getRequest_spf_chk() == null && form_details.getRequest_srv_chk() == null && form_details.getRequest_txt_chk() == null && form_details.getRequest_dmarc_chk() == null) {
            return true;
        } else if ((form_details.getRequest_mx_chk() != null && form_details.getRequest_mx_chk().isEmpty())
                && (form_details.getRequest_ptr_chk() != null && form_details.getRequest_ptr_chk().isEmpty())
                && (form_details.getRequest_spf_chk() != null && form_details.getRequest_spf_chk().isEmpty())
                && (form_details.getRequest_srv_chk() != null && form_details.getRequest_srv_chk().isEmpty())
                && (form_details.getRequest_txt_chk() != null && form_details.getRequest_txt_chk().isEmpty())
                && (form_details.getRequest_dmarc_chk() != null && form_details.getRequest_dmarc_chk().isEmpty())) {
            return true;
        } else if ((form_details.getRequest_mx_chk() != null && form_details.getRequest_mx_chk().equals("mx"))
                || (form_details.getRequest_ptr_chk() != null && form_details.getRequest_ptr_chk().equals("ptr"))
                || (form_details.getRequest_spf_chk() != null && form_details.getRequest_spf_chk().equals("spf"))
                || (form_details.getRequest_srv_chk() != null && form_details.getRequest_srv_chk().equals("srv"))
                || (form_details.getRequest_txt_chk() != null && form_details.getRequest_txt_chk().equals("txt"))
                || (form_details.getRequest_dmarc_chk() != null && form_details.getRequest_dmarc_chk().equals("dmarc"))) {
            return false;
        } else if ((form_details.getRequest_mx_chk() != null && !form_details.getRequest_mx_chk().isEmpty())
                || (form_details.getRequest_ptr_chk() != null && !form_details.getRequest_ptr_chk().isEmpty())
                || (form_details.getRequest_spf_chk() != null && !form_details.getRequest_spf_chk().isEmpty())
                || (form_details.getRequest_srv_chk() != null && !form_details.getRequest_srv_chk().isEmpty())
                || (form_details.getRequest_txt_chk() != null && !form_details.getRequest_txt_chk().isEmpty())
                || (form_details.getRequest_dmarc_chk() != null && !form_details.getRequest_dmarc_chk().isEmpty())) {
            return false;
        } else {
            return false;
        }
    }

    private boolean isThirdLevelDomain(String domain) {
        int count_char = countChar(domain, '.');
        if (count_char == 3 && domain.startsWith("www.")) {
            return true;
        } else if (count_char < 3) {
            return true;
        } else {
            return false;
        }
    }

    private boolean doesIPbelongToNIC(String ip) {
        return ip.startsWith("164.100") || ip.startsWith("14.139") || ip.startsWith("180.149") || ip.startsWith("10.");
    }

    private boolean isApplicantOwner(String url) {
        Set<String> owners = dnsservice.fetchOwner(url);
        String applicantEmail = "";
        UserData userdata = null;
        if (session != null) {
            if (session.get("uservalues") != null) {
                userdata = (UserData) session.get("uservalues");
                if (session.get("ref") == null) {
                    applicantEmail = userdata.getEmail();
                } else {
                    applicantEmail = dnsservice.fetchApplicant(session.get("ref").toString());
                }
            }
        }

        if (!applicantEmail.isEmpty()) {
            return owners.contains(applicantEmail);
        } else {
            return false;
        }
    }

    private boolean isApplicantOwnerWhileSubmission(String url) {
        Set<String> owners = dnsservice.fetchOwner(url);
        String applicantEmail = "";
        UserData userdata = null;
        if (session != null) {
            if (session.get("uservalues") != null) {
                userdata = (UserData) session.get("uservalues");
                applicantEmail = userdata.getEmail();
            }
        }

        if (!applicantEmail.isEmpty()) {
            if (owners.size() == 0) {
                return true;
            }
            return owners.contains(applicantEmail);
        } else {
            return false;
        }
    }

    private boolean isGovDomain(String domain) {
        String[] a = domain.split(".");
        int lastIndex, secondLastIndex;
        lastIndex = domain.lastIndexOf('.');
        secondLastIndex = domain.lastIndexOf('.', lastIndex - 1);
        String d = domain.substring(secondLastIndex + 1, lastIndex) + domain.substring(lastIndex, domain.length());
        return d.equals("gov.in");
    }

    public String DNS_tab2() {
        int campaignId = -1;
        if (session.get("ref") != null) {
            campaignId = dnsservice.fetchCampaignId(session.get("ref").toString());
        } else {
            if (session.get("campaign_id") != null) {
                campaignId = (int) session.get("campaign_id");
            }
            if (campaignId == -1) {
                campaignId = iCampaignId;
            }
        }

        if (campaignId != -1) {

            try {
                db = new Database();
                UserData userdata = (UserData) session.get("uservalues");
                boolean ldap_employee = userdata.isIsEmailValidated();
                boolean nic_employee = userdata.isIsNICEmployee();
                HodData hoddata = (HodData) userdata.getHodData();
                String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

                error.clear();
                String req_ = "";
                if (getAction_type().equals("update")) {
                    req_ = session.get("req_for").toString();
                    request_type = session.get("request_type").toString();
                } else {
                    form_details = (FormBean) session.get("dns_details");
                    req_ = form_details.getReq_();
                }
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);
                System.out.println("form details migration date:::::::" + form_details.getMigration_date());
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                profile_values = (HashMap) session.get("profile-values");
                if (ldap_employee) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
                }

                if (form_details.getRequest_mx_chk() != null) {
                } else {
                    form_details.setRequest_mx_chk("");
                }
                if (form_details.getRequest_ptr_chk() != null) {
                } else {
                    form_details.setRequest_ptr_chk("");
                }
                if (form_details.getRequest_spf_chk() != null) {
                } else {
                    form_details.setRequest_spf_chk("");
                }
                if (form_details.getRequest_srv_chk() != null) {
                } else {
                    form_details.setRequest_srv_chk("");
                }
                if (form_details.getRequest_txt_chk() != null) {
                } else {
                    form_details.setRequest_txt_chk("");
                }
                if (form_details.getRequest_dmarc_chk() != null) {
                } else {
                    form_details.setRequest_dmarc_chk("");
                }

                if (form_details.getRequest_mx() != null) {
                } else {
                    form_details.setRequest_mx("");
                }
                if (form_details.getRequest_mx1() != null) {
                } else {
                    form_details.setRequest_mx1("");
                }
                if (form_details.getRequest_ptr() != null) {
                } else {
                    form_details.setRequest_ptr("");
                }
                if (form_details.getRequest_ptr1() != null) {
                } else {
                    form_details.setRequest_ptr1("");
                }

                if (form_details.getRequest_spf() != null) {
                } else {
                    form_details.setRequest_spf("");
                }
                if (form_details.getRequest_srv() != null) {
                } else {
                    form_details.setRequest_srv("");
                }
                if (form_details.getRequest_txt() != null) {
                } else {
                    form_details.setRequest_txt("");
                }
                if (form_details.getRequest_dmarc() != null) {
                } else {
                    form_details.setRequest_dmarc("");
                }

                boolean employment_error = valid.EmploymentValidation(form_details.getUser_employment());

                if (employment_error == true) {
                    error.put("employment_error", "Please enter Employment in correct format");
                } else if (form_details.getUser_employment().equals("Central")) {
                    if (form_details.getMin() == null) {
                        error.put("ministry_error", "Please enter Ministry in correct format");
                    } else if (form_details.getDept() == null) {
                        error.put("dept_error", "Please enter Department in correct format");
                    } else {
                        boolean ministry_error = valid.MinistryValidation(form_details.getMin());
                        boolean dept_error = valid.MinistryValidation(form_details.getDept());
                        if (ministry_error == true) {
                            error.put("ministry_error", "Please enter Ministry in correct format");
                        }
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("ministry_error", "Please enter Ministry in correct format");

                        }
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
                        }

                        List deplist = profileService.getCentralDept(form_details.getMin());
                        if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!deplist.contains(form_details.getDept())) {
                                error.put("dept_error", "Please enter Department in correct format");
                            }
                        }
                        if (form_details.getDept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {

                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("State")) {
                    if (form_details.getState_dept() == null) {
                        error.put("state_dept_error", "Please enter State Department in correct format");
                    } else if (form_details.getStateCode() == null) {
                        error.put("state_error", "Please enter State in correct format");
                    } else {

                        boolean state_dept_error = valid.MinistryValidation(form_details.getState_dept());
                        boolean state_error = valid.nameValidation(form_details.getStateCode());
                        if (state_dept_error == true) {
                            error.put("state_dept_error", "Please enter State Department in correct format");
                        }
                        if (state_error == true) {
                            error.put("state_error", "Please enter State in correct format");
                        }

                        List statedeplist = profileService.getCentralDept(form_details.getStateCode());
                        System.out.println("statedeplist" + statedeplist);
                        if (!form_details.getState_dept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!statedeplist.contains(form_details.getState_dept())) {
                                error.put("state_dept_error", "Please enter correct State Department");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getState_dept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                    if (form_details.getOrg() == null) {
                        error.put("org_error", "Please enter Organization in correct format");
                    } else {
                        boolean org_error = valid.MinistryValidation(form_details.getOrg());
                        if (org_error == true) {
                            error.put("org_error", "Please enter Organization in correct format");
                        }

                        List orglist = profileService.getCentralMinistry(form_details.getUser_employment());
                        System.out.println("orglist@@@@@@@@" + orglist);
                        if (!form_details.getOrg().toLowerCase().equalsIgnoreCase("other")) {
                            if (!orglist.contains(form_details.getOrg())) {
                                error.put("org_error", "Please enter correct department");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getOrg().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                }

                if (form_details.getUser_empcode() == "" || form_details.getUser_empcode() == null) {
                    error.put("emp_code_error", "Enter Employee code");
                } else if (form_details.getUser_empcode() != null || !form_details.getUser_empcode().equals("")) {
                    boolean emp_code_error = valid.dnsemploycodevalidation(form_details.getUser_empcode());
                    if (emp_code_error == true) {
                        error.put("emp_code_error", "Please Enter Employee code in correct format");
                    }

                }

                if (form_details.getModule() == null) {
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.roTelValidation(form_details.getHod_tel());
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Department Name [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (form_details.getHod_mobile().trim().startsWith("+")) {
                        if ((hod_mobile_error == true) && (form_details.getHod_mobile().trim().startsWith("+91"))) {
                            error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                        } else if ((hod_mobile_error == true) && (!form_details.getHod_mobile().trim().startsWith("+91"))) {
                            error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
                        } else {

                        }
                    } else {

                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                    }
                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                    }
                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                        error.put("hod_email_error", "Please change it in your profile and then come back");

                    }
                    if (hod_telephone_error == true) {
                        error.put("hod_telephone_error", "Please enter Reporting/Nodal/Forwarding Officer Telephone in correct format");
                    }
                    if (hod_email_error == true) {
                        error.put("hod_email_error", "Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]");
                    } else // check for nic domain
                     if (form_details.getHod_email().equals(form_details.getUser_email())) {
                            error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                        } else {
                            Set s = (Set) userdata.getAliases();
                            System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);
                            ArrayList<String> newAr = new ArrayList<String>();
                            newAr.addAll(s);
                            ArrayList email_aliases = null;
                            email_aliases = newAr;
                            if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                                // b = (ArrayList) userdata.getAliases();
                                if (email_aliases.contains("mdlmail@nic.in") && form_details.getHod_email().contains("@mazdock.com")) {
                                    error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                                } else if (email_aliases.contains(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                                } else //String user_bo = session.get("user_bo").toString();
                                 if (nic_employee && nicemployeeeditable.equals("no")) {

                                        map = db.fetchUserValuesFromOAD(userdata.getUid());
                                        if (map.size() > 0) {
                                            System.out.println("map.get(\"email\")" + map.get("email") + "form_details.getHod_email()" + form_details.getHod_email());
                                            if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                                error.put("hod_email_error", "Reporting officer details can not be changed.");

                                            } else {
                                                String e = form_details.getHod_email();
                                                String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                                if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                                } else {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                    error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                                }
                                            }
                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }

                                        }
                                    } else {
                                        // DONE BY NIKKI MEENAXI ON 16 feb 
                                        //  hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                    }
                            } else //String user_bo = session.get("user_bo").toString();
                             if (nic_employee && nicemployeeeditable.equals("no")) {
                                    System.out.println("map.get(\"email\")" + map.get("email") + "form_details.getHod_email()" + form_details.getHod_email());
                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");

                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }

                                    }
                                } else {
                                    // DONE BY NIKKI MEENAXI ON 16 feb 
                                    // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                }
                        }

                    if (ldap_employee) {

                        System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                        if (hodDetails.get("bo").toString().equals("no bo")) {
                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        }

                    }
                }
                // end validate profile 20th march

                // start, code added by pr on 22ndjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();
                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                }

                if (getAction_type().equals("confirm")) {
                    if (!error.isEmpty()) {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                        dns_url = "";
                        dns_oldip = "";
                        dns_newip = "";
                        dns_cname = "";
                        dns_loc = "";
                        ReturnString = "";
                    } else {
                        profile_values = (HashMap) session.get("profile-values");
                        String ref_num = "";
                        System.out.println("req_________ in registraion::" + req_);
                        if (session.get("update_without_oldmobile").equals("no")) {
                            if (request_type.equals("dns_bulk")) {
                                form_details.setRequest_type("dns_bulk");
                                String filename = "";
                                String upload_filename = "";
                                String rename_filename = "";
                                if (session.get("uploaded_filename") != null && !session.get("uploaded_filename").toString().isEmpty()) {
                                    filename = session.get("filetodownload").toString() + "-valid.csv";
                                    upload_filename = session.get("uploaded_filename").toString();
                                    rename_filename = session.get("renamed_filepath").toString();
                                }
                                form_details.setReq_(session.get("req_").toString());
                                form_details.setReq_other_add(session.get("req_other_add").toString());
                                campaignId = (int) session.get("campaign_id");

                                ref_num = dnsservice.DNS_tab2(form_details, profile_values, filename, upload_filename, rename_filename, campaignId);
                            } else {
                                System.out.println("form_details###########3333" + form_details.getMigration_date());
                                ref_num = dnsservice.DNS_tab2(form_details, profile_values, "", "", "", 0);
                            }
                        }

                        if (ref_num != null || !ref_num.isEmpty()) {
                            int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                            System.out.println("email :::::::" + form_details.getUser_email());
                            System.out.println("mobile :::::::" + form_details.getUser_mobile());
                            if (i > 0) {
                                profile_values.put("user_employment", form_details.getUser_employment().trim());
                                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                                    profile_values.put("min", form_details.getMin().trim());
                                    profile_values.put("dept", form_details.getDept().trim());
                                } else if (form_details.getUser_employment().trim().equals("State")) {
                                    profile_values.put("dept", form_details.getState_dept().trim());
                                    profile_values.put("stateCode", form_details.getStateCode().trim());
                                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                                    profile_values.put("Org", form_details.getOrg().trim());
                                }
                                profile_values.put("other_dept", form_details.getOther_dept().trim());
                                profile_values.put("ca_name", form_details.getHod_name().trim());
                                profile_values.put("ca_design", form_details.getCa_design().trim());
                                if (form_details.getHod_mobile().trim().startsWith("+91")) {
                                    profile_values.put("ca_mobile", form_details.getHod_mobile().trim());
                                } else {
                                    profile_values.put("ca_mobile", "+91" + form_details.getHod_mobile().trim());
                                }
                                profile_values.put("ca_email", form_details.getHod_email().trim());
                                profile_values.put("hod_name", form_details.getHod_name().trim());
                                if (form_details.getHod_mobile().trim().startsWith("+91")) {
                                    profile_values.put("hod_mobile", form_details.getHod_mobile().trim());
                                } else {
                                    profile_values.put("hod_mobile", "+91" + form_details.getHod_mobile().trim());
                                }
                                profile_values.put("hod_email", form_details.getHod_email().trim());
                                profile_values.put("hod_tel", form_details.getHod_tel().trim());
                            }
                        }
                        session.put("dns_details", form_details);
                        session.put("ref_num", ref_num);
                        session.remove("req_");
                        session.remove("req_other_add");
                    }
                } else if (getAction_type().equals("validate")) {
                    if (!error.isEmpty()) {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                        dns_url = "";
                        dns_oldip = "";
                        dns_newip = "";
                        dns_cname = "";
                        dns_loc = "";
                        ReturnString = "";
                    }
                } else if (getAction_type().equals("update")) // else if added by pr on 6thfeb18
                {
                    System.out.println("inside update##################");
                    if (error.isEmpty()) {
                        String ref = session.get("ref").toString();
                        //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                        String admin_role = "NA";
                        if (session.get("admin_role") != null) {
                            admin_role = session.get("admin_role").toString();
                        }

                        Boolean flag = true;
                        if (request_type.equals("dns_bulk")) {
                            form_details.setRequest_type("dns_bulk");
                            form_details.setReq_(session.get("req_").toString());
                            form_details.setReq_other_add(session.get("req_other_add").toString());
                            String filename = session.get("filetodownload").toString() + "-valid.csv";
                            flag = updateService.prevUpdateService(form_details, ref, admin_role, filename, userdata.getEmail(), userdata.getAliases());
                        } else {
                            form_details.setDns_url(dns_url);
                            form_details.setDns_cname(dns_cname);
                            form_details.setDns_newip(dns_newip);
                            form_details.setDns_oldip(dns_oldip);
                            flag = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                        }

                        if (flag && form_details.getModule().equals("user")) {
                            profile_values = (HashMap) session.get("profile-values");
                            form_details.setHod_email(profile_values.get("hod_email").toString());
                            form_details.setHod_name(profile_values.get("hod_name").toString());
                            form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
                            form_details.setHod_tel(profile_values.get("hod_tel").toString());
                            form_details.setCa_design(profile_values.get("ca_design").toString());
                            int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                            if (i > 0) {
                                profile_values.put("user_employment", form_details.getUser_employment().trim());
                                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                                    profile_values.put("min", form_details.getMin().trim());
                                    profile_values.put("dept", form_details.getDept().trim());
                                } else if (form_details.getUser_employment().trim().equals("State")) {
                                    profile_values.put("dept", form_details.getState_dept().trim());
                                    profile_values.put("stateCode", form_details.getStateCode().trim());
                                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                                    profile_values.put("Org", form_details.getOrg().trim());
                                }
                                profile_values.put("other_dept", form_details.getOther_dept().trim());
                            }
                        }
                    } else {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                    dns_url = "";
                    dns_oldip = "";
                    dns_newip = "";
                    dns_cname = "";
                    dns_loc = "";
                    ReturnString = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                data = "";
                CSRFRandom = "";
                action_type = "";
                dns_url = "";
                dns_oldip = "";
                dns_newip = "";
                dns_cname = "";
                dns_loc = "";
                ReturnString = "";
            }
        } else {
            try {
                db = new Database();
                UserData userdata = (UserData) session.get("uservalues");
                boolean ldap_employee = userdata.isIsEmailValidated();
                boolean nic_employee = userdata.isIsNICEmployee();
                HodData hoddata = (HodData) userdata.getHodData();
                String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

                error.clear();
                String req_ = "";
                if (getAction_type().equals("update")) {
                    req_ = session.get("req_for").toString();
                    request_type = session.get("request_type").toString();
                } else {

                    form_details = (FormBean) session.get("dns_details");
                    req_ = form_details.getReq_();
                }
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                form_details = mapper.readValue(data, FormBean.class);
                System.out.println("form details migration date:::::::" + form_details.getMigration_date());
                hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                profile_values = (HashMap) session.get("profile-values");
                if (ldap_employee) {
                    profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                    form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
                }

                System.out.println("dns_cname@@@@@@" + dns_cname);
                form_details.setDns_url(dns_url);
                form_details.setDns_cname(dns_cname);
                form_details.setDns_newip(dns_newip);
                form_details.setDns_oldip(dns_oldip);
                form_details.setReq_(req_);
                form_details.setRequest_type(request_type);
                if (form_details.getRequest_mx_chk() != null) {
                } else {
                    form_details.setRequest_mx_chk("");
                }
                if (form_details.getRequest_ptr_chk() != null) {
                } else {
                    form_details.setRequest_ptr_chk("");
                }
                if (form_details.getRequest_spf_chk() != null) {
                } else {
                    form_details.setRequest_spf_chk("");
                }
                if (form_details.getRequest_srv_chk() != null) {
                } else {
                    form_details.setRequest_srv_chk("");
                }
                if (form_details.getRequest_txt_chk() != null) {
                } else {
                    form_details.setRequest_txt_chk("");
                }
                if (form_details.getRequest_dmarc_chk() != null) {
                } else {
                    form_details.setRequest_dmarc_chk("");
                }
                if (request_type.equals("dns_single")) {
                    if (!form_details.getRequest_mx_chk().equals("mx") && !form_details.getRequest_ptr_chk().equals("ptr") && !form_details.getRequest_spf_chk().equals("spf") && !form_details.getRequest_srv_chk().equals("srv") && !form_details.getRequest_txt_chk().equals("txt") && !form_details.getRequest_dmarc_chk().equals("dmarc")) {
                        System.out.println("inside not check boxes");

                        if (form_details.getDns_url().contains(";")) {
                            String[] dnsurl = form_details.getDns_url().split(";");

                            if (Arrays.toString(dnsurl).equals("[]")) {
                                error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                            } else if (!stat_type.equals("api") && !stat_type.equals("domainapi")) {
                                for (String url : dnsurl) {
                                    if (url.startsWith("www.")) {
                                        String urls[] = url.split("www.");
                                        url = urls[1];
                                    }
                                    int count_char = countChar(url, '.');
                                    if (count_char >= 3) {
                                        String[] a = url.split(".");
                                        int lastIndex, secondLastIndex;
                                        lastIndex = url.lastIndexOf('.');
                                        secondLastIndex = url.lastIndexOf('.', lastIndex - 1);
                                        String d = url.substring(secondLastIndex + 1, lastIndex) + url.substring(lastIndex, url.length());
                                        if (d.equals("gov.in")) {

                                            error.put("dns_url_error", "Please come through from registry.gov.in.");
                                            break;
                                        }
                                    }
                                }
                            }
                        } else if (!stat_type.equals("api") && !stat_type.equals("domainapi")) {
                            if (form_details.getDns_url().startsWith("www.")) {
                                String urls[] = form_details.getDns_url().split("www.");
                                form_details.setDns_url(urls[1]);
                            }
                            int count_char = countChar(form_details.getDns_url(), '.');
                            if (count_char >= 3) {
                                String[] a = form_details.getDns_url().split(".");
                                int lastIndex, secondLastIndex;
                                lastIndex = form_details.getDns_url().lastIndexOf('.');
                                secondLastIndex = form_details.getDns_url().lastIndexOf('.', lastIndex - 1);
                                String d = form_details.getDns_url().substring(secondLastIndex + 1, lastIndex) + form_details.getDns_url().substring(lastIndex, form_details.getDns_url().length());
                                if (d.equals("gov.in")) {
                                    error.put("dns_url_error", "Please come through from registry.gov.in.");
                                }
                            }
                        }
                    }
                    if (form_details.getReq_().equals("req_modify")) {
                        if (form_details.getDns_oldip().contains(";")) {
                            String[] dnsoldip = form_details.getDns_oldip().split(";");
                            if (Arrays.toString(dnsoldip).equals("[]")) {
                                error.put("dns_old_ip_error", "Enter the OLD IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_oldip()));
                            } else {
                                for (String ip : dnsoldip) {
                                    boolean dns_old_ip_error = valid.dnsmodifyipvalidation(ip);
                                    if (dns_old_ip_error == true) {
                                        error.put("dns_old_ip_error", "Enter the OLD IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                        form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_oldip()));
                                    }
                                }
                            }
                        } else {
                            boolean dns_old_ip_error = valid.dnsmodifyipvalidation(form_details.getDns_oldip());
                            if (dns_old_ip_error == true) {
                                error.put("dns_old_ip_error", "Enter the OLD IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_oldip()));
                            }
                        }

                        if (!form_details.getRequest_mx_chk().equals("mx") && !form_details.getRequest_ptr_chk().equals("ptr") && !form_details.getRequest_spf_chk().equals("spf") && !form_details.getRequest_srv_chk().equals("srv") && !form_details.getRequest_txt_chk().equals("txt") && !form_details.getRequest_dmarc_chk().equals("dmarc")) {
                            if (form_details.getDns_newip().contains(";")) {
                                String[] dnsnewip = form_details.getDns_newip().split(";");

                                if (Arrays.toString(dnsnewip).equals("[]")) {
                                    error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                    form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                } else {
                                    for (String ip : dnsnewip) {
                                        boolean dns_new_ip_error = valid.dnsipvalidation(ip);
                                        if (dns_new_ip_error == true) {
                                            error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                            form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                        }
                                    }
                                }
                            } else {
                                boolean dns_new_ip_error = valid.dnsipvalidation(form_details.getDns_newip());
                                if (dns_new_ip_error == true) {
                                    error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                    form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                }
                            }
                        } else if (form_details.getDns_newip().contains(";")) {
                            String[] dnsnewip = form_details.getDns_newip().split(";");

                            if (Arrays.toString(dnsnewip).equals("[]")) {
                                error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                            } else {
                                for (String ip : dnsnewip) {
                                    boolean dns_new_ip_error = valid.dnsipvalidation(ip);
                                    if (dns_new_ip_error == true) {
                                        error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                        form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                    }
                                }
                            }
                        } else {
                            boolean dns_new_ip_error = valid.dnsipvalidation(form_details.getDns_newip());
                            if (dns_new_ip_error == true) {
                                error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                            }
                        }
                        if (error.isEmpty()) {
                            if (form_details.getDns_newip().contains(";")) {
                                ArrayList dnsnewip = new ArrayList(Arrays.asList(dns_newip.split(";")));
                                for (int j = 0; j < dnsnewip.size(); j++) {
                                    if (!dnsnewip.get(j).toString().isEmpty()) {
                                        if (dnsnewip.get(j).toString().startsWith("164.100") || dnsnewip.get(j).toString().startsWith("14.139") || dnsnewip.get(j).toString().startsWith("180.149")) {
                                            ReturnString = checkIP(dnsnewip.get(j).toString());
                                            if (!"".equals(ReturnString)) {
                                                error.put("dns_new_ip_error", "Third level domain (" + ReturnString + ") already exists with ip (" + dnsnewip.get(j) + ")");
                                                //error.put("dns_new_ip_error", "Domain already exist with this ip please check (row:" + j + 1 + ")");
                                            }
                                        }

                                    }
                                }
                            } else if (!form_details.getDns_newip().isEmpty()) {
                                if (form_details.getDns_newip().startsWith("164.100") || form_details.getDns_newip().startsWith("14.139") || form_details.getDns_newip().startsWith("180.149")) {
                                    ReturnString = checkIP(form_details.getDns_newip());
                                    if (!"".equals(ReturnString)) {
                                        error.put("dns_new_ip_error", "Third level domain (" + ReturnString + ") already exists with this ip (" + form_details.getDns_newip() + ")");
                                    }
                                }
                            }
                        }
                    } else if (form_details.getReq_().equals("req_new")) {
                        if (!stat_type.equals("api") && !stat_type.equals("domainapi")) {
                            if (!form_details.getRequest_mx_chk().equals("mx") && !form_details.getRequest_ptr_chk().equals("ptr") && !form_details.getRequest_spf_chk().equals("spf") && !form_details.getRequest_srv_chk().equals("srv") && !form_details.getRequest_txt_chk().equals("txt") && !form_details.getRequest_dmarc_chk().equals("dmarc")) {
                                if (form_details.getDns_newip().contains(";")) {
                                    String[] dnsnewip = form_details.getDns_newip().split(";");

                                    if (Arrays.toString(dnsnewip).equals("[]")) {
                                        //error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                        //form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                    } else {
                                        for (String ip : dnsnewip) {
                                            boolean dns_new_ip_error = valid.dnsmodifyipvalidation(ip);
                                            if (dns_new_ip_error == true) {
                                                error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                                form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                            }
                                        }
                                    }
                                } else {
                                    boolean dns_new_ip_error = valid.dnsmodifyipvalidation(form_details.getDns_newip());
                                    if (dns_new_ip_error == true) {
                                        error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                        form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                    }
                                }
                            } else if (form_details.getDns_newip().contains(";")) {
                                String[] dnsnewip = form_details.getDns_newip().split(";");

                                if (Arrays.toString(dnsnewip).equals("[]")) {
                                    error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                    form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                } else {
                                    for (String ip : dnsnewip) {
                                        boolean dns_new_ip_error = valid.dnsmodifyipvalidation(ip);
                                        if (dns_new_ip_error == true) {
                                            error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                            form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                        }
                                    }
                                }
                            } else {
                                boolean dns_new_ip_error = valid.dnsmodifyipvalidation(form_details.getDns_newip());
                                if (dns_new_ip_error == true) {
                                    error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                    form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                }
                            }

                            if (error.isEmpty()) {
                                if (form_details.getDns_newip().contains(";")) {
                                    String[] dnsnewip = form_details.getDns_newip().split(";");
                                    for (String ip : dnsnewip) {
                                        if (!ip.isEmpty()) {
                                            ReturnString = checkIP(ip);
                                            if (ip.startsWith("164.100") || ip.startsWith("14.139") || ip.startsWith("180.149")) {
                                                if (!"".equals(ReturnString)) {
                                                    error.put("dns_new_ip_error", "Third level domain (" + ReturnString + ") already exists with this ip (" + ip + ").");
                                                }
                                            }
                                        }
                                    }
                                } else if (!form_details.getDns_newip().isEmpty()) {
                                    if (form_details.getDns_newip().startsWith("164.100") || form_details.getDns_newip().startsWith("14.139") || form_details.getDns_newip().startsWith("180.149")) {
                                        ReturnString = checkIP(form_details.getDns_newip());
                                        if (!"".equals(ReturnString)) {
                                            error.put("dns_new_ip_error", "Third level domain(" + ReturnString + ") already exists with this ip(" + form_details.getDns_newip() + ").");
                                        }
                                    }
                                }
                            }
                        }
                    } else if (form_details.getReq_().equals("req_delete")) {
                        if (form_details.getDns_newip().contains(";")) {
                            String[] dnsnewip = form_details.getDns_newip().split(";");
                            if (Arrays.toString(dnsnewip).equals("[]")) {
                                error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                            } else {
                                for (String ip : dnsnewip) {
                                    boolean dns_new_ip_error = valid.dnsipvalidation(ip);
                                    if (dns_new_ip_error == true) {
                                        error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                        form_details.setDns_newip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                                    }
                                }
                            }
                        } else {
                            boolean dns_new_ip_error = valid.dnsipvalidation(form_details.getDns_newip());
                            if (dns_new_ip_error == true) {
                                error.put("dns_new_ip_error", "Enter the NEW IP Address IPV4 [e.g.: 164.100.X.X ] and IPV6 Address");
                                form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(form_details.getDns_newip()));
                            }
                        }
                    }

                    if (form_details.getDns_url().contains(";")) {
                        String d = "";
                        String[] dnsurl = form_details.getDns_url().split(";");
                        if (Arrays.toString(dnsurl).equals("[]")) {
                            error.put("dns_url_error", "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]");
                            form_details.setDns_url(ESAPI.encoder().encodeForHTML(form_details.getDns_url()));
                        } else if (form_details.getDns_url().endsWith(";")) {
                            error.put("dns_url_error", "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]");
                            form_details.setDns_url(ESAPI.encoder().encodeForHTML(form_details.getDns_url()));
                        } else {
                            String url_after_w3 = "";
                            int dotCount = 0;
                            for (String url : dnsurl) {

                                if (url.startsWith("www.")) {
                                    String urls[] = url.split("www.");
                                    url_after_w3 = urls[1];

                                }
                                if (form_details.getReq_().equals("req_new") || form_details.getReq_().equals("req_cname")) {
                                    if (!form_details.getRequest_mx_chk().equals("mx") && !form_details.getRequest_ptr_chk().equals("ptr") && !form_details.getRequest_spf_chk().equals("spf") && !form_details.getRequest_srv_chk().equals("srv") && !form_details.getRequest_txt_chk().equals("txt") && !form_details.getRequest_dmarc_chk().equals("dmarc")) {
                                        dotCount = countChar(url_after_w3, '.');
                                        if (dotCount <= 2) {
                                            ReturnString = checkDomain(url_after_w3);
                                            if (ReturnString.startsWith("164.100") || ReturnString.startsWith("14.139") || ReturnString.startsWith("180.149") || ReturnString.startsWith("10.")) {
                                                if (form_details.getReq_().equals("req_new")) {
                                                    if (ReturnString != "") {
                                                        error.put("dns_url_error", "Ip already exists with domain [" + url_after_w3 + " ],Please apply for add CNAME");
                                                        flag_url_exist = true;
                                                    }
                                                }

                                            }
                                            if (form_details.getReq_().equals("req_cname")) {
                                                if (ReturnString.equals("")) {
                                                    error.put("dns_url_error", url_after_w3 + " domain does not exist.");

                                                }
                                                //else if (!ReturnString.equals("") && url.startsWith("www.")) {
//                                            flag_url_exist = true;
//                                        }
                                            }
                                        }
                                    }
                                }
                                if (flag_url_exist) {
                                    break;
                                }

                            }
                        }
                    } else {
                        boolean dns_url_error = valid.dnsurlvalidation(form_details.getDns_url());
                        if (form_details.getReq_().equals("req_new") || form_details.getReq_().equals("req_cname")) {
                            if (!form_details.getRequest_mx_chk().equals("mx") && !form_details.getRequest_ptr_chk().equals("ptr") && !form_details.getRequest_spf_chk().equals("spf") && !form_details.getRequest_srv_chk().equals("srv") && !form_details.getRequest_txt_chk().equals("txt") && !form_details.getRequest_dmarc_chk().equals("dmarc")) {
                                String url = "";
                                int dotCount = 0;
                                if (form_details.getDns_url().startsWith("www.")) {
                                    String urls[] = form_details.getDns_url().split("www.");
                                    url = urls[1];
                                } else {
                                    url = form_details.getDns_url();
                                }
                                dotCount = countChar(url, '.');
                                if (dotCount <= 2) {
                                    System.out.println("url for single::::: " + form_details.getDns_url());
                                    ReturnString = checkDomain(url);
                                    if (ReturnString.startsWith("164.100") || ReturnString.startsWith("14.139") || ReturnString.startsWith("180.149") || ReturnString.startsWith("10.")) {
                                        if (form_details.getReq_().equals("req_new")) {
                                            if (ReturnString != "") {
                                                error.put("dns_url_error", "Ip already exists with domain [" + url + " ] Please apply for add CNAME ");
                                                flag_url_exist = true;
                                            }
                                        }

                                    }
                                    if (form_details.getReq_().equals("req_cname")) {
                                        if (ReturnString.equals("")) {
                                            error.put("dns_url_error", url + " domain does not exist.");
                                            flag_url_exist = true;

                                        }
//                                else if (!ReturnString.equals("") && form_details.getDns_url().startsWith("www.")) {
//                                    flag_url_exist = true;
//
//                                }
//                                
                                    }
                                }
                            }
                        }
                        if (dns_url_error == true) {
                            error.put("dns_url_error", "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]");
                            form_details.setDns_url(ESAPI.encoder().encodeForHTML(form_details.getDns_url()));
                        }
                    }
                    if (form_details.getDns_cname().contains(";")) {
                        String[] dnscname = form_details.getDns_cname().split(";");
                        if (Arrays.toString(dnscname).equals("[]")) {
                            if (form_details.getReq_().equals("req_cname")) {
                                error.put("dns_cname_error", "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]");
                                form_details.setDns_cname(ESAPI.encoder().encodeForHTML(form_details.getDns_cname()));
                            }
                        } else if (form_details.getDns_cname().endsWith(";") && form_details.getReq_().equals("req_cname")) {
                            error.put("dns_cname_error", "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]");
                            form_details.setDns_url(ESAPI.encoder().encodeForHTML(form_details.getDns_url()));
                        } else {
                            for (String cname : dnscname) {
                                boolean dns_cname_error = valid.cnamevalidation(cname, form_details.getReq_());
                                if (dns_cname_error == true) {
                                    error.put("dns_cname_error", "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]");
                                    form_details.setDns_url(ESAPI.encoder().encodeForHTML(form_details.getDns_cname()));
                                } else {
//                            System.out.println("inside else $$$$$$$$$" + form_details.getReq_() + "flag" + flag_url_exist);
//                            if (form_details.getReq_().equals("req_cname")) {
//                                if (flag_url_exist) {
//                                    if (!cname.startsWith("www.")) {
//                                        error.put("dns_cname_error", "Enter the correct CNAME [e.g.: www." + cname + "]");
//                                       
//                                    }
//
//                                }
//                            }

                                }

                            }
                        }
                    } else {
                        boolean dns_cname_error = valid.cnamevalidation(dns_cname, form_details.getReq_());
                        if (dns_cname_error == true) {
                            error.put("dns_cname_error", "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]");
                            form_details.setDns_cname(ESAPI.encoder().encodeForHTML(form_details.getDns_cname()));
                        } else {
//                    if (form_details.getReq_().equals("req_cname")) {
//
//                        if (flag_url_exist) {
//
//                            if (!form_details.getDns_cname().startsWith("www.")) {
//                                error.put("dns_cname_error", "Enter the correct CNAME [e.g.: www." + form_details.getDns_cname() + "]");
//
//                            }
//
//                        }
//                    }
                        }
                    }

                    boolean dns_loc_error = valid.addValidation(form_details.getDns_loc());
                    if (dns_loc_error == true) {
                        error.put("dns_loc_error", "Enter the DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                        form_details.setDns_loc(ESAPI.encoder().encodeForHTML(form_details.getDns_loc()));
                    }
                    //boolean emp_code_error = valid.addValidation();
                    if (dns_loc_error == true) {
                        error.put("dns_loc_error", "Enter the DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespaces] allowed");
                        form_details.setDns_loc(ESAPI.encoder().encodeForHTML(form_details.getDns_loc()));
                    }
                } else {
                    if (cert != null && cert.equals("false")) {
                        error.put("file_err", "Please upload the CSV file");
                    }
                    if (session.get("uploaded_filename") != null && session.get("renamed_filepath") != null) {
                        String uploaded_filename = session.get("uploaded_filename").toString();
                        String renamed_filepath = session.get("renamed_filepath").toString();
                        form_details.setUploaded_filename(uploaded_filename);
                        form_details.setRenamed_filepath(renamed_filepath);
                    }
                }

                if (form_details.getRequest_mx_chk() != null) {
                    if (form_details.getRequest_mx_chk().equals("mx")) {
                        boolean mx_error = valid.dnsServiceValidation(form_details.getRequest_mx());
                        if (mx_error == true) {
                            error.put("request_mx_err", "Enter MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed");
                            form_details.setRequest_mx_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_mx_chk()));
                            form_details.setRequest_mx(ESAPI.encoder().encodeForHTML(form_details.getRequest_mx()));
                        }
                        if (form_details.getReq_().equals("req_modify")) {
                            boolean mx_error1 = valid.dnsServiceValidation(form_details.getRequest_mx1());
                            if (mx_error1 == true) {
                                error.put("request_mx_err1", "Enter MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed");
                                form_details.setRequest_mx_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_mx_chk()));
                                form_details.setRequest_mx1(ESAPI.encoder().encodeForHTML(form_details.getRequest_mx1()));
                            }
                        }
                    } else {
                        form_details.setRequest_mx_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_mx_chk()));
                        form_details.setRequest_mx(ESAPI.encoder().encodeForHTML(form_details.getRequest_mx()));
                    }
                }

                if (!form_details.getReq_().equals("req_delete")) {
                    if (form_details.getRequest_ptr_chk() != null) {
                        if (form_details.getRequest_ptr_chk().equals("ptr")) {
                            boolean ptr_error = valid.dnsmodifyipvalidation(form_details.getRequest_ptr());
                            if (ptr_error == true) {
                                error.put("request_ptr_err", "Enter the IPV4/IPV6 Address");
                                form_details.setRequest_ptr_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_ptr_chk()));
                                form_details.setRequest_ptr(ESAPI.encoder().encodeForHTML(form_details.getRequest_ptr()));
                            }
                            if (form_details.getReq_().equals("req_modify")) {
                                boolean ptr_error1 = valid.dnsmodifyipvalidation(form_details.getRequest_ptr1());
                                if (ptr_error1 == true) {
                                    error.put("request_ptr_err1", "Enter the IPV4/IPV6 Address");
                                    form_details.setRequest_ptr_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_ptr_chk()));
                                    form_details.setRequest_ptr1(ESAPI.encoder().encodeForHTML(form_details.getRequest_ptr1()));
                                }
                            }
                        } else {
                            form_details.setRequest_ptr_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_ptr_chk()));
                            form_details.setRequest_ptr(ESAPI.encoder().encodeForHTML(form_details.getRequest_ptr()));
                        }
                    }

                }
                if (form_details.getRequest_spf_chk() != null) {

                    if (form_details.getRequest_spf_chk().equals("spf")) {
                        boolean spf_error = valid.dnstxtValidation(form_details.getRequest_spf());
                        if (spf_error == true) {
                            error.put("request_spf_err", "Enter valid SPF value [limit 2-300]");
                            form_details.setRequest_spf_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_spf_chk()));
                            form_details.setRequest_spf(ESAPI.encoder().encodeForHTML(form_details.getRequest_spf()));
                        }
                    } else {
                        form_details.setRequest_spf_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_spf_chk()));
                        form_details.setRequest_spf(ESAPI.encoder().encodeForHTML(form_details.getRequest_spf()));
                    }
                }
                if (form_details.getRequest_srv_chk() != null) {
                    if (form_details.getRequest_srv_chk().equals("srv")) {
                        boolean srv_error = valid.dnstxtValidation(form_details.getRequest_srv());
                        if (srv_error == true) {
                            error.put("request_srv_err", "Enter valid SRV value [limit 2-300]");
                            form_details.setRequest_srv_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_srv_chk()));
                            form_details.setRequest_srv(ESAPI.encoder().encodeForHTML(form_details.getRequest_srv()));
                        }
                    } else {
                        form_details.setRequest_srv_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_srv_chk()));
                        form_details.setRequest_srv(ESAPI.encoder().encodeForHTML(form_details.getRequest_srv()));
                    }
                }
                if (form_details.getRequest_txt_chk() != null) {
                    if (form_details.getRequest_txt_chk().equals("txt")) {
                        boolean txt_error = valid.dnstxtValidation(form_details.getRequest_txt());
                        if (txt_error == true) {
                            error.put("request_txt_err", "Enter valid TXT value [limit 2-300]");
                            form_details.setRequest_txt_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_txt_chk()));
                            form_details.setRequest_txt(ESAPI.encoder().encodeForHTML(form_details.getRequest_txt()));
                        }
                    } else {
                        form_details.setRequest_txt_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_txt_chk()));
                        form_details.setRequest_txt(ESAPI.encoder().encodeForHTML(form_details.getRequest_txt()));
                    }
                }
                if (form_details.getRequest_dmarc_chk() != null) {
                    if (form_details.getRequest_dmarc_chk().equals("dmarc")) {
                        boolean dmarc_error = valid.dnstxtValidation(form_details.getRequest_dmarc());
                        if (dmarc_error == true) {
                            error.put("request_dmarc_err", "Enter valid DMARC value [limit 2-300]");
                            form_details.setRequest_dmarc_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_dmarc_chk()));
                            form_details.setRequest_dmarc(ESAPI.encoder().encodeForHTML(form_details.getRequest_dmarc()));
                        }
                    } else {
                        form_details.setRequest_dmarc_chk(ESAPI.encoder().encodeForHTML(form_details.getRequest_dmarc_chk()));
                        form_details.setRequest_dmarc(ESAPI.encoder().encodeForHTML(form_details.getRequest_dmarc()));
                    }
                }
                // validate profile 20th march

                boolean employment_error = valid.EmploymentValidation(form_details.getUser_employment());

                if (employment_error == true) {
                    error.put("employment_error", "Please enter Employment in correct format");
                } else if (form_details.getUser_employment().equals("Central")) {
                    if (form_details.getMin() == null) {
                        error.put("ministry_error", "Please enter Ministry in correct format");
                    } else if (form_details.getDept() == null) {
                        error.put("dept_error", "Please enter Department in correct format");
                    } else {
                        boolean ministry_error = valid.MinistryValidation(form_details.getMin());
                        boolean dept_error = valid.MinistryValidation(form_details.getDept());
                        if (ministry_error == true) {
                            error.put("ministry_error", "Please enter Ministry in correct format");
                        }
                        List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                        if (!minlist.contains(form_details.getMin())) {
                            error.put("ministry_error", "Please enter Ministry in correct format");

                        }
                        if (dept_error == true) {
                            error.put("dept_error", "Please enter Department in correct format");
                        }

                        List deplist = profileService.getCentralDept(form_details.getMin());
                        if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!deplist.contains(form_details.getDept())) {
                                error.put("dept_error", "Please enter Ministry in correct format");
                            }
                        }
                        if (form_details.getDept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {

                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("State")) {
                    if (form_details.getState_dept() == null) {
                        error.put("state_dept_error", "Please enter State Department in correct format");
                    } else if (form_details.getStateCode() == null) {
                        error.put("state_error", "Please enter State in correct format");
                    } else {

                        boolean state_dept_error = valid.MinistryValidation(form_details.getState_dept());
                        boolean state_error = valid.nameValidation(form_details.getStateCode());
                        if (state_dept_error == true) {
                            error.put("state_dept_error", "Please enter State Department in correct format");
                        }
                        if (state_error == true) {
                            error.put("state_error", "Please enter State in correct format");
                        }

                        List statedeplist = profileService.getCentralDept(form_details.getStateCode());
                        System.out.println("statedeplist" + statedeplist);
                        if (!form_details.getState_dept().toLowerCase().equalsIgnoreCase("other")) {
                            if (!statedeplist.contains(form_details.getState_dept())) {
                                error.put("state_dept_error", "Please enter correct State Department");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getState_dept().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                    if (form_details.getOrg() == null) {
                        error.put("org_error", "Please enter Organization in correct format");
                    } else {
                        boolean org_error = valid.MinistryValidation(form_details.getOrg());
                        if (org_error == true) {
                            error.put("org_error", "Please enter Organization in correct format");
                        }

                        List orglist = profileService.getCentralMinistry(form_details.getUser_employment());
                        System.out.println("orglist@@@@@@@@" + orglist);
                        if (!form_details.getOrg().toLowerCase().equalsIgnoreCase("other")) {
                            if (!orglist.contains(form_details.getOrg())) {
                                error.put("org_error", "Please enter correct department");
                                // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                            }
                        }
                        if (form_details.getOrg().toLowerCase().equals("other")) {
                            if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                                boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                                if (dept_other_error == true) {
                                    error.put("dept_other_error", "Please enter Other Department in correct format");
                                }
                            } else {
                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                            }
                        }
                    }
                }

                if (form_details.getUser_empcode() == "" || form_details.getUser_empcode() == null) {
                    error.put("emp_code_error", "Enter Employee code");
                } else if (form_details.getUser_empcode() != null || !form_details.getUser_empcode().equals("")) {
                    boolean emp_code_error = valid.dnsemploycodevalidation(form_details.getUser_empcode());
                    if (emp_code_error == true) {
                        error.put("emp_code_error", "Please Enter Employee code in correct format");
                    }

                }

                if (form_details.getModule() == null) {
                    boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                    boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                    boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                    boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                    boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());
                    if (ca_desg_error == true) {
                        error.put("ca_desg_error", "Enter Department Name [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                    }
                    if (hod_name_error == true) {
                        error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                    }
                    if (form_details.getHod_mobile().trim().startsWith("+")) {
                        if ((hod_mobile_error == true) && (form_details.getHod_mobile().trim().startsWith("+91"))) {
                            error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                        } else if ((hod_mobile_error == true) && (!form_details.getHod_mobile().trim().startsWith("+91"))) {
                            error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
                        } else {

                        }
                    } else {

                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                    }
                    if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                        error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                    }
                    if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                        error.put("hod_email_error", "Please change it in your profile and then come back");

                    }
                    if (hod_telephone_error == true) {
                        error.put("hod_telephone_error", "Please enter Reporting/Nodal/Forwarding Officer Telephone in correct format");
                    }
                    if (hod_email_error == true) {
                        error.put("hod_email_error", "Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]");
                    } else // check for nic domain
                     if (form_details.getHod_email().equals(form_details.getUser_email())) {
                            error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                        } else {
                            Set s = (Set) userdata.getAliases();
                            System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);
                            ArrayList<String> newAr = new ArrayList<String>();
                            newAr.addAll(s);
                            ArrayList email_aliases = null;
                            email_aliases = newAr;
                            if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                                // b = (ArrayList) userdata.getAliases();
                                if (email_aliases.contains("mdlmail@nic.in") && form_details.getHod_email().contains("@mazdock.com")) {
                                    error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                                } else if (email_aliases.contains(form_details.getHod_email())) {
                                    error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                                } else //String user_bo = session.get("user_bo").toString();
                                 if (nic_employee && nicemployeeeditable.equals("no")) {

                                        map = db.fetchUserValuesFromOAD(userdata.getUid());
                                        if (map.size() > 0) {
                                            System.out.println("map.get(\"email\")" + map.get("email") + "form_details.getHod_email()" + form_details.getHod_email());
                                            if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                                error.put("hod_email_error", "Reporting officer details can not be changed.");

                                            } else {
                                                String e = form_details.getHod_email();
                                                String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                                if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                                } else {
                                                    System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                    error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                                }
                                            }
                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }

                                        }
                                    } else {
                                        // DONE BY NIKKI MEENAXI ON 16 feb 
                                        //  hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                    }
                            } else //String user_bo = session.get("user_bo").toString();
                             if (nic_employee && nicemployeeeditable.equals("no")) {
                                    System.out.println("map.get(\"email\")" + map.get("email") + "form_details.getHod_email()" + form_details.getHod_email());
                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");

                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }

                                    }
                                } else {
                                    // DONE BY NIKKI MEENAXI ON 16 feb 
                                    // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                }
                        }

                    if (ldap_employee) {

                        System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                        if (hodDetails.get("bo").toString().equals("no bo")) {
                            error.put("hod_email_error", "Reporting officer should be govt employee");
                        }

                    }
                }
                // end validate profile 20th march

                // start, code added by pr on 22ndjan18
                String SessionCSRFRandom = session.get("CSRFRandom").toString();
                if (!SessionCSRFRandom.equals(CSRFRandom)) {
                    error.put("csrf_error", "Invalid Security Token !");
                }
                // end, code added by pr on 22ndjan18
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR in dns controllerrrrrrrrrrrrrrr tab 2: " + error);

                if (getAction_type().equals("validate")) {
                    if (!error.isEmpty()) {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                        dns_url = "";
                        dns_oldip = "";
                        dns_newip = "";
                        dns_cname = "";
                        dns_loc = "";
                        ReturnString = "";
                    }
                } else if (getAction_type().equals("update")) // else if added by pr on 6thfeb18
                {
                    if (error.isEmpty()) {
                        String ref = session.get("ref").toString();
                        //Boolean  flag = mobileservice.Mobile_tab3(form_details , ref);
                        String admin_role = "NA";
                        if (session.get("admin_role") != null) {
                            admin_role = session.get("admin_role").toString();
                        }

                        form_details.setDns_url(dns_url);
                        form_details.setDns_cname(dns_cname);
                        form_details.setDns_newip(dns_newip);
                        form_details.setDns_oldip(dns_oldip);

                        Boolean flag = true;
                        if (request_type.equals("dns_bulk")) {
                            String filename = session.get("filetodownload").toString() + "-valid.csv";

                            flag = updateService.prevUpdateService(form_details, ref, admin_role, filename, userdata.getEmail(), userdata.getAliases());

                        } else {
                            flag = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());
                        }

                        if (flag && form_details.getModule().equals("user")) {
                            profile_values = (HashMap) session.get("profile-values");
                            form_details.setHod_email(profile_values.get("hod_email").toString());
                            form_details.setHod_name(profile_values.get("hod_name").toString());
                            form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
                            form_details.setHod_tel(profile_values.get("hod_tel").toString());
                            form_details.setCa_design(profile_values.get("ca_design").toString());
                            int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                            if (i > 0) {
                                profile_values.put("user_employment", form_details.getUser_employment().trim());
                                if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                                    profile_values.put("min", form_details.getMin().trim());
                                    profile_values.put("dept", form_details.getDept().trim());
                                } else if (form_details.getUser_employment().trim().equals("State")) {
                                    profile_values.put("dept", form_details.getState_dept().trim());
                                    profile_values.put("stateCode", form_details.getStateCode().trim());
                                } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                                    profile_values.put("Org", form_details.getOrg().trim());
                                }
                                profile_values.put("other_dept", form_details.getOther_dept().trim());
                            }
                        }
                    } else {
                        data = "";
                        CSRFRandom = "";
                        action_type = "";
                    }
                } else {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                    dns_url = "";
                    dns_oldip = "";
                    dns_newip = "";
                    dns_cname = "";
                    dns_loc = "";
                    ReturnString = "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
                data = "";
                CSRFRandom = "";
                action_type = "";
                dns_url = "";
                dns_oldip = "";
                dns_newip = "";
                dns_cname = "";
                dns_loc = "";
                ReturnString = "";
            }

        }
        return SUCCESS;
    }

    public String DNS_tab3() {
        try {
            error.clear();
            bulkData.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setRequest_type("dns_bulk");
            session.put("req_", form_details.getReq_());
            session.put("req_other_add", form_details.getReq_other_add());
            if (session.get("populatedList") != null) {
                cert = null;
            } else if (cert != null) {
                if (!cert.isEmpty() && !cert.equals("true")) {
                    error.put("file_err", "Please upload the CSV file");
                }
            }

            String capcode = (String) session.get("captcha");
            if (!form_details.getImgtxt().equals(capcode)) {
                error.put("cap_error", "Please enter Correct Captcha");
                form_details.setImgtxt(ESAPI.encoder().encodeForHTML(form_details.getImgtxt()));
            }

            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "error is in dns tab3::::" + error);
            String uploaded_filename = "";
            String renamed_filepath = "";
            String refFilename = "";
            if (error.isEmpty()) {
                if (session.get("uploaded_filename") != null && session.get("renamed_filepath") != null && !session.get("uploaded_filename").equals("") && !session.get("renamed_filepath").equals("")) {
                    uploaded_filename = session.get("uploaded_filename").toString();
                    renamed_filepath = session.get("renamed_filepath").toString();
                    refFilename = renamed_filepath.substring(0, renamed_filepath.indexOf("."));
                }

                form_details.setUploaded_filename(uploaded_filename);
                form_details.setRenamed_filepath(renamed_filepath);

                Map ExcelValidate = null;
                if (form_details.getReq_other_add().isEmpty()) {
                    ExcelValidate = (HashMap) validateBulkDnsCSVFile(form_details);
                } else {
                    ExcelValidate = (HashMap) validateOtherRecords(form_details);
                }

                if (ExcelValidate.containsKey("errorMessage")) {
                    error.put("file_err", ExcelValidate.get("errorMessage").toString());
                } else {
                    session.put("filetodownload", refFilename);
                    session.remove("populatedList");
                }

                session.put("dns_details", form_details);
                int campaignId = (int) session.get("campaign_id");
                iCampaignId = campaignId;
                bulkData = dnsservice.fetchBulkUploadData(campaignId, form_details.getReq_other_add());
                //dnsservice.DNS_tab3(form_details);
                profile_values = (HashMap) session.get("profile-values");
                // session.put("dns_details", form_details);
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "if error is empty in tab3" + form_details.getDns_url() + form_details.getDns_loc());

            } else {
                System.out.println("inside else if getting error");
                data = "";
                CSRFRandom = "";
                action_type = "";
                dns_url = "";
                dns_oldip = "";
                dns_newip = "";
                dns_cname = "";
                ReturnString = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "E: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            dns_url = "";
            dns_oldip = "";
            dns_newip = "";
            dns_cname = "";
            ReturnString = "";
        }

        return SUCCESS;

    }

    public String dns_bulk_tab2() {
        System.out.println("inside dns bulk tab222222222222222222");
        int campaignId = (int) session.get("campaign_id");
        iCampaignId = campaignId;
        form_details = (FormBean) session.get("dns_details");
        if (dnsservice.fetchCountOfSuccessfulRecords(campaignId, form_details.getReq_other_add()) > 0) {
            try {
                error.clear();
//                if (session.get("error_file") != null) {
//                    System.out.println("inside dns bulk tab333333333333333");
//                    error.put("error_file", session.get("error_file").toString());
//                }
                System.out.println("error##########" + error);
                if (error.isEmpty()) {
                    System.out.println("inside dns bulk tab444444444444");
                    System.out.println("if error is empty::::::::" + form_details.getDns_loc());
                    bulkData = dnsservice.fetchSuccessBulkData(campaignId, form_details.getReq_(), form_details.getReq_other_add());
                    //dnsservice.DNS_tab1(form_details);
                    profile_values = (HashMap) session.get("profile-values");
                } else {
                    data = "";
                    CSRFRandom = "";
                    action_type = "";
                    dns_url = "";
                    dns_oldip = "";
                    dns_newip = "";
                    dns_cname = "";
                    ReturnString = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                data = "";
                CSRFRandom = "";
                action_type = "";
                dns_url = "";
                dns_oldip = "";
                dns_newip = "";
                dns_cname = "";
                ReturnString = "";
            }
        } else {
            data = "";
            CSRFRandom = "";
            action_type = "";
            dns_url = "";
            dns_oldip = "";
            dns_newip = "";
            dns_cname = "";
            ReturnString = "";
            error.put("file_err", "No Successfull Records!!! Please look into Error tab.");
        }
        return SUCCESS;
    }

    public String DNS_tab4() {
        try {
            db = new Database();
            UserData userdata = (UserData) session.get("uservalues");
            boolean ldap_employee = userdata.isIsEmailValidated();
            boolean nic_employee = userdata.isIsNICEmployee();
            HodData hoddata = (HodData) userdata.getHodData();
            String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail());

            error.clear();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            form_details = mapper.readValue(data, FormBean.class);
            form_details.setReq_(request_type);
            String req_ = form_details.getReq_();
            System.out.println("form details migration date:::::::" + form_details.getMigration_date());
            hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
            profile_values = (HashMap) session.get("profile-values");
            if (ldap_employee) {
                profile_values.put("hod_mobile", hodDetails.get("mobile").toString().trim());
                form_details.setHod_mobile(hodDetails.get("mobile").toString().trim());
            }

            if (form_details.getRequest_mx_chk() != null) {
            } else {
                form_details.setRequest_mx_chk("");
            }
            if (form_details.getRequest_ptr_chk() != null) {
            } else {
                form_details.setRequest_ptr_chk("");
            }
            if (form_details.getRequest_spf_chk() != null) {
            } else {
                form_details.setRequest_spf_chk("");
            }
            if (form_details.getRequest_srv_chk() != null) {
            } else {
                form_details.setRequest_srv_chk("");
            }
            if (form_details.getRequest_txt_chk() != null) {
            } else {
                form_details.setRequest_txt_chk("");
            }
            if (form_details.getRequest_dmarc_chk() != null) {
            } else {
                form_details.setRequest_dmarc_chk("");
            }

            boolean employment_error = valid.EmploymentValidation(form_details.getUser_employment());

            if (employment_error == true) {
                error.put("employment_error", "Please enter Employment in correct format");
            } else if (form_details.getUser_employment().equals("Central")) {
                if (form_details.getMin() == null) {
                    error.put("ministry_error", "Please enter Ministry in correct format");
                } else if (form_details.getDept() == null) {
                    error.put("dept_error", "Please enter Department in correct format");
                } else {
                    boolean ministry_error = valid.MinistryValidation(form_details.getMin());
                    boolean dept_error = valid.MinistryValidation(form_details.getDept());
                    if (ministry_error == true) {
                        error.put("ministry_error", "Please enter Ministry in correct format");
                    }
                    List minlist = profileService.getCentralMinistry(form_details.getUser_employment());
                    if (!minlist.contains(form_details.getMin())) {
                        error.put("ministry_error", "Please enter Ministry in correct format");

                    }
                    if (dept_error == true) {
                        error.put("dept_error", "Please enter Department in correct format");
                    }

                    List deplist = profileService.getCentralDept(form_details.getMin());
                    if (!form_details.getDept().toLowerCase().equalsIgnoreCase("other")) {
                        if (!deplist.contains(form_details.getDept())) {
                            error.put("dept_error", "Please enter Ministry in correct format");
                        }
                    }
                    if (form_details.getDept().toLowerCase().equals("other")) {
                        if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                            boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                            if (dept_other_error == true) {
                                error.put("dept_other_error", "Please enter Other Department in correct format");
                            }
                        } else {

                        }
                    }
                }
            } else if (form_details.getUser_employment().equals("State")) {
                if (form_details.getState_dept() == null) {
                    error.put("state_dept_error", "Please enter State Department in correct format");
                } else if (form_details.getStateCode() == null) {
                    error.put("state_error", "Please enter State in correct format");
                } else {

                    boolean state_dept_error = valid.MinistryValidation(form_details.getState_dept());
                    boolean state_error = valid.nameValidation(form_details.getStateCode());
                    if (state_dept_error == true) {
                        error.put("state_dept_error", "Please enter State Department in correct format");
                    }
                    if (state_error == true) {
                        error.put("state_error", "Please enter State in correct format");
                    }

                    List statedeplist = profileService.getCentralDept(form_details.getStateCode());
                    System.out.println("statedeplist" + statedeplist);
                    if (!form_details.getState_dept().toLowerCase().equalsIgnoreCase("other")) {
                        if (!statedeplist.contains(form_details.getState_dept())) {
                            error.put("state_dept_error", "Please enter correct State Department");
                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                        }
                    }
                    if (form_details.getState_dept().toLowerCase().equals("other")) {
                        if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                            boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                            if (dept_other_error == true) {
                                error.put("dept_other_error", "Please enter Other Department in correct format");
                            }
                        } else {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                        }
                    }
                }
            } else if (form_details.getUser_employment().equals("Others") || form_details.getUser_employment().equals("Psu") || form_details.getUser_employment().equals("Const") || form_details.getUser_employment().equals("Nkn") || form_details.getUser_employment().equals("Project")) {
                if (form_details.getOrg() == null) {
                    error.put("org_error", "Please enter Organization in correct format");
                } else {
                    boolean org_error = valid.MinistryValidation(form_details.getOrg());
                    if (org_error == true) {
                        error.put("org_error", "Please enter Organization in correct format");
                    }

                    List orglist = profileService.getCentralMinistry(form_details.getUser_employment());
                    System.out.println("orglist@@@@@@@@" + orglist);
                    if (!form_details.getOrg().toLowerCase().equalsIgnoreCase("other")) {
                        if (!orglist.contains(form_details.getOrg())) {
                            error.put("org_error", "Please enter correct department");
                            // error.put("hod_telephone_error", "Please Enter Reporting/Nodal/Forwarding Officer Telephone Number in correct format, STD CODE[3-5 digits only]-TELEPHONE[6-15 only]");
                        }
                    }
                    if (form_details.getOrg().toLowerCase().equals("other")) {
                        if (form_details.getOther_dept() != null || !form_details.getOther_dept().equals("")) {
                            boolean dept_other_error = valid.MinistryValidation(form_details.getOther_dept());
                            if (dept_other_error == true) {
                                error.put("dept_other_error", "Please enter Other Department in correct format");
                            }
                        } else {
                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ORG EMPTY");
                        }
                    }
                }
            }

            if (form_details.getUser_empcode() == "" || form_details.getUser_empcode() == null) {
                error.put("emp_code_error", "Enter Employee code");
            } else if (form_details.getUser_empcode() != null || !form_details.getUser_empcode().equals("")) {
                boolean emp_code_error = valid.dnsemploycodevalidation(form_details.getUser_empcode());
                if (emp_code_error == true) {
                    error.put("emp_code_error", "Please Enter Employee code in correct format");
                }

            }

            if (form_details.getModule() == null) {
                boolean ca_desg_error = valid.desigValidation(form_details.getCa_design());
                boolean hod_name_error = valid.nameValidation(form_details.getHod_name());
                boolean hod_mobile_error = valid.MobileValidation(form_details.getHod_mobile());
                boolean hod_email_error = valid.EmailValidation(form_details.getHod_email());
                boolean hod_telephone_error = valid.telValidation(form_details.getHod_tel());
                if (ca_desg_error == true) {
                    error.put("ca_desg_error", "Enter Department Name [Only characters,digits,whitespaces,dot(.),comma(,),hypen(-),ampersand(&) allowed]");
                }
                if (hod_name_error == true) {
                    error.put("hod_name_error", "Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespaces allowed]");
                }
                if (form_details.getHod_mobile().trim().startsWith("+")) {
                    if ((hod_mobile_error == true) && (form_details.getHod_mobile().trim().startsWith("+91"))) {
                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999], Max limit[13 digits]");

                    } else if ((hod_mobile_error == true) && (!form_details.getHod_mobile().trim().startsWith("+91"))) {
                        error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999],[limit 15 digits]");
                    } else {

                    }
                } else {

                    error.put("hod_mobile_error", "Enter Reporting/Nodal/Forwarding Officer Mobile Number with country code");
                }
                if (!form_details.getHod_mobile().equals(hodDetails.get("mobile"))) {
                    error.put("hod_mobile_error", "Reporting/Nodal/Forwarding Officer's mobile no should be same as in ldap");

                }
                if (!form_details.getHod_email().equals(hoddata.getEmail())) {
                    error.put("hod_email_error", "Please change it in your profile and then come back");

                }
                if (hod_telephone_error == true) {
                    error.put("hod_telephone_error", "Please enter Reporting/Nodal/Forwarding Officer Telephone in correct format");
                }
                if (hod_email_error == true) {
                    error.put("hod_email_error", "Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]");
                } else // check for nic domain
                 if (form_details.getHod_email().equals(form_details.getUser_email())) {
                        error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address");
                    } else {
                        Set s = (Set) userdata.getAliases();
                        System.out.println(" inside getEmailEquivalent get alias not null set value is  " + s);
                        ArrayList<String> newAr = new ArrayList<String>();
                        newAr.addAll(s);
                        ArrayList email_aliases = null;
                        email_aliases = newAr;
                        if (email_aliases != null && !email_aliases.equals("") && !email_aliases.toString().trim().equals("[]")) {
                            // b = (ArrayList) userdata.getAliases();
                            if (email_aliases.contains("mdlmail@nic.in") && form_details.getHod_email().contains("@mazdock.com")) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else if (email_aliases.contains(form_details.getHod_email())) {
                                error.put("hod_email_error", "Reporting/Nodal/Forwarding Officer email can not be same as user email address or its aliases.");
                            } else //String user_bo = session.get("user_bo").toString();
                             if (nic_employee && nicemployeeeditable.equals("no")) {

                                    map = db.fetchUserValuesFromOAD(userdata.getUid());
                                    if (map.size() > 0) {
                                        System.out.println("map.get(\"email\")" + map.get("email") + "form_details.getHod_email()" + form_details.getHod_email());
                                        if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                            error.put("hod_email_error", "Reporting officer details can not be changed.");

                                        } else {
                                            String e = form_details.getHod_email();
                                            String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                            if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                            } else {
                                                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                                error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                            }
                                        }
                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }

                                    }
                                } else {
                                    // DONE BY NIKKI MEENAXI ON 16 feb 
                                    //  hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                                }
                        } else //String user_bo = session.get("user_bo").toString();
                         if (nic_employee && nicemployeeeditable.equals("no")) {
                                System.out.println("map.get(\"email\")" + map.get("email") + "form_details.getHod_email()" + form_details.getHod_email());
                                map = db.fetchUserValuesFromOAD(userdata.getUid());
                                if (map.size() > 0) {
                                    if (!map.get("hod_email").equals(form_details.getHod_email())) {
                                        error.put("hod_email_error", "Reporting officer details can not be changed.");

                                    } else {
                                        String e = form_details.getHod_email();
                                        String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                        if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                        } else {
                                            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                            error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                        }
                                    }
                                } else {
                                    String e = form_details.getHod_email();
                                    String domain = form_details.getHod_email().substring(e.indexOf("@") + 1, e.length());
                                    if (domain.equals("nic.in") || domain.equals("gov.in")) {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is nic email in dlist controller tab 3");
                                    } else {
                                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "user email is ldap email -- hod email is not nic email in dlist controller tab 3");
                                        error.put("hod_email_error", "Please enter Reporting/Nodal/Forwarding Officer email of nic or gov domain");
                                    }

                                }
                            } else {
                                // DONE BY NIKKI MEENAXI ON 16 feb 
                                // hodDetails = (HashMap) profileService.getHODdetails(form_details.getHod_email());
                            }
                    }

                if (ldap_employee) {

                    System.out.println("bo of hod::::::::::::::::::" + hodDetails.get("bo").toString());
                    if (hodDetails.get("bo").toString().equals("no bo")) {
                        error.put("hod_email_error", "Reporting officer should be govt employee");
                    }

                }
            }
            // end validate profile 20th march

            // start, code added by pr on 22ndjan18
            String SessionCSRFRandom = session.get("CSRFRandom").toString();
            if (!SessionCSRFRandom.equals(CSRFRandom)) {
                error.put("csrf_error", "Invalid Security Token !");
            }

            if (error.isEmpty()) {
                String ref = session.get("ref").toString();
                String admin_role = "NA";
                if (session.get("admin_role") != null) {
                    admin_role = session.get("admin_role").toString();
                }

                Boolean flag = true;

                //String filename = session.get("filetodownload").toString() + "-valid.csv";
                form_details.setRequest_type("dns_bulk");
                flag = updateService.prevUpdateService(form_details, ref, admin_role, "", userdata.getEmail(), userdata.getAliases());

                if (flag && form_details.getModule().equals("user")) {
                    profile_values = (HashMap) session.get("profile-values");
                    form_details.setHod_email(profile_values.get("hod_email").toString());
                    form_details.setHod_name(profile_values.get("hod_name").toString());
                    form_details.setHod_mobile(profile_values.get("hod_mobile").toString());
                    form_details.setHod_tel(profile_values.get("hod_tel").toString());
                    form_details.setCa_design(profile_values.get("ca_design").toString());
                    int i = updateService.update_profile(form_details, profile_values.get("email").toString());
                    if (i > 0) {
                        profile_values.put("user_employment", form_details.getUser_employment().trim());
                        if (form_details.getUser_employment().trim().equals("Central") || form_details.getUser_employment().trim().equals("UT")) {
                            profile_values.put("min", form_details.getMin().trim());
                            profile_values.put("dept", form_details.getDept().trim());
                        } else if (form_details.getUser_employment().trim().equals("State")) {
                            profile_values.put("dept", form_details.getState_dept().trim());
                            profile_values.put("stateCode", form_details.getStateCode().trim());
                        } else if (form_details.getUser_employment().trim().equals("Others") || form_details.getUser_employment().trim().equals("Psu") || form_details.getUser_employment().trim().equals("Const") || form_details.getUser_employment().trim().equals("Nkn") || form_details.getUser_employment().trim().equals("Project")) {
                            profile_values.put("Org", form_details.getOrg().trim());
                        }
                        profile_values.put("other_dept", form_details.getOther_dept().trim());
                    }
                }
            } else {
                data = "";
                CSRFRandom = "";
                action_type = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "e: " + e.getMessage());
            data = "";
            CSRFRandom = "";
            action_type = "";
            dns_url = "";
            dns_oldip = "";
            dns_newip = "";
            dns_cname = "";
            dns_loc = "";
            ReturnString = "";
        }
        return SUCCESS;
    }

    public String checkDomain(String url) {
        ReturnString = dnsservice.checkDnsEformsFinalMapping(url, "url");
        if (ReturnString.isEmpty()) {
            ReturnString = dnsservice.checkDnsDataFromDnsAdminFinalMapping(url, "url");
            if (ReturnString.isEmpty()) {
                try {
                    InetAddress yes = java.net.InetAddress.getByName(url);
                    ReturnString = yes.getHostAddress();
                    System.out.println("ReturnString in fetch domain" + ReturnString);
                } catch (Exception ex) {
                    ReturnString = "";
//                    if (ex.getMessage().contains("unknown error")) {
//                        ReturnString = "";
//                    }
                }
            }
        } else if (ReturnString.equalsIgnoreCase("deleted")) {
            try {
                InetAddress yes = java.net.InetAddress.getByName(url);
                ReturnString = yes.getHostAddress();
                System.out.println("ReturnString in fetch domain" + ReturnString);
            } catch (Exception ex) {
                ReturnString = "";
//                if (ex.getMessage().contains("unknown error")) {
//                    ReturnString = "";
//                }
            }
        }
        return ReturnString;
    }

    public Map validateBulkDnsCSVFile(FormBean form_details) {
        profile_values = (HashMap) session.get("profile-values");
        DnsDao dnsDao = new DnsDao();
        Map ExcelValidate = new HashMap();
        String errorInString = "";
        List<String> alreadyAappliedForThisDomain = null;
        List<String> alreadyAappliedForThisIp = null;
        List<String> alreadyAllowed = null;
        String originalDomain = "";

        try {
            validation valid = new validation();
            Set<DnsBean> dnsData = null;
            String uploadedFile = "";
            String renamed_filepath = "";
            if (session.get("fileContent") == null && session.get("populatedList") != null) {
                dnsData = (Set<DnsBean>) session.get("populatedList");
            } else if (session.get("fileContent") != null) {
                dnsData = (Set<DnsBean>) session.get("fileContent");
                uploadedFile = session.get("uploaded_filename").toString();
                renamed_filepath = session.get("renamed_filepath").toString();
            } else {
                dnsData = new HashSet<>();
            }

            System.out.println("dnsData ::: " + dnsData);

            if (dnsData.size() <= 0) {
                ExcelValidate.put("errorMessage", "Please enter domain or Upload valid file. You can not keep it empty!!!");
            } else {
                boolean flag = true, originalFlag = true;
                Random rand = new Random();
                // Generate random integers in range 0 to 999999999 
                int random = rand.nextInt(1000000000);
                int campaignId = dnsservice.addDnsCampaign(random, profile_values, renamed_filepath, uploadedFile, form_details);
                if (campaignId != -1) {
                    iCampaignId = campaignId;
                    session.put("campaign_id", campaignId);
                    switch (form_details.getReq_()) {
                        case "req_new":
                            alreadyAappliedForThisDomain = new ArrayList<>();
                            alreadyAappliedForThisIp = new ArrayList<>();
                            alreadyAllowed = new ArrayList<>();

                            for (DnsBean dnsBean : dnsData) {
                                errorInString = "";
                                flag = true;
                                originalFlag = true;
                                if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                    flag = false;
                                    errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                }

                                if (valid.cnamevalidation(dnsBean.getCname())) {
                                    errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                    flag = false;
                                }

                                if (dnsBean.getNew_ip().isEmpty()) {
                                    flag = false;
                                    errorInString += "Please enter new IP." + " | ";
                                } else if (valid.dnsmodifyipvalidation(dnsBean.getNew_ip())) {
                                    flag = false;
                                    errorInString += "Please enter valid new IP." + " | ";
                                }

                                if (dnsBean.getDns_loc().isEmpty()) {
                                    errorInString += "Location of the server can not be empty | ";
                                    flag = false;
                                } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                    flag = false;
                                }

                                if (!dnsBean.getMigration_date().isEmpty()) {
                                    String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                    if (!msg.isEmpty()) {
                                        errorInString += msg + " | ";
                                        flag = false;
                                    }
                                }
                                domain = dnsBean.getDomain();
                                if (dnsBean.getDomain().startsWith("www.")) {
                                    ReturnString = checkDomain(dnsBean.getDomain());
                                    if (!ReturnString.isEmpty()) {
                                        if (doesIPbelongToNIC(ReturnString) || doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                            // THESE VALIDATIONS ARE GIVING RESULTS IN REVERSE MANNER:: i.e. if VALIDATION SUCCESSFUL : FALSE WILL
                                            // BE RETURNED AND FAILED ... TRUE WILL BE RETURNED
                                            if (valid.dnsmodifyipvalidation(ReturnString)) {
                                                errorInString += "Domain (" + domain + ") already exists with CNAME (" + ReturnString + "). Please use modify option." + " | ";
                                                flag = false;
                                            } else {
                                                errorInString += "Domain (" + domain + ") already exists with ip (" + ReturnString + ")" + " | ";
                                                flag = false;
                                            }
                                        } else if (!isApplicantOwnerWhileSubmission(domain)) {
                                            errorInString += "Since, You are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to make yourself owner of this domain (" + dnsBean.getDomain() + ")";
                                            flag = false;
                                        }
                                    } else {
                                        originalDomain = dnsBean.getDomain();
                                        //dnsBean.setDomain(dnsBean.getDomain().substring(4));
                                        ReturnString = checkDomain(dnsBean.getDomain().substring(4));
                                        if (!ReturnString.isEmpty()) {
                                            if (doesIPbelongToNIC(ReturnString) || doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                                // THESE VALIDATIONS ARE GIVING RESULTS IN REVERSE MANNER:: i.e. if VALIDATION SUCCESSFUL : FALSE WILL
                                                // BE RETURNED AND FAILED ... TRUE WILL BE RETURNED
                                                if (valid.dnsmodifyipvalidation(ReturnString)) {
                                                    errorInString += "Domain (" + dnsBean.getDomain().substring(4) + ") already exists with CNAME (" + ReturnString + "). Please use modify option and map "+ originalDomain +" as CNAME" + " | ";
                                                    flag = false;
                                                } else {
                                                    errorInString += "Domain (" + dnsBean.getDomain().substring(4) + ") already exists with ip (" + ReturnString + ")" + " | ";
                                                    flag = false;
                                                }
                                            } else if (!isApplicantOwnerWhileSubmission(dnsBean.getDomain().substring(4))) {
                                                errorInString += "Since, You are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to make yourself owner of this domain (" + dnsBean.getDomain().substring(4) + ")";
                                                flag = false;
                                            }
                                        }
                                    }
                                }

                                String domainIP = dnsBean.getDomain() + ":" + dnsBean.getNew_ip();

                                if (flag) {
                                    domain = dnsBean.getDomain();
                                    if (alreadyAllowed.contains(domainIP)) {
                                        flag = false;
                                        errorInString += "Duplicate record, Kindly remove this." + " | ";
                                    } else if (isThirdLevelDomain(dnsBean.getDomain())) {
                                        if (alreadyAappliedForThisDomain.contains(dnsBean.getDomain())) {
                                            errorInString += "You have already applied for this domain (" + dnsBean.getDomain() + ")" + " | ";
                                            flag = false;
                                        } else {
                                            if (dnsBean.getDomain().startsWith("www.")) {
                                                originalDomain = dnsBean.getDomain();
                                                dnsBean.setDomain(dnsBean.getDomain().substring(4));
                                            }
                                            ReturnString = checkDomain(dnsBean.getDomain());
                                            if (!ReturnString.isEmpty()) {
                                                if (doesIPbelongToNIC(ReturnString) || doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                                    if (originalDomain.startsWith("www.")) {
                                                        if (valid.dnsmodifyipvalidation(ReturnString)) {
                                                            errorInString += "Domain (" + dnsBean.getDomain() + ") already exists with CNAME (" + ReturnString + ")." + " Please add (" + originalDomain + ") as CNAME. | ";
                                                            flag = false;
                                                        } else {
                                                            errorInString += "Domain (" + dnsBean.getDomain() + ") already exists with ip (" + ReturnString + ")." + " Please add (" + originalDomain + ") as CNAME. | ";
                                                            flag = false;
                                                        }
                                                    } else if (valid.dnsmodifyipvalidation(ReturnString)) {
                                                        errorInString += "Domain (" + domain + ") already exists with CNAME (" + ReturnString + "). Please use modify option." + " | ";
                                                        flag = false;
                                                    } else {
                                                        errorInString += "Domain (" + domain + ") already exists with ip (" + ReturnString + ")" + " | ";
                                                        flag = false;
                                                    }
                                                } else if (!isApplicantOwnerWhileSubmission(dnsBean.getDomain())) {
                                                    errorInString += "Since, You are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to make yourself owner of this domain (" + dnsBean.getDomain() + ")";
                                                    flag = false;
                                                }
                                            } else if (doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                                if (alreadyAappliedForThisIp.contains(dnsBean.getNew_ip())) {
                                                    errorInString += "You have already applied for this IP (" + dnsBean.getNew_ip() + ")" + " | ";
                                                    flag = false;
                                                } else {
                                                    ReturnString = checkIP(dnsBean.getNew_ip());
                                                    if (!ReturnString.isEmpty()) {
                                                        errorInString += "IP (" + dnsBean.getNew_ip() + ") already exists with domain (" + ReturnString + ")" + " | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAappliedForThisDomain.add(dnsBean.getDomain());
                                                        alreadyAappliedForThisIp.add(dnsBean.getNew_ip());
                                                        alreadyAllowed.add(domainIP);
                                                    }
                                                }
                                            }
                                        }
                                    } else if (isGovDomain(dnsBean.getDomain())) {
                                        errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                        flag = false;
                                    } else if (doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                        ReturnString = checkIP(dnsBean.getNew_ip());
                                        if (!ReturnString.isEmpty()) {
                                            if (isThirdLevelDomain(ReturnString)) {
                                                errorInString += "IP (" + dnsBean.getNew_ip() + ") already exists with domain (" + ReturnString + ")" + " | ";
                                                flag = false;
                                            } else {
                                                alreadyAllowed.add(domainIP);
                                            }
                                        } else {
                                            alreadyAllowed.add(domainIP);
                                        }
                                    }
                                }

                                if (flag) {
                                    dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_UPLOAD);
                                } else {
                                    errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                    dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_UPLOAD);
                                }
                            }
                            break;
                        case "req_modify":
                            alreadyAappliedForThisDomain = new ArrayList<>();
                            alreadyAappliedForThisIp = new ArrayList<>();
                            alreadyAllowed = new ArrayList<>();

                            for (DnsBean dnsBean : dnsData) {
                                errorInString = "";
                                flag = true;

                                if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                    flag = false;
                                    errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                }

                                if (valid.cnamevalidation(dnsBean.getCname())) {
                                    errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                    flag = false;
                                }

                                if (dnsBean.getNew_ip().isEmpty()) {
                                    flag = false;
                                    errorInString += "Please enter new IP." + " | ";
                                } else if (valid.dnsmodifyipvalidation(dnsBean.getNew_ip())) {
                                    flag = false;
                                    errorInString += "Please enter valid new IP." + " | ";
                                }

                                //if (dnsBean.getOld_ip().isEmpty()) {
                                //    flag = false;
                                //    errorInString += "Please enter old IP." + " | ";
                                //} else
                                if (valid.dnsmodifyipvalidation(dnsBean.getOld_ip())) {
                                    flag = false;
                                    errorInString += "Please enter valid old IP." + " | ";
                                }

                                if (dnsBean.getDns_loc().isEmpty()) {
                                    errorInString += "Location of the server can not be empty | ";
                                    flag = false;
                                } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                    flag = false;
                                }

                                if (!dnsBean.getMigration_date().isEmpty()) {
                                    String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                    if (!msg.isEmpty()) {
                                        errorInString += msg + " | ";
                                        flag = false;
                                    }
                                }

                                String domainIP = dnsBean.getDomain() + ":" + dnsBean.getNew_ip();

                                if (flag) {
                                    if (alreadyAllowed.contains(domainIP)) {
                                        flag = false;
                                        errorInString += "Duplicate record, Kindly remove this." + " | ";
                                    } else if (isApplicantOwner(dnsBean.getDomain())) {
                                        if (isThirdLevelDomain(dnsBean.getDomain())) {
                                            if (alreadyAappliedForThisDomain.contains(dnsBean.getDomain())) {
                                                errorInString += "You have already applied for this domain (" + dnsBean.getDomain() + ")" + " | ";
                                                flag = false;
                                            } else {
                                                ReturnString = checkDomain(dnsBean.getDomain());
                                                if (ReturnString.isEmpty()) {
                                                    errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                    flag = false;
                                                } else {
                                                    if (!dnsBean.getOld_ip().isEmpty() && !ReturnString.equalsIgnoreCase(dnsBean.getOld_ip()) && !valid.dnsmodifyipvalidation(ReturnString)) {
                                                        errorInString += "Please enter the correct Old IP mapped with domain (" + dnsBean.getDomain() + ")" + " | ";
                                                        flag = false;
                                                    }

                                                    if (doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                                        if (alreadyAappliedForThisIp.contains(dnsBean.getNew_ip())) {
                                                            errorInString += "You have already applied for this IP (" + dnsBean.getNew_ip() + ")" + " | ";
                                                            flag = false;
                                                        } else {
                                                            ReturnString = checkIP(dnsBean.getNew_ip());
                                                            if (!ReturnString.isEmpty()) {
                                                                errorInString += "IP (" + dnsBean.getNew_ip() + ") already exists with domain (" + ReturnString + ")" + " | ";
                                                                flag = false;
                                                            } else {
                                                                alreadyAappliedForThisDomain.add(dnsBean.getDomain());
                                                                alreadyAappliedForThisIp.add(dnsBean.getNew_ip());
                                                                alreadyAllowed.add(domainIP);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (isGovDomain(dnsBean.getDomain())) {
                                            errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                            flag = false;
                                        } else {
                                            ReturnString = checkDomain(dnsBean.getDomain());
                                            if (ReturnString.isEmpty()) {
                                                errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                flag = false;
                                            } else {
                                                if (!dnsBean.getOld_ip().isEmpty() && !ReturnString.equalsIgnoreCase(dnsBean.getOld_ip()) && !valid.dnsmodifyipvalidation(ReturnString)) {
                                                    errorInString += "Please enter the correct Old IP mapped with domain (" + dnsBean.getDomain() + ")" + " | ";
                                                    flag = false;
                                                }

                                                if (doesIPbelongToNIC(dnsBean.getNew_ip())) {
                                                    if (alreadyAappliedForThisIp.contains(dnsBean.getNew_ip())) {
                                                        errorInString += "You have already applied for this IP (" + dnsBean.getNew_ip() + ")" + " | ";
                                                        flag = false;
                                                    } else {
                                                        ReturnString = checkIP(dnsBean.getNew_ip());
                                                        if (!ReturnString.isEmpty()) {
                                                            if (isThirdLevelDomain(ReturnString)) {
                                                                errorInString += "IP (" + dnsBean.getNew_ip() + ") already exists with domain (" + ReturnString + ")" + " | ";
                                                                flag = false;
                                                            } else {
                                                                alreadyAllowed.add(domainIP);
                                                            }
                                                        } else {
                                                            alreadyAllowed.add(domainIP);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (doNslookup(dnsBean.getDomain())) {
                                            errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                        } else {
                                            errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                        }
                                        flag = false;
                                    }
                                }

                                if (flag) {
                                    dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_UPLOAD);
                                } else {
                                    errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                    dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_UPLOAD);
                                }
                            }
                            break;
                        case "req_delete":
                            alreadyAappliedForThisDomain = new ArrayList<>();
                            alreadyAappliedForThisIp = new ArrayList<>();
                            alreadyAllowed = new ArrayList<>();

                            for (DnsBean dnsBean : dnsData) {
                                errorInString = "";
                                flag = true;

                                if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                    flag = false;
                                    errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                }

                                if (valid.cnamevalidation(dnsBean.getCname())) {
                                    errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                    flag = false;
                                }

                                if (!dnsBean.getNew_ip().isEmpty() && valid.dnsmodifyipvalidation(dnsBean.getNew_ip())) {
                                    flag = false;
                                    errorInString += "Please enter valid new IP." + " | ";
                                }

                                if (dnsBean.getDns_loc().isEmpty()) {
                                    errorInString += "Location of the server can not be empty | ";
                                    flag = false;
                                } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                    flag = false;
                                }

                                if (!dnsBean.getMigration_date().isEmpty()) {
                                    String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                    if (!msg.isEmpty()) {
                                        errorInString += msg + " | ";
                                        flag = false;
                                    }
                                }

                                String domainIP = dnsBean.getDomain() + ":" + dnsBean.getNew_ip();

                                if (flag) {
                                    if (alreadyAllowed.contains(domainIP)) {
                                        flag = false;
                                        errorInString += "Duplicate record, Kindly remove this." + " | ";
                                    } else if (isApplicantOwner(dnsBean.getDomain())) {
                                        if (!dnsBean.getNew_ip().isEmpty()) {
                                            if (isThirdLevelDomain(dnsBean.getDomain())) {
                                                ReturnString = checkDomain(dnsBean.getDomain());
                                                if (ReturnString.isEmpty()) {
                                                    errorInString += "This domain (" + dnsBean.getDomain() + ") is not available. Enter correct domain." + " | ";
                                                    flag = false;
                                                } else if (!ReturnString.equalsIgnoreCase(dnsBean.getNew_ip())) {
                                                    errorInString += "Please enter the correct Old IP mapped with domain (" + dnsBean.getDomain() + ")" + " | ";
                                                    flag = false;
                                                } else {
                                                    alreadyAllowed.add(domainIP);
                                                }
                                            } else if (isGovDomain(dnsBean.getDomain())) {
                                                errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                                flag = false;
                                            } else {
                                                ReturnString = checkDomain(dnsBean.getDomain());
                                                if (ReturnString.isEmpty()) {
                                                    errorInString += "This domain (" + dnsBean.getDomain() + ") is not available. Enter correct domain." + " | ";
                                                    flag = false;
                                                } else if (!ReturnString.equalsIgnoreCase(dnsBean.getNew_ip())) {
                                                    errorInString += "Please enter the correct Old IP mapped with domain (" + dnsBean.getDomain() + ")" + " | ";
                                                    flag = false;
                                                } else {
                                                    alreadyAllowed.add(domainIP);
                                                }
                                            }
                                        } else if (isThirdLevelDomain(dnsBean.getDomain())) {
                                            ReturnString = checkDomain(dnsBean.getDomain());
                                            if (ReturnString.isEmpty()) {
                                                errorInString += "This domain (" + dnsBean.getDomain() + ") is not available. Enter correct domain." + " | ";
                                                flag = false;
                                            } else {
                                                alreadyAllowed.add(domainIP);
                                            }
                                        } else if (isGovDomain(dnsBean.getDomain())) {
                                            errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                            flag = false;
                                        } else {
                                            ReturnString = checkDomain(dnsBean.getDomain());
                                            if (ReturnString.isEmpty()) {
                                                errorInString += "This domain (" + dnsBean.getDomain() + ") is not available. Enter correct domain." + " | ";
                                                flag = false;
                                            } else {
                                                alreadyAllowed.add(domainIP);
                                            }
                                        }
                                    } else {
                                        if (doNslookup(dnsBean.getDomain())) {
                                            errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                        } else {
                                            errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                        }
                                        flag = false;
                                    }
                                }

                                if (flag) {
                                    dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_UPLOAD);
                                } else {
                                    errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                    dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_UPLOAD);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    ExcelValidate.put("errorMessage", "Campaign could not be generated!!!");
                }
                if (session.get("fileContent") != null) {
                    session.remove("fileContent");
                }
                if (session.get("populatedList") != null) {
                    session.remove("populatedList");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ExcelValidate;
    }

    private boolean doNslookup(String domain) {
        boolean flag = false;
        try {
            InetAddress yes = java.net.InetAddress.getByName(domain);
            ReturnString = yes.getHostAddress();
            if (!ReturnString.isEmpty()) {
                flag = true;
            }
            System.out.println("ReturnString in doNslookup domain" + ReturnString);
        } catch (Exception ex) {
            System.out.println("Error in doNslookup :" + ex);
        }
        return flag;
    }

    public Map validateOtherRecords(FormBean form_details) {
        String req_other_record = form_details.getReq_other_add();
        profile_values = (HashMap) session.get("profile-values");
        DnsDao dnsDao = new DnsDao();
        Map ExcelValidate = new HashMap();
        try {
            validation valid = new validation();
            Set<DnsBean> dnsData = null;
            String uploadedFile = "";
            String renamed_filepath = "";
            if (session.get("fileContent") == null && session.get("populatedList") != null) {
                dnsData = (Set<DnsBean>) session.get("populatedList");
            } else if (session.get("fileContent") != null) {
                dnsData = (Set<DnsBean>) session.get("fileContent");
                uploadedFile = session.get("uploaded_filename").toString();
                renamed_filepath = session.get("renamed_filepath").toString();
            } else {
                dnsData = new HashSet<>();
            }

            System.out.println("dnsData ::: " + dnsData);

            if (dnsData.size() <= 0) {
                ExcelValidate.put("errorMessage", "Please enter domain or Upload valid file. You can not keep it empty!!!");
            } else {
                boolean flag = true;
                Random rand = new Random();
                // Generate random integers in range 0 to 999999999 
                int random = rand.nextInt(1000000000);
                int campaignId = dnsservice.addDnsCampaign(random, profile_values, renamed_filepath, uploadedFile, form_details);
                if (campaignId != -1) {
                    iCampaignId = campaignId;
                    session.put("campaign_id", campaignId);
                    String errorInString = "";
                    List<String> alreadyAllowed = null;
                    switch (form_details.getReq_()) {
                        case "req_new":
                        case "req_delete":
                            switch (req_other_record) {
                                case "cname":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getCname_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getCname_txt().isEmpty() || valid.cnamevalidation(dnsBean.getCname_txt())) {
                                                errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getCname_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_CNAME);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_CNAME);
                                        }
                                    }
                                    break;
                                case "mx":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getMx_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getMx_txt().isEmpty() || valid.dnsServiceValidation(dnsBean.getMx_txt())) {
                                                errorInString += "Enter MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getMx_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_MX);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_MX);
                                        }
                                    }
                                    break;
                                case "spf":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getSpf_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getSpf_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getSpf_txt())) {
                                                errorInString += "Enter valid SPF value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getSpf_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_SPF);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_SPF);
                                        }
                                    }
                                    break;
                                case "srv":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getSrv_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getSrv_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getSrv_txt())) {
                                                errorInString += "Enter valid SRV value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getSrv_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_SRV);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_SRV);
                                        }
                                    }
                                    break;
                                case "txt":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getTxt_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getTxt_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getTxt_txt())) {
                                                errorInString += "Enter valid TXT value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getTxt_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_TXT);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_TXT);
                                        }
                                    }
                                    break;
                                case "ptr":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getPtr_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getPtr_txt().isEmpty() || valid.dnsmodifyipvalidation(dnsBean.getPtr_txt())) {
                                                errorInString += "Enter the IPV4/IPV6 Address" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getPtr_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_PTR);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_PTR);
                                        }
                                    }
                                    break;
                                case "dmarc":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getDmarc_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (dnsBean.getDmarc_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getDmarc_txt())) {
                                                errorInString += "Enter valid DMARC value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getDmarc_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_DMARC);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_DMARC);
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "req_modify":
                            switch (req_other_record) {
                                case "cname":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getCname_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_cname_txt().isEmpty() && valid.cnamevalidation(dnsBean.getOld_cname_txt())) {
                                                errorInString += "Enter valid old CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getCname_txt().isEmpty() || valid.cnamevalidation(dnsBean.getCname_txt())) {
                                                errorInString += "Enter valid CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getCname_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_CNAME);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_CNAME);
                                        }
                                    }
                                    break;
                                case "mx":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getMx_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_mx_txt().isEmpty() && valid.dnsServiceValidation(dnsBean.getOld_mx_txt())) {
                                                errorInString += "Enter valid old MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getMx_txt().isEmpty() || valid.dnsServiceValidation(dnsBean.getMx_txt())) {
                                                errorInString += "Enter valid MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getMx_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_MX);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_MX);
                                        }
                                    }
                                    break;
                                case "spf":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getSpf_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_spf_txt().isEmpty() && valid.dnstxtValidation(dnsBean.getOld_spf_txt())) {
                                                errorInString += "Enter valid Old SPF value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getSpf_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getSpf_txt())) {
                                                errorInString += "Enter valid SPF value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getSpf_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_SPF);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_SPF);
                                        }
                                    }
                                    break;
                                case "srv":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getSrv_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_srv_txt().isEmpty() && valid.dnstxtValidation(dnsBean.getOld_srv_txt())) {
                                                errorInString += "Enter valid Old SRV value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getSrv_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getSrv_txt())) {
                                                errorInString += "Enter valid SRV value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getSrv_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_SRV);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_SRV);
                                        }
                                    }
                                    break;
                                case "txt":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getTxt_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_txt_txt().isEmpty() && valid.dnstxtValidation(dnsBean.getOld_txt_txt())) {
                                                errorInString += "Enter valid Old TXT value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getTxt_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getTxt_txt())) {
                                                errorInString += "Enter valid TXT value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getTxt_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_TXT);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_TXT);
                                        }
                                    }
                                    break;
                                case "ptr":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getPtr_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_ptr_txt().isEmpty() && valid.dnsmodifyipvalidation(dnsBean.getOld_ptr_txt())) {
                                                errorInString += "Enter the valid old IPV4/IPV6 Address" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getPtr_txt().isEmpty() || valid.dnsmodifyipvalidation(dnsBean.getPtr_txt())) {
                                                errorInString += "Enter the valid IPV4/IPV6 Address" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getPtr_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_PTR);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_PTR);
                                        }
                                    }
                                    break;
                                case "dmarc":
                                    alreadyAllowed = new ArrayList<>();
                                    for (DnsBean dnsBean : dnsData) {
                                        errorInString = "";
                                        flag = true;
                                        if (alreadyAllowed.contains(dnsBean.getDomain() + ":" + dnsBean.getDmarc_txt())) {
                                            flag = false;
                                            errorInString += "Duplicate record, Kindly remove this." + " | ";
                                        } else {
                                            if (dnsBean.getDomain().isEmpty() || valid.dnsurlvalidation(dnsBean.getDomain())) {
                                                flag = false;
                                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                            }

                                            if (!dnsBean.getOld_dmarc_txt().isEmpty() && valid.dnstxtValidation(dnsBean.getOld_dmarc_txt())) {
                                                errorInString += "Enter valid Old DMARC value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDmarc_txt().isEmpty() || valid.dnstxtValidation(dnsBean.getDmarc_txt())) {
                                                errorInString += "Enter valid DMARC value [limit 2-300]" + " | ";
                                                flag = false;
                                            }

                                            if (dnsBean.getDns_loc().isEmpty()) {
                                                errorInString += "Location of the server can not be empty | ";
                                                flag = false;
                                            } else if (valid.addValidation(dnsBean.getDns_loc())) {
                                                errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                                flag = false;
                                            }

                                            if (!dnsBean.getMigration_date().isEmpty()) {
                                                String msg = valid.dnsDateValidation(dnsBean.getMigration_date());
                                                if (!msg.isEmpty()) {
                                                    errorInString += msg + " | ";
                                                    flag = false;
                                                }
                                            }

                                            if (flag) {
                                                if (isApplicantOwner(dnsBean.getDomain())) {
                                                    ReturnString = checkDomain(dnsBean.getDomain());
                                                    if (ReturnString.isEmpty()) {
                                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                                        flag = false;
                                                    } else {
                                                        alreadyAllowed.add(dnsBean.getDomain() + ":" + dnsBean.getDmarc_txt());
                                                    }
                                                } else {
                                                    if (doNslookup(dnsBean.getDomain())) {
                                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + dnsBean.getDomain() + ")";
                                                    } else {
                                                        errorInString += "Domain (" + dnsBean.getDomain() + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                                    }
                                                    flag = false;
                                                }
                                            }
                                        }

                                        if (flag) {
                                            dnsDao.addValidRecordsInBulkTable(dnsBean, campaignId, Constants.DNS_BULK_DMARC);
                                        } else {
                                            errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");
                                            dnsDao.addInvalidRecordsInBulkTable(dnsBean, errorInString, campaignId, Constants.DNS_BULK_DMARC);
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    ExcelValidate.put("errorMessage", "Campaign could not be generated!!!");
                }
                if (session.get("fileContent") != null) {
                    session.remove("fileContent");
                }
                if (session.get("populatedList") != null) {
                    session.remove("populatedList");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ExcelValidate;
    }

    public String checkIP(String ip) {
        String ReturnString1 = "";
        ReturnString = dnsservice.checkDnsEformsFinalMapping(ip, "ip");
        DnsDao dnsDao = new DnsDao();
        System.out.println("inside check ip");

        if (ReturnString.isEmpty()) {
            ReturnString = dnsservice.checkDnsDataFromDnsAdminFinalMapping(ip, "ip");
            if (ReturnString.isEmpty()) {
                ReturnString = getDomainName(ip);
                if (ReturnString.isEmpty()) {
                    ReturnString = dnsDao.dbReverseDNSLookup(ip);
                    if (isThirdLevelDomain(ReturnString) && !ReturnString.isEmpty()) {
                    } else {
                        ReturnString = "";
                    }
                } else if (!isThirdLevelDomain(ReturnString)) {
                    ReturnString = "";
                }
            }
        } else if (ReturnString.equalsIgnoreCase("deleted")) {
            ReturnString = getDomainName(ip);
            if (ReturnString.isEmpty()) {
                ReturnString = dnsDao.dbReverseDNSLookup(ip);
                if (isThirdLevelDomain(ReturnString) && !ReturnString.isEmpty()) {
                } else {
                    ReturnString = "";
                }
            } else if (!isThirdLevelDomain(ReturnString)) {
                ReturnString = "";
            }
        } else {
            ReturnString1 = dnsservice.checkDnsEformsFinalMapping(ReturnString, "url");
            if (ReturnString1.equalsIgnoreCase("deleted")) {
                ReturnString = "";
            } else if (!ReturnString1.equalsIgnoreCase(ip)) {
                ReturnString = "";
            }
        }
        return ReturnString;
    }

    private String digHostName(String cmd) {
        String s = null;
        String hostString = "";
        boolean flag = false;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                flag = true;
                hostString += s + ",";
            }
            if (!flag) {
                hostString = "";
            }

            // read any errors from the attempted command
//			while ((s = stdError.readLine()) != null) {
//				System.out.println(s);
//			}
// 
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            s = "";
            //e.printStackTrace();
        }
        hostString = hostString.replaceAll("\\s*,\\s*$", "");
        return hostString;
    }

    public String getDomainName(String ip) {
        String cmd = "dig -x " + ip + " +short";
        return digHostName(cmd);
    }

    public int countChar(String str, char c) {
        int count = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }

        return count;
    }

    private boolean validateDomains(String[] domains) {
        boolean errorFlagForUrl = false;
        int j = 1;
        for (String domain1 : domains) {
            if (domain1.isEmpty() || valid.dnsurlvalidation(domain1)) {
                errorFlagForUrl = true;
                error.put("dns_url_error" + j, "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]");
                form_details.setDns_url(ESAPI.encoder().encodeForHTML(domain1));
            }
            j++;
        }
        return errorFlagForUrl;
    }

    private List<String> mapDomainWithIP(String[] domains, String[] newIps) {
        int i = 0;
        List<String> list = new ArrayList<>();
        for (String domain1 : domains) {
            list.add(domain1 + ":" + newIps[i]);
            i++;
        }
        return list;
    }

    private boolean validateIPs(String[] newIps, String anew, String mandatory) {
        boolean errorFlagForIp = false;
        if (mandatory.equals("mandatory")) {
            if (anew.equals("new")) {
                for (int i = 0, j = 1; i < newIps.length; i++, j++) {
                    if (newIps[i].isEmpty()) {
                        errorFlagForIp = true;
                        error.put("dns_new_ip_error" + j, "Please enter new IP.");
                        form_details.setDns_newip(ESAPI.encoder().encodeForHTML(newIps[i]));
                    } else if (valid.dnsmodifyipvalidation(newIps[i])) {
                        errorFlagForIp = true;
                        error.put("dns_new_ip_error" + j, "Please enter valid new IP.");
                        form_details.setDns_newip(ESAPI.encoder().encodeForHTML(newIps[i]));
                    }
                }
            } else if (anew.equals("old")) {
                for (int i = 0, j = 1; i < newIps.length; i++, j++) {
                    if (newIps[i].isEmpty()) {
                        errorFlagForIp = true;
                        error.put("dns_old_ip_error" + j, "Please enter old IP.");
                        form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(newIps[i]));
                    } else if (valid.dnsmodifyipvalidation(newIps[i])) {
                        errorFlagForIp = true;
                        error.put("dns_old_ip_error" + j, "Please enter valid old IP.");
                        form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(newIps[i]));
                    }
                }
            }
        } else if (anew.equals("new")) {
            for (int i = 0, j = 1; i < newIps.length; i++, j++) {
                if (valid.dnsmodifyipvalidation(newIps[i])) {
                    errorFlagForIp = true;
                    error.put("dns_new_ip_error" + j, "Please enter valid new IP.");
                    form_details.setDns_newip(ESAPI.encoder().encodeForHTML(newIps[i]));
                }
            }
        } else if (anew.equals("old")) {
            for (int i = 0, j = 1; i < newIps.length; i++, j++) {
                if (valid.dnsmodifyipvalidation(newIps[i])) {
                    errorFlagForIp = true;
                    error.put("dns_old_ip_error" + j, "Please enter valid old IP.");
                    form_details.setDns_oldip(ESAPI.encoder().encodeForHTML(newIps[i]));
                }
            }
        }
        return errorFlagForIp;
    }

    private List<Integer> checkDuplicateRecords(List<String> domainNewIP) {
        List<Integer> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        int i = 0;
        for (String string : domainNewIP) {
            if (list1.contains(string)) {
                list.add(i);
            } else {
                list1.add(string);
            }
            i++;
        }
        return list;
    }

    private void validateCname(String[] cname) {
        int j = 1;
        for (String dnscname : cname) {
            if (valid.cnamevalidation(dnscname)) {
                error.put("dns_cname_error" + j, "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]");
                form_details.setDns_cname(ESAPI.encoder().encodeForHTML(dnscname));
            }
            j++;
        }
    }

    public String bulkDataDelete() {
        dnsEditData.setId(bulkDataId);

        String req_other_add = "";
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        }

//        else {
//            campaignId = iCampaignId;
//            session.put("campaign_id", campaignId);
//        }
        if (session.get("req_other_add") != null) {
            req_other_add = session.get("req_other_add").toString();
        } else {
            req_other_add = dnsservice.fetchRequestOtherAdd(campaignId);
        }
        DnsBean dnsBean = dnsservice.bulkDataEdit(getBulkDataId(), req_other_add);
        session.put("campaign_id", dnsBean.getCampaignId());
        DbDao dbDao = new DbDao();
        if (dnsBean.getRegistrationNumber() == null || dnsBean.getRegistrationNumber().isEmpty()) {
            if (dnsservice.updateBulkEditTable(dnsEditData, "", true, false, true, req_other_add, request_type) > 0) {
                bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
            }
        } else if (!dbDao.isRequestClosed(dnsBean.getRegistrationNumber())) {
            switch (dnsservice.fetchCountOfSuccessfulRecords((int) session.get("campaign_id"), req_other_add)) {
                case 0:
                    bulkData.clear();
                    bulkData.put("Error", "There is nothing to delete. This request must be rejected.");
                    break;
                case 1:
                    bulkData.clear();
                    bulkData.put("Error", "This is the only record!!! Hence, can not be deleted. You may reject this request.");
                    break;
                default:
                    if (dnsservice.updateBulkEditTable(dnsEditData, "", true, false, true, req_other_add, request_type) > 0) {
                        bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
                    }
                    break;
            }
        } else {
            bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
        }
        return SUCCESS;
    }

    public String bulkDataEdit() {
        String req_other_add = "";
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        }
//        else {
//            campaignId = iCampaignId;
//            session.put("campaign_id", campaignId);
//        }

        if (session.get("req_other_add") != null) {
            req_other_add = session.get("req_other_add").toString();
        } else {
            req_other_add = dnsservice.fetchRequestOtherAdd(campaignId);
        }

        dnsData = dnsservice.bulkDataEdit(getBulkDataId(), req_other_add);
        session.put("campaign_id", dnsData.getCampaignId());
        return SUCCESS;
    }

    public String bulkDnsDataEditPost() {
        if (dnsEditData.getErrorMessage().isEmpty()) {
            String req_other_add = "";
            String request_type = "";
            int campaignId = -1;
            if (session.get("campaign_id") != null) {
                campaignId = (int) session.get("campaign_id");
            } else {
                campaignId = iCampaignId;
                session.put("campaign_id", campaignId);
            }

//            if (session.get("request_type") != null) {
//                request_type = session.get("request_type").toString();
//            } else {
//                request_type = dnsservice.fetchRequestType(campaignId);
//            }
            if (session.get("req_other_add") != null) {
                req_other_add = session.get("req_other_add").toString();
            } else {
                req_other_add = dnsservice.fetchRequestOtherAdd(campaignId);
            }

            //int campaignId = dnsservice.fetchCampaignIdFromBulkUploadTable(dnsEditData.getId(),form_details.getReq_other_add());
            form_details = dnsservice.fetchFormDetails(campaignId);
            request_type = form_details.getReq_();
            if (dnsservice.duplicateRecord(dnsEditData, request_type, campaignId, req_other_add)) {
                DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsEditData.getId(), req_other_add);
                if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                    dnsEditData.setErrorMessage("Duplicate record!!! Kindly delete this.");
                    return ERROR;
                } else {
                    List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_add);
                    if (ids.size() > 1) {
                        dnsEditData.setErrorMessage("This record is already there!!!");
                        return ERROR;
                    } else if (ids.size() == 1) {
                        if (ids.get(0) != dnsEditData.getId()) {
                            dnsEditData.setErrorMessage("This record is already there!!!");
                            return ERROR;
                        }
                    }

                    if (request_type.equals("req_modify") && req_other_add.isEmpty()) {
                        ReturnString = checkDomain(dnsEditData.getDomain());
                        if (!dnsEditData.getOld_ip().isEmpty() && !ReturnString.equalsIgnoreCase(dnsEditData.getOld_ip()) && !valid.dnsmodifyipvalidation(ReturnString)) {
                            dnsEditData.setErrorMessage("Please enter the correct Old IP mapped with domain (" + dnsEditData.getDomain() + ")");
                            return ERROR;
                        }
                    }

                    //if (!dnsservice.isRecordAlreadyThere(dnsEditData, campaignId, req_other_add)) {
                    dnsEditData.setErrorMessage("");
                    //DnsBean dnsBean = dnsservice.bulkDataEdit(getBulkDataId(), req_other_add);
                    DbDao dbDao = new DbDao();
                    if (!dbDao.isRequestClosed(dnsFetchedData.getRegistrationNumber())) {
                        if (req_other_add.isEmpty()) {
                            dnsservice.updateUrlTable(dnsEditData);
                            dnsservice.updateCnameTable(dnsEditData);
                            dnsservice.updateNewIpTable(dnsEditData);
                            dnsservice.updateOldIpTable(dnsEditData);
                        }
                        dnsservice.updateBulkEditTable(dnsEditData, "", false, false, false, req_other_add, request_type);
                        bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
                    } else {
                        bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
                    }
                    return SUCCESS;

//                } else {
//                    dnsEditData.setErrorMessage("This record is already there. Please check again.");
//                    return ERROR;
//                }
                }
            } else {
                String errorMessage = "";
                if (req_other_add.isEmpty()) {
                    errorMessage = validateDnsBulkEditData(dnsEditData, request_type, req_other_add);
                } else {
                    errorMessage = validateOtherRecordsEditData(dnsEditData, request_type, req_other_add, campaignId);
                }

                if (errorMessage.isEmpty()) {
                    dnsEditData.setErrorMessage("");
                    DnsBean dnsBean = dnsservice.bulkDataEdit(getBulkDataId(), req_other_add);
                    DbDao dbDao = new DbDao();
                    if (!dbDao.isRequestClosed(dnsBean.getRegistrationNumber())) {
                        if (req_other_add.isEmpty()) {
                            dnsservice.updateUrlTable(dnsEditData);
                            dnsservice.updateCnameTable(dnsEditData);
                            dnsservice.updateNewIpTable(dnsEditData);
                            dnsservice.updateOldIpTable(dnsEditData);
                        }
                        dnsservice.updateBulkEditTable(dnsEditData, "", false, false, false, req_other_add, request_type);
                        bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
                    } else {
                        bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
                    }
                    return SUCCESS;
                } else {
                    dnsEditData.setErrorMessage(errorMessage);
                    return ERROR;
                }
            }
        } else {
            return ERROR;
        }
    }

    public String singleDnsDataPost() {
        session.remove("uploaded_filename");
        session.remove("renamed_filepath");
        session.remove("fileContent");
        System.out.println("com.org.controller.Dns_registration.singleDnsDataPost() ::: " + dnsEditData);
        if (dnsEditData.getErrorMessage().isEmpty()) {
            Set<ValidatedDnsBean> set = null;
            if (session.get("populatedList") == null) {
                set = new HashSet<>();
            } else {
                set = (Set<ValidatedDnsBean>) session.get("populatedList");
            }
            int size = set.size();
            set.add(dnsEditData);
            if (size == set.size()) {
                dnsEditData.setErrorMessage("Duplicate Record!!! Please check again.");
            }
            session.put("populatedList", set);
            bulkData.put("dnsSingleData", session.get("populatedList"));
        }
        return SUCCESS;
    }

    public String singleDnsDataEditPost() {
        System.out.println("com.org.controller.Dns_registration.singleDnsDataEditPost() ::: " + dnsEditData);
        if (dnsEditData.getErrorMessage().isEmpty()) {
            Set<ValidatedDnsBean> set = null;
            Set<ValidatedDnsBean> set1 = null;
            if (session.get("populatedList") == null) {
                dnsEditData.setErrorMessage("This Record does not exist!!!");
            } else {
                set = (Set<ValidatedDnsBean>) session.get("populatedList");
                set1 = new HashSet<>(set);
                set.remove(dnsEditData);
                System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() Before:::: " + session.get("populatedList"));
                int size = set.size();
                set.add(dnsEditData);
                if (size == set.size()) {
                    dnsEditData.setErrorMessage("Duplicate Record!!! Please check again.");
                    //set = new HashSet<>(set1);
                    set = set1;
                }
            }
            System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() After:::: " + session.get("populatedList"));
            session.put("populatedList", set);
            bulkData.put("dnsSingleData", session.get("populatedList"));
        }
        return SUCCESS;
    }

    public String singleDnsDataFetch() {
        System.out.println("com.org.controller.Dns_registration.singleDnsDataEditPost() ::: " + dnsEditData);
        Set<ValidatedDnsBean> set = (Set<ValidatedDnsBean>) session.get("populatedList");
        for (ValidatedDnsBean dnsBean : set) {
            if (dnsBean.getId() == bulkDataId) {
                dnsEditData = dnsBean;
                break;
            }
        }
        return SUCCESS;
    }

    public String singleDnsDataDeletePost() {
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() ::: " + dnsEditData);
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() ::: " + bulkDataId);
        Set<ValidatedDnsBean> set = null;
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() Before:::: " + session.get("populatedList"));
        if (session.get("populatedList") == null) {
            dnsEditData.setErrorMessage("This Record does not exist!!!");
        } else {
            set = (Set<ValidatedDnsBean>) session.get("populatedList");
            for (ValidatedDnsBean dnsBean : set) {
                if (dnsBean.getId() == bulkDataId) {
                    dnsEditData = dnsBean;
                    break;
                }
            }
            set.remove(dnsEditData);
        }
        System.out.println("com.org.controller.Dns_registration.singleDnsDataDeletePost() After:::: " + session.get("populatedList"));
        session.put("populatedList", set);
        bulkData.put("dnsSingleData", session.get("populatedList"));
        return SUCCESS;
    }

    public String validateDnsBulkEditData(DnsBean dnsData, String requestType, String req_other_add) {
        int id = dnsEditData.getId();
        String domain = dnsEditData.getDomain();
        String newIp = dnsEditData.getNew_ip();
        String oldIp = dnsEditData.getOld_ip();
        String cName = dnsEditData.getCname();
        String originalDomain = dnsEditData.getDomain();
        boolean flag = true;
        String errorInString = "";
        //int campaignId = dnsservice.fetchCampaignId(id, req_other_add);
        int campaignId = iCampaignId;
        session.put("campaign_id", campaignId);
        //Map<String, List<String>> map = dnsservice.fetchAlreadyAppliedDomainsForAAAARecords(campaignId, req_other_add);

        try {

            switch (requestType) {
                case "req_new":
                    if (domain.isEmpty()) {
                        flag = false;
                        errorInString += "Domain can not empty" + " | ";
                    } else if (valid.dnsurlvalidation(domain)) {
                        flag = false;
                        errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                    }

                    if (valid.cnamevalidation(cName)) {
                        errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                        flag = false;
                    }

                    if (newIp.isEmpty()) {
                        flag = false;
                        errorInString += "Please enter new IP." + " | ";
                    } else if (valid.dnsmodifyipvalidation(newIp)) {
                        flag = false;
                        errorInString += "Please enter valid new IP." + " | ";
                    }

                    if (dnsEditData.getDns_loc().isEmpty()) {
                        errorInString += "Location of the server can not be empty | ";
                        flag = false;
                    } else if (valid.addValidation(dnsEditData.getDns_loc())) {
                        if (dnsEditData.getDns_loc().length() < 2) {
                            errorInString += "Length of server location must be at least 2 " + " | ";
                        } else if (dnsEditData.getDns_loc().length() > 100) {
                            errorInString += "Length of server location can not be more than 100 " + " | ";
                        } else {
                            errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                        }
                        flag = false;
                    }

                    if (!dnsEditData.getMigration_date().isEmpty()) {
                        String msg = valid.dnsDateValidation(dnsEditData.getMigration_date());
                        if (!msg.isEmpty()) {
                            errorInString += msg + " | ";
                            flag = false;
                        }
                    }

                    if (domain.startsWith("www.")) {
                        ReturnString = checkDomain(domain);
                        if (!ReturnString.isEmpty()) {
                            if (doesIPbelongToNIC(ReturnString) || doesIPbelongToNIC(newIp)) {
                                if (valid.dnsmodifyipvalidation(ReturnString)) {
                                    errorInString += "Domain (" + domain + ") already exists with CNAME (" + ReturnString + "). Please use modify option." + " | ";
                                    flag = false;
                                } else {
                                    errorInString += "Domain (" + domain + ") already exists with ip (" + ReturnString + ")" + " | ";
                                    flag = false;
                                }
                            }
                        }
                    }

                    if (!newIp.isEmpty() && flag) {
                        if (isThirdLevelDomain(domain)) {
                            if (dnsservice.alreadyAappliedForThisDomain(dnsData, campaignId, domain)) {
                                DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), "");
                                if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                    errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                    flag = false;
                                } else if (!dnsservice.isRecordAlreadyThere(dnsData, campaignId, req_other_add)) {
                                    if (doesIPbelongToNIC(newIp)) {
                                        int countOfSuccessfulRecordsAgainstDomain = dnsservice.fetchCountOfSuccessfulRecordsAgainstDomain(dnsData, campaignId, req_other_add);
                                        if (countOfSuccessfulRecordsAgainstDomain > 1) {
                                            errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                            flag = false;
                                        } else if (countOfSuccessfulRecordsAgainstDomain == 1) {
                                            if (dnsData.getId() != dnsservice.fetchIDForDomain(dnsData.getDomain(), campaignId, req_other_add)) {
                                                errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                flag = false;
                                            } else {
                                                int countOfSuccessfulRecordsAgainstIP = dnsservice.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
                                                if (countOfSuccessfulRecordsAgainstIP > 1) {
                                                    errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                    flag = false;
                                                } else if (countOfSuccessfulRecordsAgainstIP == 1) {
                                                    if (dnsData.getId() != dnsservice.fetchIDForIP(dnsData.getNew_ip(), campaignId, req_other_add)) {
                                                        errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                        flag = false;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        int countOfSuccessfulRecordsAgainstDomain = dnsservice.fetchCountOfSuccessfulRecordsAgainstDomain(dnsData, campaignId, req_other_add);
                                        if (countOfSuccessfulRecordsAgainstDomain > 1) {
                                            if (dnsservice.isIPmappedAgainstDomainNICIp(dnsData, campaignId, req_other_add)) {
                                                errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                flag = false;
                                            }
                                        } else if (countOfSuccessfulRecordsAgainstDomain == 1) {
                                            if (dnsData.getId() != dnsservice.fetchIDForDomain(dnsData.getDomain(), campaignId, req_other_add)) {
                                                if (dnsservice.isIPmappedAgainstDomainNICIp(dnsData, campaignId, req_other_add)) {
                                                    errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                    flag = false;
                                                }
                                            }
                                        }
                                    }
                                } else {
//                                        errorInString += "This record is already there. Please check again." + " | ";
//                                        flag = false;
                                }
                            } else {
                                if (domain.startsWith("www.")) {
                                    domain = domain.substring(4);
                                }
                                ReturnString = checkDomain(domain);
                                if (!ReturnString.isEmpty()) {
                                    if (doesIPbelongToNIC(ReturnString) || doesIPbelongToNIC(newIp)) {
                                        if (originalDomain.startsWith("www.")) {
                                            if (valid.dnsmodifyipvalidation(ReturnString)) {
                                                errorInString += "Domain (" + domain + ") already exists with CNAME (" + ReturnString + ")." + " Please add (" + originalDomain + ") as CNAME. | ";
                                                flag = false;
                                            } else {
                                                errorInString += "Domain (" + domain + ") already exists with ip (" + ReturnString + ")." + " Please add (" + originalDomain + ") as CNAME. | ";
                                                flag = false;
                                            }
                                        } else if (valid.dnsmodifyipvalidation(ReturnString)) {
                                            errorInString += "Domain (" + domain + ") already exists with CNAME (" + ReturnString + "). Please use modify option." + " | ";
                                            flag = false;
                                        } else {
                                            errorInString += "Domain (" + domain + ") already exists with ip (" + ReturnString + ")" + " | ";
                                            flag = false;
                                        }
                                    }
                                } else if (doesIPbelongToNIC(newIp)) {
                                    if (dnsservice.alreadyAappliedForThisIp(dnsData, campaignId, newIp)) {
                                        DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), "");
                                        if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                            errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                            flag = false;
                                        } else if (!dnsservice.isRecordAlreadyThere(dnsData, campaignId, req_other_add)) {
                                            int countOfSuccessfulRecordsAgainstIP = dnsservice.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
                                            if (countOfSuccessfulRecordsAgainstIP > 1) {
                                                errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                flag = false;
                                            } else if (countOfSuccessfulRecordsAgainstIP == 1) {
                                                if (dnsData.getId() != dnsservice.fetchIDForIP(dnsData.getNew_ip(), campaignId, req_other_add)) {
                                                    errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                    flag = false;
                                                }
                                            }
                                        } else {
//                                                    errorInString += "This record is already there. Please check again." + " | ";
//                                                    flag = false;
                                        }
                                    } else {
                                        ReturnString = checkIP(newIp);
                                        if (!ReturnString.isEmpty()) {
                                            errorInString += "IP (" + newIp + ") already exists with domain (" + ReturnString + ")" + " | ";
                                            flag = false;
                                        }
                                    }
                                }
                            }
                        } else if (isGovDomain(domain)) {
                            errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                            flag = false;
                        } else if (doesIPbelongToNIC(newIp)) {
                            if (dnsservice.alreadyAappliedForThisIp(dnsData, campaignId, newIp)) {
                                DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), "");
                                if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                    // IN error for 4th level domain
                                    if (dnsservice.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add)) {
                                        errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                        flag = false;
                                    }
                                } else // IN success for 4th level domain
                                 if (!dnsservice.isRecordAlreadyThere(dnsData, campaignId, req_other_add)) {
                                        int countOfSuccessfulRecordsAgainstIP = dnsservice.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
                                        if (countOfSuccessfulRecordsAgainstIP > 1) {
                                            if (dnsservice.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add)) {
                                                errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                flag = false;
                                            }
                                        } else if (countOfSuccessfulRecordsAgainstIP == 1) {
                                            if (dnsData.getId() != dnsservice.fetchIDForIP(dnsData.getNew_ip(), campaignId, req_other_add)) {
                                                if (dnsservice.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add)) {
                                                    errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                    flag = false;
                                                }
                                            }
                                        }
                                    } else {
//                                                errorInString += "This record is already there. Please check again." + " | ";
//                                                flag = false;
                                    }
                            } else {
                                ReturnString = checkIP(newIp);
                                if (!ReturnString.isEmpty()) {
                                    if (isThirdLevelDomain(ReturnString)) {
                                        errorInString += "IP (" + newIp + ") already exists with domain (" + ReturnString + ")" + " | ";
                                        flag = false;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "req_modify":
                    if (domain.isEmpty()) {
                        flag = false;
                        errorInString += "Domain can not empty" + " | ";
                    } else if (valid.dnsurlvalidation(domain)) {
                        flag = false;
                        errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                    }

                    if (valid.cnamevalidation(cName)) {
                        errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                        flag = false;
                    }

                    if (newIp.isEmpty()) {
                        flag = false;
                        errorInString += "Please enter new IP." + " | ";
                    } else if (valid.dnsmodifyipvalidation(newIp)) {
                        flag = false;
                        errorInString += "Please enter valid new IP." + " | ";
                    }

//                    if (oldIp.isEmpty()) {
//                        flag = false;
//                        errorInString += "Please enter old IP." + " | ";
//                    } else 
                    if (valid.dnsmodifyipvalidation(oldIp)) {
                        flag = false;
                        errorInString += "Please enter valid old IP." + " | ";
                    }

                    if (dnsEditData.getDns_loc().isEmpty()) {
                        errorInString += "Location of the server can not be empty | ";
                        flag = false;
                    } else if (valid.addValidation(dnsEditData.getDns_loc())) {
                        if (dnsEditData.getDns_loc().length() < 2) {
                            errorInString += "Length of server location must be at least 2 " + " | ";
                        } else if (dnsEditData.getDns_loc().length() > 100) {
                            errorInString += "Length of server location can not be more than 100 " + " | ";
                        } else {
                            errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                        }
                        flag = false;
                    }

                    if (!dnsEditData.getMigration_date().isEmpty()) {
                        String msg = valid.dnsDateValidation(dnsEditData.getMigration_date());
                        if (!msg.isEmpty()) {
                            errorInString += msg + " | ";
                            flag = false;
                        }
                    }

                    if (flag) {
                        if (isApplicantOwner(domain)) {
                            if (!newIp.isEmpty()) {
                                if (isThirdLevelDomain(domain)) {
                                    if (dnsservice.alreadyAappliedForThisDomain(dnsData, campaignId, domain)) {
                                        DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), "");
                                        if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                            errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                            flag = false;
                                        } else if (!dnsservice.isRecordAlreadyThere(dnsData, campaignId, req_other_add)) {
                                            if (doesIPbelongToNIC(newIp)) {
                                                int countOfSuccessfulRecordsAgainstDomain = dnsservice.fetchCountOfSuccessfulRecordsAgainstDomain(dnsData, campaignId, req_other_add);
                                                if (countOfSuccessfulRecordsAgainstDomain > 1) {
                                                    errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                    flag = false;
                                                } else if (countOfSuccessfulRecordsAgainstDomain == 1) {
                                                    if (dnsData.getId() != dnsservice.fetchIDForDomain(dnsData.getDomain(), campaignId, req_other_add)) {
                                                        errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                        flag = false;
                                                    } else {
                                                        int countOfSuccessfulRecordsAgainstIP = dnsservice.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
                                                        if (countOfSuccessfulRecordsAgainstIP > 1) {
                                                            errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                            flag = false;
                                                        } else if (countOfSuccessfulRecordsAgainstIP == 1) {
                                                            if (dnsData.getId() != dnsservice.fetchIDForIP(dnsData.getNew_ip(), campaignId, req_other_add)) {
                                                                errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                                flag = false;
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                int countOfSuccessfulRecordsAgainstDomain = dnsservice.fetchCountOfSuccessfulRecordsAgainstDomain(dnsData, campaignId, req_other_add);
                                                if (countOfSuccessfulRecordsAgainstDomain > 1) {
                                                    if (dnsservice.isIPmappedAgainstDomainNICIp(dnsData, campaignId, req_other_add)) {
                                                        errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                        flag = false;
                                                    }
                                                } else if (countOfSuccessfulRecordsAgainstDomain == 1) {
                                                    if (dnsData.getId() != dnsservice.fetchIDForDomain(dnsData.getDomain(), campaignId, req_other_add)) {
                                                        if (dnsservice.isIPmappedAgainstDomainNICIp(dnsData, campaignId, req_other_add)) {
                                                            errorInString += "You have already applied for this domain (" + domain + ")" + " | ";
                                                            flag = false;
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
//                                                errorInString += "This record is already there. Please check again." + " | ";
//                                                flag = false;
                                        }
                                    } else {
                                        ReturnString = checkDomain(domain);
                                        if (ReturnString.isEmpty()) {
                                            errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                            flag = false;
                                        } else {
                                            if (!oldIp.isEmpty() && !ReturnString.equalsIgnoreCase(oldIp) && !valid.dnsmodifyipvalidation(ReturnString)) {
                                                errorInString += "Please enter the correct Old IP mapped with domain (" + domain + ")" + " | ";
                                                flag = false;
                                            }

                                            if (doesIPbelongToNIC(newIp)) {
                                                if (dnsservice.alreadyAappliedForThisIp(dnsData, campaignId, newIp)) {
                                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), "");
                                                    if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                                        errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                        flag = false;
                                                    } else if (!dnsservice.isRecordAlreadyThere(dnsData, campaignId, req_other_add)) {
                                                        int countOfSuccessfulRecordsAgainstIP = dnsservice.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
                                                        if (countOfSuccessfulRecordsAgainstIP > 1) {
                                                            errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                            flag = false;
                                                        } else if (countOfSuccessfulRecordsAgainstIP == 1) {
                                                            if (dnsData.getId() != dnsservice.fetchIDForIP(dnsData.getNew_ip(), campaignId, req_other_add)) {
                                                                errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                                flag = false;
                                                            }
                                                        }
                                                    } else {
//                                                            errorInString += "This record is already there. Please check again." + " | ";
//                                                            flag = false;
                                                    }
                                                } else {
                                                    ReturnString = checkIP(newIp);
                                                    if (!ReturnString.isEmpty()) {
                                                        errorInString += "IP (" + newIp + ") already exists with domain (" + ReturnString + ")" + " | ";
                                                        flag = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else if (isGovDomain(domain)) {
                                    errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                    flag = false;
                                } else {
                                    ReturnString = checkDomain(domain);
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    } else {
                                        if (!oldIp.isEmpty() && !ReturnString.equalsIgnoreCase(oldIp) && !valid.dnsmodifyipvalidation(ReturnString)) {
                                            errorInString += "Please enter the correct Old IP mapped with domain (" + domain + ")" + " | ";
                                            flag = false;
                                        }

                                        if (doesIPbelongToNIC(newIp)) {
                                            if (dnsservice.alreadyAappliedForThisIp(dnsData, campaignId, newIp)) {
                                                DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), "");
                                                if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                                    // IN error for 4th level domain
                                                    if (dnsservice.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add)) {
                                                        errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                        flag = false;
                                                    }
                                                } else if (!dnsservice.isRecordAlreadyThere(dnsData, campaignId, req_other_add)) {
                                                    int countOfSuccessfulRecordsAgainstIP = dnsservice.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
                                                    if (countOfSuccessfulRecordsAgainstIP > 1) {
                                                        if (dnsservice.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add)) {
                                                            errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                            flag = false;
                                                        }
                                                    } else if (countOfSuccessfulRecordsAgainstIP == 1) {
                                                        if (dnsData.getId() != dnsservice.fetchIDForIP(dnsData.getNew_ip(), campaignId, req_other_add)) {
                                                            if (dnsservice.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add)) {
                                                                errorInString += "You have already applied for this IP (" + newIp + ")" + " | ";
                                                                flag = false;
                                                            }
                                                        }
                                                    }
                                                } else {
//                                                            errorInString += "This record is already there. Please check again." + " | ";
//                                                            flag = false;
                                                }
                                            } else {
                                                ReturnString = checkIP(newIp);
                                                if (!ReturnString.isEmpty()) {
                                                    if (isThirdLevelDomain(ReturnString)) {
                                                        errorInString += "IP (" + newIp + ") already exists with domain (" + ReturnString + ")" + " | ";
                                                        flag = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (!oldIp.isEmpty()) {
                                if (isThirdLevelDomain(domain)) {
                                    ReturnString = checkDomain(domain);
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                    } else if (!oldIp.isEmpty() && !ReturnString.equalsIgnoreCase(oldIp) && !valid.dnsmodifyipvalidation(ReturnString)) {
                                        errorInString += "Please enter the correct Old IP mapped with domain (" + domain + ")" + " | ";
                                    }
                                    errorInString += "Please enter the new IP." + " | ";
                                } else if (isGovDomain(domain)) {
                                    errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                } else {
                                    ReturnString = checkDomain(domain);
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                    } else if (!oldIp.isEmpty() && !ReturnString.equalsIgnoreCase(oldIp) && !valid.dnsmodifyipvalidation(ReturnString)) {
                                        errorInString += "Please enter the correct Old IP mapped with domain (" + domain + ")" + " | ";
                                    }
                                    errorInString += "Please enter the new IP." + " | ";
                                }
                                flag = false;
                            }

                        } else {
                            if (doNslookup(domain)) {
                                errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                            } else {
                                errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                            }
                            flag = false;
                        }
                    }
                    break;

                case "req_delete":
                    if (domain.isEmpty()) {
                        flag = false;
                        errorInString += "Domain can not empty" + " | ";
                    } else if (valid.dnsurlvalidation(domain)) {
                        flag = false;
                        errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                    }

                    if (valid.cnamevalidation(cName)) {
                        errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                        flag = false;
                    }

                    if (valid.dnsmodifyipvalidation(newIp)) {
                        flag = false;
                        errorInString += "Please enter valid new IP." + " | ";
                    }

                    if (dnsEditData.getDns_loc().isEmpty()) {
                        errorInString += "Location of the server can not be empty | ";
                        flag = false;
                    } else if (valid.addValidation(dnsEditData.getDns_loc())) {
                        if (dnsEditData.getDns_loc().length() < 2) {
                            errorInString += "Length of server location must be at least 2 " + " | ";
                        } else if (dnsEditData.getDns_loc().length() > 100) {
                            errorInString += "Length of server location can not be more than 100 " + " | ";
                        } else {
                            errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                        }
                        flag = false;
                    }

                    if (!dnsEditData.getMigration_date().isEmpty()) {
                        String msg = valid.dnsDateValidation(dnsEditData.getMigration_date());
                        if (!msg.isEmpty()) {
                            errorInString += msg + " | ";
                            flag = false;
                        }
                    }

                    if (flag) {
                        if (isApplicantOwner(domain)) {
                            if (!newIp.isEmpty()) {
                                if (isThirdLevelDomain(domain)) {
                                    ReturnString = checkDomain(domain);
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "This domain (" + domain + ") is not available. Enter correct domain." + " | ";
                                        flag = false;
                                    } else if (!ReturnString.equalsIgnoreCase(newIp)) {
                                        errorInString += "Please enter the correct Old IP mapped with domain (" + domain + ")" + " | ";
                                        flag = false;
                                    }
                                } else if (isGovDomain(domain) && req_other_add.isEmpty()) {
                                    errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                    flag = false;
                                } else if (isGovDomain(domain) && !newIp.isEmpty()) {
                                    errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                    flag = false;
                                } else {
                                    ReturnString = checkDomain(domain);
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "This domain (" + domain + ") is not available. Enter correct domain." + " | ";
                                        flag = false;
                                    } else if (!ReturnString.equalsIgnoreCase(newIp)) {
                                        errorInString += "Please enter the correct Old IP mapped with domain (" + domain + ")" + " | ";
                                        flag = false;
                                    }
                                }
                            } else if (isThirdLevelDomain(domain)) {
                                ReturnString = checkDomain(domain);
                                if (ReturnString.isEmpty()) {
                                    errorInString += "This domain (" + domain + ") is not available. Enter correct domain." + " | ";
                                    flag = false;
                                }
                            } else if (isGovDomain(domain) && req_other_add.isEmpty()) {
                                errorInString += "Please submit subdomain request by login into https://registry.gov.in/ , contact support@registry.gov.in  for any assistance." + " | ";
                                flag = false;
                            } else {
                                ReturnString = checkDomain(domain);
                                if (ReturnString.isEmpty()) {
                                    errorInString += "This domain (" + domain + ") is not available. Enter correct domain." + " | ";
                                    flag = false;
                                }
                            }
                        } else {
                            if (doNslookup(domain)) {
                                errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                            } else {
                                errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                            }
                            flag = false;
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        errorInString = errorInString.replaceAll("\\s*\\|\\s*$", "");

        return errorInString;
    }

    public String validateOtherRecordsEditData(DnsBean dnsData, String req_, String req_other_record, int campaignId) {
        String errorInString = "";
        try {
            validation valid = new validation();
            boolean flag = true;
            Map<String, List<String>> map = dnsservice.fetchAlreadyAppliedDomains(campaignId, req_other_record);
            List<String> alreadyAllowed = map.get("domains");

            switch (req_) {
                case "req_new":
                case "req_delete":
                    switch (req_other_record) {
                        case "cname":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getCname_txt().isEmpty()) {
                                errorInString += "CNAME can not be empty" + " | ";
                                flag = false;
                            } else if (valid.cnamevalidation(dnsData.getCname_txt())) {
                                errorInString += "Enter the CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getCname_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() != null && !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this cname (" + dnsData.getCname_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "mx":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getMx_txt().isEmpty()) {
                                errorInString += "MX can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnsServiceValidation(dnsData.getMx_txt())) {
                                errorInString += "Enter MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getMx_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this MX (" + dnsData.getMx_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "spf":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getSpf_txt().isEmpty()) {
                                errorInString += "SPF value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getSpf_txt())) {
                                if (dnsData.getSpf_txt().length() < 2) {
                                    errorInString += "Length of SPF must be at least 2 " + " | ";
                                } else if (dnsData.getSpf_txt().length() > 300) {
                                    errorInString += "Length of SPF can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid SPF value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getSpf_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this SPF (" + dnsData.getSpf_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "srv":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getSrv_txt().isEmpty()) {
                                errorInString += "SRV value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getSrv_txt())) {
                                if (dnsData.getSrv_txt().length() < 2) {
                                    errorInString += "Length of SRV must be at least 2 " + " | ";
                                } else if (dnsData.getSrv_txt().length() > 300) {
                                    errorInString += "Length of SRV can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid SRV value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getSrv_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this SRV (" + dnsData.getSrv_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "txt":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getTxt_txt().isEmpty()) {
                                errorInString += "TXT value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getTxt_txt())) {
                                if (dnsData.getTxt_txt().length() < 2) {
                                    errorInString += "Length of TXT must be at least 2 " + " | ";
                                } else if (dnsData.getTxt_txt().length() > 300) {
                                    errorInString += "Length of TXT can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid TXT value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getTxt_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this TXT (" + dnsData.getTxt_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "ptr":

                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getPtr_txt().isEmpty()) {
                                errorInString += "PTR can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnsmodifyipvalidation(dnsData.getPtr_txt())) {
                                errorInString += "Enter the IPV4/IPV6 Address" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getPtr_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this PTR (" + dnsData.getPtr_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }

                            break;

                        case "dmarc":

                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (dnsData.getDmarc_txt().isEmpty()) {
                                errorInString += "DMARC value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getDmarc_txt())) {
                                if (dnsData.getDmarc_txt().length() < 2) {
                                    errorInString += "Length of DMARC must be at least 2 " + " | ";
                                } else if (dnsData.getDmarc_txt().length() > 300) {
                                    errorInString += "Length of DMARC can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid DMARC value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getDmarc_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this DMARC (" + dnsData.getDmarc_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }

                            break;
                        default:
                            break;
                    }
                    break;
                case "req_modify":
                    switch (req_other_record) {
                        case "cname":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_cname_txt().isEmpty() && valid.cnamevalidation(dnsData.getOld_cname_txt())) {
                                errorInString += "Enter valid old CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getCname_txt().isEmpty()) {
                                errorInString += "New CNAME can not be empty" + " | ";
                                flag = false;
                            } else if (valid.cnamevalidation(dnsData.getCname_txt())) {
                                errorInString += "Enter the new CNAME [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getCname_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this cname (" + dnsData.getCname_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "mx":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_mx_txt().isEmpty() && valid.dnsServiceValidation(dnsData.getOld_mx_txt())) {
                                errorInString += "Enter valid old MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed" + " | ";
                                flag = false;
                            }

                            if (dnsData.getMx_txt().isEmpty()) {
                                errorInString += "MX can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnsServiceValidation(dnsData.getMx_txt())) {
                                errorInString += "Enter MX value, [Alphanumeric,dot(.),comma(,),hyphen(-),underscore(_),slash(/) and whitespaces] allowed" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getMx_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this MX (" + dnsData.getMx_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "spf":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_spf_txt().isEmpty() && valid.dnstxtValidation(dnsData.getOld_spf_txt())) {
                                errorInString += "Enter valid Old SPF value [limit 2-300]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getSpf_txt().isEmpty()) {
                                errorInString += "SPF value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getSpf_txt())) {
                                if (dnsData.getSpf_txt().length() < 2) {
                                    errorInString += "Length of SPF must be at least 2 " + " | ";
                                } else if (dnsData.getSpf_txt().length() > 300) {
                                    errorInString += "Length of SPF can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid SPF value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getSpf_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this SPF (" + dnsData.getSpf_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "srv":

                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_srv_txt().isEmpty() && valid.dnstxtValidation(dnsData.getOld_srv_txt())) {
                                errorInString += "Enter valid Old SRV value [limit 2-300]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getSrv_txt().isEmpty()) {
                                errorInString += "SRV value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getSrv_txt())) {
                                if (dnsData.getSrv_txt().length() < 2) {
                                    errorInString += "Length of SRV must be at least 2 " + " | ";
                                } else if (dnsData.getSrv_txt().length() > 300) {
                                    errorInString += "Length of SRV can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid SRV value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getSrv_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this SRV (" + dnsData.getSrv_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "txt":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_txt_txt().isEmpty() && valid.dnstxtValidation(dnsData.getOld_txt_txt())) {
                                errorInString += "Enter valid Old TXT value [limit 2-300]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getTxt_txt().isEmpty()) {
                                errorInString += "TXT value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getTxt_txt())) {
                                if (dnsData.getTxt_txt().length() < 2) {
                                    errorInString += "Length of TXT must be at least 2 " + " | ";
                                } else if (dnsData.getTxt_txt().length() > 300) {
                                    errorInString += "Length of TXT can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid TXT value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getTxt_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this TXT (" + dnsData.getTxt_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "ptr":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_ptr_txt().isEmpty() && valid.dnsmodifyipvalidation(dnsData.getOld_ptr_txt())) {
                                errorInString += "Enter the valid old IPV4/IPV6 Address" + " | ";
                                flag = false;
                            }

                            if (dnsData.getPtr_txt().isEmpty()) {
                                errorInString += "PTR can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnsmodifyipvalidation(dnsData.getPtr_txt())) {
                                errorInString += "Enter the IPV4/IPV6 Address" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getPtr_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this PTR (" + dnsData.getPtr_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        case "dmarc":
                            errorInString = "";
                            flag = true;

                            if (dnsData.getDomain().isEmpty()) {
                                flag = false;
                                errorInString += "Domain can not empty" + " | ";
                            } else if (valid.dnsurlvalidation(dnsData.getDomain())) {
                                flag = false;
                                errorInString += "Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" + " | ";
                            }

                            if (!dnsData.getOld_dmarc_txt().isEmpty() && valid.dnstxtValidation(dnsData.getOld_dmarc_txt())) {
                                errorInString += "Enter valid Old DMARC value [limit 2-300]" + " | ";
                                flag = false;
                            }

                            if (dnsData.getDmarc_txt().isEmpty()) {
                                errorInString += "DMARC value can not be empty" + " | ";
                                flag = false;
                            } else if (valid.dnstxtValidation(dnsData.getDmarc_txt())) {
                                if (dnsData.getDmarc_txt().length() < 2) {
                                    errorInString += "Length of DMARC must be at least 2 " + " | ";
                                } else if (dnsData.getDmarc_txt().length() > 300) {
                                    errorInString += "Length of DMARC can not be more than 300 " + " | ";
                                } else {
                                    errorInString += "Enter valid DMARC value [limit 2-300]" + " | ";
                                }
                                flag = false;
                            }

                            if (dnsData.getDns_loc().isEmpty()) {
                                errorInString += "Location of the server can not be empty | ";
                                flag = false;
                            } else if (valid.addValidation(dnsData.getDns_loc())) {
                                if (dnsData.getDns_loc().length() < 2) {
                                    errorInString += "Length of server location must be at least 2 " + " | ";
                                } else if (dnsData.getDns_loc().length() > 100) {
                                    errorInString += "Length of server location can not be more than 100 " + " | ";
                                } else {
                                    errorInString += "Enter valid DNS server location,[Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" + " | ";
                                }
                                flag = false;
                            }

                            if (!dnsData.getMigration_date().isEmpty()) {
                                String msg = valid.dnsDateValidation(dnsData.getMigration_date());
                                if (!msg.isEmpty()) {
                                    errorInString += msg + " | ";
                                    flag = false;
                                }
                            }

                            if (flag) {
                                String domain = dnsData.getDomain() + ":" + dnsData.getDmarc_txt();
                                if (alreadyAllowed.contains(domain)) {
                                    DnsBean dnsFetchedData = dnsservice.bulkDataEdit(dnsData.getId(), req_other_record);
                                    if (dnsFetchedData.getErrorMessage() == null || !dnsFetchedData.getErrorMessage().isEmpty()) {
                                        errorInString += "You have already applied for this DMARC (" + dnsData.getDmarc_txt() + ")" + " | ";
                                        flag = false;
                                    } else {
                                        List<Integer> ids = dnsservice.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_record);
                                        if (ids.size() > 1) {
                                            dnsEditData.setErrorMessage("This record is already there!!!");
                                            flag = false;
                                        } else if (ids.size() == 1) {
                                            if (ids.get(0) != dnsEditData.getId()) {
                                                dnsEditData.setErrorMessage("This record is already there!!!");
                                                flag = false;
                                            }
                                        }
                                    }
                                } else if (isApplicantOwner(dnsData.getDomain())) {
                                    ReturnString = checkDomain(dnsData.getDomain());
                                    if (ReturnString.isEmpty()) {
                                        errorInString += "It seems a new domain!!! Please enter the correct domain. | ";
                                        flag = false;
                                    }
                                } else {
                                    if (doNslookup(domain)) {
                                        errorInString += "Since, you are not the owner of this domain, please coordinate with dns-request@nic.in (01124305093) to modify the IP for domain (" + domain + ")";
                                    } else {
                                        errorInString += "Domain (" + domain + ") does not exist. Kindly get it created first and then apply for adding records through modify option.";
                                    }
                                    flag = false;
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorInString;
    }

    public String fetchOpenCampaigns() {
        if (session != null) {
            if (session.get("uservalues") != null) {
                UserData userdata = (UserData) session.get("uservalues");
                Set<String> aliases = userdata.getAliases();
                aliases.add(userdata.getEmail());
                campaigns = dnsservice.fetchOpenCampaigns(aliases);
            }
        }
        return SUCCESS;
    }

    public String fetchCampaignData() {
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        } else {
            campaignId = bulkDataId;
            iCampaignId = bulkDataId;
            session.put("campaign_id", campaignId);
        }

        String req_other_add = "";
        if (session.get("req_other_add") != null) {
            req_other_add = session.get("req_other_add").toString();
        } else {
            req_other_add = dnsservice.fetchRequestOtherAdd(campaignId);
            session.put("req_other_add", req_other_add);
        }
        bulkData = dnsservice.fetchBulkUploadData(campaignId, req_other_add);
        Map<String, String> map = (Map<String, String>) bulkData.get("formDetails");
        FormBean form_details = new FormBean();
        form_details.setCampaign_id(campaignId);
        form_details.setReq_(map.get("request_type"));
        form_details.setUploaded_filename(map.get("uploaded_file"));
        form_details.setRenamed_filepath(map.get("renamed_file"));
        form_details.setUser_email(map.get("owner_email"));
        form_details.setUser_name(map.get("owner_name"));
        form_details.setRequest_type("dns_bulk");
        form_details.setUrl("web_url");
        form_details.setSubmittedAt(map.get("submitted_at"));
        form_details.setReq_other_add(map.get("request_other_add"));
        session.put("campaign_id", campaignId);
        session.put("dns_details", form_details);
        session.put("uploaded_filename", map.get("uploaded_file"));
        session.put("renamed_filepath", map.get("renamed_file"));
        session.put("filetodownload", map.get("renamed_file"));
        session.put("req_", form_details.getReq_());
        return SUCCESS;
    }

    public String discardCampaign() {
        session.remove("campaign_id");
        session.remove("dns_details");
        session.remove("uploaded_filename");
        session.remove("renamed_filepath");
        session.remove("filetodownload");
        session.remove("req_");
        bulkDataStr = dnsservice.discardCampaign(bulkDataId);
        return SUCCESS;
    }

    public String areThereSuccessfulEntries() {
        int campaignId = -1;
        if (session.get("campaign_id") != null) {
            campaignId = (int) session.get("campaign_id");
        } else {
            campaignId = iCampaignId;
            session.put("campaign_id", campaignId);
        }

        String req_other_add = "";
        if (session.get("req_other_add") != null) {
            req_other_add = session.get("req_other_add").toString();
        } else {
            req_other_add = dnsservice.fetchRequestOtherAdd(campaignId);
        }

        areThereSuccessfulRecords = dnsservice.areThereSuccessfulEntries(campaignId, req_other_add);
        return SUCCESS;
    }

    public String fetchCompleteCampaignData() {

        if (session != null && session.get("uservalues") != null) {
            UserData userdata = (UserData) session.get("uservalues");
            Set<String> aliases = userdata.getAliases();
            aliases.add(userdata.getEmail());
            bulkData = dnsservice.fetchCompleteCampaignData(aliases);
        }

        return SUCCESS;
    }
}
