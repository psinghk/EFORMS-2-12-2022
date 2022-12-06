/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//$("#da_onboarding_tab1").click(function (e) {
//
//    e.preventDefault();
//   // var chkfemail = $("#da_onboardingForm").val();
//    var data = JSON.stringify($('#da_onboarding_tab1').serializeObject());
//    var cert = $('#cert').val();
//    $.ajax({
//        type: 'post',
//        url: 'daOnboarding',
//        data: {data : data , cert : cert},
//        datatype: JSON,
//        success: function (data) {
//            alert("data = " + data);
//            alert (cert);
//            var error_flag = true;
//
//
//
//
//        },
//        error: function () {
//            //alert("Please Select Correct Email-ID");
//            console.log("This is error Case  !!");
//        }
//
//    });
//
//    return false;
//
//});

$(document).ready(function () {

    $.ajax({
        type: "GET",
        url: "vpn_exist",
        datatype: JSON,
        success: function (result)
        {
            console.log(data);
            try {
                var jvalue = JSON.parse(result);
                var api_details = JSON.parse(jvalue.api_response);
                var data = api_details.access_details;
                var objLength = Object.keys(api_details).length;
                const keys = Object.keys(api_details[0]);



                var data = api_details.access_details;
                console.log(api_details['success']);
                if (api_details['success'] === true) {

                    var icount = 0;
                    var countTime = objLength - 2;

                    //if (api_details.vpn_ip == null) {
                    //if( api_details[0].vpn_registration_no == null) {
                    console.log(api_details)

                    if (api_details[0].ipaddress == null) {
                        $("#vpn_reg_no").hide();
                        $("#vpn_reg_no_text").removeClass('d-none');
                    } else {
                        if ($("#vpn_reg_no")[0] !== null && $("#vpn_reg_no")[0] !== undefined) {
                            $("#vpn_reg_no")[0].selectedIndex = 0;
                            $("#vpn_reg_no").empty();
                            $("#vpn_reg_no").append($("<option selected='true' />").val(api_details[0].ipaddress).text(api_details[0].ipaddress));
                        }
                    }
                }
            } catch (Ex) {
                // $("#k_content_body").hide();
                //$("#vpn_ip_not_exist").show();
            }

        },
        error: function ()
        {
            //$("#k_content_body").hide();
            // $("#vpn_ip_not_exist").show();
        }
    });



    $.ajax({
        type: "GET",
        url: "fetchBoName",
        datatype: JSON,
        success: function (result)
        {
            console.log(result.boName);
            $("#boName").val(result.boName);
            /*
             if (result.boName == null || result.boName == "") {
             $("#k_content_body").hide();
             $("#bo_name_not_exist").show();
             }
             */
            if (result.coordEmails == null || result.coordEmails.length == 0) {
                $("#k_content_body").hide();
                $("#coord_emails_not_exist").show();
            }
        },
        error: function ()

        {

            // $("#k_content_body").hide();
            // $('#bo_name_not_exist').show();
        }

    });

    var daon_mobile = false;
    $("#daon_mobile").on("click", function () {
        daon_mobile = !daon_mobile;
    });


    $('#da_onboarding_tab1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#da_onboarding_tab1').serializeObject());
        console.log(data)
        var cert = $('#cert').val();
        var eligibility = $('[name="eligibility"]').filter(":checked").val();

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $.ajax({
            type: "POST",
            url: "daOnboarding_tab1",
            data: {data: data, cert: cert, CSRFRandom: CSRFRandom, daon_mobile: daon_mobile, eligibility: eligibility}, // line modified by pr on 22ndjan18
            datatype: JSON,
            success: function (data) {

                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.vpn_error !== null && jsonvalue.error.vpn_error !== "" && jsonvalue.error.vpn_error !== undefined)
                {
                    $('#vpn_error').html(jsonvalue.error.vpn_error)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                } else {
                    $('#vpn_error').html("");
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                }

                if (jsonvalue.error.upload_file !== null && jsonvalue.error.upload_file !== "" && jsonvalue.error.upload_file !== undefined)
                {
                    $('#file_err').html(jsonvalue.error.upload_file)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#file_err').html("");
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                } else {
                    $('#captchaerror').html("");
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                }
                // start, code added by pr on 10thjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 10thjan18

                if (!error_flag) {

                } else {

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#da_onboarding_tab2 #central_div').show();
                        $('#da_onboarding_tab2 #state_div').hide();
                        $('#da_onboarding_tab2 #other_div').hide();
                        $('#da_onboarding_tab2 #other_text_div').addClass("display-hide");
                        var select = $('#da_onboarding_tab2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#da_onboarding_tab2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#da_onboarding_tab2 #other_text_div').removeClass("display-hide");
                            $('#da_onboarding_tab2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#da_onboarding_tab2 #central_div').hide();
                        $('#da_onboarding_tab2 #state_div').show();
                        $('#da_onboarding_tab2 #other_div').hide();
                        $('#da_onboarding_tab2 #other_text_div').addClass("display-hide");
                        var select = $('#da_onboarding_tab2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#da_onboarding_tab2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#da_onboarding_tab2 #other_text_div').removeClass("display-hide");
                            $('#da_onboarding_tab2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#da_onboarding_tab2 #central_div').hide();
                        $('#da_onboarding_tab2 #state_div').hide();
                        $('#da_onboarding_tab2 #other_div').show();
                        $('#da_onboarding_tab2 #other_text_div').addClass("display-hide");
                        var select = $('#da_onboarding_tab2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        // alert(jsonvalue.profile_values.Org.toString().toLowerCase())

                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#da_onboarding_tab2 #other_text_div').removeClass("display-hide");
                            $('#da_onboarding_tab2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#da_onboarding_tab2 #central_div').hide();
                        $('#da_onboarding_tab2 #state_div').hide();
                        $('#da_onboarding_tab2 #other_div').hide();
                    }

                    $('#da_onboarding_tab2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#da_onboarding_tab2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#da_onboarding_tab2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#da_onboarding_tab2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#da_onboarding_tab2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#da_onboarding_tab2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#da_onboarding_tab2 #user_state').val(jsonvalue.profile_values.state);
                    $('#da_onboarding_tab2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#da_onboarding_tab2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#da_onboarding_tab2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#da_onboarding_tab2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#da_onboarding_tab2 #user_email').val(jsonvalue.profile_values.email);
                    $('#da_onboarding_tab2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#da_onboarding_tab2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#da_onboarding_tab2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#da_onboarding_tab2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#da_onboarding_tab2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);

                    $('#da_onboarding_tab2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    $('#da_onboarding_tab2 #ca_design').val(jsonvalue.profile_values.ca_design);

                    $('#da_onboarding_tab2 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);


                    $('.edit').removeClass('display-hide');
                    $('#daonboarding_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#da_onboarding_tab2 :button[type='button']").removeAttr('disabled');
                    $("#da_onboarding_tab2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                    if (jsonvalue.form_details.da_vpn_reg_no.length > 1) {
                        if (jsonvalue.form_details.da_vpn_reg_no[0]) {
                            $("#da_onboarding_tab2 #vpn_reg_no").val(jsonvalue.form_details.da_vpn_reg_no[0]);
                        } else {
                            $("#da_onboarding_tab2 #vpn_reg_no").val(jsonvalue.form_details.da_vpn_reg_no[1]);
                        }
                    } else {
                        $("#da_onboarding_tab2 #vpn_reg_no").val(jsonvalue.form_details.da_vpn_reg_no[0]);
                    }
                    $("#da_onboarding_tab2 #bo_name").val(jsonvalue.form_details.boName);
                    $("#da_onboarding_tab2 #daon_mobile").val(jsonvalue.form_details.daon_mobile);



                    if (jsonvalue.form_details.eligibility === 'department') {

                        $('#da_onboarding_tab2 #eligibility_department').prop("checked", true);
                        $('#da_onboarding_tab2 #eligibility_psu').prop("checked", false);

                    } else {

                        $('#da_onboarding_tab2 #eligibility_department').prop("checked", false);
                        $('#da_onboarding_tab2 #eligibility_psu').prop("checked", true);
                    }

                    console.log("mohittttttt :::::" + jsonvalue.form_details.daon_mobile);
                    if (jsonvalue.form_details.daon_mobile == 'on') {
                        $('#da_onboarding_tab2 #daon_mobile').attr("checked", true);
                    } else {
                        $('#da_onboarding_tab2 #daon_mobile').attr("checked", false);
                    }
                    $("#da_onboarding_tab2 #user_employment").attr("disabled", "disabled");
                    $("#da_onboarding_tab2 #Org").attr("disabled", "disabled");
                    $("#da_onboarding_tab2 #daon_mobile").attr("disabled", "disabled");
                    $("#da_onboarding_tab2 :input").prop('readonly', true);
                    $("#daonboarding_preview  select ").attr('disabled', 'disabled');
                }
            },
            error: function () {

            }
        });
    });

    $('#da_onboarding_tab2').submit(function (e) {

        e.preventDefault();
        $("#da_onboarding_tab2 :disabled").removeAttr('disabled');
        $('#daonboarding_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#daonboarding_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#daonboarding_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#daonboarding_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var da_tab2_data = $('#da_onboarding_tab2').serializeObject();
        da_tab2_data.vpn_reg_no = da_tab2_data.da_vpn_reg_no;
        da_tab2_data.da_vpn_reg_no = [da_tab2_data.da_vpn_reg_no];
        var data = JSON.stringify(da_tab2_data);
        $('#daonboarding_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var eligibility = $('[name="eligibility"]').filter(":checked").val();
        // start, code added by pr on 11thjan18

        //resetCSRFRandom();

        var CSRFRandom = $("#CSRFRandom").val();

        $('#da_onboarding_tab2 .edit').removeClass('display-hide');

        // end, code added by pr on 11thjan18       
        $.ajax({
            type: "POST",
            url: "daOnboarding_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom, daon_mobile: daon_mobile, eligibility: eligibility}, // line added by pr on 10thjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;


                if (jsonvalue.error.service_pop_err !== null && jsonvalue.error.service_pop_err !== "" && jsonvalue.error.service_pop_err !== undefined)
                {
                    $('#da_onboarding_tab2 #service_pop_err').html(jsonvalue.error.service_pop_err)
                    error_flag = false;
                    $('#da_onboarding_tab2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#da_onboarding_tab2 #service_pop_err').html("");
                    $('#da_onboarding_tab2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#da_onboarding_tab2 #imgtxt').val("")
                }

                if (jsonvalue.error.service_imap_err !== null && jsonvalue.error.service_imap_err !== "" && jsonvalue.error.service_imap_err !== undefined)
                {
                    $('#da_onboarding_tab2 #service_imap_err').html(jsonvalue.error.service_imap_err)
                    error_flag = false;
                    $('#da_onboarding_tab2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#da_onboarding_tab2 #service_imap_err').html("");
                    $('#da_onboarding_tab2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#da_onboarding_tab2 #imgtxt').val("")
                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#da_onboarding_tab2 #useremployment_error').focus();
                    $('#da_onboarding_tab2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#da_onboarding_tab2 #minerror').focus();
                    $('#da_onboarding_tab2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#da_onboarding_tab2 #deperror').focus();
                    $('#da_onboarding_tab2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#da_onboarding_tab2 #other_dept').focus();
                    $('#da_onboarding_tab2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#da_onboarding_tab2 #smierror').focus();
                    $('#da_onboarding_tab2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#da_onboarding_tab2 #state_error').focus();
                    $('#da_onboarding_tab2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#da_onboarding_tab2 #org_error').focus();
                    $('#da_onboarding_tab2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#da_onboarding_tab2 #ca_design').focus();
                    $('#da_onboarding_tab2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#da_onboarding_tab2 #hod_name').focus();
                    $('#da_onboarding_tab2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#da_onboarding_tab2 #hod_mobile').focus();
                    $('#da_onboarding_tab2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#da_onboarding_tab2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#da_onboarding_tab2 #hod_tel').focus();
                    $('#da_onboarding_tab2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#da_onboarding_tab2 #hodtel_error').html("");
                }
                // profile on 20th march


                if (error_flag) {




                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'daonboarding'},
                        datatype: JSON,
                        success: function (data)
                        {
                            window.location.href = 'e_sign';
                        }, error: function ()
                        {
                            $('#tab1').show();
                        }
                    });
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

});


$('#da_onboarding_tab2 #confirm').click(function (e) {

    e.preventDefault();
    $("#da_onboarding_tab2 :disabled").removeAttr('disabled');
    $('#da_onboarding_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
    $('#da_onboarding_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
    $('#da_onboarding_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
    $('#da_onboarding_preview_tab #user_email').removeAttr('disabled'); // 20th march
//    var data = JSON.stringify($('#da_onboarding_tab2').serializeObject());
    var da_tab2_data = $('#da_onboarding_tab2').serializeObject();
    da_tab2_data.vpn_reg_no = da_tab2_data.da_vpn_reg_no;
    da_tab2_data.da_vpn_reg_no = [da_tab2_data.da_vpn_reg_no];
    var data = JSON.stringify(da_tab2_data);
    $('#da_onboarding_preview_tab #user_email').prop('disabled', 'true'); // 20th march
    var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 19thjan18

    $.ajax({
        type: "POST",
        url: "daOnboarding_tab2",
        data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom}, // line added by pr on 10thjan18
        datatype: JSON,
        success: function (data)
        {

            resetCSRFRandom();// line added by pr on 10thjan18

            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            var error_flag = true;




            // profile on 20th march
            if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
            {
                $('#da_onboarding_tab2 #useremployment_error').focus();
                $('#da_onboarding_tab2 #useremployment_error').html(jsonvalue.error.employment_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #useremployment_error').html("");
            }
            if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
            {
                $('#da_onboarding_tab2 #minerror').focus();
                $('#da_onboarding_tab2 #minerror').html(jsonvalue.error.ministry_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #minerror').html("");
            }
            if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
            {
                $('#da_onboarding_tab2 #deperror').focus();
                $('#da_onboarding_tab2 #deperror').html(jsonvalue.error.dept_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #deperror').html("");
            }
            if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
            {
                $('#da_onboarding_tab2 #other_dept').focus();
                $('#da_onboarding_tab2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #other_dep_error').html("");
            }
            if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
            {
                $('#da_onboarding_tab2 #smierror').focus();
                $('#da_onboarding_tab2 #smierror').html(jsonvalue.error.state_dept_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #smierror').html("");
            }
            if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
            {
                $('#da_onboarding_tab2 #state_error').focus();
                $('#da_onboarding_tab2 #state_error').html(jsonvalue.error.state_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #state_error').html("");
            }
            if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
            {
                $('#da_onboarding_tab2 #org_error').focus();
                $('#da_onboarding_tab2 #org_error').html(jsonvalue.error.org_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #org_error').html("");
            }
            if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
            {
                $('#da_onboarding_tab2 #ca_design').focus();
                $('#da_onboarding_tab2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #cadesign_error').html("");
            }
            if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
            {
                $('#da_onboarding_tab2 #hod_name').focus();
                $('#da_onboarding_tab2 #hodname_error').html(jsonvalue.error.hod_name_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #hodname_error').html("");
            }
            if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
            {
                $('#da_onboarding_tab2 #hod_mobile').focus();
                $('#da_onboarding_tab2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #hodmobile_error').html("");
            }
            if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
            {
                $('#da_onboarding_tab2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #hodemail_error').html("");
            }
            if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
            {
                $('#da_onboarding_tab2 #hod_tel').focus();
                $('#da_onboarding_tab2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                error_flag = false;
            } else {
                $('#da_onboarding_tab2 #hodtel_error').html("");
            }
            // profile on 20th march
            console.log("error: " + error_flag)
            if (!error_flag) {
                $("#da_onboarding_tab2 :disabled").removeAttr('disabled');
                $('#da_onboarding_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                $('#da_onboarding_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                $('#da_onboarding_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
            } else {
                if ($('#da_onboarding_tab2 #tnc').is(":checked"))
                {
                    $('#da_onboarding_tab2 #tnc_error').html("");
                    $('#stack3').modal({backdrop: 'static', keyboard: false});
                    var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                    var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);

                    if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
                    {
                        $('#da_onboarding_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#da_onboarding_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#da_onboarding_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);


                    } else if (jsonvalue.form_details.min == "External Affairs")
                    {
                        $('#da_onboarding_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
                        $('#da_onboarding_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
                        $('#da_onboarding_form_confirm #fill_hod_mobile').html("+919384664224");
                    } else {
                        $('#da_onboarding_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#da_onboarding_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#da_onboarding_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                    }

                    $('#da_onboarding_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');

                } else {
                    $('#da_onboarding_tab2 #tnc_error').html("Please agree to Terms and Conditions");
                    $('#da_onboarding_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#da_onboarding_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    //below line commented by MI on 25thjan19
                    // $('#da_onboarding_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $("#da_onboarding_tab2 #tnc").removeAttr('disabled');
                }
            }
        }, error: function ()
        {
            $('#tab1').show();
        }
    });
    $('#da_onboarding_form_confirm #confirmYes').click(function () {
        $('#da_onboarding_tab2').submit();
        $('#stack3').modal('toggle');
    });
});
$('#da_onboarding_tab2 .edit').click(function () {


    var employment = $('#da_onboarding_tab2 #user_employment').val();
    var min = $('#da_onboarding_tab2 #min').val();
    var dept = $('#da_onboarding_tab2 #dept').val();
    var statecode = $('#da_onboarding_tab2 #stateCode').val();
    var Smi = $('#da_onboarding_tab2 #Smi').val();
    var Org = $('#da_onboarding_tab2 #Org').val();

    if (employment === 'Central' || employment === 'UT') {
        $.get('centralMinistry', {
            orgType: employment
        }, function (response) {
            var select = $('#imappop_preview_tab #min');
            select.find('option').remove();
            // alert(select)
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {
                if (min == value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }
            });

        });
        $.get('centralDepartment', {
            depType: min
        }, function (response) {
            var select = $('#imappop_preview_tab #dept');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {

                if (dept == value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }

            });
            if (response.toString().toLowerCase().indexOf("other") == -1)
            {
                if (response.toString().toLowerCase().indexOf("other") == -1 && dept == "other") {
                    $('<option selected="selected">').val("other").text("other").appendTo(select);
                } else {
                    $('<option>').val("other").text("other").appendTo(select);

                }
            }


        });
        if (dept === 'other') {
            $('#imappop_preview_tab #other_text_div').removeClass("display-hide");

        }

    } else if (employment === 'State') {
        $.get('centralMinistry', {
            orgType: employment
        }, function (response) {
            var select = $('#imappop_preview_tab #stateCode');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {
                if (statecode == value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }
            });
        });

        $.get('centralDepartment', {
            depType: statecode
        }, function (response) {
            var select = $('#imappop_preview_tab #Smi');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {

                if (Smi == value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }
            });
            if (response.toString().toLowerCase().indexOf("other") == -1)
            {
                if (response.toString().toLowerCase().indexOf("other") == -1 && Smi == "other") {
                    $('<option selected="selected">').val("other").text("other").appendTo(select);
                } else {
                    $('<option>').val("other").text("other").appendTo(select);

                }
            }

        });

        if (Smi === 'other') {

            $('#imappop_preview_tab #other_text_div').removeClass("display-hide");

        }

    } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
        $('#imappop_preview_tab #central_div').hide();
        $('#imappop_preview_tab #state_div').hide();
        $('#imappop_preview_tab #other_div').show();
        $('#imappop_preview_tab #other_text_div').addClass("display-hide");
        $.get('centralMinistry', {
            orgType: employment
        }, function (response) {
            var select = $('#imappop_preview_tab #Org');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {
                if (Org == value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }
            });
            if (response.toString().toLowerCase().indexOf("other") == -1)
            {
                if (response.toString().toLowerCase().indexOf("other") == -1 && Org == "other") {
                    $('<option selected="selected">').val("other").text("other").appendTo(select);
                } else {
                    $('<option>').val("other").text("other").appendTo(select);

                }
            }

        });
        if (Org === 'other') {
            $('#imappop_preview_tab #other_text_div').removeClass("display-hide");

        }


    } else {
        $('#imappop_preview_tab #central_div').hide();
        $('#imappop_preview_tab #state_div').hide();
        $('#imappop_preview_tab #other_div').hide();
    }

    $('#imappop_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
    $('#imappop_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
    $('#imappop_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
    $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
    //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
    //$('#imappop_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
    $('#imappop_preview_tab #REditPreview #hod_email').removeAttr('disabled');
    $(this).addClass('display-hide');
})

var vpn_form_redirect = function () {
    if ($("[name='checkbox_b']").prop("checked") && $("[name='checkbox_b']").prop("checked")) {
        $('[name="button_yes"]').prop('disabled', false);
    }
    ;
};
$("[name='checkbox_b']").click(vpn_form_redirect);
$("[name='checkbox_a']").click(vpn_form_redirect);
;