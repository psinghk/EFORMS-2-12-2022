<%@page import="com.org.bean.UserOfficialData"%>
<%@page import="com.org.bean.HodData"%>
<%@page import="com.org.bean.UserData"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page import="java.util.ArrayList"%>
<%
    UserData userdata = (UserData) session.getAttribute("uservalues");
    UserOfficialData userofficialdata = (UserOfficialData) userdata.getUserOfficialData();%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="loader"><img src="assets/images/loader-1.gif" alt="" /></div>
<div id="k_header" class="k-header k-grid__item  k-header--fixed ">

    <!-- begin: Header Menu -->
    <button class="k-header-menu-wrapper-close" id="k_header_menu_mobile_close_btn"><i class="la la-close"></i></button>
    <div class="k-header-menu-wrapper" id="k_header_menu_wrapper">
        <div id="k_header_menu" class="k-header-menu k-header-menu-mobile ">
            <ul class="k-menu__nav ">
                <li class="k-menu__item k-menu__item--active">
                    <a href=""><img class="img-responsive header-image" src="assets/img/footer/nic.png" alt="nic" /></a>
                </li>
                <li class="k-menu__item k-menu__item--active">
                    <a href="eformsManual" class="btn btn-info btn-sm" target="_blank"><span class="k-menu__link-text">User Manual</span></a>
                </li>

                <%
                    if (session.getAttribute("co_list_show") != null) {
                        if (session.getAttribute("co_list_show") == "Data Found") { %>
                <li class="k-menu__item"><a href="coordinator-list" class="btn btn-info btn-sm">Coordinator List</a></li>
                    <% } else {

                        if (userdata.getUserOfficialData().getDepartment() != null) {
                            if (!userdata.getUserOfficialData().getDepartment().toLowerCase().contains("nic")) {
                    %>

                <li class="k-menu__item k-menu__item--active">
                    <span class="btn btn-info btn-sm" onclick="fetchUrCoord()">Know Your Coordinator</span>
                </li>
                <%  }
                            }
                        }
                    }
                %>

            </ul>
        </div>
    </div>
    <!-- end: Header Menu -->

    <!-- begin:: Header Topbar -->
    <div class="k-header__topbar">
        <!--begin: User bar -->
        <div class="k-header__topbar-item k-header__topbar-item--user">
            <div class="k-header__topbar-wrapper" data-toggle="dropdown" data-offset="10px -2px">
                <div class="k-header__topbar-user">
                    <span class="k-header__topbar-welcome k-hidden-mobile">Hi,</span>
                    <span class="k-header__topbar-username k-hidden-mobile"><s:property value="#session['uservalues'].name" /></span>
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
<!--                    <li class="k-nav__item k-nav__item--custom k-margin-t-15" style="border-top: 1px solid #eee;">
                        <a href="logout" class="btn btn-outline-metal btn-hover-brand btn-upper btn-font-dark btn-sm btn-bold" id="log_signout">Sign Out</a>
                    </li>-->

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
        <!--end: User bar -->
    </div>
</div>