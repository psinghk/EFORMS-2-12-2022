<%-- 
    Document   : retired-officers
    Created on : Aug 30, 2022, 9:35:31 AM
    Author     : rahul
--%>

<%@page import="java.util.Map"%>
<%@page import="com.org.dao.Ldap"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <!-- BEGIN HEAD -->
    <%
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Expires", "0");
        response.setDateHeader("Expires", -1);
        response.setHeader("X-Frame-Options", "DENY");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Set-Cookie", "key=value; HttpOnly; SameSite=strict");
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
        String url = request.getRequestURL().toString();
        String CSRFRandom = entities.Random.csrf_random();
        String email = "", mobile = "", dor = "", newdor = "", name = "";
        System.out.println("SESSION :::: " + ActionContext.getContext().getSession());
        System.out.println("SESSION CAPTCHA :::: " + session.getAttribute("captcha").toString());
        if (ActionContext.getContext().getSession() != null) {
            session.setAttribute("CSRFRandom", CSRFRandom);
            email = session.getAttribute("email").toString();
            mobile = session.getAttribute("mobile").toString();
            name = session.getAttribute("name").toString();
            newdor = session.getAttribute("newdor").toString();
            dor = session.getAttribute("dor").toString();
        }
    %>
    <head>
        <meta charset="utf-8">
        <title>@Gov.in | e-Forms</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <!--<link href="assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />-->
        <link href="assets/home-page/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/home-page/lib/bootstrap/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
        <!--<link rel="stylesheet" href="assets/home-page/update-mobile/fonts/material-design-iconic-font/css/material-design-iconic-font.css">-->
        <link rel="stylesheet" href="assets/home-page/update-mobile/css/style.css">
        <link rel="stylesheet" href="assets/home-page/retired-officer/css/style.css">        
        <link rel="stylesheet" href="assets/home-page/update-mobile/css/navigation.css">
        <!--<link href="assets/old_assets/css/jquery-ui.css?v=1.0" rel="stylesheet" type="text/css" crossorigin="anonymous" />-->
    </head>
    <body>
        <div class="loader"><img src="assets/images/loader-1.gif" alt="" /></div>
        <header id="header" class="fixed-top">
            <div class="container">
                <div class="logo float-left">
                    <a class="scrollto" href="https://eforms.nic.in/"><img src="assets/home-page/img/eforms-logo.png" alt="" class="img-fluid"></a>
                </div>
            </div>
        </header>
        <div class="wrapper display-show" id="retired_basic">
            <div class="col-md-6 m-auto">
                <div id="wizard">
                    <div class="swal2-icon swal2-success swal2-animate-success-icon" style="display: flex;">
                        <div class="swal2-success-circular-line-left" style="background-color: rgb(255, 255, 255);"></div>
                        <span class="swal2-success-line-tip"></span>
                        <span class="swal2-success-line-long"></span>
                        <div class="swal2-success-ring"></div>
                        <div class="swal2-success-fix" style="background-color: rgb(255, 255, 255);"></div>
                        <div class="swal2-success-circular-line-right" style="background-color: rgb(255, 255, 255);"></div>
                    </div>
                    <p class="text-center text-success"><b><%=session.getAttribute("email").toString()%></b>, your account validity has been extended till <b><%=session.getAttribute("newdor").toString()%></b></p>
                    <div class="retired-users">
                        <h2 class="head-3 mt-4 mb-2" style="text-decoration: underline;font-size:15px">Extended Validity Account Details</h2>
                        <div class="form-group">
                            <table style="font-size:12px">
                                <tr>
                                    <td><b>Name</b></td>
                                    <td> &nbsp;&nbsp;&nbsp;&nbsp; :&nbsp;&nbsp;&nbsp;&nbsp; </td>
                                    <td><%=session.getAttribute("name").toString()%></td>
                                </tr>
                                <tr>
                                    <td><b>Email Address</b></td>
                                    <td> &nbsp;&nbsp;&nbsp;&nbsp; :&nbsp;&nbsp;&nbsp;&nbsp; </td>
                                    <td><%=session.getAttribute("email").toString()%></td>
                                </tr>
                                <tr>
                                    <td><b>Previous Date Of Account Expiry</b></td>
                                    <td> &nbsp;&nbsp;&nbsp;&nbsp; :&nbsp;&nbsp;&nbsp;&nbsp; </td>
                                    <td><%=session.getAttribute("dor").toString()%></td>
                                </tr>
                                <tr>
                                    <td><b>Updated Date Of Account Expiry</b></td>
                                    <td> &nbsp;&nbsp;&nbsp;&nbsp; :&nbsp;&nbsp;&nbsp;&nbsp; </td>
                                    <td><%=session.getAttribute("newdor").toString()%></td>
                                </tr>
                            </table>
                        </div>
                    </div>
                    <div style="overflow:auto;" class="mt-2">
                        <div class="col-md-12 p-0">
                            <button type="button" class="btn btn-danger btn-action float-right" id="submit"><a href="logout_parichay_retired" style="color: #ffff;">Logout</a></button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--==========================
        Clients Section
        ============================-->
        <section id="clients" class="section-bg section-bg-logo">
            <div class="container">
                <ul class="list-unstyled partners-icon row">
                    <li class="col-md-2 col-4">
                        <a href="https://india.gov.in/" target="_blank"><img src="assets/img/footer/new/india_gov.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-left border-right">
                        <a href="http://nkn.gov.in/" target="_blank"><img src="assets/img/footer/new/nkn.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-right">
                        <a href="http://meity.gov.in/" target="_blank"><img src="assets/img/footer/new/meity.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-right mt-md-0 mt-3">
                        <a href="http://www.digitalindia.gov.in/" target="_blank"><img src="assets/img/footer/new/digital_india.png"></a>
                    </li>
                    <li class="col-md-2 col-4 border-right mt-md-0 mt-3">
                        <a href="https://email.gov.in/" target="_blank"><img src="assets/img/footer/new/gov.png"></a>
                    </li>
                    <li class="col-md-2 col-4 mt-md-0 mt-3">
                        <a href="https://mygov.in/" target="_blank"><img src="assets/img/footer/new/mygov.png"></a>
                    </li>
                </ul>
            </div>
        </section>
    </main>

    <!--==========================
    Footer
    ============================-->
    <footer id="footer">
        <div class="container">
            <div class="copyright">
                Designed and Developed by Messaging Division <br />
                <span id="year"></span>&nbsp;&copy; Copyright <a href="https://www.nic.in/" style="color:#fff;" target="_blank" title="External Link"><strong>National Informatics Centre</strong></a>.
            </div>
            <div class="credits">
                All Rights Reserved
            </div>
        </div>
    </footer>
    <!--<script src="main_js/retired-officers.js" type="text/javascript"></script>-->
    <!--<script src="assets/vendors/general/jquery/dist/jquery.js?v=1.0" type="text/javascript"></script>-->
    <!--<script src="assets/old_assets/plugins/jquery.serializeObject.js?v=1.0"></script>-->
    <!--<script src="assets/home-page/update-mobile/js/navigation.js"></script>-->
    <!--<script src="assets/home-page/lib/bootstrap/js/bootstrap.bundle.min.js"></script>-->
    <!--<script src="assets/old_assets/plugins/jquery-ui.js?v=1.0"></script>-->
    <!--<script src="assets/datepickertime/js/moment.min.js" type="text/javascript"></script>-->
    <!--<script src="assets/datepickertime/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>-->
</body>
</html>

