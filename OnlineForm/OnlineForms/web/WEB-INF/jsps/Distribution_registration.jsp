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
    String otp_type = userdata.getVerifiedOtp();
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    if (!session.getAttribute("update_without_oldmobile").equals("no")) {
        response.sendRedirect("index.jsp");
    }
    if (!session.getAttribute("update_without_oldmobile").equals("no")) {
        if (!userdata.isIsNewUser()) {
            response.sendRedirect("Mobile_registration");
        }
        response.sendRedirect("index.jsp");
    }

%>

<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<jsp:include page="include/new_include/header.jsp" />
<!-- begin:: Content -->
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
                <a href="" class="k-content__head-breadcrumb-link">Distribution List Services</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile">
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="k-portlet__head">
                    <div class="k-portlet__head-label">
                        <h3 class="k-portlet__head-title">Form Details - Step 1 of 2</h3>
                    </div>
                </div>             
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="col-md-6 m-auto">
                                <ul class="nav nav-pills nav-justified steps">
                                    <li id="dlist_tab1_nav"  class="active">
                                        <a data-toggle="tab" class="step">
                                            <div class="number"><span class="step-counter">1</span><i class="fa fa-check"></i></div>
                                            <span class="desc">Step</span>
                                        </a>
                                    </li>
                                    <li id="dlist_tab2_nav">
                                        <a data-toggle="tab" class="step">
                                            <div class="number"><span class="step-counter">2</span><i class="fa fa-check"></i></div>
                                            <span class="desc">Step</span>
                                        </a>
                                    </li>
                                </ul>
                                <div id="bar" class="progress progress-striped" role="progressbar">
                                    <div class="progress-bar progress-bar-striped bg-success" style="width: 50%;"> </div>
                                </div>
                            </div>


                            <div class="tab-content">
                                <div class="tab-pane active" id="tab1">


                                    <!-- New code for bulk request --> 

                                    <br><br>
                                    <div class="row mt-5" >
                                        <div class="col-md-12 proceed-div">                                                                                                                                                                
                                            <div class="row pr-4 pl-4">
                                                <div class="col-md-6">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="single" name="dlist_single_form" checked> Single request
                                                        <span></span>
                                                    </label>
                                                </div>
                                               <div class="col-md-6">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="bulk" name="dlist_single_form"> Bulk request
                                                        <span></span>
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>



                                    <form action="" method="post" id="dlist_form1">
                                        <div id="dlist-single-div">
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <h4 class="theme-heading-h3">Distribution List Details</h4>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="alert alert-secondary" id="smstext">
                                                    <div class="col-md-12">
                                                        <b>NOTE: Please read all instructions carefully and select the required services.</b>
                                                        <ul style="margin-top: 13px;padding-left: 15px;">
                                                            <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                                                            <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                                                            <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                                                            <!--<li>Kindly forward sub level domain entry (related to 'gov.in') through support@registry.gov.in</li>  -->
                                                            <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                                                            <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>
                                                            <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                                                            <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                                                            <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                                                        </ul>
                                                    </div>
                                                </div>                                                                   
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label for="street">Name of the list (append @lsmgr.nic.in after list name)<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="E.g: abc.def@lsmgr.nic.in [dot(.) or hyphen(-) with 6-20 characters in list name]" type="text" name="list_name" id="list_name" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="list_name_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Description of List  <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Description of List  [characters,dot(.) and whitespace] " type="text" name="description_list" id="description_list" value="" maxlength="100"  aria-required="true">                                                                                        
                                                        <font style="color:red"><span id="description_list_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12 mt-3 mb-3">
                                                <div class="row">
                                                    <div class="col-md-6" id="dlist1">
                                                        <label  for="street">Will the List be moderated ? <span style="color: red">*</span></label><br>

                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="list_mod" id="list_mod_yes" value="yes" checked> Yes(recommended)
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="list_mod" id="list_mod_no" value="no" > No
                                                            <span></span>
                                                        </label>
                                                        <font style="color:red"><span id="list_mod_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label  for="street">Are only members allowed to send mails to the list  ? <span style="color: red">*</span></label><br>
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="allowed_member" id="allowed_member_yes" value="yes" checked> Yes(recommended)
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="allowed_member" id="allowed_member_no" value="no" > No
                                                            <span></span>
                                                        </label>
                                                        <font style="color:red"><span id="allowed_member_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label  for="street">Is list temporary (if yes, indicate validity date) ? <span style="color: red">*</span></label><br>
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="list_temp" id="list_temp_yes" value="yes"> Yes
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="list_temp" id="list_temp_no" value="no" checked> No
                                                            <span></span>
                                                        </label>

                                                        <font style="color:red"><span id="list_temp_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="validity_date_show" style="display:none;">
                                                        <label  for="street">Validity date <span style="color: red">*</span></label><br>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="validity_date" id="validity_date" placeholder="Enter Validaty Date [DD-MM-YYYY]"  readonly/>
                                                        <font style="color:red"><span id="validity_date_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12 mt-3 mb-3">
                                                <label  for="street">Will list accept mail from a non-NICNET email address (from internet like gmail, yahoo etc)   ? <span style="color: red">*</span></label><br>
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" name="non_nicnet" id="non_nicnet_yes" value="yes"> Yes
                                                    <span></span>
                                                </label>&emsp;&emsp;
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" name="non_nicnet" id="non_nicnet_no" value="no" checked> No(recommended)
                                                    <span></span>
                                                </label>
                                                <font style="color:red"><span id="non_nicnet_err"></span></font>
                                            </div>
                                            <div class="col-md-3">
                                                <label for="street">Total number of member count of the list (approx) <span style="color: red">*</span></label>
                                                <input class="form-control" placeholder="Total numbers of members" type="text" name="memberCount" id="memberCount"  value="" maxlength="100" />
                                                <font style="color:red"><span id="total_member_err"></span></font>
                                            </div>

                                            <div class="col-md-12 text-center">
                                                <button class="btn btn-outline btn-primary"> Continue
                                                    <i class="fa fa-angle-right"></i>
                                                </button>
                                            </div>

                                        </div>
                                    </form>
                                    <div id="dlist-bulk-div" class="d-none mt-4">
                                        <div id="bulk-1">
                                            <form action="" method="post" id="dlist_bulkform1">
                                                <div id="bulk_uploader" class="">
                                                    <div class="col-md-12 mt-5">
                                                    <div class="alert alert-secondary">   
                                                        <div class="col-md-12">
                                                            <p><strong>(<a href="download_dlist" target="_blank" style="color: red"><i class="fa fa-download"></i> Click here to download Sample Excel-Format </a>) & the format of input file should be:</strong><br/></p>
                                                            <p>NOTE: Maximum number of rows accepted at a time is 3000. Please upload Excel file with maximum 3000 rows only.</p>
                                                            <p>User need to enter only data not a column header name like "List name, List Description etc".</p>
                                                            <p>All fields are compulsory excepted Owner Admin, Moderator Admin</p>
                                                            <p>Either owner or moderator, one record can be blank.</p>
                                                            <p>If single List have multiple owner and moderators then must write List Name in each row.</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Please upload the Excel file </label>
                                                        <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                            <input type="file" class="custom-file-input" id="user_file_dlist" name="user_file">
                                                            <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                                            <span class="fileinput-filename"> </span> &nbsp;
                                                            <font style="color:red"><span id="file_err"> </span></font>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-12">
                                                        <div class="row">
                                                            <div class="col-md-6" style="text-align: right;margin-top: 18px;">
                                                                <br/><label for="street">Captcha</label>
                                                                <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                                <img border="0" src="assets/old_assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                            </div>
                                                            <div class="col-md-2 mt-4">
                                                                <div class="form-group">
                                                                    <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                                    <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value=""  aria-required="true">
                                                                    <font style="color:red"><span id="captchaerror" class="captchaerror"></span></font>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-12 text-center">
                                                        <a href="javascript:;" class="btn default button-previous d-none" >
                                                            <i class="fa fa-angle-left"></i> Back </a>
                                                        <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                                        <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                        <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                                        <button class="btn btn-success btn-outline sbold" > Continue </button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                        <div id="bulk-2" class="display-hide">
                                            <form action="" method="post" id="dlist_bulkform2">
                                                <div class="col-md-12 ">                                                                                                                                                                
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <div class="mt-widget-3">
                                                                <div class="mt-head bg-blue-hoki">
                                                                    <div class="mt-head-icon">
                                                                        <i class="fa fa-user"></i>
                                                                    </div>
                                                                    <div class="mt-head-desc"> List of users which can be processed</div>
                                                                    <div class="mt-head-button">
                                                                        <button type="button" name="download" id="download" value="valid1" class="btn btn-circle btn-success white btn-sm">Download</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mt-widget-3">
                                                                <div class="mt-head bg-red">
                                                                    <div class="mt-head-icon">
                                                                        <i class="fa fa-users"></i>
                                                                    </div>
                                                                    <div class="mt-head-desc"> List of users which can not be processed </div>
                                                                    <div class="mt-head-button">
                                                                        <button type="button" name="download" id="download" value="not-valid1" class="btn btn-circle btn-success white btn-sm">Download</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <div class="mt-widget-3">
                                                                <div class="mt-head bg-blue-hoki">
                                                                    <div class="mt-head-icon">
                                                                        <i class="fa fa-user"></i>
                                                                    </div>
                                                                    <div class="mt-head-desc"> List of errors in Excel file</div>
                                                                    <div class="mt-head-button">
                                                                        <button type="button" name="download" id="download" value="error1" class="btn btn-circle btn-success white btn-sm">Download</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div> 
                                                <div class="row" >
                                                    <div class="col-md-12 ">  
                                                        <font style="color:red; text-align: center"><span id="error_file"></span></font>
                                                    </div>
                                                </div>
                                                <div class="col-md-12 text-center mt-5">
                                                    <button name="submit" value="preview" class="btn btn-success" > Preview and Submit </button>                                                                
                                                </div>
                                            </form>
                                        </div>
                                    </div>

                                </div>
                                <div class="tab-pane" id="tab2">
                                    <form action="" method="post" id="dlist_form2">
                                        <div id="dlist-double-div">
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <h4 class="theme-heading-h3">Owner Details</h4>
                                            </div>

                                            <div class="col-md-12">
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                    <input type="checkbox" name="owner_admin" id="owner_admin"> <b>Are you the Owner admin of the List?</b>
                                                    <span></span>
                                                </label>
                                            </div>

                                            <div class="col-md-12">
                                                <div class="row form-group card-holder" id="owner_div">

                                                    <div class="col-md-12">
                                                        <div class="row">
                                                            <div class="col-md-3">
                                                                <label for="street">Owner Name  <span style="color: red"></span></label>
                                                                <input class="form-control" placeholder="Enter Name of The Owner [characters,dot(.) and whitespace]" type="text" name="owner_name" id="auth_owner_name1"  value="" maxlength="50" minlength="2">
                                                                <font style="color:red"><span class="name_err" id="auth_owner_name1_err"></span></font>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <label for="street">Owner E-mail Address <span style="color: red"></span></label>
                                                                <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Owner Email Address [e.g:abc.xyz@nic.in OR all gov domains] " type="text" name="owner_email" id="owner_email1"  value="" maxlength="50">
                                                                <font style="color:red"><span class="email_err" id="owner_email1_err"></span></font>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <label for="street">Owner Mobile <span style="color: red"></span></label>
                                                                <input class="form-control" placeholder="Enter Mobile [e.g: 99XXXXXXXX]" type="text" name="owner_mobile" id="owner_mobile1"  value="" maxlength="15">
                                                                <font style="color:red"><span class="mobile_err" id="owner_mobile1_err"></span></font>
                                                            </div>
                                                            <div class="col-md-2 mt-4 pl-0 pt-2">
                                                                <button id="addowner" type="button" class="btn btn-primary btn-info addip"><i class="fa fa-plus fa-2x"></i> Add Record</button>

                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12 mt-4 d-none" id="owner_collection">
                                                <label for="owner_collection_tbl">Owner Data</label>
                                                <table class="table table-bordered table-striped table-sm mb-4" id="owner_collection_tbl">
                                                    <thead class="thead-light">
                                                        <tr>
                                                            <td class="text-center" width="8%">S.No</td>
                                                            <td>Owner Name</td>
                                                            <td>Owner E-mail Address</td>
                                                            <td>Owner Mobile</td>
                                                            <td class="text-center">Action</td>
                                                        </tr>
                                                    </thead>
                                                    <tbody></tbody>
                                                </table>
                                            </div>

                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <h4 class="theme-heading-h3">Moderator Details</h4>
                                            </div>
                                            <div class="col-md-12">
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                    <input type="checkbox" name="moderator_admin" id="moderator_admin"> <b>Are you the Moderator admin of the List?</b>
                                                    <span></span>
                                                </label>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group card-holder" id="moderator_div">

                                                    <div class="col-md-12">
                                                        <div class="row">
                                                            <div class="col-md-3">
                                                                <label for="street">Moderator Name  <span style="color: red"></span></label>
                                                                <input class="form-control" placeholder="Enter Name of The Admin [characters,dot(.) and whitespace]" type="text" name="t_off_name" id="auth_off_name"  value="" maxlength="50">
                                                                <font style="color:red"><span class="name_err" id="tauth_off_name_err"></span></font>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <label for="street">Moderator E-mail Address <span style="color: red"></span></label>
                                                                <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Moderator Email Address [e.g:abc.xyz@nic.in OR all gov domains] " type="text" name="tauth_email" id="auth_email"  value="" maxlength="50">
                                                                <font style="color:red"><span class="email_err" id="tauth_email_err"></span></font>
                                                            </div>
                                                            <div class="col-md-3">
                                                                <label for="street">Moderator Mobile <span style="color: red"></span></label>
                                                                <input class="form-control" placeholder="Enter Mobile [e.g: 99XXXXXXXX]" type="text" name="tmobile" id="mobile"  value="" maxlength="15">
                                                                <font style="color:red"><span class="mobile_err" id="tmobile_err"></span></font>
                                                            </div>
                                                            <div class="col-md-2 mt-4 pl-0 pt-2">
                                                                <button id="addmoderator" type="button" class="btn btn-primary btn-info addip"><i class="fa fa-plus fa-2x"></i> Add Record</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-md-12 mt-4 d-none" id="moderator_collection">
                                                <label for="moderator_collection_tbl">Moderator Data</label>
                                                <table class="table table-bordered table-striped table-sm mb-4" id="moderator_collection_tbl">
                                                    <thead class="thead-light">
                                                        <tr>
                                                            <td class="text-center" width="8%">S.No</td>
                                                            <td>Moderator Name</td>
                                                            <td>Moderator E-mail Address</td>
                                                            <td>Moderator Mobile</td>
                                                            <td class="text-center">Action</td>
                                                        </tr>
                                                    </thead>
                                                    <tbody></tbody>
                                                </table>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-6" style="text-align: right;margin-top: 18px;">
                                                        <br/><label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                        <img border="0" src="assets/old_assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                    </div>
                                                    <div class="col-md-2 mt-4">
                                                        <div class="form-group">
                                                            <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                            <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value=""  aria-required="true">
                                                            <font style="color:red"><span id="captchaerror" class="captchaerror"></span></font>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12 text-center">
                                                <a href="javascript:;" class="btn default button-previous d-none" >
                                                    <i class="fa fa-angle-left"></i> Back </a>
                                                <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                                <button class="btn btn-success btn-outline sbold" > Preview and Submit </button>
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
    <!-- END PAGE CONTENT INNER -->
</div>
<jsp:include page="include/new_include/footer.jsp" />
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="dlist_form3" method="post">
        <jsp:include page="include/distribution_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!-- /.modal1 -->
<div class="modal fade bs-modal-lg" id="large1" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="dlist_bulkform" method="post">
        <jsp:include page="include/Dlist_bulk.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal1 -->
<div class="modal fade" id="dynamicUploadEditSingle1" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Moderator Edit Form</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="dns_sigerrorMessage"></div>
                <form id="singleDomEdit1">
                    <input type="hidden" name="id" id="domid1">
                    <div class="form-group">
                        <label for="t_off_name">Name</label>
                        <input type="text" name="t_off_name" id="t_off_name" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="tauth_email">Email</label>
                        <input type="text" name="tauth_email" id="tauth_email" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="tmobile">Contact Number</label>
                        <input type="text" name="tmobile" id="tmobile" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-primary" id="singleDataEditBtnFetch1">Update</span>
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="dynamicUploadEditSingle2" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Owner Edit Form</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="dns_sigerrorMessage"></div>
                <input type="hidden" id="comming_from">
                <form id="singleDomEdit2">
                    <input type="hidden" name="id" id="domid2">
                    <div class="form-group">
                        <label for="t_off_name">Owner Name</label>
                        <input type="text" name="owner_name" id="owner_name" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="tauth_email">Owner Email</label>
                        <input type="text" name="owner_email" id="owner_email" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="tmobile">Owner Contact Number</label>
                        <input type="text" name="owner_mobile" id="owner_mobile" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-primary" id="singleDataEditBtnFetch2">Update</span>
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully and select the required services.</b>
                <br/>
                <ul class="term_ul">
                    <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <!--<li>Kindly forward sub level domain entry (related to 'gov.in') through support@registry.gov.in</li>  -->
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                </ul>
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
    <form id="dlist_form_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<!-- end -->
<div class="modal fade" id="stack777" tabindex="-1">
    <form id="dlist_form_confirm1">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<!-- end -->

<!--[if lt IE 9]>
<script src="assets/old_assets/plugins/respond.min.js"></script>
<script src="assets/old_assets/plugins/excanvas.min.js"></script>
<script src="assets/old_assets/plugins/ie8.fix.min.js"></script>
<![endif]-->
<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/dlist.js" type="text/javascript"></script>
<script src="main_js/dlist_bulk.js" type="text/javascript"></script>

<script type="text/javascript">
    jQuery(document).ready(function () {
        $("#refresh").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#closebtn").on("click", function () {
            $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#imgtxt').val("");
        });
    });
</script>
<script>
    $(document).ready(function () {
        if ('<%=nic_employee%>' !== null) {
            if ('<%=nic_employee%>' === 'true') {
                var hod_email = '<s:property value="#session['profile-values'].hod_email" />';
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
            if (form_name == "DLIST")
            {
                var list_name = '<s:property value="#session['prvwdetails'].list_name" />';
                var list_description = '<s:property value="#session['prvwdetails'].list_description" />';
                var list_mod = '<s:property value="#session['prvwdetails'].list_mod" />';
                var allowed_member = '<s:property value="#session['prvwdetails'].allowed_member" />';
                var non_nicnet = '<s:property value="#session['prvwdetails'].non_nicnet" />';
                var list_temp = '<s:property value="#session['prvwdetails'].list_temp" />';
                var validity_date = '<s:property value="#session['prvwdetails'].validity_date" />';
                var t_off_name = '<s:property value="#session['prvwdetails'].t_off_name" />';
                var tauth_email = '<s:property value="#session['prvwdetails'].tauth_email" />';
                var tmobile = '<s:property value="#session['prvwdetails'].tmobile" />';
                $("#list_name").val(list_name);
                $("#description_list").val(list_description);
                if (list_mod === 'yes')
                {
                    $('#list_mod_yes').prop('checked', true);
                } else
                {
                    $('#list_mod_no').prop('checked', true);
                }
                if (allowed_member === 'yes')
                {
                    $('#allowed_member_yes').prop('checked', true);
                } else
                {
                    $('#allowed_member_no').prop('checked', true);
                }
                if (list_temp === 'yes')
                {
                    $('#list_temp_yes').prop('checked', true);
                } else
                {
                    $('#list_temp_no').prop('checked', true);
                }
                if (non_nicnet === 'yes')
                {
                    $('#non_nicnet_yes').prop('checked', true);
                } else
                {
                    $('#non_nicnet_no').prop('checked', true);
                }
                $("#auth_off_name").val(t_off_name);
                $("#auth_email").val(tauth_email);
                $("#mobile").val(tmobile);
            }
        }
    });
</script>
</body>
</html>