<%@taglib uri="/struts-tags" prefix="s"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <!-- BEGIN HEAD -->
    <%
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("Cache-control", "no-store");
        response.setHeader("Cache-control", "pre-check=0");
        response.setHeader("Cache-control", "private");
        //response.setHeader("Cache-control","post-check=0");
        response.setHeader("Cache-control", "must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");
        String url = request.getRequestURL().toString();
        String random = entities.Random.csrf_random();
        session.setAttribute("rand", random);
        if (session.getAttribute("login") != null) {
            response.sendRedirect("showUserData");
        }
    %>

    <head>
        <meta charset="utf-8">
        <title>@Gov.in | e-Forms</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <meta content="" name="keywords">
        <meta content="" name="description">
        <!-- Favicons -->
        <link href="assets/home-page/img/favicon.ico" rel="icon">
        <link href="assets/home-page/css/fonts/exo.css" rel="stylesheet">
        <!-- Bootstrap CSS File -->
        <link href="assets/home-page/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
        <link href="assets/vendors/custom/vendors/fontawesome5/css/all.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/home-page/lib/animate/animate.min.css" rel="stylesheet">
        <link href="assets/home-page/lib/ionicons/css/ionicons.min.css" rel="stylesheet">
        <link href="assets/home-page/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">
        <link href="assets/home-page/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
        <link href="assets/css/login.min.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" href="assets/home-page/fancybox-master/dist/jquery.fancybox.min.css" />
        <!-- Main Stylesheet File -->
        <link href="assets/home-page/css/style.css" rel="stylesheet">
        <link href="assets/home-page/css/custom.css" rel="stylesheet">
        <style>
            .tooltip-inner {
                max-width: 236px !important;
                font-weight: 600;
                font-size: 15px;
                padding: 10px 15px 10px 20px;
                background: #FFFFFF;
                color: rgba(0, 0, 0, .7);
                border: 1px solid #737373;
                text-align: left;
            }
        </style>
    </head>
    <body>

        <!--==========================
Header
============================-->
        
        <div class="loader"><img src="assets/images/loader-1.gif" alt="" /></div>
        <header id="header" class="fixed-top">
            <div class="container">

                <div class="logo float-left">
                    <!-- Uncomment below if you prefer to use an image logo -->
                    <!-- <h1 class="text-light"><a href="#header"><span>NewBiz</span></a></h1> -->
                    <a href="<%= url%>" class="scrollto"><img src="assets/home-page/img/eforms-logo.png" alt="" class="img-fluid"></a>
                </div>

                <nav class="main-nav float-right d-none d-lg-block">
                    <ul>
                        <li class="active"><a href="#home">Home</a></li>
                        <li><a href="#services">Services</a></li>
                        <li><a href="#why-us">In Focus</a></li>
                        <!--                        <li><a href="#feedback">Feedback</a></li>-->
                        <li><a href="https://servicedesk.nic.in/" target="_blank" title="External Link">Contact Us</a></li>
                        <li><a href="eforms-faqs" target="_blank" title="External Link">FAQs</a></li>
                        <li><a href="#" class="btn btn-primary login-btn" onclick="loginEform()">Login <span class="new-notify">New</span></a></li>
                        <!--                        <li class="update-btn-li"><a href="#" class="btn btn-primary update-btn" onclick="updateMobile()"><span>Update Mobile</span></a></li>-->
                    </ul>
                </nav>
                <!-- .main-nav -->

            </div>
        </header>
        <!-- #header -->

        <!--==========================
  Intro Section
============================-->
        <section id="home" class="clearfix">
            <div class="carousel slide carousel-fade" data-ride="carousel" data-interval="50000" id="carousel-example-captions">
                <ol class="carousel-indicators">
                    <li class="active" data-slide-to="0" data-target="#carousel-example-captions"></li>
                    <li data-slide-to="1" data-target="#carousel-example-captions" class=""></li>
                    <li data-slide-to="2" data-target="#carousel-example-captions" class=""></li>
                </ol>
                <div role="listbox" class="carousel-inner">
                    <div class="carousel-item slider-4 active">
                        <div class="overlay"></div>
                        <div class="container carousel-caption">
                            <div class="row">
                                <div class="col-xs-12 col-md-8" style="z-index: 2;">
                                    <div class="hero__info">

                                    </div>
                                    <div class="hero__download">

                                        <a class="c-btn c-btn--primary -big updatemobile" href="update-mobile" style="float: right;margin-bottom: 150px;">
                                            <span>Update Mobile & Profile</span>
                                        </a>
                                    </div>
                                    <div class="hero__download">
                                        <a class="c-btn c-btn--action -big" href="https://parichay.nic.in/Accounts/Services?service=eformsretired" data-toggle="tooltip" data-placement="bottom" title="Extend the validity of your Email Account(Only for Retired Officers)" style="float: right;margin-bottom: 150px;">
                                            <span>For Retired Officers only</span>
                                        </a>
                                    </div>
                                    <!--                                    <div class="hero__download">
                                                                            <a class="c-btn c-btn--action -big" href="https://parichay.nic.in/Accounts/Services?service=eformsretired" style="float: right;margin-bottom: 150px;">
                                                                                <span>Extend your Account</span>
                                                                                <span>For Retired Officers only</span>
                                                                            </a>
                                                                        </div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="carousel-item slider-2">
                        <div class="overlay"></div>
                        <div class="container carousel-caption">
                            <div class="row">
                                <div class="col-xs-12 col-md-8" style="z-index: 2;">
                                    <div class="hero__info">
                                        <h2>eForms: Online Platform for all Services of NIC</h2>
                                        <p>This portal helps you to register for various services offered by NIC. Besides submitting the request online, it also helps you to track the status of the request through Tracker option. You are also notified
                                            through email and SMS for every movement of the requests.</p>
                                    </div>
                                    <div class="hero__download">
                                        <a class="c-btn c-btn--action -big" data-fancybox="" href="assets/home-page/img/slider/eforms_video.mp4">
                                            <i class="fa fa-video"></i>&ensp;portal workflow
                                        </a>
                                        <a class="c-btn c-btn--primary -big" href="assets/old_assets/upload/user_manual.pdf" target="_blank">
                                            <span>User Manual</span>
                                        </a>
                                        <a class="c-btn c-btn--primary -big updatemobile" href="update-mobile">
                                            <span>Update Mobile</span>
                                        </a>
                                    </div>
                                </div>
                                <div class="hidden-xs hidden-sm col-md-4">
                                    <div class="hero__img">
                                        <img src="assets/home-page/img/slider/mockup.png" alt="Software Mockup" style="margin-top: -20px;">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="carousel-item slider-1">
                        <div class="overlay"></div>
                        <div class="container carousel-caption">
                            <div class="row">
                                <div class="col-xs-12 col-md-8" style="z-index: 2;">
                                    <div class="hero__info">
                                        <h2>eForms: Features of the Application</h2>
                                        <p>Web responsive with convenient GUI, More than 13 services have been on-boarded, Multiple submission option like eSigning, online and manual, Dashboard and Filters, Preview and Edit, Custom flow for specific
                                            services, Auto forward to next level</p>
                                    </div>
                                    <div class="hero__download">
                                        <a class="c-btn c-btn--action -big" data-fancybox="" href="assets/home-page/img/slider/eforms_video.mp4">
                                            <i class="fa fa-video"></i>&ensp;portal workflow
                                        </a>

                                        <a class="c-btn c-btn--primary -big" href="assets/old_assets/upload/user_manual.pdf" target="_blank">
                                            <span>User Manual</span>
                                        </a>
                                        <a class="c-btn c-btn--primary -big updatemobile" href="update-mobile">
                                            <span>Update Mobile</span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="carousel-item slider-3">
                        <div class="container carousel-caption">
                            <div class="row">
                                <div class="col-xs-12 col-md-8" style="z-index: 2;">
                                    <div class="hero__info">
                                        <h2>eForms Solution : Completely Digital and paperless</h2>
                                        <p>Provides digital signing provision to reduce usage of paper. Stakeholders can approve, reject, raise query, upload and download multiple documents online.</p>
                                    </div>
                                    <div class="hero__download">
                                        <a class="c-btn c-btn--action -big" data-fancybox="" href="assets/home-page/img/slider/eforms_video.mp4">
                                            <i class="fa fa-video"></i>&ensp;portal workflow
                                        </a>
                                        <!--                                        <a id="play-home-video" class="video-play-button" data-fancybox="" href="assets/home-page/img/slider/eforms_video.mp4">
                                    <span></span>
                                </a>-->
                                        <a class="c-btn c-btn--primary -big" href="assets/old_assets/upload/user_manual.pdf" target="_blank">
                                            <span>User Manual</span>
                                        </a>
                                        <a class="c-btn c-btn--primary -big updatemobile" href="update-mobile">
                                            <span>Update Mobile</span>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="overlay"></div>
                    </div>
                </div>
                <a data-slide="prev" role="button" href="#carousel-example-captions" class="left carousel-control">
                    <span aria-hidden="true" class="fa fa-angle-left"></span>
                </a>
                <a data-slide="next" role="button" href="#carousel-example-captions" class="right carousel-control">
                    <span aria-hidden="true" class="fa fa-angle-right"></span>
                </a>
            </div>
            <a href="#services" class="header__scroll">
                <img src="assets/home-page/img/scroll-arrow.svg" alt="Scroll To Bottom Arrow">
            </a>
        </section>
        <!-- #intro -->

        <main id="main">

            <!--            <div class="rate-table">
        <div class="container">
            <div class="row">
                <div class="col-xl-3 col-lg-3 col-md-3 col-sm-6 col-6">
                    <div class="rate-counter-block">
                        <div class="icon rate-icon  "><i class="fa fa-users"></i></div>
                        <div class="rate-box">
                            <h1 class="loan-rate usercount"><//s:property value="val.usercount" /></h1>
                            <small class="rate-title">Users</small>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-lg-3 col-md-3 col-sm-6 col-6">
                    <div class="rate-counter-block">
                        <div class="icon rate-icon  "> 
                            <i class="fa fa-edit"></i>
                        </div>
                        <div class="rate-box">
                            <h1 class="loan-rate totalcount"><//s:property value="val.totalrequest" /></h1>
                            <small class="rate-title">Total Forms</small>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-lg-3 col-md-3 col-sm-6 col-6">
                    <div class="rate-counter-block">
                        <div class="icon rate-icon  "> 
                            <i class="fa fa-file-alt"></i>
                        </div>
                        <div class="rate-box">
                            <h1 class="loan-rate pendingcount"><//s:property value="val.totalpending" /></h1>
                            <small class="rate-title">Total Pending</small>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-lg-3 col-md-3 col-sm-6 col-6">
                    <div class="rate-counter-block" style="border-right: 1px solid #eee;">
                        <div class="icon rate-icon  "> 
                            <i class="fa fa-file-invoice"></i>
                        </div>
                        <div class="rate-box">
                            <h1 class="loan-rate completedcount"><//s:property value="val.totalComplete" /></h1>
                            <small class="rate-title">Total Completed</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>-->
            <!--==========================
      Services Section
    ============================-->
            <section id="services" class="section-bg">
                <div class="container">

                    <header class="section-header mb-5 mt-4">
                        <h3>Services</h3>
                    </header>

                    <div class="row">

                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-user-lock"></i></div>
                                <h4 class="title">Authentication Services (LDAP)</h4>
                                <p class="description">This registration form is designed to access the Central Repository of NIC and to authenticate user through it.</p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-share-alt"></i></div>
                                <h4 class="title">Distribution List<br>Services</h4>
                                <p class="description">This registration form is designed to create a distribution list for information disbursement through email.</p>
                            </div>
                        </div>

                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.1s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-sitemap"></i></div>
                                <h4 class="title">VPN<br>Registration</h4>
                                <p class="description">This registration form is designed for the applicants who require a Virtual Private Network to access Intranet.</p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.1s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-sort-amount-up"></i></div>
                                <h4 class="title">DNS <br />Services</h4>
                                <p class="description">This registration form is designed to register a domain for NIC Private/Public IP Pool (164.100.X.X) and NIC IPV6 addresses.</p>
                            </div>
                        </div>

                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.2s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-mail-bulk"></i></div>
                                <h4 class="title">Email <br>Service</h4>
                                <p class="description">This registration form is designed for the applicants who need a government Email account provided by NIC.</p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.2s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-envelope-open"></i></div>
                                <h4 class="title">SMS <br>Service</h4>
                                <p class="description">SMS service allows you to register for following services PUSH / PULL / OBD / MISSED CALL / OTP SERVICE / QUICK SMS. </p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.2s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-wifi"></i></div>
                                <h4 class="title">WIFI<br>Service</h4>
                                <p class="description">This registration form is designed to access NIC WIFI service to use internet. For every user maximum 4 devices are allowed.</p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.2s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-server"></i></div>
                                <h4 class="title">SMTP<br>Gateway</h4>
                                <p class="description">SMTP Service allows you to register for Relay (SMTP gateway) service to send emails from applications (only outgoing mails).</p>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-6 wow bounceInUp" data-wow-delay="0.2s" data-wow-duration="1.4s">
                            <div class="box">
                                <div class="icon"><i class="fa fa-mobile-alt"></i></div>
                                <h4 class="title">Update Mobile<br>Service</h4>
                                <p class="description">Update Mobile Service allows you to Update your Mobile Number in NIC central Repository against the your id.</p>
                            </div>
                        </div>
                    </div>

                </div>
            </section>
            <!-- #services -->
            <!--==========================
      Why Us Section
    ============================-->
            <section id="why-us" class="wow fadeIn">
                <div class="container">
                    <header class="section-header">
                        <h3>In Focus</h3>
                    </header>
                    <div class="row row-eq-height justify-content-center">
                        <div class="col-lg-4 mb-4">
                            <div class="card wow bounceInUp">
                                <i class="fa fa-building"></i>
                                <div class="card-body">
                                    <h5 class="card-title">GEM(PSU)</h5>
                                    <p class="card-text">GEM(PSU) users must select GEM Subscription option in email service for creating the Email ID's.</p>
                                    <a href="https://eforms.nic.in/download_10" target="_blank" class="readmore">Click Here</a>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4 mb-4">
                            <div class="card wow bounceInUp">
                                <i class="fa fa-chalkboard"></i>
                                <div class="card-body">
                                    <h5 class="card-title">Compatibility</h5>
                                    <p class="card-text">The site can be best viewed in the latest version of Chrome, Firefox, Safari, Opera, Internet Explorer (11 +).</p>
                                    <a href="https://www.whatismybrowser.com/" target="_blank" class="readmore">Check Now</a>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4 mb-4">
                            <div class="card wow bounceInUp">
                                <i class="fa fa-file-pdf"></i>
                                <div class="card-body">
                                    <h5 class="card-title">User Manual</h5>
                                    <p class="card-text">For any assitance regarding this application please Download eForms Manual</p>
                                    <a href="assets/old_assets/upload/user_manual.pdf" target="_blank" class="readmore">User Manual</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!--==========================
      About Us Section
    ============================-->
            <!--            <section id="feedback">
                            <div class="site-section bg-light" id="contact-section">
                                <div class="container">
                                    <div class="row justify-content-center">
                                        <div class="col-md-12  pt-5 pb-5">
                                            <div class="card p-3" style="background-color: #007bff14;">
                                                <h2 class="section-title mb-3">Write to Us</h2>
                                                <div class="msg_div"></div>
                                                <form method="post" data-aos="fade" class="aos-init aos-animate" id="registration_form">
                                                    <div class="form-group row">
                                                        <div class="col-md-6 mb-3 mb-lg-0">
                                                            <input type="text" name="fname" class="form-control" placeholder="First name" id="form_first">
                                                            <div id="first_error_message" class="error"></div>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <input type="text" name="lname" class="form-control" placeholder="Last name" id="form_last">
                                                            <div id="last_error_message" class="error"></div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <div class="col-md-12">
                                                            <input type="email" name="email" id="form_email" class="form-control" placeholder="Email">
                                                            <div id="email_error_message" class="error"></div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <div class="col-md-12">
                                                            <textarea class="form-control" id="msg_txt_message" cols="30" rows="5" placeholder="Write your message here."></textarea>
                                                            <div id="msg_error_message" class="error"></div>
                                                        </div>
                                                    </div>
                                                    <div class="form-group row">
                                                        <div class="col-9 mt-2">
                                                            <div class="row feedback-form">
                                                                <div class="col-md-3">
                                                                    <img name="Captcha" id="captcha_feedback" src="Captcha?var=<%=random%>" width="150" height="45" alt="captcha background" class="ft-lft captcha" />
                                                                    <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh_feedback" height="20" width="20">
                                                                </div>
                                                                <div class="col-md-3">
                                                                    <div class="form-group">
                                                                        <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt_feedback" id="imgtxt_feedback" maxlength="6" value="">
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div id="captcha_error_message" class="error"></div>
                                                        </div>
                                                        <div class="col-md-3">
                                                            <button type="submit" class="btn btn-primary py-3 px-5 float-right">Send Message</button>
                                                        </div>
                                                    </div>
                                                    <input type="hidden" name="as_sfid" value="AAAAAAUnNGfVKPupWyW0TEj9IdE8nLCAjVt9Mx8lup1fV_gbTjS0yP6pvFBGnYQ16M2RnhBcwyMpO-RY1p_p5258rFNq1CgVvr23F98VvurgINWVbeneZnXI487ctF-fKnjzZjs=" /><input type="hidden"
                                                                                                                                                                                                        name="as_fid" value="f0ec539f6f041ac6313d0edea66b69ccaf22a765" /></form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </section>-->
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
        <!-- #footer -->
        <div class="modal" id="myModallogin" role="dialog">
            <div class="modal-dialog login-popup">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Login</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body pb-3 pt-3">
                        <div class="k-login-v2__body">
                            <!-- LOGIN FORM START -->
                            <!--begin::Wrapper-->
                            <div class="k-login-v2__body-wrapper" id="signup-tab">
                                <div class="k-login-v2__body-container">
                                    <div class="k-login-v2__body-title">
                                        <h6 class="mt-2 mb-3">Sign in to Portal</h6>
                                    </div>
                                    <div class="err_msg_pop"></div>
                                    <span id="err_msg_mobile" class="err_msg" style="text-align: center;"></span>
                                     <span id="blocked_user" class="err_msg" style="text-align: center;"></span>
                                    
                                    <!--begin::Form-->
                                    <form class="k-login-v2__body-form k-form k-login-v2__body-form--border" method="post" action="#" id="submit-pwd">
                                        <div class="alert alert-danger display-hide">
                                            <button class="close" data-close="alert"></button>
                                            <span> <s:text name="login.label"/></span>
                                        </div>
                                        
                                        <div class="form-group email-div" style="padding-top: 10px;">
                                            <label for="email">Enter Your Email Address</label>
                                            <!--(<a href="#" onclick="updatemob()" class="updatemob">Update Mobile</a>)-->
                                            <small class="update-notify"></small>
                                            <input class="form-control form-control-solid placeholder-no-fix" type="email" autocomplete="off" placeholder="Enter Your Email Address" name="email" id="email" maxlength="50" autofocus />
                                            <!--<span id="email_error" class="err_msg"></span>-->
                                             <span id="email_error_emailCountAgainstMobile" class="err_msg_emailCountAgainstMobile display-hide text-danger"></span>
                                            
                                        </div>
                                        <div id="mobile-div" class="form-group display-hide">
                                            <label class="control-label visible-ie8 visible-ie9">Mobile Number</label>
                                            <div class="row">
                                                <div class="col-md-4 col-sm-4 col-xs-4" style="padding-right: 0;">
                                                    <select class="form-control" name="country_code" id="country_code">
<!--                                                        <option value="+213">Algeria (+213)</option> 
                                                        <option value="+376">Andorra (+376)</option> 
                                                        <option value="+244">Angola (+244)</option> 
                                                        <option value="+1264">Anguilla (+1264)</option> 
                                                        <option value="+1268">Antigua and Barbuda (+1268)</option> 
                                                        <option value="+599">Antilles (Dutch) (+599)</option> 
                                                        <option value="+54">Argentina (+54)</option> 
                                                        <option value="+374">Armenia (+374)</option> 
                                                        <option value="+297">Aruba (+297)</option> 
                                                        <option value="+247">Ascension Island (+247)</option> 
                                                        <option value="+61">Australia (+61)</option> 
                                                        <option value="+43">Austria (+43)</option> 
                                                        <option value="+994">Azerbaijan (+994)</option> 
                                                        <option value="+1242">Bahamas (+1242)</option> 
                                                        <option value="+973">Bahrain (+973)</option> 
                                                        <option value="+880">Bangladesh (+880)</option> 
                                                        <option value="+1246">Barbados (+1246)</option> 
                                                        <option value="+375">Belarus (+375)</option> 
                                                        <option value="+32">Belgium (+32)</option> 
                                                        <option value="+501">Belize (+501)</option> 
                                                        <option value="+229">Benin (+229)</option> 
                                                        <option value="+1441">Bermuda (+1441)</option> 
                                                        <option value="+975">Bhutan (+975)</option> 
                                                        <option value="+591">Bolivia (+591)</option> 
                                                        <option value="+387">Bosnia Herzegovina (+387)</option> 
                                                        <option value="+267">Botswana (+267)</option> 
                                                        <option value="+55">Brazil (+55)</option> 
                                                        <option value="+673">Brunei (+673)</option> 
                                                        <option value="+359">Bulgaria (+359)</option> 
                                                        <option value="+226">Burkina Faso (+226)</option> 
                                                        <option value="+257">Burundi (+257)</option> 
                                                        <option value="+855">Cambodia (+855)</option> 
                                                        <option value="+237">Cameroon (+237)</option> 
                                                        <option value="+1">Canada (+1)</option> 
                                                        <option value="+238">Cape Verde Islands (+238)</option> 
                                                        <option value="+1345">Cayman Islands (+1345)</option> 
                                                        <option value="+236">Central African Republic (+236)</option> 
                                                        <option value="+56">Chile (+56)</option> 
                                                        <option value="+86">China (+86)</option> 
                                                        <option value="+57">Colombia (+57)</option> 
                                                        <option value="+269">Comoros (+269)</option> 
                                                        <option value="+242">Congo (+242)</option> 
                                                        <option value="+682">Cook Islands (+682)</option> 
                                                        <option value="+506">Costa Rica (+506)</option> 
                                                        <option value="+385">Croatia (+385)</option> 
                                                        <option value="+53">Cuba (+53)</option> 
                                                        <option value="+90392">Cyprus North (+90392)</option> 
                                                        <option value="+357">Cyprus South (+357)</option> 
                                                        <option value="+42">Czech Republic (+42)</option> 
                                                        <option value="+45">Denmark (+45)</option> 
                                                        <option value="+2463">Diego Garcia (+2463)</option> 
                                                        <option value="+253">Djibouti (+253)</option> 
                                                        <option value="+1809">Dominica (+1809)</option> 
                                                        <option value="+1809">Dominican Republic (+1809)</option> 
                                                        <option value="+593">Ecuador (+593)</option> 
                                                        <option value="+20">Egypt (+20)</option> 
                                                        <option value="+353">Eire (+353)</option> 
                                                        <option value="+503">El Salvador (+503)</option> 
                                                        <option value="+240">Equatorial Guinea (+240)</option> 
                                                        <option value="+291">Eritrea (+291)</option> 
                                                        <option value="+372">Estonia (+372)</option> 
                                                        <option value="+251">Ethiopia (+251)</option> 
                                                        <option value="+500">Falkland Islands (+500)</option> 
                                                        <option value="+298">Faroe Islands (+298)</option> 
                                                        <option value="+679">Fiji (+679)</option> 
                                                        <option value="+358">Finland (+358)</option> 
                                                        <option value="+33">France (+33)</option> 
                                                        <option value="+594">French Guiana (+594)</option> 
                                                        <option value="+689">French Polynesia (+689)</option> 
                                                        <option value="+241">Gabon (+241)</option> 
                                                        <option value="+220">Gambia (+220)</option> 
                                                        <option value="+7880">Georgia (+7880)</option> 
                                                        <option value="+49">Germany (+49)</option> 
                                                        <option value="+233">Ghana (+233)</option> 
                                                        <option value="+350">Gibraltar (+350)</option> 
                                                        <option value="+30">Greece (+30)</option> 
                                                        <option value="+299">Greenland (+299)</option> 
                                                        <option value="+1473">Grenada (+1473)</option> 
                                                        <option value="+590">Guadeloupe (+590)</option> 
                                                        <option value="+671">Guam (+671)</option> 
                                                        <option value="+502">Guatemala (+502)</option> 
                                                        <option value="+224">Guinea (+224)</option> 
                                                        <option value="+245">Guinea - Bissau (+245)</option> 
                                                        <option value="+592">Guyana (+592)</option> 
                                                        <option value="+509">Haiti (+509)</option> 
                                                        <option value="+504">Honduras (+504)</option> 
                                                        <option value="+852">Hong Kong (+852)</option> 
                                                        <option value="+36">Hungary (+36)</option> 
                                                        <option value="+354">Iceland (+354)</option> 
-->                                                        <option value="+91" selected="selected">India (+91)</option> <!--
                                                        <option value="+62">Indonesia (+62)</option> 
                                                        <option value="+98">Iran (+98)</option> 
                                                        <option value="+964">Iraq (+964)</option> 
                                                        <option value="+972">Israel (+972)</option> 
                                                        <option value="+39">Italy (+39)</option> 
                                                        <option value="+225">Ivory Coast (+225)</option> 
                                                        <option value="+1876">Jamaica (+1876)</option> 
                                                        <option value="+81">Japan (+81)</option> 
                                                        <option value="+962">Jordan (+962)</option> 
                                                        <option value="+7">Kazakhstan (+7)</option> 
                                                        <option value="+254">Kenya (+254)</option> 
                                                        <option value="+686">Kiribati (+686)</option> 
                                                        <option value="+850">Korea North (+850)</option> 
                                                        <option value="+82">Korea South (+82)</option> 
                                                        <option value="+965">Kuwait (+965)</option> 
                                                        <option value="+996">Kyrgyzstan (+996)</option> 
                                                        <option value="+856">Laos (+856)</option> 
                                                        <option value="+371">Latvia (+371)</option> 
                                                        <option value="+961">Lebanon (+961)</option> 
                                                        <option value="+266">Lesotho (+266)</option> 
                                                        <option value="+231">Liberia (+231)</option> 
                                                        <option value="+218">Libya (+218)</option> 
                                                        <option value="+417">Liechtenstein (+417)</option> 
                                                        <option value="+370">Lithuania (+370)</option> 
                                                        <option value="+352">Luxembourg (+352)</option> 
                                                        <option value="+853">Macao (+853)</option> 
                                                        <option value="+389">Macedonia (+389)</option> 
                                                        <option value="+261">Madagascar (+261)</option> 
                                                        <option value="+265">Malawi (+265)</option> 
                                                        <option value="+60">Malaysia (+60)</option> 
                                                        <option value="+960">Maldives (+960)</option> 
                                                        <option value="+223">Mali (+223)</option> 
                                                        <option value="+356">Malta (+356)</option> 
                                                        <option value="+692">Marshall Islands (+692)</option> 
                                                        <option value="+596">Martinique (+596)</option> 
                                                        <option value="+222">Mauritania (+222)</option>
                                                        <option value="+230">Mauritius (+230)</option> 
                                                        <option value="+269">Mayotte (+269)</option> 
                                                        <option value="+52">Mexico (+52)</option> 
                                                        <option value="+691">Micronesia (+691)</option> 
                                                        <option value="+373">Moldova (+373)</option> 
                                                        <option value="+377">Monaco (+377)</option> 
                                                        <option value="+976">Mongolia (+976)</option> 
                                                        <option value="+1664">Montserrat (+1664)</option> 
                                                        <option value="+212">Morocco (+212)</option> 
                                                        <option value="+258">Mozambique (+258)</option> 
                                                        <option value="+95">Myanmar (+95)</option> 
                                                        <option value="+264">Namibia (+264)</option> 
                                                        <option value="+674">Nauru (+674)</option> 
                                                        <option value="+977">Nepal (+977)</option> 
                                                        <option value="+31">Netherlands (+31)</option> 
                                                        <option value="+687">New Caledonia (+687)</option> 
                                                        <option value="+64">New Zealand (+64)</option> 
                                                        <option value="+505">Nicaragua (+505)</option> 
                                                        <option value="+227">Niger (+227)</option> 
                                                        <option value="+234">Nigeria (+234)</option> 
                                                        <option value="+683">Niue (+683)</option> 
                                                        <option value="+672">Norfolk Islands (+672)</option> 
                                                        <option value="+670">Northern Marianas (+670)</option> 
                                                        <option value="+47">Norway (+47)</option> 
                                                        <option value="+968">Oman (+968)</option> 
                                                        <option value="+92">Pakistan (+92)</option> 
                                                        <option value="+680">Palau (+680)</option> 
                                                        <option value="+507">Panama (+507)</option> 
                                                        <option value="+675">Papua New Guinea (+675)</option> 
                                                        <option value="+595">Paraguay (+595)</option> 
                                                        <option value="+51">Peru (+51)</option> 
                                                        <option value="+63">Philippines (+63)</option> 
                                                        <option value="+48">Poland (+48)</option> 
                                                        <option value="+351">Portugal (+351)</option> 
                                                        <option value="+1787">Puerto Rico (+1787)</option> 
                                                        <option value="+974">Qatar (+974)</option> 
                                                        <option value="+262">Reunion (+262)</option> 
                                                        <option value="+40">Romania (+40)</option> 
                                                        <option value="+7">Russia (+7)</option> 
                                                        <option value="+250">Rwanda (+250)</option> 
                                                        <option value="+378">San Marino (+378)</option> 
                                                        <option value="+239">Sao Tome and Principe (+239)</option> 
                                                        <option value="+966">Saudi Arabia (+966)</option> 
                                                        <option value="+221">Senegal (+221)</option> 
                                                        <option value="+381">Serbia (+381)</option> 
                                                        <option value="+248">Seychelles (+248)</option> 
                                                        <option value="+232">Sierra Leone (+232)</option> 
                                                        <option value="+65">Singapore (+65)</option> 
                                                        <option value="+421">Slovak Republic (+421)</option> 
                                                        <option value="+386">Slovenia (+386)</option> 
                                                        <option value="+677">Solomon Islands (+677)</option> 
                                                        <option value="+252">Somalia (+252)</option> 
                                                        <option value="+27">South Africa (+27)</option> 
                                                        <option value="+34">Spain (+34)</option> 
                                                        <option value="+94">Sri Lanka (+94)</option> 
                                                        <option value="+290">St. Helena (+290)</option> 
                                                        <option value="+1869">St. Kitts (+1869)</option> 
                                                        <option value="+1758">St. Lucia (+1758)</option> 
                                                        <option value="+249">Sudan (+249)</option> 
                                                        <option value="+597">Suriname (+597)</option> 
                                                        <option value="+268">Swaziland (+268)</option> 
                                                        <option value="+46">Sweden (+46)</option> 
                                                        <option value="+41">Switzerland (+41)</option> 
                                                        <option value="+963">Syria (+963)</option> 
                                                        <option value="+886">Taiwan (+886)</option> 
                                                        <option value="+7">Tajikstan (+7)</option> 
                                                        <option value="+66">Thailand (+66)</option> 
                                                        <option value="+228">Togo (+228)</option> 
                                                        <option value="+676">Tonga (+676)</option> 
                                                        <option value="+1868">Trinidad and Tobago (+1868)</option> 
                                                        <option value="+216">Tunisia (+216)</option> 
                                                        <option value="+90">Turkey (+90)</option> 
                                                        <option value="+7">Turkmenistan (+7)</option> 
                                                        <option value="+993">Turkmenistan (+993)</option> 
                                                        <option value="+1649">Turks and Caicos Islands (+1649)</option> 
                                                        <option value="+688">Tuvalu (+688)</option> 
                                                        <option value="+256">Uganda (+256)</option> 
                                                        <option value="+44" >UK (+44)</option> 
                                                        <option value="+380">Ukraine (+380)</option> 
                                                        <option value="+971">United Arab Emirates (+971)</option> 
                                                        <option value="+598">Uruguay (+598)</option> 
                                                        <option value="+1">USA (+1)</option> 
                                                        <option value="+7">Uzbekistan (+7)</option> 
                                                        <option value="+678">Vanuatu (+678)</option> 
                                                        <option value="+379">Vatican City (+379)</option> 
                                                        <option value="+58">Venezuela (+58)</option> 
                                                        <option value="+84">Vietnam (+84)</option> 
                                                        <option value="+84">Virgin Islands - British (+1284)</option> 
                                                        <option value="+84">Virgin Islands - US (+1340)</option> 
                                                        <option value="+681">Wallis and Futuna (+681)</option> 
                                                        <option value="+969">Yemen (North)(+969)</option> 
                                                        <option value="+967">Yemen (South)(+967)</option> 
                                                        <option value="+381">Yugoslavia (+381)</option> 
                                                        <option value="+243">Zaire (+243)</option> 
                                                        <option value="+260">Zambia (+260)</option> 
                                                        <option value="+263">Zimbabwe (+263)</option>-->
                                                    </select>
                                                </div>
                                                <div class="col-md-8 col-sm-8 col-xs-8">
                                                    <input class="form-control form-control-solid placeholder-no-fix" disabled type="text" autocomplete="off" placeholder="Mobile Number" name="mobile" id="mobile" maxlength="10" autofocus />
                                                    <span id="mobile_error" class="err_msg"></span>
                                                     <span id="mobile_hide" class="err_msg"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="password-div" class="form-group display-hide">
                                            <label class="control-label visible-ie8 visible-ie9">Password</label>
                                            <input class="form-control form-control-solid placeholder-no-fix" disabled type="password" autocomplete="off" placeholder="Please Enter Your NIC Password" name="password" id="password" maxlength="50" autofocus />
                                            <span id="password_error" class="err_msg"></span>
                                            <span id="fill_password" class="err_msg"></span>
                                           
                                        </div>

                                        <div id="captcha_div" class="form-group display-hide">
                                            <div class="row">
                                                <div class="col-md-6">

                                                    <div class="row captcha-img mt-4 pt-1">
                                                        <div class="col-md-9 pr-0">
                                                            <img name="Captcha" id="captcha2" src="Captcha?var=<%=random%>" width="130" height="55" alt="captcha background" class="ft-lft captcha float-right" />
                                                        </div>
                                                        <div class="col-md-1 mt-2 pt-1">
                                                            <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="control-label" for="street">Enter Captcha<span style="color: red">*</span></label>
                                                        <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="imgtxt" id="captcha1" maxlength="6" value="" autocomplete="off">
                                                    </div>
                                                </div>
                                                <div class="col-md-12"><span class="err_msg" id="logincaptchaerror"></span></div>
                                            </div>
                                        </div>

                                        <div class="form-actions">
                                            <input type="hidden" id="role" />
                                            <button id="mobile_disable" class="btn btn-pill btn-brand btn-elevate"><s:text name="button.continue"/></button>
                                        </div>
                                        <input type="hidden" name="as_sfid" value="AAAAAAULGqC6NXdu0i7vWMbgNbd29rIpBMErGnSZ7hnuTYm7XcUmC0TBuL3KY4RfdoiVryq3vLqFPDmC8RKt-C3w7bs0szADyhNlZhGTemm0ugvP3uWQp4xVMfKTX-YSovN-bes=" /><input type="hidden"
                                                                                                                                                                                                        name="as_fid" value="f165e8cb8f39767836dbb9a35e2bfc3fe407c4a0" /></form>
                                    <!--end::Action-->
                                </div>
                            </div>
                            <!-- LOGIN FORM END -->
                            <!-- OTP Form Start -->
                            <div class="k-login-v2__body-wrapper display-hide" id="otp-tab">
                                <div class="k-login-v2__body-container">
                                    <div class="k-login-v2__body-title">
                                        <h6 class="mt-2 mb-0">Verify OTP Details</h6>
                                    </div>
                                    <div class="err_msg_pop"></div>
                                    <span id="otp_text_tab" style="font-weight: bold"></span>
                                    <div class="alert alert-danger display-hide">
                                        <button class="close" data-close="alert"></button>
                                        <span> Enter OTP (One Time Password). </span>
                                    </div>
                                    <!--begin::Form-->
                                    <form class="k-login-v2__body-form k-form k-login-v2__body-form--border" method="post" action="#">
                                        <input type="hidden" name="new_mobile" />
                                        <input type="hidden" name="country_code_val" />
                                        <input type="hidden" name="new_email" />
                                        <input type="hidden" name="mobile" />
                                        <input type="hidden" name="isNewUser" />
                                        <input type="hidden" name="isEmailValidated" />
                                        <input type="hidden" name="isIsNicEmployee" />
                                        <input type="hidden" name="emailOtpActive" />
                                        <input type="hidden" name="mobileOtpActive" />
                                        <input type="hidden" name="mobile_resend" />
                                        <div id="enter_details" style="color:red; text-align: center"></div>
                                        <div id="mobile-otp-div" class="form-group">
                                            <span id="mobile_block" class="err_msg"></span>
                                            <label class="control-label visible-ie8 visible-ie9">Enter Your Mobile OTP</label>
                                            <input class="form-control form-control-solid placeholder-no-fix" type="password" autocomplete="off" placeholder="Please Enter OTP sent to your Mobile Number" name="mobileOtp" id="mobileOtp" maxlength="6" />
                                           <!--anmol-->
                                           
                                            <div id="mobile_info" style="color:green;"></div>
                                            <span id="mobile_resend" class="err_msg"></span>
                                            <span id="mobile_resend1" class="err_msg"></span>
                                            <span id="mobile_resend2" class="err_msg"></span>

                                        </div>
                                        <div id="or-div" class="form-group display-hide" style="text-align: center; font-size: 15px;font-weight: 700;">
                                            <p>OR/BOTH</p>
                                        </div>
                                        <div id="email-otp-div" class="form-group display-hide">
                                            <label class="control-label visible-ie8 visible-ie9">Enter Your Email OTP</label>
                                            <input class="form-control form-control-solid placeholder-no-fix" type="password" autocomplete="off" placeholder="Please Enter OTP sent to your Email Address" name="emailOtp" id="emailOtp" maxlength="6" />
                                            <span id="email_info" style="color:green;"></span><br/>
                                            
                                            <span id="email_otp_error" class="err_msg"></span>
                                            <div id="email_resend" style="color:green;" class="err_msg"></div>
                                             <span id="email_resend_exh" class="err_msg"></span>
                                        </div>

                                        <div class="form-actions">
                                            <div class="form-group">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <button id="resend_mobile" class="btn btn-pill btn-brand btn-elevate float-left display-hide">Resend mobile otp</button>
                                                        <button id="resend_email" class="btn btn-pill btn-brand btn-elevate float-left display-hide" style="margin-left: 5px;">Resend email otp</button>
                                                        <button type="submit" id="submit-otp" class="btn btn-pill btn-brand btn-elevate float-right">CONTINUE</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <input type="hidden" name="as_sfid" value="AAAAAAWn3TocRiOFn-vFg3uQCL_yyCkf6k2TCnblD4egpYuIqjmxcKSdHXRGz4TbFpJFMUe17ZwpE2y3M4IRhO15ReJj_d5cnt_VTY6kWDQTdnzaf0jajrIY__ZB1TqD-JhiU1Y=" /><input type="hidden"
                                                                                                                                                                                                        name="as_fid" value="fe6df2833bcf49ef4c60b989f226d9a70dabf40d" />

                                    </form>
                                    <!--end::Action-->
                                </div>
                            </div>
                            <!-- OTP Form End -->
                            <!--end::Wrapper-->
                            <!--begin::Pic-->
                            <!--begin::Pic-->
                            <div class="login-note mt-4 alert alert-warning">
                                <ul class="ul-note">
                                    <li><b>Note:</b> All the Government IDs will log in now through SSO(Parichay) Only.</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal" id="updateMobile">
            <div class="modal-dialog login-popup">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Update Mobile Number</h5>
                        <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i></button>
                    </div>
                    <form class="k-login-v2__body-form k-form k-login-v2__body-form--border" method="post" action="#" id="update-mobile">

                        <div class="modal-body pb-5 pt-3">
                            <div class="alert alert-primary" style="font-size: 13px;">
                                <label class="k-checkbox k-checkbox--bold k-checkbox--brand">
                                    <input type="checkbox"  name="tnc" id="tnc" style="font-size: 13px;"> I hereby confirm that I am the authorised user of this email address.
                                    <span></span>
                                </label>
                                <div class="mt-2" style="text-align: justify;">
                                    Please note that updation of a mobile number against an email address for which access is not permitted to an individual may lead to penalisation as per IT Act and other governing laws of Govt of India.
                                </div>

                            </div>
                            <div class="k-login-v2__body">
                                <div class="form-group email-div" style="padding-top: 10px;">
                                    <label for="email" id="emailLbl">Enter the email address on NIC Platform</label>
                                    <small class="update-notify"></small>
                                    <input class="form-control form-control-solid placeholder-no-fix" type="email" autocomplete="off" placeholder="Enter Your Email Address" name="email" id="email" maxlength="50" autofocus />
                                    <small class="text-primary"><b>(@nic.in, @gov.in, @mea.gov.in)</b></small>
                                    <span id="email_error" class="err_msg"></span>
                                </div>

                                <div id="password-div" class="form-group display-hide">
                                    <label class="control-label visible-ie8 visible-ie9">Password</label>
                                    <input class="form-control form-control-solid placeholder-no-fix"  type="password" autocomplete="off" placeholder="Please Enter Your NIC Password" name="password" id="password" maxlength="50" autofocus />
                                    <span id="password_error" class="err_msg"></span><br/>
                                    <span id="blocked_user" class="err_msg"></span>
                                    
                                </div>


                                <!--added for captcha mobile-->
                                <div id="captcha_div" class="form-group display-hide">
                                    <div class="row">
                                        <div class="col-6">

                                            <div class="row captcha-img mt-4 pt-1">
                                                <div class="col-9 pr-0">
                                                    <img name="Captcha" id="captcha21" src="Captcha?var=<%=random%>" width="130" height="55" alt="captcha background" class="ft-lft captcha float-right" />
                                                </div>
                                                <div class="col-1 mt-2 pt-1">
                                                    <img border="0" src="assets/img/refresh1.png" style="cursor: pointer" name="refresh" id="refresh" height="20" width="20">
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-6">
                                            <div class="form-group">
                                                <label class="control-label" for="street">Enter Captcha<span style="color: red">*</span></label>
                                                <input class="form-control " style="text-transform:initial;" placeholder="Enter Captcha" type="text" name="captcha11" id="captcha11" maxlength="6" value="" autocomplete="off">
                                            </div>
                                        </div>
                                        <div class="col-md-12"><span class="err_msg" id="logincaptchaerror11"></span></div>
                                    </div>
                                </div>

                                <!--EOadded for captcha mobile-->

                                <div class="display-hide" id="old-mobile">
                                    <p class="alert alert-primary">
                                        <b>Current Mobile Number:</b>&nbsp;<span id="mobile"></span>
                                    </p>
                                </div>
                                <div class="display-hide" id="new-mobile">
                                    <div class="row">
                                        <!--                                        <div class="col-12"><label>Select Country Code and Enter Mobile Number</label></div>
                                                                                <div class="col-4 pr-0">
                                                                                    <select class="form-control" name="country_code" id="country_code_update_mob">
                                                                                        <option value="+91">+91</option>
                                                                                    </select>
                                                                                    
                                                                                </div>-->
                                        <div class="col-12">
                                            <label for="email" id="emailLbl">Enter New Mobile Number(10 digits)</label>
                                            <input type="text" autocomplete="off" class="form-control" name="mobile" id="newmobile" placeholder="Enter 10 digits Mobile Number" maxlength="10"/>
                                        </div>
                                        <div class="col-12">
                                            <span class="err_msg" id="moberr"></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row mt-2 display-hide" id="new-code">
                                    <div class="col-md-12"><label>Enter OTP Sent to Mobile Number</label></div>
                                    <div class="col-8 pr-0">
                                        <input type="password" autocomplete="off" class="form-control" placeholder="Enter New OTP code" name="newcode" id="newMobilecode" maxlength="6">
                                    </div>
                                    <div class="col-4">
                                        <button id="resend_mobile" val="resend_mobile" class="btn btn-brand btn-elevate btn-pill d-block" style="font-size: 13px;text-align: center;width: 100%;">Resend OTP</button>
                                    </div>
                                    <div class="col-md-12">
                                        <span id="file_cert_err" class="err_msg"></span>
                                        <span id="succ_err" class="err_msg" style="font-weight: 700;"></span>
                                        <span class="success_msg" id="succ_msg" style="font-weight: 700;"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-actions">
                                <input type="hidden" id="role" />
                                <input type="hidden" id="resend_submit_val"  value="otpGenerate"/>
                                <button class="btn btn-pill btn-brand btn-elevate d-none" id="continueId"><s:text name="button.continue"/></button>
                            </div>
                        </div>

                        <!--                        <div class="modal-footer">
                                                    <button type="button" class="btn dark btn-light" data-dismiss="modal" id="mod_close_mobile">Close</button>
                                                    <button type="button" class="btn btn-primary updatemobile">Submit</button>
                                                </div>-->
                    </form>
                </div>
            </div>
        </div>
        <!-- Notice Modal start --->
        <div id="myModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title">Notice</h4>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body">
                        <ul class="notice-ul">
                            <li>For ease of user on-boarding , <b class="text-danger">eForms has now been integrated with NIC Single Sign-On Platform (Parichay).</b> Now, all the users having NIC Email address (i.e. abc.xyz@gov.in, xyz.abc@nic.in)
                                will be authenticated through Parichay(SSO).</li>
                            <li>To access the NIC Coordinator module, the coordinator should access eforms portal through VPN.</li>
                        </ul>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Ok</button>
                    </div>
                </div>

            </div>
        </div>
        <!-- Notice Modal End -->
        <div class="modal" id="preLoginModal">
            <div class="modal-dialog login-popup">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Login</h5>
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                    </div>
                    <div class="modal-body pb-3 pt-3">
                        <div class="k-login-v2__body">
                            <!-- LOGIN FORM START -->
                            <!--begin::Wrapper-->
                            <div class="k-login-v2__body-wrapper" id="signup-tab">
                                <div class="k-login-v2__body-container">
                                    <div class="k-login-v2__body-title">
                                        <h6 class="mt-2 mb-3">Login with a Social Account</h6>
                                    </div>
                                    <!--end::Action-->
                                </div>
                            </div>
                            <!--begin::Pic-->
                            <div class="login-note mt-4 alert alert-warning">
                                <ul class="ul-note">
                                    <li><b>Note:</b> All the Government IDs will log in now through SSO(Parichay) Only.</li>
                                    <li>If you are having a NIC email address (examples @gov.in, @miety.in, @nic.in) <a href="https://parichay.nic.in/Accounts/NIC/index.html?service=eforms">Login With Parichay</a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <a href="#" class="back-to-top"><i class="fa fa-chevron-up"></i></a>
        <!-- Uncomment below i you want to use a preloader -->
        <!-- <div id="preloader"></div> -->
        <!-- JavaScript Libraries -->
        <script src="assets/home-page/lib/jquery/jquery.min.js"></script>
        <script src="assets/home-page/lib/jquery/jquery-migrate.min.js"></script>
        <script src="assets/home-page/lib/bootstrap/js/bootstrap.bundle.min.js"></script>
        <script src="assets/home-page/lib/easing/easing.min.js"></script>
        <script src="assets/home-page/lib/mobile-nav/mobile-nav.js"></script>
        <script src="assets/home-page/lib/wow/wow.min.js"></script>
        <script src="assets/home-page/lib/waypoints/waypoints.min.js"></script>
        <script src="assets/home-page/lib/counterup/counterup.min.js"></script>
        <script src="assets/home-page/lib/owlcarousel/owl.carousel.min.js"></script>
        <script src="assets/home-page/lib/isotope/isotope.pkgd.min.js"></script>
        <script src="assets/home-page/lib/lightbox/js/lightbox.min.js"></script>
        <!-- Contact Form JavaScript File -->
        <!-- Template Main Javascript File -->
        <script src="assets/home-page/js/main.js"></script>
        <script src="assets/home-page/js/form_val.js"></script>
        <script src="assets/home-page/fancybox-master/dist/jquery.fancybox.min.js"></script>
        <script src="main_js/register.js" type="text/javascript"></script>

        <script src="main_js/updatemobile.js" type="text/javascript"></script>


        <script type="text/javascript" src="assets/old_assets/plugins/bootbox.min.js"></script>
        <script type="text/javascript">
                                                $(document).ready(function () {
                                                    $("body").tooltip({selector: '[data-toggle=tooltip]'});
                                                });
                                                $(".feedback-form #refresh_feedback").on("click", function () {
                                                    feedback_captcha_reset()
                                                });

                                                function feedback_captcha_reset() {
                                                    $('.feedback-form #captcha_feedback').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
                                                }
        </script>
        <script>
            var input = document.getElementById("mobileOtp");
            var input1 = document.getElementById("emailOtp");

            input.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("submit-otp").click();
        }
    });
     input1.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("submit-otp").click();
        }
    });
</script>
<script>//
//    function discover()
//{
//    try{
//        //Get Local IP
//        window.RTCPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection;   //compatibility for firefox and chrome
//
//        if (pc)
//            pc.close();
//        
//        pc = new RTCPeerConnection({iceServers:[]});   
//        pc.onicecandidate = onIceCandidate;   
//        pc.createDataChannel("");   
//        pc.createOffer(pc.setLocalDescription.bind(pc), noop);   
//    
//    } catch (e)
//    { console.log(e.message);}
//}
//
//function noop()
//{
//}
//
//function onIceCandidate(ice)
//{   
//    console.log(ice.candidate);
//
//    if(!ice || !ice.candidate || !ice.candidate.candidate)  return;
//    
//    var my_ip = /([0-9]{1,3}(\.[0-9]{1,3}){3}|[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7})/.exec(ice.candidate.candidate)[1];
//
//    this.onicecandidate = noop;
//
//    ip = my_ip.split(".")[0]+'.'+my_ip.split(".")[1]+'.'+my_ip.split(".")[2];
//}
//
//    </script>
    </body>

</html>