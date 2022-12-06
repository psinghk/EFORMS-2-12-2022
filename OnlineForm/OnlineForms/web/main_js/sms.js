/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {

//    $("#datepicker1").datepicker({
//        dateFormat: "dd-mm-yy", changeMonth: true, changeYear: true, yearRange: "+0:+65", minDate: "+1D", maxDate: "+65Y"
//    });
//    $("#datepicker2").datepicker({
//        dateFormat: "dd-mm-yy", changeMonth: true, changeYear: true, yearRange: "+0:+65", minDate: "+1D", maxDate: "+65Y"
//    });

    $("#datepicker1").datepicker({
        dateFormat: "dd-mm-yy", changeMonth: true, changeYear: true, yearRange: "+0:+65", minDate: "+1D", maxDate: "2M"
    });
    $("#datepicker2").datepicker({
        dateFormat: "dd-mm-yy", changeMonth: true, changeYear: true, yearRange: "+0:+65", minDate: "+1D", maxDate: "2M"
    });

    $('.page-container-bg-solid').find('input').click(function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');

        var field_name = $(this).attr('name');
        if (field_id === 'sms_serv2') {
            var isChecked = $(this).is(":checked");
            if (isChecked) {

                $('#' + field_form + ' #sms_serv1').prop('checked', true);
                $('#' + field_form + ' #sms_serv1').attr("disabled", true);
                $('#' + field_form + ' #show_pull_url').show();
                $('#' + field_form + ' #show_note').show();
            } else {

                $('#' + field_form + ' #sms_serv1').prop('checked', true);
                $('#' + field_form + ' #sms_serv1').removeAttr("disabled");
                $('#' + field_form + ' #show_pull_url').hide();
                $('#' + field_form + ' #show_note').hide();
            }
        }
        if (field_name === 's_code') {
            var t = $(this).val();

            if (t === "y" || t === "b") {
                $('#' + field_form + ' #short_code_input').show();
            } else {
                $('#' + field_form + ' #short_code_input').hide();
            }
        }
        if (field_id === 'audit_y') {
            $('#' + field_form + ' input[name=datepicker1]').val("");
            $('#' + field_form + ' #audit_date_div').hide();
        }
        if (field_id === 'audit_n') {
            $('#' + field_form + ' input[name=datepicker1]').val("");
            $('#' + field_form + ' #audit_date_div').show();
        }
        if (field_id === 'sender_y') {
            $('#' + field_form + ' input[name=sender_id]').val("");
            $('#' + field_form + ' #sender_div').show();
        }
        if (field_id === 'sender_n') {
            $('#' + field_form + ' input[name=sender_id]').val("");
            $('#' + field_form + ' #sender_div').hide();
        }

        if (field_id === "t_admin" || field_id === "b_admin") {

            var isChecked = $(this).is(":checked");
            if (isChecked) {

                $.ajax({
                    type: "POST",
                    url: "fill_sms_admin_tab",
                    success: function (data) {
                        var myJSON = JSON.stringify(data);
                        var jsonvalue = JSON.parse(myJSON);

                        $('#' + field_form + ' #auth_email').val(jsonvalue.detail_email);
                        $('#' + field_form + ' #mobile').val(jsonvalue.detail_mobile);
                        $('#' + field_form + ' #tel_res').val(jsonvalue.detail_rphone);
                        $('#' + field_form + ' #tel_ofc').val(jsonvalue.detail_ophone);
                        $('#' + field_form + ' #pin').val(jsonvalue.detail_pin);
                        if (field_id === "t_admin")
                        {

                            $('[name=tstate]').val(jsonvalue.detail_state);
                            $.get('getDistrict', {
                                user_state: jsonvalue.detail_state
                            }, function (response) {

                                var select = $('#tcity');
                                select.find('option').remove();
                                $('<option>').val("").text("-SELECT-").appendTo(select);

                                $.each(response, function (index, value) {
                                    if (jsonvalue.detail_city == value)
                                    {
                                        $('<option selected="selected">').val(value).text(value).appendTo(select);
                                    } else
                                    {
                                        $('<option>').val(value).text(value).appendTo(select);
                                    }

                                });


                            });

                        } else {

                            $('[name=bstate]').val(jsonvalue.detail_state);
                            $.get('getDistrict', {
                                user_state: jsonvalue.detail_state
                            }, function (response) {

                                var select = $('#bcity');
                                select.find('option').remove();
                                $('<option>').val("").text("-SELECT-").appendTo(select);

                                $.each(response, function (index, value) {
                                    if (jsonvalue.detail_city == value)
                                    {
                                        $('<option selected="selected">').val(value).text(value).appendTo(select);
                                    } else
                                    {
                                        $('<option>').val(value).text(value).appendTo(select);
                                    }

                                });


                            });
                        }


                        //$('#' + field_form + ' #state').val(jsonvalue.detail_state);

                        $('#' + field_form + ' #addrs').val(jsonvalue.detail_address);
                        $('#' + field_form + ' #emp_code').val(jsonvalue.detail_empcode);
                        $('#' + field_form + ' #designation').val(jsonvalue.detail_desig);
                        $('#' + field_form + ' #auth_off_name').val(jsonvalue.detail_name);
                    },
                    error: function () {
                        console.log('error');
                    }
                });
            } else {
                $('#' + field_form)[0].reset();
            }
        }
    });

    $(document).click(function () {
        $('.page-wrapper').find('input').click(function () {
            var field_id = $(this).attr('id');
            var field_form = $(this).closest('form').attr('id');
            var field_name = $(this).attr('name');

            if (field_name === 'sms_service') {
                if ($('#' + field_form + ' #sms_serv6').is(':checked') && !$('#' + field_form + ' #sms_serv5').is(':checked') && !$('#' + field_form + ' #sms_serv4').is(':checked') && !$('#' + field_form + ' #sms_serv3').is(':checked') && !$('#' + field_form + ' #sms_serv2').is(':checked') && !$('#' + field_form + ' #sms_serv1').is(':checked')) {

               
                    $('#' + field_form + ' #quick_hide').hide();

                    $('#sms_form4 #audit_div').hide();
                    //$('#sms_form4 #senderid_div').hide();

                    $('#' + field_form + ' #msgip').html("<strong>IP1</strong> from which you will access QuickSMS");
                    $('#' + field_form + ' #msgip2').html("<strong>IP2</strong> from which you will access QuickSMS");


                } else {

                    $('#' + field_form + ' #quick_hide').show();

                    $('#sms_form4 #audit_div').show();
                    //$('#sms_form4 #senderid_div').show();

                    $('#' + field_form + ' #msgip').html("<strong>IP1</strong> from which you will access SMS Gateway <span style='color: red; font-size:25px'>*</span> <a href='' data-toggle='modal' data-target='#myModal1' style='color:blue'>(Know Your IP<i class='entypo-help'></i>)</a>");
                    $('#' + field_form + ' #msgip2').html("<strong>IP2</strong> from which you will access SMS Gateway");
                }
            }
        });

    });

    $('#sms_form1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#sms_form1').serializeObject());
        var sms_service = $("#sms_form1 input[name='sms_service']:checked").map(function () {
            return this.value;
        }).get().join(',');


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "sms_tab1",
            data: {data: data, sms_service: sms_service, CSRFRandom: CSRFRandom},
            datatype: 'html',
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.pull_url_error !== null && jsonvalue.error.pull_url_error !== "" && jsonvalue.error.pull_url_error !== undefined)
                {
                    $('#sms_form1 #pull_url_err').html(jsonvalue.error.pull_url_error)
                    $('#sms_form1 #pull_url').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #pull_url_err').html("")
                }
                if (jsonvalue.error.pull_keyword_error !== null && jsonvalue.error.pull_keyword_error !== "" && jsonvalue.error.pull_keyword_error !== undefined)
                {
                    $('#sms_form1 #pull_keyword_err').html(jsonvalue.error.pull_keyword_error)
                    $('#sms_form1 #pull_keyword').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #pull_keyword_err').html("")
                }
                if (jsonvalue.error.snote_error !== null && jsonvalue.error.snote_error !== "" && jsonvalue.error.snote_error !== undefined)
                {
                    $('#sms_form1 #short_note_err').html(jsonvalue.error.snote_error)
                    $('#sms_form1 #short_code').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #short_note_err').html("")
                }
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#sms_form1 #app_name_err').html(jsonvalue.error.app_name_error)
                    $('#sms_form1 #app_name_err').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#sms_form1 #app_url_err').html(jsonvalue.error.app_url_error)
                    $('#sms_form1 #app_url').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #app_url_err').html("")
                }
                if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                {
                    $('#sms_form1 #sms_usage_err').html(jsonvalue.error.purpose_error)
                    $('#sms_form1 #sms_usage').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #sms_usage_err').html("")
                }

                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#sms_form1 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#sms_form1 #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#sms_form1 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#sms_form1 #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #server_txt_err').html("")
                }


                if (jsonvalue.error.base_ip_error !== null && jsonvalue.error.base_ip_error !== "" && jsonvalue.error.base_ip_error !== undefined)
                {
                    $('#sms_form1 #base_ip_err').html(jsonvalue.error.base_ip_error)
                    $('#sms_form1 #base_ip').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #base_ip_err').html("")
                }
                if (jsonvalue.error.service_ip_error !== null && jsonvalue.error.service_ip_error !== "" && jsonvalue.error.service_ip_error !== undefined)
                {
                    $('#sms_form1 #service_ip_err').html(jsonvalue.error.service_ip_error)
                    $('#sms_form1 #service_ip').focus();
                    error_flag = false;
                } else {
                    $('#sms_form1 #service_ip_err').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18



                if (!error_flag) {
                    $('#tab1').show();
                    $('#tab2, #tab2, #tab3').hide();
                    $('#sms_tab1_nav').addClass('active');
                    $('#sms_tab1_nav').removeClass('done');
                    $('.progress-bar').css("width", "25%");
                    $('#sms_tab2_nav').removeClass('active');
                } else {
                    $('#tab1, #tab3,#tab4 ').hide();
                    $('#tab2').show();
                    $('#sms_tab1_nav').removeClass('active');
                    $('#sms_tab1_nav').addClass('done');
                    $('.progress-bar').css("width", "50%");
                    $('#sms_tab2_nav').addClass('active');
                    $('.button-previous').click(function () {
                        $('#tab1').show();
                    });
                }
            },
            error: function () {
                console.log('error');
            }
        });
    });

    $('#sms_form2').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#sms_form2').serializeObject());
        console.log("DTAA:: " + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "sms_tab2",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: 'html',
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.t_name_error !== null && jsonvalue.error.t_name_error !== "" && jsonvalue.error.t_name_error !== undefined)
                {
                    $('#sms_form2 #tauth_off_name_err').html(jsonvalue.error.t_name_error)
                    error_flag = false;
                    $('#sms_form2 #auth_off_name').focus();

                } else {
                    $('#sms_form2 #tauth_off_name_err').html("")
                }
                if (jsonvalue.error.t_desig_error !== null && jsonvalue.error.t_desig_error !== "" && jsonvalue.error.t_desig_error !== undefined)
                {
                    $('#sms_form2 #tdesignation_err').html(jsonvalue.error.t_desig_error)
                    $('#sms_form2 #designation').focus();
                    error_flag = false;

                } else {
                    $('#sms_form2 #tdesignation_err').html("")
                }
                if (jsonvalue.error.t_empcode_error !== null && jsonvalue.error.t_empcode_error !== "" && jsonvalue.error.t_empcode_error !== undefined)
                {
                    $('#sms_form2 #temp_code_err').html(jsonvalue.error.t_empcode_error)
                    $('#sms_form2 #emp_code').focus();
                    error_flag = false;

                } else {
                    $('#sms_form2 #temp_code_err').html("")
                }
                if (jsonvalue.error.t_address_error !== null && jsonvalue.error.t_address_error !== "" && jsonvalue.error.t_address_error !== undefined)
                {
                    $('#sms_form2 #tadd1error').html(jsonvalue.error.t_address_error)
                    error_flag = false;
                    $('#sms_form2 #addrs').focus();

                } else {
                    $('#sms_form2 #tadd1error').html("")
                }
                if (jsonvalue.error.t_city_error !== null && jsonvalue.error.t_city_error !== "" && jsonvalue.error.t_city_error !== undefined)
                {
                    $('#sms_form2 #tcity_err').html(jsonvalue.error.t_city_error)
                    error_flag = false;
                    $('#sms_form2 #city').focus();

                } else {
                    $('#sms_form2 #tcity_err').html("")
                }
                if (jsonvalue.error.t_state_error !== null && jsonvalue.error.t_state_error !== "" && jsonvalue.error.t_state_error !== undefined)
                {
                    $('#sms_form2 #tstate_err').html(jsonvalue.error.t_state_error)
                    error_flag = false;
                    $('#sms_form2 #state').focus();

                } else {
                    $('#sms_form2 #tstate_err').html("")
                }
                if (jsonvalue.error.t_pin_error !== null && jsonvalue.error.t_pin_error !== "" && jsonvalue.error.t_pin_error !== undefined)
                {
                    $('#sms_form2 #tpin_err').html(jsonvalue.error.t_pin_error)
                    error_flag = false;
                    $('#sms_form2 #pin').focus();

                } else {
                    $('#sms_form2 #tpin_err').html("")
                }
                if (jsonvalue.error.t_tel1_error !== null && jsonvalue.error.t_tel1_error !== "" && jsonvalue.error.t_tel1_error !== undefined)
                {
                    $('#sms_form2 #ttel_ofc_err').html(jsonvalue.error.t_tel1_error)
                    error_flag = false;
                    $('#sms_form2 #tel_ofc').focus();

                } else {
                    $('#sms_form2 #ttel_ofc_err').html("")
                }
                if (jsonvalue.error.t_tel2_error !== null && jsonvalue.error.t_tel2_error !== "" && jsonvalue.error.t_tel2_error !== undefined)
                {
                    $('#sms_form2 #ttel_res_err').html(jsonvalue.error.t_tel2_error)
                    error_flag = false;
                    $('#sms_form2 #tel_res').focus();

                } else {
                    $('#sms_form2 #ttel_res_err').html("")
                }
                if (jsonvalue.error.t_mobile_error !== null && jsonvalue.error.t_mobile_error !== "" && jsonvalue.error.t_mobile_error !== undefined)
                {
                    $('#sms_form2 #tmobile_err').html(jsonvalue.error.t_mobile_error)
                    error_flag = false;
                    $('#sms_form2 #mobile').focus();

                } else {
                    $('#sms_form2 #tmobile_err').html("")
                }
                if (jsonvalue.error.t_email_error !== null && jsonvalue.error.t_email_error !== "" && jsonvalue.error.t_email_error !== undefined)
                {
                    $('#sms_form2 #tauth_email_err').html(jsonvalue.error.t_email_error)
                    error_flag = false;
                    $('#sms_form2 #auth_email').focus();

                } else {
                    $('#sms_form2 #tauth_email_err').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18

                if (!error_flag) {

                    $('#tab1,#tab3,#tab4 ').hide();
                    $('#tab2').show();
                    $('#sms_tab1_nav').removeClass('active');
                    $('#sms_tab2_nav').addClass('active');
                    $('#sms_tab2_nav').removeClass('done');
                    $('.progress-bar').css("width", "50%");
                } else {

                    $('#tab2, #tab1, #tab4').hide();
                    $('#tab3').show();
                    $('#sms_tab1_nav, #sms_tab2_nav').removeClass('active');
                    $('#sms_tab2_nav').addClass('done');
                    $('.progress-bar').css("width", "75%");
                    $('#sms_tab3_nav').addClass('active');
                    $('.button-previous').click(function () {
                        $('#tab2').show();
                    });
                }


            },
            error: function () {


            }
        });
    });

    $('#sms_form3').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#sms_form3').serializeObject());

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "sms_tab3",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: 'html',
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var flag = true;
                if (jsonvalue.error.b_name_error !== null && jsonvalue.error.b_name_error !== "" && jsonvalue.error.b_name_error !== undefined)
                {
                    $('#sms_form3 #bauth_off_name_err').html(jsonvalue.error.b_name_error)
                    flag = false;
                    $('#sms_form3 #auth_off_name').focus();
                } else {
                    $('#sms_form3 #bauth_off_name_err').html("")
                }
                if (jsonvalue.error.b_desig_error !== null && jsonvalue.error.b_desig_error !== "" && jsonvalue.error.b_desig_error !== undefined)
                {
                    $('#sms_form3 #bdesignation_err').html(jsonvalue.error.b_desig_error)
                    flag = false;
                    $('#sms_form3 #designation').focus();

                } else {
                    $('#sms_form3 #bdesignation_err').html("")
                }
                if (jsonvalue.error.b_empcode_error !== null && jsonvalue.error.b_empcode_error !== "" && jsonvalue.error.b_empcode_error !== undefined)
                {
                    $('#sms_form3 #bemp_code_err').html(jsonvalue.error.b_empcode_error)
                    flag = false;
                    $('#sms_form3 #emp_code').focus();

                } else {
                    $('#sms_form3 #bemp_code_err').html("")
                }
                if (jsonvalue.error.b_address_error !== null && jsonvalue.error.b_address_error !== "" && jsonvalue.error.b_address_error !== undefined)
                {
                    $('#sms_form3 #badd1error').html(jsonvalue.error.b_address_error)
                    flag = false;
                    $('#sms_form3 #addrs').focus();

                } else {
                    $('#sms_form3 #badd1error').html("")
                }
                if (jsonvalue.error.b_city_error !== null && jsonvalue.error.b_city_error !== "" && jsonvalue.error.b_city_error !== undefined)
                {
                    $('#sms_form3 #bcity_err').html(jsonvalue.error.b_city_error)
                    flag = false;
                    $('#sms_form3 #city').focus();

                } else {
                    $('#sms_form3 #bcity_err').html("")
                }
                if (jsonvalue.error.b_state_error !== null && jsonvalue.error.b_state_error !== "" && jsonvalue.error.b_state_error !== undefined)
                {
                    $('#sms_form3 #bstate_err').html(jsonvalue.error.b_state_error)
                    flag = false;
                    $('#sms_form3 #state').focus();

                } else {
                    $('#sms_form3 #bstate_err').html("")
                }
                if (jsonvalue.error.b_pin_error !== null && jsonvalue.error.b_pin_error !== "" && jsonvalue.error.b_pin_error !== undefined)
                {
                    $('#sms_form3 #bpin_err').html(jsonvalue.error.b_pin_error)
                    flag = false;
                    $('#sms_form3 #pin').focus();

                } else {
                    $('#sms_form3 #bpin_err').html("")
                }
                if (jsonvalue.error.b_tel1_error !== null && jsonvalue.error.b_tel1_error !== "" && jsonvalue.error.b_tel1_error !== undefined)
                {
                    $('#sms_form3 #btel_ofc_err').html(jsonvalue.error.b_tel1_error)
                    flag = false;
                    $('#sms_form3 #tel_ofc').focus();

                } else {
                    $('#sms_form3 #btel_ofc_err').html("")
                }
                if (jsonvalue.error.b_tel2_error !== null && jsonvalue.error.b_tel2_error !== "" && jsonvalue.error.b_tel2_error !== undefined)
                {
                    $('#sms_form3 #btel_res_err').html(jsonvalue.error.b_tel2_error)
                    flag = false;
                    $('#sms_form3 #tel_res').focus();

                } else {
                    $('#sms_form3 #btel_res_err').html("")
                }
                if (jsonvalue.error.b_mobile_error !== null && jsonvalue.error.b_mobile_error !== "" && jsonvalue.error.b_mobile_error !== undefined)
                {
                    $('#sms_form3 #bmobile_err').html(jsonvalue.error.b_mobile_error)
                    flag = false;
                    $('#sms_form3 #mobile').focus();

                } else {
                    $('#sms_form3 #bmobile_err').html("")
                }
                if (jsonvalue.error.b_email_error !== null && jsonvalue.error.b_email_error !== "" && jsonvalue.error.b_email_error !== undefined)
                {
                    $('#sms_form3 #bauth_email_err').html(jsonvalue.error.b_email_error)
                    flag = false;
                    $('#sms_form3 #auth_email').focus();

                } else {
                    $('#sms_form3 #bauth_email_err').html("")
                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    flag = false;
                }

                // end, code added by pr on 22ndjan18


                if (!flag) {
                    $('#tab1, #tab2, #tab4').hide();
                    $('#tab3').show();
                    $('#sms_tab1_nav, #sms_tab2_nav').removeClass('active');
                    $('#sms_tab3_nav').addClass('active');
                    $('#sms_tab3_nav').removeClass('done');
                    $('.progress-bar').css("width", "75%");
                } else {
                    $('#tab1,#tab2,#tab3').hide();
                    $('#tab4').show();
                    $('#sms_tab1_nav,#sms_tab2_nav,#sms_tab3_nav ').removeClass('active');
                    $('#sms_tab4_nav').addClass('active');
                    $('#sms_tab3_nav').addClass('done');
                    $('.progress-bar').css("width", "100%");
                    $('.button-previous').click(function () {
                        $('#tab3').show();
                    });
                }
            },
            error: function () {
                console.log("error");
            }
        });
    });

    $('#sms_form4').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#sms_form4').serializeObject());

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        // $('#sms_form5 .edit').removeClass('display-hide');

        $.ajax({
            type: "POST",
            url: "sms_tab4",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: 'html',
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18                

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var flag = true;
               
                 if (jsonvalue.error.audit_error !== null && jsonvalue.error.audit_error !== "" && jsonvalue.error.audit_error !== undefined)
                {
                    $('#sms_form4 #audit_err').html(jsonvalue.error.audit_error);
                    flag = false;
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                    // $('#sms_form4 #datepicker1').focus();

                } else {
                    $('#sms_form4 #audit_err').html("")
                }
                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#sms_form4 #audit_date_err').html(jsonvalue.error.audit_date_err);
                    flag = false;
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                    // $('#sms_form4 #datepicker1').focus();

                } else {
                    $('#sms_form4 #audit_date_err').html("")
                }
                if (jsonvalue.error.ip_staging_error !== null && jsonvalue.error.ip_staging_error !== "" && jsonvalue.error.ip_staging_error !== undefined)
                {
                    $('#sms_form4 #staging_ip_err').html(jsonvalue.error.ip_staging_error)
                    flag = false;
                    $('#sms_form4 #staging_ip').focus();
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");

                } else {
                    $('#sms_form4 #staging_ip_err').html("")
                }
                if (jsonvalue.error.domestic_traf_error !== null && jsonvalue.error.domestic_traf_error !== "" && jsonvalue.error.domestic_traf_error !== undefined)
                {
                    //$('#domestic_traf_error').html(jsonvalue.error.domestic_traf_error)
                    $('#sms_form4 #domestic_traf_yes').html(jsonvalue.error.domestic_traf_error)
                    flag = false;
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                    //$('#sms_form4 #domestic_traf').focus();
                } else {
                    $('#sms_form4 #domestic_traf_yes').html("")
                }
                if (jsonvalue.error.inter_traf_error !== null && jsonvalue.error.inter_traf_error !== "" && jsonvalue.error.inter_traf_error !== undefined)
                {
                    //$('#inter_traf_error').html(jsonvalue.error.inter_traf_error)
                    $('#sms_form4 #inter_traf_yes').html(jsonvalue.error.inter_traf_error)
                    flag = false;
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                    //  $('#sms_form4 #inter_traf').focus();
                } else {
                    $('#sms_form4 #inter_traf_yes').html("")
                }
                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined)
                {
                    $('#sms_form4 #sender_id_err').html(jsonvalue.error.sender_id_error)
                    flag = false;
                    $('#sms_form4 #sender_id').focus();
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                } else {
                    $('#sms_form4 #sender_id_err').html("")
                }
                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#sms_form4 #captchaerror').html(jsonvalue.error.cap_error)
                    flag = false;
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                } else {
                    $('#sms_form4 #captchaerror').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#sms_form4 #captchaerror').html(jsonvalue.error.csrf_error);
                    flag = false;
                    $('#sms_form4 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#sms_form4 #imgtxt').val("");
                }

                // end, code added by pr on 22ndjan18

                if (!flag) {
                    $('#tab1,#tab2,#tab3 ').hide();
                    $('#tab4').show();
                    $('#sms_tab1_nav, #sms_tab2_nav, #sms_tab3_nav').removeClass('active');
                    $('#sms_tab4_nav').addClass('active');
                    $('#sms_tab4_nav').removeClass('done');
                    $('.progress-bar').css("width", "100%");
                    $('.button-previous').click(function () {
                        $('#tab3').show();
                    });
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


                    $('#sms_form5 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#sms_form5 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#sms_form5 #user_name').val(jsonvalue.profile_values.cn);
                    $('#sms_form5 #user_design').val(jsonvalue.profile_values.desig);
                    $('#sms_form5 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#sms_form5 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#sms_form5 #user_state').val(jsonvalue.profile_values.state);
                    $('#sms_form5 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#sms_form5 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#sms_form5 #user_rphone').val(jsonvalue.profile_values.rphone);

                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#sms_form5 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    //$('#sms_form5 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#sms_form5 #user_email').val(jsonvalue.profile_values.email);
                    $('#sms_form5 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#sms_form5 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#sms_form5 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#sms_form5 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#sms_form5 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {
                        $('#sms_form5 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                           
                    } else {
                        var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#sms_form5 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    }
                    $('#sms_form5 #ca_design').val(jsonvalue.profile_values.ca_design);


                    if (jsonvalue.smsTotal.smsDetails1.smsservice1 === "quicksms") {
                        $('#sms_form5 #sms_serv6').prop('checked', true);
                        $('#sms_form5 #show_pull_url').hide();
                        $('#sms_form5 #show_note').hide();
                        $('#sms_form5 #quick_hide').hide();
                        $('#sms_form5 #audit_div').hide();
                        $('#sms_form5 #audit_ip_div').hide();
                        $('#sms_form5 #msgip').html("<strong>IP1</strong> from which you will access QuickSMS");
                        $('#sms_form5 #msgip2').html("<strong>IP2</strong> from which you will access QuickSMS");
                    } else {
                        $('#sms_form5 #quick_hide').show();
                        $('#sms_form5 #audit_div').show();
                        $('#sms_form5 #audit_ip_div').show();
                        $('#sms_form5 #msgip').html("<strong>IP1</strong> from which you will access SMS Gateway");
                        $('#sms_form5 #msgip2').html("<strong>IP2</strong> from which you will access SMS Gateway");

                        var temp;



                
                        if (jsonvalue.smsTotal.smsDetails1.smsservice1 != null)
                        {
                            if (jsonvalue.smsTotal.smsDetails1.smsservice1.indexOf(",") > -1) {
                                temp = jsonvalue.smsTotal.smsDetails1.smsservice1;
                            } else {
                                temp = jsonvalue.smsTotal.smsDetails1.smsservice1 + ",";
                            }
                            var myarray = temp.split(',');
                            for (var i = 0; i < myarray.length; i++)
                            {
                                var service = myarray[i].trim();
                                if (service === 'quicksms') {
                                    $('#sms_form5 #sms_serv6').prop('checked', true);
                                } else if (service === 'push') {
                                    $('#sms_form5 #sms_serv1').prop('checked', true);
                                } else if (service === 'pull') {
                                    $('#sms_form5 #sms_serv1').prop('checked', true);
                                    $('#sms_form5 #sms_serv2').prop('checked', true);
                                    $('#sms_form5 #show_pull_url').show();
                                    $('#sms_form5 #show_note').show();
                                    if (jsonvalue.prvwdetails.short_flag === 'y')
                                    {
                                        $('#sms_form5 #short_code_input').show();
                                        $('#sms_form5 #short_code').val(jsonvalue.prvwdetails.short_note)
                                        $('#sms_form5 #sy_code').prop('checked', true);
                                    } else if (jsonvalue.prvwdetails.short_flag === 'b')
                                    {
                                        $('#sms_form5 #short_code_input').show();
                                        $('#sms_form5 #short_code').val(jsonvalue.prvwdetails.short_note)
                                        $('#sms_form5 #sb_code').prop('checked', true);
                                    } else {
                                        $('#sms_form5 #sn_code').prop('checked', true);
                                        $('#sms_form5 #short_code_input').hide();
                                        $('#sms_form5 #short_code').val("")
                                    }
                                } else if (service === 'obd') {
                                    $('#sms_form5 #sms_serv3').prop('checked', true);
                                } else if (service === 'missedcall') {
                                    $('#sms_form5 #sms_serv4').prop('checked', true);
                                } else if (service === 'otp') {
                                    $('#sms_form5 #sms_serv5').prop('checked', true);
                                }
                            }
                        }
                    }

                    $('#sms_form5 #app_name').val(jsonvalue.smsTotal.smsDetails1.app_name);
                    $('#sms_form5 #app_url').val(jsonvalue.smsTotal.smsDetails1.app_url);
                    $('#sms_form5 #sms_usage').val(jsonvalue.smsTotal.smsDetails1.sms_usage);
                    if (jsonvalue.smsTotal.smsDetails1.server_loc === "Other")
                    {
                        $('#sms_form5 #server_other').show();
                        $('#sms_form5 #server_loc_txt').val(jsonvalue.smsTotal.smsDetails1.server_loc_txt);
                        $('#sms_form5 #server_loc').val(jsonvalue.smsTotal.smsDetails1.server_loc);
                    } else {
                        $('#sms_form5 #server_other').hide();
                        $('#sms_form5 #server_loc').val(jsonvalue.smsTotal.smsDetails1.server_loc);
                        $('#sms_form5 #server_loc_txt').val("");
                    }
                    $('#sms_form5 #base_ip').val(jsonvalue.smsTotal.smsDetails1.base_ip);
                    $('#sms_form5 #service_ip').val(jsonvalue.smsTotal.smsDetails1.service_ip);
                    $('#sms_form5 #pull_url').val(jsonvalue.smsTotal.smsDetails1.pull_url);
                    $('#sms_form5 #pull_keyword').val(jsonvalue.smsTotal.smsDetails1.pull_keyword);
                    $('#sms_form5 input[name=t_off_name]').val(jsonvalue.smsTotal.smsDetails2.t_off_name);
                    $('#sms_form5 input[name=tdesignation]').val(jsonvalue.smsTotal.smsDetails2.tdesignation);
                    $('#sms_form5 input[name=temp_code]').val(jsonvalue.smsTotal.smsDetails2.temp_code);
                    $('#sms_form5 input[name=taddrs]').val(jsonvalue.smsTotal.smsDetails2.taddrs);


//                    $('#sms_form5 input[name=tcity]').val(jsonvalue.prvwdetails.tech_city);
                    $('#sms_form5 select[name=tstate]').val(jsonvalue.smsTotal.smsDetails2.tstate);

                    $.get('getDistrict', {
                        user_state: jsonvalue.smsTotal.smsDetails2.tstate
                    }, function (response) {

                        var select = $('#sms_form5 #tcity');
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);

                        $.each(response, function (index, value) {

                            if (jsonvalue.smsTotal.smsDetails2.tcity == value)
                            {
                                $('<option selected="selected">').val(value).text(value).appendTo(select);
                            } else
                            {
                                $('<option>').val(value).text(value).appendTo(select);
                            }

                        });


                    });
                    $('#sms_form5 input[name=tpin]').val(jsonvalue.smsTotal.smsDetails2.tpin);
                    $('#sms_form5 input[name=ttel_ofc]').val(jsonvalue.smsTotal.smsDetails2.ttel_ofc);
                    $('#sms_form5 input[name=ttel_res]').val(jsonvalue.smsTotal.smsDetails2.ttel_res);
                    $('#sms_form5 input[name=tmobile]').val(jsonvalue.smsTotal.smsDetails2.tmobile);
                    $('#sms_form5 input[name=tauth_email]').val(jsonvalue.smsTotal.smsDetails2.tauth_email);
                    $('#sms_form5 input[name=bauth_off_name]').val(jsonvalue.smsTotal.smsDetails3.bauth_off_name);
                    $('#sms_form5 input[name=bdesignation]').val(jsonvalue.smsTotal.smsDetails3.bdesignation);
                    $('#sms_form5 input[name=bemp_code]').val(jsonvalue.smsTotal.smsDetails3.bemp_code);
                    $('#sms_form5 input[name=baddrs]').val(jsonvalue.smsTotal.smsDetails3.baddrs);
                    $('#sms_form5 input[name=bcity]').val(jsonvalue.smsTotal.smsDetails3.bcity);
                    $('#sms_form5 select[name=bstate]').val(jsonvalue.smsTotal.smsDetails3.bstate);

                    $.get('getDistrict', {
                        user_state: jsonvalue.smsTotal.smsDetails3.bstate
                    }, function (response) {

                        var select = $('#sms_form5 #bcity');
                        select.find('option').remove();
                        $('<option>').val("").text("-SELECT-").appendTo(select);

                        $.each(response, function (index, value) {

                            if (jsonvalue.smsTotal.smsDetails3.bcity == value)
                            {
                                $('<option selected="selected">').val(value).text(value).appendTo(select);
                            } else
                            {
                                $('<option>').val(value).text(value).appendTo(select);
                            }

                        });


                    });
                    $('#sms_form5 input[name=bpin]').val(jsonvalue.smsTotal.smsDetails3.bpin);
                    $('#sms_form5 input[name=btel_ofc]').val(jsonvalue.smsTotal.smsDetails3.btel_ofc);
                    $('#sms_form5 input[name=btel_res]').val(jsonvalue.smsTotal.smsDetails3.btel_res);
                    $('#sms_form5 input[name=bmobile]').val(jsonvalue.smsTotal.smsDetails3.bmobile);
                    $('#sms_form5 input[name=bauth_email]').val(jsonvalue.smsTotal.smsDetails3.bauth_email);
                     $('#sms_form5 input[name=baddrs]').val(jsonvalue.smsTotal.smsDetails3.baddrs);
                      $('#sms_form5 input[name=bstate]').val(jsonvalue.smsTotal.smsDetails3.bstate);
                       $('#sms_form5 input[name=bcity]').val(jsonvalue.smsTotal.smsDetails3.bcity);
                    $('#sms_form5 #staging_ip').val(jsonvalue.smsTotal.smsDetails4.staging_ip);
                    if (jsonvalue.prvwdetails.flag_sender === 'Yes')
                    {
                        $('#sms_form5 #sender_div').show();
                        $('#sms_form5 #sender_id').val(jsonvalue.prvwdetails.sender_id);
                        $('#sms_form5 #sender_y').prop('checked', true);
                    } else {
                        $('#sms_form5 #sender_n').prop('checked', true);
                        $('#sms_form5 #sender_div').hide();
                        $('#sms_form5 #sender_id').val("");
                    }
                    if (jsonvalue.smsTotal.smsDetails4.audit === 'Yes')
                    {
                        
                        $('#sms_form5 #audit_date_div').hide();
                        $('#sms_form5 #datepicker2').val("");
                        $('#sms_form5 #audit_y').prop('checked', true);
                    } else {
                        $('#sms_form5 #audit_n').prop('checked', true);
                        $('#sms_form5 #audit_date_div').show();
                        $('#sms_form5 #datepicker2').val(jsonvalue.prvwdetails.audit_date);
                    }
                     //$('#sms_form5 #staging_ip').val(jsonvalue.smsTotal.smsDetails4.staging_ip);
                    $('#sms_form5 #domestic_traf').val(jsonvalue.smsTotal.smsDetails4.domestic_traf);
                    $('#sms_form5 #inter_traf').val(jsonvalue.smsTotal.smsDetails4.inter_traf);


                    $('.edit').removeClass('display-hide');
                    $('#sms_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#sms_form5 :button[type='button']").removeAttr('disabled');
                    $("#sms_form5 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }
            },
            error: function () {
                console.log("errorrrr");
            }
        });
    });

    $('#sms_form5').submit(function (e) {
        e.preventDefault();
        $("#sms_form5 :disabled").removeAttr('disabled');
        $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');

        $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#sms_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#sms_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#sms_form5').serializeObject());
        $('#sms_preview_tab #user_email').prop('disabled', 'true'); // 20th march


        var sms_service = $("#sms_form5 input[name='sms_service']:checked").map(function () {
            return $(this).val();
        }).get().join(',');


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "sms_tab5",
            data: {data: data, sms_service: sms_service, action_type: "confirm", CSRFRandom: CSRFRandom},
            dataType: 'JSON',
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.pull_url_error !== null && jsonvalue.error.pull_url_error !== "" && jsonvalue.error.pull_url_error !== undefined)
                {

                    $('#sms_form5 #pull_url_err').html(jsonvalue.error.pull_url_error)
                    error_flag = false;
                    $('#sms_form5 #pull_url').focus();
                } else {
                    $('#sms_form5 #pull_url_err').html("")
                }
                if (jsonvalue.error.pull_keyword_error !== null && jsonvalue.error.pull_keyword_error !== "" && jsonvalue.error.pull_keyword_error !== undefined)
                {
                    $('#sms_form5 #pull_keyword_err').html(jsonvalue.error.pull_keyword_error)
                    error_flag = false;
                    $('#sms_form5 #pull_keyword').focus();
                } else {
                    $('#sms_form5 #pull_keyword_err').html("")
                }
                if (jsonvalue.error.snote_error !== null && jsonvalue.error.snote_error !== "" && jsonvalue.error.snote_error !== undefined)
                {
                    $('#sms_form5 #short_note_err').html(jsonvalue.error.snote_error)
                    error_flag = false;
                    $('#sms_form5 #short_code').focus();
                } else {
                    $('#sms_form5 #short_note_err').html("")
                }
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#sms_form5 #app_name_err').html(jsonvalue.error.app_name_error)

                    error_flag = false;
                    $('#sms_form5 #app_name').focus();
                } else {
                    $('#sms_form5 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#sms_form5 #app_url_err').html(jsonvalue.error.app_url_error)
                    error_flag = false;
                    $('#sms_form5 #app_url').focus();
                } else {
                    $('#sms_form5 #app_url_err').html("")
                }
                if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                {
                    $('#sms_form5 #sms_usage_err').html(jsonvalue.error.purpose_error)
                    error_flag = false;
                    $('#sms_form5 #sms_usage').focus();
                } else {
                    $('#sms_form5 #sms_usage_err').html("")
                }
//                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
//                {
//                    $('#sms_form5 #server_txt_err').html(jsonvalue.error.server_loc_error)
//                    error_flag = false;
//                } else {
//                    $('#sms_form5 #server_txt_err').html("")
//                }

                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#sms_form5 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#sms_form5 #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#sms_form5 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#sms_form5 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#sms_form5 #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#sms_form5 #server_txt_err').html("")
                }

                if (jsonvalue.error.base_ip_error !== null && jsonvalue.error.base_ip_error !== "" && jsonvalue.error.base_ip_error !== undefined)
                {
                    $('#sms_form5 #base_ip_err').html(jsonvalue.error.base_ip_error)
                    error_flag = false;
                    $('#sms_form5 #base_ip').focus();
                } else {
                    $('#sms_form5 #base_ip_err').html("")
                }
                if (jsonvalue.error.service_ip_error !== null && jsonvalue.error.service_ip_error !== "" && jsonvalue.error.service_ip_error !== undefined)
                {
                    $('#sms_form5 #service_ip_err').html(jsonvalue.error.service_ip_error)
                    error_flag = false;
                    $('#sms_form5 #service_ip').focus();
                } else {
                    $('#sms_form5 #service_ip_err').html("")
                }

                if (jsonvalue.error.t_name_error !== null && jsonvalue.error.t_name_error !== "" && jsonvalue.error.t_name_error !== undefined)
                {
                    $('#sms_form5 #tauth_off_name_err').html(jsonvalue.error.t_name_error)
                    error_flag = false;
                    $("#sms_form5 input[name=t_off_name]").focus();

                } else {
                    $('#sms_form5 #tauth_off_name_err').html("")
                }
                if (jsonvalue.error.t_desig_error !== null && jsonvalue.error.t_desig_error !== "" && jsonvalue.error.t_desig_error !== undefined)
                {
                    $('#sms_form5 #tdesignation_err').html(jsonvalue.error.t_desig_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tdesignation]").focus();

                } else {
                    $('#sms_form5 #tdesignation_err').html("")
                }
                if (jsonvalue.error.t_empcode_error !== null && jsonvalue.error.t_empcode_error !== "" && jsonvalue.error.t_empcode_error !== undefined)
                {
                    $('#sms_form5 #temp_code_err').html(jsonvalue.error.t_empcode_error)
                    error_flag = false;
                    $("#sms_form5 input[name=temp_code]").focus();

                } else {
                    $('#sms_form5 #temp_code_err').html("")
                }
                if (jsonvalue.error.t_address_error !== null && jsonvalue.error.t_address_error !== "" && jsonvalue.error.t_address_error !== undefined)
                {
                    $('#sms_form5 #tadd1error').html(jsonvalue.error.t_address_error)
                    error_flag = false;
                    $("#sms_form5 input[name=taddrs]").focus();

                } else {
                    $('#sms_form5 #tadd1error').html("")
                }
                if (jsonvalue.error.t_city_error !== null && jsonvalue.error.t_city_error !== "" && jsonvalue.error.t_city_error !== undefined)
                {
                    $('#sms_form5 #tcity_err').html(jsonvalue.error.t_city_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tcity]").focus();

                } else {
                    $('#sms_form5 #tcity_err').html("")
                }
                if (jsonvalue.error.t_state_error !== null && jsonvalue.error.t_state_error !== "" && jsonvalue.error.t_state_error !== undefined)
                {
                    $('#sms_form5 #tstate_err').html(jsonvalue.error.t_state_error)
                    error_flag = false;
                    $('#sms_form5 input[name=tstate]').focus();

                } else {
                    $('#sms_form5 #tstate_err').html("")
                }
                if (jsonvalue.error.t_pin_error !== null && jsonvalue.error.t_pin_error !== "" && jsonvalue.error.t_pin_error !== undefined)
                {
                    $('#sms_form5 #tpin_err').html(jsonvalue.error.t_pin_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tpin]").focus();

                } else {
                    $('#sms_form5 #tpin_err').html("")
                }
                if (jsonvalue.error.t_tel1_error !== null && jsonvalue.error.t_tel1_error !== "" && jsonvalue.error.t_tel1_error !== undefined)
                {
                    $('#sms_form5 #ttel_ofc_err').html(jsonvalue.error.t_tel1_error)
                    error_flag = false;
                    $("#sms_form5 input[name=ttel_ofc]").focus();

                } else {
                    $('#sms_form5 #ttel_ofc_err').html("")
                }
                if (jsonvalue.error.t_tel2_error !== null && jsonvalue.error.t_tel2_error !== "" && jsonvalue.error.t_tel2_error !== undefined)
                {
                    $('#sms_form5 #ttel_res_err').html(jsonvalue.error.t_tel2_error)
                    error_flag = false;
                    $("#sms_form5 input[name=ttel_res]").focus();

                } else {
                    $('#sms_form5 #ttel_res_err').html("")
                }
                if (jsonvalue.error.t_mobile_error !== null && jsonvalue.error.t_mobile_error !== "" && jsonvalue.error.t_mobile_error !== undefined)
                {
                    $('#sms_form5 #tmobile_err').html(jsonvalue.error.t_mobile_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tmobile]").focus();

                } else {
                    $('#sms_form5 #tmobile_err').html("")
                }
                if (jsonvalue.error.t_email_error !== null && jsonvalue.error.t_email_error !== "" && jsonvalue.error.t_email_error !== undefined)
                {
                    $('#sms_form5 #tauth_email_err').html(jsonvalue.error.t_email_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tauth_email]").focus();

                } else {
                    $('#sms_form5 #tauth_email_err').html("")
                }
                if (jsonvalue.error.b_name_error !== null && jsonvalue.error.b_name_error !== "" && jsonvalue.error.b_name_error !== undefined)
                {
                    $('#sms_form5 #bauth_off_name_err').html(jsonvalue.error.b_name_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bauth_off_name]").focus();

                } else {
                    $('#sms_form5 #bauth_off_name_err').html("")
                }
                if (jsonvalue.error.b_desig_error !== null && jsonvalue.error.b_desig_error !== "" && jsonvalue.error.b_desig_error !== undefined)
                {
                    $('#sms_form5 #bdesignation_err').html(jsonvalue.error.b_desig_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bdesignation]").focus();

                } else {
                    $('#sms_form5 #bdesignation_err').html("")
                }
                if (jsonvalue.error.b_empcode_error !== null && jsonvalue.error.b_empcode_error !== "" && jsonvalue.error.b_empcode_error !== undefined)
                {
                    $('#sms_form5 #bemp_code_err').html(jsonvalue.error.b_empcode_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bemp_code]").focus();

                } else {
                    $('#sms_form5 #bemp_code_err').html("")
                }
                if (jsonvalue.error.b_address_error !== null && jsonvalue.error.b_address_error !== "" && jsonvalue.error.b_address_error !== undefined)
                {
                    $('#sms_form5 #badd1error').html(jsonvalue.error.b_address_error)
                    error_flag = false;
                    $("#sms_form5 input[name=baddrs]").focus();

                } else {
                    $('#sms_form5 #badd1error').html("")
                }
                if (jsonvalue.error.b_city_error !== null && jsonvalue.error.b_city_error !== "" && jsonvalue.error.b_city_error !== undefined)
                {
                    $('#sms_form5 #bcity_err').html(jsonvalue.error.b_city_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bcity]").focus();

                } else {
                    $('#sms_form5 #bcity_err').html("")
                }
                if (jsonvalue.error.b_state_error !== null && jsonvalue.error.b_state_error !== "" && jsonvalue.error.b_state_error !== undefined)
                {
                    $('#sms_form5 #bstate_err').html(jsonvalue.error.b_state_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bstate]").focus();

                } else {
                    $('#sms_form5 #bstate_err').html("")
                }
                if (jsonvalue.error.b_pin_error !== null && jsonvalue.error.b_pin_error !== "" && jsonvalue.error.b_pin_error !== undefined)
                {
                    $('#sms_form5 #bpin_err').html(jsonvalue.error.b_pin_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bpin]").focus();

                } else {
                    $('#sms_form5 #bpin_err').html("")
                }
                if (jsonvalue.error.b_tel1_error !== null && jsonvalue.error.b_tel1_error !== "" && jsonvalue.error.b_tel1_error !== undefined)
                {
                    $('#sms_form5 #btel_ofc_err').html(jsonvalue.error.b_tel1_error)
                    error_flag = false;
                    $("#sms_form5 input[name=btel_ofc]").focus();

                } else {
                    $('#sms_form5 #btel_ofc_err').html("")
                }
                if (jsonvalue.error.b_tel2_error !== null && jsonvalue.error.b_tel2_error !== "" && jsonvalue.error.b_tel2_error !== undefined)
                {
                    $('#sms_form5 #btel_res_err').html(jsonvalue.error.b_tel2_error)
                    error_flag = false;
                    $("#sms_form5 input[name=btel_res]").focus();

                } else {
                    $('#sms_form5 #btel_res_err').html("")
                }
                if (jsonvalue.error.b_mobile_error !== null && jsonvalue.error.b_mobile_error !== "" && jsonvalue.error.b_mobile_error !== undefined)
                {
                    $('#sms_form5 #bmobile_err').html(jsonvalue.error.b_mobile_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bmobile]").focus();

                } else {
                    $('#sms_form5 #bmobile_err').html("")
                }
                if (jsonvalue.error.b_email_error !== null && jsonvalue.error.b_email_error !== "" && jsonvalue.error.b_email_error !== undefined)
                {
                    $('#sms_form5 #bauth_email_err').html(jsonvalue.error.b_email_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bauth_email]").focus();

                } else {
                    $('#sms_form5 #bauth_email_err').html("")
                }
                if (jsonvalue.error.ip_staging_error !== null && jsonvalue.error.ip_staging_error !== "" && jsonvalue.error.ip_staging_error !== undefined)
                {
                    $('#sms_form5 #staging_ip_err').html(jsonvalue.error.ip_staging_error)
                    error_flag = false;
                    $('#sms_form5 #staging_ip').focus();

                } else {
                    $('#sms_form5 #staging_ip_err').html("")
                }

                if (jsonvalue.error.domestic_traf_error !== null && jsonvalue.error.domestic_traf_error !== "" && jsonvalue.error.domestic_traf_error !== undefined)
                {
                    $('#sms_form5 #domestic_traf_error').html(jsonvalue.error.domestic_traf_error)
                    error_flag = false;
                    $('#sms_form5 #domestic_traf').focus();


                } else {
                    $('#sms_form5 #domestic_traf_error').html("")
                }
                if (jsonvalue.error.inter_traf_error !== null && jsonvalue.error.inter_traf_error !== "" && jsonvalue.error.inter_traf_error !== undefined)
                {
                    $('#sms_form5 #inter_traf_error').html(jsonvalue.error.inter_traf_error)
                    error_flag = false;
                    $('#sms_form5 #inter_traf').focus();


                } else {
                    $('#sms_form5 #inter_traf_error').html("")
                }
                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined)
                {
                    $('#sms_form5 #sender_id_err').html(jsonvalue.error.sender_id_error)
                    error_flag = false;
                    $('#sms_form5 #sender_id').focus();


                } else {
                    $('#sms_form5 #sender_id_err').html("")
                }
                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#sms_form5 #audit_date_err1').html(jsonvalue.error.audit_date_err)
                    error_flag = false;
                    $('#sms_form5 #audit_date_err1').focus();

                } else {
                    $('#sms_form5 #audit_date_err1').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#sms_form5 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#sms_form5 #useremployment_error').focus();
                    $('#sms_form5 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#sms_form5 #minerror').focus();
                    $('#sms_form5 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#sms_form5 #deperror').focus();
                    $('#sms_form5 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#sms_form5 #other_dept').focus();
                    $('#sms_form5 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#sms_form5 #smierror').focus();
                    $('#sms_form5 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#sms_form5 #state_error').focus();
                    $('#sms_form5 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#sms_form5 #org_error').focus();
                    $('#sms_form5 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#sms_form5 #ca_design').focus();
                    $('#sms_form5 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#sms_form5 #hod_name').focus();
                    $('#sms_form5 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#sms_form5 #hod_mobile').focus();
                    $('#sms_form5 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#sms_form5 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#sms_form5 #hod_tel').focus();
                    $('#sms_form5 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodtel_error').html("");
                }
                // profile on 20th march
                if (error_flag) {
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'sms'},
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

    $('#telnet_no').click(function (e) {
        e.preventDefault();
        var data = "form_details.telnet=n";
        $.ajax({
            type: "POST",
            url: "sms_tab6",
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
            url: "sms_tab6",
            data: data,
            dataType: 'html',
            success: function (data) {

                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'sms'},
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
            url: "sms_tab7",
            data: data,
            dataType: 'html',
            success: function (data) {

                $.ajax({
                    type: "POST",
                    url: "esign",
                    data: {formtype: 'sms'},
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
            url: "sms_tab7",
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
                        data: {formtype: 'sms'},
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

    $('#sms_form5 .edit').click(function () {
        var employment = $('#sms_preview_tab #user_employment').val();
        var min = $('#sms_preview_tab #min').val();
        var dept = $('#sms_preview_tab #dept').val();
        var statecode = $('#sms_preview_tab #stateCode').val();
        var Smi = $('#sms_preview_tab #Smi').val();
        var Org = $('#sms_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#sms_preview_tab #min');
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
                var select = $('#sms_preview_tab #dept');
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
                $('#sms_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#sms_preview_tab #stateCode');
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
                var select = $('#sms_preview_tab #Smi');
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
                $('#sms_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {

            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#sms_preview_tab #Org');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    console.log("Org:::" + Org + "val" + value)
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
                $('#sms_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#sms_preview_tab #central_div').hide();
            $('#sms_preview_tab #state_div').hide();
            $('#sms_preview_tab #other_div').hide();
        }
        $('#sms_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#sms_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#sms_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#sms_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $(this).addClass('display-hide');
        //$(this).hide();
    });

    $('#sms_form5 #confirm').click(function () {
        $("#sms_form5 :disabled").removeAttr('disabled');
        $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#sms_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#sms_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#sms_form5').serializeObject());
        $('#sms_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var sms_service = $("#sms_form5 input[name='sms_service']:checked").map(function () {
            return $(this).val();
        }).get().join(';');

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "sms_tab5",
            data: {data: data, sms_service: sms_service, action_type: "validate", CSRFRandom: CSRFRandom},
            dataType: 'JSON',
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.pull_url_error !== null && jsonvalue.error.pull_url_error !== "" && jsonvalue.error.pull_url_error !== undefined)
                {

                    $('#sms_form5 #pull_url_err').html(jsonvalue.error.pull_url_error)
                    error_flag = false;
                    $('#sms_form5 #pull_url').focus();
                } else {
                    $('#sms_form5 #pull_url_err').html("")
                }
                if (jsonvalue.error.pull_keyword_error !== null && jsonvalue.error.pull_keyword_error !== "" && jsonvalue.error.pull_keyword_error !== undefined)
                {
                    $('#sms_form5 #pull_keyword_err').html(jsonvalue.error.pull_keyword_error)
                    error_flag = false;
                    $('#sms_form5 #pull_keyword').focus();
                } else {
                    $('#sms_form5 #pull_keyword_err').html("")
                }
                if (jsonvalue.error.snote_error !== null && jsonvalue.error.snote_error !== "" && jsonvalue.error.snote_error !== undefined)
                {
                    $('#sms_form5 #short_note_err').html(jsonvalue.error.snote_error)
                    error_flag = false;
                    $('#sms_form5 #short_code').focus();
                } else {
                    $('#sms_form5 #short_note_err').html("")
                }
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#sms_form5 #app_name_err').html(jsonvalue.error.app_name_error)

                    error_flag = false;
                    $('#sms_form5 #app_name').focus();
                } else {
                    $('#sms_form5 #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#sms_form5 #app_url_err').html(jsonvalue.error.app_url_error)
                    error_flag = false;
                    $('#sms_form5 #app_url').focus();
                } else {
                    $('#sms_form5 #app_url_err').html("")
                }
                if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                {
                    $('#sms_form5 #sms_usage_err').html(jsonvalue.error.purpose_error)
                    error_flag = false;
                    $('#sms_form5 #sms_usage').focus();
                } else {
                    $('#sms_form5 #sms_usage_err').html("")
                }
//                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
//                {
//                    $('#sms_form5 #server_txt_err').html(jsonvalue.error.server_loc_error)
//                    error_flag = false;
//                } else {
//                    $('#sms_form5 #server_txt_err').html("")
//                }

                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#sms_form5 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#sms_form5 #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#sms_form5 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#sms_form5 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#sms_form5 #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#sms_form5 #server_txt_err').html("")
                }

                if (jsonvalue.error.base_ip_error !== null && jsonvalue.error.base_ip_error !== "" && jsonvalue.error.base_ip_error !== undefined)
                {
                    $('#sms_form5 #base_ip_err').html(jsonvalue.error.base_ip_error)
                    error_flag = false;
                    $('#sms_form5 #base_ip').focus();
                } else {
                    $('#sms_form5 #base_ip_err').html("")
                }
                if (jsonvalue.error.service_ip_error !== null && jsonvalue.error.service_ip_error !== "" && jsonvalue.error.service_ip_error !== undefined)
                {
                    $('#sms_form5 #service_ip_err').html(jsonvalue.error.service_ip_error)
                    error_flag = false;
                    $('#sms_form5 #service_ip').focus();
                } else {
                    $('#sms_form5 #service_ip_err').html("")
                }

                if (jsonvalue.error.t_name_error !== null && jsonvalue.error.t_name_error !== "" && jsonvalue.error.t_name_error !== undefined)
                {
                    $('#sms_form5 #tauth_off_name_err').html(jsonvalue.error.t_name_error)
                    error_flag = false;
                    $("#sms_form5 input[name=t_off_name]").focus();

                } else {
                    $('#sms_form5 #tauth_off_name_err').html("")
                }
                if (jsonvalue.error.t_desig_error !== null && jsonvalue.error.t_desig_error !== "" && jsonvalue.error.t_desig_error !== undefined)
                {
                    $('#sms_form5 #tdesignation_err').html(jsonvalue.error.t_desig_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tdesignation]").focus();

                } else {
                    $('#sms_form5 #tdesignation_err').html("")
                }
                if (jsonvalue.error.t_empcode_error !== null && jsonvalue.error.t_empcode_error !== "" && jsonvalue.error.t_empcode_error !== undefined)
                {
                    $('#sms_form5 #temp_code_err').html(jsonvalue.error.t_empcode_error)
                    error_flag = false;
                    $("#sms_form5 input[name=temp_code]").focus();

                } else {
                    $('#sms_form5 #temp_code_err').html("")
                }
                if (jsonvalue.error.t_address_error !== null && jsonvalue.error.t_address_error !== "" && jsonvalue.error.t_address_error !== undefined)
                {
                    $('#sms_form5 #tadd1error').html(jsonvalue.error.t_address_error)
                    error_flag = false;
                    $("#sms_form5 input[name=taddrs]").focus();

                } else {
                    $('#sms_form5 #tadd1error').html("")
                }
                if (jsonvalue.error.t_city_error !== null && jsonvalue.error.t_city_error !== "" && jsonvalue.error.t_city_error !== undefined)
                {
                    $('#sms_form5 #tcity_err').html(jsonvalue.error.t_city_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tcity]").focus();

                } else {
                    $('#sms_form5 #tcity_err').html("")
                }
                if (jsonvalue.error.t_state_error !== null && jsonvalue.error.t_state_error !== "" && jsonvalue.error.t_state_error !== undefined)
                {
                    $('#sms_form5 #tstate_err').html(jsonvalue.error.t_state_error)
                    error_flag = false;
                    $('#sms_form5 input[name=tstate]').focus();

                } else {
                    $('#sms_form5 #tstate_err').html("")
                }
                if (jsonvalue.error.t_pin_error !== null && jsonvalue.error.t_pin_error !== "" && jsonvalue.error.t_pin_error !== undefined)
                {
                    $('#sms_form5 #tpin_err').html(jsonvalue.error.t_pin_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tpin]").focus();

                } else {
                    $('#sms_form5 #tpin_err').html("")
                }
                if (jsonvalue.error.t_tel1_error !== null && jsonvalue.error.t_tel1_error !== "" && jsonvalue.error.t_tel1_error !== undefined)
                {
                    $('#sms_form5 #ttel_ofc_err').html(jsonvalue.error.t_tel1_error)
                    error_flag = false;
                    $("#sms_form5 input[name=ttel_ofc]").focus();

                } else {
                    $('#sms_form5 #ttel_ofc_err').html("")
                }
                if (jsonvalue.error.t_tel2_error !== null && jsonvalue.error.t_tel2_error !== "" && jsonvalue.error.t_tel2_error !== undefined)
                {
                    $('#sms_form5 #ttel_res_err').html(jsonvalue.error.t_tel2_error)
                    error_flag = false;
                    $("#sms_form5 input[name=ttel_res]").focus();

                } else {
                    $('#sms_form5 #ttel_res_err').html("")
                }
                if (jsonvalue.error.t_mobile_error !== null && jsonvalue.error.t_mobile_error !== "" && jsonvalue.error.t_mobile_error !== undefined)
                {
                    $('#sms_form5 #tmobile_err').html(jsonvalue.error.t_mobile_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tmobile]").focus();

                } else {
                    $('#sms_form5 #tmobile_err').html("")
                }
                if (jsonvalue.error.t_email_error !== null && jsonvalue.error.t_email_error !== "" && jsonvalue.error.t_email_error !== undefined)
                {
                    $('#sms_form5 #tauth_email_err').html(jsonvalue.error.t_email_error)
                    error_flag = false;
                    $("#sms_form5 input[name=tauth_email]").focus();

                } else {
                    $('#sms_form5 #tauth_email_err').html("")
                }
                if (jsonvalue.error.b_name_error !== null && jsonvalue.error.b_name_error !== "" && jsonvalue.error.b_name_error !== undefined)
                {
                    $('#sms_form5 #bauth_off_name_err').html(jsonvalue.error.b_name_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bauth_off_name]").focus();

                } else {
                    $('#sms_form5 #bauth_off_name_err').html("")
                }
                if (jsonvalue.error.b_desig_error !== null && jsonvalue.error.b_desig_error !== "" && jsonvalue.error.b_desig_error !== undefined)
                {
                    $('#sms_form5 #bdesignation_err').html(jsonvalue.error.b_desig_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bdesignation]").focus();

                } else {
                    $('#sms_form5 #bdesignation_err').html("")
                }
                if (jsonvalue.error.b_empcode_error !== null && jsonvalue.error.b_empcode_error !== "" && jsonvalue.error.b_empcode_error !== undefined)
                {
                    $('#sms_form5 #bemp_code_err').html(jsonvalue.error.b_empcode_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bemp_code]").focus();

                } else {
                    $('#sms_form5 #bemp_code_err').html("")
                }
                if (jsonvalue.error.b_address_error !== null && jsonvalue.error.b_address_error !== "" && jsonvalue.error.b_address_error !== undefined)
                {
                    $('#sms_form5 #badd1error').html(jsonvalue.error.b_address_error)
                    error_flag = false;
                    $("#sms_form5 input[name=baddrs]").focus();

                } else {
                    $('#sms_form5 #badd1error').html("")
                }
                if (jsonvalue.error.b_city_error !== null && jsonvalue.error.b_city_error !== "" && jsonvalue.error.b_city_error !== undefined)
                {
                    $('#sms_form5 #bcity_err').html(jsonvalue.error.b_city_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bcity]").focus();

                } else {
                    $('#sms_form5 #bcity_err').html("")
                }
                if (jsonvalue.error.b_state_error !== null && jsonvalue.error.b_state_error !== "" && jsonvalue.error.b_state_error !== undefined)
                {
                    $('#sms_form5 #bstate_err').html(jsonvalue.error.b_state_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bstate]").focus();

                } else {
                    $('#sms_form5 #bstate_err').html("")
                }
                if (jsonvalue.error.b_pin_error !== null && jsonvalue.error.b_pin_error !== "" && jsonvalue.error.b_pin_error !== undefined)
                {
                    $('#sms_form5 #bpin_err').html(jsonvalue.error.b_pin_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bpin]").focus();

                } else {
                    $('#sms_form5 #bpin_err').html("")
                }
                if (jsonvalue.error.b_tel1_error !== null && jsonvalue.error.b_tel1_error !== "" && jsonvalue.error.b_tel1_error !== undefined)
                {
                    $('#sms_form5 #btel_ofc_err').html(jsonvalue.error.b_tel1_error)
                    error_flag = false;
                    $("#sms_form5 input[name=btel_ofc]").focus();

                } else {
                    $('#sms_form5 #btel_ofc_err').html("")
                }
                if (jsonvalue.error.b_tel2_error !== null && jsonvalue.error.b_tel2_error !== "" && jsonvalue.error.b_tel2_error !== undefined)
                {
                    $('#sms_form5 #btel_res_err').html(jsonvalue.error.b_tel2_error)
                    error_flag = false;
                    $("#sms_form5 input[name=btel_res]").focus();

                } else {
                    $('#sms_form5 #btel_res_err').html("")
                }
                if (jsonvalue.error.b_mobile_error !== null && jsonvalue.error.b_mobile_error !== "" && jsonvalue.error.b_mobile_error !== undefined)
                {
                    $('#sms_form5 #bmobile_err').html(jsonvalue.error.b_mobile_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bmobile]").focus();

                } else {
                    $('#sms_form5 #bmobile_err').html("")
                }
                if (jsonvalue.error.b_email_error !== null && jsonvalue.error.b_email_error !== "" && jsonvalue.error.b_email_error !== undefined)
                {
                    $('#sms_form5 #bauth_email_err').html(jsonvalue.error.b_email_error)
                    error_flag = false;
                    $("#sms_form5 input[name=bauth_email]").focus();

                } else {
                    $('#sms_form5 #bauth_email_err').html("")
                }
                if (jsonvalue.error.ip_staging_error !== null && jsonvalue.error.ip_staging_error !== "" && jsonvalue.error.ip_staging_error !== undefined)
                {
                    $('#sms_form5 #staging_ip_err').html(jsonvalue.error.ip_staging_error)
                    error_flag = false;
                    $('#sms_form5 #staging_ip').focus();

                } else {
                    $('#sms_form5 #staging_ip_err').html("")
                }

                if (jsonvalue.error.domestic_traf_error !== null && jsonvalue.error.domestic_traf_error !== "" && jsonvalue.error.domestic_traf_error !== undefined)
                {
                    $('#sms_form5 #domestic_traf_error').html(jsonvalue.error.domestic_traf_error)
                    error_flag = false;
                    $('#sms_form5 #domestic_traf').focus();


                } else {
                    $('#sms_form5 #domestic_traf_error').html("")
                }
                if (jsonvalue.error.inter_traf_error !== null && jsonvalue.error.inter_traf_error !== "" && jsonvalue.error.inter_traf_error !== undefined)
                {
                    $('#sms_form5 #inter_traf_error').html(jsonvalue.error.inter_traf_error)
                    error_flag = false;
                    $('#sms_form5 #inter_traf').focus();


                } else {
                    $('#sms_form5 #inter_traf_error').html("")
                }
                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined)
                {
                    $('#sms_form5 #sender_id_err').html(jsonvalue.error.sender_id_error)
                    error_flag = false;
                    $('#sms_form5 #sender_id').focus();


                } else {
                    $('#sms_form5 #sender_id_err').html("")
                }
                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#sms_form5 #audit_date_err1').html(jsonvalue.error.audit_date_err)
                    error_flag = false;
                    $('#sms_form5 #audit_date_err1').focus();

                } else {
                    $('#sms_form5 #audit_date_err1').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#sms_form5 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }


                // end, code added by pr on 22ndjan18

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#sms_form5 #useremployment_error').focus();
                    $('#sms_form5 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#sms_form5 #minerror').focus();
                    $('#sms_form5 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#sms_form5 #deperror').focus();
                    $('#sms_form5 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#sms_form5 #other_dept').focus();
                    $('#sms_form5 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#sms_form5 #smierror').focus();
                    $('#sms_form5 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#sms_form5 #state_error').focus();
                    $('#sms_form5 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#sms_form5 #org_error').focus();
                    $('#sms_form5 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#sms_form5 #ca_design').focus();
                    $('#sms_form5 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#sms_form5 #hod_name').focus();
                    $('#sms_form5 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#sms_form5 #hod_mobile').focus();
                    $('#sms_form5 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#sms_form5 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#sms_form5 #hod_tel').focus();
                    $('#sms_form5 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#sms_form5 #hodtel_error').html("");
                }
                // profile on 20th march
                if (error_flag) {
                    if ($('#sms_form5 #tnc').is(":checked"))
                    {
                        $('#sms_form5 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                         
//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#sms_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#sms_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#sms_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#sms_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#sms_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#sms_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                      //  else {
                            $('#sms_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#sms_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#sms_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                      //  }
//                        $('#sms_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                        $('#sms_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                        $('#sms_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        $('#sms_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#sms_form5 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $('#sms_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#sms_form5 #tnc").removeAttr('disabled');
                    }
                } else {
                    $("#sms_form5 :disabled").removeAttr('disabled');
                    $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    // line commented by MI on 25thjan19
                    //$('#sms_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                }
            }
        });
    });


    $('#sms_form_confirm #confirmYes').click(function () {
        $('#sms_form5').submit();
        $('#stack3').modal('toggle');
    });

    $(document).on('click', '#sms_preview .edit', function () {
        //$('#sms_preview .edit').click(function () {

        var employment = $('#sms_preview_tab #user_employment').val();
        var min = $('#sms_preview_tab #min').val();
        var dept = $('#sms_preview_tab #dept').val();
        var statecode = $('#sms_preview_tab #stateCode').val();
        var Smi = $('#sms_preview_tab #Smi').val();
        var Org = $('#sms_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#sms_preview_tab #min');
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
                var select = $('#sms_preview_tab #dept');
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
                $('#sms_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#sms_preview_tab #stateCode');
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
                var select = $('#sms_preview_tab #Smi');
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
                $('#sms_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {

            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#sms_preview_tab #Org');
                select.find('option').remove();
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    console.log("Org:::" + Org + "val" + value)
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
                $('#sms_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#sms_preview_tab #central_div').hide();
            $('#sms_preview_tab #state_div').hide();
            $('#sms_preview_tab #other_div').hide();
        }

        $('#sms_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#sms_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#sms_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18

            $("#sms_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#sms_preview #confirm', function () {
        //$('#sms_preview #confirm').click(function () {

        $('#sms_preview').submit();
        // $('#large1').modal('toggle');
    });
    $('#sms_preview').submit(function (e) {


        e.preventDefault();
        $("#sms_preview :disabled").removeAttr('disabled');
        var sms_service = $("#sms_preview input[name='sms_service']:checked").map(function () {
            return this.value;
        }).get().join(',');


        $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march  
        $('#sms_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
        $('#sms_preview_tab #user_email').removeAttr('disabled'); // 20th march 
        var data = JSON.stringify($('#sms_preview').serializeObject());
        console.log("data on sms submit preview" + data + "sms_service" + sms_service)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        $('#sms_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#sms_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');// 20th march 

        $.ajax({
            type: "POST",
            url: "sms_tab5",
            data: {data: data, sms_service: sms_service, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.pull_url_error !== null && jsonvalue.error.pull_url_error !== "" && jsonvalue.error.pull_url_error !== undefined)
                {
                    $('#sms_preview #pull_url_err').html(jsonvalue.error.pull_url_error)
                    error_flag = false;
                    $('#sms_preview #pull_url').focus();
                } else {
                    $('#sms_preview #pull_url_err').html("")
                }
                if (jsonvalue.error.pull_keyword_error !== null && jsonvalue.error.pull_keyword_error !== "" && jsonvalue.error.pull_keyword_error !== undefined)
                {
                    $('#sms_preview #pull_keyword_err').html(jsonvalue.error.pull_keyword_error)
                    error_flag = false;
                    $('#sms_preview #pull_keyword').focus();
                } else {
                    $('#sms_preview #pull_keyword_err').html("")
                }
                if (jsonvalue.error.snote_error !== null && jsonvalue.error.snote_error !== "" && jsonvalue.error.snote_error !== undefined)
                {
                    $('#sms_preview #short_note_err').html(jsonvalue.error.snote_error)
                    error_flag = false;
                    $('#sms_preview #short_code').focus();
                } else {
                    $('#sms_preview #short_note_err').html("")
                }
                if (jsonvalue.error.app_name_error !== null && jsonvalue.error.app_name_error !== "" && jsonvalue.error.app_name_error !== undefined)
                {
                    $('#sms_preview #app_name_err').html(jsonvalue.error.app_name_error)

                    error_flag = false;
                    $('#sms_preview #app_name').focus();
                } else {
                    $('#sms_preview #app_name_err').html("")
                }
                if (jsonvalue.error.app_url_error !== null && jsonvalue.error.app_url_error !== "" && jsonvalue.error.app_url_error !== undefined)
                {
                    $('#sms_preview #app_url_err').html(jsonvalue.error.app_url_error)
                    error_flag = false;
                    $('#sms_preview #app_url').focus();
                } else {
                    $('#sms_preview #app_url_err').html("")
                }
                if (jsonvalue.error.purpose_error !== null && jsonvalue.error.purpose_error !== "" && jsonvalue.error.purpose_error !== undefined)
                {
                    $('#sms_preview #sms_usage_err').html(jsonvalue.error.purpose_error)
                    error_flag = false;
                    $('#sms_preview #sms_usage').focus();
                } else {
                    $('#sms_preview #sms_usage_err').html("")
                }
//                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
//                {
//                    $('#sms_preview #server_txt_err').html(jsonvalue.error.server_loc_error)
//                    error_flag = false;
//                    $('#sms_preview #server_loc_txt').focus();
//                } else {
//                    $('#sms_preview #server_txt_err').html("")
//                }
                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#sms_preview #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#sms_preview #server_loc').focus();
                    error_flag = false;
                } else {
                    $('#sms_preview #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#sms_preview #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#sms_preview #server_loc_txt').focus();
                    error_flag = false;
                } else {
                    $('#sms_preview #server_txt_err').html("")
                }

                if (jsonvalue.error.base_ip_error !== null && jsonvalue.error.base_ip_error !== "" && jsonvalue.error.base_ip_error !== undefined)
                {
                    $('#sms_preview #base_ip_err').html(jsonvalue.error.base_ip_error)
                    error_flag = false;
                    $('#sms_preview #base_ip').focus();
                } else {
                    $('#sms_preview #base_ip_err').html("")
                }
                if (jsonvalue.error.service_ip_error !== null && jsonvalue.error.service_ip_error !== "" && jsonvalue.error.service_ip_error !== undefined)
                {
                    $('#sms_preview #service_ip_err').html(jsonvalue.error.service_ip_error)
                    error_flag = false;
                    $('#sms_preview #service_ip').focus();
                } else {
                    $('#sms_preview #service_ip_err').html("")
                }

                if (jsonvalue.error.t_name_error !== null && jsonvalue.error.t_name_error !== "" && jsonvalue.error.t_name_error !== undefined)
                {
                    $('#sms_preview #tauth_off_name_err').html(jsonvalue.error.t_name_error)
                    error_flag = false;
                    $("#sms_preview input[name=t_off_name]").focus();

                } else {
                    $('#sms_preview #tauth_off_name_err').html("")
                }
                if (jsonvalue.error.t_desig_error !== null && jsonvalue.error.t_desig_error !== "" && jsonvalue.error.t_desig_error !== undefined)
                {
                    $('#sms_preview #tdesignation_err').html(jsonvalue.error.t_desig_error)
                    error_flag = false;
                    $("#sms_preview input[name=tdesignation]").focus();

                } else {
                    $('#sms_preview #tdesignation_err').html("")
                }
                if (jsonvalue.error.t_empcode_error !== null && jsonvalue.error.t_empcode_error !== "" && jsonvalue.error.t_empcode_error !== undefined)
                {
                    $('#sms_preview #temp_code_err').html(jsonvalue.error.t_empcode_error)
                    error_flag = false;
                    $("#sms_preview input[name=temp_code]").focus();

                } else {
                    $('#sms_preview #temp_code_err').html("")
                }
                if (jsonvalue.error.t_address_error !== null && jsonvalue.error.t_address_error !== "" && jsonvalue.error.t_address_error !== undefined)
                {
                    $('#sms_preview #tadd1error').html(jsonvalue.error.t_address_error)
                    error_flag = false;
                    $("#sms_preview input[name=taddrs]").focus();

                } else {
                    $('#sms_preview #tadd1error').html("")
                }
                if (jsonvalue.error.t_city_error !== null && jsonvalue.error.t_city_error !== "" && jsonvalue.error.t_city_error !== undefined)
                {
                    $('#sms_preview #tcity_err').html(jsonvalue.error.t_city_error)
                    error_flag = false;
                    $("#sms_preview input[name=tcity]").focus();

                } else {
                    $('#sms_preview #tcity_err').html("")
                }
                if (jsonvalue.error.t_state_error !== null && jsonvalue.error.t_state_error !== "" && jsonvalue.error.t_state_error !== undefined)
                {
                    $('#sms_preview #tstate_err').html(jsonvalue.error.t_state_error)
                    error_flag = false;
                    $('#sms_preview input[name=tstate]').focus();

                } else {
                    $('#sms_preview #tstate_err').html("")
                }
                if (jsonvalue.error.t_pin_error !== null && jsonvalue.error.t_pin_error !== "" && jsonvalue.error.t_pin_error !== undefined)
                {
                    $('#sms_preview #tpin_err').html(jsonvalue.error.t_pin_error)
                    error_flag = false;
                    $("#sms_preview input[name=tpin]").focus();

                } else {
                    $('#sms_preview #tpin_err').html("")
                }
                if (jsonvalue.error.t_tel1_error !== null && jsonvalue.error.t_tel1_error !== "" && jsonvalue.error.t_tel1_error !== undefined)
                {
                    $('#sms_preview #ttel_ofc_err').html(jsonvalue.error.t_tel1_error)
                    error_flag = false;
                    $("#sms_preview input[name=ttel_ofc]").focus();

                } else {
                    $('#sms_preview #ttel_ofc_err').html("")
                }
                if (jsonvalue.error.t_tel2_error !== null && jsonvalue.error.t_tel2_error !== "" && jsonvalue.error.t_tel2_error !== undefined)
                {
                    $('#sms_preview #ttel_res_err').html(jsonvalue.error.t_tel2_error)
                    error_flag = false;
                    $("#sms_preview input[name=ttel_res]").focus();

                } else {
                    $('#sms_preview #ttel_res_err').html("")
                }
                if (jsonvalue.error.t_mobile_error !== null && jsonvalue.error.t_mobile_error !== "" && jsonvalue.error.t_mobile_error !== undefined)
                {
                    $('#sms_preview #tmobile_err').html(jsonvalue.error.t_mobile_error)
                    error_flag = false;
                    $("#sms_preview input[name=tmobile]").focus();

                } else {
                    $('#sms_preview #tmobile_err').html("")
                }
                if (jsonvalue.error.t_email_error !== null && jsonvalue.error.t_email_error !== "" && jsonvalue.error.t_email_error !== undefined)
                {
                    $('#sms_preview #tauth_email_err').html(jsonvalue.error.t_email_error)
                    error_flag = false;
                    $("#sms_preview input[name=tauth_email]").focus();

                } else {
                    $('#sms_preview #tauth_email_err').html("")
                }
                if (jsonvalue.error.b_name_error !== null && jsonvalue.error.b_name_error !== "" && jsonvalue.error.b_name_error !== undefined)
                {
                    $('#sms_preview #bauth_off_name_err').html(jsonvalue.error.b_name_error)
                    error_flag = false;
                    $("#sms_preview input[name=bauth_off_name]").focus();

                } else {
                    $('#sms_preview #bauth_off_name_err').html("")
                }
                if (jsonvalue.error.b_desig_error !== null && jsonvalue.error.b_desig_error !== "" && jsonvalue.error.b_desig_error !== undefined)
                {
                    $('#sms_preview #bdesignation_err').html(jsonvalue.error.b_desig_error)
                    error_flag = false;
                    $("#sms_preview input[name=bdesignation]").focus();

                } else {
                    $('#sms_preview #bdesignation_err').html("")
                }
                if (jsonvalue.error.b_empcode_error !== null && jsonvalue.error.b_empcode_error !== "" && jsonvalue.error.b_empcode_error !== undefined)
                {
                    $('#sms_preview #bemp_code_err').html(jsonvalue.error.b_empcode_error)
                    error_flag = false;
                    $("#sms_preview input[name=bemp_code]").focus();

                } else {
                    $('#sms_preview #bemp_code_err').html("")
                }
                if (jsonvalue.error.b_address_error !== null && jsonvalue.error.b_address_error !== "" && jsonvalue.error.b_address_error !== undefined)
                {
                    $('#sms_preview #badd1error').html(jsonvalue.error.b_address_error)
                    error_flag = false;
                    $("#sms_preview input[name=baddrs]").focus();

                } else {
                    $('#sms_preview #badd1error').html("")
                }
                if (jsonvalue.error.b_city_error !== null && jsonvalue.error.b_city_error !== "" && jsonvalue.error.b_city_error !== undefined)
                {
                    $('#sms_preview #bcity_err').html(jsonvalue.error.b_city_error)
                    error_flag = false;
                    $("#sms_preview input[name=bcity]").focus();

                } else {
                    $('#sms_preview #bcity_err').html("")
                }
                if (jsonvalue.error.b_state_error !== null && jsonvalue.error.b_state_error !== "" && jsonvalue.error.b_state_error !== undefined)
                {
                    $('#sms_preview #bstate_err').html(jsonvalue.error.b_state_error)
                    error_flag = false;
                    $("#sms_preview input[name=bstate]").focus();

                } else {
                    $('#sms_preview #bstate_err').html("")
                }
                if (jsonvalue.error.b_pin_error !== null && jsonvalue.error.b_pin_error !== "" && jsonvalue.error.b_pin_error !== undefined)
                {
                    $('#sms_preview #bpin_err').html(jsonvalue.error.b_pin_error)
                    error_flag = false;
                    $("#sms_preview input[name=bpin]").focus();

                } else {
                    $('#sms_preview #bpin_err').html("")
                }
                if (jsonvalue.error.b_tel1_error !== null && jsonvalue.error.b_tel1_error !== "" && jsonvalue.error.b_tel1_error !== undefined)
                {
                    $('#sms_preview #btel_ofc_err').html(jsonvalue.error.b_tel1_error)
                    error_flag = false;
                    $("#sms_preview input[name=btel_ofc]").focus();

                } else {
                    $('#sms_preview #btel_ofc_err').html("")
                }
                if (jsonvalue.error.b_tel2_error !== null && jsonvalue.error.b_tel2_error !== "" && jsonvalue.error.b_tel2_error !== undefined)
                {
                    $('#sms_preview #btel_res_err').html(jsonvalue.error.b_tel2_error)
                    error_flag = false;
                    $("#sms_preview input[name=btel_res]").focus();

                } else {
                    $('#sms_preview #btel_res_err').html("")
                }
                if (jsonvalue.error.b_mobile_error !== null && jsonvalue.error.b_mobile_error !== "" && jsonvalue.error.b_mobile_error !== undefined)
                {
                    $('#sms_preview #bmobile_err').html(jsonvalue.error.b_mobile_error)
                    error_flag = false;
                    $("#sms_preview input[name=bmobile]").focus();

                } else {
                    $('#sms_preview #bmobile_err').html("")
                }
                if (jsonvalue.error.b_email_error !== null && jsonvalue.error.b_email_error !== "" && jsonvalue.error.b_email_error !== undefined)
                {
                    $('#sms_preview #bauth_email_err').html(jsonvalue.error.b_email_error)
                    error_flag = false;
                    $("#sms_preview input[name=bauth_email]").focus();

                } else {
                    $('#sms_preview #bauth_email_err').html("")
                }
                if (jsonvalue.error.ip_staging_error !== null && jsonvalue.error.ip_staging_error !== "" && jsonvalue.error.ip_staging_error !== undefined)
                {
                    $('#sms_preview #staging_ip_err').html(jsonvalue.error.ip_staging_error)
                    error_flag = false;
                    $('#sms_preview #staging_ip').focus();

                } else {
                    $('#sms_preview #staging_ip_err').html("")
                }

                if (jsonvalue.error.domestic_traf_error !== null && jsonvalue.error.domestic_traf_error !== "" && jsonvalue.error.domestic_traf_error !== undefined)
                {
                    $('#sms_preview #domestic_traf_error1').html(jsonvalue.error.domestic_traf_error)
                    error_flag = false;
                    $('#sms_preview #domestic_traf').focus();


                } else {
                    $('#sms_preview #domestic_traf_error').html("")
                }
                if (jsonvalue.error.inter_traf_error !== null && jsonvalue.error.inter_traf_error !== "" && jsonvalue.error.inter_traf_error !== undefined)
                {
                    $('#sms_preview #inter_traf_error').html(jsonvalue.error.inter_traf_error)
                    error_flag = false;
                    $('#sms_preview #inter_traf').focus();


                } else {
                    $('#sms_preview #inter_traf_error').html("")
                }
                if (jsonvalue.error.sender_id_error !== null && jsonvalue.error.sender_id_error !== "" && jsonvalue.error.sender_id_error !== undefined)
                {
                    $('#sms_preview #sender_id_err').html(jsonvalue.error.sender_id_error)
                    error_flag = false;
                    $('#sms_preview #sender_id').focus();


                } else {
                    $('#sms_preview #inter_traf_error').html("")
                }
                if (jsonvalue.error.audit_date_err !== null && jsonvalue.error.audit_date_err !== "" && jsonvalue.error.audit_date_err !== undefined)
                {
                    $('#sms_preview #audit_date_err1').html(jsonvalue.error.audit_date_err)
                    error_flag = false;
                    $('#sms_preview #audit_date_err1').focus();

                } else {
                    $('#sms_preview #audit_date_err1').html("")
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
                    $('#sms_preview #useremployment_error').focus();
                    $('#sms_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#sms_preview #minerror').focus();
                    $('#sms_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#sms_preview #deperror').focus();
                    $('#sms_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#sms_preview #other_dept').focus();
                    $('#sms_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#sms_preview #smierror').focus();
                    $('#sms_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#sms_preview #state_error').focus();
                    $('#sms_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#sms_preview #org_error').focus();
                    $('#sms_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#sms_preview #org_error').html("");
                }
                // profile 20th march 

                if (!error_flag)
                {

                    $("#sms_preview :disabled").removeAttr('disabled');
                    $('#sms_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#sms_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#large1').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });

    });



    $('#tstate').change(function () {

        var tstate = $("#tstate").val();

        $.get('getDistrict', {
            user_state: tstate
        }, function (response) {

            var select = $('#tcity');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {
                $('<option>').val(value).text(value).appendTo(select);
            });


        });

    })
    $('#bstate').change(function () {

        var bstate = $("#bstate").val();

        $.get('getDistrict', {
            user_state: bstate
        }, function (response) {

            var select = $('#bcity');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {
                $('<option>').val(value).text(value).appendTo(select);
            });


        });

    })

    $('#sms_form5 #tstate').change(function () {

        var tstate = $("#sms_form5 #tstate").val();

        $.get('getDistrict', {
            user_state: tstate
        }, function (response) {

            var select = $('#sms_form5 #tcity');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {
                $('<option>').val(value).text(value).appendTo(select);
            });


        });

    })



    $('#sms_form5 #bstate').change(function () {

        var tstate = $("#sms_form5 #bstate").val();

        $.get('getDistrict', {
            user_state: tstate
        }, function (response) {

            var select = $('#sms_form5 #bcity');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {
                $('<option>').val(value).text(value).appendTo(select);
            });


        });

    })


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