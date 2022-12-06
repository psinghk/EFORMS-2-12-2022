
$(document).ready(function () {
    var count1 = 0;
    var count2 = 0;
    var count_ip = 0;
    var count_ip_form = 0;
    var flag1;


    $('.page-wrapper').find('input').click(function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        var security_audit = $('input[name="security_audit"]:checked').val();

        if ($('#' + field_form + ' #ip_staging').is(':checked')) {
            console.log("11111111111")
            $('#' + field_form + ' #cert_div').addClass('d-none');
            $('#' + field_form + ' #hardware_cert_div').addClass('d-none');
            $('#' + field_form + " #security_exp_date").addClass('d-none');
            $('#' + field_form + ' #cert').val('false');
            $('#' + field_form + ' #hardwarecert').val('false');

        } else {
            if (field_form === "relay_form1")
            {
                if (security_audit === "Software")
                {
                    console.log("33333333333")
                    $('#' + field_form + ' #cert_div').removeClass('d-none');
                    $('#' + field_form + " #security_exp_date").removeClass('d-none');
                    $('#' + field_form + ' #hardware_cert_div').addClass('d-none');
                }
                if (security_audit === "Hardware")
                {
                    $('#' + field_form + ' #cert_div').removeClass('display-hide');
                    $('#' + field_form + ' #hardware_cert_div').removeClass('d-none');
                    $('#' + field_form + " #security_exp_date").addClass('d-none');
                    $('#' + field_form + ' #cert_div').addClass('d-none');
                }
            }
        }
    });

    $(document).on('find click', '.page-wrapper button', function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        var form_tab;
        console.log("field_name" + field_name)
        if (field_form === "relay_form1") {
            form_tab = "1";
            if (field_name == "addip") {
                if (count_ip_form < 9) {
                    var elmnt = '<div class="row con mt-2"><div class="col-10"><input class="form-control class_name" placeholder="Enter the IP Address [e.g.: 164.100.X.X]" type="text" name="relay_ip[]" id="relay_ip" maxlength="20"></div>' + '<div class="col-1 pl-0"><button type="button" class="rmvbox2 btn btn-danger" name="dns_rmv2" title="Remove Row" id="rmv"><i class="fa fa-window-close"></i></button></div></div>';
                    count_ip_form++;
                    $('#' + field_form + ' #add_ip').append(elmnt);
                } else if (flag1) {
                    $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                        $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only ten(10) entries allowed.</div>');
                        $('#' + field_form + ' .alert_msg').fadeOut(8000);
                    });
                    flag1 = false;
                }
                if (count2 === 1) {
                    count_ip_form += 1;
                    count2++;
                } else {
                    if (count1 < 9) {
                        count2++;
                    }
                }
                if (count_ip_form == "9") {
                    $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                        $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only ten(10) entries allowed.</div>');
                        $('#' + field_form + ' .alert_msg').fadeOut(2000);
                    });
                }
            }
            if (field_name == "modifyip") {

                if (count_ip_form < 9) {
                    var elmnt = '<div class="row con mt-2"><div class="col-5"><input class="form-control class_name" placeholder="Enter the OLD IP Address [e.g.: 164.100.X.X]" type="text" name="old_relay_ip[]" id="old_relay_ip" maxlength="20"></div><div class="col-5"><input class="form-control class_name" placeholder="Enter the OLD IP Address [e.g.: 164.100.X.X]" type="text" name="relay_ip[]" id="relay_ip" maxlength="20"></div>' + '<div class="col-1 pl-0"><button type="button" class="rmvbox2 btn btn-danger" name="dns_rmv2" title="Remove Row" id="rmv"><i class="fa fa-window-close"></i></button></div></div>';
                    count_ip_form++;
                    $('#' + field_form + ' #add_ip').append(elmnt);
                } else if (flag1) {
                    $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                        $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only ten(10) entries allowed.</div>');
                        $('#' + field_form + ' .alert_msg').fadeOut(8000);
                    });
                    flag1 = false;
                }
                if (count2 === 1) {
                    count_ip_form += 1;
                    count2++;
                } else {
                    if (count1 < 9) {
                        count2++;
                    }
                }
                if (count_ip_form == "9") {
                    $('#' + field_form + ' #add_ip').fadeOut('slow', function () {
                        $('#' + field_form + ' #add_ip').fadeIn('slow').append('<div class="alert alert-danger alert_msg">Only ten(10) entries allowed.</div>');
                        $('#' + field_form + ' .alert_msg').fadeOut(2000);
                    });
                }
            }

            if (field_name == "dns_rmv2") {
                count_ip_form--;
                count2--;
                $(this).closest('.con').remove();
            }
        }

        if (count_ip_form <= 9 && count2 >= 1) {
            $('input[name="req_"]:not(:checked)').prop('disabled', true);
        } else {
            $('input[name="req_"]:not(:checked)').prop('disabled', false);
        }

    });
//     $('.page-container-bg-solid').find('input').click(function () {
//       alert("hi")  
//     })
    $(document).on('find click', '.page-container-bg-solid input ', function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        //alert(field_id)
        if (field_id === 'point_contact_1') {
            var isChecked = $('#point_contact_1').is(":checked");

            if (isChecked) {
                $.ajax({
                    type: "POST",
                    url: "filling_relay_tab",
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);

                        $('#' + field_form + ' #point_email').val(jsonvalue.userDetails.email);
                        $('#' + field_form + ' #mobile_number').val(jsonvalue.userDetails.mobile);
                        $('#' + field_form + ' #landline_number').val(jsonvalue.userDetails.ophone);
                        $('#' + field_form + ' #point_name').val(jsonvalue.userDetails.name);
                    },
                    error: function () {
                        console.log('error');
                    }
                });
            }

        }
        if (field_id === 'point_contact_2') {

            $('#' + field_form + ' #point_email').val("");
            $('#' + field_form + ' #mobile_number').val("");
            $('#' + field_form + ' #landline_number').val("");
            $('#' + field_form + ' #point_name').val("");

        }

//        if ($(this).val() === 'Software') {
//            $('#' + field_form + " #hardware_cert_div").addClass('d-none');
//            if ($('#' + field_form + ' #ip_staging').is(':checked')) {
//
//                // $('#' + field_form + ' #cert_div').removeClass('d-none');
//                $('#' + field_form + ' #hardware_cert_div').addClass('display-hide');
//                $('#' + field_form + " #security_exp_date").addClass('d-none');
//            } else {
//
//                $('#' + field_form + ' #cert_div').removeClass('d-none');
//                $('#' + field_form + " #security_exp_date").removeClass('d-none');
//                $('#' + field_form + ' #hardware_cert_div').removeClass('display-hide');
//            }
//
//
//        }
//        if ($(this).val() === 'Hardware') {
//
//            if ($('#' + field_form + ' #ip_staging').is(':checked')) {
//
//                // $('#' + field_form + ' #cert_div').removeClass('d-none');
//                $('#' + field_form + ' #hardware_cert_div').addClass('display-hide');
//                $('#' + field_form + " #security_exp_date").addClass('d-none');
//                $('#' + field_form + ' #cert_div').addClass('d-none');
//            } else {
//
//                $('#' + field_form + ' #hardware_cert_div').removeClass('display-hide');
//            }
//
//        }


        if ($(this).val() === 'req_delete') {
            $('#' + field_form + " #hardware_cert_div").addClass('d-none');
            $('#' + field_form + " #not-prompt-in-caseofdelete").addClass('d-none');
            $('#' + field_form + " #check-mxdomain-available").addClass('d-none');


        }
        if ($(this).val() === 'req_add' || $(this).val() === 'req_new' || $(this).val() === 'req_modify') {

            $('#' + field_form + " #hardware_cert_div").removeClass('d-none');
            $('#' + field_form + " #not-prompt-in-caseofdelete").removeClass('d-none');
            $('#' + field_form + " #check-mxdomain-available").removeClass('d-none');
        }

        if ($(this).val() === '465') {
            $('#' + field_form + " #auth_id_div").removeClass('d-none');
        }
        if ($(this).val() === '25') {
            $('#' + field_form + " #auth_id_div").addClass('d-none');
        }




    })
    // dns form submission
    $('#relay_form1').submit(function (e) {
        e.preventDefault();
        var old_relay_ip = $("#relay_form1 input[name='old_relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var relay_ip = $("#relay_form1 input[name='relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var data = JSON.stringify($('#relay_form1').serializeObject());

        var cert = $('#relay_form1 #cert').val();
        console.log("cert::" + cert)
        var hardwarecert = $('#hardwarecert').val();
        console.log("old_relay_ip" + old_relay_ip + "relay_ip" + relay_ip)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('#relay_form2 .edit').removeClass('display-hide');
        $('#relay_form1 #domain_mx').removeAttr('disabled');
        console.log("data:::" + data)
        $.ajax({
            type: "POST",
            url: "relay_tab1",
            data: {data: data, relay_ip: relay_ip, old_relay_ip: old_relay_ip, cert: cert, hardwarecert: hardwarecert, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#relay_form1 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#relay_form1 #relay_app_name').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined) {

                    $('#relay_form1 #app_url_err').html(jsonvalue.error.app_url_error);
                    $('#relay_form1 #relay_app_url').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #app_url_err').html("")
                }

                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined) {
                    console.log("inside iffffffffff")
                    $('#relay_form1 #sender_id_err').html(jsonvalue.error.sender_id_error);
                    $('#relayform1 #sender_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #sender_id_err').html("");
                }


                if (jsonvalue.error.auth_id_error !== null && jsonvalue.error.auth_id_error !== "" && jsonvalue.error.auth_id_error !== undefined) {

                    $('#relay_form1 #auth_id_err').html(jsonvalue.error.auth_id_error);
                    $('#relayform1 #auth_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #auth_id_err').html("");
                }
                if (jsonvalue.error.division_error !== null && jsonvalue.error.division_error !== "" && jsonvalue.error.division_error !== undefined)
                {
                    $('#relay_form1 #division_err').html(jsonvalue.error.division_error)
                    $('#relay_form1 #division').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #division_err').html("")
                }
                if (jsonvalue.error.os_error !== null && jsonvalue.error.os_error !== "" && jsonvalue.error.os_error !== undefined)
                {

                    $('#relay_form1 #os_err').html(jsonvalue.error.os_error)
                    $('#relay_form1 #os').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #os_err').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#relay_form1 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#relay_form1 #server_loc').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#relay_form1 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#relay_form1 #server_loc_txt').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #server_txt_err').html("")
                }

                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#relay_form1 #relay_ip_err').html(jsonvalue.error.ip1_error)
                    $('#relay_form1 #relay_server_loc').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #relay_ip_err').html("")
                }

                if (jsonvalue.error.port_err !== null && jsonvalue.error.port_err !== "" && jsonvalue.error.port_err !== undefined)
                {
                    $('#relay_form1 #relay_port_err').html(jsonvalue.error.port_err)
                    $('#relay_form1 #relay_port_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #relay_port_err').html("")
                }

                if (jsonvalue.error.port_err !== null && jsonvalue.error.port_err !== "" && jsonvalue.error.port_err !== undefined)
                {
                    $('#relay_form1 #relay_port_err').html(jsonvalue.error.port_err)
                    $('#relay_form1 #relay_port_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#relay_form1 #relay_port_err').html("")
                }

                if (jsonvalue.error.record_err !== null && jsonvalue.error.record_err !== "" && jsonvalue.error.record_err !== undefined)
                {
                    $('#relay_form1 #record_err').html(jsonvalue.error.record_err)
                    $('#relay_form1 #record_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #record_err').html("")
                }
                if (jsonvalue.error.domain_mx_error !== null && jsonvalue.error.domain_mx_error !== "" && jsonvalue.error.domain_mx_error !== undefined)
                {
                    $('#relay_form1 #domain_mx_err').html(jsonvalue.error.domain_mx_error)
                    $('#relay_form1 #domain_mx_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #domain_mx_err').html("")
                }

                if (jsonvalue.error.total_mail_err !== null && jsonvalue.error.total_mail_err !== "" && jsonvalue.error.total_mail_err !== undefined)
                {
                    $('#relay_form1 #total_mail_err').html(jsonvalue.error.total_mail_err)
                    $('#relay_form1 #total_mail_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #total_mail_err').html("")
                }

                if (jsonvalue.error.p_name_error !== null && jsonvalue.error.p_name_error !== "" && jsonvalue.error.p_name_error !== undefined)
                {
                    $('#relay_form1 #point_name_error').focus();
                    $('#relay_form1 #point_name_error').html(jsonvalue.error.p_name_error);
                    error_flag = false;
                } else {
                    $('#relay_form1 #point_name_error').html("");
                }
                if (jsonvalue.error.p_email_error !== null && jsonvalue.error.p_email_error !== "" && jsonvalue.error.p_email_error !== undefined)
                {
                    $('#relay_form1 #point_email_error').focus();
                    $('#relay_form1 #point_email_error').html(jsonvalue.error.p_email_error);
                    error_flag = false;
                } else {
                    $('#relay_form1 #point_email_error').html("");
                }
                if (jsonvalue.error.p_mobile_error !== null && jsonvalue.error.p_mobile_error !== "" && jsonvalue.error.p_mobile_error !== undefined)
                {
                    $('#relay_form1 #point_mobile_error').html(jsonvalue.error.p_mobile_error);
                    error_flag = false;
                } else {
                    $('#relay_form1 #point_mobile_error').html("");
                }
                if (jsonvalue.error.p_tel_error !== null && jsonvalue.error.p_tel_error !== "" && jsonvalue.error.p_tel_error !== undefined)
                {
                    $('#relay_form1 #point_tel_error').focus();
                    $('#relay_form1 #point_tel_error').html(jsonvalue.error.p_tel_error);
                    error_flag = false;
                } else {
                    $('#relay_form1 #point_tel_error').html("");
                }

                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#relay_form1 #audit_date_err').focus();
                    $('#relay_form1 #audit_date_err').html(jsonvalue.error.audit_date_err);
                    error_flag = false;
                } else {
                    $('#relay_form1 #audit_date_err').html("");
                }
                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                {
                    $('#relay_form1 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#relay_form1 #file_err').html("")
                }
                if (jsonvalue.error.hardware_file_err !== null && jsonvalue.error.hardware_file_err !== "" && jsonvalue.error.hardware_file_err !== undefined)
                {
                    $('#relay_form1 #hardware_file_err').html(jsonvalue.error.hardware_file_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#relay_form1 #hardware_file_err').html("")
                }

                if (checkDuplicates())
                {

                    $('#relay_form1 #relay_dup_ip_err').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#relay_form1 #relay_dup_ip_err').html("");

                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#captchaerror').html("")
                }

                if (jsonvalue.error.record_err !== null && jsonvalue.error.record_err !== "" && jsonvalue.error.record_err !== undefined)
                {
                    $('#record_err').html(jsonvalue.error.record_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#record_err').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                }

                // end, code added by pr on 22ndjan18
                if (!error_flag) {
                    $('#relay_form1 #domain_mx').prop('disabled', 'true');
                } else {
                    if (jsonvalue.form_details.req_ == "req_add" || jsonvalue.form_details.req_ == "req_modify")
                    {

                        $("#relay_form2 #oldip").removeClass('d-none');

                        if (old_relay_ip.indexOf(";") > -1) {

                            var myarray = old_relay_ip.split(';');
                            //var myarray = relay_ip.split(';');
                            for (var i = 0; i < myarray.length; i++)
                            {
                                var service = myarray[i].trim();
                                if (i === 0) {
                                    $('#relay_form2 #old_relay_ip').val(service);
                                } else {
                                    var elmnt = '<div class="con mt-2"><input class="form-control class_name_prvw" placeholder="Enter the IP Address [e.g.: 164.100.X.X]" type="text" name="old_relay_ip[]" id="relay_oldip1" value="' + service + '" maxlength="12"></div>';
                                    $('#relay_form2 #old_add_ip').append(elmnt);
                                }
                            }
                        } else {
                            $('#relay_form2 #old_relay_ip').val(old_relay_ip);
                        }
                    } else {
                        $("#relay_form2 #oldip").addClass('d-none');

                    }
                    $('#relay_form2 #add_ip').html('');
                    var error_flag = true;

                    if (relay_ip.indexOf(";") > -1) {
                        var myarray = relay_ip.split(';');
                        for (var i = 0; i < myarray.length; i++)
                        {
                            var service = myarray[i].trim();

                            if (i === 0) {
                                $('#relay_form2 #relay_ip').val(service);
                            } else {
                                var elmnt = '<div class="con mt-2"><input class="form-control class_name_prvw" placeholder="Enter the IP Address [e.g.: 164.100.X.X]" type="text" name="relay_ip[]" id="relay_ip1" value="' + service + '" maxlength="12"></div>';
                                $('#relay_form2 #add_ip').append(elmnt);
                            }
                        }
                    } else {

                        $('#relay_form2 #relay_ip').val(relay_ip);
                    }

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#central_div').show();
                        $('#state_div').hide();
                        $('#other_div').hide();
                        $('#other_text_div').addClass("display-hide");
                        var select = $('#min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#other_text_div').removeClass("display-hide");
                            $('#other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#central_div').hide();
                        $('#state_div').show();
                        $('#other_div').hide();
                        $('#other_text_div').addClass("display-hide");
                        var select = $('#stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#other_text_div').removeClass("display-hide");
                            $('#other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#central_div').hide();
                        $('#state_div').hide();
                        $('#other_div').show();
                        $('#other_text_div').addClass("display-hide");
                        var select = $('#Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org === 'other') {
                            $('#other_text_div').removeClass("display-hide");
                            $('#other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#central_div').hide();
                        $('#state_div').hide();
                        $('#other_div').hide();
                    }
                    $('#relay_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#relay_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#relay_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#relay_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#relay_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#relay_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#relay_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#relay_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#relay_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#relay_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#relay_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    //$('#relay_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#relay_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#relay_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#relay_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#relay_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#relay_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#relay_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    // $('#relay_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#relay_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#relay_form2 #relay_app_name').val(jsonvalue.form_details.relay_app_name);
                    $('#relay_form2 #relay_app_url').val(jsonvalue.form_details.relay_app_url);
                    $('#relay_form2 #req_').val(jsonvalue.form_details.req_)
                    if (jsonvalue.form_details.req_ !== "req_delete")
                    {
                        $('#relay_form2 #not-prompt-in-caseofdelete').removeClass("d-none")
                        $('#relay_form2 #relay_sender_id').val(jsonvalue.form_details.relay_sender_id);
                        $('#relay_form2 #domain_mx').val(jsonvalue.form_details.domain_mx);
                        $('#relay_form2 #mailsent').val(jsonvalue.form_details.mailsent);
                        $('#relay_form2 #point_name').val(jsonvalue.form_details.point_name);
                        $('#relay_form2 #point_email').val(jsonvalue.form_details.point_email);
                        $('#relay_form2 #landline_number').val(jsonvalue.form_details.landline_number);
                        $('#relay_form2 #mobile_number').val(jsonvalue.form_details.mobile_number);
                        $('#relay_form2 #point_contact_1').val(jsonvalue.form_details.point_contact);
                        $('#relay_form2 #security_exp_date').val(jsonvalue.form_details.security_exp_date);
//                        if (jsonvalue.form_details.domain_mx === "mailgw.nic.in")
//                        {
//                            $('#relay_form2 #other-record-addition').addClass('d-none')
//
//                        } else {
//                            $('#relay_form2 #other-record-addition').removeClass('d-none')
//                            if (jsonvalue.form_details.dkim === "dkim") {
//                                $('#relay_form2 #dkimtest').attr('checked', true);
//                            }
//
//                            if (jsonvalue.form_details.spf === "spf") {
//                                $('#relay_form2 #spftest').attr('checked', true);
//                            }
//
//                            if (jsonvalue.form_details.dmarc === "dmarc") {
//                                $('#relay_form2 #dmarctest').attr('checked', true);
//                            }
//
//                        }
//                        if (jsonvalue.form_details.is_hosted === "yes") {
//                            $('#relay_form2 #is_hosted_1').attr('checked', true);
//                        }
//
//                        if (jsonvalue.form_details.is_hosted === "no") {
//                            $('#relay_form2 #is_hosted_2').attr('checked', true);
//
//                        }





                        if (jsonvalue.form_details.smtp_port == "465") {
                            $('#relay_form2 #port_465').attr('checked', true);
                            $("#relay_form2 #auth_id_div").removeClass('d-none');
                            $('#relay_form2 #auth_id').val(jsonvalue.form_details.relay_auth_id);
                        }
                        if (jsonvalue.form_details.smtp_port == "25") {
                            $("#relay_form2 #auth_id_div").addClass('d-none');
                            $('#relay_form2 #port_25').attr('checked', true);
                        }


                        if (jsonvalue.form_details.point_contact == "yes") {
                            $('#relay_form2 #point_contact_1').attr('checked', true);
                        }

                        if (jsonvalue.form_details.point_contact == "no") {
                            $('#relay_form2 #point_contact_2').attr('checked', true);

                        }

                        // alert(jsonvalue.form_details.mail_type)
                        if (jsonvalue.form_details.otp_mail_service === "OTP Service through Email") {
                            $('#relay_form2 #otp_mail_service').attr('checked', true);
                        }

                        if (jsonvalue.form_details.mail_type_trans_mail === "Transactional Mails") {
                            $('#relay_form2 #mail_type_trans_mail').attr('checked', true);
                        }

                        if (jsonvalue.form_details.mail_type_reg_mail === "Registration Mails") {
                            $('#relay_form2 #mail_type_reg_mail').attr('checked', true);
                        }

                        if (jsonvalue.form_details.mail_type_forgotpass === "Forgot id/password") {
                            $('#relay_form2 #mail_type_forgotpass').attr('checked', true);
                        }

                        if (jsonvalue.form_details.mail_type_alert === "Alerts") {
                            $('#relay_form2 #mail_type_alert').attr('checked', true);
                        }



                        if (jsonvalue.form_details.mail_type_other === "Others") {
                            $('#relay_form2 #mail_type_other').attr('checked', true);
                            $("#relay_form2 #other_mail_type").removeClass('d-none');
                            $('#relay_form2 #other_mail_type').val(jsonvalue.form_details.other_mail_type);
                        }

                        if (jsonvalue.form_details.security_audit === "Hardware" && jsonvalue.form_details.ip_staging === "yes") {

                            $('#relay_form2 #security_audit_1').attr('checked', true);
                            $("#relay_form2 #security_exp_date").addClass('d-none');
                            $("#relay_form2 #hardware_cert_div").addClass('d-none')
                            $("#relay_form2 #cert_div").addClass('d-none')
                            $("#relay_form2 #cert_div1").removeClass('d-none')
                        }
                        if (jsonvalue.form_details.security_audit === "Software" && jsonvalue.form_details.ip_staging === "yes") {

                            $('#relay_form2 #security_audit_2').attr('checked', true);
                            $("#relay_form2 #security_exp_date").addClass('d-none');
                            $("#relay_form2 #hardware_cert_div").addClass('d-none')
                            $("#relay_form2 #cert_div").addClass('d-none')
                            $("#relay_form2 #cert_div1").removeClass('d-none')
                        }

                        if (jsonvalue.form_details.security_audit === "Hardware" && jsonvalue.form_details.ip_staging === "no") {

                            $('#relay_form2 #security_audit_1').attr('checked', true);
                            $("#relay_form2 #security_exp_date").addClass('d-none');
                            $("#relay_form2 #hardware_cert_div").removeClass('d-none')
                            $("#relay_form2 #cert_div").addClass('d-none')
                            $("#relay_form2 #cert_div1").addClass('d-none')
                            if (jsonvalue.form_details.hardware_uploaded_filename !== "")
                            {
                                $('#relay_form2 #hardware_uploaded_filename').text(jsonvalue.form_details.hardware_uploaded_filename);
                            }
                        }

                        if (jsonvalue.form_details.security_audit === "Software" && jsonvalue.form_details.ip_staging === "no") {

                            $('#relay_form2 #security_audit_2').attr('checked', true);
                            $("#relay_form2 #security_exp_date").removeClass('d-none');
                            $("#relay_form2 #hardware_cert_div").addClass('d-none')
                            $("#relay_form2 #cert_div").removeClass('d-none')
                            $("#relay_form2 #cert_div1").addClass('d-none')
                            if (jsonvalue.form_details.uploaded_filename !== "")
                            {
                                $('#relay_form2 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);
                            }
                        }




                    } else {


                        $('#relay_form2 #not-prompt-in-caseofdelete').addClass("d-none")
                    }

                    $('#relay_form2 #relay_app_name').val(jsonvalue.form_details.relay_app_name);
                    // $('#relay_form2 #relay_app_url').val(jsonvalue.form_details.application_url);
                    $('#relay_form2 #division').val(jsonvalue.form_details.division);
                    $('#relay_form2 #os').val(jsonvalue.form_details.os);
                    if (jsonvalue.form_details.server_loc === "Other")
                    {
                        $('#relay_form2 #server_other').removeClass('display-hide');
                        $('#relay_form2 #server_loc_txt').val(jsonvalue.form_details.server_loc_txt);
                        $('#relay_form2 #server_loc').val(jsonvalue.form_details.server_loc);
                    } else {
                        $('#relay_form2 #server_other').addClass('display-hide');
                        $('#relay_form2 #server_loc_txt').val('');
                        $('#relay_form2 #server_loc').val(jsonvalue.form_details.server_loc);
                    }


                    $('.edit').removeClass('display-hide');
                    $('#relay_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#relay_form2 :button[type='button']").removeAttr('disabled');
                    $("#relay_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }

            }, error: function ()
            {
                $('#tab1').show();
            }

        });

    });
    $('#relay_form2').submit(function (e) {
        e.preventDefault();
        $("#relay_form2 :disabled").removeAttr('disabled');
        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        var relay_ip = $("#relay_form2 input[name='relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var old_relay_ip = $("#relay_form1 input[name='old_relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');

        $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#relay_form2').serializeObject());
        $('#relay_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "relay_tab2",
            data: {data: data, relay_ip: relay_ip, old_relay_ip: old_relay_ip, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#relay_form2 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#relay_form2 #relay_app_name').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #app_name_err').html("")
                }
                if (jsonvalue.error.division_error !== null && jsonvalue.error.division_error !== "" && jsonvalue.error.division_error !== undefined)
                {
                    $('#relay_form2 #division_error').html(jsonvalue.error.division_error)
                    $('#relay_form2 #division').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #division_error').html("")
                }
                if (jsonvalue.error.os_error !== null && jsonvalue.error.os_error !== "" && jsonvalue.error.os_error !== undefined)
                {
                    $('#relay_form2 #os_error').html(jsonvalue.error.os_error)
                    $('#relay_form2 #os').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #os_error').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#relay_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#relay_form2 #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#relay_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#relay_form2 #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #server_txt_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#relay_form2 #relay_ip_err').html(jsonvalue.error.ip1_error)
                    $('#relay_form2 #relay_ip').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #relay_ip_err').html("")
                }

                if (checkDuplicates())
                {

                    $('#relay_form2 #relay_dup_ip_err').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#relay_form2 #relay_dup_ip_err').html("");

                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#relay_form2 #useremployment_error').focus();
                    $('#relay_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#relay_form2 #minerror').focus();
                    $('#relay_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#relay_form2 #deperror').focus();
                    $('#relay_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#relay_form2 #other_dept').focus();
                    $('#relay_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#relay_form2 #smierror').focus();
                    $('#relay_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#relay_form2 #state_error').focus();
                    $('#relay_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#relay_form2 #org_error').focus();
                    $('#relay_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#relay_form2 #ca_design').focus();
                    $('#relay_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#relay_form2 #hod_name').focus();
                    $('#relay_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#relay_form2 #hod_mobile').focus();
                    $('#relay_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#relay_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#relay_form2 #hod_tel').focus();
                    $('#relay_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodtel_error').html("");
                }

                if (jsonvalue.error.domain_mx_error !== null && jsonvalue.error.domain_mx_error !== "" && jsonvalue.error.domain_mx_error !== undefined)
                {
                    $('#relay_form2 #domain_mx_err').html(jsonvalue.error.domain_mx_error)
                    $('#relay_form2 #domain_mx_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #domain_mx_err').html("")
                }


                if (jsonvalue.error.total_mail_err !== null && jsonvalue.error.total_mail_err !== "" && jsonvalue.error.total_mail_err !== undefined)
                {
                    $('#relay_form2 #total_mail_err').html(jsonvalue.error.total_mail_err)
                    $('#relay_form2 #total_mail_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #total_mail_err').html("")
                }

                if (jsonvalue.error.p_name_error !== null && jsonvalue.error.p_name_error !== "" && jsonvalue.error.p_name_error !== undefined)
                {
                    $('#relay_form2 #point_name_error').focus();
                    $('#relay_form2 #point_name_error').html(jsonvalue.error.p_name_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_name_error').html("");
                }
                if (jsonvalue.error.p_email_error !== null && jsonvalue.error.p_email_error !== "" && jsonvalue.error.p_email_error !== undefined)
                {
                    $('#relay_form2 #point_email_error').focus();
                    $('#relay_form2 #point_email_error').html(jsonvalue.error.p_email_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_email_error').html("");
                }
                if (jsonvalue.error.p_mobile_error !== null && jsonvalue.error.p_mobile_error !== "" && jsonvalue.error.p_mobile_error !== undefined)
                {
                    $('#relay_form2 #point_mobile_error').html(jsonvalue.error.p_mobile_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_mobile_error').html("");
                }
                if (jsonvalue.error.p_tel_error !== null && jsonvalue.error.p_tel_error !== "" && jsonvalue.error.p_tel_error !== undefined)
                {
                    $('#relay_form2 #point_tel_error').focus();
                    $('#relay_form2 #point_tel_error').html(jsonvalue.error.p_tel_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_tel_error').html("");
                }

                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#relay_form2 #audit_date_err').focus();
                    $('#relay_form2 #audit_date_err').html(jsonvalue.error.audit_date_err);
                    error_flag = false;
                } else {
                    $('#relay_form2 #audit_date_err').html("");
                }
                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined) {
                    console.log("inside iffffffffff")
                    $('#relay_form2 #sender_id_err').html(jsonvalue.error.sender_id_error);
                    $('#relay_form2 #sender_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form1 #sender_id_err').html("");
                }


                if (jsonvalue.error.auth_id_error !== null && jsonvalue.error.auth_id_error !== "" && jsonvalue.error.auth_id_error !== undefined) {

                    $('#relay_form2 #auth_id_err').html(jsonvalue.error.auth_id_error);
                    $('#relay_form2 #auth_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #auth_id_err').html("");
                }
                // profile on 20th march
                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'relay'},
                        datatype: JSON,
                        success: function (data)
                        {
                            window.location.href = 'e_sign';
                        }, error: function ()
                        {
                            $('#tab1').show();
                        }
                    });
                } else {

                    $('#relay_form2 #domain_mx').prop('disabled', 'true');
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });

    });

    $('#relay_form2 .edit').click(function () {

        var employment = $('#relay_preview_tab #user_employment').val();
        var min = $('#relay_preview_tab #min').val();
        var dept = $('#relay_preview_tab #dept').val();
        var statecode = $('#relay_preview_tab #stateCode').val();
        var Smi = $('#relay_preview_tab #Smi').val();
        var Org = $('#relay_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#relay_preview_tab #min');
                select.find('option').remove();
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
                var select = $('#relay_preview_tab #dept');
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
                $('#relay_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#relay_preview_tab #stateCode');
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
                var select = $('#relay_preview_tab #Smi');
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
                $('#relay_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#relay_preview_tab #central_div').hide();
            $('#relay_preview_tab #state_div').hide();
            $('#relay_preview_tab #other_div').show();
            $('#relay_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#relay_preview_tab #Org');
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
                $('#relay_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#relay_preview_tab #central_div').hide();
            $('#relay_preview_tab #state_div').hide();
            $('#relay_preview_tab #other_div').hide();
        }
        $('#relay_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#relay_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#relay_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $('#relay_preview_tab #domain_mx').prop('disabled', 'true');
        $('#relay_preview_tab input[name="security_audit"]').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // $(this).hide();
    });

    $('#relay_form2 #confirm').click(function (e) {
        e.preventDefault();
        $("#relay_form2 :disabled").removeAttr('disabled');
        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        //$('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        var relay_ip = $("#relay_form2 input[name='relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var old_relay_ip = $("#relay_form2 input[name='old_relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab #user_email').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab input[name="security_audit"]').removeAttr('disabled');
        var data = JSON.stringify($('#relay_form2').serializeObject());
        $('#relay_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#relay_preview_tab input[name="security_audit"]').prop('disabled', 'true');


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18


        $.ajax({
            type: "POST",
            url: "relay_tab2",
            // data: {data: data, relay_ip: relay_ip, action_type: "validate", CSRFRandom: CSRFRandom},
            data: {data: data, relay_ip: relay_ip, old_relay_ip: old_relay_ip, action_type: "validate", CSRFRandom: CSRFRandom},

            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#relay_form2 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#relay_form2 #relay_app_name').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #app_name_err').html("")
                }
                if (jsonvalue.error.division_error !== null && jsonvalue.error.division_error !== "" && jsonvalue.error.division_error !== undefined)
                {
                    $('#relay_form2 #division_error').html(jsonvalue.error.division_error)
                    $('#relay_form2 #division').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #division_error').html("")
                }
                if (jsonvalue.error.os_error !== null && jsonvalue.error.os_error !== "" && jsonvalue.error.os_error !== undefined)
                {
                    $('#relay_form2 #os_error').html(jsonvalue.error.os_error)
                    $('#relay_form2 #os').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #os_error').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#relay_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#relay_form2 #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#relay_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#relay_form2 #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #server_txt_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#relay_form2 #relay_ip_err').html(jsonvalue.error.ip1_error)
                    $('#relay_form2 #relay_ip').focus();
                    error_flag = false;
                } else {

                    $('#relay_form2 #relay_ip_err').html("")
                }

                if (checkDuplicatesprvw())
                {

                    $('#relay_form2 #relay_dup_ip_err').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#relay_form2 #relay_dup_ip_err').html("");

                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#relay_form2 #useremployment_error').focus();
                    $('#relay_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#relay_form2 #minerror').focus();
                    $('#relay_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#relay_form2 #deperror').focus();
                    $('#relay_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#relay_form2 #other_dept').focus();
                    $('#relay_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#relay_form2 #smierror').focus();
                    $('#relay_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#relay_form2 #state_error').focus();
                    $('#relay_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#relay_form2 #org_error').focus();
                    $('#relay_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#relay_form2 #ca_design').focus();
                    $('#relay_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#relay_form2 #hod_name').focus();
                    $('#relay_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#relay_form2 #hod_mobile').focus();
                    $('#relay_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#relay_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#relay_form2 #hod_tel').focus();
                    $('#relay_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #hodtel_error').html("");
                }

                if (jsonvalue.error.domain_mx_error !== null && jsonvalue.error.domain_mx_error !== "" && jsonvalue.error.domain_mx_error !== undefined)
                {
                    $('#relay_form2 #domain_mx_err').html(jsonvalue.error.domain_mx_error)
                    $('#relay_form2 #domain_mx_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #domain_mx_err').html("")
                }


                if (jsonvalue.error.total_mail_err !== null && jsonvalue.error.total_mail_err !== "" && jsonvalue.error.total_mail_err !== undefined)
                {
                    $('#relay_form2 #total_mail_err').html(jsonvalue.error.total_mail_err)
                    $('#relay_form2 #total_mail_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #total_mail_err').html("")
                }

                if (jsonvalue.error.p_name_error !== null && jsonvalue.error.p_name_error !== "" && jsonvalue.error.p_name_error !== undefined)
                {
                    $('#relay_form2 #point_name_error').focus();
                    $('#relay_form2 #point_name_error').html(jsonvalue.error.p_name_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_name_error').html("");
                }
                if (jsonvalue.error.p_email_error !== null && jsonvalue.error.p_email_error !== "" && jsonvalue.error.p_email_error !== undefined)
                {
                    $('#relay_form2 #point_email_error').focus();
                    $('#relay_form2 #point_email_error').html(jsonvalue.error.p_email_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_email_error').html("");
                }
                if (jsonvalue.error.p_mobile_error !== null && jsonvalue.error.p_mobile_error !== "" && jsonvalue.error.p_mobile_error !== undefined)
                {
                    $('#relay_form2 #point_mobile_error').html(jsonvalue.error.p_mobile_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_mobile_error').html("");
                }
                if (jsonvalue.error.p_tel_error !== null && jsonvalue.error.p_tel_error !== "" && jsonvalue.error.p_tel_error !== undefined)
                {
                    $('#relay_form2 #point_tel_error').focus();
                    $('#relay_form2 #point_tel_error').html(jsonvalue.error.p_tel_error);
                    error_flag = false;
                } else {
                    $('#relay_form2 #point_tel_error').html("");
                }

                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#relay_form2 #audit_date_err').focus();
                    $('#relay_form2 #audit_date_err').html(jsonvalue.error.audit_date_err);
                    error_flag = false;
                } else {
                    $('#relay_form2 #audit_date_err').html("");
                }
                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined) {
                    console.log("inside iffffffffff")
                    $('#relay_form2 #sender_id_err').html(jsonvalue.error.sender_id_error);
                    $('#relay_form2 #sender_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #sender_id_err').html("");
                }

                if (jsonvalue.error.auth_id_error !== null && jsonvalue.error.auth_id_error !== "" && jsonvalue.error.auth_id_error !== undefined) {

                    $('#relay_form2 #auth_id_err').html(jsonvalue.error.auth_id_error);
                    $('#relay_form2 #auth_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_form2 #auth_id_err').html("");
                }
                // profile on 20th march
                if (!error_flag) {
                    $("#relay_form2 :disabled").removeAttr('disabled');
                    $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#relay_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#relay_preview_tab #domain_mx').prop('disabled', 'true');
                } else {
                    if ($('#tnc').is(":checked"))
                    {
                        $('#relay_form2 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});


//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#relay_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#relay_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#relay_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#relay_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#relay_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#relay_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                        
                     //   else {
                            $('#relay_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#relay_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#relay_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                      //  }
                        $('#relay_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');

                    } else {
                        $('#relay_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#relay_form2 #tnc").removeAttr('disabled');
                        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //line commented by MI on 25thjan19
                        //$('#relay_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }

            }, error: function ()
            {
                $('#tab1').show();
            }
        });


    });


    $('#relay_form_confirm #confirmYes').click(function () {
        $('#relay_form2').submit();
        $('#stack3').modal('toggle');
    });

    $('#telnet_no').click(function (e) {
        e.preventDefault();
        var data = "form_details.telnet=n";
        $.ajax({
            type: "POST",
            url: "relay_tab6",
            data: data,
            dataType: 'html',
            success: function (data) {
                $('#form_wizard_1,#form_wizard_2, #form_wizard_4').css('display', 'none');
                $('#form_wizard_3').css('display', 'block');
            },
            error: function () {
                console.log('error');
            }
        });
    });

    $('#telnet_yes').click(function (e) {
        e.preventDefault();
        var data = "form_details.telnet=y";
        $.ajax({
            type: "POST",
            url: "relay_tab6",
            data: data,
            dataType: 'html',
            success: function (data) {
                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'relay'},
                    datatype: JSON,
                    success: function (data)
                    {
                        window.location.href = 'e_sign';
                    }, error: function ()
                    {
                        $('#tab1').show();
                    }
                });
            },
            error: function () {
                console.log('error');
            }
        });
    });

    $('#firewall_no').click(function (e) {

        e.preventDefault();
        var data = "form_details.firewall_id=NA";

        $.ajax({
            type: "POST",
            url: "relay_tab7",
            data: data,
            dataType: 'html',
            success: function (data) {

                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'relay'},
                    datatype: JSON,
                    success: function (data)
                    {
                        window.location.href = 'e_sign';
                    }, error: function ()
                    {
                        $('#tab1').show();
                    }
                });
            },
            error: function () {
                console.log('error');
            }
        });
    });

    $('#firewall_yes').click(function (e) {
        var firewall_id = $('#req_id').val();
        e.preventDefault();
        var data = "form_details.firewall_id=" + firewall_id;

        $.ajax({
            type: "POST",
            url: "relay_tab7",
            data: data,
            datatype: JSON,
            success: function (data) {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.firewall_err !== null && jsonvalue.error.firewall_err !== "" && jsonvalue.error.firewall_err !== undefined)
                {
                    $('#firewall_err').html(jsonvalue.error.firewall_err)
                    $('#firewall_err').focus();
                    error_flag = false;

                } else {

                    $('#firewall_err').html("")
                }
                if (error_flag) {
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'relay'},
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
            },
            error: function () {
                console.log('error');
            }
        });
    });

    $(document).on('click', '#relay_preview .edit', function () {
        //$('#relay_preview .edit').click(function () {


        var employment = $('#relay_preview_tab #user_employment').val();
        var min = $('#relay_preview_tab #min').val();
        var dept = $('#relay_preview_tab #dept').val();
        var statecode = $('#relay_preview_tab #stateCode').val();
        var Smi = $('#relay_preview_tab #Smi').val();
        var Org = $('#relay_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#relay_preview_tab #min');
                select.find('option').remove();
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
                var select = $('#relay_preview_tab #dept');
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
                $('#relay_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#relay_preview_tab #stateCode');
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
                var select = $('#relay_preview_tab #Smi');
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
                $('#relay_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#relay_preview_tab #central_div').hide();
            $('#relay_preview_tab #state_div').hide();
            $('#relay_preview_tab #other_div').show();
            $('#relay_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#relay_preview_tab #Org');
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
                $('#relay_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#relay_preview_tab #central_div').hide();
            $('#relay_preview_tab #state_div').hide();
            $('#relay_preview_tab #other_div').hide();
        }
        $('#relay_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#relay_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#relay_preview_tab input[name="security_audit"]').prop('disabled', 'true');
        $('#relay_preview_tab #domain_mx').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#relay_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#relay_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#relay_preview #confirm', function () {
        //$('#relay_preview #confirm').click(function () {
        $('#relay_preview').submit();
        // $('#relay_preview_form').modal('toggle');
    });
    $('#relay_preview').submit(function (e) {
        e.preventDefault();
//
        var relay_ip = $("#relay_preview input[name='relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        var old_relay_ip = $("#relay_preview input[name='old_relay_ip[]']").map(function () {
            return $(this).val();
        }).get().join(';');
        $("#relay_preview :disabled").removeAttr('disabled');
        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
        $('#relay_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
        $('#relay_preview_tab #user_email').removeAttr('disabled'); // 20th march 
        $('#relay_preview_tab input[name="security_audit"]').removeAttr('disabled');
        var data = JSON.stringify($('#relay_preview').serializeObject());
        $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#relay_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');// 20th march 
        $('#relay_preview_tab input[name="security_audit"]').prop('disabled', 'true');
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "relay_tab2",
            data: {data: data, relay_ip: relay_ip, old_relay_ip: old_relay_ip, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#relay_preview #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#relay_preview #relay_app_name').focus();
                    error_flag = false;
                } else {

                    $('#relay_preview #app_name_err').html("")
                }
                if (jsonvalue.error.division_error !== null && jsonvalue.error.division_error !== "" && jsonvalue.error.division_error !== undefined)
                {
                    $('#relay_preview #division_error').html(jsonvalue.error.division_error)
                    $('#relay_preview #division').focus();
                    error_flag = false;
                } else {

                    $('#relay_preview #division_error').html("")
                }
                if (jsonvalue.error.os_error !== null && jsonvalue.error.os_error !== "" && jsonvalue.error.os_error !== undefined)
                {
                    $('#relay_preview #os_error').html(jsonvalue.error.os_error)
                    $('#relay_preview #os').focus();
                    error_flag = false;
                } else {

                    $('#relay_preview #os_error').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#relay_preview #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#relay_preview #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#relay_preview #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#relay_preview #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#relay_preview #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#relay_preview #server_txt_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#relay_preview #relay_ip_err').html(jsonvalue.error.ip1_error)
                    $('#relay_preview #relay_ip').focus();
                    error_flag = false;
                } else {

                    $('#relay_preview #relay_ip_err').html("")
                }

                if (checkDuplicatesprvw())
                {

                    $('#relay_preview #relay_dup_ip_err').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#relay_preview #relay_dup_ip_err').html("");

                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#relay_preview #useremployment_error').focus();
                    $('#relay_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#relay_preview #minerror').focus();
                    $('#relay_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#relay_preview #deperror').focus();
                    $('#relay_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#relay_preview #other_dept').focus();
                    $('#relay_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#relay_preview #smierror').focus();
                    $('#relay_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#relay_preview #state_error').focus();
                    $('#relay_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#relay_preview #org_error').focus();
                    $('#relay_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #org_error').html("");
                }
                if (jsonvalue.error.domain_mx_error !== null && jsonvalue.error.domain_mx_error !== "" && jsonvalue.error.domain_mx_error !== undefined)
                {
                    $('#relay_preview #domain_mx_err').html(jsonvalue.error.domain_mx_error)
                    $('#relay_preview #domain_mx_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_preview #domain_mx_err').html("")
                }


                if (jsonvalue.error.total_mail_err !== null && jsonvalue.error.total_mail_err !== "" && jsonvalue.error.total_mail_err !== undefined)
                {
                    $('#relay_preview #total_mail_err').html(jsonvalue.error.total_mail_err)
                    $('#relay_preview #total_mail_err').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_preview #total_mail_err').html("")
                }

                if (jsonvalue.error.p_name_error !== null && jsonvalue.error.p_name_error !== "" && jsonvalue.error.p_name_error !== undefined)
                {
                    $('#relay_preview #point_name_error').focus();
                    $('#relay_preview #point_name_error').html(jsonvalue.error.p_name_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #point_name_error').html("");
                }
                if (jsonvalue.error.p_email_error !== null && jsonvalue.error.p_email_error !== "" && jsonvalue.error.p_email_error !== undefined)
                {
                    $('#relay_preview #point_email_error').focus();
                    $('#relay_preview #point_email_error').html(jsonvalue.error.p_email_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #point_email_error').html("");
                }
                if (jsonvalue.error.p_mobile_error !== null && jsonvalue.error.p_mobile_error !== "" && jsonvalue.error.p_mobile_error !== undefined)
                {
                    $('#relay_preview #point_mobile_error').html(jsonvalue.error.p_mobile_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #point_mobile_error').html("");
                }
                if (jsonvalue.error.p_tel_error !== null && jsonvalue.error.p_tel_error !== "" && jsonvalue.error.p_tel_error !== undefined)
                {
                    $('#relay_preview #point_tel_error').focus();
                    $('#relay_preview #point_tel_error').html(jsonvalue.error.p_tel_error);
                    error_flag = false;
                } else {
                    $('#relay_preview #point_tel_error').html("");
                }

                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#relay_preview #audit_date_err').focus();
                    $('#relay_preview #audit_date_err').html(jsonvalue.error.audit_date_err);
                    error_flag = false;
                } else {
                    $('#relay_preview #audit_date_err').html("");
                }

                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined) {
                    console.log("inside iffffffffff")
                    $('#relay_preview #sender_id_err').html(jsonvalue.error.sender_id_error);
                    $('#relay_preview #sender_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_preview #sender_id_err').html("");
                }

                if (jsonvalue.error.auth_id_error !== null && jsonvalue.error.auth_id_error !== "" && jsonvalue.error.auth_id_error !== undefined) {

                    $('#relay_preview #auth_id_err').html(jsonvalue.error.auth_id_error);
                    $('#relay_preview #auth_id').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#relay_preview #auth_id_err').html("");
                }
                // profile 20th march 
                if (!error_flag)
                {

                    $("#relay_preview :disabled").removeAttr('disabled');
                    $('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#relay_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                    $('#relay_preview_tab #domain_mx').prop('disabled', 'true');

                } else {

                    $('#relay_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });

    $(document).on('click', 'input[name="mail_type_other"]', function () {

        //$('input[name="mail_type_other"]').click(function () {
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr("name");
        //console.log("field_form"+field_form+"field_name"+field_name)
        // alert($('#' + field_form + ' #mail_type_other').is(':checked'))
        if ($('#' + field_form + ' #mail_type_other').is(':checked')) {
            $("#" + field_form + " #other_mail_type").removeClass('d-none');
        } else {
            $("#" + field_form + " #other_mail_type").addClass('d-none');
        }
    });

//    $('input[name="security_audit"]').click(function () {
//        if ($(this).val() === 'Software Application') {
//            $("#security_exp_date").removeClass('d-none');
//        } else {
//            $("#security_exp_date").addClass('d-none');
//        }
//    });
});

$("input[name='req_']").click(function () {


    if ($(this).val() === "req_modify" || $(this).val() === "req_add") {
        $(".addip").attr("name", "modifyip");
        $('#newip').removeClass('col-md-10').addClass('col-md-5');
        $('#oldip').removeClass('d-none');
        $("#newip label").html("New Application IP <span style='color: red'>*</span>");
    } else {
        $(".addip").attr("name", "addip");
        $('#newip').removeClass('col-md-5').addClass('col-md-10');
        $('#oldip').addClass('d-none');
        $("#newip label").html("Application IP <span style='color: red'>*</span>");
    }
});
function backToMain() {
    $("#myModalAlert").modal('hide');
    $("#form_wizard_1").show();
    $("#form_wizard_2").hide();
    $("#form_wizard_3").hide();
    $('.integration_guideline').html("Integration Guideline");
    $('.read_instruction').html("Read Instruction");
}
function integration_guideline() {
    $("#myModalAlert").modal('hide');
    $("#form_wizard_1").hide();
    $("#form_wizard_2").show();
    $("#form_wizard_3").hide();
    $('.integration_guideline').html("Go to Relay Form");
    $('.read_instruction').html("Read Instruction");
}

function read_instruction() {
    $("#myModalAlert").modal('hide');
    $("#form_wizard_1").hide();
    $("#form_wizard_2").hide();
    $("#form_wizard_3").show();
    $('.integration_guideline').html("Integration Guideline");
    $('.read_instruction').html("Go to Relay Form");
}
;


$(document).on('blur', '#relay_form1 #relay_sender_id', function () {

    var relay_sender_id = $('#relay_sender_id').val();

    if (relay_sender_id !== "")
    {
        $.ajax({
            type: "POST",
            url: "fetchMx",
            data: {relay_sender_id: relay_sender_id}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                //$('#relay_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');

                if (jsonvalue.domainMX != null)
                {
                    $("#check-mxdomain-available input").attr('disabled', false);

                    $('#relay_form1 #domain_mx').val(jsonvalue.domainMX);

//                    if (jsonvalue.domainMX === "mailgw.nic.in")
//                    {
//
//                        $('#relay_form1 #other-record-addition').addClass('d-none')
//
//                    } else {
//                        $('#relay_form1 #other-record-addition').removeClass('d-none')
//
//                    }
                    $('#relay_form1 #domain_mx').prop('disabled', 'true');
                    $('#relay_form1 #div-mx').removeClass("d-none")
                    //$('#relay_form1 #domain-hosted-with-nic').removeClass("d-none")
                } else {
                    $("#check-mxdomain-available input").attr('disabled', true);
                    $("#check-mxdomain-available #relay_sender_id").attr('disabled', false);
                    $("#previewSubmit").prop('disabled', true);
                    $('#relay_form1 #div-mx').addClass("d-none")
                    $('#relay_form1 #domain_mx').val("");
                    //$('#relay_form1 #domain-hosted-with-nic').addClass("d-none")

                }
                if (jsonvalue.error.sender_id_error) {
                    $('#sender_id_err').html(jsonvalue.error.sender_id_error)

                } else {
                    $('#sender_id_err').html("");


                }
            }, error: function (request, error)
            {
                console.log('error');
            }

        });
    }
});
$('input[name=is_hosted]').click(function () {
    if ($(this).val().toLowerCase() === 'no') {


        bootbox.alert("Since your domain not hosted with nic please make sure you have spf,dmarc Mapped for security reasons.");
    }


})

// function checkDuplicates() {
//       
//    // get all input elements
//    var $elems = $('.class_name');
//    // we store the inputs value inside this array
//    var values = [];
//    // return this
//    var isDuplicated = false;
//    // loop through elements
//    $elems.each(function () {
//        //If value is empty then move to the next iteration.
//        if (!this.value)
//            return true;
//        //If the stored array has this value, break from the each method
//
//        if (values.indexOf(this.value) !== -1) {
//            isDuplicated = true;
//            return false;
//        }
//        // store the value
//        values.push(this.value);
//    });
//    return isDuplicated;
//}

//function checkDuplicatesprvw() {
//
//// get all input elements
//    var $elems = $('.class_name_prvw');
//    // we store the inputs value inside this array
//    var values = [];
//    // return this
//    var isDuplicated = false;
//    // loop through elements
//    $elems.each(function () {
//        //If value is empty then move to the next iteration.
//        if (!this.value)
//            return true;
//        //If the stored array has this value, break from the each method
//        if (values.indexOf(this.value) !== -1) {
//            isDuplicated = true;
//            return false;
//        }
//        // store the value
//        values.push(this.value);
//    });
//    return isDuplicated;
//}