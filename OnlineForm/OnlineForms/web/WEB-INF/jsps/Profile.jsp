<%@page import="com.org.dao.Ldap"%>
<%@page import="com.org.bean.HodData"%>
<%@page import="com.org.service.ProfileService"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.ArrayList"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page import="org.owasp.esapi.ESAPI"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<body>
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
<%  ProfileService profileService = new ProfileService();
    Map hodDetails = new HashMap();
    Ldap ldap = new Ldap();
    System.out.println("hod bo" + hodDetails.get("bo"));
    UserData userdata = (UserData) session.getAttribute("uservalues");
    HodData hoddata = (HodData) userdata.getHodData();
    String otp_type = userdata.getVerifiedOtp();
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    boolean isNewUser = userdata.isIsNewUser();
    hodDetails = (HashMap) profileService.getHODdetails(hoddata.getEmail());
    String nicemployeeeditable = ldap.isUserNICEmployeeButNotContainsNicEmployees(userdata.getEmail().toString());
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
                <a href="" class="k-content__head-breadcrumb-link">Profile Page</a>
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <div class="page-content-inner">
                <div class="col-md-12">
                    <!-- BEGIN PROFILE CONTENT -->
                    <div class="profile-content">
                        <div class="col-md-12">
                            <div class="portlet light-graph">
                                <div class="portlet-title tabbable-line">
                                    <div class="k-portlet__head">
                                        <div class="k-portlet__head-label">
                                            <h3 class="k-portlet__head-title">User Profile</h3>
                                        </div>
                                    </div>

                                </div>
                                <div class="col-md-12 m-auto portlet-body">
                                    <div class="tab-content">
                                        <div class="col-md-12 mt-4">
                                            <div class="alert alert-secondary"><h6>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</h6></div>
                                        </div>
                                        <!-- PERSONAL INFO TAB -->  
                                        <div class="col-md-12">
                                            <nav>
                                                <div class="nav nav-tabs" id="nav-tab" role="tablist">
                                                    <a class="nav-item nav-link active" id="t1" data-toggle="tab" href="#tab_1_1" role="tab" aria-controls="nav-home" aria-selected="true">Personal Info</a>
                                                    <a class="nav-item nav-link" id="t2" data-toggle="tab" href="#tab_1_2" role="tab" aria-controls="nav-profile" aria-selected="false">Organizational Info</a>
                                                </div>
                                            </nav>
                                            <div class="tab-content" id="nav-tabContent">
                                                <div id="tab_1_1" class="tab-pane fade show active" role="tabpanel" aria-labelledby="nav-home-tab"> 
                                                    <form id="form1" role="form" action="" method="post">    
                                                        <div class="row form-group">
                                                            <div class="col-md-6">
                                                                <label>User Name <span style="color: red">*</span></label>
                                                                <input type="text" id="user_name" name="user_name" placeholder="Enter Full Name [Only characters,dot(.) and whitespace allowed]" class="form-control" maxlength="50" value="<s:property value="#session['uservalues'].name" />"  /> 
                                                                <font style="color:red"><span id="username_error"></span></font>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label>Employee Code</label>
                                                                <input type="text" id="user_empcode" name="user_empcode" placeholder="Enter Employee Code [Only characters and digits allowed]" maxlength="17" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.employeeCode" />" /> 
                                                                <font style="color:red"><span id="userempcode_error"></span></font>
                                                            </div>
                                                        </div>
                                                        <div class="row form-group">
                                                            <div class="col-md-6">
                                                                <label>Mobile <span style="color: red">*</span><% if (!isNewUser) {%> <a class="fupload_2" title="Click here to Upload sealed and sigisNewUserned form in PDF Format ">
<!--                                                                        <span style="color: dodgerblue;font-family:initial"><b>update mobile number</b></span>-->
                                                                        <!--                                                                                            <i class=""></i>      <img src="assets/images/update_mobile.gif" alt="logo" id="header-logo" class="wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.4s">-->
                                                                    </a><% } %></label>
                                                                    <%
                                                                        String endMobile = "";
                                                                        String finalString = "";
                                                                        System.out.println("mobile on profile" + userdata.getMobile());
                                                                        if (userdata.getMobile() != null) {
                                                                            if (!userdata.getMobile().equals("")) {
                                                                                String startMobile = userdata.getMobile().substring(0, 3);
                                                                                if (userdata.getMobile().contains("+91")) {
                                                                                    endMobile = userdata.getMobile().substring(10, 13);
                                                                                } else if (userdata.getMobile().startsWith("91")) {
                                                                                    endMobile = userdata.getMobile().substring(10, 12);
                                                                                } else {
                                                                                    endMobile = userdata.getMobile().substring(userdata.getMobile().length() - 4);
                                                                                    // endMobile = userdata.getMobile().substring(8, 10);
                                                                                }
                                                                                finalString = startMobile + "XXXXXXX" + endMobile;
                                                                            }
                                                                        }
                                                                        if (userdata.isIsEmailValidated()) {
                                                                    %>
                                                                <input type="text" id="user_mobile" name="user_mobile" placeholder="Enter Mobile Number [e.g:+919999999999]" class="form-control" maxlength="13" value="<%= finalString%>"/> 
                                                                <% } else { %>
                                                                <input type="text" autocomplete="off" id="user_mobile" name="user_mobile" placeholder="Enter Mobile Number [e.g:+919999999999]" class="form-control" maxlength="13"  value="<s:property value="#session['uservalues'].mobile" />" />

                                                                <% }
                                                                %>
                                                                <font style="color:red"><span id="usermobile_error"></span></font>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label>Email Address <span style="color: red">*</span></label>
                                                                <input type="text" id="user_email" name="user_email" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" class="form-control" maxlength="100" value="<s:property value="#session['uservalues'].email" />" /> 
                                                                <font style="color:red"><span id="useremail_error"></span></font>
                                                            </div>
                                                        </div>
                                                        <div class="row form-group">
                                                            <div class="col-md-6">
                                                                <label>Telephone Number(O)</label>
                                                                <input type="text" id="user_ophone" name="user_ophone" placeholder="Enter Official Telephone Number [STD CODE-TELEPHONE]" maxlength="15" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.officePhone" />" />
                                                                <font style="color:red"><span id="userophone_error"></span></font>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label>Telephone Number(R)</label>
                                                                <input type="text" id="user_rphone" name="user_rphone" placeholder="Enter Residence Telephone Number [STD CODE-TELEPHONE]" maxlength="15" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.rphone" />" /> 
                                                                <font style="color:red"><span id="userrphone_error"></span></font>
                                                            </div>
                                                        </div>
                                                        <div class="row form-group">
                                                            <div class="col-md-6">
                                                                <label>Designation <span style="color: red">*</span></label>
                                                                <input type="text" id="user_design" name="user_design" placeholder="Enter Designation [Only characters,digits,whitespace and [. , - &] allowed]"  maxlength="50" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.designation" />" /> 
                                                                <font style="color:red"><span id="userdesign_error"></span></font>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label>Enter Your Official Address <span style="color: red">*</span></label>
                                                                <input type="text" id="user_address" name="user_address" placeholder="Enter Your Official Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.officeAddress" />" /> 
                                                                <font style="color:red"><span id="useraddress_error"></span></font>
                                                            </div>
                                                            <div class="col-md-4" id="desigadd" style="display: none">
                                                                <label>Enter Your Alternate Email Address </label>
                                                                <input type="text" id="user_alt_address" name="user_alt_address" placeholder="Enter Your Alternate Email Address [e.g:abc.xyz@gov.in]" maxlength="100" class="form-control" value="<s:property value="#session['uservalues'].alternate_email" />"  /> 
                                                                <font style="color:red"><span id="user_alt_address_err"></span></font>
                                                            </div>
                                                        </div>
                                                        <div class="row form-group">
                                                            <div class="col-md-4">
                                                                <label>State where you are posted <span style="color: red">*</span></label>
                                                                <%
                                                                    ImportantData impdata = (ImportantData) userdata.getImpData();
                                                                    ArrayList state_name = (ArrayList) impdata.getStates();
                                                                    String statename = "";
                                                                %>
                                                                <select id="user_state" name="user_state" theme="simple" class="form-control">
                                                                    <option value="" selected>select</option>
                                                                    <%            for (int i = 0; i < state_name.size(); i++) {
                                                                            statename = state_name.get(i).toString();
                                                                    %>
                                                                    <option value="<%=statename%>"><%=statename%></option>
                                                                    <%}
                                                                    %>
                                                                </select>
                                                                <font style="color:red"><span id="userstate_error"></span></font>
                                                            </div>
                                                            <div class="col-md-4">
                                                                <label>District/City Name <span style="color: red">*</span></label>
                                                                <select class='form-control' name='user_city' id='user_city'>
                                                                    <option value=''>-SELECT-</option>
                                                                </select>   
                                                                <font style="color:red"><span id="usercity_error"></span></font>                                                                                        
                                                            </div>                                                                              
                                                            <div class="col-md-4">
                                                                <label>Pin Code <span style="color: red">*</span></label>
                                                                <input type="text" id="user_pincode" name="user_pincode" maxlength="6" placeholder="Enter Pin Code [Only digits(6) allowed]" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.pincode" />" /> 
                                                                <font style="color:red"><span id="userpincode_error"></span></font>
                                                            </div>
                                                        </div>
                                                        <div class="row"  style="padding:10px;">

                                                        </div>
                                                        <div class="col-md-12 text-center mt-5">                                                                                   
                                                            <button type="submit" class="btn btn-primary" > CONTINUE </button>
                                                        </div>
                                                    </form>
                                                </div>
                                                <div id="tab_1_2" class="tab-pane fade" role="tabpanel" aria-labelledby="nav-profile-tab">
                                                    <form id="form2"  action="" method="post" >
                                                        <input type="hidden" id="otp_type" value="" />
                                                        <input type="hidden" id="emailvalidate" value="" />
                                                        <input type="hidden" id="isIsNewUser" value="" />
                                                        <input type="hidden" id="isIsNicEmployee" value="" />
                                                        <div class="row form-group">
                                                            <div class="col-md-4">
                                                                <label>Organization Category <span style="color: red">*</span></label>                                                                                    
                                                                <select class="form-control" id="user_employment" name="user_employment">
                                                                    <option value="">--Select--</option>
                                                                    <option value="Central">Central</option>
                                                                    <option value="State">State</option>
                                                                    <option value="Psu">PSU</option>
                                                                    <option value="Const">Constitutional Bodies</option>
                                                                    <option value="Nkn">Nkn Institutes</option>
                                                                    <option value="Project">Project</option>
                                                                    <option value="UT">Union Territory</option>
                                                                    <option value="Others">Others</option>
                                                                </select>
                                                                <font style="color:red"><span id="useremployment_error"></span></font>
                                                            </div>
                                                            <div id="central_div" class="col-md-8" style="display:none">
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <label for="street">Ministry/Organization <span style="color: red">*</span></label>
                                                                        <select class='form-control' name='min' id='min'>
                                                                            <option value=''>-SELECT-</option>
                                                                        </select>
                                                                        <font style="color:red"><span id="minerror"></span></font>
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="street">Department/Division/Domain <span style="color: red">*</span></label>
                                                                        <select class='form-control' name='dept' id='dept' >
                                                                            <option value=''>-SELECT-</option>
                                                                        </select>   
                                                                        <font style="color:red"><span id="deperror"></span></font>                                                                                        
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div id="state_div" class="col-md-8" style="display:none">
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <label for="street">State <span style="color: red">*</span></label>
                                                                        <select class='form-control' id="stateCode" name="stateCode">
                                                                            <option value=''>-SELECT-</option>
                                                                        </select>
                                                                        <font style="color:red"><span id="state_error"></span></font>                                                                                                                                                                     
                                                                    </div>
                                                                    <div class="col-md-6">
                                                                        <label for="street">Department <span style="color: red">*</span></label>
                                                                        <select class='form-control' name='state_dept' id='Smi'>
                                                                            <option value=''>-SELECT-</option>
                                                                        </select>       
                                                                        <font style="color:red"><span id="smierror"></span></font>                                                                                        
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div id="other_div" class="col-md-8" style="display:none">
                                                                <div class="row">
                                                                    <div class="col-md-6">
                                                                        <div class="form-group">
                                                                            <label for='street'>Organization Name <span style='color: red'>*</span></label>
                                                                            <select class='form-control' name='org' id='Org'>
                                                                                <option value=''>-SELECT-</option>
                                                                            </select>
                                                                        </div>
                                                                        <font style='color:red'><span id='org_error'></span></font> 
                                                                    </div>

                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row mt-3 mb-3">
                                                            <div class="col-md-6 ">
                                                                <div id="other_text_div" class="display-hide" >
                                                                    <label>Other <span style="color: red">*</span></label>
                                                                    <input type="text" id="other_dept" name="other_dept" placeholder="Enter Department Name [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),ampersand(&) allowed]" maxlength="100" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.other_dept" />" /> 
                                                                    <font style="color:red"><span id="other_dep_error"></span></font>
                                                                </div> 
                                                            </div>
                                                        </div>
                                                        <div class="row form-group">                                                                               
                                                            <s:if test="%{#session['hod_values'].error==null  }">                                                                                        
                                                                <div class="col-md-6">
                                                                    <label>Reporting/Nodal/Forwarding Officer Email <span style="color: red">*</span></label>
                                                                    <input type="text" id="hod_email" name="hod_email" placeholder="Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]" maxlength="100" class="form-control"  value="<s:property value="#session['uservalues'].hodData.email" />" /> 
                                                                    <font style="color:red"><span id="hodemail_error"></span></font>
                                                                </div>
                                                            </s:if>
                                                            <s:else>                                                                                   
                                                                <div class="col-md-6">
                                                                    <label>Reporting/Nodal/Forwarding Officer Email<span style="color: red">*</span></label>
                                                                    <input type="text" id="hod_email" name="hod_email" placeholder="Enter Reporting/Nodal/Forwarding Officer Email [e.g: abc.xyz@zxc.com]" maxlength="100" class="form-control"  value="<s:property value="#session['uservalues'].hodData.email" />"/> 
                                                                    <font style="color:red"><span id="hodemail_error"></span></font>
                                                                </div>
                                                            </s:else>
                                                            <div class="col-md-6">
                                                                <label>Reporting/Nodal/Forwarding Officer Name <span style="color: red">*</span></label>
                                                                <input type="text" id="hod_name" name="hod_name" placeholder="Enter Reporting/Nodal/Forwarding Officer Name [Only characters,dot(.) and whitespace allowed]" maxlength="50" class="form-control" value="<s:property value="#session['uservalues'].hodData.name" />" /> 
                                                                <font style="color:red"><span id="hodname_error"></span></font>
                                                            </div>  
                                                        </div>
                                                        <div class="row form-group">
                                                            <div class="col-md-6">
                                                                <label>Reporting/Nodal/Forwarding Officer Mobile <span style="color: red">*</span></label>
                                                                <%
                                                                    String ROendMobile = "";
                                                                    String ROfinalString = "";
                                                                    if (userdata.getHodData().getMobile() != null) {
                                                                        if (!userdata.getHodData().getMobile().equals("")) {
                                                                            System.out.println("mobile on profile" + userdata.getHodData().getMobile());
                                                                            String ROstartMobile = userdata.getHodData().getMobile().substring(0, 3);
                                                                            if (userdata.getHodData().getMobile().contains("+91") && ROstartMobile.length() == 13) {
                                                                                ROendMobile = userdata.getHodData().getMobile().substring(10, 13);
                                                                            } else if (userdata.getMobile().startsWith("91") && ROstartMobile.length() == 12) {
                                                                                ROendMobile = userdata.getHodData().getMobile().substring(10, 12);
                                                                            } else {
                                                                                ROendMobile = userdata.getHodData().getMobile().substring(userdata.getHodData().getMobile().length() - 4);
                                                                                // endMobile = userdata.getMobile().substring(8, 10);
                                                                            }
                                                                            ROfinalString = ROstartMobile + "XXXXXXX" + ROendMobile;
                                                                        }
                                                                    }
                                                                    if (userdata.isIsEmailValidated() || userdata.getHodData().isGovEmployee()) {
                                                                %>
                                                                <input type="text" id="hod_mobile" name="hod_mobile" placeholder="Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999]" maxlength="15" class="form-control" value="<%= ROfinalString%>" /> 

                                                                <% } else {%>
                                                                <input type="text" id="hod_mobile" name="hod_mobile" placeholder="Enter Reporting/Nodal/Forwarding Officer Mobile Number [e.g: +919999999999]" maxlength="15" class="form-control" value="<s:property value="#session['uservalues'].hodData.mobile" />" /> 

                                                                <% } %>
                                                                <font style="color:red"><span id="hodmobile_error"></span></font>
                                                            </div>                                                                                
                                                            <div class="col-md-6">
                                                                <label>Reporting/Nodal/Forwarding Officer Telephone <span style="color: red">*</span></label>
                                                                <input type="text" id="hod_tel" name="hod_tel" placeholder="Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE-TELEPHONE]" maxlength="15" class="form-control" value="<s:property value="#session['uservalues'].hodData.telephone" />"  />
                                                                <font style="color:red"><span id="hodtel_error"></span></font>
                                                            </div>
                                                        </div>


                                                       

                                                        <div class="row">
                                                            <div class="col-md-12">
                                                                <label>Reporting/Nodal/Forwarding Officer Designation <span style="color: red">*</span></label>
                                                                <input type="text" id="ca_design" name="ca_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control" value="<s:property value="#session['uservalues'].hodData.designation" />"  /> 
                                                            </div>
                                                            <font style="color:red"><span id="ca_desg_error"></span></font>
                                                        </div>
                                                        <div id="under_sec_details" class="display-hide">
                                                            <div class="row">                                                                               
                                                                <div class="col-md-6">
                                                                    <label>Under secretary/Joint secretary/Secretary Email <span style="color: red">*</span></label>
                                                                    <input type="text" id="under_sec_email" name="under_sec_email" placeholder="Enter Under secretary/Joint secretary/Secretary Email [e.g: abc.xyz@zxc.com]" maxlength="100" class="form-control"  value="<s:property value="#session['uservalues'].userOfficialData.under_sec_email"/>" /> 
                                                                    <font style="color:red"><span id="under_sec_email_error"></span></font>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label>Under secretary/Joint secretary/Secretary Name <span style="color: red">*</span></label>
                                                                    <input type="text" id="under_sec_name" name="under_sec_name" placeholder="Enter Under secretary/Joint secretary/Secretary Name [Only characters,dot(.) and whitespace allowed]" maxlength="50" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.under_sec_name"/>" /> 
                                                                    <font style="color:red"><span id="under_sec_name_error"></span></font>
                                                                </div>  
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <label>Under secretary/Joint secretary/Secretary Mobile <span style="color: red">*</span></label>
                                                                    <%

                                                                        String USendMobile = "";
                                                                        String USfinalString = "";
                                                                        if (userdata.getUserOfficialData() != null) {
                                                                            if (userdata.getUserOfficialData().getUnder_sec_mobile() != null) {
                                                                                if (!userdata.getUserOfficialData().getUnder_sec_mobile().equals("")) {
                                                                                    System.out.println("userdata.getUserOfficialData().getUnder_sec_mobile()" + userdata.getUserOfficialData().getUnder_sec_mobile());
                                                                                    System.out.println("mobile on profile" + userdata.getUserOfficialData().getUnder_sec_mobile());
                                                                                    String USstartMobile = userdata.getUserOfficialData().getUnder_sec_mobile().substring(0, 3);
                                                                                    if (userdata.getUserOfficialData().getUnder_sec_mobile().contains("+91")) {
                                                                                        USendMobile = userdata.getUserOfficialData().getUnder_sec_mobile().substring(10, 13);
                                                                                    } else if (userdata.getUserOfficialData().getUnder_sec_mobile().startsWith("91")) {
                                                                                        USendMobile = userdata.getUserOfficialData().getUnder_sec_mobile().substring(10, 12);
                                                                                    } else {
                                                                                        USendMobile = userdata.getUserOfficialData().getUnder_sec_mobile().substring(userdata.getUserOfficialData().getUnder_sec_mobile().length() - 4);
                                                                                        // endMobile = userdata.getMobile().substring(8, 10);
                                                                                    }
                                                                                    USfinalString = USstartMobile + "XXXXXXX" + USendMobile;
                                                                                }
                                                                            }
                                                                        }
                                                                    %>

                                                                    <input type="text" id="under_sec_mobile" name="under_sec_mobile" placeholder="Enter Under secretary/Joint secretary/Secretary Mobile Number [e.g: +919999999999]" maxlength="15" class="form-control" value="<%= USfinalString%>"  />
                                                                    <font style="color:red"><span id="under_sec_mobile_error"></span></font>
                                                                </div>                                                                                
                                                                <div class="col-md-6">
                                                                    <label>Under secretary/Joint secretary/Secretary Telephone <span style="color: red">*</span></label>
                                                                    <input type="text" id="under_sec_tel" name="under_sec_tel" placeholder="Enter Under secretary/Joint secretary/Secretary Telephone Number [STD CODE-TELEPHONE]" maxlength="15" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.under_sec_tel" />" />
                                                                    <font style="color:red"><span id="under_sec_tel_error"></span></font>
                                                                </div>
                                                            </div>
                                                            <div class="row">
                                                                <div class="col-md-12">
                                                                    <label>Under secretary/Joint secretary/Secretary Designation <span style="color: red">*</span></label>
                                                                    <input type="text" id="under_sec_desig" name="under_sec_desig" placeholder="Enter Under secretary/Joint secretary/Secretary Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control" value="<s:property value="#session['uservalues'].userOfficialData.under_sec_desig" />" /> 
                                                                </div>
                                                                <font style="color:red"><span id="under_sec_desg_error"></span></font>
                                                            </div>
                                                        </div>
                                                        <div class="mt-4 mb-4">
                                                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                                <input type="checkbox" name="tnc1" id="tnc1" checked="checked" required> I declare that my Reporting/Nodal/Forwarding Officer belongs to the same Ministry/Department from which i belong.
                                                                <span></span>
                                                            </label>
                                                            <font style="color:red"><span id="tnc1_error"></span></font>
                                                        </div>
                                                        <div class="row display-hide" id="rodetails" style="padding-left: 20px;">
                                                            <label >
                                                                <strong>NOTE: If the Reporting/Nodal/Forwarding Officer details are not correct, please send the details to eforms[at]nic[dot]in for updation. </strong>
                                                                <span></span>
                                                            </label>                                                                               
                                                        </div>
                                                        <div class="alert alert-secondary">
                                                            <!--  modified by pr on 12thapr18  -->  
                                                            <strong>NOTE:</strong><br>
                                                            <ul><li><strong> If any "PSU/Ministry/Department" needs to be added, please send the details to <span style='color: red'>eforms[at]nic[dot]in</span> </strong></li>
                                                                    <%                                            if (session.getAttribute("nic_employee") != null) {
                                                                            if (!session.getAttribute("user_bo").equals("NIC Employees")) {
                                                                    %>  
                                                                <li><strong> If you are not able to update your Reporting/Nodal/Forwarding Officer's email address, please send the details to <span style='color: red'>eforms[at]nic[dot]in</span> to update it. </strong></li>
                                                                    <% }
                                                                        }%>  
                                                                    <%                                            if (session.getAttribute("nic_employee") != null) {
                                                                            if (session.getAttribute("user_bo").equals("NIC Employees")) {
                                                                    %>             
                                                                <li><strong>Your Reporting/Nodal/Forwarding Officer details are being taken from NIC OAD division, incase of any discrepancy please contact NIC OAD division for the same.</strong></li>
                                                                    <% }
                                                                        }%>  
                                                            </ul>
                                                            <span></span>
                                                        </div>
                                                        <div class="col-md-12 text-center">                                                                                   
                                                            <button type="submit" class="btn btn-success" > SUBMIT </button>
                                                        </div>
                                                        <div class="modal fade" id="otp_modal" role="dialog">
                                                            <div class="modal-dialog">
                                                                <!-- Modal content-->
                                                                <div class="modal-content">
                                                                    <div class="modal-header">
                                                                        <button type="button" class="close" data-dismiss="modal">×</button>
                                                                        <h4 class="modal-title"><b>OTP verification</b></h4>
                                                                    </div>
                                                                    <div class="modal-body">                                                                                               
                                                                        <div class="form-group" id="mobilemod" style="display: none">
                                                                            <label id="mob_otp" for="otp">One Time Password on Mobile</label>
                                                                            <input class="form-control" name="otp_sms" id="otp_sms_mobile1"  placeholder="Enter otp received on Mobile" value="" maxlength="6" autofocus/> 
                                                                            <br/>
                                                                        </div>
                                                                        <div class="form-group" id="emailmod" style="display: none">
                                                                            <label id="em_otp" for="otp">One Time Password on Email</label>
                                                                            <input class="form-control" name="otp_sms" id="otp_sms_email1"  placeholder="Enter otp received on Email" value="" maxlength="6" autofocus/> 
                                                                            <br/>
                                                                        </div>
                                                                        <br/>
                                                                        <div class="col-md-9">
                                                                        </div>
                                                                    </div>
                                                                    <div class="modal-footer">
                                                                        <font style="color:red; float:left"><span id="otp_error_modal"></span></font>   
                                                                        <button type="submit" id="verify_profile_otp" class="btn btn-primary" >Verify</button>                                
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div> 
                                                    </form>
                                                    <!-- END PERSONAL INFO TAB -->
                                                    <!-- Organizational INFO TAB -->                                                                   
                                                    <!-- END of CHANGE Organizational TAB -->                                                                       
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- END PROFILE CONTENT -->
                    </div>
                </div>
                <!-- END PAGE CONTENT INNER -->
            </div>
        </div>
        <!-- END PAGE CONTENT BODY -->
        <!-- END CONTENT BODY -->
    </div>
    <!-- END CONTENT -->
</div>

<!-- File Upload Modal Start -->
<div class="modal fade bd-modal-lg" id="fileUpload" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4>Update Mobile Number</h4> 
            </div>
            <div class="modal-body">
                <div class="portlet-body">
                    <button type="button" id="generate_code" class="btn btn-primary rand">Generate Random Code </button>
                    <div class="row display-hide" id="old_code">
                        <div class="col-md-12">
                            <label>Enter Code</label>
                            <input type="text" class="form-control" name="oldcode" id="oldcode" placeholder="Enter OTP Code" maxlength="6">
                            <span class="help-block">Enter Otp code which is sent on registered mobile number</span>
                            <img src="assets/old_assets/images/check.png" alt="logo" id="right" class="wow animated fadeInUp display-hide" data-wow-duration="1s" data-wow-delay="0.4s" style="width: 20px;height: 20px">
                            <img src="assets/old_assets/images/error5.png" alt="logo" id="wrong" class="wow animated fadeInUp display-hide" data-wow-duration="1s" data-wow-delay="0.4s" style="width: 20px;height: 20px">
                            <font style="color:red"><span id="old_code_err"> </span></font>
                        </div>
                    </div>
                    <div class="row display-hide mt-4" id="new-mobile">
                        <div class="col-md-12">
                            <label>New Mobile Number</label>
                            <input type="text" class="form-control" name="mobile" id="newmobile"  placeholder="Enter Mobile Number e.g : +91XXXXXXXXXX">
                            <div class="help-block">Enter Mobile number with country code which you have to update</div>
                            <font style="color:red"><span id="moberr"> </span></font>
                        </div>
                    </div>
                    <div class="row display-hide mt-4"  id="new-code">
                        <div class="col-md-12">
                            <label>Enter New Code</label>
                            <input type="text" class="form-control" name="newcode" id="newcode" maxlength="6">
                            <font style="color:red"><span id="file_cert_err"> </span></font>
                            <font style="color:red"><span id="succ_err"> </span></font>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal" id="mod_close_mobile">Close</button>
                <button type="button" class="btn btn-success updatemobile" >Submit</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- File Upload Modal End -->
<div class="modal fade" id="info" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">INFORMATION</h4>
            </div>
            <div class="modal-body">                                                                       
                <p>Reporting/Nodal/Forwarding Officer should be of same Ministry/Department and should be having an email address like @nic.in, @gov.in, @meity.gov.in etc.</p>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="include/new_include/footer.jsp" />

<script src="main_js/onlineforms.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->
<script type="text/javascript">
    if ('<%= session.getAttribute("uservalues")%>' !== '') {
        $("#user_state").val('<s:property value="#session[\'uservalues\'].userOfficialData.postingState" />');
        setTimeout(function () {
            var city = '<s:property value="#session[\'uservalues\'].userOfficialData.postingCity" />';
            $("#user_city").val(city.replace('&amp;', '&'));
        }, 500);
        $("#user_employment").val('<s:property value="#session[\'uservalues\'].userOfficialData.employment" />');
        if ('<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === 'Central' || '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === 'UT') {
            $('#central_div').show();
            $('#state_div').hide();
            $('#other_div').hide();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />'
            }, function (response) {
                var select = $('#min');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
            });
            setTimeout(function () {
                var a = '<s:property value="#session[\'uservalues\'].userOfficialData.ministry" />';
                $("#min").val(a.replace('&amp;', '&'));
            }, 500);
            setTimeout(function () {
                $.get('centralDepartment', {
                    depType: '<s:property value="#session[\'uservalues\'].userOfficialData.ministry" />'
                }, function (response) {
                    var select = $('#dept');
                    select.find('option').remove();
                    $('<option>').val("").text("-SELECT-").appendTo(select);
                    $.each(response, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    if (response.toString().toLowerCase().indexOf("other") > -1) {
                    } else {
                        $('<option>').val("other").text("other").appendTo(select);
                    }
                });
            }, 500);
            setTimeout(function () {
                var a = '<s:property value="#session[\'uservalues\'].userOfficialData.department" />';

                $("#dept").val(a.replace('&amp;', '&'));
            }, 1000);
            setTimeout(function () {

                if ('<s:property value="#session[\'uservalues\'].userOfficialData.department"/>'.toString().toLowerCase() === 'other') {
                    $('#other_text_div').removeClass("display-hide");

                }
            }, 1000);
        } else if ('<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === 'State') {
            $('#central_div').hide();
            $('#state_div').show();
            $('#other_div').hide();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />'
            }, function (response) {
                var select = $('#stateCode');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
            });
            setTimeout(function () {
                $("#stateCode").val('<s:property value="#session[\'uservalues\'].userOfficialData.state" />');
            }, 500);
            setTimeout(function () {
                $.get('centralDepartment', {
                    depType: '<s:property value="#session[\'uservalues\'].userOfficialData.state" />'
                }, function (response) {
                    var select = $('#Smi');
                    select.find('option').remove();
                    $('<option>').val("").text("-SELECT-").appendTo(select);
                    $.each(response, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    if (response.toString().toLowerCase().indexOf("other") > -1) {
                    } else {
                        $('<option>').val("other").text("other").appendTo(select);
                    }
                });
            }, 500);
            setTimeout(function () {
                var a = '<s:property value="#session[\'uservalues\'].userOfficialData.department" />';
                $("#Smi").val(a.replace('&amp;', '&'));
            }, 1000);
            setTimeout(function () {
                if ('<s:property value="#session[\'uservalues\'].userOfficialData.department"/>'.toString().toLowerCase() === 'other') {

                    $('#other_text_div').removeClass("display-hide");


                }
            }, 1000);
        } else if ('<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === "Others" || '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === "Psu" || '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === "Const" || '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === "Nkn" || '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />' === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: '<s:property value="#session[\'uservalues\'].userOfficialData.employment" />'
            }, function (response) {
                var select = $('#Org');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    $('<option>').val(value).text(value).appendTo(select);
                });
                if (response.toString().toLowerCase().indexOf("other") > -1) {
                } else {
                    $('<option>').val("other").text("other").appendTo(select);
                }
            });
            setTimeout(function () {
                var a = '<s:property value="#session[\'uservalues\'].userOfficialData.organizationCategory" />';

                $("#Org").val(a.replace('&amp;', '&'));
            }, 500);

            setTimeout(function () {
                if ('<s:property value="#session[\'uservalues\'].userOfficialData.organizationCategory"/>'.toString().toLowerCase() === 'other') {
                    $('#other_text_div').removeClass("display-hide");
                }
            }, 1000);
        }
        var user_state = $("#user_state").val();
        $.get('getDistrict', {
            user_state: user_state
        }, function (response) {
            var select = $('#user_city');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {
                if ('<s:property value="#session[\'uservalues\'].userOfficialData.postingCity" />' === value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else {
                    $('<option>').val(value).text(value).appendTo(select);
                }
            });
        });
        var under_sec_email = '<s:property value="#session[\'uservalues\'].under_sec_email" />';
        if (under_sec_email != "")
        {
            $('#under_sec_details').removeClass("display-hide");
        }
    }
</script>
<script>
    $(document).ready(function (e) {
        var under_sec_email = '<s:property value="#session['uservalues'].userOfficialData.under_sec_email" />';
        if (under_sec_email === null || under_sec_email === "") {
        } else {
            $('#under_sec_name').prop('readonly', true);
            $('#under_sec_mobile').prop('readonly', true);
            $('#under_sec_tel').prop('readonly', true);
            $('#under_sec_desig').prop('readonly', true);
        }
        var desig = '<s:property value="#session['uservalues'].userOfficialData.designation" />';
        if ('<%=otp_type%>' !== null) {
            var otp_type = '<%=otp_type%>';
            $('#otp_type').val('<%=otp_type%>');
            $('#emailvalidate').val('<%=userdata.isIsEmailValidated()%>');
            $('#isIsNicEmployee').val('<%=userdata.isIsNICEmployee()%>');
            $('#isIsNewUser').val('<%=userdata.isIsNewUser()%>');
            if (otp_type === 'emailOnly') {
                $('#user_email').prop("readonly", 'true');
            } else if (otp_type === 'mobileOnly') {
                $('#user_mobile').prop("readonly", 'true');
            } else {
                $('#user_email').prop('readonly', 'true');
                $('#user_mobile').prop('readonly', 'true');
            }
        }
  
        if ('<%=nic_employee%>' == 'true') {
            $('#ca_detail').addClass("display-hide");
            var hod_email = '<s:property value="#session['uservalues'].hodData.email" />';
            if (hod_email === null || hod_email === "") {
            } else if('<%=nicemployeeeditable%>' == "yes"){
                
                
            }else  {
                $('#hod_email').prop('disabled', true);

            }
        }
        if ('<%=ldap_employee%>' == 'true') {
            var hod_email = '<s:property value="#session['uservalues'].hodData.email" />';
            if (hod_email !== null && hod_email !== "") {
                $('#ca_detail').addClass("display-hide");
                $('under_sec_details').addClass("display-hide");
                $('#hod_name').prop('readonly', true);
                $('#hod_mobile').prop('readonly', true);
                $('#hod_tel').prop('readonly', true);
                $('#ca_design').prop('readonly', true);
            }
        } else {
            if ('<%=hodDetails.get("bo")%>' !== "no bo")
            {
                $('#hod_name').prop('readonly', true);
                $('#hod_mobile').prop('readonly', true);
                $('#hod_tel').prop('readonly', true);
                $('#ca_design').prop('readonly', true);
            } else {
                $('#hod_name').prop('readonly', false);
                $('#hod_mobile').prop('readonly', false);
                $('#hod_tel').prop('readonly', false);
                $('#ca_design').prop('readonly', false);
                $('#ca_detail').removeClass("display-hide");
                if (under_sec_email !== "") {
                    $('#under_sec_details').removeClass("display-hide");
                }
            }
        }
    });
</script>
</body>
</html>
