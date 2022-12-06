<%-- 
    Document   : e_sign
    Created on : Oct 27, 2017, 9:38:06 AM
    Author     : nikki
--%>
<%@taglib prefix = "s" uri = "/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    response.setContentType("text/html;charset=UTF-8");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("Expires", "0");
    response.setDateHeader("Expires", -1);
    response.setHeader("X-Frame-Options", "DENY");
    response.addHeader("X-Content-Type-Options", "nosniff");
    response.addHeader("X-XSS-Protection", "1; mode=block");
%>
<html lang="en">
    <!-- BEGIN HEAD -->
    <head>
        <%
            String random = entities.Random.csrf_random();
            session.setAttribute("rand", random);
        %>
        <meta charset="utf-8" />
        <title>@Gov.in | e-Sign | e-Forms</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE"/>
        <META HTTP-EQUIV="EXPIRES" CONTENT="0"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/>
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="" name="author" />
        <link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="assets/css/plugins.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/layout.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/css/default.min.css" rel="stylesheet" type="text/css" id="style_color" />
        <script src="assets/plugins/jquery.min.js" type="text/javascript"></script>      
    </head>
    <body class="page-container-bg-solid">
        <div class="page-wrapper">
            <div class="page-wrapper-row">
                <div class="page-wrapper-top">
                    <!-- BEGIN HEADER -->
                    <div class="page-header">
                        <!-- BEGIN HEADER TOP -->
                        <jsp:include page="include/header.jsp" />
                        <!-- END HEADER TOP -->
                        <!--                    </div>
                                             END HEADER 
                                        </div>
                                    </div>
                                    <div class="page-wrapper-row full-height">
                                        <div class="page-wrapper-middle">
                                             BEGIN CONTAINER 
                                            <div class="page-container">
                                                 BEGIN CONTENT 
                                                <div class="page-content-wrapper">
                                                     BEGIN CONTENT BODY 
                                                     BEGIN PAGE HEAD
                                                    <div class="page-head">
                                                        <div class="container">
                                                             BEGIN PAGE TITLE 
                                                            <div class="page-title">
                                                                <h1>Form
                                                                    <small><%//=session.getAttribute("form_text").toString()%></small>
                                                                </h1>
                                                            </div>
                                                             END PAGE TITLE 
                                                        </div>
                                                    </div>-->
                        <!-- END PAGE HEAD-->
                        <!-- BEGIN PAGE CONTENT BODY -->
                        <div class="page-content">
                            <div class="container">
                                <!-- BEGIN PAGE BREADCRUMBS -->
                                <ul class="page-breadcrumb breadcrumb">
                                    <li>
                                        <a href="Forms">HOME</a>
                                        <i class="fa fa-circle"></i>
                                    </li>
                                    <li><!-- form type -->                                        
                                    </li>
                                </ul>
                                <!-- END PAGE BREADCRUMBS -->
                                <!-- BEGIN PAGE CONTENT INNER -->
                                <div class="page-content-inner">
                                    <div class="row">
                                        <div class="portlet light " id="form_wizard_2">
                                            <div class="portlet-title">
                                                <div class="caption">
                                                    <i class=" icon-layers font-red"></i>
                                                    <span class="caption-subject font-red bold"> </span>
                                                </div>
                                            </div>
                                            <div class="portlet-body form">
                                                <div class="row">
                                                    <div class="col-md-12">
                                                        <div class="form-body">
                                                            <div class="form-group" style="padding:15px;" id="esign1">
                                                                <p id="reg_num"></p>
                                                                <p><i class="entypo-info-circled"></i> You can use  it to track your request. You can track your request using <a href="showUserData">Track User</a></p>
                                                                <p><i class="entypo-info-circled"></i> For any assistance, please contact on <b>1800-111-555</b> or mail us to <b>servicedesk@nic.in</b>.</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-md-12 text-right">
                                                        <div class="form-action">
                                                            <button type="button" class="btn dark btn-outline" id="done_close" >HOME</button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- END PAGE CONTENT INNER -->
                        </div>
                    </div>
                    <!-- END PAGE CONTENT BODY -->
                    <!-- END CONTENT BODY -->
                </div>
                <!-- END CONTENT -->
            </div>
            <!-- END CONTAINER -->
        </div>
    </div>
    <!-- end -->
    <jsp:include page="include/footer.jsp" />
    <script src="assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
    <script src="assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
    <script src="assets/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
    <script src="assets/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
    <script src="assets/plugins/bootstrap-wizard/jquery.bootstrap.wizard.min.js" type="text/javascript"></script>
    <script src="assets/scripts/app.min.js" type="text/javascript"></script>
    <script src="assets/scripts/form-wizard.min.js" type="text/javascript"></script>
    <script src="js/onlineforms.js" type="text/javascript"></script>
    <script src="js/imappop.js" type="text/javascript"></script>
</body>
</html>