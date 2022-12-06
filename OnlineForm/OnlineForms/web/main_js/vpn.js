
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    $(document).on("change", "#vpn_server_loc", function () {
        var field_id = $(this).attr('id');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        var vpn_server_loc = $(this).val();
        console.log("server loc vpn::::::::::::::::::" + vpn_server_loc)
        if (vpn_server_loc === 'Other')
        {
            $(this).closest("div.detailContainer_div_select").find('#server_other').show();
            $(this).closest("div.detailContainer_div_select").find('#server_other').removeClass('display-hide');
            $(this).closest("div.detailContainer_div_select").find('.col-md-12').addClass('col-md-6').removeClass('col-md-12');
            $(this).closest("div.detailContainer_div_select").find('#server_loc_txt').val('');
        } else
        {
            $(this).closest("div.detailContainer_div_select").find('#server_other').hide();
            $(this).closest("div.detailContainer_div_select").find('#server_other').addClass('display-hide');
            $(this).closest("div.detailContainer_div_select").find('.col-md-6').addClass('col-md-12').removeClass('col-md-6');
            $(this).closest("div.detailContainer_div_select").find('#server_loc_txt').val('');
        }
    });

    //$(document).on('.ip_type','click',function () {
    $(document).on("change", ".vpn_ip_type", function () {
        //$('input[name="ip_type"]').click(function () {
        //$('.page-container-bg-solid').find('input').click(function () {
        console.log('dont be a kid');
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');
        //if (field_name === 'vpn_ip_type[]') {
        var ip_type = $(this).val();
        if (ip_type === 'range') {

            console.log('kid----' + field_form);
            //var kid = $(this).closest('#detailContainer').attr('id');console.log('this is lit----'+kid);
            //var data = $(this).closest(".ip_div input#new_ip1").hide()

            //$(this).closest('.ip_div').next().find('input:text').show();
            $(this).closest("div.detailContainer_div_radio").find('#ip_div').addClass("display-hide");
            $(this).closest("div.detailContainer_div_radio").find('#ip_range_div').removeClass("display-hide");
            $(this).closest("div.detailContainer_div_radio").find('.col-md-4').addClass("col-md-3").removeClass("col-md-4");
            $(this).closest("div.detailContainer_div_radio").find('#new_ip1').val('');

            //$(this).closest("#detailContainer").find(".ip_div").hide();
            //$('#' + field_form + ' #ip_div').addClass('display-hide');
//                $('#' + field_form + ' #ip_div').addClass('display-hide');
//                $('#' + field_form + ' #ip_range_div').removeClass('display-hide');
//                $('#' + field_form + ' .col-md-4').addClass('col-md-3').removeClass('col-md-4');
//                $('#' + field_form + ' #new_ip1').val('');

        } else {
            $(this).closest("div.detailContainer_div_radio").find("#ip_div").removeClass("display-hide");
            $(this).closest("div.detailContainer_div_radio").find("#ip_range_div").addClass("display-hide");
            $(this).closest("div.detailContainer_div_radio").find(".col-md-3").addClass("col-md-4").removeClass("col-md-3");
            $(this).closest("div.detailContainer_div_radio").find("#new_ip1").val();
            $(this).closest("div.detailContainer_div_radio").find("#new_ip2").val();

//                $('#' + field_form + ' #ip_div').removeClass('display-hide');
//                $('#' + field_form + ' #ip_range_div').addClass('display-hide');
//                $('#' + field_form + ' .col-md-3').addClass('col-md-4').removeClass('col-md-3');
//                $('#' + field_form + ' #new_ip1').val('');
//                $('#' + field_form + ' #new_ip2').val('');
        }


//        if (field_name === 'fill_profile') {
//            var isChecked = $(this).is(":checked");
//            if (isChecked) {
//                $.ajax({
//                    type: "POST",
//                    url: "fill_profile",
//                    success: function (data) {
//                        var myJSON = JSON.stringify(data);
//                        var jsonvalue = JSON.parse(myJSON);
//
//                        $('#' + field_form + ' #t_off_name').val(jsonvalue.profile_values.cn);
//                        $('#' + field_form + ' #tdesignation').val(jsonvalue.profile_values.desig);
//                        $('#' + field_form + ' #tmobile').val(jsonvalue.profile_values.mobile);
//                        $('#' + field_form + ' #tauth_email').val(jsonvalue.profile_values.email);
//                        $('#' + field_form + ' #taddrs').val(jsonvalue.profile_values.postalAddress);
//                    },
//                    error: function () {
//                        console.log('error');
//                    }
//                });
//            } else {
//                $('#vpn_form2')[0].reset();
//            }
//        }
    });
    $("#server_loc").change(function () {
        var val = $(this).val();
        if (val == "Other") {
            $("#server_other").removeClass('display-hide');
        } else {
            $("#server_other").addClass('display-hide');
        }
    })
    $('#vpn_form1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#vpn_form1').serializeObject());

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        console.log("DATA:: " + data);

        var vip = [];
        var vip1 = [];
        for (var i = 0; i < lol; i++) {
            if ($("input[name='vpn_ip_type[" + i + "]']:checked").val() !== undefined) {
                var v = $("input[name='vpn_ip_type[" + i + "]']:checked").val();
                vip.push(v);
            }
            console.log('kidssssssssssssssssss----------------');

            if ($("input[name='delete_this_entry[" + i + "]']:checked").val() !== undefined) {
                var v1 = $("input[name='delete_this_entry[" + i + "]']:checked").val();
                vip1.push(v1);
            }
        }
        console.log('FORMS VALUE-----------------------------------' + vip + "delete value---------------" + vip1);
        var x = vip.toString();

        var y = vip1.toString()



        console.log('FORMS VALUE-----------------------------------X' + x + "yyyyyyyyy" + y);

        $.ajax({
            type: "POST",
            url: "vpn_tab1",
            data: {data: data, CSRFRandom: CSRFRandom, vpn_ip: x, delete_chk_value: y},
            datatype: JSON,
            success: function (data)
            {
                //alert("hereeeeeeeeee")
                resetCSRFRandom();// line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                let iCount = 0;
                var sString = "";
                $('[id$=_err]').html('');
                console.log("RETURNSTRING: " + jsonvalue.returnString);
                if (jsonvalue.returnString != '') {
                    $('#captchaerror').html(jsonvalue.returnString);
                    error_flag = false;
                } else {
                    $('#captchaerror').html("");
                }

                $('#vpn_form2 #vpn_data_filled').append(content);


                console.log("RETURNSTRING2: " + jsonvalue.returnString2);

                if (jsonvalue.returnString != '') {
                    $('#captchaerror').html(jsonvalue.returnString);
                    error_flag = false;
                } else {
                    $('#captchaerror').html("");
                }

                $('#vpn_form2 #vpn_data_filled').append(content);


                console.log("RETURNSTRING2: " + jsonvalue.returnString2);
                if (jsonvalue.returnString2 !== null)
                {
                    if (jsonvalue.returnString2 != '') {
                        $('#vpn_coo_error').html(jsonvalue.returnString2);
                        error_flag = false;
                    } else {
                        $('#vpn_coo_error').html("");
                    }
                }
                console.log(jsonvalue.returnString3)
                if (jsonvalue.returnString3 !== null)
                {
                    if (jsonvalue.returnString3 != '') {
                        $('#vpn_coo_error1').html(jsonvalue.returnString3);
                        error_flag = false;
                    } else {
                        $('#vpn_coo_error1').html("");
                    }
                }

//
//                if (jsonvalue.returnString2 !== '' && jsonvalue.returnString2 !==null) {
//                    $('#vpn_coo_error').html(jsonvalue.returnString2);
//                    error_flag = false;
//                } else {
//                    $('#vpn_coo_error').html("");
//                }
//
//                if (jsonvalue.returnString1 !== '' && jsonvalue.returnString1 !==null) {
//                    $('#remarks_error').html(jsonvalue.returnString1);
//                    error_flag = false;
//                } else {
//                    $('#remarks_error').html("");
//                }

                $.each(jsonvalue.error, function (key, value) {
                    sString = key.substring(4);
                    $.each(value, function (paramName, paramValue) {

                        console.log("paramName" + paramName + "paramValue")
                        console.log('count---------------------------' + iCount);

                        $('.detailContainer_div_radio').eq(sString).find("#" + paramName).html(paramValue);


                    });
                    iCount++;
                    console.log('lol------------------------------' + key + '------------ this is key-----' + sString);
                    error_flag = false;
                });

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#vpn_form1 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }



                // end, code added by pr on 23rdjan18
                console.log("ERROR FLAG: " + error_flag);

                if (!error_flag) {
                    $('#vpn_form1 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#vpn_form1 #imgtxt').val("");
                } else {
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#central_div').show();
                        $('#state_div').hide();
                        $('#other_div').hide();
                        $('#other_text_div').addClass("display-hide");
                        $.get('centralMinistry', {
                            orgType: jsonvalue.profile_values.user_employment
                        }, function (response) {
                            var select = $('#min');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            $("#min").val(jsonvalue.profile_values.min);
                        });


                        $.get('centralDepartment', {
                            depType: jsonvalue.profile_values.min
                        }, function (response) {
                            var select = $('#dept');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            $('<option>').val("other").text("other").appendTo(select);
                            $("#dept").val(jsonvalue.profile_values.dept);
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);

                            }
                        });

                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#central_div').hide();
                        $('#state_div').show();
                        $('#other_div').hide();
                        $('#other_text_div').addClass("display-hide");
                        $.get('centralMinistry', {
                            orgType: jsonvalue.profile_values.user_employment
                        }, function (response) {
                            var select = $('#stateCode');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            $("#stateCode").val(jsonvalue.profile_values.stateCode);
                        });
                        $.get('centralDepartment', {
                            depType: jsonvalue.profile_values.stateCode
                        }, function (response) {
                            var select = $('#Smi');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            $('<option>').val("other").text("other").appendTo(select);
                            $("#Smi").val(jsonvalue.profile_values.dept);
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);

                            }
                        });
                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#central_div').hide();
                        $('#state_div').hide();
                        $('#other_div').show();
                        $('#other_text_div').addClass("display-hide");
                        $.get('centralMinistry', {
                            orgType: jsonvalue.profile_values.user_employment
                        }, function (response) {
                            var select = $('#Org');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            $('<option>').val("other").text("other").appendTo(select);
                            $("#Org").val(jsonvalue.profile_values.Org);
                            if (jsonvalue.profile_values.Org === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);
                            }
                        });

                    } else {
                        $('#central_div').hide();
                        $('#state_div').hide();
                        $('#other_div').hide();
                    }
                    $('#vpn_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#vpn_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#vpn_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#vpn_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#vpn_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#vpn_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#vpn_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#vpn_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#vpn_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#vpn_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    $('#vpn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    // $('#vpn_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#vpn_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#vpn_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#vpn_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#vpn_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#vpn_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);

                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {

                        $('#vpn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    } else {

                        var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#vpn_form2 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    }

                    //$('#vpn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);


                    $('#vpn_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#vpn_form2 #req_for').val(jsonvalue.form_details.req_for);
                    console.log("jsonvalue.form_details.req_for: " + jsonvalue.form_details.req_for)


                    var content = "<table class='table table-bordered table-hover'>"
                    content += '<th>IP Type</th><th>IP Address</th><th>Application URL</th><th>Destination Port</th><th>Server Location</th><th>Action</th>';
                    $.each(jsonvalue.vpn_data, function (key, value) {
                        console.log("== " + key + ": " + value);
                        content += '<tr>';
                        $.each(value, function (key1, value1) {
                            console.log("++" + key1 + ": " + value1);
                            if (key1 === 'ip1' || key1 === 'ip2' || key1 === 'ip3' || key1 === 'server_loc' || key1 === 'server_loc_txt') {

                            } else {
                                content += '<td>' + value1 + '</td>';
                            }
                        });
                        content += '</tr>';
                    });
                    content += "</table>"

                    console.log('CONTENT: ' + content);
                    $('#vpn_form2 #vpn_data_filled').html('');
                    $('#vpn_form2 #vpn_data_filled').append(content);

                    if (jsonvalue.form_details.vpn_coo != "na")

                    {
                        $('#sel_coo1').removeClass("display-hide");
                        $('#vpn_form2 #vpn_coo').val(jsonvalue.form_details.vpn_coo);
                    } else {

                        $('#sel_coo1').addClass("display-hide");
                        $('#vpn_form2 #vpn_coo').val("");
                    }
                    if (jsonvalue.form_details.remarks != "")
                    {
                        $('#vpn_form2 #remarks').val(jsonvalue.form_details.remarks);
                        $('#vpn_form2 #remarks_div').removeClass("display-hide");
                    } else {
                        $('#vpn_form2 #remarks_div').addClass("display-hide");
                    }

                    $('.edit').addClass('display-hide');
                    $('#vpn_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#vpn_form2 :button[type='button']").removeAttr('disabled');
                    $("#vpn_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }

            },
            error: function () {
                console.log('error');
            }
        });
    });

    $('#vpn_form2').submit(function (e) {
        e.preventDefault();
        $("#vpn_form2 :disabled").removeAttr('disabled');
        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#vpn_preview_tab #user_email').removeAttr('disabled'); // 20th march        
        var data = JSON.stringify($('#vpn_form2').serializeObject());
        $('#vpn_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "vpn_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;
                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 23rdjan18


                if (error_flag) {
                    // show modal
                    $('#large').modal('toggle');
                    if (jsonvalue.form_details.req_for === 'single') {
                        var a = 'vpn_single';
                    } else if (jsonvalue.form_details.req_for === 'bulk') {
                        var a = 'vpn_bulk';
                    } else if (jsonvalue.form_details.req_for === 'change_add') {
                        var a = 'change_add';
                    }
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: a},
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


//    $('#vpn_form2 .edit').click(function () {
//
//        var employment = $('#vpn_form2 #user_employment').val();
//        var min = $('#vpn_form2 #min').val();
//        var dept = $('#vpn_form2 #dept').val();
//        var statecode = $('#vpn_form2 #stateCode').val();
//        var Smi = $('#vpn_form2 #Smi').val();
//        var Org = $('#vpn_form2 #Org').val();
//
//        if (employment === 'Central'|| employment === 'UT') {
//            $.get('centralMinistry', {
//                orgType: employment
//            }, function (response) {
//                var select = $('#vpn_preview_tab #min');
//                select.find('option').remove();
//                // alert(select)
//                $('<option>').val("").text("-SELECT-").appendTo(select);
//                $.each(response, function (index, value) {
//                    if (min == value)
//                    {
//                        $('<option selected="selected">').val(value).text(value).appendTo(select);
//                    } else
//                    {
//                        $('<option>').val(value).text(value).appendTo(select);
//                    }
//                });
//
//            });
//            $.get('centralDepartment', {
//                depType: min
//            }, function (response) {
//                var select = $('#vpn_preview_tab #dept');
//                select.find('option').remove();
//                $('<option>').val("").text("-SELECT-").appendTo(select);
//                $.each(response, function (index, value) {
//
//                    if (dept == value)
//                    {
//                        $('<option selected="selected">').val(value).text(value).appendTo(select);
//                    } else
//                    {
//                        $('<option>').val(value).text(value).appendTo(select);
//                    }
//
//                });
//                if (response.toString().toLowerCase().indexOf("other") == -1)
//                {
//                    if (response.toString().toLowerCase().indexOf("other") == -1 && dept == "other") {
//                        $('<option selected="selected">').val("other").text("other").appendTo(select);
//                    } else {
//                        $('<option>').val("other").text("other").appendTo(select);
//
//                    }
//                }
//
//
//            });
//            if (dept === 'other') {
//                $('#vpn_preview_tab #other_text_div').removeClass("display-hide");
//
//            }
//
//        } else if (employment === 'State') {
//            $.get('centralMinistry', {
//                orgType: employment
//            }, function (response) {
//                var select = $('#vpn_preview_tab #stateCode');
//                select.find('option').remove();
//                $('<option>').val("").text("-SELECT-").appendTo(select);
//                $.each(response, function (index, value) {
//                    if (statecode == value)
//                    {
//                        $('<option selected="selected">').val(value).text(value).appendTo(select);
//                    } else
//                    {
//                        $('<option>').val(value).text(value).appendTo(select);
//                    }
//                });
//            });
//
//            $.get('centralDepartment', {
//                depType: statecode
//            }, function (response) {
//                var select = $('#vpn_preview_tab #Smi');
//                select.find('option').remove();
//                $('<option>').val("").text("-SELECT-").appendTo(select);
//
//                $.each(response, function (index, value) {
//
//                    if (Smi == value)
//                    {
//                        $('<option selected="selected">').val(value).text(value).appendTo(select);
//                    } else
//                    {
//                        $('<option>').val(value).text(value).appendTo(select);
//                    }
//                });
//                if (response.toString().toLowerCase().indexOf("other") == -1)
//                {
//                    if (response.toString().toLowerCase().indexOf("other") == -1 && Smi == "other") {
//                        $('<option selected="selected">').val("other").text("other").appendTo(select);
//                    } else {
//                        $('<option>').val("other").text("other").appendTo(select);
//
//                    }
//                }
//
//            });
//
//            if (Smi === 'other') {
//
//                $('#vpn_preview_tab #other_text_div').removeClass("display-hide");
//
//            }
//
//        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project" ) {
//            $('#vpn_preview_tab #central_div').hide();
//            $('#vpn_preview_tab #state_div').hide();
//            $('#vpn_preview_tab #other_div').show();
//            $('#vpn_preview_tab #other_text_div').addClass("display-hide");
//            $.get('centralMinistry', {
//                orgType: employment
//            }, function (response) {
//                var select = $('#vpn_preview_tab #Org');
//                select.find('option').remove();
//                $('<option>').val("").text("-SELECT-").appendTo(select);
//                $.each(response, function (index, value) {
//                    if (Org == value)
//                    {
//                        $('<option selected="selected">').val(value).text(value).appendTo(select);
//                    } else
//                    {
//                        $('<option>').val(value).text(value).appendTo(select);
//                    }
//                });
//                if (response.toString().toLowerCase().indexOf("other") == -1)
//                {
//                    if (response.toString().toLowerCase().indexOf("other") == -1 && Org == "other") {
//                        $('<option selected="selected">').val("other").text("other").appendTo(select);
//                    } else {
//                        $('<option>').val("other").text("other").appendTo(select);
//
//                    }
//                }
//
//            });
//            if (Org === 'other') {
//                $('#vpn_preview_tab #other_text_div').removeClass("display-hide");
//
//            }
//
//
//        } else {
//            $('#vpn_preview_tab #central_div').hide();
//            $('#vpn_preview_tab #state_div').hide();
//            $('#vpn_preview_tab #other_div').hide();
//        }
//
//
//
//
//        $('#vpn_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
//        $('#vpn_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
//        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//        $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march       
//        $('#vpn_preview_tab #REditPreview #hod_email').removeAttr('disabled');
//        $(this).addClass('display-hide');
//
//
//    });

    $('#vpn_form2 #confirm').click(function (e) {

        e.preventDefault();
        $("#vpn_form2 :disabled").removeAttr('disabled');
        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#vpn_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#vpn_form2').serializeObject());
        $('#vpn_preview_tab #user_email').prop('disabled', 'true'); // 20th march


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "vpn_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (!error_flag) {

                    $("#vpn_form2 :disabled").removeAttr('disabled');
                    $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    //$('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    //$('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march                    

                } else {
                    if ($('#vpn_form2 #tnc').is(":checked"))
                    {


//                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                        {
//                            $('#vpn_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#vpn_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#vpn_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                        } else if (jsonvalue.profile_values.min == "External Affairs")
//                        {
//                            $('#vpn_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#vpn_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#vpn_form_confirm #fill_hod_mobile').html("+919384664224");
//                        }
                    //    else {
                            $('#vpn_form_confirm #fill_hod_name').html(jsonvalue.profile_values.hod_name);
                            $('#vpn_form_confirm #fill_hod_email').html(jsonvalue.profile_values.hod_email);
                            var startMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                            $('#vpn_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                     //   }
                        $('#vpn_form2 #tnc_error').html("");
                        $('#vpn_preview_tab').find('input, textarea, button, select').prop('disabled', 'true')
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                    } else {
                        $('#vpn_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#vpn_form2 #tnc").removeAttr('disabled');
                        $("#vpn_form2 :disabled").removeAttr('disabled');
                        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        // $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }, error: function ()
            {
                $('#tab2').show();
            }
        });
    });

    $('#vpn_form_confirm #confirmYes').click(function () {
        $('#vpn_form2').submit();
        $('#stack3').modal('toggle');
    });

    $('#vpn_renew_confirm #confirmYes').click(function () {
        $('#stack4').modal('toggle');
        $.ajax({
            type: "POST",
            url: "esign",
            data: {formtype: "vpn_renew"},
            datatype: JSON,
            success: function (data)
            {

                window.location.href = 'e_sign';
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $('#vpn_surrender_confirm #confirmYes').click(function () {
        $('#stack5').modal('toggle');
        $.ajax({
            type: "POST",
            url: "esign",
            data: {formtype: "vpn_surrender"},
            datatype: JSON,
            success: function (data)
            {
                window.location.href = 'e_sign';
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

    $('#vpn_delete_confirm #confirmYes').click(function () {
        $('#stack4').modal('toggle');
        $.ajax({
            type: "POST",
            url: "esign",
            data: {formtype: "vpn_delete"},
            datatype: JSON,
            success: function (data)
            {

                window.location.href = 'e_sign';
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });

//    $('#vpn_form2').submit(function (e) {
//
//        e.preventDefault();
//        $("#vpn_form2 :disabled").removeAttr('disabled');
//        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//        $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//        $('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//        $('#vpn_preview_tab #user_email').removeAttr('disabled'); // 20th march        
//        var data = JSON.stringify($('#vpn_form2').serializeObject());
//        $('#vpn_preview_tab #user_email').prop('disabled', 'true'); // 20th march
//
//        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
//
//        $.ajax({
//            type: "POST",
//            url: "vpn_tab2",
//            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
//            datatype: JSON,
//            success: function (data)
//            {
//                resetCSRFRandom();// line added by pr on 23rdjan18
//
//                var myJSON = JSON.stringify(data);
//                var jsonvalue = JSON.parse(myJSON);
//
//                var error_flag = true;
//                if (jsonvalue.error.new_ip1_err !== null && jsonvalue.error.new_ip1_err !== "" && jsonvalue.error.new_ip1_err !== undefined)
//                {
//                    $('#vpn_form2 #new_ip1_err').html(jsonvalue.error.new_ip1_err)
//                    $('#vpn_form2 #new_ip1_err').focus();
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #new_ip1_err').html("");
//                }
//
//                if (jsonvalue.error.new_ip2_err !== null && jsonvalue.error.new_ip2_err !== "" && jsonvalue.error.new_ip2_err !== undefined)
//                {
//                    $('#vpn_form2 #new_ip2_err').html(jsonvalue.error.new_ip2_err);
//                    $('#vpn_form2 #new_ip2_err').focus();
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #new_ip2_err').html("");
//                }
//
//                if (jsonvalue.error.new_ip3_err !== null && jsonvalue.error.new_ip3_err !== "" && jsonvalue.error.new_ip3_err !== undefined)
//                {
//                    $('#vpn_form2 #new_ip3_err').html(jsonvalue.error.new_ip3_err);
//                    $('#vpn_form2 #new_ip3_err').focus();
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #new_ip3_err').html("");
//                }
//
//                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
//                {
//                    $('#vpn_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
//                    $('#vpn_form2 #server_loc').focus();
//                    error_flag = false;
//                } else {
//
//                    $('#vpn_form2 #server_loc_err').html("")
//                }
//
//                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
//                {
//                    $('#vpn_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
//                    $('#vpn_form2 #server_loc_txt').focus();
//                    error_flag = false;
//                } else {
//
//                    $('#vpn_form2 #server_txt_err').html("")
//                }
//
//                if (jsonvalue.error.app_url_err !== null && jsonvalue.error.app_url_err !== "" && jsonvalue.error.app_url_err !== undefined)
//                {
//                    $('#vpn_form2 #app_url_err').html(jsonvalue.error.app_url_err);
//                    $('#vpn_form2 #app_url_err').focus();
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #app_url_err').html("");
//                }
//
//                if (jsonvalue.error.dest_port_err !== null && jsonvalue.error.dest_port_err !== "" && jsonvalue.error.dest_port_err !== undefined)
//                {
//                    $('#vpn_form2 #dest_port_err').html(jsonvalue.error.dest_port_err);
//                    $('#vpn_form2 #dest_port_err').focus();
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #dest_port_err').html("");
//                }
//
//                // profile 20th march
//                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
//                {
//                    $('#vpn_form2 #useremployment_error').focus();
//                    $('#vpn_form2 #useremployment_error').html(jsonvalue.error.employment_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #useremployment_error').html("");
//                }
//                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
//                {
//                    $('#vpn_form2 #minerror').focus();
//                    $('#vpn_form2 #minerror').html(jsonvalue.error.ministry_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #minerror').html("");
//                }
//                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
//                {
//                    $('#vpn_form2 #deperror').focus();
//                    $('#vpn_form2 #deperror').html(jsonvalue.error.dept_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #deperror').html("");
//                }
//                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
//                {
//                    $('#vpn_form2 #other_dept').focus();
//                    $('#vpn_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #other_dep_error').html("");
//                }
//                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
//                {
//                    $('#vpn_form2 #smierror').focus();
//                    $('#vpn_form2 #smierror').html(jsonvalue.error.state_dept_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #smierror').html("");
//                }
//                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
//                {
//                    $('#vpn_form2 #state_error').focus();
//                    $('#vpn_form2 #state_error').html(jsonvalue.error.state_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #state_error').html("");
//                }
//                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
//                {
//                    $('#vpn_form2 #org_error').focus();
//                    $('#vpn_form2 #org_error').html(jsonvalue.error.org_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #org_error').html("");
//                }
//                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
//                {
//                    $('#vpn_form2 #ca_design').focus();
//                    $('#vpn_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #cadesign_error').html("");
//                }
//                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
//                {
//                    $('#vpn_form2 #hod_name').focus();
//                    $('#vpn_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #hodname_error').html("");
//                }
//                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
//                {
//                    $('#vpn_form2 #hod_mobile').focus();
//                    $('#vpn_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #hodmobile_error').html("");
//                }
//                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
//                {
//                    $('#vpn_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #hodemail_error').html("");
//                }
//                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
//                {
//                    $('#vpn_form2 #hod_tel').focus();
//                    $('#vpn_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
//                    error_flag = false;
//                } else {
//                    $('#vpn_form2 #hodtel_error').html("");
//                }
//
//                // profile 20th march
//
//                // start, code added by pr on 23rdjan18
//
//                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
//                {
//                    $('#captchaerror').html(jsonvalue.error.csrf_error);
//                    error_flag = false;
//                }
//
//                // end, code added by pr on 23rdjan18
//
//
//                if (error_flag) {
//                    // show modal
//                    $('#large').modal('toggle');
//                    if (jsonvalue.form_details.req_for === 'single') {
//                        var a = 'vpn_single';
//                    } else if (jsonvalue.form_details.req_for === 'bulk') {
//                        var a = 'vpn_bulk';
//                    } else if (jsonvalue.form_details.req_for === 'change_add') {
//                        var a = 'change_add';
//                    }
//                    $.ajax({
//                        type: "POST",
//                        url: "esign",
//                        data: {formtype: a},
//                        datatype: JSON,
//                        success: function (data)
//                        {
//                            window.location.href = 'e_sign';
//                        }, error: function ()
//                        {
//                            $('#tab1').show();
//                        }
//                    });
//                }
//
//            }, error: function ()
//            {
//                $('#tab1').show();
//            }
//        });
//
//    });

    $(document).on('click', '#vpn_preview .edit', function () {
        //$('#vpn_preview .edit').click(function () {
        var employment = $('#vpn_preview_tab #user_employment').val();
        var min = $('#vpn_preview_tab #min').val();
        var dept = $('#vpn_preview_tab #dept').val();
        var statecode = $('#vpn_preview_tab #stateCode').val();
        var Smi = $('#vpn_preview_tab #Smi').val();
        var Org = $('#vpn_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#vpn_preview_tab #min');
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
                var select = $('#vpn_preview_tab #dept');
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
                $('#vpn_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#vpn_preview_tab #stateCode');
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
                var select = $('#vpn_preview_tab #Smi');
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
                $('#vpn_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#vpn_preview_tab #central_div').hide();
            $('#vpn_preview_tab #state_div').hide();
            $('#vpn_preview_tab #other_div').show();
            $('#vpn_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#vpn_preview_tab #Org');
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
                $('#vpn_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#vpn_preview_tab #central_div').hide();
            $('#vpn_preview_tab #state_div').hide();
            $('#vpn_preview_tab #other_div').hide();
        }




        $('#vpn_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#vpn_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        //console.log($(this))
        $(this).addClass('display-hide');

        if ($("#comingFrom").val("admin"))
        {

            $("#vpn_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18

            $("#vpn_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#vpn_preview #confirm', function () {
        //$('#vpn_preview #confirm').click(function () {
        $('#vpn_preview').submit();
        // $('#vpn_preview_form').modal('toggle');
    });

    $('#vpn_preview').submit(function (e) {

        e.preventDefault();
        $("#vpn_preview :disabled").removeAttr('disabled');
        $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march   
        $('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march // nikki 22 jan 2019   
        $('#vpn_preview_tab #user_email').removeAttr('disabled'); // 20th march  
        var data = JSON.stringify($('#vpn_preview').serializeObject());
        $('#vpn_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#vpn_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 jan 2019
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        $.ajax({
            type: "POST",
            url: "vpn_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.new_ip1_err !== null && jsonvalue.error.new_ip1_err !== "" && jsonvalue.error.new_ip1_err !== undefined)
                {
                    $('#vpn_preview #new_ip1_err').html(jsonvalue.error.new_ip1_err)
                    $('#vpn_preview #new_ip1_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_preview #new_ip1_err').html("");
                }

                if (jsonvalue.error.new_ip2_err !== null && jsonvalue.error.new_ip2_err !== "" && jsonvalue.error.new_ip2_err !== undefined)
                {
                    $('#vpn_preview #new_ip2_err').html(jsonvalue.error.new_ip2_err);
                    $('#vpn_preview #new_ip2_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_preview #new_ip2_err').html("");
                }

                if (jsonvalue.error.new_ip3_err !== null && jsonvalue.error.new_ip3_err !== "" && jsonvalue.error.new_ip3_err !== undefined)
                {
                    $('#vpn_preview #new_ip3_err').html(jsonvalue.error.new_ip3_err);
                    $('#vpn_preview #new_ip3_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_preview #new_ip3_err').html("");
                }

                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#vpn_preview #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#vpn_preview #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#vpn_preview #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#vpn_preview #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#vpn_preview #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#vpn_preview #server_txt_err').html("")
                }

                if (jsonvalue.error.app_url_err !== null && jsonvalue.error.app_url_err !== "" && jsonvalue.error.app_url_err !== undefined)
                {
                    $('#vpn_preview #app_url_err').html(jsonvalue.error.app_url_err);
                    $('#vpn_preview #app_url_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_preview #app_url_err').html("");
                }

                if (jsonvalue.error.dest_port_err !== null && jsonvalue.error.dest_port_err !== "" && jsonvalue.error.dest_port_err !== undefined)
                {
                    $('#vpn_preview #dest_port_err').html(jsonvalue.error.dest_port_err);
                    $('#vpn_preview #dest_port_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_preview #dest_port_err').html("");
                }


                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 23rdjan18


                // profile 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#vpn_preview #useremployment_error').focus();
                    $('#vpn_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#vpn_preview #minerror').focus();
                    $('#vpn_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#vpn_preview #deperror').focus();
                    $('#vpn_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#vpn_preview #other_dept').focus();
                    $('#vpn_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#vpn_preview #smierror').focus();
                    $('#vpn_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#vpn_preview #state_error').focus();
                    $('#vpn_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#vpn_preview #org_error').focus();
                    $('#vpn_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#vpn_preview #ca_design').focus();
                    $('#vpn_preview #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#vpn_preview #hod_name').focus();
                    $('#vpn_preview #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#vpn_preview #hod_mobile').focus();
                    $('#vpn_preview #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#vpn_preview #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#vpn_preview #hod_tel').focus();
                    $('#vpn_preview #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#vpn_preview #hodtel_error').html("");
                }

                // profile 20th march

                if (!error_flag)
                {
                    $("#vpn_preview :disabled").removeAttr('disabled');
                    $('#vpn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#vpn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {
                    $('#vpn_preview_form').modal('toggle');
                }
            }, error: function (request, error)
            {
                console.log('error');
            }
        });

    });

// nitin editions


    $(document).on('click', 'input[name="req_for"]', function () {

        // $("#vpn_change").click(function () {

        var a = $("input[name='req_co']:checked").val();
        console.log("aaaaaaaaaaaaa" + a)
        if (a === undefined)
        {
            console.log("###########")
            $("#req_co_err").html("Please choose coordinator");

        } else {

            $("#req_co_err").html("");
        }

    });



    $("#vpn_change").click(function () {

        var a = $("input[name='req_co']:checked").val();

        if (a === "central_co")
        {
            var vpn_coo = $('#vpn_coo').find(":selected").val();

            if (vpn_coo === "") {
                $("#vpn_coo_error").html("Please select coordinator email address from dropdown");
                $("#vpn_single").prop("checked", true);
            } else {
                $("#vpn_coo_error").html("");
                var CSRFRandom = $("#CSRFRandom").val();

                $.ajax({
                    type: "POST",
                    url: "vpn_exist",
                    data: {CSRFRandom: CSRFRandom},
                    dataType: 'text',
                    async: false,
                    success: function (result)
                    {

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
                            var dropdown = $("#vpn_reg_no");    // drop down 
                            $("#vpn_reg_no")[0].selectedIndex = 0;
                            $("#vpn_reg_no").empty();
                            console.log(api_details['success']);
                            $("#vpn_reg_no").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                            $.each(api_details, function (key1, value1) {
                                if (countTime == icount)
                                    return false;
                                $.each(value1, function (key, value) {

                                    if (key == "vpn_registration_no")
                                    {
                                        console.log(key + ": " + value);
                                        // fill data in drop down
                                        dropdown.append($("<option />").val(value).text(value));
                                    }
                                });
                                icount++;
                            });
                        }
                        var data = api_details.access_details;
                        // console.log(api_details.expdate + vpn_reg_no);
                        if (data !== undefined) {



                        } else {

                        }
                        resetCSRFRandom();

                    }, error: function ()
                    {
                        console.log('error');
                    }

                });

                $("#modal_id").modal({'backdrop': 'static'});
            }
        } else if (a === "state_co")
        {
            var vpn_coo = $('#vpn_state_coo').find(":selected").val();
            if (vpn_coo === "") {
                $("#vpn_coo_error1").html("Please select coordinator email address from dropdown");
                $("#vpn_single").prop("checked", true);
            } else {
                $("#vpn_coo_error1").html("");
                var CSRFRandom = $("#CSRFRandom").val();

                $.ajax({
                    type: "POST",
                    url: "vpn_exist",
                    data: {CSRFRandom: CSRFRandom},
                    dataType: 'text',
                    async: false,
                    success: function (result)
                    {

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
                            var dropdown = $("#vpn_reg_no");    // drop down 
                            $("#vpn_reg_no")[0].selectedIndex = 0;
                            $("#vpn_reg_no").empty();
                            console.log(api_details['success']);
                            $("#vpn_reg_no").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                            $.each(api_details, function (key1, value1) {
                                if (countTime == icount)
                                    return false;
                                $.each(value1, function (key, value) {

                                    if (key == "vpn_registration_no")
                                    {
                                        console.log(key + ": " + value);
                                        // fill data in drop down
                                        dropdown.append($("<option />").val(value).text(value));
                                    }
                                });
                                icount++;
                            });
                        }
                        var data = api_details.access_details;
                        // console.log(api_details.expdate + vpn_reg_no);
                        if (data !== undefined) {



                        } else {

                        }
                        resetCSRFRandom();

                    }, error: function ()
                    {
                        console.log('error');
                    }

                });

                $("#modal_id").modal({'backdrop': 'static'});
            }
        } else {
            $("#vpn_coo_error1").html("");
            var CSRFRandom = $("#CSRFRandom").val();

            $.ajax({
                type: "POST",
                url: "vpn_exist",
                data: {CSRFRandom: CSRFRandom},
                dataType: 'text',
                async: false,
                success: function (result)
                {

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
                        var dropdown = $("#vpn_reg_no");    // drop down 
                        $("#vpn_reg_no")[0].selectedIndex = 0;
                        $("#vpn_reg_no").empty();
                        console.log(api_details['success']);
                        $("#vpn_reg_no").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                        $.each(api_details, function (key1, value1) {
                            if (countTime == icount)
                                return false;
                            $.each(value1, function (key, value) {

                                if (key == "vpn_registration_no")
                                {
                                    console.log(key + ": " + value);
                                    // fill data in drop down
                                    dropdown.append($("<option />").val(value).text(value));
                                }
                            });
                            icount++;
                        });
                    }
                    var data = api_details.access_details;
                    // console.log(api_details.expdate + vpn_reg_no);
                    if (data !== undefined) {



                    } else {

                    }
                    resetCSRFRandom();

                }, error: function ()
                {
                    console.log('error');
                }

            });

            $("#modal_id").modal({'backdrop': 'static'});

        }
    });






//    $("#vpn_change").click(function () {
//        var vpn_coo = $('#vpn_coo').find(":selected").val();
//        if (vpn_coo === "") {
//
//            $("#vpn_coo_error").html("Please select coordinator email address from dropdown");
//            $("#vpn_single").prop("checked", true);
//        } else {
//            $("#vpn_coo_error").html("");
//            $("#vpn_edit_button").html('');
//            // find existed vpn id's
//
//            var CSRFRandom = $("#CSRFRandom").val();
//
//            $.ajax({
//                type: "POST",
//                url: "vpn_exist",
//                data: {CSRFRandom: CSRFRandom},
//                dataType: 'text',
//                async: false,
//                success: function (result)
//                {
//
//                    var jvalue = JSON.parse(result);
//                    var api_details = JSON.parse(jvalue.api_response);
//                    var data = api_details.access_details;
//                    var objLength = Object.keys(api_details).length;
//                    const keys = Object.keys(api_details[0]);
//
//
//
//                    var data = api_details.access_details;
//                    console.log(api_details['success']);
//                    if (api_details['success'] === true) {
//
//
//                        var icount = 0;
//                        var countTime = objLength - 2;
//                        var dropdown = $("#vpn_reg_no");    // drop down 
//                        $("#vpn_reg_no")[0].selectedIndex = 0;
//                        $("#vpn_reg_no").empty();
//                        console.log(api_details['success']);
//                        $.each(api_details, function (key1, value1) {
//                            if (countTime == icount)
//                                return false;
//                            $.each(value1, function (key, value) {
//
//                                if (key == "vpn_registration_no")
//                                {
//                                    console.log(key + ": " + value);
//                                    // fill data in drop down
//                                    dropdown.append($("<option />").val(value).text(value));
//                                }
//                            });
//                            icount++;
//                        });
//                    }
//                    var data = api_details.access_details;
//                    // console.log(api_details.expdate + vpn_reg_no);
//                    if (data !== undefined) {
//
//
//
//                    } else {
//
//                    }
//                    resetCSRFRandom();
//
//                }, error: function ()
//                {
//                    console.log('error');
//                }
//
//            });
//            $("#modal_id").modal({'backdrop': 'static'});
//        }
//    });
    // for seprate renew button 
    $("#vpn_change_renew").click(function () {

        // call existing api 


        /********************************************************************************/





        /**********************************************************************************/
        var a = $("input[name='req_co']:checked").val();

        if (a === "central_co")
        {
            var vpn_coo = $('#vpn_coo').find(":selected").val();
            if (vpn_coo === "") {
                $("#vpn_coo_error").html("Please select coordinator email address from dropdown");
                $("#vpn_single").prop("checked", true);
            } else {
                $("#vpn_coo_error").html("");
                var CSRFRandom = $("#CSRFRandom").val();

                $.ajax({
                    type: "POST",
                    url: "vpn_exist",
                    data: {CSRFRandom: CSRFRandom},
                    dataType: 'text',
                    async: false,
                    success: function (result)
                    {

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
                            var dropdown = $("#vpn_reg_no_renew");    // drop down 
                            $("#vpn_reg_no_renew")[0].selectedIndex = 0;
                            $("#vpn_reg_no_renew").empty();
                            console.log(api_details['success']);
                            $("#vpn_reg_no_renew").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                            $.each(api_details, function (key1, value1) {
                                if (countTime == icount)
                                    return false;
                                $.each(value1, function (key, value) {

                                    if (key == "vpn_registration_no")
                                    {
                                        console.log(key + ": " + value);
                                        // fill data in drop down
                                        dropdown.append($("<option />").val(value).text(value));
                                    }
                                });
                                icount++;
                            });
                        }
                        var data = api_details.access_details;
                        // console.log(api_details.expdate + vpn_reg_no);
                        if (data !== undefined) {



                        } else {

                        }
                        resetCSRFRandom();

                    }, error: function ()
                    {
                        console.log('error');
                    }

                });
                $("#modal_id_renew").modal({'backdrop': 'static'});
            }
        } else if (a === "state_co")
        {
            var vpn_coo = $('#vpn_state_coo').find(":selected").val();
            if (vpn_coo === "") {
                $("#vpn_coo_error1").html("Please select coordinator email address from dropdown");
                $("#vpn_single").prop("checked", true);
            } else {
                $("#vpn_coo_error1").html("");
                var CSRFRandom = $("#CSRFRandom").val();

                $.ajax({
                    type: "POST",
                    url: "vpn_exist",
                    data: {CSRFRandom: CSRFRandom},
                    dataType: 'text',
                    async: false,
                    success: function (result)
                    {

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
                            var dropdown = $("#vpn_reg_no_renew");    // drop down 
                            $("#vpn_reg_no_renew")[0].selectedIndex = 0;
                            $("#vpn_reg_no_renew").empty();
                            console.log(api_details['success']);
                            $("#vpn_reg_no_renew").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                            $.each(api_details, function (key1, value1) {
                                if (countTime == icount)
                                    return false;
                                $.each(value1, function (key, value) {

                                    if (key == "vpn_registration_no")
                                    {
                                        console.log(key + ": " + value);
                                        // fill data in drop down
                                        dropdown.append($("<option />").val(value).text(value));
                                    }
                                });
                                icount++;
                            });
                        }
                        var data = api_details.access_details;
                        // console.log(api_details.expdate + vpn_reg_no);
                        if (data !== undefined) {



                        } else {

                        }
                        resetCSRFRandom();

                    }, error: function ()
                    {
                        console.log('error');
                    }

                });
                $("#modal_id_renew").modal({'backdrop': 'static'});
            }
        } else {
            $("#vpn_coo_error1").html("");
            var CSRFRandom = $("#CSRFRandom").val();

            $.ajax({
                type: "POST",
                url: "vpn_exist",
                data: {CSRFRandom: CSRFRandom},
                dataType: 'text',
                async: false,
                success: function (result)
                {

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
                        var dropdown = $("#vpn_reg_no_renew");    // drop down 
                        $("#vpn_reg_no_renew")[0].selectedIndex = 0;
                        $("#vpn_reg_no_renew").empty();
                        console.log(api_details['success']);
                        $("#vpn_reg_no_renew").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                        $.each(api_details, function (key1, value1) {
                            if (countTime == icount)
                                return false;
                            $.each(value1, function (key, value) {

                                if (key == "vpn_registration_no")
                                {
                                    console.log(key + ": " + value);
                                    // fill data in drop down
                                    dropdown.append($("<option />").val(value).text(value));
                                }
                            });
                            icount++;
                        });
                    }
                    var data = api_details.access_details;
                    // console.log(api_details.expdate + vpn_reg_no);
                    if (data !== undefined) {



                    } else {

                    }
                    resetCSRFRandom();

                }, error: function ()
                {
                    console.log('error');
                }

            });
            $("#modal_id_renew").modal({'backdrop': 'static'});

        }

//        var vpn_coo = $('#vpn_coo').find(":selected").val();
//        if (vpn_coo === "") {
//            $("#vpn_coo_error").html("Please select coordinator email address from dropdown");
//            $("#vpn_single").prop("checked", true);
//        } else {
//            $("#vpn_coo_error").html("");
//
//            $("#modal_id_renew").modal({'backdrop': 'static'});
//        }
    });




// surrender


    $("#vpn_surrender").click(function () {

        // call existing api 

        var a = $("input[name='req_co']:checked").val();

        if (a === "central_co")
        {
            var vpn_coo = $('#vpn_coo').find(":selected").val();
            if (vpn_coo === "") {
                $("#vpn_coo_error").html("Please select coordinator email address from dropdown");
                $("#vpn_single").prop("checked", true);
            } else {
                $("#vpn_coo_error").html("");
                var CSRFRandom = $("#CSRFRandom").val();

                $.ajax({
                    type: "POST",
                    url: "vpn_exist",
                    data: {CSRFRandom: CSRFRandom},
                    dataType: 'text',
                    async: false,
                    success: function (result)
                    {

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
                            var dropdown = $("#vpn_reg_no_surrender");    // drop down 
                            $("#vpn_reg_no_surrender")[0].selectedIndex = 0;
                            $("#vpn_reg_no_surrender").empty();
                            console.log(api_details['success']);
                            $("#vpn_reg_no_surrender").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                            $.each(api_details, function (key1, value1) {
                                if (countTime == icount)
                                    return false;
                                $.each(value1, function (key, value) {

                                    if (key == "vpn_registration_no")
                                    {
                                        console.log(key + ": " + value);
                                        // fill data in drop down
                                        dropdown.append($("<option />").val(value).text(value));
                                    }
                                });
                                icount++;
                            });
                        }
                        var data = api_details.access_details;
                        // console.log(api_details.expdate + vpn_reg_no);
                        if (data !== undefined) {



                        } else {

                        }
                        resetCSRFRandom();

                    }, error: function ()
                    {
                        console.log('error');
                    }

                });
                $("#modal_id_surrender").modal({'backdrop': 'static'});
            }
        } else if (a === "state_co")
        {
            var vpn_coo = $('#vpn_state_coo').find(":selected").val();
            if (vpn_coo === "") {
                $("#vpn_coo_error1").html("Please select coordinator email address from dropdown");
                $("#vpn_single").prop("checked", true);
            } else {
                $("#vpn_coo_error1").html("");
                var CSRFRandom = $("#CSRFRandom").val();

                $.ajax({
                    type: "POST",
                    url: "vpn_exist",
                    data: {CSRFRandom: CSRFRandom},
                    dataType: 'text',
                    async: false,
                    success: function (result)
                    {

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
                            var dropdown = $("#vpn_reg_no_surrender");    // drop down 
                            $("#vpn_reg_no_surrender")[0].selectedIndex = 0;
                            $("#vpn_reg_no_surrender").empty();
                            console.log(api_details['success']);
                            $("#vpn_reg_no_surrender").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                            $.each(api_details, function (key1, value1) {
                                if (countTime == icount)
                                    return false;
                                $.each(value1, function (key, value) {

                                    if (key == "vpn_registration_no")
                                    {
                                        console.log(key + ": " + value);
                                        // fill data in drop down
                                        dropdown.append($("<option />").val(value).text(value));
                                    }
                                });
                                icount++;
                            });
                        }
                        var data = api_details.access_details;
                        // console.log(api_details.expdate + vpn_reg_no);
                        if (data !== undefined) {



                        } else {

                        }
                        resetCSRFRandom();

                    }, error: function ()
                    {
                        console.log('error');
                    }

                });
                $("#modal_id_surrender").modal({'backdrop': 'static'});
            }
        } else {
            $("#vpn_coo_error1").html("");
            var CSRFRandom = $("#CSRFRandom").val();

            $.ajax({
                type: "POST",
                url: "vpn_exist",
                data: {CSRFRandom: CSRFRandom},
                dataType: 'text',
                async: false,
                success: function (result)
                {

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
                        var dropdown = $("#vpn_reg_no_surrender");    // drop down 
                        $("#vpn_reg_no_surrender")[0].selectedIndex = 0;
                        $("#vpn_reg_no_surrender").empty();
                        console.log(api_details['success']);
                        $("#vpn_reg_no_surrender").html('<option value="" selected="true" >-Select VPN REGISTRATION NO-</option>');
                        $.each(api_details, function (key1, value1) {
                            if (countTime == icount)
                                return false;
                            $.each(value1, function (key, value) {

                                if (key == "vpn_registration_no")
                                {
                                    console.log(key + ": " + value);
                                    // fill data in drop down
                                    dropdown.append($("<option />").val(value).text(value));
                                }
                            });
                            icount++;
                        });
                    }
                    var data = api_details.access_details;
                    // console.log(api_details.expdate + vpn_reg_no);
                    if (data !== undefined) {



                    } else {

                    }
                    resetCSRFRandom();

                }, error: function ()
                {
                    console.log('error');
                }

            });
            $("#modal_id_surrender").modal({'backdrop': 'static'});

        }




        /********************************************************************************/





        /**********************************************************************************/
//        var vpn_coo = $('#vpn_coo').find(":selected").val();
//        if (vpn_coo === "") {
//            $("#vpn_coo_error").html("Please select coordinator email address from dropdown");
//            $("#vpn_single").prop("checked", true);
//        } else {
//            $("#vpn_coo_error").html("");
//
//            $("#modal_id_surrender").modal({'backdrop': 'static'});
//        }
    });





    $("#vpn_single").click(function () {

        // check vpn exists
        var CSRFRandom = $("#CSRFRandom").val();
        // black div values 
        $("#vpn_reg_no_single").html('');
        $.ajax({
            type: "POST",
            url: "vpn_exist",
            data: {CSRFRandom: CSRFRandom},
            dataType: 'text',
            async: false,
            success: function (result)
            {

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
                    var dropdown = $("#vpn_reg_no_renew");    // drop down 
                    $("#vpn_reg_no_renew")[0].selectedIndex = 0;
                    $("#vpn_reg_no_renew").empty();
                    console.log(api_details['success']);
                    var str = "";
                    str += "<ul class='vpn-listing'>"
                    $.each(api_details, function (key1, value1) {
                        if (countTime == icount)
                            return false;
                        $.each(value1, function (key, value) {

                            if (key == "vpn_registration_no")
                            {
                                console.log(key + ": " + value);
                                str += "<li><label>" + value + "</label></li>";
                            }
                        });
                        icount++;
                    });
                    str += "</ul>";
                    $("#vpn_reg_no_single").html(str);
                    $("#modal_id_vpn_single").modal({'backdrop': 'static'});

                } else {
                    console.log("NO Exited VPN Found....");
                }

                resetCSRFRandom();

            }, error: function ()
            {
                console.log('error');
            }

        });

        $("#vpn_renew_form_new").html('');
        $('#vpn_ip_add_div').removeClass('display-hide');
    });


    $(document).on('change', '#check_table input:checkbox', function () {

        var currentRow = $(this).closest("tr");
        var col1 = currentRow.find("td:eq(1)").text();
        var col2 = currentRow.find("td:eq(2)").text();
        var col3 = currentRow.find("td:eq(3)").text();
        var col4 = currentRow.find("td:eq(4)").text();
        var data = col1 + "\n" + col2 + "\n" + col3 + "\n" + col4;

        //alert(data);
        $("#vpn_edit").prop("disabled", $('#check_table input:checkbox:checked').length === 0);
        $("#vpn_delete").prop("disabled", $('#check_table input:checkbox:checked').length === 0);
    });

//    // ajax for fatch/ call vpn api to get user details 
//    $('#vpn_fatch_data_submit').click(function (e) {
//alert("hiii");
//        var CSRFRandom = $("#CSRFRandom").val();
//        var data = $("#vpn_reg_no").val();
//        $.ajax({
//            type: "POST",
//            url: "vpn_fatch2",
//            data: {data: data, CSRFRandom: CSRFRandom},
//            dataType: 'text',
//            async: false,
//            success: function (result)
//            {
//                var vpn_reg_no = $("#vpn_reg_no").val();
//                var jvalue = JSON.parse(result);
//                var api_details = JSON.parse(jvalue.api_response);
//
//                var data = api_details.access_details;
//                console.log(api_details.expdate);
//                console.log("----2->" + data);
//                if (data !== undefined) {
//
//
//
//                    console.log(Object.keys(data).length);
//                    // add and create spans                    
//                    var a = "<table id='check_table' class='table table-striped table-hover'><tr><th>Select</th><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";
//
//                    $.each(data, function (key, value) {
//
//                        a += ("<tr><td><input id='checkid' name='deleted_value' value='" + key + "~" + value.serip + "~" + value.serloc + "~" + value.destport + "~" + value.desc_service + "' type='checkbox'/> </td><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");
//
//                    });
//                    a += "</table>" + "<input type='hidden' id='req_for' name='req_for' value='vpn_delete'/> <input type='hidden'value='" + $("#vpn_reg_no").val() + "' name='vpn_reg_no' id='vpn_reg_no' /> <input type='hidden' id='vpn_coo_email' name='vpn_coo' value='" + $('#vpn_coo').find(":selected").val() + "'/><input type='hidden' id='vpn_coo_email' name='vpn_coo1' value='" + $('#vpn_state_coo').find(":selected").val() + "'/>";
//
//                    //$("#vpn_access_details").append(a);
//                    $("#vpn_renew_form").html('');
//                    $("#vpn_renew_form").append(a);
//                    $("#vpn_renew_button").html('');
//                    $("#vpn_edit_button").html('');
//                    // $("#vpn_renew_button").append("<button class='btn btn-warning' id='vpn_renew' > Renew </button>");
//                    $("#vpn_renew_button").append("<button class='btn btn-success ml-2'  id='vpn_addnew' > Add New </button>");
//                    // buttons for edit and delete ------------------------------------------------------------------------------
//                    //$("#vpn_edit_button").append("<button class='btn btn-warning ml-2' id='vpn_edit' disabled> Edit </button>");
//                  //  $("#vpn_edit_button").append("<button class='btn btn-warning ml-2' id='vpn_delete' disabled> Delete </button>");
//                    //-----------------------------------------------------------------------------------------------------------
//
//                    $("#vpn_renew_msg").addClass('d-none');
//                    //$("#vpn_renew_form").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no").val()+"' name='vpn_reqno' id='vpn_reqno' />")
//                    // check for vpn button enable/disable
//                    $.ajax({
//                        type: "POST",
//                        url: "vpn_check_exp",
//                        data: {data: data, vpn_reg_no: vpn_reg_no, CSRFRandom: CSRFRandom},
//                        dataType: 'json',
//                        async: false,
//                        success: function (result)
//                        {
//                            // if true bisable button 
//                            console.log(result.api_response);
//                            if (result.api_response === 'true') {
//                                $("#vpn_renew").attr("disabled", true);
//                                // $("#vpn_renew_msg").removeClass('d-none');
//                                //$("#vpn_renew_msg").html("Your Request is under proccess ")
//                                // $("#vpn_renew").attr("title", "Your Request is under proccess ");
//                            }
//                        }, error: function (result)
//                        {
//                            console.log('error' + result);
//                        }
//                    });
//                } else {
//                    $("#vpn_renew_form").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Entered VPN / EFORMS Registration number does not belong to you:</div>');
//                    $("#vpn_renew_button").html('');
//                }
//                resetCSRFRandom();
//
//            }, error: function ()
//            {
//                console.log('error');
//            }
//
//        });
//    });


    //ajax for fatch/ call vpn api to get user details 
    //Begins::changes by gaurav on select of options show response
    $("#vpn_reg_no").change(function () {
        var CSRFRandom = $("#CSRFRandom").val();
        var data = $("#vpn_reg_no").val();
        $.ajax({
            type: "POST",
            url: "vpn_fatch2",
            data: {data: data, CSRFRandom: CSRFRandom},
            dataType: 'text',
            async: false,
            success: function (result)
            {
                var vpn_reg_no = $("#vpn_reg_no").val();
                var jvalue = JSON.parse(result);
                var api_details = JSON.parse(jvalue.api_response);

                var data = api_details.access_details;
                console.log(api_details.expdate);
                console.log("----2->" + data);
                if (data !== undefined) {



                    console.log(Object.keys(data).length);
                    // add and create spans 
                    var a = '<span class="alert alert-danger">You can select record for deletion, if you don\'t want to delete record then click directly to Add New button.</span>';
                    a += "<table id='check_table' class='table table-striped table-hover table-bordered'><thead><tr><th>Select</th><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr></thead><tbody>";
                    $.each(data, function (key, value) {
                        var rowdata = key + '~' + value.serip + '~' + value.serloc + '~' + value.destport + '~' + value.desc_service;
                        // onclick=\"fetchTrackByRole('"+ old_forward_by +"','"+ val +"','"+ forword_to +"','"+ data.hmTrack.reg_no +"');\"
                        a += ("<tr id='row-" + key + "'><td><input data-toggle='tooltip' title='Mark for Delete' name='deleted_value' onclick=\"checkForDelete('" + rowdata + "', this);\" type='checkbox'/> </td><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");
                    });
                    a += "</tbody></table>" + "<input type='hidden' id='req_for' name='req_for' value='vpn_delete'/> <input type='hidden'value='" + $("#vpn_reg_no").val() + "' name='vpn_reg_no' id='vpn_reg_no' /> <input type='hidden' id='vpn_coo_email' name='vpn_coo' value='" + $('#vpn_coo').find(":selected").val() + "'/><input type='hidden' id='vpn_coo_email' name='vpn_coo1' value='" + $('#vpn_state_coo').find(":selected").val() + "'/>";
                    //$("#vpn_access_details").append(a);
                    $("#vpn_renew_form").html('');
                    $("#vpn_renew_form").append(a);
                    $("#vpn_renew_button").html('');
                    $("#vpn_edit_button").html('');
                    setTimeout(function () {
                        $("#check_table").DataTable();
                    }, 5);
                    // $("#vpn_renew_button").append("<button class='btn btn-warning' id='vpn_renew' > Renew </button>");
                    $("#vpn_renew_button").append("<button class='btn btn-success ml-2'  id='vpn_addnew' > Add New </button>");
                    // buttons for edit and delete ------------------------------------------------------------------------------
                    //$("#vpn_edit_button").append("<button class='btn btn-warning ml-2' id='vpn_edit' disabled> Edit </button>");
                    //  $("#vpn_edit_button").append("<button class='btn btn-warning ml-2' id='vpn_delete' disabled> Delete </button>");
                    //-----------------------------------------------------------------------------------------------------------

                    $("#vpn_renew_msg").addClass('d-none');
                    //$("#vpn_renew_form").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no").val()+"' name='vpn_reqno' id='vpn_reqno' />")
                    // check for vpn button enable/disable
                    $.ajax({
                        type: "POST",
                        url: "vpn_check_exp",
                        data: {data: data, vpn_reg_no: vpn_reg_no, CSRFRandom: CSRFRandom},
                        dataType: 'json',
                        async: false,
                        success: function (result)
                        {
                            // if true bisable button 
                            console.log(result.api_response);
                            if (result.api_response === 'true') {
                                $("#vpn_renew").attr("disabled", true);
                                // $("#vpn_renew_msg").removeClass('d-none');
                                //$("#vpn_renew_msg").html("Your Request is under proccess ")
                                // $("#vpn_renew").attr("title", "Your Request is under proccess ");
                            }
                        }, error: function (result)
                        {
                            console.log('error' + result);
                        }
                    });
                } else {
                    $("#vpn_renew_form").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Entered VPN / EFORMS Registration number does not belong to you:</div>');
                    $("#vpn_renew_button").html('');
                }
                resetCSRFRandom();

            }, error: function ()
            {
                console.log('error');
            }

        });
    });
//End::changes by gaurav on select of options show response

    // when vpn edit button calls


    $(document).on('click', '#vpn_edit', function (e) {

        var selected = [];
        $('div#vpn_access_details input[type=checkbox]').each(function () {
            if ($(this).is(":checked")) {
                selected.push($(this).attr('value'));
            }
        });

        var all_val = selected;
        //all_val='1~2~3';
        console.log(all_val);
        lol = 0;
        for (var i = 0; i < selected.length; i++)
        {
            var kid = selected[i].split('~');
            console.log('value one---' + kid[1] + '---value two----' + kid[2] + '-----value three---' + kid[3] + '-----value four---' + kid[4]);

            var div = document.createElement('DIV');
            div.setAttribute("class", "row");
            div.innerHTML = GetDynamicTextBox_edit(lol, kid[1], kid[2], kid[3], kid[4]);
            document.getElementById("detailContainer").appendChild(div);
            lol++;
            $("select.form-control").change(function () {
                var data = $(this).val();
                if (data === "Other") {
                    $(this).closest(".detailContainer_div_select").find("#server_other").removeClass('display-hide');
                } else {
                    $(this).closest(".detailContainer_div_select").find("#server_other").addClass('display-hide');
                }
            });
        }
        $('#first_vpn_row').remove();
        $('#modal_id').modal('toggle');
    });



    function GetDynamicTextBox_edit(value, ip, location, port, url) {
        var first_div = '<div id="detailContainer_div_radio" class="detailContainer_div_radio col-md-11 col-10">\n\
        <div class="ip_div_2">\n\
        <div class="row" id="vpn_ip_add_div">\n\
        <div class="col-md-6"><label class="control-label" for="street">IP Address <span style="color: red">*</span></label><br>';
        var ip1 = '', ip2 = '', middle_div;
        if (ip.indexOf("-") != -1) {
            var arr = ip.split('-');
            ip1 = arr[0];
            var index = arr[0].lastIndexOf('.');
            ip2 = arr[0].substring(0, index) + '.' + arr[1];
            middle_div = '<div class="mt-2">\n\
            <label class="k-radio k-radio--bold k-radio--brand">\n\
            <input type="radio" name="vpn_ip_type[' + value + ']" id="ip_single" class="vpn_ip_type" value="single">Single IP<span></span></label>\n\
            &emsp;&emsp;&emsp;&nbsp;\n\
            <label class="k-radio k-radio--bold k-radio--brand">\n\
            <input type="radio" name="vpn_ip_type[' + value + ']" id="ip_range" class="vpn_ip_type" value="range" checked> IP Range <span></span></label>\n\
            <font style="color:red"><span id="ip_type_err"></span></font></div></div></div>\n\
            <div class="row ip_div_1">\n\
            <div class="col-md-4 ip_div display-hide" id="ip_div">\n\
            <label class="control-label" for="street">Enter IP address <span style="color: red">*</span></label>\n\
            <input class="form-control" placeholder="Enter IP Address [e.g: 10.10.10.10]" type="text" name="vpn_new_ip1" value="" id="new_ip1" maxlength="100" aria-required="true">\n\
            <font style="color:red"><span id="new_ip1_err"></span></font></div>\n\
            <div class="col-md-6" id="ip_range_div">\n\
            <div class="row">\n\
            <div class="col-md-6">\n\
            <label class="control-label" for="street">Enter IP range (From) <span style="color: red">*</span></label>\n\
            <input class="form-control" placeholder="Enter IP range (From) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip2" id="new_ip2" maxlength="100" value="' + ip1 + '" aria-required="true">\n\
            <font style="color:red"><span id="new_ip2_err"></span></font>\n\
            </div>\n\
            <div class="col-md-6 ">\n\
            <label class="control-label" for="street">Enter IP range (To) <span style="color: red">*</span></label>\n\
            <input class="form-control" placeholder="Enter IP range (To) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip3" id="new_ip3" maxlength="100" value="' + ip2 + '" aria-required="true">\n\
            <font style="color:red"><span id="new_ip3_err"></span></font></div></div></div>';

        } else {
            ip1 = ip;
            middle_div = '<div class="mt-2">\n\
            <label class="k-radio k-radio--bold k-radio--brand">\n\
            <input type="radio" name="vpn_ip_type[' + value + ']" id="ip_single" class="vpn_ip_type" value="single" checked>Single IP<span></span></label>\n\
            &emsp;&emsp;&emsp;&nbsp;\n\
            <label class="k-radio k-radio--bold k-radio--brand">\n\
            <input type="radio" name="vpn_ip_type[' + value + ']" id="ip_range" class="vpn_ip_type" value="range" > IP Range <span></span></label>\n\
            <font style="color:red"><span id="ip_type_err"></span></font></div></div></div>\n\
            <div class="row ip_div_1">\n\
            <div class="col-md-4 ip_div" id="ip_div">\n\
            <label class="control-label" for="street">Enter IP address <span style="color: red">*</span></label>\n\
            <input class="form-control" placeholder="Enter IP Address [e.g: 10.10.10.10]" type="text" name="vpn_new_ip1" value="' + ip1 + '" id="new_ip1" maxlength="100" aria-required="true">\n\
            <font style="color:red"><span id="new_ip1_err"></span></font></div>\n\
            <div class="col-md-6 display-hide" id="ip_range_div">\n\
            <div class="row">\n\
            <div class="col-md-6">\n\
            <label class="control-label" for="street">Enter IP range (From) <span style="color: red">*</span></label>\n\
            <input class="form-control" placeholder="Enter IP range (From) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip2" id="new_ip2" maxlength="100" value="" aria-required="true">\n\
            <font style="color:red"><span id="new_ip2_err"></span></font>\n\
            </div>\n\
            <div class="col-md-6 ">\n\
            <label class="control-label" for="street">Enter IP range (To) <span style="color: red">*</span></label>\n\
            <input class="form-control" placeholder="Enter IP range (To) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip3" id="new_ip3" maxlength="100" value="" aria-required="true">\n\
            <font style="color:red"><span id="new_ip3_err"></span></font></div></div></div>';
        }

        var second_div = '<div class="col-md-4">\n\
        <label class="control-label" for="street">Application URL <span style="color: red">*</span></label>\n\
        <input class="form-control" placeholder="Enter Application URL [e.g: (abc.com)] " type="text" name="vpn_app_url" id="app_url" value="' + url + '" maxlength="100" aria-required="true">\n\
        <font style="color:red"><span id="app_url_err"></span></font>\n\
        </div>\n\
        <div class="col-md-4">\n\
        <label class="control-label" for="street">Destination Port <span style="color: red">*</span></label>\n\
        <input placeholder="Enter Destination Port [e.g: 80,443] " type="text" name="vpn_dest_port" id="dest_port" value="' + port + '" class="form-control" >\n\
        <font style="color:red"><span id="dest_port_err"></span></font>\n\
        </div>\n\
        </div>';

        var location_div = '';
        if (location.indexOf("NDC Delhi") !== -1 || location.indexOf("NDC Pune") !== -1 || location.indexOf("NDC Hyderabad") !== -1 || location.indexOf("NDC Bhubaneswar") !== -1) {
            $('#server_loc').val(location);
            location_div = '<div class="row detailContainer_div_select">\n\
        <div class="col-md-6">\n\
        <label class="control-label" for="street">Server Location<span style="color: red">*</span></label>\n\
        <select name="vpn_server_loc" class="form-control" id="server_loc">\n\
        <option value="NDC Delhi">NDC Delhi</option>\n\
        <option value="NDC Pune">NDC Pune</option>\n\
        <option value="NDC Hyderabad">NDC Hyderabad</option>\n\
        <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>\n\
        <option value="Other">Other</option></select>\n\
        <font style="color:red"><span id="server_loc_err"></span></font>\n\
        </div>\n\
        <div class="col-md-6 display-hide" id="server_other">\n\
        <label class="control-label" for="street">Enter server location <span style="color: red">*</span></label>\n\
        <input class="form-control" placeholder="Enter Server Location [characters,dot(.) and whitespace]" type="text" name="vpn_server_loc_txt" id="server_loc_txt" value="" maxlength="100">\n\
        <font style="color:red"><span id="server_txt_err"></span></font>\n\
        </div></div></div></div>';
        } else {
            location_div = '<div class="row detailContainer_div_select">\n\
        <div class="col-md-6">\n\
        <label class="control-label" for="street">Server Location<span style="color: red">*</span></label>\n\
        <select name="vpn_server_loc" class="form-control" id="server_loc">\n\
        <option value="NDC Delhi">NDC Delhi</option>\n\
        <option value="NDC Pune">NDC Pune</option>\n\
        <option value="NDC Hyderabad">NDC Hyderabad</option>\n\
        <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>\n\
        <option value="Other" selected>Other</option></select>\n\
        <font style="color:red"><span id="server_loc_err"></span></font>\n\
        </div>\n\
        <div class="col-md-6" id="server_other">\n\
        <label class="control-label" for="street">Enter server location <span style="color: red">*</span></label>\n\
        <input class="form-control" placeholder="Enter Server Location [characters,dot(.) and whitespace]" type="text" name="vpn_server_loc_txt" id="server_loc_txt" value="' + location + '" maxlength="100">\n\
        <font style="color:red"><span id="server_txt_err"></span></font>\n\
        </div></div></div></div>';
        }

        return first_div + middle_div + second_div + location_div;
    }







//
//// for renew vpn form
//    $('#vpn_fatch_data_submit_renew').click(function (e) {
//
//
//
//
//        var CSRFRandom = $("#CSRFRandom").val();
//        var data = $("#vpn_reg_no_renew").val();
//        $.ajax({
//            type: "POST",
//            url: "vpn_fatch_renew",
//            data: {data: data, CSRFRandom: CSRFRandom},
//            dataType: 'text',
//            async: false,
//            success: function (result)
//            {
//                var vpn_reg_no = $("#vpn_reg_no_renew").val();
//                var jvalue = JSON.parse(result);
//                var api_details = JSON.parse(jvalue.api_response);
//
//                var data = api_details.access_details;
//                console.log("----->" + data);
//                console.log(api_details.expdate + vpn_reg_no);
//                if (data !== undefined) {
//
//
//
//                    console.log(Object.keys(data).length);
//                    // add and create spans                    
//                    var a = "<table class='table table-striped table-hover'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";
//
//                    $.each(data, function (key, value) {
//
//                        a += ("<tr><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");
//
//                    });
//
//                    a += "</table>" + "<input type='hidden' id='req_for' name='req_for' value='vpn_renew'/> <input type='text'value='" + $("#vpn_reg_no_renew").val() + "' name='vpn_reg_no' id='vpn_reg_no_renew' /><input type='hidden'value='" + $("input[name='req_co']:checked").val() + "' name='req_co' id='req_co' /> <input type='hidden' id='vpn_coo_email' name='vpn_coo' value='" + $('#vpn_coo').find(":selected").val() + "'/><input type='hidden' id='vpn_coo_email' name='vpn_coo1' value='" + $('#vpn_state_coo').find(":selected").val() + "'/>";
//// a += "</table>" + "<input type='text' id='req_for' name='req_for' value='vpn_renew'/> <input type='text'value='" + $("#vpn_reg_no_renew").val() + "' name='vpn_reg_no' id='vpn_reg_no_renew' /> <input type='text' id='vpn_coo_email' name='vpn_coo_email' value='" + $('#vpn_coo').find(":selected").val() + "'/>";
////$("#vpn_access_details").append(a);
//                    $("#vpn_renew_form_renew").html('');
//                    $("#vpn_renew_form_renew").append(a);
//                    $("#vpn_renew_button_renew").html('');
//                    $("#vpn_renew_button_renew").append("<button class='btn btn-warning' id='vpn_renew' > Renew </button>");
////                    $("#vpn_renew_button_renew").append("<button class='btn btn-success ml-2'  id='vpn_addnew' > Add New </button>");
//                    $("#vpn_renew_msg_renew").addClass('d-none');
////$("#vpn_renew_form_renew").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
//// $("#vpn_renew_form_renew").append("<input type='text' id='req_for' name='req_for' value='change_renew'/> <input type='text'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
//// check for vpn button enable/disable
//                    $.ajax({
//                        type: "POST",
//                        url: "vpn_check_exp",
//                        data: {data: data, vpn_reg_no: vpn_reg_no, CSRFRandom: CSRFRandom},
//                        dataType: 'json',
//                        async: false,
//                        success: function (result)
//                        {
//                            // if true bisable button 
//                            console.log(result.api_response);
//                            if (result.api_response === 'true') {
//                                $("#vpn_renew").attr("disabled", true);
//                                $("#vpn_renew_msg_renew").removeClass('d-none');
//                                $("#vpn_renew_msg_renew").html("Your Request is under proccess ")
//                                $("#vpn_renew").attr("title", "Your Request is under proccess ");
//                            }
//                        }, error: function (result)
//                        {
//                            console.log('error' + result);
//                        }
//                    });
//                } else {
//                    $("#vpn_renew_form_renew").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Entered VPN / EFORMS Registration number does not belong to you:</div>');
//                    $("#vpn_renew_button_renew").html('');
//                }
//                resetCSRFRandom();
//
//            }, error: function ()
//            {
//                console.log('error');
//            }
//
//        });
//    });


// for renew vpn form 
//Begins::changes done by gaurav on change on option show data
    $("#vpn_reg_no_renew").change(function () {
        var CSRFRandom = $("#CSRFRandom").val();
        var data = $("#vpn_reg_no_renew").val();
        $.ajax({
            type: "POST",
            url: "vpn_fatch_renew",
            data: {data: data, CSRFRandom: CSRFRandom},
            dataType: 'text',
            async: false,
            success: function (result)
            {
                var vpn_reg_no = $("#vpn_reg_no_renew").val();
                var jvalue = JSON.parse(result);
                var api_details = JSON.parse(jvalue.api_response);

                var data = api_details.access_details;
                console.log("----->" + data);
                console.log(api_details.expdate + vpn_reg_no);
                if (data !== undefined) {



                    console.log(Object.keys(data).length);
                    // add and create spans                    
                    var a = "<table class='table table-striped table-hover'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";

                    $.each(data, function (key, value) {

                        a += ("<tr><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");

                    });

                    a += "</table>" + "<input type='hidden' id='req_for' name='req_for' value='vpn_renew'/> <input type='text'value='" + $("#vpn_reg_no_renew").val() + "' name='vpn_reg_no' class='form-control' id='vpn_reg_no_renew' readonly /><input type='hidden'value='" + $("input[name='req_co']:checked").val() + "' name='req_co' id='req_co' /> <input type='hidden' id='vpn_coo_email' name='vpn_coo' value='" + $('#vpn_coo').find(":selected").val() + "'/><input type='hidden' id='vpn_coo_email' name='vpn_coo1' value='" + $('#vpn_state_coo').find(":selected").val() + "'/>";
// a += "</table>" + "<input type='text' id='req_for' name='req_for' value='vpn_renew'/> <input type='text'value='" + $("#vpn_reg_no_renew").val() + "' name='vpn_reg_no' id='vpn_reg_no_renew' /> <input type='text' id='vpn_coo_email' name='vpn_coo_email' value='" + $('#vpn_coo').find(":selected").val() + "'/>";
//$("#vpn_access_details").append(a);
                    $("#vpn_renew_form_renew").html('');
                    $("#vpn_renew_form_renew").append(a);
                    $("#vpn_renew_button_renew").html('');
                    $("#vpn_renew_button_renew").append("<button class='btn btn-warning' id='vpn_renew' > Renew </button>");
//                    $("#vpn_renew_button_renew").append("<button class='btn btn-success ml-2'  id='vpn_addnew' > Add New </button>");
                    $("#vpn_renew_msg_renew").addClass('d-none');
//$("#vpn_renew_form_renew").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
// $("#vpn_renew_form_renew").append("<input type='text' id='req_for' name='req_for' value='change_renew'/> <input type='text'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
// check for vpn button enable/disable
                    $.ajax({
                        type: "POST",
                        url: "vpn_check_exp",
                        data: {data: data, vpn_reg_no: vpn_reg_no, CSRFRandom: CSRFRandom},
                        dataType: 'json',
                        async: false,
                        success: function (result)
                        {
                            // if true bisable button 
                            console.log(result.api_response);
                            if (result.api_response === 'true') {
                                $("#vpn_renew").attr("disabled", true);
                                $("#vpn_renew_msg_renew").removeClass('d-none');
                                $("#vpn_renew_msg_renew").html("Your Request is under proccess ")
                                $("#vpn_renew").attr("title", "Your Request is under proccess ");
                            }
                        }, error: function (result)
                        {
                            console.log('error' + result);
                        }
                    });
                } else {
                    $("#vpn_renew_form_renew").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Entered VPN / EFORMS Registration number does not belong to you:</div>');
                    $("#vpn_renew_button_renew").html('');
                }
                resetCSRFRandom();

            }, error: function ()
            {
                console.log('error');
            }

        });
    });
//End::changes done by gaurav on change on option show data


// surrender 
//
//    $('#vpn_fatch_data_submit_surrender').click(function (e) {
//
//
//
//
//        var CSRFRandom = $("#CSRFRandom").val();
//        var data = $("#vpn_reg_no_surrender").val();
//        $.ajax({
//            type: "POST",
//            url: "vpn_fatch_renew",
//            data: {data: data, CSRFRandom: CSRFRandom},
//            dataType: 'text',
//            async: false,
//            success: function (result)
//            {
//                var vpn_reg_no = $("#vpn_reg_no_surrender").val();
//                var jvalue = JSON.parse(result);
//                var api_details = JSON.parse(jvalue.api_response);
//
//                var data = api_details.access_details;
//                console.log("----->" + data);
//                console.log(api_details.expdate + vpn_reg_no);
//                if (data !== undefined) {
//
//
//
//                    console.log(Object.keys(data).length);
//                    // add and create spans                    
//                    var a = "<table class='table table-striped table-hover'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";
//
//                    $.each(data, function (key, value) {
//
//                        a += ("<tr><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");
//
//                    });
//
//                  
//                    a += "</table>" + "<input type='hidden' id='req_for' name='req_for' value='vpn_surrender'/> <input type='hidden'value='" + $("input[name='req_co']:checked").val() + "' name='req_co' id='req_co' /><input type='hidden'value='" + $("#vpn_reg_no_surrender").val() + "' name='vpn_reg_no' id='vpn_reg_no_surrender' /> <input type='hidden' id='vpn_coo_email' name='vpn_coo' value='" + $('#vpn_coo').find(":selected").val() + "'/><input type='hidden' id='vpn_coo_email' name='vpn_coo1' value='" + $('#vpn_state_coo').find(":selected").val() + "'/>";
//// a += "</table>" + "<input type='text' id='req_for' name='req_for' value='vpn_renew'/> <input type='text'value='" + $("#vpn_reg_no_renew").val() + "' name='vpn_reg_no' id='vpn_reg_no_renew' /> <input type='text' id='vpn_coo_email' name='vpn_coo_email' value='" + $('#vpn_coo').find(":selected").val() + "'/>";
////$("#vpn_access_details").append(a);
//                    $("#vpn_renew_form_surrender").html('');
//                    $("#vpn_renew_form_surrender").append(a);
//                    $("#vpn_renew_button_surrender").html('');
//                    $("#vpn_renew_button_surrender").append("<button class='btn btn-warning' id='vpn_surrender_submit' > Surrender </button>");
////                    $("#vpn_renew_button_renew").append("<button class='btn btn-success ml-2'  id='vpn_addnew' > Add New </button>");
//                    $("#vpn_renew_msg_surrender").addClass('d-none');
////$("#vpn_renew_form_renew").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
//// $("#vpn_renew_form_renew").append("<input type='text' id='req_for' name='req_for' value='change_renew'/> <input type='text'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
//// check for vpn button enable/disable
//                    $.ajax({
//                        type: "POST",
//                        url: "vpn_check_surrender",
//                        data: {data: data, vpn_reg_no: vpn_reg_no, CSRFRandom: CSRFRandom},
//                        dataType: 'json',
//                        async: false,
//                        success: function (result)
//                        {
//                            // if true bisable button 
//                            console.log(result.api_response);
//                            if (result.api_response === 'true') {
//                                $("#vpn_surrender_submit").attr("disabled", true);
//                                $("#vpn_renew_msg_surrender").removeClass('d-none');
//                                $("#vpn_renew_msg_surrender").html("Your Request is under proccess ")
//                                $("#vpn_surrender").attr("title", "Your Request is under proccess ");
//                            }
//                        }, error: function (result)
//                        {
//                            console.log('error' + result);
//                        }
//                    });
//                } else {
//                    $("#vpn_renew_form_surrender").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Entered VPN / EFORMS Registration number does not belong to you:</div>');
//                    $("#vpn_renew_button_surrender").html('');
//                }
//                resetCSRFRandom();
//
//            }, error: function ()
//            {
//                console.log('error');
//            }
//
//        });
//    });
//

// surrender 
    $("#vpn_reg_no_surrender").change(function () {
        var CSRFRandom = $("#CSRFRandom").val();
        var data = $("#vpn_reg_no_surrender").val();
        $.ajax({
            type: "POST",
            url: "vpn_fatch_renew",
            data: {data: data, CSRFRandom: CSRFRandom},
            dataType: 'text',
            async: false,
            success: function (result)
            {
                var vpn_reg_no = $("#vpn_reg_no_surrender").val();
                var jvalue = JSON.parse(result);
                var api_details = JSON.parse(jvalue.api_response);

                var data = api_details.access_details;
                console.log("----->" + data);
                console.log(api_details.expdate + vpn_reg_no);
                if (data !== undefined) {



                    console.log(Object.keys(data).length);
                    // add and create spans                    
                    var a = "<table class='table table-striped table-hover'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";

                    $.each(data, function (key, value) {

                        a += ("<tr><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");

                    });


                    a += "</table>" + "<input type='hidden' id='req_for' name='req_for' value='vpn_surrender'/> <input type='hidden'value='" + $("input[name='req_co']:checked").val() + "' name='req_co' id='req_co' /><input type='hidden'value='" + $("#vpn_reg_no_surrender").val() + "' name='vpn_reg_no' id='vpn_reg_no_surrender' /> <input type='hidden' id='vpn_coo_email' name='vpn_coo' value='" + $('#vpn_coo').find(":selected").val() + "'/><input type='hidden' id='vpn_coo_email' name='vpn_coo1' value='" + $('#vpn_state_coo').find(":selected").val() + "'/>";
// a += "</table>" + "<input type='text' id='req_for' name='req_for' value='vpn_renew'/> <input type='text'value='" + $("#vpn_reg_no_renew").val() + "' name='vpn_reg_no' id='vpn_reg_no_renew' /> <input type='text' id='vpn_coo_email' name='vpn_coo_email' value='" + $('#vpn_coo').find(":selected").val() + "'/>";
//$("#vpn_access_details").append(a);
                    $("#vpn_renew_form_surrender").html('');
                    $("#vpn_renew_form_surrender").append(a);
                    $("#vpn_renew_button_surrender").html('');
                    $("#vpn_renew_button_surrender").append("<button class='btn btn-warning' id='vpn_surrender_submit' > Surrender </button>");
//                    $("#vpn_renew_button_renew").append("<button class='btn btn-success ml-2'  id='vpn_addnew' > Add New </button>");
                    $("#vpn_renew_msg_surrender").addClass('d-none');
//$("#vpn_renew_form_renew").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
// $("#vpn_renew_form_renew").append("<input type='text' id='req_for' name='req_for' value='change_renew'/> <input type='text'value='"+$("#vpn_reg_no_renew").val()+"' name='vpn_reqno' id='vpn_reqno' />");
// check for vpn button enable/disable
                    $.ajax({
                        type: "POST",
                        url: "vpn_check_surrender",
                        data: {data: data, vpn_reg_no: vpn_reg_no, CSRFRandom: CSRFRandom},
                        dataType: 'json',
                        async: false,
                        success: function (result)
                        {
                            // if true bisable button 
                            console.log(result.api_response);
                            if (result.api_response === 'true') {
                                $("#vpn_surrender_submit").attr("disabled", true);
                                $("#vpn_renew_msg_surrender").removeClass('d-none');
                                $("#vpn_renew_msg_surrender").html("Your Request is under proccess ")
                                $("#vpn_surrender").attr("title", "Your Request is under proccess ");
                            }
                        }, error: function (result)
                        {
                            console.log('error' + result);
                        }
                    });
                } else {
                    $("#vpn_renew_form_surrender").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Entered VPN / EFORMS Registration number does not belong to you:</div>');
                    $("#vpn_renew_button_surrender").html('');
                }
                resetCSRFRandom();

            }, error: function ()
            {
                console.log('error');
            }
        });
    });


// add new row 
    $(document).on('click', "#vpn_addnew", function () {

        //$("#vpn_renew_form2").append('<table><tr><td><span id="serip_err"></span></td><td><span id="serloc_err"></span></td><td><span id="destport_err"></span></td><td><span id="service_err"></span></td></tr><tr><td><input type="text" name="serip" id="serip" placeholder="Server IP" autofocus="autofocus" required/></td><td><input placeholder="Server Location" type="text" name="serloc" id="serloc" required/></td><td><input placeholder="Destination Port" type="text" name="destport" id="destport" required/></td><td><input placeholder="Service" type="text" name="service" id="service" required/><button id="add_ip"  style="color: green;cursor: pointer;" class="fa fa-check fa-2x" title="ADD"></button>    <i id="remove_ip" title="REMOVE" style="color: red;cursor: pointer;" class="fa fa-remove fa-2x"></i></td></tr></table>');

        // hide model and fatch data and load in vpn_renew_form_new
        $('#modal_id').modal('hide');
        // show data in div  
        //$('#vpn_ip_add_div').addClass('display-hide');
        $('#vpn_renew_form').html('');
        $('#vpn_renew_button').html('');
        var CSRFRandom = $("#CSRFRandom").val();
        var data = $("#vpn_reg_no").val();
        $.ajax({
            type: "POST",
            url: "vpn_fatch2",
            data: {data: data, CSRFRandom: CSRFRandom},
            dataType: 'text',
            async: false,
            success: function (result)
            {

                var jvalue = JSON.parse(result);
                var api_details = JSON.parse(jvalue.api_response);

                var data = api_details.access_details;
                console.log(api_details.expdate);
                if (data !== undefined) {



                    // console.log(Object.keys(data).length);
                    // add and create spans
                    var a = "<table class='table table-striped table-hover'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";

                    $.each(data, function (key, value) {

                        a += ("<tr><td><input type='hidden' name='" + key + "' value ='" + value.serip + "'/> " + value.serip + " </td>" + "<td ><input type='hidden' name='" + key + "' value ='" + value.serloc + "'/> " + value.serloc + " </td><td> <input type='hidden' name='" + key + "' value ='" + value.destport + "'/>" + value.destport + "  </td> <td> <input type='hidden' name='" + key + "' value ='" + value.desc_service + "'/>" + value.desc_service + " </td></tr>");

                    });
                    a += "</table>" + "<input type='hidden' id='req_for' name='' value='vpn_renew'/> <input type='hidden'value='" + $("#vpn_reg_no").val() + "' name='vpn_reg_no' id='vpn_reg_no' />";
                    $("#vpn_access_details_new").append(a);
                    $("#vpn_renew_form_new").html('');
                    $("#vpn_renew_form_new").append(a);
                    $("#new_ip1").focus();
//$("#vpn_renew_button").html('');
//$("#vpn_renew_button").append("<button class='btn btn-warning' id='vpn_renew' > Renew </button>");
// $("#vpn_renew_button").append("<button class='btn '  id='vpn_addnew' > Add New </button>");
//$("#vpn_renew_form").append("<input type='hidden' id='req_for' name='req_for' value='change_renew'/> <input type='hidden'value='"+$("#vpn_reg_no").val()+"' name='vpn_reqno' id='vpn_reqno' />")

                } else {
                    //  $("#vpn_renew_form_new").html('<br/><br/><div style="text-align: center;font-weight: 800;font-style: italic;font-size: large" class="alert alert-danger">Invalid VPN NO:</div>');
                    // $("#vpn_renew_button").html('');
                }
                resetCSRFRandom();

            }, error: function ()
            {
                console.log('error');
            }

        });



// $("#vpn_renew_form2").append('<table><tr> <td><span id="new_ip1_err"></span></td> <td><span id="server_loc_err"></span></td> <td><span id="dest_port_err"></span></td> <td><span id="app_url_err"></span></td> </tr><tr> <td><input type="text" name="new_ip1" id="new_ip1" placeholder="Server IP" autofocus="autofocus" required/></td> <td><select placeholder="Server Location" name="server_loc" id="server_loc" required><option value="NDC Delhi">NDC Delhi</option> <option value="NDC Pune">NDC Pune</option> <option value="NDC Hyderabad">NDC Hyderabad</option> <option value="NDC Bhubaneswar">NDC Bhubaneswar</option> <option value="Other">Other</option></select></td> <td><input placeholder="Destination Port" type="text" name="dest_port" id="dest_port" required/></td> <td><input placeholder="Service" type="text" name="app_url" id="app_url" required/> <button id="add_ip" style="color: green;cursor: pointer;" class="fa fa-check fa-2x" title="ADD"></button> <button id="remove_ip" title="REMOVE" style="color: red;cursor: pointer;" class="fa fa-remove fa-2x"></button></td></tr></table>');
    });
// remove div
    $(document).on('click', "#remove_ip", function () {
        $(this).parent().parent().parent().parent().remove();
    });

// submit 
    $('#vpn_renew_form2').submit(function (e) {
        e.preventDefault();

        var data = JSON.stringify($('#vpn_renew_form2').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val();
        console.log(data + ":::::::" + CSRFRandom);


        $.ajax({
            type: "POST",
            url: "vpn_tab1",
            data: {data: data, ip_type: "single", vpn_form_type: "vpn_renew_form", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 23rdjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.new_ip1_err !== null && jsonvalue.error.new_ip1_err !== "" && jsonvalue.error.new_ip1_err !== undefined)
                {
                    $('#vpn_renew_form2 #new_ip1_err').html(jsonvalue.error.new_ip1_err)
                    $('#vpn_renew_form2 #new_ip1_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_renew_form2 #new_ip1_err').html("");
                }


                if (jsonvalue.error.server_loc_error !== null && jsonvalue.error.server_loc_error !== "" && jsonvalue.error.server_loc_error !== undefined)
                {
                    $('#vpn_renew_form2 #server_loc_err').html(jsonvalue.error.server_loc_error)
                    $('#vpn_renew_form2 #server_loc').focus();
                    error_flag = false;
                } else {

                    $('#vpn_renew_form2 #server_loc_err').html("")
                }

                if (jsonvalue.error.server_txt_error !== null && jsonvalue.error.server_txt_error !== "" && jsonvalue.error.server_txt_error !== undefined)
                {
                    $('#vpn_renew_form2 #server_txt_err').html(jsonvalue.error.server_txt_error)
                    $('#vpn_renew_form2 #server_loc_txt').focus();
                    error_flag = false;
                } else {

                    $('#vpn_renew_form2 #server_txt_err').html("")
                }

                if (jsonvalue.error.app_url_err !== null && jsonvalue.error.app_url_err !== "" && jsonvalue.error.app_url_err !== undefined)
                {
                    $('#vpn_renew_form2 #app_url_err').html(jsonvalue.error.app_url_err);
                    $('#vpn_renew_form2 #app_url_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_renew_form2 #app_url_err').html("");
                }

                if (jsonvalue.error.dest_port_err !== null && jsonvalue.error.dest_port_err !== "" && jsonvalue.error.dest_port_err !== undefined)
                {
                    $('#vpn_renew_form2 #dest_port_err').html(jsonvalue.error.dest_port_err);
                    $('#vpn_renew_form2 #dest_port_err').focus();
                    error_flag = false;
                } else {
                    $('#vpn_renew_form2 #dest_port_err').html("");
                }

// start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

// end, code added by pr on 23rdjan18


                if (!error_flag) {

                } else {
                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#central_div').show();
                        $('#state_div').hide();
                        $('#other_div').hide();
                        $('#other_text_div').addClass("display-hide");
                        $.get('centralMinistry', {
                            orgType: jsonvalue.profile_values.user_employment
                        }, function (response) {
                            var select = $('#min');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                        });
                        setTimeout(function () {
                            $("#min").val(jsonvalue.profile_values.min);
                        }, 500);
                        setTimeout(function () {
                            $.get('centralDepartment', {
                                depType: jsonvalue.profile_values.min
                            }, function (response) {
                                var select = $('#dept');
                                select.find('option').remove();
                                $('<option>').val("").text("-SELECT-").appendTo(select);
                                $.each(response, function (index, value) {
                                    $('<option>').val(value).text(value).appendTo(select);
                                });
                                $('<option>').val("other").text("other").appendTo(select);
                            });
                        }, 500);
                        setTimeout(function () {
                            $("#dept").val(jsonvalue.profile_values.dept);
                        }, 1000);

                        setTimeout(function () {
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);

                            }
                        }, 1000);

                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#central_div').hide();
                        $('#state_div').show();
                        $('#other_div').hide();
                        $('#other_text_div').addClass("display-hide");
                        $.get('centralMinistry', {
                            orgType: jsonvalue.profile_values.user_employment
                        }, function (response) {
                            var select = $('#stateCode');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                        });
                        setTimeout(function () {
                            $("#stateCode").val(jsonvalue.profile_values.stateCode);
                        }, 1000);
                        setTimeout(function () {
                            $.get('centralDepartment', {
                                depType: jsonvalue.profile_values.stateCode
                            }, function (response) {
                                var select = $('#Smi');
                                select.find('option').remove();
                                $('<option>').val("").text("-SELECT-").appendTo(select);
                                $.each(response, function (index, value) {
                                    $('<option>').val(value).text(value).appendTo(select);
                                });
                                $('<option>').val("other").text("other").appendTo(select);
                            });
                        }, 500);
                        setTimeout(function () {
                            $("#Smi").val(jsonvalue.profile_values.dept);
                        }, 1000);
                        setTimeout(function () {
                            if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);

                            }
                        }, 1000);
                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#central_div').hide();
                        $('#state_div').hide();
                        $('#other_div').show();
                        $('#other_text_div').addClass("display-hide");
                        $.get('centralMinistry', {
                            orgType: jsonvalue.profile_values.user_employment
                        }, function (response) {
                            var select = $('#Org');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                            $('<option>').val("other").text("other").appendTo(select);
                        });
                        setTimeout(function () {
                            $("#Org").val(jsonvalue.profile_values.Org);
                        }, 500);
                        setTimeout(function () {
                            if (jsonvalue.profile_values.Org === 'other') {
                                $('#other_text_div').removeClass("display-hide");
                                $('#other_dept').val(jsonvalue.profile_values.other_dept);
                            }
                        }, 1000);
                    } else {
                        $('#central_div').hide();
                        $('#state_div').hide();
                        $('#other_div').hide();
                    }
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#vpn_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#vpn_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#vpn_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#vpn_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#vpn_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#vpn_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#vpn_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#vpn_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#vpn_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#vpn_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    $('#vpn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    //$('#vpn_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#vpn_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#vpn_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#vpn_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#vpn_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#vpn_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    $('#vpn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#vpn_form2 #ca_design').val(jsonvalue.profile_values.ca_design);

                    $('#vpn_form2 #vpn_renew_div_preview').addClass('display-hide');
                    $('#vpn_form2 #ip_single').prop('checked', true);
                    $('#vpn_form2 #ip_div').removeClass('display-hide');
                    $('#vpn_form2 #new_ip1').val(jsonvalue.form_details.new_ip1);


                    if (jsonvalue.form_details.server_loc === "Other")
                    {
                        $('#vpn_form2 #server_other').removeClass('display-hide');
                        $('#vpn_form2 #server_loc_txt').val(jsonvalue.form_details.server_loc_txt);
                        $('#vpn_form2 #server_loc').val(jsonvalue.form_details.server_loc);
                    } else {
                        $('#vpn_form2 #server_other').addClass('display-hide');
                        $('#vpn_form2 #server_loc_txt').val('');
                        $('#vpn_form2 #server_loc').val(jsonvalue.form_details.server_loc);
                    }
                    $('#vpn_form2 #app_url').val(jsonvalue.form_details.app_url);
                    $('#vpn_form2 #dest_port').val(jsonvalue.form_details.dest_port);

                    // $('#modal_id').modal('toggle');

                    $('.edit').removeClass('display-hide');
                    $('#vpn_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#vpn_form2 :button[type='button']").removeAttr('disabled');
                    $("#vpn_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({
                        backdrop: 'static', keyboard: false});
                }

            },
            error: function () {
                console.log('error');
            }
        });



    });


    // reset modal values
    $("#modal_id").on("hidden.bs.modal", function () {
//        $(".modal-body input").val("");
        $("#vpn_renew_form").html("");
        $("#vpn_renew_button").html("");
        // remove radio button

        //$("#vpn_single").prop('checked', true);
    });

    $(document).on('click', "#vpn_renew", function () {

        var data = JSON.stringify($('#vpn_renew_form_renew').serializeObject());
        console.log('DATA:: ' + data);
        var CSRFRandom = $("#CSRFRandom").val();
        $.ajax({
            type: "POST",
            url: "vpn_renew",
            data: {data: data, CSRFRandom: CSRFRandom},
            dataType: 'json',
            async: false,
            success: function (data)
            {

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);


//                if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                {
//                    $('#vpn_renew_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                    $('#vpn_renew_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                    var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                    var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                    $('#vpn_renew_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                } else if (jsonvalue.profile_values.min == "External Affairs")
//                {
//                    $('#vpn_renew_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                    $('#vpn_renew_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                    $('#vpn_renew_confirm #fill_hod_mobile').html("+919384664224");
//                } 
             //   else {

                    $('#vpn_renew_confirm #fill_hod_name').html(jsonvalue.profile_values.hod_name);
                    $('#vpn_renew_confirm #fill_hod_email').html(jsonvalue.profile_values.hod_email);
                    var startMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#vpn_renew_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
              //  }
                $('#vpn_form2_renew #tnc_error').html("");
                $('#stack4').modal({backdrop: 'static', keyboard: false});
            }, error: function ()
            {
                console.log('error');
            }

        });


    });


    // vpn delete

    $(document).on('click', "#vpn_delete", function () {
//        var selected = [];
//        $('div#vpn_access_details input[type=checkbox]').each(function () {
//            if ($(this).is(":checked")) {
//                selected.push($(this).attr('value'));
//            }
//        });
//        //var all_val = selected;
//        console.log(selected);
        var deleted_value = $("#vpn_renew_form input[name='deleted_value']:checked").map(function () {
            return this.value;
        }).get().join('#');



        if (deleted_value.includes("#")) {

            var split_hash = deleted_value.split("#");
            var final_table = "<table border='2'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";
            for (var i = 0; i < split_hash.length; i++) {
                var split_delete = split_hash[i].split("~");
                final_table += "<tr><td>" + split_delete[1] + "</td><td>" + split_delete[2] + "</td><td>" + split_delete[3] + "</td><td>" + split_delete[4] + "</td></tr>";


            }
            final_table += "</table>";

        } else {
            var final_table = "<table border='2'><tr><th>Server IP</th><th>Server Location</th><th>Destination Port</th><th>Service</th></tr>";

            var split_delete2 = deleted_value.split("~");
            final_table += "<tr><td>" + split_delete2[1] + "</td><td>" + split_delete2[2] + "</td><td>" + split_delete2[3] + "</td><td>" + split_delete2[4] + "</td></tr>";



            final_table += "</table>";
        }




        bootbox.confirm({
            message: "Do you want to delete Below data?</br></br>" + final_table,
            buttons: {
                confirm: {
                    label: 'Yes',
                    className: 'btn-success'
                },
                cancel: {
                    label: 'No',
                    className: 'btn-danger'
                }
            },
            callback: function (result) {
                console.log('This was logged in the callback: ' + result); // true for yes .. false for no
                if (result === true) {
                    var data = JSON.stringify($('#vpn_renew_form').serializeObject());
                    var deleted_value = $("#vpn_renew_form input[name='deleted_value']:checked").map(function () {
                        return this.value;
                    }).get().join('#');

                    console.log('deleted_value:: ' + deleted_value);
                    var vpn_reg_no = $('#vpn_renew_form #vpn_reg_no').val();
                    console.log('vpn_reg_no: ' + vpn_reg_no);

                    var CSRFRandom = $("#CSRFRandom").val();
                    $.ajax({
                        type: "POST",
                        url: "vpn_delete",
                        data: {CSRFRandom: CSRFRandom, deleted_value: deleted_value, data: data},
                        dataType: 'json',
                        async: false,
                        success: function (data)
                        {

                            var myJSON = JSON.stringify(data);
                            var jsonvalue = JSON.parse(myJSON);

                            $('#vpn_delete_confirm #fill_hod_name').html(jsonvalue.profile_values.hod_name);
                            $('#vpn_delete_confirm #fill_hod_email').html(jsonvalue.profile_values.hod_email);
                            var startMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                            $('#vpn_delete_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                            // $('#vpn_form2_renew #tnc_error').html("");
                            $('#stack6').modal({backdrop: 'static', keyboard: false});
                        }, error: function ()
                        {
                            console.log('error');
                        }
                    });
                }
            }
        });
    });

    // surrender on submit
    $(document).on('click', "#vpn_surrender_submit", function () {

        var data = JSON.stringify($('#vpn_renew_form_surrender').serializeObject());
        console.log('DATA:: ' + data);
        var CSRFRandom = $("#CSRFRandom").val();
        $.ajax({
            type: "POST",
            url: "vpn_surrender",
            data: {data: data, CSRFRandom: CSRFRandom},
            dataType: 'json',
            async: false,
            success: function (data)
            {

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

//                if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
//                {
//                    $('#vpn_surrender_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                    $('#vpn_surrender_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                    var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                    var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                    $('#vpn_surrender_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//
//
//                } else if (jsonvalue.profile_values.min == "External Affairs")
//                {
//                    $('#vpn_surrender_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                    $('#vpn_surrender_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                    $('#vpn_surrender_confirm #fill_hod_mobile').html("+919384664224");
//                }
              //  else {
                    $('#vpn_surrender_confirm #fill_hod_name').html(jsonvalue.profile_values.hod_name);
                    $('#vpn_surrender_confirm #fill_hod_email').html(jsonvalue.profile_values.hod_email);
                    var startMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#vpn_surrender_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
              //  }
                $('#vpn_form2_surrender #tnc_error').html("");
                $('#stack5').modal({backdrop: 'static', keyboard: false});
            }, error: function ()
            {
                console.log('error');
            }

        });


    });





    $(document).on('click', '.vpnentry', function (e) {
        var field_id = $(this).attr('id');
        var field_data = $(this).attr('data');
        var resultId = field_data.split(',')[0];
        $.ajax({
            type: "POST",
            url: "vpn_entry_edit",
            data: {field_id: field_id, field_data: field_data},
            dataType: 'json',
            async: false,
            success: function (data)
            {
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                if (jsonvalue.returnString === 'done' && field_id === 'disable_vpnentry') {
                    $("." + resultId).addClass("blue").removeClass("red").prop('id', 'enable_vpnentry').html('Enable');
                    if (jsonvalue.action_type === 'disapprove') {
                        $("#vpn_approve_btn").prop("disabled", "true");
                        $("#vpn_action_form #statRemarksError").html('You have no entries to Approve. Please enable atleast one entry to Approve the request.');
                    } else {
                        $("#vpn_approve_btn").removeAttr('disabled');
                        $("#vpn_action_form #statRemarksError").html('');
                    }
                } else if (jsonvalue.returnString === 'done' && field_id === 'enable_vpnentry') {
                    $("." + resultId).addClass("red").removeClass("blue").prop('id', 'disable_vpnentry').html('Disable');
                    if (jsonvalue.action_type === 'disapprove') {
                        $("#vpn_approve_btn").prop("disabled", "true");
                        $("#vpn_action_form #statRemarksError").html('You have no entries to Approve. Please enable atleast one entry to Approve the request.');
                    } else {
                        $("#vpn_approve_btn").removeAttr('disabled');
                        $("#vpn_action_form #statRemarksError").html('');
                    }
                } else if (jsonvalue.returnString === 'not done') {
                    alert('something went wrong');
                }
            }, error: function ()
            {
                console.log('error');
            }

        });

    });

});

function GetDynamicTextBox(value) {
    console.log(value);
    return '<div id="detailContainer_div_radio" class="detailContainer_div_radio col-md-11 col-10">\n\
 <div class="ip_div_2">\n\
 <div class="row" id="vpn_ip_add_div">\n\
 <div class="col-md-6"> \n\
 <label class="control-label" for="street">IP Address <span style="color: red">*</span></label><br> \n\
 <div="mt-2"><label class="k-radio k-radio--bold k-radio--brand"><input type="radio" name="vpn_ip_type[' + value + ']" id="ip_single" class="vpn_ip_type" value="single" checked> Single IP \n\
 <span></span></label>&emsp;&emsp;&emsp;&nbsp;<label class="k-radio k-radio--bold k-radio--brand"><input type="radio" name="vpn_ip_type[' + value + ']" id="ip_range" class="vpn_ip_type" value="range" > IP Range <span></span></label><font style="color:red"><span id="ip_type_err"></span></font></div> \n\
 </div> \n\
 </div> \n\
 <div class="row ip_div_1"> \n\
 <div class="col-md-4 ip_div" id="ip_div"> \n\
 <label class="control-label" for="street">Enter IP address <span style="color: red">*</span></label>\n\
 <input class="form-control" placeholder="Enter IP Address [e.g: 10.10.10.10]" type="text" name="vpn_new_ip1" id="new_ip1" maxlength="100" aria-required="true"> <font style="color:red"><span id="new_ip1_err"></span></font> \n\
 </div> \n\
 <div class="display-hide col-md-6" id="ip_range_div"> \n\
 <div class="row"><div class="col-md-6"> \n\
 <label class="control-label" for="street">Enter IP range (From) <span style="color: red">*</span></label>\n\
 <input class="form-control" placeholder="Enter IP range (From) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip2" id="new_ip2" maxlength="100" aria-required="true"> <font style="color:red"><span id="new_ip2_err"></span></font>\n\
 </div>\n\
 <div class="col-md-6 ">\n\
 <label class="control-label" for="street">Enter IP range (To) <span style="color: red">*</span></label>\n\
 <input class="form-control" placeholder="Enter IP range (To) [e.g: 10.10.10.10]" type="text" name="vpn_new_ip3" id="new_ip3" value="" maxlength="100" aria-required="true"> <font style="color:red"><span id="new_ip3_err"></span></font>\n\
 </div></div>\n\
 </div>\n\
 <div class="col-md-4">\n\
 <label class="control-label" for="street">Application URL <span style="color: red"></span></label>\n\
 <input class="form-control" placeholder="Enter Application URL [e.g: (abc.com)] " type="text" name="vpn_app_url" id="app_url" value="" maxlength="100" aria-required="true"> <font style="color:red"><span id="app_url_err"></span></font>\n\
 </div>\n\
 <div class="col-md-4">\n\
 <label class="control-label" for="street">Destination Port <span style="color: red">*</span></label>\n\
 <input placeholder="Enter Destination Port [e.g: 80,443] " type="text" name="vpn_dest_port" id="dest_port" class="form-control" > <font style="color:red"><span id="dest_port_err"></span></font>\n\
 </div>\n\
 </div>\n\
 <div class="row detailContainer_div_select">\n\
 <div class="col-md-6">\n\
 <label class="control-label" for="street">Server Location<span style="color: red">*</span></label>\n\
 <select name="vpn_server_loc" class="form-control" id="server_loc">\n\
 <option value="NDC Delhi">NDC Delhi</option>\n\
 <option value="NDC Pune">NDC Pune</option>\n\
 <option value="NDC Hyderabad">NDC Hyderabad</option>\n\
 <option value="NDC Bhubaneswar">NDC Bhubaneswar</option>\n\
 <option value="Other">Other</option>\n\
 </select>\n\
 <font style="color:red"><span id="server_loc_err"></span></font>\n\
 </div>\n\
 <div class="col-md-6 display-hide" id="server_other">\n\
 <label class="control-label" for="street">Enter server location <span style="color: red">*</span></label>\n\
 <input class="form-control" placeholder="Enter Server Location [characters,dot(.) and whitespace]" type="text" name="vpn_server_loc_txt" id="server_loc_txt" value="" maxlength="100"> <font style="color:red"><span id="server_txt_err"></span></font>\n\
 </div>\n\
 </div>\n\
 </div>\n\
 </div>\n\
 <div class="add-ip-div col-md-1 col-2"><div class="btn-div"><button class="btn btn-primary float-right" type="button" onclick="AddTextBox()"><i class="fa fa-plus"></i></button> <button type="button" value="Remove" onclick="RemoveTextBox(this)" class="btn btn-danger float-right mt-2" title="Remove Row"><i class="fa fa-minus"></i></button></div></div>';

}
window.lol = 1;
function AddTextBox() {

    var div = document.createElement('DIV');
    div.setAttribute("class", "row");
    div.innerHTML = GetDynamicTextBox(lol);
    document.getElementById("detailContainer").appendChild(div);
    lol++;
    $("select.form-control").change(function () {
        var data = $(this).val();
        if (data === "Other") {
            $(this).closest(".detailContainer_div_select").find("#server_other").removeClass('display-hide');
        } else {
            $(this).closest(".detailContainer_div_select").find("#server_other").addClass('display-hide');
        }
    });
}
function RemoveTextBox(div) {

    $(div).closest('.row').remove();

    console.log($(this))
    $(div).closest('.row').find('.detailContainer_div_radio').remove();
    // $(div.parentNode).prev('.detailContainer_div_radio').remove();

}


$(document).on("click", "#mod1", function (e) {
    bootbox.confirm({
        closeButton: false,
        title: "Confirmation",
        message: "If you close this window. You will be redirect as a new request.",
        buttons: {
            confirm: {
                label: 'yes',
                className: 'btn-danger'
            },
            cancel: {
                label: 'No',
                className: 'btn-primary'
            }
        },
        callback: function (result) {
            if (result === true) {
                location.reload();
            } else if (result === false) {
                $('#modal_id').modal('show');
            }
        }
    });
});


$(document).on("click", "#mod2", function (e) {
    bootbox.confirm({
        closeButton: false,
        title: "Confirmation",
        message: "If you close this window. You will be redirect as a new request.",
        buttons: {
            confirm: {
                label: 'Yes',
                className: 'btn-danger'
            },
            cancel: {
                label: 'No',
                className: 'btn-primary'
            }
        },
        callback: function (result) {
            if (result === true) {
                location.reload();
            } else if (result === false) {
                $('#modal_id_renew').modal('show');
            }
        }
    });
});

$(document).on("click", "#mod3", function (e) {
    bootbox.confirm({
        closeButton: false,
        title: "Confirmation",
        message: "If you close this window. You will be redirect as a new request.",
        buttons: {
            confirm: {
                label: 'Yes',
                className: 'btn-danger'
            },
            cancel: {
                label: 'No',
                className: 'btn-primary'
            }
        },
        callback: function (result) {
            if (result === true) {
                location.reload();
            } else if (result === false) {
                $('#modal_id_surrender').modal('show');
            }
        }
    });
});