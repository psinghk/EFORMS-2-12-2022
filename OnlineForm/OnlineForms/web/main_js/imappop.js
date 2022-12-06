/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {

    // Imap pop submission
    $('#imappop_form1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#imappop_form1').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val();

        $.ajax({
            type: "POST",
            url: "imappop_tab1",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.protocol_err !== null && jsonvalue.error.protocol_err !== "" && jsonvalue.error.protocol_err !== undefined)
                {
                    $('#protocol_err').html(jsonvalue.error.protocol_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#protocol_err').html("");
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                }
                if (jsonvalue.error.service_pop_err !== null && jsonvalue.error.service_pop_err !== "" && jsonvalue.error.service_pop_err !== undefined)
                {
                    $('#service_pop_err').html(jsonvalue.error.service_pop_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#service_pop_err').html("");
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                }

                if (jsonvalue.error.service_imap_err !== null && jsonvalue.error.service_imap_err !== "" && jsonvalue.error.service_imap_err !== undefined)
                {
                    $('#service_imap_err').html(jsonvalue.error.service_imap_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#service_imap_err').html("");
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
                        $('#imappop_form2 #central_div').show();
                        $('#imappop_form2 #state_div').hide();
                        $('#imappop_form2 #other_div').hide();
                        $('#imappop_form2 #other_text_div').addClass("display-hide");
                        var select = $('#imappop_form2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#imappop_form2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#imappop_form2 #other_text_div').removeClass("display-hide");
                            $('#imappop_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#imappop_form2 #central_div').hide();
                        $('#imappop_form2 #state_div').show();
                        $('#imappop_form2 #other_div').hide();
                        $('#imappop_form2 #other_text_div').addClass("display-hide");
                        var select = $('#imappop_form2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#imappop_form2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#imappop_form2 #other_text_div').removeClass("display-hide");
                            $('#imappop_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#imappop_form2 #central_div').hide();
                        $('#imappop_form2 #state_div').hide();
                        $('#imappop_form2 #other_div').show();
                        $('#imappop_form2 #other_text_div').addClass("display-hide");
                        var select = $('#imappop_form2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        // alert(jsonvalue.profile_values.Org.toString().toLowerCase())

                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#imappop_form2 #other_text_div').removeClass("display-hide");
                            $('#imappop_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#imappop_form2 #central_div').hide();
                        $('#imappop_form2 #state_div').hide();
                        $('#imappop_form2 #other_div').hide();
                    }

                    $('#imappop_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#imappop_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#imappop_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#imappop_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#imappop_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#imappop_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#imappop_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#imappop_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#imappop_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#imappop_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#imappop_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#imappop_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#imappop_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#imappop_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#imappop_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#imappop_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#imappop_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);

                    $('#imappop_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    $('#imappop_form2 #ca_design').val(jsonvalue.profile_values.ca_design);

                    if (jsonvalue.form_details.protocol === 'imap')
                    {
                        $('#imappop_form2 #protocol_imap').prop('checked', true);
                    } else {
                        $('#imappop_form2 #protocol_pop').prop('checked', true);
                    }

                    $('.edit').removeClass('display-hide');
                    $('#imappop_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#imappop_form2 :button[type='button']").removeAttr('disabled');
                    $("#imappop_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }

            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $('#imappop_form2').submit(function (e) {

        e.preventDefault();
        $("#imappop_form2 :disabled").removeAttr('disabled');
        $('#imappop_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#imappop_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#imappop_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#imappop_form2').serializeObject());
        $('#imappop_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        // start, code added by pr on 11thjan18

        //resetCSRFRandom();

        var CSRFRandom = $("#CSRFRandom").val();

        $('#imappop_form2 .edit').removeClass('display-hide');

        // end, code added by pr on 11thjan18       
        $.ajax({
            type: "POST",
            url: "imappop_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom}, // line added by pr on 10thjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.protocol_err !== null && jsonvalue.error.protocol_err !== "" && jsonvalue.error.protocol_err !== undefined)
                {
                    $('#imappop_form2 #protocol_err').html(jsonvalue.error.protocol_err)
                    $('#imappop_form2 #protocol_err').focus();
                    error_flag = false;
                } else {

                    $('#imappop_form2 #protocol_err').html("")
                }
                if (jsonvalue.error.service_pop_err !== null && jsonvalue.error.service_pop_err !== "" && jsonvalue.error.service_pop_err !== undefined)
                {
                    $('#imappop_form2 #service_pop_err').html(jsonvalue.error.service_pop_err)
                    error_flag = false;
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#imappop_form2 #service_pop_err').html("");
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imappop_form2 #imgtxt').val("")
                }

                if (jsonvalue.error.service_imap_err !== null && jsonvalue.error.service_imap_err !== "" && jsonvalue.error.service_imap_err !== undefined)
                {
                    $('#imappop_form2 #service_imap_err').html(jsonvalue.error.service_imap_err)
                    error_flag = false;
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#imappop_form2 #service_imap_err').html("");
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imappop_form2 #imgtxt').val("")
                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#imappop_form2 #useremployment_error').focus();
                    $('#imappop_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#imappop_form2 #minerror').focus();
                    $('#imappop_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#imappop_form2 #deperror').focus();
                    $('#imappop_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#imappop_form2 #other_dept').focus();
                    $('#imappop_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#imappop_form2 #smierror').focus();
                    $('#imappop_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#imappop_form2 #state_error').focus();
                    $('#imappop_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#imappop_form2 #org_error').focus();
                    $('#imappop_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#imappop_form2 #ca_design').focus();
                    $('#imappop_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#imappop_form2 #hod_name').focus();
                    $('#imappop_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#imappop_form2 #hod_mobile').focus();
                    $('#imappop_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#imappop_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#imappop_form2 #hod_tel').focus();
                    $('#imappop_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodtel_error').html("");
                }
                // profile on 20th march

                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'imappop'},
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

    $('#imappop_form2 .edit').click(function () {
        var employment = $('#imappop_form2 #user_employment').val();
        var min = $('#imappop_form2 #min').val();
        var dept = $('#imappop_form2 #dept').val();
        var statecode = $('#imappop_form2 #stateCode').val();
        var Smi = $('#imappop_form2 #Smi').val();
        var Org = $('#imappop_form2 #Org').val();

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
    });

    $('#imappop_form2 #confirm').click(function (e) {

        e.preventDefault();
        $("#imappop_form2 :disabled").removeAttr('disabled');
        $('#imappop_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#imappop_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#imappop_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#imappop_form2').serializeObject());
        $('#imappop_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 19thjan18

        $.ajax({
            type: "POST",
            url: "imappop_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom}, // line added by pr on 10thjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.protocol_err !== null && jsonvalue.error.protocol_err !== "" && jsonvalue.error.protocol_err !== undefined)
                {
                    $('#imappop_form2 #protocol_err').html(jsonvalue.error.protocol_err)
                    $('#imappop_form2 #protocol_err').focus();
                    error_flag = false;
                } else {

                    $('#imappop_form2 #protocol_err').html("")
                }
                if (jsonvalue.error.service_pop_err !== null && jsonvalue.error.service_pop_err !== "" && jsonvalue.error.service_pop_err !== undefined)
                {
                    $('#imappop_form2 #service_pop_err').html(jsonvalue.error.service_pop_err)
                    error_flag = false;
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#imappop_form2 #service_pop_err').html("");
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imappop_form2 #imgtxt').val("")
                }

                if (jsonvalue.error.service_imap_err !== null && jsonvalue.error.service_imap_err !== "" && jsonvalue.error.service_imap_err !== undefined)
                {
                    $('#imappop_form2 #service_imap_err').html(jsonvalue.error.service_imap_err)
                    error_flag = false;
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));

                } else {
                    $('#imappop_form2 #service_imap_err').html("");
                    $('#imappop_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imappop_form2 #imgtxt').val("")
                }
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#imappop_form2 #useremployment_error').focus();
                    $('#imappop_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#imappop_form2 #minerror').focus();
                    $('#imappop_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#imappop_form2 #deperror').focus();
                    $('#imappop_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#imappop_form2 #other_dept').focus();
                    $('#imappop_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#imappop_form2 #smierror').focus();
                    $('#imappop_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#imappop_form2 #state_error').focus();
                    $('#imappop_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#imappop_form2 #org_error').focus();
                    $('#imappop_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#imappop_form2 #ca_design').focus();
                    $('#imappop_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#imappop_form2 #hod_name').focus();
                    $('#imappop_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#imappop_form2 #hod_mobile').focus();
                    $('#imappop_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#imappop_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#imappop_form2 #hod_tel').focus();
                    $('#imappop_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#imappop_form2 #hodtel_error').html("");
                }
                // profile on 20th march
                console.log("error: " + error_flag)
                if (!error_flag) {
                    $("#imappop_form2 :disabled").removeAttr('disabled');
                    $('#imappop_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#imappop_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#imappop_form2 #tnc').is(":checked"))
                    {

                        $('#imappop_form2 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);

//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#imappop_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#imappop_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#imappop_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//
//                            $('#imappop_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#imappop_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#imappop_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                     //   else {
                            $('#imappop_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#imappop_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#imappop_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                      //  }

                        $('#imappop_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');

                    } else {
                        $('#imappop_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#imappop_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        // $('#imappop_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#imappop_form2 #tnc").removeAttr('disabled');
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $('#imappop_form_confirm #confirmYes').click(function () {
        $('#imappop_form2').submit();
        $('#stack3').modal('toggle');
    });



    $(document).on('click', '#imappop_preview .edit', function () {
        //$('#imappop_preview .edit').click(function () {
        console.log('LIT');
        var employment = $('#imappop_preview_tab #user_employment').val();
        var min = $('#imappop_preview_tab #min').val();
        var dept = $('#imappop_preview_tab #dept').val();
        var statecode = $('#imappop_preview_tab #stateCode').val();
        var Smi = $('#imappop_preview_tab #Smi').val();
        var Org = $('#imappop_preview_tab #Org').val();

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
        $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#imappop_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        //console.log($(this))
        $(this).addClass('display-hide');
//        $(this).hide();
// below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#imappop_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#imappop_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#imappop_preview #confirm', function () {
        //$('#imappop_preview #confirm').click(function () {

        $('#imappop_preview').submit();
        // $('#imappop_preview_form').modal('toggle');
    });

    $('#imappop_preview').submit(function (e)
    {
        e.preventDefault();

        $("#imappop_preview :disabled").removeAttr('disabled');
        $('#imappop_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#imappop_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#imappop_preview #user_email').removeAttr('disabled');// 20th march  

        var data = JSON.stringify($('#imappop_preview').serializeObject());
        $('#imappop_preview #user_email').prop('disabled', 'true'); // 20th march



        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "imappop_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.protocol_err !== null && jsonvalue.error.protocol_err !== "" && jsonvalue.error.protocol_err !== undefined)
                {
                    $('#imappop_preview #protocol_err').html(jsonvalue.error.protocol_err)
                    $('#imappop_preview #protocol_err').focus();
                    error_flag = false;
                } else {
                    $('#imappop_preview #protocol_err').html("")
                }
                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#imappop_preview #useremployment_error').focus();
                    $('#imappop_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#imappop_preview #minerror').focus();
                    $('#imappop_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#imappop_preview #deperror').focus();
                    $('#imappop_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#imappop_preview #other_dept').focus();
                    $('#imappop_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#imappop_preview #smierror').focus();
                    $('#imappop_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#imappop_preview #state_error').focus();
                    $('#imappop_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#imappop_preview #org_error').focus();
                    $('#imappop_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#imappop_preview #org_error').html("");
                }
                // profile 20th march 

                if (!error_flag)
                {

                    $("#imappop_preview :disabled").removeAttr('disabled');
                    $('#imappop_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#imappop_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#imappop_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });
});


// below function added by pr on 10thjan18 below function commented by pr on 19thjan18
/*function resetCSRFRandom()
 {
 console.log("inside resetRandom func random value before settting is " + $("#CSRFRandom").val());
 
 $.getJSON('resetCSRFRandom',
 {
 
 },
 function (jsonResponse)
 {
 console.log(" isnide resetrandom func response that is " + jsonResponse.random);
 
 $("#CSRFRandom").val(jsonResponse.random);
 
 //$("#CSRFRandom").val(1);
 
 console.log("inside resetRandom func random value after settting again is " + $("#CSRFRandom").val());
 
 }).error(function ()
 {
 alert(" error in resetRandom func response ");
 });
 
 }*/
