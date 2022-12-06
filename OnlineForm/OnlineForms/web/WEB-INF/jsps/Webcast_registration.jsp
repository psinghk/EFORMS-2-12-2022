<%@page import="com.org.bean.UserOfficialData"%>
<%@page import="com.org.bean.HodData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="com.org.dao.EmailDao"%>
<%@page import="entities.LdapQuery"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
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
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");

    Map a = (HashMap) session.getAttribute("profile-values");
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    UserData userdata = (UserData) session.getAttribute("uservalues");
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    String employment = userdata.getUserOfficialData().getEmployment();
    HodData hodData = (HodData) userdata.getHodData();
    UserOfficialData UserOfficialData = (UserOfficialData) userdata.getUserOfficialData();
    String hod_email = "";
    String hod_name = "";
    String hod_mobile = "";
    String hod_desig = "";
    String hod_tel = "";
    //Map profile_values = (HashMap) session.getAttribute("profile-values");
    String dept = UserOfficialData.getDepartment();
    if (hodData.getEmail() != null) {
        hod_email = hodData.getEmail();
    }
    if (hodData.getName() != null) {
        hod_name = hodData.getName();
    }
    if (hodData.getMobile() != null) {

        hod_mobile = hodData.getMobile();
    }
    if (hodData.getDesignation() != null && hodData.getDesignation() != "null" && hodData.getDesignation() != "") {
        hod_desig = hodData.getDesignation();
    }
    if (hodData.getTelephone() != null) {
        hod_tel = hodData.getTelephone();
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
                <a href="" class="k-content__head-breadcrumb-link">Webcast Services</a>

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
                        <h3 class="k-portlet__head-title">Webcast Services</h3>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">                                                                                                                          
                                <div class="tab-pane active" id="tab1" >   
                                    <div class="col-md-12"  id="webcast_form">
                                        <form action="" method="post" id="webcast_form1" class="form_val">
                                            <h3 class="theme-heading-h3 mt-3 mb-4">Forwarding Officer Details</h3>
                                            <div class="form-group row"> 
                                                <div class="col-md-6">
                                                    <label for="street">Email <span style="color: red">*</span></label>
                                                    <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email"   maxlength="50"  value="<s:property value="#session['profile-values'].hod_email"  />"  >
                                                    <font style="color:red"><span id="fwd_email_err"></span></font>
                                                    <!--                                                                                <font style="color:red"><span id="fwd_email_err1"></span></font>-->
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="street">Name  <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="fwd_ofc_name" id="fwd_ofc_name"  maxlength="50" value="<s:property value="#session['profile-values'].hod_name" />" <%if (!hod_name.equals("")) { %> readonly="" <% }%>  >
                                                    <font style="color:red"><span id="fwd_name_err"></span></font>
                                                </div>
                                                <span id="nicemp"></span>
                                            </div>
                                            <div class="form-group row">
                                                <div class="col-md-6">
                                                    <label for="street"> Mobile <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="fwd_ofc_mobile" id="fwd_ofc_mobile"  maxlength="15" value="<s:property value="#session['profile-values'].hod_mobile" />"  <%if (!hod_mobile.equals("")) { %> readonly="" <% }%> >
                                                    <font style="color:red"><span id="fwd_mobile_err"></span></font>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="street">Telephone <span style="color: red">*</span></label>
                                                    <input class="form-control" style="text-transform:lowercase;" type="text" id="fwd_ofc_tel" name="fwd_ofc_tel" placeholder="Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" maxlength="15" value="<s:property value="#session['profile-values'].hod_tel" />" <%if (!hod_tel.equals("")) { %> readonly="" <% }%>>
                                                    <font style="color:red"><span id="fwd_tel_err"></span></font>
                                                </div>
                                                <span id="nicemp"></span>
                                            </div>
                                            <div class="form-group row">
                                                <div class="col-md-6">
                                                    <label>Designation <span style="color: red">*</span></label>
                                                    <input type="text" id="fwd_ofc_desig" name="fwd_ofc_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control"  value="<s:property value="#session['profile-values'].ca_design" />" <%if (!hod_desig.equals("") && hod_desig != "null" && hod_desig != null) { %> readonly="" <% }%> /> 
                                                    <font style="color:red"><span id="fwd_desig_err"></span></font>
                                                </div>
                                                <div class="col-md-6">
                                                    <label>Address <span style="color: red">*</span></label>
                                                    <input type="text" id="fwd_ofc_add" name="fwd_ofc_add" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control"  /> 
                                                    <font style="color:red"><span id="fwd_add_err"></span></font>
                                                </div>
                                            </div>
                                            <div class="form-group row">
                                                <div class="col-md-6"><h3 class="theme-heading-h3-popup mt-5 mb-4">Webcast Request Details</h3></div>
                                                <div class="col-md-6"><h3 class="theme-heading-h3-popup mt-5 mb-4"><a href="https://webcast.gov.in/" target="_blank"><u>Click here to get more webcast details</u></a></h3></div>
                                            </div>
                                            <div class="form-group row">
                                                <div class="col-md-12">
                                                    <label for="street">Type of Request: <span style="color: red">*</span></label>
                                                    <div class="mt-3" id="request_type_div">
                                                        <label class="k-radio k-radio--bold k-radio--brand" style="color: green; font-weight: 600">
                                                            <input type="radio" name="req_for" id="live"  value="live" checked="" > Live Webcast
                                                            <span></span>
                                                        </label>&emsp;&emsp;&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand" style="color: green; font-weight: 600">
                                                            <input type="radio" name="req_for" id="demand"  value="demand" > On-demand Webcast (Should be in streaming format)                                                                                  
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <font style="color:red"><span id="req_for_err"></span></font>
                                                </div>
                                            </div>
                                            <!-- WEBCAST LIVE START -->
                                            <div id="web_live" >
                                                <div class="form-group row">
                                                    <div class="col-md-6">
                                                        <label for="street">Event Name/Description (English)<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Event name and description" type="text" name="event_name_eng" id="event_name_eng" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="event_name_eng_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Event Name/Description (Hindi)<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Event name and description " type="text" name="event_name_hin" id="event_name_hin" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="event_name_hin_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-md-6">
                                                        <label for="street">Date and timings of Event<span style="color: red">*</span></label>
                                                        <div class="form-group row">
                                                            <div class='col-md-6'>
                                                                <div class="input-group date">
                                                                    <input type="text" class="form-control from_date" placeholder="Start Date & Time" id="event_start_date" name="event_start_date" readonly>
                                                                    <div class="input-group-append">
                                                                        <span class="input-group-text">
                                                                            <i class="la la-clock-o glyphicon-th"></i>
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                                <font style="color:red"><span id="event_startdate_err"></span></font>
                                                            </div>
                                                            <div class='col-md-6'>
                                                                <div class="input-group date">
                                                                    <input type="text" class="form-control to_date" placeholder="End Date & Time" name="event_end_date" id="event_end_date" readonly>
                                                                    <div class="input-group-append">
                                                                        <span class="input-group-text">
                                                                            <i class="la la-clock-o glyphicon-th"></i>
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                                <font style="color:red"><span id="event_enddate_err"></span></font>
                                                            </div>
                                                        </div>
                                                        
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Type of Event<span style="color: red">*</span></label>
                                                        <select name="event_type" class="form-control" id="event_type">
                                                            <option value="International">International</option>
                                                            <option value="National">National</option>
                                                            <option value="State Government">State Government</option>                                                                                        
                                                            <option value="Others">Others</option>
                                                        </select>
                                                        <font style="color:red"><span id="event_type_err"></span></font>
                                                    </div>
                                                </div>


                                                <div class="form-group row"> 
                                                    <div class="col-md-6">
                                                        <label for="street">Event Coordinator Name <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="event_coo_name" id="event_coo_name"  maxlength="50" value="">
                                                        <font style="color:red"><span id="event_coo_name_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Event Coordinator Email <span style="color: red">*</span></label>
                                                        <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="event_coo_email" id="event_coo_email"   maxlength="50"  value=""  >
                                                        <font style="color:red"><span id="event_coo_email_err"></span></font>
                                                    </div>                                                                                                        
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-md-6">
                                                        <label>Event Coordinator Designation <span style="color: red">*</span></label>
                                                        <input type="text" id="event_coo_design" name="event_coo_design" placeholder="Enter Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control"  value="" /> 
                                                        <font style="color:red"><span id="event_coo_desig_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Event Coordinator Mobile <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="event_coo_mobile" id="event_coo_mobile"  maxlength="15" value="" >
                                                        <font style="color:red"><span id="event_coo_mobile_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row">                                                   
                                                    <div class="col-md-12">
                                                        <label for="street">Event Coordinator Address <span style="color: red">*</span></label>
                                                        <input type="text" id="event_coo_address" name="event_coo_address" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control"  /> 
                                                        <font style="color:red"><span id="event_coo_address_err"></span></font>
                                                    </div>
                                                </div>

                                                <div class="form-group row" >
                                                    <div class="col-md-6">
                                                        <label for="street">Is live telecast will be available on DD (If Yes then please specify the channel name)<span style="color: red">*</span></label>
                                                        <div class="mt-3" id="event_telecast">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="telecast" id="telecast_yes"  value="yes" checked=""> Yes
                                                                <span></span>
                                                            </label>&emsp;&emsp;&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="telecast" id="telecast_no"  value="no" > No                                                                                 
                                                                <span></span>
                                                            </label>
                                                        </div>
                                                        <font style="color:red"><span id="telecast_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="telecast_yes_div">
                                                        <label for="street">Channel Name<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Channel name " type="text" name="channel_name" id="channel_name" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="channel_name_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6 display-hide" id="telecast_no_div">
                                                        <label for="street">How Are you planning to get live audio/video feed ?<span style="color: red">*</span></label>
                                                        <select name="live_feed" class="form-control" id="live_feed">
                                                            <option value="Own Live encoding">Own Live encoding</option>
                                                            <option value="Through VC">Through VC</option>                                                                                        
                                                            <option value="Through Hired Agency">Through Hired Agency</option>                                                                                        
                                                            <option value="By other means">By other means</option>
                                                        </select>
                                                        <font style="color:red"><span id="live_feed_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="row display-hide" id="vcid_div">                                                                                
                                                    <div class="col-md-6">
                                                        <label for="street">VC ID<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter VC ID " type="text" name="vc_id" id="vc_id" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="vc_id_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row">                                                                                
                                                    <div class="col-md-6">
                                                        <label for="street">Is it a Conference/Workshop ?<span style="color: red">*</span></label>
                                                        <div id="conf_workshop" class="mt-3">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_radio" id="conf_radio_yes"  value="yes" checked=""> Conference
                                                                <span></span>
                                                            </label>&emsp;&emsp;&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_radio" id="conf_radio_no"  value="no" > Workshop                                                                                 
                                                                <span></span>
                                                            </label>
                                                        </div>
                                                        <font style="color:red"><span id="conf_radio_err"></span></font>
                                                    </div>
                                                </div>

                                                <!-- WEBCAST CONFERENCE START NIKKI-->                                                
                                                <div class="form-group row">
                                                    <div class="col-md-6">
                                                        <label for="street">Name of Conference/Workshop<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Name of Conference/Workshop" type="text" name="conf_name" id="conf_name" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="conf_name_err"></span></font>
                                                    </div>
                                                    <div class="col-md-2">
                                                        <label for="street">Type of Conference/Workshop<span style="color: red">*</span></label>
                                                        <select name="conf_type" class="form-control" id="conf_type">
                                                            <option value="International">International </option>
                                                            <option value="National">National</option>
                                                            <option value="State Government">State Government</option>                                                                                        
                                                            <option value="Others">Others</option>
                                                        </select>
                                                        <font style="color:red"><span id="conf_type_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">City and Venue of Conference/Workshop<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter City and Venue of Conference/Workshop" type="text" name="conf_city" id="conf_city" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="conf_city_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row">

                                                    <div class="col-md-6">
                                                        <label for="street">Conference/Workshop schedule /Program details with no. of days<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Conference/Workshop schedule /Program details with no. of days" type="text" name="conf_schedule" id="conf_schedule" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="conf_schedule_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="cert_div">
                                                        <label for="street">Upload Letter/Schedule/Agenda in <b>PDF/JPG</b> format (less than 1mb)</label>
                                                        <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                            <input type="file" class="custom-file-input" multiple="multiple" id="webcast_file" name="webcast_file" >
                                                            <label class="custom-file-label text-left" for="webcast_file">Select File</label>
                                                            <span class="fileinput-filename"> </span> &nbsp;
                                                            <font style="color:red"><span id="file_err"> </span></font>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group row" >
                                                    <div class="col-md-6">
                                                        <label for="street">Number of parallel sessions<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Number of parallel sessions [Only digits]" type="text" name="conf_session" id="conf_session" maxlength="2"  aria-required="true">
                                                        <font style="color:red"><span id="conf_session_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="hall_div">
                                                        <label for="street">Number of Hall<span style="color: red">*</span></label>
                                                        <div class="row">
                                                            <div class="col-md-5 mt-2">
                                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                                    <input type="radio" name="hall_type" id="hall_multiple"  value="multiple" checked=""> Multiple
                                                                    <span></span>
                                                                </label>&emsp;&emsp;&emsp;&emsp;
                                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                                    <input type="radio" name="hall_type" id="hall_single"  value="single" > Single                                                                                
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-7">
                                                                <input class="form-control" value="" placeholder="Enter Number of Halls e.g. 1/2/3" type="text" name="hall_number" id="hall_number" maxlength="3"  aria-required="true">
                                                            </div>
                                                        </div>
                                                        <font style="color:red"><span id="hall_type_err"></span></font>
                                                    </div>                                                                                 
                                                </div>
                                                <div class="form-group row" >                                                                                    
                                                    <div class="col-md-6" >
                                                        <label for="street">Internet Connectivity /Leased line (Minimum 2 Mbps for each live stream/session)<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Type and capacity in terms of bandwidth " type="text" name="conf_bw" id="conf_bw" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="conf_bw_err"></span></font>
                                                    </div>      
                                                    <div class="col-md-6">
                                                        <label for="street">Internet/Leased line Service provider Name<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Internet/Leased line Service provider Name" type="text" name="conf_provider" id="conf_provider" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="conf_provider_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row" >                                                                                    
                                                    <div class="col-md-6" id="conf_event_div">
                                                        <label for="street">Event Management company hired ?<span style="color: red">*</span></label>
                                                        <div class="mt-3">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_event_hired" id="conf_event_hired_yes"  value="yes" checked=""> Yes
                                                                <span></span>
                                                            </label>&emsp;&emsp;&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_event_hired" id="conf_event_hired_no"  value="no" > No
                                                                <span></span>
                                                            </label>
                                                        </div>
                                                        <font style="color:red"><span id="conf_event_hired_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="flash_media_encoder">
                                                        <label for="street">Do this agency able to stream live using Flash Live Media Encoder<span style="color: red">*</span></label>
                                                        <div class="mt-3">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_flash" id="conf_flash_yes"  value="yes" checked=""> Yes
                                                                <span></span>
                                                            </label>&emsp;&emsp;&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_flash" id="conf_flash_no"  value="no" > No
                                                                <span></span>
                                                            </label>
                                                            <div id="conf_flash_no_div" class="display-hide">
                                                                <input class="form-control" value="" placeholder="Enter local setup" type="text" name="local_setup" id="local_setup" maxlength="50"  aria-required="true">
                                                            </div>
                                                        </div>
                                                        <font style="color:red"><span id="conf_flash_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row">                                                                                    
                                                    <div class="col-md-6">
                                                        <label for="street">Video coverage agency hired or not<span style="color: red">*</span></label>
                                                        <div  id="conf_video_div" class="mt-3">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_video" id="conf_video_yes"  value="yes" checked=""> Yes
                                                                <span></span>
                                                            </label>&emsp;&emsp;&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="conf_video" id="conf_video_no"  value="no" > No
                                                                <span></span>
                                                            </label>
                                                        </div>
                                                        <font style="color:red"><span id="conf_video_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="conf_contact_div">
                                                        <label for="street">Contact details of video agency<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter contact details of video agency" type="text" name="conf_contact" id="conf_contact" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="conf_contact_err"></span></font>
                                                    </div>  
                                                </div>
                                                <div class="row alert alert-secondary" style="border: 2px solid #ddd;">
                                                    <b>Duration of on-demand availability : NIC shall host the VoD/Live webcast recordings for 01 year and thereafter will be removed or handed over to the owner/Ministry /Department on request.</b>
                                                </div>                                                
                                                <!-- WEBCAST CONFERENCE END -->
                                            </div>
                                            <!-- WEBCAST LIVE END -->
                                            <!-- WEBCAST DEMAND START -->
                                            <div id="web_demand" class="display-hide">
                                                <div class="form-group row">
                                                    <div class="col-md-4">
                                                        <label for="street">Total number of video clips <span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Total number of video clips" type="text" name="event_no" id="event_no" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="event_no_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Total size in GB<span style="color: red">*</span></label>
                                                        <input class="form-control" value="" placeholder="Enter Total size in GB" type="text" name="event_size" id="event_size" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="event_size_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Media Format provided<span style="color: red">*</span></label>
                                                        <select name="media_format" class="form-control" id="media_format">
                                                            <option value="MP4">MP4</option>
                                                            <option value="MPEG">MPEG</option>
                                                            <option value="FLV">FLV</option>                                                                                        
                                                            <option value="F4V">F4V</option>
                                                        </select>
                                                        <font style="color:red"><span id="media_format_err"></span></font>
                                                    </div>
                                                </div>


                                                <div class="row alert alert-info" style="padding:10px;width: 96%;margin: 15px;background-color: #f5f5f5">
                                                    <div class="col-md-12">
                                                        <p>
                                                            <strong>
                                                                Duration of on-demand availability : NIC shall host the VoD/Live webcast recordings for 01 year and thereafter will be removed or handed over to the owner/Ministry /Department on request.
                                                            </strong>
                                                        </p>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- WEBCAST DEMAND END -->
                                            <div class="form-group row">
                                                <div class="col-md-12">
                                                    <label for="street">Payment details applicable or not? <span style="color: red">*</span></label>
                                                    <div id="payment_applicable_div" class="mt-3">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="payment" id="payment_yes"  value="yes" checked=""> Yes
                                                            <span></span>
                                                        </label>&emsp;&emsp;&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="payment" id="payment_no"  value="no" > No
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <font style="color:red"><span id="payment_err"></span></font>
                                                </div>
                                            </div> 
                                            <div id="payment_div">
                                                <div class="form-group row">
                                                    <div class="col-md-4">
                                                        <label for="street">Cheque/DD No/NEFT No/RTGS No </label>
                                                        <input class="form-control" value="" placeholder="Enter Cheque/DD No/NEFT No/RTGS No" type="text" name="cheque_no" id="cheque_no" maxlength="30"  aria-required="true">
                                                        <font style="color:red"><span id="cheque_no_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Amount </label>
                                                        <input class="form-control" value="" placeholder="Enter Amount" type="text" name="cheque_amount" id="cheque_amount" maxlength="50"  aria-required="true">
                                                        <font style="color:red"><span id="amount_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Date </label>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="cheque_date" id="cheque_date" placeholder="Enter Date [DD-MM-YYYY]" readonly/>
                                                        <font style="color:red"><span id="cheque_date_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <div class="col-md-12">
                                                        <label for="street">Bank & Branch </label>
                                                        <input class="form-control" value="" placeholder="Enter Bank & Branch" type="text" name="bank_name" id="bank_name" maxlength="100"  aria-required="true">
                                                        <font style="color:red"><span id="bank_name_err"></span></font>
                                                    </div>                                                                                
                                                </div>
                                            </div>

                                            <div class="form-group row">                                                
                                                <div class="col-md-12">
                                                    <label for="street">Any other details or remarks</label>
                                                    <input class="form-control" value="" placeholder="Enter any other details or remarks" type="text" name="remarks" id="remarks" maxlength="100"  aria-required="true">
                                                    <font style="color:red"><span id="remarks_err"></span></font>
                                                </div>                                                                                
                                            </div>

                                            <div class="form-group row">
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
                                            <div class="form-group row">
                                                <div class="col-md-12 text-center">
                                                    <input type="hidden" id="cert" value="false" />
                                                    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                     <font style="color:red"><span id="webcast_error"></span></font>
                                                    <button name="submit" value="preview" id="submit_single" class="btn btn-primary"> Preview and Submit </button>                                                                
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
    <form role="form" id="webcast_form2" method="post">
        <jsp:include page="include/webcast_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>

<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>Terms and conditions</b>
                <br/>
                <ol type="1">
                    <li>NIC shall host the VoD/ Live Webcast recordings for 01 year and thereafter will be removed or handed over to the owner/Ministry /Department on request.</li>                                            
                    <li>In case of any issue, you may contact NIC 24x7 service desk at 1800-111-555 or send mail to [ mailto:servicedesk@nic.in | servicedesk@nic.in]</li>                                            
                    <li>Please note that advance payment is a must for paid users.</li>                                            
                    <li>Please visit the URL [ https://webcast.gov.in/avail_services.html | https://webcast.gov.in/avail_services.html ] for more details
                        you can get them implemented and ask the users to apply online</li>                                            
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
    <form id="webcast_form_confirm" class="display-hide">
        <jsp:include page="include/wifi_detail.jsp" />
    </form>        
</div>
<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/webcast.js" type="text/javascript"></script>

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
</script>