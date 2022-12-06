package com.org.service;

import com.org.bean.DnsBean;
import com.org.bean.DnsTeamBean;
import com.org.bean.DnsTeamBeanValidation;
import com.org.bean.ValidatedDnsBean;
import com.org.dao.DnsDao;
import com.org.dao.DnsTeamDao;
import com.org.dao.Ldap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import model.FormBean;

public class DnsService {

    DnsDao dnsdao = null;
    DnsTeamDao dnsteamdao = null;

    public DnsService() {
        dnsdao = new DnsDao();
        dnsteamdao = new DnsTeamDao();
        if (dnsdao == null) {
            dnsdao = new DnsDao();
        }
        if (dnsteamdao == null) {
            dnsteamdao = new DnsTeamDao();
        }
    }

    public void DNS_tab1(FormBean form_details) {
        dnsdao.DNS_tab1(form_details);
    }

//    public Map previewData(int temp_common_id, Map profile_values) {
//         return dnsdao.previewData(temp_common_id, profile_values);
//    }
    public String DNS_tab2(FormBean form_details, Map profile_values, String filename, String upload_filename, String rename_filename, int campaignId) {
        if (dnsdao != null) {
            return dnsdao.DNS_tab2(form_details, profile_values, filename, upload_filename, rename_filename, campaignId);
        } else {
            return "";
        }
    }

    public Map validateBulkDnsCSVFile(String renamed_filepath, String whichform, String req_) {
        if (dnsdao != null) {
            return dnsdao.validateBulkDnsCSVFile(renamed_filepath, whichform, req_);
        } else {
            return new HashMap();
        }
    }

    public void DNS_tab3(FormBean form_details) {
        if (dnsdao != null) {
            dnsdao.DNS_tab3(form_details);
        }
    }

    public Set<String> fetchOwner(String url) {
        Set<String> finalSet = new HashSet<>();
        Set<String> result = null;
        if (dnsdao != null) {
            result = dnsdao.fetchOwner(url);
        } else {
            result = new HashSet<>();
        }
        Ldap ldap = new Ldap();
        for (String email : result) {
            Set<String> set = ldap.fetchAliases(email);
            finalSet.addAll(set);
        }
        return finalSet;
    }

    public String fetchApplicant(String ref) {
        if (dnsdao != null) {
            return dnsdao.fetchApplicant(ref);
        } else {
            return "";
        }
    }

    public String checkDnsEformsFinalMapping(String url, String flag) {
        String result = "";
        if (dnsdao != null) {
            Set<String> completedRegNumbers = dnsdao.fetchCompletedDnsRegistrationNumbers(url, flag);

            if (!completedRegNumbers.isEmpty()) {
                TreeSet<String> ips = dnsdao.checkInBaseTable(completedRegNumbers);
                String[] arr = ips.last().split(":");
                if (!arr[1].equals("req_delete")) {
                    if (flag.equals("url")) {
                        result = dnsdao.fetchMappedIp(arr[0]);
                    } else if (flag.equals("ip")) {
                        result = dnsdao.fetchMappedDomain(arr[0]);
                    }
                } else {
                    result = "deleted";
                }
            }
        }
        return result;
    }

    public String checkDnsDataFromDnsAdminFinalMapping(String url, String flag) {
        if (dnsdao != null) {
            return dnsdao.checkDnsDataFromDnsAdminFinalMapping(url, flag);
        } else {
            return "";
        }
    }

    public DnsBean bulkDataEdit(int bulkDataId, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchSingleBulkRecord(bulkDataId, request_other_add);
        } else {
            return new DnsBean();
        }
    }

    public boolean duplicateRecord(DnsBean dnsEditData, String requestType, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.duplicateRecord(dnsEditData, requestType, campaignId, req_other_add);
        } else {
            return false;
        }
    }

    public int updateBulkEditTable(DnsBean dnsEditData, String errMsg, boolean error, boolean isDuplicate, boolean toBeDeleted, String request_other_add, String request_type) {
        if (dnsdao != null) {
            return dnsdao.updateBulkEditTable(dnsEditData, errMsg, error, isDuplicate, toBeDeleted, request_other_add, request_type);
        } else {
            return -1;
        }
    }

    public boolean alreadyAappliedForThisDomain(DnsBean dnsData, int campaignId, String domain) {
        if (dnsdao != null) {
            return dnsdao.alreadyAappliedForThisDomain(dnsData, campaignId, domain);
        } else {
            return false;
        }
    }

    public boolean alreadyAappliedForThisIp(DnsBean dnsData, int campaignId, String newIp) {
        if (dnsdao != null) {
            return dnsdao.alreadyAappliedForThisIp(dnsData, campaignId, newIp);
        } else {
            return false;
        }
    }

    public int addDnsCampaign(int random, Map email, String renamed_filepath, String uploaded_file, FormBean form_details) {
        int result = -1;
        if (dnsdao != null && form_details != null) {
            result = dnsdao.insertIntoDnsCampaign(random, email, renamed_filepath, uploaded_file, form_details);
            while (result <= 0) {
                Random rand = new Random();
                random = rand.nextInt(1000000000);
                result = dnsdao.insertIntoDnsCampaign(random, email, renamed_filepath, uploaded_file, form_details);
            }
        } else {
            random = result;
        }
        return random;
    }

    public Map<String, Object> fetchBulkUploadData(int campaignId, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchBulkUploadData(campaignId, request_other_add);
        } else {
            return new HashMap<>();
        }
    }

    public Map<String, Object> fetchSuccessBulkData(int id, String req_for, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchSuccessBulkData(id, req_for, request_other_add);
        } else {
            return new HashMap<>();
        }
    }

    public List<Map<String, String>> fetchOpenCampaigns(Set<String> aliases) {
        if (dnsdao != null) {
            return dnsdao.fetchOpenCampaigns(aliases);
        } else {
            return new ArrayList<>();
        }
    }

    public String discardCampaign(int bulkDataId) {
        if (dnsdao != null) {
            return dnsdao.discardCampaign(bulkDataId);
        } else {
            return "";
        }
    }

    public int fetchCampaignId(String regNumber) {
        if (dnsdao != null) {
            return dnsdao.fetchCampaignId(regNumber);
        } else {
            return -1;
        }
    }

    public FormBean fetchFormDetails(int campaignId) {
        if (dnsdao != null) {
            return dnsdao.fetchFormDetails(campaignId);
        } else {
            return new FormBean();
        }
    }

    public Map<String, Object> deleteSingleUploadRecord(int bulkDataId, String req_for, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.deleteSingleUploadRecord(bulkDataId, req_for, request_other_add);
        } else {
            return new HashMap<>();
        }
    }

    public Object fetchSuccessBulkDataUsingReg(String reg_no, String req_for, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchSuccessBulkDataUsingReg(reg_no, req_for, request_other_add);
        } else {
            return null;
        }
    }

    public int fetchCampaignIdFromBulkUploadTable(int id, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchCampaignIdFromBulkUploadTable(id, request_other_add);
        } else {
            return -1;
        }
    }

    public String fetchRegNumberFromCampaignTable(int campaignId) {
        if (dnsdao != null) {
            return dnsdao.fetchRegNumberFromCampaignTable(campaignId);
        } else {
            return "";
        }
    }

    public int updateUrlTable(DnsBean dnsEditData) {
        if (dnsdao != null) {
            return dnsdao.updateUrlTable(dnsEditData);
        } else {
            return -1;
        }
    }

    public int updateCnameTable(DnsBean dnsEditData) {
        if (dnsdao != null) {
            return dnsdao.updateCnameTable(dnsEditData);
        } else {
            return -1;
        }
    }

    public int updateNewIpTable(DnsBean dnsEditData) {
        if (dnsdao != null) {
            return dnsdao.updateNewIpTable(dnsEditData);
        } else {
            return -1;
        }
    }

    public int updateOldIpTable(DnsBean dnsEditData) {
        if (dnsdao != null) {
            return dnsdao.updateOldIpTable(dnsEditData);
        } else {
            return -1;
        }
    }

    public boolean areThereSuccessfulEntries(int campaignId, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.areThereSuccessfulEntries(campaignId, request_other_add);
        } else {
            return false;
        }
    }

    public Map<String, List<String>> fetchAlreadyAppliedDomains(int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchAlreadyAppliedDomains(campaignId, req_other_add);
        } else {
            return new HashMap<>();
        }
    }

    public int fetchCampaignId(int id, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchCampaignId(id, request_other_add);
        } else {
            return -1;
        }
    }

    public int fetchCountOfSuccessfulRecords(int campaignId, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchCountOfSuccessfulRecords(campaignId, request_other_add);
        } else {
            return -1;
        }
    }

    public String fetchRequestOtherAdd(int campaignId) {
        if (dnsdao != null) {
            return dnsdao.fetchRequestOtherAdd(campaignId);
        } else {
            return "";
        }
    }

    public String fetchRequestType(int campaignId) {
        if (dnsdao != null) {
            return dnsdao.fetchRequestType(campaignId);
        } else {
            return "";
        }
    }

    public String fetchRequestOtherAdd(String regNumber) {
        if (dnsdao != null) {
            return dnsdao.fetchRequestOtherAdd(regNumber);
        } else {
            return "";
        }
    }

    public Map<String, Object> fetchCompleteCampaignData(Set<String> aliases) {
        if (dnsdao != null) {
            return dnsdao.fetchCompleteCampaignData(aliases);
        } else {
            return new HashMap<>();
        }
    }

    public boolean isRecordAlreadyThere(DnsBean dnsEditData, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.isRecordAlreadyThere(dnsEditData, campaignId, req_other_add);
        } else {
            return false;
        }
    }

    public int fetchCountOfSuccessfulRecordsAgainstDomain(DnsBean dnsData, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchCountOfSuccessfulRecordsAgainstDomain(dnsData, campaignId, req_other_add);
        } else {
            return -1;
        }
    }

    public boolean isIPmappedAgainstDomainNICIp(DnsBean dnsData, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.isIPmappedAgainstDomainNICIp(dnsData, campaignId, req_other_add);
        } else {
            return false;
        }
    }

    public int fetchCountOfSuccessfulRecordsAgainstIP(DnsBean dnsData, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchCountOfSuccessfulRecordsAgainstIP(dnsData, campaignId, req_other_add);
        } else {
            return -1;
        }
    }

    public int fetchIDForDomain(String domain, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchIDForDomain(domain, campaignId, req_other_add);
        } else {
            return -1;
        }
    }

    public int fetchIDForIP(String new_ip, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchIDForIP(new_ip, campaignId, req_other_add);
        } else {
            return -1;
        }
    }

    public boolean isDomainMappedAgainstIPThirdLevel(String newIp, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.isDomainMappedAgainstIPThirdLevel(newIp, campaignId, req_other_add);
        } else {
            return false;
        }
    }

    public boolean isNewRequest(String regNumber, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.isNewRequest(regNumber, request_other_add);
        } else {
            return false;
        }
    }

    public boolean isNewRequestCompleteCheck(String regNumber, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.isNewRequestCompleteCheck(regNumber, request_other_add);
        } else {
            return false;
        }
    }

    public List<DnsBean> fetchSuccessBulkData(String regNumber, String req_for, String request_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchSuccessBulkData(regNumber, req_for, request_other_add);
        } else {
            return new ArrayList<>();
        }
    }

    public List<Integer> fetchIDOfDuplicateRecord(ValidatedDnsBean dnsEditData, String request_type, int campaignId, String req_other_add) {
        if (dnsdao != null) {
            return dnsdao.fetchIDOfDuplicateRecord(dnsEditData, request_type, campaignId, req_other_add);
        } else {
            return new ArrayList<>();
        }
    }

    public String fetchDnsTeamData(HttpServletRequest request) {
        if (dnsteamdao != null) {
            return dnsteamdao.fetchDnsTeamData(request);
        } else {
            return "";
        }
    }

    public DnsTeamBean fetchDnsTeamData(String id) {
        if (dnsteamdao != null) {
            return dnsteamdao.fetchDnsTeamData(id);
        }
        return null;
    }

    public boolean domainExists(String domain) {
        if (dnsteamdao != null) {
            return dnsteamdao.domainExistsInTeamData(domain);
        } else {
            return false;
        }
    }

    public String addRecordInTeamData(DnsTeamBeanValidation dnsTrckerFrmData) {
        if (dnsteamdao != null) {
            return dnsteamdao.addRecordInTeamData(dnsTrckerFrmData);
        } else {
            return "";
        }
    }

    public List<Object> fetchRecordInTeamData(int dnsTeamId) {
        if (dnsteamdao != null) {
            return dnsteamdao.fetchRecordInTeamData(dnsTeamId);
        } else {
            return new ArrayList<>();
        }
    }

    public String updateRecordInTeamData(DnsTeamBeanValidation dnsTrckerFrmData) {
        if (dnsteamdao != null) {
            return dnsteamdao.updateRecordInTeamData(dnsTrckerFrmData);
        } else {
            return "";
        }
    }

    public String deleetRecordInTeamData(int dnsTeamId) {
        if (dnsteamdao != null) {
            return dnsteamdao.deleetRecordInTeamData(dnsTeamId);
        } else {
            return "";
        }
    }

    public boolean findValidUser(String aliases) {
        if (dnsteamdao != null) {
            return dnsteamdao.findValidUser(aliases);
        } else {
            return false;
        }
    }

    public String discardCampaign(String aliase) {
        if (dnsdao != null) {
            return dnsdao.discardCampaign(aliase);
        } else {
            return "";
        }
    }

    public boolean discardCampaignThroughDomains(String domain) {
        if (dnsdao != null) {
            Set<Integer> campaigns = dnsdao.fetchCampaignsAgainstDomain(domain);
            for (Integer campaign : campaigns) {
                discardCampaign(campaign);
            }
        }
        return true;
    }

    public boolean discardCampaignThroughIPs(String ip) {
        if (dnsdao != null) {
            Set<Integer> campaigns = dnsdao.fetchCampaignsAgainstIp(ip);
            for (Integer campaign : campaigns) {
                discardCampaign(campaign);
            }
        }
        return true;
    }
}
