<%@page import="com.org.bean.UserData"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<%
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    UserData userdata = (UserData) session.getAttribute("uservalues");
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    if (!session.getAttribute("update_without_oldmobile").equals("no")) {
        if (!userdata.isIsNewUser()) {
            response.sendRedirect("Mobile_registration");
        }
        response.sendRedirect("index.jsp");
    }
    session.removeAttribute("populatedList");
%>
<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">

    <!-- begin:: Content Head -->
    <div class="k-content__head	k-grid__item">
        <div class="k-content__head-main">
            <h3 class="k-content__head-title">eForms</h3>
            <div class="k-content__head-breadcrumbs">
                <a href="#" class="k-content__head-breadcrumb-home"><i class="flaticon2-shelter"></i></a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Dashboards</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Domain Name System Services</a>
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="k-portlet__head">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 p-0">
                                <div class="k-portlet__head-label mt-4">
                                    <h3 class="k-portlet__head-title">DNS Services</h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <div class="float-right">
                                    <span id="dns_history_page" class="dns_history_page ">DNS History</span>&emsp;
                                    <% if (session.getAttribute("showDnsTrackerLink") == "showlink") { %>
                                        <a href="dns-domaintracker" class="ml-2 mt-3 domain_tracker">Domain Tracker</a>
                                    <% }%>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <div class="col-md-12 mt-4 d-none" id="history-page">
                                    <ul class="nav nav-tabs" id="myTab" role="tablist">
                                        <li class="nav-item">
                                            <a class="nav-link active" id="pending-tab" data-toggle="tab" href="#pending" role="tab" aria-controls="pending" aria-selected="true">Pending</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="completed-tab" data-toggle="tab" href="#completed" role="tab" aria-controls="completed" aria-selected="false">Completed</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="discard-tab" data-toggle="tab" href="#discard" role="tab" aria-controls="discard" aria-selected="false">Discards</a>
                                        </li>
                                    </ul>
                                    <div class="tab-content" id="myTabContent">
                                        <div class="tab-pane fade show active" id="pending" role="tabpanel" aria-labelledby="pending-tab">
                                            <div class="dns_errorMessage"></div>
                                            <table class="table table-bordered table-striped table-sm mb-4" id="campaign_pendingtbl">
                                                <thead>
                                                    <tr>
                                                        <td>S.No</td>
                                                        <td>Campaign ID</td>
                                                        <td>Submission Type</td>
                                                        <td>Creation Time</td>
                                                        <td>Action (Continue)</td>
                                                        <td>Action (Discard)</td>
                                                    </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>
                                        <div class="tab-pane fade" id="completed" role="tabpanel" aria-labelledby="completed-tab">
                                            <div class="dns_errorMessage"></div>
                                            <table class="table table-bordered table-striped table-sm mb-4" id="campaign_completetbl">
                                                <thead>
                                                    <tr>
                                                        <td>S.No</td>
                                                        <td>Campaign ID</td>
                                                        <td>Submission Type</td>
                                                        <td>Creation Time</td>
                                                        <td>Action (Continue)</td>
                                                        <td>Action (Discard)</td>
                                                    </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>
                                        <div class="tab-pane fade" id="discard" role="tabpanel" aria-labelledby="discard-tab">
                                            <div class="dns_errorMessage"></div>
                                            <table class="table table-bordered table-striped table-sm mb-4" id="campaign_discardtbl">
                                                <thead>
                                                    <tr>
                                                        <td>S.No</td>
                                                        <td>Campaign ID</td>
                                                        <td>Submission Type</td>
                                                        <td>Creation Time</td>
                                                        <td>Action (Continue)</td>
                                                        <td>Action (Discard)</td>
                                                    </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>

                                <div class="tab-pane active" id="tab1" >
                                    <div class="row mt-5" >
                                        <div class="col-md-12 proceed-div">                                                                                                                                                                
                                            <div class="row pr-4 pl-4">
                                                <div class="col-md-6">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="single" name="dns_single_form" checked> DNS User Subscription Through (Manual Entries)
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="bulk" name="dns_single_form"> DNS User Subscription Through (File Upload)
                                                        <span></span>
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="tab-pane_radio" id="tab2" >
                                    <div id="dns_single_form_div">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">DNS Entry Details</h4>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="alert alert-secondary">
                                                <div class="col-md-12">
                                                    <p style="font-size: 14px;font-weight: 500;">This registration form is designed for generating a request for DNS entry in NIC DNS Server. DNS services for websites being hosted in NIC NDC & SDC.</p>
                                                    <b>NOTE: Please read all instructions carefully and select the required services. &emsp;(Refer <a href="assets/images/EFORM DOC.pdf" target="1" style="color:red">Steps & Guidelines for DNS Entry</a>) <span style="color: red;">*</span></b>
                                                    <ul class="mt-3" style="padding-left: 15px;">
                                                        <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                                                        <li>First confirm MX pointer(mailgw.nic.in) from Mrs. Rajeshwari/ Mrs. Seema Khanna (rajp@nic.in/seema@gov.in) if related to NIC Mail Service</li>
                                                        <li>Kindly forward sub level domain entry (related to 'gov.in') through support@registry.gov.in</li>  
                                                        <li>NIC Domains are NOT allowed for PSUs (Public Sector Undertaking). </li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="hidden" name="url" value="web_url" /> <!--  above commented this added by pr on 20thmar19  -->
                                        <form id="sigle_form">
                                            <div class="col-md-12 mt-2 mb-3">
                                                <label for="street"><strong>Request For:</strong></label>
                                                <div id="dns_service" class="mt-2">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_" id="req_"  value="req_new" checked> NEW
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_" id="req_"  value="req_modify"> MODIFY
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_" id="req_"  value="req_delete"> DELETE
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <font style="color:red"><span id="dns_domain_err"></span></font>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group card-holder" id="first_div">
                                                    <div class="col-md-12 mb-4" id="other_record_additional">
                                                        <div class="singleDataErr"></div>
                                                         <div class="dns_owner_error"></div>
                                                        <label class="mb-3" for="street"><strong>Other Record Addition: </strong></label>
                                                        <div class="row">
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='cname' name="req_other_add" id="cname"> CNAME
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='mx' name="req_other_add" id="mx"> MX
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='ptr'  name="req_other_add" id="ptr"> PTR
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" name="req_other_add" value='txt' readonly="readonly" id="txt"> TXT
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='srv' name="req_other_add" id="srv"> SRV
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='spf' name="req_other_add" id="spf"> SPF
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" name="req_other_add" value='dmarc' id="dmarc"> DMARC
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6 form-group">
                                                        <input type="hidden" name="id" id="recd_id">
                                                        <label for="street" id="domain_label">Fully Qualified Domain Name  </label><span style="color: red;">*</span>
                                                        <input class="form-control class_name_dns class_name_url" placeholder="e.g : demo.nic.in or demo.gov.in" type="text" name="domain" id="dns_url_1"  value="" style="display:inline;" >
                                                        <div class=""><div id="add_dns"></div></div>
                                                        <div id="dnsurl"></div>
                                                        <font style="color:red"><span class="text-danger dns_url_error dns_url_error1" id="dns_url_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6 form-group not-other" id="cname_entry">
                                                        <label for="street" id="dns_cname">CNAME</label>
                                                        <input class="form-control class_name_dns class_name_url" placeholder="Enter CNAME e.g : www.demo.nic.in or demo.gov.in" type="text" name="cname" id="dns_cname_1"  value="" style="display:inline;" >
                                                        <div class=""><div id="add_cname"></div></div>
                                                        <div id="dnsurl"></div>
                                                        <font style="color:red"><span class="text-danger dns_cname_error1" id="dns_cname_error"></span></font>
                                                    </div>
                                                    <div class="col-md-6 form-group d-none" id="other_data_entry">
                                                        <div class="row">
                                                            <div id="other_old" class="col-md-6 d-none">
                                                                <label for="street" id="dns_old_other_lbl">CNAME </label>
                                                                <input class="form-control class_name_dns class_name_url" placeholder="Enter CNAME e.g : www.demo.nic.in or demo.gov.in" type="text" name="old_oth_txt" id="old_other_txt"  value="" style="display:inline;" >
                                                                <font style="color:red"><span class="text-danger dns_other_old_error" id="dns_other_old_error"></span></font>
                                                            </div>
                                                            <div id="other_new" class="col-md-12">
                                                                <label for="street" id="dns_new_other_lbl">CNAME </label> <span style="color: red;">*</span>
                                                                <input class="form-control class_name_dns class_name_url" placeholder="Enter CNAME e.g : www.demo.nic.in or demo.gov.in" type="text" name="oth_txt" id="new_other_txt"  value="" style="display:inline;" >
                                                                <font style="color:red"><span class="text-danger dns_other_new_error" id="dns_other_new_error"></span></font>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6 form-group not-other display-hide" id="req_old_ip">
                                                        <label for="street" id="old_ip_label"> Old Application IP/A/AAAA  </label>
                                                        <input class="form-control class_name_dns class_name_url" placeholder="Enter Old IP e.g : 164.100.X.X or IPV6 Address" type="text" name="old_ip" id="dns_oldip_1"  value="" style="display:inline;" >
                                                        <div id="dnsurl"></div>
                                                        <font style="color:red"><span class="text-danger dns_old_ip_error dns_old_ip_error1" id="dns_old_ip_error"></span></font>
                                                    </div>
                                                    <div class="col-md-6 form-group not-other" id="req_new_ip">
                                                        <label for="street" id="new_ip_label">IP Address A OR AAAA <span class="text-danger new_ip_chk">*</span></label>
                                                        <input class="form-control class_name_loc" placeholder="Enter New IP e.g : 164.100.X.X or IPV6 Address" type="text" name="new_ip" id="dns_new_ip"  value="">
                                                        <font style="color:red"><span class="text-danger dns_new_ip_error1" id="dns_new_ip_error"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="row form-group">
                                                            <div class="col-md-7">
                                                                <label for="street" id="">Web Server Location  </label><span style="color: red;">*</span>
                                                                <input class="form-control class_name_loc" placeholder="Enter server location" type="text" name="dns_loc" id="dns_loc"  value="">
                                                                <font style="color:red"><span id="dns_loc_error" class="dns_loc_error"></span></font>
                                                            </div>
                                                            <div class="col-md-5 pl-0">
                                                                <label for="street" id="">Migration Date</label>
                                                                <input type="text" class="form-control migrationdatepicker1" placeholder="Migration Date" name="migration_date" readonly>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-12 text-center">
                                                        <button id="addiprecord" type="button" class="btn btn-primary btn-info addip"><i class="fa fa-plus fa-2x"></i> Add Record</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        <div class="col-md-12 mt-4 d-none" id="dnsip_collection">
                                            <label for="dnsip_collection_tbl">DNS User Subscription Data</label>
                                            <table class="table table-bordered table-striped table-sm mb-4" id="dnsip_collection_tbl">
                                                <thead class="thead-light">
                                                    <tr>
                                                        <td class="text-center" width="8%">S.No</td>
                                                        <td>Domain</td>
                                                        <td>CNAME</td>
                                                        <td>OLD IP</td>
                                                        <td>NEW IP</td>
                                                        <td>Web Server Location</td>
                                                        <td>Migration Date</td>
                                                        <td class="text-center">Action</td>
                                                    </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>	
                                        <form action="" method="post" id="dns_single_form" class="form_val">
                                            <div class="col-md-12 mt-5" id="third_div">
                                                <div class="row">                                        
                                                    <div class="col-md-6 mt-4" style="text-align: right;">
                                                        <label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                        <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                    </div>
                                                    <div class="col-md-2">
                                                        <div class="form-group">
                                                            <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                            <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value="" autocomplete="off"> 
                                                            <font style="color:red"><span id="captchaerror" class="cap_error"></span></font>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12 text-center mt-3 mb-5 four_div" id="four_div">
                                                <input type="hidden" name="req_" id="_req">
                                                <input type="hidden" name="req_other_add" id="req_other_additional">
                                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                <input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />
                                                <button name="submit" value="dns_single" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                            </div>
                                        </form>
                                    </div>
                                    <div id="dnsbulk-1">           
                                        <form action="" method="post" id="dns_bulk_form" class="form_val display-hide">
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <h4 class="theme-heading-h3">DNS Bulk Entry Details</h4>
                                            </div>

                                            <div class="col-md-12">
                                                <div class="alert alert-secondary">
                                                    <div class="col-md-12">
                                                        <p style="font-size: 14px;font-weight: 500;">This registration form is designed for generating a request for DNS entry in NIC DNS Server. DNS services for websites being hosted in NIC NDC & SDC.</p>
                                                        <b>NOTE: Please read all instructions before uploading the file</b>
                                                        <ul style="padding-left: 15px;margin-top: 15px;">
                                                            <li>All the colums  heading are mandatory in CSV file</li>
                                                            <li>Download the sample file then do the entries.</li>
                                                            <li>DNS URL is mandatory field.</li>
                                                            <li>Maximum number of rows accepted at a time is 3000. Please upload CSV file with maximum 3000 rows only.</li>
                                                        </ul>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-md-12 mt-3 mb-3">
                                                <label for="street"><strong>Request For:</strong></label>
                                                <div id="dns_service">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_" id="req_"  value="req_new" checked> NEW
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_" id="req_"  value="req_modify"> MODIFY
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_" id="req_"  value="req_delete"> DELETE
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <font style="color:red"><span id="dns_domain_err"></span></font>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group card-holder">
                                                    <div class="col-md-12 mb-4" id="other_record_additional">
                                                        <label class="mb-3" for="street"><strong>Other Record Addition: </strong></label>
                                                        <div class="row">
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='cname' name="req_other_add" id="bulk_cname"> CNAME
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='mx' name="req_other_add" id="bulk_mx"> MX
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='ptr'  name="req_other_add" id="bulk_ptr"> PTR
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" name="req_other_add" value='txt' readonly="readonly" id="bulk_txt"> TXT
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='srv' name="req_other_add" id="bulk_srv"> SRV
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" value='spf' name="req_other_add" id="bulk_spf"> SPF
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-1 col-sm-2 col-3">
                                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                    <input type="radio" name="req_other_add" value='dmarc' id="bulk_dmarc"> DMARC
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label>Please Select Your File</label>
                                                        <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                            <input type="file" class="custom-file-input" name="user_file" id="user_file_dns">
                                                            <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                                            <span class="fileinput-filename"> </span> &nbsp;
                                                            <font style="color:red"><span id="file_err"> </span></font>
                                                        </div>
                                                        <font style="color:red"><span id="file_err"> </span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="">You can Download the File and fill the Detail and Upload</label> <br>
                                                        <a href="assets/downloads/req_new.csv" class="btn btn-danger" id="download_bulk_file"><i class="fa fa-download"></i> Download File</a>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group" id="third_div">
                                                <div class="row">                                        
                                                    <div class="col-md-6 mt-5" style="text-align: right;">
                                                        <label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                        <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh1" height="20" width="20">
                                                    </div>
                                                    <div class="col-md-2 mt-3">
                                                        <div class="form-group">
                                                            <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                            <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value="" autocomplete="off"> 
                                                            <font style="color:red"><span id="captchaerror" class="cap_error"></span></font>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <font style="color:red"><span id="request_err" ></span></font>
                                            <div class="row" >

                                            </div>
                                            <div class="col-md-12 text-center mt-5 mb-4" id="four_div">
                                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                <input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />
                                                <button name="submit" value="nkn_bulk" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                            </div>
                                        </form>
                                    </div>
                                    <div id="dnsbulk-2" class="display-hide">
                                        <form action="" method="post" id="dns_bulk_form2" class="form_val" >
                                            <div class="col-md-12">
                                                <div class="dns_owner_error"></div>
                                                <ul class="nav nav-tabs" id="myTab" role="tablist">
                                                    <li class="nav-item">
                                                        <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Success&ensp;<span class="counter succ-count">0</span></a>
                                                    </li>
                                                    <li class="nav-item">
                                                        <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Error&ensp;<span class="counter err-count">0</span></a>
                                                    </li>
                                                </ul>
                                                <div class="tab-content" id="myTabContent">
                                                    <div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                                                        <h4 class="h4-head pb-3 success-head-notify">Following Domain's are valid for email creation.</h4>
                                                        <table class="table table-bordered table-striped" id="success_bulk_record">
                                                            <thead>
                                                                <tr>
                                                                    <td>S.No</td>
                                                                    <td>Domain</td>
                                                                    <td>CNAME</td>
                                                                    <td>OLD&nbsp;IP</td>
                                                                    <td>NEW&nbsp;IP</td>
                                                                    <td>Action</td>
                                                                </tr>
                                                            </thead>
                                                            <tbody></tbody>
                                                        </table>
                                                    </div>
                                                    <div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                                                        <h4 class="h4-head pb-3 err-head-notify"></h4>
                                                        <table class="table table-bordered table-striped" id="error_bulk_record">
                                                            <thead>
                                                                <tr>
                                                                    <td>S.No</td>
                                                                    <td>Domain</td>
                                                                    <td>CNAME</td>
                                                                    <td>OLD&nbsp;IP</td>
                                                                    <td>NEW&nbsp;IP</td>
                                                                    <td>Error</td>
                                                                    <td>Action</td>
                                                                </tr>
                                                            </thead>
                                                            <tbody></tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row text-center mt-5 mb-5">
                                                <div class="col-md-12">
                                                    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                    <button name="submit" value="preview" class="btn purple btn-outline sbold btn-success" > Preview and Submit </button>                                                                
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- END PAGE CONTENT INNER -->
<jsp:include page="include/new_include/footer.jsp" />
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="dns_form2" method="post">
        <jsp:include page="include/dns_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully and select the required services.</b>
                <br/>
                <ol>
                    <li>DNS entry request only on NIC Private/Public IP Pool (164.100.X.X) and NIC IPV6 addresses will be entertained</li>
                    <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                    <li>First confirm MX pointer(mailgw.nic.in) from Mrs. Rajeshwari/ Mrs. Seema Khanna (rajp@nic.in/seema@gov.in) if related to NIC Mail Service</li>
                    <!--<li>Kindly forward sub level domain entry (related to 'gov.in') through support@registry.gov.in</li>  -->
                    <li>NIC Domains are NOT allowed for PSUs (Public Sector Undertaking). </li>
                    <li>NIC does not capture any aadhaar related information.</li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="dns_form_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<div class="modal fade" id="bulkUploadEditSingle" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">DNS Edit Form</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="dns_errorMessage"></div>
                <input type="hidden" id="comming_from">
                <input type="hidden" name="iCampaignId" id="iCampaignIdDt">
                <form id="singleBulkEdit">
                    <input type="hidden" name="id" id="sinId">
                    <div id="bulkdynamic-form"></div>
                    <div class="form-group">
                        <label for="street" id="">Migration Date </label>
                        <input type="text" id="migrate_pop1" class="form-control migrationdatepicker" placeholder="Migration Date" name="migration_date" readonly>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-primary d-none" id="singleDataEditBtn">Update</span>
                <span class="btn btn-primary" id="singleBulkEditBtn">Update</span>
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="dynamicUploadEditSingle" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">DNS Edit Form</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="dns_sigerrorMessage"></div>
                <input type="hidden" id="comming_from">
                <form id="singleDomEdit">
                    <input type="hidden" name="id" id="domid">
                    <div id="dynamic-form"></div>
                    <div class="form-group">
                        <label for="street" id="">Migration Date </label>
                        <input type="text" class="form-control migrationdatepicker" placeholder="Migration Date" name="migration_date" id="migrate_pop1_sig" readonly>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-primary" id="singleDataEditBtnFetch">Update</span>
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="campaign_modaltbl" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Fetch Campaign</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <p><b>Important:</b> It's seems that you have some Pending record/s, You can continue from previous activity by click (Continue Button), else you can Discard it, and fill up a Fresh Application.</p>
                <div class="dns_errorMessage"></div>
                <table class="table table-bordered" id="campaign_tbl">
                    <thead>
                        <tr>
                            <td>S.No</td>
                            <td>Campaign ID</td>
                            <td>Request Type</td>
                            <td>Other Request Type</td>
                            <td>Creation Time</td>
                            <td colspan="2">Action</td>
                        </tr>
                    </thead>
                    <tbody></tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/dns.js?v=1.0.12" type="text/javascript"></script>
<script src="main_js/dns_single.js?v=1.0.12" type="text/javascript"></script>
<script src="main_js/dns_bulk.js?v=1.0.11" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/dataTables.buttons.min.js" type="text/javascript"></script>
<script src="assets/demo/default/base/assets_datatable/js/buttons.html5.min.js" type="text/javascript"></script>
<script type="text/javascript">
    jQuery(document).ready(function () {
        $("#refresh").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#closebtn").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#imgtxt').val("");
        });
        $("#refresh1").on("click", function () {
            $('#captcha1').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
    });
</script>
<script>
    $(document).ready(function () {
        if ('<%=nic_employee%>' !== null) {
            if ('<%=nic_employee%>' === 'true') {
                var hod_email = '<s:property value="#session['uservalues'].hodData.email" />';
                if (hod_email === null || hod_email === "") {
                } else {
                    $("input[name='hod_email']").prop('readonly', true);
                }
            }
        }
    });
</script>
<script>
    $(document).ready(function () {
        if ('<%=session.getAttribute("resend_request")%>' == "true") {
            var form_name = '<s:property value="#session['prvwdetails'].form_name" />';
            if (form_name == "DNS")
            {
                var dns_type = '<s:property value="#session['prvwdetails'].dns_type" />';
                var record_mx = '<s:property value="#session['prvwdetails'].record_mx" />';
                var record_ptr = '<s:property value="#session['prvwdetails'].record_ptr" />';
                var record_srv = '<s:property value="#session['prvwdetails'].record_srv" />';
                var record_spf = '<s:property value="#session['prvwdetails'].record_spf" />';
                var record_txt = '<s:property value="#session['prvwdetails'].record_txt" />';
                var record_dmarc = '<s:property value="#session['prvwdetails'].record_dmarc" />';
                var req_for = '<s:property value="#session['prvwdetails'].req_for" />';
                var form_type = '<s:property value="#session['prvwdetails'].form_type" />';
                var uploaded_filename = '<s:property value="#session['prvwdetails'].uploaded_filename" />';
                var renamed_filepath = '<s:property value="#session['prvwdetails'].renamed_filepath" />';
                var server_location = '<s:property value="#session['prvwdetails'].server_location" />';
                var record_ptr1 = '<s:property value="#session['prvwdetails'].record_ptr1" />';
                var record_mx1 = '<s:property value="#session['prvwdetails'].record_mx1" />';
                var domain_url = '<s:property value="#session['prvwdetails'].domain_url" />';
                var domain_new_ip = '<s:property value="#session['prvwdetails'].domain_new_ip" />';
                var domain_cname = '<s:property value="#session['prvwdetails'].domain_cname" />';
                var domain_old_ip = '<s:property value="#session['prvwdetails'].domain_old_ip" />';
                $('#add_dns').html('');
                $('#add_cname').html('');
                $('#addoldip').html('');
                $('#addnewip').html('');
                // dns url
                $('#dns_loc').val(server_location);
                if (domain_url.indexOf(";") > -1) {
                    var myarray = domain_url.split(';');
                    for (var i = 0; i < myarray.length; i++)
                    {
                        var service = myarray[i].trim();
                        if (i === 0) {
                            $('#dns_url_1').val(service);
                        } else {
                            var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" type="text" name="dns_url[]" value="' + service + '" style="margin: 20px 0px 20px 0px; "></div>';
                            //$('#add_dns').append(elmnt);
                        }
                    }
                } else
                {
                    $('#dns_url_1').val(domain_url);
                }
                if (domain_new_ip.indexOf(";") > -1) {
                    var myarray = domain_new_ip.split(';');
                    for (var i = 0; i < myarray.length; i++)
                    {
                        var service = myarray[i].trim();
                        if (i === 0) {
                            $('#dns_newip_1').val(service);
                        } else {
                            var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" type="text" name="dns_new_ip[]" value="' + service + '" style="margin: 20px 0px 20px 0px; "></div>';
                            //$('#addnewip').append(elmnt);
                        }
                    }
                } else
                {
                    $('#dns_newip_1').val(domain_new_ip);
                }
                if (domain_old_ip != null)
                {
                    if (domain_old_ip.indexOf(";") > -1) {
                        var myarray = domain_old_ip.split(';');
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var service = myarray[i].trim();
                            if (i === 0) {
                                $('#dns_oldip_1').val(service);
                            } else {
                                var elmnt = '<div class="con"><input class="form-control dns_name class_name_url1" placeholder="Enter the DNS URL [e.g.: demo.nic.in or demo.gov.in]" type="text" name="dns_old_ip[]" value="' + service + '" style="margin: 20px 0px 20px 0px; "></div>';
                                // $('#addoldip').append(elmnt);
                            }
                        }
                    } else
                    {
                        $('#dns_oldip_1').val(domain_old_ip);
                    }
                }
                if (domain_cname.indexOf(";") > -1) {
                    var myarray = domain_cname.split(';');
                    for (var i = 0; i < myarray.length; i++)
                    {
                        var service = myarray[i].trim();
                        if (i === 0) {
                            $('#dns_cname_1').val(service);
                        } else {
                            var elmnt = '<div class="con"><input class="form-control class_name_loc1" placeholder="Enter the DNS server location" value="' + service + '" name="dns_cname[]" id="dns_loc" style="margin: 20px 0px 20px 0px;"></div>';
                            //$('#add_cname').append(elmnt);
                        }
                    }
                } else
                {
                    $('#dns_cname_1').val(domain_cname);
                }
                // RADIO
                if (dns_type === 'nic')
                {
                    $('#dns_domain1_1').prop('checked', true);
                    $('#domain1').text('.nic.in');
                } else {
                    $('#dns_domain1_2').prop('checked', true);
                    $('#domain1').text('.gov.in');
                }

                if (record_mx !== '') {
                    $('#request_mx_1').val(record_mx);
                    $('#request_mx_1').removeClass('display-hide');
                    $('#mx_1').prop('checked', true);
                } else
                {
                    $('#request_mx_1').val("");
                    $('#request_mx_1').addClass('display-hide');
                    $('#mx_1').prop('checked', false);
                }
                if (record_mx1 !== '') {
                    $('#mx_1').prop('checked', true);
                    if (req_for === "req_modify")
                    {
                        $('#request_mx_2').val(record_mx1);
                        $('#request_mx_2').removeClass('display-hide');
                    }
                } else {
                    $('#mx_1').prop('checked', false);
                }
                if (record_ptr !== '') {
                    $('#request_ptr_1').val(record_ptr);
                    $('#request_ptr_1').removeClass('display-hide');
                    $('#ptr_1').prop('checked', true);
                    if (req_for === "req_modify")
                    {
                        $('#request_ptr_2').val(record_ptr1);
                        $('#request_ptr_2').removeClass('display-hide');
                    }
                } else
                {
                    $('#request_ptr_1').val("");
                    $('#request_ptr_1').addClass('display-hide');
                    if (req_for === "req_modify")
                    {
                        $('#request_ptr_2').val("");
                        $('#request_ptr_2').addClass('display-hide');
                    }
                    $('#ptr_1').prop('checked', false);
                }
                if (record_srv !== '') {
                    $('#request_srv_1').val(record_srv);
                    $('#request_srv_1').removeClass('display-hide');
                    $('#srv_1').prop('checked', true);
                } else
                {
                    $('#request_srv_1').val("");
                    $('#request_srv_1').addClass('display-hide');
                    $('#srv_1').prop('checked', false);
                }
                if (record_spf !== '')
                {
                    $('#request_spf_1').val(record_spf);
                    $('#request_spf_1').removeClass('display-hide');
                    $('#spf_1').prop('checked', true);
                } else {
                    $('#request_spf_1').val("");
                    $('#request_spf_1').addClass('display-hide');
                    $('#spf_1').prop('checked', false);
                }
                if (record_txt !== '') {
                    $('#request_txt_1').val(record_txt);
                    $('#request_txt_1').removeClass('display-hide');
                    $('#txt_1').prop('checked', true);
                } else {
                    $('#request_txt_1').val("");
                    $('#request_txt_1').addClass('display-hide');
                    $('#txt_1').prop('checked', false);
                }
//                        
                if (record_dmarc !== '') {
                    $('#request_dmarc_1').val(record_dmarc);
                    $('#request_dmarc_1').removeClass('display-hide');
                    $('#dmarc_1').prop('checked', true);
                } else {
                    $('#request_dmarc_1').val("");
                    $('#request_dmarc_1').addClass('display-hide');
                    $('#dmarc_1').prop('checked', false);
                }
                if (req_for === "req_new")
                {
                    $('#first_div').removeClass('display-hide');
                    $('#second_div').removeClass('display-hide');
                    $('#third_div').removeClass('display-hide');
                    $('#other_div_req').addClass('display-hide');
                    $('#dns_url_label').html("DNS URL")
                    $('#dns_ip_label').html("Application IP")
                    $('#req_prvw').html("NEW Request: ")
                    $('#req_new_ip').removeClass('display-hide');
                    $('#req_old_ip').addClass('display-hide');
                    $('#req_action1').val(req_for);
                    $('#ptr_1').prop('checked', false);
                }
                if (req_for === "req_modify")
                {
                    $('#first_div').removeClass('display-hide');
                    $('#second_div').removeClass('display-hide');
                    $('#third_div').removeClass('display-hide');
                    $('#other_div_req').removeClass('display-hide');
                    $('#dns_url_label').html("DNS URL")
                    $('#dns_ip_label').html("Application IP")
                    $('#other_url_label').html("OLD DNS URL")
                    $('#other_ip_label').html("OLD Application IP")
                    $('#req_prvw').html("Modify Request: ")
                    $('#req_old_ip').removeClass('display-hide');
                    $('#req_new_ip').removeClass('display-hide');
                    $('#req_action1').val(req_for);
                    $('#ptr_1').prop('checked', false);
                }
                if (req_for === "req_delete")
                {
                    $('#first_div').removeClass('display-hide');
                    $('#second_div').removeClass('display-hide');
                    $('#third_div').removeClass('display-hide');
                    $('#dns_url_label').html("OLD DNS URL")
                    $('#dns_ip_label').html("OLD Application IP")
                    $('#other_div_req').addClass('display-hide');
                    $('#req_prvw').html("Delete Request: ")
                    $('#req_new_ip').removeClass('display-hide');
                    $('#req_action1').val(req_for);
                    $('#ptr_1').prop('checked', true);
                }
                if (form_type == "dns_bulk")
                {
                    $('#first_div').addClass('display-hide');
                    $('#dns_url_label').html("DNS URL")
                    $('#dns_ip_label').html("Application IP")
                    $('#dns_bulk_preview').removeClass("display-hide");
                    $('#uploaded_filename').text(uploaded_filename);
                    $('#confirm').val(form_type);
                    $('#upload_dns').removeClass('display-hide');
                } else
                {
                    $('#upload_dns').addClass('display-hide');
                    $('#dns_bulk_preview').addClass("display-hide");
                }
                $('#req_action').val(req_for);

            }
        }
    });
</script>