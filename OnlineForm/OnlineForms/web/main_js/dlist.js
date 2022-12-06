/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var error_name = false;
var error_email = false;
var error_mobile = false;

$(document).ready(function () {


    $('.page-container-bg-solid').find('input').click(function () {
        var field_form = $(this).closest('form').attr('id');
        var field_name = $(this).attr('name');

        if (field_name === 'list_temp') {
            var list_temp = $(this).val();

            if (list_temp === 'no') {
                $('#' + field_form + ' #validity_date_show').hide();
                $('#' + field_form + ' #validity_date').val('');
                $('#' + field_form + ' #validity_date1').val('');
            } else {
                $('#' + field_form + ' #validity_date_show').show();
            }
        }
    });

    $("#validity_date,#validity_date1").datepicker({
        dateFormat: "dd-mm-yy", changeMonth: true, changeYear: true, yearRange: "+0:+65", minDate: "+1D", maxDate: "2M"
    });

    $('#dlist_form1').submit(function (e) {
        e.preventDefault();

        var data = JSON.stringify($('#dlist_form1').serializeObject());
        var validateForm = $('.page-container-bg-solid #dlist_form1 input,textarea,select').blur();

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18


        if (validateForm) {
            $.ajax({
                type: "POST",
                url: "dlist_tab1",
                data: {data: data, CSRFRandom: CSRFRandom},
                datatype: JSON,
                success: function (data) {


                    resetCSRFRandom();// line added by pr on 22ndjan18

                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);

                    var error_flag = true;

                    if (jsonvalue.error.list_name_error !== null && jsonvalue.error.list_name_error !== "" && jsonvalue.error.list_name_error !== undefined)
                    {
                        $('#list_name_err').html(jsonvalue.error.list_name_error);
                        $('#dlist_form1 #list_name_err').focus();
                        error_flag = false;

                    } else {
                        $('#list_name_err').html("");
                    }


                    if (jsonvalue.error.list_desc_error !== null && jsonvalue.error.list_desc_error !== "" && jsonvalue.error.list_desc_error !== undefined)
                    {
                        $('#description_list_err').html(jsonvalue.error.list_desc_error);
                        $('#dlist_form1 #description_list_err').focus();
                        error_flag = false;
                    } else {
                        $('#description_list_err').html("");
                    }


                    if (jsonvalue.error.list_mod_error !== null && jsonvalue.error.list_mod_error !== "" && jsonvalue.error.list_mod_error !== undefined)
                    {
                        $('#list_mod_err').html(jsonvalue.error.list_mod_error);
                        $('#dlist_form1 #list_mod_err').focus();
                        error_flag = false;
                    } else {
                        $('#list_mod_err').html("");
                    }



                    if (jsonvalue.error.list_allowed_error !== null && jsonvalue.error.list_allowed_error !== "" && jsonvalue.error.list_allowed_error !== undefined)
                    {
                        $('#allowed_member_err').html(jsonvalue.error.list_allowed_error);
                        $('#dlist_form1 #allowed_member_err').focus();
                        error_flag = false;
                    } else {
                        $('#allowed_member_err').html("");
                    }

                    if (jsonvalue.error.list_temp_error !== null && jsonvalue.error.list_temp_error !== "" && jsonvalue.error.list_temp_error !== undefined)
                    {
                        $('#list_temp_err').html(jsonvalue.error.list_temp_error);
                        $('#dlist_form1 #list_temp_err').focus();

                        error_flag = false;
                    } else {
                        $('#list_temp_err').html("");
                    }


                    if (jsonvalue.error.list_validdate_error !== null && jsonvalue.error.list_validdate_error !== "" && jsonvalue.error.list_validdate_error !== undefined)
                    {
                        $('#validity_date_err').html(jsonvalue.error.list_validdate_error);
                        $('#dlist_form1 #validity_date_err').focus();
                        error_flag = false;
                    } else {
                        $('#validity_date_err').html("");
                    }

                    if (jsonvalue.error.list_nonnicnet_error !== null && jsonvalue.error.list_nonnicnet_error !== "" && jsonvalue.error.list_nonnicnet_error !== undefined)
                    {
                        $('#non_nicnet_err').html(jsonvalue.error.list_nonnicnet_error);
                        $('#dlist_form1 #non_nicnet_err').focus();
                        error_flag = false;
                    } else {
                        $('#non_nicnet_err').html("");
                    }
                    
                     if (jsonvalue.error.total_member_err !== null && jsonvalue.error.total_member_err !== "" && jsonvalue.error.total_member_err !== undefined)
                    {
                        $('#total_member_err').html(jsonvalue.error.total_member_err);
                        $('#dlist_form1 #total_member_err').focus();
                        error_flag = false;
                    } else {
                        $('#total_member_err').html("");
                    }


                    // start, code added by pr on 22ndjan18

                    if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                    {

                        $('#captchaerror').html(jsonvalue.error.csrf_error);
                        error_flag = false;
                    }

                    // end, code added by pr on 22ndjan18


                    if (!error_flag) {

                    } else {

                        $('#tab1').hide();
                        $('#tab2').show();
                        $('#dlist_tab1_nav').removeClass('active');
                        $('#dlist_tab1_nav').addClass('done');
                        $('.progress-bar').css("width", "100%");
                        $('#dlist_tab2_nav').addClass('active');
                        $('.button-previous').click(function () {
                            $('#tab1').show();
                        });

                    }

                },
                error: function () {

                }
            });
        }
    });


    $('#moderator_admin').click(function (e) {
        var isChecked = $(this).is(":checked");
        if (isChecked) {
            $.ajax({
                type: "POST",
                url: "fill_dlist_moderator_tab",
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);

                    $('#dlist_form2 #auth_off_name').val(jsonvalue.profile_values.cn);
                    $('#dlist_form2 #auth_email').val(jsonvalue.profile_values.email);
                    $('#dlist_form2 #mobile').val(jsonvalue.profile_values.mobile);
                },
                error: function () {
                    //alert("please Try after some time");
                    console.log('error');
                }
            });
        } else {
            $('#dlist_form2')[0].reset();
        }
    });



    function createThead1() {
        var thead = "";
        thead += '<tr><td class="text-center" width="8%">S.No</td><td>Moderator Name</td><td>Moderator E-Mail Address</td><td>Moderator Mobile</td> <td class="text-center">Action</td></tr>';
        console.log("inside CreateThead1:::::::::::::");

        return thead;
    }


    function createThead2() {
        var thead = "";
        thead += '<tr><td class="text-center" width="8%">S.No</td><td>Owner Name</td><td>Owner E-Mail Address</td><td>Owner Mobile</td> <td class="text-center">Action</td></tr>';
        console.log("inside CreateThead2:::::::::::::");

        return thead;
    }

    $('#addmoderator').click(function () {
        $('.text-danger').html('');
        name_err = false;
        email_err = false;
        mobile_err = false;

        checkName();
        checkEmail();
        checkMobile();
        console.log("mobileee ::::::" + mobile_err);
        if (email_err == false && name_err == false && mobile_err == false) {
            var dataObj = {dlistData: $('#dlist_form2').serializeObject()};
            var data = JSON.stringify(dataObj);
            console.log("checkkk:::::::::" + data)
            $.ajax({
                type: 'POST',
                url: 'moderatorDataPost',
                data: data,
                datatype: JSON,
                contentType: 'application/json; charset=utf-8',
                success: function (data) {
                    if (data.error.ModeratorEmail) {
                        bootbox.alert(data.error.ModeratorEmail);

                    }

                    if (data.error.Mod) {

                        console.log("inside if of duplicate");

                        $("#tauth_email_err").html(data.error.Mod);
                        $("#tauth_email_err").show();
                    }

                    if (data.error.ModeratorName) {
                        $("#tauth_off_name_err").html("Enter Valid Name");
                        $("#tauth_off_name_err").show();
                    }
                    if (!jQuery.isEmptyObject(data.bulkData.dnsSingleData)) {
                        $('.card-holder').removeClass('err-holder');
                        $("#moderator_collection").removeClass("d-none");
                        $("#dlist_form2 input:text").val("");
                        console.log("inside if block2 ::::::::::::");
                        var thead = "";
                        thead = createThead1();
                        $("#moderator_collection_tbl thead").html("");
                        $("#moderator_collection_tbl thead").html(thead);
                        console.log("inside if block3 ::::::::::::");
                        singleRecordTbl1(data.bulkData.dnsSingleData);
                    }
                },
                error: function (data) {
                    console.log(data)
                },
                complete: function () {
                    $('.loader').hide();
                }
            });
        }
    });



    $('#addowner').click(function () {
        var dataObj = {dlistData1: $('#dlist_form2').serializeObject()};
        var data = JSON.stringify(dataObj);
        email_err = false;
        name_err = false;
        mobile_err = false;

        checkOwnerName();
        checkOwnerEmail();
        checkOwnerMobile();

        console.log("mobileee222 ::::::" + mobile_err);
        if (email_err == false && name_err == false && mobile_err == false) {
            console.log("checkkk:::::::::" + data)
            $.ajax({
                type: 'post',
                url: 'singleOwnerDataPost',
                data: data,
                datatype: JSON,
                contentType: 'application/json; charset=utf-8',
                success: function (data) {

                    console.log(data.bulkData.dnsSingleData1);
                    console.log(data.bulkData.dnsSingleData1);
                    if (data.error.OwnerEmail) {
                        bootbox.alert(data.error.OwnerEmail);
                    }

                    if (data.error.OwnerName) {
                        $("#auth_owner_name1_err").html("Enter Valid Owner Name.");
                        $("#auth_owner_name1_err").show();
                    }

                    if (data.error.Own) {

                        console.log("inside if of duplicate of Owner");

                        $("#owner_email1_err").html(data.error.Own);
                        $("#owner_email1_err").show();
                    }

                    if (!jQuery.isEmptyObject(data.bulkData.dnsSingleData)) {


                        console.log("inside if block1 ::::::::::::");
                        console.log(data.bulkData.dnsSingleData);

                        $('.card-holder').removeClass('err-holder');
                        $("#owner_collection").removeClass("d-none");
                        $("#dlist_form2 input:text").val("");
                        console.log("inside if block2 ::::::::::::");
                        var thead = "";
                        thead = createThead2();
                        $("#owner_collection_tbl thead").html("");
                        $("#owner_collection_tbl thead").html(thead);
                        console.log("inside if block3 ::::::::::::");
                        singleRecordTbl2(data.bulkData.dnsSingleData);
                    }



                },
                complete: function () {
                    $('.loader').hide();
                }
            });
        }
    });
    $('#dlist_form2').submit(function (e) {
        e.preventDefault();

        var data = JSON.stringify($('#dlist_form2').serializeObject());

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $('#dlist_form2 .edit').removeClass('display-hide');

        $.ajax({
            type: "POST",
            url: "dlist_tab2",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;


              
                if (jsonvalue.error.ModeratorData !== null && jsonvalue.error.ModeratorData !== "" && jsonvalue.error.ModeratorData !== undefined) {

                    console.log("indide valation check");
                    $('#dlist_form2 #tauth_off_name_err').html("please enter valid details")
                    error_flag = false;
                    $('#dlist_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                } else {
                    $('#dlist_form2 #tauth_off_name_err').html("")



                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.ModeratorData) {
                    bootbox.alert(jsonvalue.error.ModeratorData);


                }
                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#dlist_form2  #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#dlist_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dlist_form2  #imgtxt').val("");
                }
                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#dlist_form2  #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#dlist_form2  #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("")
                } else {
                    $('#dlist_form2 #captchaerror').html("");
                    $('#dlist_form2 #captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dlist_form2 #imgtxt').val("")
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
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#dlist_form3 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#dlist_form3 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#dlist_form3 #user_name').val(jsonvalue.profile_values.cn);
                    $('#dlist_form3 #user_design').val(jsonvalue.profile_values.desig);
                    $('#dlist_form3 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#dlist_form3 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#dlist_form3 #user_state').val(jsonvalue.profile_values.state);
                    $('#dlist_form3 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#dlist_form3 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#dlist_form3 #user_rphone').val(jsonvalue.profile_values.rphone);
                    //$('#dlist_form3 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#dlist_form3 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#dlist_form3 #user_email').val(jsonvalue.profile_values.email);
                    $('#dlist_form3 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#dlist_form3 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#dlist_form3 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#dlist_form3 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#dlist_form3 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    var startROMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var endROMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#dlist_form3 #hod_mobile').val(startROMobile + 'XXXXXXX' + endROMobile);
                    $('#dlist_form3 #ca_design').val(jsonvalue.profile_values.ca_design);

                    //form details

                    $('#dlist_form3 #list_name').val(jsonvalue.form_details.list_name);
                    $('#dlist_form3 #description_list').val(jsonvalue.form_details.description_list);
                    $('#dlist_form3 #validity_date1').val(jsonvalue.form_details.validity_date);

                    if (jsonvalue.form_details.list_mod === 'yes') {
                        $('#dlist_form3 #list_mod_yes').prop('checked', true);
                    } else {
                        $('#dlist_form3 #list_mod_no').prop('checked', true);
                    }

                    if (jsonvalue.form_details.allowed_member === 'yes') {
                        $('#dlist_form3 #allowed_member_yes').prop('checked', true);
                    } else {
                        $('#dlist_form3 #allowed_member_no').prop('checked', true);
                    }

                    if (jsonvalue.form_details.list_temp === 'yes') {
                        $('#dlist_form3 #list_temp_yes').prop('checked', true);
                        $('#dlist_form3 #validity_date_show').show();

                    } else {
                        $('#dlist_form3 #list_temp_no').prop('checked', true);
                        $('#dlist_form3 #validity_date_show').hide();
                    }

                    if (jsonvalue.form_details.non_nicnet === 'yes') {
                        $('#dlist_form3 #non_nicnet_yes').prop('checked', true);

                    } else {
                        $('#dlist_form3 #non_nicnet_no').prop('checked', true);
                    }
                    $('#dlist_form3 #memberCount').val(jsonvalue.form_details.memberCount);


                    if (jsonvalue.bulkData.moderatorData == null) {
                        $('#dlist_form3 #moderator_collection').addClass("d-none");
                        $('#dlist_form3 #mod_heading').addClass("d-none");
                    }

                    if (jsonvalue.bulkData.ownerData == null) {
                        $('#dlist_form3 #owner_collection').addClass("d-none");
                        $('#dlist_form3 #own_heading').addClass("d-none");
                    }

                    $('#dlist_form3 #auth_off_name').val(jsonvalue.form_details.t_off_name);
                    $('#dlist_form3 #auth_email').val(jsonvalue.form_details.tauth_email);
                    $('#dlist_form3 #mobile').val(jsonvalue.form_details.tmobile);

                    $('.edit').removeClass('display-hide');
                    $('#dlist_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#dlist_form3 :button[type='button']").removeAttr('disabled');
                    $("#dlist_form3 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }
            },
            error: function () {

            }
        });
    });

    $('#dlist_form3 .edit').click(function () {

        var employment = $('#dlist_preview_tab #user_employment').val();
        var min = $('#dlist_preview_tab #min').val();
        var dept = $('#dlist_preview_tab #dept').val();
        var statecode = $('#dlist_preview_tab #stateCode').val();
        var Smi = $('#dlist_preview_tab #Smi').val();
        var Org = $('#dlist_preview_tab #Org').val();

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
            if (dept === 'other') {
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
            if (Smi === 'other') {
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

        $('#dlist_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#dlist_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        $(this).addClass('display-hide');
        //$(this).hide();
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dlist_preview_tab #REditPreview #hod_email').removeAttr('disabled');
    });


    $('#dlist_form3 #confirm').click(function (e) {
        e.preventDefault();
       
        $("#dlist_form3 :disabled").removeAttr('disabled');
        $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dlist_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#dlist_form3').serializeObject());
        $('#dlist_preview_tab #user_email').prop('disabled', 'true');

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "dlist_tab3",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18                

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.list_name_error !== null && jsonvalue.error.list_name_error !== "" && jsonvalue.error.list_name_error !== undefined)
                {
                    $('#dlist_form3 #list_name_err').html(jsonvalue.error.list_name_error);
                    $('#dlist_form3 #list_name_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #list_name_err').html("");
                }


                if (jsonvalue.error.list_desc_error !== null && jsonvalue.error.list_desc_error !== "" && jsonvalue.error.list_desc_error !== undefined)
                {
                    $('#dlist_form3 #description_list_err').html(jsonvalue.error.list_desc_error);
                    $('#dlist_form3 #description_list_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #description_list_err').html("");
                }


                if (jsonvalue.error.list_mod_error !== null && jsonvalue.error.list_mod_error !== "" && jsonvalue.error.list_mod_error !== undefined)
                {
                    $('#dlist_form3 #list_mod_err').html(jsonvalue.error.list_mod_error);
                    $('#dlist_form3 #list_mod_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #list_mod_err').html("");
                }



                if (jsonvalue.error.list_allowed_error !== null && jsonvalue.error.list_allowed_error !== "" && jsonvalue.error.list_allowed_error !== undefined)
                {
                    $('#dlist_form3 #allowed_member_err').html(jsonvalue.error.list_allowed_error);
                    $('#dlist_form3 #allowed_member_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #allowed_member_err').html("");
                }

                if (jsonvalue.error.list_temp_error !== null && jsonvalue.error.list_temp_error !== "" && jsonvalue.error.list_temp_error !== undefined)
                {
                    $('#dlist_form3 #list_temp_err').html(jsonvalue.error.list_temp_error);
                    $('#dlist_form3 #list_temp_err').focus();

                    error_flag = false;
                } else {
                    $('#dlist_form3 #list_temp_err').html("");
                }


                if (jsonvalue.error.list_validdate_error !== null && jsonvalue.error.list_validdate_error !== "" && jsonvalue.error.list_validdate_error !== undefined)
                {
                    $('#dlist_form3 #validity_date_err').html(jsonvalue.error.list_validdate_error);
                    $('#dlist_form3 #validity_date_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #validity_date_err').html("");
                }

                if (jsonvalue.error.list_nonnicnet_error !== null && jsonvalue.error.list_nonnicnet_error !== "" && jsonvalue.error.list_nonnicnet_error !== undefined)
                {
                    $('#dlist_form3 #non_nicnet_err').html(jsonvalue.error.list_nonnicnet_error);
                    $('#dlist_form3 #non_nicnet_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #non_nicnet_err').html("");
                }

                if (jsonvalue.error.mod_name_error !== null && jsonvalue.error.mod_name_error !== "" && jsonvalue.error.mod_name_error !== undefined)
                {
                    $('#dlist_form3 #tauth_off_name_err').html(jsonvalue.error.mod_name_error);
                    $('#dlist_form3 #tauth_off_name_err').focus();

                    error_flag = false;
                } else {
                    $('#dlist_form3 #tauth_off_name_err').html("");
                }


                if (jsonvalue.error.mod_email_error !== null && jsonvalue.error.mod_email_error !== "" && jsonvalue.error.mod_email_error !== undefined)
                {
                    $('#dlist_form3 #tauth_email_err').html(jsonvalue.error.mod_email_error);
                    $('#dlist_form3 #tauth_email_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #tauth_email_err').html("");
                }

                if (jsonvalue.error.mod_mobile_error !== null && jsonvalue.error.mod_mobile_error !== "" && jsonvalue.error.mod_mobile_error !== undefined)
                {
                    $('#dlist_form3 #tmobile_err').html(jsonvalue.error.mod_mobile_error);
                    $('#dlist_form3 #tmobile_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #tmobile_err').html("");
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
                    $('#dlist_form3 #useremployment_error').focus();
                    $('#dlist_form3 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#dlist_form3 #minerror').focus();
                    $('#dlist_form3 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#dlist_form3 #deperror').focus();
                    $('#dlist_form3 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#dlist_form3 #other_dept').focus();
                    $('#dlist_form3 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#dlist_form3 #smierror').focus();
                    $('#dlist_form3 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#dlist_form3 #state_error').focus();
                    $('#dlist_form3 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#dlist_form3 #org_error').focus();
                    $('#dlist_form3 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#dlist_form3 #ca_design').prop("readonly", false);
                    $('#dlist_form3 #ca_design').focus();
                    $('#dlist_form3 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#dlist_form3 #hod_name').prop("readonly", false);
                    $('#dlist_form3 #hod_name').focus();
                    $('#dlist_form3 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#dlist_form3 #hod_mobile').focus();
                    $('#dlist_form3 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#dlist_form3 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#dlist_form3 #hod_tel').prop("readonly", false);
                    $('#dlist_form3 #hod_tel').focus();
                    $('#dlist_form3 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #hodtel_error').html("");
                }

                // profile 20th march
                if (!error_flag) {
                    $("#dlist_form3 :disabled").removeAttr('disabled');
                    $('#dlist_form3 #dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dlist_form3 #dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#dlist_form3 #dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {

                    if ($('#dlist_form3 #tnc').is(":checked"))
                    {

                        $('#dlist_form3 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        if (jsonvalue.form_details.min == "External Affairs" && jsonvalue.form_details.dept == "Indian Council for Cultural Relations(ICCR)")
                        {
                            $('#dlist_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#dlist_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#dlist_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);


                        } else if (jsonvalue.form_details.min == "External Affairs")
                        {
                            $('#dlist_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
                            $('#dlist_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
                            $('#dlist_form_confirm #fill_hod_mobile').html("+919384664224");
                        } else {
                            $('#dlist_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#dlist_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#dlist_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        }

                        $('#dlist_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#dlist_form3 #dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#dlist_form3 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#dlist_form3 #dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        //$('#dlist_form3 #dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        // added for disable 21 dec
                        $("#dlist_form3 #tnc").removeAttr('disabled');
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });


    });


    $('#dlist_form_confirm #confirmYes').click(function () {
        $('#dlist_form3').submit();
        $('#stack3').modal('toggle');
    });

    $('#dlist_form3').submit(function (e) {
        e.preventDefault();
       
        $("#dlist_form3 :disabled").removeAttr('disabled');
        $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dlist_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dlist_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#dlist_form3').serializeObject());
        $('#dlist_preview_tab #user_email').prop('disabled', 'true');

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "dlist_tab3",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom}, // line added by pr on 22ndjan18
            datatype: JSON,
            success: function (data)
            {

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.list_name_error !== null && jsonvalue.error.list_name_error !== "" && jsonvalue.error.list_name_error !== undefined)
                {
                    $('#dlist_form3 #list_name_err').html(jsonvalue.error.list_name_error);
                    $('#dlist_form3 #list_name_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #list_name_err').html("");
                }


                if (jsonvalue.error.list_desc_error !== null && jsonvalue.error.list_desc_error !== "" && jsonvalue.error.list_desc_error !== undefined)
                {
                    $('#dlist_form3 #description_list_err').html(jsonvalue.error.list_desc_error);
                    $('#dlist_form3 #description_list_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #description_list_err').html("");
                }


                if (jsonvalue.error.list_mod_error !== null && jsonvalue.error.list_mod_error !== "" && jsonvalue.error.list_mod_error !== undefined)
                {
                    $('#dlist_form3 #list_mod_err').html(jsonvalue.error.list_mod_error);
                    $('#dlist_form3 #list_mod_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #list_mod_err').html("");
                }



                if (jsonvalue.error.list_allowed_error !== null && jsonvalue.error.list_allowed_error !== "" && jsonvalue.error.list_allowed_error !== undefined)
                {
                    $('#dlist_form3 #allowed_member_err').html(jsonvalue.error.list_allowed_error);
                    $('#dlist_form3 #allowed_member_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #allowed_member_err').html("");
                }

                if (jsonvalue.error.list_temp_error !== null && jsonvalue.error.list_temp_error !== "" && jsonvalue.error.list_temp_error !== undefined)
                {
                    $('#dlist_form3 #list_temp_err').html(jsonvalue.error.list_temp_error);
                    $('#dlist_form3 #list_temp_err').focus();

                    error_flag = false;
                } else {
                    $('#dlist_form3 #list_temp_err').html("");
                }


                if (jsonvalue.error.list_validdate_error !== null && jsonvalue.error.list_validdate_error !== "" && jsonvalue.error.list_validdate_error !== undefined)
                {
                    $('#dlist_form3 #validity_date_err').html(jsonvalue.error.list_validdate_error);
                    $('#dlist_form3 #validity_date_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #validity_date_err').html("");
                }

                if (jsonvalue.error.list_nonnicnet_error !== null && jsonvalue.error.list_nonnicnet_error !== "" && jsonvalue.error.list_nonnicnet_error !== undefined)
                {
                    $('#dlist_form3 #non_nicnet_err').html(jsonvalue.error.list_nonnicnet_error);
                    $('#dlist_form3 #non_nicnet_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #non_nicnet_err').html("");
                }

                if (jsonvalue.error.mod_name_error !== null && jsonvalue.error.mod_name_error !== "" && jsonvalue.error.mod_name_error !== undefined)
                {
                    $('#dlist_form3 #tauth_off_name_err').html(jsonvalue.error.mod_name_error);
                    $('#dlist_form3 #tauth_off_name_err').focus();

                    error_flag = false;
                } else {
                    $('#dlist_form3 #tauth_off_name_err').html("");
                }


                if (jsonvalue.error.mod_email_error !== null && jsonvalue.error.mod_email_error !== "" && jsonvalue.error.mod_email_error !== undefined)
                {
                    $('#dlist_form3 #tauth_email_err').html(jsonvalue.error.mod_email_error);
                    $('#dlist_form3 #tauth_email_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #tauth_email_err').html("");
                }

                if (jsonvalue.error.mod_mobile_error !== null && jsonvalue.error.mod_mobile_error !== "" && jsonvalue.error.mod_mobile_error !== undefined)
                {
                    $('#dlist_form3 #tmobile_err').html(jsonvalue.error.mod_mobile_error);
                    $('#dlist_form3 #tmobile_err').focus();
                    error_flag = false;
                } else {
                    $('#dlist_form3 #tmobile_err').html("");
                }

                // profile 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#dlist_form3 #useremployment_error').focus();
                    $('#dlist_form3 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#dlist_form3 #minerror').focus();
                    $('#dlist_form3 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#dlist_form3 #deperror').focus();
                    $('#dlist_form3 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#dlist_form3 #other_dept').focus();
                    $('#dlist_form3 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#dlist_form3 #smierror').focus();
                    $('#dlist_form3 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#dlist_form3 #state_error').focus();
                    $('#dlist_form3 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#dlist_form3 #org_error').focus();
                    $('#dlist_form3 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#dlist_form3 #ca_design').prop("readonly", false);
                    $('#dlist_form3 #ca_design').focus();
                    $('#dlist_form3 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#dlist_form3 #hod_name').prop("readonly", false);
                    $('#dlist_form3 #hod_name').focus();
                    $('#dlist_form3 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#dlist_form3 #hod_mobile').focus();
                    $('#dlist_form3 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#dlist_form3 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#dlist_form3 #hod_tel').prop("readonly", false);
                    $('#dlist_form3 #hod_tel').focus();
                    $('#dlist_form3 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#dlist_form3 #hodtel_error').html("");
                }

                // profile 20th march

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18
                if (error_flag) {
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'distributionlist'},
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

    $(document).on('click', '#distribution_preview .edit', function () {
        //$(document).on('click', '#distribution_preview .edit', function () {
        // $('#distribution_preview .edit').click(function () {


        var employment = $('#dlist_preview_tab #user_employment').val();
        var min = $('#dlist_preview_tab #min').val();
        var dept = $('#dlist_preview_tab #dept').val();
        var statecode = $('#dlist_preview_tab #stateCode').val();
        var Smi = $('#dlist_preview_tab #Smi').val();
        var Org = $('#dlist_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dlist_preview_tab #min');
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
                var select = $('#dlist_preview_tab #dept');
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
                $('#dlist_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dlist_preview_tab #stateCode');
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
                var select = $('#dlist_preview_tab #Smi');
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
                $('#dlist_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dlist_preview_tab #Org');
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
                $('#dlist_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#dlist_preview_tab #central_div').hide();
            $('#dlist_preview_tab #state_div').hide();
            $('#dlist_preview_tab #other_div').hide();
        }

        $('#dlist_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#dlist_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {

            $("#distribution_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18

            $("#distribution_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#distribution_preview #confirm', function () {
        //$('#distribution_preview #confirm').click(function () {
        $('#distribution_preview').submit();
        // $('#distribution_preview_form').modal('toggle');
    });

    $('#distribution_preview').submit(function (e) {
        e.preventDefault();

        $("#distribution_preview :disabled").removeAttr('disabled');
        $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#dlist_preview_tab #user_email').removeAttr('disabled');// 20th march  

        var data = JSON.stringify($('#distribution_preview').serializeObject());
        $('#dlist_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18        

        $.ajax({
            type: "POST",
            url: "dlist_tab3",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18                

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.list_name_error !== null && jsonvalue.error.list_name_error !== "" && jsonvalue.error.list_name_error !== undefined)
                {
                    $('#distribution_preview #list_name_err').html(jsonvalue.error.list_name_error);
                    $('#distribution_preview #list_name_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #list_name_err').html("");
                }


                if (jsonvalue.error.list_desc_error !== null && jsonvalue.error.list_desc_error !== "" && jsonvalue.error.list_desc_error !== undefined)
                {
                    $('#distribution_preview #description_list_err').html(jsonvalue.error.list_desc_error);
                    $('#distribution_preview #description_list_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #description_list_err').html("");
                }


                if (jsonvalue.error.list_mod_error !== null && jsonvalue.error.list_mod_error !== "" && jsonvalue.error.list_mod_error !== undefined)
                {
                    $('#distribution_preview #list_mod_err').html(jsonvalue.error.list_mod_error);
                    $('#distribution_preview #list_mod_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #list_mod_err').html("");
                }



                if (jsonvalue.error.list_allowed_error !== null && jsonvalue.error.list_allowed_error !== "" && jsonvalue.error.list_allowed_error !== undefined)
                {
                    $('#distribution_preview #allowed_member_err').html(jsonvalue.error.list_allowed_error);
                    $('#distribution_preview #allowed_member_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #allowed_member_err').html("");
                }

                if (jsonvalue.error.list_temp_error !== null && jsonvalue.error.list_temp_error !== "" && jsonvalue.error.list_temp_error !== undefined)
                {
                    $('#distribution_preview #list_temp_err').html(jsonvalue.error.list_temp_error);
                    $('#distribution_preview #list_temp_err').focus();

                    error_flag = false;
                } else {
                    $('#distribution_preview #list_temp_err').html("");
                }


                if (jsonvalue.error.list_validdate_error !== null && jsonvalue.error.list_validdate_error !== "" && jsonvalue.error.list_validdate_error !== undefined)
                {
                    $('#distribution_preview #validity_date_err').html(jsonvalue.error.list_validdate_error);
                    $('#distribution_preview #validity_date_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #validity_date_err').html("");
                }

                if (jsonvalue.error.list_nonnicnet_error !== null && jsonvalue.error.list_nonnicnet_error !== "" && jsonvalue.error.list_nonnicnet_error !== undefined)
                {
                    $('#distribution_preview #non_nicnet_err').html(jsonvalue.error.list_nonnicnet_error);
                    $('#distribution_preview #non_nicnet_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #non_nicnet_err').html("");
                }

                if (jsonvalue.error.mod_name_error !== null && jsonvalue.error.mod_name_error !== "" && jsonvalue.error.mod_name_error !== undefined)
                {
                    $('#distribution_preview #tauth_off_name_err').html(jsonvalue.error.mod_name_error);
                    $('#distribution_preview #tauth_off_name_err').focus();

                    error_flag = false;
                } else {
                    $('#distribution_preview #tauth_off_name_err').html("");
                }


                if (jsonvalue.error.mod_email_error !== null && jsonvalue.error.mod_email_error !== "" && jsonvalue.error.mod_email_error !== undefined)
                {
                    $('#distribution_preview #tauth_email_err').html(jsonvalue.error.mod_email_error);
                    $('#distribution_preview #tauth_email_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #tauth_email_err').html("");
                }

                if (jsonvalue.error.mod_mobile_error !== null && jsonvalue.error.mod_mobile_error !== "" && jsonvalue.error.mod_mobile_error !== undefined)
                {
                    $('#distribution_preview #tmobile_err').html(jsonvalue.error.mod_mobile_error);
                    $('#distribution_preview #tmobile_err').focus();
                    error_flag = false;
                } else {
                    $('#distribution_preview #tmobile_err').html("");
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
                    $('#distribution_preview #useremployment_error').focus();
                    $('#distribution_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#distribution_preview #minerror').focus();
                    $('#distribution_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#distribution_preview #deperror').focus();
                    $('#distribution_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#distribution_preview #other_dept').focus();
                    $('#distribution_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#distribution_preview #smierror').focus();
                    $('#distribution_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#distribution_preview #state_error').focus();
                    $('#distribution_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#distribution_preview #org_error').focus();
                    $('#distribution_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#distribution_preview #org_error').html("");
                }
                // profile 20th march 


                if (!error_flag)
                {

                    $("#distribution_preview :disabled").removeAttr('disabled');
                    $('#dlist_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dlist_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#distribution_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });

    });

});
var frm_val = true;
function singleModeratorDataEditFetch1(i) {
    $.ajax({
        type: 'post',
        url: 'singleModeratorDataFetch',
        data: {bulkDataId: i},
        success: function (data) {

            console.log("INSIDE SINGLE singleModeratorDataEditFetch1")
            var form_dom = "";
            $("#domid1").val(i);
            singleEditPopup1(data.dlistData);
            $("#dynamicUploadEditSingle1").modal('show');
        }
    });
}

function singleEditPopup1(data) {
    var req_other_add = $('input[name="req_other_add"]:checked').val();
    var req_ = $('input[name="req_"]:checked').val();
    var form_dom = "";
    frm_val = false;
    $.each(data, function (i, v) {
        $("#" + i).val(v);
    })
    console.log(data)

    return form_dom;
}

$("#singleDataEditBtnFetch1").click(function () {
    $('.loader').show();
    var dataObj = {dlistData: $('#singleDomEdit1').serializeObject()};
    var data = JSON.stringify(dataObj);
    console.log("dataBeforeUPDATE ::::;" + data);
    $.ajax({
        type: 'post',
        url: 'singleModeratorDataEditPost',
        data: data,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.bulkData.dnsSingleData.length > 0) {
                singleRecordTbl1(data.bulkData.dnsSingleData);
                $(".dns_sigerrorMessage").html("");
                $(".dns_sigerrorMessage").removeClass('alert alert-danger');
                $("#dynamicUploadEditSingle1").modal('hide');
            }
        },
        complete: function () {
            $('.loader').hide();
        }
    })
});


$("#singleDataEditBtnFetch2").click(function () {
    $('.loader').show();
    var dataObj = {dlistData1: $('#singleDomEdit2').serializeObject()};
    var data = JSON.stringify(dataObj);
    $.ajax({
        type: 'post',
        url: 'singleOwnerDataEditPost',
        data: data,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.bulkData.dnsSingleData.length > 0) {
                singleRecordTbl2(data.bulkData.dnsSingleData);
                // if(data.dnsEditData.errorMessage) {
                //  $(".dns_sigerrorMessage").html(data.dnsEditData.errorMessage);
                //  $(".dns_sigerrorMessage").addClass('alert alert-danger');
                // } else{
                $(".dns_sigerrorMessage").html("");
                $(".dns_sigerrorMessage").removeClass('alert alert-danger');
                $("#dynamicUploadEditSingle2").modal('hide');
                // }
            }
        },
        complete: function () {
            $('.loader').hide();
        }
    })
});



function singleModeratorDataDeletePost1(i) {
    bootbox.confirm({
        title: "Discard Moderator",
        message: "Are you sure? Do you want to discard this Record?",
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
            if (result) {
                $.ajax({
                    type: 'post',
                    url: 'singleModeratorDataDeletePost',
                    data: {bulkDataId: i},
                    success: function (data) {
                        singleRecordTbl1(data.bulkData.dnsSingleData);
                    }
                });
            }
        }
    });
}



function singleOwnerDataDeletePost2(i) {
    bootbox.confirm({
        title: "Discard Owner",
        message: "Are you sure? Do you want to discard this Record?",
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
            if (result) {
                $.ajax({
                    type: 'post',
                    url: 'singleOwnerDataDeletePost',
                    data: {bulkDataId: i},
                    success: function (data) {
                        singleRecordTbl2(data.bulkData.dnsSingleData);
                    }
                });
            }
        }
    });
}


var frm_val = true;
function singleOwnerDataEditFetch2(i) {
    $.ajax({
        type: 'post',
        url: 'singleOwnerDataFetch',
        data: {bulkDataId: i},
        success: function (data) {

            console.log("INSIDE SINGLE singleOwnerDataEditFetch2")
            $("#domid2").val(i);
            singleEditPopup2(data.dlistData1);
            $("#dynamicUploadEditSingle2").modal('show');
        }
    });
}


function singleEditPopup2(data) {
    $.each(data, function (i, v) {
        $("#" + i).val(v);
    })
}

$("#tauth_off_name_err").hide();
$("#tauth_email_err").hide();
$("#tmobile_err").hide();

$("#auth_owner_name1_err").hide();
$("#owner_email1_err").hide();
$("#owner_mobile1_err").hide();

$("#auth_off_name").focusout(function () {
    checkName();
})

$("#auth_email").focusout(function () {
    checkEmail();
})

$("#mobile").focusout(function () {
    checkMobile();
})

$("#auth_owner_name1").focusout(function () {
    checkOwnerName();
})

$("#owner_email1").focusout(function () {
    checkOwnerEmail();
})

$("#owner_mobile1").focusout(function () {
    checkOwnerMobile();
})

function checkName() {
    if ($("#auth_off_name").val().length < 2) {
        $("#tauth_off_name_err").html("Enter Valid Name.");
        $("#tauth_off_name_err").show();
        error_name = false;
    } else {
        $("#tauth_off_name_err").hide();
        error_name = false;
    }
}

function checkEmail() {
    var re = /^\w+([-+.'][^\s]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!re.test($("#auth_email").val())) {
        $("#tauth_email_err").html("Enter Valid Email Address.");
        $("#tauth_email_err").show();
        error_name = true;
    } else {
        $("#tauth_email_err").hide();
        error_name = false;
    }
}

function checkMobile() {
    var pattern = new RegExp(/^[0-9-+]+$/);

    if (!pattern.test($("#mobile").val()) || $("#mobile").val().length < 10 ||  $("#mobile").val().length > 13) {
        $("#tmobile_err").html("Please Enter valid Contact Number. Do not Use (+91)");
        $("#tmobile_err").show();
        mobile_err = true;
    } else {
        $("#tmobile_err").hide();
        mobile_err = false;
    }
}

function checkOwnerMobile() {
    var pattern = new RegExp(/^[0-9-+]+$/);
    if (!pattern.test($("#owner_mobile1").val()) || $("#owner_mobile1").val().length < 10 || $("#owner_mobile1").val().length > 13) {
        $("#owner_mobile1_err").html("Please Enter valid Contact Number. Do not Use (+91)");
        $("#owner_mobile1_err").show();
        mobile_err = true;
    } else {
        $("#owner_mobile1_err").hide();
        mobile_err = false;
    }
}

function checkOwnerName() {
    if ($("#auth_owner_name1").val().length < 2) {
        $("#auth_owner_name1_err").html("Enter Valid Owner Name.");
        $("#auth_owner_name1_err").show();
        error_name = false;
    } else {
        $("#auth_owner_name1_err").hide();
        error_name = false;
    }
}

function checkOwnerEmail() {
    var re = /^\w+([-+.'][^\s]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
    if (!re.test($("#owner_email1").val())) {
        $("#owner_email1_err").html("Enter Valid Owner Email Address.");
        $("#owner_email1_err").show();
        error_name = true;
    } else {
        $("#owner_email1_err").hide();
        error_name = false;
    }
}


//function validateModerate() {
//        // var = 
//    }

function singleRecordTbl1(data) {
    var add_data = "";
    $("#moderator_collection").removeClass("d-none")
    if (data.length > 0) {
        console.log("inside singleRecordTbl If block ::::::::::::");
        console.log(data);
        $.each(data, function (k, val) {
            add_data += fetchSingleRecords1(val, k);
        })

        $("#moderator_collection_tbl tbody").html("");
        $("#moderator_collection_tbl tbody").html(add_data);

    } else {
        $("#moderator_collection").addClass("d-none")
        $("#moderator_collection_tbl thead").html("");
        $("#moderator_collection_tbl tbody").html("");
    }
}

function singleRecordTbl2(data) {
    var add_data = "";
    $("#owner_collection").removeClass("d-none")
    if (data.length > 0) {
        console.log("inside singleRecordTbl If block ::::::::::::");
        console.log(data);
        $.each(data, function (k, val) {
            add_data += fetchSingleRecords2(val, k);
        })

        $("#owner_collection_tbl tbody").html("");
        $("#owner_collection_tbl tbody").html(add_data);

    } else {
        $("#owner_collection").addClass("d-none")
        $("#owner_collection_tbl thead").html("");
        $("#owner_collection_tbl tbody").html("");
    }
}

function moderatorRecordTbl(data) {
    var add_data = "";
    $("#moderator_collection").removeClass("d-none")
    if (data.length > 0) {
        console.log("inside singleRecordTbl If block ::::::::::::");
        console.log(data);
        $.each(data, function (k, val) {
            add_data += previewModeratorTbl(val, k);
        })

        $("#moderator_collection_tbl tbody").html("");
        $("#moderator_collection_tbl tbody").html(add_data);

    } else {
        $("#moderator_collection").addClass("d-none")
        $("#moderator_collection_tbl thead").html("");
        $("#moderator_collection_tbl tbody").html("");
    }
}

function ownerRecordTbl(data) {
    var add_data = "";
    $("#owner_collection").removeClass("d-none")
    if (data.length > 0) {
        console.log("inside singleRecordTbl If block ::::::::::::");
        console.log(data);
        $.each(data, function (k, val) {
            add_data += previewOwnerTbl(val, k);
        })

        $("#owner_collection_tbl tbody").html("");
        $("#owner_collection_tbl tbody").html(add_data);

    } else {
        $("#owner_collection").addClass("d-none")
        $("#owner_collection_tbl thead").html("");
        $("#owner_collection_tbl tbody").html("");
    }
}

function fetchSingleRecords1(data, i) {
    i++;
    var add_data = "";

    if (data !== null) {
        add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.t_off_name + `</td><td>` + data.tauth_email + `</td><td>` + data.tmobile + `</td>`;
        add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleModeratorDataEditFetch1(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleModeratorDataDeletePost1(` + data.id + `)">Delete</a></li>
                </ul>
            </td></tr>`;
    }

    return add_data;
}

function fetchSingleRecords2(data, i) {
    i++;
    var add_data = "";

    if (data !== null) {
        add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.owner_name + `</td><td>` + data.owner_email + `</td><td>` + data.owner_mobile + `</td>`;
        add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
               
                    <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleOwnerDataEditFetch2(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleOwnerDataDeletePost2(` + data.id + `)">Delete</a></li>
                </ul>
              
            </td></tr>`;
    }
    return add_data;
}

function previewModeratorTbl(data, i) {
    i++;
    var add_data = "";

    if (data !== null) {
        add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.name + `</td><td>` + data.email + `</td><td>` + data.mobile + `</td>`;
        add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down" disabled></i></button>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleModeratorDataEditFetch1(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleModeratorDataDeletePost1(` + data.id + `)">Delete</a></li>
                </ul>
            </td></tr>`;
    }

    return add_data;
}

function previewOwnerTbl(data, i) {
    i++;
    var add_data = "";

    if (data !== null) {
        add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.name + `</td><td>` + data.email + `</td><td>` + data.mobile + `</td>`;
        add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown" disabled >Action&ensp;<i class="fa fa-angle-down"></i></button>
               
                    <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleOwnerDataEditFetch2(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleOwnerDataDeletePost2(` + data.id + `)">Delete</a></li>
                </ul>
              
            </td></tr>`;
    }
    return add_data;
}


function fetchSingleRecords1(data, i) {
    i++;
    var add_data = "";

    if (data !== null) {
        add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.t_off_name + `</td><td>` + data.tauth_email + `</td><td>` + data.tmobile + `</td>`;
        add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
                <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleModeratorDataEditFetch1(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleModeratorDataDeletePost1(` + data.id + `)">Delete</a></li>
                </ul>
            </td></tr>`;
    }

    return add_data;
}

function fetchSingleRecords2(data, i) {
    i++;
    var add_data = "";

    if (data !== null) {
        add_data += `<tr><td class='text-center'>` + i + `</td><td>` + data.owner_name + `</td><td>` + data.owner_email + `</td><td>` + data.owner_mobile + `</td>`;
        add_data += `<td class='text-center' width="15%">
                <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
               
                    <ul class="dropdown-menu">
                    <li><a href="javascript:void(0);" onclick="singleOwnerDataEditFetch2(` + data.id + `)">Edit</a></li>
                    <li><a href="javascript:void(0);" onclick="singleOwnerDataDeletePost2(` + data.id + `)">Delete</a></li>
                </ul>
              
            </td></tr>`;
    }
    return add_data;
}

//var frm_val = true;
//function singleModeratorDataEditFetchPreview(i) {
//    $.ajax({
//        type: 'post',
//        url: 'singleModeratorDataFetch',
//        data: {bulkDataId: i},
//        success: function (data) {
//
//            console.log("INSIDE SINGLE singleModeratorDataEditFetch1")
//            var form_dom = "";
//            $("#domid1").val(i);
//            singleEditPopup1(data.dlistData);
//            $("#dynamicUploadEditSingle1").modal('show');
//        }
//    });
//}

   $('#owner_admin').click(function (e) {
        var isChecked = $(this).is(":checked");
        if (isChecked) {
            $.ajax({
                type: "POST",
                url: "fill_dlist_owner_tab",
                success: function (data) {
                    var myJSON = JSON.stringify(data);
                    var jsonvalue = JSON.parse(myJSON);

                    $('#dlist_form2 #auth_owner_name1').val(jsonvalue.profile_values.cn);
                    $('#dlist_form2 #owner_email1').val(jsonvalue.profile_values.email);
                    $('#dlist_form2 #owner_mobile1').val(jsonvalue.profile_values.mobile);
                },
                error: function () {
                    //alert("please Try after some time");
                    console.log('error');
                }
            });
        } else {
            $('#dlist_form2')[0].reset();
        }
    });