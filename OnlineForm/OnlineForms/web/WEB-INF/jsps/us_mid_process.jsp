<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
    <!--<![endif]-->
    <!-- BEGIN HEAD -->

    <head>
        <meta charset="utf-8" />
        <title>@Gov.in | Form | Online Forms</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta content="width=device-width, initial-scale=1" name="viewport" />
        <meta content="" name="description" />
        <meta content="" name="author" />
        <link href="assets/old_assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/old_assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/old_assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/old_assets/css/components.min.css" rel="stylesheet" id="style_components" type="text/css" />
        <link href="assets/old_assets/css/plugins.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/old_assets/css/layout.min.css" rel="stylesheet" type="text/css" />
        <link href="assets/old_assets/css/default.min.css" rel="stylesheet" type="text/css" id="style_color" />
	
</head>

    <body class="page-container-bg-solid">
        <div class="page-wrapper">
            <div class="page-wrapper-row">
                <div class="page-wrapper-top">
                    <!-- BEGIN HEADER -->
                    <div class="page-header">
                        <!-- BEGIN HEADER TOP -->
                        <div class="page-header-top" style="background: #333;">
                            <div class="container">
                                <!-- BEGIN LOGO -->
                                <div class="page-logo">
                                    <a href="index.html">
                                        <img src="assets/media/logos/logo-1.png" alt="logo" class="logo-default" style="width: 170px;">
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
            <div class="page-wrapper-row full-height">
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
                                    <div class="page-title">
                                        <h1>eForms
                                            <small>Action Page</small>
                                        </h1>
                                    </div>
                                    <!-- END PAGE TITLE -->
                                    
                                </div>
                            </div>
                            <!-- END PAGE HEAD-->
                            <!-- BEGIN PAGE CONTENT BODY -->
                            <div class="page-content">
                                <div class="container">
                                    <!-- BEGIN PAGE BREADCRUMBS -->
                                    <ul class="page-breadcrumb breadcrumb">
                                        <li>
                                            <a href="index.html">eForms</a>
                                            <i class="fa fa-circle"></i>
                                        </li>
                                        <li>
                                            <span>Action Page</span>
                                        </li>
                                    </ul>
                                    <!-- END PAGE BREADCRUMBS -->
                                    <!-- BEGIN PAGE CONTENT INNER -->
                                    <div class="page-content-inner">
                                        
					<div class="portlet light portlet-fit ">
                                                    <div class="portlet-title">
                                                        <div class="caption">
                                                            <i class=" icon-layers font-green"></i>
                                                            <span class="caption-subject font-green bold uppercase">Action</span>
                                                        </div>
                                                    </div>
                                                    <div class="portlet-body">
                                                        
                          <h3>Dear Sir/Madam,</h3>
<p>A request has been forwarded to You by the Reporting Officer(<% if(session.getAttribute("ro_email") != null){ %><%=session.getAttribute("ro_email")%><% } %>) for your approval through eForms with Reg No <strong><% if(session.getAttribute("reg_no") != null){ %><%=session.getAttribute("reg_no")%><% } %></strong><br><br>

You are requested to do the due diligence and verify the credentials of user and his Reporting Officer before approving the request.The PDF with Form Details has been attached with this mail and the Applicant and Reporting Officers Details are shown below:

</p>



			<div class="portlet box green">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-comments"></i>Applicant Details </div>
                                
                            </div>
                            <div class="portlet-body">
                                <div class="table-scrollable">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th width="250px"> &nbsp; </th>
                                                <th> &nbsp; </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
					      <td class="text" width="100">Name</td>
					      <td class="text"><% if(session.getAttribute("name") != null){ %><%=session.getAttribute("name")%><% } %></td>
					    </tr>
						  
					   <tr>
					      <td class="text">Email</td>
					      <td class="text"><% if(session.getAttribute("email") != null){ %><%=session.getAttribute("email")%><% } %></td>
					    </tr>
						  
					    <tr>
					      <td class="text">Mobile</td>
					      <td class="text"><% if(session.getAttribute("mobile") != null){ %><%=session.getAttribute("mobile")%><% } %></td>
					    </tr>
						  
					   <tr>
					      <td class="text">Designation</td>
					      <td class="text"><% if(session.getAttribute("desig") != null){ %><%=session.getAttribute("desig")%><% } %></td>
					    </tr>
						  
					  <tr>
					      <td class="text">Organization</td>
					      <td class="text"><% if(session.getAttribute("ministry") != null){ %><%=session.getAttribute("ministry")%><% } %></td>
					    </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>


			<div class="portlet box green">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-comments"></i>Reporting Officer's Details </div>
                                
                            </div>
                            <div class="portlet-body">
                                <div class="table-scrollable">
                                    <table class="table table-striped table-hover">
                                        <thead>
                                            <tr>
                                                <th width="250px"> &nbsp; </th>
                                                <th> &nbsp; </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
					      <td class="text">RO Name</td>
					      <td class="text"><% if(session.getAttribute("ro_name") != null){ %><%=session.getAttribute("ro_name")%><% } %></td>
					    </tr>
						  
					   <tr>
					      <td class="text">Ro Email</td>
					      <td class="text"><% if(session.getAttribute("ro_email") != null){ %><%=session.getAttribute("ro_email")%><% } %></td>
					    </tr>
						  
					  <tr>
					      <td class="text">Ro Mobile</td>
					      <td class="text"><% if(session.getAttribute("ro_mobile") != null){ %><%=session.getAttribute("ro_mobile")%><% } %></td>
					    </tr>
						  
					   <tr>
					      <td class="text">Designation</td>
					      <td class="text"><% if(session.getAttribute("ro_desig") != null){ %><%=session.getAttribute("ro_desig")%><% } %></td>
					    </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

	  
	  
			<div class="row">
				<div class="col-md-9">
				<div class="form-group">
					<label>Please enter the OTP sent to your registered Mobile, and then Please click on the checkbox to proceed further.</label>
					<div class="input-group">
		                                <input type="text" name="us_otp" id="us_otp" placeholder="Please Enter OTP" class="form-control">
                                                
		                                <span class="input-group-btn">
                                                    <button id="resend_us_otp" class="btn blue" type="button"><i class="fa fa-repeat"></i> Resend</button>
		                                </span>
		                        </div>
                                        <span class="text text-danger" id="otp_err"></span>

				</div>
				</div>
			</div>
			
			<div class="form-group">
			    <div class="mt-checkbox-list">
	                        <label class="mt-checkbox mt-checkbox-outline"> I hereby declare that i know the officer and the credentials submitted by the applicant are hereby approved and verified by me.<br/> And hence, the government ID may be created for him.
	                            <input type="checkbox" name="term_id" id="term_id"  value="1" required>
	                            <span></span>
	                        </label>
	                        
	                    </div>
	                </div>

			<div class="row" >
				<div class="form-group" style="float:right;">
                                    <a class="approve_td" style="display:none;" href="https://eforms.nic.in/us_process?aid=<% if(session.getAttribute("sha_value") != null){ %><%=session.getAttribute("sha_value")%><% } %>"><button type="button"  class="btn red btn-outline"><i class="fa fa-times"></i> Approve</button></a>
					<a class="reject_td" style="display:none;"  href="https://eforms.nic.in/us_process?rid=<% if(session.getAttribute("sha_value") != null){ %><%=session.getAttribute("sha_value")%><% } %>"><button type="button"  class="btn green btn-outline"><i class="fa fa-check"></i> Reject</button></a>
				</div>
			</div>
			
	

</div>
</div>

</div>
                                                               
                                                            
                                                        
            <div class="page-wrapper-row">
                <div class="page-wrapper-bottom">
                    <!-- BEGIN FOOTER -->
                    <!-- BEGIN PRE-FOOTER -->
                    <div class="page-prefooter">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-2 col-sm-6 col-xs-12 footer-block">
                                    <a href="#"><img src="assets/old_assets/img/footer/india_gov_logo.png" style="height:40px;"></a>
                                </div>
                                <div class="col-md-2 col-sm-6 col-xs-12 footer-block">
                                    <a href="#"><img src="assets/old_assets/img/footer/nkn-small.png" style="height:40px;"></a>
                                </div>
                                <div class="col-md-2 col-sm-6 col-xs-12 footer-block">
                                    <a href="#"><img src="assets/old_assets/img/footer/meity_logo.png" style="height:40px;"></a>
                                </div>
                                <div class="col-md-2 col-sm-6 col-xs-12 footer-block">
                                    <a href="#"><img src="assets/old_assets/img/footer/digital_india_logo.png" style="height:40px;"></a>
                                </div>
                                <div class="col-md-2 col-sm-6 col-xs-12 footer-block">
                                    <a href="#"><img src="assets/old_assets/img/footer/eGreetings_small.png" style="height:40px;"></a>
                                </div>
                                <div class="col-md-2 col-sm-6 col-xs-12 footer-block">
                                    <a href="#"><img src="assets/old_assets/img/footer/esampark.png" style="height:40px;"></a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- END PRE-FOOTER -->
                    <!-- BEGIN INNER FOOTER -->
                    <div class="page-footer">
                        <div class="container"> 2019 &copy; <a target="_blank" href="http://nic.in">National Informatics Center  </a>
                            
                        </div>
                    </div>
                    <div class="scroll-to-top">
                        <i class="icon-arrow-up"></i>
                    </div>
                    <!-- END INNER FOOTER -->
                    <!-- END FOOTER -->
                </div>
            </div>
        </div>
        
        <!--[if lt IE 9]>
	<script src="assets/old_assets/plugins/respond.min.js"></script>
	<script src="assets/old_assets/plugins/excanvas.min.js"></script> 
	<script src="assets/old_assets/plugins/ie8.fix.min.js"></script> 
	<![endif]-->
        <script src="assets/old_assets/plugins/jquery.min.js" type="text/javascript"></script>
        <script src="assets/old_assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
        <script src="assets/old_assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
        <script src="assets/old_assets/plugins/select2/js/select2.full.min.js" type="text/javascript"></script>
        <script src="assets/old_assets/plugins/bootstrap-wizard/jquery.bootstrap.wizard.min.js" type="text/javascript"></script>
        <script src="assets/old_assets/scripts/app.min.js" type="text/javascript"></script>
        <!--<script src="assets/old_assets/scripts/form-wizard.min.js" type="text/javascript"></script>-->
        
        <script src="assets/old_assets/plugins/bootbox.min.js" type="text/javascript"></script>
        
        <script>
            
            function showAlertBox(text)
            {
                //console.log(" inside show alert box function ");

                bootbox.alert(text, function () {
                    //console.log("Alert Callback");
                });
            }
            
            
            $(document).on('click', '#term_id', function ()
            {
                if ($(this).is(":checked"))
                {
                    console.log(" checkbox is checked ");
                    
                    // check for the value of OTP if not present show message
                    
                    var us_otp = $("#us_otp").val();
                    
                    if( us_otp == "" )
                    {
                        showAlertBox("Please enter OTP") // line modified by pr on 22ndmar19
                    }
                    else
                    {
                        // proceed with AJAX to the serverside to check otp with the value in the DB
                       
                        var regNo = '<% if(session.getAttribute("reg_no") != null){ %><%=session.getAttribute("reg_no")%><% } %>';
                       
                        console.log(" inside us mid process reg no is "+regNo+" urs otp is "+us_otp);
                       
                        $.ajax({
                        type: "POST",
                        url: "checkUSOTP",
                        data: {regNo: regNo, usOTP:us_otp},
                        datatype: JSON,
                        success: function (jsonResponse)
                        {
                            console.log(" inside checkus otp success isSuccess "+jsonResponse.isSuccess+" msg is "+jsonResponse.msg );
                            
                            var isSuccess = jsonResponse.isSuccess;
                            
                            var msg = jsonResponse.msg;
                            
                            if( isSuccess )
                            {
                                // if all well then show the approve and reject buttons
                                
                                $(".approve_td").show();
                                
                                $(".reject_td").show();
                                
                            }
                            else
                            {
                                $("#otp_err").html(msg);
                                
                                $(".approve_td").hide();
                                
                                $(".reject_td").hide();
                                
                            }
                            
                        }, 
                        error: function ()
                        {
                            console.log("error")
                            
                            $(".approve_td").hide();
                                
                            $(".reject_td").hide();
                        }

                    });
   
                        
                        
                    }
                    
                }
                else
                {
                    console.log(" checkbox is UNchecked ");
                    
                    // hide the approve and reject buttons
                    
                    $(".approve_td").hide();
                                
                    $(".reject_td").hide();
                }
                
            });            
            
            $(document).on('keyup', '#us_otp', function ()
            {
                // clear the checkbox if checked
                
                console.log(" inside us otp onkeyup ");
                
                $("#term_id").prop("checked", false);
                
                $(".approve_td").hide();
                                
                $(".reject_td").hide();
                
                 $("#otp_err").html("");
                
            });
            
            
            $(document).on('click', '#resend_us_otp', function ()
            {
                // clear the checkbox if checked
                
                console.log(" inside resend_us_otp func ");
                
                // var regNo = '<% if(session.getAttribute("reg_no") != null){ %><%=session.getAttribute("reg_no")%><% } %>';
                
                $.ajax({
                        type: "POST",
                        url: "resendUSOTP",
                        //data: {regNo: regNo},
                        datatype: JSON,
                        success: function (jsonResponse)
                        {
                            console.log(" inside resendUSOTP success isSuccess "+jsonResponse.isSuccess+" msg is "+jsonResponse.msg );
                            
                            var isSuccess = jsonResponse.isSuccess;
                            
                            var msg = jsonResponse.msg;
                            
                            $("#otp_err").html(msg);
                                                       
                        }, 
                        error: function ()
                        {
                            console.log("error")
                           
                        }                
                
                
                    });
            
            });
            
        </script>
        
    </body>

</html>