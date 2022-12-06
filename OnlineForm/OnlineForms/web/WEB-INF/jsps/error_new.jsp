<html lang="en">
    <!-- BEGIN HEAD -->
<%     
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.setStatus(HttpServletResponse.SC_FOUND);
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<script>
    //window.location.href ="index.jsp";
    </script>
    <head>
        <meta charset="utf-8" />
        <title>@Gov.in | e-Forms</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>
        <META HTTP-EQUIV="EXPIRES" CONTENT="0"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="NIC" name="author" />
        <link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="assets/css/login.min.css" rel="stylesheet" type="text/css" />
    </head>
    <!-- END HEAD -->
    <body class=" login">
        <div class="page-wrapper-row" style="background:#fff;">
            <div class="page-wrapper-top">
                <!-- BEGIN HEADER -->
                <div style="padding-bottom:10px;">
                    <!-- BEGIN HEADER TOP -->
                    <div class="page-header-top">
                        <div class="container" style="padding:0px;">
                            <!-- BEGIN LOGO -->
				<div class="page-logo" style="margin-top:15px;height:auto;">
                                        <a href="Forms" style="text-decoration:none;">
                                                <img src="assets/img/eforms.png" alt="logo" class="logo-default" style="margin: 3.5px 6px 0;">
<!--                                                <strong>Forms for NIC services</strong>-->
                                        </a>
                                </div>
                            <!-- END LOGO -->
                        </div>
                    </div>
                    <!-- END HEADER TOP -->
                </div>
                <!-- END HEADER -->
            </div>
        </div>
        <!-- BEGIN LOGIN -->        
        <div class="content " id="signup-tab" >
            <!-- BEGIN LOGIN FORM -->
            <h3 class="form-title font-green">Something went wrong. Please try again.</h3>
            <div class="alert alert-danger display-hide">
                
            </div>
            
            <!-- END LOGIN FORM -->
        </div>
        
        <div class="copyright"> 2017 &copy; National Informatics Center. </div>
        <footer class="login-footer" style="position: fixed; width: 100%; background: #000000; padding: 14px; bottom: 0px; right: 0px; margin-bottom: 0px;">
            <section class="container foot">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-xs-12" style="text-align:center">
                        <a href="https://india.gov.in/" target="_blank"><img src="assets/img/footer/india_gov_logo.png" alt="india_gov_logo" class="foot-indiagv"></a>
                        <a href="http://nkn.gov.in/" target="_blank"><img src="assets/img/footer/nkn-logo.png" alt="nkn-logo" class="marg-footer-nkn"></a>
                        <a href="http://meity.gov.in/" target="_blank"><img src="assets/img/footer/meity_logo.png" alt="meity_logo" class="marg-footer"></a>
                        <a href="http://pmindia.gov.in/en/" target="_blank"><img src="assets/img/footer/pm_india_logo.png" alt="pm_india_logo" class="marg-footer"></a>
                        <a href="http://www.digitalindia.gov.in/" target="_blank"><img src="assets/img/footer/digital_india_logo.png" alt="digital_india_logo" class="marg-footer"></a>
                        <a href="https://mygov.in/" target="_blank"><img src="assets/img/footer/esampark.png" alt="eSampark-footer-logo" class="marg-footer"></a>
                        <a href="https://egreetings.gov.in/" target="_blank"><img style="width: 195px; height: 56px;" src="assets/img/footer/eGreetings_small.png" alt="mygov_e_greetings" class="marg-footer"></a>
                    </div>
                </div>
            </section>
        </footer>
        <!-- BEGIN CORE PLUGINS -->
        <script src="assets/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="js/register.js" type="text/javascript"></script>
    </body>
</html>
