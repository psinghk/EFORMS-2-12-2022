<%-- 
    Document   : retired-officers
    Created on : Aug 30, 2022, 9:35:31 AM
    Author     : rahul
--%>

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
        String CSRFRandom = entities.Random.csrf_random();
        String email = "", mobile = "", dor = "", newdor = "", name = "";
        System.out.println("SESSION :::: " + ActionContext.getContext().getSession());
       
        if (ActionContext.getContext().getSession() != null) {
            session.setAttribute("CSRFRandom", CSRFRandom);
            if (session.getAttribute("email") == null || session.getAttribute("email").toString().isEmpty()) {
                session.invalidate();
                response.sendRedirect("index.jsp");
            } else {
                email = session.getAttribute("email").toString();
            }

            if (session.getAttribute("mobile") == null || session.getAttribute("mobile").toString().isEmpty()) {
                session.invalidate();
                response.sendRedirect("index.jsp");
            } else {
                mobile = session.getAttribute("mobile").toString();
            }

            if (session.getAttribute("name") == null || session.getAttribute("name").toString().isEmpty()) {
                session.invalidate();
                response.sendRedirect("index.jsp");
            } else {
                name = session.getAttribute("name").toString();
            }

            if (session.getAttribute("dor") == null || session.getAttribute("dor").toString().isEmpty()) {
                session.invalidate();
                response.sendRedirect("index.jsp");
            } else {
                dor = session.getAttribute("dor").toString();
            }

            if (session.getAttribute("newdor") == null || session.getAttribute("newdor").toString().isEmpty()) {
                session.invalidate();
                response.sendRedirect("index.jsp");
            } else {
                newdor = session.getAttribute("newdor").toString();
            }
        }
    %>

    <head>
        <meta charset="utf-8">
        <title>@Gov.in | e-Forms</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <link href="assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/home-page/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/home-page/lib/bootstrap/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
        <link rel="stylesheet" href="assets/home-page/update-mobile/fonts/material-design-iconic-font/css/material-design-iconic-font.css">
        <link rel="stylesheet" href="assets/home-page/update-mobile/css/style.css">
        <link rel="stylesheet" href="assets/home-page/update-mobile/css/navigation.css">
        <link href="assets/old_assets/css/jquery-ui.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />\
        <style>
            #wizard {
                margin-right: 20px !important;
                padding: 30px !important;
            }

            ul.notice-ul li {
                font-size: 18px !important;
                font-family: auto !important;
            }
            
            .dowload-mannual {
                font-size: 19px;
                color: coral;
            }
            .dowload-mannual:hover {
                color: darksalmon;
            }
        </style>
    </head>
    <body>
        <div class="loader"><img src="assets/images/loader-1.gif" alt="" /></div>
        <header id="header" class="fixed-top">
            <div class="container">
                <div class="logo float-left">
                    <a href="<%= url%>" class="scrollto"><img src="assets/home-page/img/eforms-logo.png" alt="" class="img-fluid"></a>
                </div>
                <!--                <nav class="main-nav float-right d-none d-lg-block">
                                    <ul>
                                        <li class="active"><a href="https://eforms.nic.in/">Home</a></li>
                                        <li><a href="https://eforms.nic.in/#services">Services</a></li>
                                        <li><a href="https://eforms.nic.in/#why-us">In Focus</a></li>
                                        <li><a href="https://servicedesk.nic.in/" target="_blank" title="External Link">Contact Us</a></li>
                                    </ul>
                                </nav>-->
            </div>
        </header>
        <div class="wrapper display-show" id="retired_basic">
            <div class="image-holder text-white">
                <h1 style="font-weight: 700;font-family: auto;">For Retired Officers only</h1><br/>
                <ul class="notice-ul">
                    <!--                    <li>This facility is available only to email addresses which are on NIC Platform</li>
                                        <li><b>You will be signing in through https://parichay.nic.in (NIC Single Sign-On Platform)</b></li>-->
                    <li>Request will be submitted through eSign with Aadhaar OTP.</li>
                    <li>Validity of your email address will be extended for 1 year from current date</li>
                    <li>You can extend the validity maximum 30 days prior to your account expiry date.</li>
                    <li>For any queries/issues, Please contact our 24x7 support toll free number <b>1800-111-555</b> or you can raise ticket on <b>https://servicedesk.nic.in</b></li>
                    <li><a href="download_Retired_Manual" class="dowload-mannual"><i class=" fas fa-solid fa-download mr-1"></i>Click here to download User Mannual</a></li>
                </ul>
            </div>
            <form action="" method="post" id="dor_ext1" class="form_val" autocomplete="off">
                <div id="wizard">
                    <h1 class="head-1 mb-4">Extend the Validity of Account</h1>
                    <div class="alert alert-danger d-none" id="commonErr"></div>
                    <div class="retired-users">
                        <!--                        <h2 class="head-3 mt-3 mb-3">Extend the Validity of Account</h2>-->
                        <div class="form-group">
                            <div class="alert alert-warning mt-2" style="text-align: justify;">
                                Please note that updation of Account Expiry Date of an email address for which access is not permitted to an individual may lead to prosecution as per IT Act and other governing laws of Govt of India.
                            </div>
                            <div class="alert alert-primary">
                                <div class="row">
                                    <div class="col-1">
                                        <input type="checkbox" id="tnc" class="regular-checkbox mt-2" /><label for="tnc" class="mt-2"></label>
                                    </div>
                                    <div class="col-10">
                                        I Hereby confirm that I am the owner of this email account and hence i am  an authorised user to update the account expiry date of this Email Address. I also confirm that I am alive and have submitted my life certificate to bank from where I withdraw my pension.
                                    </div>
                                </div>
                            </div>
                        </div>                      

                        <div class="row mt-4">
                            <div class = "col-md-6">
                                <label for=""><b>Email Address</b> <span style="color: red">*</span></label>
                                <input type="text" name="dor_email" id="dor_email" class="form-control act_email1" placeholder="Enter Email ID (Ex: abc.xyz@nic.in)" value="<%=email%>" readonly/>
                                <font style="color:red"><span id="dor_ext_err"></span></font>&nbsp;&nbsp;&nbsp;
                            </div>
                            <div class = "col-md-6">
                                <label for="street" ><span id="p_dor_text"><b>Previous Date Of Account Expiry</b> </span> <span style="color: red">*</span></label>
                                <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="p_single_dor" id="p_email_single_dor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" value="<%=dor%>" readonly/>
                                <font style="color:red"><span id="prev_dor_err"></span></font>&nbsp;
                            </div>
                        </div>

                        <input type="hidden" name="p_single_name" id="p_email_single_name" value="<%=name%>"/>
                        <input type="hidden" name="p_single_mobile" id="p_email_single_mobile" value="<%=mobile%>"/>
                        <input type="hidden" name="p_single_newdor" id="p_email_single_newdor" value="<%=newdor%>"/>       
                        <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                    </div>
                    <div class="form-group new-code">
                        <div class="row mt-3">
                            <div class="col-6">
                                <div class="row captcha-img mt-4 pt-1">
                                    <div class="col-7 pr-0">
                                        <img name="Captcha" id="captcha21" src="Captcha?var=<%=CSRFRandom%>" width="130" height="55" alt="captcha background" class="ft-lft captcha float-right" />
                                    </div>
                                    <div class="col-3 mt-3 pt-1">
                                        <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" onclick="refreshCaptcha()" height="20" width="20">
                                    </div>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="form-group">
                                    <label class="control-label" for="street">Enter Captcha<span style="color: red">*</span></label>
                                    <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="captcha" id="captcha" maxlength="6" value="" autocomplete="off">
                                    <div><small class="text-danger" id="captchaerror"></small></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="overflow:auto;" class="mt-4">
                        <div class="col-md-12 p-0">
                            <button type="button" class="btn btn-primary btn-action float-right" id="submit" onclick="submitByRetiredOfficers()">Next</button>
                            <!--<button type="button" class="btn btn-primary btn-action float-right" id="submit"><a href="retired-officers_success" style="color: #ffff;">Next</a></button>-->
                        </div>
                    </div>
                </div>
            </form>         
        </div>

        <div class="wrapper display-hide" id="retired_preview">
            <div class="image-holder text-white">
                <h1 style="font-weight: 700;font-family: auto;">For Retired Officers only</h1><br/>
                <ul class="notice-ul">
                    <!--                    <li>This facility is available only to email addresses which are on NIC Platform</li>
                                        <li><b>You will be signing in through https://parichay.nic.in (NIC Single Sign-On Platform)</b></li>-->
                    <li>Request will be submitted through eSign with Aadhaar OTP.</li>
                    <li>Validity of your email address will be extended for 1 year from current date</li>
                    <li>You can extend the validity maximum 30 days prior to your account expiry date.</li>
                    <li>For any queries/issues, Please contact our 24x7 support toll free number <b>1800-111-555</b> or you can raise ticket on <b>https://servicedesk.nic.in</b></li>
                   <li><a href="download_Retired_Manual" class="dowload-mannual"><i class=" fas fa-solid fa-download mr-1"></i>Click here to download User Mannual</a></li>
                </ul>
            </div>
            <form action="" method="post" id="dor_ext2" class="form_val" autocomplete="off">
                <div id="wizard" style="min-height: 560px">
                    <h1 class="head-1 mb-3">Extend the Validity of Account</h1>
                    <div class="portlet-body form">
                        <div class="k-portlet__head">
                            <!--                            <h2 class="head-3 mt-3 mb-3">Extend the Validity of Account</h2>-->
                        </div>
   
                        <!--<div class="row">-->
                             <div class="col-md-12 mb-3 p-0">
                                <label class="control-label" for="street"><b>Email Address</b> <span style="color: red">*</span></label>
                                <input class="form-control act_email1" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com OR abc.xyz@nic.in]" type="text" name="dor_email" id="dor_email" value="" readonly>
                                <font style="color:red"><span id="single_email1_err"></span></font>
                            </div>
                            <div class = "col-md-12 mb-3 p-0">
                                <label class="control-label" for="street" ><b>Name</b> <span style="color: red">*</span></label>
                                <input class="form-control " type="text" name="p_name_dor" id="p_email_single_name" placeholder="Enter name of the user" readonly/>
                                <font style="color:red"><span id="prev_dor_err"></span></font>
                            </div>
                        <!--</div>-->
                        <!--<div class="row">-->
                            <div class="col-md-12 mb-3 p-0" id="divDor"> 
                                <label class="control-label" for="street"><b>Previous Date Of Account Expiry</b> <span style="color: red">*</span></label>
                                <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="p_single_dor" id="p_email_single_dor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                                <font style="color:red"><span id="single_dor_err"></span></font>
                            </div>
                            <div class="col-md-12 mb-3 p-0" id="divDor"> 
                                <label class="control-label" for="street"><b>Date Of Account Expiry</b> <span style="color: red">*</span></label>
                                <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="p_email_single_newdor" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                                <font style="color:red"><span id="single_dor_err"></span></font>
                            </div>
                        <!--</div>--> 
                        <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                        <div class="form-group" id='tnc_div' style="display: flex;">
                            <div class="mt-checkbox-list mt-3">
                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                    <input type="checkbox" name="tnc" id="tnc_retired" class="ml-2" style="width: 13px;"> <b>I agree to <a data-toggle="modal" id="tnc_retired_officers" class="mt-2" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                                    <span></span>
                                </label>
                                <br>
                                <font style="color:red"><span id="tnc_error"></span></font>
                            </div>
                        </div>
                    </div>
                    <div style="overflow:auto;" class="mt-4">
                        <div class="col-md-12 p-0">
                            <button type="button" class="btn btn-primary btn-action float-right" id="submit" onclick="submitPeviewByRetiredOfficers()">Submit</button>
                        </div>
                    </div>
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
    
    <div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <ol>                           
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                    <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                </ol>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
    <script src="main_js/retired-officers.js" type="text/javascript"></script>
    <script src="assets/vendors/general/jquery/dist/jquery.js?v=1.0" type="text/javascript"></script>
    <script src="assets/old_assets/plugins/jquery.serializeObject.js?v=1.0"></script>
    <script src="assets/home-page/update-mobile/js/navigation.js"></script>
    <script src="assets/home-page/lib/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="assets/old_assets/plugins/jquery-ui.js?v=1.0"></script>
    <script src="assets/datepickertime/js/moment.min.js" type="text/javascript"></script>
    <script src="assets/datepickertime/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
    <script src="assets/home-page/update-mobile/js/updatemobsubmit.js"></script>
    <script>
                                $(document).ready(function () {
                                    $('#submit').prop('disabled', true);
                                });
                                $("#tnc").click(function () {
                                    console.log("tnc value   " + $('#tnc'));
                                    if (document.getElementById("tnc").checked === true)
                                        $('#submit').prop('disabled', false);
                                    else
                                        $('#submit').prop('disabled', true);

                                    console.log(document.getElementById("tnc").checked);
                                });
    </script>
</body>
</html>

