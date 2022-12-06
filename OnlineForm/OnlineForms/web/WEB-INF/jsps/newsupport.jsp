<%@page import="com.org.bean.UserData"%>
<%@page import="admin.ForwardAction"%>
<%@page import="java.util.List"%>
<%--<%@page import="com.org.bean.Forms"%>--%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.org.utility.Constants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib  uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<%
    

//    if (!session.getAttribute("update_without_oldmobile").equals("no")) {
//        if (!session.getAttribute("profile_present").equals("true")) {
//            response.sendRedirect("Mobile_registration");
//        }
//        response.sendRedirect("index.jsp");
//    }
    String alertVpn = "";
    UserData userdata = (UserData) session.getAttribute("uservalues");
    String sessEmail = userdata.getEmail();

    String role = Constants.ROLE_CA;
    if (session.getAttribute("admin_role") != null) {
        role = session.getAttribute("admin_role").toString();
    }
    String loggedin_email = sessEmail; // line added by pr on 6thmar18
    // below code added by pr on 23rdjan18
    String CSRFRandom = entities.Random.csrf_random();

    session.setAttribute("CSRFRandom", CSRFRandom);
    String esignMsg = "";
    String moduleEsign = "";
    System.out.println(" inside new support file after session values admin role value is " + loggedin_email);
//    if (session.getAttribute("moduleEsign") != null) {
//        moduleEsign = session.getAttribute("moduleEsign").toString();
//        System.out.println("moduleEsign" + moduleEsign);
//        if (session.getAttribute("msg") != null) {
//            esignMsg = session.getAttribute("msg").toString();
//        } else {
//            esignMsg = "Esigining of the document could not be done.Please try after sometime";
//        }
//        System.out.println("text" + esignMsg);
//
//    }

    if (!session.getAttribute("update_without_oldmobile").equals("no")) {

        if (!userdata.isIsNewUser()) {
            response.sendRedirect("Mobile_registration");
        }
        response.sendRedirect("index.jsp");
    }

    ArrayList roless = (ArrayList) userdata.getRoles();
    System.out.println("roless###########3" + roless);

    if (roless.contains("co")) {
        if (roless.contains("email_co") && !roless.contains("vpn_co")) {
            //As you are VPN coordinator only, you may take action only for VPN requests. 
        } else if (!roless.contains("email_co") && roless.contains("vpn_co")) {
            alertVpn = "<div class=\"alert alert-danger\" style='border-color: #2d80c3;margin: 0;'><div class='col-md-12'><div class='mb-2'>As you are VPN coordinator only, you may take action only for VPN requests.</div></div>";
        }
    }

%>
<%    String alertMsg = "";
    if (role.equals(Constants.ROLE_CA)) {
        alertMsg = "<div class=\"alert alert-warning\" style='background: #64B5F6;border-color: #2d80c3;margin: 0;'><div class='col-md-12'><div class='mb-2'><b>Dear Reporting Officer/Forwarding Officer/Nodal Officer,</b></div><b>Notice : </b>You are requested to verify the credentials and authenticity of the applicant prior to approval or creation of account. If more information is required please use the option “RAISE A QUERY“ and ask for more inputs for verifying credentials</h3></div></div>";
    } else if (role.equals(Constants.ROLE_CO)) {
        alertMsg = "<div class=\"alert alert-warning\" style='background: #64B5F6;border-color: #2d80c3;margin: 0;'><div class='col-md-12'><div class='mb-2'><b>Dear Coordinator,</b></div><b>Notice : </b>You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer prior to approval or creation of account. If more information is required please use the option “RAISE A QUERY“ and ask for more inputs for verifying credentials</h3></div></div>";
    } else if (role.equals(Constants.ROLE_MAILADMIN)) {
        alertMsg = "<div class=\"alert alert-warning\" style='background: #64B5F6;border-color: #2d80c3;margin: 0;'><div class='col-md-12'><div class='mb-2'><b>Dear Admin,</b></div><b>Notice : </b>You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer prior to approval or creation of account. If more information is required please use the option “RAISE A QUERY“ and ask for more inputs for verifying credentials</h3></div></div>";
    } else if (role.equals(Constants.ROLE_SUP)) {
        alertMsg = "<div class=\"alert alert-warning\" style='background: #64B5F6;border-color: #2d80c3;margin: 0;'><div class='col-md-12'><div class='mb-2'><b>Dear Support,</b></div><b>Notice : </b>You are requested to verify the credentials and authenticity of the applicant and Reporting/Forwarding Officer before forwarding the requests. If more information is required please use the option “RAISE A QUERY“ and ask for more inputs for verifying credentials</h3></div></div>";
    }
%>

<jsp:include page="include/new_include/sidebar_nav.jsp" />
<link rel="stylesheet" href="assets/custom/tracker-sty.css" />
<jsp:include page="include/new_include/header.jsp" />  
<jsp:include page="include/new_include/filter_pop_support.jsp" />
<!-- begin:: Content -->
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">
    <!--    <div class="alert alert-info"><h4><i class="fa fa-chalkboard-teacher"></i>&ensp;<//s:property value="listBeanObj.console" /></h4></div>-->
    <!-- begin:: Content Head -->
    <div class="alert-box-div">
        <%=alertMsg%>
    </div>

    <!-- end:: Content Head -->

    <!-- begin:: Content Body -->
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">

        <!--begin::Dashboard 2-->
        <div class="row fillter-div-max-tabs">
            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('total');">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--info" style="background: #303F9F;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">

                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total User Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>

                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number totalCount" id="totalCount"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
                <!--begin::Portlet-->

                <!--end::Portlet-->
            </div>

            <div class="col-3">

                <!--begin::Portlet-->
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('new');">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--warning">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">

                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Today's Pending Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>

                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number newCount" id="newCount"></span>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </a>

                <!--end::Portlet-->
            </div>

            <div class="col-3">

                <!--begin::Portlet-->
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('pending');" >
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--danger" style="background: #d32f2f;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">

                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total Pending Request</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>

                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number counterup" id="counterup"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
                <!--end::Portlet-->
            </div>

            <div class="col-3">

                <!--begin::Portlet-->
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('completed');" >
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--success" style="background: #00796B;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">

                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total Completed Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>

                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number completeCount" id="completeCount"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>

                <!--end::Portlet-->
            </div>
        </div>
        <!--begin::Row-->
        <div class="row">


            <div class="col-lg-3 col-xl-3 order-lg-2 order-xl-1 fillter-div-max">

                <!--begin::Portlet-->
                <div class="k-portlet k-portlet--height-fluid stack-eform">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">General Filters</h3>
                        </div>

                    </div>
                    <div class="k-portlet__body">
                        <div class="k-widget-18">
                            <div class="search-filter d-none">

                                <div class="panel-group">
                                    <div class="panel panel-default" style="border:none;">

                                        <h3 class="theme-heading-h3">Application</h3>    
                                        <div class="error_cnts"></div><!-- line added by pr on 26thmar19 -->

                                        <div id="collapse_3_1" class="panel-collapse ">
                                            <div class="panel-body">
                                                <div class="form-group">
                                                    <div style="font-weight: bold;" class="mt-radio-list" id="filter_div"> <!--  div id added by pr on 21stmay18  -->
                                                        <!-- title added and form name changed by pr on 17thjan19 -->
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.SINGLE_FORM_KEYWORD%>_form" id="<%=Constants.SINGLE_FORM_KEYWORD%>_form" style="display:none;" title="Check this checkbox, if you want to see Single email ID creation requests"> Single Email Creation Request
                                                            <input type="checkbox" value="<%=Constants.SINGLE_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.BULK_FORM_KEYWORD%>_form" id="<%=Constants.BULK_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Bulk email ID creation requests"> Bulk Email Creation Request
                                                            <input type="checkbox" value="<%=Constants.BULK_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.SMS_FORM_KEYWORD%>_form" id="<%=Constants.SMS_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see SMS Services Requests"> SMS Services
                                                            <input type="checkbox" value="<%=Constants.SMS_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.IP_FORM_KEYWORD%>_form" id="<%=Constants.IP_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see IP Change Requests requests"> IP Change Requests
                                                            <input type="checkbox" value="<%=Constants.IP_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.DIST_FORM_KEYWORD%>_form" id="<%=Constants.DIST_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Distribution List requests"> Distribution List Services
                                                            <input type="checkbox" value="<%=Constants.DIST_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.BULKDIST_FORM_KEYWORD%>_form" id="<%=Constants.BULKDIST_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Bulk Distribution List requests"> Bulk Distribution List
                                                            <input type="checkbox" value="<%=Constants.BULKDIST_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.GEM_FORM_KEYWORD%>_form" id="<%=Constants.GEM_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see GEM email ID creation  requests"> GEM Email Creation Request
                                                            <input type="checkbox" value="<%=Constants.GEM_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.MOB_FORM_KEYWORD%>_form" id="<%=Constants.MOB_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Update Mobile requests"> Update Your Mobile Number
                                                            <input type="checkbox" value="<%=Constants.MOB_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.IMAP_FORM_KEYWORD%>_form" id="<%=Constants.IMAP_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see IMAP/POP requests"> Enable or Disable IMAP/POP
                                                            <input type="checkbox" value="<%=Constants.IMAP_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.LDAP_FORM_KEYWORD%>_form" id="<%=Constants.LDAP_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see LDAP requests"> Authentication Services (LDAP)
                                                            <input type="checkbox" value="<%=Constants.LDAP_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.NKN_SINGLE_FORM_KEYWORD%>_form" id="<%=Constants.NKN_SINGLE_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see NKN email ID creation requests"> NKN Email Creation Request
                                                            <input type="checkbox"  value="<%=Constants.NKN_SINGLE_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.RELAY_FORM_KEYWORD%>_form" id="<%=Constants.RELAY_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Relay requests"> SMTP Gateway Services (Relay)
                                                            <input type="checkbox"  value="<%=Constants.RELAY_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.DNS_FORM_KEYWORD%>_form" id="<%=Constants.DNS_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see DNS requests"> DNS Services Registration
                                                            <input type="checkbox" value="<%=Constants.DNS_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label> 
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.WIFI_FORM_KEYWORD%>_form" id="<%=Constants.WIFI_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> Wi-Fi Services Registration
                                                            <input type="checkbox" value="<%=Constants.WIFI_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>    
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.WIFI_PORT_FORM_KEYWORD%>_form" id="<%=Constants.WIFI_PORT_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> Wi-Fi Port Services Registration
                                                            <input type="checkbox" value="<%=Constants.WIFI_PORT_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label> 
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.VPN_SINGLE_FORM_KEYWORD%>_form" id="<%=Constants.VPN_SINGLE_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> VPN Single Registration
                                                            <input type="checkbox" value="<%=Constants.VPN_SINGLE_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.VPN_ADD_FORM_KEYWORD%>_form" id="<%=Constants.VPN_ADD_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> VPN Add Registration (VPN id)
                                                            <input type="checkbox" value="<%=Constants.VPN_ADD_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.VPN_RENEW_FORM_KEYWORD%>_form" id="<%=Constants.VPN_RENEW_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see WIFI requests"> VPN Renew Registration (VPN id)
                                                            <input type="checkbox" value="<%=Constants.VPN_RENEW_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.CENTRAL_UTM_FORM_KEYWORD%>_form" id="<%=Constants.CENTRAL_UTM_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Central UTM requests"> Central Utm Registration
                                                            <input type="checkbox" value="<%=Constants.CENTRAL_UTM_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.EMAILACTIVATE_FORM_KEYWORD%>_form" id="<%=Constants.EMAILACTIVATE_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Email Activate requests"> Email Activate Registration
                                                            <input type="checkbox" value="<%=Constants.EMAILACTIVATE_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.EMAILDEACTIVATE_FORM_KEYWORD%>_form" id="<%=Constants.EMAILDEACTIVATE_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Email Deactivate requests"> Email Deactivate Registration
                                                            <input type="checkbox" value="<%=Constants.EMAILDEACTIVATE_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.DOR_EXT_FORM_KEYWORD%>_form" id="<%=Constants.DOR_EXT_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Date of Account Expiry requests"> Date of Account Expiry
                                                            <input type="checkbox" value="<%=Constants.DOR_EXT_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand <%=Constants.DAONBOARDING_FORM_KEYWORD%>_form" id="<%=Constants.DAONBOARDING_FORM_KEYWORD%>_form" style="display:none;"  title="Check this checkbox, if you want to see Date of Account Expiry requests"> Da-Onboarding Registration
                                                            <input type="checkbox" value="<%=Constants.DAONBOARDING_FORM_KEYWORD%>" name="frm">
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="panel-group " >
                                    <div class="panel panel-default" style="border:none;">
                                        <h3 class="theme-heading-h3">Status</h3>
                                        <div id="collapse_3_2" class="panel-collapse ">
                                            <div class="panel-body">
                                                <div class="form-group">
                                                    <!-- title added by pr on 17thjan19 -->
                                                    <div class="mt-radio-list">
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Forwarded by you."> Forwarded Request
                                                            <input type="checkbox" value="forwarded" class="stat" name="stat">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Pending with you."> Pending Request
                                                            <input type="checkbox" value="pending" class="stat" name="stat">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Rejected by you."> Rejected Request
                                                            <input type="checkbox" value="rejected" class="stat" name="stat">
                                                            <span></span>
                                                        </label>
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand" title="Check this checkbox, when you want to see only those requests which are Completed by you."> Completed Request
                                                            <input type="checkbox" value="completed" class="stat" name="stat">
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <!-- start, code added by pr on 8thjan2020 -->                            

                                <%
                                    if (role.equalsIgnoreCase(Constants.ROLE_MAILADMIN) || role.equalsIgnoreCase(Constants.ROLE_SUP)) {
                                %>
                                <div class="search-filter-divider bg-grey-steel"></div>

                                <div class="panel-group " >
                                    <div class="panel panel-default" style="border:none;">
                                        <h3 class="theme-heading-h3">On Hold/Off Hold Requests</h3><!-- text changed by pr on 20thapr2020 -->
                                        <div id="collapse_3_2" class="panel-collapse ">
                                            <div class="panel-body">
                                                <div class="form-group">

                                                    <div class="mt-radio-list"><!-- onclick="clearOther() method added by pr on 27thapr2020 -->
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" onclick="clearOther(this.id)" id="onhold_id" value="onhold" class="stat" name="stat">On Hold Requests
                                                            <span></span>
                                                        </label>&emsp;&emsp;                                                        
                                                    </div>

                                                    <div class="mt-radio-list"> <!-- div added by pr on 20thapr2020  -->
                                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                            <input type="checkbox" onclick="clearOther(this.id)" id="offhold_id" value="offhold" class="stat" name="stat">Off Hold Requests
                                                            <span></span>
                                                        </label>&emsp;&emsp;                                                        
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <%
                                    }
                                %>

                                <!-- end, code added by pr on 8thjan2020 -->                                                                                        

                                <!-- start, code added by pr on 30thjul19  --> 

                                <div class="search-filter-divider bg-grey-steel"></div>

                                <div class="panel-group " >
                                    <div class="panel panel-default" style="border:none;">
                                        <h3 class="theme-heading-h3">Query Raise</h3>
                                        <div id="collapse_3_2" class="panel-collapse ">
                                            <div class="panel-body">
                                                <div class="form-group">

                                                    <div class="mt-radio-list">
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" value="query" class="queryfilter" name="queryfilter"> Query Raised
                                                            <span></span>
                                                        </label>&emsp;&emsp;
                                                        <label class="k-radio k-radio--bold k-radio--brand">
                                                            <input type="radio" value="query_ans" class="queryfilter" name="queryfilter"> Query Answered
                                                            <span></span>
                                                        </label>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <input type="text" name="querydate" placeholder="Search Query in a Date Range" id="querydate_range" class="form-control daterange" autocomplete="off" />                                                              
                                <div class="text-center mt-3">
                                    <button type="button" onclick="setAction('filter');"  class="btn btn-primary apply_filter d-none">Apply Filter</button>
                                </div>



                                <div class="search-filter-divider bg-grey-steel"></div>
                            </div>
                        </div>
                    </div>
                </div>

                <!--end::Portlet-->
            </div>
            <div class="col-lg-9 col-xl-9 order-lg-2 order-xl-1">

                <!--begin::Portlet-->
                <div class="k-portlet k-portlet--height-fluid k-widget-17">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title"><span id="heading_id">Pending Requests</span></h3>
                        </div>
                    </div>
                    <div class="k-portlet__body">
                        <!--begin: Datatable -->
                        <div id="filter-div-checked" class="mb-3"></div>
                        <div class="row mb-3">
                            <div class="col-md-6"> <!-- Div added by pr on 30thjul19  -->
                                <span id="export-file-data" class="caption font-dark">
                                    <button type="button" onclick="setAction('onload', true);"  class="btn btn-primary btn-sm mb-3 onload_ex" id="export-file-btn">Export All</button>
                                </span>
                            </div>
                            <%
                                if (role.equals("sup")) {
                            %>                                                            
                            <div class="col-md-6">
                                <span class="caption-subject bold uppercase">
                                    <a data-toggle="modal" class="btn btn-outline-metal btn-hover-brand btn-upper btn-font-dark btn-sm btn-bold float-right" href="#search_regno_modal" onclick="clearSearch();" >Search a Registration Number</a>
                                    <%
                                        if (userdata.getEmail().equals("support@nic.in") || userdata.getEmail().equals("support@gov.in")) {
                                    %>  
                                    <a class="btn btn-danger  btn-sm btn-bold float-right" href="move_request1" >Move Request</a>
                                    <% } %>

                                </span>
                            </div>
                            <%
                            } else if (role.equals(Constants.ROLE_CO)) // else if added by pr on 28thjan2020
                            {
                            %>
                            <div class="col-md-6"> <!-- Div added by pr on 28thjan2020 -->
                                <span id="export-file-data" class="caption font-dark float-right">

                                    <a href="exportIds" ><button type="button" class="btn btn-primary btn-sm mb-3 onload_ex" id="export-file-btn">Export All IDs Created</button></a>

                                    <!--<button type="button" onclick="window.location.href='exportIds'" class="btn btn-primary btn-sm mb-3 onload_ex" id="export-file-btn">Export All IDs Created</button>-->
                                </span>
                            </div>    
                            <%
                                }
                            %>
                        </div>
                        <table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive dataTable no-footer dtr-inline" id="example">
                            <thead>
                                <tr>
                                    <th>App Id</th>
                                    <th>Applicant <br> Details</th><!--  text modified by pr on 4thmay18  -->
                                    <th>Status</th>
                                    <th>Submission <br />Type</th> <!--  line added by pr on 19thapr18  -->
                                    <th>Date</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>

                            </tbody>
                        </table>
                    </div>
                </div>

                <!--end::Portlet-->
            </div>
        </div>

        <!--end::Row-->

        <!--end::Dashboard 2-->
    </div>
    <input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
    <!-- below line added by pr on 25thjan18  -->
    <input type="hidden" name="comingFrom" id="comingFrom" value="admin" />
    <div id="action_div" style="display: hidden">
    </div>
    <!-- below line added by pr on 3rdjan18 to implement CSRF  -->  
    <input type="hidden" name="random" value="1" id="random" />                   
    <input type="hidden" name="role" id="role" value="<%=role%>" /> 
    <input type="hidden" name="action_hidden" id="action_hidden" value="" /> <!-- line added by pr on 25thmay18  -->                                                               
    <input type="hidden" name="loggedin_email" id="loggedin_email" value="<%=loggedin_email%>" />   
    <!-- end:: Content Body -->
</div>
<div id="filter-btn" class="k-header__topbar-item k-header__topbar-item--quick-panel filter-btn" data-toggle="k-tooltip" title="" data-placement="right" data-original-title="Filter Panel">
    <span class="k-header__topbar-icon" id="k_quick_panel_toggler_btn">
        <i class="fa fa-filter fa-2x"></i>
    </span>
</div> 
<!-- end:: Content -->
<jsp:include page="include/new_include/footer.jsp" />
<!-- end:: Footer -->

<!-- /.modal-dialog -->
<div class="modal fade" id="forward_modal">
    <div class="modal-dialog modal-lg single-page">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Action</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <!-- start, div shows the esign related options  -->
                <div class="portlet-body" >
                    <div id="forward_esign_div"><!-- div to show with or without using esign, div replaced by pr on 12thapr18  -->
                        <div class="row" style="color:red;padding:2px;margin:2px;"></div>
                        <div class="form-group" style="padding:15px;" id="esign1">
                            <!--                                    <label><strong>Please select any to proceed:</strong></label>-->
                            <div class="mt-radio-list" id="esign_div">
                                <div class="col-md-12 mb-4">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" id="esign_type" value="esign" name="esign_type"> e-Sign the document with Aadhaar?
                                        <span></span>
                                    </label>
                                    <span style="font-size:10px;color:red;display: block;margin-left: 27px;">(Delivery of e-sign with aadhaar depends on platforms outside control of NIC. In case of delay, you may choose to proceed online without aadhaar)</span>
                                </div>
                                <div class="col-md-12 mb-3">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" value="online" name="esign_type" checked> Proceed online
                                        <span></span>
                                    </label>
                                </div>
                                <input type="hidden" id="role" value="role" />
                            </div>
                            <font style="color:red"><span id="esign_error"></span></font>
                        </div>
                        <div class="row">
                            <div class="col-md-12 text-right">
                                <div class="form-action text-center">
                                    <!-- below line added by pr on 22ndmar18  -->
                                    <input type="hidden" name="app_type" id="app_type" />
                                    <input type="hidden" name="app_ca_path" id="app_ca_path" /> <!--  line added by pr on 4thjun18 -->
                                    <input type="hidden" name="app_ca_type" id="app_ca_type" /> <!--  line added by pr on 4thjun18 -->                                            
                                    <!-- <button class="btn btn-info btn-circle btn-block" data-toggle="modal" href="#done" type="button"><i class="fa fa-check"></i> Continue</button>-->                                                                    
                                    <button class="btn btn-info" id="fwd_wo_ad" type="button"><i class="fa fa-check"></i> Continue</button>
                                    <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- start, forward from support  -->
                    <div class="box-body" id="forward_from_support_div" style="display:none;">            
                        <div class="bs-example">
                            <div class="panel-group" id="accordion">
                                <div class="panel panel-default">
                                    <div class="panel-heading">
                                        <h4 class="panel-title">
                                            <a data-toggle="collapse" id="one" data-parent="#accordion" href="#collapseOne" class="alert alert-info" style="font-size: 16px;font-weight: 500;">
                                                Forward to Coordinator
                                            </a>
                                        </h4>
                                    </div>
                                    <div id="collapseOne" class="panel-collapse collapse in show">
                                        <div id="one_body" class="panel-body">
                                            <div class="col-md-12 emp_forward_detail">
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="Coordinator"><strong>Employment Details</strong></label>
                                                            <div class="ajaxEmpDetails"></div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label for="Coordinator"><strong>Choose one of the two Radio buttons</strong></label><br/>
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" class="" name="forwardtoda" id="mappedradio" value="mapped" onclick="cleartestdatahide();" checked> Mapped DA/Coordinator
                                                                <span></span>
                                                            </label><br />
                                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                                <input type="radio" class="" name="forwardtoda" id="simplyforwardradio" onclick="cleartestdata();"  value="simplyforward" > Simply Forward
                                                                <span></span>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row mt-3">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label for="Coordinator">Choose Coordinator <span style="color: red">*</span></label>
                                                        <select class="form-control" id="choose_da_type" name="choose_da_type" >
                                                            <option value="" >-SELECT-</option>
                                                            <!--<option value="da" >DA-Admin</option>
                                                            <option value="c" >Coordinator</option>--> <!-- commented by pr on 23rdmar18  -->
                                                        </select>
                                                        <font style="color:red"><span id="fwd_coord_error"></span></font>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-md-12" id="choose_fwd_div" ><!--  id added by pr on 16thmar18  -->
                                                    <div class="form-group">
                                                        <div style="display:none;" class="alert alert-danger" id="empty-coordlist"></div>
                                                    </div>
                                                    <div class="form-group" id="choose_fwd_div_detailedData">
                                                        <label for="Coordinator">Select Coordinator (you can select multiple Coordinators) <span style="color: red">*</span></label>
                                                        <select  multiple="multiple" class="form-control" id="fwd_coord" name="fwd_coord" >
                                                            <option value="" >-SELECT-</option>
                                                        </select>
                                                        <font style="color:red"><span id="fwd_coord_error"></span></font>
                                                    </div>
                                                    <div class="form-group" id="simplyforward" style="display:none;">
                                                        <label for="Coordinator">Enter the email address to whom you want to forward the request<span style="color: red">*</span></label>
                                                        <input type="text" name="fwd_coord" class="form-control" id="enter_da_co" >
                                                        <font style="color:red"><span id="enter_da_co_error"></span></font>
                                                    </div>
                                                    <div class="form-group text-center">
                                                        <button type="button" name="fwd_coord_btn" id="fwd_coord_btn" class="btn btn-primary">Submit</button>
                                                        <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%   // commented by pr on 15thmar18
                                // bellow if added by pr on 16thmar18
                                if (loggedin_email.equals("rajp@nic.in") || loggedin_email.equals("nfo7.nhq-dl@nic.in")
                                        || loggedin_email.equals("nfo19.sp-dl@nkn.in") || loggedin_email.equals("priya.nhq@nic.in")) {
                            %>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a data-toggle="collapse" class="btn btn-primary btn-sm mb-3 mt-4" id="two" data-parent="#accordion" href="#collapseTwo">
                                        Add Coordinator
                                    </a>
                                </div>
                                <div id="collapseTwo" class="panel-collapse collapse">
                                    <div id="two_body" class="panel-body">
                                        <form>
                                            <div class="row">
                                                <!-- below div added by pr on 12thmar18  -->
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label for="Coordinator">Choose DA-Admin/Coordinator <span style="color: red">*</span></label>
                                                        <select class="form-control" id="choose_da_type_add" name="choose_da_type_add" >
                                                            <option value="" >-SELECT-</option>
                                                            <option value="da" >DA-Admin</option>
                                                            <option value="c" >Coordinator</option>
                                                        </select>
                                                        <font style="color:red"><span id="fwd_coord_add_error"></span></font>
                                                    </div>
                                                </div>
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <div style="display:none;" class="alert alert-danger"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator"><strong>Employment Details</strong></label>
                                                        <!--                                                            <div id="ajaxEmpDetails">Employment Details Here</div>-->
                                                        <div class="ajaxEmpDetails">Employment Details Here</div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator"><strong>Add <span id="coordType">DA-Admin/Coordinator</span></strong> <span style="color: red">*</span></label>
                                                        <input type="text" class="form-control" name="" value="" id="add_coord" />
                                                        <font style="color:red"><span class="hideerr" id="add_coord_error"></span></font>
                                                    </div>
                                                    <div class="form-group text-center">
                                                        <button type="button" name="add_coord_btn" id="add_coord_btn" class="btn btn-success">Submit</button>
                                                        <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div><% } %>
                        </div>
                    </div>
                </div>
                <div class="box-body" id="forward_from_coord_div" style="display:none;">            
                    <!-- MODAL FORM START -->
                    <div class="panel panel-default panel-shadow"><!-- to apply shadow add class "panel-shadow" -->
                        <div class="col-md-12">
                            <h4>Forward to Admin</h4>
                            <div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label for="street">Select Admin<span style="color: red">*</span></label>
                                            <select multiple="multiple" class="form-control" id="fwd_madmin" style="height:100px;" name="fwd_madmin" >
                                                <option value="">Select Admin</option>
                                            </select>
                                            <font style="color:red"><span id="madmin_error"></span></font>
                                        </div>
                                        <div class="form-group text-center">
                                            <button type="button" id="fwd_madmin_btn" class="btn btn-primary">Forward</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.box-body -->
                </div>                            
                <!-- end, forward from coordinator  -->
            </div><!-- /.box-body -->
            <div class="modal-footer" >
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- end, div shows the esign related options  -->
    </div>
</div>
<!--  start, aadhaar modal added by pr on 12thapr18  -->
<div class="modal fade bs-modal-lg" id="esign" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="max-width: 550px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">E-sign</h4>
                <input type="hidden" name="ref_num" id="ref_num" />
                <input type="hidden" name="formtype" id="formtype" />
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
                    <button type="button" class="btn  red closecls" data-dismiss="modal">Close</button>
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
                    <button type="button" class="btn  green" id="esign_2admin" >Submit</button>
                    <img src="assets/images/loading.gif" id="esign_img2" style="height:60px; display: none">
                    <button type="button" class="btn  red closecls" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!--  end, aadhaar modal added by pr on 12thapr18  -->   
<!-- start, forward from mailadmin  --> <!-- below modal added by pr on 6thmar18  -->
<div class="modal fade" id="madmin_forward_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Action</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <!-- start, div shows the esign related options  -->
                <!-- start, forward from support  -->
                <div class="box-body">            
                    <div class="bs-example">
                        <div class="panel-group" id="accordionda">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a data-toggle="collapse" id="oneda" data-parent="#accordionda" href="#collapseOneDA" class="btn btn-secondary">
                                        Forward to DA-Admin <!-- text changed by pr on 6thfeb18  -->
                                    </a>
                                </div>
                                <div id="collapseOneDA" class="panel-collapse collapse in show">
                                    <div id="oneda_body" class="panel-body">			            
                                        <form method="post" action="">
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <div style="display:none;" class="alert alert-danger"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator">Select DA-Admin<span style="color: red">*</span></label>
                                                        <select  multiple="multiple" class="form-control" id="fwd_da" name="fwd_da" >
                                                            <option value="" >-SELECT-</option>
                                                        </select>
                                                        <font style="color:red"><span id="fwd_da_error"></span></font>
                                                    </div>
                                                    <div class="form-group">
                                                        <button type="button" name="fwd_coord_btn" id="fwd_da_btn" class="btn btn-primary float-right">Submit</button>
                                                        <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <%
                                // commented by pr on 15thmar18  
                                if (loggedin_email.equals("rajp@nic.in") || loggedin_email.equals("nfo7.nhq-dl@nic.in")
                                        || loggedin_email.equals("nfo19.sp-dl@nkn.in") || loggedin_email.equals("priya.nhq@nic.in")) {
                            %>
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h4 class="panel-title">
                                        <a data-toggle="collapse" id="twoda" data-parent="#accordionda" href="#collapseTwoDA">
                                            <b>Add DA-Admin</b>
                                        </a>
                                    </h4>
                                </div>
                                <div id="collapseTwoDA" class="panel-collapse collapse">
                                    <div id="twoda_body" class="panel-body">
                                        <form>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <div style="display:none;" class="alert alert-danger"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator"><strong>Employment Details</strong></label>
                                                        <!--<div id="ajaxEmpDetailsMAdmin">Employment Details Here</div>-->
                                                        <!--  above line commented below added by pr on 23rdapr18  -->
                                                        <div class="ajaxEmpDetailsMAdmin">Employment Details Here</div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator"><strong>Add Da-Admin</strong> <span style="color: red">*</span></label>
                                                        <input type="text" class="form-control" name="" value="" id="add_da" />
                                                        <font style="color:red"><span class="hideerr" id="add_da_error"></span></font>
                                                    </div>
                                                    <div class="form-group">
                                                        <button type="button" name="add_coord_btn" id="add_da_btn" class="btn btn-primary">Submit</button>
                                                        <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <% }%>
                            <!--  start,  div added by pr on 23rdapr18  -->
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <a data-toggle="collapse" id="threeda" data-parent="#accordionda" href="#collapseThreeDA" class="btn btn-secondary">
                                        Forward Directly to DA-Admin
                                    </a>
                                </div>
                                <div id="collapseThreeDA" class="panel-collapse collapse">
                                    <div id="threeda_body" class="panel-body">
                                        <form>
                                            <div class="row">
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <div style="display:none;" class="alert alert-danger"></div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator"><strong>Employment Details</strong></label>
                                                        <div id="ajaxEmpDetailsMAdmin">Employment Details Here</div>
                                                        <!--  above line commented below added by pr on 23rdapr18  -->
                                                        <div class="ajaxEmpDetailsMAdmin">Employment Details Here</div>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="Coordinator"><strong>Forward to Da-Admin</strong> <span style="color: red">*</span></label>
                                                        <input type="text" class="form-control" name="da_from_admin" value="" id="da_from_admin" />
                                                        <font style="color:red"><span class="hideerr" id="da_from_admin_error"></span></font>
                                                    </div>
                                                    <div class="form-group">
                                                        <button type="button" name="fwd_m_da_btn" id="fwd_m_da_btn" class="btn btn-primary float-right">Forward</button>
                                                        <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <!--  end,  div added by pr on 23rdapr18  -->
                        </div>
                    </div>	               
                </div>
                <!-- end, forward from support  -->
                <!-- start, forward from coordinator  -->
                <!-- end, forward from coordinator  -->
            </div>
            <!-- end, div shows the esign related options  -->
        </div>
        <div class="modal-footer" >
            <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
        </div>
    </div>
    <!-- /.modal-content -->
</div>
<!-- end, forward from mailadmin  -->
<div class="modal fade bd-modal-lg" id="fileUpload" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4>Upload Scanned Form</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="alert alert-warning" style="padding: 0;">
                    <ul class="pt-3">
                        <li>Only PDF format files are allowed. </li>
                        <li>maximum file size per upload to 1 MB.</li>
                    </ul>
                </div>
                <div class="portlet-body">
                    <div class="col-md-12 p-0">
                        <form action="" method="post" class="track_upload" class="form_val" >
                            <input type="hidden" value="" id="form_val">
                            <input type="hidden" value="" id="ref_no">
                            <input type="hidden" value="CA" id="panel">
                            <div class="form-group">
                                <label>Please Select Your File</label>
                                <div class="custom-file">
                                    <input type="file" class="custom-file-input" name="cert_file" id="cert_file">
                                    <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                    <font style="color:red" id="file_cert_err"></font><!-- span made div by pr on 1stnov18 -->
                                    <a href="javascript:;" class="close fileinput-exists" data-dismiss="fileinput"> </a>
                                    <span class="fileinput-filename"></span>
                                </div>
                            </div>	
                        </form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn dark btn-default" data-dismiss="modal" id="scan_mod_close">Close</button>
                <button type="button" class="btn btn-primary upload_cert" >Upload</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="include/multiple-upload.jsp" />  
<!-- start, reject modal -->
<div class="modal fade" id="reject_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" style="font-weight:400px;">Reject Action</h4><!-- strong added and plcaholder added for below textarea  -->
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <form role="form">
                        <div class="form-body">
                            <div class="form-group">
                                <label>Add Remarks <span style="color:red;">*</span></label>
                                <!-- maxlength="500"  added by pr on 14thjun18  -->
                                <textarea class="form-control" maxlength="500"  name="statRemarks"  id="statRemarks" placeholder="<%=Constants.STAT_REMARKS_TEXT%>"  rows="3"></textarea><!--  modified by pr on 19thfeb18  --><!--  & allowed by pr on 19thapr18, placeholder got from Constant by pr on 28thdec18  -->
                            </div>
                            <font style="color:red"><span id="statRemarksError"></span></font><!-- line added by pr on 19thfeb18 -->
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="reject_btn" class="btn btn-danger">Reject</button>
                <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!--  below div added by pr on 4thjun18  -->
<div class="modal fade" id="approve_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" style="font-weight:400px;"></h4><!-- strong added and plcaholder added for below textarea  -->
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <form role="form">
                        <div class="form-body">
                            <div class="form-group">
                                <label>Add Remarks</label>
                                <!-- maxlength="500"   added by pr on 14thjun18 -->
                                <textarea class="form-control" maxlength="500"  id="statRemarksApp"  placeholder="<%=Constants.STAT_REMARKS_TEXT%>"  rows="3"></textarea><!--  modified by pr on 19thfeb18  --><!--  & allowed by pr on 19thapr18, placeholder got from Constant by pr on 28thdec18  -->
                            </div>
                            <font style="color:red"><span class="statRemarksError"></span></font><!-- line added by pr on 19thfeb18 -->
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="approve_btn" class="btn btn-success">Approve</button> 
                <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!--  below div added by pr on 6thjun18  -->
<div class="modal fade" id="revert_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <form role="form">
                        <div class="form-body">
                            <div class="form-group">
                                <label>Add Remarks <span style="color:red;"></span></label><!-- * removed by pr on 1stnov19 -->
                                <!-- maxlength="500" added by pr on 14thjun18 -->
                                <textarea class="form-control" id="statRemarksRev" maxlength="500" placeholder="<%=Constants.STAT_REMARKS_TEXT%>"  rows="3"></textarea><!--  modified by pr on 19thfeb18  --><!--  & allowed by pr on 19thapr18, placeholder got from Constant by pr on 28thdec18  -->
                            </div>
                            <font style="color:red"><span class="statRemarksError"></span></font><!-- line added by pr on 19thfeb18 -->
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="revert_btn" class="btn btn-primary">Forward</button>  
                <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!--  below code added by pr on 18thapr18 , approve_modal -> app_modal by pr on 4thjun18,app_modal by pr on 10thjuly18 -->
<div class="modal fade" id="app_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4>Manual Process Approve Steps</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    Since user had opted for manual process while submitting the form, you will have to download the user's scanned copy first by clicking the "Download Docs uploaded by user" Link ( under "Actions" ) and then upload it back by clicking the "Upload Scanned Form" link ( under "Actions" ) after putting your seal and sign to approve the request.
                </div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- start, on hold modal added by pr on 8thjan2020 -->
<div class="modal fade" id="onhold_modal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" style="font-weight:400px;"><span id="onhold_title"></span></h4><!-- strong added and plcaholder added for below textarea  -->
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <form role="form">
                        <div class="form-body">
                            <div class="form-group" id="onhold_add_remark">
                                <!--<h3>Add Remarks</h3>-->

                                <textarea class="form-control" maxlength="500"  name="statRemarks" id="hold_statRemarks" class="statRemarks" placeholder="<%=Constants.STAT_REMARKS_TEXT%>"  rows="3"></textarea>
                            </div>
                            <div class="form-group" id="onhold_show_remark" style="display:none;">
                                <!--<h3>Remarks Added</h3>-->

                                <div id="show_remark"></div>

                            </div>
                            <font style="color:red"><span class="statRemarksError"></span></font><!-- line added by pr on 19thfeb18 -->
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" >

                <input type="hidden" name="hold_type" id="hold_type" value="" >

                <input type="hidden" name="hold_reg_no" id="hold_reg_no" value="" >

                <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                <button type="button" id="onhold_btn" onclick="putOnHold();" class="btn btn-primary float-center"></button>
                <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 20thapr18 -->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- end, on hold modal added by pr on 8thjan2020 -->  

<!--  below div for modal added by pr on 6thapr18  -->
<div class="modal fade" id="query_raise">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <!--<div class="portlet-body form" id="query_table" style="overflow:auto;width:400px;height:400px;">-->
                <div class="portlet-body form" id="query_table"> 

                </div>                    
                <div class="portlet-body form" id="showQueryAdd" style="display:none;">
                    <form role="form">
                        <div class="row">
                            <div class="col-6">
                                <div class="form-group">
                                    <label>Choose Recipient</label>
                                    <select class="form-control" name="choose_recp" id="choose_recp">
                                        <option value="">Select Recipient</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-10 pr-0">
                                <div class="form-group">
                                    <label>Raise a Query</label>
                                    <textarea class="form-control" name="query" maxlength="500"  id="query" placeholder="Enter Your Query."  rows="3"></textarea>
                                    <font style="color:red"><span id="queryError"></span></font>
                                </div>
                            </div>
                            <div class="col-2 pl-0">
                                <div class="form-group">

                                    <button type="button" id="query_btn" class="btn btn-info float-right">Send&ensp;<i class="fa fa-paper-plane"></i></button>
                                    <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none">
                                </div>
                            </div>                            
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer" >
                <ul class="color-indicator">
                    <li title="Applicant"><div class="color-tag u-div"></div>&nbsp;User</li>
                    <li title="Co-Ordinator"><div class="color-tag c-div"></div>&nbsp;CO</li>
                    <li title="Reporting/Nodal/Forwarding Officer"><div class="color-tag ca-div"></div>&nbsp;RO/FO/Nodal</li>
                    <li title="Admin"><div class="color-tag m-div"></div>&nbsp;Admin</li>
                    <li title="Support"><div class="color-tag s-div"></div>&nbsp;Support</li>
                    <li title="Delegated Admin"><div class="color-tag d-div"></div>&nbsp;DA</li>
                </ul>
                <button type="button" class="btn dark btn-default" id="query_close_btn" data-dismiss="modal">Close</button> <!--  id added by pr on 18thjun18 -->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- start, create id modal  -->
<div class="modal fade" id="create_modal">
    <div class="modal-dialog modal-lg single-page">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="portlet-body" >
                    <!-- start, for single and nkn forms create email div  -->
                    <div class="box-body" id="create_email_div" style="display:none;">            
                        <!-- MODAL FORM START -->
                        <div class="panel panel-default panel-shadow">
                            <div class="alert alert-primary"><span style="font-size: 16px;">Create Email Account</span></div>
                            <div class="form-group">
                                <div style="display:none;" class="alert alert-danger"></div>
                            </div>
                            <div class="row" id="single_emp_type">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="street">Employment Type</label>
                                        <div class="col-sm-offset-1">
                                            <div class="emp_type_span"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row mb-4">
                                <div class="col-md-10">
                                    <div class="form-group">
                                        <label for="street">Preferred Email</label>
                                        <div class="col-sm-offset-1">
                                            <div id="pref_email_span"></div>
                                        </div>
                                    </div>
                                    <div id="dom_err"></div><!-- line added by pr on 5thdec19 -->
                                </div>
                                <div class="col-md-2">
                                    <div class="form-group"><button class="bulk_mobile btn btn-primary float-right" >Emails Linked to Mobile</button></div>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-12">
                                    <label for="street">Select Account as FREE/PAID<span style="color: red">*</span></label>
                                    <div class="col-sm-offset-1 mt-2 mb-2">
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="description" id="email_desc1"  value="free" /> FREE
                                            <span></span>
                                        </label>&emsp;&emsp;&emsp;
                                        <label class="k-radio k-radio--bold k-radio--brand">
                                            <input type="radio" name="description" id="email_desc2"  value="paid"  /> PAID
                                            <span></span>
                                        </label>
                                        <font style="color:red"><span></span></font>
                                    </div>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-4">
                                    <label for="street">Select PO<span style="color: red">*</span></label>
                                    <select name="po" id="po" class="form-control">
                                        <option value="">-SELECT PO-</option>
                                    </select>
                                    <font style="color:red"><span ></span></font>
                                </div>
                                <div class="col-md-4">
                                    <label for="street">Select BO<span style="color: red">*</span></label>
                                    <select name="bo" id="bo" class="form-control">
                                        <option value="">-SELECT BO-</option>
                                    </select>
                                    <font style="color:red"><span ></span></font>
                                </div>
                                <div class="col-md-4">
                                    <label for="street">Select Domain<span style="color: red">*</span></label>
                                    <select name="domain" id="domain" class="form-control">
                                        <option value="">-SELECT Domain-</option>
                                    </select>
                                    <font style="color:red"><span></span></font>
                                </div>
                            </div>
                            <div class="row form-group">
                                <div class="col-md-4">
                                    <label for="street"><strong>Final UID</strong></label>
                                    <input type="text" name="final_email_id" id="final_email_id" onkeyup="showAlias();" placeholder="Only Alphanumeric with . and - allowed." class="form-control" />
                                </div>
                                <div class="col-md-4">
                                    <label for="street"><strong>Primary Email (prefix before @ Domain)</strong></label>
                                    <input type="text" name="primary_id" id="primary_id" placeholder="Only Alphanumeric with . and - allowed." class="form-control" />
                                </div><!-- field added by pr on 19thfeb18  -->
                                <div class="col-md-4">
                                    <label for="street"><strong>Mail Equivalent Address</strong></label>
                                    <input readonly="" type="text" name="alias_id" id="alias_id" placeholder="MailEquivalentAddress" class="form-control" />
                                </div><!-- field added by pr on 19thfeb18  -->
                            </div> 

                            <div class="row form-group"><!-- div added by pr on 3rdoct19  -->
                                <div class="col-md-12">
                                    <label for="street">Remarks<span style="color: red"></span></label><!-- * removed by pr on 1stnov19 -->
                                    <textarea class="form-control statRemarks" name="statRemarks" placeholder="Enter Remarks"></textarea>
                                </div>
                            </div>

                            <div class="form-group mt-5">
                                <input type="hidden" name="emp_type_hid" id="emp_type_hid" value="" /> <!-- line added by pr on 1stmay19 -->
                                <div class="text-center"><button type="button" id="create_email_btn" class="btn btn-primary">Create ID</button></div>
                                <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 19thapr18 -->
                            </div>
                        </div>
                    </div><!-- /.box-body -->
                </div>
                <!-- end, for single and nkn forms create email div  -->
                <!-- start, for sms form create auth div  -->
                <div class="box-body" id="create_sms_div" style="display:none;">            
                    <!-- MODAL FORM START -->
                    <div class="panel panel-default panel-shadow"><!-- to apply shadow add class "panel-shadow" -->
                        <div class="panel-heading" style="background-color: grey;">
                            <div class="panel-title" style="color:white;font-weight:bold;">Create SMS Auth ID</div>
                        </div>
                        <div>
                            <!-- below div added by pr on 13thmar18  --> 
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="street">Select Account as FREE/PAID<span style="color: red">*</span></label>
                                        <div class="col-sm-offset-1">
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="description" id="sms_desc1"  value="free" checked autofocus /> FREE
                                                <span></span>
                                            </label>&emsp;&emsp;&emsp;
                                            <label class="k-radio k-radio--bold k-radio--brand">
                                                <input type="radio" name="description" id="sms_desc2"  value="paid"  /> PAID
                                                <span></span>
                                            </label>
                                            <font style="color:red"><span></span></font>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="street"><strong>Primary Business Organization (PO)</strong>: Application Services</label>
                                    </div>
                                    <div class="form-group">
                                        <label for="street"><strong>Business Organization (BO)</strong>: inoc services</label>
                                    </div>
                                    <div class="form-group">
                                        <label for="street"><strong>Final UID</strong></label>
                                        <input type="text" name="final_sms_id" id="final_sms_id" placeholder="Alphabets allowed" maxlength="24" class="form-control" /> <!--  maxlength added by pr on 21staug18  -->
                                    </div>
                                    <div class="form-group">
                                        <button type="button" id="create_sms_btn" class="btn btn-primary float-center">Create ID</button>
                                        <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 19thapr18 -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div><!-- /.box-body -->
                </div>                            
                <!-- end, for sms form create auth div  -->
                <!-- start, for mark as done div  -->
                <div class="box-body" id="mark_done_div" style="display:none;">            
                    <!-- MODAL FORM START -->
                    <div class="panel panel-default panel-shadow"><!-- to apply shadow add class "panel-shadow" -->
                        <div class="alert alert-primary"><span style="font-size: 16px;">Mark as Done</span></div>
                        <div id="wifi_select" style="display: none;">
                            <p id="wifi_att"></p>
                            <p id="wifi_process"></p>
                            <p id="cert_mail_sent"></p>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="street">Wifi Value<span style="color: red">*</span></label>
                                        <select class="form-control" name="wifi_value" id="wifi_value"><!-- class added by pr on 11thdec18 -->
                                            <option value="">Select</option>
                                            <option value="1">LEHAR(1)</option>
                                            <option value="30">ANANT(30)</option>
                                            <option value="7">SARAS(7)</option>
                                            <option value="23">DEVICE(23)</option>
                                            <option value="2">VAYU(2)</option>
                                        </select>
                                        <font style="color:red"><span id="wifi_error"></span></font>
                                    </div>
                                </div>
                            </div>
                            <div class="row"> <!--  div added by pr on 7thdec18 for wifi -->
                                <div class="col-md-12"> 
                                    <div class="form-group">

                                        <!-- below line commented by pr on 24thapr19, uncommented by pr on 1stmay19  -->

                                        <input type="button" class="btn btn-info btn-block" name="" id="update_wifi_att" value="Update WIFI Attribute in LDAP and/or Generate Certificate & Send Mail to VPN Team" />
                                    </div>
                                </div>                                            
                            </div>
                        </div>                                    
                        <!--  end, code added by pr on 9thaug18  -->  
                        <!-- start, code added by pr on 31staug18 to show users selectde protocol and the current protocol in the LDAP  -->
                        <div id="imap_mad" style="display:none;" ><!--  imap_mad -> imap mark as done div  -->
                            <h6 id="imap_att" ></h6> <!-- to show the current value in the LDAP --->
                            <div id="imap_select" > <!-- to show the value chosen by the user from the front end form filling  -->
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label for="street">Protocol Value Chosen by Applicant: <h6 name="protocol_value" style="display:inline;" id="protocol_value"></h6></label>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">                                        
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                                <input type="checkbox" name="imap_update_ldap" value="1" id="imap_update_ldap" style="display: inline;" /> Check if not to be updated in LDAP
                                                <span></span>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                            </div>             
                        </div>
                        <div id="firewall_mad" style="display:block;" ><!--  firewall_mad -> firewall mark as done div  -->
                            <h4 id="imap_att" ></h4> <!-- to show the current value in the LDAP --->
                            <div id="imap_select" > <!-- to show the value chosen by the user from the front end form filling  -->
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label class="control-label" for="street">Shastri Park: <input type="checkbox" style="display:inline;" name="location" value="shastri park" /></label>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label class="control-label" for="street">CGO Complex: <input type="checkbox" style="display:inline;" name="location" value="cgo complex" /></label>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label class="control-label" for="street">Hyderabad: <input type="checkbox" style="display:inline;" name="location" value="hyderabad" /></label>
                                        </div>
                                    </div>

                                </div>                                            
                            </div>             
                        </div>
                        <!-- end, code added by pr on 31staug18 to show users selectde protocol and the current protocol in the LDAP  -->
                        <div id="emailact_mad" style="display:block;" ><!--  emailact_mad -> email act mark as done div  -->

                            <div>
                                <label for="street" ><span id="dor_text">Employment Type :</span> </label>
                                <span class="emp_type"></span>
                                <input type="hidden" id="emp_type_hid" />
                            </div>

                            <div id="show_dor" style="display:none">

                                <div id="emailact_pdf"></div>

                                <div>
                                    <label for="street" ><span id="dor_text">Date Of Retirement(DOR)</span> <span style="color: red">*</span></label>
                                    <i class="icon-calendar"></i><input class="form-control date-picker" type="text" name="single_dor" id="single_dor7" placeholder="Enter Date Of Retirement [DD-MM-YYYY]" readonly />
                                    <font style="color:red"><span id="single_dor_err"></span></font>
                                </div>

                            </div>

                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                <div class="form-group">
                                    <label for="street">Remarks<span style="color: red">*</span></label>
                                    <!--  maxlength="500"   added by pr on 14thjun18 -->
                                    <textarea class="form-control" name="doneRemarks" maxlength="500"  placeholder="<%=Constants.STAT_REMARKS_TEXT%>"  id="doneRemarks"  rows="3"></textarea><!--  & allowed by pr on 19thapr18 ? allowed by pr on 18thjul18, placeholder got from Constant by pr on 28thdec18  -->
                                    <font style="color:red"><span id="doneRemarks_error"></span></font>
                                </div>
                                <div class="form-group text-center mt-4">
                                    <button type="button" id="mark_done_btn" class="btn btn-primary">Mark as Done</button>
                                    <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 19thapr18 -->
                                </div>
                            </div>
                        </div>
                    </div><!-- /.box-body -->
                </div>                            
                <!-- end, for mark as done div  -->
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- end, create id modal   -->
<!-- end, reject modal -->
<!-- start, reject modal -->
<div class="modal fade" id="bulk_modal">
    <div class="modal-dialog modal-lg single-page">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Bulk Users</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <div id="showRejectData"></div> <!-- line added by pr on 28thnov19  -->
                    <div class="alert alert-primary"><span style="font-size: 16px;">Choose PO/BO/Domain Details</span></div>
                    <div class="form-group">
                        <div style="color:red;"></div><!-- show error here -->
                    </div>
                    <!-- below div added by pr on 1stmay19  -->
                    <div class="row form-group" id="bulk_emp_type">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="street">Employment Type:&emsp;<span class="emp_type_span"></span></label>
                            </div>
                        </div>
                    </div>


                    <!-- below div added by pr on 13thmar18  --> 
                    <div class="row form-group">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="street">Select Account as FREE/PAID<span style="color: red">*</span></label>
                                <div class="col-sm-offset-1">
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="description" id="bulk_desc1"  value="free" checked autofocus /> FREE
                                        <span></span>
                                    </label>&emsp;&emsp;&emsp;
                                    <label class="k-radio k-radio--bold k-radio--brand">
                                        <input type="radio" name="description" id="bulk_desc2"  value="paid"  /> PAID
                                        <span></span>
                                    </label>
                                    <font style="color:red"><span></span></font>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row form-group">
                        <div class="col-md-4">
                            <label for="street">Select PO<span style="color: red">*</span></label>
                            <select name="bulkpo" class="form-control">
                                <option value="">-SELECT-</option>
                            </select>
                            <font style="color:red"><span ></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Select BO<span style="color: red">*</span></label>
                            <select name="bulkbo" class="form-control bo">
                                <option value="">-SELECT BO-</option>
                            </select>
                            <font style="color:red"><span ></span></font>
                        </div>
                        <div class="col-md-4">
                            <label for="street">Select Domain<span style="color: red">*</span></label>
                            <select name="bulkdomain" class="form-control domain">
                                <option value="">-SELECT Domain-</option>
                            </select>
                            <font style="color:red"><span ></span></font>
                        </div>
                    </div>

                    <div class="row form-group"><!-- div added by pr on 3rdoct19  -->
                        <div class="col-md-12">
                            <label for="street">Remarks<span style="color: red">*</span></label>
                            <textarea class="form-control statRemarksBulk" name="statRemarks" placeholder="Enter Remarks"></textarea>
                        </div>
                    </div>

                    <!-- start, table -->
                    <div class="k-portlet__head">
                        <h3 class="theme-heading-h3-popup">Choose Emails To be Created</h3>
                    </div>
                    <div class="alert alert-danger" style="display:none;" id="bulkUpdateMessage"></div><!-- div added by pr on 9thmar18  -->
                    <div class="table-height">
                        <table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive nowrap" id="example_bulk">
                            <thead>
                                <tr>
                                    <th> S.No. </th><!--  line added by pr on 18thapr18  -->
                                    <th> Bulk Id </th>
                                    <th> UID </th>
                                    <th> Email </th>
                                    <th> Mobile </th>
                                    <th> UID already <br>
                                        linked with <br>Mobile </th><!--  modified by pr on 29thmay18  -->
                                    <th> Reject Remarks </th><!--  added by pr on 29thmay18  -->
                                    <th> Date </th>
                                    <th> <input type="checkbox" name="check_all" id="check_all" /> </th>
                                </tr>
                            </thead>
                            <tbody id="bulk_users_tbody" style="display: none;"> <!-- display added by pr on 30thmay18  -->
                                <!-- AJAX Response here  --> 
                            </tbody>
                        </table>
                    </div>
                    <!-- end, table -->
                </div>
            </div>
            <div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
                <button type="button" id="bulk_create_btn" class="btn btn-success">Create Email ID</button>
                <button type="button" id="bulk_reject_btn" class="btn btn-danger">Reject</button><!-- button added by pr on 4thapr18  -->
                <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- img added by p r on 19thapr18 -->
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="bulk_mobile">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h6>UID/Email/MailEquivalentAddress Linked to the Mobile</h6>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div id="mobile_ldap_email" class="modal-body">
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- start, code added by pr on 27thdec17  -->
<div class="modal fade" id="bulk_view_modal">
    <div class="modal-dialog modal-lg single-page">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">View Bulk Users</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <!-- start, table -->
                    <div class="alert alert-primary"><span style="font-size: 16px;" id="bulk_view_heading">Bulk User Subscription Form</span></div>
                    <div id="table_id">
                    </div>  
                    <!-- end, table -->
                </div>
            </div>
            <div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- end, code added by pr on 27thdec17  -->
<!-- start, code added by pr on 2ndapr18  -->
<div class="modal fade" id="bulk_upload_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Update Bulk Users by Uploading</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <!-- start, table -->
                    <div class="alert alert-primary"><span style="font-size: 16px;">Upload Bulk Users</span></div>
                    <div id="bulk_errmsg" class="row">
                    </div>
                    <div class="row">
                        <div class="col-md-12 form-group">
                            <label>Select CSV File to Upload <span style="color: red">*</span></label>
                            <div></div>
                            <div class="custom-file">
                                <input type="file" class="custom-file-input" name="csv_file" id="csv_file" />
                                <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                <font style="color:red"><span id="csv_file_error" ></span></font>
                            </div>
                            <input type="hidden" id="cert" value="">
                        </div>
                        <div class="col-md-12 text-center">
                            <img src="assets/images/loading.gif" class="loadergif" style="height:60px; display: none"><!-- line added by pr on 17thdec19  -->
                            <input type="button" id="bulk_upload" class="btn btn-success" value="Upload" />
                            <!-- below line added by pr on 12thdec19 -->
                            <div style="color:#00796B;font-weight: 600;font-size: 11px;line-height: 19px;">Note: Please click on <strong>Download Bulk Records</strong> Link under Actions to download the data file.Modify that excel file and save it as <strong>CSV</strong>.Then upload it here.</div><!-- link text changed by pr on 17thdec19  -->                            
                        </div>
                    </div>
                </div>
            </div>
            <div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- end, code added by pr on 2ndapr18  -->
<!-- /.modal -->
<div class="modal fade bd-modal-lg" id="basic_track" tabindex="-1"  aria-hidden="true" onfocus="openAllTooltip()" onblur="closeAllTooltip()">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title" id="refnumbertrack"></h3>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeAllTooltip()"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body clearfix">
                <div class="col-md-12">
                    <h6 class="mb-3"><b>User Detail</b></h6>
                    <table class="table table-bordered table-striped track-tbl" id="track_user_tbl">
                        <thead>
                            <tr class="table-info">
                                <td><b>Name</b></td>
                                <td><b>Email</b></td>
                                <td><b>Mobile</b></td>
                                <td><b>Date</b></td>
                            </tr>
                        </thead>
                        <tbody id="user_data_tbody"></tbody>
                    </table>

                </div>
                <div class="col-md-12">
                    <form class="form-horizontal" action="#" id="submit_form" method="POST">
                        <div class="form-wizard">
                            <div class="form-body">
                                <div class="tracker-div clearfix">
                                    <ul class="order-tracker">

                                    </ul>
                                </div>
                                <div class="track-msg" id="track-msg">
                                    Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy.
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="query_raise_hyperlink">Raised/Responded Query</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeAllTooltip()">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!--  start, code added by pr on 12thmar18 -->
<div class="modal fade" id="search_regno_modal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Status</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeAllTooltip()"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <div class="mt-2 mb-4" style="border: 1px solid #ddd;padding: 8px 0px 0px 10px;border-radius: 5px;">
                        <label id='srch_reg_no' class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="radio6" checked > Search Registration Number
                            <span></span>
                        </label>&emsp;&emsp;&emsp;&emsp;
                        <label id='srch_frm' class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="radio6" > Search Forms
                            <span></span>
                        </label>&emsp;&emsp;&emsp;&emsp;
                        <label id='srchform_byemail' class="k-radio k-radio--bold k-radio--brand">
                            <input type="radio" name="radio6" > Search Forms by email
                            <span></span>
                        </label>
                    </div>
                    <div class="row" id='srch_reg_no_div'>
                        <div class="col-md-10">
                            <div class="form-group">
                                <label for="sreg_no">Search Registration Number</label>
                                <input type="text" class="form-control"  name="sreg_no" id="sreg_no" placeholder="Enter Registration Number"  /><!-- width increased by pr on 15thmar18  -->
                            </div>
                        </div>
                        <div class="col-md-2">
                            <div class="form-group mt-4 pt-1">
                                <button class="btn btn-info btn-block" type="button" id="search_btn" value="Search">Search</button>
                                <font id="reg_error" style="color:red"><span ></span></font>
                            </div>
                        </div>
                    </div>
                     <div class="row display-hide" id='srch_reg_by_email'>
                         <div class="col-md-10" style="width:600px; display:inline-block;">
                            <div class="form-group">
                                <label for="sreg_no">Search Registration Number by email</label>
                                <input type="text" class="form-control"  name="sreg_no" id="sreg_no_byemail" placeholder="Enter email Id"  /><!-- width increased by pr on 15thmar18  -->
                            </div>
                        </div> 
                        <div class="col-md-2" style="width:150px;display:inline-block;">
                            <div class="form-group mt-4 pt-1">
                                <button class="btn btn-info btn-block" type="button" id="search_btn_of_email" value="Search">Search</button>
                                <font id="reg_error" style="color:red"><span ></span></font>
                            </div>
                        </div>
                    </div>

                    <div class="row display-hide" id='srch_frm_div' >
                        <div class="col-md-4" style="width:300px; display:inline-block;">
                            <div class="form-group">
                                <label for="skeyword">Search Forms</label>
                                <input type="text" class="form-control" onkeyup="showRole(this.value);"  name="skeyword" id="skeyword" placeholder="Enter Keyword - Email/Name/Designation/Mobile/State "  />
                                <font id="key_error" style="color:red"><span ></span></font>
                            </div>
                        </div>
                        <div class="col-md-4" style="width:300px; display:inline-block;">
                            <div class="form-group">
                                <label for="srole">Select Role</label>
                                <select  name="srole" id="srole" class="form-control">
                                    <option value="">Select Role</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-2 mt-4 pt-1" style="width:150px; display:inline-block;">
                            <div class="form-group">
                                <button class="btn btn-info btn-block" type="button" id="search_frm_btn" value="Search">Search</button>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row display-hide" id='srch_forms_email'>
                        <div class="col-md-10" >
                            <div class="form-group">
                                <label for="sreg_no">Search Forms By Email</label>
                                <input type="text" class="form-control"  name="sreg_no" id="sreg_no" placeholder="Enter Email ID"  /><!-- width increased by pr on 15thmar18  -->
                            </div>
                        </div>
                        <div class="col-md-2" >
                            <div class="form-group">
                                <button class="btn btn-info btn-block" type="button" id="search_btn" value="Search">Search</button>
                                <font id="reg_error" style="color:red"><span ></span></font>
                            </div>
                        </div>
                    </div>

                    <!-- start, table to show search a registration number search results -->
                    <h5 id="search_heading" class="mb-2 mt-2" style="font-size: 15px;color: #333 !important;font-weight: 600;"></h5>
                    <!-- below div added by pr on 2ndmay18  -->
                    <table class="table table-bordered">
                        <tbody class="ajaxEmpDetails">

                        </tbody>
                    </table>                       
                    <div id="search_table_id" class="mb-4 mt-5"></div>  
                    <!-- end, table -->
                </div>
                <h3 id="search_key_heading"></h3> <!--  to show keyword search results HEADING  -->
                <div id="keysearch_table_id"></div>  <!--  to show keyword search results  -->
                <!-- end, div added by pr on 14thaug18 to provide forms search as well  -->
                <div style="color: #282a3c;font-weight: 600;font-size: 11px;line-height: 19px;">Note: Email,Name,Designation,Mobile,Posting State search is for Applicant.Email, Name, Mobile search for RO.Email search for Coordinator and Admin.</div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn btn-default" id="search_close_btn" data-dismiss="modal">Close</button><!-- id added by pr on 16thmar18  -->
            </div>
        </div>
    </div>
    <div>
    </div>
</div>
<!-- /.modal-content -->
</div>
<!-- /.modal-dialog -->
</div>
<!--  end, code added by pr on 12thmar18 -->
<div class="modal fade bs-modal-lg preview-change" id="large1" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="sms_preview">
        <div class="sms_preview"></div>  
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="dns_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="dns_preview">
        <div class="dns_preview"></div>    
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="wifi_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="wifi_preview">
        <div class="wifi_preview"></div>   
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="singleuser_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="singleuser_preview">
        <div class="single_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="ldap_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="ldap_preview">
        <div class="ldap_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="imappop_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="imappop_preview">
        <div class="imappop_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>

<div class="modal fade bs-modal-lg preview-change" id="dor_ext_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="dor_ext_preview">
        <div class="dor_ext_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>

<div class="modal fade bs-modal-lg preview-change" id="mobile_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="mobile_preview">
        <div class="mobile_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="nkn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="nkn_preview">
        <div class="nkn_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="gem_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="gem_preview">
        <div class="gem_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg preview-change" id="bulkuser_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="bulkuser_preview">
        <div class="bulk_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg  preview-change" id="distribution_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="distribution_preview" method="post">
        <div class="distribution_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>           
<div class="modal fade bs-modal-lg  preview-change" id="relay_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="relay_preview" method="post">
        <div class="relay_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg  preview-change" id="ip_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="ip_preview" method="post">
        <div class="ip_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>

</div>
<div class="modal fade bs-modal-lg  preview-change" id="webcast_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="webcast_preview" method="post">
        <div class="webcast_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg  preview-change" id="centralutm_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="centralutm_preview" method="post">
        <div class="centralutm_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg  preview-change" id="wifiport_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="wifiport_preview" method="post">
        <div class="wifiport_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg  preview-change" id="vpn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="vpn_preview" method="post">
        <div class="vpn_preview"></div>
        <input type="hidden" name="module" value="admin" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="email_act_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="email_act_preview" method="post">    
        <div class="email_act_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="email_deact_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="email_deact_preview" method="post">    
        <div class="email_deact_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="daonboarding_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="daonboarding_preview" method="post">    
        <div class="daonboarding_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>

<!--    <div class="modal fade bs-modal-lg" id="vpn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
        <form role="form" id="vpn_preview" method="post">
<!--jsp:include page="include/vpn_preview.jsp" />
</form>
/.modal-dialog 
</div>-->
<!--Nested Modal -->
<div class="modal fade" id="stack2" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">Terms and conditions</h4>
            </div>
            <div class="modal-body">                           
                <p><i class="entypo-info-circled"></i> Terms and conditions goes here</p>
                <div class="modal-footer">
                    <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Nested Modal-->
<!-- Modal for last submission -->
<div class="modal fade" id="stack3" tabindex="-1">
    <form id="sms_preview_confirm">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title">Confirmation</h4>
                </div>
                <div class="modal-body">                            
                    <p><i class="entypo-info-circled"></i> Are you sure?</p>
                    <div class="modal-footer">
                        <button type="button" class="btn dark btn-outline" data-dismiss="modal">No</button>                                
                        <button type="button" class="btn red btn-outline save-changes" id="confirmYes">Yes</button>                                
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="modal fade" id="bulkUploadEditSingle" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Edit Form</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body">
                <div class="dns_errorMessage"></div>
                <div class="email_errorMessage"></div>
                <input type="hidden" id="comming_from">
                <input type="hidden" name="iCampaignId" id="iCampaignIdDt">
                <form id="singleBulkEdit">
                    <input type="hidden" name="id" id="sinId">
                    <div id="bulkdynamic-form"></div>
                    <div class="form-group">
                        <!--<label for="street" id="">Migration Date & Time </label>-->
                        <input type="hidden" id="migrate_pop1" class="form-control migrationdatepicker" placeholder="Start Date & Time" name="migration_date" readonly>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <span class="btn btn-primary d-none" id="singleDataEditBtn">Update</span>
                <span class="btn btn-primary" id="singleBulkEditBtn">Update</span>
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="Alert.jsp"></jsp:include>

<!-- below line added by pr on 22ndjan18 to implement CSRF  -->  
<input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
<!-- below line added by pr on 25thjan18  -->
<input type="hidden" name="comingFrom" id="comingFrom" value="admin" />
<script src="main_js/onlineforms.js" type="text/javascript"></script>
<script src="main_js/sms.js" type="text/javascript"></script>
<script src="main_js/dns.js" type="text/javascript"></script>
<script src="main_js/dns_bulk.js" type="text/javascript"></script>
<script src="main_js/wifi.js" type="text/javascript"></script>
<script src="main_js/wifiport.js" type="text/javascript"></script>
<script src="main_js/email.js" type="text/javascript"></script>
<script src="main_js/ldap.js" type="text/javascript"></script>
<script src="main_js/imappop.js" type="text/javascript"></script>
<script src="main_js/mobile.js" type="text/javascript"></script>
<script src="main_js/dlist.js" type="text/javascript"></script>
<script src="main_js/relay.js" type="text/javascript"></script>
<script src="main_js/change_ip.js" type="text/javascript"></script>
<script src="main_js/vpn.js" type="text/javascript"></script>
<script src="main_js/CA.js" type="text/javascript"></script>
<script type="text/javascript" src="main_js/vpb_uploader.js"></script>
<!-- start, code added by pr on 30thjul19  -->
<link rel="stylesheet" href="assets/query_date_range/daterangepicker/daterangepicker-bs3.css">
<script src="assets/query_date_range/gsap/main-gsap.js"></script>
<script src="assets/query_date_range/resizeable.js"></script>
<script src="assets/query_date_range/daterange-custom.js"></script>
<script src="assets/query_date_range/daterangepicker/moment.min.js"></script>
<script src="assets/query_date_range/daterangepicker/daterangepicker.js"></script>
<script type="text/javascript" src="assets/custom/tracker_js.js"></script>
<script type="text/javascript" src="main_js/page_js/newsupport.js"></script>
<!-- end, code added by pr on 30thjul19  -->
<script>
//                                            $(function () {
//                                        $('[data-toggle="tooltip"]').tooltip()
//                                            })
//                                                $("#srch_frm").click(function () {
//                                        if ($(this).is(':checked')) {
//                                                $("#srch_frm_div").removeClass('d-none')
//                                                $("#srch_reg_no_div").addClass('d-none')
//                                        } else {
//                                                $("#srch_frm_div").addClass('d-none')
//                                        $("#srch_reg_no_div").removeClass('d-none')
//                                        }
//                                            });
//                                                $("#srch_reg_no").click(function () {
//                                        if ($(this).is(':checked')) {
//                                                $("#srch_reg_no_div").removeClass('d-none')
//                                                $("#srch_frm_div").addClass('d-none')
//                                        } else {
//                                                $("#srch_reg_no_div").addClass('d-none')
//                                        $("#srch_frm_div").removeClass('d-none')
//                                        }
//                                    });

                                    $('#srch_reg_no').click(function(){
                                       $('#srch_reg_no_div').show();
                                       $('#srch_reg_by_email').hide();
                                       $('#srch_frm_div').hide();
                                        table="";
                                       $("#search_table_id").html(table);
                                       $(".ajaxEmpDetails").html(table);
                                    })
                                    
                                    $('#srch_frm').click(function(){
                                        $('#srch_reg_no_div').hide();
                                       $('#srch_reg_by_email').hide();
                                       $('#srch_frm_div').show();
                                      
                                       table="";
                                       $("#search_table_id").html(table);
                                       $(".ajaxEmpDetails").html(table);
                                    })
                                    $('#srchform_byemail').click(function(){
                                        $('#srch_reg_no_div').hide();
                                       $('#srch_reg_by_email').show();
                                       $('#srch_frm_div').hide();
                                        table="";
                                       $("#search_table_id").html(table);
                                       $(".ajaxEmpDetails").html(table);
                                    })





                                        $('.fillter-div-max-tabs').bind('contextmenu', function (e) {
                                        return false;
                                    });
                                            $(document).ready(function () {
    <%if (moduleEsign.equals("esign")) {%>
                                                bootbox.alert('<%=esignMsg%>', function () {
                                            console.log("Alert Callback");
                                        });
                                            <%session.removeAttribute("moduleEsign");
        }%>
    <% if (moduleEsign.equals("esignFail")) {%>
                                                bootbox.alert('<%=esignMsg%>', function () {
                                            console.log("Alert Callback");
                                            });
                                            <% session.removeAttribute("moduleEsign");
        }%>
                                        clearCountsOnLoad();
                                        initDatatable("onload")
                                        });

                                        // below function added by pr on 19thmar19

                                            function fetchCounts()
                                    {
                                                // above code commented below added by pr on 26thmar19
                                                $.ajax({
                                                url: "fetchAllCount",
                                            type: "POST",
                                                //data: new FormData(document.getElementById('rcpt_Details')),
                                                contentType: false,
                                                cache: false,
                                                    processData: false,
                                            beforeSend: function (xhr) {
                                                $('.error_cnts').fadeIn('slow').html('<div align="center"><i class="fa fa-spinner fa-spin fa-2x fa-fw""></i><br/>Loading...<br/></div>');
                                                    },
                                                complete: function () {
                                                $(".error_cnts").hide();
                                                    },
                                                        success: function (data) {
                                                if (data) {
                                                            if (data.filterForms !== undefined && data.filterForms.length < 1) {
                                                            $('.search-filter').addClass('d-none');
                                                            $('.stack-eform .k-widget-18').html("<h6>No Request Available</h6>")
                                                    } else {
                                                        $('.search-filter').removeClass('d-none');
                                                    }
                                                        var myJSON = JSON.stringify(data);
                                                        var jsonResponse = JSON.parse(myJSON);
                                                        $(".totalCount").html(jsonResponse.totalRequest);
                                                        $(".newCount").html(jsonResponse.newRequest);
                                                        $(".counterup").html(jsonResponse.pendingRequest);
                                                        $(".completeCount").html(jsonResponse.completeRequest);
                                                            $.each(jsonResponse.filterForms, function (index, value)
                                                    {
                                                            $("." + value + "_form").show();
                                                    $("#" + value + "_form").show();
                                                });
                                                }
                                                    },
                                                error: function () {
                                            alert('Error: ');
                                        }
                                        });
                                    }

                                            function initDatatable(action, exportFlag)
                                    {

                                            $("#export-file-btn").prop("onclick", null).off("click");
                                            $("#export-file-btn").attr('onclick', "setAction('" + action + "', true);");
                                        $('.loader').show();
                                            $("#action_hidden").val(action); // so that it could be retirved to revert to this page, line added  by pr on 25thmay18
                                                var str = "";

                                                if (action == "filter") {

                                                var frm = [];
                                            var stat = [];
                                                $.each($("input[name='frm']:checked"), function () {
                                                frm.push($(this).val());
                                            });
                                                $.each($("input[name='stat']:checked"), function () {
                                                stat.push($(this).val());
                                                });                                             str += "&frm=" + frm + "&stat=" + stat;
                                                var queryfilter = $("input[name='queryfilter']:checked").val();
                                                    var querydate = $("#querydate_range").val();
                                            if (querydate == '') {
                                                querydate = $("#querydate").val();
                                            }
                                                    str += "&queryfilter=" + queryfilter + "&querydate=" + querydate; // line added by pr on 30thjul19
                                            if (exportFlag) {
                                                str += "&exportFlag=" + true;
                                            }
                                            console.log(" inside initdatatable query string is " + str);
                                                } else
                                                {
                                                    fetchCounts();
                                            if (exportFlag) {
                                            str += "&exportFlag=" + true;
                                            }
                                        }

                                            var current_url = window.location.href;
                                            var ajaxSource = "rawShowLinkData?action=" + action + str;
                                            var url = window.location.pathname;
                                            var urlArr = url.split("/");
                                        var firstPosition = urlArr[1];
                                                if (current_url.indexOf("OnlineForm") >= 0)
                                        {
                                            ajaxSource = "/" + firstPosition + "/rawShowLinkData?action=" + action + str; // done dynamic on 11thjan19                                                
                                                }
                                                $("#example").dataTable({
                                                "bServerSide": true,
                                                "sAjaxSource": ajaxSource,
                                                "bProcessing": true,
                                                "responsive": true,
                                                    "bJQueryUI": true,
                                            "aoColumns": [
                                                    {"mDataProp": "_stat_reg_no"},
                                                {"mDataProp": "email"},
                                                    {"mDataProp": "_stat_type"},
                                                    {"mDataProp": "app_user_type"},
                                                        {"mDataProp": "_stat_createdon"},
                                                        {
                                                    "mData": "Actions",
                                                            "mRender": function (data, type, row) {
                                                                //alert(data);
                                                        var call1 = rander_action_dd(row);
                                                    return call1;
                                                }
                                                }
                                                ],
                                                    "order": [[5, "desc"]],
                                                        "columnDefs": [
                                                {
                                                        "targets": [0, 5], //first and last not sortable
                                                    "orderable": false,
                                                className: "all", "targets": [0, 5]
                                                },
                                            ],
                                                    "fnRowCallback": function (nRow, mDataProp) {
                                                    var sub_type_value = mDataProp["app_user_type"];
                                                        if (mDataProp.supportRemarks === 'coordinator deleted, request moved to support' && mDataProp._stat_type === 'Pending with Support')
                                                {
                                                        $('td', nRow).css('background-color', '#ffd273');
                                                    $('td', nRow).attr('title', 'Coordinator Deleted, Request moved to Support');
                                                }
                                                        if (sub_type_value != null && sub_type_value.indexOf("Manual") > 0)
                                                {
                                                $('td', nRow).css('background-color', 'Pink');
                                                }
                                                }, // function added by pr on 14thjan19
                                                    "fnDrawCallback": function (data) {
                                                handleData(data, action);
                                            $('.loader').hide();
                                        }
                                        });
                                    }
                                            function handleData(responseData, action) // new parameter action added by pr on 31stmay18 so as to update that particular action count after the processing or during loading
                                    {
                                                $('#example tbody tr').each(function () {
                                                var regId = $(this).find('td').first().addClass('track_id');
                                                var regId = $(this).find('td').first().text();
                                                var linkStr = '<a href="#" class="previewedit" id="true">' + regId + '</a>';
                                            $(this).find('td').first().html(linkStr);
                                        });

                                            var listObj = responseData.json['listBeanObj'];
                                            //alert(listObj);
                                                var iTotalRecords = responseData.json['iTotalRecords'];
                                            if (iTotalRecords > 0) {
                                                $(".caption.font-dark").show()
                                            } else {
                                            $(".caption.font-dark").hide()
                                        }
                                        $("#heading_id").html(listObj.heading);
                                            }

                                            function rander_action_dd(val) {
                                        var role = $("#role").val();

                                            // start, code added by pr on 8thjan2020

                                            var showOnHold = false; // to show or hide the on hold link

                                        var showOnHoldOuterIf = false;

                                            console.log(" role value is "+role+" stat type value is "+val._stat_type);                                            

                                                if (role == "admin" && (val._stat_type.toLowerCase().indexOf("pending") > -1 && val._stat_type.toLowerCase().indexOf("admin") > -1))
                                            {
                                            showOnHoldOuterIf = true;
                                                } else if (role == "sup" && (val._stat_type.toLowerCase().indexOf("pending") > -1 && val._stat_type.toLowerCase().indexOf("support") > -1))
                                            {
                                                //console.log(" role value is "+role+" stat type value is "+val._stat_type);                                            
                                            showOnHoldOuterIf = true;
                                            }
                                        showOnHold = val.on_hold;

                                            // end, code added by pr on 8thjan2020                                        
                                            
                                        var manualStr = val.app_user_type;
                                        console.log(" app_user_type is "+manualStr);
                                            var remarks = "Click to take appropriate actions"; // remarks added by pr on 17thjan19        
                                                if (manualStr != null && manualStr.indexOf("Manual") > -1)
                                        {
                                            remarks = "Since it is a case of Manual selection.Please go through the details mentioned in the form thoroughly and before approving the request.";
                                            }
                                        var showAction = val.showAction;
                                        console.log(" show Action is "+showAction + " val.showCreateAction is " + val.showCreateAction);
                                            var str = '<div class="btn-group curr_reg_btn" id="actions_' + val._stat_reg_no + '"><button class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="true" title="' + remarks + '"> Actions  '; // remarks added by pr on 6thjan19  

                                            var showQueryResponse = val.showQueryResponse;
                                            // console.log(val);
                                             console.log(" showQueryREsponse is "+showQueryResponse);
                                            if (showQueryResponse === "raised")
                                                {
                                            str += '<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="top" style="color:#FFEB3B;" title="Query has been Raised, Please click on Raise/Respond to Query Link to respond."></i> ';
                                                } else  if (showQueryResponse === "responded") {
                                            str += '<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="top" style="color:#4cfa52;" title="You are waiting for response of the already raised query."></i> ';
                                                } else if (showQueryResponse === "both") {
                                                str += '<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="top" style="color:#FFEB3B;height:8px;width:8px;margin-top:8px;" title="Query has been Raised as well as you are waiting for response, Please click on Raise/Respond to Query Link to raise/respond to queries."></i> ';
                                            str += '<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="top" style="color:#4cfa52;height:8px;width:8px;margin-top:8px;" title="Query has been Raised as well as you are waiting for response, Please click on Raise/Respond to Query Link to raise/respond to queries."></i> ';
                                                } else {
                                            str += '<i data-toggle="tooltip" data-placement="top"></i> ';                                       }

//                                        if (val['showQuery'] == true)
//                                        {
//                                            str += '<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="top" style="color:#FFEB3B;" title="Query Raised, Please click on Raise/Respond to Query Link"></i> ';
//                                        }
//                                        <!-- added by rahul march 2021 -->
//                                        if (val['showQueryBy'] == true)
//                                        {
//                                            str += '<i class="fa fa-question-circle" data-toggle="tooltip" data-placement="top" style="color:#4cfa52;" title="Query Responded, Please click on Raise/Respond to Query Link"></i> ';
//                                        }
                                            str += '<i class="fa fa-angle-down"></i></button><ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;">';
                                            str += '<li title="Edit the details of your form or Preview your form"><a class="previewedit" id="' + showAction + '"><i class="fa fa-edit"></i> Preview ';
                                            if (showAction)
                                            {
                                                str += '/ Edit </a></li>';
                                            }
                                            // approve and reject buttons
                                            if (val.showAction)
                                            {
                                                if (val.showApprove)
                                                {
                                                    //str += "<li><a data-toggle=\"modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'forward', '" + val.appType + "');prefillData();resetRandom();\" ><i class=\"fa fa-step-forward\"></i> Approve </a></li>";

                                                    // class and id to a tag added by pr on 27thnov19
                                                    //daonboarding_approve_hide = val._stat_reg_no.indexOf("DAONBOARDING") > -1 && role == "co" ? "style='display:none;'" : "";
                                                    str += "<li><a class=" + val.email + "~" + val.mobile + " id='app_" + val._stat_reg_no + "' data-toggle=\"modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'forward', '" + val.appType + "');prefillData();resetRandom();\" ><i class=\"fa fa-step-forward\"></i> Approve </a></li>";

                                                } else
                                                {
                                                    if (role == "ca")
                                                    {
                                                        //str += '<li><a data-toggle="modal" href="#app_modal" ><i class="fa fa-step-forward"></i> Approve</a></li>'; // approve_modal -> app_modal on 4thjun18

                                                        // class and id to a tag added by pr on 27thnov19
                                                        str += '<li><a class=' + val.email + '~' + val.mobile + ' id="app_' + val._stat_reg_no + '" data-toggle="modal" href="#app_modal" ><i class="fa fa-step-forward"></i> Approve</a></li>'; // approve_modal -> app_modal on 4thjun18
                                                    }
                                                }
                                                if (role === "admin" && val._stat_form_type === "centralutm")
                                                {
                                                } else {
                                                    str += "<li><a data-toggle=\"modal\" href=\"#reject_modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'reject', '');resetRandom();\"  ><i class=\"fa fa-times-circle\"></i> Reject </a></li>";
                                                }
                                            }

                                            if (val.showCreateAction)
                                            {
                                                //str += "<li><a data-toggle=\"modal\" href=\"#create_modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'create', '');resetRandom();\"><i class=\"fa fa-step-forward\"></i> Create ID / Mark as Done </a></li>";

                                                // above line modified by pr on 28thnov19, class and id to a tag added by pr on 28thnov19                                            
                                                str += "<li><a class=" + val.email + "~" + val.mobile + " id='app_" + val._stat_reg_no + "' data-toggle=\"modal\" href=\"#create_modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'create', '');resetRandom();\"><i class=\"fa fa-step-forward\"></i> Create ID / Mark as Done </a></li>";
                    }

                                        if (val._stat_form_type == "bulk" || val._stat_form_type == "nkn_bulk") // if block added by pr on 19thjul19, modified by pr on 31stjul2020
                                        {
                                            str += "<li><a data-toggle=\"modal\" href=\"#\" onclick=\"prefillViewBulkData('" + val._stat_reg_no + "', '" + val._stat_form_type + "')\" ><i class=\"fa fa-step-forward\"></i> View Bulk Records</a></li>";// text changed from "View Bulk User IDS" on 17thdec19

                                        }

                                        // show bulk related links
                                        if (val.showBulkLink)
                                        {
                                            if (val.showBulkCreateAction)
                                            {
                                                console.log("aaaaaaaaaaaaaaaaaaaaaaa:" + val.email +  val.mobile + val._stat_reg_no)
                                                //str += "<li><a data-toggle=\"modal\" href=\"#bulk_modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'bulk', '');resetRandom();\" ><i class=\"fa fa-step-forward\"></i> Create Bulk User IDS</a></li>";
                                                // above line modified by pr on 28thnov19, class and id to a tag added by pr on 28thnov19                                            
                                                str += "<li><a class=" + val.email + "~" + val.mobile + " id='app_" + val._stat_reg_no + "' data-toggle=\"modal\" href=\"#bulk_modal\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'bulk', '');resetRandom();\" ><i class=\"fa fa-step-forward\"></i> Create Bulk User IDS</a></li>";
                                            }

                                            // below line commented by pr on 19thjul19        
                                            //str += "<li><a data-toggle=\"modal\" href=\"#\" onclick=\"prefillViewBulkData('" + val._stat_reg_no + "', '" + val._stat_form_type + "')\" ><i class=\"fa fa-step-forward\"></i> View Bulk User IDS</a></li>";

                                            //if (val.showBulkCreateAction)
                                            if (val.showBulkUpdateAction && val._stat_form_type == "nkn_bulk") // line modified by pr on 12thdec19
                                            {
                                                 console.log("aaaaaaaaaaaaaaaaaaaaaaa:" + val.email +  val.mobile + val._stat_reg_no)
                                                //str += "<li><a href=\"/OnlineForms/bulkdownload?regNo=" + val._stat_reg_no + "&type=all\" ><i class=\"fa fa-step-forward\"></i>Bulk Users Download</a></li>";
                                                // above line commented below added by pr on 17thdec19
                                                str += "<li><a href=\"bulkdownload?regNo=" + val._stat_reg_no + "&type=all\" ><i class=\"fa fa-step-forward\"></i>Download Bulk Records</a></li>"; // text changed from "Bulk Users Download" on 17thdec19
                                                // onclick added by pr on 17thdec19
                                                str += "<li><a data-toggle=\"modal\" href=\"#bulk_upload_modal\" onclick=\"clearFileUpload();\" ><i class=\"fa fa-step-forward\"></i>Update Bulk Records</a></li>";
                                            }
                                            if (val.showBulkUpdateAction)
                                            {
                                               str += "<li><a data-toggle=\"modal\" href=\"#bulk_upload_modal\" onclick=\"clearFileUpload();\" ><i class=\"fa fa-step-forward\"></i>Update Bulk Records</a></li>"; 
                                            }

                                            // below line commented by pr on 25thjul19    
                                            /*str += "<li><a href=\"/OnlineForms/sdownload?regNo=" + val._stat_reg_no + "&type=success\" ><i class=\"fa fa-step-forward\"></i>Success Document</a></li>";
                                             str += "<li><a href=\"/OnlineForms/download?regNo=" + val._stat_reg_no + "&type=error\" ><i class=\"fa fa-step-forward\"></i>Error Document</a></li>";*/
                                        }

                                        if (val._stat_form_type == "bulk" || val._stat_form_type == "nkn_bulk") // if block taken from above by pr on 17thdec19, modified by pr on 31stjul2020
                                        {
                                            // start, code added by pr on 25thjul19

                                            if (val.showErrorSuccess.indexOf("success") >= 0)
                                            {
                                                str += "<li><a href=\"sdownload?regNo=" + val._stat_reg_no + "&type=success\" ><i class=\"fa fa-step-forward\"></i>Success Document</a></li>";
                                            }

                                            if (val.showErrorSuccess.indexOf("error") >= 0 && val._stat_form_type == "nkn_bulk")
                                            {
                                                str += "<li><a href=\"download?regNo=" + val._stat_reg_no + "&type=error\" ><i class=\"fa fa-step-forward\"></i>Error Document</a></li>";
                                            }

                                            // end, code added by pr on 25thjul19
                                        }

                                        if (role == "co" || role == "admin")
                                        {
                                            var revertStr = "Revert";

                                            if (val.showAction) // only in case of pending request it should be visible 
                                            {
                                                var showRevertLink = true; // line added by pr on 13thnov19

                                                if (role == "admin")
                                                {
                                                    var loggedin_email = '<%=loggedin_email%>';

                                                    if (loggedin_email == "support@nic.in" || loggedin_email == "support@gov.in" || loggedin_email == "support@dummy.nic.in")
                                                    {
                                                        $("#role").val("admin"); // role_data changed to role by pr on 13thnov19
                                                        revertStr = "Foward to DA";
                                                    } else
                                                    {
                                                        revertStr = "Foward to Admin (iNOC support)";

                                                        // start, code added by pr on 13thnov19
                                                        if (val._stat_forwarded_to_user == "support@nic.in" || val._stat_forwarded_to_user == "support@gov.in")
                                                        {
                                                            showRevertLink = false;
                                                        }
                                                        // end, code added by pr on 13thnov19
                                                    }
                                                }

                                                // if block around added by pr on 13thnov19
                                                if (showRevertLink)
                                                {
                                                    str += "<li><a data-toggle=\"modal\" onclick=\"revoke('" + val._stat_reg_no + "', '" + val._stat_form_type + "');resetRandom();\"><i class=\"fa fa-step-forward\"></i> " + revertStr + " </a></li>";
                                                }
                                            }
                                            if(val._stat_reg_no.indexOf("DAONBOARDING") > -1 && role == "co"){

                                                 str += "<li><a class=\"fupload\" id='" + val._stat_reg_no + "~" + val._stat_form_type + "' title=\"Download the sealed and signed form of user, provide your seal and sign on the form and click here to Upload in PDF Format\"><i class=\"fa fa-upload\"></i> Upload Scanned Form & Approve</a></li>";
                                            }
                                        }
                                        str += "<li  title='Track the status of your request'><a class=\"track\" onclick=\"track_frm('" + val._stat_reg_no + "')\"><i class=\"fa fa-map-marker\"></i> Track </a></li>";
                                        if (val.sign_cert != null && val.rename_sign_cert != null)
                                        {
                                            str += "<li><a href=\"download_4?ref_num=+" + val._stat_reg_no + "&stat_form_type=" + val._stat_form_type + "\" title=\"Click here to Download form sealed and signed by user in PDF Format\"><i class=\"fa fa-download\"></i> Download User's Scanned Form</a></li>";
                                        }
                                        //$(document).ready(function () {
                                        
//                                            $.ajax({
//                                                type: "POST",
//                                                url: "encodeURI",
//                                                data: {data: val._stat_reg_no},
//                                                datatype: JSON,
//                                                success: function (data)
//                                                {
//                                                   //console.log("inside encode url")
//                                                    str += "<li><a href=\"generatefile?data=+" + data + "\" title=\"Click here to Download the filled form in PDF\"><i class=\"fa fa-download\"></i> Generate Form </a></li>";
//                                                      //console.log("inside encode url"+str)
//                                                }, error: function ()
//                                                {
//                                                   // $('#tab1').show();
//                                                }
//                                            });
                                        //});
                                        if((val._stat_reg_no.indexOf("DAONBOARDING") == -1) && (val._stat_reg_no.indexOf("DOREXTRT-FORM") == -1)){ 
                                        //console.log("HAHAHA  ");
                                            str += "<li><a onclick=\"generatePdfs('" + val._stat_reg_no + "')\" title=\"Click here to Download the filled form in PDF\"><i class=\"fa fa-download\"></i> Generate Form </a></li>";
                                        }
                                        if (role == "ca")
                                        {
                                            if ((val.sign_cert != null && val.sign_cert != "") &&
                                                    ((val.ca_sign_cert == null && val.ca_rename_sign_cert == null)
                                                            || ((val.ca_sign_cert != null && val.ca_rename_sign_cert != null) && val._stat_type != null && val._stat_type.toLowerCase().indexOf("pending") >= 0 && val._stat_type.toLowerCase().indexOf("reporting") >= 0)))
                                            {
                                                console.log(" inside ca sign cert value null ");
                                                str += "<li><a class=\"fupload\" id='" + val._stat_reg_no + "~" + val._stat_form_type + "' title=\"Download the sealed and signed form of user, provide your seal and sign on the form and click here to Upload in PDF Format\"><i class=\"fa fa-upload\"></i> Upload Scanned Form </a></li>";
                                            }
                                        }

                                        if ((val.ca_sign_cert != null && val.ca_sign_cert != "") && (val.ca_rename_sign_cert != null && val.ca_rename_sign_cert != ""))
                                        {
                                            if((val._stat_reg_no.indexOf("DAONBOARDING") == -1) && (val._stat_reg_no.indexOf("DOREXTRT-FORM") == -1)){
                                                str += "<li><a href=\"download_5?ref_num=+" + val._stat_reg_no + "&stat_form_type=" + val._stat_form_type + "\" title=\"Click here to Download form sealed and signed by you in PDF Format\"><i class=\"fa fa-download\"></i> Download Scanned Form </a></li>";
                                                str += "<li><a class=\"fuploadmul_file\" onclick=\"fuploadmul('" + val._stat_reg_no + "');resetRandom();\" title='Click here to Upload relevant Documents in PDF Format. Example: Office ID Proof, Letter of consent, etc.'><i class=\"fa fa-upload\"></i> Upload Multiple Docs</a></li>";
                                            }
                                        }
                                        //if (role == "ca") // line commented by pr on 16thdec19
                                       if (!(manualStr.indexOf("Manual") > -1))
                                        {
                                            if(val._stat_reg_no.indexOf("DOREXTRT-FORM") == -1){
                                            //console.log("RRRRRRRRR");
                                            str += "<li><a class=\"fuploadmul_file\" onclick=\"fuploadmul('" + val._stat_reg_no + "');resetRandom();\" title='Click here to Upload relevant Documents in PDF Format. Example: Office ID Proof, Letter of consent, etc.'><i class=\"fa fa-upload\"></i> Upload Multiple Docs</a></li>";
                                            }
                                        }

                                        if (showOnHoldOuterIf)// if block added by pr on 8thjan2020
                                        {
                                            if (showOnHold)
                                            {
                                                str += "<li><a class=\"\" onclick=\"showonhold('" + val._stat_reg_no + "', 'false')\" title='Click here to put the request Off Hold'><i class=\"fa fa-upload\"></i> Put Off Hold</a></li>";

                                            } else
                                            {
                                                str += "<li><a class=\"\" onclick=\"showonhold('" + val._stat_reg_no + "', 'true')\" title='Click here to put the request On Hold'><i class=\"fa fa-upload\"></i> Put On Hold</a></li>";

                                            }
                                        }

                                        if((val._stat_reg_no.indexOf("DAONBOARDING") == -1) && (val._stat_reg_no.indexOf("DOREXTRT-FORM") == -1)){
                                        //console.log("have finally landed!!!");
                                            str += "<li><a class=\"fdownload_file\" onclick=\"fdownload('" + val._stat_reg_no + "');resetRandom();\" title='Click here to Download relevant Documents uploaded by you in PDF Format'><i class=\"fa fa-download\"></i> Download Multiple Docs </a></li>";
                                            str += "<li><a class=\"fuserdownload_file\" onclick=\"fuserdownload('" + val._stat_reg_no + "');resetRandom();\" title='Click here to Download relevant Documents uploaded by user in PDF Format'><i class=\"fa fa-download\"></i> Download Docs uploaded by user </a></li>";
                                        }else if(val._stat_reg_no.indexOf("DOREXTRT-FORM") == 0){
                                            str += "<li><a href=\"download_1_retired?ref_num=+" + val._stat_reg_no + "&cert_fileFileName=download_esigned\" title=\"Click here to Download eSigned PDF\"><i class=\"fa fa-download\"></i> Download esigned document</a></li>";
                                        }
                                        
                                       if(val.isRaiseQueryEnabled) {
                                        str += "<li title='If you have any query or you want to respond to a query, use this link' ><a data-toggle=\"modal\" href=\"#query_raise\" onclick=\"apply_heading('" + val._stat_reg_no + "', '" + val._stat_form_type + "', 'query_raise', '');resetRandom();\"  ><i class=\"fa fa-comment\"></i> Raise/Respond to Query </a></li><span id='" + val._stat_reg_no + "_email' style='display:none;'>" + val.email + "</span>";
                                    }
                                        str += "</ul></div>";
                                        return str;
                                    }

                                    function setAction(action, exportFlag = false)
                                    {
                                        var querydate = $("#querydate").val();
                                        //alert(querydate)
                                        if (action != "filter") {
                                            $("input[name='frm']").prop("checked", false);
                                            $("input[name='stat']").prop("checked", false);
                                            $('.queryfilter').prop("checked", false);
                                            $('.apply_filter').addClass('d-none');
                                            $("#querydate_range").val("");
                                        }
                                        $("#example").DataTable().clear().destroy();
                                        initDatatable(action, exportFlag);
                                        if (exportFlag) {
                                            window.open(
                                                    'exportData',
                                                    '_blank' // <- This is what makes it open in a new window.
                                                    );
                                    }
                                    }
                                    $("#oneda").click(function () {
                                        $("#collapseThreeDA").removeClass('show');
                                    });
                                    $("#threeda").click(function () {
                                        $("#collapseOneDA").removeClass('show');
                                    });

                                    function clearOther(id)// function added by pr on 27thapr2020
                                    {
                                        console.log(" inside clear other func id is " + id);
                                        if (id == "onhold_id")
                                        {
                                            $("#offhold_id").prop("checked", false);
                                        } else if (id == "offhold_id")
                                        {
                                            $("#onhold_id").prop("checked", false);
                                        }
                                    }


</script>