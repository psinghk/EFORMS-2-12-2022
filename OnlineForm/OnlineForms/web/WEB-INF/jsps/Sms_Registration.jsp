<%@page import="com.org.bean.ImportantData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.ArrayList"%>
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
                <a href="" class="k-content__head-breadcrumb-link">Short Messaging Services</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile">
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="k-portlet__head">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 p-0">
                                <div class="k-portlet__head-label mt-4">
                                    <h3 class="k-portlet__head-title">Short Messaging Services</h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <div class="float-right">
                                    <span onclick="integration_guideline()" class="dns_history_page integration_guideline">Integration Guideline</span>&emsp;
                                    <span onclick="read_instruction()" class="dns_history_page ">Read Instruction</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="col-md-12 mt-2">
                                <div class="alert alert-secondary" style="background: #d3dae6;padding: 11px 12px 0px 16px;">
                                    <p><b>Have you completed the TRAI DLT registration under TCCCPR 2018? This compliance is mandatory. Non compliance can impact your SMS service. For further information, please drop a mail to <a href="#">smssupport@nic.in</a>.</b></p>
                                </div>
                            </div>
                            <div class="mb-5">
                                <div class="col-md-8 m-auto">
                                    <ul class="nav nav-pills nav-justified steps">
                                        <li id="sms_tab1_nav"  class="active">
                                            <a data-toggle="tab" class="step">
                                                <div class="number"><span class="step-counter">1</span><i class="fa fa-check"></i></div>
                                                <span class="desc">Step</span>
                                            </a>
                                        </li>
                                        <li id="sms_tab2_nav">
                                            <a data-toggle="tab" class="step">
                                                <div class="number"><span class="step-counter">2</span><i class="fa fa-check"></i></div>
                                                <span class="desc">Step</span>
                                            </a>
                                        </li>
                                        <li id="sms_tab3_nav">
                                            <a data-toggle="tab" class="step">
                                                <div class="number"><span class="step-counter">3</span><i class="fa fa-check"></i></div>
                                                <span class="desc">Step</span>
                                            </a>
                                        </li>
                                        <li id="sms_tab4_nav">
                                            <a data-toggle="tab" class="step">
                                                <div class="number"><span class="step-counter">4</span><i class="fa fa-check"></i></div>
                                                <span class="desc">Step</span>
                                            </a>
                                        </li>
                                    </ul>
                                    <div id="bar" class="progress progress-striped" role="progressbar">
                                        <div class="progress-bar progress-bar-striped bg-success" style="width: 25%;"> </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab1">
                                    <form action="" method="post" id="sms_form1">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">Application Details</h4>
                                        </div>

                                        <div class="col-md-12">
                                            <label for="street">SMS Services , Please select appropriate <span style="color: red">*</span></label>
                                            <div class="sms_serv_err alert alert-danger d-none"><b><span id="sms_serv_err"></span></b></div>
                                            <div class="row mb-3 mt-3 sms_preview_checks">
                                                <div class="col-md-2">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="sms_service" id="sms_serv1"  value="push" checked=""> PUSH
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <div class="col-md-2">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="sms_service" id="sms_serv2"  value="pull" > PULL
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <div class="col-md-2">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="sms_service" id="sms_serv3"  value="obd" > OBD
                                                        <span></span>
                                                    </label>
                                                </div>

                                                <div class="col-md-2">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="sms_service" id="sms_serv4"  value="missedcall" > Missed Call
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <div class="col-md-2">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="sms_service" id="sms_serv5"  value="otp"> OTP Service
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <div class="col-md-2">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="sms_service" id="sms_serv6"  value="quicksms"> QuickSMS Service
                                                        <span></span>
                                                    </label>
                                                </div>
                                            </div>          
                                            <div class="btn btn-info btn-success"> 
                                                <a href="Change_ip" style="color: #fff; font-size: 16;">
                                                    IP Change Request
                                                </a>
                                            </div>
                                            <font style="color:red"><span id="chk_push_err"></span></font>

                                        </div>

                                        <div class="col-md-12" style="padding: 10px;">
                                            <div class="alert alert-secondary" id="smstext">
                                                <div class="col-md-12">
                                                    <ul style="padding-left: 0px;">
                                                        <li><b>PUSH:</b> To send SMS from application to mobile using API(A2M)</li>
                                                        <li><b>PULL:</b> To send SMS from mobile to application using API(M2A)</li>
                                                        <li><b>OBD: </b> To send phone call (voice message) from application to subscriber</li>
                                                        <li><b>MISSED CALL: </b> Allows missed call on a predefined number to subscribe or avail a service</li>
                                                        <li><b>OTP SERVICE: </b> High priority SMS sent through application usnig SMS API</li>
                                                        <li><b>QUICK SMS: </b> NIC SMS web console for sending SMS</li>
                                                    </ul>
                                                </div>
                                            </div>                                                                   
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group" style="display:none" id="show_pull_url">
                                                <div class="col-md-4">
                                                    <label for="street">If PULL, Mention URL path <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter URL (e.g: https://abc.com)" type="text" name="pull_url" id="pull_url"  value="" maxlength="100">
                                                    <font style="color:red"><span id="pull_url_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Keyword<span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Keyword [Alphanumeric,length 1 to 15 digits]" type="text" name="pull_keyword" id="pull_keyword"  value="" maxlength="15">
                                                    <font style="color:red"><span id="pull_keyword_err"></span></font>
                                                </div>
                                                <div class="col-md-4" id="short_code_input" style="display:none">
                                                    <label for="street">Short code<span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter short code [Only Numeric are allowed, length 3 to 10 ]" type="text" name="short_code" id="short_code" value="" maxlength="10">
                                                    <font style="color:red"><span id="short_note_err"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group" style="display:none" id="show_note">
                                                <div class="col-md-6">
                                                    <label for="street">Do you have a short code?</label>
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="s_code" id="sy_code" value="y"> Yes
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="s_code" id="sn_code" value="n" checked=""> No
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="s_code" id="sb_code" value="b"> Both
                                                        <span></span>
                                                    </label>
                                                    <font style="color:red"><span id="snote_flag_err"></span></font>
                                                </div>

                                                <div class="col-sm-offset-0">
                                                </div>
                                                <div class="col-md-12">
                                                    <div class="alert alert-secondary" id="show_note_text">
                                                        <div class="col-md-12">
                                                            <ul>
                                                                <li><b>SHORT CODE</b>: Premium caller ID give by DOT (Department of Telecommunication)</li>
                                                                <li> In case of NO, NIC will provide you 10 digit Virtual Mobile Number(VMN).</li>
                                                                <li><b>VMN</b>: Virtual Mobile Number is a 10 digit pre-defined mobile number used in PULL SMS Service.</li>
                                                            </ul>
                                                        </div>
                                                    </div>                                                                   
                                                </div>
                                            </div>
                                        </div>
                                        <div id="quick_hide">
                                            <div class="col-md-12">
                                                <div class="row form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Name of the Application <span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Name of the Applicaion [characters,Numbers,dot(.) and whitespace]" type="text" name="app_name" id="app_name" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="app_name_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Application URL <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Application URL [e.g: (https://abc.com)] " type="text" name="app_url" id="app_url" value="" maxlength="100"  aria-required="true">                                                                                        
                                                        <font style="color:red"><span id="app_url_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group">
                                                    <div class="col-md-4">
                                                        <label for="street">Purpose of the application </label>
                                                        <input type="text" class="form-control" placeholder="Enter Purpose of the application [characters,dot(.) and whitespace]" name="sms_usage" value="" id="sms_usage" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="sms_usage_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Server Location<span style="color: red">*</span></label>
                                                        <select name="server_loc" class="form-control" id="server_loc">
                                                            <option value="NDC Delhi">NDC Delhi</option>
                                                            <option value="NDC Pune">NDC Pune</option>
                                                            <option value="NDC Hyderabad">NDC Hyderabad</option>
                                                            <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>
                                                            <option value="Other">Other</option>
                                                        </select>
                                                        <font style="color:red"><span id="server_loc_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4" id="server_other" style="display:none">
                                                        <label for="street">Enter server location <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Server Location Alphanumeric,whitespace and [. , - # / ( ) ]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                                                        <font style="color:red"><span id="server_txt_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group">
                                                <div class="col-md-6">
                                                    <label for="street" id="msgip"><strong>IP1</strong> from which you will access SMS Gateway <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Application IP1 [e.g: 10.10.10.10]" type="text" name="base_ip" id="base_ip" value="" maxlength="20"  aria-required="true">
                                                    <font style="color:red"><span id="base_ip_err"></span></font>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="street" id="msgip2"><strong>IP2</strong> from which you will access SMS Gateway</label>
                                                    <input class="form-control" placeholder="Enter Application IP2 [e.g: 10.10.10.10]" type="text" name="service_ip" id="service_ip" maxlength="20">
                                                    <font style="color:red"><span id="service_ip_err"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal fade" id="myModal1" role="dialog">
                                            <div class="modal-dialog">
                                                <!-- Modal content-->
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h4 class="modal-title"><b>Know the IP of your server</b></h4>
                                                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <p><b>Copy the below URL and run it from the server where your application will be hosted i.e. from which you will be accessing SMS Gateway server.</b></p><br>
                                                        <p><b><a href="https://msgapp.emailgov.in/findip.jsp" target="_blank" title="External site that opens in a new window">https://msgapp.emailgov.in/findip.jsp</a></b></p>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12 text-center mt-5 mb-5">
                                            <button class="btn btn-primary green"> Continue
                                                <i class="fa fa-angle-right"></i>
                                            </button>
                                        </div>
                                    </form>
                                </div>
                                <div class="tab-pane" id="tab2">
                                    <form action="" method="post" id="sms_form2">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">Contact Details of Technical Admin</h4>
                                        </div>
                                        <div class="col-md-12 mt-3 mb-5">
                                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                <input type="checkbox" name="t_admin" id="t_admin" > <b>Are you the technical admin of the server application?</b>
                                                <span></span>
                                            </label>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <label for="street">Name of The Technical Admin  <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Name of The Admin [characters,dot(.) and whitespace]" type="text" name="t_off_name" id="auth_off_name"  value="" maxlength="100">
                                                    <font style="color:red"><span id="tauth_off_name_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Designation <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Designation [characters,digits,whitespace,(.),(,),(-),(_),(&)]" type="text" name="tdesignation" id="designation"  value="" maxlength="100">
                                                    <font style="color:red"><span id="tdesignation_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Admin Employee Code </label>
                                                    <input class="form-control" placeholder="Enter Admin Employee Code [Only characters and digits allowed]" type="text" name="temp_code" id="emp_code"  value="" maxlength="12">
                                                    <font style="color:red"><span id="temp_code_err"></span></font>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-12">
                                            <h3 class="theme-heading-h3-popup">Office Address:</h3>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group">
                                                <div class="col-md-3">
                                                    <label for="street">Postal Address <span style="color: red">*</span></label>
                                                    <input class="form-control " placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" type="text" name="taddrs" id="addrs"  maxlength="50"  value="" aria-required="true">
                                                    <font style="color:red"><span id="tadd1error"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label>State where you are posted <span style="color: red">*</span></label>
                                                    <%
                                                        ImportantData impdata = (ImportantData) userdata.getImpData();
                                                        ArrayList state_name = (ArrayList) impdata.getStates();

                                                        String statename = "";
                                                    %>
                                                    <select id="tstate" name="tstate" theme="simple" class="form-control">
                                                        <option value="">--SELECT--</option>
                                                        <%            for (int i = 0; i < state_name.size(); i++) {
                                                                statename = state_name.get(i).toString();
                                                        %>
                                                        <option value="<%=statename%>"><%=statename%></option>
                                                        <%}
                                                        %>
                                                    </select>
                                                    <font style="color:red"><span id="tstate_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label>District Name <span style="color: red">*</span></label>
                                                    <select class='form-control' name='tcity' id='tcity'>
                                                        <option value=''>-SELECT-</option>
                                                    </select>   
                                                    <font style="color:red"><span id="tcity_err"></span></font>                                                                                        
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Pin Code <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Pin Code [Only digits(6) allowed]" type="text" name="tpin" id="pin"  maxlength="6"  value="" aria-required="true">
                                                    <font style="color:red"><span id="tpin_err"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label for="street">Telephone Number :(O)</label>
                                                    <input class="form-control" placeholder="Enter Telephone number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" type="text" name="ttel_ofc" id="tel_ofc"  maxlength="20"  value="" aria-required="true">
                                                    <font style="color:red"><span id="ttel_ofc_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Telephone Number :(R)</label>
                                                    <input class="form-control" placeholder="Enter Telephone number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" type="text" name="ttel_res" id="tel_res"  maxlength="20" value="">
                                                    <font style="color:red"><span id="ttel_res_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Mobile <span style="color: red">*</span></label>
                                                    <!-- below line updated by preeti on 9th aug 16  -->
                                                    <input class="form-control" placeholder="Enter Mobile [e.g:+919999999999]" type="text" name="tmobile" id="mobile"  value="" maxlength="15">
                                                    <font style="color:red"><span id="tmobile_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">E-mail Address <span style="color: red">*</span></label>
                                                    <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="tauth_email" id="auth_email"  value="" maxlength="50">
                                                    <font style="color:red"><span id="tauth_email_err"></span></font>
                                                </div>
                                                <span id="nicemp"></span>
                                            </div>
                                        </div>

                                        <div class="col-md-12 text-center mt-5 mb-5">
                                            <a href="javascript:;" class="btn default d-none button-previous">
                                                <i class="fa fa-angle-left"></i> Back </a>
                                            <button class="btn btn-primary green"> Continue
                                                <i class="fa fa-angle-right"></i>
                                            </button>                                                                
                                        </div>
                                    </form>
                                </div>
                                <div class="tab-pane" id="tab3">
                                    <form action="" method="post" id="sms_form3">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">Contact Details of Billing Owner</h4>
                                        </div>
                                        <div class="col-md-12 mt-3 mb-5">
                                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                <input type="checkbox" name="b_admin" id="b_admin"> <b>Are you the technical admin of the server application?</b>
                                                <span></span>
                                            </label>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <label for="street">Name of The Owner  <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Name of The Owner [characters,dot(.) and whitespace]" type="text" name="bauth_off_name" id="auth_off_name" value="" maxlength="100">
                                                    <font style="color:red"><span id="bauth_off_name_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Designation <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Designation [characters,digits,whitespace,(.),(,),(-),(_),(&)]" type="text" name="bdesignation" id="designation" value="" maxlength="100">
                                                    <font style="color:red"><span id="bdesignation_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Owner Emp Code </label>
                                                    <input class="form-control" placeholder="Enter Admin Employee Code [Only characters and digits allowed]" type="text" name="bemp_code" id="emp_code" value="" maxlength="12">
                                                    <font style="color:red"><span id="bemp_code_err"></span></font>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-12">
                                            <h3 class="theme-heading-h3-popup">Office Address:</h3>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group">
                                                <div class="col-md-3">
                                                    <label for="street">Postal Address <span style="color: red">*</span></label>
                                                    <input class="form-control " placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" type="text" name="baddrs" id="addrs" maxlength="50"  value="" aria-required="true">
                                                    <font style="color:red"><span id="badd1error"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label>State where you are posted <span style="color: red">*</span></label>
                                                    <%
                                                        ArrayList state_name1 = (ArrayList) impdata.getStates();

                                                        String statename1 = "";
                                                    %>
                                                    <select id="bstate" name="bstate" theme="simple" class="form-control">
                                                        <option value="" selected>select</option>
                                                        <%            for (int i = 0; i < state_name1.size(); i++) {
                                                                statename1 = state_name1.get(i).toString();
                                                        %>
                                                        <option value="<%=statename1%>"><%=statename1%></option>
                                                        <%}
                                                        %>
                                                    </select>
                                                    <font style="color:red"><span id="bstate_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label>District Name <span style="color: red">*</span></label>
                                                    <select class='form-control' name='bcity' id='bcity'>
                                                        <option value=''>-SELECT-</option>
                                                    </select>   
                                                    <font style="color:red"><span id="bcity_err"></span></font>                                                                                        
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Pin Code <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Pin Code [Only digits(6) allowed]" type="text" name="bpin" id="pin" maxlength="6"  value="" aria-required="true">
                                                    <font style="color:red"><span id="bpin_err"></span></font>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label for="street">Telephone Number :(O)</label>
                                                    <input class="form-control" placeholder="Enter Telephone Number STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]" type="text" name="btel_ofc" id="tel_ofc" maxlength="20" value="" aria-required="true">
                                                    <font style="color:red"><span id="btel_ofc_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Telephone Number :(R)</label>
                                                    <input class="form-control" placeholder="Enter Telephone Number STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]" type="text" name="btel_res" id="tel_res" maxlength="20" value="">
                                                    <font style="color:red"><span id="btel_res_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Mobile <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Mobile [e.g:+919999999999]" type="text" name="bmobile" id="mobile" value="" maxlength="15">
                                                    <font style="color:red"><span id="bmobile_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">E-mail Address <span style="color: red">*</span></label>
                                                    <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="bauth_email" id="auth_email" value="" maxlength="50">
                                                    <font style="color:red"><span id="bauth_email_err"></span></font>
                                                </div>
                                                <span id="nicemp"></span>
                                            </div>
                                        </div>
                                        <div class="col-md-12 text-center mt-5 mb-5">
                                            <a href="javascript:;" class="btn default button-previous d-none">
                                                <i class="fa fa-angle-left"></i> Back </a>
                                            <button class="btn btn-primary green"> Continue
                                                <i class="fa fa-angle-right"></i>
                                            </button>                                                                
                                        </div>
                                    </form>
                                </div>
                                <div class="tab-pane" id="tab4">
                                    <form method="post" action="" id="sms_form4">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">Other Details</h4>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row" id="audit_div">
                                                <div class="col-md-4">
                                                    <label for="street">Is the application security audit cleared <span style="color: red">**</span></label>
                                                    <div class="col-sm-offset-1">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" checked="" name="audit" id="audit_y"   value="Yes"> Yes
                                                            <span></span>
                                                        </label>&emsp;&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="audit" id="audit_n"  value="No"> No
                                                            <span></span>
                                                        </label>
                                                        <font style="color: red;"><span id="audit_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="col-md-4" id="audit_date_div" style="display:none;">
                                                    <label for="street">#If not cleared by audit, give date by when it will be cleared <span style="color: red">*</span></label>
                                                    <!--<input class="form-control hasDatepicker" type="text" name="datepicker1" id="datepicker1" onchange="check_audit_date(this.id, 'audit_n', 'Audit Clearance Date', 'audit_date_err');" value="" readonly="">-->
                                                    <div class="input-icon right">
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="datepicker1" id="datepicker1" placeholder="Enter Audit Clearance Date" readonly/> </div>
                                                    <font style="color:red"><span id="audit_date_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Mention IP of Staging Server Required for Testing</label>
                                                    <input class="form-control" placeholder="Enter IP of Staging Server [e.g: 10.10.10.10]" type="text" name="staging_ip" id="staging_ip" value="">
                                                    <font style="color:red"><span id="staging_ip_err"></span></font>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-12">
                                            <label for="street"><b>Monthly Expected SMS traffic: </b></label>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row" id="senderid_div">
                                                <div class="col-md-3">
                                                    <label for="street">Do you have TRAI exempted Sender Id?<span style="color: red">*</span>  <a href="" data-toggle="modal" data-target="#myModal" style="color:blue">(Know More<i class="entypo-help"></i>) </a> </label>
                                                    <div class="col-sm-offset-1">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="sender" id="sender_y" value="Yes"> Yes
                                                            <span></span>
                                                        </label>&emsp;&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" checked="" name="sender" id="sender_n"  value="No"> No
                                                            <span></span>
                                                        </label>&emsp;&emsp;&emsp;
                                                        <font style="color: red;"><span id="sender_err"></span></font>
                                                        <input type="hidden" value="34" id="sms-exempted">
                                                        <input type="hidden" value="72" id="sms-nexempted">
                                                        <input type="hidden" value="4" id="inter-exempted">
                                                        <font style="color:red"><span id="ex_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="col-md-3" id="sender_div" style="display:none;" >
                                                    <label for="street">Sender ID <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="For characters [length 6 only], for digits [length min:4, max:7 only]" type="text" name="sender_id" id="sender_id" value="" maxlength="7">
                                                    <font style="color:red"><span id="sender_id_err"></span></font>
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Projected Domestic Monthly SMS traffic <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Domestic Traffic [Only digits allowed (minimum 1000)]" type="text" name="domestic_traf" id="domestic_traf" value="" maxlength="9">                                                                                
                                                    <font style="color:red"><span id="domestic_traf_error"></span></font>   
                                                    <font style="color:blue;font-weight: bold"><span id="domestic_traf_yes"></span></font>  
                                                </div>
                                                <div class="col-md-3">
                                                    <label for="street">Projected International SMS traffic</label>
                                                    <input class="form-control" placeholder="Enter International Traffic [Only digits allowed (minimum 1000)]" type="text" name="inter_traf" id="inter_traf" value="" maxlength="9">                                                                                
                                                    <font style="color:red"><span id="inter_traf_error"></span></font>
                                                    <font style="color:blue; font-weight: bold"><span id="inter_traf_yes"></span></font>   
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal fade" id="myModal" role="dialog">
                                            <div class="modal-dialog">
                                                <!-- Modal content-->
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <button type="button" class="close" data-dismiss="modal">?</button>
                                                        <h4 class="modal-title"><b>TRAI exempted Sender ID</b></h4>
                                                    </div>
                                                    <div class="modal-body">
                                                        <p>Sender ID is the ID with which SMS is received on your mobile phone.</p><p>For Example: NICSMS</p>
                                                        <p>To integrate your application with NIC SMS gateway, you need to have a Sender ID. As per TRAI, Sender ID should be exactly 6 character length string [all alphabets].</p>
                                                        <p>There are 2 types of Sender IDs from commercial point of view</p>
                                                        <ul><li><b>TRAI Exempted Sender ID:</b> TRAI grants exemption of 5 paisa/SMS on such Sender ID. For this, you need to approach TRAI with your request at the address mentioned below.</li>
                                                            <li><b>TRAI Non-Exempted Sender ID:</b> There is no exemption on these Sender IDs. You can choose any valid Sender ID as per your preference and the same shall be configured on NIC SMS gateway.</li></ul>
                                                        <p><b>Address of TRAI:</b><br>Advisor, QoS<br>Mahanager Doorsanchar Bhawan (next to Zakir Hussain College)<br>Jawaharlal Nehru Marg New Delhi 110 002<br> Phone number: 23230404 <br> Email: adviqos@trai.gov.in<br> Website: trai.gov.in</p>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="col-md-12 mt-5 mb-5">
                                            <div class="row">
                                                <div class="col-md-6" style="text-align: right;">
                                                    <br/><label for="street">Captcha</label>
                                                    <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                    <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                </div>
                                                <div class="col-md-2">
                                                    <div class="form-group">
                                                        <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                        <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value=""> 
                                                        <font style="color:red"><span id="captchaerror"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12 text-center">
                                            <a href="javascript:;" class="btn default button-previous d-none">
                                                <i class="fa fa-angle-left"></i> Back </a>
                                            <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                            <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                            <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                            <button class="btn purple btn-success sbold" > Preview and Submit </button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="portlet light " id="form_wizard_2" style="display:none;">
                <div class="k-portlet__head">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 p-0">
                                <div class="k-portlet__head-label mt-4">
                                    <h3 class="k-portlet__head-title">Important Instructions to test the connectivity</h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <div class="float-right">
                                    <span onclick="backToMain()" class="dns_history_page integration_guideline">Integration Guideline</span>&emsp;
                                    <span onclick="read_instruction()" class="dns_history_page read_instruction">Read Instruction</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="col-md-12">
                        <ol class="pre-requisite mt-5 mb-5" >
                            <li>
                                ALL TESTING MUST BE DONE FROM SERVER FROM WHICH SMS GATEWAY NEEDS TO BE INTEGRATED.
                            </li>
                            <li>
                                Check the domain look up using nslookup command. If domain (smsgw.sms.gov.in) does not get resolved,
                                then please contact your server/firewall administrator.<br/> Refer below screenshot for using nslookup command.
                                <br/>
                                <br/>
                                <img src="assets/img/nslookupsms.png" alt="sms nslookup" />
                            </li>
                            <li>
                                Telnet smsgw.sms.gov.in (164.100.14.211) on port 443. Go to command line interface (CLI) and type
                                telnet smsgw.sms.gov.in 443 and press enter. If telnet works then connection to SMS gateway is OK.
                                <br/>
                                <br/>
                                <img src="assets/img/telnet_smsyes.png" alt="sms telnet yes" />
                            </li>
                            <li>
                                If telnet does not work, then there is connection issue. Refer below screenshot.
                                <br/>
                                <br/>
                                <img src="assets/img/telnet_smsno.png" alt="sms telnet no" />
                            </li>
                        </ol>
                        <div class="alert alert-info"><strong>Disclaimer :</strong>  NIC will not be responsible if the testing is not done from the host machine at which application is hosted.</div>
                    </div>
                </div>
            </div>
            <div class="portlet light " id="form_wizard_3" style="display:none;">
                <div class="k-portlet__head">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 p-0">
                                <div class="k-portlet__head-label mt-4">
                                    <h3 class="k-portlet__head-title">Please Read Instruction</h3>
                                </div>
                            </div>
                            <div class="col-md-6 p-0">
                                <div class="float-right">
                                    <span onclick="integration_guideline()" class="dns_history_page integration_guideline">Integration Guideline</span>&emsp;
                                    <span onclick="backToMain()" class="dns_history_page read_instruction">Read Instruction</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="col-md-12">
                        <ol type="I" class="pre-requisite mt-5 mb-5">
                            <li>
                                One possible reason for telnet failure is that your application server is behind a firewall
                                and firewall rule is not configured to allow traffic from your server to SMS Gateway.
                            </li>
                            <li>
                                In this regard you have to apply for firewall rule through <a href="https://farps.nic.in" target="_blank">https://farps.nic.in</a> as per below screenshot.
                            <center><img src="assets/img/img1.png" style="width: 550px;" alt="firewall apply" /></center>
                            <p>
                                *If both private IP and public IP are configured on your application server, then before filling up the following firewall form,
                                please confirm from your network/firewall administrator that the SMS request from your application will reach SMS server from which IP (PUBLIC or PRIVATE).
                            </p>
                            <br/>
                            <table style="width:60%; margin: 0 auto;" border="1">
                                <tr>
                                    <td style="padding:5px;">Are you the Server/ Website/ Application Owner ?</td>
                                    <td style="padding:5px;">NO</td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">Data Centre where server hosted</td>
                                    <td style="padding:5px;">NIC HQ DATA CENTER </td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">Functionality of the Server</td>
                                    <td style="padding:5px;">SMS Server </td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">OS of the Server</td>
                                    <td style="padding:5px;">LINUX</td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">IP Type</td>
                                    <td style="padding:5px;">Public Only</td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">Public IP </td>
                                    <td style="padding:5px;">164.100.14.211</td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">URL of the application hosted on above IP (if any)</td>
                                    <td style="padding:5px;"></td>
                                </tr>
                            </table>
                            </br>
                            <label for="about"><b>Public IP rule for access from Outside Systems to SMS Gateway Server </b></label>
                            <table style="width:100%; margin: 0 auto;" border="1">
                                <tr>
                                    <td style="padding:5px;"><b>From</b></td>
                                    <td style="padding:5px;"><b>TO</b></td>
                                    <td style="padding:5px;"><b>Services</b></td>
                                    <td style="padding:5px;"><b>URL of the server to be visited</b></td>
                                    <td style="padding:5px;"><b>Ports</b></td>
                                    <td style="padding:5px;"><b>Reason for opening port</b></td>
                                    <td style="padding:5px;"><b>Protocol</b></td>
                                </tr>
                                <tr>
                                    <td style="padding:5px;">YOUR APPLICATION SERVER IP</td>
                                    <td style="padding:5px;">164.100.14.211</td>
                                    <td style="padding:5px;">HTTPS</td>
                                    <td style="padding:5px;"></td>
                                    <td style="padding:5px;">443</td>
                                    <td style="padding:5px;">SMS Service</td>
                                    <td style="padding:5px;">TCP</td>
                                </tr>
                            </table>
                            <br/>
                            <p>For any issue related to firewall, NIC Cyber Security team should be contacted on 011-24305140 (Email on : appsecmon2@nic.in, appsecdev3@nic.in , aniljha@nic.in).</p>
                            </li>
                            <li>
                                After you have applied for the firewall rule to allow traffic from your server to SMS Gateway, an auto generated request ID will be generated.
                            </li>
                            <li>
                                <b>If you have the request ID, please enter the request ID in the space provided below. And click on submit button to proceed. Your form will be submitted successfully.</b>
                            </li>                                                                    
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="include/new_include/footer.jsp" />
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="sms_form5" method="post">
        <jsp:include page="include/sms_preview.jsp" />
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
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">   
                <ol type="1">
                    <li>The ID provided to you is for a single application ONLY. If a user department/division wishes to integrate additional
                        applications with the NIC SMS Gateway, then an id pertaining to that application needs to be created. Id created for
                        application A cannot be used for application B. The user department needs to contact NIC for creation of the id for
                        application B. Any violation detected by the user department will result in termination of the application integration. It is
                        the responsibility of undersigned to ensure the same.</li>                                            
                    <li><b>The authorized officer is the officer at the level of undersecretary or above working in
                            Ministries/Departments/Statutory Bodies/Autonomous Bodies of both Central and State/UT Governments.</b></li>                                            
                    <li>Concerned Department/Ministry shall be solely responsible for all the information, contents, data send and received using
                        NIC SMS gateway under this agreement. Concerned Department/Ministry further acknowledges that it shall be solely
                        responsible and undertake to maintain complete authenticity of the information/data sent and/or received and takes all
                        possible steps and measures to ensure that consistent authentic information is transmitted.</li>                                            
                    <li>Concerned Department/ Ministry shall be solely responsible at its own costs and expenses for obtaining and maintaining all
                        necessary approvals, sanctions, permissions, and licenses as  for sending and receiving SMS from the relevant
                        authorities and/or regulatory bodies, as the case may be.</li>                                            
                    <li>International SMS will only be given after approval from Reporting/Nodal/Forwarding Officer.</li>                                            
                    <li>NIC reserves the right to change any parameter relating integration of application and changing criteria of SMS sent.</li>
                    <li>NIC will not share the details regarding the traffic or id details with anyone without the due authorization from the
                        Reporting/Nodal/Forwarding Officer of the concerned department</li>
                    <li>Concerned Department/ Ministry shall , at all times during the Term, fully comply with the regulations and directions
                        issued by TRAI (Telecom Regulatory Authority of India) and the Department of Telecommunications, Government of India,
                        from time to time relating to the duties and obligations of the other service provider under the Agreement</li>                                            
                    <li>Concerned Department/ Ministry undertakes that it shall be fully responsible and liable for clearance of, in relation to third
                        party, all rights including, but not limited to, copyrights, right to privacy / publicity, etc. in relation to the publicity
                        undertaken by Concerned Department/Ministry as well as acquiring, propagating, publicizing, sharing and/or using the
                        requisite intellectual property rights including trademark and copyrights of any third party for the SMS’s being transmitted
                        by NIC SMS Gateway.</li>                                             
                    <li>Concerned Department/Ministry shall keep the account information such as userid, password provided, obtained from SMS
                        gateway operations in safe custody to avoid any misuse by unauthorized users.</li>                                            
                    <li>Any unauthorized commercial use of the services is expressly prohibited and concerned Department/Ministry shall be
                        solely responsible for all acts or omissions that occur under its account or password, including the content of any
                        transmissions through the services and the concerned Department/ Ministry shall strictly not:
                        <ol type="a">
                            <li>Use the service in connection with junk Short Messages, spamming or any unsolicited Short Messages
                                (commercial or otherwise).</li>
                            <li>Harvest or otherwise collect information about others, including email addresses, without their consent.</li>
                            <li>Create a false identity mobile phone address or header, or otherwise attempt to mislead others as to the identity
                                of the sender or the origin of the message.</li>
                            <li>Transmit through the Service, associated with the Service or publishing with the Service unlawful, harassing,
                                libelous, abusive, threatening, harmful, vulgar, obscene or otherwise objectionable material of any kind or nature.</li>
                            <li>Transmit any material that may infringe the intellectual property rights or other rights of third parties, including
                                trademark, copyright or right of publicity.</li>
                            <li>Libel, defame or slander any person, or infringe upon any person's privacy rights.</li>
                            <li>Transmit any material that contains viruses, Trojan horses, worms, time bombs; cancel bots, or any other harmful
                                or deleterious programs.</li>
                            <li>Interfere with or disrupt networks connected to the Service or violate the regulations, policies or procedures of
                                such networks.</li>
                            <li>Attempt to gain unauthorized access to the Service, other accounts, computer systems or networks connected to
                                the Service, through password mining or any other means.</li>
                            <li>Interfere with another user's use and enjoyment of the Service or another entity's use and enjoyment of similar
                                services or engage in any other activity that SMS Service Providers believes could subject it to criminal liability or
                                civil penalty or judgment. Concerned Department/ Ministry is fully responsible for the content sent through Short
                                Messages from their respective application or otherwise.</li>
                            <li>Send any Short Messages to any numbers listed under ‘Do Not Disturb’ category.</li>
                        </ol>
                    </li>                                            
                    <li><b>The user agrees to transfer a 6-month advance payment to NICSI based on the projected traffic prior to
                            integration of the application.</b></li>
                    <li><b>SMS Support shall share the billable SMS count. If the undersigned does not give go-ahead/raise query
                            within 05 working days, it will be assumed that the SMS count is certified for bill realization.</b></li>                                            
                    <li><b>The list of chargeable error codes are placed at http://sms.gov.in.</b></li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>The department shall abide by the payment terms and conditions of NICSI at all time during and after the usage of SMS gateway services and shall pay all outstanding dues if any.</li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Notice Modal start --->
<div id="myModalAlert" class="modal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Notice</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <div class="alert alert-secondary" style="background: #d3dae6;padding: 5px;">
                    <p>Have you completed the TRAI DLT registration under TCCCPR 2018? This compliance is mandatory. Non compliance can impact your SMS service. For further information, please drop a mail to <a href="#">smssupport@nic.in</a>.</p>
                </div>
                <p>Please read the Integration guidelines before Submitting the Request</p>
                <span onclick="integration_guideline()" class="span_link">Integration Guideline</span>&emsp;
                <span onclick="read_instruction()" class="span_link">Read Instruction</span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
            </div>
        </div>

    </div>
</div>
<!-- Notice Modal End -->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="sms_form_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>

<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/sms.js" type="text/javascript"></script>
<script>
                    $(document).ready(function () {
                        $('#myModalAlert').modal({
                            backdrop: 'static',
                            show: true
                        });
                    });
</script>
<%if (session.getAttribute("flag") != null) {
        String t1 = session.getAttribute("flag").toString();
        System.out.print("test by meenaxi session flag value::::" + t1);
        if (t1.equals("1")) {
%>
<script >
    $(function () {
        $('#tab1, #tab3,#tab4 ').hide();
        $('#tab2').show();
        $('#sms_tab1_nav').removeClass('active');
        $('#sms_tab1_nav').addClass('done');
        $('.progress-bar').css("width", "50%");
        $('#sms_tab2_nav').addClass('active');
    });
</script>
<% }
    if (t1.equals("2")) {
%>
<script>
    $(function () {
        $('#tab2, #tab1, #tab4').hide();
        $('#tab3').show();
        $('#sms_tab1_nav, #sms_tab2_nav').removeClass('active');
        $('#sms_tab1_nav,#sms_tab2_nav').addClass('done');
        $('.progress-bar').css("width", "75%");
        $('#sms_tab3_nav').addClass('active');
    });
</script>
<% }
    if (t1.equals("3")) {
%>
<script >
    $(function () {
        $('#tab1,#tab2,#tab3').hide();
        $('#tab4').show();
        $('#sms_tab1_nav,#sms_tab2_nav,#sms_tab3_nav ').removeClass('active');
        $('#sms_tab4_nav').addClass('active');
        $('#sms_tab1_nav,#sms_tab2_nav,#sms_tab3_nav').addClass('done');
        $('.progress-bar').css("width", "100%");
    });
</script>
<% }
    if (t1.equals("4")) {
%>
<script>
    $(function () {
        $('#tab1,#tab2,#tab3 ').hide();
        $('#tab4').show();
        $('#sms_tab1_nav, #sms_tab2_nav, #sms_tab3_nav').removeClass('active');
        $('#sms_tab4_nav').addClass('active');
        $('#sms_tab4_nav').removeClass('done');
        $('.progress-bar').css("width", "100%");
    });
</script>
<% }
    if (t1.equals("5")) {
%>
<script>
    $(function () {
        $('#form_wizard_1').hide();
        $('#form_wizard_2').show();
        $('#form_wizard_3').hide();
        $('#form_wizard_4').hide();
    });
</script>
<% }
    if (t1.equals("6")) {
%>
<script>
    $(function () {
        $('#form_wizard_1,#form_wizard_2, #form_wizard_4').css('display', 'none');
        $('#form_wizard_3').css('display', 'block');
    });
</script>
<% }
    if (t1.equals("7")) {
%>
<script>
    $(function () {
        $('#form_wizard_1,#form_wizard_2, #form_wizard_3').css('display', 'none');
        $('#form_wizard_4').css('display', 'block');
    });
</script>
<% }
    }
%>
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
            if (form_name == "SMS")
            {
                var app_name = '<s:property value="#session['prvwdetails'].app_name" />';
                var app_url = '<s:property value="#session['prvwdetails'].app_url" />';
                var sms_usage = '<s:property value="#session['prvwdetails'].sms_usage" />';
                var server_loc = '<s:property value="#session['prvwdetails'].server_loc" />';
                var server_loc_other = '<s:property value="#session['prvwdetails'].server_loc_other" />';
                var base_ip = '<s:property value="#session['prvwdetails'].base_ip" />';
                var service_ip = '<s:property value="#session['prvwdetails'].service_ip" />';
                var pull_url = '<s:property value="#session['prvwdetails'].pull_url" />';
                var pull_keyword = '<s:property value="#session['prvwdetails'].pull_keyword" />';
                var tech_name = '<s:property value="#session['prvwdetails'].tech_name" />';
                var tech_desig = '<s:property value="#session['prvwdetails'].tech_desig" />';
                var tech_emp_code = '<s:property value="#session['prvwdetails'].tech_emp_code" />';
                var tech_address = '<s:property value="#session['prvwdetails'].tech_address" />';
                var tech_city = '<s:property value="#session['prvwdetails'].tech_city" />';
                var tech_state = '<s:property value="#session['prvwdetails'].tech_state" />';
                var tech_pin = '<s:property value="#session['prvwdetails'].tech_pin" />';
                var tech_ophone = '<s:property value="#session['prvwdetails'].tech_ophone" />';
                var tech_rphone = '<s:property value="#session['prvwdetails'].tech_rphone" />';
                var tech_mobile = '<s:property value="#session['prvwdetails'].tech_mobile" />';
                var tech_email = '<s:property value="#session['prvwdetails'].tech_email" />';
                var bowner_name = '<s:property value="#session['prvwdetails'].bowner_name" />';
                var bowner_desig = '<s:property value="#session['prvwdetails'].bowner_desig" />';
                var bowner_emp_code = '<s:property value="#session['prvwdetails'].bowner_emp_code" />';
                var bowner_address = '<s:property value="#session['prvwdetails'].bowner_address" />';
                var bowner_city = '<s:property value="#session['prvwdetails'].bowner_city" />';
                var bowner_state = '<s:property value="#session['prvwdetails'].bowner_state" />';
                var bowner_pin = '<s:property value="#session['prvwdetails'].bowner_pin" />';
                var bowner_ophone = '<s:property value="#session['prvwdetails'].bowner_ophone" />';
                var bowner_rphone = '<s:property value="#session['prvwdetails'].bowner_rphone" />';
                var bowner_mobile = '<s:property value="#session['prvwdetails'].bowner_mobile" />';
                var bowner_email = '<s:property value="#session['prvwdetails'].bowner_email" />';
                var audit = '<s:property value="#session['prvwdetails'].audit" />';
                var audit_date = '<s:property value="#session['prvwdetails'].audit_date" />';
                var staging_ip = '<s:property value="#session['prvwdetails'].staging_ip" />';
                var flag_sender = '<s:property value="#session['prvwdetails'].flag_sender" />';
                var sender_id = '<s:property value="#session['prvwdetails'].sender_id" />';
                var domestic_traffic = '<s:property value="#session['prvwdetails'].domestic_traffic" />';
                var inter_traffic = '<s:property value="#session['prvwdetails'].inter_traffic" />';
                $('#app_name').val(app_name);
                $('#app_url').val(app_url);
                $('#sms_usage').val(sms_usage);
                $('#base_ip').val(base_ip);
                $('#service_ip').val(service_ip);
                $('#pull_url').val(pull_url);
                if (server_loc === "Other")
                {
                    $('#server_other').show();
                    $('#server_loc_txt').val(server_loc_other);
                    $('#server_loc').val(server_loc);
                } else {
                    $('#server_other').hide();
                    $('#server_loc').val(server_loc);
                    $('#server_loc_txt').val("");
                }
                $('#pull_url').val(pull_url);
                $('#pull_keyword').val(pull_keyword);
                $('input[name=t_off_name]').val(tech_name);
                $('input[name=tdesignation]').val(tech_desig);
                $('input[name=temp_code]').val(tech_emp_code);
                $('input[name=taddrs]').val(tech_address);
                $('input[name=tcity]').val(tech_city);
                $('select[name=tstate]').val(tech_state);
                $('input[name=tpin]').val(tech_pin);
                $('input[name=ttel_ofc]').val(tech_ophone);
                $('input[name=ttel_res]').val(tech_rphone);
                $('input[name=tmobile]').val(tech_mobile);
                $('input[name=tauth_email]').val(tech_email);
                $('input[name=bauth_off_name]').val(bowner_name);
                $('input[name=bdesignation]').val(bowner_desig);
                $('input[name=bemp_code]').val(bowner_emp_code);
                $('input[name=baddrs]').val(bowner_address);
                $('input[name=bcity]').val(bowner_city);
                $('select[name=bstate]').val(bowner_state);
                // $('input[name=bstate]').val(bowner_state);
                $('input[name=bpin]').val(bowner_pin);
                $('input[name=btel_ofc]').val(bowner_ophone);
                $('input[name=btel_res]').val(bowner_rphone);
                $('input[name=bmobile]').val(bowner_mobile);
                $('input[name=bauth_email]').val(bowner_email);
                $('#staging_ip').val(staging_ip);
                if (flag_sender === 'Yes')
                {
                    $('#sender_div').show();
                    $('#sender_id').val(sender_id)
                    $('#sender_y').prop('checked', true);
                } else {
                    $('#sender_n').prop('checked', true);
                    $('#sender_div').hide();
                    $('#sender_id').val("")
                }
                if (audit === 'Yes')
                {
                    $('#audit_date_div').hide();
                    $('#datepicker1').val("")
                    $('#audit_y').prop('checked', true);
                } else {
                    $('#audit_n').prop('checked', true);
                    $('#audit_date_div').show();
                    $('#datepicker1').val(audit_date)
                }
                $('#domestic_traf').val(domestic_traffic)
                $('#inter_traf').val(inter_traffic)
            }
        }

    });
</script>
</body>
</html>