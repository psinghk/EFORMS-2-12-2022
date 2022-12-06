<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="page-header-top">
    <div class="container">
        <!-- BEGIN LOGO -->
        <div class="page-logo" style="margin-top:15px;height:auto;">
            <a href="Forms" style="text-decoration:none;">
                <img src="assets/img/eforms.png" alt="logo" class="logo-default" style="margin: 3.5px 6px 0;">
                <!--<strong>Forms for NIC services</strong>-->
            </a>
        </div>
        <!-- END LOGO -->
        <!-- BEGIN RESPONSIVE MENU TOGGLER -->
        <a href="javascript:;" class="menu-toggler"></a>
        <!-- END RESPONSIVE MENU TOGGLER -->
        <!-- BEGIN TOP NAVIGATION MENU -->
        <div class="top-menu">
            <ul class="nav navbar-nav pull-right">
                <!-- BEGIN NOTIFICATION DROPDOWN -->
                <!-- END NOTIFICATION DROPDOWN -->
                <!-- BEGIN USER LOGIN DROPDOWN -->
                <li class="dropdown dropdown-user dropdown-dark">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
                        <img alt="" class="img-circle" src="assets/img/avatar.png">
                        <span class="username username-hide-mobile">Welcome <s:property value="#session['uservalues'].name" /></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-default" style="min-width: 230px;">
                        <li>
                            <a href="profile">
                                <i class="icon-user"></i> My Profile </a>
                        </li>
                        <%
                            UserData userdata = (UserData) session.getAttribute("uservalues");
                            if (!userdata.isIsHOG()) {
                               
                        %>
                        <li>
                            <a href="showUserData">
                                <i class="icon-doc"></i> My Request </a>
                        </li>
                        <li id="reg_form">
                            <a href="Forms">
                                <i class="icon-doc"></i> Registration Forms</a>
                        </li>
                        <%    
                            }
//  ArrayList role_head = (ArrayList) session.getAttribute("role");
                            ArrayList user_role = (ArrayList)userdata.getRoles();
System.out.println("user role:::::::::"+user_role);
                            String current_role = ""; // line added by pr on 8thfeb19    
//                            if( session.getAttribute("user_role") != null )
//                            {
//                               System.out.println(" inside user_role session not null and its value is "+session.getAttribute("user_role")); 
//
//                                current_role = session.getAttribute("user_role").toString();
//                            }    
                            for (Object r : user_role) {
                                
                                String str = r.toString();
                                System.out.println("str::::::::::::::::"+str);
                            if (!str.equals("user") && !str.equals("NewUser") && !user_role.equals(str)) {
                        %>
                        <li>
                            <%                                    
                            if (str.trim().equals("ca")) // && condition added by pr on 8thfeb19
                            {
                            %>
                            <a href="showLinkData?adminValue=<%=str%>">
                                <i class="icon-doc"></i>
                                Switch to RO Panel
                            </a>
                            <% } %>
                            <%  
                                //if (str.equals("coordinator")) 
                                if (str.equals("co")) // && condition added by pr on 8thfeb19
                                {
                            %>
                            <a href="showLinkData?adminValue=co">
                                <i class="icon-doc"></i>
                                Switch to Co-ordinator Panel
                            </a>
                            <% } %>
                            <%  
                                //if (str.equals("support")) 
                                if (str.equals("sup")) // && condition added by pr on 8thfeb19
                            {%>
                            <a href="showLinkData?adminValue=sup">
                                <i class="icon-doc"></i>
                                Switch to Support Panel
                            </a>
                            <% } %>
                            <%
                            //if (str.equals("mailadmin")) 
                            if (str.equals("admin")) // && condition added by pr on 8thfeb19
                            {%>
                            <a href="showLinkData?adminValue=<%=str%>">
                                <i class="icon-doc"></i>
                                Switch to Admin Panel
                            </a>
                            <% } %>
                            <%
                             if (str.equals("dashboard")) // && condition added by pr on 8thfeb19
                            {%>
                             <a href="showDashBoard?showValue=dash">
                                <i class="icon-doc"></i>
                                Switch to Dash-Board Panel
                            </a>
                            <% } %>
                        </li>
                        <%  
                            }
}
%>
                         
                          
                                    
                        <li class="divider"> </li>
                        <li>
                            <form action="logout" method="post">
                                <button title="Click here to Logout" type="submit" style="width: 100%;border: 0;color: white;padding: 10px;background: #2e343b;"><i class="icon-key"></i> Log Out</button>
                            </form>
                        </li>
                    </ul>
                </li>
                <!-- END USER LOGIN DROPDOWN -->
                <!-- BEGIN QUICK SIDEBAR TOGGLER -->
                <!--                    <li class="dropdown dropdown-extended quick-sidebar-toggler">
                                        <span class="sr-only">Toggle Quick Sidebar</span>
                                        <form action="logout" method="post">
                                            <button title="Click here to Logout" type="submit" style="border:none;background:none;"><i class="icon-logout" style="font-size: 20px;"></i></button>
                                        </form>
                                    </li>-->
                <!-- END QUICK SIDEBAR TOGGLER -->
            </ul>
        </div>
        <!-- END TOP NAVIGATION MENU -->
    </div>
</div>
</div>
<!-- END HEADER -->
</div>
</div>
<div class="page-wrapper-row full-height page-container-bg-solid">
    <div class="page-wrapper-middle">
        <!-- BEGIN CONTAINER -->
        <div class="page-container">
            <!-- BEGIN CONTENT -->
            <div class="page-content-wrapper">
                <!-- BEGIN CONTENT BODY -->
                <!-- BEGIN PAGE HEAD-->
                <div class="page-head">
                    <div class="container">
                        <!-- BEGIN PAGE TITLE -->
                        <div class="row"><div class="col-md-4">
                                <div class="page-title">
                                    <h1><a href="https://nic-cert.nic.in/" title="Report an Incident" target="_blank"><img src="assets/img/nic-cert.png "/></a></h1>
                                </div></div>
                            <div class="col-md-4"><div class="page-title" style=""><a href="https://servicedesk.nic.in/" target="_blank" title="Service Desk"><img src="assets/images/servicedesk.png" style="margin-left:90px;"/></a></div>
                            </div class="col-md-4"><div class="page-title" style="float:right"><a href="http://digitalindia.gov.in/" title="Digital India" target="_blank"><img src="assets/img/footer/digital_india_logo.png"/></a></div></div>
                    </div>
                    <!-- END PAGE TITLE -->
                </div>
            </div>
            <!-- END PAGE HEAD-->
