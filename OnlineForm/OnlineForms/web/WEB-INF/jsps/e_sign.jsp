<%@page import="entities.LdapQuery"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
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
    if (session.getAttribute("ref_num") != null) {
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
                <a href="Forms" class="k-content__head-breadcrumb-link">Home</a>
                <span class="k-content__head-breadcrumb-separator"></span>
                <a href="" class="k-content__head-breadcrumb-link"><span><%=session.getAttribute("form_text").toString()%></span></a>

                <!-- <span class="k-content__head-breadcrumb-link k-content__head-breadcrumb-link--active">Active link</span> -->
            </div>
        </div>
    </div>
    <!-- begin:: Content Body -->
    <div class="k-portlet k-portlet--mobile">
        <div class="highlight-div-sty">
            <div class="k-portlet__head">
                <div class="k-portlet__head-label">
                    <h3 class="k-portlet__head-title">Form Submission Type</h3>
                </div>
            </div>
            <div class="portlet-body form">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-body">
                            <div class="form-group" style="padding:15px;" id="esign1">
                                <div class="mt-radio-list" id="esign_div">
                                            <% if (session.getAttribute("update_without_oldmobile").equals("yes")) { %>
                                            <!--<div class="alert alert-warning mb-4">
//                                                <label><strong style="font-size: 15px;">
//                                                        Out of these 3 options shown below for submitting the request,  If you choose, eSign option, your mobile number will be updated in NIC repository instantaneously bypassing all the workflow. If you want to update the mobile number immediately, please use eSign option.
//                                                    </strong></label>
//                                            </div>-->
                                    <% }
                                        if (session.getAttribute("esign_hod_email") != null) {
                                            String da_onbording = "";
                                            String hod_email = session.getAttribute("esign_hod_email").toString();
                                            String user_exists = LdapQuery.GetMobile(hod_email);
                                            if (user_exists.equals("error")) {
                                                //show manual
                                    %>
                                    <br/>
                                    <label class="mt-radio mt-radio-outline" style="font-size: 16px; text-align: center"> Since email address of your Reporting/Nodal/Forwarding Officer does not exist in NIC repository, therefore your request cannot be processed online. <br/>
                                        Please click on Continue and Download PDF file. You have to seal and sign the downloaded application form and upload it again on eForms. <a href="showUserData">Track</a> USER STATUS module to get the request processed.
                                        <input class="display-hide" type="radio" value="manual_upload" name="esign_type" checked="checked" />
                                        <!--                                                                        <span></span>-->
                                    </label>
                                    <%
                                    } else if (session.getAttribute("nodomain") != null && session.getAttribute("nodomain").toString().equals("yes")) {
                                        //show manual
                                    %>
                                    <br/>
                                    <label class="mt-radio mt-radio-outline" style="font-size: 16px; text-align: center"> Since email address of your Reporting/Nodal/Forwarding Officer does not exist in NIC repository, therefore your request cannot be processed online. <br/>
                                        Please click on Continue and Download PDF file. You have to seal and sign the downloaded application form and upload it again on eForms. <a href="showUserData">Track</a> USER STATUS module to get the request processed.
                                        <input class="display-hide" type="radio" value="manual_upload" name="esign_type" checked="checked" />
                                        <!--                                                                        <span></span>-->
                                    </label>
                                    <%
                                    } else {
                                        //show other
                                    %>
                                    <%
                                        boolean show_2option = true;
                                        System.out.println("esign daonboarding :::::::::"+session.getAttribute("da_onboarding"));
                                        if (session.getAttribute("da_onboarding") != null && session.getAttribute("da_onboarding").toString().equalsIgnoreCase("true")) {
                                            show_2option = false;
                                          
                                        }
                                        
                                        if (show_2option) {

                                      
                                    %>

                                    <label><strong>Please select any to proceed:</strong></label><br/><br/>
                                    <div class="col-md-12 mb-2">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="esign" name="esign_type"> e-Sign the document with Aadhaar?
                                            <div style="font-size: 10px; color: red;">(Delivery of e-sign with aadhaar depends on platforms outside control of NIC. In case of delay, you may choose to proceed online without aadhaar)</div>
                                            <span></span>
                                        </label>
                                    </div>
                                    <div class="col-md-12 mb-3">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="online" name="esign_type" checked> Proceed online
                                            <span></span>
                                        </label>
                                    </div>
                                    <%                                        }
                                    %>
                                    <div class="col-md-12 mb-3">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" value="manual_upload" name="esign_type" 
                                                   <%
                                                       if (!show_2option) {
                                                           out.print("checked");
                                                       }
                                                   %>
                                                   > Proceed manually by uploading the scanned Copy?
                                            <% if (show_2option) { %>
                                               <div style="font-size: 10px; color: red;">(Here, in this option, you will have to download the generated PDF and will have to sign and stamp and then upload it again on the eforms TRACK USER STATUS module  to get the request processed)</div>
                                            <% } else { %>
<!--                                            <div style="font-size: 15px; color: red;">You have to download generated PDF of application form, seal & sign and upload it on the eforms</div>-->
                                                <div style="font-size: 15px; color: red;">You have to download generated PDF of application form, duly filled (seal & sign) by you and your competent authority and upload it on the eforms</div>
                                            <!--<div style="font-size: 10px; color: red;">You have to download generated PDF of application form, sign and stamp and upload it again on the eforms</div>-->
                                            <!--(Here in this option you will have to download PDF of application form, sign and stamp and upload it again on the eforms.)-->
                                            <% } %>
                                            <span></span>
                                        </label>
                                    </div>
                                    <%                                            }
                                        }
                                    %>
                                    <!--                                                                        <span></span>-->
                                </div>
                                <font style="color:red"><span id="esign_error"></span></font>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-12 text-center">
                    <div class="form-action mb-4">
                        <button class="btn btn-success" id="esign_submit" value="<%=session.getAttribute("esign_submit").toString()%>" type="button"><i class="fa fa-check"></i>Continue</button>
                        <!--                            <img src="assets/images/loading.gif" id="esign_img3" style="height:60px; display: none">-->
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- end -->
    <div class="modal fade bs-modal-lg" id="done" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Your form has been submitted</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
                </div>
                <div class="modal-body">
                    <p id="reg_num"></p>
                    <p><i class="entypo-info-circled"></i> You can use  it to track your request. You can track your request using <a class="btn btn-sm btn-primary" href="showUserData"><i class="fa fa-map-marker"></i>&ensp;Track User</a></p>
                    <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <span class="reg_no_sty"><b>servicedesk@nic.in</b></span>.</p>


                    <%
 if(session.getAttribute("esign_submit").toString().equalsIgnoreCase("sms"))
                            {%> 
  <p><i class="entypo-info-circled"></i>  Request will remain under process unless you share the TRAIL DLT registration details to smssupport@nic.in. Please share the below details with SMS Team (smssupport@nic.in) to get the SMS credentials :-</p>

                        <ul><li> Department Name as registered on DLT:</li>
                            <li>Department Entity ID (19 Digit ):</li>
                            <li>Name of the person authorized for DLT:</li>
                            <li> Department Mail Id registered:</li>
                            <li>Name of all Headers/sender id registered:</li>
                            <li> All Headers /sender-id (19 Digit) ID:</li>
                        </ul>
<% } %>                   


                    <div class="modal-footer">
                        <button type="button" class="btn btn-danger" id="done_close" >Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade bs-modal-lg" id="failed" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <!--                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>-->
                    <h4 class="modal-title">FORM COULD NOT BE SUBMITTED</h4>
                </div>
                <div class="modal-body">
                    <p><i class="entypo-info-circled"></i> There was some technical issue, please try again. Sorry for inconvenience.</p>
                    <p><i class="entypo-info-circled"></i> For any other assistance, please contact on <b>1800-111-555</b> or mail us to <span class="reg_no_sty"><b>servicedesk@nic.in</b></span>.</p>
                    <div class="modal-footer">
                        <button type="button" class="btn dark btn-default" id="generate_close1" >Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <s:url var="fileDownload" namespace="/" action="fileDownload" ></s:url>
        <div class="modal fade bs-modal-lg" id="generate" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Your form has been submitted</h4>
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
                    </div>
                    <div class="modal-body">
                        <p id="reg_num1"></p>
                        <p><i class="entypo-info-circled"></i> You can use  it to track your request. You can track your request using <a class="btn btn-sm btn-primary" href="showUserData"><i class="fa fa-map-marker"></i>&ensp;Track User</a></p>
                        <p><i class="entypo-info-circled"></i> You have to download generated PDF, seal and sign and upload it again on eforms <a class="btn btn-sm btn-primary" href="showUserData"><i class="fa fa-map-marker"></i>&ensp;Track User</a> to get the request processed.</p>
                        <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <span class="reg_no_sty"><b>servicedesk@nic.in</b></span>.</p>
                   <%
 if(session.getAttribute("esign_submit").toString().equalsIgnoreCase("sms"))
                            {%> 
  <p><i class="entypo-info-circled"></i>  Request will remain under process unless you share the TRAIL DLT registration details to smssupport@nic.in. Please share the below details with SMS Team (smssupport@nic.in) to get the SMS credentials :-</p>

                        <ul><li> Department Name as registered on DLT:</li>
                            <li>Department Entity ID (19 Digit ):</li>
                            <li>Name of the person authorized for DLT:</li>
                            <li> Department Mail Id registered:</li>
                            <li>Name of all Headers/sender id registered:</li>
                            <li> All Headers /sender-id (19 Digit) ID:</li>
                        </ul>
<% } %>                   


                        <div class="modal-footer">
                        <s:a href="%{fileDownload}" target="_blank"><button type="button"  class="btn  green btn-primary">Download PDF</button></s:a>
                            <button type="button" class="btn dark btn-danger" id="generate_close" >Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade bs-modal-lg" id="esign" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog modal-lg" style="max-width: 550px;">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                        <h4 class="modal-title">E-sign</h4>
                    </div>
                    <div  id="aadhaar-div">
                        <div class="modal-body">
                            <div class="row" style="padding: 20px;">
                                <label>Enter aadhaar Number: </label>
                                <input type="text" id="aadhaar" name="aadhaar" maxlength="12" autofocus class="form-control" placeholder="Enter 12 digit aadhaar Number" />
                                <font style="color:red"><span id="aadhaar_error"></span></font>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn  green" id="esign_1" >Submit</button>
                            <img src="assets/images/loading.gif" id="esign_img1" style="height:60px; display: none">
                            <button type="button" class="btn  red" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                    <div  id="aadhaar-otp-div" class="display-hide">
                        <div class="modal-body">
                            <div class="row" style="padding: 20px;">
                                <label>Enter OTP sent to your mobile number registered with aadhaar: </label>
                                <input type="text" id="otp-aadhaar" name="otp-aadhaar" maxlength="6" autofocus class="form-control" placeholder="Enter OTP sent to your mobile number registered with aadhaar" />
                                <font style="color:red"><span id="otp-aadhaar_error"></span></font>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn  green" id="esign_2" >Submit</button>
                            <img src="assets/images/loading.gif" id="esign_img2" style="height:60px; display: none">
                            <button type="button" class="btn  red" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
<jsp:include page="include/new_include/footer.jsp" />
<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/imappop.js" type="text/javascript"></script>
<% } else {
        response.sendRedirect("index.jsp");
    }%>
    
    <%
        String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    %>
    
    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />