$(document).ready(function () {
    $('input[name="ip_single_form"]').click(function () {
        var sele_data = $('input[name="ip_single_form"]:checked').val();
        console.log(sele_data)
        if (sele_data === 'single') {
            $('#add_ip_div').removeClass('display-hide');
            $('#change_wifi_div').addClass('display-hide');
        } else {
            $('#tab2').show();
            $('#add_ip_div').addClass('display-hide');
            $('#change_wifi_div').removeClass('display-hide');

        }
    })
    $(document).on('find click', '.page-wrapper button', function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        var form_tab;
        // Change ip form 
        if (field_form === "ip_form1") {
            if (field_name === 'add_ip') {
                $('#type_div').addClass('display-hide');
                $('#add_ip_div').removeClass('display-hide');
                $('#ip_text').text("Add IP Details")
            } else if (field_name === 'change_ip') {
                $('#type_div').addClass('display-hide');
                $('#change_wifi_div').removeClass('display-hide');
                $('#change_ip_form3 #sms_account').addClass('display-hide');
                $('#ip_text').text("Change IP Details")
            }
        }


    });
    //28-apr-2022
    $('.page-container-bg-solid input[value="sms"][id="req_for_sms"]').click();
    $('.page-container-bg-solid').find('input').click(function () {

        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');

        if (field_name === 'req_for' || field_name === 'req_for1') {
            var t = $(this).val();

            if (t === "sms") {
                $('#' + field_form + ' #sms_account').removeClass('display-hide');
            } else {
                $('#' + field_form + ' #sms_account').addClass('display-hide');
                $('#' + field_form + ' #account_name').val('');
            }
            if (t === "ldap") {
                $('#' + field_form + ' #ldap_account').removeClass('display-hide');
                $('#' + field_form + ' #ldap_url').removeClass('display-hide');
                $('#' + field_form + ' #ldap_id').removeClass('display-hide');
            } else {
                $('#' + field_form + ' #ldap_account').addClass('display-hide');
                $('#' + field_form + ' #ldap_url').addClass('display-hide');
                $('#' + field_form + ' #ldap_id').addClass('display-hide');
                $('#' + field_form + ' #account_name').val('');
            }
            if (t === "relay") {
                $('#' + field_form + ' #relay_app_div').removeClass('display-hide');
                $('#' + field_form + ' #relay_app_ip').removeClass('display-hide');
                $('#' + field_form + ' #server_div').removeClass('display-hide');
            } else {
                $('#' + field_form + ' #relay_app_div').addClass('display-hide');
                $('#' + field_form + ' #relay_app').val('');
                $('#' + field_form + ' #relay_app_ip').addClass('display-hide');
                $('#' + field_form + ' #relay_old_ip').val('');
                $('#' + field_form + ' #server_div').addClass('display-hide');
            }
        }

    });

    // Change ip form submission
    $('#change_ip_form2').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#change_ip_form2').serializeObject());
        // var ip_type = $('#change_ip_form2 #submit_val').val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 19thjan18

        $.ajax({
            type: "POST",
            url: "ip_tab1",
            data: {data: data, CSRFRandom: CSRFRandom}, // line modified by pr on 19thjan18
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 19thjan18
                $('#change_ip_form4 #add_ip_div').removeClass('display-hide');
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#add_ip1_error').html(jsonvalue.error.ip1_error)
                    $('#change_ip_form2 #add_ip1').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#add_ip1_error').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#add_ip2_error').html(jsonvalue.error.ip2_error)
                    $('#change_ip_form2 #add_ip2').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {

                    $('#add_ip2_error').html("")
                }
                if (jsonvalue.error.ip3_error !== null && jsonvalue.error.ip3_error !== "" && jsonvalue.error.ip3_error !== undefined)
                {
                    $('#add_ip3_error').html(jsonvalue.error.ip3_error)
                    $('#change_ip_form2 #add_ip3').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {

                    $('#add_ip3_error').html("")
                }
                if (jsonvalue.error.ip4_error !== null && jsonvalue.error.ip4_error !== "" && jsonvalue.error.ip4_error !== undefined)
                {
                    $('#add_ip4_error').html(jsonvalue.error.ip4_error)
                    $('#change_ip_form2 #add_ip4').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {

                    $('#add_ip4_error').html("")
                }

                if (jsonvalue.error.sms_acc_error !== null && jsonvalue.error.sms_acc_error !== "" && jsonvalue.error.sms_acc_error !== undefined)
                {
                    $('#change_ip_form2 #account_name_error').html(jsonvalue.error.sms_acc_error)
                    $('#change_ip_form2 #account_name').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #account_name_error').html("")
                }

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#change_ip_form2 #app_name_error').html(jsonvalue.error.app_name_error)
                    $('#change_ip_form2 #relay_app').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #app_name_error').html("")
                }
                if (jsonvalue.error.relay_app_ip_error !== null && jsonvalue.error.relay_app_ip_error !== "" && jsonvalue.error.relay_app_ip_error !== undefined)
                {
                    $('#change_ip_form2 #relay_app_ip_error').html(jsonvalue.error.relay_app_ip_error)
                    $('#change_ip_form2 #relay_old_ip').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #relay_app_ip_error').html("")
                }



                if (jsonvalue.error.ldap_acc_error !== null && jsonvalue.error.ldap_acc_error !== "" && jsonvalue.error.ldap_acc_error !== undefined)
                {
                    $('#change_ip_form2 #ldap_account_name_error').html(jsonvalue.error.ldap_acc_error)
                    $('#change_ip_form2 #ldap_account_name').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #ldap_account_name_error').html("")
                }

                if (jsonvalue.error.ldap_url_error !== null && jsonvalue.error.ldap_url_error !== "" && jsonvalue.error.ldap_url_error !== undefined)
                {
                    $('#change_ip_form2 #ldap_url_error').html(jsonvalue.error.ldap_url_error)
                    $('#change_ip_form2 #ldap_url').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #ldap_url_error').html("")
                }
                if (jsonvalue.error.ldap_alocate_error !== null && jsonvalue.error.ldap_alocate_error !== "" && jsonvalue.error.ldap_alocate_error !== undefined)
                {
                    $('#change_ip_form2 #ldap_alocate_error').html(jsonvalue.error.ldap_alocate_error)
                    $('#change_ip_form2 #ldap_auth_allocate').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #ldap_alocate_error').html("")
                }



                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#change_ip_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#change_ip_form2 #server_loc').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#change_ip_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#change_ip_form2 #server_loc_txt').focus();
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #server_txt_err').html("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#change_ip_form2 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form2 #captchaerror').html("")
                }
                if (checkDuplicates())
                {
                    $('#change_ip_form2 #ipduperr').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else
                {
                    $('#change_ip_form2 #ipduperr').html("");

                }
                // start, code added by pr on 19thjan18
                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#change_ip_form2 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#change_ip_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                }
                // end, code added by pr on 19thjan18

                if (!error_flag) {
                } else {



                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#change_ip_form4 #central_div').show();
                        $('#change_ip_form4 #state_div').hide();
                        $('#change_ip_form4 #other_div').hide();
                        $('#change_ip_form4 #other_text_div').addClass("display-hide");
                        var select = $('#change_ip_form4 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#change_ip_form4 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#change_ip_form4 #other_text_div').removeClass("display-hide");
                            $('#change_ip_form4 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#change_ip_form4 #central_div').hide();
                        $('#change_ip_form4 #state_div').show();
                        $('#change_ip_form4 #other_div').hide();
                        $('#change_ip_form4 #other_text_div').addClass("display-hide");
                        var select = $('#change_ip_form4 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#change_ip_form4 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#change_ip_form4 #other_text_div').removeClass("display-hide");
                            $('#change_ip_form4 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#change_ip_form4 #central_div').hide();
                        $('#change_ip_form4 #state_div').hide();
                        $('#change_ip_form4 #other_div').show();
                        $('#change_ip_form4 #other_text_div').addClass("display-hide");
                        var select = $('#change_ip_form4 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org === 'other') {
                            $('#change_ip_form4 #other_text_div').removeClass("display-hide");
                            $('#change_ip_form4 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#change_ip_form4 #central_div').hide();
                        $('#change_ip_form4 #state_div').hide();
                        $('#change_ip_form4 #other_div').hide();
                    }
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#change_ip_form4 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#change_ip_form4 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#change_ip_form4 #user_name').val(jsonvalue.profile_values.cn);
                    $('#change_ip_form4 #user_design').val(jsonvalue.profile_values.desig);
                    $('#change_ip_form4 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#change_ip_form4 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#change_ip_form4 #user_state').val(jsonvalue.profile_values.state);
                    $('#change_ip_form4 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#change_ip_form4 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#change_ip_form4 #user_rphone').val(jsonvalue.profile_values.rphone);
                    $('#change_ip_form4 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    // $('#change_ip_form4 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#change_ip_form4 #user_email').val(jsonvalue.profile_values.email);
                    $('#change_ip_form4 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#change_ip_form4 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#change_ip_form4 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#change_ip_form4 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#change_ip_form4 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    $('#change_ip_form4 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $("#change_ip_form4 input:radio[value='" + jsonvalue.form_details.req_for + "']").prop("checked", true);
                    if (jsonvalue.form_details.req_for === "sms")
                    {
                        $('#change_ip_form4 #sms_account').removeClass('display-hide');
                        $('#change_ip_form4 #account_name').val(jsonvalue.form_details.account_name);
                        $('#change_ip_form4 #relay_app_div').addClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #relay_app').val('');
                        $('#change_ip_form4 #relay_old_ip').val('');
                        $('#change_ip_form4 #server_div').addClass('display-hide');
                    } else if (jsonvalue.form_details.req_for === "ldap")
                    {
                        $('#change_ip_form4 #ldap_account').removeClass('display-hide');
                        $('#change_ip_form4 #ldap_url').removeClass('display-hide');
                        $('#change_ip_form4 #ldap_id').removeClass('display-hide');
                        $('#change_ip_form4 #ldap_account_name').val(jsonvalue.form_details.ldap_account_name);
                        $('#change_ip_form4 #ldap_url').val(jsonvalue.form_details.ldap_url);
                        $('#change_ip_form4 #ldap_auth_allocate').val(jsonvalue.form_details.ldap_auth_allocate);
                        $('#change_ip_form4 #relay_app_div').addClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #relay_app').val('');
                        $('#change_ip_form4 #relay_old_ip').val('');
                        $('#change_ip_form4 #server_div').addClass('display-hide');
                    } else if (jsonvalue.form_details.req_for === "relay")
                    {
                        $('#change_ip_form4 #sms_account').addClass('display-hide');
                        $('#change_ip_form4 #account_name').val("");
                        $('#change_ip_form4 #relay_app_div').removeClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').removeClass('display-hide');
                        $('#change_ip_form4 #server_div').removeClass('display-hide');
                        $('#change_ip_form4 #relay_app').val(jsonvalue.form_details.relay_app);
                        $('#change_ip_form4 #relay_old_ip').val(jsonvalue.form_details.relay_old_ip);
                        if (jsonvalue.form_details.server_loc === "Other")
                        {
                            $('#change_ip_form4 #server_other').removeClass('display-hide');
                            $('#change_ip_form4 #server_loc_txt').val(jsonvalue.form_details.server_loc_txt);
                            $('#change_ip_form4 #server_loc').val(jsonvalue.form_details.server_loc);
                        } else {
                            $('#change_ip_form4 #server_other').addClass('display-hide');
                            $('#change_ip_form4 #server_loc_txt').val('');
                            $('#change_ip_form4 #server_loc').val(jsonvalue.form_details.server_loc);
                        }
                    } else {
                        $('#change_ip_form4 #sms_account').addClass('display-hide');
                        $('#change_ip_form4 #account_name').val("");
                        $('#change_ip_form4 #relay_app_div').addClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #relay_app').val('');
                        $('#change_ip_form4 #relay_old_ip').val('');
                        $('#change_ip_form4 #server_div').addClass('display-hide');
                    }
                    $('#change_ip_form4 #add_ip1').val(jsonvalue.form_details.add_ip1);
                    $('#change_ip_form4 #add_ip2').val(jsonvalue.form_details.add_ip2);
                    $('#change_ip_form4 #add_ip3').val(jsonvalue.form_details.add_ip3);
                    $('#change_ip_form4 #add_ip4').val(jsonvalue.form_details.add_ip4);
                    $('#change_ip_form4 #confirm').val("addip");
                    $('.edit').removeClass('display-hide');
                    $('#changeip_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#change_ip_form4 :button[type='button']").removeAttr('disabled');
                    $("#change_ip_form4 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }
            }
        });
    });
    $('#change_ip_form3').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#change_ip_form3').serializeObject());
        $("#change_ip_form3 :disabled").removeAttr('disabled');
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 19thjan18

        $.ajax({
            type: "POST",
            url: "ip_tab2",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {


                resetCSRFRandom();// line added by pr on 19thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.ipold1_error !== null && jsonvalue.error.ipold1_error !== "" && jsonvalue.error.ipold1_error !== undefined)
                {
                    $('#old_ip1_error').html(jsonvalue.error.ipold1_error)
                    $('#change_ip_form3 #old_ip1').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#old_ip1_error').html("")
                }
                if (jsonvalue.error.ipnew1_error !== null && jsonvalue.error.ipnew1_error !== "" && jsonvalue.error.ipnew1_error !== undefined)
                {
                    $('#new_ip1_error').html(jsonvalue.error.ipnew1_error)
                    $('#change_ip_form3 #new_ip1').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#new_ip1_error').html("")
                }
                if (jsonvalue.error.ipold2_error !== null && jsonvalue.error.ipold2_error !== "" && jsonvalue.error.ipold2_error !== undefined)
                {
                    $('#old_ip2_error').html(jsonvalue.error.ipold2_error)
                    $('#change_ip_form3 #old_ip2').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#old_ip2_error').html("")
                }
                if (jsonvalue.error.ipnew2_error !== null && jsonvalue.error.ipnew2_error !== "" && jsonvalue.error.ipnew2_error !== undefined)
                {
                    $('#new_ip2_error').html(jsonvalue.error.ipnew2_error)
                    $('#change_ip_form3 #new_ip2').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#new_ip2_error').html("")
                }
                if (jsonvalue.error.ipold3_error !== null && jsonvalue.error.ipold3_error !== "" && jsonvalue.error.ipold3_error !== undefined)
                {
                    $('#old_ip3_error').html(jsonvalue.error.ipold3_error)
                    $('#change_ip_form3 #old_ip3').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#old_ip3_error').html("")
                }
                if (jsonvalue.error.ipnew3_error !== null && jsonvalue.error.ipnew3_error !== "" && jsonvalue.error.ipnew3_error !== undefined)
                {
                    $('#new_ip3_error').html(jsonvalue.error.ipnew3_error)
                    $('#change_ip_form3 #new_ip3').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#new_ip3_error').html("")
                }
                if (jsonvalue.error.ipold4_error !== null && jsonvalue.error.ipold4_error !== "" && jsonvalue.error.ipold4_error !== undefined)
                {
                    $('#old_ip4_error').html(jsonvalue.error.ipold4_error)
                    $('#change_ip_form3 #old_ip4').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#old_ip4_error').html("")
                }
                if (jsonvalue.error.ipnew4_error !== null && jsonvalue.error.ipnew4_error !== "" && jsonvalue.error.ipnew4_error !== undefined)
                {
                    $('#new_ip4_error').html(jsonvalue.error.ipnew4_error)
                    $('#change_ip_form3 #new_ip4').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {

                    $('#new_ip4_error').html("")
                }


                if (jsonvalue.error.ldap_acc_error !== null && jsonvalue.error.ldap_acc_error !== "" && jsonvalue.error.ldap_acc_error !== undefined)
                {
                    $('#change_ip_form3 #ldap_account_name_error').html(jsonvalue.error.ldap_acc_error)
                    $('#change_ip_form3 #ldap_account_name').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form3 #ldap_account_name_error').html("")
                }

                if (jsonvalue.error.ldap_url_error !== null && jsonvalue.error.ldap_url_error !== "" && jsonvalue.error.ldap_url_error !== undefined)
                {
                    $('#change_ip_form3 #ldap_url_error').html(jsonvalue.error.ldap_url_error)
                    $('#change_ip_form3 #ldap_url').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form3 #ldap_url_error').html("")
                }
                if (jsonvalue.error.ldap_alocate_error !== null && jsonvalue.error.ldap_alocate_error !== "" && jsonvalue.error.ldap_alocate_error !== undefined)
                {
                    $('#change_ip_form3 #ldap_alocate_error').html(jsonvalue.error.ldap_alocate_error)
                    $('#change_ip_form3 #ldap_auth_allocate').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form3 #ldap_alocate_error').html("")
                }

                if (jsonvalue.error.sms_acc_error !== null && jsonvalue.error.sms_acc_error !== "" && jsonvalue.error.sms_acc_error !== undefined)
                {
                    $('#change_ip_form3 #account_name_error').html(jsonvalue.error.sms_acc_error)
                    $('#change_ip_form3 #account_name').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {
                    $('#change_ip_form3 #account_name_error').html("")
                }
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#change_ip_form3 #app_name_error').html(jsonvalue.error.app_name_error)
                    $('#change_ip_form3 #relay_app').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {
                    $('#change_ip_form3 #app_name_error').html("")
                }
                if (jsonvalue.error.relay_app_ip_error !== null && jsonvalue.error.relay_app_ip_error !== "" && jsonvalue.error.relay_app_ip_error !== undefined)
                {
                    $('#change_ip_form3 #relay_app_ip_error').html(jsonvalue.error.relay_app_ip_error)
                    $('#change_ip_form3 #relay_old_ip').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {
                    $('#change_ip_form3 #relay_app_ip_error').html("")
                }

                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#change_ip_form3 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#change_ip_form3 #server_loc').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {
                    $('#change_ip_form3 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#change_ip_form3 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#change_ip_form3 #server_loc_txt').focus();
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {
                    $('#change_ip_form3 #server_txt_err').html("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#change_ip_form3 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else {
                    $('#change_ip_form3 #captchaerror').html("")
                }
                if (checkDuplicates())
                {

                    $('#change_ip_form3 #ipduperr').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                } else
                {
                    $('#change_ip_form3 #ipduperr').html("");
                }

                // start, code added by pr on 19thjan18
                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#change_ip_form3 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt2').val("");
                }
                // end, code added by pr on 19thjan18


                if (!error_flag) {
                } else {
                    $('#change_ip_form4 #change_ip_div').removeClass('display-hide');
                    $('#change_ip_form4 .inc_dec').hide();
                    // profile


                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#change_ip_form4 #central_div').show();
                        $('#change_ip_form4 #state_div').hide();
                        $('#change_ip_form4 #other_div').hide();
                        $('#change_ip_form4 #other_text_div').addClass("display-hide");
                        var select = $('#change_ip_form4 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#change_ip_form4 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#change_ip_form4 #other_text_div').removeClass("display-hide");
                            $('#change_ip_form4 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#change_ip_form4 #central_div').hide();
                        $('#change_ip_form4 #state_div').show();
                        $('#change_ip_form4 #other_div').hide();
                        $('#change_ip_form4 #other_text_div').addClass("display-hide");
                        var select = $('#change_ip_form4 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#change_ip_form4 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#change_ip_form4 #other_text_div').removeClass("display-hide");
                            $('#change_ip_form4 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#change_ip_form4 #central_div').hide();
                        $('#change_ip_form4 #state_div').hide();
                        $('#change_ip_form4 #other_div').show();
                        $('#change_ip_form4 #other_text_div').addClass("display-hide");
                        var select = $('#change_ip_form4 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org === 'other') {
                            $('#change_ip_form4 #other_text_div').removeClass("display-hide");
                            $('#change_ip_form4 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#change_ip_form4 #central_div').hide();
                        $('#change_ip_form4 #state_div').hide();
                        $('#change_ip_form4 #other_div').hide();
                    }
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#change_ip_form4 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#change_ip_form4 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#change_ip_form4 #user_name').val(jsonvalue.profile_values.cn);
                    $('#change_ip_form4 #user_design').val(jsonvalue.profile_values.desig);
                    $('#change_ip_form4 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#change_ip_form4 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#change_ip_form4 #user_state').val(jsonvalue.profile_values.state);
                    $('#change_ip_form4 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#change_ip_form4 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#change_ip_form4 #user_rphone').val(jsonvalue.profile_values.rphone);
                    $('#change_ip_form4 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#change_ip_form4 #user_email').val(jsonvalue.profile_values.email);
                    $('#change_ip_4 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#change_ip_form4 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#change_ip_form4 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#change_ip_form4 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#change_ip_form4 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    //$('#change_ip_form4 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#change_ip_form4 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#change_ip_form4 #confirm').val("changeip");
                    $("#change_ip_form4 input:radio[value='" + jsonvalue.form_details.req_for + "']").prop("checked", true);


                    if (jsonvalue.form_details.req_for === "sms")
                    {
                        $('#change_ip_form4 #sms_account').removeClass('display-hide');
                        $('#change_ip_form4 #account_name').val(jsonvalue.form_details.account_name);
                        $('#change_ip_form4 #relay_app_div').addClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #relay_app').val('');
                        $('#change_ip_form4 #relay_old_ip').val('');
                        $('#change_ip_form4 #server_div').addClass('display-hide');
                    } else if (jsonvalue.form_details.req_for === "ldap")
                    {
                        $('#change_ip_form4 #ldap_account').removeClass('display-hide');
                        $('#change_ip_form4 #ldap_url').removeClass('display-hide');
                        $('#change_ip_form4 #ldap_id').removeClass('display-hide');
                        $('#change_ip_form4 #ldap_account_name').val(jsonvalue.form_details.ldap_account_name);
                        $('#change_ip_form4 #ldap_url').val(jsonvalue.form_details.ldap_url);
                        $('#change_ip_form4 #ldap_auth_allocate').val(jsonvalue.form_details.ldap_auth_allocate);
                        $('#change_ip_form4 #relay_app_div').addClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #relay_app').val('');
                        $('#change_ip_form4 #relay_old_ip').val('');
                        $('#change_ip_form4 #server_div').addClass('display-hide');
                    } else if (jsonvalue.form_details.req_for === "relay")
                    {
                        $('#change_ip_form4 #sms_account').addClass('display-hide');
                        $('#change_ip_form4 #account_name').val("");
                        $('#change_ip_form4 #relay_app_div').removeClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #server_div').removeClass('display-hide');
                        $('#change_ip_form4 #relay_app').val(jsonvalue.form_details.relay_app);
                        $('#change_ip_form4 #relay_old_ip').val(jsonvalue.form_details.relay_old_ip);
                        if (jsonvalue.form_details.server_loc === "Other")
                        {
                            $('#change_ip_form4 #server_other').removeClass('display-hide');
                            $('#change_ip_form4 #server_loc_txt').val(jsonvalue.form_details.server_loc_txt);
                            $('#change_ip_form4 #server_loc').val(jsonvalue.form_details.server_loc);
                        } else {
                            $('#change_ip_form4 #server_other').addClass('display-hide');
                            $('#change_ip_form4 #server_loc_txt').val('');
                            $('#change_ip_form4 #server_loc').val(jsonvalue.form_details.server_loc);
                        }
                    } else {
                        $('#change_ip_form4 #sms_account').addClass('display-hide');
                        $('#change_ip_form4 #account_name').val("");
                        $('#change_ip_form4 #relay_app_div').addClass('display-hide');
                        $('#change_ip_form4 #relay_app_ip').addClass('display-hide');
                        $('#change_ip_form4 #relay_app').val('');
                        $('#change_ip_form4 #relay_old_ip').val('');
                        $('#change_ip_form4 #server_div').addClass('display-hide');
                    }


                    $('#change_ip_form4 #old_ip1').val(jsonvalue.form_details.old_ip1);
                    $('#change_ip_form4 #new_ip1').val(jsonvalue.form_details.new_ip1);

                    if (jsonvalue.form_details.old_ip2 !== "" && jsonvalue.form_details.new_ip2 !== "")
                    {
                        $('#change_ip_form4 #newdiv1ad1').show();
                        $('#change_ip_form4 #old_ip2').val(jsonvalue.form_details.old_ip2);
                        $('#change_ip_form4 #new_ip2').val(jsonvalue.form_details.new_ip2);
                    } else {
                        $('#change_ip_form4 #newdiv1ad1').hide();
                        $('#change_ip_form4 #old_ip2').val("");
                        $('#change_ip_form4 #new_ip2').val("");
                    }
                    if (jsonvalue.form_details.old_ip3 !== "" && jsonvalue.form_details.new_ip3 !== "")
                    {
                        $('#change_ip_form4 #newdiv2ad2').show();
                        $('#change_ip_form4 #old_ip3').val(jsonvalue.form_details.old_ip3);
                        $('#change_ip_form4 #new_ip3').val(jsonvalue.form_details.new_ip3);
                    } else {
                        $('#change_ip_form4 #newdiv2ad2').hide();
                        $('#change_ip_form4 #old_ip3').val("");
                        $('#change_ip_form4 #new_ip3').val("");
                    }
                    if (jsonvalue.form_details.old_ip4 !== "" && jsonvalue.form_details.new_ip4 !== "")
                    {
                        $('#change_ip_form4 #newdiv3ad3').show();
                        $('#change_ip_form4 #old_ip4').val(jsonvalue.form_details.old_ip4);
                        $('#change_ip_form4 #new_ip4').val(jsonvalue.form_details.new_ip4);
                    } else {
                        $('#change_ip_form4 #newdiv3ad3').hide();
                        $('#change_ip_form4 #old_ip4').val("");
                        $('#change_ip_form4 #new_ip4').val("");
                    }

                    $('.edit').removeClass('display-hide');
                    $('#changeip_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#change_ip_form4 :button[type='button']").removeAttr('disabled');
                    $("#change_ip_form4 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }
            }, error: function ()
            {
                console.log('error');
            }
        });
    });

    $('#change_ip_form4').submit(function (e) {
        e.preventDefault();
        $("#change_ip_form4 :disabled").removeAttr('disabled');
        $('#changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#changeip_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#changeip_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#change_ip_form4').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 11thjan18  
        $('#changeip_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        $.ajax({
            type: "POST",
            url: "ip_tab3",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom}, // line modified by pr on 22ndjan18
            datatype: JSON,
            success: function (data)
            {


                resetCSRFRandom();// line added by pr on 19thjan18

                // start, code added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip1_error').html(jsonvalue.error.ip1_error)
                    $('#change_ip_form4 #add_ip1').focus();
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #add_ip1_error').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip2_error').html(jsonvalue.error.ip2_error)
                    $('#change_ip_form4 #add_ip2').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #add_ip2_error').html("")
                }
                if (jsonvalue.error.ip3_error !== null && jsonvalue.error.ip3_error !== "" && jsonvalue.error.ip3_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip3_error').html(jsonvalue.error.ip3_error)
                    $('#change_ip_form4 #add_ip3').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #add_ip3_error').html("")
                }
                if (jsonvalue.error.ip4_error !== null && jsonvalue.error.ip4_error !== "" && jsonvalue.error.ip4_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip4_error').html(jsonvalue.error.ip4_error)
                    $('#change_ip_form4 #add_ip4').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #add_ip4_error').html("")
                }


                if (checkDuplicatesprvw())
                {
                    $('#change_ip_form4 #ipduperr').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#change_ip_form4 #ipduperr').html("");

                }
                if (jsonvalue.error.ipold1_error !== null && jsonvalue.error.ipold1_error !== "" && jsonvalue.error.ipold1_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip1_error').html(jsonvalue.error.ipold1_error)
                    $('#change_ip_form4 #old_ip1').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip1_error').html("")
                }
                if (jsonvalue.error.ipnew1_error !== null && jsonvalue.error.ipnew1_error !== "" && jsonvalue.error.ipnew1_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip1_error').html(jsonvalue.error.ipnew1_error)
                    $('#change_ip_form4 #new_ip1').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip1_error').html("")
                }
                if (jsonvalue.error.ipold2_error !== null && jsonvalue.error.ipold2_error !== "" && jsonvalue.error.ipold2_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip2_error').html(jsonvalue.error.ipold2_error)
                    $('#change_ip_form4 #old_ip2').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip2_error').html("")
                }
                if (jsonvalue.error.ipnew2_error !== null && jsonvalue.error.ipnew2_error !== "" && jsonvalue.error.ipnew2_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip2_error').html(jsonvalue.error.ipnew2_error)
                    $('#change_ip_form4 #new_ip2').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip2_error').html("")
                }
                if (jsonvalue.error.ipold3_error !== null && jsonvalue.error.ipold3_error !== "" && jsonvalue.error.ipold3_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip3_error').html(jsonvalue.error.ipold3_error)
                    $('#change_ip_form4 #old_ip3').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip3_error').html("")
                }
                if (jsonvalue.error.ipnew3_error !== null && jsonvalue.error.ipnew3_error !== "" && jsonvalue.error.ipnew3_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip3_error').html(jsonvalue.error.ipnew3_error)
                    $('#change_ip_form4 #new_ip3').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip3_error').html("")
                }
                if (jsonvalue.error.ipold4_error !== null && jsonvalue.error.ipold4_error !== "" && jsonvalue.error.ipold4_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip4_error').html(jsonvalue.error.ipold4_error)
                    $('#change_ip_form4 #old_ip4').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip4_error').html("")
                }
                if (jsonvalue.error.ipnew4_error !== null && jsonvalue.error.ipnew4_error !== "" && jsonvalue.error.ipnew4_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip4_error').html(jsonvalue.error.ipnew4_error)
                    $('#change_ip_form4 #new_ip4').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip4_error').html("")
                }




                if (jsonvalue.error.ldap_acc_error !== null && jsonvalue.error.ldap_acc_error !== "" && jsonvalue.error.ldap_acc_error !== undefined)
                {
                    $('#change_ip_form4 #ldap_account_name_error').html(jsonvalue.error.ldap_acc_error)
                    $('#change_ip_form4 #ldap_account_name').focus();
                    error_flag = false;
                    $('#change_ip_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form4 #ldap_account_name_error').html("")
                }

                if (jsonvalue.error.ldap_url_error !== null && jsonvalue.error.ldap_url_error !== "" && jsonvalue.error.ldap_url_error !== undefined)
                {
                    $('#change_ip_form4 #ldap_url_error').html(jsonvalue.error.ldap_url_error)
                    $('#change_ip_form4 #ldap_url').focus();
                    error_flag = false;
                    $('#change_ip_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form4 #ldap_url_error').html("")
                }
                if (jsonvalue.error.ldap_alocate_error !== null && jsonvalue.error.ldap_alocate_error !== "" && jsonvalue.error.ldap_alocate_error !== undefined)
                {
                    $('#change_ip_form4 #ldap_alocate_error').html(jsonvalue.error.ldap_alocate_error)
                    $('#change_ip_form4 #ldap_auth_allocate').focus();
                    error_flag = false;
                    $('#change_ip_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form4 #ldap_alocate_error').html("")
                }

                if (jsonvalue.error.sms_acc_error !== null && jsonvalue.error.sms_acc_error !== "" && jsonvalue.error.sms_acc_error !== undefined)
                {
                    $('#change_ip_form4 #account_name_error').html(jsonvalue.error.sms_acc_error)
                    $('#change_ip_form4 #account_name').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #account_name_error').html("")
                }


                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#change_ip_form4 #app_name_error').html(jsonvalue.error.app_name_error)
                    $('#change_ip_form4 #relay_app').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #app_name_error').html("")
                }
                if (jsonvalue.error.relay_app_ip_error !== null && jsonvalue.error.relay_app_ip_error !== "" && jsonvalue.error.relay_app_ip_error !== undefined)
                {
                    $('#change_ip_form4 #relay_app_ip_error').html(jsonvalue.error.relay_app_ip_error)
                    $('#change_ip_form4 #relay_old_ip').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #relay_app_ip_error').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#change_ip_form4 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#change_ip_form4 #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#change_ip_form4 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#change_ip_form4 #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #server_txt_err').html("")
                }
                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#change_ip_form4 #tnc_error').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#change_ip_form4 #useremployment_error').focus();
                    $('#change_ip_form4 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#change_ip_form4 #minerror').focus();
                    $('#change_ip_form4 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#change_ip_form4 #deperror').focus();
                    $('#change_ip_form4 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#change_ip_form4 #other_dept').focus();
                    $('#change_ip_form4 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#change_ip_form4 #smierror').focus();
                    $('#change_ip_form4 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#change_ip_form4 #state_error').focus();
                    $('#change_ip_form4 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#change_ip_form4 #org_error').focus();
                    $('#change_ip_form4 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {

                    $('#change_ip_form4 #ca_design').focus();
                    $('#change_ip_form4 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {

                    $('#change_ip_form4 #hod_name').focus();
                    $('#change_ip_form4 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#change_ip_form4 #hod_mobile').focus();
                    $('#change_ip_form4 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#change_ip_form4 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {

                    $('#change_ip_form4 #hod_tel').focus();
                    $('#change_ip_form4 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #hodtel_error').html("");
                }
                // profile on 20th march
                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'ip'},
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
    $('#change_ip_form4 .edit').click(function () {

        var employment = $('#changeip_preview_tab #user_employment').val();
        var min = $('#changeip_preview_tab #min').val();
        var dept = $('#changeip_preview_tab #dept').val();
        var statecode = $('#changeip_preview_tab #stateCode').val();
        var Smi = $('#changeip_preview_tab #Smi').val();
        var Org = $('#changeip_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#min');
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
                var select = $('#dept');
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
            if (Smi === 'other') {
                $('#other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#stateCode');
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
                var select = $('#Smi');
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
            if (dept === 'other') {
                $('#other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#Org');
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
                $('#other_text_div').removeClass("display-hide");

            }


        } else {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').hide();
        }
        $('#changeip_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#changeip_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#changeip_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#changeip_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $(this).addClass('display-hide');
    });

    $('#changeip_form_confirm #confirmYes').click(function () {
        $('#change_ip_form4').submit();
        $('#stack3').modal('toggle');

    });

    $('#change_ip_form4 #confirm').click(function (e) {
        e.preventDefault();
        $("#change_ip_form4 :disabled").removeAttr('disabled');
        $('#changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#changeip_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#changeip_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#change_ip_form4').serializeObject());
        $('#changeip_preview_tab #user_email').prop('disabled', 'true');


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 11thjan18  

        $.ajax({
            type: "POST",
            url: "ip_tab3",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom}, // line added by pr on 22ndjan18
            datatype: JSON,
            success: function (data)
            {

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#change_ip_form4 #app_name_error').html(jsonvalue.error.app_name_error)
                    $('#change_ip_form4 #relay_app').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #app_name_error').html("")
                }
                if (jsonvalue.error.relay_app_ip_error !== null && jsonvalue.error.relay_app_ip_error !== "" && jsonvalue.error.relay_app_ip_error !== undefined)
                {
                    $('#change_ip_form4 #relay_app_ip_error').html(jsonvalue.error.relay_app_ip_error)
                    $('#change_ip_form4 #relay_old_ip').focus();
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #relay_app_ip_error').html("")
                }

                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#change_ip_form4 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#change_ip_form4 #server_loc').focus();
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#change_ip_form4 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#change_ip_form4 #server_loc_txt').focus();
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #server_txt_err').html("")
                }

                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip1_error').html(jsonvalue.error.ip1_error)
                    $('#change_ip_form4 #add_ip1').focus();
                    error_flag = false;

                } else {
                    $('#change_ip_form4 #add_ip1_error').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip2_error').html(jsonvalue.error.ip2_error)
                    $('#change_ip_form4 #add_ip2').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #add_ip2_error').html("")
                }
                if (jsonvalue.error.ip3_error !== null && jsonvalue.error.ip3_error !== "" && jsonvalue.error.ip3_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip3_error').html(jsonvalue.error.ip3_error)
                    $('#change_ip_form4 #add_ip3').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #add_ip3_error').html("")
                }
                if (jsonvalue.error.ip4_error !== null && jsonvalue.error.ip4_error !== "" && jsonvalue.error.ip4_error !== undefined)
                {
                    $('#change_ip_form4 #add_ip4_error').html(jsonvalue.error.ip4_error)
                    $('#change_ip_form4 #add_ip4').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #add_ip4_error').html("")
                }
                if (checkDuplicatesprvw())
                {
                    $('#change_ip_form4 #ipduperr').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#change_ip_form4 #ipduperr').html("");
                }
                if (jsonvalue.error.ipold1_error !== null && jsonvalue.error.ipold1_error !== "" && jsonvalue.error.ipold1_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip1_error').html(jsonvalue.error.ipold1_error)
                    $('#change_ip_form4 #old_ip1').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip1_error').html("")
                }
                if (jsonvalue.error.ipnew1_error !== null && jsonvalue.error.ipnew1_error !== "" && jsonvalue.error.ipnew1_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip1_error').html(jsonvalue.error.ipnew1_error)
                    $('#change_ip_form4 #new_ip1').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip1_error').html("")
                }
                if (jsonvalue.error.ipold2_error !== null && jsonvalue.error.ipold2_error !== "" && jsonvalue.error.ipold2_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip2_error').html(jsonvalue.error.ipold2_error)
                    $('#change_ip_form4 #old_ip2').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip2_error').html("")
                }
                if (jsonvalue.error.ipnew2_error !== null && jsonvalue.error.ipnew2_error !== "" && jsonvalue.error.ipnew2_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip2_error').html(jsonvalue.error.ipnew2_error)
                    $('#change_ip_form4 #new_ip2').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip2_error').html("")
                }
                if (jsonvalue.error.ipold3_error !== null && jsonvalue.error.ipold3_error !== "" && jsonvalue.error.ipold3_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip3_error').html(jsonvalue.error.ipold3_error)
                    $('#change_ip_form4 #old_ip3').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip3_error').html("")
                }
                if (jsonvalue.error.ipnew3_error !== null && jsonvalue.error.ipnew3_error !== "" && jsonvalue.error.ipnew3_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip3_error').html(jsonvalue.error.ipnew3_error)
                    $('#change_ip_form4 #new_ip3').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip3_error').html("")
                }
                if (jsonvalue.error.ipold4_error !== null && jsonvalue.error.ipold4_error !== "" && jsonvalue.error.ipold4_error !== undefined)
                {
                    $('#change_ip_form4 #old_ip4_error').html(jsonvalue.error.ipold4_error)
                    $('#change_ip_form4 #old_ip4').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #old_ip4_error').html("")
                }
                if (jsonvalue.error.ipnew4_error !== null && jsonvalue.error.ipnew4_error !== "" && jsonvalue.error.ipnew4_error !== undefined)
                {
                    $('#change_ip_form4 #new_ip4_error').html(jsonvalue.error.ipnew4_error)
                    $('#change_ip_form4 #new_ip4').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #new_ip4_error').html("")
                }
                if (jsonvalue.error.sms_acc_error !== null && jsonvalue.error.sms_acc_error !== "" && jsonvalue.error.sms_acc_error !== undefined)
                {
                    $('#change_ip_form4 #account_name_error').html(jsonvalue.error.sms_acc_error)
                    $('#change_ip_form4 #account_name').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #account_name_error').html("")
                }

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#change_ip_form4 #app_name_error').html(jsonvalue.error.app_name_error)
                    $('#change_ip_form4 #relay_app').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #app_name_error').html("")
                }
                if (jsonvalue.error.relay_app_ip_error !== null && jsonvalue.error.relay_app_ip_error !== "" && jsonvalue.error.relay_app_ip_error !== undefined)
                {
                    $('#change_ip_form4 #relay_app_ip_error').html(jsonvalue.error.relay_app_ip_error)
                    $('#change_ip_form4 #relay_old_ip').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #relay_app_ip_error').html("")
                }


                if (jsonvalue.error.ldap_acc_error !== null && jsonvalue.error.ldap_acc_error !== "" && jsonvalue.error.ldap_acc_error !== undefined)
                {
                    $('#change_ip_form4 #ldap_account_name_error').html(jsonvalue.error.ldap_acc_error)
                    $('#change_ip_form4 #ldap_account_name').focus();
                    error_flag = false;
                    $('#change_ip_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form4 #ldap_account_name_error').html("")
                }

                if (jsonvalue.error.ldap_url_error !== null && jsonvalue.error.ldap_url_error !== "" && jsonvalue.error.ldap_url_error !== undefined)
                {
                    $('#change_ip_form4 #ldap_url_error').html(jsonvalue.error.ldap_url_error)
                    $('#change_ip_form4 #ldap_url').focus();
                    error_flag = false;
                    $('#change_ip_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form4 #ldap_url_error').html("")
                }
                if (jsonvalue.error.ldap_alocate_error !== null && jsonvalue.error.ldap_alocate_error !== "" && jsonvalue.error.ldap_alocate_error !== undefined)
                {
                    $('#change_ip_form4 #ldap_alocate_error').html(jsonvalue.error.ldap_alocate_error)
                    $('#change_ip_form4 #ldap_auth_allocate').focus();
                    error_flag = false;
                    $('#change_ip_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#change_ip_form4 #ldap_alocate_error').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#change_ip_form4 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#change_ip_form4 #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#change_ip_form4 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#change_ip_form4 #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #server_txt_err').html("")
                }
                // end, code added by pr on 22ndjan18

                // profile 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#change_ip_form4 #useremployment_error').focus();
                    $('#change_ip_form4 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#change_ip_form4 #minerror').focus();
                    $('#change_ip_form4 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#change_ip_form4 #deperror').focus();
                    $('#change_ip_form4 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#change_ip_form4 #other_dept').focus();
                    $('#change_ip_form4 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#change_ip_form4 #smierror').focus();
                    $('#change_ip_form4 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#change_ip_form4 #state_error').focus();
                    $('#change_ip_form4 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#change_ip_form4 #org_error').focus();
                    $('#change_ip_form4 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {


                    $('#change_ip_form4 #ca_design').focus();
                    $('#change_ip_form4 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {

                    $('#change_ip_form4 #hod_name').focus();
                    $('#change_ip_form4 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {

                    $('#change_ip_form4 #hod_mobile').focus();
                    $('#change_ip_form4 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#change_ip_form4 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {

                    $('#change_ip_form4 #hod_tel').focus();
                    $('#change_ip_form4 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#change_ip_form4 #hodtel_error').html("");
                }

                // profile 20th march



                if (!error_flag) {
                    $("#change_ip_form4 :disabled").removeAttr('disabled');
                    $('#changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#changeip_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#change_ip_form4 #tnc').is(":checked"))
                    {
                        $('#change_ip_form4 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});

                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
                        {
                            $('#changeip_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#changeip_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#changeip_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);


                        } else if (jsonvalue.form_details.min == "External Affairs")
                        {
                            $('#changeip_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
                            $('#changeip_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
                            $('#changeip_form_confirm #fill_hod_mobile').html("+919384664224");
                        } else {
                            $('#changeip_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#changeip_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#changeip_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        }
                        $('#changeip_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');

                    } else {
                        $('#change_ip_form4 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        // $('#changeip_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#change_ip_form4 #tnc").removeAttr('disabled');
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $(document).on('click', '#ip_preview .edit', function () {
        //$('#ip_preview .edit').click(function () {

        var employment = $('#ip_preview #user_employment').val();
        var min = $('#ip_preview #min').val();
        var dept = $('#ip_preview #dept').val();
        var statecode = $('#ip_preview #stateCode').val();
        var Smi = $('#ip_preview #Smi').val();
        var Org = $('#ip_preview #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ip_preview #min');
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
                var select = $('#ip_preview #dept');
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
                $('#ip_preview #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ip_preview #stateCode');
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
                var select = $('#ip_preview #Smi');
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
            if (dept === 'other') {
                $('#ip_preview #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ip_preview #Org');
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
                $('#ip_preview #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#ip_preview #central_div').hide();
            $('#ip_preview #state_div').hide();
            $('#ip_preview #other_div').hide();
        }


        $('#ip_preview #changeip_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#ip_preview #changeip_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#ip_preview #changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ip_preview #changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#ip_preview .edit').addClass('display-hide');
        // $(this).hide();
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#ip_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#ip_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#ip_preview #confirm', function () {
        //$('#ip_preview #confirm').click(function () {
        $('#ip_preview').submit();
        // $('#ip_preview_form').modal('toggle');
    });

    $('#ip_preview').submit(function (e) {
        e.preventDefault();

        $("#ip_preview :disabled").removeAttr('disabled');
        $('#ip_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ip_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march    
        $('#ip_preview #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march // nikki 22 jan 2019   
        $('#ip_preview #user_email').removeAttr('disabled');// 20th march  
        var data = JSON.stringify($('#ip_preview').serializeObject());
        $('#ip_preview #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 jan 2019
        $('#ip_preview #user_email').prop('disabled', 'true'); // 20th march


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "ip_tab3",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#ip_preview #add_ip1_error').html(jsonvalue.error.ip1_error)
                    $('#ip_preview #add_ip1').focus();
                    error_flag = false;

                } else {
                    $('#ip_preview #add_ip1_error').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#ip_preview #add_ip2_error').html(jsonvalue.error.ip2_error)
                    $('#ip_preview #add_ip2').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #add_ip2_error').html("")
                }
                if (jsonvalue.error.ip3_error !== null && jsonvalue.error.ip3_error !== "" && jsonvalue.error.ip3_error !== undefined)
                {
                    $('#ip_preview #add_ip3_error').html(jsonvalue.error.ip3_error)
                    $('#ip_preview #add_ip3').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #add_ip3_error').html("")
                }
                if (jsonvalue.error.ip4_error !== null && jsonvalue.error.ip4_error !== "" && jsonvalue.error.ip4_error !== undefined)
                {

                    $('#ip_preview #add_ip4_error').html(jsonvalue.error.ip4_error);
                    $('#ip_preview #add_ip4_error').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #add_ip4_error').html("");
                }


                if (checkDuplicatesprvw())
                {
                    $('#ip_preview #ipduperr').html("You entered duplicate ip address please correct and enter diffrent ip address");
                    error_flag = false;
                } else
                {
                    $('#ip_preview #ipduperr').html("");

                }
                if (jsonvalue.error.ipold1_error !== null && jsonvalue.error.ipold1_error !== "" && jsonvalue.error.ipold1_error !== undefined)
                {
                    $('#ip_preview #old_ip1_error').html(jsonvalue.error.ipold1_error)
                    $('#ip_preview #old_ip1').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #old_ip1_error').html("")
                }
                if (jsonvalue.error.ipnew1_error !== null && jsonvalue.error.ipnew1_error !== "" && jsonvalue.error.ipnew1_error !== undefined)
                {
                    $('#ip_preview #new_ip1_error').html(jsonvalue.error.ipnew1_error)
                    $('#ip_preview #new_ip1').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #new_ip1_error').html("")
                }
                if (jsonvalue.error.ipold2_error !== null && jsonvalue.error.ipold2_error !== "" && jsonvalue.error.ipold2_error !== undefined)
                {
                    $('#ip_preview #old_ip2_error').html(jsonvalue.error.ipold2_error)
                    $('#ip_preview #old_ip2').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #old_ip2_error').html("")
                }
                if (jsonvalue.error.ipnew2_error !== null && jsonvalue.error.ipnew2_error !== "" && jsonvalue.error.ipnew2_error !== undefined)
                {
                    $('#ip_preview #new_ip2_error').html(jsonvalue.error.ipnew2_error)
                    $('#ip_preview #new_ip2').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #new_ip2_error').html("")
                }
                if (jsonvalue.error.ipold3_error !== null && jsonvalue.error.ipold3_error !== "" && jsonvalue.error.ipold3_error !== undefined)
                {
                    $('#ip_preview #old_ip3_error').html(jsonvalue.error.ipold3_error)
                    $('#ip_preview #old_ip3').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #old_ip3_error').html("")
                }
                if (jsonvalue.error.ipnew3_error !== null && jsonvalue.error.ipnew3_error !== "" && jsonvalue.error.ipnew3_error !== undefined)
                {
                    $('#ip_preview #new_ip3_error').html(jsonvalue.error.ipnew3_error)
                    $('#ip_preview #new_ip3').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #new_ip3_error').html("")
                }
                if (jsonvalue.error.ipold4_error !== null && jsonvalue.error.ipold4_error !== "" && jsonvalue.error.ipold4_error !== undefined)
                {
                    $('#ip_preview #old_ip4_error').html(jsonvalue.error.ipold4_error)
                    $('#ip_preview #old_ip4').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #old_ip4_error').html("")
                }
                if (jsonvalue.error.ipnew4_error !== null && jsonvalue.error.ipnew4_error !== "" && jsonvalue.error.ipnew4_error !== undefined)
                {
                    $('#ip_preview #new_ip4_error').html(jsonvalue.error.ipnew4_error)
                    $('#ip_preview #new_ip4').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #new_ip4_error').html("")
                }
                if (jsonvalue.error.sms_acc_error !== null && jsonvalue.error.sms_acc_error !== "" && jsonvalue.error.sms_acc_error !== undefined)
                {
                    $('#ip_preview #account_name_error').html(jsonvalue.error.sms_acc_error)
                    $('#ip_preview #account_name').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #account_name_error').html("")
                }

                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#ip_preview #app_name_error').html(jsonvalue.error.app_name_error)
                    $('#ip_preview #relay_app').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #app_name_error').html("")
                }
                if (jsonvalue.error.relay_app_ip_error !== null && jsonvalue.error.relay_app_ip_error !== "" && jsonvalue.error.relay_app_ip_error !== undefined)
                {
                    $('#ip_preview #relay_app_ip_error').html(jsonvalue.error.relay_app_ip_error)
                    $('#ip_preview #relay_old_ip').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #relay_app_ip_error').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#ip_preview #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#ip_preview #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #server_loc_err').html("")
                }


                if (jsonvalue.error.ldap_acc_error !== null && jsonvalue.error.ldap_acc_error !== "" && jsonvalue.error.ldap_acc_error !== undefined)
                {
                    $('#ip_preview #ldap_account_name_error').html(jsonvalue.error.ldap_acc_error)
                    $('#ip_preview #ldap_account_name').focus();
                    error_flag = false;
                    $('#ip_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#ip_preview #ldap_account_name_error').html("")
                }

                if (jsonvalue.error.ldap_url_error !== null && jsonvalue.error.ldap_url_error !== "" && jsonvalue.error.ldap_url_error !== undefined)
                {
                    $('#ip_preview #ldap_url_error').html(jsonvalue.error.ldap_url_error)
                    $('#ip_preview #ldap_url').focus();
                    error_flag = false;
                    $('#ip_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#ip_preview #ldap_url_error').html("")
                }
                if (jsonvalue.error.ldap_alocate_error !== null && jsonvalue.error.ldap_alocate_error !== "" && jsonvalue.error.ldap_alocate_error !== undefined)
                {
                    $('#ip_preview #ldap_alocate_error').html(jsonvalue.error.ldap_alocate_error)
                    $('#ip_preview #ldap_auth_allocate').focus();
                    error_flag = false;
                    $('#ip_preview #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt1').val("");
                } else {
                    $('#ip_preview #ldap_alocate_error').html("")
                }


                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#ip_preview #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#ip_preview #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#ip_preview #server_txt_err').html("")
                }
                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#ip_preview #tnc_error').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#ip_preview #useremployment_error').focus();
                    $('#ip_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#ip_preview #minerror').focus();
                    $('#ip_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#ip_preview #deperror').focus();
                    $('#ip_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#ip_preview #other_dept').focus();
                    $('#ip_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#ip_preview #smierror').focus();
                    $('#ip_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#ip_preview #state_error').focus();
                    $('#ip_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#ip_preview #org_error').focus();
                    $('#ip_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#ip_preview #org_error').html("");
                }
                // profile 20th march 
                if (!error_flag)
                {

                    $("#ip_preview :disabled").removeAttr('disabled');
                    $('#changeip_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#changeip_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#ip_preview_form').modal('toggle');
                }


            }, error: function ()
            {
                console.log('error');
            }
        });

    });

});
function show2rowchild()
{

    $('#newdiv1ad1').show();
    $('#addremdi2').show();
    $('#addremdi1').hide();
}

function show3rowchild()
{
    $('#newdiv2ad2').show();
    $('#addremdi2').hide();
    $('#addremdi3').show();
}

function show4rowchild()
{
    $('#newdiv3ad3').show();
    $('#addremdi3').hide();
    $('#addremdi4').show();
}

function hide4rowchild()
{
    $('#old_ip4').val("");
    $('#new_ip4').val("");
    $('#addremdi4').hide();
    $('#newdiv3ad3').hide();
    $('#addremdi3').show();
}

function hide3rowchild()
{
    $('#old_ip3').val("");
    $('#new_ip3').val("");
    $('#addremdi3').hide();
    $('#newdiv2ad2').hide();
    $('#addremdi2').show();
}

function hide2rowchild()
{
    $('#old_ip2').val("");
    $('#new_ip2').val("");
    $('#addremdi2').hide();
    $('#newdiv1ad1').hide();
    $('#addremdi1').show();
}
