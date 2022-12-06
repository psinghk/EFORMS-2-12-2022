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
                <div class="portlet-title">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">Form Details - Step 1 of 2</h3>
                        </div>
                    </div>
                </div>
                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab2" >
                                    <form action="" method="post" id="imappop_form1" class="form_val">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">IMAP POP Update</h4>
                                        </div>
                                        <div class="col-md-12 mt-4 mb-4">
                                            <div class="alert alert-secondary">
                                                <div class="col-md-12">
                                                    <b>NOTE: Please read all instructions carefully.</b>
                                                    <ul style="padding-left: 15px;margin-top: 15px;">
                                                        <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                                                        <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                                                        <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                                                        <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>
                                                        <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                                                        <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.
                                                            Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:
                                                            Trash - 7 days
                                                            ProbablySpam – 7 days</li>
                                                        <li>NIC account will be deactivated, if not used for 90 days.</li>
                                                        <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>
                                                        <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                                                        <li>Please note that advance payment is a must for paid users.</li>
                                                        <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>
                                                        <li>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</li>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-12">
                                            <label class="control-label" for="street">Please check the Protocol to be enabled : <span style="color: red">*</span></label>
                                            <div id="dns_service">
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" name="protocol" id="protocol_imap"  value="imap" checked=""> IMAP
                                                    <span></span>
                                                </label>&emsp;&emsp;
                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                    <input type="radio" name="protocol" id="protocol_pop"   value="pop" > POP
                                                    <span></span>
                                                </label>
                                            </div>
                                            <font style="color:red"><span id="protocol_err"></span></font>
                                            <font style="color:red"><span id="service_pop_err"></span></font>
                                            <font style="color:red"><span id="service_imap_err"></span></font>
                                        </div>
                                        <div class="row" style="padding:10px;">
                                            <div class="col-md-offset-11 col-md-1">
                                            </div>
                                        </div>
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
<!-- END PAGE CONTENT INNER -->
<jsp:include page="include/new_include/footer.jsp" />
<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="imappop_form2" method="post">
        <jsp:include page="include/imappop_preview.jsp" />
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
                <b>NOTE: Please read all instructions carefully.</b>
                <br/>
                <ol>
                    <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                    <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                    <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                    <li>NIC is not responsible for the contents that are being sent as part of the mail. The views expressed are solely that of the originator.</li>
                    <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                    <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.
                        Individuals are responsible for saving email messages as they deem appropriate. Messages will be automatically purged from folders as follows:
                        Trash - 7 days
                        ProbablySpam – 7 days</li>
                    <li>NIC account will be deactivated, if not used for 90 days.</li>
                    <li>Email id will be deleted after a period of 9 months from the date of deactivation if no request for activation is received.</li>
                    <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                    <li>Please note that advance payment is a must for paid users.</li>
                    <li>NIC coordinator reserves the right to ask for supporting documents like copy of identify card or any other document deemed appropriate to confirm the credentials of the applicant.</li>
                    <li>NIC will not share the details of Email Accounts and Email Addresses with anyone unless authorized by Reporting/Nodal/Forwarding Officer of the Department.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
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
    <form id="imappop_form_confirm">
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


<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/imappop.js" type="text/javascript"></script>
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
        
        if ('<%=ldap_employee%>' !== null) {
            if ('<%=ldap_employee%>' === 'true') {
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
            if (form_name == "IMAPPOP")
            {
                var protocol = '<s:property value="#session['prvwdetails'].protocol" />';
                if (protocol === 'imap')
                {
                    $('#imappop_form1 #protocol_imap').prop('checked', true);
                } else
                {
                    //$('#protocol_pop_hidden').val(jsonvalue.protocol)
                    $('#imappop_form1 #protocol_pop').prop('checked', true);
                }
            }
        }

    });
</script>