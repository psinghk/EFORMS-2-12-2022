<%@page import="com.org.dao.Ldap"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    String email = "", Name = "", Mobile = "", coordEmail = "";
    System.out.println("SESSION :::: " + ActionContext.getContext().getSession());
    System.out.println("SESSION CAPTCHA :::: " + session.getAttribute("captcha").toString());
    if (ActionContext.getContext().getSession() != null) {
        System.out.println("coordEmail = " + session.getAttribute("das_coords"));
        email = session.getAttribute("email").toString();
//            dob = session.getAttribute("dob").toString();
        Name = session.getAttribute("name").toString();
        Mobile = session.getAttribute("mobile").toString();
        coordEmail = session.getAttribute("das_coords").toString();
    }
%>
<head>
    <meta charset="utf-8">
    <title>@Gov.in | e-Forms</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <meta content="" name="keywords">
    <meta content="" name="description">
    <link href="assets/home-page/lib/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/home-page/lib/bootstrap/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/home-page/update-mobile/css/style.css">
    <link rel="stylesheet" href="assets/home-page/update-mobile/css/navigation.css">
    <style>
        .circle,
        .circle-border {
            width: 80px;
            height: 80px;
            border-radius: 50%;
        }
        .circle {
            z-index: 1;
            position: relative;
            background: white;
            transform: scale(1);
            animation: success-anim 700ms ease;
        }
        .circle-border {
            z-index: 0;
            position: absolute;
            transform: scale(1.1);
            animation: circle-anim 400ms ease;
            background: #f86;	
        }
        @keyframes success-anim {
            0% {
                transform: scale(0);
            }
            30% {
                transform: scale(0);
            }
            100% {
                transform: scale(1);
            }
        }
        @keyframes circle-anim {
            from {
                transform: scale(0);
            }
            to {
                transform: scale(1.1);
            }
        }
        .error::before,
        .error::after {
            content: "";
            display: block;
            height: 7px;
            background: #f86;
            position: absolute;
        }
        .error::before {
            width: 50px;
            top: 48%;
            left: 20%;
            transform: rotateZ(50deg);
        }
        .error::after {
            width: 50px;
            top: 48%;
            left: 20%;
            transform: rotateZ(-50deg);
        }
        #wizard {
            padding: 25px !important;
            min-height: 350px !important;
            margin-right: 0px;
            font-size: 16px;
            margin-bottom: 150px;
        }
        #clients {
            padding: 30px 0 30px !important;
        }
    </style>
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
                <div style="text-align: center; padding: 0px 0px 0px 500px;">
                    <a class="c-btn c-btn--action -big" href="logout_parichay_retired" data-toggle="tooltip" data-placement="bottom" title="Logout">
                        <span>Logout</span>
                    </a>
                </div>
                <div style="text-align: center; padding: 30px 0px 0px 265px;">
                    <div class="circle-border"></div>
                    <div class="circle">
                        <div class="error"></div>
                    </div>
                </div>
                <h5 class="text-center mt-5 mb-4 text-danger">As per our record, it seems that you are not a retired official. To be a retired official, you must have following before retirement:- </h5>
                <ol style="font-size: 14px">
                    <li>You must have served government for at least 20 years</li>
                    <li>You must have been in Central/State government organization</li>
                    <li>You must have been a regular employee and must have been drawing salary from government</li>
                </ol> 
                <br>
                <input type="hidden" value="<%=coordEmail%>" class="coordDetails" id="coordDetails" />
                <div class="form-group">
                    <h6 style="font-size: 14px"><b>Email:</b> <%=session.getAttribute("email").toString()%></h6>
                    <h6 style="font-size: 14px"><b>Name:</b> <%=session.getAttribute("name").toString()%></h6>
                    <h6 style="font-size: 14px"><b>Mobile Number: </b> <%=session.getAttribute("mobile").toString()%></h6>
                    <br>
                </div>
                <p class="text-dark" style="font-size: 13px"> Please contact your concerned officer as shown below for further assistance. </p>
                <br>
                <div class="coord" id="tableCoord"></div>
                
                <h6 class="text-center mt-5 mb-4 text-secondary">For any further assistance/queries, kindly call at our toll free number 1800111555 or raise a ticket at https://servicedesk.nic.in.</h6>
                <div style="overflow:auto;" class="mt-2">
                    <div class="col-md-12 p-0" style="text-align: center;">
                        <button type="button" class="btn btn-primary btn-action" id="submit"><a href="https://eforms.nic.in/" style="color: #ffff;">Go To Homepage</a></button>
                    </div>
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
</body>
</html>
<script src="main_js/retired-officers.js" type="text/javascript"></script>
<script src="assets/vendors/general/jquery/dist/jquery.js?v=1.0" type="text/javascript"></script>
<script src="assets/old_assets/plugins/jquery.serializeObject.js?v=1.0"></script>
<script src="assets/home-page/update-mobile/js/navigation.js"></script>
<script src="assets/home-page/lib/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="assets/old_assets/plugins/jquery-ui.js?v=1.0"></script>
<script src="assets/datepickertime/js/moment.min.js" type="text/javascript"></script>
<script src="assets/datepickertime/js/bootstrap-datetimepicker.min.js" type="text/javascript"></script>
<script src="assets/home-page/update-mobile/js/updatemobsubmit.js"></script>
<script>
    $(document).ready(function () {
        let string_coord = $("#coordDetails").val();
        let str_array = string_coord.split(",");
        var table = `
   <table style="font-size:12px" class="table table-striped table-bordered table-hover">
                        <thead>
                            <tr style="font-size: 14px">
                                <th>S.No.</th>
                                <th>Coordinator/Delegate Admin Email</th>
                                <th>Coordinator/Delegate Admin Mobile</th>
                            </tr>
                        </thead>`;
        for (var i = 0; i < str_array.length; i++) {
            str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
            let a = str_array[i];
            let count = i + 1;
            let b = a.split(":");
            table += `  <tbody>
                                                <tr>
                                                    <td> ` + count + `</td>
                                                    <td> ` + b[0] + ` </td>
                                                    <td> ` + b[1] + ` </td>
                                                </tr>
                                            </tbody>`;
        }
        table += ` </table>`
        $("#tableCoord").html(table);
    });
</script>

