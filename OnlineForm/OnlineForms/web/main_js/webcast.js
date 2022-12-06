/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



$(document).ready(function () {

    var date = new Date();
    var currentMonth = date.getMonth();
    var currentDate = date.getDate();
    var currentYear = date.getFullYear();

    $('#cheque_date,#cheque_date1').datepicker({
        changeYear: true,
        changeMonth: true,
        minDate: new Date(currentYear, currentMonth - 3, currentDate),
        maxDate: new Date(currentYear, currentMonth + 3, currentDate),
        dateFormat: "dd-mm-yy"
    });

    $('#event_start_date,#event_start_date1').datetimepicker({
        todayHighlight: true,
        autoclose: true,
        pickerPosition: 'bottom-right',
        format: 'mm/dd/yyyy hh:ii'
    });
    $('#event_end_date,#event_end_date1').datetimepicker({
        todayHighlight: true,
        autoclose: true,
        pickerPosition: 'bottom-right',
        format: 'mm/dd/yyyy hh:ii'
    });


    $('#webcast_form1 #web_demand').find('input, textarea, button, select').attr('disabled', 'true');
    $("#webcast_form1 #web_live").find('input, textarea, button, select').removeAttr('disabled');


    $(document).on('click', '.page-container-bg-solid input', function () {
        //$('.page-container-bg-solid').find('input').click(function () {
        var field_form = $(this).closest('form').attr('id');
        var request_type = $('#' + field_form + " #request_type_div input[type=radio]:checked").val();

        if (request_type === 'live') {
            $('#' + field_form + ' #web_live').removeClass('display-hide');
            $('#' + field_form + ' #web_demand').addClass('display-hide');
            $('#' + field_form + ' #web_demand').find('input, textarea, button, select').attr('disabled', 'true');
            $('#' + field_form + " #web_live").find('input, textarea, button, select').removeAttr('disabled');
        } else if (request_type === 'demand') {
            $('#' + field_form + ' #web_live').addClass('display-hide');
            $('#' + field_form + ' #web_demand').removeClass('display-hide');
            $('#' + field_form + ' #web_live').find('input, textarea, button, select').attr('disabled', 'true');
            $('#' + field_form + " #web_demand").find('input, textarea, button, select').removeAttr('disabled');
        }
        var event_telecast = $('#' + field_form + " #event_telecast input[type=radio]:checked").val();
        if (event_telecast === 'yes') {
            $('#' + field_form + ' #telecast_yes_div').removeClass('display-hide');
            $('#' + field_form + ' #telecast_no_div').addClass('display-hide');
            $('#' + field_form + ' #vcid_div').addClass('display-hide');
            $('#' + field_form + ' #live_feed').val('Own Live encoding');
        } else if (event_telecast === 'no') {
            $('#' + field_form + ' #telecast_yes_div').addClass('display-hide');
            $('#' + field_form + ' #telecast_no_div').removeClass('display-hide');
            //$('#' + field_form + ' #vcid_div').removeClass('display-hide');
        }

        var payment = $('#' + field_form + " #payment_applicable_div input[type=radio]:checked").val();

        if (payment === 'yes') {
            $('#' + field_form + ' #payment_div').removeClass('display-hide');
        } else if (payment === 'no') {
            $('#' + field_form + ' #payment_div').addClass('display-hide');
        }

        var conf_contact = $('#' + field_form + " #conf_video_div input[type=radio]:checked").val();
        if (conf_contact === 'yes') {
            $('#' + field_form + ' #conf_contact_div').removeClass('display-hide');
        } else if (conf_contact === 'no') {
            $('#' + field_form + ' #conf_contact_div').addClass('display-hide');
        }

        var hall_type = $('#' + field_form + " #hall_div input[type=radio]:checked").val();
        if (hall_type === 'multiple') {
            $('#' + field_form + ' #hall_number').removeClass('display-hide');
            $('#' + field_form + ' #hall_number').css('display', 'inline');
        } else if (hall_type === 'single') {
            $('#' + field_form + ' #hall_number').addClass('display-hide');
            $('#' + field_form + ' #hall_number').css('display', 'none');
        }

        var conf_flash = $('#' + field_form + " #conf_flash_div input[type=radio]:checked").val();
       
        if (conf_flash === 'yes') {
            $('#' + field_form + ' #conf_flash_no_div').addClass('display-hide');
            $('#' + field_form + ' #conf_flash_no_div').css('display', 'none');
        } else if (conf_flash === 'no') {
            $('#' + field_form + ' #conf_flash_no_div').removeClass('display-hide');
            $('#' + field_form + ' #conf_flash_no_div').css('display', 'inline');
        }
        
         var con_event = $('#' + field_form + " #conf_event_div input[type=radio]:checked").val();
       
        if (con_event === 'yes') {
            $('#' + field_form + ' #flash_media_encoder').removeClass('display-hide');
           // $('#' + field_form + ' #conf_flash_no_div').css('display', 'none');
        } else if (con_event === 'no') {
            $('#' + field_form + ' #flash_media_encoder').addClass('display-hide');
            //$('#' + field_form + ' #conf_flash_no_div').css('display', 'inline');
        }
    });

    $('.page-container-bg-solid').find('select').change(function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        if (field_name === 'live_feed') {
            var live_feed = $(this).val();
            if (live_feed === 'Through VC') {
                $('#' + field_form + ' #vcid_div').removeClass('display-hide');
            } else {
                $('#' + field_form + ' #vcid_div').addClass('display-hide');
            }
        }
    });

    var emailregex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var nameregex = /^[a-zA-Z .,]{1,50}$/;
    var mobileregex = /^[+0-9]{10,13}$/;
    var phoneregex1 = /^[0-9]{3,5}[-]([0-9]{6,15})$/;
    var addregex = /^[a-zA-Z0-9 .,-\\#\\/\\(\\)]{2,100}$/;
    var desigregex = /^[a-zA-Z0-9 \.\,\-\&]{2,50}$/;

    $('#webcast_form1 #fwd_ofc_email').blur(function () {
        var hod_email = $('#webcast_form1 #fwd_ofc_email').val();
        var flag = false;
        if (hod_email.match(emailregex) && !flag) {
            $.ajax({
                type: "POST",
                url: "getHODdetails",
                data: {email: hod_email},
                datatype: JSON,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var flg = false;
                    //$('.' + field_name).remove();

                    if (jsonvalue.hodDetails.error === 'User Not valid') {
                        $('#webcast_form1 #fwd_ofc_mobile').prop('readonly', false);
                        $('#webcast_form1 #fwd_ofc_mobile').val("");
                        $('#webcast_form1 #fwd_ofc_name').prop('readonly', false);
                        $('#webcast_form1 #fwd_ofc_name').val("");
                        $('#webcast_form1 #fwd_ofc_tel').prop('readonly', false);
                        $('#webcast_form1 #fwd_ofc_tel').val("");
                        $('#webcast_form1 #fwd_ofc_desig').prop('readonly', false);
                        $('#webcast_form1 #fwd_ofc_desig').val("");
                        $('#webcast_form1 #fwd_ofc_add').prop('readonly', false);
                        $('#webcast_form1 #fwd_ofc_add').val("");
                        $('#ca_design').prop('readonly', false);
                        $('#ca_design').val("");
                        $('#webcast_form1 #fwd_email_err').html('This is not a valid Government email address.')

                    } else {
                        if (jsonvalue.hodDetails.email !== "") {
                            $('#webcast_form1 #fwd_ofc_email').val(jsonvalue.hodDetails.email);
                            if (jsonvalue.hodDetails.email.match(emailregex))
                            {
                                flg = true;
                            } else {
                                $('#webcast_form1 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            }
                        } else {
                            $('#webcast_form1 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            $('#hod_email').val("");
                        }
                        if (jsonvalue.hodDetails.mobile !== "") {
                            $('#webcast_form1 #fwd_ofc_mobile').val(jsonvalue.hodDetails.mobile);
                            if (jsonvalue.hodDetails.mobile.match(mobileregex))
                            {
                                flg = true;
                                $('#webcast_form1 #fwd_ofc_mobile').prop('readonly', true);
                            } else {
                                $('#webcast_form1 #fwd_mobile_err').html("Enter Mobile Number [e.g: +919999999999]");
                                $('#webcast_form1 #fwd_ofc_mobile').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form1 #fwd_ofc_mobile').val("");
                            $('#webcast_form1 #fwd_ofc_mobile').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.cn !== "") {
                            $('#webcast_form1 #fwd_ofc_name').val(jsonvalue.hodDetails.cn);
                            if (jsonvalue.hodDetails.cn.match(nameregex))
                            {
                                flg = true;
                                $('#webcast_form1 #fwd_ofc_name').prop('readonly', true);
                            } else {
                                $('#webcast_form1 #fwd_name_err').html("Enter Name [characters,dot(.) and whitespaces]");
                                $('#webcast_form1 #fwd_ofc_name').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form1 #fwd_ofc_name').val("");
                            $('#webcast_form1 #fwd_ofc_name').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.ophone !== "") {
                            $('#webcast_form1 #fwd_ofc_tel').val(jsonvalue.hodDetails.ophone);

                            if (jsonvalue.hodDetails.ophone.match(phoneregex1))
                            {
                                flg = true;
                                $('#webcast_form1 #fwd_ofc_tel').prop('readonly', true);
                            } else {
                                $('#webcast_form1 #fwd_tel_err').html("Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                                $('#webcast_form1 #fwd_ofc_tel').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form1 #fwd_ofc_tel').val("");
                            $('#webcast_form1 #fwd_ofc_tel').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.desig !== "" && jsonvalue.hodDetails.desig !== "null") {

                            $('#webcast_form1 #fwd_ofc_desig').val(jsonvalue.hodDetails.desig);
                            if (jsonvalue.hodDetails.desig.match(desigregex))
                            {
                                flg = true;
                                $('#webcast_form1 #fwd_ofc_desig').prop('readonly', true);
                            } else {
                                $('#webcast_form1 #fwd_desig_err').html("Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespaces and [. , - &]]]");
                                $('#webcast_form1 #fwd_ofc_desig').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form1 #fwd_ofc_desig').val("");
                            $('#webcast_form1 #fwd_ofc_desig').prop('readonly', false);
                        }

                        if (jsonvalue.hodDetails.postalAddress !== "" && jsonvalue.hodDetails.postalAddress !== "null") {

                            $('#webcast_form1 #fwd_ofc_add').val(jsonvalue.hodDetails.postalAddress);
                            if (jsonvalue.hodDetails.postalAddress.match(addregex))
                            {
                                flg = true;
                                $('#webcast_form1 #fwd_ofc_add').prop('readonly', true);
                            } else {
                                $('#webcast_form1 #fwd_add_err').html("Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                                $('#webcast_form1 #fwd_ofc_add').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form1 #fwd_ofc_add').val("");
                            $('#webcast_form1 #fwd_ofc_add').prop('readonly', false);
                        }
                        if (flg) {
                            $('#rodetails').removeClass("display-hide");
                        }
                    }
                    //}
                },
                error: function () {
                    console.log('error');
                }
            });
        } else {

            //$(this).parent().append('<span class="' + field_name + '" style="color:#f00;">' + fPlace + '</span>');
            return false;

        }
    });

    $('#webcast_form2 #fwd_ofc_email').blur(function () {
        var hod_email = $('#webcast_form2 #fwd_ofc_email').val();
        var flag = false;
        if (hod_email.match(emailregex) && !flag) {
            $.ajax({
                type: "POST",
                url: "getHODdetails",
                data: {email: hod_email},
                datatype: JSON,
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);
                    var flg = false;
                    //$('.' + field_name).remove();

                    if (jsonvalue.hodDetails.error === 'User Not valid') {
                        $('#webcast_form2 #fwd_ofc_mobile').prop('readonly', false);
                        $('#webcast_form2 #fwd_ofc_mobile').val("");
                        $('#webcast_form2 #fwd_ofc_name').prop('readonly', false);
                        $('#webcast_form2 #fwd_ofc_name').val("");
                        $('#webcast_form2 #fwd_ofc_tel').prop('readonly', false);
                        $('#webcast_form2 #fwd_ofc_tel').val("");
                        $('#webcast_form2 #fwd_ofc_desig').prop('readonly', false);
                        $('#webcast_form2 #fwd_ofc_desig').val("");
                        $('#webcast_form2 #fwd_ofc_add').prop('readonly', false);
                        $('#webcast_form2 #fwd_ofc_add').val("");
                        $('#ca_design').prop('readonly', false);
                        $('#ca_design').val("");
                        $('#webcast_form2 #fwd_email_err').html('This is not a valid Government email address.')

                    } else {
                        if (jsonvalue.hodDetails.email !== "") {
                            $('#webcast_form2 #fwd_ofc_email').val(jsonvalue.hodDetails.email);
                            if (jsonvalue.hodDetails.email.match(emailregex))
                            {
                                flg = true;
                            } else {
                                $('#webcast_form2 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            }
                        } else {
                            $('#webcast_form2 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            $('#hod_email').val("");
                        }
                        if (jsonvalue.hodDetails.mobile !== "") {
                            $('#webcast_form2 #fwd_ofc_mobile').val(jsonvalue.hodDetails.mobile);
                            if (jsonvalue.hodDetails.mobile.match(mobileregex))
                            {
                                flg = true;
                                $('#webcast_form2 #fwd_ofc_mobile').prop('readonly', true);
                            } else {
                                $('#webcast_form2 #fwd_mobile_err').html("Enter Mobile Number [e.g: +919999999999]");
                                $('#webcast_form2 #fwd_ofc_mobile').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form2 #fwd_ofc_mobile').val("");
                            $('#webcast_form2 #fwd_ofc_mobile').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.cn !== "") {
                            $('#webcast_form2 #fwd_ofc_name').val(jsonvalue.hodDetails.cn);
                            if (jsonvalue.hodDetails.cn.match(nameregex))
                            {
                                flg = true;
                                $('#webcast_form2 #fwd_ofc_name').prop('readonly', true);
                            } else {
                                $('#webcast_form2 #fwd_name_err').html("Enter Name [characters,dot(.) and whitespaces]");
                                $('#webcast_form2 #fwd_ofc_name').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form2 #fwd_ofc_name').val("");
                            $('#webcast_form2 #fwd_ofc_name').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.ophone !== "") {
                            $('#webcast_form2 #fwd_ofc_tel').val(jsonvalue.hodDetails.ophone);

                            if (jsonvalue.hodDetails.ophone.match(phoneregex1))
                            {
                                flg = true;
                                $('#webcast_form2 #fwd_ofc_tel').prop('readonly', true);
                            } else {
                                $('#webcast_form2 #fwd_tel_err').html("Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                                $('#webcast_form2 #fwd_ofc_tel').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form2 #fwd_ofc_tel').val("");
                            $('#webcast_form2 #fwd_ofc_tel').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.desig !== "" && jsonvalue.hodDetails.desig !== "null") {

                            $('#webcast_form2 #fwd_ofc_desig').val(jsonvalue.hodDetails.desig);
                            if (jsonvalue.hodDetails.desig.match(desigregex))
                            {
                                flg = true;
                                $('#webcast_form2 #fwd_ofc_desig').prop('readonly', true);
                            } else {
                                $('#webcast_form2 #fwd_desig_err').html("Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespaces and [. , - &]]]");
                                $('#webcast_form2 #fwd_ofc_desig').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form2 #fwd_ofc_desig').val("");
                            $('#webcast_form2 #fwd_ofc_desig').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.postalAddress !== "" && jsonvalue.hodDetails.postalAddress !== "null") {

                            $('#webcast_form2 #fwd_ofc_add').val(jsonvalue.hodDetails.postalAddress);
                            if (jsonvalue.hodDetails.postalAddress.match(addregex))
                            {
                                flg = true;
                                $('#webcast_form2 #fwd_ofc_add').prop('readonly', true);
                            } else {
                                $('#webcast_form2 #fwd_add_err').html("Enter Postal Address [Only characters,digits,whitespace and [. , - # / ( ) ] allowed]");
                                $('#webcast_form2 #fwd_ofc_add').prop('readonly', false);
                            }
                        } else {
                            $('#webcast_form2 #fwd_ofc_add').val("");
                            $('#webcast_form2 #fwd_ofc_add').prop('readonly', false);
                        }

                        if (flg) {
                            $('#rodetails').removeClass("display-hide");
                        }
                    }
                    //}
                },
                error: function () {
                    console.log('error');
                }
            });
        } else {

            //$(this).parent().append('<span class="' + field_name + '" style="color:#f00;">' + fPlace + '</span>');
            return false;

        }
    });

    $('#webcast_form1').submit(function (e) {
        e.preventDefault();
        var cert = $('#cert').val();
        var data = JSON.stringify($('#webcast_form1').serializeObject());
        var validateForm = $('.page-container-bg-solid #webcast_form1 input,textarea,select').blur();

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18                       
        if (validateForm) {
            $.ajax({
                type: "POST",
                url: "webcast_tab1",
                data: {data: data, CSRFRandom: CSRFRandom, cert: cert},
                datatype: JSON,
                success: function (data) {

                    resetCSRFRandom();// line added by pr on 22ndjan18

                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);

                    var error_flag = true;

                    if (jsonvalue.error.req_for_err !== null && jsonvalue.error.req_for_err !== "" && jsonvalue.error.req_for_err !== undefined)
                    {
                        $('#webcast_form1 #req_for_err').html(jsonvalue.error.req_for_err);
                        $('#webcast_form1 #req_for_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #req_for_err').html("");
                    }

                    if (jsonvalue.returnString === "live") {

                        if (jsonvalue.error.event_coo_name_err !== null && jsonvalue.error.event_coo_name_err !== "" && jsonvalue.error.event_coo_name_err !== undefined)
                        {
                            $('#webcast_form1 #event_coo_name_err').html(jsonvalue.error.event_coo_name_err);
                            $('#webcast_form1 #event_coo_name_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_coo_name_err').html("");
                        }
                        if (jsonvalue.error.event_coo_desig_err !== null && jsonvalue.error.event_coo_desig_err !== "" && jsonvalue.error.event_coo_desig_err !== undefined)
                        {
                            $('#webcast_form1 #event_coo_desig_err').html(jsonvalue.error.event_coo_desig_err);
                            $('#webcast_form1 #event_coo_desig_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_coo_desig_err').html("");
                        }
                        if (jsonvalue.error.event_coo_mobile_err !== null && jsonvalue.error.event_coo_mobile_err !== "" && jsonvalue.error.event_coo_mobile_err !== undefined)
                        {
                            $('#webcast_form1 #event_coo_mobile_err').html(jsonvalue.error.event_coo_mobile_err);
                            $('#webcast_form1 #event_coo_mobile_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_coo_mobile_err').html("");
                        }
                        if (jsonvalue.error.event_coo_email_err !== null && jsonvalue.error.event_coo_email_err !== "" && jsonvalue.error.event_coo_email_err !== undefined)
                        {
                            $('#webcast_form1 #event_coo_email_err').html(jsonvalue.error.event_coo_email_err);
                            $('#webcast_form1 #event_coo_email_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_coo_email_err').html("");
                        }
                        if (jsonvalue.error.event_coo_address_err !== null && jsonvalue.error.event_coo_address_err !== "" && jsonvalue.error.event_coo_address_err !== undefined)
                        {
                            $('#webcast_form1 #event_coo_address_err').html(jsonvalue.error.event_coo_address_err);
                            $('#webcast_form1 #event_coo_address_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_coo_address_err').html("");
                        }

                        if (jsonvalue.error.event_name_eng_err !== null && jsonvalue.error.event_name_eng_err !== "" && jsonvalue.error.event_name_eng_err !== undefined)
                        {
                            $('#webcast_form1 #event_name_eng_err').html(jsonvalue.error.event_name_eng_err);
                            $('#webcast_form1 #event_name_eng_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_name_eng_err').html("");
                        }
                        if (jsonvalue.error.event_name_hin_err !== null && jsonvalue.error.event_name_hin_err !== "" && jsonvalue.error.event_name_hin_err !== undefined)
                        {
                            $('#webcast_form1 #event_name_hin_err').html(jsonvalue.error.event_name_hin_err);
                            $('#webcast_form1 #event_name_hin_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_name_hin_err').html("");
                        }
                        
                       
                        if (jsonvalue.error.event_startdate_err !== null && jsonvalue.error.event_startdate_err !== "NA" && jsonvalue.error.event_startdate_err !== undefined)
                        {
                            $('#webcast_form1 #event_startdate_err').html(jsonvalue.error.event_startdate_err);
                            $('#webcast_form1 #event_startdate_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_startdate_err').html("");
                        }
                         if (jsonvalue.error.event_enddate_err !== null && jsonvalue.error.event_enddate_err !== "NA" && jsonvalue.error.event_enddate_err !== undefined)
                        {
                            $('#webcast_form1 #event_enddate_err').html(jsonvalue.error.event_enddate_err);
                            $('#webcast_form1 #event_enddate_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_enddate_err').html("");
                        }
                        if (jsonvalue.error.event_type_err !== null && jsonvalue.error.event_type_err !== "" && jsonvalue.error.event_type_err !== undefined)
                        {
                            $('#webcast_form1 #event_type_err').html(jsonvalue.error.event_type_err);
                            $('#webcast_form1 #event_type_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_type_err').html("");
                        }
                        if (jsonvalue.error.telecast_err !== null && jsonvalue.error.telecast_err !== "" && jsonvalue.error.telecast_err !== undefined)
                        {
                            $('#webcast_form1 #telecast_err').html(jsonvalue.error.telecast_err);
                            $('#webcast_form1 #telecast_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #telecast_err').html("");
                        }
                        if (jsonvalue.error.channel_name_err !== null && jsonvalue.error.channel_name_err !== "" && jsonvalue.error.channel_name_err !== undefined)
                        {
                            $('#webcast_form1 #channel_name_err').html(jsonvalue.error.channel_name_err);
                            $('#webcast_form1 #channel_name_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #channel_name_err').html("");
                        }
                        if (jsonvalue.error.live_feed_err !== null && jsonvalue.error.live_feed_err !== "" && jsonvalue.error.live_feed_err !== undefined)
                        {
                            $('#webcast_form1 #live_feed_err').html(jsonvalue.error.live_feed_err);
                            $('#webcast_form1 #live_feed_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #live_feed_err').html("");
                        }
                        if (jsonvalue.error.vc_id_err !== null && jsonvalue.error.vc_id_err !== "" && jsonvalue.error.vc_id_err !== undefined)
                        {
                            $('#webcast_form1 #vc_id_err').html(jsonvalue.error.vc_id_err);
                            $('#webcast_form1 #vc_id_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #vc_id_err').html("");
                        }
                        if (jsonvalue.error.conf_radio_err !== null && jsonvalue.error.conf_radio_err !== "" && jsonvalue.error.conf_radio_err !== undefined)
                        {
                            $('#webcast_form1 #conf_radio_err').html(jsonvalue.error.conf_radio_err);
                            $('#webcast_form1 #conf_radio_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_radio_err').html("");
                        }
                        if (jsonvalue.error.conf_name_err !== null && jsonvalue.error.conf_name_err !== "" && jsonvalue.error.conf_name_err !== undefined)
                        {
                            $('#webcast_form1 #conf_name_err').html(jsonvalue.error.conf_name_err);
                            $('#webcast_form1 #conf_name_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_name_err').html("");
                        }
                        if (jsonvalue.error.conf_type_err !== null && jsonvalue.error.conf_type_err !== "" && jsonvalue.error.conf_type_err !== undefined)
                        {
                            $('#webcast_form1 #conf_type_err').html(jsonvalue.error.conf_type_err);
                            $('#webcast_form1 #conf_type_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_type_err').html("");
                        }
                        if (jsonvalue.error.conf_city_err !== null && jsonvalue.error.conf_city_err !== "" && jsonvalue.error.conf_city_err !== undefined)
                        {
                            $('#webcast_form1 #conf_city_err').html(jsonvalue.error.conf_city_err);
                            $('#webcast_form1 #conf_city_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_city_err').html("");
                        }
                        if (jsonvalue.error.conf_schedule_err !== null && jsonvalue.error.conf_schedule_err !== "" && jsonvalue.error.conf_schedule_err !== undefined)
                        {
                            $('#webcast_form1 #conf_schedule_err').html(jsonvalue.error.conf_schedule_err);
                            $('#webcast_form1 #conf_schedule_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_schedule_err').html("");
                        }
                        if (jsonvalue.error.conf_session_err !== null && jsonvalue.error.conf_session_err !== "" && jsonvalue.error.conf_session_err !== undefined)
                        {
                            $('#webcast_form1 #conf_session_err').html(jsonvalue.error.conf_session_err);
                            $('#webcast_form1 #conf_session_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_session_err').html("");
                        }
                        if (jsonvalue.error.conf_bw_err !== null && jsonvalue.error.conf_bw_err !== "" && jsonvalue.error.conf_bw_err !== undefined)
                        {
                            $('#webcast_form1 #conf_bw_err').html(jsonvalue.error.conf_bw_err);
                            $('#webcast_form1 #conf_bw_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_bw_err').html("");
                        }
                        if (jsonvalue.error.conf_provider_err !== null && jsonvalue.error.conf_provider_err !== "" && jsonvalue.error.conf_provider_err !== undefined)
                        {
                            $('#webcast_form1 #conf_provider_err').html(jsonvalue.error.conf_provider_err);
                            $('#webcast_form1 #conf_provider_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_provider_err').html("");
                        }
                        if (jsonvalue.error.conf_event_hired_err !== null && jsonvalue.error.conf_event_hired_err !== "" && jsonvalue.error.conf_event_hired_err !== undefined)
                        {
                            $('#webcast_form1 #conf_event_hired_err').html(jsonvalue.error.conf_event_hired_err);
                            $('#webcast_form1 #conf_event_hired_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_event_hired_err').html("");
                        }
                        if (jsonvalue.error.conf_flash_err !== null && jsonvalue.error.conf_flash_err !== "" && jsonvalue.error.conf_flash_err !== undefined)
                        {
                            $('#webcast_form1 #conf_flash_err').html(jsonvalue.error.conf_flash_err);
                            $('#webcast_form1 #conf_flash_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_flash_err').html("");
                        }
                        if (jsonvalue.error.conf_video_err !== null && jsonvalue.error.conf_video_err !== "" && jsonvalue.error.conf_video_err !== undefined)
                        {
                            $('#webcast_form1 #conf_video_err').html(jsonvalue.error.conf_video_err);
                            $('#webcast_form1 #conf_video_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_video_err').html("");
                        }
                        if (jsonvalue.error.conf_contact_err !== null && jsonvalue.error.conf_contact_err !== "" && jsonvalue.error.conf_contact_err !== undefined)
                        {
                            $('#webcast_form1 #conf_contact_err').html(jsonvalue.error.conf_contact_err);
                            $('#webcast_form1 #conf_contact_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #conf_contact_err').html("");
                        }
                        if (jsonvalue.error.hall_type_err !== null && jsonvalue.error.hall_type_err !== "" && jsonvalue.error.hall_type_err !== undefined)
                        {
                            $('#webcast_form1 #hall_type_err').html(jsonvalue.error.hall_type_err);
                            $('#webcast_form1 #hall_type_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #hall_type_err').html("");
                        }

                        if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                        {
                            $('#webcast_form1 #file_err').html(jsonvalue.error.file_err)
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #file_err').html("")
                        }
                    } else if (jsonvalue.returnString === "demand") {

                        if (jsonvalue.error.event_no_err !== null && jsonvalue.error.event_no_err !== "" && jsonvalue.error.event_no_err !== undefined)
                        {
                            $('#webcast_form1 #event_no_err').html(jsonvalue.error.event_no_err);
                            $('#webcast_form1 #event_no_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_no_err').html("");
                        }
                        if (jsonvalue.error.event_size_err !== null && jsonvalue.error.event_size_err !== "" && jsonvalue.error.event_size_err !== undefined)
                        {
                            $('#webcast_form1 #event_size_err').html(jsonvalue.error.event_size_err);
                            $('#webcast_form1 #event_size_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #event_size_err').html("");
                        }
                        if (jsonvalue.error.media_format_err !== null && jsonvalue.error.media_format_err !== "" && jsonvalue.error.media_format_err !== undefined)
                        {
                            $('#webcast_form1 #media_format_err').html(jsonvalue.error.media_format_err);
                            $('#webcast_form1 #media_format_err').focus();
                            error_flag = false;
                        } else {
                            $('#webcast_form1 #media_format_err').html("");
                        }
                    }

                    if (jsonvalue.error.payment_err !== null && jsonvalue.error.payment_err !== "" && jsonvalue.error.payment_err !== undefined)
                    {
                        $('#webcast_form1 #payment_err').html(jsonvalue.error.payment_err);
                        $('#webcast_form1 #payment_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #payment_err').html("");
                    }
                    if (jsonvalue.error.cheque_no_err !== null && jsonvalue.error.cheque_no_err !== "" && jsonvalue.error.cheque_no_err !== undefined)
                    {
                        $('#webcast_form1 #cheque_no_err').html(jsonvalue.error.cheque_no_err);
                        $('#webcast_form1 #cheque_no_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #cheque_no_err').html("");
                    }
                    if (jsonvalue.error.amount_err !== null && jsonvalue.error.amount_err !== "" && jsonvalue.error.amount_err !== undefined)
                    {
                        $('#webcast_form1 #amount_err').html(jsonvalue.error.amount_err);
                        $('#webcast_form1 #amount_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #amount_err').html("");
                    }
                    if (jsonvalue.error.cheque_date_err !== null && jsonvalue.error.cheque_date_err !== "" && jsonvalue.error.cheque_date_err !== undefined)
                    {
                        $('#webcast_form1 #cheque_date_err').html(jsonvalue.error.cheque_date_err);
                        $('#webcast_form1 #cheque_date_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #cheque_date_err').html("");
                    }
                    if (jsonvalue.error.bank_name_err !== null && jsonvalue.error.bank_name_err !== "" && jsonvalue.error.bank_name_err !== undefined)
                    {
                        $('#webcast_form1 #bank_name_err').html(jsonvalue.error.bank_name_err);
                        $('#webcast_form1 #bank_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #bank_name_err').html("");
                    }
                    if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                    {
                        $('#webcast_form1 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                        $('#webcast_form1 #fwd_ofc_mobile').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_mobile_err').html("");
                    }
                    if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                    {
                        $('#webcast_form1 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                        $('#webcast_form1 #fwd_ofc_email').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_email_err').html("");
                    }

                    if (jsonvalue.error.fwd_email_err1 !== null && jsonvalue.error.fwd_email_err1 !== "" && jsonvalue.error.fwd_email_err1 !== undefined)
                    {
                        $('#webcast_form1 #fwd_email_err1').html(jsonvalue.error.fwd_email_err1);
                        $('#webcast_form1 #fwd_ofc_email').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_email_err1').html("");
                    }

                    if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                    {
                        $('#webcast_form1 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                        $('#webcast_form1 #fwd_ofc_name').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_name_err').html("");
                    }
                    if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                    {
                        $('#webcast_form1 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                        $('#webcast_form1 #fwd_ofc_desig').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_desig_err').html("");
                    }
                    if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                    {
                        $('#webcast_form1 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                        $('#webcast_form1 #fwd_ofc_add').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_add_err').html("");
                    }
                    if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                    {
                        $('#webcast_form1 #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                        $('#webcast_form1 #fwd_ofc_tel').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #fwd_tel_err').html("");
                    }

                    if (jsonvalue.error.remarks_err !== null && jsonvalue.error.remarks_err !== "" && jsonvalue.error.remarks_err !== undefined)
                    {
                        $('#webcast_form1 #remarks_err').html(jsonvalue.error.remarks_err);
                        $('#webcast_form1 #remarks_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form1 #remarks_err').html("");
                    }



                    // start, code added by pr on 22ndjan18

                    if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                    {
                        $('#webcast_form1 #captchaerror').html(jsonvalue.error.cap_error);
                        error_flag = false;
                    }

                    // end, code added by pr on 22ndjan18

                    if (!error_flag) {
                        $('#webcast_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                        $('#webcast_form1 #imgtxt').val("");
                        $('#webcast_form1 #webcast_error').html("There is some error! Please check above fields");
                    } else {
                         $('#webcast_form1 #webcast_error').html("");
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
                        $('#webcast_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                        $('#webcast_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                        $('#webcast_form2 #user_name').val(jsonvalue.profile_values.cn);
                        $('#webcast_form2 #user_design').val(jsonvalue.profile_values.desig);
                        $('#webcast_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                        $('#webcast_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                        $('#webcast_form2 #user_state').val(jsonvalue.profile_values.state);
                        $('#webcast_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                        $('#webcast_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                        $('#webcast_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                        $('#webcast_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                        // $('#webcast_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                        $('#webcast_form2 #user_email').val(jsonvalue.profile_values.email);
                        $('#webcast_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                        $('#webcast_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                        $('#webcast_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                        $('#webcast_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                        // $('#webcast_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                        var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);

                        $('#webcast_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                        $('#webcast_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                        $('#webcast_form2 #fwd_ofc_name').val(jsonvalue.form_details.fwd_ofc_name);
                        $('#webcast_form2 #fwd_ofc_email').val(jsonvalue.form_details.fwd_ofc_email);
                        $('#webcast_form2 #fwd_ofc_tel').val(jsonvalue.form_details.fwd_ofc_tel);
                        $('#webcast_form2 #fwd_ofc_mobile').val(jsonvalue.form_details.fwd_ofc_mobile);
                        $('#webcast_form2 #fwd_ofc_design').val(jsonvalue.form_details.fwd_ofc_design);
                        $('#webcast_form2 #fwd_ofc_add').val(jsonvalue.form_details.fwd_ofc_add);


                        $('#webcast_form2 #req_for').val(jsonvalue.form_details.req_for);
                        $('#webcast_form2 #req_for_span').html(jsonvalue.form_details.req_for);
                        if (jsonvalue.form_details.req_for === 'live') {

                            $('#webcast_form2 #web_live').removeClass('display-hide');
                            $('#webcast_form2 #web_demand').addClass('display-hide');
                            $('#webcast_form2 #web_demand').find('input, textarea, button, select').attr('disabled', 'true');
                            $('#webcast_form2 #web_live').find('input, textarea, button, select').removeAttr('disabled');

                            $('#webcast_form2 #event_coo_name').val(jsonvalue.form_details.event_coo_name);
                            $('#webcast_form2 #event_coo_email').val(jsonvalue.form_details.event_coo_email);
                            $('#webcast_form2 #event_coo_design').val(jsonvalue.form_details.event_coo_design);
                            $('#webcast_form2 #event_coo_mobile').val(jsonvalue.form_details.event_coo_mobile);
                            $('#webcast_form2 #event_coo_address').val(jsonvalue.form_details.event_coo_address);
                            $('#webcast_form2 #event_name_eng').val(jsonvalue.form_details.event_name_eng);
                            $('#webcast_form2 #event_name_hin').val(jsonvalue.form_details.event_name_hin);
                            $('#webcast_form2 #event_start_date1').val(jsonvalue.form_details.event_start_date);
                            $('#webcast_form2 #event_end_date1').val(jsonvalue.form_details.event_end_date);
                            $('#webcast_form2 #event_type').val(jsonvalue.form_details.event_type);
                            if (jsonvalue.form_details.telecast === 'yes') {
                                $("#webcast_form2 #telecast_yes").prop('checked', true);
                                $('#webcast_form2 #telecast_yes_div').removeClass('display-hide');
                                $('#webcast_form2 #telecast_no_div').addClass('display-hide');
                                $('#webcast_form2 #vcid_div').addClass('display-hide');
                                $('#webcast_form2 #channel_name').val(jsonvalue.form_details.channel_name);
                            } else {
                                $("#webcast_form2 #telecast_no").prop('checked', true);
                                $('#webcast_form2 #telecast_yes_div').addClass('display-hide');
                                $('#webcast_form2 #telecast_no_div').removeClass('display-hide');
                                $('#webcast_form2 #live_feed').val(jsonvalue.form_details.live_feed);

                                if (jsonvalue.form_details.live_feed === 'Through VC') {
                                    $('#webcast_form2 #vcid_div').removeClass('display-hide');
                                    $('#webcast_form2 #vc_id').val(jsonvalue.form_details.vc_id);
                                } else {
                                    $('#webcast_form2 #vcid_div').addClass('display-hide');
                                }
                            }
                            if (jsonvalue.form_details.conf_radio === 'yes') {
                                $("#webcast_form2 #conf_radio_yes").prop('checked', true);
                            } else {
                                $("#webcast_form2 #conf_radio_no").prop('checked', true);
                            }
                            $('#webcast_form2 #conf_name').val(jsonvalue.form_details.conf_name);
                            $('#webcast_form2 #conf_type').val(jsonvalue.form_details.conf_type);
                            $('#webcast_form2 #conf_city').val(jsonvalue.form_details.conf_city);
                            $('#webcast_form2 #conf_schedule').val(jsonvalue.form_details.conf_schedule);
                            $('#webcast_form2 #conf_session').val(jsonvalue.form_details.conf_session);
                            $('#webcast_form2 #conf_bw').val(jsonvalue.form_details.conf_bw);
                            $('#webcast_form2 #conf_provider').val(jsonvalue.form_details.conf_provider);
                           
                            if (jsonvalue.form_details.conf_event_hired === 'yes') {
                                $("#webcast_form2 #conf_event_hired_yes").prop('checked', true);
                                $('#webcast_form2 #flash_media_encoder').removeClass('display-hide');
                            } else {
                                $("#webcast_form2 #conf_event_hired_no").prop('checked', true);
                                $('#webcast_form2 #flash_media_encoder').addClass('display-hide');
                            }
                            if (jsonvalue.form_details.conf_flash === 'yes') {
                                $("#webcast_form2 #conf_flash_yes").prop('checked', true);
                                $('#webcast_form2 #conf_flash_no_div').addClass('display-hide');
                                $('#webcast_form2 #conf_flash_no_div').css('display', 'none');
                            } else {
                                $("#webcast_form2 #conf_flash_no").prop('checked', true);
                                $('#webcast_form2 #conf_flash_no_div').css('display', 'inline');
                                $('#webcast_form2 #conf_flash_no_div').removeClass('display-hide');
                                $('#webcast_form2 #local_setup').val(jsonvalue.form_details.local_setup);
                            }
                            if (jsonvalue.form_details.conf_video === 'yes') {
                                $("#webcast_form2 #conf_video_yes").prop('checked', true);
                            } else {
                                $("#webcast_form2 #conf_video_no").prop('checked', true);
                            }

                            if (jsonvalue.form_details.conf_video === 'yes') {
                                $('#webcast_form2 #conf_contact_div').removeClass('display-hide');
                                $('#webcast_form2 #conf_contact').val(jsonvalue.form_details.conf_contact);
                            } else if (jsonvalue.form_details.conf_video === 'no') {
                                $('#webcast_form2 #conf_contact_div').addClass('display-hide');
                                $('#webcast_form2 #conf_contact').val('');
                            }
                            if (jsonvalue.form_details.hall_type === 'multiple') {
                                $("#webcast_form2 #hall_multiple").prop('checked', true);
                                $('#webcast_form2 #hall_number').removeClass('display-hide');
                                $('#webcast_form2 #hall_number').css('display', 'inline');
                                $('#webcast_form2 #hall_number').val(jsonvalue.form_details.hall_number);
                            } else if (jsonvalue.form_details.hall_type === 'single') {
                                $("#webcast_form2 #hall_single").prop('checked', true);
                                $('#webcast_form2 #hall_number').addClass('display-hide');
                                $('#webcast_form2 #hall_number').css('display', 'none');
                                $('#webcast_form2 #hall_number').val("");
                            }
                            if (cert === 'true') {
                                $('#webcast_form2 #cert_div').removeClass('display-hide');
                                var fileString = "";
                                $.each(jsonvalue.form_details.webcast_uploaded_files, function (key, value) {
                                    fileString += '<span>' + key + '</span><br/>';
                                });
                                $("#webcast_form2 #webcast_files").append(fileString);
                            } else {
                                $('#webcast_form2 #cert_div').addClass('display-hide');
                            }
                        } else if (jsonvalue.form_details.req_for === 'demand') {
                            $('#webcast_form2 #web_live').addClass('display-hide');
                            $('#webcast_form2 #web_demand').removeClass('display-hide');
                            $('#webcast_form2 #web_live').find('input, textarea, button, select').attr('disabled', 'true');
                            $('#webcast_form2 #web_demand').find('input, textarea, button, select').removeAttr('disabled');
                            $('#webcast_form2 #event_no').val(jsonvalue.form_details.event_no);
                            $('#webcast_form2 #event_size').val(jsonvalue.form_details.event_size);
                            $('#webcast_form2 #media_format').val(jsonvalue.form_details.media_format);
                        }

                        if (jsonvalue.form_details.payment === 'yes') {
                            $("#webcast_form2 #payment_yes").prop('checked', true);
                            $('#webcast_form2 #payment_div').removeClass('display-hide');
                            $('#webcast_form2 #cheque_no').val(jsonvalue.form_details.cheque_no);
                            $('#webcast_form2 #cheque_amount').val(jsonvalue.form_details.cheque_amount);
                            $('#webcast_form2 #cheque_date1').val(jsonvalue.form_details.cheque_date);
                            $('#webcast_form2 #bank_name').val(jsonvalue.form_details.bank_name);
                        } else {
                            $("#webcast_form2 #payment_no").prop('checked', true);
                            $('#webcast_form2 #payment_div').addClass('display-hide');
                        }

                        $('#webcast_form2 #remarks').val(jsonvalue.form_details.remarks);

                        $('.edit').removeClass('display-hide');
                        $('#webcast_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                        $("#webcast_form2 :button[type='button']").removeAttr('disabled');
                        $("#webcast_form2 #tnc").removeAttr('disabled');
                        $('#large').modal({backdrop: 'static', keyboard: false});
                    }
                },
                error: function () {

                }
            });
        }

    });

    $('#webcast_form2 .edit').click(function () {
        var employment = $('#webcast_preview_tab #user_employment').val();
        var min = $('#webcast_preview_tab #min').val();
        var dept = $('#webcast_preview_tab #dept').val();
        var statecode = $('#webcast_preview_tab #stateCode').val();
        var Smi = $('#webcast_preview_tab #Smi').val();
        var Org = $('#webcast_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#webcast_preview_tab #min');
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
                var select = $('#webcast_preview_tab #dept');
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
                $('#webcast_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#webcast_preview_tab #stateCode');
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
                $('#webcast_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#webcast_preview_tab #central_div').hide();
            $('#webcast_preview_tab #state_div').hide();
            $('#webcast_preview_tab #other_div').show();
            $('#webcast_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#webcast_preview_tab #Org');
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
                $('#webcast_preview_tab #other_text_div').removeClass("display-hide");
            }


        } else {
            $('#webcast_preview_tab #central_div').hide();
            $('#webcast_preview_tab #state_div').hide();
            $('#webcast_preview_tab #other_div').hide();
        }

        $('#webcast_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#webcast_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#webcast_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'false');
        $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#webcast_preview_tab #forwarding_div #fwd_ofc_email').removeAttr('disabled');
        $(this).addClass('display-hide');
        //$(this).hide();
    });

    $('#webcast_form2 #confirm').click(function () {

        $("#webcast_form2 :disabled").removeAttr('disabled');
        $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#webcast_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // 20th march
        $('#webcast_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#webcast_form2').serializeObject());
        $('#webcast_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "webcast_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.req_for_err !== null && jsonvalue.error.req_for_err !== "" && jsonvalue.error.req_for_err !== undefined)
                {
                    $('#webcast_form2 #req_for_err').html(jsonvalue.error.req_for_err);
                    $('#webcast_form2 #req_for_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #req_for_err').html("");
                }


                if (jsonvalue.returnString === "live") {

                    if (jsonvalue.error.event_coo_name_err !== null && jsonvalue.error.event_coo_name_err !== "" && jsonvalue.error.event_coo_name_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_name_err').html(jsonvalue.error.event_coo_name_err);
                        $('#webcast_form2 #event_coo_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_name_err').html("");
                    }
                    if (jsonvalue.error.event_coo_desig_err !== null && jsonvalue.error.event_coo_desig_err !== "" && jsonvalue.error.event_coo_desig_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_desig_err').html(jsonvalue.error.event_coo_desig_err);
                        $('#webcast_form2 #event_coo_desig_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_desig_err').html("");
                    }
                    if (jsonvalue.error.event_coo_mobile_err !== null && jsonvalue.error.event_coo_mobile_err !== "" && jsonvalue.error.event_coo_mobile_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_mobile_err').html(jsonvalue.error.event_coo_mobile_err);
                        $('#webcast_form2 #event_coo_mobile_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_mobile_err').html("");
                    }
                    if (jsonvalue.error.event_coo_email_err !== null && jsonvalue.error.event_coo_email_err !== "" && jsonvalue.error.event_coo_email_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_email_err').html(jsonvalue.error.event_coo_email_err);
                        $('#webcast_form2 #event_coo_email_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_email_err').html("");
                    }
                    if (jsonvalue.error.event_coo_address_err !== null && jsonvalue.error.event_coo_address_err !== "" && jsonvalue.error.event_coo_address_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_address_err').html(jsonvalue.error.event_coo_address_err);
                        $('#webcast_form2 #event_coo_address_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_address_err').html("");
                    }
                    if (jsonvalue.error.event_name_eng_err !== null && jsonvalue.error.event_name_eng_err !== "" && jsonvalue.error.event_name_eng_err !== undefined)
                    {
                        $('#webcast_form2 #event_name_eng_err').html(jsonvalue.error.event_name_eng_err);
                        $('#webcast_form2 #event_name_eng_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_name_eng_err').html("");
                    }
                    if (jsonvalue.error.event_name_hin_err !== null && jsonvalue.error.event_name_hin_err !== "" && jsonvalue.error.event_name_hin_err !== undefined)
                    {
                        $('#webcast_form2 #event_name_hin_err').html(jsonvalue.error.event_name_hin_err);
                        $('#webcast_form2 #event_name_hin_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_name_hin_err').html("");
                    }
                    if (jsonvalue.error.event_date_err !== null && jsonvalue.error.event_date_err !== "NA" && jsonvalue.error.event_date_err !== undefined)
                    {
                        $('#webcast_form2 #event_date_err').html(jsonvalue.error.event_date_err);
                        $('#webcast_form2 #event_date_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_date_err').html("");
                    }
                    if (jsonvalue.error.event_type_err !== null && jsonvalue.error.event_type_err !== "" && jsonvalue.error.event_type_err !== undefined)
                    {
                        $('#webcast_form2 #event_type_err').html(jsonvalue.error.event_type_err);
                        $('#webcast_form2 #event_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_type_err').html("");
                    }
                    if (jsonvalue.error.telecast_err !== null && jsonvalue.error.telecast_err !== "" && jsonvalue.error.telecast_err !== undefined)
                    {
                        $('#webcast_form2 #telecast_err').html(jsonvalue.error.telecast_err);
                        $('#webcast_form2 #telecast_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #telecast_err').html("");
                    }
                    if (jsonvalue.error.channel_name_err !== null && jsonvalue.error.channel_name_err !== "" && jsonvalue.error.channel_name_err !== undefined)
                    {
                        $('#webcast_form2 #channel_name_err').html(jsonvalue.error.channel_name_err);
                        $('#webcast_form2 #channel_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #channel_name_err').html("");
                    }
                    if (jsonvalue.error.live_feed_err !== null && jsonvalue.error.live_feed_err !== "" && jsonvalue.error.live_feed_err !== undefined)
                    {
                        $('#webcast_form2 #live_feed_err').html(jsonvalue.error.live_feed_err);
                        $('#webcast_form2 #live_feed_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #live_feed_err').html("");
                    }
                    if (jsonvalue.error.vc_id_err !== null && jsonvalue.error.vc_id_err !== "" && jsonvalue.error.vc_id_err !== undefined)
                    {
                        $('#webcast_form2 #vc_id_err').html(jsonvalue.error.vc_id_err);
                        $('#webcast_form2 #vc_id_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #vc_id_err').html("");
                    }
                    if (jsonvalue.error.conf_radio_err !== null && jsonvalue.error.conf_radio_err !== "" && jsonvalue.error.conf_radio_err !== undefined)
                    {
                        $('#webcast_form2 #conf_radio_err').html(jsonvalue.error.conf_radio_err);
                        $('#webcast_form2 #conf_radio_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_radio_err').html("");
                    }
                    if (jsonvalue.error.conf_name_err !== null && jsonvalue.error.conf_name_err !== "" && jsonvalue.error.conf_name_err !== undefined)
                    {
                        $('#webcast_form2 #conf_name_err').html(jsonvalue.error.conf_name_err);
                        $('#webcast_form2 #conf_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_name_err').html("");
                    }
                    if (jsonvalue.error.conf_type_err !== null && jsonvalue.error.conf_type_err !== "" && jsonvalue.error.conf_type_err !== undefined)
                    {
                        $('#webcast_form2 #conf_type_err').html(jsonvalue.error.conf_type_err);
                        $('#webcast_form2 #conf_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_type_err').html("");
                    }
                    if (jsonvalue.error.conf_city_err !== null && jsonvalue.error.conf_city_err !== "" && jsonvalue.error.conf_city_err !== undefined)
                    {
                        $('#webcast_form2 #conf_city_err').html(jsonvalue.error.conf_city_err);
                        $('#webcast_form2 #conf_city_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_city_err').html("");
                    }
                    if (jsonvalue.error.conf_schedule_err !== null && jsonvalue.error.conf_schedule_err !== "" && jsonvalue.error.conf_schedule_err !== undefined)
                    {
                        $('#webcast_form2 #conf_schedule_err').html(jsonvalue.error.conf_schedule_err);
                        $('#webcast_form2 #conf_schedule_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_schedule_err').html("");
                    }
                    if (jsonvalue.error.conf_session_err !== null && jsonvalue.error.conf_session_err !== "" && jsonvalue.error.conf_session_err !== undefined)
                    {
                        $('#webcast_form2 #conf_session_err').html(jsonvalue.error.conf_session_err);
                        $('#webcast_form2 #conf_session_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_session_err').html("");
                    }
                    if (jsonvalue.error.conf_bw_err !== null && jsonvalue.error.conf_bw_err !== "" && jsonvalue.error.conf_bw_err !== undefined)
                    {
                        $('#webcast_form2 #conf_bw_err').html(jsonvalue.error.conf_bw_err);
                        $('#webcast_form2 #conf_bw_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_bw_err').html("");
                    }
                    if (jsonvalue.error.conf_provider_err !== null && jsonvalue.error.conf_provider_err !== "" && jsonvalue.error.conf_provider_err !== undefined)
                    {
                        $('#webcast_form2 #conf_provider_err').html(jsonvalue.error.conf_provider_err);
                        $('#webcast_form2 #conf_provider_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_provider_err').html("");
                    }
                    if (jsonvalue.error.conf_event_hired_err !== null && jsonvalue.error.conf_event_hired_err !== "" && jsonvalue.error.conf_event_hired_err !== undefined)
                    {
                        $('#webcast_form2 #conf_event_hired_err').html(jsonvalue.error.conf_event_hired_err);
                        $('#webcast_form2 #conf_event_hired_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_event_hired_err').html("");
                    }
                    if (jsonvalue.error.conf_flash_err !== null && jsonvalue.error.conf_flash_err !== "" && jsonvalue.error.conf_flash_err !== undefined)
                    {
                        $('#webcast_form2 #conf_flash_err').html(jsonvalue.error.conf_flash_err);
                        $('#webcast_form2 #conf_flash_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_flash_err').html("");
                    }
                    if (jsonvalue.error.conf_video_err !== null && jsonvalue.error.conf_video_err !== "" && jsonvalue.error.conf_video_err !== undefined)
                    {
                        $('#webcast_form2 #conf_video_err').html(jsonvalue.error.conf_video_err);
                        $('#webcast_form2 #conf_video_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_video_err').html("");
                    }
                    if (jsonvalue.error.conf_contact_err !== null && jsonvalue.error.conf_contact_err !== "" && jsonvalue.error.conf_contact_err !== undefined)
                    {
                        $('#webcast_form2 #conf_contact_err').html(jsonvalue.error.conf_contact_err);
                        $('#webcast_form2 #conf_contact_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_contact_err').html("");
                    }
                    if (jsonvalue.error.hall_type_err !== null && jsonvalue.error.hall_type_err !== "" && jsonvalue.error.hall_type_err !== undefined)
                    {
                        $('#webcast_form2 #hall_type_err').html(jsonvalue.error.hall_type_err);
                        $('#webcast_form2 #hall_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #hall_type_err').html("");
                    }
                } else if (jsonvalue.returnString === "demand") {

                    if (jsonvalue.error.event_no_err !== null && jsonvalue.error.event_no_err !== "" && jsonvalue.error.event_no_err !== undefined)
                    {
                        $('#webcast_form2 #event_no_err').html(jsonvalue.error.event_no_err);
                        $('#webcast_form2 #event_no_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_no_err').html("");
                    }
                    if (jsonvalue.error.event_size_err !== null && jsonvalue.error.event_size_err !== "" && jsonvalue.error.event_size_err !== undefined)
                    {
                        $('#webcast_form2 #event_size_err').html(jsonvalue.error.event_size_err);
                        $('#webcast_form2 #event_size_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_size_err').html("");
                    }
                    if (jsonvalue.error.media_format_err !== null && jsonvalue.error.media_format_err !== "" && jsonvalue.error.media_format_err !== undefined)
                    {
                        $('#webcast_form2 #media_format_err').html(jsonvalue.error.media_format_err);
                        $('#webcast_form2 #media_format_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #media_format_err').html("");
                    }
                }

                if (jsonvalue.error.payment_err !== null && jsonvalue.error.payment_err !== "" && jsonvalue.error.payment_err !== undefined)
                {
                    $('#webcast_form2 #payment_err').html(jsonvalue.error.payment_err);
                    $('#webcast_form2 #payment_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #payment_err').html("");
                }
                if (jsonvalue.error.cheque_no_err !== null && jsonvalue.error.cheque_no_err !== "" && jsonvalue.error.cheque_no_err !== undefined)
                {
                    $('#webcast_form2 #cheque_no_err').html(jsonvalue.error.cheque_no_err);
                    $('#webcast_form2 #cheque_no_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #cheque_no_err').html("");
                }
                if (jsonvalue.error.amount_err !== null && jsonvalue.error.amount_err !== "" && jsonvalue.error.amount_err !== undefined)
                {
                    $('#webcast_form2 #amount_err').html(jsonvalue.error.amount_err);
                    $('#webcast_form2 #amount_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #amount_err').html("");
                }
                if (jsonvalue.error.cheque_date_err !== null && jsonvalue.error.cheque_date_err !== "" && jsonvalue.error.cheque_date_err !== undefined)
                {
                    $('#webcast_form2 #cheque_date_err').html(jsonvalue.error.cheque_date_err);
                    $('#webcast_form2 #cheque_date_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #cheque_date_err').html("");
                }
                if (jsonvalue.error.bank_name_err !== null && jsonvalue.error.bank_name_err !== "" && jsonvalue.error.bank_name_err !== undefined)
                {
                    $('#webcast_form2 #bank_name_err').html(jsonvalue.error.bank_name_err);
                    $('#webcast_form2 #bank_name_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #bank_name_err').html("");
                }

                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#webcast_form2 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#webcast_form2 #fwd_ofc_mobile').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_mobile_err').html("");
                }
                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#webcast_form2 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#webcast_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_email_err').html("");
                }

                if (jsonvalue.error.fwd_email_err1 !== null && jsonvalue.error.fwd_email_err1 !== "" && jsonvalue.error.fwd_email_err1 !== undefined)
                {
                    $('#webcast_form2 #fwd_email_err1').html(jsonvalue.error.fwd_email_err1);
                    $('#webcast_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_email_err1').html("");
                }

                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {
                    $('#webcast_form2 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#webcast_form2 #fwd_ofc_name').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#webcast_form2 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#webcast_form2 #fwd_ofc_desig').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#webcast_form2 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#webcast_form2 #fwd_ofc_add').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#webcast_form2 #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                    $('#webcast_form2 #fwd_ofc_tel').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_tel_err').html("");
                }

                if (jsonvalue.error.remarks_err !== null && jsonvalue.error.remarks_err !== "" && jsonvalue.error.remarks_err !== undefined)
                {
                    $('#webcast_form2 #remarks_err').html(jsonvalue.error.remarks_err);
                    $('#webcast_form2 #remarks_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #remarks_err').html("");
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 23rdjan18    

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#webcast_form2 #useremployment_error').focus();
                    $('#webcast_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#webcast_form2 #minerror').focus();
                    $('#webcast_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#webcast_form2 #deperror').focus();
                    $('#webcast_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#webcast_form2 #other_dept').focus();
                    $('#webcast_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#webcast_form2 #smierror').focus();
                    $('#webcast_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#webcast_form2 #state_error').focus();
                    $('#webcast_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#webcast_form2 #org_error').focus();
                    $('#webcast_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #org_error').html("");
                }

                // profile on 20th march

                if (!error_flag) {
                    $('#webcast_form2 #webcast_error').html("There is some error! Please check");
                    $("#webcast_form2 :disabled").removeAttr('disabled');
                    $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#webcast_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'false');
                    $('#webcast_preview_tab #forwarding_div #fwd_ofc_email').removeAttr('disabled');
                    
                } else {
                    // show modal
                    if ($('#webcast_form2 #tnc').is(":checked"))
                    {
                         $('#webcast_form2 #webcast_error').html("");
                        $('#webcast_form2 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        $('#webcast_form_confirm').removeClass("display-hide");
                        $('#webcast_form_confirm #wifi_details2').addClass("display-hide");
                        $('#webcast_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#webcast_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $('#webcast_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'false');
                        $('#webcast_preview_tab #forwarding_div #fwd_ofc_email').removeAttr('disabled');
                        //line commented by MI on 25thjan19
                        $("#webcast_form2 #tnc").removeAttr('disabled');
                    }
                }

            }
        });
    });

    $('#webcast_form_confirm #confirmYes').click(function () {
        $('#webcast_form2').submit();
        $('#stack3').modal('toggle');
    });

    $('#webcast_form2').submit(function (e) {
        e.preventDefault();

        $("#webcast_form2 :disabled").removeAttr('disabled');
        $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#webcast_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // 20th march
        $('#webcast_preview_tab #user_email').removeAttr('disabled'); // 20th march

        var req_for = $('#webcast_preview_tab #req_for_span').html();
        if (req_for === 'live') {
            $('#webcast_form2 #web_demand').find('input, textarea, button, select').attr('disabled', 'true');
            $('#webcast_form2 #web_live').find('input, textarea, button, select').removeAttr('disabled');
        } else if (req_for === 'demand') {
            $('#webcast_form2 #web_live').find('input, textarea, button, select').attr('disabled', 'true');
            $('#webcast_form2 #web_demand').find('input, textarea, button, select').removeAttr('disabled');
        }

        var data = JSON.stringify($('#webcast_form2').serializeObject());
        $('#webcast_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#webcast_form2 .edit').removeClass('display-hide');
        console.log('data: ' + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18    

        $.ajax({
            type: "POST",
            url: "webcast_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data) {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.req_for_err !== null && jsonvalue.error.req_for_err !== "" && jsonvalue.error.req_for_err !== undefined)
                {
                    $('#webcast_form2 #req_for_err').html(jsonvalue.error.req_for_err);
                    $('#webcast_form2 #req_for_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #req_for_err').html("");
                }

                if (jsonvalue.returnString === "live") {

                    if (jsonvalue.error.event_coo_name_err !== null && jsonvalue.error.event_coo_name_err !== "" && jsonvalue.error.event_coo_name_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_name_err').html(jsonvalue.error.event_coo_name_err);
                        $('#webcast_form2 #event_coo_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_name_err').html("");
                    }
                    if (jsonvalue.error.event_coo_desig_err !== null && jsonvalue.error.event_coo_desig_err !== "" && jsonvalue.error.event_coo_desig_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_desig_err').html(jsonvalue.error.event_coo_desig_err);
                        $('#webcast_form2 #event_coo_desig_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_desig_err').html("");
                    }
                    if (jsonvalue.error.event_coo_mobile_err !== null && jsonvalue.error.event_coo_mobile_err !== "" && jsonvalue.error.event_coo_mobile_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_mobile_err').html(jsonvalue.error.event_coo_mobile_err);
                        $('#webcast_form2 #event_coo_mobile_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_mobile_err').html("");
                    }
                    if (jsonvalue.error.event_coo_email_err !== null && jsonvalue.error.event_coo_email_err !== "" && jsonvalue.error.event_coo_email_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_email_err').html(jsonvalue.error.event_coo_email_err);
                        $('#webcast_form2 #event_coo_email_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_email_err').html("");
                    }
                    if (jsonvalue.error.event_coo_address_err !== null && jsonvalue.error.event_coo_address_err !== "" && jsonvalue.error.event_coo_address_err !== undefined)
                    {
                        $('#webcast_form2 #event_coo_address_err').html(jsonvalue.error.event_coo_address_err);
                        $('#webcast_form2 #event_coo_address_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_coo_address_err').html("");
                    }
                    if (jsonvalue.error.event_name_eng_err !== null && jsonvalue.error.event_name_eng_err !== "" && jsonvalue.error.event_name_eng_err !== undefined)
                    {
                        $('#webcast_form2 #event_name_eng_err').html(jsonvalue.error.event_name_eng_err);
                        $('#webcast_form2 #event_name_eng_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_name_eng_err').html("");
                    }
                    if (jsonvalue.error.event_name_hin_err !== null && jsonvalue.error.event_name_hin_err !== "" && jsonvalue.error.event_name_hin_err !== undefined)
                    {
                        $('#webcast_form2 #event_name_hin_err').html(jsonvalue.error.event_name_hin_err);
                        $('#webcast_form2 #event_name_hin_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_name_hin_err').html("");
                    }
                    if (jsonvalue.error.event_date_err !== null && jsonvalue.error.event_date_err !== "NA" && jsonvalue.error.event_date_err !== undefined)
                    {
                        $('#webcast_form2 #event_date_err').html(jsonvalue.error.event_date_err);
                        $('#webcast_form2 #event_date_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_date_err').html("");
                    }
                    if (jsonvalue.error.event_type_err !== null && jsonvalue.error.event_type_err !== "" && jsonvalue.error.event_type_err !== undefined)
                    {
                        $('#webcast_form2 #event_type_err').html(jsonvalue.error.event_type_err);
                        $('#webcast_form2 #event_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_type_err').html("");
                    }
                    if (jsonvalue.error.telecast_err !== null && jsonvalue.error.telecast_err !== "" && jsonvalue.error.telecast_err !== undefined)
                    {
                        $('#webcast_form2 #telecast_err').html(jsonvalue.error.telecast_err);
                        $('#webcast_form2 #telecast_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #telecast_err').html("");
                    }
                    if (jsonvalue.error.channel_name_err !== null && jsonvalue.error.channel_name_err !== "" && jsonvalue.error.channel_name_err !== undefined)
                    {
                        $('#webcast_form2 #channel_name_err').html(jsonvalue.error.channel_name_err);
                        $('#webcast_form2 #channel_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #channel_name_err').html("");
                    }
                    if (jsonvalue.error.live_feed_err !== null && jsonvalue.error.live_feed_err !== "" && jsonvalue.error.live_feed_err !== undefined)
                    {
                        $('#webcast_form2 #live_feed_err').html(jsonvalue.error.live_feed_err);
                        $('#webcast_form2 #live_feed_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #live_feed_err').html("");
                    }
                    if (jsonvalue.error.vc_id_err !== null && jsonvalue.error.vc_id_err !== "" && jsonvalue.error.vc_id_err !== undefined)
                    {
                        $('#webcast_form2 #vc_id_err').html(jsonvalue.error.vc_id_err);
                        $('#webcast_form2 #vc_id_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #vc_id_err').html("");
                    }

                    if (jsonvalue.error.conf_radio_err !== null && jsonvalue.error.conf_radio_err !== "" && jsonvalue.error.conf_radio_err !== undefined)
                    {
                        $('#webcast_form2 #conf_radio_err').html(jsonvalue.error.conf_radio_err);
                        $('#webcast_form2 #conf_radio_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_radio_err').html("");
                    }

                    if (jsonvalue.error.conf_name_err !== null && jsonvalue.error.conf_name_err !== "" && jsonvalue.error.conf_name_err !== undefined)
                    {
                        $('#webcast_form2 #conf_name_err').html(jsonvalue.error.conf_name_err);
                        $('#webcast_form2 #conf_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_name_err').html("");
                    }
                    if (jsonvalue.error.conf_type_err !== null && jsonvalue.error.conf_type_err !== "" && jsonvalue.error.conf_type_err !== undefined)
                    {
                        $('#webcast_form2 #conf_type_err').html(jsonvalue.error.conf_type_err);
                        $('#webcast_form2 #conf_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_type_err').html("");
                    }
                    if (jsonvalue.error.conf_city_err !== null && jsonvalue.error.conf_city_err !== "" && jsonvalue.error.conf_city_err !== undefined)
                    {
                        $('#webcast_form2 #conf_city_err').html(jsonvalue.error.conf_city_err);
                        $('#webcast_form2 #conf_city_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_city_err').html("");
                    }
                    if (jsonvalue.error.conf_schedule_err !== null && jsonvalue.error.conf_schedule_err !== "" && jsonvalue.error.conf_schedule_err !== undefined)
                    {
                        $('#webcast_form2 #conf_schedule_err').html(jsonvalue.error.conf_schedule_err);
                        $('#webcast_form2 #conf_schedule_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_schedule_err').html("");
                    }
                    if (jsonvalue.error.conf_session_err !== null && jsonvalue.error.conf_session_err !== "" && jsonvalue.error.conf_session_err !== undefined)
                    {
                        $('#webcast_form2 #conf_session_err').html(jsonvalue.error.conf_session_err);
                        $('#webcast_form2 #conf_session_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_session_err').html("");
                    }
                    if (jsonvalue.error.conf_bw_err !== null && jsonvalue.error.conf_bw_err !== "" && jsonvalue.error.conf_bw_err !== undefined)
                    {
                        $('#webcast_form2 #conf_bw_err').html(jsonvalue.error.conf_bw_err);
                        $('#webcast_form2 #conf_bw_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_bw_err').html("");
                    }
                    if (jsonvalue.error.conf_event_hired_err !== null && jsonvalue.error.conf_event_hired_err !== "" && jsonvalue.error.conf_event_hired_err !== undefined)
                    {
                        $('#webcast_form2 #conf_event_hired_err').html(jsonvalue.error.conf_event_hired_err);
                        $('#webcast_form2 #conf_event_hired_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_event_hired_err').html("");
                    }
                    if (jsonvalue.error.conf_flash_err !== null && jsonvalue.error.conf_flash_err !== "" && jsonvalue.error.conf_flash_err !== undefined)
                    {
                        $('#webcast_form2 #conf_flash_err').html(jsonvalue.error.conf_flash_err);
                        $('#webcast_form2 #conf_flash_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_flash_err').html("");
                    }
                    if (jsonvalue.error.conf_video_err !== null && jsonvalue.error.conf_video_err !== "" && jsonvalue.error.conf_video_err !== undefined)
                    {
                        $('#webcast_form2 #conf_video_err').html(jsonvalue.error.conf_video_err);
                        $('#webcast_form2 #conf_video_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_video_err').html("");
                    }
                    if (jsonvalue.error.conf_contact_err !== null && jsonvalue.error.conf_contact_err !== "" && jsonvalue.error.conf_contact_err !== undefined)
                    {
                        $('#webcast_form2 #conf_contact_err').html(jsonvalue.error.conf_contact_err);
                        $('#webcast_form2 #conf_contact_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #conf_contact_err').html("");
                    }
                    if (jsonvalue.error.hall_type_err !== null && jsonvalue.error.hall_type_err !== "" && jsonvalue.error.hall_type_err !== undefined)
                    {
                        $('#webcast_form2 #hall_type_err').html(jsonvalue.error.hall_type_err);
                        $('#webcast_form2 #hall_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #hall_type_err').html("");
                    }
                } else if (jsonvalue.returnString === "demand") {

                    if (jsonvalue.error.event_no_err !== null && jsonvalue.error.event_no_err !== "" && jsonvalue.error.event_no_err !== undefined)
                    {
                        $('#webcast_form2 #event_no_err').html(jsonvalue.error.event_no_err);
                        $('#webcast_form2 #event_no_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_no_err').html("");
                    }
                    if (jsonvalue.error.event_size_err !== null && jsonvalue.error.event_size_err !== "" && jsonvalue.error.event_size_err !== undefined)
                    {
                        $('#webcast_form2 #event_size_err').html(jsonvalue.error.event_size_err);
                        $('#webcast_form2 #event_size_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #event_size_err').html("");
                    }
                    if (jsonvalue.error.media_format_err !== null && jsonvalue.error.media_format_err !== "" && jsonvalue.error.media_format_err !== undefined)
                    {
                        $('#webcast_form2 #media_format_err').html(jsonvalue.error.media_format_err);
                        $('#webcast_form2 #media_format_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_form2 #media_format_err').html("");
                    }
                }

                if (jsonvalue.error.payment_err !== null && jsonvalue.error.payment_err !== "" && jsonvalue.error.payment_err !== undefined)
                {
                    $('#webcast_form2 #payment_err').html(jsonvalue.error.payment_err);
                    $('#webcast_form2 #payment_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #payment_err').html("");
                }
                if (jsonvalue.error.cheque_no_err !== null && jsonvalue.error.cheque_no_err !== "" && jsonvalue.error.cheque_no_err !== undefined)
                {
                    $('#webcast_form2 #cheque_no_err').html(jsonvalue.error.cheque_no_err);
                    $('#webcast_form2 #cheque_no_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #cheque_no_err').html("");
                }
                if (jsonvalue.error.amount_err !== null && jsonvalue.error.amount_err !== "" && jsonvalue.error.amount_err !== undefined)
                {
                    $('#webcast_form2 #amount_err').html(jsonvalue.error.amount_err);
                    $('#webcast_form2 #amount_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #amount_err').html("");
                }
                if (jsonvalue.error.cheque_date_err !== null && jsonvalue.error.cheque_date_err !== "" && jsonvalue.error.cheque_date_err !== undefined)
                {
                    $('#webcast_form2 #cheque_date_err').html(jsonvalue.error.cheque_date_err);
                    $('#webcast_form2 #cheque_date_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #cheque_date_err').html("");
                }
                if (jsonvalue.error.bank_name_err !== null && jsonvalue.error.bank_name_err !== "" && jsonvalue.error.bank_name_err !== undefined)
                {
                    $('#webcast_form2 #bank_name_err').html(jsonvalue.error.bank_name_err);
                    $('#webcast_form2 #bank_name_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #bank_name_err').html("");
                }

                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#webcast_form2 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#webcast_form2 #fwd_ofc_mobile').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_mobile_err').html("");
                }
                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#webcast_form2 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#webcast_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_email_err').html("");
                }

                if (jsonvalue.error.fwd_email_err1 !== null && jsonvalue.error.fwd_email_err1 !== "" && jsonvalue.error.fwd_email_err1 !== undefined)
                {
                    $('#webcast_form2 #fwd_email_err1').html(jsonvalue.error.fwd_email_err1);
                    $('#webcast_form2 #fwd_ofc_email').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_email_err1').html("");
                }

                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {
                    $('#webcast_form2 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#webcast_form2 #fwd_ofc_name').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#webcast_form2 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#webcast_form2 #fwd_ofc_desig').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#webcast_form2 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#webcast_form2 #fwd_ofc_add').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#webcast_form2 #fwd_tel_err').html(jsonvalue.error.fwd_tel_err);
                    $('#webcast_form2 #fwd_ofc_tel').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #fwd_tel_err').html("");
                }

                if (jsonvalue.error.remarks_err !== null && jsonvalue.error.remarks_err !== "" && jsonvalue.error.remarks_err !== undefined)
                {
                    $('#webcast_form2 #remarks_err').html(jsonvalue.error.remarks_err);
                    $('#webcast_form2 #remarks_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_form2 #remarks_err').html("");
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 23rdjan18    

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#webcast_form2 #useremployment_error').focus();
                    $('#webcast_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#webcast_form2 #minerror').focus();
                    $('#webcast_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#webcast_form2 #deperror').focus();
                    $('#webcast_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#webcast_form2 #other_dept').focus();
                    $('#webcast_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#webcast_form2 #smierror').focus();
                    $('#webcast_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#webcast_form2 #state_error').focus();
                    $('#webcast_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#webcast_form2 #org_error').focus();
                    $('#webcast_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#webcast_form2 #org_error').html("");
                }

                // end, code added by pr on 22ndjan18

                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'webcast'},
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

            }
        });
    });

    $(document).on('click', '#webcast_preview .edit', function () {
        //$('#webcast_preview .edit').click(function () {        
        var employment = $('#webcast_preview_tab #user_employment').val();
        var min = $('#webcast_preview_tab #min').val();
        var dept = $('#webcast_preview_tab #dept').val();
        var statecode = $('#webcast_preview_tab #stateCode').val();
        var Smi = $('#webcast_preview_tab #Smi').val();
        var Org = $('#webcast_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#webcast_preview_tab #min');
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
                var select = $('#webcast_preview_tab #dept');
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
                $('#webcast_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#webcast_preview_tab #stateCode');
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
                var select = $('#webcast_preview_tab #Smi');
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
                $('#webcast_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#webcast_preview_tab #central_div').hide();
            $('#webcast_preview_tab #state_div').hide();
            $('#webcast_preview_tab #other_div').show();
            $('#webcast_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#webcast_preview_tab #Org');
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
                $('#webcast_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#webcast_preview_tab #central_div').hide();
            $('#webcast_preview_tab #state_div').hide();
            $('#webcast_preview_tab #other_div').hide();
        }

        $('#webcast_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#webcast_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#webcast_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#webcast_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        //console.log($(this))
        $(this).addClass('display-hide');
//        $(this).hide();
// below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#webcast_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#webcast_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#webcast_preview #confirm', function () {
        // $('#webcast_preview #confirm').click(function () {
        $('#webcast_preview').submit();
    });

    $('#webcast_preview').submit(function (e)
    {
        e.preventDefault();

        $("#webcast_preview :disabled").removeAttr('disabled');
        $('#webcast_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview #forwarding_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#webcast_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#webcast_preview #user_email').removeAttr('disabled');// 20th march  

        var data = JSON.stringify($('#webcast_preview').serializeObject());
        $('#webcast_preview #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "webcast_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 10thjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.req_for_err !== null && jsonvalue.error.req_for_err !== "" && jsonvalue.error.req_for_err !== undefined)
                {
                    $('#webcast_preview #req_for_err').html(jsonvalue.error.req_for_err);
                    $('#webcast_preview #req_for_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #req_for_err').html("");
                }


                if (jsonvalue.returnString === "live") {

                    if (jsonvalue.error.event_coo_name_err !== null && jsonvalue.error.event_coo_name_err !== "" && jsonvalue.error.event_coo_name_err !== undefined)
                    {
                        $('#webcast_preview #event_coo_name_err').html(jsonvalue.error.event_coo_name_err);
                        $('#webcast_preview #event_coo_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_coo_name_err').html("");
                    }
                    if (jsonvalue.error.event_coo_desig_err !== null && jsonvalue.error.event_coo_desig_err !== "" && jsonvalue.error.event_coo_desig_err !== undefined)
                    {
                        $('#webcast_preview #event_coo_desig_err').html(jsonvalue.error.event_coo_desig_err);
                        $('#webcast_preview #event_coo_desig_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_coo_desig_err').html("");
                    }
                    if (jsonvalue.error.event_coo_mobile_err !== null && jsonvalue.error.event_coo_mobile_err !== "" && jsonvalue.error.event_coo_mobile_err !== undefined)
                    {
                        $('#webcast_preview #event_coo_mobile_err').html(jsonvalue.error.event_coo_mobile_err);
                        $('#webcast_preview #event_coo_mobile_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_coo_mobile_err').html("");
                    }
                    if (jsonvalue.error.event_coo_email_err !== null && jsonvalue.error.event_coo_email_err !== "" && jsonvalue.error.event_coo_email_err !== undefined)
                    {
                        $('#webcast_preview #event_coo_email_err').html(jsonvalue.error.event_coo_email_err);
                        $('#webcast_preview #event_coo_email_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_coo_email_err').html("");
                    }
                    if (jsonvalue.error.event_coo_address_err !== null && jsonvalue.error.event_coo_address_err !== "" && jsonvalue.error.event_coo_address_err !== undefined)
                    {
                        $('#webcast_preview #event_coo_address_err').html(jsonvalue.error.event_coo_address_err);
                        $('#webcast_preview #event_coo_address_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_coo_address_err').html("");
                    }
                    if (jsonvalue.error.event_name_eng_err !== null && jsonvalue.error.event_name_eng_err !== "" && jsonvalue.error.event_name_eng_err !== undefined)
                    {
                        $('#webcast_preview #event_name_eng_err').html(jsonvalue.error.event_name_eng_err);
                        $('#webcast_preview #event_name_eng_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_name_eng_err').html("");
                    }
                    if (jsonvalue.error.event_name_hin_err !== null && jsonvalue.error.event_name_hin_err !== "" && jsonvalue.error.event_name_hin_err !== undefined)
                    {
                        $('#webcast_preview #event_name_hin_err').html(jsonvalue.error.event_name_hin_err);
                        $('#webcast_preview #event_name_hin_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_name_hin_err').html("");
                    }
                    if (jsonvalue.error.event_date_err !== null && jsonvalue.error.event_date_err !== "NA" && jsonvalue.error.event_date_err !== undefined)
                    {
                        $('#webcast_preview #event_date_err').html(jsonvalue.error.event_date_err);
                        $('#webcast_preview #event_date_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_date_err').html("");
                    }
                    if (jsonvalue.error.event_type_err !== null && jsonvalue.error.event_type_err !== "" && jsonvalue.error.event_type_err !== undefined)
                    {
                        $('#webcast_preview #event_type_err').html(jsonvalue.error.event_type_err);
                        $('#webcast_preview #event_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_type_err').html("");
                    }
                    if (jsonvalue.error.telecast_err !== null && jsonvalue.error.telecast_err !== "" && jsonvalue.error.telecast_err !== undefined)
                    {
                        $('#webcast_preview #telecast_err').html(jsonvalue.error.telecast_err);
                        $('#webcast_preview #telecast_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #telecast_err').html("");
                    }
                    if (jsonvalue.error.channel_name_err !== null && jsonvalue.error.channel_name_err !== "" && jsonvalue.error.channel_name_err !== undefined)
                    {
                        $('#webcast_preview #channel_name_err').html(jsonvalue.error.channel_name_err);
                        $('#webcast_preview #channel_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #channel_name_err').html("");
                    }
                    if (jsonvalue.error.live_feed_err !== null && jsonvalue.error.live_feed_err !== "" && jsonvalue.error.live_feed_err !== undefined)
                    {
                        $('#webcast_preview #live_feed_err').html(jsonvalue.error.live_feed_err);
                        $('#webcast_preview #live_feed_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #live_feed_err').html("");
                    }
                    if (jsonvalue.error.vc_id_err !== null && jsonvalue.error.vc_id_err !== "" && jsonvalue.error.vc_id_err !== undefined)
                    {
                        $('#webcast_preview #vc_id_err').html(jsonvalue.error.vc_id_err);
                        $('#webcast_preview #vc_id_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #vc_id_err').html("");
                    }
                    if (jsonvalue.error.conf_radio_err !== null && jsonvalue.error.conf_radio_err !== "" && jsonvalue.error.conf_radio_err !== undefined)
                    {
                        $('#webcast_preview #conf_radio_err').html(jsonvalue.error.conf_radio_err);
                        $('#webcast_preview #conf_radio_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_radio_err').html("");
                    }
                    if (jsonvalue.error.conf_name_err !== null && jsonvalue.error.conf_name_err !== "" && jsonvalue.error.conf_name_err !== undefined)
                    {
                        $('#webcast_preview #conf_name_err').html(jsonvalue.error.conf_name_err);
                        $('#webcast_preview #conf_name_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_name_err').html("");
                    }
                    if (jsonvalue.error.conf_type_err !== null && jsonvalue.error.conf_type_err !== "" && jsonvalue.error.conf_type_err !== undefined)
                    {
                        $('#webcast_preview #conf_type_err').html(jsonvalue.error.conf_type_err);
                        $('#webcast_preview #conf_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_type_err').html("");
                    }
                    if (jsonvalue.error.conf_city_err !== null && jsonvalue.error.conf_city_err !== "" && jsonvalue.error.conf_city_err !== undefined)
                    {
                        $('#webcast_preview #conf_city_err').html(jsonvalue.error.conf_city_err);
                        $('#webcast_preview #conf_city_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_city_err').html("");
                    }
                    if (jsonvalue.error.conf_schedule_err !== null && jsonvalue.error.conf_schedule_err !== "" && jsonvalue.error.conf_schedule_err !== undefined)
                    {
                        $('#webcast_preview #conf_schedule_err').html(jsonvalue.error.conf_schedule_err);
                        $('#webcast_preview #conf_schedule_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_schedule_err').html("");
                    }
                    if (jsonvalue.error.conf_session_err !== null && jsonvalue.error.conf_session_err !== "" && jsonvalue.error.conf_session_err !== undefined)
                    {
                        $('#webcast_preview #conf_session_err').html(jsonvalue.error.conf_session_err);
                        $('#webcast_preview #conf_session_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_session_err').html("");
                    }
                    if (jsonvalue.error.conf_bw_err !== null && jsonvalue.error.conf_bw_err !== "" && jsonvalue.error.conf_bw_err !== undefined)
                    {
                        $('#webcast_preview #conf_bw_err').html(jsonvalue.error.conf_bw_err);
                        $('#webcast_preview #conf_bw_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_bw_err').html("");
                    }
                    if (jsonvalue.error.conf_event_hired_err !== null && jsonvalue.error.conf_event_hired_err !== "" && jsonvalue.error.conf_event_hired_err !== undefined)
                    {
                        $('#webcast_preview #conf_event_hired_err').html(jsonvalue.error.conf_event_hired_err);
                        $('#webcast_preview #conf_event_hired_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_event_hired_err').html("");
                    }
                    if (jsonvalue.error.conf_flash_err !== null && jsonvalue.error.conf_flash_err !== "" && jsonvalue.error.conf_flash_err !== undefined)
                    {
                        $('#webcast_preview #conf_flash_err').html(jsonvalue.error.conf_flash_err);
                        $('#webcast_preview #conf_flash_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_flash_err').html("");
                    }
                    if (jsonvalue.error.conf_video_err !== null && jsonvalue.error.conf_video_err !== "" && jsonvalue.error.conf_video_err !== undefined)
                    {
                        $('#webcast_preview #conf_video_err').html(jsonvalue.error.conf_video_err);
                        $('#webcast_preview #conf_video_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_video_err').html("");
                    }
                    if (jsonvalue.error.conf_contact_err !== null && jsonvalue.error.conf_contact_err !== "" && jsonvalue.error.conf_contact_err !== undefined)
                    {
                        $('#webcast_preview #conf_contact_err').html(jsonvalue.error.conf_contact_err);
                        $('#webcast_preview #conf_contact_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #conf_contact_err').html("");
                    }
                    if (jsonvalue.error.hall_type_err !== null && jsonvalue.error.hall_type_err !== "" && jsonvalue.error.hall_type_err !== undefined)
                    {
                        $('#webcast_preview #hall_type_err').html(jsonvalue.error.hall_type_err);
                        $('#webcast_preview #hall_type_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #hall_type_err').html("");
                    }
                } else if (jsonvalue.returnString === "demand") {

                    if (jsonvalue.error.event_no_err !== null && jsonvalue.error.event_no_err !== "" && jsonvalue.error.event_no_err !== undefined)
                    {
                        $('#webcast_preview #event_no_err').html(jsonvalue.error.event_no_err);
                        $('#webcast_preview #event_no_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_no_err').html("");
                    }
                    if (jsonvalue.error.event_size_err !== null && jsonvalue.error.event_size_err !== "" && jsonvalue.error.event_size_err !== undefined)
                    {
                        $('#webcast_preview #event_size_err').html(jsonvalue.error.event_size_err);
                        $('#webcast_preview #event_size_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #event_size_err').html("");
                    }
                    if (jsonvalue.error.media_format_err !== null && jsonvalue.error.media_format_err !== "" && jsonvalue.error.media_format_err !== undefined)
                    {
                        $('#webcast_preview #media_format_err').html(jsonvalue.error.media_format_err);
                        $('#webcast_preview #media_format_err').focus();
                        error_flag = false;
                    } else {
                        $('#webcast_preview #media_format_err').html("");
                    }

                }

                if (jsonvalue.error.payment_err !== null && jsonvalue.error.payment_err !== "" && jsonvalue.error.payment_err !== undefined)
                {
                    $('#webcast_preview #payment_err').html(jsonvalue.error.payment_err);
                    $('#webcast_preview #payment_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #payment_err').html("");
                }
                if (jsonvalue.error.cheque_no_err !== null && jsonvalue.error.cheque_no_err !== "" && jsonvalue.error.cheque_no_err !== undefined)
                {
                    $('#webcast_preview #cheque_no_err').html(jsonvalue.error.cheque_no_err);
                    $('#webcast_preview #cheque_no_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #cheque_no_err').html("");
                }
                if (jsonvalue.error.amount_err !== null && jsonvalue.error.amount_err !== "" && jsonvalue.error.amount_err !== undefined)
                {
                    $('#webcast_preview #amount_err').html(jsonvalue.error.amount_err);
                    $('#webcast_preview #amount_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #amount_err').html("");
                }
                if (jsonvalue.error.cheque_date_err !== null && jsonvalue.error.cheque_date_err !== "" && jsonvalue.error.cheque_date_err !== undefined)
                {
                    $('#webcast_preview #cheque_date_err').html(jsonvalue.error.cheque_date_err);
                    $('#webcast_preview #cheque_date_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #cheque_date_err').html("");
                }
                if (jsonvalue.error.bank_name_err !== null && jsonvalue.error.bank_name_err !== "" && jsonvalue.error.bank_name_err !== undefined)
                {
                    $('#webcast_preview #bank_name_err').html(jsonvalue.error.bank_name_err);
                    $('#webcast_preview #bank_name_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #bank_name_err').html("");
                }

                if (jsonvalue.error.remarks_err !== null && jsonvalue.error.remarks_err !== "" && jsonvalue.error.remarks_err !== undefined)
                {
                    $('#webcast_preview #remarks_err').html(jsonvalue.error.remarks_err);
                    $('#webcast_preview #remarks_err').focus();
                    error_flag = false;
                } else {
                    $('#webcast_preview #remarks_err').html("");
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 23rdjan18    

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#webcast_preview #useremployment_error').focus();
                    $('#webcast_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#webcast_preview #minerror').focus();
                    $('#webcast_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#webcast_preview #deperror').focus();
                    $('#webcast_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#webcast_preview #other_dept').focus();
                    $('#webcast_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#webcast_preview #smierror').focus();
                    $('#webcast_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#webcast_preview #state_error').focus();
                    $('#webcast_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#webcast_preview #org_error').focus();
                    $('#webcast_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#webcast_preview #org_error').html("");
                }

                // profile on 20th march

                if (!error_flag)
                {
                    $("#webcast_preview :disabled").removeAttr('disabled');
                    $('#webcast_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#webcast_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                    $('#webcast_preview #forwarding_div').find('input, textarea, button, select').prop('disabled', 'true');
                } else {

                    $('#webcast_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });

});

