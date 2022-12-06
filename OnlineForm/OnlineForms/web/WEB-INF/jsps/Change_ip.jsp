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
                <a href="" class="k-content__head-breadcrumb-link">Add/Change an IP for other services</a>
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">
        <div class="k-portlet k-portlet--mobile" style="height: 100%;">
            <div id="form_wizard_1">
                <div class="k-portlet__head">
                    <div class="k-portlet__head-label">
                        <h3 class="k-portlet__head-title">Form IP - Add/Change Request Form</h3>
                    </div>
                </div>

                <div class="k-portlet__head">
                    <div class="k-portlet__head-label">
                        <div class="btn btn-info btn-success"> 
                            <a href="Sms_Registration"  style="color: #fff; font-size: 16;">
                                Go back to SMS Service
                            </a>
                        </div>
                    </div>
                </div>

                <form action="" method="post" id="ip_form1"  >

                    <div class="tab-pane active" id="tab1" >
                        <div class="row mt-5" >
                            <div class="col-md-12 proceed-div">                                                                                                                                                                                                
                                <div class="row pr-4 pl-4">
                                    <div class="col-md-2">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="single" name="ip_single_form" checked> Add IP
                                            <span></span>
                                        </label>
                                    </div>
                                    <div class="col-md-2">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="bulk" name="ip_single_form"> Change IP
                                            <span></span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>   
                    </div> 

                </form>
                <form action="" method="post" id="change_ip_form2"  >
                    <div id="add_ip_div">
                        <div class="col-md-12 mt-4">
                            <div class="alert alert-secondary">
                                <div class="col-md-12">
                                    <b>NOTE:</b><br>
                                    <ul style="margin-top: 15px;padding-left: 15px;">
                                        <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>
                                        <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                                        <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                                        <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                                        <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                                        <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                                        <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                                    </ul>
                                </div>
                            </div>                                                                   
                        </div>
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="street">Service request for <span style="color: red">*</span><br/><br/></label>
                                <div class="col-sm-offset-1">
                                    <!--                                                                        <div class="radio" style="display: inline-block;">
                                                                                                                <label class="k-radio k-radio--bold k-radio--brand">
                                                                                                                    <input type="radio" name="req_for" id="req_for_ldapauth"  value="ldap" />LDAP AUTH
                                                                                                                    <span></span>
                                                                                                                </label>&emsp;&emsp;
                                                                                                            </div>-->
                                    <!--                                    <div class="radio" style="display: inline-block;">
                                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                                <input type="radio" name="req_for" id="req_for_relay"  value="relay"  />RELAY
                                                                                <span></span>
                                                                            </label>&emsp;&emsp;
                                                                        </div>-->
                                    <div class="radio" style="display: inline-block;">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="req_for" id="req_for_sms"  value="sms" checked autofocus />SMS
                                            <span></span>
                                        </label>
                                    </div>
                                    <font style="color:red"><span id="show_req_for"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-3" id="ldap_account">
                                    <div class="form-group">
                                        <label for="street">Account Name <span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_account_name" id="ldap_account_name"  value="" maxlength="15" autocomplete="off" />
                                        <font style="color:red"><span id="ldap_account_name_error"></span></font>
                                    </div>

                                </div>
                                <div class="col-md-3" id="ldap_url">
                                    <div class="form-group">
                                        <label for="street"> URL of the application<span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_url" id="ldap_url"  value="" autocomplete="off" />
                                        <font style="color:red"><span id="ldap_url_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-3" id="ldap_id">
                                    <div class="form-group">
                                        <label for="street"> LDAP auth id allocated:<span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_auth_allocate" id="ldap_auth_allocate"  value="" autocomplete="off" />
                                        <font style="color:red"><span id="ldap_alocate_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-12 display-hide" id="sms_account">
                                    <div class="alert alert-secondary" style="background: #d3dae6;padding: 11px 12px 0px 16px;margin-bottom: 10px;">
                                        <p><b>Have you completed the TRAI DLT registration under TCCCPR 2018? This compliance is mandatory. Non compliance can impact your SMS service. For further information, please drop a mail to <a href="#">smssupport@nic.in</a>.</b></p>
                                    </div>
                                    <div class="form-group">
                                        <label for="street">Account Name <span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="account_name" id="account_name"  value="" maxlength="15" />
                                        <font style="color:red"><span id="account_name_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-3 display-hide" id="relay_app_div">
                                    <div class="form-group">
                                        <label for="street">Application Name <span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespace allowed]" type="text" name="relay_app" id="relay_app"  value="" maxlength="50" />
                                        <font style="color:red"><span id="app_name_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-3 display-hide" id="relay_app_ip">
                                    <div class="form-group">
                                        <label for="street">Old IP Address </label>
                                        <input class="form-control class_name" placeholder="Enter IP Address [e.g. 10.1.1.1]" type="text" name="relay_old_ip" id="relay_old_ip"  value="" maxlength="15" />
                                        <font style="color:red"><span id="relay_app_ip_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="row display-hide" id="server_div">
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
                                        <div class="col-md-6 display-hide" id="server_other">
                                            <label for="street">Enter server location <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                                            <font style="color:red"><span id="server_txt_err"></span></font>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-3">
                                    <label for="street">IP Address 1 <span style="color: red">*</span></label>
                                    <input class="form-control class_name" placeholder="IP Address 1 [e.g. 10.1.X.X]" type="text" name="add_ip1" id="add_ip1"  value="" maxlength="15"  />
                                    <font style="color:red"><span id="add_ip1_error"></span></font>

                                </div>
                                <div class="col-md-3">
                                    <label for="street">IP Address 2 <span style="color: red"></span></label>
                                    <input class="form-control class_name" placeholder="IP Address 2 [e.g. 10.1.X.X]" type="text" name="add_ip2" id="add_ip2"  value="" maxlength="15" />
                                    <font style="color:red"><span id="add_ip2_error"></span></font>
                                </div>
                                <div class="col-md-3">
                                    <label for="street">IP Address 3 <span style="color: red"></span></label>
                                    <input class="form-control class_name" placeholder="IP Address 3 [e.g. 10.1.X.X]" type="text" name="add_ip3" id="add_ip3" value="" maxlength="15" />
                                    <font style="color:red"><span id="add_ip3_error"></span></font>
                                </div>
                                <div class="col-md-3">
                                    <label for="street">IP Address 4 <span style="color: red"></span></label>
                                    <input class="form-control class_name" placeholder="IP Address 4 [e.g. 10.1.X.X]" type="text" name="add_ip4" id="add_ip4"  value="" maxlength="15" />
                                    <font style="color:red"><span id="add_ip4_error"></span></font>
                                </div>
                            </div>
                            <font style="color:red"><span id="ipduperr"></span></font> 
                        </div>

                        <div class="col-md-12 mt-5">
                            <div class="row">
                                <div class="col-md-6 mt-1" style="text-align: right;">
                                    <br/><label for="street">Captcha</label>
                                    <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>"  width="100" height="50" alt="captcha background" class="ft-lft captcha" />
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
                        </div>
                        <div class="col-md-12 mt-5 mb-5 text-center"> 
                            <!-- below line added by pr on 19thjan18 to implement CSRF  -->  
                            <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
                            <button name="submit" value="addip" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                        </div>
                    </div>
                    <input type="hidden" id="submit_val" value="addip">
                </form>
                <form action="" method="post" id="change_ip_form3">
                    <div id="change_wifi_div" class="display-hide" >
                        <div class="col-md-12 mt-4 mb-4">
                            <div class="alert alert-secondary">
                                <div class="col-md-12">
                                    <b>NOTE:</b><br>
                                    <ul style="margin-top: 15px;padding-left: 15px;">
                                        <li>Entries marked with asterisk  (<span style="color: red">*</span>) are mandatory</li>                                                                                            
                                        <li>Users are requested to install the personal firewall software to secure their machine and e-mail traffic.</li>
                                        <li>Users are requested to install the Antivirus software with latest pattern update periodically and OS patches in their system.</li>
                                        <li>If using Outlook, Outlook Express, Mozilla Firefox on Microsoft WINDOWS, please apply the appropriate patches announced by the Microsoft/ Mozilla from time to time.</li>
                                        <li>NIC will take all possible measures to prevent data loss, however, due to unforeseen technical issues, if the same happens, NIC cannot be held responsible.</li>
                                        <li>User is responsible for his/her data. In case he/she accidentally deletes data, he/she will not ask NIC to restore it.</li>
                                        <li>Contact our 24x7 support if you have any problems. Phone <b>1800-111-555</b> or you can send mail to <b>servicedesk@nic.in</b></li>
                                    </ul>
                                </div>
                            </div>                                                                   
                        </div>
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="street">Service request for <span style="color: red">*</span><br/><br/></label>
                                <div class="col-sm-offset-1">
                                    <!--28-apr-2022-->
                                    <!--                                    <div class="radio" style="display: inline-block;">
                                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                                <input type="radio" name="req_for" id="req_for_ldapauth"  value="ldap" /> LDAP AUTH
                                                                                <span></span>
                                                                            </label>&emsp;&emsp;
                                                                        </div>-->
                                    <!--                                    <div class="radio" style="display: inline-block;">
                                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                                <input type="radio" name="req_for" id="req_for_relay"  value="relay"  /> RELAY
                                                                                <span></span>
                                                                            </label>&emsp;&emsp;
                                                                        </div>-->
                                    <div class="radio" style="display: inline-block;">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="req_for" id="req_for_sms" value="sms" checked autofocus /> SMS
                                            <span></span>
                                        </label>&emsp;&emsp;
                                    </div>
                                    <font style="color:red"><span id="show_req_for"></span></font>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-3" id="ldap_account">
                                    <div class="form-group">
                                        <label for="street">Account Name <span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_account_name" id="ldap_account_name"  value="" maxlength="15" autocomplete="off" />
                                        <font style="color:red"><span id="ldap_account_name_error"></span></font>
                                    </div>

                                </div>
                                <div class="col-md-3" id="ldap_url">
                                    <div class="form-group">
                                        <label for="street"> URL of the application<span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_url" id="ldap_url"  value="" autocomplete="off" />
                                        <font style="color:red"><span id="ldap_url_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-3" id="ldap_id">
                                    <div class="form-group">
                                        <label for="street"> LDAP auth id allocated:<span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="ldap_auth_allocate" id="ldap_auth_allocate"  value="" autocomplete="off" />
                                        <font style="color:red"><span id="ldap_alocate_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-6 display-hide" id="sms_account">
                                    <div class="form-group">
                                        <label for="street">Account Name <span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Only characters,digits,dot(.),hyphen(-),underscore(_) allowed [5 to 15 characters]" type="text" name="account_name" id="account_name"  value="" maxlength="15" />
                                        <font style="color:red"><span id="account_name_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-md-6 display-hide" id="relay_app_div">
                                    <div class="form-group">
                                        <label for="street">Application Name <span style="color: red">*</span></label>
                                        <input class="form-control" placeholder="Enter Application Name, [characters only limit[50],dot(,),comma(,) whitespace allowed]" type="text" name="relay_app" id="relay_app"  value="" maxlength="50" />
                                        <font style="color:red"><span id="app_name_error"></span></font>
                                    </div>
                                </div>
                                <!--                                    <div class="col-md-6 display-hide" id="relay_app_ip">
                                                                        <div class="form-group">
                                                                            <label for="street">Old IP Address </label>
                                                                            <input class="form-control class_name" placeholder="Enter IP Address [e.g. 10.1.1.1]" type="text" name="relay_old_ip" id="relay_old_ip"  value="" maxlength="15" autocomplete="off" />
                                                                            <font style="color:red"><span id="relay_app_ip_error"></span></font>
                                                                        </div>
                                                                    </div>
                                -->
                                <div class="col-md-6">
                                    <div class="row display-hide" id="server_div">
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
                                        <div class="col-md-6 display-hide" id="server_other">
                                            <label for="street">Enter server location <span style="color: red">*</span></label>
                                            <input class="form-control" placeholder="Enter Server Location [Alphanumeric,whitespace and [. , - # / ( ) ] allowed]" type="text" name="server_loc_txt" id="server_loc_txt"  value="" maxlength="100">
                                            <font style="color:red"><span id="server_txt_err"></span></font>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">OLD IP Address 1 <span style="color: red">*</span></label>
                                        <input class="form-control class_name" placeholder="Enter OLD IP Address 1 [e.g. 10.1.X.X]" type="text" name="old_ip1" id="old_ip1" value="" maxlength="15"  />
                                        <font style="color:red"><span id="old_ip1_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">NEW IP Address 1 <span style="color: red">*</span></label>
                                        <input class="form-control class_name" placeholder="Enter NEW IP Address 1 [e.g. 10.1.X.X]" type="text" name="new_ip1" id="new_ip1"  value="" maxlength="15"  />
                                        <font style="color:red"><span id="new_ip1_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-2" style="margin-top: 30px;">
                                    <button type="button" value="abadd1" id="newbadd1" name="newbadd1" class="btn btn-primary btn-sm" title="Add IP" onclick="show2rowchild();"><i class="fa fa-plus fa-2x"></i></button>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="row" id="newdiv1ad1" style="display: none;">
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">OLD IP Address 2 <span style="color: red"></span></label>
                                        <input class="form-control class_name" placeholder="Enter OLD IP Address 2 [e.g. 10.1.X.X]" type="text" name="old_ip2" id="old_ip2"  value="" maxlength="15"/>
                                        <font style="color:red"><span id="old_ip2_error"></span></font>
                                        <font style="color:red"><span id="old_ipp2_error"></span></font>  
                                    </div>
                                </div>
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">NEW IP Address 2 <span style="color: red"></span></label>
                                        <input class="form-control class_name" placeholder="NEW IP Address 2 [e.g. 10.1.X.X]" type="text" name="new_ip2" id="new_ip2" value="" maxlength="15" />
                                        <font style="color:red"><span id="new_ip2_error"></span></font>
                                        <font style="color:red"><span id="new_ipp2_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-2" style="margin-top: 30px;">
                                    <button type="button" class="btn btn-primary btn-sm" value="abadd2" id="newbadd2" name="newbadd2" title="Add IP" onclick="show3rowchild();"><i class="fa fa-plus fa-2x"></i></button>
                                    <button type="button" class="btn btn-warning btn-sm" value="abadd2" id="newbremo2" name="newbremo2" title="Remove IP" onclick="hide2rowchild();"><i class="fa fa-minus fa-2x"></i></button>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <div class="row" id="newdiv2ad2" style="display: none;">
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">OLD IP Address 3 <span style="color: red"></span></label>
                                        <input class="form-control class_name" placeholder="Enter OLD IP Address 3 [e.g. 10.1.X.X]" type="text" name="old_ip3" id="old_ip3" value="" maxlength="15"  />
                                        <font style="color:red"><span id="old_ip3_error"></span></font>
                                        <font style="color:red"><span id="old_ipp3_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">NEW IP Address 3 <span style="color: red"></span></label>
                                        <input class="form-control class_name" placeholder="Enter NEW IP Address 3 [e.g. 10.1.X.X]" type="text" name="new_ip3" id="new_ip3"  value="" maxlength="15" />
                                        <font style="color:red"><span id="new_ip3_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-2" style="margin-top: 30px;">
                                    <button type="button" class="btn btn-primary btn-sm" value="abadd3" id="newbadd3" name="newbadd3" title="Add IP" onclick="show4rowchild();"><i class="fa fa-plus fa-2x"></i></button>
                                    <button type="button" class="btn btn-warning btn-sm" value="abadd3" id="newbremo3" name="newbremo3" title="Remove IP" onclick="hide3rowchild();"><i class="fa fa-minus fa-2x"></i></button>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-12">
                            <div class="row" id="newdiv3ad3" style="display: none;">
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">OLD IP Address 4 <span style="color: red"></span></label>
                                        <input class="form-control class_name" placeholder="Enter OLD IP Address 4 [e.g. 10.1.X.X]" type="text" name="old_ip4" id="old_ip4"  value="" maxlength="15" />
                                        <font style="color:red"><span id="old_ip4_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-5">
                                    <div class="form-group">
                                        <label for="street">NEW IP Address 4 <span style="color: red"></span></label>
                                        <input class="form-control class_name" placeholder="Enter NEW IP Address 4 [e.g. 10.1.X.X]" type="text" name="new_ip4" id="new_ip4" value="" maxlength="15" />
                                        <font style="color:red"><span id="new_ip4_error"></span></font>
                                    </div>
                                </div>
                                <div class="col-2" style="margin-top: 30px;">
                                    <button type="button" class="btn btn-warning btn-sm" value="abadd4" id="newbremo4" name="newbremo4" title="Remove IP" onclick="hide4rowchild();"><i class="fa fa-minus fa-2x"></i></button>
                                </div>
                            </div>
                        </div>

                        <font style="color:red"><span id="ipduperr"></span></font>
                        <div class="col-md-12">
                            <div class="row">
                                <div class="col-md-6" style="text-align: right;">
                                    <br/><label for="street">Captcha</label>
                                    <img name="Captcha" id="captcha" src="Captcha?var=<%=random%>"  width="100" height="50" alt="captcha background" class="ft-lft captcha" />
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
                        </div>
                        <div class="col-md-12 mt-5 mb-4 text-center">
                            <!-- below line added by pr on 10thjan18 to implement CSRF  -->  
                            <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom"  />
                            <button name="submit" value="changeip" class="btn purple btn-success sbold" > Preview and Submit </button>                                                                
                        </div>
                    </div>
                    <input type="hidden" id="submit_val" value="changeip">
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="include/new_include/footer.jsp" />

<!-- /.modal -->
<div class="modal fade bs-modal-lg" id="large" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="change_ip_form4" method="post">
        <jsp:include page="include/change_ip_preview.jsp" />
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
<!-- Nested Modal-->
<!-- Modal for confirmation -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="changeip_form_confirm">
        <jsp:include page="include/Hod_detail.jsp" />
    </form>
</div>


<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/change_ip.js" type="text/javascript"></script>
<script>
                                        jQuery(document).ready(function () {
                                            $("#change_ip_form2 #refresh").on("click", function () {
                                                $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
                                            });
                                            $("#change_ip_form3 #refresh").on("click", function () {
                                                $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
                                            });
                                            $("#change_ip_form4 #closebtn").on("click", function () {
                                                $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
                                                $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
                                                $('#imgtxt1').val("");
                                                $('#imgtxt2').val("");
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
    });
</script>
<script>
    $(document).ready(function () {
        if ('<%=session.getAttribute("resend_request")%>' == "true") {
            var form_name = '<s:property value="#session['prvwdetails'].form_name" />';
            if (form_name == "IP")
            {
                var req_for = '<s:property value="#session['prvwdetails'].req_for" />';
                var account_name = '<s:property value="#session['prvwdetails'].account_name" />';
                var app_name = '<s:property value="#session['prvwdetails'].app_name" />';
                var app_ip = '<s:property value="#session['prvwdetails'].app_ip" />';
                var server_loc = '<s:property value="#session['prvwdetails'].server_loc" />';
                var server_loc_other = '<s:property value="#session['prvwdetails'].server_loc_other" />';
                var ip_type = '<s:property value="#session['prvwdetails'].ip_type" />';
                var add_ip1 = '<s:property value="#session['prvwdetails'].add_ip1" />';
                var add_ip2 = '<s:property value="#session['prvwdetails'].add_ip2" />';
                var add_ip3 = '<s:property value="#session['prvwdetails'].add_ip3" />';
                var add_ip4 = '<s:property value="#session['prvwdetails'].add_ip4" />';
                $("input:radio[value='" + req_for + "']").prop("checked", true);

                if (req_for === "sms")
                {
                    $('#sms_account').removeClass('display-hide');
                    $('#account_name').val(account_name);
                    $('#relay_app_div').addClass('display-hide');
                    $('#relay_app_ip').addClass('display-hide');
                    $('#relay_app').val('');
                    $('#relay_old_ip').val('');
                    $('#server_div').addClass('display-hide');
                } else if (req_for === "relay")
                {
                    $('#sms_account').addClass('display-hide');
                    $('#account_name').val("");
                    $('#relay_app_div').removeClass('display-hide');
                    $('#relay_app_ip').removeClass('display-hide');
                    $('#server_div').removeClass('display-hide');
                    $('#relay_app').val(app_name);
                    $('#relay_old_ip').val(app_ip);
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
                } else {
                    $('#sms_account').addClass('display-hide');
                    $('#account_name').val("");
                    $('#relay_app_div').addClass('display-hide');
                    $('#relay_app_ip').addClass('display-hide');
                    $('#relay_app').val('');
                    $('#relay_old_ip').val('');
                    $('#server_div').addClass('display-hide');
                }
                if (ip_type === 'addip') {
//                            $('#add_ip_div').removeClass('display-hide');
//                            $('#change_ip_div').addClass('display-hide');
                    $('#add_ip1').val(add_ip1);
                    $('#add_ip2').val(add_ip2);
                    $('#add_ip3').val(add_ip3);
                    $('#add_ip4').val(add_ip4);
                    $('#confirm').val(ip_type);
                    $('#ip_form_type').html("ADD IP");
                } else {
//                            $('#ip_preview #add_ip_div').addClass('display-hide');
//                            $('#ip_preview #change_ip_div').removeClass('display-hide');
                    if (add_ip1.indexOf(";") > -1) {
                        var myarray = add_ip1.split(';');
                        $('#old_ip1').val(myarray[0].trim());
                        $('#new_ip1').val(myarray[1].trim());
                    }
                    if (add_ip2 !== "")
                    {
                        if (add_ip2.indexOf(";") > -1) {
                            var myarray = add_ip2.split(';');
                            if (myarray[0].trim() !== "")
                            {
                                $('#newdiv1ad1').show();
                                $('#old_ip2').val(myarray[0].trim());
                                $('#new_ip2').val(myarray[1].trim());
                            }
                        }
                    } else {
                        $('#newdiv1ad1').hide();
                        $('#old_ip2').val("");
                        $('#new_ip2').val("");
                    }
                    if (add_ip3 !== "")
                    {
                        if (add_ip3.indexOf(";") > -1) {
                            var myarray = add_ip3.split(';');
                            if (myarray[0].trim() !== "")
                            {
                                $('#newdiv2ad2').show();
                                $('#old_ip3').val(myarray[0].trim());
                                $('#new_ip3').val(myarray[1].trim());
                            }
                        } else
                        {
                            $('#newdiv2ad2').hide();
                            $('#old_ip3').val("");
                            $('#new_ip3').val("");
                        }
                    } else {
                        $('#newdiv2ad2').hide();
                        $('#old_ip3').val("");
                        $('#new_ip3').val("");
                    }
                    if (add_ip4 !== "")
                    {
                        if (add_ip4.indexOf(";") > -1) {
                            var myarray = add_ip4.split(';');
                            if (myarray[0].trim() !== "")
                            {
                                $('#old_ip4').val(myarray[0].trim());
                                $('#new_ip4').val(myarray[1].trim());
                                $('#newdiv3ad3').show();
                            } else {
                                $('#newdiv3ad3').hide();
                                $('#old_ip4').val("");
                                $('#new_ip4').val("");
                            }
                        }
                    } else {
                        $('#newdiv3ad3').hide();
                        $('#old_ip4').val("");
                        $('#new_ip4').val("");
                    }
                    $('#ip_form_type').html("CHANGE IP");
                }
                $('#confirm').val(ip_type);
            }
        }

    });
</script>