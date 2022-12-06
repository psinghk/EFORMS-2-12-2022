/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function () {
    var dt = new Date;
    //var yrg = dt.getFullYear() - 66;

    var yrg = dt.getFullYear() - 67; // line modified by pr on 2ndaug18

    var yrgf = dt.getFullYear() - 18;
    var initDate = new Date();
    initDate.setFullYear(initDate.getFullYear() - 18);
    initDate.setMonth(1 - 1);
    initDate.setDate(1);
    $(function () {


        $(document).on("focusin", "#single_dob,#email_single_dob,#single_dob1,#single_dob2,#single_dob3,#single_dob4,#single_dob5,#single_dob6,#single_dob7", function () {

            $("#single_dob,#email_single_dob,#single_dob1,#single_dob2,#single_dob3,#single_dob4,#single_dob5,#single_dob6,#single_dob7").datepicker({
                yearRange: yrg + ":" + yrgf,
                changeMonth: true,
                changeYear: true,
                dateFormat: "dd-mm-yy",
                defaultDate: initDate,
                onSelect: function (selected, evnt) {

                    var yrgr = dt.getFullYear();

                    //var yrgfr = dt.getFullYear() + 48;

                    var yrgfr = dt.getFullYear() + 49; // line modified by pr on 2ndaug18 to add in the current year to make it 67

                    var initrDate = new Date();
                    initrDate.setFullYear(initrDate.getFullYear());
                    initrDate.setMonth(1 + 1);
                    initrDate.setDate(1);


                    var date = selected.substring(0, 2);
                    var month = selected.substring(3, 5);
                    var year = selected.substring(6, 10);
                    var dateToCompare = new Date(year, month - 1, date);
                    var yrgr = dt.getFullYear();
                    //var yrgfr = dateToCompare.getFullYear() + 66;

                    var yrgfr = dateToCompare.getFullYear() + 67; // line modified by pr on 2ndaug18

                    $("#single_dor").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor1").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor1").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor2").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor2").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor3").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor3").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor4").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor4").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor5").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor5").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor6").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor6").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});
                    $("#single_dor7").datepicker("option", "yearRange", yrgr + ":" + yrgfr);
                    $("#single_dor7").datepicker({minDate: "+0", yearRange: yrgr + ":" + yrgfr, changeMonth: true, changeYear: true, dateFormat: "dd-mm-yy", defaultDate: initrDate});

                }});
        });
    });
//    $("#single_dob,#single_dob1,#single_dob2,#single_dob3").datepicker({
//        dateFormat: "dd-mm-yy", changeMonth: true, changeYear: true, yearRange: "-80:-10", minDate: "-80Y", maxDate: "-18Y"
//    });
    $(document).on("focusin", "#single_dor,#single_dor1,#single_dor2,#single_dor3,#single_dor4,#single_dor5,#single_dor6,#email_single_dor,#email_act_dor,#email_single_dor1,#email_single_dor2,#single_dor7,#date_annum,#last_email_single_dor", function () {

        console.log("sahil dateee");


        $("#single_dor,#single_dor1,#single_dor2,#single_dor3,#single_dor4,#single_dor5,#single_dor6,#email_single_dor,#email_act_dor,#email_single_dor1,#single_dor7,#email_single_dor2,#last_email_single_dor,#date_annum").datepicker({
            // start, code added by pr on 21stjan19
            beforeShow: function (input, inst) {
                $(document).off("focusin.bs.modal");
            },
            onClose: function () {
                $(document).on("focusin.bs.modal");
            },
            // end, code added by pr on 21stjan19
            dateFormat: "dd-mm-yy",
            changeMonth: true,
            changeYear: true,
            //yearRange: "+0:+48", 
            yearRange: "+0:+49", // line modified by pr on 2ndaug18 to have dor as 67
            minDate: "+1D",
            maxDate: "+50Y"
        });
    });

    $(document).on('find click', '.page-wrapper button', function () {
        var field_name = $(this).attr('name');
        // wifi form        

        if (field_name === 'download') {
            var data = $(this).val();
            window.location = "download_2?uploaded_filename=" + data;
        }
    });

    $(document).on('find click', '.page-wrapper input:radio', function () {
        var req_user_type = $("input[name='req_user_type']:checked").val();
        var field_form = $(this).closest('form').attr('id');
        var radioValue = $("input[name='form_name']:checked").val();
        if (req_user_type === "self")
        {
            $('#' + field_form + ' #info_other_user').addClass('display-hide');
            $('#' + field_form + ' #div_for_self').removeClass('display-hide');
            $('#' + field_form + ' #div_for_other').addClass('display-hide');
            $('#' + field_form + ' #single_emp_type1').attr('checked', 'checked');
            $('#' + field_form + ' #single_id_type_1').attr('checked', 'checked');
            $('#' + field_form + ' #mail_1').attr('checked', 'checked');
            $('#' + field_form + ' #req_user_type_err').html("")

        } else if (req_user_type === "other_user")
        {

            $('#' + field_form + ' #info_other_user').removeClass('display-hide');
            $('#' + field_form + ' #div_for_self').addClass('display-hide');
            $('#' + field_form + ' #div_for_other').removeClass('display-hide');
            $('#' + field_form + ' #single_emp_type1').attr('checked', 'checked');
            $('#' + field_form + ' #single_id_type_1').attr('checked', 'checked');
            $('#' + field_form + ' #mail').attr('checked', 'checked');
        }

        var single_emp_type = $("input[name='single_emp_type']:checked").val();
        var account_expiry = $("input[name='single_emp_type']:checked").val();
        $('#single_user_form').addClass('display-hide');
        $('#single_form_confirm').addClass('display-hide');
        $('#bulk_user_form').addClass('display-hide');
        $('#bulk_form_confirm').addClass('display-hide');
        $('#gem_user_form').addClass('display-hide');
        $('#gem_form_confirm').addClass('display-hide');
        $('#nkn_single_form').addClass('display-hide');
        $('#nkn_form_confirm').addClass('display-hide');
        $('#nkn_bulk_form').addClass('display-hide');
        $("#email_act_div").addClass('display-hide');
        $("#email_deact_div").addClass('display-hide');
        $("#email_single_doreact_div").addClass('display-hide');
        $("#dor_ext_div").addClass('display-hide');

        // wifi form         
        if (radioValue === 'single_form') {
            $('#tab2').show();
            $('#tab1').hide();
            $('#email_tab1_nav').removeClass('active');
            $('#email_tab1_nav').addClass('done');
            //$('.progress-bar').css("width", "100%");
            $('#email_tab2_nav').addClass('active');
            $('#single_user_form').removeClass('display-hide');
            $('#single_form_confirm').removeClass('display-hide');

        } else if (radioValue === 'bulk_form') {

            $('#tab2').show();
            $('#tab1').hide();
            $('#email_tab1_nav').removeClass('active');
            $('#email_tab1_nav').addClass('done');
            //$('.progress-bar').css("width", "100%");
            $('#email_tab2_nav').addClass('active');
            $('#bulk_user_form').removeClass('display-hide');
            $('#bulk_form_confirm').removeClass('display-hide');
        } else if (radioValue === 'gem_form') {
            $('#tab2').show();
            $('#tab1').hide();
            $('#email_tab1_nav').removeClass('active');
            $('#email_tab1_nav').addClass('done');
            //$('.progress-bar').css("width", "100%");
            $('#email_tab2_nav').addClass('active');
            $('#gem_user_form').removeClass('display-hide');
            $('#gem_form_confirm').removeClass('display-hide');

        } else if (radioValue === 'nkn_single_form') {
            $('#tab2').show();
            $('#tab1').hide();
            $('#email_tab1_nav').removeClass('active');
            $('#email_tab1_nav').addClass('done');
            // $('.progress-bar').css("width", "100%");
            $('#email_tab2_nav').addClass('active');
            $('#nkn_single_form').removeClass('display-hide');
            $('#nkn_form_confirm').removeClass('display-hide');
        } else if (radioValue === 'nkn_bulk_form') {
            $('#tab2').show();
            $('#tab1').hide();
            $('#email_tab1_nav').removeClass('active');
            $('#email_tab1_nav').addClass('done');
            // $('.progress-bar').css("width", "100%");
            $('#email_tab2_nav').addClass('active');
            $('#nkn_bulk_form').removeClass('display-hide');
            $('#nkn_form_confirm').removeClass('display-hide');

        } else if (radioValue === 'email_act') {
            $("#email_act_div").removeClass('display-hide');
            $("#email_deact_div").addClass('display-hide');
            $("#single_user_form").addClass('display-hide');
            $("#bulk_user_form").addClass('display-hide');
            $("#nkn_single_form").addClass('display-hide');
            $("#nkn_bulk_form").addClass('display-hide');
            $("#gem_user_form").addClass('display-hide');
               $("#dor_ext_div").addClass('display-hide');
            $("#dor_ext_confirm").addClass('display-hide');
        } else if (radioValue === 'dor_ext') {
            // alert("dor_ext");
            if (account_expiry === "serving_employee") {
                $("#" + field_form + ' #cert_div').removeClass("display-hide");
                //  alert("dor_ext");

                $("#" + field_form + ' #dor_text').html("Date Of Account Expiry");


            } else {

                $("#" + field_form + ' #cert_div').removeClass("display-hide");
                //  alert("dor_ext");

//            $("#" + field_form + ' #dor_text').html("Date Of Retirement");

            }
            $("#dor_ext_div").removeClass('display-hide');
            $("#dor_ext_confirm").removeClass('display-hide');
            $("#email_act_div").addClass('display-hide');
            $("#email_deact_div").addClass('display-hide');
            $("#single_user_form").addClass('display-hide');
            $("#bulk_user_form").addClass('display-hide');
            $("#nkn_single_form").addClass('display-hide');
            $("#nkn_bulk_form").addClass('display-hide');
            $("#gem_user_form").addClass('display-hide');
        } else if (radioValue === 'email_deact') {
            if (single_emp_type !== "emp_regular")
            {
                $("#" + field_form + ' #cert_div').removeClass("display-hide");
                $("#" + field_form + ' #dor_text').html("Date of expiry");
            } else {
                $("#" + field_form + ' #cert_div').addClass("display-hide");
                $("#" + field_form + ' #dor_text').html("Date Of Retirement");
            }
            $("#email_deact_div").removeClass('display-hide');
            $("#email_act_div").addClass('display-hide');
            $("#single_user_form").addClass('display-hide');
            $("#bulk_user_form").addClass('display-hide');
            $("#nkn_single_form").addClass('display-hide');
            $("#nkn_bulk_form").addClass('display-hide');
            $("#gem_user_form").addClass('display-hide');
            $("#dor_ext_div").ddClass('display-hide');
            $("#dor_ext_confirm").addClass('display-hide');
        }
        if (field_form === "email_act2")
        {
            var single_emp_type = $("#" + field_form + " input[name='single_emp_type']:checked").val();
            console.log("single_emp_type" + single_emp_type)
            if (single_emp_type !== "emp_regular")
            {
                //alert("inside emp type")
                $("#" + field_form + ' #cert_div').removeClass("display-hide");
                $("#" + field_form + ' #dor_text').html("Date of expiry");
            } else {
                $("#" + field_form + ' #cert_div').addClass("display-hide");
                $("#" + field_form + ' #dor_text').html("Date Of Retirement");
            }
        }
        if (field_form === "email_act_preview")
        {
            var single_emp_type = $("#" + field_form + " input[name='single_emp_type']:checked").val();
            if (single_emp_type !== "emp_regular")
            {
                $("#" + field_form + ' #cert_div').removeClass("display-hide");
                $("#" + field_form + ' #dor_text').html("Date of expiry");
            } else {
                $("#" + field_form + ' #cert_div').addClass("display-hide");
                $("#" + field_form + ' #dor_text').html("Date Of Retirement");
            }


        }
    });

    var pse = $("#pse_service input[type=radio]:checked").val();
    if (pse === 'central_pse') {
        $.get('centralMinistry', {
            orgType: 'Central'
        }, function (response) {
            var select = $('#pse_ministry');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {
                $('<option>').val(value).text(value).appendTo(select);
            });
        });
        $('#pse_state').val('');
        $('#pse_district').val('');
    } else {
        $('#central_pse_div').addClass('display-hide');
        $('#state_pse_div').removeClass('display-hide');
        $('#pse_ministry').val('');
    }

    $('.page-container-bg-solid').find('input').click(function () {
        var field_form = $(this).closest('form').attr('id');
        var a = $('#' + field_form + " input[name=primary_user]:checked").val();
        if (a == "yes")
        {
            $('#' + field_form + ' #primary_user_err').html("");
            $('#' + field_form + ' #primary_text_id').removeClass('display-hide');


        } else {

            $('#' + field_form + ' #primary_user_err').html("");
            $('#' + field_form + ' #primary_text_id').addClass('display-hide');

        }
        var field_name = $(this).attr('name');
        if (field_name === 'pse') {
            var pseValue = $(this).val();
            if (pseValue === 'state_pse') {
                $('#' + field_form + ' #central_pse_div').addClass('display-hide');
                $('#' + field_form + ' #state_pse_div').removeClass('display-hide');
            } else {
                $('#' + field_form + ' #central_pse_div').removeClass('display-hide');
                $('#' + field_form + ' #state_pse_div').addClass('display-hide');
                $('#' + field_form + ' #pse_district').val('');
            }
        }


        if (field_name === "single_emp_type") {
            var emp_type = $('#' + field_form + " #emp_type input[type=radio]:checked").val();
            if (emp_type === 'emp_contract') {
                $('#' + field_form + ' #single_emp_type3').attr('checked', 'checked');
                $('#' + field_form + ' #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                $('#' + field_form + ' #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
            } else if (emp_type === 'consultant') {
                $('#' + field_form + ' #single_emp_type2').attr('checked', 'checked');
                $('#' + field_form + ' #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                $('#' + field_form + ' #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
            } else {
                $('#' + field_form + ' #single_emp_type1').attr('checked', 'checked');
                $('#' + field_form + ' #single_emp_type2').removeAttr('checked');
                $('#' + field_form + ' #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                $('#' + field_form + ' #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
            }
        }
        if (field_name === "single_id_type") {
            var emp_type = $('#' + field_form + " #id_based input[type=radio]:checked").val();
            if (emp_type === 'id_name') {
                $('#' + field_form + ' #single_id_type_2').removeAttr('checked');
                $('#' + field_form + ' #single_id_type_1').attr('checked', 'checked');
            } else {
                $('#' + field_form + ' #single_id_type_2').attr('checked', 'checked');
                $('#' + field_form + ' #single_id_type_1').removeAttr('checked');
            }
        }
    });

    $(document).on('find blur', '.emailcheck input', function () {
        var field_name = $(this).attr("name");
        var field_form = $(this).closest('form').attr('id');
        var flag = false;
        if (field_name === 'single_email1' || field_name === 'single_email2') {
            if (field_name === 'single_email2') {
                var a = $('#' + field_form + " input[name=single_email1]").val();
                var b = $('#' + field_form + " input[name=single_email2]").val();
                console.log("A :: " + a + " B " + b);
                if (a === b) {
                    if (field_form === 'gem_form1' || field_form === 'gem_form2') {
                        $('#' + field_form + " #gem_email2_err").text("Preferred Email2 cannot be same as Preferred Email1");
                        $('#' + field_form + " #gem_email2_err").css("color", "red");
                    } else {
                        $('#' + field_form + " #" + field_name + "_err").text("Preferred Email2 cannot be same as Preferred Email1");
                        $('#' + field_form + " #" + field_name + "_err").css("color", "red");
                    }
                    flag = true;
                }
            }
            if (field_form === "single_form1" || field_form === "single_form2" || field_form === "singleuser_preview") {
                var emp_detail = $('#' + field_form + " #emp_type input[type=radio]:checked").val();
                var id_detail = $('#' + field_form + " #id_based input[type=radio]:checked").val();
                var field_form1 = field_form + "," + emp_detail + "," + id_detail;
                console.log("Inside deepest if " + field_form);
            } else {
                var field_form1 = field_form;
            }
            console.log("Inside deepest if " + field_form);

            if (!flag) {
                checkAvailableEmail($(this).val(), "1", field_form1, field_name, field_form)
            }
        }
    });

    $(document).on('find blur', '.applicantemailcheck input', function () {
        var field_name = $(this).attr("name");
        var field_form = $(this).closest('form').attr('id');
        var flag = false;
        if (field_name === 'applicant_single_email1' || field_name === 'applicant_single_email2') {
            if (field_name === 'applicant_single_email2') {
                var a = $('#' + field_form + " input[name=applicant_single_email1]").val();
                var b = $('#' + field_form + " input[name=applicant_single_email2]").val();
                console.log("A :: " + a + " B " + b);
                if (a === b) {
                    if (field_form === 'gem_form1' || field_form === 'gem_form2') {
                        $('#' + field_form + " #gem_email2_err").text("Preferred Email2 cannot be same as Preferred Email1");
                        $('#' + field_form + " #gem_email2_err").css("color", "red");
                    } else {
                        $('#' + field_form + " #" + field_name + "_err").text("Preferred Email2 cannot be same as Preferred Email1");
                        $('#' + field_form + " #" + field_name + "_err").css("color", "red");
                    }
                    flag = true;
                }
            }
            if (field_form === "single_form1" || field_form === "single_form2" || field_form === "singleuser_preview") {
                var emp_detail = $('#' + field_form + " #emp_type input[type=radio]:checked").val();
                var id_detail = $('#' + field_form + " #id_based input[type=radio]:checked").val();
                var field_form1 = field_form + "," + emp_detail + "," + id_detail;
                console.log("Inside deepest if " + field_form);
            } else {
                var field_form1 = field_form;
            }
            console.log("Inside deepest if " + field_form);

            if (!flag) {
                checkAvailableEmail($(this).val(), "1", field_form1, field_name, field_form)
            }
        }
    });
// single user submission
    $('#single_form1').submit(function (e) {
        $(".loader").show()
        e.preventDefault();
        var data = JSON.stringify($('#single_form1').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $.ajax({
            type: "POST",
            url: "single_tab1",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
                {
                    error_flag = false;
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Your request is already pending/completed so you can not fill any more request.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
                if (jsonvalue.error.email_info_error1 !== null && jsonvalue.error.email_info_error1 !== "" && jsonvalue.error.email_info_error1 !== undefined)
                {
                    $('#req_user_type_err').html("Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further");
                    error_flag = false;
                    bootbox.dialog({
                        message: "Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further.",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined && jsonvalue.form_details.req_user_type !== "other_user")
                {
                    $('#single_form1 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#single_form1 #single_email1').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined && jsonvalue.form_details.req_user_type === "other_user")
                {
                    $('#single_form1 #applicant_single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#single_form1 #applicant_single_email1_err').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {
                    if (jsonvalue.form_details.req_user_type === "other_user")
                    {
                        if (jsonvalue.error.errorMsg1 !== undefined) {
                            if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {

                                $('#single_form1 #applicant_single_email1_err').html("")
                            } else if (jsonvalue.error.errorMsg1.indexOf("some info") > -1)
                            {
                                $('#single_form1 #applicant_single_email1_err').html("some info")
                            } else if (jsonvalue.error.errorMsg1.indexOf("There are already 3 email addresses registered against your mobile number") > -1)
                            {

                                $('#single_form1 #applicant_single_email1_err').html(jsonvalue.error.errorMsg1)
                                //  $('#single_form1 #single_email1').focus();
                                $('#single_form1 #applicant_single_email1_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            } else {
                                $('#single_form1 #applicant_single_email1_err').html(jsonvalue.error.errorMsg1)
                                //  $('#single_form1 #single_email1').focus();
                                $('#single_form1 #applicant_single_email1_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            }
                        }


                    } else {



                        if (jsonvalue.error.errorMsg1 !== undefined) {
                            if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {

                                $('#single_form1 #single_email1_err').html("")
                            } else if (jsonvalue.error.errorMsg1.indexOf("some info") > -1)
                            {
                                $('#single_form1 #single_email1_err').html("some info")
                            } else if (jsonvalue.error.errorMsg1.indexOf("There are already 3 email addresses registered against your mobile number") > -1)
                            {

                                $('#single_form1 #single_email1_err').html(jsonvalue.error.errorMsg1)
                                //  $('#single_form1 #single_email1').focus();
                                $('#single_form1 #single_email1_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            } else {
                                $('#single_form1 #single_email1_err').html(jsonvalue.error.errorMsg1)
                                //  $('#single_form1 #single_email1').focus();
                                $('#single_form1 #single_email1_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            }
                        }
                    }

                }



                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined && jsonvalue.form_details.req_user_type !== "other_user")
                {
                    $('#single_form1 #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#single_form1 #single_email2').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined && jsonvalue.form_details.req_user_type === "other_user")
                {
                    $('#single_form1 #applicant_single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#single_form1 #applicant_single_email2_err').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    if (jsonvalue.form_details.req_user_type === "other_user")
                    {
                        if (jsonvalue.error.errorMsg2 !== undefined) {
                            if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {

                                $('#single_form #applicant_single_email2_err').html("")
                            } else if (jsonvalue.error.errorMsg2.indexOf("some info") > -1)
                            {
                                $('#single_form1 #applicant_single_email2_err').html("some info")
                            } else if (jsonvalue.error.errorMsg1.indexOf("There are already 3 email addresses registered against your mobile number") > -1)
                            {
                                $('#single_form1 #applicant_single_email2_err').html(jsonvalue.error.errorMsg2)
                                //  $('#single_form1 #single_email2').focus();
                                $('#single_form1 #applicant_single_email2_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            } else {
                                $('#single_form1 #applicant_single_email2_err').html(jsonvalue.error.errorMsg2)
                                //  $('#single_form1 #single_email2').focus();
                                $('#single_form1 #applicant_single_email2_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            }
                        }


                    } else {
                        if (jsonvalue.error.errorMsg2 !== undefined) {
                            if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {

                                $('#single_form1 #single_email2_err').html("")
                            } else if (jsonvalue.error.errorMsg2.indexOf("some info") > -1)
                            {
                                $('#single_form1 #single_email1_err').html("some info")
                            } else if (jsonvalue.error.errorMsg1.indexOf("There are already 3 email addresses registered against your mobile number") > -1)
                            {
                                $('#single_form1 #single_email2_err').html(jsonvalue.error.errorMsg2)
                                //  $('#single_form1 #single_email2').focus();
                                $('#single_form1 #single_email2_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            } else {
                                $('#single_form1 #single_email2_err').html(jsonvalue.error.errorMsg2)
                                //  $('#single_form1 #single_email2').focus();
                                $('#single_form1 #single_email2_err').css("color", "red");
                                error_flag = false;
                                $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                                $('#single_form1 #imgtxt').val("");
                            }
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#single_form1 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#single_form1 #single_dob').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #single_dob_err').html("")
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#single_form1 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#single_form1 #single_dor').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #single_dor_err').html("")
                }


                if (jsonvalue.error.applicant_name_error !== null && jsonvalue.error.applicant_name_error !== "" && jsonvalue.error.applicant_name_error !== undefined)
                {
                    $('#single_form1 #applicant_name_error').html(jsonvalue.error.applicant_name_error)
                    $('#single_form1 #applicant_name_error').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #applicant_name_error').html("")
                }



                if (jsonvalue.error.applicant_mobile_error !== null && jsonvalue.error.applicant_mobile_error !== "" && jsonvalue.error.applicant_mobile_error !== undefined)
                {
                    $('#single_form1 #applicant_mobile_error').html(jsonvalue.error.applicant_mobile_error)
                    $('#single_form1 #applicant_mobile_error').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #applicant_mobile_error').html("")
                }


                if (jsonvalue.error.applicant_state_error !== null && jsonvalue.error.applicant_state_error !== "" && jsonvalue.error.applicant_state_error !== undefined)
                {
                    $('#single_form1 #applicant_state_error').html(jsonvalue.error.applicant_state_error)
                    $('#single_form1 #applicant_state_error').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #applicant_state_error').html("")
                }


                if (jsonvalue.error.applicant_email_error !== null && jsonvalue.error.applicant_email_error !== "" && jsonvalue.error.applicant_email_error !== undefined)
                {
                    $('#single_form1 #applicant_email_error').html(jsonvalue.error.applicant_email_error)
                    $('#single_form1 #applicant_email_error').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #applicant_email_error').html("")
                }



                if (jsonvalue.error.applicant_desg_error !== null && jsonvalue.error.applicant_desg_error !== "" && jsonvalue.error.applicant_desg_error !== undefined)
                {
                    $('#single_form1 #applicant_desg_error').html(jsonvalue.error.applicant_desg_error)
                    $('#single_form1 #applicant_desg_error').focus();
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {

                    $('#single_form1 #applicant_desg_error').html("")
                }
                if (jsonvalue.error.applicant_employment_error !== null && jsonvalue.error.applicant_employment_error !== "" && jsonvalue.error.applicant_employment_error !== undefined)
                {

                    $('#single_form1 #applicant_employment_error').focus();
                    $('#single_form1 #applicant_employment_error').html(jsonvalue.error.applicant_employment_error);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_employment_error').html("");
                }
                if (jsonvalue.error.applicant_minerror !== null && jsonvalue.error.applicant_minerror !== "" && jsonvalue.error.applicant_minerror !== undefined)
                {
                    $('#single_form1 #applicant_minerror').focus();
                    $('#single_form1 #applicant_minerror').html(jsonvalue.error.applicant_minerror);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_minerror').html("");
                }
                if (jsonvalue.error.applicant_dept_error !== null && jsonvalue.error.applicant_dept_error !== "" && jsonvalue.error.applicant_dept_error !== undefined)
                {
                    $('#single_form1 #applicant_dept_error').focus();
                    $('#single_form1 #applicant_dept_error').html(jsonvalue.error.applicant_dept_error);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_dept_error').html("");
                }
                if (jsonvalue.error.applicant_dept_other_error !== null && jsonvalue.error.applicant_dept_other_error !== "" && jsonvalue.error.applicant_dept_other_error !== undefined)
                {
                    $('#single_form1 #applicant_dept_other_error').focus();
                    $('#single_form1 #applicant_dept_other_error').html(jsonvalue.error.applicant_dept_other_error);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_dept_other_error').html("");
                }
                if (jsonvalue.error.applicant_state_dept_error !== null && jsonvalue.error.applicant_state_dept_error !== "" && jsonvalue.error.applicant_state_dept_error !== undefined)
                {
                    $('#single_form1 #applicant_state_dept_error').focus();
                    $('#single_form1 #applicant_state_dept_error').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_state_dept_error').html("");
                }
                if (jsonvalue.error.applicant_state_error !== null && jsonvalue.error.applicant_state_error !== "" && jsonvalue.error.applicant_state_error !== undefined)
                {
                    $('#single_form1 #applicant_state_error').focus();
                    $('#single_form1 #applicant_state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_state_error').html("");
                }
                if (jsonvalue.error.applicant_org_error !== null && jsonvalue.error.applicant_org_error !== "" && jsonvalue.error.applicant_org_error !== undefined)
                {
                    $('#single_form1 #applicant_org_error').focus();
                    $('#single_form1 #applicant_org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#single_form1 #applicant_org_error').html("");
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                } else {
                    $('#captchaerror').html("")
                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#single_form1 #imgtxt').val("");
                }


                // end, code added by pr on 22ndjan18


                if (!error_flag) {
                } else {
                    //alert(jsonvalue.profile_values.bo_check)
                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#single_form2 #central_div').show();
                        $('#single_form2 #state_div').hide();
                        $('#single_form2 #other_div').hide();
                        $('#single_form2 #other_text_div').addClass("display-hide");
                        var select = $('#single_form2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#single_form2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#single_form2 #other_text_div').removeClass("display-hide");
                            $('#single_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#single_form2 #central_div').hide();
                        $('#single_form2 #state_div').show();
                        $('#single_form2 #other_div').hide();
                        $('#single_form2 #other_text_div').addClass("display-hide");
                        var select = $('#single_form2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#single_form2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#single_form2 #other_text_div').removeClass("display-hide");
                            $('#single_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#single_form2 #central_div').hide();
                        $('#single_form2 #state_div').hide();
                        $('#single_form2 #other_div').show();
                        $('#single_form2 #other_text_div').addClass("display-hide");
                        var select = $('#single_form2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#single_form2 #other_text_div').removeClass("display-hide");
                            $('#single_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#single_form2 #central_div').hide();
                        $('#single_form2 #state_div').hide();
                        $('#single_form2 #other_div').hide();
                    }




                    //for applicant
                    // alert(jsonvalue.form_details.user_employment)
                    $('#single_form2 #user_employment').val(jsonvalue.profile_values.user_employment);

                    if (jsonvalue.form_details.applicant_employment === 'Central' || jsonvalue.form_details.applicant_employment === 'UT') {
                        $('#single_form2 #applicant_central_div').show();
                        $('#single_form2 #applicant_state_div').hide();
                        $('#single_form2 #applicant_other_div').hide();
                        $('#single_form2 #applicant_other_text_div').addClass("display-hide");
                        var select = $('#single_form2 #applicant_min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.form_details.applicant_min).text(jsonvalue.form_details.applicant_min).appendTo(select);
                        var select = $('#single_form2 #applicant_dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.form_details.applicant_dept).text(jsonvalue.form_details.applicant_dept).appendTo(select);

                        if (jsonvalue.form_details.applicant_dept.toString().toLowerCase() === 'other') {
                            $('#single_form2 #applicant_other_text_div').removeClass("display-hide");
                            $('#single_form2 #applicant_other_dept').val(jsonvalue.form_details.applicant_other_dept);
                        }


                    } else if (jsonvalue.form_details.applicant_employment === 'State') {



                        $('#single_form2 #applicant_central_div').hide();
                        $('#single_form2 #applicant_state_div').show();
                        $('#single_form2 #applicant_other_div').hide();
                        $('#single_form2 #applicant_other_text_div').addClass("display-hide");
                        var select = $('#single_form2 #applicant_stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.form_details.applicant_stateCode).text(jsonvalue.form_details.applicant_stateCode).appendTo(select);
                        var select = $('#single_form2 #applicant_Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.form_details.applicant_Smi).text(jsonvalue.form_details.applicant_Smi).appendTo(select);

                        if (jsonvalue.form_details.applicant_Smi.toString().toLowerCase() === 'other') {
                            $('#single_form2 #other_text_div').removeClass("display-hide");
                            $('#single_form2 #other_dept').val(jsonvalue.form_details.applicant_other_dept);
                        }

                    } else if (jsonvalue.form_details.applicant_employment === 'Others' || jsonvalue.form_details.applicant_employment === "Psu" || jsonvalue.form_details.applicant_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#single_form2 #applicant_central_div').hide();
                        $('#single_form2 #applicant_state_div').hide();
                        $('#single_form2 #applicant_other_div').show();
                        $('#single_form2 #applicant_other_text_div').addClass("display-hide");
                        var select = $('#single_form2 #applicant_Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.form_details.applicant_Org).text(jsonvalue.form_details.applicant_Org).appendTo(select);
                        if (jsonvalue.form_details.applicant_Org.toString().toLowerCase() === 'other') {
                            $('#single_form2 #applicant_other_text_div').removeClass("display-hide");
                            $('#single_form2 #applicant_other_dept').val(jsonvalue.form_details.applicant_other_dept);
                        }

                    } else {
                        $('#single_form2 #applicant_central_div').hide();
                        $('#single_form2 #applicant_state_div').hide();
                        $('#single_form2 #applicant_other_div').hide();
                    }

                    $('#single_form2 #applicant_employment').val(jsonvalue.form_details.applicant_employment);
                    $('#single_form2 #applicant_state').val(jsonvalue.form_details.applicant_state);
                    $('#single_form2 #applicant_name').val(jsonvalue.form_details.applicant_name);
                    $('#single_form2 #applicant_design').val(jsonvalue.form_details.applicant_design);
                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {
                        $('#single_form2 #USEditPreview').removeClass("display-hide");
                        $('#single_form2 #under_sec_email').val(jsonvalue.profile_values.under_sec_email);
                        $('#single_form2 #under_sec_name').val(jsonvalue.profile_values.under_sec_name);
                        var USstartMobile = jsonvalue.profile_values.under_sec_mobile.substring(0, 3);
                        var USendMobile = jsonvalue.profile_values.under_sec_mobile.substring(10, 13);
                        $('#single_form2 #under_sec_mobile').val(USstartMobile + 'XXXXXXX' + USendMobile);
                        $('#single_form2 #under_sec_tel').val(jsonvalue.profile_values.under_sec_tel);
                        $('#single_form2 #under_sec_desig').val(jsonvalue.profile_values.under_sec_desig);
                        $('#single_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile)
                        $('#single_form2 #user_mobile').val(jsonvalue.profile_values.mobile);

                    } else {

                        $('#single_form2 #USEditPreview').addClass("display-hide");
                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#single_form2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                        $('#single_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    }
                    $('#single_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#single_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#single_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#single_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#single_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#single_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#single_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#single_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#single_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#single_form2 #user_rphone').val(jsonvalue.profile_values.rphone);

                    $('#single_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#single_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#single_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#single_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#single_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    $('#single_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    if (jsonvalue.form_details.req_user_type === "other_user")
                    {
                        // alert("inside other user"+jsonvalue.form_details.applicant_single_email1)
                        $('#single_form2  #req_other_type').prop("checked", true);
                        $('#single_form2 #div_for_other').removeClass("display-hide");
                        $('#single_form2 #div_for_self').addClass("display-hide");
                        $('#single_form2 #applicant_name').val(jsonvalue.form_details.applicant_name);
                        $('#single_form2 #applicant_empcode').val(jsonvalue.form_details.applicant_empcode);
                        $('#single_form2 #applicant_mobile').val(jsonvalue.form_details.applicant_mobile);
                        $('#single_form2 #applicant_email').val(jsonvalue.form_details.applicant_email);
                        $('#single_form2 #applicant_design').val(jsonvalue.form_details.applicant_design);
                        $('#single_form2 #single_dob7').val(jsonvalue.form_details.applicant_single_dob);
                        $('#single_form2 #single_dor7').val(jsonvalue.form_details.applicant_single_dor);
                        $('#single_form2 #applicant_single_email1').val(jsonvalue.form_details.applicant_single_email1);
                        $('#single_form2 #applicant_single_email2').val(jsonvalue.form_details.applicant_single_email2);
                        if (jsonvalue.form_details.applicant_req_for === 'mail') {
                            $("#single_form2 #mail_1").prop('checked', 'checked');
                        } else if (jsonvalue.form_details.applicant_req_for === 'eoffice') {
                            $("#single_form2 #eoffice_1").prop('checked', 'checked');
                        } else {
                            $("#single_form2 #app_1").prop('checked', 'checked');
                        }
                        if (jsonvalue.form_details.applicant_single_id_type === 'id_name') {
                            $("#single_form2 #single_id_type_1").prop('checked', 'checked');
                        } else {
                            $("#single_form2 #single_id_type_2").prop('checked', 'checked');
                        }
                        if (jsonvalue.form_details.applicant_single_emp_type === 'emp_regular') {
                            $('#single_form2 #single_emp_type1').prop('checked', 'checked');
                            $('#single_form2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                            $('#single_form2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                        } else if (jsonvalue.form_details.applicant_single_emp_type === 'consultant') {
                            $('#single_form2 #single_emp_type2').prop('checked', 'checked');
                            $('#single_form2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                            $('#single_form2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                        } else {
                            $('#single_form2 #single_emp_type3').prop('checked', 'checked');
                            $('#single_form2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                            $('#single_form2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                        }

                    } else {
                        $('#single_form2  #req_self_type').prop("checked", true);
                        $('#single_form2 #div_for_self').removeClass("display-hide");
                        $('#single_form2 #div_for_other').addClass("display-hide");
                        $('#single_form2 #single_email1').val(jsonvalue.form_details.single_email1);
                        $('#single_form2 #single_email2').val(jsonvalue.form_details.single_email2);
                        $('#single_form2 #single_dob1').val(jsonvalue.form_details.single_dob);
                        $('#single_form2 #single_dor1').val(jsonvalue.form_details.single_dor);
                        if (jsonvalue.form_details.req_for === 'mail') {
                            $("#single_form2 #mail_1").prop('checked', 'checked');
                        } else if (jsonvalue.form_details.req_for === 'eoffice') {
                            $("#single_form2 #eoffice_1").prop('checked', 'checked');
                        } else {
                            $("#single_form2 #app_1").prop('checked', 'checked');
                        }
                        if (jsonvalue.form_details.single_id_type === 'id_name') {
                            $("#single_form2 #single_id_type_1").prop('checked', 'checked');
                        } else {
                            $("#single_form2 #single_id_type_2").prop('checked', 'checked');
                        }
                        if (jsonvalue.form_details.single_emp_type === 'emp_regular') {
                            $('#single_form2 #single_emp_type1').prop('checked', 'checked');
                            $('#single_form2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                            $('#single_form2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                        } else if (jsonvalue.form_details.single_emp_type === 'consultant') {
                            $('#single_form2 #single_emp_type2').prop('checked', 'checked');
                            $('#single_form2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                            $('#single_form2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                        } else {
                            $('#single_form2 #single_emp_type3').prop('checked', 'checked');
                            $('#single_form2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                            $('#single_form2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                        }

                    }

                    $('.edit').removeClass('display-hide');
                    $('#single_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $('#single_preview_tab #profile_div').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#single_form2 :button[type='button']").removeAttr('disabled');
                    $("#single_form2 #tnc").removeAttr('disabled');
                    $('#large').modal({backdrop: 'static', keyboard: false});
                }

            }, error: function ()
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
    $('#single_form2').submit(function (e) {
        $(".loader").show();
        e.preventDefault();

        $("#single_form2 :disabled").removeAttr('disabled');
        $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#single_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#single_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#single_form2').serializeObject());
        $('#single_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $('#single_form2 .edit').removeClass('display-hide');
        console.log('data: ' + data)
        $.ajax({
            type: "POST",
            url: "single_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#single_form2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#single_form2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#single_form2 #single_email1_err').html("")
                        } else if (jsonvalue.error.errorMsg1.indexOf("some info") > -1)
                        {
                            $('#single_form1 #single_email1_err').html("some info")
                        } else {
                            $('#single_form2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#single_form2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#single_form2 #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#single_form2 #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#single_form2 #single_email2_err').html("")
                        } else if (jsonvalue.error.errorMsg2.indexOf("some info") > -1)
                        {
                            $('#single_form1 #single_email1_err').html("some info")
                        } else {
                            $('#single_form2 #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#single_form2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#single_form2 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#single_form2 #single_dob').focus();
                    error_flag = false;
                } else {

                    $('#single_form2 #single_dob_err').html("")
                }
                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#single_form2 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#single_form2 #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#single_form2 #single_dor_err').html("")
                }

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#single_form2 #useremployment_error').focus();
                    $('#single_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#single_form2 #minerror').focus();
                    $('#single_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#single_form2 #deperror').focus();
                    $('#single_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#single_form2 #other_dept').focus();
                    $('#single_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#single_form2 #smierror').focus();
                    $('#single_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#single_form2 #state_error').focus();
                    $('#single_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#single_form2 #org_error').focus();
                    $('#single_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#single_form2 #ca_design').focus();
                    $('#single_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#single_form2 #hod_name').focus();
                    $('#single_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#single_form2 #hod_mobile').focus();
                    $('#single_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {

                    $('#single_form2 #hod_email').focus();
                    $('#single_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodemail_error').html("");
                }


                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#single_form2 #hod_email').focus();
                    $('#single_form2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#single_form2 #hod_tel').focus();
                    $('#single_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodtel_error').html("");
                }
                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#single_form2 #under_sec_name').focus();
                    $('#single_form2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#single_form2 #under_sec_mobile').focus();
                    $('#single_form2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#single_form2 #under_sec_email').focus();
                    $('#single_form2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#single_form2 #under_sec_desig').focus();
                    $('#single_form2 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#single_form2 #under_sec_tel').focus();
                    $('#single_form2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_tel_error').html("");
                }
                // profile on 20th march

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                if (jsonvalue.error.email_info_error1 !== null && jsonvalue.error.email_info_error1 !== "" && jsonvalue.error.email_info_error1 !== undefined)
                {
                    $('#req_user_type_err').html("Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further");
                    error_flag = false;
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
                if (error_flag) {
                    $('#large').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'single'},
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
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });
    $('#single_form2 .edit').click(function () {
        var employment = $('#single_preview_tab #user_employment').val();
        var min = $('#single_preview_tab #min').val();
        var dept = $('#single_preview_tab #dept').val();
        var statecode = $('#single_preview_tab #stateCode').val();
        var Smi = $('#single_preview_tab #Smi').val();
        var Org = $('#single_preview_tab #Org').val();

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

        $('#single_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#single_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#single_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#single_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#single_preview_tab #REditPreview #hod_email').removeAttr('disabled');

        $(this).addClass('display-hide');
        //$(this).hide();
    });
    $('#single_form2 #confirm').click(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#single_form2 :disabled").removeAttr('disabled');
        $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#single_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#single_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#single_form2').serializeObject());

        $('#single_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "single_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;
                if (jsonvalue.error.email_info_error1 !== null && jsonvalue.error.email_info_error1 !== "" && jsonvalue.error.email_info_error1 !== undefined)
                {
                    $('#req_user_type_err').html("Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further");
                    error_flag = false;
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#single_form2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#single_form2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#single_form2 #single_email1_err').html("")
                        } else if (jsonvalue.error.errorMsg1.indexOf("some info") > -1)
                        {
                            $('#single_form1 #single_email1_err').html("some info")
                        } else {
                            $('#single_form2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#single_form2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#single_form2 #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#single_form2 #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#single_form2 #single_email2_err').html("")
                        } else if (jsonvalue.error.errorMsg2.indexOf("some info") > -1)
                        {
                            $('#single_form1 #single_email2_err').html("some info")
                        } else {
                            $('#single_form2 #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#single_form2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#single_form2 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#single_form2 #single_dob').focus();
                    error_flag = false;
                } else {

                    $('#single_form2 #single_dob_err').html("")
                }
                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#single_form2 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#single_form2 #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#single_form2 #single_dor_err').html("")
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
                    $('#single_form2 #useremployment_error').focus();
                    $('#single_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#single_form2 #minerror').focus();
                    $('#single_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#single_form2 #deperror').focus();
                    $('#single_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#single_form2 #other_dept').focus();
                    $('#single_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#single_form2 #smierror').focus();
                    $('#single_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#single_form2 #state_error').focus();
                    $('#single_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#single_form2 #org_error').focus();
                    $('#single_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#single_form2 #ca_design').focus();
                    $('#single_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#single_form2 #hod_name').focus();
                    $('#single_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#single_form2 #hod_mobile').focus();
                    $('#single_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {

                    $('#single_form2 #hod_email').focus();
                    $('#single_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodemail_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#single_form2 #hod_email').focus();
                    $('#single_form2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#single_form2 #hod_tel').focus();
                    $('#single_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodtel_error').html("");
                }



                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#single_form2 #under_sec_name').focus();
                    $('#single_form2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#single_form2 #under_sec_mobile').focus();
                    $('#single_form2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#single_form2 #under_sec_email').focus();
                    $('#single_form2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#single_form2 #under_sec_tel').focus();
                    $('#single_form2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#single_form2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#single_form2 #under_sec_desig').focus();
                    $('#single_form2 #under_sec_desg_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#single_form2 #under_sec_desig_error').html("");
                }
                // profile on 20th march

                if (!error_flag) {
                    $("#single_form2 :disabled").removeAttr('disabled');
                    $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#single_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#single_form2 #tnc').is(":checked"))
                    {
                        $('#single_form2 #tnc_error').html("");
                        $('#single_form_confirm').removeClass('display-hide');
                        $('#stack3').modal({backdrop: 'static', keyboard: false});


                        if (jsonvalue.form_details.req_for == "eoffice")
                        {

                            $('#single_form_confirm #fill_hod_name').html("Rachna Srivastava");
                            $('#single_form_confirm #fill_hod_email').html("rachna_sri@nic.in");
                            $('#single_form_confirm #fill_hod_mobile').html("+91XXXXXXX166");


                        } else {


//                            if (jsonvalue.form_details.min == "External Affairs")
//                            {
//                                $('#single_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                                $('#single_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                                $('#single_form_confirm #fill_hod_mobile').html("+919384664224");
//                            } else {
                            $('#single_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#single_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#single_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                            //  }
                            $('#single_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        }
//                        $('#single_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                        $('#single_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                        $('#single_form_confirm #fill_hod_mobile').html(jsonvalue.form_details.hod_mobile);
//                        $('#single_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        //$('#single_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#single_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#single_form2 #tnc").removeAttr('disabled');
                        $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        // $('#single_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }, error: function ()
            {
                $(".loader").hide();
                $('#tab1').show();
            }
        });

    });
    $('#single_form_confirm #confirmYes').click(function () {
        $('#single_form2').submit();
        $('#stack3').modal('toggle');
    });
    $(document).on('click', '#singleuser_preview .edit', function () {
        //$('#singleuser_preview .edit').click(function () {

        var employment = $('#single_preview_tab #user_employment').val();
        var min = $('#single_preview_tab #min').val();
        var dept = $('#single_preview_tab #dept').val();
        var statecode = $('#single_preview_tab #stateCode').val();
        var Smi = $('#single_preview_tab #Smi').val();
        var Org = $('#single_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#single_preview_tab #min');
                select.find('option').remove();
                // alert(select)
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    console.log("min:::::::::" + min + "value::::::::::" + value)
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
                var select = $('#single_preview_tab #dept');
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
                $('#single_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#single_preview_tab #stateCode');
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
                var select = $('#single_preview_tab #Smi');
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
                $('#single_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#single_preview_tab #central_div').hide();
            $('#single_preview_tab #state_div').hide();
            $('#single_preview_tab #other_div').show();
            $('#single_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#single_preview_tab #Org');
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
                $('#single_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#single_preview_tab #central_div').hide();
            $('#single_preview_tab #state_div').hide();
            $('#single_preview_tab #other_div').hide();
        }

        $('#single_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#single_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#single_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#singleuser_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#singleuser_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#singleuser_preview #confirm', function () {
        //$('#singleuser_preview #confirm').click(function () {
        $('#singleuser_preview').submit();
        // $('#singleuser_preview_form').modal('toggle');
    });
    $('#singleuser_preview').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#singleuser_preview :disabled").removeAttr('disabled');
        $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march   
        $('#single_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march // nikki 22 jan 2019   
        $('#single_preview_tab #user_email').removeAttr('disabled');// 20th march    
        var data = JSON.stringify($('#singleuser_preview').serializeObject());
        $('#single_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#single_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 jan 2019 
        console.log('DATA inside submit preview: ' + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "single_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;
                if (jsonvalue.error.email_info_error1 !== null && jsonvalue.error.email_info_error1 !== "" && jsonvalue.error.email_info_error1 !== undefined)
                {
                    $('#req_user_type_err').html("Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further");
                    error_flag = false;
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Since, you have selected <b>For Other users(Where you are posted)</b> option and you are not NIC employee, Kindly unselect it to proceed further.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }
                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#singleuser_preview #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#singleuser_preview #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#singleuser_preview #single_email1_err').html("")
                        } else {
                            $('#singleuser_preview #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#singleuser_preview #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#singleuser_preview #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#singleuser_preview #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#singleuser_preview #single_email2_err').html("")
                        } else {
                            $('#singleuser_preview #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#singleuser_preview #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#singleuser_preview #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#singleuser_preview #single_dob').focus();
                    error_flag = false;
                } else {

                    $('#singleuser_preview #single_dob_err').html("")
                }
                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#singleuser_preview #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#singleuser_preview #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#singleuser_preview #single_dor_err').html("")
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
                    $('#singleuser_preview #useremployment_error').focus();
                    $('#singleuser_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#singleuser_preview #minerror').focus();
                    $('#singleuser_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#singleuser_preview #deperror').focus();
                    $('#singleuser_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#singleuser_preview #other_dept').focus();
                    $('#singleuser_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#singleuser_preview #smierror').focus();
                    $('#singleuser_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#singleuser_preview #state_error').focus();
                    $('#singleuser_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#singleuser_preview #org_error').focus();
                    $('#singleuser_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #org_error').html("");
                }

                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {

                    $('#singleuser_preview #under_sec_name').focus();
                    $('#singleuser_preview #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#singleuser_preview #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#singleuser_preview #under_sec_mobile').focus();
                    $('#singleuser_preview #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#singleuser_preview #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#singleuser_preview #under_sec_email').focus();
                    $('#singleuser_preview #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#singleuser_preview #under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#singleuser_preview #under_sec_desig').focus();
                    $('#singleuser_preview #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#singleuser_preview #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#singleuser_preview #under_sec_tel').focus();
                    $('#singleuser_preview #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {

                    $('#singleuser_preview #under_sec_tel_error').html("");
                }
                // profile 20th march 

                if (!error_flag)
                {

                    $("#singleuser_preview :disabled").removeAttr('disabled');
                    $('#single_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#single_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                    //$('#mobile_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    //$("#mobile_form2 #tnc").removeAttr('disabled');
                } else {

                    $('#singleuser_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
    // bulk form submission

//    $('#bulk_form1').submit(function (e) {
//        $(".loader").show();
//        e.preventDefault();
//        var data = JSON.stringify($('#bulk_form1').serializeObject());
//        var cert = $('#cert').val();
//
//        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
//
//        $.ajax({
//            type: "POST",
//            url: "bulk_tab1",
//            data: {data: data, cert: cert, CSRFRandom: CSRFRandom},
//            datatype: JSON,
//            success: function (data)
//            {
//                $(".loader").hide();
//                resetCSRFRandom();// line added by pr on 22ndjan18
//
//                var myJSON = JSON.stringify(data);
//                var jsonvalue = JSON.parse(myJSON);
//                var error_flag = true;
//
//                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
//                {
//                    $('#bulk_form1 #file_err').html(jsonvalue.error.file_err)
//                    error_flag = false;
//                    $('#bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                    $('#bulk_form1 #captcha1').val("");
//
//                } else {
//                    $('#bulk_form1 #file_err').html("")
//                }
//
//                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
//                {
//                    $('#bulk_form1 #captchaerror').html(jsonvalue.error.cap_error)
//                    error_flag = false;
//                    $('#bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                    $('#bulk_form1 #imgtxt').val("");
//                } else {
//                    $('#bulk_form1 #captchaerror').html("")
//                }
//
//                // start, code added by pr on 22ndjan18
//
//                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
//                {
//
//                    $('#captchaerror').html(jsonvalue.error.csrf_error);
//                    error_flag = false;
//                    $('#bulk_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                    $('#bulk_form1 #imgtxt2').val("");
//                }
//
//                // end, code added by pr on 22ndjan18
//                if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
//                {
//                    error_flag = false;
//                    bootbox.dialog({
//                        message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li> <li>VPN Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
//                                + "<p style='text-align: center; font-size: 18px;'>To register for other services, please log in with your government email service(NIC) email address.</p>",
//                        title: "",
//                        buttons: {
//                            cancel: {
//                                label: "OK",
//                                className: 'btn-info'
//                            }
//                        }
//                    });
//
//
//                }
//                if (error_flag) {
//
//                    $('#email_head1').addClass('display-hide');
//                    $('#email_head2').addClass('display-hide');
//                    $('#bulk-1').addClass('display-hide');
//                    $('#bulk-2').removeClass('display-hide');
//                } else {
//                    $('#email_head1').removeClass('display-hide');
//                    $('#email_head2').removeClass('display-hide');
//                    $('#bulk-1').removeClass('display-hide');
//                    $('#bulk-2').addClass('display-hide');
//                }
//            }, error: function ()
//            {
//                $(".loader").hide();
//                console.log('error');
//            }
//        });
//    });
    $('#bulk_form1').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        var data = JSON.stringify($('#bulk_form1').serializeObject());
        var cert = $('#cert').val();

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "bulk_tab1",
            data: {data: data, cert: cert, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                console.log(jsonvalue.bulkData)
                if (!jQuery.isEmptyObject(jsonvalue.bulkData)) {
                    setTimeout(function () {
                        bulkEmailCreate(jsonvalue.bulkData);
                    }, 100);
                }
                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                {
                    $('#bulk_form1 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#bulk_form1 #captcha1').val("");

                } else {
                    $('#bulk_form1 #file_err').html("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#bulk_form1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#bulk_form1 #imgtxt').val("");
                } else {
                    $('#bulk_form1 #captchaerror').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#bulk_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#bulk_form1 #imgtxt2').val("");
                }
//                    if (jsonvalue.iCampaignId) {
//                    showCapaign(jsonvalue.iCampaignId)
//                }
                // end, code added by pr on 22ndjan18
                if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
                {
                    error_flag = false;
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li> <li>VPN Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                                + "<p style='text-align: center; font-size: 18px;'>To register for other services, please log in with your government email service(NIC) email address.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });


                }
                if (error_flag) {

                    $('#email_head1').addClass('display-hide');
                    $('#email_head2').addClass('display-hide');
                    $('#bulk-1').addClass('display-hide');
                    $('#bulk-2').removeClass('display-hide');
                } else {
                    $('#email_head1').removeClass('display-hide');
                    $('#email_head2').removeClass('display-hide');
                    $('#bulk-1').removeClass('display-hide');
                    $('#bulk-2').addClass('display-hide');
                }
            }, error: function ()
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
//    $('#bulk_form2').submit(function (e) {
//        $(".loader").show();
//        e.preventDefault();
//        $.ajax({
//            type: "POST",
//            url: "bulk_tab2",
//            datatype: JSON,
//            success: function (data)
//            {
//                $(".loader").hide();
//                var myJSON = JSON.stringify(data);
//                var jsonvalue = JSON.parse(myJSON);
//
//                var error_flag = true;
//
//                if (jsonvalue.error.error_file !== null && jsonvalue.error.error_file !== "" && jsonvalue.error.error_file !== undefined)
//                {
//                    $('#bulk_form2 #error_file').html(jsonvalue.error.error_file)
//                    error_flag = false;
//                } else {
//                    $('#bulk_form2 #error_file').html("")
//                }
//                if (error_flag) {
//
//                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
//                        $('#bulk_form3 #central_div').show();
//                        $('#bulk_form3 #state_div').hide();
//                        $('#bulk_form3 #other_div').hide();
//                        $('#bulk_form3 #other_text_div').addClass("display-hide");
//                        var select = $('#bulk_form3 #min');
//                        select.find('option').remove();
//                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
//                        var select = $('#bulk_form3 #dept');
//                        select.find('option').remove();
//                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
//                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
//                            $('#bulk_form3 #other_text_div').removeClass("display-hide");
//                            $('#bulk_form3 #other_dept').val(jsonvalue.profile_values.other_dept);
//                        }
//
//
//                    } else if (jsonvalue.profile_values.user_employment === 'State') {
//                        $('#bulk_form3 #central_div').hide();
//                        $('#bulk_form3 #state_div').show();
//                        $('#bulk_form3 #other_div').hide();
//                        $('#bulk_form3 #other_text_div').addClass("display-hide");
//                        var select = $('#bulk_form3 #stateCode');
//                        select.find('option').remove();
//                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
//                        var select = $('#bulk_form3 #Smi');
//                        select.find('option').remove();
//                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
//                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
//                            $('#bulk_form3 #other_text_div').removeClass("display-hide");
//                            $('#bulk_form3 #other_dept').val(jsonvalue.profile_values.other_dept);
//                        }
//
//                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
//                        $('#bulk_form3 #central_div').hide();
//                        $('#bulk_form3 #state_div').hide();
//                        $('#bulk_form3 #other_div').show();
//                        $('#bulk_form3 #other_text_div').addClass("display-hide");
//                        var select = $('#bulk_form3 #Org');
//                        select.find('option').remove();
//                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
//                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
//                            $('#bulk_form3 #other_text_div').removeClass("display-hide");
//                            $('#bulk_form3 #other_dept').val(jsonvalue.profile_values.other_dept);
//                        }
//
//                    } else {
//                        $('#bulk_form3 #central_div').hide();
//                        $('#bulk_form3 #state_div').hide();
//                        $('#bulk_form3 #other_div').hide();
//                    }
//
//
//                    if (jsonvalue.profile_values.bo_check == "no bo")
//                    {
//                        $('#bulk_form3 #USEditPreview').removeClass("display-hide");
//                        $('#bulk_form3 #under_sec_email').val(jsonvalue.profile_values.under_sec_email);
//                        $('#bulk_form3 #under_sec_name').val(jsonvalue.profile_values.under_sec_name);
//                        var USstartMobile = jsonvalue.profile_values.under_sec_mobile.substring(0, 3);
//                        var USendMobile = jsonvalue.profile_values.under_sec_mobile.substring(10, 13);
//                        $('#bulk_form3 #under_sec_mobile').val(USstartMobile + 'XXXXXXX' + USendMobile);
//                        $('#bulk_form3 #under_sec_tel').val(jsonvalue.profile_values.under_sec_tel);
//                        $('#bulk_form3 #under_sec_desig').val(jsonvalue.profile_values.under_sec_desig);
//                        $('#bulk_form3 #hod_mobile').val(jsonvalue.profile_values.hod_mobile)
//                        $('#bulk_form3 #user_mobile').val(jsonvalue.profile_values.mobile);
//
//                    } else {
//
//                        $('#bulk_form3 #USEditPreview').addClass("display-hide");
//                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
//                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
//                        $('#bulk_form3 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
//                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
//                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
//                        $('#bulk_form3 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
//                    }
//
//                    $('#bulk_form3 #user_employment').val(jsonvalue.profile_values.user_employment);
//                    $('#bulk_form3 #stateCode').val(jsonvalue.profile_values.stateCode);
//                    $('#bulk_form3 #user_name').val(jsonvalue.profile_values.cn);
//                    $('#bulk_form3 #user_design').val(jsonvalue.profile_values.desig);
//                    $('#bulk_form3 #user_address').val(jsonvalue.profile_values.postalAddress);
//                    $('#bulk_form3 #user_city').val(jsonvalue.profile_values.nicCity);
//                    $('#bulk_form3 #user_state').val(jsonvalue.profile_values.state);
//                    $('#bulk_form3 #user_pincode').val(jsonvalue.profile_values.pin);
//                    $('#bulk_form3 #user_ophone').val(jsonvalue.profile_values.ophone);
//                    $('#bulk_form3 #user_rphone').val(jsonvalue.profile_values.rphone);
//                    $('#bulk_form3 #user_email').val(jsonvalue.profile_values.email);
//                    $('#bulk_form3 #user_empcode').val(jsonvalue.profile_values.emp_code);
//                    $('#bulk_form3 #hod_name').val(jsonvalue.profile_values.hod_name);
//                    $('#bulk_form3 #hod_email').val(jsonvalue.profile_values.hod_email);
//                    $('#bulk_form3 #hod_tel').val(jsonvalue.profile_values.hod_tel);
//                    //$('#bulk_form3 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
//                    $('#bulk_form3 #ca_design').val(jsonvalue.profile_values.ca_design);
//                    if (jsonvalue.form_details.req_for === 'mail') {
//                        $("#bulk_form3 #bulk_mail_1").prop('checked', 'checked');
//                    } else if (jsonvalue.form_details.req_for === 'eoffice') {
//                        $("#bulk_form3 #bulk_eoffice_1").prop('checked', 'checked');
//                    } else {
//                        $("#bulk_form3 #bulk_app_1").prop('checked', 'checked');
//                    }
//                    if (jsonvalue.form_details.single_id_type === 'id_name') {
//                        $("#bulk_form3 #bulk_id_type_1").prop('checked', true);
//                    } else {
//                        $("#bulk_form3 #bulk_id_type_2").prop('checked', true);
//                    }
//                    $('#bulk_form3 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);
//
//                    if (jsonvalue.form_details.single_emp_type === 'emp_regular') {
//                        $('#bulk_form3 #bulk_emp_type1').prop('checked', 'checked');
//
//                    } else if (jsonvalue.form_details.single_emp_type === 'consultant') {
//                        $('#bulk_form3 #bulk_emp_type2').prop('checked', 'checked');
//                    } else {
//                        $('#bulk_form3 #bulk_emp_type3').prop('checked', 'checked');
//
//                    }
//
//
//                    // start, code added by pr on 14thjun18
//
//                    var renamedPath = jsonvalue.form_details.renamed_filepath;
//
//                    var pathName = renamedPath.replace(".csv", "");
//
//                    pathName = pathName.trim();
//
//                    var valid_filepath = pathName + "-valid.csv";
//
//                    var notvalid_filepath = pathName + "-notvalid.csv";
//
//                    var error_filepath = pathName + "-error.csv";
//
//
//                    /*$('#bulk_form3 #valid_filepath').text(valid_filepath);
//                     
//                     $('#bulk_form3 #notvalid_filepath').text(notvalid_filepath);
//                     
//                     $('#bulk_form3 #error_filepath').text(error_filepath);*/
//
//                    // end, code added by pr on 14thjun18
//
//
//
//
//                    $('.edit').removeClass('display-hide');
//                    $('#bulk_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
//                    $("#bulk_form3 :button[type='button']").removeAttr('disabled');
//                    $("#bulk_form3 #tnc").removeAttr('disabled');
//                    $('#large1').modal({backdrop: 'static', keyboard: false});
//                }
//
//
//            }, error: function ()
//            {
//                $(".loader").hide();
//                $('#tab1').show();
//            }
//        });
//    });
    $('#bulk_form2').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $.ajax({
            type: "POST",
            url: "Email_bulk_tab2",
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.error_file !== null && jsonvalue.error.error_file !== "" && jsonvalue.error.error_file !== undefined)
                {
                    $('#bulk_form2 #error_file').html(jsonvalue.error.error_file)
                    error_flag = false;

                } else {
                    $('#bulk_form2 #error_file').html("")
                }
                if (error_flag) {

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#bulk_form3 #central_div').show();
                        $('#bulk_form3 #state_div').hide();
                        $('#bulk_form3 #other_div').hide();
                        $('#bulk_form3 #other_text_div').addClass("display-hide");
                        var select = $('#bulk_form3 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#bulk_form3 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#bulk_form3 #other_text_div').removeClass("display-hide");
                            $('#bulk_form3 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#bulk_form3 #central_div').hide();
                        $('#bulk_form3 #state_div').show();
                        $('#bulk_form3 #other_div').hide();
                        $('#bulk_form3 #other_text_div').addClass("display-hide");
                        var select = $('#bulk_form3 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#bulk_form3 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#bulk_form3 #other_text_div').removeClass("display-hide");
                            $('#bulk_form3 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#bulk_form3 #central_div').hide();
                        $('#bulk_form3 #state_div').hide();
                        $('#bulk_form3 #other_div').show();
                        $('#bulk_form3 #other_text_div').addClass("display-hide");
                        var select = $('#bulk_form3 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#bulk_form3 #other_text_div').removeClass("display-hide");
                            $('#bulk_form3 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#bulk_form3 #central_div').hide();
                        $('#bulk_form3 #state_div').hide();
                        $('#bulk_form3 #other_div').hide();
                    }


                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {
                        $('#bulk_form3 #USEditPreview').removeClass("display-hide");
                        $('#bulk_form3 #under_sec_email').val(jsonvalue.profile_values.under_sec_email);
                        $('#bulk_form3 #under_sec_name').val(jsonvalue.profile_values.under_sec_name);
                        var USstartMobile = jsonvalue.profile_values.under_sec_mobile.substring(0, 3);
                        var USendMobile = jsonvalue.profile_values.under_sec_mobile.substring(10, 13);
                        $('#bulk_form3 #under_sec_mobile').val(USstartMobile + 'XXXXXXX' + USendMobile);
                        $('#bulk_form3 #under_sec_tel').val(jsonvalue.profile_values.under_sec_tel);
                        $('#bulk_form3 #under_sec_desig').val(jsonvalue.profile_values.under_sec_desig);
                        $('#bulk_form3 #hod_mobile').val(jsonvalue.profile_values.hod_mobile)
                        $('#bulk_form3 #user_mobile').val(jsonvalue.profile_values.mobile);

                    } else {

                        $('#bulk_form3 #USEditPreview').addClass("display-hide");
                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#bulk_form3 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                        $('#bulk_form3 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    }

                    $('#bulk_form3 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#bulk_form3 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#bulk_form3 #user_name').val(jsonvalue.profile_values.cn);
                    $('#bulk_form3 #user_design').val(jsonvalue.profile_values.desig);
                    $('#bulk_form3 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#bulk_form3 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#bulk_form3 #user_state').val(jsonvalue.profile_values.state);
                    $('#bulk_form3 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#bulk_form3 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#bulk_form3 #user_rphone').val(jsonvalue.profile_values.rphone);
                    $('#bulk_form3 #user_email').val(jsonvalue.profile_values.email);
                    $('#bulk_form3 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#bulk_form3 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#bulk_form3 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#bulk_form3 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#bulk_form3 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#bulk_form3 #ca_design').val(jsonvalue.profile_values.ca_design);
                    if (jsonvalue.form_details.req_for === 'mail') {
                        $("#bulk_form3 #bulk_mail_1").prop('checked', 'checked');
                    } else if (jsonvalue.form_details.req_for === 'eoffice') {
                        $("#bulk_form3 #bulk_eoffice_1").prop('checked', 'checked');
                    } else {
                        $("#bulk_form3 #bulk_app_1").prop('checked', 'checked');
                    }
                    if (jsonvalue.form_details.single_id_type === 'id_name') {
                        $("#bulk_form3 #bulk_id_type_1").prop('checked', true);
                    } else {
                        $("#bulk_form3 #bulk_id_type_2").prop('checked', true);
                    }
                    $('#bulk_form3 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);

                    if (jsonvalue.form_details.single_emp_type === 'emp_regular') {
                        $('#bulk_form3 #bulk_emp_type1').prop('checked', 'checked');

                    } else if (jsonvalue.form_details.single_emp_type === 'consultant') {
                        $('#bulk_form3 #bulk_emp_type2').prop('checked', 'checked');
                    } else {
                        $('#bulk_form3 #bulk_emp_type3').prop('checked', 'checked');

                    }


                    // start, code added by pr on 14thjun18

                    var renamedPath = jsonvalue.form_details.renamed_filepath;

                    var pathName = renamedPath.replace(".csv", "");

                    pathName = pathName.trim();

                    var valid_filepath = pathName + "-valid.csv";

                    var notvalid_filepath = pathName + "-notvalid.csv";

                    var error_filepath = pathName + "-error.csv";


                    /*$('#bulk_form3 #valid_filepath').text(valid_filepath);
                     
                     $('#bulk_form3 #notvalid_filepath').text(notvalid_filepath);
                     
                     $('#bulk_form3 #error_filepath').text(error_filepath);*/

                    // end, code added by pr on 14thjun18


                    if (!jQuery.isEmptyObject(jsonvalue.bulkData.validRecord)) {
                        validDomForReqModify1(jsonvalue.bulkData)
                    }

                    $('.edit').removeClass('display-hide');
                    $('#bulk_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#bulk_form3 :button[type='button']").removeAttr('disabled');
                    $("#bulk_form3 #tnc").removeAttr('disabled');
                    $('#large1').modal({backdrop: 'static', keyboard: false});
                }


            }, error: function ()
            {
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });
    $('#bulk_form3').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#bulk_form3 :disabled").removeAttr('disabled');
        $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#bulk_form3').serializeObject());

        $('#bulk_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('#bulk_form3 .edit').removeClass('display-hide');

        $.ajax({
            type: "POST",
            url: "bulk_tab3",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18


                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#bulk_form3 #useremployment_error').focus();
                    $('#bulk_form3 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#bulk_form3 #minerror').focus();
                    $('#bulk_form3 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#bulk_form3 #deperror').focus();
                    $('#bulk_form3 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#bulk_form3 #other_dept').focus();
                    $('#bulk_form3 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#bulk_form3 #smierror').focus();
                    $('#bulk_form3 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#bulk_form3 #state_error').focus();
                    $('#bulk_form3 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#bulk_form3 #org_error').focus();
                    $('#bulk_form3 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#bulk_form3 #ca_design').focus();
                    $('#bulk_form3 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#bulk_form3 #hod_name').focus();
                    $('#bulk_form3 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#bulk_form3 #hod_mobile').focus();
                    $('#bulk_form3 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#bulk_form3 #hod_email').focus();
                    $('#bulk_form3 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#bulk_form3 #hod_tel').focus();
                    $('#bulk_form3 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodtel_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {
                    $('#bulk_form3 #hod_email').focus();
                    $('#bulk_form3 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodemail_error1').html("");
                }



                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_name').focus();
                    $('#bulk_form3 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_mobile').focus();
                    $('#bulk_form3 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#bulk_form3 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_email').focus();
                    $('#bulk_form3 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#bulk_form3 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#bulk_form3 #under_sec_tel').focus();
                    $('#bulk_form3 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_desig').focus();
                    $('#bulk_form3 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#bulk_form3 #under_sec_desig_error').html("");
                }
                // profile on 20th march

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#bulk_form3 #tnc_error').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18

                // below if else added by pr on 22ndjan18
                if (error_flag) {
                    $('#large1').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'bulk'},
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
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });
    $('#bulk_form3 .edit').click(function () {

        var employment = $('#bulk_preview_tab #user_employment').val();
        var min = $('#bulk_preview_tab #min').val();
        var dept = $('#bulk_preview_tab #dept').val();
        var statecode = $('#bulk_preview_tab #stateCode').val();
        var Smi = $('#bulk_preview_tab #Smi').val();
        var Org = $('#bulk_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#bulk_preview_tab #min');
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
                var select = $('#bulk_preview_tab #dept');
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
                $('#bulk_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#bulk_preview_tab #stateCode');
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
                var select = $('#bulk_preview_tab #Smi');
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
                $('#bulk_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#bulk_preview_tab #central_div').hide();
            $('#bulk_preview_tab #state_div').hide();
            $('#bulk_preview_tab #other_div').show();
            $('#bulk_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#bulk_preview_tab #Org');
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
                $('#bulk_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#bulk_preview_tab #central_div').hide();
            $('#bulk_preview_tab #state_div').hide();
            $('#bulk_preview_tab #other_div').hide();
        }
        $('#bulk_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#bulk_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#bulk_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#bulk_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #REditPreview #hod_email').removeAttr('disabled');
        $(this).addClass('display-hide');
    });
    $('#bulk_form3 #confirm').click(function () {
        $(".loader").show();
        $("#bulk_form3 :disabled").removeAttr('disabled');
        $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#bulk_form3').serializeObject());
        $('#bulk_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "bulk_tab3",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18


                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#bulk_form3 #useremployment_error').focus();
                    $('#bulk_form3 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#bulk_form3 #minerror').focus();
                    $('#bulk_form3 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#bulk_form3 #deperror').focus();
                    $('#bulk_form3 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#bulk_form3 #other_dept').focus();
                    $('#bulk_form3 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#bulk_form3 #smierror').focus();
                    $('#bulk_form3 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#bulk_form3 #state_error').focus();
                    $('#bulk_form3 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#bulk_form3 #org_error').focus();
                    $('#bulk_form3 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#bulk_form3 #ca_design').focus();
                    $('#bulk_form3 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#bulk_form3 #hod_name').focus();
                    $('#bulk_form3 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#bulk_form3 #hod_mobile').focus();
                    $('#bulk_form3 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodmobile_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {
                    $('#bulk_form3 #hod_email').focus();
                    $('#bulk_form3 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#bulk_form3 #hod_email').focus();
                    $('#bulk_form3 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#bulk_form3 #hod_tel').focus();
                    $('#bulk_form3 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #hodtel_error').html("");
                }


                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_name').focus();
                    $('#bulk_form3 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_mobile').focus();
                    $('#bulk_form3 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#bulk_form3 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_email').focus();
                    $('#bulk_form3 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#bulk_form3 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#bulk_form3 #under_sec_tel').focus();
                    $('#bulk_form3 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#bulk_form3 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#bulk_form3 #under_sec_desig').focus();
                    $('#bulk_form3 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#bulk_form3 #under_sec_desig_error').html("");
                }
                // profile on 20th march

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#bulk_form3 #tnc_error').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18
                console.log('error:: ' + error_flag)
                // below if else added by pr on 22ndjan18
                if (!error_flag) {
                    $("#bulk_form3 :disabled").removeAttr('disabled');
                    $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#bulk_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#bulk_form3 #tnc').is(":checked"))
                    {
                        console.log("no error")
                        $('#bulk_form3 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});


                        if (jsonvalue.form_details.req_for == "eoffice")
                        {

                            $('#bulk_form_confirm #fill_hod_name').html("Rachna Srivastava");
                            $('#bulk_form_confirm #fill_hod_email').html("rachna_sri@nic.in");
                            $('#bulk_form_confirm #fill_hod_mobile').html("+91XXXXXXX166");


                        } else {


//                            if (jsonvalue.form_details.min == "External Affairs")
//                            {
//                                $('#bulk_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                                $('#bulk_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                                $('#bulk_form_confirm #fill_hod_mobile').html("+919384664224");
//                            } else {
                            $('#bulk_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#bulk_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#bulk_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                            //  }
                            //alert(jsonvalue.form_details.min)
//                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                            $('#bulk_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                            $('#bulk_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                            $('#bulk_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);

                        }

                        $('#bulk_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');

                    } else {
                        $('#bulk_form3 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        // $('#bulk_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        $("#bulk_form3 #tnc").removeAttr('disabled');
                    }
                }
            }, error: function ()
            {
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });
    $('#bulk_form_confirm #confirmYes').click(function () {
        $('#bulk_form3').submit();
        $('#stack3').modal('toggle');
    });
    $(document).on('click', '#bulkuser_preview .edit', function () {
        //$('#bulkuser_preview .edit').click(function () {
        var employment = $('#bulk_preview_tab #user_employment').val();
        var min = $('#bulk_preview_tab #min').val();
        var dept = $('#bulk_preview_tab #dept').val();
        var statecode = $('#bulk_preview_tab #stateCode').val();
        var Smi = $('#bulk_preview_tab #Smi').val();
        var Org = $('#bulk_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#bulk_preview_tab #min');
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
                var select = $('#bulk_preview_tab #dept');
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
                $('#bulk_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#bulk_preview_tab #stateCode');
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
                var select = $('#bulk_preview_tab #Smi');
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
                $('#bulk_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#bulk_preview_tab #central_div').hide();
            $('#bulk_preview_tab #state_div').hide();
            $('#bulk_preview_tab #other_div').show();
            $('#bulk_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#bulk_preview_tab #Org');
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
                $('#bulk_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#bulk_preview_tab #central_div').hide();
            $('#bulk_preview_tab #state_div').hide();
            $('#bulk_preview_tab #other_div').hide();
        }
        $('#bulk_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#bulk_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#bulkuser_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#bulkuser_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#bulkuser_preview #confirm', function () {
        //$('#bulkuser_preview #confirm').click(function () {
        $('#bulkuser_preview').submit();
        //$('#bulkuser_preview_form').modal('toggle');
    });
    $('#bulkuser_preview').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#bulkuser_preview :disabled").removeAttr('disabled');


        $('#bulkuser_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#bulkuser_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#bulkuser_preview #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        // nikki 22 Jan 2019
        $('#bulkuser_preview #user_email').removeAttr('disabled');// 20th march  

        var data = JSON.stringify($('#bulkuser_preview').serializeObject());
        $('#bulkuser_preview #user_email').prop('disabled', 'true'); // 20th march
        $('#bulkuser_preview #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 Jan 2019
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "bulk_tab3",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 23rdjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#bulkuser_preview #tnc_error').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                // end, code added by pr on 22ndjan18

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#bulkuser_preview #useremployment_error').focus();
                    $('#bulkuser_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#bulkuser_preview #minerror').focus();
                    $('#bulkuser_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#bulkuser_preview #deperror').focus();
                    $('#bulkuser_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#bulkuser_preview #other_dept').focus();
                    $('#bulkuser_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#bulkuser_preview #smierror').focus();
                    $('#bulkuser_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#bulkuser_preview #state_error').focus();
                    $('#bulkuser_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#bulkuser_preview #org_error').focus();
                    $('#bulkuser_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #org_error').html("");
                }


                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#bulkuser_preview #under_sec_name').focus();
                    $('#bulkuser_preview #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#bulkuser_preview #under_sec_mobile').focus();
                    $('#bulkuser_preview #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#bulkuser_preview #under_sec_mobile_error').html("");
                }

                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#bulkuser_preview #under_sec_email').focus();
                    $('#bulkuser_preview #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#bulkuser_preview #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#bulkuser_preview #under_sec_tel').focus();
                    $('#bulkuser_preview #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#bulkuser_preview #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#bulkuser_preview #under_sec_desig').focus();
                    $('#bulkuser_preview #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#bulkuser_preview #under_sec_desig_error').html("");
                }
                //alert(error_flag)
                // profile 20th march 
                if (!error_flag)
                {
                    console.log('error block');
                    //alert("inside if")
                    $("#bulkuser_preview :disabled").removeAttr('disabled');
                    $('#bulk_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#bulk_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#bulkuser_preview_form').modal('toggle');
                }


            }, error: function ()
            {
                $(".loader").hide();
                console.log('error');
            }
        });




//        $.ajax({
//            type: "POST",
//            url: "update",
//            data: {data: data, CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
//            dataType: JSON,
//            success: function (data)
//            {
//                resetCSRFRandom();// line added by pr on 23rdjan18
//
//            }, error: function ()
//            {
//                console.log('error');
//            }
//        });
    });
    // gem form submission

    $('#gem_form1').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        var data = JSON.stringify($('#gem_form1').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "gem_tab1",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.dup_err !== null && jsonvalue.error.dup_err !== "" && jsonvalue.error.dup_err !== undefined)
                {
                    $('#gem_form1 #gem_email1_err').html(jsonvalue.error.dup_err)
                    error_flag = false;
                }

                if (jsonvalue.error.central_pse_error !== null && jsonvalue.error.central_pse_error !== "" && jsonvalue.error.central_pse_error !== undefined)
                {
                    $('#gem_form1 #central_pse_err').html(jsonvalue.error.central_pse_error);
                    $('#gem_form1 #central_pse_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #central_pse_err').html("");
                }
                if (jsonvalue.error.state_pse_error !== null && jsonvalue.error.state_pse_error !== "" && jsonvalue.error.state_pse_error !== undefined)
                {
                    $('#gem_form1 #pse_state_err').html(jsonvalue.error.state_pse_error);
                    $('#gem_form1 #pse_state_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #pse_state_err').html("");
                }
                if (jsonvalue.error.district_pse_error !== null && jsonvalue.error.district_pse_error !== "" && jsonvalue.error.district_pse_error !== undefined)
                {
                    $('#gem_form1 #pse_district_err').html(jsonvalue.error.district_pse_error);
                    $('#gem_form1 #pse_district_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #pse_district_err').html("");
                }

                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#gem_form1 #gem_dob_err').html(jsonvalue.error.dob_err);
                    $('#gem_form1 #gem_dob_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #gem_dob_err').html("");
                }


                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#gem_dor_err').html(jsonvalue.error.dor_err);
                    $('#gem_form1 #gem_dor_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_dor_err').html("");
                }

                if (jsonvalue.error.email1_pse_error !== null && jsonvalue.error.email1_pse_error !== "" && jsonvalue.error.email1_pse_error !== undefined)
                {
                    $('#gem_email1_err').html(jsonvalue.error.email1_pse_error);
                    $('#gem_form1 #gem_email1_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#gem_form1 #gem_email1_err').html("")
                        } else {
                            $('#gem_form1 #gem_email1_err').html(jsonvalue.error.errorMsg1);
                            $('#gem_form1 #gem_email1_err').focus();
                            error_flag = false;
                            $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                            $('#gem_form1 #imgtxt2').val("");
                        }
                    }
                }

                if (jsonvalue.error.email2_pse_error !== null && jsonvalue.error.email2_pse_error !== "" && jsonvalue.error.email2_pse_error !== undefined)
                {
                    $('#gem_email2_err').html(jsonvalue.error.email2_pse_error);
                    $('#gem_form1 #gem_email2_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#gem_form1 #gem_email2_err').html("")
                        } else {
                            $('#gem_form1 #gem_email2_err').html(jsonvalue.error.errorMsg2);
                            $('#gem_form1 #gem_email2_err').focus();
                            error_flag = false;
                            $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                            $('#gem_form1 #imgtxt2').val("");
                        }
                    }
                }

                if (jsonvalue.error.traffic_pse_error !== null && jsonvalue.error.traffic_pse_error !== "" && jsonvalue.error.traffic_pse_error !== undefined)
                {
                    $('#gem_form1 #gem_traffic_err').html(jsonvalue.error.traffic_pse_error);
                    $('#gem_form1 #gem_traffic_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #gem_traffic_err').html("");
                }

                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#gem_form1 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    $('#gem_form1 #fwd_mobile_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #fwd_mobile_err').html("");
                }

                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#gem_form1 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#gem_form1 #fwd_email_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #fwd_email_err').html("");
                }
                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#gem_form1 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#gem_form1 #fwd_name_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#gem_form1 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#gem_form1 #fwd_desig_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#gem_form1 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#gem_form1 #fwd_add_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#gem_form1 #fwd_tel_err').html(jsonvalue.error.fwd_add_err);
                    $('#gem_form1 #fwd_tel_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #fwd_tel_err').html("");
                }
                if (jsonvalue.error.primary_user_err !== null && jsonvalue.error.primary_user_err !== "" && jsonvalue.error.primary_user_err !== undefined)
                {
                    $('#gem_form1 #primary_user_err').html(jsonvalue.error.primary_user_err);
                    $('#gem_form1 #primary_user_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #primary_user_err').html("");
                }
                if (jsonvalue.error.primary_id_err !== null && jsonvalue.error.primary_id_err !== "" && jsonvalue.error.primary_id_err !== undefined)
                {
                    $('#gem_form1 #primary_id_err').html(jsonvalue.error.primary_id_err);
                    $('#gem_form1 #primary_id_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #primary_id_err').html("");
                }
                if (jsonvalue.error.role_assign_err !== null && jsonvalue.error.role_assign_err !== "" && jsonvalue.error.role_assign_err !== undefined)
                {
                    $('#gem_form1 #role_assign_err').html(jsonvalue.error.role_assign_err);
                    $('#gem_form1 #role_assign_err').focus();
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #role_assign_err').html("");
                }
                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#gem_form1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form1 #captchaerror').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#gem_form1 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                }

                // end, code added by pr on 22ndjan18
                if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
                {
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Your request is already pending/completed so you can not fill any more request.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });

                }

                if (!error_flag) {

                } else {
                    console.log(jsonvalue.profile_values.min)


                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#gem_form2 #central_div').show();
                        $('#gem_form2 #state_div').hide();
                        $('#gem_form2 #other_div').hide();
                        $('#gem_form2 #other_text_div').addClass("display-hide");
                        var select = $('#gem_form2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#gem_form2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#gem_form2 #other_text_div').removeClass("display-hide");
                            $('#gem_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#gem_form2 #central_div').hide();
                        $('#gem_form2 #state_div').show();
                        $('#gem_form2 #other_div').hide();
                        $('#gem_form2 #other_text_div').addClass("display-hide");
                        var select = $('#gem_form2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#gem_form2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#gem_form2 #other_text_div').removeClass("display-hide");
                            $('#gem_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#gem_form2 #central_div').hide();
                        $('#gem_form2 #state_div').hide();
                        $('#gem_form2 #other_div').show();
                        $('#gem_form2 #other_text_div').addClass("display-hide");
                        var select = $('#gem_form2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#gem_form2 #other_text_div').removeClass("display-hide");
                            $('#gem_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#gem_form2 #central_div').hide();
                        $('#gem_form2 #state_div').hide();
                        $('#gem_form2 #other_div').hide();
                    }




                    $('#gem_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#gem_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#gem_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#gem_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#gem_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#gem_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#gem_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#gem_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#gem_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#gem_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    //$('#gem_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#gem_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#gem_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#gem_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#gem_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#gem_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#gem_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);

                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {
                        $('#nkn_form2 #USEditPreview').removeClass("display-hide");
                        $('#nkn_form2 #under_sec_email').val(jsonvalue.profile_values.under_sec_email);
                        $('#nkn_form2 #under_sec_name').val(jsonvalue.profile_values.under_sec_name);
                        var USstartMobile = jsonvalue.profile_values.under_sec_mobile.substring(0, 3);
                        var USendMobile = jsonvalue.profile_values.under_sec_mobile.substring(10, 13);
                        $('#nkn_form2 #under_sec_mobile').val(USstartMobile + 'XXXXXXX' + USendMobile);
                        $('#nkn_form2 #under_sec_tel').val(jsonvalue.profile_values.under_sec_tel);
                        $('#nkn_form2 #under_sec_desig').val(jsonvalue.profile_values.under_sec_desig);
                        $('#nkn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile)
                        $('#nkn_form2 #user_mobile').val(jsonvalue.profile_values.mobile);

                    } else {

                        $('#nkn_form2 #USEditPreview').addClass("display-hide");
                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#nkn_form2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                        $('#nkn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    }
                    var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#gem_form2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                    $('#gem_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    if (jsonvalue.form_details.pse === 'central_pse') {
                        $("#gem_form2 #central_pse").prop('checked', true);
                        $('#gem_form2 #central_pse_div').removeClass('display-hide');
                        $('#gem_form2 #state_pse_div').addClass('display-hide');
                        $.get('centralMinistry', {
                            orgType: 'Central'
                        }, function (response) {
                            var select = $('#gem_form2 #pse_ministry');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);
                            $.each(response, function (index, value) {
                                $('<option>').val(value).text(value).appendTo(select);
                            });
                        });
                        setTimeout(function () {
                            $("#gem_form2  #pse_ministry").val(jsonvalue.form_details.pse_ministry);
                        }, 200);
                    } else {

                        $("#gem_form2 #state_pse").prop('checked', true);
                        $('#gem_form2 #state_pse_div').removeClass('display-hide');
                        $('#gem_form2 #central_pse_div').addClass('display-hide');

                        $('#gem_form2 select[name=pse_state]').val(jsonvalue.form_details.pse_state);

                        $.get('getDistrict', {
                            user_state: jsonvalue.form_details.pse_state
                        }, function (response) {

                            var select = $('#gem_form2 #pse_district');
                            select.find('option').remove();
                            $('<option>').val("").text("-SELECT-").appendTo(select);

                            $.each(response, function (index, value) {

                                if (jsonvalue.form_details.pse_district == value)
                                {
                                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                                } else
                                {
                                    $('<option>').val(value).text(value).appendTo(select);
                                }

                            });


                        });

//                        $("#gem_form2 #pse_state").val(jsonvalue.form_details.pse_state);
//                        $("#gem_form2 #pse_district").val(jsonvalue.form_details.pse_district);
                    }
                    console.log(jsonvalue.form_details.fwd_ofc_add + "address")
                    $('#gem_form2 #fwd_ofc_name').val(jsonvalue.form_details.fwd_ofc_name);
                    $('#gem_form2 #fwd_ofc_email').val(jsonvalue.form_details.fwd_ofc_email);
                    $('#gem_form2 #fwd_ofc_tel').val(jsonvalue.form_details.fwd_ofc_tel);
                    $('#gem_form2 #fwd_ofc_mobile').val(jsonvalue.form_details.fwd_ofc_mobile);
                    $('#gem_form2 #fwd_ofc_design').val(jsonvalue.form_details.fwd_ofc_design);
                    $('#gem_form2 #fwd_ofc_add').val(jsonvalue.form_details.fwd_ofc_add);
                    $('#gem_form2 #single_dob3').val(jsonvalue.form_details.single_dob);
                    $('#gem_form2 #single_dor3').val(jsonvalue.form_details.single_dor);
                    $('#gem_form2 #single_email1').val(jsonvalue.form_details.single_email1);
                    $('#gem_form2 #single_email2').val(jsonvalue.form_details.single_email2);
                    $('#gem_form2 #domestic_traf').val(jsonvalue.form_details.domestic_traf);
                    // $('#gem_form2 #primary_user').val(jsonvalue.form_details.primary_user);

                    if (jsonvalue.form_details.primary_user == "yes")
                    {
                        $('#gem_form2 #primary_user_yes').prop('checked', true);
                        $('#gem_form2 #primary_text_id').removeClass("display-hide");
                        $('#gem_form2 #primary_user_id').val(jsonvalue.form_details.primary_user_id);
                    } else {

                        $('#gem_form2 #primary_user_no').prop('checked', true);
                    }




                    $('#gem_form2 #role_assign').val(jsonvalue.form_details.role_assign);
                    $('.edit').removeClass('display-hide');
                    $('#gem_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#gem_form2 :button[type='button']").removeAttr('disabled');
                    $("#gem_form2 #tnc").removeAttr('disabled');
                    $('#large2').modal({backdrop: 'static', keyboard: false});
                }
            }, error: function ()
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
    var emailregex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    var nameregex = /^[a-zA-Z .,]{1,50}$/;
    var mobileregex = /^[+0-9]{10,13}$/;
    var phoneregex1 = /^[0-9]{3,5}[-]([0-9]{6,15})$/;
    var addregex = /^[a-zA-Z0-9 .,-\\#\\/\\(\\)]{2,100}$/;
    var desigregex = /^[a-zA-Z0-9 \.\,\-\&]{2,50}$/;

    $('#gem_form1 #fwd_ofc_email').blur(function () {
        var hod_email = $('#gem_form1 #fwd_ofc_email').val();
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
                        $('#gem_form1 #fwd_ofc_mobile').prop('readonly', false);
                        $('#gem_form1 #fwd_ofc_mobile').val("");
                        $('#gem_form1 #fwd_ofc_name').prop('readonly', false);
                        $('#gem_form1 #fwd_ofc_name').val("");
                        $('#gem_form1 #fwd_ofc_tel').prop('readonly', false);
                        $('#gem_form1 #fwd_ofc_tel').val("");
                        $('#gem_form1 #fwd_ofc_desig').prop('readonly', false);
                        $('#gem_form1 #fwd_ofc_desig').val("");
                        $('#gem_form1 #fwd_ofc_add').prop('readonly', false);
                        $('#gem_form1 #fwd_ofc_add').val("");
                        $('#ca_design').prop('readonly', false);
                        $('#ca_design').val("");
                        $('#gem_form1 #fwd_email_err').html('This is not a valid Government email address.')

                    } else {
                        if (jsonvalue.hodDetails.email !== "") {
                            $('#gem_form1 #fwd_ofc_email').val(jsonvalue.hodDetails.email);
                            if (jsonvalue.hodDetails.email.match(emailregex))
                            {
                                flg = true;
                            } else {
                                $('#gem_form1 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            }
                        } else {
                            $('#gem_form1 #fwd_email_err').html("Enter E-Mail Address [e.g: abc.xyz@zxc.com]");
                            $('#hod_email').val("");
                        }
                        if (jsonvalue.hodDetails.mobile !== "") {
                            $('#gem_form1 #fwd_ofc_mobile').val(jsonvalue.hodDetails.mobile);
                            if (jsonvalue.hodDetails.mobile.match(mobileregex))
                            {
                                flg = true;
                                $('#gem_form1 #fwd_ofc_mobile').prop('readonly', true);
                            } else {
                                $('#gem_form1 #fwd_mobile_err').html("Enter Mobile Number [e.g: +919999999999]");
                                $('#gem_form1 #fwd_ofc_mobile').prop('readonly', false);
                            }
                        } else {
                            $('#gem_form1 #fwd_ofc_mobile').val("");
                            $('#gem_form1 #fwd_ofc_mobile').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.cn !== "") {
                            $('#gem_form1 #fwd_ofc_name').val(jsonvalue.hodDetails.cn);
                            if (jsonvalue.hodDetails.cn.match(nameregex))
                            {
                                flg = true;
                                $('#gem_form1 #fwd_ofc_name').prop('readonly', true);
                            } else {
                                $('#gem_form1 #fwd_name_err').html("Enter Name [characters,dot(.) and whitespaces]");
                                $('#gem_form1 #fwd_ofc_name').prop('readonly', false);
                            }
                        } else {
                            $('#gem_form1 #fwd_ofc_name').val("");
                            $('#gem_form1 #fwd_ofc_name').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.ophone !== "") {
                            $('#gem_form1 #fwd_ofc_tel').val(jsonvalue.hodDetails.ophone);

                            if (jsonvalue.hodDetails.ophone.match(phoneregex1))
                            {
                                flg = true;
                                $('#gem_form1 #fwd_ofc_tel').prop('readonly', true);
                            } else {
                                $('#gem_form1 #fwd_tel_err').html("Enter Reporting/Nodal/Forwarding Officer Telephone Number [STD CODE(3-5 DIGIT)-TELEPHONE(8-15 DIGIT)]");
                                $('#gem_form1 #fwd_ofc_tel').prop('readonly', false);
                            }
                        } else {
                            $('#gem_form1 #fwd_ofc_tel').val("");
                            $('#gem_form1 #fwd_ofc_tel').prop('readonly', false);
                        }
                        if (jsonvalue.hodDetails.desig !== "" && jsonvalue.hodDetails.desig !== "null") {

                            $('#gem_form1 #fwd_ofc_desig').val(jsonvalue.hodDetails.desig);
                            if (jsonvalue.hodDetails.desig.match(desigregex))
                            {
                                flg = true;
                                $('#gem_form1 #fwd_ofc_desig').prop('readonly', true);
                            } else {
                                $('#gem_form1 #fwd_desig_err').html("Enter Reporting/Nodal/Forwarding Officer Designation [characters,digits,whitespaces and [. , - &]]]");
                                $('#gem_form1 #fwd_ofc_desig').prop('readonly', false);
                            }
                        } else {
                            $('#gem_form1 #fwd_ofc_desig').val("");
                            $('#gem_form1 #fwd_ofc_desig').prop('readonly', false);
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
    $('#gem_form2').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#gem_form2 :disabled").removeAttr('disabled');
        $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#gem_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#gem_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#gem_form2').serializeObject());
        $('#gem_preview_tab #user_email').prop('disabled', 'true'); // 20th march


        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('#gem_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "gem_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.central_pse_error !== null && jsonvalue.error.central_pse_error !== "" && jsonvalue.error.central_pse_error !== undefined)
                {
                    $('#gem_form2 #central_pse_err').html(jsonvalue.error.central_pse_error);
                    $('#gem_form2 #central_pse_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #central_pse_err').html("");
                }
                if (jsonvalue.error.state_pse_error !== null && jsonvalue.error.state_pse_error !== "" && jsonvalue.error.state_pse_error !== undefined)
                {
                    $('#gem_form2 #pse_state_err').html(jsonvalue.error.state_pse_error);
                    $('#gem_form2 #pse_state_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #pse_state_err').html("");
                }
                if (jsonvalue.error.district_pse_error !== null && jsonvalue.error.district_pse_error !== "" && jsonvalue.error.district_pse_error !== undefined)
                {
                    $('#gem_form2 #pse_district_err').html(jsonvalue.error.district_pse_error);
                    $('#gem_form2 #pse_district_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #pse_district_err').html("");
                }

                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#gem_form2 #gem_dob_err').html("");
                    $('#gem_form2 #gem_dob_err').html(jsonvalue.error.dob_err);
                    $('#gem_form2 #gem_dob_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #gem_dob_err').html("");
                }


                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#gem_form2 #gem_dor_err').html("");
                    $('#gem_form2 #gem_dor_err').html(jsonvalue.error.dor_err);
                    $('#gem_form2 #gem_dor_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #gem_dor_err').html("");
                }



                if (jsonvalue.error.email1_pse_error !== null && jsonvalue.error.email1_pse_error !== "" && jsonvalue.error.email1_pse_error !== undefined)
                {
                    $('#gem_form2 #gem_email1_err').html("");
                    $('#gem_form2 #gem_email1_err').html(jsonvalue.error.email1_pse_error);
                    $('#gem_form2 #gem_email1_err').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#gem_form2 #gem_email1_err').html("")
                        } else {
                            $('#gem_form2 #gem_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#gem_form2 #gem_email1_err').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.email2_pse_error !== null && jsonvalue.error.email2_pse_error !== "" && jsonvalue.error.email2_pse_error !== undefined)
                {
                    $('#gem_form2 #gem_email2_err').html("");
                    $('#gem_form2 #gem_email2_err').html(jsonvalue.error.email2_pse_error);
                    $('#gem_form2 #gem_email2_err').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#gem_form2 #gem_email2_err').html("")
                        } else {
                            $('#gem_form2 #gem_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#gem_form2 #gem_email2_err').focus();
                            error_flag = false;
                        }
                    }
                }


                if (jsonvalue.error.traffic_pse_error !== null && jsonvalue.error.traffic_pse_error !== "" && jsonvalue.error.traffic_pse_error !== undefined)
                {
                    $('#gem_form2 #gem_traffic_err').html("");
                    $('#gem_form2 #gem_traffic_err').html(jsonvalue.error.traffic_pse_error);
                    $('#gem_form2 #gem_traffic_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #gem_traffic_err').html("");
                }


                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#gem_form2 #useremployment_error').focus();
                    $('#gem_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#gem_form2 #minerror').focus();
                    $('#gem_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#gem_form2 #deperror').focus();
                    $('#gem_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#gem_form2 #other_dept').focus();
                    $('#gem_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#gem_form2 #smierror').focus();
                    $('#gem_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#gem_form2 #state_error').focus();
                    $('#gem_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#gem_form2 #org_error').focus();
                    $('#gem_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#gem_form2 #ca_design').focus();
                    $('#gem_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#gem_form2 #hod_name').focus();
                    $('#gem_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#gem_form2 #hod_mobile').focus();
                    $('#gem_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#gem_form2 #hod_email').focus();
                    $('#gem_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#gem_form2 #hod_tel').focus();
                    $('#gem_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #hodtel_error').html("");
                }
                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#gem_form2 #fwd_ofc_mobile').focus();
                    $('#gem_form2 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    //$('#gem_form2 #fwd_mobile_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_mobile_err').html("");
                }

                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#gem_form2 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#gem_form2 #fwd_ofc_email').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_email_err').html("");
                }
                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#gem_form2 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#gem_form2 #fwd_ofc_name').focus();
                    error_flag = false;

                } else {
                    $('#gem_form1 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#gem_form2 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#gem_form2 #fwd_ofc_design').focus();
                    error_flag = false;
                    $('#gem_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form2 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#gem_form2 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#gem_form2 #fwd_ofc_add').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#gem_form2 #fwd_tel_err').html(jsonvalue.error.fwd_add_err);
                    $('#gem_form2 #fwd_ofc_tel').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_tel_err').html("");
                }



                if (jsonvalue.error.primary_user_error !== null && jsonvalue.error.primary_user_error !== "" && jsonvalue.error.primary_user_error !== undefined)
                {
                    $('#gem_form2 #primary_user_err').html(jsonvalue.error.primary_user_error);
                    $('#gem_form2 #primary_user_err').focus();
                    error_flag = false;
                    $('#gem_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form2 #imgtxt2').val("");
                } else {
                    $('#gem_form2 #primary_user_err').html("");
                }
                if (jsonvalue.error.primary_id_err !== null && jsonvalue.error.primary_id_err !== "" && jsonvalue.error.primary_id_err !== undefined)
                {
                    $('#gem_form2 #primary_id_err').html(jsonvalue.error.primary_id_err);
                    $('#gem_form2 #primary_id_err').focus();
                    error_flag = false;
                    $('#gem_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form2 #imgtxt2').val("");
                } else {
                    $('#gem_form2 #primary_id_err').html("");
                }
                if (jsonvalue.error.role_assign_err !== null && jsonvalue.error.role_assign_err !== "" && jsonvalue.error.role_assign_err !== undefined)
                {
                    $('#gem_form2 #role_assign_err').html(jsonvalue.error.role_assign_err);
                    $('#gem_form2 #role_assign_err').focus();
                    error_flag = false;
                    $('#gem_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form2 #imgtxt2').val("");
                } else {
                    $('#gem_form2 #role_assign_err').html("");
                }
                // profile on 20th march

                if (error_flag) {
                    $('#large2').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'gem'},
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
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });
    $('#gem_form2 .edit').click(function () {
        var pse_state = $('#gem_preview_tab #pse_state').val();
        var pse_district = $('#gem_preview_tab #pse_district').val();
        var employment = $('#gem_preview_tab #user_employment').val();
        var min = $('#gem_preview_tab #min').val();
        var dept = $('#gem_preview_tab #dept').val();
        var statecode = $('#gem_preview_tab #stateCode').val();
        var Smi = $('#gem_preview_tab #Smi').val();
        var Org = $('#gem_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#gem_preview_tab #min');
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
                var select = $('#gem_preview_tab #dept');
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
                $('#gem_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#gem_preview_tab #stateCode');
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
                var select = $('#gem_preview_tab #Smi');
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
                $('#gem_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#gem_preview_tab #Org');
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
                $('#gem_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#gem_preview_tab #central_div').hide();
            $('#gem_preview_tab #state_div').hide();
            $('#gem_preview_tab #other_div').hide();
        }
        $('#gem_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#gem_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#gem_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#gem_preview_tab #forwarding_div').find('input, textarea, button, select')
        $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march

        // $('#gem_preview_tab #primary_div').find('input, textarea, button, select,radio').prop('disabled', 'false');
        $('#gem_preview_tab #primary_div').find('input, textarea, button, select,radio').prop('disabled', 'false');
        $('#gem_preview_tab #primary_div').find('input, textarea, button, select,radio').removeAttr('disabled');
        $('#gem_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');

        $.get('getDistrict', {
            user_state: pse_state
        }, function (response) {

            var select = $('#gem_preview_tab #pse_district');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {
                console.log("pse_district" + pse_district + "value" + value)
                if (pse_district === value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }

            });


        });


        $(this).addClass('display-hide');
    });
    $('#gem_form2 #confirm').click(function () {

        $("#gem_form2 :disabled").removeAttr('disabled');
        $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#gem_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#gem_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#gem_form2').serializeObject());
        $('#gem_preview_tab #user_email').prop('disabled', 'true'); // 20th march

        $.ajax({
            type: "POST",
            url: "gem_tab2",
            data: {data: data, action_type: "validate"},
            datatype: JSON,
            success: function (data)
            {

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.central_pse_error !== null && jsonvalue.error.central_pse_error !== "" && jsonvalue.error.central_pse_error !== undefined)
                {
                    $('#gem_form2 #central_pse_err').html(jsonvalue.error.central_pse_error);
                    $('#gem_form2 #central_pse_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #central_pse_err').html("");
                }
                if (jsonvalue.error.state_pse_error !== null && jsonvalue.error.state_pse_error !== "" && jsonvalue.error.state_pse_error !== undefined)
                {
                    $('#gem_form2 #pse_state_err').html(jsonvalue.error.state_pse_error);
                    $('#gem_form2 #pse_state_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #pse_state_err').html("");
                }
                if (jsonvalue.error.district_pse_error !== null && jsonvalue.error.district_pse_error !== "" && jsonvalue.error.district_pse_error !== undefined)
                {
                    $('#gem_form2 #pse_district_err').html(jsonvalue.error.district_pse_error);
                    $('#gem_form2 #pse_district_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #pse_district_err').html("");
                }

                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#gem_form2 #gem_dob_err').html("");
                    $('#gem_form2 #gem_dob_err').html(jsonvalue.error.dob_err);
                    $('#gem_form2 #gem_dob_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #gem_dob_err').html("");
                }


                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#gem_form2 #gem_dor_err').html("");
                    $('#gem_form2 #gem_dor_err').html(jsonvalue.error.dor_err);
                    $('#gem_form2 #gem_dor_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #gem_dor_err').html("");
                }



                if (jsonvalue.error.email1_pse_error !== null && jsonvalue.error.email1_pse_error !== "" && jsonvalue.error.email1_pse_error !== undefined)
                {
                    $('#gem_form2 #gem_email1_err').html("");
                    $('#gem_form2 #gem_email1_err').html(jsonvalue.error.email1_pse_error);
                    $('#gem_form2 #gem_email1_err').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#gem_form2 #gem_email1_err').html("")
                        } else {
                            $('#gem_form2 #gem_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#gem_form2 #gem_email1_err').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.email2_pse_error !== null && jsonvalue.error.email2_pse_error !== "" && jsonvalue.error.email2_pse_error !== undefined)
                {
                    $('#gem_form2 #gem_email2_err').html("");
                    $('#gem_form2 #gem_email2_err').html(jsonvalue.error.email2_pse_error);
                    $('#gem_form2 #gem_email2_err').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#gem_form2 #gem_email2_err').html("")
                        } else {
                            $('#gem_form2 #gem_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#gem_form2 #gem_email2_err').focus();
                            error_flag = false;
                        }
                    }
                }


                if (jsonvalue.error.traffic_pse_error !== null && jsonvalue.error.traffic_pse_error !== "" && jsonvalue.error.traffic_pse_error !== undefined)
                {
                    $('#gem_form2 #gem_traffic_err').html("");
                    $('#gem_form2 #gem_traffic_err').html(jsonvalue.error.traffic_pse_error);
                    $('#gem_form2 #gem_traffic_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_form2 #gem_traffic_err').html("");
                }
                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#gem_form2 #useremployment_error').focus();
                    $('#gem_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#gem_form2 #minerror').focus();
                    $('#gem_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#gem_form2 #deperror').focus();
                    $('#gem_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#gem_form2 #other_dept').focus();
                    $('#gem_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#gem_form2 #smierror').focus();
                    $('#gem_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#gem_form2 #state_error').focus();
                    $('#gem_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#gem_form2 #org_error').focus();
                    $('#gem_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#gem_form2 #ca_design').focus();
                    $('#gem_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#gem_form2 #hod_name').focus();
                    $('#gem_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#gem_form2 #hod_mobile').focus();
                    $('#gem_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#gem_form2 #hod_email').focus();
                    $('#gem_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #hodemail_error').html("");
                }

                if (jsonvalue.error.fwd_mobile_err !== null && jsonvalue.error.fwd_mobile_err !== "" && jsonvalue.error.fwd_mobile_err !== undefined)
                {
                    $('#gem_form2 #fwd_ofc_mobile').focus();
                    $('#gem_form2 #fwd_mobile_err').html(jsonvalue.error.fwd_mobile_err);
                    //$('#gem_form2 #fwd_mobile_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_mobile_err').html("");
                }

                if (jsonvalue.error.fwd_email_err !== null && jsonvalue.error.fwd_email_err !== "" && jsonvalue.error.fwd_email_err !== undefined)
                {
                    $('#gem_form2 #fwd_email_err').html(jsonvalue.error.fwd_email_err);
                    $('#gem_form2 #fwd_ofc_email').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_email_err').html("");
                }
                if (jsonvalue.error.fwd_name_err !== null && jsonvalue.error.fwd_name_err !== "" && jsonvalue.error.fwd_name_err !== undefined)
                {

                    $('#gem_form2 #fwd_name_err').html(jsonvalue.error.fwd_name_err);
                    $('#gem_form2 #fwd_ofc_name').focus();
                    error_flag = false;

                } else {
                    $('#gem_form1 #fwd_name_err').html("");
                }
                if (jsonvalue.error.fwd_desig_err !== null && jsonvalue.error.fwd_desig_err !== "" && jsonvalue.error.fwd_desig_err !== undefined)
                {
                    $('#gem_form2 #fwd_desig_err').html(jsonvalue.error.fwd_desig_err);
                    $('#gem_form2 #fwd_ofc_design').focus();
                    error_flag = false;
                    $('#gem_form2 #captcha3').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#gem_form1 #imgtxt2').val("");
                } else {
                    $('#gem_form2 #fwd_desig_err').html("");
                }
                if (jsonvalue.error.fwd_add_err !== null && jsonvalue.error.fwd_add_err !== "" && jsonvalue.error.fwd_add_err !== undefined)
                {
                    $('#gem_form2 #fwd_add_err').html(jsonvalue.error.fwd_add_err);
                    $('#gem_form2 #fwd_ofc_add').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_add_err').html("");
                }
                if (jsonvalue.error.fwd_tel_err !== null && jsonvalue.error.fwd_tel_err !== "" && jsonvalue.error.fwd_tel_err !== undefined)
                {
                    $('#gem_form2 #fwd_tel_err').html(jsonvalue.error.fwd_add_err);
                    $('#gem_form2 #fwd_ofc_tel').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #fwd_tel_err').html("");
                }

                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#gem_form2 #hod_tel').focus();
                    $('#gem_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#gem_form2 #hodtel_error').html("");
                }
                if (jsonvalue.error.primary_user_error !== null && jsonvalue.error.primary_user_error !== "" && jsonvalue.error.primary_user_error !== undefined)
                {
                    $('#gem_form2 #primary_user_err').html(jsonvalue.error.primary_user_error);
                    $('#gem_form2 #primary_user_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #primary_user_err').html("");
                }
                if (jsonvalue.error.primary_id_err !== null && jsonvalue.error.primary_id_err !== "" && jsonvalue.error.primary_id_err !== undefined)
                {
                    $('#gem_form2 #primary_id_err').html(jsonvalue.error.primary_id_err);
                    $('#gem_form2 #primary_id_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #primary_id_err').html("");
                }
                if (jsonvalue.error.role_assign_err !== null && jsonvalue.error.role_assign_err !== "" && jsonvalue.error.role_assign_err !== undefined)
                {
                    $('#gem_form2 #role_assign_err').html(jsonvalue.error.role_assign_err);
                    $('#gem_form2 #role_assign_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_form2 #role_assign_err').html("");
                }
                // profile on 20th march
                if (!error_flag) {
                    $("#gem_form2 :disabled").removeAttr('disabled');
                    $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#gem_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    // $("#gem_form2 #tnc").removeAttr('disabled');
                } else {
                    if ($('#gem_form2 #tnc').is(":checked"))
                    {
//                        if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#gem_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#gem_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#gem_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } else {
                        $('#gem_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#gem_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#gem_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        //}
//                        var startMobile = jsonvalue.profile_values.fwd_ofc_mobile.substring(0, 3);
//                        var endMobile = jsonvalue.profile_values.fwd_ofc_mobile.substring(10, 13);
//                        $('#gem_form_confirm #fill_hod_name').html(jsonvalue.profile_values.fwd_ofc_name)
//                        $('#gem_form_confirm #fill_hod_email').html(jsonvalue.profile_values.fwd_ofc_email)
//                        $('#gem_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile)
                        $('#gem_form2 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        $('#gem_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');

                    } else {
                        $('#gem_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        //$('#gem_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        $("#gem_form2 #tnc").removeAttr('disabled');
                        $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        //$('#gem_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march

                    }
                }

            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });
    $('#gem_form_confirm #confirmYes').click(function () {
        $('#gem_form2').submit();
        $('#stack3').modal('toggle');
    });
    $(document).on('click', '#gem_preview .edit', function () {
        //$('#gem_preview .edit').click(function () {
        var pse_state = $('#gem_preview_tab #pse_state').val();
        var pse_district = $('#gem_preview_tab #pse_district').val();
        var employment = $('#gem_preview_tab #user_employment').val();
        var min = $('#gem_preview_tab #min').val();
        var dept = $('#gem_preview_tab #dept').val();
        var statecode = $('#gem_preview_tab #stateCode').val();
        var Smi = $('#gem_preview_tab #Smi').val();
        var Org = $('#gem_preview_tab #Org').val();

        $.get('getDistrict', {
            user_state: pse_state
        }, function (response) {

            var select = $('#gem_preview_tab #pse_district');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);

            $.each(response, function (index, value) {

                if (pse_district === value)
                {
                    $('<option selected="selected">').val(value).text(value).appendTo(select);
                } else
                {
                    $('<option>').val(value).text(value).appendTo(select);
                }

            });


        });
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#gem_preview_tab #min');
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
                var select = $('#gem_preview_tab #dept');
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
                $('#gem_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#gem_preview_tab #stateCode');
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
                var select = $('#gem_preview_tab #Smi');
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
                $('#gem_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#gem_preview_tab #Org');
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
                $('#gem_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#gem_preview_tab #central_div').hide();
            $('#gem_preview_tab #state_div').hide();
            $('#gem_preview_tab #other_div').hide();
        }
        $('#gem_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#gem_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#gem_preview_tab #forwarding_div').find('input, textarea, button, select').prop('disabled', 'true');
        // $('#gem_preview_tab #primary_div').find('input, textarea, button, select,radio').prop('disabled', 'false');
        $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#gem_preview_tab #primary_div').find('input, textarea, button, select,radio').prop('disabled', 'false');
        $('#gem_preview_tab #primary_div').find('input, textarea, button, select,radio').removeAttr('disabled');
        $('#gem_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#gem_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#gem_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#gem_preview #confirm', function () {
        /// $('#gem_preview #confirm').click(function () {
        $('#gem_preview').submit();
        // $('#gem_preview_form').modal('toggle');
    });
    $('#gem_preview').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#gem_preview :disabled").removeAttr('disabled');
        $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march     
        var data = JSON.stringify($('#gem_preview').serializeObject());

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "gem_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);


                var error_flag = true;
                if (jsonvalue.error.central_pse_error !== null && jsonvalue.error.central_pse_error !== "" && jsonvalue.error.central_pse_error !== undefined)
                {
                    $('#gem_preview #central_pse_err').html(jsonvalue.error.central_pse_error);
                    $('#gem_preview #central_pse_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_preview #central_pse_err').html("");
                }
                if (jsonvalue.error.state_pse_error !== null && jsonvalue.error.state_pse_error !== "" && jsonvalue.error.state_pse_error !== undefined)
                {
                    $('#gem_preview #pse_state_err').html(jsonvalue.error.state_pse_error);
                    $('#gem_preview #pse_state_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_preview #pse_state_err').html("");
                }
                if (jsonvalue.error.district_pse_error !== null && jsonvalue.error.district_pse_error !== "" && jsonvalue.error.district_pse_error !== undefined)
                {
                    $('#gem_preview #pse_district_err').html(jsonvalue.error.district_pse_error);
                    $('#gem_preview #pse_district_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_preview #pse_district_err').html("");
                }


                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#gem_preview #gem_dob_err').html("");
                    $('#gem_preview #gem_dob_err').html(jsonvalue.error.dob_err);
                    $('#gem_preview #gem_dob_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_preview #gem_dob_err').html("");
                }


                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#gem_preview #gem_dor_err').html("");
                    $('#gem_preview #gem_dor_err').html(jsonvalue.error.dor_err);
                    $('#gem_preview #gem_dor_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_preview #gem_dor_err').html("");
                }



                if (jsonvalue.error.email1_pse_error !== null && jsonvalue.error.email1_pse_error !== "" && jsonvalue.error.email1_pse_error !== undefined)
                {
                    $('#gem_preview #gem_email1_err').html("");
                    $('#gem_preview #gem_email1_err').html(jsonvalue.error.email1_pse_error);
                    $('#gem_preview #gem_email1_err').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#gem_preview #gem_email1_err').html("")
                        } else {
                            $('#gem_preview #gem_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#gem_preview #gem_email1_err').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.email2_pse_error !== null && jsonvalue.error.email2_pse_error !== "" && jsonvalue.error.email2_pse_error !== undefined)
                {
                    $('#gem_preview #gem_email2_err').html("");
                    $('#gem_preview #gem_email2_err').html(jsonvalue.error.email2_pse_error);
                    $('#gem_preview #gem_email2_err').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#gem_preview #gem_email2_err').html("")
                        } else {
                            $('#gem_preview #gem_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#gem_preview #gem_email2_err').focus();
                            error_flag = false;
                        }
                    }
                }


                if (jsonvalue.error.traffic_pse_error !== null && jsonvalue.error.traffic_pse_error !== "" && jsonvalue.error.traffic_pse_error !== undefined)
                {
                    $('#gem_preview #gem_traffic_err').html("");
                    $('#gem_preview #gem_traffic_err').html(jsonvalue.error.traffic_pse_error);
                    $('#gem_preview #gem_traffic_err').focus();
                    error_flag = false;
                } else {
                    $('#gem_preview #gem_traffic_err').html("");
                }

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#gem_preview #useremployment_error').focus();
                    $('#gem_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#gem_preview #minerror').focus();
                    $('#gem_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#gem_preview #deperror').focus();
                    $('#gem_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#gem_preview #other_dept').focus();
                    $('#gem_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#gem_preview #smierror').focus();
                    $('#gem_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#gem_preview #state_error').focus();
                    $('#gem_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#gem_preview #org_error').focus();
                    $('#gem_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#gem_preview #org_error').html("");
                }

                if (jsonvalue.error.primary_user_error !== null && jsonvalue.error.primary_user_error !== "" && jsonvalue.error.primary_user_error !== undefined)
                {

                    $('#gem_preview #primary_user_err').html(jsonvalue.error.primary_user_error);
                    $('#gem_preview #primary_user_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_preview #primary_user_err').html("");
                }
                if (jsonvalue.error.primary_id_err !== null && jsonvalue.error.primary_id_err !== "" && jsonvalue.error.primary_id_err !== undefined)
                {
                    $('#gem_preview #primary_id_err').html(jsonvalue.error.primary_id_err);
                    $('#gem_preview #primary_id_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_preview #primary_id_err').html("");
                }
                if (jsonvalue.error.role_assign_err !== null && jsonvalue.error.role_assign_err !== "" && jsonvalue.error.role_assign_err !== undefined)
                {
                    $('#gem_preview #role_assign_err').html(jsonvalue.error.role_assign_err);
                    $('#gem_preview #role_assign_err').focus();
                    error_flag = false;

                } else {
                    $('#gem_preview #role_assign_err').html("");
                }
                // profile 20th march 

                if (!error_flag)
                {

                    $("#gem_preview :disabled").removeAttr('disabled');
                    $('#gem_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#gem_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#gem_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
    // nkn single form submission

    $('#nkn_single_form1').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        var data = JSON.stringify($('#nkn_single_form1').serializeObject());
        var submit = document.activeElement.getAttribute('value');
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "nkn_single_tab1",
            data: {data: data, request_type: submit, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                // resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.dup_err !== null && jsonvalue.error.dup_err !== "" && jsonvalue.error.dup_err !== undefined)
                {
                    $('#nkn_single_form1 #single_email1_err').html(jsonvalue.error.dup_err)
                    error_flag = false;
                }
                if (jsonvalue.error.inst_name_error !== null && jsonvalue.error.inst_name_error !== "" && jsonvalue.error.inst_name_error !== undefined)
                {

                    $('#nkn_single_form1 #inst_name_err').html(jsonvalue.error.inst_name_error)
                    $('#nkn_single_form1 #inst_name').focus();
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {

                    $('#nkn_single_form1 #inst_name_err').html("")
                }
                if (jsonvalue.error.inst_id_error !== null && jsonvalue.error.inst_id_error !== "" && jsonvalue.error.inst_id_error !== undefined)
                {
                    $('#nkn_single_form1 #inst_id_err').html(jsonvalue.error.inst_id_error)
                    $('#nkn_single_form1 #inst_id').focus();
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {

                    $('#nkn_single_form #inst_id_err').html("")
                }

                if (jsonvalue.error.project_name_error !== null && jsonvalue.error.project_name_error !== "" && jsonvalue.error.project_name_error !== undefined)
                {

                    $('#nkn_single_form1 #nkn_project_err').html(jsonvalue.error.project_name_error)
                    $('#nkn_single_form1 #nkn_project').focus();
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {

                    $('#nkn_single_form1 #nkn_project_err').html("")
                }
                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#nkn_single_form1 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#nkn_single_form1 #single_email1').focus();
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#nkn_single_form1 #single_email1_err').html("")
                        } else {
                            $('#nkn_single_form1 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#nkn_single_form1 #single_email1').focus();
                            error_flag = false;
                            $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                            $('#nkn_single_form1 #imgtxt').val("");
                        }
                    }
                }

                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#nkn_single_form1 #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#nkn_single_form1 #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#nkn_single_form1 #single_email2_err').html("")
                        } else {
                            $('#nkn_single_form1 #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#nkn_single_form1 #single_email2').focus();
                            error_flag = false;
                            $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                            $('#nkn_single_form1 #imgtxt').val("");
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#nkn_single_form1 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#nkn_single_form1 #single_dob').focus();
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {

                    $('#nkn_single_form1 #single_dob_err').html("")
                }

                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#nkn_single_form1 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#nkn_single_form1 #single_dob').focus();
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {

                    $('#nkn_single_form1 #single_dob_err').html("")
                }

                if (jsonvalue.error.nkn_req_for !== null && jsonvalue.error.nkn_req_for !== "" && jsonvalue.error.nkn_req_for !== undefined)
                {
                    $('#nkn_single_form1 #nkn_req_for_err').html(jsonvalue.error.nkn_req_for)
                    $('#nkn_single_form1 #nkn_req_for_err').focus();
                } else {
                    $('#nkn_single_form1 #nkn_req_for_err').html("")
                }

                if (jsonvalue.error.nkn_id_type !== null && jsonvalue.error.nkn_id_type !== "" && jsonvalue.error.nkn_id_type !== undefined)
                {
                    $('#nkn_single_form1 #nkn_id_type_err').html(jsonvalue.error.nkn_id_type)
                    $('#nkn_single_form1 #nkn_id_type_err').focus();
                } else {
                    $('#nkn_single_form1 #nkn_id_type_err').html("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#nkn_single_form1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                } else {
                    $('#nkn_single_form1 #captchaerror').html("")
                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#nkn_single_form1 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_single_form1 #imgtxt').val("");
                }


                // end, code added by pr on 22ndjan18

                if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
                {
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 18px;'>Your request is already pending/completed so you can not fill any more request.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });

//                    $('#nkn_single_form1 #email_info_error').html(jsonvalue.error.email_info_error);
//                    error_flag = false;
//                    $('#nkn_single_form1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                    $('#nkn_single_form1 #imgtxt').val("");
                }
                if (!error_flag) {
                } else {

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#nkn_form2 #central_div').show();
                        $('#nkn_form2 #state_div').hide();
                        $('#nkn_form2 #other_div').hide();
                        $('#nkn_form2 #other_text_div').addClass("display-hide");
                        var select = $('#nkn_form2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#nkn_form2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#nkn_form2 #other_text_div').removeClass("display-hide");
                            $('#nkn_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#nkn_form2 #central_div').hide();
                        $('#nkn_form2 #state_div').show();
                        $('#nkn_form2 #other_div').hide();
                        $('#nkn_form2 #other_text_div').addClass("display-hide");
                        var select = $('#nkn_form2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#nkn_form2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#nkn_form2 #other_text_div').removeClass("display-hide");
                            $('#nkn_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#nkn_form2 #central_div').hide();
                        $('#nkn_form2 #state_div').hide();
                        $('#nkn_form2 #other_div').show();
                        $('#nkn_form2 #other_text_div').addClass("display-hide");
                        var select = $('#nkn_form2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#nkn_form2 #other_text_div').removeClass("display-hide");
                            $('#nkn_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#nkn_form2 #central_div').hide();
                        $('#nkn_form2 #state_div').hide();
                        $('#nkn_form2 #other_div').hide();
                    }


                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {
                        $('#nkn_form2 #USEditPreview').removeClass("display-hide");
                        $('#nkn_form2 #under_sec_email').val(jsonvalue.profile_values.under_sec_email);
                        $('#nkn_form2 #under_sec_name').val(jsonvalue.profile_values.under_sec_name);
                        var USstartMobile = jsonvalue.profile_values.under_sec_mobile.substring(0, 3);
                        var USendMobile = jsonvalue.profile_values.under_sec_mobile.substring(10, 13);
                        $('#nkn_form2 #under_sec_mobile').val(USstartMobile + 'XXXXXXX' + USendMobile);
                        $('#nkn_form2 #under_sec_tel').val(jsonvalue.profile_values.under_sec_tel);
                        $('#nkn_form2 #under_sec_desig').val(jsonvalue.profile_values.under_sec_desig);
                        $('#nkn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile)
                        $('#nkn_form2 #user_mobile').val(jsonvalue.profile_values.mobile);

                    } else {

                        $('#nkn_form2 #USEditPreview').addClass("display-hide");
                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#nkn_form2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                        $('#nkn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    }
                    console.log(jsonvalue.form_details.nkn_id_type);
                    $('#nkn_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#nkn_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#nkn_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#nkn_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#nkn_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#nkn_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#nkn_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#nkn_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#nkn_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#nkn_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
//                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
//                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
//                    $('#nkn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    //$('#nkn_form2 #user_mobile').val(jsonvalue.profile_values.mobile);
                    $('#nkn_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#nkn_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#nkn_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#nkn_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#nkn_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
//                    var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
//                    var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
//                    $('#nkn_form2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                    $('#nkn_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#nkn_form2 #inst_name').val(jsonvalue.form_details.inst_name);
                    $('#nkn_form2 #inst_id').val(jsonvalue.form_details.inst_id);
                    $('#nkn_form2 #nkn_project').val(jsonvalue.form_details.nkn_project);
                    $('#nkn_form2 #single_dob5').val(jsonvalue.form_details.single_dob);
                    $('#nkn_form2 #single_dor5').val(jsonvalue.form_details.single_dor);
                    $('#nkn_form2 #single_email1').val(jsonvalue.form_details.single_email1);
                    $('#nkn_form2 #single_email2').val(jsonvalue.form_details.single_email2);
                    $('#nkn_form2 #nkn_req_for').val(jsonvalue.form_details.nkn_req_for);
                    $('#nkn_form2 #nkn_id_type').val(jsonvalue.form_details.nkn_id_type);
                    $('#nkn_form2 #confirm').val('nkn_single');
                    $('.edit').removeClass('display-hide');
                    $('#nkn_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#nkn_form2 :button[type='button']").removeAttr('disabled');
                    $("#nkn_form2 #tnc").removeAttr('disabled');
                    $('#nkn_single_preview').removeClass('display-hide');
                    $('#nkn_bulk_preview').addClass('display-hide');
                    $('#large3').modal({backdrop: 'static', keyboard: false});
                }
            }, error: function ()
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
    $('#nkn_bulk_form1').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        var data = JSON.stringify($('#nkn_bulk_form1').serializeObject());
        var cert = $('#cert1').val();

        var submit = document.activeElement.getAttribute('value');

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "nkn_bulk_tab1",
            data: {data: data, request_type: submit, cert: cert, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 23rdjan18


                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.inst_name_error !== null && jsonvalue.error.inst_name_error !== "" && jsonvalue.error.inst_name_error !== undefined)
                {
                    $('#nkn_bulk_form1 #inst_name_err').html(jsonvalue.error.inst_name_error)
                    $('#nkn_bulk_form1 #inst_name').focus();
                    error_flag = false;
                    $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_bulk_form1 #imgtxt1').val("");
                } else {

                    $('#nkn_bulk_form1 #inst_name_err').html("")
                }
                if (jsonvalue.error.inst_id_error !== null && jsonvalue.error.inst_id_error !== "" && jsonvalue.error.inst_id_error !== undefined)
                {
                    $('#nkn_bulk_form1 #inst_id_err').html(jsonvalue.error.inst_id_error)
                    $('#nkn_bulk_form1 #inst_id').focus();
                    error_flag = false;
                    $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_bulk_form1 #imgtxt1').val("");
                } else {

                    $('#nkn_bulk_form1 #inst_id_err').html("")
                }
                if (jsonvalue.error.project_name_error !== null && jsonvalue.error.project_name_error !== "" && jsonvalue.error.project_name_error !== undefined)
                {
                    $('#nkn_bulk_form1 #nkn_project_err').html(jsonvalue.error.project_name_error)
                    $('#nkn_bulk_form1 #nkn_project_err').focus();
                    error_flag = false;
                    $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_bulk_form1 #imgtxt1').val("");
                } else {

                    $('#nkn_bulk_form1 #nkn_project_err').html("")
                }
                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined) {
                    $('#nkn_bulk_form1 #file_err1').html(jsonvalue.error.file_err)
                    $('#nkn_bulk_form1 #file_err1').focus();
                    error_flag = false;
                    $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_bulk_form1 #imgtxt1').val("");
                }

                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                {
                    $('#nkn_bulk_form1 #file_err1').html(jsonvalue.error.file_err)
                    error_flag = false;
                } else {
                    $('#nkn_bulk_form1 #file_err1').html("")
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {
                    $('#nkn_bulk_form1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_bulk_form1 #imgtxt1').val("");
                } else {
                    $('#nkn_bulk_form1 #captchaerror').html("")
                }

                // start, code added by pr on 23rdjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#nkn_bulk_form1 #captcha2').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#nkn_bulk_form1 #imgtxt1').val("");
                }
                // end, code added by pr on 23rdjan18

                if (jsonvalue.error.email_info_error !== null && jsonvalue.error.email_info_error !== "" && jsonvalue.error.email_info_error !== undefined)
                {
                    error_flag = false;
                    bootbox.dialog({
                        message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> You may register only for the following services :-</p><ul><li> Email Service</li> <li>VPN Service</li> <li>Security Audit Service</li> <li>e-Sampark Service</li><li>Cloud Service</li><li>Domain Registration Service</li><li>Firewall Service</li><li>Reservation for video conferencing Service</li> <li>Web Application Firewall services</li></ul>"
                                + "<p style='text-align: center; font-size: 18px;'>To register for other services, please log in with your government email service(NIC) email address.</p>",
                        title: "",
                        buttons: {
                            cancel: {
                                label: "OK",
                                className: 'btn-info'
                            }
                        }
                    });
                }

                if (error_flag) {
                    $('#email_head1').addClass('display-hide');
                    $('#email_head2').addClass('display-hide');
                    $('#nknbulk-1').addClass('display-hide');
                    $('#nknbulk-2').removeClass('display-hide');
                } else {
                    $('#email_head1').removeClass('display-hide');
                    $('#email_head2').removeClass('display-hide');
                    $('#nknbulk-1').removeClass('display-hide');
                    $('#nknbulk-2').addClass('display-hide');
                }

            }, error: function ()
            {
                $(".loader").hide();
                console.log("inside error");
            }
        });
    });
    $('#nkn_bulk_form2').submit(function (e) {
        $(".loader").show();
        e.preventDefault();

        $('#nkn_bulk_form2 .edit').removeClass('display-hide');
        $.ajax({
            type: "POST",
            url: "nkn_bulk_tab2",
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.error_file !== null && jsonvalue.error.error_file !== "" && jsonvalue.error.error_file !== undefined)
                {
                    $('#nkn_bulk_form2 #error_file1').html(jsonvalue.error.error_file)
                    error_flag = false;
                } else {
                    $('#nkn_bulk_form2 #error_file1').html("")
                }
                if (error_flag) {

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#nkn_form2 #central_div').show();
                        $('#nkn_form2 #state_div').hide();
                        $('#nkn_form2 #other_div').hide();
                        $('#nkn_form2 #other_text_div').addClass("display-hide");
                        var select = $('#nkn_form2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#nkn_form2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#nkn_form2 #other_text_div').removeClass("display-hide");
                            $('#nkn_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#nkn_form2 #central_div').hide();
                        $('#nkn_form2 #state_div').show();
                        $('#nkn_form2 #other_div').hide();
                        $('#nkn_form2 #other_text_div').addClass("display-hide");
                        var select = $('#nkn_form2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#nkn_form2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);
                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#nkn_form2 #other_text_div').removeClass("display-hide");
                            $('#nkn_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#nkn_form2 #central_div').hide();
                        $('#nkn_form2 #state_div').hide();
                        $('#nkn_form2 #other_div').show();
                        $('#nkn_form2 #other_text_div').addClass("display-hide");
                        var select = $('#nkn_form2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#nkn_form2 #other_text_div').removeClass("display-hide");
                            $('#nkn_form2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#nkn_form2 #central_div').hide();
                        $('#nkn_form2 #state_div').hide();
                        $('#nkn_form2 #other_div').hide();
                    }

                    $('#nkn_form2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#nkn_form2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#nkn_form2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#nkn_form2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#nkn_form2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#nkn_form2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#nkn_form2 #user_state').val(jsonvalue.profile_values.state);
                    $('#nkn_form2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#nkn_form2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#nkn_form2 #user_rphone').val(jsonvalue.profile_values.rphone);
//                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
//                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
//                    $('#nkn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#nkn_form2 #user_email').val(jsonvalue.profile_values.email);
                    $('#nkn_form2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#nkn_form2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#nkn_form2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#nkn_form2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    //$('#nkn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile);
                    $('#nkn_form2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#nkn_form2 #inst_name').val(jsonvalue.form_details.inst_name);
                    $('#nkn_form2 #inst_id').val(jsonvalue.form_details.inst_id);
                    $('#nkn_form2 #nkn_id_type').val(jsonvalue.form_details.nkn_id_type);
                    $('#nkn_form2 #nkn_req_for').val(jsonvalue.form_details.nkn_req_for);
                    $('#nkn_form2 #nkn_project').val(jsonvalue.form_details.nkn_project);
                    $('#nkn_form2 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);
                    $('#nkn_form2 #confirm').val('nkn_bulk');
                    $('.edit').removeClass('display-hide');
                    if (jsonvalue.profile_values.bo_check == "no bo")
                    {
                        $('#nkn_form2 #USEditPreview').removeClass("display-hide");
                        $('#nkn_form2 #under_sec_email').val(jsonvalue.profile_values.under_sec_email);
                        $('#nkn_form2 #under_sec_name').val(jsonvalue.profile_values.under_sec_name);
                        var USstartMobile = jsonvalue.profile_values.under_sec_mobile.substring(0, 3);
                        var USendMobile = jsonvalue.profile_values.under_sec_mobile.substring(10, 13);
                        $('#nkn_form2 #under_sec_mobile').val(USstartMobile + 'XXXXXXX' + USendMobile);
                        $('#nkn_form2 #under_sec_tel').val(jsonvalue.profile_values.under_sec_tel);
                        $('#nkn_form2 #under_sec_desig').val(jsonvalue.profile_values.under_sec_desig);
                        $('#nkn_form2 #hod_mobile').val(jsonvalue.profile_values.hod_mobile)
                        $('#nkn_form2 #user_mobile').val(jsonvalue.profile_values.mobile);

                    } else {

                        $('#nkn_form2 #USEditPreview').addClass("display-hide");
                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                        $('#nkn_form2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                        var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                        var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                        $('#nkn_form2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    }
                    $('#nkn_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#nkn_form2 :button[type='button']").removeAttr('disabled');
                    $("#nkn_form2 #tnc").removeAttr('disabled');
                    $('#nkn_single_preview').addClass('display-hide');
                    $('#nkn_bulk_preview').removeClass('display-hide');
                    $('#large3').modal({backdrop: 'static', keyboard: false});
                }
            }, error: function ()
            {
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });
    $('#nkn_form2 .edit').click(function () {

        var employment = $('#nkn_preview_tab #user_employment').val();
        var min = $('#nkn_preview_tab #min').val();
        var dept = $('#nkn_preview_tab #dept').val();
        var statecode = $('#nkn_preview_tab #stateCode').val();
        var Smi = $('#nkn_preview_tab #Smi').val();
        var Org = $('#nkn_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#nkn_preview_tab #min');
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
                var select = $('#nkn_preview_tab #dept');
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
                $('#nkn_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#nkn_preview_tab #stateCode');
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
                var select = $('#nkn_preview_tab #Smi');
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
                $('#nkn_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#nkn_preview_tab #central_div').hide();
            $('#nkn_preview_tab #state_div').hide();
            $('#nkn_preview_tab #other_div').show();
            $('#nkn_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#nkn_preview_tab #Org');
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
                $('#nkn_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#nkn_preview_tab #central_div').hide();
            $('#nkn_preview_tab #state_div').hide();
            $('#nkn_preview_tab #other_div').hide();
        }
        $('#nkn_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#nkn_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        $('#nkn_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $(this).addClass('display-hide');
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#nkn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#bulk_preview_tab #REditPreview #hod_email').removeAttr('disabled');
    });
    $('#nkn_form2 #confirm').click(function (e) {

        e.preventDefault();
        $("#nkn_form2 :disabled").removeAttr('disabled');
        $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#nkn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#nkn_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#nkn_form2').serializeObject());
        $('#nkn_preview_tab #user_email').prop('disabled', 'true');
        console.log("on validate" + data)
        var submit = $('#nkn_form2 #confirm').val();

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "nkn_tab2",
            data: {data: data, request_type: submit, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.inst_name_error !== null && jsonvalue.error.inst_name_error !== "" && jsonvalue.error.inst_name_error !== undefined)
                {
                    $('#nkn_form2 #inst_name_err').html(jsonvalue.error.inst_name_error)
                    $('#nkn_form2 #inst_name').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #inst_name_err').html("")
                }

                if (jsonvalue.error.inst_id_error !== null && jsonvalue.error.inst_id_error !== "" && jsonvalue.error.inst_id_error !== undefined)
                {
                    $('#nkn_form2 #inst_id_err').html(jsonvalue.error.inst_id_error)
                    $('#nkn_form2 #inst_id').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #inst_id_err').html("")
                }

                if (jsonvalue.error.project_name_error !== null && jsonvalue.error.project_name_error !== "" && jsonvalue.error.project_name_error !== undefined)
                {
                    $('#nkn_form2 #nkn_project_err').html(jsonvalue.error.project_name_error)
                    $('#nkn_form2 #nkn_project').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #nkn_project_err').html("")
                }

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#nkn_form2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#nkn_form2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#nkn_form2 #single_email1_err').html("")
                        } else {
                            $('#nkn_form2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#nkn_form2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#nkn_form2 #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#nkn_form2 #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#nkn_form2 #single_email2_err').html("")
                        } else {
                            $('#nkn_form2 #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#nkn_form2 #single_email2').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#nkn_form2 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#nkn_form2 #single_dob').focus();
                    error_flag = false;
                } else {

                    $('#nkn_single_form #single_dob_err').html("")
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#nkn_form2 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#nkn_form2 #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #single_dor_err').html("")
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
                    $('#nkn_form2 #useremployment_error').focus();
                    $('#nkn_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#nkn_form2 #minerror').focus();
                    $('#nkn_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#nkn_form2 #deperror').focus();
                    $('#nkn_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#nkn_form2 #other_dept').focus();
                    $('#nkn_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#nkn_form2 #smierror').focus();
                    $('#nkn_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#nkn_form2 #state_error').focus();
                    $('#nkn_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#nkn_form2 #org_error').focus();
                    $('#nkn_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#nkn_form2 #ca_design').focus();
                    $('#nkn_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#nkn_form2 #hod_name').focus();
                    $('#nkn_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#nkn_form2 #hod_mobile').focus();
                    $('#nkn_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {
                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#nkn_form2 #hod_tel').focus();
                    $('#nkn_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodtel_error').html("");
                }
                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_name').focus();
                    $('#nkn_form2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_mobile').focus();
                    $('#nkn_form2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_email').focus();
                    $('#nkn_form2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#nkn_form2 #under_sec_tel').focus();
                    $('#nkn_form2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_tel_error').html("");
                }

                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_desig').focus();
                    $('#nkn_form2 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_desig_error').html("");
                }

                // profile 20th march
                if (!error_flag) {
                    $("#nkn_form2 :disabled").removeAttr('disabled');
                    $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#nkn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#nkn_form2 #tnc').is(":checked"))
                    {
                        $('#nkn_form2 #tnc_error').html("");
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
//                        if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#nkn_form_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#nkn_form_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#nkn_form_confirm #fill_hod_mobile').html("+919384664224");
//                        } else {
                        $('#nkn_form_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#nkn_form_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#nkn_form_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        //  }
                        $('#nkn_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#nkn_form2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#nkn_form2 #tnc").removeAttr('disabled');
                        $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                        //below line commented by MI on 25thjan19
                        // $('#nkn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });
    });
    $('#nkn_form_confirm #confirmYes').click(function () {
        $('#nkn_form2').submit();
        $('#stack3').modal('toggle');
    });

    $(document).on('click', '#nkn_preview .edit', function () {
        //$('#nkn_preview .edit').click(function () {

        var employment = $('#nkn_preview_tab #user_employment').val();
        var min = $('#nkn_preview_tab #min').val();
        var dept = $('#nkn_preview_tab #dept').val();
        var statecode = $('#nkn_preview_tab #stateCode').val();
        var Smi = $('#nkn_preview_tab #Smi').val();
        var Org = $('#nkn_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#nkn_preview_tab #min');
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
                var select = $('#nkn_preview_tab #dept');
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
                $('#nkn_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#nkn_preview_tab #stateCode');
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
                var select = $('#nkn_preview_tab #Smi');
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
                $('#nkn_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#nkn_preview_tab #central_div').hide();
            $('#nkn_preview_tab #state_div').hide();
            $('#nkn_preview_tab #other_div').show();
            $('#nkn_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#nkn_preview_tab #Org');
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
                $('#nkn_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#nkn_preview_tab #central_div').hide();
            $('#nkn_preview_tab #state_div').hide();
            $('#nkn_preview_tab #other_div').hide();
        }
        $('#nkn_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#nkn_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#nkn_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#nkn_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#nkn_preview .save-changes").html("Update");
        }
        // code end
    });

    $(document).on('click', '#nkn_preview #confirm', function () {
        //$('#nkn_preview #confirm').click(function () {
        $('#nkn_preview').submit();
        //$('#nkn_preview_form').modal('toggle');
    });
    $('#nkn_preview').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#nkn_preview :disabled").removeAttr('disabled');
        $('#nkn_preview #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#nkn_preview #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#nkn_preview #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        // nikki 22 Jan 2019
        $('#nkn_preview #user_email').removeAttr('disabled');// 20th march  
        var data = JSON.stringify($('#nkn_preview').serializeObject());
        $('#nkn_preview #user_email').prop('disabled', 'true'); // 20th march
        $('#nkn_preview #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');    // nikki 22 Jan 2019

        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18

        $.ajax({
            type: "POST",
            url: "nkn_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.inst_name_error !== null && jsonvalue.error.inst_name_error !== "" && jsonvalue.error.inst_name_error !== undefined)
                {
                    $('#nkn_preview #inst_name_err').html(jsonvalue.error.inst_name_error)
                    $('#nkn_preview #inst_name').focus();
                    error_flag = false;
                } else {

                    $('#nkn_preview #inst_name_err').html("")
                }
                if (jsonvalue.error.inst_id_error !== null && jsonvalue.error.inst_id_error !== "" && jsonvalue.error.inst_id_error !== undefined)
                {
                    $('#nkn_preview #inst_id_err').html(jsonvalue.error.inst_id_error)
                    $('#nkn_preview #inst_id').focus();
                    error_flag = false;
                } else {

                    $('#nkn_preview #inst_id_err').html("")
                }
                if (jsonvalue.error.project_name_error !== null && jsonvalue.error.project_name_error !== "" && jsonvalue.error.project_name_error !== undefined)
                {
                    $('#nkn_preview #nkn_project_err').html(jsonvalue.error.project_name_error)
                    $('#nkn_preview #nkn_project').focus();
                    error_flag = false;
                } else {

                    $('#nkn_preview #nkn_project_err').html("")
                }

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#nkn_preview #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#nkn_preview #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#nkn_preview #single_email1_err').html("")
                        } else {
                            $('#nkn_preview #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#nkn_preview #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#nkn_preview #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#nkn_preview #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#nkn_preview #single_email2_err').html("")
                        } else {
                            $('#nkn_preview #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#nkn_preview #single_email2').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#nkn_preview #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#nkn_preview #single_dob').focus();
                    error_flag = false;
                } else {

                    $('#nkn_single_form #single_dob_err').html("")
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#nkn_preview #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#nkn_preview #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#nkn_preview #single_dor_err').html("")
                }

                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#nkn_form2 #hod_name').focus();
                    $('#nkn_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#nkn_form2 #hod_mobile').focus();
                    $('#nkn_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#nkn_form2 #hod_tel').focus();
                    $('#nkn_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodtel_error').html("");
                }

                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_name').focus();
                    $('#nkn_form2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_mobile').focus();
                    $('#nkn_form2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_email').focus();
                    $('#nkn_form2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#nkn_form2 #under_sec_tel').focus();
                    $('#nkn_form2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_desig').focus();
                    $('#nkn_form2 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_name').focus();
                    $('#nkn_form2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_mobile').focus();
                    $('#nkn_form2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#nkn_preview #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_email').focus();
                    $('#nkn_preview #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#nkn_preview #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#nkn_preview #under_sec_tel').focus();
                    $('#nkn_preview #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #under_sec_tel_error').html("");
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {
                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error1').html("");
                }

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#nkn_preview #useremployment_error').focus();
                    $('#nkn_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#nkn_preview #minerror').focus();
                    $('#nkn_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#nkn_preview #deperror').focus();
                    $('#nkn_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#nkn_preview #other_dept').focus();
                    $('#nkn_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#nkn_preview #smierror').focus();
                    $('#nkn_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#nkn_preview #state_error').focus();
                    $('#nkn_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#nkn_preview #org_error').focus();
                    $('#nkn_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#nkn_preview #org_error').html("");
                }
                // profile 20th march 

                if (!error_flag)
                {
                    $("#nkn_preview :disabled").removeAttr('disabled');
                    $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#nkn_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                $(".loader").hide();
                console.log('error');
            }
        });
    });
    $('#nkn_form2').submit(function (e) {
        $(".loader").show();
        e.preventDefault();
        $("#nkn_form2 :disabled").removeAttr('disabled');
        $('#nkn_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');

        $('#nkn_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#nkn_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#nkn_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#nkn_form2').serializeObject());

        console.log("on submit" + data)
        $('#nkn_preview_tab #user_email').prop('disabled', 'true');

        var submit = $('#nkn_form2 #confirm').val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "nkn_tab2",
            data: {data: data, request_type: submit, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                $(".loader").hide();
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.inst_name_error !== null && jsonvalue.error.inst_name_error !== "" && jsonvalue.error.inst_name_error !== undefined)
                {
                    $('#nkn_form2 #inst_name_err').html(jsonvalue.error.inst_name_error)
                    $('#nkn_form2 #inst_name').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #inst_name_err').html("")
                }
                if (jsonvalue.error.inst_id_error !== null && jsonvalue.error.inst_id_error !== "" && jsonvalue.error.inst_id_error !== undefined)
                {
                    $('#nkn_form2 #inst_id_err').html(jsonvalue.error.inst_id_error)
                    $('#nkn_form2 #inst_id').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #inst_id_err').html("")
                }
                if (jsonvalue.error.project_name_error !== null && jsonvalue.error.project_name_error !== "" && jsonvalue.error.project_name_error !== undefined)
                {
                    $('#nkn_form2 #nkn_project_err').html(jsonvalue.error.project_name_error)
                    $('#nkn_form2 #nkn_project').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #nkn_project_err').html("")
                }

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#nkn_form2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#nkn_form2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                            $('#nkn_form2 #single_email1_err').html("")
                        } else {
                            $('#nkn_form2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#nkn_form2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.pref2_error !== null && jsonvalue.error.pref2_error !== "" && jsonvalue.error.pref2_error !== undefined)
                {
                    $('#nkn_form2 #single_email2_err').html(jsonvalue.error.pref2_error)
                    $('#nkn_form2 #single_email2').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg2 !== undefined) {
                        if (jsonvalue.error.errorMsg2.indexOf("is available for creation") > -1) {
                            $('#nkn_form2 #single_email2_err').html("")
                        } else {
                            $('#nkn_form2 #single_email2_err').html(jsonvalue.error.errorMsg2)
                            $('#nkn_form2 #single_email2').focus();
                            error_flag = false;
                        }
                    }
                }
                if (jsonvalue.error.dob_err !== null && jsonvalue.error.dob_err !== "" && jsonvalue.error.dob_err !== undefined)
                {
                    $('#nkn_form2 #single_dob_err').html(jsonvalue.error.dob_err)
                    $('#nkn_form2 #single_dob').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #single_dob_err').html("")
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#nkn_form2 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#nkn_form2 #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#nkn_form2 #single_dor_err').html("")
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
                    $('#nkn_form2 #useremployment_error').focus();
                    $('#nkn_form2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#nkn_form2 #minerror').focus();
                    $('#nkn_form2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#nkn_form2 #deperror').focus();
                    $('#nkn_form2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#nkn_form2 #other_dept').focus();
                    $('#nkn_form2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#nkn_form2 #smierror').focus();
                    $('#nkn_form2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#nkn_form2 #state_error').focus();
                    $('#nkn_form2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#nkn_form2 #org_error').focus();
                    $('#nkn_form2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#nkn_form2 #ca_design').focus();
                    $('#nkn_form2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#nkn_form2 #hod_name').focus();
                    $('#nkn_form2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#nkn_form2 #hod_mobile').focus();
                    $('#nkn_form2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {
                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {
                    $('#nkn_form2 #hod_email').focus();
                    $('#nkn_form2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#nkn_form2 #hod_tel').focus();
                    $('#nkn_form2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #hodtel_error').html("");
                }

                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_name').focus();
                    $('#nkn_form2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_mobile').focus();
                    $('#nkn_form2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_email').focus();
                    $('#nkn_form2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#nkn_form2 #under_sec_tel').focus();
                    $('#nkn_form2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#nkn_form2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#nkn_form2 #under_sec_desig').focus();
                    $('#nkn_form2 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#nkn_form2 #under_sec_desig_error').html("");
                }

                // profile 20th march

                if (error_flag) {

                    $('#large3').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: jsonvalue.form_details.request_type},
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
                $(".loader").hide();
                $('#tab1').show();
            }
        });
    });

    $('#gem_preview_tab #pse_state').change(function () {
        var pse_state = $("#pse_state").val();

        $.get('getDistrict', {
            user_state: pse_state
        }, function (response) {

            var select = $('#gem_preview_tab #pse_district');
            select.find('option').remove();
            $('<option>').val("").text("-SELECT-").appendTo(select);
            $.each(response, function (index, value) {
                $('<option>').val(value).text(value).appendTo(select);
            });
        });
    })


    $('#email_act1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#email_act1').serializeObject());
        var cert = $("#hardwarecert").val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        var radioValue = $("input[name='form_name']:checked").val();


        $.ajax({
            type: "POST",
            url: "email_act_tab1",
            data: {data: data, CSRFRandom: CSRFRandom, emailrequest: radioValue, cert: cert},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                //    alert("value: " + cert);

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {

                    $('#email_act1 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_act1 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for activation") > -1) {
                            $('#email_act1 #single_email1_err').html("")
                        } else {
                            $('#email_act1 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_act1 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {

                    $('#email_act1 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#email_act1 #single_dor').focus();
                    error_flag = false;
                    $('#email_act1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_act1 #imgtxt').val("");
                } else {

                    $('#email_act1 #single_dor_err').html("")
                }
                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {

                    $('#email_act1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#email_act1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_act1 #imgtxt').val("");
                } else {
                    $('#email_act1 #captchaerror').html("")
                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#email_act1 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#email_act1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_act1 #imgtxt').val("");
                }
                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                {
                    $('#email_act1 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");

                } else {
                    $('#email_act1 #file_err').html("")
                }
                // alert(error_flag)
                if (!error_flag) {
                } else {

                    if (jsonvalue.form_details.single_emp_type !== "emp_regular")
                    {

                        if (jsonvalue.form_details.uploaded_filename === "")
                        {
                            bootbox.dialog({
                                message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'> <ul><li>For Contractual Employees and FMS Support Staff, provide relevant Work Order at the time of filling Single User Subscription details. Here, Date of Expiry will be same as the Date of Expiry mention in your Work Order</li><li>If the Work Order is not provided within 1 month of application then Email Id will be automatically deactivated.</li><li>User can also upload Work Order later in <b>My Request</b> option available in Dashboard.</li></ul></p>",
                                title: "<b>NOTE:</b>",
                                buttons: {
                                    success: {
                                        label: "YES",
                                        className: "green",
                                        callback: function () {
                                            $('#large4').modal({backdrop: 'static', keyboard: false});

                                        }
                                    },
                                    main: {
                                        label: "NO",
                                        className: "red",
                                        callback: function () {
                                            //bootbox.hideAll();
                                        }
                                    }
                                }
                            });
                        } else {

                            bootbox.dialog({
                                message: "<p style='text-align: center; font-size: 15px;font-weight:bold;'><ul style='list-style-type:none';><li>For Contractual Employees and FMS Support Staff, provide relevant Work Order at the time of filling Single User Subscription details. Here, Date of Expiry will be same as the Date of Expiry mention in your Work Order</li></ul></p>",
                                title: "<b>NOTE:</b>",
                                buttons: {
                                    success: {
                                        label: "YES",
                                        className: "green",
                                        callback: function () {
                                            $('#large4').modal({backdrop: 'static', keyboard: false});

                                        }
                                    },
                                    main: {
                                        label: "NO",
                                        className: "red",
                                        callback: function () {
                                            //bootbox.hideAll();
                                        }
                                    }
                                }
                            });




                        }
                    } else {

                        $('#large4').modal({backdrop: 'static', keyboard: false});
                    }

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#email_act2 #central_div').show();
                        $('#email_act2 #state_div').hide();
                        $('#email_act2 #other_div').hide();
                        $('#email_act2 #other_text_div').addClass("display-hide");
                        var select = $('#email_act2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#email_act2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#email_act2 #other_text_div').removeClass("display-hide");
                            $('#email_act2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#email_act2 #central_div').hide();
                        $('#email_act2 #state_div').show();
                        $('#email_act2 #other_div').hide();
                        $('#email_act2 #other_text_div').addClass("display-hide");
                        var select = $('#email_act2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#email_act2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#email_act2 #other_text_div').removeClass("display-hide");
                            $('#email_act2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#email_act2 #central_div').hide();
                        $('#email_act2 #state_div').hide();
                        $('#email_act2 #other_div').show();
                        $('#email_act2 #other_text_div').addClass("display-hide");
                        var select = $('#email_act2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#email_act2 #other_text_div').removeClass("display-hide");
                            $('#email_act2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#email_act2 #central_div').hide();
                        $('#email_act2 #state_div').hide();
                        $('#email_act2 #other_div').hide();
                    }

                    $('#email_act2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#email_act2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#email_act2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#email_act2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#email_act2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#email_act2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#email_act2 #user_state').val(jsonvalue.profile_values.state);
                    $('#email_act2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#email_act2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#email_act2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#email_act2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#email_act2 #user_email').val(jsonvalue.profile_values.email);
                    $('#email_act2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#email_act2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#email_act2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#email_act2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#email_act2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                    $('#email_act2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#email_act2 #dup_mail_exist').val(jsonvalue.form_details.dup_mail_exist);
                    $('#email_act2 #email_deactdiv').addClass("display-hide");
                    $('#email_act2 #email_actdiv').removeClass("display-hide");
                    $('#email_act2 #act_email1').val(jsonvalue.form_details.act_email1);
                    $('#email_act2 #emp_type_div').removeClass("display-hide");
                    if (jsonvalue.form_details.single_emp_type === 'emp_regular') {
                        $('#email_act2 #single_emp_type1').prop('checked', 'checked');
                        $('#email_act2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                        $('#email_act2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                    } else if (jsonvalue.form_details.single_emp_type === 'consultant') {
                        $('#email_act2 #single_emp_type2').prop('checked', 'checked');
                        $('#email_act2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                        $('#email_act2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@gov.in OR abc.xyz@nic.in]");
                    } else {
                        $('#email_act2 #single_emp_type3').prop('checked', 'checked');
                        $('#email_act2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                        $('#email_act2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                    }
                    if (jsonvalue.form_details.single_emp_type !== "emp_regular")
                    {
                        if (jsonvalue.form_details.uploaded_filename !== "")
                        {
                            $('#email_act2 #cert_div').removeClass("display-hide");
                            $('#email_act2 #uploaded_filename').text(jsonvalue.form_details.uploaded_filename);
                        }
                    } else {

                        $('#email_act2 #cert_div').addClass("display-hide");
                    }
                    $('#email_act2 #email_single_dor1').val(jsonvalue.form_details.single_dor);
                    $('#email_act2 #divDor').removeClass('display-hide');
                    $('#email_act2 .edit').removeClass('display-hide');
                    $('#email_act_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    // $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#email_act2 :button[type='button']").removeAttr('disabled');
                    $("#email_act2 #tnc").removeAttr('disabled');




                }

            }, error: function ()
            {
                console.log('error');
            }
        });
    });

    //DOR-EXT JS

    $('#dor_ext1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#dor_ext1').serializeObject());
        var cert = $("#cert").val();
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        var radioValue = $("input[name='form_name']:checked").val();
        var account_expiry = $("input[name='account_expiry']:checked").val();
        var dt = JSON.parse(data);
        if (dt.single_dor === "")
            return false;
        $.ajax({
            type: "POST",
            url: "dor_ext_tab1",
            data: {data: data, CSRFRandom: CSRFRandom, emailrequest: radioValue, cert: cert},
            datatype: JSON,
            success: function (data)
            {
                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.alert_error != null && jsonvalue.error.alert_error != "") {
                    $('#dor_ext1 #text_service_employees').html(jsonvalue.error.alert_error);
                    $('#dor_ext1 #alert_service_employees').removeClass("display-hide");
                    error_flag = false;
                } else {
                    $('#dor_ext1 #text_service_employees').html("");
                    $('#dor_ext1 #alert_service_employees').addClass("display-hide");
                }

//                 if (jsonvalue.emailrequest === "dor_ext") {
//            $('#dor_ext_preview_tab #REditPreview').hide(); 
//        } else {
//            $('#dor_ext_preview_tab #REditPreview').show();
//        }
                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {

                    $('#dor_ext1 #dor_ext_err').html(jsonvalue.error.pref1_error)
                    $('#dor_ext1 #dor_email').focus();
                    error_flag = false;
                    $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dor_ext1 #imgtxt').val("");
                    console.log("inside if>>>>>11111");
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for activation") > -1) {
                            $('#dor_ext1 #dor_ext_err').html("")
                            console.log("inside if>>>>>222222");
                        } else {
                            $('#dor_ext1 #dor_ext_err').html(jsonvalue.error.errorMsg1)
                            $('#dor_ext1 #dor_email').focus();
                            error_flag = false;
                            $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                            $('#dor_ext1 #imgtxt').val("");
                            console.log("inside elseee>>>>>11111");
                        }
                    }
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    console.log("satyaaaaaa" + jsonvalue.error.dor_err);
                    $('#dor_ext1 #dor_err2').html(jsonvalue.error.dor_err)
                    $('#dor_ext1 #email_single_dor').focus();
                    error_flag = false;
                    $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dor_ext1 #imgtxt').val("");
                    console.log("inside if>>>>>33333");
                } else {

                    $('#dor_ext1 #dor_err2').html("")
                }

                if (jsonvalue.error.dor_err1 !== null && jsonvalue.error.dor_err1 !== "" && jsonvalue.error.dor_err1 !== undefined)
                {

                    $('#dor_ext1 #dor_err').html(jsonvalue.error.dor_err1)
                    $('#dor_ext1 #email_single_dor').focus();
                    error_flag = false;
                    $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dor_ext1 #imgtxt').val("");
                    console.log("inside if>>>>>33333");
                } else {

                    $('#dor_ext1 #dor_err').html("")
                }



                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {

                    $('#dor_ext1 #captchaerror').html(jsonvalue.error.cap_error)
                    $('#dor_ext1 #captcha1').focus();
                    error_flag = false;
                    $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dor_ext1 #imgtxt').val("");
                    console.log("inside if>>>>>44444444");
                } else {

                    $('#dor_ext1 #captchaerror').html("")
                }
                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#dor_ext1 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#dor_ext1 #imgtxt').val("");
                    console.log("inside if>>>>>55555");
                }
                if (jsonvalue.error.file_err !== null && jsonvalue.error.file_err !== "" && jsonvalue.error.file_err !== undefined)
                {
                    $('#dor_ext1 #file_err').html(jsonvalue.error.file_err)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                    console.log("inside if>>>>>6666666666");

                } else {
                    $('#dor_ext1 #file_err').html("")
                }
                if (jsonvalue.error.id_typeErr !== null && jsonvalue.error.id_typeErr !== "" && jsonvalue.error.id_typeErr !== undefined)
                {
                    $('#dor_ext1 #single_id_type_err').html(jsonvalue.error.id_typeErr)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                    console.log("inside if>>>>>88888888");

                } else {
                    $('#dor_ext1 #single_id_type_err').html("")
                }
                if (jsonvalue.error.single_emp_typeErr !== null && jsonvalue.error.single_emp_typeErr !== "" && jsonvalue.error.single_emp_typeErr !== undefined)
                {
                    $('#dor_ext1 #single_emp_type_err').html(jsonvalue.error.single_emp_typeErr)
                    error_flag = false;
                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#imgtxt').val("");
                    console.log("inside if>>>>>9999999");

                } else {
                    $('#dor_ext1 #single_emp_type_err').html("")
                }
                if (jsonvalue.error.p_dor_error !== null && jsonvalue.error.p_dor_error !== "" && jsonvalue.error.p_dor_error !== undefined)
                {
                    error_flag = false;
                    console.log("inside if>>>>>7777777");
                }




                // alert(!error_flag)
                if (error_flag && jsonvalue.form_details != null) {
//                    } else {
//                        var account_expiry = $("input[name='account_expiry']:checked").val();
//                        if (account_expiry === "serving_employee") {
                    $('#large6').modal({backdrop: 'static', keyboard: false});
//                        } else {
//                            $('#large6').modal({backdrop: 'static', keyboard: false});
//                        }

//                    $('#large6').modal({backdrop: 'static', keyboard: false});


                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#dor_ext2 #central_div').show();
                        $('#dor_ext2 #state_div').hide();
                        $('#dor_ext2 #other_div').hide();
                        $('#dor_ext2 #other_text_div').addClass("display-hide");
                        var select = $('#dor_ext2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#dor_ext2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#dor_ext2 #other_text_div').removeClass("display-hide");
                            $('#dor_ext2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#dor_ext2 #central_div').hide();
                        $('#dor_ext2 #state_div').show();
                        $('#dor_ext2 #other_div').hide();
                        $('#dor_ext2 #other_text_div').addClass("display-hide");
                        var select = $('#dor_ext2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#dor_ext2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#dor_ext2 #other_text_div').removeClass("display-hide");
                            $('#dor_ext2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#dor_ext2 #central_div').hide();
                        $('#dor_ext2 #state_div').hide();
                        $('#dor_ext2 #other_div').show();
                        $('#dor_ext2 #other_text_div').addClass("display-hide");
                        var select = $('#dor_ext2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#dor_ext2 #other_text_div').removeClass("display-hide");
                            $('#dor_ext2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#dor_ext2 #central_div').hide();
                        $('#dor_ext2 #state_div').hide();
                        $('#dor_ext2 #other_div').hide();
                    }

                    $('#dor_ext2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#dor_ext2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#dor_ext2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#dor_ext2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#dor_ext2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#dor_ext2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#dor_ext2 #user_state').val(jsonvalue.profile_values.state);
                    $('#dor_ext2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#dor_ext2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#dor_ext2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#dor_ext2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#dor_ext2 #user_email').val(jsonvalue.profile_values.email);
                    $('#dor_ext2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#dor_ext2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#dor_ext2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#dor_ext2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var ROstartMobile = "";
                    var ROendMobile = "";
                    if (jsonvalue.profile_values.hod_mobile === undefined) {
                        ROstartMobile = "";
                        ROendMobile = "";
                    } else {
                        var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                        var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    }
                    $('#dor_ext2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                    $('#dor_ext2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#dor_ext2 #dup_mail_exist').val(jsonvalue.form_details.dup_mail_exist);

                    $('#dor_ext2 #email_deactdiv').addClass("display-hide");
                    $('#dor_ext2 #email_actdiv').removeClass("display-hide");
                    $('#dor_ext2 #dor_ext_email1').val(jsonvalue.form_details.dor_ext_email1);
                    $('#dor_ext2 #emp_type_div').removeClass("display-hide");

                    $('#dor_ext2 #single_emp_type3').prop('checked', 'checked');
                    $('#dor_ext2 #single_email1').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");
                    $('#dor_ext2 #single_email2').attr("placeholder", "Enter Email Address [e.g: abc.xyz@nic.in]");

                    if (jsonvalue.form_details.single_emp_type !== "emp_regular")
                    {
                        if (jsonvalue.form_details.uploaded_filename !== "")
                        {
                            $('#dor_ext2 #cert_div').removeClass("display-hide");
                            $('#dor_ext2 #hardware_cert_file3').text(jsonvalue.form_details.uploaded_filename);
                        }
                    } else {

                        $('#dor_ext2 #cert_div').addClass("display-hide");
                    }

                    if (jsonvalue.form_details.single_id_type === 'id_name')
                    {

                        $('#dor_ext2 #single_id_type_dor_1').prop('checked', true);
                    }
                    if (jsonvalue.form_details.single_id_type === 'id_desig')
                    {

                        $('#dor_ext2 #single_id_type_dor_2').prop('checked', true);
                    }

                    if (jsonvalue.form_details.single_emp_type === 'emp_regular') {

                        $('#dor_ext2 #single_emp_type_dor1').prop('checked', true);
                    }
                    if (jsonvalue.form_details.single_emp_type === 'consultant') {
                        $('#dor_ext2 #single_emp_type_dor2').prop('checked', true);
                    }
                    if (jsonvalue.form_details.single_emp_type === 'emp_contract') {
                        $('#dor_ext2 #single_emp_type_dor3').prop('checked', true);

                    }



                    if (jsonvalue.form_details.single_emp_type == "emp_regular") {

                        $('#dor_ext2 #fileUploadDiv').addClass("d-none");
                        $('#dor_ext2 #fileDownloadDiv').addClass("d-none");
                    } else {
                        $('#dor_ext2 #fileUploadDiv').removeClass("d-none");
                        $('#dor_ext2 #fileDownloadDiv').removeClass("d-none");
                    }
                    $('#dor_ext2 #dor_email').val(jsonvalue.form_details.dor_email);
                    $('#dor_ext2 #p_email_single_dor').val(jsonvalue.form_details.p_single_dor);
                    $('#dor_ext2 #email_single_dor2').val(jsonvalue.form_details.single_dor);
                    $('#dor_ext2 #email_single_dob2').val(jsonvalue.form_details.single_dob);

                    $('#dor_ext2 #divDor').removeClass('display-hide');
                    $('#dor_ext2 .edit').addClass('display-hide');
                    $('#dor_ext_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    // $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#dor_ext2 :button[type='button']").removeAttr('disabled');
                    $("#dor_ext2 #tnc").removeAttr('disabled');

                }

            }, error: function ()
            {
                console.log('error');
            }
        });
//        } else {
//            $.ajax({
//                type: "POST",
//                url: "dor_ext_retired_tab1",
//                data: {data: data, CSRFRandom: CSRFRandom, emailrequest: radioValue, cert: cert},
//                datatype: JSON,
//                success: function (data)
//                {
//
//                    resetCSRFRandom();// line added by pr on 22ndjan18
//
//                    var myJSON = JSON.stringify(data);
//                    var jsonvalue = JSON.parse(myJSON);
//                    var error_flag = true;
//
//
////                 if (jsonvalue.emailrequest === "dor_ext") {
////            $('#dor_ext_preview_tab #REditPreview').hide(); 
////        } else {
////            $('#dor_ext_preview_tab #REditPreview').show();
////        }
//                    if (jsonvalue.form_details.dor_email !== null && jsonvalue.form_details.dor_email !== "" && jsonvalue.form_details.dor_email !== undefined)
//                    {
//
//                        $('#dor_ext2 #dor_ext_email1').val(jsonvalue.form_details.dor_email);
//
//                    }
//                    if (jsonvalue.error.retired_dn_error !== null && jsonvalue.error.retired_dn_error !== "" && jsonvalue.error.retired_dn_error !== undefined)
//                    {
//                        console.log(jsonvalue.error.retired_dn_error)
//
//                        $('#dor_ext1 #dor_err7').html(jsonvalue.error.retired_dn_error);
//                    }
//                    if (jsonvalue.form_details.dor_email !== null && jsonvalue.form_details.dor_email !== "" && jsonvalue.form_details.dor_email !== undefined)
//                    {
//
//                        $('#dor_ext1 #act_email1').val(jsonvalue.form_details.dor_email)
//
//                    }
//                    if (jsonvalue.form_details.dor_email !== null && jsonvalue.form_details.dor_email !== "" && jsonvalue.form_details.dor_email !== undefined)
//                    {
//
//                        $('#dor_ext1 #act_email1').val(jsonvalue.form_details.dor_email)
//
//                    }
//                    if (jsonvalue.error.dor_err1 !== null && jsonvalue.error.dor_err1 !== "" && jsonvalue.error.dor_err1 !== undefined)
//                    {
//
//                        $('#dor_ext1 #dor_err').html(jsonvalue.error.dor_err1)
//                        $('#dor_ext1 #email_single_dor').focus();
//                        error_flag = false;
//                        $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                        $('#dor_ext1 #imgtxt').val("");
//                        console.log("inside if>>>>>33333");
//                    } else {
//
//                        $('#dor_ext1 #dor_err').html("")
//                    }
//                    if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
//                    {
//
//                        $('#dor_ext1 #captchaerror').html(jsonvalue.error.cap_error)
//                        $('#dor_ext1 #captcha1').focus();
//                        error_flag = false;
//                        $('#dor_ext1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                        $('#dor_ext1 #imgtxt').val("");
//                        console.log("inside if>>>>>44444444");
//                    } else {
//
//                        $('#dor_ext1 #captchaerror').html("")
//                    }
//                    
//                    
//
//
//
//
//
//                    // alert(error_flag)
//                    if (!error_flag) {
//                    } else {
//                        var account_expiry = $("input[name='account_expiry']:checked").val();
//                        if (account_expiry === "serving_employee") {
//                            $('#large6').modal({backdrop: 'static', keyboard: false});
//                        } else {
//                            $('#large7').modal({backdrop: 'static', keyboard: false});
//                        }
//
////                    $('#large6').modal({backdrop: 'static', keyboard: false});
//
//
//
//                        if (jsonvalue.form_details.single_emp_type !== "emp_regular")
//                        {
//                            if (jsonvalue.form_details.uploaded_filename !== "")
//                            {
//                                $('#dor_ext2 #cert_div').removeClass("display-hide");
//                                $('#dor_ext2 #hardware_cert_file3').text(jsonvalue.form_details.uploaded_filename);
//                            }
//                        } else {
//
//                            $('#dor_ext2 #cert_div').addClass("display-hide");
//                        }
//
//                        if (jsonvalue.form_details.single_id_type === 'id_name')
//                        {
//
//                            $('#dor_ext2 #single_id_type_dor_1').prop('checked', true);
//                        }
//                        if (jsonvalue.form_details.single_id_type === 'id_desig')
//                        {
//
//                            $('#dor_ext2 #single_id_type_dor_2').prop('checked', true);
//                        }
//
//                        if (jsonvalue.form_details.single_emp_type === 'emp_regular') {
//
//                            $('#dor_ext2 #single_emp_type_dor1').prop('checked', true);
//                        }
//                        if (jsonvalue.form_details.single_emp_type === 'consultant') {
//                            $('#dor_ext2 #single_emp_type_dor2').prop('checked', true);
//                        }
//                        if (jsonvalue.form_details.single_emp_type === 'emp_contract') {
//                            $('#dor_ext2 #single_emp_type_dor3').prop('checked', true);
//
//                        }
//
//
//
//
//                        $('#dor_ext2 #dor_email').val(jsonvalue.form_details.dor_email);
//                        $('#dor_ext2 #email_single_dor2').val(jsonvalue.form_details.single_dor);
//                        $('#dor_ext2 #email_single_dob2').val(jsonvalue.form_details.single_dob);
//                        $('#dor_ext2 #p_email_single_name').val(jsonvalue.form_details.name_single_dor);
//                        $('#dor_ext2 #p_email_single_dor').val(jsonvalue.form_details.p_single_dor);
//                        $('#dor_ext2 #p_email_single_name').val(jsonvalue.form_details.applicant_name);
//                        $('#dor_ext2 #p_email_mobile_dor').val(jsonvalue.form_details.applicant_mobile);
//                        
//
//                        $('#dor_ext2 #divDor').removeClass('display-hide');
//                        $('#dor_ext2 .edit').removeClass('display-hide');
//                        $('#dor_ext_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
//                        // $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').attr('disabled', 'true');
//                        $("#dor_ext2 :button[type='button']").removeAttr('disabled');
//                        $("#dor_ext2 #tnc").removeAttr('disabled');
//
//                    }
//
//                }, error: function ()
//                {
//                    console.log('error');
//                }
//            });
//        }
    });



    $('#dor_ext2 #confirm').click(function (e) {

        e.preventDefault();
        $("#dor_ext2 :disabled").removeAttr('disabled');
        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#dor_ext2').serializeObject());
        $('#dor_ext_preview_tab #user_email').prop('disabled', 'true');
        var CSRFRandom = $("#CSRFRandom").val();
        // var hardwarecert1=$('#hardwarecert1').val();
        var account_expiry = $("input[name='account_expiry']:checked").val();
//        if (account_expiry === "serving_employee") {

        //   alert(hardwarecert1);
        $.ajax({
            type: "POST",
            url: "dor_ext_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {
                // alert("inside validate tab 2");
                resetCSRFRandom();

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined) {
                    error_flag = false;
                    console.log("inside validation dor");
                } else {
                    error_flag = true;
                }
                if (jsonvalue.error.p_dor_error !== null && jsonvalue.error.p_dor_error !== "" && jsonvalue.error.p_dor_error !== undefined)
                {
                    error_flag = false;
                    console.log("inside if>>>>>5858585");
                }

                console.log("error: " + error_flag)
                if (!error_flag) {
                    $("#dor_ext2 :disabled").removeAttr('disabled');
                    $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#dor_ext_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#dor_ext2 #tnc').is(":checked"))
                    {

                        $('#dor_ext2 #tnc_error').html("");
//                        var account_expiry = $("input[name='account_expiry']:checked").val();
//                        if (account_expiry == "serving_employee") {
                        $('#stack3').modal({backdrop: 'static', keyboard: false});
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);

//                        if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#dor_ext_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#dor_ext_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#dor_ext_confirm #fill_hod_mobile').html("+919384664224");
//                        } 
                        // else {
                        $('#dor_ext_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#dor_ext_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#dor_ext_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        //  }

                        $('#dor_ext_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
//                        } else {
//                            dor_ext_submit();
//                        }
                    } else {
                        $('#dor_ext2 #tnc_error').html("Please agree to Terms and Conditions");
                        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march

                        $("#dor_ext2 #tnc").removeAttr('disabled');
                    }
                }

            }, error: function ()
            {
                $('#tab1').show();
            }
        });
//        } else {
//            $.ajax({
//                type: "POST",
//                url: "dor_ext_retired_tab2",
//                data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
//                datatype: JSON,
//                success: function (data)
//                {
//                    // alert("inside validate tab 2");
//                    resetCSRFRandom();
//
//                    var myJSON = JSON.stringify(data);
//                    var jsonvalue = JSON.parse(myJSON);
//                    var error_flag = true;
//
//                    if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined) {
//                        error_flag = false;
//                        console.log("inside validation dor");
//                    } else {
//                        error_flag = true;
//                    }
//                    if (jsonvalue.error.p_dor_error !== null && jsonvalue.error.p_dor_error !== "" && jsonvalue.error.p_dor_error !== undefined)
//                    {
//                        error_flag = false;
//                        console.log("inside if>>>>>5858585");
//                    }
//
//                    console.log("error: " + error_flag)
//                    if (!error_flag) {
//                        $("#dor_ext2 :disabled").removeAttr('disabled');
//                        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//                        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//                        $('#dor_ext_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//                    } else {
//                        if ($('#dor_ext2 #tnc').is(":checked"))
//                        {
//
//                            $('#dor_ext2 #tnc_error').html("");
//                            var account_expiry = $("input[name='account_expiry']:checked").val();
//                            if (account_expiry == "serving_employee") {
//                                $('#stack3').modal({backdrop: 'static', keyboard: false});
//                                var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                                var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//
////                        if (jsonvalue.form_details.min == "External Affairs")
////                        {
////                            $('#dor_ext_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
////                            $('#dor_ext_confirm #fill_hod_email').html("jsegit@mea.gov.in");
////                            $('#dor_ext_confirm #fill_hod_mobile').html("+919384664224");
////                        } 
//                                // else {
//                                $('#dor_ext_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
//                                $('#dor_ext_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
//                                var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                                var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                                $('#dor_ext_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
//                                //  }
//
//                                $('#dor_ext_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
//                            } else {
////                                dor_ext_submit();
//var csrf = $('#CSRFRandom').val();
//                            $.ajax({
//                            type: "POST",
//                            url: "consent",
//                            data: {check: 'esign', formtype: 'dor_ext_retired', CSRFRandom: csrf, retired_no_yes: 'yes'},
//                            success: function (check) {
//
//                                window.location.href = "esignPDF";
//                            },
//                            error: function () {
//                                console.log('error');
//                            }
//                        });
//                            }
//                        } else {
//                            $('#dor_ext2 #tnc_error').html("Please agree to Terms and Conditions");
//                            $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
//                            $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
//
//                            $("#dor_ext2 #tnc").removeAttr('disabled');
//                        }
//                    }
//
//                }, error: function ()
//                {
//                    $('#tab1').show();
//                }
//            });
//        }
    });

    $('#dor_ext_confirm #confirmYes').click(function () {
        dor_ext_submit();
        $('#stack3').modal('toggle');
    });




    $('#dor_ext2').submit(dor_ext_submit);
    var dor_ext_submit = function (e) {

//        e.preventDefault();
        $("#dor_ext2 :disabled").removeAttr('disabled');
        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#dor_ext2').serializeObject());
        $('#dor_ext_preview_tab #user_email').prop('disabled', 'true');

//        alert("123")

        var CSRFRandom = $("#CSRFRandom").val();

        $('#dor_ext2 .edit').removeClass('display-hide');
        var account_expiry = $("input[name='account_expiry']:checked").val();
//        if (account_expiry === "serving_employee") {

//            alert("321")
        // for submit preview
        $.ajax({
            type: "POST",
            url: "dor_ext_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;


                if (error_flag) {
                    // show modal
                    $('#large6').modal('toggle');
                    console.log("inside if of subimtttttt::::::");
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: 'dor_ext'},
                        datatype: JSON,
                        success: function (data)
                        {
//                                alert("456")
                            console.log("")
                            window.location.href = 'e_sign';
                        }, error: function ()
                        {
                            $('#tab1').show();
                        }
                    });
                } else { // for retired consent
                    //      var formtype = $('#esign_submit').val();
//                        alert("retired")
                    var csrf = $('#CSRFRandom').val();
//        var esign = $("#esign_div input[type=radio]:checked").val();
                    $.ajax({
                        type: "POST",
                        url: "consent",
                        data: {check: 'esign', formtype: 'dor_ext', CSRFRandom: csrf, retired_no_yes: 'yes'},
                        success: function (check) {

                            window.location.href = "esignPDF";
                        },
                        error: function () {
                            console.log('error');
                        }
                    });

                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });

//        } else {
//
//            $.ajax({
//                type: "POST",
//                url: "dor_ext_retired_tab2",
//                data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
//                datatype: JSON,
//                success: function (data)
//                {
//
//                    resetCSRFRandom();
//
//                    var myJSON = JSON.stringify(data);
//                    var jsonvalue = JSON.parse(myJSON);
//                    var error_flag = false;
//
//
//                    if (error_flag) {
//                        // show modal
//                        $('#large6').modal('toggle');
//                        console.log("inside if of subimtttttt::::::");
//                        $.ajax({
//                            type: "POST",
//                            url: "esign",
//                            data: {formtype: 'dor_ext'},
//                            datatype: JSON,
//                            success: function (data)
//                            {
//                                console.log("")
//                                window.location.href = 'e_sign';
//                            }, error: function ()
//                            {
//                                $('#tab1').show();
//                            }
//                        });
//                    } else { // for retired consent
//                        //      var formtype = $('#esign_submit').val();
//                        //alert("123")
//                        var csrf = $('#CSRFRandom').val();
////        var esign = $("#esign_div input[type=radio]:checked").val();
//                        $.ajax({
//                            type: "POST",
//                            url: "consent",
//                            data: {check: 'esign', formtype: 'dor_ext', CSRFRandom: csrf, retired_no_yes: 'yes'},
//                            success: function (check) {
//
//                                window.location.href = "esignPDF";
//                            },
//                            error: function () {
//                                console.log('error');
//                            }
//                        });
//
//                    }
//                }, error: function ()
//                {
//                    $('#tab1').show();
//                }
//            });
//        }
    };


    $(document).on('click', '#dor_ext_preview .edit', function () {
        // $('#ldap_preview .edit').click(function () {
        //alert("sahi")
        var employment = $('#dor_ext_preview_tab #user_employment').val();
        var min = $('#dor_ext_preview_tab #min').val();
        var dept = $('#dor_ext_preview_tab #dept').val();
        var statecode = $('#dor_ext_preview_tab #stateCode').val();
        var Smi = $('#dor_ext_preview_tab #Smi').val();
        var Org = $('#dor_ext_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dor_ext_preview_tab #min');
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
                var select = $('#dor_ext_preview_tab #dept');
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
                $('#dor_ext_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dor_ext_preview_tab #stateCode');
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
                var select = $('#dor_ext_preview_tab #Smi');
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
                $('#dor_ext_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#dor_ext_preview_tab #central_div').hide();
            $('#dor_ext_preview_tab #state_div').hide();
            $('#dor_ext_preview_tab #other_div').show();
            $('#dor_ext_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dor_ext_preview_tab #Org');
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
                $('#dor_ext_preview_tab #other_text_div').removeClass("display-hide");
            }


        } else {
            $('#dor_ext_preview_tab #central_div').hide();
            $('#dor_ext_preview_tab #state_div').hide();
            $('#dor_ext_preview_tab #other_div').hide();
        }
        $('#dor_ext_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#dor_ext_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #ldap_https').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #ldap_audit').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            

        if ($("#comingFrom").val("admin"))
        {
            $("#dor_ext_preview .save-changes").removeClass('display-hide'); // line added by pr on 25thjan18
            $("#dor_ext_preview .save-changes").html("Update");
        }
        $('input:radio[name="single_id_type"]:not(:checked)').prop('disabled', true);
        $('input:radio[name="single_emp_type"]:not(:checked)').prop('disabled', true);
        $('#dor_ext_preview #dor_email').prop('disabled', 'true');
        $('#dor_ext_preview #dor_email').prop('disabled', 'true');
        $('#dor_ext_preview #dor_email').prop('disabled', 'true');
// code end

    });

    $(document).on('click', '#dor_ext_preview #confirm', function () {
        // $('#ldap_preview #confirm').click(function () {
        $('#dor_ext_preview').submit();
        // $('#ldap_preview_form').modal('toggle');
    });


    $('#dor_ext_preview').submit(function (e) {
        e.preventDefault();
        $("#dor_ext_preview :disabled").removeAttr('disabled');
        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #user_email').removeAttr('disabled');
        var data = JSON.stringify($('#dor_ext_preview').serializeObject());
        $('#dor_ext_preview_tab #user_email').prop('disabled', 'true');
        $('#dor_ext_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        var CSRFRandom = $("#CSRFRandom").val();

        $.ajax({
            type: "POST",
            url: "dor_ext_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();


                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined) {
                    error_flag = false;
                    console.log("inside validation dor");
                } else {
                    error_flag = true;
                }

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#dor_ext_preview #useremployment_error').focus();
                    $('#dor_ext_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#dor_ext_preview #minerror').focus();
                    $('#dor_ext_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#dor_ext_preview #deperror').focus();
                    $('#dor_ext_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#dor_ext_preview #other_dept').focus();
                    $('#dor_ext_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#dor_ext_preview #smierror').focus();
                    $('#dor_ext_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#dor_ext_preview #state_error').focus();
                    $('#dor_ext_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#dor_ext_preview #org_error').focus();
                    $('#dor_ext_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#dor_ext_preview #org_error').html("");
                }


                if (!error_flag)
                {

                    $("#dor_ext_preview :disabled").removeAttr('disabled');
                    $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                } else {

                    $('#dor_ext_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });


    $('#dor_ext2 .edit').click(function () {


        var employment = $('#dor_ext_preview_tab #user_employment').val();
        var min = $('#dor_ext_preview_tab #min').val();
        var dept = $('#dor_ext_preview_tab #dept').val();
        var statecode = $('#dor_ext_preview_tab #stateCode').val();
        var Smi = $('#dor_ext_preview_tab #Smi').val();
        var Org = $('#dor_ext_preview_tab #Org').val();
        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dor_ext_preview_tab #min');
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
                var select = $('#dor_ext_preview_tab #dept');
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
                $('#dor_ext_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dor_ext_preview_tab #stateCode');
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
                var select = $('#dor_ext_preview_tab #Smi');
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
                $('#dor_ext_preview_tab #other_text_div').removeClass("display-hide");
            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#dor_ext_preview_tab #central_div').hide();
            $('#dor_ext_preview_tab #state_div').hide();
            $('#dor_ext_preview_tab #other_div').show();
            $('#dor_ext_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#dor_ext_preview_tab #Org');
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
                $('#dor_ext_preview_tab #other_text_div').removeClass("display-hide");
            }


        } else {
            $('#dor_ext_preview_tab #central_div').hide();
            $('#dor_ext_preview_tab #state_div').hide();
            $('#dor_ext_preview_tab #other_div').hide();
        }

//alert( $('#dor_ext_preview_tab').find("#email_single_dor1").length);
        $('#dor_ext_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#dor_ext_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#dor_ext_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #ldap_https').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #ldap_audit').find('input, textarea, button, select').prop('disabled', 'true');
        $('#dor_ext_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        //below line commented by MI on 25thjan19 for disable the field in case of RO is govt and enable the field in case of RO is non gov
        //$('#ldap_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#dor_ext_preview_tab #REditPreview #hod_email').removeAttr('disabled');

        $('input:radio[name="single_id_type"]:not(:checked)').prop('disabled', true);
        $('input:radio[name="single_emp_type"]:not(:checked)').prop('disabled', true);
        $(this).addClass('display-hide');
        $('#dor_ext_preview_tab #dor_email').prop('disabled', 'true'); //added by sahil to make this field non editable.

    });



    $('#email_act2').submit(function (e) {
        e.preventDefault();
        $("#email_act2 :disabled").removeAttr('disabled');
        $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#email_act_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#email_act_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#email_act2').serializeObject());
        $('#single_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('#single_form2 .edit').removeClass('display-hide');
        console.log('data: ' + data)
        $.ajax({
            type: "POST",
            url: "email_act_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;
                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#email_act2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_act2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for activation") > -1) {
                            $('#email_act2 #single_email1_err').html("")
                        } else {
                            $('#email_act2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_act2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#email_act2 #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#email_act2 #single_dor').focus();
                    error_flag = false;
                    $('#email_act2 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_act2 #imgtxt').val("");
                } else {

                    $('#email_act2 #single_dor_err').html("")
                }
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#email_act2 #useremployment_error').focus();
                    $('#email_act2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#email_act2 #minerror').focus();
                    $('#email_act2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#email_act2 #deperror').focus();
                    $('#email_act2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#email_act2 #other_dept').focus();
                    $('#email_act2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#email_act2 #smierror').focus();
                    $('#email_act2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#email_act2 #state_error').focus();
                    $('#email_act2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#email_act2 #org_error').focus();
                    $('#email_act2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#email_act2 #ca_design').focus();
                    $('#email_act2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#email_act2 #hod_name').focus();
                    $('#email_act2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#email_act2 #hod_mobile').focus();
                    $('#email_act2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {

                    $('#email_act2 #hod_email').focus();
                    $('#email_act2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#email_act2 #hod_email').focus();
                    $('#email_act2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#single_form2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#email_act2 #hod_tel').focus();
                    $('#email_act2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodtel_error').html("");
                }
                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#email_act2 #under_sec_name').focus();
                    $('#email_act2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#email_act2 #under_sec_mobile').focus();
                    $('#email_act2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#email_act2 #under_sec_email').focus();
                    $('#email_act2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#email_act2 #under_sec_desig').focus();
                    $('#email_act2 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#email_act2 #under_sec_tel').focus();
                    $('#email_act2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#email_act2 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                if (error_flag) {
                    $('#large4').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: "email_act"},
                        datatype: JSON,
                        success: function (data)
                        {
                            window.location.href = 'e_sign'
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
    $('#email_act2 .edit').click(function () {

        var employment = $('#email_act_preview_tab #user_employment').val();
        var min = $('#email_act_preview_tab #min').val();
        var dept = $('#email_act_preview_tab #dept').val();
        var statecode = $('#email_act_preview_tab #stateCode').val();
        var Smi = $('#email_act_preview_tab #Smi').val();
        var Org = $('#email_act_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_act_preview_tab #min');
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
                var select = $('#email_act_preview_tab #dept');
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
                var select = $('#email_act_preview_tab #stateCode');
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
                var select = $('#email_act_preview_tab #Smi');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_act_preview_tab #Org');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#email_act_preview_tab #central_div').hide();
            $('#email_act_preview_tab #state_div').hide();
            $('#email_act_preview_tab #other_div').hide();
        }

        $('#email_act_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#email_act_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_act_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        $('#email_act_preview_tab #REditPreview #hod_email').removeAttr('disabled');

        $(this).addClass('display-hide');
        //$(this).hide();
    });
    $('#email_act2 #confirm').click(function (e) {

        e.preventDefault();
        $("#email_act2 :disabled").removeAttr('disabled');
        $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#email_act_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#email_act_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#email_act2').serializeObject());
        $('#email_act_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        console.log("data in email activate for validate" + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "email_act_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#email_act2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_act2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for activation") > -1) {
                            $('#email_act2 #single_email1_err').html("")
                        } else {
                            $('#email_act2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_act2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }



                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#email_act2 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#email_act2 #useremployment_error').focus();
                    $('#email_act2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#email_act2 #minerror').focus();
                    $('#email_act2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#email_act2 #deperror').focus();
                    $('#email_act2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#email_act2 #other_dept').focus();
                    $('#email_act2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#email_act2 #smierror').focus();
                    $('#email_act2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#email_act2 #state_error').focus();
                    $('#email_act2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#email_act2 #org_error').focus();
                    $('#email_act2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#email_act2 #ca_design').focus();
                    $('#email_act2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#email_act2 #hod_name').focus();
                    $('#email_act2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#email_act2 #hod_mobile').focus();
                    $('#email_act2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {

                    $('#email_act2 #hod_email').focus();
                    $('#email_act2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodemail_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#email_act2 #hod_email').focus();
                    $('#email_act2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#email_act2 #hod_tel').focus();
                    $('#email_act2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodtel_error').html("");
                }



                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#email_act2 #under_sec_name').focus();
                    $('#email_act2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#email_act2 #under_sec_mobile').focus();
                    $('#email_act2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#email_act2 #under_sec_email').focus();
                    $('#email_act2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#email_act2 #under_sec_tel').focus();
                    $('#email_act2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#email_act2 #under_sec_desig').focus();
                    $('#email_act2 #under_sec_desg_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#email_act2 #under_sec_desig_error').html("");
                }
                // profile on 20th march

                if (!error_flag) {
                    $("#email_act2 :disabled").removeAttr('disabled');
                    $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#email_act_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#email_act2 #tnc').is(":checked"))
                    {
                        $('#email_act2 #tnc_error').html("");
                        $('#email_act_confirm').removeClass('display-hide');
                        $('#stack3').modal({backdrop: 'static', keyboard: false});


                        if (jsonvalue.form_details.req_for == "eoffice")
                        {

                            $('#email_act_confirm #fill_hod_name').html("Rachna Srivastava");
                            $('#email_act_confirm #fill_hod_email').html("rachna_sri@nic.in");
                            $('#email_act_confirm #fill_hod_mobile').html("+91XXXXXXX166");


                        } else {

//                            if (jsonvalue.form_details.min == "External Affairs")
//                            {
//                                $('#email_act_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                                $('#email_act_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                                $('#email_act_confirm #fill_hod_mobile').html("+919384664224");
//                            } 

                            // else {
                            $('#email_act_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                            $('#email_act_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                            var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                            var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                            $('#email_act_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                            // }
                            $('#email_act_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        }
                        $('#email_act_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#email_act2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#email_act2 #tnc").removeAttr('disabled');
                        $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });

    });
    $('#email_act_confirm #confirmYes').click(function () {
        $('#email_act2').submit();
        $('#stack3').modal('toggle');
    });
    $(document).on('click', '#email_act_preview .edit', function () {
        //$('#singleuser_preview .edit').click(function () {

        var employment = $('#email_act_preview_tab #user_employment').val();
        var min = $('#email_act_preview_tab #min').val();
        var dept = $('#email_act_preview_tab #dept').val();
        var statecode = $('#email_act_preview_tab #stateCode').val();
        var Smi = $('#email_act_preview_tab #Smi').val();
        var Org = $('#email_act_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_act_preview_tab #min');
                select.find('option').remove();
                // alert(select)
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    console.log("min:::::::::" + min + "value::::::::::" + value)
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
                var select = $('#email_act_preview_tab #dept');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_act_preview_tab #stateCode');
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
                var select = $('#email_act_preview_tab #Smi');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#email_act_preview_tab #central_div').hide();
            $('#email_act_preview_tab #state_div').hide();
            $('#email_act_preview_tab #other_div').show();
            $('#email_act_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_act_preview_tab #Org');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#email_act_preview_tab #central_div').hide();
            $('#email_act_preview_tab #state_div').hide();
            $('#email_act_preview_tab #other_div').hide();
        }
        $('#email_act_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#email_act_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#email_act_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');

        $('#email_act_preview_tab').find('input[type="radio"]').prop('disabled', 'true');
        //$('#email_act_preview_tab').find('input[type="radio"]').removeAttr('disabled');

        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#email_act_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#email_act_preview .save-changes").html("Update");
        }
        // code end
    });
    $(document).on('click', '#email_act_preview #confirm', function () {
        if ($('#email_act_preview #confirm').val() === "work_order_upload")
        {
            if ($('#email_act_preview #tnc').is(":checked"))
            {
                $('#email_act_preview_form').modal('toggle');
                $('#email_act_preview #email_act2 #tnc_error').html("");
                $('#email_act_confirm_prvw').removeClass('display-hide');
                $('#stack_upload').modal({backdrop: 'static', keyboard: false});
//                $('#email_act_confirm_prvw #fill_hod_name').html("test");
//                $('#email_act_confirm_prvw #fill_hod_email').html("test");
//                var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
//                var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
//                $('#email_act_confirm_prvw #fill_hod_mobile').html("test");
                $('#email_act_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
            } else {
                $('#email_act2 #tnc_error').html("Please agree to Terms and Conditions");
                $("#email_act2 #tnc").removeAttr('disabled');
                $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
            }
        } else {

            $('#email_act_preview').submit();
            //$('#email_act_preview_form').modal('toggle');

        }
    });
    $(document).on('click', '#email_act_confirm_prvw #confirmYes', function () {
        $('#email_act_preview').submit();
        $('#stack3').modal('toggle');
    });
    $('#email_act_preview').submit(function (e) {

        e.preventDefault();
        $("#email_act_preview :disabled").removeAttr('disabled');
        $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march   
        $('#email_act_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march // nikki 22 jan 2019   
        $('#email_act_preview_tab #user_email').removeAttr('disabled');// 20th march                  
        var data = JSON.stringify($('#email_act_preview').serializeObject());
        $('#email_act_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#email_act_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 jan 2019 
        console.log('DATA inside submit preview: ' + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        var action_type = "";
        if ($('#email_act_preview #confirm').val() === "work_order_upload")
        {
            action_type = "submit";

        } else {

            action_type = "update";
        }

        $.ajax({
            type: "POST",
            url: "email_act_tab2",
            data: {data: data, action_type: action_type, CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;



                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#email_act_preview #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_act_preview #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for activation") > -1) {
                            $('#email_act_preview #single_email1_err').html("")
                        } else {
                            $('#email_act_preview #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_act_preview #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }




                if (jsonvalue.error.dor_err !== null && jsonvalue.error.dor_err !== "" && jsonvalue.error.dor_err !== undefined)
                {
                    $('#email_act_preview #single_dor_err').html(jsonvalue.error.dor_err)
                    $('#email_act_preview #single_dor').focus();
                    error_flag = false;
                } else {

                    $('#email_act_preview #single_dor_err').html("")
                }

                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#email_act_preview #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#email_act_preview #useremployment_error').focus();
                    $('#email_act_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#email_act_preview #minerror').focus();
                    $('#email_act_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#email_act_preview #deperror').focus();
                    $('#email_act_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#email_act_preview #other_dept').focus();
                    $('#email_act_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#email_act_preview #smierror').focus();
                    $('#email_act_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#email_act_preview #state_error').focus();
                    $('#email_act_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#email_act_preview #org_error').focus();
                    $('#email_act_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #org_error').html("");
                }

                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {

                    $('#email_act_preview #under_sec_name').focus();
                    $('#email_act_preview #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#email_act_preview #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#email_act_preview #under_sec_mobile').focus();
                    $('#email_act_preview #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#email_act_preview #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#email_act_preview #under_sec_email').focus();
                    $('#email_act_preview #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#email_act_preview #under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#email_act_preview #under_sec_desig').focus();
                    $('#email_act_preview #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#email_act_preview #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#email_act_preview #under_sec_tel').focus();
                    $('#email_act_preview #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {

                    $('#email_act_preview #under_sec_tel_error').html("");
                }
                // profile 20th march 




                if (!error_flag)
                {

                    $("#email_act_preview :disabled").removeAttr('disabled');
                    $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#email_act_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                    //$('#mobile_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    //$("#mobile_form2 #tnc").removeAttr('disabled');
                } else {

                    if ($('#email_act_preview #confirm').val() === "work_order_upload")
                    {

                        $.ajax({
                            type: "POST",
                            url: "esign",
                            data: {formtype: "email_act"},
                            datatype: JSON,
                            success: function (data)
                            {
                                window.location.href = 'e_sign'
                            }, error: function ()
                            {
                                $('#tab1').show();
                            }
                        });
                    } else {

                        $('#email_act_preview_form').modal('toggle');
                    }



                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });



    $('#email_deact1').submit(function (e) {
        e.preventDefault();
        var data = JSON.stringify($('#email_deact1').serializeObject());
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        console.log("data" + data)
        $.ajax({
            type: "POST",
            url: "email_deact_tab1",
            data: {data: data, CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {

                    $('#email_deact1 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_deact1 #single_email1').focus();
                    error_flag = false;
                } else {
                    $('#email_deact1 #single_email1_err').html("");

//                    alert("inside else");
//                    if (jsonvalue.error.errorMsg1 !== undefined) {
//                        alert("inside if1111");
////                        if (jsonvalue.error.errorMsg1.indexOf("is available for deactivation") > -1) {
////                            alert("inside if2222");
////                            $('#email_deact1 #single_email1_err').html("")
////                        } else {
////                            $('#email_deact1 #single_email1_err').html(jsonvalue.error.errorMsg1)
////                            $('#email_deact1 #single_email1').focus();
////                            error_flag = false;
////                        }
//                    }
                }

                if (jsonvalue.error.cap_error !== null && jsonvalue.error.cap_error !== "" && jsonvalue.error.cap_error !== undefined)
                {

                    $('#email_deact1 #captchaerror').html(jsonvalue.error.cap_error)
                    error_flag = false;
                    $('#email_deact1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_deact1 #imgtxt').val("");
                } else {
                    $('#email_deact1 #captchaerror').html("")
                }
                if (jsonvalue.error.errorMsg1 !== null && jsonvalue.error.errorMsg1 !== "" && jsonvalue.error.errorMsg1 !== undefined)
                {
                    //alert("inside 33")
                    $('#email_deact1 #deact_email1_err').html(jsonvalue.error.errorMsg1)
                    error_flag = false;
                    $('#email_deact1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_deact1 #imgtxt').val("");
                    document.getElementById("deact_email1").focus();
                } else {
                    $('#email_deact1 #deact_email1_err').html("")
                }


                // start, code added by pr on 22ndjan18

                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#email_deact1 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                    $('#email_deact1 #captcha1').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                    $('#email_deact1 #imgtxt').val("");
                }

                // alert(error_flag)
                if (!error_flag) {
                } else {

                    if (jsonvalue.profile_values.user_employment === 'Central' || jsonvalue.profile_values.user_employment === 'UT') {
                        $('#email_deact2 #central_div').show();
                        $('#email_deact2 #state_div').hide();
                        $('#email_deact2 #other_div').hide();
                        $('#email_deact2 #other_text_div').addClass("display-hide");
                        var select = $('#email_deact2 #min');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.min).text(jsonvalue.profile_values.min).appendTo(select);
                        var select = $('#email_deact2 #dept');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#email_deact2 #other_text_div').removeClass("display-hide");
                            $('#email_deact2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }


                    } else if (jsonvalue.profile_values.user_employment === 'State') {
                        $('#email_deact2 #central_div').hide();
                        $('#email_deact2 #state_div').show();
                        $('#email_deact2 #other_div').hide();
                        $('#email_deact2 #other_text_div').addClass("display-hide");
                        var select = $('#email_deact2 #stateCode');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.stateCode).text(jsonvalue.profile_values.stateCode).appendTo(select);
                        var select = $('#email_deact2 #Smi');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.dept).text(jsonvalue.profile_values.dept).appendTo(select);

                        if (jsonvalue.profile_values.dept.toString().toLowerCase() === 'other') {
                            $('#email_deact2 #other_text_div').removeClass("display-hide");
                            $('#email_deact2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else if (jsonvalue.profile_values.user_employment === 'Others' || jsonvalue.profile_values.user_employment === "Psu" || jsonvalue.profile_values.user_employment === "Const" || jsonvalue.profile_values.user_employment === "Nkn" || jsonvalue.profile_values.user_employment === "Project") {
                        $('#email_deact2 #central_div').hide();
                        $('#email_deact2 #state_div').hide();
                        $('#email_deact2 #other_div').show();
                        $('#email_deact2 #other_text_div').addClass("display-hide");
                        var select = $('#email_deact2 #Org');
                        select.find('option').remove();
                        $('<option>').val(jsonvalue.profile_values.Org).text(jsonvalue.profile_values.Org).appendTo(select);
                        if (jsonvalue.profile_values.Org.toString().toLowerCase() === 'other') {
                            $('#email_deact2 #other_text_div').removeClass("display-hide");
                            $('#email_deact2 #other_dept').val(jsonvalue.profile_values.other_dept);
                        }

                    } else {
                        $('#email_deact2 #central_div').hide();
                        $('#email_deact2 #state_div').hide();
                        $('#email_deact2 #other_div').hide();
                    }

                    $('#email_deact2 #user_employment').val(jsonvalue.profile_values.user_employment);
                    $('#email_deact2 #stateCode').val(jsonvalue.profile_values.stateCode);
                    $('#email_deact2 #user_name').val(jsonvalue.profile_values.cn);
                    $('#email_deact2 #user_design').val(jsonvalue.profile_values.desig);
                    $('#email_deact2 #user_address').val(jsonvalue.profile_values.postalAddress);
                    $('#email_deact2 #user_city').val(jsonvalue.profile_values.nicCity);
                    $('#email_deact2 #user_state').val(jsonvalue.profile_values.state);
                    $('#email_deact2 #user_pincode').val(jsonvalue.profile_values.pin);
                    $('#email_deact2 #user_ophone').val(jsonvalue.profile_values.ophone);
                    $('#email_deact2 #user_rphone').val(jsonvalue.profile_values.rphone);
                    var startMobile = jsonvalue.profile_values.mobile.substring(0, 3);
                    var endMobile = jsonvalue.profile_values.mobile.substring(10, 13);
                    $('#email_deact2 #user_mobile').val(startMobile + 'XXXXXXX' + endMobile);
                    $('#email_deact2 #user_email').val(jsonvalue.profile_values.email);
                    $('#email_deact2 #user_empcode').val(jsonvalue.profile_values.emp_code);
                    $('#email_deact2 #hod_name').val(jsonvalue.profile_values.hod_name);
                    $('#email_deact2 #hod_email').val(jsonvalue.profile_values.hod_email);
                    $('#email_deact2 #hod_tel').val(jsonvalue.profile_values.hod_tel);
                    var ROstartMobile = jsonvalue.profile_values.hod_mobile.substring(0, 3);
                    var ROendMobile = jsonvalue.profile_values.hod_mobile.substring(10, 13);
                    $('#email_deact2 #hod_mobile').val(ROstartMobile + 'XXXXXXX' + ROendMobile);
                    $('#email_deact2 #ca_design').val(jsonvalue.profile_values.ca_design);
                    $('#email_deact2 #dup_mail_exist').val(jsonvalue.form_details.dup_mail_exist);
                    $('#email_deact2 #deact_email1').val(jsonvalue.form_details.deact_email1);
                    $('#email_deact2 #divDor').addClass('display-hide');
                    $('#email_deact2 #cert_div').addClass("display-hide");
                    $('#email_deact2 #emp_type_div').addClass("display-hide");
                    $('#large5').modal({backdrop: 'static', keyboard: false});
                    $('#email_deact2 .edit').removeClass('display-hide');
                    $('#email_deact_preview_tab').find('input, textarea, button, select').attr('disabled', 'true');
                    // $('#email_act_preview_tab #profile_div').find('input, textarea, button, select').attr('disabled', 'true');
                    $("#email_deact2 :button[type='button']").removeAttr('disabled');
                    $("#email_deact2 #tnc").removeAttr('disabled');




                }

            }, error: function ()
            {
                console.log('error');
            }
        });
    });
    $('#email_deact2').submit(function (e) {
        e.preventDefault();
        $("#email_deact2 :disabled").removeAttr('disabled');
        $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#email_deact_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#email_deact_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#email_deact2').serializeObject());
        $('#email_deact_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18
        $('#email_deact2 .edit').removeClass('display-hide');
        console.log('data: ' + data)
        $.ajax({
            type: "POST",
            url: "email_deact_tab2",
            data: {data: data, action_type: "submit", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18
                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);
                var error_flag = true;

                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#email_deact2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_deact2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for deactivation") > -1) {
                            $('#email_deact2 #single_email1_err').html("")
                        } else {
                            $('#email_deact2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_deact2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }

                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#email_deact2 #useremployment_error').focus();
                    $('#email_deact2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#email_deact2 #minerror').focus();
                    $('#email_deact2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#email_deact2 #deperror').focus();
                    $('#email_deact2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#email_deact2 #other_dept').focus();
                    $('#email_deact2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#email_deact2 #smierror').focus();
                    $('#email_deact2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#email_deact2 #state_error').focus();
                    $('#email_deact2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#email_deact2 #org_error').focus();
                    $('#email_deact2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#email_deact2 #ca_design').focus();
                    $('#email_deact2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#email_deact2 #hod_name').focus();
                    $('#email_deact2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#email_act2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#email_deact2 #hod_mobile').focus();
                    $('#email_deact2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {

                    $('#email_deact2 #hod_email').focus();
                    $('#email_deact2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodemail_error').html("");
                }
                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#email_deact2 #hod_email').focus();
                    $('#email_deact2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#email_deact2 #hod_tel').focus();
                    $('#email_deact2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodtel_error').html("");
                }
                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#email_deact2 #under_sec_name').focus();
                    $('#email_deact2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#email_deact2 #under_sec_mobile').focus();
                    $('#email_deact2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#email_deact2 #under_sec_email').focus();
                    $('#email_deact2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#email_deact2 #under_sec_desig').focus();
                    $('#email_deact2 #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#email_deact2 #under_sec_tel').focus();
                    $('#email_deact2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {
                    $('#email_deact2 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                if (error_flag) {
                    $('#large4').modal('toggle');
                    $.ajax({
                        type: "POST",
                        url: "esign",
                        data: {formtype: "email_deact"},
                        datatype: JSON,
                        success: function (data)
                        {
                            window.location.href = 'e_sign'
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
    $('#email_deact2 .edit').click(function () {

        var employment = $('#email_deact_preview_tab #user_employment').val();
        var min = $('#email_deact_preview_tab #min').val();
        var dept = $('#email_deact_preview_tab #dept').val();
        var statecode = $('#email_deact_preview_tab #stateCode').val();
        var Smi = $('#email_deact_preview_tab #Smi').val();
        var Org = $('#email_deact_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_deact_preview_tab #min');
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
                var select = $('#email_deact_preview_tab #dept');
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
                var select = $('#email_deact_preview_tab #stateCode');
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
                var select = $('#email_deact_preview_tab #Smi');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#central_div').hide();
            $('#state_div').hide();
            $('#other_div').show();
            $('#other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_deact_preview_tab #Org');
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
                $('#email_deact_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#email_deact_preview_tab #central_div').hide();
            $('#email_deact_preview_tab #state_div').hide();
            $('#email_deact_preview_tab #other_div').hide();
        }

        $('#email_deact_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#email_deact_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled');  // 20th march
        $('#email_deact_preview_tab #REditPreview #hod_email').removeAttr('disabled');

        $(this).addClass('display-hide');
        //$(this).hide();
    });
    $('#email_deact2 #confirm').click(function (e) {

        e.preventDefault();
        $("#email_deact2 :disabled").removeAttr('disabled');
        $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#email_deact_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march        
        $('#email_deact_preview_tab #user_email').removeAttr('disabled'); // 20th march
        var data = JSON.stringify($('#email_deact2').serializeObject());
        $('#email_act_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        console.log("data in email activate for validate" + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 22ndjan18

        $.ajax({
            type: "POST",
            url: "email_deact_tab2",
            data: {data: data, action_type: "validate", CSRFRandom: CSRFRandom},
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;


                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#email_deact2 #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_deact2 #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for deactivation") > -1) {
                            $('#email_deact2 #single_email1_err').html("")
                        } else {
                            $('#email_deact2 #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_deact2 #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }



                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#email_deact2 #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // end, code added by pr on 22ndjan18

                // profile on 20th march
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#email_deact2 #useremployment_error').focus();
                    $('#email_deact2 #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#email_deact2 #minerror').focus();
                    $('#email_deact2 #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#email_deact2 #deperror').focus();
                    $('#email_deact2 #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#email_deact2 #other_dept').focus();
                    $('#email_deact2 #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#email_deact2 #smierror').focus();
                    $('#email_deact2 #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#email_deact2 #state_error').focus();
                    $('#email_deact2 #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#email_deact_tab2 #org_error').focus();
                    $('#email_deact2 #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #org_error').html("");
                }
                if (jsonvalue.error.ca_desg_error !== null && jsonvalue.error.ca_desg_error !== "" && jsonvalue.error.ca_desg_error !== undefined)
                {
                    $('#email_deact2 #ca_design').focus();
                    $('#email_deact2 #cadesign_error').html(jsonvalue.error.ca_desg_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #cadesign_error').html("");
                }
                if (jsonvalue.error.hod_name_error !== null && jsonvalue.error.hod_name_error !== "" && jsonvalue.error.hod_name_error !== undefined)
                {
                    $('#email_deact2 #hod_name').focus();
                    $('#email_deact2 #hodname_error').html(jsonvalue.error.hod_name_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodname_error').html("");
                }
                if (jsonvalue.error.hod_mobile_error !== null && jsonvalue.error.hod_mobile_error !== "" && jsonvalue.error.hod_mobile_error !== undefined)
                {
                    $('#email_deact2 #hod_mobile').focus();
                    $('#email_deact2 #hodmobile_error').html(jsonvalue.error.hod_mobile_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodmobile_error').html("");
                }
                if (jsonvalue.error.hod_email_error !== null && jsonvalue.error.hod_email_error !== "" && jsonvalue.error.hod_email_error !== undefined)
                {

                    $('#email_deact2 #hod_email').focus();
                    $('#email_deact2 #hodemail_error').html(jsonvalue.error.hod_email_error);
                    error_flag = false;
                } else {
                    $('#email_deact_tab2 #hodemail_error').html("");
                }

                if (jsonvalue.error.hod_email_error1 !== null && jsonvalue.error.hod_email_error1 !== "" && jsonvalue.error.hod_email_error1 !== undefined)
                {

                    $('#email_deact2 #hod_email').focus();
                    $('#email_deact2 #hodemail_error1').html(jsonvalue.error.hod_email_error1);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodemail_error1').html("");
                }
                if (jsonvalue.error.hod_telephone_error !== null && jsonvalue.error.hod_telephone_error !== "" && jsonvalue.error.hod_telephone_error !== undefined)
                {
                    $('#email_deact2 #hod_tel').focus();
                    $('#email_deact2 #hodtel_error').html(jsonvalue.error.hod_telephone_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #hodtel_error').html("");
                }



                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {
                    $('#email_deact2 #under_sec_name').focus();
                    $('#email_deact2 #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#email_deact2 #under_sec_mobile').focus();
                    $('#email_deact2 #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#email_deact2 #under_sec_email').focus();
                    $('#email_deact2 #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_email_error').html("");
                }

                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {

                    $('#email_deact2 #under_sec_tel').focus();
                    $('#email_deact2 #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {
                    $('#email_deact2 #under_sec_tel_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#email_deact2 #under_sec_desig').focus();
                    $('#email_deact2 #under_sec_desg_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#email_deact2 #under_sec_desig_error').html("");
                }
                // profile on 20th march

                if (!error_flag) {
                    $("#email_deact2 :disabled").removeAttr('disabled');
                    $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    $('#email_deact_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                } else {
                    if ($('#email_deact2 #tnc').is(":checked"))
                    {
                        $('#email_deact2 #tnc_error').html("");
                        $('#email_deact_confirm').removeClass('display-hide');
                        $('#stack3').modal({backdrop: 'static', keyboard: false});

//                        if (jsonvalue.form_details.min == "External Affairs")
//                        {
//                            $('#email_deact_confirm #fill_hod_name').html("Dr S JANAKIRAMAN");
//                            $('#email_deact_confirm #fill_hod_email').html("jsegit@mea.gov.in");
//                            $('#email_deact_confirm #fill_hod_mobile').html("+919384664224");
//                        }
                        // else {
                        $('#email_deact_confirm #fill_hod_name').html(jsonvalue.form_details.hod_name);
                        $('#email_deact_confirm #fill_hod_email').html(jsonvalue.form_details.hod_email);
                        var startMobile = jsonvalue.form_details.hod_mobile.substring(0, 3);
                        var endMobile = jsonvalue.form_details.hod_mobile.substring(10, 13);
                        $('#email_deact_confirm #fill_hod_mobile').html(startMobile + 'XXXXXXX' + endMobile);
                        // }
                        $('#email_deact_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#email_deact_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    } else {
                        $('#email_deact2 #tnc_error').html("Please agree to Terms and Conditions");
                        $("#email_deact2 #tnc").removeAttr('disabled');
                        $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                        $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
                    }
                }
            }, error: function ()
            {
                $('#tab1').show();
            }
        });

    });
    $('#email_deact_confirm #confirmYes').click(function () {
        $('#email_deact2').submit();
        $('#stack3').modal('toggle');
    });
    $(document).on('click', '#email_deact_preview .edit', function () {
        //$('#singleuser_preview .edit').click(function () {

        var employment = $('#email_deact_preview_tab #user_employment').val();
        var min = $('#email_deact_preview_tab #min').val();
        var dept = $('#email_deact_preview_tab #dept').val();
        var statecode = $('#email_deact_preview_tab #stateCode').val();
        var Smi = $('#email_deact_preview_tab #Smi').val();
        var Org = $('#email_deact_preview_tab #Org').val();

        if (employment === 'Central' || employment === 'UT') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_deact_preview_tab #min');
                select.find('option').remove();
                // alert(select)
                $('<option>').val("").text("-SELECT-").appendTo(select);
                $.each(response, function (index, value) {
                    console.log("min:::::::::" + min + "value::::::::::" + value)
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
                var select = $('#email_deact_preview_tab #dept');
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
                $('#email_deact_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'State') {
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_deact_preview_tab #stateCode');
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
                var select = $('#email_deact_preview_tab #Smi');
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
                $('#email_act_preview_tab #other_text_div').removeClass("display-hide");

            }

        } else if (employment === 'Others' || employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project") {
            $('#email_deact_preview_tab #central_div').hide();
            $('#email_deact_preview_tab #state_div').hide();
            $('#email_deact_preview_tab #other_div').show();
            $('#email_deact_preview_tab #other_text_div').addClass("display-hide");
            $.get('centralMinistry', {
                orgType: employment
            }, function (response) {
                var select = $('#email_deact_preview_tab #Org');
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
                $('#email_deact_preview_tab #other_text_div').removeClass("display-hide");

            }


        } else {
            $('#email_deact_preview_tab #central_div').hide();
            $('#email_deact_preview_tab #state_div').hide();
            $('#email_deact_preview_tab #other_div').hide();
        }
        $('#email_deact_preview_tab').find('input, textarea, button, select').prop('disabled', 'false');
        $('#email_deact_preview_tab').find('input, textarea, button, select').removeAttr('disabled');
        $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march
        $('#email_deact_preview_tab #USEditPreview').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab').find('input[type="radio"]').prop('disabled', 'true');
        //$('#email_act_preview_tab').find('input[type="radio"]').removeAttr('disabled');

        $(this).addClass('display-hide');
        // below code added by pr on 25thjan18                            
        if ($("#comingFrom").val("admin"))
        {
            $("#email_deact_preview .save-changes").removeClass('display-hide');// line added by pr on 25thjan18
            $("#email_deact_preview .save-changes").html("Update");
        }
        // code end
    });
    $(document).on('click', '#email_deact_preview #confirm', function () {
        $('#email_deact_preview').submit();
    });
    $('#email_deact_preview').submit(function (e) {

        e.preventDefault();
        $("#email_deact_preview :disabled").removeAttr('disabled');
        $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
        $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march   
        $('#email_deact_preview_tab #REditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march // nikki 22 jan 2019   
        $('#email_deact_preview_tab #user_email').removeAttr('disabled');// 20th march                  
        var data = JSON.stringify($('#email_deact_preview').serializeObject());
        $('#email_deact_preview_tab #user_email').prop('disabled', 'true'); // 20th march
        $('#email_deact_preview_tab #REditPreview').find('input, textarea, button, select').prop('disabled', 'true'); // nikki 22 jan 2019 
        console.log('DATA inside submit preview: ' + data)
        var CSRFRandom = $("#CSRFRandom").val(); // line added by pr on 23rdjan18
        var action_type = "";
        $.ajax({
            type: "POST",
            url: "email_deact_tab2",
            data: {data: data, action_type: "update", CSRFRandom: CSRFRandom}, // line modified  by pr on 23rdjan18
            datatype: JSON,
            success: function (data)
            {

                resetCSRFRandom();// line added by pr on 22ndjan18

                var myJSON = JSON.stringify(data);
                var jsonvalue = JSON.parse(myJSON);

                var error_flag = true;



                if (jsonvalue.error.pref1_error !== null && jsonvalue.error.pref1_error !== "" && jsonvalue.error.pref1_error !== undefined)
                {
                    $('#email_deact_preview #single_email1_err').html(jsonvalue.error.pref1_error)
                    $('#email_deact_preview #single_email1').focus();
                    error_flag = false;
                } else {
                    if (jsonvalue.error.errorMsg1 !== undefined) {
                        if (jsonvalue.error.errorMsg1.indexOf("is available for deactivation") > -1) {
                            $('#email_deact_preview #single_email1_err').html("")
                        } else {
                            $('#email_deact_preview #single_email1_err').html(jsonvalue.error.errorMsg1)
                            $('#email_deact_preview #single_email1').focus();
                            error_flag = false;
                        }
                    }
                }


                if (jsonvalue.error.csrf_error !== null && jsonvalue.error.csrf_error !== "" && jsonvalue.error.csrf_error !== undefined)
                {

                    $('#email_deact_preview #captchaerror').html(jsonvalue.error.csrf_error);
                    error_flag = false;
                }

                // profile 20th march 
                if (jsonvalue.error.employment_error !== null && jsonvalue.error.employment_error !== "" && jsonvalue.error.employment_error !== undefined)
                {
                    $('#email_deact_preview #useremployment_error').focus();
                    $('#email_deact_preview #useremployment_error').html(jsonvalue.error.employment_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #useremployment_error').html("");
                }
                if (jsonvalue.error.ministry_error !== null && jsonvalue.error.ministry_error !== "" && jsonvalue.error.ministry_error !== undefined)
                {
                    $('#email_deact_preview #minerror').focus();
                    $('#email_deact_preview #minerror').html(jsonvalue.error.ministry_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #minerror').html("");
                }
                if (jsonvalue.error.dept_error !== null && jsonvalue.error.dept_error !== "" && jsonvalue.error.dept_error !== undefined)
                {
                    $('#email_deact_preview #deperror').focus();
                    $('#email_deact_preview #deperror').html(jsonvalue.error.dept_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #deperror').html("");
                }
                if (jsonvalue.error.dept_other_error !== null && jsonvalue.error.dept_other_error !== "" && jsonvalue.error.dept_other_error !== undefined)
                {
                    $('#email_deact_preview #other_dept').focus();
                    $('#email_deact_preview #other_dep_error').html(jsonvalue.error.dept_other_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #other_dep_error').html("");
                }
                if (jsonvalue.error.state_dept_error !== null && jsonvalue.error.state_dept_error !== "" && jsonvalue.error.state_dept_error !== undefined)
                {
                    $('#email_deact_preview #smierror').focus();
                    $('#email_deact_preview #smierror').html(jsonvalue.error.state_dept_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #smierror').html("");
                }
                if (jsonvalue.error.state_error !== null && jsonvalue.error.state_error !== "" && jsonvalue.error.state_error !== undefined)
                {
                    $('#email_deact_preview #state_error').focus();
                    $('#email_deact_preview #state_error').html(jsonvalue.error.state_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #state_error').html("");
                }
                if (jsonvalue.error.org_error !== null && jsonvalue.error.org_error !== "" && jsonvalue.error.org_error !== undefined)
                {
                    $('#email_deact_preview #org_error').focus();
                    $('#email_deact_preview #org_error').html(jsonvalue.error.org_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #org_error').html("");
                }

                if (jsonvalue.error.under_sec_name_error !== null && jsonvalue.error.under_sec_name_error !== "" && jsonvalue.error.under_sec_name_error !== undefined)
                {

                    $('#email_deact_preview #under_sec_name').focus();
                    $('#email_deact_preview #under_sec_name_error').html(jsonvalue.error.under_sec_name_error);
                    error_flag = false;
                } else {
                    $('#email_deact_preview #under_sec_name_error').html("");
                }
                if (jsonvalue.error.under_sec_mobile_error !== null && jsonvalue.error.under_sec_mobile_error !== "" && jsonvalue.error.under_sec_mobile_error !== undefined)
                {
                    $('#email_deact_preview #under_sec_mobile').focus();
                    $('#email_deact_preview #under_sec_mobile_error').html(jsonvalue.error.under_sec_mobile_error);
                    error_flag = false;
                } else {

                    $('#email_deact_preview #under_sec_mobile_error').html("");
                }
                if (jsonvalue.error.under_sec_email_error !== null && jsonvalue.error.under_sec_email_error !== "" && jsonvalue.error.under_sec_email_error !== undefined)
                {
                    $('#email_deact_preview #under_sec_email').focus();
                    $('#email_deact_preview #under_sec_email_error').html(jsonvalue.error.under_sec_email_error);
                    error_flag = false;
                } else {

                    $('#email_deact_preview #under_sec_email_error').html("");
                }
                if (jsonvalue.error.under_sec_desg_error !== null && jsonvalue.error.under_sec_desg_error !== "" && jsonvalue.error.under_sec_desg_error !== undefined)
                {
                    $('#email_deact_preview #under_sec_desig').focus();
                    $('#email_deact_preview #under_sec_desig_error').html(jsonvalue.error.under_sec_desg_error);
                    error_flag = false;
                } else {

                    $('#email_deact_preview #under_sec_desig_error').html("");
                }
                if (jsonvalue.error.under_sec_telephone_error !== null && jsonvalue.error.under_sec_telephone_error !== "" && jsonvalue.error.under_sec_telephone_error !== undefined)
                {
                    $('#email_deact_preview #under_sec_tel').focus();
                    $('#email_deact_preview #under_sec_tel_error').html(jsonvalue.error.under_sec_telephone_error);
                    error_flag = false;
                } else {

                    $('#email_deact_preview #under_sec_tel_error').html("");
                }
                // profile 20th march 




                if (!error_flag)
                {

                    $("#email_deact_preview :disabled").removeAttr('disabled');
                    $('#email_deact_preview_tab #profile_div').find('input, textarea, button, select').prop('disabled', 'true');
                    $('#email_deact_preview_tab #EditPreview').find('input, textarea, button, select').removeAttr('disabled'); // 20th march 
                    //$('#mobile_preview_tab').find('input, textarea, button, select').prop('disabled', 'true');
                    //$("#mobile_form2 #tnc").removeAttr('disabled');
                } else {

                    $('#email_deact_preview_form').modal('toggle');
                }

            }, error: function (request, error)
            {
                console.log('error');
            }
        });
    });

});

function resetCSRFRandom()
{


    $.ajax({
        type: "POST",
        url: "resetCSRFRandom",
        datatype: JSON,
        success: function (jsonResponse)
        {
            $("#CSRFRandom").val(jsonResponse.random);
        }
        , error: function ()
        {
        }
    });

}

$('input[type="radio"]').click(function () {
//    if ($(this).val() == 'email_activate' || $(this).val() == 'email_deactivate') {
//        $("#email_act_deact").removeClass('d-none');
//        $("#single_user_form").addClass('display-hide');
//        $("#bulk_user_form").addClass('display-hide');
//        $("#nkn_single_form").addClass('display-hide');
//        $("#nkn_bulk_form").addClass('display-hide');
//        $("#gem_user_form").addClass('display-hide');
//
//    } else {
    //$("#email_act_deact").addClass('d-none');
    //}

//    if ($(this).val() == 'email_activate') {
//        $("#email_activate_div").removeClass('d-none');
//        $("#email_deactivate_div").addClass('d-none');
//    }
//    if ($(this).val() == 'email_deactivate') {
//        $("#email_deactivate_div").removeClass('d-none');
//        $("#email_activate_div").addClass('d-none');
//    }
});

$(".preferr-email-1").change(function () {
    var field_form = $(this).closest('form').attr('id');
    var email = /^[a-zA-Z0-9._%+-]+$/i;
    //if (!email.test($(this).val()) || $(this).val().indexOf('.') == -1) {
    if ((!email.test($(this).val())) && ($(this).val().indexOf('.') == -1 || $(this).val().indexOf('-') == -1)) {// line modified on 27thfeb2020
        $(this).closest("#" + field_form + ' .domain-suggestion-div').find(".err_msg").html("You have Entered Invallid Uid, (Ex:abcd.wxyz)");
        $(this).closest("#" + field_form + ' .col-md-6').find("font span").html("");
        $(this).closest("#" + field_form + " .domain-suggestion-div").find('select').prop("disabled", true);
    } else {
        $(this).closest("#" + field_form + " .domain-suggestion-div").find('select').prop("disabled", false);
        $(this).closest("#" + field_form + ' .domain-suggestion-div').find(".err_msg").html("");
        var formType = $('input[name="form_name"]:checked').val();
        var emp_detail = $("#" + field_form + ' input[name="single_emp_type"]:checked').val();
        var id_type = $("#" + field_form + ' input[name="single_id_type"]:checked').val();
        $("#" + field_form + ' .emailcheck').find('.preferr-email-2').val("");
        $("#" + field_form + ' .emailcheck').find('.preferr-email-2').prop('disabled', true);
        var domain_name = $("#" + field_form + ' .emailcheck').find('.programmodule').val();

        if (domain_name !== "") {
            var field_name = $("#" + field_form + ' .domain-suggestion-div').find('.domain_suggestion').attr("name");

            var form_email_field = "";
            form_email_field = field_form + ',' + emp_detail + ',' + id_type;
            $("#" + field_form + ' .emailcheck').find('.preferr-email-2').val("");
            $("#" + field_form + ' .emailcheck').find('.preferr-email-2').prop('disabled', false);
            var new_email = $(this).val() + '@' + domain_name;
            $("#" + field_form + ' .domain-suggestion-div').find('.domain_suggestion').val(new_email);
            $("#" + field_form + ' .domain-suggestion-div').find('.domain-suggestion-lbl').html(new_email);
            $("#" + field_form + ' .domain-name').html('@' + domain_name);
            checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
            //user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
        }
//        fetchDomaindatafunction(formType, emp_detail, id_type);
        var txt_email1 = $(this).val();
        var txt_email2 = $(this).closest("#" + field_form + ' .emailcheck').find('.preferr-email-2').val();
        if (txt_email1 === txt_email2) {
            $(this).closest("#" + field_form + ' .domain-suggestion-div').find("#single_email1_err").html("Prefer Emails can't be same, Please check.");
        }
    }
});



$(".applicant-preferr-email-1").change(function () {

    console.log("inside app pref email")
    var email = /^[a-zA-Z0-9._%+-]+$/i;
    //if (!email.test($(this).val()) || $(this).val().indexOf('.') == -1) {
    if ((!email.test($(this).val())) && ($(this).val().indexOf('.') == -1 || $(this).val().indexOf('-') == -1)) {// line modified on 27thfeb2020
        $(this).closest('.applicant-domain-suggestion-div').find(".err_msg").html("You have Entered Invallid Uid, (Ex:abcd.wxyz)");
        $(this).closest('.col-md-6').find("font span").html("");
        $(this).closest(".applicant-domain-suggestion-div").find('select').prop("disabled", true);

    } else {

        $(this).closest(".applicant-domain-suggestion-div").find('select').prop("disabled", false);
        $(this).closest('.applicant-domain-suggestion-div').find(".err_msg").html("");
        var formType = $('input[name="form_name"]:checked').val();
        var emp_detail = $('input[name="applicant_single_emp_type"]:checked').val();
        var id_type = $('input[name="applicant_single_id_type"]:checked').val();
        $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val("");
        $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').prop('disabled', true);
        var domain_name = $(this).closest('.applicantemailcheck').find('.appprogrammodule').val();
        if (domain_name !== "") {
            console.log("inside app pref email  domain name not blank")
            var field_name = $(this).closest('.applicant-domain-suggestion-div').find('.applicant_domain_suggestion').attr("name");
            var field_form = $(this).closest('form').attr('id');
            var form_email_field = "";
            form_email_field = field_form + ',' + emp_detail + ',' + id_type;
            $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val("");
            $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').prop('disabled', false);
            var new_email = $(this).val() + '@' + domain_name;
            $(this).closest('.applicant-domain-suggestion-div').find('.applicant_domain_suggestion').val(new_email);
            $(this).closest('.applicant-domain-suggestion-div').find('.applicant-domain-suggestion-lbl').html(new_email);
            $('.applicant-domain-name').html('@' + domain_name);

            checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
        }

        var field_form = $(this).closest('form').attr('id');
        var user_type = "";
        var formType = $('input[name="form_name"]:checked').val();
        var emp_detail = $("#" + field_form + ' input[name="applicant_single_emp_type"]:checked').val();

        var id_type = $("#" + field_form + ' input[name="applicant_single_id_type"]:checked').val();
        user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
        $(this).closest('.emailcheck').find('.applicant-preferr-email-2').val("");
        $(this).closest('.emailcheck').find('.applicant-preferr-email-2').prop('disabled', true);
        var employment = $('#applicant_employment').val();
        var min = $('#applicant_min').val();
        var dept = $('#applicant_dept').val();
        var state_code = $('#applicant_stateCode').val();
        var state_dept = $('#applicant_Smi').val();
        var org = $('#applicant_Org').val();
//        fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org);
        // alert("")
        // fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type);
        var txt_email1 = $(this).val();
        var txt_email2 = $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val();
        if (txt_email1 === txt_email2) {
            $(this).closest('.applicant-domain-suggestion-div').find("#applicant_single_email1_err").html("Prefer Emails can't be same, Please check.");
        }
    }
});
$('input[name="form_name"]').click(function () {
    var formType = $('input[name="form_name"]:checked').val();
    var field_form = $(this).closest('form').attr('id');
    var emp_detail = "";
    var id_type = "";
    var user_type = "";
//    if ($(this).val() === 'single_form' || $(this).val() === 'bulk_form') {
//        emp_detail = $("#" + field_form + ' input[name="single_emp_type"]:checked').val();
//        id_type = $("#" + field_form + ' input[name="single_id_type"]:checked').val();
//        user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
//    }

    //modified on 04Nov2022
    user_type = $(this).val() === 'single_form' ? $('input[name="applicant_req_for"]:checked').val() : $(this).val() === 'nkn_single_form' ? $('input[name="nkn_req_for"]:checked').val() : "";
    id_type = $(this).val() === 'single_form' ? $('input[name="single_id_type"]:checked').val() : $(this).val() === 'nkn_single_form' ? $('input[name="nkn_id_type"]:checked').val() : $(this).val() === 'gem_form' ? "id_desig" : "";
    emp_detail = $(this).val() === 'single_form' ? $('input[name="single_emp_type"]:checked').val() : "";

    $(this).val() === 'single_form' ? $('input[name="applicant_req_for"]:checked').val() : $(this).val() === 'nkn_single_form' ? $('input[name="nkn_req_for"]:checked').val() : "";
    $(this).closest('.emailcheck').find('.preferr-email-2').val("");
    $(this).closest('.emailcheck').find(' .preferr-email-2').prop('disabled', true);

    if (formType === 'single_form' || formType === 'gem_form' || formType === 'nkn_single_form')
        fetchDomaindatafunction(formType, emp_detail, id_type, user_type);
    else if (formType === 'dor_ext')
        fetchDor($('#dor_ext1 #dor_email').val());
});

$('input[name="single_emp_type"]').click(function () {
    var field_form = $(this).closest('form').attr('id');
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $("#" + field_form + ' input[name="single_emp_type"]:checked').val();
    var id_type = $("#" + field_form + ' input[name="single_id_type"]:checked').val();
    user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
    $(this).closest("#" + field_form + ' .emailcheck').find('.preferr-email-2').val("");
    $(this).closest("#" + field_form + ' .emailcheck').find('.preferr-email-2').prop('disabled', true);
    fetchDomaindatafunction(formType, emp_detail, id_type, user_type);
});

$('input[name="single_id_type"]').click(function () {
    var field_form = $(this).closest('form').attr('id');
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $("#" + field_form + ' input[name="single_emp_type"]:checked').val();
    var id_type = $("#" + field_form + ' input[name="single_id_type"]:checked').val();
    user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
    $(this).closest("#" + field_form + ' .emailcheck').find('.preferr-email-2').val("");
    $(this).closest("#" + field_form + ' .emailcheck').find('.preferr-email-2').prop('disabled', true);
    fetchDomaindatafunction(formType, emp_detail, id_type, user_type);
});

$('input[name="applicant_single_emp_type"]').click(function () {

    var field_form = $(this).closest('form').attr('id');
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $("#" + field_form + ' input[name="applicant_single_emp_type"]:checked').val();

    var id_type = $("#" + field_form + ' input[name="applicant_single_id_type"]:checked').val();
    user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
    $(this).closest('.emailcheck').find('.applicant-preferr-email-2').val("");
    $(this).closest('.emailcheck').find('.applicant-preferr-email-2').prop('disabled', true);
    var employment = $('#applicant_employment').val();
    var min = $('#applicant_min').val();
    var dept = $('#applicant_dept').val();
    var state_code = $('#applicant_stateCode').val();
    var state_dept = $('#applicant_Smi').val();
    var org = $('#applicant_Org').val();
    fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org);

})
$('input[name="applicant_single_id_type"]').click(function () {

    var field_form = $(this).closest('form').attr('id');
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $("#" + field_form + ' input[name="applicant_single_emp_type"]:checked').val();
    var id_type = $("#" + field_form + ' input[name="applicant_single_id_type"]:checked').val();
    user_type = $("#" + field_form + ' input[name="applicant_req_for"]:checked').val();
    $(this).closest('.applicantemailcheck').find('.preferr-email-2').val("");
    $(this).closest('.applicantemailcheck').find('.preferr-email-2').prop('disabled', true);
    var employment = $('#applicant_employment').val();
    var min = $('#applicant_min').val();
    var dept = $('#applicant_dept').val();
    var state_code = $('#applicant_stateCode').val();
    var state_dept = $('#applicant_Smi').val();
    var org = $('#applicant_Org').val();
    fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org);


})
$(".applicant-preferr-email-2").change(function () {
    var email = /^[a-zA-Z0-9._%+-]+$/i;
    //if (!email.test($(this).val()) || $(this).val().indexOf('.') == -1) {
    if ((!email.test($(this).val())) && ($(this).val().indexOf('.') == -1 || $(this).val().indexOf('-') == -1)) {// line modified on 27thfeb2020
        $(this).closest('.col-md-6').find("font span").html("");
        $(this).closest('.applicant-domain-suggestion-div').find(".err_msg").html("You have Entered Invallid Uid, (Ex:abcd.wxyz)");
    } else {
        $(this).closest('.applicant-domain-suggestion-div').find(".err_msg").html("");
        var txt_email2 = $(this).val();
        var txt_email1 = $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-1').val();
        if (txt_email1 === txt_email2) {
            $(this).closest('.applicant-domain-suggestion-div').find(".err_msg").html("Prefer Emails can't be same, Please check.");
            $(this).closest('.col-md-6').find("font span").html("");
        } else {
            var formType = $(this).closest('form').find('input[name="form_name"]:checked').val();
            var emp_detail = $(this).closest('form').find('input[name="applicant_single_emp_type"]:checked').val();
            var id_type = $(this).closest('form').find('input[name="applicant_single_id_type"]:checked').val();

            var field_name = $(this).closest('.applicant-domain-suggestion-div').find('.applicant_domain_suggestion').attr("name");
            var field_form = $(this).closest('form').attr('id');
            var preferr_email_2 = $(this).closest('.applicant-domain-suggestion-div').find('.applicant-preferr-email-2').val();
            var domain_name = $(this).closest('.applicant-domain-suggestion-div').find('.applicant-domain-name').html();
            var new_email = preferr_email_2 + domain_name;
            $(this).closest('.applicant-domain-suggestion-div').find('.applicant_domain_suggestion').val(new_email);
            var form_email_field = formType + ',' + emp_detail + ',' + id_type;
            checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
        }
    }
});



//$(".preferr-email-2").change(function () {
//    var email = /^[a-zA-Z0-9._%+-]+$/i;
//    var field_form = $(this).closest('form').attr('id');
//    //if (!email.test($(this).val()) || $(this).val().indexOf('.') == -1) {
//    if ((!email.test($(this).val())) && ($(this).val().indexOf('.') == -1 || $(this).val().indexOf('-') == -1)) {// line modified on 27thfeb2020
//        $(this).closest('.col-md-6').find("font span").html("");
//        $(this).closest('.domain-suggestion-div').find(".err_msg").html("You have Entered Invallid Uid, (Ex:abcd.wxyz)");
//    } else {
//        $(this).closest('.domain-suggestion-div').find(".err_msg").html("");
//        var txt_email2 = $(this).val();
//        var txt_email1 = $(this).closest('.emailcheck').find('.preferr-email-1').val();
//        if (txt_email1 === txt_email2) {
//            $(this).closest('.domain-suggestion-div').find(".err_msg").html("Prefer Emails can't be same, Please check.");
//            $(this).closest('.col-md-6').find("font span").html("");
//        } else {
//            var formType = $(this).closest('form').find('input[name="form_name"]:checked').val();
//            var emp_detail = $(this).closest('form').find('input[name="single_emp_type"]:checked').val();
//            var id_type = $(this).closest('form').find('input[name="single_id_type"]:checked').val();
//
//            var field_name = $(this).closest('.domain-suggestion-div').find("#" + field_form + ' .domain_suggestion').attr("name");
//            //var field_form = $(this).closest('form').attr('id');
//            var preferr_email_2 = $(this).closest('.domain-suggestion-div').find('.preferr-email-2').val();
//            var domain_name = $(this).closest('.domain-suggestion-div').find('.domain-name').html();
//            var new_email = preferr_email_2 + domain_name;
//            $(this).closest('.domain-suggestion-div').find('.domain_suggestion').val(new_email);
//            var form_email_field = field_form + ',' + emp_detail + ',' + id_type;
//            checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
//        }
//    }
//});




function fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org) {

    //$(".programmodule").show();
    if (employment === "Central")
    {

        dept = employment + "~" + min + "~" + dept;
    }
    if (employment === "State")
    {

        dept = employment + "~" + state_code + "~" + state_dept;
    }
    if (employment === "Psu" || employment === "Const" || employment === "Nkn" || employment === "Project" || employment === "UT" || employment === "Others")
    {

        dept = employment + "~" + org;
    }

    $.ajax({
        type: 'post',
        url: 'fetchDomainDataforApplicant',
        data: {form_type: formType, emp_type: emp_detail, id_type: id_type, user_type: user_type, department: dept},
        success: function (data) {
            var str = "";
            $(".domain_list_data").html('');
            $(".domain_list_data").append("<option value=''>- Select Domain -</option>");
            var j = 1;
            if (data.allowedDomains.length < 2) {
                str += "<option value='" + data.allowedDomains[0] + "' selected readonly>" + data.allowedDomains[0] + "</option>";
            } else {
                $.each(data.allowedDomains, function (i, val) {
                    if (val !== "") {
                        str += "<option value='" + val + "'>" + val + "</option>";
                    }
                    j++;
                });
            }

            $(".domain_list_data").append(str);
        }, complete: function (data) {
            // $(".loader").hide();
        }
    });
}

function fetchDor(email) {
    $.ajax({
        type: "POST",
        url: "fetchDor",
        data: {email_id: email},
        datatype: JSON,
        success: function (data)
        {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);

            if (jsonvalue.error.alert_error != null && jsonvalue.error.alert_error != "") {
                console.log("cnhefjkh")
                $('#dor_ext1 #text_service_employees').html(jsonvalue.error.alert_error);
                $('#dor_ext1 #alert_service_employees').removeClass("display-hide");
                $("#submit_single").prop("disabled", true);
            } else {
                $('#dor_ext1 #text_service_employees').html("");
                $('#dor_ext1 #alert_service_employees').addClass("display-hide");
                $("#submit_single").prop("disabled", false);
            }

            if (jsonvalue.error !== null && jsonvalue.error !== "") {
                $('#dor_ext1 #dor_ext_err').html(jsonvalue.error.email_id_error);
                $('#dor_ext1 #dor_email').focus();
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            }

            if (jsonvalue.dateOfRetirement != null)
            {
                console.log(jsonvalue.dateOfRetirement)
                $("#check-mxdomain-available input").attr('disabled', false);

                $('#dor_ext1 #p_email_single_dor').val(jsonvalue.dateOfRetirement);

                //                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                //                    $('#imgtxt').val("");

            } else {
                console.log(jsonvalue.dateOfRetirement)
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            }
            if (jsonvalue.dateOfBirth != null)
            {
                $("#check-mxdomain-available input").attr('disabled', false);

                $('#dor_ext1 #email_single_dob').val(jsonvalue.dateOfBirth);

                //                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                //                    $('#imgtxt').val("");

            } else {

                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            }




        }, error: function (request, error)
        {
            console.log('error');
        }

    });
}
function fetchDomaindatafunction(formType, emp_detail, id_type, user_type) {
    console.log("ckjendc");
    $.ajax({
        type: 'post',
        url: 'fetchDomainData',
        data: {form_type: formType, emp_type: emp_detail, id_type: id_type, user_type: user_type},
        success: function (data) {
            sessionStorage.setItem("user_email", data.loggedInEmail);
            sessionStorage.setItem("allowedDomains", data.allowedDomains);
            var str = "";
            $(".domain_list_data").html('');
            $(".domain_list_data").append("<option value=''>- Select Domain -</option>");
            var j = 1;
            if (data.allowedDomains.length < 2) {
                str += "<option value='" + data.allowedDomains[0] + "' selected readonly>" + data.allowedDomains[0] + "</option>";
            } else {
                $.each(data.allowedDomains, function (i, val) {
                    if (val !== "") {
                        str += "<option value='" + val + "'>" + val + "</option>";
                    }
                    j++;
                });
            }
            if (data.formTypeDor !== null && data.formTypeDor !== "" && data.formTypeDor !== undefined) {
                $('#dor_ext1 #dor_email').val(data.loggedInEmail);
                var email_id = $("#dor_email").val();
                if (email_id !== "")
                    fetchDor(email_id);

            }

            $(".domain_list_data").append(str);
        }, complete: function (data) {
            // $(".loader").hide();
        }
    });
}

//$("#retired_emp_type2").click(function(){
//    var user_email = sessionStorage.getItem("user_email");
//    fetchDomaindatafunctionForRetiredOfficial(user_email);
//})

function fetchDomaindatafunctionForRetiredOfficial(user_email, formType, emp_detail, id_type, user_type) {
    var email_id = ""
    $.ajax({
        type: "POST",
        url: "fetchDoe",
        data: {email_id: user_email}, // by sahil on 31 march 2021
        datatype: JSON,
        success: function (data)
        {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);

            if (jsonvalue.error !== null && jsonvalue.error !== "") {

                $('#dor_ext1 #dor_ext_err').html(jsonvalue.error.email_id_error);
                $('#dor_ext1 #dor_email').focus();
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            }

            if (jsonvalue.dateOfRetirement != null)
            {
                console.log(jsonvalue.dateOfRetirement)
                $("#check-mxdomain-available input").attr('disabled', false);

                $('#dor_ext1 #p_email_single_dor').val(jsonvalue.dateOfRetirement);

                //                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                //                    $('#imgtxt').val("");

            } else {
                console.log(jsonvalue.dateOfRetirement)
                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            }
            if (jsonvalue.dateOfBirth != null)
            {
                $("#check-mxdomain-available input").attr('disabled', false);

                $('#dor_ext1 #email_single_dob').val(jsonvalue.dateOfBirth);

                //                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                //                    $('#imgtxt').val("");

            } else {

                $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
                $('#imgtxt').val("");
            }
        }, error: function (request, error)
        {
            console.log('error');
        }

    });

}

$(".programmodule").change(function () {

    var field_name = $(this).closest('.domain-suggestion-div').find('.domain_suggestion').attr("name");

    var field_form = $(this).closest('form').attr('id');

    var form_email_field = "";
    //var formType = $('input[name="form_name"]:checked').val();
    if (field_form === "single_form1" || field_form === "single_form2" || field_form === "singleuser_preview") {
        var emp_detail = $('input[name="single_emp_type"]:checked').val();
        var id_type = $('input[name="single_id_type"]:checked').val();
        form_email_field = field_form + ',' + emp_detail + ',' + id_type;
    } else {
        form_email_field = field_form;
    }

    var preferr_email_1 = $(this).closest('.domain-suggestion-div').find('.preferr-email-1').val();
    if (preferr_email_1) {
        $(this).closest('.emailcheck').find('.preferr-email-2').val("");
        $(this).closest('.emailcheck').find('.preferr-email-2').prop('disabled', false);
        var new_email = preferr_email_1 + '@' + $(this).val();
        $(this).closest('.domain-suggestion-div').find('.domain_suggestion').val(new_email);
        $(this).closest('.domain-suggestion-div').find('.domain-suggestion-lbl').html(preferr_email_1 + '@' + $(this).val());
        $('.domain-name').html('@' + $(this).val());
        checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
    }
});


$(".appprogrammodule").change(function () {
    var field_name = $(this).closest('.applicant-domain-suggestion-div').find('.applicant_domain_suggestion').attr("name");

    var field_form = $(this).closest('form').attr('id');
    var form_email_field = "";
    if (field_form === "single_form1" || field_form === "single_form2" || field_form === "singleuser_preview") {
        var emp_detail = $('input[name="single_emp_type"]:checked').val();
        var id_type = $('input[name="single_id_type"]:checked').val();
        form_email_field = field_form + ',' + emp_detail + ',' + id_type;
    } else {
        form_email_field = field_form;
    }

    var preferr_email_1 = $(this).closest('.applicant-domain-suggestion-div').find('.applicant-preferr-email-1').val();

    if (preferr_email_1) {
        $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val("");
        $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').prop('disabled', false);
        var new_email = preferr_email_1 + '@' + $(this).val();
        $(this).closest('.applicant-domain-suggestion-div').find('.applicant_domain_suggestion').val(new_email);
        $(this).closest('.applicant-domain-suggestion-div').find('.domain-suggestion-lbl').html(preferr_email_1 + '@' + $(this).val());
        $('.applicant-domain-name').html('@' + $(this).val());
        checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
    }
});

$(".preferr-email-2").change(function () {
    var email = /^[a-zA-Z0-9._%+-]+$/i;
    if ((!email.test($(this).val())) && ($(this).val().indexOf('.') == -1 || $(this).val().indexOf('-') == -1)) {
        $(this).closest('.col-md-6').find("font span").html("");
        $(this).closest('.domain-suggestion-div').find(".err_msg").html("You have Entered Invallid Uid, (Ex:abcd.wxyz)");
    } else {
        $(this).closest('.domain-suggestion-div').find(".err_msg").html("");
        var txt_email2 = $(this).val();
        var txt_email1 = $(this).closest('.emailcheck').find('.preferr-email-1').val();
        if (txt_email1 === txt_email2) {
            $(this).closest('.domain-suggestion-div').find(".err_msg").html("Prefer Emails can't be same, Please check.");
            $(this).closest('.col-md-6').find("font span").html("");
        } else {
            var formType = $(this).closest('form').find('input[name="form_name"]:checked').val();
            var emp_detail = $(this).closest('form').find('input[name="single_emp_type"]:checked').val();
            var id_type = $(this).closest('form').find('input[name="single_id_type"]:checked').val();

            var field_name = $(this).closest('.domain-suggestion-div').find('.domain_suggestion').attr("name");
            var field_form = $(this).closest('form').attr('id');
            var preferr_email_2 = $(this).closest('.domain-suggestion-div').find('.preferr-email-2').val();
            var domain_name = $(this).closest('.domain-suggestion-div').find('.domain-name').html();
            var new_email = preferr_email_2 + domain_name;
            $(this).closest('.domain-suggestion-div').find('.domain_suggestion').val(new_email);
            var form_email_field = field_form + ',' + emp_detail + ',' + id_type;
            checkAvailableEmail(new_email, "1", form_email_field, field_name, field_form);
        }
    }
});

function checkAvailableEmail(val_data, email_form, form_email_field, field_name, field_form) {
    var employment = "", min = "", dept = "", stateCode = "", smi = "", org = "", applicant_mobile = "";
    var user_type = $('input[name="req_user_type"]:checked').val();

    if (user_type === "self")
    {
        employment = $("#" + field_form + " #user_employment").val();
        min = $("#" + field_form + " #min").val();
        dept = $("#" + field_form + " #dept").val();
        stateCode = $("#" + field_form + " #stateCode").val();
        smi = $("#" + field_form + " #Smi").val();
        org = $("#" + field_form + " #Org").val();
    }

    if (user_type === "other_user")
    {
        employment = $("#" + field_form + " #applicant_employment").val();
        min = $("#" + field_form + " #applicant_min").val();
        dept = $("#" + field_form + " #applicant_dept").val();
        stateCode = $("#" + field_form + " #applicant_stateCode").val();
        smi = $("#" + field_form + " #applicant_Smi").val();
        org = $("#" + field_form + " #applicant_Org").val();
        applicant_mobile = $("#" + field_form + " #applicant_mobile").val();

    }
//anmol
    $.ajax({
        type: "POST",
        url: "checkAvailableEmail",
        data: {data: val_data, whichemail: email_form, whichform: form_email_field, employment: employment, ministry: min, department: dept, state: stateCode, stateDepartment: smi, organization: org, user_type: user_type, applicant_mobile: applicant_mobile},
        datatype: JSON,
        success: function (data) {
            var myJSON = JSON.stringify(data);
            var jsonvalue = JSON.parse(myJSON);
            //   alert("field_name"+field_name)
            if (field_name === 'applicant_single_email1') {
                field_name = 'single_email1';
            }
            if (field_name === 'applicant_single_email2') {
                field_name = 'single_email2';
            }
            if ((field_form === 'gem_form1' || field_form === 'gem_form2') && field_name === 'single_email1') {
                $('#' + field_form + " #gem_email1_err").text(jsonvalue.error.errorMsg1);
                if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                    $('#' + field_form + " #gem_email1_err").css("color", "green");
                } else {
                    $('#' + field_form + " #gem_email1_err").css("color", "red");
                }
                if (jsonvalue.error.errorMsgDup_create != "" && jsonvalue.error.errorMsgDup_create != undefined)
                {
                    $('#' + field_form + " #gem_email1_err").text(jsonvalue.error.errorMsgDup_create);
                    $('#' + field_form + " #gem_email1_err").css("color", "green");
                    $('#' + field_form + " #gem_email1_err").css("font-weight", "bold");
                } else if (jsonvalue.error.errorMsgDup != "" && jsonvalue.error.errorMsgDup != undefined)
                {
                    $('#' + field_form + " #gem_email1_err").text(jsonvalue.error.errorMsgDup);
                    $('#' + field_form + " #gem_email1_err").css("color", "red");
                    $('#' + field_form + " #gem_email1_err").css("font-weight", "bold");
                }
            } else if ((field_form === 'gem_form1' || field_form === 'gem_form2') && field_name === 'single_email2') {
                $('#' + field_form + " #gem_email2_err").text(jsonvalue.error.errorMsg1);
                if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                    $('#' + field_form + " #gem_email2_err").css("color", "green");
                } else {
                    $('#' + field_form + " #gem_email2_err").css("color", "red");
                }
                if (jsonvalue.error.errorMsgDup_create != "" && jsonvalue.error.errorMsgDup_create != undefined)
                {
                    $('#' + field_form + " #gem_email2_err").text(jsonvalue.error.errorMsgDup_create);
                    $('#' + field_form + " #gem_email2_err").css("color", "green");
                    $('#' + field_form + " #gem_email2_err").css("font-weight", "bold");
                } else if (jsonvalue.error.errorMsgDup != "" && jsonvalue.error.errorMsgDup != undefined)
                {
                    $('#' + field_form + " #gem_email2_err").text(jsonvalue.error.errorMsgDup);
                    $('#' + field_form + " #gem_email2_err").css("color", "red");
                    $('#' + field_form + " #gem_email2_err").css("font-weight", "bold");
                }
            } else if (field_form === 'email_act1' || field_form === 'email_act2' || field_form === "email_act_preview" && field_name === 'single_email1') {
                if (jsonvalue.error.errorMsg1.indexOf("Email address is available to activate") > -1) {
                    $('#' + field_form + " #" + field_name + "_err").text(jsonvalue.error.errorMsg1);
                    $('#' + field_form + " #" + field_name + "_err").css("color", "green");
                } else {
                    $('#' + field_form + " #" + field_name + "_err").text(jsonvalue.error.errorMsg1);
                    $('#' + field_form + " #" + field_name + "_err").css("color", "red");
                }

            } else {
                if (jsonvalue.error.errorMsgDup_create != "" && jsonvalue.error.errorMsgDup_create != undefined)
                {
                    $('#' + field_form + " #" + field_name + "_err").text(jsonvalue.error.errorMsgDup_create);
                    $('#' + field_form + " #" + field_name + "_err").css("color", "green");
                    $('#' + field_form + " #" + field_name + "_err").css("font-weight", "bold");
                } else if (jsonvalue.error.errorMsgDup != "" && jsonvalue.error.errorMsgDup != undefined)
                {
                    $('#' + field_form + " #" + field_name + "_err").text(jsonvalue.error.errorMsgDup);
                    $('#' + field_form + " #" + field_name + "_err").css("color", "red");
                    $('#' + field_form + " #" + field_name + "_err").css("font-weight", "bold");
                } else {

                    $('#' + field_form + " #" + field_name + "_err").text(jsonvalue.error.errorMsg1);
                    if (jsonvalue.error.errorMsg1.indexOf("is available for creation") > -1) {
                        $('#' + field_form + " #" + field_name + "_err").css("color", "green");
                    } else {

                        $('#' + field_form + " #" + field_name + "_err").css("color", "red");
                    }
                }
            }
        },
        error: function () {
            console.log('error');
            return false;
        }
    });
}

$(document).on('change', '#applicant_employment', function () {
    //$('#user_employment').change(function () {
    console.log("user employment change select")
    $('#applicant_other_text_div').addClass("display-hide");
    $('#applicant_other_dept').val("");
    var field_form = $(this).closest('form').attr('id');
    console.log("form" + field_form)
    var orgname = $("#" + field_form + " #applicant_employment").val();
    console.log(orgname + "orgname")
    if (orgname === null || orgname === "") {
        $('#applicantemployment_error').html("Select organization category");
    } else {
        var regn = "^[a-zA-Z]{1,8}$";
        if (orgname.match(regn)) {

            $('#applicantemployment_error').html("");
            if (orgname === "Central" || orgname === "UT") {
                $("#" + field_form + " #applicant_central_div").show();
                $("#" + field_form + " #applicant_state_div").hide();
                $("#" + field_form + " #applicant_other_div").hide();
                $("#" + field_form + " #applicant_state_div,#other_div").find('input,select').val('');
                $.get('centralMinistry', {
                    orgType: orgname
                }, function (response) {
                    var select = $("#" + field_form + " #applicant_min");
                    select.find('option').remove();
                    $('<option>').val("").text("-SELECT-").appendTo(select);
                    $.each(response, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                });
            } else if (orgname === "State") {
                $("#" + field_form + " #applicant_central_div").hide();
                $("#" + field_form + " #applicant_state_div").show();
                $("#" + field_form + " #applicant_other_div").hide();
                $("#" + field_form + " #applicant_central_div,#applicant_other_div").find('input,select').val('');
                $("#" + field_form + " #applicant_stateCode").val("Delhi");
                $.get('centralMinistry', {
                    orgType: orgname
                }, function (response) {
                    var select = $("#" + field_form + " #applicant_stateCode");
                    select.find('option').remove();
                    $('<option>').val("").text("-SELECT-").appendTo(select);
                    $.each(response, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    if (response.toString().toLowerCase().indexOf("other") == -1)
                    {
                        if (response.toString().toLowerCase().indexOf("other") > -1) {
                        } else {
                            $('<option>').val("other").text("other").appendTo(select);
                        }
                    }
                });
            } else if (orgname === "Others" || orgname === "Psu" || orgname === "Const" || orgname === "Nkn" || orgname === "Project") {

                $("#" + field_form + " #applicant_central_div").hide();
                $("#" + field_form + " #applicant_state_div").hide();
                $("#" + field_form + " #applicant_other_div").show();
                $("#" + field_form + " #applicant_central_div,#applicant_state_div").find('input,select').val('');
                $.get('centralMinistry', {
                    orgType: orgname
                }, function (response) {
                    var select = $("#" + field_form + " #applicant_Org");
                    select.find('option').remove();
                    $('<option>').val("").text("-SELECT-").appendTo(select);
                    $.each(response, function (index, value) {
                        $('<option>').val(value).text(value).appendTo(select);
                    });
                    if (response.toString().toLowerCase().indexOf("other") == -1)
                    {
                        if (response.toString().toLowerCase().indexOf("other") > -1) {
                        } else {
                            $('<option>').val("other").text("other").appendTo(select);
                        }
                    }
                });
            } else {
                $("#" + field_form + " #applicant_central_div").hide();
                $("#" + field_form + " #applicant_state_div").hide();
                $("#" + field_form + " #applicant_other_div").hide();
            }
        } else {
            $("#" + field_form + " #applicant_employment_error").html("Select organization category in correct format");
        }
    }
});
$(document).on('change', '#applicant_min', function () {
    var field_form = $(this).closest('form').attr('id')
    console.log("form" + field_form)
    //$('#min').change(function () {

    var orgname = $("#" + field_form + " #applicant_min").val();
    $.get('centralDepartment', {
        depType: orgname
    }, function (response) {

        var select = $("#" + field_form + " #applicant_dept");
        select.find('option').remove();
        $('<option>').val("").text("-SELECT-").appendTo(select);
        $.each(response, function (index, value) {
            $('<option>').val(value).text(value).appendTo(select);
        });
        if (response.toString().toLowerCase().indexOf("other") == -1)
        {
            if (response.toString().toLowerCase().indexOf("other") > -1) {
            } else {
                $('<option>').val("other").text("other").appendTo(select);
            }
        }
    });
});
$(document).on('change', '#applicant_stateCode', function () {
    //$('#stateCode').change(function () {

    var field_form = $(this).closest('form').attr('id')
    console.log("form" + field_form)
    var orgname = $("#" + field_form + " #applicant_stateCode").val();
    $.get('centralDepartment', {
        depType: orgname
    }, function (response) {
        var select = $("#" + field_form + " #applicant_Smi");
        select.find('option').remove();
        $('<option>').val("").text("-SELECT-").appendTo(select);
        $.each(response, function (index, value) {
            $('<option>').val(value).text(value).appendTo(select);
        });
        if (response.toString().toLowerCase().indexOf("other") == -1)
        {
            if (response.toString().toLowerCase().indexOf("other") > -1) {
            } else {
                $('<option>').val("other").text("other").appendTo(select);
            }
        }
    });
});
$(document).on('change', '#applicant_dept', function () {

    var field_form = $(this).closest('form').attr('id')
    console.log("field_form" + field_form)
    //$('#dept').change(function () {
    var dept = $("#" + field_form + " #applicant_dept").val();
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $('input[name="applicant_single_emp_type"]:checked').val();
    var id_type = $('input[name="applicant_single_id_type"]:checked').val();
    user_type = $('input[name="applicant_req_for"]:checked').val();
    $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val("");
    $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').prop('disabled', true);
    var employment = $('#applicant_employment').val();
    var min = $('#applicant_min').val();
    var dept = $('#applicant_dept').val();
    var state_code = $('#applicant_stateCode').val();
    var state_dept = $('#applicant_Smi').val();
    var org = $('#applicant_Org').val();
    fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org);
    if (dept.toString().toLowerCase() == 'other')
    {

        $("#" + field_form + " #applicant_other_text_div").removeClass("display-hide");
    } else {
        $("#" + field_form + " #applicant_other_text_div").addClass("display-hide");

    }
});
$(document).on('change', '#applicant_Smi', function () {

    //$('#Smi').change(function () {
    var field_form = $(this).closest('form').attr('id')
    var Smi = $("#" + field_form + " #applicant_Smi").val();
    var field_form = $(this).closest('form').attr('id')
    console.log("field_form" + field_form)
    //$('#dept').change(function () {
    var dept = $("#" + field_form + " #applicant_dept").val();
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $('input[name="applicant_single_emp_type"]:checked').val();
    var id_type = $('input[name="applicant_single_id_type"]:checked').val();
    user_type = $('input[name="applicant_req_for"]:checked').val();
    $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val("");
    $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').prop('disabled', true);
    var employment = $('#applicant_employment').val();
    var min = $('#applicant_min').val();
    var dept = $('#applicant_dept').val();
    var state_code = $('#applicant_stateCode').val();
    var state_dept = $('#applicant_Smi').val();
    var org = $('#applicant_Org').val();
    fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org);
    if (Smi.toString().toLowerCase() == 'other')
    {
        $("#" + field_form + " #applicant_other_text_div").removeClass("display-hide");
    } else
    {
        $("#" + field_form + " #applicant_other_text_div").addClass("display-hide");
    }
});
$(document).on('change', '#applicant_Org', function () {
    var field_form = $(this).closest('form').attr('id')
    //$('#Org').change(function () {
    var Org = $("#applicant_Org").val();
    var field_form = $(this).closest('form').attr('id')
    console.log("field_form" + field_form)
    //$('#dept').change(function () {
    var dept = $("#" + field_form + " #applicant_dept").val();
    var user_type = "";
    var formType = $('input[name="form_name"]:checked').val();
    var emp_detail = $('input[name="applicant_single_emp_type"]:checked').val();
    var id_type = $('input[name="applicant_single_id_type"]:checked').val();
    user_type = $('input[name="applicant_req_for"]:checked').val();
    $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').val("");
    $(this).closest('.applicantemailcheck').find('.applicant-preferr-email-2').prop('disabled', true);
    var employment = $('#applicant_employment').val();
    var min = $('#applicant_min').val();
    var dept = $('#applicant_dept').val();
    var state_code = $('#applicant_stateCode').val();
    var state_dept = $('#applicant_Smi').val();
    var org = $('#applicant_Org').val();

    fetchDomaindatafunctionforapplicant(formType, emp_detail, id_type, user_type, employment, min, dept, state_code, state_dept, org);
    if (Org.toString().toLowerCase() == 'other')
    {
        $("#" + field_form + " #applicant_other_text_div").removeClass("display-hide");
    } else
    {
        $("#" + field_form + " #applicant_other_text_div").addClass("display-hide");
    }

});


//function popForOther() {
$(document).on('change', '#other_user', function () {
    bootbox.alert("This facility is provided only to NIC employees using which they can create email accounts for other users(Only for users in the department where he is posted)");
});

//$(document).on('change', '#dor_ext1 #dor_email', function () {
//    alert("hellooooo");
//
//    var email_id = $('#dor_email').val();
//
//    if (email_id !== "")
//    {
//        $.ajax({
//            type: "POST",
//            url: "fetchDor",
//            data: {email_id: email_id}, // by sahil on 31 march 2021
//            datatype: JSON,
//            success: function (data)
//            {
//                var myJSON = JSON.stringify(data);
//                var jsonvalue = JSON.parse(myJSON);
//                
//                if(jsonvalue.error !== null && jsonvalue.error !== ""){
//                 
//                   $('#dor_ext1 #dor_ext_err').html(jsonvalue.error.email_id_error);
//                    $('#dor_ext1 #dor_email').focus();
//                      $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                    $('#imgtxt').val("");
//                }
//
//                if (jsonvalue.dateOfRetirement != null)
//                {
//                    $("#check-mxdomain-available input").attr('disabled', false);
//
//                    $('#dor_ext1 #p_email_single_dor').val(jsonvalue.dateOfRetirement);
//
////                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
////                    $('#imgtxt').val("");
//
//                } else {
//
//                    $('#captcha').attr('src', 'Captcha?var=<%=session.getAttribute("rand")%>' + Date.parse(new Date().toString()));
//                    $('#imgtxt').val("");
//                }
//
//
//
//
//            }, error: function (request, error)
//            {
//                console.log('error');
//            }
//
//        });
//    }
//});

/* Custom Start */
//
//    $("#dor_ext1 input[name='single_id_type']").click(function(){
//        var singleIdType= $("input[name='single_id_type']:checked").val();
//        alert(singleIdType)
//        if(singleIdType == 'id_desig'){
//            $("#dor_desc").hide();
//        }else{
//            $("#dor_desc").show();
//        }
//    });


$("#dor_ext1 input[name='single_emp_type']").click(function () {
    var singleEmpType = $("input[name='single_emp_type']:checked").val();
    //alert(singleEmpType)

    if (singleEmpType == 'emp_regular') {
        $('#dor_upload_pdf').addClass("d-none");
        $('#dor_annum').removeClass("d-none");
        $('#last_date').addClass("d-none");
    } else if (singleEmpType == 'consultant') {
        $('#dor_upload_pdf').removeClass("d-none");
        $('#dor_annum').addClass("d-none");
        $('#last_date').addClass("d-none");
    } else if (singleEmpType == 'emp_contract') {
        $('#dor_upload_pdf').removeClass("d-none");
        $('#dor_annum').addClass("d-none");
        $('#last_date').addClass("d-none");
    }
});

//    $("#dor_ext1 input[name='single_emp_type']").click(function(){
//        var singleEmpType= $("input[name='single_emp_type']:checked").val();
//        alert(singleEmpType)
//        if(singleEmpType == 'consultant'){
//            $('#dor_annum').removeClass("d-none");
//        }
//    });
$("#dor_ext1 input[name='single_id_type']").click(function () {
    var singleEmpType = $("input[name='single_id_type']:checked").val();
    //alert(singleEmpType)
    if (singleEmpType == 'id_desig') {
        $('#last_date').removeClass("d-none");
    } else if (singleEmpType == 'id_name') {
        $('#last_date').addClass("d-none");
    }
//        if(singleEmpType == 'emp_regular')
});
$("#email_act_div input[name='single_emp_type']").click(function () {
    if ($(this).val() === 'consultant' || $(this).val() === 'emp_contract') {
        $('#email_act_div #hardware_cert_div').removeClass('d-none');
    } else {
        $('#email_act_div #hardware_cert_div').addClass('d-none');
    }
})



//Date-22/09/2022

//26-aug-2022


$('input[id="bulk_form"]').click(function () {
    if ($(this).is(':checked')) {
        fetchEmailData()
    }
});


function fetchEmailData() {
    var user_type = $('input[name="form_name"]:checked').val();
    if (user_type === "bulk_form")
    {
        $.ajax({
            type: 'post',
            url: 'fetchEmailCampaigns',
            success: function (data) {
                console.log(data);
                $('input:radio[id="bulk_form"]').prop('disabled', false);

                if (data.campaigns.length > 0) {
                    var campaign_tbl = "";
                    $("#campaign_tbl tbody").html("");
                    $.each(data.campaigns, function (key, val) {
                        key++;
                        +"</td><td>" + namingConvention(val.renamed_file)
                        campaign_tbl += "<tr><td>" + key + "</td><td>" + val.id + "</td><td>" + namingConvention(val.uploaded_file) + "</td><td>" + val.submitted_at + "</td><td class='text-center'><span class='btn btn-primary btn-sm' onclick='showCapaign(" + val.id + ")'>Continue</span></td><td class='text-center'><span class='btn btn-danger btn-sm' onclick='discardCampaign(" + val.id + ")'>Discard</span></td></tr>";
                    });
                    $("#campaign_tbl tbody").html(campaign_tbl);
                    $("#campaign_modaltbl").modal('show');

                } else {
                    console.log(data.campaigns.length)
                    $("#campaign_modaltbl").modal('hide');

                }
            }
        })
    } else if (user_type === "nkn_bulk_form") {
    }
}


function showCapaign(i) {
    $.ajax({
        type: 'post',
        url: 'fetchEmailCampaignData',
        data: {EmailbulkDataId: i},
        success: function (data) {
            $("#_req").val(data.emailbulkData);
            $("#campaign_modaltbl").modal('hide');
            $('#email_head1').addClass('display-hide');
            $('#email_head2').addClass('display-hide');
            $('#bulk-1').addClass('display-hide');
            $('#bulk-2').removeClass('display-hide');
            bulkEmailCreate(data.bulkData)
        }
    });
}

function namingConvention(lbl) {
//    alert("namingConvention")
    var req_type = $("#_req").val();
    if (lbl.indexOf('fname') != -1) {

        return 'First Name';
    }
    if (lbl.indexOf('designation') != -1) {

        return 'Designation';
    }
    if (lbl.indexOf('lname') != -1) {

        return 'Last Name';
    }
    if (lbl.indexOf('mail') != -1) {

        return 'Mail ID';
    }
    if (lbl.indexOf('mobile') != -1) {

        return 'Mobile Number';
    }
    if (lbl.indexOf('state') != -1) {

        return 'State';
    }
    if (lbl.indexOf('department') != -1) {

        return 'Department';
    }
//    if (lbl.indexOf('dob') != -1) {
//
//        return 'Date of Birth';
//    }
    if (lbl.indexOf('dor') != -1) {

        return 'Date of Retirement';
    }
    return lbl;
}
function discardCampaign(i) {
    bootbox.confirm({
        title: "Discard Campaign",
        message: "Are you sure? Do you want to discard this Campaign?",
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
                    url: 'EmaildiscardCampaign',
                    data: {EmailbulkDataId: i},
                    success: function (data) {
                        fetchEmailData();
                    }
                })
            }
        }

    });
}

function discardRelatedCampaign(i) {
    $.ajax({
        type: 'post',
        url: 'EmaildiscardCampaign',
        data: {EmailbulkDataId: i},
        success: function (data) {
        }
    })
}

function bulkEmailCreate(jsonvalue) {
    $("#success_bulk_record thead").html("");
    $("#success_bulk_record tbody").html("");
    $("#error_bulk_record tbody").html("");
    $("#error_bulk_record thead").html("");
    var invalidData = "";
    var validData = "";
    $('span.err-count').html(jsonvalue.invalidRecord.length);
    if (jsonvalue.invalidRecord.length > 0) {
        var thead = "";
        thead += `<tr><td class='text-center not-export-col'>S.No</td>`;
        thead += `<td>` + namingConvention("First Name") + `</td>`
        thead += `<td>` + namingConvention("Last Name") + `</td>`
        //thead += `<td>` + namingConvention("Date of Birth") + `</td>`
        thead += `<td>` + namingConvention("Date of Retirement") + `</td>`
        thead += `<td>` + namingConvention("Department") + `</td>`
        thead += `<td>` + namingConvention("Designation") + `</td>`
        thead += `<td>` + namingConvention("Mail") + `</td>`
        thead += `<td>` + namingConvention("Mobile") + `</td>`
        thead += `<td>` + namingConvention("User State") + `</td>`
        if (cname_col) {
            thead += `<td>CNAME</td>`
        }
        thead += `<td>Error Message</td><td class='text-center not-export-col'>Action</td></tr>`;
        $("#error_bulk_record thead").html(thead);
        $.each(jsonvalue.invalidRecord, function (key, val) {
            invalidData += dynamic_append_bulk_invalidtbl(val, key);
        });

        $('#error_bulk_record').DataTable().clear().destroy();
        $("#error_bulk_record tbody").html(invalidData);

        setTimeout(function () {
            $("#error_bulk_record").DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'csv'
                ], exportOptions: {
                    columns: ':visible(:not(.not-export-col))'
                }
            });
        }, 5);
    } else {
        $(".err-head-notify").html("No Record Found.")
    }
    $('span.succ-count').html(jsonvalue.validRecord.length);
    if (jsonvalue.validRecord.length > 0) {
        var validcname_col = false;
        $("button[value=preview]").prop('disabled', false);
        var thead = "";
        thead += `<tr><td class='text-center not-export-col'>S.No</td>`
        thead += `<td>` + namingConvention("First Name") + `</td>`
        thead += `<td>` + namingConvention("Last Name") + `</td>`
        //thead += `<td>` + namingConvention("Date of Birth") + `</td>`
        thead += `<td>` + namingConvention("Date of Retirement") + `</td>`
        thead += `<td>` + namingConvention("Department") + `</td>`
        thead += `<td>` + namingConvention("Designation") + `</td>`
        thead += `<td>` + namingConvention("Mail") + `</td>`
        thead += `<td>` + namingConvention("Mobile") + `</td>`
        thead += `<td>` + namingConvention("User State") + `</td>`

        if (validcname_col) {
            thead += `<td>CNAME</td>`
        }
        thead += `<td class='text-center not-export-col'>Action</td></tr>`;
        $("#success_bulk_record thead").html(thead);
        $.each(jsonvalue.validRecord, function (key, val) {
            validData += dynamic_append_bulk_validtbl(val, key);
        });
        $("#success_bulk_record").DataTable().clear().destroy();
        $("#success_bulk_record tbody").html(validData);
        setTimeout(function () {
            var numCols = $('#example thead th').length;
            $("#success_bulk_record").DataTable({
                dom: 'Bfrtip',
                buttons: [
                    {
                        extend: 'csv',
                        exportOptions: {
                            columns: ':not(td.not-export-col)'
                        }
                    },
                ]
            });
        }, 5);
    } else {
        $("button[value=preview]").prop('disabled', true);
        $(".success-head-notify").html("No Record Found");
        if (jsonvalue.invalidRecord.length > 0) {
            $('.nav-tabs a[href="#profile"]').tab('show');
        }
    }
}
var validcname_col = false;
var cname_col = false;
function dynamic_append_bulk_invalidtbl(data, id_data) {
//    alert("4")
    id_data++;
    var add_data = "";
    add_data += `<tr><td class='text-center'>` + id_data + `</td>`;
    add_data += `<td>` + checkNullval(data.fname) + `</td>`;
    add_data += `<td>` + checkNullval(data.lname) + `</td>`;
    // add_data += `<td>` + checkNullval(data.dob) + `</td>`;
    add_data += `<td>` + checkNullval(data.dor) + `</td>`;
    add_data += `<td>` + checkNullval(data.department) + `</td>`;
    add_data += `<td>` + checkNullval(data.designation) + `</td>`;
    add_data += `<td>` + checkNullval(data.mail) + `</td>`;
    add_data += `<td>` + checkNullval(data.mobile) + `</td>`;
    add_data += `<td>` + checkNullval(data.state) + `</td>`;
//    if (cname_col) {
//        add_data += `<td>` + checkNullval(data.cname) + `</td>`;
//    }
    add_data += `<td>` + data.errorMessage + `</td>`;
    add_data += `<td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="editRecordByBulkUserId(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="deleteRecordByBulkUserId(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}
function dynamic_append_bulk_validtbl(data, id_data) {
//    alert("3")
    id_data++;
    var add_data = "";
    add_data += `<tr><td class='text-center'>` + id_data + `</td>`;
    add_data += `<td>` + checkNullval(data.fname) + `</td>`;
    add_data += `<td>` + checkNullval(data.lname) + `</td>`;
    //add_data += `<td>` + checkNullval(data.dob) + `</td>`;
    add_data += `<td>` + checkNullval(data.dor) + `</td>`;
    add_data += `<td>` + checkNullval(data.department) + `</td>`;
    add_data += `<td>` + checkNullval(data.designation) + `</td>`;
    add_data += `<td>` + checkNullval(data.mail) + `</td>`;
    add_data += `<td>` + checkNullval(data.mobile) + `</td>`;
    add_data += `<td>` + checkNullval(data.state) + `</td>`;


//    if (validcname_col) {
//        add_data += `<td>` + checkNullval(data.cname) + `</td>`;
//    }
    add_data += `<td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="editRecordByBulkUserId(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="deleteRecordByBulkUserId(` + data.id + `, 'mainpage', ` + data.campaign_id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}
function dynamic_append_final_validtb2(data, id_data) {
//    alert("3")
    id_data++;
    var add_data = "";
    add_data += `<tr><td class='text-center'>` + id_data + `</td>`;
    add_data += `<td>` + checkNullval(data.fname) + `</td>`;
    add_data += `<td>` + checkNullval(data.lname) + `</td>`;
    //add_data += `<td>` + checkNullval(data.dob) + `</td>`;
    add_data += `<td>` + checkNullval(data.dor) + `</td>`;
    add_data += `<td>` + checkNullval(data.department) + `</td>`;
    add_data += `<td>` + checkNullval(data.designation) + `</td>`;
    add_data += `<td>` + checkNullval(data.mail) + `</td>`;
    add_data += `<td>` + checkNullval(data.mobile) + `</td>`;
    add_data += `<td>` + checkNullval(data.state) + `</td>`;
//    if (validcname_col) {
//        add_data += `<td>` + checkNullval(data.cname) + `</td>`;
//    }
    add_data += `<td class='text-center' width="15%">
        <button class="btn btn-info btn-sm dropdown" type="button" data-toggle="dropdown">Action&ensp;<i class="fa fa-angle-down"></i></button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="editRecordByBulkUserId(` + data.id + `, 'finalpopup', ` + data.campaign_id + `)">Edit</a></li>
            <li><a href="javascript:void(0);" onclick="deleteRecordByBulkUserId(` + data.id + `, 'finalpopup', ` + data.campaign_id + `)">Delete</a></li>
        </ul>
    </td></tr>`;
    return add_data;
}
function validDomForReqModify1(jsonvalue) {
    var validData = "";
    var thead = "";
    thead += `<tr><td class='text-center not-export-col'>S.No</td>`
//    if (validcname_col) {
//        thead += `<td>CNAME</td>`
//    }
    thead += `<td class='text-center not-export-col'>First Name</td>`
    thead += `<td class='text-center not-export-col'>Last Name</td>`
    //thead += `<td class='text-center not-export-col'>Date of Birth</td>`
    thead += `<td class='text-center not-export-col'>Date of Retirement</td>`
    thead += `<td class='text-center not-export-col'>Department</td>`
    thead += `<td class='text-center not-export-col'>Designation</td>`
    thead += `<td class='text-center not-export-col'>Mail ID</td>`
    thead += `<td class='text-center not-export-col'>Mobile</td>`
    thead += `<td class='text-center not-export-col'>State</td>`

    thead += `<td class='readonly text-center not-export-col'>Action</td></tr>`;
    $("#successBulkData thead").html(thead);
//    alert("4")
    if (jsonvalue != undefined && jsonvalue.validRecord != undefined && jsonvalue.validRecord.length > 0) {
        $.each(jsonvalue.validRecord, function (key, val) {
//     $.each(jsonvalue.validRecord, function (key, val) {
            validData += dynamic_append_final_validtb2(val, key);
        });
    } else if (jsonvalue != undefined && jsonvalue.length != undefined && jsonvalue.length > 0) {
        $.each(jsonvalue, function (key, val) {
//     $.each(jsonvalue.validRecord, function (key, val) {
            validData += dynamic_append_final_validtb2(val, key);
        });
    }
    $("#successBulkData").DataTable().clear().destroy();
    $("#successBulkData tbody").html(validData);
    setTimeout(function () {
        $("#successBulkData").DataTable();
    }, 5);
}

function checkNullval(data) {
    if (data === undefined || data === null || data === 'null' || data === 'N/A') {
        return "";
    }
    return data;
}

function editRecordByBulkUserId(i, comming_from, campid) {
    $(".loader").show();
    $.ajax({
        type: 'post',
        url: 'bulkEmailDataEdit',
        data: {EmailbulkDataId: i, iCampaignId: campid},
        success: function (data) {
            $("#comming_from").val(comming_from)
            var form_dom = "";
            $("#sinId").val(data.emailBean.id);
            $("#iCampaignIdDt").val(data.emailBean.campaignId);
            form_dom = bulkEditPopup(data.emailBean)
//            $("#migrate_pop1").val(checkNullval(data.emailBean.migration_date));
            $("#bulkdynamic-form").html("");
            $("#bulkdynamic-form").html(form_dom);
            $("#bulkUploadEditSingle").modal('show');
        }, complete: function (data) {
            $(".loader").hide();
        }
    })
}

function deleteRecordByBulkUserId(i, comming_from, campid) {
    bootbox.confirm({
        title: "Discard Campaign",
        message: "Are you sure? Do you want to delete this domain?",
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
        callback: function () {
            $.ajax({
                type: 'post',
                url: 'bulkEmailDataDelete',
                data: {EmailbulkDataId: i, iCampaignId: campid},
                success: function (data) {
                    if (data.bulkData.Error) {
                        bootbox.alert(data.bulkData.Error);
                    } else {
                        if (!jQuery.isEmptyObject(data.bulkData)) {
                            if (data.bulkData.invalidRecord.length < 1 && data.bulkData.validRecord.length < 1) {
                                discardRelatedCampaign(data.bulkData.formDetails.campaignId);
                                //location.reload();
                            }
                        }
                        if (comming_from !== 'mainpage') {
                            if (comming_from === 'newpopup') {
                                if (!jQuery.isEmptyObject(data.bulkData)) {
                                    validDomForReqNew(data)
                                    setTimeout(function () {
                                        bulkDomCreate(data.bulkData);
                                    }, 50);
                                }
                            } else {
                                if (!jQuery.isEmptyObject(data.bulkData)) {
                                    validDomForReqModify1(data.bulkData)
                                    setTimeout(function () {
                                        bulkDomCreate(data.bulkData);
                                    }, 50);
                                }
                            }
                        } else {
                            if (!jQuery.isEmptyObject(data.bulkData)) {
                                setTimeout(function () {
                                    bulkDomCreate(data.bulkData);
                                }, 50);
                            }
                        }
                    }
                }
            })
        }

    });
}

function bulkDomCreate(jsonvalue) {
    $("#success_bulk_record thead").html("");
    $("#success_bulk_record tbody").html("");
    $("#error_bulk_record tbody").html("");
    $("#error_bulk_record thead").html("");
    var invalidData = "";
    var validData = "";
    $('span.err-count').html(jsonvalue.invalidRecord.length);
    if (jsonvalue.invalidRecord.length > 0) {
        var thead = "";
        thead += `<tr><td class='text-center not-export-col'>S.No</td>`;
        thead += `<td class='text-center not-export-col'>First Name</td>`
        thead += `<td class='text-center not-export-col'>Last Name</td>`
        //thead += `<td class='text-center not-export-col'>Date of Birth</td>`
        thead += `<td class='text-center not-export-col'>Date of Retirement</td>`
        thead += `<td class='text-center not-export-col'>Department</td>`
        thead += `<td class='text-center not-export-col'>Designation</td>`
        thead += `<td class='text-center not-export-col'>Mail ID</td>`
        thead += `<td class='text-center not-export-col'>Mobile</td>`
        thead += `<td class='text-center not-export-col'>State</td>`
//        if (cname_col) {
//            thead += `<td>CNAME</td>`
//        }
        thead += `<td>Error Message</td><td class='text-center not-export-col'>Action</td></tr>`;
        $("#error_bulk_record thead").html(thead);
        $.each(jsonvalue.invalidRecord, function (key, val) {
            invalidData += dynamic_append_bulk_invalidtbl(val, key);
        });

        $('#error_bulk_record').DataTable().clear().destroy();
        $("#error_bulk_record tbody").html(invalidData);

        setTimeout(function () {
            $("#error_bulk_record").DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'csv'
                ], exportOptions: {
                    columns: ':visible(:not(.not-export-col))'
                }
            });
        }, 5);
    } else {
        $(".err-head-notify").html("No Record Found.")
    }
    $('span.succ-count').html(jsonvalue.validRecord.length);
    if (jsonvalue.validRecord.length > 0) {
        $("button[value=preview]").prop('disabled', false);
        var thead = "";
        thead += `<tr><td class='text-center not-export-col'>S.No</td>`
        thead += `<td class='text-center not-export-col'>First Name</td>`
        thead += `<td class='text-center not-export-col'>Last Name</td>`
        // thead += `<td class='text-center not-export-col'>Date of Birth</td>`
        thead += `<td class='text-center not-export-col'>Date of Retirement</td>`
        thead += `<td class='text-center not-export-col'>Department</td>`
        thead += `<td class='text-center not-export-col'>Designation</td>`
        thead += `<td class='text-center not-export-col'>Mail ID</td>`
        thead += `<td class='text-center not-export-col'>Mobile</td>`
        thead += `<td class='text-center not-export-col'>State</td>`
//        if (validcname_col) {
//            thead += `<td>CNAME</td>`
//        }
        thead += `<td class='text-center not-export-col'>Action</td></tr>`;
        $("#success_bulk_record thead").html(thead);
        $.each(jsonvalue.validRecord, function (key, val) {
            validData += dynamic_append_bulk_validtbl(val, key);
        });
        $("#success_bulk_record").DataTable().clear().destroy();
        $("#success_bulk_record tbody").html(validData);
        setTimeout(function () {
            var numCols = $('#example thead th').length;
            $("#success_bulk_record").DataTable({
                dom: 'Bfrtip',
                buttons: [
                    {
                        extend: 'csv',
                        exportOptions: {
                            columns: ':not(td.not-export-col)'
                        }
                    },
                ]
            });
        }, 5);
    } else {
        $("button[value=preview]").prop('disabled', true);
        $(".success-head-notify").html("No Record Found");
        if (jsonvalue.invalidRecord.length > 0) {
            $('.nav-tabs a[href="#profile"]').tab('show');
        }
    }
}
function bulkEditPopup(data) {
    var form_dom = "";
    $.each(data, function (k, val) {
        if (val !== '' && k != 'id' && k != 'migration_date' && k != 'req_' && k != "campaignId" && k != "req_other_add" && k != "errorMessage" && k.indexOf("registration") == -1) {
            form_dom += `<div class="form-group">
                 <label for="">` + namingConvention(k) + `</label>
                 <input type="text" name="` + k + `" id="` + k + `" class="form-control migration_date_pop" placeholder="Enter ` + namingConvention(k) + `" value="` + checkNullval(val) + `">
             </div>`;
        }
    });
    return form_dom;
}

function checkAccountExtendVilidity() {
//     $(".loader").show();
    if (document.getElementById("type_retired_employee").checked === true) {
        $("#serving_emp_note").hide();
        $("#details").hide();
        $("#dor_desc").hide();
        $("#dor_ext1 .emailcheck").hide();
        $(".captcha_details").hide();
        $(".preview_submit").hide();
        $("#date_of_birth").hide();
        $("#serving_emp_note").show();
        $("#retired-check-bo").show();
        $.ajax({
            type: 'get',
            url: 'fetchBoForEmployeeRetiringThisMonth',
            success: function (data) {
                if (data.boForUserRetiringThisMonth === "paid") {
                    let string_coord = data.coordsOrDas;
                    let str_array = string_coord.split(",");
                    var table = `
                        <table style="font-size:12px" class="table table-striped table-bordered table-hover">
                        <thead>
                            <tr style="font-size: 14px">
                                <th><b>S.No.</b></th>
                                <th><b>Coordinator/Delegate Admin Email</b></th>
                                <th><b>Coordinator/Delegate Admin Mobile</b></th>
                            </tr>
                        </thead>`;
                    for (var i = 0; i < str_array.length; i++) {
                        str_array[i] = str_array[i].replace(/^\s*/, "").replace(/\s*$/, "");
                        let a = str_array[i];
                        let count = i + 1;
                        let b = a.split(":");
                        table += `  <tbody>
                                                <tr>
                                                    <td> ` + count + `</td>
                                                    <td> ` + b[0] + ` </td>
                                                    <td> ` + b[1] + ` </td>
                                                </tr>
                                            </tbody>`;
                    }
                    table += ` </table>`
                    $("#tableCoordForPaid").html(table);
                    $("#retired-check-for-other").hide();
                    $("#retiringEmployeesTextMessage").html("");
                    $("#retired-check-for-paid").show();
                } else if (data.boForUserRetiringThisMonth === "error") {
                    $("#retired-check-for-paid").hide();
                    $("#retired-check-for-other").show();
                    $("#retiringEmployeesTextMessage").html(data.errorMessageRetiring);
                } else if (data.boForUserRetiringThisMonth === "outsourced") {
                    $("#retired-check-for-paid").hide();
                    $("#retired-check-for-other").show();
                    $("#retiringEmployeesTextMessage").html("As checked, you are not a government employee. Hence, Serving employee option to extend the account validity.");
                } else if (data.boForUserRetiringThisMonth === "retired") {
                    $("#retired-check-for-paid").hide();
                    $("#retired-check-for-other").show();
                    $("#retiringEmployeesTextMessage").html("As checked, you are already a retired official. Kindly logout and use <b>For Retired Officers Only</b> option at the landing page.");
                } else {
                    $("#retired-check-for-paid").hide();
                    $("#retired-check-for-other").show();
                    $("#retiringEmployeesTextMessage").html("Your request has been taken successfully. Validity of your account will be extended by the end of the current month. For any queries, you may visit <b>https://servicedesk.nic.in</b> or can raise a ticket by calling at <b>1800 111 555</b>.");
                }
            },
            complete: function () {
                $('.loader').hide();
            }
        });
    } else {
        $("#serving_emp_note").show();
        $("#retired_emp_note").hide();
        $("#details").show();
        $("#dor_desc").show();
        $("#dor_ext1 .emailcheck").show();
        $(".captcha_details").show();
        $(".preview_submit").show();
        $("#retired_name").hide();
        $("#retired_mobile").hide();
        $("#date_of_birth").show();
        $("#note_govt").hide();
        $("#retired-check-for-other").hide();
        $("#retired-check-for-paid").hide();
        $("#retiringEmployeesTextMessage").html("");
    }
}

$("#singleBulkEditBtn").click(function () {
    $('.loader').show();
//    var ref_no_uploadblkdata = $("#ref_no_uploadblkdata").val();
    var iCampaignId = $("#iCampaignIdDt").val();
    var dataObj = {emailEditData: $('#singleBulkEdit').serializeObject(), iCampaignId: iCampaignId};
    var comming_from = $("#comming_from").val();
    var data = JSON.stringify(dataObj);
    $.ajax({
        type: 'post',
        url: 'bulkEmailDataEditPost',
        data: data,
        datatype: JSON,
        contentType: 'application/json; charset=utf-8',
        success: function (data) {
            if (data.hasOwnProperty('errorMessage') || data.emailEditData.errorMessage) {
                $('.email_errorMessage').html("");
                $('.email_errorMessage').fadeIn('fast').html('<div class="alert alert-danger alert_msg">' + data.emailEditData.errorMessage + '</div>');
                $('.email_errorMessage').fadeOut(5000);
            } else {
                $("#bulkUploadEditSingle").modal('hide');
                if (comming_from !== 'mainpage') {
//                    alert("7")
                    validDomForReqModify1(data.bulkData)
                    setTimeout(function () {
                        bulkDomCreate(data.bulkData);
                    }, 50);
                } else {
//                    alert("8")
                    if (!jQuery.isEmptyObject(data.bulkData)) {
                        setTimeout(function () {
                            bulkDomCreate(data.bulkData);
                        }, 50);
                    }
                }
            }
        },
        complete: function () {
            $('.loader').hide();
        }
    });
});

$('input[name="nkn_id_type"]').click(function () {
    var field_form = $(this).closest('form').attr('id');
    var user_type = "";
    var emp_detail = "";
    var formType = $('input[name="form_name"]:checked').val();
    var id_type = $("#" + field_form + ' input[name="nkn_id_type"]:checked').val();
    user_type = $("#" + field_form + ' input[name="nkn_req_for"]:checked').val();
    fetchDomaindatafunction(formType, emp_detail, id_type, user_type);
});

$('input[name="nkn_bulk_id_type"]').click(function () {
    var field_form = $(this).closest('form').attr('id');
    var user_type = "";
    var emp_detail = "";
    var formType = $('input[name="form_name"]:checked').val();
    var id_type = $("#" + field_form + ' input[name="nkn_bulk_id_type"]:checked').val();
    user_type = $("#" + field_form + ' input[name="nkn_bulk_req_for"]:checked').val();
    fetchDomaindatafunction(formType, emp_detail, id_type, user_type);
});


$("input").click(function () {
   var input_filed = $(this).closest('input').attr('id');
   if(input_filed != null || input_filed != undefined) {
       $('#'+input_filed+'_err').html("");
       document.getElementById(input_filed).focus();
   }
});

