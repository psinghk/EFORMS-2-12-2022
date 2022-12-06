<%@page import="com.org.bean.UserData"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    
  
    String random = entities.Random.csrf_random();
    session.setAttribute("rand", random);
    // below code added by pr on 22ndjan18
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
                <a href="" class="k-content__head-breadcrumb-link">Ldap Services</a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile">
            <div id="form_wizard_1">
                <div class="k-portlet__head">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 p-0">
                                <div class="k-portlet__head-label mt-4">
                                    <h3 class="k-portlet__head-title">LDAP Request Form</h3>
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
                <form action="" method="post" id="ldap_form1" class="form_val pl-3 pr-3">

                    <div class="col-md-12 mt-3">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="street">Name of the Application <span style="color: red">*</span></label>
                                <input class="form-control" value="" placeholder="Enter Name of the Applicaion [characters,dot(.) and whitespace]" type="text" name="app_name" id="app_name" maxlength="50"  aria-required="true">
                                <font style="color:red"><span id="app_name_err"></span></font>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="street">Application URL <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Application URL [e.g: (https://abc.com)] " type="text" name="app_url" id="app_url" value="" maxlength="100"  aria-required="true">                                                                                        
                                <font style="color:red"><span id="app_url_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="street"><strong>IP1</strong> from which you will access LDAP Server <span style="color: red">*</span> <a href="" id="mD2" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know Your IP<i class="entypo-help"></i>)</a></label>
                                <input class="form-control" placeholder="Enter Application IP1 [e.g: 10.10.10.10]" type="text" name="base_ip" id="base_ip" value="" maxlength="20"  aria-required="true">
                                <font style="color:red"><span id="base_ip_err"></span></font>
                            </div>
                            <div class="col-md-6 form-group">
                                <label for="street"><strong>IP2</strong> from which you will access LDAP Server</label>
                                <input class="form-control" placeholder="Enter Application IP2 [e.g: 10.10.10.10]" type="text" name="service_ip" id="service_ip" maxlength="20">
                                <font style="color:red"><span id="service_ip_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label for="street">Domain/Group Of People who will access this application<span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Only [Alphanumeric,dot(.),comma(,),hyphen(-),slash(/) and whitespace] allowed" type="text" name="domain" id="domain" maxlength="50">
                                <font style="color:red"><span id="domain_err"></span></font>
                            </div>
                            <div class="col-md-6 form-group">
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
                        </div>
                    </div>

                    <div class="col-md-12">
                        <div class="row form-group">
                            <div class="col-md-3">
                                <label for="street">Is the application enabled over https: <span style="color: red">*</span></label>
                                <div id="ldap_https">
                                    <div class="k-radio-inline">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="https" id="https_1"  value="yes" checked=""> Yes
                                            <span></span>
                                        </label>
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="https" id="https_2"  value="no" > No
                                            <span></span>
                                        </label>
                                    </div>

                                </div>
                                <font style="color:red"><span id="https_err"></span></font>
                            </div>
                            <div class="col-md-3">
                                <label for="street">Is the application security audit cleared: <span style="color: red">*</span></label>
                                <div id="ldap_audit">
                                    <div class="k-radio-inline">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="audit" id="audit_1"  value="yes" checked=""> Yes
                                            <span></span>
                                        </label>
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="audit" id="audit_2"  value="no" > No
                                            <span></span>
                                        </label>
                                    </div>
                                </div>
                                <font style="color:red"><span id="audit_err"></span></font>
                            </div>
                            <div class="col-md-6 display-hide" id="server_other">
                                <label for="street">Enter server location <span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                                <font style="color:red"><span id="server_txt_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12" id="cert1_div">
                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Application should have Security audit clearance certificate, Upload certificate in PDF format (less than 1mb) <span style="color: red">*</span></label>
                                <div></div>
                                <div class="custom-file">
                                    <input type="file" class="custom-file-input" name="cert_file" id="cert_file">
                                    <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                    <font style="color:red"><span id="file_err"> </span></font>
                                </div>
                                <input type="hidden" id="cert" value="">
                            </div>
                        </div>
                    </div>

                    <div class="col-md-12"><div id="note_div" class="row display-hide alert alert-info"></div> </div>
                    <div class="col-md-12 form-group">
                        <div class="row display-hide" id="no_audit_div">
                            <div class="col-md-6">
                                <label for="street">Enter Email id which will access ldap server<span style="color: red">*</span></label>
                                <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="ldap_id1" id="ldap_id1" value="" maxlength="50"  aria-required="true">
                                <font style="color:red"><span id="specific_id1_err"></span></font>
                            </div>
                            <div class="col-md-6">
                                <label for="street">Enter Email id which will access ldap server</label>
                                <input class="form-control" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="ldap_id2" id="ldap_id2" maxlength="50">
                                <font style="color:red"><span id="specific_id2_err"></span></font>
                            </div>
                        </div>
                    </div>
                    <div id="submit_div">
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
                        <div class="row">
                            <div class="col-md-12 text-center mt-5 mb-5" >
                                <input type="hidden" id="cert" value="false" />
                                <!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
                                <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                                <!--<input type="hidden" name="CSRFRandom" value="1" id="CSRFRandom" />-->
                                <button name="submit" value="preview" class="btn purple btn-primary sbold" > Preview and Submit </button>                                                                
                            </div>                                                                                                                                                       
                        </div>
                    </div>
                </form>
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
                <div class="col-md-12 mt-5">
                    <p class="mb-4"><b>Step 1:</b> ALL TESTING MUST BE DONE FROM SERVER FROM WHICH LDAP AUTHENTICATION SERVICE NEEDS TO BE INTEGRATED.</p>
                    <p><b>Step 2:</b> Check the domain look up using nslookup command. If domain (auths.nic.in) does not get resolved,
                        then please contact your server/firewall administrator. Refer below screenshot for using nslookup command.</p>
                    <img  src="assets/img/ldap-nslookup.png" class="img-responsive mb-5" alt="ldap-nslookup" />
                    <p><b>Step 3:</b> Telnet auths.nic.in (164.100.14.58) on port 636. Go to command line interface (CLI) and type
                        telnet auths.nic.in 636 and press enter. If telnet works then connection to LDAP server is OK.</p>
                    <img class="img-responsive mb-5" src="assets/img/ldap-yes.png" alt="ldap-telnet-yes" />
                    <p><b>Step 4:</b>If telnet does not work, then there is connection issue. Refer below screenshot.</p>
                    <img class="img-responsive mb-5" src="assets/img/ldap-telnet-no.png"  alt="ldap-telnet-no" />
                    <div class="alert alert-info"><strong>Disclaimer :</strong>  NIC will not be responsible if the testing is not done from the host machine at which application is hosted.</div>
                </div>
            </div>
            <div class="portlet light " id="form_wizard_3" style="display:none;">
                <div class="portlet-body form">
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
                    <div class="col-md-12 mt-3">
                        <p>
                            <b>Step 1: </b>
                            One possible reason for telnet failure is that your
                            application server is behind a firewall
                            and firewall rule is not configured to allow traffic
                            from your server to LDAP Authentication Sever.
                        </p>
                        <p>
                            <b>Step 2: </b>
                            In this regard you have to apply for firewall rule
                            through <a href="https://farps.nic.in">https://farps.nic.in</a>.
                        <p>
                            * If both private IP and Public IP are configured
                            on your application server, then before filling up
                            the following firewall form, please confirm from
                            your network/firewall
                            administrator that the auth request from your
                            application will reach LDAP server from which IP
                            (PUBLIC or PRIVATE). </p>
                        <br />
                        <table style="width:60%; margin: 0 auto;" border="1">
                            <tr>
                                <td style="padding:5px;">Are you the Server/
                                    Website/ Application Owner ?</td>
                                <td style="padding:5px;">NO</td>
                            </tr>
                            <tr>
                                <td style="padding:5px;">Data Centre where
                                    server hosted</td>
                                <td style="padding:5px;">NIC HQ DATA CENTER
                                </td>
                            </tr>
                            <tr>
                                <td style="padding:5px;">Functionality of the
                                    Server</td>
                                <td style="padding:5px;">LDAP Server </td>
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
                                <td style="padding:5px;">164.100.14.58 </td>
                            </tr>
                            <tr>
                                <td style="padding:5px;">URL of the application
                                    hosted on above IP (if any)</td>
                                <td style="padding:5px;"></td>
                            </tr>
                        </table>
                        <br />
                        <label for="about"><b>Public IP
                                rule for access from Outside Systems to Server
                            </b></label></br>
                        <table style="width:100%; margin: 0 auto;" border="1">
                            <tr>
                                <td style="padding:5px;"><b>From</b></td>
                                <td style="padding:5px;"><b>TO</b></td>
                                <td style="padding:5px;"><b>Services</b></td>
                                <td style="padding:5px;"><b>URL of the server
                                        to be visited</b></td>
                                <td style="padding:5px;"><b>Ports</b></td>
                                <td style="padding:5px;"><b>Reason for opening
                                        port</b></td>
                                <td style="padding:5px;"><b>Protocol</b></td>
                            </tr>
                            <tr>
                                <td style="padding:5px;">YOUR APPLICATION
                                    SERVER IP</td>
                                <td style="padding:5px;">164.100.14.58</td>
                                <td style="padding:5px;">LDAPS</td>
                                <td style="padding:5px;"></td>
                                <td style="padding:5px;">636</td>
                                <td style="padding:5px;">Authentication</td>
                                <td style="padding:5px;">TCP</td>
                            </tr>
                        </table>
                        <br />
                        <p>
                            <b>Step 3: </b>
                            For any issue related to firewall, NIC Cyber
                            Security team should be contacted on 011-24305140
                            (Email on : appsecmon2@nic.in, appsecdev3@nic.in ,
                            aniljha@nic.in).</p>
                        </p>
                        <p>
                            <b>Step 4: </b>
                            After you have applied for the firewall rule to allow
                            traffic from your server to LDAP Authentication Server,
                            an auto generated request ID will be generated.
                        </p>
                        <p>
                            <b>If you have the request ID, please enter the request
                                ID in the space provided below. And click on submit
                                button to proceed. Your form will be submitted
                                successfully.</b>
                        </p>
                    </div>
                </div>
            </div>
        </div>


    </div>
    <!-- END PAGE CONTENT BODY -->
    <!-- END CONTENT BODY -->
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
<jsp:include page="include/new_include/footer.jsp" />

<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="ldap_form2" method="post">
        <jsp:include page="include/ldap_preview.jsp" />
    </form>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->
<div class="modal fade" id="myModal1"  tabindex="-1">
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
                <ul class="term_ul">
                    <li>Concerned Department/ Ministry shall be solely responsible for all the information, contents, data send and received using NIC LDAP Authentication under this Agreement. Concerned Department/ Ministry further acknowledges that it shall be solely responsible and undertake to maintain complete authenticity of the information/data sent and/or received and takes all possible steps and measures to ensure that consistent authentic information is transmitted.</li>
                    <li>Concerned Department/ Ministry shall keep the account information such as userid, password provided obtained for LDAP Authentication in safe custody to avoid any misuse by unauthorised users.</li>
                    <li>I will take one id per application. ID assigned for authentication with one application will not be used by another application. I understand the risks involved . I hereby authorize NIC support cell to deactivate the id in case of misuse/abuse.</li>
                    <li>NIC does not capture any aadhaar related information.</li>
                </ul>
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
    <form id="ldap_form_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>
<!-- end -->
<script src="main_js/onlineforms.js" type="text/javascript" async></script>
<script src="main_js/ldap.js" type="text/javascript" async></script>
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
        if ('<%=session.getAttribute("resend_request")%>' == "true") {
            var form_name = '<s:property value="#session['prvwdetails'].form_name" />';
            if (form_name == "LDAP")
            {
                var app_name = '<s:property value="#session['prvwdetails'].prvw_appname" />';
                var app_url = '<s:property value="#session['prvwdetails'].prvw_appurl" />';
                var domain = '<s:property value="#session['prvwdetails'].prvw_domain" />';
                var base_ip = '<s:property value="#session['prvwdetails'].prvw_baseip" />';
                var service_ip = '<s:property value="#session['prvwdetails'].prvw_serviceip" />';
                var server_loc = '<s:property value="#session['prvwdetails'].prvw_server_loc" />';
                var server_loc_other = '<s:property value="#session['prvwdetails'].prvw_server_loc_other" />';
                var https = '<s:property value="#session['prvwdetails'].prvw_https" />';
                var audit = '<s:property value="#session['prvwdetails'].prvw_audit" />';
                var ldap_id1 = '<s:property value="#session['prvwdetails'].prvw_ldap_id1" />';
                var ldap_id2 = '<s:property value="#session['prvwdetails'].prvw_ldap_id2" />';
                var uploaded_filename = '<s:property value="#session['prvwdetails'].prvw_uploaded_filename" />';
                $('#app_name').val(app_name);
                $('#app_url').val(app_url);
                $('#domain').val(domain);
                $('#base_ip').val(base_ip);
                $('#service_ip').val(service_ip);
                $('#server_loc_txt').val(server_loc);
                if (https === 'yes')
                {
                    $('#https_1').prop('checked', true);
                } else {
                    $('#https_2').prop('checked', true);
                }
                if (audit === 'yes') {
                    $("#audit_1").prop('checked', true);
                    $('#cert1_div').removeClass('display-hide');
                    $('#no_audit_div').addClass('display-hide');
                    $('#uploaded_filename').text(uploaded_filename);
                } else {
                    $("#audit_2").prop('checked', true);
                    $('#cert1_div').addClass('display-hide');
                    $('#no_audit_div').removeClass('display-hide');
                    $('#ldap_id1').val(ldap_id1);
                    $('#ldap_id2').val(ldap_id2);
                }
            }
        }

    });
</script>