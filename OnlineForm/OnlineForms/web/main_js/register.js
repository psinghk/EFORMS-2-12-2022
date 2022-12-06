$(document).ready(function () {

    $('#submit-pwd').submit(function (e) {
       

        $('.loader').show();
        e.preventDefault();
        // alert("hahhahahah");
        //var email = $('#email').val();
        var lowercase_email = $('#email').val();// add on 20-04-2022
        var email = lowercase_email.toLowerCase();// add on 20-04-2022 
        // alert(email);
        var password = $('#password').val();
        var mobile = $('#mobile').val();
        var country_code = $('#country_code').val();
        var login_captcha = $('#captcha1').val();

        var emailregn = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var mobileregn = /^[0-9]{10}$/;
        var mobileIntregn = /^[+0-9]{8,15}$/;
        var captchaRegex = /^[0-9a-zA-Z]{6}$/;
        
        if ((email !== null && email !== "")) {
            if(!mobile.match(mobileregn) && !$('#mobile-div').hasClass("display-hide")){
                $("#mobile_hide").html("Please enter Mobile number in correct format");
            }
//            if(mobile.match(mobileregn) && !$('#mobile-div').hasClass("display-hide")){
//               alert("1")
//               $("#mobile_hide").hide();
//            }else {
//                alert("2")
//                $('.loader').hide();
//               $("#mobile_hide").html("Please enter Mobile number in correct format");
//            }
            if (email.match(emailregn)) {
                if ((password === null || password === "") && (mobile === null || mobile === "")) {
//                    danger_msg("Please enter Password.");
                    
                    $('#continue').attr("disabled", true);
                    var dataObj = {userValues: {email: email}};
                    var data1 = JSON.stringify(dataObj);
                    $.ajax({
                        type: "POST",
                        url: "AuthEmail",
                        data: data1,
                        datatype: JSON,
                        contentType: 'application/json; charset=utf-8',
                        success: function (data) {
                            
                            $('#email_error').hide();
                           if ($('#password').val() === "" && !$('#password-div').hasClass("display-hide")) {
                            $('#fill_password').html("Please enter password details.")
                            }
                            if(data.userValues.blocked === true){
                                 $('#err_msg_mobile').html('You have been Blocked due to multiple wrong attempts');
                                          $("#mobile").prop('readonly', true);
                                    $("#captcha1").prop('readonly', true);
                                    $("#country_code").prop('readonly', true);
                                    $("#mobile_disable").attr("disabled", true);
                                    $("#refresh").hide();
                            }
                            $('.loader').hide();
                            console.log(data);
                            if (!data.errors) {
                                $('#continue').attr("disabled", false);
                                
                                if (data.userValues.isEmailValidated) {
                                    if (data.userValues.pariSessionout === "parichayout") {
                                        bootbox.alert("Since, You are an existing user of the NIC, you can login only through NIC Single Sign-on Platform(Parichay).", function () {
                                            //window.location.href="https://parichay.staging.nic.in:8080/Accounts/ClientManagement?sessionTimeOut=true&service=efromsCO";
                                            window.location.href = "https://parichay.nic.in/Accounts/ClientManagement?sessionTimeOut=true&service=eforms";
                                            //window.location.href = 'https://parichay.pp.nic.in/Accounts/NIC/index.html?service=eforms';
                                            //window.location.href = 'https://parichay.nic.in/Accounts/NIC/index.html?service=eforms';
                                        });


                                        //window.location.href="https://parichay.pp.nic.in/Accounts/ClientManagement?sessionTimeOut=true&service=eforms";
                                        //window.location.href = "https://parichay.nic.in/Accounts/Services?service=eforms";

                                    } else
                                    if (data.userValues.ssoAllowed === "ssologin") {
                                        $('#password-div').addClass("display-hide");
                                        $('#captcha_div').addClass("display-hide");
                                        bootbox.alert("Since, You are an existing user of the NIC, you can login only through NIC Single Sign-on Platform(Parichay).", function () {
                                            //window.location.href="https://parichay.staging.nic.in:8080/Accounts/Services?service=efromsCO";
                                            //window.location.href = 'https://parichay.pp.nic.in/Accounts/NIC/index.html?service=eforms';
                                            window.location.href = 'https://parichay.nic.in/Accounts/NIC/index.html?service=eforms';
                                        });
                                    } else {

                                        if (data.userValues.isEmailValidated && (email == "support@nic.in" || email == "support@gov.in" || email == "support@nkn.in" || email == "smssupport@nic.in" || email == "vpnsupport@nic.in"))
                                        {  // alert("inside support");
                                            $('#password-div').removeClass("display-hide");
                                            //$('#captcha_div').removeClass("display-hide");

                                        } else {

                                            $('#password-div').removeClass("display-hide");
                                            $('#captcha_div').removeClass("display-hide");
                                        }



                                    }
                                    if ((email == "support@nic.in" || email == "support@gov.in" || email == "support@nkn.in" || email == "smssupport@nic.in" || email == "vpnsupport@nic.in") && (mobile == null || mobile == "")) {
                                        $('#captcha_div').addClass("display-hide");
                                    }
                                    $('#mobile-div').addClass("display-hide");
                                    $('#email').prop('readonly', true);
                                    $('#password').removeAttr('disabled');
                                    $('#password').focus();
                                    $("input[name='isNewUser']").val(data.userValues.isNewUser);
                                    $("input[name='isEmailValidated']").val(data.userValues.isEmailValidated);

                                } else if (!data.userValues.isNewUser) { // user not in ldap but in db
                                    console.log("hereeeeeeeeeeeeeeeeeeeeeeeeeeee user not in ldap but in db")

                                    if (data.userValues.emailCountAgainstMobile > 0)
                                    {
                                        $('#email_error_emailCountAgainstMobile').html("Please come through your government email address <b>" + data.userValues.emailAgainstMobileNumber) + "</b>";
                                         $('#email_error_emailCountAgainstMobile').removeClass('display-hide');    
                                    } else {
                                        bootbox.dialog({
                                            message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li> <li>VPN Service</li><li>SMS Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                                                    + " <p style='text-align: center; font-size: 15px;'>To register for other services, please log in with your government email service(NIC) email address.</p><div class='alert alert-danger' style='text-align: center;font-size: 18px;WORD-WRAP: break-word;'> Are you sure, you want to proceed with <b>" + email + "</b>?</div>",
                                            title: "<b>NOTE:</b>",
                                            buttons: {
                                                success: {
                                                    label: "YES",
                                                    className: "green",
                                                    callback: function () {
                                                        var dataObj = {userValues: {email: email, mobile: mobile, countryCode: country_code, isEmailValidated: data.userValues.isEmailValidated, isNewUser: data.userValues.isNewUser, isMobileInLdap: data.userValues.isMobileInLdap}};
                                                        var data1 = JSON.stringify(dataObj);
                                                        $.ajax({
                                                            type: "POST",
                                                            url: "ValidateEmail",
                                                            data: data1,
                                                            datatype: JSON,
                                                            contentType: 'application/json; charset=utf-8',
                                                            success: function (data) {
                                                                console.log(data)
                                                                var startMobile = data.userValues.mobile.substring(0, 3);
                                                                var endMobile = data.userValues.mobile.substring(10, 13);
                                                                var name = email.substring(0, email.lastIndexOf("@"));
                                                                var domain = email.substring(email.lastIndexOf("@") + 1)
                                                                var startName = name.substring(0, 3);
                                                                $('#otp-tab').removeClass("display-hide");
                                                                $('#signup-tab').addClass("display-hide");
                                                                $('#mobile-otp-div').removeClass("display-hide");
                                                                $('#email-otp-div').removeClass("display-hide");
                                                                $('#mobileOtp').attr("placeholder", "OTP has been sent on " + startMobile + 'XXXXXXX' + endMobile);
                                                                $("input[name='mobileOtpActive']").val(data.userValues.mobileOtpActive);
                                                                $("input[name='emailOtpActive']").val(data.userValues.emailOtpActive);
                                                                if (data.userValues.mobileOtpActive)
                                                                {
                                                                    $('#mobileOtp').attr('placeholder', 'Please use previos OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#emailOtp').attr('placeholder', 'Please use previous OTP sent on ' + startName + '*****@' + domain);
                                                                    $('#mobile_info').html('Please use previos OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#email_info').html('Please use previous OTP sent on ' + startName + '*****@' + domain);
                                                                } else {
                                                                    $('#mobile_info').html("OTP has been sent on " + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#email_info').html('Please Enter OTP sent on ' + startName + '*****@' + domain);
                                                                }
                                                                $("input[name='mobile']").val("");
                                                                $("input[name='new_mobile']").val(data.userValues.mobile);
                                                                $("input[name='new_email']").val(data.userValues.email);
                                                                $('#mobileOtp').focus();
                                                                $('#or-div').addClass("display-hide");
                                                                $('#resend_mobile').removeClass("display-hide");
                                                                $('#r_mobile_div').addClass("col-md-6");
                                                                $('#r_email_div').addClass("col-md-6");
                                                                $('#resend_email').removeClass("display-hide");
                                                                $('#resend_mobile').removeClass("btn-block");
                                                            }, error: function () {
                                                                console.log('error');
                                                            }
                                                        });
                                                    }
                                                },
                                                main: {
                                                    label: "NO",
                                                    className: "red",
                                                    callback: function () {
                                                        bootbox.hideAll();
                                                    }
                                                }
                                            }
                                        });
                                    }
//                                       
                                } else {
                                    $('#password').val("");
                                    // user not in ldap and not in db i.e New User with Non-gov Email address
                                    bootbox.dialog({
                                        message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li> <li>VPN Service</li> <li>SMS Service</li><li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                                                + " <p style='text-align: center; font-size: 15px;'>To register for other services, please log in with your government email service(NIC) email address.</p><div class='alert alert-danger' style='text-align: center;font-size: 18px;WORD-WRAP: break-word;'> Are you sure, you want to proceed with <b>" + email + "</b>?</div>",
                                        title: "<b>NOTE:</b>",
                                        buttons: {
                                            success: {
                                                label: "YES",
                                                className: "green",
                                                callback: function () {
                                                    // user is not in ldap or in db
                                                    $('#email_error').html("");
                                                    $('#mobile-div').removeClass("display-hide");
                                                    $('#password-div').addClass("display-hide");
                                                    $('#captcha_div').removeClass("display-hide");
                                                    //emil field readonly commented by MI on 29-05-18
                                                    $('#email').prop('readonly', true);
                                                    $('#mobile').removeAttr('disabled');
                                                    $('#mobile').focus();
                                                }
                                            },
                                            main: {
                                                label: "NO",
                                                className: "red",
                                                callback: function () {
                                                    bootbox.hideAll();
                                                }
                                            }
                                        }
                                    });
                                }
                            } else {
                                if (data.errors.email === 'empty')
                                    $('#email_error').html("Please Enter Your Email Address");
                                else
                                    $('#email_error').html("Please Enter Your Email in correct format.");
                                return false;
                            }
                        }
                    });
                } else if ((password !== null && password !== "") && (mobile !== null || mobile !== "")) {
                    if ((email == "support@nic.in" || email == "support@gov.in" || email == "support@nkn.in" || email == "smssupport@nic.in" || email == "vpnsupport@nic.in") && (mobile == null || mobile == ""))
                    {
                        $('.loader').hide();
                        $('#mobile-div').removeClass("display-hide");
                        $('#password-div').addClass("display-hide");
                        $('#captcha_div').removeClass("display-hide");
                        $('#otp-tab').addClass("display-hide");
                        $('#mobile').removeAttr('disabled');
                        $('#email').removeAttr('disabled');
                    } else if ((email == "support@nic.in" || email == "support@gov.in" || email == "support@nkn.in" || email == "smssupport@nic.in" || email == "vpnsupport@nic.in") && (mobile !== null && mobile !== ""))
                    {
                        var dataObj = {userValues: {email: email, password: password, loginCaptcha: login_captcha, mobile: mobile}};
                        var data1 = JSON.stringify(dataObj);
                        $.ajax({
                            type: "POST",
                            url: "AuthPwd",
                            data: data1,
                            datatype: JSON,
                            contentType: 'application/json; charset=utf-8',
                            success: function (data) {
                                $('.loader').hide();
//                                anmol

                                if (data.userValues.blocked === true) {
                                    $('#password_error').hide();
                                    $('#blocked_user').html('You have been blocked due to wrong multiple attempts.');
                                    $('#password').prop('readonly', true);
                                    $('#captcha1').prop('readonly', true);
                                    $('#mobile_disable').attr('disabled', true);
                                }
                                //$('.loader').removeClass("display-hide")
                                if (!data.errors) {



                                    if (data.userValues.loginCaptchaAuthenticated === true) {

                                        if (data.userValues.authenticated === true) {

                                            var dataObj = {userValues: {email: email, mobile: mobile, countryCode: country_code, isEmailValidated: data.userValues.isEmailValidated, isNewUser: data.userValues.isNewUser}};
                                            var data1 = JSON.stringify(dataObj);
                                            $.ajax({
                                                type: "POST",
                                                url: "checkSupportEmail",
                                                data: data1,
                                                datatype: JSON,
                                                contentType: 'application/json; charset=utf-8',
                                                success: function (data) {



                                                    if ((data.userValues.existInsupport) === true || (data.userValues.existInadmin === true))
                                                    {
                                                        $.ajax({
                                                            type: "POST",
                                                            url: "OtpGenerate",
                                                            data: data1,
                                                            datatype: JSON,
                                                            contentType: 'application/json; charset=utf-8',
                                                            success: function (data) {
                                                                $("input[name='new_mobile']").val(data.userValues.mobile);
                                                                $("input[name='country_code_val']").val(data.userValues.countryCode);
                                                                $("input[name='isNewUser']").val(data.userValues.isNewUser);
                                                                $("input[name='isEmailValidated']").val(data.userValues.isEmailValidated);
                                                                $("input[name='isIsNicEmployee']").val(data.userValues.isNICEmployee);
                                                                var startMobile = data.userValues.mobile.substring(0, 3);
                                                                var endMobile = data.userValues.mobile.substring(10, 13);
                                                                var name = email.substring(0, email.lastIndexOf("@"));
                                                                var domain = email.substring(email.lastIndexOf("@") + 1)
                                                                var startName = name.substring(0, 3);
                                                                $("input[name='mobileOtpActive']").val(data.userValues.mobileOtpActive);
                                                                $("input[name='emailOtpActive']").val(data.userValues.emailOtpActive);
                                                                if (data.userValues.mobileOtpActive)
                                                                {
                                                                    $('#mobileOtp').attr('placeholder', 'Please use previos OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#emailOtp').attr('placeholder', 'Please use previous OTP sent on ' + startName + '*****@' + domain);
                                                                    $('#mobile_info').html('Please use previos OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#email_info').html('Please use previous OTP sent on ' + startName + '*****@' + domain);
                                                                } else {
                                                                    $('#mobileOtp').attr('placeholder', 'Please Enter OTP sent on' + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#emailOtp').attr('placeholder', 'Please Enter OTP sent on ' + startName + '*****@' + domain);
                                                                    $('#mobile_info').html('Please Enter OTP sent on' + startMobile + 'XXXXXXX' + endMobile);
                                                                    $('#email_info').html('Please Enter OTP sent on ' + startName + '*****@' + domain);
                                                                }
                                                                $('#email_error').html("");
                                                                // show otp as the user is new
                                                                $('#otp-tab').removeClass("display-hide");
                                                                $('#signup-tab').addClass("display-hide");
                                                                $('#email-otp-div').addClass("display-hide");
                                                                $('#or-div').addClass("display-hide");
                                                                $('#r_mobile_div').addClass("col-md-12");
                                                                $('#r_email_div').removeClass("display-hide");
                                                                $('#mobileOtp').focus();
                                                                $('#resend_mobile').removeClass("display-hide");
                                                            },
                                                            error: function () {
                                                                console.log('error');
                                                            }
                                                        });
                                                    } else {
                                                        $('#mobile_error').html("You are not a valid user");
                                                        $('#mobile-div').removeClass("display-hide");
                                                        $('#captcha_div').addClass("display-hide");
                                                        $('#password-div').addClass("display-hide");
                                                        $('#otp-tab').addClass("display-hide");
                                                    }
                                                },
                                                error: function () {
                                                    console.log('error');
                                                }
                                            });
                                        } else {
                                            console.log(data.userValues.blocked)

                                            // show password field
                                            $('#mobile-div').addClass("display-hide");
                                            $('#password-div').removeClass("display-hide");
                                            $('#password_error').text("Please enter correct password");
                                            $('#captcha_div').removeClass("display-hide");
                                            $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                            $('#captcha1').val("");
                                            $('#password').val("");
                                            $('#password').focus();
                                            return false;
                                        }
                                    } else {
                                        danger_msg("Please enter correct captcha code.");
                                        $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                        return false;
                                    }
                                } else {
                                    if (data.errors.email === 'empty')
                                        $('#email_error').html("Please Enter Your Email Address");
                                    else
                                        $('#email_error').html("Please Enter Your Email in correct format.");
                                    return false;
                                }
                            },
                            error: function () {
                                $('.loader').hide();
                                $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                console.log('error');
                                return false;
                            }
                        });
                        $('#email_error').html("");
                    } else {
                        var isNewUser = $("input[name='isNewUser']").val();
                        var dataObj = {userValues: {email: email, password: password, loginCaptcha: login_captcha, isNewUser: isNewUser}};
                        var data1 = JSON.stringify(dataObj);
                        $.ajax({
                            type: "POST",
                            url: "AuthPwd",
                            data: data1,
                            datatype: JSON,
                            contentType: 'application/json; charset=utf-8',
                            success: function (data) {
                                console.log(data);
                                if (data.userValues.blocked === true) {
                                    $("#blocked_user").text('You have been blocked.');
                                    $("#password").prop('readonly', true);
                                    $("#captcha1").prop('readonly', true);
                                    $("#password_error").hide();
                                }
                                $('.loader').hide();
                                if (!data.errors) {

                                    if (data.userValues.isEmailValidated) {
                                        $("#mobile-otp-div small.update-notify").html('If you want to update your mobile number in NIC central repositary, please click on (<a href="#" onclick="updatemob()" class="updatemob">Update Mobile</a>)')
                                    }
                                    if (data.userValues.loginCaptchaAuthenticated === true) {
                                        if (data.userValues.authenticated === true) {
                                            $('#continue').attr("disabled", true);
                                            $("input[name='new_mobile']").val(data.userValues.mobile);
                                            $("input[name='country_code_val']").val(data.userValues.countryCode);
                                            $("input[name='isNewUser']").val(data.userValues.isNewUser);
                                            $("input[name='isEmailValidated']").val(data.userValues.isEmailValidated);
                                            $("input[name='isIsNicEmployee']").val(data.userValues.isNICEmployee);
                                            var startMobile = data.userValues.mobile.substring(0, 3);
                                            var endMobile = data.userValues.mobile.substring(10, 13);
                                            var name = email.substring(0, email.lastIndexOf("@"));
                                            var domain = email.substring(email.lastIndexOf("@") + 1)
                                            var startName = name.substring(0, 3);
                                            $("input[name='mobileOtpActive']").val(data.userValues.mobileOtpActive);
                                            $("input[name='emailOtpActive']").val(data.userValues.emailOtpActive);
                                            if (data.userValues.mobileOtpActive)
                                            {
                                                $('#mobileOtp').attr('placeholder', 'Please use previous OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                $('#emailOtp').attr('placeholder', 'Please use previous OTP sent on ' + startName + '*****@' + domain);
                                                $('#mobile_info').html('Please use previous OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                $('#email_info').html('Please use previous OTP sent on ' + startName + '*****@' + domain);
                                            } else {
                                                $('#mobileOtp').attr('placeholder', 'Please Enter OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                $('#emailOtp').attr('placeholder', 'Please Enter OTP sent on ' + startName + '*****@' + domain);
                                                $('#mobile_info').html('Please Enter OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                $('#email_info').html('Please Enter OTP sent on ' + startName + '*****@' + domain);
                                            }
                                            $('#email_error').html("");
                                            // show otp as the user is new
                                            $('#otp-tab').removeClass("display-hide");
                                            $('#signup-tab').addClass("display-hide");
                                            $('#email-otp-div').addClass("display-hide");
                                            $('#or-div').addClass("display-hide");
                                            $('#r_mobile_div').addClass("col-md-12");
                                            $('#r_email_div').removeClass("display-hide");
                                            $('#mobileOtp').focus();
                                            $('#resend_mobile').removeClass("display-hide");
                                        } else {
                                            // show password field
                                            $('#mobile-div').addClass("display-hide");
                                            $('#password-div').removeClass("display-hide");
                                            $('#password_error').text("Please enter correct password");
                                            $('#captcha1').val("");
                                            $('#password').val("");
                                            $('#password').focus();
                                            $('#captcha_div').removeClass("display-hide");
                                            $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                            return false;
                                        }
                                    } else {
                                        $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                        danger_msg("Please enter correct captcha code.")
                                        $('#captcha1').val("");
                                        // $('#password').val("");
                                        $('#logincaptchaerror').focus();
                                        return false;
                                    }
                                } else {
                                    if (data.errors.email === 'empty')
                                        $('#email_error').html("Please Enter Your Email Address");
                                    else
                                        $('#email_error').html("Please Enter Your Email in correct format.");
                                    return false;
                                }
                            },
                            error: function () {
                                $('.loader').hide();
                                console.log('error');
                                return false;
                            }
                        });
                        $('#email_error').html("");
                    }
                    $('#logincaptchaerror').html("");
                } else if ((password === null || password === "") && (mobile !== null && mobile !== "") && mobile.match(mobileregn)) {
                    if (!mobile.match(mobileregn)) {
                        $('.loader').hide();
                        $('#mobile-div').removeClass("display-hide");
                        $('#password-div').addClass("display-hide");
                        $('#mobile_error').text("Please enter mobile number in allowed format");
                        $('#mobile').focus();
                        return false;
                    } else {
                        var isNewUser = $("input[name='isNewUser']").val();
                        var isEmailValidated = $("input[name='isEmailValidated']").val();
                        var dataObj = {userValues: {email: email, mobile: mobile, countryCode: country_code, isEmailValidated: isEmailValidated, isNewUser: isNewUser, loginCaptcha: login_captcha}};
                        var data1 = JSON.stringify(dataObj);
                        $.ajax({
                            type: "POST",
                            url: "checkldapmobile",
                            data: data1,
                            datatype: JSON,
                            contentType: 'application/json; charset=utf-8',
                            success: function (data) {
                                $('.loader').hide();
                                $('#continue').attr("disabled", true);
                                console.log("data.userValues.isMobileInLdap" + data.userValues.isMobileInLdap)

                                if (data.userValues.emailCountAgainstMobile > 0)

                                {
                                    $('#mobile_error').text("Please come through your government email id. There is already an email address " + data.userValues.emailAgainstMobileNumber + " associated with this mobile number");
                                } else {
                                    console.log("inside elseeeeeeeeeeeeeeeeeeee")
                                    if (data.userValues.loginCaptchaAuthenticated) {
                                        $.ajax({
                                            type: "POST",
                                            url: "OtpGenerate",
                                            data: data1,
                                            datatype: JSON,
                                            contentType: 'application/json; charset=utf-8',
                                            success: function (data) {
                                                var dataObj = {userValues: {email: email, mobile: mobile, countryCode: country_code, isEmailValidated: data.userValues.isEmailValidated, isNewUser: data.userValues.isNewUser, isMobileInLdap: data.userValues.isMobileInLdap, mobileOtpActive: data.userValues.mobileOtpActive, emailOtpActive: data.userValues.emailOtpActive}};
                                                var data1 = JSON.stringify(dataObj);
                                                $.ajax({
                                                    type: "POST",
                                                    url: "ValidateEmail",
                                                    data: data1,
                                                    datatype: JSON,
                                                    contentType: 'application/json; charset=utf-8',
                                                    success: function (data) {
                                                        $('.loader').hide();
                                                        console.log(data)
                                                        $("input[name='new_mobile']").val(data.userValues.mobile);
                                                        $("input[name='country_code_val']").val(data.userValues.countryCode);
                                                        $("input[name='isNewUser']").val(data.userValues.isNewUser);
                                                        $("input[name='isEmailValidated']").val(data.userValues.isEmailValidated);
                                                        $("input[name='isIsNicEmployee']").val(data.userValues.isNICEmployee);

                                                        var showMobile = country_code + data.userValues.mobile
                                                        var startMobile = showMobile.substring(0, 3);
                                                        var endMobile = showMobile.substring(10, 13);
                                                        var name = email.substring(0, email.lastIndexOf("@"));
                                                        var domain = email.substring(email.lastIndexOf("@") + 1)
                                                        var startName = name.substring(0, 3);
                                                        $("input[name='mobileOtpActive']").val(data.userValues.mobileOtpActive);
                                                        $("input[name='emailOtpActive']").val(data.userValues.emailOtpActive);
                                                        if (data.userValues.mobileOtpActive)
                                                        {
                                                            $('#mobileOtp').attr('placeholder', 'Please use previous OTP which has been sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                            $('#emailOtp').attr('placeholder', 'Please use previous OTP which has been sent on ' + startName + '*****@' + domain);
                                                            $('#mobile_info').html('Please use previous OTP which has been sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                            $('#email_info').html('Please use previous OTP which has been sent on ' + startName + '*****@' + domain);
                                                        } else {
                                                            $('#mobileOtp').attr('placeholder', 'Please Enter OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);

                                                            $('#emailOtp').attr('placeholder', 'Please Enter OTP sent on ' + startName + '*****@' + domain);
                                                            $('#mobile_info').html('Please Enter OTP sent on ' + startMobile + 'XXXXXXX' + endMobile);
                                                            $('#email_info').html('Please Enter OTP sent on ' + startName + '*****@' + domain);
                                                        }
                                                        $('#email_error').html("");
                                                        // show otp as the user is new
                                                        $('#otp-tab').removeClass("display-hide");
                                                        $('#signup-tab').addClass("display-hide");
                                                        $('#email-otp-div').removeClass("display-hide");
                                                        $("input[name='new_email']").val(email);
                                                        $("input[name='new_mobile']").val(mobile);
                                                        $('#country_code_val').val("+91");
                                                        $('#or-div').removeClass("display-hide");
                                                        $('#r_mobile_div').addClass("col-md-12");
                                                        $('#r_email_div').removeClass("display-hide");
                                                        $('#mobileOtp').focus();
                                                        $('#resend_mobile').removeClass("display-hide");
                                                        $('#resend_email').removeClass("display-hide");

                                                    },
                                                    error: function () {
                                                        console.log('error');
                                                        $('.loader').hide();
                                                    }
                                                });
                                            }, error: function () {
                                                console.log('error');
                                                $('.loader').hide();
                                            }
                                        });
                                        $('#logincaptchaerror').text("");
                                    } else {
                                        $('.loader').hide();
                                        $('#captcha1').text("");
                                        //$('#password').text("");
                                        $('#captcha2').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                        danger_msg("Please enter correct captcha code.");
                                        $('#logincaptchaerror').focus();
                                    }
                                }
                            }, error: function () {
                                $('.loader').hide();
                                console.log('error');
                            }
                        });
                    }
                }
                $('.loader').hide();
            } else {
                $('.loader').hide();
                $('#email_error').html("Please Enter Your Email Address in correct format");
            }
        } else {
            $('.loader').hide();
            $('#email_error').html("Please Enter Your Email Address");
        }
    });
    $('#submit-otp').click(function (e) {

        $('.loader').show();
        e.preventDefault();
        var emailregn = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var emailOtp = $('#emailOtp').val();
        var mobileOtp = $('#mobileOtp').val();
        var new_email = $('#submit-pwd #email').val();
        var new_mobile = $("input[name='new_mobile']").val();
        var country_code = $("input[name='country_code_val']").val();
        var whichform = $('#whichform').val();
        var isNewUser = $("input[name='isNewUser']").val();
        var isEmailValidated = $("input[name='isEmailValidated']").val();
        var mobileOtpActive = $("input[name='mobileOtpActive']").val();
        var emailOtpActive = $("input[name='emailOtpActive']").val();
        var isIsNicEmployee = $("input[name='isIsNicEmployee']").val();
        console.log("mobileOtpActive on otp submit:::::" + mobileOtpActive)
        console.log("emailOtpActive on otp submit:::::" + emailOtpActive)
        var dataObj = {userValues: {email: new_email, mobile: new_mobile, countryCode: country_code, emailOtp: emailOtp, mobileOtp: mobileOtp, isEmailValidated: isEmailValidated, isNewUser: isNewUser, mobileOtpActive: mobileOtpActive, emailOtpActive: emailOtpActive}};
        var data1 = JSON.stringify(dataObj);
//        $('#submit-otp').attr("disabled", true);
//        $('#resend_mobile').attr('disabled', true);
//        $('#resend_email').attr('disabled', true);
        if($('#mobileOtp').val() == "" && $('#emailOtp').val() == ""){
             $('.loader').hide();
              danger_msg('Please insert otp in Email or Mobile number section');
              $('#mobile_resend').hide();
              $('#mobile_resend2').hide();
//             $('#enter_details').html('Please insert otp in Email or Mobile number section')
        }
        else{
            $('#enter_details').hide();
        $.ajax({
            type: "POST",
            url: "verifyOtp",
            data: data1,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                $('#submit-otp').attr("disabled", false);
                $('#resend_mobile').attr('disabled', false);
                $('#resend_email').attr('disabled', false);

                //$('.loader').removeClass("display-hide")
                $('.loader').hide();



                if (data.userValues.blocked === true)
                {
                    $('#mobile_block').html('You have been Blocked due to multiple wrong attempts');
                    $('#mobile_info').hide();
                    $('#email_info').hide();
                    $('#mobileOtp').prop('readonly', true);
                    $('#emailOtp').prop('readonly', true);
                    $('#resend_mobile').attr('disabled', true);
                    $('#resend_email').attr('disabled', true);
                    $('#submit-otp').attr('disabled', true);
                }
                if (data.userValues.verifiedOtp == "block")
                {

                    danger_msg("Your are blocked for half an hour");
                } else {
                    if (data.userValues.authenticated) {
                        if (data.userValues.role) { // check roles, if user , show profile else show admin pages
                            var isNewUser = data.userValues.isNewUser;
                            var role = data.userValues.role;
                            if (role.indexOf("admin") > -1 || role.indexOf("ca") > -1 || role.indexOf("co") > -1 || role.indexOf("sup") > -1) {
                                if (data.userValues.isEmailValidated)
                                {
                                    console.log('validated');
                                    if (role.indexOf("admin") > -1) {
                                        window.location.href = "showLinkData?adminValue=admin";
                                    } else if (role.indexOf("co") > -1) {
                                        window.location.href = "showLinkData?adminValue=co"
                                    } else if (role.indexOf("ca") > -1) {
                                        window.location.href = "showLinkData?adminValue=ca"
                                    } else if (role.indexOf("sup") > -1) {
                                        window.location.href = "showLinkData?adminValue=sup"
                                    }
                                } else {
                                    console.log('non validated');
                                    if (data.userValues.verifiedOtp == "both" && data.userValues.isEmailValidated == false)
                                    {
                                        if (role.indexOf("admin") > -1) {
                                            window.location.href = "showLinkData?adminValue=admin";
                                        } else if (role.indexOf("co") > -1) {
                                            window.location.href = "showLinkData?adminValue=co"
                                        } else if (role.indexOf("ca") > -1) {
                                            window.location.href = "showLinkData?adminValue=ca"
                                        } else if (role.indexOf("sup") > -1) {
                                            window.location.href = "showLinkData?adminValue=sup"
                                        }
                                    } else {
                                        window.location.href = "profile";
                                    }
                                }
                            } else if (role.indexOf("user") > -1 && !isNewUser) {
                                window.location.href = "showUserData";
                            } else if (isNewUser && role.indexOf("user") > -1) {
                                window.location.href = "profile";
                            } else {
                                window.location.href = "error.jsp";
                            }
                        }
                    } else {
                        var isNewUser = data.userValues.isNewUser;
                        if (!data.userValues.isEmailValidated && !isNewUser)
                        {
                            if (mobileOtp == "" && emailOtp == "")
                            {
                                danger_msg("Please Enter otp to proceed")
                            } else if (mobileOtp !== "" && emailOtp !== "")
                            {
                                danger_msg("Please Enter correct otp received on mobile/email")
                            } else if (mobileOtp == "" && emailOtp !== "")
                            {
                                danger_msg("Please Enter otp received on mobile to proceed");
                                
                            } else if (mobileOtp !== "" && emailOtp == "")
                            {
                                danger_msg("Please Enter otp received on email to proceed");
                            }
                        } else if (!data.userValues.isEmailValidated && isNewUser)
                        {
                            if (mobileOtp != "" && emailOtp === "")
                            {
                                danger_msg("Please Enter correct otp received on mobile");
                                $('#mobile_resend').hide();
                                $('#mobile_resend2').hide();
                            } else if (emailOtp != "" && mobileOtp === "")
                            {
                                danger_msg("Please Enter correct otp received on email");
                                $('#email_otp_error').hide();
                                $('#email_resend').hide();
                            } else if (mobileOtp != "" && emailOtp != "")
                            {
                                danger_msg("Please Enter correct otps received on email and mobile");
                                $('#mobile_resend').hide();
                                $('#mobile_resend2').hide();
                                $('#email_otp_error').hide();
                                $('#email_resend').hide();
                            }
                        } else {
                            if (mobileOtp == "")
                            {
                                danger_msg("Please Enter otp received on mobile to proceed");
                            } else {
                                danger_msg("Please Enter correct otp received on mobile");
                            }
                        }
                    }
                }
            },
            error: function () {
                $('.loader').hide();
                console.log('error');
            }
        });
         }
    });
// resend mobile otp
    $('#resend_mobile').click(function (e) {
        var isNewUser = $("input[name='isNewUser']").val();
        var email = $(' #email').val();
        var password = $('#password').val();
        var newmobile = $('#mobile').val();
        //alert(newmobile);
        //var newcode = $('#update-mobile #newMobilecode').val();
        var dataObj = {userValues: {new_mobile: newmobile, email: email, password: password, isNewUser: isNewUser, otp_generate_or_resend: "otpResend"}};
        var data1 = JSON.stringify(dataObj);
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "Otpgen_newmobile",
            data: data1,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data)
            {
                $('.loader').hide();
                console.log("dataaaaaaaaaaaa" + data.returnString)
                console.log("return output 2222222222if click new mobile" + data.userValues.returnString)


                $("input[name='mobile_resend']").val(data.userValues.returnString);
                $("input[name='mobile_resend']").val(data.userValues.returnString);
                if (data.userValues.returnString === 'ResendOtp')
                {
                    $('#mobile_resend').show();
                    $('#mobile_resend').html('Your OTP has been resent');
                    $('#mobile_info').hide();
                }

                if (data.userValues.noOfMobileOtpAttempts === 0) {
                    $('#mobile_resend2').text('You have limit of ' + data.userValues.noOfMobileOtpAttempts + ' to request for otp.')

                    $('#mobile_resend').hide();
                    $('#mobile_info').hide();
                } else {
                    $('#mobile_resend2').show();
                    $('#mobile_resend2').text('You have limit of ' + data.userValues.noOfMobileOtpAttempts + ' to request for otp.')
                }
                if (data.userValues.returnString === "ResendOtpLimitExceed")
                {
                    $('#mobile_resend1').html('Your OTP limit has been exceeded');
                    $('#mobile_resend').hide();
                    $('#mobile_resend2').hide();
                    $('#mobile_info').hide();
                }



//                if (data.userValues.authenticated === true) {
//                    if (data.userValues.returnString == "moberr")
//                    {
//                        $("#update-mobile #new-code").addClass("display-hide");
//                        $("#update-mobile #succ_msg").hide();
//                        $('#update-mobile #moberr').html("Please Enter Mobile in correct format with country code e.g +91XXXXXXXXXX");
//                    } else if (data.userValues.returnString.indexOf("@") > -1)
//                    {
//                        $("#update-mobile #new-code").addClass("display-hide");
//                        $("#update-mobile #succ_msg").hide();
//                        $('#update-mobile #moberr').text("There is already an email address " + data.userValues.returnString + " associated with this mobile number");
//                        // $('#update-mobile #moberr').html("There is already an email address associated with this mobile number so the same mobile number can't be updated");
//                    } else {
//                        $("#update-mobile #new-code").removeClass("display-hide");
//                        $("#update-mobile #succ_msg").show();
//                        $("#update-mobile #succ_msg").html("OTP Sent to entered Mobile Number.")
//                        $('#update-mobile #moberr').html("");
//
//                    }
//                }
            }, error: function ()
            {
                $('.loader').hide();
                console.log("error in old code verify")
            }
        })
    });
// resend email otp
    $('#resend_email').click(function (e) {
        $('.loader').show();
        e.preventDefault();
        var new_email = $('#submit-pwd #email').val();
        var new_mobile = $("input[name='new_mobile']").val();
        var country_code = $("input[name='country_code_val']").val();
        var dataObj = {userValues: {email: new_email, mobile: new_mobile, countryCode: country_code}};
        var data1 = JSON.stringify(dataObj);
        $.ajax({
            type: "POST",
            url: "resendEmailOtp",
            data: data1,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {
                $('.loader').hide();
                if (data.userValues.resendemail) {
                    $('#email_otp_error').show();
                    $('#email_resend').show();
                    $('#email_otp_error').html("no of attempt left: "+data.userValues.noOfEmailAttempts)
                    $('#email_resend').html("OTP Resent Successfully. Validity of the OTP is 30 mins");
                    $('email_resend_exh').hide();
                    $('#email_info').hide();
                    // success_msg("OTP Resent Successfully. Validity of the OTP is 30 mins");
                } else {
                    
                    $('#email_resend_exh').html("You have exhausted your resend attempts.");
                    $('#email_resend').hide();
                    $('#email_info').hide();
                     $('#email_otp_error').hide();
                    // danger_msg("You have exhausted your resend attempts.");
                    // alert("You have exhausted your resend attempts.");
                }
            },
            error: function () {
                $('.loader').hide();
                //alert("Request Again for OTP or please Try after some time");
            }
        });
    });
    $(".form-selection a").click(function (e) {
        $('.loader').show();
        e.preventDefault();
        var i = $(this).data("value");

//alert(i);

        $.ajax({
            type: "POST",
            url: "checkProfile",
            data: {whichform: i},
            datatype: 'JSON',
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                if (jsonvalue.view === 'profile') {
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>You need to update your profile first. <br/>You will be automatically redirected to your profile page.</p>",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                    setTimeout(function () {
                        window.location.href = jsonvalue.view;
                    }, 100000000000);
                } else if (i === "cloud" || i === "gov" || i === "audit" || i === "security" || i === "waf" || i === "sampark" || i === "video") {
                    window.open(jsonvalue.view, '_blank');
                } else if (i === "utm")
                {
                    var view = jsonvalue.view;
                    $.ajax({
                        type: "POST",
                        url: "fetchFirewallUsers",
                        datatype: 'html',
                        success: function (data)
                        {
                            var myJSON = JSON.stringify(data);
                            var jsonvalue = JSON.parse(myJSON);
                            if (jsonvalue.allowed_user == true)
                            {
                                window.location.href = view;
                                // window.open(view, '_blank');
                            } else {
                                bootbox.dialog({
                                    message: "<p style='text-align: center; font-size: 18px;'>You are not a valid user for Central UTM service. <br/></p>",
                                    title: "",
                                    buttons: {
                                        cancel: {
                                            label: "OK",
                                            className: 'btn-info'
                                        }
                                    }
                                });
                            }
                        }, error: function ()
                        {
                            console.log("error")
                        }
                    })
                } else {
                    if (jsonvalue.view === null)
                    {


                        if (jsonvalue.returnString === "nongov")
                        {
                            $('.loader').hide();
                            bootbox.dialog({
                                message: "<p style='font-size: 16px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li><li> Sms Service</li> <li>VPN Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                                        + "<p style='font-size: 14px;'>To register for other services, please log in with your government email service(NIC) email address.</p>",
                                title: "",
                                buttons: {
                                    cancel: {
                                        label: "OK",
                                        className: 'btn-info'
                                    }
                                }
                            });
                        }
                        if (jsonvalue.returnString === "profileNotFound")
                        {
                            $('.loader').hide();
                            bootbox.dialog({
                                message: "<p style='font-size: 15px;'> You need to update your profile first.</p>",
                                buttons: {
                                    cancel: {
                                        label: "OK",
                                        className: 'btn-info'
                                    }
                                }
                            });
                        } else if (jsonvalue.returnString === "updatemobile")
                        {

                            $('.loader').hide();
                            if (i == "mobile")
                            {
                                window.location.href = "Mobile_registration"

                            } else {
                                bootbox.dialog({
                                    message: "<p style='font-size: 15px;'>  Since, Your mobile number has been changed, kindly logout and login again to submit the requests.</p>",
                                    buttons: {
                                        cancel: {
                                            label: "OK",
                                            className: 'btn-info'
                                        }
                                    }
                                });
                            }




                        }
                    } else {

                        window.location.href = jsonvalue.view;
                    }
                }

            },
            error: function () {
                console.log('error');
                $('.loader').hide();
            }
        });

    });


//    $(document).on('blur', '#newmobile', function (e) {
//        $('.loader').show();
//        e.preventDefault();
//        var new_mobile = $('#newmobile').val();
//        var email = $('#submit-pwd #email').val();
//        var dataObj = {userValues: {new_mobile: new_mobile, email: email}};
//        var data1 = JSON.stringify(dataObj);
//        $.ajax({
//            type: "POST",
//            url: "Otpgen_newmobile",
//            data: data1,
//            datatype: JSON,
//            contentType: 'application/json; charset=utf-8',
//            success: function (data)
//            {
//                $('.loader').hide();
//
//                $('#moberr').html("");
//                console.log("return output if click new mobile" + data.returnString)
//                //console.log("return output 2222222222if click new mobile" + data.userValues.returnString)
//                if (data.userValues.returnString == "moberr")
//                {
//                    $("#new-code").addClass("display-hide");
//                    $("#succ_msg").hide();
//                    $('#moberr').html("Please Enter Mobile in correct format with country code e.g +91XXXXXXXXXX");
//                } else if (data.userValues.returnString == "mobileInLdap")
//                {
//                    $("#new-code").addClass("display-hide");
//                    $("#succ_msg").hide();
//                    $('#moberr').html("There is already an email address associated with this mobile number so the same mobile number can't be updated");
//                } else if (data.userValues.returnString == "same")
//                {
//                    $('#moberr').html("This is the same number you are updating");
//                    $("#new-code").addClass("display-hide");
//                    $("#succ_msg").hide();
//                    $('#succ_err').html("");
//                    $("#newcode").val('');
//                } else {
//                    $("#new-code").removeClass("display-hide");
//                    $("#succ_msg").show();
//                    $("#succ_msg").html("OTP Sent to entered Mobile Number.")
//                }
//            }, error: function ()
//            {
//                $('.loader').hide();
//                console.log("error in old code verify")
//            }
//        })
//    })
//    $(document).on('blur', '#newMobilecode', function (e) {
//        $('.loader').hide();
//        e.preventDefault();
//        var email = $('#submit-pwd #email').val();
//        var mobile = $("input[name='new_mobile']").val();
//        console.log("mobile on new mobile submit" + mobile)
//        console.log("email on new mobile submit" + email)
//        var newcode = $('#newMobilecode').val();
//        var new_mobile = $('#newmobile').val();
//        var isNewUser = $("input[name='isNewUser']").val();
//        var dataObj = {userValues: {new_mobile: new_mobile, newcode: newcode, isNewUser: isNewUser, email: email, mobile: mobile}};
//        var data1 = JSON.stringify(dataObj);
//        $.ajax({
//            type: "POST",
//            url: "verify_newmobile_code",
//            data: data1,
//            datatype: JSON,
//            contentType: 'application/json; charset=utf-8',
//            success: function (data)
//            {
//                $('.loader').hide();
//                if (data.userValues.returnString == "new_code_err")
//                {
//                    $('#file_cert_err').html("Please enter new otp code in correct format");
//                } else if (data.userValues.returnString == "newOtpNotMatch")
//                {
//                    $('#file_cert_err').html("Please enter correct new otp");
//                } else if (data.userValues.returnString == "newuser")
//                {
//                    window.location.href = "profile";
//                } else if (data.userValues.returnString == "olduser")
//                {
//                    window.location.href = "Mobile_registration";
//                } else if (data.userValues.returnString == "mobilesamehod")
//                {
//                    $('#moberr').html("You can not update your mobile number same as of your reporting officer");
//                    $("#newcode").val('');
//                } else if (data.userValues.returnString == "mobilesameus")
//                {
//                    $('#moberr').html("You can not update your mobile number same as of your Under Secratery");
//                    $("#newcode").val('');
//                }
//            }, error: function ()
//            {
//                $('.loader').hide();
//                console.log("error in new code ")
//            }
//        })
//    });

    function danger_msg(txt) {
        $(".err_msg_pop").addClass("alert alert-danger");
        $(".err_msg_pop").removeClass("alert alert-success");
        $(".err_msg_pop").html(txt);
        $('.err_msg_pop').delay(8000).show().fadeOut('slow');
    }
    function success_msg(txt) {
        $(".err_msg_pop").addClass("alert alert-success");
        $(".err_msg_pop").removeClass("alert alert-danger");
        $(".err_msg_pop").html(txt);
        $('.err_msg_pop').delay(8000).show().fadeOut('slow');
    }
});
function updatemob() {
    $('#myModallogin').modal('hide');
    $('#updateMobile').modal('show', {backdrop: 'static'});
}
//
//window.addEventListener("online", function(){
//        alert("hahahaha");
//    });
//    window.addEventListener("offline", function(){
//        alert("hahahahaha");
//    });
//    let conn = navigator.connection;
//    conn.addEventListener("change", function(){
//        alert("hahahaha");
//    })
//    
//    