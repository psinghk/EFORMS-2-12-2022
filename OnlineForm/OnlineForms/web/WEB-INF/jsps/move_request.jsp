
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
    if (userdata.getEmail().equals("support@nic.in") || userdata.getEmail().equals("support@gov.in")) {

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
            <div class="portlet light moveRequest " id="form_wizard_1" style="display:block;">

                <div class="portlet-body form">
                    <div class="form-wizard">
                        <div class="form-body">
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab2" >
                                    <form id="" autocomplete="off">
                                        <div class="col-md-12" style="margin-top: 25px;">
                                            <h4 class="theme-heading-h3">Move Request</h4>
                                        </div>
                                        <div class="col-md-12"><span id="msg"></span></div>
                                        <div class="col-md-12">
                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label for="street">Fetch Requests By Email<span style="color: red">*</span></label>
                                                    <input class="form-control" value="" placeholder="" type="text" name="inputCurrentCoordinatorEmail" id="inputCurrentCoordinatorEmail" maxlength="50"  aria-required="true">
                                                    <span style="color: red;" id="errorCurrentCoordinatorEmail"></span>
                                                </div>

                                                <div class="col-md-3" id="rolesSection">                                                   
                                                    <label for="">Roles <span style="color: red">*</span></label>                                           
                                                    <select class="form-control" id="inputPendingStatus" name="inputPendingStatus">
                                                        <option value="">--Select--</option>
                                                        <option value="coordinator_pending">coordinator_pending</option>
                                                        <option value="da_pending">da_pending</option>
                                                    </select>
                                                    <span style="color: red;" id="errorPendingStatus"></span>
                                                </div>

                                                <div class="col-md-3" style="display:none;" id="cemailSection">
                                                    <label for="street"> Change Email <span style="color: red">*</span></label>
                                                    <input class="form-control" placeholder="" type="text" name="to_email" id="to_email" value="" maxlength="100"  aria-required="true">  
                                                    <span style="color: red;" id="toemailerror"></span>

                                                </div>

                                                <div class="col-md-3">
                                                    <span class="btn btn-outline btn-primary mt-4 ml-3" style="display:none;" id="move_tab_submit">  Submit</span>
                                                    <button class="btn btn-outline btn-primary mt-4 ml-3 buttonShowDepartment">Show Departments</</button>
                                                </div> 

                                            </div> 

                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <!--<h4 class="theme-heading-h3">Pending </h4>-->
                                                <div >
                                                    <table id="showEmpCategoryMinDept" >
                                                    </table>
                                                </div>
                                            </div>
                                            
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <!--<h4 class="theme-heading-h3">Pending </h4>-->
                                                <div >
                                                    <table id="lePendingCoordinatorRequests" >                                                    
                                                    </table>
                                                </div>
                                            </div>
                                            
                                            <div class="col-md-12" style="margin-top: 25px;">
                                                <!--<h4 class="theme-heading-h3">Pending </h4>-->
                                                <div >
                                                    <table id="leCoordinatorEmails" >                                                    
                                                    </table>
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
    </div>
</div>
<!-- END PAGE CONTENT INNER -->
<jsp:include page="include/new_include/footer.jsp" />
<script src="main_js/moveRequest.js" type="text/javascript"></script>   
<link rel="stylesheet" type="text/css" href="assets/css/moveRequest.css"/>
<%    }
%>