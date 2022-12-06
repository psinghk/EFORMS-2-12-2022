<!DOCTYPE html>
<html class="no-js" lang="en">
    <!--<![endif]-->
    <head>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
%>
       
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">        
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>
        <META HTTP-EQUIV="EXPIRES" CONTENT="0"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>
        <META HTTP-EQUIV="X-Frame-Options" CONTENT="DENY"/>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="keywords" content="">
        <meta name="author" content="">
        <title>Welcome to e-Forms</title>
        <link href='assets1/css/bootstrap.min.css' rel='stylesheet'>
        <link href='assets1/css/style.css' rel='stylesheet'>
        <!-- color style start -->
        <link href='assets1/css/color/red.css' rel='stylesheet'>
        <!-- color style end -->
        <link href='assets1/css/custom.css' rel='stylesheet'>
        <!--[if gte IE 9]
        <style type="text/css">
        .gradient {filter: none!important;}
        </style>
        <![endif]-->
        <!--[if lt IE 9]>
        <script type="text/javascript" src="assets/js/ie/html5shiv.min.js"></script>
        <script type="text/javascript" src="assets/js/ie/respond.min.js"></script>
        <![endif]-->
        <!--<link rel="icon" href="assets/img/favicon.ico">-->
    </head>
    <body>
        <!-- preloader -->
        <div id="preloader">
            <div id="preloader-img"></div>
        </div>
        <!-- /preloader -->
        <!-- border bar start -->
        <div class="border border-top"></div>
        <div class="border border-left"></div>
        <div class="border border-right"></div>
        <div class="border border-bottom"></div>
        <!-- border bar end -->
        <!-- header logo start -->
        <a id="header" href="https://mail.gov.in" target="_blank" title="logo">
            <img src="assets1/img/mail-logo.png" alt="logo" id="header-logo" class="wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.4s">
        </a>
        <a id="header1" href="http://digitalindia.gov.in/" target="_blank" title="Digital India" style="position: absolute;top: 20px;right: 20px;left:230px;z-index:1050;">
            <img src="assets1/img/digital_india_logo.png" alt="logo" id="header-logo1" class="wow  fadeInUp animated" data-wow-duration="1s" data-wow-delay="0.4s" style="position: absolute;right: 0px;">
        </a>
        <a id="header1" href="http://meity.gov.in/" target="_blank" title="MIETY" style="position: absolute;bottom: 20px;right: 20px;left:230px;z-index:1050;">
            <img src="assets1/img/meity_logo.png" alt="logo" id="header-logo1" class="wow  fadeInUp  animated" data-wow-duration="1s" data-wow-delay="0.4s" style="position: absolute;bottom: 0px;right: 0px;">
        </a>
        <a id="header1" href="http://www.nic.in/" target="_blank" title="National Informatics Center" style="position: absolute;bottom: 20px;left: 20px;right: 0px;z-index:1050;" >
            <img src="assets1/img/NIC.png" alt="logo" id="header-logo1" class="wow  fadeInUp   animated" data-wow-duration="1s" data-wow-delay="0.4s" style="position: absolute; bottom: 0px; left: 0px;">
        </a>
        <!-- header logo end -->
        <!-- wrap start -->
        <div id="wrap">
            <!-- home start -->
            <section id="intro">
                <div class="overlays"></div>
                <canvas id="canvas"></canvas>
                <div id="intro-wrapper" class="tb">
                    <div id="intro-inner" class="tb-cell">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-12">
                                    <h1 class="intro-title wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.5s"><span style="text-transform:lowercase;">e</span>-<span class="color">Forms</span></h1>
                                    <p class="intro-desc wow animated fadeInDown" data-wow-duration="1s" data-wow-delay="0.6s">Forms for NIC services</p> 
                                    <!-- button grid -->
                                    <div class="btn-grid wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.8s">
                                        <a href="#service" class="btn btn-50 rounded btn-default">Continue</a>
                                    </div>
                                    <!-- /button grid -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <!-- home end -->
            <!-- service start -->
            <section id="service">
                <div class="container">
                    <div class="row">
                        <div class="col-md-3 col-service wow animated fadeInUp" data-wow-duration="1s">
                            <a href="signup_user"><i class="icons icon icon-Resume"></i>
                                <h3>Registration <span class="color">forms</span></h3></a>
                            <div class="separator"></div>
                            <p>Registration for NIC Services. You can apply for User eMail creation, SMS services and many more services. </p>
                        </div>
                        <div class="col-md-3 col-service wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.2s">
                            <a href="signup_track"><i class="icons icon icon-Pointer"></i>
                                <h3>Track Form <span class="color">Status</span></h3></a>
                            <div class="separator"></div>
                            <p>User can track the status of his/her application and can modify or cancel the request.</p> 
                        </div>
                        <div class="col-md-3 col-service wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.4s">
                            <a href="signup_ca"><i class="icons icon icon-User"></i>
                                <h3>Reporting <span class="color">Officer</span></h3></a>
                            <div class="separator"></div>
                            <p>Reporting/Nodal/Forwarding Officer can approve/reject the request sent by the user and he/she can modify the form and track the status of the request. </p>
                        </div>
                        <div class="col-md-3 col-service wow animated fadeInUp" data-wow-duration="1s" data-wow-delay="0.4s">
                            <a href="signup_coordinator"><i class="icons icon icon-Users"></i>
                                <h3>Coordinator <span class="color">Login</span></h3></a>
                            <div class="separator"></div>
                            <p>Console for Coordinator Login to process the Application request.</p>
                        </div>
                    </div>
                </div>
                <div style="text-align:center;margin-top: 46px;font-size: larger;color:#0c1335;" class="alert alert-success">
<!--                    <b>GEM(PSU)</b> users should click <b>GEM subscription</b> forms only to create the IDs. For GEM Registration Policy <a href="https://assets-bg.gem.gov.in/resources/pdf/Buyer_Registration_V1.2.pdf"     target="_blank">click here </a><br/>
                       -->
                    
                    <b>GEM(PSU)</b> users should click <b>GEM subscription</b> forms only to create the IDs. For GEM Registration Policy <a href="download_10"  target="_blank">click here </a><br/>
                
                    <strong>The site can be best viewed in the latest version of Chrome, Firefox, Safari, Opera, Internet Explorer (11 +). </strong>
                    </br>
                     <b>Download Eforms Manual<a href="download_11"  target="_blank"> click here </a><br/>
                
                </div>
            </section>
            <!-- service end -->
            <!-- footer start -->
            <footer id="site-footer" style="opacity: 0.75; background: -webkit-linear-gradient(-45deg, #be2121 0%, #be2121 30%, #191691 75%, #191691 100%);">
                <div class="container">
                    <div class="row">
                        <div class="col-md-12">
                            <!-- social list start -->
                            <ul class="social-list">
                                <li class="wow animated fadeInUp" data-wow-duration="1s">
                                    <a href="https://www.india.gov.in/" title="" target="_blank"> 
                                        <img src="assets1/img/india_gov_logo.png" alt="india_gov_logo" class="marg-footer">
                                    </a>
                                </li>
                                <li class="wow animated rollIn" data-wow-duration="1s" data-wow-delay="0.2s">
                                    <a href="http://nkn.gov.in/" title="" target="_blank">
                                        <img src="assets1/img/nkn-logo.png" alt="nkn-logo" class="marg-footer">
                                    </a>
                                </li>
                                <li class="wow animated rollIn" data-wow-duration="1s" data-wow-delay="0.4s">
                                    <a href="http://meity.gov.in/" title="" target="_blank">
                                        <img src="assets1/img/meity_logo.png" alt="meity_logo" class="marg-footer">
                                    </a>
                                </li>
                                <li class="wow animated rollIn" data-wow-duration="1s" data-wow-delay="0.6s">
                                    <a href="http://www.pmindia.gov.in/en/" title="" target="_blank">
                                        <img src="assets1/img/pm_india_logo.png" alt="pm_india_logo" class="marg-footer">
                                    </a>
                                </li>
                                <li class="wow animated rollIn" data-wow-duration="1s" data-wow-delay="0.8s">
                                    <a href="http://www.digitalindia.gov.in/" title="" target="_blank">
                                        <img src="assets1/img/digital_india_logo.png" alt="digital_india_logo" class="marg-footer">
                                    </a>
                                </li>
                                <li class="wow animated rollIn" data-wow-duration="1s" data-wow-delay="0.8s">
                                    <a href="https://mail.gov.in/" title="" target="_blank">
                                        <img src="assets/img/footer/mail-logo.png" alt="mail-footer-logo" class="marg-footer">
                                    </a>
                                </li>
                                <li class="wow animated rollIn" data-wow-duration="1s" data-wow-delay="0.8s">
                                    <a href="https://mygov.in/" title="" target="_blank">
                                        <img src="assets/img/footer/mygov-footer-logo.png" alt="mygov_logs" class="marg-footer">
                                    </a>
                                </li>
                            </ul>
                            <!-- social end -->
                            <div class="copyright wow  fadeInUp animated" data-wow-duration="1s" data-wow-delay="0.5s" style="visibility: visible; animation-duration: 1s; animation-delay: 0.5s; animation-name: fadeInUp;color:#fff;font-weight:bolder;">Designed and Developed by Messaging Division<br>National Informatics Centre<br>&copy; 2018</div>
                            <div class="copyright wow  fadeInUp  animated" data-wow-duration="1s" data-wow-delay="0.5s" style="visibility: visible; animation-duration: 1s; animation-delay: 0.5s; animation-name: fadeInUp;font-weight:bolder;padding-top:15px;">
                                <a href="signup_sup" title="Support" class="btn btn-50 rounded btn-info" style="padding:5px 12px;"><i class="fa fa-desktop"></i> Support Login</a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="signup_admin" title="Admin" class="btn btn-50 rounded btn-warning" style="padding:5px 12px;"><i class="fa fa-user"></i> Admin Login</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="https://mailadmin.nic.in/DAadmin" title="DA" class="btn btn-50 rounded btn-success" style="padding:5px 12px;"><i class="fa fa-user-secret"></i> Delegated Admin Login</a>
                            </div>
                        </div>
                    </div>
                </div>
            </footer>
            <!-- footer end -->
        </div>
        <!-- wrap end -->
        <!-- script start -->
        <script type='text/javascript' src='assets1/js/jquery-1.11.2.min.js'></script>
        <!--[if lt IE 10]>
        <script type='text/javascript' src='assets/js/ie/jquery.placeholder.min.js'></script>
        <script type='text/javascript' src='assets/js/ie/ie.js'></script>
        <![endif]-->
        <script type='text/javascript' src='assets1/js/bootstrap.min.js'></script>
        <script type='text/javascript' src='assets1/js/plugin.js'></script>
        <!--[if gte IE 10]><!-->
        <!--<![endif]-->
        <script type='text/javascript' src='assets1/js/variable-static.js'></script>
        <script type='text/javascript' src='assets1/js/main.js'></script>
        <!-- script end -->
    </body>