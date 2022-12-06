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
 
   session.setAttribute("uploaded_filename", null);
   session.setAttribute("hardware_uploaded_filename", null);
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
                <a href="" class="k-content__head-breadcrumb-link">SMTP Gateway Services (Relay)</a>
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile">
            <div class="form-wizard" id="form_wizard_1" style="display:block;">
                <div class="form-body">
                    <div class="tab-content">
                        <div class="tab-pane active" id="tab1" >
                            <form action="" method="post" id="relay_form1" class="form_val" >
                                <div class="k-portlet__head">
                                    <div class="col-md-12">
                                        <div class="row">
                                            <div class="col-md-6 p-0">
                                                <div class="k-portlet__head-label mt-4">
                                                    <h3 class="k-portlet__head-title">Relay Entry Details</h3>
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

                                <div class="col-md-12 mt-3">
                                    <div class="alert alert-danger">
                                        Requests for opening of necessary firewall ports have to be done through farps.nic.in by the concerned NIC coordinator.
                                    </div>
                                    <div class="mb-3">
                                        <label for="street"><strong>Request For:</strong></label>
                                        <div id="dns_service" class="mt-2">
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="req_" id="req_new" value="req_new" checked=""> NEW
                                                <span></span>
                                            </label>&emsp;&emsp;
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="req_" id="req_add" value="req_add"> ADD
                                                <span></span>
                                            </label>&emsp;&emsp;
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="req_" id="req_modify" value="req_modify"> MODIFY
                                                <span></span>
                                            </label>&emsp;&emsp;
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="req_" id="req_delete" value="req_delete"> DELETE
                                                <span></span>
                                            </label>&emsp;&emsp;
                                            <font style="color:red"><span id="dns_domain_err"></span></font>
                                        </div>
                                    </div>
                                    <div class="row form-group">
                                        <div id="oldip" class="col-md-5 d-none">
                                            <label for="street">Old Application IP  <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter the IP Address [e.g.: 164.100.X.X ]" type="text" name="old_relay_ip[]" id="old_relay_ip"  value="" maxlength="15">

                                        </div>
                                        <div id="newip" class="col-md-10">
                                            <label for="street">Application IP  <span style="color: red">*</span></label>
                                            <input class="form-control class_name" placeholder="Enter the IP Address [e.g.: 164.100.X.X ]" type="text" name="relay_ip[]" id="relay_ip"  value="" maxlength="15">

                                        </div>

                                        <div class="col-md-2 mt-4 pl-0 pt-2">

                                            <button name="addip" type="button" class="btn btn-circle btn-info addip"><i class="fa fa-plus-square"></i> ADD</button>
                                        </div>

                                        <div class="col-md-12" id="add_ip"></div>
                                        <div class="col-md-12"><font style="color:red"><span id="relay_ip_err"></span></font></div>
                                        <font style="color:red"><span id="relay_dup_ip_err"></span></font> 
                                    </div>
                                </div>
                                <div class="col-md-12 mt-4 mb-4" id="not-prompt-in-caseofdelete">
                                    <div class="row form-group">
                                        <div class="col-md-6">
                                            <label for="">
                                                Security Audit: Whether mail will be sent through any application or Hardware Device. <b>(If mail is sent through Hardware Device, Security Audit may be exempted.)</b>
                                            </label>
                                            <div class="row form-group">
                                                <div class="col-md-6">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="Hardware" name="security_audit" id="security_audit_1" checked="checked"> Hardware
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="Software" name="security_audit" id="security_audit_2"> Software Application
                                                        <span></span>
                                                    </label>
                                                </div>

                                                <font style="color:red"><span id="audit_error"></span></font>



                                            </div>

                                        </div>
                                        <div class="col-md-6">

                                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                <input type="checkbox" name="ip_staging" id="ip_staging" value="yes"  /> For Staging Server, please check ( IP will be allowed maximum for 15 days )
                                                <span></span>
                                            </label>
                                            <div class="col-sm-offset-1">
                                                <font style="color:red"><span id="ip_staging_err"></span></font>
                                            </div>




                                        </div>



                                    </div>
                                    <div class="col-md-6">
                                        <div class="" id="hardware_cert_div">
                                            <label for="street">exemption certificate/letter in <b>PDF</b> format (less than 1mb)<span style="color: red">*</span></label>

                                            <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                <input type="file" class="custom-file-input" id="hardware_cert_file" name="hardware_cert_file" >
                                                <label class="custom-file-label text-left" for="hardware_cert_file">Select File</label>
                                                <span class="fileinput-filename"> </span> &nbsp;
                                                <font style="color:red"><span id="hardware_file_err"> </span></font>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="col-md-12">
                                        <div class="row form-group">
                                            <div class="col-md-6">
                                                <input class="form-control security_exp_date d-none" type="text" name="security_exp_date" id="security_exp_date" placeholder="Security Audit Expiry Date (To check validity)*" readonly="">
                                                <font style="color:red"><span id="audit_date_err"></span></font>


                                                <div id="cert_div" class="d-none">
                                                    <label for="street">Application should have Security audit clearance certificate, Upload certificate in <b>PDF</b> format (less than 1mb)<span style="color: red">*</span></label>

                                                    <div class="custom-file fileinput fileinput-new" data-provides="fileinput">
                                                        <input type="file" class="custom-file-input" id="cert_file" name="cert_file" >
                                                        <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                                        <span class="fileinput-filename"> </span> &nbsp;
                                                        <font style="color:red"><span id="file_err"> </span></font>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>



                                </div>





                                <div class="col-md-12">
                                    <div class="row form-group">
                                        <div class="col-md-4">
                                            <label for="street">Application Name <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Application Name, [characters limit[50] only,dot(,),comma(,) whitespace allowed] " type="text" name="relay_app_name" id="relay_app_name"  value="" maxlength="50" />
                                            <font style="color:red"><span id="app_name_err"></span></font>
                                        </div>
                                        <div class="col-md-4">
                                            <label for="street">Application URL </label>
                                            <input class="form-control" placeholder="Enter Application URL [e.g: (https://abc.com)]" type="text" name="relay_app_url" id="relay_app_url"  value="" maxlength="50" />
                                            <font style="color:red"><span id="app_url_err"></span></font>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="street">Name of Division <span style="color: red">*</span></label>
                                                <input class="form-control" placeholder="Enter Name of Division, [characters limit[50]  only, dot(,),comma(,) whitespace allowed]" type="text" name="division" id="division" value="" maxlength="50"  />
                                                <font style="color:red"><span id="division_err"></span></font>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-12">
                                    <div class="row form-group">
                                        <div class="col-md-6">
                                            <label for="street">Operating System (Name, Version) <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Operating System (Name, Version), [Only characters limit[100],whitespace,comma(,),hypen(-) allowed]" type="text" name="os" id="os"  value="" maxlength="100" />
                                            <font style="color:red"><span id="os_err"></span></font>
                                        </div>

                                        <div class="col-md-6">
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
                                        <div class="col-md-4 display-hide pt-3" id="server_other">
                                            <label for="street">Enter server location <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                                            <font style="color:red"><span id="server_txt_err"></span></font>
                                        </div>
                                    </div>

                                </div>
                                <div id="check-mxdomain-available">
                                    <div class="col-md-12">
                                        <div class="row form-group">


                                            <div class="col-6">
                                                <label for="street">Port <span style="color: red">*</span></label>
                                                <div class="mt-2">
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="25" name="smtp_port"  id="port_25" checked=""> Port 25
                                                        <span></span>
                                                    </label>&emsp;&emsp;
                                                    <label class="k-radio k-radio--bold k-radio--brand">
                                                        <input type="radio" value="465" name="smtp_port"> Port 465
                                                        <span></span>
                                                    </label>
                                                </div>
                                                <font style="color:red"><span id="port_err"></span></font>
                                            </div>
                                            <div class="col-6 d-none" id="auth_id_div">
                                                <label for="">Auth ID <span style="color: red">*</span></label>
                                                <input type="text" name="relay_auth_id" id="auth_id" class="form-control" placeholder="Enter Auth ID">
                                                <font style="color:red"><span id="auth_id_err"></span></font>
                                            </div>





                                        </div>
                                    </div>
                                    <div class="col-md-12">
                                        <div class="row form-group">
                                            <div class="col-md-6">
                                                <label for="street">Sender ID <span style="color: red">*</span></label>
                                                <input class="form-control" placeholder="Enter Sender ID. Ex: no-reply@xyz.gov.in" type="text" name="relay_sender_id" id="relay_sender_id"  value="" maxlength="100" />
                                                <font style="color:red"><span id="sender_id_err"></span></font>
                                            </div>
                                            <div class="col-md-6 div-mx">
                                                <label for="street">MX of the Domain </label>
                                                <input class="form-control" placeholder="MX of the Domain" type="text" name="domain_mx" id="domain_mx"  value="" maxlength="100"  disabled="disabled"/>
                                                <font style="color:red"><span id="domain_mx_err"></span></font>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-12">
                                        <div class="row form-group">
                                            <div class="col-md-6">
                                                <label for="street">Total Number of mails to be sent daily (approx)</label>
                                                <input class="form-control" placeholder="Total numbers of mail" type="text" name="mailsent" id="mailsent"  value="" maxlength="100" />
                                                <font style="color:red"><span id="total_mail_err"></span></font>
                                            </div>
                                            <div id="other-record-addition" class="col-md-6 d-none">
                                                <label class="mb-3" for="street"><strong>Other Record Addition: </strong></label>
                                                <div class="row">
                                                    <div class="col-2">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="spf" name="spf" id="spf"> SPF
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-2">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="dkim" name="dkim" id="dkim"> DKIM
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-2">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="dmarc" name="dmarc" id="dmarc"> DMARC
                                                            <span></span>
                                                        </label>
                                                    </div>

                                                </div>
                                                <font style="color:red"><span id="record_err"></span></font>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-12 mt-2">
                                        <div class="form-group">
                                            <label for=""><b>Mail Type</b></label>
                                            <div class="mt-2">
                                                <div class="row">
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="OTP Service through Email" name="otp_mail_service" id="otp_mail_service"
                                                                   > OTP Service through Email
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Transactional Mails" name="mail_type_trans_mail" id="mail_type_trans_mail"> Transactional Mails
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Registration Mails" name="mail_type_reg_mail" id="mail_type_reg_mail"> Registration Mails
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Forgot id/password" name="mail_type_forgotpass" id="mail_type_forgotpass"> Forgot id/password
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-1 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Alerts" name="mail_type_alert" id="mail_type_alert"> Alerts
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-1 col-sm-3 mt-2 pt-1">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" value="Others" name="mail_type_other" id="mail_type_other"> Others
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                    <div class="col-md-2 col-sm-6">
                                                        <input type="text" name="other_mail_type" id="other_mail_type" class="form-control d-none" placeholder="Other Mail type">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-12">
                                        <label for="" class='mb-3'><strong>Are you The point of contact of the application?</strong></label>
                                        <div class="row form-group">
                                            <div class="col-md-2">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" value="yes" name="point_contact" id="point_contact_1"> Yes
                                                    <span></span>
                                                </label>
                                            </div>
                                            <div class="col-md-2">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" value="no" name="point_contact" id="point_contact_2" checked="checked"> No
                                                    <span></span>
                                                </label>
                                            </div>
                                        </div>
                                        <div class="row form-group">
                                            <div class="col-md-3">
                                                <label for="street">Name  <span style="color: red">*</span></label>
                                                <input class="form-control " placeholder="Enter Name" type="text" name="point_name" id="point_name"  value="">
                                                <font style="color:red"><span id="point_name_error"></span></font>  
                                            </div>
                                            <div class="col-md-3">
                                                <label for="street">Email  <span style="color: red">*</span></label>
                                                <input class="form-control " placeholder="Enter Email Address" type="text" name="point_email" id="point_email"  value="">
                                                <font style="color:red"><span id="point_email_error"></span></font>
                                            </div>
                                            <div class="col-md-3">
                                                <label for="street">Mobile Number  <span style="color: red">*</span></label>
                                                <input class="form-control " placeholder="Enter Mobile Number" type="text" name="mobile_number" id="mobile_number"  value="" maxlength="15">
                                                <font style="color:red"><span id="point_mobile_error"></span></font>
                                            </div>
                                            <div class="col-md-3">
                                                <label for="street">Landline Number</label>
                                                <input class="form-control " placeholder="Enter Landline Number (Optional)" type="text" name="landline_number" id="landline_number"  value="" maxlength="15">
                                                <font style="color:red"><span id="point_tel_error"></span></font>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-12 d-none" id="domain-hosted-with-nic">
                                        <label for="" class='mb-3'><strong>Is domain hosted with nic</strong></label>
                                        <div class="row form-group">
                                            <div class="col-md-2">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" value="yes" name="is_hosted" id="is_hosted_1" checked="checked"> Yes
                                                    <span></span>
                                                </label>
                                            </div>
                                            <div class="col-md-2">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" value="no" name="is_hosted" id="is_hosted_2"> No
                                                    <span></span>
                                                </label>
                                            </div>
                                        </div>

                                    </div>

                                </div>


                                <font style="color:red"><span id="request_err"></span></font>
                                <div class="col-md-12 mt-5 text-center" id="previewSubmit">
                                    <div class="col-md-12">
                                        <div class="row mt-5 mb-3">
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
                                    <input type="hidden" id="cert" value="false" />
                                    <input type="hidden" id="hardwarecert" value="false" />
                                    <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                    <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                    <button name="submit" value="preview" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                </div>
                            </form>

                        </div>
                    </div>
                </div>
            </div> <!-- 04-july 2022 relay issue instruction not visible-->
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
                        <ol type="I" class="wizard-list">
                            <li>
                                <b>ALL TESTING MUST BE DONE FROM SERVER FROM WHICH NIC EMAIL RELAY GATEWAY NEEDS TO BE INTEGRATED.</b>
                            </li>
                            <li>
                                Check the domain look up using nslookup command. If domain (relay.nic.in) does not get resolved,
                                then please contact your server/firewall administrator.<br/> Refer below screenshot for using nslookup command.
                                <br/>
                                <img src="assets/img/relay_nslookup.png" alt="relay-nslookup" />
                            </li>
                            <li>
                                A) For applications hosted in NIC/NKN network Telnet relay.nic.in (164.100.14.95) on port 25.
                                If telnet works then connection to relay server is OK on port 25.
                                <br/>
                                <img src="assets/img/relay_25_yes.png" alt="relay 25 port telnet" />
                                <br/>
                                If telnet does not work, then there is connection issue. Refer below screenshot.
                                <br/>
                                <img src="assets/img/relay_25_no.png" alt="relay 25 port telnet" />
                                <br/>
                                B) For applications hosted outside NIC/NKN network.
                                I. Telnet relay.nic.in (164.100.14.95) on port 465. If telnet works then connection to relay server is
                                OK on port 465.
                                <br/>
                                <img src="assets/img/relay_465_yes.png" alt="relay 465 port telnet" />
                                <br/>
                                If telnet does not work, then there is connection issue. Refer below screenshot.<br/>
                                <img src="assets/img/relay_465_no.png" alt="relay 465 port telnet" />
                                <br/>                                                                                                        
                            </li>
                            <li>
                                Integration of relay service over port 465 requires a userid/password for authentication.
                                If you don’t have an id, same may be applied at <a href="https://eforms.nic.in/Email_registration.jsp">https://eforms.nic.in/Email_registration.jsp</a>.       
                            </li>
                        </ol>
                        <div class="alert alert-info"><strong>Disclaimer :</strong>  NIC will not be responsible if the testing is not done from the host machine at which application is hosted.</div>
                    </div>
                </div>
            </div>
            <div class="portlet light" id="form_wizard_3" style="display:none;">
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
                                    <span onclick="backToMain()" class="dns_history_page read_instruction">Go to Relay Form</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="col-md-12">
                        <ol type="I" class="wizard-list">
                            <li>
                                One possible reason for telnet failure is that your application server is behind a firewall and 
                                firewall rule is not configured to allow traffic from your server to NIC Email Relay Gateway.
                            </li>
                            <li>
                                In this regard you have to apply for firewall rule through <a href="https://farps.nic.in">https://farps.nic.in</a>.                                        
                                <p>
                                    * If both private IP and Public IP are configured on your application server, then before filling up the following firewall form,
                                    please confirm from your network/firewall administrator that the Relay request from your application will reach Realy server from which IP (PUBLIC or PRIVATE).
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
                                        <td style="padding:5px;">Mail Relay Server </td>
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
                                        <td style="padding:5px;">164.100.14.95 </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:5px;">URL of the application hosted on above IP (if any)</td>
                                        <td style="padding:5px;"></td>
                                    </tr>
                                </table>
                                <br/>
                                <label for="about"><b>Public IP rule for access from Outside Systems to Server </b></label><br/>
                                <label for="about"><b>For applications hosted in NIC/NKN Network</b></label>
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
                                        <td style="padding:5px;">164.100.14.95</td>
                                        <td style="padding:5px;">SMTP</td>
                                        <td style="padding:5px;"></td>
                                        <td style="padding:5px;">25</td>
                                        <td style="padding:5px;">Sending mail</td>
                                        <td style="padding:5px;">TCP</td>
                                    </tr>
                                </table>
                                <br/>
                            <center><b>OR</b></center>
                            <br/>
                            <label for="about"><b> For Application Hosted outside NIC/NKN Network</b></label>
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
                                    <td style="padding:5px;">164.100.14.95</td>
                                    <td style="padding:5px;">SMTPS</td>
                                    <td style="padding:5px;"></td>
                                    <td style="padding:5px;">465</td>
                                    <td style="padding:5px;">Sending mail</td>
                                    <td style="padding:5px;">TCP</td>
                                </tr>
                            </table>
                            <br/>
                            <p>If domain (relay.nic.in) does not get resolved, then please contact your server/firewall administrator.For any issue related to firewall, NIC Cyber Security team should be contacted on 011-24305140 (Email on : appsecmon2@nic.in, appsecdev3@nic.in , aniljha@nic.in).</p>
                            </li>
                            <li>
                                After you have applied for the firewall rule to allow traffic from your server to NIC Email Relay Gateway, an auto generated request ID will be generated.
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
    <form role="form" id="relay_form2" method="post">
        <jsp:include page="include/relay_preview.jsp" />
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
                <b>NOTE: Please read all instructions carefully and select the required services.</b>
                <br/>
                <ol type="1">
                    <li>Fill one form for one IP only.</li>                                            
                    <li>If administrator is from outside NIC, please get the form approved by NIC coordinator.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default dark btn-outline" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="relay_form_confirm">
        <%

            if (!nic_employee) {%>
        <jsp:include page="include/Hod_detail.jsp" />
        <% } else {%>
        <jsp:include page="include/wifi_detail.jsp" />
        <%}%>
    </form>
</div>
<div id="myModalAlert" class="modal" role="dialog">
    <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Notice</h4>
                <button type="button" class="close" data-dismiss="modal">&times;</button>
            </div>
            <div class="modal-body">
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
<!-- end -->


<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/relay.js" type="text/javascript"></script>
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
        $('#myModalAlert').modal({
            backdrop: 'static',
            show: true
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
    });
</script>
<script>
    $(document).ready(function () {
        $(function () {
            $(".security_exp_date").datepicker();
        });
        if ('<%=session.getAttribute("resend_request")%>' == "true") {
            var form_name = '<s:property value="#session['prvwdetails'].form_name" />';
            if (form_name == "RELAY")
            {
                var app_ip = '<s:property value="#session['prvwdetails'].app_ip" />';
                var app_name = '<s:property value="#session['prvwdetails'].app_name" />';
                var division_name = '<s:property value="#session['prvwdetails'].division_name" />';
                var os = '<s:property value="#session['prvwdetails'].os" />';
                var server_loc = '<s:property value="#session['prvwdetails'].server_loc" />';
                var server_loc_other = '<s:property value="#session['prvwdetails'].server_loc_other" />';
                var uploaded_filename = '<s:property value="#session['prvwdetails'].uploaded_filename" />';
                var staging_ip = '<s:property value="#session['prvwdetails'].staging_ip" />';
                $('#add_ip').html('');
                var error_flag = true;
                if (app_ip.indexOf(";") > -1) {
                    var myarray = app_ip.split(';');
                    for (var i = 0; i < myarray.length; i++)
                    {
                        var service = myarray[i].trim();
                        if (i === 0) {
                            $('#relay_ip').val(service);
                        } else {
                            var elmnt = '<div class="con"><input class="form-control" placeholder="Enter the IP Address [e.g.: 127.0.0.1]" type="text" name="relay_ip[]" id="relay_ip1" value="' + service + '" maxlength="12" style="margin: 20px 0px 20px 0px; width: 96%;"></div>';
                            $('#add_ip').append(elmnt);
                        }
                    }
                } else {
                    $('#relay_ip').val(app_ip);
                }
                $('#relay_app_name').val(app_name);
                $('#division').val(division_name);
                $('#os').val(os);
                if (server_loc === "Other")
                {
                    $('#server_other').removeClass('display-hide');
                    $('#server_loc_txt').val(server_loc_other);
                    $('#server_loc').val(server_loc);
                } else {
                    $('#server_other').addClass('display-hide');
                    $('#server_loc_txt').val('');
                    $('#server_loc').val(server_loc);
                }
                if (staging_ip === "yes")
                {
                    $('#cert_div').addClass('display-hide');
                    $('#cert_div1').removeClass('display-hide');
                } else {
                    $('#cert_div').removeClass('display-hide');
                    $('#cert_div1').addClass('display-hide');
                    //$('#cert_file').text(uploaded_filename);
                }
            }
        }

    });
</script>