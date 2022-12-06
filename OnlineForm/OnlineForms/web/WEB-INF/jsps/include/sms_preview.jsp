
<%@page import="com.org.bean.UserData"%>
<%@page import="com.org.bean.ImportantData"%>
<%@page import="java.util.ArrayList"%>
<div class="modal-dialog modal-lg single-page">
    <div class="modal-content" style="">
        <div class="modal-header">
            <h4 class="modal-title">Preview</h4>
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
        </div>
        <div class="modal-body"  id="sms_preview_tab">
            <div class="portlet light ">
                <span class="alert alert-info">SMS Service Registration Form</span>
            </div>
            <div class="portlet-body form">
                <div id="profile_div">
                    <jsp:include page="profile_preview.jsp" />
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Application Details</h3>
                </div>
                <div class="row page-wrapper form-group">
                    <div class="col-md-12 mb-5">
                        <label for="street">SMS Services , Please select appropriate <span style="color: red">*</span></label>
                        <div id="core_service">
                            <div class="row">
                                <div class="col-md-2">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="sms_service" id="sms_serv1"  value="push"> PUSH
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-2">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="sms_service" id="sms_serv2"  value="pull" > PULL
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-1">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="sms_service" id="sms_serv3"  value="obd" > OBD
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-2">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="sms_service" id="sms_serv4"  value="missedcall" > Missed Call
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-2">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="sms_service" id="sms_serv5"  value="otp"> OTP Service
                                        <span></span>
                                    </label>
                                </div>
                                <div class="col-md-3">
                                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                        <input type="checkbox" name="sms_service" id="sms_serv6"  value="quicksms"> QuickSMS Service
                                        <span></span>
                                    </label>
                                </div>
                            </div>
                        </div>
                        <font style="color:red"><span id="sms_serv_err"></span></font>
                    </div>
                    <div class="col-md-12">
                        <div class="alert alert-secondary" id="smstext">
                            <div class="col-md-12">
                                <ul style="padding-left: 0;">
                                    <li><b>PUSH:</b> To send SMS from application to mobile using API(A2M)</li>
                                    <li><b>PULL:</b> To send SMS from mobile to application using API(M2A)</li>
                                    <li><b>OBD: </b> To send phone call (voice message) from application to subscriber</li>
                                    <li><b>MISSED CALL: </b> Allows missed call on a predefined number to subscribe or avail a service</li>
                                    <li><b>OTP SERVICE: </b> High priority SMS sent through application usnig SMS API</li>
                                    <li><b>QUICK SMS: </b> NIC SMS web console for sending SMS</li>
                                </ul>
                            </div>
                        </div>                                                                                                                               
                    </div>
                </div>
                <div class="row form-group" style="display:none" id="show_pull_url">
                    <div class="col-md-6">
                        <label for="street">If PULL, Mention URL path <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter URL (e.g: https://abc.com)" type="text" name="pull_url" id="pull_url"  value="" maxlength="100">
                        <font style="color:red"><span id="pull_url_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Keyword<span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Keyword [Alphanumeric,length 1 to 15 digits]" type="text" name="pull_keyword" id="pull_keyword"  value="" maxlength="15">
                        <font style="color:red"><span id="pull_keyword_err"></span></font>
                    </div>
                </div>
                <div class="row form-group" style="display:none" id="show_note">
                    <div class="col-md-6">
                        <div>
                            <label for="street">Do you have a short code?</label>
                        </div>
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="s_code" id="sy_code" value="y"> Yes
                            <span></span>
                        </label>&emsp;&emsp;
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="s_code" id="sn_code" value="n" checked=""> No
                            <span></span>
                        </label>&emsp;&emsp;
                        <label class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="s_code" id="sb_code" value="b"> Both
                            <span></span>
                        </label>
                        <font style="color:red"><span id="snote_flag_err"></span></font>
                    </div>
                    <div class="col-md-6" id="short_code_input" style="display:none">
                        <label for="street">Short code<span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter short code [Only Numeric allowed, length 3 to 10]" type="text" name="short_code" id="short_code" value="" maxlength="10">
                        <font style="color:red"><span id="short_note_err"></span></font>
                    </div>
                </div>
                <div class="alert alert-secondary" id="show_note_text">
                    <ul style="padding-left: 0;">
                        <li><b>SHORT CODE</b>: Premium caller ID give by DOT (Department of Telecommunication)</li>
                        <li>In case of NO, NIC will provide you 10 digit Virtual Mobile Number(VMN).</li>
                        <li><b>VMN</b>: Virtual Mobile Number is a 10 digit pre-defined mobile number used in PULL SMS Service.</li>
                    </ul>
                </div>
                <div id="quick_hide">
                    <div class="row form-group">
                        <div class="col-md-6">
                            <label for="street">Name of the Application <span style="color: red">*</span></label>
                            <input class="form-control" value="" placeholder="Enter Name of the Applicaion [characters,dot(.) and whitespace]" type="text" name="app_name" id="app_name" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="app_name_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street">Application URL <span style="color: red">*</span></label>
                            <input class="form-control" placeholder="Enter Application URL [e.g: (https://abc.com)]" type="text" name="app_url" id="app_url" value="" maxlength="100"  aria-required="true">                            
                            <font style="color:red"><span id="app_url_err"></span></font>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Purpose of the application </label>
                            <input type="text" class="form-control" placeholder="Enter Purpose of the application [characters,dot(.) and whitespace]" name="sms_usage" value="" id="sms_usage" maxlength="100"  aria-required="true">
                            <font style="color:red"><span id="sms_usage_err"></span></font>
                        </div>
                        <div class="col-md-4">
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
                        <div class="col-md-4" id="server_other" style="display:none">
                            <label for="">Other Location</label>
                            <input class="form-control" placeholder="Enter Server Location Alphanumeric,whitespace and [. , - # / ( ) ]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                            <font style="color:red"><span id="server_txt_err"></span></font>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <div class="col-md-6">
                            <label for="street" id="msgip"><strong>IP1</strong> from which you will access SMS Gateway<span style="color: red">*</span> <a href="" data-toggle="modal" data-target="#myModal1" style="color:blue">(Know Your IP<i class="entypo-help"></i>)</a></label>
                            <input class="form-control" placeholder="Enter Application IP1 [e.g: 10.10.10.10]" type="text" name="base_ip" id="base_ip" value="" maxlength="20"  aria-required="true">
                            <font style="color:red"><span id="base_ip_err"></span></font>
                        </div>
                        <div class="col-md-6">
                            <label for="street" id="msgip2"><strong>IP2</strong> from which you will access SMS Gateway</label>
                            <input class="form-control" placeholder="Enter Application IP2 [e.g: 10.10.10.10]" type="text" name="service_ip" id="service_ip" maxlength="20">
                            <font style="color:red"><span id="service_ip_err"></span></font>
                        </div>
                    </div>
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Contact Details of Technical Admin</h3>
                </div>
                <div class="row">
                    <div class="col-md-4">
                        <label for="street">Name of The Technical Admin  <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Name of The Admin  [Only characters,dot(.) and whitespace allowed]" type="text" name="t_off_name" id="auth_off_name"  value="" maxlength="100">
                        <font style="color:red"><span id="tauth_off_name_err"></span></font>
                    </div>
                    <div class="col-md-4">
                        <label for="street">Designation <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Designation [characters,digits,whitespace,(.),(,),(-),(_),(&)]" type="text" name="tdesignation" id="designation"  value="" maxlength="100">
                        <font style="color:red"><span id="tdesignation_err"></span></font>
                    </div>
                    <div class="col-md-4">
                        <label for="street">Admin Employee Code </label>
                        <input class="form-control" placeholder="Enter Admin Employee Code [Only characters and digits allowed]" type="text" name="temp_code" id="emp_code"  value="" maxlength="12">
                        <font style="color:red"><span id="temp_code_err"></span></font>
                    </div>
                </div>
                
                <label for="street" class="mt-3 mb-3"><b>Office Address: </b></label>
                <div class="row form-group">
                    <div class="col-md-12">
                        <label for="street">Postal Address <span style="color: red">*</span></label>
                        <input class="form-control " placeholder="Enter Postal Address [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),underscore(_) allowed]" type="text" name="taddrs" id="addrs"  maxlength="50"  value="" aria-required="true">
                        <font style="color:red"><span id="tadd1error"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-4">
                        <label >State where you are posted <span style="color: red">*</span></label>
                        <%
                            UserData userdata = (UserData) session.getAttribute("uservalues");
                            ImportantData impdata = (ImportantData) userdata.getImpData();
                            ArrayList state_name = (ArrayList) impdata.getStates();
                            // ArrayList state_name = (ArrayList) session.getAttribute("state_name");
                            String statename = "";
                        %>
                        <select id="tstate" name="tstate" theme="simple" class="form-control">
                            <%            for (int i = 0; i < state_name.size(); i++) {
                                    statename = state_name.get(i).toString();
                            %>
                            <option value="<%=statename%>"><%=statename%></option>
                            <%}
                            %>
                        </select>
                        <font style="color:red"><span id="tstate_err"></span></font>
                    </div>
                    <div class="col-md-4">
                        <label >District Name <span style="color: red">*</span></label>
                        <select class='form-control' name='tcity' id='tcity'>
                            <option value=''>-SELECT-</option>
                        </select>   
                        <font style="color:red"><span id="tcity_err"></span></font>                                                                                        
                    </div>   
                    <div class="col-md-4">
                        <label for="street">Pin Code <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Pin Code [Only digits(6) allowed]" type="text" name="tpin" id="pin"  maxlength="6"  value="" aria-required="true">
                        <font style="color:red"><span id="tpin_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Telephone Number :(O)<span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]" type="text" name="ttel_ofc" id="tel_ofc"  maxlength="20"  value="" aria-required="true">
                        <font style="color:red"><span id="ttel_ofc_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Telephone Number :(R)<span style="color: red"></span></label>
                        <input class="form-control" placeholder="Enter STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]" type="text" name="ttel_res" id="tel_res"  maxlength="20" value="">
                        <font style="color:red"><span id="ttel_res_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Mobile <span style="color: red">*</span></label>
                        <!-- below line updated by preeti on 9th aug 16  -->
                        <input class="form-control" placeholder="Enter Mobile [e.g: +919999999999]" type="text" name="tmobile" id="mobile"  value="" maxlength="15">
                        <font style="color:red"><span id="tmobile_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">E-mail Address <span style="color: red">*</span></label>
                        <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="tauth_email" id="auth_email"  value="" maxlength="50">
                        <font style="color:red"><span id="tauth_email_err"></span></font>
                    </div>
                    <span id="nicemp"></span>
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Contact Details of Billing Owner</h3>
                </div>
                <div class="row form-group">
                    <div class="col-md-4">
                        <label for="street">Name of The Owner  <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Name of The Owner [characters,dot(.) and whitespace]" type="text" name="bauth_off_name" id="auth_off_name" value="" maxlength="100">
                        <font style="color:red"><span id="bauth_off_name_err"></span></font>
                    </div>
                    <div class="col-md-4">
                        <label for="street">Designation <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Designation [characters,digits,whitespace,(.),(,),(-),(_),(&)]" type="text" name="bdesignation" id="designation" value="" maxlength="100">
                        <font style="color:red"><span id="bdesignation_err"></span></font>
                    </div>
                    <div class="col-md-4">
                        <label for="street">Owner Emp Code </label>
                        <input class="form-control" placeholder="Enter Admin Employee Code [Only characters and digits allowed]" type="text" name="bemp_code" id="emp_code" value="" maxlength="12">
                        <font style="color:red"><span id="bemp_code_err"></span></font>
                    </div>
                </div>
                
                <div class="row">
                    <div class="col-md-12">
                        <label for="street"><b>Office Address: </b></label>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-12">
                        <label for="street">Postal Address <span style="color: red">*</span></label>
                        <input class="form-control " placeholder="Enter Postal Address [Only characters,digits,whitespace,dot(.),comma(,),hypen(-),underscore(_) allowed]" type="text" name="baddrs" id="addrs" maxlength="50"  value="" aria-required="true">
                        <font style="color:red"><span id="tadd1error"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-4">
                        <label >State where you are posted <span style="color: red">*</span></label>
                        <%
                           
                            ArrayList state_name1 = (ArrayList) impdata.getStates();
                
                            String statename1 = "";
                            String statecode1 = "";
                        %>
                        <select id="bstate" name="bstate" theme="simple" class="form-control">
                            <option value="" selected>select</option>
                            <%            for (int i = 0; i < state_name1.size(); i++) {
                                    statename1 = state_name1.get(i).toString();
                            %>
                            <option value="<%=statename1%>"><%=statename1%></option>
                            <%}
                            %>
                        </select>
                        <font style="color:red"><span id="bstate_err"></span></font>
                    </div>
                    <div class="col-md-4">
                        <label >District Name <span style="color: red">*</span></label>
                        <select class='form-control' name='bcity' id='bcity'>
                            <option value=''>-SELECT-</option>
                        </select>   
                        <font style="color:red"><span id="bcity_err"></span></font>                                                                                        
                    </div>   
                    <div class="col-md-4">
                        <label for="street">Pin Code <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Pin Code [Only digits(6) allowed]" type="text" name="bpin" id="pin" maxlength="6"  value="" aria-required="true">
                        <font style="color:red"><span id="bpin_err"></span></font>
                    </div>
                </div>
                
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Telephone Number :(O)<span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]" type="text" name="btel_ofc" id="tel_ofc" maxlength="20" value="" aria-required="true">
                        <font style="color:red"><span id="btel_ofc_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">Telephone Number :(R)<span style="color: red"></span></label>
                        <input class="form-control" placeholder="Enter STD CODE[3-5 DIGIT]-TELEPHONE[8-15 DIGIT]" type="text" name="btel_res" id="tel_res" maxlength="20" value="">
                        <font style="color:red"><span id="btel_res_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-6">
                        <label for="street">Mobile <span style="color: red">*</span></label>
                        <input class="form-control" placeholder="Enter Mobile  [e.g: 9999999999 or +919999999999]" type="text" name="bmobile" id="mobile" value="" maxlength="15">
                        <font style="color:red"><span id="bmobile_err"></span></font>
                    </div>
                    <div class="col-md-6">
                        <label for="street">E-mail Address <span style="color: red">*</span></label>
                        <input class="form-control" style="text-transform:lowercase;" placeholder="Enter Email Address [e.g: abc.xyz@zxc.com]" type="text" name="bauth_email" id="auth_email" value="" maxlength="50">
                        <font style="color:red"><span id="bauth_email_err"></span></font>
                    </div>
                    <span id="nicemp"></span>
                </div>
                <div class="k-portlet__head">
                    <h3 class="theme-heading-h3-popup">Other Details</h3>
                </div>
                <div class="row" id="audit_div">
                    <div class="col-md-6">
                        <label for="street">Is the application security audit cleared <span style="color: red">**</span></label>
                        <div class="pt-2">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" checked="" name="audit" id="audit_y"  value="Yes"> Yes
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="audit" id="audit_n"  value="No"> No
                                <span></span>
                            </label>&emsp;&emsp;
                            <font style="color: red;"><span id="audit_err"></span></font>
                        </div>
                    </div>
                    <div class="col-md-6" id="audit_date_div" style="display:none;">
                        <label for="street">#If not cleared by audit, give date by when it will be cleared <span style="color: red">*</span></label>
                        <div class="input-icon right"><i class="icon-calendar"></i><input class="form-control date-picker" type="text" id="datepicker2" name="datepicker1" placeholder="Date"  readonly/> </div>
                        <font style="color:red"><span id="audit_date_err1"></span></font>
                    </div>
                </div>
                <div class="row form-group" id="audit_ip_div">
                    <div class="col-md-6">
                        <label for="street">Mention IP of Staging Server Required for Testing</label>
                        <input class="form-control" placeholder="Enter IP of Staging Server [e.g: 10.10.10.10]" type="text" name="staging_ip" id="staging_ip" value="">
                        <font style="color:red"><span id="staging_ip_err"></span></font>
                    </div>
                </div>
                <div class="row form-group">
                    <div class="col-md-12">
                        <label for="street"><b>Monthly Expected SMS traffic: </b></label>
                    </div>
                </div>
                <div class="row" id="senderid_div">
                    <div class="col-md-6">
                        <label for="street">Do you have TRAI exempted Sender Id?<span style="color: red">*</span>
                            <!--                            <a href="" data-toggle="modal" data-target="#myModal" style="color:blue">(Know More<i class="entypo-help"></i>) </a> </label>-->
                        <div class="pt-3">
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" name="sender" id="sender_y"   value="Yes"> Yes
                                <span></span>
                            </label>&emsp;&emsp;
                            <label class="k-radio k-radio--bold k-radio--brand">
                                <input type="radio" checked="" name="sender" id="sender_n"  value="No"> No
                                <span></span>
                            </label>&emsp;&emsp;
                            <input type="hidden" value="34" id="sms-exempted">
                            <input type="hidden" value="72" id="sms-nexempted">
                            <input type="hidden" value="4" id="inter-exempted">
                            <font style="color:red"><span id="ex_err"></span></font>
                        </div>
                    </div>
                    <div class="col-md-6" id="sender_div" style="display:none;" >
                        <label for="street">Sender ID</label>
                        <input class="form-control" placeholder="Enter Sender ID [if character then it will be[length 6]only,if numeric digits will be[length min:4, max:10]" type="text" name="sender_id" id="sender_id" value="" maxlength="7">
                        <font style="color:red"><span id="sender_id_err"></span></font>
                    </div>
                </div>
                <div class="modal fade" id="myModal" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">?</button>
                                <h4 class="modal-title"><b>TRAI exempted Sender ID</b></h4>
                            </div>
                            <div class="modal-body">
                                <p>Sender ID is the ID with which SMS is received on your mobile phone.</p><p>For Example: NICSMS</p>
                                <p>To integrate your application with NIC SMS gateway, you need to have a Sender ID. As per TRAI, Sender ID should be exactly 6 character length string [all alphabets].</p>
                                <p>There are 2 types of Sender IDs from commercial point of view</p>
                                <ul><li><b>TRAI Exempted Sender ID:</b> TRAI grants exemption of 5 paisa/SMS on such Sender ID. For this, you need to approach TRAI with your request at the address mentioned below.</li>
                                    <li><b>TRAI Non-Exempted Sender ID:</b> There is no exemption on these Sender IDs. You can choose any valid Sender ID as per your preference and the same shall be configured on NIC SMS gateway.</li></ul>
                                <p><b>Address of TRAI:</b><br>Advisor, QoS<br>Mahanager Doorsanchar Bhawan (next to Zakir Hussain College)<br>Jawaharlal Nehru Marg New Delhi 110 002<br> Phone number: 23230404 <br> Email: adviqos@trai.gov.in<br> Website: trai.gov.in</p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <label for="street">Projected Domestic Monthly SMS traffic</label>
                        <input class="form-control" placeholder="Enter Domestic Traffic [Only digits allowed (minimum 1000)]" type="text" name="domestic_traf" id="domestic_traf" value="" maxlength="9"> 
                        <font style="color:red"><span id="domestic_traf_err22"></span></font>
                        <font style="color:blue;font-weight: bold"><span id="domestic_traf_yes"></span></font>                        
                    </div>
                    <div class="col-md-6">
                        <label for="street">Projected International SMS traffic</label>
                        <input class="form-control" placeholder="Enter International Traffic [Only digits allowed (minimum 1000)]" type="text" name="inter_traf" id="inter_traf" value="" maxlength="9">
                        <font style="color:red"><span id="inter_traf_err"></span></font>
                        <font style="color:blue; font-weight: bold"><span id="inter_traf_yes"></span></font>                        
                    </div>
                </div>
                <!--  below div added by pr on 18thjul18  -->
                <div class="row emailcheck final_id_span_cls display-hide"  style="padding:10px;">
                    <div class="col-md-12">
                        <label for="street"><strong>ID Assigned : </strong></label>
                        <span class="final_id_cls"></span>
                    </div>
                </div>
                <div class="form-group mt-4" id='tnc_div'>
                    <label class="k-checkbox k-checkbox--bold k-checkbox--brand mt-5">
                        <input type="checkbox"  name="tnc" id="tnc"> <b>I agree to <a data-toggle="modal" href="#stack2" style="color:blue">Terms and Conditions</a></b>
                        <span></span>
                    </label>
                    <font style="color:red"><span id="tnc_error"></span></font>
                </div>
            </div>
        </div>
        <div class="modal-footer">
            <a href="javascript:void(0);" class="btn btn-warning edit " >Edit</a>
            <button type="button" class="btn red btn-success save-changes" id="confirm">Submit</button>
            <button type="button" class="btn dark btn-default" data-dismiss="modal" id="closebtn">Close</button>
        </div> 
    </div>
</div>