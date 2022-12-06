<%@page import="java.util.ArrayList"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="com.org.dao.EmailDao"%>
<%@page import="entities.LdapQuery"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
%>
<%
    Map a = (HashMap) session.getAttribute("profile-values");
    System.out.println("a  in jsp::::::::::" + a);
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    UserData userdata = (UserData) session.getAttribute("uservalues");
    boolean nic_employee = userdata.isIsNICEmployee();
    boolean ldap_employee = userdata.isIsEmailValidated();
    String employment = userdata.getUserOfficialData().getEmployment();

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
                <a href="" class="k-content__head-breadcrumb-link">Dashboard</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link">Email Services for Government of India</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="portlet light " id="form_wizard_1" style="display:block;">
                <div class="portlet-title">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">Email Subscription Forms</h3>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content mt-5">  
                                <div class="tab-pane" id="tab2" >
                                    <div class="col-md-12" id="email_head2">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="single_form"  value="single_form" checked=""> Single Subscription
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="bulk_form"  value="bulk_form" > Bulk Subscription
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="single_form"  value="nkn_single_form"> NKN Single Subscription
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="bulk_form"  value="nkn_bulk_form" > NKN Bulk Subscription
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="gem_form"  value="gem_form" > GEM Subscription
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="email_act"  value="email_activate" checked=""> Email Activate
                                            <span></span>
                                        </label>&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="form_name" id="email_deact"  value="email_deactivate" > Email De-Activate
                                            <span></span>
                                        </label>
                                        <font style="color:red"><span id="req_for_err"></span></font>
                                    </div>

                                    <div id="email_act_deact" class="mt-4 mb-2 d-none">
                                        <form action="" method="post" id="email_act_deact_form1" class="form_val" >
                                            <div class="col-md-12 mt-3 mb-3">
                                                <div class="form-group mt-3" id="email_deactivate">
                                                    <div class="col-md-4 p-0 d-none" id="email_activate_div">
                                                        <div style="margin-top: 25px;">
                                                            <h4 class="theme-heading-h3">Email Activation</h4>
                                                        </div>
                                                        <label for="">Enter Email ID</label>
                                                        <input type="text" name="single_email1" id="single_email1" class="form-control" placeholder="Enter Email ID (Ex: abc.xyz@nic.in)" />
                                                        <font style="color:red"><span id="single_email1_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6 p-0 d-none" id="email_deactivate_div">
                                                        <div style="margin-top: 25px;">
                                                            <h4 class="theme-heading-h3">Email De-Activation</h4>
                                                        </div>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox"> <b>Are you sure? Do you want to deactivate this Email account (<text id="deactivate_email" style="color:red;">abc.xyz@nic.in</text>)</b>
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row mt-5" >
                                                    <div class="col-md-6" style="text-align: right;margin-top: 19px;">
                                                        <label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha1" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
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
                                            <div class="col-md-12">
                                                <div class="row" ><div class="col-md-6" style="text-align: right;margin-top: 19px;"><font style="color:red"><span id="email_info_error"></span></font></div></div>
                                            </div>
                                            <div class="col-md-12 text-center mt-4">
                                                <button name="submit" value="preview" id="submit_single" class="btn btn-success btn-outline sbold"> Preview and Submit </button>                                                                
                                            </div>
                                        </form>
                                    </div>

                                    <div  id="single_user_form">
                                        <form action="" method="post" id="single_form1" class="form_val" >
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <h4 class="theme-heading-h3">Single User Subscription Details</h4>
                                            </div>
                                            <div class="col-md-12">
                                                <label for="street">Type of Mail ID: <span style="color: red">*</span> <a href="" id="mD2" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know More<i class="entypo-help"></i>)</a></label>
                                                <div class="mt-3 mb-3">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_for" id="mail"  value="mail" checked=""> Mail user (with mailbox)
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_for" id="app"  value="app" > Application user (without mail box(Eoffice-auth)) <!-- line modified by pr on 5thmay2020 -->
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" name="req_for" id="eoffice"  value="eoffice" > e-office-srilanka
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                </div>
                                                <font style="color:red"><span id="req_for_err"></span></font>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Date Of Birth <span style="color: red">*</span></label>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dob" id="single_dob" placeholder="Enter Date Of Birth [DD-MM-YYYY]" readonly />
                                                        <font style="color:red"><span id="single_dob_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Date Of Retirement <span style="color: red">*</span></label>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly/>
                                                        <font style="color:red"><span id="single_dor_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group" >
                                                    <div class="col-md-6">
                                                        <label for="street">Email address preference: <span style="color: red">*</span></label>
                                                        <div id="id_based">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="single_id_type" id="single_id_type_1"  value="id_name" checked=""> Name Based
                                                                <span></span>
                                                            </label>&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="single_id_type" id="single_id_type_2"  value="id_desig" > Designation/Office based id
                                                                <span></span>
                                                            </label>&emsp;&emsp;
                                                        </div>
                                                        <font style="color:red"><span id="single_id_type_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Employee Description: <span style="color: red">*</span></label>
                                                        <div id="emp_type">
                                                            <div class="row">
                                                                <div class="col-md-6">
                                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                                        <input type="radio" name="single_emp_type" id="single_emp_type1"  value="emp_regular" checked=""> Govt/Psu Official
                                                                        <span></span>
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                                        <input type="radio" name="single_emp_type" id="single_emp_type2"  value="consultant" > Consultant/Contractual Staff
                                                                        <span></span>
                                                                    </label>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                                        <input type="radio" name="single_emp_type" id="single_emp_type3"  value="emp_contract" > FMS Support Staffs

                                                                        <span></span>
                                                                    </label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <font style="color:red"><span id="single_emp_type_err"></span></font>
                                                    </div>
                                                </div>       
                                            </div>                                                                     
                                            <div class="col-md-12">
                                                <div class="row emailcheck form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]" type="text" name="single_email1" id="single_email1" value="">
                                                        <font style="color:red"><span id="single_email1_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]" type="text" name="single_email2" id="single_email2" value="">
                                                        <font style="color:red"><span id="single_email2_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row mt-5" >
                                                    <div class="col-md-6" style="text-align: right;margin-top: 19px;">
                                                        <label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha1" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
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
                                            <div class="col-md-12">
                                                <div class="row" ><div class="col-md-6" style="text-align: right;margin-top: 19px;"><font style="color:red"><span id="email_info_error"></span></font></div></div>
                                            </div>

                                            <div class="col-md-12 text-center mt-4">
                                                <button name="submit" value="preview" id="submit_single" class="btn btn-success btn-outline sbold"> Preview and Submit </button>                                                                
                                            </div>
                                        </form>
                                    </div>
                                    <div class="display-hide" id="bulk_user_form">
                                        <div id="bulk-1">
                                            <form action="" method="post" id="bulk_form1" class="form_val" >
                                                <div class="col-md-12" style="margin-top: 25px;">
                                                    <h4 class="theme-heading-h3">Bulk User Subscription Details</h4>
                                                </div>
                                                <div class="col-md-12 mt-3 mb-3">
                                                    <label for="street">Type of Mail ID: <span style="color: red">*</span> <a href="" id="mD2" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know More<i class="entypo-help"></i>)</a></label>
                                                    <div>
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_for" id="mail"  value="mail" checked=""> Mail user (with mailbox)
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_for" id="app"  value="app" > Application user (without mail box(Eoffice-auth)) <!-- line modified by pr on 5thmay2020 -->
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="req_for" id="eoffice"  value="eoffice" > e-office-srilanka
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                    </div>
                                                    <font style="color:red"><span id="req_for_err"></span></font>
                                                </div>
                                                <div class="col-md-12 mt-3 mb-3">
                                                    <label for="street">Email address preference: <span style="color: red">*</span></label>
                                                    <div>
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="single_id_type" id="bulk_id_type_1"  value="id_name" checked=""> Name Based
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" name="single_id_type" id="bulk_id_type_2"  value="id_desig" > Designation/Office based id
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                    </div>
                                                    <font style="color:red"><span id="bulk_id_type_err"></span></font>
                                                </div>
                                                <div class="col-md-12 mt-5">
                                                    <div class="alert alert-secondary">   
                                                        <div class="col-md-12">
                                                            <p><strong>(<a href="download_3" target="_blank" style="color: red"><i class="fa fa-download"></i> Click here to download Sample CSV-Format </a>) & the format of input file should be:</strong><br/></p>
                                                            <p>First Name:Last Name:Designation:Department/ Ministry:State:Country Code without(+):Mobile:Date of Retirement(dd-mm-yyyy):Login UID:Complete Email address:Date of Birth(dd-mm-yyyy):Employee Code<br/></p>
                                                            <p>NOTE: All Fields are mandatory (except Date of Birth and Employee code) for account creation</p>
                                                            <p>NOTE: Maximum number of rows accepted at a time is 3000. Please upload CSV file with maximum 3000 rows only.</p>
                                                            <p>NOTE: We have allowed ID Creation facility for International mobile numbers as well. To handle this, now excel file will have one more column to accommodate country codes for their country.</p>
                                                            <p>Country Code (allowed 1-5 digits) , Mobile Number (allowed 8-14 digits)* For for(91) Country Code Only 10 digit mobile number is allowed.</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-12">
                                                    <label for="street">Employee Description: <span style="color: red">*</span></label>
                                                    <div id="emp_type" class="mt-2 mb-3">
                                                        <div class="row">
                                                            <div class="col-md-6">
                                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                                    <input type="radio" name="single_emp_type" id="single_emp_type1"  value="emp_regular" checked=""> Govt/Psu Official
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                                    <input type="radio" name="single_emp_type" id="single_emp_type2"  value="consultant" > Consultant/Contractual Staff
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                                    <input type="radio" name="single_emp_type" id="single_emp_type3"  value="fms_support" > FMS Support Staffs
                                                                    <span></span>
                                                                </label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <font style="color:red"><span id="single_emp_type_err"></span></font>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="street">Please upload the CSV file </label>
                                                    <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                        <input type="file" class="custom-file-input" id="user_file" name="user_file">
                                                        <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                                        <span class="fileinput-filename"> </span> &nbsp;
                                                        <font style="color:red"><span id="file_err"> </span></font>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6" style="text-align: right;margin-top: 37px;">
                                                        <label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha2" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                        <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                    </div>
                                                    <div class="col-md-2">
                                                        <div class="form-group">
                                                            <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                            <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="captcha1" maxlength="6" value=""> 
                                                            <font style="color:red"><span id="captchaerror"></span></font>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-12 text-center mt-5">
                                                    <input type="hidden" id="cert" value="false" /> 
                                                    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                    <font style="color:red"><span id="email_info_error"></span></font>
                                                    <button name="submit" value="preview" class="btn btn-success btn-outline sbold" > Submit </button>                                                                
                                                </div>
                                            </form>
                                        </div>
                                        <div id="bulk-2" class="col-md-12 display-hide">
                                            <form action="" method="post" id="bulk_form2" class="form_val">
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
                                                                        <button type="button" name="download" id="download" value="valid" class="btn btn-circle btn-success white btn-sm">Download</button>
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
                                                                        <button type="button" name="download" id="download" value="not-valid" class="btn btn-circle btn-success white btn-sm">Download</button>
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
                                                                    <div class="mt-head-desc"> List of errors in CSV file</div>
                                                                    <div class="mt-head-button">
                                                                        <button type="button" name="download" id="download" value="error" class="btn btn-circle btn-success white btn-sm">Download</button>
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
                                    <div class="display-hide mt-3 pt-3" id="nkn_single_form">
                                        <form action="" method="post" id="nkn_single_form1" class="form_val">
                                            <div class="col-md-12">
                                                <h4 class="theme-heading-h3">NKN User Subscription Details</h4>
                                                <div class="row form-group">
                                                    <div class="col-md-4">
                                                        <label for="street">Institute Name <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Institute Name [Only characters,whitespace,comma(,) allowed]" type="text" name="inst_name" id="inst_name" value="" maxlength="50">
                                                        <font style="color:red"><span id="inst_name_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Institute ID </label>
                                                        <input class="form-control" placeholder="Enter Institute ID [Alphanumeric,dot(.),comma(,),hypen(-) allowed]" type="text" name="inst_id" id="inst_id" value="" maxlength="50">
                                                        <font style="color:red"><span id="inst_id_err"></span></font>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <label for="street">Name of Project NKN <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Name of Project NKN [Only characters,whitespace,comma(,)allowed]" type="text" name="nkn_project" id="nkn_project" value="" maxlength="50">
                                                        <font style="color:red"><span id="nkn_project_err"></span></font>
                                                    </div> 
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Date Of Birth <span style="color: red">*</span></label>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dob" id="single_dob4" placeholder="Enter Date Of Birth [DD-MM-YYYY]" readonly />
                                                        <font style="color:red"><span id="single_dob_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Date Of Retirement <span style="color: red">*</span></label>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor4" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                                                        <font style="color:red"><span id="single_dor_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row emailcheck form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@nkn.in]" type="text" name="single_email1" id="single_email1" value="">
                                                        <font style="color:red"><span id="single_email1_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@nkn.in]" type="text" name="single_email2" id="single_email2" value="">
                                                        <font style="color:red"><span id="single_email2_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-6 mt-4" style="text-align: right;">
                                                    <label for="street">Captcha</label>
                                                    <img name="Captcha" id="captcha1" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
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
                                            <font style="color:red"><span id="email_info_error"></span></font>
                                            <div class="col-md-12 mt-5 text-center">
                                                <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                                <button name="submit" value="nkn_single"  id="submit_nkn" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                            </div>
                                        </form>
                                    </div>
                                    <div class="display-hide" id="nkn_bulk_form">
                                        <div id="nknbulk-1">
                                            <form action="" method="post" id="nkn_bulk_form1" class="form_val">
                                                <div class="col-md-12" style="margin-top: 25px;">
                                                    <h4 class="theme-heading-h3">NKN Bulk User Subscription Details</h4>
                                                </div> 
                                                <div class="col-md-12">
                                                    <div class="row">
                                                        <div class="col-md-4">
                                                            <label for="street">Institute Name <span style="color: red">*</span></label>
                                                            <input class="form-control" placeholder="Enter Institute Name [Only characters,whitespace,comma(,),dot(.) allowed]" type="text" name="inst_name" id="inst_name" value="" maxlength="50"> 
                                                            <font style="color:red"><span id="inst_name_err"></span></font>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <label for="street">Institute ID </label>
                                                            <input class="form-control" placeholder="Enter Institute ID [Alphanumeric,dot(.),comma(,) allowed]" type="text" name="inst_id" id="inst_id" value="" maxlength="50">
                                                            <font style="color:red"><span id="inst_id_err"></span></font>
                                                        </div>
                                                        <div class="col-md-4">
                                                            <label for="street">Name of Project NKN <span style="color: red">*</span></label>
                                                            <input class="form-control" placeholder="Enter Name of Project NKN [Only characters,whitespace,comma(,),dot(.) allowed]" type="text" name="nkn_project" id="nkn_project" value="" maxlength="50">
                                                            <font style="color:red"><span id="nkn_project_err"></span></font>
                                                        </div>  
                                                    </div>
                                                </div>
                                                <div class="col-md-12 mt-4 mb-4">
                                                    <div class="alert alert-secondary">
                                                        <div class="col-md-12">
                                                            <p><strong>(<a href="download_3" target="_blank" style="color: red"><i class="fa fa-download"></i> Click here to download Sample CSV-Format </a>) & the format of input file should be:</strong></p>
                                                            <p>First Name:Last Name:Designation:Department/ Ministry:State:Country Code without(+):Mobile:Date of Retirement(dd-mm-yyyy):Login UID:Complete Email address:Date of Birth(dd-mm-yyyy):Employee Code</p>
                                                            <p>NOTE: All Field are mandatory (Except Date of Birth and Employee code) for account creation</p>
                                                            <p>NOTE: Maximum number of rows accepted at a time is 3000. Please upload CSV file with maximum 3000 rows only.</p>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="street">Please upload the CSV file </label>

                                                    <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                        <input type="file" class="custom-file-input" id="user_file1" name="user_file1">
                                                        <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                                        <span class="fileinput-filename"> </span> &nbsp;
                                                        <font style="color:red"><span id="file_err1"> </span></font>
                                                    </div>
                                                </div> 
                                                <div class="row">
                                                    <div class="col-md-6" style="text-align: right;">
                                                        <br/><label for="street">Captcha</label>
                                                        <img name="Captcha" id="captcha2" src="Captcha?var=<%=random%>"  width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                        <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                    </div>
                                                    <div class="col-md-2">
                                                        <div class="form-group">
                                                            <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                            <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt1" maxlength="6" value=""> 
                                                            <font style="color:red"><span id="captchaerror"></span></font>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-12 text-center mt-5">
                                                    <font style="color:red"><span id="email_info_error"></span></font>
                                                    <input type="hidden" id="cert1" value="false" />
                                                    <button name="submit" value="nkn_bulk" class="btn purple btn-success sbold" > Submit </button>                                                                
                                                </div>
                                            </form>
                                        </div>
                                        <div id="nknbulk-2" class="col-md-12 display-hide">
                                            <form action="" method="post" id="nkn_bulk_form2" class="form_val" >
                                                <div class="row" >
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
                                                                            <button type="button" name="download" id="download" value="valid" class="btn btn-circle btn-success white btn-sm">Download</button>
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
                                                                            <button type="button" name="download" id="download" value="not-valid" class="btn btn-circle btn-success white btn-sm">Download</button>
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
                                                                        <div class="mt-head-desc"> List of errors in CSV file</div>
                                                                        <div class="mt-head-button">
                                                                            <button type="button" name="download" id="download" value="error" class="btn btn-circle btn-success white btn-sm">Download</button>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div> 
                                                <div class="row" >
                                                    <div class="col-md-12 ">  
                                                        <font style="color:red; text-align: center"><span id="error_file1"></span></font>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12 text-center mt-5">
                                                        <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                                        <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                                        <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                                        <button name="submit" value="preview" class="btn btn-success purple btn-outline sbold" > Preview and Submit </button>                                                                
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                    <div class="display-hide" id="gem_user_form">
                                        <form action="" method="post" id="gem_form1" class="form_val" >
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <h4 class="theme-heading-h3">GEM User Subscription Details</h4>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label for="street">Organization Category <span style="color: red">*</span></label>
                                                        <div class="mt-2 mb-4" id="pse_service">
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="pse" id="central_pse"  value="central_pse" checked=""> Central PSE (Controlled by Central Ministry)
                                                                <span></span>
                                                            </label>&emsp;&emsp;
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" name="pse" id="state_pse"  value="state_pse" > State PSE (Controlled by State Ministry)
                                                                <span></span>
                                                            </label>
                                                        </div>
                                                        <font style="color:red"><span id="pse_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6" id="central_pse_div">
                                                        <label for='street'>Controlling Ministry <span style='color: red'>*</span></label>
                                                        <div class="form-group">
                                                            <select class='form-control' name='pse_ministry' id='pse_ministry'>
                                                                <option value=''>-SELECT-</option>
                                                            </select>
                                                        </div>
                                                        <font style='color:red'><span id='central_pse_err'></span></font>                                                                                        
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="row display-hide" id="state_pse_div">
                                                            <div class="col-md-6">
                                                                <label>State where you are posted <span style="color: red">*</span></label>
                                                                <%
                                                                    ImportantData impdata = (ImportantData) userdata.getImpData();
                                                                    ArrayList state_name = (ArrayList) impdata.getStates();
                                                                    String statename = "";
                                                                %>
                                                                <div class="form-group">
                                                                    <select id="pse_state" name="pse_state" theme="simple" class="form-control">
                                                                        <option value="" selected>select</option>
                                                                        <%            for (int i = 0; i < state_name.size(); i++) {
                                                                                statename = state_name.get(i).toString();
                                                                        %>
                                                                        <option value="<%=statename%>"><%=statename%></option>
                                                                        <%}
                                                                        %>
                                                                    </select>
                                                                </div>

                                                                <font style='color:red'><span id='pse_state_err'></span></font>                                                                                        
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label for="street">District Name (Where applicant is posted) <span style="color: red">*</span></label>
                                                                <select class='form-control' name='pse_district' id='pse_district'>
                                                                    <option value=''>-SELECT-</option>
                                                                </select>   
                                                                <font style="color:red"><span id="pse_district_err"></span></font>
                                                            </div>  
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="col-md-12">
                                                <h3 class="theme-heading-h3-popup">Forwarding Officer Details</h3>

                                                <label for="street"><strong>Your application needs to be forwarded by an officer at the level of Under Secretary or above and having government email address. For example <strong> @nic.in/@gov.in. </strong>
                                                        Once approved by the Forwarding Officer, your request will be forwarded to gemapplicant@gem.gov.in. 
                                                        Please contact GEM support (gemapplicant@gem.gov.in) for any queries.</strong></label>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group">
                                                    <%
                                                        String hod_email = "";
                                                        String hod_name = "";
                                                        String hod_mobile = "";
                                                        String hod_desig = "";
                                                        String hod_tel = "";
                                                        // Map profile_values = (HashMap) session.getAttribute("profile-values");
                                                        if (userdata.getHodData().getEmail() != null) {
                                                            hod_email = userdata.getHodData().getEmail().toString();
                                                        }
                                                        if (userdata.getHodData().getName() != null) {
                                                            hod_name = userdata.getHodData().getName().toString();
                                                        }
                                                        if (userdata.getHodData().getMobile() != null) {
                                                            hod_mobile = userdata.getHodData().getMobile().toString();
                                                        }
                                                        if (userdata.getHodData().getDesignation() != null && userdata.getHodData().getDesignation().toString() != "null" && userdata.getHodData().getDesignation().toString() != "") {
                                                            hod_desig = userdata.getHodData().getDesignation().toString();
                                                        }
                                                        if (userdata.getHodData().getTelephone() != null) {
                                                            hod_tel = userdata.getHodData().getTelephone().toString();
                                                        }
                                                        String user_exists = LdapQuery.GetMobile(hod_email);
                                                        System.out.println("user_exist::::" + user_exists);
                                                        if (!user_exists.equals("error")) {
                                                    %>
                                                    <div class="col-md-6">
                                                        <label for="street">Email <span style="color: red">*</span></label>
                                                        <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email" value="<s:property value="#session['profile-values'].hod_email" />"  maxlength="50"  >
                                                        <font style="color:red"><span id="fwd_email_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Name  <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="fwd_ofc_name" id="fwd_ofc_name" value="<s:property value="#session['profile-values'].hod_name" />" <%if (!hod_name.equals("")) { %> readonly="" <% }%> maxlength="50">
                                                        <font style="color:red"><span id="fwd_name_err"></span></font>
                                                    </div>
                                                    <span id="nicemp"></span>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row form-group">
                                                    <div class="col-md-3">
                                                        <label for="street"> Mobile <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="fwd_ofc_mobile" id="fwd_ofc_mobile" value="<s:property value="#session['profile-values'].hod_mobile" />"  <%if (!hod_mobile.equals("")) { %> readonly="" <% }%>maxlength="15">
                                                        <font style="color:red"><span id="fwd_mobile_err"></span></font>
                                                    </div>
                                                    <div class="col-md-3">
                                                        <label for="street">Telephone <span style="color: red">*</span></label>
                                                        <input class="form-control" style="text-transform:lowercase;" type="text" id="fwd_ofc_tel" name="fwd_ofc_tel" placeholder="Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" value="<s:property value="#session['profile-values'].hod_tel" />" <%if (!hod_tel.equals("")) { %> readonly="" <% }%> maxlength="15">
                                                        <font style="color:red"><span id="fwd_tel_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6"><label for="street" class="mt-4"> If you want to update your mobile number please click on <a href="https://quicksms.emailgov.in/mobile/#/login" target="_blank">https://quicksms.emailgov.in/mobile/#/login</a></label></div>
                                                    <span id="nicemp"></span>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label>Designation <span style="color: red">*</span></label>
                                                        <input type="text" id="fwd_ofc_desig" name="fwd_ofc_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control" value="<s:property value="#session['profile-values'].ca_design" />" <%if (!hod_desig.equals("") && hod_desig != "null" && hod_desig != null) { %> readonly="" <% }%>  value="" /> 
                                                        <font style="color:red"><span id="fwd_desig_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label>Address <span style="color: red">*</span></label>
                                                        <input type="text" id="fwd_ofc_add" name="fwd_ofc_add" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                                                        <font style="color:red"><span id="fwd_add_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="row mt-3 mt-3">
                                                    <div class="col-md-6 mt-3">
                                                        <label>Are you primary user/HOD on GeM portal    <span style="color: red">*</span></label>
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" id="primary_user1" name="primary_user"  value="yes" /> Yes
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" id="primary_user2" name="primary_user"  value="no" /> No
                                                            <span></span>
                                                        </label>
                                                        <font style="color:red"><span id="primary_user_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6 display-hide" id="primary_text_id">
                                                        <label>Enter User Id <span style="color: red">*</span></label>
                                                        <input type="text" id="primary_user_id" name="primary_user_id" placeholder="Enter User Id [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                                                        <font style="color:red"><span id="primary_id_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <% } else {
                                                System.out.println("inside else::::" + user_exists);
                                            %>
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label for="street">Email <span style="color: red">*</span></label>
                                                    <input class="form-control" style="text-transform:lowercase;" placeholder="Enter E-Mail Address [e.g: abc.xyz@zxc.com]" type="text" name="fwd_ofc_email" id="fwd_ofc_email" value="" maxlength="50">
                                                    <font style="color:red"><span id="fwd_email_err"></span></font>
                                                </div>
                                                <%  System.out.println(" inside email "); %>
                                                <div class="col-md-6">
                                                    <label for="street">Name  <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="Enter Name [characters,dot(.) and whitespace]" type="text" name="fwd_ofc_name" id="fwd_ofc_name" value="" maxlength="50">
                                                    <font style="color:red"><span id="fwd_name_err"></span></font>
                                                </div>
                                                <span id="nicemp"></span>
                                            </div>
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label for="street"> Mobile <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Mobile Number [e.g: +919999999999]" type="text" name="fwd_ofc_mobile" id="fwd_ofc_mobile" value="" maxlength="15">
                                                        <font style="color:red"><span id="fwd_mobile_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Telephone <span style="color: red">*</span></label>
                                                        <input class="form-control" style="text-transform:lowercase;" type="text" id="fwd_ofc_tel" name="fwd_ofc_tel" placeholder="Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]" maxlength="15">
                                                        <font style="color:red"><span id="fwd_tel_err"></span></font>
                                                    </div>
                                                    <span id="nicemp"></span>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label>Designation <span style="color: red">*</span></label>
                                                        <input type="text" id="ca_design" name="fwd_ofc_design" placeholder="Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespace and [. , - &]]" maxlength="50" class="form-control" value="" /> 
                                                        <font style="color:red"><span id="fwd_desig_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label>Address <span style="color: red">*</span></label>
                                                        <input type="text" id="ca_design" name="fwd_ofc_add" placeholder="Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                                                        <font style="color:red"><span id="fwd_add_err"></span></font>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label>Are you primary user/HOD on GeM portal    <span style="color: red">*</span></label>
                                                        <input type="radio" id="primary_user_yes" name="primary_user"  value="yes" /> Yes
                                                        <input type="radio" id="primary_user_no" name="primary_user"  value="no" /> No
                                                        <font style="color:red"><span id="primary_user_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6 display-hide" id="primary_text_id">
                                                        <label>Enter User Id <span style="color: red">*</span></label>
                                                        <input type="text" id="fwd_ofc_add" name="primary_user_id" placeholder="Enter User Id [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]" maxlength="50" class="form-control" value="" /> 
                                                        <font style="color:red"><span id="primary_id_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <% }%>

                                            <div class="col-md-12">
                                                <h3 class="theme-heading-h3-popup">Personal Details</h3>
                                                <div class="row form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Date Of Retirement <span style="color: red">*</span></label>
                                                        <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor2" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly/>
                                                        <font style="color:red"><span id="gem_dor_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Role to be assign <span style="color: red">*</span></label>
                                                        <select name="role_assign" class="form-control" id="role_assign">
                                                            <option value="">--Select--</option>
                                                            <option value="HOD">HOD</option>
                                                            <option value="Buyer">Buyer</option>
                                                            <option value="Consignee">Consignee</option>
                                                            <option value="BuyerCon">Buyer & consignee</option>
                                                            <option value="DDO">DDO</option>
                                                        </select>
                                                        <font style="color:red"><span id="role_assign_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row emailcheck form-group">
                                                    <div class="col-md-6">
                                                        <label for="street">Preferred Email Address 1 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@gembuyer.in" type="text" name="single_email1" id="single_email1" value="">
                                                        <font style="color:red"><span id="gem_email1_err"></span></font>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <label for="street">Preferred Email Address 2 (Refer <a href='https://msgapp.emailgov.in/docs/assets/download/E-mail_policy_of_Government_of_India.pdf' target='1' style='color:red'>email address guidelines</a> ) <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@gembuyer.in" type="text" name="single_email2" id="single_email2" value="">
                                                        <font style="color:red"><span id="gem_email2_err"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <label for="street">Enter Your Projected Monthly Traffic <span style="color: red">*</span></label>
                                                        <input class="form-control" placeholder="Enter Your Projected Monthly Traffic, Numeric Value(Minimum 1000)" type="text" name="domestic_traf" id="domestic_traf" value="" maxlength="9" >
                                                        <font style="color:red"><span id="gem_traffic_err"></span></font>
                                                    </div>                                                                                
                                                </div>
                                            </div>
                                            <div class="row mt-5">
                                                <div class="col-md-6" style="text-align: right;margin-top: 19px;">
                                                    <label for="street">Captcha</label>
                                                    <img name="Captcha" id="captcha3" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                                    <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                </div>
                                                <div class="col-md-2">
                                                    <div class="form-group">
                                                        <label for="street">Enter Captcha<span style="color: red">*</span></label>
                                                        <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt2" maxlength="6" value=""> 
                                                        <font style="color:red"><span id="captchaerror"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="row" ><div class="col-md-6" style="text-align: right;margin-top: 19px;"><font style="color:red"><span id="email_info_error"></span></font></div></div>
                                            </div>

                                            <div class="col-md-12 mt-4 text-center">

                                                <button name="submit"  id="submit_gem" value="preview" class="btn btn-success btn-outline sbold" > Preview and Submit </button>                                                                
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
</div>
<!-- END PAGE CONTENT BODY -->
<!-- END CONTENT BODY -->
<jsp:include page="include/new_include/footer.jsp" />
<div class="modal fade" id="myModal1"  tabindex="-1">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"><b>Type Of Mail ID: </b></h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <ul>
                    <li>Mail user id is with mailbox.</li>
                    <li>Application user id is without mailbox, which can be used for login purpose in eoffice, intranic etc.</li>
                </ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="email_act_tab2" method="post">
        <jsp:include page="include/email_act_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="single_form2" method="post">
        <jsp:include page="include/single_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg" id="large1" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="bulk_form3" method="post">
        <jsp:include page="include/bulk_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg" id="large2" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="gem_form2" method="post">
        <jsp:include page="include/gem_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large3" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="nkn_form2" method="post">
        <jsp:include page="include/nkn_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
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
                <b>Terms and conditions</b>
                <br/>
                <ol type="1">
                    <li>Users are requested to keep the given userid and password a secret.</li>                                            
                    <li>Please change your password at least once in every three months.</li>                                            
                    <li>By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. <b>NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.</b></li>                                            
                    <li>Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.</li>                                            
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>                                            
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>                                            
                    <li>NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: <a href="https://msgapp.emailgov.in/docs/assets/download/POP.pdf" target="1">https://msgapp.emailgov.in/docs/assets/download/POP.pdf</a></li>                                             
                    <li>By default accounts will be given access over WEB only (<a href="https://mail.gov.in" target="1">https://mail.gov.in</a>). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.</li>                                            
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>                                            
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:                                            
                        <br/>Trash - 7 days                                            
                        <br/>ProbablySpam – 7 days</li>                                            
                    <li>NIC account will be deactivated, if not used for 90 days.</li>
                    <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>                                            
                    <li>Contact our 24x7 support if you have any problems. Phone <b><b>1800-111-555</b></b> or you can send mail to <b>support@gov.in</b></li>
                    <li>Please note that advance payment is a must for paid users.</li>                                            
                    <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>                                            
                    <li><b>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</b></li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate  to NIC about  their willingness to retain the id  through NIC coordinator prior to retirement.</li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="bulk-stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>Terms and conditions</b>
                <br/>
                <ol type="1">
                    <li>Users are requested to keep the given userid and password a secret.</li>                                            
                    <li>Please change your password at least once in every three months.</li>                                            
                    <li>By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. <b>NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.</b></li>                                            
                    <li>Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.</li>                                            
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>                                            
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>                                            
                    <li>NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: <a href="https://msgapp.emailgov.in/docs/assets/download/POP.pdf" target="1">https://msgapp.emailgov.in/docs/assets/download/POP.pdf</a></li>                                             
                    <li>By default accounts will be given access over WEB only (<a href="https://mail.gov.in" target="1">https://mail.gov.in</a>). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.</li>                                            
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>                                            
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:                                            
                        <br/>Trash - 7 days                                            
                        <br/>ProbablySpam - 7 days</li>                                            
                    <li>NIC account will be deactivated, if not used for 90 days.</li>
                    <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>                                            
                    <li>Contact our 24x7 support if you have any problems. Phone <b><b>1800-111-555</b></b> or you can send mail to <b>support@gov.in</b></li>
                    <li>Please note that advance payment is a must for paid users.</li>                                            
                    <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>                                            
                    <li><b>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</b></li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate  to NIC about  their willingness to retain the id  through NIC coordinator prior to retirement.</li>
                </ol>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="gem-stack2" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">Terms and conditions</h4>
            </div>
            <div class="modal-body">                           
                <b>Terms and conditions</b>
                <br/>
                <ol type="1">
                    <li>The validity of account will be 12 months from the date of intergration.</li>
                    <li>The applicant agrees to transfer the fund within 7 days from the date of PI generation by NICSI.</li>
                    <li>NIC reserves te right to deactivate the account anytime due to non-payment of dues.</li>
                    <li>Users are requested to keep the given userid and password a secret.</li>                                            
                    <li>Please change your password at least once in every three months.</li>                                            
                    <li>By not doing so (point no. 4 & 5 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. <b>NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.</b></li>                                            
                    <li>Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.</li>                                            
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>                                            
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>                                            
                    <li>NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: <a href="https://msgapp.emailgov.in/docs/assets/download/POP.pdf" target="1">https://msgapp.emailgov.in/docs/assets/download/POP.pdf</a></li>                                             
                    <li>By default accounts will be given access over WEB only (<a href="https://mail.gov.in" target="1">https://mail.gov.in</a>). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.</li>                                            
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>                                            
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:                                            
                        <br/>Trash - 7 days                                            
                        <br/>ProbablySpam – 7 days</li>                                            
                    <li>NIC account will be deactivated, if not used for 90 days.</li>
                    <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>                                            
                    <li>Contact our 24x7 support if you have any problems. Phone <b><b>1800-111-555</b></b> or you can send mail to <b>support@gov.in</b></li>
                    <li>Please note that advance payment is a must for paid users.</li>                                            
                    <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>                                            
                    <li><b>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</b></li>
                    <li>NIC does not capture any aadhaar related information.</li>
                </ol>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="nkn-stack2" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">Terms and conditions</h4>
            </div>
            <div class="modal-body">                           
                <b>Terms and conditions</b>
                <br/>
                <ol type="1">
                    <li>Users are requested to keep the given userid and password a secret.</li>                                            
                    <li>Please change your password at least once in every three months.</li>                                            
                    <li>By not doing so (point no. 1 & 2 above) the account may be compromised by hackers and the hacker can use the same account for sending spurious mails on the accounts behalf. <b>NIC is neither responsible nor accountable for this type of misuse of the compromised mail accounts. Gross misuse might be detected by automated monitoring tools, which in turn will automatically deactivate the account.</b></li>                                            
                    <li>Do not open any attachments unless, it has come from a known source. In fact delete those mails which are not relevant to you and still you have received them. They might contain a virus that will corrupt your computer.</li>                                            
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>                                            
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>                                            
                    <li>NIC e-Mail Service is provided over secure channels only. WEB interface can be accessed over HTTPs(port 443), POP service is over POP3s(port 995),IMAP service is over IMAPs(port 993) and SMTP service is over SMTPs(port 465). Users are required to suitably modify the client software settings to use the services.Please check the FAQ at: <a href="https://msgapp.emailgov.in/docs/assets/download/POP.pdf" target="1">https://msgapp.emailgov.in/docs/assets/download/POP.pdf</a></li>                                             
                    <li>By default accounts will be given access over WEB only (<a href="https://mail.gov.in" target="1">https://mail.gov.in</a>). If user wants to access over POP/IMAP,please generate a request through eForms (https://eforms.nic). For security reasons either POP or IMAP will be allowed. NIC recommends use of IMAP.</li>                                            
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>                                            
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:                                            
                        <br/>Trash - 7 days                                            
                        <br/>ProbablySpam ? 7 days</li>                                            
                    <li>NIC account will be deactivated, if not used for 90 days.</li>
                    <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>                                            
                    <li>Contact our 24x7 support if you have any problems. Phone <b><b>1800-111-555</b></b> or you can send mail to <b>support@gov.in</b></li>
                    <li>Please note that advance payment is a must for paid users.</li>                                            
                    <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>                                            
                    <li><b>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</b></li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Government officers who resign or superannuate after rendering at least 20 years of service shall be allowed to retain the name based e-mail address. However, they need to intimate  to NIC about  their willingness to retain the id  through NIC coordinator prior to retirement.</li>
                </ol>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="single_form_confirm" class="display-hide">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
    <form id="bulk_form_confirm" class="display-hide">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
    <form id="gem_form_confirm" class="display-hide">
        <jsp:include page="include/Fwd_Ofc.jsp" />
    </form>
    <form id="nkn_form_confirm" class="display-hide">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<div id="emailModal" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Please Note:</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
                <ul><b><li>GEM(PSU) users should click GEM subsription forms only to create the IDs. </li></br><li> Central/State government users should click on Single/Bulk subscription form only to create the IDs. </li></br><li> NKN users from any institute should click on NKN subscription form only. </li></b></ul>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" data-dismiss="modal">OK</button>
            </div>
        </div>

    </div>
</div>
<!-- end -->
<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/email.js" type="text/javascript"></script>

<script>
    var employment = '<%=employment%>';
    if (employment == "Nkn")
    {
        $(".nknclass").click(function () {
            bootbox.dialog({
                message: "<p style='text-align: center; font-size: 18px;'>Since you are from NKN, please use NKN Single/Bulk User Subscription to fill up the form for NKN email ids. If you want to use single subscription please go to your profile and change organization details.</p>",
                title: "You are not authorized",
                buttons: {
                    cancel: {
                        label: "OK",
                        className: 'btn-info'
                    }
                }
            });
        });
        $('.grayemail').css('pointer-events', 'none');
        $('.grayemail').css('opacity', '1');
    } else {
        $('.grayemail').css('pointer-events', '');
        $('.grayemail').css('opacity', '1');
    }
</script>
<script type="text/javascript">
    $(window).on('load', function () {
        $('#emailModal').modal('show');
    });
    jQuery(document).ready(function () {
        $(".tab-content .tab-pane").show();
        var email = '<%=userdata.getEmail()%>';
        var emailValidate = '<%=userdata.isIsEmailValidated()%>';
        $("#single_form1 #refresh").on("click", function () {
            $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#bulk_form1 #refresh").on("click", function () {
            $('#bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#gem_form1 #refresh").on("click", function () {
            $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#single_form2 #closebtn").on("click", function () {
            $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#single_form1 #imgtxt').val("");
        });
        $("#bulk_form2 #closebtn").on("click", function () {
            $('#bulk_form1 captcha2').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#bulk_form1 #imgtxt').val("");
        });
        $("#gem_form2 #closebtn").on("click", function () {
            $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#gem_form1 #imgtxt2').val("");
        });
        $("#nkn_single_form1 #refresh").on("click", function () {
            $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#nkn_bulk_form1 #refresh").on("click", function () {
            $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        });
        $("#nkn_form2 #closebtn").on("click", function () {
            $('#nkn_form2 #captcha1').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#nkn_form2 #captcha2').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
            $('#imgtxt').val("");
            $('#imgtxt1').val("");
        });
        if ('<%=nic_employee%>' !== null) {
            if ('<%=nic_employee%>' === 'true') {
                var hod_email = '<s:property value="#session['uservalues'].hodData.email" />';
                if (hod_email === null || hod_email === "") {
                } else {
                    $("input[name='hod_email']").prop('readonly', true);
                }
            }
        }
        if ('<%=session.getAttribute("resend_request")%>' == "true") {
            var form_name = '<s:property value="#session['prvwdetails'].form_name" />';
            if (form_name == "SINGLEUSER")
            {
                var prvw_dob = '<s:property value="#session['prvwdetails'].prvw_dob" />';
                var prvw_dor = '<s:property value="#session['prvwdetails'].prvw_dor" />';
                var prvw_idtype = '<s:property value="#session['prvwdetails'].prvw_idtype" />';
                var prvw_pemail1 = '<s:property value="#session['prvwdetails'].prvw_pemail1" />';
                var pref_email2 = '<s:property value="#session['prvwdetails'].prvw_pemail2" />';
                var prvw_emp_type = '<s:property value="#session['prvwdetails'].prvw_emp_type" />';
                var prvw_type = '<s:property value="#session['prvwdetails'].prvw_type" />';
                $('#single_dob').val(prvw_dob);
                $('#single_dor').val(prvw_dor);
                if (prvw_idtype === 'id_name') {
                    $("#single_id_type_1").prop('checked', true);
                } else {
                    $("#single_id_type_2").prop('checked', true);
                }
                $('#single_email1').val(prvw_pemail1);
                $('#single_email2').val(pref_email2);
                if (prvw_emp_type === 'emp_regular') {
                    $("#single_emp_type1").prop('checked', true);
                } else {
                    $("#single_emp_type2").prop('checked', true);
                }
                if (prvw_type === 'mail') {
                    $("#mail_1").prop('checked', true);
                } else {
                    $("#app_1").prop('checked', true);
                }
            } else if (form_name == "GEM")
            {
                var prvw_pse = '<s:property value="#session['prvwdetails'].prvw_pse" />';
                var prvw_pse_ministry = '<s:property value="#session['prvwdetails'].prvw_pse_ministry" />';
                var prvw_pse_state = '<s:property value="#session['prvwdetails'].prvw_pse_state" />';
                var prvw_pse_district = '<s:property value="#session['prvwdetails'].prvw_pse_district" />';
                var prvw_dob = '<s:property value="#session['prvwdetails'].prvw_dob" />';
                var prvw_dor = '<s:property value="#session['prvwdetails'].prvw_dor" />';
                var prvw_pemail1 = '<s:property value="#session['prvwdetails'].prvw_pemail1" />';
                var prvw_pemail2 = '<s:property value="#session['prvwdetails'].prvw_pemail2" />';
                var prvw_traffic = '<s:property value="#session['prvwdetails'].prvw_traffic" />';
                var primary_user = '<s:property value="#session['prvwdetails'].primary_user" />';
                var primary_user_id = '<s:property value="#session['prvwdetails'].primary_user_id" />';
                var prvw_ofc_name = '<s:property value="#session['prvwdetails'].prvw_ofc_name" />';
                var prvw_ofc_email = '<s:property value="#session['prvwdetails'].prvw_ofc_email" />';
                var prvw_ofc_mobile = '<s:property value="#session['prvwdetails'].prvw_ofc_mobile" />';
                var prvw_ofc_tel = '<s:property value="#session['prvwdetails'].prvw_ofc_tel" />';
                var prvw_ofc_add = '<s:property value="#session['prvwdetails'].prvw_ofc_add" />';
                var prvw_ofc_desig = '<s:property value="#session['prvwdetails'].prvw_ofc_desig" />';
                var role_assign = '<s:property value="#session['prvwdetails'].role_assign" />';
                if (prvw_pse === 'central_pse') {
                    $("#central_pse").prop('checked', true);
                    $('#central_pse_div').removeClass('display-hide');
                    $('#state_pse_div').addClass('display-hide');
                    $.get('centralMinistry', {
                        orgType: 'Central'
                    }, function (response) {
                        var select = $('#pse_ministry');
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);
                        $.each(response, function (index, value) {
                            $('<option>').val(value).text(value).appendTo(select);
                        });
                    });
                    setTimeout(function () {
                        $(" #pse_ministry").val(prvw_pse_ministry);
                    }, 500);
                } else {
                    $("#state_pse").prop('checked', true);
                    $('#state_pse_div').removeClass('display-hide');
                    $('#central_pse_div').addClass('display-hide');
                    $("#pse_state").val(prvw_pse_state);
                    $("#pse_district").val(prvw_pse_district);
                }
                $('#gem_form1 #single_dor2').val(prvw_dor);
                $('#gem_form1 #single_email1').val(prvw_pemail1);
                $('#gem_form1 #single_email2').val(prvw_pemail2);
                $('#gem_form1 #domestic_traf').val(prvw_traffic);
                $('#gem_form1 #fwd_ofc_name').val(prvw_ofc_name);
                $('#gem_form1 #fwd_ofc_email').val(prvw_ofc_email);
                $('#gem_form1 #fwd_ofc_tel').val(prvw_ofc_tel);
                $('#gem_form1 #fwd_ofc_mobile').val(prvw_ofc_mobile);
                $('#gem_form1 #fwd_ofc_design').val(prvw_ofc_desig);
                $('#gem_form1 #fwd_ofc_add').val(prvw_ofc_add);
                $('#gem_form1 #single_dor2').val(prvw_dor);
                $('#gem_form1 #single_email1').val(prvw_pemail1);
                $('#gem_form1 #single_email2').val(prvw_pemail2);
                $('#gem_form1 #domestic_traf').val(prvw_traffic);
                $("input:radio[value='" + primary_user + "']").prop("checked", true);
                if (primary_user == "yes")
                {
                    $('#primary_text_id').removeClass("display-hide");
                    $('#primary_user_id').val(primary_user_id);
                } else {
                    $('#primary_text_id').addClass("display-hide");
                    $('#primary_user_id').val("");
                }
                $('#role_assign').val(role_assign);
            } else if (form_name == "NKN")
            {
                var prvw_request_type = '<s:property value="#session['prvwdetails'].prvw_request_type" />';
                var prvw_inst_name = '<s:property value="#session['prvwdetails'].prvw_inst_name" />';
                var prvw_inst_id = '<s:property value="#session['prvwdetails'].prvw_inst_id" />';
                var prvw_nkn_project = '<s:property value="#session['prvwdetails'].prvw_nkn_project" />';
                var prvw_dob = '<s:property value="#session['prvwdetails'].prvw_dob" />';
                var prvw_dor = '<s:property value="#session['prvwdetails'].prvw_dor" />';
                var prvw_pemail1 = '<s:property value="#session['prvwdetails'].prvw_pemail1" />';
                var prvw_pemail2 = '<s:property value="#session['prvwdetails'].prvw_pemail2" />';
                var request_type = '<s:property value="#session['prvwdetails'].prvw_request_type" />';
                $('#nkn_single_form1 #single_dob4').val(prvw_dob);
                $('#nkn_single_form1 #single_dor4').val(prvw_dor);
                $('#nkn_single_form1 #single_email1').val(prvw_pemail1);
                $('#nkn_single_form1 #single_email2').val(prvw_pemail2);
                if (request_type == "nkn_bulk")
                {
                    $('#nkn_bulk_form1 #inst_name').val(prvw_inst_name);
                    $('#nkn_bulk_form1 #inst_id').val(prvw_inst_id);
                    $('#nkn_bulk_form1 #nkn_project').val(prvw_nkn_project);
                    $('#nkn_single_form1 #inst_name').val("");
                    $('#nkn_single_form1 #inst_id').val("");
                    $('#nkn_single_form1 #nkn_project').val("");
                } else {
                    $('#nkn_single_form1 #inst_name').val(prvw_inst_name);
                    $('#nkn_single_form1 #inst_id').val(prvw_inst_id);
                    $('#nkn_single_form1 #nkn_project').val(prvw_nkn_project);
                    $('#nkn_bulk_form1 #inst_name').val("");
                    $('#nkn_bulk_form1 #inst_id').val("");
                    $('#nkn_bulk_form1 #nkn_project').val("");
                }
            }
        }
    });
</script> 