/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
// Ldap user submission
    $('#mD2').click(function () {
        $('.modal').scrollTop(0);
//        $('#fD2').focus();
    });
    $('.page-wrapper').find('input').click(function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        if (field_name === 'https') {

            if ($('#' + field_form + ' #https_2').is(':checked')) {
                $('#' + field_form + ' #cert1_div').addClass('display-hide');
                $('#' + field_form + ' #submit_div').addClass('display-hide');
                $('#' + field_form + ' #note_div').removeClass('display-hide');
                $('#' + field_form + ' #note_div').text('NOTE: Application should be enabled over https');
                $('#' + field_form + ' #no_audit_div').addClass('display-hide');
            } else {
                $('#' + field_form + ' #cert1_div').removeClass('display-hide');
                $('#' + field_form + ' #submit_div').removeClass('display-hide');
                $('#' + field_form + ' #note_div').addClass('display-hide');
                $('#' + field_form + ' #note_div').text('');
                if ($('#' + field_form + ' #audit_2').is(':checked')) {
                    $('#' + field_form + ' #submit_div').removeClass('display-hide');
                    $('#' + field_form + ' #cert1_div').addClass('display-hide');
                    $('#' + field_form + ' #no_audit_div').removeClass('display-hide');
                    $('#' + field_form + ' #note_div').removeClass('display-hide');
                    $('#' + field_form + ' #note_div').text('NOTE: The LDAP certificate would be available for 1 month, that too for specific id\'s only.');
                }
            }
        }
        if (field_name === 'audit') {
            if ($('#' + field_form + ' #https_2').is(':checked')) {
                $('#' + field_form + ' #cert1_div').addClass('display-hide');
                $('#' + field_form + ' #submit_div').addClass('display-hide');
                $('#' + field_form + ' #note_div').removeClass('display-hide');
                $('#' + field_form + ' #note_div').text('NOTE: Application should be enabled over https');
            } else {
                if ($('#' + field_form + ' #audit_2').is(':checked')) {
                    $('#' + field_form + ' #submit_div').removeClass('display-hide');
                    $('#' + field_form + ' #cert1_div').addClass('display-hide');
                    $('#' + field_form + ' #no_audit_div').removeClass('display-hide');
                    $('#' + field_form + ' #note_div').removeClass('display-hide');
                    $('#' + field_form + ' #note_div').text('NOTE: The LDAP certificate would be available for 1 month, that too for specific id\'s only.');
                } else {
                    $('#' + field_form + ' #submit_div').removeClass('display-hide');
                    $('#' + field_form + ' #cert1_div').removeClass('display-hide');
                    $('#' + field_form + ' #note_div').addClass('display-hide');
                    $('#' + field_form + ' #no_audit_div').addClass('display-hide');
                    $('#' + field_form + ' #note_div').text('');
                }
            }
        }
    });
    $('#ldap_form1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#ldap_form1').serializeObject());
        var cert = $('#cert').val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "ldap_tab1",
            data: {data: data, cert: cert, CSRFRandom: CSRFRandom}, // line modified by pr on 22ndjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#ldap_form1 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#ldap_form1 #app_name').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#ldap_form1 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#ldap_form1 #app_url_err').html(jsonvalue.error.app_url_error)
                    $('#ldap_form1 #app_url').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#ldap_form1 #app_url_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#ldap_form1 #base_ip_err').html(jsonvalue.error.ip1_error)
                    $('#ldap_form1 #base_ip').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#ldap_form1 #base_ip_err').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#ldap_form1 #service_ip_err').html(jsonvalue.error.ip2_error)
                    $('#ldap_form1 #service_ip').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#ldap_form1 #service_ip_err').html("")
                }
                if (jsonvalue.error.domain_error !== null && jsonvalue.error.domain_error !== "" && jsonvalue.error.domain_error !== undefined)
                {
                    $('#ldap_form1 #domain_err').html(jsonvalue.error.domain_error)
                    $('#ldap_form1 #domain').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form1 #domain_err').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#ldap_form1 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#ldap_form1 #server_loc').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#ldap_form1 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#ldap_form1 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#ldap_form1 #server_loc_txt').focus();
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {

                    $('#ldap_form1 #server_txt_err').html("")
                }

                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                {
                    $('#ldap_form1 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#ldap_form1 #file_err').html("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#ldap_form1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#ldap_form1 #captchaerror').html("")
                }

                if (jsonvalue.error.audit_err !== null && jsonvalue.error.audit_err !== "" && jsonvalue.error.audit_err !== undefined)
                {
                    $('#ldap_form1 #audit_err').html(jsonvalue.error.audit_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#ldap_form1 #audit_err').html("")
                }
                if (jsonvalue.error.https_err !== null && jsonvalue.error.https_err !== "" && jsonvalue.error.https_err !== undefined)
                {
                    $('#ldap_form1 #https_err').html(jsonvalue.error.https_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#ldap_form1 #https_err').html("")
                }
                if (jsonvalue.error.specific_id1_err !== null && jsonvalue.error.specific_id1_err !== "" && jsonvalue.error.specific_id1_err !== undefined)
                {
                    $('#ldap_form1 #specific_id1_err').html(jsonvalue.error.specific_id1_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#ldap_form1 #specific_id1_err').html("")
                }
                if (jsonvalue.error.specific_id2_err !== null && jsonvalue.error.specific_id2_err !== "" && jsonvalue.error.specific_id2_err !== undefined)
                {
                    $('#ldap_form1 #specific_id2_err').html(jsonvalue.error.specific_id2_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#ldap_form1 #specific_id2_err').html("")
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

                } else {

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
                    $('#ldap_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#ldap_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#ldap_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#ldap_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#ldap_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#ldap_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#ldap_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#ldap_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#ldap_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#ldap_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#ldap_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    //$('#ldap_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#ldap_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#ldap_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#ldap_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#ldap_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#ldap_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#ldap_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    //$('#ldap_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#ldap_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#ldap_form2 #app_name').val(jsonvalue.form_details.app_name);
                    $('#ldap_form2 #app_url').val(jsonvalue.form_details.app_url);
                    $('#ldap_form2 #base_ip').val(jsonvalue.form_details.base_ip);
                    $('#ldap_form2 #service_ip').val(jsonvalue.form_details.service_ip);
                    $('#ldap_form2 #domain').val(jsonvalue.form_details.domain);
                    if (jsonvalue.form_details.server_loc === "Other")
                    {
                        $('#ldap_form2 #server_other').removeClass('display-hide');
                        $('#ldap_form2 #server_loc_txt').val(jsonvalue.form_details.server_loc_txt);
                        $('#ldap_form2 #server_loc').val(jsonvalue.form_details.server_loc);
                    } else {
                        $('#ldap_form2 #server_other').addClass('display-hide');
                        $('#ldap_form2 #server_loc_txt').val('');
                        $('#ldap_form2 #server_loc').val(jsonvalue.form_details.server_loc);
                    }

                    if (jsonvalue.form_details.https === 'yes') {
                        $("#ldap_form2 #https_1").prop('checked', true);
                    } else {
                        $("#ldap_form2 #https_2").prop('checked', true);
                    }

                    if (jsonvalue.form_details.audit === 'yes') {
                        $("#ldap_form2 #audit_1").prop('checked', true);
                        $('#ldap_form2 #cert1_div').removeClass('display-hide');
                        $('#ldap_form2 #no_audit_div').addClass('display-hide');
                        $('#ldap_form2 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);
                    } else {
                        $("#ldap_form2 #audit_2").prop('checked', true);
                        $('#ldap_form2 #cert1_div').addClass('display-hide');
                        $('#ldap_form2 #no_audit_div').removeClass('display-hide');
                        $('#ldap_form2 #upload_ldap').addClass('display-hide');
                        $('#ldap_form2 #ldap_id1').val(jsonvalue.form_details.ldap_id1);
                        $('#ldap_form2 #ldap_id2').val(jsonvalue.form_details.ldap_id2);
                    }

                    $('.edit').removeClass('display-hide');
                    $('#ldap_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#ldap_form2 :button[type='button']").removeAttr('disabled');
                    $("#ldap_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }
            }, error: function ()
            {
                console.log('error');
            }
        });
    });
    $('#ldap_form2').submit(function (e) {
        e.preventDefault();
        $("#ldap_form2 :disabled").removeAttr('disabled');
        $('#ldap_form2 #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_form2 #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#ldap_form2 #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#ldap_form2 #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#ldap_form2').serializeObject());
        $('#ldap_form2 #user_email').prop('disabled', 'true'); // 20th march


        var cert = $('#ldap_form2 #cert').val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('#ldap_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "ldap_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom}, // line modified by pr on 22ndjan18
            datatype: JSON,
            success: function (data)
            {

                // below if else added by pr on 22ndjan18

                resetCSRFRandom(); // line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#ldap_form2 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#ldap_form2 #app_name').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#ldap_form2 #app_url_err').html(jsonvalue.error.app_url_error)
                    $('#ldap_form2 #app_url').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #app_url_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#ldap_form2 #base_ip_err').html(jsonvalue.error.ip1_error)
                    $('#ldap_form2 #base_ip').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #base_ip_err').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#ldap_form2 #service_ip_err').html(jsonvalue.error.ip2_error)
                    $('#ldap_form2 #service_ip').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #service_ip_err').html("")
                }
                if (jsonvalue.error.domain_error !== null && jsonvalue.error.domain_error !== "" && jsonvalue.error.domain_error !== undefined)
                {
                    $('#ldap_form2 #domain_err').html(jsonvalue.error.domain_error)
                    $('#ldap_form2 #domain').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #domain_err').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#ldap_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#ldap_form2 #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#ldap_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#ldap_form2 #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #server_txt_err').html("")
                }
                if (jsonvalue.error.audit_err !== null && jsonvalue.error.audit_err !== "" && jsonvalue.error.audit_err !== undefined)
                {
                    $('#ldap_form2 #audit_err').html(jsonvalue.error.audit_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #audit_err').html("")
                }
                if (jsonvalue.error.https_err !== null && jsonvalue.error.https_err !== "" && jsonvalue.error.https_err !== undefined)
                {
                    $('#ldap_form2 #https_err').html(jsonvalue.error.https_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #https_err').html("")
                }
                if (jsonvalue.error.specific_id1_err !== null && jsonvalue.error.specific_id1_err !== "" && jsonvalue.error.specific_id1_err !== undefined)
                {
                    $('#ldap_form2 #specific_id1_err').html(jsonvalue.error.specific_id1_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #specific_id1_err').html("")
                }
                if (jsonvalue.error.specific_id2_err !== null && jsonvalue.error.specific_id2_err !== "" && jsonvalue.error.specific_id2_err !== undefined)
                {
                    $('#ldap_form2 #specific_id2_err').html(jsonvalue.error.specific_id2_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #specific_id2_err').html("")
                }
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#ldap_form2 #useremployment_error').focus();
                    $('#ldap_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#ldap_form2 #minerror').focus();
                    $('#ldap_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#ldap_form2 #deperror').focus();
                    $('#ldap_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#ldap_form2 #other_dept').focus();
                    $('#ldap_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#ldap_form2 #smierror').focus();
                    $('#ldap_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#ldap_form2 #state_error').focus();
                    $('#ldap_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#ldap_form2 #org_error').focus();
                    $('#ldap_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#ldap_form2 #ca_design').focus();
                    $('#ldap_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#ldap_form2 #hod_name').focus();
                    $('#ldap_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#ldap_form2 #hod_mobile').focus();
                    $('#ldap_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#ldap_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#ldap_form2 #hod_tel').focus();
                    $('#ldap_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodtel_error').html("");
                }
                // profile on 20th march
                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'ldap'},
                        datatype: JSON,
                        success: function (data)
                        {
                            window.location.href = 'e_sign';
                        }, error: function ()
                        {
                            $('#tab1').show();
                        }
                    });
//                    $('#form_wizard_1').hide();
//                    $('#form_wizard_2').show();
//                    $('#form_wizard_3').hide();
//                    $('#form_wizard_4').hide();
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });
    $('#telnet_no').click(function (e) {
        e.preventDefault();
        var data = "form_details.telnet=n";
        $.ajax({
            type: "POST",
            url: "ldap_tab3",
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
            url: "ldap_tab3",
            data: data,
            dataType: 'html',
            success: function (data) {

                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'ldap'},
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
            url: "ldap_tab4",
            data: data,
            dataType: 'html',
            success: function (data) {
                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'ldap'},
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
            url: "ldap_tab4",
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
                        data: {formtype: 'ldap'},
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
    $('#ldap_form2 .edit').click(function () {
        var employment = $('#ldap_preview_tab #user_employment').val();
        var min = $('#ldap_preview_tab #min').val();
        var dept = $('#ldap_preview_tab #dept').val();
        var statecode = $('#ldap_preview_tab #stateCode').val();
        var Smi = $('#ldap_preview_tab #Smi').val();
        var Org = $('#ldap_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ldap_preview_tab #min');
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
                var select = $('#ldap_preview_tab #dept');
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
                $('#ldap_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ldap_preview_tab #stateCode');
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
                var select = $('#ldap_preview_tab #Smi');
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
                $('#ldap_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project" ) {
            $('#ldap_preview_tab #central_div').hide();
            $('#ldap_preview_tab #state_div').hide();
            $('#ldap_preview_tab #other_div').show();
            $('#ldap_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ldap_preview_tab #Org');
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
                $('#ldap_preview_tab #other_text_div').removeClass("display-hide");
            }


        } else {
            $('#ldap_preview_tab #central_div').hide();
            $('#ldap_preview_tab #state_div').hide();
            $('#ldap_preview_tab #other_div').hide();
        }

        $('#ldap_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#ldap_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #ldap_https').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #ldap_audit').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#ldap_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $(this).addClass('display-hide');
        //$(this).hide();
    });
    $('#ldap_form2 #confirm').click(function (e) {
        e.preventDefault();
        $("#ldap_form2 :disabled").removeAttr('disabled');
        $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_form2 #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#ldap_form2 #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#ldap_form2 #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#ldap_form2').serializeObject());
        $('#ldap_form2 #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "ldap_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom}, // line modified by pr on 22ndjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 22ndjan18


                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#ldap_form2 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#ldap_form2 #app_name').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#ldap_form2 #app_url_err').html(jsonvalue.error.app_url_error)
                    $('#ldap_form2 #app_url').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #app_url_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#ldap_form2 #base_ip_err').html(jsonvalue.error.ip1_error)
                    $('#ldap_form2 #base_ip').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #base_ip_err').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#ldap_form2 #service_ip_err').html(jsonvalue.error.ip2_error)
                    $('#ldap_form2 #service_ip').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #service_ip_err').html("")
                }
                if (jsonvalue.error.domain_error !== null && jsonvalue.error.domain_error !== "" && jsonvalue.error.domain_error !== undefined)
                {
                    $('#ldap_form2 #domain_err').html(jsonvalue.error.domain_error)
                    $('#ldap_form2 #domain').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #domain_err').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#ldap_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#ldap_form2 #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#ldap_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#ldap_form2 #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#ldap_form2 #server_txt_err').html("")
                }
                if (jsonvalue.error.audit_err !== null && jsonvalue.error.audit_err !== "" && jsonvalue.error.audit_err !== undefined)
                {
                    $('#ldap_form2 #audit_err').html(jsonvalue.error.audit_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #audit_err').html("")
                }
                if (jsonvalue.error.https_err !== null && jsonvalue.error.https_err !== "" && jsonvalue.error.https_err !== undefined)
                {
                    $('#ldap_form2 #https_err').html(jsonvalue.error.https_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #https_err').html("")
                }
                if (jsonvalue.error.specific_id1_err !== null && jsonvalue.error.specific_id1_err !== "" && jsonvalue.error.specific_id1_err !== undefined)
                {
                    $('#ldap_form2 #specific_id1_err').html(jsonvalue.error.specific_id1_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #specific_id1_err').html("")
                }
                if (jsonvalue.error.specific_id2_err !== null && jsonvalue.error.specific_id2_err !== "" && jsonvalue.error.specific_id2_err !== undefined)
                {
                    $('#ldap_form2 #specific_id2_err').html(jsonvalue.error.specific_id2_err)
                    error_flag = false;
                } else {
                    $('#ldap_form2 #specific_id2_err').html("")
                }
                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18

                // profile 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#ldap_form2 #useremployment_error').focus();
                    $('#ldap_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#ldap_form2 #minerror').focus();
                    $('#ldap_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#ldap_form2 #deperror').focus();
                    $('#ldap_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#ldap_form2 #other_dept').focus();
                    $('#ldap_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#ldap_form2 #smierror').focus();
                    $('#ldap_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#ldap_form2 #state_error').focus();
                    $('#ldap_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#ldap_form2 #org_error').focus();
                    $('#ldap_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#ldap_form2 #ca_design').focus();
                    $('#ldap_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#ldap_form2 #hod_name').focus();
                    $('#ldap_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#ldap_form2 #hod_mobile').focus();
                    $('#ldap_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#ldap_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#ldap_form2 #hod_tel').focus();
                    $('#ldap_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#ldap_form2 #hodtel_error').html("");
                }

                // profile 20th march


                if (!error_flag) {
                    $("#ldap_form2 :disabled").removeAttr('disabled');
                    $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#ldap_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    // show modal
                    if ($('#ldap_form2 #tnc').is(":checked"))
                    {
                        $('#ldap_form2 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                       
                         
//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#ldap_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#ldap_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#ldap_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#ldap_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#ldap_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#ldap_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                       // else {
                            $('#ldap_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#ldap_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#ldap_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                      //  }
//                        $('#ldap_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                        $('#ldap_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                        $('#ldap_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        $('#ldap_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#ldap_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#ldap_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //line commented by MI on 25thjan19
                        // $('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#ldap_form2 #tnc").removeAttr('disabled');
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });
    $('#ldap_form_confirm #confirmYes').click(function () {
        $('#ldap_form2').submit();
        $('#stack3').modal('toggle');
    });

    $(document).on('click', '#ldap_preview .edit', function () {
        // $('#ldap_preview .edit').click(function () {

        var employment = $('#ldap_preview_tab #user_employment').val();
        var min = $('#ldap_preview_tab #min').val();
        var dept = $('#ldap_preview_tab #dept').val();
        var statecode = $('#ldap_preview_tab #stateCode').val();
        var Smi = $('#ldap_preview_tab #Smi').val();
        var Org = $('#ldap_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ldap_preview_tab #min');
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
                var select = $('#ldap_preview_tab #dept');
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
                $('#ldap_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ldap_preview_tab #stateCode');
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
                var select = $('#ldap_preview_tab #Smi');
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
                $('#ldap_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project" ) {
            $('#ldap_preview_tab #central_div').hide();
            $('#ldap_preview_tab #state_div').hide();
            $('#ldap_preview_tab #other_div').show();
            $('#ldap_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#ldap_preview_tab #Org');
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
                $('#ldap_preview_tab #other_text_div').removeClass("display-hide");
            }


        } else {
            $('#ldap_preview_tab #central_div').hide();
            $('#ldap_preview_tab #state_div').hide();
            $('#ldap_preview_tab #other_div').hide();
        }
        $('#ldap_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#ldap_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #ldap_https').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #ldap_audit').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#ldap_preview .save-changes").removeClass('display-hide'); // line added by pr on 25thjan18
            $("#ldap_preview .save-changes").html("Update");
        }
// code end

    });

    $(document).on('click', '#ldap_preview #confirm', function () {
        // $('#ldap_preview #confirm').click(function () {
        $('#ldap_preview').submit();
        // $('#ldap_preview_form').modal('toggle');
    });
    $('#ldap_preview').submit(function (e) {
        e.preventDefault();
        $("#ldap_preview :disabled").removeAttr('disabled');
        $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#ldap_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march   
        $('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march // nikki 22 jan 2019   
        $('#ldap_preview_tab #user_email').removeAttr('disabled'); // 20th march  
        var data = JSON.stringify($('#ldap_preview').serializeObject());
        $('#ldap_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 jan 2019
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        console.log('Ldap DATA inside submit preview: ' + data)
        $.ajax({
            type: "POST",
            url: "ldap_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom(); // line added by pr on 22ndjan18


                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#ldap_preview #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#ldap_preview #app_name').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#ldap_preview #app_url_err').html(jsonvalue.error.app_url_error)
                    $('#ldap_preview #app_url').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #app_url_err').html("")
                }
                if (jsonvalue.error.ip1_error !== null && jsonvalue.error.ip1_error !== "" && jsonvalue.error.ip1_error !== undefined)
                {
                    $('#ldap_preview #base_ip_err').html(jsonvalue.error.ip1_error)
                    $('#ldap_preview #base_ip').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #base_ip_err').html("")
                }
                if (jsonvalue.error.ip2_error !== null && jsonvalue.error.ip2_error !== "" && jsonvalue.error.ip2_error !== undefined)
                {
                    $('#ldap_preview #service_ip_err').html(jsonvalue.error.ip2_error)
                    $('#ldap_preview #service_ip').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #service_ip_err').html("")
                }
                if (jsonvalue.error.domain_error !== null && jsonvalue.error.domain_error !== "" && jsonvalue.error.domain_error !== undefined)
                {
                    $('#ldap_preview #domain_err').html(jsonvalue.error.domain_error)
                    $('#ldap_preview #domain').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #domain_err').html("")
                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#ldap_preview #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#ldap_preview #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#ldap_preview #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#ldap_preview #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#ldap_preview #server_txt_err').html("")
                }
                if (jsonvalue.error.audit_err !== null && jsonvalue.error.audit_err !== "" && jsonvalue.error.audit_err !== undefined)
                {
                    $('#ldap_preview #audit_err').html(jsonvalue.error.audit_err)
                    error_flag = false;
                } else {
                    $('#ldap_preview #audit_err').html("")
                }
                if (jsonvalue.error.https_err !== null && jsonvalue.error.https_err !== "" && jsonvalue.error.https_err !== undefined)
                {
                    $('#ldap_preview #https_err').html(jsonvalue.error.https_err)
                    error_flag = false;
                } else {
                    $('#ldap_preview #https_err').html("")
                }
                if (jsonvalue.error.specific_id1_err !== null && jsonvalue.error.specific_id1_err !== "" && jsonvalue.error.specific_id1_err !== undefined)
                {
                    $('#ldap_preview #specific_id1_err').html(jsonvalue.error.specific_id1_err)
                    error_flag = false;
                } else {
                    $('#ldap_preview #specific_id1_err').html("")
                }
                if (jsonvalue.error.specific_id2_err !== null && jsonvalue.error.specific_id2_err !== "" && jsonvalue.error.specific_id2_err !== undefined)
                {
                    $('#ldap_preview #specific_id2_err').html(jsonvalue.error.specific_id2_err)
                    error_flag = false;
                } else {
                    $('#ldap_preview #specific_id2_err').html("")
                }
                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#ldap_preview #useremployment_error').focus();
                    $('#ldap_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#ldap_preview #minerror').focus();
                    $('#ldap_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#ldap_preview #deperror').focus();
                    $('#ldap_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#ldap_preview #other_dept').focus();
                    $('#ldap_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#ldap_preview #smierror').focus();
                    $('#ldap_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#ldap_preview #state_error').focus();
                    $('#ldap_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#ldap_preview #org_error').focus();
                    $('#ldap_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#ldap_preview #org_error').html("");
                }
                // profile 20th march 

                if (!error_flag)
                {

                    $("#ldap_preview :disabled").removeAttr('disabled');
                    $('#ldap_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#ldap_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#ldap_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });
    $(document).on('blur', '#app_url', function (e) {
        e.preventDefault();
        var app_url = $('#app_url').val();
        if (app_url != "")
        {

            $.ajax({
                type: "POST",
                url: "verify_url",
                data: {app_url: app_url},
                datatype: JSON,
                success: function (data)
                {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    if (jsonvalue.returnString === 'audit') {
                        $("#ldap_form1 #audit_1").prop('checked', true);
                        $('#ldap_form1 #cert1_div').removeClass('display-hide');
                        $('#ldap_form1 #no_audit_div').addClass('display-hide');
                    } else {

                        $("#ldap_form1 #audit_2").prop('checked', true);
                        $('#ldap_form1 #cert1_div').addClass('display-hide');
                        $('#ldap_form1 #no_audit_div').removeClass('display-hide');
                        $('#ldap_form1 #upload_ldap').addClass('display-hide');
                    }

                }, error: function ()
                {
                    console.log("error in new code ")
                }
            })
        }

    });
    $("#refresh").on("click", function () {
        $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
    });
    $("#closebtn").on("click", function () {
        $('#captcha').attr('src', 'Captcha?var=<%=random%>' + Date.parse(new Date().toString()));
        $('#imgtxt').val("");
    });
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
};