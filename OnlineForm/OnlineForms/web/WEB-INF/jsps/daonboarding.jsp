<%-- 
    Document   : move_request
    Created on : Jan 12, 2021, 4:09:56 PM
    Author     : Gyan
--%>
<!--

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="/action_page.php" id="move_tab">
            <label for="fname">Email For fatch:</label><br>
            <input type="text" id="femail" name="femail" value="femail"><br>
            <label for="lname">Email For Change:</label><br>
            <input type="text" id="cemail" name="cemail" value="cemail"><br><br>
            <input type="submit" value="Submit" id="move_tab">
        </form>



    </body>
</html>-->
<%-- 
    Document   : ImapPop_registration
    Created on : 2 Dec, 2017, 1:14:31 PM
    Author     : dhiren
--%>
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
<!-- begin:: Content -->

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
                <a href="" class="k-content__head-breadcrumb-link">Enable or Disable IMAP/POP</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="portlet light " id="form_wizard_1" style="display:block;">

                <div class="portlet-body form mt-5">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <form action="" id="da_onboarding_tab1" class="form_val">

                                    <div class="col-md-12 mt-4 mb-4">
                                        <div class="alert alert-secondary">
                                            <div class="col-md-12">
                                                <b>NOTE: Please read all instructions carefully.</b>
                                                <ol style="padding-left: 15px;margin-top: 15px;">
                                                    <li> I hereby take full responsibility and accountability for DA console and for all email accounts
                                                        created under this domain and respond to any queries by LEAs if any.</li>
                                                    <li> I will inform NIC in case of any change in delegated administrator.</li>
                                                    <li> I will ensure the authenticity of the applicant.</li>
<!--                                                    <li> I will create Consultants and support staff ids with the following domains respectively.<br>
                                                        a) @govcontractor.in<br>
                                                        b) @supportgov.in</li>-->
                                                    <li> I will ensure that all the ids have correct date of expiry set as per the user profile and
                                                        needful action will be taken on time. e.g. If the Govt officer wants to retain the name based
                                                        email-id post superannuation, needful action will be taken by move the email-id to retired
                                                        officers container. Similarly, contractual/support staff email ids will be deactivated/deleted
                                                        at the end of the tenure.</li>
                                                    <li> For organizations under paid accounts category, Delegated Administrator has to provide
                                                        the relevant documents to NIC regarding proof of payment made to NICSI.</li>
                                                    <li> Admin ID will be renewed every year. If user fails to do so, ID will be deactivated
                                                        automatically.</li>
                                                    <li> I agree to maintain the confidentiality, safekeeping and protection of confidential
                                                        information contained in all user list. </li>
                                                    <li><strong> You can be DA of your organization only. If you want to change the organization, kindly update your profile.</strong></li>
                                                </ol>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="col-md-12">
                                        <label class="control-label" for="street">Eligibility : <span style="color: red">*</span></label>
                                        <div id="dns_service">
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="eligibility" id="eligibility"  value="department" checked=""> Govt department/institutes/organization
                                                <!--<input type="radio" name="eligibility" id="eligibility"  value="department" checked=""> US and above-->
                                                <span></span>
                                            </label>&emsp;&emsp;
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="eligibility" id="eligibility"  value="psu" > PSU
                                                <!--<input type="radio" name="eligibility" id="eligibility"  value="psu" > DGM and above-->
                                                
                                                <span></span>
                                            </label>
                                        </div>
                                        <font style="color:red"><span id="protocol_err"></span></font>
                                        <font style="color:red"><span id="service_pop_err"></span></font>
                                        <font style="color:red"><span id="service_imap_err"></span></font>
                                    </div>   
                                    
                                    <div class="col-md-12 my-3">
                                        <div class="alert alert-secondary">
                                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                        <input type="checkbox" name="daon_mobile" id="daon_mobile"> I will ensure the mobile numbers of all users under this domain are updated.
                                                                                                                                                    Please note that by selecting this, the users under this domain will not be 
                                                                                                                                                    allowed to update their mobile numbers on their own and all requests will be 
                                                                                                                                                    sent to you ONLY.
                                                        <span></span>
                                                    </label>
                                            </div>
                                    </div>

                                    <div class="col-md-12">
                                        <div class="row">
                                            <div class="col-md-3 ">
                                                <label>VPN IP</label>                        
                                                <select class="form-control" id="vpn_reg_no" name="da_vpn_reg_no">
                                                    <option value="" selected="true">-SELECT VPN IP-</option>
                                                    <!--<option>VPN202566</option>-->
                                             </select>
                                                <input type="text" id="vpn_reg_no_text" name="da_vpn_reg_no" class="form-control d-none" placeholder="Enter VPN IP">
                                                <font style="color:red"><span id="vpn_error"> </span></font>
                                            </div>
                                            
                                               <div class="col-md-3 "></div>
      <!--                                      <div class="col-md-3" id="cert1_div">
                                                <label>BO Name <span style="color: red">*</span></label>
                                                <div class="custom-file">
                                                    <input class="form-control" value="" type="text" id="boName" disabled>
                                                    <b id="boName" ></b>
                                                    <font style="color:red"><span id="file_err"> </span></font>
                                                </div>
                                                <input type="hidden" id="cert" value="">
                                            </div> 
                                            -->
                                            
                                            
                                            <div class="col-md-3 "></div>
                                        </div>
                                    </div>





                                    <font style="color:red"><span id="request_err"></span></font>

                                    <div class="row">
                                        <div class="col-md-6 mt-4" style="text-align: right;">
                                            <br/><label class="control-label" for="street">Captcha</label>
                                            <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>" width="100" height="50" alt="captcha background" class="ft-lft captcha" />
                                            <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                        </div>
                                        <div class="col-md-2">
                                            <div class="form-group">
                                                <label class="control-label" for="street">Enter Captcha<span style="color: red">*</span></label>
                                                <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="imgtxt" maxlength="6" value=""> 
                                                <font style="color:red"><span id="captchaerror"></span></font>
                                            </div>
                                        </div>
                                    </div>
                                    <font style="color:red"><span id="request_err"></span></font>
                                    <div class="col-md-12 text-center mt-4">
                                        <!-- below line added by pr on 10thjan18 to implement CSRF  -->  
                                        <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                        <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                        <button name="submit"  value="preview" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                                    </div>
                                    <span id="response">

                                    </span>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="vpn_ip_not_exist" style="display:none;">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="portlet light " id="form_wizard_1" style="display:block;">

                <div class="portlet-body form mt-5">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <form action="" id="da_onboarding_tab1" class="form_val">
                                    <div class="col-md-6 ">
                                        <label>You do not have VPN IP, please read the instruction which is written below and apply for vpn </label>   <br/>
                                        <div class="col-md-12">
                                            <p class="red bold">a) I am a permanent Employee of the department/organization for which DA is applied</p>
                                        </div>
                                        <div class="col-md-12">
                                            <p class="red bold">b) Designation is Under secretary/Equivalent or Above.</p>
                                        </div>     
                                        <div class="row">
                                            <div class="col-md-6 text-center mt-4">
                                                <button class="btn btn-success sbold color-white" type="button" name="button_yes" class="btn purple btn-success sbold" > <a href="Vpn_registration?vpn_app_url=mailadmin">Yes</a> </button>                                                                
                                            </div>
                                            <div class="col-md-6 text-center mt-4">
                                                <button class="btn purple btn-danger sbold" type="button"> <a href="showUserData">No</a> </button>                                                                
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
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="bo_name_not_exist" style="display:none;">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="portlet light " id="form_wizard_1" style="display:block;">

                <div class="portlet-body form mt-5">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <form action="" id="da_onboarding_tab1" class="form_val">
                                    <div class="col-md-6 ">
                                        <label>You have no BO name, please apply for BO name through <a href="http://servicedesk.nic.in">Service Desk</a></label>                        
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="coord_emails_not_exist" style="display:none;">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <!-- BEGIN PAGE CONTENT INNER -->
            <div class="portlet light " id="form_wizard_1" style="display:block;">

                <div class="portlet-body form mt-5">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <form action="" id="da_onboarding_tab1" class="form_val">
                                    <div class="col-md-6 ">
                                        <label> coordinator email not found , please contact to <a href="http://servicedesk.nic.in">Service Desk</a></label>                        
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
<jsp:include page="include/new_include/footer.jsp" /> 

<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="da_onboarding_tab2" method="post">
        <jsp:include page="include/daonboarding_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>

<!-- Nested Modal-->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Terms and conditions</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">                           
                <b>NOTE: Please read all instructions carefully.</b>
                <br/>
                <ol>
                    <li>I hereby take full responsibility and accountability for DA console and for all email accounts created under this domain and respond to any queries by LEAs if any.</li>
                    <li>I will inform NIC in case of any change in delegated administrator.</li>
                    <li>I will ensure the authenticity of the applicant.</li>
                    <li>I will create Consultants and support staff ids with the following domains respectively.<br>
                        <b>a) @govcontractor.in</b><br>
                        <b>b) @supportgov.in</b></li>
                    <li>I will ensure that all the ids have correct date of expiry set as per the user profile and needful action will be taken on time. e.g. If the Govt officer wants to retain the name based email-id post superannuation, needful action will be taken by move the email-id to retired officers container. Similarly, contractual/support staff email ids will be deactivated/deleted at the end of the tenure.</li>
                    <li>For organizations under paid accounts category, Delegated Administrator has to provide the relevant documents to NIC regarding proof of payment made to NICSI.</li>
                    <li>Admin ID will be renewed every year. If user fails to do so, ID will be deactivated automatically.</li>
                    <li>I agree to maintain the confidentiality, safekeeping and protection of confidential information contained in all user list. </li>
                </ol>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="da_onboarding_form_confirm">
        <%
            if (!nic_employee) {%>
        <jsp:include page="include/Hod_detail.jsp" />
        <% } else {
        %>
        <jsp:include page="include/wifi_detail.jsp" />
        <%}%>
    </form>
</div>
<!-- end -->

<link href="assets/css/daonboarding.css" rel="stylesheet" type="text/css" />
<script src="main_js/onlineforms.js" type="text/javascript" async></script>
<script src="main_js/daOnboarding.js" type="text/javascript" async></script>
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
