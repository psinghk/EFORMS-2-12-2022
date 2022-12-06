<%--
    Document   : Single
    Created on : Oct 27, 2017, 10:31:57 AM
    Author     : nikki
--%>
<%@page import="com.org.bean.UserOfficialData"%>
<%@page import="com.org.bean.HodData"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="com.org.bean.UserData"%>
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
%>
<%
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    // below code added by pr on 23rdjan18
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
                <a href="" class="k-content__head-breadcrumb-link">Wi-Fi Services</a>
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile">
            <div class="form-wizard portlet light-graph">
                <div class="form-body">                                                            
                    <div class="tab-content">
                        <div class="tab-pane active" id="tab1" >
                            <div class="k-portlet__head">
                                <div class="k-portlet__head-label">
                                    <h3 class="k-portlet__head-title">WIFI Details</h3>
                                </div>
                            </div>
                            <form action="" method="post" id="wifi_form1" class="form_val"  >
                                <div class="col-md-12 display-hide mt-5" id="wifi_type_div">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="mt-widget-3">
                                                <div class="mt-head bg-blue-hoki">
                                                    <div class="mt-head-icon">
                                                        <i class="fa fa-wifi"></i>
                                                    </div>
                                                    <div class="mt-head-desc"> WIFI for Guest User</div>
                                                    <div class="mt-head-button">
                                                        <button type="button" name="guest_wifi" class="btn btn-success btn-circle btn-outline white btn-sm">Proceed</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="mt-widget-3">
                                                <div class="mt-head bg-red">
                                                    <div class="mt-head-icon">
                                                        <i class="fa fa-wifi"></i>
                                                    </div>
                                                    <div class="mt-head-desc"> WIFI for NIC User </div>
                                                    <div class="mt-head-button">
                                                        <button type="button" name="nic_wifi" value="" class="btn btn-success btn-circle btn-outline white btn-sm">Proceed</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>   
                            </form>
                            <form action="" method="post" id="wifi_form2" class="form_val" >
                                <div id="nic_wifi_div" class="display-hide nic_wifi_div">
                                    <input type="hidden" id="wifi_type" value="nic" />
                                    <div class="col-md-12 mt-5">
                                        <div class="alert alert-secondary">
                                            <div class="col-md-12">
                                                <b>NOTE:</b><br>
                                                <ul style="margin-top: 15px;padding-left: 17px;">
                                                    <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                                                    <li>Only three devices allowed per user ID.</li>                                                                                            
                                                    <li>For iPhone/iPad/MAC, write <b>ios(version)</b> in Operating System.</li>
                                                </ul>
                                            </div>
                                        </div>                                                                   
                                    </div>
                                    <%
                                        //String nic_bo = session.getAttribute("nic_employee").toString();
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
                                        String isNICemployeeOrMygov = "";
                                        // if (nic_bo.equals("true")) isNICemployeeOrMygov = "nic";
                                        //else if (dept.contains("MyGov Project")) isNICemployeeOrMygov = "mygov";
                                        if (!nic_employee && !dept.contains("MyGov Project")) { %>
                                    <div class="k-portlet__head d-none">
                                        <div class="k-portlet__head-label">
                                            <h3 class="k-portlet__head-title">Forwarding Officer Details</h3>
                                        </div>
                                    </div>
                                    <%
                                        String user_exists = LdapQuery.GetMobile(hod_email);
                                        System.out.println("user_exist::::" + user_exists);
                                        if (!user_exists.equals("error")) {
                                    %>    
                                    <div class="col-md-12 mt-3 display-hide">
                                        <div class="row form-group"> 
                                            <div class="col-md-6">
                                                <label for="street">Email <span style="color: red">*</span></label>
                                                <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email"   maxlength="50"  value="<s:property value="#session['profile-values'].hod_email"  />" <%if (isNICemployeeOrMygov.equals("mygov")) { %> readonly="" <% }%>  >
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
                                    </div>
                                    <div class="col-md-12 display-hide">
                                        <div class="row form-group">
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
                                    </div>
                                    <div class="col-md-12 display-hide">
                                        <label for="street"> If you want to update your mobile number please click on <a href="https://quicksms.emailgov.in/mobile/#/login" target="_blank">https://quicksms.emailgov.in/mobile/#/login</a></label>
                                        <div class="row form-group">
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
                                    </div>
                                    <% } else {
                                    %>
                                    <div class="row display-hide"> 
                                        <div class="col-md-6">
                                            <label for="street">Email <span style="color: red">*</span></label>
                                            <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email"   maxlength="50"  >
                                            <font style="color:red"><span id="fwd_email_err"></span></font>
                                            <font style="color:red"><span id="fwd_email_err1"></span></font>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="street">Name  <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="fwd_ofc_name" id="fwd_ofc_name" value="" maxlength="50">
                                            <font style="color:red"><span id="fwd_name_err"></span></font>
                                        </div>
                                        <span id="nicemp"></span>
                                    </div>
                                    <div class="row display-hide">
                                        <div class="col-md-6">
                                            <label for="street"> Mobile <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="fwd_ofc_mobile" id="fwd_ofc_mobile" value="" maxlength="15">
                                            <font style="color:red"><span id="fwd_mobile_err"></span></font>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="street">Telephone <span style="color: red">*</span></label>
                                            <input class="form-control" style="text-transform:lowercase;" type="text" id="fwd_ofc_tel" name="fwd_ofc_tel" placeholder="Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" value="" maxlength="15">
                                            <font style="color:red"><span id="fwd_tel_err"></span></font>
                                        </div>
                                        <span id="nicemp"></span>
                                        <label for="street"> If you want to update your mobile number please click on <a href="https://quicksms.emailgov.in/mobile/#/login" target="_blank">https://quicksms.emailgov.in/mobile/#/login</a></label>
                                    </div>
                                    <div class="row display-hide">
                                        <div class="col-md-6">
                                            <label>Designation <span style="color: red">*</span></label>
                                            <input type="text" id="fwd_ofc_desig" name="fwd_ofc_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control"  value="" /> 
                                            <font style="color:red"><span id="fwd_desig_err"></span></font>
                                        </div>
                                        <div class="col-md-6">
                                            <label>Address <span style="color: red">*</span></label>
                                            <input type="text" id="fwd_ofc_add" name="fwd_ofc_add" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                                            <font style="color:red"><span id="fwd_add_err"></span></font>
                                        </div>
                                    </div>
                                    <% }
                                        }%>
                                    <div class="k-portlet__head">
                                        <div class="k-portlet__head-label">
                                            <h3 class="k-portlet__head-title">WIFI Request Details</h3>
                                        </div>
                                    </div>

                                    <div class="col-md-12 mt-5">
                                        <div class="row form-group">
                                            <div class="col-md-4">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input style="text-transform:lowercase;" type="radio" name="wifi_process" id="wifi_req" value="request" checked=""> WIFI Request <b>(Request to register your device for wifi)</b>
                                                    <span></span>
                                                </label>
                                            </div>
                                            <div class="col-md-2">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input style="text-transform:lowercase;" type="radio" name="wifi_process" id="wifi_deleteReq"  value="req_delete"> DELETE
                                                    <span></span>
                                                </label>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input class="form-control" type="radio" name="wifi_process" id="wifi_cert" value="certificate">WIFI Certificate <b>(Request to generate certificate to use wifi)</b>
                                                    <span></span>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="wifi_req_div" class="">
                                        <font style="color:red"><span id="macduperr"></span></font>
                                        <div class="col-md-12 d-none" id="wifi_Pendingreq_data">
                                            <h6 class="mt-4"><b>Pending Request for WiFi</b></h6>
                                            <table class="table table-bordered table-sm">
                                                <thead>
                                                    <tr>
                                                        <th class='text-center'><b>S.No</b></th>
                                                        <th><b>MAC Address of the Device</b></th>
                                                        <th><b>Operating System of the Device</b></th>
                                                        <th><b>Action</b></th>
                                                    </tr>
                                                </thead>
                                                <tbody id="wifi_pending_tbody"></tbody>
                                            </table>
                                            <font style="color:red"><span id="deleteMacId_error"></span></font> 
                                        </div>
                                        <div class="col-md-12 mt-4">
                                            <div class="alert alert-secondary">
                                                <div class="col-md-12"> <b>NOTE: Sample Text</b>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12 mt-3">
                                            <div class="row form-group">
                                                <div class="col-md-4">
                                                    <label for="street">MAC Address of the Device <span style="color: red">*</span></label>
                                                    <input class="form-control class_name" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac1" id="wifi_mac1"  value="" maxlength="17">
                                                    <font style="color:red"><span id="wifi_mac1_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Operating System of the Device<span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os1" id="wifi_os1"  value="">
                                                    <font style="color:red"><span id="wifi_os1_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Device Type<span style="color: red">*</span></label>
                                                    <select name="device_type1" class="form-control" id="device_type1">
                                                        <option value="">Select</option>
                                                        <option value="Bio-Metric device">Bio-Metric device</option>
                                                        <option value="Laptop">Laptop</option>
                                                        <option value="Phone">Phone</option>
                                                        <option value="Windows">Windows</option>
                                                        <option value="Other">Other</option>
                                                    </select>
                                                    <font style="color:red"><span id="device_type_err1"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group">
                                                <div class="col-md-4">
                                                    <label for="street">MAC Address of the Device</label>
                                                    <input class="form-control class_name" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac2" id="wifi_mac2"  value="" maxlength="17">
                                                    <font style="color:red"><span id="wifi_mac2_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Operating System of the Device</label>
                                                    <input class="form-control" placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os2" id="wifi_os2"  value="">
                                                    <font style="color:red"><span id="wifi_os2_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Device Type</label>
                                                    <select name="device_type2" class="form-control" id="device_type2">
                                                        <option value="">Select</option>
                                                        <option value="Bio-Metric device">Bio-Metric device</option>
                                                        <option value="Laptop">Laptop</option>
                                                        <option value="Phone">Phone</option>
                                                        <option value="Windows">Windows</option>
                                                        <option value="Other">Other</option>
                                                    </select>
                                                    <font style="color:red"><span id="device_type_err2"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group">
                                                <div class="col-md-4">
                                                    <label for="street">MAC Address of the Device</label>
                                                    <input class="form-control class_name" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac3" id="wifi_mac3"  value="" maxlength="17">
                                                    <font style="color:red"><span id="wifi_mac3_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Operating System of the Device</label>
                                                    <input class="form-control" placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os3" id="wifi_os3"  value="">
                                                    <font style="color:red"><span id="wifi_os3_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Device Type</label>
                                                    <select name="device_type3" class="form-control" id="device_type3">
                                                        <option value="">Select</option>
                                                        <option value="Bio-Metric device">Bio-Metric device</option>
                                                        <option value="Laptop">Laptop</option>
                                                        <option value="Phone">Phone</option>
                                                        <option value="Windows">Windows</option>
                                                        <option value="Other">Other</option>
                                                    </select>
                                                    <font style="color:red"><span id="device_type_err3"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <div class="row form-group">
                                                <div class="col-md-4">
                                                    <label for="street">MAC Address of the Device</label>
                                                    <input class="form-control class_name" placeholder="Enter MAC Address (e.g: AA:AA:AA:AA:AA:AA)" type="text" name="wifi_mac4" id="wifi_mac4"  value="" maxlength="17">
                                                    <font style="color:red"><span id="wifi_mac4_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Operating System of the Device</label>
                                                    <input class="form-control" placeholder="Enter Operating System [characters,whitespace,comma(,),dot(.),hyphen(-)]" type="text" name="wifi_os4" id="wifi_os4"  value="">
                                                    <font style="color:red"><span id="wifi_os4_err"></span></font>
                                                </div>
                                                <div class="col-md-4">
                                                    <label for="street">Device Type</label>
                                                    <select name="device_type4" class="form-control" id="device_type4">
                                                        <option value="">Select</option>
                                                        <option value="Bio-Metric device">Bio-Metric device</option>
                                                        <option value="Laptop">Laptop</option>
                                                        <option value="Phone">Phone</option>
                                                        <option value="Windows">Windows</option>
                                                        <option value="Other">Other</option>
                                                    </select>
                                                    <font style="color:red"><span id="device_type_err4"></span></font>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Payal Agarwal-->
                                    <!--div to delete request-->

                                    <div id="wifi_Deletereq_div" class="display-hide">
                                        <font style="color:red"><span id="deleteMacId_error"></span></font> 
                                    </div>
                                    <div class="row mt-5">
                                        <div class="col-md-6"  style="text-align: right;">
                                            <br/><label for="street">Captcha</label>
                                            <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                            <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                        </div>
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" autocomplete="off" name="imgtxt" id="imgtxt1" maxlength="6" value=""> 
                                                <font style="color:red"><span id="captchaerror"></span></font>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-12 mt-5 mb-5 text-center"> 
                                        <!-- below line added by pr on 23rdjan18 to implement CSRF  -->  
                                        <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                        <button name="submit" value="nic" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                    </div>
                                </div>
                            </form>
                            <form action="" method="post" id="wifi_form3" class="form_val" >
                                <div id="guest_wifi_div" class="display-hide guest_wifi_div" >
                                    <input type="hidden" id="wifi_type" value="guest" />
                                    <div class="row">
                                        <div class="col-md-12">
                                            <div class="alert alert-info" style="width: 96%;margin: 15px;background-color: #f5f5f5">
                                                <b>NOTE:</b><br>
                                                <ol>
                                                    <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>                                                                                            
                                                </ol>
                                            </div>                                                                   
                                        </div>
                                    </div>
                                    <div class="row" style="padding: 10px;">
                                        <div class="col-md-12">
                                            <label for="street">WIFI Request For: <span style="color: red">*</span></label>
                                            <div style="margin-left:20px;">
                                                <input type="radio" name="wifi_request" id="wifi_request1"  value="laptop" checked=""> <span style="color:#000;">LAPTOP</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio" name="wifi_request" id="wifi_request2"  value="mobile" > <span style="color:#000;">MOBILE</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio" name="wifi_request" id="wifi_request3"  value="others" > <span style="color:#000;">OTHERS</span>                                                                                        
                                            </div>                                                                                    
                                            <font style="color:red"><span id="wifi_request_err"></span></font>
                                        </div>
                                    </div>
                                    <div class="row" style="padding: 10px;">
                                        <div class="col-md-6">
                                            <label for="street"><b>Time Duration:</b>  <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Time Duration [Numeric only]" type="text" autocomplete="off" name="wifi_time" id="wifi_time"  value="" maxlength="2">
                                            <font style="color:red"><span id="wifi_duration_err"></span></font>
                                        </div>
                                        <div class="col-md-6">
                                            <label for="street"><br/></label>
                                            <div style="margin-left:20px;">
                                                <input type="radio" name="wifi_duration" id="wifi_duration1"  value="days" checked=""> <span style="color:#000;">DAYS(Up to 90 days)</span>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <input type="radio" name="wifi_duration" id="wifi_duration2"  value="months" > <span style="color:#000;">MONTHS(UP to 3 months)</span>&nbsp;&nbsp;&nbsp;&nbsp;                                                                                        
                                            </div>                                                                                    
                                        </div>
                                    </div>   
                                    <div class="row">
                                        <div class="col-md-6" style="text-align: right;">
                                            <br/><label for="street">Captcha</label>
                                            <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                            <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                        </div>
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" autocomplete="off" name="imgtxt" id="imgtxt2" maxlength="6" value=""> 
                                                <font style="color:red"><span id="captchaerror"></span></font>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row" style="padding: 10px;">
                                        <div class="col-md-offset-5 col-md-7">
                                            <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                            <button name="submit" value="guest" class="btn purple btn-outline sbold" > Preview and Submit </button>
                                        </div>
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
<jsp:include page="include/new_include/footer.jsp" />


<div class="modal fade" id="delModal1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h6>Your Request has been Cancelled Sucessfully. </h6><button type="button" class="btn btn-primary" data-dismiss="modal" style="width:inherit;" autofocus value="no" id="delModal_no">OK</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="delModal2" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h6>Profile Request not found. </h6><form method="post" name="delc" id="delc"><input type="hidden" name="imid"><input type="hidden" name="csrf_token" value="123safdsaf"> <button type="button" class="btn btn-primary" data-dismiss="modal" style="width:inherit;" autofocus>Cancel</button></form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="delModal3" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h6>There is Some problem, please try again later. </h6><form method="post" name="delc" id="delc"><input type="hidden" name="imid"><input type="hidden" name="csrf_token" value="123safdsaf"><button type="button" class="btn btn-danger" style="width:inherit;">Yes,Sure</button> <button type="button" class="btn btn-primary" data-dismiss="modal" style="width:inherit;" autofocus value="no" >No</button></form>
            </div>
        </div>
    </div>
</div>
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="wifi_form4" method="post">
        <jsp:include page="include/wifi_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<!--Nested Modal Terms and condition -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>NOTE:</b><br>                        
                <ol id="nic_tnc" class="display-hide">                            
                    <li>Only three devices allowed per user ID.</li>                                                                                            
                    <li>For iPhone/iPad/MAC, write <b>ios(version)</b> in Operating System.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <!-- below text added by pr on 13thdec18 -->
                    <li><b>In case of Certificate Generation</b> , I hereby declare that: </li>                                                                                            
                    <li>The information provided is correct.</li>
                    <li>I am responsible for the safety of the Digital Certificate, PIN, Username and Password issued for accessing Wi-Fi
                        Service.</li>
                    <li>I undertake to surrender the Digital certificate on transfer/leaving the division.</li>
                    <li>The certificate issued will be used only for accessing the NIC Wi-Fi Service.</li>
                    <li>Will not indulge in any activity and no attempt will be made to gain unauthorized access to other NIC Websites and facilities.</li>
                    <li>I am responsible for the content/data uploaded in the servers through Wi-Fi.</li>
                </ol>
                <ol id="guest_tnc" class="display-hide">                            
                    <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>                                                                                                                        
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<!-- Nested Modal-->
<!-- Modal for confirmation -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="wifi_form_confirm">
        <%
            if (!nic_employee) {
        %>
        <jsp:include page="include/Hod_detail.jsp" />
        <%} else {
        %>
        <jsp:include page="include/wifi_detail.jsp" />
        <%}%>
    </form>
</div>       

<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/wifi.js" type="text/javascript"></script>
<script type="text/javascript">
    jQuery(document).ready(function () {
        $("#wifi_form3 #refresh").on("click", function () {
            $('#wifi_form3 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#wifi_form2 #refresh").on("click", function () {
            $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#wifi_form4 #closebtn").on("click", function () {
            $('#wifi_form3 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#wifi_form2 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#wifi_form2 #imgtxt1').val("");
            $('#wifi_form3 #imgtxt2').val("");
        });
    });
</script>
<script>
    $(document).ready(function () {
        if ('<%=nic_employee%>' !== null) {

            if ('<%=nic_employee%>' == 'true') {
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
            if (form_name == "WIFI")
            {
                var protocol = '<s:property value="#session['prvwdetails'].protocol" />';
                var prvw_ofc_name = '<s:property value="#session['prvwdetails'].prvw_ofc_name" />';
                var prvw_ofc_email = '<s:property value="#session['prvwdetails'].prvw_ofc_email" />';
                var prvw_ofc_tel = '<s:property value="#session['prvwdetails'].prvw_ofc_tel" />';
                var prvw_ofc_mobile = '<s:property value="#session['prvwdetails'].prvw_ofc_mobile" />';
                var prvw_ofc_desig = '<s:property value="#session['prvwdetails'].prvw_ofc_desig" />';
                var prvw_ofc_add = '<s:property value="#session['prvwdetails'].prvw_ofc_add" />'
                var prvw_wifitype = '<s:property value="#session['prvwdetails'].prvw_wifitype" />'
                var prvw_wifimac1 = '<s:property value="#session['prvwdetails'].prvw_wifimac1" />';
                var prvw_wifios1 = '<s:property value="#session['prvwdetails'].prvw_wifios1" />';
                var prvw_wifimac2 = '<s:property value="#session['prvwdetails'].prvw_wifimac2" />';
                var prvw_wifios2 = '<s:property value="#session['prvwdetails'].prvw_wifios2" />';
                var prvw_wifimac3 = '<s:property value="#session['prvwdetails'].prvw_wifimac3" />';
                var prvw_wifios3 = '<s:property value="#session['prvwdetails'].prvw_wifios3" />'
                var prvw_wifitype = '<s:property value="#session['prvwdetails'].prvw_wifitype" />'
                $('#fwd_ofc_name').val(prvw_ofc_name);
                $('#fwd_ofc_email').val(prvw_ofc_email);
                $('#fwd_ofc_tel').val(prvw_ofc_tel);
                $('#fwd_ofc_mobile').val(prvw_ofc_mobile);
                $('#fwd_ofc_desig').val(prvw_ofc_desig);
                $('#fwd_ofc_add').val(prvw_ofc_add);
                if (prvw_wifitype === 'nic') {
                    $('#nic_wifi_div').removeClass('display-hide');
                    $('#wifi_mac1').val(prvw_wifimac1);
                    $('#wifi_os1').val(prvw_wifios1);
                    if (prvw_wifimac2 !== '' && prvw_wifios2 !== '') {
                        $('#wifi_mac2_div').removeClass('display-hide');
                        $('#wifi_mac2').val(prvw_wifimac2);
                        $('#wifi_os2').val(prvw_wifios2);
                    } else {
                        $('#wifi_mac2_div').addClass('display-hide');
                    }
                    if (prvw_wifimac3 !== '' && prvw_wifios3 !== '') {
                        $('#wifi_mac3_div').removeClass('display-hide');
                        $('#wifi_mac3').val(prvw_wifimac3);
                        $('#wifi_os3').val(prvw_wifios3);
                    } else {
                        $('#wifi_mac3_div').addClass('display-hide');
                    }
                }
            }
        }

    });

    // below function added by pr on 24thapr19    
    $(document).ready(function () {
        $('#wifi_type_div').addClass('display-hide');
        $('#nic_wifi_div').removeClass('display-hide');
        $('#guest_wifi_div').addClass('display-hide');
    });
</script>