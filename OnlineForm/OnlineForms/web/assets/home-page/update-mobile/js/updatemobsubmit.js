/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var otpTxt = false;
var currentMobile = '';
var passwordDivEnabled = false;
var passValidation = false;
var captchaValidation = false;
var mobValidation = false;
$.fn.serializeObject = function ()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
function emailDetail() {

    $('.loader').show();
    $('.text-danger').html('');
    $('.text-success').html('');
    //e.preventDefault();
    var email = $('#update-mobile #email').val();
    var valid = true;
    var emailregn = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var data = JSON.stringify($('#update-mobile').serializeObject());
    if ((email !== null && email !== "")) {
        if (email.match(emailregn)) {

            $('#continue').attr("disabled", true);
            var dataObj = {userValues: {email: email}};
            var data1 = JSON.stringify(dataObj);
            $.ajax({
                type: "POST",
                url: "CheckEmail",
                data: data1,
                datatype: JSON,
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    // 
                    console.log(data);
                    if (!data.errors) {
                        $('#update-mobile #continue').attr("disabled", false);
                        if (data.userValues.isEmailValidated) {
                            if (data.userValues.userAuthenticatedForUpdateMobile !== undefined && data.userValues.userAuthenticatedForUpdateMobile != null && data.userValues.userAuthenticatedForUpdateMobile === "You_Are_NOT_Eligible") {
                                $('#update-mobile #email').addClass("is-invalid");
                                $('#update-mobile #email_error').html("You are not authorised to update mobile number,Please coordinate with your Delegated Admin/NIC Coordinator");
                                valid = false;
                            } else {

                                if (data.userValues.existMobileRequest !== undefined && data.userValues.existMobileRequest != null && data.userValues.existMobileRequest === "true") {
                                    $('#update-mobile #email').addClass("is-invalid");
                                    $('#update-mobile #email_error').html("You can update your mobile number once per calendar day. You have already updated it today. So, please try changing it tomorrow.");
                                    valid = false;
                                } else if (data.userValues.existMobileRequestByFlow !== undefined && data.userValues.existMobileRequestByFlow != null && data.userValues.existMobileRequestByFlow === "true") {
                                    $('#update-mobile #email').addClass("is-invalid");
                                    $('#update-mobile #email_error').html("You have already requested for updation of mobile number. Your request is still pending. Kindly wait to get it completed or rejected.");
                                    valid = false;
                                } else if (data.userValues.isexistInKavach == "true")
                                {
                                    $('#update-mobile #email').addClass("is-invalid");
                                    $('#update-mobile #email_error').html("You have already requested for updation of mobile number. Your request is still pending. Kindly wait to get it completed or rejected.");
                                    valid = false;
                                } else {
                                    if (data.userValues.blocked) {
                                        $('#update-mobile #email').addClass("is-invalid");
                                        $(".new-code").addClass('d-none');
                                        $("#commonErr").removeClass('d-none');
                                        $('#commonErr').html("You have been blocked for making multiple failed attemtps. Please try after some time");
                                        valid = false;
                                    } else {
                                        $(".new-code").removeClass('d-none');
                                        $('#update-mobile .emailClone').val(email);
                                        $('#update-mobile #email').removeClass("is-invalid");
                                        $('#update-mobile #tnc').prop('disabled', true);
                                        showNextTab(1)



                                    }


                                }

                            }
                        } else {
                            $('#update-mobile #email').addClass("is-invalid");
                            $('#update-mobile #email').val("");
                            $('#update-mobile #email_error').html("Please enter a valid Email Address as mentioned above");
                            valid = false;
                        }



                    } else {
                        if (data.errors.email === 'empty')
                        {
                            $('#update-mobile #email_error').html("Please Enter Your NIC/Gov Address");
                            $('#update-mobile #email').val("");
                        } else {
                            $('#update-mobile #email_error').html("Please Enter Your Email in correct format.");
                            $('#update-mobile #email').val("");
                            valid = false;
                        }
                    }
                    return valid;
                }, complete() {
                    $('.loader').hide();

                }
            });
        } else {
            $('#update-mobile #email_error').html("Please Enter Your Email Address in correct format");
            $('#update-mobile #email').val("");
            valid = false;
        }
    } else {

        $('#update-mobile #email_error').html("Please Enter Your Email Address");
        $('#update-mobile #email').val("");
        valid = false;
    }
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    if (!valid) {
        $('.loader').hide();
    }
    return valid;
}

function passDetail() {

    $('.text-danger').html('');
    $('.text-success').html('');
    $('.loader').show();
    var valid = true;
    var isNewUser = $("input[name='isNewUser']").val();
    passwordValidation();
    var email = $('#update-mobile #email').val();
    var password = $('#update-mobile #password').val();
    var captcha = $("#captcha11").val();
    var dataObj = {userValues: {email: email, password: password, isNewUser: isNewUser, loginCaptcha: captcha}};
    var data1 = JSON.stringify(dataObj);
    if (passValidation == false && captchaValidation == false) {
        $.ajax({
            type: "POST",
            url: "AuthPwdForUpdateMobile",
            data: data1,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data) {

                currentMobile = data.userValues.mobile.substring(3, 13);
                if (!data.errors) {

                    if (!data.userValues.blocked)
                    {

                        if (data.userValues.loginCaptchaAuthenticated === true) {

                            if (data.userValues.authenticated === true) {
                                $('#updateMobile #captcha_div').addClass("display-hide");
                                $('#update-mobile #password-div').addClass('display-hide');
                                // alert("data.userValues.mobile"+data.userValues.mobile)
                                $('#update-mobile #continue').attr("disabled", true);
                                //$("input[name='new_mobile']").val(data.userValues.mobile);
                                //$("input[name='country_code_val']").val(data.userValues.countryCode);
                                $("input[name='isNewUser']").val(data.userValues.isNewUser);
                                $("input[name='isEmailValidated']").val(data.userValues.isEmailValidated);
                                $("input[name='isIsNicEmployee']").val(data.userValues.isNICEmployee);
                                $('#update-mobile #email_error').val(data.userValues.mobile);
                                $('#update-mobile #old-mobile').removeClass("display-hide");
                                // var startMobile = data.userValues.mobile.substring(0, 3);
                                var endMobile = data.userValues.mobile.substring(10, 13);
                                $('#update-mobile #mobile').html("+91XXXXXXX" + endMobile);
                                $('#update-mobile #nicDateOfRetirement').val(data.updateMobileMap.nicDateOfRetirement);
                                $('#update-mobile #nicDateOfBirth').val(data.updateMobileMap.nicDateOfBirth);
                                $('#update-mobile #designation').val(data.updateMobileMap.designation);
                                $('#update-mobile #displayName').val(data.updateMobileMap.displayName);
                                // $('#update-mobile #mobile').prop("readonly",true);
                                $('#update-mobile #new-mobile').removeClass("display-hide");
                                $('#update-mobile #signup-tab').addClass("display-hide");
                                $('#update-mobile #password_error').text("");
                                passwordDivEnabled = false;
                                $('#update-mobile #mobile-div').addClass("display-hide");
                                $('#update-mobile #password').focus();
                                $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                valid = true;
                                showNextTab(1)


                            } else {
                                $('#update-mobile #mobile-div').addClass("display-hide");
                                $('#update-mobile #password-div').removeClass("display-hide");
                                $('#update-mobile #password_error').text("Please enter correct password");
                                $('#update-mobile #password').val("");
                                $('#update-mobile #password').addClass("is-invalid");
                                $('#update-mobile #captcha11').removeClass("is-invalid");
                                $('#update-mobile #password').focus();
                                $('#update-mobile #logincaptchaerror11').html("");
                                $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                $("#update-mobile #captcha11").val("");
                                valid = false;
                                ;
                            }
                        } else {
                            if (data.userValues.authenticated === false) {
                                $('#update-mobile #mobile-div').addClass("display-hide");
                                $('#update-mobile #password-div').removeClass("display-hide");
                                $('#update-mobile #password_error').html("Please enter correct password");
                                $('#update-mobile #password').val("");
                                $('#update-mobile #password').focus();
                                $('#update-mobile #password').addClass("is-invalid");
                                $('#update-mobile #logincaptchaerror11').html("");
                                $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                                $("#update-mobile #captcha11").val("");
                                valid = false;
                            }
                            $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
                            $('#update-mobile #captcha11').addClass("is-invalid");
                            $('#update-mobile #logincaptchaerror11').html("Please enter correct captcha");
                            $("#update-mobile #captcha11").val("");
                            $('#update-mobile #logincaptchaerror11').focus();
                            valid = false;
                        }
                    } else {
                        $(".new-code").addClass('d-none');
                        $("#commonErr").removeClass('d-none');
                        $('#commonErr').html("You have been blocked for making multiple failed attemtps. Please try after some time");
                        valid = false;
                    }
                } else {
                    if (data.errors.email === 'empty')
                    {
                        $('#update-mobile #email_error').html("Please Enter Your Email Address");
                        $('#update-mobile #email').val("");
                    } else
                    {
                        $('#update-mobile #email_error').html("Please Enter Your Email in correct format.");
                        $('#update-mobile #email').val("");
                        valid = false;
                    }
                }

            },
            error: function () {


                console.log('error');
                valid = false;
            }, complete() {
                $('.loader').hide();
            }
        });
    } else {

        valid = false;
    }

    $('#update-mobile #email_error').html("");
    $('#update-mobile #logincaptchaerror').html("");
    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    if (!valid) {
        $('.loader').hide();
    }
    return valid;
}

function mobileDetail(otpGenerateType) {


    $('.text-danger').html('');
    $('.text-success').html('');
    $('.loader').show();
    if ($('#update-mobile #profile_update:checked').val() == 'profile') {
        $("#update-mobile #newmobile").val(currentMobile);
        $("#updation_type").val('profile');
    }
    var counter = 0;
    var valid = true;
    var email = $('#update-mobile #email').val();
    var password = $('#update-mobile #password').val();
    var prefixMob = $('#update-mobile #country_code_update_mob').val();
    var newmobile = $('#update-mobile #newmobile').val();
    var newcode = $('#update-mobile #newMobilecode').val();
    var isNewUser = $("input[name='isNewUser']").val();
    var updationType = $("#updation_type").val();
    var resendVal = otpGenerateType;
    mobileValidation();
    var dataObj = {userValues: {new_mobile: newmobile, country_code: "+91", email: email, password: password, isNewUser: isNewUser, otp_generate_or_resend: resendVal, type: updationType}};
    var data1 = JSON.stringify(dataObj);
    //alert("otpGenerateType"+otpGenerateType)
    //alert((mobValidation === false && !otpTxt))
    if ((mobValidation === false && !otpTxt) || otpGenerateType === 'otpResend') {

        $.ajax({
            type: "POST",
            url: "Otpgen_newmobile",
            data: data1,
            datatype: JSON,
            contentType: 'application/json; charset=utf-8',
            success: function (data)
            {

                $("#moberrMax").removeClass('alert alert-success mt-2')
                $("#moberrMax").html('');
                $('#resendOtppp').removeClass('d-none');
                $('#resendOtppp').addClass('d-block');

                $("#updateoptionProfile").addClass('display-hide');
                if (data.userValues.authenticated === true) {

                    if (data.userValues.returnString == "mobileInLdap")
                    {
                        $("#update-mobile #new-code").addClass("display-hide");
                        $("#update-mobile #succ_msg").hide();
                        $('#update-mobile #moberr').html("Your email address (" + email + ") is already mapped with this number.");
                        $('#update-mobile #newmobile').addClass("is-invalid");
                    } else if (data.userValues.returnString == "moberr")
                    {
                        $("#update-mobile #new-code").addClass("display-hide");
                        $("#update-mobile #succ_msg").hide();
                        $('#update-mobile #moberr').html("Please Enter 10 Digits Mobile Number");
                        $('#update-mobile #newmobile').addClass("is-invalid");
                    } else if (data.userValues.returnString.indexOf("@") > -1)
                    {
                        $("#update-mobile #new-code").addClass("display-hide");
                        $("#update-mobile #succ_msg").hide();
                        $("#updation_type").val("profile");
                        $("#moberrMax").addClass('alert alert-success mt-2')
                        $('#update-mobile #moberrMax').html("There are already 3 or more email addresses (" + data.userValues.returnString + ") associated with this mobile number so now <b>you can update only user profile, If you want to continue than click to next button</b>");
                        $('#update-mobile #newmobile').addClass("is-invalid");
                    } else if (data.userValues.returnString == "previousOtp") {
                        $("#update-mobile #new-code").removeClass("display-hide");
                        $("#update-mobile #succ_msg").show();
                        $("#update-mobile #succ_msg").html("Please use previous otp,It is valid for 30 mins.")
                        $('#update-mobile #moberr').html("");
                        $('#update-mobile #newmobile').prop('readonly', true);
                        otpTxt = true;
                    } else if (data.userValues.returnString === 'ResendOtpLimitExceed') {
                        $("#err_msggg").html('You have exhausted your resend attempts. Please retry after half an hour')
                        $('#resendOtppp').addClass('d-none');
                        $('#resendOtppp').removeClass('d-block');
                        otpTxt = true;
                    } else if (data.userValues.returnString == "ResendOtp") {
                        $("#update-mobile #new-code").removeClass("display-hide");
                        $("#update-mobile #succ_msg").show();
                        $("#update-mobile #succ_msg").html("OTP resent successfully")
                        $('#update-mobile #moberr').html("");
                        $('#update-mobile #newmobile').prop('readonly', true);
                        otpTxt = true;
                    } else {
                        $('#update-mobile #newmobile').prop('readonly', true);
                        $("#update-mobile #new-code").removeClass("display-hide");
                        $("#update-mobile #succ_msg").show();
                        $("#update-mobile #succ_msg").html("Please Enter OTP.")
                        $('#update-mobile #moberr').html("");
                        otpTxt = true;
                    }
                }
                return false
            }, error: function ()
            {
                //  
                console.log("error in old code verify")
            }, complete() {
                $('.loader').hide();
            }
        })
    }

    if (otpTxt && otpGenerateType !== 'otpResend') {
        //alert("222222222222")
        verifyOtp();
    }
}

function verifyOtp() {
    //alert("verifyOTP");
    $('.text-danger').html('');
    $('.text-success').html('');
    $('.loader').show();
    var valid = true;
    var email = $('#update-mobile #email').val();
    var password = $('#update-mobile #password').val();
    var prefixMob = $('#update-mobile #country_code_update_mob').val();
    var newmobile = $('#update-mobile #newmobile').val();
    var newcode = $('#update-mobile #newMobilecode').val();
    var isNewUser = $("input[name='isNewUser']").val();

    var nicDateOfBirth = $("input[name='nicDateOfBirth']").val();
    var nicDateOfRetirement = $("input[name='nicDateOfRetirement']").val();
    var designation = $("input[name='designation']").val();
    var displayName = $("input[name='displayName']").val();
    newmobile = "+91" + newmobile;
    $("#commonErr").addClass('d-none');
    var mobile = $("input[name='new_mobile']").val();
    var isNewUser = $("input[name='isNewUser']").val();
    var dataObj = {userValues: {new_mobile: newmobile, newcode: newcode, isNewUser: isNewUser, email: email, mobile: newmobile, password: password, nicDateOfBirth: nicDateOfBirth, nicDateOfRetirement: nicDateOfRetirement, designation: designation, displayName: displayName}};
    var data1 = JSON.stringify(dataObj)
    $.ajax({
        type: "POST",
        url: "verify_newmobile_code",
        data: data1,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data)
        {
            //   alert("inside success");
            console.log("-----" + data);


            if (data.userValues.blocked === true)
            {
                $(".new-code").addClass('d-none');
                $("#commonErr").removeClass('d-none');
                $('#commonErr').html("You have been blocked for making multiple failed attemtps. Please try after some time");
                //alert("blocked")

            } else if (data.userValues.isexistInKavach == "true")
            {
                window.location.href = "Mobile_registration";
                $("#update-mobile #newcode").val('');
                valid = false;
            } else {

                if (data.userValues.authenticated === true) {
                    if (data.userValues.returnString == "new_code_err")
                    {
                        $('#update-mobile #err_msggg').html("Please Enter OTP Code in Correct Format");
                        $("#update-mobile #succ_msg").html("");
                        valid = false;
                    } else if (data.userValues.returnString == "newOtpNotMatch")
                    {
                        $('#update-mobile #err_msggg').html("Please Enter Correct OTP");
                        $("#update-mobile #succ_msg").html("");
                        valid = false;
                    } else if (data.userValues.returnString == "newuser")

                    {
                        showNextTab(1);
                        $('#entered-email').val(email)
                        $('#entered-mobile').val(newmobile)
                        $('#fname').val(data.userValues.name)
                        //window.location.href = "profile";
                    } else if (data.userValues.returnString == "olduser")
                    {
                        showNextTab(1);
                        $(".mobileClone").val(newmobile);
                        valid = true;
                        //window.location.href = "Mobile_registration";
                    } else if (data.userValues.returnString == "mobilesamehod")
                    {
                        $('#update-mobile #moberr').html("You can not update your mobile number same as of your reporting officer");
                        $("#update-mobile #newcode").val('');
                    } else if (data.userValues.returnString == "mobilesameus")
                    {
                        $('#update-mobile #moberr').html("You can not update your mobile number same as of your Under Secratery");
                        $("#update-mobile #newcode").val('');
                        valid = false;
                    }
                }
            }
        }, error: function ()
        {

            console.log("error in new code ")
        }, complete() {
            $('.loader').hide();
        }
    })

    if (valid) {
        document.getElementsByClassName("step")[currentTab].className += " finish";
    }
    return valid;
}


function presubmitForm() {
    var data = JSON.stringify($('#update-mobile').serializeObject());
    var dt = JSON.parse(data)
    $.each(dt, function (i, v) {
        $('#updateMobilePreview input[name=' + i + ']').val(v);
        $('#updateMobilePreview input[name=' + i + ']').prop('readonly', true);
    })
    console.log(dt);
    //$('#updateMobilePreview').modal('show');
}


function sendOTP(otp_generate_or_resend)
{
    var email = $('#update-mobile #email').val();
    var on_behalf_email = $('#update-mobile #on_behalf_email').val();
    // alert("email::"+email+"on_behalf_email:::"+on_behalf_email+"otp_generate_or_resend"+otp_generate_or_resend)
    console.log("on_behalf_email" + on_behalf_email + "email" + email)
    var dataObj = {userValues: {email: email, on_behalf_email: on_behalf_email, otp_generate_or_resend: otp_generate_or_resend}};
    var data1 = JSON.stringify(dataObj);
    console.log("data1::::" + data1)
    $.ajax({
        type: "POST",
        url: "sendOtp",
        data: data1,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {

            console.log("data.userValues.returnString" + data.userValues.returnString)

            if (data.userValues.returnString === "previousOtp") {

                $('#update-mobile #on_behalf_email').prop('readonly', true);
                $("#update-mobile #new-code").removeClass("display-hide");
                $("#update-mobile #succ_msg").show();
                $("#update-mobile #succ_msg").html("Please use previous otp,It is valid for 30 mins")
                $('#update-mobile #moberr').html("");
                $("#update-mobile #err_msgggg").html('');
                $("#update-mobile #onbehalf_email_err").html("")
                $("#update-mobile #otp_onbehalf_err").html("")
                $("#update-mobile #newcodediv").removeClass("d-none");
                otpTxt = true;
            } else if (data.userValues.returnString === "same_as_user") {
                $("#update-mobile #newcodediv").addClass("d-none");
                $('#update-mobile #on_behalf_email').prop('readonly', false);
                $("#update-mobile #new-code").removeClass("display-hide");
                $("#update-mobile #succ_msg").show();
                $("#update-mobile #onbehalf_email_err").html("Email address can not be same as user email address")
                $('#update-mobile #moberr').html("");
                $("#update-mobile #succ_msg").html('');
                otpTxt = true;
            } else if (data.userValues.returnString === "nongov") {
                $("#update-mobile #newcodediv").addClass("d-none");
                $('#update-mobile #on_behalf_email').prop('readonly', false);
                $("#update-mobile #new-code").removeClass("display-hide");
                $("#update-mobile #succ_msg").show();
                $("#update-mobile #onbehalf_email_err").html("Please Enter Your NIC/Gov Address")
                $('#update-mobile #moberr').html("");
                $("#update-mobile #succ_msg").html('');
                otpTxt = true;
            } else if (data.userValues.returnString === 'ResendOtpLimitExceed') {
                $("#update-mobile #newcodediv").removeClass("d-none");
                $('#update-mobile #on_behalf_email').prop('readonly', true);
                $("#update-mobile #err_msgggg").html('You have exhausted your resend attempts. Please retry after half an hour')
                $("#update-mobile #succ_msg").html("");
                $('#update-mobile #resendOtppp').addClass('d-none');
                $('#update-mobile #resendOtppp').removeClass('d-block');
                $("#update-mobile #onbehalf_email_err").html("")
                $("#update-mobile #otp_onbehalf_err").html("")
                otpTxt = true;
            } else if (data.userValues.returnString === "ResendOtp") {
                $("#update-mobile #newcodediv").removeClass("d-none");
                $('#update-mobile #on_behalf_email').prop('readonly', true);
                $("#update-mobile #new-code").removeClass("display-hide");
                $("#update-mobile #succ_msg").show();
                $("#update-mobile #succ_msg").html("OTP resent successfully")
                $('#update-mobile #moberr').html("");
                $('#update-mobile #newmobile').prop('readonly', true);
                $("#update-mobile #err_msgggg").html('')
                $("#update-mobile #onbehalf_email_err").html("")
                $("#update-mobile #otp_onbehalf_err").html("")

                otpTxt = true;
            } else {
                $("#update-mobile #newcodediv").removeClass("d-none");
                $('#update-mobile #on_behalf_email').prop('readonly', true);
                $('#update-mobile #newmobile').prop('readonly', true);
                $("#update-mobile #new-code").removeClass("display-hide");
                $("#update-mobile #succ_msg").show();
                var startMobile = data.userValues.mobile.substring(0, 3);
                var endMobile = data.userValues.mobile.substring(10, 13);
                $("#update-mobile #succ_msg").html("OTP has been sent to " + startMobile + "XXXXXXX" + endMobile + " mobile number")
                $('#update-mobile #moberr').html("");
                $("#update-mobile #err_msgggg").html('')
                $("#update-mobile #onbehalf_email_err").html("")
                $("#update-mobile #otp_onbehalf_err").html("")
                otpTxt = true;
            }

        }, error: function ()
        {

            console.log("error in new code ")
        }, complete: function () {

        }
    });
}

function submitForm() {
    $('.loader').show()
    var data = JSON.stringify($('#update-mobile').serializeObject());
    $.ajax({
        type: "POST",
        url: "update_mobile_tab1",
        data: {data: data},
        datatype: JSON,
        success: function (data) {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error_flag = true;
            if (jsonvalue.error.title_error !== null && jsonvalue.error.title_error !== "" && jsonvalue.error.title_error !== undefined)
            {
                $('#initial_err').html(jsonvalue.error.title_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#initial_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }
            if (jsonvalue.error.fname_error !== null && jsonvalue.error.fname_error !== "" && jsonvalue.error.fname_error !== undefined)
            {
                $('#fname_err').html(jsonvalue.error.fname_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#fname_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }
            if (jsonvalue.error.mname_error !== null && jsonvalue.error.mname_error !== "" && jsonvalue.error.mname_error !== undefined)
            {
                $('#mname_err').html(jsonvalue.error.mname_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#mname_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }
            if (jsonvalue.error.lname_error !== null && jsonvalue.error.lname_error !== "" && jsonvalue.error.lname_error !== undefined)
            {
                $('#lname_err').html(jsonvalue.error.lname_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#lname_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }
            if (jsonvalue.error.reason_error !== null && jsonvalue.error.reason_error !== "" && jsonvalue.error.reason_error !== undefined)
            {
                $('#reason_err').html(jsonvalue.error.reason_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#reason_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }
            if (jsonvalue.error.reason_txt_error !== null && jsonvalue.error.reason_txt_error !== "" && jsonvalue.error.reason_txt_error !== undefined)
            {
                $('#reason_txt_err').html(jsonvalue.error.reason_txt_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#reason_txt_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }

            if (jsonvalue.error.onbehalf_email_error !== null && jsonvalue.error.onbehalf_email_error !== "" && jsonvalue.error.onbehalf_email_error !== undefined)
            {
                $('#onbehalf_email_err').html(jsonvalue.error.onbehalf_email_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#onbehalf_email_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }
            if (jsonvalue.error.relation_with_error !== null && jsonvalue.error.relation_with_error !== "" && jsonvalue.error.relation_with_error !== undefined)
            {
                $('#relation_err').html(jsonvalue.error.relation_with_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#relation_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }

            if (jsonvalue.error.relationtxt_with_error !== null && jsonvalue.error.relationtxt_with_error !== "" && jsonvalue.error.relationtxt_with_error !== undefined)
            {
                $('#other_relation_err').html(jsonvalue.error.relationtxt_with_error)
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#other_relation_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }

            if (jsonvalue.error.otp_onbehalf_error !== null && jsonvalue.error.otp_onbehalf_error !== "" && jsonvalue.error.otp_onbehalf_error !== undefined)
            {

                $('#otp_onbehalf_err').html(jsonvalue.error.otp_onbehalf_error)
                $("#update-mobile #succ_msg").html("")

                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#otp_onbehalf_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }

            if (jsonvalue.error.nongov_error !== null && jsonvalue.error.nongov_error !== "" && jsonvalue.error.nongov_error !== undefined)
            {

                $('#onbehalf_email_err').html(jsonvalue.error.nongov_error)
                $("#update-mobile #succ_msg").html("")

                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#onbehalf_email_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }

            if (jsonvalue.error.verified_mobile_err !== null && jsonvalue.error.verified_mobile_err !== "" && jsonvalue.error.verified_mobile_err !== undefined)
            {

                $('#verified_mobile_err').html(jsonvalue.error.verified_mobile_err)
                $("#update-mobile #succ_msg").html("")

                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            } else {
                $('#verified_mobile_err').html("");
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("")
            }


            if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
            {

                $('#dor_err').html(jsonvalue.error.dor_err)
                //$("#update-mobile #succ_msg").html("")
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            }

            if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
            {

                $('#dob_err').html(jsonvalue.error.dob_err)
                //$("#update-mobile #succ_msg").html("")
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            }

            if (jsonvalue.error.deg_err !== null && jsonvalue.error.deg_err !== "" && jsonvalue.error.deg_err !== undefined)
            {

                $('#deg_err').html(jsonvalue.error.deg_err)
                //$("#update-mobile #succ_msg").html("")
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            }

            if (jsonvalue.error.dis_err !== null && jsonvalue.error.dis_err !== "" && jsonvalue.error.dis_err !== undefined)
            {

                $('#dis_err').html(jsonvalue.error.dis_err)
                //$("#update-mobile #succ_msg").html("")
                error_flag = false;
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
            }



            if (!error_flag) {


            } else {

                if (jsonvalue.returnString == "true")
                {
                    var data = JSON.stringify($('#update-mobile').serializeObject());
                    var dt = JSON.parse(data)
                    $.each(dt, function (i, v) {
                        $('#updateMobilePreview input[name=' + i + ']').val(v);
                        $('#updateMobilePreview input[name=' + i + ']').prop('readonly', true);
                        $("#update-mobile #succ_msg").html("")
                        $("#update-mobile #err_msgggg").html("")
                    })
                    $('#updateMobilePreview').modal('show');
                }
            }

        }, error: function ()
        {

            console.log("error in new code ")
        }, complete: function () {
            $('.loader').hide();
        }
    });
}
function SubmitUsingEsign() {

    $.ajax({
        type: "POST",
        url: "consentMobile",
        data: {check: "esign"},
        success: function (data) {
//                        var myJSON = JSON.stringify(data);
//                        var jsonvalue = JSON.parse(myJSON);
//                        alert("data.userValues.returnString222222222" + jsonvalue.returnString)
            window.location.href = "esignPdfForUpdateMobile";
        },
        error: function () {
            console.log('error');
        }
    });
}
$('#reason').change(function () {

    if ($(this).val() === 'other') {
        $('#othReason').removeClass('d-none');
    } else {
        $('#othReason').addClass('d-none');
    }
})

$("#relationWithOwner").change(function () {
    if ($(this).val() === 'other') {
        $('#onBehalf #othRelation').removeClass('d-none');
        $('#onBehalf div#selectOnBehalf').removeClass('col-md-12').addClass('col-md-6 pr-0');
    } else {
        $('#onBehalf #othRelation').addClass('d-none');
        $('#onBehalf div#selectOnBehalf').removeClass('col-md-6 pr-0').addClass('col-md-12');
    }
})




$('input[name=radioOnBehalf]').click(function () {

    if ($(this).val() === 'On Behalf') {
        $('#onBehalf').removeClass('d-none');
    } else {
        $('#onBehalf').addClass('d-none');
    }
})



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
        $('#update-mobile #password').val("");
        $('#update-mobile #password').addClass("is-invalid");
        $("#update-mobile #captcha11").val("");
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
        $('.loader').hide();
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




function passwordValidation() {

    var passVal = $("#update-mobile #password").val();
    var captchaVal = $("#update-mobile #captcha11").val();
    if (passVal.length < 2) {
        $('#update-mobile #password_error').text("Please enter correct password");
        $('#update-mobile #password').val("");
        $("#update-mobile #captcha11").val("");
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
        $('.loader').hide();
    } else {
        $('#update-mobile #moberr').text("");
        $('#update-mobile #newmobile').removeClass("is-invalid");
        mobValidation = false;
    }
}


function updateCheck() {
    if ($('#update-mobile #mobileprofile_update:checked').val() == 'mobile_and_profile') {
        $('#update-mobile #new-mobile').removeClass('display-hide');
    } else if ($('#update-mobile #profile_update:checked').val() == 'profile') {
        $('#update-mobile #new-mobile').addClass('display-hide');
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


$('#update-mobile #refresh').click(function () {
    $('#captcha21').attr('src', 'Captcha?var=12345678' + Date.parse(new Date().toString()));
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
$('#country_opt').click(function () {
    $('.country_err').html('For international number, Please contact your NIC Coordinator/Delegated Admin');
})
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
}
)