<%@page import="java.util.Map"%>
<%@page import="com.org.dao.Ldap"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <!-- BEGIN HEAD -->
    <%
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Expires", "0");
        response.setDateHeader("Expires", -1);
        response.setHeader("X-Frame-Options", "DENY");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
        String url = request.getRequestURL().toString();
        String random = entities.Random.csrf_random();
        session.setAttribute("rand", random);
        Ldap ldap = new Ldap();
        String name = "";
        String mobile = "";
        Map<String, Object> contents;
        String dob = "";
        String dor = "";
        if (ActionContext.getContext().getSession() != null) {
            if (ActionContext.getContext().getSession().get("useremail") != null) {

                name = ldap.fetchNameFromLdap(ActionContext.getContext().getSession().get("useremail").toString());
                mobile = ldap.fetchMobileFromLdap(ActionContext.getContext().getSession().get("useremail").toString());
                contents = ldap.fetchAttrUpdateMobile(ActionContext.getContext().getSession().get("useremail").toString());
                dob = contents.get("nicDateOfBirth").toString();
                dor = contents.get("nicDateOfRetirement").toString();

            }
        }

    %>






    <head>
        <meta charset="utf-8">
        <title>@Gov.in | e-Forms</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <!-- Bootstrap CSS File -->
        <link href="assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/home-page/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="assets/home-page/update-mobile/fonts/material-design-iconic-font/css/material-design-iconic-font.css">
        <link rel="stylesheet" href="assets/home-page/update-mobile/css/style.css">
        <link rel="stylesheet" href="assets/home-page/update-mobile/css/navigation.css">
        <link href="assets/old_assets/css/jquery-ui.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />
    </head>
    <body>
        <div class="loader"><img src="assets/images/loader-1.gif" alt="" /></div>
        <header id="header" class="fixed-top">
            <div class="container">

                <div class="logo float-left">
                    <!-- Uncomment below if you prefer to use an image logo -->
                    <!-- <h1 class="text-light"><a href="#header"><span>NewBiz</span></a></h1> -->
                    <a href="<%= url%>" class="scrollto"><img src="assets/home-page/img/eforms-logo.png" alt="" class="img-fluid"></a>
                </div>

                <nav class="main-nav float-right d-none d-lg-block">
                    <ul>
                        <li class="active"><a href="https://eforms.nic.in/">Home</a></li>
                        <li><a href="https://eforms.nic.in/#services">Services</a></li>
                        <li><a href="https://eforms.nic.in/#why-us">In Focus</a></li>
                        <!--                        <li><a href="https://eforms.nic.in/">Feedback</a></li>-->
                        <li><a href="https://servicedesk.nic.in/" target="_blank" title="External Link">Contact Us</a></li>
                    </ul>
                </nav>
                <!-- .main-nav -->

            </div>
        </header>
        <div class="wrapper">
            <div class="image-holder text-white">
                <h1 style="font-weight: 700;font-family: sans-serif;">Update Mobile & Profile</h1>
                <ul class="notice-ul">
                    <li>This facility is available only to email addresses which are on NIC Platform</li>
                    <li><b>For updation of international contact numbers, Please contact your NIC Coordinator/Delegated Admin</b></li>
                    <li>Please use <b>"On Behalf"</b> option, if you are updating mobile number for someone else.</li>
                    <li>Request will be submitted through eSign with Aadhaar OTP.</li>
                    <li>Your mobile number and Name will be updated in NIC repository.</li>
                    <!--                    <li>If you are NIC employee, please make sure your details are updated in https://digital.nic.in too.</li>-->
                    <li>For any queries/issues, Please contact our 24x7 support toll free number <b>1800-111-555</b> or you can raise ticket on <b>https://servicedesk.nic.in</b></li>
                </ul>
            </div>
            <form action="" id="update-mobile">
                <div id="wizard">
                    <h1 class="head-1">Update Mobile & Profile</h1>
                    <div class="mt-2 mb-3">
                        <span class="step"></span>
                        <span class="step"></span>
                        <span class="step"></span>
                        <span class="step"></span>
                        <span class="step"></span>
                    </div>
                    <div class="alert alert-danger d-none" id="commonErr"></div>
                    <!-- One "tab" for each step in the form: -->
                    <div class="tab">
                        <h2 class="head-3 mb-3">Enter Email</h2>
                        <div class="form-group">
                            <div class="alert alert-warning mt-2" style="text-align: justify;">
                                Please note that updation of a mobile number against an email address for which access is not permitted to an individual may lead to prosecution as per IT Act and other governing laws of Govt of India.
                            </div>
                            <div class="alert alert-primary">
                                <div class="row">
                                    <div class="col-1">
                                        <input type="checkbox" id="tnc" class="regular-checkbox mt-2" /><label for="tnc" class="mt-2"></label>
                                    </div>
                                    <div class="col-10">
                                        I Hereby confirm that I am the Authorised User to update the mobile number of this Email Address.
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="">
                                Enter the email address on NIC Platform
                            </label>
                            <input autofocus class="form-control form-control-solid placeholder-no-fix" type="email" autocomplete="off" placeholder="Enter Your Email Address" name="user_email" id="email" maxlength="50" />
                            <small class="text-dark text-monospace">(user@nic.in, user@gov.in, user@mea.gov.in etc.)</small>
                            <div><small id="email_error" class="text-danger"></small></div>
                        </div>
                    </div>

                    <div class="tab">
                        <h2 class="head-3 mb-3">Verify Password</h2>
                        <div class="form-group">
                            <label>Email Address</label>
                            <input class="form-control form-control-solid placeholder-no-fix emailClone" type="email" autocomplete="off" placeholder="Enter Your Email Address" name=""  maxlength="50" readonly />
                        </div>
                        <div class="form-group new-code">
                            <label>Enter Password</label>
                            <input class="form-control form-control-solid placeholder-no-fix" type="password" autocomplete="off" placeholder="Please Enter Your Email Password" name="password" id="password" maxlength="50" />
                            <div><small id="password_error" class="text-danger"></small></div>
                        </div>
                        <div class="form-group new-code">
                            <div class="row">
                                <div class="col-6">

                                    <div class="row captcha-img mt-4 pt-1">
                                        <div class="col-7 pr-0">
                                            <img name="Captcha" id="captcha21" src="Captcha?var=<%=random%>" width="130" height="55" alt="captcha background" class="ft-lft captcha float-right" />
                                        </div>
                                        <div class="col-3 mt-3 pt-1">
                                            <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                        </div>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <div class="form-group">
                                        <label class="control-label" for="street">Enter Captcha<span style="color: red">*</span></label>
                                        <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="captcha11" id="captcha11" maxlength="6" value="" autocomplete="off">
                                        <div><small class="text-danger" id="logincaptchaerror11"></small></div>
                                    </div>
                                </div>

                            </div>
                        </div>
                        <div class="form-group"></div>
                    </div>

                    <div class="tab">
                        <h2 class="head-3 mb-3">Mobile Number Details</h2>

                        <div class="form-group">
                            <label>Email Address</label>
                            <input class="form-control form-control-solid placeholder-no-fix emailClone" type="email" autocomplete="off" placeholder="Enter Your Email Address" name=""  maxlength="50" readonly />
                        </div>
                        <!--                       <div class="row form-group">
                                                   <div class="col-md-6">
                                                       <label for="">Date of Birth</label>
                                                       <input type="text" name="nicDateOfBirth" id="nicDateOfBirth" class="form-control" placeholder="Select Date of Birth" />
                                                   </div>
                                                   <div class="col-md-6">
                                                       <label for="">Date of Retirement</label>
                                                       <input type="text" name="nicDateOfRetirement" id="nicDateOfRetirement" class="form-control" placeholder="Select Date of Retirement" />
                                                   </div>
                                               </div>
                                               <div class="row form-group">
                                                   <div class="col-md-6">
                                                       <label for="">Designation</label>
                                                       <input type="text" name="designation" id="designation" placeholder="Enter Designation" class="form-control" />
                                                   </div>
                                                   <div class="col-md-6">
                                                       <label for="">Display Name</label>
                                                       <input type="text" name="displayName" id="displayName" class="form-control" placeholder="Enter Display Name" />
                                                   </div>
                                               </div>-->
                        <div id="old-mobile">
                            <p class="alert alert-primary">
                                <b>Current Mobile Number:</b>&nbsp;<span id="mobile"></span>
                            </p>
                        </div>

                        <div id="updateoptionProfile">
                            <div class="alert alert-info">
                                <label><b>I want to update</b></label>
                                <div class="row">
                                        <div class="col-md-6">
                                            <input type="radio" name="form_name" id="mobileprofile_update"  value="mobile_and_profile" checked="" onClick="updateCheck()" style="width: auto;">&ensp;<b>Mobile & Profile</b>
                                        </div>
                                        <div class="col-md-6">
                                            <input type="radio" name="form_name" id="profile_update"  value="profile" onClick="updateCheck()" style="width: auto;">&ensp;<b>Profile</b>
                                        </div>
                                    </div>
                            </div>
                        </div>

                        <div id="new-mobile">
                            <div class="row">
                                <div class="col-3 pr-0">
                                    <label>Country Code</label>
                                    <input type="text" id="country_opt" class="form-control" data-toggle="tooltip" title="For updation of international contact numbers, Please contact your NIC Coordinator/Delegated Admin" value="+91 (India)" readonly />
                                    <input type="hidden" name="country_code" id="country_code_update_mob" value="+91" />
                                    <input type="hidden" name="type" id="updation_type" value="mobile">
                                </div>
                                <div class="col-9">
                                    <label>Enter New Mobile Number</label>
                                    <input type="text" autocomplete="off" class="form-control" name="new_mobile" id="newmobile" placeholder="Enter Mobile Number" maxlength="10"/>
                                    <small class="text-danger" id="moberr"></small>
                                </div>
                                <div class="col-md-12">
                                    <div id="moberrMax"></div>
                                    <small class="text-danger country_err"></small>
                                </div>
                            </div>
                        </div>

                        <div class="mt-2 display-hide" id="new-code">
                            <div class="row new-code">
                                <div class="col-md-12"><label>Enter OTP Sent to Mobile Number</label></div>
                                <div class="col-8 pr-0">
                                    <input type="password" autocomplete="off" class="form-control" placeholder="Enter New OTP code" name="newcode" id="newMobilecode" maxlength="6">
                                </div>
                                <div class="col-4">
                                    <span val="resend_mobile" class="btn btn-warning otp-btn d-block" id="resendOtppp" onclick="mobileDetail('otpResend')">Resend OTP</span>
                                </div>
                            </div>
                            <div class="col-md-12 p-0">
                                <small id="err_msggg" class="text-danger"></small>
                                <small id="succ_err" class="text-success" style="font-weight: 700;"></small>
                                <small class="text-success" id="succ_msg" style="font-weight: 700;"></small>
                            </div>
                        </div>
                    </div>

                    <div class="tab">
                        <b>User Details(Please enter the details of the individual whose mobile number is getting updated)</b>
                        <div class="form-group mt-3">
                            <div class="row">
                                <div class="col-6 pr-0">
                                    <label>Email Address</label>
                                    <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="email" autocomplete="off" placeholder="Enter Your Email Address" name="entered-email" id="entered-email" maxlength="50" readonly />
                                </div>
                                <div class="col-6">
                                    <!--<label>New Mobile Number</label>-->
                                    <label>Entered Mobile Number</label>
                                    <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter Your Mobile Number" name="entered-mobile"  id="entered-mobile" maxlength="50" readonly />

                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-md-6 pr-0">
                                <label for="">Title of the account owner</label>
                                <select name="initials" id="titleId" class="form-control">
                                    <option value="">-Title-</option>
                                    <option value="Mr.">Mr.</option>
                                    <option value="Ms.">Ms.</option>
                                    <option value="Mrs.">Mrs.</option>
                                    <option value="Dr.">Dr.</option>
                                    <option value="Er.">Er.</option>
                                </select>
                                <small id="initial_err" class="text-danger"></small>
                            </div>
                            <div class="col-md-6">
                                <label> First Name of the account owner</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter First Name" name="fname" id="fnameId"/>
                                <small id="fname_err" class="text-danger"></small>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-md-6 pr-0">
                                <label>Middle Name of the account owner</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter MIddle Name" name="mname" id="mnameId"/>
                                <small id="mname_err" class="text-danger"></small>
                            </div>
                            <div class="col-md-6">
                                <label>Last Name of the account owner</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter Last Name" name="lname" id="lnameId"/>
                                <small id="lname_err" class="text-danger"></small>
                            </div>
                        </div>



                        <div class="row form-group">
                            <div class="col-md-6">
                                <label for="">Date of Birth</label>
                                <input type="text" name="nicDateOfBirth" id="nicDateOfBirth" class="form-control" placeholder="Select Date of Birth" />
                                <small id="dob_err" class="text-danger"></small>
                            </div>
                            <div class="col-md-6">
                                <label for="">Date of Retirement</label>
                                <input type="text" name="nicDateOfRetirement" id="nicDateOfRetirement" class="form-control" placeholder="Select Date of Retirement" />
                                <small id="dor_err" class="text-danger"></small>
                            </div>
                        </div>
                        <div class="row form-group">
                            <div class="col-md-6">
                                <label for="">Designation</label>
                                <input type="text" name="designation" id="designation" placeholder="Enter Designation" class="form-control" />
                                <small id="des_err" class="text-danger"></small>
                            </div>
                            <div class="col-md-6">
                                <label for="">Display Name</label>
                                <input type="text" name="displayName" id="displayName" class="form-control" placeholder="Enter Display Name" />
                                <small id="dis_err" class="text-danger"></small>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-md-12">
                                <label>I am updating mobile number</label>
                            </div>
                            <div class="col-6">
                                <div class="row">
                                    <div class="col-2">
                                        <input type="radio" id="radio-1-1" name="radioOnBehalf" class="regular-radio" value="Self" checked>
                                        <label for="radio-1-1"></label>
                                    </div>
                                    <div class="col-10">Self</div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="row">
                                    <div class="col-2">
                                        <input type="radio" id="radio-1-2" name="radioOnBehalf" class="regular-radio" value="On Behalf">
                                        <label for="radio-1-2"></label>
                                    </div>
                                    <div class="col-10">On Behalf</div>
                                </div>
                            </div>
                        </div>
                        <div class="d-none" id="onBehalf">
                            <div class="form-group row">
                                <div class="col-md-12" id="selectOnBehalf">
                                    <label>Relationship with Owner ID</label>
                                    <select name="relationWithOwner" id="relationWithOwner" class="form-control">
                                        <option value="">Select Relation</option>
                                        <option value="Personal Secretary">Personal Secretary</option>
                                        <option value="other">Other Relation</option>
                                    </select>
                                    <small id="relation_err" class="text-danger"></small>
                                </div>
                                <div class="col-md-6 d-none" id="othRelation">
                                    <label>Other Relation</label>
                                    <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter Relation" name="otherRelation" />
                                    <small id="other_relation_err" class="text-danger"></small>
                                </div>
                            </div>
                            <div class="form-group">

                                <label for="">
                                    Enter the email address on NIC Platform
                                </label>
                                <input class="form-control form-control-solid placeholder-no-fix" type="email" autocomplete="off" placeholder="Enter Your Email Address" name="on_behalf_email" id="on_behalf_email" maxlength="50"  onkeyup="sendOTP('generate')"/>
                                <small class="text-dark text-monospace">(@nic.in, @gov.in, @mea.gov.in)</small>
                                <div><small id="onbehalf_email_err" class="text-danger"></small></div>

                            </div>
                            <div class="form-group row d-none" id="newcodediv">
                                <div class="col-md-12"><label>Enter OTP Sent to Mobile Number</label></div>
                                <div class="col-8 pr-0">
                                    <input type="password" autocomplete="off" class="form-control" placeholder="Enter New OTP code" name="newcodeonbehalf" maxlength="6">
                                    <small id="otp_onbehalf_err" class="text-danger"></small>


                                </div>
                                <div class="col-4">
                                    <span class="btn btn-warning otp-btn d-block" onclick="sendOTP('resend')">Resend OTP</span>
                                </div>
                                <div class="col-md-12">
                                    <small id="file_cert_err" class="text-danger"></small>
                                    <small id="succ_err" class="text-success" style="font-weight: 700;"></small>
                                    <small class="text-success" id="succ_msg" style="font-weight: 700;"></small>
                                    <small id="err_msgggg" class="text-danger"></small>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="">Reason for updation</label>
                            <select name="reason" id="reason" class="form-control">
                                <option value="">Select Reason</option>
                                <option value="I do not have access to my old number">I do not have access to my old number</option>
                                <option value="This is the designation based Id and has been transferred to me">This is the designation based Id and has been transferred to me</option>
                                <option value="other">Other Reason</option>

                            </select>
                            <small id="reason_err" class="text-danger"></small>
                        </div>
                        <div class="form-group d-none" id="othReason">
                            <label>Enter Reason</label>
                            <textarea name="reason_txt" id="reason_txt" class="form-control" placeholder="Enter Reason"></textarea>
                            <small id="reason_txt_err" class="text-danger"></small>
                        </div>
                        <small id="verified_mobile_err" class="text-danger"></small>
                    </div>

                    <div class="tab">

                        <h2 class="head-3 mb-3">Success Message</h2>
                        <div class="alert alert-success">

                            <%

                                if (request.getParameter("esign") != null) {%>
                            You have successfully updated your Name and Mobile Number in NIC central repository. Now you can login with your updated mobile number.

                            <ul class='mt-4 mb-4'>
                                <li class="mt-3"><b>Updated Name:</b>&ensp;<%= name%> </li>
                                <li class="mt-3"><b>Updated Mobile Number:</b>&ensp;<%= mobile%></li>
                                <li class="mt-3"><b>Updated Date of Birth:</b>&ensp;<%= dob%></li>
                                <li class="mt-3"><b>Updated Date of Retirement:</b>&ensp;<%= dor%></li>
                            </ul>
                            <% } %>
                            <p class="mt-3">
                                To login into eForms, please click below.
                            </p>
                        </div>
                        <div class="col-md-12 mt-4 text-center">
                            <a href="https://parichay.nic.in/Accounts/NIC/index.html?service=eforms" class="btn btn-primary btn-action">LOGIN WITH PARICHAY</a>
                        </div>
                    </div>

                    <div style="overflow:auto;" class="mt-4">
                        <div class="col-md-12 p-0">
                            <!--                            <button type="button" class="btn btn-primary btn-action" id="prevBtn" onclick="nextPrev(-1)">Previous</button>-->
                            <button type="button" class="btn btn-primary btn-action float-right" id="nextBtn" onclick="nextPrev(1)">Next</button>
                        </div>

                    </div>
                    <div class="col-12 p-0">
                        <p class="mt-4" style="color: #000;"><small><b>Note: </b>You can update <b style="color:red;">(Mobile Number, Date of Birth, Date of Retirement, Designation and Display Name)</b> only using Update Mobile & Profile portal.</small></p>
                    </div>

                    <!-- Circles which indicates the steps of the form: -->

                </div>
            </form>
        </div>
        <!--==========================
     Clients Section
   ============================-->
        <section id="clients" class="section-bg section-bg-logo">
            <div class="container">
                <ul class="list-unstyled partners-icon row">
                    <li class="col-md-2 col-4">
                        <a href="https://india.gov.in/" target="_blank"><img src="assets/img/footer/new/india_gov.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-left border-right">
                        <a href="http://nkn.gov.in/" target="_blank"><img src="assets/img/footer/new/nkn.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-right">
                        <a href="http://meity.gov.in/" target="_blank"><img src="assets/img/footer/new/meity.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-right mt-md-0 mt-3">
                        <a href="http://www.digitalindia.gov.in/" target="_blank"><img src="assets/img/footer/new/digital_india.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-right mt-md-0 mt-3">
                        <a href="https://email.gov.in/" target="_blank"><img src="assets/img/footer/new/gov.png"></a>
                    </li>
                    <li class="col-md-2 col-4 mt-md-0 mt-3">
                        <a href="https://mygov.in/" target="_blank"><img src="assets/img/footer/new/mygov.png"></a>
                    </li>
                </ul>
            </div>

        </section>


    </main>

    <!--==========================
Footer
============================-->
    <footer id="footer">
        <div class="container">
            <div class="copyright">
                Designed and Developed by Messaging Division <br />
                <span id="year"></span>&nbsp;&copy; Copyright <a href="https://www.nic.in/" style="color:#fff;" target="_blank" title="External Link"><strong>National Informatics Centre</strong></a>.
            </div>
            <div class="credits">
                All Rights Reserved
            </div>
        </div>
    </footer>
    <!-- #footer -->
    <!-- Modal -->
    <div class="modal fade" id="updateMobilePreview" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Are you Sure?</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="" autocomplete="off">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-6 pr-0">
                                    <label>Entered Email</label>
                                    <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="email" autocomplete="off" placeholder="Enter Your Email Address" name="entered-email" id="enteredemail" maxlength="50" readonly />
                                </div>
                                <div class="col-6">
                                    <label>Entered Mobile Number</label>
                                    <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter Your Mobile Number" name="entered-mobile"  id="enteredmobile" maxlength="50" readonly />
                                </div>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-md-6 pr-0">
                                <label for="">Title</label>
                                <input type="text" name="initials" id="titlePreview" class="form-control" placeholder="Enter Title" />
                            </div>
                            <div class="col-md-6">
                                <label>First Name</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter First Name" name="fname" id="fname"/>
                            </div>
                        </div>
                        <div class="form-group row">
                            <div class="col-md-6 pr-0">
                                <label>Middle Name</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter MIddle Name" name="mname" id="mname"/>
                            </div>
                            <div class="col-md-6">
                                <label>Last Name</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off" placeholder="Enter Last Name" name="lname" id="lname"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-md-6 pr-0">
                                <label>Date of Birth</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off"  name="nicDateOfBirth" id="nicDateOfBirth" readonly="true"/>
                            </div>
                            <div class="col-md-6">
                                <label>Date of Retirement</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off"  name="nicDateOfRetirement" id="nicDateOfRetirement" readonly="true"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-md-6 pr-0">
                                <label>Designation</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off"  name="designation" id="designation" readonly="true"/>
                            </div>
                            <div class="col-md-6">
                                <label>Display name</label>
                                <input class="form-control form-control-solid placeholder-no-fix mobileClone" type="text" autocomplete="off"  name="displayName" id="displayName" readonly="true"/>
                            </div>
                        </div>

                        <div class="alert alert-warning my-3" style="font-size: 13px;">
                            <!--                            Do you want to Update your Mobile Number and Name in NIC Central Repository?-->
                            Do you want to Update your Profile in NIC Central Repository?    
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">No</button>
                    <button type="button" class="btn btn-primary" onclick="SubmitUsingEsign()">Yes</button>
                </div>
            </div>
        </div>

        <%

            if (request.getParameter("esign") != null) {
        %>
        <script type="text/javascript">

            var currentTab = 4;
            <% session.invalidate();%>


        </script>
        <% } else {

        %>
        <script type="text/javascript">

            var currentTab = 0;
        </script>
        <%}%>
    </div>
    <!--    <script src="assets/plugins/jquery.serializeObject.js"></script>-->
    <!-- <script src="assets/home-page/update-mobile/js/jquery-3.3.1.min.js"></script>-->
    <!--    <script src="assets/vendors/general/jquery/dist/jquery.js" type="text/javascript"></script>-->
    <!--<script src="assets/old_assets/plugins/jquery-ui.js"></script>-->
    <!-- JQUERY STEP -->
    <script src="assets/vendors/general/jquery/dist/jquery.js?v=1.0" type="text/javascript"></script>
    <script src="assets/old_assets/plugins/jquery.serializeObject.js?v=1.0"></script>
    <script src="assets/home-page/update-mobile/js/navigation.js"></script>
    <script src="assets/home-page/update-mobile/js/main.js"></script>
    <script src="assets/home-page/update-mobile/js/updatemobsubmit.js"></script>
    <script src="assets/home-page/lib/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="assets/old_assets/plugins/jquery-ui.js?v=1.0"></script>
    <script type="text/javascript">

            $(function () {
                var d = new Date();
                var year = d.getFullYear() - 18;
                d.setFullYear(year);
                $("#nicDateOfBirth").datepicker({dateFormat: 'dd-mm-yy',
                    changeMonth: true,
                    changeYear: true, yearRange: '-65:-18', defaultDate: d
                });
                $("#nicDateOfRetirement").datepicker({changeYear: true,
                    //            changeMonth: true,
                    //            yearRange: "-100:+20",
                    //            dateFormat: "dd-mm-yy",
                    //            minDate: '0' 
                    dateFormat: 'dd-mm-yy',
                    minDate: '1',
                    changeMonth: true,
                    changeYear: true,
                    yearRange: '-99:+50'
                });
            });

            $(document).ready(function () {
                setTimeout(function () {
                    $("#input-field").focus();
                }, 5);
            });
    </script>
    <!-- Template created and distributed by Colorlib -->
</body>
</html>
