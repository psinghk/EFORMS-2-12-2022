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
                    <h3 class="k-portlet__head-title">Form Submission Status</h3>
                </div>
            </div>
            <div class="portlet-body form">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-body">
                            <%  if (session.getAttribute("moduleEsign") != null) {

                                    System.out.println("module esign:::::" + session.getAttribute("moduleEsign"));

                                                                    if (session.getAttribute("moduleEsign").equals("esign")) {%>
                            <div class="modal-body">


<!--                                <h4 class="modal-title">Your mobile number has been updated</h4>-->
                                
                                
                                <% if (session.getAttribute("mobile_flag").equals("yes") && session.getAttribute("check_flag").equals("esign")) {%>
                                <h4 class="modal-title">Your Profile has been updated</h4>
                                </br>
                                <p>Your profile has been successfully updated against Registration number <%= session.getAttribute("ref_num")%> </p> 
                                <p><i class="entypo-info-circled"></i>Your request can be viewed under Total User Requests and Total Completed Requests tabs under MY REQUEST dashboard.</p>
                                <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <b>servicedesk@nic.in</b>.</p>
                                <% } else if (session.getAttribute("form_type").equals("dor_ext_retired") && session.getAttribute("check_flag").equals("esign")) {%>
                                <h4 class="modal-title">Your email account's expiry date has been updated</h4>
                                </br>
                                <p>Account expiry date for your email address has been successfully updated against Registration number <%= session.getAttribute("ref_num")%> </p> 
                                <p><i class="entypo-info-circled"></i>Your request can be viewed under Total User Requests and Total Completed Requests tabs under MY REQUEST dashboard.</p>
                                <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <b>servicedesk@nic.in</b>.</p>
                                <% } else { %>
                                <h4 class="modal-title">Form successfully eSigned!!!</h4>
                                <p>Your Registration number is <b><%=session.getAttribute("ref_num").toString()%></b></p>
                                <p><i class="entypo-info-circled"></i> You can use  it to track your request. You can track your request using <a href="showUserData">Track User</a></p>
                                <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <b>servicedesk@nic.in</b>.</p>
                                <% } %>
                                
                                <div class="text-center">
                                    <a href="showUserData" class="btn btn-primary mt-4" >Back</a>
                                </div>
                            </div>
                            <% }%>
                            <%  if (session.getAttribute("moduleEsign").equals("esignFail")) {%>
                            <div class="modal-body">
                                <h4 class="modal-title">Esign Failed</h4>
                                </br>
                                <p><i class="entypo-info-circled"></i> Esigining of the document could not be done.Please try after sometime.</p>
                                <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <b>servicedesk@nic.in</b>.</p>
                                <div class="modal-footer">
                                    <button type="button" class="btn dark btn-outline" id="done_close" >Close</button>
                                </div>
                            </div>
                            <% }%>
                            <% }%>
                        </div>
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