<%@page import="com.org.bean.UserOfficialData"%>
<%@page import="com.org.bean.HodData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page import="java.util.ArrayList"%>
<%
    UserData userdata = (UserData) session.getAttribute("uservalues");
    UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData(); 
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>e-Forms | Support,Component Authority,Mail-Admin | @Gov.in</title>
        <meta name="description" content="e-Forms | Support,Component Authority,Mail-Admin | @Gov.in">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <link rel="shortcut icon" href="assets/media/logos/favicon.ico" />
        <link href="assets/demo/default/base/style.bundle.css?v=1.0" rel="stylesheet" type="text/css" rel="stylesheet" />
        <link href="assets/css/fonts/webfonts.css?v=1.0" rel="stylesheet" type="text/css" rel="stylesheet" />  
        <link href="assets/demo/default/skins/header/base/light.css?v=1.0" rel="stylesheet" type="text/css" rel="stylesheet" />
        <link href="assets/demo/default/skins/brand/brand.css?v=1.0" rel="stylesheet" type="text/css" rel="stylesheet" />
        <link href="assets/demo/default/skins/aside/brand.css?v=1.0" rel="stylesheet" type="text/css" rel="stylesheet" />
        <link rel="stylesheet" href="assets/custom/custom.css?v=1.0" rel="stylesheet" crossorigin="anonymous" />
        <link href="assets/old_assets/css/jquery-ui.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />
        <link href="assets/vendors/general/perfect-scrollbar/css/perfect-scrollbar.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />
        <link href="assets/vendors/custom/datatables/datatables.bundle.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />
        <link href="assets/vendors/custom/vendors/fontawesome5/css/all.min.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />
        <link href="assets/demo/bootstrap-datetime-picker/css/bootstrap-datetimepicker.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />
        <link href="assets/custom/datatable/css/responsive.bootstrap.min.css?v=1.0" rel="stylesheet" type="text/css" rel="stylesheet" crossorigin="anonymous" />
    </head>
    <!-- begin::Body -->
    <body class="k-header--fixed k-header-mobile--fixed k-aside--enabled k-aside--fixed page-wrapper page-container-bg-solid">

        <!-- begin:: Page -->

        <!-- begin:: Header Mobile -->
        <div id="k_header_mobile" class="k-header-mobile  k-header-mobile--fixed ">
            <div class="k-header-mobile__logo">
                <a href="showUserData">
                    <img alt="Logo" src="assets/media/logos/logo-1.png" />
                </a>
            </div>
            <div class="k-header-mobile__toolbar">
                <button class="k-header-mobile__toolbar-toggler k-header-mobile__toolbar-toggler--left" id="k_aside_mobile_toggler"><span></span></button>
                <button class="k-header-mobile__toolbar-toggler" id="k_header_mobile_toggler"><span></span></button>
                <div class="k-header__topbar-item k-header__topbar-item--user">
                    <div class="k-header__topbar-wrapper" data-toggle="dropdown" data-offset="10px -2px">
                        <div class="k-header__topbar-user mobile-view">
                            <img alt="Pic" src="assets/media/users/300_25.jpg" />
                            <span class="k-badge k-badge--username k-badge--lg k-badge--brand k-hidden">A</span>
                        </div>
                    </div>
                    <div class="dropdown-menu dropdown-menu-fit dropdown-menu-right dropdown-menu-anim dropdown-menu-top-unround dropdown-menu-md">
                        <div class="k-user-card k-margin-b-50 k-margin-b-30-tablet-and-mobile" style="background-image: url(assets/media/misc/head_bg_sm.jpg)">
                            <div class="k-user-card__wrapper">
                                <div class="k-user-card__pic">
                                    <img alt="Pic" src="assets/media/users/300_21.jpg" />
                                </div>
                                <div class="k-user-card__details">
                                    <div class="k-user-card__name"><s:property value="#session['uservalues'].name" /></div>
                                    <div class="k-user-card__position"><%=userofficialdata.getDesignation()%></div>
                                </div>
                            </div>
                        </div>
                        <ul class="k-nav k-margin-b-10">
                            <li class="k-nav__item">
                                <a href="profile" class="k-nav__link">
                                    <span class="k-nav__link-icon"><i class="flaticon2-calendar-3"></i></span>
                                    <span class="k-nav__link-text">My Profile</span>
                                </a>
                            </li>
                            <li class="k-nav__item">
                                <a href="showUserData" class="k-nav__link">
                                    <span class="k-nav__link-icon"><i class="flaticon2-browser-2"></i></span>
                                    <span class="k-nav__link-text">My Request</span>
                                </a>
                            </li>
                            <li class="k-nav__item k-nav__item--custom k-margin-t-15" style="border-top: 1px solid #eee;">
                                <% if (session.getAttribute("loginType") != null && session.getAttribute("loginType").equals("sso")) { %> 
                                <a href="logout_parichay" class="btn btn-outline-metal btn-hover-brand btn-upper btn-font-dark btn-sm btn-bold" id="log_signout">Sign Out</a>
                                <% } else { %> 
                                <a href="logout" class="btn btn-outline-metal btn-hover-brand btn-upper btn-font-dark btn-sm btn-bold" id="log_signout">Sign Out</a>
                                <% }%>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <!-- end:: Header Mobile -->
        <div class="k-grid k-grid--hor k-grid--root">
            <div class="k-grid__item k-grid__item--fluid k-grid k-grid--ver k-page">

                <!-- begin:: Aside -->
                <button class="k-aside-close " id="k_aside_close_btn"><i class="fa fa-angle-left"></i></button>
                <div class="k-aside  k-aside--fixed k-grid__item k-grid k-grid--desktop k-grid--hor-desktop" id="k_aside">

                    <!-- begin:: Aside -->
                    <div class="k-aside__brand	k-grid__item " id="k_aside_brand">
                        <div class="k-aside__brand-logo">
                            <a href="showUserData">
                                <img alt="Logo" src="assets/media/logos/logo-1.png" />
                            </a>
                        </div>
                        <div class="k-aside__brand-tools">
                            <button class="k-aside__brand-aside-toggler k-aside__brand-aside-toggler--left" id="k_aside_toggler"><span></span></button>
                        </div>
                    </div>

                    <!-- end:: Aside -->

                    <!-- begin:: Aside Menu -->
                    <div class="k-aside-menu-wrapper	k-grid__item k-grid__item--fluid" id="k_aside_menu_wrapper">
                        <div id="k_aside_menu" class="k-aside-menu " data-kmenu-vertical="1" data-kmenu-scroll="1" data-kmenu-dropdown-timeout="500">
                            <!-- begin:: Aside Menu -->
                            <div class="k-aside-menu-wrapper	k-grid__item k-grid__item--fluid" id="k_aside_menu_wrapper">
                                <div id="k_aside_menu" class="k-aside-menu " data-kmenu-vertical="1" data-kmenu-scroll="1" data-kmenu-dropdown-timeout="500">
                                    <ul class="k-menu__nav">
                                        <li class="k-menu__item k-menu__item--submenu k-menu__item--here k-menu__item--open" aria-haspopup="true" data-kmenu-submenu-toggle="hover"><a href="javascript:;" class="k-menu__link k-menu__toggle"><i class="k-menu__link-icon fa fa-chalkboard-teacher"></i><span class="k-menu__link-text">Dashboards</span><i class="k-menu__ver-arrow fa fa-angle-right"></i></a>  
                                            <div class="k-menu__submenu "><span class="k-menu__arrow"></span>
                                                <ul class="k-menu__subnav">
                                                    <% if (!userdata.isIsHOG()) { %>
                                                    <li class="k-menu__item" aria-haspopup="true"> <a href="showUserData"class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">My Request</span></a></li>
                                                                    <%   }
                                                                        System.out.println("userdata.isIsNewUser()" + userdata.isIsNewUser());
                                                                        ArrayList user_role = (ArrayList) userdata.getRoles();
                                                                        System.out.println("user role:::::::::" + user_role);
                                                                        String current_role = "";
                                                                        for (Object r : user_role) {
                                                                            String str = r.toString();
                                                                            System.out.println("str::::::::::::::::" + str + "is new user " + userdata.isIsNewUser() + "session update mobile" + session.getAttribute("update_without_oldmobile"));
                                                                            if (!str.equals("user") && !str.equals("NewUser") && !user_role.equals(str)) { %>
                                                                    <%   if (str.trim().equals("ca")) {%>
                                                    <li class="k-menu__item upmobile" aria-haspopup="true"><a <% if (session.getAttribute("update_without_oldmobile") != "yes") {%> href="showLinkData?adminValue=<%=str%>" <% } %> class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">RO Panel</span></a></li>
                                                                    <% } %>
                                                                    <% if (str.equals("co")) { %>
                                                    <li class="k-menu__item upmobile" aria-haspopup="true"><a <% if (session.getAttribute("update_without_oldmobile") != "yes") { %> href="showLinkData?adminValue=co" <% } %> class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">Co-ordinator Panel</span></a></li>
                                                                    <% } %>
                                                                    <% if (str.equals("sup")) { %>
                                                    <li class="k-menu__item upmobile " aria-haspopup="true"><a <% if (session.getAttribute("update_without_oldmobile") != "yes") { %> href="showLinkData?adminValue=sup" <% } %> class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">Support Panel</span></a></li>
                                                                    <% } %>
                                                                    <% if (str.equals("admin")) {%>
                                                    <li class="k-menu__item upmobile" aria-haspopup="true"><a <% if (session.getAttribute("update_without_oldmobile") != "yes") {%> href="showLinkData?adminValue=<%=str%>" <% } %> class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">Admin Panel</span></a></li>
                                                                    <% } %>
                                                                    <% if (str.equals("dashboard")) { %>
                                                    <li class="k-menu__item " aria-haspopup="true"><a <% if (session.getAttribute("update_without_oldmobile") != "yes") { %> href="showDashBoard?showValue=dash" <% } %> class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">Dash-Board Panel</span></a></li>
                                                                    <% }
                                                                            }
                                                                        }%>
                                                                    <% if (userdata.getEmail().equalsIgnoreCase("vpnsupport@nic.in") ||userdata.getEmail().equalsIgnoreCase("Sandeep.kumar9@nic.in")
                                                                            || userdata.getEmail().equalsIgnoreCase("vp.agrawal@nic.in")|| userdata.getEmail().equalsIgnoreCase("prog13.nhq-dl@nic.in")) { %>
                                                    <li class="k-menu__item " aria-haspopup="true"><a <% if (session.getAttribute("update_without_oldmobile") != "yes") { %> href="updateCO?showValue=updateCO" <% } %> class="k-menu__link"><i class="k-menu__link-bullet k-menu__link-bullet--dot"><span></span></i><span class="k-menu__link-text">Update Co-ordinator Panel</span></a></li>
                                                                    <% } %>
                                                </ul>
                                            </div>
                                        </li>
                                        <li class="k-menu__section ">
                                            <h4 class="k-menu__section-text">Our Services</h4>
                                            <i class="k-menu__section-icon flaticon-more-v2"></i>
                                        </li>
<!--<<<<<<< HEAD-->

                                        
<!--                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="ldap" class="k-menu__link"><i class="k-menu__link-icon fa fa-user-lock"></i><span class="k-menu__link-text">Authentication Services (LDAP)</span></a></li>-->
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile") != "yes") {%> href="Daonboarding_registration" data-value="da" <% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-user-lock"></i><span class="k-menu__link-text">DA Onboarding</span></a></li>                                        
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="dlist" class="k-menu__link"><i class="k-menu__link-icon fa fa-share-alt"></i><span class="k-menu__link-text">Distribution List Services</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="dns" class="k-menu__link"><i class="k-menu__link-icon fa fa-sort-amount-up"></i><span class="k-menu__link-text">DNS Services</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="email" class="k-menu__link"><i class="k-menu__link-icon fa fa-mail-bulk"></i><span class="k-menu__link-text">Email (@gov)</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a  href="#" data-value="imap" class="k-menu__link"><i class="k-menu__link-icon fa fa-recycle"></i><span class="k-menu__link-text">IMAP/POP</span></a></li>
                                        <!--28-apr-2022-->
<!--                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="ip" class="k-menu__link" ><i class="k-menu__link-icon fa fa-signal"></i><span class="k-menu__link-text">IP Change Requests</span></a></li>-->
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="sms" class="k-menu__link"><i class="k-menu__link-icon fa fa-envelope-open"></i><span class="k-menu__link-text">SMS Service</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="relay" class="k-menu__link"><i class="k-menu__link-icon fa fa-server"></i><span class="k-menu__link-text">SMTP Gateway</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" class="k-menu__link" data-value="mobile"><i class="k-menu__link-icon fa fa-mobile-alt"></i><span class="k-menu__link-text">Update Profile in(@gov)</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="vpn" class="k-menu__link" title="Virtual Private Network Services"><i class="k-menu__link-icon fa fa-sitemap"></i><span class="k-menu__link-text">VPN Service</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" data-value="wifi" class="k-menu__link" ><i class="k-menu__link-icon fa fa-wifi"></i><span class="k-menu__link-text">WIFI Service</span></a></li>
<!--                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a  href="#" data-value="webcast" class="k-menu__link" title="Request for webcast"><i class="k-menu__link-icon fa fa-broadcast-tower"></i><span class="k-menu__link-text">Webcast Services</span></a></li>
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a  href="#" data-value="utm" class="k-menu__link" title="Request for Firewall"><i class="k-menu__link-icon fa fa-broadcast-tower"></i><span class="k-menu__link-text">Firewall Services</span></a></li>-->
                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a  href="Wifiport_registration" data-value="wifiport"  class="k-menu__link" ><i class="k-menu__link-icon fa fa-wifi"></i><span class="k-menu__link-text">WiFi Port Services</span></a></li>
<!--                                        <li class="k-menu__item form-selection" aria-haspopup="true"><a  href="cooPannel" data-value="cooPannel"  class="k-menu__link" ><i class="k-menu__link-icon fa fa-wifi"></i><span class="k-menu__link-text">Coopannel</span></a></li>
                                         <li class="k-menu__item form-selection" aria-haspopup="true"><a href="cooPannel" data-value="cooPannel" class="k-menu__link"><i class="k-menu__link-icon fa fa-user-lock"></i><span class="k-menu__link-text">COOPANEL</span></a></li>-->
<!--=======
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile") != "yes") {%> href="Ldap_registration" data-value="ldap" <% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-user-lock"></i><span class="k-menu__link-text">Authentication Services (LDAP)</span></a></li>
                                        <li class="k-menu__item email_invalid upmobile " aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile") != "yes") {%> href="Distribution_registration" data-value="dlist"<% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-share-alt"></i><span class="k-menu__link-text">Distribution List Services</span></a></li>
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a href="move_request1" data-value="move" class="k-menu__link" title="Request for Live/on-demand Webcast"><i class="k-menu__link-icon fa fa-exchange-alt"></i><span class="k-menu__link-text">Move Request</span></a></li>    
                                        <li class="k-menu__item email_invalid upmobile " aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile") != "yes") {%> href="Dns_registration" data-value="dns"<% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-sort-amount-up"></i><span class="k-menu__link-text">DNS Services</span></a></li>
                                        <li class="k-menu__item nongovuser" aria-haspopup="true"><a <% if (!userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile") != "yes") {%> href="Email_registration" data-value="email"<% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-mail-bulk"></i><span class="k-menu__link-text">Email (@gov)</span></a></li>
                                        <li class="k-menu__item form-selection email_invalid" aria-haspopup="true"><a <% //if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Firewall_registration" data-value="utm"<% //} %> class="k-menu__link" ><i class="k-menu__link-icon fa fa fa-user-shield"></i><span class="k-menu__link-text">Central UTM Services</span></a></li>
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true" value="imap"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="ImapPop_registration" data-value="imap" <% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-recycle"></i><span class="k-menu__link-text">IMAP/POP</span></a></li>
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Change_ip" data-value="ip"<% } %> class="k-menu__link" ><i class="k-menu__link-icon fa fa-signal"></i><span class="k-menu__link-text">IP Change Requests</span></a></li>
                                        <li class="k-menu__item form-selection upmobile " aria-haspopup="true"><a <% if (!userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Sms_Registration" data-value="sms"<% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-envelope-open"></i><span class="k-menu__link-text">SMS Service</span></a></li>
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Relay_registration" data-value="relay"<% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-server"></i><span class="k-menu__link-text">SMTP Gateway</span></a></li>
                                        <li class="k-menu__item email_invalid <% if(session.getAttribute("update_without_oldmobile")!= "yes") {%> upmobile <% } %> <% if(!userdata.isIsNewUser()){ %> nongovuser <% } %>" aria-haspopup="true"><a<% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser()){ %> href="Mobile_registration" <% } %> class="k-menu__link" data-value="mobile"><i class="k-menu__link-icon fa fa-mobile-alt"></i><span class="k-menu__link-text">Update Mobile in(@gov)</span></a></li>
                                        <li class="k-menu__item form-selection nongovuser" aria-haspopup="true"><a <% if (!userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Vpn_registration" data-value="vpn"<% } %> class="k-menu__link" title="Virtual Private Network Services"><i class="k-menu__link-icon fa fa-sitemap"></i><span class="k-menu__link-text">VPN Service</span></a></li>
                                        <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Wifi_registration" data-value="wifi" <% } %> class="k-menu__link" ><i class="k-menu__link-icon fa fa-wifi"></i><span class="k-menu__link-text">WIFI Service</span></a></li>
					 <li class="k-menu__item form-selection" aria-haspopup="true"><a  href="#" data-value="wifiport" class="k-menu__link" title="Request for WiFi Port"><i class="k-menu__link-icon fa fa-broadcast-tower"></i><span class="k-menu__link-text">WiFi Port Services</span></a></li>
                                          <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Wifiport_registration" data-value="wifiport" <% } %> class="k-menu__link" ><i class="k-menu__link-icon fa fa-wifi"></i><span class="k-menu__link-text">WiFi Port Services</span></a></li>
                                         <li class="k-menu__item email_invalid upmobile" aria-haspopup="true"><a <% if (userdata.isIsEmailValidated() && !userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="Webcast_registration" data-value="webcast" <% }%> class="k-menu__link" title="Request for Live/on-demand Webcast"><i class="k-menu__link-icon fa fa-broadcast-tower"></i><span class="k-menu__link-text">Webcast Services</span></a></li>
                                         
>>>>>>> moveRequestFinalBranch-->
                                        <li class="k-menu__section ">
                                            <h4 class="k-menu__section-text">External Services</h4>
                                            <i class="k-menu__section-icon flaticon-more-v2"></i>

                                        </li>
                                        <li class="k-menu__item" aria-haspopup="true"><a href="https://cloud.gov.in/registration.php" data-value="cloud" class="k-menu__link" target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-cloud"></i><span class="k-menu__link-text">Cloud</span></a></li>
                                        <li class="k-menu__item" aria-haspopup="true"><a href="https://registry.gov.in/" data-value="gov" class="k-menu__link" target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-receipt"></i><span class="k-menu__link-text">Domain Registration</span></a></li>
                                        <li class="k-menu__item" aria-haspopup="true"><a href="https://sampark.gov.in/Sampark/guidelines/guidelines.html" data-value="sampark" class="k-menu__link" target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-handshake"></i><span class="k-menu__link-text">Sampark</span></a></li>
                                        <li class="k-menu__item" aria-haspopup="true"><a href="https://asams.nic.in/ASAMS/index.aspx" data-value="audit" class="k-menu__link"  target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-user-secret"></i><span class="k-menu__link-text">Security Audit</span></a></li>
                                        <li class="k-menu__item" aria-haspopup="true"><a href="https://reserve.nic.in/" data-value="video" class="k-menu__link" target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-video"></i><span class="k-menu__link-text">Video Conference</span></a></li>
                                        <li class="k-menu__item" aria-haspopup="true"><a href="https://security.nic.in/WAF/Default.aspx" data-value="waf" class="k-menu__link" target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-shield-alt"></i><span class="k-menu__link-text">WAF</span></a></li>
                                        <!--<li class="k-menu__item" aria-haspopup="true"><a <% if (!userdata.isIsNewUser() && session.getAttribute("update_without_oldmobile")!="yes") {%> href="https://security.nic.in/WAF/Default.aspx" target="_blank" title="External site that opens in a new window" data-value="waf" <% } %> class="k-menu__link"><i class="k-menu__link-icon fa fa-shield-alt"></i><span class="k-menu__link-text">WAF</span></a></li>-->
                                        <!--<li class="k-menu__item form-selection" aria-haspopup="true"><a href="#" class="k-menu__link" data-value="firewall" target="_blank" title="External site that opens in a new window"><i class="k-menu__link-icon fa fa-user-shield"></i><span class="k-menu__link-text">Firewall</span></a></li>-->

                                    </ul>
                                </div>
                            </div>
                            <!-- end:: Aside Menu -->
                        </div>
                    </div>
                    <!-- end:: Aside Menu -->
                </div>
                <div class="k-grid__item k-grid__item--fluid k-grid k-grid--hor k-wrapper" id="k_wrapper">