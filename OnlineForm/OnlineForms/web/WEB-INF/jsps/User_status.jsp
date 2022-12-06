<%@page import="java.util.Base64"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="com.org.utility.Constants"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%--<%@page import="com.org.bean.Forms"%>--%>
<%@page import="java.util.ArrayList"%>
<%@page import="admin.UserTrack"%> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib  uri="/struts-tags" prefix="s"%>

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
//        //response.sendRedirect("index.jsp");
//    }
    ArrayList frmSession = null;
    ArrayList statSession = null;
    if (session.getAttribute("frmArrList") != null) {
        frmSession = (ArrayList) session.getAttribute("frmArrList");
        session.removeAttribute("frmArrList");
    }
    if (session.getAttribute("statArrList") != null) {
        statSession = (ArrayList) session.getAttribute("statArrList");
        session.removeAttribute("statArrList");
    }
    // below code added by pr on 23rdjan18
    String CSRFRandom = entities.Random.csrf_random();
    session.setAttribute("CSRFRandom", CSRFRandom);
    session.setAttribute("admin_role", "user"); // line added by pr on 10thapr18
%>

<jsp:include page="include/new_include/sidebar_nav.jsp" />
<jsp:include page="Alert.jsp"></jsp:include>
<link rel="stylesheet" href="assets/custom/tracker-sty.css" />
<link rel="stylesheet" href="assets/custom/da-onboarding.css" />
<jsp:include page="include/new_include/header.jsp" />
<jsp:include page="include/new_include/filter_pop.jsp" />
<!-- begin:: Content -->
<div class="k-content	k-grid__item k-grid__item--fluid k-grid k-grid--hor" id="k_content">
    <div class="k-content__body	k-grid__item k-grid__item--fluid" id="k_content_body">

        <div class="alert alert-warning" style="background: #64B5F6;border-color: #2d80c3;margin: 0;"><div class='col-md-12'><div class='mb-2'><b>Dear <s:property value="#session['uservalues'].name" />,</b></div><b>Notice : </b> For any query or doubt, You can use “RAISE A QUERY“ option to interact with other stakeholders. For tracking the status of the request, use "TRACK" option.</div></div>
        <%
            UserData userdata = (UserData) session.getAttribute("uservalues");
            ArrayList user_role = (ArrayList) userdata.getRoles();
            if (user_role.size() > 1) { %>
        <div class="alert danger mt-3 popup-alert">
            <strong>Important!&nbsp;</strong> You are viewing your requests. To view requests on RO/Coordinator panel, Please click respective panels under Dashboard. You have 
            <%  for (int i = 0; i < user_role.size(); i++) {

                    String str = user_role.get(i).toString();
                    if (str.trim().equals("ca")) {%>
            <%= (i > 1) ? ",&nbsp;" : "&nbsp;"%>
            <a href="showLinkData?adminValue=ca" class="alert-ahref">RO Panel</a>
            <% } %>
            <% if (str.equals("co")) {%>
            <%= (i > 1) ? ",&nbsp;" : "&nbsp;"%><a href="showLinkData?adminValue=co" class="alert-ahref">Coordinator Panel</a>
            <% } %>
            <% if (str.equals("sup")) {%>
            <%= (i > 1) ? ",&nbsp;" : "&nbsp;"%><a href="showLinkData?adminValue=sup" class="alert-ahref">Support Panel</a>
            <% } %>
            <% if (str.equals("admin")) {%>
            <%= (i > 1) ? ",&nbsp;" : "&nbsp;"%><a href="showLinkData?adminValue=admin" class="alert-ahref">Admin Panel</a>
            <% }
                } %>&nbsp;access.
            <div class="closebtn">&times;</div>
        </div>
        <% } %>
        <div class="row fillter-div-max-tabs">
            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('total');">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--info" style="background: #303F9F;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>User Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.totalRequest" />">0</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
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
                                            <div class="k-widget-3__content-title">Today's<br>Pending Request</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.newRequest" />">0</span>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('pending');">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--danger" style="background: #d32f2f;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>Pending Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.pendingRequest" />">0</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>

            <div class="col-3">
                <a class="filter-a" href="javascript:void(0);"  onclick="setAction('completed');">
                    <div class="k-portlet k-portlet--fit k-portlet--height-fluid">
                        <div class="k-portlet__body k-portlet__body--fluid">
                            <div class="k-widget-3 k-widget-3--success" style="background: #00796B;">
                                <div class="k-widget-3__content dashtab">
                                    <div class="k-widget-3__content-info">
                                        <div class="k-widget-3__content-section">
                                            <div class="k-widget-3__content-title">Total<br>Completed Requests</div>
                                            <div class="k-widget-3__content-desc"></div>
                                        </div>
                                        <div class="k-widget-3__content-section">
                                            <span class="k-widget-3__content-number" data-counter="counterup" data-value="<s:property value="listBeanObj.completeRequest" />">0</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-3 col-xl-3 order-lg-2 order-xl-1 fillter-div-max mainpage">

                <!--begin::Portlet-->
                <div class="k-portlet k-portlet--height-fluid stack-eform">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title">General Filters</h3>
                        </div>

                    </div>
                    <div id="collapse_3_1" class="panel-collapse ">
                        <div class="k-portlet__body">
                            <div class="k-widget-18">
                                <s:if test="%{listBeanObj.filterForms.size() > 0}">
                                    <h3 class="theme-heading-h3">Application</h3>
                                    <div class="mt-checkbox-list">
                                        <!-- start, below code added and modified by pr on 9thjan18  -->
                                        <s:iterator value="listBeanObj.filterForms" var="formString">
                                            <s:if test="%{#formString == 'single'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Single User
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.SINGLE_FORM_KEYWORD)) { %> checked <%}%> value="<%=Constants.SINGLE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'bulk'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Bulk User
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.BULK_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.BULK_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'sms'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> SMS User
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.SMS_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.SMS_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'ip'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Change IP
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.IP_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.IP_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'bulkdlist'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Bulk Distribution List
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.BULKDIST_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.BULKDIST_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'dlist'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Distribution List
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.DIST_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.DIST_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'gem'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> GEM User
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.GEM_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.GEM_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'mobile'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Mobile change
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.MOB_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.MOB_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'imappop'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> IMAP POP
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.IMAP_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.IMAP_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'ldap'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> LDAP Authentication
                                                    <input type="checkbox" <% if (frmSession != null && frmSession.contains(Constants.LDAP_FORM_KEYWORD)) { %> checked <%}%>  value="<%=Constants.LDAP_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'nkn_single'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> NKN User
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.NKN_SINGLE_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.NKN_SINGLE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'relay'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Relay Server
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.RELAY_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.RELAY_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>
                                            <s:if test="%{#formString == 'dns'}">    
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> DNS Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.DNS_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.DNS_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label> 
                                            </s:if>
                                            <s:if test="%{#formString == 'wifi'}">        
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> WIFI Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.WIFI_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.WIFI_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>   
                                            <s:if test="%{#formString == 'wifiport'}">        
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> WIFI Port Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.WIFI_PORT_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.WIFI_PORT_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>    
                                            <s:if test="%{#formString == 'vpn_single'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> VPN Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.VPN_SINGLE_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.VPN_SINGLE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>    
                                            <s:if test="%{#formString == 'email_act'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Email Activate Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.EMAILACTIVATE_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.EMAILACTIVATE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>    
                                            <s:if test="%{#formString == 'email_deact'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Email Deactivate Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.EMAILDEACTIVATE_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.EMAILDEACTIVATE_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if>    
                                            <s:if test="%{#formString == 'webcast'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Webcast Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.WEBCAST_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.WEBCAST_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if> 
                                            <s:if test="%{#formString == 'webcast'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Central UTM Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.CENTRAL_UTM_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.CENTRAL_UTM_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if> 
                                            <s:if test="%{#formString == 'dor_ext'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Date of Account Expiry
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.DOR_EXT_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.DOR_EXT_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if> 
                                            <s:if test="%{#formString == 'daonboarding'}">           
                                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Da-Onboarding Registration
                                                    <input type="checkbox"  <% if (frmSession != null && frmSession.contains(Constants.DAONBOARDING_FORM_KEYWORD)) { %> checked <%}%>   value="<%=Constants.DAONBOARDING_FORM_KEYWORD%>" name="frm">
                                                    <span></span>
                                                </label>
                                            </s:if> 
                                        </s:iterator>
                                        <!-- end, below code added and modified by pr on 9thjan18  -->
                                    </div>
                                    <h3 class="theme-heading-h3">Status</h3>
                                    <div class="mt-checkbox-list">
                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Pending Request
                                            <input type="checkbox" value="pending"    name="stat">
                                            <span></span>
                                        </label>
                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Rejected Request
                                            <input type="checkbox" value="rejected" name="stat">
                                            <span></span>
                                        </label>
                                        <label class="k-checkbox k-checkbox--bold k-checkbox--brand"> Completed Request
                                            <input type="checkbox" value="completed" name="stat">
                                            <span></span>
                                        </label>
                                    </div>
                                </s:if>
                                <s:else>
                                    <h6>No Request Available</h6>
                                </s:else>
                            </div>
                        </div>
                    </div>

                    <div class="text-center d-none" style="padding-bottom: 25px;">
                        <button type="button" onclick="setAction('filter');"  class="btn btn-primary">Apply Filter</button>
                    </div>
                </div>
                <!--</form>--><!-- form tag deleted by pr on 2ndjan18 -->
            </div>

            <div class="col-lg-9 col-xl-9 order-lg-2 order-xl-1">

                <!--begin::Portlet-->
                <div class="k-portlet k-portlet--height-fluid k-widget-17">
                    <div class="k-portlet__head">
                        <div class="k-portlet__head-label">
                            <h3 class="k-portlet__head-title"><span class="caption-subject bold uppercase"><s:property value="listBeanObj.heading" /></span></h3>
                        </div>

                    </div>
                    <div class="k-portlet__body">
                        <div id="filter-div-checked" class="mb-3"></div>
                        <table class="table table-striped table-bordered table-hover table-checkable order-column dt-responsive" id="example">
                            <thead>
                                <tr>
                                    <th class="all"> App Id </th>
                                    <th> Email </th>
                                    <th> Status </th>
                                    <th> Date </th>
                                    <th class="all"> Actions </th>
                                </tr>
                            </thead>
                            <tbody id="rawFilterAction">
                                <s:iterator value="listBeanObj.data" var="formsBean">
                                    <tr class="odd gradeX">
                                        <td class="track_id" id="${formsBean.get_stat_reg_no()}" ><a href="#" class="previewedit" id="true">${formsBean.get_stat_reg_no()}</a></td>
                                <input type="hidden" value="" id="form_val">
                                <input type="hidden" value="" id="ref_no">
                                <input type="hidden" value="USER" id="panel">

                                <td>
                                    ${formsBean.get_email()}
                                </td>
                                <td>
                                    <span class="label label-sm label-success">${formsBean.get_stat_type()} </span><!-- apply the code to call getstatype func before showing -->
                                </td>
                                <td class="center">
                                    ${formsBean.get_stat_createdon()}
                                </td>
                                <td>
                                    <div class="btn-group curr_reg_btn" id="${formsBean.get_stat_reg_no()}">

                                        <s:if test="%{#formsBean.getshowQueryResponse == 'raised'}">
                                            <button  class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false"> Actions 
                                                <i class="fa fa-question-circle" style="color:#FFEB3B;" data-toggle="tooltip" data-placement="top" title="Query has been Raised, Please click on Raise/Respond to Query Link to respond."></i>
                                                <i class="fa fa-angle-down"></i>
                                            </button>
                                        </s:if>
                                        <s:elseif test="%{#formsBean.getshowQueryResponse == 'responded'}">
                                            <button  class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false"> Actions 
                                                <i class="fa fa-question-circle" style="color:#4cfa52;" data-toggle="tooltip" data-placement="top" title="You are waiting for response of the already raised query."></i>
                                                <i class="fa fa-angle-down"></i>
                                            </button>
                                        </s:elseif>
                                        <s:elseif test="%{#formsBean.getshowQueryResponse == 'both'}">
                                            <button  class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false"> Actions 
                                                <i class="fa fa-question-circle" style="color:#FFEB3B;height:8px;width:8px;margin-top:8px;" data-toggle="tooltip" data-placement="top" title="Query has been Raised as well as you are waiting for response, Please click on Raise/Respond to Query Link to raise/respond to queries."></i>
                                                <i class="fa fa-question-circle" style="color:#4cfa52;height:8px;width:8px;margin-top:8px;" data-toggle="tooltip" data-placement="top" title="Query has been Raised as well as you are waiting for response, Please click on Raise/Respond to Query Link to raise/respond to queries."></i>
                                                <i class="fa fa-angle-down"></i>
                                            </button>
                                        </s:elseif> 
                                        <s:else>
                                            <button  class="btn btn-primary btn-xs green dropdown-toggle" type="button" data-toggle="dropdown" aria-expanded="false"> Actions  
                                                <i class="fa fa-angle-down"></i>
                                            </button>
                                        </s:else>  

                                        <s:if test="%{#formsBean.getShowResponse()}">
                                            <ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;" >
                                                <li>
                                                    <a class="previewedit"  id="${formsBean.getShowAction()}">
                                                        <i class="fa fa-edit"></i> Preview 
                                                        <s:if test="%{#formsBean.getShowdnsEdit()}">
                                                            / Edit 
                                                        </s:if>
                                                    </a>
                                                </li>
                                                <s:if test="%{#formsBean.getShowdnsCancel()}">                                                        <li>
                                                    <a class="reject">
                                                        <i class="fa fa-times-circle"></i> Reject </a>
                                                </li>                                            </s:if>
                                                <li>
                                                    <a class="" id="${formsBean.getShowAction()}" onclick="approve_ca('${formsBean.get_stat_reg_no()}', '${formsBean.get_stat_form_type()}', '${formsBean.getStatusApi()}')">
                                                        <i class="fa fa-edit"></i> Approve 
                                                    </a>
                                                </li>
                                                
                                                <s:if test="%{#formsBean.getIsReject()}">
                                                    <li>
                                                        <a class="" id="">
                                                            <i class="fa fa-edit"></i> Resend 
                                                        </a>
                                                    </li>
                                                </s:if> 
                                            </ul>
                                        </s:if>       
                                        <s:else>
                                            <ul class="dropdown-menu dropdown-menu-right action-btn-list" role="menu" style="position:inherit;" >
                                                <li>
                                                    <a class="previewedit" id="${formsBean.getShowAction()}">
                                                        <i class="fa fa-edit"></i> Preview 
                                                        <s:if test="%{#formsBean.getShowAction()}">
                                                            / Edit 
                                                        </s:if>
                                                    </a>
                                                </li>

                                                <s:if test="%{#formsBean.getIsReject()}">
                                                    <li>
                                                        <a onclick="resend('${formsBean.get_stat_reg_no()}')">
                                                            <i class="fa fa-edit"></i> Resend 
                                                        </a>
                                                    </li>
                                                </s:if> 

                                                <s:if test="%{#formsBean.getShowAction()}">
                                                    <li>
                                                        <a class="reject">
                                                            <i class="fa fa-times-circle"></i> Cancel </a>
                                                    </li>
                                                </s:if>
                                                     <s:if test="%{#formsBean.getShowdnsCancel()}">       
                                                    <li>
                                                        <a class="reject">
                                                            <i class="fa fa-times-circle"></i> Cancel </a>                                                    </li>
                                                </s:if>
                                                    
                                                <s:if test="%{#formsBean.get_stat_form_type() == 'email_act' && #formsBean.getShowWorkOrder()}"> <!-- if across added by pr on 29thjan18  -->


                                                    <li>
                                                        <a class="work_order" id="${formsBean.get_stat_reg_no()}">
                                                            <i class="fa fa-upload"></i> Upload work order 
                                                        </a>
                                                    </li>
                                                </s:if>

                                                <li>
                                                    <s:if test="%{#formsBean.getShowBulkLink()}">   
                                                        <!-- below li added by pr on 28thdec17 -->
                                                    <li>
                                                        <a data-toggle="modal" href="#bulk_view_modal" onclick="prefillViewBulkData('${formsBean.get_stat_reg_no()}', '${formsBean.get_stat_form_type()}')" >
                                                            <i class="fa fa-step-forward"></i> View Bulk User IDS</a>
                                                    </li>    
                                                    <li>                                                                                                  
                                                        <s:url var="sfileDownload" namespace="/" action="sdownload" >
                                                            <s:param name="regNo">${formsBean.get_stat_reg_no()}</s:param>
                                                            <s:param name="type">success</s:param>
                                                        </s:url>
                                                        <s:a href="%{sfileDownload}"><i class="fa fa-step-forward"></i>Success Document</s:a>
                                                            <!-- i tag added on 14thdec17 -->
                                                        </li>                                                                                                  
                                                        <li>                                                                                                  
                                                        <s:url var="erfileDownload" namespace="/" action="download" >
                                                            <s:param name="regNo">${formsBean.get_stat_reg_no()}</s:param>
                                                            <s:param name="type">error</s:param>
                                                        </s:url>
                                                        <s:a href="%{erfileDownload}"><i class="fa fa-step-forward"></i>Error Document</s:a>
                                                            <!-- i tag added on 14thdec17 -->
                                                        </li>
                                                </s:if>   
                                                <s:if test="%{#formsBean.get_stat_type() != 'Manual' }"> <!-- if across added by pr on 29thjan18  -->
                                                    <li>
                                                        <a class="track" onclick="track_frm('${formsBean.get_stat_reg_no()}')">
                                                            <i class="fa fa-map-marker"></i> Track </a>
                                                    </li>
                                                </s:if> 
                                                <s:if test="%{#formsBean.get_stat_form_type != 'dor_ext_retired' }">
                                                    <li>

                                                        <a onClick="generatePdfs('${formsBean.get_stat_reg_no()}')" title="Click here to Download the filled form in PDF">

                                                            <i class="fa fa-download"></i> Generate Form </a>
                                                    </li>
                                                    <%-- below line commented by MI on 15 nov 18--%>
                                                    <%--  <s:if test="%{#formsBean.getSign_cert() ==null &&  #formsBean.getRename_sign_cert() ==null}">--%>
                                                    <%-- <s:if test="%{#formsBean.getPdf_path() =='manual_upload' }">  --%>

                                                    <!-- above line commented below added by pr on 19thnov18  -->

                                                    <s:if test="%{#formsBean.getPdf_path() =='manual_upload' && #formsBean.get_stat_type() != 'Cancelled' }">      

                                                        <li>
                                                            <a class="fupload_1" title="Click here to Upload sealed and signed form in PDF Format ">
                                                                <i class="fa fa-upload"></i> Upload/Change Scanned Form </a>
                                                        </li>
                                                    </s:if> 
                                                    <%-- below line commented by MI on 15 nov 18--%>
                                                    <s:if test="%{#formsBean.get_stat_type() == 'Pending with RO/Nodal/FO' && #formsBean.getSign_cert() !=null &&  #formsBean.getRename_sign_cert() !=null}">
                                                        <li>
                                                            <a href="download_4?ref_num=+${formsBean.get_stat_reg_no()}&stat_form_type=${formsBean.get_stat_form_type()}" title='Click here to Download sealed and signed form in PDF Format'>
                                                                <i class="fa fa-download" ></i> Download Scanned Form </a>
                                                        </li>
                                                    </s:if>
                                                    <s:if test="%{#formsBean.getPdf_path() !='online processing' &&  #formsBean.getPdf_path() !='manual_upload'}">
                                                        <li>
                                                            <a href="download_1?ref_num=+${formsBean.get_stat_reg_no()}&cert_fileFileName=download_esigned" title='Click here to Download e-Signed form in PDF Format'>
                                                                <i class="fa fa-download"></i> Download esigned document  </a>
                                                        </li>    
                                                    </s:if>
                                                    <s:if test="%{#formsBean.getStatusApi() !='manual_upload'}"> 
                                                        <li>
                                                            <a class="fuploadmul_file" onclick="fuploadmul('${formsBean.get_stat_reg_no()}')" title='Click here to Upload relevant Documents in PDF Format. Example: Office ID Proof, Letter of consent, etc.'>
                                                                <i class="fa fa-upload"></i> Upload Multiple Docs</a>
                                                        </li>
                                                    </s:if>
                                                    <li>
                                                        <a class="fdownload_file" onclick="fdownload('${formsBean.get_stat_reg_no()}')" title='Click here to Download relevant Documents uploaded by you in PDF Format'>
                                                            <i class="fa fa-download"></i> Download Uploaded Docs</a>
                                                    </li>
                                                    <!--  below li added by pr on 6thapr18  -->
                                                    <li>
                                                        <a data-toggle="modal" href="#query_raise" onclick="apply_heading('${formsBean.get_stat_reg_no()}', '${formsBean.get_stat_form_type()}', 'query_raise');
                                                                resetRandom();"  >
                                                            <i class="fa fa-comment"></i> Raise/Respond to Query </a>
                                                    </li>
                                                </s:if>
                                                <s:else>
                                                    <s:if test="%{#formsBean.getPdf_path() !='online processing' &&  #formsBean.getPdf_path() !='manual_upload'}">
                                                        <li>
                                                            <a href="download_1_retired?ref_num=+${formsBean.get_stat_reg_no()}&cert_fileFileName=download_esigned" title='Click here to Download e-Signed form in PDF Format'>
                                                                <i class="fa fa-download"></i> Download esigned document  </a>
                                                        </li>    
                                                    </s:if>
                                                </s:else>
                                            </ul>
                                        </s:else>  
                                    </div>
                                </td>
                                </tr>
                            </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </div>
                <!-- END EXAMPLE TABLE PORTLET-->
            </div>
        </div>
    </div>

</div>
<div id="action_div" style="display: hidden">
</div>
<input type="hidden" name="role" id="role" value="user" /> <!-- line added by pr on 11thapr18  -->   
<input type="hidden" name="reject_click" id="reject_click" value="" />
<div id="filter-btn" class="k-header__topbar-item k-header__topbar-item--quick-panel filter-btn" data-toggle="k-tooltip" title="" data-placement="right" data-original-title="Filter Panel">
    <span class="k-header__topbar-icon" id="k_quick_panel_toggler_btn">
        <i class="fa fa-filter fa-2x"></i>
    </span>
</div>
<jsp:include page="include/new_include/footer.jsp" />
<!-- end:: Footer -->
<!-- /.modal-content -->
</body>
</html>
<!--  below div for modal added by pr on 6thapr18  -->
<div class="modal fade" id="query_raise" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" style="font-weight:400px;">Manage Query For SINGLE01012017</h4>
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
                            <div class="col-md-10 col-9 pr-0">
                                <div class="form-group">
                                    <label>Raise a Query</label>
                                    <textarea class="form-control" name="query" maxlength="500"  id="query" placeholder="Enter Your Query."  rows="3"></textarea>
                                    <font style="color:red"><span id="queryError"></span></font>
                                </div>
                            </div>
                            <div class="col-md-2 col-3 pl-0">
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
                <button type="button" class="btn dark btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- start, code added by pr on 28thdec17  -->
<div class="modal fade" id="bulk_view_modal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">View Bulk Users</h4>
            </div>
            <div class="modal-body">
                <div class="portlet-body form">
                    <!-- start, table -->
                    <h3 id="bulk_view_heading"></h3>
                    <table class="table table-striped table-bordered table-hover table-checkable order-column" id="example1">
                        <thead>
                            <tr>
                                <th> Bulk Id </th>
                                <th> UID </th>
                                <th> Email </th>
                                <th> Mobile </th>
                                <th> Date </th>
                                <th> Is Created </th>                                        
                                <th> Is Rejected </th>
                            </tr>
                        </thead>
                        <tbody id="bulk_users_view_tbody">
                            <!-- AJAX Response here  -->    
                        </tbody>
                    </table>
                    <!-- end, table -->
                </div>
            </div>
            <div>
            </div>
            <div class="modal-footer" >
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- end, code added by pr on 28thdec17  -->    
<div class="modal fade bd-modal-lg" id="workOrderUpload" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4>Upload Work Order</h4>
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
                    <div class="row" style="padding:20px;">
                        <div class="form-body" >
                            <form action="" method="post"  class="form_val" id="">
                                <div class="form-group">

                                    <div class="col-md-12">
                                        <label>Please Select Your File</label>

                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" name="workorder_cert_file" id="workorder_cert_file">
                                            <label class="custom-file-label text-left" for="workorder_cert_file">Select File</label>
                                            <font style="color:red"><span id="file_err"> </span></font>
                                        </div>
                                        <font style="color:red"><span id="file_cert_err"> </span></font>
                                    </div>
                                </div>	
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" id="insert_footer_cert">
                <button type="button" class="btn dark btn-default mod_close" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary uploadworder" >Upload</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- File Upload Modal Start -->

<div class="modal fade bd-modal-lg" id="fileUpload" tabindex="-1"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4>Upload/Delete Scanned Form</h4>
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
                    <div class="row" style="padding:20px;">
                        <div class="form-body" >
                            <form action="" method="post"  class="form_val track_upload1 display-hide" id="update_cert">
                                <div class="form-group">
                                    <div class="alert alert-info display-hide" style="width: 96%;margin: 15px;background-color: #f5f5f5" >
                                        <b>Download Certificate: </b><a href="download_12"><span id="cert_relay_file">download</span></a><a href="javascript:;" class="close fileinput-exists" data-dismiss="fileinput" id="delcert"> </a>
                                        <input type="hidden" id="regno_cert"  value="">
                                        <input type="hidden" class="" name="cert" id="cert">
                                    </div> 

                                    <div class="col-md-12">
                                        <label>Update Your File</label>
                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" name="cert_file1" id="cert_file1">
                                            <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                            <font style="color:red"><span id="file_err"> </span></font>
                                        </div>
                                        <font style="color:red"><span id="file_cert_err1"> </span></font>
                                    </div>

                                </div>	
                            </form>
                            <form action="" method="post"  class="form_val track_upload" id="insert_cert">
                                <div class="form-group">
                                    <div id="da-onboarding-manual-upload-note" class="col-md-12 text-hide">
                                        <strong class="alert-danger">Uploaded file should be sealed and signed by you and your competent authority</strong>
                                    </div><br/>
                                    <div class="col-md-12">
                                        <label>Please Select Your File</label>

                                        <div class="custom-file">
                                            <input type="file" class="custom-file-input" name="cert_file" id="cert_file">
                                            <label class="custom-file-label text-left" for="cert_file">Select File</label>
                                            <font style="color:red"><span id="file_err"> </span></font>
                                        </div>
                                        <font style="color:red"><span id="file_cert_err"> </span></font>

                                    </div>
                                </div>	
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" id="insert_footer_cert">
                <button type="button" class="btn dark btn-default mod_close" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary upload_cert" >Upload</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- File Upload Modal End -->
<jsp:include page="include/multiple-upload.jsp" />  

<div class="modal fade" id="approve_ca_modal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <h6>Please fill your profile first. </h6>
                <span id="useremployment_error"></span>
                <span id="minerror" style="color: red"></span>
                <span id="deperror" style="color: red"></span>
                <span id="smierror" style="color: red" ></span>
                <span id="state_error" style="color: red"></span>
                <span id="org_error" style="color: red"></span>
                <span id="hodname_error" style="color: red"></span>
                <span id="hodmobile_error" style="color: red"></span>
                <span id="cadesign_error" style="color: red"></span>
                <span id="hodemail_error" style="color: red"></span>
                <span id="hodtel_error" style="color: red"></span>
                <span id="userempcode_error" style="color: red"></span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-sm float-right" data-dismiss="modal" style="width:inherit;" autofocus value="no" id="delModal_no">Cancel</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="stack_upload" tabindex="-1">
    <form id="email_act_confirm_prvw" class="display-hide">
        <jsp:include page="include/Hod_detail_activate.jsp" />
    </form>
</div>
<div class="modal fade" id="delModal" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h6>Your application will not be processed as you are choosing to cancel your request . Do you still wish to proceed ?.</h6><button type="button" class="btn btn-danger mt-3" style="width:inherit;" value="yes" id="delModal_yes">OK</button> <button type="button" class="btn btn-primary mt-3" data-dismiss="modal" style="width:inherit;" autofocus value="no" id="delModal_no">Cancel</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="delModal1" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h2>Your Request has been cancelled sucessfully. </h2><button type="button" class="btn btn-danger" style="width:inherit;" value="yes">OK</button> <button type="button" class="btn btn-primary" data-dismiss="modal" style="width:inherit;" autofocus value="no" id="delModal_no">Cancel</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="delModal2" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h2>Profile Request not found. </h2><form method="post" name="delc" id="delc"><input type="hidden" name="imid"><input type="hidden" name="csrf_token" value="123safdsaf"> <button type="button" class="btn btn-primary" data-dismiss="modal" style="width:inherit;" autofocus>Cancel</button></form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="delModal3" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body" align="center">
                <h2>There is Some problem, please try again later. </h2><form method="post" name="delc" id="delc"><input type="hidden" name="imid"><input type="hidden" name="csrf_token" value="123safdsaf"><button type="button" class="btn btn-danger" style="width:inherit;">Yes,Sure</button> <button type="button" class="btn btn-primary" data-dismiss="modal" style="width:inherit;" autofocus value="no" >No</button></form>
            </div>
        </div>
    </div>
</div>  
<div class="modal fade bd-modal-lg" id="basic_track" tabindex="-1"  aria-hidden="true" onfocus="openAllTooltip()" onblur="closeAllTooltip()">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title" id="refnumbertrack"></h3>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeAllTooltip()"><i class="fa fa-window-close"></i></button>
            </div>
            <div class="modal-body clearfix">
                <div class="col-md-12 track-user-record">
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
                <button type="button" class="btn btn-primary" id="query_raise_hyperlink" onfocus="closeAllTooltip()">Raised/Responded Query</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" onclick="closeAllTooltip()">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade" id="basic" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">Action For SINGLE01012017</h4>
            </div>
            <div class="modal-body">
                <div class="portlet-body">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="mt-widget-3">
                                <div class="mt-head bg-blue-hoki">
                                    <div class="mt-head-icon">
                                        <i class="fa fa-lock"></i>
                                    </div>
                                    <div class="mt-head-desc"> eSign Application using Aadhar </div>
                                    <div class="mt-head-button">
                                        <button type="button" class="btn btn-circle btn-outline white btn-sm">Proceed</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mt-widget-3">
                                <div class="mt-head bg-red">
                                    <div class="mt-head-icon">
                                        <i class="fa fa-unlock"></i>
                                    </div>
                                    <div class="mt-head-desc"> eSign without using Aadhar </div>
                                    <div class="mt-head-button">
                                        <button type="button" class="btn btn-circle btn-outline white btn-sm">Proceed</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="mt-widget-3">
                                <div class="mt-head bg-green">
                                    <div class="mt-head-icon">
                                        <i class="fa fa-mail-forward"></i>
                                    </div>
                                    <div class="mt-head-desc"> Manually Forward(By Downloading) </div>
                                    <div class="mt-head-button">
                                        <button type="button" class="btn btn-circle btn-outline white btn-sm">Proceed</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="display:none;">
                <button type="button" class="btn dark btn-outline" data-dismiss="modal">Close</button>
                <button type="button" class="btn green">Submit</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<!-- /.modal preview-->
<div class="modal fade bs-modal-lg preview-change" id="large1" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="sms_preview">
        <div class="sms_preview"></div>
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="dns_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="dns_preview">
        <div class="dns_preview"></div> 
        <input type="hidden" name="module" id="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="wifi_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="wifi_preview">
        <div class="wifi_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="singleuser_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="singleuser_preview">
        <div class="single_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="ldap_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="ldap_preview">
        <div class="ldap_preview"></div>  
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="imappop_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="imappop_preview">
        <div class="imappop_preview"></div>  
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="mobile_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="mobile_preview">
        <div class="mobile_preview"></div>    
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="nkn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="nkn_preview">
        <div class="nkn_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="gem_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="gem_preview">
        <div class="gem_preview"></div>  
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="bulkuser_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="bulkuser_preview">
        <div class="bulk_preview"></div>  
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="distribution_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="distribution_preview" method="post">
        <div class="distribution_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>           
<div class="modal fade bs-modal-lg preview-change" id="relay_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="relay_preview" method="post">
        <div class="relay_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="ip_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="ip_preview" method="post">
        <div class="ip_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="webcast_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="webcast_preview" method="post">    
        <div class="webcast_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="vpn_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form role="form" id="vpn_preview" method="post">
        <div class="vpn_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
</div>  
<div class="modal fade bs-modal-lg preview-change" id="centralutm_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="centralutm_preview" method="post">    
        <div class="centralutm_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="wifiport_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="wifiport_preview" method="post">    
        <div class="wifiport_preview"></div> 
        <input type="hidden" name="module" value="user" />
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

<div class="modal fade bs-modal-lg preview-change" id="dor_ext_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="dor_ext_preview">
        <div class="dor_ext_preview"></div>  
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="bulkUploadEditSingle" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title"> Edit Form</h4>
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
<div class="modal fade bs-modal-lg preview-change" id="email_deact_preview_form" tabindex="-1" role="dialog" aria-hidden="true">           
    <form role="form" id="email_deact_preview" method="post">    
        <div class="email_deact_preview"></div> 
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade bs-modal-lg preview-change" id="daonboarding_preview_form" tabindex="-1" role="dialog" aria-hidden="true">
    <form method="post" action="" id="daonboarding_preview">
        <div class="daonboarding_preview"></div>  
        <input type="hidden" name="module" value="user" />
    </form>
    <!-- /.modal-dialog -->
</div>
<input type="hidden" name="CSRFRandom" value="<%=CSRFRandom%>" id="CSRFRandom" />
<input type="hidden" name="comingFrom" id="comingFrom" value="user" /><!-- line added by pr on 29thjan18, line modified by pr on 26thoct18 -->
<script src="assets/js/jquery-migrate-3.0.0.min.js"></script>
<script src="assets/js/waypoints.min.js"></script>
<script src="assets/old_assets/plugins/counterup/jquery.counterup.min.js" type="text/javascript"></script>
<script src="assets/old_assets/scripts/app.min.js" type="text/javascript"></script>
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
<script src="main_js/vpn.js" type="text/javascript"></script>
<script src="main_js/webcast.js" type="text/javascript"></script>
<script src="main_js/firewall.js" type="text/javascript"></script>
<script type="text/javascript" src="main_js/vpb_uploader.js"></script>
<script type="text/javascript" src="assets/custom/tracker_js.js"></script>
<script type="text/javascript" src="main_js/page_js/user_status.js"></script>
<script type="text/javascript">
                    $(function () {
                        $('[data-toggle="tooltip"]').tooltip()
                    })
                    var close = document.getElementsByClassName("closebtn");
                    var i;

                    for (i = 0; i < close.length; i++) {
                        close[i].onclick = function () {
                            var div = this.parentElement;
                            div.style.opacity = "0";
                            setTimeout(function () {
                                div.style.display = "none";
                            }, 600);
                        }
                    }
</script>