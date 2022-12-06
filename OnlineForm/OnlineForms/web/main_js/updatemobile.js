var passwordDivEnabled = false;
var passValidation = false;
var captchaValidation = false;
var mobValidation = false;


$('#update-mobile').submit(function (e) {
    $('.err_msg').html('');
    $('.loader').show();
    e.preventDefault();
    var email = $('#update-mobile #email').val();
    var password = $('#update-mobile #password').val();
    var prefixMob = $('#update-mobile #country_code_update_mob').val();
    var newmobile = $('#update-mobile #newmobile').val();
    var newcode = $('#update-mobile #newMobilecode').val();



    var emailregn = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if ((email !== null && email !== "")) {
        if (email.match(emailregn)) {
            if ((password === null || password === "") && !passwordDivEnabled) {
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
                        $('.loader').hide();
                        console.log(data);
                        if (!data.errors) {
                            $('#update-mobile #continue').attr("disabled", false);
                            //alert(data.userValues.isEmailValidated)


                            if (data.userValues.isEmailValidated) {

                                if (data.userValues.existMobileRequest === "true") {
                                    $('#update-mobile #email_error').html("Mobile number can be updated just once per day.");
                                } else {
                                    // alert(data.userValues.blocked);
                                    if (data.userValues.blocked) {
                                        $('#update-mobile #email_error').html("You have been blocked for making multiple failed attemtps. Please try after some time.");
                                    } else {
                                        if ((email == "support@nic.in" || email == "support@gov.in" || email == "support@nkn.in" || email == "smssupport@nic.in" || email == "vpnsupport@nic.in") && (mobile == null || mobile == "")) {
                                            $('#captcha_div').addClass("display-hide");
                                        }

                                        $('#update-mobile #email').prop('readonly', true);
                                        $('#update-mobile #tnc').prop('disabled', true);
                                        $('#update-mobile #password-div').removeClass('display-hide');
                                        $("#continueId").prop('disabled', true);
                                        $('#update-mobile #captcha_div').removeClass('display-hide');
                                        passwordDivEnabled = true;
                                        $("#emailLbl").html('Entered Email Address')
                                        $('#update-mobile #password').focus();
                                        $("#update-mobile input[name='isNewUser']").val(data.userValues.isNewUser);
                                        $("#update-mobile input[name='isEmailValidated']").val(data.userValues.isEmailValidated);
                                    }


                                }
                            } else {

                                // $('#update-mobile #email_error').html("Please come with NIC/Gov email address.");

                                $('#update-mobile #email_error').html("Please enter a valid Email Address as mentioned above");
                            }



                        } else {
                            if (data.errors.email === 'empty')
                                $('#update-mobile #email_error').html("Please Enter Your NIC/Gov Address");
                            else
                                $('#update-mobile #email_error').html("Please Enter Your Email in correct format.");
                            return false;
                        }
                    }, complete() {
                        $('.loader').hide();
                    }
                });
            } else if (((password !== null && password !== "") && (newmobile == null || newmobile == "")) || passwordDivEnabled) {

                var isNewUser = $("input[name='isNewUser']").val();
                passwordValidation();
                var captcha = $("#captcha11").val();
//                alert("captcha="+captcha);
                var dataObj = {userValues: {email: email, password: password, isNewUser: isNewUser, loginCaptcha: captcha}};
                var data1 = JSON.stringify(dataObj);
                if (passValidation == false && captchaValidation == false) {
                    $.ajax({
                        type: "POST",
                        url: "AuthPwd",
                        data: data1,
                        datatype: JSON,
                        contentType: 'application/json; charset=utf-8',
                        success: function (data) {

                            $('.loader').hide();
                            if (!data.errors) {


                                if (data.userValues.blocked) {
                                    $('#update-mobile #password_error').html("You have been blocked for making multiple failed attemtps. Please try after some time.");
                                } else if (data.userValues.loginCaptchaAuthenticated === true) {

                                    if (data.userValues.authenticated === true) {
                                        $('#updateMobile #captcha_div').addClass("display-hide");
                                        $('#update-mobile #password-div').addClass('display-hide');
                                        // alert("data.userValues.mobile"+data.userValues.mobile)
                                        $('#update-mobile #continue').attr("disabled", true);
                                        $("input[name='new_mobile']").val(data.userValues.mobile);
                                        $("input[name='country_code_val']").val(data.userValues.countryCode);
                                        $("input[name='isNewUser']").val(data.userValues.isNewUser);
                                        $("input[name='isEmailValidated']").val(data.userValues.isEmailValidated);
                                        $("input[name='isIsNicEmployee']").val(data.userValues.isNICEmployee);
                                        $('#update-mobile #email_error').val(data.userValues.mobile);
                                        $('#update-mobile #old-mobile').removeClass("display-hide");
                                        var startMobile = data.userValues.mobile.substring(0, 3);
                                        var endMobile = data.userValues.mobile.substring(10, 13);
                                        $('#update-mobile #mobile').html(startMobile + 'XXXXXXX' + endMobile)
                                        // $('#update-mobile #mobile').prop("readonly",true);
                                        $('#update-mobile #new-mobile').removeClass("display-hide");
                                        $('#update-mobile #signup-tab').addClass("display-hide");
                                        $('#update-mobile #password_error').text("");
                                        passwordDivEnabled = false;
                                        $('#update-mobile #mobile-div').addClass("display-hide");
                                        $('#update-mobile #password').focus();
                                        $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));

                                    } else {
                                        $('#update-mobile #mobile-div').addClass("display-hide");
                                        $('#update-mobile #password-div').removeClass("display-hide");
                                        $('#update-mobile #password_error').text("Please enter correct password");
                                        $('#update-mobile #password').focus();
                                        $('#update-mobile #logincaptchaerror11').html("");
                                        $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                    }
                                } else {
                                    if (data.userValues.authenticated === false) {
                                        $('#update-mobile #mobile-div').addClass("display-hide");
                                        $('#update-mobile #password-div').removeClass("display-hide");
                                        $('#update-mobile #password_error').text("Please enter correct password");
                                        $('#update-mobile #password').focus();
                                        $('#update-mobile #logincaptchaerror11').html("");
                                        $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                    }


                                    $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                    //danger_msg("Please enter correct captcha code.");
                                    $('#update-mobile #password_error').text("");
                                    $('#update-mobile #logincaptchaerror11').html("Please enter correct captcha");
                                    $('#update-mobile #logincaptchaerror11').focus();
                                    return false;
                                }
                            } else {
                                if (data.errors.email === 'empty')
                                    $('#update-mobile #email_error').html("Please Enter Your Email Address");
                                else
                                    $('#update-mobile #email_error').html("Please Enter Your Email in correct format.");
                                return false;
                            }
                        },
                        error: function () {
                            $('.loader').hide();
                            console.log('error');
                            return false;
                        }, complete() {
                            $('.loader').hide();
                        }
                    });
                } else {
                    $('.loader').hide();
                    return false;
                }

                $('#update-mobile #email_error').html("");

                $('#update-mobile #logincaptchaerror').html("");
            } else if ((password !== null && password !== "") && (newmobile !== null && newmobile !== "") && (newcode == null || newcode == "")) {
                var isNewUser = $("input[name='isNewUser']").val();
                var resendVal = $('#resend_submit_val').val();

                //newmobile = prefixMob + newmobile;
                var dataObj = {userValues: {new_mobile: newmobile,country_code: "+91", email: email, password: password, isNewUser: isNewUser, otp_generate_or_resend: resendVal}};
                var data1 = JSON.stringify(dataObj);
                $.ajax({
                    type: "POST",
                    url: "Otpgen_newmobile",
                    data: data1,
                    datatype: JSON,
                    contentType: 'application/json; charset=utf-8',
                    success: function (data)
                    {

                        $('.loader').hide();
                        console.log("dataaaaaaaaaaaa" + data.returnString + "otp_generate_or_resend" + resendVal)
                        //alert("::::::::"+data.userValues.returnString);
                        //alert("::::::::"+email);
                        // alert("::::::::"+newmobile);
                        console.log("return output 2222222222if click new mobile" + data.userValues.returnString)

                        if (data.userValues.authenticated === true) {
                            $('#resend_submit_val').val("otpResend");

                            if (data.userValues.returnString == "moberr1")
                            {
                                $("#update-mobile #new-code").addClass("display-hide");
                                $("#update-mobile #succ_msg").hide();
                                $('#update-mobile #moberr').html("Please Enter Mobile with country code");

                            } else if (data.userValues.returnString == "mobileInLdap")
                            {
                                $("#update-mobile #new-code").addClass("display-hide");
                                $("#update-mobile #succ_msg").hide();
                                $('#update-mobile #moberr').html("Your email address (" + email + ") is already mapped with this number.");

                            } else if (data.userValues.returnString == "moberr")
                            {
                                $("#update-mobile #new-code").addClass("display-hide");
                                $("#update-mobile #succ_msg").hide();
                                $('#update-mobile #moberr').html("Please Enter Mobile in correct format with country code e.g +91XXXXXXXXXX");

                            } else if (data.userValues.returnString.indexOf("@") > -1)
                            {
                                $("#update-mobile #new-code").addClass("display-hide");
                                $("#update-mobile #succ_msg").hide();
                                $('#update-mobile #moberr').text("There are already 3 or more email addresses (" + data.userValues.returnString + ") associated with this mobile number");
                                // $('#update-mobile #moberr').html("There is already an email address associated with this mobile number so the same mobile number can't be updated");
                            } else if (data.userValues.returnString == "previousOtp") {
                                $("#update-mobile #new-code").removeClass("display-hide");
                                $("#update-mobile #succ_msg").show();
                                $("#update-mobile #succ_msg").html("Please use previous otp,It is valid for 30 mins.")
                                $('#update-mobile #moberr').html("");


                            } else if (data.userValues.returnString == "ResendOtp") {
                                $("#update-mobile #new-code").removeClass("display-hide");
                                $("#update-mobile #succ_msg").show();
                                $("#update-mobile #succ_msg").html("otp resent successfully")
                                $('#update-mobile #moberr').html("");
                                $('#update-mobile #newmobile').prop('readonly', true);

                            } else if (data.userValues.returnString == "ResendOtpLimitExceed") {
                                $("#update-mobile #new-code").removeClass("display-hide");
                                $("#update-mobile #succ_msg").show();
                                $("#update-mobile #succ_msg").html("Otp limit has been exceed please use previous otp it is valid for 30 mins")
                                $('#update-mobile #moberr').html("");
                                $('#update-mobile #newmobile').prop('readonly', true);

                            } else if (data.userValues.returnString == "newOtp") {
                                $("#update-mobile #new-code").removeClass("display-hide");
                                $("#update-mobile #succ_msg").show();
                                $("#update-mobile #succ_err").html("Please enter otp")
                                $('#update-mobile #moberr').html("");


                            }




                        }
                    }, error: function ()
                    {
                        $('.loader').hide();
                        console.log("error in old code verify")
                    }, complete() {
                        $('.loader').hide();
                    }
                })

            } else if ((password !== null && password !== "") && (newmobile !== null && newmobile !== "") && (newcode !== null && newcode !== "")) {

                newmobile = "+91"+newmobile;
                var mobile = $("input[name='new_mobile']").val();
                var isNewUser = $("input[name='isNewUser']").val();
                var dataObj = {userValues: {new_mobile: newmobile, newcode: newcode, isNewUser: isNewUser, email: email, mobile: mobile, password: password}};
                var data1 = JSON.stringify(dataObj)
                $.ajax({
                    type: "POST",
                    url: "verify_newmobile_code",
                    data: data1,
                    datatype: JSON,
                    contentType: 'application/json; charset=utf-8',
                    success: function (data)
                    {
                        $('.loader').hide();

                        if (data.userValues.authenticated === true) {
                            if (data.userValues.returnString == "new_code_err")
                            {
                                $('#update-mobile #file_cert_err').html("Please enter new otp code in correct format");
                                $("#update-mobile #succ_msg").html("");
                            } else if (data.userValues.returnString == "newOtpNotMatch")
                            {
                                $('#update-mobile #file_cert_err').html("Please enter correct new otp");
                                $("#update-mobile #succ_msg").html("");
                            } else if (data.userValues.returnString == "newuser")

                            {
                                window.location.href = "profile";
                            } else if (data.userValues.returnString == "olduser")
                            {
                               
                                $('#mobile_form1 #new_mobile').prop("readonly", true);
                                $('#mobile_form1 #new_mobile').val(newmobile);
                                window.location.href = "Mobile_registration";
                            } else if (data.userValues.returnString == "mobilesamehod")
                            {
                                $('#update-mobile #moberr').html("You can not update your mobile number same as of your reporting officer");
                                $("#update-mobile #newcode").val('');
                            } else if (data.userValues.returnString == "mobilesameus")
                            {
                                $('#update-mobile #moberr').html("You can not update your mobile number same as of your Under Secratery");
                                $("#update-mobile #newcode").val('');
                            }
                        }
                    }, error: function ()
                    {
                        $('.loader').hide();
                        console.log("error in new code ")
                    }, complete() {
                        $('.loader').hide();
                    }
                })
            }

        } else {
            $('.loader').hide();
            $('#update-mobile #email_error').html("Please Enter Your Email Address in correct format");
        }
    } else {
        $('.loader').hide();
        $('#update-mobile #email_error').html("Please Enter Your Email Address");
    }
});




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

function passwordValidation() {
    var passVal = $("#update-mobile #password").val();
    var captchaVal = $("#update-mobile #captcha11").val();
    if (passVal.length < 2) {
        $('#update-mobile #password_error').text("Please enter correct password");
        $('#update-mobile #password').addClass("is-invalid");
        passValidation = true;
    } else {
        $('#update-mobile #password_error').text("");
        $('#update-mobile #password').removeClass("is-invalid");
        passValidation = false;
    }

    if (captchaVal.length < 1) {
        $('#update-mobile #logincaptchaerror11').text("Captcha can not be blank");
        $('#update-mobile #captcha11').addClass("is-invalid");
        captchaValidation = true;
    } else {
        $('#update-mobile #logincaptchaerror11').text("");
        $('#update-mobile #captcha11').removeClass("is-invalid");
        captchaValidation = false;
    }
}

function mobileValidation() {
    var mobVal = $("#newmobile").val();
    if (mobVal.length < 10) {
        $('#update-mobile #moberr').text("Please enter correct Mobile Number");
        $('#update-mobile #newmobile').addClass("is-invalid");
        mobValidation = true;
    } else {
        $('#update-mobile #moberr').text("");
        $('#update-mobile #newmobile').removeClass("is-invalid");
        mobValidation = false;
    }
}

$("#update-mobile #password").keyup(function () {
    var passVal = $("#update-mobile #password").val();
    var captchaVal = $("#update-mobile #captcha11").val();
    if (passVal.length > 2 && captchaVal.length > 5) {
        $("#continueId").prop('disabled', false);
    } else {
        $("#continueId").prop('disabled', true);
    }
})

$("#update-mobile #captcha11").keyup(function () {
    var passVal = $("#update-mobile #password").val();
    var captchaVal = $("#update-mobile #captcha11").val();
    if (passVal.length > 2 && captchaVal.length > 5) {
        $("#continueId").prop('disabled', false);
    } else {
        $("#continueId").prop('disabled', true);
    }
})

$('#update-mobile #refresh').click(function () {
    $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
})